<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>订单报表</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="/cbtconsole/img/mathematics1.ico" type="image/x-icon"/>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/table.css">
<style>
@media print{
　　.noprint{
  　　display:none
　　}
}
</style>
<script language=javascript>   
$(function(){
	var orderid=location.search.split('orderid=')[1].split('&state=')[0];
	var state=location.search.split('orderid=')[1].split('&state=')[1];
	var id = location.search.split('?id=')[1].split('&orderid=')[0];
	$.post("/cbtconsole/WebsiteServlet?action=getOrderAddress&className=OrderwsServlet",{orderid:orderid},
			 function(res){
				 var json=eval(res);
				 $('#reciepients').html(json[0].recipients+'<br>'+json[0].address+','+json[0].street+'<br>'+json[0].address2+','+json[0].statename+'<br>'
						 +json[0].countryname+'<br>'+json[0].phone_number);
		}); 
	$.post("/cbtconsole/WebsiteServlet?action=getPrintDetail&className=OrderwsServlet",{orderNo:orderid},
			 function(data){
				var json=eval(data);
				var total=0;
				for(var i=0;i<json.length;i++){
					$('#printtable tr:eq('+i+')').after('<tr></tr>');
					$('#printtable tr:eq('+(i+1)+')').append('<td width="70px">'+(i+1)+'</td>');
					$('#printtable tr:eq('+(i+1)+')').append('<td width="150px"><img width="50px" height="50px" src="'+json[i].goods_img+'"><br><span style="font-size:10px">'+json[i].goods_type+'</span></td>');
					$('#printtable tr:eq('+(i+1)+')').append('<td width="400px">'+json[i].goodsname+'</td>');
					$('#printtable tr:eq('+(i+1)+')').append('<td width="70px">'+json[i].yourorder+'</td>');
					total+=json[i].yourorder;
				}
				document.getElementById("createtime").innerHTML=json[0].createtime;
				document.getElementById("total").innerHTML='Total:'+total;
				document.getElementById("orderid").innerHTML=orderid+";";
				document.getElementById("id").innerHTML=id;
				window.print();
		});
});
</script>
</head>
<body>
<div class="noprint" style="width:640px;height:20px;margin:100px auto 0 auto;font-size:12px;text-align:right;">
    <OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="wb" width="0">
    </OBJECT>
</div>
<div style="width:640px;height:624px;margin:20px auto;">
	<strong style="font-size: 25px"><img alt="" src="/cbtconsole/img/importexpress.png"></strong><br><br>
	<strong style="font-size: 18px">Address:</strong> No.999 West Zhongshan Road, Suite 416, Shanghai, China 200051<br>
	<strong style="font-size: 18px">Email：</strong>Contact@import-express.com<br>
	<strong style="font-size: 18px">Tel:</strong> (China) 21 61504007 ; (USA) 607 968 2368<br>
	www.import-express.com
	<div style="margin-top:20px;border:1px solid black"></div>
	<div style="height:20px;margin-top:20px;display:block">
		<div style="float:left"><strong>Order No#:</strong><span id="orderid"></span></div>
		<div style="float:left"><strong>User Id#:</strong><span id="id"></span></div>
		<div style="float:right"><strong>Order Create Time:</strong><span id="createtime"></span></div>
	</div>
	<div style="height:90px;margin-top:10px;display:block">
		<div style="float:left"><strong>Delivery Address:</strong></div>
		<div style="margin-left:150px">
			<div><span id="reciepients"></span></div>
		</div>
	</div>
	<table id="printtable" width="640px" style="margin-top:10px">
		<tr><td>No</td><td>Item Img</td><td>Item Description</td><td>Quantity</td></tr>
	</table>
	<div style="margin-top:10px">
		<div style="float:left">Checked by:</div>
		<div id="total" style="margin-left:560px"></div>
	</div>
</div>
 
<div id="dd"></div>
</body>
</html>