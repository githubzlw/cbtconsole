package com.importExpress.utli;

import java.util.HashMap;
import java.util.Map;

public enum WebSizeEnum {
    IMPORTX(1,"https://www.import-express.com"), KIDS(2,"https://www.kidsproductwholesale.com"),
    PETS(3,"https://www.lovelypetsupply.com"), RESTAURANT(4,"https://www.restaurantkitchenequipments.com");


    /**
     * 下标
     */
    private int code;
    /**
     * 访问网站
     */
    private String url;


    WebSizeEnum(int code) {
        this.code = code;
    }
    WebSizeEnum(int code, String url) {
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
        for (WebSizeEnum ws : WebSizeEnum.values()) {
            webSizeMap.put(ws.getCode(), ws.name());
        }
        System.err.println(webSizeMap.toString());
    }
}
