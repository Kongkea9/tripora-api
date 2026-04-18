package tripora.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tripora.api.dto.InvoiceResponse;
import tripora.api.service.invoice.InvoiceService;

import java.util.List;

@RestController
@RequestMapping("v1/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/mine")
    public List<InvoiceResponse> myInvoices() {
        return invoiceService.myInvoices();
    }

    @GetMapping("/mine/{id}")
    public InvoiceResponse myInvoiceDetail(@PathVariable Integer id) {
        return invoiceService.getMine(id);
    }

    @GetMapping("/admin")
    public List<InvoiceResponse> all(
            @RequestParam(required = false) String status
    ) {
        return invoiceService.getAll(status);
    }

    @GetMapping("/admin/{id}")
    public InvoiceResponse adminDetail(@PathVariable Integer id) {
        return invoiceService.getById(id);
    }


}
