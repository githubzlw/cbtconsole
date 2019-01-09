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
<title>用户黑名单信息管理</title>
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
		setDatagrid();
		// var opts = $("#easyui-datagrid").datagrid("options");
		// opts.url = "/cbtconsole/warehouse/getUserInfo.do";
	})

	function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '用户黑名单信息管理',
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
			url : '/cbtconsole/warehouse/getUserBackList',//url调用Action方法
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
	    var blackVlue=$("#pBlackVlue").val();
	    var flag=$("#flag").val();
        var paramType=$("#paramType").val();
		$("#easyui-datagrid").datagrid("load", {
			"page" : page,
			"blackVlue":blackVlue,
			"flag":flag,
			"paramType":paramType
		});
	}

	function doReset() {
	    $("#pBlackVlue").val("");
	    $("#flag").val("");
        $("#paramType").val("-1");
	}

	function updateFlag(id,type){
        $.ajax({
            url: "/cbtconsole/warehouse/updateFlag",
            type:"POST",
            data : {"id":id,"type":type},
            dataType:"json",
            success:function(data){
                if(data>0){
                    $('#easyui-datagrid').datagrid('reload');
				}else{
                    topCenter("操作失败");
				}
            }
        });
	}

	function updateEmail(id,email){
        $("#oldEmail").textbox('setValue',email);
        $("#id").val(id);
        $('#dlg').dialog('open');
	}

	function cance(){
        $('#dlg').dialog('close');
        $("#oldEmail").textbox('setValue',"");
        $("#newBlackVlue").textbox('setValue',"");
        $("#newType").combobox('setValue',"-1");
        $("#type").combobox('setValue',"-1");
        $("#id").val("");
        $('#dlg1').dialog('close')
        $("#blackVlue").textbox('setValue',"");
        $('#valid').combobox('setValue','-1');
	}

	function updatebackEmail(){
	    var newBlackVlue=$.trim(document.getElementById("newBlackVlue").value);
        var oldEmail=$.trim(document.getElementById("oldEmail").value);
	    var id=$("#id").val();
        var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
        var type=$('#newType').combobox('getValue');
        if(type == -1){
            topCenter("请选择黑名单类型");
            return false;
        }else if(type == 0){
            if(newBlackVlue == null || newBlackVlue == ""){
                topCenter("Please enter your email！");
                return false;
            }
            if(!reg.test(newBlackVlue)){
                topCenter("Please enter your vaild email！");
                return false;
            }
        }
        if(newBlackVlue == null || newBlackVlue == "" || newBlackVlue ==oldEmail ){
            topCenter("请输入黑名单值，且不能喝当前黑名单信息一致");
            return false;
		}
        $.ajax({
            url: "/cbtconsole/warehouse/updatebackEmail",
            type:"POST",
            data : {"id":id,"newBlackVlue":newBlackVlue,"type":type},
            dataType:"json",
            success:function(data){
                if(data>0){
                    $('#easyui-datagrid').datagrid('reload');
                    cance();
                }else{
                    topCenter("更新失败");
                }
            }
        });
	}

	function openBackUser(){
        $('#dlg1').dialog('open');
        $("#userEmail").textbox('setValue',"");
        $("#userIp").textbox('setValue',"");
	}

	function addBackUser(){
        var reg = new RegExp("^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$");
        var blackVlue=$.trim(document.getElementById("blackVlue").value);
        var type=$('#type').combobox('getValue');
        if(type == -1){
            topCenter("请选择黑名单类型");
            return false;
		}else if(type == 0){
            if(blackVlue == null || blackVlue == ""){
                topCenter("Please enter your email！");
                return false;
            }
            if(!reg.test(blackVlue)){
                topCenter("Please enter your vaild email！");
                return false;
            }
		}
        $.ajax({
            url: "/cbtconsole/warehouse/addBackUser",
            type:"POST",
            data : {"blackVlue":blackVlue,"type":type},
            dataType:"json",
            success:function(data){
                if(data>0){
                    $('#easyui-datagrid').datagrid('reload');
                    cance();
                }else{
                    topCenter("更新失败");
                }
            }
        });
	}
</script>
</head>
<body onload="$('#dlg').dialog('close');$('#dlg1').dialog('close');">
<div id="dlg" class="easyui-dialog"  title="修改黑名单" data-options="modal:true" style="width:500px;height:300px;padding:10px;autoOpen:false;closed:true;display: none;">
	<form id="ff" method="post" style="height:100%;">
		<div style="margin-bottom:20px;margin-left:35px;">
			<input class="easyui-textbox" name="oldEmail" id="oldEmail" readonly="readonly"   style="width:90%;"  data-options="label:'当前内容:'">
			<input type="hidden" id="id" name="id"/>
		</div>
		<div style="margin-bottom:20px;margin-left:35px;">
			<input class="easyui-textbox" name="newBlackVlue" id="newBlackVlue"  style="width:90%;"  data-options="label:'修改后内容:'">
		</div>
		<div style="margin-bottom:20px;margin-left:35px;">
			<select class="easyui-combobox" name="newType" id="newType" style="width:230px" data-options="label:'类型:',Height:'auto'">
				<option value="-1" selected="selected">请选择</option>
				<option value="0">邮箱黑名单</option>
				<option value="1">ip黑名单</option>
				<option value="2">城市黑名单</option>
			</select>
		</div>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updatebackEmail()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance()" style="width:80px">取消</a>
		</div>
	</form>
</div>
<div id="dlg1" class="easyui-dialog"  title="新增黑名单" data-options="modal:true" style="width:400px;height:200px;padding:10px;autoOpen:false;closed:true;display: none;">
	<form id="ff1" method="post" style="height:100%;">
		<div style="margin-bottom:20px;margin-left:35px;">
			<input class="easyui-textbox" name="blackVlue" id="blackVlue"  style="width:70%;"  data-options="label:'黑名单内容:'">
		</div>
		<div style="margin-bottom:20px;margin-left:35px;">
			<select class="easyui-combobox" name="type" id="type" style="width:230px" data-options="label:'类型:',Height:'auto'">
				<option value="-1" selected="selected">请选择</option>
				<option value="0">邮箱黑名单</option>
				<option value="1">ip黑名单</option>
				<option value="2">城市黑名单</option>
			</select>
		</div>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addBackUser()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance()" style="width:80px">取消</a>
		</div>
	</form>
</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<table style="margin:auto;">
				<tr>
					<td class="td_class"><div class="w_input">黑名单内容：<input id="pBlackVlue" type="text"
						value="" onblur="this.value=this.value.trim();"
						onkeypress="this.value=this.value.trim();if (event.keyCode == 13) doQuery(1)" /></div></td>
					<td>
						状态：<select id="flag">
							<option value="">全部</option>
							<option value="0">使用中</option>
						    <option value="1">停用</option>
						</select>
					</td>
					<td>
						类型：<select id="paramType">
						<option value="-1">全部</option>
						<option value="0">邮箱黑名单</option>
						<option value="1">ip黑名单</option>
						<option value="2">城市黑名单</option>
					</select>
					</td>
					<td>
						<input class="but_color" type="button"  value="查询" onclick="doQuery(1)">
						<input class="but_color" type="button"  value="重置" onclick="doReset()">
					</td>
				</tr>
			</table>
		</div>
		<a href="javascript:openBackUser();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">新增黑名单</a>
	</div>

	<table class="easyui-datagrid" id="easyui-datagrid">
		<thead>
			<tr>
				<th data-options="field:'blackVlue',width:40,align:'center'"></th>
				<th data-options="field:'createtime',width:40,align:'center'">创建时间</th>
				<th data-options="field:'updateTime',width:40,align:'center'">最后更新时间</th>
				<th data-options="field:'flag',width:10,align:'center'">状态</th>
				<th data-options="field:'type',width:10,align:'center'">类型</th>
				<th data-options="field:'username',width:30,align:'center'">操作人</th>
				<th data-options="field:'option',width:40,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>