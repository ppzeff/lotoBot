package ppzeff.servise;

import ppzeff.entity.Loto;

import java.io.IOException;
import java.util.List;

public interface ExcelService {
    String savetoExcel(List<Loto> lotoList) throws IOException;
}
