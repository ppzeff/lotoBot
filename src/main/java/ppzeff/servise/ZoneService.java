package ppzeff.servise;

import ppzeff.entity.Zone;
import ppzeff.repo.ZoneRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {
    private final ZoneRepo zoneRepo;

    public ZoneService(ZoneRepo zoneRepo) {
        this.zoneRepo = zoneRepo;
    }

    Zone findZoneById(String zineId) {
        return zoneRepo.findAll().stream().filter(el -> el.getMyId().equalsIgnoreCase(zineId.toLowerCase())).findFirst().orElse(null);
    }

    List<Zone> getAllZone() {
        return zoneRepo.findAll();
    }
}
