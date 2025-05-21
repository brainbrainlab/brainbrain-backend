package site.brainbrain.iqtest.controller;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.infrastructure.payment.nice.dto.NicePaymentCallbackRequest;
import site.brainbrain.iqtest.service.CertificateService;
import site.brainbrain.iqtest.service.EmailService;
import site.brainbrain.iqtest.service.ScoreService;
import site.brainbrain.iqtest.service.payment.PaymentService;

@RestController
@RequestMapping
public class ResultController {

    @Qualifier("nicePaymentService")
    private final PaymentService<NicePaymentCallbackRequest> paymentService;
    private final CertificateService certificateService;
    private final ScoreService scoreService;
    private final EmailService emailService;

    public ResultController(@Qualifier("nicePaymentService") final PaymentService<NicePaymentCallbackRequest> paymentService,
                            final CertificateService certificateService,
                            final ScoreService scoreService,
                            final EmailService emailService) {
        this.paymentService = paymentService;
        this.certificateService = certificateService;
        this.scoreService = scoreService;
        this.emailService = emailService;
    }

    @PostMapping("/")
    public void create(@RequestBody final CreateResultRequest request) {
        paymentService.pay(request.nicePaymentCallbackRequest());
        Integer score = scoreService.calculate(request);
        String name = request.name();

        final ByteArrayOutputStream certificate = certificateService.generate(name, score);
        emailService.send(request.email(), name, certificate);
    }

    @GetMapping("/check")
    public String check() {
        return "헬스 췤";
    }
}
