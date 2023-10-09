package ppzeff.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ppzeff.entity.Zone;


public interface ZoneRepo extends JpaRepository<Zone, Long> {
}
