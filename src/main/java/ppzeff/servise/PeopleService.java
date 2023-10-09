package ppzeff.servise;

import ppzeff.entity.People;
import ppzeff.repo.PeopleRepo;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PeopleService {
    private final PeopleRepo peopleRepo;

    public PeopleService(PeopleRepo peopleRepo) {
        this.peopleRepo = peopleRepo;
    }

    public List<People> findAll() {
        return peopleRepo.findAll();
    }

    public List<People> filterPeopleBySurname(String filter){
        return peopleRepo.findPeopleBySurnameContainingIgnoreCase(filter).stream().sorted(Comparator.comparing(People::getScore)).limit(25).toList();
    }

    public People getPeopleById(String peopleId) {
        return findAll().stream().filter(el -> el.getMyId().equalsIgnoreCase(peopleId))
                .findFirst()
                .orElse(null);
    }

    public String getFullName(){
        return "full";
    }
}
