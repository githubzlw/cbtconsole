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
<title>1688采购订单建议退货管理</title>
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
</style>
<script type="text/javascript">
	$(function() {
		setDatagrid();
	})

	function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '1688采购订单建议退货管理',
			//iconCls : 'icon-ok',
			width : "100%",
			height : "100%",
			fit : true,//自动补全 
			autoRowWidth:false,
			pageSize : 40,//默认选择的分页是每页20行数据
			pageList : [ 40 ],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
			// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/warehouse/getBuyReturnManage',//url调用Action方法
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


	function returns(id,orderid){
	    console.log(id);
	    console.log(orderid);
        $.ajax({
            url: "/cbtconsole/order/orderReturn",
            type:"POST",
            data : {"id":id,"orderid":orderid},
            dataType:"json",
            success:function(data){
               if(data>0){
                   topCenter("成功");
                   $('#easyui-datagrid').datagrid('reload');
			   }else{
                   topCenter("失败");
			   }
            }
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





	function doQuery(page) {
        var state=$('#state').combobox('getValue');
		var goodsid = $("#goodsid").val();
		$("#easyui-datagrid").datagrid("load", {
			"goodsid" : goodsid,
			"state":state,
			"page" : page
		});
	}

	function doReset() {
		$("#goodsid").val("");
        $('#state').combobox('setValue','0');
	}
    function doReset1() {
        $("#goodsid").val("");
    }
</script>
</head>
<body>
<div id="top_toolbar" style="padding: 5px; height: auto">
	<div>
		<form id="query_form" action="#" onsubmit="return false;">
			<span style="margin-left:700px;"></span>goodsid：<input  onFocus="doReset1()" onkeypress="if (event.keyCode == 13) doQuery(1)" type="text" id="goodsid">
			<select class="easyui-combobox" name="state" id="state" style="width:10%;" data-options="label:'状态:',panelHeight:'auto'">
				<option value="0" selected>全部</option>
				<option value="1">未退货</option>
				<option value="2">整单退货</option>
				<option value="3">部分退货</option>
			</select>
			<input class="but_color" type="button" value="查询" onclick="doQuery(1)">
			<input class="but_color" type="button" value="重置" onclick="doReset()">
		</form>
	</div>
</div>

	<table class="easyui-datagrid" id="easyui-datagrid"
		style="width: 1800px; height: 900px">
		<thead>
			<tr>
				<th data-options="field:'sOrderid',width:50,align:'center'">销售订单</th>
				<th data-options="field:'orderid',width:50,align:'center'">采购订单号</th>
				<th data-options="field:'orderstatus',width:35,align:'center'">采购订单状态</th>
				<th data-options="field:'itemname',width:45,align:'center'">采购商品名称</th>
				<th data-options="field:'imgurl',width:35,align:'center'">采购商品图片</th>
				<th data-options="field:'itemprice',width:20,align:'center'">采购商品单价</th>
				<th data-options="field:'itemqty',width:20,align:'center'">采购商品数量</th>
				<th data-options="field:'totalprice',width:30,align:'center'">采购订单总价</th>
				<th data-options="field:'sku',width:55,align:'center'">采购商品规格</th>
				<th data-options="field:'orderdate',width:55,align:'center'">采购订单时间</th>
				<th data-options="field:'username',width:45,align:'center'">采购人</th>
				<th data-options="field:'state',width:45,align:'center'">状态</th>
				<th data-options="field:'operating',width:60,align:'center'">退货操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>