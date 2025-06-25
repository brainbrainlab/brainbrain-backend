package site.brainbrain.iqtest.controller.dto;

import java.util.List;

public record CreateResultRequest(String orderId,
                                  UserInfoRequest userInfoRequest,
                                  List<Integer> answers) {

    @Override
    public String toString() {
        return "CreateResultRequest{" +
                "orderId='" + orderId + '\'' +
                ", userInfoRequest=" + userInfoRequest +
                ", answers=" + answers +
                '}';
    }
}
