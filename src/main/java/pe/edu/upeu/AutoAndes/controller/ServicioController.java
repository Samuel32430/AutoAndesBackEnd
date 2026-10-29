package pe.edu.upeu.AutoAndes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upeu.AutoAndes.dto.servicio.ServicioRequestDTO;
import pe.edu.upeu.AutoAndes.dto.servicio.ServicioResponseDTO;
import pe.edu.upeu.AutoAndes.service.ServicioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/servicios")
@RequiredArgsConstructor
@Tag(name = "Servicios", description = "Endpoints de catálogo y gestión de servicios")
public class ServicioController {

    private final ServicioService servicioService;

    @GetMapping
    @Operation(summary = "Listar todos los servicios (activos e inactivos)")
    public ResponseEntity<List<ServicioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(servicioService.listarTodos());
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar únicamente los servicios activos")
    public ResponseEntity<List<ServicioResponseDTO>> listarActivos() {
        return ResponseEntity.ok(servicioService.listarActivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un servicio por su ID")
    public ResponseEntity<ServicioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo servicio (RN-01)")
    public ResponseEntity<ServicioResponseDTO> crear(@Valid @RequestBody ServicioRequestDTO dto) {
        ServicioResponseDTO creado = servicioService.crear(dto);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar servicio existente por su ID")
    public ResponseEntity<ServicioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ServicioRequestDTO dto) {
        return ResponseEntity.ok(servicioService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Alternar estado activo/inactivo (Soft-delete)")
    public ResponseEntity<ServicioResponseDTO> cambiarEstado(@PathVariable Long id) {
        return ResponseEntity.ok(servicioService.cambiarEstado(id));
    }
}
