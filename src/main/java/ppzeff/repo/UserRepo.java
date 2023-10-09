package ppzeff.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ppzeff.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity, Long> {
}
