package be.flo.translationTool.model;

/**
 * Created by florian on 13/05/15.
 */
public class FileInfo {

    private String name;

    private String desc;

    private boolean founded=false;

    public FileInfo(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public FileInfo(FileInfo fileInfo){
        this.name=fileInfo.getName();
        this.desc=fileInfo.getDesc();
        this.founded=fileInfo.isFounded();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isFounded() {
        return founded;
    }

    public void setFounded(boolean founded) {
        this.founded = founded;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileInfo fileInfo = (FileInfo) o;

        if (name != null ? !name.equals(fileInfo.name) : fileInfo.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
