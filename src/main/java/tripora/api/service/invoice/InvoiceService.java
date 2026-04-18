package tripora.api.service.invoice;

import tripora.api.dto.InvoiceResponse;

import java.util.List;

public interface InvoiceService {
    InvoiceResponse getMine(Integer invoiceId);
    List<InvoiceResponse> myInvoices();
    List<InvoiceResponse> getAll(String status);
    InvoiceResponse getById(Integer id);

}