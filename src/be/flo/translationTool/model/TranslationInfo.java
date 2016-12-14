package be.flo.translationTool.model;

import be.flo.translationTool.SourceEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flo on 14/05/16.
 */
public class TranslationInfo implements Comparable<TranslationInfo> {

    private boolean              founded     = false;
    private List<SourceTypeEnum> sourceTypes = new ArrayList<SourceTypeEnum>();
    //key : language, value : contents
    private Map<String, String>  contents    = new HashMap<String, String>();
    private int position;

    public TranslationInfo() {
    }

    public TranslationInfo(boolean founded, SourceTypeEnum[] sourceTypes) {
        this.founded = founded;
        for (SourceTypeEnum sourceType : sourceTypes) {

            this.sourceTypes.add(sourceType);
        }
    }

    public TranslationInfo(boolean founded, SourceTypeEnum[] sourceTypes, int position) {
        this.founded = founded;
        this.position = position;
        for (SourceTypeEnum sourceType : sourceTypes) {

            this.sourceTypes.add(sourceType);
        }
    }

    public TranslationInfo(TranslationInfo value) {
        for (SourceTypeEnum sourceType : value.sourceTypes) {
            this.sourceTypes.add(sourceType);
        }

        this.founded = value.founded;
        for (Map.Entry<String, String> entry : value.contents.entrySet()) {
            this.contents.put(entry.getKey(), entry.getValue());
        }
    }

    public boolean isFounded() {
        return founded;
    }

    public void setFounded(boolean founded) {
        this.founded = founded;
    }

    public List<SourceTypeEnum> getSourceTypes() {
        return sourceTypes;
    }

    public void addContent(String lang, String translation) {
        if (!this.contents.containsKey(lang)) {
            this.contents.put(lang, translation);
        }
    }

    public Map<String, String> getContents() {
        return contents;
    }

    public boolean testAviability(SourceEnum sourceEnum) {
        for (SourceTypeEnum typeEnum : this.sourceTypes) {
            switch (typeEnum) {
                case ANDROID:
                    if(sourceEnum.getAndroidPath()!=null){
                        return true;
                    }
                    break;
                case SITE:
                    if(sourceEnum.getPath()!=null){
                        return true;
                    }
                    break;
                case SERVER:
                    if(sourceEnum.getPath()!=null){
                        return true;
                    }
                    break;
                case IOS:
                    if(sourceEnum.getIosPath()!=null){
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public int compareTo(TranslationInfo o) {
        if (this.position == o.position) {
            return 0;
        }
        return (this.position > o.position) ? 1 : -1;
    }

    public void fusion(TranslationInfo translationInfo) {

        //position and founded not relevant

        for (SourceTypeEnum sourceTypeEnum : translationInfo.getSourceTypes()) {
            if (!this.sourceTypes.contains(sourceTypeEnum)) {
                this.sourceTypes.add(sourceTypeEnum);
            }
        }
        for (Map.Entry<String, String> entry : translationInfo.contents.entrySet()) {
            if (!this.contents.containsKey(entry.getKey())) {
                this.contents.put(entry.getKey(), entry.getValue());
            }
        }


    }
}
