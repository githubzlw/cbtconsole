package com.cbt.pojo;

public class Coupons {
	private int id;
	  private String batch;//批次
	  private String coupons_name;//优惠券名称
	  private String coupons_type;//优惠券类型 1限额券 2折扣券 3运费抵用券
	  private double denomination;//优惠券面额
	  private int total_circulation;//总发行量
	  private double total_amount;//总金额
	  private double minimum_cons;//最低消费
	  private double most_favorable;//最多优惠
	  private int validity_type;//有效期类型
	  private String using_range;//使用范围
	  private String disbursement;//发放形式 1新用户发放 2用户自领 3下单奖励
	  private String createtime;//创建时间
	  private String create_adm;//创建人
	  private String is_enable;//是否启用  0启用  1停用
	  private int for_most;//每位用户最多领取张数
	  private String startdata;
	  private String enddata;
	  private String validity_day;
	  private String operation;
	  public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getValidity_day() {
		return validity_day;
	}
	public void setValidity_day(String validity_day) {
		this.validity_day = validity_day;
	}
	public int getValidity_type() {
		return validity_type;
	}
	public void setValidity_type(int validity_type) {
		this.validity_type = validity_type;
	}
	public String getStartdata() {
		return startdata;
	}
	public void setStartdata(String startdata) {
		this.startdata = startdata;
	}
	public String getEnddata() {
		return enddata;
	}
	public void setEnddata(String enddata) {
		this.enddata = enddata;
	}
  public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getCoupons_name() {
		return coupons_name;
	}
	public void setCoupons_name(String coupons_name) {
		this.coupons_name = coupons_name;
	}
	public String getCoupons_type() {
		return coupons_type;
	}
	public void setCoupons_type(String coupons_type) {
		this.coupons_type = coupons_type;
	}
	public double getDenomination() {
		return denomination;
	}
	public void setDenomination(double denomination) {
		this.denomination = denomination;
	}
	public int getTotal_circulation() {
		return total_circulation;
	}
	public void setTotal_circulation(int total_circulation) {
		this.total_circulation = total_circulation;
	}
	public double getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(double total_amount) {
		this.total_amount = total_amount;
	}
	public double getMinimum_cons() {
		return minimum_cons;
	}
	public void setMinimum_cons(double minimum_cons) {
		this.minimum_cons = minimum_cons;
	}
	public double getMost_favorable() {
		return most_favorable;
	}
	public void setMost_favorable(double most_favorable) {
		this.most_favorable = most_favorable;
	}
	public String getUsing_range() {
		return using_range;
	}
	public void setUsing_range(String using_range) {
		this.using_range = using_range;
	}
	public String getDisbursement() {
		return disbursement;
	}
	public void setDisbursement(String disbursement) {
		this.disbursement = disbursement;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getCreate_adm() {
		return create_adm;
	}
	public void setCreate_adm(String create_adm) {
		this.create_adm = create_adm;
	}
	public String getIs_enable() {
		return is_enable;
	}
	public void setIs_enable(String is_enable) {
		this.is_enable = is_enable;
	}
	public int getFor_most() {
		return for_most;
	}
	public void setFor_most(int for_most) {
		this.for_most = for_most;
	}
}
