package com.importExpress.pojo;

import java.util.Date;

public class ShopUrlAuthorizedInfoPO {

	private Long id;

	// 店铺ID
	private String shopId;

	// 授权文件名
	private String fileName;

	// 该文件在本地路径（下载时候会用到）
	private String fileUrl;

	// 该文件在图片服务器中地址（只做备份 未使用）
	private String imgFileUrl;

	// 文件上传人
	private Integer adminId = 0;

	private String admuser;

	// 授权开始时间（默认上传时间）
	private Date startTime;

	// 授权结束时间（默认一年后时间）
	private Date endTime;

	// 授权备注
	private String remark;

	// 是否有效 1-默认有效；2-授权时间到了 失效；3-用户删除
	private Integer valid = 1;

	private Date createtime;

	private Date updatetime;

	private String shopBrand;//店铺品牌属性

    public String getShopBrand() {
        return shopBrand;
    }

    public void setShopBrand(String shopBrand) {
        this.shopBrand = shopBrand;
    }

    public String getImgFileUrl() {
        return imgFileUrl;
    }

    public void setImgFileUrl(String imgFileUrl) {
        this.imgFileUrl = imgFileUrl;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getValid() {
		return valid;
	}

	public void setValid(Integer valid) {
		this.valid = valid;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	
	public String getAdmuser() {
		return admuser;
	}

	public void setAdmuser(String admuser) {
		this.admuser = admuser;
	}

	public ShopUrlAuthorizedInfoPO() {
		super();
	}

	public ShopUrlAuthorizedInfoPO(Long id, String shopId, Integer adminId, String admuser, Date startTime, Date endTime, String remark, String shopBrand) {
		super();
		this.id = id;
		this.shopId = shopId;
		this.adminId = adminId;
		this.admuser = admuser;
		this.startTime = startTime;
		this.endTime = endTime;
		this.remark = remark;
		this.shopBrand = shopBrand;
	}

	@Override
	public String toString() {
		return "ShopUrlAuthorizedInfoPO [id=" + id + ", shopId=" + shopId + ", fileName=" + fileName + ", fileUrl="
				+ fileUrl + ", adminId=" + adminId + ", admuser=" + admuser + ", startTime=" + startTime + ", endTime="
				+ endTime + ", remark=" + remark + ", valid=" + valid + ", createtime=" + createtime + ", updatetime="
				+ updatetime + "]";
	}
	
}