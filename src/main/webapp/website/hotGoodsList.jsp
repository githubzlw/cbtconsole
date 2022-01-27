<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>热卖商品</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <script type="text/javascript"
            src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <style>
        .but_edit {
            background: #009688;
            width: 90px;
            height: 29px;
            border: 1px #aaa solid;
            color: #fff;
        }

        .but_edit_2 {
            background: #009688;
            width: 110px;
            height: 29px;
            border: 1px #aaa solid;
            color: #fff;
        }

        .but_delete {
            background: #f44336;
            width: 60px;
            height: 29px;
            border: 1px #aaa solid;
            color: #fff;
        }

        .checkBg {
            background-color: #b6f5b6;
        }

        .check_sty {
            float: right;
            height: 24px;
            width: 24px;
        }

        .check_sty_all {
            height: 22px;
            width: 22px;
        }

        #edit_amazon_info {
            display: none;
            position: fixed;
            top: 30%;
            background: #8cdab6;
            padding: 50px;
            right: 35%;
            width: 460px;
            box-shadow: 1px 10px 15px #e2e2e2;
        }

        #add_goods {
            display: none;
            position: fixed;
            top: 23%;
            background: #8cdab6;
            padding: 50px;
            left: 35%;
            width: 480px;
            box-shadow: 1px 10px 15px #e2e2e2;
        }

        #add_or_edit_discount {
            display: none;
            position: fixed;
            top: 23%;
            background: #8cdab6;
            padding: 50px;
            left: 35%;
            width: 480px;
            box-shadow: 1px 10px 15px #e2e2e2;
        }

        .img_sty {
            max-width: 180px;
            max-height: 180px;
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


        //添加类别商品
        function addGoods(categoryId) {
            $("#add_goods_form")[0].reset();
            $("#nw_goods_category_id").val(categoryId);
            $("#add_goods").show();
            $("#nw_goods_flag_off").attr("checked", true);
        }

        function setGoodsDiscount(categoryId, goodsPid,obj) {

            $.ajax({
                type: 'POST',
                url: '/cbtconsole/hotManage/queryDiscountByHotIdAndPid',
                data: {
                    "categoryId": categoryId,
                    "goodsPid": goodsPid
                },
                success: function (data) {
                    if (data.ok) {
                        if (data.total > 0) {
                            var json = data.data;
                            $("#discount_edit").val(1);
                            $("#discount_id").val(json.id);
                            $("#discount_goods_pid").val(json.goodsPid);
                            $("#discount_begin_time").val(json.beginTime);
                            $("#discount_end_time").val(json.endTime);
                            $("#discount_percentage").val(json.percentage);
                            $("#discount_sort").val(json.sort);
                        }
                        showIsChoose(obj);
                        $("#discount_goods_pid").val(goodsPid);
                        $("#add_or_edit_discount").show();
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", "获取折扣数据错误，请联系管理员", "error");
                }
            });

        }
        
        function showIsChoose(obj) {
            $("#hot_goods_table tbody").find("tr").find("td").each(function () {
                $(this).removeClass("checkBg");
            });
            $(obj).parent().addClass("checkBg");
        }

        //显示类别商品
        function editAmazonInfo(categoryId, id, goodsPid, amazonPrice, asinCode, profitMargin, flag,obj) {
            $("#edit_amazon_info_form")[0].reset();
            $("#goods_id").val(id);
            $("#goods_category_id").val(categoryId);
            $("#goods_goodsPid").text(goodsPid);
            var goodsName = $("#goodsName_" + id).text();
            $("#goods_goodsname").text(goodsName);
            var showName = $("#showName_" + id).text();
            $("#goods_showname").text(showName);
            var goodsPrice = $("#goodsPrice_" + id).text();
            $("#goods_goodsprice").text(goodsPrice);
            $("#goods_amazon_price").val(amazonPrice);
            $("#goods_asin_code").val(asinCode);
            $("#goods_profit_margin").val(profitMargin);

            if (flag == 1) {
                $("#goods_flag_on").prop("checked", true);
                $("#goods_flag_off").removeAttr("checked");
            } else {
                $("#goods_flag_on").removeAttr("checked");
                $("#goods_flag_off").prop("checked", true);
            }
            $(".fade_bkgd").show();
            $("body").css({
                "overflow": "hidden",
                "height": "100%"
            });
            $("#edit_amazon_info").show();
            showIsChoose(obj);
            return false;
        }

        //更新类别商品
        function updateGoods() {
            var id = $("#goods_id").val();
            var goodsPid = $("#goods_goodsPid").text();
            var amazonPrice = $("#goods_amazon_price").val();
            var asinCode = $("#goods_asin_code").val();
            var profitMargin = $("#goods_profit_margin").val();
            var category_id = $("#goods_category_id").val();
            var goods_off = $("input[type='radio'][name='goods_off']").filter(
                ":checked");
            var flag = 0;
            if (goods_off.attr("id") == "goods_flag_on") {
                flag = 1;
            }
            if (id == null || id == "" || id == 0) {
                $.messager.alert("提醒", "获取商品id失败", "info");
                return;
            }
            if (category_id == null || category_id == "" || category_id == 0) {
                $.messager.alert("提醒", "获取类别id失败", "info");
                return;
            }
            if (goodsPid == null || goodsPid == "") {
                $.messager.alert("提醒", "获取goodsPid失败", "info");
                return;
            }
            if (amazonPrice == null || amazonPrice == "") {
                $.messager.alert("提醒", "获取亚马逊价格失败", "info");
                return;
            }
            if (asinCode == null || asinCode == "") {
                $.messager.alert("提醒", "获取ASIN码失败", "info");
                return;
            }

            $.ajax({
                type: 'POST',
                url: '/cbtconsole/hotGoods/updateGoods.do',
                data: {
                    "id": id,
                    "categoryId": category_id,
                    "goodsPid": goodsPid,
                    "amazonPrice": amazonPrice,
                    "asinCode": asinCode,
                    "profitMargin": profitMargin,
                    "flag": flag
                },
                success: function (data) {
                    if (data.ok) {
                        $("#edit_amazon_info").hide();
                        $.messager.alert("提醒", "保存成功，页面即将刷新", "info");
                        $(".fade_bkgd").hide();
                        $("body").css({
                            "overflow": "auto"
                        });
                        setTimeout(function () {
                            window.location.reload();
                        }, 1500);
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                }
            });
        }

        function saveGoods() {
            var id = $("#goods_id").val();
            var goodsPid = $("#nw_goods_goodsPid").text();
            var goodsName = $("#nw_goods_goodsname").text();
            var goodsUrl = $("#nw_goods_goodsurl").text();
            var goodsImg = $("#nw_goods_goodsimg")[0].src;
            var showName = $("#nw_goods_showname").text();
            var goodsPrice = $("#nw_goods_goodsprice").text();
            var category_id = $("#nw_goods_category_id").val();
            var amazonPrice = $("#nw_goods_amazon_price").val();
            var asinCode = $("#nw_goods_asin_code").val();
            var profitMargin = $("#nw_goods_profit_margin").val();

            var goods_off = $("input[type='radio'][name='nw_goods_off']").filter(
                ":checked");
            var flag = 0;
            if (category_id == null || category_id == "" || category_id == 0) {
                $.messager.alert("提醒", "获取类别id失败", "info");
                return;
            }
            if (goodsPid == null || goodsPid == "") {
                $.messager.alert("提醒", "获取商品Pid失败", "info");
                return;
            }
            if (goodsName == null || goodsName == "") {
                goodsName == "";
            }
            if (goodsUrl == null || goodsUrl == "") {
                $.messager.alert("提醒", "获取商品链接失败", "info");
                return;
            }
            if (goodsImg == null || goodsImg == "") {
                $.messager.alert("提醒", "获取图片链接失败", "info");
                return;
            }
            if (showName == null || showName == "") {
                $.messager.alert("提醒", "获取显示名称失败", "info");
                return;
            }
            if (amazonPrice == null || amazonPrice == "") {
                amazonPrice == "0"
            }
            if (asinCode == null || asinCode == "") {
                asinCode == ""
            }
            if (profitMargin == null || profitMargin == "") {
                $.messager.alert("提醒", "获取利润率失败", "info");
                return;
            }

            if (goods_off.attr("id") == "nw_goods_flag_on") {
                flag = 1;
            }
            $.messager.progress({
                    title: '正在执行中',
                    msg: '请等待...'
                });
            $.ajax({
                type: 'POST',
                url: '/cbtconsole/hotGoods/saveGoods.do',
                data: {
                    "id": id,
                    "goodsPid": goodsPid,
                    "goodsName": goodsName,
                    "goodsUrl": goodsUrl,
                    "goodsImg": goodsImg,
                    "showName": showName,
                    "categoryId": category_id,
                    "goodsPrice": goodsPrice,
                    "amazonPrice": amazonPrice,
                    "asinCode": asinCode,
                    "profitMargin": profitMargin,
                    "flag": flag
                },
                success: function (data) {
                    $.messager.progress('close');
                    if (data.ok) {
                        $("#add_goods").hide();
                        alert("保存成功，请等待数据拉取");
                    } else {
                        $.messager.alert("提醒", data.message, "info");
                    }
                },
                error: function (res) {
                    $.messager.progress('close');
                    $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                }
            });
        }

        function deleteGoods(categoryId, goodsPid) {
            $.messager.confirm('系统提醒', '是否删除，删除后数据不可恢复', function (r) {
                if (r) {
                    $.ajax({
                        type: "post",
                        url: "/cbtconsole/hotGoods/deleteGoodsByPid.do",
                        data: {
                            "categoryId": categoryId,
                            "goodsPid": goodsPid
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
                            $.messager.alert("提醒", "执行失败，请重试", "error");
                        }
                    });
                }
            });
        }

        //根据1688url获取1688商品信息
        function queryGoodsFrom1688() {
            $("#show_notice").show();
            var val = $("#idOrUrl").val();
            $.ajax({
                type: "post",
                url: "/cbtconsole/hotGoods/queryGoodsFrom1688.do",
                data: {
                    url: val
                },
                dataType: "json",
                success: function (data) {
                    $("#show_notice").hide();
                    if (data.ok) {
                        var json = data.data;
                        $("#show_notice").hide();
                        $("#nw_goods_goodsPid").text(json.pid);
                        $("#nw_goods_goodsname").text(json.name);
                        $("#nw_goods_showname").text(json.enname);
                        $("#nw_goods_goodsurl").text(json.url);
                        if(json.salable > 0){
                            $("#nw_goods_salable").text("美加不可售卖");
                        }else{
                            $("#nw_goods_salable").text("美加可售卖");
                        }
                        var img = json.img;
                        if(img.indexOf("http:") > -1 || img.indexOf("https:") > -1){
                            $("#nw_goods_goodsimg").attr("src", img);
                        }else{
                            $("#nw_goods_goodsimg").attr("src", json.remotpath + img);
                        }
                        $("#nw_goods_goodsprice").text(json.price);
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

        //根据id关闭DIV
        function closeDiv(id) {
            if (id != null && id != '') {
                $("#add_goods").hide();
                $("#" + id).hide();
                $(".fade_bkgd").hide();
                $("body").css({
                    "overflow": "auto"
                });
            }
        }

        function closeGoodsSave() {
            $("#add_goods").hide();
        }

        function closeDiscountSet() {
            $("#add_or_edit_discount").hide();
        }

        function resetGoodsData() {
            $("#idOrUrl").val("");
            $("#nw_goods_goodsPid").text("");
            $("#nw_goods_showname").text("");
            $("#nw_goods_goodsname").text("");
            $("#nw_goods_goodsurl").text("");
            $("#nw_goods_salable").text("");
            $("#nw_goods_goodsimg").attr("src", "#");
            $("#nw_goods_goodsprice").text("0.00");
            $("#nw_goods_amazon_price").val("0.00");
            $("#nw_goods_asin_code").val("");
            $("#nw_goods_profit_margin").val("0.00");
        }

        function chooseBox(obj) {
            var is = $(obj).is(':checked');
            if (is) {
                $(obj).addClass("isChoose");
                $(obj).parent().addClass("checkBg");
            } else {
                $(obj).removeClass("isChoose");
                $(obj).parent().removeClass("checkBg");
            }
        }

        function chooseAll(obj) {
            var is = $(obj).is(':checked');
            if (is) {
                $(".check_sty").each(function () {
                    $(this).prop("checked", true);
                    $(this).addClass("isChoose");
                    $(this).parent().addClass("checkBg");
                });
            } else {
                $(".check_sty").each(function () {
                    $(this).removeAttr("checked");
                    $(this).removeClass("isChoose");
                    $(this).parent().removeClass("checkBg");
                });
            }
        }

        function useHotGoods(state,categoryId) {
            var pids = "";
            $(".check_sty").each(function () {
                if ($(this).is(':checked')) {
                    pids += "," + $(this).val();
                }
            });
            if (pids == "") {
                $.messager.alert("提醒", "请选择需要执行的数据", "info");
                return false;
            }
            $.ajax({
                type: "POST",
                url: "/cbtconsole/hotGoods/useHotGoods.do",
                data: {
                    pids: pids.substring(1),
                    categoryId: categoryId,
                    state: state
                },
                success: function (data) {
                    if (data.ok) {
                        $.messager.alert("提醒", '保存成功，即将刷新页面', "info");
                        setTimeout(function () {
                            window.location.reload();
                        }, 1500);
                    } else {
                        $.messager.alert("提醒", '执行错误:' + data.message, "error");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", '保存错误，请联系管理员', "error");
                }
            });
        }

        function useGoodsPromotionFlag() {
            var pids = "";
            $(".check_sty").each(function () {
                if ($(this).is(':checked')) {
                    pids += "," + $(this).val();
                }
            });
            if (pids == "") {
                $.messager.alert("提醒", "请选择需要执行的数据", "info");
                return false;
            }
            $.ajax({
                type: "POST",
                url: "/cbtconsole/hotManage/useGoodsPromotionFlag",
                data: {
                    pids: pids.substring(1)
                },
                success: function (data) {
                    if (data.ok) {
                        $.messager.alert("提醒", '保存成功，即将刷新页面', "info");
                        setTimeout(function () {
                            window.location.reload();
                        }, 1500);
                    } else {
                        $.messager.alert("提醒", '执行错误:' + data.message, "error");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", '保存错误，请联系管理员', "error");
                }
            });
        }

        function querySearchGoods(id) {
            var webType = $("#web_size_type").val();
            var localUrl = window.location.href;
            var url = "http://192.168.1.29:8081/goodslistxx?background=1&hotid=";
            if (localUrl.indexOf(".1.27:") > -1 || localUrl.indexOf(".1.9:") > -1) {
                url = "https://www.import-express.com/goodslistxx?background=1&hotid=";
                if (webType == 2) {
                    url = "https://www.kidscharming.com/goodslistxx?background=1&hotid=";
                } else if (webType == 3){
                    url = "https://www.petstoreinc.com/goodslistxx?background=1&hotid=";
                } else if (webType == 4){
                    url = "https://www.restaurantkitchenequipments.com/goodslistxx?background=1&hotid=";
                }
            } else if (localUrl.indexOf(".1.29:") > -1) {
                url = "http://192.168.1.29:8081/goodslistxx?background=1&hotid=";
            }else if(localUrl.indexOf("127.0.0.1") > -1 || localUrl.indexOf("localhost") > -1){
                url = "http://127.0.0.1:8087/goodslistxx?background=1&hotid=";
            }
            var param = "height=900,width=1666,top=0,left=200,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
            window.open(url + id, "windows", param);
        }

        //实时计算利润率
        function calculateProfitMargin(amazonPrice, num) {
            if (amazonPrice == 0) {
                $.messager.alert("提醒", '获取亚马逊价格失败', "error");
                return false;
            }
            var pid = "";
            if (num > 0) {
                pid = $("#nw_goods_goodsPid").text();
            } else {
                pid = $("#goods_goodsPid").text();
            }
            if (pid == "") {
                $.messager.alert("提醒", '获取PID失败', "error");
                return false;
            }
            $.ajax({
                type: "POST",
                url: "/cbtconsole/hotGoods/calculateProfitMargin.do",
                data: {
                    pid: pid,
                    amazonPrice: amazonPrice
                },
                success: function (data) {
                    if (data.ok) {
                        if (num > 0) {
                            pid = $("#nw_goods_profit_margin").val(data.data);
                        } else {
                            pid = $("#goods_profit_margin").val(data.data);
                        }
                    } else {
                        $.messager.alert("提醒", '执行错误:' + data.message, "error");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", '执行错误，请联系管理员', "error");
                }
            });
        }

        function saveDiscountSet() {
            var isEdit = $("#discount_edit").val();
            var id = $("#discount_id").val();
            var goodsPid = $("#discount_goods_pid").val();
            var category_id = $("#discount_category_id").val();

            var discount_percentage = $("#discount_percentage").val();
            var discount_begin_time = $("#discount_begin_time").val();
            var discount_end_time = $("#discount_end_time").val();
            var discount_sort = $("#discount_sort").val();
            if (category_id == null || category_id == "" || category_id == 0) {
                $.messager.alert("提醒", "获取类别id失败", "info");
                return;
            }
            if (goodsPid == null || goodsPid == "") {
                $.messager.alert("提醒", "获取商品Pid失败", "info");
                return;
            }
            if (discount_percentage == null || discount_percentage == "" || discount_percentage == 0) {
                $.messager.alert("提醒", "获取折扣百分比失败", "info");
                return;
            }
            if (discount_begin_time == null || discount_begin_time == "") {
                $.messager.alert("提醒", "获取折扣开始时间失败", "info");
                return;
            }
            if (discount_end_time == null || discount_end_time == "") {
                $.messager.alert("提醒", "获取折扣结束时间失败", "info");
                return;
            }
            if (discount_sort == null || discount_sort == "") {
                discount_sort = "0";
            }
            var url = '/cbtconsole/hotManage/insertIntoDiscount';
            if (isEdit > 0) {
                //编辑
                url = '/cbtconsole/hotManage/updateDiscountInfo';
            }
            $.ajax({
                type: 'POST',
                url: url,
                data: {
                    "id": id,
                    "goodsPid": goodsPid,
                    "hotId": category_id,
                    "percentage": discount_percentage,
                    "beginTime": discount_begin_time,
                    "endTime": discount_end_time,
                    "sort": discount_sort
                },
                success: function (data) {
                    if (data.ok) {
                        //$("#add_goods").hide();
                        window.location.reload();
                        //$.messager.alert("提醒", "保存成功", "info");
                    } else {
                        $.messager.alert("提醒", data.message, "info");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", "保存错误，请联系管理员", "error");
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
    <h1 style="text-align: center"><b style="color: green;">[${categoryName}]</b>的商品</h1>
    <table id="hot_goods_table" border="1" cellpadding="0" cellspacing="0" align="left">
        <thead>
        <tr>
            <td colspan="5">全选<input type="checkbox" class="check_sty_all"
                                     onclick="chooseAll(this)"/> &nbsp;&nbsp;&nbsp;<input
                    type="button" class="but_edit" onclick="useHotGoods(1,${categoryId})"
                    value="启用 "/>
                &nbsp;&nbsp;&nbsp;<input type="button"
                                                           class="but_edit" onclick="useGoodsPromotionFlag()" value="标记促销商品 "/>
                &nbsp;&nbsp;&nbsp;<input type="button"
                                                           class="but_delete" onclick="useHotGoods(0,${categoryId})" value="关闭 "/>
                &nbsp;&nbsp;&nbsp;
                <b style="color: red;">(提示：绿色背景表示当前商品已选中；点击图片可直接进入电商网站产品单页)</b> <br>
                <br>
                <div id="add_goods_btn">
                    <b style="float:left;">统计：${isOnTotal}/${allTotal}(启用数/总数)</b>
                    <input type="button" class="but_edit_2" value="添加商品" onclick="addGoods('${categoryId}')"/>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" class="but_edit_2" value="批量导入商品" onclick="querySearchGoods('${categoryId}')"/>
                    <input type="hidden" id="web_size_type" value="${webType}"/>
                </div>
            </td>
        </tr>
        </thead>
        <tbody>
        <c:set var="total1" value="${fn:length(goodsList)}"/>
        <c:set var="count1" value="0"/>
        <c:forEach items="${goodsList}" var="goods" varStatus="index">
        <c:set var="count1" value="${count1 + 1 }"/>
        <c:if test="${count1 == 1}">
        <tr>
            </c:if>
            <td style="font-size: 17px;">
                <a target="_blank" href="${goods.goodsUrl}"><img class="img_sty" alt="无图" src="${goods.goodsImg}"></a>
                <input type="checkbox" class="check_sty" onclick="chooseBox(this)" value="${goods.goodsPid}"><br>
                <span id="showName_${goods.id}">${goods.showName}</span><br>
                <span id="goodsPrice_${goods.id}">价格:$${goods.showPrice}&nbsp;&nbsp;<em>${goods.goodsUnit}</em></span>
                &nbsp;&nbsp;<span>(<a target="_blank"
                                      href=https://detail.1688.com/offer/${goods.goodsPid}.html">商品原链接</a>)</span><br>
                <span>亚马逊价格:$&nbsp;${goods.amazonPrice}</span><br>
                <span>ASIN码:${goods.asinCode}</span><br>
                <span>利润率:${goods.profitMargin}</span><em>%</em><br>
                <span>创建时间:${goods.productCreateTime}</span><br>
                <span>编辑时间:${goods.productPublishTime}</span><br>
                <b style="color:${goods.isOn > 0 ? 'green':'red'};">状态:${goods.isOn > 0 ? '开启':'关闭'}</b>&nbsp;&nbsp;
                <a target="_blank" href="/cbtconsole/editc/detalisEdit?pid=${goods.goodsPid}">编辑商品详情</a>
                <c:if test="${goods.isSoldFlag > 1}">
                    &nbsp;&nbsp;<b style="background-color: #e2305b;color: #f1eb0a;">免邮商品</b>
                </c:if>
                <br>
                <c:if test="${goods.promotionFlag > 0}">
                    <b style="color:red;">促销商品</b>
                </c:if>
                <c:if test="${goods.unsellableReason == 26}">
                    <br><b style="color:red;">1688货源异常</b>
                </c:if>
                <br>
                <c:if test="${goods.salable > 0}">
                    <b style="color:red;">美加不可售</b><br>
                </c:if>
                <c:if test="${hotType == 2}">
                    <br>
                    <c:if test="${goods.discountId>0}">
                        <div style="background-color: #9ecdef">
                            <span>折扣开始时间:[${goods.discountBeginTime}]<br>折扣结束时间:[${goods.discountEndTime}]</span><br>
                            <span>虚拟折扣比:[${goods.discountPercentage}%]&nbsp;&nbsp;折扣排序:[${goods.discountSort}]</span>
                            <span><br>虚拟原价:[${goods.maxPrice}*(1+${goods.discountPercentage}%)=${goods.virtualOldPrice}]</span><br>
                        </div>
                    </c:if>
                    <input class="but_edit_2" type="button" value="折扣设置"
                           onclick="setGoodsDiscount(${categoryId},'${goods.goodsPid}',this)"/>&nbsp;&nbsp;
                </c:if>
                <input class="but_edit_2" type="button" value="编辑亚马逊数据"
                       onclick="editAmazonInfo(${categoryId},${goods.id},'${goods.goodsPid}',${goods.amazonPrice},'${goods.asinCode}',${goods.profitMargin},${goods.isOn},this)"/>
                &nbsp;&nbsp;<input class="but_delete" type="button" value="删除"
                                   onclick="deleteGoods(${categoryId},'${goods.goodsPid}')">
            </td>
            <c:if test="${count1 % 5 == 0}">
        </tr>
        <tr>
            </c:if>
            </c:forEach>
            <c:if test="${total1 % 4 > 0}">
        </tr>
        </c:if>
        </tbody>
    </table>
</c:if>


<div id="edit_amazon_info">
    <form id="edit_amazon_info_form" action="#" onsubmit="return false;">
        <table class="edit_style">
            <tr>
                <td colspan="2" align="center">
                    <h3 class="text_show">编辑亚马逊数据</h3>
                    <input id="goods_category_id" type="hidden" value=""/>
                    <input id="goods_id" type="hidden" value=""/>
                </td>
            </tr>

            <tr>
                <td>商品名：</td>
                <td><span id="goods_goodsname"></span></td>
            </tr>

            <tr>
                <td>商品PID：</td>
                <td><span id="goods_goodsPid"></span></td>
            </tr>

            <tr>
                <td>显示名称：</td>
                <td><span id="goods_showname"></span></td>
            </tr>

            <tr>
                <td>亚马逊价：</td>
                <td><input id="goods_amazon_price" type="text" value="0.00"
                           onblur="calculateProfitMargin(this.value,0)"/><em>$</em></td>
            </tr>

            <tr>
                <td>ASIN码：</td>
                <td><input id="goods_asin_code" type="text" value=""/></td>
            </tr>

            <tr>
                <td>利润率：</td>
                <td><input id="goods_profit_margin" type="text" value="0.00" readonly="readonly"/><em>%</em></td>
            </tr>

            <tr>
                <td>状态：</td>
                <td><input type="radio" id="goods_flag_on" name="goods_off"/>
                    <label>启用</label>&nbsp;&nbsp; <input type="radio"
                                                         id="goods_flag_off" name="goods_off"/> <label>关闭</label></td>
            </tr>

            <tr>
                <td colspan="2" align="center"><input id="goods_update"
                                                      type="button" value="保存" onclick="updateGoods()"/> <input
                        type="button" value="取消" onclick="closeDiv('edit_amazon_info')"/></td>
            </tr>

        </table>

    </form>
</div>


<div id="add_goods">

    <form id="add_goods_form" action="#" onsubmit="return false;">
        <table class="edit_style">
            <tr>

                <td colspan="2" align="center"><h3 class="text_show">添加商品</h3>
                    <input id="nw_goods_category_id" type="hidden" value=""/></td>
            </tr>

            <tr>
                <td>PID</td>
                <td><input id="idOrUrl" type="text" style="width: 250px;"/><input
                        type="button" value="读取" onclick="queryGoodsFrom1688()"/><input
                        type="button" value="重置" onclick="resetGoodsData()"/> <span
                        id="show_notice" style="color: red; display: none;">正在读取中...</span></td>

            </tr>

            <tr style="display: none;">
                <td>商品链接：</td>
                <td><span id="nw_goods_goodsurl"></span></td>
            </tr>
            <tr>
                <td>商品图片：</td>
                <td><img id="nw_goods_goodsimg" class="img_little" alt="无图"
                         src="#"> <!-- <span id="nw_goods_goodsimg"></span> --></td>
            </tr>
            <tr>
                <td>商品名：</td>
                <td><span id="nw_goods_goodsname"></span></td>
            </tr>
            <tr>
                <td>商品PID：</td>
                <td><span id="nw_goods_goodsPid"></span></td>
            </tr>
            <tr>
                <td>美加可售卖：</td>
                <td><span id="nw_goods_salable"></span></td>
            </tr>
            <tr>
                <td>显示名称：</td>
                <td><span id="nw_goods_showname"></span></td>
            </tr>

            <tr>
                <td>市场价：</td>
                <td><span id="nw_goods_goodsprice">0.00</span>$</td>
            </tr>

            <tr>
                <td>亚马逊价：</td>
                <td><input id="nw_goods_amazon_price" type="text"
                           value="0.00" onblur="calculateProfitMargin(this.value,1)"/><em>$</em></td>
            </tr>

            <tr>
                <td>ASIN码：</td>
                <td><input id="nw_goods_asin_code" type="text" value=""/></td>
            </tr>

            <tr>
                <td>利润率：</td>
                <td><input id="nw_goods_profit_margin" type="text" value="0.00" readonly="readonly"/><em>%</em></td>
            </tr>

            <tr>
                <td>状态：</td>
                <td><input type="radio" id="nw_goods_flag_on"
                           name="nw_goods_off"/> <label>启用</label>&nbsp;&nbsp; <input
                        type="radio" id="nw_goods_flag_off" name="nw_goods_off"/> <label>关闭</label></td>
            </tr>

            <tr>
                <td colspan="2" align="center"><input id="goods_save"
                                                      type="button" value="保存" onclick="saveGoods()"/> <input
                        type="button" value="取消" onclick="closeGoodsSave()"/></td>
            </tr>

        </table>
    </form>

</div>


<div id="add_or_edit_discount">

    <form id="discount_form" action="#" onsubmit="return false;">
        <table class="edit_style">
            <caption id="discount_caption">折扣设置</caption>

            <tr>
                <td>类别名称:</td>
                <td>
                    <input id="discount_category_name" type="text" style="width: 250px;" readonly="readonly"
                           value="${categoryName}"/>
                    <input id="discount_category_id" type="hidden" value="${categoryId}"/>
                    <input id="discount_edit" type="hidden" value="0"/>
                    <input id="discount_id" type="hidden" value="0"/>
                </td>
            </tr>

            <tr>
                <td>商品PID:</td>
                <td><input id="discount_goods_pid" type="text" style="width: 250px;" readonly="readonly"/></td>
            </tr>

            <tr>
                <td>折扣优惠百分比:</td>
                <td><input id="discount_percentage" type="number" style="width: 250px;" value=""/></td>
            </tr>

            <tr>
                <td>开始时间:</td>
                <td><input id="discount_begin_time" type="text" style="width: 250px;" value=""
                           onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/></td>
            </tr>

            <tr>
                <td>结束时间:</td>
                <td><input id="discount_end_time" type="text" style="width: 250px;" value=""
                           onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/></td>
            </tr>

            <tr>
                <td>排序:</td>
                <td><input id="discount_sort" type="number" style="width: 250px;"/></td>
            </tr>

            <tr>
                <td colspan="2" align="center"><input type="button" value="保存" onclick="saveDiscountSet()"/> <input
                        type="button" value="取消" onclick="closeDiscountSet()"/></td>
            </tr>

        </table>
    </form>

</div>

</body>
</html>
