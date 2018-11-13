<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>团购商品的同店铺商品选择</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <style>

        .check_sty {
            height: 24px;
            float: left;
            width: 24px;
        }

        .checkBg {
            background-color: #b6f5b6;
        }

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

    <script>

        function chooseBox(obj) {
            var is = $(obj).is(':checked');
            if (is) {
                $(obj).addClass("isChoose");
                $(obj).parent().parent().addClass("checkBg");
            } else {
                $(obj).removeClass("isChoose");
                $(obj).parent().parent().removeClass("checkBg");
            }
        }

        function addShopGoods(gbId,shopId) {
            $("#show_notice").show();
            var json = [];
            $(".isChoose").each(function() {
                var tempPam = {"shopId":shopId};
                tempPam["pid"] =$(this).val();
                json[json.length] = tempPam;
            });
            if (json.length == 0) {
                $.messager.alert("提醒", "请选择商品", "info");
            }else{
                $.ajax({
                    type : 'POST',
                    dataType : 'json',
                    url : '/cbtconsole/groupBuy/addShopGoods',
                    data : {
                        gbId : gbId,
                        shopId : shopId,
                        infos : JSON.stringify(json)
                    },
                    success : function(data) {
                        if (data.ok) {
                            $("#show_notice").text("执行成功，请关闭当前页面");
                        } else {
                            $("#show_notice").text(data.message);
                        }
                    },
                    error : function(XMLResponse) {
                        $("#show_notice").text("保存错误，请联系管理员");
                    }
                });
            }

        }
    </script>
</head>
<body>

<c:if test="${isShow == 0}">
    <h1 align="center">${message}</h1>
</c:if>
<c:if test="${isShow > 0}">

    <div>
        <h1 align="center">团购商品的同店铺商品选择(<b style="color:red;font-size:26px;">总数:${fn:length(goodsList)}</b>)</h1>
        <div style="text-align: center;">
            <input class="save_btn" type="button" value="保存商品" onclick="addShopGoods(${gbId},'${shopId}')">
            <span id="show_notice" style="color: red;display: none;">正在保存，请等待...</span>
        </div>
        <br>

        <c:if test="${fn:length(goodsList) > 0}">

            <table border="3" cellpadding="0" cellspacing="0" align="left">
                <c:set var="total1" value="${fn:length(goodsList)}"/>
                <c:set var="count1" value="0"/>
                <c:forEach items="${goodsList}" var="goods" varStatus="index">
                <c:set var="count1" value="${count1 + 1 }"/>
                <c:if test="${count1 == 1}">
                <tr>
                    </c:if>
                    <td ${goods.onlineFlag > 0 ? "class='checkBg'" : ""}>
                        <div class="div_sty">
                            <input type="checkbox" class="check_sty ${goods.onlineFlag > 0 ? ' isChoose' : ''}"
                                   ${goods.onlineFlag > 0 ? "checked='checked'" : ""} onclick="chooseBox(this)" value="${goods.pid}"/>
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
