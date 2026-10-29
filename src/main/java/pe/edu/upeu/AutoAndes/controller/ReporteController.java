package pe.edu.upeu.AutoAndes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upeu.AutoAndes.dto.reporte.IngresoPorServicioDTO;
import pe.edu.upeu.AutoAndes.service.ReporteService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Reportes analíticos de ingresos y servicios")
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/ingresos-por-servicio")
    @Operation(summary = "Reporte de ingresos acumulados agrupados por servicio (excluye órdenes anuladas)")
    public ResponseEntity<List<IngresoPorServicioDTO>> ingresosPorServicio(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return ResponseEntity.ok(reporteService.obtenerIngresosPorServicio(desde, hasta));
    }
}
