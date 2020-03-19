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
.report {font-size: 16px;color: #333;}
.report label {margin-right: 10px;}
.report .mt20 {margin-top: 20px;}
.report .row2 button {margin-right: 10px;}
.report b {font-size: 18px;}
.report .w200 {width: 200px;}
.report .w350 {width: 350px;}
.report .w160 {width: 160px;}
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
.img-responsive{max-width: 137px;max-height: 135px;}
.w350{width: 350px;}
</style>
</head>
<body>
	<div class="container-fluid report">
		<h1 class="text-center"></h1>
		
			<input type="hidden" value="${param.bfid }" id="query_bf_id">
		<div class="row mt20">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>产品名称</th>
						<th>产品ID</th>
						<th>产品图片</th>
						<th>产品价格</th>
						<th>申请数量</th>
						<th>状态</th>
						<th>备注</th>
						<!-- <th>时间</th> -->
						<th></th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${orderDetails }" var="detail" varStatus="index">
					<tr>
					 <td>${detail.title }</td>
					 <td>${detail.numIid }</td>
					 <td><img src="${detail.picUrl }" class="img-responsive"></td>
					 <td>${detail.price }</td>
					 <td>${detail.num }</td>
					 <td>${detail.state }</td>
					 <td>${detail.remark}</td>
					 <%-- <td>${detail.createTime}</td> --%>
					 <td class="s-bf">
					 <input type="hidden" value="${detail.id}" class="bfdid">
					 <table class="table table-bordered table-primary">
					<thead>
						<tr>
							<th>规格</th><th>录入数量</th><th>产品链接</th>
						</tr>
					</thead>
					<tbody id="lu_tr">
					 <c:forEach items="${detail.skus }" var="sku">
					 <tr>
							<td><input type="text" class="form-control lu_skuid" value="${sku.skuid}"></td>
							<td><input type="text" class="form-control lu_count" value="${sku.num}"></td>
							<td><input type="text" class="lu_url" value="${sku.url}"><button class="btn btn-success btn-update">修改</button></td>
						</tr>
					 </c:forEach>
						<tr>
							<td><input type="text" class="form-control lu_skuid" value=""></td>
							<td><input type="text" class="form-control lu_count" value="0"></td>
							<td><input type="text" class="lu_url"><button class="btn btn-success btn-add">录入</button></td>
						</tr>
					</tbody>
				</table>
						<i class="b-add">+</i>
					 
					 </td>
						
					</tr>
					</c:forEach>
				</tbody>
			</table>
				
		</div>
		
	</div>
<div class="tc">
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
</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/msg-confirm.js"></script>
<script type="text/javascript" src="/cbtconsole/js/buyforme_details.js"></script>
<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
</html>














