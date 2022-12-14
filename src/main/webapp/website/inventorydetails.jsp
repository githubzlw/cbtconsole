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
<title>库存明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css"><script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>

<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script type="text/javascript">
var updateSourcesUrl = "/cbtconsole/inventory/updateSources"; //盘点库存
</script>
<style type="text/css">
		.wraps em,i{font-style: normal;display: inline-block;float:left;}
		.clearfix:before,.clearfix:after{content:"";display:table;}
		.clearfix:after{clear:both;}
		.clearfix{zoom:1;} 
		.wraps{border:2px solid #999;width:1047px;margin:0 auto;padding:20px;position: relative;display:none;/* top:500px;z-index: 999;  */   background-color: #e0ecff;}
		.wraps span{display:inline-block;width:100px;float:left;}
		.wraps input[type="text"]{border:1px solid #999;background-color: #fff;height:28px;border-radius: 4px;width:205px;float:left;}
        .wraps input[type="radio"]{width:18px;height:18px;position: relative;top:2px;}
        .wraps label{margin-right:10px;}
        .wrap6{overflow:hidden;}
        .wrap6 span,.wraps .reasons{float:left;position: relative;}
        .w235{width:300px;}
        .wraps .wrap{margin-bottom:40px;overflow:hidden;}
        .wrap7 img{width:250px;height: 250px;}
        .img_class{width:60px;height: 60px;}
        .wrap2 em{width:645px;}
        .wrap7 {float:right;position: relative;top:-30px;}
        .left{float:left;}
        p{text-align: center;}
        .other{position: absolute;top:15px;right:-222px;}
        .wrap8{text-align: center;}
        .submit_button{border:1px solid #999;background-color:#fff;padding:0 80px; line-height:28px;border-radius: 4px;}
.button_c{
border-radius: 5px;
background: #e7f1ff;
}
.button_top{
margin-top: 3px;
}
.av_count{margin-left: 70px;}
.top_title{font-size: 15px;font-weight: bold;}
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
<script type="text/javascript">
$(function(){
	 setDatagrid();
})

function setDatagrid() {
	var inid = $('#query_in_id').val();
		$('#easyui-datagrid').datagrid({
			title : '库存明细',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [20],//可以选择的分页集合
			nowrap : false,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/inventory/inventorydetails?inid='+inid,//url调用Action方法
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
	var inid = $('#query_in_id').val();
	/* var goods_pid = $('#query_goods_pid').val(); */
	
	$("#easyui-datagrid").datagrid("load", {
	  "page":page,
	  "inid":inid

    });
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


//导出报表
</script>
</head>
<body text="#000000" >
	<div  id="top_toolbar" style="padding: 5px; height: auto;display:none;" >
	<div>
	<span><!-- 库存ID --><input type="hidden" id="query_in_id" value="${param.inid}"></span>&nbsp;&nbsp;
	</div>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'goodsPid',width:50,align:'center'" >产品ID</th>
				<th data-options="field:'skuContext',width:100,align:'center'">产品SKU</th>
				<th data-options="field:'goodsImg',width:80,align:'center'">商品图片</th>
				<th data-options="field:'goodsNumber',width:50,align:'center'">库存变化数量</th>
				<th data-options="field:'orderContext',width:50,align:'center'">订单信息</th>
				<th data-options="field:'typeContext',width:50,align:'center'">类型</th>
				<th data-options="field:'remark',width:50,align:'center'">备注说明</th>
				<th data-options="field:'adm',width:50,align:'center'">操作人</th>
				<th data-options="field:'createtime',width:50,align:'center'">时间</th>
				<th data-options="field:'delContext',width:50,align:'center'">删除</th>
			</tr>
		</thead>
	</table>
</body>
</html>