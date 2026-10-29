package pe.edu.upeu.AutoAndes.dto.orden;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicioRequestDTO {

    @NotNull(message = "El vehículo es obligatorio")
    private Long vehiculoId;

    @NotNull(message = "El kilometraje es obligatorio")
    @Min(value = 0, message = "El kilometraje no puede ser negativo")
    private Integer kilometraje;

    @NotEmpty(message = "La orden debe contener al menos un detalle")
    @Size(min = 1, max = 10, message = "Una orden debe tener entre 1 y 10 detalles")
    @Valid
    private List<DetalleOrdenRequestDTO> detalles;
}
