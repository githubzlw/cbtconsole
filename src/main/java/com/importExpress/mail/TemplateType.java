package com.importExpress.mail;

/**
 * @author luohao
 * @date 2018/10/23
 */
public enum TemplateType {
    ACCOUNT_UPDATE("ACCOUNT-UPDATE"),
    ACTIVATION("ACTIVATION"),
    BUSINESS_INQUIRIES("BUSINESS-INQUIRIES"),
    CANCEL_ORDER("CANCEL-ORDER"),
    GOODS_CHANGE("GOODS-CHANGE"),
    CHECK("CHECK"),
    COMPLAINT("COMPLAINT"),
    BATCK("BATCK"),
    PURCHASE("PURCHASE"),
    DISMANTLING("DISMANTLING"),
    NEW_PASSWORD("NEW-PASSWORD"),
    PROBLEM_RESPONSE("PROBLEM-RESPONSE"),
    RECEIVED("RECEIVED"),
    SHOPPING_CART_MARKETING("SHOPPING-CART-MARKETING"),
    SHOPPING_CART_PROBLEM("SHOPPING-CART-PROBLEM"),
    WELCOME("WELCOME"),
    SHOPPING_CART("SHOPPING-CART");
    REPLACEGOODS("REPLACE-GOODS");

    private String name;

    private TemplateType(String name){
        this.name=name;
    }
    @Override
    public String toString(){
        return "mailTemplate/"+this.name.toLowerCase();
    }
}
