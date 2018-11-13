<%@page import="com.cbt.util.UUIDUtil"%>
<%@page import="com.cbt.website.service.OrderSplitServer"%>
<%@page import="com.cbt.website.service.IOrderSplitServer"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>发送邮件</title>
</head>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript">
function fnSendEmail(){
 	 var reason3 = $("#reason3").val();
	var reason = "";
	if($("#reason1").prop("checked")){
		reason = "process delay";
	}
	if($("#reason2").prop("checked")){
		if(reason != "")reason += "/";
		reason += "partial shipment";
	}
	if(reason3 != ""){
		if(reason != "")reason += "/";
		reason += reason3;
	}
	var reasonHTML = $("#Reason").html();
	$("#Reason").html(reason);
//	var freight = $("#freight").html();
	var emailInfo = "";
//	var actual_ffreight = $("#actual_ffreight").val();
//	var actual_ffreight1 = $("#actual_ffreight1").val();
	var titleinfo = $("#titleinfo").val();
	if($("#type").val() == 1){
/*  		if(actual_ffreight == "" || actual_ffreight1 == ""){
			alert("请输入两个运费框");
			return;
		}
		$("#freight").html('${param.currency}'+actual_ffreight+" to " + '${param.currency}'+actual_ffreight1);  */
		emailInfo = $("#sendemail").html();
		if(titleinfo == ""){
			alert("邮件标题不能为空");
			return;
		}
	}else if($("#type").val() == 3){
		if(titleinfo == ""){
			alert("邮件标题不能为空");
			return;
		}
		var textinfo = $("#textinfo").val();
		if(textinfo == ""){
			alert("邮件内容不能为空");
			return;
		}
		textinfo=textinfo.replace(/\n/g,"<BR/>");
		$("#sendemailInfo3").html(textinfo);
		
	}
	var textinfo = $("#textinfo").val();
	textinfo=textinfo.replace(/\n/g,"<BR/>");  
	$("#sendemailInfo3").html(reason);
	emailInfo = $("#sendemail3").html();
	var emailaddress = $("#emailaddress").val();
	$("#send_bt").attr("disabled","disabled");
	var copyEmail = $("#copyEmail").val();
	var orderNo = $("#orderNo").val();
	var userId = $("#userId").val();
	
	$.post("/cbtconsole/WebsiteServlet",
  			{action:'chaPsendEmail',className:'SendEmailServlet',emailInfo : emailInfo,email:emailaddress,copyEmail:copyEmail,orderNo:orderNo,userId:userId,title:titleinfo},
  			function(res){
				if(res>0){
					alert("发送成功");
					window.close();
				}else{
					alert("发送失败");
				 	$("#send_bt").removeAttr("disabled");
					$("#Reason").html(reasonHTML);
					$("#freight").html(freight);
					$("#sendemailInfo3").html('<textarea id="textinfo" rows="25" cols="140"></textarea>');
				}
  	});
}
 function fn(val){
	if(val == 2){
		$("#sendemail1").show();
		$("#sendemail").hide();
		$("#sendemail3").hide();
		$("#titleinfo").hide();
	}else if(val == 1){
		alert(1);
		$("#sendemail").show();
		$("#sendemail1").hide();
		$("#sendemail3").hide();
		$("#titleinfo").show();
		$("#titleinfo").val("Your ImportExpress shipping discount!");
	}else if(val == 3){
		$("#sendemail3").show();
		$("#sendemail1").hide();
		$("#sendemail").hide();
		$("#titleinfo").show();
		$("#titleinfo").val("");
	}
	else if(val == 4){
		$("#sendemail4").show();
		$("#sendemail1").hide();
		$("#sendemail3").hide();
		$("#sendemail").hide();
		$("#titleinfo").show();
		$("#titleinfo").val("Your ImportExpress order paymant is currently under PAYPAL's varification!");
	}
} 
</script>
<% 
String userId = request.getParameter("userId");
IOrderSplitServer splitServer = new OrderSplitServer();
String email = splitServer.getUserEmailByUserName(Integer.parseInt(userId)); 

String uuid = UUIDUtil.getEffectiveUUID(Integer.parseInt(userId), "");
String url = UUIDUtil.getAutoLoginPath("/orderInfo/getChangeProduct?flag=1&orderNo=", uuid);
%>
<body>
	收件人:<input type="text" id="emailaddress" value="<%=email%>">&nbsp;&nbsp;&nbsp;
	发件人:<input type="text" id="copyEmail" value="${param.sendEmail}">&nbsp;&nbsp;&nbsp;
	<input type="hidden" id="orderNo" value="${param.orderno}">
	<input type="hidden" id="userId" value="${param.userId}">
	<br>标题:<input style="width: 260px;" type="text" lang="100" value="Your ImportExpress order has substitutes need your confirm before purchase!" id="titleinfo">
	<input type="button" id="send_bt" value="确认并发送邮件" style="font-size: 14px;font-weight: bold;" onclick="fnSendEmail();"><br>
	邮件显示内容：
	<br>
	<div id="sendemail1" style="display: none;">后台自动生成内容，不显示。</div>
	<div id="sendemail">
		<div style="width: 950px;font-size: 13px;">
<a href="http://www.import-express.com" target="_blank"><img style="cursor: pointer" src="https://img1.import-express.com/importcsvimg/webpic/newindex/img/logo.png"><img></a>
<div style="font-weight: bolder;margin-bottom: 10px;">Dear <%=email%>,</div>
	<div>Order#:${param.orderno}</div>
	<div id="sendemailInfo">
		
		Unfortunately, we are not able to buy the following items for you.  We are suggesting alternatives for your review.<br />
		<a style="color: #0070C0" href="http://www.import-express.com/orderInfo/getChangeProduct?flag=1&orderNo=${param.orderno}" target="_blank">Click here for details.</a>
		<br />
		<span id="Reason">
		<!-- Due to the <span id="Reason"><input type="checkbox" id="reason1" value="delay">process delay/<input id="reason2" type="checkbox" value="partial shipment">partial shipment/ -->
		<textarea id="reason3" rows="1" cols="89"></textarea></span>
		<%-- </span>, we will decrease the freight cost for this order from <span id="freight">${param.currency}<input type="text" value="${param.actual_ffreight}" id="actual_ffreight"> to ${param.currency}<input type="text" value="${param.actual_ffreight1}" id="actual_ffreight1"></span>.
		<br>Apologize for the inconvenient and hope you could accept our freight cost deduction as compensation. --%>
		
	</div>
	<br>
	<div style="font-weight: bolder;">Best regards,</div>
	<div style="font-weight: bolder;margin-bottom: 10px;">Import-Express.com</div>
	<div style="border: 1px solid #BDBDBD;background-color: #EBEBEB;padding-left: 10px;width: 950px;"><div style="margin-bottom: 10px;font-weight: bolder;color: red;">PLEASE NOTE:</div><div style="margin-bottom: 10px;font-size: 13px;">This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style="color: #0070C0" href="http://www.import-express.com<%=url %>" target="_blank">here</a>.</div></div>
</div>
</div>
<br>
<div id="sendemail3" style="display: none;">
<div style="width: 950px;font-size: 13px;">
<a href="http://www.import-express.com" target="_blank"><img style="cursor: pointer" src="https://img1.import-express.com/importcsvimg/webpic/newindex/img/logo.png"><img></a>
<div style="font-weight: bolder;margin-bottom: 10px;">Dear <%=email%>,</div>
	<div>Order#:${param.orderno}</div>
		Unfortunately, we are not able to buy the following items for you.  We are suggesting alternatives for your review.<br />
		<a style="color: #0070C0" href="http://www.import-express.com/orderInfo/getChangeProduct?flag=1&orderNo=${param.orderno}" target="_blank">Click here for details.</a>
		
		<br />
	<div id="sendemailInfo3">
<textarea id="textinfo" rows="25" cols="140"></textarea>
</div>
<br>
	<div style="font-weight: bolder;">Best regards,</div>
	<div style="font-weight: bolder;margin-bottom: 10px;">Import-Express.com</div>
	<div style="border: 1px solid #BDBDBD;background-color: #EBEBEB;padding-left: 10px;width: 950px;"><div style="margin-bottom: 10px;font-weight: bolder;color: red;">PLEASE NOTE:</div><div style="margin-bottom: 10px;font-size: 13px;">This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style="color: #0070C0" href="http://www.import-express.com<%=url %>" target="_blank">here</a>.</div></div>
</div>
<br>
	</div>
<%-- <div id="sendemail4" style="display: none;">
<div style="width: 950px;font-size: 13px;">
<a href="http://www.import-express.com" target="_blank"><img style="cursor: pointer" src="http://www.import-express.com/cbtconsole/img/logo.png"><img></a>
<div style="font-weight: bolder;margin-bottom: 10px;">Dear <%=email%>,</div>
	<div>Order#:${param.orderno}</div>
	<div id="sendemailInfo4">
Thanks for your order, your paymant is currently under PAYPAL's varification and it may take few days for them to release the payment. Our purchase process will start as soon as we got PAYPAL's comfirmation. Thanks for your paitient and you may go to your <a href="/cbtconsole/processesServlet?action=getCenter&className=IndividualServlet">accout</a> to check the order status. 
</div>
<br>
	<div style="font-weight: bolder;">Best regards,</div>
	<div style="font-weight: bolder;margin-bottom: 10px;">Import-Express.com</div>
	<div style="border: 1px solid #BDBDBD;background-color: #EBEBEB;padding-left: 10px;width: 950px;"><div style="margin-bottom: 10px;font-weight: bolder;color: red;">PLEASE NOTE:</div><div style="margin-bottom: 10px;font-size: 13px;">This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account <a style="color: #0070C0" href="/cbtconsole/processesServlet?action=getCenter&className=IndividualServlet" target="_blank">here</a>.</div></div>
</div>
<br>
	</div> --%>
</body>
</html>