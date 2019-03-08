<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.cbt.util.*"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String admuserJson = Redis.hget(request.getSession().getId(),
			"admuser");
	Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,
			Admuser.class);
	int role = Integer.parseInt(adm.getRoletype());
%>
<!DOCTYPE html>
<html lang="en">
<head>
 <style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
    <title>退货申请</title>
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script type="text/javascript">
var updateSourcesUrl = "/cbtconsole/StatisticalReport/updateSources"; //盘点库存
</script>
<style type="text/css">
.displaynone{display:none;}
.item_box{display:inline-block;margin-right:52px;}
.item_box select{width:150px;}
.w-group{margin-bottom: 10px;width: 60%;text-align: center;}
.w-label{float:left;}
.w-div{margin-left:120px;}
.w-remark{width:100%;}
table.imagetable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #999999;
	border-collapse: collapse;
}
.mod_pay3 {
	width: 400px;
	height:400px;
	position: fixed;
	top: 100px;
	left: 15%;
	margin-left:400px;
	z-index: 1011;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 1011;
}
table.imagetable th {
	background:#b5cfd2 url('cell-blue.jpg');
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #999999;
}
.panel-title {
	text-align: center;
	height: 30px;
	font-size: 24px;
}
table.imagetable td {
/* 	background:#dcddc0 url('cell-grey.jpg'); */
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #999999;
	word-break: break-all;
}
.displaynone{display:none;}
.but_color {
	background: #44a823;
	width: 80px;
	height: 24px;
	border: 1px #aaa solid;
	color: #fff;
}

</style>
<script type="text/javascript">
function Single(tborder,warehouse,ordeerPeo){
	var returnApply=this.$('#returnApply').val();
	var returnReason=this.$('#returnReason option:selected').val();
	var returnMoney=this.$('#returnMoney').val();
	if(returnApply==""||returnReason=="1"||returnMoney==""){
		alert("请完善信息");
		
	}
	
	 window.location.href ="/cbtconsole/AddReturnOrder/AddtbOrder/${rets.cusorder }"+"/"+returnApply+"/"+returnReason+"/"+returnMoney+"/"+tborder+"/"+warehouse+"/"+ordeerPeo+"/"+${admuserid };

}
</script>
</script>
</head>
<body>
<div id="app">
	    <span><h1 align="center" style="color:red ">${addString }</h1></span>
		<div id="top_toolbar" style="padding: 5px; height: auto">
		<input v-model="se"/><button @click="jrUpdata(se)">退货申请</button>
		<a href="/cbtconsole/AddReturnOrder/LookReturnOrder?state=退货申请"  class="easyui-linkbutton" >申请退货列表</a>
	</div>
    <table class="easyui-datagrid" id="easyui-datagrid"   style="width:100%;height:900px">
     
		<thead >	
		<tr>
		<th data-options="field:'Warehouse',width:100,align:'center'">客户订单：</th>
				<th data-options="field:'cusorder',width:120,align:'center'">${rets.cusorder }</th>
				
		</tr>
			<tr>
				<th data-options="field:'Warehouse',width:100,align:'center'">仓库编号</th>
				<th data-options="field:'cusorder',width:100,align:'center'">客户订单号</th>
				<th data-options="field:'purSou',width:100,align:'center'">采购来源</th>
				<th data-options="field:'purManey',width:100,align:'center'">采购订单总金额</th>
				<th data-options="field:'purNum',width:100,align:'center'">订单商品数量</th>
				<th data-options="field:'returnApply',width:100,align:'center'">申请人</th>
				<th data-options="field:'ordeerPeo',width:100,align:'center'">采购</th>
				<th data-options="field:'PlaceDate',width:100,align:'center'">下单时间</th>
				<th data-options="field:'paydata',width:100,align:'center'">淘宝订单号</th>
				<th data-options="field:'deliveryDate',width:100,align:'center'">发货时间</th>
				<th data-options="field:'returnState',width:100,align:'center'">订单状态</th>
				<th data-options="field:'Waybill',width:100,align:'center'">采购运单号</th>
				<th data-options="field:'returnReason',width:100,align:'center'">退货备注</th>
				
				<th data-options="field:'returnMoney',width:100,align:'center'">退货金额</th>

				<th data-options="field:'operating',width:100,align:'center'">退货状态操作</th>
			</tr>
			<c:forEach items="${rets.list }" var="u" varStatus="i">
			<tr>
            <th data-options="field:'Warehouse',width:100,align:'center'">${u.warehouse }</th>
				<th data-options="field:'cusorder',width:100,align:'center'"><a href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo=${u.cusorder }" target='_blank'>${u.cusorder }</th>
				<th data-options="field:'purSou',width:100,align:'center'">${u.purSou }</th>
				<th data-options="field:'purManey',width:100,align:'center'">${u.purManey }</th>
				<th data-options="field:'purNum',width:100,align:'center'">${u.purNum }</th>
				<th data-options="field:'returnApply ',width:100,align:'center'"><input id="returnApply"  name="returnApply"  value=""/></th>
				<th data-options="field:'ordeerPeo',width:100,align:'center'">${u.ordeerPeo }</th>
				<th data-options="field:'PlaceDate',width:100,align:'center'">${u.placeDate }</th>
				<th data-options="field:'paydata',width:100,align:'center'"><a href="https://trade.1688.com/order/new_step_order_detail.htm?orderId=${u.tborder }" target='_blank'>${u.tborder }</a></th>
				<th data-options="field:'deliveryDate',width:100,align:'center'">${u.deliveryDate }</th>
				<th data-options="field:'returnState',width:100,align:'center'">退货申请</th>
				<th data-options="field:'Waybill',width:100,align:'center'"><a href="/cbtconsole/website/newtrack.jsp?shipno=${u.waybill }&barcode=${u.warehouse }" target='_blank'>${u.waybill }</th>
				<th data-options="field:'returnReason',width:100,align:'center'"><select id="returnReason" name="returnReason">
							<option value="1">请选择退货原因</option>
							<option value="2" >次品退货</option>							
							<option value="3">客户退单</option>
					</select></th>
			
				<th data-options="field:'returnMoney',width:100,align:'center'"><input id="returnMoney" name="returnMoney"  value=""/></th>
				
				<th data-options="field:'operating',width:100,align:'center'"><input type="button" value="单个退货申请" onclick="Single('${u.tborder }','${u.warehouse }','${u.ordeerPeo }')"></th>
			</tr>
			
			</c:forEach>
		</thead>
	</table>
</div>
<script src="./js/vue/vue.js"></script>
<script src="/cbtconsole/js/axios.min.js"></script>
<script src="/cbtconsole/js/common.js"></script>
<script src="/cbtconsole/js/vuetify.js"></script>
<script src="/cbtconsole/js/vue/vue.min.js"></script>
<script type="text/javascript">
var vue=new Vue({
	el:"#app",
	data:{
		List:[],
		se:"请输入定单号",
		orderNum:""
	
	},
	 methods: {      //这里使用methods
		 jrUpdata: function (se) {
			 if(se==""||se=="请输入定单号"){
				 alert("请输入定单号")
				 return;
			 }
			window.location.href ="/cbtconsole/AddReturnOrder/FindReturnOrder/"+this.se;    
         },
         		
     }
})
</script>
</body>
</html>