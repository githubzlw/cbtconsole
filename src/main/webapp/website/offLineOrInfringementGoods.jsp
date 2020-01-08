<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>侵权或者硬下架商品处理</title>
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
            max-height: 120px;
            max-width: 120px;
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

        .checkBg {
            background-color: #b6f5b6;
        }

        .check_sty {
            height: 24px;
            float: left;
            width: 24px;
        }

        .select_ckb {
            height: 26px;
            width: 75px;
            background: #169bd4;
            color: #fff;
        }

        .select_dle {
            height: 26px;
            width: 130px;
            background: red;
            color: #fff;
        }
    </style>
    <script type="text/javascript">
        function jumpPage(page, flag) {
            var type = $("#query_type").val();
            var catid = $("#query_catid").val();
            var shopId = $("#query_shop_id").val();
            var totalPage = $("#total_page").text();
            if (page < 1) {
                page = 1;
            } else if (page > totalPage) {
                page = totalPage;
            }
            if (flag > 0) {
                var queryPage = $("#jump_page").val();
                if (queryPage) {
                    if (queryPage < 1) {
                        queryPage = 1;
                    } else if (queryPage > totalPage) {
                        queryPage = totalPage;
                    }
                    window.location.href = "/cbtconsole/mongo/getOffLineOrInfringementGoods?type="
                        + type + "&catid=" + catid + "&shopId=" + shopId + "&page=" + queryPage;
                }
            } else {
                window.location.href = "/cbtconsole/mongo/getOffLineOrInfringementGoods?type="
                    + type + "&catid=" + catid + "&shopId=" + shopId + "&page=" + page;
            }
        }
    </script>
</head>
<body>

<c:if test="${isSu > 0}">
    <div class="div_sty_img" style="display: none;" id="big_img"></div>
    <table>
        <caption style="text-align: center;"><b style="font-size: 18px;">侵权或者硬下架商品处理</b></caption>
        <tr>
            <td>类别:<select id="query_type" style="height: 28px;">
                <c:if test="${type == 1}">
                    <option value="1" selected="selected">侵权商品</option>
                    <option value="2">硬下架商品</option>
                </c:if>
                <c:if test="${type == 2}">
                    <option value="1">侵权商品</option>
                    <option value="2" selected="selected">硬下架商品</option>
                </c:if>
            </select></td>
            <td>类别ID:<input id="query_catid" value="${catid}" style="height: 28px;"/></td>
            <td>店铺ID:<input id="query_shop_id" value="${shopId}" style="height: 28px;"/></td>
            <td>
                <button class="select_ckb" onclick="jumpPage(1,0)">查询</button>
            </td>


            <c:if test="${type == 1}">
                <td>
                    &nbsp;&nbsp;&nbsp;<button class="select_dle" onclick="deleteData(${type},0,'')">批量删除侵权数据</button>
                </td>
            </c:if>
            <c:if test="${type == 2}">
                <td>
                    &nbsp;&nbsp;&nbsp;<button class="select_dle" onclick="deleteImg(${type},1,'')">批量删除详情图片</button>
                </td>
            </c:if>

            <td>
                &nbsp;&nbsp;<input class="select_ckb" type="button" onclick="chooseImgBox(1)" value="全选"/>
                <input class="select_ckb" type="button" onclick="chooseImgBox(0)" value="反选"/>
                <input class="select_ckb" type="button" onclick="chooseImgBox(2)" value="取消全选"/>
            </td>
            <td><b style="color: red; font-size: 18px;">请点击图片查看放大的图片</b></td>
        </tr>
    </table>
    <table border="1" cellpadding="0" cellspacing="0">
        <thead>
        <tr>
            <td style="width: 180px;text-align: center;">PID</td>
            <td style="width: 700px;text-align: center;">商品信息</td>
            <td style="width: 700px;text-align: center;">商品详情图片</td>
            <td style="width: 120px;text-align: center;">操作</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${goodsList}" var="gd">
            <tr>
                <td style="width: 180px;">
                    <a target="_blank" href="/cbtconsole/editc/detalisEdit?pid=${gd.pid}">${gd.pid}</a>
                    <br>
                    <span>类别:${gd.catid1}</span>
                    <br>
                    <span>店铺ID:${gd.shopId}</span>
                </td>
                <td style="width: 700px;">
                    <span>${gd.name}</span>
                    <br>
                    <span>${gd.enname}</span>
                    <br>
                    <img class="img_sty" src="${gd.customMainImage}" onclick="bigImg('${gd.customMainImage}')"/>
                </td>
                <td style="width: 700px;">
                    <c:if test="${gd.infoList != null}">
                        <c:forEach items="${gd.infoList}" var="img">
                            <img class="img_sty" src="${img}" onclick="bigImg('${img}')"/>
                        </c:forEach>
                    </c:if>
                </td>
                <td style="width: 120px;">
                    <input class="check_sty" type="checkbox"
                           value="${gd.pid}" onclick="chooseBox(this)"/>

                    <c:if test="${type == 1}">
                        <br><br>
                        <button class="select_dle" onclick="deleteData(${type},0,'${gd.pid}')">删除侵权数据</button>
                        <br>
                    </c:if>
                    <c:if test="${type == 2}">
                        <br><br>
                        <button class="select_dle" onclick="deleteImg(${type},0,'${gd.pid}')">删除详情图片</button>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>

    </table>

    <p>
        <span>当前页<em>${currPage}</em>
            &nbsp;&nbsp;总页数:<em id="total_page">${totalPage}</em>&nbsp;&nbsp;
            总数:<em>${count}</em></span>
        <c:if test="${currPage > 1}">
            &nbsp;&nbsp;<button class="select_ckb" onclick="jumpPage(${currPage -1},0)">上一页</button>
        </c:if>
        <c:if test="${currPage == 1}">
            &nbsp;&nbsp;<span>上一页</span>
        </c:if>
        <c:if test="${currPage  < totalPage}">
            &nbsp;&nbsp;<button class="select_ckb" onclick="jumpPage(${currPage +1},0)">下一页</button>
        </c:if>
        <c:if test="${currPage == totalPage}">
            &nbsp;&nbsp;<span>下一页</span>
        </c:if>
        <span><input id="jump_page" value=""/><button class="select_ckb" onclick="jumpPage(${totalPage},1)">跳转</button></span>
    </p>


</c:if>

<c:if test="${isSu == 0}">
    <h1 style="text-align: center">${message}</h1>
</c:if>
</body>
<script type="text/javascript">

    function deleteImg(type, flag, pid) {
        var pids = "";
        if (flag > 0) {
            $(".isChoose").each(function () {
                var checkVal = $(this).val();
                pids += "," + checkVal;
            });
        } else {
            pids = pid;
        }

        if (pids.length > 1) {
            $.ajax({
                url: '/cbtconsole/mongo/deleteImg',
                type: "post",
                data: {pids: pids, type: type},
                success: function (res) {
                    if (res.ok) {
                        alert('操作成功');
                        setTimeout(function () {
                            window.location.reload();
                        }, 500)
                    } else {
                        alert('操作失败');
                    }
                },
                error: function () {
                    alert('操作失败');
                }
            });
        } else {
            alert('请选择商品');
        }
    }

    function deleteData(type, flag, pid) {
        var pids = "";
        if (flag > 0) {
            $(".isChoose").each(function () {
                var checkVal = $(this).val();
                pids += "," + checkVal;
            });
        } else {
            pids = pid;
        }
        if (pids.length > 1) {
            $.ajax({
                url: '/cbtconsole/mongo/deleteData',
                type: "post",
                data: {pids: pids, type: type},
                success: function (res) {
                    if (res.ok) {
                        alert('操作成功');
                        setTimeout(function () {
                            window.location.reload();
                        }, 500)
                    } else {
                        alert('操作失败');
                    }
                },
                error: function () {
                    alert('操作失败');
                }
            });
        } else {
            alert('请选择商品');
        }
    }

    function bigImg(img) {
        $('#big_img').empty();
        htm_ = "<img style='max-width:700px;max-height:700px;' src=" + img
            + "><br><input class='s_btn' type='button' value='关闭' onclick='closeBigImg()' />";
        $("#big_img").append(htm_);
        $("#big_img").css("display", "block");
    }

    function closeBigImg() {
        $("#big_img").css("display", "none");
        $('#big_img').empty();
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
</script>
</html>
