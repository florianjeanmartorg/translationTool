package be.flo.translationTool.model;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by flo on 14/05/16.
 */
public class SourceInfo {

    private String path;

    private Map<String, SourceTypeEnum[]> fileType = new HashMap<String, SourceTypeEnum[]>();

    private String[] targetDirectories;

    private String translationDirectory;

    private Pattern translationPattern;

    public SourceInfo(String path, String[] targetDirectories, Map<String, SourceTypeEnum[]> fileType) {
        this.fileType = fileType;
        this.path = path;
        this.targetDirectories = targetDirectories;
    }

    public SourceInfo(String path, String[] targetDirectories, Map<String, SourceTypeEnum[]> fileType, String translationDirectory, Pattern translationPattern) {
        this.fileType = fileType;
        this.path = path;
        this.targetDirectories = targetDirectories;
        this.translationDirectory = translationDirectory;
        this.translationPattern = translationPattern;
    }

    public Map<String, SourceTypeEnum[]> getFileType() {
        return fileType;
    }

    public String getPath() {
        return path;
    }

    public String[] getTargetDirectories() {
        return targetDirectories;
    }

    public String getTranslationDirectory() {
        return translationDirectory;
    }

    public Pattern getTranslationPattern() {
        return translationPattern;
    }
}
