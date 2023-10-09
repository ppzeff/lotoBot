package ppzeff.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ppzeff.entity.People;
import ppzeff.servise.Mapper;
import ppzeff.servise.MapperImpl;
import ppzeff.servise.ReaderXLSXImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PeopleRepoXLSX {
    @Autowired
    private final Mapper mapper = new MapperImpl();
    @Autowired
    private final ReaderXLSXImpl readerXLSX = new ReaderXLSXImpl();
    List<People> list = new ArrayList<>();

    public List<People> getAllPeople() {
        return list;
    }

    public List<People> filterPeopleBySurname(String str) {
        return list.stream().filter(el -> el.getSurname().toLowerCase().contains(str.toLowerCase())).toList();
    }

    public People getPeopleById(String myId) {
        return list.stream().filter(el -> el.getMyId().equalsIgnoreCase(myId)).findFirst().orElseThrow();
    }

    public PeopleRepoXLSX() throws IOException {
        list = mapper.mapXlsxToPeople(readerXLSX.getAllPeople());
//        list.add(new People("peopleId1", "Абрамов", "Сергей", "Александрович",
//                "Руководитель энергоблока", "1-1.1-08.25", LocalDate.of(2025, Month.AUGUST, 1),
//                "https://i.ibb.co/72gNKpg/1024x681-0xac120003-7202495331615824311.jpg", null));
//        list.add(new People("peopleId2", "Грицук", "Дмитрий", "Николаевич", null, "https://i.ibb.co/bzbDSMy/b9kl7zxh.jpg"));
//        list.add(new People("peopleId3", "Преснякова", "Екатерина", "Андреевна", null, "https://i.ibb.co/sWw5tgv/728x542-1-ac85140c1f60f564a152a3e17470289b-1000x745-0xac120003-17763851401588781623.jpg"));

    }
}
