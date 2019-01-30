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
</head>
<body>
<c:if test="${success == 0}">
    <h1 align="center">${message}</h1>
</c:if>
<c:if test="${success > 0}">
    <div>
        <h2 style="text-align: center">最佳运输方式邮件预览</h2>
        <img style="cursor: pointer"
                 src="https://img1.import-express.com/importcsvimg/webpic/newindex/img/logo.png"/>
        <input type="button" style="border-color: orangered;background-color: aquamarine;"
               value="确认并发送邮件给客户" onclick="confirmAndSendEmail(${userId},'${userEmail}')"/>
        <span id="show_notice" style="display: none;color: red;">*正在执行，请等待...</span>

    </div>
    <div style="height: auto;font-size:20px;" id="email_content">
        <div style="font-family: Times New Roman;">
            <p style="margin-bottom: 10px;">
            <h3 id="email_title" style="font-family: Times New Roman;margin-bottom:20px;">Faster And Cheaper Shipping Method For Your Consideration!</h3>
            <h3 style="font-family: Times New Roman;margin-bottom:20px;">Dear customer,</h3>
            <span>I'm <input id="admin_name_first" value="${adminName}">, Marketing Manager of Import Express.</span>
            <br>
            <span>Recently you have added some items to your shopping  cart, </span>
            <br>
            <span>and ticked  A  as your shipping method, however there is a faster and cheaper shipping method you have probably missed:</span>
            </p>

            <table style="width: 820px;font-size: 16px; border-color: #b6ff00;" id="email_update_table" border="1"
                   cellpadding="1" cellspacing="0">
                <thead>
                <tr style="font-weight: bold;">
                    <td align="center" width="200px">Shipping Method</td>
                    <td align="center" width="200px">Shipping Time</td>
                    <td align="center" width="200px">Shipping Cost</td>
                </tr>
                </thead>
                <tbody>
                <tr align="center" >
                    <td>A</td>
                    <td><input value="XXX days"/></td>
                    <td>$<input value="XXX"/></td>
                </tr>

                <tr align="center" >
                    <td>B</td>
                    <td><input value="XXX days"/>
                    <br><span style="color: red;">Faster √</span>
                    </td>
                    <td>$<input value="XXX"/>
                    <br><span style="color: red;">Cheaper √</span>
                    </td>
                </tr>
                </tbody>
            </table>


            <br>
            <div style="width:600px;">
                <span>We have updated your shopping cart with this better shipping method</span>
                <span style="font-weight: bold;font-size: 20px;margin-right:8px;">You have saved :USD&nbsp;<input value="2"/></span>
                <span><a href="https://www.import-express.com" target="_blank"
                         style="font-size: 16px;border-radius: 4px;color: #fff;background-color: #3e9eea;padding:4px 8px;text-decoration: none;">BUY NOW</a></span>
            </div>
        </div>
        <br>
        <div style="font-family: Times New Roman;">
            <div>
                <p style="margin-bottom: 5px;margin-top:0;">
                    <span>But If there were actually any other reasons why you didn't complete your purchase,</span>
                    <br>
                    <span>please don't hesitate to let me know by responding to this Email,</span>
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
                <p style="margin-bottom: 5px;margin-top:0;">Email:<input id="admin_email" style="width: 280px;" value="${adminEmail}"/></p>
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
    function confirmAndSendEmail(userId, userEmail) {
        var r = confirm("是否确认发送邮件?");
        if (r) {
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
                        "whatsApp": whatsApp
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
