package site.brainbrain.iqtest.domain.result;

import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.domain.PurchaseOption;

public interface ResultStrategy {

    PurchaseOption getPurchaseOption();

    void createResult(final CreateResultRequest request);
}
