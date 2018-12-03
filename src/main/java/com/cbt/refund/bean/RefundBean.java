package com.cbt.refund.bean;


import com.cbt.bean.Payment;

import java.util.List;

public class RefundBean {
    private int id;
    private int userid;//用户id
    private String username;  //客户账号
    private String userEmail;//用户名称
    private double appcount;//申请金额
    private double balance;
    private String apptime;//申请时间
    private int agreeapp;
    private String agreetime;//
    private int isend;
    private int iscancle;
    private int valid;//数据有效
    private String agree;//申请进度（还未同意、银行处理中、完结、拒绝、用户取消）
    private String refundstate;
    private String chkable;
    private String endtime;
    private String currency;//货币
    private String remark;//备注
    private String paypalname;       //paypal帐号
    private String balanceCurrency;  //用户余额货币单位
    private String transactionCode;   // Paypal交易流水号
    protected String currencyshow;
    protected String adminUid;        //该退款客户对应的管理员id
    private String feedback;        //6.3根据Emma 新需求，添加后台人员使用的反馈功能
    private String refuse;  // 7.18 拒绝退款的理由
    private Integer status;//8-24 退款状态   0-申请退款 1-同意退款 2-退款完结 -1-驳回退款 -2 -用户取消退款
    private int type;//来源   0-余额提现  1-paypal申诉   2-售后投诉
    private double account;//实际退款金额
    private String orderid;//订单号
    private String payid;//付款号
    private int additionid;//余额补偿表id
    private int paypalstate;//paypal申诉状态  0-有异常   1-正常处理
    private String reasoncode;//paypal申诉退款原因
    private String reasonnote;//paypal申诉退款原因备注
    private String casetype;//paypal类型
    private int complainId;//投诉表id
    private String refundType;//退款类型
    private int count;
    private double additionBanlance;//投诉余额补偿金额
    private String outcomeCode;//paypal申诉结果
    private String delaytime;//paypal申诉处理到期时间
    private int isDelay;//距离paypal自动处理时间是否在3天之内
    private String statusDesc;

    private String appAcounts;
    private String user_id;
    private String operation;
    private String states;
    private List<Payment> orderNoList;

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }

    public int getIsDelay() {
        return isDelay;
    }

    public void setIsDelay(int isDelay) {
        this.isDelay = isDelay;
    }

    public String getDelaytime() {
        return delaytime;
    }

    public void setDelaytime(String delaytime) {
        this.delaytime = delaytime;
    }

    public String getOutcomeCode() {
        return outcomeCode;
    }

    public void setOutcomeCode(String outcomeCode) {
        this.outcomeCode = outcomeCode;
    }

    public double getAdditionBanlance() {
        return additionBanlance;
    }

    public void setAdditionBanlance(double additionBanlance) {
        this.additionBanlance = additionBanlance;
    }

    public int getComplainId() {
        return complainId;
    }

    public void setComplainId(int complainId) {
        this.complainId = complainId;
    }

    public int getPaypalstate() {
        return paypalstate;
    }

    public void setPaypalstate(int paypalstate) {
        this.paypalstate = paypalstate;
    }

    public String getReasoncode() {
        return reasoncode;
    }

    public void setReasoncode(String reasoncode) {
        this.reasoncode = reasoncode;
    }

    public String getReasonnote() {
        return reasonnote;
    }

    public void setReasonnote(String reasonnote) {
        this.reasonnote = reasonnote;
    }

    public String getCasetype() {
        return casetype;
    }

    public void setCasetype(String casetype) {
        this.casetype = casetype;
    }

    public int getAdditionid() {
        return additionid;
    }

    public void setAdditionid(int additionid) {
        this.additionid = additionid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        //0-新申请 1-销售审批 2-主管审批 3-财务确认 4-已经完结 9驳回  -1客户取消
        if (status == 0) {
            this.statusDesc = "新申请";
        } else if (status == 1) {
            this.statusDesc = "销售审批";
        } else if (status == 2) {
            this.statusDesc = "主管审批";
        } else if (status == 3) {
            this.statusDesc = "财务确认";
        } else if (status == 4) {
            this.statusDesc = "已经完结";
        } else if (status == 9) {
            this.statusDesc = "驳回";
        } else if (status == -1) {
            this.statusDesc = "客户取消";
        } else {
            this.statusDesc = "";
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getRefuse() {
        return refuse;
    }

    public void setRefuse(String refuse) {
        this.refuse = refuse;
    }


    public RefundBean() {
        super();
    }

    public RefundBean(int id, int userid, String username, double appcount,
                      double balance, String apptime, String chkable, int agreeapp,
                      String agreetime, int isend, int iscancle, int valid, String agree,
                      String refundstate, String endtime, String currency, String remark,
                      String paypalname, String balanceCurrency) {
        super();
        this.id = id;
        this.userid = userid;
        this.username = username;
        this.appcount = appcount;
        this.balance = balance;
        this.apptime = apptime;
        this.agreeapp = agreeapp;
        this.agreetime = agreetime;
        this.isend = isend;
        this.iscancle = iscancle;
        this.valid = valid;
        this.agree = agree;
        this.refundstate = refundstate;
        this.chkable = chkable;
        this.endtime = endtime;
        this.remark = remark;
        this.paypalname = paypalname;
        this.balanceCurrency = balanceCurrency;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPayid() {
        return payid;
    }

    public void setPayid(String payid) {
        this.payid = payid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getAppcount() {
        return appcount;
    }

    public void setAppcount(double appcount) {
        this.appcount = appcount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getApptime() {
        return apptime;
    }

    public void setApptime(String apptime) {
        this.apptime = apptime;
    }

    public int getAgreeapp() {
        return agreeapp;
    }

    public void setAgreeapp(int agreeapp) {
        this.agreeapp = agreeapp;
    }

    public String getAgreetime() {
        return agreetime;
    }

    public void setAgreetime(String agreetime) {
        this.agreetime = agreetime;
    }

    public int getIsend() {
        return isend;
    }

    public void setIsend(int isend) {
        this.isend = isend;
    }

    public int getIscancle() {
        return iscancle;
    }

    public void setIscancle(int iscancle) {
        this.iscancle = iscancle;
    }

    public int getValid() {
        return valid;
    }

    public void setValid(int valid) {
        this.valid = valid;
    }

    public String getAgree() {
        return agree;
    }

    public void setAgree(String agree) {
        this.agree = agree;
    }

    public String getRefundstate() {
        return refundstate;
    }

    public void setRefundstate(String refundstate) {
        this.refundstate = refundstate;
    }

    public String getChkable() {
        return chkable;
    }

    public void setChkable(String chkable) {
        this.chkable = chkable;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPaypalname() {
        return paypalname;
    }

    public void setPaypalname(String paypalname) {
        this.paypalname = paypalname;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getBalanceCurrency() {
        return balanceCurrency;
    }

    public void setBalanceCurrency(String balanceCurrency) {
        this.balanceCurrency = balanceCurrency;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getCurrencyshow() {
        return currencyshow;
    }

    public void setCurrencyshow(String currencyshow) {
        this.currencyshow = currencyshow;
    }

    public String getAdminUid() {
        return adminUid;
    }

    public void setAdminUid(String adminUid) {
        this.adminUid = adminUid;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getAppAcounts() {
        return appAcounts;
    }

    public void setAppAcounts(String appAcounts) {
        this.appAcounts = appAcounts;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public List<Payment> getOrderNoList() {
        return orderNoList;
    }

    public void setOrderNoList(List<Payment> orderNoList) {
        this.orderNoList = orderNoList;
    }
}
