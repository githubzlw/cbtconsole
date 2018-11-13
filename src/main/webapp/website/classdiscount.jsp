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
		if(type==0){
			//编辑
			var hwid = ($(document).width()- $(".main1").width())/2;
			var heig = ($(window).height()- $(".main1").height())/2;
			$(".main1").show().css({"left":hwid,"top":heig});
			$("#showName").val($("#showName_"+id).html().replace('&amp;','&'));
			$("#className").val($("#className_"+id).html().replace('&amp;','&'));
			$("#price").val($("#price_"+id).html());
			$("#depositRate").val($("#depositRate_"+id).html());
			$("#catid").val($("#catid_"+id).html());
			$("#classType").val($("#classType_"+id).html());
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
		var catid=$('#pcatid').val();
		var pdesopite=$('#pdesopite').val();
		var pprice=$('#pprice').val();
		window.location.href="/cbtconsole/discount/list?desopite="+pdesopite+"&catid="+catid+"&price="+pprice;
	}
	
	function mhide(){
		$(".main1").hide();
	}
	
	/* 编辑*/
	function confirm(id,type){
		 var index=id;
		var showName = $('#showName').val();
		var className = $('#className').val();
		var price= $('#price').val();
		var depositRate = $('#depositRate').val();
		var catid = $('#catid').val();
		var classType = $('#classType').val();
		
		$.post("/cbtconsole/discount/deal",
				{type:type,index:index,showName:showName,className:className,price:price,depositRate:depositRate,catid:catid,classType:classType},
				function(res){
					if(res>0){
						alert("成功");
						window.location.reload();
					}
					if(res==0){
						alert("失败");
					}
					if(res==-1){
						alert("请输入正确的折扣起始金额");
					}
					if(res==-2){
						alert("请输入正确的折扣( 0.0-1.0)");
					}
					if(res==-3){
						alert("请输入正确的classtype");
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
		 $.post("/cbtconsole/discount/refresh",
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

<title>混批折扣管理</title>
</head>

<body>
	<div class="main" align="center">
		<div class="tabletitle">混批折扣管理</div>
		
		<div class="row" style="margin-bottom: 20px;font-size: 20px;">
		<span >类别catid:<input type="text" id="pcatid" class="pcatid" style="height: 40px;" value="${param.catid}"></span>
		<span>折扣:<input type="text" id="pdesopite" class="pdesopite" style="height: 40px;" value="${param.desopite}"></span>
		<span>金额:<input type="text" id="pprice" class="pprice" style="height: 40px;" value="${param.price}"></span>
		<input type="button" id="bt-query" class="bt-query" value="查询" onclick="fnquery()">
		<input type="button" id="bt-query" class="bt-query" value="刷新" onclick="fnre()">
			
		</div>
		<table class="table table-bordered  table-hover definewidth m10" style="table-layout: fixed;word-wrap:break-word;">
			<thead>
				<tr bgcolor="#9e9e9e">
				<td width="80px">id</td>
				<td width="150px">showname</td>
				<td width="300px">classname</td>
				<td width="100px">Total(USD)</td>
				<td width="100px">deposite_rate</td>
				<td width="100px">catid</td>
				<td width="80px">classtype</td>
				<td width="80px">action</td>
				</tr>
			</thead>
			<tbody id="trRecords">
			<c:forEach items="${list}" var="discount" varStatus="steps">
				<tr bgcolor="${steps.index%2==0?'#FFF7FB':'#ECFFFF' }">
				<td width="80px" id="index_${discount.id}">${steps.index+1}</td>
				<td width="150px" id="showName_${discount.id}">${discount.showName}</td>
				<td width="300px" id="className_${discount.id}">${discount.className}</td>
				<td width="100px" id="price_${discount.id}">${discount.price}</td>
				<td width="100px" id="depositRate_${discount.id}">${discount.depositRate}</td>
				<td width="100px" id="catid_${discount.id}">${discount.catid}</td>
				<td width="80px" id="classType_${discount.id}">${discount.classType}</td>
				<td width="80px" id="">
				<a onclick="fnrefresh(${discount.id},0)">编辑</a>
				<a onclick="fnrefresh(${discount.id},2)">删除</a>
				<%-- <input type="button" value="修改" onclick="fnrefresh(${discount.id},0)" class="bt-fresh"> 
				<input type="button" value="增加"  onclick="fnrefresh(${discount.id},1)" class="bt-add">
				<input type="button" value="删除"  onclick="fnrefresh(${discount.id},2)" class="bt-delete"></td> --%>
				</tr>
			</c:forEach>
			</tbody>
		</table>
		<%-- <div>
			<c:choose>
				<c:when test="${pagecount==0 }">
					<label>当前没有数据</label>
				</c:when>
				<c:otherwise>
					<span>共查到条${count }数据</span>&nbsp;&nbsp;
					<a id="prepage">上一页</a>&nbsp;
					第<span id="pagenow">${pagenow }</span>页/共<span id="pagecount">${pagecount }</span>页&nbsp;
					<a id="nextpage">下一页</a>&nbsp;
					<input type="text" id="topage" value="1" style="width:40px" onkeydown="if(event.keyCode==13){enterToJump()}"/><input type="button" id="jumpToPage" value="Go" />
				</c:otherwise>
			</c:choose>
		</div> --%>
	</div>
	
	<div class="main1" align="center" >
	<div class="maitit">新增/修改折扣<span class="maiclo" onclick="mhide()">X</span></div>
	<table  class="mailtab">
	<tr>
	<td width="200px">showname:</td>
	<td width="300px"><input  class="inputclass" type="text"  id="showName" value=""> </td>
	</tr>
	<tr>
	<td width="200px">classname:</td>
	<td width="300px"><input type="text" class="inputclass" id="className" value="" > </td>
	</tr>
	
	<tr>
	<td width="200px">Total(USD):</td>
	<td width="300px"> <input type="text" class="inputclass" id="price" value="" ></td>
	</tr>
	
	<tr>
	<td width="200px">deposite_rate:</td>
	<td width="300px"> <input type="text" class="inputclass" id="depositRate" value="" ></td>
	</tr>
	
	<tr>
	<td width="200px">catid:</td>
	<td width="300px"><input type="text" class="inputclass" id="catid" value="" > </td>
	</tr>
	
	<tr>
	<td width="200px">classtype:</td>
	<td width="300px"> <input type="text" class="inputclass" id="classType" value=""></td>
	
	</tr>
	
	</table>
	<input type="hidden"  id="did" value="">
	<input type="button" value="保存" class="mailibtn" onclick="fnrefresh(0,1)">
	<br>
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