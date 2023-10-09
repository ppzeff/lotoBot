package ppzeff.repo;

import org.springframework.stereotype.Repository;
import ppzeff.entity.People;

import java.util.List;

@Repository
public class PeopleRepoSqlImpl  {

    public List<People> getAllPeople() {
        return null;
    }

    public List<People> filterPeopleBySurname(String str) {
        return null;
    }

    public People getPeopleById(String myId) {
        return null;
    }
}
