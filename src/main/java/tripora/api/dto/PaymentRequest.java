package tripora.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import tripora.api.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(

        @NotNull
        @Positive
        BigDecimal amountPaid,

        @NotNull
        PaymentMethod paymentMethod,

        String transactionRef
) {}