package pe.edu.upeu.AutoAndes.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {

    private String token;
    @Builder.Default
    private String tipo = "Bearer";
    private String username;
    private String nombreCompleto;
    private String rol;
    private List<String> permisos;
}
