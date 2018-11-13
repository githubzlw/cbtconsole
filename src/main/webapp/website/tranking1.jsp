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
<title>物流反馈信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
var searchReport = "/cbtconsole/StatisticalReport/searchCoupusManagement"; //报表查询
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
	//取出当前登录用户
   String sessionId = request.getSession().getId();
    String userJson = Redis.hget(sessionId, "admuser");
	Admuser adm =(Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class); 
	int userid=adm.getId();
	%>
<script type="text/javascript">
$(function(){
// 	document.onkeydown = function(e){
// 		var ev = document.all ? window.event : e; 
// 		if(ev.keyCode==13) {
// 			 var number=$('#easyui-datagrid').datagrid('options').pageNumber;
// 			 doQuery(number);
// 		}
// 	} 
	setDatagrid();
	$('#data').datebox({  
		 closeText:'关闭',  
		 formatter:function(date){  
		  var y = date.getFullYear();  
		  var m = date.getMonth()+1;  
		  var d = date.getDate();  
		  var h = date.getHours();  
		  var M = date.getMinutes();  
		  var s = date.getSeconds();  
		  function formatNumber(value){  
		   return (value < 10 ? '0' : '') + value;  
		  }  
		  return y+'-'+m+'-'+d;  
		 },  
		 parser:function(s){  
		  var t = Date.parse(s);  
		  if (!isNaN(t)){  
		   return new Date(t);  
		  } else {  
		   return new Date();  
		  }  
		 }  
		});
	$('#dlg').dialog('close');
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '物流反馈信息',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 40,//默认选择的分页是每页20行数据
			pageList : [ 40],//可以选择的分页集合
			nowrap : false,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/trackingController/showAllTrackInfo',//url调用Action方法
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
	var readFlag=$('#readFlag').combobox('getValue');
	var transportcompany=$('#transportcompany').combobox('getValue');
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
    	"transportcompany":transportcompany,
    	"readFlag":readFlag
	});
}



// 客户消息回复
function updateReply(){
	 var expressno= $("#expressno").val();
	 var replyContent= $("#replyContent1").val();
	 var id=$("#id").val();
	 if(replyContent==null||replyContent==""){
		 $.messager.alert('提示','请输入回复内容');
		 return false;
	 }
	 $.ajax({
		   type:'post',
		   data:{bz:replyContent,id:id,expressno:expressno},
		   url:"/cbtconsole/trackingController/updateBz",
		   dataType:'json',
		   success:function(res){
			   if(res=="success"){
				   $.messager.alert('提示','操作成功');
				   setTimeout(function(){
		    	  		doQuery(number);
		    	 		}, 2000)
			   }else{
				   $.messager.alert('提示','操作失败');
			   }
		   }
	   }) 
}

function doReset(){
	$('#query_form').form('clear');
	$('#status').combobox('setValue','');
	$('#transportcompany').combobox('setValue','');
}

//回复留言问题
function reply(order_no,id){
	$('#dlg').dialog('open');
	$("#replyContent1").textbox('setValue','');
	$('#replyContent1').textbox('textbox').focus();
	$("#expressno").val(order_no);
	$("#id").val(id);

};
</script>
</head>
<body onload="$('#dlg').dialog('close');doQuery(1)">
	<div id="dlg" class="easyui-dialog" title="新增备注" data-options="modal:true" style="width:400px;height:200px;padding:10px;">
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" style="width:100%;height:60px" name="replyContent1" id="replyContent1" data-options="multiline:true">
				<input type="hidden" id="expressno"/>
				<input type="hidden" id="id"/>
			</div>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateReply()" style="width:80px">增加备注</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg').dialog('close');" style="width:80px">关闭</a>
		</div>
	</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" style="margin-left:350px;" onsubmit="return false;">
				<select class="easyui-combobox" name="readFlag" id="readFlag" style="width:15%;" data-options="label:'读取状态:',panelHeight:'auto'">
				<option value="" selected>所有</option>
				<option value="N">未读</option>
				<option value="Y">已读</option>
				</select>
				<select class="easyui-combobox" name="transportcompany" id="transportcompany" style="width:15%;height: 30px;" data-options="label:'运输方式:',panelHeight:'auto'">
				<option value="" selected>所有</option>
				<option value="emsinten">邮政</option>
				<option value="JCEX">JCEX</option>
				<option value="yfh">原飞航</option>
				</select>
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'order_no',width:25,align:'center'">运单号</th>
				<th data-options="field:'link_url',width:40,align:'center'">运输方式</th>
				<th data-options="field:'send_content',width:200,align:'center'">延迟信息</th>
				<th data-options="field:'is_read',width:15,align:'center'">状态</th>
				<th data-options="field:'reservation3',width:40,align:'center'">已备注信息</th>
				<th data-options="field:'operation',width:30,align:'center'">新增备注</th>
			</tr>
		</thead>
	</table>
</body>
</html>