package ceRong.tools.bean;

public class SearchLog {
	private  int  id ; 
	private  String  keyWords; 
	private  String  catid ; 
	private  String  sortType ;
	private  String   pageNumber ; 
	private  String  productShowIdList;
	private  String  allProductList;
	private  int listSize;
	private  int device;
	private  String  saveFlag;
	private  int  year;
	private  String  day;
	private  String  month;
	private  String searchMD5;

	private String sessionid;
	private String searchUserMD5;
	private int userid;
	private String userIP;
	
	
	public int getDevice() {
		return device;
	}
	public void setDevice(int device) {
		this.device = device;
	}
	public int getListSize() {
		return listSize;
	}
	public void setListSize(int listSize) {
		this.listSize = listSize;
	}
	public String getAllProductList() {
		return allProductList;
	}
	public void setAllProductList(String allProductList) {
		this.allProductList = allProductList;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public String getSearchUserMD5() {
		return searchUserMD5;
	}
	public void setSearchUserMD5(String searchUserMD5) {
		this.searchUserMD5 = searchUserMD5;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	public String getSearchMD5() {
		return searchMD5;
	}
	public void setSearchMD5(String searchMD5) {
		this.searchMD5 = searchMD5;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getSaveFlag() {
		return saveFlag;
	}
	public void setSaveFlag(String saveFlag) {
		this.saveFlag = saveFlag;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getProductShowIdList() {
		return productShowIdList;
	}
	public void setProductShowIdList(String productShowIdList) {
		this.productShowIdList = productShowIdList;
	} 
}
