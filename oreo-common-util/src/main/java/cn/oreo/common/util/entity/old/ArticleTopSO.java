package cn.oreo.common.util.entity.old;

public class ArticleTopSO {

    private int id;
    private String type;
    private String title;
    private String authorName;
    private int comNum;
    private int readNum;
    private int likeNum;
    private double recomIndex;
    private String headImg;     //头像

    public ArticleTopSO() {
    }

    public ArticleTopSO(int id, String type, String title, String authorName, int comNum, int readNum, int likeNum, double recomIndex, String headImg) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.authorName = authorName;
        this.comNum = comNum;
        this.readNum = readNum;
        this.likeNum = likeNum;
        this.recomIndex = recomIndex;
        this.headImg = headImg;
    }

    @Override
    public String toString() {
        return "ArticleTopSO{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", comNum=" + comNum +
                ", readNum=" + readNum +
                ", likeNum=" + likeNum +
                ", recomIndex=" + recomIndex +
                ", headImg='" + headImg + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getComNum() {
        return comNum;
    }

    public void setComNum(int comNum) {
        this.comNum = comNum;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public double getRecomIndex() {
        return recomIndex;
    }

    public void setRecomIndex(double recomIndex) {
        this.recomIndex = recomIndex;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
