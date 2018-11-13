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
<title>有匹配订单未入库采购订单明细</title>
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
.repalyBtn{
    height: 30px;
    width: 70px;
    background: #1c9439;
    border: 0px solid #dcdcdc;
    color: #ffffff;
    cursor: pointer;
}
</style>
<script type="text/javascript">
$(function(){
	setDatagrid();
	var opts = $("#easyui-datagrid").datagrid("options");
	opts.url = "/cbtconsole/StatisticalReport/BuyOrderDetails";
	$("#data").combobox({
		onChange: function (n,o) {
			getZfuDate();
		}
	});
	$('#dlg').dialog('close');
	$('#dlg1').dialog('close');
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '有匹配订单未入库采购订单明细(单位:RMB)',
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
	var times=$("#times").val();
	 var userName=$('#userName').combobox('getValue');
	if(times==null || times==""){
		showMessage("查询错误");
		return;
	}
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
		"times":times,
		"userName":userName,
		"type":"1"
	});
}

function doReset(){
	$('#userName').combobox('setValue','请选择');
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
	  var times=$("#times").val();
	  var userName=$('#userName').combobox('getValue');
 	 window.location.href ="/cbtconsole/StatisticalReport/exportBuyOrderDetails?times="+times+"&userName="+userName+"&type=1";
 }
//备注
  function doReplay(id){
  	$('#dlg').dialog('open');
  	$("#tb_id").val(id);
  };
//添加备注
  function updateReply(){
  	 var id= $("#tb_id").val();
  	 var replyContent= $("#replyContent1").val();
  	 if(replyContent==null||replyContent==""){
  		showMessage("请输入备注信息");
  		 return false;
  	 }
  	 var params = {  
     			"id":id,
     			"replyContent":replyContent
     		};
        $.ajax({  
           url:'/cbtconsole/StatisticalReport/updateReply',  
           type:"post",  
           data:params,  
           success:function(data){
         	if(data.length< 0){
         		showMessage("添加备注失败");
   	  		}else{
   	  			$('#dlg').dialog('close');
   	  			$("#replyContent1").textbox('setValue','');
   	  			$("#tb_id").val("");
   	  			$("#"+id+"").html(data);
   	  		}
           }, 
       }); 
  }
  function inStorage(){
	  var import_orderid= $("#import_orderid").val();
	  var import_goodsid= $("#import_goodsid").val();
	  var tb_orderid=$("#tb_orderid").val();
	  var tb_shipno=$("#tb_shipno").val();
	  var tb_sku=$("#tb_sku").val();
	  var tb_qty=$("#tb_qty").val();
	  var tb_itemid=$("#tb_temid").val();
	  var params = {  
   			"import_orderid":import_orderid,
   			"import_goodsid":import_goodsid,
   			"tb_orderid":tb_orderid,
   			"tb_shipno":tb_shipno,
   			"tb_sku":tb_sku,
   			"tb_qty":tb_qty,
   			"type":"1",
   			"tb_itemid":tb_itemid
   		};
      $.ajax({  
         url:'/cbtconsole/StatisticalReport/inStorage',  
         type:"post",  
         data:params,  
         success:function(data){
       	if(data< 0){
       		showMessage("强制入库失败");
 	  		}else{
 	  			$('#dlg1').dialog('close');
 	  			$("#import_orderid").textbox('setValue','');
 	  			$("#import_goodsid").textbox('setValue','');
 	  			$("#tb_orderid").val("");
 	  		 	$("#tb_shipno").val("");
 	  		 	$("#tb_sku").val("");
 	  		 	$("#tb_qty").val("");
 	  		 	$("#tb_itemid").val("");
 	  			doQuery(1);
 	  		}
         }, 
     }); 
  }
  function openStorage(tb_orderid,tb_shipno,tb_sku,tb_qty,itemurl,tb_itemid){
	  $("#tb_orderid").val(tb_orderid);
	  $("#tb_shipno").val(tb_shipno);
	  $("#tb_sku").val(tb_sku);
	  $("#tb_qty").val(tb_qty);
	  $("#tb_itemid").val(tb_itemid);
	  $('#dlg1').dialog('open');
  }
</script>
</head>
<body text="#000000" onload="doQuery(1)">
	<div id="dlg" class="easyui-dialog" title="添加备注" data-options="modal:true" style="width:400px;height:200px;padding:10px;display: none;">
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" style="width:100%;height:60px" name="replyContent1" id="replyContent1" data-options="multiline:true">
				<input type="hidden" id="tb_id"/>
			</div>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateReply()" style="width:80px">添加备注</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg').dialog('close');" style="width:80px">关闭</a>
		</div>
	</div>
		<div id="dlg1" class="easyui-dialog" title="强制入库" data-options="modal:true" style="width:400px;height:200px;padding:10px;display: none;">
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="import_orderid" id="import_orderid" style="width:85%;"  data-options="label:'订单号:'">
				<input type="hidden" id="tb_orderid"/>
				<input type="hidden" id="tb_shipno"/>
				<input type="hidden" id="tb_sku"/>
				<input type="hidden" id="tb_qty"/>
				<input type="hidden" id="tb_itmeid"/>
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="import_goodsid" id="import_goodsid" style="width:85%;"  data-options="label:'商品号:'">
			</div>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="inStorage()" style="width:80px">强制入库</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg1').dialog('close');" style="width:80px">关闭</a>
		</div>
	</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;" style="margin-left:500px;">
				<select class="easyui-combobox" name="userName" id="userName" style="width:20%;" data-options="label:'采购人:',panelHeight:'auto',valueField: 'account',   
                    textField: 'account', value:'请选择',
                    url: '/cbtconsole/StatisticalReport/getAllBuyer',  
                    method:'get'">
				</select>
				<input type="hidden" id="times" value="${param.times}" name="times"/>
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<a href="javascript:exportdata();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">导出</a>
		<span>【${param.times}】采购订单没有入库总金额</span><span style="color:red">￥【${param.amount}】</span>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1500px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'tbOr1688',width:25,align:'center'">下单来源</th>
				<th data-options="field:'orderid',width:40,align:'center'">订单号</th>
				<th data-options="field:'orderstatus',width:40,align:'center'">订单状态</th>
				<th data-options="field:'itemname',width:55,align:'center'">商品名称</th>
				<th data-options="field:'imgurl',width:35,align:'center'">商品图片</th>
				<th data-options="field:'itemprice',width:40,align:'center'">商品单价</th>
				<th data-options="field:'itemqty',width:45,align:'center'">商品数量</th>
				<th data-options="field:'preferential',width:45,align:'center'">国内运费</th>
				<th data-options="field:'totalprice',width:30,align:'center'">订单总价</th>
				<th data-options="field:'sku',width:55,align:'center'">商品规格</th>
				<th data-options="field:'orderdate',width:55,align:'center'">订单时间</th>
				<th data-options="field:'paydata',width:30,align:'center'">支付时间</th>
				<th data-options="field:'delivery_date',width:45,align:'center'">发货时间</th>
				<th data-options="field:'username',width:45,align:'center'">采购人</th>
				<th data-options="field:'operation_remark',width:45,align:'center'">处理备注</th>
			</tr>
		</thead>
	</table>
</body>
</html>