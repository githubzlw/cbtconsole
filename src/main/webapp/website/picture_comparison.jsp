<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>AliExpress图片下载</title>
</head>
<script type="text/javascript">
function fnPictureDown(){
	window.location = "/cbtconsole/customerServlet?action=pictureDown&className=PictureComparisonServlet";
}
function fnPictureCompare(){
	window.location = "/cbtconsole/customerServlet?action=pictureCompare&className=PictureComparisonServlet";
}
function FnStart(){
	$.ajax({
		async: false, 
		type:'POST',
		url:'/cbtconsole/customerServlet?action=pictureDown&className=PictureComparisonServlet',
		data:{},
		success:function(res){
			setInterval("FnStart()",5000);
		}
	});
}

function FnStart1(){
	$.ajax({
		async: false, 
		type:'POST',
		url:'/cbtconsole/customerServlet?action=pictureCompare&className=PictureComparisonServlet',
		data:{},
		success:function(res){
			setInterval("FnStart1()",5000);
		}
	});
} 

</script>
<body>
<div>
<h3>AliExpress图片下载</h3>
<div>
	&nbsp;&nbsp;&nbsp;&nbsp;<input onclick="FnStart()" type="button" value="ali下载">
	<c:if test="${aliCount!=null}">
		<div>
			AliExpress下载数量${aliCount}
		</div>
	</c:if>
</div>
<div>
	&nbsp;&nbsp;&nbsp;&nbsp;<input onclick="FnStart1()" type="button" value="at图片比较">
</div>
</div>
</body>
</html>