<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>与速卖通商品对比</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript">
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
</head>
<body>

<c:if test="${success == 0}">
    <h1 align="center">${message}</h1>
</c:if>
<c:if test="${success > 0}">

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
                <span style="display: inline-block;width: 800px;margin-bottom: 20px;">We offer best price in the market.  Please compare the following offerings.</span>
                <br><a style="color: #0070C0" href="https://www.import-express.com/Goods/getShopCar" target="_blank">Details<em>.</em></a>
            </p>

            <table style="border-color: #0cc960;font-size: 18px;" border="1" cellpadding="1" cellspacing="0">
                <thead>
                <tr align="center" style="font-size: 14px;">
                    <td style="width: 400px;">Your ShopCar Goods</td>
                    <td style="width: 400px;">AliExpress Goods</td>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${resultList}" var="good">
                    <tr style="font-size: 14px;">
                        <td style="width: 400px;">
                            <div style="width: 350px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">
                                <a style="color:#01a4ef;" href="${good.onlineUrl}" target="_blank">${good.goodsTitle}</a>
                            </div>
                            <div>
                                <img style="cursor: pointer" width="60px;" height="60px;" src="${good.cartGoodsImg}">
                                <span style="border: 1px solid #E1E1E1;">Price:${good.cartGoodsPrice}<em>$</em></span>
                            </div>
                        </td>
                        <td style="width: 400px;">

                            <div style="width: 350px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">
                                <a href="https://www.aliexpress.com/item/ali/${good.aliPid}.html"
                                   target="_blank">${good.aliName}</a>
                            </div>
                            <div>
                                <img style="cursor: pointer" width="60px;" height="60px;" src="${good.aliImg}">
                                <span style="border: 1px solid #E1E1E1;">Price:${good.aliPrice}<em>$</em></span>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>


            <span><a href="https://www.import-express.com" target="_blank"
                     style="border-color: #2e6da4;font-size: 16px;border-radius: 5px;color: #26ef19;background-color: #2e6da4;">BUY NOW</a></span>
        </div>
    </div>

</c:if>

</body>
</html>
