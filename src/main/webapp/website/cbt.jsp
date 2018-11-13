<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>cbt DA</title>
</head>
<script>
	function fnGoods(type){
		$.post("/cbtconsole/GoodsServlet", {
			type : type
		}, function(res) {
			var json = eval(res);
			$("#table tbody tr").eq(0).nextAll().remove();
			for (var i = 0; i < json.length; i++) {
				$("#table tr:eq("+(i)+")").after("<tr></tr>");
				$("#table tr:eq("+(i)+")").after("<td>"+(i+1)+"<input type='checkbox' onchange='fnPrice()' checked='checked'></td><td>"+json[i][2]+"</td><td>"+json[i][3]+"</td><td>"+json[i][1]+"</td><td>"+json[i][0]+"</td>");
			}
			fnPrice();
		});
	}
	//总额计算
	function fnPrice(){
		var price = 0;
		$("#table :checkbox:gt(0) ").each(function(){
			if($(this).attr("checked")){
				price = price + parseFloat($(this).parent().next().next().next().next().html());
			}
		});
		$("#sumprice").html(price);
	}
</script>
<body onload="fnGoods(1)">
<div align="center">
	<div style="width: 1000px;">
	<a>查看用户信息</a>
	<div><input type="radio" checked="checked" name="usertype" value="1" onchange="fnGoods(1)">用户添加<input type="radio" name="usertype" value="2" onchange="fnGoods(2)">游客添加</div>
	<div>购物车中列表总金额：<em id="sumprice">0</em></div>
	
		<table id="table"  border="1" cellpadding="0" cellspacing="0">
			<tr>
				<td>item<input type="checkbox" checked='checked' onchange='fnPrice()'></td>
				<td>用户ID</td>
				<td>用户名称</td>
				<td>购物车数量</td>
				<td>购物车总额</td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>