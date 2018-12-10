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
<title>商品库存盘点</title>
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
var updateSourcesUrl = "/cbtconsole/StatisticalReport/updateSources"; //盘点库存
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
<% 
  String sku=request.getParameter("sku");
  String pid=request.getParameter("pid");
  String car_urlMD5=request.getParameter("car_urlMD5");
%>
<script type="text/javascript">
$(function(){
	setDatagrid();
	var sku='<%=sku%>';
    var pid='<%=pid%>';
    if(sku != null && sku != '' && sku != 'null'){
	   	 console.log("sku="+sku);
	   	 $("#sku").val(sku);
	   	 doQuery(1);
    }
    if(pid != null && pid != '' && pid != 'null'){
        console.log("pid="+pid);
        $("#goods_pid").val(pid);
        doQuery(1);
    }
	$.ajax({
		url : "/cbtconsole/inventory/searchAliCategory",
		data:{
        	  "type":"type1",
        	  "cid":"0"
        	  },
		type : "post",
		success :function(data){
			if(data){
				var reportDetailList=data.data.aliCategoryList;
				htm_='';
				for(var i=0;i<reportDetailList.length;i++){
                	htm_ += "<option value='"+reportDetailList[i].category+"'> "+reportDetailList[i].category+"";
                	htm_ += '</option>            ';
        		}
				$('#type1').append(htm_);
			}
		}
	});
	$('#dlg').dialog('close');
	$('#dlg1').dialog('close');
    $('#dlg3').dialog('close');
// 	var opts = $("#easyui-datagrid").datagrid("options");
// 	opts.url = "/cbtconsole/StatisticalReport/searchGoodsInventoryInfo";
	
})

function selecType2(type,cid){
		if(type=="type1"){
			$("#type2").html("");
			$("#type3").html("");
			$("#type4").html("");
			$("#type5").html("");
		}else if(type=="type2"){
			$("#type3").html("");
			$("#type4").html("");
			$("#type5").html("");
		}else if(type=="type3"){
			$("#type4").html("");
			$("#type5").html("");
		}else if(type=="type4"){
			$("#type5").html("");
		}
		$.ajax({
			url : "/cbtconsole/inventory/searchAliCategory",
			data:{
	        	  "type":type,
	        	  "cid":cid
	        	  },
			type : "post",
			async:false,
			success :function(data){
				if(data){
					$("#"+type+"").html("");
					var reportDetailList=data.data.aliCategoryList;
					for(var i=0;i<reportDetailList.length;i++){
	        			htm_='';
	                	htm_ = '<option value='+reportDetailList[i].category+'> '+reportDetailList[i].category+'';
	                	htm_ += '</option>            ';
		        		$('#'+type+'').append(htm_);
	        		}
				}
			}
		});
	}
	

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '商品库存盘点',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20],//可以选择的分页集合
			nowrap : false,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '/cbtconsole/inventory/searchGoodsInventoryInfo',//url调用Action方法
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
	var have_barcode=$('#have_barcode').combobox('getValue');
    var valid=$('#valid').combobox('getValue');
    var goodscatid=$('#goodscatid').combobox('getValue');
	var flag =$('#flag').val();
	var type =$('#type').val();
	var goodinfo =$('#goodinfo').val();
    var goods_pid =$('#goods_pid').val();
	var scope =$('#scope').val();
	var count =$('#count').val();
	var sku =$('#sku').val()
	var barcode =$('#barcode').val();
	var type1 =$('#type1').val();
	var type_="0";
	var startdate = $("#startdate").val();
	var enddate = $("#enddate").val();
	if(type1!=null){
		type_=type1;
	}
	$("#easyui-datagrid").datagrid("load", {
	  "page":page,
  	  "type":type,
  	  "goodinfo":goodinfo,
  	  "scope":scope,
  	  "count":count,
  	  "type_":type_,
  	  "sku":sku,
  	  "barcode":barcode,
  	  "flag":flag,
  	  "have_barcode":have_barcode,
  	  "startdate":startdate,
  	  "enddate":enddate,
	  "goods_pid":goods_pid,
        "valid":valid,
        "goodscatid":goodscatid

    });
}

function doReset(){
	$("#flag").val("-1");
	$("#type").val("remaining");
	$("#scope").val("0");
	$("#count").val("");
	$("#goodinfo").val("");
	$("#sku").val("");
	$("#barcode").val("");
	$("#type1").val("0");
	$("#type2").val("");
	$("#type3").val("");
	$("#type4").val("");
	$("#type5").val("");
	$("#goods_pid").val();
	$('#have_barcode').combobox('setValue','全部');
    $('#valid').combobox('setValue','-1');
	$("#startdate").val("");
	$("#enddate").val("");
    $('#goodscatid').combobox('setValue','全部');
}

function update_inventory(flag,id,barcode,old_remaining,remark) {
	$('#dlg').dialog('open');
	$('#ff').form('load',{
		old_remaining:old_remaining,
		old_barcode:barcode,
		remark:remark,
		pd_id:id,
		flag_:flag
	});
}


function problem_inventory(id){
	 $.messager.confirm('继续操作', '确定要讲该库存标记为问题库存吗?',         
			 function(r){
				 if (r){
				  	 var params = {"in_id":id};
				  	 $.ajax({  
				           url:'/cbtconsole/inventory/problem_inventory',
				           type:"post",  
				           data:params,  
				           success:function(data){
				         	if(Number(data)>0){
				         		topCenter("操作成功");
				         		$('#easyui-datagrid').datagrid('reload');
				   	  		}else{
				   	  		topCenter("操作失败");
				   	  		}
				           }, 
				       }); 
				 }
			 }
	 );
}

function updateSources(){
	 $.ajaxSetup({
	        async: false
	 });
	var new_barcode = $('#new_barcode1').combobox('getValue');
	var old_barcode = $.trim(document.getElementById("old_barcode").value);
	var new_remaining = $.trim(document.getElementById("new_remaining").value);
	var old_remaining = $.trim(document.getElementById("old_remaining").value);
	var remark = $.trim(document.getElementById("remark").value);
	var flag = $.trim(document.getElementById("flag_").value);
	var pd_id = $.trim(document.getElementById("pd_id").value);
	jQuery.ajax({
       url:updateSourcesUrl,
       data:{
       	  "new_barcode":new_barcode,
       	  "old_barcode":old_barcode,
       	  "new_remaining":new_remaining,
       	  "old_remaining":old_remaining,
       	  "remark":remark,
       	  "pd_id":pd_id,
       	  "flag":flag
       	  },
       type:"post",
       success:function(data){
       	var allCount=data.data.allCount;
       	if(allCount>0){
       		$("#remark").textbox('setValue','');
       		$("#new_remaining").textbox('setValue','');
       		$('#new_barcode1').combobox('setValue','');
       		$('#new_remaining').textbox('setValue','');
       		$("#pd_id").val("");
       		$("#flag_").val("");
       		topCenter("库存盘点成功");
       		$('#dlg').dialog('close')
       		$('#easyui-datagrid').datagrid('reload');
       	}else{
       		topCenter("库存盘点失败");
       	}
       },
   	error:function(e){
   		topCenter("库存盘点失败");
   	}
   });
}

function BigImg(img){
	htm_="<img width='400px' height='400px' src="+img+">";
	$("#big_img").append(htm_);
	$("#big_img").css("display","block");
}

function closeBigImg(){
	$("#big_img").css("display","none");
	$('#big_img').empty();
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

//导出报表
function exportData(){
	//生成报表
	var have_barcode=$('#have_barcode').combobox('getValue');
	var flag =$('#flag').val();
	var type =$('#type').val();
	var goodinfo =$('#goodinfo').val();
	var scope =$('#scope').val();
	var count =$('#count').val();
	var sku =$('#sku').val()
	var barcode =$('#barcode').val();
	var type1 =$('#type1').val();
	var type_="0";
	var startdate = $("#startdate").val();
	var enddate = $("#enddate").val();
    var goodscatid=$('#goodscatid').combobox('getValue');
	if(type1!=null){
		type_=type1;
	}
	if(goodscatid == "全部"){
        goodscatid="abc";
	}else if(goodscatid == "其他"){
        goodscatid="bcd"
	}
	window.location.href ="/cbtconsole/inventory/exportGoodsInventory?startdate="+startdate+"&enddate="+enddate+"&type="+type+"&goodinfo="+goodinfo+"&scope="+scope+"&count="+count+"&sku="+sku+"&type_="+type_+"&barcode="+barcode+"&flag="+flag+"&goodscatid="+goodscatid;
}

function openInventoryEntryView(){
	$('#dlg1').dialog('open');
}

function openYmxInventoryEntryView(){
    $('#dlg3').dialog('open');
}

function inventoryEntry(){
	var goodsid=$("#goodsid").val();
	var count=$("#count_").val();
	var in_barcode = $('#new_barcode2').combobox('getValue');
	var remark=$("#remark_").val();
	jQuery.ajax({
	       url:"/cbtconsole/inventory/inventoryEntry",
	       data:{
	       	  "goodsid":goodsid,
	       	  "count":count,
	       	  "in_barcode":in_barcode,
	       	  "remark":remark
	       	  },
	       type:"post",
	       success:function(data){
	       	var allCount=data.data.allCount;
	       	if(allCount>0){
				topCenter("库存录入成功");
	       		$('#dlg1').dialog('close')
	       		$("#goodsid").textbox('setValue','');
				$("#count_").textbox('setValue','');
				$("#remark_").textbox('setValue','');
				$('#new_barcode2').combobox('setValue','');
	       		setTimeout(function(){
	       			var pages=$('#easyui-datagrid').datagrid('options').pageNumber;
	       			doQuery(pages);
				}, 1000)
	       	}else{
	       		topCenter("库存录入失败");
	       	}
	       },
	   	error:function(e){
	   		topCenter("库存录入失败");
	   	}
	   });
}

function inventoryYmxEntry(){
    var itmeid=$("#itmeid").val();
    var ymx_count=$("#ymx_count").val();
    var goods_p_price=$("#goods_p_price").val();
    var ymx_barcode2 = $('#ymx_barcode2').combobox('getValue');
    var remark_ymx=$("#remark_ymx").val();
    var ymx_img=$("#ymx_img").val();
    var ymx_name=$("#ymx_name").val();
    jQuery.ajax({
        url:"/cbtconsole/StatisticalReport/inventoryYmxEntry",
        data:{
            "itmeid":itmeid,
            "ymx_count":ymx_count,
            "ymx_barcode2":ymx_barcode2,
            "remark_ymx":remark_ymx,
            "ymx_img":ymx_img,
			"ymx_name":ymx_name,
			"goods_p_price":goods_p_price
        },
        type:"post",
        success:function(data){
            var allCount=data.data.allCount;
            if(allCount>0){
                topCenter("亚马逊库存录入成功");
                cance3();
                setTimeout(function(){
                    var pages=$('#easyui-datagrid').datagrid('options').pageNumber;
                    doQuery(pages);
                }, 1000)
            }else{
                topCenter("亚马逊库存录入失败");
            }
        },
        error:function(e){
            topCenter("亚马逊库存录入失败");
        }
    });
}

function cance1(){
	$('#dlg1').dialog('close');
	$("#goodsid").textbox('setValue','');
	$("#count_").textbox('setValue','');
	$("#remark_").textbox('setValue','');
	$('#new_barcode2').combobox('setValue','');
}

function cance3(){
    $('#dlg3').dialog('close');
    $("#itmeid").textbox('setValue','');
    $("#ymx_count").textbox('setValue','');
    $("#goods_p_price").textbox('setValue','');
    $("#ymx_img").textbox('setValue','');
    $("#ymx_name").textbox('setValue','');
    $("#remark_ymx").textbox('setValue','');
    $('#ymx_barcode2').combobox('setValue','');
}

function cance(){
	$('#dlg').dialog('close')
	$("#new_remaining").textbox('setValue','');
	$("#remark").textbox('setValue','');
	$('#new_barcode1').combobox('setValue','');
}

function changDiv(value){
	if(value=="1"){
		var rfddd = document.getElementById("pandian_time");
		rfddd.style.display = "block";
	}else{
		var rfddd = document.getElementById("pandian_time");
		rfddd.style.display = "none";
	}
}

function initData(){
    //最近30天 新产生的库存
    $.ajax({
        type:'post',
        dataType:"text",
        async:true,
        url:'/cbtconsole/warehouse/getNewInventory',
        data:{},
        success:function(data){
            $("#tj_info_1").html(data);
        }});
    //最近30天销售掉的库存
    $.ajax({
        type:'post',
        dataType:"text",
        async:true,
        url:'/cbtconsole/warehouse/getSaleInventory',
        data:{},
        success:function(data){
            if(data=='null'){
                data = 0;
            }
            $("#tj_info_2").html(data);
        }});
    //最近30天 产生的 库存损耗
    $.ajax({
        type:'post',
        dataType:"text",
        async:true,
        url:'/cbtconsole/warehouse/getLossInventory',
        data:{},
        success:function(data){
            if(data=='null'){
                data = 0;
            }
            $("#tj_info_3").html(data);
        }});
    //最近30天 产生的 库存删除
    $.ajax({
        type:'post',
        dataType:"text",
        async:true,
        url:'/cbtconsole/warehouse/getDeleteInventory',
        data:{},
        success:function(data){
            if(data=='null'){
                data = 0;
            }
            $("#tj_info_4").html(data);
        }});
}

function cance2(){
    $("#delRemark").textbox('setValue','');
    $('#dlg2').dialog('close');
    $("#dId").val("");
    $("#dPid").val("");
    $("#dBarcode").val("");
    $("#dAmount").val("");
}

function delInventorySources(){
   var dId=$("#dId").val();
   var dPid= $("#dPid").val();
   var dBarcode= $("#dBarcode").val();
   var dAmount= $("#dAmount").val();
    var delRemark=$("#delRemark").val();
    if(delRemark == null || delRemark == ""){
        topCenter("请输入删除备注");
        return;
	}
    jQuery.ajax({
        url:"/cbtconsole/StatisticalReport/deleteInventory",
        data:{
            "id":dId,
            "goods_pid":dPid,
            "barcode":dBarcode,
            "amount":dAmount,
			"dRemark":delRemark
        },
        type:"post",
        success:function(data){
            var allCount=data.data.allCount;
            if(allCount>0){
                topCenter("删除库存成功");
                cance2();
                $('#easyui-datagrid').datagrid('reload');
            }else{
                topCenter("删除库存失败");
            }
        },
        error:function(e){
            topCenter("删除库存失败");
        }
    });
}

function delete_inventory(id,goods_pid,barcode,amount){
 	 $("#dId").val(id);
    $("#dPid").val(goods_pid);
    $("#dBarcode").val(barcode);
    $("#dAmount").val(amount);
    $('#dlg2').dialog('open');
    $("#delRemark").textbox('setValue','');
}

</script>
</head>
<body text="#000000" onload="$('#dlg').dialog('close');$('#dlg1').dialog('close');$('#dlg2').dialog('close');$('#dlg3').dialog('close'); doQuery(1);">
    	<div class="mod_pay3" style="display: none;" id="big_img">
			
		</div>
		<div id="dlg2" class="easyui-dialog"  title="库存删除" data-options="modal:true" style="width:400px;height:200px;padding:10px;autoOpen:false;closed:true;display: none;">
			<form id="ff2" method="post" style="height:100%;">
				<div style="margin-bottom:20px;margin-left:35px;">
					<input type="hidden" id="dId">
					<input type="hidden" id="dPid">
					<input type="hidden" id="dBarcode">
					<input type="hidden" id="dAmount">
					<input class="easyui-textbox" name="delRemark" id="delRemark"  style="width:70%;"  data-options="label:'盘点备注:'">
				</div>
				<div style="text-align:center;padding:5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="delInventorySources()" style="width:80px">提交</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance2()" style="width:80px">取消</a>
				</div>
			</form>
		</div>
	<div id="dlg" class="easyui-dialog"  title="库存盘点" data-options="modal:true" style="width:400px;height:400px;padding:10px;autoOpen:false;closed:true;display: none;">
	<form id="ff" method="post" style="height:100%;">
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="old_remaining" id="old_remaining" readonly="readonly"  style="width:70%;"  data-options="label:'当前库存:'">
				<script type="text/javascript">$(function () {$('#old_remaining').textbox('textbox').css('background','#ccc')})</script>
				<input type="hidden" id="pd_id" name="pd_id"/>
				<input type="hidden" id="flag_" name="flag_"/>
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-numberbox" name="new_remaining" id="new_remaining"  style="width:70%;"  data-options="label:'盘点数量:'">
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="old_barcode" id="old_barcode" readonly="readonly" style="width:70%;"  data-options="label:'当前库位:'">
				<script type="text/javascript">$(function () {$('#old_barcode').textbox('textbox').css('background','#ccc')})</script>
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<select class="easyui-combobox" name="new_barcode" id="new_barcode1" style="width:70%;" data-options="label:'盘点库位:',valueField: 'id',   
                    textField: 'path', value:'',
                    url: '/cbtconsole/StatisticalReport/getNewBarcode',  
                    method:'get'">
				</select>
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="remark" id="remark"  style="width:70%;"  data-options="label:'盘点备注:'">
			</div>
			<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateSources()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance()" style="width:80px">取消</a>
		</div>
		</form>
	</div>
	<div id="dlg1" class="easyui-dialog" title="手动录入库存" data-options="modal:true" style="width:400px;height:400px;padding:10px;autoOpen:false;;closed:true;display: none;">
	<form  method="post" style="height:100%;">
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-numberbox" name="goodsid" id="goodsid"  style="width:70%;"  data-options="label:'商品号:',required:true">
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-numberbox" name="count_" id="count_"  style="width:70%;"  data-options="label:'库存数量:',required:true">
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<select class="easyui-combobox" name="new_barcode2" id="new_barcode2" style="width:70%;" data-options="label:'库存库位:',required:true,valueField: 'id',   
                    textField: 'path', value:'',
                    url: '/cbtconsole/StatisticalReport/getNewBarcode',  
                    method:'get'">
				</select>
			</div>
			<div style="margin-bottom:20px;margin-left:35px;">
				<input class="easyui-textbox" name="remark_" id="remark_"  style="width:70%;"  data-options="label:'盘点备注:'">
			</div>
			<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="inventoryEntry()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance1()" style="width:80px">取消</a>
		</div>
		</form>
	</div>
		<div id="dlg3" class="easyui-dialog" title="手动录入亚马逊库存" data-options="modal:true" style="width:400px;height:450px;padding:10px;autoOpen:false;;closed:true;display: none;">
			<form  method="post" style="height:100%;">
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-numberbox" name="itmeid" id="itmeid"  style="width:90%;"  data-options="label:'商品pid:',required:true">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-numberbox" name="goods_p_price" id="goods_p_price"  style="width:90%;"  data-options="label:'商品价格:'">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-numberbox" name="ymx_count" id="ymx_count"  style="width:90%;"  data-options="label:'库存数量:',required:true">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="ymx_img" id="ymx_img"  style="width:90%;"  data-options="label:'商品图片:'">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="ymx_name" id="ymx_name"  style="width:90%;"  data-options="label:'商品名称:'">
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<select class="easyui-combobox" name="ymx_barcode2" id="ymx_barcode2" style="width:90%;" data-options="label:'库存库位:',required:true,valueField: 'id',
                    textField: 'path', value:'',
                    url: '/cbtconsole/StatisticalReport/getNewBarcode',
                    method:'get'">
					</select>
				</div>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="remark_ymx" id="remark_ymx"  style="width:90%;"  data-options="label:'盘点备注:'">
				</div>
				<div style="text-align:center;padding:5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="inventoryYmxEntry()" style="width:80px">提交</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance3()" style="width:80px">取消</a>
				</div>
			</form>
		</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div style="margin-left:10px;">
			<span style="font-size:13px;font-weight:bold">最近30天新产生的库存(数量/金额):</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_1" style="font-size:20px;width:35px;margin-right:100px">0</span>
			<span style="font-size:13px;font-weight:bold">最近30天销售掉的库存(数量/金额):</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_2" style="font-size:20px;width:35px;margin-right:100px">0</span>
			<span style="font-size:13px;font-weight:bold">最近30天产生的库存损耗(数量/金额):</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_3" style="font-size:20px;width:35px;margin-right:100px">0</span>
			<span style="font-size:13px;font-weight:bold">最近30天产生的库存删除(数量/金额):</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_4" style="font-size:20px;width:35px;margin-right:100px">0</span>
		</div>
		<div>
			<form id="query_form" action="#" onsubmit="return false;">
	              是否盘点: <select name="" id="flag" style="width: 60px;" onchange="changDiv(this.value)">
	                         <option value="-1">全部</option>
		                     <option value="1">已盘点</option>
					         <option value="0">未盘点</option>
					         <option value="2">问题库存</option>
	                   </select>
	                   <div style="display: none">
	                    <select name="" id="type">
		                     <option value="remaining">库存</option>
	                      </select>
	                       <select name="" id="scope">
		                     <option value="0">ALL</option>
		                     <option value="1">大于</option>
					         <option value="2">等于</option>
					         <option value="3">小于</option>
	                      </select>
	                      <input type="text" id="count"/>
	                   </div>
	                      商品信息：<input type="text" id="goodinfo">
						  商品PID：<input type="text" id="goods_pid">
	                      商品规格：<input type="text" id="sku">
	                      商品库位：<input type="text" id="barcode" onkeypress="if (event.keyCode == 13) searchExport(1);">
	                      <select class="easyui-combobox" name="have_barcode" id="have_barcode" style="width:12%;" data-options="label:'有库存库位:',panelHeight:'400px',valueField: 'barcode',   
                    textField: 'barcode', value:'全部',selected:true,
                    url: '/cbtconsole/StatisticalReport/getHavebarcode',  
                    method:'get'">
				</select>
					<select class="easyui-combobox" name="valid" id="valid" style="width:10%;" data-options="label:'是否上架:',panelHeight:'auto'">
						<option value="-1" selected="selected">所有</option>
						<option value="1">上架</option>
						<option value="0">下架</option>
						<option value="2">ali商品</option>
						<option value="3">亚马逊商品</option>
					</select>
				<select class="easyui-combobox" name="goodscatid" id="goodscatid" style="width:15%;" data-options="label:'库存商品类别:',Height:'2000px',valueField: 'goodscatid',
                    textField: 'goodscatid', value:'全部',selected:true,
                    url: '/cbtconsole/StatisticalReport/getAllInventory',
                    method:'get'">
				</select>
	         <div id="pandian_time" style="display: none">
	              	              盘点时间:<input id="startdate"
						name="startdate" readonly="readonly"
						onfocus="WdatePicker({isShowWeek:true})"
						value="${param.startdate}"
						onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />-
						<input id="enddate" name="enddate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})"
						value="${param.startdate}"
						onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}'  style="margin-laft:5px;"/>
	           </div>
				<br> <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<a href="javascript:exportData();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">导出</a>
		<a href="javascript:openInventoryEntryView();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">库存录入</a>
		<a href="javascript:openYmxInventoryEntryView();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">亚马逊库存录入</a>
		<a href="/cbtconsole/website/inventory_update_log.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">库存盘点日志</a>
<!-- 		<a href="/cbtconsole/website/inventory_update_log.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">库存盘点记录</a> -->
		<a href="/cbtconsole/website/inventory_delete_log.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">库存删除记录</a>
		<a href="/cbtconsole/website/loss_inventory_log.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">库存损耗列表</a>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'goodscatid',width:50,align:'center'">商品品类</th>
				<th data-options="field:'onLine',width:50,align:'center'">是否上架</th>
				<th data-options="field:'good_name',width:50,align:'center'">商品名称</th>
				<th data-options="field:'barcode',width:80,align:'center'">商品库位</th>
				<th data-options="field:'sku',width:50,align:'center'">商品规格</th>
				<th data-options="field:'car_img',width:80,align:'center'">商品图片</th>
				<th data-options="field:'goods_p_price',width:50,align:'center'">采购价</th>
				<th data-options="field:'remaining',width:50,align:'center'">首次库存数量</th>
				<th data-options="field:'inventory_amount',width:60,align:'center'">首次库存金额</th>
				<th data-options="field:'new_remaining',width:40,align:'center'">盘点后库存数量</th>
				<th data-options="field:'new_inventory_amount',width:50,align:'center'">盘点后金额</th>
				<th data-options="field:'can_remaining',width:40,align:'center'">可用库存数量</th>
				<th data-options="field:'createtime',width:60,align:'center'">首次库存录入时间</th>
				<th data-options="field:'updatetime',width:60,align:'center'">最后更新库存时间</th>
				<th data-options="field:'remark',width:60,align:'center'">备注</th>
				<th data-options="field:'editLink',width:60,align:'center'">产品编辑链接</th>
				<th data-options="field:'unsellableReason',width:60,align:'center'">下架原因</th>
				<th data-options="field:'operation',width:50,align:'center'">盘点</th>
			</tr>
		</thead>
	</table>
		<script type="text/javascript">
            initData();
		</script>
</body>
</html>