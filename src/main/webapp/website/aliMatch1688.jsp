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
<body>


<table id="shop_category_id" border="1" cellpadding="1"
       cellspacing="0" align="center">
    <thead>
    <tr align="center" bgcolor="#DAF3F5" style="height: 50px;">
        <th style="width: 200px;">速卖通商品信息</th>
        <th style="width: 800px;">lire对标1688商品信息</th>
        <th style="width: 800px;">爆款对标商品信息</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${infos}" var="shopInfo" varStatus="status">
        <tr bgcolor="#FFF7FB" style="height: 42px;">
            <td>
                <div>

                </div>
            </td>
            <td>

                <div>

                </div>
            </td>
            <td>

                <div>

                </div>
            </td>
        </tr>
    </c:forEach>

    </tbody>
</table>

</body>
</html>
