package com.cbt.pojo;

public class TbGoodsSampleDetails {
    private Integer id;

    private Integer goodssampleid;

    private Integer goodsid;

    private String goodsname;

    private String goodsurl;

    private String goodsimg;

    private Double goodsprice;

    private Integer amazongoosid;

    private Double amazongoosprice;

    private String cid;

    private String cidpath;

    private String category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodssampleid() {
        return goodssampleid;
    }

    public void setGoodssampleid(Integer goodssampleid) {
        this.goodssampleid = goodssampleid;
    }

    public Integer getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(Integer goodsid) {
        this.goodsid = goodsid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname == null ? null : goodsname.trim();
    }

    public String getGoodsurl() {
        return goodsurl;
    }

    public void setGoodsurl(String goodsurl) {
        this.goodsurl = goodsurl == null ? null : goodsurl.trim();
    }

    public String getGoodsimg() {
        return goodsimg;
    }

    public void setGoodsimg(String goodsimg) {
        this.goodsimg = goodsimg == null ? null : goodsimg.trim();
    }

    public Double getGoodsprice() {
		return goodsprice;
	}

	public void setGoodsprice(Double goodsprice) {
		this.goodsprice = goodsprice;
	}

	public Double getAmazongoosprice() {
		return amazongoosprice;
	}

	public void setAmazongoosprice(Double amazongoosprice) {
		this.amazongoosprice = amazongoosprice;
	}

	public Integer getAmazongoosid() {
        return amazongoosid;
    }

    public void setAmazongoosid(Integer amazongoosid) {
        this.amazongoosid = amazongoosid;
    }

  

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid == null ? null : cid.trim();
    }

    public String getCidpath() {
        return cidpath;
    }

    public void setCidpath(String cidpath) {
        this.cidpath = cidpath == null ? null : cidpath.trim();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }
}