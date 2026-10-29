package pe.edu.upeu.AutoAndes.dto.orden;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicioResponseDTO {
    private Long id;
    private String numero;
    private LocalDateTime fecha;
    private Long vehiculoId;
    private String vehiculoPlaca;
    private String clienteNombre;
    private Integer kilometraje;
    private String estado;
    private BigDecimal total;
    private String usuarioRegistro;
    private String motivoAnulacion;
    private List<DetalleOrdenResponseDTO> detalles;
}
