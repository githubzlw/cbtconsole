<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>运费统计报表</title>

<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.imgbox.pack.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.base64.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/tableExport.js"></script>
   
<!-- CSS goes in the document HEAD or added to your external stylesheet -->
<link rel="stylesheet" href="/cbtconsole/css/warehousejs/lrtk.css" type="text/css">
<style type="text/css">
/* 表格样式 */
table.altrowstable {              
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #F4FFF4;           
	border-collapse: collapse;
}
table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
.oddrowcolor{
	background-color:#F4F5FF;
}
.evenrowcolor{
	background-color:#FFF4F7;            
}



/* 字体样式 */
body{font-family: arial,"Hiragino Sans GB","Microsoft Yahei",sans-serif;} 
p.thicker {font-weight: 900}

/* button样式 */
.className{
  line-height:30px;
  height:30px;
  width:70px;
  color:#ffffff;
  background-color:#3ba354;
  font-size:16px;
  font-weight:normal;
  font-family:Arial;
  border:0px solid #dcdcdc;
  -webkit-border-top-left-radius:3px;
  -moz-border-radius-topleft:3px;
  border-top-left-radius:3px;
  -webkit-border-top-right-radius:3px;
  -moz-border-radius-topright:3px;
  border-top-right-radius:3px;
  -webkit-border-bottom-left-radius:3px;
  -moz-border-radius-bottomleft:3px;
  border-bottom-left-radius:3px;
  -webkit-border-bottom-right-radius:3px;
  -moz-border-radius-bottomright:3px;
  border-bottom-right-radius:3px;
  -moz-box-shadow: inset 0px 0px 0px 0px #ffffff;
  -webkit-box-shadow: inset 0px 0px 0px 0px #ffffff;
  box-shadow: inset 0px 0px 0px 0px #ffffff;
  text-align:center;
  display:inline-block;
  text-decoration:none;
}
.className:hover{
  background-color:#1c9439;
}

/* 订单号 */
.someclass {
	text-indent : 0em;
	word-spacing : 1px;
	letter-spacing : 2px;
	text-align : left;
	text-decoration : none;
	font-family : monospace;
	color : #007007;
	font-weight : bold;
	font-size : 14pt;        
	background-color : #F0F0FF;
	border-color : transparent;
}

/* 查询条件文本框 */
.querycss {
	color : #00B026;        
	font-size : 12pt;
	border-width : 1px;
	border-color : #AFFFA0;
	border-style : solid;
	height : 23px;
	width : 120px;
}


/* 一页显示select */


.classSelect{
  line-height:30px;
  height:30px;
  width:157px;
  color:#ffffff;
  background-color:#3ba354;
  font-size:16px;
  font-weight:normal;
  font-family:Arial;
  border:0px solid #dcdcdc;
  -webkit-border-top-left-radius:3px;
  -moz-border-radius-topleft:3px;
  border-top-left-radius:3px;
  -webkit-border-top-right-radius:3px;
  -moz-border-radius-topright:3px;
  border-top-right-radius:3px;
  -webkit-border-bottom-left-radius:3px;
  -moz-border-radius-bottomleft:3px;
  border-bottom-left-radius:3px;
  -webkit-border-bottom-right-radius:3px;
  -moz-border-radius-bottomright:3px;
  border-bottom-right-radius:3px;
  -moz-box-shadow: inset 0px 0px 0px 0px #ffffff;
  -webkit-box-shadow: inset 0px 0px 0px 0px #ffffff;
  box-shadow: inset 0px 0px 0px 0px #ffffff;
  text-align:center;
  display:inline-block;
  text-decoration:none;
}
.classSelect:hover{
  background-color:#FAFFF4;         
}



</style>
<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
<link rel="stylesheet" href="css/orderinfo.css" type="text/css"> 
<script type="text/javascript">
//
//表格样式
function altRows(id){
	if(document.getElementsByTagName){  
		
		var table = document.getElementById(id);  
		var rows = table.getElementsByTagName("tr"); 
		 
		for(i = 1; i < rows.length; i++){          
			if(i % 2 == 0){
				rows[i].className = "evenrowcolor";
			}else{
				rows[i].className = "oddrowcolor";
			}      
		}
	}
}

window.onload=function(){
	
	
	var ordersLength = document.getElementById("ordersLength").value;
	for(var i=0; i<ordersLength; i++){
		altRows('alternatecolor'+(i+1));
	}
	
}

// 查询 提交
function aSubmit(){
	document.getElementById("idForm").submit();
}

//清空查询条件
function cleText(){
//	document.getElementById("idname").value="";
	$('input[type=text]').val("");
}
</script>

</head>
<body style="background-color : #F4FFF4;">
<form id="idForm" action="getOrderfeeFreight.do" method="get">
	<div align="center" >
		
		<div><H1>运费统计报表</H1></div>
		<div>          
           
			<!--订单号: 用户ID:-->
			<input class="querycss" style="width : 160px;" id="idname" name="orderid" value="${oip.orderid}" type="hidden"/>
			<input class="querycss" style="width : 100px;" id="idname" name="userid" value="${oip.userid}" type="hidden"/>
			
			 <button type="button" onclick="$('#tb_doc_m').tableExport({ type: 'excel', separator: ';', escape: 'false' });"  class="btn btn-default">
            <i class="glyphicon glyphicon-search">导出Excel</i>
        </button>
			<!-- readonly="readonly" -->
			开始日期:<input type="text" id="ckStartTime" name="ckStartTime"  value="${oip.ckStartTime}"  onfocus="WdatePicker({isShowWeek:true})"  />(0点0分0秒)
			结束日期:<input type="text" id="ckEndTime" name="ckEndTime" value="${oip.ckEndTime}"  onfocus="WdatePicker({isShowWeek:true})"  />(0点0分0秒)

			<a href='javascript:void(0);' onclick="aSubmit()" class='className'>查询</a>
			<a href='javascript:void(0);' onclick="cleText()" class='className'>清空</a>
			
		</div>           
		<div>
			<br/>          
			
			<!-- 选择运输方式 -->
			<select id="selectID"  onchange="window.location=this.value" >
				<option selected="selected" value='getOrderfeeFreight.do?trans_method=0&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>所有</option>
				<option selected="selected" value='getOrderfeeFreight.do?trans_method=1&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>4px</option>
				<option selected="selected" value='getOrderfeeFreight.do?trans_method=2&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>原飞航</option>
				<option selected="selected" value='getOrderfeeFreight.do?trans_method=3&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>佳成</option>
				<option selected="selected" value='getOrderfeeFreight.do?trans_method=4&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>其他</option>
			</select>       
			<script>
			//	alert('${oip.trans_method}');
				if('${oip.trans_method}'!=''){
					$("#selectID").get(0).selectedIndex ='${oip.trans_method}';  
				}
				
				         
			</script>
			
			当前第${oip.pageNum}页 &nbsp;&nbsp; 共${oip.pageCount}页&nbsp;&nbsp; 共${oip.pageSum }记录       
			<!-- class='classSelect' -->
			<select  onchange="window.location=this.value" >
				<option selected="selected" value='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>每页显示${oip.pageSize}条</option>
				
				<c:forEach begin="1" end="3" var="i">
					<c:if test="${i*50 != oip.pageSize}">
						<option value='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${i*50 }'> 每页显示${i*50}条</option>
					</c:if>
				</c:forEach>
				
			</select>          
			            
			                 
			 <!-- 销售与采购 

			 -->  
			
			 <a href='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=1&pageSize=${oip.pageSize }' class='className'>第一页</a>
			 <a href='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize }' class='className'>上一页</a>
			 <a href='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.nextPage }&pageSize=${oip.pageSize }' class='className'>下一页</a>
			 <a href='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.pageCount }&pageSize=${oip.pageSize }' class='className'>最后一页</a>
		</div>
		<div><h3></h3></div>
		<div>
				<div>
					     
					<div>
					<!-- id="alternatecolor${status.count}" -->
						<table id="tb_doc_m" width="1370px" class="altrowstable" >
							<tr class="someclass" ><td colspan="8">
							<div >
							运费一共是：<span id ="sumfreight_RMB"></span>(RMB)&nbsp;
							<input type="hidden" value="0" id="h_sumfreight_RMB">         
							
							<span id ="sumfreight_USD"></span>(USD)
							<input type="hidden" value="0" id="h_sumfreight_USD">
							
							
							</div>       
						             
							</td></tr>
	   						<tr style="background-color:#FAFFF4;">
	   							<td width="40px"><font style="font-size : 15px;">行号</font></td>
	  		  	    			<td width="50px"><font style="font-size : 15px;">用户ID</font></td>
						  		<td width="210px"><font style="font-size : 15px;">订单编号</font></td>
						  		<td width="210px"><font style="font-size : 15px;">快递单号</font></td>
				  				<td width="70px"><font style="font-size : 15px;">出货方式</font></td>
			  					<td width="220px"><font style="font-size : 15px;">出库时间</font></td>
								<td width="230px"><font style="font-size : 15px;">实际运费(USD)</font></td>
								<td width="230px"><font style="font-size : 15px;">实际运费(RMB)</font></td>
							</tr>
							                                                  
							<c:forEach items="${oip.orderfeelist}" var="orderfeelist" varStatus="s">
									<tr >
										<td><font style="font-size : 15px;">${s.index+1 }</font></td>
										<td><font style="font-size : 15px;">${orderfeelist.userid }</font></td>
										<td><font style="font-size : 15px;">${orderfeelist.orderno }</font></td>
										<td><font style="font-size : 15px;">${orderfeelist.express_no }</font></td>
										
										<td>
										<c:if test="${orderfeelist.trans_method=='1'}">
											<font style="font-size : 15px;">4px</font>
										</c:if>
										
										<c:if test="${orderfeelist.trans_method=='2'}">
											<font style="font-size : 15px;">原飞航</font>
										</c:if>
										
										<c:if test="${orderfeelist.trans_method=='3'}">
											<font style="font-size : 15px;">佳成</font>
										</c:if>
										
										<c:if test="${orderfeelist.trans_method=='4'}">
											<font style="font-size : 15px;">其他</font>
										</c:if>
										</td>
										
										<td><font style="font-size : 15px;">${orderfeelist.exporttime }</font></td>
										
										
										<c:if test="${orderfeelist.currency =='USD'}">
											<td>
												<font style="font-size : 15px;">
												${orderfeelist.acture_fee }<!-- (${orderfeelist.currency }) -->
												</font>
											</td>
											<td>
												<font style="font-size : 15px;">
												${orderfeelist.acture_fee_RMB }
												</font>
											</td>
										</c:if>
										
										<c:if test="${orderfeelist.currency !='USD'}">
										
											<td>
												<font style="font-size : 15px;">
												${orderfeelist.acture_fee }
												</font>
											</td>
											<td>
												<font style="font-size : 15px;">
												${orderfeelist.acture_fee_RMB }
												</font>
											</td>
										</c:if>
										  
										<!-- 累加运费 -->           
										
										
										<script>
										//	alert('${oip.trans_method}');
									//	setActure_fee_USD
											var acture_fee = ${orderfeelist.acture_fee_RMB };
											if(acture_fee == ''){
												acture_fee = 0;
											}
											$("#h_sumfreight_RMB").val((Number($("#h_sumfreight_RMB").val())+Number(acture_fee)).toFixed(2));
											$("#sumfreight_RMB").html($("#h_sumfreight_RMB").val());
											
											acture_fee = ${orderfeelist.acture_fee_USD };
											if(acture_fee == ''){
												acture_fee = 0;
											}
											$("#h_sumfreight_USD").val((Number($("#h_sumfreight_USD").val())+Number(acture_fee)).toFixed(2));
											$("#sumfreight_USD").html($("#h_sumfreight_USD").val());
											         
										</script>
									</tr>
							</c:forEach>        
							
						</table>                                   
					</div>
				</div>
		</div>
		<!-- 用来空行 -->
		    
		<div>
		<!-- class='classSelect' -->
			<select  onchange="window.location=this.value" >
				<option selected="selected" value='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>每页显示${oip.pageSize}条</option>
				
				<c:forEach begin="1" end="3" var="i">
					<c:if test="${i*50 != oip.pageSize}">
						<option value='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${i*50 }'> 每页显示${i*50}条</option>
					</c:if>
				</c:forEach>
				
			</select>
			 <a href='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=1&pageSize=${oip.pageSize }' class='className'>第一页</a>
			 <a href='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize }' class='className'>上一页</a>
			 <a href='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.nextPage }&pageSize=${oip.pageSize }' class='className'>下一页</a>
			 <a href='getOrderfeeFreight.do?trans_method=${oip.trans_method}&ckStartTime=${oip.ckStartTime}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.pageCount }&pageSize=${oip.pageSize }' class='className'>最后一页</a>
		</div>
		<div>
			<h4>当前第${oip.pageNum}页 &nbsp;&nbsp; 共${oip.pageCount}页&nbsp;&nbsp; 共${oip.pageSum }记录 </h4>
			
		</div>         
	</div>
	<input type="hidden"  id="ordersLength" value="${fn:length(oip.orderfeelist)}"/>  <!-- 商品个数  用来做表格颜色-->
	<input type="hidden"  id="pageNum" value="${oip.pageNum}"/>					   <!-- 当前第几页  submit 提交需要的值 -->
	<input type="hidden"  name="pageSize" value="${oip.pageSize}"/>				   <!-- 每页显示多少条  submit 提交需要的值 -->
    <input type="hidden"  name="trans_method" value="${oip.trans_method}"/>			<!--	    运输方式 -->          
	
	                             
	</form>
	
</body>
</html>