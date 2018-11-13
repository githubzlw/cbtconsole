<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>店铺数据整理和上线</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <style type="text/css">
        .s_btn {
            display: inline-block;
            width: 125px;
            height: 40px;
            background: #169bd4;
            margin: 20px 20px;
            border-radius: 10px;
            line-height: 40px;
            text-align: center;
            color: #fff;
            cursor: pointer;
            font-size: 14px;
        }

        .s_btn_l {
            display: inline-block;
            width: 90px;
            height: 30px;
            background: red;
            border-radius: 10px;
            line-height: 30px;
            text-align: center;
            color: #fff;
            cursor: pointer;
            font-size: 14px;
        }

        .s_btn_l_tt {
            display: inline-block;
            width: 90px;
            height: 25px;
            background: #ff0000a6;
            border-radius: 5px;
            line-height: 25px;
            text-align: center;
            color: #fff;
            cursor: pointer;
            font-size: 14px;
        }

        .s_btn_ot {
            display: inline-block;
            width: 120px;
            height: 40px;
            background: red;
            border-radius: 10px;
            line-height: 40px;
            text-align: center;
            color: #fff;
            cursor: pointer;
            font-size: 14px;
        }

        .inp_sty {
            height: 22px;
            width: 80px;
        }

        .inp_sty_lt {
            height: 20px;
            width: 45px;
        }

        .inp_sty_md {
            height: 20px;
            width: 150px;
        }

        .radio_sty {

        }

        .key_table {
            float: left;
            margin-top: 8px;
            margin-bottom: 3px;
        }

        .add_bt {
            right: 0px;
            bottom: 0px;
            width: 105px;
            height: 25px;
            background-color: #00ff0880;
        }

        .tb_div {
            position: relative;
            width: 420px;
            float: left;
        }

        .img_sty {
            max-width: 200px;
            max-height: 200px;
        }

        .checkBg {
            background-color: #b6f5b6;
        }

        .check_sty {
            height: 22px;
            width: 22px;
        }

        .check_sty_all {
            height: 22px;
            width: 22px;
        }
    </style>
    <script type="text/javascript">
        function saveAndUpdateGoods(shopId) {

            var json = [];
            var isSave = true;
            $("#shop_category_id tbody")
                .find("tr")
                .each(
                    function (index) {
                        var tdArr = $(this).find("td");
                        var catidPam = {};
                        var categoryId = tdArr.eq(1).text();
                        catidPam["categoryId"] = categoryId;
                        var avgWeight = tdArr.eq(5).find(".avg_weight").val();
                        if (avgWeight == null || avgWeight == "0"
                            || avgWeight == "" || avgWeight == "0.0") {
                            $.messager.alert("提醒", "第" + (index + 1)
                                + "行，类别：" + categoryId
                                + "，获取平均重量失败，请确认数据输入正常", "info");
                            isSave = false;
                            return false;
                        } else {
                            catidPam["weightVal"] = avgWeight;
                        }
                        var keyWeightDivs = tdArr.eq(5).find(".tb_div")
                            .find("div");
                        var keyWeight = "";
                        var noKeyWeight = false;
                        if (!(keyWeightDivs == null || keyWeightDivs.length == 0)) {
                            keyWeightDivs.each(function () {
                                var inpArr = $(this).find("input");
                                var tempId = inpArr.eq(0).val();
                                var tempWeight = inpArr.eq(1).val();
                                var tempKeyword = inpArr.eq(2).val();
                                if (tempWeight == null || tempWeight == ""
                                    || tempKeyword == null
                                    || tempKeyword == "") {
                                    noKeyWeight = true;
                                    isSave = false;
                                    return false;
                                } else {
                                    keyWeight += "," + tempId + "@" + tempWeight + "@" + tempKeyword;
                                }
                            });
                        }
                        if (noKeyWeight) {
                            $.messager.alert("提醒", "第" + (index + 1)
                                + "行，获取重量或者关键词失败，请确认数据输入正常", "info");
                            isSave = false;
                            return false;
                        }
                        catidPam["keyWeight"] = keyWeight.substring(1);
                        catidPam["firstIntervalRate"] = tdArr.eq(6).find(
                            "input").val();
                        catidPam["otherIntervalRate"] = tdArr.eq(8).find(
                            "input").val();
                        json[json.length] = catidPam;
                    });
            if (isSave) {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/ShopUrlC/saveAndUpdateInfos.do',
                    data: {
                        shopId: shopId,
                        infos: JSON.stringify(json)
                    },
                    success: function (data) {
                        if (data.ok) {
                            $.messager.alert("提醒", "执行成功,页面即将刷新", "info");
                            setTimeout(function () {
                                window.location.reload();
                            }, 500);
                        } else {
                            $.messager.alert("提醒", data.message, "error");
                        }
                    },
                    error: function (XMLResponse) {
                        $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                    }
                });
            }
        }


        function doGoodsClear(shopId) {

            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '/cbtconsole/ShopUrlC/doGoodsClear.do',
                data: {
                    shopId: shopId
                },
                success: function (data) {
                    if (data.ok) {
                        $.messager.alert("提醒", "执行成功,页面即将刷新", "info");
                        setTimeout(function () {
                            window.location.reload();
                        }, 500);
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (XMLResponse) {
                    $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                }
            });
        }


        function addKeyWeight(divId) {
            var str = '<div><input type="hidden" value="0"/>&nbsp;'
                + '<input class="inp_sty_lt" type="text" value="" placeholder="重量" />'
                + '&nbsp;<input class="inp_sty_md" type="text" value="" placeholder="关键词" /></div>';
            $("#" + divId).append(str);
        }

        function deleteKeyWeight(divId) {
            $("#" + divId).find("div:last").remove();
        }

        function chooseOrWeight() {
            $(".av_radio_cs").each(function () {
                $(this).removeAttr("checked");
            });
            $(".or_radio_cs").each(function () {
                $(this).prop("checked", "checked");
            });
        }

        function showCatidErrorWeightGoods(shopId) {
            window.open("/cbtconsole/ShopUrlC/showCatidErrorWeightGoods?shopId=" + shopId);
        }

        function chooseBox(obj) {
            var is = $(obj).is(':checked');
            if (is) {
                $(obj).addClass("isChoose");
                $(obj).parent().parent().addClass("checkBg");
            } else {
                $(obj).removeClass("isChoose");
                $(obj).parent().parent().removeClass("checkBg");
            }
        }

        function chooseAll(obj) {
            var is = $(obj).is(':checked');
            if (is) {
                $(".check_sty").each(function() {
                    $(this).prop("checked", true);
                    $(this).addClass("isChoose");
                    $(this).parent().parent().addClass("checkBg");
                });
            } else {
                $(".check_sty").each(function() {
                    $(this).removeAttr("checked");
                    $(this).removeClass("isChoose");
                    $(this).parent().parent().removeClass("checkBg");
                });
            }
        }

        function deleteCatidGoods(shopId) {
            var catids = "";
            $(".check_sty").each(function() {
                if ($(this).is(':checked')) {
                    catids += "," + $(this).val();
                }
            });
            if (catids == "") {
                $.messager.alert("提醒", "请选择需要删除的类别数据", "info");
                return false;
            }
            $.messager.confirm('系统提醒', '是否删除，删除后数据不可恢复', function(r) {
                if (r) {
                    $.ajax({
                        type : "POST",
                        url : "/cbtconsole/ShopUrlC/deleteCatidGoods",
                        data : {
                            shopId : shopId,
                            catids : catids.substring(1)
                        },
                        success : function(data) {
                            if (data.ok) {
                                $.messager.alert("提醒", '执行成功，即将刷新页面', "info");
                                setTimeout(function() {
                                    window.location.reload();
                                }, 1500);
                            } else {
                                $.messager.alert("提醒", '执行错误:' + data.message, "error");
                            }
                        },
                        error : function(res) {
                            $.messager.alert("提醒", '执行错误，请联系管理员', "error");
                        }
                    });
                }
            });
        }
    </script>
</head>
<body>

<c:if test="${show == 0}">
    <h1 align="center">${msgStr}</h1>
</c:if>
<c:if test="${show > 0}">
    <div style="text-align: center;">

        <div>
            <h1>店铺数据整理和上线</h1>
            <p style="color: #1d4dbb; font-size: 20px;">
                <input type="button" class="s_btn_ot" style="float: left;" value="删除类别商品" onclick="deleteCatidGoods('${shopId}')"/>
                <span>整理利润率参考(第1区间):[${wholeRate}]</span>
                <!-- <input type="button" class="s_btn" value="一键原始" title="点击按钮，“重量选择”全部选中原始" onclick="chooseOrWeight()"/>
                &nbsp;&nbsp;&nbsp;&nbsp; -->
                <input type="button" class="s_btn_ot" style="margin-left: 100px;" value="解决重量超差" onclick="showCatidErrorWeightGoods('${shopId}')"/>
                <c:if test="${noSave > 0}">
                    <b style="color: red;font-size: 18px;">(未保存类别平均重量数据)</b>
                </c:if>
            </p>
            <table id="shop_category_id" border="1" cellpadding="1"
                   cellspacing="0" align="center">
                <thead>
                <tr align="center" bgcolor="#DAF3F5" style="height: 50px;">
                    <th style="width: 100px;">
                        全选<input type="checkbox" class="check_sty_all" onclick="chooseAll(this)" />
                    </th>
                    <th style="width: 120px;">类别ID</th>
                    <th style="width: 200px;">类别名称</th>
                    <th style="width: 90px;">商品数量</th>
                    <th style="width: 150px;">重量情况</th>
                    <th style="width: 440px;">设定平均/关键词重量</th>
                    <th style="width: 150px;">第1区间利润率</th>
                    <th style="width: 180px;">建议参考利润率</th>
                    <th style="width: 150px;">第2,3区间利润率</th>
                    <th style="width: 240px;">统计</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${infos}" var="shopInfo" varStatus="status">
                    <tr bgcolor="#FFF7FB" style="height: 42px;">
                        <td>
                            <input type="checkbox" class="check_sty" onclick="chooseBox(this)" value="${shopInfo.categoryId}"/>
                        </td>
                        <td>${shopInfo.categoryId}</td>
                        <td>${shopInfo.categoryName}</td>
                        <td>${shopInfo.goodsNum}</td>
                        <td>${shopInfo.weightInterval}</td>
                        <td>
                            <div style="float:left;">
                                <span style="color:${shopInfo.weightVal > 0 ? 'black':'red'} ">类别平均重量:</span><input
                                    class="inp_sty avg_weight" type="text"
                                    value="${shopInfo.weightVal}"/> <br><br>
                                <div class="tb_div" id="category_key_id_${status.index}">
                                    <span>类别关键词重量:</span><input type="button" class="add_bt" value="添加关键词重量"
                                                                onclick="addKeyWeight('category_key_id_${status.index}')"/>
                                    &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="s_btn_l_tt" value="删除新增"
                                                                   onclick="deleteKeyWeight('category_key_id_${status.index}')"/>
                                    <c:if test="${fn:length(shopInfo.ctwts) > 0}">
                                        <br>
                                        <c:forEach items="${shopInfo.ctwts}" var="ctwt">
                                            <div>
                                                <input type="hidden" value="${ctwt.id}"/> &nbsp;<input
                                                    class="inp_sty_lt" type="text" value="${ctwt.avgWeight}"/>
                                                &nbsp;<input class="inp_sty_md" type="text"
                                                             value="${ctwt.keyword}"/>
                                            </div>
                                        </c:forEach>
                                    </c:if>

                                </div>

                            </div>
                        </td>
                        <td><input class="inp_sty" type="text"
                                   value="${shopInfo.firstIntervalRate > 0 ? shopInfo.firstIntervalRate : 0.30}"/></td>
                        <td>${shopInfo.suggestRate}</td>
                        <td><input class="inp_sty" type="text"
                                   value="${shopInfo.otherIntervalRate > 0 ? shopInfo.otherIntervalRate : 0.30}"/></td>
                        <td><c:if test="${shopInfo.tooLightNum > 0}">
                            <b style="color:red;">太轻(${shopInfo.tooLightNum})</b>&nbsp;&nbsp;
                        </c:if> <c:if test="${shopInfo.tooHeavyNum > 0}">
                            <b style="color:red;">太重(${shopInfo.tooHeavyNum})</b>&nbsp;&nbsp;
                        </c:if> <c:if test="${shopInfo.weightZoneNum > 0}">
                            <b style="color:red;">无重量(${shopInfo.weightZoneNum})</b>
                        </c:if></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <input class="s_btn" type="button" value="保存数据"
                   onclick="saveAndUpdateGoods('${shopId}')"/>
                <%-- <input class="s_btn" type="button" value="确认清洗" onclick="doGoodsClear('${shopId}')" /> --%>
            <c:if test="${dealState == 0}">
                &nbsp;&nbsp;<span style="color: red;">店铺商品数据待处理</span>
            </c:if>

            <c:if test="${dealState == 1}">
                &nbsp;&nbsp;<span style="color: red;">店铺商品数据正在处理，请等待</span>
            </c:if>

            <c:if test="${dealState==2}">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a target="_blank"
                href="/cbtconsole/ShopUrlC/beforeOnlineGoodsShow.do?shopId=${shopId}">查看处理后的店铺商品</a>
            </c:if>
        </div>
        <div>
            <h1>提供利润率参考(已在线上的商品)：</h1>
            <table border="1" cellpadding="0" cellspacing="0" align="center">
                <thead>
                <tr bgcolor="#DAF3F5" style="height: 50px;">
                    <th style="width: 240px;">图片</th>
                    <th style="width: 430px;">商品名称</th>
                    <th style="width: 150px;">速卖通免邮价(美元)</th>
                    <th style="width: 200px;">1688第一区间价(美元)</th>
                    <th style="width: 150px;">重量(KG)</th>
                    <th style="width: 180px;">单件商品预估运费(美元)</th>
                    <th style="width: 160px;">单件商品利润率</th>
                    <!-- <th style="width: 200px;">5件商品平均预估运费(美元)</th> -->
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${profits}" var="gdPft" varStatus="index">
                    <tr bgcolor="#FFF7FB" style="height: 205px;">
                        <td><a target="_blank"
                               href="https://detail.1688.com/offer/${gdPft.pid}.html"><img
                                class="img_sty" src="${gdPft.imgUrl}"/></a></td>
                        <td>${gdPft.goodsName}</td>
                        <td>${gdPft.aliPrice}</td>
                        <td>${gdPft.firstPrice}</td>
                        <td>${gdPft.finalWeight}</td>
                        <td>${gdPft.freight}</td>
                        <td>${gdPft.rate}<em>%</em></td>
                            <%-- <td>${gdPft.freight5Gd}<em>%</em></td> --%>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>


</c:if>


</body>
</html>