<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>对标商品统计</title>
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
            height: 30px;
            font-size: 24px;
        }


        #statistic_query_form {
            font-size: 18px;
        }

        .panel-title {
            text-align: center;
            height: 30px;
            font-size: 24px;
        }


        #statistic_top_toolbar {
            padding: 20px 28px !important;
        }

        .datagrid-header .datagrid-cell span, .datagrid-cell,
        .datagrid-cell-group, .datagrid-header-rownumber,
        .datagrid-cell-rownumber {
            font-size: 14px;
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


    </style>
    <%
     String admName=request.getParameter("admName");
      String days=request.getParameter("days");
    %>
    <script type="text/javascript">
        $(document).ready(function () {
            setDatagrid();
            var opts = $("#statistic_easyui-datagrid").datagrid("options");
            opts.url = "/cbtconsole/aliBeanchmarking/queryStatistic";
            var admName='<%=admName%>';
            var days='<%=days%>';
            if(admName != null && admName != ""){
                $("#admName").val(admName);
                doChooseData(days);
            }else{
                doChooseData(7);
            }

        });

        function setDatagrid() {
            $('#statistic_easyui-datagrid').datagrid({
                title: '对标商品统计&nbsp;&nbsp;<a target="_blank" href="/cbtconsole/website/aliBeanchmarking.jsp" style="font-size: 13px;text-decoration: underline;">所有对标商品</a>',
                width: "100%",
                fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                toolbar: "#statistic_top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
                url: '',//url调用Action方法
                loadMsg: '数据装载中...',
                singleSelect: true,//为true时只能选择单行
                fitColumns: true,//允许表格自动缩放，以适应父容器
                rownumbers: true,
                nowrap: false,
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
            var admName=$("#admName").val();
            var beginDate = $("#beginDate").val();
            var endDate = $("#endDate").val();
            $("#statistic_easyui-datagrid").datagrid("load", {
                "beginDate": beginDate,
                "endDate": endDate,
                "admName":admName
            });
        }


        function doChooseData(num) {
            $("#beginDate").val(getFormatDate(num, 0));
            $("#endDate").val(getFormatDate(0, 1));
            doQuery();
        }


        function getFormatDate(subDayNum, isBegin) {
            var today = new Date();
            if (subDayNum > 0) {
                today.setDate(today.getDate() - subDayNum - 1);
            } else {
                today.setDate(today.getDate() - 1);
            }
            var month = today.getMonth() + 1;
            if (month >= 1 && month <= 9) {
                month = "0" + month;
            }
            var strDate = today.getDate();
            if (strDate >= 0 && strDate <= 9) {
                strDate = "0" + strDate;
            }
            var content = "";
            if (isBegin > 0) {
                //content = today.getFullYear() + "-" + month + "-" + strDate + " 23:59:59";
                content = today.getFullYear() + "-" + month + "-" + strDate;
            } else {
                //content = today.getFullYear() + "-" + month + "-" + strDate + " 00:00:00";
                content = today.getFullYear() + "-" + month + "-" + strDate;
            }
            return content;
        }

        function formatActionDetails(val, row, index) {
            return '<a href="/cbtconsole/website/aliBeanchmarkingDetails.jsp?adminId=' + val
                + '&adminName='+row.adminName+'" target="_blank">查看详情</a>';
        }
    </script>

</head>
<body>

<c:if test="${uid == 0}">
    <h1 align="center">请登录后操作</h1>
</c:if>
<c:if test="${uid > 0}"></c:if>


<div id="statistic_top_toolbar" style="padding: 5px; height: auto">
    <form id="statistic_query_form" action="#" onsubmit="return false;">
        <input type="hidden" id="admName" name="admName">
			<span>日期起:<input id="beginDate"
                             style="width: 130px; height: 24px" name="beginDate"
                             readonly="readonly"
                             onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd'})"/></span>
        <span>至:<input
                id="endDate" style="width: 130px; height: 24px" name="endDate"
                readonly="readonly" onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd'})"/></span>
        <input type="button"
               class="enter_btn" value="查询" onclick="doQuery()"/>
        <input type="button" class="enter_btn"
                                    onclick="doChooseData(30)" value="最近30天"> <input
            type="button" class="enter_btn" onclick="doChooseData(90)"
            value="最近90天">
    </form>
</div>

<table id="statistic_easyui-datagrid" style="width: 100%; height: 100%;"
       class="easyui-datagrid">
    <thead>
    <tr>
        <th data-options="field:'noBenchmarksCount',width:80,align:'center'">非对标上线产品数量</th>
        <th data-options="field:'benchmarkingTotalNum',align:'center',width:'120px'">完成对标的数量</th>
        <th data-options="field:'onlineRate',align:'center',width:'200px'">上线的百分比(%)</th>
        <th data-options="field:'isEditedRate',align:'center',width:'120px'">人为编辑过的百分比(%)</th>
        <th data-options="field:'freightFreeNum',align:'center',width:'200px'">免邮商品的数量</th>
        <th data-options="field:'moqNum',align:'center',width:'200px'">MOQ大于1的数量</th>
        <th data-options="field:'adminName',align:'center',width:'100px'">录入人</th>
        <th data-options="field:'adminId',align:'center',width:'100px',formatter:formatActionDetails">对标详情</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>


</body>
</html>