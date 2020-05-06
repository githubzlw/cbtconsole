package com.cbt.warehouse.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cbt.pojo.Admuser;
import com.cbt.warehouse.pojo.returnbill;
import com.cbt.warehouse.pojo.returndisplay;

public interface LookReturnOrderServiceNewMapper {

	int selectCount(@Param("nameString")String nameString, @Param("state")String state,
			@Param("a1688Shipno")String a1688Shipno, @Param("optTimeStart")String optTimeStart, @Param("optTimeEnd")String optTimeEnd,@Param("page")int page,@Param("userOther")String userOther,@Param("user")String user,@Param("a1688order")String a1688order,@Param("pid")String pid,@Param("skuid")String skuid);

	List<returndisplay> FindReturndisplay(@Param("nameString")String nameString, @Param("state")String state,
			@Param("a1688Shipno")String a1688Shipno, @Param("optTimeStart")String optTimeStart, @Param("optTimeEnd")String optTimeEnd,@Param("page")int page,@Param("userOther")String userOther,@Param("user")String user,@Param("a1688order")String a1688order,@Param("pid")String pid,@Param("skuid")String skuid);

	@Update("UPDATE return_display SET State=0,returntime=NOW(),actual_money=#{money},freight=#{freight} WHERE id=#{ship} ")
	Boolean UpdaeReturnOrder(@Param("ship")String ship,@Param("money") double money,@Param("freight")double freight);

	@Delete("UPDATE return_display set reason=#{cusorder},return_number=0,State=5,returntime=NOW() where id=#{ship}")
	Boolean RemReturnOrder(@Param("ship")String ship,@Param("cusorder") String cusorder);

	@Update("UPDATE return_display set end_time=curtime(),State=4,differences=#{number} where shipno=#{ship}")
	Boolean SetReturnOrder(@Param("ship")String ship,@Param("number")String number);
    @Select("SELECT id AS appyId,opt_user AS optUser,opt_time as optTime FROM return_display where id=#{ship}")
	returnbill FindOrdetByship(@Param("ship")String ship);
//    @Insert("INSERT INTO `return_bill` (`appy_id`, `chang_amount`, `opt_user`, `opt_time`, `end_time`) VALUES (#{re.appyId}, #{re.changAmount}, #{re.optUser}, #{re.optTime}, #{re.endTime});")
//	Boolean AddMoney(@Param("re")returnbill re);
   
    @Update("UPDATE return_display SET shipno=#{number},opt_user=#{name},opt_time=curtime() where id=#{ship}")
	Boolean UpdateReturnOrder(@Param("ship")int ship,@Param("number")String number,@Param("name")String name);
    @Update("UPDATE return_display SET change_shipno=#{number},opt_user=#{name},opt_time=curtime() where id=#{ship}")
	Boolean UpdateReturnOrderH(@Param("ship")int ship,@Param("number")String number,@Param("name")String name);
    
    @Select("SELECT orderid as a1688Order,orderdate as placeDate,delivery_date as signtime,seller as sellerpeo,itemid as item,itemqty as itemNumber,shipno as a1688Shipno,username as optUser,id as tbId, sku FROM taobao_1688_order_history "
    		+ " WHERE odid=#{orid}")
	returndisplay FindReturndisplayByOrid(@Param("orid")int orid);

	@Select("SELECT tlh.orderid as a1688Order,tlh.orderdate as placeDate,tlh.delivery_date as signtime,tlh.seller as sellerpeo,tlh.itemid as item,tlh.itemqty as itemNumber,tlh.shipno as a1688Shipno," +
			"tlh.username as optUser,tlh.id as tbId, tlh.sku FROM id_relationtable as ir INNER JOIN taobao_1688_order_history as tlh on ir.tborderid=tlh.orderid WHERE " +
			"ir.orderid=#{cusorder} and ir.itemid=#{pid} LIMIT 1")
	returndisplay FindReturndisplayBypid(@Param("cusorder")String orid,@Param("pid")String pid);
    
    @Insert("INSERT INTO `return_display` (`customer_info`, `1688_order`, `1688_shipno`, `item`, `item_number`, `apply_user`, `apply_time`,`opt_user`,`State`,`return_reason`, `barcode`, `return_number`, `tb_id`, `sku`,`returntime`,`actual_money`) VALUES (#{re.customerorder}, #{re.a1688Order}, #{re.a1688Shipno}, #{re.item}, #{re.itemNumber}, "
    		+ "#{re.applyUser}, #{re.applyTime},#{re.optUser},#{re.State}, #{re.returnReason}, #{re.barcode}, #{re.returnNumber},#{re.tbId},#{re.sku},#{re.returntime},#{re.actual_money});")
	Boolean AddOrder(@Param("re")returndisplay re);

	@Select("SELECT distinct orderid as a1688Order,orderdate as placeDate,itemid as item,seller as sellerpeo,SUM(itemqty) as itemNumber,shipno as a1688Shipno,username as optUser,delivery_date as signtime "
			+ "from taobao_1688_order_history WHERE orderid in(SELECT tborderid FROM id_relationtable WHERE orderid=#{tborid}) group by itemid;")
    List<returndisplay> LookOrder(@Param("tborid")String tborid);
    @Select("SELECT DISTINCT tborderid as a1688Order,orderid AS customerorder FROM id_relationtable WHERE orderid=#{cusOrder}")
	List<returndisplay> getAllOrder(@Param("cusOrder")String cusOrder);

	@Select("SELECT orderid as a1688Order ,orderdate as placeDate,seller as sellerpeo,itemid as item,itemqty as itemNumber,shipno as a1688Shipno,username as optUser,"
			+ "delivery_date as signtime,sku,id AS tbId from taobao_1688_order_history WHERE orderid=#{a1688Order};")
	List<returndisplay> getAllItem(@Param("a1688Order")String a1688Order);
    
	@Select("SELECT orderid as a1688Order ,orderdate as placeDate,seller as sellerpeo,itemid as item,itemqty as itemNumber,shipno as a1688Shipno,username as optUser,"
			+ "delivery_date as signtime,sku,id AS tbId from taobao_1688_order_history WHERE odid=#{orid};")
	List<returndisplay> getAllOrderByitem(@Param("orid")String orid);

	@Select("SELECT admName as admname from admuser WHERE id=#{applyUser}")
	Admuser findUserName(@Param("applyUser")String applyUser);
    @Select("SELECT odid as item FROM id_relationtable WHERE orderid=#{cusorder}")
	List<returndisplay> FindOdid(@Param("cusorder")String cusorder);
	@Update("UPDATE return_display SET state=1 WHERE id=#{ship} and state=0")
	void UpdaeReturnOrderBy1(int ship);
    @Select("SELECT SUM(return_number)AS returnNumber,item_number as itemNumber FROM return_display WHERE tb_id=#{tbId}")
    returndisplay Finditnum(@Param("tbId")String tbId);
    @Select("SELECT yourorder AS itemNumber FROM order_details WHERE id=#{orid}")
	returndisplay FindOrderdetailsByOrid(@Param("orid")int orid);

    @Select("SELECT SUM(return_number)AS returnNumber,item_number as itemNumber FROM return_display WHERE customer_info=#{cusorder} AND item=#{pid}")
	returndisplay FinditnumByPid(@Param("cusorder")String orid,@Param("pid")String pid);

	@Select("SELECT tlh.orderid as a1688Order,tlh.orderdate as placeDate,tlh.delivery_date as signtime,tlh.seller as sellerpeo,tlh.itemid as item,tlh.itemqty as itemNumber,tlh.shipno as a1688Shipno," +
			"tlh.username as optUser,tlh.id as tbId, tlh.sku FROM id_relationtable as ir INNER JOIN taobao_1688_order_history as tlh on ir.tborderid=tlh.orderid WHERE " +
			"ir.orderid=#{cusorder} and ir.itemid=#{pid}")
	List<returndisplay> FindReturndisplayByCusorder(@Param("cusorder")String orid,@Param("pid")String pid);
   @Select("SELECT COUNT(1) FROM return_display WHERE state=-1")
    int getAllOrderCount();
    @Select("SELECT sum(return_number)as returnNumber ,item_number as itemNumber from return_display WHERE 1688_order=#{a1688Order} GROUP BY 1688_order")
	returndisplay FindItemCount(@Param("a1688Order") String a1688Order);
	@Update("UPDATE return_display SET State=3,returntime=NOW(),reason=#{ch} WHERE id=#{ship} ")
	Boolean UpdaeReturnOrderCh(@Param("ship") String ship,@Param("ch") String ch);
	@Select("SELECT b.orderid as customerorder,a.orderid as a1688Order ,a.orderdate as placeDate,a.seller as sellerpeo,a.itemid as item,a.itemqty as itemNumber,a.shipno as a1688Shipno,a.username as optUser," +
			"a.delivery_date as signtime,a.sku,a.id AS tbId from taobao_1688_order_history a,id_relationtable b WHERE a.orderid=b.tborderid AND  a.shipno=#{shipno} GROUP BY a.id;")
    List<returndisplay> getOrderByship(@Param("shipno") String shipno);
   @Update("UPDATE return_display set actual_money=#{number} where shipno=#{ship}")
	Boolean SetUpMoney(@Param("ship") String ship, @Param("number") double number);
    @Select("SELECT a.orderid as a1688Order ,a.orderdate as placeDate,a.seller as sellerpeo,itemname,imgurl,a.itemid as item,a.itemqty as itemNumber,a.shipno as a1688Shipno,a.username as optUser," +
			"a.delivery_date as signtime,a.sku,a.id AS tbId from taobao_1688_order_history a WHERE a.orderid=#{tborder}")
	List<returndisplay> FindAllByTborder(@Param("tborder") String tborder);

	@Select("SELECT DISTINCT b.orderid AS customerorder,a.orderid as a1688Order ,a.orderdate as placeDate,a.seller as sellerpeo,a.itemid as item,a.itemqty as itemNumber,a.shipno as a1688Shipno,a.username as optUser," +
			" a.delivery_date as signtime,sku,a.id AS tbId from taobao_1688_order_history a LEFT JOIN id_relationtable b ON a.orderid=b.tborderid and a.skuID=b.skuid WHERE a.id=#{tbId} ;")
	returndisplay FindOrderByTbid(@Param("tbId") String tbId);
    @Select("SELECT SUM(Return_number) from return_display WHERE tb_id=#{tbId}")
    int FindRetuByTbid(@Param("tbId") String tbId);
    @Insert("INSERT INTO Only_refund (`1688_order`, `total_amount`, `Refund_people`, `refund_date`) VALUES (#{orid}, #{odmany}, #{admName}, now());")
    void AddOnlyRefund(@Param("orid") String orid, @Param("odmany") Double odmany, @Param("admName") String admName);
    @Select("SELECT COUNT(1) FROM only_refund WHERE 1688_order=#{orid}")
	int findOrder(@Param("orid") String orid);

	List<returndisplay> Lookstatement(@Param("nameString") String nameString, @Param("optTimeStart") String optTimeStart, @Param("optTimeEnd") String optTimeEnd, @Param("page") int page, @Param("applyUser") String applyUser);

	int LookstatementCount(@Param("nameString") String nameString, @Param("optTimeStart") String optTimeStart, @Param("optTimeEnd") String optTimeEnd, @Param("page") int page, @Param("applyUser") String applyUser);

	List<returndisplay> LookstatementOnly(@Param("nameString") String nameString, @Param("optTimeStart") String optTimeStart, @Param("optTimeEnd") String optTimeEnd, @Param("page") int page, @Param("applyUser") String applyUser);

	int LookstatementOnlyCount(@Param("nameString") String nameString, @Param("optTimeStart") String optTimeStart, @Param("optTimeEnd") String optTimeEnd, @Param("page") int page, @Param("applyUser") String applyUser);

	double LookstatementManey(@Param("nameString") String nameString, @Param("optTimeStart") String optTimeStart, @Param("optTimeEnd") String optTimeEnd, @Param("page") int page, @Param("applyUser") String applyUser);
}
