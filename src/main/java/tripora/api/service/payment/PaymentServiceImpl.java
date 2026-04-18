package tripora.api.service.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tripora.api.Util.security.SecurityUtil;
import tripora.api.domain.Invoice;
import tripora.api.domain.Payment;
import tripora.api.dto.PaymentRequest;
import tripora.api.dto.PaymentResponse;
import tripora.api.exception.ConflictException;
import tripora.api.exception.ResourceNotFoundException;
import tripora.api.mapper.PaymentMapper;
import tripora.api.repository.InvoiceRepository;
import tripora.api.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponse pay(Integer invoiceId, PaymentRequest req) {

        if (req.amountPaid() == null || req.amountPaid().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ConflictException("Invalid payment amount");
        }

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if ("PAID".equals(invoice.getStatus())) {
            throw new ConflictException("Invoice already paid");
        }

        BigDecimal grandTotal = invoice.getGrandTotal();

        if (req.amountPaid().compareTo(grandTotal) != 0) {
            throw new ConflictException(
                    "Payment must be exactly " + grandTotal
            );
        }

        Payment payment = new Payment();
        payment.setInvoice(invoice);
        payment.setAmountPaid(req.amountPaid());

        payment.setPaymentMethod(req.paymentMethod());

        payment.setTransactionRef(req.transactionRef());
        payment.setStatus("SUCCESS");
        payment.setPaidAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        invoice.setStatus("PAID");
        invoiceRepository.save(invoice);

        return paymentMapper.mapToPaymentResponse(saved);
    }
    @Override
    public PaymentResponse getPayments(Integer invoiceId) {

        Integer userId = SecurityUtil.getCurrentUserId();

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        if (!invoice.getBooking().getUser().getId().equals(userId)) {
            throw new ConflictException("Not your invoice");
        }

        Payment payment = paymentRepository.findByInvoiceId(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        return paymentMapper.mapToPaymentResponse(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAllByOrderByPaidAtDesc()
                .stream()
                .map(paymentMapper::mapToPaymentResponse)
                .toList();
    }
}