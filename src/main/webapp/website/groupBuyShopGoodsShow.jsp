<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>团购商品的同店铺商品查看</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <style>


        .img_sty {
            max-width: 180px;
            max-height: 180px;
        }

        .save_btn {
            border-radius: 10px;
            height: 40px;
            width: 80px;
            margin-right: 40px;
            border-color: #2e6da4;
            color: white;
            background-color: green;
        }

        .div_sty {
            width: 350px;
        }

    </style>

</head>
<body>

<c:if test="${isShow == 0}">
    <h1 align="center">${message}</h1>
</c:if>
<c:if test="${isShow > 0}">

    <div>
        <h1 align="center">团购商品的同店铺商品查看(<b style="color:red;font-size:26px;">总数:${fn:length(infos)}</b>)</h1>

        <c:if test="${fn:length(infos) > 0}">

            <table border="3" cellpadding="0" cellspacing="0" align="left">
                <c:set var="total1" value="${fn:length(infos)}"/>
                <c:set var="count1" value="0"/>
                <c:forEach items="${infos}" var="goods" varStatus="index">
                <c:set var="count1" value="${count1 + 1 }"/>
                <c:if test="${count1 == 1}">
                <tr>
                    </c:if>
                    <td>
                        <div class="div_sty">
                            <div>
                                <a target="_blank" href="https://detail.1688.com/offer/${goods.pid}.html"><img
                                        class="img_sty" src="${goods.imgUrl}"/></a>
                                <br>价格:<span>${goods.showPrice}</span><span>$</span>
                                <br>名称:<span>${goods.enName}</span>
                            </div>
                            <br>
                            <div style="margin-bottom: 2px;">
                                <c:if test="${goods.enInfoNum == 0}">
                                    <b><a target="_blank" style="color:#f50ed8"
                                          href="/cbtconsole/editc/detalisEdit?pid=${goods.pid}">编辑详情(无详情图片)</a></b>
                                </c:if>
                                <c:if test="${goods.enInfoNum > 0}">
                                    <a target="_blank"
                                       href="/cbtconsole/editc/detalisEdit?pid=${goods.pid}">编辑详情</a>
                                </c:if>
                            </div>
                        </div>
                    </td>
                    <c:if test="${count1 % 5 == 0}">
                </tr>
                <tr>
                    </c:if>
                    </c:forEach>
                    <c:if test="${total1 % 4 > 0}">
                </tr>
                </c:if>
            </table>
            <br>

        </c:if>


    </div>

</c:if>

</body>
</html>
