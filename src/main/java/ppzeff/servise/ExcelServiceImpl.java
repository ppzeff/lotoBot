package ppzeff.servise;

import ppzeff.entity.Loto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {

    private static final String SHEET_NAME = "LOTO";

    @Override
    public String savetoExcel(List<Loto> lotoList) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            configHeader(workbook);
            addRowsLoto(lotoList, workbook);
            String fileLocation = saveWB(workbook);
            return fileLocation;
        }
    }

    private String saveWB(Workbook workbook) {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) +
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) +
                "_loto.xlsx";

        try (var outputStream = new FileOutputStream(fileLocation)) {

            workbook.write(outputStream);
            workbook.close();

            return fileLocation;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addRowsLoto(List<Loto> lotoList, Workbook workbook) {
        var sheet = workbook.getSheet(SHEET_NAME);
        for (int i = 0; i < lotoList.size(); i++) {
            var row = sheet.createRow(i + 1);
            var loto = lotoList.get(i);
            row.createCell(0).setCellValue(loto.getId());
            row.createCell(1).setCellValue(loto.getDt().toString());
            row.createCell(2).setCellValue(loto.getZone().getZoneName());
            row.createCell(3).setCellValue(loto.getDate());
            row.createCell(4).setCellValue(loto.getComplex());
            row.createCell(5).setCellValue(
                    loto.getPeople().getSurname() + " " +
                            loto.getPeople().getName() + " " +
                            loto.getPeople().getPatronymic()
            );
        }
    }

    private void configHeader(Workbook workbook) {
        var sheet = workbook.createSheet(SHEET_NAME);
        sheet.setColumnWidth(0, 2000);
        sheet.setColumnWidth(1, 2000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 8000);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("#");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Дата регистрации");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(2);
        headerCell.setCellValue("Зона работ");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(3);
        headerCell.setCellValue("Дата работ");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(4);
        headerCell.setCellValue("Комплексная");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(5);
        headerCell.setCellValue("Специалист");
        headerCell.setCellStyle(headerStyle);

    }
}
