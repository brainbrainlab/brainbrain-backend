package site.brainbrain.iqtest.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import site.brainbrain.iqtest.domain.User;
import site.brainbrain.iqtest.exception.UserException;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(long id);

    default User fetchById(final long id) {
        return findById(id).orElseThrow(() -> new UserException("유저 id를 찾을 수 없습니다."));
    }
}
