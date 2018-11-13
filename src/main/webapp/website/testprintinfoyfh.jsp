<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/table.css">
<title>Insert title here</title>
<style type="text/css">
.text1{border:0;border-bottom:1 solid black;background:;}
.someclass {top:3px;left:4px;position:relative;
}

</style>

<script type="text/javascript">
function test(){
	window.print();
}
</script>

</head>
<body>
<div onclick="test()" id="printdiv" style="width: 750px;height: 400px;top: 20;left: 23%;">
	<div style="padding-top: 6%; padding-left: 3%;height:20px;">
		<span style="font-size: 14px;font-family: 宋体;" id="adminna">${uod.adminname }</span>
		<span style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 8%" id="userna">${uod.userName }</span></div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;height:20px;" id="adminph" >${uod.adminphone }</div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;height:20px;" id="admincomp" >${uod.admincompany }</div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;width: 250px; height: 40px; word-wrap:break-word;" id="adminaddre">${uod.adminaddress}</div>
	<div style="padding-top: 0.4%; padding-left: 3%;height:20px;">
		<span style="font-size: 14px;font-family: 宋体;height:20px;" id="admincit" >${uod.admincity }</span>
		<span style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;height:20px;" id="adminzon" >${uod.adminzone }</span>
		<span style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 7%;height:20px;" id="admincod" >${uod.admincode }</span></div>
	<br><br>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 2%; padding-left: 3%;height:20px;" id="usernam" >${uod.userName }</div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;height:20px;" id="userph" >${uod.userphone }</div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;height:20px;" id="usercmp" >${uod.usercompany }</div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;width: 250px; height: 40px; word-wrap:break-word;" id="ueseraddre">${uod.useraddress }</div>
	<div style="padding-top: 0.4%; padding-left: 3%;height:20px;">
		<span style="font-size: 14px;font-family: 宋体;height:20px;" id="usercit" >${uod.usercity }</span>
		<span style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 5%;height:20px;" id="userzon" >${uod.userzone }</span>
		<span style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 8%;height:20px;" id="usercod" >${uod.usercode }</span></div>
</div>


</body>
</html>
<%
request.getSession().removeAttribute("uod");
%>