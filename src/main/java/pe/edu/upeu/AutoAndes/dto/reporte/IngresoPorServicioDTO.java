package pe.edu.upeu.AutoAndes.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngresoPorServicioDTO {
    private String codigo;
    private String servicio;
    private Long cantidadTotal;
    private BigDecimal importeTotal;
}
