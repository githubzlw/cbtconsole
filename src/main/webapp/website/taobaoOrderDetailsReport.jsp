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
<title>taobaoOrderReport</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/taoBaoDetails.css">
<script type="text/javascript">
var searchReport = "/cbtconsole/StatisticalReport/searchTaoBaoOrder"; //报表查询
</script>
   <style type="text/css">

#outerContainer {
	width:1000px;
	height:1500px;
}

.vsplitbar {
    width: 3px;
    /*background: #B9B9B9;*/
    border-right: 1px solid #8f949a;
}

.vsplitbar:hover, .vsplitbar.active {
    background: #8f949a;
}

#rightPanel {
    background-color: White;
}

/*#leftPanel {
    min-width: 120px;
}*/

.splitterMask {
   position:absolute;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   overflow: hidden;
   background-image: url(resources/images/transparent.gif);
   z-index: 20000;
}

    </style>

  <script type="text/javascript">
  $(document).ready(function(){ 
	  var orderid = '<%=request.getAttribute("orderid")%>';
	  var orderdate = '<%=request.getAttribute("orderdate")%>';
	  var tbOt1688 = '<%=request.getAttribute("tbOt1688")%>';
	  var orderstatus = '<%=request.getAttribute("orderstatus")%>';
	  var totalprice = '<%=request.getAttribute("totalprice")%>';
	  var paytreasureid = '<%=request.getAttribute("paytreasureid")%>';
	  var merchantorderid = '<%=request.getAttribute("merchantorderid")%>';
	  var seller = '<%=request.getAttribute("seller")%>';
	  var username = '<%=request.getAttribute("username")%>';
	  var totalqty = '<%=request.getAttribute("totalqty")%>';
	  var isStorage = '<%=request.getAttribute("isStorage")%>';
	  var orderids = '<%=request.getAttribute("orderids")%>';
	  var reportDetailList = <%=request.getAttribute("list")%>;
	  var buyCountList = <%=request.getAttribute("buyCount")%>;
	  for(var i=0;i<reportDetailList.length;i++){
		  	reportDetail = reportDetailList[i];
		  	if(reportDetail.opsid!=null && reportDetail.opsid >0){
		  		remark="有匹配货源";
		  	}else{
		  		remark="无匹配货源";
		  	}
		  	htm_='';
		  	htm_ = '<tr>                                               ';
		  	htm_ += '<td align="center">'+         (i+1)                      +'</td>';
		  	htm_ += '<td align="center">'+reportDetail.itemid+'</td>';
		  	htm_ += '<td align="center">'+reportDetail.itemname          +'</td>'; 
		  	htm_ += '<td align="center"> <img src="'+reportDetail.imgurl+'" height="100" width="100"></td>';
		  	htm_ += '<td align="center">'+reportDetail.sku           +'</td>';
		  	htm_ += '<td align="center">'+reportDetail.itemqty           +'</td>';
		  	htm_ += '<td align="center" id="'+reportDetail.id+'">0</td>';
		  	htm_ += '<td align="center">'+reportDetail.itemprice               +'</td>';
		  	htm_ += '<td align="center">'+ (parseFloat(reportDetail.itemqty)*parseFloat(reportDetail.itemprice)).toFixed(2) +'</td>';
		  	htm_ += '<td align="center">'+reportDetail.itemurl             +'</td>';
			htm_ += '<td align="center">'+reportDetail.creatTime             +'</td>';
			htm_ += '<td align="center">'+remark             +'</td>';
		  	htm_ += '</tr>                                             ';
		  	$('#categroyReport').append(htm_);
		  }
	  for(var i=0;i<buyCountList.length;i++){
		  	info = buyCountList[i];
		  	document.getElementById(""+info.id+"").innerHTML=info.buycount;
	  }
  });
  
  </script>
</head>
<body text="#000000">
<div class="wrapper">
 <div class="content-wrapper">
   <form id="adduserForm" name="adduserForm" action="" method="post">
      <div class="box box-solid" >
         <div class="box-header with-border">
             <h4>订单详情</h4>
         </div>
         <div class="box-body" style="position: relative;">
              <div class="col-xs-4 form-group" style=" display: inline-block;">
                   <div class="text">
                     <p style="font-weight:bold;">采购订单号：${orderid}</p>
                   </div>
                   <div>
                     <p style="font-weight:bold;">采购时间：${orderdate}</p>
                   </div>
                   <div>
                     <p style="font-weight:bold;">采购来源：${tbOt1688}</p>
                   </div>
                   <div>
                     <p style="font-weight:bold;">采购订单状态：${orderstatus}</p>
                   </div>
                   <div>
                     <p style="font-weight:bold;">采购订单金额：${totalprice}</p>
                   </div>
              </div>
              <div>
                <img id="u507_img" class="img " src="../img/u507.png" style="position: absolute;left: 367px;">
              </div>
              <div class="col-xs-4 form-group">
                   <div class="text">
                     <p style="font-weight:bold;">支付宝交易号：${paytreasureid}</p>
                   </div>
                   <div>
                     <p style="font-weight:bold;">商&nbsp;户&nbsp;订&nbsp;单&nbsp;号：${merchantorderid}</p>
                   </div>
                   <div>
                     <p style="font-weight:bold;">商&nbsp;&nbsp;&nbsp;家&nbsp;&nbsp;&nbsp;名&nbsp;&nbsp;称：${seller}</p>
                   </div>
                   <div>
                     <p style="font-weight:bold;">商&nbsp;&nbsp;&nbsp;品&nbsp;&nbsp;数&nbsp;&nbsp;&nbsp;量：${totalqty}</p>
                   </div>
                   <div class="text">
                     <p style="font-weight:bold;">公&nbsp;司&nbsp;订&nbsp;单&nbsp;ID：${orderids }</p>
                   </div>
              </div>
              <div>
                <img id="u507_img" class="img " src="../img/u507.png" style="position: absolute;left: 970px;">
              </div>
               <div class="col-xs-4 form-group">
                   <div>
                     <p style="font-weight:bold;">入库状态：${isStorage }</p>
                   </div>
                   <div>
                     <p style="font-weight:bold;">采购人：${username }</p>
                   </div>
              </div>
         </div>
         <div  style="padding:15px;">
         <table id="categroyReport" class="imagetable">
                    <thead>
                       <tr>
                            <th width="50" style="text-align: center;">序号</th>
	                        <th width="180" style="text-align: center;">商品ID</th>
	                        <th width="150" style="text-align: center;">采购商品名称</th>
	                        <th width="150" style="text-align: center;">采购图片</th>
	                        <th width="190" style="text-align: center;">采购规格</th>
						    <th width="50"  style="text-align: center;">销售数量</th>
						    <th width="50"  style="text-align: center;">对应公司订单相同货源数量</th>
							<th width="180" style="text-align: center;">采购单价</th>
							<th width="80" style="text-align: center;">采购总额</th>
							<th width="200" style="text-align: center;">采购货源URL</th>
							<th width="100" style="text-align: center;">抓取时间</th>
							<th width="100" style="text-align: center;">备注</th>
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