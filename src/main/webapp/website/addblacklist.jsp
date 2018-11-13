<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>录入黑名单</title>
<script type="text/javascript">
$(function(){
	$("#button").click(function(){
		var email=$("#email").val();
		var userip=$("#userip").val();
		var username=$("#username").val();
		//var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/; 
		/* if(email.trim()==""){
			alert("邮箱不能为空");
			$("#email").focus();
			return false;
		}else if(!reg.test(email)){
			alert("邮箱格式错误");
			return false;
		}else{ */
			$.post("/cbtconsole/BlackListServlet",{action: "addblacklist",email:email,userip:userip,username:username},function(result){
			    var json=eval('('+result+')');
			    var code=json[0].code;
			    alert(json[0].msg);
			    if(code==1){
			    	 window.location.href="/cbtconsole/BlackListServlet?action=blacklist&page=1";
			    }			   
			});
		/* } */
	});	
});
</script>
</head>
<body>
<div align="center">
    <div>
        <span>录入黑名单</span>
    </div>
	<div>
			<table>
					<tr><td>邮箱:&nbsp;<input type="text" id="email" name="email" value="${param.email}" ></td></tr>
					<tr><td>ip:&nbsp;&nbsp;<input type="text" id="userip" name="userip" value="${param.userip}" ></td></tr>
					<tr style="display: none"><td>用户名:<input type="text" id="username" name="username" value="${param.username}" ></td></tr>
					<tr><td><input type="button" id="button" name="button" value="提交" /></td></tr>
			</table> 
	</div>
</div>
</body>
</html>