<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>邮件发送失败信息查询</title>
</head>
<script type="text/javascript">
var page=1;
var count = 1;
function fn(va){
	if(va==1){
		  page=1;
	  }
	  else if(va==2){
		  page = page+1;
		  if(count<page){
			  return;
		  }
	  }else if(va == 3){
		  page = page-1;
		  if(0>=page){
			  page = 1;
			  return;
		  }
	  }else if(va == 4){
		  page=$("#jump").val();
	  }
	var time = $("#time").val();
	var endtime = $("#endtime").val();
	if(time == "" || endtime == ""){
		alert("请输入开始时间和结束时间");
		return;
	}
	$.post("/cbtconsole/WebsiteServlet",
  			{action:'getMessage_error',className:'OrderSplitServlet',time:time,endtime:endtime,page:page},
  			function(res){
  				var jsons = eval(res)[0];
  				count = jsons.count;
  				var json = jsons.message;
  			 	var row = 1;
  			 	$("#table tr:gt(0)").remove();
  				for (var i = json.length-1; i >= 0; i--) {
  					 $("#table tr:eq("+(row-1)+")").after("<tr></tr>");
  					  $("#table tr:eq("+row+")").append("<td>"+json[i][0]+"</td>");
  					  $("#table tr:eq("+row+") td:eq(0)").after("<td>"+json[i][1]+"</td>");
  					  $("#table tr:eq("+row+") td:eq(1)").after("<td>"+json[i][2]+"</td>");
  					  $("#table tr:eq("+row+") td:eq(2)").after("<td>"+json[i][3]+"</td>");
  					  $("#table tr:eq("+row+") td:eq(3)").after("<td>"+json[i][4]+"</td>");
				}

  			  count = Math.ceil(jsons.count / 40);
  			  $("#count").html(count);
  			  $("#counto").html(jsons.count);
  			  page = parseInt(jsons.page, 0);
  			  $("#page").html(page);
  			});
}
</script>
<body>
<div>
<h3>邮件发送失败信息查询</h3>
<div  align="center">
	开始时间：<input id="time" name="date" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" />
	&nbsp;结束时间:<input id="endtime" name="date" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" />&nbsp;&nbsp;<input onclick="fn(1)" type="button" value="查询">
	<br>
	<table id="table" border="1px" style="font-size: 13px;" bordercolor="#8064A2" cellpadding="0" cellspacing="0">
		<tr>
			<td>Item</td>
			<td>收件人</td>
			<td>错误信息</td>
			<td>备注信息</td>
			<td>创建时间</td>
		</tr>
	</table><br>
	<div class="pages" id="pages">
		  
<span>&nbsp;&nbsp;总条数：<font id="counto"></font>&nbsp;&nbsp;总页数：<font id="count"></font></span>&nbsp;&nbsp;当前页<font id="page"></font><button onclick="fn(3)">上一页</button>&nbsp;<button  onclick="fn(2)">下一页</button>&nbsp;<input id="jump" type="text"><button onclick="fn(4)">转至</button></div>
</div>
</div>
</body>
</html>