package com.importExpress.mapper;

import com.cbt.bean.UserBean;
import com.importExpress.pojo.TabCouponNew;
import com.importExpress.pojo.TabCouponRules;
import com.importExpress.pojo.TabCouponType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TabCouponMapper {

	List<TabCouponNew> queryTabCouponList(@Param("startBars") Integer startBars, @Param("rows") Integer rows,
                                          @Param("typeCode") String typeCode, @Param("valid") Integer valid,
                                          @Param("timeTo") Integer timeTo, @Param("couponSite") Integer couponSite);

	Long queryTabCouponListCount(@Param("typeCode") String typeCode, @Param("valid") Integer valid,
                                 @Param("timeTo") Integer timeTo, @Param("couponSite") Integer couponSite);

	List<TabCouponType> queryTabCouponTypeCodeList();

	List<TabCouponRules> queryTabCouponRulesList();

	void addCoupon(@Param("tabCouponNew") TabCouponNew tabCouponNew);

	Long checkCouponCode(@Param("couponCode") String couponCode);

    TabCouponNew queryTabCouponOne(@Param("couponCode") String couponCode);

    void insertCouponUsers(@Param("bean") TabCouponNew bean, @Param("list") List<UserBean> list);

    String queryTabCouponUser(@Param("couponCode") String couponCode);

    void updateCouponValid(@Param("couponCode") String couponCode, @Param("valid") int valid);

    String queryCouponCodeByCreatetime(@Param("createtime") String createtime);

    void updateCouponUsers(@Param("couponCode") String couponCode, @Param("userid") String userid);

    List<String> queryCouponUsersCount(@Param("couponCode") String couponCode);

    List<UserBean> queryLocalUser(@Param("list") List<String> list);

    long queryCouponCodeCount(@Param("couponCode") String couponCode);

    int addTabCouponNew(TabCouponNew tabCouponNew);
}
