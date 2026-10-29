package pe.edu.upeu.AutoAndes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AutoAndes.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByDocumento(String documento);
}
