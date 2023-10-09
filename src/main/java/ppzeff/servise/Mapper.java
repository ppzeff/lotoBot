package ppzeff.servise;

import ppzeff.entity.People;
import ppzeff.entity.PeopleXLSXDto;

import java.util.List;


public interface Mapper {
    List<People> mapXlsxToPeople(List<PeopleXLSXDto> list);
}
