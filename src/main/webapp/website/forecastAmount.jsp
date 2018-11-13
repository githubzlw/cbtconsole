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
<title>预估订单运费明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
<script type="text/javascript">
$(function(){
	setDatagrid();
	var opts = $("#easyui-datagrid").datagrid("options");
	opts.url = "/cbtconsole/StatisticalReport/getforecastAmount";
	$("#data").combobox({
		onChange: function (n,o) {
			getZfuDate();
		}
	});
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '预估订单运费明细(单位:RMB)',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20],//可以选择的分页集合
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
	var times=$("#times").val();
	 var orderid = $("#orderid").val();
	if(times==null || times==""){
		showMessage("查询错误");
		return;
	}
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
		"times":times,
		"type":"1",
		"orderid":orderid
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
 
 /**
  * 查询结果导出excel
  */
  function exportdata(){
	  var orderid=$("#orderid").val();
	  var times=$("#times").val();
 	 window.location.href ="/cbtconsole/StatisticalReport/exportForecastAmount?times="+times+"&orderid="+orderid+"&type=1";
 }

</script>
</head>
<body text="#000000" onload="doQuery(1)">
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;" style="margin-left:500px;">
				<div style="margin-bottom:20px">
					<input class="easyui-textbox"  name="orderid" id="orderid" style="width:18%;"  data-options="label:'销售订单号:'">
					 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				     <input class="but_color" type="button" value="重置" onclick="doReset()">
				</div>
				<input type="hidden" id="times" value="${param.times}" name="times"/>
				<input type="hidden" id="type" value="1" name="type"/>
			</form>
		</div>
		<a href="javascript:exportdata();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">导出</a>
		<span>【${param.times}】预估运费总金额</span><span style="color:red">￥【${param.amount}】</span>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:2000px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'shipmentno',width:25,align:'center'">公司出运号</th>
				<th data-options="field:'orderid',width:40,align:'center'">出运订单号</th>
				<th data-options="field:'remarks',width:55,align:'center'">合并出运订单</th>
				<th data-options="field:'createtime',width:35,align:'center'">出运时间</th>
				<th data-options="field:'sentTime',width:35,align:'center'">实际出运时间</th>
				<th data-options="field:'sweight',width:40,align:'center'">我司称重</th>
				<th data-options="field:'realWeight',width:45,align:'center'">运输公司重量</th>
				<th data-options="field:'svolume',width:45,align:'center'">我司体积</th>
				<th data-options="field:'volumeweight',width:45,align:'center'">抛重</th>
				<th data-options="field:'transportcompany',width:30,align:'center'">运输公司</th>
				<th data-options="field:'shippingtype',width:55,align:'center'">运输方式</th>
				<th data-options="field:'country',width:55,align:'center'">运输国家</th>
				<th data-options="field:'expressno',width:55,align:'center'">运输单号</th>
				<th data-options="field:'estimatefreight',width:30,align:'center'">预估运费</th>
				<th data-options="field:'totalPrice',width:45,align:'center'">实际运费</th>
				<th data-options="field:'reMark',width:45,align:'center'">出运备注</th>
			</tr>
		</thead>
	</table>
</body>
</html>