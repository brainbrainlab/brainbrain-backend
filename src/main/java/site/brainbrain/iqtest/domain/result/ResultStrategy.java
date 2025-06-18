package site.brainbrain.iqtest.domain.result;

import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.dto.ResultStrategyDto;

public interface ResultStrategy {

    PurchaseOption getPurchaseOption();

    void createResult(final ResultStrategyDto request);
}
