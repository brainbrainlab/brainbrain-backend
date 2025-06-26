package site.brainbrain.iqtest.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.brainbrain.iqtest.controller.dto.CreateEmailResultRequest;
import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.controller.dto.UserInfoRequest;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.ScoreResult;
import site.brainbrain.iqtest.domain.UserInfo;
import site.brainbrain.iqtest.domain.dto.ResultStrategyDto;
import site.brainbrain.iqtest.domain.result.ResultStrategy;
import site.brainbrain.iqtest.domain.result.ResultStrategyFactory;
import site.brainbrain.iqtest.repository.UserInfoRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class ResultService {

    private final UserInfoRepository userInfoRepository;
    private final ScoreService scoreService;
    private final ResultStrategyFactory resultStrategyFactory;

    public void createResult(final CreateResultRequest request, final PurchaseOption purchaseOption) {
        final UserInfoRequest userInfo = request.userInfoRequest();
        final UserInfo user = UserInfo.builder()
                .email(userInfo.email())
                .name(userInfo.name())
                .age(userInfo.age())
                .gender(userInfo.gender())
                .country(userInfo.country())
                .answers(request.answers())
                .build();
        final UserInfo savedUserInfo = userInfoRepository.save(user);

        final ScoreResult scoreResult = scoreService.calculate(request.answers());
        final ResultStrategy strategy = resultStrategyFactory.getStrategy(purchaseOption);
        final ResultStrategyDto strategyDto = new ResultStrategyDto(
                savedUserInfo.getId(),
                userInfo.email(),
                userInfo.name(),
                scoreResult
        );
        strategy.createResult(strategyDto);
    }

    public void createResultForExtraPayment(final CreateEmailResultRequest request, final PurchaseOption purchaseOption) {
        final UserInfo userInfo = userInfoRepository.fetchById(request.userId());

        final ScoreResult scoreResult = scoreService.calculate(userInfo.getAnswers());
        final ResultStrategy strategy = resultStrategyFactory.getStrategy(purchaseOption);
        final ResultStrategyDto strategyDto = new ResultStrategyDto(
                userInfo.getId(),
                userInfo.getEmail(),
                userInfo.getName(),
                scoreResult
        );
        strategy.createResult(strategyDto);
    }
}
