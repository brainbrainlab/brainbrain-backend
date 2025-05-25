package site.brainbrain.iqtest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NicePaymentCallbackRequest;
import site.brainbrain.iqtest.service.payment.PaymentService;

@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final PaymentService<NicePaymentCallbackRequest> paymentService;

    @PostMapping("/payments/confirm")
    public ResponseEntity<Void> confirmPayment(@RequestBody final NicePaymentCallbackRequest request) {
        paymentService.pay(request);
        return ResponseEntity.ok().build();
    }
}
