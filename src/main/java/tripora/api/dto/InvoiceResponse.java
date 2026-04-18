package tripora.api.dto;

import java.math.BigDecimal;

public record InvoiceResponse(
        Integer id,
        Integer bookingId,
        String invoiceNumber,
        BigDecimal subtotal,
        BigDecimal taxAmount,
        BigDecimal grandTotal,
        String status
) {}