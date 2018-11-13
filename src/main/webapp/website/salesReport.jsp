<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>销售统计报表</title>
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
	                  <label for="nickname" >年份选择 <font color="red">*</font>：</label>
                      <select class="form-control" name="" id="rYear" >
                      </select>
	              </div>
	              <div class="col-xs-4 form-group">
	                  <label for="nickname" >月份选择 <font color="red">*</font>：</label>
	                     <select class="form-control" name="" id="rMonth">
		                      <option>请选择</option>
	                      </select>
	              </div>
	              <div class="col-xs-4 form-group" style="display:none;">
                  <label for="nickname" >日期选择</label>
                      <select class="form-control" name="" id="rDay">
	                      <option>请选择日期</option>
                      </select>
				  </div>
	              <div class="col-xs-4 form-group">
	                  <label for="nickname" >排行条件：</label>
	                      <select class="form-control" name="" id="orderName" >
							   <option value="ordertable3.orderpaytime" selected="selected">订单支付时间</option> 
		                      <option value="ordertable6.sl">订单数量</option>
		                      <option value="ordertable3.payprice">消费金额</option>
	                      </select>
	              </div>

               	<div class="col-xs-4 form-group">
               	     <label for="nickname" > &nbsp;&nbsp;&nbsp;&nbsp;  </label>
		             <div class="btn btn-primary pull-right" id="pgSearch" style="margin-right: 5px;">
		                    <i class="fa fa-search">查 询</i>
		             </div>
              	</div>
              	<div class="box-body">
	            <div class="col-xs-4 form-group">
		              <div class="btn btn-primary pull-right" id="addReport">
		                     <i class="fa fa-fw fa-plus">重新生成报表</i>
		              </div>
		               <div class="btn btn-primary pull-right" id="addReport1" style="display:none;">
		                    <i class="fa fa-fw fa-plus">报表生成中...</i>
		              </div>
	              </div> 
	              <div class="col-xs-4 form-group">
		              <div class="btn btn-primary pull-right" id="pgExport">
		                     <i class="fa  fa-upload">导出报表</i>
		              </div>
		          </div>  
		      </div>
         	</div>
         	
         <div  style="padding:15px;">
	         <label >销售报表数据：</label>
	         <table id="salesReport" class="imagetable">
                   <thead>
                      <tr>
                        <th width="80" >序号</th>
                        <th width="80" >用户id</th>
                        <th width="100"  >管理员</th>
                        <th width="130"  >本月消费(美金)</th>
					    <th width="180" >本月首单支付时间</th>
						<th width="120" >本月订单数量(个)</th>
						<th width="130" >总消费金额(美金)</th>
						<th width="120" >总下单数量(个)</th>
						<th width="180" >首次订单支付时间</th>
						<th width="180" >上月最后订单支付时间</th>
						<th width="120" >新用户标识</th>
                      </tr>
                  </thead>
                  <tbody>
				</tbody>
			</table><br/>
         </div>
         
         <div style="text-align: center; " id="pagediv">
		 	共查到<span id="datacount">0</span>数据&nbsp;&nbsp;
		 	<input type="button" id="prePage" value="上一页"/>&nbsp;
		 	第<span id="nowPage">1</span>页/共<span id="allPage">0</span>页
		 	<input type="button" id="nextPage" value="下一页"/>&nbsp;&nbsp;
		 	跳至<input type="text" id="toPage" style="width:50px;"/>页 <input type="button" value="Go" id="jumpPage"/>
		 </div>

		<div style="padding:15px;" >
			<label >报表合计：</label>
			<table id="sumReport" class="imagetable">
				<thead>
					<tr>
	                <th width="120" >用户数量(个)</th>
	                <th width="150" >本月总消费(美金)</th>
	                <th width="150"  >本月总订单数(个)</th>
					<th width="150" >历史总消费(美金)</th>
	                <th width="150"  >历史订单数(个)</th>
					<th width="150" >新用户数量(个)</th>
					<th width="150" >老用户数量(个)</th>
					</tr>
				</thead>
				<tbody>
			</tbody>
			</table>
		</div>

				 <div style="display:none;">
					<input type="hidden" id="type" value="${type}">
					<input id="timeFrom" type="hidden">
					<input id="timeTo" type="hidden">
				 </div>
			</div>
		</form>
	</div>
	</div> 
</body>
<script type="text/javascript">
//导出
$("#pgExport").click(function(){
	var choseYear =$('#rYear').val();
	var choseMonth=$('#rMonth').val();
	var orderName = $("#orderName").val();
	var type =$('#type').val();
	if(choseYear == '-1'){
		alert('请选择年份！');
		return;
	}else if(choseMonth == '-1'){
		alert('请选择月份！');
		return;
	}
	var day =new Date(choseYear, choseMonth, 0).getDate();
	var startTime = choseYear+"-"+choseMonth+"-1 0:00:00";
	var endTime = choseYear+"-"+choseMonth+"-"+day+" 23:59:59";
	window.location.href ="/cbtconsole/StatisticalReport/exportSales?startTime="+startTime+"&endTime="+endTime+"&type="+type+"&orderName"+orderName;
}); 

$("#prePage").click(function(){
	var nowpage = $("#nowPage").text();
	if(parseInt(nowpage) <=1){
		alert("已经到达第一页");
		return ;
	}
	searchReport(parseInt(nowpage)-1);
});

$("#nextPage").click(function(){
	var nowpage = $("#nowPage").text();
	var maxpage = $("#allPage").text();
	if(parseInt(nowpage)>=parseInt(maxpage)){
		alert("已经到达最后一页");
		return;
	}
	searchReport(parseInt(nowpage)+1);
});

$("#jumpPage").click(function(){
	var maxpage = $("#allPage").text();
	var page = $("#toPage").val();
	if(isNaN(page)){
		alert("请输入正确的页码");
		return ;
	}
	if(parseInt(page)<1 || parseInt(page)>parseInt(maxpage)){
		alert("页码超出范围");
		return;
	}
	searchReport(page);
});

//查询报表
$("#pgSearch").click(function(){
	searchReport(1);
});

function searchReport(page){
	var year = $("#rYear").val();
	var mon = $("#rMonth").val();
	var odn= $("#orderName").val();
	var type = $("#type").val();
	if(year == '-1'){
		alert('请选择年份！');
		return;
	}else if(mon == '-1'){
		alert('请选择月份！');
		return;
	}
	var day = new Date(year, mon, 0).getDate();
	var startTime = year+"-"+mon+"-1 0:00:00";
	var endTime = year+"-"+mon+"-"+day+" 23:59:59";
	$("#salesReport tbody").html("");
	$("#sumReport tbody").html("");
	$.ajax({
		url : "/cbtconsole/StatisticalReport/selectSalesReport?startTime="+startTime+"&endTime="+endTime+"&orderName="+odn+"&type="+type+"&page="+page,
		type:"POST",
		dataType: "json",
		success:function(data){
			if(data.ok){
				var list = data.data.saleReport;
				var info = data.data.reportSalesInfo;
				
				var count = list[0].datacount;
				var allpage = 0;
				if(count%20==0){
					allpage = count/20;
				}else{
					allpage = parseInt(count/20) + 1;
				}
				$("#datacount").html(count);
				$("#nowPage").html(page);
				$("#allPage").html(allpage);
				for(var i=0; i<list.length; i++){
					$("#salesReport tbody").append("<tr><td>"+((page-1)*20+i+1)+"</td>"+
							"<td>"+list[i].user_id+"</td>"+
							"<td>"+list[i].admName+"</td>"+
							"<td>"+(Math.round(list[i].payprice*100)/100)+"</td>"+
							"<td>"+formatDateTime(list[i].orderpaytime)+"</td>"+
							"<td>"+list[i].sl+"</td>"+
							"<td>"+(Math.round(list[i].zje*100)/100)+"</td>"+
							"<td>"+list[i].zsl+"</td>"+
							"<td>"+formatDateTime(list[i].firstbuy)+"</td>"+
							"<td>"+formatDateTime(list[i].scgm)+"</td>"+
							"<td style='color:"+(list[i].newsign=='NEW' ? 'red' : 'black')+";'>"+list[i].newsign+"</td>"+
							"</tr>");
				}
				$("#sumReport tbody").append("<tr><td>"+info.userCount+"</td>"+
						"<td>"+(Math.round(info.cost*100)/100)+"</td>"+
						"<td>"+info.orderCount+"</td>"+
						"<td>"+(Math.round(info.allCost*100)/100)+"</td>"+
						"<td>"+info.allOrder+"</td>"+
						"<td>"+info.newUser+"</td>"+
						"<td>"+info.oldUser+"</td>"+
						"</tr>");
			}else{
				$("#salesReport tbody").append("<tr><td colspan='11' style='color:red;text-align:center;'>没有数据</td></tr>");
				$("#sumReport tbody").append("<tr><td colspan='7' style='color:red;text-align:center;'>没有数据</td></tr>");
			}
		},
		error:function(){
			alert("Error");
		}
	});
}

//查询 插入 数据
$("#addReport").click(function(){
	searchInsertReport();
});

function searchInsertReport(){
	var year = $("#rYear").val();
	var mon = $("#rMonth").val();
	var odn= $("#orderName").val();
	var type = $("#type").val();
	if(year == '-1'){
		alert('请选择年份！');
		return;
	}else if(mon == '-1'){
		alert('请选择月份！');
		return;
	}
	var day = new Date(year, mon, 0).getDate();
	var startTime = year+"-"+mon+"-1 0:00:00";
	var endTime = year+"-"+mon+"-"+day+" 23:59:59";
	
	$("#sumReport tbody").html("");
	$("#salesReport tbody").html("");
	$.ajax({
		url : "/cbtconsole/StatisticalReport/insertSalesReport?startTime="+startTime+"&endTime="+endTime+"&orderName="+odn+"&type="+type,
		type:"POST",
		dataType: "json",
		success:function(data){
			if(data.ok){
				var list = data.data.saleReport;
				var info = data.data.reportSalesInfo;
				
				var count = info.userCount;
				var allpage = 0;
				if(count%20==0){
					allpage = count/20;
				}else{
					allpage = parseInt(count/20) + 1;
				}
				$("#datacount").html(count);
				$("#nowPage").html(1);
				$("#allPage").html(allpage);
				
				$("#datacount").html(list.length);
				for(var i=0; i<list.length; i++){
					$("#salesReport tbody").append("<tr><td>"+(i+1)+"</td>"+
							"<td>"+list[i].user_id+"</td>"+
							"<td>"+list[i].admName+"</td>"+
							"<td>"+(Math.round(list[i].payprice*100)/100)+"</td>"+
							"<td>"+formatDateTime(list[i].orderpaytime)+"</td>"+
							"<td>"+list[i].sl+"</td>"+
							"<td>"+(Math.round(list[i].zje*100)/100)+"</td>"+
							"<td>"+list[i].zsl+"</td>"+
							"<td>"+formatDateTime(list[i].firstbuy)+"</td>"+
							"<td>"+formatDateTime(list[i].scgm)+"</td>"+
							"<td style='color:"+(list[i].newsign=='NEW' ? 'red' : 'black')+";'>"+list[i].newsign+"</td>"+
							"</tr>");
				}
				$("#sumReport tbody").append("<tr><td>"+info.userCount+"</td>"+
						"<td>"+(Math.round(info.cost*100)/100)+"</td>"+
						"<td>"+info.orderCount+"</td>"+
						"<td>"+(Math.round(info.allCost*100)/100)+"</td>"+
						"<td>"+info.allOrder+"</td>"+
						"<td>"+info.newUser+"</td>"+
						"<td>"+info.oldUser+"</td>"+
						"</tr>");
			}else{
				$("#salesReport tbody").append("<tr><td colspan='11' style='color:red;text-align:center;'>没有数据</td></tr>");
				$("#sumReport tbody").append("<tr><td colspan='7' style='color:red;text-align:center;'>没有数据</td></tr>");
			}
		},
		error:function(){
			alert("Unknown Error.");
		}
	});
}

function formatDateTime(datetime){
	if(datetime.indexOf(".")>0){
		var date = datetime.split(".")[0];
		return date;
	}else{
		return datetime;
	}
}
</script>
</html>