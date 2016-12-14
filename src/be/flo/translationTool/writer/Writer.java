package be.flo.translationTool.writer;

import be.flo.translationTool.SourceEnum;
import be.flo.translationTool.model.FileInfo;
import be.flo.translationTool.model.TranslationInfo;
import be.flo.translationTool.util.Const;
import be.flo.translationTool.util.FileUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by florian on 13/05/15.
 */
public class Writer {

    public void write(SourceEnum sourceInfo, HashMap<FileInfo, Map<String, TranslationInfo>> map) throws Exception {

        //load translation file
        File file = new File(sourceInfo.getPath() + Const.TRANSLATION_DIRECTORIES);

        if (file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                Matcher matcher = Pattern.compile(Const.TRANSLATION_FILE_PATTERN).matcher(file1.getName());
                if (matcher.find()) {
                    //add
                    completeFile(sourceInfo, file1, clone(map), map, matcher.group(1));
                    //comment deleted key

                    //savef
                }
            }

        } else {
            throw new Exception(sourceInfo.getPath() + Const.TRANSLATION_DIRECTORIES + " is not a directory");
        }
    }

    private void completeFile(SourceEnum sourceInfo,
                              File file,
                              HashMap<FileInfo, Map<String, TranslationInfo>> map,
                              HashMap<FileInfo, Map<String, TranslationInfo>> map2,
                              String lang) throws Exception {

        String content = FileUtil.getString(file);

        Matcher matcher1 = Pattern.compile(".*\n").matcher(content);

        Map<String, TranslationInfo> newKey = new HashMap<String, TranslationInfo>();

        String previousLine = "";
        int counter = 0;

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

            Matcher matcher2 = Pattern.compile(Const.CATCH_TRANSLATION_CONTENT).matcher(line);
            if (matcher2.find()) {
                String key = matcher2.group(4);
                String translation = matcher2.group(5);

                //find equivalent in new keys
                boolean founded = false;
                for (Map<String, TranslationInfo> stringBooleanMap : map.values()) {
                    if (stringBooleanMap.containsKey(key)) {
                        stringBooleanMap.put(key, new TranslationInfo());
                        founded = true;
                        if ((matcher2.group(3) != null && matcher2.group(3).length() > 0)) {
                            content = content.replace(matcher2.group(), matcher2.group().replace("#", ""));
                        }

                        //add translation content
                        for (Map.Entry<FileInfo, Map<String, TranslationInfo>> fileInfoMapEntry : map2.entrySet()) {
                            if (fileInfoMapEntry.getValue().containsKey(key)) {
                                fileInfoMapEntry.getValue().get(key).addContent(lang, translation);
                                fileInfoMapEntry.getValue().get(key).setPosition(counter);
                            }
                        }

                        break;
                    }
                }

                //disactive
                if (!founded && (matcher2.group(3) == null || matcher2.group(3).length() == 0)) {
                    content = content.replace(key, "#" + key);
                }

            }
            counter++;

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

        FileUtil.save(content + "\n\n", sourceInfo.getPath()+Const.TRANSLATION_DIRECTORIES+"/_" + file.getName(), true);

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
