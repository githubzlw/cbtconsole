<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>童装网站店铺推荐设置</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <style type="text/css">
        .img_min {
            max-width: 100px;
            max-height: 100px;
        }
    </style>
</head>
<body>
<c:if test="${isShow == 0}">
    <h1 style="text-align: center;">${message}</h1>
</c:if>


<c:if test="${isShow > 0}">

    <div>
        <h3 style="text-align: center">童装网站店铺推荐设置</h3>
        <button class="btn_sty" onclick="beforeAddInfo(0)">添加店铺</button>
        <button class="btn_sty" style="margin-left: 60px;" onclick="syncToOnline()">刷新到线上</button>
        <span id="show_message" style="color: red;display: none;">正在执行...</span>
    </div>

    <div>
        <c:forEach items="${list}" var="info">
            <div>
                <input type="hidden" value="${info.coverPid}"/>
                <img class="img_min" src="${info.coverImg}"/>
                <br>
                <a href="https://www.import-express.com/shop?sid=${info.shopId}" target="_blank">${info.shopId}</a>
                <br>
                <ul>
                    <c:if test="${not empty info.goodsList}">
                        <c:forEach items="${info.goodsList}" var="gd">
                            <li>
                                <img src="${gd.goodsImg}">
                            </li>
                        </c:forEach>
                    </c:if>
                </ul>
            </div>
        </c:forEach>
    </div>


</c:if>

</body>
</html>
