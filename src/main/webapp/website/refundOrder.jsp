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
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>采样订单退样管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
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
$(function(){
	setDatagrid();
	$('#dlg1').dialog('close');
})

	

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '采样订单退样管理',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 50,//默认选择的分页是每页20行数据
			pageList : [ 50],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/warehouse/searchRefundOrder',//url调用Action方法
			loadMsg : '数据装载中......',
			singleSelect : false,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
			idField:'itemid',
			//sortName : 'xh',//当数据表格初始化时以哪一列来排序
// 			sortOrder : 'desc',//定义排序顺序，可以是'asc'或者'desc'（正序或者倒序）。
			pagination : true,//分页
			rownumbers : true
		//行数
		});
	}
	
function doQuery(page) {
	var state=$('#state').combobox('getValue');
	var admuserid=$('#admuserid').combobox('getValue');
	$("#easyui-datagrid").datagrid("load", {
	  "page":page,
	  "state":state,
		"admuserid":admuserid
	});
}

function doReset(){
	$('#state').combobox('setValue','0');
    $('#state').combobox('setValue','1');
}





function topCenter(msg){
	$.messager.show({
		title:'消息',
		msg:msg,
		showType:'slide',
		style:{
			right:'',
			top:document.body.scrollTop+document.documentElement.scrollTop,
			bottom:''
		}
	});
}
// 录入退货快递单号
function nntryShipno(flag,in_id,t_id,orderid,od_orderid,goodsid){
	$('#dlg1').dialog('open');
	$("#t_id").val(t_id);
	$("#t_orderid").val(orderid);
	$("#in_id").val(in_id);
	$("#od_orderid").val(od_orderid);
	$("#goodsid").val(goodsid);
	$("#flag").val(flag);
	$("#remark_").textbox('setValue','');
	$("#t_id").val("");
}

function openRefund(t_orderid){
	$('#dlg1').dialog('open');
	$("#t_orderid").val(t_orderid);
	$("#remark_").textbox('setValue','');
	$("#shipno").textbox('setValue','');
}

//取消录入单号
function cance1(){
		$('#dlg1').dialog('close');
		$("#shipno").textbox('setValue','');
		$("#remark_").textbox('setValue','');
		$("#t_orderid").val("");
}

function refundShipnoEntry(){
	var t_orderid=$("#t_orderid").val();
	var shipno= $.trim(document.getElementById("shipno").value);
	var remark= $.trim(document.getElementById("remark_").value);
	if(shipno == null || shipno == ""){
		topCenter("请输入退货单号");
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
	 	  		topCenter("操作失败");
	 	  	}
         }, 
     }); 
}



</script>
</head>
<body onload="doQuery('1');$('#dlg1').dialog('close')">
	<div id="dlg1" class="easyui-dialog" title="录入退货单号" data-options="modal:true" style="width:400px;height:200px;padding:10px;autoOpen:false;;closed:true;display: none;">
	<form id="ff" method="post" style="height:100%;">
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="shipno" id="shipno"  style="width:70%;"  data-options="label:'退货单号:',required:true">
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="remark_" id="remark_"  style="width:70%;"  data-options="label:'退货备注:'">
			</div>
			<input type="hidden" id="t_orderid" name="t_orderid"/>
			<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="refundShipnoEntry()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance1()" style="width:80px">取消</a>
		</div>
		</form>
	</div>
		<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;" style="margin-left:500px;">
				<select class="easyui-combobox" name="admuserid" id="admuserid" style="width:15%;" data-options="label:'电商采购人:',panelHeight:'auto',valueField: 'id',   
                    textField: 'admName', value:'<%=adm.getId()%>',selected:true,
                    url: '/cbtconsole/warehouse/getAllBuyer',  
                    method:'get'">
				</select>
				<select class="easyui-combobox" name="state" id="state" style="width:15%;" data-options="label:'超期状态:',panelHeight:'auto'">
				<option value="0" selected>全部</option>
				<option value="1" selected>已超期</option>
				<option value="2">未超期</option>
				</select>
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<a href="/cbtconsole/website/monthly_refund_statistics.jsp" target='_blank' class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">月退款统计</a>
	</div>
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'seller',width:50,align:'center'">采购店铺</th>
				<th data-options="field:'orderid',width:50,align:'center'">采购订单号</th>
				<th data-options="field:'tbOr1688',width:80,align:'center'">采购来源</th>
				<th data-options="field:'totalprice',width:50,align:'center'">采购订单总金额</th>
				<th data-options="field:'itemqty',width:80,align:'center'">订单商品数量</th>
				<th data-options="field:'username',width:50,align:'center'">采购人</th>
				<th data-options="field:'orderdate',width:50,align:'center'">下单时间</th>
				<th data-options="field:'paydata',width:60,align:'center'">付款时间</th>
				<th data-options="field:'delivery_date',width:40,align:'center'">发货时间</th>
				<th data-options="field:'orderstatus',width:50,align:'center'">订单状态</th>
				<th data-options="field:'shipno',width:40,align:'center'">订单运单号</th>
				<th data-options="field:'remark',width:40,align:'center'">退货备注</th>
				<th data-options="field:'counts',width:40,align:'center'">已退货数量</th>
				<th data-options="field:'operating',width:60,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>