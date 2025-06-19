package site.brainbrain.iqtest.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import site.brainbrain.iqtest.controller.dto.CreateEmailResultRequest;
import site.brainbrain.iqtest.controller.dto.CreateResultRequest;
import site.brainbrain.iqtest.controller.dto.UserInfoRequest;
import site.brainbrain.iqtest.domain.PurchaseOption;
import site.brainbrain.iqtest.domain.UserInfo;
import site.brainbrain.iqtest.domain.result.ResultStrategyFactory;
import site.brainbrain.iqtest.exception.UserInfoException;
import site.brainbrain.iqtest.repository.UserInfoRepository;

@SpringBootTest
class ResultServiceTest {

    @Autowired
    private ResultService resultService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private ResultStrategyFactory resultStrategyFactory;

    @MockitoBean
    private EmailService emailService;

    @Test
    @DisplayName("테스트 결과를 저장하고 이메일을 전송한다.")
    void create_result() {
        // given
        final UserInfoRequest userInfo = new UserInfoRequest("test@example.com", "name", "age", "FEMALE", "대한민국", true);
        final List<Integer> answers = List.of(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1);
        final CreateResultRequest request = new CreateResultRequest("test orderId", userInfo, answers);
        doNothing().when(emailService)
                .sendOnlyScore(any(), any());
        int beforeUserCount = userInfoRepository.findAll().size();

        // when & then
        assertDoesNotThrow(() -> resultService.createResult(request, PurchaseOption.BASIC));
        int afterUserCount = userInfoRepository.findAll().size();
        assertThat(afterUserCount).isEqualTo(beforeUserCount + 1);
        verify(emailService).sendOnlyScore(any(), any());
    }

    @Test
    @DisplayName("유저 id로 테스트 결과를 불러오고 이메일을 전송한다.")
    void create_only_email() {
        // given
        final UserInfo userInfo = UserInfo.builder()
                .email("test@email.com")
                .name("test")
                .age("34")
                .gender("FEMALE")
                .country("대한민국")
                .answer(List.of(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1))
                .build();
        final UserInfo savedUserInfo = userInfoRepository.save(userInfo);

        final CreateEmailResultRequest request = new CreateEmailResultRequest(savedUserInfo.getId(), "test orderId");
        doNothing().when(emailService)
                .sendOnlyScore(any(), any());
        final int beforeUserCount = userInfoRepository.findAll().size();

        // when & then
        assertDoesNotThrow(() -> resultService.createOnlyEmailResult(request, PurchaseOption.BASIC));
        final int afterUserCount = userInfoRepository.findAll().size();
        assertThat(afterUserCount).isEqualTo(beforeUserCount);
        verify(emailService).sendOnlyScore(any(), any());
    }

    @Test
    @DisplayName("유저 id가 존재하지 않으면 예외가 발생한다.")
    void throw_exception_when_notFoundUserId() {
        // given
        final int notExistId = userInfoRepository.findAll().size() + 1;
        final CreateEmailResultRequest request = new CreateEmailResultRequest(notExistId, "test orderId");
        doNothing().when(emailService)
                .sendOnlyScore(any(), any());

        // when & then
        assertThatThrownBy(() -> resultService.createOnlyEmailResult(request, PurchaseOption.BASIC))
                .isExactlyInstanceOf(UserInfoException.class);
    }
}
