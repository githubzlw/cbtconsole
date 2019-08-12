<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>
<title>盘点历史记录</title>
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


.report .btn_page_in{width:100px;}
.w350{width: 350px;}
.datagrid-cell-c2-goodsName{width:200px;}
</style>
</head>
<body>
	<div class="container-fluid report">
		<h1 class="text-center">盘点记录</h1>
			<input type="hidden" value="${inid}" id="query_in_id">
			
		<div class="row">
			<div class="col-xs-11">
				<label>产品ID：<input type="text" class="form-control p_q_r" id="query_goods_pid" value="${queryParam.goods_pid }"></label>
				<button class="btn btn-info bt_ready"  id="query_button">查询</button>
			</div>
		</div>

		<div class="row mt20">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>产品ID</th>
						<th>产品SKU</th>
						<th>产品图片</th>
						<th>产品价格</th>
						<th>盘点前库存</th>
						<th>盘点后库存</th>
						<th>盘点前库位</th>
						<th>盘点后库位</th>
						<th>盘点时间</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${icrHistory}" var="tory" varStatus="index">
					<tr id="datagrid-row-r2-2-${index.index}">
					 
						<td class="datagrid-goodsPid">${tory.goodsPid}</td>
						<td><em class="emsku">${tory.goodsSku}</em>
						<br>
						<em>Skuid:</em><em class="emskuid">${tory.goodsSkuid}</em>
						<br>
						<em>Specid:</em><em class="emspecid">${tory.goodsSpecid}</em></td>
						
						<td class="datagrid-carImg">
						<img alt="" src="${tory.goodsImg }">
						</td>
						<td class="emprice">${tory.goodsPrice}</td>
						<td class="datagrid-last-remaining">${tory.inventoryRemaining}</td>
						<td class="datagrid-check-remaining">${tory.checkRemaining}</td>
						<td class="datagrid-bbarcode">
						${tory.beforeBarcode }
						</td>
						<td class="datagrid-abarcode">
							${tory.afterBarcode}
						</td>
						<td>
							${tory.createTime}
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
				<div>
				<span>当前页 :${currentPage} / ${totalPage},总共 ${totalCount }条数据,跳转</span>
				<input type="text" class="form-control btn_page_in" id="current_page" value="${currentPage}"><button class="btn btn-success btn_page_qu" onclick="doQuery()">查询</button>
				</div>
		</div>
		
	</div>
</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript">
$(function(){
	$("#query_button").click(function(){
		doQuery(1);
	})
})
function doQuery(isQuery){
	var page = $("#current_page").val();
	var goods_pid = $("#query_goods_pid").val();
	var href = "/cbtconsole/inventory/check/info?page="+page
	if(isQuery !='1'){
		var query_in_id = $("#query_in_id").val();
		href = href+"&inid="+query_in_id
	}
		href = href+"&goods_pid="+goods_pid;
	window.open(href, "_self");
}


</script>
</html>














