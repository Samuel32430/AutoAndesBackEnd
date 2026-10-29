package pe.edu.upeu.AutoAndes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upeu.AutoAndes.dto.vehiculo.VehiculoRequestDTO;
import pe.edu.upeu.AutoAndes.dto.vehiculo.VehiculoResponseDTO;
import pe.edu.upeu.AutoAndes.service.VehiculoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
@RequiredArgsConstructor
@Tag(name = "Vehículos", description = "Endpoints de gestión de vehículos del taller")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    @GetMapping
    @Operation(summary = "Listar vehículos (opcionalmente filtrado por clienteId)")
    public ResponseEntity<List<VehiculoResponseDTO>> listarTodos(
            @RequestParam(required = false) Long clienteId) {
        return ResponseEntity.ok(vehiculoService.listarTodos(clienteId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un vehículo por su ID")
    public ResponseEntity<VehiculoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(vehiculoService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo vehículo (RN-02)")
    public ResponseEntity<VehiculoResponseDTO> crear(@Valid @RequestBody VehiculoRequestDTO dto) {
        VehiculoResponseDTO creado = vehiculoService.crear(dto);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vehículo existente por su ID")
    public ResponseEntity<VehiculoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody VehiculoRequestDTO dto) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar vehículo (RN-02: solo si no tiene órdenes asociadas)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
