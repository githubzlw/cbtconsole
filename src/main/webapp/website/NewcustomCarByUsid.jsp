<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>客户访问记录</title>
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
        .small {
            text-align: center
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
        .tag{
            position: relative;
            top: 0px;
            left: 280px;
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
            $("#" + formId)[0].reset();
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

        function confirmAndSendEmail(userId, userEmail, emailContentId,type) {
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

        function openUserFollow() {

        }


        function  openSendEmail(userId) {
            var type = $("#send_type").val();
            var url = "/cbtconsole/shopCarMarketingCtr/genShoppingCarMarketingEmail?userId=" + userId + "&type=" + type;
            var iWidth = 1680; //弹出窗口的宽度;
            var iHeight = 880; //弹出窗口的高度;
            var iTop = (window.screen.availHeight - 30 - iHeight) / 2; //获得窗口的垂直位置;
            var iLeft = (window.screen.availWidth - 10 - iWidth) / 2; //获得窗口的水平位置;
            var param = "height=" + iHeight + ",width=" + iWidth + ",top=" + iTop + ",left=" + iLeft + ",toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no";
            window.open(url, 'windows', param);
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
    <div style="height: 85%;width: 100%;overflow-y: auto;">
         <h1 align="center">${Message==null?"客户商品浏览记录":Message}</h1>
      <a href="http://192.168.1.27:10004/helpServlet?action=writeNewEmail&className=EmailListServlet&userEmail=${userEmail}&usname=${usname}&uspassword=${uspassword}" target="_blank" class="tag">发送邮件给客户</a>
        <table style="border-color: #0cc960;font-size: 18px;" border="1" cellpadding="1" cellspacing="0" align="center">
            <thead>
            <tr align="center">
                <td style="width: 150px;">用户id</td>
                <td style="width: 150px;">商品pid</td>
                <td style="width: 150px;">浏览次数</td>
                <td style="width: 200px;">浏览时间</td>
                <td style="width: 100px;">商品链接</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${goodsList}" var="good">
                <tr>
                    <td style="text-align: center;">${good.userId}</td>
                    <td style="text-align: center;">${good.shippingName}</td>
                    <td style="text-align: center;">${good.totalCatid}</td>
                    <td style="text-align: center;">${good.createTime}</td>
                    <td style="text-align: left;width: 690px;">
                        <div style="width: 100%">
                            <a class="goods_name" href="https://www.import-express.com/goodsinfo/cbtconsole-1${good.shippingName}.html" target="_blank">${good.followAdminName}</a>
                            <a href="https://detail.1688.com/offer/${good.shippingName}.html" target="_blank">
                                <img src="${good.lastLoginTime}" class="img_sty"/>
                            </a>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span style="color: red">${good.currency}</span>
                        </div>
                    </td>

                </tr>
            </c:forEach>

            </tbody>
        </table>
        <div class="small">
            <a href="/cbtconsole/NewCustomersFollow/queryNewCustomByUserId?userEmail=${userEmail}&userId=${userId}&page=${page==totalpage?totalpage:page+1}">下一页</a>
            <span>总条数：${total}</span><span>总页数：${totalpage}</span><span>当前页：${page}</span>
            <a href="/cbtconsole/NewCustomersFollow/queryNewCustomByUserId?userEmail=${userEmail}&userId=${userId}&page=${page==1?1:page-1}">上一页</a>
        </div>
    </div>
</body>
</html>
