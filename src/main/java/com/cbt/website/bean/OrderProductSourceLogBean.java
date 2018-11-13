package com.cbt.website.bean;

import java.io.Serializable;
import java.util.Date;

public class OrderProductSourceLogBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;

	// order_detils表的ID
	private String odId;

	// 录入人员的id
	private String adminid;

	// 用户id
	private String userid;

	// 录入时间
	private String addtime;

	// 订单号
	private String orderid;

	// 确认采购人员id
	private String confirmUserid;

	// 确认时间
	private String confirmTime;

	// 物车的购商品ID号
	private String goodsid;

	// 户用订量
	private String usecount;

	// 替代购买数量
	private String buycount;

	// 除删标志,1表示删除
	private String del;

	private String tb1688Itemid;

	// 采购状态：0：未开始采购；1:采购货源确认 2、开始采购 3、已采购，没到货 4 、已入库 5：问题货源 6：已入库，但货有问题 8、出运中，
	// 9、已退货 10、已换货 11：已取消 12替代 13:客户同意替换
	private String purchaseState;

	private String remark;

	private String updatetime;

	private String opsId;

	// 同步到线上的标志，同步到线上后存储同步时间
	private Date curTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOdId() {
		return odId;
	}

	public void setOdId(String odId) {
		this.odId = odId;
	}

	public String getAdminid() {
		return adminid;
	}

	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getConfirmUserid() {
		return confirmUserid;
	}

	public void setConfirmUserid(String confirmUserid) {
		this.confirmUserid = confirmUserid;
	}

	public String getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(String confirmTime) {
		this.confirmTime = confirmTime;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getUsecount() {
		return usecount;
	}

	public void setUsecount(String usecount) {
		this.usecount = usecount;
	}

	public String getBuycount() {
		return buycount;
	}

	public void setBuycount(String buycount) {
		this.buycount = buycount;
	}

	public String getDel() {
		return del;
	}

	public void setDel(String del) {
		this.del = del;
	}

	public String getTb1688Itemid() {
		return tb1688Itemid;
	}

	public void setTb1688Itemid(String tb1688Itemid) {
		this.tb1688Itemid = tb1688Itemid;
	}

	public String getPurchaseState() {
		return purchaseState;
	}

	public void setPurchaseState(String purchaseState) {
		this.purchaseState = purchaseState;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getOpsId() {
		return opsId;
	}

	public void setOpsId(String opsId) {
		this.opsId = opsId;
	}

	public Date getCurTime() {
		return curTime;
	}

	public void setCurTime(Date curTime) {
		this.curTime = curTime;
	}
	
	


}