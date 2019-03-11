package com.cbt.bean;

import org.apache.commons.lang3.StringUtils;

public class CustomGoodsBean {
    private int id;//
    private String keyword;// 关键词
    private String catid;// 对应aliexpress类别id
    private String catid1;// 1688类别id
    private String pid;// 产品id
    private String img;// 产品单页展示图片集合
    private String url;// 产品链接
    private String name;// 产品名称-中文
    private String enname;// 产品名称-英文
    private String price;// 产品价格
    private String wprice;// 产品批发价格
    private String volum;// 产品体积-长宽高等
    private String weight;// 产品重量
    private String type;// 产品规格参数（中文）-颜色、尺寸
    private String entype;// 产品规格（英文）-颜色、尺寸
    private String detail;// 产品明细描述-中文
    private String endetail;// 产品明细描述-英文
    private String info;// 产品详情-1688原描述
    private String eninfo;// 产品详情-适应aliexpress描述
    private String sku;//
    private int morder;// 最小订量
    private int sold;// 销量
    private int solds;// 可售数量
    private String feeprice;//
    private String method;// 快递方式
    private String posttime;// 快递时间
    private String fprice;// 运费
    private int valid = -1;// 状态 0-产品上传取消 1-产品上传成功
    private String remotpath;// 线上图片服务器地址
    private String localpath;// 本地图片服务器地址
    private String createtime;// 产品上传时间
    private String updatetime;// 产品上传更新时间
    private String publishtime;// 产品发布时间
    private String admin;// 最近一次操作人员
    private int goodsState;// 2-产品下架 3-发布失败 4-发布成功
    private String lastPrice;//
    private String detailEx;// 翻译详情数据
    private String goodsStateValue;// 货源属性值
    private int isNewCloud;//是否新品云商品
    private String updateTimeAll;
    private String customMainImage;

    private int count;//

    public String getRangePrice() {
        return rangePrice;
    }

    public void setRangePrice(String rangePrice) {
        this.rangePrice = rangePrice;
    }

    private String rangePrice;

    public String getIsSoldFlag() {
        return isSoldFlag;
    }

    public void setIsSoldFlag(String isSoldFlag) {
        this.isSoldFlag = isSoldFlag;
    }

    private String isSoldFlag;

    public String getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(String wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }


    private String wholesalePrice;


    public String getCustomMainImage() {
        return customMainImage;
    }

    public void setCustomMainImage(String customMainImage) {
        this.customMainImage = customMainImage;
    }


    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public int getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(int goodsState) {
        this.goodsState = goodsState;
        if (goodsState > 0) {
            if (goodsState == 1) {
                this.goodsStateValue = "产品发布中";
            }
            if (goodsState == 2) {
                this.goodsStateValue = "产品下架";
            } else if (goodsState == 3) {
                this.goodsStateValue = "发布失败";
            } else if (goodsState == 4) {
                this.goodsStateValue = "发布成功";
            } else if (goodsState == 5) {
                this.goodsStateValue = "产品待发布";
            }
        }
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getPublishtime() {
        return publishtime;
    }

    public void setPublishtime(String publishtime) {
        this.updateTimeAll = publishtime;
        if (publishtime == null || "".equals(publishtime) || publishtime.length() < 10) {
            this.publishtime = publishtime;
        } else {
            this.publishtime = publishtime.substring(0, 10);
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getCatid1() {
        return catid1;
    }

    public void setCatid1(String catid1) {
        this.catid1 = catid1;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
        this.url = "https://www.import-express.com/goodsinfo/cbtconsole-1" + pid + ".html";
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (StringUtils.isBlank(name)) {
            this.name = "1688 商品url";
        } else {
            this.name = name;
        }
    }

    public String getEnname() {
        return enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWprice() {
        return wprice;
    }

    public void setWprice(String wprice) {
        /*if (wprice == null || "".equals(wprice)) {
            this.wprice = "";
        } else {
            this.wprice = wprice.replace("[", "").replace("]", "").replace("$", "@");
        }*/
        this.wprice = wprice;
    }

    public void setWprice(String wprice,int isInsert){
        this.wprice = wprice;
    }

    public String getVolum() {
        return volum;
    }

    public void setVolum(String volum) {
        this.volum = volum;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntype() {
        return entype;
    }

    public void setEntype(String entype) {
        this.entype = entype;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getEndetail() {
        return endetail;
    }

    public void setEndetail(String endetail) {
        this.endetail = endetail;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getEninfo() {
        return eninfo;
    }

    public void setEninfo(String eninfo) {
        this.eninfo = eninfo;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getMorder() {
        return morder;
    }

    public void setMorder(int morder) {
        this.morder = morder;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getSolds() {
        return solds;
    }

    public void setSolds(int solds) {
        this.solds = solds;
    }

    public String getFeeprice() {
        return feeprice;
    }

    public void setFeeprice(String feeprice) {
        /*if (StringUtils.isBlank(feeprice)) {
            this.feeprice = "";
        } else {
            this.feeprice = feeprice.replace("[", "").replace("]", "").replace("$", "@");
        }*/
        this.feeprice  = feeprice;
    }

    public void setFeeprice(String feeprice,int isInsert) {
        this.feeprice  = feeprice;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPosttime() {
        return posttime;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public String getFprice() {
        return fprice;
    }

    public void setFprice(String fprice) {
        this.fprice = fprice;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public String getRemotpath() {
        return remotpath;
    }

    public void setRemotpath(String remotpath) {
        this.remotpath = remotpath;
    }

    public String getLocalpath() {
        return localpath;
    }

    public void setLocalpath(String localpath) {
        this.localpath = localpath;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        if (updatetime == null || "".equals(updatetime) || updatetime.length() < 10) {
            this.updatetime = updatetime;
        } else {
            this.updatetime = updatetime.substring(0, 10);
        }
    }

    public String getDetailEx() {
        return detailEx;
    }

    public void setDetailEx(String detailEx) {
        this.detailEx = detailEx;
    }

    public String getGoodsStateValue() {
        return goodsStateValue;
    }

    public void setGoodsStateValue(String goodsStateValue) {
        this.goodsStateValue = goodsStateValue;
    }


    public int getIsNewCloud() {
        return isNewCloud;
    }

    public void setIsNewCloud(int isNewCloud) {
        this.isNewCloud = isNewCloud;
    }

    public String getUpdateTimeAll() {
        return updateTimeAll;
    }

    public void setUpdateTimeAll(String updateTimeAll) {
        this.updateTimeAll = updateTimeAll;
    }

    @Override
    public String toString() {
        return String.format(
                "CustomGoodsBean [id=%s, keyword=%s, catid=%s, catid1=%s, pid=%s, img=%s, url=%s, name=%s, enname=%s, price=%s, wprice=%s, volum=%s, weight=%s, type=%s, entype=%s, detail=%s, endetail=%s, info=%s, eninfo=%s, sku=%s, morder=%s, sold=%s, solds=%s, feeprice=%s, method=%s, posttime=%s, fprice=%s, valid=%s, remotpath=%s, localpath=%s, createtime=%s, updatetime=%s, count=%s]",
                id, keyword, catid, catid1, pid, img, url, name, enname, price, wprice, volum, weight, type, entype,
                detail, endetail, info, eninfo, sku, morder, sold, solds, feeprice, method, posttime, fprice, valid,
                remotpath, localpath, createtime, updatetime, count);
    }
}
