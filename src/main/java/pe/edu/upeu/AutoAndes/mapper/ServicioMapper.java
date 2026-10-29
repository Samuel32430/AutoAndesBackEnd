package pe.edu.upeu.AutoAndes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AutoAndes.dto.servicio.ServicioRequestDTO;
import pe.edu.upeu.AutoAndes.dto.servicio.ServicioResponseDTO;
import pe.edu.upeu.AutoAndes.entity.Servicio;

@Component
public class ServicioMapper {

    public ServicioResponseDTO toDTO(Servicio s) {
        if (s == null) return null;
        return new ServicioResponseDTO(
                s.getId(),
                s.getCodigo(),
                s.getNombre(),
                s.getPrecio(),
                s.getDuracionMin(),
                s.getActivo()
        );
    }

    public Servicio toEntity(ServicioRequestDTO dto) {
        if (dto == null) return null;
        Servicio s = new Servicio();
        s.setCodigo(dto.getCodigo().toUpperCase().trim());
        s.setNombre(dto.getNombre().trim());
        s.setPrecio(dto.getPrecio());
        s.setDuracionMin(dto.getDuracionMin());
        s.setActivo(dto.getActivo() != null ? dto.getActivo() : 1);
        return s;
    }

    public void updateEntity(Servicio s, ServicioRequestDTO dto) {
        if (s == null || dto == null) return;
        s.setCodigo(dto.getCodigo().toUpperCase().trim());
        s.setNombre(dto.getNombre().trim());
        s.setPrecio(dto.getPrecio());
        s.setDuracionMin(dto.getDuracionMin());
        if (dto.getActivo() != null) {
            s.setActivo(dto.getActivo());
        }
    }
}
