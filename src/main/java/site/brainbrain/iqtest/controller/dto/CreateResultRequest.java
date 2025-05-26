package site.brainbrain.iqtest.controller.dto;

import java.util.List;

public record CreateResultRequest(String couponCode,
                                  String orderId,
                                  UserInfoRequest userInfoRequest,
                                  List<Integer> answers) {
}
