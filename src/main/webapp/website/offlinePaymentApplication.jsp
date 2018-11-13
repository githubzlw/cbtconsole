<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.cbt.util.*"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%
	String admuserJson = Redis.hget(request.getSession().getId(),
			"admuser");
	Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,
			Admuser.class);
	int role = Integer.parseInt(adm.getRoletype());
%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>线下采购付款申请明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
	$('#startTime').datebox({  
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
	$('#endTime').datebox({  
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
	setDatagrid();
	var opts = $("#easyui-datagrid").datagrid("options");
	opts.url = "/cbtconsole/StatisticalReport/getOfflinePaymentApplication";
	$("#data").combobox({
		onChange: function (n,o) {
			getZfuDate();
		}
	});
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '线下采购付款申请明细',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
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
	var state=$("#state").val();
	var startTime=$("#startTime").val();
	var endTime=$("#endTime").val();
	 var userName=$('#userName').combobox('getValue');
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
		"state":state,
		"userName":userName,
		"startTime":startTime,
		"endTime":endTime
	});
}

function doReset(){
	$('#query_form').form('clear');
	$('#userName').combobox('setValue','请选择');
	$('#state').combobox('setValue','2');
}

 function showMessage(tip){
	 $.messager.show({
			title:'提示',
			msg:tip,
			showType:'slide',
			timeout: 1000,
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop,
				bottom:''
			}
		});
 }
 
 /**
  * 查询结果导出excel
  */
  function exportdata(){
		var state=$("#state").val();
		var startTime=$("#startTime").val();
		var endTime=$("#endTime").val();
		var userName=$('#userName').combobox('getValue');
		if(userName=="请选择"){
			userName="whj";
		}
 	 window.location.href ="/cbtconsole/StatisticalReport/exportOfflinePayment?state="+state+"&startTime="+startTime+"&endTime="+endTime+"&userName="+userName;
 }
 
 function throughReview(id){
		jQuery.ajax({
	        url:"/cbtconsole/StatisticalReport/throughReview",
	        data:{"id":id
	        	  },
	        type:"post",
	        success:function(data){
	        	if(data>0){
	        		$.messager.alert('提示','审核成功');
	        		location.reload();
	        	}else{
	        		$.messager.alert('提示','审核失败');
	        	}
	        },
	    	error:function(e){
	    		$.messager.alert('提示','审核失败');
	    	}
	    });
 }
 
 function batThroughReview(){
	    var row = $('#easyui-datagrid').datagrid('getSelections'); 
		var i = 0;  
	    var ordersArr = "";  
	    for(i;i<row.length;i++){  
	    	ordersArr += row[i].id+",";  
	    }
		if(ordersArr.length < 1){
			$.messager.alert('提示','至少选一条数据');
			return;
		}else{
			
		}
		jQuery.ajax({
	        url:"/cbtconsole/StatisticalReport/throughReview",
	        data:{"id":ordersArr
	        	  },
	        type:"post",
	        success:function(data){
	        	if(data>0){
	        		location.reload();
	        	}else{
	        		$.messager.alert('提示','审核失败');
	        	}
	        },
	    	error:function(e){
	    		$.messager.alert('提示','审核失败');
	    	}
	    });
 }

</script>
</head>
<body text="#000000" onload="doQuery(1)">
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;" style="margin-left:200px;">
				<select class="easyui-combobox" name="userName" id="userName" style="width:15%;" data-options="label:'电商采购人:',panelHeight:'auto',valueField: 'admName',
                    textField: 'admName', value:'<%=adm.getAdmName()%>',selected:true,
                    url: '/cbtconsole/warehouse/getAllBuyer',
                    method:'get'">
				</select>
				<select class="easyui-combobox" name="state" id="state" style="width:15%;" data-options="label:'state:',panelHeight:'auto'">
				<option value="2" selected>全部</option>
				<option value="1">已批准</option>
				<option value="0" >未批准</option>
				</select>
				<input class="easyui-datebox" name="startTime" id="startTime" style="width:15%;"  data-options="label:'开始日期:'">
				<input class="easyui-datebox" name="endTime" id="endTime" style="width:15%;"  data-options="label:'结束日期:'">
				<input type="hidden" id="type" value="2" name="type"/>
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<a href="javascript:exportdata();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">导出</a>
		<a href="javascript:batThroughReview();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">批量通过</a>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1500px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'userid',width:40,align:'center'">用户ID</th>
				<th data-options="field:'orderid',width:55,align:'center'">订单号</th>
				<th data-options="field:'goodsid',width:40,align:'center'">商品编号</th>
				<th data-options="field:'car_type',width:40,align:'center'">商品规格</th>
				<th data-options="field:'car_img',width:55,align:'center'">商品图片</th>
				<th data-options="field:'buycount',width:20,align:'center'">采购数量</th>
				<th data-options="field:'goods_p_price',width:20,align:'center'">采购价格</th>
				<th data-options="field:'amount',width:20,align:'center'">采购总价</th>
				<th data-options="field:'createtime',width:45,align:'center'">申请时间</th>
				<th data-options="field:'applicantName',width:25,align:'center'">申请人</th>
				<th data-options="field:'off_remark',width:50,align:'center'">备注</th>
				<th data-options="field:'paydata',width:30,align:'center'">处理时间</th>
				<th data-options="field:'admName',width:25,align:'center'">处理人</th>
				<th data-options="field:'flag',width:25,align:'center'">状态</th>
				<th data-options="field:'operation',width:30,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>