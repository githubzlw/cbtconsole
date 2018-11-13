<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>已出库列表</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery-1.2.6.pack.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.messager.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.base64.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/tableExport.js"></script>
<!-- CSS goes in the document HEAD or added to your external stylesheet -->
<style type="text/css">
table.altrowstable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #a9c6c9;
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
  width:88px;
  color:#ffffff;
  background-color:#7fa7e8;
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
  background-color:#4a8cf7;
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
	background-color : #E0F8FF;
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
  background-color:#7fa7e8;
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
  background-color:#4a8cf7;
}

/* 审核按钮 */
.classSh{
  line-height:24px;
  height:24px;
  width:85px;
  color:#141513;
  background-color:#c9d9f2;
  font-size:14px;
  font-weight:400;
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
.classSh:hover{
  background-color:#b6ccf0;
}
/* 审核通过 按钮*/
.classSHTG{
  line-height:25px;
  height:25px;
  width:77px;
  color:#ffffff;
  background-color:#6dab5e;
  font-size:15px;
  font-weight:normal;
  font-family:Arial;
  border:1px solid #ffffff;
  -webkit-border-top-left-radius:4px;
  -moz-border-radius-topleft:4px;
  border-top-left-radius:4px;
  -webkit-border-top-right-radius:4px;
  -moz-border-radius-topright:4px;
  border-top-right-radius:4px;
  -webkit-border-bottom-left-radius:4px;
  -moz-border-radius-bottomleft:4px;
  border-bottom-left-radius:4px;
  -webkit-border-bottom-right-radius:4px;
  -moz-border-radius-bottomright:4px;
  border-bottom-right-radius:4px;
  -moz-box-shadow: inset 0px 20px 0px -24px #e67a73;
  -webkit-box-shadow: inset 0px 20px 0px -24px #e67a73;
  box-shadow: inset 0px 20px 0px -24px #e67a73;
  text-align:center;
  display:inline-block;
  text-decoration:none;
}
.classSHTG:hover{
  background-color:#3bc306;
}
/* 有问题 按钮*/
.classYWT{
  line-height:25px;
  height:25px;
  width:77px;
  color:#ffffff;
  background-color:#f78d83;
  font-size:15px;
  font-weight:normal;
  font-family:Arial;
  border:1px solid #ffffff;
  -webkit-border-top-left-radius:4px;
  -moz-border-radius-topleft:4px;
  border-top-left-radius:4px;
  -webkit-border-top-right-radius:4px;
  -moz-border-radius-topright:4px;
  border-top-right-radius:4px;
  -webkit-border-bottom-left-radius:4px;
  -moz-border-radius-bottomleft:4px;
  border-bottom-left-radius:4px;
  -webkit-border-bottom-right-radius:4px;
  -moz-border-radius-bottomright:4px;
  border-bottom-right-radius:4px;
  -moz-box-shadow: inset 0px 20px 0px -24px #e67a73;
  -webkit-box-shadow: inset 0px 20px 0px -24px #e67a73;
  box-shadow: inset 0px 20px 0px -24px #e67a73;
  text-align:center;
  display:inline-block;
  text-decoration:none;
}
.classYWT:hover{
  background-color:#eb675e;
}


</style>
<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>
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
	altRows("alternatecolor");
	
}
//已出库列表  点击快点单号文本框 如果是提示信息清空
function updateExperssNo(id){
	var express_no = $('#express_no'+id).val();
	var order_no = $('#order_no'+id).html();
	$.ajax({
		type:'post',
		url:'updateExperssNo.do',
		data:{express_no:express_no,order_no:order_no,logistics_name:$("#logistics_name"+id).val()},
		success:function(retData){
			if(Number(retData)>0){
				
				$.messager.show(0, '修改成功',1800);
			}else{
				$.messager.show(0, '修改失败',1800);
			}
		}
	})
	
//	alert(express_no+"----"+order_no);
}

// 查询 提交
function aSubmit(){
	document.getElementById("idForm").submit();
}

//清空查询条件
function cleText(){
//	document.getElementById("idname").value="";

	$('#iduserid').val("");
	$('#idname').val("");
	//$('input[type=text]').val("");
}
</script>

</head>
<body style="background-color : #F4FFF4;">
<form id="idForm" action="getForwarder.do" method="get">
	<div align="center" >
		<div><H1>已出库列表</H1></div>
		<div>                                                   
			用户id:<input class="querycss" style="width : 160px;" id="iduserid" name="userid" value="${oip.userid}" type="text"/>

			订单号:<input class="querycss" style="width : 160px;" id="idname" name="orderid" value="${oip.orderid}" type="text"/>
			快递单号:<input class="querycss" style="width : 160px;" id="express_no" name="express_no" value="${oip.express_no}" type="text"/>
			<!-- 用户ID: --><input class="querycss" style="width : 100px;" id="idname" name="userid" value="${oip.userid}" type="hidden"/>
			<a href='javascript:void(0);' onclick="cleText()" class='className'>清空</a>
			<a href='javascript:void(0);' onclick="aSubmit()" class='className'>查询</a>
			
		</div>
		<div>
		    <br/>
		    <span>当前第${oip.pageNum}页 &nbsp;&nbsp; 共${oip.pageCount}页&nbsp;&nbsp; 共${oip.pageSum }记录</span>
		    <select class='classSelect' onchange="window.location=this.value" >
				<option selected="selected" value='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>每页显示${oip.pageSize}条</option>
				
				<c:forEach begin="1" end="3" var="i">
					<c:if test="${i*50 != oip.pageSize}">
						<option value='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=${oip.previousPage }&pageSize=${i*50 }'> 每页显示${i*50}条</option>
					</c:if>
				</c:forEach>
				
			</select> 
			<a href='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=1&pageSize=${oip.pageSize }' class='className'>第一页</a>
			 <a href='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=${oip.previousPage }&pageSize=${oip.pageSize }' class='className'>上一页</a>
			 <a href='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=${oip.nextPage }&pageSize=${oip.pageSize }' class='className'>下一页</a>
			 <a href='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=${oip.pageCount }&pageSize=${oip.pageSize }' class='className'>最后一页</a>
		</div>
		<div><h3></h3></div>
		<div>
					<div>
						<table class="altrowstable" id="alternatecolor">
							<tr style="background-color:#FFECE4;">
							
								<td><font style="font-size : 15px;">用户id</font></td>
								<td><font style="font-size : 15px;">订单编号</font></td>
								<td><font style="font-size : 15px;">快递跟踪号</font></td>
								<td><font style="font-size : 15px;">出货方式</font></td>
								<td><font style="font-size : 15px;">打包运费</font></td>
								<td><font style="font-size : 15px;">打包重量</font></td>
								<td><font style="font-size : 15px;">打包体积</font></td>
								<td><font style="font-size : 15px;">实际重量</font></td>
								
								<td><font style="font-size : 15px;">出货时间</font></td>
								<td><font style="font-size : 15px;">实际计费重量</font></td>
								<td><font style="font-size : 15px;">实收金额</font></td>
							</tr>
							
							<c:forEach items="${oip.forwarderlist}" var="storageLocation" varStatus="s">
									<tr>
										<td>${storageLocation.id}------<font  style="font-size : 15px;">${storageLocation.userid }</font></td>
										<td><font id="order_no${s.index}" style="font-size : 15px;">${storageLocation.order_no }</font></td>
										<td><input id="express_no${s.index}" type="text" value='${storageLocation.express_no }'/><input name="upBnt" type="button" value="修改" onclick="updateExperssNo('${s.index}')"/></td>
										
										<td><font style="font-size : 15px;">
										<select id="logistics_name${s.index }">
											<option value="原飞航">原飞航</option>
											<option value="JCEX">JCEX</option>
											<option value="emsinten">emsinten</option>
										</select>
										<script>
										$("#logistics_name${s.index }").val('${storageLocation.logistics_name }');
										</script>
										
										
										</font></td>
										
										<td><font  style="font-size : 15px;">
										<fmt:formatNumber type="number" value="${storageLocation.inputPrice*6.58 }" pattern="0.00" maxFractionDigits="2"/> RMB
										
										</font></td>
										<td><font  style="font-size : 15px;">${storageLocation.inputWeight }(kg)</font></td>
									    <td><font  style="font-size : 15px;">${storageLocation.inputVolume }(cm<sup>3</sup>)</font></td>
										<td><font  style="font-size : 15px;">${storageLocation.esWeight }(kg)</font></td>
										<td><font style="font-size : 15px;">
										<fmt:formatDate value="${storageLocation.createtime}" type="both"/>
										</font></td>
										
										<td><font style="font-size : 15px;">${storageLocation.settleWeight}</font></td>
										
										<td><font style="font-size : 15px;">${storageLocation.shipprice}</font></td>
									</tr>
							</c:forEach>
						</table>
					</div>
		</div>
		<!-- 用来空行 -->
		<div> 
		  <h3></h3>
		</div>
		<div>
			<select class='classSelect' onchange="window.location=this.value" >
				<option selected="selected" value='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>每页显示${oip.pageSize}条</option>
				
				<c:forEach begin="1" end="3" var="i">
					<c:if test="${i*50 != oip.pageSize}">
						<option value='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=${oip.previousPage }&pageSize=${i*50 }'> 每页显示${i*50}条</option>
					</c:if>
				</c:forEach>
				
			</select>
			
			 <a href='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=1&pageSize=${oip.pageSize }' class='className'>第一页</a>
			 <a href='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=${oip.previousPage }&pageSize=${oip.pageSize }' class='className'>上一页</a>
			 <a href='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=${oip.nextPage }&pageSize=${oip.pageSize }' class='className'>下一页</a>
			 <a href='getForwarder.do?express_no=${oip.express_no }&userid=${oip.userid}&orderid=${oip.orderid}&pageNum=${oip.pageCount }&pageSize=${oip.pageSize }' class='className'>最后一页</a>
		</div>
		<div>
			<h4>当前第${oip.pageNum}页 &nbsp;&nbsp; 共${oip.pageCount}页&nbsp;&nbsp; 共${oip.pageSum }记录 </h4>
			
		</div>
	</div>
	<input type="hidden"  id="ordersLength" value="${fn:length(oip.forwarderlist)}"/>  <!-- 商品个数  用来做表格颜色-->
	<input type="hidden"  id="pageNum" value="${oip.pageNum}"/>					   <!-- 当前第几页  submit 提交需要的值 -->
	<input type="hidden"  name="pageSize" value="${oip.pageSize}"/>				   <!-- 每页显示多少条  submit 提交需要的值 -->
	</form>
	
</body>
</html>