package com.cbt.pojo;

import lombok.Data;

/**
 * 库存表实体类
 * 
 * @author whj
 *
 */
@Data
public class Inventory {
	private int id;

	private String goods_url;// 商品链接

	private int add_goods;// 入库数量

	private int out_goods;// 出库数量

	private String remaining;// 库存数量

	private String last_add_time;// 最后入库时间

	private String last_out_time;// 最后出库时间

	private String itemid;// 商品编号

	private String sku;// 商品规格

	private String good_name;// 商品名称

	private String barcode;// 存放库位

	private int buyCount;// 采购数量

	private int noInCount;// 未入库数量

	private String last_buy_time;// 最后采购时间

	private String goods_p_url;// 货源链接
	
	private String goodscatid;//商品类别
	
	private double inventory_amount;//库存金额
	
	private String car_img;//商品图片
	
	private String userName;//入库人
	
	private String goods_p_price;//采购价
	
	private String goodsprice;//销售价
	
	private int flag;
	
	private double new_inventory_amount;
	
	private String new_barcode;
	
	private String new_remaining;
	
	private String oldUrl;
	
	private String operation;//操作
	
	private String goods_pid;
	
	private String car_urlMD5;
	
	private String updatetime;
	
	private String createtime; 
	private String remark;
	private String can_remaining;
	
	private String admName;
	private String create_time;
	private String old_inventory;//盘点前库存数量
	private String new_inventroy;//盘点后库存数量
	private String old_barcode;//盘点前库位
	private String goodsid;
	private String onLine;//是否上架
	private String unsellableReason;//下架原因
	private String pid;
	private String online_flag;
	private String db_flag;
	private String editLink;
	private String name;
	private int type;
	private String check_time;
	/**
	 * 规格skuid
	 * 2019-08-01-sj add
	 */
	private String skuid;
	/**
	 * 规格specid
	 *  2019-08-01-sj add
	 */
	private String specid;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}



	public String getDelRemark() {
		return delRemark;
	}

	public void setDelRemark(String delRemark) {
		this.delRemark = delRemark;
	}

	private String delRemark;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEditLink() {
		return editLink;
	}


	public void setEditLink(String editLink) {
		this.editLink = editLink;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getOnline_flag() {
		return online_flag;
	}

	public void setOnline_flag(String online_flag) {
		this.online_flag = online_flag;
	}

	public String getDb_flag() {
		return db_flag;
	}

	public void setDb_flag(String db_flag) {
		this.db_flag = db_flag;
	}

	public String getUnsellableReason() {
		return unsellableReason;
	}

	public void setUnsellableReason(String unsellableReason) {
		this.unsellableReason = unsellableReason;
	}

	public String getOnLine() {
		return onLine;
	}

	public void setOnLine(String onLine) {
		this.onLine = onLine;
	}

	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getNew_inventroy() {
		return new_inventroy;
	}

	public void setNew_inventroy(String new_inventroy) {
		this.new_inventroy = new_inventroy;
	}
	public String getOld_inventory() {
		return old_inventory;
	}

	public void setOld_inventory(String old_inventory) {
		this.old_inventory = old_inventory;
	}


	public String getOld_barcode() {
		return old_barcode;
	}

	public void setOld_barcode(String old_barcode) {
		this.old_barcode = old_barcode;
	}
	public String getAdmName() {
		return admName;
	}

	public void setAdmName(String admName) {
		this.admName = admName;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getCan_remaining() {
		return can_remaining;
	}

	public void setCan_remaining(String can_remaining) {
		this.can_remaining = can_remaining;
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

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	
	public String getCar_urlMD5() {
		return car_urlMD5;
	}

	public void setCar_urlMD5(String car_urlMD5) {
		this.car_urlMD5 = car_urlMD5;
	}

	public String getGoods_pid() {
		return goods_pid;
	}

	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOldUrl() {
		return oldUrl;
	}

	public void setOldUrl(String oldUrl) {
		this.oldUrl = oldUrl;
	}

	public String getNew_remaining() {
		return new_remaining;
	}

	public void setNew_remaining(String new_remaining) {
		this.new_remaining = new_remaining;
	}

	public String getNew_barcode() {
		return new_barcode;
	}

	public void setNew_barcode(String new_barcode) {
		this.new_barcode = new_barcode;
	}

	public double getNew_inventory_amount() {
		return new_inventory_amount;
	}

	public void setNew_inventory_amount(double new_inventory_amount) {
		this.new_inventory_amount = new_inventory_amount;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getGoodsprice() {
		return goodsprice;
	}

	public void setGoodsprice(String goodsprice) {
		this.goodsprice = goodsprice;
	}

	public String getGoodscatid() {
		return goodscatid;
	}

	public void setGoodscatid(String goodscatid) {
		this.goodscatid = goodscatid;
	}

	public double getInventory_amount() {
		return inventory_amount;
	}

	public void setInventory_amount(double inventory_amount) {
		this.inventory_amount = inventory_amount;
	}

	public String getCar_img() {
		return car_img;
	}

	public void setCar_img(String car_img) {
		this.car_img = car_img;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getGoods_p_price() {
		return goods_p_price;
	}

	public void setGoods_p_price(String goods_p_price) {
		this.goods_p_price = goods_p_price;
	}


	public String getGoods_p_url() {
		return goods_p_url;
	}

	public void setGoods_p_url(String goods_p_url) {
		this.goods_p_url = goods_p_url;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public int getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(int buyCount) {
		this.buyCount = buyCount;
	}

	public int getNoInCount() {
		return noInCount;
	}

	public void setNoInCount(int noInCount) {
		this.noInCount = noInCount;
	}

	public String getLast_buy_time() {
		return last_buy_time;
	}

	public void setLast_buy_time(String last_buy_time) {
		this.last_buy_time = last_buy_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGoods_url() {
		return goods_url;
	}

	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}

	public int getAdd_goods() {
		return add_goods;
	}

	public void setAdd_goods(int add_goods) {
		this.add_goods = add_goods;
	}

	public int getOut_goods() {
		return out_goods;
	}

	public void setOut_goods(int out_goods) {
		this.out_goods = out_goods;
	}

	public String getRemaining() {
		return remaining;
	}

	public void setRemaining(String remaining) {
		this.remaining = remaining;
	}

	public String getLast_add_time() {
		return last_add_time;
	}

	public void setLast_add_time(String last_add_time) {
		this.last_add_time = last_add_time;
	}

	public String getLast_out_time() {
		return last_out_time;
	}

	public void setLast_out_time(String last_out_time) {
		this.last_out_time = last_out_time;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getGood_name() {
		return good_name;
	}

	public void setGood_name(String good_name) {
		this.good_name = good_name;
	}

}
