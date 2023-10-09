package ppzeff.servise;


import ppzeff.entity.PeopleXLSXDto;

import java.io.IOException;
import java.util.List;

public interface ReaderXLSX {
    List<PeopleXLSXDto> getAllPeople() throws IOException;
}
