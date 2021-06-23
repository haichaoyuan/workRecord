package com.example.module_homepage2.base;



//@DatabaseTable(tableName = "tb_hotinfo")
//public class HotInfo extends BaseDaoEnabled<HotInfo, Integer> {
public class HotInfo {
//    @DatabaseField(generatedId = true)
    private int id;

//    @DatabaseField
    private String title;

//    @DatabaseField
    private String updatetime;

//    @DatabaseField
    private String ext;

//    @DatabaseField
    private String cover;

//    @DatabaseField
    private String readNum;

//    @DatabaseField
    private String stickTop;


    public HotInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getReadNum() {
        return readNum;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getStickTop() {
        return stickTop;
    }

    public void setStickTop(String stickTop) {
        this.stickTop = stickTop;
    }
}
