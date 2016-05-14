package be.flo.translationTool.writer;

import be.flo.translationTool.model.FileInfo;
import be.flo.translationTool.model.SourceInfo;
import be.flo.translationTool.model.TranslationInfo;
import be.flo.translationTool.util.Const;
import be.flo.translationTool.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by florian on 13/05/15.
 */
public class Writer {

    public void write(SourceInfo sourceInfo,HashMap<FileInfo, Map<String, TranslationInfo>> map) throws Exception {

        //load translation file
        File file = new File(sourceInfo.getTranslationDirectory());

        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                Matcher matcher = sourceInfo.getTranslationPattern().matcher(file1.getName());
                if (matcher.find()) {
                    //add
                    completeFile(file1, clone(map));
                    //comment deleted key

                    //savef
                }
            }

        } else {
            throw new Exception(sourceInfo.getTranslationDirectory() + " is not a directory");
        }
    }

    private void completeFile(File file, HashMap<FileInfo, Map<String, TranslationInfo>> map) throws Exception {

        String content = FileUtil.getString(file);

        Pattern pattern = Pattern.compile("(\\n|^)((#)? *(--\\.[a-zA-Z0-9._-]+))=(.+)");

        Matcher matcher1 = Pattern.compile(".*\n").matcher(content);

        Map<String, TranslationInfo> newKey = new HashMap<String, TranslationInfo>();

        String previousLine = "";

        while (matcher1.find()) {

            //each line
            String line = matcher1.group();
            if (line.equals("\n")) {

                //insert last key
                for (Map.Entry<String, TranslationInfo> stringBooleanEntry : newKey.entrySet()) {
                    if (stringBooleanEntry.getValue().isFounded()) {

                        String s = stringBooleanEntry.getKey();

                        String toAdd = s + "=" + s.replace("--.", "") + "\n";

                        content = content.replace(previousLine, previousLine + toAdd);
                    }
                }
                newKey.clear();
            } else {
                previousLine = line;
            }

            if (line.contains("##")) {


                for (Map.Entry<FileInfo, Map<String, TranslationInfo>> fileInfoListEntry : map.entrySet()) {
                    if (line.contains(fileInfoListEntry.getKey().getName())) {
                        fileInfoListEntry.getKey().setFounded(true);

                        newKey = fileInfoListEntry.getValue();
                        break;
                    }
                }
            }

            if(line.contains("--.email.invitation.body")){
                int i = 0;
            }

            Matcher matcher2 = pattern.matcher(line);
            if (matcher2.find()) {
                String key = matcher2.group(4);

                if(key.equals("--.email.invitation.body")){
                    int i = 0;
                }

                //find equivalent in new keys
                boolean founded = false;
                for (Map<String, TranslationInfo> stringBooleanMap : map.values()) {
                    if (stringBooleanMap.containsKey(key)) {
                        stringBooleanMap.put(key, new TranslationInfo());
                        founded = true;
                        if ((matcher2.group(3) != null && matcher2.group(3).length() > 0)) {
                            content = content.replace(matcher2.group(), matcher2.group().replace("#", ""));
                        }
                        break;
                    }
                }
                //disactive
                if (founded == false && (matcher2.group(3) == null || matcher2.group(3).length() == 0)) {
                    content = content.replace(key, "#" + key);
                }
            }

        }

        for (Map.Entry<FileInfo, Map<String, TranslationInfo>> fileInfoListEntry : map.entrySet()) {
            if (!fileInfoListEntry.getKey().isFounded()) {

                boolean first = true;
                for (Map.Entry<String, TranslationInfo> stringBooleanEntry : fileInfoListEntry.getValue().entrySet()) {
                    if (stringBooleanEntry.getValue().isFounded()) {
                        String s = stringBooleanEntry.getKey();

                        //add
                        if (first) {
                            first = false;
                            String info = "\n##  " + fileInfoListEntry.getKey().getName();
                            if (fileInfoListEntry.getKey().getDesc() != null) {
                                info += " (" + fileInfoListEntry.getKey().getDesc() + ")";
                            }
                            info += "\n";

                            content += info;
                        }
                        content += s + "=" + s.replace("--.", "") + "\n";
                    }
                }
            }
        }


//        for (Map.Entry<FileInfo, Set<String>> fileInfoListEntry : map.entrySet()) {
//            boolean first = true;
//            for (String s : fileInfoListEntry.getValue()) {
//                if (!checkList.containsKey(s)) {
//                    //add
//                    if (first) {
//                        first = false;
//                        String info = "\n##  " + fileInfoListEntry.getKey().getName();
//                        if (fileInfoListEntry.getKey().getDesc() != null) {
//                            info += " (" + fileInfoListEntry.getKey().getDesc() + ")";
//                        }
//                        info += "\n";
//
//                        content += info;
//                    }
//                    content += s + "=" + s.replace("--.", "") + "\n";
//                } else if (checkList.get(s).equals(KeyStatus.NOT_ACTIVED)) {
//                    checkList.put(s, KeyStatus.TO_REACTIVE);
//                } else {
//                    checkList.put(s, KeyStatus.FOUNDED);
//                }
//
//            }
//        }
//
//        for (Map.Entry<String, KeyStatus> stringKeyStatusEntry : checkList.entrySet()) {
//            if (stringKeyStatusEntry.getValue().equals(KeyStatus.TO_REACTIVE)) {
//                content = content.replace("//" + stringKeyStatusEntry.getKey(), stringKeyStatusEntry.getKey());
//            } else if (stringKeyStatusEntry.getValue().equals(KeyStatus.NOT_FOUND)) {
//
//            }
//        }

        FileUtil.save(content + "\n\n", "/tmp/" + file.getName(), true);

    }

    public HashMap<FileInfo, Map<String, TranslationInfo>> clone(HashMap<FileInfo, Map<String, TranslationInfo>> toClone) {
        HashMap<FileInfo, Map<String, TranslationInfo>> result = new HashMap<FileInfo, Map<String, TranslationInfo>>();


        for (Map.Entry<FileInfo, Map<String, TranslationInfo>> fileInfoMapEntry : toClone.entrySet()) {
            Map<String, TranslationInfo> c = new HashMap<String, TranslationInfo>();

            for (Map.Entry<String, TranslationInfo> stringBooleanEntry : fileInfoMapEntry.getValue().entrySet()) {
                c.put(new String(stringBooleanEntry.getKey()), new TranslationInfo(stringBooleanEntry.getValue()));
            }

            result.put(new FileInfo(fileInfoMapEntry.getKey()), c);
        }


        return result;
    }


//    private enum KeyStatus {
//        NOT_FOUND,
//        FOUNDED,
//        TO_REACTIVE, NOT_ACTIVED
//    }
}
