package tripora.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import tripora.api.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String transactionRef;

    @Column(nullable = false)
    private String status;

    private LocalDateTime paidAt;
}