<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>童装网站店铺推荐设置</title>
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
    <style type="text/css">
        .img_shop {
            max-width: 180px;
            max-height: 180px;
        }

        .img_goods {
            max-width: 70px;
            max-height: 70px;
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

        .btn_del {
            margin: 5px 0 0 0;
            width: 96px;
            color: #fff;
            background-color: red;
            border-color: #2e6da4;
            font-size: 16px;
            border-radius: 5px;
            border: 1px solid transparent;
        }

        .div_sty {
            margin-right: 20px;
            float: left;
            background-color: antiquewhite;
            height: 460px;
            width: 360px;
        }

        .inp_wd {
            width: 220px;
            height: 30px;
        }

        .li_sty {
            float: left;
            list-style: none;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            closeAddInfoDialog();
        });

        function beforeAddInfo(shopId, id, isOn, sort) {
            if (id > 0) {
                $("#edit_id").val(id);
                $("#add_shop_id").val(shopId);
                $("#add_shop_id").attr("disabled", true);
                $("#edit_is_on").prop("checked", isOn > 0);
                $("#add_sort").val(sort);
                $("#add_title").text("编辑店铺");
            } else {
                $("#add_shop_id").attr("disabled", false);
                $("#edit_id").val(0);
                $("#add_title").text("新增店铺");
            }
            $('#add_info').dialog('open');
        }

        function addInfoFun() {
            var spId = $("#edit_id").val();
            var isOn = $("#edit_is_on").is(':checked') ? 1 : 0;
            var addShopId = $("#add_shop_id").val();
            var addSort = $("#add_sort").val();
            if (addShopId && addSort) {
                $.ajax({
                    type: 'POST',
                    dataType: 'text',
                    url: '/cbtconsole/shopRecommend/insertShopRecommendInfo',
                    data: {
                        "spId": spId,
                        "shopId": addShopId,
                        "sort": addSort,
                        "isOn": isOn
                    },
                    success: function (data) {
                        var json = eval('(' + data + ')');
                        if (json.ok) {
                            window.location.reload();
                        } else {
                            $.messager.alert("提醒", json.message, "error");
                        }
                    },
                    error: function () {
                        $.messager.alert("提醒", "链接超时，请重试", "error");
                    }
                });
            } else {
                $.messager.alert("提醒", "请全部输入数据", "info");
            }
        }

        function deleteShopInfo(shopId) {
            $.messager.confirm('系统提醒', '是否删除，删除后数据不可恢复', function (r) {
                if (r) {
                    $.ajax({
                        type: "post",
                        url: "/cbtconsole/shopRecommend/deleteShopRecommendInfoByShopId",
                        data: {
                            shopId: shopId
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data.ok) {
                                window.location.reload();
                            } else {
                                $.messager.alert("提醒", data.message, "error");
                            }
                        },
                        error: function (res) {
                            $.messager.alert("提醒", "获取失败，请重试", "error");
                        }
                    });
                }
            });
        }

        function closeAddInfoDialog() {
            $('#add_info').dialog('close');
            $("#addInfoForm")[0].reset();
        }

        function openShopGoods(shopId) {
            var param = "height=910,width=1360,top=70,left=280,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
            window.open("/cbtconsole/shopRecommend/queryGoodsListByShopId?shopId=" + shopId, "windows", param);
        }

        function syncToOnline() {
            $("#show_message").text("正在执行...").show();
            $.ajax({
                type: "post",
                url: "/cbtconsole/shopRecommend/genOnlineData",
                data: {},
                dataType: "json",
                success: function (data) {
                    if (data.ok) {
                        $("#show_message").text("刷新成功").show();
                    } else {
                        $("#show_message").text(data.message).show();
                    }
                },
                error: function (res) {
                    $("#show_message").text("网路链接失败").show();
                }
            });
        }
    </script>
</head>
<body>
<c:if test="${isShow == 0}">
    <h1 style="text-align: center;">${message}</h1>
</c:if>


<c:if test="${isShow > 0}">

    <div id="add_info" class="easyui-dialog" title="新增店铺"
         data-options="modal:true"
         style="width: 330px; height: 230px; padding: 10px;">
        <span id="add_title" style="margin-left: 100px;font-size: 22px;">新增店铺</span>
        <form id="addInfoForm" method="post" onsubmit="false">
            <table>
                <tr>
                    <td>店铺ID:</td>
                    <td><input id="add_shop_id" class="inp_wd"/></td>
                </tr>
                <tr>
                    <td>排序数:</td>
                    <td><input id="add_sort" type="number" class="inp_wd" value="0"/></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span style="font-size: 14px;"><input type="checkbox" id="edit_is_on"
                                                              checked="checked"/>启用</span>
                    </td>
                </tr>
            </table>
        </form>

        <div style="text-align: center; padding: 5px 0">
            <input id="edit_id" type="hidden" value="0"/>
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="addInfoFun()" style="width: 80px">保存</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeAddInfoDialog()"
               style="width: 80px">关闭</a>
        </div>
    </div>

    <div>
        <h3 style="text-align: center">童装网站店铺推荐设置</h3>
        <button class="btn_sty" onclick="beforeAddInfo('', 0, 1, 0)">添加店铺</button>
        <button class="btn_sty" style="margin-left: 60px;" onclick="syncToOnline()">刷新到线上</button>
        <span id="show_message" style="color: red;display: none;">正在执行...</span>
    </div>

    <div>
        <c:forEach items="${list}" var="info">
            <div class="div_sty">
                <input type="hidden" value="${info.coverPid}"/>
                <img title="封面图" class="img_shop" src="${info.coverImg}"/>
                <br>
                <span>
                    店铺ID:<a href="https://www.import-express.com/shop?sid=${info.shopId}"
                            target="_blank">${info.shopId}</a>
                </span>
                <ul style="height: 150px;">
                    <c:if test="${not empty info.goodsList}">
                        <c:forEach items="${info.goodsList}" var="gd">
                            <li class="li_sty">
                                <img class="img_goods" src="${gd.goodsImg}">
                            </li>
                        </c:forEach>
                    </c:if>
                </ul>
                <p><span>排序:${info.sort}</span>
                    &nbsp;&nbsp;<span>状态:
                    <c:if test="${info.isOn > 0}">
                        <b style="color: green">开启</b>
                    </c:if>
                        <c:if test="${info.isOn == 0}">
                            <b style="color: red">关闭</b>
                        </c:if>
                    </span>
                </p>
                <p>
                    <button class="btn_sty"
                            onclick="beforeAddInfo('${info.shopId}',${info.id},${info.isOn},${info.sort})">编辑
                    </button>
                    &nbsp;&nbsp;<button class="btn_del" onclick="deleteShopInfo('${info.shopId}')">删除</button>
                    &nbsp;&nbsp;<button class="btn_sty" onclick="openShopGoods('${info.shopId}')">选择商品</button>
                </p>
            </div>
        </c:forEach>
    </div>


</c:if>

</body>
</html>
