package com.importExpress.utli;

import java.util.HashMap;
import java.util.Map;

public enum WebSizeEnum {
    IMPORTX(1), KIDS(2), PETS(3), RESTAURANT(4);

    private int code;

    WebSizeEnum(int code) {
        this.code = code;
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
