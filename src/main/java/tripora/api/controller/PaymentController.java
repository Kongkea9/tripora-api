package tripora.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tripora.api.dto.PaymentRequest;
import tripora.api.dto.PaymentResponse;
import tripora.api.service.payment.PaymentService;

import java.util.List;


@RestController
@RequestMapping("v1/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/user/invoices/{invoiceId}/pay")
    public PaymentResponse pay(
            @PathVariable Integer invoiceId,
            @Valid @RequestBody PaymentRequest req
    ) {
        return paymentService.pay(invoiceId, req);
    }

    @GetMapping("/user/invoices/{invoiceId}")
    public PaymentResponse getUserPayments(
            @PathVariable Integer invoiceId
    ) {
        return paymentService.getPayments(invoiceId);
    }
    
    @GetMapping("/admin/invoices/{invoiceId}")
    public PaymentResponse getAdminPayments(
            @PathVariable Integer invoiceId
    ) {
        return paymentService.getPayments(invoiceId);
    }

    @GetMapping("/admin")
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }
}