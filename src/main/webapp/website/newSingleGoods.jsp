<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>手工录入商品</title>
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
            width: 80px;
            height: 30px;
            border: 1px #aaa solid;
            color: #fff;
        }

        #single_query_form {
            font-size: 18px;
        }

        #button_style {
            font-size: 18px;
        }

        .panel-title {
            text-align: center;
            height: 30px;
            font-size: 24px;
        }

        .but_color {
            padding: 5px;
            font-size: 14px
        }

        #single_top_toolbar {
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

        .err_sty {
            color: red;
        }

        .suc_sty {
            color: green;
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

        .style_btn_delete {
            width: 120px;
            height: 30px;
            background-color: red;
            border-color: #ffffff;
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
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            closeDialog();
            setDatagrid();
            getAdminList();
            var opts = $("#single_easyui-datagrid").datagrid("options");
		    opts.url = "/cbtconsole/singleGoods/queryForNewGoodsList";
        });

        function setDatagrid() {
            $('#single_easyui-datagrid').datagrid({
                title: '手工录入商品',
                width: "100%",
                fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                toolbar: "#single_top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
                url: '',//url调用Action方法
                loadMsg: '数据装载中......',
                singleSelect: true,//为true时只能选择单行
                fitColumns: true,//允许表格自动缩放，以适应父容器
                rownumbers: true,
                nowrap:false,
                pageSize: 40,//默认选择的分页是每页50行数据
                pageList: [40,60],//可以选择的分页集合
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

        function doQuery(flag) {
            var pid = $("#pid_id").val();
            if (pid == null || pid == "") {
                pid = "";
            }
            var shopId = $("#query_shop_id").val();
            if (shopId == null || shopId == "") {
                shopId = "";
            }
            var sttime = $("#query_sttime").val();
            if (sttime == null || sttime == "") {
                sttime = "";
            }
            var edtime = $("#query_edtime").val();
            if (edtime == null || edtime == "") {
                edtime = "";
            }
            var admid = $("#query_admid").val();
            if (admid == null || admid == "") {
                admid = 0;
            }
            var drainageFlag = $("#query_drainage_flag").val();
            var goodsType = $("#query_goods_type").val();
            var state = $("#query_state").val();
            if (state == null || state == "") {
                state = -1;
            }
            if (flag > 0) {
                $("#single_easyui-datagrid").datagrid("reload", {
                    "pid": pid,
                    "shopId": shopId,
                    "sttime": sttime,
                    "edtime": edtime,
                    "admid": admid,
                    "state": state,
                    "drainageFlag": drainageFlag,
                    "goodsType": goodsType
                });
            } else {
                $("#single_easyui-datagrid").datagrid("load", {
                    "pid": pid,
                    "shopId": shopId,
                    "sttime": sttime,
                    "edtime": edtime,
                    "admid": admid,
                    "state": state,
                    "drainageFlag": drainageFlag,
                    "goodsType": goodsType
                });
            }

        }

        function formatImg(val, row, index) {
            var content = "";
            if (val == null || val == "") {
                content = "<img class='img_sty' scr='/cbtconsole/img/yuanfeihang/loaderTwo.gif' alt='无图' />";
            } else {
                content = "<img class='img_sty' src='" + val + "' alt='无图' />";
            }
            return content;
        }

        function formatUploadResult(val, row, index) {

            if (val == 1) {
                return '<span class="suc_sty">发布成功</span>';
            } else if (val > 1) {
                return '<span class="err_sty">流程第4步,同步失败：<br>' + row.syncRemark
                    + '</span>';
            } else if (row.crawlFlag == 0) {
                return '<span class="err_sty">数据还未进行处理，请等待</span>';
            } else if (row.crawlFlag == 1 || row.crawlFlag == 11 || row.crawlFlag == 9) {
                return '<span class="err_sty">流程第1步,正在抓取</span>';
            } else if (row.crawlFlag == 4) {
                return '<span class="err_sty">流程第1步,抓取失败</span>';
            } else if (row.crawlFlag == 2) {
                if (row.clearFlag == 0) {
                    return '<span class="err_sty">流程第2步,数据待清洗</span>';
                } else if (row.clearFlag == 1) {
                    return '<span class="err_sty">流程第2步,数据正在清洗</span>';
                } else if (row.valid == 0) {
                    return '<span class="err_sty">流程第2步,数据清洗完成，商品数据无效</span>';
                } else if (row.clearFlag == 3) {
                    return '<span class="err_sty">流程第3步,下载图片到本地失败</span>';
                } else if (val == 0) {
                    return '<span class="err_sty">流程第4步,待数据同步</span>';
                } else {
                    return '<span class="err_sty">' + val + '</span>';
                }
            } else {
                return '<span class="err_sty">流程第1步,抓取失败</span>';
            }
        }
        
        function formatGoodsType(val, row, index) {
            if(val == 2){
                return 'Amazon对标';
            }else if(val == 1){
                return 'AliExpress对标';
            }else if(val == 0){
                return '未对标';
            }else{
                return '';
            }
        }
        
        function formatDrainageFlag(val, row, index) {
            if(val == 2){
                return '非引流';
            }else if(val == 1){
                return '引流';
            }else{
                return '';
            }
        }
        

        function formatOperation(val, row, index) {
            var content = '';
            if (row.syncFlag == 0 || row.syncFlag == 2) {
                content += '<br><input type="button" class="style_btn_delete" value="编辑商品" onclick="editGoodsByPid(\'' + row.goodsPid + '\')">';
                content += '<br><input type="button" style="display: none;" value="重新上传" onclick="openSyncWindow(\''+row.goodsPid + '\')"/>';
            } else if (row.syncFlag == 1) {
                content += '<br>'
                    + '<a href="/cbtconsole/editc/detalisEdit?pid='
                    + row.goodsPid
                    + '" target="_blank">编辑详情</a><br><br><a href="https://www.import-express.com/goodsinfo/ep-0-0-1'
                    + row.goodsPid + '.html" target="_blank">线上预览</a><br>';
            }
            return content;
        }

        function formatShopNum(val, row, index) {
            return '<span>'+val + '(<b style="color: red;">'+ row.shopGoodsNum +'</b>)</span>';
        }

        function closeDialog() {
            $('#enter_div_sty').dialog('close');
            $("#form_enter")[0].reset();
        }

        function enterGoods() {
            $("#form_enter")[0].reset();
            $('#enter_div_sty').dialog('open');
        }

        function  beforeSave() {
            var goodsUrl = $("#goods_url").val();
            var goodsWeight = $("#goods_weight").val();
            var goodsType = $("#goods_type").val();
            var aliPid = $("#ali_pid").val();
            var aliPrice = $("#ali_price").val();
            var shopId = $("#shop_id").val();
            if (goodsUrl == "" || goodsUrl == null) {
                $.messager.alert("提醒", '请输入1688URL', "info");
                return false;
            }
            if (shopId == "" || shopId == null) {
                $.messager.alert("提醒", '请输入店铺ID', "info");
                return false;
            }
            if (goodsWeight == "" || goodsWeight == null || goodsWeight == "0") {
                $.messager.alert("提醒", '请输入商品重量', "info");
                return false;
            }
            if(goodsType >0){
                if (aliPid == "" || aliPid == null || aliPid == "0") {
                    $.messager.alert("提醒", '请输入对标商品ID', "info");
                    return false;
                }
                if (aliPrice == "" || aliPrice == null || aliPrice == "0") {
                    $.messager.alert("提醒", '请输入对标商品价格', "info");
                    return false;
                }
            }
            var drainageFlag = $("#drainage_flag").val();

            $.ajax({
                type: "POST",
                url: "/cbtconsole/singleGoods/checkExitsGoods",
                data: {
                    goodsUrl: goodsUrl
                },
                success: function (data) {
                    $.messager.progress('close');
                    if (data.ok) {
                        if(data.total > 0){
                            $.messager.confirm('系统提醒', data.message + '，执行保存操作，线上数据将被更新，是否执行？', function (r) {
                                if (r) {
                                    doAddAction(goodsUrl,goodsWeight,drainageFlag,goodsType,aliPid,aliPrice, shopId);
                                }});
                        }else{
                            doAddAction(goodsUrl,goodsWeight,drainageFlag,goodsType,aliPid,aliPrice, shopId);
                        }
                    } else {
                        $.messager.alert("提醒", '执行错误:' + data.message, "info");
                    }
                },
                error: function (res) {
                    $.messager.progress('close');
                    $.messager.alert("提醒", '保存错误，请联系管理员', "error");
                }
            });



        }

        function doAddAction(goodsUrl,goodsWeight,drainageFlag,goodsType,aliPid,aliPrice, shopId) {
            $.messager.progress({
                title: '正在执行',
                msg: '请等待...'
            });
            $.ajax({
                type: "POST",
                url: "/cbtconsole/singleGoods/saveGoods",
                data: {
                    goodsUrl: goodsUrl,
                    goodsWeight: goodsWeight,
                    drainageFlag: drainageFlag,
                    goodsType: goodsType,
                    aliPid: aliPid,
                    aliPrice: aliPrice,
                    shopId: shopId
                },
                success: function (data) {
                    $.messager.progress('close');
                    if (data.ok) {
                        closeDialog();
                        showMessage('保存成功，请等待数据的处理！');
                        setTimeout(function () {
                            //window.location.reload();
                            doQuery(1);
                        }, 1000);
                    } else {
                        $.messager.alert("提醒", '执行错误:' + data.message, "info");
                    }
                },
                error: function (res) {
                    $.messager.progress('close');
                    $.messager.alert("提醒", '保存错误，请联系管理员', "error");
                }
            });
        }

        function editGoodsByPid(pid) {
            window.open("/cbtconsole/newProduct/getNewProductDetail?pid="+pid, "_blank");
        }

        function openSyncWindow(pid) {
            var url = "http://192.168.1.102:8080/syncGoodsToOnline/sync/singleGoodsSync?pid=" + pid;
            var param = "height=400,width=600,top=200,left=600,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
            window.open(url, "windows", param);
        }

        function addNewProduct(){
            window.open("/cbtconsole/newProduct/addNewProduct","_blank");
        }
    </script>

</head>
<body>

<c:if test="${uid == 0}">
    <h1 align="center">请登录后操作</h1>
</c:if>
<c:if test="${uid > 0}"></c:if>


<div id="enter_div_sty" class="easyui-dialog" title="新增1688商品"
     data-options="modal:true" style="width: 888px; height: 330px;">
    <form id="form_enter" action="#" onsubmit="return false">
        <table>
            <tr>
                <td>1688URl：</td>
                <td><input id="goods_url" value="" style="width: 555px; height: 28px;" placeholder="请输入1688商品url"/></td>
            </tr>
            <tr>
                <td>店铺ID：</td>
                <td><input id="shop_id" value="" style="width: 555px; height: 28px;" placeholder="请输入店铺ID"/></td>
            </tr>
            <tr>
                <td>平均重量：</td>
                <td><input
                        id="goods_weight" value="" style="width: 555px; height: 28px;"
                        placeholder="请输入商品的重量"/></td>
            </tr>
            <tr>
                <td>对标类型：</td>
                <td><select id="goods_type">
                    <option value="0">未对标</option>
                    <option value="1">AliExpress对标</option>
                    <option value="2">Amazon对标</option>
                </select></td>
            </tr>
            <tr>
                <td>对标商品ID:</td>
                <td><input type="text" id="ali_pid" style="width: 200px; height: 24px" value=""/></td>
            </tr>
            <tr>
                <td>对标商品价格:</td>
                <td><input type="text" id="ali_price" style="width: 200px; height: 24px" value=""/></td>
            </tr>
            <tr>
                <td>引流标识：</td>
                <td><select id="drainage_flag">

                    <option value="1">引流商品</option>
                    <option value="2" selected="selected">非引流商品</option>
                </select></td>
            </tr>
        </table>
        <div style="text-align: center;">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton" onclick=" beforeSave()" style="width: 80px">生成</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeDialog()"
               style="width: 80px">取消</a>
        </div>

    </form>
</div>


<div id="single_top_toolbar" style="padding: 5px; height: auto">
    <form id="single_query_form" action="#" onsubmit="return false;">
			<span> PID: <input type="text" id="pid_id"
                               style="width: 170px; height: 24px" value=""/></span>&nbsp;&nbsp;


        &nbsp;&nbsp;<span><input type="button"
                                 class="enter_btn" value="查询" onclick="doQuery(0)"/></span>
        &nbsp;<span><input type="button"
                           class="enter_btn" value="新增" onclick="addNewProduct()"/></span>

        <%--<span><a href="/cbtconsole/website/singleGoodsCheck.jsp" target="_blank">跨境商品审核</a></span>--%>
    </form>
</div>

<table id="single_easyui-datagrid" style="width: 100%; height: 100%;"
       class="easyui-datagrid">
    <thead>
    <tr>
        <th data-options="field:'goodsPid',width:'150px'">PID</th>
        <th
                data-options="field:'goodsImg',width:'220px',formatter:formatImg">图片
        </th>
        <th data-options="field:'goodsName',width:'440px'">产品名称</th>
        <th data-options="field:'aveWeight',align:'center',width:'140px'">商品重量(KG)</th>

        <th
                data-options="field:'syncFlag',width:'200px',formatter:formatUploadResult">产品上传结果
        </th>
        <th data-options="field:'adminName',align:'center',width:'100px'">录入人</th>
        <th data-options="field:'createTime',align:'center',width:'180px'">创建时间</th>
        <th
                data-options="field:'opFlag',align:'center',width:'240px',formatter:formatOperation">操作
        </th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>


</body>
</html>