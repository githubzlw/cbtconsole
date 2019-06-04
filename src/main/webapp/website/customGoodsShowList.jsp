<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <title>店铺中产品列表</title>
    <style type="text/css">
        b {
            color: red;
        }

        .btn {
            color: #fff;
            margin: 5px;
            background-color: #5db5dc;
            border-color: #2e6da4;
            padding: 4px 10px;
            font-size: 12px;
            line-height: 1.5;
            border-radius: 3px;
            border: 1px solid transparent;
            cursor: pointer;
        }

        .btn2 {
            color: #fff;
            background-color: #cccccc;
            border-color: #2e6da4;
            padding: 5px 10px;
            font-size: 12px;
            line-height: 1.5;
            border-radius: 3px;
            border: 1px solid transparent;
            cursor: default;
        }

        .a_style {
            display: block;
            width: 229px;
        }

        .td_style {
            background-color: #880c0c;
        }

        .tr_disable {
            background-color: #d4d3d3;
        }

        .tr_edited {
            background-color: #45f959;
        }

        .img_sty{
            max-height: 200px;
            max-width: 200px;
        }
        .show_img img {
            width:19%;
        }
    </style>
    <script type="text/javascript">
        /* MD5 图片删除 函数来至于后台功能“图片MD5展示相同店铺”中 */
        function markAndDeleteImg(shopId, pid, url) {
            var isSt = confirm("是否进行同店铺MD5标记删除? 删除后此店铺该MD5图片数据将不再显示");
            if (isSt) {
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/editc/deleteImgByMd5',
                    data: {
                        "pid": pid,
                        "shopId": shopId,
                        "url": url
                    },
                    success: function (json) {
                        if (json.ok) {
                            window.location.reload();
                        } else {
                            showMessage("提醒", json.message, "error");
                        }
                    },
                    error: function () {
                        showMessage("提醒", "执行失败，请重试", "error");
                    }
                });
            }
        }


        /* 下架  */
        function fndown(pid, reason) {
            if (pid == null || pid == '') {
                return;
            }
            $.messager.confirm('提示', '是否下架商品' + pid + '?', function(rs) {
                if (rs) {
                    $.ajax({
                        type : 'POST',
                        dataType : 'json',
                        url : '/cbtconsole/queryuser/insertNeedoffDownAll.do',
                        data : {
                            pids : pid,
                            reason: reason
                        },
                        success : function(data) {
                            if (data.state == 'true') {
                                showMessage("对应数据已标记, 隔天会自动下架.");
                            } else {
                                showMessage("执行失败，请重试, message " + data.message);
                            }
                        },
                        error : function() {
                            showMessage("提醒", "error", "error");
                        }
                    });
                }
            });
        }


        /* 批量下架 */
        function fndownall(reason, shopId) {
            var checked = "";
            var title = "";
            if (reason == '32') {
                $(".checkpid:checked").each(function () {
                    checked += this.value + ',';
                });
                title = "是否确认批量下架(下架下面勾选的商品 因为中文或者画质下架)?";
            } else if (reason == '31') {
                checked = shopId;
                title = "是否确认批量下架(全店铺商品下架)?";
            }
            if (checked == '') {
                showMessage("请选择需要批量下架的数据");
                return;
            }
            $.messager.confirm('提示', title, function(rs) {
                if (rs) {
                    $.ajax({
                        type : 'POST',
                        dataType : 'json',
                        url : '/cbtconsole/queryuser/insertNeedoffDownAll.do',
                        data : {
                            pids : checked,
                            reason: reason
                        },
                        success : function(data) {
                            if (data.state == 'true') {
                                showMessage("对应数据已标记, 隔天会自动下架.");
                            } else {
                                showMessage("执行失败，请重试, message " + data.message);
                            }
                        },
                        error : function() {
                            showMessage("提醒", "error", "error");
                        }
                    });
                }
            });
        }


        /* 全选  type 1-全选；0-更具全选按钮决定全选或者全取消*/
        function fnselect(type) {
            if (type == 1) {
                $("#checked_all").val('1');
                $(".checkpid").each(function() {
                    $(this).prop("checked", true);
                });
            } else {
                var checked = $("#checked_all").val();
                $("#checked_all").val(checked == '0' ? '1' : '0');
                $(".checkpid").each(function() {
                    $(this).prop("checked", checked == '0' ? true : false);
                });
            }
        }


        function showMessage(msgStr) {
            $.messager.alert({
                title : '提醒',
                msg : msgStr,
                timeout : 1500,
                showType : 'slide',
                style : {
                    right : '',
                    top : ($(window).height() * 0.35),
                    bottom : ''
                }
            });
        }

        function fnjump(num) {
            var totalpage = $("#totalpage").val();
            var page = $("#page").val();
            if (page == "") {
                page = "1";
            }
            if (num == -1) {//前一页
                if (parseInt(page) > 1) {
                    page = parseInt(page) - 1;
                } else {
                    page = 1;
                }
            } else if (num == 1) {//下一页
                if (parseInt(page) > parseInt(totalpage) - 1) {
                    return;
                }
                page = parseInt(page) + 1;
            } else if (num < -1) {
                page = 1;
            }
            parent.parentDoQuery(page);
        }
    </script>

</head>
<body>
<div>
    <input type="button" onclick="fndownall('32', '')" value="批量下架(下架下面勾选的商品 因为中文或者画质下架)" style="height: 30px; margin-right: 40px;" class="btn">
    <c:if test="${not empty shopId}">
        <input type="button" onclick="fndownall('31', '${shopId}')" value="批量下架(全店铺商品下架)" style="height: 30px; margin-right: 40px;" class="btn">
        &nbsp;&nbsp;当前店铺:<span>${shopId}</span>&nbsp;&nbsp;条数:<span>${totalNum}</span>条
    </c:if>
</div>

<table id="table" border="1" cellpadding="0" cellspacing="0"
       class="table">
    <tr align="center" bgcolor="#DAF3F5">
        <td style="width: 4%;"><span style="font-size: 14px;">
                <input
                        id="checked_all" type="checkbox" style="height: 16px; width: 16px;"
                        value="0" onclick="fnselect(0)">全选</span></td>
        <td style="width: 8%;">1688PID</td>
        <td style="width: 70%;">前2张橱窗图+前3张详情图</td>
        <td style="width: 10%;">状态标识</td>
        <td style="width: 8%;">操作</td>
    </tr>


    <c:forEach items="${goodsList}" var="list" varStatus="index">
        <tr class="tr_disable">
            <td>
                <input type="checkbox" id="checkpid_${list.pid}"
                       class="checkpid is_disabled" style="height: 16px; width: 16px;"
                       value="${list.pid}" title="非当前商品的编辑人">
            </td>
            <!-- 产品id -->
            <td id="user_${index.index}">
                    ${list.pid}
                <br /><br /><a target="_blank" href="https://detail.1688.com/offer/${list.pid}.html">原1688链接</a>
                <br /><br /><a target="_blank" href="https://www.import-express.com/goodsinfo/cbtconsole-1${list.pid}.html">线上链接</a>
            </td>
            <!-- 图片 -->
            <td id="order_${index.index}" class="show_img">
                <c:forEach items="${list.showImages}" var="showImg" varStatus="index">
                    <img alt="无图" src="${showImg}" class="img_sty"
                         onclick="markAndDeleteImg('${list.shopId}', '${list.pid}', '${showImg}')">
                </c:forEach>
            </td>
            <!-- 状态时间等  -->
            <td id="state_${index.index}" style="font-size: 14px;">
                <span>店铺id:${list.shopId}</span>
                <c:if test="${list.valid == 0}">
                    <br>
                    <span>在线状态:硬下架</span>
                </c:if>
                <c:if test="${list.valid == 1}">
                    <br>
                    <span>在线状态:在线</span>
                </c:if>
                <c:if test="${list.valid == 2}">
                    <br>
                    <span>在线状态:软下架</span>
                </c:if>
                <br>
                <span>销量(产品在订单中出现次数):${list.isSoldFlag}</span>
                <c:if
                        test="${list.isEdited > 0}">
                    <br>
                    <span>商品编辑:已编辑</span>
                </c:if>
                <c:if test="${list.isBenchmark > 0}">
                    <br>
                    <span>货源对标情况:${list.benchmarkValue}</span>
                </c:if>
                <c:if test="${list.isBenchmark <= 0}">
                    <br>
                    <span>货源对标情况:没找到对标</span>
                </c:if>
                <c:if test="${not empty list.updatetime}">
                    <br>
                    <span>人为编辑时间:${list.updatetime}</span>
                </c:if>
            </td>
            <!-- 操作 -->
            <td id="order_${index.index}" style="text-align: center;">
                <a target="_blank" href="/cbtconsole/editc/detalisEdit?pid=${list.pid}">编辑详情</a>
                <br><br>
                <c:if test="${empty shopId}">
                    <a target="_blank" href="/cbtconsole/cutom/cmslist?valid=1&shopId=${list.shopId}">店铺所有商品</a>
                    <br><br>
                </c:if>
                <a href="javascript:;" onclick="fndown('${list.pid}','32')">产品下架(因为中文或者画质下架)</a>
            </td>
        </tr>
    </c:forEach>

</table>
<br>
<c:if test="${empty shopId}">
    <div>
        <input type="hidden" id="totalpage" value="${totalpage}"> 条数:<span>${totalNum}</span>条&nbsp;&nbsp;
        分页数:<span>${pagingNum}</span>条/页&nbsp;&nbsp;&nbsp;&nbsp; 总页数:<span
            id="pagetotal">${currentpage}<em>/</em> ${totalpage}
            </span> 页&nbsp;&nbsp; <input type="button" value="上一页" onclick="fnjump(-1)"
                                         class="btn">&nbsp; <input type="button" value="下一页"
                                                                   onclick="fnjump(1)" class="btn"> &nbsp;第<input id="page"
                                                                                                                  type="text" value="${currentpage}" style="height: 26px;"> <input
            type="button" value="查询" onclick="fnjump(0)" class="btn">
    </div>
</c:if>
</body>
</html>