package site.brainbrain.iqtest.controller;

import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import site.brainbrain.iqtest.controller.dto.PaymentConfirmResponse;
import site.brainbrain.iqtest.service.CouponService;
import site.brainbrain.iqtest.service.payment.PaymentService;

@RestController
public class PaymentController {

    private final PaymentService paymentService;
    private final CouponService couponService;

    public PaymentController(@Qualifier("nicePaymentService") final PaymentService paymentService,
                             final CouponService couponService) {
        this.paymentService = paymentService;
        this.couponService = couponService;
    }

    @PostMapping("/payments/confirm")
    public ResponseEntity<PaymentConfirmResponse> confirm(@RequestParam(name = "coupon", required = false) final String coupon,
                                                          @RequestParam final Map<String, String> params) {
        couponService.tryConsumeIfPresent(coupon);

        final PaymentConfirmResponse response = paymentService.pay(params);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("https://brainbrain.site/")) // 임시 리다이렉트 경로
                .body(response);
    }
}
