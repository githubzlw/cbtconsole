package com.cbt.warehouse.service;

import java.util.List;

import com.cbt.bean.Orderinfo;
import com.cbt.pojo.Admuser;
import com.cbt.warehouse.pojo.OldCustom;


public interface OldCustomShowService {

	List<OldCustom> FindOldCustoms(String admName, String staTime,
			String enTime, String email, String id, String cuName, int page,
			int pagesize);

	int getOldCustomCount(String admName, String staTime, String enTime,
			String email, String id, String cuName);

	List<Admuser> FindAllAdm();

	List<Orderinfo> FindOrderByUsid(String usid, int start, int pagesize,String order);

	int getOrderCount(String usid,String order);



}
