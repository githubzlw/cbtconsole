<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>付款审批的相关财务明细报表</title>
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
<script type="text/javascript">
</script>
<style type="text/css">
.displaynone{display:none;}
.item_box{display:inline-block;margin-right:52px;}
.item_box select{width:150px;}
.mod_pay3 { width: 600px; position: fixed;
	top: 100px; left: 15%;      
	z-index: 1011; background: gray;
	padding: 5px; padding-bottom: 20px;
	z-index: 1011; border: 15px solid #33CCFF; }
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
<% 
 String times=request.getParameter("times");
%>
<script type="text/javascript">
$(function(){
	var time='<%=times%>';
	$("#date_time").val(time);
	setDatagrid();
	var opts = $("#easyui-datagrid").datagrid("options");
	opts.url = "/cbtconsole/StatisticalReport/orderDetetailsSaleDetails";
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '付款审批的相关财务明细报表(单位:RMB)',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 30,//默认选择的分页是每页20行数据
			pageList : [ 30],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '',//url调用Action方法
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
	var orderid=$('#orderid').val();
	var date_time=$('#date_time').val();
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
		"orderid":orderid,
		"date_time":date_time
	});
}

function doReset(){
	$("#orderid").textbox('setValue','');
}

 function showMessage(tip){
	 $.messager.show({
			title:'提示',
			msg:tip,
			showType:'slide',
			timeout: 1000,
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop,
				bottom:''
			}
		});
 }

</script>
</head>
<body text="#000000" onload="doQuery(1)">
		<div id="top_toolbar" style="padding: 5px; height: auto">
				<div>
					<form id="query_form" action="#" onsubmit="return false;" style="margin-left:500px;">
						<input class="easyui-textbox" name="orderid" id="orderid" style="width:15%;"  data-options="label:'订单号:'">
						<input type="hidden" id="date_time"/>
						 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
						 <input class="but_color" type="button" value="重置" onclick="doReset()">
					</form>
				</div>
			</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1500px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'order_no',width:40,align:'center'">订单号</th>
<!-- 				<th data-options="field:'originalAmount',width:40,align:'center'">订单最初到账金额</th> -->
<!-- 				<th data-options="field:'estimateBuyAmount',width:40,align:'center'">预估采购成本</th> -->
				<th data-options="field:'estimateFright',width:45,align:'center'">预估运费成本</th>
<!-- 				<th data-options="field:'estimateProfits',width:45,align:'center'">预估毛利润</th> -->
				<th data-options="field:'orderactualAmount',width:45,align:'center'">订单实际金额</th>
				<th data-options="field:'orderActualBuyAmount',width:45,align:'center'">实际采购成本</th>
				<th data-options="field:'orderActualFright',width:45,align:'center'">实际运费成本</th>
				<th data-options="field:'orderActualProfits',width:45,align:'center'">实际毛利润</th>
			</tr>
		</thead>
	</table>
</body>
</html>