<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>修改用户email地址</title>
</head>
<script type="text/javascript">
function fnUp(){
	var email = $("#email").val();
	var userid = $("#userid").val();
	var newemail = $("#newemail").val();
	if(newemail == ""){alert("请输入新email地址");return;}
	if(userid == "" || userid == "0" ){alert("用户ID为空");return;}
	if(email == ""){alert("用户原email地址为空");return;}
	$.post("/cbtconsole/userinfo/upEmail", {
		oldemail:email,email:newemail,userid:userid
	}, function(res) {
		if(res == 1){
			alert("修改成功!");
			window.close();
		}else if(res == 2){
			alert("邮箱已存在!");
		}else if(res == 3){
			alert("请登录!");
			window.close();
		}else{
			alert("修改失败!");
		}
		
	});
}
</script>
<body>
<div>
<input type="hidden" value="<%=request.getParameter("email")%>" id="email">
<input type="hidden" value="<%=request.getParameter("uid")%>" id="userid">
	用户新的email地址:<input id="newemail" type="text">
	<br>
	<button onclick="fnUp();">确认修改</button>
</div>
</body>
</html>