<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>出入库明细统计报表</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">
    <script type="text/javascript"
            src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
var searchReport = "/cbtconsole/StatisticalReport/searchinOutDetails"; //报表查询
</script>
<style type="text/css">
.displaynone{display:none;}
</style>


</head>
<%
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String date = sdf.format(new Date());
%>
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
                   <label for="nickname" >出入库时间：</label>
               <input id="startTime"
                      name="startTime" readonly="readonly"
                      onfocus="WdatePicker({isShowWeek:true})"
                      value="${param.startdate}"
                      onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' /></td>
               ~<input id="endTime"
                       name="endTime" readonly="readonly"
                       onfocus="WdatePicker({isShowWeek:true})" value="${param.enddate}"
                       onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />
           </div>
           <div class="col-xs-4 form-group">
                   <label for="nickname" >出入库类型：</label>
                   <select name="" id="type">
	                     <option value="">--全部--</option>
	                     <option value="1">入库</option>
				         <option value="2">出库</option>
                      </select>
           </div>
           <div class="col-xs-4 form-group">
                   <label for="nickname" >订单号：</label>
                   <input type="text" id="orderid" width="300px;">
           </div>
           <div class="col-xs-4 form-group">
                   <label for="nickname" >商品号：</label>
                   <input type="text" id="goodsid">
           </div>
               <div class="col-xs-4 form-group">
		             <div class="btn btn-primary pull-right" id="pgSearch" style="margin-right: 5px;">
		                    <i class="fa fa-search">查 询</i>
		             </div>
              </div>
             <div class="col-xs-4 form-group">
                 <div class="btn btn-primary pull-right" id="reset" style="margin-right: 5px;">
                     <i class="fa fa-search">重 置</i>
                 </div>
             </div>
         </div>
         <div  style="padding:15px;">
         <label >商品出入库明细：</label>
         <table id="categroyReport" class="imagetable">
                    <thead>
                       <tr>
                            <th width="50" style="text-align: center;">序号</th>
	                        <th width="180" style="text-align: center;">订单号</th>
	                        <th width="150" style="text-align: center;">商品号</th>
                            <th width="150" style="text-align: center;">商品图片</th>
                            <th width="150" style="text-align: center;">商品销售数量</th>
                            <th width="150" style="text-align: center;">商品销售规格</th>
	                        <th width="150" style="text-align: center;">出入库类型</th>
	                        <th width="200" style="text-align: center;">出入库时间</th>
	                        <th width="190"  style="text-align: center;">出入库数量</th>
						    <th width="150" style="text-align: center;">产品pid</th>
							<th width="180" style="text-align: center;">出入库人员</th>
							<th width="200" style="text-align: center;">出入库的库位</th>
                            <th width="200" style="text-align: center;">出入库备注</th>
                       </tr>
                   </thead>
                   <tbody>
				</tbody>
		</table>
         </div>
        <div style="text-align: center; " id="pagediv">
           <%--<span style="color: red;">* 注：出入库数量中，整数为入库，负数为出库</span>--%>
           &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
	window.location.href ="/cbtconsole/StatisticalReport/exportTaoBaoOrder";
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
//重置
$('#reset').click(function(){
    $('#orderid').val("");
    $('#goodsid').val("");
    $('#type').val("");
    $('#startTime').val("");
    $('#endTime').val("");
});

function searchExport(page){
	$("#categroyReport tbody").html("");
	var orderid =$('#orderid').val();
	var goodsid =$('#goodsid').val();
	var type =$('#type').val();
	var startTime =$('#startTime').val();
	var endTime =$('#endTime').val();
	jQuery.ajax({
        url:searchReport,
        data:{"page":page,
        	  "orderid":orderid,
        	  "goodsid":goodsid,
        	  "type":type,
        	  "endTime":endTime,
        	  "startTime":startTime
        	  },
        type:"post",
        success:function(data){
        	if(data){
        		var reportDetailList=data.data.inOutList;
        		var allCount=data.data.allCount;
        		if(allCount%20==0){
					allpage = allCount/20;
				}else{
					allpage = parseInt(allCount/20) + 1;
				}
        		$("#datacount").html(allCount);
				$("#nowPage").html(page);
				$("#allPage").html(allpage);
                for(var i=0;i<reportDetailList.length;i++){
                	reportDetail = reportDetailList[i];
                	htm_='';
                	htm_ = '<tr>';
                	htm_ += '<td align="center">'+(i+1)+'</td>';
                	htm_ += '<td align="center">'+reportDetail.orderid+'</td>';
                	htm_ += '<td align="center">'+reportDetail.goodsid+'</td>';
                	htm_ += '<td align="center"><img src='+reportDetail.car_img+'></img></td>';
                    htm_ += '<td align="center">'+reportDetail.yourorder+'</td>';
                	htm_ += '<td align="center" style="width:200px">'+reportDetail.car_type +'</td>';
                	htm_ += '<td align="center">'+reportDetail.TYPE+'</td>';
                	htm_ += '<td align="center">'+reportDetail.createtime+'</td>';
                	htm_ += '<td align="center">'+reportDetail.counts+'</td>';
                    htm_ += '<td align="center">'+reportDetail.goods_pid+'</td>';
                    htm_ += '<td align="center">'+reportDetail.admName+'</td>';
                    htm_ += '<td align="center">'+reportDetail.barcode+'</td>';
                    htm_ += '<td align="center">'+reportDetail.remark+'</td>';
                	htm_ += '</tr>';
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