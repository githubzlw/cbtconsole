<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>Merge Order</title>
<script type="text/javascript">
  function fn( ){
	  var orderid = $("#orderid").val();
		$.post("/cbtconsole/paysServlet",
  			{action:'mergeOrder',className:'MergeOrder',orderid:orderid},
  			function(res){
				
  			});
  }  	  
</script>
</head>
<body>
	<div>
		<table>
			<tbody>
				<tr>
					<td>orderid:<input type="text" id="orderid"> <input type="button" value="合并" onclick="fn();"></td>
				</tr>
			</tbody>
		</table>
		<br/>
		<div>
		<table id="table" border="1px" style="font-size: 13px;" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<Tr>
				<td>序号</td>
				<td>id</td>
				<td>用户ID</td>
				<td>url</td>
				<td>title</td>
				<td>img</td>
				<td>price</td>
				<td>number</td>
				<td>size</td>
				<td>color</td>
				<td>交期</td>
				<td>remark</td>
				<td>添加时间</td>
				<td>是否免邮</td>
				<td>批发价</td>
			</Tr>
			
		</table>
	</div>
	</div>
</body>
</html>