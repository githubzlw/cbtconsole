<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>在线商品审核</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
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
            height: 30px;
            font-size: 24px;
        }

        #online_check_query_form {
            font-size: 18px;
        }

        .panel-title {
            text-align: center;
            height: 30px;
            font-size: 24px;
        }

        #online_check_top_toolbar {
            padding: 20px 28px !important;
        }

        .datagrid-header .datagrid-cell span, .datagrid-cell,
        .datagrid-cell-group, .datagrid-header-rownumber,
        .datagrid-cell-rownumber {
            font-size: 14px;
        }

        .img_sty {
            max-height: 180px;
            max-width: 180px;
        }

        .enter_btn {
            width: 140px;
            height: 30px;
            color: #ffffff;
            background-color: #009688;
            transform: rotate(0deg);
            font-size: 16px;
            border-radius: 4px;
            border-width: 1px;
            border-style: solid;
            text-align: center;
            font-weight: normal;
            font-style: normal;
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

        .easyui-tree b {
            color: red;
        }

        .sty_select {
            font-size: 16px;
            height: 26px;
            width: 90px;
        }
    </style>
    <script type="text/javascript">
        var sessionStorage = window.sessionStorage;
        var queryParams = {"pid": "", "catid": "0", "clickNum": "0"};
        var nDivHight = 0;
        var nScrollTop = 0;  //滚动到的当前位置
        var tempNScrollTop = 0;

        $(document).ready(function () {
            var pid = sessionStorage.getItem("pid");
            if (!(pid == null || pid == "")) {
                queryParams.pid = pid;
                $("#query_pid").val(pid);
            }
            var catid = sessionStorage.getItem("catid");
            if (!(catid == null || catid == "" || catid == "0")) {
                queryParams.catid = catid;
                $("#query_catid").val(catid);
            }
            var clickNum = sessionStorage.getItem("clickNum");
            if (!(clickNum == null || clickNum == "")) {
                queryParams.clickNum = clickNum;
                $("#query_click_num").val(clickNum);
            }
            genCatergoryTree();
            setDatagrid();
            var opts = $("#online_check_easyui-datagrid").datagrid("options");
            opts.url = "/cbtconsole/onlineGoodsCtr/queryForList";
            loadDataByParam();
            setImgLazyLoad();
        });

        function setImgLazyLoad() {

            $(".datagrid-body").scroll(function () {
                nDivHight = $(this)[0].scrollHeight;
                tempNScrollTop = $(this)[0].scrollTop;
                if (tempNScrollTop - nScrollTop > 600 || nDivHight - nScrollTop < 200) {
                    nScrollTop = tempNScrollTop;
                    imgLazyLoad();
                }
            });
        }

        function setDatagrid() {
            $('#online_check_easyui-datagrid').datagrid({
                title: '在线商品审核(点击图片放大)',
                width: "100%",
                fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                toolbar: "#online_check_top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
                url: '',//url调用Action方法
                loadMsg: '数据装载中......',
                singleSelect: true,//为true时只能选择单行
                fitColumns: true,//允许表格自动缩放，以适应父容器
                rownumbers: true,
                nowrap: false,
                pageSize: 50,//默认选择的分页是每页50行数据
                pageList: [50, 100],//可以选择的分页集合
                pagination: true,//分页
                style: {
                    padding: '8 8 10 8'
                },
                onLoadError: function () {
                    $.messager.alert("提示信息", "获取数据信息失败");
                    return;
                },
                onLoadSuccess: function (data) {
                    if (data.success) {
                        $('#online_check_easyui-datagrid').datagrid("selectRow", 0);

                        setTimeout(function () {
                            imgLazyLoad();
                            nScrollTop = 0;
                            nDivHight = 0;
                            tempNScrollTop = 0;
                        }, 400);
                    } else {
                        $.messager.alert(data.message);
                    }
                }
            });
        }

        function doQueryWithButton() {
            var clickNum = $("#query_click_num").val();
            var pid = $("#query_pid").val();
            queryParams.catid = "0";
            queryParams.pid = pid;
            queryParams.clickNum = clickNum;

            sessionStorage.setItem("catid", "0");
            sessionStorage.setItem("pid", pid);
            sessionStorage.setItem("clickNum", queryParams.clickNum);

            loadDataByParam();
            genCatergoryTree();
        }

        function genCatergoryTree() {
            var nodeId = queryParams.catid;
            $(".easyui-tree").hide();
            $('.easyui-tree').tree({
                url: "/cbtconsole/onlineGoodsCtr/genCategoryTree",
                animate: true,
                lines: true,
                method: "post",
                queryParams: {"pid": queryParams.pid, "clickNum": queryParams.clickNum},
                onClick: function (node) {
                    sessionStorage.setItem("catid", node.id);
                    queryParams.catid = node.id;
                    loadDataByParam();
                },
                onBeforeExpand: function (node) {
                    if (node.children.length == 0) {
                        return false;
                    }
                },
                onLoadSuccess: function (node, data) {
                    $('.easyui-tree').find('.tree-node-selected').removeClass('tree-node-selected');
                    if (data.length > 0) {
                        if (nodeId == null || nodeId == "" || nodeId == "0") {
                            var nd = $('.easyui-tree').tree('find', data[0].id);
                            $('.easyui-tree').tree('select', nd.target);
                        } else {
                            var clNode = $('.easyui-tree').tree('find', nodeId);
                            if (clNode) {

                                var parentNode = $('.easyui-tree').tree("getParent", clNode.target);
                                if (parentNode != null && parentNode != "undefined") {
                                    var firstNode = $('.easyui-tree').tree("getParent", parentNode.target);
                                    if (firstNode != null && firstNode != "undefined") {
                                        $('.easyui-tree').tree('expand', firstNode.target);
                                    }
                                    $('.easyui-tree').tree('expand', parentNode.target);
                                }
                                setTimeout(function () {
                                    $('.easyui-tree').tree('select', clNode.target);
                                }, 500);
                            }
                        }
                    }
                    $(".easyui-tree").show();
                }
            });
        }


        function loadDataByParam() {
            $("#online_check_easyui-datagrid").datagrid("load", queryParams);
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

        function imgLazyLoad() {
            $('img.img_sty').lazyload({effect: "fadeIn", threshold: 69});
        }

        function formatCatid(val, row, index) {

            return '<span>ID:' + val + '</span><br><br><span>名称:' + row.categoryName + '</span>';

        }

        function formatImgEinfo(val, row, index) {
            var content = '';
            if(val && val.length > 10){
                content = '<div><img class="img_sty" src="/cbtconsole/img/beforeLoad.gif" data-original="'
                + val + '" onclick="bigImg(\'' + val + '\')"/>';
            }
            return content;
        }

        function formatImgWindow(val, row, index) {
            var content = '';
            if(val && val.length > 10){
                content = '<div><img class="img_sty" src="/cbtconsole/img/beforeLoad.gif" data-original="'
                +  val + '" onclick="bigImg(\'' + val + '\')"/>';
            }
            return content;
        }

        function formatPid(val, row, index) {
            return '<a target="_blank" href="/cbtconsole/editc/detalisEdit?pid=' + val + '">' + val + '</a>';
        }
    </script>

</head>
<body style="overflow-y: hidden">

<div class="div_sty_img" style="display: none;" id="big_img"></div>

<div id="online_check_left_toolbar" style="float: left;width: 13%;height: 933px;">
    <div class="easyui-panel" style="padding: 5px; height: 100%">
        <ul class="easyui-tree">
        </ul>
    </div>
</div>


<div style="float: right;width: 87%;height: 966px;">
    <div id="online_check_top_toolbar" style="padding: 5px; height: auto">
        <form id="online_check_query_form" action="#" onsubmit="return false;">
		<span> PID: <input type="text" id="query_pid"
                           style="width: 160px; height: 20px" value=""/></span>
            <span style="display: none;"> 类别ID: <input type="text" id="query_catid"
                                                       style="width: 130px; height: 20px" value=""/></span>
            <span> 点击数: <select id="query_click_num" class="sty_select">
                <option value="0" selected="selected">全部</option>
                <option value="1"><10</option>
                <option value="2">≥10且<50</option>
                <option value="3">≥50且<100</option>
                <option value="4">>100</option>
        </select></span>
            &nbsp;&nbsp;&nbsp;&nbsp; <span><input type="button" class="enter_btn" value="查询"
                                                  onclick="doQueryWithButton()"/></span>
        </form>
    </div>


    <table id="online_check_easyui-datagrid" style="width: 100%; height: 100%;"
           class="easyui-datagrid">
        <thead>
        <tr>
            <th data-options="field:'pid',width:'150px',formatter:formatPid">PID</th>
            <th data-options="field:'catid',align:'center',width:'170px',formatter:formatCatid">类别</th>
            <th data-options="field:'shopId',align:'center',width:'160px'">店铺</th>
            <%--<th data-options="field:'mainImg',width:'220px',formatter:formatMainImg">搜索图</th>--%>
            <th data-options="field:'imgShow',align:'center',width:'240px',formatter:formatImgWindow">橱窗图</th>
            <th data-options="field:'eninfoShow1',align:'center',width:'240px',formatter:formatImgEinfo">详情图1</th>
            <th data-options="field:'eninfoShow2',align:'center',width:'240px',formatter:formatImgEinfo">详情图2</th>
            <th data-options="field:'eninfoShow3',align:'center',width:'240px',formatter:formatImgEinfo">详情图3</th>
            <th data-options="field:'eninfoShow4',align:'center',width:'240px',formatter:formatImgEinfo">详情图4</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>

</div>


</body>
</html>