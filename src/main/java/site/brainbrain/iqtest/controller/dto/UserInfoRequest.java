package site.brainbrain.iqtest.controller.dto;

public record UserInfoRequest(String email,
                              String name,
                              String age,
                              String gender,
                              String country,
                              boolean agreement) {

    @Override
    public String toString() {
        return "UserInfoRequest{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                ", agreement=" + agreement +
                '}';
    }
}
