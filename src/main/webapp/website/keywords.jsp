<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>keywords</title>
<style type="text/css">
.red{
  color: red;
}
</style>
</head>
<script>
	
    var tycsave=[]; 
	$(function(){
		if('${map.msg}' != ''){
			alert('${map.msg}');
		}
		$("#wh").click(function(){
			var zc=$("#zc").val();
			if(zc.trim() == ""){
				alert("主词必填");
				return;
			}
			var tyc=$("#tyc").val();
			if(tyc.trim() == ""){
				alert("同义词必填");
				return;
			}
			tyc=tyc.replace(/，/ig,','); 
			var tycs=tyc.split(",");
			for(var i in tycs){
				$("#tyc2").append('<span style="width:44px; height:20px;float:left; text-align: center; cursor:pointer;" onclick="checktyc(this)">'+tycs[i]+'</span>');
			}
			$("#zc").attr("disabled","disabled");
			$("#tyc").attr("disabled","disabled");
			$("#wh").attr("disabled","disabled");
			$("#content").show();
		});
		
		$("#add").click(function(){
			var write=$("#write").val();
			write=write.replace(/，/ig,','); 
			var writelist=write.split(",");
			if(writelist.length <= 1){
				$("#tyc2").append('<span style="width:44px; height:20px;float:left; text-align: center; cursor:pointer;" onclick="checktyc(this)">'+$("#write").val()+'</span>');
			}else{
				for(var i in writelist){
					$("#tyc2").append('<span style="width:44px; height:20px;float:left; text-align: center; cursor:pointer;" onclick="checktyc(this)">'+writelist[i]+'</span>');
				}
			}			
		});
		
		$("#save").click(function(){
			$("#tyc2 span").each(function(){	
				if($(this).attr("class") == "red"){
					tycsave.push($(this).text());
				}
			});
			if(tycsave.length==0){
				alert("必须选择一个同义词");
				return;
			}
			showLoading();
			var zc=$("#zc").val();
			var list=tycsave.join();
			$("#zchidden").val(zc);
			$("#tychidden").val(list);
			$("#form").submit();
		});
	});
	
	function checktyc(obj){
		$(obj).toggleClass("red");	
	}
	
	function showLoading(){
		cDiv();
	    document.getElementById("over2").style.display = "block";
	    document.getElementById("layout2").style.display = "block";
	}
	
	function cDiv(){
		var divObj=document.createElement("div"); 
		divObj.innerHTML="<div id='over2' style='display: none;position: absolute;top: 0;left: 0;width: 100%;height: 150%;background-color: #f5f5f5;opacity:0.5;z-index: 1000;'></div>" +
				                                        " <div id='layout2' style=' display: none; position: absolute;top: 40%;left: 40%;width: 20%;height: 20%;z-index: 1001;text-align:center;'><img src='/cbtconsole/img/loading/loading.gif' /></div>";
		var first=document.body.firstChild;//得到页面的第一个元素 
		document.body.insertBefore(divObj,first);//在得到的第一个元素之前插入 
	}
	
</script>
<body>
	<div align="center"><h1>词管理</h1></div>
	<div align="center">
	    <form id="form" action="/cbtconsole/cbt/keywords/update" method="post">
	      <input type="hidden" name="zc" id="zchidden">
	      <input type="hidden" name="tyc" id="tychidden">
	    </form>
		<table border="1" cellpadding="0" cellspacing="0" width="300px">
			<thead>
				<tr>
					<th>录入</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>主词:&nbsp;<input type="text" style="width:202px" id="zc"></td>
				</tr>
				<tr>
					<td>同义词:<textarea style="width:200px; height: 150px; max-width: 200px; max-height: 200px;" id="tyc"></textarea></td>
				</tr>
				<tr>
					<td>&nbsp;&nbsp;&nbsp;<input type="button" value="维护" id="wh"></td>
				</tr>
			</tbody>
		</table>
		<table border="1" id="content" cellpadding="0" cellspacing="0" width="300px" style="display: none">
			<thead>
				<tr>
					<th>选择结果</th>
				</tr>
			</thead>
			<tbody>
			     <tr>
					<td style="width:200px; height: 150px;" id="tyc2"></td>
				</tr>
				<tr>
					<td style="text-align: center"><input type="button" value="保存结果" id="save"><input type="text" value="输入同义词" id="write" onfocus="this.value=''" onblur="if(this.value.length == 0)this.value='输入同义词'"><input type="button" value="添加" id="add"></td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>