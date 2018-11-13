、<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>修改用户手机号码</title>
</head>
<script type="text/javascript">
function fnUp(){
	var phone = $("#phone").val();
	var userid = $("#userid").val();
	var newphone = $("#newphone").val();
	if(newphone == ""){alert("请输入新电话");return;}
	if(userid == "" || userid == "0" ){alert("用户ID为空");return;}
	if(phone == newphone){alert("用户新旧电话一致");return;}
	$.post("/cbtconsole/userinfo/upPhone", {
		oldPhone:phone,newPhone:newphone,userid:userid
	}, function(res) {
		if(res == 1){
			alert("修改成功!");
			window.close();
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
<%-- <input type="hidden" value="<%=request.getParameter("phone")%>" id="phone"> --%>
<input type="hidden" value="<%=request.getParameter("uid")%>" id="userid">
	用户原有手机号码:<input id="phone" type="text" value="<%=request.getParameter("phone")%>"><br>
	用户新的手机号码:<input id="newphone" type="text">
	<br>
	<button onclick="fnUp();">确认修改</button>
</div>
</body>
</html>