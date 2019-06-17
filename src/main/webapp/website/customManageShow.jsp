<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
    <title>批量产品删图和下架</title>
    <style type="text/css">
        select{
            width: 125px;
        }
        a {
            cursor: pointer;
            text-decoration: underline;
        }
        b {
            color: red;
        }

        .btn {
            color: #fff;
            background-color: #5db5dc;
            border-color: #2e6da4;
            padding: 5px 10px;
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
            background-color: #d4d3d3 !important;
        }

        .tr_isEdited {
            background-color: #45f959;
        }
    </style>
    <script type="text/javascript">

        var sessionStorage = window.sessionStorage;
        /*除了第一行外 其他的都需要设置成默认值，同 产品库中分支树同样使用方式*/
        var queryParams = {
            "isBenchmark":"-1", "isEdited":"-1", "page":"1", "catid":"0", "valid":"-1",
            "state":"0", "sttime":"", "edtime":"", "adminid":"0",
            "isAbnormal":"-1", "weightCheck":"-1",
            "bmFlag":"0",  "sourceProFlag":"0", "priorityFlag":"0","soldFlag":"-1",
            "addCarFlag":"0","sourceUsedFlag":"-1", "ocrMatchFlag":"0", "infringingFlag":"-1",
            "aliWeightBegin":"","aliWeightEnd":"","onlineTime":"","offlineTime":"","editBeginTime":"","editEndTime":"",
            "weight1688Begin":"","weight1688End":"","price1688Begin":"","price1688End":"","isSort":"0",
            "unsellableReason":"-1","fromFlag":"-1","finalWeightBegin":"","finalWeightEnd":"",
            "minPrice":"","maxPrice":"","isSoldFlag":"-1","isWeigthZero":"0","isWeigthCatid":"0",
            "shopId":"","chKeyWord":""
        };
        $(function() {
            queryParams.valid = 1;
            $("#query_valid").val('1');

            $("#query_is_benchmark").change(function(){
                sessionStorage.setItem("isBenchmark", $("#query_is_benchmark").val());
                queryParams.isBenchmark = $("#query_is_benchmark").val();
            });
            $("#query_is_edited").change(function(){
                sessionStorage.setItem("isEdited", $("#query_is_edited").val());
                queryParams.isEdited = $("#query_is_edited").val();
            });
            $("#query_valid").change(function(){
                sessionStorage.setItem("valid", $("#query_valid").val());
                queryParams.valid = $("#query_valid").val();
            });

            // 编辑状态
            var isBenchmark = sessionStorage.getItem("isBenchmark");
            if(!(isBenchmark == null || isBenchmark == "" || isBenchmark == "-1")){
                queryParams.isBenchmark = isBenchmark;
                $("#query_is_benchmark").val(isBenchmark);
            }
            // 货源对标情况
            var isEdited = sessionStorage.getItem("isEdited");
            if(!(isEdited == null || isEdited == "" || isEdited == "-1")){
                queryParams.isEdited = isEdited;
                $("#query_is_edited").val(isEdited);
            }
            // 在线状态
            var valid = sessionStorage.getItem("valid");
            if(!(valid == null || valid == "" || valid == "-1")){
                queryParams.valid = valid;
                $("#query_valid").val(valid);
            }
            // 页码
            var page = sessionStorage.getItem("page");
            if(!(page == null || page == "" || page == "0")){
                queryParams.page = page;
            }
            createCateroryTree(queryParams.catid);
        });

        function createCateroryTree(nodeId){
            $('.easyui-tree').tree({
                url : "/cbtconsole/cutom/queryCategoryTree",
                animate:true,
                lines:true,
                method : "post",
                queryParams: queryParams,
                onClick: function(node){
                    queryParams.catid = node.id;
                    queryParams.page = 1;
                    doQueryList();
                },
                onBeforeExpand: function(node){
                    if(node.children.length ==0){
                        showMessage('当前节点没有子节点');
                        return false;
                    }
                },
                onLoadSuccess:function(node,data){
                    $('.easyui-tree').find('.tree-node-selected').removeClass('tree-node-selected');
                    if(data.length > 0){
                        if(nodeId==null || nodeId=="" || nodeId=="0"){
                            var nd = $('.easyui-tree').tree('find', data[0].id);
                            $('.easyui-tree').tree('select', nd.target);
                        }else{
                            var clNode = $('.easyui-tree').tree('find', nodeId);
                            if(clNode){
                                $('.easyui-tree').tree('select', clNode.target);
                            }
                        }
                    }
                    $(".easyui-tree").show();
                }
            });
        }

        function doQueryWidthJump(){
            if ($("#query_source").val() == '1') {
                queryParams.page = "1";
                queryParams.catid = "0";
                $(".easyui-tree").hide();
                createCateroryTree(queryParams.catid);
            } else if ($("#query_source").val() != '1') {
                var url = "/cbtconsole/cutom/cmslist?page=" + queryParams.page
                    + "&source=" + $("#query_source").val();
                console.log(url);
                $('#goods_list').attr('src',encodeURI(url));
            }
        }

        function parentDoQuery(page){
            queryParams.page = page;
            doQueryList();
        }

        function doQueryList(){
            $('#goods_list').empty();
            var url = "/cbtconsole/cutom/cmslist?page=" + queryParams.page
                + "&catid=" + queryParams.catid
                + "&isBenchmark=" + queryParams.isBenchmark
                + "&valid=" + queryParams.valid
                + "&isEdited=" + queryParams.isEdited;
            $('#goods_list').attr('src',encodeURI(url));
        }

        function showMessage(msgStr) {
            $.messager.show({
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

        function reloadHtml(){
            window.location.reload();
        }
    </script>

</head>

<body class="easyui-layout">

<c:if test="${uid > 0}">
    <div data-options="region:'north',split:true,border:false" style="height:50px;">
        <div style="padding-top: 14px;font-size: 18px;">
            当前操作人:
            <span id="admName">${admName}</span>

            &nbsp;&nbsp;数据源:
            <select id="query_source">
                <option value="1" selected="selected">1-按照类别树</option>
                <option value="2">2-指定商品</option>
                <option value="3">3-指定商品</option>
                <option value="4">4-指定商品</option>
            </select>

            <div style="display: inline-block;">
                &nbsp;&nbsp;编辑状态:
                <select id="query_is_edited">
                    <option value="-1" selected="selected">全部</option>
                    <option value="0">未编辑</option>
                    <option value="1">已编辑</option>
                </select>

                &nbsp;&nbsp;货源对标情况:
                <select id="query_is_benchmark">
                    <option value="-1" selected="selected">全部</option>
                    <option value="0">没找到对标</option>
                    <option value="1">精确对标</option>
                    <option value="2">近似对标</option>
                </select>

                &nbsp;&nbsp;在线状态:
                <select id="query_valid">
                    <option value="-1" selected="selected">全部</option>
                    <option value="0">硬下架</option>
                    <option value="1">在线</option>
                    <option value="2">软下架</option>
                </select>
            </div>

            &nbsp;&nbsp;
            <input type="button" value="查询" onclick="doQueryWidthJump()">
        </div>

    </div>
    <div data-options="region:'west',split:true,border:false" style="width:300px;">
        <div class="easyui-panel">
            <ul class="easyui-tree">
            </ul>
        </div>
    </div>
    <div data-options="region:'center',border:false" style="">
        <iframe id="goods_list" src="/cbtconsole/cutom/cmslist?valid=-2" style="width: 98%;height: 98%;">
        </iframe>
    </div>
</c:if>
<c:if test="${uid ==0}">
    {"status":false,"message":"请重新登录进行操作"}
</c:if>
<script type="text/javascript">
    jQuery(function($){
        // 数据源切换
        $("#query_source").change(function(){
            if ($("#query_source").val() == '1') {
                $("#query_is_edited").parent().css('display', 'inline-block')
            } else if ($("#query_source").val() != '1') {
                $("#query_is_edited").parent().css('display', 'none');
                $(".easyui-tree").hide();
                $('#goods_list').attr('src','/cbtconsole/cutom/cmslist?valid=-2');
            }
        });
    });
</script>
</body>
</html>