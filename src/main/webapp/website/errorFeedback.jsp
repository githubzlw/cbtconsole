<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>异常问题反馈管理界面</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<!-- <link rel="stylesheet" href="/cbtconsole/js/bootstrap/bootstrap.min.css"> -->
<style type="text/css">
.table {
	margin-left: 20px;
}
</style>
<script type="text/javascript"
	src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String date = sdf.format(new Date());
%>
<style type="text/css">
* {
	padding: 0;
	margin: 0;
	list-style: none;
}

html, body {
	height: 100%;
	font-family: "微软雅黑", Helvetica, Arial, sans-serif;
	color: #333;
	min-width: 1200px;
	font-size: 14px;
}


.main-top {
	overflow: hidden;
	padding-left: 50px;
}

.left {
	display: inline-block;
}
.main-center  .wenzi {
    margin-left: 55px;
}

.wenzi {
	display: inline-block;
	height: 32px;
	line-height: 32px;
	width: 100px;
	text-align: center;
	background: #629fd6;
	color: #fff;
	border-radius: 4px;
	letter-spacing: 2px;
	margin:5px;
}

.inputText, .selectText {
	display: inline-block;
	width: 200px;
	height: 30px;
	line-height: 30px;
	outline: none;
}

.left-margin {
	margin-left: 40px;
}

div.margin2 {
	margin-top: 20px;
}

.main-float {
	float: left;
	margin-left:50px;
	margin-top:50px;
}

.main-right {
	float: left;
	display: inline-block;
	margin-top: 90px;
	margin-left: 50px;
}

.main-right span {
	cursor: pointer;
}

.main-all {
	overflow: hidden;
}

.main-center {
    margin-left: 75px;
	margin-top: 50px;  
}

.main-table {
	margin: 0 55px;
	margin-bottom: 50px;
}

.table {
	width: 100%;
	border: 1px solid #ddd;
	border-collapse: separate;
	border-spacing: 0;
	border-collapse: collapse;
}

.table td, .table th {
	border: 1px solid #ddd;
	line-height: 30px;
	height: 30px;
	vertical-align: top;
	text-align: center;
}

.table th {
	background: #629fd6;
	color: #fff;
}

.a-link {
	display: inline-block;
	margin: 0 10px;
	color: #0000ff;
}
</style>

<script type="text/javascript">

function fnjump(obj){
	var page=$("#page").val();
	if(page==""){
		page = "1";
	}
	if(obj==-1){//前一页
		if(parseInt(page)<2){
			return ;
		}
		page = parseInt(page)-1;
	}else if(obj==1){//下一页
		if(parseInt(page)>parseInt($("#totalpage").val())-1){
			return ;
		}
		page = parseInt(page)+1;
	}else if(obj < -1){
		page=1;
	}
	
	$("#page").val(page);
	var type = $("#mokuai").val();
	var belongTo = $("input[name='belongTo']").val();
	var startTime = $("input[name='startTime']").val();
	var endTime = $("input[name='endTime']").val();
	var delFlag = $("#delFlag").val();
	window.location.href="/cbtconsole/errorFeedbackController/showErrorFb?page="+page+"&type="+type+"&belongTo="+belongTo+"&startTime="+startTime+"&endTime="+endTime+"&delFlag="+delFlag;
}


function search(){
	var type = $("#mokuai").val();
	var belongTo = $("input[name='belongTo']").val();
	var startTime = $("input[name='startTime']").val();
	var endTime = $("input[name='endTime']").val();
	var delFlag = $("#delFlag").val();
	window.location.href="/cbtconsole/errorFeedbackController/showErrorFb?type="+type+"&belongTo="+belongTo+"&startTime="+startTime+"&endTime="+endTime+"&delFlag="+delFlag;
}



function  reset(){
	$(".selectText").val('');
	$("input[type='text']").val('');
}


function  update(){
	$("table tr").each(function(){
		$(this).click(function(){
			var remark = $(this).find("input[name='remark']").val();
			var id = $(this).find("input[name='id']").val();
			$.ajax({
				type:'post',
				url:"${ctx}/errorFeedbackController/updateErrorFlag",
				data:{id:id,remark:remark},
				dataType:"json",
				success:function(res){
					if(res==1){
						$("#tip").html("执行成功 !");
						window.location.reload();
					}else{
						$("#tip").html("执行失败  !")
					}
				}
			})
		})
	})
}

/* 全选  */
function fnselect(){
	if($("#checked").prop("checked") == true){
		$("input[name='cbox']").prop('checked',true );//全选 
		$("#table tr:not(:first)").each(function(){
			  if($(this).css("display")=="none"){
				  $(this).find("input[name='cbox']").prop('checked',false);
			  }
		});
	}else{
		$("input[name='cbox']").prop('checked',false);//反选  
	} 
}



function  updateSomes(){
	var  mainMap ={};
	var erList= new Array();  
	var checked = "";
	var remarks ="";
    var sbi = 0;
	$(".checkpid:checked").each(function(){
		  var erMap={}; 
		  erMap['id'] =this.value;
		  erMap['remark'] = $("#remark"+this.value).val();
		  erList[sbi] = erMap;
		  sbi++;  		  
	});
	if(erList.length == 0){
		alert("请至少选择一个 ！");
	}else{
		mainMap['bgList'] = erList;
		console.log(mainMap);            
	 	$.ajax({
			type:"post",
			url:"${ctx}/errorFeedbackController/updateSomeErrorFlag",
			dataType:"json",
			contentType : 'application/json;charset=utf-8', 
		    data:JSON.stringify(mainMap),
			success:function(res){
				if(res==0){
					$("#tip").html("执行失败  !")
				}else{
					$("#tip").html("执行成功 !");
					window.location.reload();
				}
			}
		})  
	}
	
}
</script>
</head>
<body>
<h1 align="center"><b>问题反馈监控界面</b></h1>
<h3 align="center" ><font color="red" id="tip"></font></h3>
	<div class="main">
		<div class="main-head"></div>
		<div class="main-all">
			<div class="main-float">
				<div class="main-top">
					<div class="left">
						<span class="wenzi">模块：</span> <select name="type"  id="mokuai" class="selectText"  >
							<option value="" <c:if test="${type==''}">selected</c:if>>请选择</option>
							<option value="1" <c:if test="${type=='1'}">selected</c:if>>物流追踪</option>
							<option value="2" <c:if test="${type=='2'}">selected</c:if>>邮件</option>
						</select>
					</div>
					<div class="left left-margin">
						<span class="wenzi">负责人：</span> <input type="text" name="belongTo"  value="${belongto }"
							class="inputText" />
					</div>
				</div>
				<div class="main-top margin2">
					<div class="left">
						<span class="wenzi">开始时间：</span> <input type="text"  name="startTime"
							class="Wdate" style="width: 110px;height: 26px"  value="${startTime }" onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})" />
					</div>
					<div class="left left-margin">
						<span class="wenzi">结束时间：</span> <input type="text"  name="endTime"
							class="Wdate" style="width: 110px;height: 26px" type="text" value="${endTime }" onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"  />
					</div>
					
					<div class="left left-margin">
						 <span class="wenzi">是否解决</span> <select name="delFlag"  id="delFlag" class="selectText">
							<option value=""  <c:if test="${deFlag=='2'}">selected</c:if>></option>
							<option value="0" <c:if test="${deFlag=='0'}">selected</c:if>>待修复</option>
							<option value="1" <c:if test="${deFlag=='1'}">selected</c:if>>已完成</option>
						</select>
					</div>
					<div class="left left-margin">
						<span class="wenzi"  onclick="search();"><a href="#" style="text-decoration:none"><font color="white">查询</font></a></span> <span class="wenzi"  onclick="reset();"><a href="#" style="text-decoration:none"><font color="white">重置</font></a></span>
					</div>
				</div>
			</div>
		</div>
		<div style="margin-left:100px;"><span class="wenzi"  onclick="updateSomes()" ><a href="#" style="text-decoration:none"><font color="white">批量执行</font></a></span></div>
		<div class="main-table">
			<table class="table">
				<tr>
					<th><label><input type="checkbox" class="checkbox-all"   id="checked" onclick="fnselect()">全选</label></th>
					<th>序号</th>
					<th>模块名</th>
					<th>发生时间</th>
					<th>错误内容</th>
					<th>负责人</th>
					<th>错误状态</th>
					<th>备注</th>
					<th>操作</th>
				</tr>
				<c:forEach  var="errorFeedBack"  items="${list }"  varStatus="status">
				 <tr>
					<td><input type="checkbox"   name="cbox"  class="checkpid"  value="${errorFeedBack.id }"/></td>
					<td>${status.index+1 }</td>
					<td>
					   <c:if test="${errorFeedBack.type==1 }">
					       物流追踪
					   </c:if>
					    <c:if test="${errorFeedBack.type==2 }">
					       邮件 
					   </c:if>
					</td>
					<td>${errorFeedBack.createtime }</td>
					<td>${errorFeedBack.content }</td>
					<td>${errorFeedBack.belongTo }</td>
					<td>
					 <c:if test="${errorFeedBack.delFlag==0 }">
					   <span><font color="red">待修复</font></span>
					 </c:if>
					  <c:if test="${errorFeedBack.delFlag==1 }">
					   <span>已完成</span>
					 </c:if>
					</td>
					<td>
					<input  type="text"  name="remark"  id="remark${errorFeedBack.id }"  value="${errorFeedBack.remark}">
					</td>
					<td>
					<input  type="hidden"  name="id"  value="${errorFeedBack.id }">
					<a href="#" class="a-link" >下载日志</a><a href="#"  onclick="update()"
						class="a-link">已处理</a></td>
				</tr>
				</c:forEach>
			</table>
		</div>
		
		<br>
		<div  style="margin-left:650px">
		<input type="hidden" id="totalpage" value="${totalpage}">
		
		总共:&nbsp;&nbsp;<span id="pagetotal">${currentPage}<em>/</em> ${totalpage}</span>
		页&nbsp;&nbsp;
		<input type="button" value="上一页" onclick="fnjump(-1)" class="btn">
		<input type="button" value="下一页" onclick="fnjump(1)" class="btn">
		
		第<input id="page" type="text" value="${currentPage}" style="height: 26px;">
		<input type="button" value="查询" onclick="fnjump(0)" class="btn">
		</div>
	</div>
</body>
</html>
