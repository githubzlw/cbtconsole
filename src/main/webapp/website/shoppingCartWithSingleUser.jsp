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
            width: 130px;
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
            closeDialog('simple_coupon_div', 'simple_coupon_enter');
            closeDialog('follow_info_id', null);

            imgLazyLoad();
            var website = '${param.website}';
            if (!website) {
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
                        for (var key in data) {
                            content += '<option value="' + key + '">' + data[key] + '</option>'
                        }
                        $("#select_web_site").empty();
                        $("#select_web_site").append(content);
                        if (website > 0) {
                            $("#select_web_site").val(website);
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

        function imgLazyLoad() {
            $('img.img_sty').lazyload({effect: "fadeIn", threshold: 88});
        }

        function updateGoodsPrice(pid, goodsId, userId, goodsPrice, obj) {
            setChooseTr(obj);
            if (pid == null || pid == "") {
                $.messager.alert("提醒", "获取pid失败", "info");
                return false;
            }
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
                    "pid": pid,
                    "goodsId": goodsId,
                    "userId": userId,
                    "goodsPrice": goodsPrice,
                    "newPrice": newPrice
                },
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.ok) {
                        // window.location.reload();
                        $(obj).parent().parent().find("td").eq(7).text(goodsPrice);
                        $(obj).parent().parent().find("td").eq(8).text(newPrice);
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
            if (formId && formId != null) {
                $("#" + formId)[0].reset();
            }
        }

        function enterSimpleEmail() {
            $("#simple_form_enter")[0].reset();
            var content = 'Hello, we noticed that you have over xx products in your shopping cart, '
                + 'but haven’t placed orders.Could you please let us know the reason?  '
                + 'If you have any questions or concerns, please do let us know.';
            $("#simple_email_content").text(content);
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

        function confirmAndSendEmail(userId, userEmail, emailContentId, type) {
            var emailContent = $("#" + emailContentId).val();
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
                        if (json.ok) {
                            showMessage("执行成功");
                            if (type == 1) {
                                closeDialog('simple_email_div', 'simple_form_enter');
                            } else {
                                closeDialog('shop_cart_div', 'hop_cart_form');
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

        function getNewCarInfo(userId, website) {
            var url = "http://192.168.1.31:9090/webSiteCar/getSingleInfo/" + userId;
            var iWidth = 666; //弹出窗口的宽度;
            var iHeight = 333; //弹出窗口的高度;
            var iTop = (window.screen.availHeight - 30 - iHeight) / 2; //获得窗口的垂直位置;
            var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; //获得窗口的水平位置;
            var param = "height=" + iHeight + ",width=" + iWidth + ",top=" + iTop + ",left=" + iLeft + ",toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no";
            window.open(url, 'windows', param);
        }

        function openFollowInfo() {
            $("#follow_info_id").dialog('open');
        }

        function recoverOnlineData(userId, website) {
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/shopCarMarketingCtr/recoverOnlineDataSingle',
                data: {
                    "website": website + 1,
                    "userId": userId
                },
                success: function (data) {
                    var json = eval("(" + data + ")");
                    if (json.ok) {
                        showMessage("执行成功");
                    } else {
                        $.messager.alert("提醒", json.message, "info");
                    }
                },
                error: function () {
                    $.messager.alert("提醒", "执行失败,请联系管理员", "info");
                }
            });
        }


        function openSendEmail(userId, website) {
            var type = $("#send_type").val();
            if (type == 2) {
                // 动态展示折扣券
                getCouponCode(userId, website);
            } else {
                var url = "/cbtconsole/shopCarMarketingCtr/genShoppingCarMarketingEmail?userId="
                    + userId + "&type=" + type + "&website=" + website;
                var iWidth = 1680; //弹出窗口的宽度;
                var iHeight = 880; //弹出窗口的高度;
                var iTop = (window.screen.availHeight - 30 - iHeight) / 2; //获得窗口的垂直位置;
                var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; //获得窗口的水平位置;
                var param = "height=" + iHeight + ",width=" + iWidth + ",top=" + iTop + ",left=" + iLeft + ",toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no";
                window.open(url, 'windows', param);
            }
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

        function changeWebsite(site, userId, obj) {
            if (site == 3) {
                var website = $(obj).val();
                window.location.href = '/cbtconsole/shopCarMarketingCtr/queryShoppingCarByUserId?userId='
                    + userId + '&website=' + website;
            } else {
                return false;
            }
        }

        function getCouponCode(userId, website) {
            $.ajax({
                type: "GET",
                url: "/cbtconsole/coupon/querycouponcode.do",
                dataType: "text",
                success: function (msg) {
                    $("#coupon_code").val(msg);
                    $("#coupon_web_site").val(website);
                    $("#coupon_user_id").val(userId);

                    $("#simple_coupon_div").dialog('open');
                    $('#simple_coupon_div').window('center');
                },
                error: function () {
                    $.messager.alert("提醒", "执行失败,请联系管理员", "info");
                }
            });
        }

        function addCoupon(obj) {
            var couponWebsiteType = $("#coupon_web_site").val();// 网站
            var couponCode = $("#coupon_code").val();// 卷码
            var typeCodeMo = $("#coupon_type").val();//卷类别
            var valueLeft = $("#coupon_min_amount").val();//最低消费金额
            var valueRight = $("#coupon_deduction").val();// 该卷抵扣金额
            var describe = $("#coupon_desc").val();// 描述
            var count = 1;
            var fromTime = $("#coupon_begin_time").val();
            var toTime = $("#coupon_end_time").val();
            var websiteType = couponWebsiteType;
            var userids = $("#coupon_user_id").val();
            var isSu = true;
            if (couponWebsiteType < 1) {
                $.messager.alert("提醒", "获取网站类别失败", "info");
                isSu = false;
                return isSu;
            }
            if (couponCode == null || couponCode == "") {
                $.messager.alert("提醒", "获取卷码失败", "info");
                isSu = false;
                return isSu;
            }
            if (typeCodeMo < 1) {
                $.messager.alert("提醒", "获取卷类别失败", "info");
                isSu = false;
                return isSu;
            }
            if (valueLeft < 0) {
                $.messager.alert("提醒", "获取最低消费金额失败", "info");
                isSu = false;
                return isSu;
            }
            if (valueRight < 0) {
                $.messager.alert("提醒", "获取该卷抵扣金额失败", "info");
                isSu = false;
                return isSu;
            }
            if (describe == null || describe == "") {
                $.messager.alert("提醒", "获取该卷描述失败", "info");
                isSu = false;
                return isSu;
            }
            if (fromTime == null || fromTime == "") {
                $.messager.alert("提醒", "获取开始时间失败", "info");
                isSu = false;
                return isSu;
            }
            if (toTime == null || toTime == "") {
                $.messager.alert("提醒", "获取到期时间失败", "info");
                isSu = false;
                return isSu;
            }
            if (userids < 0) {
                $.messager.alert("提醒", "获取客户ID失败", "info");
                isSu = false;
                return isSu;
            }
            if (valueLeft * 0.07 < valueRight) {
                $.messager.alert("提醒", "折扣金额不能超过最低消费金额的7%", "info");
                isSu = false;
                return isSu;
            }
            if (valueLeft < 100) {
                $.messager.alert("提醒", "最低消费金额不能小于100", "info");
                isSu = false;
                return isSu;
            }
            /*if(50 < valueRight){
                $.messager.alert("提醒", "折扣金额不能超过50", "info");
                isSu = false;
                return isSu;
            }*/
            if (isSu) {
                $(obj).prop("disabled", true);
                $.ajax({
                    type: "POST",
                    url: "/cbtconsole/coupon/addcoupon.do",
                    data: {
                        couponWebsiteType: couponWebsiteType,
                        couponCode: couponCode,
                        type: typeCodeMo,
                        valueLeft: valueLeft,
                        valueRight: valueRight,
                        describe: describe,
                        count: count,
                        fromTime: fromTime,
                        toTime: toTime,
                        websiteType: websiteType,
                        userids: userids,
                        isShopCar: 1
                    },
                    dataType: "json",
                    success: function (msg) {
                        $(obj).prop("disabled", false);
                        if (msg.state == 'true') {
                            var url = "/cbtconsole/shopCarMarketingCtr/genShoppingCarMarketingEmail?userId="
                                + userids + "&type=" + 2 + "&website=" + websiteType + "&couponCode=" + couponCode;
                            var iWidth = 1680; //弹出窗口的宽度;
                            var iHeight = 880; //弹出窗口的高度;
                            var iTop = (window.screen.availHeight - 30 - iHeight) / 2; //获得窗口的垂直位置;
                            var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; //获得窗口的水平位置;
                            var param = "height=" + iHeight + ",width=" + iWidth + ",top=" + iTop + ",left=" + iLeft + ",toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no";
                            window.open(url, 'windows', param);
                            closeDialog('simple_coupon_div', 'simple_coupon_enter');
                        } else {
                            $.messager.alert("提醒", msg.message, "info");
                        }
                    },
                    error: function () {
                        $(obj).prop("disabled", false);
                        $.messager.alert("提醒", "执行失败,请联系管理员", "info");
                    }
                });
            }
        }

        function changeDescribe(price) {
            if (price && price > 0) {
                $("#coupon_desc").val('If order over  $' + price);
            }
        }
    </script>
</head>
<body>

<c:if test="${success == 0}">
    <h1 align="center">${message}</h1>
</c:if>
<c:if test="${success > 0}">

    <div id="simple_coupon_div" class="easyui-dialog" title="创建购物车营销优惠券"
         data-options="modal:true" style="width: 500px; height: 250px;">
        <form id="simple_coupon_enter" style="text-align: center" action="#" onsubmit="return false">

            <table cellspacing="1">

                <tr>
                    <td style="display: none;">
                        <input id="coupon_web_site" type="hidden" value="0"/>
                        <input id="coupon_user_id" type="hidden" value="0"/>
                    </td>
                    <td>卷码(自动生成):</td>
                    <td><input id="coupon_code" readonly="readonly"/></td>
                </tr>
                <tr>
                    <td>卷类别:</td>
                    <td>
                        <select id="coupon_type">
                            <option value="1">1-满减券</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>最低消费金额</td>
                    <td><input id="coupon_min_amount" type="number" step="0.01" value="100"
                               onchange="changeDescribe(this.value)"/>(<b style="color: red">*根据总产品金额给</b>)
                    </td>
                </tr>
                <tr>
                    <td>抵扣金额</td>
                    <td><input id="coupon_deduction" type="number" step="0.01"/>(<b style="color: red">*按照优惠券逻辑设置</b>)
                    </td>
                </tr>
                <tr>
                    <td>描述(自动生成):</td>
                    <td><input id="coupon_desc" readonly="readonly" value="If order over  $100"/></td>
                </tr>
                <tr>
                    <td>截止时间:</td>
                    <td><input id="coupon_begin_time" class="Wdate"
                               style="width: 168px; height: 24px" type="text" value=""
                               onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
                        <input id="coupon_end_time" class="Wdate"
                               style="width: 168px; height: 24px" type="text" value=""
                               onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/></td>
                </tr>

                <tr>
                    <td colspan="2" style="text-align: center;">
                        <button onclick="addCoupon(this)">创建</button>
                        &nbsp;&nbsp;<button onclick="closeDialog('simple_coupon_div', 'simple_coupon_enter')">关闭
                    </button>
                    </td>
                </tr>
            </table>

        </form>
    </div>


    <div id="simple_email_div" class="easyui-dialog" title="发送邮件(简单跟进)"
         data-options="modal:true" style="width: 750px; height: 463px;">
        <form id="simple_form_enter" action="#" onsubmit="return false">
        <textarea id="simple_email_content" style="height: 91%;width: 99%;font-size: 22px;">
        </textarea>
            <div style="text-align: center;">
                <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
                   class="easyui-linkbutton"
                   onclick=" confirmAndSendEmail(${userId},'${userInfo.userEmail}','simple_email_content',1)"
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

    <div id="follow_info_id" class="easyui-dialog" title="跟进列表" data-options="modal:true"
         style="width: 400px; height: 300px;">
        <table border="1" cellpadding="1" cellspacing="0" align="center">
            <thead>
            <tr>
                <td style="width: 80px;">跟进人</td>
                <td style="width: 150px;">跟进时间</td>
            </tr>
            </thead>
            <c:if test="${not empty followList}">
                <c:forEach items="${followList}" var="fl">
                    <tr>
                        <td>${fl.adminName}</td>
                        <td>${fl.createTime}</td>
                    </tr>
                </c:forEach>
            </c:if>
            <tr>
                <td colspan="2" style="text-align: center;">
                    <button onclick="closeDialog('follow_info_id', null)">关闭</button>
                </td>
            </tr>
        </table>
    </div>

    <div style="height: 15%;width: 100%;text-align: center">

        <table style="border-color: #e65510;" border="1" cellpadding="1" cellspacing="0" align="center">
            <caption><b style="font-size: 24px;color: #e65510;">客户【${userId}】购物车信息</b></caption>
            <tr>
                <td>总产品金额:${userInfo.totalPrice}<em>$</em></td>
                <td>购物车运费:${userInfo.totalFreight}<em>$</em></td>
                <td>总采购价:${userInfo.totalWhosePrice}<em>$</em></td>
                    <%--利润率 = （客户需要掏的钱-真实运费-采购额）/客户掏的钱--%>
                <td>
                    <c:choose>
                        <c:when test="${isGetFreigthResult==true}">
                            预估利润率:${userInfo.estimateProfit}
                            <em>%</em>【(商品总价+购物车运费-线下采购运输运费-商品采购价)/商品采购价(${userInfo.totalPrice}+${userInfo.totalFreight}-${userInfo.offFreight}-${userInfo.totalWhosePrice})/(${userInfo.totalPrice}+${userInfo.totalFreight}))】
                        </c:when>
                        <c:otherwise>
                            <font color="red">没有获取到正确运费无法预估</font>
                        </c:otherwise>
                    </c:choose>
                </td>
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
                            <%--<input class="btn_sty" type="button" value="基本跟进" onclick="enterSimpleEmail()"/>
                            &nbsp;&nbsp;
                            <input class="btn_sty" type="button" value="购物车价格比较" onclick="enterShopCarEmail(${userId})"/>--%>
                        <span>网站:<select id="select_web_site" style="height: 28px;width: 180px;" disabled="disabled">
                            <option value="0">import-express</option>
                            <option value="1">kidscharming</option>
                        </select></span>
                        <span>邮件类型:
                            <select id="send_type" style="height: 28px;width: 180px;">
                            <option value="1" selected="selected">不做变动,直接发送</option>
                            <%--<option value="2">给单个产品价格改价</option>--%>
                            <option value="2">给折扣券</option>
                            <%--<option value="3">操作运费</option>--%>
                            <option value="4">为客户选择最佳运输方式</option>
                            </select>
                        </span>
                        &nbsp;&nbsp;
                        <input class="btn_sty" type="button" value="发送邮件"
                               onclick="openSendEmail(${userId}, ${param.website})"/>
                        &nbsp;&nbsp;
                        <input class="btn_sty" type="button" value="竞争对手对比"
                               onclick="openComparedEmail(${userId}, ${param.website})"/>
                        &nbsp;&nbsp;
                        <input class="btn_sty" type="button" value="查看客户信息"
                               onclick="openUserInfo(${userId}, ${param.website})"/>

                        &nbsp;&nbsp;<input class="btn_sty" type="button" value="查看跟进记录" onclick="openFollowInfo()"/>
                        &nbsp;&nbsp;
                            <%--<input class="btn_sty" type="button" value="查看EDM跟踪" onclick="openUserFollow(${userId}, ${param.website})"/>--%>
                        &nbsp;&nbsp;
                            <%--<input class="btn_sty" style="display: none;" type="button" value="恢复线上数据" onclick="recoverOnlineData(${userId}, ${param.website})"/>--%>
                        &nbsp;&nbsp;<input class="btn_sty" type="button" value="重新获取购物车"
                                           onclick="getNewCarInfo(${userId}, ${param.website})"/>
                    </div>
                </td>
            </tr>
        </table>

    </div>

    <div style="height: 85%;width: 100%;overflow-y: auto;">

        <table style="border-color: #0cc960;font-size: 18px;" border="1" cellpadding="1" cellspacing="0" align="center">
            <caption style="font-size: 20px;">
                <b style="color: #0cc960;">商品信息</b>(<span style="color: red;">点击PID进入商品编辑界面;点击产品标题进入电商网站产品单页;点击图片进入对应的货源网站</span>)
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
                    <%-- <td style="width: 80px;">改价</td>--%>
                    <%--<td style="width: 140px;">操作</td>--%>
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
                        <%--<td style="text-align: center">
                            <c:if test="${good.cartOldPrice > 0}">
                                ${good.cartGoodsPrice}
                            </c:if>
                        </td>
                        <td>
                            <input type="number" value="" style="width: 120px;"/>
                            <c:if test="${good.cartOldPrice > 0}">
                                <br><input class="btn_sty" type="button" value="修改价格"
                                           onclick="updateGoodsPrice('${good.pid}',${good.id},${userId},${good.cartOldPrice},this)"/>
                            </c:if>
                            <c:if test="${good.cartOldPrice == 0}">
                                <br><input class="btn_sty" type="button" value="修改价格"
                                           onclick="updateGoodsPrice('${good.pid}',${good.id},${userId},${good.cartGoodsPrice},this)"/>
                            </c:if>
                        </td>--%>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </div>
</c:if>

</body>
</html>
