<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<title>产品编辑发布管理</title>
<style type="text/css">
/*body {
	width: 1920px;
	height: 900px;
	margin: 0 auto;
	overflow-y: "hidden";
}*/

.body {
	width: 100%;
	height: 100%;
	overflow-y: hidden;
	overflow-x: hidden;
}

select{
	width: 125px;
}

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

.align_center {
	width: 1900px;
	height: 850px;
}

.div_left {
	float: left;
	width: 350px;
	height: 100%;
}

.div_right {
	float: left;
	width: 1550px;
	height: 100%;
}

.td_style {
	background-color: #880c0c;
}

.tr_disable {
	background-color: #d4d3d3 !important;
}

.tr_isEdited {
	background-color: #45f959;
}
</style>
<script type="text/javascript">

	var sessionStorage = window.sessionStorage;
	var queryParams = {
			"state":"0", "page":"1", "catid":"0","sttime":"", "edtime":"", "adminid":"0",
			"isEdited":"-1", "isAbnormal":"-1", "isBenchmark":"-1","weightCheck":"-1",
			"bmFlag":"0",  "sourceProFlag":"0", "priorityFlag":"0","soldFlag":"0",
			"addCarFlag":"0","sourceUsedFlag":"-1", "ocrMatchFlag":"0", "infringingFlag":"-1",
			"aliWeightBegin":"","aliWeightEnd":"","onlineTime":"","offlineTime":"","editBeginTime":"","editEndTime":"",
			"weight1688Begin":"","weight1688End":"","price1688Begin":"","price1688End":"","isSort":"0"
	};
	var isQuery =0;

	$(function() {
		var state = sessionStorage.getItem("state");
		if(!(state == null || state == "" || state == "0")){
			queryParams.state = state;
			$("#query_state").val(state);
		}
		var page = sessionStorage.getItem("page");
		if(!(page == null || page == "" || page == "0")){
			queryParams.page = page;
		}
		var catid = sessionStorage.getItem("catid");
		if(!(catid == null || catid == "" || catid == "0")){
			queryParams.catid = catid;
		}
		var sttime = sessionStorage.getItem("sttime");
		if(!(sttime == null || sttime == "")){
			queryParams.sttime = sttime;
			$("#query_publish_begin_time").val(sttime);
		}
		var edtime = sessionStorage.getItem("edtime");
		if(!(edtime == null || edtime == "")){
			queryParams.edtime = edtime;
			$("#query_publish_end_time").val(edtime);
		}
		var adminid = sessionStorage.getItem("adminid");
		if(!(adminid == null || adminid == "" || adminid == "0")){
			queryParams.adminid = adminid;
			$("#query_admid").val(adminid);
		}
		var isEdited = sessionStorage.getItem("isEdited");
		if(!(isEdited == null || isEdited == "" || isEdited == "-1")){
			queryParams.isEdited = isEdited;
			$("#query_is_edited").val(isEdited);
		}
		var isAbnormal = sessionStorage.getItem("isAbnormal");
		if(!(isAbnormal == null || isAbnormal == "" || isAbnormal == "-1")){
			queryParams.isAbnormal = isAbnormal;
			$("#query_is_abnormal").val(isAbnormal);
		}
		var isBenchmark = sessionStorage.getItem("isBenchmark");
		if(!(isBenchmark == null || isBenchmark == "" || isBenchmark == "-1")){
			queryParams.isBenchmark = isBenchmark;
			$("#query_is_benchmark").val(isBenchmark);
		}
		var weightCheck = sessionStorage.getItem("weightCheck");
		if(!(weightCheck == null || weightCheck == "" || weightCheck == "-1")){
			queryParams.weightCheck = weightCheck;
			$("#query_weight_check").val(weightCheck);
		}
		var bmFlag = sessionStorage.getItem("bmFlag");
		if(!(bmFlag == null || bmFlag == "" || bmFlag == "0")){
			queryParams.bmFlag = bmFlag;
			$("#query_bm_flag").val(bmFlag);
		}
		var sourceProFlag = sessionStorage.getItem("sourceProFlag");
		if(!(sourceProFlag == null || sourceProFlag == "" || sourceProFlag == "0")){
			queryParams.sourceProFlag = sourceProFlag;
			$("#query_sourcePro_flag").val(sourceProFlag);
		}
		var priorityFlag = sessionStorage.getItem("priorityFlag");
		if(!(priorityFlag == null || priorityFlag == "" || priorityFlag == "0")){
			queryParams.priorityFlag = priorityFlag;
			$("#query_priority_flag").val(priorityFlag);
		}
		var addCarFlag = sessionStorage.getItem("addCarFlag");
		if(!(addCarFlag == null || addCarFlag == "" || addCarFlag == "0")){
			queryParams.addCarFlag = addCarFlag;
			$("#query_add_car_flag").val(addCarFlag);
		}
		var sourceUsedFlag = sessionStorage.getItem("sourceUsedFlag");
		if(!(sourceUsedFlag == null || sourceUsedFlag == "" || sourceUsedFlag == "-1")){
			queryParams.sourceUsedFlag = sourceUsedFlag;
			$("#query_source_used_flag").val(sourceUsedFlag);
		}
		var ocrMatchFlag = sessionStorage.getItem("ocrMatchFlag");
		if(!(ocrMatchFlag == null || ocrMatchFlag == "" || ocrMatchFlag == "0")){
			queryParams.ocrMatchFlag = ocrMatchFlag;
			$("#query_ocr_match_flag").val(ocrMatchFlag);
		}
		var soldFlag = sessionStorage.getItem("soldFlag");
		if(!(soldFlag == null || soldFlag == "" || soldFlag == "0")){
			queryParams.soldFlag = soldFlag;
			$("#query_sold_flag").val(soldFlag);
		}
        var infringingFlag = sessionStorage.getItem("infringingFlag");
        if(!(infringingFlag == null || infringingFlag == "" || infringingFlag == "-1")){
            queryParams.infringingFlag = infringingFlag;
            $("#query_infringing_flag").val(infringingFlag);
        }

        var aliWeightBegin = sessionStorage.getItem("aliWeightBegin");
        if(!(aliWeightBegin == null || aliWeightBegin == "")){
            queryParams.aliWeightBegin = aliWeightBegin;
            $("#query_ali_weight_begin").val(aliWeightBegin);
        }
        var aliWeightEnd = sessionStorage.getItem("aliWeightEnd");
        if(!(aliWeightEnd == null || aliWeightEnd == "")){
            queryParams.aliWeightEnd = aliWeightEnd;
            $("#query_ali_weight_end").val(aliWeightEnd);
        }
        var onlineTime = sessionStorage.getItem("onlineTime");
        if(!(onlineTime == null || onlineTime == "")){
            queryParams.onlineTime = onlineTime;
            $("#query_online_time").val(onlineTime);
        }
        var offlineTime = sessionStorage.getItem("offlineTime");
        if(!(offlineTime == null || offlineTime == "")){
            queryParams.offlineTime = offlineTime;
            $("#query_offline_time").val(offlineTime);
        }
        var editBeginTime = sessionStorage.getItem("editBeginTime");
        if(!(editBeginTime == null || editBeginTime == "")){
            queryParams.editBeginTime = editBeginTime;
            $("#query_edit_begin_time").val(editBeginTime);
        }
        var editEndTime = sessionStorage.getItem("editEndTime");
        if(!(editEndTime == null || editEndTime == "")){
            queryParams.editEndTime = editEndTime;
            $("#query_edit_end_time").val(editEndTime);
        }
        var weight1688Begin = sessionStorage.getItem("weight1688Begin");
        if(!(weight1688Begin == null || weight1688Begin == "")){
            queryParams.weight1688Begin = weight1688Begin;
            $("#query_1688_weight_begin").val(weight1688Begin);
        }
        var weight1688End = sessionStorage.getItem("weight1688End");
        if(!(weight1688End == null || weight1688End == "")){
            queryParams.weight1688End = weight1688End;
            $("#query_1688_weight_end").val(weight1688End);
        }
		var price1688Begin = sessionStorage.getItem("price1688Begin");
        if(!(price1688Begin == null || price1688Begin == "")){
            queryParams.price1688Begin = price1688Begin;
            $("#query_1688_price_begin").val(price1688Begin);
        }
        var price1688End = sessionStorage.getItem("price1688End");
        if(!(price1688End == null || price1688End == "")){
            queryParams.price1688End = price1688End;
            $("#query_1688_price_end").val(price1688End);
        }
        var isSort = sessionStorage.getItem("isSort");
        if(!(isSort == null || isSort == "")){
            queryParams.isSort = isSort;
            $("#query_is_sort").val(isSort);
        }
        var isComplain = sessionStorage.getItem("isComplain");
        if(!(isComplain == null || isComplain == "" || isComplain != '1')){
            queryParams.isComplain = isComplain;
            $("#is_complain").attr("checked",'true');//全选 
        }

		createCateroryTree(queryParams.catid);
		doQueryList();
        doStatistic();
	});

	function createCateroryTree(nodeId){
		sessionStorage.setItem("catid", nodeId);
		$('.easyui-tree').tree({
			url : "/cbtconsole/cutom/queryCategoryTree",
			animate:true,
			lines:true,
			method : "post",
			queryParams: queryParams,
		    onClick: function(node){
		    	queryParams.catid = node.id;
		    	queryParams.page = 1;
		    	doQueryList();
		    },
		    onBeforeExpand: function(node){
		    	if(node.children.length ==0){
		    		showMessage('当前节点没有子节点');
		    		return false;
                }
		    },
		    onLoadSuccess:function(node,data){
		    	isQuery = 0;
		    	$('.easyui-tree').find('.tree-node-selected').removeClass('tree-node-selected');
		    	if(data.length > 0){
		    		if(nodeId==null || nodeId=="" || nodeId=="0"){
		    			var nd = $('.easyui-tree').tree('find', data[0].id);
			            $('.easyui-tree').tree('select', nd.target);
		    		}else{
		    			var clNode = $('.easyui-tree').tree('find', nodeId);
		    			if(clNode){
		    				$('.easyui-tree').tree('select', clNode.target);
		    			}
		    		}
		    	}
		    	$(".easyui-tree").show();
		    }
		});
	}

	function doQueryWidthJump(){
		var adminid = $("#query_admid").val();
		var state = $("#query_state").val();
		var sttime = $("#query_publish_begin_time").val();
		var edtime = $("#query_publish_end_time").val();
		var isEdited =$("#query_is_edited").val();
		var isAbnormal = $("#query_is_abnormal").val();
		var isBenchmark = $("#query_is_benchmark").val();
		//var weightCheck = $("#query_weight_check").val();
		var weightCheck = -1;
		var bmFlag = $("#query_bm_flag").val();
		var sourceProFlag = $("#query_sourcePro_flag").val();
		var priorityFlag = $("#query_priority_flag").val();
		var addCarFlag = $("#query_add_car_flag").val();
		//var sourceUsedFlag = $("#query_source_used_flag").val();
		var sourceUsedFlag = -1;
		//var ocrMatchFlag = $("#query_ocr_match_flag").val();
		var ocrMatchFlag = 0;
		var soldFlag = $("#query_sold_flag").val();
		var infringingFlag = $("#query_infringing_flag").val();

		var aliWeightBegin = $("#query_ali_weight_begin").val();
		var aliWeightEnd = $("#query_ali_weight_end").val();
		var onlineTime = $("#query_online_time").val();
		var offlineTime = $("#query_offline_time").val();
		var editBeginTime = $("#query_edit_begin_time").val();
		var editEndTime = $("#query_edit_end_time").val();
		var weight1688Begin = $("#query_1688_weight_begin").val();
		var weight1688End = $("#query_1688_weight_end").val();
		var price1688Begin = $("#query_1688_price_begin").val();
		var price1688End = $("#query_1688_price_end").val();
		var isSort = $("#query_is_sort").val();
		var isComplain = $("#is_complain").is(":checked")?"1":"0";

		queryParams.catid = "0";
		queryParams.page = "1";
		queryParams.adminid = adminid;
		queryParams.state = state;
		queryParams.sttime = sttime;
		queryParams.edtime = edtime;
		queryParams.isAbnormal = isAbnormal;
		queryParams.isEdited = isEdited;
		queryParams.isBenchmark = isBenchmark;
		queryParams.weightCheck = weightCheck;
		queryParams.bmFlag = bmFlag;
		queryParams.sourceProFlag = sourceProFlag;
		queryParams.priorityFlag = priorityFlag;
		queryParams.addCarFlag = addCarFlag;
		queryParams.sourceUsedFlag = sourceUsedFlag;
		queryParams.ocrMatchFlag = ocrMatchFlag;
		queryParams.soldFlag = soldFlag;
        queryParams.infringingFlag = infringingFlag;

        queryParams.aliWeightBegin = aliWeightBegin;
        queryParams.aliWeightEnd = aliWeightEnd;
        queryParams.onlineTime = onlineTime;
        queryParams.offlineTime = offlineTime;
        queryParams.editBeginTime = editBeginTime;
        queryParams.editEndTime = editEndTime;
        queryParams.weight1688Begin = weight1688Begin;
        queryParams.weight1688End = weight1688End;
        queryParams.price1688Begin = price1688Begin;
        queryParams.price1688End = price1688End;
        queryParams.isSort = isSort;
        queryParams.isComplain = isComplain;
		$(".easyui-tree").hide();
		createCateroryTree(queryParams.catid);
		doQueryList();
	}

	function parentDoQuery(page){
		queryParams.page=page;
		doQueryList();
	}

	function doQueryList(){

		if(isQuery ==0){
			sessionStorage.setItem("catid", queryParams.catid);
			sessionStorage.setItem("page", queryParams.page);
			sessionStorage.setItem("state", queryParams.state);
			sessionStorage.setItem("sttime", queryParams.sttime);
			sessionStorage.setItem("edtime", queryParams.edtime);
			sessionStorage.setItem("adminid", queryParams.adminid);
			sessionStorage.setItem("isEdited", queryParams.isEdited);
			sessionStorage.setItem("isBenchmark", queryParams.isBenchmark);
			sessionStorage.setItem("weightCheck", queryParams.weightCheck);
			sessionStorage.setItem("isAbnormal", queryParams.isAbnormal);
			sessionStorage.setItem("bmFlag", queryParams.bmFlag);
			sessionStorage.setItem("sourceProFlag", queryParams.sourceProFlag);
			sessionStorage.setItem("priorityFlag", queryParams.priorityFlag);
			sessionStorage.setItem("addCarFlag", queryParams.addCarFlag);
			sessionStorage.setItem("sourceUsedFlag", queryParams.sourceUsedFlag);
			sessionStorage.setItem("ocrMatchFlag", queryParams.ocrMatchFlag);
			sessionStorage.setItem("soldFlag", queryParams.soldFlag);
            sessionStorage.setItem("infringingFlag", queryParams.infringingFlag);

            sessionStorage.setItem("aliWeightBegin", queryParams.aliWeightBegin);
            sessionStorage.setItem("aliWeightEnd", queryParams.aliWeightEnd);
            sessionStorage.setItem("onlineTime", queryParams.onlineTime);
            sessionStorage.setItem("offlineTime", queryParams.offlineTime);
            sessionStorage.setItem("editBeginTime", queryParams.editBeginTime);
            sessionStorage.setItem("editEndTime", queryParams.editEndTime);
            sessionStorage.setItem("weight1688Begin", queryParams.weight1688Begin);
            sessionStorage.setItem("weight1688End", queryParams.weight1688End);
            sessionStorage.setItem("price1688Begin", queryParams.price1688Begin);
            sessionStorage.setItem("price1688End", queryParams.price1688End);
            sessionStorage.setItem("isSort", queryParams.isSort);
            sessionStorage.setItem("isComplain", queryParams.isComplain);

			$('#goods_list').empty();
			var url = "/cbtconsole/cutom/clist?page=" + queryParams.page + "&catid=" + queryParams.catid
			+ "&state=" + queryParams.state + "&sttime=" + queryParams.sttime + "&edtime="
			+ queryParams.edtime + "&adminid=" + queryParams.adminid+ "&isBenchmark=" + queryParams.isBenchmark
			+ "&weightCheck=" + queryParams.weightCheck+ "&isEdited=" + queryParams.isEdited + "&isAbnormal=" + queryParams.isAbnormal
			+ "&bmFlag=" + queryParams.bmFlag + "&sourceProFlag=" + queryParams.sourceProFlag + "&soldFlag=" + queryParams.soldFlag
			+ "&priorityFlag=" + queryParams.priorityFlag + "&addCarFlag=" + queryParams.addCarFlag + "&sourceUsedFlag=" + queryParams.sourceUsedFlag
            + "&infringingFlag=" + queryParams.infringingFlag + "&aliWeightBegin=" + queryParams.aliWeightBegin + "&aliWeightEnd=" + queryParams.aliWeightEnd
			+ "&onlineTime=" + queryParams.onlineTime + "&offlineTime=" + queryParams.offlineTime
			+ "&editBeginTime=" + queryParams.editBeginTime + "&editEndTime=" + queryParams.editEndTime + "&weight1688Begin="
			+ queryParams.weight1688Begin + "&weight1688End=" + queryParams.weight1688End + "&price1688Begin=" + queryParams.price1688Begin
			+ "&price1688End=" + queryParams.price1688End + "&isSort=" + queryParams.isSort+"&isComplain="+queryParams.isComplain;

			$('#goods_list').attr('src',url);
		}
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

	function reloadHtml(){
		window.location.reload();
	}

	function jumpToTranslation() {
		var ipStr = location.href;
		var opUrl = "";
		if (ipStr.indexOf("192.168.1.29") != -1) {
			opUrl = "http://192.168.1.29:9955/pap/translation.jsp";
		} else if (ipStr.indexOf("192.168.1.27") != -1) {
			opUrl = "http://192.168.1.27:9089/pap/translation.jsp";
		} else if (ipStr.indexOf("27.115.38.42") != -1) {
			opUrl = "http://27.115.38.42:9089/pap/translation.jsp";
		} else {
			opUrl = "http://192.168.1.27:9089/pap/translation.jsp";
		}
		window.open(opUrl);
	}

	function doStatistic() {
        $.ajax({
            async : true,
            type : 'POST',
            dataType : 'text',
            url : '/cbtconsole/cutom/onlineGoodsStatistic',
            data : {},
            success : function(data) {
                var json = eval('(' +data+ ')');
                if (json.ok) {
                    var jsonDt = json.data;
                    $("#online_total").text(jsonDt.onlineTotal);
                    $("#input_total").text(jsonDt.typeinTotal);
                    $("#edit_total").text(jsonDt.isEditTotal);
                }
            }
        });
    }
</script>

</head>
<body class="body">

	<c:if test="${uid > 0}">
		<div class="align_center">
			<table style="width: 90%;">
				<tr>
					<td><span
						style="margin-right: 15px; font-size: 16px; font-weight: bold;">当前操作人:<span
							id="admName">${admName}</span></span></td>

					<td>发布状态:<select id="query_state"
						style="font-size: 18px; height: 28px;">
							<option value="0" selected="selected">所有</option>
							<%--<option value="1">产品发布中</option>--%>
							<option value="4">发布成功</option>
							<option value="2">产品下架</option>
							<option value="3">发布失败</option>
							<option value="5">产品待发布</option>
							<option value="6">产品软下架</option>
					</select></td>

					<td>编辑状态:<select id="query_is_edited"
						style="font-size: 18px; height: 28px;">
							<option value="-1" selected="selected">所有</option>
							<option value="0">未编辑</option>
							<option value="1">已编辑</option>
					</select></td>

					<td>编辑人: <select id="query_admid"
						style="font-size: 18px; height: 28px; width: 120px; color: #6a6aff; font-weight: bold;">
							<option value="0" selected="selected">ALL</option>
							<c:forEach items="${sellAdm}" var="sell">
								<option value="${sell.id}">${sell.confirmusername}</option>
							</c:forEach>
					</select></td>
					<td>精选和侵权:<select id="query_infringing_flag"
									 style="font-size: 18px; height: 28px;">
						<option value="-1" selected="selected">所有</option>
						<option value="0">未侵权</option>
						<option value="1">侵权</option>
						<option value="15">8月人为精选</option>
					</select></td>
					<td colspan="2"><p style="background-color: #e6dba1;">在线商品统计【
						在线数:<span style="color: red" id="online_total">0</span>
						/ 当前用户录入数:<span style="color: red" id="input_total">0</span>
						/ 当前用户编辑数:<span style="color: red" id="edit_total">0</span>】</p>
					</td>
				</tr>
				<tr>

					<td>货源对标情况:<select id="query_is_benchmark"
						style="font-size: 18px; height: 28px;">
							<option value="-1" selected="selected">请选择</option>
							<option value="0">没找到对标</option>
							<option value="1">精确对标</option>
							<option value="2">近似对标</option>
					</select></td>

					<%--<td>重量检查:<select id="query_weight_check"
						style="font-size: 18px; height: 28px;">
							<option value="-1" selected="selected">请选择</option>
							<option value="0">重量正常</option>
							<option value="10">比类别平均重量高30%且运费占总价格超35%</option>
							<option value="15">比类别平均重量低40%</option>
							<option value="4">重量数据为空</option>
							<option value="5">运费占总免邮价格 60%以上</option>
					</select></td>--%>


					<td>核心商品:<select id="query_priority_flag"
						style="font-size: 18px; height: 28px;">
							<option value="0" selected="selected">请选择</option>
							<option value="1">核心</option>
							<option value="2">非核心</option>
					</select></td>

					<td>货源属性:<select id="query_sourcePro_flag"
						style="font-size: 18px; height: 28px;">
							<option value="0" selected="selected">请选择</option>
							<option value="1">同店铺商品</option>
							<option value="2">同款商品</option>
							<option value="3">对标商品</option>
							<option value="4">原始老数据</option>
					</select></td>

					<td>发布时间:<input id="query_publish_begin_time" class="Wdate"
						style="width: 95px; height: 26px" type="text" value="${param.sttime}"
						onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})" />
						<span>-</span><input id="query_publish_end_time" class="Wdate"
						style="width: 95px; height: 26px;" type="text"
						value="${param.edtime}"
						onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})" />
					</td>

					<td>速卖通重量:<input id="query_ali_weight_begin" type="number" step="0.01" style="width: 50px;height: 22px;"/>
						<span>-</span>
						<input id="query_ali_weight_end" type="number" step="0.01" style="width: 50px;height: 22px;"/></td>
					<td>
						新上架时间晚于:<input id="query_online_time" class="Wdate"
						style="width: 95px; height: 26px" type="text" value="${param.sttime}"
						onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})" />
					</td>
					<td>
						新下架时间晚于:<input id="query_offline_time" class="Wdate"
						style="width: 95px; height: 26px" type="text" value="${param.sttime}"
						onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})" />
				</tr>
				<tr>
					<td>人为对标货源:<select id="query_bm_flag"
						style="font-size: 18px; height: 28px;">
							<option value="0" selected="selected">请选择</option>
							<option value="1">是</option>
							<option value="2">否</option>
					</select></td>


					<td style="display:none">是否加入购物车:<select id="query_add_car_flag"
						style="font-size: 18px; height: 28px;">
							<option value="0" selected="selected">请选择</option>
							<option value="1">已加入购物车</option>
							<option value="2">未加入购物车</option>
							<option value="3">加入后删除</option>
					</select></td>


					<td>是否售卖:<select id="query_sold_flag"
						style="font-size: 18px; height: 28px;">
							<option value="0" selected="selected">请选择</option>
							<option value="1">卖过</option>
							<option value="2">没有卖过</option>
					</select></td>


					<%--<td>OCR判断:<select id="query_ocr_match_flag"
						style="font-size: 18px; height: 28px;">
							<option value="0" selected="selected">请选择</option>
							<option value="1">人工OCR判断(详情)</option>
							<option value="2">未判断</option>
							<option value="3">未判断(全禁)</option>
							<option value="4">未判断(全免)</option>
							<option value="5">无中文</option>
							<option value="6">有中文，不删除</option>
							<option value="7">有中文，部分被删</option>
							<option value="8">没图</option>
					</select></td>--%>



					<%--<td>OCR货源可用度:<select id="query_source_used_flag"
						style="font-size: 18px; height: 28px;">
							<option value="-1" selected="selected">请选择</option>
							<option value="0">不可用</option>
							<option value="1">可用</option>
							<option value="2">描述很精彩 </option>
					</select></td>--%>
					<td>数据状态:<select id="query_is_abnormal"
						style="font-size: 18px; height: 28px;">
							<option value="-1" selected="selected">请选择</option>
							<option value="1">类别错误</option>
							<option value="2">商品太贵</option>
							<option value="3">太便宜</option>
							<option value="0">正常数据</option>
					</select></td>

					<td>修改时间:<input id="query_edit_begin_time" class="Wdate"
						style="width: 95px; height: 26px" type="text" value=""
						onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})" />
						<span>-</span>
						<input id="query_edit_end_time" class="Wdate"
						style="width: 95px; height: 26px" type="text" value=""
						onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})" />

					</td>
					<td>1688&nbsp;&nbsp;重量:<input id="query_1688_weight_begin" type="number" step="0.01" style="width: 50px;height: 22px;"/>
						<span>-</span>
						<input id="query_1688_weight_end" type="number" step="0.01" style="width: 50px;height: 22px;"/></td>
					<td>1688价格:<input id="query_1688_price_begin" type="number" step="0.01" style="width: 50px;height: 22px;"/>
						<span>-</span>
						<input id="query_1688_price_end" type="number" step="0.01" style="width: 50px;height: 22px;"/></td>
					<td>排序:<select id="query_is_sort"
						style="font-size: 18px; height: 28px;">
							<option value="0" selected="selected">请选择</option>
							<option value="1">搜索次数倒排序</option>
							<option value="2">点击次数倒排序</option>
							<%--<option value="3">已点击商品倒排序</option>--%>
							<option value="4">按照类别排序</option>
					</select><br>&nbsp;&nbsp;<input type="checkbox" id="is_complain">是否被投诉&nbsp;&nbsp;<input type="button" onclick="doQueryWidthJump()"
						value="查询" style="height: 30px; width: 60px;" class="btn" />
						&nbsp;&nbsp;<input type="button" onclick="jumpToTranslation()"
						value="翻译词典管理" style="height: 30px; width: 90px;" class="btn" /></td>

				</tr>
			</table>


			<div class="div_left">
				<div class="easyui-panel" style="padding: 5px; height: 96%">
					<ul class="easyui-tree">
					</ul>
				</div>
			</div>


			<div class="div_right">
				<iframe id="goods_list" src="/cbtconsole/cutom/clist" height="96%"
					width="100%"> </iframe>

			</div>
		</div>
	</c:if>
	<c:if test="${uid ==0}">
		{"status":false,"message":"请重新登录进行操作"}
	</c:if>
</body>
</html>