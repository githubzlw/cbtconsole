<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>


<title>海外仓出库管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">

<style type="text/css">
/* 主页 */
.report .form-control {display: inline-block;width: 70%;}
.report {font-size: 16px;color: #333;}
.report .mt20 {margin-top: 20px;margin-right: 18px;margin-left: 18px;}
.check-in{height:34px;width:405px;}
.row-1{margin-left: 1px;}
.ingo-border{border: 1px solid #ddd; height: 500px;}
.row-info{border: 1px solid #ddd;height: 38px;width: 98%;margin-top: 1px;margin-left: 2px;}
.row-info-a{border: 1px solid #ddd;height: 200px;width: 98%;margin-top: 1px;margin-left: 2px;}
.info-order{margin-left: -3px;margin-top: 5px;}
.row-top-m{margin-top: 8px;margin-left: -15px;}
.row-adress{ margin-top: 10px;margin-left: 2px;}
.row-c{margin-left: 2px;}
.info-address{margin-top: 20px;}
.row-l-30{margin-left: 0px;margin-top: 20px;}
.btn-shipout{width: 300px;}
.btn-shipno{    height: 30px;}
.row-table{margin-top: 10px;}
</style>
</head>
<body>
	<div class="container-fluid report">
		<h1 class="text-center">海外仓出库管理</h1>
		<div class="row mt20">
			<div class="row row-1">
				<b>海外仓订单</b>
			</div>
			<div class="col-xs-3">
			<div class="row">
			<input type="text" class="check-in" value="" placeholder="userid or orderno">
			<button class="btn btn-info btn-check">查询</button>
			</div>
			<div class="row row-table">
			<table class="table table-bordered">
			<thead>
					<tr>
						<th>User ID</th>
						<th>Order No</th>
					</tr>
				</thead>
			<tbody class="clear-all-table">
			<c:forEach items="${ows}" var="stock">
			<tr>
			<td class="datagrid-userid">${stock['user_id']}</td>
			<td class="datagrid-orderno">${stock['order_no']}</td>
			</tr>
			</c:forEach>
			</tbody>
			
			</table>
			</div>
			</div>
			<div class="col-xs-1"></div>
			<div class="col-xs-8 ingo-border">
			<div class="row info-order info-order-1">
				<label>订单详情:</label>
				<div class="row row-info">
					<div class="col-xs-4 row-top-m">
					<label>用户ID:</label>
					<span class="info-order-userid clear-all">${owsDetail['user_id'] }</span>
					</div>
					<div class="col-xs-4 row-top-m">
					<label>订单号码:</label>
					<span class="info-order-orderno clear-all">${owsDetail['order_no'] }</span>
					</div>
					<div class="col-xs-4 row-top-m">
					<label>邮箱地址:</label>
					<span class="info-order-email clear-all">${owsDetail.email }</span>
					</div>
				</div>
			</div>
			<div class="row info-address info-order">
				<label>收货地址:</label>
				<div class="row row-info-a">
					<div class="row row-c">
						<div class="col-xs-2 row-top-m">
						<label>Country:</label>
						<span class="info-address-country clear-all">${owsDetail.Country }</span>
						</div>
						<div class="col-xs-2 row-top-m">
						<label>State:</label>
						<span class="info-address-state clear-all">${owsDetail.statename }</span>
						</div>
						<div class="col-xs-2 row-top-m">
						<label>City:</label>
						<span class="info-address-city clear-all">${owsDetail.address2 }</span>
						</div>
						<div class="col-xs-2 row-top-m">
						<label>ZIPCODE:</label>
						<span class="info-address-code clear-all">${owsDetail.zipcode }</span>
						</div>
						<div class="col-xs-4 row-top-m">
						<label>Phone:</label>
						<span class="info-address-phone clear-all">${owsDetail.phoneNumber }</span>
						</div>
					</div>
					<div class="row row-adress">
					<label>Address</label>
					<span class="info-address-a clear-all">${owsDetail.address }${owsDetail.street }</span>
					</div>
				</div>
				
			</div>
				
			<div class="row row-l-30">
			<label>尾程运单追踪号:</label>
			<input type="text" value="" class="in-shipno" id="in-shipno">
			<button class="btn btn-info btn-shipno">确认</button>
			</div>	
				
			<div class="row row-l-30">
			<div class="col-xs-4"></div>
			<div class="col-xs-4"><button class="btn btn-info btn-shipout">出货</button></div>
			<div class="col-xs-4"></div>
			
			</div>
			</div>
		</div>
		
		
		
		
		
		
		
		
	</div>
</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/ows_shipment.js"></script>
</html>














