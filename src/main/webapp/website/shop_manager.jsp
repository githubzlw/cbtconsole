<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
    <title>数据来源店铺管理</title>
    <style type="text/css">
        .but_color {
            background: #44a823;
            width: 80px;
            height: 24px;
            border: 1px #aaa solid;
            color: #fff;
        }

        .panel-title {
            text-align: center;
            height: 30px;
            font-size: 24px;
        }

        tr .td_class {
            width: 230px;
        }

        .td_class lable {
            float: left;
            width: 120px;
        }

        .w_input input {
            width: 200px;
        }

        .mod_pay3 {
            width: 400px;
            height: 400px;
            position: fixed;
            top: 100px;
            left: 15%;
            margin-left: 400px;
            z-index: 1011;
            padding: 5px;
            padding-bottom: 20px;
            z-index: 1011;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            setDatagrid();
        });

        function setDatagrid() {
            $('#easyui-datagrid').datagrid({
                title: '数据来源店铺管理',
                width: "100%",
                height: "100%",
                fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                pageSize: 20,//默认选择的分页是每页20行数据
                pageList: [20, 30],//可以选择的分页集合
                nowrap: true,//设置为true，当数据长度超出列宽时将会自动截取
                striped: true,//设置为true将交替显示行背景。
                toolbar: "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
                url: '/cbtconsole/warehouse/getShopManager',//url调用Action方法
                loadMsg: '数据装载中......',
                singleSelect: true,//为true时只能选择单行
                fitColumns: true,//允许表格自动缩放，以适应父容器
                //sortName : 'xh',//当数据表格初始化时以哪一列来排序
                //sortOrder : 'desc',//定义排序顺序，可以是'asc'或者'desc'（正序或者倒序）。
                pagination: true,//分页
                rownumbers: true
                //行数
            });
        }


        function doQuery() {
            var shop_name = $("#shop_name").val();
            var remark = $('#remark').combobox('getValue');
            $("#easyui-datagrid").datagrid("load", {
                "shop_name": shop_name,
                "remark": remark
            });
        }

        function doReloadQuery() {
            var options = $("#easyui-datagrid").datagrid('getPager').data("pagination").options;
            var shop_name = $("#shop_name").val();
            var remark = $('#remark').combobox('getValue');
            $("#easyui-datagrid").datagrid("load", {
                "shop_name": shop_name,
                "remark": remark,
                "page": options.pageNumber
            });
        }

        function doReset() {
            $('#shop_name').textbox('setValue', '');
            $('#remark').combobox('setValue', '-1');
        }

        function BigImg(img) {
            htm_ = "<img width='600px' height='600px' src=" + img + ">";
            $("#big_img").append(htm_);
            $("#big_img").css("display", "block");
        }

        function closeBigImg() {
            $("#big_img").css("display", "none");
            $('#big_img').empty();
        }

        function updateState(id, state) {
            if (state == "-1") {
                return;
            }
            $.ajax({
                url: "/cbtconsole/warehouse/updateShopState",
                data: {
                    "id": id,
                    "state": state,
                    "type": "0"
                },
                type: "post",
                async: false,
                success: function (data) {
                    if (data == "1") {
                        $.messager.alert('提示', '更新店铺状态成功');
                        doReloadQuery();
                    } else {
                        $.messager.alert('提示', '更新店铺状态成功');
                    }
                }
            });
        }

    </script>
</head>
<body>
<div class="mod_pay3" style="display: none;" id="big_img">

</div>
<div id="top_toolbar" style="padding: 5px; height: auto">
    <div>
        <form id="query_form" action="#" onsubmit="return false;" style="margin-left:450px;">
            <input class="easyui-textbox" name="shop_name" id="shop_name" style="width:15%;"
                   data-options="label:'店铺名称:'">
            <select class="easyui-combobox" name="remark" id="remark" style="width:15%;"
                    data-options="label:'店铺状态:',panelHeight:'auto'">
                <option value="-1" selected>全部</option>
                <option value="0">自动禁用</option>
                <option value="1">自动全免</option>
                <option value="2">人工解禁</option>
                <option value="3">人工全免</option>
                <option value="3">系统无法判断</option>
            </select>
            <input class="but_color" type="button" value="查询" onclick="doQuery()">
            <input class="but_color" type="button" value="重置" onclick="doReset()">
        </form>
    </div>
</div>

<table class="easyui-datagrid" id="easyui-datagrid"
       style="width: 1800px; height: 900px">
    <thead>
    <tr>
        <th data-options="field:'shop_id',width:40,align:'center'">店铺ID</th>
        <th data-options="field:'shop_name',width:70,align:'center'">店铺名称</th>
        <th data-options="field:'shop_url',width:70,align:'center'">店铺链接</th>
        <th data-options="field:'admuser',width:30,align:'center'">操作人</th>
        <th data-options="field:'remark',width:30,align:'center'">店铺状态</th>
        <th data-options="field:'flag',width:25,align:'center'">是否下载完成</th>
        <th data-options="field:'system_evaluation',width:30,align:'center'">系统是否判</th>
        <th data-options="field:'imgs',width:100,align:'center'">产品图片</th>
        <!-- 				<th data-options="field:'createtime',width:50,align:'center'">店铺录入时间</th> -->
        <!-- 				<th data-options="field:'updatetime',width:60,align:'center'">最后修改时间</th> -->
        <th data-options="field:'operation',width:30,align:'center'">操作</th>
    </tr>
    </thead>
</table>
</body>
</html>