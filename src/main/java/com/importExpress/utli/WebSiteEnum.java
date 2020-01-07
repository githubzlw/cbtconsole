package com.importExpress.utli;

import java.util.HashMap;
import java.util.Map;

public enum WebSiteEnum {
    /**
     * IMPORT网站
     */
    IMPORTX(1,"https://www.import-express.com","ImportExpress"),
    /**
     * KIDS网站
     */
    KIDS(2,"https://www.kidsproductwholesale.com","KidsProductWholesale"),
    /**
     * PETS网站
     */
    PETS(3,"https://www.petstoreinc.com","PetStoreInc");
    /**
     * RESTAURANT网站
     */
    // RESTAURANT(4,"https://www.restaurantkitchenequipments.com"),
    /**
     * MEDICAL网站
     */
    // MEDICAL(5,"https://www.medicalequipments.com");


    /**
     * 下标
     */
    private int code;
    /**
     * 访问网站
     */
    private String url;

    /**
     * 网站名称
     */
    private String name;


    WebSiteEnum(int code) {
        this.code = code;
    }
    WebSiteEnum(int code, String url) {
        this.code = code;
        this.url = url;
    }

    WebSiteEnum(int code, String url,String name) {
        this.code = code;
        this.url = url;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        Map<Integer, String> webSizeMap = new HashMap<>(10);
        for (WebSiteEnum ws : WebSiteEnum.values()) {
            webSizeMap.put(ws.getCode(), ws.name());
        }
        System.err.println(webSizeMap.toString());
    }
}
