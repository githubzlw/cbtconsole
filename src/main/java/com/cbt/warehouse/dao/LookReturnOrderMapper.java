package com.cbt.warehouse.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cbt.warehouse.pojo.Returndetails;
import com.cbt.warehouse.pojo.Returnresult;
import com.mysql.fabric.xmlrpc.base.Data;

public interface LookReturnOrderMapper {

	List<Returndetails> SelectReturnOrder(@Param("admuserid")String admuserid, @Param("returnState")String returnState,
			@Param("orderNum")String orderNum,@Param("page")int page,@Param("mid")String mid);
	@Update("UPDATE return_details SET returnState=#{returnState} where cusorder=#{tborder}")
   Boolean UpdataReturnOrder(@Param("tborder")String tborder,@Param("returnState")String returnState);
  @Insert("INSERT INTO return_details (`cusorder`, `purNum`, `tborder`, `purSou`, `waybill`, `purManey`, `returnOrder`, "
  		+ "`warehouse`, `returnNum`, `returnMoney`, `ordeerPeo`, `orderSale`, `returnApply`, `returnReason`, `placeDate`, `deliveryDate`,"
  		+ " `returnState`) VALUES (#{returndetails.cusorder}, #{returndetails.purNum}, #{returndetails.tborder}, #{returndetails.purSou}, #{returndetails.waybill},"
  		+ " #{returndetails.purManey}, #{returndetails.returnOrder}, #{returndetails.warehouse}, #{returndetails.returnNum}, #{returndetails.returnMoney}, #{returndetails.ordeerPeo}, "
  		+ "#{returndetails.orderSale}, #{returndetails.returnApply}, #{returndetails.returnReason},"
  		+ " #{returndetails.placeDate}, #{returndetails.deliveryDate}, #{returndetails.returnState})") 
Boolean AddOrder(@Param("returndetails")Returndetails returndetails);
@Select("SELECT orderid AS tborder, orderdate AS PlaceDate,seller AS purSou,SUM(totalprice) as purManey,SUM(itemqty) AS purNum,shipno AS Waybill,paydata AS deliveryDate,username AS ordeerPeo FROM taobao_1688_order_history where orderid=#{tborder} GROUP BY orderid;")  
Returndetails FindReturndetails(@Param("tborder")String tborder);

@Select("SELECT distinct tborderid AS tborder,username AS ordeerPeo,barcode AS warehouse  from id_relationtable where orderid=#{order} AND tborderid not in (SELECT tborder FROM return_details)")
List<Returnresult> FindCusOrder(@Param("order")String order);

@Select("SELECT admName as orderSale from admin_r_user where userid=(SELECT user_id from orderinfo where order_no=#{cusorder});")
Returndetails findOrderSale(@Param("cusorder")String cusorder);

@Delete("DELETE  FROM return_details WHERE cusorder=#{order}")
Boolean RemReturnOrder(String order);
@Select("SELECT * from return_details where cusorder=#{cusorder} ")
List<Returndetails> FindReturnOrder(@Param("cusorder")String cusorder);
@Select("SELECT COUNT(tborder) from return_details where tborder=#{tborder}")
int findTborder(@Param("tborder")String tborder);
@Update("UPDATE return_details SET returnState=#{returnState},returnOrder=#{returnOrder},company=#{company},returndate=#{daString} where tborder=#{order}")
Boolean UpdataReturnTbOrder(@Param("order")String order,@Param("returnState") String returnState,@Param("returnOrder") String returnOrder,@Param("company") String company,@Param("daString") String daString);

@Delete("DELETE  FROM return_details WHERE tborder=#{order}")
Boolean RemReturnTbOrder(@Param("order")String order);
int countPage(@Param("admuserid")String admuserid, @Param("state")String state,
			@Param("orderNum")String orderNum,@Param("page")int page,@Param("mid")String mid);

@Select("SELECT * from return_details where cusorder=#{cusorder} and returnState='退货申请'")
List<Returndetails> FindReturnOrderS(@Param("cusorder")String cusorder);

}
