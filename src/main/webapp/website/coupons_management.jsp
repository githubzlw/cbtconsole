<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<title>优惠券管理</title>
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
<script type="text/javascript">
$(function(){
	setDatagrid();
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '优惠券管理',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/StatisticalReport/searchCoupusManagement',//url调用Action方法
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
	var coupons_name = $("#coupons_name").val();
	var batch=$("#batch").val();
	var promo_code=$("#promo_code").val();
	var userid=$("#userid").val();
	var coupons_type = $("#coupons_type").val();
	var disbursement = $("#disbursement").val();
	var startdate=$("#startdate").val();
	var enddate=$("#enddate").val();
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
    	"coupons_name":coupons_name,
    	"batch":batch,
    	"coupons_type":coupons_type,
    	"disbursement":disbursement,
    	"userid":userid,
    	"promo_code":promo_code,
    	"enddate":enddate,
    	"startdate":startdate
	});
}

function doReset(){
	$("#coupons_name").val("");
	$("#batch").val("");
	$("#promo_code").val("");
	$("#userid").val("");
	$("#coupons_type").val("");
	$("#disbursement").val("");
	$("#startdate").val("");
	$("#enddate").val("");
}

function addCoupons() {
// 	$('#win').window('open');
	$('#dlg').dialog('open');
}
function fnUp(){
	var all_amount = $("#all_amount").val();
	window.location.href = "/cbtconsole/website/open_coupons.jsp?all_amount="+all_amount;
}
</script>
</head>
<body text="#000000" onload="$('#dlg').dialog('close')">
	<div id="dlg" class="easyui-dialog" title="创建优惠券" data-options="modal:true" style="width:400px;height:200px;padding:10px;">
			<div style="margin-bottom:20px">
				<input class="easyui-numberbox" name="all_amount" id="all_amount" onkeypress="if (event.keyCode == 13) fnUp();" style="width:85%;"  data-options="label:'总金额($):'">
			</div>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="fnUp()" style="width:80px">开始创建</a>
		</div>
	</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;">
				优惠券名称：<input id="coupons_name" type="text" value="" onkeypress="if (event.keyCode == 13) doQuery(1)"/> 
				批次：<input id="batch" type="text" value="" onkeypress="if (event.keyCode == 13) doQuery(1)"/>
				优惠码：<input id="promo_code" type="text" value="" onkeypress="if (event.keyCode == 13) doQuery(1)"/> 
				用户ID：<input id="userid" type="text" value="" onkeypress="if (event.keyCode == 13) doQuery(1)"/> 
				优惠券类型：<select id="coupons_type">
				 <option value="">全部</option>
		         <option value="1">限额券</option>
				 <option value="2">折扣券</option>
				 <option value="3">运费抵用券</option>
				</select> 发放方式：<select id="disbursement">
				 <option value="">全部</option>
		         <option value="1">新用户发放</option>
				 <option value="2">用户自领</option>
				 <option value="3">下单奖励</option>
				 <option value="4">运营赠送</option>
				</select> 创建时间： <input id="startdate"
							name="startdate" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})"
							value="${param.startdate}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />-<input id="enddate"
							name="enddate" readonly="readonly"
							onfocus="WdatePicker({isShowWeek:true})"
							value="${param.startdate}"
							onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' /> 
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<a href="javascript:allEnable('1');" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">启用</a>
		<a href="javascript:allEnable('2');" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">停用</a>
		<a href="javascript:addCoupons();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">创建优惠券</a>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
                <th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'batch',width:30,align:'center'">批次</th>
				<th data-options="field:'coupons_name',width:50,align:'center'">优惠券名称</th>
				<th data-options="field:'coupons_type',width:30,align:'center'">类型</th>
				<th data-options="field:'denomination',width:30,align:'center'">面额/折扣</th>
				<th data-options="field:'total_circulation',width:30,align:'center'">总发行量</th>
				<th data-options="field:'total_amount',width:30,align:'center'">总金额</th>
				<th data-options="field:'minimum_cons',width:30,align:'center'">最低消费</th>
				<th data-options="field:'most_favorable',width:30,align:'center'">最多优惠</th>
				<th data-options="field:'validity_day',width:100,align:'center'">有效期</th>
				<th data-options="field:'using_range',width:30">使用范围</th>
				<th data-options="field:'disbursement',width:30,align:'center'">发放方式</th>
				<th data-options="field:'is_enable',width:30,align:'center'">状态</th>
				<th data-options="field:'createtime',width:50,align:'center'">创建日期</th>
				<th data-options="field:'create_adm',width:30,align:'center'">创建人</th>
				<th data-options="field:'operation',width:50,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
</body>
<script type="text/javascript">

//全选
function selChecked(){
	var a=document.getElementById("allSelcheckid").checked;  
	var ele=document.getElementsByName("inputPrintid");
	for(i=0;i<ele.length;i++){
		ele[i].checked=a;
	}
}

  
  function closeConpons(){
	    var rfddd = document.getElementById("insertInfo");
		rfddd.style.display = "none";
		$("#all_amount").val(0);
  }
$('#reset').click(function(){
	$("#coupons_name").val("");
	$("#batch").val("");
	$("#promo_code").val("");
	$("#coupons_type").val("");
	$("#disbursement").val("");
	$("#startdate").val("");
	$("#enddate").val("");
	$("#userid").val("");
});


function enable(id){
	jQuery.ajax({
        url:"/cbtconsole/StatisticalReport/enable",
        data:{"id":id
        	  },
        type:"post",
        async : false,
        success:function(data){
        	if(data>0){
        		$.messager.alert('提示','启用成功');
        	}else{
        		$.messager.alert('提示','启用失败');
        	}
        },
    	error:function(e){
    		$.messager.alert('提示','启用失败');
    	}
    });
	location.reload();
}

/**
 * 批量启用/停用优惠券
 */
function allEnable(type){
	var row = $('#easyui-datagrid').datagrid('getSelections'); 
	var i = 0;  
    var ordersArr = "";  
    for(i;i<row.length;i++){  
    	ordersArr += row[i].id+",";  
    }
	if(ordersArr.length < 1){
		$.messager.alert('提示','至少选一条数据');
		return;
	}
	jQuery.ajax({
        url:"/cbtconsole/StatisticalReport/Allenable",
        data:{"ordersArr":ordersArr,
        	"type":type
        	  },
        type:"post",
        success:function(data){
        	if(data>0){
        		$.messager.alert('提示','批量成功');
        		location.reload();
        	}else{
        		$.messager.alert('提示','批量失败');
        	}
        },
    	error:function(e){
    		$.messager.alert('提示','批量失败');
    	}
    });
}


function view(id,coupons_type,denomination,minimum_cons,disbursement,coupons_name){
	window.location = "/cbtconsole/website/coupons_details.jsp?id="+id+"&coupons_type="+coupons_type+"&denomination="+denomination+"&minimum_cons="+minimum_cons+"&disbursement="+disbursement+"&coupons_name="+coupons_name;
}




</script>
</html>