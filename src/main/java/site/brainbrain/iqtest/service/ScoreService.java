package site.brainbrain.iqtest.service;

import static site.brainbrain.iqtest.domain.StandardDeviation.CATTELL_STANDARD_DEVIATION;
import static site.brainbrain.iqtest.domain.StandardDeviation.STANFORD_BINET_STANDARD_DEVIATION;
import static site.brainbrain.iqtest.domain.StandardDeviation.WECHSLER_STANDARD_DEVIATION;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import site.brainbrain.iqtest.domain.QuestionType;
import site.brainbrain.iqtest.domain.ScoreResult;
import site.brainbrain.iqtest.domain.StandardDeviation;

@Service
public class ScoreService {

    private static final int QUESTIONS_SIZE = 42;

    private static final List<QuestionType> QUESTION_TYPE = List.of(
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY,
            QuestionType.LOGICAL_REASONING_ABILITY
    );

    private static final List<Integer> ANSWER = List.of(
            1, 2, 3, 4, 1, 2, 3, 4, 1, 2,
            3, 4, 1, 5, 3, 7, 1, 2, 3, 4,
            5, 6, 7, 8, 5, 6, 7, 8, 7, 6,
            3, 4, 1, 2, 3, 4, 1, 2, 6, 4, 1, 2
    );

    private static final Map<Integer, Integer> TOTAL_IQ_SCORE = Map.ofEntries(
            Map.entry(42, 172), Map.entry(41, 169), Map.entry(40, 164),
            Map.entry(39, 161), Map.entry(38, 156), Map.entry(37, 153),
            Map.entry(36, 148), Map.entry(35, 145), Map.entry(34, 142),
            Map.entry(33, 139), Map.entry(32, 137), Map.entry(31, 134),
            Map.entry(30, 131), Map.entry(29, 128), Map.entry(28, 125),
            Map.entry(27, 122), Map.entry(26, 119), Map.entry(25, 116),
            Map.entry(24, 114), Map.entry(23, 111), Map.entry(22, 108),
            Map.entry(21, 105), Map.entry(20, 100), Map.entry(19, 97),
            Map.entry(18, 94), Map.entry(17, 91), Map.entry(16, 88),
            Map.entry(15, 86), Map.entry(14, 83), Map.entry(13, 80),
            Map.entry(12, 77), Map.entry(11, 74), Map.entry(10, 71),
            Map.entry(9, 68), Map.entry(8, 68), Map.entry(7, 68),
            Map.entry(6, 68), Map.entry(5, 68), Map.entry(4, 68),
            Map.entry(3, 68), Map.entry(2, 68), Map.entry(1, 68),
            Map.entry(0, 68)
    );

    public ScoreResult calculate(final List<Integer> request) {
        final Map<QuestionType, Integer> countType = new EnumMap<>(QuestionType.class);
        int correctCount = 0;

        for (int i = 0; i < QUESTIONS_SIZE; i++) {
            final QuestionType type = QUESTION_TYPE.get(i);
            final boolean isCorrect = Objects.equals(ANSWER.get(i), request.get(i));
            if (isCorrect) {
                countType.put(type, countType.getOrDefault(type, 0) + 1);
                correctCount += 1;
            }
        }
        final int cattellScore = calculateTotalScore(correctCount);
        final int stanfordScore = calculateStandardDeviation(cattellScore, STANFORD_BINET_STANDARD_DEVIATION);
        final int wechslerScore = calculateStandardDeviation(cattellScore, WECHSLER_STANDARD_DEVIATION);

        return new ScoreResult(countType, wechslerScore, stanfordScore, cattellScore);
    }

    private int calculateTotalScore(final int totalScore) {
        return TOTAL_IQ_SCORE.getOrDefault(totalScore, 68);
    }

    private int calculateStandardDeviation(final int iq, StandardDeviation sd) {
        return (iq - 100) * sd.getValue() / CATTELL_STANDARD_DEVIATION.getValue() + 100;
    }
}
