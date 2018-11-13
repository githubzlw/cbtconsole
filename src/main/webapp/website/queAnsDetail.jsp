<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/xheditor-1.2.2.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/xheditor_lang/zh-cn.js"></script>
<link rel="stylesheet" type="text/css"
	href="xheditor_skin/vista/iframe.css" />
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<title>提问详情页</title>
</head>
<body>
<div style="border:solid 1px #aaa;width:100%;max-width:800px;padding:0px 40px;" >
<!-- style="margin-left:40%;margin-top:1%;width:1200px;" -->
	<h3>提问详情</h3>
	<form id="Form" name="Form" action="edit"method="post" style="height:100%;">
			<div style="margin-bottom:20px">
				提问序号：<input  name="questionid" id="questionid" value="${queAns.questionid}"
						readonly="readonly" style="border:none;" >
			</div>
			<div style="margin-bottom:20px">
			 提问用户：<input  name="userid" id="userid" value="${queAns.userid}" 
						readonly="readonly" style="border:none;">
				<input  name="parentqid" id="parentqid" value="${queAns.parentqid}" 
						type="hidden" readonly="readonly" style="border:none;" >
			</div>
			<div style="margin-bottom:20px">
				提问用户：<input  name="userName" id="userName" value="${queAns.userName}" 
						readonly="readonly" style="border:none;" >
			</div>
			<div style="margin-bottom:10px">
				商品编号：<input  name="pid" id="pid" value="${queAns.pid}" 
					readonly="readonly" style="border:none;" >
			</div>
			<div style="margin-bottom:20px">
			商品名字：<a href="${queAns.purl}" target="_blank">
						${queAns.pname}
					  </a>
				  <input type="hidden" name="pname" id="pname" value="${queAns.pname}">
				<input type="hidden" name="purl" id="purl" value="${queAns.purl}">
			</div>
			<div style="margin-bottom:20px">
				<%--  <p name="questionContent" id="questionContent" >提问内容：${queAns.questionContent}</p> --%>
			提问内容： <textarea name="questionContent" id="questionContent" readonly="readonly" 
						style="width: 80%;margin: -16px 0px 0px 80px;border:none;">${queAns.questionContent}</textarea>
			</div>
			<div style="margin-bottom:20px">
				提问时间：<input  name="createTime" id="createTime" value="${queAns.createTime}"
					readonly="readonly" style="border:none;">
			</div>
			<div style="margin-bottom:20px">
				<div>
					<c:if test="${queAns.replyContent != null}">
					回复内容：<input  name="replyContent" id="replyContent" value="${queAns.replyContent}">
					</c:if>
					<c:if test="${queAns.replyContent == null}">
						回复内容：<input  name="replyContent" id="replyContent" style="width: 60%;height: 20px;" ><br>
						<font color="red">请不要多于200字否则无法发送</font>
					</c:if>
				</div>
			</div>
			<div style="margin-bottom:20px">
				<c:if test="${queAns.replyTime != null}">
				回复时间：<input  name="replyTime" id="replyTime" value="${queAns.replyTime}"
						readonly="readonly" style="border:none;" >
				</c:if>
			</div>
			<div style="margin-bottom:20px">
				<c:if test="${queAns.replyName == null}">
					<input type="hidden" name="replyName" id="replyName" />
				</c:if>
				<c:if test="${queAns.replyName != null}">
					回复人：<input  name="replyName" id="replyName" value="${queAns.replyName}"
						readonly="readonly" style="border:none;" >
				</c:if>
			</div>
			<div style="margin-bottom:20px">
				显示回复：
				<%-- <input type="checkbox" name="isshow" id="isshow" value="1"<c:if test="${queAns.isshow ==1 }">checked</c:if>>隐藏 --%>
				<input type="checkbox" name="isshow" id="isshow" value="2">是
				<input type="hidden"  name="replyStatus" id="replyStatus" value="1" >
			<div style="margin-bottom:20px">
			电商页面显示：<input type="checkbox" name="questionIsShow" id="questionIsShow" value="2" >是
				<%-- <input type="radio" name="questionIsShow" id="questionIsShow" value="1" <c:if test="${queAns.questionIsShow ==1 }">checked</c:if>>不显示
				<input type="radio" name="questionIsShow" id="questionIsShow" value="2"<c:if test="${queAns.questionIsShow ==2 }">checked</c:if>>显示 --%>
			</div>
			<div style="text-align:center;padding:5px 0">
				<td style="text-align: center;" colspan="10">
					<a class="btn btn-mini btn-danger" href="#" onclick="save()">回复</a> 
					<a class="btn btn-mini btn-danger" href="/cbtconsole/question/questionlist">取消</a>
				</td>
			</div>
		</div>
	</form>
</div>
</body>
<script type="text/javascript">
function save() {
	var replyContent = $("#replyContent").val();
	var isshow =$("#isshow").val();
	var questionIsShow =$("#questionIsShow").val();
	if(replyContent == null||replyContent == ""){
		alert('回复内容不能为空 ');
		return false;
	}
	$("#Form").submit();
	
}
</script>
</html>