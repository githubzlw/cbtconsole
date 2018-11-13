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
<title>优惠券详情列表</title>
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
<% 
  request.setCharacterEncoding("UTF-8");
  String id=request.getParameter("id");
  String coupons_type=request.getParameter("coupons_type");//优惠券类型
  coupons_type=(new String(coupons_type.getBytes("ISO-8859-1"),"UTF-8")).trim();
  String denomination=request.getParameter("denomination");//面额/折扣
  String minimum_cons=request.getParameter("minimum_cons");//最低消费
  String disbursement=request.getParameter("disbursement");//发放形式
  disbursement=(new String(disbursement.getBytes("ISO-8859-1"),"UTF-8")).trim();
  String coupons_name=request.getParameter("coupons_name");//发放形式
  coupons_name=(new String(coupons_name.getBytes("ISO-8859-1"),"UTF-8")).trim();
%>
<script type="text/javascript">
$(function(){
	var id=<%=id%>;
	$("#coupons_id").val(id);
	var coupons_type='<%=coupons_type%>';
	var denomination='<%=denomination%>';
	var minimum_cons='<%=minimum_cons%>';
	var disbursement='<%=disbursement%>';
	var coupons_name='<%=coupons_name%>';
	document.getElementById("coupons_type").innerHTML=coupons_type;
	document.getElementById("denomination").innerHTML=denomination;
	document.getElementById("minimum_cons").innerHTML=minimum_cons;
	document.getElementById("disbursement").innerHTML=disbursement;
	document.getElementById("coupons_name").innerHTML=coupons_name;
	setDatagrid();
	doQuery(1);
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '优惠券详情列表',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/StatisticalReport/searchCoupusDetails',//url调用Action方法
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
	var coupons_id=$("#coupons_id").val();
	var disbursement=$("#disbursement1").val();
	var promo_code=$("#promo_code").val();
	var coupons_type=$("#coupons_type1").val();
	var use_time=$("#use_time").val();
	var get_time=$("#get_time").val();
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
		"coupons_id":coupons_id,
  	    "coupons_type":coupons_type,
  	    "use_time":use_time,
  	    "get_time":get_time,
  	    "promo_code":promo_code,
  	    "disbursement":disbursement
	});
}

function doReset(){
	$("#promo_code").val("");
	$("#get_time").val("");
	$("#use_time").val("");
	$("#coupons_type1").val("");
	$("#disbursement1").val("");
}

</script>
</head>
<body text="#000000">
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;">
			    <input type="hidden" id="coupons_id">
			    <label for="nickname" >优惠券名称：</label><span id="coupons_type"></span>
           		<label for="nickname" >面额/折扣：</label><span id="denomination"></span>
                <label for="nickname" >最低消费：</label><span id="minimum_cons"></span>
                <label for="nickname" >发放方式：</label><span id="disbursement"></span><br>
				优惠券名称：<input id="coupons_name" type="text" value="" onkeypress="if (event.keyCode == 13) doQuery(1)"/> 
				批次：<input id="batch" type="text" value="" onkeypress="if (event.keyCode == 13) doQuery(1)"/>
				优惠码：<input id="promo_code" type="text" value="" onkeypress="if (event.keyCode == 13) doQuery(1)"/> 
				获取时间：<input id="get_time" type="text" value="" onkeypress="if (event.keyCode == 13) doQuery(1)"/> 
				使用时间：<input id="use_time" type="text" value="" onkeypress="if (event.keyCode == 13) doQuery(1)"/> 
				优惠券类型：<select id="coupons_type1">
				 <option value="">全部</option>
		         <option value="1">限额券</option>
				 <option value="2">折扣券</option>
				 <option value="3">运费抵用券</option>
				</select> 发放方式：<select id="disbursement1">
				 <option value="">全部</option>
		         <option value="1">新用户发放</option>
				 <option value="2">用户自领</option>
				 <option value="3">下单奖励</option>
				 <option value="4">运营赠送</option>
				</select> 
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<a href="javascript:allEnable('1');" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">启用</a>
		<a href="javascript:allEnable('2');" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">停用</a>
		<a href="javascript:history.back(-1)" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">返回上一页</a>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
                <th data-options="field:'ck',checkbox:true"></th>
				<th data-options="field:'batch',width:30,align:'center'">批次</th>
				<th data-options="field:'promo_code',width:50,align:'center'">优惠码</th>
				<th data-options="field:'userid',width:30,align:'center'">用户ID</th>
				<th data-options="field:'user_name',width:50,align:'center'">用户名称</th>
				<th data-options="field:'states',width:30,align:'center'">状态</th>
				<th data-options="field:'disbursement',width:30,align:'center'">获取方式</th>
				<th data-options="field:'get_time',width:30,align:'center'">获取时间</th>
				<th data-options="field:'use_time',width:30,align:'center'">使用时间</th>
				<th data-options="field:'validity_day',width:100,align:'center'">有效期</th>
				<th data-options="field:'remark',width:30,align:'center'">运营备注</th>
				<th data-options="field:'is_enable',width:30,align:'center'">是否启用</th>
				<th data-options="field:'adm_name',width:30,align:'center'">操作人</th>
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

  function BeginCreate(){
	  var all_amount=$("#all_amount").val();
	  if(all_amount==null || all_amount=="" || Number(all_amount)<=0){
		 alert('活动金额不能小于0');
		  return;
	  }
	  closeConpons();
	  $("#all_amount").val("");
	  window.location.href = "/cbtconsole/website/open_coupons.jsp?all_amount="+all_amount;
  }
  
  function closeConpons(){
	   var rfddd = document.getElementById("insertInfo");
		rfddd.style.display = "none";
		$("#all_amount").val(0);
  }
//查询报表
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
		async : false,
        url:"/cbtconsole/StatisticalReport/enableForSubsidiary",
        data:{"id":id
        	  },
        type:"post",
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
	setTimeout(function(){
		location.reload();
	}, 2000)
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
		url:"/cbtconsole/StatisticalReport/AllenableDetails",
        data:{"ordersArr":ordersArr,
        	"type":type
        	  },
        type:"post",
        success:function(data){
        	if(data>0){
        		$.messager.alert('提示','批量成功');
        	}else{
        		$.messager.alert('提示','批量失败');
        	}
        },
    	error:function(e){
    		$.messager.alert('提示','批量失败');
    	}
    });
	setTimeout(function(){
		location.reload();
	}, 3000)
}



/**
 * 查看优惠券详情明细
 */
function view(id){
	window.location = "/cbtconsole/website/coupons_details_view.jsp?id="+id;
}




</script>
</html>