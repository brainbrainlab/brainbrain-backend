package site.brainbrain.iqtest.domain.dto;

import site.brainbrain.iqtest.domain.ScoreResult;

public record ResultStrategyDto(long userId, String email, String name, ScoreResult scoreResult) {
}
