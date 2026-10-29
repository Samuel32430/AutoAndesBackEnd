package pe.edu.upeu.AutoAndes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AutoAndes.entity.DetalleOrden;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Long> {
    boolean existsByServicioId(Long servicioId);
}
