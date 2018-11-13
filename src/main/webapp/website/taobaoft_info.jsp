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
<title>辅图产品</title>
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
</head>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
	<div>
		<a style="text-decoration: none;" href="/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo=${orderNo}"  target="_blank">
			<input id="save" type="button" style="height: 30px; margin-top: -20px; margin-left: 1358px;" value="保存货源" >	<br>
		</a>
		<br>
		<table id="table" align="center" border="1px" style="font-size: 13px;" width="1200" bordercolor="#8064A2" cellpadding="10px;" cellspacing="15px;" >
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
						Add to supply of goods：<input type="checkbox" class="checkbox"id="check${i.index}"
				              	onclick="fnadd('check${i.index}','${gbb.tbUrl }','${gbb.tbName }','${gbb.tbprice }','${gbb.tbImg }','${gbb.aligSourceUrl}','${gbb.goodsDataId}','${gbb.orderNo}')" style="width: 20px;height: 20px;">
					</td>
					<td style="width: 200px;">
						<a target='_blank' href="${gbb.tbUrl1 }" >
							<img  width='200px' title="" height='200px' src="${gbb.tbImg1 }">
						</a>
						<br>
						${gbb.tbName1 }
						<br>
						RMB ${gbb.tbprice1 }
						<br>
						Add to supply of goods：<input type="checkbox" class="checkbox"id="check${i.index}${i.index}"
							onclick="fnadd('check${i.index}${i.index}','${gbb.tbUrl1 }','${gbb.tbName1 }','${gbb.tbprice1 }','${gbb.tbImg1 }','${gbb.aligSourceUrl}','${gbb.goodsDataId}','${gbb.orderNo}')" style="width: 20px;height: 20px;">
					</td>
					<td style="width: 200px;">
						<a target='_blank' href="${gbb.tbUrl2 }" >
							<img  width='200px' title="" height='200px' src="${gbb.tbImg2 }">
						</a>
						<br>
						${gbb.tbName2 }
						<br>
						RMB ${gbb.tbprice2 }
						<br>
						Add to supply of goods：<input type="checkbox" class="checkbox"id="check${i.index}${i.index}${i.index}"
							onclick="fnadd('check${i.index}${i.index}${i.index}','${gbb.tbUrl2 }','${gbb.tbName2 }','${gbb.tbprice2 }','${gbb.tbImg2 }','${gbb.aligSourceUrl}','${gbb.goodsDataId}','${gbb.orderNo}')" style="width: 20px;height: 20px;">
					</td>
					<td style="width: 200px;">
						<a target='_blank' href="${gbb.tbUrl2 }" >
							<img  width='200px' title="" height='200px' src="${gbb.tbImg3 }">
						</a>
						<br>
						${gbb.tbName3 }
						<br>
						RMB ${gbb.tbprice3 }
						<br>
						Add to supply of goods：<input type="checkbox" class="checkbox"id="check${i.index}${i.index}${i.index}${i.index}"
							onclick="fnadd('check${i.index}${i.index}${i.index}${i.index}','${gbb.tbUrl3 }','${gbb.tbName3 }','${gbb.tbprice3 }','${gbb.tbImg3 }','${gbb.aligSourceUrl}','${gbb.goodsDataId}','${gbb.orderNo}')" style="width: 20px;height: 20px;">
					</td>
				</Tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>