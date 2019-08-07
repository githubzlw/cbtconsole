<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>库存清单</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/inventoryReport.js"></script>
<script src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script type="text/javascript">
var updateSourcesUrl = "/cbtconsole/inventory/updateSources"; //盘点库存
</script>
<style type="text/css">
		.wraps em,i{font-style: normal;display: inline-block;float:left;}
		.clearfix:before,.clearfix:after{content:"";display:table;}
		.clearfix:after{clear:both;}
		.clearfix{zoom:1;} 
		.wraps{border:2px solid #999;width:1047px;margin:0 auto;padding:20px;position: relative;display:none;/* top:500px;z-index: 999;  */   background-color: #e0ecff;}
		.wraps span{display:inline-block;width:100px;float:left;}
		.wraps input[type="text"]{border:1px solid #999;background-color: #fff;height:28px;border-radius: 4px;width:205px;float:left;}
        .wraps input[type="radio"]{width:18px;height:18px;position: relative;top:2px;}
        .wraps label{margin-right:10px;}
        .wrap6{overflow:hidden;}
        .wrap6 span,.wraps .reasons{float:left;position: relative;}
        .w235{width:300px;}
        .wraps .wrap{margin-bottom:40px;overflow:hidden;}
        .wrap7 img{width:250px;height: 250px;}
        .wrap2 em{width:645px;}
        .wrap7 {float:right;position: relative;top:-30px;}
        .left{float:left;}
        p{text-align: center;}
        .other{position: absolute;top:15px;right:-222px;}
        .wrap8{text-align: center;}
        .submit_button{border:1px solid #999;background-color:#fff;padding:0 80px; line-height:28px;border-radius: 4px;}
.button_c{
border-radius: 5px;
background: #e7f1ff;
}
.button_top{
margin-top: 3px;
}
.av_count{margin-left: 70px;}
.top_title{font-size: 15px;font-weight: bold;}
.displaynone{display:none;}
.item_box{display:inline-block;margin-right:52px;}
.item_box select{width:150px;}
.w-group{margin-bottom: 10px;width: 60%;text-align: center;}
.w-label{float:left;}
.w-div{margin-left:120px;}
.w-remark{width:100%;}
table.imagetable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #999999;
	border-collapse: collapse;
}
.mod_pay3 {
	width: 400px;
	height:400px;
	position: fixed;
	top: 100px;
	right: 15%;
	margin-left:400px;
	z-index: 1011;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 1011;
}
table.imagetable th {
	background:#b5cfd2 url('cell-blue.jpg');
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #999999;
}
.panel-title {
	text-align: center;
	height: 30px;
	font-size: 24px;
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
.but_color {
	background: #44a823;
	width: 80px;
	height: 24px;
	border: 1px #aaa solid;
	color: #fff;
}
.form-horizontal .control-label{text-align: left;}
th{font-weight: normal;}
th,td{text-align: center;}
label{font-weight: normal;}
.remark label{margin-right:32px;}
button{width: 300px;}
.container{display: none;}
table td:last-child{text-align: center;}
table input[type="radio"]{width:20px;height:20px;}

th{font-weight: normal;}
th,td{text-align: center;}
label{font-weight: normal;}
input[type="file"]{position: absolute;top:0;left:15px;width: 124px;height:34px;opacity: 0;}
.gain{color:#4395ff;cursor:pointer;}
table  input[type="checkbox"]{width:20px;height:20px;}
.remark{margin-top:20px;}
.remark label{margin-right:32px;}
button{width: 300px;}
.transparent,.transparent-bg{width:100%;height:100%;background-color:rgba(0,0,0,0);position: fixed;z-index:1;display: none;text-align: center;}
.transparent-bg{z-index:2;background-color:rgba(0,0,0,.5);}
      .transparent img{display: inline-block;z-index:3;position: relative;top:20px;}
em,i{font-style: normal;}
#img-responsive{width: 200px;height: 200px;}

</style>
<% 
  String sku=request.getParameter("sku");
  String pid=request.getParameter("pid");
  String car_urlMD5=request.getParameter("car_urlMD5");
%>

</head>
<body text="#000000" >
    	<div class="mod_pay3" style="display: none;" id="big_img">
			
		</div>
		<!-- <div id="dlg2" class="easyui-dialog"  title="库存删除" data-options="modal:true" style="width:400px;height:200px;padding:10px;autoOpen:false;closed:true;display: none;">
			<form id="ff2" method="post" style="height:100%;">
				<div style="margin-bottom:20px;margin-left:35px;">
					<input type="hidden" id="dId">
					<input type="hidden" id="dPid">
					<input type="hidden" id="dBarcode">
					<input type="hidden" id="dAmount">
					<input class="easyui-textbox" name="delRemark" id="delRemark"  style="width:70%;"  data-options="label:'盘点备注:'">
				</div>
				<div style="text-align:center;padding:5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="delInventorySources()" style="width:80px">提交</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance2()" style="width:80px">取消</a>
				</div>
			</form>
		</div> -->
	<!-- <div id="dlg" class="easyui-dialog"  title="库存盘点" data-options="modal:true" style="width:400px;height:400px;padding:10px;autoOpen:false;closed:true;display: none;">
	<form id="ff" method="post" style="height:100%;">
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="old_remaining" id="old_remaining" readonly="readonly"  style="width:70%;"  data-options="label:'当前库存:'">
				<script type="text/javascript">$(function () {$('#old_remaining').textbox('textbox').css('background','#ccc')})</script>
				<input type="hidden" id="pd_id" name="pd_id"/>
				<input type="hidden" id="flag_" name="flag_"/>
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-numberbox" name="new_remaining" id="new_remaining"  style="width:70%;"  data-options="label:'盘点数量:'">
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="old_barcode" id="old_barcode" readonly="readonly" style="width:70%;"  data-options="label:'当前库位:'">
				<script type="text/javascript">$(function () {$('#old_barcode').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<select class="easyui-combobox" name="new_barcode" id="new_barcode1" style="width:70%;" data-options="label:'盘点库位:',valueField: 'id',   
                    textField: 'path', value:'',
                    url: '/cbtconsole/StatisticalReport/getNewBarcode',  
                    method:'get'">
				</select>
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="remark" id="remark"  style="width:70%;"  data-options="label:'盘点备注:'">
			</div>
			<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateSources()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance()" style="width:80px">取消</a>
		</div>
		</form>
	</div> -->
	<!-- <div id="dlg1" class="easyui-dialog" title="手动录入库存" data-options="modal:true" style="width:400px;height:400px;padding:10px;autoOpen:false;;closed:true;display: none;">
	<form  method="post" style="height:100%;">
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="orderNo" id="order_no_id"  style="width:70%;"  data-options="label:'订单号:',required:true">
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-numberbox" name="goodsid" id="goodsid"  style="width:70%;"  data-options="label:'商品号:',required:true">
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-numberbox" name="count_" id="count_"  style="width:70%;"  data-options="label:'库存数量:',required:true">
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<select class="easyui-combobox" name="new_barcode2" id="new_barcode2" style="width:70%;" data-options="label:'库存库位:',required:true,valueField: 'id',   
                    textField: 'path', value:'',
                    url: '/cbtconsole/StatisticalReport/getNewBarcode',  
                    method:'get'">
				</select>
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="remark_" id="remark_"  style="width:70%;"  data-options="label:'盘点备注:'">
			</div>
			<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="inventoryEntry()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance1()" style="width:80px">取消</a>
		</div>
		</form>
	</div> -->
		<!-- <div id="dlg3" class="easyui-dialog" title="手动录入亚马逊库存" data-options="modal:true" style="width:400px;height:450px;padding:10px;autoOpen:false;;closed:true;display: none;">
			<form  method="post" style="height:100%;">
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-numberbox" name="itmeid" id="itmeid"  style="width:90%;"  data-options="label:'商品pid:',required:true">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-numberbox" name="goods_p_price" id="goods_p_price"  style="width:90%;"  data-options="label:'商品价格:'">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-numberbox" name="ymx_count" id="ymx_count"  style="width:90%;"  data-options="label:'库存数量:',required:true">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="ymx_img" id="ymx_img"  style="width:90%;"  data-options="label:'商品图片:'">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="ymx_name" id="ymx_name"  style="width:90%;"  data-options="label:'商品名称:'">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<select class="easyui-combobox" name="ymx_barcode2" id="ymx_barcode2" style="width:90%;" data-options="label:'库存库位:',required:true,valueField: 'id',
                    textField: 'path', value:'',
                    url: '/cbtconsole/StatisticalReport/getNewBarcode',
                    method:'get'">
					</select>
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="remark_ymx" id="remark_ymx"  style="width:90%;"  data-options="label:'盘点备注:'">
				</div>
				<div style="text-align:center;padding:5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="inventoryYmxEntry()" style="width:80px">提交</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance3()" style="width:80px">取消</a>
				</div>
			</form>
		</div> -->
	<%-- <div id="top_toolbar" style="padding: 5px; height: auto">
		<div style="margin-left:10px;">
			<span style="font-size:13px;font-weight:bold">最近30天新产生的库存(数量/金额):</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_1" style="font-size:20px;width:35px;margin-right:100px">0</span>
			<span style="font-size:13px;font-weight:bold">最近30天销售掉的库存(数量/金额):</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_2" style="font-size:20px;width:35px;margin-right:100px">0</span>
			<span style="font-size:13px;font-weight:bold">最近30天产生的库存损耗(数量/金额):</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_3" style="font-size:20px;width:35px;margin-right:100px">0</span>
			<span style="font-size:13px;font-weight:bold">最近30天产生的库存删除(数量/金额):</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_4" style="font-size:20px;width:35px;margin-right:100px">0</span>
		</div>
		<div>
			<form id="query_form" action="#" onsubmit="return false;">
	              是否盘点: <select name="" id="flag" style="width: 60px;" onchange="changDiv(this.value)">
	                         <option value="-1">全部</option>
		                     <option value="1">已盘点</option>
					         <option value="0">未盘点</option>
					         <option value="2">问题库存</option>
	                   </select>
	                   <div style="display: none">
	                    <select name="" id="type">
		                     <option value="remaining">库存</option>
	                      </select>
	                       <select name="" id="scope">
		                     <option value="0">ALL</option>
		                     <option value="1">大于</option>
					         <option value="2">等于</option>
					         <option value="3">小于</option>
	                      </select>
	                      <input type="text" id="count"/>
	                   </div>
	                      商品信息：<input type="text" id="goodinfo">
						  商品PID：<input type="text" id="goods_pid">
	                      商品规格：<input type="text" id="sku">
	                      商品库位：<input type="text" id="barcode" onkeypress="if (event.keyCode == 13) searchExport(1);">
	                      <select class="easyui-combobox" name="have_barcode" id="have_barcode" style="width:12%;" data-options="label:'有库存库位:',panelHeight:'400px',valueField: 'barcode',   
                    textField: 'barcode', value:'全部',selected:true,
                    url: '/cbtconsole/StatisticalReport/getHavebarcode',  
                    method:'get'">
				</select>
					<select class="easyui-combobox" name="valid" id="valid" style="width:10%;" data-options="label:'是否上架:',panelHeight:'auto'">
						<option value="-1" selected="selected">所有</option>
						<option value="1">上架</option>
						<option value="0">下架</option>
						<option value="2">ali商品</option>
						<option value="3">亚马逊商品</option>
					</select>
				<select class="easyui-combobox" name="goodscatid" id="goodscatid" style="width:15%;" data-options="label:'库存商品类别:',Height:'2000px',valueField: 'goodscatid',
                    textField: 'goodscatid', value:'全部',selected:true,
                    url: '/cbtconsole/StatisticalReport/getAllInventory',
                    method:'get'">
				</select>
	         <div id="pandian_time" style="display: none">
	              	              盘点时间:<input id="startdate"
						name="startdate" readonly="readonly"
						onfocus="WdatePicker({isShowWeek:true})"
						value="${param.startdate}"
						onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />-
						<input id="enddate" name="enddate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})"
						value="${param.startdate}"
						onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}'  style="margin-laft:5px;"/>
	           </div>
				<br> <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<a href="javascript:exportData();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">导出</a>
		<a href="javascript:openInventoryEntryView();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">库存录入</a>
		<a href="javascript:openYmxInventoryEntryView();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">亚马逊库存录入</a>
		<a href="/cbtconsole/website/inventory_update_log.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">库存盘点日志</a>
<!-- 		<a href="/cbtconsole/website/inventory_update_log.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">库存盘点记录</a> -->
		<a href="/cbtconsole/website/inventory_delete_log.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">库存删除记录</a>
		<a href="/cbtconsole/website/loss_inventory_log.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">库存损耗列表</a>
	</div> --%>
	<div class="wraps easyui-dialog" id="dlg4" title="报损调整" style="width:1047px;padding:10px;autoOpen:false;closed:true;">
		<div class="wrap wrap1">
			<span>产品 ID</span>
			<input type="text" name="igoodsId" id="index_igoodsID" readonly="readonly">
			<span style="margin-left: 40px;">SKUID</span>
			<input type="text" name="iskuid" id="index_iskuid" readonly="readonly">
			<span style="margin-left: 40px;">SPECID</span>
			<input type="text" name="ispecid" id="index_ispecid" readonly="readonly">
		</div>
		<div class="wrap wrap2">
			<span>产品名称</span>
			<em id="index_igoodsname"></em>
		</div>
		<div class="wrap-overflow clearfix">
			<div class="left">
				<div class="wrap wrap3">
					<span>产品规格</span>
					<em class="w235" id="index_isku"></em>
				</div>
				<div class="wrap wrap4">
					<span class="al_count" >当前库存数量</span>
					<i id="index_iremaining">0</i>
					<span class="av_count" >当前可用库存数量</span>
					<i id="index_icanremaining">0</i>
				</div>
				<div class="wrap wrap5">
					<span>调整当前库存数量为</span>
					<input type="text" name="changeNumber" id="index_ichangcount">
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
			<!-- 0  损坏 1 遗失  3 添加 4 补货  5 漏发 7 其他原因 -->
				<label>
					<input type="radio" name="change_type" value="0" checked="checked" class="radio_change">
					损坏
				</label>
				<label >
					<input type="radio" name="change_type" value="1" class="radio_change">
					遗失
				</label>
				<label>
					<input type="radio" name="change_type" value="3" class="radio_change">
					添加
				</label>
				<label>
					<input type="radio" name="change_type" value="4" class="radio_change">
					补货
				</label>
				<label>
					<input type="radio" name="change_type" value="5" class="radio_change">
					漏发
				</label>
				<label>
					<input type="radio" name="change_type" value="7">
					其他
				</label>
				<input type="text" class="other" name="remark" id="index_iremark" value="">
			</div>
		</div>
		<div class="wrap wrap8">
		<input type="hidden" value="" name="in_id" id="index_in_id">
			<input value="保存" type="button" class="submit_button" onclick="addLoss()">
		</div>
	</div>
	
	<div class="container easyui-dialog" id="dlg5" style="width:1055px;padding:10px;autoOpen:false;closed:true;height:700px;" title="录入库存">
		<div class="wrap row">
			<div class="col-xs-7">	
			<div class="form-horizontal">
				<div class="form-group">
					<label class="col-xs-2 control-label text-left">产品ID:</label>
					<div class="col-xs-10">
						<input type="text" class="form-control" id="lu_pid" onchange="getProduct()">
						<input type="hidden" value="" id="lu_catid">
					</div>					
				</div>
				<div class="form-group">
					<label class="col-xs-2 control-label text-left">产品名称:</label>
					<div class="col-xs-10">
						<span id="lu_name">产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称产品名称</span>
					</div>
				</div>		
			</div>	
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
					<tr>
						<td >
						<span class="lu_sku"></span><br>
						<span class="lu_specid"></span><br>
						<span class="lu_skuid"></span>
						</td>
						<td><input type="text" class="form-control lu_count"></td>
						<td class="lu_barcode"><a onclick="getbarcode();" >获取库位</a></td>
						<td><input type="checkbox" name="entry" class="lu_is"></td>
					</tr>
				</tbody>
			</table>																
			</div>
			<div class="col-xs-5">
				<img src="https://img.kidsproductwholesale.com/importcsvimg/webpic/img/cl_72/children/banner1.jpg" alt="" class="img-responsive" id="lu_img">
			</div>
		</div>
		<div class="row remark">
			<div class="form-horizon col-xs-12">
				<div class="form-group">
					<label >备注原因</label>
					<label>
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
				<button class="btn btn-success" id="lu_click">保存</button>
			</div>

		</div>
	</div>
	<div class="transparent" >
		<div class="transparent-bg"></div>
		<img src="" alt="" class="img-responsive">
	</div>
	<div class="container easyui-dialog" id="dlg6" style="width:1055px;padding:10px;autoOpen:false;closed:true;" title="未匹配录入">
		<div class="row">
			<div class="form-horizon">
				<div class="form-group row">
					<label class="control-label col-xs-2">淘宝订单/运单号</label>
					<div class="col-xs-5">
						<input type="text" class="form-control">
					</div>
					<div class="col-xs-3">
						<input type="file">
						<button class="btn btn-default">选择未匹配订单</button>
					</div>					
				</div>
			</div>
		</div>
		<div class="row">
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>序号</th>
						<th>产品名称</th>
						<th>产品规格</th>
						<th>订单数量</th>
						<th>实际数量</th>
						<th>库位</th>
						<th>是否录入</th>
					</tr>
				</thead>
				<tbody class="lu_tb_tr">
					<tr>
						<td>1</td>
						<td class="lu_tb_name">产品名称产品名称产品名称产品名称</td>
						<td>12/R12</td>
						<td >10</td>
						<td><input type="text" class="form-control"></td>
						<td>4KR01</td>
						<td><input type="checkbox"></td>
					</tr>
					<tr>
						<td>2</td>
						<td>产品名称</td>
						<td>12/R12</td>
						<td>10</td>
						<td><input type="text" class="form-control"></td>
						<td><span class="gain">获取库位</span></td>
						<td><input type="checkbox"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="row serial">
			<div class="col-xs-3">
				<span>序号：<em>1</em></span>
				<img src="https://img.kidsproductwholesale.com/importcsvimg/webpic/img/cl_72/children/banner1.jpg" alt="" class="img-responsive">
			</div>
			<div class="col-xs-3">
				<span>序号：<em>1</em></span>
				<img src="https://img.kidsproductwholesale.com/importcsvimg/webpic/img/cl_72/children/banner1.jpg" alt="" class="img-responsive">
			</div>
			<div class="col-xs-3">
				<span>序号：<em>1</em></span>
				<img src="https://img.kidsproductwholesale.com/importcsvimg/webpic/img/cl_72/children/banner1.jpg" alt="" class="img-responsive">
			</div>
			<div class="col-xs-3">
				<span>序号：<em>1</em></span>
				<img src="https://img.kidsproductwholesale.com/importcsvimg/webpic/img/cl_72/children/banner1.jpg" alt="" class="img-responsive">
			</div>
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
						<input type="text" class="form-control" id="lu_tb_remark"> 
					</label>
				</div>
			</div>
			<div class="col-xs-12 text-center">
				<button class="btn btn-success">保存</button>
			</div>

		</div>
	</div>
	<div  id="top_toolbar" style="padding: 5px; height: auto">
	<div>
	<span class="top_title">产品检索</span>&nbsp;&nbsp;&nbsp;&nbsp;
	<input type="hidden" value="${param.inid }" id="query_in_id">
	<!-- <span>产品名称<input type="text" id="query_goods_name" value=""></span>&nbsp;&nbsp; -->
	<span>产品ID<input type="text" id="query_goods_pid" value=""></span>&nbsp;&nbsp;
	<select class="easyui-combobox" name="goodscatid" id="query_goodscatid" style="width:15%;" 
	data-options="label:'产品类别:',Height:'2000px',valueField:'goodsCatid',
                    textField:'categoryName', value:'0',selected:true,
                    url: '/cbtconsole/StatisticalReport/getAllInventory',
                    method:'get'">
	</select>&nbsp;&nbsp;
	<span>库存量大于<input type="text" id="query_minintentory" value=""></span>&nbsp;&nbsp;
	<span>库存量小于<input type="text" id="query_maxintentory" value=""></span>&nbsp;&nbsp;
	<input type="button" value="查询" class="button_c" id="query_button"/>
	
	</div>
	
	<br><br>
	<div>
	<span class="top_title">库存修正</span>&nbsp;&nbsp;&nbsp;&nbsp;
	<span><input type="button" class="button_c" id="add_inventory" value="录入库存" onclick="openInventoryEntryView()"></span>&nbsp;&nbsp;
	<span><input type="button"  class="button_c" id="luimport" value="导入未匹配产品"></span>&nbsp;&nbsp;
	<!-- <span><input type="button"  class="button_c" id="update_inventory" value="产品报损调整"></span>&nbsp;&nbsp; -->
	<span><input type="button"  class="button_c" id="add_inventory_online" value="增加线上产品库存"></span>&nbsp;&nbsp;
	<span><span class="title_tile">最近盘点时间 </span><span id="intentory_time"></span></span>
	
	</div>
	<br>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'categoryName',width:80,align:'center'">产品品类</th>
				<th data-options="field:'goodsPid',width:50,align:'center'" >产品ID</th>
				<th data-options="field:'goodsName',width:100,align:'Left'">产品名称</th>
				<th data-options="field:'skuContext',width:100,align:'center'">产品SKU</th>
				<th data-options="field:'carImg',width:80,align:'center'">商品图片</th>
				<th data-options="field:'remaining',width:50,align:'center'">库存数量</th>
				<th data-options="field:'canRemaining',width:50,align:'center'">可用库存</th>
				<th data-options="field:'barcode',width:50,align:'center'">库位</th>
				<th data-options="field:'checkTime',width:50,align:'center'">盘点时间</th>
				<th data-options="field:'operation',width:50,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
		<script type="text/javascript">
           // initData();
		</script>
</body>
</html>