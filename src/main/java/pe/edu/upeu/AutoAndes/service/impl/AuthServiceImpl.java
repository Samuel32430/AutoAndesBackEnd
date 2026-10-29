package pe.edu.upeu.AutoAndes.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AutoAndes.dto.auth.LoginRequestDTO;
import pe.edu.upeu.AutoAndes.dto.auth.LoginResponseDTO;
import pe.edu.upeu.AutoAndes.dto.auth.UsuarioResponseDTO;
import pe.edu.upeu.AutoAndes.entity.Rol;
import pe.edu.upeu.AutoAndes.entity.Usuario;
import pe.edu.upeu.AutoAndes.exception.RecursoNoEncontradoException;
import pe.edu.upeu.AutoAndes.mapper.UsuarioMapper;
import pe.edu.upeu.AutoAndes.repository.UsuarioRepository;
import pe.edu.upeu.AutoAndes.security.JwtService;
import pe.edu.upeu.AutoAndes.security.PermisosPorRol;
import pe.edu.upeu.AutoAndes.security.UsuarioDetailsService;
import pe.edu.upeu.AutoAndes.service.AuthService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;
    private final UsuarioMapper usuarioMapper;

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(dto.getUsername().trim())
                .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        if (usuario.getActivo() == null || usuario.getActivo() != 1) {
            throw new DisabledException("El usuario se encuentra inactivo");
        }

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        UserDetails userDetails = usuarioDetailsService.loadUserByUsername(usuario.getUsername());
        String token = jwtService.generarToken(userDetails);

        List<String> permisos = PermisosPorRol.permisos(usuario.getRoles());
        String rolPrincipal = usuario.getRoles().stream()
                .findFirst()
                .map(Rol::getNombre)
                .orElse("SIN_ROL");

        return LoginResponseDTO.builder()
                .token(token)
                .tipo("Bearer")
                .username(usuario.getUsername())
                .nombreCompleto(usuario.getNombrecompleto())
                .rol(rolPrincipal)
                .permisos(permisos)
                .build();
    }

    @Override
    public UsuarioResponseDTO obtenerUsuarioActual(String username) {
        Usuario usuario = usuarioRepository.findByUsernameIgnoreCase(username.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado: " + username));

        return usuarioMapper.toDTO(usuario);
    }
}
