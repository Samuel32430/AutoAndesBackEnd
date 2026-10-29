package pe.edu.upeu.AutoAndes.dto.vehiculo;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehiculoRequestDTO {

    @NotBlank(message = "La placa es obligatoria")
    @Pattern(regexp = "^[A-Z]{3}-\\d{3}$", message = "La placa debe tener el formato ABC-123")
    private String placa;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 40, message = "La marca no puede superar los 40 caracteres")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 40, message = "El modelo no puede superar los 40 caracteres")
    private String modelo;

    @NotNull(message = "El año es obligatorio")
    @Min(value = 1990, message = "El año debe estar entre 1990 y 2027")
    @Max(value = 2027, message = "El año debe estar entre 1990 y 2027")
    private Integer anio;

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;
}
