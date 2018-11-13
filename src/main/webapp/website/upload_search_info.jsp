<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>

<title>客户搜索信息</title>
<script type="text/javascript">
function fnSelect(value){
	document.getElementById("type").value=value;
}
</script>
<style type="text/css">
	ul li{list-style:none;}
</style>
</head>
<body>
<div align="center">
	<form action="/cbtconsole/customerServlet?action=customorSearchInfo&className=MoreActionServlet" onsubmit="" method="post">
		<table>
			<tr>
<%-- 				<td>userid:<input type="text" value="${userId }" id="userid" name="userId"><font id="ts" color="red"></font></td>
				<td>username:<input type="text" value="${userName }" id="username" name="userName"></td>
				<td>useremail:<input type="text" id="useremail" value="${useremail}"name="useremail" ></td> --%>
				<td style="width: 120px;">类别:
					<select id="maxSelect" style="width:200px" onchange="fnSelect(this.value)" > 
								<option selected="selected"></option>
			             <c:forEach var="largeIndexList" items="${largeIndexList}">
			             	<c:if test="${maxSelectId==largeIndexList.indexName}">
			             		<option value="${largeIndexList.indexName}" selected="selected">${largeIndexList.indexNameCn}</option>
			             	</c:if>
			             	<c:if test="${maxSelectId!=largeIndexList.indexName}">
			             		<option value="${largeIndexList.indexName}">${largeIndexList.indexNameCn}</option> 
			             	</c:if>
			             </c:forEach>
		          </select>
		          <td style="width: 120px;">开始日期：<input id="startdate" name="startdate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value="${startdate}" /></td>
					<td style="width: 120px;">结束日期：<input id="enddate" name="enddate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value="${enddate}" /></td>
						
		          <input type="hidden" id="type" name ="type">
		        </td>
				<td><input type="submit" value="查询"></td>
			</tr>
		</table> 
	</form>
	</div>
	<div>
		<table id="table" align="center" border="1px" style="font-size: 13px;" width="800" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>序号</th>
				<th>图片</th>
				<th>所选类别</th>
				<th>关键词</th>
				<th>时间</th>
			</Tr>
			<c:forEach items="${cusSrearchList }" var="gbb" varStatus="i">
				<Tr>
					<td>${i.index+1 }</td>
					<td> <img  width='100px' title="" height='100px;' src="${pageContext.request.contextPath}/searchupload/${gbb.imgName }" style='' ></td>
					<td>${gbb.indexNameCn }</td>
					<td>${gbb.keyWord }</td>
					<td>${gbb.createTime }</td>
				</Tr>
			</c:forEach>
		</table>
		</br>
		<c:if test="${!empty  cusSrearchList}">
			<div align="center">${pager }</div> 
		</c:if>
 		
	</div>
</body>
</html>