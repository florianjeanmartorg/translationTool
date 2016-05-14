package be.flo.translationTool.model;

/**
 * Created by flo on 14/05/16.
 */
public class TranslationInfo {

    private boolean founded = false;
    private boolean website = false;
    private boolean server  = false;
    private boolean android = false;
    private boolean ios     = false;

    public TranslationInfo() {
    }

    public TranslationInfo(boolean founded, SourceTypeEnum[] sourceTypes) {
        this.founded = founded;
        for (SourceTypeEnum sourceType : sourceTypes) {

            switch (sourceType) {
                case ANDROID:
                    this.android = true;
                    break;
                case SITE:
                    this.website = true;
                    break;
                case SERVER:
                    this.server = true;
                    break;
                case IOS:
                    this.ios = true;
                    break;
            }
        }
    }

    public TranslationInfo(TranslationInfo value) {
        this.founded = value.founded;
        this.website = value.website;
        this.server = value.server;
        this.android = value.android;
        this.ios = value.ios;

    }

    public boolean isAndroid() {
        return android;
    }

    public void setAndroid(boolean android) {
        this.android = android;
    }

    public boolean isFounded() {
        return founded;
    }

    public void setFounded(boolean founded) {
        this.founded = founded;
    }

    public boolean isIos() {
        return ios;
    }

    public void setIos(boolean ios) {
        this.ios = ios;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }

    public boolean isWebsite() {
        return website;
    }

    public void setWebsite(boolean website) {
        this.website = website;
    }
}
