package com.cbt.bean;

/**
 * @author ylm
 *货代信息
 */
public class Forwarder {

	private int id;
	private String userid; //用户id
	private String order_no;  //订单号
	private String express_no;//快递跟踪号
	private String logistics_name;//物流跟踪名称
	private String transport_details;//内部运输细节
	private String new_state;//给客户看的最新状态
	private String createtime; //出货时间
	private int isneed;
	private Double settleWeight;//实际计费重量
	private Double shipprice;//实收金额
	private String ship_name;
	private String inputWeight;
	private String inputVolume;
	private String inputPrice;
	private String esWeight;
	private String time;
	//,o.acture_fee,o.weight,o.volume_lwh
	private String shipmentno;
	private String orderid;
	private String remarks;
	private String sweight;
	private String svolume;
	private String sflag;
	private String transportcompany;
	private String shippingtype;
	private String transportcountry;
	private String expressno;
	private String user_id;
	private double freight; //运费
	private double estimatefreight;//预估运费
	private String pdfUrl;//中通下单标签地址
	private String country;//订单目标国家
	private double volumeweight;
	private String types;

	private double optimal_cost;
	private String optimal_company;
	private String ad_country;
	private double actualFreight;//订单实际出货运费

	public String getExchange_rate() {
		return exchange_rate;
	}

	public void setExchange_rate(String exchange_rate) {
		this.exchange_rate = exchange_rate;
	}

	private String exchange_rate;
	public double getActualFreight() {
		return actualFreight;
	}

	public void setActualFreight(double actualFreight) {
		this.actualFreight = actualFreight;
	}

	public String getAd_country() {
		return ad_country;
	}

	public void setAd_country(String ad_country) {
		this.ad_country = ad_country;
	}


	public double getOptimal_cost() {
		return optimal_cost;
	}

	public void setOptimal_cost(double optimal_cost) {
		this.optimal_cost = optimal_cost;
	}

	public String getOptimal_company() {
		return optimal_company;
	}

	public void setOptimal_company(String optimal_company) {
		this.optimal_company = optimal_company;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public double getVolumeweight() {
		return volumeweight;
	}

	public void setVolumeweight(double volumeweight) {
		this.volumeweight = volumeweight;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPdfUrl() {
		return pdfUrl;
	}
	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}
	public double getEstimatefreight() {
		return estimatefreight;
	}
	public void setEstimatefreight(double estimatefreight) {
		this.estimatefreight = estimatefreight;
	}
	private int  zoneId ;  //国家id
	
	private int isDropshipFlag ; // dropship订单
	
	private String senttime;//物流公司提供的出运时间

	public String getSenttime() {
		return senttime;
	}
	public void setSenttime(String senttime) {
		this.senttime = senttime;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
	public String getTransportcountry() {
		return transportcountry;
	}
	public void setTransportcountry(String transportcountry) {
		this.transportcountry = transportcountry;
	}
	public String getExpressno() {
		return expressno;
	}
	public void setExpressno(String expressno) {
		this.expressno = expressno;
	}
	public String getShip_name() {
		return ship_name;
	}
	public String getEsWeight() {
		return esWeight;
	}
	public void setEsWeight(String esWeight) {
		this.esWeight = esWeight;
	}
	public String getInputWeight() {
		return inputWeight;
	}
	public void setInputWeight(String inputWeight) {
		this.inputWeight = inputWeight;
	}
	public String getInputVolume() {
		return inputVolume;
	}
	public void setInputVolume(String inputVolume) {
		this.inputVolume = inputVolume;
	}
	public String getInputPrice() {
		return inputPrice;
	}
	public void setInputPrice(String inputPrice) {
		this.inputPrice = inputPrice;
	}
	public void setShip_name(String ship_name) {
		this.ship_name = ship_name;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	/**
	 * @return the isneed
	 */
	public int getIsneed() {
		return isneed;
	}
	/**
	 * @param isneed the isneed to set
	 */
	public void setIsneed(int isneed) {
		this.isneed = isneed;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public String getExpress_no() {
		return express_no;
	}
	public void setExpress_no(String express_no) {
		this.express_no = express_no;
	}
	public String getLogistics_name() {
		return logistics_name;
	}
	public void setLogistics_name(String logistics_name) {
		this.logistics_name = logistics_name;
	}
	public String getTransport_details() {
		return transport_details;
	}
	public void setTransport_details(String transport_details) {
		this.transport_details = transport_details;
	}
	public String getNew_state() {
		return new_state;
	}
	public void setNew_state(String new_state) {
		this.new_state = new_state;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	
	public Double getSettleWeight() {
		return settleWeight;
	}
	public void setSettleWeight(Double settleWeight) {
		this.settleWeight = settleWeight;
	}
	public Double getShipprice() {
		return shipprice;
	}
	public void setShipprice(Double shipprice) {
		this.shipprice = shipprice;
	}
	@Override
	public String toString() {
		return String
				.format("{\"id\":\"%s\", \"order_no\":\"%s\", \"express_no\":\"%s\", \"logistics_name\":\"%s\", \"transport_details\":\"%s\", \"new_state\":\"%s\", \"createtime\":\"%s\"}",
						id, order_no, express_no, logistics_name,
						transport_details, new_state, createtime);
	}
	public int getZoneId() {
		return zoneId;
	}
	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}
	public double getFreight() {
		return freight;
	}
	public void setFreight(double freight) {
		this.freight = freight;
	}
	public int getIsDropshipFlag() {
		return isDropshipFlag;
	}
	public void setIsDropshipFlag(int isDropshipFlag) {
		this.isDropshipFlag = isDropshipFlag;
	}
	 
	
	
}
