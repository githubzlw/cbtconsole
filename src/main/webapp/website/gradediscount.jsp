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
		if(type==2){
			$.dialog.confirm("Message","确定要删除吗?", function(){
				$.post("/cbtconsole/grade/delete",
						{gid:id},
						function(res){
							window.location.reload();
						});
			}, function(){
			});
			
		}else if(type==0){
			
			var hwid = ($(document).width()- $(".main1").width())/2;
			var heig = ($(window).height()- $(".main1").height())/3;
			$(".main1").show().css({"left":hwid,"top":heig});
			
			//$("#grade option[flag='"+id+"']").attr('selected','selected');
			id==0?1:id;
			$("#grade").val(id);
			
			var dis = $("#discount_"+id).html();
			dis = dis?dis:0.05;
			$("#discount").val(dis*100);
			
		}else if(type==1){
			 var gid = $('#grade').val();
			var discount = $('#discount').val();
			$.post("/cbtconsole/grade/updiscount",
					{gid:id,discount:discount},
					function(res){
						window.location.reload();
					}); 
		}
	}
	
	
	var grd='';
	$(function(){
		$.post("/cbtconsole/grade/get",
				{},
				function(res){
					var json=eval(res);
					for (var i=0;i<json.length;i++){
						grd +='<option value="'+json[i].gid+'" flag="'+json[i].gid+'">'+json[i].grade+'</option>';
					}
					
				$('#grade').html(grd);
				});
	})	
	function mhide(){
		$(".main1").hide();
	}
function fnre(){
	 $.post("/cbtconsole/grade/refresh",
				{},
				function(res){
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
    width: 800px;
   
} 
.main1{
display:none;
    width: 378px;
    height: 237px;
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
</style>

<title>用户等级折扣管理</title>
</head>

<body>
<div class="main" align="center"><div class="tabletitle">用户等级折扣管理</div></div>
<input type="button" value="新增" onclick="fnrefresh(0,0)" class="mailibtn"  style="margin-left: -560px;">
<input type="button" id="bt-query" class="mailibtn"  value="刷新" onclick="fnre()">
	   
	<div class="main" align="center">
		<table class="table table-bordered  table-hover definewidth m10" style="table-layout: fixed;word-wrap:break-word;">
			<thead>
				<tr bgcolor="#f2f2f2">
				<td width="50px">id</td>
				<td width="50px">等级</td>
				<td width="50px">折扣</td>
				<td width="100px">更新时间</td>
				<td width="50px">操作</td>
				</tr>
			</thead>
			<tbody id="trRecords">
			<c:forEach items="${list}" var="glist" varStatus="steps">
				<tr bgcolor="${steps.index%2==0?'#FFF7FB':'#ECFFFF' }">
				<td width="50px" id="gid_${glist.gid}">${glist.gid}</td>
				<td width="50px" id="grade_${glist.gid}">${glist.grade}</td>
				<td width="50px" id="discount_${glist.gid}">${glist.discount}</td>
				<td width="100px" id="time_${glist.gid}">${glist.createtime}</td>
				<td width="50px" id="">
				<a onclick="fnrefresh(${glist.gid},0)">编辑</a>
				<a onclick="fnrefresh(${glist.gid},2)">删除</a>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		
	</div >
	
	<div class="main1" align="center" >
	<div class="maitit">新增/修改折扣<span class="maiclo" onclick="mhide()">X</span></div>
	<table class="mailtab">
	<tr><td style="width: 150px;">用户等级:</td><td style="width: 150px;"><select id="grade" class="mailisel"></select></td></tr>
	<tr><td style="height: 20px;"></td><td></td></tr>
	<tr><td style="width: 150px;">折扣</td><td style="width: 150px;"><input id="discount" value="5" class="mailisel"><span>%</span></td></tr>
	
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