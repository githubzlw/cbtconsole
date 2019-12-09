<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>


<title>库存损益</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">

<style type="text/css">
/* 主页 */
 .report .form-control {
	display: inline-block;
	/* width: 70%; */
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

.mt5 {
	margin-top: 5px;
}

em,i{font-style: normal;}


.c_img{width: 100px;height: 100px;}
.report .btn_page_in{width:100px;}
.query_state{width: 100px;}
.btn-cancel{margin-top: 10px;}
.datagrid-cell-c2-goodsSku{width: 350px;}
</style>
</head>
<body>
	<div class="container-fluid report">
		<h1 class="text-center">库存损益</h1>
		<div class="row">
			<div class="col-xs-1">
				<b>产品检索</b>
			</div>
			<div class="col-xs-11">
				<%-- <label>订单号：<input type="text" class="form-control" id="query_orderid" value="${queryParam.orderid}"></label> --%>
				<label>时间:<input id="startdate"
						name="startdate" readonly="readonly"
						onfocus="WdatePicker({isShowWeek:true})"
						value="${queryParam.startdate}"/> -
						<input id="enddate" name="enddate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})"
						value="${queryParam.enddate}" style="margin-left:5px;"/>
	           </label>
				
				<label>类型：
				<input type="hidden" id="i_state" value="${ queryParam.type}">
				<select class="form-control query_state" id="query_state" >
				<!-- 0  损坏 1 遗失  3 添加 4 补货  5 漏发 7 其他原因 -->
						<option value="-1">全部</option>
						<option value="0">损坏</option>
						<option value="1">遗失</option>
						<option value="3">添加</option>
						<option value="4">补货</option>
						<option value="5">漏发</option>
						<option value="8">送样</option>
						<option value="7">其他</option>
						
				</select>
				
				</label>
				<button class="btn btn-info"  id="query_button">查询</button>
				<button class="btn btn-info"  id="download_button">下载</button>
			<label><a href="/cbtconsole/inventory/list" target="_blank"><button class="btn btn-success">库存清单</button></a></label>
				<label><a href="/cbtconsole/inventory/check/list" target="_blank"><button class="btn btn-success button_loss"  id="query_button_loss">库存盘点</button></a></label>
			</div>
		</div>
		<div class="row mt20">
			<table class="table table-bordered"  >
				<thead>
					<tr>
						<th>时间</th>
						<th>产品ID</th>
						<th>产品图片</th>
						<th>产品Sku</th>
						<th>变更数量</th>
						<th>类型</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody  align="left">
				<c:forEach items="${lossList }" var="l" varStatus="index">
					<tr id="datagrid-row-r2-2-${index.index}"  align="left">
					<td>${l.changeTime }</td>
					<td>${l.goodsPid }</td>
					<td><img src="/cbtconsole/img/beforeLoad.gif" data-original="${l.img }" style="width: 100px;height: 100px;" class="img-lazy" ></td>
					<td>${l.skuid }<br>${l.specid }</td>
					<td>${l.changeNumber }</td>
					<td>${l.changeContext }</td>
					<td>${l.remark }</td>
					</tr>
					</c:forEach>
					<tr>
					<td colspan="3">总数:</td>
					<td colspan="4">${lossListSum }</td>
					</tr>
				</tbody>
			</table>
				<div>
				<span>当前页 :${queryParam.currentPage } / ${lossListPage},总共 ${lossCount }条数据,跳转</span>
				<input type="text" class="form-control btn_page_in" id="current_page" value="${queryParam.currentPage}">
				<button class="btn btn-success btn_page_qu" onclick="doQuery()">查询</button>
				<button class="btn btn-success btn_page_up" onclick="doBeforePage()">上一页</button>
				<button class="btn btn-success btn_page_down" onclick="doNextPage()">下一页</button>
				
				<input type="hidden" value="${lossListPage}" id="total_page">
				
				</div>
		</div>
		<br>
		<br>
	</div>


</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
<script type="text/javascript">
$(function(){
	$('.img-lazy').lazyload({effect: "fadeIn"});	
	var state = $("#i_state").val();
	$("#query_state").val(state);//设置value为xx的option选项为默认选中
	$("#query_button").click(function(){
		$("#current_page").val(1);
		doQuery();
	})
	//下载
	$("#download_button").click(function(){
		var state = $('#query_state').val();
		var startdate = $('#startdate').val();
		var enddate = $('#enddate').val();
		window.location.href="/cbtconsole/inventory/loss/download?type="+state+"&enddate="+enddate+"&startdate="+startdate;
	})
	
})

function doQuery(){
	var page = $("#current_page").val();
	var state = $('#query_state').val();
	var startdate = $('#startdate').val();
	var enddate = $('#enddate').val();
	window.open("/cbtconsole/inventory/loss/list?page="+page+"&type="+state+"&enddate="+enddate+"&startdate="+startdate, "_self");
}
function doBeforePage(){
	var page = Number($("#current_page").val());
	var tpage = Number($("#total_page").val());
	page = page -1;
	if(page < 1){
		$("#current_page").val(1);
	}else if(page > tpage){
		$("#current_page").val(tpage);
	}else{
		$("#current_page").val(page);
	}
	doQuery();
}
function doNextPage(){
	var page = Number($("#current_page").val());
	var tpage = Number($("#total_page").val());
	page = page+1;
	if(page < 1){
		$("#current_page").val(1);
	}else if(page > tpage){
		$("#current_page").val(tpage);
	}else{
		$("#current_page").val(page);
	}
	doQuery();
}


</script>
</html>














