package pe.edu.upeu.AutoAndes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AutoAndes.dto.orden.DetalleOrdenResponseDTO;
import pe.edu.upeu.AutoAndes.dto.orden.OrdenServicioResponseDTO;
import pe.edu.upeu.AutoAndes.entity.DetalleOrden;
import pe.edu.upeu.AutoAndes.entity.OrdenServicio;

import java.util.Collections;
import java.util.List;

@Component
public class OrdenServicioMapper {

    public OrdenServicioResponseDTO toDTO(OrdenServicio o) {
        if (o == null) return null;

        OrdenServicioResponseDTO dto = new OrdenServicioResponseDTO();
        dto.setId(o.getId());
        dto.setNumero(o.getNumero());
        dto.setFecha(o.getFecha());

        if (o.getVehiculo() != null) {
            dto.setVehiculoId(o.getVehiculo().getId());
            dto.setVehiculoPlaca(o.getVehiculo().getPlaca());
            if (o.getVehiculo().getCliente() != null) {
                dto.setClienteNombre(o.getVehiculo().getCliente().getNombres());
            }
        }

        dto.setKilometraje(o.getKilometraje());
        dto.setEstado(o.getEstado());
        dto.setTotal(o.getTotal());
        dto.setUsuarioRegistro(o.getUsuarioRegistro());
        dto.setMotivoAnulacion(o.getMotivoAnulacion());

        if (o.getDetalles() != null) {
            List<DetalleOrdenResponseDTO> detallesDTO = o.getDetalles().stream()
                    .map(this::toDetalleDTO)
                    .toList();
            dto.setDetalles(detallesDTO);
        } else {
            dto.setDetalles(Collections.emptyList());
        }

        return dto;
    }

    public DetalleOrdenResponseDTO toDetalleDTO(DetalleOrden d) {
        if (d == null) return null;
        DetalleOrdenResponseDTO dto = new DetalleOrdenResponseDTO();
        dto.setId(d.getId());
        if (d.getServicio() != null) {
            dto.setServicioId(d.getServicio().getId());
            dto.setServicioCodigo(d.getServicio().getCodigo());
            dto.setServicioNombre(d.getServicio().getNombre());
        }
        dto.setCantidad(d.getCantidad());
        dto.setPrecioUnitario(d.getPrecioUnitario());
        dto.setSubtotal(d.getSubtotal());
        return dto;
    }
}
