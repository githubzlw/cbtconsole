package com.importExpress.utli;

/**
 * @author luohao
 * @date 2018/6/25
 */
public class UpdateTblModel {

	private String type = "1";

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	private String pk; //pid

	private String valid;
	private String goodsstate;
	private String unsellableReason;
	public String getPk() {
		return pk;
	}
	public void setPk(String pk) {
		this.pk = pk;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getGoodsstate() {
		return goodsstate;
	}
	public void setGoodsstate(String goodsstate) {
		this.goodsstate = goodsstate;
	}
	public String getUnsellableReason() {
		return unsellableReason;
	}
	public void setUnsellableReason(String unsellableReason) {
		this.unsellableReason = unsellableReason;
	}





}
