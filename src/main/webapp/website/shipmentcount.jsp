<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="script/style.css" type="text/css">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>出货统计</title>
</head>
<script type="text/javascript">
$(function(){
	fnInquiry();
});

function fnInquiry(){
	var datas = {};
	var paytimestart = $("#paytimestart").val();
	var paytimeend = $("#paytimeend").val();
	var days = $("#days").val();
	if(paytimestart != ''){
		if(paytimeend == ''){
			paytimeend = date.getFullYear()+"-"+(date.getMonth() + 1)+"-"+date.getDate();
		}
		datas = {'paytimestart':paytimestart,'paytimeend':paytimeend,days:days};
	}
	$.ajax({
		type : 'GET',
		async : true,
		url : '../shipment/shipmentCount',
		data :datas,
		dataType : 'text',
		success : function(data){
			var json;
			try {
				json = eval(data)[0];
			} catch (e) {
				window.location.href="/cbtconsole";
			}
			var json = eval(data)[0];
			console.log(json);
			console.log(json.res);
			if(json.res < 1){
				$("#alertfont").show();
				if(json.res == -1){
					$("#alertfont").html("<a href='/cbtconsole'>请登录！</a>");
				}else{
					$("#alertfont").html("您无权限查看！");
				}
				return;
			}else{
				$("#alertfont").hide();
			}
			if(json.avg == 0){
				$("#intervals").html("无订单");
			}else{
				$("#intervals").html(json.avg);
// 				$("#order_buy_time").html(json.order_buy_time);
// 				$("#goods_buy_time").html(json.goods_buy_time);
// 				$("#ch_time").html(json.ch_time);
// 				$("#details_count").html(json.details_count);
			}
			var orderinfo = json.orderinfo;
			
			var orders =  orderinfo.length;
			if(orders>0){
				$("#tab tbody").html("");
				$("#total tbody").html("");
			}
			for (var i = 0; i < orderinfo.length; i++) {
				$("#tab tbody").append("<tr><td><a target='_blank' href='/cbtconsole/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo="+orderinfo[i][0]+"&state="+orderinfo[i][5]+"&username="+orderinfo[i][4]+"&paytime="+orderinfo[i][1]+"'>"+
						orderinfo[i][0]+"</a></td><td>"+orderinfo[i][1]+"</td><td>"+orderinfo[i][2]+"</td><td>"+orderinfo[i][3]+"</td><td>"+orderinfo[i][4]+"</td><td>"+orderinfo[i][5]+"</td><td>"+orderinfo[i][6]+"</td></tr>");
			}
			var avgTime = json.avgTime;
			var orderCount = json.orderCount;
			var delaysRate = json.delaysRate;
			$("#total tbody").append("<tr><td>"+orderCount+"</td><td>"+avgTime+"</td><td>"+delaysRate+"</td></tr>")
		}
	})
}

function tj_query(){
	var tj_days=$("#tj_days").val();
	datas = {'tj_days':tj_days};
	$.ajax({
		type : 'GET',
		async : true,
		url : '../shipment/PurchaseStatisticsInquiry',
		data :datas,
		dataType : 'text',
		success : function(data){
			var json;
			try {
				json = eval(data)[0];
			} catch (e) {
				window.location.href="/cbtconsole";
			}
			var json = eval(data)[0];
			var orderinfo = json.list;
			var orders =  orderinfo.length;
			if(orders>0){
				$("#tab1 tbody").html("");
			}
			for (var i = 0; i < orderinfo.length; i++) {
				$("#tab1 tbody").append("<tr><td>"+orderinfo[i][0]+"</td><td>"+orderinfo[i][1]+"</td><td>"+orderinfo[i][2]+"</td><td>"+orderinfo[i][3]+"</td><td>"+orderinfo[i][4]+"</td></tr>");
			}
		}
	})
}
function exportExcel(){
	  var str = "";
	  var tab = $("#tab tr").length;
	  if(tab < 0){
		  alert("无数据导出");
		  return;
	  }
	  for(var i = 0;i < tab; i++){
		var tr = $("#tab tr").eq(i); 
		var td_th = "td";
		var td = tr.find("td").length;
		if(td < 1){
			td_th = "th";
			td = tr.find("th").length;
		}
		for(var j = 0;j < td; j++){
			var td_ = tr.find(td_th).eq(j);
			var html = td_.html().replace(/,/g,".").replace(/&nbsp;/g," ");
			if(j==11){
				html="'"+html;
			}
			if(html == "img"){
				continue;
			}
			if(typeof(td_.find("a").attr("href")) != "undefined"){
				html = td_.find("a").html();
			}
			str += html + ",";
		}
	  	str += "\n";
	  }
	  str = encodeURIComponent(str);
	  var uri = 'data:text/csv;charset=utf-8,\ufeff' + str;
	  var downloadLink = document.createElement("a");
	  downloadLink.href = uri;
	  downloadLink.weight="100";
	  downloadLink.download = "完成出货订单统计.csv";
	  document.body.appendChild(downloadLink);
	  downloadLink.click();
	  document.body.removeChild(downloadLink);
	  }

</script>
<body>
<div>
		<div>
		<form id="adduserForm" name="adduserForm" action="" method="post">
			<div class="box box-solid" >
			<div class="box-header with-border">
				<h4>查询条件</h4>
				<div class="box-body">
					<div class="col-xs-4 form-group">
						<label for="nickname">开始时间<font color="red">*</font>：</label>
						<input id="paytimestart" name="startdate" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value=""  >
		                <label for="nickname">结束时间<font color="red">*</font>：</label>
						<input id="paytimeend" name="startdate" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value=""  >
		            </div>
		            <div class="col-xs-2 form-group">
		             <label for="nickname">延迟天数<font color="red">*</font>：</label>
						<select  style="width:100px;" name="" id="days">
	                      <option value="">请选择</option>
	                      <option value="1">大于1</option>
	                      <option value="3">大于3</option>
	                      <option value="5">大于5</option>
                        </select>
		            </div>
		           
		            <div class="col-xs-2 form-group">
						<label for="nickname"> &nbsp;&nbsp;&nbsp;&nbsp;  </label>
						<div class="btn btn-primary pull-right" id="pgSearch" style="margin-right: 50px;">
		                    <i class="fa fa-search" onclick="exportExcel();">导出</i>
						</div>
						<div class="btn btn-primary pull-right" id="pgSearch" style="margin-right: 50px;">
		                    <i class="fa fa-search" onclick="fnInquiry()">查 询</i>
						</div>
		           </div>
				</div>
				</div>
					<div  style="padding:15px;float:left;width:50%">
					
					<div class="box-body">
<!-- 					<div id="div_show"> -->
<!-- 					  <label for="nickname">(最近4个月统计数据)</label> -->
<!-- 					  <label for="nickname">订单平均采购完成时间:<font  style="color: red;" id="order_buy_time">0</font >天</label> -->
<!-- 					  <label for="nickname">商品平均采购完成时间:<font  style="color: red;" id="goods_buy_time">0</font >天</label> -->
<!-- 					  <label for="nickname">平均总出货时间:<font  style="color: red;" id="ch_time">0</font >天</label> -->
<!-- 					  <label for="nickname">订单中商品数量:<font  style="color: red;" id="details_count">0</font >个</label> -->
<!-- 					</div> -->
					<label for="nickname">平均时间间隔:<font  style="color: red;" id="intervals"></font></label>
				</div>
			         <table id="tab" class="imagetable">
		                   <thead>
		                      <tr>
		                        <th width="150" >订单号</th>
<!-- 		                        <th width="150" >平均采购完成时间</th> -->
<!-- 		                        <th width="150" >订单中平均产品数量</th> -->
		                        <th width="150" >支付时间</th>
							    <th width="150" >发货时间</th>
							    <th width="150">订单约定时效</th>
							    <th width="150">发货用时(实际时效)</th>
							    <th width="150">延迟天数</th>
		                        <th width="100" >销售负责人</th>
		                      </tr>
								
		                  </thead>
		                  <tbody>
		                  	 
		                  </tbody>
					</table><br/>
					
					
					
					 <table id="total" class="imagetable">
		                   <thead>
		                      <tr>
		                        <th width="200" >总订单数(合并订单的主单)</th>
		                        <th width="150" >平均时效</th>
							    <th width="150" >延迟占比</th>
		                      </tr>
		                  </thead>
		                  <tbody>
		                  	 
		                  </tbody>
					</table><br/>
					<h3 style="display: none" id="alertfont">您无权限查看！</h3>
		         </div>
		         <div style="float:left;margin-left:5%;width:45%">
				     <div class="col-xs-2 form-group">
		             <label for="nickname">统计时间<font color="red">*</font>：</label>
						<select  style="width:100px;" name="" id="tj_days">
	                      <option value="0">请选择</option>
	                      <option value="30">最近一个月</option>
	                      <option value="90">最近三个月</option>
	                      <option value="180">最近半年</option>
	                      <option value="360">最近一年</option>
                        </select>
		            </div>
				         <div class="btn btn-primary pull-right" id="tjSearch" style="margin-right: 50px;">
		                    <i class="fa fa-search" onclick="tj_query();">查 询</i>
						</div>
		         		 <table id="tab1" class="imagetable" style="margin-top:60px;">
		                   <thead>
		                      <tr>
		                      	<th width="100" >月份</th>
		                        <th width="200" >订单平均采购完成时间(天)</th>
		                        <th width="200" >商品平均采购完成时间(天)</th>
							    <th width="200" >平均总出货时间(天)</th>
							    <th width="200"> 订单中商品数量(个)</th>
		                      </tr>
								
		                  </thead>
		                  <tbody>
		                  	 
		                  </tbody>
					</table>
		         
		         
		         </div>
			</div>
		</form>
		</div>
	</div>

 
</body>
</html>