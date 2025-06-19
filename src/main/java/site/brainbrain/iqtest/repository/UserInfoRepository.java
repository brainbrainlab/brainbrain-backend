package site.brainbrain.iqtest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.brainbrain.iqtest.domain.UserInfo;
import site.brainbrain.iqtest.exception.UserInfoException;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> findById(long id);

    default UserInfo fetchById(final long id) {
        return findById(id).orElseThrow(() -> new UserInfoException("유저 id를 찾을 수 없습니다."));
    }
}
