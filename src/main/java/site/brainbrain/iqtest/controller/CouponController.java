package site.brainbrain.iqtest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.controller.dto.CouponResponse;
import site.brainbrain.iqtest.service.CouponService;

@RequiredArgsConstructor
@RestController
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/coupons")
    public ResponseEntity<CouponResponse> getCouponByCode(@RequestParam final String code) {
        final CouponResponse response = couponService.getCouponByCode(code);
        return ResponseEntity.ok(response);
    }
}
