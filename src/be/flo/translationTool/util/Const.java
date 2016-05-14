package be.flo.translationTool.util;

import be.flo.translationTool.model.SourceInfo;
import be.flo.translationTool.model.SourceTypeEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by florian on 11/05/15.
 */
public class Const {

    public static final SourceInfo SOURCES[] = new SourceInfo[]{
            new SourceInfo(
                    "/home/flo/myProject/maolis/",
                    new String[]{"/home/flo/myProject/maolis/" + "app/",
                            "/home/flo/myProject/maolis/" + "conf/"},
                    new HashMap<String, SourceTypeEnum[]>() {{
                        put("java",new SourceTypeEnum[]{SourceTypeEnum.SERVER});
                        put("vm",new SourceTypeEnum[]{SourceTypeEnum.SERVER});
                        put("jade",new SourceTypeEnum[]{SourceTypeEnum.SITE});
                        put("coffee",new SourceTypeEnum[]{SourceTypeEnum.SITE});
                    }},
                    "/home/flo/myProject/maolis/" + "conf/",
                    Pattern.compile("messages\\..*")
            )
    };

    public static final String[] EXPECTED_REGEXS = new String[]{"--\\.[a-zA-Z0-9._-]+"};
    public static final boolean  DEBUG           = true;
}
