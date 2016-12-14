package be.flo.translationTool.reader;

import be.flo.translationTool.SourceEnum;
import be.flo.translationTool.model.SourceTypeEnum;
import be.flo.translationTool.model.TranslationInfo;
import be.flo.translationTool.util.Const;
import be.flo.translationTool.util.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by flo on 15/05/16.
 */
public class AndroidReader {

    private String languageFilePath = "app/src/main/res/";

    public Map<String, TranslationInfo> read(SourceEnum sourceEnum) throws Exception {

        Map<String, TranslationInfo> map = new HashMap<String, TranslationInfo>();

        File baseDirector = new File(sourceEnum.getAndroidPath() + languageFilePath);

        for (File valueDirectory : baseDirector.listFiles()) {

            Pattern compile = Pattern.compile("values_?([a-z]*)");
            Pattern translationPattern = Pattern.compile("<string name=\"([a-zA-Z0-9_]*)\">(.*)</string>");

            Matcher matcher = compile.matcher(valueDirectory.getName());

            if (matcher.find()) {

                String lang = sourceEnum.getDefaultLanguage();

                if (matcher.groupCount() > 1) {
                    lang = matcher.group(1);
                }

                File file = new File(valueDirectory.getAbsolutePath() + "/strings.xml");

                if (file.exists()) {

                    String content = FileUtil.getString(file);

                    Matcher matcher1 = translationPattern.matcher(content);


                    int counter=1000;


                    while (matcher1.find()) {

                        String key = matcher1.group(1);
                        String contentTranslation = matcher1.group(2);


                        if (!map.containsKey(key)) {
                            map.put(key, new TranslationInfo(true, new SourceTypeEnum[]{SourceTypeEnum.ANDROID},counter));
                        }
                        map.get(key).addContent(lang, contentTranslation);

                        counter++;
                    }
                }

            }

        }
        return map;
    }
}
