<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<div>
    <input type="button" style="border-color: orangered;background-color: aquamarine;"
           value="确认并发送邮件给客户" onclick="confirmAndSendEmail(${userId},'${userEmail}')"/>
    <h2>邮件预览</h2>
</div>
<div style="height: auto" id="email_content">
    <div>
        <img style="cursor: pointer"
             src="https://img1.import-express.com/importcsvimg/webpic/newindex/img/logo.png"/>
        <p style="margin-bottom: 10px;">
            <span style="display: inline-block;width: 800px;margin-bottom: 20px;">Dear customer,
            <br>I'm  跟进人名字,  职位头衔of ImportExpress.</span>
            <br>
            <span>I noticed that you were about to pick up some products on our website but haven’t completed your order </span>
            <br>
            <span>Is there anything I could help you with?</span>
            <br>
            <span>Here's your shopping cart list</span>
        </p>

        <table style="width: 820px;font-size: 12px; border-color: #b6ff00;" id="email_update_table" border="1"
               cellpadding="1" cellspacing="0">
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
        <table style="width: 820px;font-size: 12px; border-color: #b6ff00;" id="email_old_table" border="1"
               cellpadding="1" cellspacing="0">
            <caption style="float: left;font-weight: bold;font-size: 18px;width: 360px;">Others Items in your shopping
                Cart
            </caption>
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
        <div>

            <span style="font-weight: bold;font-size: 12px;">You have saved :USD&nbsp;${offCost}&nbsp;&nbsp;(${offRate}% Off)</span>
            &nbsp;<a style="color: #0070C0" href="https://www.import-express.com/Goods/getShopCar" target="_blank">Details</a><em>.</em>
            <br>
            <span><a href="https://www.import-express.com" target="_blank"
                     style="border-color: #2e6da4;font-size: 16px;border-radius: 5px;color: #26ef19;background-color: #2e6da4;">BUY NOW</a></span>
        </div>

    </div>
</div>
</body>
<script>
    function confirmAndSendEmail(userId, userEmail) {
        var r = confirm("是否确认发送邮件?");
        if (r) {
            var emailContent = $("#email_content").html();
            var model = $("#modeStr").html();
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/shopCarMarketingCtr/confirmAndSendEmail',
                data: {
                    "userEmail": userEmail,
                    "userId": userId,
                    "emailContent": emailContent,
                    "model": model
                },
                success: function (data) {
                    var json = eval("(" + data + ")");
                    alert(json.message);
                },
                error: function () {
                    alert("执行失败,请联系管理员");
                }
            });
        } else {
            return false;
        }
    }
</script>
</html>
