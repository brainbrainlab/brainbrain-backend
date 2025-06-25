package site.brainbrain.iqtest.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.result.ResultStrategy;
import site.brainbrain.iqtest.domain.result.ResultStrategyFactory;
import site.brainbrain.iqtest.service.payment.PaymentService;

@Slf4j
@RestController
public class ResultController {

    private final PaymentService paymentService;
    private final ResultStrategyFactory resultStrategyFactory;

    public ResultController(@Qualifier("nicePaymentService") final PaymentService paymentService,
                            final ResultStrategyFactory resultStrategyFactory) {
        this.paymentService = paymentService;
        this.resultStrategyFactory = resultStrategyFactory;
    }

    @PostMapping("/results")
    public void create(@RequestBody final CreateResultRequest request) {
        log.info(request.toString());

        final PurchaseOption purchaseOption = paymentService.getPurchaseOptionByOrderId(request.orderId());
        final ResultStrategy strategy = resultStrategyFactory.getStrategy(purchaseOption);
        strategy.createResult(request);
    }

    @GetMapping("/check")
    public String check() {
        return "헬스 췤";
    }
}
