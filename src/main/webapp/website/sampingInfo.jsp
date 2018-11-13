<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>买样-尚无订单预选工厂性 </title>
<style>
.a1{ color:#00afff;}

</style>
<script type="text/javascript">
function getCodeId(value){
	window.location = "/cbtconsole/customerServlet?action=findSuppliesFactory&className=PictureComparisonServlet&cid="+value;
}
function butSearch(){
	var shopId=$("#shopId").val();
	window.location = "/cbtconsole/customerServlet?action=findSuppliesFactory&className=PictureComparisonServlet&shopId="+shopId;
}

function butUpdate(id){
	alert("请等待大概需要10秒。");
	$.ajax({
		type : "post",
		datatype : "json",
		url : "/cbtconsole/customerServlet?action=validationDateUpdate&className=PictureComparisonServlet",
		data : {id:id},
		success : function(res) {
			alert("更新成功。");
		},
		error : function() {
			//alert("保存失败。");
		}
   	})
}

function shopPolymerization(id){
	alert("请等待大概需要5分钟。");
	$.ajax({
		type : "post",
		datatype : "json",
		url : "/cbtconsole/customerServlet?action=shopPolymerization&className=PictureComparisonServlet",
		data : {id:id},
		success : function(res) {
			alert("更新成功。");
		},
		error : function() {
			//alert("保存失败。");
		}
   	})
}



</script>
</head>
<body style="">
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
<div align="center">
	<table>
		<tr>
			<td>
				店铺：<input type="text" id="shopId" name="shopId" value="${shopId}">
			</td>
			<td>
				<input id="searchId" style="height: 30px;width: 86px;" type="button" value="查询" onclick="butSearch()">
			</td>
			<td>
				<a href="/cbtconsole/supplierscoring/querySupplierScoringList" target="_black"><input  id="updateId" style="cursor:pointer;height: 30px;width: 100px;" type="button" value="供应商评分列表页"></a>
			</td>
		</tr>
	</table>
	
	</div>
	<div>
	
		<table id="table1" align="center" border="1px" style="font-size: 13px;" width="1000" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>工厂名</th>
				<th>我司属性</th>
				<th>是否授权</th>
				<th>1688属性</th>
				<th>上架产品数量</th>
				<th>采过样的数量</th>
			</Tr>
			
			<c:forEach items="${gbbs }" var="gbb" varStatus="i">
				<Tr class="a" id="tr${gbb.shopId}">
					<td>
						<a href="/cbtconsole/customerServlet?action=fingGoodsByShopId&className=PictureComparisonServlet&shop_id=${gbb.shopId}&total=${gbb.pId}" title="进入采购页面" target="_blank">${gbb.shopId}</a>
					</td>
					<td>
						${gbb.shopLevel}
					</td>
					<td>
						${gbb.authorizedFlag}
					</td>
					<td>
						${gbb.shopGuarantee}${gbb.saleService}${gbb.address}
					</td>
					<td>
						${gbb.pId}
					</td>
					<td>
						<a target="_blank" href="/cbtconsole/supplierscoring/supplierproducts?shop_id=${gbb.shopId}" title="查看店铺、商品评分列表">${gbb.samplCount}</a>
					</td>
				</Tr>
			</c:forEach>
			
		</table>
 	 	<div align="center">${pager }</div>
		
	</div>
</div>
</body>
</html>