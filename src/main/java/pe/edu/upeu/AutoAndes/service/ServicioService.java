package pe.edu.upeu.AutoAndes.service;

import pe.edu.upeu.AutoAndes.dto.servicio.ServicioRequestDTO;
import pe.edu.upeu.AutoAndes.dto.servicio.ServicioResponseDTO;

import java.util.List;

public interface ServicioService {
    List<ServicioResponseDTO> listarTodos();
    List<ServicioResponseDTO> listarActivos();
    ServicioResponseDTO obtenerPorId(Long id);
    ServicioResponseDTO crear(ServicioRequestDTO dto);
    ServicioResponseDTO actualizar(Long id, ServicioRequestDTO dto);
    ServicioResponseDTO cambiarEstado(Long id);
}
