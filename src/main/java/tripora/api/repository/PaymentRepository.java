package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tripora.api.domain.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByInvoiceId(Integer invoiceId);


    @Query("SELECT COALESCE(SUM(p.amountPaid),0) FROM Payment p WHERE p.invoice.id = :invoiceId")
    BigDecimal sumPaid(@Param("invoiceId") Integer invoiceId);
    List<Payment> findAllByOrderByPaidAtDesc();
}