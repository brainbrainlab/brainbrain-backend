package site.brainbrain.iqtest.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.brainbrain.iqtest.controller.dto.PaymentConfirmResponse;
import site.brainbrain.iqtest.service.payment.PaymentService;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(@Qualifier("nicePaymentService") final PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payments/confirm")
    public ResponseEntity<PaymentConfirmResponse> confirm(@RequestParam final Map<String, String> params) {
        final PaymentConfirmResponse response = paymentService.pay(params);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("https://brainbrain.site/"))
                .body(response);
    }
}
