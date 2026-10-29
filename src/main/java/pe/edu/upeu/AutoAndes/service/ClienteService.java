package pe.edu.upeu.AutoAndes.service;

import pe.edu.upeu.AutoAndes.dto.cliente.ClienteResponseDTO;

import java.util.List;

public interface ClienteService {
    List<ClienteResponseDTO> listarTodos();
    ClienteResponseDTO obtenerPorId(Long id);
}
