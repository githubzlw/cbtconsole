package com.importExpress.pojo;

import lombok.Data;

@Data
public class AmazonOrderParam extends AmazonOrderBean{

    private int pageSize;
    private int pageNum;

}
