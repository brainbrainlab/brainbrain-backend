package site.brainbrain.iqtest.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import site.brainbrain.iqtest.controller.dto.CreateEmailResultRequest;
import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.service.ResultService;
import site.brainbrain.iqtest.service.payment.PaymentService;

@RestController
public class ResultController {

    private final PaymentService paymentService;
    private final ResultService resultService;

    public ResultController(@Qualifier("nicePaymentService") final PaymentService paymentService, final ResultService resultService) {
        this.paymentService = paymentService;
        this.resultService = resultService;
    }

    @PostMapping("/results")
    public void create(@RequestBody final CreateResultRequest request) {
        final PurchaseOption purchaseOption = paymentService.getPurchaseOptionByOrderId(request.orderId());
        resultService.createResult(request, purchaseOption);
    }

    @PostMapping("/results/extra-payment")
    public void createForExtraPayment(@RequestBody final CreateEmailResultRequest request) {
        final PurchaseOption purchaseOption = paymentService.getPurchaseOptionByOrderId(request.orderId());
        resultService.createOnlyEmailResult(request, purchaseOption);
    }

    @GetMapping("/check")
    public String check() {
        return "헬스 췤";
    }
}
