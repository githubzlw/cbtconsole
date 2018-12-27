package com.importExpress.service;

import com.importExpress.pojo.CouponRedisBean;
import com.importExpress.pojo.TabCouponNew;
import com.importExpress.pojo.TabCouponRules;
import com.importExpress.pojo.TabCouponType;

import java.util.List;
import java.util.Map;

public interface TabCouponService {

	Map<String, Object> queryTabCouponList(Integer page, Integer rows, String typeCode);

	List<TabCouponType> queryTabCouponTypeCodeList();

	List<TabCouponRules> queryTabCouponRulesList();

	void addCoupon(CouponRedisBean couponRedis, TabCouponNew tabCouponNew) throws Exception;

	Boolean checkCouponCode(String couponCode);

    TabCouponNew queryTabCouponOne(String couponCode);

    Map<String, String> addCouponUser(String couponCode, List<String> useridList);
}
