package com.importExpress.pojo;

import lombok.Data;

@Data
public class GoodsCarconfigWithBLOBs extends GoodsCarconfig {
    private String shopcarinfo;

    private String shopcarshowinfo;

    private String saveforlatershowinfo;

    private String saveforlaterinfo;

    private String buyformecarconfig;

    private String kidscarconfig;
}