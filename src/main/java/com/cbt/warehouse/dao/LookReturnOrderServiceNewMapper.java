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
			@Param("a1688Shipno")String a1688Shipno, @Param("optTimeStart")String optTimeStart, @Param("optTimeEnd")String optTimeEnd,@Param("page")int page);

	List<returndisplay> FindReturndisplay(@Param("nameString")String nameString, @Param("state")String state,
			@Param("a1688Shipno")String a1688Shipno, @Param("optTimeStart")String optTimeStart, @Param("optTimeEnd")String optTimeEnd,@Param("page")int page);

	@Update("UPDATE return_display SET State=0,returntime=NOW() WHERE id=#{ship} ")
	Boolean UpdaeReturnOrder(@Param("ship")String ship);

	@Delete("UPDATE return_display set reason=#{cusorder}, State=4,returntime=NOW() where id=#{ship}")
	Boolean RemReturnOrder(@Param("ship")String ship,@Param("cusorder") String cusorder);

	@Update("UPDATE return_display set State=4,end_time=curtime() where id=#{ship}")
	Boolean SetReturnOrder(@Param("ship")String ship);
    @Select("SELECT id AS appyId,opt_user AS optUser,opt_time as optTime FROM return_display where id=#{ship}")
	returnbill FindOrdetByship(@Param("ship")String ship);
    @Insert("INSERT INTO `return_bill` (`appy_id`, `chang_amount`, `opt_user`, `opt_time`, `end_time`) VALUES (#{re.appyId}, #{re.changAmount}, #{re.optUser}, #{re.optTime}, #{re.endTime});")
	Boolean AddMoney(@Param("re")returnbill re);
   
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
    
    @Insert("INSERT INTO `return_display` (`customer_info`, `1688_order`, `1688_shipno`, `item`, `item_number`, `apply_user`, `apply_time`,`opt_user`,`State`,`return_reason`, `barcode`, `return_number`, `tb_id`, `sku`) VALUES (#{re.customerorder}, #{re.a1688Order}, #{re.a1688Shipno}, #{re.item}, #{re.itemNumber}, "
    		+ "#{re.applyUser}, #{re.applyTime},#{re.optUser},#{re.State}, #{re.returnReason}, #{re.barcode}, #{re.returnNumber},#{re.tbId},#{re.sku});")
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
    @Select("SELECT COUNT(return_number)as returnNumber ,item_number as itemNumber from return_display WHERE 1688_order=#{a1688Order} GROUP BY 1688_order")
	returndisplay FindItemCount(@Param("a1688Order") String a1688Order);
	@Update("UPDATE return_display SET State=3,returntime=NOW(),reason=#{ch} WHERE id=#{ship} ")
	Boolean UpdaeReturnOrderCh(@Param("ship") String ship,@Param("ch") String ch);

}
