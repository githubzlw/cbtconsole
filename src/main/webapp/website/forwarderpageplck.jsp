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
<title>已出库列表</title>
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
<%
	String orderid=request.getParameter("orderid");
	%>
<script type="text/javascript">

/* 获取url中参数 */
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}
/* 时间格式化 */
function formatNum(val) {
    return val < 10 ? "0" + val : val
}
/* 日期格式化 */
function formatterData(value) {
    if(value != null && value != "") {
        var date = new Date(value);
        return date.getFullYear() + '-' + formatNum(date.getMonth() + 1) + '-' + formatNum(date.getDate()) + ' ' + formatNum(date.getHours()) + ':' + formatNum(date.getMinutes());
    } else {
        return "";
    }
}
/* 格式化图片的显示 */
function formatterImg(imgUrl) {
	if (imgUrl != null && imgUrl != "") {
		var temHtml = '<div style="width: 234px;height:234px;float: left;"><a href="' 
			+ imgUrl
			+ '"  target="_blank"><img style="width: 230px;" src="' 
			+ imgUrl
			+ '"/></a></div>';
		return temHtml;
	}
	return "";
}
/* 点击出运详情的详情后 查询并显示对应信息 */
function showDetails(remarks) {
	//初始化 清空
	var picturepaths = $("#picturepaths");
	picturepaths.empty();
	var ftpPicPaths = $("#ftpPicPaths");
	ftpPicPaths.empty();
	$.ajax({
	   type: "GET",
	   url: "/cbtconsole/warehouse/getDetailsByRemarks.do?remarks=" + remarks,
	   dataType:"json",
	   success: function(msg){
		   if(msg == null){
               // 未查询到数据 跳转
               return;
           }
		   if(msg.picturepaths != null && msg.picturepaths.length > 0){ //入库验货照片 集合
			   $(msg.picturepaths).each(function(index, item) {
				   picturepaths.append(formatterImg(item));
               });
           } else {
        	   picturepaths.append($("<div style=\"width: 254px;height:154px;float: left;\"><img src=\"http://img1.import-express.com/importcsvimg/webpic/newindex/img/index315/check_pictures.jpg\"/><br /><span style='color: #666'>Oh no quality check pictures</span></td></div>"));
           }
		   if(msg.ftpPicPaths != null && msg.ftpPicPaths.length > 0){ //出货照片 集合
			   $(msg.ftpPicPaths).each(function(index, item) {
				   ftpPicPaths.append(formatterImg(item));
               });
           } else {
        	   ftpPicPaths.append($("<div style=\"width: 254px;height:154px;float: left;\"><img src=\"http://img1.import-express.com/importcsvimg/webpic/newindex/img/index315/check_pictures.jpg\"/><br /><span style='color: #666'>Oh no shipping pictures</span></td></div>"));
           }
		 	//显示模态框
		    $('#myModal').modal('show');
	   }
	});
}
/* 列表显示格式化  formatterDetails*/
function formatterDetails(value, row, index) {
	if(row.remarks != null) {
     return '<a onclick="showDetails(\'' + row.remarks + '\');">详情</a>'
	}
	return;
}
$(function(){
	setDatagrid();
	var opts = $("#easyui-datagrid").datagrid("options");
	opts.url = "/cbtconsole/warehouse/getForwarderPlck";
	var orderno='<%=orderid%>';
	if(orderno!=null && orderno!="null" && orderno!=""){
		$("#orderid").textbox('setValue',orderno);
		doQuery(1);
	}
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '已出库列表',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20],//可以选择的分页集合
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
	
function getFreight_package(weight,vol,countryid){
	var freight;
		$.ajax({          
		type:"post", 
		url:"/cbtconsole/feeServlet?action=getFreight_package&className=GetFright",
		dataType:"json",
		async: false,
		data:{'weight':weight,'volume':vol,'countryid':countryid},
		success : function(res){
			var length = res.length;
			if(length==0){
				freight =0;
			}else{
				freight = res[length-1].result;
			}
		}
	});
	return freight;
}
	
function doQuery(page) {
	var iduserid = $("#iduserid").val();
	var orderid = $("#orderid").val();
	var express_no = $("#express_no").val();
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
    	"express_no":express_no,
    	"orderid":orderid,
    	"userid":iduserid
	});
}

function updateExperssNo(id,shipmentno,volume,weight,freight,countryid){
	var t =volume.split("*");
	var vol;
	var express_no=$("#express_no_"+id+"").val();
	if(t.length>2){
		vol = (t[0]*0.01)*(t[1]*0.01)*([2]*0.01);
		var estimatefreight =  0.00;//getFreight_package(weight,vol,countryid);
		  $.ajax({
				type:'post',                                                          
				url:'/cbtconsole/warehouse/updateExperssNopPlck.do',              
				data:{express_no:express_no,shipmentno:shipmentno,logistics_name:$("#logistics_name_"+id).val(),
					sweight:weight,svolume:volume,freight:freight,estimatefreight:estimatefreight},
				success:function(retData){
					if(Number(retData)>0){
						$.messager.alert('提示','操作成功');
					}else{
						$.messager.alert('提示','操作失败');
					}
				}
			})
	}	
}


function doReset(){
	$('#query_form').form('clear');
}

jQuery(function($){
	var expressNo = getUrlParam("expressNo");
	if(expressNo != undefined){
		$("#express_no").textbox('setValue',expressNo);
	}
	if(getUrlParam("orderid") == undefined){
		doQuery(1);
	}
});

</script>
</head>
<body>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" style="margin-left:350px;" onsubmit="return false;">
				<input class="easyui-textbox" name="iduserid" id="iduserid" style="width:15%;margin-top: 10px;"  data-options="label:'用户id:'">
				<input class="easyui-textbox" name="orderid" id="orderid" style="width:15%;margin-top: 10px;"  data-options="label:'订单号:'">
				<input class="easyui-textbox" name="express_no" id="express_no" style="width:15%;margin-top: 10px;"  data-options="label:'快递单号:'">
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'user_id',width:25,align:'center'">用户id</th>
				<th data-options="field:'remarks',width:50,align:'center'">订单编号</th>
				<th data-options="field:'shipmentno',width:25,align:'center'">出运编号</th>
				<th data-options="field:'expressno',width:70,align:'center'">快递跟踪号</th>
				<th data-options="field:'transportcompany',width:30,align:'center'">出货方式</th>
				<th data-options="field:'sweight',width:25,align:'center'">打包重量(kg)</th>
				<th data-options="field:'volumeweight',width:25,align:'center'">打包抛重(kg)</th>
				<th data-options="field:'svolume',width:30,align:'center'">打包体积cm3)</th>
				<th data-options="field:'ad_country',width:30,align:'center'">订单目标国家</th>
				<th data-options="field:'estimatefreight',width:30,align:'center'">根据实际重量计算的运费</th>
				<th data-options="field:'actualFreight',width:30,align:'center'">实际运费</th>
				<th data-options="field:'types',width:30,align:'center'">抛货/重货</th>
				<th data-options="field:'optimal_company',width:30,align:'center'">最优运费方式</th>
				<th data-options="field:'optimal_cost',width:30,align:'center'">我司最优运费</th>
				<th data-options="field:'createtime',width:40,align:'center'">出货时间(我司)</th>
				<th data-options="field:'senttime',width:40,align:'center'">出运时间(物流)</th>
				<c:if test="${param.flag }">
					<th data-options="field:'details',width:40,align:'center',formatter:formatterDetails">出运详情</th>
				</c:if>
			</tr>
		</thead>
	</table>
	<!-- 模态框 用于弹窗显示订单详细  -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
       <div class="modal-dialog" style="width: 76%;">
           <div class="modal-content">
               <div class="modal-header">
                   <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                   <h4 class="modal-title" id="myModalLabel">出运详情</h4>
               </div>
               <div class="modal-body">
               <!-- start 弹窗显示内容 -->
				<table border="0px">
					<tr><td><span style="display: block;font-size: 14px">入库验货照片:</span></td></tr>
					<tr><td id="picturepaths">
					</td></tr>
					<tr><td><span style="display: block;font-size: 14px">出货照片:</span></td></tr>
					<tr><td id="ftpPicPaths">
					</td></tr>
				</table>
			   <!-- end 弹窗显示内容 -->
               </div>
               <div style="clear:both"></div>
               <div class="modal-footer">
                   <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                   <!--<button type="button" class="btn btn-primary">提交更改</button>-->
               </div>
               <div style="clear:both"></div>
           </div>
       </div>
   </div>
</body>
</html>