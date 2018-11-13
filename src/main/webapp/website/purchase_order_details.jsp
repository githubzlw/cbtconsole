<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="com.cbt.util.Redis" %>
<%@page import="com.cbt.util.AppConfig"%>
<%@ page import="com.cbt.website.userAuth.bean.Admuser" %>
<%@ page import="com.cbt.util.SerializeUtil" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>入库问题包裹反查</title>
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
	opacity: 0.4; display: none; }
	 ul#img_sf_1{ list-style:none;float:center; display:inline;}
ul#img_sf_1 li{float:left; width:150px; height:60px; display:inline; margin:2px 2px 2px 5px;}
ul#img_sf_11 li a{width:150px; height:60px; display:block;}
ul#img_sf_1 li a img{width:100%; height:100%; border:1px #999 solid;}
/* ul#img_sf_1 li a:hover{ z-index:100; margin:-32px 0px 0px -32px; position:absolute;} */
/* ul#img_sf_1 li a img :hover{width:600px; height:600px; border:1px #999 solid;} */
ul#img_sf_1 li {
    width: 250px;
    height: 250px;}
</style>

<%
	//取出当前登录用户
   String sessionId = request.getSession().getId();
    String userJson = Redis.hget(sessionId, "admuser");
	Admuser adm =(Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class); 
	int userid=adm.getId();
	String shipno=request.getParameter("shipno");
	String orderid=request.getParameter("orderid");
	String id=request.getParameter("id");
	%>
<script type="text/javascript">
$(function(){
	shipno='<%=shipno%>';
	orderid='<%=orderid%>';
	id='<%=id%>';
	userid='<%=userid%>';
	console.log("shipno="+shipno);
	console.log("id="+id);
	$.post("/cbtconsole/warehouse/getAllBuy",
			{"action" : "sell"},
			function(res){
				var json=eval(res);
				var str1='<option value="0">全部</option>';
				for (var i=0;i<json.length;i++){
					if(json[i].del=='1'){
					str1+='<option value="'+json[i].account+'">'+json[i].adminname+'</option>';
					}
				}
				$('select[id=username]').append(str1);
	});
	if((shipno!=null && shipno!='undefind' && shipno!='null') || (orderid!=null && orderid!='undefind' && orderid!='null')){
// 		$("#shipno").val(shipno);
		$("#orderid").val(orderid);
		$("#info_id").val(id);
		$("#user_id").val(userid);
		search(1);
	}
})
</script>
<script type="text/javascript">
   function search(page){
	   $.ajaxSetup({
	        async: false
	    });
	   $("#operatediv").css("display","block");
	   if(document.body.scrollHeight > window.screen.height){
	   		$("#operatediv").css("height",document.body.scrollHeight);
	   }            
	   $("#tabId tbody").html("");
	   var data =$('#data').val();
	   var type =$('#type').val();
	   var shipno =$('#shipno').val();
	   var orderid =$('#orderid').val();
	   var id =$('#info_id').val();
	   var user_id =$('#user_id').val();
	   var username =$('#username').val();
	   $.post("/cbtconsole/warehouse/getPurchaseOrderDetails",{page:page,data:data,orderid:orderid,shipno:shipno,username:username,type:type},
				function(data){
				    var objlist = eval("("+data+")");
					if(objlist.length>0){
						var allCount=objlist[0].buycount;
		       		if(allCount%20==0){
							allpage = allCount/20;
						}else{
							allpage = parseInt(allCount/20) + 1;
						}
			       		$("#datacount").html(allCount);
						$("#nowPage").html(page);
						$("#allPage").html(allpage);					
					}else{
						document.getElementById('rules2').innerHTML="仓库已入库，无需处理";
					}
					if(id!=null && id!="" && id!="null" && userid!="43"){
						document.getElementById('title').innerHTML= "问题件采购货源贴错提醒";
						document.getElementById('rules').innerHTML= "1、序号黄色数据是线下录入的采购订单信息；2、然后是发货一天还没入库的采购订单；3、紧接着是发货15天未入库的采购订单；4、最后是原链接采购30天没有入库的商品信息";
	           			document.getElementById('rules1').innerHTML="采购通过点击[去处理]按钮跳转到采购页面进行货源链接比对修改，修改完后点击【问题件采购货源贴错提醒】中的[确认并已处理]按钮通知仓库进行关联入库";
					}else{
						document.getElementById('title').innerHTML= "入库问题包裹反查";
						document.getElementById('rules').innerHTML= "1、序号橘黄色数据是线下录入的采购订单信息；2、然后是发货一天还没入库的采购订单；3、紧接着是发货15天未入库的采购订单；4、最后是原链接采购30天没有入库的商品信息；5、关联入库列为红色则表示采购已处理该运单号下的商品货源错误";
					}
					for(var i=0; i<objlist.length; i++){              
		           	htm_='';
		           	htm_ = '<tr>';
		           	if(objlist[i].tbOr1688==2){
		           		if(id!=null && id!="" && id!="null" && userid!="43"){
		           			htm_ += '<td align="center" width="20px;"  style="background-color:yellow;">'+(i+1)+'</td>';
		           		}else{
		           			htm_ += '<td align="center" width="20px;"  style="background-color:orange;">'+(i+1)+'</td>';
		           		}
		           	}else{
		           		htm_ += '<td align="center" width="20px;">'+(i+1)+'</td>';
		           	}
					if(objlist[i].orderid.length<9){
						htm_ += '<td align="center" width="150px;">'+objlist[i].orderstatus+'(<a target="blank" href="/cbtconsole/warehouse/getOrderinfoPage.do?goodid='+objlist[i].orderid+'">'+objlist[i].orderid+'</a>)</td>'; 
					}else{
						htm_ += '<td align="center" width="150px;">'+objlist[i].orderid+'</td>'; 
					}
		           	htm_ += '<td align="center" width="150px;" style="word-wrap:break-word;">'+objlist[i].shipno+'</td>';
// 		        	htm_ += '<td align="center" width="150px;">'+objlist[i].delivery_date+'</td>';
		           	htm_ += '<td align="center" width="150px;"><a title="'+objlist[i].itemname+'" href="'+objlist[i].itemurl+'" target="_blank">'+objlist[i].itemname.substring(0,objlist[i].itemname.length/3)+"..."+'</a></td>';
		           	htm_ += '<td align="center" width="150px;" style="word-wrap:break-word;">'+objlist[i].sku+'</td>';
		           	htm_ += '<td align="center" width="200px;"> <ul id="img_sf_1"><li><a><img src="'+objlist[i].imgurl+'"></a></li></ul></td>';
		           	htm_ += '<td align="center" width="50px;">'+objlist[i].username+'</td>';
		           	htm_ += '<td align="center" width="180px;"><span id="'+objlist[i].shipno+'_remark_'+i+'">'+objlist[i].remark+'</span><textarea id="'+objlist[i].shipno+'_'+i+'"></textarea><input type="button" value="确认" onclick="insertRemark(\''+objlist[i].shipno+'\',\''+objlist[i].sku+'\',\''+i+'\');"/></td>';
		           	if(id!=null && id!="" && id!="null" && userid!="43"){
		           		if(objlist[i].is_processing=="0"){
		           			htm_ += '<td align="center" width="50px;"><input type="button" class="sendInfo" value="确认已处理完成" onclick="sendInfo();"></td>';
		           		}else{
		           			htm_ += '<td align="center" width="50px;" style="background-color:;">待入库</td>';
		           		}
		           	}else{
		           		if(objlist[i].delivery_date=="0"){
		           			if(objlist[i].is_processing==0){
		           				htm_ += '<td align="center" width="50px;"><a target="blank" href="/cbtconsole/website/newtrack.jsp?type=1&orderid='+objlist[i].orderid+'&goodsid='+objlist[i].shipno+'">关联入库</a></td>';
		           			}else{
		           				htm_ += '<td align="center" width="50px;" style="background-color:red;"><a target="blank" href="/cbtconsole/website/newtrack.jsp?type=1&orderid='+objlist[i].orderid+'&goodsid='+objlist[i].shipno+'">关联入库</a></td>';
		           			}
		           		}else{
		           			if(objlist[i].is_processing==0){
		           				htm_ += '<td align="center" width="50px;"><a target="blank" href="/cbtconsole/website/newtrack.jsp?type=1&shipno='+objlist[i].shipno+'">关联入库</a></td>';
		           			}else{
		           				htm_ += '<td align="center" width="50px;" style="background-color:red;"><a target="blank" href="/cbtconsole/website/newtrack.jsp?type=1&shipno='+objlist[i].shipno+'">关联入库</a></td>';
		           			}
		           		}
		           	}
		           	htm_ += '</tr>';
					$("#tabId").append(htm_);
					}
			 	}
			);
 	   $("#operatediv").css("display","none");
   }
   
   function toPurchaseShow(orderid){
	   window.open("/cbtconsole/PurchaseServlet?action=getPurchaseByXXX&className=Purchase&pagenum=1&goodsid=&orderid_no_array=&orderid=0&admid=9&userid=&orderno="+orderid+"&goodid=&date=&days=&state=&unpaid=0&pagesize=50&goodname=");    
   }
   
   function sendInfo(){
	   var id =$('#info_id').val();
	   var shipno =$('#shipno').val();
	   if(id==null || id==''){
		   alert("无法发送消息");
		   return;
	   }
		$.post("/cbtconsole/InsertMessageForServlet",
				{id:id,tbOrderId:shipno},
				function(res){
					if(res>0){
						 $('.sendInfo').val('已处理');
						alert('已通知仓库,等待扫描入库');
					}else{
						alert('通知采购仓库失败');
					}
				});
   }
   
   function insertRemark(orderid,sku,i){
	   var page=$("#nowPage").html();
	   var reamrk=$("#"+orderid+"_"+i).val();
	   var old_remark = document.getElementById(""+orderid+"_remark_"+i+"").innerHTML;
	   $.ajax({
			type:"post", 
			url:"/cbtconsole/warehouse/insertRemark",
			dataType:"text",                                                      
			data:{orderid : orderid,sku:sku,remark:reamrk,old_remark:old_remark}, 
			success : function(data){
				 if(data>0){
                	alert("更新成功");
                	search(Number(page));
                }else{
                	alert("更新失败");
                }
			}
		});
   }

</script>
</head>
<body style="background-color : #F4FFF4;" onload="search(1)">
<input type="hidden" id="info_id" value=""/>
<input type="hidden" id="user_id" value=""/>
<div align="center">
	<div>
	<h1><span id="title">入库问题包裹反查</span></h1>
	<span id="rules" style="color:red"></span></br>
	<span id="rules1" style="color:red"></span></br>
	<span id="rules2" style="color:orange;font-size:50px"></span>
	</div>
	<div id="msginfo"></div>               
	<br/><br/>                      
	<!-- 扫描 -->
		<div style="width: 1300px">
		延迟天数:<select id="data" style="width: 150px;text-align: center;">
		 		  <option value="0" style="text-align: center;">全部</option>
		          <option value="3" style="text-align: center;">3</option>
		          <option value="5">5</option>
		          <option value="7">7</option>
		       </select>
		采购人:<select id="username" style="width: 150px;text-align: center;">
		       </select>
		采购平台:<select id="type" style="width: 150px;text-align: center;">
		 		  <option value="-1">全部</option>
		 		  <option value="0">淘宝</option>
		          <option value="1">1688</option>
		          <option value="2">线下</option>
		          <option value="3">忘录入订单信息</option>
		       </select>
		快递单号:<input type="text" value="" id="shipno" onkeypress="if (event.keyCode == 13) search(1);"/>
		<input type="hidden" id="orderid"/>
		<input type="button" value="查询" onclick="search(1);"/>
	</div> 
	<!-- 表格 -->
	<div>
		<table id="tabId" class="altrowstable" style="width: 800px;table-layout:fixed ;">
		<thead>
			<tr>
				<td width="20px;" align="center">序号</td>
				<td width="250px;" align="center">采购订单号</td>
				<td width="200px;" align="center">运单号</td>
				<td width="150px;" align="center">商品名称</td>
				<td width="150px;" align="center">采购规格</td>
				<td width="360px;" align="center">商品图片</td>
				<td width="50px;" align="center">采  购  人</td>
				<td width="180px;" align="center">备注信息</td>
				<td width="120px;" align="center">操       作</td>
			</tr>
			</thead>
			  <tbody>
				</tbody>
		</table>
	</div>
	<div style="text-align: center; " id="pagediv">
		 	共查到<span id="datacount">0</span>数据&nbsp;&nbsp;
		 	<input type="button" id="prePage" value="上一页"/>&nbsp;
		 	第<span id="nowPage">1</span>页/共<span id="allPage">0</span>页
		 	<input type="button" id="nextPage" value="下一页"/>&nbsp;&nbsp;
		 	跳至<input type="text" id="toPage" style="width:50px;"/>页 <input type="button" value="Go" id="jumpPage"/>
		 </div>
</div>
<div id="operatediv" class="loading"></div>
</body>
<script type="text/javascript">
$("#prePage").click(function(){
	var nowPage = $("#nowPage").html();
	if(parseInt(nowPage)<=1 ){
		alert("已到达首页");
		return false;
	}else{
		$("#nowPage").html(parseInt(nowPage)-1)
		search(parseInt(nowPage)-1);
	}
});
$("#nextPage").click(function(){
	var nowPage = $("#nowPage").html();
	var allPage = $("#allPage").html();
	if(parseInt(nowPage)==parseInt(allPage) ){
		alert("已到达尾页");
		return false;
	}else{
		$("#nowPage").html(parseInt(nowPage)+1)
		search(parseInt(nowPage)+1);
	}
});
$("#jumpPage").click(function(){
	var allPage = $("#allPage").html();
	var topage = $("#toPage").val();
	if(isNaN(topage)){
		alert("请输入正确的页码");
		return false;
	}else if(parseInt(topage)<=0 || parseInt(topage)>allPage){
		alert("页码超出范围");
		return false;
	}else{
		$("#nowPage").html(parseInt(topage))
		search(parseInt(topage));
	}
});
</script>
</html>