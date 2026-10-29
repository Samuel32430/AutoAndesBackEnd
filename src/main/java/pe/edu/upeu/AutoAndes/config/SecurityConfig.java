package pe.edu.upeu.AutoAndes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import pe.edu.upeu.AutoAndes.security.JwtAuthenticationFilter;
import pe.edu.upeu.AutoAndes.security.JwtService;
import pe.edu.upeu.AutoAndes.security.RestAccessDeniedHandler;
import pe.edu.upeu.AutoAndes.security.RestAuthenticationEntryPoint;

import static pe.edu.upeu.AutoAndes.security.Permiso.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] RUTAS_PUBLICAS = {
            "/api/v1/auth/login",
            "/api/health",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/error"
    };

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            JwtService jwtService,
            UserDetailsService userDetailsService,
            CorsConfigurationSource corsConfigurationSource,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler) throws Exception {

        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService, userDetailsService);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas y Preflight
                        .requestMatchers(RUTAS_PUBLICAS).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Clientes (requiere VEHICULO_LEER)
                        .requestMatchers("/api/v1/clientes/**").hasAuthority(VEHICULO_LEER.name())

                        // Servicios (Lectura vs Escritura)
                        .requestMatchers(HttpMethod.GET, "/api/v1/servicios/**").hasAuthority(SERVICIO_LEER.name())
                        .requestMatchers("/api/v1/servicios/**").hasAuthority(SERVICIO_ESCRIBIR.name())

                        // Vehículos (Lectura vs Escritura)
                        .requestMatchers(HttpMethod.GET, "/api/v1/vehiculos/**").hasAuthority(VEHICULO_LEER.name())
                        .requestMatchers("/api/v1/vehiculos/**").hasAuthority(VEHICULO_ESCRIBIR.name())

                        // Órdenes de Servicio (Registro vs Anulación vs Lectura)
                        .requestMatchers(HttpMethod.POST, "/api/v1/ordenes").hasAuthority(ORDEN_REGISTRAR.name())
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/ordenes/*/anular").hasAuthority(ORDEN_ANULAR.name())
                        .requestMatchers("/api/v1/ordenes/**").hasAuthority(ORDEN_LEER.name())

                        // Reportes
                        .requestMatchers("/api/v1/reportes/**").hasAuthority(REPORTE_VER.name())

                        // Cualquier otra petición exige autenticación (ej. /auth/me)
                        .anyRequest().authenticated()
                )
                .exceptionHandling(errores -> errores
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
