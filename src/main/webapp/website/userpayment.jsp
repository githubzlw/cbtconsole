<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.cbt.util.Redis"%>
<% String admuserJson = Redis.hget(request.getSession().getId(), "admuser"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>用户消费记录</title>
<style type="text/css">
body{
width: 1800px;
margin: 0 auto;
}
.body{
width: 1800px;
height:auto;
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

.table1 td{width: 435px;}
.table2 td{width: 435px;}
</style>
<script type="text/javascript">

function fnjump(obj){
	var page=$("#page").val();
	if(page==""){
		page = "1";
		return ;
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
	}
	$("#page").val(page);
	var userid = $("#userid").val();
	$("#pagenow").html(page);
	$.post("/cbtconsole/paycheckc/record", {
		userid:userid,page:page
	}, function(res) {
		var json = eval(res);
	   var html = "";
		if(json!=null&&json!=undefined&&json!=''){
			$("#table4 tbody tr").eq(0).nextAll().remove();
			for(var i=0;i<json.length;i++){
			 var record = json[i];
			 $("#table4 tr:eq("+(i)+")").after("<tr></tr>");
			 $("#table4 tr:eq("+(i)+")").after("<td >"+record.datatime+"</td>");
			$("#table4 tr:eq("+(i)+")").after("<td >"+record.adminUser+"</td>");
			$("#table4 tr:eq("+(i)+")").after("<td >"+record.currency+"</td>");
			$("#table4 tr:eq("+(i)+")").after("<td >"+record.balanceAfter+"</td>");
			$("#table4 tr:eq("+(i)+")").after("<td >"+record.remark_id+"</td>");
			$("#table4 tr:eq("+(i)+")").after("<td >"+record.remark+"</td>");
			$("#table4 tr:eq("+(i)+")").after("<td >"+record.type+"</td>");
			$("#table4 tr:eq("+(i)+")").after("<td >"+record.price+"</td>");
			$("#table4 tr:eq("+(i)+")").after("<td >"+record.userid+"</td>");
			$("#table4 tr:eq("+(i)+")").after("<td >"+(i+1) +"</td>");
				
			}
		}	
	}); 
	
	
}

function fnquery(){
	var userid = $("#userid").val();
	if(userid==''){
		return ;
	}
	window.location.href="/cbtconsole/paycheckc/payInfo?userid="+userid;
}


function changeavailable(){
	var available=document.getElementById("current_m").innerText;
	var userid=document.getElementById("userid").value;
	
	var admuserJson = <%=admuserJson%>;
	if(userid == 0 || userid == ""){
		alert("获取用户id失败");
	} else{
		if(admuserJson.roletype == 0){
			window.open("/cbtconsole/website/change_available.jsp?userid="+userid+"&available="+available,"windows","height=500,width=900,top=300,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
		} else{
			alert("无管理员权限");
		}
	}
	
	
	
}
</script>

</head>
<body class="body">
<div align="center">
	<br>
	<div >
	<a style="font-size: 25px;">用户消费记录</a>
	<br>
	<br>
	<a href="javascript:" onclick="self.location=document.referrer;">返回上一页重载页面，本地刷新</a> 
	<div>
	</div>
	<br>
	<div>
	用户id:<input style="width: 200px;" id="userid" type="text" value="${param.userid}"/>
	email:<input style="width: 300px;" id="email" type="text" value="${payment.userEmail}"/>
	<input style="width: 50px;" id="button" type="button" value="查询" onclick="fnquery()" class="btn"/>
	<input style="width: 80px;" class="btn" id="button" type="button" value="修改余额" onclick="changeavailable()"/>
	</div>
	<br>
	<br>
		
		<table id="table1"  border="1" cellpadding="0" cellspacing="0" class="table1">
			<tr>
				<td > 额外奖励或补偿 </td>
				<td id="add_balance">${payment.addBalance }</td>
			</tr>
			<tr>
				<td >总收入(美元)(Paypal &amp; Wire transfer) </td>
				<td id="pay_all">${payment.orderPayPrice }</td>
			</tr>
			
		</table>
		<table id="table2"  border="1" cellpadding="0" cellspacing="0" class="table2">
			<tr>
				<td >采购总支出(美元)</td>
				<td id="order_all">${payment.orderPriceAll}</td>
			</tr>
			<tr>
				<td >退款已完结 </td>
				<td id="refund">${payment.refund }  </td>
			</tr>
			<tr>
				<td >提现处理中 </td>
				<td id="app_refund"> ${payment.appRefund } </td>
			</tr>
			<tr>
				<td >paypal申诉处理中</td>
				<td id="app_refund"> ${payment.appPaypal } </td>
			</tr>
			<tr>
				<td >应有余额 </td>
				<td id="balance"> ${payment.balance } </td>
			</tr>
		</table>
		<table id="table3"  border="1" cellpadding="0" cellspacing="0" class="table2">
			<tr>
				<td >客户在我司账户实际余额</td>
				<td id="current_m">${payment.currencyBalance }</td>
			</tr>
			<%-- <tr style="background: #DAF3F5; ">
				<td >汇总</td>
				<td id="total"> ${payment.total } </td>
			</tr> --%>
		</table>
		<br>
		<br>
		<c:if test="${pagecount>0 }">
		<div style="font-size: 25px;">余额变更记录</div>
		<table id="table4"  border="1" cellpadding="0" cellspacing="0" class="table2">
			<tr>
				<td>时间</td>
				<td>金额</td>
				<td>类型</td>
				<td>备注</td>
				<td>备注ID</td>
				<td>操作后余额</td>
				<td>货币单位</td>
				<td>处理人</td>
			</tr>
			
		<c:forEach items="${recordList }" var="record"  varStatus="step">
		<tr class="autotr">
			<td>${record.datatime }</td>
			<td>${record.price }</td>
			<td>${record.type }
			<c:if test="${record.type==0}"></c:if>
			<c:if test="${record.type==1}">取消订单</c:if>
			<c:if test="${record.type==2}">多余金额</c:if>
			<c:if test="${record.type==3}">重复支付</c:if>
			<c:if test="${record.type==4}">手动修改</c:if>
			<c:if test="${record.type==5}">其他</c:if>
			<c:if test="${record.type==7}">余额抵扣</c:if>
			<c:if test="${record.type==8}">申请提现</c:if>
			<c:if test="${record.type==9}">提现取消</c:if>
			<c:if test="${record.type==10}">提现拒绝</c:if>
			</td>
			<td>${record.remark }</td>
			<td>${record.remark_id }</td>
			<td>${record.balanceAfter }</td>
			<td>${record.currency }</td>
			<td>${record.adminUser }</td>
		</tr>
		</c:forEach>
	</table>
		<br>
		<br>
		<div>
		<input type="hidden" id="totalpage" value="${pagecount}">
		
		总共:&nbsp;&nbsp;<span id="pagetotal"><em id="pagenow">${pagenow}</em><em>/</em> ${pagecount}</span>
		页&nbsp;&nbsp;
		<input type="button" value="上一页" onclick="fnjump(-1)" class="btn">
		<input type="button" value="下一页" onclick="fnjump(1)" class="btn">
		
		第<input id="page" type="text" value="${pagenow}" style="height: 26px;" >
		<input type="button" value="查询" onclick="fnjump(0)" class="btn">
		</div>
		<br><br>
		<br><br>
		
		</c:if>
		</div>
		
		</div>
</body>
</html>