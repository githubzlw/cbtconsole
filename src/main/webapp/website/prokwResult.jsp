<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="../css/eight.css"/>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>图片+关键字搜索</title>
<script type="text/javascript">
function fnpage(){
	var currentpage = $('#current').val();
	var total = $('#total').val();
	var pageNext = $('#pageNext').val();
	if(pageNext==''||pageNext>total||pageNext<1){
		return;
	}
	window.location.href="/cbtconsole/SearchByPicController/getKeyWordsList?page="+pageNext;
}


</script>
</head>
<body>
<h3 class="eititl">客户需求管理界面</h3>


<div class="eixcon">
 <a href="/cbtconsole/SearchByPicController/select" target="_blank">信息列表</a> 
<table class="eitable">
<tr class="eigname">
	<td  width="100px">id</td>
	<td  width="100px">关键字</td>
	<td  width="100px">最低价格</td>
	<td  width="100px">最高价格</td>
	<td  width="50px">数量</td>
	<td  width="250px">说明</td>
	<td  width="100px">创建时间</td>
	<td  width="250px">邮箱</td>
	<td  width="100px">是否已发送邮件</td>
	<td  width="100px">数据管理</td>
	<td width="100px">进入单页</td>
</tr>

<c:forEach  items="${items}" var="item">
<tr>
	<td>${item.id}</td>
	<td>${item.keyWords }</td>
	<td>${item.minu }</td>
	<td>${item.maxu }</td>
	<td>${item.quantity }</td>
	<td>${item.comments }</td>
	<td>${item.createtime}</td>
	<td>${item.email }</td>
	<c:if test="${item.flag==1 }">
	<td>已发送邮件</td>
	</c:if>
	<c:if test="${item.flag==0 }">
	 <td><font  color="red">未发送邮件</font></td>
	</c:if>
	<td><a href="/cbtconsole/SearchByPicController/select?indexId=${item.index_id} " target="_blank">数据管理</a></td>
	<td><a href="/cbtconsole/SearchByPicController/correct?id=${item.id}&email=${item.email}" target="_blank">进入单页</a></td>
</tr>
</c:forEach>


</table>
	
</div>
<br>
<div align="center">
<input type="hidden" id="current" value="${current}"> 
<input type="hidden" id="total" value="${total}"> 
<c:set var="add" value=""></c:set>
<c:set var="release" value=""></c:set>
<c:if test="${current>1}">
<c:set var="release" value="/cbtconsole/SearchByPicController/getKeyWordsList?page=${current-1}"></c:set>
</c:if>
<c:if test="${total>current}">
<c:set var="add" value="/cbtconsole/SearchByPicController/getKeyWordsList?page=${current+1}"></c:set>
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

</body>
</html>