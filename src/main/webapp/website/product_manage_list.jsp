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
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<title>线下产品列表</title>
<style type="text/css">
a {
	cursor: pointer;
	text-decoration: underline;
}

b {
	color: red;
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

.btn2 {
	color: #fff;
	background-color: #cccccc;
	border-color: #2e6da4;
	padding: 5px 10px;
	font-size: 12px;
	line-height: 1.5;
	border-radius: 3px;
	border: 1px solid transparent;
	cursor: default;
}

.a_style {
	display: block;
	width: 229px;
}

.td_style {
	background-color: #880c0c;
}

.tr_disable {
	background-color: #d4d3d3;
	!
	import
}

.tr_edited {
	background-color: #45f959;
}
</style>
<script type="text/javascript">
	$(function() {
		var state = '${param.state}';
		if (!state || state == '') {
			state = '0';
		}
		if (state == 5 || state == "5") {
			fnselect(1);
		}

	});
	/* 发布  */
	function fnpublish(pid, adminid) {
		if (pid == null || pid == '') {
			return;
		}
		if (adminid == null || adminid == '' || adminid == '0') {
			return;
		}
		$("#publish_" + pid).removeAttr("onclick");
		$("#publish_" + pid).removeClass("btn").addClass("btn2");

		$.ajax({
			type : 'POST',
			dataType : 'text',
			url : '/cbtconsole/productOff/publish',
			data : {
				pid : pid,
				adminid : adminid
			},
			success : function(res) {
				if (res == 'fail') {
					$.messager.alert("提醒", "图片服务器出错啦......", "error");
					//alert("图片服务器出错啦......");
				} else {
					window.location.reload();
				}
			},
			error : function(XMLResponse) {
				$.messager.alert("提醒", "error", "error");
			}
		});
	}
	/* 下架  */
	function fndown(pid, adminid) {
		if (pid == null || pid == '') {
			return;
		}
		if (adminid == null || adminid == '' || adminid == '0') {
			return;
		}
		$("#down_" + pid).removeAttr("onclick");
		$("#down_" + pid).removeClass("btn").addClass("btn2");
		$.ajax({
			type : 'POST',
			dataType : 'text',
			url : '/cbtconsole/productOff/offshelf',
			data : {
				pid : pid,
				adminid : adminid
			},
			success : function(res) {
				showMessage("执行成功");
				window.location.reload();
			},
			error : function(XMLResponse) {
				//alert('error');
				$.messager.alert("提醒", "error", "error");
			}
		});
	}

	/* 全选  */
	function fnselect(type) {
		if (type == 1) {
			$("#checked_all").val('1');
			$(".checkpid").each(function() {
				if (!$(this).hasClass("is_disabled")) {
					$(this).prop("checked", true);
					$(this).parent().addClass("td_style");
				}
			});
		} else {
			var checked = $("#checked_all").val();
			$("#checked_all").val(checked == '0' ? '1' : '0');
			if (checked == '0') {
				$(".checkpid").each(function() {
					if (!$(this).hasClass("is_disabled")) {
						$(this).prop("checked", true);
						$(this).parent().addClass("td_style");
					}
				});
			} else {
				$(".checkpid").prop("checked", false);
				$(".checkpid").parent().removeClass("td_style");
			}
		}
	}
	/* 展示td的背景色*/
	function showBkColor(obj) {
		var is = $(obj).parent().hasClass("td_style");
		if (is) {
			$(obj).parent().removeClass("td_style");
		} else {
			$(obj).parent().addClass("td_style");
		}
	}

	function fnpsave(adminid) {
		var checked = "";
		var save_tran_json = [];
		var flag = 1;
		$(".checkpid:checked").each(function() {
			var pidv = this.value;
			checked += pidv + ',';
			var tj = {
				"pid" : pidv
			};
			var enname = $("#import_" + pidv).val();
			if (enname == "") {
				showMessage("pid:" + pidv + " 无翻译文字，请确认");
				flag = 0;
			} else {
				tj["enname"] = enname;
			}
			save_tran_json[save_tran_json.length] = tj;
		});
		if (checked == '') {
			showMessage("当前未选中");
			return;
		}
		if (flag == 0) {
			showMessage("选中pid中含有无翻译文字");
			return;
		}

		$.ajax({
			type : 'POST',
			dataType : 'json',
			url : '/cbtconsole/productOff/batchSaveEnName',
			data : {
				ennames : JSON.stringify(save_tran_json)
			},
			success : function(data) {
				if (data.ok) {
					showMessage("保存成功");
					window.location.reload();
				} else {
					$.messager.alert("提醒", data.message, "error");
				}
			},
			error : function(XMLResponse) {
				$.messager.alert("提醒", "保存错误，请联系管理员", "error");
			}
		});

	}


	function batchDelete() {

		var checked = "";
		$(".checkpid:checked").each(function() {
			checked += this.value + ',';
		});
		if (checked == '') {
			showMessage("请选择需要删除的数据");
			return;
		}

		$.messager.confirm('提示', '是否确认删除? 删除后数据将无法恢复！', function(rs) {
			if (rs) {
				$("#batch_delete").attr("disabled", true);
				$.ajax({
					type : 'POST',
					dataType : 'text',
					url : '/cbtconsole/productOff/batchDelete',
					data : {
						pids : checked
					},
					success : function(data) {
						$("#batch_delete").removeAttr("disabled");
						var json = eval('(' + data + ')');
						if (json.ok) {
							showMessage("执行成功");
							parent.reloadHtml();
						} else {
							showMessage(json.message);
						}
					},
					error : function() {
						$("#batch_delete").removeAttr("disabled");
						$.messager.alert("提醒", "error", "error");
					}
				});
			}
		});
	}

	function showMessage(msgStr) {
		$.messager.show({
			title : '提醒',
			msg : msgStr,
			timeout : 1500,
			showType : 'slide',
			style : {
				right : '',
				top : ($(window).height() * 0.35),
				bottom : ''
			}
		});
	}

	function changeAfterEdit(pid, obj) {
		$("#pb_btn_" + pid).val("重新发布");
		$("#pb_btn_" + pid).removeAttr("onclick");
		$("#pb_btn_" + pid).attr("onclick",
				"saveAndRepublishGoods(" + pid + ")");
		$("#checkpid_" + pid).attr("checked", true);
		$(obj).parent().parent().css("background-color", "rgb(132, 255, 255)");
	}

	function fnjump(num) {
		var totalpage = $("#totalpage").val();
		var page = $("#page").val();
		if (page == "") {
			page = "1";
		}
		if (num == -1) {//前一页
			if (parseInt(page) > 1) {
				page = parseInt(page) - 1;
			} else {
				page = 1;
			}
		} else if (num == 1) {//下一页
			if (parseInt(page) > parseInt(totalpage) - 1) {
				return;
			}
			page = parseInt(page) + 1;
		} else if (num < -1) {
			page = 1;
		}
		parent.parentDoQuery(page);
	}
</script>

</head>
<body>
	<div>
		<span><input type="button" id="sall"
			onclick="fnpsave('${uid}')" value="批量保存"
			style="height: 30px; width: 80px;" class="btn"></span>
		<span><input type="button" id="batch_delete"
			onclick="batchDelete()" value="批量删除"
			style="height: 30px; width: 80px; margin-right: 40px; border-color: #2e6da4; color: white; background-color: red;"></span>
		<span><b style="color: red;">*[</b> <b style="color: #d4d3d3;">灰色背景</b>
			<b style="color: red;">表示非本人编辑过的商品/</b> <b style="color: #45f959;">绿色背景</b>
			<b style="color: red;">表示本人编辑过的商品]</b> </span>
	</div>
	<br>
	<table id="table" border="1" cellpadding="0" cellspacing="0"
		class="table">
		<tr align="center" bgcolor="#DAF3F5">
			<td style="width: 65px;"><span style="font-size: 14px;"><input
					id="checked_all" type="checkbox" style="height: 16px; width: 16px;"
					value="0" onclick="fnselect(0)">全选</span></td>
			<td style="width: 120px;">1688PID</td>
			<td style="width: 200px;">1688图片</td>
			<td style="width: 190px;">1688产品名称</td>
			<td style="width: 250px;">ImportE翻译名称</td>
			<td style="width: 190px;">对应Aliexpress名称</td>
			<td style="width: 200px;">Aliexpress图片</td>
			<td style="width: 250px;">状态标识</td>
			<td style="width: 130px;">操作</td>
		</tr>


		<c:forEach items="${goodsList}" var="list" varStatus="index">
			<c:if test="${list.canEdit !=0 && list.canEdit != uid}">
				<tr class="tr_disable">
					<!-- 索引 -->
					<td><input type="checkbox" id="checkpid_${list.pid}"
						class="checkpid is_disabled" style="height: 16px; width: 16px;"
						value="${list.pid}" disabled="disabled" title="非当前商品的编辑人">
			</c:if>
			<c:if test="${list.canEdit ==0 || list.canEdit == uid}">
				<tr bgcolor="#FFF7FB"
					class="${(list.isEdited =='1' || list.isEdited =='2') ? 'tr_edited':''}">
					<!-- 索引 -->
					<td><input type="checkbox" id="checkpid_${list.pid}"
						class="checkpid" style="height: 16px; width: 16px;"
						value="${list.pid}" onclick="showBkColor(this)">
			</c:if>

			</td>
			<!-- 产品id -->
			<td id="user_${index.index}">${list.pid}</td>
			<!-- 图片 -->
			<td id="order_${index.index}"><img alt="无图" src="${list.img}"
				width="200px" height="200px"></td>
			<!-- 名称 -->
			<td id="user_${index.index}"><a target="_blank"
				href="https://detail.1688.com/offer/${list.pid}.html">${list.name}</a></td>
			<td>关键词:&nbsp;&nbsp;&nbsp;&nbsp;${list.keyword} <br>类别id:&nbsp;&nbsp;&nbsp;&nbsp;${list.catid}<br>
				<c:if test="${list.canEdit !=0 && list.canEdit != uid}">
					<textarea id="import_${list.pid}" disabled="disabled"
						onchange="changeAfterEdit('${list.pid}',this)"
						style="min-height: 160px; width: 98%; font-size: 16px;">${list.enname}</textarea>
				</c:if> <c:if test="${list.canEdit ==0 || list.canEdit == uid}">
					<textarea id="import_${list.pid}"
						onchange="changeAfterEdit('${list.pid}',this)"
						style="min-height: 160px; width: 98%; font-size: 16px;">${list.enname}</textarea>
				</c:if>

			</td>
			<td style="width: 230px;"><a class="a_style" target="_blank"
				href="${list.aliGoodsUrl}">${list.aliGoodsName}</a></td>
			<td><img alt="无图" src="${list.aliGoodsImgUrl}" width="200px"
				height="200px" style="float: right;"></td>
			<!-- 发布状态时间  -->
			<td id="state_${index.index}" style="font-size: 14px;"><c:if
					test="${list.isEdited > 0}">
					<br>
					<span>商品编辑:${list.isEdited == 1 ? '标题已编辑':'详情已编辑'}</span>
				</c:if> <c:if test="${list.isAbnormal > 0}">
					<br>
					<span>数据状态:${list.abnormalValue}</span>
				</c:if> <c:if test="${list.isBenchmark > 0}">
					<br>
					<span>货源对标情况 :${list.benchmarkValue}</span>
				</c:if> <c:if test="${list.bmFlag >0}">
					<br>
					<span>人为对标货源:${list.bmFlag ==1 ? '是':'不是'}</span>
				</c:if> <c:if test="${list.sourceProFlag >0}">
					<br>
					<span>货源属性:${list.sourceProValue}</span>
				</c:if> <c:if test="${list.soldFlag >0}">
					<br>
					<span>是否售卖:${list.soldFlag ==1 ? '卖过':'没有卖过'}</span>
				</c:if> <c:if test="${list.addCarFlag >0}">
					<br>
					<span>是否加入购物车:${list.carValue}</span>
				</c:if> <c:if test="${list.priorityFlag >0}">
					<br>
					<span>商品优先级:${list.priorityFlag ==1 ? '核心':'非核心'}</span>
				</c:if> <c:if test="${list.sourceUsedFlag >0}">
					<br>
					<span>货源可用度:${list.sourceUsedFlag ==1 ? '可用':'不可用'}</span>
				</c:if> <c:if test="${list.ocrMatchFlag >0}">
					<br>
					<span>OCR判断:${list.ocrMatchValue}</span>
				</c:if> <c:if test="${list.rebidFlag >0}">
					<br>
					<span>是否重新对标:是</span>
				</c:if> <c:if test="${list.goodsState >0}">
					<br>
					<span>发布状态:${list.goodsStateValue}</span>
				</c:if> <c:if test="${not empty list.updatetime}">
					<br>
					<span>更新时间:${list.updatetime}</span>
				</c:if></td>

			<!-- 操作 -->
			<td id="order_${index.index}" style="text-align: center;"><br>
				<a target="_blank"
				href="/cbtconsole/productEdit/detalisEdit?pid=${list.pid}">编辑详情</a> <br>
				<!-- 最近发布人  --> <span
				id="admin_${index.index}" style="text-align: center;">
					${list.admin} 
			</span></td>
			</tr>

		</c:forEach>

	</table>
	<br>
	<div>
		<input type="hidden" id="totalpage" value="${totalpage}"> 总条数:<span>${totalNum}</span>条&nbsp;&nbsp;
		分页数:<span>${pagingNum}</span>条/页&nbsp;&nbsp;&nbsp;&nbsp; 总页数:<span
			id="pagetotal">${currentpage}<em>/</em> ${totalpage}
		</span> 页&nbsp;&nbsp; <input type="button" value="上一页" onclick="fnjump(-1)"
			class="btn">&nbsp; <input type="button" value="下一页"
			onclick="fnjump(1)" class="btn"> &nbsp;第<input id="page"
			type="text" value="${currentpage}" style="height: 26px;"> <input
			type="button" value="查询" onclick="fnjump(0)" class="btn">
	</div>


</body>
</html>