package pe.edu.upeu.AutoAndes.service;

import pe.edu.upeu.AutoAndes.dto.reporte.IngresoPorServicioDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ReporteService {
    List<IngresoPorServicioDTO> obtenerIngresosPorServicio(LocalDateTime desde, LocalDateTime hasta);
}
