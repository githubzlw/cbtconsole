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
<title>采样订单退样</title>
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
  String orderid=request.getParameter("orderid");
%>
<script type="text/javascript">
$(function(){
	var t_orderid='<%=orderid%>';
	if(t_orderid != null && t_orderid != ''){
		$("#t_orderid").val(t_orderid);
        doQuery(1);
	}
	setDatagrid();
	$('#dlg1').dialog('close');
})

	

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '采样订单退样',
			//iconCls : 'icon-ok',
			width : "100%",
            nowrap:false,
			fit : true,//自动补全 
			pageSize : 50,//默认选择的分页是每页20行数据
			pageList : [ 50],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/warehouse/searchRefundSample',//url调用Action方法
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
// 	var state=$('#state').combobox('getValue');
	var t_orderid=$("#t_orderid").val();
	$("#easyui-datagrid").datagrid("load", {
	  "page":page,
// 	  "state":state,
	  "t_orderid":t_orderid
	});
}

function doReset(){
// 	$('#state').combobox('setValue','0');
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
function nntryShipno(counts,flag,in_id,t_id,orderid,od_orderid,goodsid){
	$('#dlg1').dialog('open');
	$("#t_id").val(t_id);
	$("#t_orderid").val(orderid);
	$("#in_id").val(in_id);
	$("#od_orderid").val(od_orderid);
	$("#goodsid").val(goodsid);
	$('#counts').numberbox('setValue',counts);
	$("#flag").val(flag);
	$("#remark_").textbox('setValue','');
}

//取消录入单号
function cance1(){
		$('#dlg1').dialog('close');
		$("#shipno").textbox('setValue','');
		$("#remark_").textbox('setValue','');
		$("#t_id").val("");
		$("#counts").textbox('setValue','');
		$("#t_orderid").val("");
		$("#in_id").val("");
		$("#od_orderid").val("");
		$("#goodsid").val("");
		$("#flag").val("");
}

function refundShipnoEntry(){
	var t_id=$("#t_id").val();
	var in_id=$("#in_id").val();
	var counts=$("#counts").val();
	var t_orderid=$("#t_orderid").val();
	var od_orderid=$("#od_orderid").val();
	 var counts= $("#counts").val();
	var goodsid=$("#goodsid").val();
	var flag=$("#flag").val();
	var shipno= $.trim(document.getElementById("shipno").value);
	var remark= $.trim(document.getElementById("remark_").value);
	 var params = {"t_id":t_id,"t_orderid":t_orderid,"shipno":shipno,"remark":remark,"in_id":in_id,"od_orderid":od_orderid,"goodsid":goodsid,"flag":flag,"counts":counts};
 	 $.ajax({  
         url:'/cbtconsole/warehouse/refundShipnoEntry',  
         type:"post",  
         data:params,  
         success:function(data){
        	 console.log(data);
       	if(Number(data)>0){
       		topCenter("操作成功");
       		$('#easyui-datagrid').datagrid('reload');
       		cance1();
 	  	}else{
 	  		topCenter("操作失败或未做验货操作");
 	  		}
         }, 
     }); 
}




</script>
</head>
<body onload="doQuery('1');$('#dlg1').dialog('close')">
	<div id="dlg1" class="easyui-dialog" title="录入退货单号" data-options="modal:true" style="width:400px;height:300px;padding:10px;autoOpen:false;;closed:true;display: none;">
	<form id="ff" method="post" style="height:100%;">
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="shipno" id="shipno"  style="width:70%;"  data-options="label:'退货单号:',required:true">
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-numberbox" name="counts" id="counts"  style="width:70%;"  data-options="label:'退货数量:'">
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="remark_" id="remark_"  style="width:70%;"  data-options="label:'退货备注:'">
			</div>
			<input type="hidden" id="t_id" name="t_id"/>
			<input type="hidden" id="t_orderid" name="t_orderid"/>
			<input type="hidden" id="in_id" name="in_id"/>
			<input type="hidden" id="od_orderid" name="od_orderid"/>
			<input type="hidden" id="goodsid" name="goodsid"/>
			<input type="hidden" id="flag" name="flag"/>
			<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="refundShipnoEntry()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance1()" style="width:80px">取消</a>
		</div>
		</form>
	</div>
		<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;" style="margin-left:500px;">
<!-- 				<select class="easyui-combobox" name="state" id="state" style="width:15%;" data-options="label:'超期状态:',panelHeight:'auto'"> -->
<!-- 				<option value="0" selected>全部</option> -->
<!-- 				<option value="1">已超期</option> -->
<!-- 				<option value="2">未超期</option> -->
<!-- 				</select> -->
				<input type="hidden" id="t_orderid"/>
<!-- 				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)">  -->
<!-- 				 <input class="but_color" type="button" value="重置" onclick="doReset()"> -->
			</form>
		</div>
	</div>
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'goods_name',width:50,align:'center'">产品名</th>
				<th data-options="field:'img',width:50,align:'center'">图片</th>
				<th data-options="field:'price',width:80,align:'center'">采购价格</th>
				<th data-options="field:'profit_rate',width:50,align:'center'">利润率</th>
				<th data-options="field:'inventory_count',width:80,align:'center'">库存数量</th>
				<th data-options="field:'id_time',width:50,align:'center'">入库时间</th>
				<th data-options="field:'last_return_time',width:50,align:'center'">最晚退货时间</th>
				<th data-options="field:'shop_id',width:60,align:'center'">供应商名称</th>
				<th data-options="field:'shop_id_socre',width:40,align:'center'">供应商评分</th>
				<th data-options="field:'state',width:50,align:'center'">当前状态</th>
				<th data-options="field:'shipno',width:40,align:'center'">快递单号</th>
				<th data-options="field:'remark',width:40,align:'center'">退货备注</th>
				<th data-options="field:'goodsAttr',width:50,align:'center'">采样属性</th>
				<th data-options="field:'operating',width:60,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>