package be.flo.translationTool.util;

import be.flo.translationTool.model.SourceInfo;
import be.flo.translationTool.model.SourceTypeEnum;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by florian on 11/05/15.
 */
public interface ConverterInterface {
    public String convert(String s);
}
