<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
	<title>采购统计报表</title>
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
			         <label >统计报表数据：</label>
			         <table id="purchaseReport" class="imagetable">
		                   <thead>
		                      <tr>
		                        <th width="100" >序号</th>
		                        <th width="150" >月份</th>
		                        <th width="150"  >采购员id</th>
		                        <th width="150"  >管理员</th>
							    <th width="150" >采购数量(个)</th>
								<th width="150" >采购金额(人民币)</th>
								<th width="150" >销售金额(人民币)</th>
		                      </tr>
		                  </thead>
		                  <tbody></tbody>
					</table><br/>
		         </div>
		         <div style="padding:15px;" >
					<label >报表合计：</label>
					<table id="sumReport" class="imagetable">
						<thead>
							<tr>
							<th width="150">开始时段</th>
							<th width="150">结束时段</th>
			                <th width="150" >总采购数量(人民币)</th>
			                <th width="150" >采购总金额(人民币)</th>
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
	var day = new Date(edy, edm, 0).getDate();
	var startdate = sty+"-"+stm+"-01 00:00:00";
	var enddate = edy+"-"+edm+"-"+day+" 23:59:59";
	
	$("#purchaseReport tbody").html("");
	$("#sumReport tbody").html("");
	$.ajax({
		url : "/cbtconsole/StatisticalReport/selectPurchaseByDate?startdate="+startdate+"&enddate="+enddate+"&rand="+Math.random(),
		type:"POST",
		dataType: "json",
		success: function(data){
			if(data.ok){
			var list = data.data.purchaseReport;
			var obj = data.data.obj;
				for(var i=0;i<list.length;i++){
					$("#purchaseReport tbody").append("<tr><td>"+(i+1)+"</td>"+
							"<td>"+list[i].years+"年"+list[i].mon+"月</td>"+
							"<td>"+list[i].buyid+"</td>"+
							"<td>"+list[i].admName+"</td>"+
							"<td>"+list[i].num+"</td>"+
							"<td>"+(Math.round(list[i].je*100)/100)+"</td>"+
							"<td>"+(Math.round(list[i].saleje*100)/100)+"</td>"+
							"</tr>");
				}
				$("#sumReport tbody").append("<tr><td>"+sty+"年"+stm+"月</td>"+
						"<td>"+edy+"年"+edm+"月</td>"+
						"<td>"+obj[0]+"</td>"+
						"<td>"+(Math.round(obj[1]*100)/100)+"</td></tr>");
			}else{
				$("#purchaseReport tbody").append("<tr><td colspan='6' style='text-align:center;color:red;'>没有数据</td></tr>");
				$("#sumReport tbody").append("<tr><td colspan='2' style='text-align:center;color:red;'>没有数据</td></tr>");
			}
		},
		error:function(XMLResponse){
			alert(XMLResponse.responseText);
		}
	});
});
</script>
</html>