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
<title>月采购对账报表</title>
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
	var opts = $("#easyui-datagrid").datagrid("options");
	opts.url = "/cbtconsole/StatisticalReport/buyReconciliationReport";
	$("#data").combobox({
		onChange: function (n,o) {
			getZfuDate();
		}
	});
	$('#ali_dlg').dialog('close');
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '月采购对账报表(单位:RMB)',
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

	function showMsg(msg){
        $.messager.show({
            title:'提示',
            msg:msg,
            showType:'slide',
            timeout: 1000,
            style:{
                right:'',
                top:document.body.scrollTop+document.documentElement.scrollTop,
                bottom:''
            }
        });
	}
	
function doQuery(page) {
	var year=$('#year').combobox('getValue');
	var month=$('#month').combobox('getValue');
	if(year=="0"){
        showMsg("请选择年份");
		return;
	}
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
		"year":year,
		"month":month
	});
}

function doReset(){
	$('#year').combobox('setValue','0');
	$('#month').combobox('setValue','0');
}

/**
 * 查询结果导出excel
 */
 function exportdata(){
	 var year=$('#year').combobox('getValue');
	 var month=$('#month').combobox('getValue');
	 window.location.href ="/cbtconsole/StatisticalReport/exportBuyReconciliation?year="+year+"&month="+month;
}
 function openLog() {
		$('#ali_dlg').dialog('open');
		getZfuDate();
}
 // 根据选择的年月获取相应的支付宝数据
 function getZfuDate(){
		$('#beginBlance').numberbox('setValue','0.00');
		$('#endBlance').numberbox('setValue','0.00');
		$('#transfer').numberbox('setValue','0.00');
		$('#payFreight').numberbox('setValue','0.00');
		$('#ebayAmount').numberbox('setValue','0.00');
		$('#materialsAmount').numberbox('setValue','0.00');
		$('#zfbFright1').numberbox('setValue','0.00');
    	$('#cancelAmount').numberbox('setValue','0.00');
	 var date=$('#data').combobox('getValue');
	 jQuery.ajax({
	        url:"/cbtconsole/StatisticalReport/getZfuDate",
	        data:{"date":date
	        	  },
	        type:"post",
	        success:function(data){
	        	if(data.data.list.length>0){
	        		 var reportDetail=data.data.list;
	        		$('#ff').form('load',{
	        			beginBlance:reportDetail[0].beginBlance,
	        			endBlance:reportDetail[0].endBlance,
	        			transfer:reportDetail[0].transfer,
	        			payFreight:reportDetail[0].payFreight,
	        			ebayAmount:reportDetail[0].ebayAmount,
	        			materialsAmount:reportDetail[0].materialsAmount,
	        			zfbFright1:reportDetail[0].zfbFright,
                        cancelAmount:reportDetail[0].cancelAmount,
            		});
	        	}else{
	        		showMessage("没有当月支付宝余额数据");
	        	}
	        },
	    	error:function(e){
	    		showMessage("获取支付宝余额失败");
	    	}
	    });
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
 // 修改/添加当月支付宝余额
 function addZfbData(){
	 var date=$('#data').combobox('getValue');
	 var beginBlance= $("#beginBlance").val();
	 var endBlance= $("#endBlance").val();
	 var transfer= $("#transfer").val();
	 var payFreight= $("#payFreight").val();
	 var ebayAmount= $("#ebayAmount").val();
	 var materialsAmount= $("#materialsAmount").val();
	 var zfbFright1= $("#zfbFright1").val();
	 var cancelAmount=$("#cancelAmount").val();
	 if(Number(beginBlance)<=0){
		 showMessage("月初余额不能小于0");
		 return;
	 }
	 if(Number(endBlance)<=0){
		 showMessage("月末余额不能小于0");
		 return;
	 }
	 if(Number(transfer)<=0){
		 showMessage("银行转账不能小于0");
		 return;
	 }
	 jQuery.ajax({
	        url:"/cbtconsole/StatisticalReport/addZfbData",
	        data:{"date":date,
	        	   "beginBlance":beginBlance,
	        	   "endBlance":endBlance,
	        	   "transfer":transfer,
	        	   "payFreight":payFreight,
	        	   "ebayAmount":ebayAmount,
	        	   "materialsAmount":materialsAmount,
	        	   "zfbFright":zfbFright1,
					"cancelAmount":cancelAmount
	        	  },
	        type:"post",
	        success:function(data){
	        	if(data.data.allCount>0){
	        		$('#ali_dlg').dialog('close');
	        		doQuery(1);
	        	}else{
	        		showMessage("修改/添加当月支付宝余额失败");
	        	}
	        },
	    	error:function(e){
	    		showMessage("修改/添加当月支付宝余额失败");
	    	}
	    });
 }
 
 function uploadExcelFile() {
      $.messager.progress({
                title: '上传Excel',
                msg: '请等待...'
            });
	 $("#multiFileForm").form('submit', {
                type: "post",  //提交方式
                url: "/cbtconsole/StatisticalReport/uploadExcelFile", //请求url
                success: function (data) {
                    $.messager.progress('close');
                    var data = eval('(' + data + ')');
                    if (data.ok) {
                        closeDialogById('#excel_dlg');
                        $("#multiFileForm")[0].reset();
                        $.messager.alert("提醒", "执行成功，请刷新界面", "info");
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function () {
                    $.messager.progress('close');
                    $.messager.alert("提醒", "上传错误，请联系管理员", "error");
                }
            });
 }

 function uploadBuyingData() {
     $('#excel_dlg').dialog('open');
 }
 
 function closeAllDialog() {
     closeDialogById('#ali_dlg');
	 closeDialogById('#excel_dlg');
 }
 
 function closeDialogById(id) {
	 $(id).dialog('close');
 }
</script>
</head>
<body text="#000000" onload="closeAllDialog()">

<div id="excel_dlg" class="easyui-dialog" title="上传Excel文件" data-options="modal:true" style="width: 380px; height: 220px; padding: 10px;">
        <form style="margin-left: 44px;" id="multiFileForm" method="post" enctype="multipart/form-data">
            <input id="file" type="file" name="file" multiple="false">
			<br>
			<span>类型:<select style="height: 28px;" name="type">
				<%--<option value="0">支付宝买入交易</option>--%>
				<option value="1">账务明细(service@import-express.com)</option>
				<option value="2">账务明细(cerong6@qq.com)</option>
			</select></span>

        </form>
        <br>
        <div style="text-align: center; padding: 5px 0">
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton"
               onclick="uploadExcelFile()" style="width: 80px">确认上传</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeDialogById('#excel_dlg')"
               style="width: 80px">关闭</a>
        </div>
    </div>

		<div id="ali_dlg" class="easyui-dialog" title="添加/修改支付宝余额" data-options="modal:true" style="width:400px;height:550px;padding:10px;">
		 <form id="ff" method="post">
				<div style="margin-bottom:20px">
					<select class="easyui-combobox" name="data" id="data" style="width:85%;" data-options="label:'年月:',panelHeight:'auto'">
					<option value="2017-01-01 00:00:00">2017-01</option>
					<option value="2017-02-01 00:00:00">2017-02</option>
					<option value="2017-03-01 00:00:00">2017-03</option>
					<option value="2017-04-01 00:00:00" selected="selected">2017-04</option>
					<option value="2017-05-01 00:00:00">2017-05</option>
					<option value="2017-06-01 00:00:00">2017-06</option>
					<option value="2017-07-01 00:00:00">2017-07</option>
					<option value="2017-08-01 00:00:00">2017-08</option>
					<option value="2017-09-01 00:00:00">2017-09</option>
					<option value="2017-10-01 00:00:00">2017-10</option>
					<option value="2017-11-01 00:00:00">2017-11</option>
					<option value="2017-12-01 00:00:00">2017-12</option>
					<option value="2018-01-01 00:00:00">2018-01</option>
					<option value="2018-02-01 00:00:00">2018-02</option>
					<option value="2018-03-01 00:00:00">2018-03</option>
					<option value="2018-04-01 00:00:00">2018-04</option>
					<option value="2018-05-01 00:00:00">2018-05</option>
					<option value="2018-06-01 00:00:00">2018-06</option>
					<option value="2018-07-01 00:00:00">2018-07</option>
					<option value="2018-08-01 00:00:00">2018-08</option>
					<option value="2018-09-01 00:00:00">2018-09</option>
					<option value="2018-10-01 00:00:00">2018-10</option>
					<option value="2018-11-01 00:00:00">2018-11</option>
					<option value="2018-12-01 00:00:00">2018-12</option>
					</select>
				</div>
				<div style="margin-bottom:20px">
					<input class="easyui-numberbox" name="beginBlance" id="beginBlance" style="width:85%;"  data-options="label:'月初余额:',precision:2">
				</div>
				<div style="margin-bottom:20px">
					<input class="easyui-numberbox"  name="endBlance" id="endBlance" style="width:85%;"  data-options="label:'月末余额:',precision:2">
				</div>
				<div style="margin-bottom:20px">
					<input class="easyui-numberbox" name="transfer" id="transfer" style="width:85%;"  data-options="label:'银行转账:',precision:2">
				</div>
				<div style="margin-bottom:20px">
					<input class="easyui-numberbox" name="ebayAmount" id="ebayAmount" style="width:85%;"  data-options="label:'ebay备用金/运费:',precision:2">
				</div>
				<div style="margin-bottom:20px">
					<input class="easyui-numberbox" name="zfbFright1" id="zfbFright1" style="width:85%;"  data-options="label:'支付宝运费:',precision:2">
				</div>
				 <div style="margin-bottom:20px">
					 <input class="easyui-numberbox" name="cancelAmount" id="cancelAmount" style="width:85%;"  data-options="label:'订单退款:',precision:2">
				 </div>
				 <div style="margin-bottom:20px">
					 <input class="easyui-numberbox" name="materialsAmount" id="materialsAmount" style="width:85%;"  data-options="label:'电商物料:',precision:2">
				 </div>
				 <div style="margin-bottom:20px">
					 <input class="easyui-numberbox" name="payFreight" id="payFreight" style="width:85%;"  data-options="label:'实际运费:',precision:2">
				 </div>
			<div style="text-align:center;padding:5px 0">
				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="addZfbData()" style="width:160px">录入支付宝余额</a>
			</div>
			</form>
		</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;" style="margin-left:500px;">
				<select class="easyui-combobox" name="year" id="year" style="width:15%;" data-options="label:'年份:',panelHeight:'auto'">
				<option value="0" selected>全部</option>
				<option value="2017">2017</option>
				<option value="2018">2018</option>
				<option value="2019">2019</option>
				<option value="2020">2020</option>
				<option value="2021">2021</option>
				</select>
				<select class="easyui-combobox" name="month" id="month" style="width:15%;height: 30px;" data-options="label:'月份:',panelHeight:'auto'">
				<option value="0" selected>全部</option>
				<option value="01">1</option>
				<option value="02">2</option>
				<option value="03">3</option>
				<option value="04">4</option>
				<option value="05">5</option>
				<option value="06">6</option>
				<option value="07">7</option>
				<option value="08">8</option>
				<option value="09">9</option>
				<option value="10">10</option>
				<option value="11">11</option>
				<option value="12">12</option>
				</select>
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<a href="javascript:exportdata();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">导出</a>
		<a href="javascript:openLog();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">录入支付宝余额及转账</a>
		<a href="/cbtconsole/website/orderSalesAmount.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">订单页面的销售额</a>
		<a href="/cbtconsole/website/orderDetailsSales.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">每天的进账退款数据</a>
		<a href="/cbtconsole/website/sales_buy.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">采购订单/销售订单匹配查询</a>
		<a href="javascript:uploadBuyingData();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">上传支付宝买入交易数据(Excel)</a>
		<%--<a href="/cbtconsole/website/data_query.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">GA数据统计</a>--%>
		<%--<a href="/cbtconsole/website/user_profit.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">用户月利润统计</a>--%>
		<br>
		<span style="color:red;">【D3=A+C-B-ebay-物料-支付宝支出运费】|【D3=D1】|【H=0】|【G=0】|【D1=D2】|【W1≈W2>W3】|【D2=E+F+G+H】</span><br>
		<span style="color:black;">S:销售额 &nbsp;&nbsp;Z:余额补偿 &nbsp;&nbsp;A:月初支付宝余额 &nbsp;&nbsp;B:月末支付宝余额&nbsp;&nbsp;C:银行转账给支付宝</span><br>
		<span style="color:darkturquoise;">D1:抓取采购订单金额&nbsp;&nbsp;D3:公式计算&nbsp;&nbsp;D2:支付宝付款</span><br>
		<span style="color:blueviolet;">E:实际入库非取消采购金额&nbsp;&nbsp;F:实际入库取消采购金额&nbsp;&nbsp;G:无订单匹配未入库采购金额 </span>
		<span style="color:blueviolet;">H:有匹配订单未入库采购金额&nbsp;&nbsp;J:对应上月订单采购金额&nbsp;&nbsp;I:本月产生库存金额&nbsp;&nbsp;K:销售库存金额</span><br>
		<span style="color:chocolate;">W1：预估运费金额&nbsp;&nbsp;W2:物流公司运费&nbsp;&nbsp;W4:实际支付运费(财务实际支出为准)&nbsp;&nbsp;W3:订单运费汇总金额</span>
		<span style="color:blueviolet;">P:月度利润=销售额S-采购额D1-运费W4- 库存减少(K-I)-余额补偿(Z)</span><br>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1500px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'times',width:25,align:'center'">月份</th>
				<th data-options="field:'order_sales',width:40,align:'center'">(S)</th>
				<th data-options="field:'balance_compensation',width:40,align:'center'">(Z)</th>
				<th data-options="field:'beginBlance',width:40,align:'center'">(A)</th>
				<th data-options="field:'endBlance',width:40,align:'center'">(B)</th>
				<th data-options="field:'transfer',width:40,align:'center'">(C)</th>
				<th data-options="field:'grabAmount',width:40,align:'center'">(D1)</th>
				<th data-options="field:'grabAmounts',width:30,align:'center'">(D3)</th>
				<th data-options="field:'zfbPayAmount',width:30,align:'center'">(D2)</th>
				<th data-options="field:'normalAmount',width:40,align:'center'">(E)</th>
				<th data-options="field:'cancelAmount',width:40,align:'center'">(F)</th>
				<th data-options="field:'noMatchingOrder',width:40,align:'center'">(G)</th>
				<th data-options="field:'noStorage',width:35,align:'center'">(H)</th>
				<th data-options="field:'lastMonth',width:25,align:'center'">(J)</th>
				<th data-options="field:'inventory_amount',width:30,align:'center'">(I)</th>
				<th data-options="field:'sale_inventory',width:25,align:'center'">(K)</th>
				<th data-options="field:'forecastAmount',width:35,align:'center'">(W1)</th>
				<th data-options="field:'actualAmount',width:35,align:'center'">(W2)</th>
				<th data-options="field:'orderFreight',width:35,align:'center'">(W3)</th>
				<th data-options="field:'payFreight',width:40,align:'center'">(W4)</th>
				<th data-options="field:'profit',width:40,align:'center'">(P)</th>
			</tr>
		</thead>
	</table>
</body>
</html>
