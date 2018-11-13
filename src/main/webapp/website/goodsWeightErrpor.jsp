<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>重量超差列表</title>
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
        .div_sty {
            width: 370px;
        }

        .img_sty {
            max-width: 180px;
            max-height: 180px;
        }

        .check_sty {
            height: 24px;
            float: left;
            width: 24px;
        }

        .check_sty_l {
            height: 20px;
            width: 20px;
        }

        .checkBg {
            background-color: #b6f5b6;
        }

        .inp_sty {
            height: 23px;
            width: 80px;
        }

        .del_btn {
            float: left;
            border-radius: 10px;
            height: 40px;
            width: 80px;
            margin-left: 40px;
            border-color: #2e6da4;
            color: white;
            background-color: red;
        }

        .btn_ot_sty {
            float: left;
            border-radius: 10px;
            height: 40px;
            width: 140px;
            margin-left: 40px;
            border-color: #2e6da4;
            color: white;
            background-color: #169bd4;
        }

        .btn_add_sty {
            border-radius: 10px;
            height: 34px;
            width: 140px;
            margin-left: 40px;
            border-color: #2e6da4;
            color: white;
            background-color: #169bd4;
        }

        .inp_sty_lt {
            height: 20px;
            width: 45px;
        }

        .inp_sty_md {
            height: 20px;
            width: 240px;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            closeDialog();
        });

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

        function chooseWeightBox(obj, weight) {
            if (weight > 0) {
                var is = $(obj).is(':checked');
                if (is) {
                    $(obj).addClass("isChoose");
                    $(obj).parent().parent().addClass("checkBg");
                } else {
                    $(obj).removeClass("isChoose");
                    $(obj).parent().parent().removeClass("checkBg");
                }

                $(obj).parent().parent().find(".inp_sty").val(weight);
            } else {
                $(obj).removeClass("isChoose");
                $(obj).parent().parent().removeClass("checkBg");
                var is = $(obj).is(':checked');
                if (is) {
                    $(obj).removeAttr("checked");
                }
                return false;
            }
        }

        function deleteShopOfferGoods(shopId) {
            $.messager.confirm('系统提醒', '是否删除，删除保存后数据不可恢复', function (r) {
                if (r) {
                    var pids = "";
                    $(".isChoose").each(function () {
                        var checkVal = $(this).val();
                        pids += "," + checkVal;
                    });
                    if (pids == "") {
                        $.messager.alert("提醒", "请选择需要删除的商品", "info");
                    } else {
                        $.ajax({
                            type: 'POST',
                            dataType: 'json',
                            url: '/cbtconsole/ShopUrlC/deleteShopOfferGoods.do',
                            data: {
                                shopId: shopId,
                                pids: pids.substring(1)
                            },
                            success: function (data) {
                                if (data.ok) {
                                    $.messager.alert("提醒", "执行成功", "info");
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
            });
        }

        function saveShowDealShopGoods(shopId, total) {
            if (total > 0) {
                var isSave = true;
                var json = [];
                var errContent = {};
                $("#shop_goods_error").find(".div_sty").each(function () {
                    var tempPam = {};
                    var catid = $(this).find(".catid_sty").val();
                    tempPam["catid"] = catid;
                    var pid = $(this).find(".pid_sty").val();
                    tempPam["pid"] = pid;
                    var divOj = $(this).find("div").eq(2);
                    var setWeight = divOj.find(".inp_sty").val();
                    var checkOj = divOj.find(".check_sty_l");
                    if ((setWeight == null || setWeight == "" || setWeight == "0" || setWeight == "0.0") && !(checkOj.is(':checked'))) {
                        $.messager.alert("提醒", "存在未设置的重量或者未标记特例", "info");
                        isSave = false;
                        return false;
                    } else {
                        var curWeight = divOj.find(".cur_weight").val();
                        if (checkOj.is(':checked')) {
                            if (curWeight == 0) {
                                if (catid in errContent) {
                                    errContent[catid] += 1;
                                } else {
                                    errContent[catid] = 1;
                                }
                            }
                            tempPam["weightFlag"] = 1;
                            tempPam["setWeight"] = 0;
                        } else if (!(setWeight == null || setWeight == "" || setWeight == "0" || setWeight == "0.0")) {
                            var weightFlag = divOj.find(".weight_flag").val();
                            if (weightFlag == 1 && curWeight == 0) {
                                tempPam["weightFlag"] = 5;
                            } else {
                                tempPam["weightFlag"] = weightFlag;
                            }
                            tempPam["setWeight"] = setWeight;
                        }
                    }
                    json[json.length] = tempPam;
                });
                var count = Object.keys(errContent).length;
                if (count > 0) {
                    var errDesc = "";
                    for (var keyV in errContent) {
                        errDesc += ";" + keyV + " : " + errContent[keyV] + "个";
                    }
                    $("#error_desc").empty();
                    $("#error_desc").append("存在重量为0商品的未设置重量[" + errDesc.substring(1) + "]");
                    $("#error_desc").show();
                } else if (isSave) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: '/cbtconsole/ShopUrlC/saveDealErrorShopGoods',
                        data: {
                            shopId: shopId,
                            infos: JSON.stringify(json)
                        },
                        success: function (data) {
                            if (data.ok) {
                                $.messager.alert("提醒", "执行成功,页面即将跳转", "info");
                                setTimeout(function () {
                                    window.location.href = "/cbtconsole/ShopUrlC/showHasDealShopGoods?shopId=" + shopId;
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
            } else {
                $.messager.alert("提醒", "执行成功,页面即将跳转", "info");
                setTimeout(function () {
                    window.location.href = "/cbtconsole/ShopUrlC/showHasDealShopGoods?shopId=" + shopId;
                }, 500);
            }
        }

        function doSave() {
            var noKeyWeight = false;
            var shopId = $("#add_shop_id").val();
            var catid = $("#add_shop_catid").val();
            if (shopId == null || shopId == "" || shopId == "0") {
                $.messager.alert("提醒", "获取店铺ID失败，请刷新页面重试", "info");
                noKeyWeight = true;
            }
            if (catid == null || catid == "" || catid == "0") {
                $.messager.alert("提醒", "获取类别ID失败，请刷新页面重试", "info");
                noKeyWeight = true;
            }
            var avgWeight = "";
            var inpArr = $("#form_enter").find("div").find("input");
            var tempWeight = inpArr.eq(0).val();
            var tempKeyword = inpArr.eq(1).val();
            if (!(tempWeight == null || tempWeight == "")
                && (tempKeyword == null || tempKeyword == "")) {
                noKeyWeight = true;
                $.messager.alert("提醒", "获取关键词失败，请确认数据输入正常", "info");
                return false;
            }
            if ((tempWeight == null || tempWeight == "" || tempWeight == "0" || tempWeight == "0.0")
                && !(tempKeyword == null || tempKeyword == "")) {
                noKeyWeight = true;
                $.messager.alert("提醒", "获取重量失败，请确认数据输入正常", "info");
                return false;
            }
            if (!noKeyWeight) {
                $.ajax({
                    type: "POST",
                    url: "/cbtconsole/ShopUrlC/saveKeyWordWeight",
                    data: {
                        shopId: shopId,
                        catid: catid,
                        avgWeight: tempWeight,
                        keyword: tempKeyword
                    },
                    success: function (data) {
                        if (data.ok) {
                            closeDialog();
                            showMessage('数据保存成功');
                            setGoodsWeight(tempWeight, tempKeyword, catid);
                        } else {
                            $.messager.alert("提醒", '执行错误:' + data.message, "info");
                        }
                    },
                    error: function (res) {
                        $.messager.alert("提醒", '保存错误，请联系管理员', "error");
                    }
                });
            }
        }

        function addKeyWordWeight(shopId, catid) {
            $("#add_shop_id").val(shopId);
            $("#add_shop_catid").val(catid);
            $('#enter_div_sty').dialog('open');
        }

        function chooseCurrenWeight(catid) {
            $(".er_" + catid).empty();
            $(".er_" + catid).hide();
            var count = 0;
            $(".ct_" + catid).each(function () {
                var lastDiv = $(this).find("div:last");
                var curWeight = lastDiv.find(".cur_weight").val();
                if (curWeight == 0) {
                    count++;
                } else {
                    lastDiv.find(".inp_sty").val(curWeight);
                    var checkObj = lastDiv.find(".check_sty_l");
                    checkObj.addClass("isChoose");
                    checkObj.prop("checked", "checked");
                    lastDiv.addClass("checkBg");
                }
            });
            if (count > 0) {
                $(".er_" + catid).append("此类别下存在重量为0的商品" + count + "个，请检查");
                $(".er_" + catid).show();
            }
        }

        function closeDialog() {
            $('#enter_div_sty').dialog('close');
            $("#add_shop_id").val("");
            $("#add_shop_catid").val("");
            $("#form_enter")[0].reset();
        }

        function setGoodsWeight(weight, keyword, catid) {
            $(".ct_" + catid).each(function () {
                var goodsName = $(this).find(".goods_name").text();
                if (goodsName.indexOf(keyword) > -1) {
                    $(this).find(".inp_sty").val(weight);
                }
            });
        }

        function showMessage(msgStr) {
            $.messager.show({
                title: '提醒',
                msg: msgStr,
                timeout: 1500,
                showType: 'slide',
                style: {
                    right: '',
                    top: ($(window).height() * 0.35),
                    bottom: ''
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

    <div id="enter_div_sty" class="easyui-dialog" title="添加类别关键词重量"
         data-options="modal:true"
         style="width: 388px; height: 320px;">
        <form id="form_enter" action="#" onsubmit="return false" style="margin-left: 30px;">
            <br> 店铺ID:<input id="add_shop_id" type="text" value=""
                             readonly="readonly"/><br>
            <br> 类别ID:<input id="add_shop_catid" type="text" value=""
                             readonly="readonly"/> <br>
            <br>
            <h3>填写关键词重量</h3>
            <div>
                <input class="inp_sty_lt" type="text" value="" placeholder="重量"/>
                &nbsp;<input class="inp_sty_md" type="text" value=""
                             placeholder="关键词"/>
            </div>
            <br> <br>
            <div style="text-align: center;">
                <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
                   class="easyui-linkbutton" onclick="doSave()" style="width: 80px">生成</a>
                <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
                   class="easyui-linkbutton" onclick="closeDialog()"
                   style="width: 80px">取消</a>
            </div>

        </form>
    </div>

    <div style="text-align: center;">
        <h1>
            重量超差列表(<span style="color: red;">强建议添加重量关键词后再强制改重量</span>)
        </h1>
        <br>
        <div style="float: left;">
            <h2 style="float: left;">
                重量异常商品(<span style="color: red;">总数：${errorTotal}</span>)
            </h2>
            <input class="del_btn" type="button" value="删除商品"
                   onclick="deleteShopOfferGoods('${shopId}')"/> &nbsp;&nbsp;&nbsp;&nbsp;
            <input class="btn_ot_sty" type="button" value="执行改重量操作"
                   onclick="saveShowDealShopGoods('${shopId}',${errorTotal})">
            &nbsp;&nbsp;<span id="error_desc" style="color:red;display:none;font-size: 22px;float: left;"></span>
            <br>
            <table id="shop_goods_error" border="1" cellpadding="1"
                   cellspacing="0" align="left" width="100%">
                <tbody>
                <c:forEach items="${errorList}" var="erCid" varStatus="index">

                    <tr align="left" bgcolor="#DAF3F5" style="height: 50px;width:100%;">
                        <td colspan="5" style="width:100%;"><span>类别：${erCid.categoryId}【${erCid.categoryName}】</span>
                                <%-- &nbsp;&nbsp;&nbsp;&nbsp;<span>设定平均重量：${erCid.weightVal}<em>KG</em></span> --%>
                            &nbsp;&nbsp;&nbsp;&nbsp; <input class="btn_add_sty"
                                                            type="button" value="添加类别关键词重量"
                                                            onclick="addKeyWordWeight('${shopId}','${erCid.categoryId}')"/>
                            &nbsp;&nbsp;&nbsp;&nbsp; <input class="btn_add_sty"
                                                            type="button" value="一键选择就用现重"
                                                            onclick="chooseCurrenWeight('${erCid.categoryId}')"/>
                            &nbsp;&nbsp;&nbsp;&nbsp; <span class="er_${erCid.categoryId}"
                                                           style="color:red;dispaly:none;"></span></td>
                    </tr>

                    <c:set var="total" value="${erCid.totalNum}"/>
                    <c:set var="count" value="0"/>
                    <c:forEach items="${erCid.gdOfLs}" var="goods" varStatus="status">
                        <c:set var="count" value="${count + 1}"/>
                        <c:if test="${count == 1}">
                            <tr align="left" style="width:100%;">
                        </c:if>
                        <td style="width:20%;">
                            <div class="div_sty ct_${erCid.categoryId}">
                                <input type="hidden" class="catid_sty" value="${erCid.categoryId}"/>
                                <input type="hidden" class="pid_sty" value="${goods.pid}"/>
                                <input type="checkbox" class="check_sty"
                                       onclick="chooseBox(this)" value="${goods.pid}"/>
                                <div>
                                    <a target="_blank"
                                       href="https://detail.1688.com/offer/${goods.pid}.html"><img
                                            class="img_sty" src="${goods.imgUrl}"/></a>
                                    <br>单个价格:<span>${goods.price}</span><span>$</span>
                                    <br>名称:<span class="goods_name">${goods.goodsName}</span>
                                </div>
                                <div style="margin-bottom: 2px;">
                                    <span style="color:${goods.weightFlag == 0 ? 'green':'red'};">重量异常标识:[${goods.weightFlag == 0 ? '正常' : goods.weightFlagDescribe}]</span>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span
                                        style="color:${goods.weightDeal == 1 ? 'green':'red'};">处理标识:[${goods.weightDeal == 1 ? '已处理' : '未处理'}]</span>
                                    <br>
                                    <c:if test="${goods.weight == 0}">
                                        <b style="color:red;">现重量：${goods.weight}<em>KG</em></b>

                                    </c:if>
                                    <c:if test="${goods.weight > 0}">
                                        <span style="color:black;">现重量：${goods.weight}<em>KG</em></span>
                                    </c:if>
                                    <c:if test="${goods.detailNum == 0}">
                                        <br>
                                        <b style="color:#f50ed8;">(此商品无详情)</b>
                                    </c:if>

                                </div>

                                <div style="margin-bottom: 2px;">
                                    <input type="hidden" class="cur_weight" value="${goods.weight}"/>
                                    <span>准备改成的重量：<input
                                            type="text" class="inp_sty" value="${goods.setWeight}"/><em>KG</em></span>
                                    &nbsp;&nbsp;<span>
											<input
                                                    type="hidden" class="weight_flag" value="${goods.weightFlag}"/>
											<input
                                                    type="checkbox"
                                                    class="check_sty_l"  ${goods.weightFlag == 1 ? 'checked="checked"' : ''}
                                                ${goods.weight == 0 ? 'disabled="disabled"' : ''}
                                                    onclick="chooseWeightBox(this,${goods.weight})"
                                                    value="${goods.pid}"/>就用现重量</span>

                                </div>
                            </div>

                        </td>
                        <c:if test="${count % 5 == 0}">
                            </tr>
                            <tr align="left" style="width:100%;">
                        </c:if>
                    </c:forEach>
                    <c:if test="${total % 4 > 0}">
                        </tr>
                    </c:if>


                </c:forEach>

                </tbody>
            </table>
        </div>
        <br>
        <div style="float: left;">
            <h2 style="float: left;">
                重量正常商品(<span style="color: red;">总数：${nomalTotal}</span>)
            </h2>
            <br>
            <table id="shop_goods_nomal" border="1" cellpadding="1"
                   cellspacing="0" align="center" width="100%">
                <tbody>
                <c:forEach items="${nomalList}" var="nmCid" varStatus="index">

                    <tr align="left" bgcolor="#DAF3F5" style="height: 50px;">
                        <td colspan="5" style="width:100%;"><span>类别：${nmCid.categoryId}【${nmCid.categoryName}】</span>
                                <%-- &nbsp;&nbsp;&nbsp;&nbsp;<span>设定平均重量：${nmCid.weightVal}<em>KG</em></span> --%></td>
                    </tr>

                    <c:set var="total" value="${nmCid.totalNum}"/>
                    <c:set var="count" value="0"/>
                    <c:forEach items="${nmCid.gdOfLs}" var="goods" varStatus="status">
                        <c:set var="count" value="${count + 1}"/>
                        <c:if test="${count == 1}">
                            <tr align="left" style="width:100%;">
                        </c:if>
                        <td style="width:20%;">
                            <div class="div_sty">
                                <input type="checkbox" class="check_sty"
                                       onclick="chooseBox(this)" value="${goods.pid}"/>
                                <div>
                                    <a target="_blank"
                                       href="https://detail.1688.com/offer/${goods.pid}.html"><img
                                            class="img_sty" src="${goods.imgUrl}"/></a>
                                    <br>单个价格:<span>${goods.price}</span><span>$</span>
                                    <br>名称:<span>${goods.goodsName}</span>
                                </div>
                                <div style="margin-bottom: 2px;">
                                    <span style="color:${goods.weightFlag == 0 ? 'green':'red'};">重量异常标识:${goods.weightFlag == 0 ? '正常' : goods.weightFlagDescribe}</span>
                                        <%-- <br> <span style="color:${goods.weightDeal == 1 ? 'green':'red'};">处理标识:${goods.weightDeal == 1 ? '已处理' : '未处理'}</span> --%>
                                </div>
                                <div style="margin-bottom: 2px;">
                                    <span>现重量：${goods.weight}<em>KG</em></span>
                                    <c:if test="${goods.detailNum == 0}">
                                        <br>
                                        <b style="color:#f50ed8;">(此商品无详情)</b>
                                    </c:if>
                                </div>
                            </div>

                        </td>
                        <c:if test="${count % 5 == 0}">
                            </tr>
                            <tr align="left" style="width:100%;">
                        </c:if>
                    </c:forEach>
                    <c:if test="${total % 4 > 0}">
                        </tr>
                    </c:if>
                </c:forEach>

                </tbody>
            </table>
        </div>
    </div>


</c:if>


</body>
</html>