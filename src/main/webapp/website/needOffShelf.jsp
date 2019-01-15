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
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            setDatagrid();
        });

        function setDatagrid() {
            $('#neef_off_easyui-datagrid').datagrid({
                title: '商品下架审核',
                width: "100%",
                fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                toolbar: "#neef_off_top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
                url: '/cbtconsole/singleGoods/queryOffShelfList',//url调用Action方法
                loadMsg: '数据装载中......',
                singleSelect: true,//为true时只能选择单行
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
            $("#neef_off_easyui-datagrid").datagrid("load", {
                "pid": pid,
                "catid":catid,
                "beginTime": beginTime,
                "endTime": endTime,
                "reason": reason,
                "isOffShelf": isOffShelf,
                "updateFlag": updateFlag,
                "neverFlag": neverFlag,
                "soldFlag":soldFlag
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
                return '上架';
            } else if (val == 2) {
                return '软下架';
            } else {
                return '';
            }
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
        
        function formatReason(val, row, index) {
            //商品下架原因
            if (val == 1) {
                return '1-1688货源下架';
            } else if (val == 2) {
                return '2-不满足库存条件';
            } else if (val == 3) {
                return '3-销量无变化(低库存)';
            } else if (val == 4) {
                return '4-页面404';
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
                return '20-一手数据下架';
            } else if (val == 21) {
                return '21-大于400美元商品下架';
            } else if (val == 22) {
                return '22-原因老数据没有展示详情图片';
            } else {
                return '';
            }
        }

        function formatOperation(val, row, index) {
            var content = '<a target="_blank" href="/cbtconsole/editc/detalisEdit?pid='+ row.pid +'">编辑商品</a>';
            return content;
        }

    </script>

</head>
<body>

<c:if test="${uid == 0}">
    <h1 align="center">请登录后操作</h1>
</c:if>
<c:if test="${uid > 0}"></c:if>


<div id="neef_off_top_toolbar" style="padding: 5px; height: auto">
    <form id="neef_off_query_form" action="#" onsubmit="return false;">
        <span> PID: <input type="text" id="pid_id" style="width: 180px; height: 24px" value=""/></span>
        <span> 类别: <input type="text" id="catid_id" style="width: 120px; height: 24px" value=""/></span>
        <span>更新时间: <input id="query_beginTime" class="Wdate" style="width: 110px; height: 24px" type="text" value=""
                         onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
		    <span>&nbsp;-&nbsp;</span>
            <input id="query_endTime" class="Wdate" style="width: 110px; height: 24px;" type="text" value=""
                   onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
        </span>
        <span> 状态: <select id="query_isOffShelf" style="font-size: 14px; height: 24px; width: 120px;">
                        <option value="-1" selected="selected">全部</option>
                        <option value="0">硬下架</option>
                        <option value="1">上架</option>
                        <option value="2">软下架</option>
                    </select>
        </span>
        <span> 下架原因: <select id="query_reason" style="font-size: 14px; height: 24px; width: 160px;">
                                <option value="0" selected="selected">全部</option>
                                <option value="1">1-1688货源下架</option>
                                <option value="2">2-不满足库存条件</option>
                                <option value="3">3-销量无变化(低库存)</option>
                                <option value="4">4-页面404</option>
                                <option value="5">5-重复验证合格</option>
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
                                <option value="20">20-一手数据下架</option>
                                <option value="21">21-大于400美元商品下架</option>
                                <option value="22">22-原因老数据没有展示详情图片</option>
                        </select>
        </span>
        <span> 更新标识: <select id="query_updateFlag" style="font-size: 14px; height: 24px; width: 100px;">
                                <option value="-1" selected="selected">全部</option>
                                <option value="0">未更新</option>
                                <option value="1">更新失败</option>
                                <option value="2">更新成功</option>
                                <option value="3">重新验证过</option>
                        </select>
        </span>
        <span> 永不下架标识: <select id="query_never_off" style="font-size: 14px; height: 24px; width: 90px;">
                                <option value="0" selected="selected">全部</option>
                                <option value="1">永不下架</option>
                        </select>
            <span> 有销量等筛选(<a href="#" id="refreshData">刷新临时数据</a>): <select id="query_sold_flag" style="font-size: 14px; height: 24px; width: 120px;">
                                <option value="0" selected="selected">不进行筛选</option>
                                <option value="1">有销量</option>
                                <option value="2">购物车中商品</option>
                                <option value="3">有跨境图片包</option>
                        </select>
        </span>
        <span><input type="button" class="enter_btn" value="查询" onclick="doQuery()"/></span>
    </form>
</div>

<table id="neef_off_easyui-datagrid" style="width: 100%; height: 100%;"
       class="easyui-datagrid">
    <thead>
    <tr>
        <th data-options="field:'pid',align:'center',width:'110px'">PID</th>
        <th data-options="field:'imgUrl',align:'center',width:'180px',formatter:formatImg">商品图片</th>
        <th data-options="field:'catidName',align:'center',width:'150px'">所属类别</th>
        <th data-options="field:'isOffShelf',align:'center',width:'150px',formatter:formatIsOffShelf">上下架标识</th>
        <th data-options="field:'updateFlag',align:'center',width:'150px',formatter:formatUpdateFlag">更新标识</th>
        <th data-options="field:'competitiveFlag',align:'center',width:'150px',formatter:formatCompetitiveFlag">精品标识</th>
        <th data-options="field:'neverOffFlag',align:'center',width:'150px',formatter:formatneverOffFlag">永不下架</th>
        <th data-options="field:'reason',align:'center',width:'150px',formatter:formatReason">下架原因</th>
        <th data-options="field:'updateTime',align:'center',width:'200px'">更新时间</th>
        <th data-options="field:'opFlag',align:'center',width:'200px',formatter:formatOperation">操作</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>
<script type="text/javascript">
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
</script>

</body>
</html>