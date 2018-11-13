package com.cbt.warehouse.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class Mabangshipment {
    private Long id;

    private String orderno;

    private Date paymenttime;

    private String shipmethod;

    private String shipno;

    private String storename;

    private String clientacount;

    private String clientname;

    private String clienttelphone;

    private String country;

    private BigDecimal productunitprice;

    private BigDecimal producttotalamount;

    private BigDecimal producttotalcost;

    private BigDecimal freightrevenue;

    private BigDecimal shipmentexpenses;

    private BigDecimal weight;

    private String sku;

    private String productname;

    private Integer productnumbers;

    private String productdirectory;

    private String currency;

    private BigDecimal ordertotalamout;

    private Date createtime;
    
    private String url;
    private String item_id;
    private Integer state;
    private BigDecimal unitCost; //采购价格
    private String start_time;
    private String end_time;
    private String ym; //日期段
    private Double cost; //成本
    private Integer page;
    private Integer count;

    public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getYm() {
		return ym;
	}
	public void setYm(String ym) {
		this.ym = ym;
	}
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno == null ? null : orderno.trim();
    }

    public Date getPaymenttime() {
        return paymenttime;
    }

    public void setPaymenttime(Date paymenttime) {
        this.paymenttime = paymenttime;
    }

    public String getShipmethod() {
        return shipmethod;
    }

    public void setShipmethod(String shipmethod) {
        this.shipmethod = shipmethod == null ? null : shipmethod.trim();
    }

    public String getShipno() {
        return shipno;
    }

    public void setShipno(String shipno) {
        this.shipno = shipno == null ? null : shipno.trim();
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename == null ? null : storename.trim();
    }

    public String getClientacount() {
        return clientacount;
    }

    public void setClientacount(String clientacount) {
        this.clientacount = clientacount == null ? null : clientacount.trim();
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname == null ? null : clientname.trim();
    }

    public String getClienttelphone() {
        return clienttelphone;
    }

    public void setClienttelphone(String clienttelphone) {
        this.clienttelphone = clienttelphone == null ? null : clienttelphone.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public BigDecimal getProductunitprice() {
        return productunitprice;
    }

    public void setProductunitprice(BigDecimal productunitprice) {
        this.productunitprice = productunitprice;
    }

    public BigDecimal getProducttotalamount() {
        return producttotalamount;
    }

    public void setProducttotalamount(BigDecimal producttotalamount) {
        this.producttotalamount = producttotalamount;
    }

    public BigDecimal getProducttotalcost() {
        return producttotalcost;
    }

    public void setProducttotalcost(BigDecimal producttotalcost) {
        this.producttotalcost = producttotalcost;
    }

    public BigDecimal getFreightrevenue() {
        return freightrevenue;
    }

    public void setFreightrevenue(BigDecimal freightrevenue) {
        this.freightrevenue = freightrevenue;
    }

    public BigDecimal getShipmentexpenses() {
        return shipmentexpenses;
    }

    public void setShipmentexpenses(BigDecimal shipmentexpenses) {
        this.shipmentexpenses = shipmentexpenses;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku == null ? null : sku.trim();
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    public Integer getProductnumbers() {
        return productnumbers;
    }

    public void setProductnumbers(Integer productnumbers) {
        this.productnumbers = productnumbers;
    }

    public String getProductdirectory() {
        return productdirectory;
    }

    public void setProductdirectory(String productdirectory) {
        this.productdirectory = productdirectory == null ? null : productdirectory.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public BigDecimal getOrdertotalamout() {
        return ordertotalamout;
    }

    public void setOrdertotalamout(BigDecimal ordertotalamout) {
        this.ordertotalamout = ordertotalamout;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	public BigDecimal getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(BigDecimal unitCost) {
		this.unitCost = unitCost;
	}

    
}