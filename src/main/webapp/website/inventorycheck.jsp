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
<title>库存盘点</title>
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
.form-horizontal .control-label{text-align: left;}
th{font-weight: normal;}
th,td{text-align: center;}
label{font-weight: normal;}
.remark label{margin-right:32px;}
button{width: 300px;}
.container{display: none;}
table td:last-child{text-align: center;}
table input[type="radio"]{width:20px;height:20px;}

th{font-weight: normal;}
th,td{text-align: center;}
label{font-weight: normal;}
input[type="file"]{position: absolute;top:0;left:15px;width: 124px;height:34px;opacity: 0;}
.gain{color:#4395ff;cursor:pointer;}
table  input[type="checkbox"]{width:20px;height:20px;}
.remark{margin-top:20px;}
.remark label{margin-right:32px;}
button{width: 300px;}
.transparent,.transparent-bg{width:100%;height:100%;background-color:rgba(0,0,0,0);position: fixed;z-index:1;display: none;text-align: center;}
.transparent-bg{z-index:2;background-color:rgba(0,0,0,.5);}
      .transparent img{display: inline-block;z-index:3;position: relative;top:20px;}
em,i{font-style: normal;}

</style>
<script type="text/javascript">
$(function(){
	setDatagrid();
	//doQuery(1);
	
	 $("#query_button").click(function(){
		doQuery(1);
		
	});
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '库存清单',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [20],//可以选择的分页集合
			nowrap : false,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/inventory/check,//url调用Action方法
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
	var goods_name = $('#query_goods_name').val();
	var goods_pid = $('#query_goods_pid').val();
    var goodscatid = $('#query_goodscatid').combobox('getValue');
	var minintentory = $('#query_minintentory').val();
	var maxintentory = $('#query_maxintentory').val();
	
	$("#easyui-datagrid").datagrid("load", {
	  "page":page,
	  "goods_pid":goods_pid,
	  "maxintentory":maxintentory,
	  "minintentory":minintentory,
      "goodscatid":goodscatid

    });
}

function doReset(){
	$('#query_goods_name').val("");
	$('#query_goods_pid').val("");
	$('#query_minintentory').val("");
	$('#query_maxintentory').val("");
    $('#query_goodscatid').combobox('setValue','0');
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
function exportData(){
	//生成报表
	var have_barcode=$('#have_barcode').combobox('getValue');
	var flag =$('#flag').val();
	var type =$('#type').val();
	var goodinfo =$('#goodinfo').val();
	var scope =$('#scope').val();
	var count =$('#count').val();
	var sku =$('#sku').val()
	var barcode =$('#barcode').val();
	var type1 =$('#type1').val();
	var type_="0";
	var startdate = $("#startdate").val();
	var enddate = $("#enddate").val();
    var goodscatid=$('#goodscatid').combobox('getValue');
	if(type1!=null){
		type_=type1;
	}
	if(goodscatid == "全部"){
        goodscatid="abc";
	}else if(goodscatid == "其他"){
        goodscatid="bcd"
	}
	window.location.href ="/cbtconsole/inventory/exportGoodsInventory?startdate="+startdate+"&enddate="+enddate+"&type="+type+"&goodinfo="+goodinfo+"&scope="+scope+"&count="+count+"&sku="+sku+"&type_="+type_+"&barcode="+barcode+"&flag="+flag+"&goodscatid="+goodscatid;
}
</script>
</head>
<body text="#000000" >
	<div  id="top_toolbar" style="padding: 5px; height: auto">
	<div>
	<span class="top_title">产品检索</span>&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="hidden" value="${param.inid }" id="query_in_id">
	<!-- <span>产品名称<input type="text" id="query_goods_name" value=""></span>&nbsp;&nbsp; -->
	<span>产品ID<input type="text" id="query_goods_pid" value=""></span>&nbsp;&nbsp;
	<select class="easyui-combobox" name="goodscatid" id="query_goodscatid" style="width:15%;" 
	data-options="label:'产品类别:',Height:'2000px',valueField:'goodsCatid',
                    textField:'categoryName', value:'0',selected:true,
                    url: '/cbtconsole/StatisticalReport/getAllInventory',
                    method:'get'">
	</select>&nbsp;&nbsp;
	<span>库存量大于<input type="text" id="query_minintentory" value=""></span>&nbsp;&nbsp;
	<span>库存量小于<input type="text" id="query_maxintentory" value=""></span>&nbsp;&nbsp;
	<input type="button" value="查询" class="button_c" id="query_button"/>
	
	</div>
	
	<br><br>
	<div>
	<span class="top_title">库存修正</span>&nbsp;&nbsp;&nbsp;&nbsp;
	<span><input type="button" class="button_c" id="add_inventory" value="录入库存" onclick="openInventoryEntryView()"></span>&nbsp;&nbsp;
	<span><input type="button"  class="button_c" id="luimport" value="导入未匹配产品"></span>&nbsp;&nbsp;
	<!-- <span><input type="button"  class="button_c" id="update_inventory" value="产品报损调整"></span>&nbsp;&nbsp; -->
	<span><input type="button"  class="button_c" id="add_inventory_online" value="增加线上产品库存"></span>&nbsp;&nbsp;
	<span><span class="title_tile">最近盘点时间 </span><span id="intentory_time"></span></span>
	
	</div>
	<br>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'categoryName',width:80,align:'center'">产品品类</th>
				<th data-options="field:'goodsPid',width:50,align:'center'" >产品ID</th>
				<th data-options="field:'goodsName',width:100,align:'Left'">产品名称</th>
				<th data-options="field:'skuContext',width:100,align:'center'">产品SKU</th>
				<th data-options="field:'carImg',width:80,align:'center'">商品图片</th>
				<th data-options="field:'remaining',width:50,align:'center'">库存数量</th>
				<th data-options="field:'canRemaining',width:50,align:'center'">可用库存</th>
				<th data-options="field:'barcode',width:50,align:'center'">库位</th>
				<th data-options="field:'checkTime',width:50,align:'center'">盘点时间</th>
				<th data-options="field:'operation',width:50,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>