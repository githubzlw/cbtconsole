<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改密码</title>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">
<style type="text/css">
.imagetable{
margin-left: auto;
margin-right: auto;}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		var admid =$("#admid").val();
		if(admid==1){
			$('.ling').css("display","block");
		}else{
			$('.notling').css("display","block");
		}
		var error = false;
		$("#oldpass").blur(function(){
			var password = $("#password").val();
			var oldpass = $("#oldpass").val();
			if(oldpass =='') {
				showError('oldpass', '密码不能为空');
				error = true;
				return;
			}else 
				if(password !=oldpass){
					showError('oldpass', '密码错误');
					error = true;	
			}else{
				$("#oldpass").css({"border-color":"green"});
				$("#oldpassTip").css({"display":"none"});
			}
		});

		$("#newpass").blur(function(){
			var newpass = $("#newpass").val();
			if(newpass == '') {
				showError('newpass', '新密码不能为空');
				error = true;
			}
			else {
				$("#newpass").css({"border-color":"green"});
				$("#newpassTip").css({"display":"none"});
			}
		});

		$("#newpassAgain").blur(function(){
			var newpass = $("#newpass").val();
			if(newpass == '') {
				showError('newpass', '新密码不能为空');
				error = true;
				return;
			}

			var newpassAgain = $("#newpassAgain").val();
			if(newpassAgain != newpass) {
				showError('newpassAgain', '与输入的新密码不一致');
				error = true;
			}
			else {
				$("#newpassAgain").css({"border-color":"green"});
				$("#newpassAgainTip").css({"display":"none"});
			}
		});
		
		$("#submit").click(function(event){
			hidecss();
			$("#oldpass").blur();
			$("#newpass").blur();
			$("#newpassAgain").blur();

			if(!error) {
				var id = $("#admid").val();			
				var password = $("#newpass").val();
				$.post('/cbtconsole/admuser/findpassword', {id:id, password:password}, function(data) {
					if(data.ok){
						$("#modifySuccess").css({'display':'inline'});
					}else{
						$("#modifyFail").css({'display':'inline'});
					}
					
				});
			}
			event.preventDefault();
			return false;
		});
		
		$(".changepsw").click(function(){
			var id = $(this).find("#id").val();			
			var password = $(this).parent().parent().find("#password").val();
			if(password==''){
				$(this).parent().parent().find("#password").css({"border-color":"red"});
			}
			$.post('/cbtconsole/admuser/findpassword', {id:id, password:password}, function(data) {
				if(data.ok){
					$("#modifySuccess").css({'display':'inline'});
				}else{
					$("#modifyFail").css({'display':'inline'});
				}
				
			});
		});
	});

	function showError(formSpan, errorText) {
		$("#" + formSpan).css({"border-color":"red"});
		$("#" + formSpan + "Tip").empty();
		$("#" + formSpan + "Tip").append(errorText);;
		$("#" + formSpan + "Tip").css({"display":"inline"});
	}
	
	function hidecss(){
		$("#modifySuccess").css({'display':'none'});
		$("#modifyFail").css({'display':'none'});
	}
</script>
</head>
<body>

<div class="container" style="margin-top:100px;">
<div id="modifySuccess" class="alert alert-success alert-dismissable" style="width:50%;margin-left:40%;display:none;">
  <strong>Success!</strong> 你已成功修改密码！
</div>
<div id="modifyFail" class="alert alert-danger alert-dismissable" style="width:50%;margin-left:40%;display:none;">
  <strong>Fail!</strong> 密码修改失败！
</div>
	<form class="form-horizontal" role="form">
	<div class="ling" style="display:none;">
	  	 <div><label>管理员信息：</label></div>
	     <table class="imagetable text-center">
	     	 <thead>
                 <tr>
                   <th width="120">用户名</th>
                   <th width="150">密码</th>
                   <th width="200">更换密码</th>
                   <th width="150">操作</th>
                 </tr>
             </thead>
             <tbody>
	             <c:forEach items="${admuserList}" var="admuser">
	                   <tr>
	                       <td>${admuser.admname}</td>
	                       <td>${admuser.password}</td>
	                       <td><input type="text" id="password" class="form-control" value=""/></td>
	                       <td><span class="btn btn-primary changepsw"><input type="hidden" id="id" value="${admuser.id}"/>修改</span></td>
	                   </tr>
	             </c:forEach>
			</tbody>
	     </table>
	  </div>
	  <div class="notling" style="display:none;">
		  <div class="form-group">
		    <label for="oldpass" class="col-sm-2 control-label">旧密码</label>
		    <div class="col-sm-10">
		      <input type="password" class="form-control" style="width:250px;" id="oldpass" maxlength="20" placeholder="Old Password"><span id="oldpassTip" style="display:none;color:red;"></span>
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="newpass" class="col-sm-2 control-label">新密码</label>
		    <div class="col-sm-10">
		      <input type="password" class="form-control" style="width:250px;" id="newpass" maxlength="20" placeholder="New Password"><span id="newpassTip" style="display:none;color:red;"></span>
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="newpassAgain" class="col-sm-2 control-label">再次确认新密码</label>
		    <div class="col-sm-10">
		      <input type="password" class="form-control" style="width:250px;" id="newpassAgain" maxlength="20" placeholder="Again New Password"><span id="newpassAgainTip" style="display:none;color:red;"></span>
		    </div>
		  </div>
		  <div class="form-group">
		    <label class="col-sm-2 control-label">  </label>
		 	 <button type="submit" class="btn btn-primary" id="submit" style="text-align:center;">确认修改</button>
		  </div>
	  </div>
	  
	  <input  id="admid" type="hidden" value="${admuser.id}"> 
	  <input type="hidden" id="password" value="${admuser.password}">
	</form>
</div>
</body>
</html>