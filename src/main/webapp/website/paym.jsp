<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="/cbtconsole/CbtDA/js/jquery-1.10.2.js"></script>
<title>Shopping cart</title>
<script type="text/javascript">
  function fn( ){
	  var userid = 80;
	  if(userid == ""){alert("请输入用户ID")}
		$.post("/cbtconsole/CbtDA/OrdersPayServlet",
  			{userid:userid},
  			function(res){
			  var json = eval(res); 
			  var row = 1;
			  for (var i = json.length-1; i >= 0; i--) {
				  rs.getString("paymentid"),rs.getString("userid"),rs.getString("orderid"),rs.getInt("pstate"),rs.getInt("ostate"),rs.getInt("payment_amount"),rs.getInt("addressid")
				  $("#table tr:eq("+(row-1)+")").after("<tr></tr>");
				  $("#table tr:eq("+row+")").append("<td>"+(i+1)+"</td>");
				  $("#table tr:eq("+row+") td:eq(0)").after("<td>"+json[i][1]+"</td>");
				  $("#table tr:eq("+row+") td:eq(1)").after("<td>"+json[i][2]+"</td>");
				  $("#table tr:eq("+row+") td:eq(2)").after("<td>"+json[i][0]+"</td>");
				  $("#table tr:eq("+row+") td:eq(3)").after("<td>"+json[i][3]+"</td>");
				  $("#table tr:eq("+row+") td:eq(4)").after("<td>"+json[i][4]+"</td>");
				  $("#table tr:eq("+row+") td:eq(5)").after("<td>"+json[i][5]+"</td>");
				  $("#table tr:eq("+row+") td:eq(6)").after("<td>"+json[i][6]+"</td>");
				  $("#table tr:eq("+row+") td:eq(7)").after("<td><input type='button' onclick='fnUpdateState('"+json[i][2]+"')'></td>");
			}
	});
  }
  
  function fnUpdateState(orderid){
	  $.post("/cbtconsole/UPOrdersPayServlet",
	  			{orderno:orderid},
	  			function(res){
				    alert(res);
		});
  }
</script>
</head>
<body onload="fn();">
	<div>
		<table id="table"  border="1" cellpadding="0" cellspacing="0">
			<tr>
				<td></td>
				<td>用户ID</td>
				<td>订单号</td>
				<td>现支付返回状态</td>
				<td>数据库显示状态</td>
				<td>订单状态</td>
				<td>金额</td>
				<td>用户地址id</td>
				<td>操作</td>
			</tr>
		</table>
	</div>
</body>
</html>