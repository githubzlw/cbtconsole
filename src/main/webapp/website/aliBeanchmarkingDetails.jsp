<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>对标关键词详情</title>
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
            height: 24px;
            font-size: 20px;
        }

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

        .div_left {
            float: left;
            width: 30%;
            height: 880px;
        }

        .div_right {
            float: right;
            width: 69%;
            height: 880px;
            /*display: none;*/
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

        .img_sty {
            max-width: 180px;
            max-height: 180px;
        }

        .inp_moq_sty{
            width: 63px;
        }

        .inp_price_sty{
            width: 63px;
        }
    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            hideGoodsDlg();
            setKeyWordDatagrid();
            setDetailsDatagrid();
            var keyWordOpts = $("#keyword_easyui-datagrid").datagrid("options");
            keyWordOpts.url = "/cbtconsole/aliBeanchmarking/queryKeyWordListByAdminId";
            var detailsOpts = $("#details_easyui-datagrid").datagrid("options");
            detailsOpts.url = "/cbtconsole/aliBeanchmarking/queryKeyWordDetails";
            var adminId = "${param.adminId}";
            doQueryKey(adminId);
        });

        function setKeyWordDatagrid() {
            $('#keyword_easyui-datagrid').datagrid({
                title: '关键词信息',
                width: "100%",
                fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                url: '',//url调用Action方法
                loadMsg: '数据装载中...',
                singleSelect: true,//为true时只能选择单行
                fitColumns: true,//允许表格自动缩放，以适应父容器
                rownumbers: true,
                nowrap: false,
                pageSize: 25,//默认选择的分页是每页50行数据
                pageList: [25],//可以选择的分页集合
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


        function setDetailsDatagrid() {
            $('#details_easyui-datagrid').datagrid({
                title: '关键词商品信息(点击图片查看原始信息)',
                width: "100%",
                fit: true,//自动补全
                striped: true,//设置为true将交替显示行背景。
                collapsible: true,//显示可折叠按钮
                url: '',//url调用Action方法
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
                        $.messager.alert("提示信息", data.message);
                    }
                }
            });
        }


        function doQueryKey(adminId) {
            $("#keyword_easyui-datagrid").datagrid("load", {
                "adminId": adminId
            });
        }


        function formatActionDetails(val, row, index) {
            return '<button class="enter_btn" onclick="doQueryDetails(' + val + ',\'' + row.keyword + '\')">关键词商品</button>';
        }


        function doQueryDetails(adminId, keyword) {
            //$(".div_right").show();
            $("#details_easyui-datagrid").datagrid("load", {
                "adminId": adminId,
                "keyword": keyword
            });
        }

        function format1688Info(val, row, index) {
            var content = '<a target="_blank" href="' + row.url + '">'
                + '<img class="img_sty" src="' + row.image + '" alt="无图"/>' + '</a>';
            return content;
        }

        function formatAliInfo(val, row, index) {
            var content = '<a target="_blank" href="' + row.aliUrl + '">'
                + '<img class="img_sty" src="' + row.aliImage + '" alt="无图"/>' + '</a>';
            return content;
        }

        function formatIsOnline(val, row, index) {
            if (val > 0) {
                return "是";
            } else {
                return "否";
            }
        }

        function formatOption(val, row, index) {
            var content = '<a target="_blank" href="https://www.import-express.com/goodsinfo/cbtconsole-1'
                + row.pid + '.html">查看线上</a>';
            content += '<br><br><input type="button" class="enter_btn" value="商品标记" onclick="beforeSetGoodsFlag(\''
                    + row.pid + '\',\''+  row.isSoldFlag  +'\',\'' + row.wprice +'\',\''+ row.feePrice +'\','+ row.isBenchmark +')"/>';
            return content;
        }

        function hideGoodsDlg() {
            $('#set_goods_dlg').dialog('close');
            $('#fee_div').empty();
            $('#not_fee_div').empty();
            $('#edit_pid').val("");
            $('#is_sold_flag').val("");
            $('#free_id').val("0");
            $('#benchmarking_id').val("0");
            $('#fee_tr').hide();
            $('#not_fee_tr').hide();

        }


        function beforeSetGoodsFlag(pid,isSoldFlag,wprice,feePrice,isBenchmark) {

            $('#edit_pid').val(pid);
            $('#is_sold_flag').val(isSoldFlag);
            if(isBenchmark > 0){
                $('#benchmarking_id').val(1);
            }else{
                $('#benchmarking_id').val(2);
            }
            $('#fee_div').empty();
            $('#not_fee_div').empty();
            var content = '<br><div><b>MOQ</b>@<b>价格</b></div>';
            var priceList = feePrice.split(",");
            for (var i = 0; i < priceList.length; i++) {
                var spKey = priceList[i].split("@");
                content += '<span><input class="inp_moq_sty" value="' + spKey[0]
                    + '" />@<input class="inp_price_sty" value="' + spKey[1] + '" /></span><br>';

            }
            $('#fee_div').append(content);

            priceList = wprice.split(",");
            content = '<br><div><b>MOQ</b>@<b>价格</b></div>';
            for (var j = 0; j < priceList.length; j++) {
                var spKey = priceList[j].split("@");
                content += '<span><input class="inp_moq_sty" value="' + spKey[0]
                    + '" />@<input class="inp_price_sty" value="' + spKey[1] + '" /></span><br>';
            }
            $('#not_fee_div').append(content);
            if(isSoldFlag > 0){
                $('#fee_tr').show();
                $('#not_fee_tr').hide();
                $('#free_id').val(1);
            }else{
                $('#fee_tr').hide();
                $('#not_fee_tr').show();
                $('#free_id').val(2);
            }
            $('#set_goods_dlg').dialog('open');
        }

        function changeFree() {
            var freeFlag =  $('#free_id').val();
            if(freeFlag == 1){
                $('#fee_tr').show();
                $('#not_fee_tr').hide();
            }else if(freeFlag == 2){
                $('#fee_tr').hide();
                $('#not_fee_tr').show();
            }else{
                $('#fee_tr').hide();
                $('#not_fee_tr').hide();
            }
        }
        
        function saveGoodsFlag() {
            var pid = $('#edit_pid').val();
            if(pid == null || pid == ""){
                $.messager.alert('提示', '获取PID失败','info');
            }
            var freeFlag = $('#free_id').val();
            //获取MOQ信息和最大最小价格信息
            var priceContent = "";
            var tempMorder = "";
            var tempPrice = "";
            if(freeFlag == 1){
                $('#fee_div').find('span').each(function () {
                   tempMorder = $(this).find('.inp_moq_sty').val();
                   tempPrice = $(this).find('.inp_price_sty').val();
                   priceContent += "," + tempMorder + "@" + tempPrice;
                });
            }else if(freeFlag == 2){
                $('#not_fee_div').find('span').each(function () {
                   tempMorder = $(this).find('.inp_moq_sty').val();
                   tempPrice = $(this).find('.inp_price_sty').val();
                   priceContent += "," + tempMorder + "@" + tempPrice;
                });
            }
            var benchmarkingFlag = $('#benchmarking_id').val();
            $.ajax({
                url: '/cbtconsole/aliBeanchmarking/saveGoodsFlag',
                type: "post",
                data: {
                    "pid" : pid,
                    "priceContent" : priceContent.substring(1),
                    "freeFlag" : freeFlag,
                    "benchmarkingFlag" : benchmarkingFlag
                },
                success: function (data) {
                    if (data.ok) {
                        hideGoodsDlg();
                        $("#details_easyui-datagrid").datagrid("reload");
                    } else {
                        $.messager.alert('提示', data.message,'error');
                    }
                }
            });
        }
    </script>

</head>
<body>

<h2 style="text-align: center;">${param.adminName}的对标关键词详情</h2>
<div class="div_left">
    <table id="keyword_easyui-datagrid" style="width: 100%; height: 98%;"
           class="easyui-datagrid">
        <thead>
        <tr>
            <th data-options="field:'keyword',align:'center',width:'180px'">关键词</th>
            <th data-options="field:'adminId',align:'center',width:'150px',formatter:formatActionDetails">操作</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>

<div class="div_right">

    <table id="details_easyui-datagrid" style="width: 100%; height: 98%;"
           class="easyui-datagrid">
        <thead>
        <tr>
            <th data-options="field:'keyword',align:'center',width:'200px'">关键词</th>
            <th data-options="field:'pid',align:'center',width:'200px',formatter:format1688Info">1688信息</th>
            <th data-options="field:'aliPid',align:'center',width:'200px',formatter:formatAliInfo">AliExpress信息</th>
            <th data-options="field:'createTime',align:'center',width:'150px'">创建时间</th>
            <th data-options="field:'isOnline',align:'center',width:'100px',formatter:formatIsOnline">是否上线</th>
            <th data-options="field:'opFlag',align:'center',width:'150px',formatter:formatOption">操作</th>
        </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>


<div id="set_goods_dlg" class="easyui-dialog" title="商品标记"
     data-options="modal:true" style="width: 240px; height: 270px;">

    <table>
        <tr>
            <td>PID:</td>
            <td>
                <input id="edit_pid" type="text" readonly="readonly" style="width: 141px;height: 21px;">
            </td>
        </tr>
        <tr>
            <td>是否对标:</td>
            <td>
                <select id="benchmarking_id" style="width: 145px;height: 24px;">
                    <option value="1">对标</option>
                    <option value="2">不对标</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>是否免邮:</td>
            <td>
                <select id="free_id" style="width: 145px;height: 24px;" onchange="changeFree()">
                    <%--<option value="0">请选择</option>--%>
                    <option value="1">免邮</option>
                    <option value="2">非免邮</option>
                </select>
            </td>
        </tr>
        <tr id="fee_tr" style="display: none;">
            <td>免邮:</td>
            <td><div id="fee_div"></div></td>
        </tr>
        <tr id="not_fee_tr" style="display: none;">
            <td>非免邮:</td>
            <td><div id="not_fee_div"></div></td>
        </tr>

        <tr>
            <td colspan="2" style="text-align: center"><a href="javascript:void(0)" class="easyui-linkbutton"
                               onclick="saveGoodsFlag()" style="width: 80px">保存</a> <a
                    href="javascript:void(0)" class="easyui-linkbutton"
                    onclick="hideGoodsDlg()" style="width: 80px">关闭</a></td>
        </tr>
    </table>
</div>

</body>
</html>