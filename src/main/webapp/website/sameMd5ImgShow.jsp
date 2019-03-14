<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <title>同MD5店铺图片展示</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <style type="text/css">
        .img_sty {
            max-width: 180px;
            max-height: 180px;
        }

        .checkBg {
            background-color: #b6f5b6;
        }

        .check_sty {
            height: 24px;
            float: left;
            width: 24px;
        }

        .select_ckb {
            height: 30px;
            width: 100px;
            background: #169bd4;
            color: #fff;
        }

        .option_ckb {
            height: 30px;
            width: 100px;
            background: #5ed416;
            color: #fff;
        }

        .div_sty {
            width: 232px;
            height: 277px;
        }

        .s_btn {
            display: inline-block;
            width: 120px;
            height: 35px;
            background: #169bd4;
            margin: 0px 0px 10px 10px;
            border-radius: 10px;
            text-align: center;
            color: #fff;
            cursor: pointer;
            font-size: 14px;
        }

        .div_sty_img {
            border: 5px solid #000;
            text-align: center;
            background-color: #FFF7FB;
            width: 750px;
            height: 750px;
            position: fixed;
            top: 90px;
            left: 15%;
            margin-left: 400px;
            padding: 5px;
            padding-bottom: 20px;
            z-index: 1011;
        }
    </style>
    <script type="text/javascript">

        $(document).ready(function () {
            $('.img_sty').lazyload({effect: "fadeIn"});
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

        function chooseImgBox(num) {
            if (num > 0) {
                if (num == 1) {
                    $(".check_sty").each(function () {
                        $(this).addClass("isChoose");
                        $(this).parent().parent().addClass("checkBg");
                        $(this).prop("checked", true);
                    });
                } else {
                    $(".check_sty").each(function () {
                        $(this).removeClass("isChoose");
                        $(this).parent().parent().removeClass("checkBg");
                        $(this).removeAttr("checked");
                    });
                }
            } else {
                $(".check_sty").each(function () {
                    if ($(this).is(':checked')) {
                        $(this).removeClass("isChoose");
                        $(this).parent().parent().removeClass("checkBg");
                        $(this).removeAttr("checked");
                    } else {
                        $(this).addClass("isChoose");
                        $(this).parent().parent().addClass("checkBg");
                        $(this).prop("checked", true);
                    }
                });
            }
        }


        function bigImg(img) {
            $('#big_img').empty();
            var htm_ = "<img style='max-width:700px;max-height:700px;' src=" + img
                + "><br><input class='s_btn' type='button' value='关闭' onclick='closeBigImg()' />";
            $("#big_img").append(htm_);
            $("#big_img").css("display", "block");
        }

        function closeBigImg() {
            $("#big_img").css("display", "none");
            $('#big_img').empty();
        }

        function setShopImgFlag(md5Val) {
            var pids = getPids();
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '/cbtconsole/cutom/setShopImgFlag',
                data: {
                    "md5Val": md5Val,
                    "pids": pids
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

        function batchOffGoods() {
            var pids = getPids();
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '/cbtconsole/cutom/batchOffGoods',
                data: {
                    "pids": pids
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

        function getPids() {
            var pids = "";
            $(".check_sty").each(function () {
                if ($(this).is(':checked')) {
                    pids += "," + $(this).val();
                }
            });
            if (pids.length > 1) {
                return pids.substring(1);
            } else {
                return pids;
            }
        }
    </script>
</head>
<body>

<c:if test="${isShow == 0}">
    <h1 align="center">${message}</h1>
</c:if>
<c:if test="${isShow > 0}">

    <div class="div_sty_img" style="display: none;" id="big_img"></div>
    <div>
        <h2 align="center">同MD5店铺图片展示(<span style="color:green">总数:${showTotal}</span>)</h2>
        <div>
            <span><b style="color: red; font-size: 18px;">请点击图片查看放大的图片</b></span>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input class="select_ckb" type="button" onclick="chooseImgBox(1)" value="全选"/>
            &nbsp;&nbsp;&nbsp;
            <input class="select_ckb" type="button" onclick="chooseImgBox(0)" value="反选"/>
            &nbsp;&nbsp;&nbsp;
            <input class="select_ckb" type="button" onclick="chooseImgBox(2)" value="取消全选"/>
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input class="option_ckb" type="button" onclick="setShopImgFlag('${md5Val}')" value="标记公共图"/>
            &nbsp;&nbsp;
            <input class="option_ckb" type="button" onclick="batchOffGoods()" value="批量下架"/>
        </div>
        <br>
        <table border="1" cellpadding="0" cellspacing="0" align="left">
            <c:set var="count" value="0"/>
            <c:forEach items="${list}" var="imgGd">
                <c:set var="count" value="${count+1 }"/>
                <c:if test="${count == 1}">
                    <tr>
                </c:if>
                <td>
                    <div class="div_sty">
                        <label>
                            <input class="check_sty isChoose" type="checkbox" checked="checked"
                                   value="${imgGd.pid}" onclick="chooseBox(this)"/>
                        </label>&nbsp;
                        <span>PID:<a target="_blank"
                                     href="/cbtconsole/editc/detalisEdit?pid=${imgGd.pid}">${imgGd.pid}</a></span>
                        <br>
                        <span>ShopId:${imgGd.shopId}</span>
                        <br> <%--<img class="img_sty" src="/cbtconsole/img/yuanfeihang/loaderTwo.gif"
                                  data-original="${imgGd.imgShow}" onclick="bigImg('${imgGd.imgShow}')"/>--%>
                            <img class="img_sty" src="${imgGd.imgShow}" onclick="bigImg('${imgGd.imgShow}')"/>
                        <br>
                        <c:if test="${imgGd.valid == 1}">
                            <span>商品状态：上架</span>
                        </c:if>
                        <c:if test="${imgGd.valid != 1}">
                            <span>商品状态：下架</span>
                        </c:if>
                        <br>
                        <c:if test="${imgGd.isMark == 0}">
                            <span class="no_flag">未标记</span>
                        </c:if>
                        <c:if test="${imgGd.isMark > 0}">
                            <span class="is_flag">已标记</span>
                        </c:if>


                    </div>
                </td>
                <c:if test="${count % 8 == 0}">
                    </tr>
                </c:if>
            </c:forEach>
            <c:if test="${count % 8 > 0}">
                </tr>
            </c:if>
        </table>
    </div>

</c:if>


</body>
</html>