<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <style type="text/css">
        .img_class {
            max-width: 200px;
            max-height: 200px;
        }

        .split_check_bok {
            width: 22px;
            height: 22px;
        }

        .btn {
            height: 30px;
            width: 70px;
            background: #1c9439;
            border: 0px solid #dcdcdc;
            color: #ffffff;
            cursor: pointer;
        }

        .btn_lh {
            height: 30px;
            width: 150px;
            background: #1c9439;
            border: 0px solid #dcdcdc;
            color: #ffffff;
            cursor: pointer;
        }
    </style>

    <script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.js"></script>
    <script type="text/javascript">
        function doNumSplit(orderNo) {
            var odIds = "";

            $(".split_check_bok").each(function () {
                if ($(this).is(":checked")) {
                    var tempId = $(this).attr("id");
                    odIds += "@" + tempId;
                }
            });
            if (odIds.length < 2) {
                $.messager.alert("提醒", "请选择拆单商品", "error");
                return;
            } else {
                $("#show_notice").show().text("正在执行中...");
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/orderSplit/splitGoodsByOverSea',
                    data: {
                        orderNo: orderNo,
                        odIds: odIds.substring(1)
                    },
                    success: function (data) {
                        $("#show_notice").hide();
                        if (data.ok) {
                            $("#show_notice").show().text("执行成功，请关闭当前页面.");
                        } else {
                            $.messager.alert("提醒", data.message, "error");
                        }
                    },
                    error: function (XMLResponse) {
                        $.messager.alert("提醒", "连接超时，请重试", "error");
                    }
                });
            }
        }

        function setOverSeaOrder(orderNo) {
            $("#show_notice").show().text("正在执行中...");
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '/cbtconsole/orderSplit/setOverSeaOrder',
                data: {
                    orderNo: orderNo
                },
                success: function (data) {
                    $("#show_notice").hide();
                    if (data.ok) {
                        $("#show_notice").show().text("执行成功，请关闭当前页面.");
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (XMLResponse) {
                    $.messager.alert("提醒", "连接超时，请重试", "error");
                }
            });
        }
    </script>
    <title>订单海外仓拆单</title>
</head>
<body>

<c:if test="${isShow == 0}">
    <h1 style="text-align: center;">${message}</h1>
</c:if>
<c:if test="${isShow > 0}">
    <p style="text-align: center;">
        <b style="font-size: 30px;">订单海外仓拆单</b>
        &nbsp;&nbsp;&nbsp;
        <button class="btn" onclick="doNumSplit('${param.orderNo}')">拆单</button>
        <button class="btn_lh" onclick="setOverSeaOrder('${param.orderNo}')">设置整单为海外仓订单</button>
        <span id="show_notice" style="display: none;color: red;">正在执行...</span>
    </p>

    <table style="width: 100%;border-color: #b6ff00;" border="1" cellpadding="1" cellspacing="0" align="center">
        <thead>
        <tr style="background: #e5e5e5;font-size: 15px;font-weight: bold;height: 40px;text-align: center;">
            <td>
                商品编号/购物车id
            </td>
            <td>
                详情
            </td>
            <td>
                购买信息
            </td>
            <td>
                拆单
            </td>

        </tr>
        </thead>
        <tbody>
        <c:forEach items="${odList}" var="odBean">
            <tr>
                <td>
                        ${odBean.goodsid}/${odBean.id}
                </td>
                <td>
                    <a>
                        <img class="img_class" src=" ${fn:replace(odBean.goods_img,'50x50','') }"
                             style="cursor: pointer;">
                    </a>
                </td>
                <td>
                    <span
                            style="color: red;">商品名称:</span><br>${odBean.state == 2? "<br>用户已取消":""}${odBean.goodsname}<br>
                    <span style="color: red;">客户下单规格:</span><br> <span
                        style="color: #00B1FF;display: inline-block;max-width: 400px;overflow: hidden;word-wrap: break-word;">
                        ${odBean.goods_type}
                </span><br> <span style="color: #8064A2; word-break: break-all;">${odBean.remark}</span>
                    <c:if test="${not empty odBean.img_type}">
                        <c:forEach items="${fn:split(odBean.img_type,'@')}"
                                   var="img_type" varStatus="i">
                            <c:if test="${fn:indexOf(img_type,'http') > -1}">
                                <img style="max-width: 60px;max-height: 60px;" src="${img_type}">&nbsp;
                            </c:if>
                        </c:forEach>
                    </c:if>
                    <span>客户购买数量:<b style="color: red;margin-right: 10px;">${odBean.yourorder}</b><em
                            style="font-weight: bold;">${odBean.goodsUnit}</em>
                        </span>
                </td>
                <td>
                    <span>
                        <input id="${odBean.id}" type="checkbox" class="split_check_bok"/>
                    </span>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

</body>

</html>