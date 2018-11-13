<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String date = sdf.format(new Date());
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>网红管理</title>

<link rel="stylesheet" href="/cbtconsole/js/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/style.css">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" 	src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<script type="text/javascript" src="/cbtconsole/js/website/jquery.paginate.js"></script>
</head>
<style type="text/css">
.demo{
                width:580px;
                padding:10px;
                margin:10px auto;
                border: 1px solid #fff;
                background-color:#f7f7f7;
            }
.grey_bj{ position:fixed;width:100%;height:100%;background:	#E0E0E0;opacity:0.6;z-index: 1;display:none;}
.tank{width:450px; height:500px; padding-bottom:20px;background-color:#ffffff;position:fixed;top:15%;left:40%;border:2px solid #EEEEE0;border-radius:10px;-webkit-border-radius:10px;z-index: 2;display:none;}
.thank_top{width:96%;border-bottom:1px  dashed #ccc;margin:auto;text-align:right;padding-bottom: 10px;display:none;}
.close_1{color:#f2350c;font-size: 20px;font-weight: bold;text-decoration: none;padding-right: 3px;}
.redNetInfo{width:400px;height:400px;}
.redNetInfo tr td {text-align:center;}
</style>
<script type="text/javascript">
$(function(){
	  
	searchRedNets(1);
	  
	  $("#back").click(function(){
		 window.location.href="${ctx}/website/redNetStatistics.jsp" ;
	  })
	
	 $(".close_1").click(function() {
			$("#bg").css("display","none");
			$("#share").css("display","none");
			$(".tanchu").css("display","none");
			$(".thank_top").css("display","none");
     });
	 
	 //add redNet
	 $("#submit").click(function(){
		 var  id  = $("input[name='id']").val();
		 var redNetName =  $("input[name='redNetName']").val();
		 var  site =  $("input[name='site']").val();
		 var  tomosonUrl =  $("input[name='tomosonUrl']").val();
		 var  pushSum =  $("input[name='pushSum']").val();
		 var  redNetOffer =  $("input[name='redNetOffer']").val();
		 var  email =  $("input[name='email']").val();
		 var cooperationTime  =  $("input[name='cooperationTime']").val();
		 var bz =  $("input[name='bz']").val();
		 if(redNetName==""){
			 alert("网红名称不能为空!");
		 }else{
			 $("#bg").css("display","none");
			 $("#share").css("display","none");
			 $.ajax({
				 type:"post",
				 url:"${ctx}/redNet/addRedNet",
				 data:{id:id,redNetName:redNetName,site:site,tomosonUrl:tomosonUrl,pushSum:pushSum,redNetOffer:redNetOffer,email:email,cooperationTime:cooperationTime,bz:bz,cooperationTime:cooperationTime}, 
				 dataType:"json",
				 success:function(res){
					if(res==1){
						alert("添加/修改成功!")
						window.location.reload();
					} 
					else  if(res==2){
						$.dialog.alert("Message","数据不能重复!");
						$(".form-control").val('');
					}else{
						$.dialog.alert("Message","系统错误,请联系IT!");
						$(".form-control").val('');
					}
				 }
			 })
		 }
	 })
	 
})

var curPage;
	function searchRedNets(page) {
		curPage = page;
		var redNetId = $("#redNetId").val();
		var redNetName = $("#redNetName").val();
		var site = $("#site").val();
		$("#shipmentList tbody").html("");
		$("#shipmentList tfoot").html("");
		$.ajax({
			type:'post',
			url:'${ctx}/redNet/showRedNetInfo',
			data:{redNetId:redNetId,redNetName:redNetName,site:site,page:page},
			dataType:'json',
			success:function(res){
					var tabStr = "";
					for (var i = 0; i < res.length; i++) {
						var obj = res[i];
						tabStr+=("<tr>");
						tabStr+=("<td>"+(i+1)+"</td>");
						tabStr+=("<td>"+obj.id+"</td>");
						tabStr+=("<td>"+obj.redNetName+"</td>");
						tabStr+=("<td>"+obj.site+"</td>");
						tabStr+=("<td>"+obj.tomosonUrl+"</td>");
						tabStr+=("<td>"+obj.pushSum+"</td>");
						tabStr+=("<td>"+obj.redNetOffer+"</td>");
						tabStr+=("<td>"+obj.email+"</td>");
						tabStr+=("<td>"+obj.cooperationTime+"</td>");
						tabStr+=("<td>"+obj.bz+"</td>");
						tabStr+=("<td><a href='#' class='edit'  onclick='edit("+obj.id+")'>编辑</a><input type='hidden'  name='id'  value="+obj.id+"></td>");
						tabStr+=("</tr>");
					}
					$("#shipmentList tfoot").append("<tr><td colspan='11'>总条数:"+res[0].count+"&nbsp;当前页:<span>"+res[0].currentPage+"</span>&nbsp;总页数:"+res[0].totalPage+"<div class='demo'><div id='demo1'></div></div></td></tr>");
					$("#demo1").paginate({
						count 		: res[0].totalPage,
						start 		: curPage,
						display     : 8,
						border					: true,
						border_color			: '#fff',
						text_color  			: '#fff',
						background_color    	: '#239ed7',	
						border_hover_color		: '#ccc',
						text_hover_color  		: '#239ed7',
						background_hover_color	: '#fff', 
						images					: false,
						mouse					: 'press'
					});
				 
				$("#shipmentList tbody").append(tabStr);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				$.dialog.alert("Message","系统错误,请联系开发人员确认问题!");
			}
		});
	}
	 
	function resetCondition() {
		$("input[name='redNetId']").val('');
		$("input[name='redNetName']").val('');
		$("input[name='site']").val('');
		$(".form-control").val('');
	}
	
	//添加网红
	function  addRedNet(){
		resetCondition();
		$("#bg").css("display","block");
		$("#share").css("display","block");
		$(".tanchu").css("display","block");
		$(".thank_top").css("display","block");
	}
	

	function  edit(id){
		 $.ajax({
			 type:"post",
			 url:"${ctx}/redNet/showRedNetById",
			 data:{id:id},
			 dataType:"json",
			 success:function(res){
				 $("input[name='id']").val(res.id);
				 $("input[name='redNetName']").val(res.redNetName);
				 $("input[name='site']").val(res.site);
				 $("input[name='tomosonUrl']").val(res.tomosonUrl);
				 $("input[name='pushSum']").val(res.pushSum);
				 $("input[name='redNetOffer']").val(res.redNetOffer);
				 $("input[name='email']").val(res.email);
				 $("input[name='cooperationTime']").val(res.cooperationTime);
				 $("input[name='bz']").val(res.bz);
				 $("#bg").css("display","block");
				$("#share").css("display","block");
				$(".tanchu").css("display","block");
				$(".thank_top").css("display","block");
			 }
		 })
	}
	
 
</script>
    
<body>
<!-- 添加网红 -->
<div class="grey_bj" id="bg"></div>
<div class="tank"  id="share">
<div class="thank_top"><a href="javascript:void(0)"  class="close_1">X</a></div>
<div>
<input  type="hidden"  name="id"  value="" />
<table class="redNetInfo">
<tr>
    <td>网红名称:</td>
    <td><input  type="text"  class="form-control"  name="redNetName"  value=""></td>
</tr>
<tr>
    <td>网红博客:</td>
    <td><input  type="text"  class="form-control"  name="site"></td>
</tr>
<tr>
    <td>红人Tomoson资质评估链接:</td>
    <td><input  type="text"  class="form-control"   name="tomosonUrl"></td>
</tr>
<tr>
    <td>发文次数:</td>
    <td><input  type="text"  class="form-control"   name="pushSum"></td>
</tr>
<tr>
    <td>红人报价:</td>
    <td><input  type="text"   class="form-control"  name="redNetOffer"  placeholder="默认单位为美金"></td>
</tr>
<tr>
    <td>红人邮箱:</td>
    <td><input  type="text"  class="form-control"   name="email"></td>
</tr>
<tr>
    <td>合作时间:</td>
    <td><input class="form-control" type="date" name="cooperationTime" placeholder="例:2017/06/01"></td>
</tr>
<tr>
    <td>备注:</td>
    <td><input  type="text"  class="form-control"   name="bz"></td>
</tr>
<tr>
    <td colspan="2"><input type="button"  id="submit"   value="确定"></td>
</tr>
</table>
</div>
</div>

	<h2 align="center">网红管理界面</h2>
	<form class="form-inline" role="form">
		<div class="form-group">
			<div class="input-group">
				<div class="input-group-addon">网红id:</div>
				<input  class="form-control" type="text"  id="redNetId"  style="width: 155px">
			</div>
			<div class="input-group">
				<div class="input-group-addon">网红名称:</div>
				<input  class="form-control" type="text"   id="redNetName"  style="width: 155px">
			</div>
			<div class="input-group">
				<div class="input-group-addon">网红博客:</div>
				<input  class="form-control" type="text"   id ="site"  style="width: 155px">
			</div>
		</div>
		
		<button type="button" class="btn btn-primary" onclick="searchRedNets(1)">查询</button>
		<button type="button" class="btn btn-warning" onclick="resetCondition()">重置</button>
		<button type="button" class="btn btn-danger" onclick="addRedNet()">添加</button>
		<button type="button" class="btn btn-danger" id="back">返回</button>
	</form>
	<table id='shipmentList' class="table table-bordered table-condensed table-striped">
		<thead>
			<tr>
				<th>序号</th>
				<th>网红ID</th>
				<th>网红名称</th>
				<th>红人合作发布站点（博客/YTB链接）</th>
				<th>红人Tomoson资质评估链接</th>
				<th>发文次数</th>
				<th>红人报价</th>
				<th>红人邮箱</th>
				<th>合作时间</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody></tbody>
		<tfoot>
		</tfoot>
	</table>
</body>
</html>