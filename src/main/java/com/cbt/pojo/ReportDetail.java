package com.cbt.pojo;

public class ReportDetail {
    private Integer id;

    private String categroy;

    private Double purchasePrice;

    private Double salesPrice;
    
    private Double usePayPrice;

    private Double averagePrice;

    private Integer salesVolumes;

    private Integer genralReportId;

    private Integer buycount;

    private Double buyAveragePrice;

    private Double profitLoss;
    
    private Double freight;
    
    private Double packageFee;
    
    private String currency;
    
    private int datacount;
    private Double od_total_weight; //order_detail表中的 估算重量
    private Double of_weight; //order_fee表中的 计费重量
    private Double settle_weight; //forwarder表中的settleWeight实收重量
    
   
	public Double getOd_total_weight() {
		return od_total_weight;
	}
	public void setOd_total_weight(Double od_total_weight) {
		this.od_total_weight = od_total_weight;
	}
	public Double getOf_weight() {
		return of_weight;
	}
	public void setOf_weight(Double of_weight) {
		this.of_weight = of_weight;
	}
	public Double getSettle_weight() {
		return settle_weight;
	}
	public void setSettle_weight(Double settle_weight) {
		this.settle_weight = settle_weight;
	}
	public int getDatacount() {
		return datacount;
	}
	public void setDatacount(int datacount) {
		this.datacount = datacount;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategroy() {
        return categroy;
    }

    public void setCategroy(String categroy) {
        this.categroy = categroy == null ? null : categroy.trim();
    }

    public Double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Double getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(Double salesPrice) {
        this.salesPrice = salesPrice;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Integer getSalesVolumes() {
        return salesVolumes;
    }

    public void setSalesVolumes(Integer salesVolumes) {
        this.salesVolumes = salesVolumes;
    }

    public Integer getGenralReportId() {
        return genralReportId;
    }

    public void setGenralReportId(Integer genralReportId) {
        this.genralReportId = genralReportId;
    }

    public Integer getBuycount() {
        return buycount;
    }

    public void setBuycount(Integer buycount) {
        this.buycount = buycount;
    }

    public Double getBuyAveragePrice() {
        return buyAveragePrice;
    }

    public void setBuyAveragePrice(Double buyAveragePrice) {
        this.buyAveragePrice = buyAveragePrice;
    }

    public Double getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(Double profitLoss) {
        this.profitLoss = profitLoss;
    }

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
	}

	public Double getPackageFee() {
		return packageFee;
	}

	public void setPackageFee(Double packageFee) {
		this.packageFee = packageFee;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Double getUsePayPrice() {
		return usePayPrice;
	}

	public void setUsePayPrice(Double usePayPrice) {
		this.usePayPrice = usePayPrice;
	}
	
}