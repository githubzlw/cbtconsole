<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.cbt.util.*"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>红人产品管理</title>
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
tr .td_class{width:230px;}
.td_class lable{
	float:left;
	width:120px;
}
.w_input input{width:200px;}
</style>
<script type="text/javascript">
	$(function() {
        $('#dlg2').dialog('close');
		setDatagrid();
		// var opts = $("#easyui-datagrid").datagrid("options");
		// opts.url = "/cbtconsole/warehouse/getUserInfo.do";
	})

	function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '红人产品管理',
			//iconCls : 'icon-ok',
			width : "100%",
			height : "100%",
			fit : true,//自动补全 
			autoRowWidth:false,
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20 ],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
			// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/warehouse/getRedManProduct',//url调用Action方法
			loadMsg : '数据装载中......',
			singleSelect : true,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
			//sortName : 'xh',//当数据表格初始化时以哪一列来排序
			//sortOrder : 'desc',//定义排序顺序，可以是'asc'或者'desc'（正序或者倒序）。
			pagination : true,//分页
			rownumbers : true
		//行数
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


	function doQuery(page) {
	    var qEmail=$("#qEmail").val();
	    var shipno=$("#shipno").val();
        var shipnoState=$('#shipnoState').combobox('getValue');
		$("#easyui-datagrid").datagrid("load", {
			"page" : page,
			"email":qEmail,
			"shipnoState":shipnoState,
			"shipno":shipno
		});
	}

	function doReset() {
	    $("#qEmail").val("");
        $("#shipno").textbox('setValue','');
        $('#shipnoState').combobox('setValue','0');
	}

	function saveShipno(a_id,type){
        $('#dlg2').dialog('open');
        $("#a_id").val(a_id);
        $("#type").val(type);
	}

	function insertShipno(){
	    var a_id=$("#a_id").val();
	    var type=$("#type").val();
	    var newShipno=$("#ac_shipno").val();
	    if(newShipno == null || newShipno== ""){
            topCenter("请输入发货单号");
            return false;
		}
        $.ajax({
            url: "/cbtconsole/warehouse/insertShipno",
            type:"POST",
            data : {"a_id":a_id,"type":type,"newShipno":newShipno},
            dataType:"json",
            success:function(data){
                if (data == 1) {
                    cance2();
                    $('#easyui-datagrid').datagrid('reload');
                }else{
                    topCenter("添加/修改发货单号失败");
                }
            }
        });
	}

	function cance2(){
	    $("#a_id").val("");
        $("#type").val("");
        $("#ac_shipno").textbox('setValue','');
        $('#dlg2').dialog('close');
	}
</script>
</head>
<body onload="$('#dlg2').dialog('close');">
	<div id="dlg2" class="easyui-dialog"  title="添加/修改发货单号" data-options="modal:true" style="width:400px;height:200px;padding:10px;autoOpen:false;closed:true;display: none;">
		<form id="ff2" method="post" style="height:100%;">
			<div style="margin-bottom:20px;margin-left:35px;">
				<input type="hidden" id="a_id">
				<input type="hidden" id="type">
				<input class="easyui-textbox" name="ac_shipno" id="ac_shipno"  style="width:70%;"  data-options="label:'发货单号:'">
			</div>
			<div style="text-align:center;padding:5px 0">
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="insertShipno()" style="width:80px">提交</a>
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance2()" style="width:80px">取消</a>
			</div>
		</form>
	</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;" style="margin-left:0px;">
				<input class="easyui-textbox" name="qEmail" id="qEmail" style="width:10%;margin-top: 10px;"  data-options="label:'邮箱:'">
				<input class="easyui-textbox" name="shipno" id="shipno" style="width:10%;margin-top: 10px;"  data-options="label:'发货单号:'">
				<select class="easyui-combobox" name="shipnoState" id="shipnoState" style="width:12%;" data-options="label:'发货状态:',panelHeight:'auto'">
					<option value="0" selected="selected">全部</option>
					<option value="1">未发货</option>
					<option value="2">已发货</option>
				</select>
				<input class="but_color" type="button"  value="查询" onclick="doQuery(1)">
				<input class="but_color" type="button"  value="重置" onclick="doReset()">
			</form>
		</div>
	</div>



	<table class="easyui-datagrid" id="easyui-datagrid">
		<thead>
			<tr>
				<th data-options="field:'addressEmail',width:30,align:'center'">客户邮箱</th>
				<th data-options="field:'redProduct',width:100,align:'center'">红人产品</th>
				<th data-options="field:'createTime',width:30,align:'center'">创建时间</th>
				<th data-options="field:'address',width:20,align:'center'">发货地址</th>
				<th data-options="field:'remark',width:10,align:'center'">备注</th>
				<th data-options="field:'shipno',width:10,align:'center'">发货单号</th>
			</tr>
		</thead>
	</table>
</body>
</html>