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
<title>搜索产品</title>
<script type="text/javascript">
	
	function fnadd(obj,purl,name,price,img,url,goodId,orderNo){
		var checkFlag= $("#"+obj).is(':checked');
		$.ajax({
			type:'POST',
			async:false,
			url:'/cbtconsole/WebsiteServlet?action=addSource&className=GoodsWebsiteServlet',
			data:{purl:purl,name:name,price:price,img:img,url:url,goodsid:goodId,checkFlag:checkFlag},
			success:function(){	
			
			}
		});
	}
	
</script>
<style type="text/css">
	ul li{list-style:none;}
</style>
</head>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
	<div style="width:1280px;margin:0 auto;position:relative;border:1px solid #777;">
		<a style="text-decoration: none;" href="/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo=${orderNo}"  target="_blank">
			<input id="save" type="button" style="height: 30px;position:absolute;top:-40px;right:0;" value="保存货源" >	<br>
		</a>
<%-- 		<table id="table" align="center" border="1px" style="font-size: 13px;" width="300" bordercolor="#8064A2" cellpadding="10px;" cellspacing="15px;" style="float:left;">
			<c:forEach items="${cgbList }" var="gbb" varStatus="i">
				<Tr id="tr${i.index}">
					<td style="width: 200px;">
						<a target='_blank' href="${gbb.tbUrl }" >
							<img  width='200px' title="" height='200px;' src="${gbb.tbImg }">
						</a>
						<br>
						${gbb.tbName }
						<br>
						RMB ${gbb.tbprice }
						<br>
 						相似度 ${gbb.score }
						<br>
						Add to supply of goods：
						<input type="checkbox" class="checkbox"id="check${i.index}"
				              	onclick="fnadd('check${i.index}','${gbb.tbUrl }','${gbb.tbName }','${gbb.tbprice }','${gbb.tbImg }','${gbb.aligSourceUrl}','${gbb.goodsDataId}','${gbb.orderNo}')" style="width: 20px;height: 20px;">
					</td>
				</Tr>
			</c:forEach>
		</table> --%>
		<ul>
			<c:forEach items="${cgbList }" var="gbb" varStatus="i">
				<li style="width:280px;float:left;margin:10px;" id="tr${i.index}">
					<div style="border:1px solid #777;padding:10px;position:relative;height:330px;">
						<a target='_blank' href="${gbb.tbUrl }" >
								<img  width='200px' title="" height='200px;' src="${gbb.tbImg }">
							</a>
							<br>
							${gbb.tbName }
							<br>
							RMB ${gbb.tbprice }
							<br>
	 						相似度 ${gbb.score }
							<br>
							Add to supply of goods：
							<input type="checkbox" class="checkbox"id="check${i.index}"
					              	onclick="fnadd('check${i.index}','${gbb.tbUrl }','${gbb.tbName }','${gbb.tbprice }','${gbb.tbImg }','${gbb.aligSourceUrl}','${gbb.goodsDataId}','${gbb.orderNo}')" style="width: 20px;height: 20px;">
					</div>
				</li>
			</c:forEach>
		</ul>
		
		<div style="clear:both;"></div>
	</div>
</body>
</html>