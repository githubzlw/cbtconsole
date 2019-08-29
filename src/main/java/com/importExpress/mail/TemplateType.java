package com.importExpress.mail;

/**
 * @author luohao
 * @date 2018/10/23
 */
public enum TemplateType {
    ACCOUNT_UPDATE("ACCOUNT-UPDATE"),
    ACTIVATION("ACTIVATION"),
    BUSINESS_INQUIRIES("BUSINESS-INQUIRIES"),
    BUSINESS_INQUIRIES_KIDS("BUSINESS-INQUIRIES-KIDS"),
    BUSINESS_INQUIRIES_PET("BUSINESS-INQUIRIES-PET"),
    CANCEL_ORDER_IMPORT("CANCEL-ORDER-IMPORT"),
    CANCEL_ORDER_KID("CANCEL-ORDER-KID"),
    CANCEL_ORDER_PET("CANCEL-ORDER-PET"),
    CANCEL_ORDER_RESTAURANT("CANCEL-ORDER-RESTAURANT"),
    GOODS_CHANGE("GOODS-CHANGE"),
    GOODS_CHANGE_KIDS("GOODS-CHANGE-KIDS"),
    GOODS_CHANGE_PET("GOODS-CHANGE-PET"),
    CHECK("CHECK"),
    COMPLAINT("COMPLAINT"),
    BATCK("BATCK"),
    PURCHASE("PURCHASE"),
    DISMANTLING_IMPORT("DISMANTLING-IMPORT"),
    DISMANTLING_KID("DISMANTLING-KID"),
    DISMANTLING_PET("DISMANTLING-PET"),
    DISMANTLING_RESTAURANT("DISMANTLING-RESTAURANT"),
    NEW_PASSWORD("NEW-PASSWORD"),
    SHOPPING_REPLY("SHOPPING-REPLY"),
    PROBLEM_RESPONSE("PROBLEM-RESPONSE"),
    RECEIVED("RECEIVED"),
    SHOPPING_CART_MARKETING("SHOPPING-CART-MARKETING"),
    SHOPPING_CART_PROBLEM("SHOPPING-CART-PROBLEM"),
    WELCOME("WELCOME"),
    SHOPPING_CART_NO_CHANGE("SHOPPING-CART-NO-CHANGE"),
    SHOPPING_CART_UPDATE_PRICE("SHOPPING-CART-UPDATE-PRICE"),
    SHOPPING_CART_FREIGHT_COUPON("SHOPPING-CART-FREIGHT-COUPON"),
    SHOPPING_CART_BEST_TRANSPORT("SHOPPING-CART-BEST-TRANSPORT"),
    BATCK_KIDS("BATCK_KIDS"),
    BATCK_PET("BATCK_PET"),
    SHOPPING_CART_NO_CHANGE_IMPORT("SHOPPING-CART-NO-CHANGE-IMPORT"),
    SHOPPING_CART_UPDATE_PRICE_IMPORT("SHOPPING-CART-UPDATE-PRICE-IMPORT"),
    SHOPPING_CART_FREIGHT_COUPON_IMPORT("SHOPPING-CART-FREIGHT-COUPON-IMPORT"),
    SHOPPING_CART_BEST_TRANSPORT_IMPORT("SHOPPING-CART-BEST-TRANSPORT-IMPORT"),
    SHOPPING_CART_NO_CHANGE_KID("SHOPPING-CART-NO-CHANGE-KID"),
    SHOPPING_CART_UPDATE_PRICE_KID("SHOPPING-CART-UPDATE-PRICE-KID"),
    SHOPPING_CART_FREIGHT_COUPON_KID("SHOPPING-CART-FREIGHT-COUPON-KID"),
    SHOPPING_CART_BEST_TRANSPORT_KID("SHOPPING-CART-BEST-TRANSPORT-KID"),
    SHOPPING_CART_NO_CHANGE_PET("SHOPPING-CART-NO-CHANGE-PET"),
    SHOPPING_CART_UPDATE_PRICE_PET("SHOPPING-CART-UPDATE-PRICE-PET"),
    SHOPPING_CART_FREIGHT_COUPON_PET("SHOPPING-CART-FREIGHT-COUPON-PET"),
    SHOPPING_CART_BEST_TRANSPORT_PET("SHOPPING-CART-BEST-TRANSPORT-PET"),
    SHOPPING_CART_NO_CHANGE_RESTAURANT("SHOPPING-CART-NO-CHANGE-RESTAURANT"),
    SHOPPING_CART_UPDATE_PRICE_RESTAURANT("SHOPPING-CART-UPDATE-PRICE-RESTAURANT"),
    SHOPPING_CART_FREIGHT_COUPON_RESTAURANT("SHOPPING-CART-FREIGHT-COUPON-RESTAURANT"),
    SHOPPING_CART_BEST_TRANSPORT_RESTAURANT("SHOPPING-CART-BEST-TRANSPORT-RESTAURANT"),

    GUESTBOOK_REPLY("GUESTBOOK-REPLY"),
    REPLACEGOODS("REPLACE-GOODS"),
    COUPON("COUPON");

    private String name;

    private TemplateType(String name){
        this.name=name;
    }
    @Override
    public String toString(){
        return "mailTemplate/"+this.name.toLowerCase();
    }
}
