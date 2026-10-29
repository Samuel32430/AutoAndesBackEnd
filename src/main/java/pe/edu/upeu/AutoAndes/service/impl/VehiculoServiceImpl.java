package pe.edu.upeu.AutoAndes.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AutoAndes.dto.vehiculo.VehiculoRequestDTO;
import pe.edu.upeu.AutoAndes.dto.vehiculo.VehiculoResponseDTO;
import pe.edu.upeu.AutoAndes.entity.Cliente;
import pe.edu.upeu.AutoAndes.entity.Vehiculo;
import pe.edu.upeu.AutoAndes.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AutoAndes.exception.ReglaNegocioException;
import pe.edu.upeu.AutoAndes.repository.ClienteRepository;
import pe.edu.upeu.AutoAndes.repository.OrdenServicioRepository;
import pe.edu.upeu.AutoAndes.repository.VehiculoRepository;
import pe.edu.upeu.AutoAndes.service.VehiculoService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;
    private final OrdenServicioRepository ordenServicioRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VehiculoResponseDTO> listarTodos(Long clienteId) {
        List<Vehiculo> vehiculos;
        if (clienteId != null) {
            vehiculos = vehiculoRepository.findByClienteIdConCliente(clienteId);
        } else {
            vehiculos = vehiculoRepository.findAll();
        }
        return vehiculos.stream().map(this::mapearADTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehiculoResponseDTO obtenerPorId(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo no encontrado con ID: " + id));
        return mapearADTO(vehiculo);
    }

    @Override
    public VehiculoResponseDTO crear(VehiculoRequestDTO dto) {
        String placaLimpia = dto.getPlaca().toUpperCase().trim();
        if (vehiculoRepository.existsByPlacaIgnoreCase(placaLimpia)) {
            throw new ReglaNegocioException("Ya existe un vehículo registrado con la placa: " + placaLimpia);
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + dto.getClienteId()));

        Vehiculo vehiculo = new Vehiculo();
        vehiculo.setPlaca(placaLimpia);
        vehiculo.setMarca(dto.getMarca().trim());
        vehiculo.setModelo(dto.getModelo().trim());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setCliente(cliente);

        Vehiculo guardado = vehiculoRepository.save(vehiculo);
        return mapearADTO(guardado);
    }

    @Override
    public VehiculoResponseDTO actualizar(Long id, VehiculoRequestDTO dto) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo no encontrado con ID: " + id));

        String placaLimpia = dto.getPlaca().toUpperCase().trim();
        if (vehiculoRepository.existsByPlacaIgnoreCaseAndIdNot(placaLimpia, id)) {
            throw new ReglaNegocioException("Ya existe otro vehículo registrado con la placa: " + placaLimpia);
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + dto.getClienteId()));

        vehiculo.setPlaca(placaLimpia);
        vehiculo.setMarca(dto.getMarca().trim());
        vehiculo.setModelo(dto.getModelo().trim());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setCliente(cliente);

        Vehiculo actualizado = vehiculoRepository.save(vehiculo);
        return mapearADTO(actualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Vehículo no encontrado con ID: " + id);
        }

        if (ordenServicioRepository.existsByVehiculoId(id)) {
            throw new ReglaNegocioException("No se puede eliminar el vehículo porque tiene órdenes de servicio asociadas");
        }

        vehiculoRepository.deleteById(id);
    }

    private VehiculoResponseDTO mapearADTO(Vehiculo v) {
        return new VehiculoResponseDTO(
                v.getId(),
                v.getPlaca(),
                v.getMarca(),
                v.getModelo(),
                v.getAnio(),
                v.getCliente().getId(),
                v.getCliente().getNombres()
        );
    }
}
