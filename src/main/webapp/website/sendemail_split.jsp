<%@page import="com.cbt.util.Redis"%>
<%@page import="com.cbt.util.SerializeUtil"%>
<%@page import="com.cbt.pojo.Admuser"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>发送邮件</title>
</head>
<script type="text/javascript">
 	function fnSendEmail(){
 		$("#remark_").html('<span style="font-weight: bold;">Remark:</span><span>'+$("#remark").val()+'</span>');
 		$("#remark_div").remove();
 		var emailInfo = $("#sendemail").html();
 		var emailaddress = $("#emailaddress").val();
 		$("#send_bt").attr("disabled","disabled");
 		var copyEmail = $("#copyEmail").val();
 		var orderNo = $("#orderNo").val();
 		$.post("/cbtconsole/WebsiteServlet",
 	  			{action:'sendEmail',className:'OrderSplitServlet',emailInfo : emailInfo,email:emailaddress,copyEmail:copyEmail,orderNo:orderNo},
 	  			function(res){
 					if(res>0){
 						alert("发送成功");
 						window.close();
 						parent.location.reload();
 					}else{
 						alert("发送失败");
 				 		$("#send_bt").removeAttr("disabled");
 				 		$("#remark_").html("").after('<div id="remark_div">Remark:<textarea id="remark" rows="3" cols="63"></textarea></div>');
 					}
 	  	});
 	}
 <%	
 String sessionId = request.getSession().getId();
 String userJson = Redis.hget(sessionId, "admuser");
 	Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
 	 %>
</script>
<body>
	收件人:<input type="text" id="emailaddress" value="${email}">&nbsp;&nbsp;&nbsp;
	发件人:<input type="text" id="copyEmail" value="${param.email}">&nbsp;&nbsp;&nbsp;
	<input type="hidden" id="orderNo" value="${param.orderno}">
	<input type="button" id="send_bt" value="确认并发送邮件" style="font-size: 14px;font-weight: bold;" onclick="fnSendEmail();"><br>
	邮件显示内容：
	<br>
<div id="sendemail">

<div style="width: 950px;font-size: 13px;">
	<%--<h3>Your Import Express order was partially cancelled</h3>--%>
<a href="http://www.import-express.com" target="_blank"><img style="cursor: pointer" src="http://www.import-express.com/img/logo.png" /></a>
<div style="font-weight: bolder;margin-bottom: 10px;">Dear ${email},</div>
	<c:if test="${param.state == 1}">
	<div>
	&nbsp;&nbsp;Due to supply reasons, we might need more time to process your complete order and we will send you a portion of your order first. <br/>
	&nbsp;&nbsp;No more extra fee needed, just you will receive more than one delivery. If you have any problem with it, please contact your <br/>
	&nbsp;&nbsp;account manager or our 24/7 whatsapp service number 0086 13564025061. 
	</div><br>
	<div style="background-color: #E7E7E7;font-weight: bold;width: 290px;">Order Number: ${param.orderno}</div>
	<div style="color: red;">
		Items to send you first:
	</div>
	<c:forEach items="${details}" var="detail" varStatus="sd">
		<%-- <tr>
			<td>
			<img style="cursor: pointer" width="50px;" height="50px;" src="${detail[5]}"><img>
			</td>
			<td><div style="width: 435px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;" title="${detail[4]}">${detail[4]}</div></td>
			<td>${detail[2]} pc </td>
			<td align="right">USD ${detail[3]}</td>
		</tr> --%>
		<div style="width: 200px;float: left;border: 1px solid #E5E5E5;margin-left: 10px;">
			<img style="cursor: pointer;" width="200px;" height="200px;" src="${detail[5]}"><img>
			<div style="width: 200px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;" title="${detail[4]}">${detail[4]}</div>
			<div><c:if test="${not empty detail[6]}">
							<c:forEach items="${fn:split(detail[6],',')}" var="types" varStatus="i">
								<span style="border: 1px solid #E1E1E1;"><c:out value="${fn:split(fn:split(types,':')[1],'@')[0]}"></c:out></span>
							</c:forEach>
						</c:if>
						</div>
			<div style="float: left;">
				<div>${detail[2]} pc </div>
				<div>${currency} ${detail[3]}</div>
			</div>
			<div style="float: left;margin-left: 10px;width: 130px;">
						<c:if test="${not empty detail[7]}">
							<c:forEach items="${fn:split(detail[7],'@')}" var="img_type" varStatus="i">
								<c:if test="${fn:indexOf(img_type,'http') > -1}">
								<img  height="40" width="40" src="${img_type}">&nbsp;
								</c:if>
							</c:forEach>
						</c:if>
			</div>
		</div>
		<c:if test="${sd.index !=0 && sd.index % 3 ==0}"><br></c:if>
	</c:forEach>
	    <div style="clear: both;"></div>
		<div style="font-weight: bold;border-top: 1px  dashed #AAABAF;margin-top: 5px;" >
				Product Value: ${currency} ${orderbean.product_cost}&nbsp;
<%-- 			<c:if test="${orderbean.pay_price_tow != 'null' && orderbean.pay_price_tow != null}"> --%>
<%-- 				Shipping Cost ${param.state == 0?'Associated':''}:${currency} ${orderbean.pay_price_tow} --%>
<%-- 			</c:if> --%>
		</div>
		<div >Estimated Ship Time: ${param.time_}&nbsp;&nbsp;<c:if test="${expect_arrive_time_ != null && expect_arrive_time_ != ''}">Estimated Arrival Time: ${expect_arrive_time_}</c:if></div>
		<c:if test="${parat.state == 0}">
		<div >Due to the supply reason, part of your order can not be supplied and you can find the below refund amount in <a href="http://www.import-express.com${autoUrl }">Your Account</a></div>
<%-- 		<div >Due to supply reasons, we might need more time to process your complete order and we will send you a portion of your order first. No more extra fee needed, just you will received more than one delivery. If you have any problem with it, please contact your account manager or our 24/7 whatsapp service number 0086 13564025061.<a href="http://www.import-express.com${autoUrl }">Your Account</a></div> --%>
		</c:if>
		</c:if>
	<div>
	<br>
	<div style="background-color: #E7E7E7;font-weight: bold;width: 290px;color:red;">${param.state == 0 ? 'Split ' : ''}Order Number:${orderbean_.orderNo}</div>
	<c:if test="${param.state == 1}">
		Items to send you after the first batch:
		</c:if>
		<c:if test="${param.state == 0}">
		Items we can not supply: 
		</c:if>
	</div>
	<c:forEach items="${details_}" var="detail" varStatus="sd">
		<div style="width: 200px;float: left;border: 1px solid #E5E5E5;margin-left: 10px;">
			<img style="cursor: pointer;" width="200px;" height="200px;" src="${detail[5]}"><img>
			<div style="width: 200px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;" title="${detail[4]}">${detail[4]}</div>
			<div><c:if test="${not empty detail[6]}">
							<c:forEach items="${fn:split(detail[6],',')}" var="types" varStatus="i">
								<span style="border: 1px solid #E1E1E1;"><c:out value="${fn:split(fn:split(types,':')[1],'@')[0]}"></c:out></span>
							</c:forEach>
						</c:if>
						</div>
			<div style="float: left;">
				<div>${detail[2]} pc </div>
				<div>${currency} ${detail[3]}</div>
			</div>
			<div style="float: left;margin-left: 10px;width: 130px;">
						<c:if test="${not empty detail[7]}">
							<c:forEach items="${fn:split(detail[7],'@')}" var="img_type" varStatus="i">
								<c:if test="${fn:indexOf(img_type,'http') > -1}">
								<img  height="40" width="40" src="${img_type}">&nbsp;
								</c:if>
							</c:forEach>
						</c:if>
			</div>
		</div>
		<c:if test="${sd.index !=0 && sd.index % 3 ==0}"><br></c:if>
	 </c:forEach>
	    <div style="clear: both;"></div>
		<div style="border-top: 1px  dashed #AAABAF;margin-top: 5px;" >Product Value: ${currency} ${orderbean_.product_cost}
<%-- 			<c:if  test="${param.state == 1}">&nbsp; --%>
<%-- 				<c:if test="${orderbean_.pay_price_tow != 'null' && orderbean_.pay_price_tow != null}"> --%>
<%-- 					Shipping Cost: ${currency}${orderbean_.pay_price_tow} --%>
<%-- 				</c:if> --%>
<%-- 			</c:if> --%>
		</div>
		 
		<c:if test="${param.state == 0}">
		<%-- <div style="font-weight: bold;">Shipping Cost Associated: ${currency}${orderbean_.pay_price_tow}</div> --%>
		<div>Offered Discount Deduction: ${totalDisCount}</div>
		<div>Extra Shipping Fee Paid: ${totalExtraFree}</div>
		<div style="clear: both;"></div>
		<div style="font-weight: bold;border-top: 1px  dashed #AAABAF;margin-top: 5px;width: 220px;">Refund Amount: ${currency}${orderbean_.pay_price}</div>
		</c:if>
	<br>
	<hr style="border:1px solid #FFC167;">
	<div id="remark_"></div>
	<div id="remark_div">Remark:<textarea id="remark" rows="3" cols="63"></textarea></div>
	<br>
	<div style="font-weight: bolder;">Best regards,</div>
	<div style="font-weight: bolder;margin-bottom: 10px;">Import-Express.com</div>
	<div style="border: 1px solid #BDBDBD;background-color: #EBEBEB;padding-left: 10px;width: 950px;"><div style="margin-bottom: 10px;font-weight: bolder;color: red;">PLEASE NOTE:</div><div style="margin-bottom: 10px;font-size: 13px;">This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, please access our <a style="color: #0070C0" href="http://chat32.live800.com/live800/chatClient/chatbox.jsp?companyID=496777&configID=70901&lan=en&jid=4818862369&enterurl=http%3A%2F%2Fwww.import-express.com%2Fcbtconsole%2Fapa%2Fcontact.html&amp;timestamp=1441622560799&amp;pagereferrer=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2F&amp;firstEnterUrl=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2Fcbtconsole%2Fapa%2Fcontact%2Ehtml&amp;pagetitle=Customer+Service" target="_blank">Live Chat</a>.</div><div style="margin-bottom: 10px;font-weight: bold;">Thank you for reading this letter. </div></div>
</div>
</div>
<div>
<br>
</div>
</body>
</html>