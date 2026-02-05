package com.ferpfirstcode.utils.dataReader;

import com.ferpfirstcode.utils.logs.LogsManager;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class ExcelReader {
    private static final String TEST_DATA_PATH = "src/test/resources/test-data";

    public static String getexceldata(String excelfilename, String sheetname, int rownum, int colnum) {
        String celldata;
        try (XSSFWorkbook workbook = new XSSFWorkbook(TEST_DATA_PATH + excelfilename)) {
            XSSFSheet sheet = workbook.getSheet(sheetname);
            celldata = sheet.getRow(rownum - 1).getCell(colnum).getStringCellValue();
            return celldata;
        } catch (IOException e) {
            LogsManager.error("Error reading excel file: ", excelfilename, e.getMessage());
            return "";
        }
    }

}
