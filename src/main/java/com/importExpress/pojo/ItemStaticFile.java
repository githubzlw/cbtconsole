package com.importExpress.pojo;

import java.util.Date;

public class ItemStaticFile {
	
	private String itemStaticFile;
	
	private Date createTime;

	public String getItemStaticFile() {
		return itemStaticFile;
	}

	public void setItemStaticFile(String itemStaticFile) {
		this.itemStaticFile = itemStaticFile;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "ItemStaticFile [itemStaticFile=" + itemStaticFile + ", createTime=" + createTime + "]";
	}
	
}
