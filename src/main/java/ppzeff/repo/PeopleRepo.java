package ppzeff.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ppzeff.entity.People;

import java.util.List;

public interface PeopleRepo extends JpaRepository<People, Long> {
    List<People> findPeopleBySurnameContainingIgnoreCase(String str);
}
