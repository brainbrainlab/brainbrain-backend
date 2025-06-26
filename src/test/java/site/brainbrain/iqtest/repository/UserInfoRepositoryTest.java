package site.brainbrain.iqtest.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.brainbrain.iqtest.domain.UserInfo;

@SpringBootTest
class UserInfoRepositoryTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Test
    @DisplayName("유저 정보를 저장한다.")
    void save_userinfo() {
        // given
        final UserInfo userInfo = UserInfo.builder()
                .email("test")
                .age("23")
                .gender("MALE")
                .name("jihyeon")
                .country("대한민국")
                .answers(List.of(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1))
                .build();

        // when & then
        assertDoesNotThrow(() -> userInfoRepository.save(userInfo));
    }

    @Test
    @DisplayName("유저 정보를 불러온다. - 문제 답변이 정수 리스트 형식으로 반환된다.")
    void get_userinfo() {
        // given
        final UserInfo userInfo = UserInfo.builder()
                .email("test")
                .age("23")
                .gender("MALE")
                .name("jihyeon")
                .country("대한민국")
                .answers(List.of(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1))
                .build();
        UserInfo saveUserInfo = userInfoRepository.save(userInfo);

        // when
        UserInfo actual = userInfoRepository.fetchById(saveUserInfo.getId());

        // then
        assertThat(actual.getAnswers()).isEqualTo(userInfo.getAnswers());
    }
}
