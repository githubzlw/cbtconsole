<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>拆包统计、采购包裹统计</title>
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">
</head>
<body>
  <div>
	 
	<div>
	  <form id="adduserForm" name="adduserForm" action="" method="post">
		<div class="box box-solid" >
		  <div class="box-header with-border">
             <h4>查询条件</h4>
	         </div>
	         <div class="box-body">
	           <div class="col-xs-4 form-group">
					<label for="nickname" >开始时间<font color="red">*</font>：</label>
					<select id="startYear"  style="width:100px;"></select> 年
	            	<select id="startMonth" style="width:100px;"></select> 月
		    	</div>
		   		<div class="col-xs-4 form-group">
					<label for="nickname" >结束时间<font color="red">*</font>：</label>
					<select id="endYear" style="width:100px;"></select> 年
	                <select id="endMonth" style="width:100px;"></select> 月
		        </div>
				<div class="col-xs-4 form-group">
					<label for="nickname" > &nbsp;&nbsp;&nbsp;&nbsp;  </label>
					<div class="btn btn-primary pull-right" id="pgSearch" style="margin-right: 50px;">
		            	<i class="fa fa-search">查 询</i>
					</div>
				</div>
	         </div>
	         
	         <div  style="padding:15px;">
	         	<div style="width:650px;float:left;">
		         <label >拆包统计：</label>
		         <table id="unpack" class="imagetable">
	                   <thead>
	                      <tr>
	                        <th width="150" >序号</th>
	                        <th width="150" >月份</th>
	                        <th width="150" >用户名</th>
						    <th width="150" >拆包数量(个)</th>
	                      </tr>
	                  </thead>
	                  <tbody></tbody>
				</table><br/>
	         	</div>
	         	<div>
		         <label >采购包裹统计：</label>
		         <table id="purchase" class="imagetable">
	                   <thead>
	                      <tr>
	                        <th width="150" >序号</th>
	                        <th width="150" >月份</th>
	                        <th width="150" >用户名</th>
						    <th width="150" >包裹数量(个)</th>
	                      </tr>
	                  </thead>
	                  <tbody></tbody>
				</table><br/>
	         	</div>
	         </div>
	         
	           <div style="padding:15px;" >
					<label >报表合计：</label>
					<table id="sumReport" class="imagetable">
						<thead>
							<tr>
							<th width="160">开始时段</th>
							<th width="160">结束时段</th>
			                <th width="160" >总拆包数量(个)</th>
			                <th width="160" >总采购包裹数量(个)</th>
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
<script type="text/javascript">
$(document).ready(function(){
	var date = new Date();
	var nowyear = date.getFullYear();
	for(var sty =2015;sty<=nowyear;sty++){
		$("#startYear").append("<option value="+sty+">"+sty+"</option>");
		$("#endYear").append("<option value="+sty+">"+sty+"</option>");
	}
	for(var stm = 1;stm<=12;stm++){
		$("#startMonth").append("<option value="+stm+">"+stm+"</option>");
		$("#endMonth").append("<option value="+stm+">"+stm+"</option>");
	}
});

$("#startYear").change(function(){
	var date = new Date();
	var nowyear = date.getFullYear();
	var nowmonth  = date.getMonth()+1;
	var startYear = $("#startYear").val();
	if(startYear==nowyear){
		$("#startMonth").empty();
		for(var stm = 1;stm<=nowmonth;stm++){
			$("#startMonth").append("<option value="+stm+">"+stm+"</option>");
		}
	}else{
		$("#startMonth").empty();
		for(var stm = 1;stm<=12;stm++){
			$("#startMonth").append("<option value="+stm+">"+stm+"</option>");
		}
	}
}); 

$("#endYear").change(function(){
	var date = new Date();
	var nowyear = date.getFullYear();
	var nowmonth  = date.getMonth()+1;
	var endYear = $("#endYear").val();
	if(endYear==nowyear){
		$("#endMonth").empty();
		for(var stm = 1;stm<=nowmonth;stm++){
			$("#endMonth").append("<option value="+stm+">"+stm+"</option>");
		}
	}else{
		$("#endMonth").empty();
		for(var stm = 1;stm<=12;stm++){
			$("#endMonth").append("<option value="+stm+">"+stm+"</option>");
		}
	}
}); 

$("#pgSearch").click(function(){
	var sty = $("#startYear").val();
	var stm = $("#startMonth").val();
	var edy = $("#endYear").val();
	var edm = $("#endMonth").val();
	if(sty=="" || stm=="" || edy=="" || edm==""){
		alert("请选择开始结束时间");
		return;
	}
	if(stm.length==1){
		stm = "0"+stm;
	}
	if(edm.length==1){
		edm = "0"+edm;
	}
	var day = new Date(edy, edm, 0).getDate();
	var startdate = sty+"-"+stm+"-01 00:00:00";
	var enddate = edy+"-"+edm+"-"+day+" 23:59:59";
	
	$("#unpack tbody").html("");
	$("#purchase tbody").html("");
	$.ajax({
		url : "/cbtconsole/StatisticalReport/selectUnpackPurchase?startdate="+startdate+"&enddate="+enddate+"&rand="+Math.random(),
		type:"POST",
		dataType: "json",
		success: function(data){
			if(data.ok){
			var unpack = data.data.unpack;
			var purchase = data.data.purchase;
			var unpackNum = data.data.unpackNum;
			var purchaseNum = data.data.purchaseNum;
				for(var i=0;i<unpack.length;i++){
					$("#unpack tbody").append("<tr><td>"+(i+1)+"</td>"+
							"<td>"+unpack[i].years+"年"+unpack[i].mon+"月</td>"+
							"<td>"+((purchase[i].username=='' || purchase[i].username==null) ? '--' : purchase[i].username)+"</td>"+
							"<td>"+unpack[i].num+"</td>"+
							"</tr>");
				}
				for(var i=0;i<purchase.length;i++){
					$("#purchase tbody").append("<tr><td>"+(i+1)+"</td>"+
							"<td>"+purchase[i].years+"年"+purchase[i].mon+"月</td>"+
							"<td>"+((purchase[i].username=='' || purchase[i].username==null) ? '--' : purchase[i].username)+"</td>"+
							"<td>"+purchase[i].num+"</td></tr>");
				}
				$("#sumReport tbody").append("<tr><td>"+startdate+"</td><td>"+enddate+"</td><td>"+unpackNum+"</td><td>"+purchaseNum+"</td></tr>")
			}else{
				$("#unpack tbody").append("<tr><td colspan='4' style='text-align:center;color:red;'>没有数据</td></tr>");
				$("#purchase tbody").append("<tr><td colspan='4' style='text-align:center;color:red;'>没有数据</td></tr>");
			}
		},
		error:function(XMLResponse){
			alert(XMLResponse.responseText);
		}
	});
});
</script>
</html>