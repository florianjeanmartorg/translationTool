package be.flo.translationTool;

import be.flo.translationTool.model.SourceTypeEnum;
import be.flo.translationTool.util.ConverterInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by flo on 14/12/16.
 */
public enum SourceEnum {

    MAOLIS("/home/flo/myProject/maolis/","/home/flo/myProject/demo2/","/home/flo/myProject/maolis-ios/","fr"),
    DEMO("/home/flo/myProject/demo2/",null,null,"fr");

    private final String path;

    private final String androidPath;

    private final String iosPath;

    private final String defaultLanguage;

    SourceEnum(String path, String androidPath, String iosPath,String defaultLanguage) {
        this.path = path;
        this.androidPath = androidPath;
        this.iosPath = iosPath;
        this.defaultLanguage=defaultLanguage;
    }

    public String getAndroidPath() {
        return androidPath;
    }

    public String getIosPath() {
        return iosPath;
    }

    public String getPath() {
        return path;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }
}
