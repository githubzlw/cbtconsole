package com.cbt.pojo;

public class ReportInfo {
    private Integer id;

    private Integer breportId;

    private Double totalExpenditure;

    private Double totalRevenue;

    private Integer goodsCount;

    private Double averagePrice;

    private Integer invalidPurchaseNum;

    private Double profitLoss;

    private Integer genralReportId;

    private Integer categroyNum;

    private Integer orderNum;
    
    private Integer buyCount;
    
    private Double freight;
    
    private Double packageFee;
    
    private int redundantCount;

    public int getRedundantCount() {
		return redundantCount;
	}

	public void setRedundantCount(int redundantCount) {
		this.redundantCount = redundantCount;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBreportId() {
        return breportId;
    }

    public void setBreportId(Integer breportId) {
        this.breportId = breportId;
    }

    public Double getTotalExpenditure() {
        return totalExpenditure;
    }

    public void setTotalExpenditure(Double totalExpenditure) {
        this.totalExpenditure = totalExpenditure;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Integer getInvalidPurchaseNum() {
        return invalidPurchaseNum;
    }

    public void setInvalidPurchaseNum(Integer invalidPurchaseNum) {
        this.invalidPurchaseNum = invalidPurchaseNum;
    }

    public Double getProfitLoss() {
        return profitLoss;
    }

    public void setProfitLoss(Double profitLoss) {
        this.profitLoss = profitLoss;
    }

    public Integer getGenralReportId() {
        return genralReportId;
    }

    public void setGenralReportId(Integer genralReportId) {
        this.genralReportId = genralReportId;
    }

    public Integer getCategroyNum() {
        return categroyNum;
    }

    public void setCategroyNum(Integer categroyNum) {
        this.categroyNum = categroyNum;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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

	public Integer getBuyCount() {
		return buyCount;
	}

	public void setBuyCount(Integer buyCount) {
		this.buyCount = buyCount;
	}
}