<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>商品下架审核</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <style type="text/css">
        .datagrid-htable {
            text-align: center;
            height: 22px;
            font-size: 20px;
        }

        #neef_off_query_form {
            font-size: 16px;
        }

        #button_style {
            font-size: 14px;
        }

        .panel-title {
            text-align: center;
            height: 22px;
            font-size: 20px;
        }

        #neef_off_top_toolbar {
            padding: 20px 28px !important;
        }

        .datagrid-header .datagrid-cell span, .datagrid-cell,
        .datagrid-cell-group, .datagrid-header-rownumber,
        .datagrid-cell-rownumber {
            font-size: 14px;
        }

        .enter_btn {
            width: 140px;
            height: 32px;
            color: #ffffff;
            background-color: #009688;
            transform: rotate(0deg);
            font-size: 18px;
            text-align: center;
            font-weight: normal;
            font-style: normal;
        }

        #no_shelf table tr {
            height:40px;
        }

        #same_goods table,#same_goods table tr th, #same_goods table tr td {
            /*border:1px solid #CCC;*/
            margin: 0px;
            padding: 0px;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            setDatagrid();
        });
        //标记为已处理
        function updateFlag3() {
            //选择的商品
            var checkArr = new Array();
            $("input[name='ck']:checked").parent().parent().parent().find("td[field='pid'] div")
                .each(function(index,item){
                    checkArr.push(item.innerHTML);
                });
            if(checkArr == undefined || checkArr.length == 0){
                alert("未选择商品!");
            } else {
                $.ajax({
                    type: "GET",
                    url: "/cbtconsole/queryuser/updateNeedoffshellEditFlag.do",
                    data: {"pids":checkArr.join(",")},
                    dataType:"json",
                    success: function(msg){
                        alert(msg.message);
                    }
                });
            }
        }

        function setDatagrid() {
            $('#neef_off_easyui-datagrid').datagrid({
//                title: '商品下架审核',
                width: "100%",
                fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                toolbar: "#neef_off_top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
//                url: '/cbtconsole/singleGoods/queryOffShelfList',//url调用Action方法
                loadMsg: '数据装载中......',
//                singleSelect: true,//为true时只能选择单行
                fitColumns: true,//允许表格自动缩放，以适应父容器
                rownumbers: true,
                nowrap: false,
                pageSize: 25,//默认选择的分页是每页25行数据
                pageList: [25,50],//可以选择的分页集合
                pagination: true,//分页
                style: {
                    padding: '8 8 10 8'
                },
                onLoadError: function () {
                    $.message.alert("提示信息", "获取数据信息失败");
                    return;
                },
                onLoadSuccess: function (data) {
                    if (!data.success) {
                        showMessage(data.message);
                    }
                }
            });
        }


        function showMessage(message) {
            $.messager.show({
                title: '提醒',
                msg: message,
                timeout: 2000,
                showType: 'slide',
                style: {
                    right: '',
                    top: ($(window).height() * 0.15),
                    bottom: ''
                }
            });
        }

        function doQuery() {
            var pid = $("#pid_id").val();
            if (pid == null || pid == "") {
                pid = "";
            }
            var catid = $("#catid_id").val();
            if (catid == null || catid == "") {
                catid = "";
            }
            var beginTime = $("#query_beginTime").val();
            if (beginTime == null || beginTime == "") {
                beginTime = "";
            }
            var endTime = $("#query_endTime").val();
            if (endTime == null || endTime == "") {
                endTime = "";
            }
            var reason = $("#query_reason").val();
            if (reason == null || reason == "") {
                reason = 0;
            }
            var isOffShelf = $("#query_isOffShelf").val();
            if (isOffShelf == null || isOffShelf == "") {
                isOffShelf = -1;
            }
            var updateFlag = $("#query_updateFlag").val();
            if (updateFlag == null || updateFlag == "") {
                updateFlag = -1;
            }
            var neverFlag = $("#query_never_off").val();
            if (neverFlag == null || neverFlag == "") {
                neverFlag = 0;
            }
            var soldFlag = $("#query_sold_flag").val();
            var soldFlag2 = $("#query_sold_flag2").val();
            var soldFlag3 = $("#query_sold_flag3").val();
            var source = $("#query_source").val();

            $("#neef_off_easyui-datagrid").datagrid("options").url = "/cbtconsole/singleGoods/queryOffShelfList";
            $("#neef_off_easyui-datagrid").datagrid("load", {
                "pid": pid,
                "catid":catid,
                "beginTime": beginTime,
                "endTime": endTime,
                "reason": reason,
                "isOffShelf": isOffShelf,
                "updateFlag": updateFlag,
                "neverFlag": neverFlag,
                "soldFlag":soldFlag,
                "soldFlag2":soldFlag2,
                "soldFlag3":soldFlag3,
                "source":source
            });
        }


        function formatImg(val, row, index){
            return '<img src="' + val + '" alt="无图" style="max-height: 180px;max-width: 180px;" />';
        }

        function formatIsOffShelf(val, row, index) {
            //是否需要下架
            if (val == 0) {
                return '硬下架';
            } else if (val == 1) {
                return '在线';
            } else if (val == 2) {
                return '软下架';
            } else {
                return '';
            }
        }

        function formatCatid(val, row, index) {
            return row.catid + "<br />" + row.catidName;
        }

        function formatUpdateFlag(val, row, index) {
            //更新标识 0未更新 1更新失败 2更新成功，3-重新验证过
            if (val == 0) {
                return '未更新';
            } else if (val == 1) {
                return '更新失败';
            } else if (val == 2) {
                return '更新成功';
            } else if (val == 3) {
                return '重新验证过';
            } else {
                return '';
            }
        }

        function formatCompetitiveFlag(val, row, index) {
            if(val >0){
                return '精品';
            }else{
                return '';
            }
        }

        function formatneverOffFlag(val, row, index) {
            if(val >0){
                return '是';
            }else{
                return '';
            }
        }
        
        function showShelf(pid) {
            $.messager.confirm('提示','确定下架该商品?',function(r){
                if(r){
                    $.ajax({
                        type: "GET",
                        url: "/cbtconsole/queryuser/updateNeedoffshelfByPid.do",
                        data: {
                            pid : pid,
                            noShelfInfo :  "4@@"
                        },
                        dataType:"json",
                        success: function(msg){
                            if(msg.message == '已修改') {
                                msg.message += " 隔天会定时下架.";
                            }
                            $.messager.alert('提示', msg.message);
                        }
                    });
                }
            });
        }

        function showNoShelf(pid) {
            $('#no_shelf input[name=pid]').val(pid);
            $('#no_shelf input[name=reason][value=1]').prop('checked', 'checked');
            $('#no_shelf input[name=reason_other]').val('');
            $('#no_shelf input[name=reason_goods]').val('');
            $('#no_shelf').window('open');
        }


        /* 同款数据弹窗 */
        function showSameGoods(pid, imgUrl) {
            $.ajax({
                type: "GET",
                url: "/cbtconsole/queryuser/querySameGoodsInfoByPid.do",
                data: {
                    pid: pid
                },
                dataType:"json",
                success: function(data){
                    console.log(data);
                    if (data.state == false) {
                        alert(data.message);
                        return;
                    }
                    $("#same_goods input[name=pid]").val(pid);
                    $("#same_goods img[name=main_img]").attr('src', imgUrl);

                    var same_url = "https://s.1688.com/collaboration/collaboration_search.htm?fromOfferId="
                        + pid + "&tab=sameDesign";
                    $("#same_goods a[name=same_url]").attr('href', same_url);

                    // 之前抓取同款详情
                    if (data.sameGoods == '') {
                        $("#same_goods tr[name=same_url_detail]").css('display', 'none');
                    } else {
                        $("#same_goods tr[name=same_url_detail]").css('display', 'table-row');
                        var same_url_datail = $("#same_goods tr[name=same_url_detail] div");
                        same_url_datail.empty();
                        $(data.sameGoods).each(function (index, item) {
                            var htm = "<div style=\"float:left;margin: 4px;width: 46%\"><div style=\"\">\n" +
                                "同款pid: <a href=\"https://detail.1688.com/offer/" + item.pid +
                                ".html\" target=\"_blank\">" + item.pid +
                                "</a><br />同款店铺: <a href=\"https://" + item.shopId +
                                ".1688.com\" target=\"_blank\">" + item.shopId +
                                "</a><br />同款店铺名:" + item.shopName + "<br /></div>\n" +
                                "<div><img class=\"search_for_goods\" style=\"height: 160px;\" \n" +
                                "src=\"" + item.jsonContent + "\" alt=\"无图\"></div></div>";
                            same_url_datail.append(htm);
                        });
                    }

                    // 以图找货
                    if (data.pics == '') {
                        $("#same_goods tr[name=same_img_goods]").css('display', 'none');
                    } else {
                        $("#same_goods tr[name=same_img_goods]").css('display', 'table-row');
                        var same_img = $("#same_goods tr[name=same_img_goods] div[name=img_goods]");
                        same_img.empty();
                        $(data.pics.split(";")).each(function (index, item) {
                            same_img.append("<div style=\"float:left; margin: 2px;\">" +
                                "<img class=\"search_for_goods\" style=\"height: 160px;\" src=\"" +
                                item + "\" alt=\"无图\"></div>");
                        });
                    }

                    // 以图找货 图片点击找货
                    $("img[class=search_for_goods]").click(function () {
                        var src_url = new URL($(this).attr('src'));
                        var tar_url = "https://s.1688.com/youyuan/index.htm?tab=imageSearch&from=plugin&imageType=" +
                            src_url.origin + "&imageAddress=" + src_url.pathname;
                        $("#search_for_goods_id").attr('href', tar_url)
                        document.getElementById("search_for_goods_id").click();
                    });

                    $('#same_goods').window('open');
                }
            });
        }

        function noShelf() {
            var pid = $('#no_shelf input[name=pid]').val();
            var check = $('#no_shelf input[name=reason]:checked').val();
            var reason_other = $('#no_shelf input[name=reason_other]').val();
            var reason_goods = $('#no_shelf input[name=reason_goods]').val();
            $.ajax({
                type: "GET",
                url: "/cbtconsole/queryuser/updateNeedoffshelfByPid.do",
                data: {
                    pid : pid,
                    noShelfInfo : check + "@" + reason_other + "@" + reason_goods
                },
                dataType:"json",
                success: function(msg){
                    $.messager.alert('提示', msg.message);
                    $('#no_shelf').window('close');
                }
            });
        }

        function formatReason(val, row, index) {
            if (window.UNSELLABLEREASON != {}) {
                return window.UNSELLABLEREASON[val];
            }
            //商品下架原因
            if (val == 1) {
                return '1-1688货源下架';
            } else if (val == 2) {
                return '2-不满足库存条件';
            } else if (val == 3) {
                return '3-销量不合格';
            } else if (val == 4) {
                return '4-1688页面404或者货源下架';
            } else if (val == 5) {
                return '5-重复验证合格';
            } else if (val == 6) {
                return '6-IP问题或运营直接下架';
            } else if (val == 7) {
                return '7-店铺整体禁掉';
            } else if (val == 8) {
                return '8-采样不合格';
            } else if (val == 9) {
                return '9-有质量问题';
            } else if (val == 10) {
                return '10-商品侵权';
            } else if (val == 11) {
                return '11-店铺侵权';
            } else if (val == 12) {
                return '12-难看';
            } else if (val == 13) {
                return '13-中文';
            } else if (val == 14) {
                return '14-1688商品货源变更';
            } else if (val == 15) {
                return '15-除服装珠宝分类外的非精品数据更新到软下架';
            } else if (val == 16) {
                return '16-搜索展现点击比+添加购物车数据 指标不符合要求';
            } else if (val == 17) {
                return '17-低价商品下架';
            } else if (val == 18) {
                return '18-类别隐藏数据下架';
            } else if (val == 19) {
                return '19-店铺小于5件商品软下架';
            } else if (val == 20) {
                return '20-按手起批';
            } else if (val == 21) {
                return '21-大于400美元商品下架';
            } else if (val == 22) {
                return '22-原因老数据没有展示详情图片';
            } else if (val == 23) {
                return '23-对应1688商品成交量小于4';
            } else if (val == 24) {
                return '24-同款下架';
            } else if (val == 27) {
                return '27-商品无详情图(批量删除中文图)';
            } else if (val == 28) {
                return '28-定制商品';
            } else if (val == 29) {
                return '29-清洗中判断的商品异常下架(比如去除一手等规格后剩余规格不可卖)';
            } else if (val == 30) {
                return '30-大于40美元商品软下架(不包含婚纱礼服)';
            } else if (val == 31) {
                return '31-整店下架';
            } else if (val == 32) {
                return '32-因为中文或者画质下架';
            } else {
                return '';
            }
        }

        function formatOperation(val, row, index) {
            var content = '<a target="_blank" href="/cbtconsole/editc/detalisEdit?pid='+ row.pid +'">编辑商品</a>';
            content += '<br /><br /><span><a href="#" onclick="showSameGoods(\'' + row.pid + '\', \'' + row.imgUrl + '\')">同款数据</a></span>'
            if (row.sourceFlag == 5) {
                content += '<br /><br /><span><a href="#" onclick="showShelf(' + row.pid + ')">确认下架</a></span>'
                content += '&nbsp;&nbsp;<span><a href="#" onclick="showNoShelf(' + row.pid + ')">不下架</a></span>'
                if (row.noShelfInfo != undefined) {
                    var info = '';
                    if (row.noShelfInfo.startsWith("4@")) {
                        info = '人为确认需要下架(隔天定时处理下架)';
                    } else if (row.noShelfInfo.startsWith("5@")) {
                        info = '人为确认需要下架且已经下架';
                    } else if (row.noShelfInfo.startsWith("3@")) {
                        info = '不下架 其他原因:' + row.noShelfInfo.split("@")[1];
                    } else if (row.noShelfInfo.startsWith("2@")) {
                        info = '不下架 有替换货源:' + row.noShelfInfo.split("@")[2];
                    } else if (row.noShelfInfo.startsWith("1@")) {
                        info = '不下架 不考虑库存';
                    }
                    content += '<br /><br /><span>' + info + '</span>'
                }
            }
            return content;
        }

        function formatPid(val, row, index) {
            return row.pid + '<br /><br /><a target="_blank" href="https://detail.1688.com/offer/'+ row.pid +'.html">原1688链接</a>' +
                '<br /><br /><a target="_blank" href="https://www.import-express.com/goodsinfo/cbtconsole-1'+ row.pid +'.html">线上链接</a>';
        }

    </script>

</head>
<body>

<c:if test="${uid == 0}">
    <h1 align="center">请登录后操作</h1>
</c:if>
<c:if test="${uid > 0}"></c:if>


<div id="no_shelf" class="easyui-window" title="不下架相关信息"
     data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"
     style="width:800px;height:auto;display: none;font-size: 16px;">
    <div style="margin-left:20px;">
        <input type="hidden" name="pid">
        <div style="margin-top:20px;">
            <table>
                <tr>
                    <td>商品pid:</td>
                    <td><input type="text" readonly name="pid" style="color: #4c4c4c; width: 600px;"></td>
                </tr>
                <tr>
                    <td>不下架原因:</td>
                    <td>
                        <input type="radio" name="reason" checked="checked" value="1">
                        <span onclick="$('#no_shelf input[name=reason][value=1]').prop('checked', 'checked');">不考虑库存</span>
                        <br />
                        <input type="radio" name="reason" value="2">
                        <span onclick="$('#no_shelf input[name=reason][value=2]').prop('checked', 'checked');">有替换货源</span>
                        <input type="text" name="reason_goods" style="width: 490px" onfocus="$('#no_shelf input[name=reason][value=2]').prop('checked', 'checked');">
                        <br />
                        <input type="radio" name="reason" value="3">
                        <span onclick="$('#no_shelf input[name=reason][value=3]').prop('checked', 'checked');">其他原因</span>
                        <input type="text" name="reason_other" style="width: 506px" onfocus="$('#no_shelf input[name=reason][value=3]').prop('checked', 'checked');">
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div style="margin:20px 0 20px 40px;">
        <a href="javascript:void(0)" class="easyui-linkbutton"
           onclick="noShelf()" style="width:80px">确认</a>
    </div>
</div>


<%-- 同款数据弹窗 --%>
<div id="same_goods" class="easyui-window" title="同款数据"
     data-options="collapsible:false,minimizable:false,maximizable:false,closed:true,overflow:true"
     style="width:1000px;height:700px;display: none;font-size: 16px;">
    <div style="margin-left:20px;margin-right: 20px">
        <input type="hidden" name="pid">
        <div style="margin-top:20px;">
            <table width="100%">
                <tr>
                    <td>
                        <h3 style="line-height: 0px">当前商品信息</h3>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        商品pid:
                        <input type="text" readonly name="pid" style="color: #4c4c4c; width: 200px;">
                        &nbsp;&nbsp;&nbsp;商品主图:
                        <div style="position: relative; top: -80px; left: 400px; height: 90px;width: 90px;">
                            <img name="main_img" style="height: 150px;" src="" alt="无图">
                        </div>
                    </td>
                </tr>
                <tr name="same_url_flag">
                    <td>
                        <h3 style="line-height: 0px">同款货源</h3>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;如果当前1688商品还在线 可以点击查询最新的同款数据: </span>
                        <span><a href="#" target="_blank" name="same_url">同款链接</a></span>
                    </td>
                </tr>
                <tr name="same_url_detail">
                    <td>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;原1688商品在线时候通过链接抓取的同款数据(前4个)</span>
                        <div style="margin-left: 44px">
                        </div>
                    </td>
                </tr>
                <tr name="same_img_goods">
                    <td>
                        <br />
                        <h3 style="line-height: 0px">以图搜货</h3>
                        <span>&nbsp;&nbsp;&nbsp;&nbsp;(下面图片是原1688商品在线时候保存的橱窗图, 点击对应图片可以跳转到1688对应图片的以图搜货页面)</span>
                        <div name="img_goods" style="margin-left: 44px">
                        </div>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <div style="margin:20px 0 20px 40px;">
        <a id="search_for_goods_id" href="#" target="_blank"></a>
    </div>
</div>


<div id="neef_off_top_toolbar" style="padding: 5px; height: auto">
    <form id="neef_off_query_form" action="#" onsubmit="return false;">
        <span> PID: <input type="text" id="pid_id" style="width: 180px; height: 24px" value=""/></span>
        <span> 类别ID: <input type="text" id="catid_id" style="width: 120px; height: 24px" value=""/></span>
        <span>上下架时间: <input id="query_beginTime" class="Wdate" style="width: 110px; height: 24px" type="text" value=""
                         onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
		    <span>-</span>
            <input id="query_endTime" class="Wdate" style="width: 110px; height: 24px;" type="text" value=""
                   onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
        </span>
        <span> 当前在线状态: <select id="query_isOffShelf" style="font-size: 14px; height: 24px; width: 120px;">
                        <option value="-1" selected="selected">全部</option>
                        <option value="1">在线</option>
                        <option value="2">软下架</option>
                        <option value="0">硬下架</option>
                    </select>
        </span>
        <span> 下架原因: <select id="query_reason" style="font-size: 14px; height: 24px; width: 160px;">
                                <option value="0" selected="selected">全部</option>
                                <option value="2">2-不满足库存条件</option>
                                <option value="3">3-销量不合格</option>
                                <option value="4">4-1688页面404或者货源下架</option>
                                <option value="5">5-重复验证合格(原本1688异常下架后重复验证合格上线的商品)</option>
                                <option value="6">6-IP问题或运营直接下架</option>
                                <option value="7">7-店铺整体禁掉</option>
                                <option value="8">8-采样不合格</option>
                                <option value="9">9-有质量问题</option>
                                <option value="10">10-商品侵权</option>
                                <option value="11">11-店铺侵权</option>
                                <option value="12">12-难看</option>
                                <option value="13">13-中文</option>
                                <option value="14">14-1688商品货源变更</option>
                                <option value="15">15-除服装珠宝分类外的非精品数据更新到软下架</option>
                                <option value="16">16-搜索展现点击比+添加购物车数据 指标不符合要求</option>
                                <option value="17">17-低价商品下架</option>
                                <option value="18">18-类别隐藏数据下架</option>
                                <option value="19">19-店铺小于5件商品软下架</option>
                                <option value="20">20-按手起批</option>
                                <option value="21">21-大于400美元商品下架</option>
                                <option value="22">22-原因老数据没有展示详情图片</option>
                                <option value="23">23-对应1688商品成交量小于4</option>
                                <option value="24">24-同款下架</option>
                                <option value="27">27-商品无详情图(批量删除中文图)</option>
                                <option value="28">28-定制商品</option>
                                <option value="29">29-清洗中判断的商品异常下架(比如去除一手等规格后剩余规格不可卖)</option>
                                <option value="30">30-大于40美元商品软下架(不包含婚纱礼服)</option>
                                <option value="31">31-整店下架</option>
                                <option value="32">32-因为中文或者画质下架</option>
                        </select>
        </span>
       <%-- <span> 永不下架标识: <select id="query_never_off" style="font-size: 14px; height: 24px; width: 90px;">
                                <option value="0" selected="selected">全部</option>
                                <option value="1">永不下架</option>
                        </select>
        </span>--%>
            <br />
        <span> 其他筛选: <select id="query_sold_flag" style="font-size: 14px; height: 24px; width: 120px;">
                                <option value="0" selected="selected">不进行筛选</option>
                                <option value="3">我们公司卖过的 或者 购物车中商品</option>
                                <option value="1">我们公司卖过的</option>
                                <option value="2">购物车中商品</option>
                                <option value="4">人为编辑过</option>
                                <option value="5">有库存的</option>
                                <option value="6">有人为对标的</option>
                        </select>
            </span>
            <span> 有跨境图片包: <select id="query_sold_flag2" style="font-size: 14px; height: 24px; width: 120px;">
                                <option value="0" selected="selected">不进行筛选</option>
                                <option value="1">有跨境图片包</option>
                        </select>
            </span>
            <span> 数据源筛选
                <%--<a href="https://img.import-express.com/importcsvimg/stock_picture/researchimg/1554172194483.70268.zip">原始文档</a>--%>:
                <select id="query_source" style="font-size: 14px; height: 24px; width: 120px;">
                                <option value="-1" selected="selected">不进行筛选</option>
                                <%--<option value="1">1-库存验证中校验的上下架</option>
                                <option value="2">2-商品拯救行动V4中F点要拯救的商品</option>
                                <option value="3">3-后台标注的定时上下架的</option>--%>
                                <option value="5">5-需要人为确认后下架(在购物车、卖过的、Favorite、描述很精彩)</option>
                        </select>
            </span>
            <span> 是否处理过: <select id="query_sold_flag3" style="font-size: 14px; height: 24px; width: 120px;">
                                <option value="0" selected="selected">不进行筛选</option>
                                <option value="1">处理过的</option>
                                <option value="2">未处理过</option>
                        </select>
                </span>
        </span>
        <span><input type="button" class="enter_btn" value="查询" onclick="doQuery()"/></span>
                <br />
                <span>
                    <a href="#" onclick="updateFlag3()" style="text-decoration: none;position: relative;top: 12px;line-height: 14px;">将选择的标记为处理过</a>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <a target="_blank" href="/cbtconsole/website/singleGoods.jsp" style="text-decoration: none;position: relative;top: 12px;line-height: 14px;">链接:单个商品上线</a>
                </span>
    </form>
</div>

<table id="neef_off_easyui-datagrid" style="width: 100%; height: 100%;"
       class="easyui-datagrid">
    <thead>
    <tr>
        <th field="ck" checkbox="true"></th>
        <th data-options="field:'pid',align:'center',width:'110px',formatter:formatPid">PID</th>
        <th data-options="field:'imgUrl',align:'center',width:'180px',formatter:formatImg">商品主图</th>
        <th data-options="field:'catidName',align:'center',width:'150px',formatter:formatCatid">类别</th>
        <th data-options="field:'isOffShelf',align:'center',width:'150px',formatter:formatIsOffShelf">当前在线状态</th>
        <%--<th data-options="field:'competitiveFlag',align:'center',width:'150px',formatter:formatCompetitiveFlag">精品标识</th>--%>
        <%--<th data-options="field:'neverOffFlag',align:'center',width:'150px',formatter:formatneverOffFlag">永不下架</th>--%>
        <th data-options="field:'reason',align:'center',width:'150px',formatter:formatReason">下架原因</th>
        <th data-options="field:'createtime',align:'center',width:'200px'">首次上架时间</th>
        <th data-options="field:'updateTime',align:'center',width:'200px'">上下架时间</th>
        <th data-options="field:'opFlag',align:'center',width:'200px',formatter:formatOperation">操作</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>
<script type="text/javascript">
    // 商品下架原因
    $.ajax({
        type: "GET",
        url: "/cbtconsole/queryuser/queryUnsellablereasonMaster.do",
        async: false,
        dataType:"json",
        success: function(msg){
            if(msg.state == true) {
                window.UNSELLABLEREASON = {};
                var query_reason = $("#query_reason");
                query_reason.empty();
                $("<option>", {
                    value: "0",
                    selected: "selected",
                    text: "全部"
                }).appendTo(query_reason);
                $(msg.reason).each(function (key, val) {
                    window.UNSELLABLEREASON[val.id] = val.name;
                    $("<option>", {
                        value: val.id,
                        text: val.id + "-" + val.name
                    }).appendTo(query_reason);
                });
            }
        }
    });


    $("#refreshData").click(function () {
        alert("临时查询数据刷新中 预计30s!");
        $.ajax({
            type: "GET",
            url: "/cbtconsole/queryuser/refreshNeedOffShelfData.do",
            dataType:"json",
            success: function(msg){
                alert(msg.message);
            }
        });
    });

    jQuery(function($){
        // 页面加载完成 初始加载 5-需要下架但在购物车或卖过的人为确认后下架 未处理过的
        $("#query_source").val('5');
        $("#query_sold_flag3").val('2');
        doQuery();
    });
</script>

</body>
</html>