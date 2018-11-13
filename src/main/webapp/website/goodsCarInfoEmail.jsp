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
    </style>
</head>
<body>

<c:if test="${success == 0}">
    <h1 align="center">${message}</h1>
</c:if>
<c:if test="${success > 0}">


    <div style="max-height: 500px;overflow-y: auto">
        <table style="width:98%;border-color: #b6ff00;" border="1" cellpadding="1" cellspacing="0" align="center">

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
            </tr>

            <c:forEach items="${updateList}" var="carInfo" varStatus="status">
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
                </tr>
            </c:forEach>
        </table>
    </div>
    <div>
        <input type="button" value="确认并发送邮件给客户" onclick="confirmAndSendEmail(${userId},'${userEmail}')"/>
        <h2>邮件预览</h2>
    </div>
    <div style="height: auto" id="email_content">
        <div>
            <img style="cursor: pointer"
                 src="https://img1.import-express.com/importcsvimg/webpic/newindex/img/logo.png"/>
            <p style="font-weight: bolder; margin-bottom: 10px;">Dear&nbsp;&nbsp;${userEmail},</p>
            <p style="margin-bottom: 10px;">
            	<span style="display: inline-block;width: 800px;margin-bottom: 20px;">Thanks for visiting ImportExpress.com.  We have noticed that you added many items to the shopping cart.
            	We are now offering discounts to some of them and hope to win your business.
            	If you have other questions or concerns, please feel free to email us back.</span>
            	<br>
                <span style="font-weight: bold;font-size: 12px;">You have saved :USD&nbsp;${offCost}&nbsp;&nbsp;(${offRate}% Off)</span>
                &nbsp;<a style="color: #0070C0" href="https://www.import-express.com/Goods/getShopCar" target="_blank">Details</a><em>.</em>

            </p>

            <table style="width: 820px;font-size: 12px; border-color: #b6ff00;" id="email_update_table" border="1" cellpadding="1" cellspacing="0">
                <caption style="float: left;font-weight: bold;font-size: 18px;">Price reduced items</caption>
                <tbody>
                <tr>
                    <td colspan="6" style="border-top: 1px solid;"></td>
                </tr>
                <tr style="font-weight: bold;font-size: 12px;">
                    <td align="center" width="350px">Item Name &amp; Details</td>
                    <td align="center" width="140px">Item Price</td>
                    <td align="center" width="140px">Quantity</td>
                    <td align="center" width="140px">New Price</td>
                    <td align="center" width="140px">Total Price</td>
                    <td align="center" width="160px">Remark</td>
                </tr>
                <c:forEach items="${updateList}" var="carInfo" varStatus="status">
                    <tr>
                        <td>
                            <div style="width: 350px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">${carInfo.goodsTitle}
                            </div>
                            <div><img style="cursor: pointer" width="50px;" height="50px;" src="${carInfo.googsImg}"/>
                                <c:set var="typeList" value="${fn:split(carInfo.goodsType, ';')}"></c:set>
                                <c:forEach var="typeStr" items="${typeList}">
                                    <c:if test="${fn:length(typeStr) >0}">
                                        <span style="border: 1px solid #E1E1E1;">${typeStr}</span>&nbsp;
                                    </c:if>
                                </c:forEach>
                            </div>
                        </td>
                        <td align="center" width="140px">USD <span>${carInfo.price1}</span></td>
                        <td align="center" width="140px">${carInfo.googsNumber}&nbsp;${carInfo.goodsunit}&nbsp;</td>
                        <td align="center" width="140px">USD <span>${carInfo.googsPrice}</span></td>
                        <td align="center" width="140px">USD <span>${carInfo.totalPrice}</span></td>
                        <td width="160px">${carInfo.remark}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <table style="width: 820px;font-size: 12px;text-align: right">
                <tbody>
                <tr>
                    <td>Products Cart Total:</td>
                    <td style="width: 150px">USD&nbsp;&nbsp;${productCost}</td>
                </tr>
                <tr>
                    <td>Final Due:</td>
                    <td style="width: 150px">USD&nbsp;&nbsp;${actualCost}</td>
                </tr>
                </tbody>
            </table>

            <br>
            <table style="width: 820px;font-size: 12px; border-color: #b6ff00;" id="email_old_table" border="1" cellpadding="1" cellspacing="0">
                <caption style="float: left;font-weight: bold;font-size: 18px;width: 360px;">Others Items in your shopping Cart</caption>
                <tbody>
                <tr>
                    <td colspan="6" style="border-top: 1px solid;"></td>
                </tr>
                <tr style="font-weight: bold;font-size: 12px;">
                    <td align="center" width="350px">Item Name &amp; Details</td>
                    <td align="center" width="140px">Item Price</td>
                    <td align="center" width="140px">Quantity</td>
                    <td align="center" width="140px">Total Price</td>
                    <td align="center" width="160px">Remark</td>
                </tr>
                <c:forEach items="${sourceList}" var="carInfo" varStatus="status">
                    <tr>
                        <td>
                            <div style="width: 350px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">${carInfo.goodsTitle}
                            </div>
                            <div><img style="cursor: pointer" width="50px;" height="50px;" src="${carInfo.googsImg}"/>
                                <c:set var="typeList" value="${fn:split(carInfo.goodsType, ';')}"></c:set>
                                <c:forEach var="typeStr" items="${typeList}">
                                    <c:if test="${fn:length(typeStr) >0}">
                                        <span style="border: 1px solid #E1E1E1;">${typeStr}</span>&nbsp;
                                    </c:if>
                                </c:forEach>
                            </div>
                        </td>
                        <td align="center" width="140px">USD <span>${carInfo.googsPrice}</span></td>
                        <td align="center" width="140px">${carInfo.googsNumber}&nbsp;${carInfo.goodsunit}&nbsp;</td>
                        <td align="center" width="140px">USD <span>${carInfo.totalPrice}</span></td>
                        <td width="160px">${carInfo.remark}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <span><a href="https://www.import-express.com" target="_blank" style="border-color: #2e6da4;font-size: 16px;border-radius: 5px;color: #26ef19;background-color: #2e6da4;">BUY NOW</a></span>
        </div>
    </div>

</c:if>

</body>
<script>
    function confirmAndSendEmail(userId,userEmail) {
        var r=confirm("是否确认发送邮件?");
        if (r){
            var emailContent = $("#email_content").html();
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/shopCarMarketingCtr/confirmAndSendEmail',
                data: {
                    "userEmail": userEmail,
                    "userId":userId,
                    "emailContent": emailContent
                },
                success: function (data) {
                    var json = eval("(" + data + ")");
                    alert(json.message);
                },
                error: function () {
                    alert("执行失败,请联系管理员");
                }
            });
        }else{
            return false;
        }
    }

</script>
</html>
