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
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
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
	
	getAllRedNet(); 
	
	searchRedNets(1);
	
	$("#addRedNet").click(function(){
		resetCondition();
		$.ajax({
			type:"post",
			async: false ,
			url:"${ctx}/redNet/createShareIdToRedNet",
			dataType:"json",
			success:function(res){
				$("#shareId").val(res);
			}
		})
		$("#bg").css("display","block");
		$("#share").css("display","block");
		$(".tanchu").css("display","block");
		$(".thank_top").css("display","block");
	})
	
	 $(".close_1").click(function() {
			$("#bg").css("display","none");
			$("#share").css("display","none");
			$(".tanchu").css("display","none");
			$(".thank_top").css("display","none");
    });
	
	
	$("#selectRedNet").click(function(){
		 window.location.href="${ctx}/website/redNet.jsp";
	});
	
	
	$("#submit").click(function(){
		var  id  = $("input[name='id']").val();
		var  shareId =  $("input[name='shareId']").val();
		/*  var  redNetId =  $("input[name='redNetId']").val(); */
		 var  redNetId = $("#redNetId").find("option:selected").val();
		 var  publishAddress =  $("input[name='publishAddress']").val();
		 var  pushTime =  $("input[name='pushTime']").val();
		 var bz =  $("input[name='bz']").val();
		 if(redNetId==""){
			 alert("网红id不能为空!");
		 }else{
			 $("#bg").css("display","none");
			 $("#share").css("display","none");
			   $.ajax({
				 type:"post",
				 url:"${ctx}/redNet/addRedNetStatistics",
				 data:{id:id,shareId:shareId,redNetId:redNetId,publishAddress:publishAddress,bz:bz,pushTime:pushTime}, 
				 dataType:"json",
				 success:function(res){
					if(res==1){
						alert("添加/修改成功!")
						window.location.reload();
					} 
					else if(res==2){
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
		var redNetId = $("#redNet").find("option:selected").val();
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		var pushTime = $("#pushTime").val();
		$("#shipmentList tbody").html("");
		$("#shipmentList tfoot").html("");
		$.ajax({
			type:'post',
			url:'${ctx}/redNet/showRedNetStatistics',
			data:{'redNetId':redNetId,'startTime':startTime,'endTime':endTime,'pushTime':pushTime,page:page},
			dataType:'json',
			success:function(res){
				if (res[0].count > 0) {
					var tabStr = "";
					for (var i = 0; i < res.length; i++) {
						var obj = res[i];
						tabStr+=("<tr>");
						tabStr+=("<td>"+(i+1)+"</td>");
						tabStr+=("<td>"+obj.shareId+"</td>");
						tabStr+=("<td>"+obj.redNetName+"</td>");
						tabStr+=("<td>"+obj.publishAddress+"</td>");
						tabStr+=("<td>"+obj.clickSum+"</td>");
						tabStr+=("<td>"+obj.addGoodCarSum+"</td>");
						tabStr+=("<td>"+obj.createOrderSum+"</td>");
						tabStr+=("<td>"+obj.createTime+"</td>");
						tabStr+=("<td>"+obj.pushTime+"</td>");
						tabStr+=("<td>"+obj.bz+"</td>");
						tabStr+=("<td><a href='#'  onclick='edit("+obj.id+")'>编辑</a></td>");
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
				}
				$("#shipmentList tbody").append(tabStr);
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("系统错误,请联系开发人员确认问题!");
			}
		});
	}
	 
	function resetCondition() {
		$("input[name='redNetId']").val('');
		$("input[name='redNetName']").val('');
		$("input[name='site']").val('');
	}
	
	function getAllRedNet(){
		$.ajax({
			type:"post",
			url:"${ctx}/redNet/showAllRedNet",
			dataType:"json",
			success:function(res){
				if(res.length>0){
					for(var i=0;i<res.length;i++){
						$("#redNetId").append("<option  value="+res[i].id+">"+res[i].redNetName+"</option>");
						$("#redNet").append("<option  value="+res[i].id+">"+res[i].redNetName+"</option>");
					}
				}
			}
		})
	}
	
	
	function  edit(id){
		 $.ajax({
			 type:"post",
			 url:"${ctx}/redNet/showRedNetStatisticsById",
			 data:{id:id},
			 dataType:"json",
			 success:function(res){
				 $("input[name='id']").val(res.id);
				 $("input[name='shareId']").val(res.shareId);
				 $("input[name='pushTime']").val(res.pushTime);
				 $("input[name='publishAddress']").val(res.publishAddress);
				 $("input[name='bz']").val(res.bz);
				 $("#redNetId").val(res.redNetId)
				 $("#bg").css("display","block");
				 $("#share").css("display","block");
				 $(".tanchu").css("display","block");
				 $(".thank_top").css("display","block");
			 }
		 })
	}
	
	function resetCondition() {
		$("input[name='redNetId']").val('');
		$("input[name='redNetName']").val('');
		$("input[name='site']").val('');
		$(".form-control").val('');
	}
	 
</script>
<body>
<!--分配shareId -->
<div class="grey_bj" id="bg"></div>
<div class="tank"  id="share">
<div class="thank_top"><a href="javascript:void(0)"  class="close_1">X</a></div>
<div>
<input  type="hidden"   name="id" >
<table class="redNetInfo">
<tr>
    <td>shareId:</td>
    <td><input  type="text"  class="form-control" id ="shareId" name="shareId"  value=""></td>
</tr>
<tr>
    <td>分配网红:</td>
    <td>
	  <select  name="redNetId"  id="redNetId" class="form-control">
		  <option value="">请选择</option>
      </select>
	</td>
</tr>
<tr>
    <td>发文地址:</td>
    <td><input  type="text"  class="form-control"   name="publishAddress"></td>
</tr>
<tr>
    <td>发文时间:</td>
    <td><input    name="pushTime" class="form-control" type="date" value="" style="width: 155px"  placeholder="例:2017/06/01"><span></span></td>
</tr>
<tr>
    <td>备注:</td>
    <td><input  type="text"   class="form-control"  name="bz"></td>
</tr>
<tr>
    <td colspan="2"><input type="button"  id="submit"   value="确定"></td>
</tr>
</table>
</div>
</div>
	

	<h2 align="center"  style="margin-button:20px;">网红管理界面</h2>
	<form class="form-inline" role="form">
	     <div class="form-group">
			<div class="input-group">
				<div class="input-group-addon">网红</div>
				<select id="redNet" class="form-control">
					<option value="">请选择</option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<div class="input-group">
				<div class="input-group-addon">创建时间</div>
				<input id='startTime' name ="startTime" class="form-control" type="date" value="" style="width: 155px">
				<div class="input-group-addon">~</div>
				<input id='endTime'  name="endTime" class="form-control" type="date" value="" style="width: 155px">
			</div>
		</div>
		<div class="input-group">
			<div class="input-group-addon">发布时间:</div>
			<input id="pushTime"  class="form-control" type="date" value="" style="width: 155px" >
		</div>
		<button type="button" class="btn btn-primary" onclick="searchRedNets(1)">查询</button>
		<button type="button" class="btn btn-warning" onclick="resetCondition()">重置</button>
		<button type="button" class="btn btn-warning" id="selectRedNet" >管理网红</button>
		<button type="button" class="btn btn-danger"  id="addRedNet">生成shareId</button>
	</form>

	<table id='shipmentList' class="table table-bordered table-condensed table-striped">
		<thead>
			<tr>
				<th>序号</th>
				<th>shareid</th>
				<th>网红名称</th>
				<th>发文地址</th>
				<th>点击人数</th>
				<th>加购物车人数</th>
				<th>下单次数</th>
				<th>创建时间</th>
				<th>发文时间</th>
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