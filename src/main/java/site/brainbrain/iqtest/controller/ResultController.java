package site.brainbrain.iqtest.controller;

import java.io.ByteArrayOutputStream;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.service.CertificateService;
import site.brainbrain.iqtest.service.EmailService;
import site.brainbrain.iqtest.service.ScoreService;
import site.brainbrain.iqtest.service.payment.NicePaymentService;

@RestController
@RequiredArgsConstructor
public class ResultController {

    private final NicePaymentService nicePaymentService;
    private final CertificateService certificateService;
    private final ScoreService scoreService;
    private final EmailService emailService;

    @PostMapping("/results")
    public void create(@RequestBody final CreateResultRequest request) {
        final String goodsName = nicePaymentService.findGoodsNameByOrderId(request.orderId());
        Integer score = scoreService.calculate(request);
        String name = request.userInfoRequest().name();

        final ByteArrayOutputStream certificate = certificateService.generate(name, score);
        emailService.send(request.userInfoRequest().email(), name, certificate);
    }

    @GetMapping("/check")
    public String check() {
        return "헬스 췤";
    }
}
