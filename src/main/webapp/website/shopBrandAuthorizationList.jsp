<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>店铺品牌信息</title>
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
<body>

<c:if test="${show == 0}">
    <h1 align="center">${msgStr}</h1>
</c:if>
<c:if test="${show > 0}">

    <h1>店铺【${shopId}】品牌数据</h1>
    <table id="shop_brand_table" border="1" cellpadding="1"
                   cellspacing="0" align="center">
                <thead>
                <tr align="center" bgcolor="#DAF3F5" style="height: 50px;">
                    <th style="width: 120px;">品牌ID</th>
                    <th style="width: 200px;">品牌名称</th>
                    <th style="width: 90px;">店铺ID</th>
                    <th style="width: 150px;">授权状态</th>
                    <th style="width: 440px;">有效期</th>
                    <th style="width: 150px;">创建时间</th>
                    <th style="width: 180px;">更新时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${brandAuthorizationList}" var="brandInfo" varStatus="status">
                    <tr bgcolor="#FFF7FB" style="height: 42px;">
                        <td>
                            ${brandInfo.id}
                        </td>
                        <td>
                            ${brandInfo.brandName}
                        </td>
                        <td>
                            ${brandInfo.authorizeState}
                        </td>
                        <td>
                            ${brandInfo.termOfValidity}
                        </td>
                        <td>
                            ${brandInfo.certificateFile}
                        </td>
                        <td>
                            ${brandInfo.createTime}
                        </td>
                        <td>
                            ${brandInfo.localPath}
                        </td>
                        <td>
                            ${brandInfo.remotePath}
                        </td>
                        <td>
                            ${brandInfo.updateTime}
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>


</c:if>

</body>
</html>
