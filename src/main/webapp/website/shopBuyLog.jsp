<%@ page language="java" contentType="text/html; charset=utf-8"
		 pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.cbt.util.Redis" %>
<%@page import="com.cbt.util.AppConfig"%>
<%@ page import="com.cbt.website.userAuth.bean.Admuser" %>
<%@ page import="com.cbt.util.SerializeUtil" %>
<!DOCTYPE html>
<html>
<head>
	<style type="text/css">
	</style>
	<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
	<title>供应商的采购历史</title>
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
	</script>
	<style type="text/css">
		.displaynone{display:none;}
		.item_box{display:inline-block;margin-right:52px;}
		.item_box select{width:150px;}
		.mod_pay3 { width: 600px; position: fixed;
			top: 100px; left: 15%;
			z-index: 1011; background: gray;
			padding: 5px; padding-bottom: 20px;
			z-index: 1011; border: 15px solid #33CCFF; }
		.w-group{margin-bottom: 10px;width: 60%;text-align: center;}
		.w-label{float:left;}
		.w-div{margin-left:120px;}
		.w-remark{width:100%;}
		table.imagetable {
			font-family: verdana,arial,sans-serif;
			font-size:11px;
			color:#333333;
			border-width: 1px;
			border-color: #999999;
			border-collapse: collapse;
		}
		table.imagetable th {
			background:#b5cfd2 url('cell-blue.jpg');
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
		.displaynone{display:none;}
		.but_color {
			background: #44a823;
			width: 80px;
			height: 24px;
			border: 1px #aaa solid;
			color: #fff;
		}

	</style>
	<%
	  String shopId=request.getParameter("shopId");
	%>
	<script type="text/javascript">
        $(function(){
            setDatagrid();
            var opts = $("#easyui-datagrid").datagrid("options");
            opts.url = "/cbtconsole/warehouse/getShopBuyLogInfo";
            var shopId='<%=shopId%>';
            if(shopId != null && shopId != ""){
				$("#shopId").val(shopId);
                doQuery(1);
			}
            $('#dlg1').dialog('close');
        })

        function setDatagrid() {
            $('#easyui-datagrid').datagrid({
                title : '供应商的采购历史',
                width : "100%",
                fit : true,//自动补全
                pageSize : 20,//默认选择的分页是每页20行数据
                pageList : [ 20],//可以选择的分页集合
                nowrap : false,//设置为true，当数据长度超出列宽时将会自动截取
                striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
                toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
                url : '',//url调用Action方法
                loadMsg : '数据装载中......',
                singleSelect : false,//为true时只能选择单行
                fitColumns : true,//允许表格自动缩放，以适应父容器
                idField:'itemid',
                //sortName : 'xh',//当数据表格初始化时以哪一列来排序
// 			sortOrder : 'desc',//定义排序顺序，可以是'asc'或者'desc'（正序或者倒序）。
                pagination : true,//分页
                rownumbers : true
                //行数
            });
        }


        function doQuery(page) {
            var orderid = $.trim(document.getElementById("orderid").value);
            var shopId=$("#shopId").val();
            $("#easyui-datagrid").datagrid("load", {
                "page":page,
                "orderid":orderid,
				"shopId":shopId
            });
        }

        function topCenter(msg){
            $.messager.show({
                title:'消息',
                msg:msg,
                showType:'slide',
                style:{
                    right:'',
                    top:document.body.scrollTop+document.documentElement.scrollTop,
                    bottom:''
                }
            });
        }


        function doReset(){
            $("#orderid").textbox('setValue','');
        }
        function openView(){
            $('#dlg1').dialog('open');
        }
        function cance1(){
            $('#dlg1').dialog('close');
            $("#su_data").textbox('setValue','0');
            $('#protocol').combobox('setValue','1');
            $('#quality').combobox('setValue','0');
            $("#remark").textbox('setValue','');
        }


        // 提交采购供应商打分数据
        function saveSupplier(){
            var shop_id=$("#shopId").val();
            if(shop_id == null || shop_id == "" || shop_id == "0000"){
                topCenter("店铺ID不符合打分规则");
                return;
            }
            var service="0";
            var quality= $('#quality').combobox('getValue');
            var remark = $.trim(document.getElementById("remark").value);
            var su_data= $.trim(document.getElementById("su_data").value);
            var  protocol= $('#protocol').combobox('getValue');
            $.ajax({
                type: "POST",//方法类型
                dataType:'json',
                url:'/cbtconsole/supplierscoring/saveproductscord',
                data:{quality:quality,rerundays:su_data,shopId:shop_id,inven:protocol,remarks:remark},
                dataType:"json",
                success:function(data){
                    if(data.flag == 'success'){
                        topCenter("采购供应商打分成功");
                        cance1();
                        $('#easyui-datagrid').datagrid('reload');
                    }else{
                        topCenter("采购供应商打分失败");
                    }
                }
            });
        }

	</script>
</head>
<body onload="doQuery(1);;$('#dlg1').dialog('close')">
<div id="dlg1" class="easyui-dialog" title="供应商打分" data-options="modal:true" style="width:600px;height:350px;padding:10px;autoOpen:false;;closed:true;display: none;">
	<form  method="post" style="height:100%;">
		<span style="color:red">[质量]</span>:1:差   2:较差  3: 一般  4:无投诉  5: 优质
		<div style="margin-bottom:20px;margin-left:35px;">
			<input class="easyui-numberbox" name="su_data" id="su_data"  style="width:70%;"  data-options="label:'支持退货天数:'">
		</div>
		<div style="margin-bottom:20px;margin-left:35px;">
			<select class="easyui-combobox" name="protocol" id="protocol" style="width:70%;" data-options="label:'是否有库存协议:',panelHeight:'auto'">
				<option value="1">无</option>
				<option value="2">有</option>
			</select>
		</div>
		<div style="margin-bottom:20px;margin-left:35px;">
			<select class="easyui-combobox" name="quality" id="quality" style="width:70%;" data-options="label:'质量:',panelHeight:'auto'">
				<option value="0">0</option>
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
			</select>
		</div>
		<div style="margin-bottom:20px;margin-left:35px;">
			<input class="easyui-textbox" name="remark" id="remark"  style="width:70%;"  data-options="label:'评分备注:'">
		</div>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveSupplier()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance1()" style="width:80px">取消</a>
		</div>
	</form>
</div>
<div id="top_toolbar" style="padding: 5px; height: auto">
	<div>
		<form id="query_form" action="#" style="margin-left:350px;" onsubmit="return false;">
			<input class="easyui-textbox" name="orderid" id="orderid" style="width:15%;margin-top: 10px;"  data-options="label:'采购订单号:'">
			<input type="hidden" id="shopId">
			<input class="but_color" type="button" value="查询" onclick="doQuery(1)">
			<input class="but_color" type="button" value="重置" onclick="doReset()">
		</form>
		<a href="javascript:openView();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">供应商打分</a>
		<span style="color:red">【点击店铺ID跳转到店铺、商品评分信息展示列表】</span>
	</div>
</div>

<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
	<thead>
	<tr>
		<th data-options="field:'shopId',width:50,align:'center'">店铺ID</th>
		<th data-options="field:'orderdate',width:25,align:'center'">采购时间</th>
		<th data-options="field:'orderid',width:50,align:'center'">订单号</th>
		<th data-options="field:'totalprice',width:50,align:'center'">订单金额(￥)</th>
		<th data-options="field:'username',width:50,align:'center'">采购人</th>
	</tr>
	</thead>
</table>
</body>
</html>