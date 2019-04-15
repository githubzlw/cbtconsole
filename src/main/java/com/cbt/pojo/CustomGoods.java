package com.cbt.pojo;

import java.sql.Timestamp;

public class CustomGoods {
	//
	private Integer id;
	private String pid;
	private String shopid;
	private String localpath;
	private String remotepath;
	private String goodsmd5;
	private Timestamp createtime;
	private int isdelete;
	private int ocrneeddelete;
	private int count;
	private String name;
	private String catgrid;
	private String useroperation;
	private int ocrneeddate;
	private Category1688 Category1688Map;


	public String getShopid() { return shopid; }

	public void setShopid(String shopid) { this.shopid = shopid; }

	public String getLocalpath() { return localpath; }

	public void setLocalpath(String localpath) { this.localpath = localpath; }

	public String getRemotepath() { return remotepath; }

	public void setRemotepath(String remotepath) { this.remotepath = remotepath; }

	public String getGoodsmd5() { return goodsmd5; }

	public void setGoodsmd5(String goodsmd5) { this.goodsmd5 = goodsmd5; }

	public Timestamp getCreatetime() { return createtime; }

	public void setCreatetime(Timestamp createtime) { this.createtime = createtime; }

	public int getIsdelete() { return isdelete; }

	public void setIsdelete(int isdelete) { this.isdelete = isdelete; }

	public int getOcrneeddelete() { return ocrneeddelete; }

	public void setOcrneeddelete(int ocrneeddelete) { this.ocrneeddelete = ocrneeddelete; }

	public Integer getId() { return id; }

	public void setId(Integer id) { this.id = id; }

	public String getPid() { return pid; }

	public void setPid(String pid) { this.pid = pid; }

	public int getCount() { return count; }

	public void setCount(int count) { this.count = count; }

	public String getName() { return name; }

	public void setName(String name) { this.name = name; }

	public String getCatgrid() { return catgrid; }

	public void setCatgrid(String catgrid) { this.catgrid = catgrid; }

	public String getUseroperation() { return useroperation; }

	public void setUseroperation(String useroperation) { this.useroperation = useroperation; }

	public int getOcrneeddate() { return ocrneeddate; }

	public void setOcrneeddate(int ocrneeddate) { this.ocrneeddate = ocrneeddate; }
	@Override
	public String toString(){
		return "CustomGoods:[id="+ getId() +",shopid="+shopid+",localpath="+localpath+",remotepath="+remotepath+",goodsmd5"+goodsmd5
				+",createtime="+createtime+",isdelete="+isdelete+",ocrneeddelete="+ocrneeddelete+",name="+name+",catgrid="+catgrid+",useroperation="+useroperation+",ocrneeddate="+ocrneeddate+"]";
	}


	public Category1688 getCategory1688Map() {
		return Category1688Map;
	}

	public void setCategory1688Map(Category1688 category1688Map) {
		Category1688Map = category1688Map;
	}
}