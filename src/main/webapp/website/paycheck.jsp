<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>查询用户付款信息</title>
<style type="text/css">
body {
	width: 1800px;
	margin: 0 auto;
}

.body {
	width: 1800px;
	height: auto;
}

table {
	table-layout: fixed;
	word-wrap: break-word;
}

.payAmount {
	color: red;
	font-weight: bold;
}

.balancePay {
	color: #6A6AFF;
	font-weight: bold;
}

.qsplit {
	color: red;
}

.hsplit {
	color: #6A6AFF;
}

.payAll {
	background: #DAF3F5;
}

.balancepayorderid {
	background: rgba(228, 69, 57, 0.24);
}
</style>
<script type="text/javascript">
	
	var firstEnter = 0;

	function selSearch(sel) {

		//debugger;
		var day = $(sel).val();
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

		var str = "" + mydate.getFullYear() + "-";
		str += (mydate.getMonth() + 1) + "-";
		str += mydate.getDate();

		//$("#datestart").attr("value",end);  这种方式清空之后就不能设置了
		//$("#dateend").attr("value",str);
		$("#dateend").val(str);
		$("#datestart").val(end);
		//	dateend
	}
	function fnjump(obj) {
		var userid = $("#userid").val();
		var datestart = $("#datestart").val();
		var dateend = $("#dateend").val();
		var email = $("#email").val();
		var paymentemail = $("#paymentemail").val();
		var paytype = $("#paytype").val();
		var page = $("#page").val();
		var ordersum = $("#ordersum").val();
		if (page == '') {
			page = '1';
		}
		if (obj == 0) {
			page = $("#page").val();
		} else if (obj == -1) {
			if (page < 2) {
				return;
			}
			page = parseInt(page) - 1;
		} else if (obj == 1) {
			var totalpage = parseInt($("#totalpage").val());
			if (page > totalpage - 1) {
				return;
			}
			page = parseInt(page) + 1;
		}
		var preStr = "/cbtconsole/paycheckc/doall";
		var str = "page=" + page;
		if (userid != '') {
			str = str + "&userid=" + userid;
		}
		if (datestart != '') {
			str = str + "&datestart=" + datestart;
		}
		if (dateend != '') {
			str = str + "&dateend=" + dateend;
		}
		if (email != '') {
			str = str + "&email=" + email;
		}
		if (paymentemail != '') {
			str = str + "&paymentemail=" + paymentemail;
		}
		if (paytype != '') {
			str = str + "&paytype=" + paytype;
		}
		if (ordersum != 0) {
			str = str + "&ordersum=" + ordersum;
		}
		var url = window.location.href;
		if (url.substring(url.indexOf("?") + 1) == str) {
			if(firstEnter ==0){
				firstEnter++;
				return;
			} else{
				window.location.href = preStr + "?" + str;
			}	
		} else {
			window.location.href = preStr + "?" + str;
		}
	}

	$(function name() {
		var srt = '${param.paytype}';
		var ordersum = '${param.ordersum}';

		if (srt.trim() != "") {
			$("#paytype").val(srt);
		}
		if (ordersum.trim() != "") {
			$("#ordersum").val(ordersum);
		}
		var datestart = '${param.datestart}';
		var dateend = '${param.dateend}';
		$('#datestart').val(datestart);
		$('#dateend').val(dateend);

		document.getElementById("userid").onclick = function() {
			$("#page").val(1);
		}
		document.getElementById("email").onclick = function() {
			$("#page").val(1);
		}
		document.getElementById("paymentemail").onclick = function() {
			$("#page").val(1);
		}
		document.getElementById("datestart").onclick = function() {
			$("#page").val(1);
		}
		document.getElementById("dateend").onclick = function() {
			$("#page").val(1);
		}
		document.getElementById("paytype").onchange = function() {
			$("#page").val(1);
		}
	});
	
	function doReset(){
		$("#query_form")[0].reset();
	}
</script>

</head>
<body class="body" onload="fnjump(0)">
	<div>
		<a href="/cbtconsole/paycheckc/paycheck" target="_blank">查看所有用户款项校对</a>
	</div>
	<div align="center">
		<div>
			<a style="font-size: 25px;">查看用户付款信息</a>
			<div>
			<form id="query_form" action="#" onsubmit="return false;">
				<table>
					<tr>
						<td>用户ID:</td>
						<td>最近天数</td>
						<td>新/老用户</td>
						<td>邮箱:</td>
						<td>PayPal邮箱:</td>
						<td>起始日期:</td>
						<td>结束日期:</td>
						<td>支付类型选择:</td>
					</tr>


					<tr>
						<td><input style="width: 120px;" id="userid" type="text"
							value="${param.userid}" /></td>
						<td><select id="searchdays" style="width: 100px;"
							onchange="selSearch(this)">
								<option value="" selected="selected">--最近天数--</option>
								<option value="3">最近三天</option>
								<option value="7">最近一周</option>
								<option value="14">最近两周</option>
								<option value="30">最近一个月</option>
						</select></td>
						<td><select id="ordersum">
								<option value="0">全部</option>
								<option value="1">新用户</option>
						</select></td>
						<td><input type="text" id="email" style="width: 220px;"
							value="${param.email}" /></td>
						<td><input id="paymentemail" type="text"
							style="width: 220px;" value="${param.paymentemail}"></td>
						<td><input type="text" id="datestart" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})" value="" /></td>
						<td><input type="text" id="dateend" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})" value="" /></td>
						<td><select id="paytype" name="paytype">
								<option value="-1" flag="-1">全部</option>
								<option value="0" flag="0">PayPal</option>
								<option value="1" flag=1">Wire Transfer</option>
								<option value="2" flag="2">余额支付</option>
								<option value="4" flag="4">合并支付</option>
						</select></td>
						<td><input onclick="fnjump(0)" value="查询" type="button"></td>
						<td><input onclick="doReset()" value="重置" type="button"></td>
					</tr>
				</table>
			  </form>
			</div>
			<br>
			<table id="table" border="1" cellpadding="0" cellspacing="0">
				<tr>
					<td width="40px;">序号</td>
					<td width="70px;">用户ID</td>
					<td width="60px;">订单数量</td>
					<td width="180px;">订单号</td>
					<td width="180px;">交易号</td>
					<td width="380px;">实际付款总额</td>
					<td width="100px;">分配到本订单的支付金额</td>
					<td width="40px;">货币单位</td>
					<td width="100px;">支付渠道</td>
					<td width="260px;">PayPal邮箱</td>
					<td width="200px;">支付时间</td>
					<td width="100px;">拆单情况</td>
				</tr>

				<c:forEach items="${list}" var="payment" varStatus="i">
					<tr>
						<td>${i.index + 1}</td>
						<td><a target="_blank"
							href="/cbtconsole/paycheckc/payInfo?userid=${payment.userid}">${payment.userid}</a>
						</td>
						<td>${payment.orderSum }</td>
						<c:if test="${payment.isMainOrder==1}">
							<td class="balancepayorderid">${payment.orderid }<br />
							<em style="color: red; font-weight: bold;">(合并支付汇总单号)</em></td>
						</c:if>
						<c:if test="${payment.isMainOrder==0 }">
							<td>${payment.orderid }</td>
						</c:if>
						<td>${payment.paymentid }</td>
						<c:if test="${payment.balancePayFlag==1 }">
							<td class="payAll">${payment.payAll}<br />(Paypal &nbsp;
								Wire Transfer:<span class="payAmount">
									${payment.payAmount}</span> 余额:<span class="balancePay">
									${payment.balancePay}</span>)
							</td>
						</c:if>
						<c:if test="${payment.balancePayFlag!=1 }">
							<td>${payment.payAmount}</td>
						</c:if>

						<td>${payment.payment_amount }</td>
						<td>${payment.payment_cc }</td>
						<td>${payment.paytype}</td>
						<td>${payment.orderdesc}</td>
						<td>${payment.createtime}</td>
						<c:if test="${payment.orderSplit=='1'}">
							<td class="qsplit">前台自动拆单</td>
						</c:if>
						<c:if test="${payment.orderSplit=='2'}">
							<td class="hsplit">后台手动拆单</td>
						</c:if>
						<c:if test="${orderSplit!='1' && payment.orderSplit!='2'}">
							<td>${payment.orderSplit}</td>
						</c:if>
					</tr>
				</c:forEach>
			</table>
			<br>
			<div>
				总金额: <span>&nbsp;&nbsp;USD:${usdTotalMoney}</span> <span>&nbsp;&nbsp;EUR:${eurTotalMoney}</span>
				<span>&nbsp;&nbsp;CAD:${cadTotalMoney}</span> <span>&nbsp;&nbsp;GBP:${gbpTotalMoney}</span>
				<span>&nbsp;&nbsp;AUD:${audTotalMoney}</span> <span id="total_mony">&nbsp;&nbsp;金额总数:${allTotalMoney}</span>&nbsp;
				<span id="total_count">&nbsp;&nbsp;总条数:${count}</span>&nbsp; <span
					id="p_" style="width: 500px;">总页数:${currenpage} / ${total}</span> <input
					type="hidden" value="${total}" id="totalpage"> <input
					type="button" value="上一页" onclick="fnjump(-1)"> <input
					type="button" value="下一页" onclick="fnjump(1)"> &nbsp;第 <input
					id="page" value="${currenpage}" style="width: 50px;">页
				<button onclick="fnjump(0)">查询</button>
			</div>
		</div>
	</div>
	<br>
	<br>
</body>
</html>