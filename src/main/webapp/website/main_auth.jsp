<%@page import="com.cbt.util.Cache"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@page import="java.util.List"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>权限管理</title>
<style type="text/css">
.usebtn{margin:5px 0;}
.usecon{width:700px;margin:0 auto;}
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript">

	var userName;
	function fn() {
        $.ajax({
            type: "POST",//方法类型
            url:'/cbtconsole/userinfo/findAllUser',
            data:{
            },
            dataType:"json",
            success:function(result) {
                if(result != null && result.length>0){
                    for ( var x in result) {
                        $('#user').append("<button class='usebtn' type='button' value='" + result[x] + "' onclick='btnClick(this.value)'>" + result[x] + "</button><br/>");
                    }
                }
            }
        });
	}

	function btnClick(admName) {
		userName = admName;
		var obj = document.getElementsByName("auth");
		for ( var k in obj) {
			obj[k].checked = false;
		}
		$.post("/cbtconsole/AdmUserServlet?action=findAdmuserAuth", {
			'admName' : admName
		}, function(result) {
			for ( var x in result) {
				var auid = "checkbox"+result[x];
				document.getElementById(auid).checked=true;
			}
		});
	}
	
	function updateAuth() {
		if (userName == undefined || userName == "" || userName == null) {
			alert("请选择需要分配权限的用户");
			return;
		}
		var obj = document.getElementsByName("auth");
		var checkAuth = [];
		for ( var k in obj) {
			if (obj[k].checked) {
				checkAuth.push(obj[k].value);
			}
		}
		if (checkAuth == undefined || checkAuth.length == 0 || checkAuth == null) {
			alert("请选择将为用户分配的权限");
			return;
		}
		var str = checkAuth.join("'");
		$.post("/cbtconsole/AdmUserServlet?action=update", {
			'authId' : str,
			'admName' : userName
		}, function(result) {
				alert(result.message);
				location.reload();
			});
	}
	
</script>
</head>
<body onload="fn()">
	<div class="usecon">
		<form style="width: 300px; float: left;text-align:left">
			<fieldset style="height:600px;overflow: auto;">
				<legend>用户</legend>

				<span id="user"></span>
			</fieldset>
		</form>
		<form style="width: 300px; float: left;text-align:left;margin-left:20px;">
			<fieldset style="height:600px;overflow: auto;">
				<legend id="auth">权限</legend>
				<%
				List<AuthInfo> authlist = new ArrayList<AuthInfo>(); 
				authlist = Cache.getAllAuth();
		
				for(int i=0;i<authlist.size();i++){
				AuthInfo authinfo = (AuthInfo)authlist.get(i);
			%>
			<input type="checkbox" style="margin-top:10px;" name="auth" class="auth" id="checkbox<%=authinfo.getAuthId() %>" value="<%=authinfo.getAuthId() %>" /><%=authinfo.getAuthName() %><br/>
			<%	} %>
				
			</fieldset>
		</form>
		<div style="clear:both;padding-top:30px;margin-left:auto;margin-right:auto;width:100px;" ><button name="update" onclick="updateAuth()">commit</button></div>
	</div>
</body>
</html>