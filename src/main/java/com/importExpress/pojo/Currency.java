package com.importExpress.pojo;

import lombok.Data;

@Data
public class Currency {
    private String currency = "USD";
    private double exchangeRate = 1.0;
    private String symbol = "$";

}
