package com.cbt.share.dao;

import com.cbt.share.pojo.SharePojo;

import java.util.List;

public interface ShareDao {
	/**
	 * 获得所有ip地址
	 * @return
	 */
	List<SharePojo> getAllIpAddress();
}
