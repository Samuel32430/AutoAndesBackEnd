package pe.edu.upeu.AutoAndes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AutoAndes.dto.cliente.ClienteResponseDTO;
import pe.edu.upeu.AutoAndes.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Endpoints de consulta de clientes (Solo Lectura)")
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    @Operation(summary = "Listar todos los clientes")
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por su identificador")
    public ResponseEntity<ClienteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerPorId(id));
    }
}
