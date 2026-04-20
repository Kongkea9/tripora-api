package tripora.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tripora.api.enums.PaymentMethod;

@RestController
@RequestMapping("/v1/api/enums")
public class EnumController {

    @GetMapping("/payment-methods")
    public PaymentMethod[] getPaymentMethods() {
        return PaymentMethod.values();
    }
}