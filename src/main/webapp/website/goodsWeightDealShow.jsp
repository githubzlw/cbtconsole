<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>重量超差确认处理</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<style type="text/css">
.div_sty {
	width: 370px;
}

.img_sty {
	max-width: 180px;
	max-height: 180px;
}

.check_sty {
	height: 24px;
	float: left;
	width: 24px;
}

.check_sty_l {
	height: 20px;
	width: 20px;
}

.checkBg {
	background-color: #b6f5b6;
}

.inp_sty {
	height: 23px;
	width: 80px;
}

.del_btn {
	float: left;
	border-radius: 10px;
	height: 40px;
	width: 80px;
	margin-left: 40px;
	border-color: #2e6da4;
	color: white;
	background-color: red;
}

.btn_ot_sty {
	float: left;
	border-radius: 10px;
	height: 40px;
	width: 140px;
	margin-left: 40px;
	border-color: #2e6da4;
	color: white;
	background-color: #169bd4;
}

.btn_add_sty {
	border-radius: 10px;
	height: 34px;
	width: 140px;
	margin-left: 40px;
	border-color: #2e6da4;
	color: white;
	background-color: #169bd4;
}

.inp_sty_lt {
	height: 20px;
	width: 45px;
}

.inp_sty_md {
	height: 20px;
	width: 240px;
}
</style>
<script type="text/javascript">

	function saveShowDealShopGoods(shopId) {
		$.ajax({
			type : 'POST',
			dataType : 'json',
			url : '/cbtconsole/ShopUrlC/checkShowDealShopGoodsAndClear',
			data : {
				shopId : shopId
			},
			success : function(data) {
				if (data.ok) {
					$.messager.alert("提醒", "执行成功,数据正在清洗，请关闭当前页面", "info");
				} else {
					$.messager.alert("提醒", data.message, "error");
				}
			},
			error : function(XMLResponse) {
				$.messager.alert("提醒", "保存错误，请联系管理员", "error");
			}
		});
	}

	
	function showMessage(msgStr) {
		$.messager.show({
			title : '提醒',
			msg : msgStr,
			timeout : 1500,
			showType : 'slide',
			style : {
				right : '',
				top : ($(window).height() * 0.35),
				bottom : ''
			}
		});
	}
</script>
</head>
<body>

	<c:if test="${show == 0}">
		<h1 align="center">${msgStr}</h1>
	</c:if>
	<c:if test="${show > 0}">

		<div style="text-align: center;">
			<h1>
				重量超差确认处理(<span style="color: red;">强建议添加重量关键词后再强制改重量</span>)
			</h1>
			<br>
			<div style="float: left;">
				<h2 style="float: left;">
					商品总数：(<span style="color: red;">${errorTotal}</span>)
				</h2>
				<input class="btn_ot_sty" type="button" value="清洗商品数据"
					onclick="saveShowDealShopGoods('${shopId}')">
				<br>
				<table id="shop_goods_error" border="1" cellpadding="1"
					cellspacing="0" align="left">
					<tbody>
						<c:forEach items="${errorList}" var="erCid" varStatus="index">

							<tr align="left" bgcolor="#DAF3F5" style="height: 50px;">
								<td colspan="5"><span>类别：${erCid.categoryId}【${erCid.categoryName}】</span>
									&nbsp;&nbsp;&nbsp;&nbsp;<span>设定平均重量：${erCid.weightVal}<em>KG</em></span></td>
							</tr>

							<c:set var="total" value="${erCid.totalNum}" />
							<c:set var="count" value="0" />
							<c:forEach items="${erCid.gdOfLs}" var="goods" varStatus="status">
								<c:set var="count" value="${count + 1}" />
								<c:if test="${count == 1}">
									<tr align="left">
								</c:if>
								<td>
									<div class="div_sty ct_${erCid.categoryId}">
										<input type="hidden" class="catid_sty"  value="${erCid.categoryId}"/>
										<input type="hidden" class="pid_sty"  value="${goods.pid}"/>
										<div>
											<a target="_blank"
												href="https://detail.1688.com/offer/${goods.pid}.html"><img
												class="img_sty" src="${goods.imgUrl}" /></a> <br>单个价格:<span>${goods.price}</span><span>$</span>
											<br>名称:<span>${goods.goodsName}</span>
										</div>
										<div style="margin-bottom: 2px;">
											<span style="color:${goods.weightFlag == 0 ? 'green':'red'};">重量异常标识:[${goods.weightFlag == 0 ? '正常' : goods.weightFlagDescribe}]</span>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
												style="color:${goods.weightDeal == 1 ? 'green':'red'};">处理标识:[${goods.weightDeal == 1 ? '已处理' : '未处理'}]</span>
											<br>
											<span>现重量：${goods.weight}<em>KG</em></span> 
										</div>
										
										<div style="margin-bottom: 2px;">
											<span>准备改成的重量：<input
												type="text" class="inp_sty" value="${goods.setWeight}" /><em>KG</em></span>
										</div>
									</div>

								</td>
								<c:if test="${count % 5 == 0}">
									</tr>
									<tr align="left">
								</c:if>
							</c:forEach>
							<c:if test="${total % 4 > 0}">
								</tr>
							</c:if>


						</c:forEach>

					</tbody>
				</table>
			</div>
		</div>


	</c:if>


</body>
</html>