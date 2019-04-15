package com.cbt.warehouse.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cbt.bean.Orderinfo;
import com.cbt.pojo.Admuser;
import com.cbt.warehouse.pojo.OldCustom;

public interface OldCustomShowMapper {
//    @Select("SELECT user_id,order_no,delivered_time,username,createdate,useremail,admName,senddate "
//+" FROM (SELECT o.user_id, o.order_no,ti.delivered_time,us.username,us.createdate,us.useremail,us.admName,us.adminid FROM shipping_package sp "
//+" INNER JOIN cross_border_shop.tab_track_info ti ON sp.transportcompany = ti.track_company AND sp.expressno = ti.track_no "
//+" INNER JOIN orderinfo o "
//+" INNER JOIN admin_r_user us on o.user_id=us.userid "
//+" WHERE sp.orderid =o.order_no AND ti.track_state = 3) s "
//+" LEFT JOIN (SELECT cid,senddate FROM(SELECT cid,senddate FROM nemail.email_info WHERE cid !=0 ORDER BY senddate DESC)s GROUP BY cid) y ON s.user_id=y.cid  where 1=1")
	List<OldCustom> FindOldCustoms(@Param("admName")String admName, @Param("staTime")String staTime,
			@Param("enTime")String enTime, @Param("email")String email, @Param("id")String id, @Param("cuName")String cuName, @Param("page")int page,
			@Param("pagesize")int pagesize);

	int getOldCustomCount(@Param("admName")String admName, @Param("staTime")String staTime, @Param("enTime")String enTime,
			@Param("email")String email, @Param("id")String id, @Param("cuName")String cuName);
    @Select("SELECT id,admName FROM admuser WHERE roleType=3 OR roleType=4 ")
	List<Admuser> FindAllAdm();
    
	List<Orderinfo> FindOrderByUsid(@Param("usid")String usid,  @Param("start")int start,  @Param("pagesize")int pagesize,  @Param("order")String order);
   
	int getOrderCount(@Param("usid")String usid, @Param("order")String order);

}
