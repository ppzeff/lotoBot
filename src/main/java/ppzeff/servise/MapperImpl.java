package ppzeff.servise;

import org.springframework.stereotype.Component;
import ppzeff.entity.People;
import ppzeff.entity.PeopleXLSXDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapperImpl implements Mapper {
    @Override
    public List<People> mapXlsxToPeople(List<PeopleXLSXDto> xlsxDtos) {
        List<People> people = new ArrayList<>();
        for (PeopleXLSXDto dto : xlsxDtos) {
            people.add(new People(
                    dto.getUuid(),
                    dto.getSurname(),
                    dto.getName(),
                    dto.getPatronymic(),
                    dto.getRole(),
                    dto.getCertificate(),
                    dto.getValidity(),
                    dto.getLinkPhoto(),
                    null,
                    0
            ));
        }
//        people.forEach(el -> System.out.println(el.getMyId()));
        return people;
    }
}
