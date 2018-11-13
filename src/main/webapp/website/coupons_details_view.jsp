<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>优惠券详情明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet"
	href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript">
	var searchReport = "/cbtconsole/StatisticalReport/searchCoupusDetailsView"; 
</script>
<style type="text/css">
.displaynone {
	display: none;
}

.item_box {
	display: inline-block;
	margin-right: 52px;
}

.item_box select {
	width: 150px;
}

.mod_pay3 {
	width: 600px;
	position: fixed;
	top: 100px;
	left: 15%;
	z-index: 1011;
	background: gray;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 1011;
	border: 15px solid #33CCFF;
}

.w-group {
	margin-bottom: 10px;
	width: 60%;
	text-align: center;
}

.w-label {
	float: left;
}

.w-div {
	margin-left: 120px;
}

.w-remark {
	width: 100%;
}

table.imagetable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #999999;
	border-collapse: collapse;
}

table.imagetable th {
	background: #b5cfd2 url('cell-blue.jpg');
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #999999;
}

table.imagetable td {
	/* 	background:#dcddc0 url('cell-grey.jpg'); */
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #999999;
	word-break: break-all;
}

.displaynone {
	display: none;
}
</style>
<%
	String id = request.getParameter("id");
%>
<script type="text/javascript">
	$(document).ready(function() {
		var id ='<%=id%>';
		$("#subsidiary_id").val(id);
		searchExport(1);
	});

</script>

</head>
<body text="#000000" >
	<div style="margin-left:40%;margin-top:1%;width:800px;">
	<div class="easyui-panel" title="修改优惠券详情明细" style="width:100%;max-width:400px;padding:40px 40px;">
		<form id="ff" method="post" style="height:100%;">
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="batch" id="batch" readonly="readonly" style="width:100%;color: red;" data-options="label:'创建批次:'">
				<input type="hidden" name="subsidiary_id" id="subsidiary_id" />
				<script type="text/javascript">$(function () {$('#batch').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="coupons_name1" id="coupons_name1" readonly="readonly" style="width:100%" data-options="label:'优惠券名称:'">
				<script type="text/javascript">$(function () {$('#coupons_name1').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="coupons_type" id="coupons_type" readonly="readonly" style="width:100%" data-options="label:'优惠券类型:'">
				<script type="text/javascript">$(function () {$('#coupons_type').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="promo_code" id="promo_code" readonly="readonly" style="width:100%" data-options="label:'优惠码:'">
				<script type="text/javascript">$(function () {$('#promo_code').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="denomination" id="denomination" readonly="readonly" style="width:100%" data-options="label:'面额/折扣:'">
				<script type="text/javascript">$(function () {$('#denomination').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="minimum_cons" id="minimum_cons" readonly="readonly" style="width:100%" data-options="label:'最低消费:'">
				<script type="text/javascript">$(function () {$('#minimum_cons').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="state" id="state" readonly="readonly" style="width:100%" data-options="label:'状态:'">
				<script type="text/javascript">$(function () {$('#state').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="using_range" id="using_range" readonly="readonly" style="width:100%" data-options="label:'使用范围:'">
				<script type="text/javascript">$(function () {$('#using_range').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px">
				<label style="text-align: left; height: 27px; line-height: 27px;">有效期开始时间：</label><input id="startdate" style="width: 216px;margin: 0px; padding-top: 0px; padding-bottom: 0px; height: 25px; line-height: 25px;"
							name="startdate" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})"
							value="${param.startdate}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />
			</div>
			<div style="margin-bottom:20px">
				<label style="text-align: left; height: 27px; line-height: 27px;">有效期结束时间：</label><input id="enddate" style="width: 216px;margin: 0px; padding-top: 0px; padding-bottom: 0px; height: 25px; line-height: 25px;"
							name="startdate" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})"
							value="${param.startdate}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="disbursement" id="disbursement" readonly="readonly" style="width:100%" data-options="label:'获取方式:'">
				<script type="text/javascript">$(function () {$('#disbursement').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="get_time" id="get_time" readonly="readonly" style="width:100%" data-options="label:'获取日期:'">
				<script type="text/javascript">$(function () {$('#get_time').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="userid" id="userid" style="width:100%" data-options="label:'所属用户ID:'">
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="remark" id="remark" style="width:100%" data-options="label:'运营备注:'">
			</div>
		</form>
		<div style="text-align:center;padding:5px 0">
		    <a href="javascript:history.back(-1)" class="easyui-linkbutton">返回上一页</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submitForm()" style="width:80px">保存</a>
		</div>
	</div>
	</div>
	<script>
		function submitForm(){
// 			$('#ff').form('submit');
			var subsidiary_id=$("#subsidiary_id").val();
			var startdate=$("#startdate").val();
			var enddate=$("#enddate").val();
			var userid=$("#userid").val();
			var remark=$("#remark").val();
			jQuery.ajax({
				url : "/cbtconsole/StatisticalReport/addCoupusDetailsView",
				data : {
					"subsidiary_id" : subsidiary_id,
					"startdate":startdate,
					"enddate":enddate,
					"userid":userid,
					"remark":remark
				},
				type : "post",
				success : function(data) {
		           if(data>0){
		        	   $.messager.alert('提示','更新成功');
		           }else{
		        	   $.messager.alert('提示','更新失败或者用户不存在');
		           }
				},
			});
		}
	</script>
</body>
<script type="text/javascript">

function save() {
	var id=$("#id").val();
	var startdate=$("#startdate").val();
	var enddate=$("#enddate").val();
	var userid=$("#userid").val();
	var remark=$("#remark").val();
	jQuery.ajax({
		url : "/cbtconsole/StatisticalReport/addCoupusDetailsView",
		data : {
			"id" : id,
			"startdate":startdate,
			"enddate":enddate,
			"userid":userid,
			"remark":remark
		},
		type : "post",
		success : function(data) {
           if(data>0){
        	   alert("更新成功");
           }else{
        	   alert("更新失败或者用户不存在");
           }
		},
	});
	searchExport(1);
}


	function BeginCreate() {
		closeConpons();
		window.location.href = "/cbtconsole/website/coupus_details.jsp";
	}

	function closeConpons() {
		var rfddd = document.getElementById("insertInfo");
		rfddd.style.display = "none";
	}

	function addCoupons() {
		var rfddd = document.getElementById("insertInfo");
		rfddd.style.display = "block";
	}

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
		var subsidiary_id = $("#subsidiary_id").val();
		jQuery.ajax({
			url : searchReport,
			data : {
				"page" : page,
				"id" : subsidiary_id
			},
			type : "post",
			success : function(data) {
               if(data){
            	   var reportDetailList=data.data.list2;
            	   for(var i=0;i<reportDetailList.length;i++){
	                 	reportDetail = reportDetailList[i];
	                 	$('#ff').form('load',{
	            			batch:reportDetail.batch,
	            			coupons_name1:reportDetail.coupons_name,
	            			coupons_type:reportDetail.coupons_type,
	            			promo_code:reportDetail.promo_code,
	            			denomination:reportDetail.denomination,
	            			disbursement:reportDetail.disbursement,
	            			minimum_cons:reportDetail.minimum_cons,
	            			state:reportDetail.states,
	            			using_range:reportDetail.using_range,
	            			userid:reportDetail.userid,
	            			remark:reportDetail.remark,
	            		});
	               }
               }
			},
			error : function(e) {
				alert("查询失败！");
			}
		});
	}
</script>
</html>