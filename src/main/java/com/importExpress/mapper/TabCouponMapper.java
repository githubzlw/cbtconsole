package com.importExpress.mapper;

import com.importExpress.pojo.TabCouponNew;
import com.importExpress.pojo.TabCouponRules;
import com.importExpress.pojo.TabCouponType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TabCouponMapper {

	List<TabCouponNew> queryTabCouponList(@Param("startBars") Integer startBars, @Param("rows") Integer rows,
                                          @Param("typeCode") String typeCode, @Param("valid") Integer valid);

	Long queryTabCouponListCount(@Param("typeCode") String typeCode, @Param("valid") Integer valid);

	List<TabCouponType> queryTabCouponTypeCodeList();

	List<TabCouponRules> queryTabCouponRulesList();

	void addCoupon(@Param("tabCouponNew") TabCouponNew tabCouponNew);

	Long checkCouponCode(@Param("couponCode") String couponCode);

    TabCouponNew queryTabCouponOne(@Param("couponCode") String couponCode);

    void insertCouponUsers(@Param("bean") TabCouponNew bean, @Param("list") List<String> list);

    String queryTabCouponUser(@Param("couponCode") String couponCode);

    void updateCouponValid(@Param("couponCode") String couponCode, @Param("valid") int valid);

    String queryCouponCodeByCreatetime(@Param("createtime") String createtime);

    void updateCouponUsers(@Param("couponCode") String couponCode, @Param("userid") String userid);

    Long queryCouponUsersCount(@Param("couponCode") String couponCode);
}
