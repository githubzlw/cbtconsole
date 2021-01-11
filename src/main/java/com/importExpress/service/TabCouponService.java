package com.importExpress.service;

import com.importExpress.pojo.CouponRedisBean;
import com.importExpress.pojo.TabCouponNew;
import com.importExpress.pojo.TabCouponRules;
import com.importExpress.pojo.TabCouponType;

import java.util.List;
import java.util.Map;

public interface TabCouponService {

	Map<String, Object> queryTabCouponList(Integer page, Integer rows, String typeCode, Integer valid, Integer timeTo, Integer couponSite);

	List<TabCouponType> queryTabCouponTypeCodeList();

	List<TabCouponRules> queryTabCouponRulesList();

    Map<String, String> addCoupon(CouponRedisBean couponRedis, TabCouponNew tabCouponNew, List<String> useridList) throws Exception;

	Boolean checkCouponCode(String couponCode);

    TabCouponNew queryTabCouponOne(String couponCode);

    Map<String, String> addCouponUser(String couponCode, List<String> useridList, Integer websiteType);

    Map<String, String> delCoupon(String couponCode);

    String querycouponcode();

    Map<String, String> delCouponUser(String couponCode, String userid);

    int SendGuestbook(int id, String replyContent, String date, String name, String qustion, String pname, String email, int parseInt, String purl, String sale_email, String picPath, Integer websiteType);

    List<String> getCouponCode(String url, String param);

    int addTabCouponNew(TabCouponNew tabCouponNew);
}
