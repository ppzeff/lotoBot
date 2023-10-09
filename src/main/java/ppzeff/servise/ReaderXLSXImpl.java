package ppzeff.servise;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ppzeff.entity.PeopleXLSXDto;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class ReaderXLSXImpl implements ReaderXLSX {
    private final String fileName = "spe.xlsx";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


    @Override
    public List<PeopleXLSXDto> getAllPeople() throws IOException {
        List<PeopleXLSXDto> list = new ArrayList<>();
        ClassLoader classLoader = this.getClass().getClassLoader();
        InputStream initialStream = classLoader.getResourceAsStream(fileName);
        if (initialStream == null) {
            throw new IllegalArgumentException("in resource file is not found!");
        } else {
            Workbook workbook = new XSSFWorkbook(initialStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }
                String[] fioArr = getFIO(row.getCell(0).getStringCellValue());
                list.add(new PeopleXLSXDto(
                        fioArr[0],
                        fioArr[1],
                        fioArr[2],
                        row.getCell(1).getStringCellValue(),
                        row.getCell(2).getStringCellValue(),
                        LocalDate.parse(row.getCell(3).getStringCellValue(), formatter),
                        row.getCell(4).getStringCellValue(),
                        row.getCell(5).getStringCellValue()

                ));
            }
        }
        return list;
    }

    private String[] getFIO(String str) {
        return str.split(" ");
    }
}
