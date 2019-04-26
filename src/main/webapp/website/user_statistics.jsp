<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
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
    <title>注册和下单分析</title>
    <style type="text/css">
        .panel-title {
            text-align: center;
            height: 30px;
            font-size: 24px;
        }

        .datagrid-header .datagrid-cell span, .datagrid-cell,
        .datagrid-cell-group, .datagrid-header-rownumber,
        .datagrid-cell-rownumber {
            font-size: 14px;
        }

        #statistics_top_toolbar {
            padding: 20px 28px !important;
        }

        .but_color_qy {
            margin-left: 15px;
            background: #234ca8;
            width: 80px;
            height: 28px;
            border: 1px #aaa solid;
            color: #fff;
        }

        .but_color {
            margin-left: 15px;
            background: #44a823;
            width: 80px;
            height: 28px;
            border: 1px #aaa solid;
            color: #fff;
        }

        .query_div {
            margin-top: 5px;
            margin-left: 5%;
            width: 42%;
        }

        .data_div {
            float: right;width: 49%;height: 90%;
        }

        .every_day_div {
            float: right;width: 49%;height: 90%;
        }

        .recent_view_div {
            float: right;width: 49%;height: 90%;
        }

        .payLog_div {
            float: right;width: 49%;height: 90%;
        }
        .addtoorder_div {
            float: right;width: 49%;height: 90%;
        }

        #single_query_form {
            margin-left: 40px;
            text-align: left;
        }

        .tr_stl {
            background-color: #62ff00;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            setDatagrid();
            var opts = $("#statistics_easyui-datagrid").datagrid("options");
            opts.url = "/cbtconsole/behaviorStatistics/getStatisticsDatails";
            var opts1 = $("#statistics_easyui-datagrid1").datagrid("options");
            opts1.url = "/cbtconsole/behaviorStatistics/getStatisticsDatails";
            var payLog = $("#statistics_easyui-datagrid3").datagrid("options");
            payLog.url = "/cbtconsole/behaviorStatistics/getStatisticsDatails";
            var optsDay = $("#statistics_every_day").datagrid("options");
            optsDay.url = "/cbtconsole/behaviorStatistics/getStatisticsByEveryDay";
            doChooseData(7);
        });

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

        function setDatagrid() {
            $('#statistics_easyui-datagrid').datagrid({
                width: "100%",
                //fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                url: '',//url调用Action方法
                loadMsg: '数据装载中......',
                singleSelect: true,//为true时只能选择单行
                fitColumns: true,//允许表格自动缩放，以适应父容器
                rowNumbers: true,
                pageSize: 25,//默认选择的分页是每页50行数据
                pageList: [25, 50],//可以选择的分页集合
                pagination: true,//分页
                style: {
                    padding: '8 8 10 8'
                },
                onLoadError: function () {
                    $.messager.progress('close');
                    $.message.alert("提示信息", "获取数据信息失败");
                    return;
                },
                onLoadSuccess: function (data) {
                    if (data.success) {
                        $("#statistics_easyui-datagrid").datagrid("resize");
                    } else {
                        $.message.alert("提示信息", data.message);
                    }
                }
            });
            $('#statistics_every_day').datagrid({
                width: "100%",
                //fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                url: '',//url调用Action方法
                loadMsg: '数据装载中......',
                singleSelect: true,//为true时只能选择单行
                fitColumns: true,//允许表格自动缩放，以适应父容器
                rowNumbers: true,
                style: {
                    padding: '8 8 10 8'
                },
                onLoadError: function () {
                    $.messager.progress('close');
                    $.message.alert("提示信息", "获取数据信息失败");
                    return;
                },
                onLoadSuccess: function (data) {
                    if (data.success) {
                        $("#statistics_every_day").datagrid("resize");
                    } else {
                        $.message.alert("提示信息", data.message);
                    }
                }
            });
        }

        function doQuery() {
            $(".data_div").hide();
            $(".every_day_div").hide();
            $(".recent_view_div").hide();
            $(".payLog_div").hide();
            var beginDate = $("#beginDate").val();
            if (beginDate == null) {
                beginDate = "";
            }
            var endDate = $("#endDate").val();
            if (endDate == null) {
                endDate = "";
            }
            if (beginDate == null || beginDate == "") {
                $.messager.alert("提醒", "请选择开始时间时间", "info");
                return;
            } else {
                loadStatisticsData(beginDate, endDate);
            }
        }

        function doChooseData(num) {
            $("#beginDate").val(getFormatDate(num, 0));
            $("#endDate").val(getFormatDate(0, 1));
            doQuery();
        }

        function loadStatisticsData(beginDate, endDate) {
            $.messager.progress({
                title: '正在查询',
                msg: '请等待...'
            });
            $
                .ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/behaviorStatistics/getStatistics',
                    data: {
                        beginDate: beginDate,
                        endDate: endDate
                    },
                    success: function (data) {
                        $.messager.progress('close');
                        if (data.ok) {
                            var json = data.data;
                            var content = "";
                            for (var i = 0; i < json.length; i++) {
                                content += "<tr><td>" + json[i].typeDesc
                                    + "</td>";
                                if (json[i].isShow == 0) {
                                    content += "<td><span>"
                                        + json[i].statisticsNum
                                        + "</span></td>";
                                } else {
                                    content += "<td><a href='javascript:void(0);' onclick='queryDetails("
                                        + json[i].statisticsNum
                                        + ",\""
                                        + json[i].typeFlag
                                        + "\",this)'>"
                                        + json[i].statisticsNum
                                        + "</a></td>";
                                }
                                if(json[i].typeFlag == 10 || json[i].typeFlag == 11){
                                    content += "<td></td>";
                                }else{
                                    content += "<td><a href='javascript:void(0);' onclick='showByEveryDay("
                                        + json[i].statisticsNum
                                        + ",\""
                                        + json[i].typeFlag
                                        + "\",this)'>按日期统计</a></td>";
                                }

                                if (json[i].isExport == 0) {
                                    content += "<td></td>";
                                } else {
                                    content += "<td><a href='javascript:void(0);' onclick='exportDetails("
                                        + json[i].statisticsNum
                                        + ",\""
                                        + json[i].typeFlag
                                        + "\",this)'>导出</a></td>";
                                }

                                content += "</tr>";
                            }
                            $("#statisticsTable tbody").empty();
                            $("#statisticsTable tbody").append(content);
                        } else {
                            $.messager.alert("提醒", data.message, "error");
                        }
                    },
                    error: function (XMLResponse) {
                        $.messager.progress('close');
                        $.messager.alert("提醒", "查询错误，请重试", "error");
                    }
                });
        }

        function queryDetails(total, typeFlag, obj) {
            if (total > 0) {
                var beginDate = $("#beginDate").val();
                if (beginDate == null) {
                    beginDate = "";
                }
                var endDate = $("#endDate").val();
                if (endDate == null) {
                    endDate = "";
                }
                if (beginDate == null || beginDate == "" || endDate == null
                    || endDate == "") {
                    $.messager.alert("提醒", "请选择开始时间或者结束时间", "info");
                    return;
                } else if (typeFlag == 10) {
                    addTrBk(obj);
                    $(".recent_view_div").show();
                    $(".data_div").hide();
                    $(".payLog_div").hide();
                    $(".every_day_div").hide();
                    $("#statistics_easyui-datagrid1").datagrid("load", {
                        "beginDate": beginDate,
                        "endDate": endDate,
                        "total": total,
                        "typeFlag": typeFlag
                    });
                }else if (typeFlag == 11) {
                    addTrBk(obj);
                    $(".payLog_div").show();
                    $(".data_div").hide();
                    $(".every_day_div").hide();
                    $(".recent_view_div").hide();
                    $("#statistics_easyui-datagrid3").datagrid("load", {
                        "beginDate": beginDate,
                        "endDate": endDate,
                        "total": total,
                        "typeFlag": typeFlag
                    });
                }else if (typeFlag == 12) {
                    addTrBk(obj);
                    $(".addtoorder_div").show();
                    $(".data_div").hide();
                    $(".every_day_div").hide();
                    $(".recent_view_div").hide();
                    $(".payLog_div").hide();
                    $("#statistics_easyui-datagrid4").datagrid("load", {
                        "beginDate": beginDate,
                        "endDate": endDate,
                        "total": total,
                        "typeFlag": typeFlag
                    });
                }
                else {
                    addTrBk(obj);
                    $(".data_div").show();
                    $(".every_day_div").hide();
                    $(".recent_view_div").hide();
                    $(".payLog_div").hide();
                    $("#statistics_easyui-datagrid").datagrid("load", {
                        "beginDate": beginDate,
                        "endDate": endDate,
                        "total": total,
                        "typeFlag": typeFlag
                    });
                }
            }
        }

        function exportDetails(total, typeFlag, obj) {
            if (total > 0) {
                var beginDate = $("#beginDate").val();
                if (beginDate == null) {
                    beginDate = "";
                }
                var endDate = $("#endDate").val();
                if (endDate == null) {
                    endDate = "";
                }
                if (beginDate == null || beginDate == "") {
                    $.messager.alert("提醒", "请选择开始时间时间", "info");
                    return;
                } else {
                    addTrBk(obj);
                    window.location.href = "/cbtconsole/behaviorStatistics/exportStatisticsExcel?beginDate="
                        + beginDate + "&endDate=" + endDate + "&typeFlag=" + typeFlag;
                }
            } else {
                $.messager.alert("提醒", "当前无数据，无法导出，请重新选择查询", "info");
            }
        }

        function showByEveryDay(total, typeFlag, obj) {
            if (total > 0) {
                var beginDate = $("#beginDate").val();
                if (beginDate == null) {
                    beginDate = "";
                }
                var endDate = $("#endDate").val();
                if (endDate == null) {
                    endDate = "";
                }
                if (beginDate == null || beginDate == "" || endDate == null
                    || endDate == "") {
                    $.messager.alert("提醒", "请选择开始时间或者结束时间", "info");
                    return;
                } else if (typeFlag == 10) {
                    addTrBk(obj);
                    $(".recent_view_div").show();
                    $(".data_div").hide();
                    $(".payLog_div").hide();
                    $(".every_day_div").hide();
                    $("#statistics_every_day").datagrid("load", {
                        "beginDate": beginDate,
                        "endDate": endDate,
                        "total": total,
                        "typeFlag": typeFlag
                    });
                } else {
                    addTrBk(obj);
                    $(".data_div").hide();
                    $(".recent_view_div").hide();
                    $(".every_day_div").show();
                    $("#statistics_every_day").datagrid("load", {
                        "beginDate": beginDate,
                        "endDate": endDate,
                        "typeFlag": typeFlag
                    });
                }
            }
        }

        function addTrBk(obj) {
            $(obj).parent().parent().siblings().removeClass("tr_stl");
            $(obj).parent().parent().addClass("tr_stl");
        }
        
        function exportStatisticsAllExcel() {
            var beginDate = $("#beginDate").val();
            if (beginDate == null) {
                beginDate = "";
            }
            var endDate = $("#endDate").val();
            if (endDate == null) {
                endDate = "";
            }
            if (beginDate == null || beginDate == "" || endDate == null
                || endDate == "") {
                $.messager.alert("提醒", "请选择开始时间或者结束时间", "info");
                return;
            } else {
                window.location.href = "/cbtconsole/behaviorStatistics/exportStatisticsAllExcel?beginDate="
                    + beginDate + "&endDate=" + endDate;
            }
        }
    </script>
</head>
<body>

<div style="float: left;width: 49%;height: 800px;">
    <h2 align="center">注册和下单分析</h2>
    <div style="padding: 5px; height: auto">

        <form id="single_query_form" action="#" onsubmit="return false;">

			<span>日期起:<input id="beginDate"
                             style="width: 130px; height: 24px" name="beginDate"
                             readonly="readonly"
                             onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd'})"/></span>
            <span>至:<input
                    id="endDate" style="width: 130px; height: 24px" name="endDate"
                    readonly="readonly" onfocus="WdatePicker({isShowWeek:true,dateFmt:'yyyy-MM-dd'})"/></span>
            <input
                    type="button" class="but_color_qy" onclick="doQuery()" value="查询">
            <input type="button" class="but_color" onclick="doChooseData(7)"
                   value="最近7天"> <input type="button" class="but_color"
                                        onclick="doChooseData(30)" value="最近30天"> <input
                type="button" class="but_color" onclick="doChooseData(90)"
                value="最近90天">
        </form>


    </div>

    <div class="query_div">
        <table id="statisticsTable" border="1"
               style="margin-top: 1px; width: 750px; text-align: center">
            <thead>
            <tr align="center" bgcolor="#DAF3F5" style="height: 35px;">
                <td width=70%>类别</td>
                <td width=10%>客户数</td>
                <td width=20% colspan="2">操作</td>
            </tr>
            </thead>
            <tbody></tbody>
        </table>

        <br>
        <input type="button" class="but_color" value="全部导出" onclick="exportStatisticsAllExcel()"/>

    </div>

</div>


<div class="data_div">
    <h2>类别明细</h2>
    <table id="statistics_easyui-datagrid"
           style="width: 99%; height:750px;" class="easyui-datagrid">
        <thead>
        <tr>
            <th data-options="field:'userId',width:'100px'">用户ID</th>
            <th data-options="field:'email',width:'120px'">邮箱</th>
            <th data-options="field:'createTime',width:'150px'">时间</th>
            <th data-options="field:'carNum',width:'80px'">购物车数量</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>


<div class="every_day_div">
    <h2>类别按照日期统计</h2>
    <table id="statistics_every_day" style="width: 99%; height: 750px;" class="easyui-datagrid">
        <thead>
        <tr>
            <th data-options="field:'recordDate',align:'center',width:'120px'">日期</th>
            <th data-options="field:'statisticsNum',align:'center',width:'100px'">客户数</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>


<div class="recent_view_div">
    <h2>产品单页浏览量最多的50个商品</h2>
    <table id="statistics_easyui-datagrid1"
           style="width: 99%; height: 750px;" class="easyui-datagrid">
        <thead>
        <tr>
            <th data-options="field:'email',width:'200px'">产品pid</th>
            <th data-options="field:'userId',width:'100px'">浏览次数</th>
            <th data-options="field:'createTime',width:'180px'">时间</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>

<div class="payLog_div">
    <h2>Pay按钮点击独特人数</h2>
    <table id="statistics_easyui-datagrid3" style="width: 99%; height: 750px;" class="easyui-datagrid">
        <thead>
        <tr>
            <th data-options="field:'createTime',align:'center',width:'180px'">日期</th>
            <th data-options="field:'email',align:'center',width:'300px'">邮箱</th>
            <th data-options="field:'pid',align:'center',width:'180px'">订单号</th>
            <th data-options="field:'orderAmount',align:'center',width:'100px'">金额($)</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>
<div class="addtoorder_div">
    <h2>Add to order 按钮点击次数</h2>
    <table id="statistics_easyui-datagrid4" style="width: 99%; height: 750px;" class="easyui-datagrid">
        <thead>
        <tr>
            <th data-options="field:'createTime',align:'center',width:'180px'">日期</th>
            <th data-options="field:'email',align:'center',width:'300px'">邮箱</th>
            <th data-options="field:'pid',align:'center',width:'180px'">订单号</th>
            <th data-options="field:'orderAmount',align:'center',width:'100px'">金额($)</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>






</body>
</html>