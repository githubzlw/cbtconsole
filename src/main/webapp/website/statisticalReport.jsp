<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>商品分类报表</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">
<script type="text/javascript">
	var searchReport = "/cbtconsole/StatisticalReport/selectReport"; //报表查询
	var downloadReport = "/cbtconsole/StatisticalReport/showReport"; //导出报表
	var showGoods = "/cbtconsole/StatisticalReport/showGoods"; //显示商品
</script>
<style type="text/css">
.displaynone {
	display: none;
}
.query_div{font-size:18px;}
.but_color {
	background: #44a823;
	width: 80px;
	height:30px;
	border: 1px #aaa solid;
	color: #fff;
}
</style>
</head>
<body text="#000000">
	<form id="adduserForm" name="adduserForm" action="" method="post">
			<div class="box box-solid">
			<br>
				<div class="query_div">
					<label for="rYear">年份选择<font color="red">*</font>：
					</label> <select name="" id="rYear">
					</select>&nbsp;&nbsp;&nbsp;&nbsp; 
					<label for="rMonth">月份选择<font color="red">*</font>：
					</label> <select name="" id="rMonth">
						<option>请选择</option>
					</select>&nbsp;&nbsp;&nbsp;&nbsp; 
					<label for="orderName">排行条件：</label> <select
						name="" id="orderName">
						<option value="sales_volumes">销售量</option>
						<option value="sales_price">销售额</option>
						<option value="profit_loss">盈亏</option>
					</select>
					&nbsp;<input id="pgSearch" class="but_color" type="button" value="查 询" />
					<%--&nbsp;<input id="addReport" class="but_color" type="button" value="生成报表" />--%>
					&nbsp;<input id="pgExport" class="but_color" type="button" value="导出报表" />
					<%--&nbsp;<span id="addReport1" style="display: none;">报表生成中...</span>--%>
				</div>
				<div style="padding: 15px;">
					<h3>分类报表数据：</h3>
					<table id="categroyReport" class="imagetable">
						<thead>
							<tr>
								<th width="120">序号</th>
								<th width="150">商品分类</th>
								<th width="150">采购金额(人民币)</th>
								<th width="150">销售金额(人民币)</th>
								<th width="150">销售平均价(人民币)</th>
								<th width="150">销售数量(个)</th>
								<th width="150">采购数量(个)</th>
								<th width="150">盈亏(%)</th>
								<!-- <th width="100">估算重量</th>
							<th width="100">计费重量</th>
							<th width="100">实收重量</th> -->
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div style="text-align: center; display: none;" id="pagediv">
					共查到<span id="datacount">0</span>数据&nbsp;&nbsp; <input type="button"
						id="prePage" value="上一页" />&nbsp; 第<span id="nowPage">1</span>页/共<span
						id="allPage">0</span>页 <input type="button" id="nextPage"
						value="下一页" />&nbsp;&nbsp; <input type="text" id="toPage"
						style="width: 50px;" /><input type="button" value="Go"
						id="jumpPage" />
				</div>
				<div style="padding: 15px;">
					<h3>报表合计：</h3>
					<table id="sumReport" class="imagetable">
						<thead>
							<tr>
								<th width="120">序号</th>
								<th width="150">分类数量(个)</th>
								<th width="150">总支出(人民币)</th>
								<th width="150">总收入(人民币)</th>
								<th width="150">销售商品数(个)</th>
								<th width="150">销售平均价(人民币)</th>
								<th width="150">盈亏(%)</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>

				<div id="goods" class="displaynone" style="padding: 15px;">
					<label>商品详情：</label>
					<table id="goodsDetail" class="imagetable">
						<thead>
							<tr>
								<th width="40">NO.</th>
								<th width="120">orderid</th>
								<th width="120">goods_name</th>
								<th width="150">goods_url</th>
								<th width="150">goods_p_url</th>
								<th width="150">goods_img_url</th>
								<th width="90">goods_price</th>
								<th width="90">goods_p_price</th>
								<th width="80">usecount</th>
								<th width="80">buycount</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>

				<div style="display: none;">
					<input type="hidden" id="type" value="${type}"> <input
						id="timeFrom" type="hidden"> <input id="timeTo"
						type="hidden">
				</div>
			</div>
		</form>

</body>
<script type="text/javascript">
	function toStatisticalReport() {
		window.location.href = "/cbtconsole/StatisticalReport/getMounthReport";
	}

	function round2(number, fractionDigits) { //实现四舍五入保留小数位
		with (Math) {
			return round(number * pow(10, fractionDigits))
					/ pow(10, fractionDigits);
		}
	}

	//生成报表
	$('#addReport').click(
			function() {
				var choseYear = $('#rYear').val();
				var choseMonth = $('#rMonth').val();
				var choseDay = $('#rDay').val();
				var type = $('#type').val();
				if (choseYear == -1) {
					alert('请选择年份！');
					return;
				} else if (choseMonth == -1) {
					alert('请选择月份！');
					return;
				}
				choiseDay = new Date(choseYear, choseMonth, 0).getDate();
				var date1 = choseYear + '-' + choseMonth + '-1 00:00:00';
				var date2 = choseYear + '-' + choseMonth + '-' + choiseDay
						+ ' 23:59:59';
				$('#addReport').hide();
				$('#addReport1').show();

				jQuery.ajax({
					url : "/cbtconsole/StatisticalReport/addReport",
					data : {
						"timeFrom" : date1,
						"timeTo" : date2,
						"reportYear" : choseYear,
						"reportMonth" : choseMonth,
						"type" : type
					},
					type : "post",
					success : function(data, status) {
						$('#addReport').show();
						$('#addReport1').hide();
						if (data.ok) {
							$('#pgSearch').trigger('click');
						} else {
							alert(data.message);
						}
					},
					error : function(e) {
						alert("没有数据！");
					}
				});
			});
	$("#pgExport")
			.click(
					function() {
						//window.location.href="/cbtconsole/StatisticalReport/get";
						//生成报表
                        var orderName = $('#orderName').val();
						var choseYear = $('#rYear').val();
						var choseMonth = $('#rMonth').val();
						var choseDay = $('#rDay').val();
						var type = $('#type').val();
						if (choseYear == -1) {
							alert('请选择年份！');
							return;
						} else if (choseMonth == -1) {
							alert('请选择月份！');
							return;
						}
						window.location.href = "/cbtconsole/StatisticalReport/exportCategoryReport?reportYear="
								+ choseYear
								+ "&reportMonth="
								+ choseMonth
								+ "&orderName="
								+ orderName
								+ "&type=" + type;
					});

	$("#prePage").click(function() {
		var nowPage = $("#nowPage").html();
		if (parseInt(nowPage) <= 1) {
			alert("已到达首页");
			return false;
		} else {
			$("#nowPage").html(parseInt(nowPage) - 1)
			searchExport(parseInt(nowPage) - 1);
		}
	});
	$("#nextPage").click(function() {
		var nowPage = $("#nowPage").html();
		var allPage = $("#allPage").html();
		if (parseInt(nowPage) == parseInt(allPage)) {
			alert("已到达尾页");
			return false;
		} else {
			$("#nowPage").html(parseInt(nowPage) + 1)
			searchExport(parseInt(nowPage) + 1);
		}
	});
	$("#jumpPage").click(function() {
		var allPage = $("#allPage").html();
		var topage = $("#toPage").val();
		if (isNaN(topage)) {
			alert("请输入正确的页码");
			return false;
		} else if (parseInt(topage) <= 0 || parseInt(topage) > allPage) {
			alert("页码超出范围");
			return false;
		} else {
			$("#nowPage").html(parseInt(topage))
			searchExport(parseInt(topage));
		}
	});
	//查询报表
	$('#pgSearch').click(function() {
		searchExport(1)
	});

	function searchExport(page) {
		$("#goods").addClass("displaynone");
		var choseYear = $('#rYear').val();
		var choseMonth = $('#rMonth').val();
		var choseDay = $('#rDay').val();
		var orderName = $('#orderName').val();
		var type = $('#type').val();
		$("#sumReport tbody").html("");
		$("#categroyReport tbody").html("");
		if (choseYear == -1) {
			alert('请选择年份！');
			return;
		} else if (choseMonth == -1) {
			alert('请选择月份！');
			return;
		}
		choiseDay = new Date(choseYear, choseMonth, 0).getDate();
		var date1 = choseYear + '-' + choseMonth + '-1 00:00:00';
		var date2 = choseYear + '-' + choseMonth + '-' + choiseDay
				+ ' 23:59:59';
		$('#timeFrom').val(date1);
		$('#timeTo').val(date2);

		jQuery
				.ajax({
					url : searchReport,
					data : {
						"reportYear" : choseYear,
						"reportMonth" : choseMonth,
						"orderName" : orderName,
						"page" : page,
						"type" : type
					},
					type : "post",
					success : function(data, status) {
						if (data.total>0) {
							var reportDetailList = data.data;
							var reportInfoList = data.allData;

							$("#pagediv").css("display", "block");
							var datacount = data.total;
							$("#datacount").html(datacount);
							if (datacount % 20 == 0) {
								$("#allPage").html(datacount / 20);
							} else {
								$("#allPage").html(parseInt(datacount / 20 + 1));
							}
							$("#nowPage").html(page);

							var htm_ = '';
							htm_ += '<tr>                                               ';
							htm_ += '<td>1</td>                                         ';
							htm_ += '<td>' + reportInfoList[0].cateAmount
									+ '</td>';
							htm_ += '<td>'
									+ reportInfoList[0].buyAmount+ '</td>';
							htm_ += '<td>'
									+ reportInfoList[0].salesAmount
									+ '</td>';
							htm_ += '<td>' + reportInfoList[0].salesCount
									+ '</td>';
							htm_ += '<td>'
									+ reportInfoList[0].avgSalesPrice
									+ '</td>';
							htm_ += '<td>'
									+ reportInfoList[0].profitLoss*100
									+ '</td>';
							htm_ += '</tr>                                              ';
							$("#sumReport").append(htm_);
							for (var i = 0; i < reportDetailList.length; i++) {
								var reportDetail = new Object();
								reportDetail = reportDetailList[i];
								htm_ = '';
								htm_ = '<tr>                                               ';
								htm_ += '<td >' + ((page - 1) * 20 + i + 1)
										+ '</td>';
								htm_ += '<td class="showDetail" style="cursor:pointer;"><font color="red" style="text-decoration:underline">'
										+ reportDetail.en_name
										+ '</font>';
								htm_ += '<input type="hidden" id="categroy" value="'+reportDetail.category_id+'"/></td>';
                                htm_ += '<td >'
                                    + reportDetail.buyAmount
                                    + '</td>';
								htm_ += '<td >'
										+ reportDetail.salesAmount
										+ '</td>';
								htm_ += '<td >'
										+ reportDetail.avgSalesPrice
										+ '</td>';
								htm_ += '<td >'
										+ reportDetail.salesCount
										+ '</td>';
								htm_ += '<td >' + reportDetail.buyCount
										+ '</td>';
								htm_ += '<td >' + reportDetail.profitLoss*100
										+ '</td>';
								htm_ += '</tr>                                             ';
								$('#categroyReport').append(htm_);
							}
						} else {
							//alert("查询失败！");
							$("#pagediv").css("display", "none");
							$("#sumReport")
									.append(
											"<tr><td colspan=8 style='text-align:center;color:red;'>(没有数据)</td></tr>");
							$("#categroyReport")
									.append(
											"<tr><td colspan=8 style='text-align:center;color:red;'>(没有数据)</td></tr>");
						}
					},
					error : function(e) {
						$("#pagediv").css("display", "none");
						alert("查询失败！");
					}
				});
	}

	//点击商品分类查询该报表下商品详细信息
	$("#categroyReport")
			.on(
					"click",
					".showDetail",
					function() {
						var categroy = $(this).find('#categroy').val();
						var timeFrom = $('#timeFrom').val();
						var timeTo = $('#timeTo').val();
						$("#goodsDetail tbody").html("");
						jQuery
								.ajax({
									url : showGoods,
									data : {
										"categroy" : categroy,
										"timeFrom" : timeFrom,
										"timeTo" : timeTo
									},
									type : "post",
									success : function(data, status) {
										if (data.ok) {
											var orderProductSourceList = data.data;
											for (var i = 0; i < orderProductSourceList.length; i++) {
												var goods = new Object();
												goods = orderProductSourceList[i];
												var htm_ = '';
												htm_ += '<tr>                                               ';
												htm_ += '<td>' + (i + 1)
														+ '</td>';
												htm_ += '<td><a href="/cbtconsole/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo='
														+ goods.orderid
														+ '&rand='
														+ Math.random()
														+ '" target="blank">'
														+ goods.orderid
														+ '</a></td>';
												htm_ += '<td>'
														+ goods.goodsName
														+ '</td>';
												htm_ += '<td><a href="'+goods.goodsUrl+'" target="_blank">'
														+ goods.goodsUrl
														+ '</a></td>';
												htm_ += '<td><a href="'+goods.goodsPUrl+'" target="_blank">'
														+ goods.goodsPUrl
														+ '</td>';
												htm_ += '<td>'
														+ goods.goodsImgUrl
														+ '</td>';
												htm_ += '<td>'
														+ goods.goodsPrice
														+ '</td>';
												htm_ += '<td>'
														+ goods.goodsPPrice
														+ '</td>';
												htm_ += '<td>' + goods.usecount
														+ '</td>     ';
												htm_ += '<td>' + goods.buycount
														+ '</td>';
												htm_ += '</tr>                                              ';
												$("#goodsDetail").append(htm_);
											}
											$("#goods").removeClass(
													"displaynone");
										} else {
											//alert("查询失败！");
											$("#goodsDetail")
													.append(
															"<tr><td colspan=10 style='text-align:center;color:red;'>(没有数据)</td></tr>");
											$("#goods").removeClass(
													"displaynone");
										}
									},
									error : function(e) {
										//alert("查询失败！");
										$("#goodsDetail")
												.append(
														"<tr><td colspan=10 style='text-align:center;color:red;'>(没有数据)</td></tr>");
									}
								});
					});
</script>
</html>