<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>商品购买报表</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">
<script type="text/javascript">
var searchReport = "/cbtconsole/StatisticalReport/selectBuyGoods"; //报表查询
</script>
<style type="text/css">
<style>
.displaynone{display:none;}
</style>


</head>
<body text="#000000">
<div>
 <div>
   <form id="adduserForm" name="adduserForm" action="" method="post">
      <div class="box box-solid" >
      
      <div class="box-header with-border">
             <h4>查询条件</h4>
         </div>
         <div class="box-body">
              <div class="col-xs-4 form-group">
                  <label for="nickname" >确认采购时间年份选择<font color="red">*</font>：</label>
                      <select class="form-control" name="" id="year" >
                      <option value="2017">2017</option>
                      <option value="2018">2018</option>
                      </select>
              </div>
              <div class="col-xs-4 form-group">
                  <label for="nickname" >确认采购时间月份选择<font color="red">*</font>：</label>
                     <select class="form-control" name="" id="month">
	                   <option value="1">1</option>
                      <option value="2">2</option>
                      <option value="3">3</option>
                      <option value="4">4</option>
                      <option value="5">5</option>
                      <option value="6">6</option>
                      <option value="7">7</option>
                      <option value="8">8</option>
                      <option value="9">9</option>
                      <option value="10">10</option>
                      <option value="11">11</option>
                      <option value="12">12</option>
                      </select>
              </div>
              <div class="col-xs-4 form-group">
                  <label for="nickname" >排行条件：</label>
                       <select class="form-control" name="" id="orderName">
	                      <option value="salesm">销售次数</option>
	                      <option value="saless">销售总量</option>
	                      <option value="salesp">销售额</option>
                      </select>
               </div>
               <div class="col-xs-4 form-group">
               	     <label for="nickname" > &nbsp;&nbsp;&nbsp;&nbsp;  </label>
		             <div class="btn btn-primary pull-right" id="pgSearch" onclick="fn(1)" style="margin-right: 5px;">
		                    <i class="fa fa-search">查 询</i>
		             </div>
              </div>
              <div class="box-body">
	              <div class="col-xs-4 form-group">
		              <div class="btn btn-primary pull-right" id="pgExport">
		                     <i class="fa  fa-upload">导出报表</i>
		                     
		              </div>
		          </div>
		      </div>
         </div>
         </div>
         <div  style="padding:15px;">
         <label >商品购买报表数据：</label>
         <table id="sumReport" class="imagetable">
                    <thead>
                       <tr>
	                        <td width="50" >序号</td>
	                        <td width="150" >商品类别</td>
	                        <td width="200" >商品名称</td>
	                        <td width="80" >销售数量</td>
	                        <td width="120" >销售价格($)</td>
						    <td width="80" >采购次数</td>
						    <td width="80" >采购总数</td>
							<td width="120" >采购价格(￥)</td>
							<td width= "100">采购总成本(￥)</td>
                       </tr>
                   </thead>
                   <tbody>
				</tbody>
		</table>
         </div>
    </form>  
	  <div class="pages" id="pages">
	               <span>&nbsp;&nbsp;总条数：<font id="counto"></font>&nbsp;&nbsp;总页数：<font id="count"></font></span>&nbsp;&nbsp;当前页：<font id="page"></font>&nbsp;&nbsp;
	               <button onclick="prepage()">上一页</button>&nbsp;<button  onclick="nextpage()">下一页</button>&nbsp;&nbsp;&nbsp;&nbsp;
	               跳至<input type="text" value="1" id="topage" style="width:50px;">页 <input type="button" value="跳" onclick="jump();">
	          </div>
   </div>
 </div>
</body>
<script type="text/javascript">

function round2(number,fractionDigits){  //实现四舍五入保留小数位
    with(Math){  
        return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);  
    }  
} 

function prepage(){
	var now = $("#page").text();
	if(now=="" || now==null){
		alert("没有数据");
		return ;
	}
	if(now=="1"){
		alert("已经达到第一页");
	}
	var p = parseInt(now)-1;
	fn(p);
}

function nextpage(){
	var max = $("#count").text();
	var now = $("#page").text();
	if(now=="" || now==null){
		alert("没有数据");
		return ;
	}
	if(now==max){
		alert("已经达到最后一页");
	}
	var p = parseInt(now)+1;
	fn(p);
}

function jump(){
	var p = $("#topage").val();
	var max = $("#count").text();
	if(isNaN(p)){
		alert("请输入正确页码");
		return ;
	}
	if(max=="" || max==null){
		alert("没有数据");
		return ;
	}
	if(p<1 || p>max){
		alert("页码超出范围");
		return;
	}
	fn(p);
}
//查询报表
var page = 1;
function fn(val){
	$("#sumReport tbody").html("");
	var orderName = $('#orderName').val();
	var year=$("#year").val();
	var month=$("#month").val();
	choiseDay = new Date(year,month,0).getDate();
	var start = year+'-'+month+'-1 00:00:00';
	var end = year+'-'+month+'-'+choiseDay+ ' 23:59:59';
	  if(val!=""){
		  page = val;
	  }

	jQuery.ajax({
		url:searchReport,
        data:{
        	  "orderName":orderName,
        	  "start":start,
        	  "end":end,
        	  "page":page
        	},
        type:"post",
        success:function(res){
        	    var json = eval(res)[0];
				var pre = json.pre;
				 for (var i = 0; i < pre.length; i++) {
					 var preferential = pre[i];
					 var htm_ ='';
	                    htm_ += '<tr>                                       ';
	                	htm_ += '<td>'+(i+1)            +'</td>';
	                	htm_ += '<td>'+preferential.category    +'</td>';
	                	htm_ += '<td>'+preferential.name    +'</td>';
	                	htm_ += '<td>'+preferential.saleNums    +'</td>';
	                	htm_ += '<td>'+preferential.price    +'</td>';
	                	htm_ += '<td>'+preferential.count    +'</td>';
	                	htm_ += '<td>'+preferential.buycount    +'</td>';
	                	htm_ += '<td>'+preferential.buyprices    +'</td>';
	                	htm_ += '<td>'+preferential.buySums    +'</td>';
	                	htm_ += '</tr>                                      ';
	                $("#sumReport").append(htm_);
				 }
				  count = Math.ceil(json.count / 20);
 				  $("#count").html(count);
 				  $("#counto").html(json.count);
 				  page = parseInt(json.page, 0);
 				  $("#page").html(page);
        },
    	error:function(e){
    		alert("没有数据！");
    	}
    });
};

//导出报表
$("#pgExport").click(function(){
	//生成报表
	var orderName = $('#orderName').val();
	var year=$("#year").val();
	var month=$("#month").val();
	choiseDay = new Date(year,month,0).getDate();
	var start = year+'-'+month+'-1 00:00:00';
	var end = year+'-'+month+'-'+choiseDay+ ' 23:59:59';
	window.location.href ="/cbtconsole/StatisticalReport/export1?orderName="+orderName+"&start="+start+"&end="+end;
});

</script>
</html>