package be.flo.translationTool.writer;

import be.flo.translationTool.model.FileInfo;
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

    private static final String  FILE    = "/home/florian/idea/project/conf/";
    private static final Pattern PATTENR = Pattern.compile("messages\\..*");

    public void write(HashMap<FileInfo, Map<String, Boolean>> map) throws Exception {

        //load translation file
        File file = new File(FILE);

        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                Matcher matcher = PATTENR.matcher(file1.getName());
                if (matcher.find()) {
                    //add
                    completeFile(file1, clone(map));
                    //comment deleted key

                    //savef
                }
            }

        } else {
            throw new Exception(FILE + " is not a directory");
        }
    }

    private void completeFile(File file, HashMap<FileInfo, Map<String, Boolean>> map) throws Exception {

        String content = FileUtil.getString(file);


        Pattern pattern = Pattern.compile("(\\n|^)((#)? *(--.+))=(.+)");

        Matcher matcher = pattern.matcher(content);

//        HashMap<String, KeyStatus> checkList = new HashMap<String, KeyStatus>();
//
//        while (matcher.find()) {
//
//        }

        Matcher matcher1 = Pattern.compile(".*\n").matcher(content);

        Map<String, Boolean> newKey = new HashMap<String, Boolean>();

        String previousLine = "";

        while (matcher1.find()) {

            //each line
            String line = matcher1.group();
            if (line.equals("\n")) {

                //insert last key
                for (Map.Entry<String, Boolean> stringBooleanEntry : newKey.entrySet()) {
                    if (stringBooleanEntry.getValue()) {

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


                for (Map.Entry<FileInfo, Map<String, Boolean>> fileInfoListEntry : map.entrySet()) {
                    if (line.contains(fileInfoListEntry.getKey().getName())) {
                        fileInfoListEntry.getKey().setFounded(true);

                        newKey = fileInfoListEntry.getValue();
                        break;
                    }
                }
            }

            Matcher matcher2 = pattern.matcher(line);
            if (matcher2.find()) {
                String key = matcher2.group(4);

                //find equivalent in new keys
                boolean founded = false;
                for (Map<String, Boolean> stringBooleanMap : map.values()) {
                    if (stringBooleanMap.containsKey(key)) {
                        stringBooleanMap.put(key, false);
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

        for (Map.Entry<FileInfo, Map<String, Boolean>> fileInfoListEntry : map.entrySet()) {
            if (!fileInfoListEntry.getKey().isFounded()) {

                boolean first = true;
                for (Map.Entry<String, Boolean> stringBooleanEntry : fileInfoListEntry.getValue().entrySet()) {
                    if (stringBooleanEntry.getValue()) {
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

    public HashMap<FileInfo, Map<String, Boolean>> clone(HashMap<FileInfo, Map<String, Boolean>> toClone) {
        HashMap<FileInfo, Map<String, Boolean>> result = new HashMap<FileInfo, Map<String, Boolean>>();


        for (Map.Entry<FileInfo, Map<String, Boolean>> fileInfoMapEntry : toClone.entrySet()) {
            Map<String, Boolean> c = new HashMap<String, Boolean>();

            for (Map.Entry<String, Boolean> stringBooleanEntry : fileInfoMapEntry.getValue().entrySet()) {
                c.put(new String(stringBooleanEntry.getKey()), new Boolean(stringBooleanEntry.getValue()));
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
