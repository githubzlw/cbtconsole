package com.cbt.share.service;

import com.cbt.share.pojo.SharePojo;

import java.util.List;

public interface ShareService {
	/**
	 * 获得所有ip地址
	 * @return
	 */
	List<SharePojo> getAllIpAddress();
}
