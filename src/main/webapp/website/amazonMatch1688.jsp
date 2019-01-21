<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>亚马逊对标1688</title>
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
        height: 30px;
        background: #169bd4;
        border-radius: 10px;
        text-amazongn: center;
        color: #fff;
        cursor: pointer;
        font-size: 14px;
    }

    .inp_sty {
        height: 26px;
    }

    .s_give {
        display: inline-block;
        height: 30px;
        background: red;
        border-radius: 10px;
        text-amazongn: center;
        color: #fff;
        cursor: pointer;
        font-size: 14px;
    }

    .b_sty {
        color: red;
    }

    .b_check {
        color: #1909ea;
    }
</style>
<script>
    $(document).ready(function () {
        var adminId = "${adminId}";
        getAdminList(adminId);
        var dealState = "${dealState}";
        if (!(dealState == null || dealState == "")) {
            $("#query_deal_state").val(dealState);
        }
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
                    alert("获取用户列表失败，原因 :" + data.message);
                }
            },
            error: function (res) {
                alert("网络获取失败");
            }
        });
    }

    function openProduct(amazonPid, amazonUrl) {
        var url = '/cbtconsole/website/searchPidByAmazonPid.jsp?amazonPid=' + amazonPid + '&amazonUrl=' + amazonUrl;
        window.open(url);
    }

    function setAmazonDealFlag(amazonPid, obj) {
        $.ajax({
            type: "POST",
            url: "/cbtconsole/amazonProductCtr/setAmazonDealFlag",
            data: {
                amazonPid: amazonPid,
                dealState: 1
            },
            success: function (data) {
                if (data.ok) {
                    $(obj).hide();
                    $(".amazon1_" + amazonPid).hide();
                    $(".amazon2_" + amazonPid).hide();
                    $(obj).parent().find('input').hide();
                    $(obj).parent().append('<b class="b_sty">放弃</b>');
                } else {
                    alert("执行失败,原因:" + data.message);
                }
            },
            error: function (res) {
                alert("网络获取失败");
            }
        });
    }

</script>
<body style="overflow-y: hidden;">


<div style="margin-bottom: -3px;text-align: center;width: 100%;height: 10%;">
    <h3 style="text-align: center;">亚马逊对标1688</h3>
    <form action="/cbtconsole/amazonProductCtr/queryForList" method="post">
        <span>amazonPid:<input type="text" name="amazonPid" class="inp_sty" value="${amazonPid}"/></span>
        <span>
            处理人人:<select id="query_admin_id" name="adminId" style="height: 28px;"></select>
            </span>
        <span>处理状态:<select id="query_deal_state" name="dealState" style="height: 28px;">
            <option value="-1">全部</option>
            <option value="0">未处理</option>
            <option value="1">已处理</option>
        </select></span>
        <input type="hidden" value="1" name="page"/>
        &nbsp;&nbsp;&nbsp;<span><input type="submit" class="s_btn" value="查询"></span>
    </form>
</div>

<div style="width: 100%;height: 90%;overflow-y: auto">
    <table id="shop_category_id" border="1" cellpadding="1"
           cellspacing="0" amazongn="center">
        <thead>
        <tr align="center" style="height: 30px;">
            <th style="width: 140px;">AmazonPid</th>
            <th style="width: 777px;">Amazon产品信息</th>
            <th style="width: 180px;">创建时间</th>
            <th style="width: 80px;">处理状态</th>
            <th style="width: 180px;">匹配PID</th>
            <th style="width: 100px;">处理人</th>
            <th style="width: 180px;">处理时间</th>
            <th style="width: 180px;">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${goodsList}" var="amazonGd" varStatus="status">
            <tr>

                <td style="width: 180px;">${amazonGd.amazonPid}</td>
                <td style="width: 777px;"><a target="_blank" href="${amazonGd.amazonUrl}"><img class="img_sty"
                                                                                               src="${amazonGd.amazonImg}"/></a>
                    <br><span style="color: red">价格USD:${amazonGd.amazonPrice}</span>
                    <br><span>产品名称:[${amazonGd.amazonName}]</span>
                </td>
                <td style="width: 140px;">${amazonGd.createTime}</td>
                <c:if test="${amazonGd.dealState > 0}">
                    <td style="width: 80px;">已处理</td>
                    <td style="width: 180px;"><a href="/cbtconsole/editc/detalisEdit?pid=${amazonGd.matchPid}"
                                                 target="_blank">${amazonGd.matchPid}</a></td>
                    <td style="width: 100px;">${amazonGd.adminName}</td>
                    <td style="width: 180px;">${amazonGd.updateTime}</td>
                    <td style="width: 180px;"></td>
                </c:if>
                <c:if test="${amazonGd.dealState == 0}">
                    <td style="width: 80px;">未处理</td>
                    <td style="width: 180px;"></td>
                    <td style="width: 100px;"></td>
                    <td style="width: 180px;"></td>
                    <td style="width: 180px;text-align: center;"><input class="s_btn" type="button" value="开发产品"
                                                     onclick="openProduct('${amazonGd.amazonPid}','${amazonGd.amazonUrl}')"/>
                    </td>
                </c:if>
            </tr>
        </c:forEach>

        </tbody>
    </table>


    <form id="submit_form" action="/cbtconsole/amazonProductCtr/queryForList" method="post">
        <input type="hidden" id="page_amazon_pid" name="amazonPid" value="${amazonPid}"/>
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
