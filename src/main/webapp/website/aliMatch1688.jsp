<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>速卖通对标1688</title>
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
<style>
    .img_sty {
        max-height: 180px;
        max-width: 180px;
    }
</style>
<body>

<div>
    <form action="/produceCtr/queryForList">
        AliPid:<input type="text" name="aliPid" value="${aliPid}"/>
        keyword:<input type="text" name="keyword" value="${keyword}"/>
        adminId:<input type="text" name="adminId" value="${adminId}"/>
        &nbsp;&nbsp;&nbsp;<input type="button" value="查询">
    </form>
</div>

<table id="shop_category_id" border="1" cellpadding="1"
       cellspacing="0" align="center">
    <thead>
    <tr align="center" bgcolor="#DAF3F5" style="height: 50px;">
        <th style="width: 200px;">速卖通商品信息</th>
        <th style="width: 800px;" colspan="4">lire对标1688商品信息</th>
        <th style="width: 800px;" colspan="4">爆款对标商品信息</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${infos}" var="aliGd" varStatus="status">
        <tr bgcolor="#FFF7FB" style="height: 42px;">
            <td>
                <div>
                    <img class="img_sty" src="${aliGd.aliImg}"/>
                    <a target="_blank" href="${aliGd.aliUrl}">${aliGd.aliUrl}</a>
                    <span>关键词:${aliGd.keyword}</span>
                    <span>价格:${aliGd.aliPrice}</span>
                    <span>AliPid:${aliGd.aliPid}</span>
                </div>
            </td>
            <c:if test="${fn:length(aliGd.productListLire)}">
                <c:forEach items="${aliGd.productListLire}" var="lireGd">
                    <td>

                        <div>
                            <img class="img_sty" src="${lireGd.img}"/>
                            <a target="_blank" href="${lireGd.url}">${lireGd.url}</a>
                            <span>价格:${lireGd.price}</span>
                            <span>Pid:${aliGd.pid}</span>
                        </div>
                    </td>
                </c:forEach>
            </c:if>
            <c:if test="${fn:length(aliGd.productListPython)}">
                <c:forEach items="${aliGd.productListPython}" var="pyGd">
                    <td>
                        <div>
                            <img class="img_sty" src="${pyGd.img}"/>
                            <a target="_blank" href="${pyGd.url}">${pyGd.url}</a>
                            <span>价格:${pyGd.price}</span>
                            <span>Pid:${aliGd.pid}</span>
                        </div>
                    </td>
                </c:forEach>
            </c:if>

        </tr>
    </c:forEach>

    </tbody>
</table>

<div>
    <form id="submit_form" action="/produceCtr/queryForList">
        <input type="hidden" id="page_ali_pid" name="aliPid" value="${aliPid}"/>
        <input type="hidden" id="page_keyword" name="keyword" value="${keyword}"/>
        <input type="hidden" id="page_adminId" name="adminId" value="${adminId}"/>
        <input type="hidden" id="page_num" name="page" value="${page}"/>
        <input type="button" value="翻页">
        <input style="display: none;" type="submit" value="提交隐藏">
    </form>
</div>
</body>
</html>
