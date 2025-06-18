package site.brainbrain.iqtest.domain.result;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.dto.BasicEmailDto;
import site.brainbrain.iqtest.domain.dto.ResultStrategyDto;
import site.brainbrain.iqtest.service.EmailService;
import site.brainbrain.iqtest.service.ScoreService;

@Component
@RequiredArgsConstructor
public class BasicResultStrategy implements ResultStrategy {

    private final ScoreService scoreService;
    private final EmailService emailService;

    @Override
    public PurchaseOption getPurchaseOption() {
        return PurchaseOption.BASIC;
    }

    @Override
    public void createResult(final ResultStrategyDto request) {
        final BasicEmailDto basicEmailDto = new BasicEmailDto(getPurchaseOption(), request.userId(), request.email(), request.name());
        emailService.sendOnlyScore(basicEmailDto, request.scoreResult());
    }
}
