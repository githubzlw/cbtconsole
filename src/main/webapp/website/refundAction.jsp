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
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<style type="text/css">
.mid{
	margin-left: 25%;	
}
.title{
	margin-left: 40%;
	color:red;
	font-size:2em;	
}
</style>

<title>Individual Center</title>
</head>

<body>
	<p class="title">退款操作</p>
	<div class="mid">
	<div onclick="javascript:history.back(-1)" style="cursor: pointer;color:#00aff8;">« Back</div>
	<div>
		<!-- 头部   余额和申请数目 -->
		<div class="rowll">
			<span>用户ID：</span>		
			<!-- ${balance}  ${currency}   ${currency}	-->
			<span id="userId" style="margin-left: 15px">${uid }</span>&nbsp;
			<span>用户余额：</span>		
			<span id="presentBalance" style="margin-left: 15px">${balance}</span>&nbsp;
			<span id="currency" style="margin-left: 5px">${currency}</span>		
			<span>Apply Refund Amount：</span>
			<input type="text" id="appcount" disabled="disabled" value="${appcount }" name="appcount" >
			<span id="currency" style="margin-left: 5px">${appcurrency}</span>	
			<input class="btn btn-primary btn-sm" type="button" onclick="getRefundNow()" value="退款" style="margin-left: 255px">	
		</div>
		
		<!--中部   退款列表或账号    默认显示到账列表，若是没有可退款的单子    显示选择  PayPal帐号界面   -->
		<div>
			<div id="area" style="width: 966px">
			<!-- 拿走 -->
				<div>
				<table class="table table-bordered table-striped table-hover definewidth m10">
					<caption class="tabletitle">可退款交易列表</caption>
					<thead>
						<tr>
						<td>日期</td>
						<td>付款方式</td>
						<td>paypal流水号</td>
						<td>交易金额</td>
						<td>货币</td>
						<td>操作</td>
						</tr>
					</thead>
					<tbody id="refundListTab">
						<c:forEach items="${paymentList}" var="item">
							<c:if test="${status==1}">
								<tr>
									<td>${item.createtime}</td>
									<c:choose>
										<c:when test="${item.paytype==0}">
											<td>PayPal</td>
										</c:when>
										<c:when test="${item.paytype==1}">
											<td>WireTransfer</td>
										</c:when>
										<c:when test="${item.paytype==2}">
											<td>Balance</td>
										</c:when>
									</c:choose>
									<td>${item.orderid}</td>
									<td>${item.payment_amount}</td>
									<td>${item.payment_cc}</td>
									<td id="selectcol">
										<input type="checkbox" name="paymentid" value="${item.id}">
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			
			
				<table class="table table-bordered table-hover definewidth m10" id="copyRefundList">
					<caption class="tabletitle">即将退款列表</caption>
					<thead>
						<tr>
							<td>日期</td>
							<td>付款方式</td>
							<td>paypal流水号</td>
							<td>交易金额</td>
							<td>货币</td>
							<td>退款金额</td>
						</tr>
					</thead>
					<tbody id="selectPaymentList">
						
					</tbody>
				</table>
				</div>
		<div>
		<span style="color: red">Note:对于申请金额大于可退款列表金额总和的用户，只能线下退款进行补足。</span>
			<table class="table table-bordered table-hover definewidth m10" id="extraRefund">
				<caption class="tabletitle">额外退款</caption>
				<thead>
					<tr>
						<td>日期</td>
						<td>付款方式</td>
						<td>paypal流水号</td>
						<td>交易金额</td>
						<td>货币</td>
						<td>退款金额</td>
					</tr>
				</thead>
				<tbody id="extraData">
				</tbody>
			</table>
		</div>
			</div>
			<form id="payForm" style="display: none" method="post">
				<!-- 交易号 -->
				<input id="subTid" name=""/>
				<!-- 退款金额 -->
				<input id="submoney"/>
				<!-- 退款货币 -->
				<input id="subcurrency"/>
			</form>
		</div>
		
	</div>
	</div>
	<script type="text/javascript">
$(function(){
	<%String[] userinfo = WebCookie.getUser(request);
	int userid = userinfo != null ? Integer.parseInt(userinfo[0]) : 0;%>
	checkPay();
	
/* 	 $("#otherAccount").onblur(function(){
		 var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		 alert(this.val());
		 if (filter.test(this.val()))
			 return true;
		 else {
		 	alert('您的电子邮件格式不正确');
		 	return false;
		 }
	}) 
	 */
	 
	 
	$("#selectcol input[type='checkbox']").on("change",function(){
		var row = $(this).parent().parent().index() + 1;
		var balance=$("#presentBalance").html()*1;   //取出用户余额   并转化为小数
		var applyMoney=$("#appcount").val()*1;   //取出用户输入的提现金额   并转化为小数
	   if(this.checked){
			var beforeTxt=$(this).parent().parent().children().last().html();
			var cloneTxt = $(this).parent().parent().html();
			var thisRowMoney =this.parentNode.parentNode.childNodes[7].innerHTML*1;
			if(applyMoney!=0){
/* 				html ='<tr id="appdend_(\''+row+'\')">'; */
				html ='<tr id="appdend_'+row+'">';
			    sum = count();
				sum += this.parentNode.parentNode.childNodes[7].innerHTML*1;
				var chazhi = 0;
				chazhi = sum - applyMoney;
				if(chazhi>0){
				/* 	alert(thisRowMoney); */
					thisRowMoney -= chazhi;
					cloneTxt = cloneTxt.replace(beforeTxt,thisRowMoney);
				}else{
					cloneTxt = cloneTxt.replace(beforeTxt,thisRowMoney);
				}
				html+=cloneTxt;
				html+="</tr>";
				$("#selectPaymentList").append(html);
			}else{
				html ='<tr id="appdend_'+row+'">';
				sum = count();
				sum += this.parentNode.parentNode.childNodes[7].innerHTML*1;
				var chazhi = 0;
				chazhi = sum - balance;
				if(chazhi>0){
					thisRowMoney -= chazhi;
					cloneTxt = cloneTxt.replace(beforeTxt,thisRowMoney);
				}else{
					cloneTxt = cloneTxt.replace(beforeTxt,thisRowMoney);
				}
				html+=cloneTxt;
				html+="</tr>";
				$("#selectPaymentList").append(html);
			}
		}else{
			$("#appdend_"+row).remove();
		}
		
	});
	
}) 
var sum = 0;	
function count(){
	$(":checkbox[checked]").each(function(){
		if(this.checked == true){
	    	var tmp = this.parentNode.parentNode.childNodes[7].innerHTML*1;
	    	sum+=tmp;
	    }
	});
	return sum;
}

/* function getRefundNow(){
	$("#selectPaymentList tr").each(function(){
		var tid = $(this).children("td").eq(2).html().trim();
		var currency = $(this).children("td").eq(4).html().trim()
		var refundMoney = $(this).children("td").eq(5).html().trim()
		$("#payForm").attr("action","https://api.sandbox.paypal.com/v1/payments/sale/"+tid+"/refund");
		$('#subTid').val(tid);
		$('#submoney').val(refundMoney);
		$('#currency').val(currency);
		payForm.submit();
	})
} */
function getRefundNow(){
 	$("#selectPaymentList tr").each(function(){
		$('#subTid').val(tid);
		$('#submoney').val(refundMoney);
		$('#currency').val(currency);

		var tid = $(this).children("td").eq(2).html().trim();
		var currency = $(this).children("td").eq(4).html().trim();
		var refundMoney = $(this).children("td").eq(5).html().trim();
		$.ajax({
			type:'POST',
			url:"https://api.sandbox.paypal.com/v1/payments/sale/"+tid+"/refund",
			data:{
				"total":refundMoney,
				"currency":currency
			},
			contentType: "application/json",
			/* headers: {
                "Authorization": "Basic " + Base64_Encode("" + ":" + clientSecret)
            }, */
			success:function(res){
				alert(res);
			}
		});
		
	}) 
	
	
}

function checkPay(){
	var str = $("#accountss").html();	
	if(str==""){
		$("#addonChannel").hide();
	}else{
		$("#addonChannel").show();
		$("#selectAccountArea").hide();
	}
	
}

</script>

</body>
</html>