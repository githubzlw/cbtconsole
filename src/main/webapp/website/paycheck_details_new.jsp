<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>订单支付情况</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<style type="text/css">
.panel-title {
	text-align: center;
	height: 30px;
	font-size: 24px;
}

.but_color {
	background: #44a823;
	width: 80px;
	height: 35px;
	border: 1px #aaa solid;
	color: #fff;
}

input, textarea, select, button {
	font-size: 16px;
	height: 28px;
}

.datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber,
	.datagrid-cell-rownumber {
	font-size: 16px;
}

.datagrid-header .datagrid-cell span, .panel-body {
	font-size: 16px;
}

.datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber,
	.datagrid-cell-rownumber {
	height: 28px;
	line-height: 28px;
	padding: 3px 5px;
}
</style>
<script type="text/javascript">
	$(function() {
		document.onkeydown = function(e) {
			var ev = document.all ? window.event : e;
			if (ev.keyCode == 13) {
				doQuery();
			}
		}

		//设置datagrid属性
		setDatagrid();
		var dtOpts = $("#details-datagrid").datagrid("options");
		dtOpts.url = "/cbtconsole/paycheckc/queryPaymentDetails.do";

		var orderNo = "${param.orderNo}";
		if (!(orderNo == null || orderNo == "")) {
			$("#query_order_no").val(orderNo);
			doQuery();
		}

	});

	function setDatagrid() {

		$('#details-datagrid').datagrid({
			title : '订单支付详情',
			//align : 'center',
			width : "100%",
			fit : true,//自动补全 
			striped : true,//设置为true将交替显示行背景。
			collapsible : true,//显示可折叠按钮
			url : '',//url调用Action方法
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			loadMsg : '数据装载中......',
			singleSelect : true,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
			rownumbers : true,
			style : {
				padding : '8 8 10 8'
			},
			onLoadError : function() {
				$.messager.alert("提示信息", "获取数据信息失败");
				return;
			}
		});
	}

	function doQuery() {
		var orderNo = $("#query_order_no").val();
		if (orderNo == null || orderNo == "") {
			$.messager.alert("提示信息", "请输入订单号");
		} else {
			$("#details-datagrid").datagrid("load", {
				"orderNo" : orderNo
			});
		}
	}

	function doReset() {
		$("#query_form")[0].reset();
	}

	function formatPayType(val, row, index) {
		if (val == '0') {
			return 'PayPal';
		} else if (val == '1') {
			return 'Wire Transfer';
		} else if (val == '2') {
			return '余额支付';
		} else if (val == '5') {
            return 'stripe支付';
        } else {
			return '';
		}
	}
</script>
</head>
<body>


	<div id="top_toolbar" style="padding: 5px; height: auto">
		<form id="query_form" action="#" onsubmit="return false;">
			&nbsp;&nbsp;&nbsp;&nbsp;订单号:<input style="width: 180px;" id="query_order_no" type="text" value="" />
			&nbsp;&nbsp;&nbsp;&nbsp;<input class="but_color" onclick="doQuery()" value="查询" type="button">
			&nbsp;&nbsp;&nbsp;&nbsp;<input class="but_color" onclick="doReset()" value="重置" type="button">
		</form>
	</div>

	<table id="details-datagrid" style="width: 100%; height: 100%"
		class="easyui-datagrid">
		<thead>
			<tr>
				<th data-options="field:'orderNo',width:'100px',align:'center'">订单号</th>
				<th
					data-options="field:'payType',width:'100px',halign:'center',formatter:formatPayType">支付渠道</th>
				<th data-options="field:'paymentAmount',width:'100px',halign:'center'">支付金额</th>
				<th data-options="field:'currency',width:'100px',halign:'center'">货币单位</th>
				<th data-options="field:'paymentNo',width:'150px',halign:'center'">交易号</th>
				<th data-options="field:'paymentTime',width:'100px',halign:'center'">支付时间</th>
				<th data-options="field:'paymentEmail',width:'130px',halign:'center'">PayPal邮箱</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>

</body>
</html>