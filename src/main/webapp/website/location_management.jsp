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
<title>验货管理</title>
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

.window, .window-shadow {
    position: fixed;}
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
	opts.url = "/cbtconsole/warehouse/getLocationManagementInfo";
	orderid = '<%=orderid%>';
	if(orderid!==null && orderid!="null"){
		$("#orderid").textbox('setValue',orderid);
		doQuery(1);
	}
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '验货管理',
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
	var location= $('#location').combobox('getValue');
	var is_empty= $('#is_empty').combobox('getValue');
	var orderid = $("#orderid").val();
	var barcode = $("#barcode").val();
    var shipno = $("#shipno").val();
	$("#easyui-datagrid").datagrid("load", {
		location_type : location_type,
		orderid:orderid,
		location : location,
		is_empty:is_empty,
		page:page,
		barcode:barcode,
		shipno:shipno
	});
}

function doReset(){
	$('#location_type').combobox('setValue','-1');
	$('#location').combobox('setValue','-1');
	$('#is_empty').combobox('setValue','-1');
	$("#orderid").textbox('setValue','');
    $("#shipno").textbox('setValue','');
	$("#barcode").textbox('setValue','如:GS271');
}

function resetLocation(barcode,short_term){
	   $.ajax({
			type:"post", 
			url:"/cbtconsole/warehouse/resetLocation",
			dataType:"text",                                                      
			data:{barcode : barcode,short_term:short_term}, 
			success : function(data){
                $('#easyui-datagrid').datagrid('reload');
			}
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
/* function returnOr(cusorder){
	//if(cusorder==""||cusorder==null){
//		alert("该订单不存在");
//		return;
//	}
	window.location.href ="/cbtconsole/AddReturnOrder/FindReturnOrder/"+cusorder;

} */
/* function returnOr(orderid) {
	if (orderid == null || orderid == "") {
		$.messager.alert('提示', '获取订单号失败,请重试');
	} else {
		window
				.open(
						"/cbtconsole/website/ReApply.jsp?orderid="
								+ orderid,
						"windows",
						"height=400,width=800,top=500,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
	}
} */

function getItem() {   
    var tbOrder=this.$("#select_id option:selected").val();
    var cusOrder = $('#cuso').text();
   
    $.ajax({
        type: "POST",
        url: "/cbtconsole/Look/getAllOrder",
        data: {cusOrder:cusOrder,tbOrder:tbOrder,mid:0},
        dataType:"json",
        success: function(msg){
            if(msg.rows !=null&&msg.rows[0] != undefined ){
                var temHtml = '';
                document.getElementById("tabl").innerHTML='';
                $("#tabl").append("<tr ><td style='width:20px'>选择</td><td>产品名</td><td>产品规格</td><td>可退数量</td><td>退货原因</td><td>退货数量</td></tr>");
                $(msg.rows).each(function (index, item) {
                	
                 	$("#tabl").append("<tr ><td ><input type='checkbox' onclick='this.value=this.checked?1:0' style='width:20px' name='"+item.item+"' id='c1' /></td><td>"+item.item+"</td><td>"+item.sku+"</td><td>"+item.itemNumber+"</td><td>"+item.returnReason+"</td><td>"+item.changeShipno+"</td></tr>");
                     
                });
            }else{
            	alert("订单已全部退货")
            	$('#user_remark').window('close');

            }
        }
    });
}
function returnOr(uid) {
    $('#user_remark .remark_list').html('');
    $("#user_remark input[name='userid']").val(uid);
    $('#new_user_remark').val('');
    //查询历史备注信息
    $.ajax({
        type: "POST",
        url: "/cbtconsole/Look/getAllOrder",
        data: {cusOrder:uid,mid:1},
        dataType:"json",
        success: function(msg){
        	var opts = $("#easyui-datagrid").datagrid("options");
        	opts.url = "/cbtconsole/Look/LookReturnOrder?mid=1";
            if(msg.rows !=null&&msg.rows[0] != undefined){
                var temHtml = '';
                document.getElementById("select_id").innerHTML='';
                $("#cuso").html("");
                $("#cuso").append(msg.rows1[0].customerorder);
                document.getElementById("tabl").innerHTML='';
                $("#tabl").append("<tr ><td>选择</td><td>产品pid</td><td>产品规格</td><td>可退数量</td><td>退货原因</td><td>退货数量</td></tr>");
                $(msg.rows).each(function (index, item) {
                	
                 	$("#tabl").append("<tr ><td ><input type='checkbox' onclick='this.value=this.checked?1:0' name='"+item.item+"' id='c1' /></td><td><a href='https://www.importx.com/goodsinfo/122916001-121814002-1"+item.item+".html' target='_blank' >"+item.item+"</a></td><td>"+item.sku+"</td><td>"+item.itemNumber+"</td><td>"+item.returnReason+"</td><td>"+item.changeShipno+"</td></tr>");
                  
                	/* $("table").append("<tr ><td >1</td><td>"+item.item+"</td><td>产品规格</td><td>"+item.itemNumber+"</td><td></td><td></td></tr>"); */

                });
                $('#user_remark .remark_list').html(temHtml);
                $(msg.rows1).each(function (index, item) {
           		
           		 $("#select_id").append("<option id='' value='"+item.a1688Order+"'>"+item.a1688Order+"</option>");
           		 $('#user_remark').window('open');

           	 })
            }else{
            	alert("订单已全部退货")
            	//$('#user_remark').window('close');
               // location.reload()
                opts.url = "/cbtconsole/warehouse/getLocationManagementInfo";
            }
            opts.url = "/cbtconsole/warehouse/getLocationManagementInfo";
        }

    });
}
function AddOll() {
	 var tbOrder=this.$("#select_id option:selected").val();
	 var cusorder = $('#cuso').text();
	 //alert(cusorder)
	 var returnNO = $("input[name='radioname']checked").val();
	 var isAutoSend = document.getElementsByName('radioname');
	 for (var i = 0; i < isAutoSend.length; i++) {
	 if (isAutoSend[i].checked == true) {
	 returnNO=isAutoSend[i].value; 
	 }
	 }
	 //alert(returnNO)
	  $.post("/cbtconsole/Look/AddRetAllOrder", {
			cusorder:cusorder,tbOrder:tbOrder,returnNO:returnNO
		}, function(res) {
			if(res.rows == 1){
				alert('退货成功');
                $("#th"+cusorder).html("");
                $("#th"+cusorder).append("最后退货时间"+res.footer);
			}else{
				alert('不可重复退单');
			}
			
			getItem();
});
}
function checkboxOnclick(checkbox){
	 
	if ( checkbox.checked == true){
	 this.value=2;
	 
	}else{
	 
	this.value=1;
	 
	}
	 
	}

</script>
</head>
<body text="#000000" onload="doQuery(1);">
<div id="user_remark" class="easyui-window" title="退货申请"
         data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"
         style="width:800px;height:auto;display: none;font-size: 16px;">
            <div id="sediv" style="margin-left:20px;">
              选择1688订单号： <select id="select_id" onchange="getItem()"></select>
              <div>公司订单：<span id="cuso"></span></div>
                <table id="tabl" border="1" cellspacing="0">
                
               <tr >
               <td style='width:20px'>选择</td>
               <td>产品pid</td>
               <td>产品规格</td>
               <td>可退数量</td>
               <td>退货原因</td>
               <td>退货数量</td>
               </tr>
               </table> 
            </div>
            <div style="margin:20px 0 20px 40px;">
                <a href="javascript:void(0)" class="easyui-linkbutton"
                   onclick="addUserRemark()" style="width:80px" >提交申请</a>部分退单选择此按钮，全单退可以使用下方按钮
            </div>
             <div style="margin:20px 0 20px 40px;">
				 1688订单：<input class="but_color" type="button" value="整单提交" onclick="AddOll()">
                <input type='radio' size='5' name='radioname' value='客户退单' id='c' />客户退单
                <input type='radio' size='5' name='radioname' value='质量问题' id='c' />质量问题
                <input type='radio' size='5' name='radioname' value='客户要求' id='c' />客户要求
            </div>
    </div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" onsubmit="return false;">
				<select class="easyui-combobox" name="location_type" id="location_type" style="width:8%;" data-options="label:'库位类型:'">
				<option value="-1" selected>全部</option>
				<option value="1">中期库</option>
				<option value="0">今明库</option>
				</select> 
	 			<select class="easyui-combobox" name="location" id="location" style="width:8%;" data-options="label:'库位地址:'">
				<option value="-1" selected>全部</option>
				<option value="1">公司库位</option>
				<option value="2">仓库库位</option>
				</select>
				<select class="easyui-combobox" name="is_empty" id="is_empty" style="width:15%;" data-options="label:'库位选项:'">
				<option value="-1" selected>全部</option>
				<option value="1">库位不为空</option>
				<option value="0">库位为空</option>
				</select>
				<input class="easyui-textbox" name="orderid" id="orderid" style="width:15%;" onkeypress="if (event.keyCode == 13) doQuery(1)"  data-options="label:'orderid:'">
				<input class="easyui-textbox" name="shipno" id="shipno" style="width:15%;" onkeypress="if (event.keyCode == 13) doQuery(1)"  data-options="label:'shipno:'">
				<input class="easyui-textbox" value="如:GS271" name="barcode" id="barcode" style="width:15%;"  data-options="label:'库位码:',events:{blur:blurs,focus:focus},">
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
           <p style="text-align: right"><a  href="/cbtconsole/website/ReturnDisplay.jsp" target='_blank' style=" color:green; font-size:20px;" >退货管理页面</a></p>
        </div>
		中期库[<span id="zhongqi">${sessionScope.mid_barcode}</span>]个空位  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;今明库[<span id="duanqi">${sessionScope.shortTerm}</span>]个空位
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;已验货-待出库:<a target="_blank" href="/cbtconsole/website/order_library.jsp" target="blank"><span id="noInspection">${sessionScope.noInspection}</span></a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;已全到库-待验货:<a target="_blank" href="/cbtconsole/website/all_library.jsp" target="blank"><span id="allLibrary">${sessionScope.allLibrary}</span></a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;有待确认使用库存的商品:<a href="/cbtconsole/PurchaseServlet?action=getStockOrderInfo&className=Purchase&pagenum=1&orderid=0" target="blank"><span style="color:red;" id="stockOrderInfo">${sessionScope.stockOrderInfo}</span></a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/cbtconsole/website/location_tracking.jsp" target="_blank">库位清空记录</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/cbtconsole/website/noMatchGoods.jsp" target="_blank">最近一天验货的商品</a>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="/cbtconsole/website/storage_inspection_log.jsp" target="_blank">入库验货数量查看</a>
	</div>
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'barcode',width:25,align:'center'">库位编号</th>
				<th data-options="field:'short_term',width:15,align:'center'">库位类型</th>
				<th data-options="field:'acount',width:15,align:'center'">是否为空</th>
				<th data-options="field:'orderids',width:30,align:'center'">公司订单</th>
				<th data-options="field:'tborderids',width:70,halign:'center'">淘宝订单</th>
				<th data-options="field:'shipnos',width:70,halign:'center'">快递单号</th>
				<th data-options="field:'state',width:30,align:'center'">验货状态</th>
				<th data-options="field:'is_company',width:20,align:'center'">库位位置</th>
				<th data-options="field:'createtime',width:38,align:'center'">时间记录</th>
				<th data-options="field:'operation',width:30,align:'center'">操作</th>
				
			</tr>
		</thead>
	</table>
</body>
<script type="text/javascript">
function addUserRemark() {
	var tbOrder=this.$("#select_id option:selected").val();
    var cusOrder = $('#cuso').text();
    var der =$("#retu").val();
   var g="";
     $("#tabl tr").each(function(){
    	
    	 $(this).find('input').each(function(){   
    		 var value = $(this).val();  
    			g+=value+","; 
    	
    	}); 	
     });
     g+=tbOrder+",";
	 g+=cusOrder;   
 
     $.post("/cbtconsole/Look/AddAllOrder", {
    	 cusOrder:g
		}, function(res) {
			if(res.rows == 1){
				alert('退货成功');
                $("#th"+cusOrder).html("");
                $("#th"+cusOrder).append("最后退货时间"+res.footer);
			}else if(res.rows==0){
				alert('不可重复退单');
			}else if(res.rows==2){
				alert('请勾选要退的商品');
			}else if(res.rows==3){
				alert('请填写数据');
			}
			else if(res.rows==4){
				alert('退货数量不能大于可退数量');
			}else if(res.rows==5){
				alert('该商品已全部退货');
			}
			getItem();			
});            
}

</script>
</html>