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
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>无货率统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">

<script type="text/javascript">
	var searchReport = "/cbtconsole/StatisticalReport/selectReport2"; //报表查询
	var downloadReport = "/cbtconsole/StatisticalReport/showReport"; //导出报表
	var showGoods = "/cbtconsole/StatisticalReport/showGoods"; //显示商品
	var showGoodsByOrder = "/cbtconsole/StatisticalReport/showGoodsByOrder";
</script>
<script type="text/javascript">
	var rateLst = [];

	function doQuery(type) {
		var beginTime = $("#beginTime").val();
		var endTime = $("#endTime").val();
		if (beginTime == "") {
			alert("请输入开始时间!");
			return;
		}
		if (endTime == "") {
			alert("请输入结束时间!");
			return;
		}
		chooseShowRs(type);
		$.ajax({
			async : false,
			url : "/cbtconsole/StatisticalReport/noAvailableRate.do",
			data : {
				"type" : type,
				"beginTime" : beginTime,
				"endTime" : endTime
			},
			type : "post",
			success : function(data) {
				if (data.ok) {
					rateLst = data.data;
					showPage(1);
				} else {
					alert(data.message);
				}
			},
			error : function(e) {
				alert("访问后台失败，请刷新页面重试");
			}
		});

	}

	function showPage(num) {
		$("#categroyReport tbody").empty();
		var tbody = "";
		var countNum = rateLst.length > num * 10 ? num * 10 : rateLst.length;
		for (var i = (num - 1) * 10; i < countNum; i++) {
			tbody += "<tr>";
			tbody += "<td>" + rateLst[i].order + "</td>";
			tbody += "<td>" + rateLst[i].dateStr + "</td>";
			tbody += "<td>" + (rateLst[i].rate) + "%</td>";
			tbody += "</tr>";
		}
		if (tbody != "") {
			$("#categroyReport tbody").append(tbody);
		}
		$("#datacount").html(rateLst.length);
		$("#nowPage").html(num);
		$("#allPage").html(Math.ceil(rateLst.length / 10));
		$("#pagediv").show();
	}

	function chooseShowRs(type) {
		$("#month_show").hide();
		$("#week_show").hide();
		$("#day_show").hide();
		switch (type) {
		case 1:
			$("#month_show").show();
			break;
		case 2:
			$("#week_show").show();
			break;
		case 3:
			$("#day_show").show();
			break;
		}
	}
</script>
<style type="text/css">
<
style>.displaynone {
	display: none;
}
</style>

</head>
<body text="#000000">
	<div>
		<div>
			<form id="adduserForm" name="adduserForm" action="" method="post">
				<div class="box box-solid">
					<div class="box-header with-border">
						<h4>查询条件</h4>
					</div>
					<div class="box-body">
						<div class="form-group">
							<label for="nickname">统计日期<font color="red">*</font>：
							</label> <input id="beginTime" style="width: 120px" readonly="readonly"
								onfocus="WdatePicker({isShowWeek:true})"> <label
								for="beginTime"></label> <span>~</span> <input id="endTime"
								style="width: 120px" readonly="readonly"
								onfocus="WdatePicker({isShowWeek:true})"> <label
								for="endTime"></label> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
								type="button" class="btn-primary" value="按月统计" onclick="doQuery(1)">
							&nbsp;&nbsp;&nbsp;&nbsp; <input type="button" class="btn-primary" value="按周统计"
								onclick="doQuery(2)"> &nbsp;&nbsp;&nbsp;&nbsp; <input
								type="button" class="btn-primary" value="按日统计" onclick="doQuery(3)">
						</div>
					</div>
					<div style="padding: 15px;">
						<span id="month_show" style="display: none;">&nbsp;&nbsp;按月统计结果:</span>
						<span id="week_show" style="display: none;">&nbsp;&nbsp;按周统计结果:</span>
						<span id="day_show" style="display: none;">&nbsp;&nbsp;按日统计结果:</span>
						<table id="categroyReport" class="imagetable">
							<thead>
								<tr>
									<th width="120">序号</th>
									<th width="240">起止日期</th>
									<th width="150">无货率</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
					<br>
					<div style="display: none;" id="pagediv">
						共查到<span id="datacount">0</span>数据&nbsp;&nbsp; <input
							type="button" class="btn-primary" id="prePage" value="上一页" />&nbsp; 第<span
							id="nowPage">1</span>页/共<span id="allPage">0</span>页 <input
							type="button" class="btn-primary" id="nextPage" value="下一页" />&nbsp;&nbsp; <input
							type="text" id="toPage" style="width: 50px;" /><input
							type="button" class="btn-primary" value="Go" id="jumpPage" />
					</div>
				</div>
			</form>
		</div>

	</div>
</body>
<script type="text/javascript">
	$("#prePage").click(function() {
		var nowPage = $("#nowPage").html();
		if (parseInt(nowPage) <= 1) {
			alert("已到达首页");
			return false;
		} else {
			$("#nowPage").html(parseInt(nowPage) - 1)
			showPage(parseInt(nowPage) - 1);
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
			showPage(parseInt(nowPage) + 1);
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
			showPage(parseInt(topage));
		}
	});
</script>
</html>