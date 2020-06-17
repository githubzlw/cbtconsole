var costList;
$(function () {
    $(".btn-can-not-buy").click(function () {
        var bfid = $("#query_bf_id").val();
        jQuery.ajax({
            url: "/cbtconsole/bf/cancel/order",
            data: {
                "bfid": bfid
            },
            type: "post",
            success: function (data) {
                if (data.state == 200) {
                    window.location.reload();
                } else {
                    $.MsgBox.Alert("提示", "订单取消失败!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "订单取消失败");
            }
        });


    })
    $(".btn-delete-p-all").click(function () {
        var id = $(this).parents(".de-td").find(".bfdid").val();
        jQuery.ajax({
            url: "/cbtconsole/bf/invalidproduct",
            data: {
                "bfdid": id
            },
            type: "post",
            success: function (data) {
                if (data.state == 200) {
                    window.location.reload();
                } else {
                    $.MsgBox.Alert("提示", "删除商品失败!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "删除商品失败");
            }
        });
    })
    $(".btn-update-address").click(function () {
        var id = $("#address_id").val();
        var country = $("#in-country").val();
        var statename = $("#in-state").val();
        var address = $("#in-city").val();
        var street = $("#in-street").val();
        var address2 = $("#in-address").val();
        var phone = $("#in-phone").val();
        var code = $("#in-code").val();
        var recipients = $("#in-recipients").val();
        jQuery.ajax({
            url: "/cbtconsole/bf/address",
            data: {
                "id": id,
                "country": country,
                "statename": statename,
                "address": address,
                "street": street,
                "address2": address2,
                "phone": phone,
                "recipients": recipients,
                "code": code
            },
            type: "post",
            success: function (data) {
                if (data.state == 200) {
                    window.location.reload();
                } else {
                    $.MsgBox.Alert("提示", "更新地址失败!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "更新地址失败");
            }
        });
    })
    $(".btn-address").click(function () {
        $(".disable-in-l").removeAttr("disabled");
        $(".btn-update-address").show();
        $(this).hide();
    })
    getShippingCost(1);
    initXheditor();
    $(".btn-delivery-time").click(function () {
        var isvalid = 1;
        $(".lu-weight").each(function () {
            var luweight = $(this).val();
            if (isvalid == 1 && (luweight == '' || parseFloat(luweight) < 0.00001)) {
                isvalid = 0;
            }
        })
        if (isvalid == 0) {
            $.MsgBox.Alert("提示", "请录入正确的产品重量!");
        }
        var ormnum = $(".ormnum").text();
        var time = $(".delivery-time").val();
        var feight = $(".delivery-feight").val();
        var method = $(".delivery-method").val();
        if (time == '' || time == '' || method == '') {
            $.MsgBox.Alert("提示", "请确认运费、交期是否准确!");
            return;
        }

        var weight = 0;
        $(".lu-weight").each(function () {
            var luweight = $(this).val();
            if (luweight != '' && parseFloat(luweight) > 0.000001) {
                var lucount = parseInt($(this).parents(".de-td").find(".lucount").val());
                if (lucount == 0) {
                    weight = weight + parseFloat(luweight);
                } else {
                    weight = weight + parseFloat(luweight) * lucount;
                }
            }
        });

        if (method.indexOf('CIF') > -1 && (method.indexOf('SEA') > -1 || (method.indexOf('AIR') > -1 && method.length < 5) || method.indexOf('WAY') > -1)) {
            if (parseFloat(weight) < 100) {
                $.MsgBox.Alert("提示", "此运输方式总量大于等于100!");
                return;
            }
        } else if (method.indexOf('Air') > -1) {
            if (parseFloat(weight) < 21) {
                $.MsgBox.Alert("提示", "此运输方式总量大于等于21!");
                return;
            }
        }

        var bfid = $("#query_bf_id").val();
        jQuery.ajax({
            url: "/cbtconsole/bf/time",
            data: {
                "orderNo": ormnum,
                "feight": feight,
                "method": method,
                "time": time,
                "bfid": bfid
            },
            type: "post",
            success: function (data) {
                if (data.state == 200) {
                    $.MsgBox.Alert("提示", "交期确认成功");
                    $("#method-change").val(2);
                    window.location.reload();
                } else {
                    $.MsgBox.Alert("提示", "交期确认失败!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "交期确认失败");
            }
        });
    })
    $(".td-font-view").click(function () {
        var parentsdiv = $(this).parents(".de-td");
        var title = parentsdiv.find(".name-title").text();
        $("#tc_name").html(title);
        var img = parentsdiv.find(".img-de-v").attr("src");
        $(".img-product").attr("src", img);
        var bfdid = parentsdiv.find(".bfdid").val();
        $("#tc_bfdid").val(bfdid);
        var pid = parentsdiv.find(".bfpid").val();
        $("#tc_pid").val(pid);
        var remark = parentsdiv.find(".de-remarl-q").text();
        $("#tc_remark").text(remark);
        var replay = parentsdiv.find(".remark-replay").html();
        $("#remark-replay-content").val(replay);
        getChatList(bfdid);
        $('.tc,.trnasparent,.tc1').show();
    })


    function getChatList(bfdid) {
        jQuery.ajax({
            url: "/cbtconsole/bf/queryBFChatList",
            data: {
                "bd_id": bfdid
            },
            type: "get",
            success: function (data) {
                if (data.ok) {
                    $("#chat_history").empty();
                    var json = data.data;
                    var content = '<div class="link-top"></div><h3>历史交流</h3>';
                    for (var i = 0; i < json.length; i++) {
                        content += '<div class="link-top"></div><p><span class="span_sty">'
                            + json[i].createTime + '</span>:<span>' + json[i].content + '</span></p>';
                    }
                    $("#chat_history").append(content);
                } else {
                    $.MsgBox.Alert("提示", "获取聊天记录失败!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "ajax失败");
            }
        });
    }

    $('.trnasparent').click(function () {
        $('.tc,.trnasparent,.tc1').hide();
    });
    $('.transparent-bg').click(function () {
        $('.transparent,.transparent-bg').hide();
    });
    $(".b-add").hide();
    $(".b-add").click(function () {
        var price = $(this).parents(".de-td").find(".price-ss").val();
        var html = '<tr class="sku-td">' +
            '<td><input type="text" class="input-w8 lu_sku" value=""></td>' +
            '<td class="td-price">USD:<span class="lu-price-buy"></span>(CNY:<input type="text" value="" class="lu-price-buy-c input-w1" onchange="changePrice(this)">)</td>' +
            '<td>' + price + '</td>' +
            '<td>USD:<input type="text" value="" class="lu-price-sale input-w1">(含运费<input type="text" value="" class="lu-ship-feight input-w1">)</td>' +
            '<td><input type="text" class="input-w6 lu_count" value="0"></td>' +
            '<td><input type="text" class="input-w75 lu_unit"></td>' +
            '<td><input type="text" class="input-w5 lu_url">' +
            '&nbsp;<button class="btn btn-success btn-add">录入</button></td>' +
            '</tr>';
        $(this).parents(".detail-div").find(".lu_tr").append(html);
        bindClick();
    })

    $(".btn-finsh").click(function () {
        var valid = 0;
        var time = $(".delivery-time").val();
        var method = $(".delivery-method").val();
        var feight = $(".delivery-feight").val();
        var changeState = $("#method-change").val();
        if (time == '' || feight == '' || !method || method == '') {
            valid = 1;
        } else {
            $(".lu-weight").each(function () {
                var v = $(this).val();
                if (valid == 0 && v == '') {
                    valid = 1;
                }
            });
        }
        if (changeState == '1') {
            $.MsgBox.Alert("提示", "您更改了交期,请先确认交期");
            return;
        }
        if (valid == 1) {
            $.MsgBox.Alert("提示", "请确认交期、快递、运费、重量信息是否准确");
            return;
        }

        var bfid = $("#query_bf_id").val();
        jQuery.ajax({
            url: "/cbtconsole/bf/finsh",
            data: {
                "bfid": bfid
            },
            type: "post",
            success: function (data) {
                if (data.state == 200) {
                    window.location.reload();
                } else {
                    $.MsgBox.Alert("提示", "确认失败!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "确认失败");
            }
        });


    })
    $(".btn-re-n").click(function () {
        var orderNo = $(".ormnum").text();
        var remark = $(".remark-dn").val();
        if (remark == '') {
            return;
        }
        jQuery.ajax({
            url: "/cbtconsole/bf/remark",
            data: {
                "orderNo": orderNo,
                "remark": remark
            },
            type: "post",
            success: function (data) {
                if (data.state == 200) {
                    window.location.reload();
                } else {
                    $.MsgBox.Alert("提示", "回复失败!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "回复失败");
            }
        });


    })
    bindClick();
    $(".btn-update").click(function () {
        var bfid = $("#query_bf_id").val();
        var trp = $(this).parents(".sku-u-td");
        var trdp = $(this).parents(".de-td");
        var bfdid = trdp.find(".bfdid").val();
        var num = trp.find(".lu_count").val();
        var numiid = trdp.find(".td-numiid").text();
        var price = trp.find(".lu-price-sale").val();
        var priceBuy = trp.find(".lu-price-buy").text();
        var priceBuyc = trp.find(".lu-price-buy-c").val();
        var shipFeight = trp.find(".lu-ship-feight").val();
        var url = trp.find(".lu_url").val();
        var unit = trp.find(".lu_unit").val();
        var sku = trp.find(".lu_sku").val();
        var skuid = trp.find(".lu_skuid").val();
        var id = trp.find(".lu_id").val();
        var weight = $(this).parents(".detail-div").find(".lu-weight").val();
        if (num == '' || parseInt(num) < 1 || price == '' || priceBuy == '' || priceBuyc == '' || url == '' || sku == '' || unit == '' || shipFeight == '') {
            $.MsgBox.Alert("提示", "请确认所填信息是否准确!");
            return;
        }
        jQuery.ajax({
            url: "/cbtconsole/bf/add",
            data: {
                "id": id,
                "bfid": bfid,
                "bfdid": bfdid,
                "num": num,
                "numiid": numiid,
                "price": price,
                "priceBuy": priceBuy,
                "priceBuyc": priceBuyc,
                "shipFeight": shipFeight,
                "url": url,
                "weight": weight,
                "unit": unit,
                "sku": sku,
                "skuid": skuid
            },
            type: "post",
            success: function (data) {
                if (data.state == 200) {
                    if (data.message) {
                        $.MsgBox.Alert("提示", data.message);
                        setTimeout(function () {
                            window.location.reload();
                        }, 1500);
                    } else {
                        window.location.reload();
                    }
                } else {
                    $.MsgBox.Alert("提示", "修改规格失败!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "修改规格失败");
            }
        });


    })
    $(".btn-replay").click(function () {
        var bfdid = $("#tc_bfdid").val();
        var pid = $("#tc_pid").val();
        var remark = $("#remark-replay-content").val();
        var orderNo = $("#buy_order_no").text();
        var userId = $("#user_id_rd").text();
        var userEmail = $("#user_email_rd").text();
        jQuery.ajax({
            url: "/cbtconsole/bf/deremark",
            data: {
                "bfdid": bfdid,
                "order_no": orderNo,
                "pid": pid,
                "remark": remark,
                "userId": userId,
                "userEmail": userEmail
            },
            type: "post",
            success: function (data) {
                if (data.state == 200) {
                    window.location.reload();
                } else {
                    $.MsgBox.Alert("提示", "回复失败1!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "回复失败1");
            }
        });


    })
    $(".btn-invalid").click(function () {
        var id = $(this).parents(".sku-u-td").find(".lu_id").val();
        var bfid = $("#query_bf_id").val();
        jQuery.ajax({
            url: "/cbtconsole/bf/invalid",
            data: {
                "id": id,
                "bfId": bfid
            },
            type: "post",
            success: function (data) {
                if (data.state == 200) {
                    $.MsgBox.Alert("提示", "执行成功，请重新刷新运费和确认订单");
                    setTimeout(function () {
                        window.location.reload();
                    }, 3000);
                } else {
                    $.MsgBox.Alert("提示", "规格取消失败!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "规格取消失败");
            }
        });


    })
    $(".btn-weight").click(function () {
        var bfdid = $(this).parents(".de-td").find(".bfdid").val();
        var weight = $(this).parents(".rowweight").find(".lu-weight").val();
        var reg = /(^[-+]?[1-9]\d*(\.\d{1,2})?$)|(^[-+]?[0]{1}(\.\d{1,3})?$)/;
        if (!reg.test(weight)) {
            $.MsgBox.Alert("提示", "重量必须为正数，最多三位小数！");
            return;
        }
        jQuery.ajax({
            url: "/cbtconsole/bf/weight",
            data: {
                "weight": weight,
                "bfdid": bfdid
            },
            type: "post",
            success: function (data) {
                if (data.state == 200) {
                    window.location.reload();
                } else {
                    $.MsgBox.Alert("提示", "重量修改失败!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "重量修改失败");
            }
        });
    })

    $(".delivery-method").change(function () {
        var method = $(this).val();
        for (var i = 0; i < costList.length; i++) {
            if (costList[i].shippingmethod.toLowerCase() == method.toLowerCase()) {
                $(".delivery-feight").val(costList[i].shippingCost);
                $(".delivery-time").val(costList[i].delivery_time);
                $("#method-change").val(1);
                break;
            }
        }

    })

    $('.img-lazy').lazyload({effect: "fadeIn"});
})


function bindClick() {
    $(".btn-add").click(function () {
        var bfid = $("#query_bf_id").val();
        var trp = $(this).parents(".sku-td");
        var trdp = $(this).parents(".de-td");
        var bfdid = trdp.find(".bfdid").val();
        var num = trp.find(".lu_count").val();
        var numiid = trdp.find(".td-numiid").text();
        var price = trp.find(".lu-price-sale").val();
        var priceBuy = trp.find(".lu-price-buy").text();
        var priceBuyc = trp.find(".lu-price-buy-c").val();
        var shipFeight = trp.find(".lu-ship-feight").val();
        var url = trp.find(".lu_url").val();
        var unit = trp.find(".lu_unit").val();
        var sku = trp.find(".lu_sku").val();
        var weight = $(this).parents(".detail-div").find(".lu-weight").val();

        if (num == '' || parseInt(num) < 1 || price == '' || priceBuy == '' || priceBuyc == '' || url == '' || sku == '' || unit == '' || shipFeight == '') {
            $.MsgBox.Alert("提示", "请确认所填信息是否准确!");
            return;
        }

        var reg = /(^[-+]?[1-9]\d*(\.\d{1,2})?$)|(^[-+]?[0]{1}(\.\d{1,3})?$)/;
        if (!reg.test(weight)) {
            $.MsgBox.Alert("提示", "重量必须为正数，最多三位小数！");
            return;
        }
        jQuery.ajax({
            url: "/cbtconsole/bf/add",
            data: {
                "bfid": bfid,
                "bfdid": bfdid,
                "num": num,
                "numiid": numiid,
                "price": price,
                "priceBuy": priceBuy,
                "priceBuyc": priceBuyc,
                "shipFeight": shipFeight,
                "url": url,
                "weight": weight,
                "unit": unit,
                "sku": sku
            },
            type: "post",
            success: function (data) {
                if (data.state == 200) {
                    window.location.reload();
                } else {
                    $.MsgBox.Alert("提示", "录入规格失败!");
                }
            },
            error: function (e) {
                $.MsgBox.Alert("提示", "录入规格失败");
            }
        });


    })
}

function getShippingCost(state) {
    var countryId = $("#in-country-id").val();
    var weight = 0;
    $(".lu-weight").each(function () {
        var luweight = $(this).val();
        if (luweight != '' && parseFloat(luweight) > 0.000001) {
            var lucount = parseInt($(this).parents(".de-td").find(".lucount").val());
            if (lucount == 0) {
                weight = weight + parseFloat(luweight);
            } else {
                weight = weight + parseFloat(luweight) * lucount;
            }
        }
    })
    if (weight < 0.00001) {
        if (state == 1) {

        } else {
            $.MsgBox.Alert("提示", "重量为0获取运费交期失败");
        }
        return;
    }
    var localHost = window.location.href;
    var url = "";
    url = "https://www.import-express.com/shippingCost/getShippingCost";
    /*if(localHost.indexOf("1.9") > -1 || localHost.indexOf("1.27") > -1 || localHost.indexOf("27.115") > -1){
    } else {
        url = "http://192.168.1.66:8087/shippingCost/getShippingCost";
    }*/
    jQuery.ajax({
        url: url,
        data: {
            "countryId": countryId,
            "free": 0,
            "weight": weight
        },
        type: "get",
        success: function (res) {
            if (res.code == 200) {
                var gmethod = $("#h-delivery-method").val();
                costList = res.data.transitPricecostList;
                var method = '';
                for (var i = 0; i < costList.length; i++) {
                    var shippingMethod = costList[i].shippingmethod;
                    if (gmethod != '' && shippingMethod.toLowerCase() == gmethod.toLowerCase()) {
                        // method +='<option selected="selected" value="'+costList[i].shippingmethod+'">'+costList[i].shippingmethod+'</option>';

                        if (shippingMethod.indexOf('CIF') > -1 && (shippingMethod.indexOf('SEA') > -1 || (shippingMethod.indexOf('AIR') > -1 && shippingMethod.length < 5) || shippingMethod.indexOf('WAY') > -1)) {

                            if (parseFloat(weight) < 100) {
                                method += '<option selected="selected" value="' + costList[i].shippingmethod + '">' + costList[i].shippingmethod + '(重量大于100)</option>';
                            }
                        } else if (shippingMethod.indexOf('Air') > -1) {
                            method += '<option selected="selected" value="' + costList[i].shippingmethod + '">' + costList[i].shippingmethod + '(重量大于21)</option>';
                        } else {
                            method += '<option selected="selected" value="' + costList[i].shippingmethod + '">' + costList[i].shippingmethod + '</option>';
                        }
                    } else {
                        if (shippingMethod.indexOf('CIF') > -1 && (shippingMethod.indexOf('SEA') > -1 || (shippingMethod.indexOf('AIR') > -1 && shippingMethod.length < 5) || shippingMethod.indexOf('WAY') > -1)) {

                            if (parseFloat(weight) < 100) {
                                method += '<option value="' + costList[i].shippingmethod + '">' + costList[i].shippingmethod + '(重量大于100)</option>';
                            }
                        } else if (shippingMethod.indexOf('Air') > -1) {
                            method += '<option value="' + costList[i].shippingmethod + '">' + costList[i].shippingmethod + '(重量大于21)</option>';
                        } else {
                            method += '<option value="' + costList[i].shippingmethod + '">' + costList[i].shippingmethod + '</option>';
                        }
                        //method +='<option value="'+costList[i].shippingmethod+'">'+costList[i].shippingmethod+'</option>';
                    }
                }
                $(".delivery-method").html(method);
            } else {
                $.MsgBox.Alert("提示", "获取运费交期失败!");
            }
        },
        error: function (e) {
            $.MsgBox.Alert("提示", "获取运费交期失败");
        }
    });
}


//初始化xheditor
function initXheditor() {
    var orderNo = $(".ormnum").text();
    editorObj = $('#remark-replay-content').xheditor({
        tools: "full",
        html5Upload: false,
        upBtnText: "上传",
        upMultiple: 1,
        upImgUrl: "/cbtconsole/bf/xheditorUploads?orderNo=" + orderNo,
        upImgExt: "jpg,jpeg,gif,png"
    });
}

function changePrice(t) {
    var cny = parseFloat($(t).val());
    if (!cny || cny < 0.001) {
        $(t).val('');
        return;
    }
    var price = parseFloat(cny / 7.0832).toFixed(2);
    $(t).parents(".td-price").find(".lu-price-buy").text(price);

}

