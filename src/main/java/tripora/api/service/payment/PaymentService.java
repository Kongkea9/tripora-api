package tripora.api.service.payment;

import tripora.api.dto.PaymentRequest;
import tripora.api.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {
    PaymentResponse pay(Integer invoiceId, PaymentRequest req);
    PaymentResponse getPayments(Integer invoiceId);

    List<PaymentResponse> getAllPayments();
}