package be.flo.translationTool.writer;

import be.flo.translationTool.model.FileInfo;
import be.flo.translationTool.model.SourceInfo;
import be.flo.translationTool.model.TranslationInfo;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by flo on 14/05/16.
 */
public class ExcelWriter {

    public static void write(){//SourceInfo sourceInfo, HashMap<FileInfo, Map<String, TranslationInfo>> map) {

        try {

            java.io.File file = new java.io.File("./rest.xls");
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("translation", 0);
            WritableSheet excelSheet = workbook.getSheet(0);

            WritableCell writableCell = new Label(0, 0, "je suis une petite pute");

            excelSheet.addCell(writableCell);

            workbook.write();
            workbook.close();

        } catch (Exception e) {

        }


    }
}
