package site.brainbrain.iqtest.controller.dto;

import java.util.List;
import site.brainbrain.iqtest.domain.UserInfo;

public record UserInfoRequest(String email,
                              String name,
                              String age,
                              String gender,
                              String country,
                              boolean agreement) {

    public UserInfo toDomain(final List<Integer> answers) {
        return UserInfo.builder()
                .email(email)
                .name(name)
                .age(age)
                .gender(gender)
                .country(country)
                .answers(answers)
                .build();
    }
}
