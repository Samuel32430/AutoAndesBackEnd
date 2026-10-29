package pe.edu.upeu.AutoAndes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.AutoAndes.dto.reporte.IngresoPorServicioDTO;
import pe.edu.upeu.AutoAndes.entity.OrdenServicio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrdenServicioRepository extends JpaRepository<OrdenServicio, Long> {

    boolean existsByVehiculoId(Long vehiculoId);

    Optional<OrdenServicio> findTopByOrderByIdDesc();

    Optional<OrdenServicio> findFirstByVehiculoIdAndEstadoOrderByIdDesc(Long vehiculoId, String estado);

    @Query("""
            SELECT DISTINCT o FROM OrdenServicio o
            LEFT JOIN FETCH o.vehiculo v
            LEFT JOIN FETCH v.cliente c
            LEFT JOIN FETCH o.detalles d
            LEFT JOIN FETCH d.servicio s
            WHERE (:placa IS NULL OR UPPER(v.placa) = UPPER(:placa))
              AND (:estado IS NULL OR o.estado = :estado)
              AND (:desde IS NULL OR o.fecha >= :desde)
              AND (:hasta IS NULL OR o.fecha <= :hasta)
            ORDER BY o.fecha DESC
            """)
    List<OrdenServicio> buscarOrdenes(
            @Param("placa") String placa,
            @Param("estado") String estado,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );

    @Query("""
            SELECT new pe.edu.upeu.AutoAndes.dto.reporte.IngresoPorServicioDTO(
                s.codigo, s.nombre, SUM(d.cantidad), SUM(d.subtotal))
            FROM DetalleOrden d
            JOIN d.orden o
            JOIN d.servicio s
            WHERE o.estado = 'REGISTRADA'
              AND (:desde IS NULL OR o.fecha >= :desde)
              AND (:hasta IS NULL OR o.fecha <= :hasta)
            GROUP BY s.codigo, s.nombre
            ORDER BY SUM(d.subtotal) DESC
            """)
    List<IngresoPorServicioDTO> reporteIngresosPorServicio(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );
}
