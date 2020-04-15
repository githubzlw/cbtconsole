package com.importExpress.pojo;

import lombok.Data;

@Data
public class Price {
	private String price;//价格--免邮
    private String factoryPrice;//工厂价格--非免邮
    private String feight;//我司运费
    private String quantity;//数量
    private int moq;//最小定量


    public Price(String price, String quantity) {
        super();
        this.price = price;
        this.quantity = quantity;
    }
    public Price(String price,String factoryPrice, String quantity) {
        super();
        this.price = price;
        this.factoryPrice = factoryPrice;
        this.quantity = quantity;
    }
    public Price() {
        super();
    }
}
