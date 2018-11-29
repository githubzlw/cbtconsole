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
<title>采购订单对账报表</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var searchReport = "/cbtconsole/StatisticalReport/searchTaoBaoOrder"; //报表查询
</script>
<style type="text/css">
.displaynone{display:none;}
</style>

<script type="text/javascript">
   function selectStatus(){
	   jQuery.ajax({
	        url:"/cbtconsole/StatisticalReport/selectStatus",
	        data:{},
	        type:"post",
	        success:function(data){
	        	if(data){
	        		for(var i=0;i<data.data.length;i++){
	        			htm_='';
	                	htm_ = '<option value='+data.data[i]+'> '+data.data[i]+'                                              ';
	                	htm_ += '</option>            ';
		        		$('#orderstatus').append(htm_);
	        		}
	        	}
	        }});
	   
   }
</script>
</head>
<body text="#000000" onload="selectStatus();">
<div>
 <div>
   <form id="adduserForm" name="adduserForm" action="" method="post">
      <div class="box box-solid" >
         <div class="box-header with-border">
             <h4>查询条件</h4><span style="color:Red">建议至少选择一个条件查询</span>
         </div>
         <div class="box-body">
             <!--   <div class="col-xs-4 form-group">
                  <label for="nickname" >订单日期：</label>
                      <select class="form-control" name="" id="orderdate">
	                     <option value="0">--最近天数--</option>
	                     <option value="3">最近三天</option>
				         <option value="7">最近一周</option>
				         <option value="14">最近两周</option>
				         <option value="30">最近一个月</option>
                      </select>
              </div>-->
              <div class="col-xs-4 form-group">
                  <label for="nickname" >订单状态：</label>
                         <select class="form-control" name="" id="orderstatus"> -->
	                      <option value="0">全部</option>
<!-- 	                      <option value="等待买家付款">等待买家付款</option> -->
<!-- 	                      <option value="交易关闭">交易关闭</option> -->
<!-- 	                      <option value="交易成功">交易成功</option> -->
<!-- 	                      <option value="卖家已发货">卖家已发货</option> -->
<!-- 	                      <option value="部分发货">部分发货</option> -->
<!-- 	                      <option value="退款中">退款中</option> -->
<!-- 	                      <option value="退款成功">退款成功</option> -->
                      <c:forEach items="${list}" var="exchangeRate">
						  <option value="${exchangeRate}">${exchangeRate}</option>
						</c:forEach>
						</select>
              </div>
               <div class="col-xs-4 form-group">
                  <label for="nickname" >采购平台：</label>
                      <select class="form-control" name="" id="orderSource">
	                      <option value="-1">全部</option>
	                      <option value="0">淘宝订单</option>
	                      <option value="1">1688订单</option>
	                       <option value="转账">转账</option>
                      </select>
              </div>
				 <div class="col-xs-4 form-group">
                  <label for="nickname" >采购金额：</label>
                      <select class="form-control" name="" id="amount">
	                      <option value="-1">全部</option>
	                      <option value="0~100">0~~100</option>
	                      <option value="100~200">100~~200</option>
	                      <option value="200~300">200~~300</option>
	                      <option value="300~100000">300~~100000</option>
                      </select>
             	 </div>              
              <div class="col-xs-4 form-group">
                  <label for="nickname" >未入库订单周期：</label>
                      <select class="form-control" name="" id="noCycle">
	                      <option value="0">全部</option>
	                      <option value="1">已入库订单</option>
	                      <option value="15">超过15天尚未入库订单</option>
	                      <option value="30">超过30天尚未入库订单</option>
                      </select>
              </div>
              <div class="col-xs-4 form-group">
                  <label for="nickname" >采购账号：</label>
                      <select class="form-control" name="" id="procurementAccount">
	                      <option value="0">全部</option>
	                      <option value="策融4">策融4</option>
	                      <option value="策融8">策融8</option>
	                      <option value="策融10">策融10</option>
	                      <option value="策融l">策融l</option>
	                      <option value="策融y">策融y</option>
	                      <option value="策融m">策融m</option>
	                      <option value="策融1013">策融1013</option>
                      </select>
              </div>
              <div class="col-xs-4 form-group">
                  <label for="nickname" >是否公司订单：</label>
                      <select class="form-control" name="" id="isCompany">
	                      <option value="0">全部</option>
	                      <option value="1">公司订单</option>
	                      <option value="2">非公司订单</option>
                      </select>
              </div>
              <div class="col-xs-4 form-group">
                 <label for="nickname" >订单时间：</label>
                   <input id="startdate" name="startdate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value=""/>
                   ~~~~~~
                   <input id="enddate" name="enddate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value=""/>
              </div>
              <div class="col-xs-4 form-group">
                 <label for="nickname" >支付时间：</label>
                   <input id="paystartdate" name="paystartdate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value=""/>
                   ~~~~~~
                   <input id="payenddate" name="payenddate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value=""/>
              </div>
              <div class="col-xs-4 form-group">
                   <label for="nickname" >订单号：</label>
                   <input type="text" id="orderid">
              </div>
              <div class="col-xs-4 form-group">
                   <label for="nickname" >关联公司订单：</label>
                      <input type="text" id="myorderid">
              </div>
               <div class="col-xs-4 form-group">
               	     <label for="nickname" > &nbsp;&nbsp;&nbsp;&nbsp;  </label>
               	           <font color="red">当前页面总金额:￥<span id="pagePrice">0</span></font>
		              <div class="btn btn-primary pull-right" id="pgExport" style="margin-right: 5px;">
		                    <i class="fa fa-search">导 出</i>
		             </div>
		             <div class="btn btn-primary pull-right" id="pgSearch" style="margin-right: 5px;">
		                    <i class="fa fa-search">查 询</i>
		             </div>
                   <div>
                       <span id="info" style="color:Red"></span>
                   </div>
              </div>
         </div>
         <div  style="padding:15px;">
         <label >采购订单对账数据：</label>
         <table id="categroyReport" class="imagetable">
                    <thead>
                       <tr>
                            <th width="50" style="text-align: center;">序号</th>
	                        <th width="180" style="text-align: center;">采购订单</th>
	                        <th width="150" style="text-align: center;">订单金额</th>
	                          <th width="150"  style="text-align: center;">订单运费</th>
	                         <!--<th width="150"  style="text-align: center;">公司订单数量</th>-->
	                        <th width="190"  style="text-align: center;">快递单号</th>
						    <th width="150" style="text-align: center;">采购账号</th>
							<th width="180" style="text-align: center;">订单日期</th>
							<th width="180" style="text-align: center;">支付日期</th>
							<th width="200" style="text-align: center;">订单状态</th>
							<th width="200" style="text-align: center;">是否入库</th>
							<th width="200" style="text-align: center;">备注</th>
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
	 
	
   </div>
   </form>
 </div>
 
</div> 
</body>
<script type="text/javascript">

//导出报表
$("#pgExport").click(function(){
	//生成报表
	var orderdate =$('#orderdate').val();
	var orderstatus=$('#orderstatus').val();
	var orderSource =$('#orderSource').val();
	var amount =$('#amount').val();
	var noCycle=$('#noCycle').val();
	var orderid=$('#orderid').val();
	var isCompany=$('#isCompany').val();
	var myorderid=$('#myorderid').val();
	var startdate=$('#startdate').val();
	var enddate=$('#enddate').val();
	var paystartdate=$('#paystartdate').val();
	var payenddate=$('#payenddate').val();
	var procurementAccount =$('#procurementAccount').val();
	window.location.href ="/cbtconsole/StatisticalReport/exportTaoBaoOrder?orderdate="+orderdate+"&orderstatus="+orderstatus+"&procurementAccount="+procurementAccount+"&noCycle="+noCycle+"&amount="+amount+"&orderid="+orderid+"&orderSource="+orderSource+"&isCompany="+isCompany+"&myorderid="+myorderid+"&startdate="+startdate+"&enddate="+enddate+"&paystartdate="+paystartdate+"&payenddate="+payenddate;
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
//查询报表
$('#pgSearch').click(function(){
	searchExport(1)
});

function searchExport(page){
    $("#info").html("查询中。。。。。");
	$("#categroyReport tbody").html("");
	var orderdate =$('#orderdate').val();
	var orderstatus=$('#orderstatus').val();
	var orderSource =$('#orderSource').val();
	var amount =$('#amount').val();
	var noCycle=$('#noCycle').val();
	var orderid=$('#orderid').val();
	var startdate=$('#startdate').val();
	var enddate=$('#enddate').val();
	var paystartdate=$('#paystartdate').val();
	var payenddate=$('#payenddate').val();
	var isCompany=$('#isCompany').val();
	var myorderid=$('#myorderid').val();
	var procurementAccount =$('#procurementAccount').val();
	jQuery.ajax({
        url:searchReport,
        data:{"page":page,
        	  "orderdate" :orderdate,
        	  "orderstatus" :orderstatus,
        	  "procurementAccount" :procurementAccount,
        	  "noCycle" :noCycle,
        	  "amount" :amount,
        	  "orderid":orderid,
        	  "orderSource" :orderSource,
        	  "isCompany":isCompany,
        	  "myorderid":myorderid,
        	  "startdate":startdate,
        	  "enddate":enddate,
        	  "paystartdate":paystartdate,
        	  "payenddate":payenddate
        	  },
        type:"post",
        success:function(data){
            $("#info").html("");
        	if(data){
        		var reportDetailList=data.data.infoList;
        		var allCount=data.data.allCount;
        		if(allCount%20==0){
					allpage = allCount/20;
				}else{
					allpage = parseInt(allCount/20) + 1;
				}
        		$("#pagePrice").html(data.data.pagePrice);
        		$("#datacount").html(allCount);
				$("#nowPage").html(page);
				$("#allPage").html(allpage);
                for(var i=0;i<reportDetailList.length;i++){
                	reportDetail = reportDetailList[i];
                	if(reportDetail.iid!=null && reportDetail.iid>0){
                		remark="已入库";
                	}else{
                		remark="未入库";
                	}
                	if(reportDetail.opsorderid!=null && reportDetail.opsorderid.length>0){
                		isCompany=reportDetail.opsorderid;
                	}else{
                		isCompany="非公司订单";
                	}
                	htm_='';
                	htm_ = '<tr>                                               ';
                	htm_ += '<td align="center">'+         (i+1)                      +'</td>';
                	htm_ += '<td align="center"><a href="/cbtconsole/StatisticalReport/getTaoBaoOrderDetailsInfo.do?orderid='+reportDetail.orderid+'" target="_blank">'+reportDetail.orderid+'</a></td>';
                	htm_ += '<td align="center">'+reportDetail.totalprice          +'</td>'; 
                	htm_ += '<td align="center">'+reportDetail.preferential             +'</td>';
                	htm_ += '<td align="center">'+reportDetail.shipno           +'</td>';
                	htm_ += '<td align="center">'+reportDetail.username           +'</td>';
                	htm_ += '<td align="center">'+reportDetail.orderdate               +'</td>';
                	htm_ += '<td align="center">'+reportDetail.paydata               +'</td>';
                	htm_ += '<td align="center">'+reportDetail.orderstatus             +'</td>';
                	htm_ += '<td align="center">'+remark             +'</td>';
                	htm_ += '<td align="center">'+isCompany             +'</td>';
                	htm_ += '</tr>                                             ';
                	$('#categroyReport').append(htm_);
                }
            }else{
				 //alert("查询失败！");
				 $("#pagediv").css("display","none");
            	$("#sumReport").append("<tr><td colspan=8 style='text-align:center;color:red;'>(没有数据)</td></tr>");
            	$("#categroyReport").append("<tr><td colspan=8 style='text-align:center;color:red;'>(没有数据)</td></tr>");
			}
        },
    	error:function(e){
    		$("#pagediv").css("display","none");
    		alert("查询失败！");
    	}
    });
}


</script>
</html>