package tripora.api.mapper;

import org.springframework.stereotype.Component;
import tripora.api.domain.Payment;
import tripora.api.dto.PaymentResponse;


@Component
public class PaymentMapper {

    public PaymentResponse mapToPaymentResponse(Payment p) {
        return new PaymentResponse(
                p.getId(),
                p.getAmountPaid(),
                p.getPaymentMethod().toString(),
                p.getStatus(),
                p.getPaidAt()
        );
    }

}
