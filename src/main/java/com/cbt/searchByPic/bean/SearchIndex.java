package com.cbt.searchByPic.bean;

import java.util.Date;

public class SearchIndex {
    private Integer id;//id

    private String enName;//产品名称（英文）

    private String cnName;//产品名称（中文）

    private String enNameOne;//别名1（英文）

    private String cnNameOne;//别名1（中文）

    private String enNameTwo;//别名2（英文）

    private String cnNameTwo;//别名2（中文）

    private String enNameThree;//别名3（英文）

    private String cnNameThree;//别名3（中文）

    private String detailCatid;//1688类别id
    
    private String aliexpressCatid;//aliexpress类别id

    private String translationCatid;//翻译类别id

    private String keywords;//屏蔽关键词

    private Date translationTime;//翻译时间

    private Date createTime;//数据写入表时间
    
    private Integer count;//总数
    
    private Integer flag;//状态 （0-未下载，1-下载完成，2-更新中）
    
    private String minPrice;
    private String maxPrice;
    
    
    public String getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(String minPrice) {
		this.minPrice = minPrice;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getAliexpressCatid() {
		return aliexpressCatid;
	}

	public void setAliexpressCatid(String aliexpressCatid) {
		this.aliexpressCatid = aliexpressCatid;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName == null ? null : enName.trim();
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName == null ? null : cnName.trim();
    }

    public String getEnNameOne() {
        return enNameOne;
    }

    public void setEnNameOne(String enNameOne) {
        this.enNameOne = enNameOne == null ? null : enNameOne.trim();
    }

    public String getCnNameOne() {
        return cnNameOne;
    }

    public void setCnNameOne(String cnNameOne) {
        this.cnNameOne = cnNameOne == null ? null : cnNameOne.trim();
    }

    public String getEnNameTwo() {
        return enNameTwo;
    }

    public void setEnNameTwo(String enNameTwo) {
        this.enNameTwo = enNameTwo == null ? null : enNameTwo.trim();
    }

    public String getCnNameTwo() {
        return cnNameTwo;
    }

    public void setCnNameTwo(String cnNameTwo) {
        this.cnNameTwo = cnNameTwo == null ? null : cnNameTwo.trim();
    }

    public String getEnNameThree() {
        return enNameThree;
    }

    public void setEnNameThree(String enNameThree) {
        this.enNameThree = enNameThree == null ? null : enNameThree.trim();
    }

    public String getCnNameThree() {
        return cnNameThree;
    }

    public void setCnNameThree(String cnNameThree) {
        this.cnNameThree = cnNameThree == null ? null : cnNameThree.trim();
    }

    public String getDetailCatid() {
        return detailCatid;
    }

    public void setDetailCatid(String detailCatid) {
        this.detailCatid = detailCatid == null ? null : detailCatid.trim();
    }

    public String getTranslationCatid() {
        return translationCatid;
    }

    public void setTranslationCatid(String translationCatid) {
        this.translationCatid = translationCatid == null ? null : translationCatid.trim();
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords == null ? null : keywords.trim();
    }

    public Date getTranslationTime() {
        return translationTime;
    }

    public void setTranslationTime(Date translationTime) {
        this.translationTime = translationTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}