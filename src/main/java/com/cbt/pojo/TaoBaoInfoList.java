package com.cbt.pojo;

import com.cbt.Specification.bean.AliCategory;
import com.cbt.bean.OrderProductSource;

import java.util.List;

public class TaoBaoInfoList {
	private List<TaoBaoOrderInfo> infoList;
	public List<InOutDetailsInfo> inOutList;
	private List<Inventory> toryList;
	private int allCount;
	private double pagePrice;
	private List<DownFileInfo> FileList;
	private List<AliCategory> aliCategoryList;
	private List<String> barcode_list;
	private List<OrderProductSource> ops_list;
	private List<TaoBaoOrderInfo> tb_list;
	private String amount;
	private int tbAllCount;
	private List<Coupons> list1;
	List<CouponSubsidiary> list2;
	List<AliCategory> list_AliCategory;
	List<ZfuDate> list;
	private List<TaoBaoOrderInfo> taobao_list;
	private int taobao_list_size;

	private List<OrderProductSource> fpNoBuyInfoList;
	private int fpNoBuyInfoListCount;
	private List<TaoBaoOrderInfo> allBuyerOrderInfo;
	private int allBuyerOrderInfoCount;
	private List<OrderProductSource> oneMathchMoreOrderInfo;
	private int oneMathchMoreOrderInfoCount;
	public List<TaoBaoOrderInfo> getAllBuyerOrderInfo() {
		return allBuyerOrderInfo;
	}

	public void setAllBuyerOrderInfo(List<TaoBaoOrderInfo> allBuyerOrderInfo) {
		this.allBuyerOrderInfo = allBuyerOrderInfo;
	}

	public int getAllBuyerOrderInfoCount() {
		return allBuyerOrderInfoCount;
	}

	public void setAllBuyerOrderInfoCount(int allBuyerOrderInfoCount) {
		this.allBuyerOrderInfoCount = allBuyerOrderInfoCount;
	}

	public List<OrderProductSource> getOneMathchMoreOrderInfo() {
		return oneMathchMoreOrderInfo;
	}

	public void setOneMathchMoreOrderInfo(List<OrderProductSource> oneMathchMoreOrderInfo) {
		this.oneMathchMoreOrderInfo = oneMathchMoreOrderInfo;
	}

	public int getOneMathchMoreOrderInfoCount() {
		return oneMathchMoreOrderInfoCount;
	}

	public void setOneMathchMoreOrderInfoCount(int oneMathchMoreOrderInfoCount) {
		this.oneMathchMoreOrderInfoCount = oneMathchMoreOrderInfoCount;
	}

	public List<OrderProductSource> getFpNoBuyInfoList() {
		return fpNoBuyInfoList;
	}

	public void setFpNoBuyInfoList(List<OrderProductSource> fpNoBuyInfoList) {
		this.fpNoBuyInfoList = fpNoBuyInfoList;
	}

	public int getFpNoBuyInfoListCount() {
		return fpNoBuyInfoListCount;
	}

	public void setFpNoBuyInfoListCount(int fpNoBuyInfoListCount) {
		this.fpNoBuyInfoListCount = fpNoBuyInfoListCount;
	}
	public List<TaoBaoOrderInfo> getTaobao_list() {
		return taobao_list;
	}

	public void setTaobao_list(List<TaoBaoOrderInfo> taobao_list) {
		this.taobao_list = taobao_list;
	}

	public int getTaobao_list_size() {
		return taobao_list_size;
	}

	public void setTaobao_list_size(int taobao_list_size) {
		this.taobao_list_size = taobao_list_size;
	}
	public List<ZfuDate> getList() {
		return list;
	}
	public void setList(List<ZfuDate> list) {
		this.list = list;
	}
	public List<AliCategory> getList_AliCategory() {
		return list_AliCategory;
	}
	public void setList_AliCategory(List<AliCategory> list_AliCategory) {
		this.list_AliCategory = list_AliCategory;
	}
	public List<CouponSubsidiary> getList2() {
		return list2;
	}
	public void setList2(List<CouponSubsidiary> list2) {
		this.list2 = list2;
	}
	public List<Coupons> getList1() {
		return list1;
	}
	public void setList1(List<Coupons> list1) {
		this.list1 = list1;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public List<TaoBaoOrderInfo> getTb_list() {
		return tb_list;
	}
	public void setTb_list(List<TaoBaoOrderInfo> tb_list) {
		this.tb_list = tb_list;
	}
	public int getTbAllCount() {
		return tbAllCount;
	}
	public void setTbAllCount(int tbAllCount) {
		this.tbAllCount = tbAllCount;
	}
	public List<OrderProductSource> getOps_list() {
		return ops_list;
	}
	public void setOps_list(List<OrderProductSource> ops_list) {
		this.ops_list = ops_list;
	}
	public List<String> getBarcode_list() {
		return barcode_list;
	}
	public void setBarcode_list(List<String> barcode_list) {
		this.barcode_list = barcode_list;
	}
	public List<AliCategory> getAliCategoryList() {
		return aliCategoryList;
	}
	public void setAliCategoryList(List<AliCategory> aliCategoryList) {
		this.aliCategoryList = aliCategoryList;
	}
	public List<DownFileInfo> getFileList() {
		return FileList;
	}
	public void setFileList(List<DownFileInfo> fileList) {
		FileList = fileList;
	}
	public List<Inventory> getToryList() {
		return toryList;
	}
	public void setToryList(List<Inventory> toryList) {
		this.toryList = toryList;
	}
	public List<InOutDetailsInfo> getInOutList() {
		return inOutList;
	}
	public void setInOutList(List<InOutDetailsInfo> inOutList) {
		this.inOutList = inOutList;
	}
	public double getPagePrice() {
		return pagePrice;
	}
	public void setPagePrice(double pagePrice) {
		this.pagePrice = pagePrice;
	}
	public List<TaoBaoOrderInfo> getInfoList() {
		return infoList;
	}
	public void setInfoList(List<TaoBaoOrderInfo> infoList) {
		this.infoList = infoList;
	}
	public int getAllCount() {
		return allCount;
	}
	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}
}
