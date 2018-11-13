<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="/cbtconsole/css/eight.css"/>
<script type="text/javascript" src="../js/jquery-1.10.2.js"></script>
<title>产品翻译结果页面</title>
<script type="text/javascript">
function fnpage(){
	var total = $('#total').val();
	var pageNext = parseInt($('#pageNext').val());
	if(pageNext==''||pageNext>total || pageNext < 1 ){
		return;
	}
	var indexid = $('#indexid').val();
	var minprice = $('#minprice').val();
	var maxprice = $('#maxprice').val();
	window.location.href="/cbtconsole/SearchByPicController/resultlist?indexid="+indexid+"&page="+pageNext+
	"&minprice="+minprice+"&maxprice="+maxprice;
}
function pageScroll() { 
	window.scrollBy(0,-10); 
	scrolldelay = setTimeout('pageScroll()',100); 
	}
	
//发送给顾客  	
function sendToCustom(){
	var indexid = $('#indexid').val();
	window.location.href="/cbtconsole/SearchByPicController/JspToPDF?indexId="+indexid+"&page="+1;
}
	 
</script>
</head>
<body>
<input type="hidden" id="indexid" value="${indexid}"> 
<div class="eixcon">
<input type="button" class="eigbtn" value="《发邮件给客户》" onclick="sendToCustom();"/>
	<div class="eiresul" id="searchInfo">
	<c:forEach  var="item" items="${items}"  varStatus="status">
	<li class="eireli">
				<div class="eirelidcon">
					<div class="eireimg">
						<a href="${item.goodsUrl }">
							<img width="250px" height="250px" class="rimgai" src="/cbtconsole/img/wy/grey.gif" data-original="${item.goodsImg }">
						</a>
					</div>
					<p class="eritxt">
						<label for="${item.goodsPid}" style="height: 50px;  width: 50px;  display: inline-block;  top:-81px;  left:12px;  clear:both;"></label> 
						<a href="${item.goodsUrl }"><span class="eirpname" title="${item.goodsNameEn}">${item.goodsNameEn}</span></a>
					</p>
					<p class="eritxt">
						<span class="eirprice"><em>$</em>${item.goodsPriceRe }</span>
						<font size="2px"> ${item.trade}</font>
					</p>
				</div>
			</li>
	
	</c:forEach>
		
	</div>
</div>
<div align="center" id="downpage" class="downpage">
<input type="hidden" id="total" value="${total}"> 
<c:set var="add" value=""></c:set>
<c:set var="release" value=""></c:set>
<c:if test="${current>1}">
<c:set var="release" value="/cbtconsole/searchresults/resultlist?indexid=${indexid}&page=${current-1}&minprice=${minprice}&maxprice=${maxprice}"></c:set>
</c:if>
<c:if test="${total>current}">
<c:set var="add" value="/cbtconsole/searchresults/resultlist?indexid=${indexid}&page=${current+1}&minprice=${minprice}&maxprice=${maxprice}"></c:set>
</c:if>

<div>当前页/总页数:&nbsp;<span>${current}/${total}</span>
	<input type="button" value="上一页" onclick="javaScript:window.location.href='${release}'">
	<input type="button" value="下一页" onclick="javaScript:window.location.href='${add}'">
	 至<input type="text" value=""  id="pageNext"><input type="button" value="跳转" onclick="fnpage()"  >
	页
</div>
</div>
<br>
<br>
<br>
<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.js"></script>
<script type="text/javascript">
	$(function() {
	    $(".rimgai").lazyload({
	    	effect:'fadeIn',
	    	threshold : 300
	    });
	});
</script>
</body>
</html>