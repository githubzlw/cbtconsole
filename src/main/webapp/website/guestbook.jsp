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
<title>Business inquiries</title>
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
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
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
	//取出当前登录用户
   String sessionId = request.getSession().getId();
    String userJson = Redis.hget(sessionId, "admuser");
	Admuser adm =(Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class); 
	int userid=adm.getId();
	%>
<script type="text/javascript">
/* 获取url中参数 */
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}
$(function(){
// 	document.onkeydown = function(e){
// 		var ev = document.all ? window.event : e; 
// 		if(ev.keyCode==13) {
// 			 var number=$('#easyui-datagrid').datagrid('options').pageNumber;
// 			 doQuery(number);
// 		}
// 	} 
	$('#timeFrom').datebox({
		 closeText:'关闭',  
		 formatter:function(date){  
		  var y = date.getFullYear();  
		  var m = date.getMonth()+1;  
		  var d = date.getDate();  
		  var h = date.getHours();  
		  var M = date.getMinutes();  
		  var s = date.getSeconds();  
		  function formatNumber(value){  
		   return (value < 10 ? '0' : '') + value;  
		  }  
		  return y+'-'+m+'-'+d;  
		 },  
		 parser:function(s){  
		  var t = Date.parse(s);  
		  if (!isNaN(t)){  
		   return new Date(t);  
		  } else {  
		   return new Date();  
		  }  
		 }  
		});
	$('#timeTo').datebox({  
		 closeText:'关闭',  
		 formatter:function(date){  
		  var y = date.getFullYear();  
		  var m = date.getMonth()+1;  
		  var d = date.getDate();  
		  var h = date.getHours();  
		  var M = date.getMinutes();  
		  var s = date.getSeconds();  
		  function formatNumber(value){  
		   return (value < 10 ? '0' : '') + value;  
		  }  
		  return y+'-'+m+'-'+d;  
		 },  
		 parser:function(s){  
		  var t = Date.parse(s);  
		  if (!isNaN(t)){  
		   return new Date(t);  
		  } else {  
		   return new Date();  
		  }  
		 }  
		});
	$('#dlg').dialog('close');
})

function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : 'Business inquiries',
			//iconCls : 'icon-ok',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 30,//默认选择的分页是每页20行数据
			pageList : [ 30],//可以选择的分页集合
			nowrap : false,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
//			url : '/cbtconsole/StatisticalReport/findAll',//url调用Action方法
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
	//var data = $("#data").val();
	var status=$('#status').combobox('getValue');
	var questionType=$('#questionType').combobox('getValue');
	var userid=$("#userid").val();
	var adminId=$("#adminId").combobox('getValue');
	var username = $("#username").val();
	var pname = $("#pname").val();
	var useremail=$("#useremail").val();
	var timeFrom = $("#timeFrom").val();
	var timeTo=$("#timeTo").val();
	$("#easyui-datagrid").datagrid("load", {
		"page":page,
    	//"createTime":data,
    	"status":status,
    	"questionType":questionType,
    	"userId":userid,
    	"adminId":adminId,
    	"userName":username,
    	"pname":pname,
    	"useremail":useremail,
    	"timeFrom":timeFrom,
    	"timeTo":timeTo
	});
}

var delreply=function(id){
	$.messager.confirm('提示', '确定删除该留言信息吗?', function(r){
		if (r){
			$.ajax({
	            url:'/cbtconsole/customerServlet?action=delreply&className=GuestBookServlet',  
	            type:"post",  
	            data:{id:id},  
	            success:function(res){
	            	if(res <= 0){
	            		$.messager.alert('提示','操作失败');
	       	  		}else{
	       	  		    $.messager.alert('提示','操作成功');
// 	       	  		    var number=$('#easyui-datagrid').datagrid('options').pageNumber;
// 		       	  		setTimeout(function(){
// 		       	  		doQuery(number);
// 		       	 		}, 2000)
	       	  		$('#easyui-datagrid').datagrid('reload');
	       	  		}
	            }, 
	            error:function(){
	            	$.messager.alert('提示','操作失败');
	            }
	        });
		}
	});
};


// 客户消息回复
function updateReply(){
	 var id= $("#gbookid").val();
	 var replyContent= $("#replyContent1").val();
	 if(replyContent==null||replyContent==""){
		 $.messager.alert('提示','请输入回复内容');
		 return false;
	 }
	 var params = {  
   			"id":id,
   			"replyContent":replyContent,
   			"className":"GuestBookServlet",
   			"action":"reply"
   		};
      $.ajax({  
         url:'/cbtconsole/customerServlet',  
         type:"post",  
         data:params,  
         success:function(data){
       	if(data < 0){
       		$.messager.alert('提示','操作失败');
 	  		}else{
 	  			$('#dlg').dialog('close');
 	  			$("#replyContent1").textbox('setValue','');
//  	  		 	var number=$('#easyui-datagrid').datagrid('options').pageNumber;
//     	  		setTimeout(function(){
//     	  		doQuery(number);
//     	 		}, 2000)
 	  			$('#easyui-datagrid').datagrid('reload');
 	  		}
         }, 
     }); 
}

function doReset(){
	$('#query_form').form('clear');
	$('#status').combobox('setValue','-1');
	$('#questionType').combobox('setValue','0');
}

//回复留言问题
function reply(id,userId,reply_userName,reply_content,reply_pname,reply_email,reply_onlineUrl){
	$('#dlg').dialog('open');
	$("#replyContent1").textbox('setValue','');
	$('#replyContent1').textbox('textbox').focus();
	$("#gbookid").val(id);

};

function closeUploadDialog1() {
    $('#dlg').dialog('close');
    $("#uploadFileForm1")[0].reset();
}

function uploadTypePictures() {
    var formData = new FormData($("#uploadFileForm1")[0]);
    $.messager.progress({
        title : '提交数据中中',
        msg : '请等待...'
    });
    $.ajax({
        url : '/cbtconsole/warehouse/reply',
        type : 'POST',
        data : formData,
        contentType : false,
        processData : false,
        success : function(data) {
            $.messager.progress('close');
            if (data.ok) {
                $('#dlg').dialog('close');
                $("#replyContent1").textbox('setValue','');
                $('#easyui-datagrid').datagrid('reload');
                $("#uploadFileForm1")[0].reset();
            } else {
                $.messager.alert('提示','操作失败');
            }
        },
        error : function(XMLResponse) {
            $.messager.progress('close');
            $.messager.alert('提示','操作失败');
        }
    });
}

function BigImg(img){
    htm_="<img width='400px' src="+img+">";
    $("#big_img").append(htm_);
    $("#big_img").css("display","block");
}

function closeBigImg(){
    $("#big_img").css("display","none");
    $('#big_img').empty();
}

$(function(){
    //负责人下拉框
    $.ajax({
        type : 'POST',
        async : false,
        url : '/cbtconsole/admuser/queryAllAdmuser',
        success : function(data){
            if(data.ok){
                var dataArr = [];
                dataArr.push({'id':'0','text':'全部',"selected":'true'});
                var results = data.data;
                for (var i = 0; i < results.length; i++) {
                    dataArr.push({'id':results[i].id,'text':results[i].admname});
                }
                $('#adminId').combobox("loadData", dataArr);
            } else{
                alert("获取下拉框数据失败，请重新刷新页面");
            }
        }
    });

    //页面加载完后 通过url中指定参数查询结果
    setDatagrid();
    var opts = $("#easyui-datagrid").datagrid("options");
    opts.url = "/cbtconsole/StatisticalReport/findAll";
    if(getUrlParam("status") != undefined && getUrlParam("status") != 'null') {
        $("#status").combobox('select',getUrlParam("status"));
    }
    if(getUrlParam("adminId") != undefined && getUrlParam("adminId") != 'null') {
        $('#adminId').combobox("setValue", getUrlParam("adminId"));
    }
    $("#questionType").combobox('select',getUrlParam("questionType"));
    doQuery(1);
})

</script>
</head>
<body>
<div class="mod_pay3" style="display: none;" id="big_img">

</div>
	<div id="dlg" class="easyui-dialog" title="回复内容" data-options="modal:true" style="width:450px;height:250px;padding:10px;">
		<form id="uploadFileForm1" method="post" enctype="multipart/form-data">
			<input type="hidden" name="gbookid" id="gbookid"/>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" style="width:100%;height:60px" name="replyContent1" id="replyContent1" data-options="multiline:true">
			</div>
			<div style="margin-bottom: 20px">
				图&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;片:<input
					id="local_picture1" name="uploadfile1" class="easyui-filebox"
					data-options="prompt:'...'"
					style="width: 360px">
			</div>
		</form>
		<div style="text-align: center; padding: 5px 0">
			<a href="javascript:void(0)" data-options="iconCls:'icon-add'"
			   class="easyui-linkbutton"
			   onclick="uploadTypePictures()" style="width: 80px">提交回复</a>
			<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
			   class="easyui-linkbutton" onclick="closeUploadDialog1()"
			   style="width: 80px">关闭</a>
		</div>
		<%--<div style="text-align:center;padding:5px 0">--%>
			<%--<a href="javascript:void(0)" class="easyui-linkbutton" onclick="updateReply()" style="width:80px">提交回复</a>--%>
			<%--<a href="javascript:void(0)" class="easyui-linkbutton" onclick="$('#dlg').dialog('close');" style="width:80px">关闭</a>--%>
		<%--</div>--%>
	</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<form id="query_form" action="#" style="margin-left:350px;" onsubmit="return false;">
				<input class="easyui-textbox" name="userid" id="userid" style="width:15%;margin-top: 10px;"  data-options="label:'userid:'">
				<select class="easyui-combobox" name="status" id="status" style="width:15%;" data-options="label:'回复状态:',panelHeight:'auto'">
				<option value="-1" selected>全部</option>
				<option value="0">未回复</option>
				<option value="1">已回复</option>
				</select>
				<select class="easyui-combobox" name="questionType" id="questionType" style="width:15%;height: 30px;" data-options="label:'type:',panelHeight:'auto'">
				<option value="0" selected>全部</option>
				<option value="1">问题</option>
				<option value="2">商业折扣</option>
				<option value="3">定制</option>
				</select>
				<span style="font-weight: bold;">日期:</span>
				<input class="easyui-datebox" name="timeFrom" id="timeFrom" style="width:100px;">
				<span>~</span>
				<input class="easyui-datebox" name="timeTo" id="timeTo" style="width:100px;">
				<br><br>
                <select class="easyui-combobox" name="adminId" id="adminId" style="width:15%;height: 30px;"
                        data-options="label:'负责人:',panelHeight:'auto',valueField: 'id',textField: 'text'">
                </select>
				<input class="easyui-textbox" name="username" id="username" style="width:15%;"  data-options="label:'username:'">
				<input class="easyui-textbox" name="pname" id="pname" style="width:15%;"  data-options="label:'pname:'">
				<input class="easyui-textbox" name="useremail" id="useremail" style="width:15%;"  data-options="label:'useremail:',validType:'email'">
				<%-- <input type="hidden" name="timeFrom" id="timeFrom" value="${param.timeFrom}"/>
				<input type="hidden" name="timeTo" id="timeTo" value="${param.timeTo}"/> --%>
				 <input class="but_color" type="button" value="查询" onclick="doQuery(1)"> 
				 <input class="but_color" type="button" value="重置" onclick="doReset()">
			</form>
		</div>
		<%-- <a href="http://192.168.1.154:80/cbtemail/emailInfo/selectall?userid=<%=userid%>" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">查询客户回信</a> --%>
	</div>
	
		<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
		<thead>	
			<tr>
				<th data-options="field:'userInfos',width:30,align:'center'">userid</th>
				<th data-options="field:'email',width:50,align:'center'">email</th>
				<th data-options="field:'pid',width:30,align:'center'">pid</th>
				<th data-options="field:'pname',width:30,align:'center'">pname</th>
				<th data-options="field:'content',width:100,align:'center'">content</th>
				<th data-options="field:'createTime',width:30,align:'center'">create_time</th>
				<th data-options="field:'replyContent',width:100,align:'center'">reply_content</th>
				<th data-options="field:'replyTime',width:30,align:'center'">reply_time</th>
				<th data-options="field:'picPath',width:40,align:'center'">回复图片</th>
				<th data-options="field:'statusinfo',width:30,align:'center'">state</th>
				<th data-options="field:'admname',width:30">负责人</th>
			</tr>
		</thead>
	</table>
</body>
</html>