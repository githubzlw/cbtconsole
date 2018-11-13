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
<title>新品云产品编辑发布管理</title>
<style type="text/css">
body {
	width: 1920px;
	height: 900px;
	margin: 0 auto;
	overflow-y: "hidden";
}

.body {
	width: 1910px;
	height: 900px;
	overflow-y: "hidden";
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
	background-color: #d4d3d3;
	!
	import
}

.tr_isEdited {
	background-color: #45f959;
}
</style>
<script type="text/javascript">

	var sessionStorage = window.sessionStorage;
	var queryParams = {
			"valid":"", "page":"1", "catid":"0", 
			"sttime":"", "edtime":"", "adminid":"0",
			"isEdited":"-1"
	};
	var isQuery =0;
	
	$(function() {
		var page = sessionStorage.getItem("page");
		if(!(page == null || page == "" || page == "0")){
			queryParams.page = page;
		}
		var catid = sessionStorage.getItem("catid");
		if(!(catid == null || catid == "" || catid == "0")){
			queryParams.catid = catid;
		}
		var valid = sessionStorage.getItem("valid");
		if(!(valid == null || valid == "")){
			queryParams.valid = valid;
			$("#query_valid").val(valid);
		}
		var sttime = sessionStorage.getItem("sttime");
		if(!(sttime == null || sttime == "")){
			queryParams.sttime = sttime;
			$("#query_sttime").val(sttime);
		}	
		var edtime = sessionStorage.getItem("edtime");
		if(!(edtime == null || edtime == "")){
			queryParams.edtime = edtime;
			$("#query_edtime").val(edtime);
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
		
		createCateroryTree(queryParams.catid);
		doQueryList();
	});
	
	function createCateroryTree(nodeId){	
		sessionStorage.setItem("catid", nodeId);
		$('.easyui-tree').tree({
			url : "/cbtconsole/newcloud/queryCategoryTree",
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
		var sttime = $("#query_sttime").val();
		var edtime = $("#query_edtime").val();
		var isEdited =$("#query_is_edited").val();
		var valid = $("#query_valid").val();
		
		queryParams.catid = "0";
		queryParams.page = "1";
		queryParams.adminid = adminid;
		queryParams.valid = valid;
		queryParams.sttime = sttime;
		queryParams.edtime = edtime;
		queryParams.isEdited = isEdited;
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
			sessionStorage.setItem("valid", queryParams.valid); 
			sessionStorage.setItem("sttime", queryParams.sttime); 
			sessionStorage.setItem("edtime", queryParams.edtime); 
			sessionStorage.setItem("adminid", queryParams.adminid); 
			sessionStorage.setItem("isEdited", queryParams.isEdited); 
			
			$('#goods_list').empty();
			var url = "/cbtconsole/newcloud/clist?page=" + queryParams.page + "&catid=" + queryParams.catid 
			+ "&valid=" + queryParams.valid + "&sttime=" + queryParams.sttime + "&edtime=" 
			+ queryParams.edtime + "&adminid=" + queryParams.adminid+ "&isEdited=" + queryParams.isEdited;
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
	<%String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}%>
		var ipStr = "<%=ip%>";
		var opUrl = "";
		if (ipStr == "192.168.1.29") {
			opUrl = "http://192.168.1.29:9955/pap/translation.jsp";
		} else if (ipStr == "192.168.1.27") {
			opUrl = "http://192.168.1.27:8083/pap/translation.jsp";
		} else if (ipStr == "27.115.38.42") {
			opUrl = "http://27.115.38.42:8083/pap/translation.jsp";
		} else {
			opUrl = "http://192.168.1.27:8083/pap/translation.jsp";
		}
		window.open(opUrl);
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
				</tr>
				<tr>
					<td>编辑人: <select id="query_admid"
						style="font-size: 18px; height: 28px; width: 120px; color: #6a6aff; font-weight: bold;">
							<option value="0" selected="selected">ALL</option>
							<c:forEach items="${sellAdm}" var="sell">
								<option value="${sell.id}">${sell.confirmusername}</option>
							</c:forEach>
					</select></td>
					<td>时间: <input id="query_sttime" class="Wdate"
						style="width: 110px; height: 26px" type="text"
						value="${param.sttime}"
						onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})" />
						<span>&nbsp;-&nbsp;</span><input id="query_edtime" class="Wdate"
						style="width: 110px; height: 26px;" type="text"
						value="${param.edtime}"
						onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})" />
					</td>
					
					<td>上架状态:<select id="query_valid"
						style="font-size: 18px; height: 28px;">
							<option value="-1" selected="selected">所有</option>
							<option value="0">下架</option>
							<option value="1">上架</option>

					</select></td>
					
					<td>编辑状态:<select id="query_is_edited"
						style="font-size: 18px; height: 28px;">
							<option value="-1" selected="selected">所有</option>
							<option value="0">未编辑</option>
							<option value="1">仅标题已编辑</option>
							<option value="2">详情已编辑</option>
					</select></td>

				</tr>
				<tr>

					<td><input type="button" onclick="doQueryWidthJump()"
						value="查询" style="height: 30px; width: 60px;" class="btn" /></td>

				</tr>
			</table>


			<div class="div_left">
				<div class="easyui-panel" style="padding: 5px; height: 100%">
					<ul class="easyui-tree">
					</ul>
				</div>
			</div>


			<div class="div_right">
				<iframe id="goods_list" src="/cbtconsole/newcloud/clist" height="100%"
					width="100%"> </iframe>

			</div>
		</div>
	</c:if>
	<c:if test="${uid ==0}">
		{"status":false,"message":"请重新登录进行操作"}
	</c:if>
</body>
</html>