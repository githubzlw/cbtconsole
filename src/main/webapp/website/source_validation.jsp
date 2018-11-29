<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@page import="com.cbt.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
	String admuserJson = Redis.hget(request.getSession().getId(),
			"admuser");
	Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,
			Admuser.class);
	int u_id=adm.getId();
%>
<head>
	<style type="text/css">
	</style>
	<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
	<title>支付宝采购对账</title>
	<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
	<link rel="stylesheet" href="script/style.css" type="text/css">
	<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
	<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
	<!-- <link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css"> -->
	<script type="text/javascript"
			src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript">
        var searchReport = "/cbtconsole/StatisticalReport/getSourceValidation"; //报表查询
	</script>
	<style type="text/css">
		.displaynone{display:none;}
		.item_box{display:inline-block;margin-right:52px;}
		.item_box select{width:150px;}
		.mod_pay3 { width: 600px; position: fixed;
			top: 100px; left: 15%;
			z-index: 1011; background: gray;
			padding: 5px; padding-bottom: 20px;
			z-index: 1011; border: 15px solid #33CCFF; }
		.w-group{margin-bottom: 10px;width: 60%;text-align: center;}
		.w-label{float:left;}
		.w-div{margin-left:120px;}
		.mod_pay3 {
			width: 500px;
			position: fixed;
			margin-left:10%;
			margin-top:5%;
			z-index: 1011;
			background: gray;
			padding: 5px;
			padding-bottom: 20px;
			z-index: 1011;
			border: 15px solid #33CCFF;
		}
		.w-remark{width:100%;}
		table.imagetable {
			font-family: verdana,arial,sans-serif;
			font-size:11px;
			color:#333333;
			border-width: 1px;
			border-color: #999999;
			border-collapse: collapse;
		}
		table.imagetable th {
			background:#b5cfd2 url('cell-blue.jpg');
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
		.displaynone{display:none;}
		.repalyDiv2{width: 500px;background: #34db51;text-align: center;position: fixed;left: 40%;top: 43%;}
		.loading { position: fixed; top: 0px; left: 0px;
			width: 100%; height: 100%; color:#fff; z-index:9999;
			background: #000 url(/cbtconsole/img/yuanfeihang/loaderTwo.gif) no-repeat 50% 300px;
			opacity: 0.4;}
	</style>

</head>
<script type="text/javascript">
    $(function(){
		$("#buyer").val(9);
    })
</script>
<body text="#000000">
<div id="operatediv" class="loading" style="display: none;"></div>
<div class="wrapper">
	<div class="content-wrapper">
		<form id="adduserForm" name="adduserForm" action="" method="post">
			<div class="box box-solid" >
				<div class="box-header with-border">
					<h4>查询条件</h4>
				</div>
				<div class="box-body">
					<div style="width:26%">
						<label for="nickname" >采购人：<span style="color:red">*</span></label>
						<select name="" id="buyer"  style="width: 90px;">
							<option value="80">sales6</option>
							<option value="58">buy2</option>
							<option value="57">buy1</option>
							<option value="56">salebuy1</option>
							<option value="68">salebuy4</option>
						</select>
					</div>
					<div style="width:26%">
						<label for="nickname" >实际采购和确认采购开始时间：<span style="color:red">*</span></label>
						<input id="startdate"
							   name="startdate" readonly="readonly"
							   onfocus="WdatePicker({isShowWeek:true})"
							   value="${param.startdate}"
							   onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />
					</div>
					<div style="width:26%">
						<label for="nickname" >实际采购和确认采购结束时间：<span style="color:red">*</span></label>
						<input id="enddate"
							   name="enddate" readonly="readonly"
							   onfocus="WdatePicker({isShowWeek:true})"
							   value="${param.startdate}"
							   onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />
					</div>
					<%--<div style="width:26%;" disabled="none">--%>
						<%--<label for="nickname" >采购包裹是否入库：</label>--%>
						<%--<select name="" id="isTrack" style="width: 90px;">--%>
							<%--<option value="0">全部</option>--%>
							<%--<option value="1">已入库</option>--%>
							<%--<option value="2">未入库</option>--%>
						<%--</select>--%>
					<%--</div>--%>
					<%--<div style="width:26%">--%>
						<%--<label for="nickname" >订单采购金额：<span id="amount" style="color:red;font-size:30px;"></span></label>--%>

					<%--</div>--%>
					<div style="width:26%">
						<div class="btn btn-primary pull-right" id="pgSearch" style="margin-right: 5px;">
							<i class="fa fa-search">查 询</i>
						</div>
					</div>
				</div>
				<div  style="padding:15px;" id="div1">
					<label style="background-color:red;"><a style='text-decoration:underline'>分配给采购的商品数量</a>：<span class="count">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('2');" title="请点击查看详情"><a style='text-decoration:underline'>采购实际买了多少商品</a>：<span class="count1">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('3');" title="请点击查看详情"><a style='text-decoration:underline'>实际采购不在分配采购中</a>：<span class="count2">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('4');" title="请点击查看详情"><a style='text-decoration:underline'>分配给采购的商品实际没有采购</a>：<span class="count3">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('5');" title="请点击查看详情"><a style='text-decoration:underline'>一个采购商品多个采购订单</a>：<span class="count4">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('6');" title="请点击查看详情"><a style='text-decoration:underline'>列出所有交易</a>：<span class="count5">0</span></label>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<%--<label style="background-color:red;">*红色行标记数据为确认采购商品没有匹配到采购订单商品(按照时间倒序)</label>--%>
					<table id="categroyReport" class="imagetable">
						<thead>
						<tr>
							<th width="50" style="text-align: center;">序号</th>
							<th width="180" style="text-align: center;">商品ID</th>
							<th width="180" style="text-align: center;">订单号</th>
							<%--<th width="200" style="text-align: center;">采购人</th>--%>
							<th width="180" style="text-align: center;">确认采购时间</th>
							<th width="180" style="text-align: center;">原链接图片</th>
							<th width="180" style="text-align: center;">是否实际采购</th>
							<th width="200" style="text-align: center;">采购订单号</th>
							<!-- 	                        <th width="180" style="text-align: center;">1688/TB图片</th> -->
						</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div style="text-align: center; " id="pagediv">
					共查到<span id="datacount">0</span>数据&nbsp;&nbsp;
					<input type="button" id="prePage" value="上一页"/>&nbsp;
					第<span id="nowPage">1</span>页/共<span id="allPage">0</span>页
					<input type="button" id="nextPage" value="下一页"/>&nbsp;&nbsp;
					跳至<input type="text" id="toPage" style="width:50px;"/>页 <input type="button" value="Go" id="jumpPage"/>
				</div>

				<!--  -->
				<div  style="padding:15px;display: none;" id="div2">
					<label  onclick="toDiv('1');" title="请点击查看详情"><a style='text-decoration:underline'>分配给采购的商品数量</a>：<span class="count">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label style="background-color:red;"><a style='text-decoration:underline'>实际采购了多少商品</a>：<span class="count1">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('3');" title="请点击查看详情"><a style='text-decoration:underline'>实际采购不在分配采购中</a>：<span class="count2">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('4');" title="请点击查看详情"><a style='text-decoration:underline'>分配给采购的商品实际没有采购</a>：<span class="count3">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('5');" title="请点击查看详情"><a style='text-decoration:underline'>一个采购商品多个采购订单</a>：<span class="count4">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('6');" title="请点击查看详情"><a style='text-decoration:underline'>列出所有交易</a>：<span class="count5">0</span></label>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<!--          <label style="background-color:red;">*红色行标记数据为采购商品没有匹配到确认采购商品(按照时间倒序)|蓝色标记列的为采购订单没有入库</label> -->
					<table id="categroyReport2" class="imagetable">
						<thead>
						<tr>
							<th width="50" style="text-align: center;">序号</th>
							<th width="180" style="text-align: center;">订单号</th>
							<th width="180" style="text-align: center;">来源</th>
							<th width="180" style="text-align: center;">货源链接</th>
							<th width="200" style="text-align: center;">产品名称</th>
							<th width="180" style="text-align: center;">规格</th>
							<th width="200" style="text-align: center;">快递号</th>
							<th width="200" style="text-align: center;">订单状态</th>
							<th width="200" style="text-align: center;">支付时间</th>
							<th width="200" style="text-align: center;">采购账号</th>
							<th width="200" style="text-align: center;">1688/TB图片</th>
							<th width="200" style="text-align: center;">入库时间</th>
							<th width="200" style="text-align: center;">可能匹配的销售订单</th>
							<th width="200" style="text-align: center;">原链接图片</th>
							<th width="200" style="text-align: center;">系统提示</th>
							<th width="200" style="text-align: center;">备注</th>
						</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div style="text-align: center;display: none; " id="pagediv2">
					共查到<span id="datacount2">0</span>数据&nbsp;&nbsp;
					<input type="button" id="prePage2" value="上一页"/>&nbsp;
					第<span id="nowPage2">1</span>页/共<span id="allPage2">0</span>页
					<input type="button" id="nextPage2" value="下一页"/>&nbsp;&nbsp;
					跳至<input type="text" id="toPage2" style="width:50px;"/>页 <input type="button" value="Go" id="jumpPage2"/>
				</div>
				<!-- start 实际采购不在分配采购中-->
				<div  style="padding:15px;display: none;" id="div3">
					<label  onclick="toDiv('1');" title="请点击查看详情"><a style='text-decoration:underline'>分配给采购的商品数量</a>：<span class="count">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('2');" title="请点击查看详情"><a style='text-decoration:underline'>实际采购了多少商品</a>：<span class="count1">0</span></label>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<label style="background-color:red;"><a style='text-decoration:underline'>实际采购不在分配采购中</a>：<span class="count2">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('4');" title="请点击查看详情"><a style='text-decoration:underline'>分配给采购的商品实际没有采购</a>：<span class="count3">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('5');" title="请点击查看详情"><a style='text-decoration:underline'>一个采购商品多个采购订单</a>：<span class="count4">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('6');" title="请点击查看详情"><a style='text-decoration:underline'>列出所有交易</a>：<span class="count5">0</span></label>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<!--          <label style="background-color:red;">*红色行标记数据为采购商品没有匹配到确认采购商品(按照时间倒序)|蓝色标记列的为采购订单没有入库</label> -->
					<table id="categroyReport3" class="imagetable">
						<thead>
						<tr>
							<th width="50" style="text-align: center;">序号</th>
							<th width="180" style="text-align: center;">订单号</th>
							<th width="180" style="text-align: center;">来源</th>
							<th width="180" style="text-align: center;">货源链接</th>
							<th width="200" style="text-align: center;">产品名称</th>
							<th width="180" style="text-align: center;">规格</th>
							<th width="200" style="text-align: center;">快递号</th>
							<th width="200" style="text-align: center;">订单状态</th>
							<th width="200" style="text-align: center;">支付时间</th>
							<th width="200" style="text-align: center;">采购账号</th>
							<th width="180" style="text-align: center;">交易金额</th>
							<th width="200" style="text-align: center;">1688/TB图片</th>
							<th width="200" style="text-align: center;">备注</th>
							<th width="200" style="text-align: center;">是否入库</th>
							<th width="200" style="text-align: center;">验货结果</th>
							<th width="200" style="text-align: center;">是否是样品采购</th>
							<th width="200" style="text-align: center;">是否退货</th>
							<th width="200" style="text-align: center;">操作</th>
						</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div style="text-align: center;display: none; " id="pagediv3">
					共查到<span id="datacount3">0</span>数据&nbsp;&nbsp;
					<input type="button" id="prePage3" value="上一页"/>&nbsp;
					第<span id="nowPage3">1</span>页/共<span id="allPage3">0</span>页
					<input type="button" id="nextPage3" value="下一页"/>&nbsp;&nbsp;
					跳至<input type="text" id="toPage3" style="width:50px;"/>页 <input type="button" value="Go" id="jumpPage3"/>
				</div>
				<!-- end 实际采购不在分配采购中-->
				<!-- start 分配给采购的商品实际没有采购-->
				<div  style="padding:15px;display: none;" id="div4">
					<label  onclick="toDiv('1');" title="请点击查看详情"><a style='text-decoration:underline'>分配给采购的商品数量</a>：<span class="count">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('2');" title="请点击查看详情"><a style='text-decoration:underline'>实际采购了多少商品</a>：<span class="count1">0</span></label>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('3');" title="请点击查看详情"><a style='text-decoration:underline'>实际采购不在分配采购中</a>：<span class="count2">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label  style="background-color:red;"><a style='text-decoration:underline'>分配给采购的商品实际没有采购</a>：<span class="count3">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('5');" title="请点击查看详情"><a style='text-decoration:underline'>一个采购商品多个采购订单</a>：<span class="count4">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('6');" title="请点击查看详情"><a style='text-decoration:underline'>列出所有交易</a>：<span class="count5">0</span></label>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<!--          <label style="background-color:red;">*红色行标记数据为采购商品没有匹配到确认采购商品(按照时间倒序)|蓝色标记列的为采购订单没有入库</label> -->
					<table id="categroyReport4" class="imagetable">
						<thead>
						<tr>
							<th width="50" style="text-align: center;">序号</th>
							<th width="180" style="text-align: center;">商品ID</th>
							<th width="180" style="text-align: center;">订单号</th>
							<th width="180" style="text-align: center;">确认采购时间</th>
							<th width="180" style="text-align: center;">原链接图片</th>
						</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div style="text-align: center;display: none; " id="pagediv4">
					共查到<span id="datacount4">0</span>数据&nbsp;&nbsp;
					<input type="button" id="prePage4" value="上一页"/>&nbsp;
					第<span id="nowPage4">1</span>页/共<span id="allPage4">0</span>页
					<input type="button" id="nextPage4" value="下一页"/>&nbsp;&nbsp;
					跳至<input type="text" id="toPage4" style="width:50px;"/>页 <input type="button" value="Go" id="jumpPage4"/>
				</div>
				<!-- end 分配给采购的商品实际没有采购-->
				<!-- start 一个商品有多条采购记录的信息-->
				<div  style="padding:15px;display: none;" id="div5">
					<label  onclick="toDiv('1');" title="请点击查看详情"><a style='text-decoration:underline'>分配给采购的商品数量</a>：<span class="count">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('2');" title="请点击查看详情"><a style='text-decoration:underline'>实际采购了多少商品</a>：<span class="count1">0</span></label>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<label  onclick="toDiv('3');" title="请点击查看详情"><a style='text-decoration:underline'>实际采购不在分配采购中</a>：<span class="count2">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('4');" title="请点击查看详情"><a style='text-decoration:underline'>分配给采购的商品实际没有采购</a>：<span class="count3">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label style="background-color:red;"><a style='text-decoration:underline'>一个采购商品多个采购订单</a>：<span class="count4">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('6');" title="请点击查看详情"><a style='text-decoration:underline'>列出所有交易</a>：<span class="count5">0</span></label>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<!--          <label style="background-color:red;">*红色行标记数据为采购商品没有匹配到确认采购商品(按照时间倒序)|蓝色标记列的为采购订单没有入库</label> -->
					<table id="categroyReport5" class="imagetable">
						<thead>
						<tr>
							<th width="50" style="text-align: center;">序号</th>
							<th width="180" style="text-align: center;">商品ID</th>
							<th width="180" style="text-align: center;">订单号</th>
							<th width="180" style="text-align: center;">确认采购时间</th>
							<th width="180" style="text-align: center;">原链接图片</th>
							<th width="180" style="text-align: center;">1688订单号</th>
						</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div style="text-align: center;display: none; " id="pagediv5">
					共查到<span id="datacount5">0</span>数据&nbsp;&nbsp;
					<input type="button" id="prePage5" value="上一页"/>&nbsp;
					第<span id="nowPage5">1</span>页/共<span id="allPage5">0</span>页
					<input type="button" id="nextPage5" value="下一页"/>&nbsp;&nbsp;
					跳至<input type="text" id="toPage5" style="width:50px;"/>页 <input type="button" value="Go" id="jumpPage5"/>
				</div>
				<!-- end 一个商品有多条采购记录的信息-->
				<!-- start 一个商品有多条采购记录的信息-->
				<div  style="padding:15px;display: none;" id="div6">
					<label  onclick="toDiv('1');" title="请点击查看详情"><a style='text-decoration:underline'>分配给采购的商品数量</a>：<span class="count">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('2');" title="请点击查看详情"><a style='text-decoration:underline'>实际采购了多少商品</a>：<span class="count1">0</span></label>
					&nbsp;&nbsp;&nbsp;&nbsp;
					<label  onclick="toDiv('3');" title="请点击查看详情"><a style='text-decoration:underline'>实际采购不在分配采购中</a>：<span class="count2">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('4');" title="请点击查看详情"><a style='text-decoration:underline'>分配给采购的商品实际没有采购</a>：<span class="count3">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label onclick="toDiv('5');"><a style='text-decoration:underline'>一个采购商品多个采购订单</a>：<span class="count4">0</span></label>&nbsp;&nbsp;&nbsp;&nbsp;
					<label style="background-color:red;" title="请点击查看详情"><a style='text-decoration:underline'>列出所有交易</a>：<span class="count5">0</span></label>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<!--          <label style="background-color:red;">*红色行标记数据为采购商品没有匹配到确认采购商品(按照时间倒序)|蓝色标记列的为采购订单没有入库</label> -->
					<table id="categroyReport6" class="imagetable">
						<thead>
						<tr>
							<th width="50" style="text-align: center;">序号</th>
							<th width="100" style="text-align: center;">采购账号</th>
							<th width="100" style="text-align: center;">采购来源</th>
							<th width="180" style="text-align: center;">支付宝交易号</th>
							<th width="100" style="text-align: center;">订单日期</th>
							<th width="50" style="text-align: center;">交易金额</th>
							<th width="100" style="text-align: center;">状态（是否有退款）</th>
							<th width="100" style="text-align: center;">卖家</th>
						</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
				<div style="text-align: center;display: none; " id="pagediv6">
					共查到<span id="datacount6">0</span>数据&nbsp;&nbsp;
					<input type="button" id="prePage6" value="上一页"/>&nbsp;
					第<span id="nowPage6">1</span>页/共<span id="allPage6">0</span>页
					<input type="button" id="nextPage6" value="下一页"/>&nbsp;&nbsp;
					跳至<input type="text" id="toPage6" style="width:50px;"/>页 <input type="button" value="Go" id="jumpPage6"/>
				</div>
				<!-- end 一个商品有多条采购记录的信息-->
			</div>
		</form>
	</div>
	<div class="mod_pay3" style="display: none;" id="repalyDiv1">
		<div>
			<a href="javascript:void(0)" class="show_x"
			   onclick="$('#repalyDiv1').hide();" style="float: right;">╳</a>
		</div>
		<input id="id" type="hidden" value="">
		回复内容:
		<textarea name="remark_content" rows="8" cols="50" style="margin-top: 20px;" id="remark_content_"></textarea>
		<input type="button" id="repalyBtnId" onclick="remarkOrder()"  value="提交回复">
	</div>
	<div class="repalyDiv2" style="display:none;">
		<input id="tb_id" type="hidden" value="">
		回复内容: <a id="hide_repalyDiv" style="color: red;float: right;margin-right: 10px;font-size: 24px;text-decoration:none" href="javascript:void(0);">X</a><br>
		<textarea name="remark_content" rows="8" cols="50" id="remark_content"></textarea>
		<font color="red" id="ts"></font><br>
		<input type="button" onclick="saveRemark()" value="提交回复">
	</div>
</div>
</body>
<script type="text/javascript">
    $(function(){
        $("#hide_repalyDiv").click(function(){
            hideRepalyDiv();
        });
    })
    function hideRepalyDiv(){
        $(".repalyDiv2").hide();
        $("#remark_content").val("");
        $("#tb_id").val("");
    }
    //添加  实际采购不在分配采购中备注
    function addRemark3(tb_id){
        $("#remark_content").val("");
        $("#tb_id").val(tb_id);
        $(".repalyDiv2").show();
    }
    //保存实际采购不在分配采购中备注
    function saveRemark(){
        var tb_id = $("#tb_id").val();
        var remarkContent = $("#remark_content").val();
        if(tb_id == null || tb_id == ""){
            alert("添加失败");
            return;
        }
        if(remarkContent == null || remarkContent == ""){
            alert("请填写备注信息");
            return;
        }
        $.ajax({
            type:"post",
            url:'/cbtconsole/StatisticalReport/saveRemark',
            dataType:"text",
            data:{"tb_id":tb_id,"remarkContent":remarkContent},
            success : function(data){
                if(data >0){
                    hideRepalyDiv();
                    searchExport(1);
                } else{
                    alert(data);
                }
            }
        });
    }
    function toDiv(type){
        if(type==1){
            //分配给采购的商品数量
            $("#div1").css("display","");
            $("#div2").css("display","none");
            $("#div3").css("display","none");
            $("#div4").css("display","none");
            $("#div5").css("display","none");
            $("#div6").css("display","none");
            $("#pagediv").css("display","");
            $("#pagediv2").css("display","none");
            $("#pagediv3").css("display","none");
            $("#pagediv4").css("display","none");
            $("#pagediv5").css("display","none");
            $("#pagediv6").css("display","none");
        }else if(type==2){
            //实际采购了多少商品
            $("#div1").css("display","none");
            $("#div2").css("display","");
            $("#div3").css("display","none");
            $("#div4").css("display","none");
            $("#div5").css("display","none");
            $("#div6").css("display","none");
            $("#pagediv").css("display","none");
            $("#pagediv2").css("display","");
            $("#pagediv3").css("display","none");
            $("#pagediv4").css("display","none");
            $("#pagediv5").css("display","none");
            $("#pagediv6").css("display","none");
        }else if(type==3){
            //实际采购不在分配采购中
            $("#div1").css("display","none");
            $("#div2").css("display","none");
            $("#div3").css("display","");
            $("#div4").css("display","none");
            $("#div5").css("display","none");
            $("#div6").css("display","none");
            $("#pagediv").css("display","none");
            $("#pagediv2").css("display","none");
            $("#pagediv3").css("display","");
            $("#pagediv4").css("display","none");
            $("#pagediv5").css("display","none");
            $("#pagediv6").css("display","none");
        }else if(type==4){
            //分配给采购的商品实际没有采购
            $("#div1").css("display","none");
            $("#div2").css("display","none");
            $("#div3").css("display","none");
            $("#div4").css("display","");
            $("#div5").css("display","none");
            $("#div6").css("display","none");
            $("#pagediv").css("display","none");
            $("#pagediv2").css("display","none");
            $("#pagediv3").css("display","none");
            $("#pagediv4").css("display","");
            $("#pagediv5").css("display","none");
            $("#pagediv6").css("display","none");
        }else if(type==5){
            //一个商品有多条采购记录的信息
            $("#div1").css("display","none");
            $("#div2").css("display","none");
            $("#div3").css("display","none");
            $("#div4").css("display","none");
            $("#div5").css("display","");
            $("#div6").css("display","none");
            $("#pagediv").css("display","none");
            $("#pagediv2").css("display","none");
            $("#pagediv3").css("display","none");
            $("#pagediv4").css("display","none");
            $("#pagediv5").css("display","");
            $("#pagediv6").css("display","none");
        }else if(type==6){
            //一个商品有多条采购记录的信息
            $("#div1").css("display","none");
            $("#div2").css("display","none");
            $("#div3").css("display","none");
            $("#div4").css("display","none");
            $("#div5").css("display","none");
            $("#div6").css("display","");
            $("#pagediv").css("display","none");
            $("#pagediv2").css("display","none");
            $("#pagediv3").css("display","none");
            $("#pagediv4").css("display","none");
            $("#pagediv5").css("display","none");
            $("#pagediv6").css("display","");
        }
        searchExport(1,type);
    }

    $("#prePage").click(function(){
        var nowPage = $("#nowPage").html();
        if(parseInt(nowPage)<=1 ){
            alert("已到达首页");
            return false;
        }else{
            $("#nowPage").html(parseInt(nowPage)-1)
            searchExport(parseInt(nowPage)-1,1);
        }
    });
    $("#nextPage").click(function(){
        var nowPage = $("#nowPage").html();
        var allPage = $("#allPage").html();
        if(parseInt(nowPage)==parseInt(allPage) ){
            alert("已到达尾页");
            return false;
        }else{
            $("#nowPage").html(parseInt(nowPage)+1)
            searchExport(parseInt(nowPage)+1,1);
        }
    });
    $("#jumpPage").click(function(){
        var allPage = $("#allPage").html();
        var topage = $("#toPage").val();
        if(isNaN(topage)){
            alert("请输入正确的页码");
            return false;
        }else if(parseInt(topage)<=0 || parseInt(topage)>allPage){
            alert("页码超出范围");
            return false;
        }else{
            $("#nowPage").html(parseInt(topage))
            searchExport(parseInt(topage),1);
        }
    });


    $("#prePage2").click(function(){
        var nowPage = $("#nowPage2").html();
        if(parseInt(nowPage)<=1 ){
            alert("已到达首页");
            return false;
        }else{
            $("#nowPage2").html(parseInt(nowPage)-1)
            searchExport(parseInt(nowPage)-1,2);
        }
    });
    $("#nextPage2").click(function(){
        var nowPage = $("#nowPage2").html();
        var allPage = $("#allPage2").html();
        if(parseInt(nowPage)==parseInt(allPage) ){
            alert("已到达尾页");
            return false;
        }else{
            $("#nowPage2").html(parseInt(nowPage)+1)
            searchExport(parseInt(nowPage)+1,2);
        }
    });
    $("#jumpPage2").click(function(){
        var allPage = $("#allPage2").html();
        var topage = $("#toPage2").val();
        if(isNaN(topage)){
            alert("请输入正确的页码");
            return false;
        }else if(parseInt(topage)<=0 || parseInt(topage)>allPage){
            alert("页码超出范围");
            return false;
        }else{
            $("#nowPage2").html(parseInt(topage))
            searchExport(parseInt(topage),2);
        }
    });

    $("#prePage3").click(function(){
        var nowPage = $("#nowPage3").html();
        if(parseInt(nowPage)<=1 ){
            alert("已到达首页");
            return false;
        }else{
            $("#nowPage3").html(parseInt(nowPage)-1)
            searchExport(parseInt(nowPage)-1,3);
        }
    });
    $("#nextPage3").click(function(){
        var nowPage = $("#nowPage3").html();
        var allPage = $("#allPage3").html();
        if(parseInt(nowPage)==parseInt(allPage) ){
            alert("已到达尾页");
            return false;
        }else{
            $("#nowPage3").html(parseInt(nowPage)+1)
            searchExport(parseInt(nowPage)+1,3);
        }
    });
    $("#jumpPage3").click(function(){
        var allPage = $("#allPage3").html();
        var topage = $("#toPage3").val();
        if(isNaN(topage)){
            alert("请输入正确的页码");
            return false;
        }else if(parseInt(topage)<=0 || parseInt(topage)>allPage){
            alert("页码超出范围");
            return false;
        }else{
            $("#nowPage3").html(parseInt(topage))
            searchExport(parseInt(topage),3);
        }
    });

    $("#prePage4").click(function(){
        var nowPage = $("#nowPage4").html();
        if(parseInt(nowPage)<=1 ){
            alert("已到达首页");
            return false;
        }else{
            $("#nowPage4").html(parseInt(nowPage)-1)
            searchExport(parseInt(nowPage)-1,4);
        }
    });
    $("#nextPage4").click(function(){
        var nowPage = $("#nowPage4").html();
        var allPage = $("#allPage4").html();
        if(parseInt(nowPage)==parseInt(allPage) ){
            alert("已到达尾页");
            return false;
        }else{
            $("#nowPage4").html(parseInt(nowPage)+1)
            searchExport(parseInt(nowPage)+1,4);
        }
    });
    $("#jumpPage4").click(function(){
        var allPage = $("#allPage4").html();
        var topage = $("#toPage4").val();
        if(isNaN(topage)){
            alert("请输入正确的页码");
            return false;
        }else if(parseInt(topage)<=0 || parseInt(topage)>allPage){
            alert("页码超出范围");
            return false;
        }else{
            $("#nowPage4").html(parseInt(topage))
            searchExport(parseInt(topage),4);
        }
    });

    $("#prePage5").click(function(){
        var nowPage = $("#nowPage5").html();
        if(parseInt(nowPage)<=1 ){
            alert("已到达首页");
            return false;
        }else{
            $("#nowPage5").html(parseInt(nowPage)-1)
            searchExport(parseInt(nowPage)-1,5);
        }
    });
    $("#nextPage5").click(function(){
        var nowPage = $("#nowPage5").html();
        var allPage = $("#allPage5").html();
        if(parseInt(nowPage)==parseInt(allPage) ){
            alert("已到达尾页");
            return false;
        }else{
            $("#nowPage5").html(parseInt(nowPage)+1)
            searchExport(parseInt(nowPage)+1,5);
        }
    });
    $("#jumpPage5").click(function(){
        var allPage = $("#allPage5").html();
        var topage = $("#toPage5").val();
        if(isNaN(topage)){
            alert("请输入正确的页码");
            return false;
        }else if(parseInt(topage)<=0 || parseInt(topage)>allPage){
            alert("页码超出范围");
            return false;
        }else{
            $("#nowPage5").html(parseInt(topage))
            searchExport(parseInt(topage),5);
        }
    });

    $("#prePage6").click(function(){
        var nowPage = $("#nowPage6").html();
        if(parseInt(nowPage)<=1 ){
            alert("已到达首页");
            return false;
        }else{
            $("#nowPage6").html(parseInt(nowPage)-1)
            searchExport(parseInt(nowPage)-1,6);
        }
    });
    $("#nextPage6").click(function(){
        var nowPage = $("#nowPage6").html();
        var allPage = $("#allPage6").html();
        if(parseInt(nowPage)==parseInt(allPage) ){
            alert("已到达尾页");
            return false;
        }else{
            $("#nowPage6").html(parseInt(nowPage)+1)
            searchExport(parseInt(nowPage)+1,6);
        }
    });
    $("#jumpPage6").click(function(){
        var allPage = $("#allPage6").html();
        var topage = $("#toPage6").val();
        if(isNaN(topage)){
            alert("请输入正确的页码");
            return false;
        }else if(parseInt(topage)<=0 || parseInt(topage)>allPage){
            alert("页码超出范围");
            return false;
        }else{
            $("#nowPage6").html(parseInt(topage))
            searchExport(parseInt(topage),6);
        }
    });
    //查询
    $('#pgSearch').click(function(){
        searchExport(1,1);
    });

    function openRemark(id){
        $("#remark_content_").val("");
        $("#id").val(id);
        var rfddd = document.getElementById("repalyDiv1");
        rfddd.style.display = "block";
    }

    function remarkOrder(){
        var id=$("#id").val();
        var context=$("#remark_content_").val();
        if(context==null || context==""){
            alert("请输入备注内容");
            return;
        }
        jQuery.ajax({
            url:"/cbtconsole/StatisticalReport/addTbOrderRemark",
            data:{
                "id":id,
                "context":context
            },
            type:"post",
            success:function(data){
                if(data){
                    var new_remark=data.data.amount;
                    $("#rk_remark_"+id+"").html(new_remark);
                    $('#repalyDiv1').hide();
                }
            },
        });
    }

    function searchExport(page,type){
        $("#categroyReport tbody").html("");
        $("#categroyReport2 tbody").html("");
        $("#categroyReport3 tbody").html("");
        $("#categroyReport4 tbody").html("");
        $("#categroyReport5 tbody").html("");
        $("#categroyReport6 tbody").html("");
        var startdate =$('#startdate').val();
        var enddate =$('#enddate').val();
        var buyer =$('#buyer').val();
        var isTrack =$('#isTrack').val();
        if(buyer == null || buyer ==""){
            alert("请选择采购人");
            return;
		}
        if(startdate==null || startdate==""){
            alert("开始时间不能为空");
            return;
        }
        document.getElementById('operatediv').style.display='block';
        jQuery.ajax({
            url:searchReport,
            data:{
                "buyer":buyer,
                "startdate":startdate,
                "enddate":enddate,
                "isTrack":isTrack,
                "page":page,
				"type":type

            },
            type:"post",
            success:function(data){
                if(data){
                    var reportDetailList=data.data.ops_list;
                    var amount=data.data.amount;
                    var allCount=data.data.allCount;
                    if(allCount%20==0){
                        allpage = allCount/20;
                    }else{
                        allpage = parseInt(allCount/20) + 1;
                    }
                    // $("#amount").html("￥"+amount);
                    $("#datacount").html(allCount);
                    $("#nowPage").html(page);
                    $("#allPage").html(allpage);
                    $(".count").html(allCount);
                    // if(reportDetailList.length>0){
                    //     $(".count").html(reportDetailList[0].adminid);
                    //     //$(".count1").html(reportDetailList[0].goodsdataid);
                    // }

                    for(var i=0;i<reportDetailList.length;i++){
                        reportDetail = reportDetailList[i];
                        htm_='';
                        if(reportDetail.buyerOrderid=="无"){
                            htm_ = '<tr style="background-color: #FF8484">';
                        }else{
                            htm_ = '<tr>';
                        }
                        htm_ += '<td align="center">'+(i+1)+'</td>';
                        htm_ += '<td align="center">'+reportDetail.goodsid+'</td>';
                        htm_ += '<td align="center">'+reportDetail.orderid+'</td>';
                        // htm_ += '<td align="center">'+reportDetail.car_img+'</td>';
                        htm_ += '<td align="center">'+reportDetail.confirmTime+'</td>';
                        htm_ += '<td align="center"><img src="'+reportDetail.goodsImgUrl+'" height="100" width="100"></td>';
                        if(reportDetail.buyerOrderid=="无"){
                            htm_ += '<td align="center">否</td>';
                        }else{
                            htm_ += '<td align="center">是</td>';
                        }
                        htm_ += '<td align="center">'+reportDetail.buyerOrderid+'</td>';
//                 	htm_ += '<td align="center"><img src="'+reportDetail.imgurl+'" height="100" width="100"></td>';
                        htm_ += '</tr>';
                        $('#categroyReport').append(htm_);
                    }

                    var reportDetailTbList=data.data.tb_list;
                    var allCount2=data.data.tbAllCount;
                    if(allCount2%20==0){
                        allpage2 = allCount2/20;
                    }else{
                        allpage2 = parseInt(allCount2/20) + 1;
                    }
                    $("#datacount2").html(allCount2);
                    $("#nowPage2").html(page);
                    $("#allPage2").html(allpage2);
                    $(".count1").html(allCount2);
                    for(var i=0;i<reportDetailTbList.length;i++){
                        reportDetail = reportDetailTbList[i];
                        htm_='';
                        htm_ = '<tr>';
                        htm_ += '<td align="center" >'+(i+1)+'</td>';
                        htm_ += '<td align="center">'+reportDetail.orderid+'</td>';
                        if(reportDetail.tbOr1688==0){
                            htm_ += '<td align="center">淘宝</td>';
                        }else{
                            htm_ += '<td align="center">1688</td>';
                        }
                        htm_ += '<td align="center"><a target="_blank" href="'+reportDetail.itemurl+'">'+reportDetail.itemurl+'</a></td>';
                        htm_ += '<td align="center">'+reportDetail.itemname+'</td>';
                        htm_ += '<td align="center">'+reportDetail.sku+'</td>';
                        htm_ += '<td align="center">'+reportDetail.shipno+'</td>';
                        htm_ += '<td align="center">'+reportDetail.orderstatus+'</td>';
                        htm_ += '<td align="center">'+reportDetail.paydata+'</td>';
                        htm_ += '<td align="center">'+reportDetail.username+'</td>';
                        htm_ += '<td align="center"><img src="'+reportDetail.imgurl+'" height="100" width="100"></td>';
                        if(reportDetail.in_time==null || reportDetail.in_time==''){
                            htm_ += '<td align="center">无入库记录</td>';
                        }else{
                            htm_ += '<td align="center">'+reportDetail.in_time+'</td>';
                        }
                        htm_ += '<td align="center">'+reportDetail.opsorderid+'</td>';
                        htm_ += '<td align="center"><img src="'+reportDetail.goodsImgUrl+'" height="100" width="100"></td>';
                        var tip="";
                        if(reportDetail.opsorderid=="无"){
                            tip+="<span style='color:red;'>该采购商品没有匹配销售商品</span><br>";
                        }
                        if(reportDetail.in_time==null || reportDetail.in_time==''){
                            tip+="<span style='color:chocolate;'>该商品没有入库记录</span><br>";
                        }
                        if(Number(reportDetail.itemqty)<Number(reportDetail.buycount)){
                            tip+="<span style='color:blue;'>该商品采购数量小于销售数量</span>";
                        }
                        if(Number(reportDetail.itemqty)>Number(reportDetail.buycount)){

                        }
                        htm_ += '<td align="center">'+tip+'</td>';
                        htm_ += '<td> <div style="overflow-y:scroll;height:200px;width:200px;"><div class="w-font">';
                        htm_ += '<font style="font-size: 15px;" id="rk_remark_'+reportDetail.id+'">'+reportDetail.remark+'</font></div></div><div class="w-margin-top"><input type="button" value="回复" onclick="openRemark('+reportDetail.id+');"/></div></td>';
                        htm_ += '</tr>';
                        $('#categroyReport2').append(htm_);
                    }
                    //实际采购订单没有入库记录信息展示
                    var taobao_list=data.data.taobao_list;
                    var taobao_list_size=data.data.taobao_list_size;
                    if(taobao_list_size%20==0){
                        allpage3 = taobao_list_size/20;
                    }else{
                        allpage3 = parseInt(taobao_list_size/20) + 1;
                    }
                    $("#datacount3").html(taobao_list_size);
                    $("#nowPage3").html(page);
                    $("#allPage3").html(allpage3);
                    $(".count2").html(taobao_list_size);
                    for(var m=0;m<taobao_list.length;m++){
                        taobao_list_details = taobao_list[m];
                        htm_='';
                        htm_ = '<tr>';
                        htm_ += '<td align="center" >'+(m+1)+'</td>';
                        htm_ += '<td align="center">'+taobao_list_details.orderid+'</td>';
                        if(taobao_list_details.tbOr1688==0){
                            htm_ += '<td align="center">淘宝</td>';
                        }else{
                            htm_ += '<td align="center">1688</td>';
                        }
                        htm_ += '<td align="center"><a target="_blank" href="'+taobao_list_details.itemurl+'">'+taobao_list_details.itemurl+'</a></td>';
                        htm_ += '<td align="center">'+taobao_list_details.itemname+'</td>';
                        htm_ += '<td align="center">'+taobao_list_details.sku+'</td>';
                        htm_ += '<td align="center">'+taobao_list_details.shipno+'</td>';
                        htm_ += '<td align="center">'+taobao_list_details.orderstatus+'</td>';
                        htm_ += '<td align="center">'+taobao_list_details.paydata+'</td>';
                        htm_ += '<td align="center">'+taobao_list_details.username+'</td>';
                        htm_ += '<td align="center">'+taobao_list_details.totalprice+'</td>';
                        htm_ += '<td align="center"><img src="'+taobao_list_details.imgurl+'" height="100" width="100"></td>';
                        if(taobao_list_details.remark==null || taobao_list_details.remark ==""){
                            htm_ += '<td align="center">-</td>';
                        }else{
                            htm_ += '<td align="center">'+taobao_list_details.remark+'</td>';
                        }
                        htm_ += '<td align="center">'+taobao_list_details.id_state+'</td>';//是否入库
                        htm_ += '<td align="center">'+taobao_list_details.inspection_result+'</td>';//验货结果
                        htm_ += '<td align="center">'+taobao_list_details.sample_goods+'</td>';
                        htm_ += '<td align="center">'+taobao_list_details.return_goods+'</td>';
                        htm_ += '<td align="center"><input type="button" onclick="addRemark3('+taobao_list_details.id+')" value="添加备注"/></td>';
                        htm_ += '</tr>';
                        $('#categroyReport3').append(htm_);
                    }
                    // 分配给采购的商品实际没有采购
                    var fpNoBuyInfoList=data.data.fpNoBuyInfoList;
                    var fpNoBuyInfoListCount=data.data.fpNoBuyInfoListCount;
                    if(fpNoBuyInfoListCount%20==0){
                        allpage4 = fpNoBuyInfoListCount/20;
                    }else{
                        allpage4 = parseInt(fpNoBuyInfoListCount/20) + 1;
                    }
                    $("#datacount4").html(fpNoBuyInfoListCount);
                    $("#nowPage4").html(page);
                    $("#allPage4").html(allpage4);
                    $(".count3").html(fpNoBuyInfoListCount);
                    for(var m=0;m<fpNoBuyInfoList.length;m++){
                        fpNoBuyInfoList_details = fpNoBuyInfoList[m];
                        htm_='';
                        htm_ = '<tr>';
                        htm_ += '<td align="center">'+(m+1)+'</td>';
                        htm_ += '<td align="center">'+fpNoBuyInfoList_details.goodsid+'</td>';
                        htm_ += '<td align="center">'+fpNoBuyInfoList_details.orderid+'</td>';
                        // htm_ += '<td align="center">'+reportDetail.car_img+'</td>';
                        htm_ += '<td align="center">'+fpNoBuyInfoList_details.confirmTime+'</td>';
                        htm_ += '<td align="center"><img src="'+fpNoBuyInfoList_details.goodsImgUrl+'" height="100" width="100"></td>';
                        htm_ += '</tr>';
                        $('#categroyReport4').append(htm_);
                    }

                    //  一个采购商品多个采购订单
                    var oneMathchMoreOrderInfo=data.data.oneMathchMoreOrderInfo;
                    var oneMathchMoreOrderInfoCount=data.data.oneMathchMoreOrderInfoCount;
                    if(oneMathchMoreOrderInfoCount%20==0){
                        allpage5 = oneMathchMoreOrderInfoCount/20;
                    }else{
                        allpage5 = parseInt(oneMathchMoreOrderInfoCount/20) + 1;
                    }
                    $("#datacount5").html(oneMathchMoreOrderInfoCount);
                    $("#nowPage5").html(page);
                    $("#allPage5").html(allpage5);
                    $(".count4").html(oneMathchMoreOrderInfoCount);
                    for(var m=0;m<oneMathchMoreOrderInfo.length;m++){
                        oneMathchMoreOrderInfo_details = oneMathchMoreOrderInfo[m];
                        htm_='';
                        htm_ = '<tr>';
                        htm_ += '<td align="center">'+(i+1)+'</td>';
                        htm_ += '<td align="center">'+oneMathchMoreOrderInfo_details.goodsid+'</td>';
                        htm_ += '<td align="center">'+oneMathchMoreOrderInfo_details.orderid+'</td>';
                        htm_ += '<td align="center">'+oneMathchMoreOrderInfo_details.confirmTime+'</td>';
                        htm_ += '<td align="center"><img src="'+oneMathchMoreOrderInfo_details.goodsImgUrl+'" height="100" width="100"></td>';
                        htm_ += '<td align="center">'+oneMathchMoreOrderInfo_details.buyerOrderid+'</td>';
                        htm_ += '</tr>';
                        $('#categroyReport5').append(htm_);
                    }

                    //  列出所有交易
                    var allBuyOrderInfo=data.data.allBuyerOrderInfo;
                    var allBuyOrderInfoCount=data.data.allBuyerOrderInfoCount;
                    if(allBuyOrderInfoCount%20==0){
                        allpage6 = allBuyOrderInfoCount/20;
                    }else{
                        allpage6 = parseInt(allBuyOrderInfoCount/20) + 1;
                    }
                    $("#datacount6").html(allBuyOrderInfoCount);
                    $("#nowPage6").html(page);
                    $("#allPage6").html(allpage6);
                    $(".count5").html(allBuyOrderInfoCount);
                    for(var m=0;m<allBuyOrderInfo.length;m++){
                        allBuyOrderInfo_details = allBuyOrderInfo[m];
                        htm_='';
                        htm_ = '<tr>';
                        htm_ += '<td align="center">'+(i+1)+'</td>';
                        htm_ += '<td align="center">'+allBuyOrderInfo_details.username+'</td>';
                        htm_ += '<td align="center">'+allBuyOrderInfo_details.tbOr1688+'</td>';
                        htm_ += '<td align="center">'+allBuyOrderInfo_details.paytreasureid+'</td>';
                        htm_ += '<td align="center">'+allBuyOrderInfo_details.orderdate+'</td>';
                        htm_ += '<td align="center">'+allBuyOrderInfo_details.totalprice+'</td>';
                        htm_ += '<td align="center">'+allBuyOrderInfo_details.orderstatus+'</td>';
                        htm_ += '<td align="center">'+allBuyOrderInfo_details.seller+'</td>';
                        htm_ += '</tr>';
                        $('#categroyReport6').append(htm_);
                    }

                    document.getElementById('operatediv').style.display='none';
                }else{
                    //alert("查询失败！");
                    $("#pagediv").css("display","none");
                    $("#pagediv2").css("display","none");
                    $("#categroyReport").append("<tr><td colspan=8 style='text-align:center;color:red;'>(没有数据)</td></tr>");
                    $("#categroyReport2").append("<tr><td colspan=8 style='text-align:center;color:red;'>(没有数据)</td></tr>");
                    $("#categroyReport3").append("<tr><td colspan=8 style='text-align:center;color:red;'>(没有数据)</td></tr>");
                    $("#categroyReport4").append("<tr><td colspan=8 style='text-align:center;color:red;'>(没有数据)</td></tr>");
                    $("#categroyReport5").append("<tr><td colspan=8 style='text-align:center;color:red;'>(没有数据)</td></tr>");
                    $("#categroyReport6").append("<tr><td colspan=8 style='text-align:center;color:red;'>(没有数据)</td></tr>");
                }
            },
            error:function(e){
                $("#pagediv").css("display","none");
                $("#pagediv2").css("display","none");
                $("#pagediv3").css("display","none");
                $("#pagediv4").css("display","none");
                $("#pagediv5").css("display","none");
                $("#pagediv6").css("display","none");
                alert("查询失败！");
            }
        });

    }



</script>
</html>