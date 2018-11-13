package com.cbt.refund.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RefundBeanExtend extends RefundBean implements Comparable<RefundBeanExtend>{
	//Payment
	private int pid;
	private int puserid;//用户id
	private String porderid;//订单流水号
	private String ppaymentid;//付款流水号
	private String porderdesc;//订单描述
	private String pusername;//用户名
	private int ppaystatus;//付款状态
	private float ppayment_amount;//付款金额
	private String ppayment_cc;//付款币种
	private String pcreatetime;
	private String ppaySID;//本地交易申请号
	private String ppayflag;
	private String ppaytype; //交易方式     渠道
	private float ppayment_other;//合并扣除费用
	
	//RechargeRecord
	
	private int rid;
	private int ruserid;
	private double rprice;
	private double rbalanceAfter;
	private String rdatatime;
	private int rtype;//充值类型:1.取消订单，2多余金额，3重复支付，4手动，5其他
	private String rremark;
	private String rremark_id;//注备ID：orderno
	private int rrusesign;//收入or支付
	private String rcurrency;
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public int getPuserid() {
		return puserid;
	}
	public void setPuserid(int puserid) {
		this.puserid = puserid;
	}
	public String getPorderid() {
		return porderid;
	}
	public void setPorderid(String porderid) {
		this.porderid = porderid;
	}
	public String getPpaymentid() {
		return ppaymentid;
	}
	public void setPpaymentid(String ppaymentid) {
		this.ppaymentid = ppaymentid;
	}
	public String getPorderdesc() {
		return porderdesc;
	}
	public void setPorderdesc(String porderdesc) {
		this.porderdesc = porderdesc;
	}
	public String getPusername() {
		return pusername;
	}
	public void setPusername(String pusername) {
		this.pusername = pusername;
	}
	public int getPpaystatus() {
		return ppaystatus;
	}
	public void setPpaystatus(int ppaystatus) {
		this.ppaystatus = ppaystatus;
	}
	public float getPpayment_amount() {
		return ppayment_amount;
	}
	public void setPpayment_amount(float ppayment_amount) {
		this.ppayment_amount = ppayment_amount;
	}
	public String getPpayment_cc() {
		return ppayment_cc;
	}
	public void setPpayment_cc(String ppayment_cc) {
		this.ppayment_cc = ppayment_cc;
	}
	public String getPcreatetime() {
		return pcreatetime;
	}
	public void setPcreatetime(String pcreatetime) {
		this.pcreatetime = pcreatetime;
	}
	public String getPpaySID() {
		return ppaySID;
	}
	public void setPpaySID(String ppaySID) {
		this.ppaySID = ppaySID;
	}
	public String getPpayflag() {
		return ppayflag;
	}
	public void setPpayflag(String ppayflag) {
		this.ppayflag = ppayflag;
	}
	public String getPpaytype() {
		return ppaytype;
	}
	public void setPpaytype(String ppaytype) {
		this.ppaytype = ppaytype;
	}
	public float getPpayment_other() {
		return ppayment_other;
	}
	public void setPpayment_other(float ppayment_other) {
		this.ppayment_other = ppayment_other;
	}
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public int getRuserid() {
		return ruserid;
	}
	public void setRuserid(int ruserid) {
		this.ruserid = ruserid;
	}
	public double getRprice() {
		return rprice;
	}
	public void setRprice(double rprice) {
		this.rprice = rprice;
	}
	public double getRbalanceAfter() {
		return rbalanceAfter;
	}
	public void setRbalanceAfter(double rbalanceAfter) {
		this.rbalanceAfter = rbalanceAfter;
	}
	public String getRdatatime() {
		return rdatatime;
	}
	public void setRdatatime(String rdatatime) {
		this.rdatatime = rdatatime;
	}
	public int getRtype() {
		return rtype;
	}
	public void setRtype(int rtype) {
		this.rtype = rtype;
	}
	public String getRremark() {
		return rremark;
	}
	public void setRremark(String rremark) {
		this.rremark = rremark;
	}
	public String getRremark_id() {
		return rremark_id;
	}
	public void setRremark_id(String rremark_id) {
		this.rremark_id = rremark_id;
	}
	public int getRrusesign() {
		return rrusesign;
	}
	public void setRrusesign(int rrusesign) {
		this.rrusesign = rrusesign;
	}
	public String getRcurrency() {
		return rcurrency;
	}
	public void setRcurrency(String rcurrency) {
		this.rcurrency = rcurrency;
	}
	
	@Override
	public int compareTo(RefundBeanExtend o) {
		RefundBeanExtend s1 = (RefundBeanExtend) o;
		String time1 = s1.getPcreatetime();
		if(s1.getPuserid()==0){
			time1 = s1.getRdatatime();
		}
		String time2 = this.getPcreatetime();
		if(this.getPuserid()==0){
			time2 = this.getRdatatime();
		}
		
		if((time1==null||time1.isEmpty())||(time2==null||time2.isEmpty())){
			return 0;
		}
		
		if(time1.indexOf(".0")>0){
			time1 = time1.substring(0, time1.length()-2);
		}
		if(time2.indexOf(".0")>0){
			time2 = time2.substring(0, time2.length()-2);
		}
		
        DateFormat df = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
		long time_long1 = 0;
		long time_long2 = 0;
		try {
			time_long1 = df.parse(time1).getTime();
			time_long2 = df.parse(time2).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(time_long1>time_long2){
			return -1;
		}
		if(time_long1<time_long2){
			return 1;
		}
		
		return 0;
	}
	
}

	
