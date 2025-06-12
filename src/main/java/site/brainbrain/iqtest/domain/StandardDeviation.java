package site.brainbrain.iqtest.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum StandardDeviation {

    WECHSLER_STANDARD_DEVIATION(15),
    STANFORD_BINET_STANDARD_DEVIATION(16),
    CATTELL_STANDARD_DEVIATION(24),
    ;

    private final int value;

}
