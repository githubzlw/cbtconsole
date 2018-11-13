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
<title>订单分类报表</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">

<script type="text/javascript">
var searchReport = "/cbtconsole/StatisticalReport/selectReport2"; //报表查询
var downloadReport ="/cbtconsole/StatisticalReport/showReport"; //导出报表
var showGoods = "/cbtconsole/StatisticalReport/showGoods"; //显示商品
var showGoodsByOrder = "/cbtconsole/StatisticalReport/showGoodsByOrder"; 
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
                  <label for="nickname" >年份选择<font color="red">*</font>：</label>
                      <select class="form-control" name="" id="rYear" >
                      </select>
              </div>
              <div class="col-xs-4 form-group">
                  <label for="nickname" >月份选择<font color="red">*</font>：</label>
                     <select class="form-control" name="" id="rMonth">
	                      <option value="-1">请选择</option>
                      </select>
              </div>
              <div class="col-xs-4 form-group" style="display:none;">
                  <label for="nickname" >日期选择</label>
                      <select class="form-control" name="" id="rDay">
	                      <option>请选择日期</option>
                      </select>
              </div>
              <div class="col-xs-4 form-group" style="display:none;">
                  <label for="nickname" >周选择：</label>
                      <select class="form-control" name="" id="rWeek">
	                      <option>请选择周</option>
                      </select>
              </div>
              <div class="col-xs-4 form-group">
                  <label for="nickname" >排行条件：</label>
                      <select class="form-control" name="" id="orderName" >
						   <%--<option value="of.create_time">订单创建时间</option> --%>
	                      <option value="sales_volumes">销售量</option>
	                      <option value="sales_price">销售额</option>
	                      <option value="profit_loss">盈亏</option>
                      </select>
               </div>
               <div class="col-xs-4 form-group">
               		<%--过滤条件：--%>
               		<%--<select id="filter_condition">--%>
               			<%--<option value="0" selected="selected">查询所有订单</option>--%>
               			<%--<option value="1" >过滤 运费 为0订单</option>--%>
               			<%--<option value="5">过滤 采购/销售 为0订单</option>--%>
               			<%--<option value="6">过滤 运费/采购/销售 为0订单</option>--%>
               			<%--<option value="7">过滤免邮中 运费/采购/销售 为0订单</option>--%>
               			<%--<option value="8">过滤非免邮中 运费/采购/销售 为0订单</option>--%>
               			<%--<option value="9">查询录入运费或实收运费为0的订单</option>--%>
               			<%--<option value="10">查询采购成本为0的订单</option>--%>
               		<%--</select>--%>
               		<!-- 
                    <label for="nickname" > &nbsp;&nbsp;&nbsp;&nbsp;  </label>
		             <div class="btn btn-primary pull-right" id="filterSearch" style="margin-right: 5px;">

		                    <i class="fa fa-search">过滤运费查询</i>
		             </div>

		                    <i class="fa fa-search">过滤运费为0的数据</i>
		             </div> -->
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
		              <div class="btn btn-primary pull-right" id="addReport1" style="display:none;color:red">
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
         </div>
         <div  style="padding:15px;">
         <label >订单报表数据：</label>
         <table id="categroyReport" class="imagetable">
                    <thead>
                       <tr>
	                        <th width="60" >序号</th>
	                        <th width="200" >订单号</th>
	                        <th width="200">运单号</th>
	                        <th width="140" >采购金额(人民币)</th>
	                        <th width="140"  >销售金额(人民币)</th>
						    <th width="140" >平均售价(人民币)</th>
							<th width="110" >销售数量(个)</th>
							<th width="110" >采购数量(个)</th>
							<%--<th width="100" >录入运费(￥)</th>--%>
							<th width="100" >实收运费(￥)</th>
							<th width="100" >盈亏金额(￥)</th>
							<th width="100">估算重量</th>
							<%--<th width="100">录入重量</th>--%>
							<th width="100">实收重量</th>
							<th width="100" >盈亏(%)</th>
							<th width="80">出货类型</th>
                       </tr>
                   </thead>
                   <tbody>
				</tbody>
			</table><br/>
         </div>
		 <div style="text-align: center; display: none;" id="pagediv">
		 	共查到<span id="datacount">0</span>数据&nbsp;&nbsp;
		 	<input type="button" id="prePage" value="上一页"/>&nbsp;
		 	第<span id="nowPage">1</span>页/共<span id="allPage">0</span>页
		 	<input type="button" id="nextPage" value="下一页"/>&nbsp;&nbsp;
		 	<input type="text" id="toPage" style="width:50px;"/><input type="button" value="Go" id="jumpPage"/>
		 </div>
         <div style="padding:15px;" >
          <label >报表合计：</label>
         <table id="sumReport" class="imagetable">
                    <thead>
                       <tr>
	                        <th width="120" >序号</th>
	                        <th width="120" >订单数量(个)</th>
	                        <th width="150" >总支出 (人民币)</th>
	                        <th width="150"  >总收入 (人民币)</th>
	                        <th width="150"  >销售商品数(个)</th>
						    <th width="150" >销售平均价 (人民币)</th>
							<%--<th width="150" >多余采购量(个)</th>--%>
							<th width="150" >实际总运费 (人民币)</th>
							<th width="150" >盈亏金额(￥)</th>
							<th width="150" >盈亏(%)</th>
                       </tr>
                   </thead>
                   <tbody>
				   </tbody>
		</table>
	 </div>
	  <div id="goods" class="displaynone" style="padding:15px;" >
         <label >商品详情：</label>
         <table id="goodsDetail" class="imagetable">
                    <thead>
                       <tr>
	                        <th width="120" >NO.</th>
	                        <th width="120" >goods_name</th>
	                        <th width="150" >goods_url</th>
	                        <th width="150" >goods_p_url</th>
	                        <th width="150"  >goods_img_url</th>
	                        <th width="150"  >goods_price</th>
						    <th width="150" >goods_p_price</th>
							<th width="150" >usecount</th>
							<th width="150" >buycount</th>
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
	 
   </form>
   </div>
 </div>

</body>
<script type="text/javascript">
function toStatisticalReport(){
	window.location.href="/cbtconsole/StatisticalReport/getMounthReport";
}
function round2(number,fractionDigits){  //实现四舍五入保留小数位
    with(Math){  
        return round(number*pow(10,fractionDigits))/pow(10,fractionDigits);  
    }  
} 

//检查报表是否存在，若不存在自动生成
$("#rMonth").change(function(){
	var year = $("#rYear").val();
	var month = $("#rMonth").val();
	if(year==-1){
		alert("请选择年份！");
		return false;
	}
	if(month==-1){
		alert("请选择月份！");
		return false;
	}
	$.ajax({
		url: "/cbtconsole/StatisticalReport/selectExistExport?rand="+Math.random(),
		data:{"reportYear": year,"reportMonth":month},
		type:"post",
		dataType:"text",
		success:function(data){
			if(data==0){
				addexport(0);
			}
		},
		error:function(){
			alert("There is taken an unknow error.");
		}
	});
	
});

//生成报表
$('#addReport').click(function(){
	addexport(1);
});
function addexport(f){
	var choseYear =$('#rYear').val();
	var choseMonth=$('#rMonth').val();
	var choseDay =$('#rDay').val();
	var type= $('#type').val();
	if(choseYear == -1){
		alert('请选择年份！');
		return false;
	}else if(choseMonth == -1){
		alert('请选择月份！');
		return false;
	}
	choiseDay = new Date(choseYear,choseMonth,0).getDate();
	var date1 = choseYear+'-'+choseMonth+'-1 00:00:00';
	var date2 = choseYear+'-'+choseMonth+'-'+choiseDay+ ' 23:59:59';
	$('#addReport').css('display','none');
	$('#addReport1').css('display','block');
	jQuery.ajax({
        url:"/cbtconsole/StatisticalReport/addReport2",
        data:{"timeFrom" : date1,"timeTo" : date2,
        	"reportYear":choseYear,
        	"reportMonth":choseMonth,
        	"type":type},
        type:"post",
        success:function(data, status){
        	   $('#addReport').css('display','block');
        	   $('#addReport1').css('display','none');
               if(data.ok){
            	   $('#pgSearch').trigger('click');
               }else{
            	   if(f==1){ //f==0表示月份选择时自动生成报表，不弹提示框
            	   	alert("生成报表失败");
            	   }
               };
        },
    	error:function(e){
    		alert("没有数据！");
    	}
    });
}

$("#pgExport").click(function(){
	//window.location.href="/cbtconsole/StatisticalReport/get";
	//导出报表
	var choseYear =$('#rYear').val();
	var choseMonth=$('#rMonth').val();
	var choseDay =$('#rDay').val();
	var type =$('#type').val();
	var Freight =document.getElementById("noFreeFreight").value;
	if(choseYear == '-1'){
		alert('请选择年份！');
		return;
	}else if(choseMonth == '-1'){
		alert('请选择月份！');
		return;
	}
	window.location.href ="/cbtconsole/StatisticalReport/exportOrder?reportYear="+choseYear+"&reportMonth="+choseMonth+"&type="+type+"&noFreeFreight="+Freight;
});

//查询报表
$('#pgSearch').click(function(){
	searchExport(1);
});
$("#prePage").click(function(){
	var nowPage = $("#nowPage").html();
	if(parseInt(nowPage)<=1 ){
		alert("已到达首页");
		return false;
	}else{
		$("#nowPage").html(parseInt(nowPage)-1)
		searchExport(parseInt(nowPage)-1);
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
		searchExport(parseInt(nowPage)+1);
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
		searchExport(parseInt(topage));
	}
});

function searchExport(page){
	var choseYear =$('#rYear').val();
	var choseMonth=$('#rMonth').val();
	var choseDay =$('#rDay').val();
	var orderName = $('#orderName').val();
	var type = $('#type').val();
	var filter = $("#filter_condition").val();
	$("#sumReport tbody").html("");
	$("#categroyReport tbody").html("");
	
	if(choseYear == '-1'){
		alert('请选择年份！');
		return;
	}else if(choseMonth == '-1'){
		alert('请选择月份！');
		return;
	}
	choiseDay = new Date(choseYear,choseMonth,0).getDate();
	var date1 = choseYear+'-'+choseMonth+'-1 00:00:00';
	var date2 = choseYear+'-'+choseMonth+'-'+choiseDay+ ' 23:59:59';
	$('#timeFrom').val(date1);
	$('#timeTo').val(date2);
	
	jQuery.ajax({
        url:searchReport,
        data:{"reportYear":choseYear,
        	  "reportMonth":choseMonth,
        	  "orderName":orderName,
        	  "noFreeFreight":filter,
        	  "page":page,
        	  "type":type},
        type:"post",
        success:function(data, status){
        	if(data.ok){
                var reportInfoList = data.data;
                var reportDetailList =data.allData;
                // $('#noFreeFreight').val("0");
                // if(reportDetailList==null || reportDetailList.length==0){
                // 	$("#pagediv").css("display","none");
                // }else{
	                $("#pagediv").css("display","block");
	                var datacount = data.total;
	                $("#datacount").html(datacount);
					if(datacount%20==0){
						$("#allPage").html(datacount/20);
					}else{
						$("#allPage").html(parseInt(datacount/20+1));
					}
					$("#nowPage").html(page);
                // }
                
                //var pay = 0.0; //总支出
                //var income = 0.0; //总收入
                //var salecount = 0; //总销售数量
                //var buycount = 0; //总采购数量
                //var fee = 0.0; //实际运费
                
                var price =0;
                for(var i=0;i<reportInfoList.length;i++){
                	var reportDetail = new Object();
                	reportDetail = reportInfoList[i];
                	// price = reportDetail.salesPrice - reportDetail.purchasePrice -reportDetail.freight;
                	htm_='';
                	htm_ = '<tr>                                               ';
                	htm_ += '<td >'+         ((page-1)*20+i+1 )                    +'</td>';
                	// var remarks = reportDetail.remarks==null?"":reportDetail.remarks;
                	// if(remarks!=""){
                	// 	if(remarks.length>20&&remarks.indexOf("O")>-1){
                	// 		var  attr = remarks.substring(0,remarks.lastIndexOf(","));
                	// 		var attrs = attr.split(",");
                	// 		htm_ += '<td  style="cursor:pointer;">';
                	// 		for(var j=0;j<attrs.length;j++){
                	// 			htm_+= '<a  target="_blank"  href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo='+attrs[j]+'"><font color="red" style="text-decoration:underline">'+attrs[j]+'</font></a></br>';
                	// 		}
                	// 		htm_ += '</td>';
                	// 	 	/* htm_ += '<input type="hidden" id="categroy" value="'+reportDetail.orderno+'"/>';  class="showDetail" */
                    	// }else{
                    	// 	htm_ += '<td class="showDetail" style="cursor:pointer;"><font color="red" style="text-decoration:underline">'+reportDetail.orderno +'</font>';
                     //    	htm_ += '<input type="hidden" id="categroy" value="'+reportDetail.orderno+'"/>';
                     //    	htm_ += '</td>                                                                ';
                    	// }
                	// }else{
                	// 	htm_ += '<td class="showDetail" style="cursor:pointer;"><font color="red" style="text-decoration:underline">'+reportDetail.orderno +'</font>';
                    	// htm_ += '<input type="hidden" id="categroy" value="'+reportDetail.orderno+'"/>';
                    	// htm_ += '</td>                                                                ';
                	// }
                	// var expressno = reportDetail.expressno==null?"":reportDetail.expressno;
                	htm_ += '<td >'+reportDetail.orderid+'</td>';
                	htm_ += '<td >'+reportDetail.expressno         +'</td>';
                	htm_ += '<td >'+reportDetail.buyAmount            +'</td>';
                	//htm_ += '<td >'+round2(reportDetail.averagePrice,2)           +'</td>';
                	htm_ += '<td >'+reportDetail.salesAmount+'</td>';
                	htm_ += '<td >'+reportDetail.avgSalePrice           +'</td>';
                	htm_ += '<td >'+reportDetail.salesCount               +'</td>';
                	htm_ += '<td >'+reportDetail.buyCount   +'</td>';
                	htm_ += '<td >'+reportDetail.actualFreight               +'</td>';
                    htm_ += '<td >'+reportDetail.proLossAmount               +'</td>';
                	htm_ += '<td>'+reportDetail.estimatedWeight                           +'</td>';
                	htm_ += '<td >'+reportDetail.actualWeight            +'</td>';
                	htm_ += '<td >'+reportDetail.proLoss          +'</td>';
                	htm_ += '<td >'+reportDetail.shippingType            +'</td>';
                	// var per = round2((reportDetail.salesPrice - reportDetail.purchasePrice-reportDetail.freight)/(reportDetail.salesPrice==0 ? 100 : reportDetail.salesPrice)*100,2);
                	// var color= "#000000";
                	// if(per<=0){color="red";}
                	// else if(per>=70){color="#07CC3E";}
                	// htm_ += '<td style="color:'+color+'">'+per+'</td>';
                	// htm_ +='<td >'+(reportDetail.typeship==2?'合并出货':'正常出货')+'</td>';
                	// htm_ += '</tr>   ';
                	$('#categroyReport').append(htm_);
                	
                	//pay = pay +parseFloat(reportDetail.purchasePrice);
                	//income = income + parseFloat(reportDetail.salesPrice);
                	//salecount = salecount + parseInt(reportDetail.salesVolumes);
                	//buycount = buycount + parseInt(reportDetail.buycount);
                	//fee = fee + parseFloat(reportDetail.freight);
                }
                if(reportDetailList.length>0 && reportDetailList[0].orderid>0){
                    var htm_ ='';
                    htm_ += '<tr>                                               ';
                    htm_ += '<td><input type="hidden" id="noFreeFreight" value="0">1</td>                                         ';
                    htm_ += '<td>'+reportDetailList[0].orderid  +'</td>';
                    htm_ += '<td>'+reportDetailList[0].buyAmount    +'</td>';
                    htm_ += '<td>'+reportDetailList[0].salesAmount        +'</td>';
                    htm_ += '<td>'+reportDetailList[0].salesCount          +'</td>';
                    htm_ += '<td>'+reportDetailList[0].avgSalePrice         +'</td>';
                    htm_ += '<td>'+reportDetailList[0].actualFreight+'</td>                                         ';
                    htm_ += '<td>'+reportDetailList[0].proLossAmount              +'</td>';
                    htm_ += '<td>'+reportDetailList[0].proLoss          +'</td>';
                    htm_ += '</tr>  ';
                    $("#sumReport").append(htm_);
				}
            }else{
				 //alert("查询失败！");
				 $("#pagediv").css("display","none");
            	$("#sumReport").append("<tr><td colspan='10' style='text-align:center;color:red;'>(没有数据)</td></tr>");
            	$("#categroyReport").append("<tr><td colspan='15' style='text-align:center;color:red;'>(没有数据)</td></tr>");
			}
        },
    	error:function(e){
    		$("#pagediv").css("display","none");
    		alert("查询失败！");
    	}
    });
}

//过滤查询报表
/*
$('#filterSearch').click(function(){
	searchExport();
});
*/
//点击商品分类查询该报表下商品详细信息
$("#categroyReport").on("click",".showDetail",function(){
	var categroy = $(this).find('#categroy').val();
	//window.open('/cbtconsole/order/getOrderInfo.do?showUnpaid=0&orderno='+categroy);
	window.open('/cbtconsole/orderDetails/queryByOrderNo.do?orderNo='+categroy);
	/* var timeFrom =$('#timeFrom').val();
	var timeTo = $('#timeTo').val();
	$("#goodsDetail tbody").html("");
	jQuery.ajax({
        url:showGoodsByOrder,
        data:{"categroy":categroy,
        	  "timeFrom":timeFrom,
        	  "timeTo":timeTo},
        type:"post",
        success:function(data, status){
        	if(data.ok){
                var orderProductSourceList = data.data;
                for(var i=0;i<orderProductSourceList.length;i++){
                	var goods = new Object();
                	goods = orderProductSourceList[i];
                   var htm_ ='';
                    htm_ += '<tr>                                               ';
                	htm_ += '<td>'+(i+1)                                 +'</td>';
                	htm_ += '<td>'+goods.goodsName            +'</td>';
                	htm_ += '<td><a href="'+goods.goodsUrl+'" target="_blank">'+goods.goodsUrl  +'</a></td>'; 
                	htm_ += '<td><a href="'+goods.goodsPUrl+'" target="_blank">'+goods.goodsPUrl    +'</td>';
                	htm_ += '<td><img src="${gbb.goods.goodsImgUrl}"/></td>';
                	htm_ += '<td>'+goods.goodsPrice          +'</td>';
                	htm_ += '<td>'+goods.goodsPPrice        +'</td>';
                	htm_ += '<td>'+goods.usecount+'</td>     ';
                	htm_ += '<td>'+goods.buycount          +'</td>';
                	htm_ += '</tr>                                              ';
                    $("#goodsDetail").append(htm_);
                }
                $("#goods").removeClass("displaynone");
            }else{
				 alert("查询失败！");
			}
        },
    	error:function(e){
    		alert("查询失败！");
    	} 
    });*/
});

</script>
</html>