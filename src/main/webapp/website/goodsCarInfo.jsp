<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>购物车信息</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <style>
        .goods_name {
            display: block;
            color: #6057da;
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
            padding-left: 5px;
            padding-top: 5px;
            text-decoration: none;
        }

        .btn_sty {
            margin: 10px 0 10px 20px;
            width: 80px;
            color: #fff;
            background-color: #5db5dc;
            border-color: #2e6da4;
            font-size: 16px;
            border-radius: 5px;
            border: 1px solid transparent;
        }

        .is_tr {
            background-color: #bfbb89;
        }
    </style>
</head>
<body>

<c:if test="${success == 0}">
    <h1 align="center">${message}</h1>
</c:if>
<c:if test="${success > 0}">


    <table style="width:96%;border-color: #b6ff00;" border="1" cellpadding="1" cellspacing="0" align="center">

        <caption style="font-size: 18px;">客户【${userId}】的购物车信息</caption>
        <tr align="center" bgcolor="#ccc">
            <td style="width: 70px;">序号</td>
            <td style="width: 95px;">购物车ID</td>
            <td style="width: 100px;">PID</td>
            <td style="width: 120px;">店铺ID</td>
            <td style="width: 220px;">商品信息</td>
            <td style="width: 80px;">商品价格</td>
            <td style="width: 80px;">购买数量</td>
            <td style="width: 80px;">交期</td>
            <td style="width: 130px;">备注</td>
            <td style="width: 80px;">改价</td>
            <td style="width: 150px;">操作</td>
        </tr>

        <c:forEach items="${list}" var="carInfo" varStatus="status">
            <tr>
                <td style="text-align: center;">${status.index + 1}</td>
                <td>${carInfo.id}</td>
                <td>${carInfo.itemid}</td>
                <td>${carInfo.shopid}</td>
                <td style="max-width: 360px;">
                    <a class="goods_name" href="${carInfo.goodsUrl}">${carInfo.goodsTitle}</a><br>
                    <div style="float: left;">
                        <img style="max-width: 80px;max-height: 80px;" src="${carInfo.googsImg}" alt="无图"/>
                    </div>
                    <div>
                        <span style="color: #ec6ed7;">规格:[${carInfo.goodsType}]</span><br>
                        <span>重量:${carInfo.perWeight}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                        <span>MOQ:${carInfo.normLeast}</span>

                    </div>
                </td>
                <td style="text-align: center;">
                    <c:if test="${carInfo.price1 > 0}">
                        ${carInfo.price1}
                    </c:if>
                    <c:if test="${carInfo.price1 == 0}">
                        ${carInfo.googsPrice}
                    </c:if>
                </td>
                <td style="text-align: center;">${carInfo.googsNumber}</td>
                <td style="text-align: center;">${carInfo.deliveryTime}</td>
                <td>${carInfo.remark}</td>
                <td style="text-align: center;">
                    <c:if test="${carInfo.price1 > 0}">
                        ${carInfo.googsPrice}
                    </c:if>
                </td>
                <td><input type="number" value="" style="width: 100px;"/>
                    <c:if test="${carInfo.price1 > 0}">
                        <input class="btn_sty" type="button" value="修改价格"
                               onclick="updateGoodsPrice(${carInfo.id},${userId},${carInfo.price1},this)"/>
                    </c:if>
                    <c:if test="${carInfo.price1 == 0}">
                        <input class="btn_sty" type="button" value="修改价格"
                               onclick="updateGoodsPrice(${carInfo.id},${userId},${carInfo.googsPrice},this)"/>
                    </c:if>
                    </td>
            </tr>
        </c:forEach>
    </table>

</c:if>

</body>
<script>
    function updateGoodsPrice(goodsId, userId,goodsPrice, obj) {
        setChooseTr(obj);
        var newPrice = $(obj).parent().find("input").val();
        if (goodsId == null || goodsId == "" || goodsId == 0) {
            alert("获取goodsId失败");
            return false;
        }
        if (userId == null || userId == "" || userId == 0) {
            alert("获取userId失败");
            return false;
        }
        if (newPrice == null || newPrice == "" || newPrice == 0) {
            alert("获取修改价格失败");
            return false;
        }
        $.ajax({
            type: 'POST',
            dataType: 'text',
            url: '/cbtconsole/shopCarMarketingCtr/updateCarGoodsPriceByUserId',
            data: {
                "goodsId": goodsId,
                "userId": userId,
                "goodsPrice": goodsPrice,
                "newPrice": newPrice
            },
            success: function (data) {
                var json = eval("(" + data + ")");
                if (json.ok) {
                    window.location.reload();
                } else {
                    alert(json.message);
                }
            },
            error: function () {
                alert("执行失败,请联系管理员");
            }
        });
    }

    function setChooseTr(obj) {
        //背景色变色
        $(obj).parent().parent().addClass("is_tr").siblings().removeClass("is_tr");
    }
</script>
</html>
