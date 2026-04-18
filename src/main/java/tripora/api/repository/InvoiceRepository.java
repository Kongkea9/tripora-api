package tripora.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tripora.api.domain.Invoice;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {

    Optional<Invoice> findByBookingId(Integer bookingId);

    List<Invoice> findAllByBookingUserId(Integer userId);

    List<Invoice> findAllByStatus(String status);
}
