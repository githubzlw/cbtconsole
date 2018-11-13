package com.cbt.warehouse.pojo;

public class TbGoodsSamplePojo {
	private String id;	//						
	private String cid;		//		样品类型id（ali_category.id）					
	private String category;	//	样品类别					
	private String title;	//		标题					
	private String viewimg;	//		样品图片					
	private String discount;	//	整体折扣（%）					
	private String discountprice;//	商品价格总和					
	private String minnum;	//		最低数量					
	private String defaultnum;	//	初始数量					
	private String isdelete;	//	删除标志(0:未删除。1：已删除)					
	private String remark;	//		备注					
	private String createuser;	//	创建者					
	private String createtime;	//	创建时间					
	private String updateuser;	//	更新者					
	private String updatetime;	//	更新时间					
	private String expirationdate;//自动失效时间	
	private String ymx_discount;

	
	
	public String getYmx_discount() {
		return ymx_discount;
	}

	public void setYmx_discount(String ymx_discount) {
		this.ymx_discount = ymx_discount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getViewimg() {
		return viewimg;
	}

	public void setViewimg(String viewimg) {
		this.viewimg = viewimg;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getDiscountprice() {
		return discountprice;
	}

	public void setDiscountprice(String discountprice) {
		this.discountprice = discountprice;
	}

	public String getMinnum() {
		return minnum;
	}

	public void setMinnum(String minnum) {
		this.minnum = minnum;
	}

	public String getDefaultnum() {
		return defaultnum;
	}

	public void setDefaultnum(String defaultnum) {
		this.defaultnum = defaultnum;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdateuser() {
		return updateuser;
	}

	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getExpirationdate() {
		return expirationdate;
	}

	public void setExpirationdate(String expirationdate) {
		this.expirationdate = expirationdate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}
