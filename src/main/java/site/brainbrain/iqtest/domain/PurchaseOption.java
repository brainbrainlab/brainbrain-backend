package site.brainbrain.iqtest.domain;

import lombok.extern.slf4j.Slf4j;
import site.brainbrain.iqtest.exception.PurchaseOptionException;

@Slf4j
public enum PurchaseOption {

    BASIC,
    STANDARD,
    PREMIUM,
    ;

    public static PurchaseOption from(final String name) {
        try {
            return PurchaseOption.valueOf(name.toUpperCase());
        } catch (final IllegalArgumentException | NullPointerException e) {
            log.error("잘못된 구매 옵션 입력: '{}'", name, e);
            throw new PurchaseOptionException("지원하지 않는 구매 옵션입니다: " + name);
        }
    }
}
