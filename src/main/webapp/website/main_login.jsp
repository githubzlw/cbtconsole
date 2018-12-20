<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.cbt.util.*"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>电商后台管理系统登录</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
.panel-title {
	font-size: 26px;
	height: 30px;
}
</style>
</head>
<body onload="init();">
	<div style="margin-left: 700px; margin-top: 240px;">

		<div class="easyui-panel" title="电商后台管理系统登录"
			style="width: 500px; font-size: 28px; background: rgb(140, 228, 111);">
			<!-- <h3 align="center">电商后台管理系统登录</h3> -->
			<br>
			<div style="padding: 10px 60px 20px 60px;">
				<form id="ff" class="easyui-form" method="post">
					<table>
						<tr>
							<td>用户名:</td>
							<td><input
								style="width: 182px; height: 30px; font-size: 24px;"
								class="easyui-validatebox" type="text" id="username"
								name="username"></input></td>
						</tr>
						<tr>
							<td>密&nbsp;&nbsp;&nbsp;码:</td>
							<td><input
								style="width: 182px; height: 30px; font-size: 24px;"
								class="easyui-validatebox" type="password" id="pwd" name="pwd"
								onkeypress="if (event.keyCode == 13){submitForm()}"></input></td>
						</tr>
						<tr>
							<td></td>
							<td><br>
							<input style="width: 166px; height: 33px; font-size: 26px;"
								class="easyui-linkbutton" onclick="submitForm()" value="登录" />
								<div class="login_info" style="color: red;"></div></td>
						</tr>
					</table>
				</form>
			</div>
		</div>
	</div>
	<script>
        fnAutoLoginByCookie();
		function init() {
			$("#username").focus();
		}
		function submitForm() {
			var userName = $('#username').val();
			var passWord = $('#pwd').val();
			if (!userName) {
				$('.login_info').html("用户名不能为空！");
				$('.login_info').show();
			} else if (!passWord) {
				$('.login_info').html("密码不能为空！");
				$('.login_info').show();
			} else {

				$
						.ajax({
							type : 'POST',
							url : '/cbtconsole/userLogin/checkUserInfo.do',
							data : {
								'userName' : userName,
								'passWord' : passWord
							},
							success : function(data) {
								if (data.ok) {
									window.location = "/cbtconsole/website/main_menu.jsp";
								} else {
									$('.login_info').html(data.message);
									$('.login_info').show();
								}
							}
						});
			}
		}
	function fnAutoLoginByCookie() {
        $.ajax({
                type : 'POST',
                url : '/cbtconsole/userLogin/autoLoginByCookie.do',
                success : function(data) {
                    if (data.ok) {
                        window.location = "/cbtconsole/website/main_menu.jsp";
                    } else {
                        /*window.location = "/cbtconsole/userLogin/loginOut.do";*/
                        $('.login_info').html(data.message);
                        $('.login_info').show();
                    }
                }
            });
    }
	</script>
</body>
</html>