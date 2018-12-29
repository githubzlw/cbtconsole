<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>速卖通对标1688</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
</head>
<style>
    .img_sty {
        max-height: 180px;
        max-width: 180px;
    }

    .s_btn {
        display: inline-block;
        width: 100px;
        height: 30px;
        background: #169bd4;
        border-radius: 10px;
        text-align: center;
        color: #fff;
        cursor: pointer;
        font-size: 14px;
    }

    .inp_sty {
        height: 26px;
    }
</style>
<script>
    $(document).ready(function () {
        var adminId = "${adminId}";
        getAdminList(adminId);
    });

    function getAdminList(adminId) {
        $.ajax({
            type: "POST",
            url: "/cbtconsole/singleGoods/getAdminList",
            data: {},
            success: function (data) {
                if (data.ok) {
                    $("#query_admin_id").empty();
                    var content = '<option value="0" selected="selected">全部</option>';
                    var json = data.data;
                    for (var i = 0; i < json.length; i++) {
                        if (json[i].id == adminId) {
                            content += '<option selected="select" value="' + json[i].id + '" ">' + json[i].confirmusername + '</option>';
                        } else {
                            content += '<option value="' + json[i].id + '" ">' + json[i].confirmusername + '</option>';
                        }
                    }
                    $("#query_admin_id").append(content);
                } else {
                    console.log("获取用户列表失败，原因 :" + data.message);
                }
            },
            error: function (res) {
                console.log("网络获取失败");
            }
        });
    }
</script>
<body style="overflow-y: hidden;">


<div style="margin-bottom: -3px;text-align: center;width: 100%;height: 10%;">
    <h3 style="text-align: center;">速卖通对标1688</h3>
    <form action="/cbtconsole/productCtr/queryForList" method="post">
        <span>AliPid:<input type="text" name="aliPid" class="inp_sty" value="${aliPid}"/></span>
        <span>关键词:<input type="text" name="keyword" class="inp_sty" value="${keyword}"/></span>
        <span>
            对标人:<select id="query_admin_id" name="adminId" style="height: 28px;"></select>
            </span>
        <input type="hidden" value="1" name="page"/>
        &nbsp;&nbsp;&nbsp;<span><input type="submit" class="s_btn" value="查询"></span>
    </form>
</div>

<div style="width: 100%;height: 90%;overflow-y: auto">
    <table id="shop_category_id" border="1" cellpadding="1"
           cellspacing="0" align="center">
        <thead>
        <tr align="center" style="height: 50px;background-color: #1fe237;">
            <th style="width: 11%;">速卖通商品信息</th>
            <th style="width: 44%;" colspan="4">lire对标1688商品信息</th>
            <th style="width: 44%;" colspan="4">爆款对标商品信息</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${infos}" var="aliGd" varStatus="status">
            <tr style="height: 42px;">
                <td style="width: 11%;">
                    <div>
                        <span>关键词:${aliGd.keyword}</span>
                        <br><span>AliPid:${aliGd.aliPid}</span>
                        <br><a target="_blank" href="${aliGd.aliUrl}"><img class="img_sty" src="${aliGd.aliImg}"/></a>
                        <br><span>价格:${aliGd.aliPrice}</span>
                        <br><span>产品名称:${aliGd.aliName}</span>
                        <br><span>操作</span>
                    </div>
                </td>
                <c:if test="${fn:length(aliGd.productListLire) > 0}">
                    <c:forEach items="${aliGd.productListLire}" var="lireGd">
                        <td style="width: 11%;">

                            <div>
                                <span>Pid:${lireGd.pid}</span>
                                <br><span>价格:${lireGd.showPrice}</span>
                                <br><a target="_blank" href="${lireGd.url}"><img class="img_sty"
                                                                             src="${lireGd.remotePath}${lireGd.img}"/></a>
                                <br><span>产品名:${lireGd.name}</span>
                            </div>
                        </td>
                    </c:forEach>
                </c:if>
                <c:if test="${fn:length(aliGd.productListPython) > 0}">
                    <c:forEach items="${aliGd.productListPython}" var="pyGd">
                        <td style="width: 11%;">
                            <div>
                                <span>Pid:${pyGd.pid}</span>
                                <br><span>价格:${pyGd.showPrice}</span>
                                <br><a target="_blank" href="${pyGd.url}"><img class="img_sty"
                                                                           src="${pyGd.remotePath}${pyGd.img}"/></a>
                                <br><span>产品名:${pyGd.name}</span>
                            </div>
                        </td>
                    </c:forEach>
                </c:if>

            </tr>
        </c:forEach>

        </tbody>
    </table>


    <form id="submit_form" action="/cbtconsole/productCtr/queryForList" method="post">
        <input type="hidden" id="page_ali_pid" name="aliPid" value="${aliPid}"/>
        <input type="hidden" id="page_keyword" name="keyword" value="${keyword}"/>
        <input type="hidden" id="page_adminId" name="adminId" value="${adminId}"/>
        <input type="hidden" id="query_current_page" name="page" value="${page}"/>
    </form>
    <div style="text-align: center;">
        <span>当前页：<span id="query_page">${page}</span>/<span id="query_total_page">${totalPage}</span></span>
        <span>&nbsp;&nbsp;总数：<span id="query_total">${total}</span></span>
        <span>&nbsp;&nbsp;<input type="button" class="s_btn" value="上一页"
                                 onclick="beforeQuery(${page},${page-1},${totalPage})"/></span>
        <span>&nbsp;&nbsp;<input type="button" class="s_btn" value="下一页"
                                 onclick="beforeQuery(${page},${page+1},${totalPage})"/></span>
        <span>&nbsp;&nbsp;&nbsp;&nbsp;跳转页：<input id="jump_page" type="number" value="1"/>
        <input type="button" value="翻页" class="s_btn" onclick="jumpPage(${page},${totalPage})"/></span>
    </div>
</div>
</body>
<script>
    function beforeQuery(currentPage, nextPage, totalPage) {
        if (nextPage > 0 && nextPage <= totalPage) {
            $("#query_current_page").val(nextPage);
            doQuery();
        } else {
            alert("无法翻页！");
            return false;
        }
    }

    function jumpPage(currentPage, totalPage) {

        var nextPage = $("#jump_page").val();
        if (nextPage == null || nextPage == "" || nextPage < 1) {
            alert("请输入跳转页");
            return false;
        } else {
            if (nextPage > 0 && nextPage <= totalPage) {
                $("#query_current_page").val(nextPage);
                doQuery();
            } else {
                alert("无法翻页！");
                return false;
            }
        }
    }

    function doQuery() {
        $("#submit_form").submit();
    }
</script>
</html>
