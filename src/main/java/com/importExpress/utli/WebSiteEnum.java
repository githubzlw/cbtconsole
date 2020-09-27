package com.importExpress.utli;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum WebSiteEnum {
    /**
     * IMPORT网站
     */
    IMPORTX(1,"https://www.import-express.com","Import-Express", 'I', 1),
    /**
     * KIDS网站
     */
    KIDS(2,"https://www.kidsproductwholesale.com","KidsProductWholesale", 'K', 2),
    /**
     * PETS网站
     */
    PETS(3,"https://www.petstoreinc.com","PetStoreInc",'P', 4),
    /**
     * CABLE网站
     */
    CABLE(4,"https://www.cablewirefactory.com/","CableWireFactory",'L', 64),
    /**
     * PIPE网站
     */
    PIPE(5,"https://www.pipetankfittings.com/","PipeTankFittings",'E', 32);


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


    private Character siteType;

    private int sourceCode;

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

    WebSiteEnum(int code, String url,String name, Character siteType) {
        this.code = code;
        this.url = url;
        this.name = name;
        this.siteType = siteType;
    }



    WebSiteEnum(int code, String url, String name, Character siteType, int sourceCode) {
        this.code = code;
        this.url = url;
        this.name = name;
        this.siteType = siteType;
        this.sourceCode = sourceCode;
    }


    public int getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(int sourceCode) {
        this.sourceCode = sourceCode;
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

    public Character getSiteType() {
        return siteType;
    }

    public void setSiteType(Character siteType) {
        this.siteType = siteType;
    }



    public static int getSourceCodeByCode(int code){
        if(code > 0){
            WebSiteEnum orElse = Arrays.stream(WebSiteEnum.values()).filter(e -> e.getCode() == code).findFirst().orElse(null);
            if(orElse != null){
                return  orElse.getSourceCode();
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }


    public static void main(String[] args) {
        Map<Integer, String> webSizeMap = new HashMap<>(10);
        for (WebSiteEnum ws : WebSiteEnum.values()) {
            webSizeMap.put(ws.getCode(), ws.name());
        }
        System.err.println(webSizeMap.toString());
    }
}
