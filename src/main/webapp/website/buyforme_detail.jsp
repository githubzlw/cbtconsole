<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>


<title>Buy for me</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">

<style type="text/css">
/* 主页 */
.b-add{width: 141px;height: 100px; background: #d9edf7; border-radius: 54px;}
.report .form-control {display: inline-block;width: 70%;}
.report .form-control-i {display: inline-block;width: 30%;}
.report .form-control-i200 {display: inline-block;width: 50%;}
.report select.form-control {width: 48%;}
.report {font-size: 16px;color: #333;width: 90%;}
.report label {margin-right: 10px;}
.report .mt20 {margin-top: 20px;}
.report .row2 button {margin-right: 10px;}
.report b {font-size: 18px;}
.report .w200 {width: 200px;}
.report .w350 {width: 350px;}
.report .w160 {width: 160px;}
.report .w800 {width: 800px;}
.report .w90 {width: 99%;margin-top: 50px;border: 1px solid #ddd;height: 120px;}
.mt5 {margin-top: 5px;}
.tc, .trnasparent {width: 100%;height: 100%;background-color: rgba(0, 0, 0, .5);position: fixed;z-index: 1;display: none;text-align: center;}
.tc {background-color: rgba(0, 0, 0, 0);top: 0;left: 0;}
/* 产品弹窗 tc1 */
.tc,.tc *{font-size:16px;}
.tc1 .form-horizontal .control-label{text-align: left;}
.tc1 th{font-weight: normal;}
.tc1 th,.tc1 td{text-align: center;}
.tc1 label{font-weight: normal;}
.tc1 .remark label{margin-right:32px;}
.tc1 button{width: 300px;}
.tc1 .container{margin-top:50px;}
.tc1 table td:last-child{text-align: center;}
.tc1 table input[type="radio"]{width:20px;height:20px;}
.tc1,.tc2,.tc3,.tc4{position:absolute;z-index:10;display:none;top:100px;left:50%;margin-left:-600px;background-color:#fff;
padding: 20px;max-height: 800px;}
.tc1_table,.tc2_table{max-height:500px;height:auto;overflow-y:auto;}
.img-responsive{max-width: 137px;max-height: 135px;margin-top: 11px;}
.w350{width: 350px;}
.btn-re-n{margin-bottom: 3px;    margin-left: 17px;}
.remark-dn{width: 350px;height:105px;margin-top: 5px;}
.h50{height: 50px;}
.input-w1{width: 50px;    border: 1px solid #ccc;
    border-radius: 4px;}
.input-w4{width: 100px;    border: 1px solid #ccc;
    border-radius: 4px;}
.w99{width: 99%;margin-top: 50px;border: 1px solid #ddd;min-height: 150px;}
.wt35{    margin-left: 2px; height: 35px;margin-top: 10px;}
.sku-td{margin-top: 5px;}
.sku-u-td{margin-top: 5px;}
.table-sku{width:100%;}
.rowweight{    margin-top: 11px;
    margin-left: -2px;}
    .w120{width:115px;}
.input-w5{width: 250px;border: 1px solid #ccc; border-radius: 4px;}
.input-w8{width: 198px;border: 1px solid #ccc; border-radius: 4px;}
.input-w6{width: 50px;border: 1px solid #ccc; border-radius: 4px;}
.w89{width: 89%;}
.w11{width: 11%;}
</style>
</head>
<body>
	<div class="container-fluid report">
		<h1 class="text-center">采购申请单详情</h1>
			<input type="hidden" value="${param.bfid }" id="query_bf_id">
			
	<div  class="row w800">
			<div class="row h50">
			<div class="col-xs-4">采购单号:<span>${order.order_no}</span></div>
			<div class="col-xs-4">${order.stateContent }</div>
			<div class="col-xs-4">关联订单号:<span></span></div>
			</div>
			<div class="row h50">
			<div class="col-xs-4">用户名:<span>${order.name }</span>(<span>${order.user_id }</span>)</div>
			<div class="col-xs-4">邮箱地址:<span>${order.email }</span></div>
			<div class="col-xs-4">下单时间:<span>${order.create_time}</span></div>
			</div>
			<!-- <div class="row">
			<div class="col-xs-4"></div>
			<div class="col-xs-4"></div>
			<div class="col-xs-4"></div>
			</div> -->
	
	</div>		
	<div class="row w90">
	<button class="btn btn-info btn-re-n">添加备注内容(对内)</button> <span class="remark-title">备注内容:</span><input type="text" class="remark-dn">
	</div>
			
		
		<div class="row mt20">
		<c:forEach items="${orderDetails }" var="detail" varStatus="index">
		<div class="row w99">
		<div class="col-xs-1">
		<img src="/cbtconsole/img/beforeLoad.gif" data-original="${detail.picUrl }" class="img-responsive img-lazy"><br>
		<a href="${detail.detailUrl }">商品原始链接</a>
		</div>
		<div class="col-xs-10">
		<div class="row wt35"><span>商品名称:</span>${detail.title }<span>商品ID:</span><span class="td-numiid">${detail.numIid }</span></div>
		<div class="row">
				<div class="col-xs-11 w89">
				<table class="table-sku">
				<thead>
					<tr>
						<th width="147px;">下单规格:</th>
						<th width="160px;">货源价格:</th>
						<th width="210px;">售卖价格(免邮价):</th>
						<th width="70px;">商品数量:</th>
						<!-- <th>时间</th> -->
						<th width="250px;">货源:</th>
					</tr>
				</thead>
				<tbody>
					<tbody id="lu_tr">
					 <c:forEach items="${detail.skus }" var="sku">
					 <tr class="sku-u-td">
						<td>
						<input type="hidden" class="lu_id" value="${sku.id}">
						<input type="text" class="input-w8 lu_sku" value="${sku.sku}"></td>
						<td>USD:${sku.priceBuy }<span></span>(CNY:<input type="text" value="${sku.priceBuyc }" class="lu-price-buy-c input-w1">)</td>
						<td>USD:<input type="text" value="${sku.price }" class="lu-price-sale input-w1">(含运费<input type="text" value="${sku.shipFeight }" class="lu-ship-feight input-w1">)</td>
						<td><input type="text" class="input-w6 lu_count" value="${sku.num}"></td>
						<td><input type="text" class="input-w5 lu_url" value="${sku.url}">
						<button class="btn btn-success btn-update">修改</button>
						<!-- <button class="btn btn-success btn-delete">删除</button> --></td>
						</tr>
					 </c:forEach>
						<tr class="sku-td">
							<td><input type="text" class="input-w8 lu_sku" value=""></td>
							<td>USD:<input type="text" value="" class="lu-price-buy input-w1" disabled="disabled">(CNY:<input type="text" value="" class="lu-price-buy-c input-w1">)</td>
						<td>USD:<input type="text" value="" class="lu-price-sale input-w1">(含运费<input type="text" value="" class="lu-ship-feight input-w1">)</td>
							<td><input type="text" class="input-w6 lu_count" value="0"></td>
							<td><input type="text" class="input-w5 lu_url">
							<button class="btn btn-success btn-add">录入</button><i class="b-add">+</i></td>
						</tr>
					</tbody>
				
				</table>
				</div>
				<div class="col-xs-1 w11">
				<div class="row">用户备注:</div>
				<div class="row">${detail.remark}</div>
				</div>
		</div>
		<div class="row rowweight">重量:<input type="text" value="" class="lu-weight input-w3">KG<button class="btn btn-info btn-weight">修改</button></div>
		
		</div>
		
		</div>
		
		</c:forEach>
		<br>
		<br>
		<br>
			
		<button class="btn btn-success btn-finsh">确认处理</button>		
		</div>
		
	</div>
<!-- <div class="tc">
	<div class="trnasparent"></div>
	<div class="container tc1">
	<input type="hidden"  id="lu_bfid" value="">
	<input type="hidden"  id="lu_bfdid" value="">
		<div class="wrap row">
			<div class="col-xs-9">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-xs-2 control-label text-left">产品ID:</label>
						<div class="col-xs-10">
							<input type="text" class="form-control" id="lu_pid" value="" disabled="disabled">
						</div>
					</div>
					<div class="form-group">
						<label class="col-xs-2 control-label text-left">产品名称:</label>
						<div class="col-xs-10">
							<span id="lu_name">产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称</span>
						</div>
					</div>
				</div>
				<div class="tc1_table">
				<table class="table table-bordered table-primary">
					<thead>
						<tr>
							<th>skuid</th><th>录入数量</th><th>价格</th><th>产品链接</th>
						</tr>
					</thead>
					<tbody id="lu_tr">
						<tr>
							<td><input type="text" class="form-control lu_skuid" value=""></td>
							<td><input type="text" class="form-control lu_count" value="0"></td>
							<td><input type="text" class="lu_price"></td>
							<td><input type="text" class="lu_url"></td>
						</tr>
					</tbody>
				</table>
						<i class="b-add">+</i>
				</div>
			</div>
			
		</div>
		
	</div>
	
	

</div>
 --></body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/msg-confirm.js"></script>
<script type="text/javascript" src="/cbtconsole/js/buyforme_details.js"></script>
<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
</html>














