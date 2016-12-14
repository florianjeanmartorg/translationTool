package be.flo.translationTool;

import be.flo.translationTool.model.FileInfo;
import be.flo.translationTool.model.TranslationInfo;
import be.flo.translationTool.reader.AndroidReader;
import be.flo.translationTool.reader.IosReader;
import be.flo.translationTool.reader.Reader;
import be.flo.translationTool.util.Const;
import be.flo.translationTool.writer.ExcelWriter;
import be.flo.translationTool.writer.Writer;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by florian on 11/05/15.
 */
public class Main {


    public static void main(String[] args) {

        SourceEnum sourceEnum = SourceEnum.DEMO;


        AndroidReader androidReader = new AndroidReader();
        IosReader iosReader = new IosReader();
        try {

            // android
            Map<String, TranslationInfo> androidTranslation = new HashMap<String, TranslationInfo>();
            if (sourceEnum.getAndroidPath() != null) {
                androidTranslation = androidReader.read(sourceEnum);
            }

            // ios
            Map<String, TranslationInfo> iosTranslation = new HashMap<String, TranslationInfo>();
            if (sourceEnum.getAndroidPath() != null) {
                iosTranslation = iosReader.read(sourceEnum);
            }

            Reader reader = new Reader();

            reader.browse(sourceEnum);
            Writer writer = new Writer();
            try {
                writer.write(sourceEnum, reader.getKeyMap());
            } catch (Exception e) {
                e.printStackTrace();
            }


            TreeMap<TranslationInfo, String> m = new TreeMap<TranslationInfo, String>();
            for (Map.Entry<FileInfo, Map<String, TranslationInfo>> fileInfoMapEntry : reader.getKeyMap().entrySet()) {
                for (Map.Entry<String, TranslationInfo> stringTranslationInfoEntry : fileInfoMapEntry.getValue().entrySet()) {

                    String key = Const.CONVERTER_INTERFACE.convert(stringTranslationInfoEntry.getKey());

                    boolean founded = false;

                    for (Map.Entry<TranslationInfo, String> translationInfoStringEntry : m.entrySet()) {
                        if (translationInfoStringEntry.getValue().equals(key)) {

                            translationInfoStringEntry.getKey().fusion(stringTranslationInfoEntry.getValue());

                            founded = true;
                        }
                    }

                    if (!founded) {
                        m.put(stringTranslationInfoEntry.getValue(), key);
                    }
                }
            }


            //android
            for (Map.Entry<String, TranslationInfo> stringTranslationInfoEntry : androidTranslation.entrySet()) {
                String key = stringTranslationInfoEntry.getKey();

                boolean founded = false;

                for (Map.Entry<TranslationInfo, String> translationInfoStringEntry : m.entrySet()) {
                    if (translationInfoStringEntry.getValue().equals(key)) {

                        translationInfoStringEntry.getKey().fusion(stringTranslationInfoEntry.getValue());

                        founded = true;
                    }
                }

                if (!founded) {
                    m.put(stringTranslationInfoEntry.getValue(), key);
                }
            }

            //ios
            for (Map.Entry<String, TranslationInfo> stringTranslationInfoEntry : iosTranslation.entrySet()) {
                String key = stringTranslationInfoEntry.getKey();

                boolean founded = false;

                for (Map.Entry<TranslationInfo, String> translationInfoStringEntry : m.entrySet()) {
                    if (translationInfoStringEntry.getValue().equals(key)) {

                        translationInfoStringEntry.getKey().fusion(stringTranslationInfoEntry.getValue());

                        founded = true;
                    }
                }

                if (!founded) {
                    m.put(stringTranslationInfoEntry.getValue(), key);
                }
            }


            ExcelWriter excelWriter = new ExcelWriter();
            excelWriter.write(sourceEnum, m);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
