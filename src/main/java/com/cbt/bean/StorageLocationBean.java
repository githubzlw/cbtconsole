package com.cbt.bean;

import java.util.List;

public class StorageLocationBean {
	//库位  所需属性
	private String orderid;
	private String createtime;  //时间
	private String goodid;		
	private String picturepath; //图片路径
	private String position;	//仓库位置
	private String car_img; 	//图片
	private String user_id;     //用户id
	private String mergeOrders; //合并的订单
	private String create_time; //审核时间
	private String mergestate; //合并状态
	private String warehouse_remark;//
	private String goodstatus;
	private String len;//拆分数量
	private String shipmentno;
	private int  isDropshipOrder ;  //dropship 标记
	private int checked;//是否验货
	private String localImgPath;//本地图片路径
	private String operation;
	private String state;//订单状态
	private List<String> picList;//验货图片


	//是否1688退货
	private int is_refund;
	//商品状态
	private int od_state;
	//出运时，需要合并发货的订单号
	private String sendOrderNo;

	public String getOdid() {
		return odid;
	}

	public void setOdid(String odid) {
		this.odid = odid;
	}

	private String odid;
	public String getSendOrderNo() {
		return sendOrderNo;
	}

	public void setSendOrderNo(String sendOrderNo) {
		this.sendOrderNo = sendOrderNo;
	}

	public int getOd_state() {
		return od_state;
	}

	public void setOd_state(int od_state) {
		this.od_state = od_state;
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getLocalImgPath() {
		return localImgPath;
	}
	public void setLocalImgPath(String localImgPath) {
		this.localImgPath = localImgPath;
	}
	public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	public String getShipmentno() {
		return shipmentno;
	}
	public void setShipmentno(String shipmentno) {
		this.shipmentno = shipmentno;
	}
	public String getLen() {
		return len;
	}
	public void setLen(String len) {
		this.len = len;
	}
	public String getWarehouse_remark() {
		return warehouse_remark;
	}
	public void setWarehouse_remark(String warehouse_remark) {
		this.warehouse_remark = warehouse_remark;
	}
	public int getIs_refund() {
		return is_refund;
	}

	public void setIs_refund(int is_refund) {
		this.is_refund = is_refund;
	}
	public String getGoodstatus() {
//		//商品状态：1.到货了;2.该到没到;3.破损;4.有疑问;5.数量不够
//		if("1".equals(goodstatus)){
//			goodstatus = "已到仓库，校验无误";
//		}else if("2".equals(goodstatus)){
//			goodstatus = "已到仓库，已校验改到没有";
//		}else if("3".equals(goodstatus)){
//			goodstatus = "已到仓库，已校验破损";
//		}else if("4".equals(goodstatus)){
//			goodstatus = "已到仓库，已校验有疑问";
//		}else if("5".equals(goodstatus)){
//			goodstatus = "已到仓库，已校验数量有误";
//		}   
		return goodstatus;
	}
	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}
	public void setGoodstatus(String goodstatus) {
		this.goodstatus = goodstatus;
	}
	public String getMergestate() {
		return mergestate;
	}
	public void setMergestate(String mergestate) {
		this.mergestate = mergestate;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getMergeOrders() {
		return mergeOrders;
	}
	public void setMergeOrders(String mergeOrders) {
		this.mergeOrders = mergeOrders;
	}
	//出库验货
	private String order_no;  //订单id
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCar_img() {
		return car_img;
	}
	public void setCar_img(String car_img) {
		this.car_img = car_img;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getGoodid() {
		return goodid;
	}
	public void setGoodid(String goodid) {
		this.goodid = goodid;
	}
	public String getPicturepath() {
		return picturepath;
	}
	public void setPicturepath(String picturepath) {
		this.picturepath = picturepath;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getOrder_no() {
		return order_no;
	}
	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}
	public int getIsDropshipOrder() {
		return isDropshipOrder;
	}
	public void setIsDropshipOrder(int isDropshipOrder) {
		this.isDropshipOrder = isDropshipOrder;
	}
	 
	
	
	
}
