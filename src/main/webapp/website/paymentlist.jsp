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
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<title>支付信息</title>
<style type="text/css">
body{width: 1500px;margin: 0 auto;}
.body{width: 1500px;height:auto;}
/* .error{background-color:#FF8484;color:white;}
.error2{background-color:#93926C;color:white;}
.error3{background-color: yellow;;color:red;}
.error4{background-color:#00FFFF;} */
a{
	cursor: pointer;
	text-decoration:underline;
}
.btn{    color: #fff;
    background-color: #5db5dc;
    border-color: #2e6da4;    
    padding: 5px 10px;
    font-size: 12px;
    line-height: 1.5;
    border-radius: 3px;
    border: 1px solid transparent;
    cursor: pointer;}
 .inputtext{
 height:25px;
 
    }

</style>
<script type="text/javascript">
function fnjump(obj){
	var page=$("#page").val();
	if(page==""){
		page = "1";
	}
	if(obj==-1){
		if(parseInt(page)<2){
			return ;
		}
		page = parseInt(page)-1;
	}else if(obj==1){
		if(parseInt(page)>parseInt($("#totalpage").val())-1){
			return ;
		}
		page = parseInt(page)+1;
	}else if(obj==-2){
		page=1;
	}
	
	$("#page").val(page);
	var appcount = $("#appcount").val();
	var curreny = $("#curreny").val();
	
	window.location.href="/cbtconsolee/refundss/query?appcount="+appcount+"&curreny="+curreny+"&page="+page
}


</script>

</head>
<body class="body">
<input type="hidden" value="${param.appcount }" id="appcount">
<input type="hidden" value="${param.curreny }" id="curreny">
<div align="center">
	<br>
	<div >
	<br>
	<div align="center"  style="font-size: 20px;font-weight: bold;">
	匹配到的支付信息
	</div>
	<br>
	<table id="table"  border="1" cellpadding="0" cellspacing="0" class="table" >
	<tr align="center"  bgcolor="#DAF3F5" >
			<td style="width: 50px;" ></td>
			<td style="width: 150px;" >支付金额</td>
			<td style="width: 150px;">货币单位</td>
			<td style="width: 200px;">支付时间</td>
			<td style="width: 150px;" >用户ID</td>
			<td style="width: 300px;" >订单号</td>
			<td style="width: 300px;" >paypal账号</td>
	</tr>
	
	
	<c:forEach items="${paymentList}" var="list" varStatus="index" >
	<tr  bgcolor="#FFF7FB">
	<!-- 索引 -->
	<td >${index.index+1}</td>
	<!-- 支付金额  -->
	<td  style="text-align: center;">
	<span style="font-size: 14px;" id="appcount_${index.index}">${list.payment_amount}</span>
	</td>
	<!-- 支付单位  -->
	<td  style="text-align: center;">
	<span style="font-size: 14px;" id="currency_${index.index}">${list.payment_cc}</span>
	</td>
	<!-- 支付时间  -->
	<td id="time_${index.index}" style="text-align: center;">
	<span >${list.createtime}</span>
	</td>
	
	<!-- 用户id  -->
	<td  style="text-align: center;">
	${list.userid }
	</td>
	<!-- 订单号  -->
	<td  style="text-align: center;">
	${list.orderid}
	</td>
	<!-- 申诉账号  -->
	<td  style="text-align: center;">
	${list.orderdesc}
	</td>
	
	</tr>
	</c:forEach>
	
	</table>
		<br>
		<div>
		<input type="hidden" id="totalpage" value="${pagecount}">
		
		总共:&nbsp;&nbsp;<span id="pagetotal">${pagenow}<em>/</em> ${pagecount}</span>
		页&nbsp;&nbsp;
		<input type="button" value="上一页" onclick="fnjump(-1)" class="btn">
		<input type="button" value="下一页" onclick="fnjump(1)" class="btn">
		
		第<input id="page" type="text" value="${pagenow}" style="height: 26px;" >
		<input type="button" value="查询" onclick="fnjump(0)" class="btn">
		</div>
		<br><br>
		<br><br>
		</div>
</div>
</body>
</html>