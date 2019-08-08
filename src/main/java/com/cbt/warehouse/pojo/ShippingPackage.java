package com.cbt.warehouse.pojo;

import com.cbt.util.BigDecimalUtil;
import com.cbt.website.bean.UserOrderDetails;

import java.util.List;

public class ShippingPackage {
	private String id;
	private String shipmentno;
	private String orderid;
	private String remarks;
	private String createtime;
	private String sweight;
	private String svolume;
	private String sflag;
	private UserOrderDetails uod;
	private String day;
	private String gjcode;
	private String zid;
	private String sumcgprice; //采购金额
	private String user_id;
	private String transport;
	private String chinapostbig;
	private String remaining_price;
	private String sumprice;
	private String allprice;
	private String mode_transport;
	private String googs_img;
	private List<String> listImg;
	private String cont;
	private String email;
	private String currency;
	private String userid;
	private String issendmail;
	private String xsbz;
	private String  orderremark ; //用户订单备注
	private String  orderpaytime ;  //订单付款时间
	private int isDropshipFlag ; // dropship 标记
	private double sumextra_freight; //总的额外运费
	private  int  OverTimeFlag ; //订单支付时间超过10天的特殊标记
	//================2017-08-28 whj添加
	private String sentTime;//实际出运时间
	private String volumeweight;//抛重
	private String transportcompany;//运输公司
	private String shippingtype;//运输方式
	private String country;//运输国家
	private String expressno;//运输单号
	private String estimatefreight;//预估运费
	private String totalPrice;//实际运费
	private String reMark;//出运备注
	private String realWeight;//运输公司重量
	private String yjRemark;//预警提交的备注信息
	private double exchange_rate;//
	private double pid_amount;
	private double actual_lwh;//质检费
	private String fedexie;//区域
	private double yfhFreight;
	private double jcexFreight;
	private int cacount;
	private String countryMsg;  // 美国海外国家提醒 yigo /  Guam/ hawaii/Honolulu/Puerto Rico/Virgin Islands/Samoa/Mariana Islands
	/**
	 * 客户付的钱-采购金额-预估运费
	 */
	private double subAmount;

	public double getSubAmount() {
		return subAmount;
	}

	public void setSubAmount(double subAmount) {
		this.subAmount = BigDecimalUtil.truncateDouble(subAmount,2);
	}

	public int getCacount() {
		return cacount;
	}

	public void setCacount(int cacount) {
		this.cacount = cacount;
	}

	public double getYfhFreight() {
		return yfhFreight;
	}

	public void setYfhFreight(double yfhFreight) {
		this.yfhFreight = yfhFreight;
	}

	public double getJcexFreight() {
		return jcexFreight;
	}

	public void setJcexFreight(double jcexFreight) {
		this.jcexFreight = jcexFreight;
	}

	public String getFedexie() {
		return fedexie;
	}

	public void setFedexie(String fedexie) {
		this.fedexie = fedexie;
	}

	public double getActual_lwh() {
		return actual_lwh;
	}

	public void setActual_lwh(double actual_lwh) {
		this.actual_lwh = actual_lwh;
	}
	public double getPid_amount() {
		return pid_amount;
	}

	public void setPid_amount(double pid_amount) {
		this.pid_amount = pid_amount;
	}

	public double getExchange_rate() {
		return exchange_rate;
	}

	public void setExchange_rate(double exchange_rate) {
		this.exchange_rate = exchange_rate;
	}

	public String getYjRemark() {
		return yjRemark;
	}

	public void setYjRemark(String yjRemark) {
		this.yjRemark = yjRemark;
	}

	public String getRealWeight() {
		return realWeight;
	}
	public void setRealWeight(String realWeight) {
		this.realWeight = realWeight;
	}
	public String getSentTime() {
		return sentTime;
	}
	public void setSentTime(String sentTime) {
		this.sentTime = sentTime;
	}
	public String getVolumeweight() {
		return volumeweight;
	}
	public void setVolumeweight(String volumeweight) {
		this.volumeweight = volumeweight;
	}
	public String getTransportcompany() {
		return transportcompany;
	}
	public void setTransportcompany(String transportcompany) {
		this.transportcompany = transportcompany;
	}
	public String getShippingtype() {
		return shippingtype;
	}
	public void setShippingtype(String shippingtype) {
		this.shippingtype = shippingtype;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getExpressno() {
		return expressno;
	}
	public void setExpressno(String expressno) {
		this.expressno = expressno;
	}
	public String getEstimatefreight() {
		return estimatefreight;
	}
	public void setEstimatefreight(String estimatefreight) {
		this.estimatefreight = estimatefreight;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getReMark() {
		return reMark;
	}
	public void setReMark(String reMark) {
		this.reMark = reMark;
	}
	public String getXsbz() {
		return xsbz;
	}
	public void setXsbz(String xsbz) {
		this.xsbz = xsbz;
	}
	public String getIssendmail() {
		return issendmail;
	}
	public void setIssendmail(String issendmail) {
		this.issendmail = issendmail;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getCont() {
		return cont;
	}
	public void setCont(String cont) {
		this.cont = cont;
	}
	public List<String> getListImg() {
		return listImg;
	}
	public void setListImg(List<String> listImg) {
		this.listImg = listImg;
	}
	public String getGoogs_img() {
		return googs_img;
	}
	public void setGoogs_img(String googs_img) {
		this.googs_img = googs_img;
	}
	public String getMode_transport() {
		return mode_transport;
	}
	public void setMode_transport(String mode_transport) {
		this.mode_transport = mode_transport;
	}
	public String getAllprice() {
		return allprice;
	}
	public void setAllprice(String allprice) {
		this.allprice = allprice;
	}
	public String getSumprice() {
		return sumprice;
	}
	public void setSumprice(String sumprice) {
		this.sumprice = sumprice;
	}
	public String getRemaining_price() {
		return remaining_price;
	}
	public void setRemaining_price(String remaining_price) {
		this.remaining_price = remaining_price;
	}
	public String getChinapostbig() {
		return chinapostbig;
	}
	public void setChinapostbig(String chinapostbig) {
		this.chinapostbig = chinapostbig;
	}
	public String getTransport() {
		return transport;
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getSumcgprice() {
		return sumcgprice;
	}
	public void setSumcgprice(String sumcgprice) {
		this.sumcgprice = sumcgprice;
	}
	public String getZid() {
		return zid;
	}
	public void setZid(String zid) {
		this.zid = zid;
	}
	public String getGjcode() {
		return gjcode;
	}
	public void setGjcode(String gjcode) {
		this.gjcode = gjcode;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public UserOrderDetails getUod() {
		return uod;
	}
	public void setUod(UserOrderDetails uod) {
		this.uod = uod;
	}
	public String getSweight() {
		return sweight;
	}
	public void setSweight(String sweight) {
		this.sweight = sweight;
	}
	public String getSvolume() {
		return svolume;
	}
	public void setSvolume(String svolume) {
		this.svolume = svolume;
	}
	public String getSflag() {
		return sflag;
	}
	public void setSflag(String sflag) {
		this.sflag = sflag;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShipmentno() {
		return shipmentno;
	}
	public void setShipmentno(String shipmentno) {
		this.shipmentno = shipmentno;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public int getIsDropshipFlag() {
		return isDropshipFlag;
	}
	public void setIsDropshipFlag(int isDropshipFlag) {
		this.isDropshipFlag = isDropshipFlag;
	}
	public double getSumextra_freight() {
		return sumextra_freight;
	}
	public void setSumextra_freight(double sumextra_freight) {
		this.sumextra_freight = sumextra_freight;
	}
	public String getOrderremark() {
		return orderremark;
	}
	public void setOrderremark(String orderremark) {
		this.orderremark = orderremark;
	}
	public String getOrderpaytime() {
		return orderpaytime;
	}
	public void setOrderpaytime(String orderpaytime) {
		this.orderpaytime = orderpaytime;
	}
	
	public int getOverTimeFlag() {
		return OverTimeFlag;
	}
	public void setOverTimeFlag(int overTimeFlag) {
		this.OverTimeFlag = overTimeFlag;
	}

    public String getCountryMsg() {
        return countryMsg;
    }

    public void setCountryMsg(String countryMsg) {
        this.countryMsg = countryMsg;
    }
}
