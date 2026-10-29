package pe.edu.upeu.AutoAndes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AutoAndes.dto.vehiculo.VehiculoRequestDTO;
import pe.edu.upeu.AutoAndes.dto.vehiculo.VehiculoResponseDTO;
import pe.edu.upeu.AutoAndes.entity.Cliente;
import pe.edu.upeu.AutoAndes.entity.Vehiculo;

@Component
public class VehiculoMapper {

    public VehiculoResponseDTO toDTO(Vehiculo v) {
        if (v == null) return null;
        return new VehiculoResponseDTO(
                v.getId(),
                v.getPlaca(),
                v.getMarca(),
                v.getModelo(),
                v.getAnio(),
                v.getCliente() != null ? v.getCliente().getId() : null,
                v.getCliente() != null ? v.getCliente().getNombres() : null
        );
    }

    public Vehiculo toEntity(VehiculoRequestDTO dto, Cliente cliente) {
        if (dto == null) return null;
        Vehiculo v = new Vehiculo();
        v.setPlaca(dto.getPlaca().toUpperCase().trim());
        v.setMarca(dto.getMarca().trim());
        v.setModelo(dto.getModelo().trim());
        v.setAnio(dto.getAnio());
        v.setCliente(cliente);
        return v;
    }

    public void updateEntity(Vehiculo v, VehiculoRequestDTO dto, Cliente cliente) {
        if (v == null || dto == null) return;
        v.setPlaca(dto.getPlaca().toUpperCase().trim());
        v.setMarca(dto.getMarca().trim());
        v.setModelo(dto.getModelo().trim());
        v.setAnio(dto.getAnio());
        v.setCliente(cliente);
    }
}
