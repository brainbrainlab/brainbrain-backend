package site.brainbrain.iqtest.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.controller.dto.PaymentConfirmResponse;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NicePaymentCallbackRequest;
import site.brainbrain.iqtest.service.payment.NicePaymentService;
import site.brainbrain.iqtest.service.payment.PaymentService;

@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final NicePaymentService paymentService;

//    @PostMapping("/payments/confirm")
//    public ResponseEntity<PaymentConfirmResponse> confirmPayment(@RequestBody final NicePaymentCallbackRequest request) {
//        paymentService.pay(request);
//        return ResponseEntity.ok(new PaymentConfirmResponse(request.orderId()));
//    }

    @PostMapping("/payments/confirm")
    public ResponseEntity<?> confirm(@RequestParam Map<String, String> params) {
        params.forEach((k, v) -> System.out.println(k + " = " + v));
        return ResponseEntity.ok().build();
    }
}
