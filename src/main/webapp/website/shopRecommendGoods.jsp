<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>店铺推荐商品</title>
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
        .img_sty {
            max-height: 160px;
            max-width: 160px;
        }
    </style>
    <script type="text/javascript">

    </script>
</head>
<body>
<c:if test="${isShow == 0}">
    <h1 style="text-align: center;">${message}</h1>
</c:if>


<c:if test="${isShow > 0}">


    <div>
        <h3 style="text-align: center">店铺推荐商品</h3>
        <span>店铺ID:<input />&nbsp;</span>
        <button class="btn_sty" onclick="beforeAddInfo('', 0, 1, 0)">添加店铺</button>
        <button class="btn_sty" style="margin-left: 60px;" onclick="syncToOnline()">刷新到线上</button>
        <span id="show_message" style="color: red;display: none;">正在执行...</span>
    </div>

    <div>
        <c:forEach items="${list}" var="gd">
            <div class="div_sty">
                <input id="ck_${gd.pid}" type="checkbox" value="${gd.pid}"/>
                <img class="img_sty" src="${gd.customMainImage}"/>
                <p>
                    <span><em>$</em>${gd.rangePrice}</span>&nbsp;&nbsp;
                    <c:if test="${gd.isSoldFlag > 0}">
                        <span style="color: green;">免邮</span>
                    </c:if>

                </p>
                <a href="/cbtconsole/editc/detalisEdit?pid=${gd.pid}" target="_blank">编辑商品</a>
                <p>
                    <button class="btn_sty" onclick="openShopGoods('${gd.shopId}')">设为封面</button>
                </p>
            </div>
        </c:forEach>
    </div>


</c:if>

</body>
</html>
