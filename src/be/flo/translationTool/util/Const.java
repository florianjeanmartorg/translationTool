package be.flo.translationTool.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by florian on 11/05/15.
 */
public class Const {


    public static final String[] EXPECTED_REGEXS = new String[]{"--\\.[a-zA-Z0-9._-]+"};
    public static final String[] TARGETS         = new String[]{
            "/home/florian/idea/project/public/javascripts/",
            "/home/florian/idea/project/app/"};

    public static final List<String> FORBIDDEN_DIR = Arrays.asList(".target","components");

    public static final List<String> FILE_TYPE = Arrays.asList("java", "html", "js");
}
