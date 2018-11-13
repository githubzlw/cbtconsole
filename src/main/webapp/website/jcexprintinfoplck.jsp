<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.cbt.util.Redis" %>
<%@page import="com.cbt.util.AppConfig"%>
<%@ page import="com.cbt.website.userAuth.bean.Admuser" %>
<%@ page import="com.cbt.util.SerializeUtil" %>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>佳成运单批量导出</title>
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
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
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
	//取出当前登录用户
   String sessionId = request.getSession().getId();
    String userJson = Redis.hget(sessionId, "admuser");
	Admuser adm =(Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class); 
	int userid=adm.getId();
	%>
<script type="text/javascript">
$(function(){
// 	document.onkeydown = function(e){
// 		var ev = document.all ? window.event : e; 
// 		if(ev.keyCode==13) {
// 			 var number=$('#easyui-datagrid').datagrid('options').pageNumber;
// 			 doQuery(number);
// 		}
// 	} 
	setDatagrid();
	$('#data').datebox({  
		 closeText:'关闭',  
		 formatter:function(date){  
		  var y = date.getFullYear();  
		  var m = date.getMonth()+1;  
		  var d = date.getDate();  
		  var h = date.getHours();  
		  var M = date.getMinutes();  
		  var s = date.getSeconds();  
		  function formatNumber(value){  
		   return (value < 10 ? '0' : '') + value;  
		  }  
		  return y+'-'+m+'-'+d;  
		 },  
		 parser:function(s){  
		  var t = Date.parse(s);  
		  if (!isNaN(t)){  
		   return new Date(t);  
		  } else {  
		   return new Date();  
		  }  
		 }  
		});
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '佳成运单批量导出',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20],//可以选择的分页集合
			nowrap : false,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/warehouse/getJcexPrintInfoPlck',//url调用Action方法
			loadMsg : '数据装载中......',
			singleSelect : false,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
// 			idField:'itemid',
			//sortName : 'xh',//当数据表格初始化时以哪一列来排序
// 			sortOrder : 'desc',//定义排序顺序，可以是'asc'或者'desc'（正序或者倒序）。
			pagination : true,//分页
			rownumbers : true
		//行数
		});
	}
	
function doQuery(page) {
	var ckStartTime = $("#ckStartTime").val();
	var ckEndTime = $("#ckEndTime").val();
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
    	"ckStartTime":ckStartTime,
    	"ckEndTime":ckEndTime
	});
}

function doReset(){
	$('#query_form').form('clear');
}

/**
 * 查询结果导出excel
 */
 function exportdata(){
	 var ckStartTime = $("#ckStartTime").val();
	 var ckEndTime = $("#ckEndTime").val();
	 window.location.href ="/cbtconsole/warehouse/exportJcexPrintInfo?ckStartTime="+ckStartTime+"&ckEndTime="+ckEndTime;
}

</script>
</head>
<body>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" style="margin-left:350px;" onsubmit="return false;">
				<input class="easyui-datebox" name="ckStartTime" id="ckStartTime" style="width:15%;"  data-options="label:'开始日期:'">
				<input class="easyui-datebox" name="ckEndTime" id="ckEndTime" style="width:15%;"  data-options="label:'结束日期:'">
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<a href="javascript:exportdata();" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">导出Excel</a>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'express_no',width:30,align:'center'">运单条码</th>
				<th data-options="field:'adminname',width:30,align:'center'">寄件人</th>
				<th data-options="field:'admincompany',width:30,align:'center'">英文公司名称</th>
				<th data-options="field:'admincode',width:30,align:'center'">发件人邮编</th>
				<th data-options="field:'recipients',width:30,align:'center'">收件人</th>
				<th data-options="field:'address',width:30,align:'center'">收件地址</th>
				<th data-options="field:'zone',width:30,align:'center'">收件国家</th>
				<th data-options="field:'address2',width:30,align:'center'">收件城市</th>
				<th data-options="field:'statename',width:30,align:'center'">州名</th>
				<th data-options="field:'zipcode',width:30,align:'center'">收件邮编</th>
				<th data-options="field:'phone',width:30,align:'center'">收件人电话</th>
				<th data-options="field:'payType',width:30,align:'center'">结算方式</th>
<!-- 				<th data-options="field:'number',width:30,align:'center'">件数</th> -->
				<th data-options="field:'cargo_type',width:30,align:'center'">包装</th>
				<th data-options="field:'productname',width:30,align:'center'">中文品名</th>
				<th data-options="field:'hscode',width:30,align:'center'">HS CODE</th>
				<th data-options="field:'productcurreny',width:30,align:'center'">申报币种</th>
				<th data-options="field:'pay_curreny',width:30,align:'center'">支付币种</th>
				<th data-options="field:'weight',width:30,align:'center'">重量</th>
				<th data-options="field:'volume_lwh',width:30,align:'center'">体积</th>
				<th data-options="field:'producenglishtname',width:30,align:'center'">英文品名</th>
				<th data-options="field:'productnum',width:30,align:'center'">内件数</th>
				<th data-options="field:'productprice',width:30,align:'center'">申报价值</th>
				<th data-options="field:'productremark',width:30,align:'center'">备注</th>
				<th data-options="field:'useridAndOrderid',width:30,align:'center'">参考单号</th>
			</tr>
		</thead>
	</table>
</body>
</html>