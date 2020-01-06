<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>


<title>海外仓库存明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">

<style type="text/css">
/* 主页 */
.report .form-control {display: inline-block;width: 70%;}
.report select.form-control {width: 106%;}
.report {font-size: 16px;color: #333;}
.report label {margin-right: 10px;}
.report .mt20 {margin-top: 20px; margin-right: 18px;margin-left: 18px;}
.report .row2 button {margin-right: 10px;}
.report b {font-size: 18px;}
.report .w200 {width: 200px;}
.mt5 {margin-top: 5px;}
.report .btn_page_in{width:100px;}
.datagrid-goodsName{width: 200px;}
.datagrid-change-type{width: 150px;}
.datagrid-remark{width: 350px;}
</style>
</head>
<body>
<a href="/cbtconsole/owstock/list" target="_blank">&lt;返回</a>
	<div class="container-fluid report">
		<h1 class="text-center">海外仓库存明细</h1>
		<div class="row mt20">
			<div class="col-xs-12">
			<input type="hidden" class="form-control" id="query_owsid" value="${stockParamter.owsid }">
				<!-- <label>产品名称：<input type="text" class="form-control" id="query_goods_name"></label> -->
				<label>产品编码：<input type="text" class="form-control" id="query_goods_code" value="${stockParamter.code }"></label>
				<label>产品ID：<input type="text" class="form-control" id="query_goods_pid" value="${stockParamter.goodsPid }"></label>
				<label>SKUID：<input type="text" class="form-control" id="query_goods_skuid" value="${stockParamter.skuid }"></label>
				<label>ODID：<input type="text" class="form-control" id="query_goods_odid" value="${stockParamter.odid }"></label>
				<label>
				<select id="query_goods_type" class="form-control">
				<option value="-1" >全部</option>
				<option value="0" ${stockParamter.changeType==0 ? 'selected="selected"':''}>占用</option>
				<option value="1" ${stockParamter.changeType==1 ? 'selected="selected"':''}>释放</option>
				</select>
				
				</label>
				<label><button class="btn btn-info query_button"  id="query_button">查询</button></label>
			</div>
		</div>
		<div class="row mt20">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>产品ID</th>
						<th>产品名称</th>
						<th>产品SKU</th>
						<th>商品编码</th>
						<th>订单号</th>
						<th>变更数量</th>
						<th>占用/释放</th>
						<th>时间</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${stockLogList }" var="stock" varStatus="index">
					<tr id="datagrid-row-r2-2-${index.index}">
						<td class="datagrid-goodsPid">${stock.goodsPid}</td>
						<td class="datagrid-goodsName">${stock.goodsName}</td>
						<td align="left" class="datagrid-sku">
						<c:if test="${stock.sku !='null'}">${stock.sku}</c:if>
						<br>${stock.skuid}<br>${stock.specid}
						</td>
						<td class="datagrid-code">${stock.code}</td>
						<td class="datagrid-order"><a href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo=${stock.orderno}" target="_blank" style="color: #c7211d;">${stock.orderno} </a> / ${stock.odid}</td>
						<td class="datagrid-change-stock">${stock.changeStock}</td>
						<td class="datagrid-change-type">
						<span style="color: #c7211d;">${stock.changeType==0?'占用':'释放'}</span>
						<c:if test="${stock.changeType==0 }">
						<br>
						<c:if test="${stock.occupy==1 }">(库存充足)</c:if>
						<c:if test="${stock.occupy==2 }">(库存部分不足)</c:if>
						<c:if test="${stock.occupy==3 }">(库存严重不足)</c:if>
						
						</c:if>
						</td>
						<td class="datagrid-create-time">${stock.createTime}</td>
						<td class="datagrid-remark">
						<c:if test="${stock.stockRemark!='null'}">${stock.stockRemark }</c:if>
						<br>${stock.remark}</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
				<div>
				<span>当前页 :${currentpage} / ${stockLogListPage},总共 ${stockLogListCount }条数据,跳转</span>
				<input type="text" class="form-control btn_page_in" id="current_page" value="${currentpage}">
				<button class="btn btn-success btn_page_qu">查询</button>
				<button class="btn btn-success btn_page_up">上一页</button>
				<button class="btn btn-success btn_page_down">下一页</button>
				
				<input type="hidden" value="${stockLogListPage}" id="total_page">
				</div>
		</div>
		<br>
		<br>
		<br>
	</div>
</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/ows_stock_log.js"></script>
</html>














