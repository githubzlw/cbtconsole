package com.cbt.warehouse.pojo;



public class Shipments {
    private Long id;

    private String orderno;

    private String switchno;

    private String addressee;

    private String country;

    private String transportcompany;

    private String transporttype;

    private Integer numbers;

    private String type;

    private String realweight;

    private String bulkweight;

    private String settleweight;

    private String charge;

    private String fuelsurcharge;

    private String securitycosts;

    private String taxs;

    private String totalprice;

    private String remark;


    public String getSenttimes() {
        return senttimes;
    }

    public void setSenttimes(String senttimes) {
        this.senttimes = senttimes;
    }

    private String senttimes;

    private String uuid;

    private Integer validate;

    //私有字段
    //预估运费
    private String estimatefreight;

    //实际出库运费
    private String freight;

    //出库重量(出库重量 or 体积/5000)取最大值
    private String volumeweight;

    private String volumeweights;//我司抛重


    //系统重量
    private  String  sweight ;
    //系统体积
    private  String  svolume ;
    //系统运输方式
    private  String  tscompany ;
    //系统选择的国家
    private  String  xtcountry ;
    //交期
    private  String  delivery ;

    private  int   passFlag ;

    private  int  count ;  //上传运单条数
    //----------------------\
    private  String  emsintenFreight;  //邮政运费
    private  String  jecxFreight;      //JCEX 运费
    private  String  yfhFreight;       //原飞航 运费
    private  String  otherFreight;      // 其他运费
    private  String      createTime ;       //统计时间
    private String sp_time;

    private String actual_freight;//变更实际出库金额
    private String flag_remarks;
    public String getFlag_remarks() {
        return flag_remarks;
    }

    public void setFlag_remarks(String flag_remarks) {
        this.flag_remarks = flag_remarks;
    }

    public String getActual_freight() {
        return actual_freight;
    }

    public void setActual_freight(String actual_freight) {
        this.actual_freight = actual_freight;
    }

    public String getVolumeweights() {
        return volumeweights;
    }

    public void setVolumeweights(String volumeweights) {
        this.volumeweights = volumeweights;
    }
    public String getSp_time() {
        return sp_time;
    }

    public void setSp_time(String sp_time) {
        this.sp_time = sp_time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPassFlag() {
        return passFlag;
    }

    public void setPassFlag(int passFlag) {
        this.passFlag = passFlag;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno == null ? null : orderno.trim();
    }

    public String getSwitchno() {
        return switchno;
    }

    public void setSwitchno(String switchno) {
        this.switchno = switchno == null ? null : switchno.trim();
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee == null ? null : addressee.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getTransportcompany() {
        return transportcompany;
    }

    public void setTransportcompany(String transportcompany) {
        this.transportcompany = transportcompany == null ? null : transportcompany.trim();
    }

    public String getTransporttype() {
        return transporttype;
    }

    public void setTransporttype(String transporttype) {
        this.transporttype = transporttype == null ? null : transporttype.trim();
    }

    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getRealweight() {
        return realweight;
    }

    public void setRealweight(String realweight) {
        this.realweight = realweight;
    }

    public String getBulkweight() {
        return bulkweight;
    }

    public void setBulkweight(String bulkweight) {
        this.bulkweight = bulkweight;
    }

    public String getSettleweight() {
        return settleweight;
    }

    public void setSettleweight(String settleweight) {
        this.settleweight = settleweight;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getFuelsurcharge() {
        return fuelsurcharge;
    }

    public void setFuelsurcharge(String fuelsurcharge) {
        this.fuelsurcharge = fuelsurcharge;
    }

    public String getSecuritycosts() {
        return securitycosts;
    }

    public void setSecuritycosts(String securitycosts) {
        this.securitycosts = securitycosts;
    }

    public String getTaxs() {
        return taxs;
    }

    public void setTaxs(String taxs) {
        this.taxs = taxs;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    public Integer getValidate() {
        return validate;
    }

    public void setValidate(Integer validate) {
        this.validate = validate;
    }

    public String getEstimatefreight() {
        return estimatefreight;
    }

    public void setEstimatefreight(String estimatefreight) {
        this.estimatefreight = estimatefreight;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getVolumeweight() {
        return volumeweight;
    }

    public void setVolumeweight(String volumeweight) {
        this.volumeweight = volumeweight;
    }



    public String getSweight() {
        return sweight;
    }

    public void setSweight(String sweight) {
        this.sweight = sweight;
    }


    public String getSvolume() {
        return svolume;
    }

    public void setSvolume(String svolume) {
        this.svolume = svolume;
    }

    public String getTscompany() {
        return tscompany;
    }

    public void setTscompany(String tscompany) {
        this.tscompany = tscompany;
    }

    public String getXtcountry() {
        return xtcountry;
    }

    public void setXtcountry(String xtcountry) {
        this.xtcountry = xtcountry;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getEmsintenFreight() {
        return emsintenFreight;
    }

    public void setEmsintenFreight(String emsintenFreight) {
        this.emsintenFreight = emsintenFreight;
    }

    public String getJecxFreight() {
        return jecxFreight;
    }

    public void setJecxFreight(String jecxFreight) {
        this.jecxFreight = jecxFreight;
    }

    public String getYfhFreight() {
        return yfhFreight;
    }

    public void setYfhFreight(String yfhFreight) {
        this.yfhFreight = yfhFreight;
    }

    public String getOtherFreight() {
        return otherFreight;
    }

    public void setOtherFreight(String otherFreight) {
        this.otherFreight = otherFreight;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }




}