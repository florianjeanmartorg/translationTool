package be.flo.translationTool.util;

import be.flo.translationTool.model.SourceTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by florian on 11/05/15.
 */
public class Const {

    public static final String             KEY_REGEX                 = "--\\.[a-zA-Z0-9._-]+";
    public static final String[]           TARGET_DIRECTORIES        = new String[]{"app/", "conf/"};
    public static final String             TRANSLATION_DIRECTORIES   = "conf/";
    public static final String             TRANSLATION_FILE_PATTERN  = "messages\\.(.*)";
    public static final String             CATCH_TRANSLATION_CONTENT = "(\\n|^)((#)? *(--\\.[a-zA-Z0-9._-]+))=(.+)";
    public static final ConverterInterface CONVERTER_INTERFACE       = new ConverterInterface() {
        @Override
        public String convert(String s) {
            s = s.replace("--.", "");
            s = s.replaceAll("\\.", "_");
            return s;
        }
    };

    public static final Map<String, SourceTypeEnum[]> FILE_TYPE = new HashMap<String, SourceTypeEnum[]>() {{
        put("java", new SourceTypeEnum[]{SourceTypeEnum.SERVER});
        put("vm", new SourceTypeEnum[]{SourceTypeEnum.SERVER});
        put("jade", new SourceTypeEnum[]{SourceTypeEnum.SITE});
        put("coffee", new SourceTypeEnum[]{SourceTypeEnum.SITE});
    }};

    public static final boolean DEBUG            = true;
}
