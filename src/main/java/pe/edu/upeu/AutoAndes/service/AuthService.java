package pe.edu.upeu.AutoAndes.service;

import pe.edu.upeu.AutoAndes.dto.auth.LoginRequestDTO;
import pe.edu.upeu.AutoAndes.dto.auth.LoginResponseDTO;
import pe.edu.upeu.AutoAndes.dto.auth.UsuarioResponseDTO;

public interface AuthService {
    LoginResponseDTO login(LoginRequestDTO dto);
    UsuarioResponseDTO obtenerUsuarioActual(String username);
}
