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
            height: 190px;
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
            width: 480px;
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

        table td {
            min-width: 120px;
            max-width: 580px;
            border: 2px solid #ada8a8;
        }
    </style>
    <script>

        $(function () {
            closeAddInfoDialog();
            closeAddGoodsDialog();
        });

        function beforeAddInfo(id, isOn) {
            if (id > 0) {
                $("#edit_id").val(id);
                $("#edit_is_on").prop("checked", isOn > 0);
                $("#add_title").text("??????events");
                $("#info_img").val($("#edit_info_img_" + id).attr("src"));
                $("#info_link").val($("#edit_info_link_" + id).attr("href"));
                $("#child_name1").val($("#edit_child_link1_" + id).text());
                $("#child_link1").val($("#edit_child_link1_" + id).attr("href"));
                $("#child_name2").val($("#edit_child_link2_" + id).text());
                $("#child_link2").val($("#edit_child_link2_" + id).attr("href"));
                $("#child_name3").val($("#edit_child_link3_" + id).text());
                $("#child_link3").val($("#edit_child_link3_" + id).attr("href"));
            } else {
                $("#add_title").text("??????events");
            }
            $('#add_info').dialog('open');
        }

        function addInfoFun() {
            var eventsId = $("#edit_id").val();
            var isOn = $("#edit_is_on").is(':checked') ? 1 : 0;
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
                        "eventsId": eventsId,
                        "infoImg": info_img,
                        "infoLink": info_link,
                        "childName1": child_name1,
                        "childLink1": child_link1,
                        "childName2": child_name2,
                        "childLink2": child_link2,
                        "childName3": child_name3,
                        "childLink3": child_link3,
                        "isOn": isOn
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
                            $.messager.alert("??????", json.message, "error");
                        }
                    },
                    error: function () {
                        $.messager.alert("??????", "????????????????????????", "error");
                    }
                });
            } else {
                $.messager.alert("??????", "?????????????????????", "info");
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
                        $("#new_goods_img").attr("src", json.remotpath + json.img);
                        $("#new_goods_price").text(json.price);
                    } else {
                        $.messager.alert("??????", data.message, "error");
                    }
                },
                error: function (res) {
                    $("#show_notice").hide();
                    $.messager.alert("??????", "????????????????????????", "error");
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
                            $.messager.alert("??????", data.message, "error");
                        }
                    },
                    error: function (res) {
                        $.messager.alert("??????", "????????????????????????", "error");
                    }
                });
            } else {
                $.messager.alert("??????", "??????events Id??????", "error");
            }
        }

        function deleteAddGoods(eventsId, pid) {
            $.messager.confirm('????????????', '??????????????????????????????????????????', function (r) {
                if (r) {
                    $.ajax({
                        type: "post",
                        url: "/cbtconsole/hotEvents/deleteIntoHotEventsGoods",
                        data: {
                            eventsId: eventsId,
                            pid: pid
                        },
                        dataType: "json",
                        success: function (data) {
                            if (data.ok) {
                                window.location.reload();
                            } else {
                                $.messager.alert("??????", data.message, "error");
                            }
                        },
                        error: function (res) {
                            $.messager.alert("??????", "????????????????????????", "error");
                        }
                    });
                }
            });
        }

        function syncToOnline() {
            $("#show_message").text("????????????...").show();
            $.ajax({
                type: "post",
                url: "/cbtconsole/hotEvents/genOnlineData",
                data: {},
                dataType: "json",
                success: function (data) {
                    if (data.ok) {
                        $("#show_message").text("????????????").show();
                    } else {
                        $("#show_message").text(data.message).show();
                    }
                },
                error: function (res) {
                    $("#show_message").text("??????????????????").show();
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


    <div id="add_info" class="easyui-dialog" title="Events"
         data-options="modal:true"
         style="width: 660px; height: 420px; padding: 10px;">
        <form id="addInfoForm" method="post" onsubmit="false">
            <table>
                <caption id="add_title">??????events</caption>
                <tr>
                    <td>??????Url:</td>
                    <td><input id="info_img" class="inp_wd"/></td>
                </tr>
                <tr>
                    <td>????????????:</td>
                    <td><input id="info_link" class="inp_wd"/></td>
                </tr>

                <tr>
                    <td>?????????1:</td>
                    <td><input id="child_name1" class="inp_wd"/></td>
                </tr>
                <tr>
                    <td>???????????????1:</td>
                    <td><input id="child_link1" class="inp_wd"/></td>
                </tr>

                <tr>
                    <td>?????????2:</td>
                    <td><input id="child_name2" class="inp_wd"/></td>
                </tr>
                <tr>
                    <td>???????????????2:</td>
                    <td><input id="child_link2" class="inp_wd"/></td>
                </tr>

                <tr>
                    <td>?????????3:</td>
                    <td><input id="child_name3" class="inp_wd"/></td>
                </tr>
                <tr>
                    <td>???????????????3:</td>
                    <td><input id="child_link3" class="inp_wd"/></td>
                </tr>
                <tr>
                    <td colspan="2">
                        <span style="font-size: 14px;"><input type="checkbox" id="edit_is_on" checked="checked"/>??????</span>
                    </td>
                </tr>
            </table>
        </form>

        <div style="text-align: center; padding: 5px 0">
            <input id="edit_id" type="hidden" value="0"/>
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="addInfoFun()" style="width: 80px">??????</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeAddInfoDialog()"
               style="width: 80px">??????</a>
        </div>
    </div>

    <div id="add_goods" class="easyui-dialog" title="????????????"
         data-options="modal:true"
         style="width: 580px; height: 420px; padding: 10px;">
        <input type="hidden" id="events_id" value="0"/>
        <form id="addGoodsForm" method="post" onsubmit="false">
            <table>
                <tr>
                    <td>PID</td>
                    <td><input id="idOrUrl" type="text" style="width: 250px;"/><input
                            type="button" value="??????" onclick="queryGoodsFrom1688()"/>
                        <span id="show_notice" style="color: red; display: none;">???????????????...</span></td>

                </tr>

                <tr style="display: none;">
                    <td>???????????????</td>
                    <td><span id="new_goods_url"></span></td>
                </tr>
                <tr>
                    <td>???????????????</td>
                    <td><img id="new_goods_img" class="img_sty" src="#">
                </tr>
                <tr>
                    <td>??????PID???</td>
                    <td><span id="new_goods_pid"></span></td>
                </tr>

                <tr>
                    <td>???????????????</td>
                    <td><span id="new_goods_show_name"></span></td>
                </tr>
            </table>

        </form>

        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="addGoodsFun()" style="width: 80px">??????</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeAddGoodsDialog()"
               style="width: 80px">??????</a>
        </div>
    </div>

    <div>
        <h3 style="text-align: center"> Hot Events ??????</h3>
        <button class="btn_sty" onclick="beforeAddInfo(0)">??????Events</button>
        <button class="btn_sty" style="margin-left: 60px;" onclick="syncToOnline()">???????????????</button>
        <span id="show_message" style="color: red;display: none;">????????????...</span>
    </div>

    <div id="show_div">
    <c:forEach items="${list}" var="info">
        <ul class="theme_ul">
            <li class="theme_li">
                <div class="theme_top"><a id="edit_info_link_${info.id}" href="${info.link}"
                                          class="theme_top_link"><img id="edit_info_img_${info.id}"
                                                                      class="theme_top_img"
                                                                      src="${info.imgUrl}"></a>
                    <p class="theme_top_keyword">
                        <a id="edit_child_link1_${info.id}" href="${info.childLink1}"
                           class="theme_keyword_link">${info.childName1}</a>
                        <a id="edit_child_link2_${info.id}" href="${info.childLink2}"
                           class="theme_keyword_link">${info.childName2}</a>
                        <a id="edit_child_link3_${info.id}" href="${info.childLink3}"
                           class="theme_keyword_link">${info.childName3}</a>
                        <button class="btn_sty" onclick="beforeAddInfo(${info.id},${info.isOn})">??????Events</button>
                    </p>
                </div>
                <div class="theme_bottom">
                    <ul class="theme_bottom_ul theme_bottom_01">
                        <li class="theme_bottom_li">
                            <div class="theme_bottom_list">
                                <c:forEach items="${info.goodsList}" var="goods">
                                <div class="theme_li_01">
                                    <a target="_blank" href="${goods.onlineUrl}" class="theme_a_01">
                                        <span class="theme_img_span"> <img src="${goods.mainImg}"
                                                                           class="img_min"></span>
                                            <%--<span class="theme_span_01">${goods.enName}</span>
                                            <span class="theme_span_02">
                                                <i class="currency_unit">$</i>
                                                <i class="theme_init hide">${goods.price}</i>
                                                <i class="theme_now">${goods.price}</i>
                                            </span>--%>
                                    </a>
                                    <a target="_blank" href="/cbtconsole/editc/detalisEdit?pid=${goods.pid}">????????????</a>
                                    <button class="btn_del" onclick="deleteAddGoods(${info.id},'${goods.pid}')">????????????
                                    </button>
                                </div>
                                </c:forEach>
                        </li>
                    </ul>
                </div>
                <div>
                    <c:if test="${info.isOn > 0}">
                        <span>??????:<b style="color: green">??????</b></span>
                    </c:if>
                    <c:if test="${info.isOn == 0}">
                        <span>??????:<b style="color: red">??????</b></span>
                    </c:if>
                    <button class="btn_sty" onclick="beforeAddGoods(${info.id})">????????????</button>
                </div>
            </li>
        </ul>
    </c:forEach>
    </div>
</c:if>
</body>
</html>
