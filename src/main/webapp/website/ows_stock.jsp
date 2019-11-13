<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>


<title>海外仓库存清单</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">

<style type="text/css">
/* 主页 */
.report .form-control {display: inline-block;width: 70%;}
.report {font-size: 16px;color: #333;}
.report label {margin-right: 10px;}
.report .mt20 {margin-top: 20px;margin-right: 18px;margin-left: 18px;}
.report .row2 button {margin-right: 10px;}
.report b {font-size: 18px;}
.btn-detail{width: 82px;margin-bottom: 5px;}
.report .btn_page_in{width:100px;}
</style>
</head>
<body>
	<div class="container-fluid report">
		<h1 class="text-center">海外仓库存清单</h1>
		<div class="row mt20">
			<div class="col-xs-1">
				<b>产品检索</b>
			</div>
			<div class="col-xs-11">
				<!-- <label>产品名称：<input type="text" class="form-control" id="query_goods_name"></label> -->
				<label>产品ID：<input type="text" class="form-control" id="query_goods_pid" value="${stockParamter.goodsPid }"></label>
				<label>SKUID：<input type="text" class="form-control" id="query_goods_skuid" value="${stockParamter.skuid }"></label>
				<label><button class="btn btn-info query_button"  id="query_button">查询</button></label>
				<label><button class="btn btn-warning btn-check-stock">核对库存</button></label>
			</div>
		</div>
		<div class="row mt20 row2">
			<div class="col-xs-12">
				<label><b>最新同步时间：</b><span id="sync_time">${lastSyncStock}</span></label>
			</div>
		</div>
		<div class="row mt20">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>产品编码</th>
						<th>产品ID</th>
						<th>产品名称</th>
						<th>产品SKU</th>
						<th>PC</th>
						<th>海外仓库存数量</th>
						<th>可用库存</th>
						<th>备注</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${stockList }" var="stock" varStatus="index">
					<tr id="datagrid-row-r2-2-${index.index}">
						<td class="datagrid-code">${stock.code}</td>
						<td class="datagrid-goodsPid">${stock.goodsPid}</td>
						<td class="datagrid-goodsName">${stock.goodsName}</td>
						<td align="left" class="datagrid-sku">
						${stock.sku}<br>${stock.skuid}<br>${stock.specid}
						</td>
						<td class="datagrid-pc"></td>
						<td class="datagrid-ow-stock">${stock.owStock}</td>
						<td class="datagrid-available-stock">${stock.availableStock}</td>
						<td class="datagrid-remark">${stock.remark}</td>
						<td>
						<button class="btn btn-warning btn-detail" name="${stock.id}">明细</button>
						<br>
						<button class="btn btn-info btn-al" name="${stock.id}">提醒补货</button>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
				<div>
				<span>当前页 :${currentpage } / ${stockListPage},总共 ${stockListCount }条数据,跳转</span>
				<input type="text" class="form-control btn_page_in" id="current_page" value="${currentpage}">
				<button class="btn btn-info btn_page_qu">查询</button>
				<button class="btn btn-info btn_page_up">上一页</button>
				<button class="btn btn-info btn_page_down">下一页</button>
				
				<input type="hidden" value="${stockListPage}" id="total_page">
				</div>
		</div>
		<br>
		<br>
		<br>
	</div>
</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/ows_stock.js"></script>
</html>














