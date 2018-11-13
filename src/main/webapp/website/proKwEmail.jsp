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
 		var fileName = $("#Enclosure").val();
 		var customId = $("#customId").val();
 		/* $.post("/cbtconsole/SendMailWithAttachmentServlet",
 	  			{emailInfo : emailInfo,emailaddress:emailaddress,copyEmail:copyEmail,fileName:fileName},
 	  			function(res){
 					if(res>0){
 							alert("发送成功");
 						window.close();
 					}else{
 						alert("发送失败");
 				 		$("#send_bt").removeAttr("disabled");
 				 		$("#remark_").html("").after('<div id="remark_div">Remark:<textarea id="remark" rows="3" cols="63"></textarea></div>');
 					}
 	  	}); */
 	  	showLoading(); 
        $.ajax({
        	type:'post',
        	url:"/cbtconsole/SendMailWithAttachmentServlet",
        	data:{emailInfo : emailInfo,emailaddress:emailaddress,copyEmail:copyEmail,fileName:fileName,customId:customId},
        	success:function(res){
        		 showLoading1(); 
					if(res>0){
						alert("后台正在处理发送邮件 ！！");
					}else{
						alert("发送失败");
				 		$("#send_bt").removeAttr("disabled");
				 		$("#remark_").html("").after('<div id="remark_div">Remark:<textarea id="remark" rows="3" cols="63"></textarea></div>');
					}
             }
        })
 	}
 	
 	//数据加载中的阴影区
 	function showLoading(){
 		cDiv();
 	    document.getElementById("over2").style.display = "block";
 	    document.getElementById("layout2").style.display = "block";
 	}

 	function showLoading1(){
 	    document.getElementById("over2").style.display = 'none';
 	    document.getElementById("layout2").style.display = 'none';
 	}

 	function cDiv(){
 		var divObj=document.createElement("div"); 
 		divObj.innerHTML="<div id='over2' style='display: none;position: absolute;top: 0;left: 0;width: 100%;height: 300%;background-color: #f5f5f5;opacity:0.5;z-index: 2100;'></div>" +
 				                                        " <div id='layout2' style=' display: none; position: absolute;top:20%;left:20%;width: 20%;height: 20%;z-index: 2201;text-align:center;'><img src='/cbtconsole/img/loading/loading.gif' /></div>";
 		var first=document.body.firstChild;//得到页面的第一个元素 
 		document.body.insertBefore(divObj,first); //在得到的第一个元素之前插入 
 	}
 <%	
 String sessionId = request.getSession().getId();
 String userJson = Redis.hget(sessionId, "admuser");
 Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
 	 %>
</script>
<body>
    <input type="hidden"  id="customId"  value="${customId}">
	收件人:<input type="text" id="emailaddress" value="${customEmail}">&nbsp;&nbsp;&nbsp;
	发件人:<input type="text" id="copyEmail" value="<%=user.getEmail() %>">&nbsp;&nbsp;&nbsp;
	<input type="button" id="send_bt" value="确认并发送邮件" style="font-size: 14px;font-weight: bold;" onclick="fnSendEmail();"><br>
	邮件显示内容：
	<br>
<div id="sendemail">
<div style="width: 950px;font-size: 13px;">
<a href="http://www.import-express.com" target="_blank"><img style="cursor: pointer" src="http://www.import-express.com/img/logo.png"><img></a>
<div style="font-weight: bolder;margin-bottom: 10px;">Dear ${customEmail}：</div>
	<div>
		 According to your request, we have selected a part of the goods for you, maybe you will like it.
	</div>
	    <div style="clear: both;"></div>
	<br>
	<hr style="border:1px solid #FFC167;">
	<div><a href="${url }">${url }</a></div>
	<div id="remark_"></div>
	<div id="remark_div">Remark:<textarea id="remark" rows="3" cols="63">
	</textarea></div>
	<br>
	<div style="font-weight: bolder;">Best regards,</div>
	<div style="font-weight: bolder;margin-bottom: 10px;">Import-Express.com</div>
	<div style="border: 1px solid #BDBDBD;background-color: #EBEBEB;padding-left: 10px;width: 950px;"><div style="margin-bottom: 10px;font-weight: bolder;color: red;">PLEASE NOTE:</div><div style="margin-bottom: 10px;font-size: 13px;">This email message was sent from a notification-only address that cannot accept incoming email. PLEASE DO NOT REPLY to this message. If you have any questions or concerns, please access our <a style="color: #0070C0" href="http://chat32.live800.com/live800/chatClient/chatbox.jsp?companyID=496777&configID=70901&lan=en&jid=4818862369&enterurl=http%3A%2F%2Fwww.import-express.com%2Fcbtconsole%2Fapa%2Fcontact.html&amp;timestamp=1441622560799&amp;pagereferrer=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2F&amp;firstEnterUrl=http%3A%2F%2Fwww%2Eimport-express%2Ecom%2Fcbtconsole%2Fapa%2Fcontact%2Ehtml&amp;pagetitle=Customer+Service" target="_blank">Live Chat</a>.</div><div style="margin-bottom: 10px;font-weight: bold;">Thank you for reading this letter. </div></div>
</div>
</div>
附件 :<input type="text" name="Enclosure"  id="Enclosure" value="${Enclosure }"  disabled="disabled">
<div>
<br>
</div>
</body>
</html>