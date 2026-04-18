package tripora.api.mapper;

import org.springframework.stereotype.Component;
import tripora.api.domain.Invoice;
import tripora.api.dto.InvoiceResponse;


@Component
public class InvoiceMapper {

    public InvoiceResponse mapToInvoiceResponse(Invoice i) {
        return new InvoiceResponse(
                i.getId(),
                i.getBooking().getId(),
                i.getInvoiceNumber(),
                i.getSubtotal(),
                i.getTaxAmount(),
                i.getGrandTotal(),
                i.getStatus()
        );
    }

}
