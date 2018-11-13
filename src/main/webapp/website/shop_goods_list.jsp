<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.cbt.util.Redis" %>
<%@page import="com.cbt.util.AppConfig" %>
<%@ page import="com.cbt.website.userAuth.bean.Admuser" %>
<%@ page import="com.cbt.util.SerializeUtil" %>
<!DOCTYPE html>
<html>
<head>
    <style type="text/css">
    </style>
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/jquery-form.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
    <title>同店铺上线产品</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <link rel="stylesheet" href="script/style.css" type="text/css">
    <link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
    <script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">

    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        var searchReport = "/cbtconsole/StatisticalReport/searchCoupusManagement"; //报表查询
    </script>
    <style type="text/css">
        .displaynone {
            display: none;
        }

        .item_box {
            display: inline-block;
            margin-right: 52px;
        }

        .item_box select {
            width: 150px;
        }

        .mod_pay3 {
            width: 600px;
            position: fixed;
            top: 100px;
            left: 15%;
            z-index: 1011;
            background: gray;
            padding: 5px;
            padding-bottom: 20px;
            z-index: 1011;
            border: 15px solid #33CCFF;
        }

        .w-group {
            margin-bottom: 10px;
            width: 60%;
            text-align: center;
        }

        .w-label {
            float: left;
        }

        .w-div {
            margin-left: 120px;
        }

        .w-remark {
            width: 100%;
        }

        table.imagetable {
            font-family: verdana, arial, sans-serif;
            font-size: 11px;
            color: #333333;
            border-width: 1px;
            border-color: #999999;
            border-collapse: collapse;
        }

        table.imagetable th {
            background: #b5cfd2 url('cell-blue.jpg');
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #999999;
        }

        .panel-title {
            text-align: center;
            height: 30px;
            font-size: 24px;
        }

        table.imagetable td {
            /* 	background:#dcddc0 url('cell-grey.jpg'); */
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #999999;
            word-break: break-all;
        }

        .displaynone {
            display: none;
        }

        .but_color {
            background: #44a823;
            width: 80px;
            height: 24px;
            border: 1px #aaa solid;
            color: #fff;
        }

    </style>
    <%
        //取出当前登录用户
        String sessionId = request.getSession().getId();
        String userJson = Redis.hget(sessionId, "admuser");
        Admuser adm = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
        int shopId = adm.getId();
        String shop_id = request.getParameter("shop_id");
    %>

</head>
<body onload="doQuery(1)">

<div id="top_toolbar" style="padding: 5px; height: auto">
    <form id="query_form" action="#" style="margin-left:350px;" onsubmit="return false;">
        <input class="but_color" type="button" value="批量下架" onclick="unshelveGoods()">
    </form>
</div>

<table class="easyui-datagrid" id="easyui-datagrid" style="width:1200px;height:900px">
    <thead>
    <tr>
        <th data-options="field:'ck',checkbox:true,align:'center'"></th>
        <th data-options="field:'pid',width:30,align:'center'">商品ID</th>
        <th data-options="field:'image',width:50,align:'center'">商品图片</th>
        <th data-options="field:'name',width:50,align:'center'">商品名称</th>
        <th data-options="field:'urlInfo',width:50,align:'center'">产品链接汇总</th>
        <!--<th data-options="field:'updatetime',width:50,align:'center'">update_time</th>  -->
        <!-- <th data-options="field:'onLineNum',width:50,align:'center'">已在线商品</th>
        <th data-options="field:'admUser',width:50,align:'center'">编辑者</th>
        <th data-options="field:'stateInfo',width:30,align:'center'">state</th> -->
    </tr>
    </thead>
</table>
</body>
<script type="text/javascript">
    $(function () {
// 	document.onkeydown = function(e){
// 		var ev = document.all ? window.event : e; 
// 		if(ev.keyCode==13) {
// 			 var number=$('#easyui-datagrid').datagrid('options').pageNumber;
// 			 doQuery(number);
// 		}
// 	} 


        setDatagrid();
        $('#data').datebox({
            closeText: '关闭',
            formatter: function (date) {
                var y = date.getFullYear();
                var m = date.getMonth() + 1;
                var d = date.getDate();
                var h = date.getHours();
                var M = date.getMinutes();
                var s = date.getSeconds();

                function formatNumber(value) {
                    return (value < 10 ? '0' : '') + value;
                }

                return y + '-' + m + '-' + d;
            },
            parser: function (s) {
                var t = Date.parse(s);
                if (!isNaN(t)) {
                    return new Date(t);
                } else {
                    return new Date();
                }
            }
        });
        $('#dlg').dialog('close');
    });

    function setDatagrid() {
        $('#easyui-datagrid').datagrid({
            title: '同店铺上线产品',
            //iconCls : 'icon-ok',
            width: "100%",
            fit: true,//自动补全
            pageSize: 30,//默认选择的分页是每页20行数据
            pageList: [30],//可以选择的分页集合
            nowrap: false,//设置为true，当数据长度超出列宽时将会自动截取
            striped: true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
            toolbar: "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
            url: '/cbtconsole/ShopUrlC/findAllGoods?shopId=<%=shop_id%>',//url调用Action方法
            loadMsg: '数据装载中......',
            singleSelect: false,//为true时只能选择单行
            fitColumns: true,//允许表格自动缩放，以适应父容器
            idField: 'itemid',
            //sortName : 'xh',//当数据表格初始化时以哪一列来排序
// 			sortOrder : 'desc',//定义排序顺序，可以是'asc'或者'desc'（正序或者倒序）。
            pagination: true,//分页
            rownumbers: true
            //行数
        });
    }

    function doQuery(page) {
        var shopId = $("#shopId").val();
        $("#easyui-datagrid").datagrid("load", {
            "page": page,
            "shopId": shopId
        });
    }
    
    function unshelveGoods() {
        var shopId = "<%=shop_id%>";
        var pids = [];
        var rows = $('#easyui-datagrid').datagrid('getSelections');
        for(var i=0; i<rows.length; i++){
            pids.push(rows[i].pid);
        }
        if(pids.length > 0){
            //alert("shopId:" + shopId + "@" + pids.toString());
            $.ajax({
                url: '/cbtconsole/cutom/offshelf',
                type: "post",
                data: {
                    "shopId":shopId,
                    "pid":pids.toString()
                },
                success: function (data) {
                    if ("" == data || !data) {
                        $.messager.alert('提示', "执行错误，请重试");
                    } else {
                        if("1" == data){
                            $("#easyui-datagrid").datagrid("reload");
                        }else{
                            $.messager.alert('提示', "执行失败，请重试");
                        }
                    }
                }
            });
        }else{
            $.messager.alert('提示', "请选择需要删除的商品");
        }

    }

</script>

</html>