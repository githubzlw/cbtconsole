package com.importExpress.service;

import com.cbt.website.userAuth.bean.AuthInfo;

import java.util.List;
import java.util.Map;


public interface QueryUserService {

	List<String> queryUser();

	Map<String, Object> queryMarkedList(Integer rows, String orderby);

	void updateMarkedById(Long id, Integer marked);

	Map<String, Object> createStandardGoodsForm();

	Map<String, Object> queryStandardGoodsFormCreatetime();

	Map<String, Object> queryStandardGoodsFormList(Integer page, Integer rows, Integer flag, Integer bmFlag, Integer valid);

	List<String> queryUserByPrice(Integer price);

	String createStaticizeForm(Integer flag);

	AuthInfo queryAuthInfo(Integer authId);


}
