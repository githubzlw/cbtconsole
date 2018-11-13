<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@page import="com.cbt.processes.servlet.Currency"%>
<%@page import="java.util.List"%>
<%@page import="com.cbt.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="/cbtprogram/img/mathematics1.ico" type="image/x-icon"/>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<script type="text/javascript">
<%-- <%String sessionId = request.getSession().getId();
String userJson = Redis.hget(sessionId, "admuser");
Admuser user = (Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
String roletype=user.getRoletype();
int uid = user.getId();
String admName =user.getAdmName();
pageContext.setAttribute("admName", admName);
%>  --%>

function rpage(flag){
	var total = parseInt($("#hi_total").val());
	var page = parseInt($("#hi_page").val());
	if(flag==0){
		$("#page").val(page-1);
	}else if(flag==1){
		$("#page").val(page+1);
	}
	if(parseInt($("#page").val()) > total || parseInt($("#page").val()) < 1){
		return ;
	}
	fnquery();
	
}


	function fnrefresh(id,type){
		if(type==0){
			//编辑
			var hwid = ($(document).width()- $(".main1").width())/2;
			var heig = ($(window).height()- $(".main1").height())/2;
			$(".main1").show().css({"left":hwid,"top":heig});
			$("#categoryCName").html($("#cname_"+id).html());
			$("#categoryName").val($("#ename_"+id).html().replace('&amp;','&'));
			$("#catid").html($("#catid_"+id).html());
			$("#did").val(id);
			
		}else if(type==1){
			var did = $("#did").val();
			
			confirm(did,1);
		}else if(type==2){
			$.dialog.confirm("Message","确定要删除吗?", function(){
				confirm(id,2);
			}, function(){
			});
		}
	}
	
	/* 查询 */
	function fnquery(){
		$("#r_form").submit();
	}
	
	function mhide(){
		$(".main1").hide();
	}
	
	/* 编辑*/
	function confirm(id,type){
		var ename = $('#categoryName').val();
		$.post("/cbtconsole/category/deal",
				{id:id,ename:ename},
				function(res){
					if(res>0){
						alert("成功");
						window.location.reload();
					}
					
				}); 
	}

	 function fnonfocus(obj){
	 	if(obj == null){
	 		return ;
	 	}
	 	obj.style.color='black';
	 	obj.value="";
	 }
	 function fnre(){
		 $.post("/cbtconsole/category/refresh",
					{},
					function(msg){
						alert(msg);
					});
	}
		
</script>

<style type="text/css">
.tabletitle{
	font-family : 微软雅黑,宋体;
	font-size : 2em;
	text-align:center;
	color:red;
	margin-bottom: 20px;
}
a{
	cursor: pointer;
}
.main{
    margin: 0 auto;
    width: 1200px;
    margin-top: 30px;
} 
body{
	text-align: center;
	width: 1800px;
	
}
td{
vertical-align:middle;

}

#trRecords tr td{
	line-height:20px; 
	height:80px;
    vertical-align: middle;
}
table { table-layout: fixed;word-wrap:break-word;}
.bt-query{
color: #fff;
    background-color: #5db5dc;
    border-color: #2e6da4;
    padding: 5px 10px;
    font-size: 12px;
    line-height: 2.5;
    border-radius: 3px;
    border: 1px solid transparent;
    cursor: pointer;
    width:80px;
    height: 40px;
    }
.bt-fresh{
color: #fff;
    background-color: #82b4ca;
    border-color: #2e6da4;
    padding: 5px 10px;
    font-size: 12px;
    line-height: 1.5;
    border-radius: 3px;
    border: 1px solid transparent;
    cursor: pointer;
    width:80px;
    }
.bt-add{
color: #fff;
    background-color: #ff6c00 ;
    border-color: #2e6da4;
    padding: 5px 10px;
    font-size: 12px;
    line-height: 1.5;
    border-radius: 3px;
    border: 1px solid transparent;
    cursor: pointer;
     width:80px;
}
.bt-delete{
color: #fff;
    background-color: #4caf50;
    border-color: #2e6da4;
    padding: 5px 10px;
    font-size: 12px;
    line-height: 1.5;
    border-radius: 3px;
    border: 1px solid transparent;
    cursor: pointer;
     width:80px;
    }
    
    .main1{
	display:none;
    position: fixed;
    left: 0px;
    top: 0px;
    background-color: rgba(242, 242, 242, 1);
    box-sizing: border-box;
    border-width: 1px;
    border-style: solid;
    border-color: rgba(153, 153, 153, 1);
    border-radius: 0px;
    height: 300px;
    width: 460px;
    
} 
 .mailisel{width:120px;height:25px;padding:2px;border:1px solid #ddd;}
    .mailtab{border-collapse:collapse;margin:20px 10px 10px 30px;font-size:18px;}
    .maitit{background: #138cdd;padding:5px 0;color:#fff;position:relative;}
    .maiclo{display:inline-block;font-size:15px;position:absolute;right:5px;top:4px;z-index:2;cursor:pointer;}
    .mailibtn{width: 116px;
    height: 33px;
    background: inherit;
    background-color: rgba(22, 155, 213, 1);
    border: none;
    border-radius: 5px;color:#fff;margin-top:10px;}
</style>

<title>1688类别管理</title>
</head>

<body>
	<div class="main" align="center">
		<div class="tabletitle">1688类别管理</div>
		<form action="/cbtconsole/category/list" id="r_form">
		<div class="row" style="margin-bottom: 20px;font-size: 20px;">
		<span >类别id:<input type="text" id="pcatid" class="pcatid" name="catid" style="height: 40px;" value="${catid}"></span>
		<span>类别名称:<input type="text" id="pname" class="pcname" name="cname" style="height: 40px;" value="${cname}"></span>
		<input type="button" id="bt-query" class="bt-query" value="查询" onclick="fnquery()">
		<input type="button" id="bt-query" class="bt-query" value="刷新" onclick="fnre()">
			
		</div>
		<table class="table table-bordered  table-hover definewidth m10" style="table-layout: fixed;word-wrap:break-word;">
			<thead>
				<tr bgcolor="#9e9e9e">
				<td width="80px">id</td>
				<td width="300px">category name0</td>
				<td width="300px">category name1</td>
				<td width="100px">category id</td>
				<td width="200px">category path</td>
				<td width="300px">category childids</td>
				<td width="100px">category lv</td>
				<td width="200px">action</td>
				</tr>
			</thead>
			<tbody id="trRecords">
			<c:forEach items="${list}" var="cate" varStatus="steps">
				<tr bgcolor="${steps.index%2==0?'#FFF7FB':'#ECFFFF' }">
				<td width="80px" id="index_${cate.id}">${steps.index+1}</td>
				<td width="300px" id="cname_${cate.id}">${cate.categoryCName}</td>
				<td width="300px" id="ename_${cate.id}">${cate.categoryName}</td>
				<td width="100px" id="catid_${cate.id}">${cate.categoryId}</td>
				<td width="200px" id="path_${cate.id}">${cate.path}</td>
				<td width="300px" id="childIds_${cate.id}">${cate.childIds}</td>
				<td width="100px" id="lv_${cate.id}">${cate.lv}</td>
				<td width="200px" id="">
				<a onclick="fnrefresh(${cate.id},0)">编辑</a>
				<%-- <a onclick="fnrefresh(${cate.id},2)">删除</a> --%>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<div class="bootom_page">
		<input type="hidden" value="${page }" id="hi_page">
		<input type="hidden" value="${total }" id="hi_total">
		总共:${page }/${total}
		<input type="button" value="上一页" onclick="rpage(0)" class="bt-query">
		<input type="button" value="下一页" onclick="rpage(1)" class="bt-query">
		<input type="text" value="${page }" id="page" name="page" style="height: 40px;">
		<input type="button" value="Go" onclick="rpage(2)" class="bt-query">
		
		</div>
		</form>
		
		
		<div class="main1" align="center" >
	<div class="maitit">修改类别<span class="maiclo" onclick="mhide()">X</span></div>
	<table  class="mailtab">
	<tr>
	<td width="200px">类别名称(中文):</td>
	<td width="300px"><span  class="inputclass"  id="categoryCName" ></span> </td>
	</tr>
	<tr>
	<td width="200px">类别名称(英文):</td>
	<td width="300px"><textarea rows="5" cols="27" id="categoryName"  class="inputclass"></textarea>
	</tr>
	
	<tr>
	<td width="200px">catid:</td>
	<td width="300px"><span id="catid"></span> </td>
	</tr>
	
	</table>
	<input type="hidden"  id="did" value="">
	<input type="button" value="保存" class="mailibtn" onclick="fnrefresh(0,1)">
	<br>
	</div>
	</div>
	
</body>
</html>