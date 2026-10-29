package pe.edu.upeu.AutoAndes.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AutoAndes.dto.orden.*;
import pe.edu.upeu.AutoAndes.entity.DetalleOrden;
import pe.edu.upeu.AutoAndes.entity.OrdenServicio;
import pe.edu.upeu.AutoAndes.entity.Servicio;
import pe.edu.upeu.AutoAndes.entity.Vehiculo;
import pe.edu.upeu.AutoAndes.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AutoAndes.exception.ReglaNegocioException;
import pe.edu.upeu.AutoAndes.mapper.OrdenServicioMapper;
import pe.edu.upeu.AutoAndes.repository.OrdenServicioRepository;
import pe.edu.upeu.AutoAndes.repository.ServicioRepository;
import pe.edu.upeu.AutoAndes.repository.VehiculoRepository;
import pe.edu.upeu.AutoAndes.service.OrdenServicioService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenServicioServiceImpl implements OrdenServicioService {

    private final OrdenServicioRepository ordenServicioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ServicioRepository servicioRepository;
    private final OrdenServicioMapper ordenServicioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<OrdenServicioResponseDTO> buscar(String placa, String estado, LocalDateTime desde, LocalDateTime hasta) {
        String placaParam = (placa != null && !placa.trim().isEmpty()) ? placa.trim() : null;
        String estadoParam = (estado != null && !estado.trim().isEmpty()) ? estado.trim() : null;

        return ordenServicioRepository.buscarOrdenes(placaParam, estadoParam, desde, hasta)
                .stream()
                .map(ordenServicioMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrdenServicioResponseDTO obtenerPorId(Long id) {
        OrdenServicio orden = ordenServicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden de servicio no encontrada con ID: " + id));
        return ordenServicioMapper.toDTO(orden);
    }

    @Override
    public OrdenServicioResponseDTO registrar(OrdenServicioRequestDTO dto, String username) {
        Vehiculo vehiculo = vehiculoRepository.findById(dto.getVehiculoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo no encontrado con ID: " + dto.getVehiculoId()));

        // RN-04: Kilometraje estrictamente >= al último registrado en órdenes activas
        ordenServicioRepository.findFirstByVehiculoIdAndEstadoOrderByIdDesc(vehiculo.getId(), "REGISTRADA")
                .ifPresent(ultimaOrden -> {
                    if (dto.getKilometraje() < ultimaOrden.getKilometraje()) {
                        throw new ReglaNegocioException(String.format(
                                "El kilometraje ingresado (%d km) no puede ser menor al último kilometraje registrado en la orden previa (%d km)",
                                dto.getKilometraje(), ultimaOrden.getKilometraje()
                        ));
                    }
                });

        // RN-03: No duplicar servicios y validar activos
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new ReglaNegocioException("La orden debe contener al menos un detalle de servicio");
        }

        Set<Long> serviciosProcesados = new HashSet<>();
        List<DetalleOrden> detalles = new ArrayList<>();
        BigDecimal totalCalculado = BigDecimal.ZERO;

        for (DetalleOrdenRequestDTO detalleDTO : dto.getDetalles()) {
            if (!serviciosProcesados.add(detalleDTO.getServicioId())) {
                throw new ReglaNegocioException("No se permite incluir servicios duplicados en la misma orden de servicio");
            }

            Servicio servicio = servicioRepository.findById(detalleDTO.getServicioId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Servicio no encontrado con ID: " + detalleDTO.getServicioId()));

            if (servicio.getActivo() == null || servicio.getActivo() != 1) {
                throw new ReglaNegocioException(String.format(
                        "El servicio '%s' (%s) se encuentra inactivo y no puede asignarse a una orden",
                        servicio.getNombre(), servicio.getCodigo()
                ));
            }

            if (detalleDTO.getCantidad() < 1 || detalleDTO.getCantidad() > 5) {
                throw new ReglaNegocioException("La cantidad permitida por cada servicio debe estar entre 1 y 5");
            }

            BigDecimal precioUnitario = servicio.getPrecio();
            BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));
            totalCalculado = totalCalculado.add(subtotal);

            DetalleOrden detalle = new DetalleOrden();
            detalle.setServicio(servicio);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(precioUnitario);
            detalle.setSubtotal(subtotal);

            detalles.add(detalle);
        }

        // Generar correlativo OS-XXXXXX
        String nuevoNumero = generarCorrelativo();

        OrdenServicio orden = new OrdenServicio();
        orden.setNumero(nuevoNumero);
        orden.setFecha(LocalDateTime.now());
        orden.setVehiculo(vehiculo);
        orden.setKilometraje(dto.getKilometraje());
        orden.setEstado("REGISTRADA");
        orden.setTotal(totalCalculado);
        orden.setUsuarioRegistro(username != null ? username : "SISTEMA");

        for (DetalleOrden detalle : detalles) {
            orden.agregarDetalle(detalle);
        }

        OrdenServicio guardada = ordenServicioRepository.save(orden);
        return ordenServicioMapper.toDTO(guardada);
    }

    @Override
    public OrdenServicioResponseDTO anular(Long id, AnularOrdenRequestDTO dto, String username) {
        OrdenServicio orden = ordenServicioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Orden de servicio no encontrada con ID: " + id));

        if ("ANULADA".equalsIgnoreCase(orden.getEstado())) {
            throw new ReglaNegocioException("La orden de servicio ya se encuentra anulada");
        }

        // RN-05: Solo se anula el mismo día calendario
        if (!orden.getFecha().toLocalDate().isEqual(LocalDate.now())) {
            throw new ReglaNegocioException("Solo se permite anular órdenes emitidas en el mismo día calendario de registro");
        }

        orden.setEstado("ANULADA");
        orden.setMotivoAnulacion(dto.getMotivo().trim());

        OrdenServicio actualizada = ordenServicioRepository.save(orden);
        return ordenServicioMapper.toDTO(actualizada);
    }

    private synchronized String generarCorrelativo() {
        return ordenServicioRepository.findTopByOrderByIdDesc()
                .map(ultima -> {
                    try {
                        String numStr = ultima.getNumero();
                        if (numStr != null && numStr.startsWith("OS-")) {
                            long val = Long.parseLong(numStr.substring(3));
                            return String.format("OS-%06d", val + 1);
                        }
                    } catch (Exception ignored) {
                    }
                    return String.format("OS-%06d", ultima.getId() + 1);
                })
                .orElse("OS-000001");
    }
}
