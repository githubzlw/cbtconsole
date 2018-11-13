<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>翻译验证</title>
<script type="text/javascript">
	function query(){
	    var word=$("#searchKey").val();
	    if(word == null || searchKey == ""){
	        alert("请输入需要翻译的词");
	        return;
		}
		window.open("http://192.168.1.27:9089/translation/gettrans?cntext="+word+"");
	}
</script>
</head>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>

<!-- <div class="tran"> -->
<h1 style="text-align: center;">翻译验证</h1>
<!-- <h2 >1.中文语句翻译</h2> -->
<div align="center">
	<form action="/cbtconsole/customerServlet?action=findSearchKey&className=MoreActionServlet" onsubmit="" method="post">
		<table>
			<tr>
				<td>搜索词：<input type="text" id="searchKey" value="${searchKey}"name="searchKey" ></td> 
				<td><input type="button" onclick="query();" value="查询"></td>
			</tr>
		</table> 
	</form>
</div>
<br>
<div>
	<c:if test="${not empty gbbs }">
		<table id="table" align="center" border="1px" style="font-size: 20px;" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>中文名</th>
				<th>英文名</th>
			</Tr>
			<c:forEach items="${gbbs }" var="gbb" >
			<Tr>
				<td>${gbb.catName }</td>
				<td>${gbb.productName }</td>
			</Tr>
			</c:forEach>
		</table>
		</c:if>
</div>
</div>
</body>
</html>