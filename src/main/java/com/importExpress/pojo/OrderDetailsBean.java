package com.importExpress.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author ylm
 * ���������
 */
public class OrderDetailsBean implements Serializable {

    private static final long serialVersionUID = 408217965310342129L;

    private int id;
    private int userid;
    private int goodsid;
    private String goodsname;
    private String goods_url;
    private String goods_img;
    private String goods_type;
    private String orderid;
    private String dropshipid;//dropshiporder订单表id
    private String delivery_time;
    private int checkprice_fee;//询价的费用
    private int checkproduct_fee;//检查产品的费用
    private int state;//状态（0-购买中，1-产品买了并已经到我们仓库）
    private String fileupload;//拍照图片
    private double actual_price;//实际采购价格
    private double actual_freight;//实际国内运费
    private String actual_weight;//实际重量(KG)
    private String actual_volume;//实际体积（长*宽*高）（平方米）
    private String goods_freight;//商品国内运费
    private String paytime;//支付时间
    private int yourorder;
    private String goodsprice;
    private SpiderNewBean spider;
    private String createtime;//
    private String freight;//国内运费
    //交期 和 价格变动 变动后的值
    private String change_delivery;
    private String change_price;
    private String change_freight;
    //新资源地址
    private String newsourceurl;
    //改商品被取消 0没有取消 1已取消
    private int iscancel;
    //商品总费用
    private String product_cost;
    private int freight_free;//是否免邮，0-否，1-是
    private String remaining_price;//订单所欠费用
    private int goodsdata_id;
    private int preferential;//是否是优惠申请 0-否
    private int purchase_state;//货源确认状态 0-未确认，1-已确认，2-用户未确认不能改过状态
    private String purchase_time;//货源确认时间
    private String purchase_confirmation;//货源确认人员
    private String remark;
    private String currency;//货币单位
    private int goods_class;//商品类型
    private String img_type;//商品类型选择图片
    private String inspection_pic;//验货图片
    private String startPrice;//yuan shi mian you jia
    private String bizPriceDiscount;//leibie zhekou

    private Integer isComment;//yyl是否评论
    private String comments_content;//评论的内容
    private Integer isCommentOvertime;//是否超时

    private String car_type;


    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public Integer getIsCommentOvertime() {
        return isCommentOvertime;
    }

    public void setIsCommentOvertime(Integer isCommentOvertime) {
        this.isCommentOvertime = isCommentOvertime;
    }

    public String getComments_content() {
        return comments_content;
    }

    public void setComments_content(String comments_content) {
        this.comments_content = comments_content;
    }

    public Integer getIsComment() {
        return isComment;
    }

    public void setIsComment(Integer isComment) {
        this.isComment = isComment;
    }

    public String getBizPriceDiscount() {
        return bizPriceDiscount;
    }

    public void setBizPriceDiscount(String bizPriceDiscount) {
        this.bizPriceDiscount = bizPriceDiscount;
    }

    public String getInspection_pic() {
        return inspection_pic;
    }

    public void setInspection_pic(String inspection_pic) {
        this.inspection_pic = inspection_pic;
    }

    private String oldUrl;//数据库表中没有相应的
    private double extra_freight;//额外运费金额

    private double discount_ratio;//混批优惠折扣比例

    private String total_weight;
    private String bulk_volume;

    private String goodsfreight;

    private String goodscatid;//商品最小类别
    private int isFeight;//是否升级运费
    private String serUnit;//产品计量单位
    private String od_total_weight;//产品总总量

    private String car_urlMD5;//goods_url的MD5数字指纹

    private String goods_pid;//产品id号，对应goods_car的itemId

    private int groupBuyId;//团购编号，也是团购标识，团购商品不执行降价逻辑:0非团购商品;>0团购商品

    public String getGoods_pid() {
        return goods_pid;
    }

    public void setGoods_pid(String goods_pid) {
        this.goods_pid = goods_pid;
    }

    public String getGoodsUrlMD5() {
        return car_urlMD5;
    }

    public void setGoodsUrlMD5(String goodsUrlMD5) {
        this.car_urlMD5 = goodsUrlMD5;
    }

    public String getOd_total_weight() {
        return od_total_weight;
    }

    public void setOd_total_weight(String od_total_weight) {
        this.od_total_weight = od_total_weight;
    }

    public String getSerUnit() {
        return serUnit;
    }

    public void setSerUnit(String serUnit) {
        this.serUnit = serUnit;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public int getIsFeight() {
        return isFeight;
    }

    public void setIsFeight(int isFeight) {
        this.isFeight = isFeight;
    }

    public void setGoodsdata_id(int goodsdata_id) {
        this.goodsdata_id = goodsdata_id;
    }

    public int getGoodsdata_id() {
        return goodsdata_id;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public String getPaytime() {
        return paytime;
    }

    public SpiderNewBean getSpider() {
        return spider;
    }

    public void setSpider(SpiderNewBean spider) {
        this.spider = spider;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public int getCheckprice_fee() {
        return checkprice_fee;
    }

    public void setCheckprice_fee(int checkprice_fee) {
        this.checkprice_fee = checkprice_fee;
    }

    public int getCheckproduct_fee() {
        return checkproduct_fee;
    }

    public void setCheckproduct_fee(int checkproduct_fee) {
        this.checkproduct_fee = checkproduct_fee;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getFileupload() {
        return fileupload;
    }

    public void setFileupload(String fileupload) {
        this.fileupload = fileupload;
    }

    public int getYourorder() {
        return yourorder;
    }

    public void setYourorder(int yourorder) {
        this.yourorder = yourorder;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(String goodsprice) {
        this.goodsprice = goodsprice;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreatetime() {
        return createtime;
    }


    public double getActual_price() {
        return actual_price;
    }

    public void setActual_price(double actual_price) {
        this.actual_price = actual_price;
    }

    public double getActual_freight() {
        return actual_freight;
    }

    public void setActual_freight(double actual_freight) {
        this.actual_freight = actual_freight;
    }

    public String getActual_weight() {
        return actual_weight;
    }

    public void setActual_weight(String actual_weight) {
        this.actual_weight = actual_weight;
    }

    public String getActual_volume() {
        return actual_volume;
    }

    public void setActual_volume(String actual_volume) {
        this.actual_volume = actual_volume;
    }

    public String getChange_delivery() {
        return change_delivery;
    }

    public void setChange_delivery(String change_delivery) {
        this.change_delivery = change_delivery;
    }

    public String getChange_price() {
        return change_price;
    }

    public void setChange_price(String change_price) {
        this.change_price = change_price;
    }

    public String getNewsourceurl() {
        return newsourceurl;
    }

    public void setNewsourceurl(String newsourceurl) {
        this.newsourceurl = newsourceurl;
    }

    public int getIscancel() {
        return iscancel;
    }

    public void setIscancel(int iscancel) {
        this.iscancel = iscancel;
    }

    public void setProduct_cost(String product_cost) {
        this.product_cost = product_cost;
    }

    public String getProduct_cost() {
        return product_cost;
    }

    public String getChange_freight() {
        return change_freight;
    }

    public void setChange_freight(String change_freight) {
        this.change_freight = change_freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getFreight() {
        return freight;
    }

    public int getFreight_free() {
        return freight_free;
    }

    public void setFreight_free(int freight_free) {
        this.freight_free = freight_free;
    }

    public String getRemaining_price() {
        return remaining_price;
    }

    public void setRemaining_price(String remaining_price) {
        this.remaining_price = remaining_price;
    }

    public void setPreferential(int preferential) {
        this.preferential = preferential;
    }

    public int getPreferential() {
        return preferential;
    }

    public int getPurchase_state() {
        return purchase_state;
    }

    public void setPurchase_state(int purchase_state) {
        this.purchase_state = purchase_state;
    }

    public String getPurchase_time() {
        return purchase_time;
    }

    public void setPurchase_time(String purchase_time) {
        this.purchase_time = purchase_time;
    }

    public String getPurchase_confirmation() {
        return purchase_confirmation;
    }

    public void setPurchase_confirmation(String purchase_confirmation) {
        this.purchase_confirmation = purchase_confirmation;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setGoods_class(int goods_class) {
        this.goods_class = goods_class;
    }

    public int getGoods_class() {
        return goods_class;
    }

    public void setImg_type(String img_type) {
        this.img_type = img_type;
    }

    public String getImg_type() {
        return img_type;
    }

    public String getOldUrl() {
        return oldUrl;
    }

    public void setOldUrl(String oldUrl) {
        this.oldUrl = oldUrl;
    }

    public void setExtra_freight(double extra_freight) {
        this.extra_freight = extra_freight;
    }

    public double getExtra_freight() {
        return extra_freight;
    }

    public String getGoods_url() {
        return goods_url;
    }

    public void setGoods_url(String goods_url) {
        this.goods_url = goods_url;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }

    public void setGoods_freight(String goods_freight) {
        this.goods_freight = goods_freight;
    }

    public String getGoods_freight() {
        return goods_freight;
    }

    public void setDiscount_ratio(double discount_ratio) {
        this.discount_ratio = discount_ratio;
    }

    public double getDiscount_ratio() {
        return discount_ratio;
    }

    public String getTotal_weight() {
        return total_weight;
    }

    public void setTotal_weight(String total_weight) {
        this.total_weight = total_weight;
    }

    public String getBulk_volume() {
        return bulk_volume;
    }

    public void setBulk_volume(String bulk_volume) {
        this.bulk_volume = bulk_volume;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setGoodscatid(String goodscatid) {
        this.goodscatid = goodscatid;
    }

    public String getGoodscatid() {
        return goodscatid;
    }

    @Override
    public String toString() {
        return String
                .format("{\"id\":\"%s\", \"userid\":\"%s\", \"goodsid\":\"%s\", \"goodsname\":\"%s\", \"orderid\":\"%s\", \"delivery_time\":\"%s\", \"checkprice_fee\":\"%s\", \"checkproduct_fee\":\"%s\", \"state\":\"%s\", \"fileupload\":\"%s\", \"yourorder\":\"%s\", \"goodsprice\":\"%s\", \"freight\":\"%s\"}",
                        id, userid, goodsid, goodsname, orderid, delivery_time,
                        checkprice_fee, checkproduct_fee, state, fileupload,
                        yourorder, goodsprice, freight);
    }

    public String getGoodsfreight() {
        return goodsfreight;
    }

    public void setGoodsfreight(String goodsfreight) {
        this.goodsfreight = goodsfreight;
    }

    public String getDropshipid() {
        return dropshipid;
    }

    public void setDropshipid(String dropshipid) {
        this.dropshipid = dropshipid;
    }

    public int getGroupBuyId() {
        return groupBuyId;
    }

    public void setGroupBuyId(int groupBuyId) {
        this.groupBuyId = groupBuyId;
    }

    private List<OrderProductSourceLogBean> productSourceLogList; //tracking页面中商品Status

    public List<OrderProductSourceLogBean> getProductSourceLogList() {
        return productSourceLogList;
    }

    public void setProductSourceLogList(List<OrderProductSourceLogBean> productSourceLogList) {
        this.productSourceLogList = productSourceLogList;
    }
}
