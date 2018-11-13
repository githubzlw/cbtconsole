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
<title>人为编辑过的产品销售业绩</title>
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
<script src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
<script type="text/javascript">
</script>
<style type="text/css">
.displaynone{display:none;}
.item_box{display:inline-block;margin-right:52px;}
.item_box select{width:150px;}
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
.mod_pay3 {
	width: 400px;
	height:400px;
	position: fixed;
	top: 100px;
	right: 15%;
	margin-left:400px;
	z-index: 1011;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 1011;
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

function initData(){
    setTimeout(
        function() {
            var adminId=$('#adminId').combobox('getValue');
            var goodsPid=$("#goodsPid").val();
            var updatetime=$('#updatetime').combobox('getValue');
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getSalesPerformanceData",
                data:{state:2,admuserid:adminId,goodsPid:goodsPid,updatetime:updatetime},
                dataType:"text",
                success : function(data){
                    if(data != '0'){
                        var obj = eval("("+data+")");
                        if(obj.ordernonum == null || obj.ordernonum == ""){
                            $("#data2").html(0);
                        }else{
                            $("#data2").html(obj.ordernonum);
                        }
                    }
                }
            });
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getSalesPerformanceData",
                data:{state:3,admuserid:adminId,goodsPid:goodsPid,updatetime:updatetime},
                dataType:"text",
                success : function(data){
                    if(data != '0'){
                        var obj = eval("("+data+")");
                        if(obj.ordernonum == null || obj.ordernonum == ""){
                            $("#data3").html(0);
                        }else{
                            $("#data3").html(obj.ordernonum);
                        }
                    }
                }
            });
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getSalesPerformanceData",
                data:{state:4,admuserid:adminId,goodsPid:goodsPid,updatetime:updatetime},
                dataType:"text",
                success : function(data){
                    if(data != '0'){
                        var obj = eval("("+data+")");
                        if(obj.ordernonum == null || obj.ordernonum == ""){
                            $("#data4").html(0);
                        }else{
                            $("#data4").html(obj.ordernonum);
                        }
                    }
                }
            });
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getSalesPerformanceData",
                data:{state:5,admuserid:adminId,goodsPid:goodsPid,updatetime:updatetime},
                dataType:"text",
                success : function(data){
                    if(data != '0'){
                        var obj = eval("("+data+")");
                        if(obj.ordernonum == null || obj.ordernonum == ""){
                            $("#data5").html(0);
						}else{
                            $("#data5").html(obj.ordernonum);
						}
                    }
                }
            });
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getSalesPerformanceData",
                data:{state:6,admuserid:adminId,goodsPid:goodsPid,updatetime:updatetime},
                dataType:"text",
                success : function(data){
                    if(data != '0'){
                        var obj = eval("("+data+")");
                        if(obj.ordernonum == null || obj.ordernonum == ""){
                            $("#data6").html(0);
                        }else{
                            $("#data6").html(obj.ordernonum);
                        }
                    }
                }
            });
            $('#easyui-datagrid').datagrid({"onLoadSuccess":function(data){
                    $("#data7").html(data.total);
                }});
        }, 1000)
}



function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '人为编辑过的产品销售业绩',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20],//可以选择的分页集合
			nowrap : false,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/warehouse/salesPerformance',//url调用Action方法
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
    // var goodsPid=$("#goodsPid").val();
    var adminId=$('#adminId').combobox('getValue');
    var updatetime=$('#updatetime').combobox('getValue');
	$("#easyui-datagrid").datagrid("load", {
        "page":page,
        "goodsPid":"",
		"adminId":adminId,
        "updatetime":updatetime
	});
    // initData();
}

function doReset(){
    // $("#goodsPid").val("");
    $('#adminId').combobox('setValue','0');
    $('#updatetime').combobox('setValue','0');
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




</script>
</head>
<body text="#000000">
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<%--<span style="font-size:25px;font-weight:bold;margin-left: 250px;">产品页面被打开次数:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="data2" style="font-size:20px;width:35px;margin-right:100px">0</span>--%>
		<%--<span style="font-size:25px;font-weight:bold">产品被加购物车次数:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="data3" style="font-size:20px;width:35px;margin-right:100px">0</span>--%>
		<%--<span style="font-size:25px;font-weight:bold">产品被购买次数:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="data4" style="font-size:20px;width:35px;margin-right:100px">0</span><br>--%>
		<%--<span style="font-size:25px;font-weight:bold;margin-left: 400px;">产生销售额(￥):</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="data5" style="font-size:20px;width:130px;margin-right:100px">0</span>--%>
		<%--<span style="font-size:25px;font-weight:bold">产品被取消次数:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="data6" style="font-size:20px;width:130px;margin-right:100px">0</span>--%>
		<%--<span style="font-size:25px;font-weight:bold">截止到今天人为编辑过的页面总数:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="data7" style="font-size:20px;width:130px;margin-right:100px">0</span>--%>
		<div>
			<form id="query_form" action="#" onsubmit="return false;">
				<select class="easyui-combobox" name="adminId" id="adminId" style="width:12%;" data-options="label:'负责人:',panelHeight:'400px',valueField: 'id',
                    textField: 'barcode', value:'0',selected:true,
                    url: '/cbtconsole/StatisticalReport/getAllUser',
                    method:'get'">
				</select>
				<select class="easyui-combobox" name="updatetime" id="updatetime" style="width:20%;" data-options="label:'编辑时间:',panelHeight:'auto'">
					<option value="0" selected>全部</option>
					<option value="1">最近1天</option>
					<option value="7">最近7天</option>
					<option value="30">最近30天</option>
				</select>
				<%--<input class="easyui-textbox" name="goodsPid" id="goodsPid"  style="width:300px"  data-options="label:'商品pid:'">--%>
				<input class="but_color" type="button" value="查询" onclick="doQuery(1)">
				<input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'admName',width:'100px',align:'center'">负责人</th>
				<th data-options="field:'editCount',width:'150px',align:'center'">截止到今天人为编辑过的产品数</th>
				<th data-options="field:'openCount',width:'100px',align:'center'">产品页面被打开次数</th>
				<th data-options="field:'addCount',width:'100px',align:'center'">产品被加购物车次数</th>
				<th data-options="field:'buyCount',width:'100px',align:'center'">产品被购买次数</th>
				<th data-options="field:'saleCount',width:'100px',align:'center'">产生销售额(￥)</th>
				<th data-options="field:'cancelCount',width:'60px',align:'center'">产品被取消次数</th>
			</tr>
		</thead>
	</table>
	<script type="text/javascript">
        // initData();
	</script>
</body>
</html>