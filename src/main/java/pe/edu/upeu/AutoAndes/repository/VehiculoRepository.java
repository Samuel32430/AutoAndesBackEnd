package pe.edu.upeu.AutoAndes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.edu.upeu.AutoAndes.entity.Vehiculo;

import java.util.List;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    boolean existsByPlacaIgnoreCase(String placa);
    boolean existsByPlacaIgnoreCaseAndIdNot(String placa, Long id);

    @Query("SELECT v FROM Vehiculo v JOIN FETCH v.cliente c WHERE (:clienteId IS NULL OR c.id = :clienteId)")
    List<Vehiculo> findByClienteIdConCliente(@Param("clienteId") Long clienteId);
}
