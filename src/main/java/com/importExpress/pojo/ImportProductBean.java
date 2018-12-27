package com.importExpress.pojo;

public class ImportProductBean {
    private  Integer id;
    private String pid;//产品id
    private String name;//产品名称
	private String img;//产品图片
	private String price;//产品价格
	private String url;//产品链接
	private String unit;//单位
    private String catid;
    private String weight;
    private String createTime;
    private String aliPid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAliPid() {
        return aliPid;
    }

    public void setAliPid(String aliPid) {
        this.aliPid = aliPid;
    }

    @Override
    public String toString() {
        return "ImportProductBean{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", name='" + name + '\'' +
                ", img='" + img + '\'' +
                ", price='" + price + '\'' +
                ", url='" + url + '\'' +
                ", unit='" + unit + '\'' +
                ", catid='" + catid + '\'' +
                ", weight='" + weight + '\'' +
                ", createTime='" + createTime + '\'' +
                ", aliPid='" + aliPid + '\'' +
                '}';
    }
}
