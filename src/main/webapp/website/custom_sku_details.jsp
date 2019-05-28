<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/css/custom_goods_details.css"/>
    <title>产品SKU重量修改</title>
    <style type="text/css">
        .mask {
            display: none;
            position: absolute;
            left: 0;
            top: 0;
            bottom: 0;
            right: 0;
            margin: auto;
            background: rgba(0, 0, 0, 0.6);
            width: 360px;
            height: 60px;
            line-height: 60px;
            text-align: center;
            border-radius: 10px;
            font-size: 20px;
            color: #fff;
            z-index: 100;
        }

        .btn {
            width: 110px;
            height: 24px;
            color: white;
            background-color: darkorange;
        }

        table td {
            height: 32px;
        }
    </style>
</head>
<body onload="genTypeNum()">

<c:if test="${success == 0}">
    <b style="font-size: 18px;text-align: center;">${message}</b>
</c:if>

<c:if test="${success > 0}">
    <div class="mask"></div>
    <div style="margin-left: 120px;">

        <h1>产品SKU重量修改(体积重量默认显示重量数据)</h1>
        <table border="1" cellspacing="0" cellpadding="0" bgcolor="#f5e9cf">
                <%--<caption><span>PID:${pid}</span></caption>--%>
            <thead style="text-align: center;">
            <tr>
                <c:forEach var="type_name" items="${typeNames}"
                           varStatus="nameIndex">
                    <td id="type_name_${type_name.key}">${type_name.value}</td>
                </c:forEach>
                <td>单价</td>
                <td>重量(KG)</td>
                <td>体积重量(KG)</td>
            </tr>
            </thead>

            <tbody id="sku_body">
            <c:forEach var="sku_bean" items="${showSku}" varStatus="skuIndex">
                <tr>
                    <c:forEach var="tp_ar" items="${fn:split(sku_bean.skuAttrs,';')}">
                        <td id="combine_id_${fn:split(tp_ar,'@')[0]}"
                            style="width: 100px;">${fn:split(tp_ar,'@')[2]}</td>
                    </c:forEach>
                    <td style="width: 100px;text-align: center;"><span>${sku_bean.price}</span></td>
                    <td style="width: 100px;">
                        <input class="inp_style inp_cmn_price" title="单击可进行编辑" data-id="${sku_bean.ppIds}"
                               value="${sku_bean.fianlWeight}"/></td>
                    <td style="width: 100px;">
                        <input class="inp_style inp_vlm_price" title="单击可进行编辑" data-id="${sku_bean.ppIds}"
                               value="${sku_bean.volumeWeight}"/></td>
                </tr>
            </c:forEach>
            </tbody>

        </table>


        <br>
        <table border="1" cellspacing="0" cellpadding="0" bgcolor="#94f1dc">
            <caption>操作</caption>
            <tr>
                <td>重量修改</td>
                <td style="width: 140px;"><input class="btn" type="button" value="全部相同"
                                                 onclick="updatePidWeight(1, 1,this)"/>
                </td>
                <td style="width: 140px;"><input class="btn" type="button" value="区间(从小到大)"
                                                 onclick="updatePidWeight(1, 2,this)"/>
                </td>
                <td style="width: 140px;"><input class="btn" type="button" value="区间(从大到小)"
                                                 onclick="updatePidWeight(1, 3,this)"/>
                </td>

            </tr>

            <tr>
                <td>体积重量修改</td>
                <td style="width: 140px;"><input class="btn" type="button" value="全部相同"
                                                 onclick="updatePidWeight(2, 1,this)"/>
                </td>
                <td style="width: 140px;"><input class="btn" type="button" value="区间(从小到大)"
                                                 onclick="updatePidWeight(2, 2,this)"/>
                </td>
                <td style="width: 140px;"><input class="btn" type="button" value="区间(从大到小)"
                                                 onclick="updatePidWeight(2, 3,this)"/>
                </td>
            </tr>

            <tr style="text-align: center;">
                <td colspan="4"><input class="btn" type="button" value="保存" onclick="doSaveSkuWeight('${pid}')"/></td>
            </tr>

        </table>

    </div>
</c:if>


</body>
<script type="text/javascript">

    var typeObj = {};
    var maxVal = 1;

    function genTypeNum() {
        // 循环遍历table,判断最大规格数据
        $("#sku_body").find("tr").each(function () {
            var tdVal = $(this).find("td").eq(0).text();
            var noBlankVal = tdVal.replace(/\s+/g,"");
            if (typeObj.hasOwnProperty(noBlankVal)) {
                typeObj[noBlankVal] = typeObj[noBlankVal] + 1;
            } else {
                typeObj[noBlankVal] = 1;
            }
            $(this).find("td").eq(3).find("input").addClass("com_" + noBlankVal);
            $(this).find("td:last").find("input").addClass("vlm_" + noBlankVal);
        });
        // 取最大值

        for (var keyV in typeObj) {
            if (maxVal < typeObj[keyV]) {
                maxVal = typeObj[keyV];
            }
        }
    }


    function updatePidWeight(type, flag, obj) {
        if (flag == 1) {
            allSamePrice(type, obj);
        } else if (flag == 2) {
            intervalPidWeight(type, flag, obj);
        } else if (flag == 3) {
            intervalPidWeight(type, flag, obj);
        }
    }

    function doSaveSkuWeight(pid) {
        var reg = /(^[-+]?[1-9]\d*(\.\d{1,3})?$)|(^[-+]?[0]{1}(\.\d{1,3})?$)/;
        var singSkus = "";
        var singVlmSkus = "";
        var isNotErr = true;
        $(".inp_cmn_price").each(function () {
            var ppid = $(this).attr("data-id");
            var pweight = $(this).val();
            if (ppid == null || ppid == "") {
                showMessage("单规格ID获取失败");
                isNotErr = false;
                return false;
            } else if (!reg.test(pweight)) {
                showMessage("单规格重量异常");
                isNotErr = false;
                return false;
            } else {
                singSkus += ";" + ppid + "@" + pweight;
            }
        });
        $(".inp_vlm_price").each(function () {
            var ppid = $(this).attr("data-id");
            var pweight = $(this).val();
            if (ppid == null || ppid == "") {
                showMessage("单规格ID获取失败");
                isNotErr = false;
                return false;
            } else if (!reg.test(pweight)) {
                showMessage("单规格重量异常");
                isNotErr = false;
                return false;
            } else {
                singVlmSkus += ";" + ppid + "@" + pweight;
            }
        });
        if (isNotErr) {
            showMessage("正在执行，请等待...");
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '/cbtconsole/editc/saveSkuInfo',
                data: {
                    "pid": pid,
                    "sku": singSkus.substring(1),
                    "volumeSku": singVlmSkus.substring(1)
                },
                success: function (data) {
                    $('.mask').hide();
                    showMessage(data.message);
                    if (data.ok) {
                        window.location.reload();
                    }
                },
                error: function (XMLResponse) {
                    $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                }
            });
        }
    }

    function intervalPidWeight(type, flag, obj) {
        $(obj).css("background-color", "#aba297");

        $.messager.prompt('提示', '请输入重量(KG)，从小到大，用“-”分割:', function (is) {
            if (is) {
                if (is.indexOf("-") > -1) {
                    var weightList = is.split("-");

                    var reg = /(^[-+]?[1-9]\d*(\.\d{1,3})?$)|(^[-+]?[0]{1}(\.\d{1,3})?$)/;
                    if (!reg.test(weightList[0]) || !reg.test(weightList[1])) {
                        showMessage('重量必须为正数，最多三位小数！');
                    } else {
                        var smallW = parseFloat(weightList[0]);
                        var bigW = parseFloat(weightList[1]);
                        var avgW = parseFloat((bigW - smallW) / (maxVal - 1));
                        var totalW = 0;
                        if (flag == 2) {
                            for (var keyV in typeObj) {
                                totalW = parseFloat(smallW);
                                var tempVal = totalW;
                                if (type == 1) {
                                    $(".com_" + keyV).each(function () {
                                        $(this).val(tempVal.toFixed(3));
                                        tempVal = tempVal + avgW;
                                    });
                                } else {
                                    $(".vlm_" + keyV).each(function () {
                                        $(this).val(tempVal.toFixed(3));
                                        tempVal = tempVal + avgW;
                                    });
                                }
                                totalW = tempVal;

                            }
                        } else {
                            for (var keyV in typeObj) {
                                totalW = bigW;
                                var tempVal = totalW;
                                if (type == 1) {
                                    $(".com_" + keyV).each(function () {
                                        $(this).val(tempVal.toFixed(3));

                                    });
                                } else {
                                    $(".vlm_" + keyV).each(function () {
                                        $(this).val(tempVal.toFixed(3));
                                        tempVal = tempVal - avgW;
                                    });
                                }
                                totalW = tempVal;
                            }
                        }
                    }
                } else {
                    showMessage('重量输入不合法');
                }
                $(obj).css("background-color", "darkorange");
            } else {
                showMessage('未输入重量或取消输入！');
                $(obj).css("background-color", "darkorange");
            }
        });
    }

    function allSamePrice(type, obj) {
        $(obj).css("background-color", "#aba297");
        $.messager.prompt('提示', '请输入重量(KG):', function (is) {
            if (is) {
                var reg = /(^[-+]?[1-9]\d*(\.\d{1,3})?$)|(^[-+]?[0]{1}(\.\d{1,3})?$)/;
                if (!reg.test(is)) {
                    showMessage('重量必须为正数，最多两位小数！');
                } else {
                    if (type == 1) {
                        $(".inp_cmn_price").val(is);
                    } else {
                        $(".inp_vlm_price").val(is);
                    }
                }
                $(obj).css("background-color", "darkorange");
            } else {
                $(obj).css("background-color", "darkorange");
            }
        });
    }

    $(".inp_style").click(function () {
        $(this).removeAttr("readonly");
        $(this).css("background-color", "rgb(255, 255, 255)");
    });

    $(".inp_style").blur(function () {
        $(this).attr("readonly", true);
        $(this).css("background-color", "#d8d8d8");
    });

    function showMessage(msg) {
        $('.mask').show().text(msg);
        setTimeout(function () {
            $('.mask').hide();
        }, 1500);
    }
</script>
</html>
