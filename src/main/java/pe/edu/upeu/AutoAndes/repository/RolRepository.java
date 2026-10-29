package pe.edu.upeu.AutoAndes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upeu.AutoAndes.entity.Rol;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
    Optional<Rol> findByNombreIgnoreCase(String nombre);
}
