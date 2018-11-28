<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>客户购物车信息</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
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
            max-height: 100px;
            max-width: 100px;
        }

        .goods_name {
            display: block;
            color: #01a4ef;
            text-overflow: ellipsis;
            overflow: hidden;
            padding-left: 5px;
            padding-top: 5px;
            text-decoration: none;
        }

        .btn_sty {
            margin: 5px 0 0 0;
            width: 120px;
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

        .a_sty {
            color: black;
        }

        .sku_sty {
            color: #f55607;
        }
    </style>
    <script type="text/javascript">

        $(document).ready(function () {
            closeDialog('simple_email_div', 'simple_form_enter');
            closeDialog('shop_cart_div', 'hop_cart_form');
            imgLazyLoad();
        });

        function imgLazyLoad() {
            $('img.img_sty').lazyload({effect: "fadeIn", threshold: 88});
        }

        function updateGoodsPrice(goodsId, userId, goodsPrice, obj) {
            setChooseTr(obj);
            var newPrice = $(obj).parent().find("input").val();
            if (goodsId == null || goodsId == "" || goodsId == 0) {
                $.messager.alert("提醒", "获取goodsId失败", "info");
                return false;
            }
            if (userId == null || userId == "" || userId == 0) {
                $.messager.alert("提醒", "获取userId失败", "info");
                return false;
            }
            if (newPrice == null || newPrice == "" || newPrice == 0) {
                $.messager.alert("提醒", "获取修改价格失败", "info");
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
                        $.messager.alert("提醒", json.message, "info");
                    }
                },
                error: function () {
                    $.messager.alert("提醒", "执行失败,请联系管理员", "info");
                }
            });
        }

        function setChooseTr(obj) {
            //背景色变色
            $(obj).parent().parent().addClass("is_tr").siblings().removeClass("is_tr");
        }

        function closeDialog(divId, formId) {
            $('#' + divId).dialog('close');
            $("#" + formId)[0].reset();
        }

        function enterSimpleEmail() {
            $("#simple_form_enter")[0].reset();
            var content = 'Hello, we noticed that you have over xx products in your shopping cart, '
                + 'but haven’t placed orders.Could you please let us know the reason?  '
                + 'If you have any questions or concerns, please do let us know.';
            $("#simle_email_content").text(content);
            $("#simple_email_div").dialog('open');
            $('#simple_email_div').window('center');//使Dialog居中显示
        }

        function enterShopCarEmail(userId) {
            $("#hop_cart_form")[0].reset();
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/shopCarMarketingCtr/genShopCarInfoByUserId',
                data: {
                    "userId": userId
                },
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.success > 0) {
                        var content = 'We have reduced the total price of items in your shopping cart from $'
                            + json.productCost + ' to $' + json.actualCost + ' (including shipping).';
                        $("#shop_cart_content").text(content);
                        $("#shop_cart_div").dialog('open');
                    } else {
                        $.messager.alert("提醒", json.message, "info");
                    }
                },
                error: function () {
                    //"提醒", '请输入1688URL', "info"
                    $.messager.alert("提醒", "执行失败,请联系管理员", "info");
                }
            });
        }

        function confirmAndSendEmail(userId, userEmail, emailContentId,type) {
            var emailContent = $("#" + emailContentId).text();
                if (emailContent == null || emailContent == "") {
                    $.messager.alert("提醒", "获取邮件内容失败", "info");
                    return false;
                } else {
                    $.ajax({
                        type: 'POST',
                        dataType: 'text',
                        url: '/cbtconsole/shopCarMarketingCtr/confirmAndSendEmail',
                        data: {
                            "userEmail": userEmail,
                            "userId": userId,
                            "emailContent": emailContent
                        },
                        success: function (data) {
                            var json = eval("(" + data + ")");
                            if(json.ok){
                                showMessage("执行成功");
                                if (type == 1) {
                                    closeDialog('simple_email_div', 'simple_form_enter');
                                } else {
                                    closeDialog('shop_cart_div', 'hop_cart_form');
                                }
                            }else{
                                $.messager.alert("提醒", json.message, "info");
                            }
                        },
                        error: function () {
                            $.messager.alert("提醒", "执行失败,请联系管理员", "info");
                        }
                    });
                }
        }

        function openComparedEmail(userId) {
            var url = "/cbtconsole/shopCarMarketingCtr/comparedEmailWithAliExpress?userId=" + userId;
            var iWidth = 1680; //弹出窗口的宽度;
            var iHeight = 880; //弹出窗口的高度;
            var iTop = (window.screen.availHeight - 30 - iHeight) / 2; //获得窗口的垂直位置;
            var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; //获得窗口的水平位置;
            var param = "height=" + iHeight + ",width=" + iWidth + ",top=" + iTop + ",left=" + iLeft + ",toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no";
            window.open(url, 'windows', param);
        }

        function openUserInfo(userId) {
            var url = "/cbtconsole/userinfo/getUserInfo.do?userId=" + userId;
            window.open(url);
        }

        function showMessage(message) {
            $.messager.show({
                title: '提醒',
                msg: message,
                timeout: 1500,
                showType: 'slide',
                style: {
                    right: '',
                    top: ($(window).height() * 0.25),
                    bottom: ''
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

    <div id="simple_email_div" class="easyui-dialog" title="发送邮件(简单跟进)"
         data-options="modal:true" style="width: 750px; height: 463px;">
        <form id="simple_form_enter" action="#" onsubmit="return false">
        <textarea id="simle_email_content" style="height: 91%;width: 99%;font-size: 22px;">
        </textarea>
            <div style="text-align: center;">
                <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
                   class="easyui-linkbutton"
                   onclick=" confirmAndSendEmail(${userId},'${userInfo.userEmail}','simle_email_content',1)"
                   style="width: 80px">发送</a>
                <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
                   class="easyui-linkbutton" onclick="closeDialog('simple_email_div','simple_form_enter')"
                   style="width: 80px">取消</a>
            </div>

        </form>
    </div>


    <div id="shop_cart_div" class="easyui-dialog" title="发送邮件(价格变化)"
         data-options="modal:true" style="width: 750px; height: 463px;">
        <form id="hop_cart_form" action="#" onsubmit="return false">
        <textarea id="shop_cart_content" style="height: 91%;width: 99%;font-size: 22px;">Hello, we noticed that you have over xx products in your shopping cart, but haven’t placed orders.Could you please let us know the reason?  If you have any questions or concerns, please do let us know.
        </textarea>
            <div style="text-align: center;">
                <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
                   class="easyui-linkbutton"
                   onclick=" confirmAndSendEmail(${userId},'${userInfo.userEmail}','shop_cart_content',2)"
                   style="width: 80px">发送</a>
                <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
                   class="easyui-linkbutton" onclick="closeDialog('shop_cart_div','hop_cart_form')"
                   style="width: 80px">取消</a>
            </div>

        </form>
    </div>

    <div style="height: 15%;width: 100%;text-align: center">

        <table style="border-color: #e65510;" border="1" cellpadding="1" cellspacing="0" align="center">
            <caption><b style="font-size: 24px;color: #e65510;">客户【${userId}】购物车信息</b></caption>
            <tr>
                <td>总产品金额:${userInfo.totalPrice}<em>$</em></td>
                <td>总运费:${userInfo.totalFreight}<em>$</em></td>
                <td>总采购价:${userInfo.totalWhosePrice}<em>$</em></td>
                <td>预计利润率:${userInfo.estimateProfit}<em>%</em></td>
            </tr>
            <tr>
                <td>客户国家:${userInfo.countryName}</td>
                <td>客户VIP等级:${userInfo.gradeId}</td>
                <td>注册时间:${userInfo.createTime}</td>
                <td>当前运输方式:${userInfo.shippingName}</td>
                    <%--<td>最近一次登录时间:${userInfo.lastLoginTime}</td>--%>
            </tr>
            <tr>
                <td colspan="4">
                    <div style="text-align: center">
                        <input class="btn_sty" type="button" value="基本跟进" onclick="enterSimpleEmail()"/>
                        &nbsp;&nbsp;
                        <input class="btn_sty" type="button" value="购物车价格比较" onclick="enterShopCarEmail(${userId})"/>
                        &nbsp;&nbsp;
                        <input class="btn_sty" type="button" value="竞争对手对比" onclick="openComparedEmail(${userId})"/>
                        &nbsp;&nbsp;
                        <input class="btn_sty" type="button" value="查看客户信息" onclick="openUserInfo(${userId})"/>
                    </div>
                </td>
            </tr>
        </table>

    </div>

    <div style="height: 85%;width: 100%;overflow-y: auto;">

        <table style="border-color: #0cc960;font-size: 18px;" border="1" cellpadding="1" cellspacing="0" align="center">
            <caption style="font-size: 20px;">
                <b style="color: #0cc960;">商品信息</b>(<b style="color: red;">点击图片跳转对应的货源网站</b>)
            </caption>
            <thead>
            <tr align="center">
                <td style="width: 150px;">PID</td>
                <td style="width: 690px;">购物车信息</td>
                <td style="width: 100px;">加价率</td>
                <td style="width: 100px;">购物车重量</td>
                <td style="width: 115px;">1688货源重量</td>
                <td style="width: 180px;">对标商品信息</td>
                <td style="width: 80px;">数量</td>
                <td style="width: 80px;">原价</td>
                <td style="width: 80px;">改价</td>
                <td style="width: 140px;">操作</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${goodsList}" var="good">
                <tr>
                    <td style="text-align: left;"><a class="a_sty" href="/cbtconsole/editc/detalisEdit?pid=${good.pid}"
                                                     target="_blank">${good.pid}</a></td>
                    <td style="text-align: left;width: 690px;">
                        <div style="width: 100%">
                            <a class="goods_name" href="${good.onlineUrl}" target="_blank">${good.goodsTitle}</a>
                            <a href="https://detail.1688.com/offer/${good.pid}.html" target="_blank">
                                    <%--<img src="/cbtconsole/img/beforeLoad.gif" data-original="${good.cartGoodsImg}" class="img_sty"/>--%>
                                <img src="${good.cartGoodsImg}" class="img_sty"/>
                            </a>
                            <span class="sku_sty">规格:[${good.goodsType}]</span>
                            &nbsp;&nbsp;<span>MOQ:${good.moq}</span>
                        </div>
                    </td>
                    <td style="text-align: center;">${good.priceRate}</td>
                    <c:if test="${good.cartWeight > good.weight1688}">
                        <td style="text-align: center;background-color: #85ea85;">${good.cartWeight}</td>
                    </c:if>
                    <c:if test="${good.cartWeight <= good.weight1688}">
                        <td style="text-align: center;">${good.cartWeight}</td>
                    </c:if>

                    <td style="text-align: center;">${good.weight1688}</td>
                    <td style="text-align: left;width: 180px;">
                        <c:if test="${good.aliPid != null && good.aliPid != ''}">
                            <div style="width: 100%;">
                                <span>Ali价格:${good.aliPrice}<em>$</em></span>
                                <br>
                                <a href="https://www.aliexpress.com/item/a/${good.aliPid}.html" target="_blank">
                                        <%--<img src="/cbtconsole/img/beforeLoad.gif" data-original="${good.aliImg}" class="img_sty"/>--%>
                                    <img src="${good.aliImg}" class="img_sty"/>
                                </a>
                            </div>
                        </c:if>

                    </td>
                    <td style="text-align: center">${good.cartGoodsNum}</td>
                    <td style="text-align: center">
                        <c:if test="${good.cartOldPrice > 0}">
                            ${good.cartOldPrice}
                        </c:if>
                        <c:if test="${good.cartOldPrice == 0}">
                            ${good.cartGoodsPrice}
                        </c:if>
                    </td>
                    <td style="text-align: center">
                        <c:if test="${good.cartOldPrice > 0}">
                            ${good.cartGoodsPrice}
                        </c:if>
                    </td>
                    <td>
                        <input type="number" value="" style="width: 120px;"/>
                        <c:if test="${good.cartOldPrice > 0}">
                            <br><input class="btn_sty" type="button" value="修改价格"
                                       onclick="updateGoodsPrice(${good.id},${userId},${good.cartOldPrice},this)"/>
                        </c:if>
                        <c:if test="${good.cartOldPrice == 0}">
                            <br><input class="btn_sty" type="button" value="修改价格"
                                       onclick="updateGoodsPrice(${good.id},${userId},${good.cartGoodsPrice},this)"/>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>
</c:if>

</body>
</html>
