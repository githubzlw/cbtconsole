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
<title>管理界面</title>
<script type="text/javascript">
function fnpage(){
	var currentpage = $('#current').val();
	var total = $('#total').val();
	var pageNext = $('#pageNext').val();
	if(pageNext==''||pageNext>total||pageNext<1){
		return;
	}
	window.location.href="/cbtconsole/SearchByPicController/select?page="+pageNext;
}

/* 同步 */
/* function  SynchData(indexId){
	$.ajax({
		type:"post",
		url:"/cbtconsole/SynchDataServlet",
		data:{indexId:indexId},
		success:function(res){
			if(res=="true"){
				alert("更新成功 ！ ");
			}else{
				alert("数据已经存在 |更新出错 ");
			}
		}
	})
}
 */


//将数据同步到美服 
function SynchData(obj){
	$(".result").html("");
	var indexid= obj;
	if(indexid===''){
		return false;
	}
	$(".result").html("Index Id :"+indexid+"   数据同步正在处理中，请稍后......");
	scroll(0,0);
	$.ajax({
		type:'GET',
		dataType:'text',
		url:'/SearchByPicController/syncdata',
		data:{indexid:indexid},
		success:function(res){
	       $("#result").html("Index Id :"+indexid+"   "+res);
		},
		error:function(XMLResponse){
		}
	});
}


</script>
</head>
<body>
<h3 class="eititl">管理界面</h3>
<div id="result" class="result" align="center" style="color: Red;font-size: 30px;font-weight: bold;"></div>
<br>
<div class="eixcon">
<table class="eitable">
<tr class="eigname">
	<td  width="100px">IndexID</td>
	<td  width="100px">翻译类目</td>
	<td  width="100px">英文名</td>
	<td  width="100px">中文名</td>
	<td  width="250px">更新时间</td>
	<td  width="250px">翻译时间</td>
	<td  width="100px">修改参数</td>
	<td width="200px">选择搜索结果</td>
	<td width="120px">状态</td>
	<td width="100px">同步数据</td>
</tr>

<c:forEach  items="${items}" var="item">
<tr>
	<td>${item.id}</td>
	<td>
	<c:if test="${item.translationCatid==1}">
	服装&amp;鞋
	</c:if>
	<c:if test="${item.translationCatid==2}">
	消费电子及配件
	</c:if>
	<c:if test="${item.translationCatid==3}">
	办公用品
	</c:if>
	<c:if test="${item.translationCatid==4}">
	家庭用品,家具&amp;园艺
	</c:if>
	<c:if test="${item.translationCatid==5}">
	首饰
	</c:if>
	<c:if test="${item.translationCatid==6}">
	手表
	</c:if>
	<c:if test="${item.translationCatid==7}">
	箱包
	</c:if>
	<c:if test="${item.translationCatid==8}">
	母婴用品
	</c:if>
	<c:if test="${item.translationCatid==9}">
	运动户外
	</c:if>
	<c:if test="${item.translationCatid==10}">
	美妆
	</c:if>
	<c:if test="${item.translationCatid==11}">
	汽配&amp;摩托
	</c:if>
	<c:if test="${item.translationCatid==12}">
	玩具
	</c:if>
	<c:if test="${item.translationCatid==13}">
	灯具
	</c:if>
	</td>
	<td>${item.enName}</td>
	<td>${item.cnName}</td>
	<td>${item.createTime}</td>
	<td>${item.translationTime}</td>
	<td><a href="/cbtconsole/SearchByPicController/correct?indexid=${item.id}&id=${item.customId}" target="_blank">修改参数</a></td>
	<td><a href="/cbtconsole/SearchByPicController/resultlist?indexid=${item.id}&page=1" target="_blank">选择搜索结果</a></td>
	<td>
	<c:if test="${item.flag==0}">
	  <span style="color: RED;">数据下载中...</span>
	</c:if>
	<c:if test="${item.flag==1}">
	  数据下载完成
	</c:if>
	<c:if test="${item.flag==2}">
	  数据更新中...
	</c:if>
	<c:if test="${item.flag==3}">
	  数据更新完成
	</c:if>
	</td>
	<td><button onclick="SynchData(${item.id})">同步数据</button></td>
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
<c:set var="release" value="/cbtconsole/SearchByPicController/select?page=${current-1}"></c:set>
</c:if>
<c:if test="${total>current}">
<c:set var="add" value="/cbtconsole/SearchByPicController/select?page=${current+1}"></c:set>
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