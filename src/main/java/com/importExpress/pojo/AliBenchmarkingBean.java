package com.importExpress.pojo;

import org.apache.commons.lang3.StringUtils;

/**
 * ali对标1688数据bean
 */
public class AliBenchmarkingBean {
    private int id;
    private String pid;
    private String url;
    private String aliPid;
    private String aliUrl;
    private int adminId;
    private String adminName;
    private int isOnline = -1;
    private int isEdited = -1;
    private String createTime;
    private int startNum;
    private int limitNum;
    private String beginDate;
    private String endDate;
    private String keyword;
    
    private String image; //1688产品图片
    private String aliImage; //速卖通产品图片
    private String category;//类别
    private String wprice;
    private String feePrice;
    private int isSoldFlag;
    private int isBenchmark;
    
    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAliImage() {
		return aliImage;
	}

	public void setAliImage(String aliImage) {
		this.aliImage = aliImage;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAliPid() {
        return aliPid;
    }

    public void setAliPid(String aliPid) {
        this.aliPid = aliPid;
    }

    public String getAliUrl() {
        return aliUrl;
    }

    public void setAliUrl(String aliUrl) {
        this.aliUrl = aliUrl;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getIsEdited() {
        return isEdited;
    }

    public void setIsEdited(int isEdited) {
        this.isEdited = isEdited;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getStartNum() {
        return startNum;
    }

    public void setStartNum(int startNum) {
        this.startNum = startNum;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getWprice() {
        return wprice;
    }

    public void setWprice(String wprice) {
        if(StringUtils.isNotBlank(wprice)){
            if(wprice.length() > 4){
                String tempStr = wprice.replaceAll("[\\[\\]]","").replaceAll("\\$","@");
                this.wprice = tempStr;
            }else{
                this.wprice = wprice;
            }
        }
    }

    public String getFeePrice() {
        return feePrice;
    }

    public void setFeePrice(String feePrice) {

        if(StringUtils.isNotBlank(feePrice)){
            if(feePrice.length() > 4){
                String tempStr = feePrice.replaceAll("[\\[\\]]","").replaceAll("\\$","@");
                this.feePrice = tempStr;
            }else{
                this.feePrice = feePrice;
            }
        }
    }

    public int getIsSoldFlag() {
        return isSoldFlag;
    }

    public void setIsSoldFlag(int isSoldFlag) {
        this.isSoldFlag = isSoldFlag;
    }

    public int getIsBenchmark() {
        return isBenchmark;
    }

    public void setIsBenchmark(int isBenchmark) {
        this.isBenchmark = isBenchmark;
    }

    @Override
    public String toString() {
        return "AliBenchmarkingBean{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", url='" + url + '\'' +
                ", aliPid='" + aliPid + '\'' +
                ", aliUrl='" + aliUrl + '\'' +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", isOnline=" + isOnline +
                ", isEdited=" + isEdited +
                ", createTime='" + createTime + '\'' +
                ", startNum=" + startNum +
                ", limitNum=" + limitNum +
                ", beginDate='" + beginDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", keyword='" + keyword + '\'' +
                '}';
    }
}
