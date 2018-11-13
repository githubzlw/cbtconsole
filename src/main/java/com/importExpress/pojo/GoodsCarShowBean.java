package com.importExpress.pojo;

import java.io.Serializable;

/**
 * Created by qiqing
 * Date :17/07/25
 * 购物车商品信息实体：添加购物车时，不会变更但需要在页面展示的商品信息
 */
public class GoodsCarShowBean implements Serializable, Comparable<GoodsCarShowBean> {

    /**
     *
     */
    private static final long serialVersionUID = 3411811848501142593L;

    private String goodsUnit;//商品单位

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoodsCarShowBean that = (GoodsCarShowBean) o;

        if (Double.compare(that.price4, price4) != 0) return false;
        if (id != that.id) return false;
        if (userid != that.userid) return false;
        if (goodsUnit != null ? !goodsUnit.equals(that.goodsUnit) : that.goodsUnit != null) return false;
        if (guId != null ? !guId.equals(that.guId) : that.guId != null) return false;
        if (img_type != null ? !img_type.equals(that.img_type) : that.img_type != null) return false;
        if (img_url != null ? !img_url.equals(that.img_url) : that.img_url != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        if (types != null ? !types.equals(that.types) : that.types != null) return false;
        if (width != null ? !width.equals(that.width) : that.width != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
        if (shopId != null ? !shopId.equals(that.shopId) : that.shopId != null) return false;
        if (seller != null ? !seller.equals(that.seller) : that.seller != null) return false;
        if (free_sc_days != null ? !free_sc_days.equals(that.free_sc_days) : that.free_sc_days != null) return false;
        if (aliPosttime != null ? !aliPosttime.equals(that.aliPosttime) : that.aliPosttime != null) return false;
        if (delivery_time != null ? !delivery_time.equals(that.delivery_time) : that.delivery_time != null)
            return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (goods_catid != null ? !goods_catid.equals(that.goods_catid) : that.goods_catid != null) return false;
        return comparePrices != null ? comparePrices.equals(that.comparePrices) : that.comparePrices == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = goodsUnit != null ? goodsUnit.hashCode() : 0;
        result = 31 * result + (guId != null ? guId.hashCode() : 0);
        result = 31 * result + (img_type != null ? img_type.hashCode() : 0);
        result = 31 * result + (img_url != null ? img_url.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        result = 31 * result + (types != null ? types.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        temp = Double.doubleToLongBits(price4);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (shopId != null ? shopId.hashCode() : 0);
        result = 31 * result + (seller != null ? seller.hashCode() : 0);
        result = 31 * result + (free_sc_days != null ? free_sc_days.hashCode() : 0);
        result = 31 * result + (aliPosttime != null ? aliPosttime.hashCode() : 0);
        result = 31 * result + (delivery_time != null ? delivery_time.hashCode() : 0);
        result = 31 * result + userid;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (goods_catid != null ? goods_catid.hashCode() : 0);
        result = 31 * result + (comparePrices != null ? comparePrices.hashCode() : 0);
        return result;
    }

    private String guId;//商品url,type等计算得到的id,可以理解为购物车商品唯一性标志
    private String img_type;//商品规格图片
    private String img_url;//商品图片的url
    private String name;//商品的标题
    private String sessionId;//针对未登录用户，添加购物车时，存入goods_car表，关联sessionId，这块还保留的原因：登陆前后，购物车商品合并时需要用到
    private String types;//商品规格
    //	private String goodsUrlMD5;//url的MD5数字指纹
    private String width;//体积信息   x*y*z格式
    private double price4;//购物车划掉的价格，产品单页中的start price
    private String remark;//客户填写的购物车商品备注
    private int id;//之前的goodsid，暂时不知道有什么用户，先放在这里，保证购物车商品按店铺分类的方法不报错。
    private String shopId;// 网站中的商店ID
    private String seller;// 店家名称
    private String free_sc_days;//免邮对应快递公司的运输时间
    private String aliPosttime;//ali原始运输交期时间
    private String delivery_time;//商品交期，用于显示在购物车页面的
    private int userid;
    private String url;//产品链接地址
    private String goods_catid;//产品的最小类别id
    private String skuid_1688;//1688那边的规格id，用来与sku表做关联，从而能校验购物车数据
    private String comparePrices;//右侧购物车，对比价格框中，其他网站的价格
    private int valid; //2-软下架标记(valid=2)

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public String getSkuid_1688() {
        return skuid_1688;
    }

    public void setSkuid_1688(String skuid_1688) {
        this.skuid_1688 = skuid_1688;
    }

    public String getComparePrices() {
        return comparePrices;
    }

    public void setComparePrices(String comparePrices) {
        this.comparePrices = comparePrices;
    }

    public String getGoods_catid() {
        return goods_catid;
    }

    public void setGoods_catid(String goods_catid) {
        this.goods_catid = goods_catid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getAliPosttime() {
        return aliPosttime;
    }

    public void setAliPosttime(String aliPosttime) {
        this.aliPosttime = aliPosttime;
    }

    public String getFree_sc_days() {
        return free_sc_days;
    }

    public void setFree_sc_days(String free_sc_days) {
        this.free_sc_days = free_sc_days;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getGuId() {
        return guId;
    }

    public void setGuId(String guId) {
        this.guId = guId;
    }

    public String getImg_type() {
        return img_type;
    }

    public void setImg_type(String img_type) {
        this.img_type = img_type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public double getPrice4() {
        return price4;
    }

    public void setPrice4(double price4) {
        this.price4 = price4;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"aliPosttime\":\"");
        builder.append(aliPosttime);
        builder.append("\", \"comparePrices\":\"");
        builder.append(comparePrices);
        builder.append("\", \"delivery_time\":\"");
        builder.append(delivery_time);
        builder.append("\", \"free_sc_days\":\"");
        builder.append(free_sc_days);
        builder.append("\", \"goodsUnit\":\"");
        builder.append(goodsUnit);
        builder.append("\", \"goods_catid\":\"");
        builder.append(goods_catid);
        builder.append("\", \"guId\":\"");
        builder.append(guId);
        builder.append("\", \"id\":\"");
        builder.append(id);
        builder.append("\", \"img_type\":\"");
        builder.append(img_type);
        builder.append("\", \"img_url\":\"");
        builder.append(img_url);
        builder.append("\", \"name\":\"");
        builder.append(name);
        builder.append("\", \"price4\":\"");
        builder.append(price4);
        builder.append("\", \"remark\":\"");
        builder.append(remark);
        builder.append("\", \"seller\":\"");
        builder.append(seller);
        builder.append("\", \"sessionId\":\"");
        builder.append(sessionId);
        builder.append("\", \"shopId\":\"");
        builder.append(shopId);
        builder.append("\", \"types\":\"");
        builder.append(types);
        builder.append("\", \"url\":\"");
        builder.append(url);
        builder.append("\", \"userid\":\"");
        builder.append(userid);
        builder.append("\", \"width\":\"");
        builder.append(width);
        builder.append("\"}");
        return builder.toString();
    }

    @Override
    public int compareTo(GoodsCarShowBean o) {
        if (this.shopId != null) {
            if (this.shopId.compareTo(o.shopId) == 0) {
                return Integer.valueOf(id).compareTo(Integer.valueOf(o.id));
            } else {
                return this.shopId.compareTo(o.shopId);
            }
        }
        return 0;
    }

}
