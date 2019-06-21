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
<title>退货管理</title>
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
String orderid=request.getParameter("orderid");
%>
<script type="text/javascript">
$(function(){
	document.onkeydown = function(e){
		var ev = document.all ? window.event : e; 
		if(ev.keyCode==13) {
			doQuery(1);
		}
	}
	setDatagrid();
	var opts = $("#easyui-datagrid").datagrid("options");
	opts.url = "/cbtconsole/Look/LookReturnOrder?mid=0";
	orderid = '<%=orderid%>';
	if(orderid!==null && orderid!="null"){
		$("#orderid").textbox('setValue',orderid);
		doQuery(1);
	}
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '退货管理',
			align : 'center',
			width : 'auto',
			height : 'auto',
			fit : true,
			pageSize : 20,
			pageList : [ 20],
			nowrap : false,//列的内容超出所定义的列宽时,false为自动换行wewr
			striped : true,//设置为true将交替显示行背景。
			toolbar : "#top_toolbar",
			url : '',
			loadMsg : '数据装载中......',
			singleSelect : false,
			fitColumns : true,
			idField:'itemid',
			style : {
	            padding : '8 8 10 8'
	        },
			pagination : true,
			rownumbers : true
		});
	}
	
function doQuery(page) {
	var location_type= $('#location_type').combobox('getValue');
	var optTimeStart = $("#optTimeStart").val();
	var optTimeEnd = $("#optTimeEnd").val();
    var shipno = $("#shipno").val();
    var order = $("#order").val();
     var state = $('#state option:selected') .val();
	$("#easyui-datagrid").datagrid("load", {
		location_type : location_type,
		optTimeStart:optTimeStart,
		page:page,
		optTimeEnd:optTimeEnd,
		shipno:shipno,
        order:order,
		state:state
	});
}

function doReset(){
	$('#location_type').combobox('setValue','-1');
	$("#optTimeStart").val('');
    $("#shipno").textbox('setValue','');
    $("#order").textbox('setValue','');
	$("#optTimeEnd").val('');
	$("#state").textbox('setValue','-2');
}

function resetLocation(barcode,short_term){
	   $.ajax({
			type:"post", 
			url:"/cbtconsole/Look/LookReturnOrder?mid=0",
			dataType:"text",                                                      
			data:{barcode : barcode,short_term:short_term}, 
			success : function(data){
                $('#easyui-datagrid').datagrid('reload');
			}
		});
}
function confirm(ship){
	   $.ajax({
			type:"post", 
			url:"/cbtconsole/Look/SetReturnOrder",
			dataType:"text",                                                      
			data:{ship : ship}, 
			success : function(data){
             $('#easyui-datagrid').datagrid('reload');
			}
		});
}
// function upState(ship ) {
// 	 $.messager.prompt('退款金额换货输入0','请输入金额',function(number){
// 				  if(number==null || number==""){
// 					 $.messager.alert('提示','退货金额不为空换货输入0');
// 					 return;
// 				  }
// 				  $.post("/cbtconsole/Look/SetReturnOrder", {
// 						ship:ship,number:number
// 					}, function(res) {
// 						if(res.rows == 0){
// 							$.messager.alert('提示','修改成功');
// 						}else{
// 							$.messager.alert('提示','修改失败');
//
// 						}
//                       doQuery(1)
// 					});
// 		});
// }

function upState(ship, that) {
    // var that = this;
    $.messager.prompt('退货金额差异原因','请输入原因',function(number){
        if(number==null || number==""){
            $.messager.alert('提示','请输入原因');
            return;
        }
        $.post("/cbtconsole/Look/SetReturnOrder", {
            ship:ship,number:number
        }, function(res) {
            if(res.rows == 0){
                $.messager.alert('提示','修改成功');
                $(that).after('<br>差异原因：'+ number);
                // $("#gid"+page).prop('lastChild').nodeValue ="差异原因:"+ number;
                $($(that)).css('display', 'none');
            }else{
                $.messager.alert('提示','修改失败');

            }

        });
    });
}
function UpMoney(ship,page) {
    $.messager.prompt('采购价格','请输入修改后价格',function(number){
        if(number==null || number==""){
            $.messager.alert('提示','请输入修改后价格');
            return;
        }
        $.post("/cbtconsole/Look/SetUpMoney", {
            ship:ship,number:number
        }, function(res) {
            if(res.rows == 0){
                $.messager.alert('提示','修改成功');
                $("#pid"+page).prop('lastChild').nodeValue ="采购录入金额:"+ number;
            }else{
                $.messager.alert('提示','修改失败');

            }

        });
    });
}

function UpShipH(ship ) {
	 $.messager.prompt('退货运单号','请输入退货运单号',function(number){
				  if(number==null || number==""){
					 $.messager.alert('提示','请输入退货运单号');
					 return;
				  }
				  $.post("/cbtconsole/Look/UpdateReturnOrder", {
						ship:ship,number:number,mid:0
					}, function(res) {
						//window.location.reload();
						 if(res.rows == 0){
							$.messager.alert('提示','修改成功');
                             doQuery(1);
						}else{
							$.messager.alert('提示','修改失败');
							
						}
					});
		});
}
function UpShip(ship ) {
	 $.messager.prompt('换货运单号','请输入换货运单号',function(number){
				  if(number==null || number==""){
					 $.messager.alert('提示','请输入要换货运单号');
					 return;
				  }
				  $.post("/cbtconsole/Look/UpdateReturnOrder", {
						ship:ship,number:number,mid:1
					}, function(res) {
						//window.location.reload();
						 if(res.rows == 0){
							$.messager.alert('提示','修改成功');
                             doQuery(1);
						}else{
							$.messager.alert('提示','修改失败');
						}
					});
		});
}
function blurs(){
	var value=$("#barcode").val();
	if (value ==''){
		$("#barcode").textbox('setValue','如:GS271');
	}
}
function focus(){
	var value=$("#barcode").val();
	if (value =='如:GS271'){
		$("#barcode").textbox('setValue','');
	}
}
function returnOr(cusorder){

	window.location.href ="/cbtconsole/AddReturnOrder/FindReturnOrder/"+cusorder;

}
function formatImgurl(val, row, index) {
        return '<a  href="'+val+'"><img src="'+val+'" class="img_sty"/></a>'

}
function Agreed(ship){
    document.getElementById('field＿name').value=ship;
    $('#user_remark').window('open');
}
function returnApply(page){
     var ch=$("input[name='radioname']:checked").val();

    var ship= $('#field＿name').val();
    var money=$('#money').val();
    var url ="/cbtconsole/Look/UpdaeReturnOrder";
    var data = {
        "ship":ship,"ch":ch,money:money
    }
    $.ajax(
        {
            type:"get",
            url:url,
            data:data,
            async:false,
            cache:false,
            success : function(res){
                //window.location.reload();
                if(res.rows == 0){
                   // $.messager.alert('提示','操作成功');
                }else{
                    $.messager.alert('提示','操作失败');
                }
                $('#user_remark').window('close');
                doQuery(1);
            }

        }

    );
}
function rejected(ship) {
    document.getElementById('fieldName').value=ship;
    $('#order_remark').window('open');

}
function rejectedok(page) {
    var ship= $('#fieldName').val();
    var cusorder= $('#cusorder').val();

    $.ajax({
        type: "post",
        url: "/cbtconsole/Look/RemReturnOrder",
        data: {ship: ship,cusorder:cusorder},
        success: function (res) {

            if (res.message == 0) {
               // $.messager.alert('提示', '操作成功');
            } else {
                $.messager.alert('提示', '操作失败');
                //window.location.reload();
            }
            $('#order_remark').window('close');
            doQuery(page);
        }
    });
}
</script>
</head>
<body text="#000000" onload="doQuery(this.page);">
<div id="user_remark2" class="easyui-window" title="信息修改"
	 data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"
	 style="width:400px;height:auto;display: none;font-size: 16px;">
	<div id="sediv3" style="margin-left:20px;">
		<div>
			订单状态：
			运单号：<input id="SetShipno" type="text">
			<br/>采购录入金额：<input id="money4" type="text">
		</div>
		<div style="margin:20px 0 20px 40px;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
			   onclick="returnApply(this.page)" style="width:80px" >确认</a>
		</div>
	</div>
</div>

<div id="user_remark" class="easyui-window" title="退货申请"
	 data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"
	 style="width:400px;height:auto;display: none;font-size: 16px;">
	<div id="sediv" style="margin-left:20px;">
		<div>
			<input type="hidden" name="field＿name" id="field＿name" value="value">
			是否退钱：
			<input type='radio' size='5' name='radioname' value='0' id='c' />是
			<input type='radio' size='5' name='radioname' value='1' id='c' />否
			<br/>退款金额：<input id="money" type="text">
		</div>
	<div style="margin:20px 0 20px 40px;">
		<a href="javascript:void(0)" class="easyui-linkbutton"
		   onclick="returnApply(this.page)" style="width:80px" >确认</a>
	</div>
	</div>
</div>
<div id="order_remark" class="easyui-window" title="退货申请"
	 data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"
	 style="width:400px;height:auto;display: none;font-size: 16px;">
	<div id="sediv_123" style="margin-left:20px;">
		<div>
			<input type="hidden" name="field＿name" id="fieldName" value="value">
			<div>驳回原因：<input id="cusorder" value='' ></div>
		</div>
		<div style="margin:20px 0 20px 40px;">
			<a href="javascript:void(0)" class="easyui-linkbutton"
			   onclick="rejectedok(this.page)" style="width:80px" >确认</a>
		</div>
	</div>
</div>
<div align="center">

	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;">
				<select class="easyui-combobox" name="location_type" id="location_type" style="width:15%;" data-options="label:'采销人员:',panelHeight:'auto',valueField: 'id',   
                    textField: 'admName', value:'',selected:true,
                    url: '/cbtconsole/warehouse/getAllBuyer',  
                    method:'get'">
				</select>
				<input class="easyui-textbox" name="shipno" id="shipno" style="width:15%;" onkeypress="if (event.keyCode == 13) doQuery(1)"  data-options="label:'运单号:'">
				<input class="easyui-textbox" name="order" id="order" style="width:15%;" onkeypress="if (event.keyCode == 13) doQuery(1)"  data-options="label:'客户订单号:'">
				<%--<input class="easyui-textbox" value="列:2019-01-17" name="optTimeStart" id="optTimeStart" style="width:15%;" onkeypress="if (event.keyCode == 13) doQuery(1)"  data-options="label:'发起时间:'">--%>
				<%--<input class="easyui-textbox" value="列:2019-01-17" name="optTimeEnd" id="optTimeEnd" style="width:15%;"  data-options="label:'结束时间:',events:{blur:blurs,focus:focus},">--%>
				时间：<input id="optTimeStart" class="Wdate"
					   style="width: 110px; height: 24px" type="text" value=""
					   onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
				<span>&nbsp;-&nbsp;</span><input id="optTimeEnd" class="Wdate"
												 style="width: 110px; height: 24px;" type="text" value=""
												 onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
				</span>&nbsp;&nbsp;&nbsp;&nbsp;
				退货状态：<select class="easyui-combobox" id="state" >
					<option value ="-2">全部</option>
					<option value ="-1">同意/驳回</option>
					<option value ="0">未处理</option>
					<option value="1">已处理</option>
					<option value="2">待确定</option>
					<option value="3">换货/线下补发</option>
					<option value="4">到账完结</option>
					<option value="5">驳回完结</option>
				</select>&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="but_color" type="button" value="查询" onclick="doQuery(1)">
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		
	</div>
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'customerorder',width:25,align:'center'">客户订单</th>
				<th data-options="field:'sellerpeo',width:30,align:'center'">1688卖家信息</th>
				<th data-options="field:'orderInfo',width:50,align:'center'">1688订单信息</th>
				<th data-options="field:'imgurl',width:50,align:'center',formatter:formatImgurl">1688订单信息</th>

				<th data-options="field:'item',width:50,align:'center'">商品详情</th>
				<th data-options="field:'returnReason',width:40,halign:'center'">退货原因</th>
				<th data-options="field:'applyTime',width:30,halign:'center'">申请时间</th>
				<th data-options="field:'pepoInfo',width:20,align:'center'">申请人员</th>
				<th data-options="field:'optTime',width:30,align:'center'">退货时间</th>
				<th data-options="field:'optUser',width:20,align:'center'">执行人员</th>
				<th data-options="field:'shipno',width:30,align:'center'">退单运单号</th>
				<%--<th data-options="field:'changeShipno',width:30,align:'center'">换产品运单号</th>--%>
				<th data-options="field:'stateShow',width:50,align:'center'">操作</th>
				
				
			</tr>
		</thead>
	</table>
</div>
</body>
</html>