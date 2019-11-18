<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>对标商品信息</title>
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


        #marking_query_form {
            font-size: 18px;
        }

        .panel-title {
            text-align: center;
            height: 30px;
            font-size: 24px;
        }


        #marking_top_toolbar {
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
    <script type="text/javascript">
        $(document).ready(function () {
            setDatagrid();
            getAdminList();
        });

        function setDatagrid() {
            $('#beanchmarking_easyui-datagrid').datagrid({
                title: '对标商品信息',
                width: "100%",
                fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                toolbar: "#marking_top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
                url: '/cbtconsole/aliBeanchmarking/queryForList',//url调用Action方法
                loadMsg: '数据装载中...',
                singleSelect: true,//为true时只能选择单行
                fitColumns: true,//允许表格自动缩放，以适应父容器
                rownumbers: true,
                nowrap: false,
                pageSize: 30,//默认选择的分页是每页50行数据
                pageList: [30, 50],//可以选择的分页集合
                pagination: true,//分页
                style: {
                    padding: '8 8 10 8'
                },
                onLoadError: function () {
                    $.messager.alert("提示信息", "获取数据信息失败");
                    return;
                },
                onLoadSuccess: function (data) {
                    if (!data.success) {
                        showMessage(data.message);
                    }
                }
            });
        }

        function getAdminList() {
            $.ajax({
                type: "POST",
                url: "/cbtconsole/singleGoods/getAdminList",
                data: {},
                success: function (data) {
                    if (data.ok) {
                        $("#query_admid").empty();
                        var content = '<option value="0" selected="selected">全部</option>';
                        var json = data.data;
                        for (var i = 0; i < json.length; i++) {
                            content += '<option value="' + json[i].id + '" ">' + json[i].confirmusername + '</option>';
                        }
                        $("#query_admid").append(content);
                    } else {
                        console.log("获取用户列表失败，原因 :" + data.message);
                    }
                },
                error: function (res) {
                    console.log("网络获取失败");
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
            var aliPid = $("#ali_pid").val();
            if (aliPid == null || aliPid == "") {
                aliPid = "";
            }
            var beginTime = $("#query_beginTime").val();
            if (beginTime == null || beginTime == "") {
                beginTime = "";
            }
            var endTime = $("#query_endTime").val();
            if (endTime == null || endTime == "") {
                endTime = "";
            }
            var admId = $("#query_admid").val();
            if (admId == null || admId == "") {
                admId = 0;
            }
            var isEdited = $("#query_is_edited").val();
            if (isEdited == null || isEdited == "") {
                isEdited = -1;
            }
            var isOnline = $("#query_is_online").val();
            if (isOnline == null || isOnline == "") {
                isOnline = -1;
            }
            var category = $("input[name=category]").val();
            if (category == null || category == "") {
            	category = 0;
            }
            $("#beanchmarking_easyui-datagrid").datagrid("load", {
                "pid": pid,
                "aliPid": aliPid,
                "beginTime": beginTime,
                "endTime": endTime,
                "isEdited": isEdited,
                "isOnline": isOnline,
                "admId": admId,
                "category":category
            });
        }

        function format1688Url(val, row, index) {
        	if (row.image != undefined) {
        		return "<a target='_blank' href='" + val + "' ><img style='width:100%;' src='" + row.image.replace("http:","https:") + "'/></a>";
			}
            return "<a target='_blank' href='" + val + "' >1688Url</a>";
        }

        function formatAliUrl(val, row, index) {
        	if (row.aliImage != undefined) {
        		return "<a target='_blank' href='" + val + "' ><img style='width:100%;' src='" + row.aliImage.replace("http:","https:") + "'/></a>";
			}
            return "<a target='_blank' href='" + val + "' >AliUrl</a>";
        }

        function formatIsOnline(val, row, index) {
            if (val > 0) {
                return "是";
            } else {
                return "否";
            }
        }

        function formatIsEdited(val, row, index) {
            if (val > 0) {
                return "已编辑";
            } else {
                return "未编辑";
            }
        }
        
        function formatAction(val, row, index) {
            if(row.isOnline > 0){
                return '<a href="/cbtconsole/editc/detalisEdit?pid='+ row.pid + '" target="_blank">编辑详情</a>';
            }
        }

    </script>

</head>
<body>

<c:if test="${uid == 0}">
    <h1 align="center">请登录后操作</h1>
</c:if>
<c:if test="${uid > 0}"></c:if>


<div id="marking_top_toolbar" style="padding: 5px; height: auto">
    <form id="marking_query_form" action="#" onsubmit="return false;">
			<span> PID: <input type="text" id="pid_id"
                               style="width: 200px; height: 24px" value=""/></span>&nbsp;&nbsp;&nbsp;&nbsp;
        <span> AliPid: <input type="text" id="ali_pid"
                              style="width: 200px; height: 24px" value=""/></span>&nbsp;&nbsp;&nbsp;&nbsp;<span>
				时间: <input id="query_beginTime" class="Wdate"
                           style="width: 110px; height: 24px" type="text" value=""
                           onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
				<span>&nbsp;-&nbsp;</span><input id="query_endTime" class="Wdate"
                                                 style="width: 110px; height: 24px;" type="text" value=""
                                                 onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
			</span>&nbsp;&nbsp;&nbsp;&nbsp; <span> 录入人: <select id="query_admid"
                                                                style="font-size: 16px; height: 24px; width: 120px;">
					<option value="0" selected="selected">全部</option>
			</select></span>&nbsp;&nbsp;&nbsp;&nbsp;
        <span> 是否编辑: <select id="query_is_edited"
                             style="font-size: 16px; height: 24px; width: 120px;">
                <option value="-1" selected="selected">全部</option>
                <option value="0">未编辑</option>
                <option value="1">已编辑</option>
        </select></span>
        &nbsp;&nbsp;&nbsp;&nbsp;<span> 是否上线: <select id="query_is_online"
                                                     style="font-size: 16px; height: 24px; width: 120px;">
                <option value="-1" selected="selected">全部</option>
                <option value="0">否</option>
                <option value="1">是</option>
        </select></span>
        &nbsp;&nbsp;&nbsp;&nbsp;<span> 类别: 
        <select name="category" class="category1688" style="width: 360px"></select>
        </span>
        &nbsp;&nbsp;&nbsp;&nbsp; <span><input type="button"
                                              class="enter_btn" value="查询" onclick="doQuery()"/></span>
    </form>
</div>

<table id="beanchmarking_easyui-datagrid" style="width: 100%; height: 100%;"
       class="easyui-datagrid">
    <thead>
    <tr>
        <th data-options="field:'pid',align:'center',width:'120px'">1688PID</th>
        <th data-options="field:'url',align:'center',width:'200px',formatter:format1688Url">点击链接到1688</th>
        <th data-options="field:'aliPid',align:'center',width:'120px'">aliPid</th>
        <th data-options="field:'aliUrl',align:'center',width:'200px',formatter:formatAliUrl">点击链接到速卖通</th>
        <th data-options="field:'createTime',align:'center',width:'200px'">创建时间</th>
        <th data-options="field:'adminName',align:'center',width:'100px'">录入人</th>
        <th data-options="field:'isOnline',align:'center',width:'120px',formatter:formatIsOnline">是否上线</th>
        <th data-options="field:'isEdited',align:'center',width:'120px',formatter:formatIsEdited">是否编辑</th>
        <th data-options="field:'action',align:'center',width:'200px',formatter:formatAction">操作</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>
<script type="text/javascript">
	//类别加载
	$.ajax({
		url : '../categoryResearch/search1688Category',
		method : 'post',
		dataType : 'json',
		success : function(data) {
			$(".category1688").combotree({
				data : data
			});
		},
	
	});
</script>

</body>
</html>