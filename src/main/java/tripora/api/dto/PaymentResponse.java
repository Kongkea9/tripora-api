package tripora.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Integer id,
        BigDecimal amountPaid,
        String paymentMethod,
        String status,
        LocalDateTime paidAt
) {}