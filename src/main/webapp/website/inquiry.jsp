<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>询价列表</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/main.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/spider.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/shopCar.css">
<style type="text/css">
.list li  span {
font-family: "Microsoft YaHei";
font-size: 16px;
padding-bottom: 20px;
color: black;
font-size: 18px;margin:0px 10px 0px 10px;
color: white;
line-height: 31px;
}
 
.list li a:HOVER{
text-decoration: none;
cursor: pointer;
color: red;
}
.list li a:active{
text-decoration: none;
cursor: pointer;
color: red;
}
.pages{ width:100.5%; text-align:right; padding:10px 0; clear:both;}
.pages span,.pages a,.pages b{ font-size:12px; font-family:Arial, Helvetica, 
sans-serif; margin:0 2px;}
.pages span font{ color:#f00; font-size:12px;}
.pages a,.pages b{ border:1px solid #5FA623; background:#fff; padding:2px 
6px; text-decoration:none}
.pages span { padding-right:10px }
.pages b,.pages a:hover{ background:#7AB63F; color:#fff;}

.cbt_d1{
width: 140px;
text-align: center;
}
.cbt_d2 {
border-right: none;
padding: 7px;
width: 70px; 
}
.products table .cbt_d3 {
border-left: none;
width: 295px;
padding-right: 10px;
line-height: 22px;
}
.products table .cbt_d4 {
width: 110px;
padding-left: 10px;
color:#FF6600;
font-weight: bold; 
}
.products table .cbt_d5 {
width: 95px;
padding-left: 5px;
}
.products table .cbt_d6 {
width: 105px;
color: #FF0000;
font-size: 18px;
padding-left: 5px;
}
.products table .cbt_d7 {
padding-left: 10px;
font-size: 14px;
}
.products table .cbt_d8 {
padding-left: 10px;
width: 70px;
}
.first{
border-right: 1px solid white;
margin-left: 10px;
}
.ibt {
height: 34px;
width: 950px;
margin: 10px 0 0 0px;
font-size: 14px;
border-radius: 5px;
border: 1px solid #B7B7B7;
}
.ibt td{
border-right: 1px solid #B7B7B7;
text-align: center;
}
.order_detailinfo td{
border-right: 1px solid #B7B7B7;
text-align: center;
border-bottom: 1px solid #B7B7B7;
}
.order_detailinfo{
border: #B7B7B7 1px solid;
width: 950px;
}
</style>
</head>
<script type="text/javascript">
function fnInquiry(){
	var state = $("#state").val();
	window.location = "/cbtconsole/processesServlet?action=getInquiryInfoWebsite&className=Inquiry&state="+state;
}
function fnSubmit(id,val,userid){
	var price = $(val).parent().parent().find("td:eq(5)").html();
	var state = $(val).parent().parent().find("td:eq(6)").html();
	 $.ajax({
	type:'POST',
	url:'/cbtconsole/processesServlet?action=upInquiryDetail&className=Inquiry',
	data:{userid:userid,id:id,state:state,price:price},
	success:function(res){
		if(res){
			alert("保存成功");
		}else{
			alert("保存失败");
		}
	}
});	 
}
function fnUp(val){
	$(val).parent().parent().find("td:eq(6)").html('<select id="state" onchange="fnChange(this)"> <option value="0" >未询价</option>'+
			'<option value="1"  >已询到价格</option> <option value="2" >未询价</option>');
}
function fnChange(val){
	if(val.value == "1"){
		$(val).parent().parent().find("td:eq(5)").html("<input type='text'/>");
	}else{
		$(val).parent().parent().find("td:eq(5)").html("0");
	}
}
</script>
<body>
	<br>
	<div align="center">
	<div class="spider_explain" align="center">
		<div style="width: 950px;height: 34px;border-radius: 5px;">
			状态：<select id="state" onchange="fnInquiry()">
				<option value="-1" ${state == -1 ? 'selected=selected':''} >所有</option>
				<option value="0" ${state == 0 ? 'selected=selected':''} >未询价</option>
				<option value="1" ${state == 1 ? 'selected=selected':''} >已询到价格</option>
				<option value="2" ${state == 2 ? 'selected=selected':''} >商品无效</option>
			</select>
		</div>
		 
		</div>
		<div style="width: 950px;">
		<table  class="ibt" style="font-size: 14px;" cellpadding="0" cellspacing="0">
			<tbody>
				<tr>
				    <td class="cbt_d5">id</td>
					<td  colspan="2" class="cbt_d1">商品</td>
					<td class="cbt_d2">订单号</td>
					<td class="cbt_d2">服务费用</td>
					<td class="cbt_d3">回应时间</td>
					<td class="cbt_d4">所询价格</td>
					<td class="cbt_d5">询价状态</td>
					<td class="cbt_d5">创建时间</td>
					<td class="cbt_d5">操作</td>
				</tr>
				</tbody></table>
				<div style="margin-top: 20px;width: 950px;">
				<table class="order_detailinfo" >
					<tr>
				    <td class="cbt_d5">id</td>
					<td  colspan="2" class="cbt_d1">商品</td>
					<td class="cbt_d2">订单号</td>
					<td class="cbt_d2">服务费用</td>
					<td class="cbt_d3">回应时间</td>
					<td class="cbt_d4">所询价格</td>
					<td class="cbt_d5">询价状态</td>
					<td class="cbt_d5">创建时间</td>
					<td style="width: 100px;">
								<button  onclick="fnUp(this);">修改</button>
								<button  onclick="fnSubmit(${info.id});">保存</button>
								
						</td>
				</tr>
				<c:forEach var="info" items="${individual}" varStatus="i">
					  
					<tr>
						<td class="cbt_d5">${info.id}</td>
						<td  class="cbt_d1" style="text-align: center;"><img onclick="location='${info.spider.url}'" style="width: 50px;width: 50px;" src="${info.spider.img_url}"/></td>
						<td  style="width: 150px;">${info.spider.name}</td>
						<td  style="width: 100px;">${info.payno}</td>
						<td  style="width: 100px;">50</td>
						<td style="width: 100px;">${info.deliverytime}</td>
						<td  style="width: 100px;">${info.state == 0?'等待询价':''}${info.state == 1?'已询到价格':''}${info.state == 2?'商品无效':''}</td>
						<td  style="width: 100px;">${info.createtime}</td>
						<td style="width: 100px;">
								<button  onclick="fnUp(this);">修改</button>
								<button  onclick="fnSubmit(${info.id},this,${info.userid});">保存</button>
								
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
		<div class="pages"><span>共<font><c:out value="${fn:length(order)}"></c:out> </font>条</span><b>1</b><!-- <a 
href="">2</a><a href="">3</a><a href="">4</a><a href="">5</a>...<a 
href="">168</a><a href="">下一页&gt;&gt;</a> --></div>
	</div>
	</div>
</body>
</html>