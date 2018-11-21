<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>跨境商品上线审核</title>
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

        #single_check_query_form {
            font-size: 18px;
        }

        .panel-title {
            text-align: center;
            height: 30px;
            font-size: 24px;
        }

        #single_check_top_toolbar {
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

        .btn_check {
            width: 120px;
            height: 30px;
            background-color: red;
            font-size: 16px;
            padding: 0px;
            border-radius: 0px;
            border-width: 1px;
            border-style: solid;
            text-align: center;
            line-height: 20px;
            font-weight: bold;
            font-style: normal;
        }

        .shop_check {
            width: 120px;
            height: 30px;
            background-color: #27ec27;
            font-size: 16px;
            padding: 0px;
            border-radius: 0px;
            border-width: 1px;
            border-style: solid;
            text-align: center;
            line-height: 20px;
            font-weight: bold;
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

        .chk_span {
            font-size: 15px;
        }

        .chk_sty {
            width: 20px;
            height: 18px;
        }

        .sty_select {
            font-size: 16px;
            height: 26px;
            width: 90px;
        }
    </style>
    <script type="text/javascript">
        var ipStr = location.href;
        var sessionStorage = window.sessionStorage;
        var queryParams = {"pid": "", "catid": "0", "isPass": "-1", "isUpdate": "-1","ip":ipStr};
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
            var isPass = sessionStorage.getItem("isPass");
            if (!(isPass == null || isPass == "")) {
                queryParams.isPass = isPass;
                $("#query_is_pass").val(isPass);
            }
            var isUpdate = sessionStorage.getItem("isUpdate");
            if (!(isUpdate == null || isUpdate == "")) {
                queryParams.isUpdate = isUpdate;
                $("#query_is_update").val(isUpdate);
            }
            genCatergoryTree();
            setDatagrid();
            var opts = $("#single_check_easyui-datagrid").datagrid("options");
		    opts.url = "/cbtconsole/singleGoods/queryCrossBorderGoodsForList";
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
            $('#single_check_easyui-datagrid').datagrid({
                title: '跨境商品上线审核(点击图片放大)',
                width: "100%",
                fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                toolbar: "#single_check_top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
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
                    $.message.alert("提示信息", "获取数据信息失败");
                    return;
                },
                onLoadSuccess: function (data) {
                    if (data.success) {
                        //$('.img_sty').lazyload({effect: "fadeIn"});
                        //$(".datagrid-body").css({"position":"fixed","top":140});
                        $('#single_check_easyui-datagrid').datagrid("selectRow",0);

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
            var isPass = $("#query_is_pass").val();
            var isUpdate = $("#query_is_update").val();
            var pid = $("#query_pid").val();
            queryParams.catid = "0";
            queryParams.pid = pid;
            queryParams.isPass = isPass;
            queryParams.isUpdate = isUpdate;

            sessionStorage.setItem("catid", "0");
            sessionStorage.setItem("pid", pid);
            sessionStorage.setItem("isPass", queryParams.isPass);
            sessionStorage.setItem("isUpdate", queryParams.isUpdate);

            loadDataByParam();
            genCatergoryTree();
        }

        function genCatergoryTree() {
            var nodeId = queryParams.catid;
            $(".easyui-tree").hide();
            $('.easyui-tree').tree({
                url: "/cbtconsole/singleGoods/genCatergoryTree",
                animate: true,
                lines: true,
                method: "post",
                queryParams: {"pid": queryParams.pid,"isPass": queryParams.isPass, "isUpdate": queryParams.isUpdate},
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
                                if(parentNode != null && parentNode != "undefined"){
                                    var firstNode = $('.easyui-tree').tree("getParent", parentNode.target);
                                    if(firstNode != null && firstNode != "undefined"){
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

        function choosePidMainImg(pid, imgUrl, obj) {
            $(".pid_check_" + pid).each(function (i, item) {
                $(this).prop("checked", false);
            });
            $.ajax({
                type: "POST",
                url: "/cbtconsole/singleGoods/setMainImgByPid",
                data: {
                    "pid": pid,
                    "imgUrl": imgUrl
                },
                success: function (data) {
                    if (data.ok) {
                        $(obj).prop("checked", true);
                    } else {
                        $.messager.alert("提醒", '执行错误:' + data.message, "info");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", '执行错误，请联系管理员', "error");
                }
            });
        }


        function chooseShopMainImg(shopId, imgUrl, obj) {
            $(".shop_check_" + shopId).each(function (i, item) {
                $(this).prop("checked", false);
            });
            $(".pid_shop_" + shopId).prop("checked", false);
            $.ajax({
                type: "POST",
                url: "/cbtconsole/singleGoods/setMainImgByShopId",
                data: {
                    "shopId": shopId,
                    "imgUrl": imgUrl
                },
                success: function (data) {
                    if (data.ok) {
                        $(obj).prop("checked", true);
                    } else {
                        $.messager.alert("提醒", '执行错误:' + data.message, "info");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", '执行错误，请联系管理员', "error");
                }
            });
        }

        function loadDataByParam() {
            $("#single_check_easyui-datagrid").datagrid("load", queryParams);
        }

        function reloadData() {
            //queryParams
            var options = $("#single_check_easyui-datagrid").datagrid('getPager').data("pagination").options;
            //当前页
            queryParams.page = options.pageNumber;
            $("#single_check_easyui-datagrid").datagrid("reload", queryParams);
        }


        function doAllCheck() {
            var options = $("#single_check_easyui-datagrid").datagrid('getPager').data("pagination").options;
            //当前页
            var page = options.pageNumber;
            $.ajax({
                type: "POST",
                url: "/cbtconsole/singleGoods/batchInsertIntoSingleGoods",
                data: {
                    "pid": "",
                    "catid": queryParams.catid,
                    "page": page,
                    "isPass": queryParams.isPass,
                    "isUpdate": queryParams.isUpdate
                },
                success: function (data) {
                    if (data.ok) {
                        //$.messager.alert("提醒","执行成功,请继续操作", "info");
                        $.messager.alert("提醒", "执行成功，页面即将刷新！", "info");
                        setTimeout(function () {window.location.reload();}, 300);
                        //reloadData();
                    } else {
                        $.messager.alert("提醒", '执行错误:' + data.message, "info");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", '执行错误，请联系管理员', "error");
                }
            });
        }

        function doSameShopCheck(shopId, type) {
            if (shopId == '' || shopId == null || shopId.length == 0) {
                $.messager.alert("提醒", "无店铺ID", "info");
            } else {
                var noticeTx = '';
                if (type == 0) {
                    noticeTx = '通过?';
                } else {
                    noticeTx = '不通过?';
                }
                $.messager.confirm('系统提醒', '是否确认执行' + noticeTx, function (r) {
                    if (r) {
                        $.ajax({
                            type: "POST",
                            url: "/cbtconsole/singleGoods/dealSameShopSingleGoods",
                            data: {
                                "shopId": shopId,
                                "type": type
                            },
                            success: function (data) {
                                if (data.ok) {
                                    //$.messager.alert("提醒", "执行成功，页面即将刷新！", "info");
                                    //setTimeout(function () {window.location.reload();}, 300);
                                    //reloadData();
                                    hideButton('', shopId, 1, null,type);
                                } else {
                                    $.messager.alert("提醒", '执行错误:' + data.message, "info");
                                }
                            },
                            error: function (res) {
                                $.messager.alert("提醒", '执行错误，请联系管理员', "error");
                            }
                        });
                    }
                });
            }
        }


        function updateSingleGoodsCheck(pid,shopId,ojb) {

            $.ajax({
                type: "POST",
                url: "/cbtconsole/singleGoods/updateSingleGoodsCheck",
                data: {
                    "pid": pid,
                    "isPass": 1
                },
                success: function (data) {
                    if (data.ok) {
                        //$.messager.alert("提醒", "执行成功，页面即将刷新！", "info");
                        //setTimeout(function () {window.location.reload();}, 300);
                       //reloadData();
                        hideButton(pid,shopId,0,ojb,0);
                    } else {
                        $.messager.alert("提醒", '执行错误:' + data.message, "info");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", '执行错误，请联系管理员', "error");
                }
            });

            /*$.messager.confirm('系统提醒', '是否确认不通过?', function (r) {
                if (r) {

                }
            });*/
        }


        function hideButton(pid,shopId,isPid,ojb,isPass) {
            if(type == 0){
                $(".notice_" + pid).text('已审核,不通过');
                $(ojb).parent().find(".shop_check").hide();
                $(ojb).parent().find(".btn_check").hide();
            }else{
                $(".shop_btn_" + shopId).hide();
                $(".pid_btn_" + shopId).hide();
                if(isPass == 0){
                    $(".notice_" + shopId).text('已审核,已通过');
                }else{
                    $(".notice_" + shopId).text('已审核,不通过');
                }
            }
        }

        function isUpdateChange() {
            var isUpdate = $("#query_is_update").val();
            if (isUpdate == 0) {
                $("#query_is_pass").val(-1);
                $("#query_is_pass").prop("disabled", true);
                $("#query_pid").val("");
                //$("#end_check").show();
            } else if (isUpdate == 1) {
                //$("#end_check").hide();
                $("#query_is_pass").prop("disabled", false);
            }
        }

        function isPassChange() {
            var isPass = $("#query_is_pass").val();
            if (isPass > 0) {
                $("#query_is_update").val(1);
                $("#query_is_update").prop("disabled", true);
            } else {
                $("#query_is_update").val(-1);
                $("#query_is_update").prop("disabled", false);
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

        function imgLazyLoad() {
            $('img.img_sty').lazyload({effect: "fadeIn", threshold: 69});
        }


        function formatWindowImg1(val, row, index) {
            var content = '<div><img class="img_sty" src="/cbtconsole/img/beforeLoad.gif" data-original="'
                + val + '" onclick="bigImg(\'' + val + '\')"/>';
            if (row.isUpdate > -1) {
                content += '<br><span class="chk_span">选为搜索图<input type="checkbox" class="chk_sty pid_check_' + row.pid + '"';
                if (row.isUpdate > 0) {
                    content += ' onclick="choosePidMainImg(\'' + row.pid + '\',\'' + val + '\',this)" ';
                }
                content += (row.imgCheck == 1 ? 'checked="checked"' : '') + '/></span>';
                content += '<span class="chk_span">同店铺搜索图<input type="checkbox" class="chk_sty shop_check_' + row.shopId + '"';
                if (row.isUpdate > 0) {
                    content += ' onclick="chooseShopMainImg(\'' + row.shopId + '\',\'' + val + '\',this)" ';
                }
                content += (row.shopCheck == 1 ? 'checked="checked"' : '') + '/></span>';
                content += '</div>';
            }
            return content;
        }

        function formatWindowImg2(val, row, index) {
            var content = '<div><img class="img_sty" src="/cbtconsole/img/beforeLoad.gif" data-original="'
                + val + '" onclick="bigImg(\'' + val + '\')"/>';
            if (row.isUpdate > -1) {
                content += '<br><span class="chk_span">选为搜索图<input type="checkbox" class="chk_sty pid_check_' + row.pid + '"';
                if (row.isUpdate > 0) {
                    content += ' onclick="choosePidMainImg(\'' + row.pid + '\',\'' + val + '\',this)" ';
                }
                content += (row.imgCheck == 2 ? 'checked="checked"' : '') + '/></span>';
                content += '<span class="chk_span">同店铺搜索图<input type="checkbox" class="chk_sty shop_check_' + row.shopId + '"';
                if (row.isUpdate > 0) {
                    content += ' onclick="chooseShopMainImg(\'' + row.shopId + '\',\'' + val + '\',this)" ';
                }
                content += (row.shopCheck == 2 ? 'checked="checked"' : '') + '/></span>';
                content += '</div>';
            }
            return content;
        }

        function formatWindowImg3(val, row, index) {
            var content = '<div><img class="img_sty" src="/cbtconsole/img/beforeLoad.gif" data-original="'
                + val + '" onclick="bigImg(\'' + val + '\')"/>';
            if (row.isUpdate > -1) {
                content += '<br><span class="chk_span">选为搜索图<input type="checkbox" class="chk_sty pid_check_' + row.pid + '"';
                if (row.isUpdate > 0) {
                    content += ' onclick="choosePidMainImg(\'' + row.pid + '\',\'' + val + '\',this)" ';
                }
                content += (row.imgCheck == 3 ? 'checked="checked"' : '') + '/></span>';
                content += '<span class="chk_span">同店铺搜索图<input type="checkbox" class="chk_sty shop_check_' + row.shopId + '"';
                if (row.isUpdate > 0) {
                    content += ' onclick="chooseShopMainImg(\'' + row.shopId + '\',\'' + val + '\',this)" ';
                }
                content += (row.shopCheck == 3 ? 'checked="checked"' : '') + '/></span>';
                content += '</div>';
            }
            return content;
        }

        function formatCatid(val, row, index) {

            return '<span>ID:'+val+'</span><br><br><span>名称:'+row.categoryName+'</span>';

        }

        function formatMainImg(val, row, index) {
            var content = '<div><img class="img_sty" src="/cbtconsole/img/beforeLoad.gif" data-original="'
                + val + '" onclick="bigImg(\'' + val + '\')"/>';
            if (row.isUpdate > -1) {
                content += '<br><span class="chk_span">选为搜索图<input type="checkbox" '
                    +'class="chk_sty pid_check_' + row.pid + ' pid_shop_' + row.shopId +  '"';
                if (row.isUpdate > 0) {
                    content += ' onclick="choosePidMainImg(\'' + row.pid + '\',\'' + val + '\',this)" ';
                } else {
                    content += ((row.imgCheck == 0 && row.shopCheck == 0) ? 'checked="checked"' : '') + '/></span></div>';
                }
            }
            return content;
        }

        function formatPid(val, row, index) {
            return '<a target="_blank" href="https://detail.1688.com/offer/' + val + '.html">' + val + '</a>';
        }

        function formatOperation(val, row, index) {
            var content = '';
            if (val > 0) {
                content += '<b style="color:green;font-size:18px;">已审核</b>';
                if (row.isPass == 2) {
                    content += '<em>,</em><b style="color:green;font-size:18px;">已通过</b>';
                } else if (row.isPass == 1) {
                    content += '<em>,</em><b style="color:red;font-size:18px;">不通过</b>';
                }
            } else {
                content += '<b class="notice_' + row.pid + ' notice_' + row.shopId + '" style="color:red;font-size:18px;">未审核</b>';
                content += '<br><br><input type="button" class="shop_check shop_btn_' + row.shopId + '" value="同店铺一键全过" '
                    + 'onclick="doSameShopCheck(\'' + row.shopId + '\',0)">';
                content += '<br><br><input type="button" class="btn_check pid_btn_' + row.shopId + '" value="检查不通过" '
                    + 'onclick="updateSingleGoodsCheck(\'' + row.pid + '\',\'' + row.shopId + '\',this)">';
                content += '<br><br><input type="button" class="btn_check pid_btn_' + row.shopId + '" value="同店铺一键全否" '
                    + 'onclick="doSameShopCheck(\'' + row.shopId + '\',1)">';
            }
            return content;
        }
    </script>

</head>
<body style="overflow-y: hidden">

<div class="div_sty_img" style="display: none;" id="big_img"></div>

<div id="single_check_left_toolbar" style="float: left;width: 13%;height: 933px;">
    <div class="easyui-panel" style="padding: 5px; height: 100%">
        <ul class="easyui-tree">
        </ul>
    </div>
</div>


<div style="float: right;width: 87%;height: 966px;">
    <div id="single_check_top_toolbar" style="padding: 5px; height: auto">
        <form id="single_check_query_form" action="#" onsubmit="return false;">
		<span> PID: <input type="text" id="query_pid"
                                                  style="width: 160px; height: 20px" value=""/></span>
            <span style="display: none;"> 类别ID: <input type="text" id="query_catid"
                                                       style="width: 130px; height: 20px" value=""/></span>
            <span> 是否审核: <select id="query_is_update" class="sty_select" onchange="isUpdateChange()">
                <option value="-1" selected="selected">全部</option>
                <option value="0">未审核</option>
                <option value="1">已审核</option>
        </select></span>
            &nbsp;&nbsp;&nbsp;&nbsp;<span> 是否通过: <select id="query_is_pass" class="sty_select"
                                                         onchange="isPassChange()">
                <option value="-1" selected="selected">全部</option>
                <option value="1">未通过</option>
                <option value="2">已通过</option>
        </select></span>

            &nbsp;&nbsp;&nbsp;&nbsp; <span><input type="button" class="enter_btn" value="查询"
                                                  onclick="doQueryWithButton()"/></span>
            &nbsp;&nbsp;&nbsp;&nbsp; <span id="end_check">
            <input type="button" class="enter_btn" value="当前页检查完成" onclick="doAllCheck()"/>
            <b style="color: red;">*请检查完成后点击此按钮</b>
        </span>

            </span>
        </form>
    </div>


    <table id="single_check_easyui-datagrid" style="width: 100%; height: 100%;"
           class="easyui-datagrid">
        <thead>
        <tr>
            <th data-options="field:'pid',width:'150px',formatter:formatPid">PID</th>
            <th data-options="field:'catid',align:'center',width:'170px',formatter:formatCatid">类别</th>
            <th data-options="field:'shopId',align:'center',width:'160px'">店铺</th>
            <%--<th data-options="field:'mainImg',width:'220px',formatter:formatMainImg">搜索图</th>--%>
            <th data-options="field:'imgShow',align:'center',width:'240px',formatter:formatMainImg">橱窗图</th>
            <th data-options="field:'eninfoShow1',align:'center',width:'240px',formatter:formatWindowImg1">跨境详情图1</th>
            <th data-options="field:'eninfoShow2',align:'center',width:'240px',formatter:formatWindowImg2">跨境详情图2</th>
            <th data-options="field:'eninfoShow3',align:'center',width:'240px',formatter:formatWindowImg3">跨境详情图3</th>
            <th data-options="field:'isUpdate',align:'center',width:'180px',formatter:formatOperation">操作</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>

</div>


</body>
</html>