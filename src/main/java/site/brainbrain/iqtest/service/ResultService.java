package site.brainbrain.iqtest.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.brainbrain.iqtest.controller.dto.CreateEmailResultRequest;
import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.controller.dto.UserInfoRequest;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.ScoreResult;
import site.brainbrain.iqtest.domain.User;
import site.brainbrain.iqtest.domain.dto.ResultStrategyDto;
import site.brainbrain.iqtest.domain.result.ResultStrategy;
import site.brainbrain.iqtest.domain.result.ResultStrategyFactory;
import site.brainbrain.iqtest.repository.UserRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class ResultService {

    private final UserRepository userRepository;
    private final ScoreService scoreService;
    private final ResultStrategyFactory resultStrategyFactory;

    public void createResult(final CreateResultRequest request, final PurchaseOption purchaseOption) {
        final UserInfoRequest userInfo = request.userInfoRequest();
        final User user = User.builder()
                .email(userInfo.email())
                .name(userInfo.name())
                .age(userInfo.age())
                .gender(userInfo.gender())
                .country(userInfo.country())
                .answer(request.answers())
                .build();
        final User savedUser = userRepository.save(user);

        final ScoreResult scoreResult = scoreService.calculate(request.answers());
        final ResultStrategy strategy = resultStrategyFactory.getStrategy(purchaseOption);
        final ResultStrategyDto strategyDto = new ResultStrategyDto(
                savedUser.getId(),
                userInfo.email(),
                userInfo.name(),
                scoreResult
        );
        strategy.createResult(strategyDto);
    }

    public void createOnlyEmailResult(final CreateEmailResultRequest request, final PurchaseOption purchaseOption) {
        User user = userRepository.fetchById(request.userId());

        final ScoreResult scoreResult = scoreService.calculate(user.getAnswer());
        final ResultStrategy strategy = resultStrategyFactory.getStrategy(purchaseOption);
        final ResultStrategyDto strategyDto = new ResultStrategyDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                scoreResult
        );
        strategy.createResult(strategyDto);
    }
}
