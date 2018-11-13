<%@ page import="com.cbt.warehouse.util.StringUtil" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>到账情况查询</title>
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
	<%
	 String u_id=request.getParameter("u_id");
	 String s_time=request.getParameter("s_time");
	 String e_time=request.getParameter("e_time");
	 u_id= StringUtil.isBlank(u_id)?"":u_id;
	 s_time= StringUtil.isBlank(s_time)?"":s_time;
	 e_time= StringUtil.isBlank(e_time)?"":e_time;
	%>
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
		var opts = $("#main-datagrid").datagrid("options");
		opts.url = "/cbtconsole/paycheckc/queryForList.do";
		var userid = "${param.userid}";
		if (!(userid == null || userid == 0 || userid == "" || userid == "0" || !userid)) {
			$("#query_userid").val(userid);
		}
		var payType = "${param.payType}";
		if (!(payType == null || payType == "" || !payType)) {
			$("#query_payType").val(payType);
		}
        var u_id='<%=u_id%>';
        var s_time='<%=s_time%>';
        var e_time='<%=e_time%>';
        if(u_id != "" && s_time != "" && e_time != ""){
			$("#query_userid").val(u_id);
			$("#query_beginDate").val(s_time);
            $("#query_endDate").val(e_time);
            doQuery();
        }

	});

	function setDatagrid() {
		$('#main-datagrid').datagrid({
			title : '到账情况查询',
			//align : 'center',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20, 30, 50, 100 ],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '',//url调用Action方法
			loadMsg : '数据装载中......',
			singleSelect : true,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
			pagination : true,//分页
			rownumbers : true,
			showFooter : true,//底部统计显示
			style : {
				padding : '8 8 10 8'
			},
			onLoadError : function() {
				$.message.alert("提示信息", "获取数据信息失败");
				return;
			}
		});

	}

	function doQuery() {
		var userid = $("#query_userid").val();
		var beginDate = $("#query_beginDate").val();
		var endDate = $("#query_endDate").val();
		var email = $("#query_email").val();
		var paymentEmail = $("#query_paymentEmail").val();
		var payType = $("#query_payType").val();
		var ordersum = $("#query_ordersum").val();
        var payId = $("#query_pay_id").val();


		$("#main-datagrid").datagrid("load", {
			"userid" : userid,
			"beginDate" : beginDate,
			"endDate" : endDate,
			"email" : email,
			"paymentEmail" : paymentEmail,
			"payType" : payType,
			"ordersum" : ordersum,
            "payId" : payId
		});
	}

	function selectChange(obj) {

		//debugger;
		var day = $(obj).val();
		var mydate = new Date();

		if (mydate.getDate() >= day) {

			var end = "" + mydate.getFullYear() + "-";
			end += (mydate.getMonth() + 1) + "-";
			end += (mydate.getDate() - day + 1);
		} else {
			var end = "" + mydate.getFullYear() + "-";
			end += (mydate.getMonth()) + "-";
			end += (30 + (mydate.getDate() - day + 1));
		}

		var begin = "" + mydate.getFullYear() + "-";
		begin += (mydate.getMonth() + 1) + "-";
		begin += mydate.getDate();

		$("#query_beginDate").val(end);
		$("#query_endDate").val(begin);

	}

	function formatUserId(val, row, index) {
		if (val == 0 || val == null) {
			return '';
		} else {
			return '<a target="_blank" href="/cbtconsole/userinfo/getUserInfo.do?userId='
					+ val + '">' + val + '</a>';
		}

	}

	function formatSplitFlag(val, row, index) {
		if (val == 1) {
			return '拆单';
		} else {
			return '';
		}
	}

	function formatAction(val, row, index) {

		if (row.isShow == 1) {

			return '<a target="_blank" href="/cbtconsole/website/paycheck_details_new.jsp?orderNo='
					+ row.orderNo + '">查看详情</a>';
		} else {
			return '';
		}
	}

	function doReset() {
		$("#query_form")[0].reset();
	}
</script>
</head>
<body onload="doQuery()">

	<div id="top_toolbar" style="padding: 5px; height: auto">
		<%--<div>
			<a href="/cbtconsole/paycheckc/paycheck" target="_blank">查看所有用户款项校对</a>
		</div>--%>
		<div>
			<form id="query_form" action="#" onsubmit="return false;">
				<table>
					<tr>
						<td>用户ID:</td>
						<td>最近天数</td>
						<td>新/老用户</td>
						<!-- <td>邮箱:</td>
							<td>PayPal邮箱:</td> -->
						<td>起始日期:</td>
						<td>结束日期:</td>
						<td>支付类型选择:</td>
						<td>交易号</td>
					</tr>


					<tr>
						<td><input style="width: 80px;" id="query_userid" type="text"
							value="" /></td>
						<td><select id="searchdays" style="width: 100px;"
							onchange="selectChange(this)">
								<option value="" selected="selected">--最近天数--</option>
								<option value="3">最近三天</option>
								<option value="7">最近一周</option>
								<option value="14">最近两周</option>
								<option value="30">最近一个月</option>
						</select></td>
						<td><select id="query_ordersum">
								<option value="0">全部</option>
								<option value="1">老用户</option>
								<option value="2">新用户</option>
						</select></td>
						<!-- <td><input type="text" id="query_email"
								style="width: 180px;" value="" /></td>
							<td><input id="query_paymentEmail" type="text"
								style="width: 180px;" value=""></td> -->
						<td><input type="text" id="query_beginDate"
							style="width: 150px;" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})" value="" /></td>
						<td><input type="text" id="query_endDate"
							style="width: 150px;" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})" value="" /></td>
						<td><select id="query_payType" name="paytype">
								<option value="-1">全部</option>
								<option value="0">PayPal</option>
								<option value="1">Wire Transfer</option>
								<option value="2">余额支付</option>
								<option value="5">Stripe支付</option>
						</select></td>
						<td><input style="width: 180px;" id="query_pay_id" type="text"
								   value="" /></td>
						<td><input class="but_color" onclick="doQuery()" value="查询"
							type="button"></td>
						<td><input class="but_color" onclick="doReset()" value="重置"
							type="button"></td>
					</tr>
				</table>
			</form>

		</div>
	</div>
	<table id="main-datagrid" style="width: 100%; height: 100%"
		class="easyui-datagrid">
		<thead>
			<tr>
				<th
					data-options="field:'userId',width:'100px',align:'center',formatter:formatUserId">客户ID</th>
				<th data-options="field:'orderNum',width:'100px',halign:'center'">订单数量</th>
				<th data-options="field:'orderNo',width:'100px',halign:'center'">订单号</th>
				<th data-options="field:'paymentAmount',width:'100px',halign:'center'">支付总额</th>
				<th data-options="field:'currency',width:'100px',halign:'center'">货币单位</th>
				<th data-options="field:'paymentTime',width:'150px',halign:'center'">支付时间</th>
				<th data-options="field:'payId',width:'110px',halign:'center'">交易号</th>
				<th data-options="field:'orderStateDesc',width:'100px',halign:'center'">订单状态</th>
				<th
					data-options="field:'splitFlag',width:'100px',halign:'center',formatter:formatSplitFlag">拆单情况</th>
				<th data-options="field:'action',width:'100px',formatter:formatAction">查看详情</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>



</body>
</html>