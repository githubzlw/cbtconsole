package com.cbt.warehouse.pojo;

import java.io.Serializable;
import java.util.Date;

public class returndisplay implements Serializable{
private int id;
private String customerorder;//获取客户订单
private String a1688Order;//获取1688订单号
private String a1688Shipno;//获取1688运单号
private String item;//获取产品编号
private int itemNumber;//获取产品数量
private String applyUser;//获取申请人员
private String applyTime;//获取申请时间
private String optUser;//获取实际退单人员
private String optTime;//获取实际退单时间
private String shipno;//获取退货运单号
private String changeShipno;//获取换货运单号
private int State;//获取退货状态
private int returnNumber;//获取退货数量
private String endTime;//获取完结时间
private String returnReason;//退货原因
private String barcode;//获取所在仓库
private String placeDate;//获取下单时间
private String signtime;//获取签收时间
private String orderInfo;//操作订单详情
private String sellerpeo;//获取卖家
private String pepoInfo;//获取申请人和申请数量
private String stateShow;//获取状态展示
private String tbId;//獲取淘寶id
private String sku;
private String itemname;//获取产品名称
private String imgurl;//获取产品图片链接
private String returntime;//同意退货时间
private String reason;//驳回退货理由
private double actual_money;//采购输入价格
private String refund_time;//退款时间
private double refund_money;//1688抓取退货金额
private String differences;//金额差异原因
private Double freight;//公司承担运费

	@Override
	public String toString() {
		return "returndisplay{" +
				"id=" + id +
				", customerorder='" + customerorder + '\'' +
				", a1688Order='" + a1688Order + '\'' +
				", a1688Shipno='" + a1688Shipno + '\'' +
				", item='" + item + '\'' +
				", itemNumber=" + itemNumber +
				", applyUser='" + applyUser + '\'' +
				", applyTime='" + applyTime + '\'' +
				", optUser='" + optUser + '\'' +
				", optTime='" + optTime + '\'' +
				", shipno='" + shipno + '\'' +
				", changeShipno='" + changeShipno + '\'' +
				", State=" + State +
				", returnNumber=" + returnNumber +
				", endTime='" + endTime + '\'' +
				", returnReason='" + returnReason + '\'' +
				", barcode='" + barcode + '\'' +
				", placeDate='" + placeDate + '\'' +
				", signtime='" + signtime + '\'' +
				", orderInfo='" + orderInfo + '\'' +
				", sellerpeo='" + sellerpeo + '\'' +
				", pepoInfo='" + pepoInfo + '\'' +
				", stateShow='" + stateShow + '\'' +
				", tbId='" + tbId + '\'' +
				", sku='" + sku + '\'' +
				", itemname='" + itemname + '\'' +
				", imgurl='" + imgurl + '\'' +
				", returntime='" + returntime + '\'' +
				", reason='" + reason + '\'' +
				", actual_money=" + actual_money +
				", refund_time='" + refund_time + '\'' +
				", refund_money=" + refund_money +
				", differences='" + differences + '\'' +
				", freight=" + freight +
				'}';
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public double getActual_money() {
		return actual_money;
	}

	public void setActual_money(double actual_money) {
		this.actual_money = actual_money;
	}

	public double getRefund_money() {
		return refund_money;
	}

	public void setRefund_money(double refund_money) {
		this.refund_money = refund_money;
	}

	public String getDifferences() {
		return differences;
	}

	public void setDifferences(String differences) {
		this.differences = differences;
	}



	public String getRefund_time() {
		return refund_time;
	}

	public void setRefund_time(String refund_time) {
		this.refund_time = refund_time;
	}


	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getReturntime() {
		return returntime;
	}

	public void setReturntime(String returntime) {
		this.returntime = returntime;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getSku() {
	return sku;
}
public void setSku(String sku) {
	this.sku = sku;
}
public String getTbId() {
	return tbId;
}
public void setTbId(String tbId) {
	this.tbId = tbId;
}

	public String getStateShow() {
	return stateShow;
}
public void setStateShow(String stateShow) {
	this.stateShow = stateShow;
}
public String getPepoInfo() {
	return pepoInfo;
}
public void setPepoInfo(String pepoInfo) {
	this.pepoInfo = pepoInfo;
}
public String getSellerpeo() {
	return sellerpeo;
}
public void setSellerpeo(String sellerpeo) {
	this.sellerpeo = sellerpeo;
}
public int getState() {
	return State;
}
public void setState(int state) {
	State = state;
}
public String getOrderInfo() {
	return orderInfo;
}
public void setOrderInfo(String orderInfo) {
	this.orderInfo = orderInfo;
}
public String getPlaceDate() {
	return placeDate;
}
public void setPlaceDate(String placeDate) {
	this.placeDate = placeDate;
}
public String getSigntime() {
	return signtime;
}
public void setSigntime(String signtime) {
	this.signtime = signtime;
}
public int getReturnNumber() {
	return returnNumber;
}
public void setReturnNumber(int returnNumber) {
	this.returnNumber = returnNumber;
}
public String getBarcode() {
	return barcode;
}
public void setBarcode(String barcode) {
	this.barcode = barcode;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getCustomerorder() {
	return customerorder;
}
public void setCustomerorder(String customerorder) {
	this.customerorder = customerorder;
}
public String getA1688Order() {
	return a1688Order;
}
public void setA1688Order(String a1688Order) {
	this.a1688Order = a1688Order;
}
public String getA1688Shipno() {
	return a1688Shipno;
}
public void setA1688Shipno(String a1688Shipno) {
	this.a1688Shipno = a1688Shipno;
}
public String getItem() {
	return item;
}
public void setItem(String item) {
	this.item = item;
}
public int getItemNumber() {
	return itemNumber;
}
public void setItemNumber(int itemNumber) {
	this.itemNumber = itemNumber;
}
public String getApplyUser() {
	return applyUser;
}
public void setApplyUser(String applyUser) {
	this.applyUser = applyUser;
}
public String getApplyTime() {
	return applyTime;
}
public void setApplyTime(String applyTime) {
	this.applyTime = applyTime;
}
public String getOptUser() {
	return optUser;
}
public void setOptUser(String optUser) {
	this.optUser = optUser;
}
public String getOptTime() {
	return optTime;
}
public void setOptTime(String optTime) {
	this.optTime = optTime;
}
public String getShipno() {
	return shipno;
}
public void setShipno(String shipno) {
	this.shipno = shipno;
}
public String getChangeShipno() {
	return changeShipno;
}
public void setChangeShipno(String changeShipno) {
	this.changeShipno = changeShipno;
}

public String getEndTime() {
	return endTime;
}
public void setEndTime(String endTime) {
	this.endTime = endTime;
}
public String getReturnReason() {
	return returnReason;
}
public void setReturnReason(String returnReason) {
	this.returnReason = returnReason;
}

}
