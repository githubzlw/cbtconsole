<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>所有用户款项校对</title>
<style type="text/css">
body{width: 1500px;margin: 0 auto;}
.body{width: 1500px;height:auto;}
.table1 td{width: 300px;}
.table td{width: 300px;}
.error{background-color:#FF8484;color:white;}
.error2{background-color:#93926C;color:white;}
.error3{background-color: yellow;;color:red;}
.error4{background-color:#00FFFF;}
</style>
<script type="text/javascript">
function fnjump(obj){
	var page=$("#page").val();
	if(page==""){
		page = "1";
	}
	if(obj==-1){
		if(parseInt(page)<1){
			return ;
		}
		page = parseInt(page)-1;
	}else if(obj==1){
		if(parseInt(page)>parseInt($("#totalpage").val())-1){
			return ;
		}
		page = parseInt(page)+1;
	}
	$("#page").val(page);
	window.location.href="/cbtconsole/paycheckc/paycheck?page="+page;
}


function fncheck(){
	var page=1
	if(document.getElementById("page")){
		page = document.getElementById("page").value;
	}
	$.post("/cbtconsole/paycheckc/paycheck", {
		page:page
	}, function(res) {
		var json = eval(res);
		$("#p_p").css("display","none");
		$("#table tbody tr").eq(0).nextAll().remove();
		var total_page = '';
		var current_page = '1';
		for (var i = 0; i < json.length; i++) {
		 var payment = json[i];
			$("#table tr:eq("+(i)+")").after("<tr></tr>");
			if(payment.userEmail.indexOf('@qq.')>-1||payment.userEmail.indexOf('@163.')>-1){
				continue;
			}
			if(payment.total=='0.00'||payment.total=='-0.00'||payment.total=='-0.01'||payment.total=='0.01'){
				/* $("#table tr:eq("+(i)+")").after("<td>"+payment.paypalAmount+"</td>"); */
				$("#table tr:eq("+(i)+")").after("<td>"+payment.total+"</td>");
				$("#table tr:eq("+(i)+")").after("<td>"+payment.balance+"</td>");
				$("#table tr:eq("+(i)+")").after("<td>"+payment.refund+"</td>");
				$("#table tr:eq("+(i)+")").after("<td>"+payment.appRefund+"</td>");
				$("#table tr:eq("+(i)+")").after("<td>"+payment.currencyBalance+"</td>");
				$("#table tr:eq("+(i)+")").after("<td>"+payment.orderPriceAll+"</td>");
				$("#table tr:eq("+(i)+")").after("<td>"+payment.orderPayPrice+"</td>");
				$("#table tr:eq("+(i)+")").after("<td>"+payment.addBalance+"</td>");
				$("#table tr:eq("+(i)+")").after("<td>"+payment.orderNum+"</td>");
				$("#table tr:eq("+(i)+")").after("<td>"+payment.userEmail+"</td>");
				$("#table tr:eq("+(i)+")").after("<td>"+payment.userId+"</td>");
			}else{
				/* $("#table tr:eq("+(i)+")").after("<td>"+payment.paypalAmount+"</td>"); */
				$("#table tr:eq("+(i)+")").after("<td class=\"error\">"+payment.total+"</td>");
				$("#table tr:eq("+(i)+")").after("<td class=\"error2\">"+payment.balance+"</td>");
				$("#table tr:eq("+(i)+")").after("<td class=\"error\">"+payment.refund+"</td>");
				$("#table tr:eq("+(i)+")").after("<td class=\"error2\">"+payment.appRefund+"</td>");
				$("#table tr:eq("+(i)+")").after("<td class=\"error\">"+payment.currencyBalance+"</td>");
				$("#table tr:eq("+(i)+")").after("<td class=\"error2\">"+payment.orderPriceAll+"</td>");
				$("#table tr:eq("+(i)+")").after("<td class=\"error4\">"+payment.orderPayPrice+"</td>");
				$("#table tr:eq("+(i)+")").after("<td class=\"error\">"+payment.addBalance+"</td>");
				$("#table tr:eq("+(i)+")").after("<td >"+payment.orderNum+"</td>");
				$("#table tr:eq("+(i)+")").after("<td >"+payment.userEmail+"</td>");
				$("#table tr:eq("+(i)+")").after("<td class=\"error\"><a target=\"_blank\" href=\"/cbtconsole/website/userpayment.jsp?userid="+payment.userId+"\">"+payment.userId+"</a></td>");
			}
			current_page = payment.pageCurrent
			total_page = payment.pageTotal
		}
		$("#totalpage").val(total_page);
		$("#pagetotal").html("查询完毕,共:"+current_page+"/"+total_page);
		$("#page").val(current_page);
	}); 
	
}

</script>

</head>
<body class="body">
<div align="center">
	<br>
	<div >
	<div style="font-size: 25px;">查看所有用户款项校对</div>
	<div style="font-size: 20px;color: red;">备注：统一货币单位USD</div>
	<br>
	<div>
	</div>
	<br>
		<table id="table"  border="1" cellpadding="0" cellspacing="0" class="table">
		<tr>
				<td >用户id</td>
				<td >email</td>
				<td >订单数</td>
				<td >额外奖励或补偿</td>
				<td >支出总金额 (Paypal &amp; Wire transfer) </td>
				<td >订单总金额</td>
				<td >已完结退款 </td>
				<td >提现处理中 </td>
				<td >paypal申诉处理中 </td>
				<td >应有余额 </td>
				<td >账户余额</td>
				<td >汇总</td>
				<!-- <td >paypal申诉</td> -->
		</tr>
		
		<c:forEach items="${list }" var="payment">
		<tr>
		<c:if test="${payment.total=='0.00' }">
		
		<td>${payment.userId}</td>
		<td>${payment.userEmail}</td>
		<td>${payment.orderNum}</td>
		<td>${payment.addBalance}</td>
		<td>${payment.orderPayPrice}</td>
		<td>${payment.orderPriceAll}</td>
		<td>${payment.refund}</td>
		<td>${payment.appRefund}</td>
		<td>${payment.appPaypal}</td>
		<td>${payment.balance}</td>
		<td>${payment.currencyBalance}</td>
		<td>${payment.total}</td>
		
		</c:if>
		<c:if test="${payment.total!='0.00' }">
		<td class="error">
		<a target="_blank" href="/cbtconsole/paycheckc/payInfo?userid=${payment.userId}">${payment.userId}</a></td>
		<td >${payment.userEmail}</td>
		<td >${payment.orderNum}</td>
		<td class="error">${payment.addBalance}</td>
		<td class="error4">${payment.orderPayPrice}</td>
		<td class="error2">${payment.orderPriceAll}</td>
		<td class="error">${payment.refund}</td>
		<td class="error2">${payment.appRefund}</td>
		<td class="error4">${payment.appPaypal}</td>
		<td class="error2">${payment.balance}</td>
		<td class="error">${payment.currencyBalance}</td>
		<td class="error2">${payment.total}</td>
		</c:if>
		</tr>
		</c:forEach>
		</table>
		<br>
		<br>
		<div>
		<input type="hidden" id="totalpage" value="${pageTotal  }">
		
		<span id="pagetotal">查询完毕,共:${pageCurrent } / ${pageTotal  }</span>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input type="button" value="上一页" onclick="fnjump(-1)">
		<input type="button" value="下一页" onclick="fnjump(1)">
		
		第<input id="page" type="text" value="${pageCurrent}">
		<input type="button" value="查询" onclick="fnjump(0)">
		</div>
		<br><br>
		<br><br>
		</div>
</div>
</body>
</html>