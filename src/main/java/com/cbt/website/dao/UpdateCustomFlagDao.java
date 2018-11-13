package com.cbt.website.dao;

public interface UpdateCustomFlagDao {

	//发送邮件后更新状态
	int updateFlag(String emailaddress, String customId);

}
