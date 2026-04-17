package tripora.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "transport_options")
@Getter
@Setter
@NoArgsConstructor
public class TransportOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String vehicleType;

    @Column(nullable = false)
    private Integer minGuests;

    @Column(nullable = false)
    private Integer maxGuests;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal groupPrice;

    @Column(length = 500)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;
}