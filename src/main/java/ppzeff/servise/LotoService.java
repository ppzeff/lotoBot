package ppzeff.servise;

import org.springframework.stereotype.Service;
import ppzeff.entity.Loto;
import ppzeff.repo.LotoRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LotoService {
    private final LotoRepo lotoRepo;

    private final Map<Long, Loto> mapStatus = new HashMap<>();

    public List<Loto> findAllLoto() {
        return lotoRepo.findAll();
    }

    public LotoService(LotoRepo lotoRepo) {
        this.lotoRepo = lotoRepo;
    }

    public void setLoto(Long id, Loto loto) {
        mapStatus.put(id, loto);
    }

    public Loto getLotoById(Long id) {
        return mapStatus.get(id);
    }

    public List<Long> getAllLong() {
        return mapStatus.keySet().stream().toList();
    }

    public void save(Loto loto) {
        lotoRepo.save(loto);
    }
}
