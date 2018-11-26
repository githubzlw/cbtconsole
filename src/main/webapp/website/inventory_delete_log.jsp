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
<title>商品库存删除日志</title>
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
			title : '商品库存删除日志',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/StatisticalReport/searchGoodsInventoryDeleteInfo',//url调用Action方法
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
	var barcode =$('#have_barcode').combobox('getValue');
	var admName =$('#admName').combobox('getValue');
	var startdate = $("#startdate").val();
	var enddate = $("#enddate").val();
	var times=$("#times").val();
	$("#easyui-datagrid").datagrid("load", {
	  "page":page,
	  "barcode":barcode,
	  "startdate":startdate,
  	  "enddate":enddate,
  	  "admName":admName,
		"times":times
	});
}

function BigImg(img){
	htm_="<img width='400px' height='400px' src="+img+">";
	$("#big_img").append(htm_);
	$("#big_img").css("display","block");
}

function closeBigImg(){
	$("#big_img").css("display","none");
	$('#big_img').empty();
}

function doReset(){
    $("#times").val("");
	$('#have_barcode').combobox('setValue','全部');
	$('#admName').combobox('setValue','全部');
	$("#startdate").val("");
	$("#enddate").val("");
}

//导出库存删除日志
	function exportDeleteInventoryExcel(){
        var barcode =$('#have_barcode').combobox('getValue');
        var admName =$('#admName').combobox('getValue');
        var startdate = $("#startdate").val();
        var enddate = $("#enddate").val();
        if(admName == "全部"){
            admName="0";
        }
        if(barcode == "全部"){
            barcode="0";
        }
        window.location.href ="/cbtconsole/StatisticalReport/exportDeleteInventoryExcel?barcode="+barcode+"&admName="+admName+"&startdate="+startdate+"&enddate="+enddate;
	}

</script>
</head>
<body onload="doQuery('1')">
	  	<div class="mod_pay3" style="display: none;" id="big_img">
			
		</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;">
	         <select class="easyui-combobox" name="have_barcode" id="have_barcode" style="width:12%;" data-options="label:'原库位:',panelHeight:'400px',valueField: 'barcode',   
                    textField: 'barcode', value:'全部',selected:true,
                    url: '/cbtconsole/StatisticalReport/getHavebarcode',  
                    method:'get'">
				</select>
				<select class="easyui-combobox" name="admName" id="admName" style="width:10%;height: 30px;" data-options="label:'删除人:',panelHeight:'auto'">
				<option value="全部" selected>全部</option>
				<option value="camry">camry</option>
				<option value="mindy">mindy</option>
				<option value="buy1">buy1</option>
				<option value="buy2">buy2</option>
				<option value="Alisa">Alisa</option>
				<option value="buy5">buy5</option>
				</select>
	              	              删除时间:<input id="startdate"
						name="startdate" readonly="readonly"
						onfocus="WdatePicker({isShowWeek:true})"
						value="${param.startdate}"
						onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />-
						<input id="enddate" name="enddate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})"
						value="${param.startdate}"
						onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}'  style="margin-laft:5px;"/>
				<input type="hidden" id="times" value="${param.times}" name="times"/>
				<input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<a href="javascript:exportDeleteInventoryExcel();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">导出</a>
	</div>
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'goodscatid',width:50,align:'center'">商品品类</th>
				<th data-options="field:'good_name',width:50,align:'center'">商品名称</th>
				<th data-options="field:'barcode',width:80,align:'center'">商品库位</th>
				<th data-options="field:'sku',width:50,align:'center'">商品规格</th>
				<th data-options="field:'car_img',width:80,align:'center'">商品图片</th>
				<th data-options="field:'goods_p_price',width:50,align:'center'">采购价</th>
				<th data-options="field:'remaining',width:50,align:'center'">首次库存数量</th>
				<th data-options="field:'inventory_amount',width:60,align:'center'">首次库存金额</th>
				<th data-options="field:'new_remaining',width:40,align:'center'">盘点后库存数量</th>
				<th data-options="field:'new_inventory_amount',width:50,align:'center'">盘点后金额</th>
				<th data-options="field:'can_remaining',width:40,align:'center'">可用库存数量</th>
				<th data-options="field:'createtime',width:60,align:'center'">首次库存录入时间</th>
				<th data-options="field:'updatetime',width:60,align:'center'">最后更新库存时间</th>
				<th data-options="field:'admName',width:60,align:'center'">删除人</th>
				<th data-options="field:'create_time',width:50,align:'center'">删除时间</th>
				<th data-options="field:'delRemark',width:50,align:'center'">删除备注</th>
			</tr>
		</thead>
	</table>
</body>
</html>