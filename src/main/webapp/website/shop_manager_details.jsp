<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.cbt.util.*"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>店铺产品图片详情</title>
<style type="text/css">
.but_color {
	background: #44a823;
	width: 80px;
	height: 24px;
	border: 1px #aaa solid;
	color: #fff;
}

.panel-title {
	text-align: center;
	height: 30px;
	font-size: 24px;
}
tr .td_class{width:230px;}
.td_class lable{
	float:left;
	width:120px;
}
.w_input input{width:200px;}
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
</style>
<%
  String id=request.getParameter("id");
  String status=request.getParameter("status");
%>
<script type="text/javascript">
	$(function() {
		setDatagrid();
		var opts = $("#easyui-datagrid").datagrid("options");
		opts.url = "/cbtconsole/warehouse/getShopManagerDetails";
		var id='<%=id%>';
		var status='<%=status%>';
		if(id!=null || id!=""){
			$("#id").val(id);
			$('#status').combobox('setValue',status);
			doQuery(1);
		}
		$(document).ready(function () {
		    $("#status").combobox({
				onChange: function (state,type,id) {
					updateState(state,type,id);
				}
			});
			});
	})

	function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '店铺产品图片详情',
			//iconCls : 'icon-ok',
			width : "100%",
			height : "100%",
			fit : true,//自动补全 
			autoRowWidth:false,
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20 ],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
			// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '',//url调用Action方法
			loadMsg : '数据装载中......',
			singleSelect : true,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
			//sortName : 'xh',//当数据表格初始化时以哪一列来排序
			//sortOrder : 'desc',//定义排序顺序，可以是'asc'或者'desc'（正序或者倒序）。
			pagination : true,//分页
			rownumbers : true
		//行数
		});
	}


	function doQuery(page) {
		var id = $("#id").val();
		var goods_pid=$("#goods_pid").val();
		$("#easyui-datagrid").datagrid("load", {
			"id" : id,
			"page" : page,
			"goods_pid":goods_pid
		});
	}

	function doReset() {
		$('#goods_pid').textbox('setValue','');
	}
	
	function BigImg(img){
		htm_="<img  src="+img+">";
		$("#big_img").append(htm_);
		$("#big_img").css("display","block");
	}

	function closeBigImg(){
		$("#big_img").css("display","none");
		$('#big_img').empty();
	}
	
	function updateState(state,type,id){
		if(type == "0"){
			id=$("#id").val();
		}
		$.ajax({
			url : "/cbtconsole/warehouse/updateShopState",
			data:{
	        	  "id":id,
	        	  "state":state,
	        	  "type":type
	        	  },
			type : "post",
			async:false,
			success :function(data){
				if(data=="1"){
					$.messager.alert('提示','更新店铺状态成功');
					var now_page=$('#easyui-datagrid').datagrid('getPager').data("pagination").options.pageNumber; 
					doQuery(now_page);
				}else{
					$.messager.alert('提示','更新店铺状态成功');
				}
			}
		});
	}
	
</script>
</head>
<body>
	<div class="mod_pay3" style="display: none;" id="big_img">
			
	</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;" style="margin-left:450px;">
				<input class="easyui-textbox" name="goods_pid" id="goods_pid" style="width:20%;"  data-options="label:'产品编号:'">
				<input type="hidden" id="id"/>
				 <input class="but_color" type="button" style="margin-left:50px;" value="查询" onclick="doQuery(1)">
				 <input class="but_color" type="button" style="margin-left:50px;margin-right:650px;" value="重置" onclick="doReset()">
				 <select class="easyui-combobox" name="status" id="status" onchange="updateState(this.value,0,0);" style="width:15%;height: 30px;" data-options="label:'当前店铺状态:',panelHeight:'auto'">
					<option value="0">自动禁用</option>
					<option value="1">自动全免</option>
					<option value="2">人工解禁</option>
					<option value="3">人工全免</option>
					<option value="4">系统无法判断</option>
				</select>
			</form>
		</div>
	</div>

	<table class="easyui-datagrid" id="easyui-datagrid"
		style="width: 1800px; height: 900px">
		<thead>
			<tr>
				<th data-options="field:'goods_pid',width:40,align:'center'">产品编号</th>
				<th data-options="field:'goods_name',width:50,align:'center'">产品名称</th>
				<th data-options="field:'status',width:30,align:'center'">产品状态</th>
				<th data-options="field:'imgs',width:200,align:'center'">产品图片</th>
				<th data-options="field:'operation',width:30,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>