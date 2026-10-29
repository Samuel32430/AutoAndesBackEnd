package pe.edu.upeu.AutoAndes.dto.orden;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrdenRequestDTO {

    @NotNull(message = "El servicio es obligatorio")
    private Long servicioId;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima por servicio es 1")
    @Max(value = 5, message = "La cantidad máxima por servicio es 5")
    private Integer cantidad;
}
