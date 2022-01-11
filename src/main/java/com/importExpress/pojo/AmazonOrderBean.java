package com.importExpress.pojo;

import lombok.Data;

@Data
public class AmazonOrderBean {

    private int id;
    private String date_time;
    private String fn_sku;
    private String asin;
    private String msku;
    private String title;
    private String disposition;
    private String starting_warehouse_balance;
    private String in_transit_between_warehouses;
    private String receipts;
    private String customer_shipments;
    private String customer_returns;
    private String vendor_returns;
    private String warehouse_transfer;

    private String found;
    private String lost;
    private String damaged;
    private String disposed;

    private String other_events;
    private String ending_warehouse_balance;
    private String unknown_events;
    private String location;

}
