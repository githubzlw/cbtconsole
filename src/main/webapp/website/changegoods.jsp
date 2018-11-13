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
<title>替代产品</title>
<script type="text/javascript">
	
	function saveInfo(){
		var tbLen = $("#table").find("tr").length;
		var list= new Array();
		for(var i=1;i<tbLen;i++){
			var changeUrl = $("#table tr:eq("+i+")").children("td").find("a").attr("href");
			//var enName = $("#table tr:eq("+i+") td:eq(3)").find("input[id='aliname']").val();
			var enName = $("#table tr:eq("+i+")").children("td").find("textarea[id='aliname']").val();
			
			var price= $("#table tr:eq("+i+")").children("td").find("input[id='chagoodprice']").val();
			var goodsType= $("#table tr:eq("+i+")").children("td").find("textarea[id='goodsType']").val();
			var quantity= $("#table tr:eq("+i+")").children("td").find("input[id='quantity']").val();
			var goodsCarId= $("#table tr:eq("+i+")").children("td").find("input[id='goodsCarId']").val();
			
			var map = {}; 
			map["changeUrl"] = changeUrl;
			map["enName"] = enName;
			map["price"] = price;
			map["goodsType"] = goodsType;
			map["quantity"] = quantity;
			map["goodsCarId"] = goodsCarId;
			list[i-1] = map;
			
/*  			$.ajax({
				type:'POST',
				async:false,
				url:'/cbtconsole/customerServlet?action=saveGoodsData&className=OrdersPurchaseServlet',
				data:{changeUrl:changeUrl,enName:enName,price:price,goodsType:goodsType,quantity:quantity,goodsCarId:goodsCarId},
				success:function(){	
				
				}
			});  */
		}
		
		var map2={};  //主
		map2['list']=list;  
		$.ajax({
			type:'POST',
			async:false,
			url:'/cbtconsole/customerServlet?action=saveGoodsData&className=OrdersPurchaseServlet',
			data:{"data":JSON.stringify(map2)},
			success:function(){	
				var orderNo= $("#table tr:eq(1)").children("td").find("input[id='orderNo']").val();
				window.open("/cbtconsole/customerServlet?action=nextChangeGoodsInfo&className=OrdersPurchaseServlet&orderNo="+orderNo); 
			}
		}); 
		
 		
	}
</script>
</head>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
	<div>
	<div align="center" style="font-size: 20px;font-weight: bold;" >1-1</div>
		<table id="table" align="center" border="1px" style="font-size: 13px;" width="1200" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>原产品图片</th>
				<th>替代产品图片</th>
				<th>替代产品链接</th>
				<th>替代产品的原名</th>
				<th>产品英文名</th>
				<th>产品价格RMB</th>
				<th>产品规格</th>
			</Tr>
			<c:forEach items="${cgbList }" var="gbb" varStatus="i">
			<Tr id="tr${i.index}">
				<td><img  width='100px' title="" height='100px;' src="${gbb.aliimg }"></td>
				<td><img  width='100px' title="" height='100px;' src="${gbb.chagoodimg }"></td>
				<td><a target='_blank' href="${gbb.chagoodurl }" >${gbb.chagoodurl }</a></td>
				<td>${gbb.chagoodname }</td>
				<%-- <td><textarea  id="aliname"  maxlength="255">${gbb.aliname }</textarea></td> --%>
				<td><textarea  id="aliname"  maxlength="255">Substitution</textarea></td>
				<td><p>原产品价格：${gbb.aliprice}</p><p>替代产品价格：${gbb.chagoodprice}</p><input type="text" id="chagoodprice" value="${gbb.maxprice }" maxlength="10"/></td>
				<td><textarea id="goodsType" maxlength="255">${gbb.goodsType }</textarea>
				<input type="hidden" id="quantity" value="${gbb.quantity }"/>
				<input type="hidden" id="orderNo" value="${gbb.orderno }"/>
				<input type="hidden" id="goodsCarId" value="${gbb.goodsCarId }"/>
				
				</td>
				
			</Tr>
			</c:forEach>
		</table>
		<div style="width: 180px; margin: auto; position: relative;" >
			<button onclick="saveInfo()"  style="">生成替代产品</button><p style="color: red; position: absolute; left: 110px; width: 600px; font-size: 12px; top: -5px;">(按钮按下，可能会花一点时间，请不要重复点击。)</p>
		</div>
	</div>
</body>
</html>