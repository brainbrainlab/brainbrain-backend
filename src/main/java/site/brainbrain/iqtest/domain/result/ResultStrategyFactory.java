package site.brainbrain.iqtest.domain.result;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.exception.PurchaseOptionException;

@Component
public class ResultStrategyFactory {

    private final Map<PurchaseOption, ResultStrategy> resultStrategies;

    public ResultStrategyFactory(final List<ResultStrategy> strategies) {
        this.resultStrategies = strategies.stream()
                .collect(Collectors.toMap(ResultStrategy::getPurchaseOption, Function.identity()));
    }

    public ResultStrategy getStrategy(final PurchaseOption purchaseOption) {
        return Optional.ofNullable(resultStrategies.get(purchaseOption))
                .orElseThrow(() -> new PurchaseOptionException("구매 옵션과 일치하는 결과를 생성할 수 없습니다."));
    }
}
