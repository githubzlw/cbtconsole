package com.importExpress.pojo;

import java.util.Date;

public class TabSeachPageBean {
	private Integer id;
	/**
	 * 关键词
	 */
	private String keyword;
	/**
	 * 近似关键词1
	 */
	private String keyword1;
	private String keyword2;
	private String keyword3;
	private String keyword4;
	
	private String banner;
	
	private String filename;
	
	private Date createtime;
	
	private int catId;//关键词对应的分类id
	private int parentId;

	private String pageTitle; //页面标题
	private String pageKeywords; //页面keywords
	private String pageDescription; //页面description
	private String pageBannerName; //底部banner图片 本地
	private String pageBannerUrl; //底部banner图片 图片服务器

	/**
	 * 按照网站分类
	 */
	private int webSite;

	public int getWebSite() {
		return webSite;
	}

	public void setWebSite(int webSite) {
		this.webSite = webSite;
	}

	public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageKeywords() {
        return pageKeywords;
    }

    public void setPageKeywords(String pageKeywords) {
        this.pageKeywords = pageKeywords;
    }

    public String getPageDescription() {
        return pageDescription;
    }

    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription;
    }

    public String getPageBannerName() {
        return pageBannerName;
    }

    public void setPageBannerName(String pageBannerName) {
        this.pageBannerName = pageBannerName;
    }

    public String getPageBannerUrl() {
        return pageBannerUrl;
    }

    public void setPageBannerUrl(String pageBannerUrl) {
        this.pageBannerUrl = pageBannerUrl;
    }

    public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	/**
	 * 0.未启用 1.启用
	 */
	private Integer isshow;
	/**
	 * 状态：0 正常  1 删除
	 */
	private Integer sate;
	
	public String getKeyword2() {
		return keyword2;
	}
	public void setKeyword2(String keyword2) {
		this.keyword2 = keyword2;
	}
	public String getKeyword3() {
		return keyword3;
	}
	public void setKeyword3(String keyword3) {
		this.keyword3 = keyword3;
	}
	public String getKeyword4() {
		return keyword4;
	}
	public void setKeyword4(String keyword4) {
		this.keyword4 = keyword4;
	}
	private String importPath;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getKeyword1() {
		return keyword1;
	}
	public void setKeyword1(String keyword1) {
		this.keyword1 = keyword1;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public Integer getSate() {
		return sate;
	}
	public void setSate(Integer sate) {
		this.sate = sate;
	}
	public String getImportPath() {
		return importPath;
	}
	public void setImportPath(String importPath) {
		this.importPath = importPath;
	}
	public String getBanner() {
		return banner;
	}
	public void setBanner(String banner) {
		this.banner = banner;
	}
	public Integer getIsshow() {
		return isshow;
	}
	public void setIsshow(Integer isshow) {
		this.isshow = isshow;
	}
	public int getCatId() {
		return catId;
	}
	public void setCatId(int catId) {
		this.catId = catId;
	}
	
}
