<%@page import="com.cbt.util.WebCookie"%>
<%@ page import="com.cbt.refund.bean.RefundBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="/cbtprogram/img/mathematics1.ico" type="image/x-icon"/>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/bootstrap.min.css">
<style type="text/css">
.tabletitle{
	font-family : 微软雅黑,宋体;
	font-size : 2em;
	color : #f00;
}
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">

<style type="text/css">
.mid{
    width: 966px;
    margin: 0 auto;
}
</style>

<title>用户消费记录</title>


</head>

<body>
	<div class="mid">
	<div onclick="javascript:history.back(-1)" style="cursor: pointer;color:#00aff8;">« Back</div>
	<div>
		<table class="table table-bordered table-striped table-hover definewidth m10">
			<caption class="tabletitle">用户消费记录</caption>
			<thead>
				<tr>
				<td>Date</td>
				<td>Order#</td>
				<td>Transaction description</td>
				<td>Balance</td>
				</tr>
			</thead>
			<tbody id="trRecords">
				<c:forEach items="${recordsList }" var="record">
					<tr>
						<td>${record.datatime }</td>
					<c:if test="${record.remark_id!=null}">
						<td style="word-wrap:break-word;word-break:break-all">${record.remark_id }
						<br/>
							<a href="http://192.168.1.206:10001/cbtconsole/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo=${record.remark_id }">订单详情</a>	
							<a>采购详情</a>	
							<a onclick="getOuthuo()">出货详情</a>	
						</td>
					</c:if>
					<c:choose>
						<c:when test="${record.type!=null }">
							<c:choose>
							<c:when test="${record.type==7 }">
							 <c:choose>
							  <c:when test="${record.paytype!=null and record.paytype==0 }">
						  		<td style="width: 340px">
									Paid ${record.currencyshow }${record.price} for goods(PayPal Transaction No: ${record.paymentId })
								</td>
							  </c:when>
							  <c:otherwise>
								<td>
									Paid ${record.currencyshow }${record.price} for goods(Used Balance)
								</td>
							  </c:otherwise> 
							</c:choose>
							</c:when>
							<c:when test="${record.type==1 }">
								<td>Refund ${record.currencyshow }${record.price} to your account, (order cancelled)</td>
							</c:when>
							</c:choose>
						</c:when>
					</c:choose>
						<td>
							${record.afterBalanceshow }
						</td>
					</tr>
				</c:forEach>
				
				
			</tbody>
			<tfoot>
					<c:forEach items="${refundList}" var="refund">
					<tr>
						<td>${refund.rdatatime}</td>					
						<td>N/A</td>
						<td>
							Withdrawing ${refund.currencyshow }${refund.rprice}
							<c:choose>
							<c:when test="${refund.state>-1 &&refund.state<2}">
								<br/>
								<span style="color:red">(under review)</span>
							</c:when>
							<c:when test="${refund.state==-1 }">
								<br/>
								<span style="color:red">(Withdraw Request Rejected)</span>
							</c:when>
							<c:when test="${refund.state==-2 }">
								<br/>
								<span style="color:red">(Has canceled)</span>
							</c:when>
							</c:choose>
						</td>
						<td>${refund.currencyshow }${refund.rbalanceAfter }</td>					
					</tr>
				</c:forEach>
			</tfoot>
		</table>
	</div>
	
	</div>
    <script type="text/javascript">
    	function getOrder(){
    	}
    	function getCaiGou(){
    	}
    	function getOuthuo(){
    	}
    </script>
</body>
</html>