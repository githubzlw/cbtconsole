<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>购物车营销邮件</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript">
        $(function () {
            var website = '${website}';
            if(!website){
               website = 0;
            }
            loadWebSize(website);
        });

        function loadWebSize(website) {
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/shopCarMarketingCtr/queryAllWebSizeList',
                data: {},
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.ok) {

                        var data = json.data;
                        var content = '';
                        for(var key in data){
                            content += '<option value="'+key+'">'+data[key]+'</option>'
                        }
                        $("#website_type").empty();
                        $("#website_type").append(content);
                        if(website > 0){
                            $("#website_type").val(website);
                        }
                    } else {
                        $.messager.alert("提醒", json.message, "info");
                    }
                },
                error: function () {
                    $.messager.alert("提醒", "执行失败,请联系管理员", "info");
                }
            });
        }
    </script>
</head>
<body>
<c:if test="${success == 0}">
    <h1 align="center">${message}</h1>
</c:if>
<c:if test="${success > 0}">
    <div>
        <h2 style="text-align: center">产品价格无修改邮件预览</h2>
        <img style="cursor: pointer"
                 src="https://img1.import-express.com/importcsvimg/webpic/newindex/img/logo.png"/>
        <span>网站名:
            <select disabled="disabled" id="website_type" style="height: 28px;width: 160px;">
                <option value="1" selected="selected">import-express</option>
                <option value="2">kidscharming</option>
            </select>
        </span>
        <input type="button" style="border-color: orangered;background-color: aquamarine;"
               value="确认并发送邮件给客户" onclick="confirmAndSendEmail(${userId},'${userEmail}',${type})"/>
        </span>
        <span id="show_notice" style="display: none;color: red;">*正在执行，请等待...</span>
    </div>
    <div style="height: auto;font-size:20px;" id="email_content">
        <div style="font-family: Times New Roman;">

            <p style="margin-bottom: 10px;">
            <h3 id="email_title" style="font-family: Times New Roman;margin-bottom:20px;">You Have Made Some Wonderful Selections!</h3>
            <h3 style="font-family: Times New Roman;margin-bottom:20px;">Dear customer,</h3>
            <span>I'm <input id="admin_name_first" value="${adminName}">, Marketing Manager of Import Express.</span>
            <br>
            <span>I noticed that you were about to buy some products from our website but haven’t completed your order yet.</span>
            <br>
            <span>Is there anything I could help you with?</span>
            <br>
            <span>Here's your shopping cart list.</span>
            </p>

            <c:if test="${fn:length(updateList) > 0}">
                <table style="width: 820px;font-size: 16px; border-color: #b6ff00;" id="email_update_table" border="1"
                       cellpadding="1" cellspacing="0">
                    <caption style="float: left;font-weight: bold;font-size: 18px;padding:6px;">Price reduced items
                    </caption>
                    <tbody>
                    <tr>
                        <td colspan="6" style="border-top: 1px solid;"></td>
                    </tr>
                    <tr style="font-weight: bold;">
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

                <table style="width: 820px;font-size: 18px;text-align: right">
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
            </c:if>
            <c:if test="${fn:length(sourceList) > 0}">
                <br>
                <table style="width: 820px;font-size: 16px; border-color: #b6ff00;" id="email_old_table" border="1"
                       cellpadding="1" cellspacing="0">
                    <caption style="float: left;font-weight: bold;font-size: 18px;padding:6px;">Items in your shopping Cart
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
                                <div><img style="cursor: pointer" width="50px;" height="50px;"
                                          src="${carInfo.googsImg}"/>
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
                <div style="margin-top:10px;">
                    <a style="color: #0070C0;" href="https://www.import-express.com/Goods/getShopCar" target="_blank">View More...</a>
                </div>
            </c:if>
            <table style="width: 820px;font-size: 18px;text-align: right">
                <tbody>
                <tr>
                    <td>Shop Cart Total:</td>
                    <td style="width: 150px">USD&nbsp;&nbsp;${totalProductCost}</td>
                </tr>
                <%--<tr>
                    <td>Shop Cart Final Due:</td>
                    <td style="width: 150px">USD&nbsp;&nbsp;${totalActualCost}</td>
                </tr>--%>
                </tbody>
            </table>

            <%--<div style="width:500px;position:relative;top:-30px;">
                <span style="font-weight: bold;font-size: 20px;margin-right:8px;">You have saved :USD&nbsp;${offCost}&nbsp;&nbsp;(${offRate}% Off)</span>
                <span><a href="https://www.import-express.com" target="_blank"
                         style="font-size: 16px;border-radius: 4px;color: #fff;background-color: #3e9eea;padding:4px 8px;text-decoration: none;">BUY NOW</a></span>
            </div>--%>
        </div>
        <div style="font-family: Times New Roman;">
            <div>
                <br>
                <p style="margin-bottom: 5px;margin-top:0;">
                    <span>If there were any reasons why you didn't complete your purchase, please don't hesitate to
                    let me know by responding to this Email,</span>
                    <br>
                    <span>our team will absolutely try every effort to meet your satisfaction!</span>
                    </p>
                <p style="margin-bottom: 5px;margin-top:15px;"><b>ImportExpress CHINA</b></p>
                <p style="margin-bottom: 5px;margin-top:0;">Best product source for small business!</p>
                <p style="margin-bottom: 5px;margin-top:0;"><a href="https://www.import-express.com/" target="_blank"
                                                               style="color:#01a4ef;font-size:28px;"><b>www.import-express.com</b></a>
                </p>

                <p style="margin-bottom: 5px;margin-top:0;">Yours Sincerely,</p>
                <p style="margin-bottom: 5px;margin-top:0;"><input id="admin_name" value="${adminName}"/> | Marketing Manager</p>
                <p style="margin-bottom: 5px;margin-top:0;"><b>Marketing Department</b></p>
                <p style="margin-bottom: 5px;margin-top:0;">Email:<input id="admin_email" value="${adminEmail}"/></p>
                <p id="whats_app_pp_temp" style="margin-bottom: 5px;margin-top:0;"><span>WhatsApp: </span><input
                        id="whats_app" value="+86 136 3644 5063"/></p>
            </div>
            <%--<div>
                <p style="margin-bottom: 5px;margin-top:15px;"><b>ImportExpress CHINA</b></p>
                <p style="margin-bottom: 5px;margin-top:0;">Best product source for small business!</p>
                <p style="margin-bottom: 5px;margin-top:0;"><a href="https://www.import-express.com/" target="_blank"
                                                               style="color:#01a4ef;font-size:28px;"><b>www.import-express.com</b></a>
                </p>
                <p>
                <p style="font-weight: 700;">FOLLOW US:</p>
                <a href="https://www.facebook.com/importexpressofficial/"
                   style="background:url(https://img1.import-express.com/importcsvimg/webpic/newindex/img/index_spritesheet.png) -781px -5px no-repeat;display: inline-block;width:42px;height:42px;"></a>
                <a href="https://www.pinterest.com/importexpressofficial"
                   style="background:url(https://img1.import-express.com/importcsvimg/webpic/newindex/img/express.png) -109px -102px no-repeat;display: inline-block;width:42px;height:42px;"></a>
                <a href="https://www.youtube.com/channel/UCQ1BcpyhuJdpCXzJuOswOKw"
                   style="background:url(https://img1.import-express.com/importcsvimg/webpic/newindex/img/express.png) -161px -109px no-repeat;display: inline-block;width:42px;height:42px;"></a>
                <a href="https://twitter.com/importexpresss"
                   style="background:url(https://img1.import-express.com/importcsvimg/webpic/newindex/img/express.png) -177px -57px no-repeat;display: inline-block;width:42px;height:42px;"></a>
                <a href="https://www.instagram.com/importexpressofficial/"
                   style="background:url(https://img1.import-express.com/importcsvimg/webpic/newindex/img/express.png) -5px -57px no-repeat;display: inline-block;width:42px;height:42px;"></a>
                <a href="http://clothing-wholesaler.com/"
                   style="background:url(https://img1.import-express.com/importcsvimg/webpic/newindex/img/express.png) -57px -5px no-repeat;display: inline-block;width:42px;height:42px;"></a>
                </p>
            </div>--%>
        </div>
    </div>
</c:if>
</body>
<script>
    function confirmAndSendEmail(userId, userEmail,type) {
        var r = confirm("是否确认发送邮件?");
        if (r) {
            var websiteType = $("#website_type").val();
            var ischeck = 0;
            var adminNameFirst = $("#admin_name_first").val();
            if (adminNameFirst == null || adminNameFirst == "") {
                $("#show_notice").text("请输入销售名称").show();
                ischeck = 1;
            }
            var adminName = $("#admin_name").val();
            if (adminName == null || adminName == "") {
                $("#show_notice").text("请输入销售名称").show();
                ischeck = 1;
            }
            var adminEmail = $("#admin_email").val();
            if (adminEmail == null || adminEmail == "") {
                $("#show_notice").text("请输入销售邮箱").show();
                ischeck = 1;
            }
            var whatsApp = $("#whats_app").val();
            if (whatsApp == null || whatsApp == "") {
                $("#show_notice").text("请输入WhatsApp").show();
                ischeck = 1;
            }
            if (ischeck == 1) {
                return false;
            } else {
                //var emailContent = $("#email_content").html();
                //var model = $("#modeStr").html();
                $("#show_notice").text("正在执行...").show();
                $.ajax({
                    type: 'POST',
                    dataType: 'text',
                    url: '/cbtconsole/shopCarMarketingCtr/confirmAndSendEmail',
                    data: {
                        "userEmail": userEmail,
                        "userId": userId,
                        "adminNameFirst": adminNameFirst,
                        "adminName": adminName,
                        "adminEmail": adminEmail,
                        "whatsApp": whatsApp,
                        "type":type,
                        "emailTitle":"You Have Made Some Wonderful Selections!",
                        "websiteType": websiteType
                        //"emailContent": emailContent,
                        //"model": model,
                    },
                    success: function (data) {
                        var json = eval("(" + data + ")");
                        $("#show_notice").text(json.message).show();
                    },
                    error: function () {
                        $("#show_notice").text("执行失败,请联系管理员").show();
                    }
                });
            }
        } else {
            return false;
        }
    }
</script>
</html>
