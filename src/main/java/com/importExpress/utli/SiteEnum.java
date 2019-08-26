package com.importExpress.utli;

/**
 * @author luohao
 * @date 2019/6/25
 */
public enum SiteEnum {

    IMPORTX(1),KIDS(2),PETS(4);

    private int code;

    SiteEnum(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

    public static void main(String[] args){
        System.out.println(SiteEnum.valueOf("importx"));
        System.out.println(SiteEnum.valueOf("pets"));
        System.out.println(SiteEnum.valueOf("pets").code);
    }
}
