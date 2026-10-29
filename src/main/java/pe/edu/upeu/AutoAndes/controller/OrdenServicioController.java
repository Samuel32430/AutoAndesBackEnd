package pe.edu.upeu.AutoAndes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upeu.AutoAndes.dto.orden.AnularOrdenRequestDTO;
import pe.edu.upeu.AutoAndes.dto.orden.OrdenServicioRequestDTO;
import pe.edu.upeu.AutoAndes.dto.orden.OrdenServicioResponseDTO;
import pe.edu.upeu.AutoAndes.service.OrdenServicioService;

import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
@Tag(name = "Órdenes de Servicio", description = "Endpoints para el registro, consulta y anulación de órdenes")
public class OrdenServicioController {

    private final OrdenServicioService ordenServicioService;

    @GetMapping
    @Operation(summary = "Buscar órdenes de servicio con filtros opcionales (placa, estado, rango de fechas)")
    public ResponseEntity<List<OrdenServicioResponseDTO>> buscar(
            @RequestParam(required = false) String placa,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(ordenServicioService.buscar(placa, estado, desde, hasta));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una orden de servicio con sus detalles por ID")
    public ResponseEntity<OrdenServicioResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ordenServicioService.obtenerPorId(id));
    }

    @PostMapping
    @Operation(summary = "Registrar orden de servicio atómicamente con detalles (RN-03, RN-04, correlativo)")
    public ResponseEntity<OrdenServicioResponseDTO> registrar(
            @Valid @RequestBody OrdenServicioRequestDTO dto,
            Principal principal) {
        String username = principal != null ? principal.getName() : "SISTEMA";
        OrdenServicioResponseDTO creada = ordenServicioService.registrar(dto, username);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creada.getId())
                .toUri();
        return ResponseEntity.created(ubicacion).body(creada);
    }

    @PatchMapping("/{id}/anular")
    @Operation(summary = "Anular orden de servicio con motivo justificado (RN-05)")
    public ResponseEntity<OrdenServicioResponseDTO> anular(
            @PathVariable Long id,
            @Valid @RequestBody AnularOrdenRequestDTO dto,
            Principal principal) {
        String username = principal != null ? principal.getName() : "SISTEMA";
        return ResponseEntity.ok(ordenServicioService.anular(id, dto, username));
    }
}
