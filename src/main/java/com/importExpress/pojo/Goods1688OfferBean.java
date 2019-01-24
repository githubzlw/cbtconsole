package com.importExpress.pojo;

/**
 * 1688原始产品bean
 */
public class Goods1688OfferBean {

    private Integer id;
    private String goods_name;
    private String price;
    private String shop_id;
    private String bargain_number;
    private String standard;
    private String sku;
    private String color;
    private String pic;
    private String pics;
    private String pics1;
    private String iDetailData;
    private String iDetailConfig;
    private String detail;
    private String detail_url;
    private String weight;
    private String weight_url;
    private String addtime;
    private String serviceid;
    private String tag_name;
    private String catid;
    private String keywords;
    private String average_deliver_time;
    private String dl_flag;
    private String set_weight;
    private String goods_pid;
    private String good_url;
    private int crawl_flag;
    private String shop_name;
    private int from_flag;
    private String aliPid;
    private String aliPrice;
    private String keyWord;
    

    public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getBargain_number() {
        return bargain_number;
    }

    public void setBargain_number(String bargain_number) {
        this.bargain_number = bargain_number;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getPics1() {
        return pics1;
    }

    public void setPics1(String pics1) {
        this.pics1 = pics1;
    }

    public String getiDetailData() {
        return iDetailData;
    }

    public void setiDetailData(String iDetailData) {
        this.iDetailData = iDetailData;
    }

    public String getiDetailConfig() {
        return iDetailConfig;
    }

    public void setiDetailConfig(String iDetailConfig) {
        this.iDetailConfig = iDetailConfig;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getDetail_url() {
        return detail_url;
    }

    public void setDetail_url(String detail_url) {
        this.detail_url = detail_url;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight_url() {
        return weight_url;
    }

    public void setWeight_url(String weight_url) {
        this.weight_url = weight_url;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getAverage_deliver_time() {
        return average_deliver_time;
    }

    public void setAverage_deliver_time(String average_deliver_time) {
        this.average_deliver_time = average_deliver_time;
    }

    public String getDl_flag() {
        return dl_flag;
    }

    public void setDl_flag(String dl_flag) {
        this.dl_flag = dl_flag;
    }

    public String getSet_weight() {
        return set_weight;
    }

    public void setSet_weight(String set_weight) {
        this.set_weight = set_weight;
    }

    public String getGoods_pid() {
        return goods_pid;
    }

    public void setGoods_pid(String goods_pid) {
        this.goods_pid = goods_pid;
    }

    public String getGood_url() {
        return good_url;
    }

    public void setGood_url(String good_url) {
        this.good_url = good_url;
    }

    public int getCrawl_flag() {
        return crawl_flag;
    }

    public void setCrawl_flag(int crawl_flag) {
        this.crawl_flag = crawl_flag;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getFrom_flag() {
        return from_flag;
    }

    public void setFrom_flag(int from_flag) {
        this.from_flag = from_flag;
    }

    public String getAliPid() {
        return aliPid;
    }

    public void setAliPid(String aliPid) {
        this.aliPid = aliPid;
    }

    public String getAliPrice() {
        return aliPrice;
    }

    public void setAliPrice(String aliPrice) {
        this.aliPrice = aliPrice;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":\"")
                .append(id).append('\"');
        sb.append("\"goods_name\":\"")
                .append(goods_name).append('\"');
        sb.append(",\"price\":\"")
                .append(price).append('\"');
        sb.append(",\"shop_id\":\"")
                .append(shop_id).append('\"');
        sb.append(",\"bargain_number\":\"")
                .append(bargain_number).append('\"');
        sb.append(",\"standard\":\"")
                .append(standard).append('\"');
        sb.append(",\"sku\":\"")
                .append(sku).append('\"');
        sb.append(",\"color\":\"")
                .append(color).append('\"');
        sb.append(",\"pic\":\"")
                .append(pic).append('\"');
        sb.append(",\"pics\":\"")
                .append(pics).append('\"');
        sb.append(",\"pics1\":\"")
                .append(pics1).append('\"');
        sb.append(",\"iDetailData\":\"")
                .append(iDetailData).append('\"');
        sb.append(",\"iDetailConfig\":\"")
                .append(iDetailConfig).append('\"');
        sb.append(",\"detail\":\"")
                .append(detail).append('\"');
        sb.append(",\"detail_url\":\"")
                .append(detail_url).append('\"');
        sb.append(",\"weight\":\"")
                .append(weight).append('\"');
        sb.append(",\"weight_url\":\"")
                .append(weight_url).append('\"');
        sb.append(",\"addtime\":\"")
                .append(addtime).append('\"');
        sb.append(",\"serviceid\":\"")
                .append(serviceid).append('\"');
        sb.append(",\"tag_name\":\"")
                .append(tag_name).append('\"');
        sb.append(",\"catid\":\"")
                .append(catid).append('\"');
        sb.append(",\"keywords\":\"")
                .append(keywords).append('\"');
        sb.append(",\"average_deliver_time\":\"")
                .append(average_deliver_time).append('\"');
        sb.append(",\"dl_flag\":\"")
                .append(dl_flag).append('\"');
        sb.append(",\"set_weight\":\"")
                .append(set_weight).append('\"');
        sb.append(",\"goods_pid\":\"")
                .append(goods_pid).append('\"');
        sb.append(",\"good_url\":\"")
                .append(good_url).append('\"');
        sb.append(",\"crawl_flag\":\"")
                .append(crawl_flag).append('\"');
        sb.append(",\"shop_name\":\"")
                .append(shop_name).append('\"');
        sb.append(",\"from_flag\":\"")
                .append(from_flag).append('\"');
        sb.append(",\"aliPid\":\"")
                .append(aliPid).append('\"');
        sb.append(",\"aliPrice\":\"")
                .append(aliPrice).append('\"');
        sb.append(",\"keyWord\":\"")
        		.append(keyWord).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
