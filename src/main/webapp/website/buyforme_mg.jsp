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
.report .form-control {display: inline-block;width: 70%;}
.report .form-control-i {display: inline-block;width: 30%;}
.report .form-control-i200 {display: inline-block;width:68%;}
.report select.form-control {width: 48%;}
.report {font-size: 16px;color: #333;width: 90%;background-color: #dff0d8;min-height:936px;}
.report label {margin-right: 10px;}
.report .mt20 {margin-top: 20px;}
.report .row2 button {margin-right: 10px;}
.report b {font-size: 18px;}
.report .w200 {width: 200px;}
.report .w350 {width: 350px;}
.report .w160 {width: 160px;}
.mt5 {margin-top: 5px;}
.report .btn_page_in{width:100px;}
.w350{width: 350px;}
</style>
</head>
<body>
	<div class="container-fluid report">
		<h1 class="text-center">Buy For Me 申请单</h1>
			
		<div class="row">
			<div >
				<label>订单号：<input type="text" class="form-control form-control-i200" id="query_oderno" value="${queryParam.orderNo }"></label>
				<label>用户ID：<input type="text" class="form-control p_q_r" id="query_user_id" value="${queryParam.userId }"></label>
				<label>时间: <input id="query_sttime" class="Wdate form-control p_tq_r"
                             style="width: 110px; height: 34px" type="text" value="${queryParam.sttime}"
                             onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
				<span>&nbsp;-&nbsp;</span><input id="query_edtime" class="Wdate form-control p_tq_r"
                                                 style="width: 110px; height: 34px;" type="text" value="${queryParam.edtime}"
                                                 onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
			</label>
				
				<label class="w200">订单状态： <select class="form-control  p_qs_r" id="query_state" >
						<option value="-2" ${queryParam.state==-2 ? 'selected="selected"':''}>全部</option>
						<option value="-1" ${queryParam.state==-1 ? 'selected="selected"':''}>取消</option>
						<option value="0" ${queryParam.state==0 ? 'selected="selected"':''}>申请</option>
						<option value="1" ${queryParam.state==1 ? 'selected="selected"':''}>处理中</option>
						<option value="2" ${queryParam.state==2 ? 'selected="selected"':''}>销售处理完成</option>
						<option value="3" ${queryParam.state==3 ? 'selected="selected"':''}>已支付</option>
				</select>
				</label>
				<button class="btn btn-info bt_ready"  id="query_button_check">查询</button>
				
			</div>
		</div>
		<div class="row mt20">
			<table class="table table-bordered">
				<thead>
					<tr><th>申请ID</th><th>订单号</th><th>用户ID</th><th>状态</th><th>支付金额</th><th>交期时间</th><th>时间</th><th>备注</th></tr>
				</thead>
				<tbody>
				<c:forEach items="${orders }" var="order" varStatus="index">
					<tr>
					 <td >${order.id}</td>
						<td ><a href="/cbtconsole/bf/detail?bfid=${order.id}">${order.orderNo}</a></td>
						<td >${order.userId}</td>
						<td >${order.state}</td>
						<td >${order.payPrice}</td>
						<td >${order.deliveryTime}</td>
						<td >${order.createTime}</td>
						<td >${order.remark}</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
				<div>
				<span>当前页 :${queryParam.current_page } / ${totalPage},总共 ${listCount }条数据,跳转</span>
				<input type="text" class="form-control btn_page_in" id="current_page" value="${queryParam.current_page }">
				<button class="btn btn-success btn_page_qu">查询</button>
				<button class="btn btn-success btn_page_up" >上一页</button>
				<button class="btn btn-success btn_page_down">下一页</button>
				
				<input type="hidden" value="${totalPage}" id="total_page">
				</div>
		</div>
		
	</div>
</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/msg-confirm.js"></script>
<script type="text/javascript" src="/cbtconsole/js/buyforme.js"></script>
<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
</html>














