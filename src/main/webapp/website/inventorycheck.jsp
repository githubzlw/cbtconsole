<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>


<title>库存盘点</title>
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

/* .report th, .report td {
	text-align: center;
} */
.datagrid-cell-c2-goodsSku{width: 350px;}
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
.tc1,.tc2,.tc3,.tc4{position:absolute;z-index:10;display:none;top:100px;left:50%;margin-left:-600px;background-color:#fff;
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
.tc3 .wrap8 button{border:1px solid #999;padding:0 80px; line-height:28px;border-radius: 4px;}

/* 产品弹窗 tc4 */
*{margin:0;padding:0;box-sizing: border-box;}
.tc4 em,.tc4 i{font-style: normal;display: inline-block;float:left;}
.clearfix:before,.clearfix:after{content:"";display:table;}
.clearfix:after{clear:both;}
.clearfix{zoom:1;} 
.tc4.wraps{border:2px solid #999;width:1049px;padding:20px;margin-left:-400px;}
.tc4.wraps span{display:inline-block;width:103px;float:left;}
.tc4.wraps input[type="text"]{border:1px solid #999;background-color: #fff;height:28px;border-radius: 4px;width:205px;float:left;}
.tc4.wraps input[type="radio"]{width:18px;height:18px;position: relative;top:2px;}
.tc4.wraps label{margin-right:10px;}
.tc4 .wrap6{overflow:hidden;}
.tc4 .wrap6 span,.wraps.tc3 .reasons{float:left;position: relative;}
.tc4 .w235{width:235px;}
.tc4.wraps .wrap{margin-bottom:40px;overflow:hidden;}
.tc4 .wrap7 img{width:250px;height: 250px;}
.tc4 .wrap2 em{width:645px;}
.tc4 .wrap7 {float:right;position: relative;top:-30px;}
.tc4 .left{float:left;}
.tc4 p{text-align: center;}
.tc4 .other{position: absolute;top:15px;right:-222px;}
.tc4 .wrap8{text-align: center;}
.tc4 .wrap8 button{border:1px solid #999;padding:0 80px; line-height:28px;border-radius: 4px;}


.report .btn_page_in{width:100px;}
.w350{width: 350px;}
.datagrid-cell-c2-goodsName{width:300px;}
</style>
</head>
<body>
	<div class="container-fluid report">
	<a href="/cbtconsole/inventory/list" style="font-size: 18px;">&lt;返回</a>
		<h1 class="text-center">库存盘点</h1>
		
			<input type="hidden" value="" id="query_in_id">
			
			<div class="row">
			<div class="col-xs-12">
			<label class="w350">产品分类： 
			<select class="form-control" id="query_catid_select" >
						
			</select>
			</label>
				<label><button class="btn btn-info"  id="query_button_check_start">开始盘点</button>
				<input type="hidden" value="${queryParam.check_id}" id="check_id">
				</label>
				<label><button class="btn btn-info qbt_check"  id="query_button_check_cancel">撤销本次盘点</button></label>
				<label><button class="btn btn-info qbt_check"  id="query_button_check_done">完成盘点/打印报表</button></label>
			</div>
			</div>
			
			
		<div class="row">
			<div class="col-xs-1">
				<b>产品检索</b>
			</div>
			<div class="col-xs-11">
				<!-- <label>产品名称：<input type="text" class="form-control" id="query_goods_name"></label> -->
				<label>产品ID：<input type="text" class="form-control p_q_r" id="query_goods_pid" value="${queryParam.goods_pid }"></label>
				<label>产品分类：
				<select class="form-control p_q_r" id="query_goodscatid" >
						
				</select>
				<input type="hidden" class="form-control p_q_r" id="query_goodscatid_q" value="${queryParam.goodscatid }">
				</label>
				<label>库存量大于：<input type="text" class="form-control p_q_r" id="query_minintentory" value="${queryParam.qminintentory }"></label>
				<label>库存量小于：<input type="text" class="form-control p_q_r" id="query_maxintentory" value="${queryParam.qmaxintentory }"></label>
				<label class="w200">是否上架： <select class="form-control  p_qs_r" id="query_line" >
				<c:if test="${queryParam.isline==0 }">
						<option value="0" selected="selected">全部</option>
						<option value="1" >是</option>
						<option value="2">否</option>
				</c:if>
				<c:if test="${queryParam.isline==1 }">
						<option value="0" >全部</option>
						<option value="1" selected="selected">是</option>
						<option value="2" >否</option>
				</c:if>
				<c:if test="${queryParam.isline==2 }">
						<option value="0" >全部</option>
						<option value="1" >是</option>
						<option value="2" selected="selected">否</option>
				</c:if>
				</select>
				</label>
				<button class="btn btn-info bt_ready"  id="query_button_check">查询</button>
				
			</div>
		</div>
		<div class="row mt20 row2">
			<div class="col-xs-1">
				<b>库存修正</b>
			</div>
			<div class="col-xs-11">
				<button class="btn btn-success bt_ready" id="tc1">录入新产品</button>
				<button class="btn btn-success bt_ready" id="tc2">导入未匹配产品</button>
				<!-- <button class="btn btn-success" id="tc3">增加线上产品库存</button> -->
				<!-- <label><b>最新盘点时间：</b><span id="intentory_time">2019.8.7</span></label> -->
			</div>
		</div>
		<div class="row mt20">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>类别</th>
						<th>产品ID</th>
						<th>产品名称</th>
						<th>产品SKU</th>
						<th>产品图片</th>
						<th>上次盘点数量</th>
						<th>变更数量</th>
						<th>库存数量</th>
						<th>当前价格</th>
						<!-- <th>盘点数量</th> -->
						<th>库位</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${checkList }" var="tory" varStatus="index">
					<tr id="datagrid-row-r2-2-${index.index}">
					 
						<td >${tory.categoryName}</td>
						<td class="datagrid-cell-c2-goodsPid">${tory.goodsPid}</td>
						<td class="datagrid-cell-c2-goodsName">${tory.goodsName}</td>
						<td  class="datagrid-cell-c2-goodsSku"><em class="emsku">${tory.goodsSku}</em>
						<br>
						<em>Skuid:</em><em class="emskuid">${tory.goodsSkuid}</em>
						<br>
						<em>Specid:</em><em class="emspecid">${tory.goodsSpecid}</em></td>
						
						<td class="datagrid-cell-c2-carImg">${tory.goodsImg }</td>
						<td class="datagrid-cell-c2-last-remaining">${tory.lastCheckRemaining}</td>
						<td class="datagrid-cell-c2-change-remaining">${tory.changeRemaining}</td>
						<td class="datagrid-cell-c2-remaining">${tory.remaining}
						</td>
						<td class="emprice">${tory.goodsPrice}</td>
						<%-- <td class="datagrid-cell-c2-checkRemaining">
						<input class="datagrid-cell-c2-check-Remaining q_in_r c_remaining" value="${tory.remaining}" readonly="readonly" onchange="updateCheckRecord(${index.index})" id="iq_in_r${index.index}"></td>
						 --%>
						 <td class="datagrid-cell-c2-barcode">
						 <em class="barcode_code">${tory.barcode}</em>
						<em class="datagrid-cell-c2-canRemaining" style="display:none;">${tory.canRemaining}</em>
						<input value="${tory.barcode}" class="q_in_barcode_h" type="hidden">
						<input value="0" class="q_record_id" type="hidden">
						<input value="${tory.inventorySkuId}" class="q_inventory_id" type="hidden">
						<input value="${tory.barcode}" class="q_in_barcode" readonly="readonly" type="hidden" onchange="updateCheckRecord(${index.index})"></td>
						<td>
							${tory.operation}
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
				<div>
				<span>当前页 :${queryParam.current_page } / ${toryListPage},总共 ${checkListCount }条数据,跳转</span>
				<input type="text" class="form-control btn_page_in" id="current_page" value="${queryParam.current_page }">
				<button class="btn btn-success btn_page_qu" onclick="doQuery(1,1)">查询</button>
				<button class="btn btn-success btn_page_up" onclick="doBeforePage(1,1)">上一页</button>
				<button class="btn btn-success btn_page_down" onclick="doNextPage(1,1)">下一页</button>
				
				<input type="hidden" value="${toryListPage}" id="total_page">
				</div>
		</div>
		
	</div>
<div class="tc">
	<div class="trnasparent"></div>
	<div class="container tc1">
		<div class="wrap row">
			<div class="col-xs-9">
				<div class="form-horizontal">
					<div class="form-group">
						<label class="col-xs-2 control-label text-left">产品ID:</label>
						<div class="col-xs-10">
							<input type="text" class="form-control" id="lu_pid" onchange="getProduct()">
							<input type="hidden"  id="lu_catid" value="">
							<input type="hidden"  id="lu_price" value="">
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
							<th>产品规格</th>
							<th>录入数量</th>
							<th>库位</th>
							<th>是否录入</th>
						</tr>
					</thead>
					<tbody id="lu_tr">
						<tr >
							<td><span class="lu_sku">xxx</span><br>
						<span class="lu_specid">ssss</span><br>
						<span class="lu_skuid">cccc</span></td>
							<td><input type="text" class="form-control lu_count" value="0"></td>
							<td class="lu_barcode"><input type="text" placeholder="请输入库位条形码" class="lu_barcode_a"></td>
							<td><input type="checkbox" name="entry" class="lu_is"></td>
						</tr>
						
					</tbody>
				</table>
				</div>
			</div>
			<div class="col-xs-3">
				<img
					src="https://img.kidsproductwholesale.com/importcsvimg/webpic/img/cl_72/children/banner1.jpg"
					alt="" class="img-responsive" id="lu_img">
			</div>
		</div>
		<div class="row remark">
			<div class="form-horizon col-xs-12">
				<div class="form-group">
					<label>备注原因</label> <label>
						<input type="radio" name="reason" class="lu_reason" value="1" checked="checked"> 添加
					</label>
					<label>
						<input type="radio" name="reason" class="lu_reason" value="2"> 补货
					</label>
					<label>
						<input type="radio" name="reason" class="lu_reason" value="3"> 线下单
					</label>
					<label>
						<input type="radio" name="reason" class="lu_reason" value="4"> 其他
					</label>
					<label>
						<input type="text" class="form-control" id="lu_remark"> 
					</label>
				</div>
			</div>
			<div class="col-xs-12 text-center">
				<button class="btn btn-success" onclick="saveInventory()">保存</button>
			</div>

		</div>
	</div>
	<div class="tc2">
		<div class="transparent">
			<div class="transparent-bg"></div>
			<img src="" alt="" class="img-responsive">
		</div>
		<div class="container">
		<div class="row">
			<div class="form-horizon">
				<div class="form-group row">
					<label class="control-label col-xs-2">淘宝订单/运单号</label>
					<div class="col-xs-5">
						<input type="text" class="form-control" id="tb_order_shipno" onchange="getTbOrder()">
						<input type="hidden"  id="tb_h_order" value="">
						<input type="hidden"  id="tb_h_shipno"  value="">
					</div>
					<!-- <div class="col-xs-3">
						<input type="file">
						<button class="btn btn-default">选择未匹配订单</button>
					</div>	 -->				
				</div>
			</div>
		</div>
		<div class="row tc2_table">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>序号</th>
						<th>产品名称</th>
						<th>产品图片</th>
						<th>产品规格</th>
						<th>订单数量</th>
						<th>实际数量</th>
						<th>库位</th>
						<th>是否录入</th>
					</tr>
				</thead>
				<tbody id="lu_tb_tr">
					<tr>
						<td class="lu_tb_index">1</td>
						<td class="lu_tb_name">产品名称产品名称产品名称产品名称</td>
						<td><img src="https://img.kidsproductwholesale.com/importcsvimg/webpic/img/cl_72/children/banner1.jpg" alt="" class="img-responsive"></td>
						<td class="lu_tb_skuc">
						Sku:<span  class="lu_tb_sku">xxxxx</span><br>
						Skuid:<span  class="lu_tb_skuid">1111111111111</span><br>
						Specid:<span  class="lu_tb_specidc">2222222222222</span>
						</td>
						<td class="lu_tb_count">10</td>
						<td><input type="text" class="form-control lu_tb_a_count" value="10"></td>
						<td class="lu_tb_bar"><input type="text" placeholder="请输入库位条形码" class="lu_tb_barcode"></td>
						<td><input type="checkbox" class="lu_tb_checkbox">
						<input type="hidden" class="lu_tb_pid" value="">
						<input type="hidden" class="lu_tb_img" value="">
						<input type="hidden" class="lu_tb_url" value="">
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="row remark">
			<div class="form-horizon col-xs-12">
				<div class="form-group">
					<label >备注原因</label>
					<label>
						<input type="radio" name="reason" class="lu_tb_reason" value="1" checked="checked"> 添加
					</label>
					<label>
						<input type="radio" name="reason" class="lu_tb_reason" value="2"> 补货
					</label>
					<label>
						<input type="radio" name="reason" class="lu_tb_reason" value="3"> 线下单
					</label>
					<label>
						<input type="radio" name="reason" class="lu_tb_reason" value="4"> 其他
					</label>
					<label>
						<input type="text" class="form-control"  id="lu_tb_remark" value=""> 
					</label>
				</div>
			</div>
			<div class="col-xs-12 text-center">
				<button class="btn btn-success" onclick="saveTbInventory()">保存</button>
			</div>

		</div>
	</div>
	</div>
	<div class="wraps tc3">
		<div class="wrap wrap1">
			<span>产品 ID</span>
			<input type="text" id="index_igoodsID" readonly="readonly">
			<span style="margin-left: 40px;">SKUID</span>
			<input type="text" id="index_iskuid" readonly="readonly">
			<span style="margin-left: 40px;">SPECID</span>
			<input type="text" name="ispecid" id="index_ispecid" readonly="readonly">
		</div>
		<div class="wrap wrap2">
			<span>产品名称</span>
			<em id="index_igoodsname">产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称</em>
		</div>
		<div class="wrap-overflow clearfix">
			<div class="left">
				<div class="wrap wrap3">
					<span>产品规格</span>
					<em class="w235" id="index_isku">产品规格产品规格产品规格产品规格产品规格</em>
				</div>
				<div class="wrap wrap4">
					<span>库存数量</span>
					<i id="index_iremaining">20</i>
					<span style="margin-left:39px">可用库存数量</span>
					<i id="index_icanremaining">20</i>
				</div>
				<div class="wrap wrap5">
					<span>调整库存数量</span>
					<input type="text" id="index_ichangcount">
				</div>
			</div>
			<div class="wrap7">
				<p>产品图</p>
				<img src="https://img1.import-express.com/importcsvimg/importimg/559138175864/8063cce6-2b0d-47c9-abe5-95e2b7ec1032_179.png" alt="" id="index_iimg">
			</div>
		</div>
		
		<div class="wrap wrap6">
			<span>备注原因</span>
			<div class="reasons w235">
				<label>
					<input type="radio" name="reason" value="0" class="radio_change" checked="checked">
					损坏
				</label>
				<label >
					<input type="radio" name="reason" value="1" class="radio_change">
					遗失
				</label>
				<label>
					<input type="radio" name="reason" value="3" class="radio_change">
					添加
				</label>
				<label>
					<input type="radio" name="reason" value="4" class="radio_change">
					补货
				</label>
				<label>
					<input type="radio" name="reason" value="5" class="radio_change">
					漏发
				</label>
				<label>
					<input type="radio" name="reason" value="7" class="radio_change">
					其他
				</label>
				<input type="text" class="other" id="index_iremark">
			</div>
		</div>
		<div class="wrap wrap8">
		<input type="hidden" value="" id="index_in_id">
			<button onclick="addLoss()" class="btn btn-success">保存</button>
		</div>
	</div>
	<div class="wraps tc4">
		<div class="wrap wrap1">
			<span>产品 ID</span>
			<input type="text" id="index_check_igoodsID" readonly="readonly">
			<span style="margin-left: 40px;">SKUID</span>
			<input type="text" id="index_check_iskuid" readonly="readonly">
			<span style="margin-left: 40px;">SPECID</span>
			<input type="text" name="ispecid" id="index_check_ispecid" readonly="readonly">
		</div>
		<div class="wrap wrap2">
			<span>产品名称</span>
			<em id="index_check_igoodsname">产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称</em>
		</div>
		<div class="wrap-overflow clearfix">
			<div class="left">
				<div class="wrap wrap3">
					<span>产品规格</span>
					<em class="w235" id="index_check_isku">产品规格产品规格产品规格产品规格产品规格</em>
				</div>
				<div class="wrap wrap4">
					<span>库存数量</span>
					<i id="index_check_iremaining">20</i>
					<span style="margin-left:39px">可用库存数量</span>
					<i id="index_check_icanremaining">20</i>
				</div>
				<div class="wrap wrap5">
					<span>盘点数量</span>
					<input type="text" id="index_check_ichangcount">
				</div>
				<div class="wrap wrap5">
					<span>库位</span>
					<input type="hidden" id="index_check_barcode_b">
					<input type="text" id="index_check_barcode">
				</div>
			</div>
			<div class="wrap7">
				<p>产品图</p>
				<img src="https://img1.import-express.com/importcsvimg/importimg/559138175864/8063cce6-2b0d-47c9-abe5-95e2b7ec1032_179.png" alt="" id="index_check_iimg">
			</div>
		</div>
		
		
		<div class="wrap wrap8">
		<input type="hidden" value="" id="index_check_index">
		<input type="hidden" value="" id="index_check_goods_price">
		<input type="hidden" value="" id="index_check_q_record_id">
		<input type="hidden" value="" id="index_check_in_id">
			<button onclick="addcheck()" class="btn btn-success">盘点</button>
		</div>
	</div>
</div>
<input type="hidden" value="${isBarcodeDone}" id="isBarcodeDone">
</body>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/msg-confirm.js"></script>
<script type="text/javascript" src="/cbtconsole/js/inventoryReport.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
</html>














