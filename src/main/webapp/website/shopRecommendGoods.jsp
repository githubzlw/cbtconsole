<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>店铺推荐商品</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
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
    <script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
    <style type="text/css">
        .img_sty {
            max-height: 200px;
            max-width: 200px;
        }

        .div_sty {
            width: 240px;
            height: 290px;
        }

        .div_sty_img {
            border: 5px solid #000;
            text-align: center;
            background-color: #FFF7FB;
            width: 400px;
            height: 400px;
            position: fixed;
            top: 200px;
            left: 15%;
            margin-left: 400px;
            padding: 5px;
            padding-bottom: 20px;
            z-index: 1011;
        }

        .check_sty {
            height: 20px;
            width: 20px;
        }

        .btn_sty {
            margin: 5px 0 0 0;
            width: 96px;
            color: #fff;
            background-color: #5db5dc;
            border-color: #2e6da4;
            font-size: 16px;
            border-radius: 5px;
            border: 1px solid transparent;
        }

        .p_sty {
            margin-bottom: -9px;
            margin-top: 5px;
        }

        .bk_choose {
            background-color: #0cc960;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $('.img_sty').lazyload({effect: "fadeIn"});
        });

        function setShopMainImg(shopId, coverPid, coverImg) {
            $.ajax({
                type: "post",
                url: "/cbtconsole/shopRecommend/setShopMainImg",
                dataType: "json",
                data: {
                    shopId: shopId,
                    coverPid: coverPid,
                    coverImg: coverImg
                },
                success: function (data) {
                    if (data.ok) {
                        $.messager.alert("提醒", "执行成功，即将刷新", "info");
                        setTimeout(function () {
                            window.location.reload();
                        }, 1500);
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", "获取失败，请重试", "error");
                }
            });
        }

        function saveShopGoods(shopId) {
            var pidArr = new Array();
            $(".check_sty").each(function () {
                if ($(this).is(":checked")) {
                    var pidInfo = {};
                    pidInfo["pid"] = $(this).parent().find(".pid_cls").val();
                    pidInfo["goodsImg"] = $(this).parent().find(".img_sty").attr("data-original");
                    pidArr[pidArr.length] = pidInfo;
                }
            });

            if (pidArr && pidArr.length > 0) {
                $("#show_message").show().text("正在执行...");
                $.ajax({
                    type: "post",
                    url: "/cbtconsole/shopRecommend/saveShopGoods",
                    data: {
                        shopId: shopId,
                        pidList: JSON.stringify(pidArr)
                    },
                    dataType: "json",
                    success: function (data) {
                        $("#show_message").hide();
                        if (data.ok) {
                            window.location.reload();
                        } else {
                            $.messager.alert("提醒", data.message, "error");
                        }
                    },
                    error: function (res) {
                        $("#show_message").hide();
                        $.messager.alert("提醒", "获取失败，请重试", "error");
                    }
                });
            } else {
                $.messager.alert("提醒", "请选择商品", "info");
            }
        }

        function changeBcColor(obj) {
            if ($(obj).is(":checked")) {
                $(obj).parent().parent().addClass("bk_choose");
            } else {
                $(obj).parent().parent().removeClass("bk_choose");
            }
        }
    </script>
</head>
<body>
<c:if test="${isShow == 0}">
    <h1 style="text-align: center;">${message}</h1>
</c:if>


<c:if test="${isShow > 0}">


    <div class="div_sty_img" style="display: none;" id="big_img"></div>
    <div>
        <p style="text-align: center;margin-bottom: -1px;"><b
                style="text-align: center;font-size: 18px;">店铺【${param.shopId}】推荐商品</b>
            <button class="btn_sty" style="margin-left: 60px;" onclick="saveShopGoods('${param.shopId}')">保存</button>
            <span id="show_message" style="color: red;display: none;">正在执行...</span></p>
    </div>

    <table border="1" cellpadding="0" cellspacing="0" align="center">
        <c:set var="count" value="0"/>
        <c:forEach items="${list}" var="gd">

            <c:set var="count" value="${count+1 }"/>
            <c:if test="${count == 1}">
                <tr>
            </c:if>
            <td class="div_sty">
                <div>
                    <p style="margin-top: -7px;margin-bottom: -3px;">
                        <input class="pid_cls" type="hidden" value="${gd.pid}"/>
                        <c:if test="${gd.isEdited > 0 || gd.bmFlag > 0}">
                            <input class="check_sty" id="ck_${gd.pid}" type="checkbox"
                                   checked="checked" ${gd.bmFlag > 0 ? 'disabled="disabled"' : ''}
                                   value="${gd.pid}" onclick="changeBcColor(this)"/>
                        </c:if>
                        <c:if test="${gd.isEdited == 0 && gd.bmFlag == 0}">
                            <input class="check_sty" id="ck_${gd.pid}" type="checkbox" value="${gd.pid}"
                                   onclick="changeBcColor(this)"/>
                        </c:if>
                        <span>${gd.pid}</span>
                        <img class="img_sty" src="/cbtconsole/img/beforeLoad.gif"
                             data-original="${gd.customMainImage}"/>
                    </p>
                    <p class="p_sty">
                        <span><em>$</em>&nbsp;&nbsp;${gd.rangePrice}</span>
                        <c:if test="${gd.isSoldFlag > 0}">
                            &nbsp;&nbsp;<span style="color: green;">免邮</span>
                        </c:if>
                    </p>
                    <p class="p_sty">
                        <a href="/cbtconsole/editc/detalisEdit?pid=${gd.pid}" target="_blank">编辑商品</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <c:if test="${gd.bmFlag > 0}">
                            <b style="color: green;font-size: 18px;">封面图</b>
                        </c:if>
                        <c:if test="${gd.bmFlag == 0}">
                        <button class="btn_sty"
                                onclick="setShopMainImg('${gd.shopId}','${gd.pid}','${gd.customMainImage}')">设为封面
                            </c:if>
                        </button>
                    </p>
                </div>
            </td>
            <c:if test="${count % 6 == 0}">
                </tr>
            </c:if>
        </c:forEach>
        <c:if test="${count % 6 > 0}">
            </tr>
        </c:if>
    </table>
</c:if>

</body>
</html>
