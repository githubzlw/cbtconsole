<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<title>到账详情</title>
</head>
<script type="text/javascript">

function confirmnamebtn(){
	var paytypeflag=$("#paytype option:selected").attr("id");
		
		$.post("/cbtconsole/cbt/orderws/paymentConfirm",{orderNo: '${param.orderNo}',userId: '${userId}',pass :userpass, paytypeid:paytypeflag},
    	function(result){
    		if(result.code == 0){
    			window.opener.fnPaymentConfirmClose(result.confirmtime,result.confirmname);
				window.close();
    		}else{
    			alert(result.msg);
    		}
  	  });
	}

 $().ready(function(){
	 /* $.post("/cbtconsole/WebsiteServlet",
				{action:'infos',className:'OrderwsServlet',orderNo:'${param.orderNo}',userId:'${param.userid}'}); */
}) 
</script>
<style type="text/css">
.ormtittdred{
    /*  color:red; */
}
</style>
<body style=" position:absolute; left:50%; top:20%; margin-left:-300px; margin-top:-150px; width:600px; height:300px;">
<div>
<c:choose>
	<c:when test="${empty paymentConfirm.id}">
		<h3>当前订单金额需支付:<td>${order.remaining_price}${order.currency}</td></h3>
	</c:when>
	<c:otherwise>
		<h3>当期订单已经确认支付过</h3>
	</c:otherwise>
</c:choose>
<div>
	<div>
		<div style="color: red">
			<h4>当前订单</h4>
			<div class="ornmatd1">订单号:<span class="ormnum" id="orderNo">${order.orderNo}</span> </div>
			<div><span class="ormnum">已付款金额：</span>${order.pay_price}<em id="currency">${order.currency}</em></div>
	 		<div><span class="ormnum">还需付款：</span>${order.remaining_price}<em id="currency">${order.currency}</em></div><br>
	 		<div>运费<span class="ormnum">${actual_ffreight_}<em id="currency">${order.currency}</em></span></div>
	 		<c:if test="${checkOrder > 0}">
	 			<br>
	 			<hr>
	 			<div><b style="color:red;font-size:20px;">订单金额校验结果:${checkMessage}</b></div>
	 		</c:if>
	 		<c:if test="${checkOrder == 0}">
	 			<br>
	 			<hr>
	 			<div><b style="font-size:20px;">订单金额校验通过</b></div>
	 		</c:if>
		</div><hr>
		<div>
		<c:if test="${ not empty payList }">
			<h3>关联订单到账的信息</h3>
			<c:forEach items="${payList}" var="paylist">
					<div>
						<div >订单号:<span class="ormtittdred" id="orderNo">${paylist.orderid}</span> </div><br>
				 		<div>流水号:<span class="ormtittdred">${paylist.paymentid}</span></div><br>
						<div class= "aaa">付款金额:<span class="ormtittdred">${paylist.payment_amount}<em id="currency">${paylist.payment_cc}</em></span></div><br>
				 		<div>用户名:<span class="ormtittdred">${paylist.username}</span></div><br>
				 		<div>付款状态:<span class="ormtittdred">
				 			<c:if test="${paylist.paystatus == 0}">支付失败</c:if>
				 			<c:if test="${paylist.paystatus == 1}">支付成功</c:if>
				 			<c:if test="${paylist.paystatus == 2}">进行中</c:if>
				 			<c:if test="${paylist.paystatus == 3}">退款</c:if>
				 			<c:if test="${paylist.paystatus == 4}">重复支付</c:if>
					 	</span></div><br>
				 		<div>本地交易申请号:<span class="ormtittdred">${paylist.paySID}</span></div><br>
				 		<div>支付标记:<span class="ormtittdred">
				 			<c:if test="${paylist.payflag == 'O'}">订单支付</c:if>
				 			<c:if test="${paylist.payflag == 'Y'}">运费支付</c:if>
				 			<c:if test="${paylist.payflag == 'N'}">修改商品价格后的订单支付金额</c:if>
				 		</span></div><br>
				 		<div>付款渠道:<span class="ormtittdred">
				 			<c:if test="${paylist.paytype == 0}">PayPal</c:if>
				 			<c:if test="${paylist.paytype == 1}">Wire Transfer</c:if>
				 			<c:if test="${paylist.paytype == 2}">余额支付
					 			&nbsp;&nbsp;&nbsp;&nbsp;<span>当前余额:</span>
					 			&nbsp;&nbsp;&nbsp;&nbsp;<span>${balance}</span>
				 			 </c:if>
				 			<c:if test="${paylist.paytype == 3}">订单拆分</c:if>
				 			<c:if test="${paylist.paytype == 4}">合并支付</c:if>
				 		</span></div><br>
				 		<div>付款时间:<span class="ormtittdred">${paylist.createtime}</span></div><br>
				 		
				 		<div>订单描述:<span class="ormtittdred">${paylist.orderdesc}</span></div><br>
				 		<div>拆单情况:
				 		<c:if test="${paylist.orderSplit=='0'}">
				 		  <span class="ormtittdred">未拆单</span>
				 		</c:if>
				 		<c:if test="${paylist.orderSplit=='1'}">
				 		  <span class="ormtittdred">前台自动拆单</span>&nbsp;&nbsp;
				 		 <span class="ormtittdred">总付款金额:<em style="color:red;font-weight: bold;">${paylist.payAll}</em><span>
				 		 <c:if test="${paylist.balancePay!='0'}">
				 		  (余额付款金额:<em style="color:#6A6AFF;font-weight: bold;">${paylist.balancePay}</em>)
				 		  </c:if>
				 		 </span></span>&nbsp;&nbsp;
				 		 <span class="ormtittdred">分配到本订单的金额:${paylist.payment_amount}</span>
				 		</c:if>
				 		<c:if test="${paylist.orderSplit=='2'}">
				 		  <span class="ormtittdred">后台手动拆单</span>&nbsp;&nbsp;
				 		  <span class="ormtittdred">总付款金额:${paylist.payAll}<span>
				 		  <c:if test="${paylist.balancePay!='0'}">
				 		  (余额付款金额:${paylist.balancePay})
				 		  </c:if>
				 		  
				 		  </span></span>&nbsp;&nbsp;
				 		  <span class="ormtittdred">分配到本订单的金额:${paylist.payment_amount}</span>
				 		</c:if>
				 		</div><br><br><hr><br>
					</div>
			</c:forEach>
		</c:if>
		</div>
		<div>
			<c:if test="${ not empty orders }">
				<h4>当前关联订单</h4>
				<c:forEach items="${orders}" var="order">
						<td width="20%" class="ormtittd">订单号:<span class="ormnum" id="orderNo">${order.orderNo}</span> </td>
						<td><span class="ormtittd">已付款金额:</span>${order.pay_price}<em id="currency">${order.currency}</em></td>
				 		<td><span class="ormtittd">还需付款:</span>${order.remaining_price}<em id="currency">${order.currency}</em></td><br>
				 		<td>运费:<span class="ormtittd">${actual_ffreight_}</span><em id="currency">${order.currency}</em></td> <br><br>
				</c:forEach><hr>
			</c:if>
		</div>
	</div>
</div>
	
	<br>
	<c:choose>
		<c:when test="${empty paymentConfirm.id}">
			<table style="display:none">
		        <tr>
		          <!--  <td>支付类型：</td>
		           <td> 
			           <select id="paytype" name="paytype" onchange="getPayType(this.options[this.selectedIndex].id)">
				            <option id="pay_0" value="PayPal" selected="selected">PayPal</option>
				            <option id="pay_1" value="Wire Transfer">Wire Transfer</option>
				            <option id="pay_2" value="yue pay">余额支付</option>
			            </select>
		           </td>
		           <td><input type="hidden" name="" value="用户密码"/></td>
		           <td><input type="hidden" name="userpass" id="userpass"/></td>
		        </tr>
		        <tr>
		            -->
		          <td colspan=4><input type="button" style="width: 220px;font-size: 35px; " onclick="window.open('/cbtconsole/website/paycheck_new.jsp?userid=${param.userId}','_blank')" value="客户到账记录" /></td>
		           <td colspan=4><input type="button" style="width: 150px;font-size: 35px; margin-left: 148px;" onclick="confirmnamebtn();" value="确认" /></td>
		        
		        </tr>
		    </table>
		</c:when>
		<c:otherwise>
			<div>
				<h3>
					确认支付人:<em>${paymentConfirm.confirmname}</em>&nbsp;&nbsp;
					确认支付时间:<em>${paymentConfirm.confirmtime}</em>&nbsp;&nbsp;
				</h3>
				<h3>确认支付类型:<em>${paymentConfirm.paytype}</em>
				<em><input type="button" style="width: 220px;font-size: 35px;margin-left: 148px;" onclick="window.open('/cbtconsole/website/paycheck_new.jsp?userid=${param.userId}','_blank')" value="客户到账记录" /></em>
				</h3>
			</div>
		</c:otherwise>
	</c:choose>
	
	 
</div>
</body>
</html>