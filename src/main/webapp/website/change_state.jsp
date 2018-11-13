<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单商品到库状态变更</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>

<script type="text/javascript" src="/cbtconsole/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/thelibrary.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.messager.js"></script>


<link rel="stylesheet" href="/cbtconsole/css/warehousejs/measure.css" type="text/css">
<style type="text/css">
  .select{
	width:200px;
	height:15px;
	color:#000;
	font-size:12px;
	border:1px solid #000;
	}
	.loading { position: fixed; top: 0px; left: 0px;
	width: 100%; height: 100%; color:#fff; z-index:9999;
    background: #000 url(/cbtconsole/img/yuanfeihang/loaderTwo.gif) no-repeat 50% 300px;
	opacity: 0.4;}
</style>
<script type="text/javascript">

   function search(page){
	   document.getElementById('operatediv').style.display='block';
	   $("#tabId tbody").html("");
	   var orderid =$('#orderid').val();
	   $.post("/cbtconsole/warehouse/geOrderState",{orderid:orderid},
				function(data){
				    var objlist = eval("("+data+")");
					for(var i=0; i<objlist.length; i++){               
			           	htm_='';
			           	htm_ = '<tr>';
			           	htm_ += '<td align="center" width="110px;">'+(i+1)+'</td>';
			           	htm_ += '<td align="center" width="50px;">'+objlist[i].orderid+'</td>'; 
			           	htm_ += '<td align="center" width="50px;">'+objlist[i].goodsid+'</td>';
			           	if(objlist[i].state==0){
			           		htm_ += '<td align="center" width="50px;">未到库</td>';
			           	}else{
			           		htm_ += '<td align="center" width="50px;">已到库</td>';
			           	}
			           	htm_ += '<td align="center"  width="70px;"><input type="button" value="更改到库" onclick="updateState(\''+objlist[i].orderid+'\',\''+objlist[i].goodsid+'\')"/></td>';
			           	htm_ += '</tr>';
						$("#tabId").append(htm_);
					}
					 document.getElementById('operatediv').style.display='none';
			 	}
			);
   }
   
   function reg(){
	    mybg = document.createElement("div");
	    mybg.setAttribute("id","mybg");
	    mybg.style.background = "#ffffff";
	    mybg.style.width = "100%";
	    mybg.style.height = "100%";
	    mybg.style.position = "absolute";
	    mybg.style.top = "0";
	    mybg.style.left = "0";
	    mybg.style.zIndex = "500";
	    mybg.style.opacity = "0.8";
	    mybg.style.filter = "Alpha(opacity=80)";
	    document.body.appendChild(mybg);
	    document.body.style.overflow = "hidden";
	}
   
   function updateState(orderid,goodsid){
	   $.ajax({
			type:"post", 
			url:"/cbtconsole/warehouse/updateState",
			dataType:"text",                                                      
			data:{orderid : orderid,goodsid:goodsid}, 
			success : function(data){
				 if(data>0){
                 	alert("更新成功");
                 	search(1);
                 }else{
                 	alert("更新失败");
                 }
			}
		});
   }
   
   function updateAllDetailsState(state){
	  var orderid= $("#orderid").val();
	   $.ajax({
			type:"post", 
			url:"/cbtconsole/warehouse/updateAllDetailsState",
			dataType:"text",                                                      
			data:{orderid : orderid,state:state}, 
			success : function(data){
				 if(data>0){
                 	alert("更新成功");
                 	search(1);
                 	$("#username").find("option[text='审核']").attr("selected",true);
                 }else{
                 	alert("更新失败");
                 }
			}
		});
   }
   
   function FnLoading(){
		$("#operatediv").css("display","block");
		if(document.body.scrollHeight > window.screen.height){
			$("#operatediv").css("height",document.body.scrollHeight);
		} $("#operatediv").css("display","none");
	}

</script>
</head>
<body style="background-color : #F4FFF4;">
<div id="operatediv" class="loading" style="display: none;"></div>
<div align="center">
	<div><h1>订单商品到库状态变更</h1></div>
	<div id="msginfo"></div>               
	<br/><br/>                      
	<!-- 扫描 -->
	<div style="width: 1300px">
		express-orderid:<input type="text" value="" id="orderid"/>
		<input type="button" value="查询" onclick="search();"/>
		变更订单状态:<select id="username" style="width: 150px;text-align: center;" onchange="updateAllDetailsState(this.value)">
		 		  <option value="0"></option>
		          <option value="5">审核</option>
		          <option value="1">采购</option>
		          <option value="2">到库</option>
		          <option value="3">出运</option>
		       </select>
<!-- 		<input type="button" value="整单到库" onclick="updateAllDetailsState();"/> -->
	</div>         
	
	<!-- 表格 -->
	<div>
		<table id="tabId" class="altrowstable" style="width: 800px;table-layout:fixed ;">
		<thead>
			<tr>
				<td width="50px;" align="center">序号</td>
				<td width="200px;" align="center">订单号</td>
				<td width="100px;" align="center">商品编号</td>
				<td width="100px;" align="center">商品状态</td>
				<td width="100px;" align="center">操作</td>
			</tr>
			</thead>
			  <tbody>
				</tbody>
		</table>
	</div>
</div>
</body>
</html>