package pe.edu.upeu.AutoAndes.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pe.edu.upeu.AutoAndes.entity.Rol;

import java.util.*;

public final class PermisosPorRol {

    private static final Map<String, Set<Permiso>> PERMISOS = Map.of(
            "ADMIN", Collections.unmodifiableSet(EnumSet.allOf(Permiso.class)),
            "ASESOR", Collections.unmodifiableSet(EnumSet.of(
                    Permiso.SERVICIO_LEER,
                    Permiso.VEHICULO_LEER,
                    Permiso.VEHICULO_ESCRIBIR,
                    Permiso.ORDEN_LEER,
                    Permiso.ORDEN_REGISTRAR
            )),
            "JEFE_TALLER", Collections.unmodifiableSet(EnumSet.of(
                    Permiso.SERVICIO_LEER,
                    Permiso.SERVICIO_ESCRIBIR,
                    Permiso.VEHICULO_LEER,
                    Permiso.ORDEN_LEER,
                    Permiso.ORDEN_ANULAR,
                    Permiso.REPORTE_VER
            ))
    );

    private PermisosPorRol() {}

    public static Set<Permiso> de(String nombreRol) {
        return PERMISOS.getOrDefault(nombreRol, Set.of());
    }

    public static List<String> permisos(Collection<Rol> roles) {
        Set<String> nombres = new TreeSet<>();
        roles.forEach(rol -> de(rol.getNombre())
                .forEach(permiso -> nombres.add(permiso.name())));
        return new ArrayList<>(nombres);
    }

    public static List<GrantedAuthority> authorities(Collection<Rol> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.stream()
                .map(Rol::getNombre)
                .sorted()
                .forEach(nombre -> authorities.add(new SimpleGrantedAuthority("ROLE_" + nombre)));

        permisos(roles).forEach(permiso ->
                authorities.add(new SimpleGrantedAuthority(permiso)));

        return authorities;
    }
}
