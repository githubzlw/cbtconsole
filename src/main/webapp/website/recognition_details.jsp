<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/tableExport.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
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

.div img{

	width: 100%;

	height: 100%;

	cursor: pointer;

	transition: all 0.6s;

	-ms-transition: all 0.8s;

}

.div img:hover{

	transform: scale(5);

	-ms-transform: scale(5);

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
//查询 提交
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
<form id="idForm" action="recognition_date_details" method="get">
	<div align="center" >
		<div><H1>详情图下架记录</H1></div>
		<div>
			pid:<input class="querycss" style="width : 160px;" id="pid" name="pid" value="${oip.express_code}" type="text"/>
			开始日期:<input type="text" id="startTime" name="startTime"  value="${oip.ckStartTime}"  onfocus="WdatePicker({isShowWeek:true})"  />(0点0分0秒)
			结束日期:<input type="text" id="endTime" name="endTime" value="${oip.ckEndTime}"  onfocus="WdatePicker({isShowWeek:true})"  />(0点0分0秒)
			<a href='javascript:void(0);' onclick="cleText()" class='className'>清空</a>
			<a href='javascript:void(0);' onclick="aSubmit()" class='className'>查询</a>
			
		</div>
		<div>
				<div align="center">
				<table class="altrowstable">
						<tr>
							<td>行号</td>
							<td>产品编号</td>
							<td>pid</td>
							<td>下架时间</td>
							<td>商品图片</td>
						</tr>
					<c:forEach items="${customGoodsList }" var="obj" varStatus="s">
						<tr>
							<td>${s.index+1 }</td>
							<td>${obj.id }</td>
							<td>${obj.pid }</td>
							<td>${obj.createtime }</td>
							<td class="div"><img src="${obj.remotepath }" style="width:50px; height:50px;"></td>
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
				<option selected="selected" value='getExpressRecord.do?pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>每页显示50条</option>
				
				<c:forEach begin="1" end="3" var="i">
					<c:if test="${i*50 != oip.pageSize}">
						<option value='recognition_date_details?pageNum=${oip.previousPage }&pageSize=${i*50 }'> 每页显示${i*50}条</option>
					</c:if>
				</c:forEach>
				
			</select>
			
			 <a href='recognition_date_details?pageNum=1&pageSize=50' class='className'>第一页</a>
			 <a href='recognition_date_details?pageNum=${oip.previousPage }' class='className'>上一页</a>
			 <a href='recognition_date_details?pageNum=${oip.nextPage }' class='className'>下一页</a>
			 <a href='recognition_date_details?pageNum=${oip.pageCount }' class='className'>最后一页</a>
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