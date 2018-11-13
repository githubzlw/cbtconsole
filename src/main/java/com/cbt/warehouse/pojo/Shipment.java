package com.cbt.warehouse.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Shipment {
    private Long id;

    private Date senttime;
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

    private Date createtime;

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

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public String getSenttime() {
        return sdf.format(senttime);
    }

    public void setSenttime(Date senttime) {
        this.senttime = senttime;
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

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressee == null) ? 0 : addressee.hashCode());
		result = prime * result + ((bulkweight == null) ? 0 : bulkweight.hashCode());
		result = prime * result + ((charge == null) ? 0 : charge.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((createtime == null) ? 0 : createtime.hashCode());
		result = prime * result + ((fuelsurcharge == null) ? 0 : fuelsurcharge.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((numbers == null) ? 0 : numbers.hashCode());
		result = prime * result + ((orderno == null) ? 0 : orderno.hashCode());
		result = prime * result + ((realweight == null) ? 0 : realweight.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((securitycosts == null) ? 0 : securitycosts.hashCode());
		result = prime * result + ((senttime == null) ? 0 : senttime.hashCode());
		result = prime * result + ((settleweight == null) ? 0 : settleweight.hashCode());
		result = prime * result + ((switchno == null) ? 0 : switchno.hashCode());
		result = prime * result + ((taxs == null) ? 0 : taxs.hashCode());
		result = prime * result + ((totalprice == null) ? 0 : totalprice.hashCode());
		result = prime * result + ((transportcompany == null) ? 0 : transportcompany.hashCode());
		result = prime * result + ((transporttype == null) ? 0 : transporttype.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = prime * result + ((validate == null) ? 0 : validate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shipment other = (Shipment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (orderno == null) {
			if (other.orderno != null)
				return false;
		} else if (!orderno.equals(other.orderno))
			return false;
		return true;
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