<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>提醒支付运费</title>
</head>
<script type="text/javascript">
	function fnSendEmail() {
		var orderNo = '${param.orderNo}';
		var currency = '${param.currency}';
		var actual_ffreight = '${param.actual_ffreight}';//实际运费
		var ys_ffreight = '${param.ys_ffreight}';//原先运费
		var pay_ffreight = '${param.pay_ffreight}';//支付运费金额
		var weight = '${param.weight}';//原本重量
		var actual_weight = '${param.actual_weight}';//实际重量
		var arrive_time = '${param.arrive_time}';//到货日期
		var transport_time = '${param.transport_time}';//国际运输时间
		var remark = $("#remark").val();
		var price = $("#price").val();
		var toEmail = '${param.toEmail}';
		var userid = '${param.userid}';
		var copyEmail = $("#copyEmail").val();
		$("#sendemail").attr("disabled", "disabled");
		$.post("/cbtconsole/orderDetails/sendEmailFright.do", {
			orderNo : orderNo,
			remark : remark,
			price : price,
			toEmail : toEmail,
			actual_ffreight : actual_ffreight,
			ys_ffreight : ys_ffreight,
			currency : currency,
			pay_ffreight : pay_ffreight,
			weight : weight,
			actual_weight : actual_weight,
			arrive_time : arrive_time,
			transport_time : transport_time,
			copyEmail : copyEmail,
			userid : userid
		}, function(data) {
			if (data.ok) {
				alert("发送成功");
			} else {
				alert(data.message);
			}
		});
	}
</script>
<body>
	<div>
		<div>
			&nbsp;发件人：<input type="text" value="${param.copyEmail}"
				id="copyEmail">
		</div>
		<br>
		<div>
			运费减免：<input type="text" value="" id="price">
		</div>
		<br>
		<div>
			预计到货时间:<input type="text" readonly="readonly"
				value="${param.arrive_time}">
		</div>
		<div>
			国际运输时间:<input type="text" readonly="readonly"
				value="${param.transport_time}">
		</div>
		<div>
			&nbsp;&nbsp;注释：
			<textarea rows="4" cols="24" id="remark"></textarea>
		</div>
		<br>
		<div>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button"
				id="sendemail" value="发送" onclick="fnSendEmail();">
		</div>
	</div>
</body>
</html>