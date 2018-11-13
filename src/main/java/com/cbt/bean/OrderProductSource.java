package com.cbt.bean;

import java.util.Date;

public class OrderProductSource {

	private Integer id;
	
    private Integer odId;

    private Integer adminid;

    private Integer userid;

    private Date addtime;

    private String orderid;

    private Integer confirmUserid;

    private String confirmTime;

    private Integer goodsid;

    private Integer goodsdataid;

    private String goodsUrl;

    private String goodsPUrl;

    private String goodsImgUrl;

    private Double goodsPrice;

    private String currency;

    private String goodsPPrice;

    private String goodsName;

    private String goodsPName;

    private Integer moq;

    private String goodssourcetype;

    private Integer usecount;

    private Integer buycount;

    private Double goodsCShipcost;

    private Integer del;

    private Integer purchaseState;

    private Double purchaseprice;

    private String remark;
    
    private String car_type;//aliexpress商品规格  whj 2017-04-13
    
    private String car_img;//aliexpress商品图片  whj 2017-04-13
    
    private String buyerOrderid;//采购订单号
    
    private String imgurl;//采商品的图片

    /**
     * 采购订单备注信息
     */
    private String bargainRemark;
    private String deliveryRemark ;
    private String colorReplaceRemark;
    private String sizeReplaceRemark;
    private String orderNumRemarks;
    private String questionsRemarks;
    private String unquestionsRemarks;
    private String againRemarks;

    private String re_shipnos;
    private String shipno;

	public String getOdids() {
		return odids;
	}

	public void setOdids(String odids) {
		this.odids = odids;
	}

	private String odids;
    public String getShipno() {
        return shipno;
    }

    public void setShipno(String shipno) {
        this.shipno = shipno;
    }

    public String getRe_shipnos() {
        return re_shipnos;
    }

    public void setRe_shipnos(String re_shipnos) {
        this.re_shipnos = re_shipnos;
    }

	public String getBargainRemark() {
		return bargainRemark;
	}

	public void setBargainRemark(String bargainRemark) {
		this.bargainRemark = bargainRemark;
	}

	public String getDeliveryRemark() {
		return deliveryRemark;
	}

	public void setDeliveryRemark(String deliveryRemark) {
		this.deliveryRemark = deliveryRemark;
	}

	public String getColorReplaceRemark() {
		return colorReplaceRemark;
	}

	public void setColorReplaceRemark(String colorReplaceRemark) {
		this.colorReplaceRemark = colorReplaceRemark;
	}

	public String getSizeReplaceRemark() {
		return sizeReplaceRemark;
	}

	public void setSizeReplaceRemark(String sizeReplaceRemark) {
		this.sizeReplaceRemark = sizeReplaceRemark;
	}

	public String getOrderNumRemarks() {
		return orderNumRemarks;
	}

	public void setOrderNumRemarks(String orderNumRemarks) {
		this.orderNumRemarks = orderNumRemarks;
	}

	public String getQuestionsRemarks() {
		return questionsRemarks;
	}

	public void setQuestionsRemarks(String questionsRemarks) {
		this.questionsRemarks = questionsRemarks;
	}

	public String getUnquestionsRemarks() {
		return unquestionsRemarks;
	}

	public void setUnquestionsRemarks(String unquestionsRemarks) {
		this.unquestionsRemarks = unquestionsRemarks;
	}

	public String getAgainRemarks() {
		return againRemarks;
	}

	public void setAgainRemarks(String againRemarks) {
		this.againRemarks = againRemarks;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getBuyerOrderid() {
		return buyerOrderid;
	}

	public void setBuyerOrderid(String buyerOrderid) {
		this.buyerOrderid = buyerOrderid;
	}

	public String getCar_img() {
		return car_img;
	}

	public void setCar_img(String car_img) {
		this.car_img = car_img;
	}

	public String getCar_type() {
		return car_type;
	}

	public void setCar_type(String car_type) {
		this.car_type = car_type;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOdId() {
        return odId;
    }

    public void setOdId(Integer odId) {
        this.odId = odId;
    }

    public Integer getAdminid() {
        return adminid;
    }

    public void setAdminid(Integer adminid) {
        this.adminid = adminid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    public Integer getConfirmUserid() {
        return confirmUserid;
    }

    public void setConfirmUserid(Integer confirmUserid) {
        this.confirmUserid = confirmUserid;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public Integer getGoodsdataid() {
        return goodsdataid;
    }

    public void setGoodsdataid(Integer goodsdataid) {
        this.goodsdataid = goodsdataid;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl == null ? null : goodsUrl.trim();
    }

    public String getGoodsPUrl() {
        return goodsPUrl;
    }

    public void setGoodsPUrl(String goodsPUrl) {
        this.goodsPUrl = goodsPUrl == null ? null : goodsPUrl.trim();
    }

    public String getGoodsImgUrl() {
        return goodsImgUrl;
    }

    public void setGoodsImgUrl(String goodsImgUrl) {
        this.goodsImgUrl = goodsImgUrl == null ? null : goodsImgUrl.trim();
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public String getGoodsPPrice() {
        return goodsPPrice;
    }

    public void setGoodsPPrice(String goodsPPrice) {
        this.goodsPPrice = goodsPPrice == null ? null : goodsPPrice.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getGoodsPName() {
        return goodsPName;
    }

    public void setGoodsPName(String goodsPName) {
        this.goodsPName = goodsPName == null ? null : goodsPName.trim();
    }

    public Integer getMoq() {
        return moq;
    }

    public void setMoq(Integer moq) {
        this.moq = moq;
    }

    public String getGoodssourcetype() {
        return goodssourcetype;
    }

    public void setGoodssourcetype(String goodssourcetype) {
        this.goodssourcetype = goodssourcetype == null ? null : goodssourcetype.trim();
    }

    public Integer getUsecount() {
        return usecount;
    }

    public void setUsecount(Integer usecount) {
        this.usecount = usecount;
    }

    public Integer getBuycount() {
        return buycount;
    }

    public void setBuycount(Integer buycount) {
        this.buycount = buycount;
    }

    public Double getGoodsCShipcost() {
        return goodsCShipcost;
    }

    public void setGoodsCShipcost(Double goodsCShipcost) {
        this.goodsCShipcost = goodsCShipcost;
    }

    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public Integer getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(Integer purchaseState) {
        this.purchaseState = purchaseState;
    }

    public Double getPurchaseprice() {
        return purchaseprice;
    }

    public void setPurchaseprice(Double purchaseprice) {
        this.purchaseprice = purchaseprice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}