<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
    response.flushBuffer();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>产品详情编辑</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/xheditor-1.2.2.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/xheditor_lang/zh-cn.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/js/xheditor_skin/vista/iframe.css"/>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/css/custom_goods_details.css"/>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <script type="text/javascript">
        var imgJson = {};
        var tempJson = {};
        var typeJson = {};
        var editorObj = {};
        var skuJson = {};
        var skuInfo = {};

        $(document).ready(function () {
            initDialog();
            closeUploadDialog();
            initXheditor();
            imageMagnifying();
            imageSwitchingDisplay();
            aliImgToExpress();
            closeSimilarGoodsDialog();
            closeKeyWordDialog();
            closeBenchmarking();
            closeSizeInfoEnDialog();
            $('#review_dlg').dialog('close');
            $('#update_review_dlg').dialog('close');
            closeNewAliPidDlg('close');
            closeMultiFileDialog();
            closeGoodsDescDialog();
            closeOverSeaDialog();
            $('#catid-dlg').dialog('close');
            createComboTree();
        });

        function relieveDisabled(obj) {
            $(obj).removeAttr("readonly");
            $(obj).css("background-color", "rgb(255, 255, 255)");
        }

        function addDisabled(obj) {
            $(obj).attr("readonly", true);
            $(obj).css("background-color", "#d8d8d8");
        }

        //初始化xheditor
        function initXheditor() {
            editorObj = $('#goods_content').xheditor({
                tools: "full",
                html5Upload: false,
                upBtnText: "上传",
                upMultiple: 1,
                upImgUrl: "/cbtconsole/editc/xheditorUploads?pid=${goods.pid}",
                upImgExt: "jpg,jpeg,gif,png"
            });
        }

        //放大镜
        function imageMagnifying() {

            var mengban = $('.mengban');
            var lvjing = $('.lvjing');
            var big_pic = $('.big_pic');
            var big_img = $('.big_img');
            mengban.hover(function () {
                lvjing.show();
                big_pic.show();
            }, function () {
                lvjing.hide();
                big_pic.hide();
            })
            mengban.on("mousemove", function (evt) {
                var e = evt || window.event;
                var x = e.offsetX - lvjing.width() / 2;
                var y = e.offsetY - lvjing.height() / 2;
                var cols = mengban.width() / lvjing.width();
                if (x < 0) {
                    x = 0;
                } else if (x > mengban.width() - lvjing.width()) {
                    x = mengban.width() - lvjing.width();
                }
                if (y < 0) {
                    y = 0;
                } else if (y > mengban.height() - lvjing.height()) {
                    y = mengban.height() - lvjing.height();
                }
                lvjing.css({
                    left: x + 'px',
                    top: y + 'px'
                });
                big_img.css({
                    left: -cols * x + 'px',
                    top: -cols * y + 'px'
                });
            })
        }

        function imageSwitchingDisplay() {
            roastingImg();
            //删除橱窗图
            $('#delete_pic').click(function () {
                $.messager.confirm('系统提醒', '是否删除，删除保存后数据不可恢复', function (r) {
                    if (r) {
                        $('.ul_pic li').each(function (i) {
                            if ($('.ul_pic li').eq(i).hasClass('red_border')) {
                                $(this).remove();
                                $('.ul_pic li').eq(0).trigger('click');
                            }
                            if (!$('.ul_pic li').length) {
                                var mainImg = $("#goods_main_img").val();
                                $('.init_img').attr("src", mainImg);
                            }
                            roastingImg();
                        });
                    }
                });
            });

            //点击尺码图
            $('.ul_color').on(
                'click',
                'li',
                function () {
                    $(this).addClass('red_border').siblings().removeClass(
                        'red_border');
                    if ($(this).hasClass('red_border')) {
                        var img_src = $(this).find('img').attr('src');
                        img_src = img_src.replace("_50x50.jpg", "").replace(
                            "._SS47_.jpg", ".jpg").replace("50x50",
                            "200X200").replace("32x32", "400x400").replace(
                            "60x60", "400x400");
                        $('.init_img').attr('src', img_src);
                        $('.big_img').attr('src', img_src);
                    }
                });
            //点击尺码图
            $('.ul_size').on(
                'click',
                'li',
                function () {
                    $(this).addClass('red_border').siblings().removeClass(
                        'red_border');
                })
        }

        //轮播图片
        function roastingImg() {
            //点击小图对应切换图片控制
            $('.ul_pic li').each(
                function (i) {
                    $(this).click(
                        function () {
                            var img_src = $(this).find('img').attr('src');
                            $(this).addClass('red_border').siblings()
                                .removeClass('red_border');
                            img_src = img_src.replace("_50x50.jpg", "")
                                .replace("._SS47_.jpg", ".jpg")
                                .replace("50x50", "200X200").replace(
                                    "32x32", "400x400").replace(
                                    "60x60", "400x400");
                            $('.init_img').attr('src', img_src);
                            $('.big_img').attr('src', img_src);
                        })
                });
            //小图点击切换运动
            var $l = 0;
            var $len = ($(".ul_pic>li").length - 1) * 90;
            $(".prev").click(function () {
                $l += 90;
                $(".ul_pic").stop().animate({
                    left: ($l + 25) + "px"
                })
                setTimeout(function () {
                    if ($l >= 0) {
                        $l = 0;
                        $(".ul_pic").stop().animate({
                            left: "25px"
                        })
                    }
                }, 100)
            })
            $(".next").click(function () {
                $l -= 90;
                $(".ul_pic").stop().animate({
                    left: ($l + 25) + "px"
                })
                setTimeout(function () {
                    if ($l < -$len) {
                        $l = -$len;
                        $(".ul_pic").stop().animate({
                            left: -$len + 25 + "px"
                        })
                    }
                }, 100)
            })
        }

        function setMainImg() {
            $('.ul_pic li').each(function (i) {
                if ($('.ul_pic li').eq(i).hasClass('red_border')) {
                    var imgUrl = $(this).find("img")[0].src;
                    $("#first_li").after($(this));
                    $("#first_li").val(imgUrl);
                }
                roastingImg();
            });
        }

        function aliImgToExpress() {
            var img_src = "";
            var index = null;
            $('#ali_goods_info').find('img').each(function (i) {
                $(this).click(function () {
                    var height = $(this).height();
                    if (height >= 100) {
                        $(this).toggleClass('img_checked');
                        if ($(this).hasClass('img_checked')) {
                            img_src = $('.r_box img').eq(i).attr('src');
                            tempJson["" + i] = img_src;
                        } else {
                            tempJson["" + i] = "";
                        }
                    } else {
                        showMessage('请选择高度大于100的图片');
                    }
                });
            });

            $(".inp_style").click(function () {
                $(this).removeAttr("readonly");
                $(this).css("background-color", "rgb(255, 255, 255)");
            });
            $(".inp_style").blur(function () {
                $(this).attr("readonly", true);
                $(this).css("background-color", "#d8d8d8");
            });
        }

        function addToImportE(pid) {
            if ($("#to_left_sp").prop("disabled") == true) {
                return;
            } else {
                var img_tag = "";
                var notChoose = true;
                $('#ali_goods_info').find('img').each(function (i) {
                    if ($(this).hasClass('img_checked')) {
                        notChoose = false;
                    }
                });
                if (notChoose) {
                    showMessage('请选择高度大于100的图片');
                } else {
                    var imgs = "";
                    for (var key in tempJson) {
                        if (!(key in imgJson)) {
                            if (tempJson[key] != "") {
                                imgJson[key] = tempJson[key];
                                //imgs += ";" + tempJson[key];
                                img_tag += '<img style="width:78%;" src="' + tempJson[key] + '" />';

                            }
                        }
                    }
                    var fmBody = $("iframe").contents().find("body");
                    fmBody.append(img_tag);
                    $('#ali_goods_info').find('img').removeClass("img_checked");
                    //添加Einfo图片到本地和线上图片服务器
                    //uploadEinfoNetImg(imgs.substring(1), pid);
                }
            }
        }

        //添加Einfo图片到本地和线上图片服务器
        function uploadEinfoNetImg(imgs, pid) {
            $("#to_left_sp").attr("disabled", true);
            $('.mask').show().text("正在上传图片，请等待...");

            $
                .ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/editc/uploadEinfoNetImg',
                    data: {
                        "imgs": imgs,
                        "pid": pid
                    },
                    success: function (data) {
                        $("#to_left_sp").removeAttr("disabled");
                        $('.mask').hide();
                        if (data.ok) {
                            //showMessage("执行成功，图片异步上传，请保存后刷新界面");
                            $.messager.alert("提醒",
                                "执行成功，如若图片未显示，请保存后刷新界面，否则请重新上传",
                                "info");
                            var newImgUrls = data.data;
                            var fmBody = $("iframe").contents().find("body");
                            var urls = newImgUrls.split(";");
                            if (urls.length > 0) {
                                var srcs = '';
                                for (var i = 0; i < urls.length; i++) {
                                    if (urls[i] != "") {
                                        srcs += '<img style="width:78%;" src="' + urls[i] + '" />'
                                    }
                                }
                                if (srcs != "") {
                                    fmBody.append(srcs);
                                }
                            }
                            $('#ali_goods_info').find('img').removeClass("img_checked");
                        } else {
                            $.messager.alert("提醒", data.message, "error");
                        }
                    },
                    error: function (XMLResponse) {
                        $("#to_left_sp").removeAttr("disabled");
                        $('.mask').hide();
                        $.messager.alert("提醒", "执行错误，请联系管理员", "error");
                    }
                });
        }

        function showMessage(msg) {
            $('.mask').show().text(msg);
            setTimeout(function () {
                $('.mask').hide();
            }, 1500);
        }


        function doSaveDetalis(pid, type, isSoldFlag) {
            if (pid == "" || pid == "0" || pid == 0) {
                showMessage("pid为空");
                return;
            }
            var enname = $("#import_enname").val();
            if (enname == "") {
                showMessage("翻译名称为空");
                $("#import_enname").focus();
                return;
            }
            var sellUtil = $("#import_sell_util").val();
            if (sellUtil == "" || sellUtil == null) {
                showMessage("售卖单位为空");
                $("#import_sell_util").focus();
                return;
            }

            var content = $("#goods_content").val();
            if (content == "") {
                showMessage("获取商品描述详情为空");
                return;
            }
            var wordSizeInfo = $("#word_info_div").html();

            var remotepath = $("#goods_remotepath").val();
            if (remotepath == "") {
                showMessage("获取图片远程路径为空");
                return;
            }

            var mainImg = $("#first_li").val();
            if (mainImg == "" || mainImg == null) {
                showMessage("设置封面图失败,请重新设置");
                return;
            } else if (mainImg == "99") {
                mainImg = "";
            }

            //不校检商品属性
            var endetail = getGoodsAttributeInfo();
            var imgInfo = getImgInfo();
            if (imgInfo == "") {
                showMessage("获取橱窗图为空");
                return;
            }
            var bizPrice = $("#biz_price").val();
            if (bizPrice == null || bizPrice == "" || bizPrice == 0) {
                bizPrice = "";
            }
            var wprice = "";
            var feePrice = "";
            var singSkus = "";
            var range_price = $("#range_price_id").val();
            var goodsPriceVal = $("#goods_price_id").val();
            //区间价格标识为空时，获取wprice表示的数据
            var reg = /(^[-+]?[1-9]\d*(\.\d{1,2})?$)|(^[-+]?[0]{1}(\.\d{1,2})?$)/;
            var isErr = true;
            if (range_price == null || range_price == "") {
                range_price = "";
                if (isSoldFlag > 0) {
                    var feePriceVal = $("#feeprice_id").val();
                    if (feePriceVal == null || feePriceVal == "") {
                        feePriceVal = "";
                    } else {
                        var i = 0;
                        $(".fee_price_inp").each(function () {
                            var feeprice_num = $.trim($("#fee_price_num_" + i).val());
                            var feeprice_val = $.trim($("#fee_price_val_" + i).val());
                            i++;
                            if (feeprice_num == null || feeprice_num == "") {
                                showMessage("区间价格[商品数量]获取失败");
                                return false;
                            } else if (!reg.test(feeprice_val)) {
                                showMessage("区间价格[" + feeprice_num + "]的价格异常");
                                return false;
                            } else {
                                isErr = false;
                                feePrice += "," + feeprice_num + "@" + feeprice_val;
                            }
                        });
                    }
                }
                if (feePrice.length == 0) {
                    var wpriceVal = $("#wprice_id").val();
                    if (wpriceVal == null || wpriceVal == "") {
                        if (goodsPriceVal == null || goodsPriceVal == "") {
                            showMessage("商品价格获取失败");
                            return false;
                        } else {
                            isErr = false;
                        }
                    } else {
                        var i = 0;
                        $(".wprice_inp").each(function () {
                            var wprice_num = $.trim($("#wprice_num_" + i).val());
                            var wprice_val = $.trim($("#wprice_val_" + i).val());
                            i++;
                            if (wprice_num == null || wprice_num == "") {
                                showMessage("区间价格[商品数量]获取失败");
                                return false;
                            } else if (!reg.test(wprice_val)) {
                                showMessage("区间价格[" + wprice_num + "]的价格异常");
                                return false;
                            } else {
                                isErr = false;
                                wprice += "," + wprice_num + "@" + wprice_val;
                            }
                        });
                    }
                }

            } else {//不为空时，获取单个sku的所有数据
                $(".inp_price").each(function () {
                    var ppid = $(this).attr("id");
                    var price = $(this).val();
                    if (ppid == null || ppid == "") {
                        showMessage("单规格价ID获取失败");
                        return false;
                    } else if (!reg.test(price)) {
                        showMessage("单规格价的价格异常");
                        return false;
                    } else {
                        isErr = false;
                        singSkus += ";" + ppid + "@" + price;
                    }
                });
            }
            if (isErr) {
                return;
            } else {
                //获取需要删除的规格ids数据
                var typeDeleteIds = getTypeDeleteIdsData();
                var typeRepalceIds = getTypeReplaceIdsData();
                var skuCount = Object.keys(skuJson).length;
                $('.mask').show().text('正在执行，请等待...');
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/editc/saveEditDetalis',
                    data: {
                        "type": type,
                        "pid": pid,
                        "enname": enname,
                        "remotepath": remotepath,
                        "imgInfo": imgInfo,
                        "endetail": endetail,
                        "content": content,
                        "sku": singSkus.substring(1),
                        "wprice": wprice.substring(1),
                        "feePrice": feePrice.substring(1),
                        "rangePrice": range_price,
                        "bizPrice": bizPrice,
                        "goodsPrice": goodsPriceVal,
                        "sellUtil": sellUtil,
                        "typeRepalceIds": typeRepalceIds.substring(1),
                        "typeDeleteIds": typeDeleteIds.substring(1),
                        "wordSizeInfo": wordSizeInfo,
                        "mainImg": mainImg,
                        "skuCount":skuCount
                    },
                    success: function (data) {
                        $('.mask').hide();
                        if (data.ok) {
                            showMessage(data.message);
                            setTimeout(function () {
                                window.location.reload();
                            }, 1500);
                        } else {
                            $.messager.alert("提醒", data.message, "error");
                        }
                    },
                    error: function (XMLResponse) {
                        $('.mask').hide();
                        $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                    }
                });
            }
        }

        function getTypeDeleteIdsData() {
            var typeIds = $("#delete_type_ids").val();
            var typeList = typeIds.split(",");
            var tempIds = "";
            if (typeList.length > 0) {
                for (var i = 0; i < typeList.length; i++) {
                    if (typeList[i].length > 1) {
                        /*if ($("#" + typeList[i]).parent().hasClass("red_border")) {
                            tempIds += "," + typeList[i];
                        }*/
                        tempIds += "," + typeList[i];
                    }
                }
            }

            if (tempIds.length == 0) {
                tempIds = ",";
            }
            return tempIds;
        }


        function getTypeReplaceIdsData() {
            var typeIds = "";
            for (key in typeJson) {
                typeIds += "," + key + "@" + typeJson[key];
            }
            return typeIds;
        }


        function getGoodsAttributeInfo() {
            var info = "";
            $("#attribute_table").find("tbody").find(".inp_style").each(function () {
                info += ";" + $(this).val().trim();
            });
            return info = "" ? "" : info.substring(1);
        }

        function getImgInfo() {
            var info = "";
            $(".li_pic").find("img").each(function () {
                info += ";" + $(this).attr("src").trim();
            });
            return info.substring(1);
        }

        function setGoodsValid(pid, type) {
            if (pid == "" || pid == "0" || pid == 0) {
                showMessage("pid为空");
                return;
            }
            if (type > 0) {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/editc/setGoodsOnline',
                    data: {
                        "pid": pid
                    },
                    success: function (data) {
                        if (data.ok) {
                            showMessage("执行成功，即将刷新页面");
                            setTimeout(function () {
                                window.location.reload();
                            }, 1500);
                        } else {
                            $.messager.alert("提醒", data.message, "error");
                        }
                    },
                    error: function (XMLResponse) {
                        $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                    }
                });
            } else {
                $.messager.prompt('提示', '请输入下架原因:', function (rs) {
                    if (rs) {
                        $.ajax({
                            type: 'POST',
                            dataType: 'json',
                            url: '/cbtconsole/editc/checkIsHotGoods',
                            data: {
                                "pid": pid
                            },
                            success: function (data) {
                                if (data.ok) {
                                    $.messager.confirm('提示', '此商品为热销有库存商品，确定要下架此商品吗？', function (yes) {
                                        if (yes) {
                                            doGoodsUnValid(pid, 1, rs);
                                        }
                                    });
                                } else {
                                    if (data.total == 1) {
                                        doGoodsUnValid(pid, 0, rs);
                                    } else {
                                        $.messager.alert("提醒", data.message, "error");
                                    }
                                }
                            },
                            error: function (XMLResponse) {
                                $.messager.alert("提醒", "执行错误，请联系管理员", "error");
                            }
                        });
                    }
                });

            }
        }

        function doGoodsUnValid(pid, tip, rs) {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '/cbtconsole/editc/setGoodsOff',
                data: {
                    "pid": pid,
                    "reason": rs
                },
                success: function (data) {
                    if (data.ok) {
                        if (tip == 1) {
                            $.messager.alert("提醒", "执行成功，请到热卖区模块将此商品移出热卖区，谢谢！", "info", function () {
                                window.location.reload();
                            });
                        } else {
                            showMessage("执行成功，即将刷新页面");
                            setTimeout(function () {
                                window.location.reload();
                            }, 1500);
                        }

                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (XMLResponse) {
                    $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                }
            });
        }

        function doQuery() {
            var pid = $("#query_pid").val();
            if (pid == null || pid == "") {
                showMessage("PID为空");
            } else {
                window.location.href = "/cbtconsole/editc/detalisEdit?pid=" + pid;
            }
        }

        function initDialog() {
            $('#type_select').combobox({
                valueField: 'id',
                textField: 'text',
                data: [{
                    "id": "0",
                    "text": "本地图片"
                }, {
                    "id": "1",
                    "text": "网络图片"
                }],
                onChange: function (newValue, oldValue) {
                    if (newValue == "0") {
                        $('#local_picture').filebox('enable');
                        $('#net_url').textbox('disable'); //设置输入框为禁用
                    } else {
                        $('#local_picture').filebox('disable');//设置文件上传框为禁用
                        $('#net_url').textbox('enable');
                    }
                }
            });
            $('#type_select').combobox('panel').height(60);
            $('#type_select').combobox('select', '0');
            $('#local_picture').filebox('enable');
            $('#net_url').textbox('disable'); //设置输入框为禁用
            //closeDialog();
        }

        function uploadTypePicture(pid) {
            var selectVal = $('#type_select').combobox('getValue');
            //选择本地文件的使用form提交后台
            var formData = new FormData($("#uploadFileForm")[0]);
            if (selectVal == '0') {
                $.messager.progress({
                    title: '上传本地图片',
                    msg: '请等待...'
                });
                $
                    .ajax({
                        url: '/cbtconsole/editc/uploadByJs',
                        type: 'POST',
                        data: formData,
                        contentType: false,
                        processData: false,
                        success: function (data) {
                            $.messager.progress('close');
                            if (data.ok) {
                                showMessage("执行成功");
                                var srcs = '<li class="li_pic"><img src="' + data.data + '" /></li>';
                                if (srcs != "") {
                                    $(".ul_pic").prepend(srcs);
                                }
                                var liLen = $('.ul_pic li').length;
                                roastingImg();
                                $('.ul_pic li').eq(0).trigger('click');
                                closeUploadDialog();
                            } else {
                                $.messager.alert("提醒", data.message, "info");
                            }
                        },
                        error: function (XMLResponse) {
                            $.messager.progress('close');
                            $.messager.alert("提醒", "执行错误，请联系管理员", "error");
                        }
                    });
            } else if (selectVal == '1') {
                //选择网络图片的直接ajax传递后台
                var url = $('#net_url').textbox('getValue');
                $.messager.progress({
                    title: '上传网络图片',
                    msg: '请等待...'
                });
                $
                    .ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: '/cbtconsole/editc/uploadTypeNetImg',
                        data: {
                            "imgs": url,
                            "pid": pid,
                            "check": 1
                        },
                        success: function (data) {
                            closeUploadDialog();
                            $.messager.progress('close');
                            if (data.ok) {
                                //showMessage("执行成功，图片异步上传，请保存后刷新界面");
                                $.messager.alert("提醒",
                                    "执行成功，如若图片未显示，请保存后刷新界面，否则请重新上传",
                                    "info");
                                var newImgUrls = data.data;
                                var urls = newImgUrls.split(";");
                                if (urls.length > 0) {
                                    var srcs = '';
                                    for (var i = 0; i < urls.length; i++) {
                                        if (urls[i] != "") {
                                            srcs += '<li class="li_pic"><img src="' + urls[i] + '" /></li>';
                                        }
                                    }
                                    if (srcs != "") {
                                        $(".ul_pic").prepend(srcs);
                                    }
                                }
                                roastingImg();
                                $('.ul_pic li').eq(0).trigger('click');
                            } else {
                                $.messager.alert("提醒", data.message, "error");
                            }
                        },
                        error: function (XMLResponse) {
                            $.messager.progress('close');
                            $.messager.alert("提醒", "执行错误，请联系管理员", "error");
                        }
                    });
            } else {
                showMessage("请选择上传类型");
            }
        }

        function showDialog() {
            $('#type_select').combobox('select', '0');
            $('#pic_dlg').dialog('open');
        }

        function closeUploadDialog() {
            $('#pic_dlg').dialog('close');
            $("#uploadFileForm")[0].reset();
        }

        function useAliGoodsDetails(pid) {
            var ali_goods_info = $("#ali_goods_info").html();
            if (ali_goods_info == null || ali_goods_info == "") {
                showMessage("获取速卖通详情失败");
            } else {
                $.messager.confirm('提示', '是否使用速卖通详情?使用后编辑的详情数据将覆盖', function (rs) {
                    if (rs) {
                        $("#use_ali_goods").attr("disabled", true);
                        $.messager.progress({
                            title: '正在处理数据',
                            msg: '请等待...'
                        });
                        $.ajax({
                            type: 'POST',
                            dataType: 'text',
                            url: '/cbtconsole/editc/useAliGoodsDetails',
                            data: {
                                "pid": pid,
                                "aliGoodsInfo": ali_goods_info
                            },
                            success: function (data) {
                                $.messager.progress('close');
                                $("#use_ali_goods").removeAttr("disabled");
                                var json = eval('(' + data + ')');
                                if (json.ok) {
                                    showMessage("执行成功");
                                    $("#goods_content").val(json.data);
                                } else {
                                    showMessage(json.message);
                                }
                            },
                            error: function () {
                                $.messager.progress('close');
                                $("#use_ali_goods").removeAttr("disabled");
                                $.messager.alert("提醒", "error", "error");
                            }
                        });
                    }
                });

            }
        }

        function openSimilarGoodsDialog() {
            $('#similar_dlg').dialog('open');
        }

        function openReviewDiv() {
            $('#review_dlg').dialog('open');
        }

        function closeSimilarGoodsDialog() {
            $('#similar_dlg').dialog('close');
            $("#similar_pids").val("");
        }

        function closeRemark() {
            $('#review_dlg').dialog('close');
            $("#review_remark").val("");
        }

        function openEditReview(id, country, review_score, review_flag, createtime, pid) {
            var review_remark= $("#review_remark_" + id).text();
            $("#oldCreateTime").val(createtime);
            $("#goods_pid").val(pid);
            $("#goods_id").val(id);

            $('#edit_score').combobox('setValue', review_score);
            $('#editcountry').combobox('setValue', country);
            $("#edit_remark").val(review_remark);
            if (review_flag == "不显示") {
                review_flag = "1";
            } else if (review_flag == "显示") {
                review_flag = "0";
            }
            $('#update_flag').combobox('setValue', review_flag);
            $('#update_review_dlg').dialog('open');
        }

        function closeUpdateRemark() {
            $("#oldCreateTime").val("");
            $("#goods_pid").val("");

            $('#edit_score').combobox('setValue', "1");
            $('#editcountry').combobox('setValue', "country");
            $("#edit_remark").val("");
            $('#update_flag').combobox('setValue', "0");
            $('#update_review_dlg').dialog('close');
        }

        function updateReviewRemark() {
            var oldCreateTime = $("#oldCreateTime").val();
            var goods_pid = $("#goods_pid").val();
            var id = $("#goods_id").val();

            var edit_remark = $("#edit_remark").val();
            var editcountry = $('#editcountry').combobox('getValue');
            var edit_score = $("#edit_score").val();
            var update_flag = $('#update_flag').combobox('getValue');
            if (edit_remark == null || edit_remark == "") {
                showMessage("请输入商品评论");
                return false;
            }
            if (editcountry == null || editcountry == "" || editcountry == "country") {
                showMessage("请选择国家");
                return false;
            }
            if (edit_score == null || edit_score == "") {
                showMessage("请选择评分");
                return false;
            }
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/editc/updateReviewRemark',
                data: {
                    "edit_remark": edit_remark,
                    "editcountry": editcountry,
                    "edit_score": edit_score,
                    "update_flag": update_flag,
                    "oldCreateTime": oldCreateTime,
                    "goods_pid": goods_pid,
                    "id": id
                },
                success: function (data) {
                    var json = eval('(' + data + ')');
                    if (json.ok) {
                        showMessage("修改评论成功；2秒后刷新页面");
                        setTimeout(function () {
                            window.location.reload();
                        }, 2000);
                    } else {
                        showMessage("修改评论失败");
                    }
                },
                error: function () {
                    $.messager.alert("提醒", "error", "error");
                }
            });
        }

        function addReviewRemark(goods_pid) {
            var review_remark = $("#review_remark").val();
            var country = $('#country').combobox('getValue');
            var review_score = $("#review_score").val();
            if (review_remark == null || review_remark == "") {
                showMessage("请输入商品评论");
                return false;
            }
            if (country == null || country == "" || country == "country") {
                showMessage("请选择国家");
                return false;
            }
            if (review_score == null || review_score == "") {
                showMessage("请选择评分");
                return false;
            }
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/editc/addReviewRemark',
                data: {
                    "goods_pid": goods_pid,
                    "review_remark": review_remark,
                    "country": country,
                    "review_score": review_score
                },
                success: function (data) {
                    var json = eval('(' + data + ')');
                    if (json.ok) {
                        // showMessage("添加评论成功；1秒后刷新页面");
                        // setTimeout(function () {
                        //     window.location.reload();
                        // }, 800);
                        $('#review_dlg').dialog('close');
                        alert(json.message);
                    } else {
                        showMessage("添加评论失败");
                    }
                },
                error: function () {
                    $.messager.alert("提醒", "error", "error");
                }
            });
        }

        function addSimilarGoods() {
            var mainPid = $("#main_pid").val();
            if (mainPid == null || mainPid == "") {
                showMessage("获取商品pid失败，请重试");
                return false;
            }
            var similarPids = $("#similar_pids").val();
            if (similarPids == null || similarPids == "") {
                showMessage("获取相似商品pid失败，请重试");
                return false;
            }
            $.messager.progress({
                title: '正在保存数据',
                msg: '请等待...'
            });
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/editc/addSimilarGoods',
                data: {
                    "mainPid": mainPid,
                    "similarPids": similarPids
                },
                success: function (data) {
                    $.messager.progress('close');
                    var json = eval('(' + data + ')');
                    closeSimilarGoodsDialog();
                    if (json.ok) {
                        showMessage(json.message);
                        querySimilarGoodsByMainPid();
                    } else {
                        $.messager.alert("提醒", json.message, "error");
                    }
                },
                error: function () {
                    $.messager.progress('close');
                    $.messager.alert("提醒", "error", "error");
                }
            });
        }

        function querySimilarGoodsByMainPid() {
            var mainPid = $("#main_pid").val();
            if (mainPid == null || mainPid == "") {
                return false;
            }
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/editc/querySimilarGoodsByMainPid',
                data: {
                    "mainPid": mainPid
                },
                success: function (data) {
                    var json = eval('(' + data + ')');
                    if (json.ok) {
                        var data = json.data;
                        if (data.length > 0) {
                            $("#similar_goods_ul").empty();
                            var lis = '';
                            for (var i = 0; i < data.length; i++) {
                                lis += '<li class="li_pic_2"><a target="_blank" href="/cbtconsole/editc/detalisEdit?pid=';
                                lis += data[i].similarPid + '"><img class="img_sty" src="' + data[i].similarGoodsImg + '"></a></li>';
                            }
                            $("#similar_goods_ul").append(lis);
                        }
                    } else {
                        showMessage(json.message);
                    }
                },
                error: function () {
                    showMessage("查询相似商品失败");
                }
            });
        }

        function setGoodsFlagByPid(pid, weight_flag, ugly_flag, benchmarking_flag, describe_good_flag, never_off_flag, uniqueness_flag, promotion_flag) {
            $.messager.confirm('提示', '是否确认设置此标识？', function (rs) {
                if (rs) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'text',
                        url: '/cbtconsole/editc/setGoodsFlagByPid',
                        data: {
                            "pid": pid,
                            "weight_flag": weight_flag,
                            "ugly_flag": ugly_flag,
                            "benchmarking_flag": benchmarking_flag,
                            "describe_good_flag": describe_good_flag,
                            "never_off_flag": never_off_flag,
                            "uniqueness_flag": uniqueness_flag,
                            "promotion_flag": promotion_flag
                        },
                        success: function (data) {
                            var json = eval('(' + data + ')');
                            if (json.ok) {
                                showMessage("执行成功");
                                setTimeout(function () {
                                    window.location.reload();
                                }, 500);
                            } else {
                                $.messager.alert("提醒", json.message, "info");
                            }
                        },
                        error: function () {
                            $.messager.alert("提醒", "执行错误，请联系管理员", "error");
                        }
                    });
                }
            });
        }


        function beforeSetAliInfo(pid, isBenchmark, bmFlag, aliPid) {
            if ((isBenchmark == 1 && bmFlag == 1) || isBenchmark == 2) {
                $.messager.confirm('提示', '已经存在对标(AliPid:' + aliPid + '),是否重新对标？', function (rs) {
                    if (rs) {
                        $("#new_ali_pid").val(aliPid);
                        $('#set_new_aliPid_dlg').dialog('open');
                    }
                });
            } else {
                $('#set_new_aliPid_dlg').dialog('open');
            }
        }


        function closeNewAliPidDlg() {
            $('#set_new_aliPid_dlg').dialog('close');
        }

        function setNewAliPidInfo(pid) {
            var aliPid = $("#new_ali_pid").val();
            var aliPrice = $("#new_ali_price").val();
            if (aliPid && aliPrice) {
                $.ajax({
                    type: 'POST',
                    dataType: 'text',
                    url: '/cbtconsole/editc/setNewAliPidInfo',
                    data: {
                        "pid": pid,
                        "aliPid": aliPid,
                        "aliPrice": aliPrice
                    },
                    success: function (data) {
                        var json = eval('(' + data + ')');
                        if (json.ok) {
                            showMessage("执行成功");
                            closeNewAliPidDlg();
                        } else {
                            $.messager.alert("提醒", json.message, "info");
                        }
                    },
                    error: function () {
                        $.messager.alert("提醒", "执行错误，请联系管理员", "error");
                    }
                });
            } else {
                showMessage("请输入相关数据");
            }

        }

        function setGoodsRepairedByPid(pid) {
            $.messager.confirm('提示', '是否确认设置已修复？', function (rs) {
                if (rs) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'text',
                        url: '/cbtconsole/editc/setGoodsRepairedByPid',
                        data: {
                            "pid": pid
                        },
                        success: function (data) {
                            var json = eval('(' + data + ')');
                            if (json.ok) {
                                showMessage(json.message);
                            } else {
                                $.messager.alert("提醒", json.message, "info");
                            }
                        },
                        error: function () {
                            $.messager.alert("提醒", "执行错误，请联系管理员", "error");
                        }
                    });
                }
            });
        }

        function setNewCatid(pid, catid1) {
            $("#catid_pid").val(pid);
            $("#catid_old").val(catid1);
            $('#catid-dlg').dialog('open');
        }


        function addKeyWordWeight(shopId, catid, pid) {
            $("#add_shop_id").val(shopId);
            $("#add_shop_catid").val(catid);
            $('#enter_div_sty').dialog('open');
        }

        function closeKeyWordDialog() {
            $('#enter_div_sty').dialog('close');
            $("#add_shop_id").val("");
            $("#add_shop_catid").val("");
            $("#form_enter")[0].reset();
        }

        function saveKeyWordWeight() {
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
                            closeKeyWordDialog();
                            showMessage('数据保存成功');
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


        function addBenchmarking(pid) {
            $("#add_goods_pid").val(pid);
            $('#enter_benchmarking_sty').dialog('open');
        }

        function openEditLog(pid) {
            window.open("/cbtconsole/apa/customGoodsEditLog.html?pid=" + pid);
        }

        function closeBenchmarking() {
            $('#enter_benchmarking_sty').dialog('close');
            $("#add_goods_pid").val("");
            $("#form_benchmarking")[0].reset();
        }

        function updateWordSizeInfo(type) {
            if (type == 0) {
                $("#size_info_en_text").val("");
            }
            $('#size_info_en_dlg').dialog('open');
        }

        function addSizeInfoEn() {
            var size_info_en_text = $("#size_info_en_text").val();
            $("#word_info_div").empty();
            $("#word_info_div").append(size_info_en_text);
            closeSizeInfoEnDialog();
        }


        function closeSizeInfoEnDialog() {
            $('#size_info_en_dlg').dialog('close');
        }


        function saveBenchmarking() {
            var noKeyWeight = false;
            var pid = $("#add_goods_pid").val();
            var benchmarkingPid = $("#add_benchmarking_pid").val();
            var benchmarkingPrice = $("#add_benchmarking_price").val();
            if (benchmarkingPid == null || benchmarkingPid == "" || benchmarkingPid == "0") {
                $.messager.alert("提醒", "获取对标商品PID失败，请刷新页面重试", "info");
                noKeyWeight = true;
            }
            if (benchmarkingPrice == null || benchmarkingPrice == "" || benchmarkingPrice == "0") {
                $.messager.alert("提醒", "获取对标商品价格失败，请刷新页面重试", "info");
                noKeyWeight = true;
            }
            if (!noKeyWeight) {
                $.ajax({
                    type: "POST",
                    url: "/cbtconsole/editc/saveBenchmarking",
                    data: {
                        pid: pid,
                        benchmarkingPid: benchmarkingPid,
                        benchmarkingPrice: benchmarkingPrice
                    },
                    success: function (data) {
                        if (data.ok) {
                            closeBenchmarking();
                            showMessage('数据保存成功');
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


        function addDeleteTypeId(typeId) {
            var tpIds = $("#delete_type_ids").val();
            $("#delete_type_ids").val(tpIds + "," + typeId);
        }

        function removeTypeId() {
            $(".goods_top").find("ul").find("li").each(function () {
                if ($(this).hasClass("red_border")) {
                    var typeId = $(this).find("input").attr("id");
                    var tpIds = $("#delete_type_ids").val();
                    $("#delete_type_ids").val(tpIds + "," + typeId);
                    $(this).remove();
                }
            });
        }

        function deleteWordSizeInfo(pid) {
            $.messager.confirm('提示', '是否删除文字尺码表，删除后数据将无法恢复？', function (rs) {
                if (rs) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: '/cbtconsole/editc/deleteWordSizeInfoByPid',
                        data: {
                            "pid": pid
                        },
                        success: function (data) {
                            if (data.ok) {
                                showMessage("执行成功，即将刷新页面");
                                setTimeout(function () {
                                    window.location.reload();
                                }, 1500);
                            } else {
                                $.messager.alert("提醒", data.message, "error");
                            }
                        },
                        error: function (XMLResponse) {
                            $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                        }
                    });
                }
            });

        }

        function saveEditValue(typeId, inputVal) {
            typeJson[typeId] = inputVal;
        }

        function setNoBenchmarking(pid, finalWeight) {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '/cbtconsole/editc/setNoBenchmarking',
                data: {
                    "pid": pid,
                    "finalWeight": finalWeight
                },
                success: function (data) {
                    if (data.ok) {
                        showMessage("执行成功");
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (XMLResponse) {
                    $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                }
            });
        }

        function setNeverOff(pid) {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '/cbtconsole/editc/setNeverOff',
                data: {
                    "pid": pid
                },
                success: function (data) {
                    if (data.ok) {
                        showMessage("执行成功");
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (XMLResponse) {
                    $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                }
            });
        }


        function initOnLoad() {
            querySimilarGoodsByMainPid();
            $('#review_dlg').dialog('close');
            $('#update_review_dlg').dialog('close');
            setTimeout("flashImg()", 3000);
            setInterval("flashImg()", 30000);
        }

        function flashImg() {
            $(".editMode").find("img")
        }

        $(".editMode").find("img").on("click", function () {
            $(this).addClass("img_choose");
            $(this).siblings().removeClass("img_choose");
        });


        function closeMultiFileDialog() {
            $('#multi_file_dlg').dialog('close');
            $("#multiFileForm")[0].reset();
        }

        function closeGoodsDescDialog() {
            $('#set_goods_desc_type').dialog('close');
            $("#set_desc_form")[0].reset();
        }

        function closeOverSeaDialog() {
            $('#set_over_sea_div').dialog('close');
            $("#set_over_sea_form")[0].reset();
        }


        function openOverSeaDialog(flag) {
            queryHotCategory();
            $.ajax({
                type: 'POST',
                sync: true,
                dataType: 'json',
                url: '/cbtconsole/editc/getAllZone',
                data: {
                    "isUsd": 1
                },
                success: function (data) {
                    if (data.ok) {
                        var content = "";
                        var jsonData = data.data;
                        for (var i=0;i< jsonData.length;i++) {
                            content += '<option value="' + jsonData[i].id + '">' + jsonData[i].country + '</option>';
                        }
                        $("#query_country_id").empty();
                        $("#query_country_id").append(content);
                        $('#set_over_sea_div').dialog('open');
                        $("#query_is_support").val(flag);
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (XMLResponse) {
                    $.messager.alert("提醒", "获取数据错误", "error");
                }
            });
        }

        function setSearchable(pid, flag) {
            var content = '是否确认设置不可搜索标识';
            if(flag > 0){
                content = '是否确认设置可搜索标识';
            }
            $.messager.confirm('提示', content, function (rs) {
                if (rs) {
                    $.ajax({
                        type: 'POST',
                        sync: true,
                        dataType: 'json',
                        url: '/cbtconsole/editc/setSearchable',
                        data: {
                            "pid":pid,
                            "flag": flag
                        },
                        success: function (data) {
                            if (data.ok) {
                                showMessage("执行成功");
                                setTimeout(function () {
                                    window.location.reload();
                                }, 500);
                            } else {
                                $.messager.alert("提醒", data.message, "error");
                            }
                        },
                        error: function (XMLResponse) {
                            $.messager.alert("提醒", "网络错误,请重试", "error");
                        }
                    });
                }
            });
        }

        function setTopSort(pid, num) {
            var content = '是否确认设置搜索排序(请选择1-59999之间数值,数值越小越排在前面)';
            if (num > 0) {
                content = '是否确认设置搜索排序(请选择1-59999之间数值,数值越小越排在前面,当前值:' + num + ')';
            }
            $.messager.prompt('提示信息', content, function (newSort) {
                if (newSort) {
                    var reg = /(^[1-9]\d*$)/;
                    if (reg.test(newSort) && 1<=newSort && newSort < 60000) {
                        if (num == newSort) {
                            $.messager.alert("提醒", "新的数值和原数值相同", "info");
                        } else {
                            $.messager.progress({
                                title: '正在更新',
                                msg: '请等待...'
                            });
                            $.ajax({
                                type: 'POST',
                                dataType: 'json',
                                url: '/cbtconsole/editc/setTopSort',
                                data: {
                                    "pid": pid,
                                    "newSort": newSort,
                                    "num": num
                                },
                                success: function (json) {
                                    $.messager.progress('close');
                                    if (json.ok) {
                                        $.messager.alert("提醒", "执行成功，页面即将刷新", "info");
                                        setTimeout(function () {
                                            window.location.reload();
                                        }, 500);
                                    } else {
                                        $.messager.alert("提醒", json.message, "error");
                                    }
                                },
                                error: function () {
                                    $.messager.progress('close');
                                    $.messager.alert("提醒", "执行失败，请重试", "error");
                                }
                            });
                        }

                    } else {
                        showMessage('新的数值必须为正数,且在范围内！');
                    }
                } else {
                    showMessage('未输入新的数值或取消输入！');
                }
            });

        }

        function setSalable(pid, flag) {
            var content = '是否确认设置取消美加不可售卖标识';
            if(flag > 0){
                content = '是否确认设置美加不可售卖标识';
            }
            $.messager.confirm('提示', content, function (rs) {
                if (rs) {
                    $.ajax({
                        type: 'POST',
                        sync: true,
                        dataType: 'json',
                        url: '/cbtconsole/editc/setSalable',
                        data: {
                            "pid":pid,
                            "flag": flag
                        },
                        success: function (data) {
                            if (data.ok) {
                                showMessage("执行成功");
                                setTimeout(function () {
                                    window.location.reload();
                                }, 500);
                            } else {
                                $.messager.alert("提醒", data.message, "error");
                            }
                        },
                        error: function (XMLResponse) {
                            $.messager.alert("提醒", "网络错误,请重试", "error");
                        }
                    });
                }
            });
        }


        function uploadMultiFile() {
            $.messager.progress({
                title: '上传本地图片',
                msg: '请等待...'
            });
            $("#multiFileForm").form('submit', {
                type: "post",  //提交方式
                url: "/cbtconsole/editc/uploadMultiFile", //请求url
                success: function (data) {
                    $.messager.progress('close');
                    var data = eval('(' + data + ')');
                    if (data.ok) {
                        closeMultiFileDialog();
                        var json = data.data;
                        var content = '';
                        for (var i = 0; i < json.length; i++) {
                            content += '<br><img src="' + json[i] + '"/>'
                        }
                        if (content.length > 10) {
                            editorObj.appendHTML(content)
                        }
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function () {
                    $.messager.progress('close');
                    $.messager.alert("提醒", "上传错误，请联系管理员", "error");
                }
            });
        }

        function setGoodsDescWithHotType() {
            getHotTypeList();

        }

        function getHotTypeList() {
            $.ajax({
                type: 'POST',
                sync: true,
                dataType: 'json',
                url: '/cbtconsole/hotManage/getHotTypeList',
                data: {"hotType": 24},
                success: function (data) {
                    if (data.success) {
                        var content = "<option value='0'>请选择</option>";
                        var jsonData = data.rows;
                        for (var i=0;i< jsonData.length;i++) {
                            content += '<option value="' + jsonData[i].id + '">' + jsonData[i].showName + '</option>';
                        }
                        $("#hot_type_id").empty();
                        $("#hot_type_id").append(content);
                        $('#set_goods_desc_type').dialog('open');
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (XMLResponse) {
                    $.messager.alert("提醒", "网络错误，请重试", "error");
                }
            });
        }

        function saveGoodsDescInfo(pid, obj) {
            $(obj).prop("disabled", true);
            var hotTypeId = $("#hot_type_id").val();
            if(hotTypeId == null || hotTypeId== "" || hotTypeId == "0"){
                $.messager.alert("提醒", "获取分类ID失败", "error");
                $(obj).prop("disabled", false);
            }else{
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/editc/saveGoodsDescInfo',
                    data: {
                        pid: pid,
                        hotTypeId: hotTypeId
                    },
                    success: function (data) {
                        if (data.ok) {
                            $(obj).prop("disabled", false);
                            showMessage("执行成功");
                            setTimeout(function () {
                                window.location.reload();
                            }, 500);
                        } else {
                            $.messager.alert("提醒", data.message, "error");
                        }
                    },
                    error: function (XMLResponse) {
                        $(obj).prop("disabled", false);
                        $.messager.alert("提醒", "保存错误，请联系管理员", "error");
                    }
                });
            }
        }

        function saveOverSeaInfo(pid) {
            var countryId = $("#query_country_id").val();
            var isSupport = $("#query_is_support").val();
            var categoryId= $("#sea_category_id").val();
            if(!categoryId || categoryId <= 0){
                showMessage("请选择类别数据");
                return;
            } else {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/editc/setGoodsOverSea',
                    data: {
                        pid: pid,
                        countryId: countryId,
                        isSupport: isSupport,
                        categoryId: categoryId
                    },
                    success: function (data) {
                        if (data.ok) {
                            showMessage("执行成功");
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
        
        function queryHotCategory() {
            var hotType = $("#sea_hot_class_id").val();
            $.ajax({
                type: 'POST',
                sync: true,
                dataType: 'json',
                url: '/cbtconsole/hotManage/getHotTypeList',
                data: {"hotType": hotType},
                success: function (data) {
                    if (data.success) {
                        var content = "";
                        var jsonData = data.rows;
                        for (var i=0;i< jsonData.length;i++) {
                            content += '<option value="' + jsonData[i].id + '">' + jsonData[i].showName + '</option>';
                        }
                        $("#sea_category_id").empty();
                        $("#sea_category_id").append(content);
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function (XMLResponse) {
                    $.messager.alert("提醒", "网络错误，请重试", "error");
                }
            });
        }

        function createComboTree() {
            $('#select_catid').combotree({
                url: '/cbtconsole/category/queryCategoryTree',
                required: true
            });
        }
    </script>
</head>

<body onload="initOnLoad()">

<c:if test="${uid ==0}">
    {"status":false,"message":"请重新登录进行操作"}
</c:if>

<c:if test="${uid < 0}">
    <div>
        <p style="margin-left: 700px;font-size: 28px;color: red;margin-top: 200px;">获取产品失败</p>
        <c:if test="${not empty message}">
            <p>${message}</p>
        </c:if>
    </div>
</c:if>

<c:if test="${uid > 0}">

    <div class="mask"></div>


    <div id="catid-dlg" class="easyui-dialog" style="width:500px;height:250px;padding:10px 30px;"
         title="设置新的类别">
        <table>
            <tr>
                <td>PID:</td>
                <td><input type="text" id="catid_pid" name="pid" style="width:250px;height: 30px;"/></td>
            </tr>
            <tr>
                <td>原类别:</td>
                <td><input type="text" id="catid_old" name="oldCatid" style="width:250px;height: 30px;"/></td>
            </tr>
            <tr>
                <td>修改类别:</td>
                <td><select id="select_catid" class="easyui-combotree" name="newCatid" style="width:250px;height: 36px;"></select>
                </td>
            </tr>
            <tr>
                <td colspan="2" style="text-align: center;">
                    <a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="savePidNewCatidInfo()">保存</a>
                    &nbsp;&nbsp;<a href="#" class="easyui-linkbutton" iconCls="icon-cancel"
                       onclick="javascript:$('#catid-dlg').dialog('close')">取消</a>
                </td>
            </tr>
        </table>
    </div>



    <div id="set_over_sea_div" class="easyui-dialog" title="设置海外仓"
         data-options="modal:true"
         style="width: 335px; height: 240px; padding: 10px;">
        <form style="margin-left: 44px;" id="set_over_sea_form" method="post" enctype="multipart/form-data">
            <table>
                <tr>
                    <td>热卖分组:</td>
                    <td>
                        <select id="sea_hot_class_id" onchange="queryHotCategory()" style="height: 26px;width: 150px;">
                            <option value="25">kids海外仓专区</option>
                            <option value="26">pets海外仓专区</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>分类:</td>
                    <td>
                        <select id="sea_category_id" style="height: 26px;width: 150px;">
                        </select>
                    </td>
                </tr>
                <tr>
                    <td>国家:</td>
                    <td><select id="query_country_id" style="height: 26px;width: 150px;"></select></td>
                </tr>
                <tr>
                    <td>是否支持</td>
                    <td><select id="query_is_support" style="height: 26px;width: 150px;">
                        <option value="0">不支持</option>
                        <option value="1">支持</option>
                    </select></td>
                </tr>
            </table>
        </form>
        <br>
        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="saveOverSeaInfo('${goods.pid}')" style="width: 80px">保存</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeOverSeaDialog()"
               style="width: 80px">关闭</a>
        </div>
    </div>

    <div id="set_goods_desc_type" class="easyui-dialog" title="设置描述很精彩"
         data-options="modal:true"
         style="width: 330px; height: 180px; padding: 10px;">
        <form style="margin-left: 44px;" id="set_desc_form" method="post" enctype="multipart/form-data">
            <span>分组:</span><select id="hot_type_id" style="height: 26px;width: 220px;">

        </select>
        </form>
        <br>
        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="saveGoodsDescInfo('${goods.pid}', this)" style="width: 80px">保存</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeGoodsDescDialog()"
               style="width: 80px">关闭</a>
        </div>
    </div>


    <div id="multi_file_dlg" class="easyui-dialog" title="详情多文件上传"
         data-options="modal:true"
         style="width: 300px; height: 180px; padding: 10px;">
        <form style="margin-left: 44px;" id="multiFileForm" method="post" enctype="multipart/form-data">
            <input type="hidden" name="pid" value="${goods.pid}"/>
            <input id="file" type="file" name="file" multiple="true">
        </form>

        <br><br>
        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="uploadMultiFile()" style="width: 80px">确认上传</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeMultiFileDialog()"
               style="width: 80px">关闭</a>
        </div>
    </div>


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
                   class="easyui-linkbutton" onclick="saveKeyWordWeight()" style="width: 80px">生成</a>
                <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
                   class="easyui-linkbutton" onclick="closeKeyWordDialog()"
                   style="width: 80px">取消</a>
            </div>

        </form>
    </div>


    <div id="enter_benchmarking_sty" class="easyui-dialog" title="添加亚马逊对标商品数据"
         data-options="modal:true"
         style="width: 344px; height: 220px;">
        <form id="form_benchmarking" action="#" onsubmit="return false" style="margin-left: 30px;">
            <br>
            <table>
                <tr>
                    <td>商品PID:</td>
                    <td><input id="add_goods_pid" type="text" value=""
                               readonly="readonly"/></td>
                </tr>
                <tr>
                    <td>ASIN码:</td>
                    <td><input id="add_benchmarking_pid" type="text" value=""/></td>
                </tr>
                <tr>
                    <td>亚马逊价格:</td>
                    <td><input id="add_benchmarking_price" type="text" value=""/></td>
                </tr>
                <tr></tr>
                <tr>
                    <td colspan="2">
                        <div style="text-align: center;">
                            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
                               class="easyui-linkbutton" onclick="saveBenchmarking()" style="width: 80px">保存</a>
                            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
                               class="easyui-linkbutton" onclick="closeBenchmarking()"
                               style="width: 80px">取消</a>
                        </div>
                    </td>
                </tr>
            </table>

        </form>
    </div>

    <div id="pic_dlg" class="easyui-dialog" title="上传橱窗图"
         data-options="modal:true"
         style="width: 460px; height: 330px; padding: 10px;">
        <form id="uploadFileForm" method="post" enctype="multipart/form-data">
            <input type="hidden" name="pid" value="${goods.pid}"/>
            <div style="margin-bottom: 20px">
                上传类型:<select id="type_select" name="type" class="easyui-combobox"
                             style="width: 360px;" placeholder="请选择上传类型">
            </select>
            </div>
            <div style="margin-bottom: 20px">
                图&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;片:<input
                    id="local_picture" name="uploadfile" class="easyui-filebox"
                    data-options="prompt:'请选择一张分辨率大于400*200的图片...'"
                    style="width: 360px">
            </div>
            <div style="margin-bottom: 20px">
                网络链接:<input id="net_url" name="neturl" class="easyui-textbox"
                            style="width: 360px; height: 100px;"
                            data-options="multiline:true,prompt:'请输入图片链接'"> <br>
                <span style="color: red; margin-left: 50px;">多个链接请用&nbsp;<b>英文分号(;)</b>&nbsp;分开,图片分辨率需大于400*200
					</span>
            </div>
        </form>

        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="uploadTypePicture('${goods.pid}')" style="width: 80px">确认上传</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeUploadDialog()"
               style="width: 80px">关闭</a>
        </div>
    </div>

    <div id="similar_dlg" class="easyui-dialog" title="添加相似商品"
         data-options="modal:true"
         style="width: 460px; height: 280px; padding: 10px;">
        <br>
        当前商品PID:<input type="text" id="main_pid" value="${goods.pid}" disabled="disabled"/>
        <br>
        <span><b style="font-size: 16px; color: red;">请输入相似商品PID，多个请用英文分号(;)隔开</b></span>
        <br>
        <!-- 相似商品PID:<input type="text" id="similar_pids" value="" placeholder="请输入相似商品PID" /> -->
        相似商品PID:<textarea id="similar_pids" style="width: 300px; height: 88px;"></textarea>
        <br><br>

        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="addSimilarGoods('${goods.pid}')" style="width: 80px">保存</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeSimilarGoodsDialog()"
               style="width: 80px">关闭</a>
        </div>
    </div>

    <div id="review_dlg" class="easyui-dialog" title="添加评论"
         data-options="modal:true"
         style="width: 460px; height: 320px; padding: 10px;">
        <br>
        评论:<textarea id="review_remark" style="width: 300px; height: 88px;"></textarea><br>
        <br>
        <select class="easyui-combobox" name="review_score" id="review_score" style="width:300px;"
                data-options="label:'分数:',panelHeight:'auto'">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
        </select>
        <br><br>
        <select class="easyui-combobox" name="country" id="country" style="width:300px;" data-options="label:'国家:',Height:'2000px',valueField: 'country',
                    textField: 'country', value:'country',selected:true,
                    url: '/cbtconsole/warehouse/getAllZone',
                    method:'get'">
        </select>
        <br><br>

        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="addReviewRemark('${goods.pid}')" style="width: 80px">保存</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeRemark()"
               style="width: 80px">关闭</a>
        </div>
    </div>

    <div id="update_review_dlg" class="easyui-dialog" title="编辑评论"
         data-options="modal:true"
         style="width: 460px; height: 360px; padding: 10px;">
        <br>
        <input id="oldCreateTime" type="hidden">
        <input id="goods_pid" type="hidden">
        <input id="goods_id" type="hidden">
        评论:<textarea id="edit_remark" style="width: 300px; height: 88px;"></textarea><br>
        <br>
        <select class="easyui-combobox" name="edit_score" id="edit_score" style="width:300px;"
                data-options="label:'分数:',panelHeight:'auto'">
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
        </select>
        <br><br>
        <select class="easyui-combobox" name="update_flag" id="update_flag" style="width:300px;"
                data-options="label:'是否显示:',panelHeight:'auto'">
            <option value="0">显示</option>
            <option value="1">不显示</option>
        </select>
        <br><br>
        <select class="easyui-combobox" name="editcountry" id="editcountry" style="width:300px;" data-options="label:'国家:',Height:'2000px',valueField: 'country',
                    textField: 'country', value:'country',selected:true,
                    url: '/cbtconsole/warehouse/getAllZone',
                    method:'get'">
        </select>
        <br><br>

        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="updateReviewRemark()" style="width: 80px">保存</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeUpdateRemark()"
               style="width: 80px">关闭</a>
        </div>
    </div>


    <div id="size_info_en_dlg" class="easyui-dialog" title="编辑文字尺码表"
         data-options="modal:true"
         style="width: 520px; height: 440px; padding: 10px;">
        <br>
        <textarea id="size_info_en_text" style="width: 460px; height: 300px;">${goods.sizeInfoEn}</textarea>
        <br><br>

        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="addSizeInfoEn()" style="width: 80px">保存</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeSizeInfoEnDialog()"
               style="width: 80px">关闭</a>
        </div>
    </div>


    <div id="set_new_aliPid_dlg" class="easyui-dialog" title="修改对标信息"
         data-options="modal:true"
         style="width: 280px; height: 200px; padding: 10px;">
        <br>
        <table>
            <tr>
                <td>AliPid:</td>
                <td><input id="new_ali_pid" type="text" style="height: 20px;"/></td>
            </tr>
            <tr></tr>
            <tr>
                <td>AliPrice:</td>
                <td><input id="new_ali_price" type="text" style="height: 20px;"/></td>
            </tr>
        </table>
        <br>

        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="setNewAliPidInfo('${goods.pid}')" style="width: 80px">保存</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeNewAliPidDlg()"
               style="width: 80px">关闭</a>
        </div>
    </div>


    <div class="allMain">
        <div class="s_top">
				<span class="s_last2"> <label for="query_pid"><b>PID:</b></label>
					<input id="query_pid" value="${goods.pid}" placeholder="请输入PID"/>
					<i class="s_btn" onclick="doQuery()">查询</i>
				</span>
            <span class="s_btn" onclick="doSaveDetalis('${goods.pid}',0,${isSoldFlag})">保存</span>

            <span class="s_btn" onclick="doSaveDetalis('${goods.pid}',1,${isSoldFlag})">保存并发布</span>
            <span class="s_btn" onclick="setGoodsValid('${goods.pid}',-1)">下架该商品</span>


            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <%--<span class="s_btn" >保存并发布</span>
                <span class="s_btn" >下架该商品</span>
                <span class="s_btn" title="无需修改时点击检查通过" >检查通过</span>--%>
            <c:if test="${goods.describeGoodFlag == 0}">
                <%--<span class="s_btn" onclick="setGoodsFlagByPid('${goods.pid}',0,0,0,1,0,0,0)">设置描述很精彩</span>--%>
                <span class="s_btn" onclick="setGoodsDescWithHotType('${goods.pid}')">描述很精彩</span>
            </c:if>
                <%--<span class="s_last">*点击后数据直接更新线上</span>--%>
            <span class="s_btn" onclick="setNoBenchmarking('${goods.pid}',${goods.finalWeight})">标识非对标商品</span>
            <span class="s_btn" onclick="setGoodsFlagByPid('${goods.pid}',0,0,0,0,1,0,0)">标识永不下架</span>
            <c:if test="${goods.promotionFlag == 0}">
                <span class="s_btn" onclick="setGoodsFlagByPid('${goods.pid}',0,0,0,0,0,0,1)">标识促销商品</span>
            </c:if>

            <c:if test="${goods.overSeaFlag == 0}">

            </c:if>
            <span class="s_btn" onclick="openOverSeaDialog(${goods.overSeaFlag})">海外仓</span>
            <c:if test="${goods.searchable == 0}">
                <span class="s_btn" onclick="setSearchable('${goods.pid}', 1)">可搜索</span>
            </c:if>
            <c:if test="${goods.searchable > 0}">
                <span class="s_btn" onclick="setSearchable('${goods.pid}', 0)">不可搜索</span>
            </c:if>

            <c:if test="${salable == 0}">
                <span class="s_btn" onclick="setSalable('${goods.pid}', 1)">设置美加不可售卖</span>
            </c:if>
            <c:if test="${salable > 0}">
                <span class="s_btn" onclick="setSalable('${goods.pid}', 0)">取消美加不可售卖</span>
            </c:if>

            <c:if test="${goods.topSort > 0}">
                <span class="s_btn" onclick="setTopSort('${goods.pid}', ${goods.topSort})">搜索排序(值:${goods.topSort})</span>
            </c:if>
            <c:if test="${goods.topSort < 1}">
                <span class="s_btn" onclick="setTopSort('${goods.pid}', -1)">搜索排序</span>
            </c:if>


        </div>
        <div class="all_s">
            <input type="hidden" id="delete_type_ids" value=""/>
            <div class="s_l">
                <p class="s_tit">
                    <label class="label"> 产品名称：<input id="import_enname"
                                                      type="text" class="s_txt" value="${goods.enname}"/>
                    </label>
                </p>
                <div class="s_box">
                    <div class="detail_goods">
                        <div class="goods_img">
                            <div class="init_pic">
                                <img src="${goods.showMainImage}" class="init_img">
                                <div class="mengban"></div>
                                <div class="lvjing"
                                     style="display: none; left: 141px; top: 30px;"></div>
                            </div>
                            <div class="big_pic" style="display: none;">
                                <img src="/cbtconsole/img/init_pic.png" class="big_img"
                                     style="left: -282px; top: -60px;">
                            </div>
                            <div class="pic_list">
                                <p class="prev">
                                    <span class="prev_arrow"></span>
                                </p>
                                <ul class="ul_pic">
                                    <input type="hidden" id="first_li" style="display: none" value="99"/>
                                    <c:forEach items="${showimgs}" var="imgBean"
                                               varStatus="imgIndex">
                                        <li class="li_pic"><img src="${imgBean}"></li>
                                    </c:forEach>
                                </ul>
                                <p class="next">
                                    <span class="next_arrow"></span>
                                </p>
                            </div>
                            <div class="clear_box">
                                <span title="添加橱窗图" class="clear_clo" onclick="showDialog()">添加</span> <span
                                    title="删除所选中的橱窗图" id="delete_pic" class="clear_clo">删除</span> <span
                                    class="clear_clo" title="选中设置为主图" onclick="setMainImg()">设置封面图</span>
                            </div>
                        </div>
                        <div class="goods_detail">

                            <div class="goods_top">
                                <input class="s_btn" type="button" value="移除选中规格" onclick="removeTypeId()"
                                       title="请选中规格，点击删除按钮"/>
                                <span style="color: red;">*请选中规格，点击删除按钮，删除完成后请点击保存</span>
                                <c:set value="" var="typeName"></c:set>
                                <c:set value="${fn:length(showtypes)}" var="typeLength"></c:set>
                                <c:forEach items="${showtypes}" var="typeBean"
                                           varStatus="typeIndex">
                                <c:if test="${typeName != typeBean.lableType }">
                                <c:if test="${typeName !=''}">
                                </ul>
                            </div>
                            </c:if>
                            <div class="goods_type_${typeBean.type}">
                                <p class="goods_color">${typeBean.type}:</p>
                                <c:if test="${typeBean.img!='null' && typeBean.img !=''}">
                                <ul class="ul_color">
                                    <!-- </ul> -->
                                    </c:if>
                                    <c:if test="${typeBean.img=='null' || typeBean.img ==''}">
                                    <ul class="entype_ul ul_size">
                                        <!-- </ul> -->
                                        </c:if>
                                        <c:set value="${typeBean.lableType}" var="typeName"></c:set>
                                        </c:if>

                                        <li
                                                class="${typeBean.img=='null'||typeBean.img==''?'li_size':'li_color'}">
                                            <c:if test="${typeBean.img!='null' && typeBean.img !=''}">
                                                <input type="hidden" id="${typeBean.id}"/>
                                                <img <%--onclick="addDeleteTypeId('${typeBean.id}')"--%>
                                                     class="img_limit"
                                                     src="${typeBean.img}" alt="${typeBean.value}"
                                                     title="${typeBean.value}"/>
                                            </c:if> <c:if test="${typeBean.img=='null' || typeBean.img ==''}">
                                            <%--<em id="${typeBean.id}" onclick="addDeleteTypeId('${typeBean.id}')"
                                                title="${typeBean.value}">${typeBean.value}</em>--%>
                                            <input id="${typeBean.id}" class="inp_set" title="${typeBean.value}"
                                                   value="${typeBean.value}"
                                                   onblur="saveEditValue('${typeBean.id}',this.value)"/>
                                        </c:if>
                                        </li>
                                        <c:if test="${typeIndex.index== typeLength-1}">
                                    </ul>
                            </div>
                            </c:if>
                            </c:forEach>


                                <%-- <div class="goods_p">
                                    <p class="goods_color">免邮价格:</p>
                                    <p class="ul_size">
                                        <input type="text" class="pr_txt" value="${goods.fprice}"
                                            disabled="disabled" /> <span class="goods_cur">$/ piece
                                            &nbsp;&nbsp;<label class="lb_style">*不允许修改价格</label>
                                        </span>
                                    </p>
                                </div> --%>

                            <input id="range_price_id" value="${goods.rangePrice}" type="hidden"/>
                            <c:if test="${not empty goods.rangePrice}">
                                <div class="goods_p">
                                    <p class="goods_color">单规格价(${isSoldFlag > 0 ? '免邮':'非免邮'}):</p>
                                    <table border="1" cellspacing="0" cellpadding="0" class="table_style">
                                        <tr>
                                            <c:forEach var="type_name" items="${typeNames}"
                                                       varStatus="nameIndex">
                                                <td id="type_name_${type_name.key}">${type_name.value}</td>
                                            </c:forEach>
                                            <td id="type_name_choose">单价<br> <input type="checkbox"
                                                                                    onclick="allSamePrice(this)"/>全部相同
                                            </td>
                                        </tr>

                                        <c:forEach var="sku_bean" items="${showSku}" varStatus="skuIndex">
                                            <tr>
                                                <c:forEach var="tp_ar" items="${fn:split(sku_bean.skuAttrs,';')}">
                                                    <td id="combine_id_${fn:split(tp_ar,'@')[0]}">${fn:split(tp_ar,'@')[2]}</td>
                                                </c:forEach>
                                                <td><input class="inp_style inp_price" title="单击可进行编辑"
                                                           id="${sku_bean.ppIds}" value="${sku_bean.price}"/></td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>

                            </c:if>
                            <c:if test="${empty goods.rangePrice}">
                                <c:if test="${isSoldFlag > 0}">
                                    <c:if test="${not empty goods.feeprice}">
                                        <input id="feeprice_id" value="${goods.feeprice}" type="hidden"/>
                                        <div class="goods_p">
                                            <p class="goods_color">区间价格(免邮):</p>

                                            <table border="1" cellspacing="0" cellpadding="0" class="table_style">
                                                <tr>
                                                    <td>商品数量</td>
                                                    <td>对应价格($)</td>
                                                </tr>
                                                <c:forEach var="fee_price" items="${fn:split(goods.feeprice,',')}"
                                                           varStatus="feeIndex">
                                                    <tr>
                                                            <%--<td id="wprice_num_${wpicIndex.index}">${fn:split(w_pic,'@')[0]}</td>--%>
                                                        <td><input type="text" id="fee_price_num_${feeIndex.index}"
                                                                   class="inp_style fee_price_inp" title="单击可进行编辑"
                                                                   value="${fn:split(fee_price,'@')[0]}"/></td>
                                                        <td><input type="text" id="fee_price_val_${feeIndex.index}"
                                                                   class="inp_style fee_price_inp" title="单击可进行编辑"
                                                                   value="${fn:split(fee_price,'@')[1]}"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </table>
                                        </div>
                                    </c:if>
                                    <c:if test="${empty goods.feeprice}">
                                        <input id="wprice_id" value="${goods.wprice}" type="hidden"/>
                                        <c:if test="${not empty goods.wprice}">
                                            <div class="goods_p">
                                                <p class="goods_color">区间价格(免邮):</p>

                                                <table border="1" cellspacing="0" cellpadding="0" class="table_style">
                                                    <tr>
                                                        <td>商品数量</td>
                                                        <td>对应价格($)</td>
                                                    </tr>
                                                    <c:forEach var="w_pic" items="${fn:split(goods.wprice,',')}"
                                                               varStatus="wpicIndex">
                                                        <tr>
                                                                <%--<td id="wprice_num_${wpicIndex.index}">${fn:split(w_pic,'@')[0]}</td>--%>
                                                            <td><input type="text" id="wprice_num_${wpicIndex.index}"
                                                                       class="inp_style wprice_inp" title="单击可进行编辑"
                                                                       value="${fn:split(w_pic,'@')[0]}"/></td>
                                                            <td><input type="text" id="wprice_val_${wpicIndex.index}"
                                                                       class="inp_style wprice_inp" title="单击可进行编辑"
                                                                       value="${fn:split(w_pic,'@')[1]}"/></td>
                                                        </tr>
                                                    </c:forEach>
                                                </table>
                                            </div>
                                        </c:if>

                                        <c:if test="${empty goods.wprice}">
                                            <div class="goods_p">
                                                <p class="goods_color">商品价格:</p>
                                                <input id="goods_price_id" type="number" class="pr_txt"
                                                       value="${goods.price}" readonly="readonly"/>
                                                <b style="font-size: 16px;color: red;">*(此价格不可修改)</b>
                                            </div>
                                        </c:if>
                                    </c:if>

                                </c:if>
                                <c:if test="${isSoldFlag == 0}">
                                    <input id="wprice_id" value="${goods.wprice}" type="hidden"/>
                                    <c:if test="${not empty goods.wprice}">
                                        <div class="goods_p">
                                            <p class="goods_color">区间价格(非免邮):</p>

                                            <table border="1" cellspacing="0" cellpadding="0" class="table_style">
                                                <tr>
                                                    <td>商品数量</td>
                                                    <td>对应价格($)</td>
                                                </tr>
                                                <c:forEach var="w_pic" items="${fn:split(goods.wprice,',')}"
                                                           varStatus="wpicIndex">
                                                    <tr>
                                                            <%--<td id="wprice_num_${wpicIndex.index}">${fn:split(w_pic,'@')[0]}</td>--%>
                                                        <td><input type="text" id="wprice_num_${wpicIndex.index}"
                                                                   class="inp_style wprice_inp" title="单击可进行编辑"
                                                                   value="${fn:split(w_pic,'@')[0]}"/></td>
                                                        <td><input type="text" id="wprice_val_${wpicIndex.index}"
                                                                   class="inp_style wprice_inp" title="单击可进行编辑"
                                                                   value="${fn:split(w_pic,'@')[1]}"/></td>
                                                    </tr>
                                                </c:forEach>
                                            </table>
                                        </div>
                                    </c:if>

                                    <c:if test="${empty goods.wprice}">
                                        <div class="goods_p">
                                            <p class="goods_color">商品价格:</p>
                                            <input id="goods_price_id" type="number" class="pr_txt"
                                                   value="${goods.price}" readonly="readonly"/>
                                            <b style="font-size: 16px;color: red;">*(此价格不可修改)</b>
                                        </div>
                                    </c:if>
                                </c:if>

                            </c:if>

                            <div class="goods_p">
                                <p class="ul_size">
                                        <%--<a href="/cbtconsole/editc/querySkuByPid?pid=${goods.pid}" target="_blank">编辑sku重量</a>--%>
                                    <input type="button" class="s_btn" value="编辑sku重量"
                                           onclick="openSkuEdit('${goods.pid}')"/>
                                </p>
                            </div>
                            <div class="goods_p">
                                <p class="goods_color">bizPrice:</p>
                                <p class="ul_size">
                                    <span class="goods_cur"><input class="pr_txt" id="biz_price"
                                                                   value="${goods.fpriceStr}"/></span>
                                </p>
                            </div>
                            <div class="goods_p">
                                <p class="goods_color">重量:</p>
                                <p class="ul_size">
                                    <span class="goods_cur">${goods.finalWeight}</span><em>KG</em>
                                    <input type="button" value="修改重量" class="s_btn"
                                           onclick="beginUpdateWeight('${goods.pid}',${goods.finalWeight})"/>
                                    <c:if test="${goods.isWeigthZero > 0}">
                                        <b style="color: red;font-size: 18px;">*抓取1688重量为空</b>
                                    </c:if>
                                    &nbsp;&nbsp;&nbsp;<input type="button" value="确认重量没有问题" class="s_btn"
                                                             onclick="updateWeightFlag('${goods.pid}',this)"/>
                                    <c:if test="${goods.weightFlag > 0}">
                                        <b style="font-size: 16px;color: red;">*重量超过类别上下限</b>
                                    </c:if>
                                </p>
                            </div>
                            <div class="goods_p">
                                <p class="goods_color">体积重量:</p>
                                <p class="ul_size">
                                    <span id="goods_volum_weight" class="goods_cur">${goods.volumeWeight}</span><em>KG</em>
                                    <input type="button" value="修改体积重量" class="s_btn"
                                           onclick="updateVolumeWeight('${goods.pid}',${goods.volumeWeight})"/>
                                </p>
                            </div>
                            <div class="goods_p">
                                <p class="goods_color">加价率:</p>
                                <p class="ul_size">
                                    <span class="goods_cur">
                                        原始[${goods.addPriceLv}]
                                    </span>
                                    <c:if test="${goods.editProfit > 0}">
                                        修改[${goods.editProfit}]
                                    </c:if>
                                    <c:if test="${goods.lockProfit > 0}">
                                        <b style="color:red;">(已锁定)</b>
                                    </c:if>
                                    <c:if test="${goods.lockProfit == 0}">
                                        <input type="button" value="修改利润率" class="s_btn"
                                               onclick="beforeProfit('${goods.pid}',0,${goods.addPriceLv})"/>
                                        <input type="button" value="锁定利润率" class="s_btn"
                                               onclick="beforeProfit('${goods.pid}',1,${goods.addPriceLv})"/>
                                    </c:if>
                                </p>
                            </div>
                            <div class="goods_p">
                                <p class="goods_color">单位:</p>
                                <p class="ul_size">
                                    <input id="import_sell_util" type="text" class="pr_txt" value="${goods.sellUnit}"/>
                                    <span>例子：lot (100 pieces/lot)</span>
                                </p>
                            </div>
                            <div class="goods_p">
                                <p class="goods_color">属性:</p>
                                <div class="ul_size ul_size_p">
                                    <table id="attribute_table" border="1px" bordercolor="#ccc" cellspacing="0px"
                                           style="border-collapse: collapse" width="100%" id="attribute_table">
                                        <c:set value="${fn:length(showattribute)}" var="itemLength"></c:set>
                                        <tr>
                                            <c:forEach var="item" items="${showattribute}"
                                                       varStatus="itemIndex">
                                            <td width="32%"><input type="text" class="inp_style" title="单击可进行编辑"
                                               id="sku_id_${item.key}" name="sku_key_val" value="${item.value}" onchange="addSkuLog('sku_id_${item.key}')"/>
                                            </td>
                                            <c:if
                                                    test="${(itemIndex.index+1) % 3 == 0 || itemIndex.index == itemLength-1}">
                                        </tr>
                                        </c:if>
                                        </c:forEach>
                                    </table>
                                    <br><input class="s_btn" type="button" value="添加属性" onclick="addGoodsAttribute();"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="s_r">
            <div class="table_su">
                <div>
                    <c:if test="${goods.goodsState == 5}">
                        <b style="color: red;font-size: 26px;">产品待发布</b><br>
                    </c:if>
                    <c:if
                            test="${goods.fromFlag > 0}">
                        <br>
                        <b style="font-size: 16px;color: red;">产品来源:${goods.fromFlagDesc}</b><br>
                    </c:if>
                    <c:if
                            test="${goods.isEdited == 1 || goods.isEdited == 2}">
                        <b style="font-size: 16px;"> 编辑状态：已编辑</b>
                        <br>
                        <b style="font-size: 16px;">编辑时间：${goods.updatetime}</b>
                        <br>
                        <b style="font-size: 16px;">编辑人：${goods.admin}</b>
                        <br>
                    </c:if> <c:if test="${goods.promotionFlag >0}">
                    <br>
                    <b style="font-size: 16px;color: red;">促销商品</b>
                </c:if><c:if test="${goods.isAbnormal >0}">
                    <br>
                    <b style="font-size: 16px;">数据状态:${goods.abnormalValue}</b>
                </c:if> <c:if test="${goods.describeGoodFlag > 0}">
                    <br>
                    <c:if test="${empty describeGoodFlagStr}">
                        <b style="font-size: 16px;color: red;">描述很精彩标记</b>
                    </c:if>
                    <c:if test="${not empty describeGoodFlagStr}">
                        <b style="font-size: 16px;color: red;">描述很精彩:(${describeGoodFlagStr})</b>
                    </c:if>
                </c:if>
                    <c:if test="${salable > 0}">
                        <br><b style="font-size: 16px;color: red;">美加不可售卖标记</b>
                    </c:if>
                    <c:if test="${not empty goodsOverSeaList && fn:length(goodsOverSeaList) > 0}">
                    <br>
                    <div style="font-size: 20px;background-color: #a2f387" >
                        <table border="1">
                            <caption>海外仓设置</caption>
                            <thead>
                            <tr>
                                <td>国家</td>
                                <td>是否支持</td>
                                <td>设置人</td>
                                <td>设置时间</td>
                            </tr>
                            </thead>
                            <tbody>

                            <c:forEach items="${goodsOverSeaList}" var="overSea">
                                <tr>
                                    <td>
                                            ${overSea.countryName}
                                    </td>
                                    <td>
                                        <c:if test="${overSea.isSupport > 0}">
                                            <b style="color: #2f5aff">支持</b>
                                        </c:if>
                                        <c:if test="${overSea.isSupport == 0}">
                                            <b style="color: red">不支持</b>
                                        </c:if>
                                    </td>
                                    <td>${overSea.adminName}</td>
                                    <td>${overSea.createTime}</td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:if><c:if test="${goods.isBenchmark >0}">
                    <br>
                    <b style="font-size: 16px;">货源对标情况:${goods.isBenchmark ==1 ? '精确对标':'近似对标'}</b>
                </c:if> <c:if test="${goods.bmFlag >0}">
                    <br>
                    <b style="font-size: 16px;">人为对标货源:${goods.bmFlag ==1 ? '是':'不是'}</b>
                </c:if> <%--<c:if test="${goods.sourceProFlag >0}">
                <br>
                <b style="font-size: 16px;">货源属性:${goods.sourceProValue}</b>
            </c:if>--%> <c:if test="${goods.soldFlag >0}">
                    <br>
                    <b style="font-size: 16px;">是否售卖:${goods.soldFlag ==1 ? '卖过':'没有卖过'}</b>
                </c:if> <c:if test="${goods.isSoldFlag >= 0}">
                    <br>
                    <b style="font-size: 16px;">是否免邮:${goods.isSoldFlag > 0 ? '免邮':'非免邮'}</b>
                </c:if> <c:if test="${goods.addCarFlag >0}">
                    <br>
                    <b style="font-size: 16px;">是否加入购物车:${goods.carValue}</b>
                </c:if> <c:if test="${goods.priorityFlag >0}">
                    <br>
                    <b style="font-size: 16px;">商品优先级:${goods.priorityFlag ==1 ? '核心':'非核心'}</b>
                </c:if>
                    <br>
                    <b style="font-size: 16px;">点击数:${goods.clickNum}</b>
                        <%--<c:if test="${goods.sourceUsedFlag >0}">
                            <br>
                            <b style="font-size: 16px;">OCR货源可用度:${goods.sourceUsedFlag ==2 ? '描述很精彩':'可用'}</b>
                        </c:if>
                            <c:if test="${goods.sourceUsedFlag  == 0}">
                                <br>
                                <b style="font-size: 16px;">OCR货源可用度:不可用</b>
                            </c:if> <c:if test="${goods.ocrMatchFlag >0}">
                                <br>
                                <b style="font-size: 16px;">OCR判断:${goods.ocrMatchValue}</b>
                            </c:if> --%>
                    <c:if test="${goods.rebidFlag >0}">
                        <br>
                        <b style="font-size: 16px;">是否重新对标:是</b>
                    </c:if> <c:if test="${goods.goodsState >0}">
                    <br>
                    <b style="font-size: 16px;">发布状态:${goods.goodsStateValue}</b>
                </c:if><c:if test="${goods.valid == 0}">
                    <c:if test="${not empty goods.offReason}">
                        <br>
                        <b style="font-size: 16px;color: red;">硬下架原因:${goods.offReason}(人为下架)</b>
                    </c:if>
                    <c:if test="${empty goods.offReason && not empty goods.unsellAbleReasonDesc}">
                        <br>
                        <b style="font-size: 16px;color: red;">硬下架原因:${goods.unsellAbleReasonDesc}(系统下架)</b>
                    </c:if>
                    </c:if>
                    <c:if test="${goods.valid == 2}">
                        <br>
                        <b style="font-size: 16px;color: red;">软下架原因:${goods.unsellAbleReasonDesc}</b>
                    </c:if><c:if test="${not empty goods.publishtime}">
                    <br>
                    <b style="font-size: 16px;">发布时间:${goods.publishtime}</b>
                </c:if> <c:if test="${not empty goods.updatetime}">
                    <br>
                    <b style="font-size: 16px;">更新时间:${goods.updatetime}</b>
                </c:if>
                    <c:if test="${goods.infringingFlag > 0}">
                        <br>
                        <b style="color: red;font-size: 16px;">${list.infringingFlag == 1 ? '此商品侵权':'8月人为精选'}</b>
                    </c:if>
                    <c:if test="${goods.matchSource == 4}">
                        <br>
                        <b style="color: red;font-size: 16px;">对标亚马逊商品</b>
                        <br>
                        <b style="color: red;font-size: 16px;">ASIN码:${goods.aliGoodsPid}</b>
                        <br>
                        <b style="color: red;font-size: 16px;">亚马逊价格:${goods.aliGoodsPrice}</b>
                    </c:if>
                    <c:if test="${goods.weightNotFlag > 0}">
                        <br>
                        <b style="font-size: 16px;">标注:重量不合理</b>
                    </c:if>
                    <c:if test="${goods.uglyFlag > 0}">
                        <br>
                        <b style="font-size: 16px;">标注:难看</b>
                    </c:if>
                    <c:if test="${goods.repairedFlag > 0}">
                        <br>
                        <b style="font-size: 16px;color: green;">标注:产品已修复</b>
                    </c:if>
                    </span> </div>
                <br>
                <div>
                    <span class="s_btn" onclick="addKeyWordWeight('${goods.shopId}','${goods.catid1}','${goods.pid}')">添加关键词重量</span>
                    &nbsp;&nbsp;<span class="s_btn" onclick="addBenchmarking('${goods.pid}')">亚马逊对标数据</span>
                    &nbsp;&nbsp;<span class="s_btn"
                                      onclick="setGoodsFlagByPid('${goods.pid}',0,1,0,0,0,0,0)">难看中文多</span>
                    &nbsp;&nbsp;<span class="s_btn"
                                      onclick="setGoodsFlagByPid('${goods.pid}',1,0,0,0,0,0,0)">重量不合理</span>
                    &nbsp;&nbsp;<span class="s_btn"
                                      onclick="setGoodsFlagByPid('${goods.pid}',0,0,0,0,0,1,0)">不具备独特性可舍弃</span>
                    &nbsp;&nbsp;<span class="s_btn" onclick="openEditLog('${goods.pid}')">查看编辑日志</span>
                    <c:if test="${goods.isBenchmark == 1 && goods.bmFlag == 1}">
                        <br><span class="s_btn" onclick="setGoodsFlagByPid('${goods.pid}',0,0,1,0,0,0,0)">对标不准确</span>
                        &nbsp;&nbsp;<span class="s_btn" onclick="setGoodsFlagByPid('${goods.pid}',0,0,2,0,0,0,0)">对标准确</span>
                    </c:if>
                    <c:if test="${goods.isBenchmark == 2}">
                        <br><span class="s_btn" onclick="setGoodsFlagByPid('${goods.pid}',0,0,1,0,0,0,0)">对标不准确</span>
                        &nbsp;&nbsp;<span class="s_btn" onclick="setGoodsFlagByPid('${goods.pid}',0,0,2,0,0,0,0)">对标准确</span>
                    </c:if>
                    <br><span class="s_btn"
                              onclick="beforeSetAliInfo('${goods.pid}',${goods.isBenchmark},${goods.bmFlag},'${goods.aliGoodsPid}')">重新录入对标</span>
                    &nbsp;&nbsp;<span class="s_btn" onclick="setGoodsRepairedByPid('${goods.pid}')">产品已修复</span>
                    <span class="s_btn" onclick="setNewCatid('${goods.pid}','${goods.catid1}')">设置新类别</span>
                    <br>
                    <button class="s_btn" onclick="openReviewDiv()">添加产品评价</button>
                    &nbsp;&nbsp;&nbsp;
                    <span class="s_btn" title="无需修改时点击检查通过" onclick="setGoodsValid('${goods.pid}',1)">检查通过</span>
                    &nbsp;&nbsp;&nbsp;
                    <a target="_blank"
                       href="http://192.168.1.29:8280/cbtconsole/customerServlet?action=findAllTaoBaoInfo&className=PictureComparisonServlet&aliPid=${goods.aliGoodsPid}&ylbbPid=${goods.pid}"
                       style="color: #ff0000;">重新对标</a>

                </div>
                <br>
                <div>

                    <a target="_blank" href="https://detail.1688.com/offer/${goods.pid}.html">1688原链接</a>
                    &nbsp;&nbsp;&nbsp;
                    <a target="_blank"
                                          href="${goods.onlineUrl}">线上链接</a>
                    &nbsp;&nbsp;&nbsp;<a target="_blank"
                                          href="${goods.aliGoodsUrl}">速卖通原链接</a>
                    <c:if test="${not empty shopId}">
                        &nbsp;&nbsp;&nbsp;
                        <a target="_blank"
                           href="/cbtconsole/supplierscoring/supplierproducts?shop_id=${shopId}">产品店铺链接</a>
                    </c:if>
                </div>
                <br>
                <div style="font-size: 16px;background-color: #a2f387;">
                    <h3 style="text-align: center">1688和ali数据详情</h3>
                    <span><b>1688原始价: ${goods.wholesalePrice}</b></span><br>
                    <span><b>抓取1688产品重量: ${goods.weight1688}</b></span>
                    <br><br>
                    <c:if test="${goods.bmFlag == 1 && goods.isBenchmark == 1}">
                        <span><b>速卖通对标价: ${goods.crawlAliPrice}</b></span><br>
                        <span><b>对标价的抓取时间: ${goods.crawlAliDate}</b></span>
                    </c:if>
                </div>
            </div>


        </div>

        <div class="s_bot">
            <div>
                <span style="font-size: 22px; color: red; margin-top: 15px;">相似商品：</span>
                <span class="s_btn_2" onclick="openSimilarGoodsDialog()">添加相似商品</span>
            </div>
            <div>
                <span style="font-size: 22px; color: red; margin-top: 15px;">商品评论:</span><br>
                <c:forEach items="${reviewList}" var="review">
                    <span style="font-size: 15px;  margin-top: 15px;">评论人:${review.review_name};评论时间:${review.createtime};国家:${review.country};评论内容:${review.review_remark};评分:${review.review_score};${review.review_flag};编辑时间:${review.updatetime}</span>
                    <span id="review_remark_${review.aliId}" style="display: none">${review.review_remark}</span>
                    <button onclick="openEditReview('${review.aliId}','${review.country}','${review.review_score}','${review.review_flag}','${review.createtime}','${review.goods_pid}');">
                        编辑
                    </button>
                    <br>
                </c:forEach>

            </div>

            <div class="goods_img_2">
                <div class="pic_list_2">
                    <ul id="similar_goods_ul" class="ul_pic_2">
                    </ul>
                </div>

            </div>


        </div>

        <c:if test="${not empty ocrSizeInfo1}">
            <div class="s_bot">
                <textarea rows="" cols="" style="width: 500px;height: 250px;">${ocrSizeInfo1}</textarea>
                <textarea rows="" cols="" style="width: 500px;height: 250px;">${ocrSizeInfo2}</textarea>
                <textarea rows="" cols="" style="width: 500px;height: 250px;">${ocrSizeInfo3}</textarea>
            </div>
        </c:if>


        <div class="s_bot">
            <div style="float:left;width:75%;" id="word_info_div">
                    ${goods.sizeInfoEn}
            </div>
            <div style="width: 20%; margin: 0 auto;float: right;">
                    <%--<span id="word_size_info" class="s_btn" onclick="deleteWordSizeInfo(${goods.pid})">删除文字尺码表</span>--%>
                <c:if test="${not empty goods.sizeInfoEn}">
                    <span id="size_info_en" class="s_btn" onclick="updateWordSizeInfo(1)">修改文字尺码表</span>
                </c:if>
                <c:if test="${empty goods.sizeInfoEn}">
                    <span id="size_info_en" class="s_btn" onclick="updateWordSizeInfo(0)">新增文字尺码表</span>
                </c:if>
            </div>

        </div>


        <div class="s_bot">
            <h1 style="text-align: center; color: red;">请不要全盘copy，请去掉所有有“品牌”、“店名”的图，请去掉所有有“aliexpress”字样的图</h1>

            <button class="s_btn" style="width: 180px;"
                    onclick="beforeDeleteMd5('${goods.pid}','${goods.shopId}')">删除同店铺相同MD5图片
            </button>&nbsp;&nbsp;&nbsp;
            <span class="s_btn"
                  onclick="$('#multi_file_dlg').dialog('open');">详情多文件上传</span>
            &nbsp;&nbsp;&nbsp;<span id="change_img_english" class="s_btn"
                  onclick="changeChineseImgToEnglishImg(${goods.pid})">替换图片文字为英文</span>
            &nbsp;&nbsp;&nbsp;<span id="use_ali_goods" class="s_btn"
                  onclick="useAliGoodsDetails(${goods.pid})">使用速卖通详情</span>
            <div class="bot_l">
                <div class="b_left">
                    <h1 style="text-align: center">importE详情编辑框</h1>
                    <input type="hidden" id="goods_savePath" value="${savePath}" name="savePath">
                    <input type="hidden" id="goods_localpath" value="${localpath}" name="localpath">
                    <input type="hidden" id="goods_remotepath" value="${goods.remotpath}" name="remotepath">
                    <textarea id="goods_content" rows="100" style="width: 100%;">${text}</textarea>
                </div>
            </div>

            <div class="bot_r">
                <h1 style="text-align: center; background-color: #5df55d;">速卖通详情</h1>
                <div class="bot_c">
                    <button id="to_left_sp" style="cursor: pointer;"
                            onclick="addToImportE('${goods.pid}')"></button>
                </div>
                <div class="r_box" id="ali_goods_info">${goods.aliGoodsInfo}</div>
            </div>
        </div>
    </div>
</c:if>
</body>
<script type="text/javascript">

    function changeChineseImgToEnglishImg(pid) {
        var img = editorObj.getSelect();
        var imgObj = $(img);
        var url = imgObj[0].src;
        if (url) {
            if (url.indexOf("http") == -1 && url.indexOf("https") == -1) {
                $.messager.alert("提醒", "获取链接地址错误", "info");
            } else {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/editc/changeChineseImgToEnglishImg',
                    data: {
                        "pid": pid,
                        "imgUrl": url
                    },
                    success: function (json) {
                        if (json.ok) {
                            var imgNw = '<img src="'+json.data+'"/>';
                            editorObj.pasteHTML(imgNw);
                        } else {
                            $.messager.alert("提醒", json.message, "error");
                        }
                    },
                    error: function () {
                        $.messager.alert("提醒", "连接服务器失败", "error");
                    }
                });
            }
        } else {
            $.messager.alert("提醒", "请选中一张图片", "error");
        }
    }

    function beforeDeleteMd5(goodsPid, shopId) {
        var img = editorObj.getSelect();
        var imgObj = $(img);
        var url = imgObj[0].src;
        if (url) {
            if (url.indexOf("http") == -1 && url.indexOf("https") == -1) {
                $.messager.alert("提醒", "获取链接地址错误", "info");
            } else {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/editc/queryMd5ByImgUrl',
                    data: {
                        "pid": goodsPid,
                        "shopId": shopId,
                        "url": url
                    },
                    success: function (json) {
                        if (json.ok) {
                            confirmAndDelete(json, goodsPid, url, shopId)
                        } else {
                            $.messager.alert("提醒", json.message, "error");
                        }
                    },
                    error: function () {
                        $.messager.alert("提醒", "执行失败，请重试", "error");
                    }
                });
            }
        } else {
            $.messager.alert("提醒", "请选中一张图片", "error");
        }
    }

    function confirmAndDelete(json, goodsPid, url, shopId) {
        $.messager.confirm('提醒', '当前商品的店铺下存在相同的MD5图片数为' + json.total + ',是否删除?', function (r) {
            if (r) {
                if (json.total > 0) {
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: '/cbtconsole/editc/deleteImgByMd5',
                        data: {
                            "pid": goodsPid,
                            "shopId": shopId,
                            "url": url
                        },
                        success: function (json) {
                            if (json.ok) {
                                $.messager.alert("提醒", "执行成功，页面即将刷新", "info");
                                setTimeout(function () {
                                    window.location.reload();
                                }, 500);
                            } else {
                                $.messager.alert("提醒", json.message, "error");
                            }
                        },
                        error: function () {
                            $.messager.alert("提醒", "执行失败，请重试", "error");
                        }
                    });
                }

            }
        });
    }

    function allSamePrice(obj) {
        if ($(obj).attr("checked")) {
            $(obj).removeAttr("checked");
        } else {
            $.messager.prompt('提示', '请输入价格:', function (is) {
                if (is) {
                    var reg = /(^[-+]?[1-9]\d*(\.\d{1,2})?$)|(^[-+]?[0]{1}(\.\d{1,2})?$)/;
                    if (!reg.test(is)) {
                        $(obj).removeAttr("checked");
                        showMessage('价格必须为正数，最多两位小数！');
                    } else {
                        $(".inp_price").val(is);
                        $(obj).attr("checked", "checked");
                    }
                } else {
                    showMessage('未输入价格或取消输入！');
                    $(obj).removeAttr("checked");
                }
            });
        }
    }

    
    function onSkuclick() {

    }
    function addSkuLog(skuId) {
        skuJson[skuId] = 1;
    }

    function addGoodsAttribute() {
        var len = $("#attribute_table tbody tr:last").find('td').length;
        var tdStr = "";
        if (len == 3) {
            tdStr = '<tr><td width="32%"><input type="text" class="inp_style" title="单击可进行编辑" '
                + 'name="sku_key_val" value=""></td></tr>';
            $("#attribute_table tbody").append(tdStr);
        } else {
            tdStr = '<td width="32%"><input type="text" class="inp_style" title="单击可进行编辑" '
                + 'name="sku_key_val" value=""></td>';
            $("#attribute_table tbody tr:last").append(tdStr);
        }
        $(".inp_style").click(function () {
            $(this).removeAttr("readonly");
            $(this).css("background-color", "rgb(255, 255, 255)");
        });
        $(".inp_style").blur(function () {
            $(this).attr("readonly", true);
            $(this).css("background-color", "#d8d8d8");
        });
    }

    function beginUpdateWeight(pid, weight) {
        $.messager.prompt('提示信息', '请输入新的重量:', function (newWeight) {
            if (newWeight) {
                var reg = /(^[-+]?[1-9]\d*(\.\d{1,2})?$)|(^[-+]?[0]{1}(\.\d{1,3})?$)/;
                if (reg.test(newWeight)) {
                    if (weight == newWeight) {
                        $.messager.alert("提醒", "新的重量和原重量相同", "info");
                    } else {
                        $.messager.progress({
                            title: '正在更新',
                            msg: '请等待...'
                        });
                        $.ajax({
                            type: 'POST',
                            dataType: 'json',
                            url: '/cbtconsole/editc/updateGoodsWeight',
                            data: {
                                "pid": pid,
                                "newWeight": newWeight,
                                "weight": weight
                            },
                            success: function (json) {
                                $.messager.progress('close');
                                if (json.ok) {
                                    $.messager.alert("提醒", "执行成功，页面即将刷新", "info");
                                    setTimeout(function () {
                                        window.location.reload();
                                    }, 500);
                                } else {
                                    $.messager.alert("提醒", json.message, "error");
                                }
                            },
                            error: function () {
                                $.messager.progress('close');
                                $.messager.alert("提醒", "执行失败，请重试", "error");
                            }
                        });
                    }

                } else {
                    showMessage('新的重量必须为正数，最多三位小数！');
                }
            } else {
                showMessage('未输入新的重量或取消输入！');
            }
        });
    }

    function updateWeightFlag(pid, obj) {
        $.ajax({
            type: 'POST',
            dataType: 'json',
            url: '/cbtconsole/editc/updateWeightFlag',
            data: {
                "pid": pid
            },
            success: function (json) {
                if (json.ok) {
                    $.messager.alert("提醒", "更新成功", "info");
                    $(obj).hide();
                } else {
                    $.messager.alert("提醒", json.message, "error");
                }
            },
            error: function () {
                $.messager.alert("提醒", "执行失败，请重试", "error");
            }
        });
    }

    function updateVolumeWeight(pid, oldVolumeWeight) {
        $.messager.prompt('提示信息', '请输入新的体积重量:', function (newWeight) {
            if (newWeight) {
                var reg = /(^[-+]?[1-9]\d*(\.\d{1,2})?$)|(^[-+]?[0]{1}(\.\d{1,3})?$)/;
                if (reg.test(newWeight)) {
                    if (newWeight == oldVolumeWeight) {
                        showMessage('新输入的体积重量和原来的一致！');
                        return false;
                    } else {
                        $.ajax({
                            type: 'POST',
                            dataType: 'json',
                            url: '/cbtconsole/editc/updateVolumeWeight',
                            data: {
                                "pid": pid,
                                "newWeight": newWeight
                            },
                            success: function (json) {
                                if (json.ok) {
                                    $("#goods_volum_weight").val(newWeight);
                                    showMessage('更新体积重量执行成功');
                                } else {
                                    $.messager.alert("提醒", json.message, "error");
                                }
                            },
                            error: function () {
                                $.messager.alert("提醒", "执行失败，请重试", "error");
                            }
                        });
                    }

                } else {
                    showMessage('新的体积重量必须为正数，最多三位小数！');
                }
            } else {
                // showMessage('未输入新的体积重量或取消输入！');
            }
        });
    }

    function beforeProfit(pid, type, profit) {
        if (type == 0) {
            $.messager.prompt('提示信息', '请输入新的利润率:', function (newProfit) {
                if (newProfit) {
                    var reg = /(^[-+]?[1-9]\d*(\.\d{1,2})?$)|(^[-+]?[0]{1}(\.\d{1,3})?$)/;
                    if (reg.test(newProfit)) {
                        editAndLockProfit(pid, type, newProfit);
                    } else {
                        showMessage('新的利润率必须为正数，最多三位小数！');
                    }
                } else {
                    showMessage('未输入新的利润率或取消输入！');
                }
            });
        } else if (type == 1) {
            if (profit == 0) {
                showMessage('未设置新的利润率，拒绝执行锁定');
            } else {
                $.messager.confirm('提示信息', '是否锁定利润率？', function (rs) {
                    if (rs) {
                        editAndLockProfit(pid, type, 0);
                    }
                });
            }
        }

        function editAndLockProfit(pid, type, newProfit) {
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '/cbtconsole/editc/editAndLockProfit',
                data: {
                    "pid": pid,
                    "editProfit": newProfit,
                    "type": type
                },
                success: function (json) {
                    if (json.ok) {
                        $.messager.alert("提醒", "执行成功，页面即将刷新", "info");
                        setTimeout(function () {
                            window.location.reload();
                        }, 500);
                    } else {
                        $.messager.alert("提醒", json.message, "error");
                    }
                },
                error: function () {
                    $.messager.alert("提醒", "执行失败，请重试", "error");
                }
            });
        }
    }

    function openSkuEdit(pid) {
        var param = "height=660,width=900,top=160,left=550,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
        window.open("/cbtconsole/editc/querySkuByPid?pid=" + pid, "windows", param);
    }
    
    function savePidNewCatidInfo() {
            var pid = $("#catid_pid").val();
            var oldCatid = $("#catid_old").val();
            var node = $("#select_catid").combotree('tree').tree('getSelected');
            var newCatid = node.id;
            if (pid && oldCatid && newCatid) {
                $.messager.progress({
                    title: '正在保存',
                    msg: '请等待...'
                });
                $.ajax({
                    type: "POST",
                    url: "/cbtconsole/editc/changePidToNewCatid",
                    data: {pid: pid, oldCatid: oldCatid, newCatid: newCatid},
                    success: function (data) {
                        // $("#notice_id").hide();
                        $.messager.progress('close');
                        if (data.ok) {
                            $('#catid-dlg').dialog('close');
                        } else {
                            $.messager.alert("提示信息", data.message, "error");
                        }
                    },
                    error: function (res) {
                        // $("#notice_id").hide();
                        $.messager.progress('close');
                        $.messager.alert("提示信息", "网络连接错误", "error");
                    }
                });
            } else {
                $.messager.alert("提示信息", "请填写完整信息", "info");
            }
        }
</script>
</html>