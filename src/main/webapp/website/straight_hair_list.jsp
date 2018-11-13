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
<title>广东直发列表</title>
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
  String goods_pid=request.getParameter("goods_pid");
%>
<script type="text/javascript">
$(function(){
	$('#dlg1').dialog('close');
	goods_pid='<%=goods_pid%>';
	if(goods_pid != null && goods_pid != '' && goods_pid != 'null'){
		$("#goods_pid").val(goods_pid);
	}
	setDatagrid();
	var opts = $("#easyui-datagrid").datagrid("options");
	opts.url = "/cbtconsole/StatisticalReport/StraightHairList";
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '广东直发列表',
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
	var admuserid=$('#admuserid').combobox('getValue');
	var goods_pid=$("#goods_pid").val();
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
		"admuserid":admuserid,
		"goods_pid":goods_pid
	});
}

function doReset(){
	$('#admuserid').combobox('setValue','<%=adm.getId()%>');
	$("#goods_pid").val("");
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
 
//录入直发快递单号
 function nntryShipno(orderid,goodsid){
 	$('#dlg1').dialog('open');
 	$("#remark_").textbox('setValue','');
	$("#od_orderid").val(orderid);
	$("#od_goodsid").val(goodsid);
 }
 
//取消录入单号
 function cance1(){
 		$('#dlg1').dialog('close');
 		$("#od_orderid").val("");
 		$("#od_goodsid").val("");
 }
 
 function refundShipnoEntry(){
		var shipno= $.trim(document.getElementById("shipno").value);
		var remark= $.trim(document.getElementById("remark_").value);
		var orderid=$("#od_orderid").val();
		var goodsid=$("#od_goodsid").val();
		 var params = {"goodsid":goodsid,"orderid":orderid,"shipno":shipno,"remark":remark};
	 	 $.ajax({  
	         url:'/cbtconsole/StatisticalReport/straightShipnoEntry',  
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
<body text="#000000" onload="doQuery(1);$('#dlg1').dialog('close')">
	<div id="dlg1" class="easyui-dialog" title="录入直发单号" data-options="modal:true" style="width:400px;height:200px;padding:10px;autoOpen:false;;closed:true;display: none;">
		<form id="ff" method="post" style="height:100%;">
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="shipno" id="shipno"  style="width:70%;"  data-options="label:'直发单号:',required:true">
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="remark_" id="remark_"  style="width:70%;"  data-options="label:'直发备注:'">
			</div>
			<input type="hidden" id="od_orderid"/>
			<input type="hidden" id="od_goodsid"/>
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
				<input type="hidden" id="goods_pid" value=""/>
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1500px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'orderid',width:25,align:'center'">订单号</th>
				<th data-options="field:'userid',width:25,align:'center'">用户ID</th>
				<th data-options="field:'goodsid',width:40,align:'center'">商品号</th>
				<th data-options="field:'buyer',width:40,align:'center'">采购人</th>
				<th data-options="field:'sku',width:40,align:'center'">规格</th>
				<th data-options="field:'img',width:40,align:'center'">图片</th>
				<th data-options="field:'state',width:40,align:'center'">状态</th>
				<th data-options="field:'times',width:30,align:'center'">确认时间</th>
				<th data-options="field:'states',width:30,align:'center'">发货单号</th>
				<th data-options="field:'shipno',width:30,align:'center'">物流状态</th>
				<th data-options="field:'remark',width:30,align:'center'">发货备注</th>
				<th data-options="field:'operating',width:30,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>