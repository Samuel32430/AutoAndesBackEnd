package pe.edu.upeu.AutoAndes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upeu.AutoAndes.dto.auth.LoginRequestDTO;
import pe.edu.upeu.AutoAndes.dto.auth.LoginResponseDTO;
import pe.edu.upeu.AutoAndes.dto.auth.UsuarioResponseDTO;
import pe.edu.upeu.AutoAndes.service.AuthService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints de login y perfil de usuario")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario y obtener token JWT")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO respuesta = authService.login(dto);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/me")
    @Operation(summary = "Obtener datos del usuario actualmente autenticado")
    public ResponseEntity<UsuarioResponseDTO> me(Principal principal) {
        String username = principal != null ? principal.getName() : "";
        UsuarioResponseDTO respuesta = authService.obtenerUsuarioActual(username);
        return ResponseEntity.ok(respuesta);
    }
}
