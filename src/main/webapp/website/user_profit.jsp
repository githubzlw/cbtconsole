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
<title>用户月利润统计报表</title>
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
.datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber,
.datagrid-cell-rownumber {
	font-size: 14px;
}

.datagrid-header .datagrid-cell span, .panel-body {
	font-size: 14px;
}

.datagrid-cell, .datagrid-cell-group, .datagrid-header-rownumber,
.datagrid-cell-rownumber {
	height: 28px;
	line-height: 22px;
	padding: 3px 5px;
}

</style>
<script type="text/javascript">
     function lastMonthDate(){
             var Nowdate = new Date();
             var vYear = Nowdate.getFullYear();
             var vMon = Nowdate.getMonth() + 1;
             var vDay = Nowdate.getDate();
         　　//每个月的最后一天日期（为了使用月份便于查找，数组第一位设为0）
             var daysInMonth = new Array(0,31,28,31,30,31,30,31,31,30,31,30,31);
             if(vMon==1){
                     vYear = Nowdate.getFullYear()-1;
                     vMon = 12;
                 }else{
                     vMon = vMon -1;
                 }　　//若是闰年，二月最后一天是29号
		 if(vYear%4 == 0 && vYear%100 != 0 || vYear%400 ==0){
             daysInMonth[2]= 29;
		 }
             if(daysInMonth[vMon] < vDay){
                    vDay = daysInMonth[vMon];
                }
		 if(vDay<10){
                     vDay="0"+vDay;
                 }
            if(vMon<10){
				vMon="0"+vMon;
                }
            var date =vYear+"-"+ vMon +"-"+vDay;
             console.log(date)
         $('#year').combobox('setValue',vYear);
         $('#month').combobox('setValue',vMon);
             // return date;
         }

$(function(){
    document.onkeydown = function(e) {
        var ev = document.all ? window.event : e;
        if (ev.keyCode == 13) {
            doQuery(1);
        }
    }
    lastMonthDate();
	setDatagrid();
	var opts = $("#easyui-datagrid").datagrid("options");
	opts.url = "/cbtconsole/StatisticalReport/getUserProfitByMonth";
})


function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '用户月利润统计报表',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 100,//默认选择的分页是每页20行数据
			pageList : [ 100],//可以选择的分页集合
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
		});
	}
	
function doQuery(page) {
	var year=$('#year').combobox('getValue');
	var month=$('#month').combobox('getValue');
    var userId = $.trim(document.getElementById("userId").value);
	if(year=="0"){
		$.messager.show({
			title:'提示',
			msg:'请选择年份',
			showType:'slide',
			timeout: 1000,
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop,
				bottom:''
			}
		});
		return;
	}else if(month=="0" && userId == ""){
		$.messager.show({
			title:'提示',
			msg:'全部用户查询必须选择月份',
			showType:'slide',
			timeout: 1000,
			style:{
				right:'',
				top:document.body.scrollTop+document.documentElement.scrollTop,
				bottom:''
			}
		});
		return;
	}
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
		"year":year,
		"month":month,
		"userId":userId
	});
    getExchange();
    profitSummaryData();
}

function doReset(){
	// $('#year').combobox('setValue','0');
	// $('#month').combobox('setValue','0');
    $("#userId").textbox('setValue','');
    lastMonthDate();
}

/**
 * 查询结果导出excel
 */
 function doExport(){
    var userId = $.trim(document.getElementById("userId").value);
	 var year=$('#year').combobox('getValue');
	 var month=$('#month').combobox('getValue');
    $.messager.show({
        title:'导出中....',
        msg:'可能会花费1-2分钟，请稍等.......',
        showType:'slide',
        timeout: 20000,
        style:{
            right:'',
            top:document.body.scrollTop+document.documentElement.scrollTop,
            bottom:''
        }
    });
    window.open("/cbtconsole/StatisticalReport/exportUserProfitByMonth?year="+year+"&month="+month+"&userId="+userId, "_blank");
	 // window.location.href ="/cbtconsole/StatisticalReport/exportUserProfitByMonth?year="+year+"&month="+month;
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

 function doUpload(){
     var year=$('#year').combobox('getValue');
     var month=$('#month').combobox('getValue');
     if(year =='0' || month == '0'){
         showMessage("请选择年、月");
         return;
	 }
     $.ajax({
         type:"post",
         url:"/cbtconsole/StatisticalReport/createUserProfitByMonth",
         data:{year:year,month:month},
         async:true,
         success : function(data){
			if(data>0){
                showMessage("生成成功，请查询");
			}else{
                showMessage("生成失败...");
			}
         }
     });

 }

     //异步获取利润汇总数据
     function profitSummaryData(){
         // $("#AllEstimateProfit").html("--");
         $("#AllForecastProfits").html("--");
         $("#endProfit").html("--");
         var year=$('#year').combobox('getValue');
         var month=$('#month').combobox('getValue');
         var userId = $.trim(document.getElementById("userId").value);
         if(year =='0' || month == '0'){
             showMessage("请选择年、月");
             return;
         }
         $.ajax({
             type:"post",
             url:"/cbtconsole/StatisticalReport/profitSummaryData",
             data:{year:year,month:month,userId:userId},
             success : function(data){
                 var json=eval(data);
                 if(json != null){
                     // $("#AllEstimateProfit").html(json.estimateProfit);
                     $("#AllForecastProfits").html(json.forecastProfits);
                     $("#endProfit").html(json.endProfit);
                 }
             }
         });
     }

</script>
</head>
<body text="#000000" >
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;" style="margin-left:500px;">
				<input class="easyui-numberbox" name="userId" id="userId"   style="width:15%;"  data-options="label:'用户ID:'">
				<select class="easyui-combobox" name="year" id="year" style="width:15%;" data-options="label:'支付年份:',panelHeight:'auto'">
				<%--<option value="0" selected>全部</option>--%>
				<option value="2017">2017</option>
				<option value="2018">2018</option>
				</select>
				<select class="easyui-combobox" name="month" id="month" style="width:15%;height: 30px;" data-options="label:'支付月份:',panelHeight:'auto'">
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
		<span style="color:crimson;margin-left:500px;">物流公司运费计算最终利润金额：<span style="color:red;" id="endProfit"></span></span>
		<%--<span style="color:crimson;margin-left:100px;">预估重量预估运费计算预估利润金额：<span style="color:red;" id="AllEstimateProfit"></span></span>--%>
		<span style="color:crimson;margin-left:100px;">实际重量预估的运费实际利润金额：<span style="color:red;" id="AllForecastProfits"></span></span><br>
		<span style="color:blueviolet;">EUR兑美元汇率：<span style="color:red;" id="eur_rate"></span></span>
		<span style="color:blueviolet;margin-left:100px;">CAD兑美元汇率：<span style="color:red;" id="cad_rate"></span></span>
		<span style="color:blueviolet;margin-left:100px;">GBP兑美元汇率：<span style="color:red;" id="gbp_rate"></span></span>
		<span style="color:blueviolet;margin-left:100px;">AUD兑美元汇率：<span style="color:red;" id="aud_rate"></span></span>
		<span style="color:blueviolet;margin-left:100px;">RMB兑美元汇率：<span style="color:red;" id="rmb_rate"></span></span>
		<br>
		<span style="color:blueviolet;"> 预估重量=该客户本月所有订单里面所有产品重量之和（不是实际重量）</span>||
		<span style="color:blueviolet;">实际重量=比如 有 两个出运号，分别重量是 1KG, 那实际重量就是 2KG </span><br>
		<span style="color:red;"> 预计运费=实际包裹重量和实际出运方式计算的运费</span>||
		<span style="color:red;">根据预估重量预估运费计算预估利润金额(RMB)=预估重量 * 客户所选运输方式的运价（订单订单预计国际运费） </span>||
		<span style="color:red;">用户利润预估=实际支付金额-人为录入的采购额-预计运费 </span>||
		<span>物流公司运费计算最终利润金额RMB（最终利润率%）= 销售额-录入采购金额-最终录入的运费</span>||
		<span style="color:red;">根据实际重量预估的运费实际利润金额RMB=实际支付金额-人为录入的采购额-实际运费 </span><br>
		A:支付月份；B:用户ID；D:VIP等级；E:订单数量;F:相关订单号;G:相关运单号/实际重量/实重预估运费;H:实际重量(kg);I:预估重量(kg);J:销售额(RMB);K:最终录入的运费（物流公司）(RMB);L:客户付的运费(RMB);M:实际重量预估运费(RMB)
		N:根据实际重量预估的运费实际利润金额RMB;Q:录入采购金额(RMB);R:用户利润率(%);S:根据物流公司运费计算最终利润金额RMB；T:自动抓取的采购额<br>
		<span style="background-color:red">F列订单号为红色订单采购数量与销售数量不一致或预计采购金额和实际采购金额相差10%需要核查</span>
		<%--O:根据预估重量预估运费计算预估利润金额(RMB);P:用户预估利润率(%);--%>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1500px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'times',width:25,align:'center'">A</th>
				<th data-options="field:'id',width:40,align:'center'">B</th>
				<%--<th data-options="field:'email',width:80,align:'center'">C</th>--%>
				<th data-options="field:'grade',width:45,align:'center'">D</th>
				<th data-options="field:'orderCount',width:20,align:'center'">E</th>
				<th data-options="field:'orderids',width:80,align:'center'">F</th>
				<th data-options="field:'shipnos',width:140,align:'center'">G</th>
				<th data-options="field:'ac_weight',width:45,align:'center'">H</th>
				<th data-options="field:'es_weight',width:45,align:'center'">I</th>
				<th data-options="field:'salesAmount',width:45,align:'center'">J</th>
				<th data-options="field:'buyAmount',width:45,align:'center'">Q</th>
				<th data-options="field:'getAmount',width:45,align:'center'">T</th>
				<th data-options="field:'freight',width:45,align:'center'">K</th>
				<th data-options="field:'custom_freight',width:45,align:'center'">L</th>
				<th data-options="field:'estimateFreight',width:45,align:'center'">M</th>
				<%--<th data-options="field:'esFreight',width:45,align:'center'">预估运费(RMB)</th>--%>
				<th data-options="field:'forecastProfits',width:45,align:'center'">N</th>
				<th data-options="field:'fProfits',width:45,align:'center'">R</th>
				<%--<th data-options="field:'estimateProfit',width:45,align:'center'">O</th>--%>
				<%--<th data-options="field:'esprofits',width:45,align:'center'">P</th>--%>
				<th data-options="field:'endProfits',width:45,align:'center'">S</th>
			</tr>
		</thead>
	</table>
</body>
<script type="text/javascript">
    $(function() {
        getExchange();
	})

	function getExchange(){
        var year=$('#year').combobox('getValue');
        var month=$('#month').combobox('getValue');
        $.ajax({
            type:"post",
            url:"/cbtconsole/StatisticalReport/getExchange",
            data:{year:year,month:month},
            success : function(data){
                console.log(data);
                 var json=eval(data);
                if(json != null){
                    $("#eur_rate").html(json.eur_rate);
                    $("#cad_rate").html(json.cad_rate);
                    $("#gbp_rate").html(json.gbp_rate);
                    $("#aud_rate").html(json.aud_rate);
                    $("#rmb_rate").html(json.rmb_rate);
                }else{
                    $("#eur_rate").html("暂无");
                    $("#cad_rate").html("暂无");
                    $("#gbp_rate").html("暂无");
                    $("#aud_rate").html("暂无");
                    $("#rmb_rate").html("暂无");
				}
            }
        });
	}
</script>
</html>