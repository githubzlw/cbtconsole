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
<title>取消OCR识别错误图片</title>
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
#Img1{ width:150px; height:150px;}/*
#Img1:hover{ width:300px; height:300px;position: absolute;}*/

/*div{
	width: 300px;
	height: 300px;
	border: #000 solid 1px;
	margin: 50px auto;
	overflow: hidden;
}
div img{
	cursor: pointer;
	transition: all 0.6s;
}*/
/*div img:hover{
	transform: scale(1.5);
}*/
.div{
	float: left; padding: 10px;
	border: 2px solid aquamarine;
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
    var pid = $("#pid").val();
    var shopid = $("#shopid").val();
    var isdelete = $("#isdelete").val();
    window.location.href="/cbtconsole/Distinguish_Picture/FindCustomGoodsInfo?page="+page+"&pid="+pid+"&shopid="+shopid+"&isdelete="+isdelete;}


function search(){
	var pid = $("#pid").val();
	var shopid = $("#shopid").val();
	var isdelete = $("#isdelete").val();
	window.location.href="/cbtconsole/Distinguish_Picture/FindCustomGoodsInfo?pid="+pid+"&shopid="+shopid+"&isdelete="+isdelete;
}



function  reset(){
	$(".selectText").val('');
	$("input[type='text']").val('');
}


function  update(ocrneeddelete,id){
			$.ajax({
				type:'post',
				url:"${ctx}/Distinguish_Picture/updateSomeis_delete",
				data:{id:id/*,ocrneeddelete:ocrneeddelete*/},
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
}

/* 全选  */
function fnselect(){
	if($("#checked").prop("checked") == true){
		$("input[class='cbox']").prop('checked',true );//全选
		$("#table:not(:first)").each(function(){
			  if($(this).css("display")=="none"){
				  $(this).find("input[class='cbox']").prop('checked',false);
			  }
		});
	}else{
		$("input[class='cbox']").prop('checked',false);//反选
	} 
}



function  updateSomes(){
	var  mainMap ={};
	var erList= new Array();  
	var id = "";
	var ocrneeddelete ="";
    var sbi = 0;
	$(".cbox:checked").each(function(){
		  var erMap={}; 
		  erMap['id'] =this.value;
		  erMap['ocrneeddelete'] = $("#ocrneeddelete"+this.value).val();
		  erList[sbi] = erMap;
		  sbi++;  		  
	});
	if(erList.length == 0){
		alert("请至少选择一个！");
	}else{
		mainMap['bgList'] = erList;
		console.log(mainMap);            
	 	$.ajax({
			type:"post",
			url:"${ctx}/Distinguish_Picture/updateSomeis_delete",
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
<h1 align="center"><b>取消OCR识别错误图片</b></h1>
<h3 align="center" ><font color="red" id="tip"></font></h3>
	<div class="main">
		<div class="main-head"></div>
		<div class="main-all">
			<div class="main-float">
				<div class="main-top">
					<div class="left">
						<span class="wenzi">商品编号：</span> <input type="text" id="pid"  value="${pid }" class="inputText" />
					</div>
					<div class="left left-margin">
						<span class="wenzi">商铺编号：</span> <input type="text" id="shopid"  value="${shopid }" class="inputText" />
						<span style="color: red">(用于人工进行检查OCR程序对图片的识别错误更正)</span>
					</div>
				</div>
				<div class="main-top margin2">
					
					<div class="left">
						 <span class="wenzi">是否删除</span> <select   id="isdelete" class="selectText">
							<option value=""  <c:if test="${isdeleteNo=='2'}">selected</c:if>></option>
							<option value="0" <c:if test="${isdeleteNo=='0'}">selected</c:if>>未删除</option>
							<option value="1" <c:if test="${isdeleteNo=='1'}">selected</c:if>>删除</option>
						</select>
					</div>
					<div class="left left-margin">
						<span class="wenzi"  onclick="search();"><a href="#" style="text-decoration:none"><font color="white">查询</font></a></span> <span class="wenzi"  onclick="reset();"><a href="#" style="text-decoration:none"><font color="white">重置</font></a></span>
						<br/><span class="wenzi"  onclick="updateSomes()" ><a href="#" style="text-decoration:none"><font color="white">批量纠正</font></a></span>
					</div>
				</div>
			</div>
		</div>
		<div class="left left-margin">
			<label><input type="checkbox" class="checkbox-all"   id="checked" onclick="fnselect()" style="width: 30px; height: 30px;">全选</label>
		</div>
		</div>
		<div class="main-table">
			<table class="table">
				<%--<tr>
				<th><label><input type="checkbox" class="checkbox-all"   id="checked" onclick="fnselect()">全选</label></th>
				&lt;%&ndash;<th>序号</th>
                <th>商品编号</th>
                <th>商铺编号</th>
                <th>本地图片地址</th>
                <th>商品照片</th>
                <th>商品标识码</th>
                <th>图片扫描时间</th>
                <th>线上状态</th>
                <th>是否线上删除</th>&ndash;%&gt;
			</tr>--%>
				<c:forEach  var="customGoodsList"  items="${customGoodsList }"  varStatus="status">
					<div class="div">
						<img src="${customGoodsList.remotepath }" style="width:200px; height:200px;">
						<input type="checkbox"   class="cbox"  class="id"  value="${customGoodsList.id }" style="width: 30px; height: 30px;"/>
					</div>
<%--				 <tr>
					<td><input type="checkbox"   name="cbox"  class="id"  value="${customGoodsList.id }"/></td>
					 <td>${status.index+1 }</td>
					 <td>${customGoodsList.pid }</td>
					<td>${customGoodsList.shopid }</td>
					<td>${customGoodsList.localpath }</td>
					 <td ><img src="${customGoodsList.remotepath }" id="Img1"></img></td>
					 <td>${customGoodsList.goodsmd5 }</td>
					 <td>${customGoodsList.createtime }</td>
					<td>
					 <c:if test="${customGoodsList.isdelete==0 }">
					   <span><font color="red">未删除</font></span>
					 </c:if>
					  <c:if test="${customGoodsList.isdelete==1 }">
					   <span>已删除</span>
						</c:if>
					</td>
					<td name="ocrneeddelete">&lt;%&ndash;update()&ndash;%&gt;
						<c:if test="${customGoodsList.ocrneeddelete==2 || customGoodsList.ocrneeddelete==0}">
						<a href="#" class="a-link" onclick="update(${customGoodsList.ocrneeddelete},${customGoodsList.id })">标记为待删除</a>
						</c:if>
					 <c:if test="${customGoodsList.ocrneeddelete==1 }">
					 <span>已标记为待删除</span><br/><a href="#" onclick="update(${customGoodsList.ocrneeddelete},${customGoodsList.id })"
											 class="a-link">取消待删除</a>
					</td>
					 </c:if>
				</tr>--%>
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
