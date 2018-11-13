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
		var id=$("#id").val();
		var email=$("#email").val();
		var userip=$("#userip").val();
		var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/; 
		if(email.trim()==""){
			alert("邮箱不能为空");
			$("#email").focus();
			return false;
		}else if(!reg.test(email)){
			alert("邮箱格式错误");
			return false;
		}else{
			$.post("/cbtconsole/BlackListServlet",{action:"modifyblacklist",id:id,email:email,userip:userip},function(result){
			    var json=eval('('+result+')');
			    var code=json[0].code;
			    alert(json[0].msg);
			    if(code==1){
			    	 window.location.href="/cbtconsole/BlackListServlet?action=blacklist&page=1";
			    }			   
			});
		}
	});	
});

</script>
</head>
<body>
<div align="center">
    <div>
        <span>修改黑名单</span>
    </div>
	<div>
			<table>
					<tr><td>邮箱:&nbsp;<input type="text" id="email" name="email" value="${blackList.email}" >(必须填写)</td></tr>
					<tr><td>ip:&nbsp;&nbsp;<input type="text" id="userip" name="userip" value="${blackList.userip}" ></td></tr>
					<tr style="display:none"><td><input type="hidden" name="id" id="id" value="${blackList.id}"/></td></tr>
					<tr><td><input type="button" id="button" name="button" value="提交" /></td></tr>
			</table> 
	</div>
</div>
</body>
</html>