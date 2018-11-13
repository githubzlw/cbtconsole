<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
		<c:if test="${empty uiblist}">
			<div>
				<font style="font-size: 24px">没有相应数据！</font>
			</div>
		</c:if>
		<c:if test="${not empty uiblist}">
		<c:forEach items="${uiblist}" var="uib">
		<div style="background: #FFCC99; color: #000">
			<span class="ee">客户ID：${uib.id}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<input type="hidden" id="chk" name="kehuID" checked="checked" value="${uib.id}<br/>客户Email：${uib.email}">
			<span class="ee">客户Email：${uib.email}</span>
		</div>
		<c:if test="${empty uib.purchaseBean}">
			<div>
				<font style="font-size: 24px">没有相应数据！</font>
			</div>
		</c:if>
		<c:if test="${not empty uib.purchaseBean}">
		<c:forEach items="${uib.purchaseBean}" var="pb">
		<div id="iidd_${pb.orderNo}" style="display: none">
		<input type="checkbox" id="iid_${pb.orderNo}" value="${pb.orderNo}" style="display: none;">
			<div>
				<div>
					<input type="hidden" id="hidorder" value="${pb.orderNo}">
<%-- 					<span class="cc"><input type="checkbox" id="chk" name="str" onclick="unSelectAll()" value="${pb.orderNo}"/>出货</span> --%>
					<span class="d">订单号：</span><span class="c">${pb.orderNo}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<span class="d">交期：</span><span class="c">${pb.deliveryTime}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<span class="d">付款日期：</span><span class="c">${pb.paytime}</span><br/>
					<span class="d">订单地址：</span><span class="cc">${pb.orderaddress}</span>
					<input type="hidden" id="${pb.orderNo}" value="${pb.orderaddress}">
				</div>
				<table id="table" border="1" style="align:center; font-size: 13px; width:100%; bordercolor:gray;">
					<tr>
						<td width="15%">商品编号/订单信息</td>
						<td width="25%">商品信息</td>
						<td width="35%">名称</td>
						<td width="25%">客户信息</td>
					</tr>
					<c:if test="${not empty pb.purchaseDetailsBean}">
					<c:forEach items="${pb.purchaseDetailsBean}" var="pbpdb">
					<tr>
						<td>
							<div>
								<span>商品号：</span>
								<span>${pbpdb.goodsid}</span><br>
								<span>编码：</span>
								<span>${pbpdb.goodsdata_id}</span>
							</div>
						</td>
						<td>
							<div><a href="${pbpdb.goods_url}" target="_block">
								<img src="${pbpdb.googs_img}" width="50px" height="50px" /></a></div>
							<div><table><tr><td>Type：</td><td>${pbpdb.goods_type}</td></tr></table></div>
						</td>
						<td>${pbpdb.goods_title }</td>
						<td>
							<div>
								<span>Price：</span>
								<span>${pbpdb.goods_price}&nbsp;${pbpdb.currency}/piece</span>
							</div>
							<div>
								<span>Quantity：</span>
								<span>${pbpdb.googs_number}</span>
							</div>
							<div>
								<span>Remark：</span>
								<span>${pbpdb.remark}</span>
							</div>
						</td>
					</tr>
					</c:forEach>
					</c:if>
				</table>
				<div>&nbsp;</div>
			</div>
		</div>
		</c:forEach>
		</c:if>
		</c:forEach>
		</c:if>