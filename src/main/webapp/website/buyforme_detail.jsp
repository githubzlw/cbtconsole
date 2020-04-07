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
.report {font-size: 16px;color: #333;width: 1700px;}
.report label {margin-right: 10px;}
.report .mt20 {margin-top: 20px;}
.report .row2 button {margin-right: 10px;}
.report b {font-size: 18px;}
.report .w200 {width: 200px;}
.report .w350 {width: 350px;}
.report .w160 {width: 160px;}
.report .w800 {width: 1100px;}
.report .w90 {width: 1699px;margin-top: 50px;border: 1px solid #8a6d3b;height: 120px;    margin-left: -30px;}
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
.img-responsive{max-width: 135px;max-height: 135px;margin-top: 11px;}
.w350{width: 350px;}
.btn-re-n{margin-bottom: 3px;margin-left: 17px;}
.remark-dn{width: 360px;height:105px;margin-top: 5px;border: 1px solid #ccc;border-radius: 4px;}
.h50{height: 50px;}
.input-w1{width: 64px;border: 1px solid #ccc;border-radius: 4px;height: 34px;}
.input-w4{width: 100px;border: 1px solid #ccc;border-radius: 4px;height: 34px;}
.w99{width:1700px;margin-top: 15px;border: 1px solid #8a6d3b;min-height: 150px;}
.wt35{ margin-left: 2px; height: 35px;margin-top: 10px;}
.sku-td{margin-top: 5px;}
.sku-u-td{margin-top: 5px;}
.table-sku{width:100%;}
.rowweight{ margin-left: -2px;}
.w120{width:115px;}
.input-w5{width: 378px;border: 1px solid #ccc; border-radius: 4px;height: 34px;}
.input-w3{width: 50px;border: 1px solid #ccc; border-radius: 4px;height: 34px;}
.input-w25{width: 38px;border: 1px solid #ccc; border-radius: 4px;height: 25px;}
.input-w8{width: 254px;border: 1px solid #ccc; border-radius: 4px;height: 34px;}
.input-w6{width: 92%;border: 1px solid #ccc; border-radius: 4px;height: 34px;}
.btn-weight{cursor: pointer;}
.btn-update{cursor: pointer;}
.btn-invalid{cursor: pointer;}
.rb-add{margin-left: 1px;margin-top: 10px;}
.b-add{cursor: pointer;}
.w89{width: 99%;margin-left: 1%;margin-top: 12px;}
.w11{width: 99%;margin-left: 1%;}
.detail-div{width:1520px;}
.td-in-valid{text-decoration:line-through;}
.th-font{font-size: 14px;color: #ca5252;font-weight: initial;}
.remark-row{margin-top: 10px;    width: 85%;}
.td-font-new{font-size: 14px;color: #ca5252;font-weight:bold;margin-left: 5px;}
.ormnum{border: 1px dashed #f90;padding: 2px;margin-left: 5px;font-size: 17px;}
.mar-l{margin-left: 10px;margin-top: 15px;width: 90%;}
.btn-delivery-time{margin-left: 2px;}
.delivery-time{border: 1px solid #ccc;border-radius: 4px;height: 35px;}
.delivery-method{border: 1px solid #ccc;border-radius: 4px;height: 35px;}
.delivery-feight{border: 1px solid #ccc;border-radius: 4px;height: 35px;}
.img-dv{width: 10%;}
.btn-finsh{margin-left: 1445px;}
.remark-replay{border: 1px solid #ccc;border-radius: 4px;height: 34px;width:30%;}
.td-font-view{margin-left: 2%;cursor: pointer;}
.text-al{text-align: left;}
.tc-al-name{font-size: 18px;font-weight: bold;}
.tc1{border: 3px solid #ccc;border-radius: 21px;}
.sy-content{    margin-left: 1%;font-size: 20px;font-weight: bold;color: #ef110c;}
body{min-height:100%;}
.num-span{margin-left: 2%;}
.btn-update-address{display: none;}
.delete-all{font-weight: bold;font-size: 15px;color: #ef0e09;}
#in-state{margin-left: 22px;width:266px;}
#in-city{margin-left: 15px;width: 260px;}
#in-street{width: 260px;}
#in-recipients{width: 256px;}
#in-address{margin-left: 10px;width: 256px;}
#in-country{width: 265px;height: 27px;}
#in-phone{margin-left: 10px;}
.adm-state-content{font-size: 20px;color:#1181e0;font-weight: bold;}
.w22{width: 360px;}
.w10{width:85px;margin-top: -24px;}
.delivery-method{width: 380px;}
.de-w1{width:170px;}
.input-w75{width: 75px;border: 1px solid #ccc; border-radius: 4px;height: 34px;}
</style>
</head>

<body>
<div style="background-color:#eaf5e5;min-height:100%,width:2000px;">
	<div class="container-fluid report">
		<h1 class="text-center">采购申请单详情</h1>
			<input type="hidden" value="${order.bf_id }" id="query_bf_id">
	<div  class="row w800">
			<div class="row h50">
			<div class="col-xs-4"><span class="th-font">采购单号:</span><span class="ormnum">${order.order_no}</span></div>
			<div class="col-xs-4 adm-state-content">${order.stateContent }</div>
			<div class="col-xs-4"><span class="th-font">关联订单号:</span><span></span></div>
			</div>
			<div class="row h50">
			<div class="col-xs-4"><span class="th-font">用户名:</span><span>${order.name }</span>(<span>${order.user_id }</span>)</div>
			<div class="col-xs-4"><span class="th-font">邮箱地址:</span><span>${order.email }</span></div>
			<div class="col-xs-4"><span class="th-font">下单时间:</span><span>${order.create_time}</span></div>
			</div>
			<div class="row"><span class="sy-content"><c:if test="${order.sample_flag ==1 }">送样</c:if></span></div>
	
	</div>
	<div class="row w90">
	<div class="row mar-l">
	<div class="col-xs-2 w22"><span>Country:</span>
	<select id="in-country" disabled="disabled" class="disable-in-l">
	<c:forEach items="${countrys }" var="lst">
	<option value="${lst.id }" ${lst.id== order.countryId?"selected='selected'":""}>${lst.country }</option>
	</c:forEach>
	
	</select>
	
	</div>
	<div class="col-xs-2 w22"><span>City:<input type="text" value="${order.address }" id="in-city" disabled="disabled" class="disable-in-l"></span></div>
	<div class="col-xs-2 w22"><span>Address:<input type="text" value="${order.address2 }" id="in-address" disabled="disabled" class="disable-in-l"></span></div>
	<div class="col-xs-2 w22"><span>Phone:<input type="text" value="${order.phone_number }" id="in-phone" disabled="disabled" class="disable-in-l"></span></div>
	</div>
	<div class="row mar-l">
	<input type="hidden" value="${order.address_id }" id="address_id">
	<div class="col-xs-2 w22"><span>State:<input type="text" value="${order.statename }" id="in-state" disabled="disabled" class="disable-in-l"></span></div>
	<div class="col-xs-2 w22"><span>Street:<input type="text" value="${order.street }" id="in-street" disabled="disabled" class="disable-in-l"></span></div>
	<div class="col-xs-2 w22"><span>recipients:<input type="text" value="${order.recipients }" id="in-recipients" disabled="disabled" class="disable-in-l"></span></div>
	<div class="col-xs-2 w22"><span>ZipCode:<input type="text" value="${order.zip_code }" id="in-code" disabled="disabled" class="disable-in-l"></span></div>
	<div class="col-xs-2 w10"><button class="btn btn-info btn-address">修改地址</button>
	<button class="btn btn-info btn-update-address">更新地址</button></div>
	</div>
	</div>
	
	
	<div class="row w90">
	<button class="btn btn-info btn-re-n">添加备注内容(对内)</button> <span class="remark-title">备注内容:</span><input type="text" class="remark-dn" value="">
	运费:<input type="text" value="${order.ship_feight}" class="delivery-feight" disabled="disabled">
	交期:<input class="delivery-time" value="${order.delivery_time }" disabled="disabled" >
	运输方式:
	<select class="delivery-method">
	
	</select>
	<input type="hidden" value="${order.delivery_method}" id="h-delivery-method">
	<button class="btn btn-info btn-delivery-time">确认交期</button>
	<input type="hidden" value="${order.countryId }" id="in-country-id">
	</div>
		
		<div class="row mt20">
		<c:forEach items="${orderDetails }" var="detail" varStatus="index">
		<div class="row w99 de-td">
		<input value="${detail.count}" class="lucount" type="hidden">
		<input value="${detail.id}" class="bfdid" type="hidden">
		<input value="${detail.price}" class="price-ss" type="hidden">
		<div class="col-xs-1 de-w1">
		<img src="/cbtconsole/img/beforeLoad.gif" data-original="${detail.picUrl }" class="img-responsive img-lazy img-de-v">
		<a href="${detail.detailUrl }" target="_blank">商品原始链接</a>
		<div>
		<c:if test="${detail.state !=-1}">
		<button class="btn btn-info btn-delete-p-all">取消商品</button>
		</c:if>
		<c:if test="${detail.state ==-1}">
		<span class="delete-all">商品已取消</span>
		</c:if>
		</div>
		</div>
		<div class="col-xs-10 detail-div">
		<div class="row wt35">
		<span class="th-font">商品名称:</span><span class="name-title">${detail.title }</span>(<span class="td-numiid">${detail.numIid }</span>)
		<span class="th-font num-span">数量:</span><span>${detail.num}</span>
		</div>
		<div class="row w11">
		<div class="row th-font">用户备注:</div>
		<div class="row remark-row">Q:<span class="th-font-l de-remarl-q">${detail.remark}</span><i class="td-font-new">New!</i> <i class="td-font-view">View</i></div>
		<%-- <div class="row remark-replay-row">A:<button class="btn btn-info btn-replay">回复</button></div> --%>
		<div class="remark-replay" style="display:none;">${detail.remarkReplay}</div>
		</div>
				
		<div class="row">
				<div class="w89">
				<table class="table-sku">
				<thead>
					<tr>
						<th width="16%" class="th-font">下单规格:</th>
						<th width="14%" class="th-font">货源价格:</th>
						<th width="5%" class="th-font">搜索价格:</th>
						<th width="15%" class="th-font">售卖价格(免邮价):</th>
						<th width="4%" class="th-font">数量:</th>
						<th width="6%" class="th-font">单位:</th>
						<!-- <th>时间</th> -->
						<th width="31%" class="th-font">货源:</th>
					</tr>
				</thead>
				<tbody>
					<tbody class="lu_tr">
					 <c:forEach items="${detail.skus }" var="sku">
					 <tr class="sku-u-td">
						<c:if test="${sku.state == 1}">
						<td>
						<input type="hidden" class="lu_id" value="${sku.id}">
						<input type="text" class="input-w8 lu_sku" value="${sku.sku}"></td>
						<td class="td-price">USD:<span class="lu-price-buy">${sku.priceBuy }</span>(CNY:<input type="text" value="${sku.priceBuyc }" class="lu-price-buy-c input-w1" onchange="changePrice(this)">)</td>
						<td>${detail.price }</td>
						<td>USD:<input type="text" value="${sku.price }" class="lu-price-sale input-w1">(含运费<input type="text" value="${sku.shipFeight }" class="lu-ship-feight input-w1">)</td>
						<td><input type="text" class="input-w6 lu_count" value="${sku.num}"></td>
						<td><input type="text" class="input-w75 lu_unit" value="${sku.unit}"></td>
						<td><input type="text" class="input-w5 lu_url" value="${sku.url}">
						<button class="btn btn-info btn-update">修改</button>
						 <button class="btn btn-info btn-invalid">无效</button>
						</td></c:if>
						<c:if test="${sku.state != 1}">
						<td class="td-in-valid">${sku.sku}</td>
						<td>USD:<span class="td-in-valid">${sku.priceBuy }</span>(CNY:<span class="td-in-valid">${sku.priceBuyc }</span>)</td>
						<td class="td-in-valid">${detail.price }</td>
						<td class="td-in-valid">USD:${sku.price }(含运费${sku.shipFeight })</td>
						<td class="td-in-valid">${sku.num}</td>
						<td class="td-in-valid">${sku.unit}</td>
						<td class="td-in-valid">${sku.url}></td>
						</c:if>
						</tr>
					 </c:forEach>
					 <c:if test="${detail.skuCount == 0}">
						<tr class="sku-td">
							<td><input type="text" class="input-w8 lu_sku" value=""></td>
							<td class="td-price">USD:<span class="lu-price-buy"></span>(CNY:<input type="text" value="" class="lu-price-buy-c input-w1" onchange="changePrice(this)">)</td>
							<td>${detail.price }</td>
							<td>USD:<input type="text" value="" class="lu-price-sale input-w1">(含运费<input type="text" value="" class="lu-ship-feight input-w1">)</td>
							<td><input type="text" class="input-w6 lu_count" value="0"></td>
							<td><input type="text" class="input-w75 lu_unit"></td>
							<td><input type="text" class="input-w5 lu_url"><button class="btn btn-info btn-add">录入</button></td>
						</tr>
					 </c:if>
					</tbody>
				</table>
				</div>
		</div>
		<div class="row rb-add"><i class="b-add">+</i></div>
		<div class="row rowweight">重量:
		<c:if test="${detail.state ==-1}">
		${detail.weight}
		</c:if>
		<c:if test="${detail.state !=-1}">
		<input type="text" value="${detail.weight}" class="lu-weight input-w25">kg<button class="btn btn-info btn-weight">修改</button>
		</c:if>
		</div>
		</div>
		
		</div>
		</c:forEach>
		<br>
		<c:if test="${order.state==0 ||  order.state==1}">
		<button class="btn btn-success btn-finsh">确认订单</button>
		</c:if>
		<c:if test="${order.state==0 ||  order.state==1 || order.state==2}">
		<button class="btn btn-warning btn-can-not-buy">不能采购</button>		
		</c:if>
		
		</div>
		<br><br><br>
	</div>
<div class="tc">
	<div class="trnasparent"></div>
	<div class="container tc1">
	<input type="hidden"  id="tc_bfdid" value="">
		<div class="wrap row">
		<div class="col-xs-2"><img src="/cbtconsole/img/beforeLoad.gif" class="img-responsive img-product"></div>
		<div class="col-xs-10">
		<div class="row text-al tc-al-name"><span id="tc_name"></span>
		</div>
		<div class="row text-al"><span>Request:</span><span id="tc_remark"></span></div>
		<div class="row"> 
           <div class="b_left">
               <input type="hidden" id="goods_savePath" value="${savePath}" name="savePath">
               <input type="hidden" id="goods_localpath" value="${localpath}" name="localpath">
               <input type="hidden" id="goods_remotepath" value="${goods.remotpath}" name="remotepath">
               <textarea id="remark-replay-content" rows="100" style="width: 100%;"></textarea>
               <button class="btn btn-info btn-replay">更新</button>
           </div>
            </div>
		
		
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
<script type="text/javascript"
            src="/cbtconsole/js/xheditor-1.2.2.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/xheditor_lang/zh-cn.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/js/xheditor_skin/vista/iframe.css"/>
</html>














