package be.flo.translationTool.reader;

import be.flo.translationTool.model.FileInfo;
import be.flo.translationTool.model.SourceInfo;
import be.flo.translationTool.model.SourceTypeEnum;
import be.flo.translationTool.model.TranslationInfo;
import be.flo.translationTool.util.Const;
import be.flo.translationTool.util.FileUtil;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by florian on 11/05/15.
 */
public class Reader {


    //key : file info : get info about the file where the keys was found
    //Value/key : the translation key
    //value/value : info about the translation
    private       HashMap<FileInfo, Map<String, TranslationInfo>> keyMap      = new HashMap<FileInfo, Map<String, TranslationInfo>>();
    private       Set<String>                                     listAllKey  = new HashSet<String>();
    private final FileInfo                                        genericInfo = new FileInfo("generic", null);

    public HashMap<FileInfo, Map<String, TranslationInfo>> getKeyMap() {
        return keyMap;
    }

    public void browse(SourceInfo source) {


        for (String target : source.getTargetDirectories()) {

            File file = new File(target);
            try {
                search(file, target, source);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //TEMP print
        System.out.println(keyMap);


    }

    private void search(File file, String target, SourceInfo sourceInfo) throws Exception {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                search(child, target, sourceInfo);
            }
        } else {
            String[] fileElements = file.getName().split("\\.");
            String type = fileElements[fileElements.length - 1];

            for (Map.Entry<String, SourceTypeEnum[]> stringEntry : sourceInfo.getFileType().entrySet()) {
                if(stringEntry.getKey().contains(type)){

                    if (Const.DEBUG) {
                        System.out.println("-- File read : " + file.getAbsolutePath());
                    }
                    fileAnalyse(file, type, target,stringEntry.getValue());
                }
            }
        }
    }

    private void fileAnalyse(File file, String type, String target, SourceTypeEnum[] sourceTypes) throws Exception {
        String content = FileUtil.getString(file);

        //file name
        FileInfo fileInfo = new FileInfo(file.getAbsolutePath().replace(target, ""), getFileDescription(file, content, type));

        //catch translation by regex
        for (String expectedRegex : Const.EXPECTED_REGEXS) {
            Pattern pattern = Pattern.compile(expectedRegex);

            Matcher matcher = pattern.matcher(content);

            //loop all translations for this regex
            while (matcher.find()) {

                //the translation key
                String key = matcher.group();

                if (Const.DEBUG) {
                    //print the key into the log
                    System.out.println("---- Translation found : " + key);
                }

                //check if the key was already catched by test the listAllKey
                if (!listAllKey.contains(key)) {
                    //if not, add the key into the keyMap

                    //if this is a generic translation, associate to the generic info file
                    if (key.contains("--.generic")) {
                        if (!keyMap.containsKey(genericInfo)) {
                            keyMap.put(genericInfo, new HashMap<String, TranslationInfo>());
                        }
                        keyMap.get(genericInfo).put(key, new TranslationInfo(true,sourceTypes));
                    } else {
                        //if not generic, add the fileInfo if needed and add the key into
                        if (!keyMap.containsKey(fileInfo)) {
                            keyMap.put(fileInfo, new HashMap<String, TranslationInfo>());
                        }

                        listAllKey.add(key);
                        keyMap.get(fileInfo).put(key, new TranslationInfo(true,sourceTypes));
                    }
                }
            }
        }

    }

    private String getFileDescription(File file, String content, String type) {
        //try to found a description
        if (type.equals("html")) {
            Matcher matcher = Pattern.compile("<!-- *([^<>]+) *-->").matcher(content);
            if (matcher.find()) {
                return matcher.group(1);
            }
            ;
        } else if (type.equals("js")) {

            Matcher matcher = Pattern.compile("// *.*").matcher(content);
            if (matcher.find()) {
                return matcher.group();
            }
            ;
        }
        return null;
    }


}
