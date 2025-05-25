package site.brainbrain.iqtest.controller.dto;

public record UserInfoRequest(String email,
                              String name,
                              String age,
                              String gender,
                              String country,
                              boolean agreement) {
}
