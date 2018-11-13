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
<title>利润管理页面</title>
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
	font-size: 34px;
}
tr .td_class{width:230px;}
.td_class lable{
	float:left;
	width:120px;
}
.w_input input{width:200px;}
</style>
</head>
<body onload="doQuery(1,0)">
	<div id="dlg1" class="easyui-dialog" title="备注" data-options="modal:true" style="width:300px;height:300px;padding:10px;autoOpen:false;display: none;">
	<form id="ff" method="post" style="height:100%;">
			<input type="hidden" id="ad_orderid"/>
			<input type="hidden" id="ad_admuserid"/>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="remark_" id="remark_"  style="width:210px;height: 150px;"  data-options="multiline:true">
			</div>
			<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="insertOrderRemark()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance1()" style="width:80px">取消</a>
		</div>
		</form>
	</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div style="margin-left:10px;">
		<span style="font-size:25px;font-weight:bold">本周销售总金额:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_1" style="font-size:20px;width:35px;margin-right:100px">0</span>
		<span style="font-size:25px;font-weight:bold">本周销售货源总金额:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_2" style="font-size:20px;width:35px;margin-right:100px">0</span>
		<span style="font-size:25px;font-weight:bold">利润:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_3" style="font-size:20px;width:35px;margin-right:100px">0</span>
		<span style="font-size:25px;font-weight:bold">销售数量:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_4" style="font-size:20px;width:35px;margin-right:100px">0</span>
		</div><br>
		<div>
				<select class="easyui-combobox" name="days" id="days" style="width:10%;" data-options="label:'商品时间:',panelHeight:'auto'">
				        <option value="1" selected="selected">当天</option>
						<option value="3">最近三天</option>
						<option value="7">最近一周</option>
						<option value="14">最近两周</option>
						<option value="30">最近一个月</option>
						<option value="90">最近三个月</option>
						<option value="180">最近半年</option>
						<option value="365">最近1年</option>
						<option value="999">全部</option>
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<select class="easyui-combobox" name="admuserid" id="admuserid" style="width:8%;" data-options="label:'电商采购人:',panelHeight:'auto',valueField: 'id',   
                    textField: 'admName', value:'<%=adm.getId()%>',selected:true,
                    url: '/cbtconsole/warehouse/getAllBuyer',  
                    method:'get'">
				</select>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="easyui-textbox" name="goodsid" id="goodsid" style="width:8%;margin-top: 10px;"  data-options="label:'订单商品编号:'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="easyui-textbox" name="orderid" id="orderid" style="width:12%;margin-top: 10px;"  data-options="label:'客户订单号:'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="easyui-textbox" name="goods_name" id="goods_name" style="width:15%;margin-top: 10px;"  data-options="label:'产品名称:'">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1,0)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<c:if test="<%=adm.getId()==9 || adm.getId()==1%>">
		<a href="javascript:purchasing_allocation();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">订单采购分配</a>
		</c:if>
	</div>
	<table class="easyui-datagrid" id="easyui-datagrid"
		style="width: 1800px; height: 900px">
		<thead>
			<tr>
				<th data-options="field:'user_id',width:30,align:'center'">商品ID</th>
				<th data-options="field:'orderid',width:50,align:'center'">订单号</th>
				<th data-options="field:'country',width:30,align:'center'">订单国家</th>
				<th data-options="field:'saleer',width:25,align:'center'">销售</th>
				<th data-options="field:'fptime',width:40,align:'center'">分配时间</th>
				<th data-options="field:'paytime',width:50,align:'center'">支付时间</th>
				<th data-options="field:'domesticTime',width:20,align:'center'">国内准备段</th>
				<th data-options="field:'amount_s',width:30,align:'center'">分配总数/采购</th>
				<th data-options="field:'amounts',width:40,align:'center'">入库/验货无误/验货疑问/无物流信息</th>
				<th data-options="field:'status',width:30,align:'center'">当前状态</th>
			</tr>
		</thead>
	</table>
</body>
<script src="../js/saleProfitRate.js"></script>
</html>