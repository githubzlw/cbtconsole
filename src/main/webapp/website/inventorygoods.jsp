<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>


<title>库存移库处理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">

<style type="text/css">
/* 主页 */
.report .form-control {
	display: inline-block;
	width: 70%;
}

.report select.form-control {
	width: 48%;
}

.report {
	font-size: 16px;
	color: #333;
}

.report label {
	margin-right: 10px;
}

.report .mt20 {
	margin-top: 20px;
}

.report .row2 button {
	margin-right: 10px;
}

.report b {
	font-size: 18px;
}

.report .w200 {
	width: 200px;
}

.report th, .report td {
	text-align: center;
}

.mt5 {
	margin-top: 5px;
}

.tc, .trnasparent {
	width: 100%;
	height: 100%;
	background-color: rgba(0, 0, 0, .5);
	position: fixed;
	z-index: 1;
	display: none;
	text-align: center;
}

.tc {
	background-color: rgba(0, 0, 0, 0);
	top: 0;
	left: 0;
}
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
.tc1,.tc2,.tc3{position:absolute;z-index:10;display:none;top:100px;left:50%;margin-left:-600px;background-color:#fff;
padding: 20px;max-height: 800px;}
.tc1_table,.tc2_table{max-height:500px;height:auto;overflow-y:auto;}
/* 产品弹窗 tc2 */
.tc2,.tc2 *{font-size:16px;}
.tc2 th{font-weight: normal;}
.tc2 th,.tc2 td{text-align: center;}
.tc2 label{font-weight: normal;}
.tc2 input[type="file"]{position: absolute;top:0;left:15px;width: 124px;height:34px;opacity: 0;}
.tc2 .gain{color:#4395ff;cursor:pointer;}
.tc2 table  input[type="checkbox"]{width:20px;height:20px;}
.tc2 .remark{margin-top:20px;}
.tc2 .remark label{margin-right:32px;}
.tc2 button{width: 300px;}
.transparent,.transparent-bg{width:100%;height:100%;background-color:rgba(0,0,0,0);position: fixed;z-index:1;display: none;text-align: center;}
.tc2 .transparent-bg{z-index:2;background-color:rgba(0,0,0,.5);}
     .tc2 .transparent img{display: inline-block;z-index:3;position: relative;top:20px;}
em,i{font-style: normal;}
.tc2 .container{margin-top:50px;}
.tc2 img{max-width: 200px;max-height: 200px;}
.tc2 td input[type="text"]{max-width:200px;}
.tc2 .lu_tb_name{max-width: 300px;} 
/* 产品弹窗 tc3 */
*{margin:0;padding:0;box-sizing: border-box;}
.tc3 em,.tc3 i{font-style: normal;display: inline-block;float:left;}
.clearfix:before,.clearfix:after{content:"";display:table;}
.clearfix:after{clear:both;}
.clearfix{zoom:1;} 
.tc3.wraps{border:2px solid #999;width:1049px;padding:20px;margin-left:-400px;}
.tc3.wraps span{display:inline-block;width:103px;float:left;}
.tc3.wraps input[type="text"]{border:1px solid #999;background-color: #fff;height:28px;border-radius: 4px;width:205px;float:left;}
.tc3.wraps input[type="radio"]{width:18px;height:18px;position: relative;top:2px;}
.tc3.wraps label{margin-right:10px;}
.tc3 .wrap6{overflow:hidden;}
.tc3 .wrap6 span,.wraps.tc3 .reasons{float:left;position: relative;}
.tc3 .w235{width:235px;}
.tc3.wraps .wrap{margin-bottom:40px;overflow:hidden;}
.tc3 .wrap7 img{width:250px;height: 250px;}
.tc3 .wrap2 em{width:645px;}
.tc3 .wrap7 {float:right;position: relative;top:-30px;}
.tc3 .left{float:left;}
.tc3 p{text-align: center;}
.tc3 .other{position: absolute;top:15px;right:-222px;}
.tc3 .wrap8{text-align: center;}
.tc3 .wrap8 button{border:1px solid #999;background-color:#fff;padding:0 80px; line-height:28px;border-radius: 4px;}

.c_img{width: 100px;height: 100px;}
.report .btn_page_in{width:100px;}
.query_state{width: 500px;}
</style>
</head>
<body>
	<div class="container-fluid report">
		<h1 class="text-center">库位移库</h1>
		<div class="row">
			<div class="col-xs-1">
				<b>产品检索</b>
			</div>
			<div class="col-xs-11">
				<label>订单号：<input type="text" class="form-control" id="query_orderid" value="${queryParam.orderid}"></label>
				<label>状态：
				<input type="hidden" id="i_state" value="${ queryParam.state}">
				<select class="form-control query_state" id="query_state" >
						<option value="-1">全部</option>
						<option value="0">待移出库存</option>
						<option value="2">待移入库存</option>
						
				</select>
				
				</label>
				<button class="btn btn-info"  id="query_button">查询</button>
			</div>
		</div>
		<div class="row mt20">
			<table class="table table-bordered"  >
				<thead>
					<tr>
						<th>时间</th>
						<th>产品信息</th>
						<th>产品图片</th>
						<th>产品Sku</th>
						<th>订单信息</th>
						<th>库位</th>
						<th>状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody  align="left">
				<c:forEach items="${barcodeList }" var="b" varStatus="index">
					<tr id="datagrid-row-r2-2-${index.index}"  align="left">
					<td style="width: 200px;">${b.createtime }</td>
					<td style="width: 300px;">
					产品ID:
					<br>
					${b. goodsPid}(订单)
					<br>
					${b. iskSGoodsPid}(库存)
					<br>
					产品名称:${b.iskGoodsName }
					</td>
					<td style="width: 200px;">
					订单:<img alt="" src="${b.odCarImg }" class="c_img"><br>
					库存:<img alt="" src="${b.iskSCarImg }" class="c_img">
					</td>
					<td>订单:
					<br>
					Sku:${b.odSku }
					<br>
					Specid:${b.odSpecid }
					<br>
					Skuid:${b.odSkuid }
					<br>
					<br>
					<br>
					库存:
					<br>
					Sku:${b.iskSku }
					<br>
					Specid:${b.iskSpecid }
					<br>
					Skuid:${b.iskSkuid }
					
					</td>
					<td>
					订单号:${b.orderid} / ${b.odid }
					
					</td>
					<td style="width: 400px;">
					分配的订单库位:
					<input value="${b.orderBarcode }" type="text">
					<br>
					库存库位:${b.inBarcode }
					</td>
					<td>
					<c:if test="${b.ibState== 0}">采购使用库存,等待仓库移出库存</c:if>
					<c:if test="${b.ibState== 1}">已完成移出库存</c:if>
					<c:if test="${b.ibState== 2}">订单取消，等待仓库移入库存</c:if>
					<c:if test="${b.ibState== 3}">已完成移入库存</c:if>
					</td>
					<td>
					<c:if test="${b.ibState== 0}"><button class="btn btn-default">确定移出库存</button></c:if>
					<c:if test="${b.ibState== 2}"><button class="btn btn-default">确定移入库存</button></c:if>
					</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
				<div>
				<span>当前页 :${currentPage } / ${barcodeListPage},总共 ${barcodeCount }条数据,跳转</span>
				<input type="text" class="form-control btn_page_in" id="current_page" value="${queryParam.currentPage}"><button class="btn btn-success btn_page_qu" onclick="doQuery()">查询</button>
				</div>
		</div>
		
	</div>
<div class="tc">
	<div class="trnasparent"></div>
	
	
</div>

</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript">
$(function(){
	var state = $("#i_state").val();
	$("#query_state").val(state);//设置value为xx的option选项为默认选中
	$("#query_button").click(function(){
		$("#current_page").val(1);
		doQuery();
	})
})

function doQuery(){
	var page = $("#current_page").val();
	var state = $('#query_state').val();
	var orderid = $('#query_orderid').val();
	window.open("/cbtconsole/inventory/barcode?page="+page+"&orderid="+orderid+"&state="+state, "_self");
}
</script>
</html>














