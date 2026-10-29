package pe.edu.upeu.AutoAndes.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String mensaje;
    private String ruta;
    private Map<String, String> campos;

    public ErrorResponseDTO(LocalDateTime timestamp, int status, String error, String mensaje, String ruta) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.mensaje = mensaje;
        this.ruta = ruta;
    }
}
