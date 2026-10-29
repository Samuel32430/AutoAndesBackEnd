package pe.edu.upeu.AutoAndes.service;

import pe.edu.upeu.AutoAndes.dto.vehiculo.VehiculoRequestDTO;
import pe.edu.upeu.AutoAndes.dto.vehiculo.VehiculoResponseDTO;

import java.util.List;

public interface VehiculoService {
    List<VehiculoResponseDTO> listarTodos(Long clienteId);
    VehiculoResponseDTO obtenerPorId(Long id);
    VehiculoResponseDTO crear(VehiculoRequestDTO dto);
    VehiculoResponseDTO actualizar(Long id, VehiculoRequestDTO dto);
    void eliminar(Long id);
}
