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
<title>淘宝图片下载</title>
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
		url:'/cbtconsole/customerServlet?action=tbPictureDown&className=PictureComparisonServlet',
		data:{},
		success:function(res){
			setInterval("FnStart()",5000);
		}
	});
}

/* function FnStyle(){
	$.ajax({
		async: false, 
		type:'POST',
		url:'/cbtconsole/customerServlet?action=tbStyleDown&className=PictureComparisonServlet',
		data:{},
		success:function(res){
			setInterval("FnStyle()",100);
		}
	});
} */


</script>
<body>
<div>
<h3>淘宝图片下载</h3>
<div>
	&nbsp;&nbsp;&nbsp;&nbsp;<input onclick="FnStart()" type="button" value="淘宝图片下载">
	<c:if test="${aliCount!=null}">
		<div>
			淘宝下载数量${aliCount}
		</div>
	</c:if>
<!-- 	<br />
	&nbsp;&nbsp;&nbsp;&nbsp;<input onclick="FnStyle()" type="button" value="淘宝规格保存"> -->
</div>
</div>
</body>
</html>