package pe.edu.upeu.AutoAndes.mapper;

import org.springframework.stereotype.Component;
import pe.edu.upeu.AutoAndes.dto.auth.UsuarioResponseDTO;
import pe.edu.upeu.AutoAndes.entity.Rol;
import pe.edu.upeu.AutoAndes.entity.Usuario;
import pe.edu.upeu.AutoAndes.security.PermisosPorRol;

import java.util.List;

@Component
public class UsuarioMapper {

    public UsuarioResponseDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;

        List<String> permisos = PermisosPorRol.permisos(usuario.getRoles());
        String rolPrincipal = usuario.getRoles().stream()
                .findFirst()
                .map(Rol::getNombre)
                .orElse("SIN_ROL");

        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(usuario.getNombrecompleto())
                .rol(rolPrincipal)
                .activo(usuario.getActivo() != null && usuario.getActivo() == 1)
                .permisos(permisos)
                .build();
    }
}
