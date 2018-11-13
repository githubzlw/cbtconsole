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
<title>入库验货数量查看</title>
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
var searchReport = "/cbtconsole/StatisticalReport/searchCoupusManagement"; //报表查询
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
String orderid=request.getParameter("orderid");
%>
<script type="text/javascript">
$(function(){
	setDatagrid();
	var opts = $("#easyui-datagrid").datagrid("options");
	opts.url = "/cbtconsole/warehouse/getStorageInspectionLogInfo";
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '入库验货数量查看',
			align : 'center',
			width : 'auto',
			height : 'auto',
			fit : true,
			pageSize : 1,
            nowrap:false,
			pageList : [ 1],
			nowrap : true,//列的内容超出所定义的列宽时,false为自动换行
			striped : true,//设置为true将交替显示行背景。
			toolbar : "#top_toolbar",
			url : '',
			loadMsg : '数据装载中......',
			singleSelect : false,
			fitColumns : true,
			idField:'itemid',
			style : {
	            padding : '8 8 10 8'
	        },
			pagination : true,
			rownumbers : true
		});
	}
	
function doQuery(page) {
    var senttimeBegin = $('#senttimeBegin').val();
    var senttimeEnd = $('#senttimeEnd').val();
	$("#easyui-datagrid").datagrid("load", {
		page:page,
		startTime:senttimeBegin,
		endTime:senttimeEnd
	});
}

function doReset(){
    $('#senttimeBegin').val("");
    $('#senttimeEnd').val("");
}





</script>
</head>
<body text="#000000" onload="doQuery(1);">
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;">
				时间：<input id="senttimeBegin"
					   name="senttimeBegin" readonly="readonly"
					   onfocus="WdatePicker({isShowWeek:true})"
					   value="${param.startdate}"
					   onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' /></td>
				~<input id="senttimeEnd"
						name="senttimeEnd" readonly="readonly"
						onfocus="WdatePicker({isShowWeek:true})" value="${param.enddate}"
						onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
	</div>
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'in_count',width:25,align:'center'">入库数量(商品)</th>
				<th data-options="field:'yhww_count',width:15,align:'center'">验货无误数量(商品)</th>
				<th data-options="field:'yywt_count',width:15,align:'center'">验货有问题数量(商品)</th>
				<th data-options="field:'no_count',width:30,align:'center'">未验货数量(商品)</th>
				<th data-options="field:'out_counts',width:30,align:'center'">已出库订单数量(订单)</th>
			</tr>
		</thead>
	</table>
</body>
</html>