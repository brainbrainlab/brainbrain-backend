package site.brainbrain.iqtest.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import site.brainbrain.iqtest.domain.ScoreResult;

@SpringBootTest
class ScoreServiceTest {

    // 백점짜리 정답 나중에 변경 가능성 있음
    private static final List<Integer> ANSWER = List.of(
            1, 2, 3, 4, 1, 2, 3, 4, 1, 2,
            3, 4, 1, 5, 3, 7, 1, 2, 3, 4,
            5, 6, 7, 8, 5, 6, 7, 8, 7, 6,
            3, 4, 1, 2, 3, 4, 1, 2, 6, 4, 1, 2
    );

    @Autowired
    ScoreService scoreService;

    @Test
    void calculateStandardDeviation() {
        final ScoreResult calculate = scoreService.calculate(ANSWER);

        assertThat(calculate.cattell()).isEqualTo(172);
        assertThat(calculate.stanford()).isEqualTo(148);
        assertThat(calculate.wechsler()).isEqualTo(145);
    }
}
