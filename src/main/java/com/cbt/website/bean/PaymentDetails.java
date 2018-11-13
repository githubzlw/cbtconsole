package com.cbt.website.bean;

/**
 * 支付明细
 *
 * @author JXW
 */
public class PaymentDetails {
    private String orderNo;// 订单号
    private int payType;// 支付渠道 0:paypal 1:Wire Transfer 2:余额支付
    private String payTypeDesc;
    private float paymentAmount;// 支付金额
    private String currency;// 货币单位
    private String paymentNo;// 交易号
    private String paymentTime;// 支付时间
    private String paymentEmail;// PayPal邮箱
    private int orderState;//订单状态
    private String orderStateDesc;//订单状态描述
    private String orderDesc;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
        //支付渠道 0:paypal 1:Wire Transfer 2:余额支付
        if (payType == 0) {
            this.payTypeDesc = "payPal支付";
        } else if (payType == 1) {
            this.payTypeDesc = "Wire Transfer";
        } else if (payType == 2) {
            this.payTypeDesc = "余额支付";
        }
    }

    public String getPayTypeDesc() {
        return payTypeDesc;
    }

    public void setPayTypeDesc(String payTypeDesc) {
        this.payTypeDesc = payTypeDesc;
    }

    public float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentEmail() {
        return paymentEmail;
    }

    public void setPaymentEmail(String paymentEmail) {

        if (paymentEmail.indexOf("@") > -1 && paymentEmail.indexOf(".") > -1) {
            this.paymentEmail = paymentEmail;
        } else {
            this.paymentEmail = "";
        }
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
        if (orderState == -1) {
            this.orderStateDesc = "后台取消";
        } else if (orderState == -1) {
            this.orderStateDesc = "后台取消";
        } else if (orderState == 0) {
            this.orderStateDesc = "未支付";
        } else if (orderState == 1) {
            this.orderStateDesc = "采购中";
        } else if (orderState == 2) {
            this.orderStateDesc = "入库";
        } else if (orderState == 3) {
            this.orderStateDesc = "出运";
        } else if (orderState == 4) {
            this.orderStateDesc = "完结";
        } else if (orderState == 5) {
            this.orderStateDesc = "审核中";
        } else if (orderState == 6) {
            this.orderStateDesc = "客户取消";
        }
    }

    public String getOrderStateDesc() {
        return orderStateDesc;
    }

    public void setOrderStateDesc(String orderStateDesc) {
        this.orderStateDesc = orderStateDesc;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
        if (orderDesc != null && orderDesc.indexOf("@") > -1 && orderDesc.indexOf(".") > -1) {
            this.paymentEmail = orderDesc;
        } else {
            this.paymentEmail = "";
        }
    }

    @Override
    public String toString() {
        return "PaymentDetails [orderNo=" + orderNo + ", payType=" + payType + ", paymentAmount=" + paymentAmount
                + ", currency=" + currency + ", paymentNo=" + paymentNo + ", paymentTime=" + paymentTime
                + ", paymentEmail=" + paymentEmail + "]";
    }


}
