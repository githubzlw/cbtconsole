package com.cbt.jcys.bean;

public class GoodsPojo {
	private String cxGoods;  //"衣服",  物品描述,0-63字符。必须。
	private String ixQuantity;  //1,    物品数量。必须。
	private String fxPrice;  //26.00,   物品单价，2位小数。
	private String cxGoodsA;  //"clothes",  物品英文描述,0-63字符。
	private String cxGoodsB;  //"SKU012345679" 物品乙码，通常为统一编码,0-63字符。

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

	private String cGoods;
	
	public String getCxGoods() {
		return cxGoods;
	}
	public void setCxGoods(String cxGoods) {
		this.cxGoods = cxGoods;
	}
	public String getIxQuantity() {
		return ixQuantity;
	}
	public void setIxQuantity(String ixQuantity) {
		this.ixQuantity = ixQuantity;
	}
	public String getFxPrice() {
		return fxPrice;
	}
	public void setFxPrice(String fxPrice) {
		this.fxPrice = fxPrice;
	}
	public String getCxGoodsA() {
		return cxGoodsA;
	}
	public void setCxGoodsA(String cxGoodsA) {
		this.cxGoodsA = cxGoodsA;
	}
	public String getCxGoodsB() {
		return cxGoodsB;
	}
	public void setCxGoodsB(String cxGoodsB) {
		this.cxGoodsB = cxGoodsB;
	}
	
	
	
}
