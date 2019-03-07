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
    <title>${ste==null?"退货管理系统":ste }</title>
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
function jump(){
	var returnApply=this.$(" #jump").val();
	if(!(/^[0-9]+$/.test($('#jump').val()))){
		alert("请输入正确的页码");
		return;
	}
	if(returnApply<1){
		returnApply=1;	
	}
	if(returnApply>${totalpage }){
		returnApply=${totalpage };	
	}
	 window.location.href ="/cbtconsole/AddReturnOrder/LookReturnOrder?page="+returnApply;
}

</script>
</script>
</head>
<body>
<div id="app">
	    
		<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="/cbtconsole/AddReturnOrder/LookReturnOrder?"  style="margin-left:500px;">
				<select class="easyui-combobox" name="admuserid" id="admuserid" style="width:15%;" data-options="label:'电商采购人:',panelHeight:'auto',valueField: 'id',   
                    textField: 'admName', value:'',selected:true,
                    url: '/cbtconsole/warehouse/getAllBuyer',  
                    method:'get'">
				</select>
				<select class="easyui-combobox" name="state" id="state" style="width:15%;" data-options="label:'超期状态:',panelHeight:'auto'">
				<option value="" selected>全部</option>
				<option value="退货待发出">退货待发出</option>
				<option value="退货已发出">退货已发出</option>
				<option value="已退货未退款">已退货未退款</option>
				<option value="退货成功">退货成功</option>
				</select>
				订单号：<input  name="orderNum"  v-model="orderNum"/>
				 <input class="but_color" type="submit" value="查询" > 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<input v-model="se"/><button @click="jrUpdata(se)">退货申请</button>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<a href="/cbtconsole/AddReturnOrder/LookReturnOrder?state=退货申请&&page=1"  class="easyui-linkbutton" >申请退货列表 </a>
	</div>
	
    <table class="easyui-datagrid" id="easyui-datagrid"   style="width:100%;height:800px">
		<thead >	
			<div align="right">当前是${page }页 &nbsp;&nbsp;总页数${totalpage }&nbsp;&nbsp;总条数${total }&nbsp;&nbsp;<a href="/cbtconsole/AddReturnOrder/LookReturnOrder?page=${page==1?page:page-1 }">上一页</a>&nbsp;&nbsp;
			跳转到<input id="jump" value="${page }" >&nbsp;&nbsp;<button onclick="jump()">确认</button>&nbsp;&nbsp;<a href="/cbtconsole/AddReturnOrder/LookReturnOrder?page=${page==totalpage?page:page+1 }">下一页</a></div>
			<c:forEach items="${rets }" var="f" varStatus="j">
			
			<tr ><th >客户订单号：</th><th >${f.cusorder }</th></tr>
			<tr>
			    <th data-options="field:'cusorder',width:100,align:'center'">客户订单号</th>
				<th data-options="field:'Warehouse',width:100,align:'center'">仓库编号</th>
				
				<th data-options="field:'purSou',width:100,align:'center'">采购来源</th>
				<th data-options="field:'purManey',width:100,align:'center'">采购订单总金额</th>
				<th data-options="field:'purNum',width:100,align:'center'">订单商品数量</th>
				<th data-options="field:'returnApply ',width:100,align:'center'">退货发起人</th>
				<th data-options="field:'ordeerPeo',width:100,align:'center'">采购</th>
				<th data-options="field:'PlaceDate',width:100,align:'center'">下单时间</th>
				<th data-options="field:'paydata',width:100,align:'center'">淘宝订单号</th>
				<th data-options="field:'deliveryDate',width:100,align:'center'">发货时间</th>
				<th data-options="field:'returnState',width:100,align:'center'">订单状态</th>
				<th data-options="field:'Waybill',width:100,align:'center'">采购运单号</th>
				<th data-options="field:'returnReason',width:100,align:'center'">退货备注</th>
				<th data-options="field:'returndate',width:100,align:'center'">退货时间</th>
				<th data-options="field:'returnMoney',width:100,align:'center'">退货金额</th>
				<th data-options="field:'returnOrder ',width:100,align:'center'">退货运单号</th>
				<th data-options="field:'company',width:100,align:'center'">快递公司</th>
				<th data-options="field:'operating',width:100,align:'center'">退货状态操作</th>
			</tr>
			<c:forEach items="${f.list }" var="u" varStatus="i">
			<tr>
				<th data-options="field:'cusorder',width:100,align:'center'"><a href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo=${u.cusorder }" target='_blank'>${u.cusorder }</a></th>
				<th data-options="field:'Warehouse',width:100,align:'center'">${u.warehouse }</th>
				<th data-options="field:'purSou',width:100,align:'center'">${u.purSou }</th>
				<th data-options="field:'purManey',width:100,align:'center'">${u.purManey }</th>
				<th data-options="field:'purNum',width:100,align:'center'">${u.purNum }</th>
				<th data-options="field:'returnApply ',width:100,align:'center'">${u.returnApply }</th>
				<th data-options="field:'ordeerPeo',width:100,align:'center'">${u.ordeerPeo }</th>
				<th data-options="field:'PlaceDate',width:100,align:'center'">${u.placeDate }</th>
				<th data-options="field:'paydata',width:100,align:'center'"><a href="https://trade.1688.com/order/new_step_order_detail.htm?orderId=${u.tborder }" target='_blank'>${u.tborder }</a></th>
				<th data-options="field:'deliveryDate',width:100,align:'center'">${u.deliveryDate }</th>
				<th data-options="field:'returnState',width:100,align:'center'">${u.returnState }</th>
				<th data-options="field:'Waybill',width:100,align:'center'"><a href="/cbtconsole/website/newtrack.jsp?shipno=${u.waybill }&barcode=${u.warehouse }" target='_blank'>${u.waybill }</a></th>
				<th data-options="field:'returnReason',width:100,align:'center'">${u.returnReason }</th>
				<th data-options="field:'returndate',width:100,align:'center'">${u.returndate }</th>
				<th data-options="field:'returnMoney',width:100,align:'center'">退货金额</th>
				<th data-options="field:'returnOrder ',width:100,align:'center'">${u.returnOrder }</th>
				<th data-options="field:'company',width:100,align:'center'">快递公司</th>
				<th data-options="field:'operating',width:100,align:'center'"><a href="/cbtconsole/AddReturnOrder/UpdataReturnOrder/${u.tborder }/${u.returnState }">${state=="退货申请"?"确认发起退货":"修改退货订单" }</a></th>
			</tr>
			</c:forEach>
			
			</c:forEach>
			
		</thead>
		
	</table>
	<div align="right">当前是${page }页 &nbsp;&nbsp;总页数${totalpage }&nbsp;&nbsp;总条数${total }&nbsp;&nbsp;<a href="/cbtconsole/AddReturnOrder/LookReturnOrder?page=${page==1?page:page-1 }">上一页</a>&nbsp;&nbsp;
			跳转到<input id="jump" value="${page }" >&nbsp;&nbsp;<button onclick="jump()">确认</button>&nbsp;&nbsp;<a href="/cbtconsole/AddReturnOrder/LookReturnOrder?page=${page==totalpage?page:page+1 }">下一页</a></div>
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