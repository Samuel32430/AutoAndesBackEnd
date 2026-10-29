package pe.edu.upeu.AutoAndes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden_servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdenServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 9)
    private String numero;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Column(nullable = false)
    private Integer kilometraje;

    @Column(nullable = false, length = 12)
    private String estado;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(name = "usuario_registro", nullable = false, length = 30)
    private String usuarioRegistro;

    @Column(name = "motivo_anulacion", length = 200)
    private String motivoAnulacion;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrden> detalles = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
        if (estado == null) {
            estado = "REGISTRADA";
        }
    }

    public void agregarDetalle(DetalleOrden detalle) {
        detalles.add(detalle);
        detalle.setOrden(this);
    }
}
