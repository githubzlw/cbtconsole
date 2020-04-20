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
<title>搜索词优先类别管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<!--  2018/05/17 18:37 ly
  jsp页面引入了多个jq文件，导致冲突，注释了一个
 <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
 --><script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
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

<script type="text/javascript">
$(function(){
    // document.onkeydown = function(e){
    //     var ev = document.all ? window.event : e;
    //     if(ev.keyCode==13) {
    //         doQuery(1);
    //     }
    // }
	setDatagrid();
	var opts = $("#easyui-datagrid").datagrid("options");
	opts.url = "/cbtconsole/warehouse/getPriorityCategory";
    $('#dlg1').dialog('close');
    $('#dlg2').dialog('close');
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '搜索词优先类别管理',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 40,//默认选择的分页是每页20行数据
			pageList : [ 40],//可以选择的分页集合
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
function updatePrice(type,id){
    var minPrice=$("#"+id+"_min").val();
    if(isNaN(minPrice)){
        alert("最低价格只能是数字");
        return;
	}
    $.ajax({
        type:"post",
        url:"/cbtconsole/warehouse/updateCatePrice",
        data:{id:id,minPrice:minPrice,type:type},
        dataType:"text",
        async:true,
        success : function(data){
            console.log(data);
            if(data >0){
                $('#easyui-datagrid').datagrid('reload');
            }else{
                topCenter("更新失败");
            }
        }
    });
}
function updateAntikey(id,antiId,keyword){
    var antikey=$("#"+id+"_anti").val();
    $.ajax({
        type:"post",
        url:"/cbtconsole/warehouse/updateAntikey",
        data:{antiId:antiId,keyword:keyword,antikey:antikey},
        dataType:"text",
        async:true,
        success : function(data){
            console.log(data);
            if(data >0){
                $('#easyui-datagrid').datagrid('reload');
            }else{
                topCenter("更新失败");
            }
        }
    });
}
	
function doQuery(page) {
    var keyword = $.trim(document.getElementById("kWord").value);
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
		"keyword":keyword
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

function updateState(id,state){
    $.ajax({
        type:"post",
        url:"/cbtconsole/warehouse/updateStateCategory",
        data:{id:id,state:state},
        dataType:"text",
        async:true,
        success : function(data){
            if(data >0){
                $('#easyui-datagrid').datagrid('reload');
            }else{
                topCenter("操作失败");
			}
        }
    });
}

function doReset(){
    $("#kWord").textbox('setValue','');
}
function cancel(){
    $('#dlg1').dialog('close');
    $('#dlg2').dialog('close');
    $("#cid").textbox('setValue','');
    $("#word").textbox('setValue','');
    $("#cateId").textbox('setValue','');
}

function openEditKeyword(id,catId,akey,antiKey){
    $('#dlg1').dialog('open');
    $("#pId").val(id);
    $("#cid").textbox('setValue',catId);
    $("#a-Key").textbox('setValue',akey);
    $("#antikey").textbox('setValue',antiKey);
}

function editKeyword(){
    var pId=$("#pId").val();
    var cid = $.trim(document.getElementById("cid").value);
    var antikey = $.trim(document.getElementById("antikey").value);
    var akey = $.trim(document.getElementById("a-Key").value);
    if(cid == null || cid == ""){
        topCenter("请输入优先类别数据");
        return;
	}
    $.ajax({
        type:"post",
        url:"/cbtconsole/warehouse/editKeyword",
        data:{id:pId,cid:cid,antikey:antikey,akey:akey},
        dataType:"text",
        async:true,
        success : function(data){
            if(data >0){
                cancel();
                $('#easyui-datagrid').datagrid('reload');
            }else{
                topCenter("操作失败");
            }
        }
    });
}

function addKeyword(){
    $('#dlg2').dialog('open');
    $("#word").textbox('setValue','');
    $("#cateId").textbox('setValue','');
}

function saveKeyWord(){
    var keyword = $.trim(document.getElementById("word").value);
    var cateId = $.trim(document.getElementById("cateId").value);
    var antiKey = $.trim(document.getElementById("antiKey").value);
    if(keyword == null || keyword == "" || cateId == null || cateId == ""){
        topCenter("请输入优先类别数据");
        return;
    }
    $.ajax({
        type:"post",
        url:"/cbtconsole/warehouse/addKeyword",
        data:{cateId:cateId,keyword:keyword,antiKey:antiKey},
        dataType:"text",
        async:true,
        success : function(data){
            if(data >0){
                cancel();
                $('#easyui-datagrid').datagrid('reload');
            }else{
                topCenter("操作失败");
            }
        }
    });
}

</script>
</head>
<body onload="doQuery(1);">
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" style="margin-left:350px;" onsubmit="return false;">
				<input class="easyui-textbox"  name="kWord" id="kWord" style="width:15%;margin-top: 10px;"  data-options="label:'搜索词:'">
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)">
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<div id="dlg1" class="easyui-dialog" title="修改关键词的优先类别" data-options="modal:true" style="width:400px;height:200px;padding:10px;autoOpen:false;;closed:true;display: none;">
			<form id="ff" method="post" style="height:100%;">
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="cid" id="cid"  style="width:70%;"  data-options="label:'优先类别:'">
					<input type="hidden" id="pId">
				</div>
					<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="antiKey" id="antikey"  style="width:70%;"  data-options="label:'反关键词:'">
					<input type="hidden" id="a-Key" class="easyui-textbox">
				</div>
				<div style="text-align:center;padding:5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="editKeyword()" style="width:80px">提交</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance1()" style="width:80px">取消</a>
				</div>
			</form>
		</div>
		<div id="dlg2" class="easyui-dialog" title="新增关键词的优先类别" data-options="modal:true" style="width:400px;height:250px;padding:10px;autoOpen:false;;closed:true;display: none;">
			<form  method="post" style="height:100%;">
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="word" id="word"  style="width:70%;"  data-options="label:'搜索词:'">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="cateId" id="cateId"  style="width:70%;"  data-options="label:'优先类别:'">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="antiKey" id="antiKey"  style="width:70%;"  data-options="label:'反关键词:'">
				</div>
				<div style="text-align:center;padding:5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="saveKeyWord()" style="width:80px">提交</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cancel()" style="width:80px">取消</a>
				</div>
			</form>
		</div>
		<a href="javascript:addKeyword();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">新增</a>
		<a href="/cbtconsole/init/flush/prority" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">刷新缓存优先类别数据</a>
		<a href="/cbtconsole/init/flush/price" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">刷新线上缓存最低价格数据</a>
		<span style="color:red">[多个优先类别用,隔开;更改完数据后需要点击刷新线上数据按钮才能生效]</span>
	</div>

		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'keyword',width:25,align:'center'">搜索词</th>
				<th data-options="field:'category',width:50,align:'center'">优先类别</th>
				<th data-options="field:'antiKey',width:50,align:'center'">反关键词
				</th>
				<th data-options="field:'enName',width:50,align:'center'">类别名称</th>
				<th data-options="field:'status',width:50,align:'center'">状态</th>
				<th data-options="field:'minPrice',width:50,align:'center'">最低价</th>
				<%--<th data-options="field:'maxPrice',width:50,align:'center'">最高价</th>--%>
				<th data-options="field:'remark',width:50,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>