package site.brainbrain.iqtest.domain.dto;

import site.brainbrain.iqtest.domain.PurchaseOption;

public record BasicEmailDto(PurchaseOption purchaseOption, long userId, String email, String name) {
}
