package com.cbt.warehouse.pojo;

import com.importExpress.pojo.OrderProductSourceLogBean;

import java.io.Serializable;
import java.util.List;

public class OrderDetailsBeans implements Serializable {
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
    private SpiderBean spider;
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
    private int isFreeShipProduct;//是否是免邮商品；0-无免邮价；1-老客户订单商品（强制要有免邮价）；2-有免邮价商品
    private String guid;
    private int inventoryid;//库存行id
    private int inventoryNumber;//库存数量
    private String oldUrl;//数据库表中没有相应的
    private double extra_freight;//额外运费金额
    private double discount_ratio;//混批优惠折扣比例
    private String total_weight;
    private String bulk_volume;
    private String goodsfreight;
    private String goodscatid;//商品最小类别
    private int isFeight;//是否升级运费  这块标识暂时不用，用来作为dropship订单标识： 3-国内库存; 4-海外仓 qiqing 2018/09/12
    private String serUnit;//产品计量单位
    private String od_total_weight;//产品总总量
    private String car_urlMD5;//goods_url的MD5数字指纹
    private String goods_pid;//产品id号，对应goods_car的itemId
    private int groupBuyId;//团购编号，也是团购标识，团购商品不执行降价逻辑:0非团购商品;>0团购商品
    private List<OrderProductSourceLogBean> productSourceLogList; //tracking页面中商品Status

    private String dsShipMethod;//运输方式
    private String dsShippingTime;//运输时间
    private String stateName;//
    private String address;//
    private String address2;//
    private String phoneNumber;//
    private String zipCode;//
    private String street;//
    private String recipients;//
    private String dsCountry;

    private int isFreeFlag; //产品是否免邮
    private String finalWeight;// 我司产品重量

    public int getIsFreeFlag() {
        return isFreeFlag;
    }

    public void setIsFreeFlag(int isFreeFlag) {
        this.isFreeFlag = isFreeFlag;
    }

    public String getFinalWeight() {
        return finalWeight;
    }

    public void setFinalWeight(String finalWeight) {
        this.finalWeight = finalWeight;
    }

    public String getDsCountry() {
        return dsCountry;
    }

    public void setDsCountry(String dsCountry) {
        this.dsCountry = dsCountry;
    }

    public String getDsShipMethod() {
        return dsShipMethod;
    }

    public void setDsShipMethod(String dsShipMethod) {
        this.dsShipMethod = dsShipMethod;
    }

    public String getDsShippingTime() {
        return dsShippingTime;
    }

    public void setDsShippingTime(String dsShippingTime) {
        this.dsShippingTime = dsShippingTime;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    /**
     *仓库验货是否有库存  0：没有  1 ：有
     */
    private  int isStockFlag;
    private  int shopCount;

    public int getIsStockFlag() {
        return isStockFlag;
    }

    public void setIsStockFlag(int isStockFlag) {
        this.isStockFlag = isStockFlag;
    }

    public int getShopCount() {
        return shopCount;
    }

    public void setShopCount(int shopCount) {
        this.shopCount = shopCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
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

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getDropshipid() {
        return dropshipid;
    }

    public void setDropshipid(String dropshipid) {
        this.dropshipid = dropshipid;
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

    public String getGoods_freight() {
        return goods_freight;
    }

    public void setGoods_freight(String goods_freight) {
        this.goods_freight = goods_freight;
    }

    public String getPaytime() {
        return paytime;
    }

    public void setPaytime(String paytime) {
        this.paytime = paytime;
    }

    public int getYourorder() {
        return yourorder;
    }

    public void setYourorder(int yourorder) {
        this.yourorder = yourorder;
    }

    public String getGoodsprice() {
        return goodsprice;
    }

    public void setGoodsprice(String goodsprice) {
        this.goodsprice = goodsprice;
    }

    public SpiderBean getSpider() {
        return spider;
    }

    public void setSpider(SpiderBean spider) {
        this.spider = spider;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
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

    public String getChange_freight() {
        return change_freight;
    }

    public void setChange_freight(String change_freight) {
        this.change_freight = change_freight;
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

    public String getProduct_cost() {
        return product_cost;
    }

    public void setProduct_cost(String product_cost) {
        this.product_cost = product_cost;
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

    public int getGoodsdata_id() {
        return goodsdata_id;
    }

    public void setGoodsdata_id(int goodsdata_id) {
        this.goodsdata_id = goodsdata_id;
    }

    public int getPreferential() {
        return preferential;
    }

    public void setPreferential(int preferential) {
        this.preferential = preferential;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getGoods_class() {
        return goods_class;
    }

    public void setGoods_class(int goods_class) {
        this.goods_class = goods_class;
    }

    public String getImg_type() {
        return img_type;
    }

    public void setImg_type(String img_type) {
        this.img_type = img_type;
    }

    public String getInspection_pic() {
        return inspection_pic;
    }

    public void setInspection_pic(String inspection_pic) {
        this.inspection_pic = inspection_pic;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public String getBizPriceDiscount() {
        return bizPriceDiscount;
    }

    public void setBizPriceDiscount(String bizPriceDiscount) {
        this.bizPriceDiscount = bizPriceDiscount;
    }

    public Integer getIsComment() {
        return isComment;
    }

    public void setIsComment(Integer isComment) {
        this.isComment = isComment;
    }

    public String getComments_content() {
        return comments_content;
    }

    public void setComments_content(String comments_content) {
        this.comments_content = comments_content;
    }

    public Integer getIsCommentOvertime() {
        return isCommentOvertime;
    }

    public void setIsCommentOvertime(Integer isCommentOvertime) {
        this.isCommentOvertime = isCommentOvertime;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public int getIsFreeShipProduct() {
        return isFreeShipProduct;
    }

    public void setIsFreeShipProduct(int isFreeShipProduct) {
        this.isFreeShipProduct = isFreeShipProduct;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public int getInventoryid() {
        return inventoryid;
    }

    public void setInventoryid(int inventoryid) {
        this.inventoryid = inventoryid;
    }

    public int getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(int inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getOldUrl() {
        return oldUrl;
    }

    public void setOldUrl(String oldUrl) {
        this.oldUrl = oldUrl;
    }

    public double getExtra_freight() {
        return extra_freight;
    }

    public void setExtra_freight(double extra_freight) {
        this.extra_freight = extra_freight;
    }

    public double getDiscount_ratio() {
        return discount_ratio;
    }

    public void setDiscount_ratio(double discount_ratio) {
        this.discount_ratio = discount_ratio;
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

    public String getGoodsfreight() {
        return goodsfreight;
    }

    public void setGoodsfreight(String goodsfreight) {
        this.goodsfreight = goodsfreight;
    }

    public String getGoodscatid() {
        return goodscatid;
    }

    public void setGoodscatid(String goodscatid) {
        this.goodscatid = goodscatid;
    }

    public int getIsFeight() {
        return isFeight;
    }

    public void setIsFeight(int isFeight) {
        this.isFeight = isFeight;
    }

    public String getSerUnit() {
        return serUnit;
    }

    public void setSerUnit(String serUnit) {
        this.serUnit = serUnit;
    }

    public String getOd_total_weight() {
        return od_total_weight;
    }

    public void setOd_total_weight(String od_total_weight) {
        this.od_total_weight = od_total_weight;
    }

    public String getGoodsUrlMD5() {
        return car_urlMD5;
    }

    public void setGoodsUrlMD5(String car_urlMD5) {
        this.car_urlMD5 = car_urlMD5;
    }

    public String getGoods_pid() {
        return goods_pid;
    }

    public void setGoods_pid(String goods_pid) {
        this.goods_pid = goods_pid;
    }

    public int getGroupBuyId() {
        return groupBuyId;
    }

    public void setGroupBuyId(int groupBuyId) {
        this.groupBuyId = groupBuyId;
    }

    public List<OrderProductSourceLogBean> getProductSourceLogList() {
        return productSourceLogList;
    }

    public void setProductSourceLogList(List<OrderProductSourceLogBean> productSourceLogList) {
        this.productSourceLogList = productSourceLogList;
    }

    @Override
    public String toString() {
        return "OrderDetailsBeans{" + "id=" + id + ", userid=" + userid + ", goodsid=" + goodsid + ", goodsname='" + goodsname + '\'' + ", goods_url='" + goods_url + '\'' + ", goods_img='" + goods_img + '\'' + ", goods_type='" + goods_type + '\'' + ", orderid='" + orderid + '\'' + ", dropshipid='" + dropshipid + '\'' + ", delivery_time='" + delivery_time + '\'' + ", checkprice_fee=" + checkprice_fee + ", checkproduct_fee=" + checkproduct_fee + ", state=" + state + ", fileupload='" + fileupload + '\'' + ", actual_price=" + actual_price + ", actual_freight=" + actual_freight + ", actual_weight='" + actual_weight + '\'' + ", actual_volume='" + actual_volume + '\'' + ", goods_freight='" + goods_freight + '\'' + ", paytime='" + paytime + '\'' + ", yourorder=" + yourorder + ", goodsprice='" + goodsprice + '\'' + ", spider=" + spider + ", createtime='" + createtime + '\'' + ", freight='" + freight + '\'' + ", change_delivery='" + change_delivery + '\'' + ", change_price='" + change_price + '\'' + ", change_freight='" + change_freight + '\'' + ", newsourceurl='" + newsourceurl + '\'' + ", iscancel=" + iscancel + ", product_cost='" + product_cost + '\'' + ", freight_free=" + freight_free + ", remaining_price='" + remaining_price + '\'' + ", goodsdata_id=" + goodsdata_id + ", preferential=" + preferential + ", purchase_state=" + purchase_state + ", purchase_time='" + purchase_time + '\'' + ", purchase_confirmation='" + purchase_confirmation + '\'' + ", remark='" + remark + '\'' + ", currency='" + currency + '\'' + ", goods_class=" + goods_class + ", img_type='" + img_type + '\'' + ", inspection_pic='" + inspection_pic + '\'' + ", startPrice='" + startPrice + '\'' + ", bizPriceDiscount='" + bizPriceDiscount + '\'' + ", isComment=" + isComment + ", comments_content='" + comments_content + '\'' + ", isCommentOvertime=" + isCommentOvertime + ", car_type='" + car_type + '\'' + ", isFreeShipProduct=" + isFreeShipProduct + ", guid='" + guid + '\'' + ", inventoryid=" + inventoryid + ", inventoryNumber=" + inventoryNumber + ", oldUrl='" + oldUrl + '\'' + ", extra_freight=" + extra_freight + ", discount_ratio=" + discount_ratio + ", total_weight='" + total_weight + '\'' + ", bulk_volume='" + bulk_volume + '\'' + ", goodsfreight='" + goodsfreight + '\'' + ", goodscatid='" + goodscatid + '\'' + ", isFeight=" + isFeight + ", serUnit='" + serUnit + '\'' + ", od_total_weight='" + od_total_weight + '\'' + ", car_urlMD5='" + car_urlMD5 + '\'' + ", goods_pid='" + goods_pid + '\'' + ", groupBuyId=" + groupBuyId + ", productSourceLogList=" + productSourceLogList + '}';
    }
}
