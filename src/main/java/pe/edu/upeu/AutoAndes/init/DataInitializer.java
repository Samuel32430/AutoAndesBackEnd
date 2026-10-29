package pe.edu.upeu.AutoAndes.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upeu.AutoAndes.entity.*;
import pe.edu.upeu.AutoAndes.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final ServicioRepository servicioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Verificando e inicializando datos de arranque de AutoAndes...");

        // 1. Roles
        Rol rolAdmin = rolRepository.findByNombreIgnoreCase("ADMIN")
                .orElseGet(() -> rolRepository.save(new Rol(null, "ADMIN")));
        Rol rolAsesor = rolRepository.findByNombreIgnoreCase("ASESOR")
                .orElseGet(() -> rolRepository.save(new Rol(null, "ASESOR")));
        Rol rolJefe = rolRepository.findByNombreIgnoreCase("JEFE_TALLER")
                .orElseGet(() -> rolRepository.save(new Rol(null, "JEFE_TALLER")));

        // 2. Usuarios
        crearUsuarioSiNoExiste("admin", "admin123", "Administrador Principal", 1, Set.of(rolAdmin));
        crearUsuarioSiNoExiste("asesor1", "asesor123", "Juan Pérez - Asesor", 1, Set.of(rolAsesor));
        crearUsuarioSiNoExiste("jefe1", "jefe123", "Carlos Rodríguez - Jefe de Taller", 1, Set.of(rolJefe));
        crearUsuarioSiNoExiste("inactivo", "inactivo123", "Usuario Deshabilitado", 0, Set.of(rolAsesor));

        // 3. Clientes
        if (clienteRepository.count() == 0) {
            log.info("Cargando clientes semilla...");
            clienteRepository.saveAll(List.of(
                    new Cliente(null, "45871234", "Juan Carlos Pérez Gómez", "987654321"),
                    new Cliente(null, "71234567", "María Elena Rodriguez Quispe", "984123456"),
                    new Cliente(null, "20556789123", "Transportes Andina SAC", "014234567"),
                    new Cliente(null, "40123987", "Carlos Alberto Mendoza Solís", "993456781"),
                    new Cliente(null, "20100456789", "Distribuidora del Centro EIRL", "015678901"),
                    new Cliente(null, "47890123", "Ana Lucía Torres Flores", "976543210")
            ));
        }

        // 4. Servicios
        if (servicioRepository.count() == 0) {
            log.info("Cargando servicios semilla...");
            servicioRepository.saveAll(List.of(
                    new Servicio(null, "SRV-001", "Cambio de Aceite Sintético", new BigDecimal("180.00"), 45, 1),
                    new Servicio(null, "SRV-002", "Alineamiento y Balanceo", new BigDecimal("90.00"), 60, 1),
                    new Servicio(null, "SRV-003", "Cambio de Pastillas de Freno", new BigDecimal("250.00"), 90, 1),
                    new Servicio(null, "SRV-004", "Afinamiento Electrónico", new BigDecimal("320.00"), 120, 1),
                    new Servicio(null, "SRV-005", "Diagnóstico por Escáner OBD-II", new BigDecimal("80.00"), 30, 1),
                    new Servicio(null, "SRV-006", "Recarga de Aire Acondicionado", new BigDecimal("150.00"), 45, 1),
                    new Servicio(null, "SRV-007", "Lavado y Pulido de Salón", new BigDecimal("200.00"), 180, 1),
                    new Servicio(null, "SRV-008", "Servicio Descontinuado", new BigDecimal("50.00"), 30, 0)
            ));
        }

        // 5. Vehículos
        if (vehiculoRepository.count() == 0) {
            log.info("Cargando vehículos semilla...");
            List<Cliente> clientes = clienteRepository.findAll();
            if (clientes.size() >= 6) {
                vehiculoRepository.saveAll(List.of(
                        new Vehiculo(null, "ABC-123", "Toyota", "Corolla", 2020, clientes.get(0)),
                        new Vehiculo(null, "XYZ-789", "Hyundai", "Tucson", 2021, clientes.get(0)),
                        new Vehiculo(null, "MNO-456", "Kia", "Sportage", 2019, clientes.get(1)),
                        new Vehiculo(null, "BCA-321", "Nissan", "Frontier", 2018, clientes.get(2)),
                        new Vehiculo(null, "OPQ-654", "Toyota", "Hilux", 2022, clientes.get(2)),
                        new Vehiculo(null, "DEF-234", "Volkswagen", "Gol", 2017, clientes.get(3)),
                        new Vehiculo(null, "GHI-567", "Chevrolet", "Sail", 2020, clientes.get(4)),
                        new Vehiculo(null, "JKL-890", "Mazda", "CX-5", 2023, clientes.get(5))
                ));
            }
        }

        // 6. Órdenes Semilla iniciales
        if (ordenServicioRepository.count() == 0) {
            log.info("Cargando órdenes semilla...");
            List<Vehiculo> vehiculos = vehiculoRepository.findAll();
            List<Servicio> servicios = servicioRepository.findAll();
            if (vehiculos.size() >= 4 && servicios.size() >= 7) {
                // OS-000001
                OrdenServicio os1 = new OrdenServicio();
                os1.setNumero("OS-000001");
                os1.setFecha(LocalDateTime.now().minusDays(5));
                os1.setVehiculo(vehiculos.get(0));
                os1.setKilometraje(45000);
                os1.setEstado("REGISTRADA");
                os1.setTotal(new BigDecimal("270.00"));
                os1.setUsuarioRegistro("asesor1");
                DetalleOrden d1 = new DetalleOrden(null, os1, servicios.get(0), 1, new BigDecimal("180.00"), new BigDecimal("180.00"));
                DetalleOrden d2 = new DetalleOrden(null, os1, servicios.get(1), 1, new BigDecimal("90.00"), new BigDecimal("90.00"));
                os1.getDetalles().add(d1);
                os1.getDetalles().add(d2);
                ordenServicioRepository.save(os1);

                // OS-000002
                OrdenServicio os2 = new OrdenServicio();
                os2.setNumero("OS-000002");
                os2.setFecha(LocalDateTime.now().minusDays(3));
                os2.setVehiculo(vehiculos.get(2));
                os2.setKilometraje(58000);
                os2.setEstado("REGISTRADA");
                os2.setTotal(new BigDecimal("570.00"));
                os2.setUsuarioRegistro("asesor1");
                DetalleOrden d3 = new DetalleOrden(null, os2, servicios.get(2), 1, new BigDecimal("250.00"), new BigDecimal("250.00"));
                DetalleOrden d4 = new DetalleOrden(null, os2, servicios.get(3), 1, new BigDecimal("320.00"), new BigDecimal("320.00"));
                os2.getDetalles().add(d3);
                os2.getDetalles().add(d4);
                ordenServicioRepository.save(os2);

                // OS-000003
                OrdenServicio os3 = new OrdenServicio();
                os3.setNumero("OS-000003");
                os3.setFecha(LocalDateTime.now().minusDays(1));
                os3.setVehiculo(vehiculos.get(3));
                os3.setKilometraje(85000);
                os3.setEstado("REGISTRADA");
                os3.setTotal(new BigDecimal("230.00"));
                os3.setUsuarioRegistro("admin");
                DetalleOrden d5 = new DetalleOrden(null, os3, servicios.get(4), 1, new BigDecimal("80.00"), new BigDecimal("80.00"));
                DetalleOrden d6 = new DetalleOrden(null, os3, servicios.get(5), 1, new BigDecimal("150.00"), new BigDecimal("150.00"));
                os3.getDetalles().add(d5);
                os3.getDetalles().add(d6);
                ordenServicioRepository.save(os3);

                // OS-000004 (Anulada)
                OrdenServicio os4 = new OrdenServicio();
                os4.setNumero("OS-000004");
                os4.setFecha(LocalDateTime.now().minusDays(1));
                os4.setVehiculo(vehiculos.get(5));
                os4.setKilometraje(92000);
                os4.setEstado("ANULADA");
                os4.setTotal(new BigDecimal("200.00"));
                os4.setUsuarioRegistro("asesor1");
                os4.setMotivoAnulacion("Cliente desistió por falta de tiempo para esperar el pulido");
                DetalleOrden d7 = new DetalleOrden(null, os4, servicios.get(6), 1, new BigDecimal("200.00"), new BigDecimal("200.00"));
                os4.getDetalles().add(d7);
                ordenServicioRepository.save(os4);
            }
        }

        log.info("Inicialización de datos completada exitosamente.");
    }

    private void crearUsuarioSiNoExiste(String username, String rawPassword, String nombreCompleto, Integer activo, Set<Rol> roles) {
        usuarioRepository.findByUsernameIgnoreCase(username).ifPresentOrElse(
                u -> {
                    if (!u.getPassword().startsWith("$2a$") && !u.getPassword().startsWith("$2b$")) {
                        u.setPassword(passwordEncoder.encode(rawPassword));
                        u.setNombrecompleto(nombreCompleto);
                        u.setActivo(activo);
                        u.setRoles(roles);
                        usuarioRepository.save(u);
                    }
                },
                () -> {
                    Usuario u = new Usuario();
                    u.setUsername(username);
                    u.setPassword(passwordEncoder.encode(rawPassword));
                    u.setNombrecompleto(nombreCompleto);
                    u.setActivo(activo);
                    u.setRoles(roles);
                    usuarioRepository.save(u);
                }
        );
    }
}
