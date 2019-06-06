<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Hot Events</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <style>


        .btn_sty {
            margin: 5px 0 0 0;
            width: 140px;
            color: #fff;
            background-color: #5db5dc;
            border-color: #2e6da4;
            font-size: 16px;
            border-radius: 5px;
            border: 1px solid transparent;
        }

        .theme_ul {
            width: 380px;
            height: 450px;
            background: #fff;
            float: left;
            margin-right: 13px;
            box-sizing: border-box;
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            border: 1px solid #e0e0e0;
        }

        .theme_li {
            width: 100%;
            height: 100%;
        }

        .theme_top {
            width: 100%;
            height: 209px;
            position: relative;
        }

        .theme_top_link {
            display: block;
            width: 100%;
            height: 100%;
            position: relative;
        }

        .theme_bottom {
            width: 100%;
            height: 210px;
            overflow: hidden;
            position: relative;
        }

        .theme_bottom_ul {
            width: 100%;
            height: 210px;
        }

        .theme_bottom_li {
            width: 100%;
            height: 210px;
            position: absolute;
            left: 0;
            top: 0;
        }

        .theme_top_keyword {
            position: absolute;
            width: 100%;
            bottom: 15px;
            height: 30px;
            text-align: center;
        }

        .theme_keyword_link {
            display: inline-block;
            height: 30px;
            width: 88px;
            line-height: 30px;
            border-radius: 15px;
            color: #000;
            font-size: 14px;
            background: #fff;
            text-align: center;
            margin-right: 10px;
        }

        .theme_top_img {
            position: absolute;
            left: 0;
            top: 0;
            right: 0;
            bottom: 0;
            margin: auto;
            max-width: 100%;
            max-height: 100%;
            background: #fff no-repeat center center;
            min-width: 30%;
        }

        .theme_li_01 {
            width: 33.3333%;
            height: 160px;
            float: left;
        }

        .inp_wd {
            width: 400px;
            height: 30px;
        }

        .img_sty {
            max-width: 180px;
            max-height: 180px;
        }

        .img_min {
            max-width: 100px;
            max-height: 100px;
        }

        table {
            border-collapse: collapse;
            border: 2px solid black;
        }

        table th {
            background-color: #ece1e1;
            border: 2px solid #3F3F3F;
            text-align: center;
        }

        .edit_style td {
            min-width: 120px;
        }

        table td {
            max-width: 425px;
            border: 2px solid #ada8a8;
        }
    </style>
    <script>

        $(function () {
            closeAddInfoDialog();
            closeAddGoodsDialog();
        });

        function addInfoFun() {
            var info_img = $("#info_img").val();
            var info_link = $("#info_link").val();
            var child_name1 = $("#child_name1").val();
            var child_link1 = $("#child_link1").val();
            var child_name2 = $("#child_name2").val();
            var child_link2 = $("#child_link2").val();
            var child_name3 = $("#child_name3").val();
            var child_link3 = $("#child_link3").val();
            if (info_img && info_link && child_name1 && child_link1 && child_name2 && child_link2 && child_name3 && child_link3) {
                $.ajax({
                    type: 'POST',
                    dataType: 'text',
                    url: '/cbtconsole/hotEvents/insertIntoHotEventsInfo',
                    data: {
                        "infoImg": info_img,
                        "infoLink": info_link,
                        "childName1": child_name1,
                        "childLink1": child_link1,
                        "childName2": child_name2,
                        "childLink2": child_link2,
                        "childName3": child_name3,
                        "childLink3": child_link3
                    },
                    success: function (data) {
                        var json = eval('(' + data + ')');
                        if (json.ok) {
                            /*var content = '<ul class="theme_ul"><li class="theme_li"><div class="theme_top">'
                                + '<a href="' + info_link + '" class="theme_top_link"><img src="' + info_img + '"></a>'
                                + '<p class="theme_top_keyword"><a href="' + child_link1 + '" class="theme_keyword_link">' + child_name1 + '</a>'
                                + '<a href="' + child_link2 + '" class="theme_keyword_link">' + child_name2 + '</a>'
                                + '<a href="' + child_link3 + '" class="theme_keyword_link">' + child_name3 + '</a>'
                                + '</p></div></li></ul>';
                            $("#show_div").append(content);*/
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

        function closeAddInfoDialog() {
            $('#add_info').dialog('close');
            $("#addInfoForm")[0].reset();
        }

        function closeAddGoodsDialog() {
            $('#add_goods').dialog('close');
            $("#addGoodsForm")[0].reset();
        }


        function beforeAddGoods(eventsId) {
            $('#add_goods').dialog('open');
            $("#events_id").val(eventsId);
        }

        function queryGoodsFrom1688() {

            $("#show_notice").show();
            var pid = $("#idOrUrl").val();
            $.ajax({
                type: "post",
                url: "/cbtconsole/hotGoods/queryGoodsFrom1688.do",
                data: {
                    url: pid
                },
                dataType: "json",
                success: function (data) {
                    $("#show_notice").hide();
                    if (data.ok) {
                        var json = data.data;
                        $("#show_notice").hide();
                        $("#new_goods_pid").text(json.pid);
                        $("#new_goods_show_name").text(json.enname);
                        $("#new_goods_url").text(json.url);
                        $("#new_goods_img").attr("src",
                            json.remotpath + json.img);
                        $("#new_goods_price").text(json.price);
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (res) {
                    $("#show_notice").hide();
                    $.messager.alert("提醒", "获取失败，请重试", "error");
                }
            });
        }

        function addGoodsFun() {
            var events_id = $("#events_id").val();
            if (events_id && events_id > 0) {
                var pid = $("#new_goods_pid").text();
                $.ajax({
                    type: "post",
                    url: "/cbtconsole/hotEvents/insertIntoHotEventsGoods",
                    data: {
                        eventsId: events_id,
                        pid: pid
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
            } else {
                $.messager.alert("提醒", "获取events Id失败", "error");
            }
        }
    </script>
</head>
<body>

<c:if test="${isShow == 0}">
    <h1 style="text-align: center;">${message}</h1>
</c:if>


<c:if test="${isShow > 0}">


    <div id="add_info" class="easyui-dialog" title="新增events"
         data-options="modal:true"
         style="width: 580px; height: 400px; padding: 10px;">
        <form id="addInfoForm" method="post" onsubmit="false">
            <table>

                <tr>
                    <td>图片Url:</td>
                    <td><input id="info_img" class="inp_wd"/></td>
                </tr>
                <tr>
                    <td>跳转链接:</td>
                    <td><input id="info_link" class="inp_wd"/></td>
                </tr>

                <tr>
                    <td>子描述1:</td>
                    <td><input id="child_name1" class="inp_wd"/></td>
                </tr>
                <tr>
                    <td>子跳转链接1:</td>
                    <td><input id="child_link1" class="inp_wd"/></td>
                </tr>

                <tr>
                    <td>子描述2:</td>
                    <td><input id="child_name2" class="inp_wd"/></td>
                </tr>
                <tr>
                    <td>子跳转链接2:</td>
                    <td><input id="child_link2" class="inp_wd"/></td>
                </tr>

                <tr>
                    <td>子描述3:</td>
                    <td><input id="child_name3" class="inp_wd"/></td>
                </tr>
                <tr>
                    <td>子跳转链接3:</td>
                    <td><input id="child_link3" class="inp_wd"/></td>
                </tr>
            </table>

        </form>

        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="addInfoFun()" style="width: 80px">保存</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeAddInfoDialog()"
               style="width: 80px">关闭</a>
        </div>
    </div>

    <div id="add_goods" class="easyui-dialog" title="添加商品"
         data-options="modal:true"
         style="width: 580px; height: 420px; padding: 10px;">
        <input type="hidden" id="events_id" value="0"/>
        <form id="addGoodsForm" method="post" onsubmit="false">
            <table class="edit_style">
                <tr>
                    <td>PID</td>
                    <td><input id="idOrUrl" type="text" style="width: 250px;"/><input
                            type="button" value="读取" onclick="queryGoodsFrom1688()"/>
                        <span id="show_notice" style="color: red; display: none;">正在读取中...</span></td>

                </tr>

                <tr style="display: none;">
                    <td>商品链接：</td>
                    <td><span id="new_goods_url"></span></td>
                </tr>
                <tr>
                    <td>商品图片：</td>
                    <td><img id="new_goods_img" class="img_sty" src="#">
                </tr>
                <tr>
                    <td>商品PID：</td>
                    <td><span id="new_goods_pid"></span></td>
                </tr>

                <tr>
                    <td>显示名称：</td>
                    <td><span id="new_goods_show_name"></span></td>
                </tr>
            </table>

        </form>

        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="addGoodsFun()" style="width: 80px">保存</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeAddGoodsDialog()"
               style="width: 80px">关闭</a>
        </div>
    </div>

    <div>
        <h3 style="text-align: center"> Hot Events 管理</h3>
        <button class="btn_sty" onclick="$('#add_info').dialog('open');">新增Events数据</button>
    </div>

    <c:forEach items="${list}" var="info">

        <div id="show_div">
        <ul class="theme_ul">
            <li class="theme_li">
                <div class="theme_top"><a href="${info.link}"
                                          class="theme_top_link"><img class="theme_top_img"
                                                                      src="${info.imgUrl}"></a>
                    <p class="theme_top_keyword">
                        <a href="${info.childLink1}" class="theme_keyword_link">${info.childName1}</a>
                        <a href="${info.childLink2}" class="theme_keyword_link">${info.childName2}</a>
                        <a href="${info.childLink3}" class="theme_keyword_link">${info.childName3}</a>
                    </p>
                </div>
                <div class="theme_bottom">
                    <ul class="theme_bottom_ul theme_bottom_01">
                        <li class="theme_bottom_li">
                            <div class="theme_bottom_list">
                                <c:forEach items="${info.goodsList}" var="goods">
                                <div class="theme_li_01">
                                    <a href="${goods.onlineUrl}" class="theme_a_01">
                                        <span class="theme_img_span"> <img src="${goods.mainImg}"
                                                                           class="img_min"></span>
                                            <%--<span class="theme_span_01">${goods.enName}</span>
                                            <span class="theme_span_02">
                                                <i class="currency_unit">$</i>
                                                <i class="theme_init hide">${goods.price}</i>
                                                <i class="theme_now">${goods.price}</i>
                                            </span>--%>
                                    </a>
                                </div>
                                </c:forEach>
                        </li>
                    </ul>
                </div>
                <div>
                    <c:if test="${info.isOn > 0}">
                        <span>状态:<b style="color: green">开启</b></span>
                    </c:if>
                    <c:if test="${info.isOn == 0}">
                        <span>状态:<b style="color: red">关闭</b></span>
                    </c:if>
                    <button class="btn_sty" onclick="beforeAddGoods(${info.id})">新增产品</button>
                </div>
            </li>
        </ul>
    </c:forEach>
    </div>
</c:if>
</body>
</html>
