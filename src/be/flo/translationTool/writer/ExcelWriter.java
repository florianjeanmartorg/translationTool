package be.flo.translationTool.writer;

import be.flo.translationTool.SourceEnum;
import be.flo.translationTool.model.SourceTypeEnum;
import be.flo.translationTool.model.TranslationInfo;
import be.flo.translationTool.util.Const;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by flo on 14/05/16.
 */
public class ExcelWriter {

    private static Integer SOURCE_COLUMN     = 0;
    private static Integer KEY_COLUMN        = 1;
    private static Integer FIRST_LANG_COLUMN = 2;

    private Map<String, Integer> langColumn = new HashMap<String, Integer>();


    public void write(SourceEnum sourceEnum,TreeMap<TranslationInfo, String> m) {


        try {

            java.io.File file = new java.io.File("./rest.xls");
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
            workbook.createSheet("translation", 0);
            WritableSheet excelSheet = workbook.getSheet(0);

            write(sourceEnum,excelSheet, m);

            workbook.write();
            workbook.close();

        } catch (Exception e) {

        }
    }

    private void write(SourceEnum sourceEnum,WritableSheet excelSheet, Map<TranslationInfo, String> translationMap) throws WriteException {

        int lineConter = 1;
        int columnCounter = FIRST_LANG_COLUMN;

        for (Map.Entry<TranslationInfo, String> entry : translationMap.entrySet()) {

            if (entry.getKey().testAviability(sourceEnum)) {

                //source
                String source = "";
                for (SourceTypeEnum sourceTypeEnum : entry.getKey().getSourceTypes()) {
                    source += sourceTypeEnum.name() + ", ";
                }
                excelSheet.addCell(new Label(SOURCE_COLUMN, lineConter, source));

                //name
                excelSheet.addCell(new Label(KEY_COLUMN, lineConter, entry.getValue()));

                //content
                for (Map.Entry<String, String> contents : entry.getKey().getContents().entrySet()) {
                    if (!langColumn.containsKey(contents.getKey())) {

                        langColumn.put(contents.getKey(), columnCounter);
                        excelSheet.addCell(new Label(columnCounter, 0, contents.getKey()));
                        columnCounter++;
                    }
                    excelSheet.addCell(new Label(langColumn.get(contents.getKey()), lineConter, contents.getValue()));
                }


                lineConter++;
            }
        }
    }

}
