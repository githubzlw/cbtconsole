<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<title>订单信息补录</title>
</head>
<script type="text/javascript">
	function confirmnamebtn() {
		var paytypeflag = $("#paytype option:selected").attr("id");
		var userpass = $("#userpass").val();
		var confirmprice = $("#confirmprice").val();
		var tradingencoding = $("#tradingencoding").val();
		var isPayFreight = $("#isPayFreight").get(0).checked;
		if(confirmprice.trim() == ""){
			alert("支付金额不能为空!");
			$("#confirmprice").focus();
			return;
		}
		if (userpass.trim() == "") {
			alert("请输入密码以确认订单信息!");
			$("#userpass").focus();
			return;
		} 
		if(tradingencoding.trim() == ""){
			alert("交易编码不能为空!");
			$("#tradingencoding").focus();
			return;
		}
		else {
			$.post("/cbtconsole/cbt/orderws/addPaymentConfirm", {
				orderNo : '${param.orderNo}',
				userId : '${param.userid}',
				userpass : userpass,
				paytypeid : paytypeflag,
				confirmprice : confirmprice,
				tradingencoding : tradingencoding,
				isPayFreight : isPayFreight
			}, function(result) {
				if (result.code == 0) {
					window.opener.fnPaymentConfirmClose(result.confirmtime,
							result.confirmname);
					window.close();
				} else {
					alert(result.msg);
				}
			});
		}
	}
</script>
<body>
	<div>
		<br>
		<br>
		<table>
			<tr>
				<td>支付类型：</td>
				<td><select id="paytype" name="paytype"
					onchange="getPayType(this.options[this.selectedIndex].id)">
						<option id="pay_0" value="PayPal" selected="selected">PayPal</option>
						<option id="pay_1" value="Wire Transfer">Wire Transfer</option>
						<option id="pay_2" value="yue pay">余额支付</option>
				</select></td>
				<td>支付金额:</td>
				<td><input type="text" name="confirmprice" id="confirmprice"/></td>
			</tr>
			<tr>
				<td>用户密码：</td>
				<td><input type="password" name="userpass" id="userpass" /></td>
				<td>交易编码：</td>
				<td><input type="text" name="tradingencoding" id="tradingencoding" /></td>
			</tr>
			<tr>
				<td style="text-align: center;"><input type="checkbox" name="isPayFreight" id="isPayFreight"></td>
				<td>是否包含运费</td>
			</tr>
			<tr>
				<td colspan=4><input type="button"
					style="width: 150px; margin-left: 148px;"
					onclick="confirmnamebtn();" value="确认" /></td>
			</tr>
		</table>
	</div>
</body>
</html>