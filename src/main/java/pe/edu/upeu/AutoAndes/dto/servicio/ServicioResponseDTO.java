package pe.edu.upeu.AutoAndes.dto.servicio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicioResponseDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private BigDecimal precio;
    private Integer duracionMin;
    private Integer activo;
}
