package site.brainbrain.iqtest.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.brainbrain.iqtest.repository.IntegerListConverter;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String age;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private String country;

    @Convert(converter = IntegerListConverter.class)
    private List<Integer> answers;

    @Builder
    public UserInfo(final Long id, final String email, final String name, final String age, final String gender,
                    final String country, final List<Integer> answers) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.answers = answers;
    }
}
