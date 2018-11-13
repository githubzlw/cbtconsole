<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
	response.flushBuffer();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>商品编辑日志</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
        src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
        src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
        href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
        href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
</head>
<html>

<body>

<c:if test="${isShow == 0}">
    <h1 style="text-align: center;color: red;">${message}</h1>
</c:if>

<c:if test="${isShow > 0}">
    <table style="width:98%;border-color: #cfe610;" border="1" cellpadding="1" cellspacing="0" align="center">
        <caption style="color: #1d48e2;font-size: 24px;">商品[<a href="/cbtconsole/editc/detalisEdit?pid=${pid}" target="_blank" title="点击进入编辑详情">${pid}</a>]编辑日志</caption>
        <thead>
            <tr align="center" bgcolor="#ccc">
                <td style="width: 280px;">原标题</td>
                <td style="width: 280px;">新标题</td>
                <td style="width: 120px;text-align: center;">重量不合理标注</td>
                <td style="width: 100px;text-align: center;">难看标注</td>
                <td style="width: 100px;text-align: center;">操作人</td>
                <td style="width: 150px;text-align: center;">操作时间</td>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${editList}" var="editBean" varStatus="status">
                <tr>
                    <td>${editBean.old_title}</td>
                    <td>${editBean.new_title}</td>
                    <td style="text-align: center">${editBean.weight_flag > 0 ? '重量不合理':''}</td>
                    <td style="text-align: center">${editBean.ugly_flag > 0 ? '难看':''}</td>
                    <td style="text-align: center">${editBean.admin_name}</td>
                    <td>${editBean.create_time}</td>
                </tr>
            </c:forEach>

        </tbody>
    </table>
</c:if>

</body>
</html>