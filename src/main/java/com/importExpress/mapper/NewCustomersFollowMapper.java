package com.importExpress.mapper;

import com.cbt.website.bean.ConfirmUserInfo;
import com.importExpress.pojo.ShopCarUserStatistic;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface NewCustomersFollowMapper {

    List<ShopCarUserStatistic> FindCustomList(ShopCarUserStatistic statistic);

    int FindCustomCount(ShopCarUserStatistic statistic);
    @Select("SELECT COUNT(pid) totalCatid, pid as shippingName,createTime,uid as userId,img_url as lastLoginTime,pname as followAdminName from recent_view WHERE uid=#{userId} GROUP BY pid LIMIT #{page},10")
    List<ShopCarUserStatistic> queryNewCustomByUserId(@Param("userId") int userId,@Param("page") int page);
    @Select("SELECT COUNT(DISTINCT pid) from recent_view WHERE uid=#{userId}")
    int queryNewCustomByUserIdCount(@Param("userId") int userId);
    @Select("select valid from custom_benchmark_ready where pid=#{shippingName}")
    int FindValidByPid(@Param("shippingName") String shippingName);
    @Select("SELECT id,admName as confirmusername,roleType as role FROM admuser  WHERE roleType in(3,4) and status = 1")
    List<ConfirmUserInfo> queryAllSale();

}
