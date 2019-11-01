<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>客户商业授权</title>
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

        .but_color {
            background: #44a823;
            margin-top: 10px;
            width: 80px;
            height: 28px;
            border: 1px #aaa solid;
            color: #fff;
        }

        #single_query_form {
            font-size: 18px;
        }

        .panel-title {
            text-align: center;
            height: 30px;
            font-size: 24px;
        }

        #user_top_toolbar {
            padding: 10px 10px !important;
        }

        .datagrid-header .datagrid-cell span, .datagrid-cell,
        .datagrid-cell-group, .datagrid-header-rownumber,
        .datagrid-cell-rownumber {
            font-size: 14px;
        }


        .enter_btn {
            width: 100px;
            height: 28px;
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

        .div_sty_1 {
            background-color: #d2f5e0;
        }

        .div_sty_2 {
            background-color: #f3dcc0;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            closeDialog();
            closeSend();
            setDatagrid();
            // getAdminList();
            getAllZone();
            doQuery(0);
        });

        function setDatagrid() {
            $('#user_easyui-datagrid').datagrid({
                title: '客户商业授权',
                width: "100%",
                fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                toolbar: "#user_top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
                url: '/cbtconsole/userinfo/queryMemAuthList',//url调用Action方法
                loadMsg: '数据装载中......',
                singleSelect: true,//为true时只能选择单行
                fitColumns: true,//允许表格自动缩放，以适应父容器
                rownumbers: true,
                nowrap: false,
                pageSize: 40,//默认选择的分页是每页50行数据
                pageList: [40, 60],//可以选择的分页集合
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

        function getAdminList() {
            $.ajax({
                type: "POST",
                url: "/cbtconsole/userinfo/getAdminList",
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

        function getAllZone() {
            $.ajax({
                type: "GET",
                url: "/cbtconsole/userinfo/getZoneList",
                dataType: "json",
                success: function (msg) {
                    var content = '<option value="0">全部</option>';
                    $(msg).each(function (index, item) {
                        content += '<option value="' + item.id + '">' + item.country + '</option>';
                    });
                    $("#query_country").empty();
                    $("#query_country").append(content);
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

        function doQuery(flag) {
            var userId = $("#query_user_id").val();
            var email = $("#query_email").val();
            var countryId = $("#query_country").val();
            var authFlag = $("#query_auth_flag").val();
            var param = {
                "userId": userId,
                "email": email,
                "countryId": countryId,
                "authFlag": authFlag,
                "site": 1
            };
            if (flag > 0) {
                $("#user_easyui-datagrid").datagrid("load", param);
            } else {
                $("#user_easyui-datagrid").datagrid("reload", param);
            }
        }


        function formatUserInfo(val, row, index) {
            var content = '<span>客户ID:' + val + '</span>';
            content += '<br><span>邮箱:' + row.email + '</span>';
            content += '<br><span>国家:' + row.zone + '</span>';
            content += '<br><span>注册时间:' + row.creattime + '</span>';
            if(row.site == 0 || row.site == 1){
                content += '<br><span>网站:Import</span>';
            }else if(row.site == 2 || row.site == 3){
                content += '<br><span>网站:KIDS</span>';
            }else if(row.site == 4){
                content += '<br><span>网站:PETS</span>';
            }
            content += '<br><span>销售:' + row.admuser + '</span>';
            return content;
        }

        function formatProduct(val, row, index) {
            var content = '<div class="div_sty_1">' + val + '</div>';
            if (row.producttwo) {
                content += '<div class="div_sty_2">' + row.producttwo + '</div>';
            }
            return content;
        }

        function formatRequire(val, row, index) {
            var content = '<div class="div_sty_1">' + val + '</div>';
            if (row.requirementtwo) {
                content += '<div class="div_sty_2">' + row.requirementtwo + '</div>';
            }
            return content;
        }

        function formatAuthFlag(val, row, index) {
            var content = '<span>授权标识:';
            if (val == 1) {
                content += '授权';
            } else {
                content += '未授权';
            }
            content += '</span>';
            return content;
        }

        function formatAmount(val, row, index) {
            var content = '<span>历史成交:' + val + '</span>';
            content += '<br><br><span>购物车:' + row.shopCarAmount + '</span>';
            return content;
        }


        function formatOperation(val, row, index) {
            var content = '<button class="but_color" onclick="openCheckout(' + row.userid + ',' + row.authFlag + ')">授权</button>';
            content += '<br><button onclick="openRecommendEmail(' + row.userid + ',' + row.site + ')" class="but_color">推荐目录</button>';
            return content;
        }


        function closeDialog() {
            $('#auth_dlg').dialog('close');
            $("#auth_query_form")[0].reset();
        }

        function closeSend() {
            $("#send_recommend_id").dialog('close');
        }

        function resetForm() {
            $("#single_query_form")[0].reset();
        }

        function openCheckout(userId, type) {
            $('#check_out_user_id').val(userId);
            $("#check_out_type").val(type);
            $('#auth_dlg').dialog('open');
        }

        function updateUserCheckout() {
            var userid = $('#check_out_user_id').val();
            var type = $("#check_out_type").val();
            var isSu = true;
            if (!userid || userid == 0) {
                isSu = false;
            }
            if (isSu) {
                $.ajax({
                    type: "POST",
                    url: "/cbtconsole/queryuser/updateUserCheckout.do",
                    data: {
                        userid: userid,
                        type: type
                    },
                    dataType: "json",
                    success: function (msg) {
                        if (msg.state == 'true') {
                            closeDialog();
                            doQuery(1);
                        }
                    }
                });
            }
        }

        function openRecommendEmail(userId, site) {

            $.ajax({
                type: 'post',
                url: '../userinfo/getUserAllInfoById',
                data: {
                    userId: userId
                },
                success: function (data) {
                    if (data != null) {
                        if (data.ok) {
                            $("#send_web_site").val(site);
                            $("#send_user_id").val(userId);
                            var json = data.data;
                            $("#send_user_email").val(json.email);
                            $("#user_create_time").val(json.creattime);
                            $("#user_buniess_info").val(json.businessinfo);
                            if (json.productone && json.productone.length > 2) {
                                $("#user_goods_need").val(json.productone);
                                $("#user_goods_require").val(json.requirementone);
                            } else if (json.productone && json.producttwo.length > 2) {
                                $("#user_goods_need").val(json.producttwo);
                                $("#user_goods_require").val(json.requirementtwo);
                            }
                            $("#sell_email").val(json.admuser);
                            var jsonList = data.allData;
                            if (jsonList && jsonList.length > 0) {
                                var content = '';
                                for (var i = 0; i < jsonList.length; i++) {
                                    content += '<tr>';
                                    content += '<td>时间:' + jsonList[i].createTime + '</td>';
                                    content += '<td>推送人:' + jsonList[i].adminName + '</td>';
                                    content += '<td><a href="' + jsonList[i].sendUrl + '">链接</a></td>';
                                    content += '</tr>';
                                }
                                $("#history_id").empty();
                                $("#history_id").append(content);
                                $("#history_table").show();
                            }
                            $("#send_recommend_id").dialog('open');
                        } else {
                            alert("获取信息失败");
                        }
                    } else {
                        alert("网络错误");
                    }
                }
            });
        }

        function sendRecommendEmail() {
            var userId = $("#send_user_id").val();
            var userEmail = $("#send_user_email").val();
            var createTime = $("#user_create_time").val();
            var buniessInfo = $("#user_buniess_info").val();
            var goodsNeed = $("#user_goods_need").val();
            var goodsRequire = $("#user_goods_require").val();
            var sendUrl = $("#send_url").val();
            var sellEmail = $("#sell_email").val();
            var webSite = $("#send_web_site").val();
            if (userId && userEmail && createTime && sendUrl && webSite) {
                $("#notice_id").show();
                $.ajax({
                    type: 'post',
                    url: '../userinfo/sendRecommendEmail',
                    data: {
                        userId: userId,
                        userEmail: userEmail,
                        createTime: createTime,
                        buniessInfo: buniessInfo,
                        goodsNeed: goodsNeed,
                        sendUrl: sendUrl,
                        sellEmail: sellEmail,
                        goodsRequire: goodsRequire,
                        webSite: webSite
                    },
                    success: function (data) {
                        $("#notice_id").hide();
                        if (data.ok) {
                            closeSend();
                        } else {
                            alert("执行报错");
                        }
                    }
                });
            } else {
                alert("请填写必要的信息");
                return;
            }

        }

    </script>
</head>
<body>

<div id="auth_dlg" class="easyui-dialog" title="授权"
     data-options="modal:true" style="width: 280px; height: 190px; padding: 10px;">
    <form id="auth_query_form">
        <table>
            <tr>
                <td>客户ID:</td>
                <td><input id="check_out_user_id" value="0" style="width: 80px;height: 26px;" disabled="disabled"/></td>
            </tr>
            <tr>
                <td>授权:</td>
                <td><select id="check_out_type" style="width: 80px;height: 26px;">
                    <option value="0">取消授权</option>
                    <option value="1">进行授权</option>
                </select></td>
            </tr>
        </table>
    </form>

    <div style="text-align: center; padding: 5px 0">
        <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
           class="easyui-linkbutton"
           onclick="updateUserCheckout()" style="width: 80px">保存</a>
        <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
           class="easyui-linkbutton" onclick="closeDialog()"
           style="width: 80px">关闭</a>
    </div>
</div>

<div id="send_recommend_id" class="easyui-dialog" title="推荐目录推送"
     data-options="modal:true"
     style="width:600px;height:420px;display: none;font-size: 16px;">
    <table align="center">
        <tr>
            <td>用户邮箱:</td>
            <td>
                <input id="send_user_id" value="0" style="display: none"/>
                <input id="send_web_site" value="-1" style="display: none"/>
                <input id="send_user_email" style="width: 330px;"/></td>
        </tr>
        <tr>
            <td>注册日期:</td>
            <td><input id="user_create_time" style="width: 330px;"/></td>
        </tr>
        <tr>
            <td>商务信息:</td>
            <td><input id="user_buniess_info" style="width: 330px;"/></td>
        </tr>
        <tr>
            <td>产品需求:</td>
            <td><input id="user_goods_need" style="width: 330px;"/>
                <br>
                <input id="user_goods_require" style="width: 330px;"/></td>
        </tr>
        <tr>
            <td>目录地址:</td>
            <td><input id="send_url" style="width: 330px;"/>
                <button class="enter_btn">生成目录</button>
            </td>
        </tr>
        <tr>
            <td>推送邮箱:</td>
            <td><input id="sell_email" style="width: 330px;"/></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: center">
                <button class="enter_btn" onclick="sendRecommendEmail()">推送</button>
                &nbsp;&nbsp;<button class="enter_btn" onclick="closeSend()">关闭</button>
                <span id="notice_id" style="display: none;color: red;">执行中，请等待...</span>
            </td>
        </tr>
    </table>
    <table align="center" id="history_table" style="display: none;" border="1" cellpadding="0">
        <caption>推送历史记录</caption>
        <tbody id="history_id">

        </tbody>
    </table>
</div>

<div id="user_top_toolbar" style="height: auto">
    <form id="single_query_form" action="#" onsubmit="return false;">

        <span>客户ID:<input id="query_user_id" style="height: 26px;width: 100px;"/></span>
        <span>邮箱:<input id="query_email" style="height: 26px;width: 200px;"/></span>
        <span>国家:<select id="query_country" style="height: 26px;width: 140px;"></select></span>
        <span>授权:<select id="query_auth_flag" style="height: 26px;width: 80px;">
            <option value="-1">全部</option>
            <option value="0">未授权</option>
            <option value="1">授权</option>
        </select></span>
        &nbsp;&nbsp;
        <span><input type="button" class="enter_btn" value="查询" onclick="doQuery(0)"/></span>
        <span><input type="button" class="enter_btn" value="重置" onclick="resetForm(0)"/></span>
    </form>
</div>

<table id="user_easyui-datagrid" style="width: 100%; height: 100%;"
       class="easyui-datagrid">
    <thead>
    <tr>
        <th data-options="field:'userid',width:'200px',formatter:formatUserInfo">客户信息</th>
        <%--<th data-options="field:'email',width:'140px'">邮箱</th>
        <th data-options="field:'zone',width:'100px'">国家</th>
        <th data-options="field:'creattime',width:'170px'">注册时间</th>--%>
        <th data-options="field:'applicationTime',width:'160px'">申请时间</th>
        <th data-options="field:'businessinfo',width:'240px'">businessinfo</th>

        <th data-options="field:'productone',width:'240px',formatter:formatProduct">product</th>
        <%--<th data-options="field:'producttwo',width:'150px'">producttwo</th>--%>
        <th data-options="field:'requirementone',width:'240px',formatter:formatRequire">requirement</th>
        <%--<th data-options="field:'requirementtwo',width:'150px'">requirementtwo</th>--%>
        <th data-options="field:'authFlag',align:'center',width:'100px',formatter:formatAuthFlag">标识</th>
        <%--<th data-options="field:'authFlag',align:'center',width:'100px',formatter:formatAuthFlag">推荐标识</th>--%>
        <th data-options="field:'historyAmount',width:'100px',formatter:formatAmount">金额</th>
        <%--<th data-options="field:'shopCarAmount',width:'100px'">购物车金额</th>--%>
        <th
                data-options="field:'opFlag',align:'center',width:'80px',formatter:formatOperation">操作
        </th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>
</body>
</html>
