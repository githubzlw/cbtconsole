<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <style type="text/css">
    </style>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/jquery-form.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
    <title>上线店铺录入</title>
    <link rel="stylesheet"
          href="/cbtconsole/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
    <script type="text/javascript"
            src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">

    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
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
            font-size: 20px;
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
            font-size: 28px;
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
            /*width: 70px;*/
            height: 24px;
            border: 1px #aaa solid;
            color: #fff;
            margin-left: 12px;
        }

        .but_color2 {
            background: #44a823;
            width: 96px;
            height: 24px;
            border: 1px #aaa solid;
            color: #fff;
            margin-left: 12px;
        }

        .but_authorized {
            background: #2836e6;
            width: 70px;
            height: 24px;
            border: 1px #aaa solid;
            color: #fff;
            margin-left: 12px;
        }

        .but_authorized2 {
            background: #2836e6;
            /* width: 96px; */
            height: 24px;
            border: 1px #aaa solid;
            color: #fff;
            margin-left: 12px;
        }

        .del_color {
            background: red;
            width: 70px;
            height: 24px;
            border: 1px #aaa solid;
            color: #fff;
            margin-left: 12px;
        }

        .remark_color {
            background: #2836e6;
            width: 70px;
            height: 24px;
            border: 1px #aaa solid;
            color: #fff;
            margin-left: 12px;
        }

        .datagrid-row {
            height: 33px;
            text-align: center;
        }

        #authorized_dlg table {
            margin: 10px;
        }

        #authorized_dlg table td {
            padding: 4px;
        }

        td > .datagrid-cell {
            text-align: left !important;
            padding: 5px 0;
            font-size: 12px;
        }

        td > .datagrid-cell > button {
            margin-bottom: 10px;
        }
    </style>
    <%
  String admName=request.getParameter("admName");
  String days=request.getParameter("days");
%>
</head>
<body onload="$('#dlg').dialog('close');doQuery(1)">
<div id="dlg" class="easyui-dialog" title="店铺url"
     data-options="modal:true"
     style="width: 410px; height: 740px; padding: 10px;">
    <input type="hidden" id="sid"/>
    <div style="margin-bottom: 10px;">
        <label>类型:</label> <select name="urlType" id="urlType"
                                   onchange="showUrlType()">
        <option value="0">店铺</option>
        <option value="1">店铺部分分类</option>
    </select>
    </div>


    <div style="margin-bottom: 10px;">
        <label>店铺地址:</label><input name="shopUrl" id="detailShopUrl"
                                   style="width: 290px">
    </div>

    <div style="margin-bottom: 10px; display: none" class="typeShopUrl">
        <label>店铺分类url:</label><input name="typeShopUrl" style="width: 275px">
    </div>
    <div style="margin-bottom: 10px; display: none" class="typeShopUrl">
        <label>店铺分类url:</label><input name="typeShopUrl" style="width: 275px">
    </div>
    <div style="margin-bottom: 10px; display: none" class="typeShopUrl">
        <label>店铺分类url:</label><input name="typeShopUrl" style="width: 275px">
    </div>
    <div style="margin-bottom: 10px; display: none" class="typeShopUrl">
        <label>店铺分类url:</label><input name="typeShopUrl" style="width: 275px">
    </div>
    <div style="margin-bottom: 10px; display: none" class="typeShopUrl">
        <label>店铺分类url:</label><input name="typeShopUrl" style="width: 275px">
    </div>


    <div style="margin-bottom: 10px;">
        <label>店铺Id:</label><!--<input name="shopId" id="detailShopId"
				style="width: 300px">  --><span id="detailShopId"></span>
    </div>
    <div style="margin-bottom: 10px;">
        <label>抓取店铺Id:</label><input name="member_id" id="member_id"
                                   style="width: 290px">
    </div>

    <div style="margin-bottom: 10px;">
        <label>销量阀值:</label><input name="salesVolume" id="salesVolume"
                                   style="width: 290px">
    </div>
    <div style="margin-bottom: 10px;">
        <label>下载数量:</label><input name="downloadNum" id="downloadNum"
                                   style="width: 290px">
        <!--<span style="margin-left: 53px;color:red;">下载商品数量请选择 20以上</span>-->
    </div>
    <div style="margin-bottom: 10px;">
        <label>店铺质量评分:</label><span id="shopQualityScore"></span>
    </div>
    <div style="margin-bottom: 10px;">
        <label>店铺服务评分:</label><span id="shopServiceScore"></span>
    </div>
    <div style="margin-bottom: 10px;">
        <label>店铺级别:</label><span id="level"></span>
    </div>

    <div style="margin-bottom: 10px;">
        <label>店铺名称:</label><input name="inputShopName" id="inputShopName"
                                   style="width: 290px">
    </div>

    <div style="margin-bottom: 10px;">
        <label>店铺描述:</label>
        <textarea name="inputShopDescription" id="inputShopDescription" style="width: 290px;height: 80px;"></textarea>
    </div>
    
    <div style="margin-bottom: 10px;">
        <label>店铺英文:</label><input name="inputShopEnName" id="inputShopEnName"
                                   style="width: 290px">
    </div>
    
    <div style="margin-bottom: 10px;">
        <label>品牌属性:</label><input name="inputShopBrand" id="inputShopBrand"
                                   style="width: 290px">
    </div>

    <div style="margin-bottom: 10px;">
        <label>是否启用:</label><select name="isValid" id="isValid">
        <option value="0">是</option>
        <option value="1">否</option>
    </select>
    </div>


    <div style="margin-bottom: 10px;">
        <label>外贸标识:</label><select name="isTrade" id="is_trade">
        <option value="0">否</option>
        <option value="1">是</option>
    </select><span style="color: red">*外贸同事使用时，请选择“是”</span>
    </div>

    <div style="text-align: center; padding: 5px 0">
        <button id="is_main_input" onclick="updateReply(1)" style="width: 120px;display: none;">转为手动录入店铺</button>
        <a href="javascript:void(0)" class="easyui-linkbutton"
           onclick="updateReply(0)" style="width: 80px">保存</a> <a
            href="javascript:void(0)" class="easyui-linkbutton"
            onclick="$('#dlg').dialog('close');" style="width: 80px">关闭</a>
    </div>
</div>


<div id="remark_dlg" class="easyui-dialog" title="确认问题店铺"
     data-options="modal:true"
     style="width: 450px; height: 300px;">

    <div style="margin-bottom: 10px;">
        <br><br>
        <span style="width: 80px;">店铺 ID：</span> <input type="text" id="del_shop_id" style="width: 290px"
                                                        readonly="readonly"/>
        <br><br>
        <span style="width: 80px;">问题类型:</span> <select name="delType" id="del_type" style="width: 290px">
        <option value="1">MOQ太高</option>
        <option value="2">水印太多</option>
        <option value="3">不适合运输</option>
        <option value="4">其他</option>
    </select>
        <br><br><br>
        <span style="width: 80px;">输入备注:</span> <textarea name="delRemark" id="del_remark"
                                                          style="width: 290px;height: 54px;"></textarea>
    </div>


    <div style="text-align: center; padding: 5px 0">
        <a href="javascript:void(0)" class="easyui-linkbutton"
           onclick="saveReadyDeleteShop()" style="width: 80px">保存</a> <a
            href="javascript:void(0)" class="easyui-linkbutton"
            onclick="$('#remark_dlg').dialog('close');" style="width: 80px">关闭</a>
    </div>
</div>

<div id="authorized_dlg" class="easyui-dialog" title="上传/修改授权文件"
     data-options="modal:true"
     style="width: 450px; height: 400px;">

    <div style="margin-bottom: 10px;">
    	<form method="post" enctype="multipart/form-data">
	    	<table>
                <input type="hidden" id="authorized_id" name="authorized_id"/>
	    		<tr>
	    			<td>店铺 ID:</td>
	    			<td><input type="text" readonly="readonly" id="authorized_shop_id" name="authorized_shop_id" 
	    				style="background-color:#EEE;border:1px solid;border-color: rgb(169, 169, 169);"></td>
	    		</tr>
	    		<tr>
					<td>授权文件:<br />(增加或修改)</td>
					<td>
						<a target="_blank" id="authorized_filename" href="#" style=""></a><br />
						<input name="uploadfile" class="easyui-filebox"
						data-options="prompt:'授权文件或授权图片'"
						style="width: 220px;"></td>
				</tr>
	    		<!-- <tr><td>上传人:</td><td></td></tr> -->
	    		<tr>
	    			<td>授权开始时间:</td>
	    			<td><input class="easyui-datebox" style="width: 100px;" name="startTime"
	                   id="startTime" value="" data-options=""/></td>
	            </tr>
	    		<tr>
	    			<td>授权结束时间:</td>
					<td><input class="easyui-datebox" style="width: 100px;" name="endTime"
	                   id="endTime" value="" data-options=""/></td>
	    		</tr>
	    		<tr>
	    			<td>授权备注:</td>
	    			<td>
	    				<textarea name="authorized_remark" id="authorized_remark" style="width: 290px;height: 54px;"></textarea>
	    			</td>
	    		</tr>
                <tr>
                    <td>品牌属性:</td>
                    <td><input type="text" id="authorized_shop_brand" name="authorized_shop_brand"></td>
                </tr>
	    	</table>
	    </form>
    </div>

    <div style="text-align: center; padding: 5px 0">
        <a href="javascript:void(0)" class="easyui-linkbutton"
           onclick="saveAuthorizedInfo()" style="width: 80px">保存</a> 
        <a href="javascript:void(0)" class="easyui-linkbutton"
           onclick="$('#authorized_dlg').dialog('close');" style="width: 80px">关闭</a>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <a href="javascript:void(0)" class="easyui-linkbutton"
           onclick="deleteAuthorizedInfo()" style="width: 80px">删除</a>
    </div>
</div>

<div id="top_toolbar" style="padding: 5px; height: auto">
    <div>
        <form id="query_form" action="#" style="margin-left: 10px;font-size: 12px;"
              onsubmit="return false;">
            <div>

                <input type="hidden" id="admName" name="admName">
                <input type="hidden" id="days" name="days">
                <span>店铺ID:<input class="easyui-textbox" name="shopId" id="shopId"
                                  style="width:220px; margin-top: 10px;"/></span>
                &nbsp;&nbsp;
                <span>上线状态:<select id="state_id" class="easyui-combobox" name="isOn" style="width:150px;">
            <option value="-1">全选</option>
            <option value="0">代办</option>
            <option value="2">详情图片已下载</option>
            <option value="4">数据上线成功</option>
            <option value="7">数据准备完成，待上线</option>
            <option value="6">数据上传失败</option>
            <option value="5">数据上传中</option>
            <option value="3">图片已上传美服</option>
            <option value="1">详情信息已下载</option>
        </select></span>
                &nbsp;&nbsp;<span>录入来源:<select id="is_auto" class="easyui-combobox" name="isAuto" style="width:100px;">
            <option value="-1">全选</option>
            <option value="0">手动录入</option>
            <option value="1">自动导入</option>
        </select></span>

                &nbsp;&nbsp;<span>是否启用:<select id="is_on" class="easyui-combobox" name="isOn" style="width:100px;">
            <option value="-1">全选</option>
            <option value="0">启用</option>
            <option value="1">不启用</option>
        </select></span>&nbsp;&nbsp;
                <span>店铺英文及品牌属性筛选:<select id="enname_brand_flag" class="easyui-combobox" name="authorizedFlag" style="width:180px;">
            <option value="-1">无筛选</option>
            <option value="1">1-店铺英文为空</option>
            <option value="2">2-品牌属性为空</option>
            <option value="3">3-店铺英文+品牌属性都为空</option>
            <option value="4">4-店铺英文不为空</option>
            <option value="5">5-品牌属性不为空</option>
            <option value="6">6-店铺英文+品牌属性都不为空</option>
        </select></span>
                &nbsp;&nbsp;<span>负责人:<input class="easyui-textbox" name="shopUserName" id="shopUserName"
                                             style="width: 130px; margin-top: 10px;"/></span>
                &nbsp;&nbsp;<span>开始时间:<input class="easyui-datebox" style="width: 100px;" name="timeFrom"
                                              id="timeFrom" value="${param.timeFrom}"/></span>
                &nbsp;&nbsp;<span>截止时间:<input class="easyui-datebox" style="width: 100px;" name="timeTo" id="timeTo"
                                  value="${param.timeTo}"/></span>

            </div>
            <div>
                <span>品牌属性:<input class="easyui-textbox" name="shopBrand" id="shopBrand"
                                  style="width:210px; margin-top: 10px;"/></span>
                &nbsp;&nbsp;
                <span>授权店铺:<select id="authorized_flag" class="easyui-combobox" name="authorizedFlag"
                                   style="width:150px;">
            <option value="-1">全选</option>
            <option value="0">0-未去问是否给授权</option>
            <option value="1">1-已给授权</option>
            <option value="2">2-暂不给授权但可卖</option>
            <option value="3">3-不给授权</option>
        </select></span>
                &nbsp;&nbsp;<span>店铺描述:<select id="shop_type" class="easyui-combobox" name="shopType"
                                               style="width:100px;">
            <option value="-1">全选</option>
            <option value="0">正常</option>
            <option value="1">精品店铺</option>
            <option value="2">侵权店铺</option>
        </select></span>

                &nbsp;&nbsp;<span>问题店铺:<select id="ready_del" class="easyui-combobox" name="readyDel"
                                               style="width:100px;">
            <option value="-1">全选</option>
            <option value="0">正常</option>
            <option value="1">MOQ太高</option>
            <option value="2">水印太多</option>
            <option value="3">不适合运输</option>
            <option value="4">其他</option>
        </select></span>
                &nbsp;&nbsp;<span>授权文件问题:<select id="authorized_file_flag" class="easyui-combobox" name="authorizedFlag"
                                                 style="width:240px;">
            <option value="-1">无筛选</option>
            <option value="1">1-已授权但无授权文件</option>
            <option value="2">2-授权文件到期</option>
            <option value="3">3-已授权但无授权文件+授权文件到期</option>
        </select></span>
                &nbsp;&nbsp;<span>类别ID:
                <input class="easyui-textbox" name="catid" id="catid_id"
                                  style="width:122px; margin-top: 10px;"/></span>
                &nbsp;&nbsp;<span>翻译产品描述:<select id="translate_description" class="easyui-combobox"
                                                   name="translateDescription" style="width:95px;">
                <option value="-1">全选</option>
                <option value="0">未标记</option>
                <option value="1">已标记</option>
            </select></span>
                &nbsp;&nbsp;<span>店铺上线:<select id="is_shop_flag" class="easyui-combobox" name="isShopFlag"
                                               style="width:100px;">
                <option value="-1">全部</option>
                <option value="1">是</option>
                <option value="0">否</option>
            </select></span>
                <input
                    class="but_color" type="button" value="查询" onclick="doQuery(1)">
                &nbsp;&nbsp;<input class="but_color" type="button" value="重置"
                                   onclick="doReset()">
                &nbsp;&nbsp;<input class="but_color" type="button"
                                   value="新增" onclick="doAdd()">

            </div>

            <!--<input class="but_color" type="button" value="导入excel"
                onclick="updateExcel()"> -->
        </form>
        <form id="form" method="post" enctype="multipart/form-data"
              style="display: none">
            <input id="file_upload" name="files" type="file" multiple="true"
                   onchange="uploadExcel(this)">
        </form>


    </div>

</div>

<table class="easyui-datagrid" id="easyui-datagrid"
       style="width: 1200px; height: 900px">
    <thead>
    <tr>
        <th data-options="field:'shopId',width:120,align:'center'">店铺ID</th>
        <%--<th data-options="field:'inputShopName',width:50,align:'center'">店铺名称</th>--%>
        <th data-options="field:'shopUrl',width:120,align:'center',formatter:formatShopInfo">店铺信息</th>
        <%--<th data-options="field:'inputShopEnName',width:100,align:'center'">店铺英文</th>
        <th data-options="field:'inputShopBrand',width:80,align:'center'">品牌属性</th>--%>
        <th data-options="field:'onLineNum',width:120,align:'center',formatter:formatShopNum">已在线商品/状态</th>
        <%--<th data-options="field:'isValidView',width:40,align:'center'">是否启用</th>--%>
        <th data-options="field:'onlineStatusView',width:80,align:'center',formatter:formatState">启用/店铺上线/授权</th>
        <th data-options="field:'updatetime',width:80,align:'center',formatter:formatUpdateInfo">更新信息</th>
        <%--<th data-options="field:'admUser',width:50,align:'center'">编辑者</th>--%>
        <%--<th data-options="field:'isAuto',width:50,align:'center',formatter:formatAuto">录入来源</th>--%>
        <%--<th data-options="field:'authorizedFlag',width:50,align:'center',formatter:formatAuthorizedFlag">授权标识</th>--%>
        <th data-options="field:'authorizedOldInfo',width:80,align:'center',formatter:formatOldAuthorizedInfo">原授权操作</th>
        <th data-options="field:'authorizedInfo',width:200,align:'center',formatter:formatAuthorizedInfo">店铺品牌信息</th>
        <th data-options="field:'inputShopDescription',width:90,align:'center'">店铺描述</th>
        <th data-options="field:'stateInfo',width:120,align:'center'">操作</th>
        <%--<th data-options="field:'opsAction',width:100,align:'center',formatter:formatShopBrand">操作</th>--%>
    </tr>
    </thead>
</table>
</body>
<script type="text/javascript">
	function formatNum(val) {
	    return val < 10 ? "0" + val : val
	}
	/*时间格式化*/
	function formatterData(date) {
	    var date = new Date(date);
	    return date.getFullYear() + '-' + formatNum(date.getMonth() + 1) + '-' + formatNum(date.getDate());
	} 
    $(function () {
        var admName='<%=admName%>';
        var days='<%=days%>';
        setDatagrid();
        var shopIdParam = "${param.shopId}";
        if(!(shopIdParam ==null || shopIdParam == "")){
            $("#shopId").textbox("setValue", shopIdParam);
            //$("#shopId").val(shopIdParam);
        }
        if(admName != null && admName != ""){
            $("#admName").val(admName);
            $("#days").val(days);
        }else{
            $("#admName").val("");
            $("#days").val("");
        }
        var opts = $("#easyui-datagrid").datagrid("options");
        opts.url = "/cbtconsole/ShopUrlC/findAll";

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
        $('#remark_dlg').dialog('close');
        $('#authorized_dlg').dialog('close');
    });

    function setDatagrid() {
        $('#easyui-datagrid').datagrid({
            title: '上线店铺录入(录入数据2小时内更新状态)',
            //iconCls : 'icon-ok',
            width: "100%",
            fit: true,//自动补全
            pageSize: 50,//默认选择的分页是每页20行数据
            pageList: [50],//可以选择的分页集合
            nowrap: false,//设置为true，当数据长度超出列宽时将会自动截取
            striped: true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
            toolbar: "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
            url: '',//url调用Action方法
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
        var admName=$("#admName").val();
        var days=$("#days").val();
        var data = $("#data").val();
        //var questionType=$('#questionType').combobox('getValue');
        var shopId = $("#shopId").textbox("getValue");
        var shopBrand = $("#shopBrand").textbox("getValue");
        var shopUserName = $('#shopUserName').val();
        var username = $("#username").val();
        var timeFrom = $("#timeFrom").val();
        var timeTo = $("#timeTo").val();
        var is_on = $("#is_on").combobox('getValue');
        var ready_del = $("#ready_del").combobox('getValue');
        var translate_description = $("#translate_description").combobox('getValue');
        var isAuto = $("#is_auto").combobox('getValue');
        var state_id = $("#state_id").combobox('getValue');
        var shopType = $("#shop_type").combobox('getValue');
        var authorizedFlag = $("#authorized_flag").combobox('getValue');
        var authorizedFileFlag = $("#authorized_file_flag").combobox('getValue');
        var ennameBrandFlag = $("#enname_brand_flag").combobox('getValue');
        var isShopFlag = $("#is_shop_flag").combobox('getValue');
        var catid = $("#catid_id").textbox("getValue");
        $("#easyui-datagrid").datagrid("load", {
            "page": page,
            "createTime": data,
            "shopId": shopId,
            "shopBrand": shopBrand,
            "shopUserName": shopUserName,
            "timeFrom": timeFrom,
            "timeTo": timeTo,
            "isOn": is_on,
            "readyDel": ready_del,
            "translateDescription": translate_description,
            "isShopFlag": isShopFlag,
            "isAuto": isAuto,
            "state": state_id,
            "shopType":shopType,
            "authorizedFlag":authorizedFlag,
            "authorizedFileFlag":authorizedFileFlag,
            "ennameBrandFlag":ennameBrandFlag,
            "admName":admName,
            "days":days,
            "catid":catid
        });
    }

    function formatAuto(val, row, index) {
        var content = '';
        if (val == 1) {
            content = '自动导入';
        } else {
            content = '手动录入';
        }
        return content;
    }

    function formatShopInfo(val, row, index) {
        var content = '<div style="font-size: 13px;">';
        content += '<span>店铺英文:'+row.inputShopEnName+'</span>';
        content += '<br><span>品牌属性:'+row.inputShopBrand+'</span>';
        content += '<br><span>品牌名称:<b style="background-color: #94ff84;">['+row.brandNames+']</b></span>';
        content += '<br><span>URL:<a href="'+val+'" target="_blank">'+val+'</a></span>';
        content += '</div>';
        return content;
    }

    function formatShopNum(val, row, index) {
        var content = '<div style="font-size: 13px;">';
        content += '<b>在线商品数:<a href="/cbtconsole/website/shop_goods_list.jsp?shop_id=' + row.shopId
            + '" target="_blank"><b style="color: red;"> ' + val + '</b>(view)</a></b>';
        content += '<br><b style="color: #101010;font-size: 14px;background-color: #baf1cdf2;">售卖金额($):'+row.shopPrice+'</b>';
        content += '<br><b style="color: #44a823;font-size: 14px;">状态:'+row.onlineStatusView+'</b>';
        content += '</div>';
        return content;
    }

    function formatState(val, row, index) {
        var content = '<div style="font-size: 13px;">';
        content += '<span>是否启用:'+row.isValidView+'</span>';
        if (row.isShopFlag == 1) {
            content += '<br><span>是否店铺上线 :是</span>';
        } else {
            content += '<br><b style="color: red;">是否店铺上线:否</b>';
        }
        if(row.authorizedFlag == 1){
            content += '<br><b style="color:green;">授权:已授权</b>';
        }else if(row.authorizedFlag == 2){
            content += '<br><b style="color:#46b7e4;">授权:部分授权</b>';
        }else if(row.authorizedFlag == 3){
            content += '<br><b style="color:red;">授权:未授权</b>';
        }
        // content += '<button class="but_color" onclick="reUpdateShopAdminId(\''+row.shopId+'\')">设置当前店铺为自己</button>';
        content += '</div>';
        return content;
    }

    function reUpdateShopAdminId(shopId) {
        $.ajax({
            url: '/cbtconsole/ShopUrlC/reUpdateShopAdminId',
            type: "post",
            data: {shopId: shopId},
            success: function (res) {
                if (!res.ok) {
                    $.messager.alert('提示', '操作失败');
                } else {
                    $.messager.alert('提示', '操作成功');
                    var number = $('#easyui-datagrid').datagrid('options').pageNumber;
                    setTimeout(function () {
                        doQuery(number);
                    }, 2000)
                }
            },
            error: function () {
                $.messager.alert('提示', '操作失败');
            }
        });
    }

    function formatUpdateInfo(val, row, index) {
        var content = '<div style="font-size: 13px;">';
        content += '<span>创建日期:'+row.createTime+'</span>';
        /*content += '<br><span>更新日期:'+val+'</span>';*/
        content += '<br><span>负责人:'+row.admUser+'</span>';
        if (row.isAuto == 1) {
            content += '<br><span>录入来源:自动导入</span>';
        } else {
            content += '<br><span>录入来源:手动录入</span>';
        }
        content += '</div>';
        return content;
    }

    function formatAuthorizedFlag(val, row, index) {
    	if (val == undefined) {
			return '';
		}
    	switch (val) {
		case 1:
			return '已授权';
			break;
		case 2:
			return '暂不给授权但可卖';
			break;
		case 3:
			return '不给授权';
			break;
		case 0:
			return '未去问是否给授权';
			break;
		}
    }

    function openBrandInfo(shopId) {
        var url = '/cbtconsole/ShopUrlC/shopBrandAuthorizationList?shopId=' + shopId;
        var param = "height=660,width=1430,top=190,left=230,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
        window.open(url, "windows", param);
    }

    function formatShopBrand(val, row, index) {
        // return "<a target='_blank' href='/cbtconsole/ShopUrlC/shopBrandAuthorizationList?shopId=" + row.shopId + "'>设置店铺品牌</a>";
        return '<button class="but_color" onclick="openBrandInfo(\''+row.shopId+'\')">设置店铺品牌</button>';
    }

    function formatAuthorizedInfo(val, row, index) {
        var countent = '<table border="2" cellspacing="1" cellpadding="1">';
        var authorizationList = row.authorizationList;
        if (authorizationList) {
            countent += '<tr style="border: 1px solid #7fe63d;">'
                +'<td style="border: 2px solid #7fe63d;width: 180px;">品牌名称</td>'
                +'<td style="border: 2px solid #7fe63d;width: 80px;text-align: center;">授权状态</td>'
                +'<td style="border: 2px solid #7fe63d;width: 100px;text-align: center;">有效期</td></tr>';
            for (var i=0;i< authorizationList.length;i++) {
                countent += '<tr style="border: 1px solid #7fe63d;">';
                countent += '<td style="border: 2px solid #7fe63d;">'+authorizationList[i].brandName+'</td>';
                if(authorizationList[i].authorizeState == 0){
                    countent += '<td style="border: 2px solid #7fe63d;text-align: center;"><b style="background-color: #ef8197;">无授权</b></td>';
                    countent +='<td style="border: 2px solid #7fe63d;"></td>';
                }else if(authorizationList[i].authorizeState == 1){
                    countent += '<td style="border: 2px solid #7fe63d;text-align: center;"><b style="background-color: #98f198;">已授权</b></td>';
                    if(checkIsValid(authorizationList[i].termOfValidity)){
                        countent += '<td style="border: 2px solid #7fe63d;text-align: center;"><b style="background-color: red;">'+authorizationList[i].termOfValidity+'</b></td>';
                    }else{
                        countent += '<td style="border: 2px solid #7fe63d;text-align: center;">'+authorizationList[i].termOfValidity+'</td>';
                    }
                }else if(authorizationList[i].authorizeState == 2){
                    countent += '<td style="border: 2px solid #7fe63d;text-align: center;"><b style="background-color: #98f198;">自有品牌</b></td>';
                    countent +='<td style="border: 2px solid #7fe63d;"></td>';
                }else if(authorizationList[i].authorizeState == 3){
                    countent += '<td style="border: 2px solid #7fe63d;text-align: center;"><b style="background-color: #98f198;">无需授权</b></td>';
                    countent +='<td style="border: 2px solid #7fe63d;"></td>';
                }else if(authorizationList[i].authorizeState == -1){
                    countent += '<td style="border: 2px solid #7fe63d;text-align: center;"><b style="background-color: red;">侵权</b></td>';
                    countent +='<td style="border: 2px solid #7fe63d;"></td>';
                }
                countent += '</tr>';
            }
        }
        countent +='</table>';
        countent +='<button class="but_color" onclick="openBrandInfo(\''+row.shopId+'\')">设置店铺品牌</button>';
        return countent;
    }

    function checkIsValid(dayTime){
        var noValid = false;
        var date = new Date(dayTime);
        var today = new Date();
        var num = date.getTime() - today.getTime();
        if(num > 0 &&　num <= 24 * 60 * 60 * 1000){
            noValid = true;
        }else if(num < 0){
            noValid = true;
        }
        return noValid;
    }

    //授权标记操作
    function formatOldAuthorizedInfo(val, row, index) {
    	if(row.shopId != null){ /* 已授权店铺才有操作 */
    		var temHtml = "";
            /*temHtml += "<button class=\"but_authorized\" onclick=\"setAuthorizedFlag('" + row.shopId + "', '3')\">不给授权</button>";
    		temHtml += "<button class=\"but_authorized\" onclick=\"setAuthorizedFlag('" + row.shopId + "', '1')\">已给授权</button>";
    		temHtml += "<button class=\"but_authorized2\" onclick=\"setAuthorizedFlag('" + row.shopId + "', '2')\">暂不给授权但可卖</button><br />";
    		if (row.authorizedFlag == 1) {
	    		if (row.authorizedInfo.fileUrl == undefined) {
			        temHtml += "<button class=\"but_color2\" onclick=\"authorizedEdit('" + row.shopId + "')\">上传授权文件</button>";
				} else {
				    temHtml += "<button class=\"but_color2\" onclick=\"authorizedEdit('" + row.shopId + "')\">编辑授权文件</button>";
				    temHtml += "<br />之前上传的授权文件:<a target=\"_blank\" href=\"" + row.authorizedInfo.fileUrl + "\">" + row.authorizedInfo.fileName + "(" + row.authorizedInfo.admuser + ")</a>"
				    if (row.authorizedInfo.endTime != undefined) {
				    	temHtml += "<br />有效期至:" + formatterData(row.authorizedInfo.endTime) + " remark:" + row.authorizedInfo.remark;
					}
				}
    		}*/
    		if(row.authorizedInfo.fileUrl){
    		    temHtml += "<br />之前上传的授权文件:<a target=\"_blank\" href=\"" + row.authorizedInfo.fileUrl + "\">" + row.authorizedInfo.fileName + "(" + row.authorizedInfo.admuser + ")</a>"
                if (row.authorizedInfo.endTime) {
                    temHtml += "<br />有效期至:" + formatterData(row.authorizedInfo.endTime) + " remark:" + row.authorizedInfo.remark;
                }
            }
		    return temHtml;
    	}
    	return "";
    }

    var delreply = function (id) {
        $.messager.confirm('提示', '确定删除该数据吗?', function (r) {
            if (r) {
                $.ajax({
                    url: '/cbtconsole/ShopUrlC/deleShopUrl.do',
                    type: "post",
                    data: {id: id},
                    success: function (res) {
                        if (!res.ok) {
                            $.messager.alert('提示', '操作失败');
                        } else {
                            $.messager.alert('提示', '操作成功');
                            var number = $('#easyui-datagrid').datagrid('options').pageNumber;
                            setTimeout(function () {
                                doQuery(number);
                            }, 2000)
                        }
                    },
                    error: function () {
                        $.messager.alert('提示', '操作失败');
                    }
                });
            }
        });
    };

    function doReset() {
        $('#query_form').form('clear');
        $('#status').combobox('setValue', '-1');
        $('#questionType').combobox('setValue', '0');
    }

    //编辑
    function edit(id,isAuto) {
        if (!id) {
            return
        }
        $('#dlg').dialog('open');

        if(isAuto > 0){
            $("#is_main_input").show();
        }else{
            $("#is_main_input").hide();
        }
        $('#detailShopUrl').val('');
        $('#detailShopId').text('');
        $('#member_id').val('');
        $('#salesVolume').val('');
        $('#downloadNum').val('');
        $('#shopQualityScore').html('');
        $('#shopServiceScore').html('');
        $('#level').html('');
        $('#urlType').val(0);
        $('#isValid').val(0);
        $('#is_trade').hide();
        $('.typeShopUrl  input').val('');
        $('#sid').val(id);
        $('#detailShopUrl').parent().show();
        $('.typeShopUrl').hide();
        $('#inputShopName').val('');
        $('#inputShopDescription').val('');
        $('#inputShopEnName').val('');
        $('#inputShopBrand').val('');

        $.ajax({
            url: "/cbtconsole/ShopUrlC/selectOneShop.do",
            data: {"id": id},
            type: "post",
            success: function (data) {
                if (data.ok) {
                    if (data.data) {
                        $('#detailShopUrl').val(data.data.shopUrl ? data.data.shopUrl : "");
                        $('#inputShopName').val(data.data.inputShopName ? data.data.inputShopName : "");
                        $('#inputShopDescription').val(data.data.inputShopDescription ? data.data.inputShopDescription : "");
                        $('#inputShopEnName').val(data.data.inputShopEnName ? data.data.inputShopEnName : "");
                        $('#inputShopBrand').val(data.data.inputShopBrand ? data.data.inputShopBrand : "");
                        $('#detailShopId').text(data.data.shopId ? data.data.shopId : "");
                        $('#member_id').val(data.data.memberId ? data.data.memberId : "");
                        $('#salesVolume').val(data.data.salesVolume ? data.data.salesVolume : 0);
                        $('#downloadNum').val(data.data.downloadNum ? data.data.downloadNum : 0);
                        $('#isValid').val(data.data.isValid ? data.data.isValid : 0);
                        $('#urlType').val(data.data.urlType ? data.data.urlType : 0);
                        $('#shopQualityScore').html(data.data.qualityAvg ? data.data.qualityAvg : "");
                        $('#shopServiceScore').html(data.data.serviceAvg ? data.data.serviceAvg : "");
                        $('#level').html(data.data.level ? data.data.level : "");

                        if (data.data.urlType == 1) {
                            $('#detailShopUrl').parent().hide();
                            $('.typeShopUrl').show();

                            if (data.data.stuList) {
                                for (var i = 0; i < data.data.stuList.length; i++) {
                                    $('.typeShopUrl input').eq(i).val(data.data.stuList[i].shopUrl);
                                }
                            }
                        }
                    }

                }
            },
            error: function (e) {
                alert("没有数据！");
            }
        });
    };


    function doAdd() {
        $('#sid').val('');
        $('#inputShopName').val('');
        $('#inputShopDescription').val('');
        $('#inputShopEnName').val('');
        $('#inputShopBrand').val('');
        $('#detailShopUrl').val('');
        $('#detailShopId').text('');
        $('#salesVolume').val('');
        $('#member_id').val('');
        $('#downloadNum').val('');
        $('#shopQualityScore').html('');
        $('#shopServiceScore').html('');
        $('#level').html('');
        $('#urlType').val(0);
        $('#isValid').val(0);
        $('#is_trade').val(0);
        $('.typeShopUrl  input').val('');
        $('#detailShopUrl').parent().show();
        $('.typeShopUrl').hide();
        $('#dlg').dialog('open');
    }

    function updateReply(type) {
        var id = $("#sid").val();
        var detailShopUrl = $('#detailShopUrl').val();
        var detailShopId = $('#detailShopId').text();
        var salesVolume = $('#salesVolume').val();
        var downloadNum = $('#downloadNum').val();
        var isValid = $('#isValid').val();
        var urlType = $('#urlType').val();
        var inputShopName = $('#inputShopName').val();
        var inputShopDescription = $('#inputShopDescription').val();
        var inputShopEnName = $('#inputShopEnName').val();
        var inputShopBrand = $('#inputShopBrand').val();
        var isTrade = $('#is_trade').val();
        var member_id = $('#member_id').val();
        /*
         if(detailShopUrl==null||detailShopUrl==""){
             $.messager.alert('提示','请输入店铺url');
             return false;
         }
         */

        var typeShopUrls = [];
        $('.typeShopUrl input').each(function () {
            typeShopUrls.push($(this).val());

        });

        if(isTrade == null || isTrade == ""){
            isTrade = "0";
        }
        var params = {
            "id": id,
            "shopUrl": detailShopUrl,
            "shopId": detailShopId,
            "memberId":member_id,
            "salesVolume": salesVolume,
            "downloadNum": downloadNum,
            "isValid": isValid,
            "isTrade":isTrade,
            "urlType": urlType,
            "inputShopName": inputShopName,
            "inputShopDescription": inputShopDescription,
            "inputShopEnName": inputShopEnName,
            "inputShopBrand": inputShopBrand,
            "typeShopUrls": typeShopUrls.toString(),
            "saveType": type
        };


        $.ajax({
            url: '/cbtconsole/ShopUrlC/insertOrUpdate.do',
            type: "post",
            data: params,
            success: function (data) {
                if (!data.ok) {
                    if(data.total > 0){
                        $.messager.confirm("提示", "已经存在此店铺，需要重新抓取数据吗？", function (rs) {
                            if (rs) {
                                reDownShopGoods(data.data);
                            }
                        });
                    }else{
                        $.messager.alert('提示', data.message);
                    }
                } else {
                    $('#dlg').dialog('close');
                    var number = $('#easyui-datagrid').datagrid('options').pageNumber;
                    setTimeout(function () {
                        doQuery(number);
                    }, 2000)
                }
            }
        });
    }

    function reDownShopGoods(shopId) {
        $.ajax({
            url: '/cbtconsole/ShopUrlC/reDownShopGoods.do',
            type: "post",
            data: {"shopId":shopId},
            success: function (data) {
                if (data.ok) {
                    $('#dlg').dialog('close');
                    $('#easyui-datagrid').datagrid("reload");
                } else {
                    $.messager.alert('提示', data.message);
                }
            }
        });
    }

    function updateExcel() {
        $('#file_upload').click()
    }

    function uploadExcel(obj) {

        $('#form').ajaxSubmit({
            type: "post",
            url: "/cbtconsole/ShopUrlC/updateExcel.do",
            dataType: "text",
            async: false,
            success: function (data) {
                var result = eval('(' + data + ')');
                if (!result.ok) {
                    $.messager.alert('提示', '操作失败');
                } else {
                    $.messager.alert('提示', '操作成功，共插入' + result.data + '条数据');
                    var number = $('#easyui-datagrid').datagrid('options').pageNumber;
                    setTimeout(function () {
                        doQuery(number);
                    }, 2000)

                }
            }
        })
    }

    function showUrlType() {

        var type = $('#urlType').val();
        $('#detailShopUrl').val('');
        $('.typeShopUrl input').val('');
        if (type == 0) {
            $('#detailShopUrl').parent().show();
            $('.typeShopUrl').hide();
        } else {
            $('#detailShopUrl').parent().hide();
            $('.typeShopUrl').show();
        }
    }

    function readyDelete(shopId) {
        if (!shopId) {
            return;
        }
        $("#del_shop_id").val(shopId);
        $('#remark_dlg').dialog('open');
    }
    
    //
    function authorizedEdit(shopId) {
        if (!shopId) {
            return;
        }
        $('#authorized_filename').attr("href", "");
		$('#authorized_filename').html("");
		$('#startTime').datebox('setValue', "");
		$('#endTime').datebox('setValue', "");
        $("#authorized_remark").val("");
        $("#authorized_id").val("");
        $.ajax({
        	type: "GET",
        	url: "/cbtconsole/tabseachpage/queryAuthorizedInfo?shopId=" + shopId,
        	dataType:"json",
        	success: function(msg){
        		$("#authorized_shop_id").val(shopId);
        		if(msg.status){
                    if (msg.bean != undefined) {
                        $("#authorized_id").val(msg.bean.id);
                        if (msg.bean.fileUrl != undefined) {
		        			$('#authorized_filename').attr("href", msg.bean.fileUrl);
		        			$('#authorized_filename').html("之前上传的授权文件:" + msg.bean.fileName + "(" + msg.bean.admuser + ")");
						}
	        			if (msg.bean.startTime != undefined) {
		        			$('#startTime').datebox('setValue', formatterData(msg.bean.startTime));
						}
	        			if (msg.bean.endTime != undefined) {
	        				$('#endTime').datebox('setValue', formatterData(msg.bean.endTime));
	        			}
	        	        $("#authorized_remark").val(msg.bean.remark);
	        	        $("#authorized_shop_brand").val(msg.bean.shopBrand);
					}
        		}
       	        $('#authorized_dlg').dialog('open');
        	}
        });
    }
    function saveAuthorizedInfo() {
    	$('#authorized_dlg form').form({    
    	    url:"/cbtconsole/tabseachpage/updateAuthorizedInfo",    
    	    onSubmit: function(){
    	    	/* var detail_update_seach_url = $("#detail_update_seach_url").val();
    	    	if(!isURL(detail_update_seach_url)) {
    	    		$.messager.alert('message','请填写正确的搜索链接！');
    	    		$.messager.progress('close');
    	    		return false;
    	    	} */
    			return true;  
    	    },    
    	    success:function(data){
    	    	var res = JSON.parse(data);
    	    	if(res.status) {
    	    		$.messager.alert('message',res.message);
    	    		/* setTimeout( function(){
    					detailList(keyword_id);
    		    		getDetail(res.id);
    				}, 1500 ); */
    		    	$('#authorized_dlg form').form('clear');
    				doQuery(1);
    	    	}else{
    	    		$.messager.alert('message',res.message);
    	    	}
    	    }    
    	});  
    	$("#authorized_dlg form").submit();
        $('#authorized_dlg').window('close');
	}

    function deleteAuthorizedInfo() {
        var shopId = $("#authorized_shop_id").val();
        if($.isEmptyObject(shopId)){
            $.messager.alert('message','店铺id为空!');
            return;
        }
        $.ajax({
            type: "POST",
            url: "/cbtconsole/tabseachpage/deleteAuthorizedInfo.do?shopId=" + shopId,
            dataType:"json",
            success: function(res){
                if(res.status) {
                    $.messager.alert('message',res.message);
                    $('#authorized_dlg').window('close');
                    $('#authorized_dlg form').form('clear');
                    doQuery(1);
                }else{
                    $.messager.alert('message',res.message);
                }
            }
        });
	}

    function saveReadyDeleteShop() {
        var del_shop_id = $("#del_shop_id").val();
        if(del_shop_id == null || del_shop_id == ""){
            $.messager.alert('提示', '获取店铺ID失败，请重新打开弹框','info');
            return false;
        }
        var del_type = $("#del_type").val();
        if(del_shop_id == null || del_shop_id == ""){
            $.messager.alert('提示', '获取问题类型失败，请重试','info');
            return false;
        }
        var del_remark = $("#del_remark").val();
        $.ajax({
            url: '/cbtconsole/ShopUrlC/saveReadyDeleteShop.do',
            type: "post",
            data: {
                "shopId" : del_shop_id,
                "type" : del_type,
                "remark" : del_remark
            },
            success: function (data) {
                if (data.ok) {
                    $("#del_shop_id").val("");
                    $("#del_type").val(1);
                    $("#del_remark").val("");
                    $('#remark_dlg').dialog('close');
                    $("#easyui-datagrid").datagrid("reload");
                } else {
                    $.messager.alert('提示', data.message,'error');
                }
            }
        });
    }

    function setShopType(shopId,type) {
        $.ajax({
            url: '/cbtconsole/ShopUrlC/setShopType.do',
            type: "post",
            data: {
                "shopId" : shopId,
                "type" : type
            },
            success: function (data) {
                if (data.ok) {
                    $("#easyui-datagrid").datagrid("reload");
                } else {
                    $.messager.alert('提示', data.message,'error');
                }
            }
        });
    }
    
    function setAuthorizedFlag(shopId, authorizedFlag) {
        $.ajax({
            url: '/cbtconsole/ShopUrlC/setAuthorizedFlag.do',
            type: "post",
            data: {
                "shopId" : shopId,
                "authorizedFlag" :authorizedFlag
            },
            success: function (data) {
                if (data.ok) {
                    $("#easyui-datagrid").datagrid("reload");
                } else {
                    $.messager.alert('提示', data.message,'error');
                }
            }
        });
    }


    function setShopTranslate(shopId,translateFlag) {
        $.ajax({
            url: '/cbtconsole/ShopUrlC/setShopTranslate.do',
            type: "post",
            data: {
                "shopId" : shopId,
                "translateFlag" :translateFlag
            },
            success: function (data) {
                if (data.ok) {
                    $("#easyui-datagrid").datagrid("reload");
                } else {
                    $.messager.alert('提示', data.message,'error');
                }
            }
        });
    }
    
    //授权文件问题 总数显示
    $.ajax({
		type: "GET",
		url: "/cbtconsole/ShopUrlC/queryAuthorizedFileFlag",
		dataType:"json",
		success: function(msg){
			if (msg.state == 1) {
				$('#authorized_file_flag').combobox({
				    valueField: 'label',
					textField: 'value',
					data: [{
						label: '-1',
						value: $("#authorized_file_flag").find("option[value=-1]").text()
					},{
						label: '1',
						value: $("#authorized_file_flag").find("option[value=1]").text() + "(" + msg.authorizedFileFlag1 + "条)"
					},{
						label: '2',
						value: $("#authorized_file_flag").find("option[value=2]").text() + "(" + msg.authorizedFileFlag2 + "条)"
					},{
						label: '3',
						value: $("#authorized_file_flag").find("option[value=3]").text() + "(" + msg.authorizedFileFlag3 + "条)"
					}]
				});
			}
		}
	});

</script>

</html>