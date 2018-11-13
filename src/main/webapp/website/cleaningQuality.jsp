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
<title>清洗质量</title>
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
	right: 15%;
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
<% 
  String sku=request.getParameter("sku");
  String goods_pid=request.getParameter("goods_pid");
  String car_urlMD5=request.getParameter("car_urlMD5");
%>
<script type="text/javascript">
$(function(){
	setDatagrid();

})

	

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '清洗质量',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20],//可以选择的分页集合
			nowrap : false,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/warehouse/cleaningQuality',//url调用Action方法
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
    var adminId=$('#adminId').combobox('getValue');
    var createtime=$('#createtime').combobox('getValue');
    var updatetime=$('#updatetime').combobox('getValue');
	$("#easyui-datagrid").datagrid("load", {
        "page":page,
		"adminId":adminId,
		"createtime":createtime,
		"updatetime":updatetime
	});
}

function doReset(){
    $('#adminId').combobox('setValue','0');
    $('#updatetime').combobox('setValue','0');
    $('#createtime').combobox('setValue','0');
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




</script>
</head>
<body text="#000000">
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;">
				<select class="easyui-combobox" name="createtime" id="createtime" style="width:20%;" data-options="label:'发布时间:',panelHeight:'auto'">
					<option value="0" selected>全部</option>
					<option value="1">最近1天</option>
					<option value="7">最近7天</option>
					<option value="30">最近30天</option>
				</select>
				<select class="easyui-combobox" name="updatetime" id="updatetime" style="width:20%;" data-options="label:'编辑时间:',panelHeight:'auto'">
					<option value="0" selected>全部</option>
					<option value="1">最近1天</option>
					<option value="7">最近7天</option>
					<option value="30">最近30天</option>
				</select>
				<select class="easyui-combobox" name="adminId" id="adminId" style="width:12%;" data-options="label:'负责人:',panelHeight:'400px',valueField: 'id',
                    textField: 'barcode', value:'0',selected:true,
                    url: '/cbtconsole/StatisticalReport/getAllUser',
                    method:'get'">
				</select>
				<input class="but_color" type="button" value="查询" onclick="doQuery(1)">
				<input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'admName',width:'100px',align:'center'">负责人</th>
				<th data-options="field:'weightProblem',width:'100px',align:'center'">疑似重量有问题产品数量</th>
				<th data-options="field:'autoShelf',width:'60px',align:'center'">本期间被自动下架商品</th>
			</tr>
		</thead>
	</table>
</body>
</html>