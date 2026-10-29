package pe.edu.upeu.AutoAndes.service;

import pe.edu.upeu.AutoAndes.dto.orden.AnularOrdenRequestDTO;
import pe.edu.upeu.AutoAndes.dto.orden.OrdenServicioRequestDTO;
import pe.edu.upeu.AutoAndes.dto.orden.OrdenServicioResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdenServicioService {
    List<OrdenServicioResponseDTO> buscar(String placa, String estado, LocalDateTime desde, LocalDateTime hasta);
    OrdenServicioResponseDTO obtenerPorId(Long id);
    OrdenServicioResponseDTO registrar(OrdenServicioRequestDTO dto, String username);
    OrdenServicioResponseDTO anular(Long id, AnularOrdenRequestDTO dto, String username);
}
