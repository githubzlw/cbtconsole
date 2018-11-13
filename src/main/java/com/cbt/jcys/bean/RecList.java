package com.cbt.jcys.bean;

import java.util.List;

public class RecList {
	private String iID;  //0,
	private String nItemType;  //1, 快件类型，默认为1。取值为：0(文件),1(包裹),2(防水袋)
	private String cRNo;  //"sunvary40564",  参考号,0-30字符。(传用户系统订单号，只允许数字和字母，中划线，其他符号不接受）
	private String cDes;  //"United States", 收件国家名,1-63字符，【中文或二字代码】。
	private String fWeight;  //0.000,  重量，公斤，3位小数
	private String cReceiver;  //"Lorna Holewinski",  收件人,0-63字符。
	private String cRPhone;  //"715-741-0323",  收件电话,0-63字符。
	private String cREMail;  //"",         收件电邮,0-63字符。
	private String cRPostcode;  //"54494", 收件邮编,0-15字符。
	private String cRCountry;  //"United States",  收件国家【必须为英文】,0-126字符。
	private String cRProvince;  //"",	收件省州,0-63字符。
	private String cRCity;  //"",          收件城市,0-126字符。
	private String cRAddr;  //"7920 Pine Haven Court  Wisconsin Rapids, WI 54494",  收件地址,0-254字符。.
	private List<GoodsPojo> GoodsList;
	private String cEmsKind;
	private String cGoods;

	public String getcGoodsA() {
		return cGoodsA;
	}

	public void setcGoodsA(String cGoodsA) {
		this.cGoodsA = cGoodsA;
	}

	private String cGoodsA;
	public String getcGoods() {
		return cGoods;
	}

	public void setcGoods(String cGoods) {
		this.cGoods = cGoods;
	}


	
	public String getcEmsKind() {
		return cEmsKind;
	}
	public void setcEmsKind(String cEmsKind) {
		this.cEmsKind = cEmsKind;
	}
	public List<GoodsPojo> getGoodsList() {
		return GoodsList;
	}
	public void setGoodsList(List<GoodsPojo> goodsList) {
		GoodsList = goodsList;
	}
	public String getiID() {
		return iID;
	}
	public void setiID(String iID) {
		this.iID = iID;
	}
	public String getnItemType() {
		return nItemType;
	}
	public void setnItemType(String nItemType) {
		this.nItemType = nItemType;
	}
	public String getcRNo() {
		return cRNo;
	}
	public void setcRNo(String cRNo) {
		this.cRNo = cRNo;
	}
	public String getcDes() {
		return cDes;
	}
	public void setcDes(String cDes) {
		this.cDes = cDes;
	}
	public String getfWeight() {
		return fWeight;
	}
	public void setfWeight(String fWeight) {
		this.fWeight = fWeight;
	}
	public String getcReceiver() {
		return cReceiver;
	}
	public void setcReceiver(String cReceiver) {
		this.cReceiver = cReceiver;
	}
	public String getcRPhone() {
		return cRPhone;
	}
	public void setcRPhone(String cRPhone) {
		this.cRPhone = cRPhone;
	}
	public String getcREMail() {
		return cREMail;
	}
	public void setcREMail(String cREMail) {
		this.cREMail = cREMail;
	}
	public String getcRPostcode() {
		return cRPostcode;
	}
	public void setcRPostcode(String cRPostcode) {
		this.cRPostcode = cRPostcode;
	}
	public String getcRCountry() {
		return cRCountry;
	}
	public void setcRCountry(String cRCountry) {
		this.cRCountry = cRCountry;
	}
	public String getcRProvince() {
		return cRProvince;
	}
	public void setcRProvince(String cRProvince) {
		this.cRProvince = cRProvince;
	}
	public String getcRCity() {
		return cRCity;
	}
	public void setcRCity(String cRCity) {
		this.cRCity = cRCity;
	}
	public String getcRAddr() {
		return cRAddr;
	}
	public void setcRAddr(String cRAddr) {
		this.cRAddr = cRAddr;
	}
		
		
		
}
