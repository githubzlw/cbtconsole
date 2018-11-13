package com.cbt.pojo;

public class CouponSubsidiary {
	private int id;
	private String batch;//批次
	private String promo_code;//优惠码
	private int userid;//用户ID
	private String user_name;//用户名称
    private int state;//状态  1待使用  2已使用 3占用中 4已失效
    private String disbursement;//发放形式
    private String get_time;//获取时间
    private String use_time;//使用时间
    private String validity_day;//有效期
    private String remark;//运营备注
    private String adm_name;//操作人
    private String is_enable;//是否启用  0启用  1停用
    private int validity_type;
	private String startdata;
    private String enddata;
    private String states;
    private String coupons_name;//优惠券名称
    private String denomination;//面额/折扣
    private String coupons_type;//优惠券类型
    private String minimum_cons;//最低消费
    private String operation;//操作
    private String using_range;//使用范围
    public String getUsing_range() {
		return using_range;
	}
	public void setUsing_range(String using_range) {
		this.using_range = using_range;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public String getMinimum_cons() {
		return minimum_cons;
	}
	public void setMinimum_cons(String minimum_cons) {
		this.minimum_cons = minimum_cons;
	}
	public String getCoupons_type() {
		return coupons_type;
	}
	public void setCoupons_type(String coupons_type) {
		this.coupons_type = coupons_type;
	}
	public String getDenomination() {
		return denomination;
	}
	public void setDenomination(String denomination) {
		this.denomination = denomination;
	}
	public String getCoupons_name() {
		return coupons_name;
	}
	public void setCoupons_name(String coupons_name) {
		this.coupons_name = coupons_name;
	}
	public String getStates() {
		return states;
	}
	public void setStates(String states) {
		this.states = states;
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
	public String getPromo_code() {
		return promo_code;
	}
	public void setPromo_code(String promo_code) {
		this.promo_code = promo_code;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getDisbursement() {
		return disbursement;
	}
	public void setDisbursement(String disbursement) {
		this.disbursement = disbursement;
	}
	public String getGet_time() {
		return get_time;
	}
	public void setGet_time(String get_time) {
		this.get_time = get_time;
	}
	public String getUse_time() {
		return use_time;
	}
	public void setUse_time(String use_time) {
		this.use_time = use_time;
	}
	public String getValidity_day() {
		return validity_day;
	}
	public void setValidity_day(String validity_day) {
		this.validity_day = validity_day;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAdm_name() {
		return adm_name;
	}
	public void setAdm_name(String adm_name) {
		this.adm_name = adm_name;
	}
	public String getIs_enable() {
		return is_enable;
	}
	public void setIs_enable(String is_enable) {
		this.is_enable = is_enable;
	}
}
