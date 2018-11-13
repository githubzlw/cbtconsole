<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<title>上传产品</title>
<style type="text/css">
body {
	width: 1500px;
	margin: 0 auto;
}

.body {
	width: 1500px;
	height: auto;
}
/* .error{background-color:#FF8484;color:white;}
.error2{background-color:#93926C;color:white;}
.error3{background-color: yellow;;color:red;}
.error4{background-color:#00FFFF;} */
a {
	cursor: pointer;
	text-decoration: underline;
}

.btn {
	color: #fff;
	background-color: #5db5dc;
	border-color: #2e6da4;
	padding: 5px 10px;
	font-size: 12px;
	line-height: 1.5;
	border-radius: 3px;
	border: 1px solid transparent;
	cursor: pointer;
}
</style>
<script type="text/javascript">
	function fnjump(obj) {
		var page = $("#page").val();
		if (page == "") {
			page = "1";
		}
		if (obj == -1) {
			if (parseInt(page) < 2) {
				return;
			}
			page = parseInt(page) - 1;
		} else if (obj == 1) {
			if (parseInt(page) > parseInt($("#totalpage").val()) - 1) {
				return;
			}
			page = parseInt(page) + 1;
		} else if (obj == -2) {
			page = 1;
		}

		$("#page").val(page);
		window.location.href = "/cbtconsole/cutom/rlist?page=" + page
	}
</script>

</head>
<body class="body">
	<div align="center">
		<br>
		<button class="btn" name="back" value="后退" onclick="history.back();" style="float: left;">返回</button>
		<br>
		<div>




			<table id="table" border="1" cellpadding="0" cellspacing="0"
				class="table">
				<tr align="center" bgcolor="#DAF3F5">
					<td style="width: 50px;"></td>
					<td style="width: 150px;">产品ID</td>
					<td style="width: 150px;">时间</td>
					<td style="width: 200px;">操作人</td>
					<td style="width: 100px;">操作</td>
					<td style="width: 300px;">备注</td>
				</tr>


				<c:forEach items="${recordList}" var="list" varStatus="index">
					<tr bgcolor="#FFF7FB">
						<!-- 索引 -->
						<td>${index.index+1}</td>
						<!-- 产品id -->
						<td id="user_${index.index}">${list.goodsPid}</td>
						<!-- 时间  -->
						<td id="state_${index.index}" style="text-align: center;"><span
							style="font-size: 14px;">${list.updateTime}</span></td>

						<!-- 最近发布人  -->
						<td id="admin_${index.index}" style="text-align: center;">
							${list.admin}</td>

						<!-- 操作 -->
						<td id="order_${index.index}" style="text-align: center;">
							${list.goodsState}</td>
						<!-- 操作 -->
						<td id="record_${index.index}" style="text-align: left;">
							${list.record}</td>
					</tr>
				</c:forEach>

			</table>
			<br>
			<div>
				<input type="hidden" id="totalpage" value="${totalpage}">

				总共:&nbsp;&nbsp;<span id="pagetotal">${currentpage}<em>/</em>
					${totalpage}
				</span> 页&nbsp;&nbsp; <input type="button" value="上一页" onclick="fnjump(-1)"
					class="btn"> <input type="button" value="下一页"
					onclick="fnjump(1)" class="btn"> 第<input id="page"
					type="text" value="${currentpage}" style="height: 26px;"> <input
					type="button" value="查询" onclick="fnjump(0)" class="btn">
			</div>
			<br> <br> <br> <br>
		</div>
	</div>
</body>
</html>