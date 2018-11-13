<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>出库打印</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="/cbtconsole/img/mathematics1.ico" type="image/x-icon"/>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/table.css">
<style>

.someclass {
	border-style : none;
	border-color : #FFFFFF;
}
.fontsomeclass {
	font-size : 18pt;
	font-weight : 900;
	font-family : monospace;
}

</style>

</head>
<body>
<c:set var="indexI" value="1"></c:set>
<c:forEach items="${oipflist}" var="oipf">  <!-- 用户id -->
	<c:forEach items="${oipf.maplist}" var="orderid"> <!-- id对应所有订单 -->

	<c:set var="begi" value="0"></c:set>
	<c:set var="cnt" value="4"></c:set>
	<c:set var="cntF" value="0"></c:set>
	<c:set var="curOrderid" value=""></c:set>
	
	<c:forEach begin="0"  end="${fn:length(orderid.value)}">
	<c:if test="${begi< (fn:length(orderid.value))}">
	<c:set var="cnt" value="4"></c:set>
	<c:set var="cntF" value="0"></c:set>
	<div id="idv${orderid.key }" style="width:640px;height:940px;margin-left:40px;margin-top:20px;">
	<c:set var="orderidkey" value="${orderid.key }"></c:set>
				<div id="hi${orderid.key }"> <!-- 用来计算高度 -->
		<c:if test="${curOrderid != orderidkey }" >
		<c:set var="curOrderid" value="${orderid.key }"></c:set>
		<c:set var="cntF" value="1"></c:set>
		  <!-- drop 不显示 -->
		  <c:if test="${oipf.isDropshipFlag==0}">
		   		<strong style="font-size: 25px"><img alt="" src="/cbtconsole/img/importexpress.png"></strong><br><br>
				<!-- <strong style="font-size: 18px">Address:</strong> No.999 West Zhongshan Road, Suite 416, Shanghai, China 200051<br> -->
				<strong style="font-size: 18px">Address:</strong>Room 605, Building No.1,Hui Yin Ming Zun Building, No.609 East Yun Ling Road, Putuo district, Shanghai, PR China, Zip code:200062<br>
				<strong style="font-size: 18px">Email：</strong>Contact@import-express.com<br>
				<strong style="font-size: 18px">Tel:</strong> (China) 21 61504007 ; (USA) 607 968 2368<br>
				www.import-express.com
				<div style="margin-top:10px;border:1px solid black"></div>
		  </c:if>
		  <c:if test="${oipf.isDropshipFlag==1}">
				<div style="height:90px;"></div>
		  </c:if>
				<div style="width:315px; height:150px; float:left;">
						
						<div style="height:20px;margin-top:10px;display:block">
							<div style="float:left"> <strong >Order No#:</strong> <span id="orderid">${orderid.key }</span></div>
						</div>
						<div style="height:20px;margin-top:15px;display:block">
							<div style="float:left"> <strong ></strong>  <font class="fontsomeclass" id="id">User Id#:${oipf.userid }</font></div>
						</div>
						 <c:if test="${oipf.isDropshipFlag==0}">
							<div style="height:20px;margin-top:15px;display:block">
								<div style="float:left"> <strong ></strong>  <font class="fontsomeclass" id="user name">User name:</font>${oipf.userName }</div>
							</div>
						</c:if>
						<div style="height:20px;margin-top:20px;display:block">
							<div style="float:left;"> <strong>Order Create Time:</strong> <span id="createtime">${oipf.createTime }</span></div>
						</div>
				</div>
				
				<div style="height:55px;margin-top:10px;display:block">
					<div style="float:left"><strong>Delivery Address:</strong></div>
					<div style="margin-left:150px">
						<div><span id="reciepients">${oipf.orderAddress }</span></div>
					</div>
				</div>
				</c:if>
					<c:if test="${curOrderid == orderidkey && cntF==0}"  > 
					
						<c:set var="cnt" value="6"></c:set>
					</c:if>
					<script type="text/javascript">
				//	alert('${curOrderid}'+','+'${cnt}');
					</script>
				<table id="" align="center" id="printtable"  style="width:640px;margin-top:120px;" >
						<tr>
							<td  width="30px">No </td>
							<td>Item  Id</td>
							<td>Item Img</td>
							<td width="300px">Item Description</td>
							<td>Quantity</td>
							<td>Warehouse location</td>
						</tr>
						
						<c:forEach begin="1"   end="${cnt}" varStatus="status">
								<c:if test="${begi < (fn:length(orderid.value))}">
									<c:set var="orderinfohr" value="${orderid.value[begi]}" />
									<tr id="tf${orderinfohr.goodsid }">
										<td height="140px">${begi+1}</td>
										<td>${orderinfohr.goodsid }</td>
										<td style="vertical-align:top;"><img width="100px" height="100px"  src="${orderinfohr.googs_img}" data-original="${orderinfohr.googs_img}"  /></td>
										<td style="vertical-align:top;"><span>${orderinfohr.goods_title}</span><br/>type:<br/>${orderinfohr.car_type}</td>
										<td>
										${orderinfohr.yourorder} 
										${orderinfohr.goodsUnit }
										<c:if test="${orderinfohr.seilUnit!=orderinfohr.goodsUnit  && orderinfohr.seilUnit !=null }">
										  ${orderinfohr.seilUnit } 
										</c:if>
										</td>
										<td>${orderinfohr.barcode}</td>
									</tr>
									<c:set var="begi" value="${begi+1 }"></c:set>
								</c:if>
								
						</c:forEach>	          
				</table>
				<div style="margin-top:10px">
					<!-- <div style="float:left">Checked by:${admname==null? 'Their own people': admname }</div> -->
					<!-- <div id="total" style="margin-left:560px">Total:${orderid.value}</div> -->
				</div>
				</div>
			</div>
			</c:if>
			</c:forEach>
				
		    
	</c:forEach>
</c:forEach>
            
<script language=javascript>   
window.print();
</script>
</body>
</html>