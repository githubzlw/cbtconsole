package com.importExpress.utli;

import java.util.HashMap;
import java.util.Map;

public enum WebSiteEnum {
    /**
     * IMPORT网站
     */
    IMPORTX(1,"https://www.import-express.com"),
    /**
     * KIDS网站
     */
    KIDS(2,"https://www.kidsproductwholesale.com"),
    /**
     * PETS网站
     */
    PETS(3,"https://www.lovelypetsupply.com"),
    /**
     * RESTAURANT网站
     */
    RESTAURANT(4,"https://www.restaurantkitchenequipments.com"),
    /**
     * MEDICAL网站
     */
    MEDICAL(5,"https://www.medicalequipments.com");


    /**
     * 下标
     */
    private int code;
    /**
     * 访问网站
     */
    private String url;


    WebSiteEnum(int code) {
        this.code = code;
    }
    WebSiteEnum(int code, String url) {
        this.code = code;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static void main(String[] args) {
        Map<Integer, String> webSizeMap = new HashMap<>(10);
        for (WebSiteEnum ws : WebSiteEnum.values()) {
            webSizeMap.put(ws.getCode(), ws.name());
        }
        System.err.println(webSizeMap.toString());
    }
}
