<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>给客户发信</title>
<script type="text/javascript">
	
/* 	function sendEmail(){
		
		var orderNo= $("#table tr:eq(1)").children("td").find("input[id='orderNo']").val();
		var userId= $("#table tr:eq(1)").children("td").find("input[id='userId']").val();
		var email= $("#table tr:eq(1)").children("td").find("input[id='email']").val();
		
		$.ajax({
			type:'POST',
			url:'/cbtconsole/customerServlet?action=sendEmail&className=OrdersPurchaseServlet',
			data:{orderNo:orderNo,userId:userId,email:email},
			success:function(){	
				alert("Send success");
			}
		});
	} */
	
	function sendEmail(orderno){
		//var actual_ffreight = $("#actual_ffreight").val();
		var orderNo= $("#table tr:eq(1)").children("td").find("input[id='orderNo']").val();
		var userId= $("#table tr:eq(1)").children("td").find("input[id='userId']").val();
		var email= $("#table tr:eq(1)").children("td").find("input[id='email']").val();
		var sendEmail= $("#table tr:eq(1)").children("td").find("input[id='sendEmail']").val();
		//var orderNo = $("#orderNo").val();
		window.open("/cbtconsole/website/sendEmail_substitution.jsp?orderno="+orderNo+"&email="+email+"&sendEmail="+sendEmail+"&userId="+userId,"windows","height=900,width=1100,top=200,left=500,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no");
	}
	
	function showDetail(url){
		window.open("/cbtconsole/processesServlet?action=getSpider&className=SpiderServlet&showFlag=1&url="+ encodeURIComponent(url));
	}
</script>
</head>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
	<div>
	<div align="center" style="font-size: 20px;font-weight: bold;" >1-2</div>
		<table id="table" align="center" border="1px" style="font-size: 13px;" width="900" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>原产品图片</th>
				<th>替代产品图片</th>
				<th>替换产品名</th>
				<th>ImportExpress替代产品链接</th>
				<th>替代产品价格RMB</th>
				<th>替代产品规格</th>
				<!-- <th>替换产品</th> -->
<!-- 				<th>产品英文名</th>
				<th>产品价格</th>
				<th>产品规格</th> -->
			</Tr>
			<c:forEach items="${cgbList }" var="gbb" varStatus="i">
			<Tr id="tr${i.index}">
				<td><img  width='100px' title="" height='100px;' src="${gbb.aliimg }"></td>
				<td><img  width='100px' title="" height='100px;' src="${gbb.chagoodimg }"></td>
				<td>${gbb.chagoodname}
				<input type="hidden" id="userId" value="${gbb.userId}"/>
				<input type="hidden" id="email" value="${gbb.email}"/>
				<input type="hidden" id="orderNo" value="${gbb.orderno}"/>
				<input type="hidden" id="sendEmail" value="${sendEmail}"/>
				
				</td>
				<td><a onclick="showDetail('${gbb.chagoodurl }')" href="javascript:void(0)" >Info</a></td>
				<td>${gbb.chagoodprice }</td>
				<td>${gbb.goodstype1}
				<%-- <td>${gbb.chagoodname }</td> --%>
<%-- 				<td><input type="text" id="aliname" value="${gbb.aliname }" maxlength="255"/></td>
				<td><input type="text" id="chagoodprice" value="${gbb.maxprice }" maxlength="10"/></td>
				<td><input type="text" id="goodsType" value="${gbb.goodsType }" maxlength="255"/>
				<input type="hidden" id="quantity" value="${gbb.quantity }"/> --%>
				</td>
				
			</Tr>
			</c:forEach>
		</table>
		<div style="width:180px; margin:auto;" >
			<button onclick="sendEmail()"  style="">给客户发信</button>
		</div>
	</div>
</body>
</html>