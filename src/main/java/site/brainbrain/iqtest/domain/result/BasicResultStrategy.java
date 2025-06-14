package site.brainbrain.iqtest.domain.result;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.ScoreResult;
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
    public void createResult(final CreateResultRequest request) {
        final String email = request.userInfoRequest().email();
        final String name = request.userInfoRequest().name();

        final List<Integer> answers = request.answers();
        final ScoreResult scoreResult = scoreService.calculate(answers);

        emailService.sendOnlyScore(email, name, scoreResult);
    }
}
