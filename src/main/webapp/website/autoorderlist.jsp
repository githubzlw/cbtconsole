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
<title>自生成订单</title>
<style type="text/css">
body{width: 90%;margin: 0 auto;}
.body{width: 95%;height:auto;}
.table1 td{width: 300px;}
.table td{width: 300px;}
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
	}else if(obj==0){
		page = "1";
	}
	$("#page").val(page);
	var userid=$("#userid").val();
	var orderid=$("#orderid").val();
	window.location.href="/cbtconsole/autoorder/alist?page="+page+"&orderid="+orderid+"&userid="+userid;
}

function fncancle(obj,type) {
	if(type==0){
		$.dialog.confirm("Message","确定要取消订单    "+order+"  ?", function(){
			 $.post("/cbtconsole/autoorder/cancelorder",
					{orderid:order},
					function(res){
						window.location.reload();
					}); 
		}, function(){
			
		});
		
	}else{
		$.dialog.confirm("Message","确定要取消该笔付款记录吗?", function(){
			 $.post("/cbtconsole/autoorder/cancelpay",
					{pid:obj},
					function(res){
						window.location.reload();
					}); 
		}, function(){
		});
	}
	
	
}
</script>

</head>
<body class="body">
<div align="center">
	<br>
	<div >
	<div style="font-size: 25px;">查看所有后台生成订单</div>
	<div style="font-size: 20px;color: red;">备注：统一货币单位USD</div>
	<br>
	<div>
	用户:<input id="userid" value="${param.userid}" type="text" style="height: 26px;">
	订单号:<input id="orderid" value="${param.orderid}" type="text" style="height: 26px;">
	<input type="button" onclick="fnjump(0)" value="查询" class="btn">
	</div>
	<br>
		<table id="table"  border="1" cellpadding="0" cellspacing="0" class="table">
		<tr align="center">
				<td style="width: 100px;">用户</td>
				<td style="width: 150px;">订单号</td>
				<td style="width: 100px;">订单操作人</td>
				<td style="width: 100px;">进账操作人</td>
				<td style="width: 100px;">总额</td>
				<td style="width: 50px;">货币</td>
				<td style="width: 100px;">订单状态</td>
				<td style="width: 200px;">支付状态</td>
				<td style="width: 150px;">支付方式</td>
				<td style="width: 100px;">时间</td>
				<td style="width: 200px;">操作</td>
				<!-- <td >paypal申诉</td> -->
		</tr>
		
		<c:forEach items="${orderList}" var="order"  >
		<tr  bgcolor="${order.orderState=='确认价格中' ? '#DAF3F5':'#FFF7FB' }" >
		<td style="width: 100px;" id="user_${order.id}">${order.userid}</td>
		<td style="width: 150px;"  id="order_${order.id}">${order.orderid}</td>
		<td style="width: 100px;"  id="oadmin_${order.id}">${order.orderAdmin}</td>
		<td style="width: 100px;"  id="padmin_${order.id}">${order.paymentAdmin}</td>
		<td  style="width: 100px;" id="payprice_${order.id}">${order.payPrice}</td>
		<td style="width: 50px;"  id="currency_${order.id}">${order.currency}</td>
		<c:if test="${order.orderState=='确认价格中'}">
		<td style="width: 200px;"  id="orderstate_${order.id}"  style="color:red; ">${order.orderState}</td>
		</c:if>
		<c:if test="${order.orderState!='确认价格中'}">
		<td style="width: 100px;"  id="orderstate_${order.id}">
		${order.orderState}
		
		</td>
         </c:if>
		<td  style="width: 200px;" id="payStatus_${order.id}">${order.payStatus}
		<c:if test="${order.payStatus=='未付款'}">(<a onclick="window.open('/cbtconsole/website/autoorder.jsp?#paymentct','_blank')">录入进账</a>)</c:if>
		
		
		</td>
		
		<td style="width: 150px;"  id="payType_${order.id}">${order.payType}</td>
		<td  style="width: 100px;" id="">${order.createTime}</td>
		<td style="width: 200px;"  >
		<c:if test="${order.orderState!='用户取消'&&order.orderState!='后台取消'}">
			<a  onclick="fncancle(${order.index},0)" style="color:#2e6da4; ">取消订单</a>&nbsp;
		</c:if>
		<c:if test="${order.payStatus=='已付款'}">
		<a  onclick="fncancle(${order.payId},1}')" style="color:#2e6da4; ">取消支付</a>
		</c:if>
		
		</td>
		</tr>
		
		</c:forEach>
		
		
		</table>
		<br>
		<br>
		<div>
		<input type="hidden" id="totalpage" value="${totalpage}">
		
		总共:&nbsp;&nbsp;<span id="pagetotal">${currentpage}<em>/</em> ${totalpage}</span>
		页&nbsp;&nbsp;
		<input type="button" value="上一页" onclick="fnjump(-1)" class="btn">
		<input type="button" value="下一页" onclick="fnjump(1)" class="btn">
		
		第<input id="page" type="text" value="${currentpage}" style="height: 26px;">
		<input type="button" value="查询" onclick="fnjump(0)" class="btn">
		</div>
		<br><br>
		<br><br>
		</div>
</div>
</body>
</html>