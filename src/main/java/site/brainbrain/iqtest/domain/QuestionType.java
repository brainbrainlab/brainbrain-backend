package site.brainbrain.iqtest.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum QuestionType {

    VISUAL_PATTERN_RECOGNITION("시각적 패턴인식"),
    LOGICAL_REASONING_ABILITY("논리적 추론 능력"),
    SPATIAL_PERCEPTION("공간 지각 능력"),
    ;

    private final String name;

}
