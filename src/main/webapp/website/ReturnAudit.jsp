
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.cbt.util.*"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"
	type="text/javascript"></script>
<title>退货申请</title>
</head>
<style type="text/css">
.form_m {
	text-align: center; /*è®©divåé¨æå­å±ä¸­*/
}
</style>

<script type="text/javascript">
function addRsd(){
	var orderid=$("#t_orderid").val();
	var shipno= $.trim(document.getElementById("shipno").value);
	var remark= $.trim(document.getElementById("remark_").value);
	if(shipno == null || shipno == ""){
		topCenter("请输入订单号");
		return;
	}
	 var params = {"t_orderid":t_orderid,"shipno":shipno,"remark":remark};
 	 $.ajax({  
         url:'/cbtconsole/warehouse/refundOrderShipnoEntry',  
         type:"post",  
         data:params,  
         success:function(data){
	       	if(Number(data)>0){
	       		$('#easyui-datagrid').datagrid('reload');
	       		cance1();
	 	  	}else{
	 	  		topCenter("申请退货失败");
	 	  	}
         }, 
     }); 
}
</script>
<body>
	<div class=".form_m" border="1"
		style="width: 60%; height: 60%; margin: auto">
		<h1 align="center">退货申请</h1>
		<form action="/cbtconsole/AddReturnOrder/AddOrder" method="post">
			<table cellpadding="3" cellspacing="0"
				style="width: 60%; margin: auto">
				<tr>
					<td>客户订单</td>
					<td><input name="cusorder" value="${rets.[0].cusorder }"/></td>
					<td>采购订单数量</td>
					<td><input name="purNum" value="${ret.purNum }"/></td>
				</tr>
				<tr>
					<td>采购订单号</td>
					<td><input name="tborder" value="${ret.tborder }" /></td>
					<td>采购来源</td>
					<td><input name="purSou" value="${ret.purSou }" /></td>
				</tr>
				 <tr>
					<td>采购运单号</td>
					<td><input name="Waybill" value=""/></td>
					<td>采购金额</td>
					<td><input name="purManey" value="${ret.purManey }" /></td>
				</tr> 
				<tr>
					<td>下单时间</td>
					<td><input name="PlaceDate" value="" /></td> 
					<td>发货时间</td>
					<td><input name="deliveryDate" value="${ret.deliveryDate }" /></td>
				</tr>
				<tr>
					<td>退货运单号</td>
					<td><input name="returnOrder" /></td>
					<td>所在仓库</td>
					<td><input name="Warehouse" /></td>
				</tr>
				<tr>
					<td>退货数量</td>
					<td><input name="returnNum" /></td>
					<td>退货金额</td>
					<td><input name="returnMoney" /></td>
				</tr>
				<tr>
					<td>订单采购人</td>
					<td><input name="ordeerPeo" value="${ret.ordeerPeo }" /></td>
					<td>订单关联销售</td>
					<td><input name="orderSale" value="${ret.orderSale }"/></td>
				</tr>
				<tr>
					<td>退货申请人</td>
					<td><input name="returnApply" /></td>
					<td>退货原因</td>
					<td><select name="returnReason">
							<option value="0" selected>全部</option>
							<option value="1" selected>破损退货</option>
							<option value="2">客户退单</option>
					</select></td>
				</tr>

			</table>
			<div style="text-align: right;">
				<input align="right" type="submit" value="确认提交" />
			</div>
		</form>
	</div>
</body>
</html>