package site.brainbrain.iqtest.domain;

import java.util.Map;

public record ScoreResult(Map<QuestionType, Integer> questionResult,
                          int wechsler,
                          int stanford,
                          int cattell) {
}
