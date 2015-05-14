package be.flo.translationTool.reader;

import be.flo.translationTool.model.FileInfo;
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


    private       HashMap<FileInfo, Map<String,Boolean>> keyMap      = new HashMap<FileInfo, Map<String,Boolean>>();
    private       Set<String>                    listAllKey  = new HashSet<String>();
    private final FileInfo                       genericInfo = new FileInfo("generic", null);

    public HashMap<FileInfo, Map<String,Boolean>> getKeyMap() {
        return keyMap;
    }

    public void browse() {

        for (String target : Const.TARGETS) {

            File file = new File(target);
            try {
                search(file, target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //TEMP print
        System.out.println(keyMap);
    }

    private void search(File file, String target) throws Exception {
        if (file.isDirectory()) {
            if (!Const.FORBIDDEN_DIR.contains(file.getName())) {
                for (File child : file.listFiles()) {
                    search(child, target);
                }
            }
        } else {
            String[] fileElements = file.getName().split("\\.");
            String type = fileElements[fileElements.length - 1];
            if (Const.FILE_TYPE.contains(type)) {
                fileAnalyse(file, type, target);
            }
        }
    }

    private void fileAnalyse(File file, String type, String target) throws Exception {
        String content = FileUtil.getString(file);

        //file name
        FileInfo fileInfo = new FileInfo(file.getAbsolutePath().replace(target, ""), getFileDescription(file, content, type));


        //regex
        Map<String,Boolean> keys = null;
        for (String expectedRegex : Const.EXPECTED_REGEXS) {
            Pattern pattern = Pattern.compile(expectedRegex);

            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String key = matcher.group();
                if (!listAllKey.contains(key)) {
                    if (key.contains("--.generic")) {
                        if (!keyMap.containsKey(genericInfo)) {
                            keyMap.put(genericInfo, new HashMap<String, Boolean>());
                        }
                        keyMap.get(genericInfo).put(key, true);
                    } else {
                        if (keys == null) {
                            keys = new HashMap<String, Boolean>();
                            keyMap.put(fileInfo, keys);
                        }

                        listAllKey.add(key);
                        keys.put(key,true);
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
