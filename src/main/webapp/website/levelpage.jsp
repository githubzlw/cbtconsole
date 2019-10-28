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
	function fnrefresh(id,type){
		 if(type==0){//编辑
			var hwid = ($(document).width()- $(".main1").width())/2;
			var heig = ($(window).height()- $(".main1").height())/3;
			$(".main1").show().css({"left":hwid,"top":heig});
			$("#id").val(id);
			if(id != 0){
				$("#name").val($("#name_"+id).html());
				$("#pagehtml").val($("#pagehtml_"+id).html());
				$("#valid").val($("#valid_"+id).val());
				$("#catid").val($("#catid_"+id).html());
			}else{
				$("#name").val("");
				$("#pagehtml").val('');
				$("#valid").val(1);
				$("#catid").val('0');
			}
		}else if(type==1){//保存
			
			var id = $('#id').val();
			var name = $('#name').val();
			var page = $('#pagehtml').val();
			var valid = $('#valid').val();
			var catid = $('#catid').val();
			$.post("/cbtconsole/level/uplevel",
					{id:id,name:name,page:page,valid:valid,catid:catid},
					function(res){
						window.location.reload();
					}); 
		}
	}
	
	
function mhide(){
	$("#name").val("");
	$("#pagehtml").val('');
	$("#valid").val(1);
	$("#catid").val('0');
	$(".main1").hide();
}
function fnre(site){
	 $.post("/cbtconsole/level/refresh",
				{'site':site},
				function(res){
					alert(msg);
				});
}

function fnjump(obj){
	var page=$("#page").val();
	if(page==""){
		page = "1";
	}
	if(obj==-1){//前一页
		if(parseInt(page)<2){
			return ;
		}
		page = parseInt(page)-1;
	}else if(obj==1){//下一页
		if(parseInt(page)>parseInt($("#totalpage").val())-1){
			return ;
		}
		page = parseInt(page)+1;
	}else if(obj < -1){
		page=1;
	}
	
	$("#page").val(page);
	if(parseInt(page)==parseInt($("#totalpage").val())){
		return ;
	}
	window.location.href="/cbtconsole/level/list?page="+page;
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
   
} 
.main1{
display:none;
    width: 378px;
    height: 295px;
    position: absolute;
    left: 0px;
    top: 0px;
    background-color: rgba(242, 242, 242, 1);
    box-sizing: border-box;
    border-width: 1px;
    border-style: solid;
    border-color: rgba(153, 153, 153, 1);
    border-radius: 0px;
    
} 
body{
	text-align: center;
	
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
    .mailisel{width:120px;height:25px;padding:2px;border:1px solid #ddd;}
    .mailtab{border-collapse:collapse;margin:20px auto 10px;font-size:18px;}
    .maitit{background: #138cdd;padding:5px 0;color:#fff;position:relative;}
    .maiclo{display:inline-block;font-size:15px;position:absolute;right:5px;top:4px;z-index:2;cursor:pointer;}
    .mailibtn{width: 116px;
    height: 33px;
    background: inherit;
    background-color: rgba(22, 155, 213, 1);
    border: none;
    border-radius: 5px;color:#fff;margin-top:10px;}
    
 .btn{
 	width: 70px;
    height: 35px;
    background: inherit;
    background-color: rgba(22, 155, 213, 1);
    border: none;
    border-radius: 5px;
    color: #fff;
    }   
    
</style>

<title>关键词二级页面管理</title>
</head>

<body>
<div class="main" align="center"><div class="tabletitle">关键词二级页面管理</div></div>
<input type="button" value="新增" onclick="fnrefresh(0,0)" class="mailibtn"  style="margin-left: -961px;">
<input type="button" id="bt-query" class="mailibtn"  value="刷新Import" onclick="fnre('import')">
<input type="button" id="bt-query" class="mailibtn"  value="刷新Kids" onclick="fnre('kids')">
<input type="button" id="bt-query" class="mailibtn"  value="刷新Pets" onclick="fnre('pets')">
	   
	<div class="main" align="center">
		<table class="table table-bordered  table-hover definewidth m10" style="table-layout: fixed;word-wrap:break-word;">
			<thead>
				<tr bgcolor="#f2f2f2">
				<td width="50px">id</td>
				<td width="100px">关键词</td>
				<td width="100px">类别</td>
				<td width="150px">二级页面</td>
				<td width="50px">状态</td>
				<td width="100px">更新时间</td>
				<td width="50px">操作</td>
				</tr>
			</thead>
			<tbody id="trRecords">
			<c:forEach items="${list}" var="glist" varStatus="steps">
				<tr bgcolor="${steps.index%2==0?'#FFF7FB':'#ECFFFF' }">
				<td width="50px" id="id_${glist.id}">${glist.id}</td>
				<td width="50px" id="name_${glist.id}">${glist.name}</td>
				<td width="50px" id="catid_${glist.id}">${glist.catid}</td>
				<td width="50px" id="page1_${glist.id}"><a href="https://www.import-express.com${glist.page}" id="pagehtml_${glist.id}">${glist.page}</a></td>
				<td width="50px" id="valid1_${glist.id}">
				<input type="hidden" id="valid_${glist.id}" value="${glist.valid}">
				<c:if test="${glist.valid==0}">失效</c:if>
				<c:if test="${glist.valid==1}">启用</c:if>
				</td>
				<td width="100px" id="time_${glist.id}">${glist.createTime}</td>
				<td width="50px" id="">
				<a onclick="fnrefresh(${glist.id},0)">编辑</a>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
		<br>
		<div>
		<input type="hidden" id="totalpage" value="${totalpage}">
		
		总共:&nbsp;&nbsp;<span id="pagetotal">${currentpage}<em>/</em> ${totalpage}</span>
		页&nbsp;&nbsp;
		<input type="button" value="上一页" onclick="fnjump(-1)" class="btn">
		<input type="button" value="下一页" onclick="fnjump(1)" class="btn">
		
		第<input id="page" type="text" value="${currentpage}" style="height: 35px;">
		<input type="button" value="查询" onclick="fnjump(0)" class="btn">
		</div>
		<br><br>
		<br><br>
		
	</div >
	
	
	<div class="main1" align="center" >
	<div class="maitit">新增/修改<span class="maiclo" onclick="mhide()">X</span></div>
	<input type="hidden" value="" id="id">
	<table class="mailtab">
	<tr><td style="width: 80px;">关键词:</td><td style="width: 150px;"><input id="name"></td></tr>
	<tr><td style="height: 20px;"></td><td></td></tr>
	<tr><td style="width: 80px;">类别:</td><td style="width: 150px;"><input id="catid" value="0"></td></tr>
	<tr><td style="height: 20px;"></td><td></td></tr>
	<tr><td style="width: 80px;">页面:</td><td style="width: 150px;"><input id="pagehtml" value=""></td></tr>
	<tr><td style="height: 20px;"></td><td></td></tr>
	<tr><td style="width: 80px;">状态:</td><td style="width: 150px;"><select id="valid" style="width: 223px;"><option value="0" selected="selected">失效</option><option value="1">启用</option></select></td></tr>
	
	</table>
	<input type="button" value="保存" class="mailibtn" onclick="fnrefresh(0,1)">
	</div>
	
	<script type="text/javascript">
	
<%-- $(function(){
	
	var roleTypejs = <%=roletype%>;
	var consoleName = '<%=admName%>';
	var statue = ${stupresent};
	var type = ${typeresent};
}) --%>





</script>
</body>
</html>