package site.brainbrain.iqtest.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.service.CertificateService;
import site.brainbrain.iqtest.service.EmailService;
import site.brainbrain.iqtest.service.ScoreService;
import site.brainbrain.iqtest.service.payment.PaymentService;

@RestController
public class ResultController {

    private final PaymentService paymentService;
    private final CertificateService certificateService;
    private final ScoreService scoreService;
    private final EmailService emailService;

    public ResultController(@Qualifier("nicePaymentService") final PaymentService paymentService,
                            final CertificateService certificateService,
                            final ScoreService scoreService,
                            final EmailService emailService) {
        this.paymentService = paymentService;
        this.certificateService = certificateService;
        this.scoreService = scoreService;
        this.emailService = emailService;
    }

    @PostMapping("/results")
    public void create(@RequestBody final CreateResultRequest request) {
        final PurchaseOption purchaseOption = paymentService.getPurchaseOptionByOrderId(request.orderId());
        final List<Integer> answers = request.answers();

        final var scoreResult = scoreService.calculate(answers);
        final String name = request.userInfoRequest().name();

        if (containsCertificate(purchaseOption)) {
            final ByteArrayOutputStream certificate = certificateService.generate(name, scoreResult);
            emailService.send(request.userInfoRequest().email(), name, certificate);
        }
    }

    private boolean containsCertificate(final PurchaseOption purchaseOption) {
        return purchaseOption == PurchaseOption.STANDARD || purchaseOption == PurchaseOption.PREMIUM;
    }

    @GetMapping("/check")
    public String check() {
        return "헬스 췤";
    }
}
