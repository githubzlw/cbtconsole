<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<title>未匹配的申诉退款</title>
<style type="text/css">
body{width: 1800px;margin: 0 auto;}
.body{width: 1800px;height:auto;}
/* .error{background-color:#FF8484;color:white;}
.error2{background-color:#93926C;color:white;}
.error3{background-color: yellow;;color:red;}
.error4{background-color:#00FFFF;} */
a{
	cursor: pointer;
	text-decoration:underline;
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
 .inputtext{
 height:25px;
 width:250px;
 
    }

</style>
<script type="text/javascript">
function fnjump(obj){
	var page=$("#page").val();
	if(page==""){
		page = "1";
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
	}else if(obj==-2){
		page=1;
	}
	
	$("#page").val(page);
	window.location.href="/cbtconsolee/refundss/rlist?page="+page
}


function fnupdate(id){
	var userid = $("#userid_"+id).val();
	var orderid = $("#orderid_"+id).val();
	var paypalname = $("#paypal_"+id).val();
	if(userid=='' && orderid == '' && paypalname == ''){
		return ;
	}
	$.ajax({
		type:'POST',
		dataType:'text',
		url:'/cbtconsole/refundss/update',
		data:{id:id,userid:userid,orderid:orderid,paypalname:paypalname},
		success:function(res){
			if(res>0){
				window.location.reload();
			}else{
				alert('添加失败，请重新添加');
			}
		},
		error:function(XMLResponse){
			alert('error');
		}
	});
	
}

</script>

</head>
<body class="body">
<div align="center">
	<br>
	<div >
	<br>
	<div align="center" style="font-size: 20px;font-weight: bold;" >
	未匹配的申诉退款
	</div>
	<br>
	<table id="table"  border="1" cellpadding="0" cellspacing="0" class="table" >
	<tr align="center"  bgcolor="#DAF3F5" >
			<td style="width: 50px;" ></td>
			<td style="width: 200px;" >申诉流水号</td>
			<td style="width: 150px;">申诉金额</td>
			<td style="width: 150px;">货币单位</td>
			<td style="width: 200px;">申诉时间</td>
			<td style="width: 250px;" >用户ID</td>
			<td style="width: 250px;" >订单号</td>
			<td style="width: 250px;" >paypal账号</td>
			<td style="width: 100px;" >操作</td>
	</tr>
	
	
	<c:forEach items="${refundList}" var="list" varStatus="index" >
	<tr  bgcolor="#FFF7FB">
	<!-- 索引 -->
	<td >${index.index+1}</td>
	<!-- 申诉流水号 -->
	<td id="payid_${list.id}">
	   ${list.payid}
	</td>
	<!-- 申诉金额  -->
	<td  style="text-align: center;">
	<span style="font-size: 14px;" id="appcount_${list.id}">
	<a href="/cbtconsole/refundss/query?appcount=${list.appcount}&curreny=${list.currency}" target="_blank">${list.appcount}</a></span>
	</td>
	<!-- 货币单位  -->
	<td  style="text-align: center;">
	<span style="font-size: 14px;" id="currency_${list.id}">${list.currency}</span>
	</td>
	<!-- 申诉时间  -->
	<td id="time_${list.id}" style="text-align: center;">
	<span >${list.apptime}</span>
	</td>
	
	<!-- 用户id  -->
	<td  style="text-align: center;">
	
	<input type="text" id="userid_${list.id}" value="${list.userid == 0 ? '':list.userid}" class="inputtext">
	</td>
	<!-- 订单号  -->
	<td  style="text-align: center;">
	<input type="text" id="orderid_${list.id}" value="${list.orderid}" class="inputtext">
	</td>
	<!-- 申诉账号  -->
	<td  style="text-align: center;">
	<input type="text" value="${list.paypalname}" id="paypal_${list.id}" class="inputtext">
	</td>
	
	<!-- 操作 -->
	<td id="action_${list.id}" style="text-align: center;">
	<input class="btn" type="button" value="录入信息" id="update" onclick="fnupdate(${list.id})">
	</td>
	</tr>
	</c:forEach>
	
	</table>
		<br>
		<div>
		<input type="hidden" id="totalpage" value="${pagecount}">
		
		总共:&nbsp;&nbsp;<span id="pagetotal">${pagenow}<em>/</em> ${pagecount}</span>
		页&nbsp;&nbsp;
		<input type="button" value="上一页" onclick="fnjump(-1)" class="btn">
		<input type="button" value="下一页" onclick="fnjump(1)" class="btn">
		
		第<input id="page" type="text" value="${pagenow}" style="height: 26px;" >
		<input type="button" value="查询" onclick="fnjump(0)" class="btn">
		</div>
		<br><br>
		<br><br>
		</div>
</div>
</body>
</html>