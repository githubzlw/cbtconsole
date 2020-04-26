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
.tc1{position:absolute;z-index:10;display:none;top:100px;left:44%;margin-left:-300px;background-color:#fff;
padding: 20px;max-height: 715px;}
#chat_history{width: 100%;height: 600px;overflow-y: auto;}
.img-responsive, #chat_history img{max-width: 135px;max-height: 135px;margin-top: 11px;}
.h50{height: 50px;}
.w99{width:1700px;margin-top: 15px;border: 1px solid #8a6d3b;min-height: 150px;}
.wt35{ margin-left: 2px; height: 35px;margin-top: 10px;}
.w11{width: 99%;margin-left: 1%;}
.detail-div{width:1520px;}
.th-font{font-size: 14px;color: #ca5252;font-weight: initial;}
.remark-row{margin-top: 10px;    width: 85%;}
.td-font-new{font-size: 14px;color: #ca5252;font-weight:bold;margin-left: 5px;}
.ormnum{border: 1px dashed #f90;padding: 2px;margin-left: 5px;font-size: 17px;}
.img-dv{width: 10%;}
.remark-replay{border: 1px solid #ccc;border-radius: 4px;height: 34px;width:30%;}
.td-font-view{margin-left: 2%;cursor: pointer;}
.text-al{text-align: left;    margin-top: 22px; height: 46px;}
.tc-al-name{font-size: 18px;font-weight: bold;}
.tc1{width: 800px;border: 3px solid #ccc;border-radius: 21px;}
body{min-height:100%;}
.num-span{margin-left: 2%;}
.delete-all{font-weight: bold;font-size: 15px;color: #ef0e09;}
.de-w1{width:170px;}
</style>
</head>

<body>
<div style="background-color:#eaf5e5;min-height:100%;width:2000px;">
	<div class="container-fluid report">
		<h1 class="text-center">Buy For Me Cart</h1>
			<input type="hidden" value="${result.userId }" id="query_bf_id">
			<input type="hidden" value="${result.sessionId }" id="sessionId">
	<div  class="row w800">
			<div class="row h50">
			<div class="col-xs-4"><span class="th-font">totalPrice:</span><span class="ormnum" id="buy_order_no">${result.totalPrice} $</span></div>
			</div>
			<div class="row h50">
			<div class="col-xs-4"><span class="th-font">
				用户名:</span><span></span>(<span>${result.userId }</span>)<br>
				<span class="th-font">产品数:</span><span>${result.itemNum}</span><br>
				<span class="th-font">产品总数量:</span><span>${result.totalNum}</span><br>
			</div>
			<%--<div class="col-xs-4"><span class="th-font">产品数:</span><span>${result.itemNum}</span></div>
			<div class="col-xs-4"><span class="th-font">产品总数量:</span><span>${result.totalNum}</span></div>--%>
			</div>
	</div>

		<div class="row mt20">
		<c:forEach items="${result.itemList}" var="detail" varStatus="index">
			<div class="row w99 de-td">
			<input value="${detail.num}" class="lucount" type="hidden">
			<input value="${detail.id}" class="bfdid" type="hidden">
			<input value="${detail.num_iid}" class="bfpid" type="hidden">
			<input value="${detail.totalPrice}" class="price-ss" type="hidden">
			<span style="display: none" id="msg"> ${detail.remarkReplay}</span>
			<div class="col-xs-1 de-w1">
			<img src="/cbtconsole/img/beforeLoad.gif" data-original="${detail.pic_url }" class="img-responsive img-lazy img-de-v">
			<a href="${detail.detail_url }" target="_blank">商品原始链接</a>
			<div>
			</div>
			</div>
			<div class="col-xs-10 detail-div">
			<div class="row wt35">
			<span class="th-font">商品名称:</span><span class="name-title">${detail.title }</span>(<span class="td-numiid">${detail.num_iid }</span>)
			<span class="th-font num-span">数量:</span><span>${detail.num}</span>
			<span class="th-font num-span">价格:</span><span>${detail.price}</span>
			<span class="th-font num-span">总价格:</span><span>${detail.totalPrice}</span>
			</div>
			<div class="col-xs-10 detail-div">
				<div class="row wt35">
					<span class="th-font num-span">MOQ:</span><span>${detail.moq}</span>
			</div>
			<div class="row w11">
			<div class="row th-font">用户备注:</div>
			<div class="row remark-row">Q:<span class="th-font-l de-remarl-q">${detail.remark}</span><i class="td-font-new">New!</i> <i class="td-font-view">View</i></div>
			<div class="remark-replay" style="display:none;">${detail.remark_replay}</div>
			<div class="remark-replay" style="display:none;">${detail.remarkReplay}</div>
			</div>
			</div>
				<br><br><br>
			</div>
			</div>
		</c:forEach>

	</div>
</div>
<div class="tc">
	<div class="trnasparent"></div>
	<div class="container tc1">
	<input type="hidden"  id="tc_bfdid" value="">
		<input type="hidden"  id="tc_pid" value="">
		<div class="wrap row">
		<div class="col-xs-4">
			<img src="/cbtconsole/img/beforeLoad.gif" class="img-responsive img-product">
			<div id="chat_history">
			</div>
		</div>
		<div class="col-xs-8">
		<div class="row text-al tc-al-name"><span id="tc_name"></span>
		</div>
		<div class="row text-al"><span>Request:</span><span id="tc_remark"></span></div>
		<div class="row"> 
           <div class="b_left">
               <input type="hidden" id="goods_savePath" value="${savePath}" name="savePath">
               <input type="hidden" id="goods_localpath" value="${localpath}" name="localpath">
               <input type="hidden" id="goods_remotepath" value="${goods.remotpath}" name="remotepath">
               <textarea id="remark-replay-content" rows="300" style="width: 100%;min-height: 400px;"></textarea>
               <button class="btn btn-info btn-replay">更新</button>
           </div>
            </div>
		</div>
		</div>
	</div>
</div>
</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/msg-confirm.js"></script>
<script type="text/javascript" src="/cbtconsole/js/buyforme_cart_details.js"></script>
<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
            src="/cbtconsole/js/xheditor-1.2.2.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/xheditor_lang/zh-cn.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/js/xheditor_skin/vista/iframe.css"/>
</html>














