package com.cbt.bean;

import com.importExpress.pojo.ShopUrlAuthorizedInfoPO;

import java.util.Date;
import java.util.List;

public class ShopUrl {
	private Integer id;
	private String shopId;
	private String shopName;
	private String inputShopName;
	private String inputShopDescription;
	private String inputShopEnName;
	private String inputShopBrand;
	private String shopUrl;
	private String admUser;
	private int adminId;
	private Date createTime;
	private Date updatetime;
	private int remark;
	private int flag;
	private int systemEvaluation;
	private int proFlag;
	private String stateInfo;
	private int isValid;
	private int salesVolume;
	private int downloadNum;
	private String onLineNum;
	private String level;
	private String qualityAvg;
	private String serviceAvg;
	private String isValidView;
	private int onlineStatus;
	private String onlineStatusView;
	private int isAuto;

	private int urlType;

	/**
	 * 授权标识  0未授权 1已授权
	 */
	private int authorizedFlag;
	/**
	 * 外贸部门使用标识 1外贸使用
	 */
	private int isTrade;

	/**
	 * 是否店铺上线  1 是 0不是
	 */
	private int isShopFlag;

	public int getIsShopFlag() {
		return isShopFlag;
	}

	public void setIsShopFlag(int isShopFlag) {
		this.isShopFlag = isShopFlag;
	}

	/**
	 * 标记翻译1688产品描述店铺 0默认未标记 1标记
	 */
	private int isTranslateDescription;

	public int getIsTranslateDescription() {
		return isTranslateDescription;
	}

	public void setIsTranslateDescription(int isTranslateDescription) {
		this.isTranslateDescription = isTranslateDescription;
	}

	//授权文件相关
	private ShopUrlAuthorizedInfoPO authorizedInfo = new ShopUrlAuthorizedInfoPO();

	public String getInputShopEnName() {
		return inputShopEnName;
	}

	public void setInputShopEnName(String inputShopEnName) {
		this.inputShopEnName = inputShopEnName;
	}

	public String getInputShopBrand() {
		return inputShopBrand;
	}

	public void setInputShopBrand(String inputShopBrand) {
		this.inputShopBrand = inputShopBrand;
	}

	public ShopUrlAuthorizedInfoPO getAuthorizedInfo() {
		return authorizedInfo;
	}

	public void setAuthorizedInfo(ShopUrlAuthorizedInfoPO authorizedInfo) {
		this.authorizedInfo = authorizedInfo;
	}

	public int getIsTrade() {
		return isTrade;
	}

	public void setIsTrade(int isTrade) {
		this.isTrade = isTrade;
	}

	private List<ShopTypeUrl> stuList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getShopId() {
		return shopId;
	}

	public String getInputShopDescription() {
		return inputShopDescription;
	}

	public void setInputShopDescription(String inputShopDescription) {
		this.inputShopDescription = inputShopDescription;
	}
	
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopUrl() {
		return shopUrl;
	}

	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}

	public String getAdmUser() {
		return admUser;
	}

	public void setAdmUser(String admUser) {
		this.admUser = admUser;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public int getRemark() {
		return remark;
	}

	public void setRemark(int remark) {
		this.remark = remark;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getSystemEvaluation() {
		return systemEvaluation;
	}

	public void setSystemEvaluation(int systemEvaluation) {
		this.systemEvaluation = systemEvaluation;
	}

	public int getProFlag() {
		return proFlag;
	}

	public void setProFlag(int proFlag) {
		this.proFlag = proFlag;
	}

	public String getStateInfo() {
		return stateInfo;
	}

	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public int getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(int salesVolume) {
		this.salesVolume = salesVolume;
	}

	public int getDownloadNum() {
		return downloadNum;
	}

	public void setDownloadNum(int downloadNum) {
		this.downloadNum = downloadNum;
	}

	public String getOnLineNum() {
		return onLineNum;
	}

	public void setOnLineNum(String onLineNum) {
		this.onLineNum = onLineNum;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getQualityAvg() {
		return qualityAvg;
	}

	public void setQualityAvg(String qualityAvg) {
		this.qualityAvg = qualityAvg;
	}

	public String getServiceAvg() {
		return serviceAvg;
	}

	public void setServiceAvg(String serviceAvg) {
		this.serviceAvg = serviceAvg;
	}

	public int getUrlType() {
		return urlType;
	}

	public void setUrlType(int urlType) {
		this.urlType = urlType;
	}

	public List<ShopTypeUrl> getStuList() {
		return stuList;
	}

	public void setStuList(List<ShopTypeUrl> stuList) {
		this.stuList = stuList;
	}

	public String getIsValidView() {
		return isValidView;
	}

	public void setIsValidView(String isValidView) {
		this.isValidView = isValidView;
	}

	public int getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public String getOnlineStatusView() {
		return onlineStatusView;
	}

	public void setOnlineStatusView(String onlineStatusView) {
		this.onlineStatusView = onlineStatusView;
	}

	public String getInputShopName() {
		return inputShopName;
	}

	public void setInputShopName(String inputShopName) {
		this.inputShopName = inputShopName;
	}

	public int getIsAuto() {
		return isAuto;
	}

	public void setIsAuto(int isAuto) {
		this.isAuto = isAuto;
	}

	public int getAuthorizedFlag() {
		return authorizedFlag;
	}

	public void setAuthorizedFlag(int authorizedFlag) {
		this.authorizedFlag = authorizedFlag;
	}
}
