<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.cbt.util.AppConfig"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.cbt.util.*"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>入库记录</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.imgbox.pack.js"></script>
<!-- CSS goes in the document HEAD or added to your external stylesheet -->
<link rel="stylesheet" href="/cbtconsole/css/warehousejs/lrtk.css" type="text/css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<style type="text/css">
/* 表格样式 */
table.altrowstable {
	font-family: verdana,arial,sans-serif;
	font-size:11px;
	color:#333333;
	border-width: 1px;
	border-color: #F4FFF4;           
	border-collapse: collapse;
}
table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}
table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
	position: relative;
}
.oddrowcolor{
	background-color:#F4F5FF;
}
.evenrowcolor{
	background-color:#FFF4F7;            
}

/* 字体样式 */
body{font-family: arial,"Hiragino Sans GB","Microsoft Yahei",sans-serif;} 
p.thicker {font-weight: 900}

/* button样式 */
.className{
  line-height:30px;
  height:30px;
  width:70px;
  color:#ffffff;
  background-color:#3ba354;
  font-size:16px;
  font-weight:normal;
  font-family:Arial;
  border:0px solid #dcdcdc;
  -webkit-border-top-left-radius:3px;
  -moz-border-radius-topleft:3px;
  border-top-left-radius:3px;
  -webkit-border-top-right-radius:3px;
  -moz-border-radius-topright:3px;
  border-top-right-radius:3px;
  -webkit-border-bottom-left-radius:3px;
  -moz-border-radius-bottomleft:3px;
  border-bottom-left-radius:3px;
  -webkit-border-bottom-right-radius:3px;
  -moz-border-radius-bottomright:3px;
  border-bottom-right-radius:3px;
  -moz-box-shadow: inset 0px 0px 0px 0px #ffffff;
  -webkit-box-shadow: inset 0px 0px 0px 0px #ffffff;
  box-shadow: inset 0px 0px 0px 0px #ffffff;
  text-align:center;
  display:inline-block;
  text-decoration:none;
}
.className:hover{
  background-color:#1c9439;
}

/* 订单号 */
.someclass {
	text-indent : 0em;
	word-spacing : 1px;
	letter-spacing : 2px;
	text-align : left;
	text-decoration : none;
	font-family : monospace;
	color : #007007;
	font-weight : bold;
	font-size : 14pt;        
	background-color : #F0F0FF;
	border-color : transparent;
}

/* 查询条件文本框 */
.querycss {
	color : #00B026;        
	font-size : 12pt;
	border-width : 1px;
	border-color : #AFFFA0;
	border-style : solid;
	height : 23px;
	width : 120px;
}


/* 一页显示select */


.classSelect{
  line-height:30px;
  height:30px;
  width:157px;
  color:#ffffff;
  background-color:#3ba354;
  font-size:16px;
  font-weight:normal;
  font-family:Arial;
  border:0px solid #dcdcdc;
  -webkit-border-top-left-radius:3px;
  -moz-border-radius-topleft:3px;
  border-top-left-radius:3px;
  -webkit-border-top-right-radius:3px;
  -moz-border-radius-topright:3px;
  border-top-right-radius:3px;
  -webkit-border-bottom-left-radius:3px;
  -moz-border-radius-bottomleft:3px;
  border-bottom-left-radius:3px;
  -webkit-border-bottom-right-radius:3px;
  -moz-border-radius-bottomright:3px;
  border-bottom-right-radius:3px;
  -moz-box-shadow: inset 0px 0px 0px 0px #ffffff;
  -webkit-box-shadow: inset 0px 0px 0px 0px #ffffff;
  box-shadow: inset 0px 0px 0px 0px #ffffff;
  text-align:center;
  display:inline-block;
  text-decoration:none;
}
.classSelect:hover{
  background-color:#FAFFF4;         
}
.repalyBtn{height: 30px;width: 70px;background: #1c9439;border: 0px solid #dcdcdc;color: #ffffff;cursor: pointer;position: absolute;right:3px;bottom:2px;}
.repalyDiv{width: 500px;background: #34db51;text-align: center;position: fixed;left: 40%;top: 43%;}
.repalyBtn:hover{opacity: 1;}
.w-margin-top{margin-top:40px;}
.window, .window-shadow {
    position: fixed;}
</style>
</style>
<script type="text/javascript" src="js/jquery-1.8.0.min.js"></script>

<link rel="stylesheet" href="css/orderinfo.css" type="text/css"> 
<script type="text/javascript">
//
$(function(){
	$("#hide_repalyDiv").click(function(){
		hideRepalyDiv();
	});
})

function hideRepalyDiv(){
	$(".repalyDiv").hide();
	$("#remark_content").val("");
	$("#rk_orderNo").val("");
	$("#rk_goodsid").val("");
}

//表格样式
function altRows(id){
	if(document.getElementsByTagName){  
		
		var table = document.getElementById(id);  
		var rows = table.getElementsByTagName("tr"); 
		 
		for(i = 1; i < rows.length; i++){          
			if(i % 2 == 0){
				rows[i].className = "evenrowcolor";
			}else{
				rows[i].className = "oddrowcolor";
			}      
		}
	}
}

window.onload=function(){
	
	
	var ordersLength = document.getElementById("ordersLength").value;
	for(var i=0; i<ordersLength; i++){
		altRows('alternatecolor'+(i+1));
	}
	
}

// 查询 提交
function aSubmit(){
	document.getElementById("idForm").submit();
}

//清空查询条件
function cleText(){
//	document.getElementById("idname").value="";
	$('input[type=text]').val("");
}

//商品采购后取消退货
function refundGoods(orderid,goodsid){
    $.ajax({
        type:"post",
        url:'/cbtconsole/warehouse/refundGoods',
        dataType:"text",
        data:{"orderid":orderid,"goodsid":goodsid},
        success : function(data){
			if(data>0){
			    // alert("成功");
                document.getElementById("re_"+orderid+goodsid+"").style.background = "red";
                document.getElementById("re_"+orderid+goodsid+"").style.display="none";
            }else{
			    alert("退货库存退回失败");
			}
        }
    });
}

//备注回复
function doReplay(userid,orderNo,goodsid){
	
	$("#remark_content").val("");
	$("#rk_orderNo").val(orderNo);
	$("#rk_goodsid").val(goodsid);
	$(".repalyDiv").show();
}

function saveRepalyContent(type){

	var orderNo = $("#rk_orderNo").val();
	var goodsid = $("#rk_goodsid").val();
	var remarkContent = $("#remark_content").val();
	if(orderNo == null || orderNo == ""){
		alert("获取订单号失败，请关闭后重试");
		return;
	}
	if(goodsid == null || goodsid == ""){
		alert("获取商品id失败，请关闭后重试");
		return;
	}
	if(remarkContent == null || remarkContent == ""){
		alert("请填写备注信息");
		return;
	}
	$.ajax({
		type:"post", 
		url:'/cbtconsole/warehouse/treasuryNote.do',
		dataType:"text",  
	    data:{"orderNo":orderNo,"goodsid":goodsid,"remarkContent":remarkContent,"type":type},
		success : function(data){
			if(data == "ok"){
				hideRepalyDiv();
				location.reload();
			} else{
				alert(data);
			}
		}
	});
}

function returnNu(orid,cusorder ) {
	              var cusorder =$(" #cusorder ").val()
	              var number =$(" #number ").val()
	              var orid =$(" #orid ").val()
	              var returnNO =$(" #returnNO ").val()
	              var retur =$(" #reNum ").val()
	              if(number > retur){
	            	  alert('不可超过可退数量');  
	            	  return;
	              }
	              if(returnNO==""||number==""){
	            	  alert('请填写数据');  
	              }
				  $.post("/cbtconsole/Look/AddOrder", {
						orid:orid,number:number,cusorder:cusorder,returnNO:returnNO
					}, function(res) {
						if(res.rows == 1){
							alert('修改成功');
						}else if(res.rows == 3){
							alert('请填写数据');
							return;
						}
						else{
							alert('不可重复退单');
						}
						 $('#user_remark').window('close');	
					
		});
}


function resetChecked(orderid,goodsid){
	 window.location.href ="/cbtconsole/website/location_management.jsp?orderid="+orderid;
}

function showType(orderid){
	document.getElementById(""+orderid+"").style.display="inline-block";
}
function returnOr(str){
	var cusorder=this.$(" #cusorder").val();
	window.location.href ="/cbtconsole/AddReturnOrder/FindReturnOrder/"+cusorder;

}
 function returnNum(orid,cusorder) {
	 $.ajax({
	        type: "POST",
	        url: "/cbtconsole/Look/getAllOrderByOrid",
	        data: {orid:orid},
	        dataType:"json",
	        success: function(msg){
	            if(msg.rows == 1){
	            	alert('该订单已经全部退货');
	            }else if(msg.rows == 2){
	            	alert('该订单不存在于1688');
	            }
	            else {
	            	document.getElementById('cusorder').value=cusorder;
	            	document.getElementById('orid').value=orid;
	            	document.getElementById('reNum').value=msg.rows.itemNumber;
	            	$('#user_remark').window('open');         
	            }
	           
	        }
	    });
} 
</script>

</head>
<body style="background-color : #F4FFF4;">
 <div id="user_remark" class="easyui-window" title="退货申请"
         data-options="collapsible:false,minimizable:false,maximizable:false,closed:true,"
         style="width:400px;height:auto;display: none;font-size: 16px;">
            <div id="sediv" style="margin-left:20px;">
            <div>客户订单：<input id="cusorder" value='' ></div>
             <div>购物车ID：<input id="orid" value='' ></div>
             <div>可退数量：<input id="reNum" value=''></div>
            <div>退货数量：<input id="number" value='' ></div>
             <div>退货理由：<input id="returnNO" value='' ></div>      
            </div>
            <div style="margin:20px 0 20px 40px;">
                <a href="javascript:void(0)" class="easyui-linkbutton"
                   onclick="returnNu()" style="width:80px" >提交申请</a>
            </div>
    </div> 
   <!--  <div id="user_remark" class="easyui-window" title="退货申请"
         data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"
         style="width:800px;height:auto;display: none;font-size: 16px;">
            <div id="sediv" style="margin-left:20px;">
                <table id="tabl" border="1" cellspacing="0">
                
               <tr >
               <td style='width:20px'>选择</td>
               <td>产品名</td>
               <td>产品规格</td>
               <td>总数量</td>
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
                <input class="but_color" type="button" value="整单提交" onclick="AddOll()">
                <input type='radio' size='5' name='radioname' value='客户退单' id='c' />客户退单
                <input type='radio' size='5' name='radioname' value='质量问题' id='c' />质量问题
                <input type='radio' size='5' name='radioname' value='客户要求' id='c' />客户退单
            </div>
    </div> -->

<form id="idForm" action="getOrderinfoPage.do" method="get">
	<div align="center" >
		
		<div><H1>入库记录</H1></div>
		<div>          

			
			订单号:<input class="querycss" style="width : 160px;"  name="orderid" value="${oip.orderid}" type="text"/>
			用户ID:<input class="querycss" style="width : 100px;"  name="userid" value="${oip.userid}" type="text"/>
			商品编号/购物车Id:<input class="querycss" style="width : 100px;"  name="goodid" value="${oip.goodid}" type="text"/>
			<!-- readonly="readonly" -->
			开始日期:<input type="text" id="ckStartTime" name="ckStartTime"  value="${oip.ckStartTime}"  onfocus="WdatePicker({isShowWeek:true})"  />
			结束日期:<input type="text" id="ckEndTime" name="ckEndTime" value="${oip.ckEndTime}"  onfocus="WdatePicker({isShowWeek:true})"  />
			入库状态:<select name="state" style="width:120px;">
		          <option value="0">全部</option>
		          <option value="1">有问题</option>
		          <option value="2">没问题</option>
		       </select>
			<a href='javascript:void(0);' onclick="aSubmit()"  class='className'>查询</a>
			<a href='javascript:void(0);' onclick="cleText()" class='className'>清空</a>
			
		</div>
		<div>
			<br/>          
			当前第${oip.pageNum}页 &nbsp;&nbsp; 共${oip.pageCount}页&nbsp;&nbsp; 共${oip.pageSum }记录       
			<!-- class='classSelect' -->
			<select  onchange="window.location=this.value" >
				<option selected="selected" value='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>每页显示${oip.pageSize}条</option>
				
				<c:forEach begin="1" end="3" var="i">
					<c:if test="${i*50 != oip.pageSize}">
						<option value='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${i*50 }'> 每页显示${i*50}条</option>
					</c:if>
				</c:forEach>
				
			</select>          
			            
			                 
			 <!-- 销售与采购 

			 -->  
			 <a href='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=1&pageSize=${oip.pageSize }' class='className'>第一页</a>
			 <a href='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize }' class='className'>上一页</a>
			 <a href='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.nextPage }&pageSize=${oip.pageSize }' class='className'>下一页</a>
			 <a href='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.pageCount }&pageSize=${oip.pageSize }' class='className'>最后一页</a>
		</div>
		<div><h3></h3></div>
		<div>
			<c:forEach items="${oip.allOrders}" var="strOrder" varStatus="status">
				<div>
					  
					<div>
						<table width="1600px" class="altrowstable" id="alternatecolor${status.count}">
							<tr class="someclass" ><td colspan="8"><div style="display:inline">
							订单号:${strOrder }&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
							&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
							&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
							
							</div>
							<div style="display:inline"><span style="display: none" id="${strOrder }">订单已取消</span></div>
							</td></tr>
	   						<tr style="background-color:#FAFFF4;">
	  		  	    			<td width="50px"><font style="font-size : 15px;">用户ID</font></td>
						  		<td width="210px"><font style="font-size : 15px;">订单编号</font></td>
				  				<td width="70px"><font style="font-size : 15px;">商品编号/购物车Id</font></td>
			  					<td width="220px"><font style="font-size : 15px;">入库时间</font></td>
								<td width="230px"><font style="font-size : 15px;">拍照图片</font></td>
								<td width="70px"><font style="font-size : 15px;">仓库位置</font></td>
								<td width="100px"><font style="font-size : 15px;">入库状态</font></td>
								<td><font style="font-size : 15px;">入库备注</font></td>
								<td width="70px"><font style="font-size : 15px;">操作</font></td>
							</tr>
							<c:forEach items="${oip.pagelist}" var="storageLocation" varStatus="s">
								<c:if test="${storageLocation.orderid == strOrder}">
									<tr >
										<c:if test="${storageLocation.state == '-1' || storageLocation.state == '6'}">
										<script type="text/javascript">
										    showType('${storageLocation.orderid }');
										    </script>
										</c:if>
										<td><font style="font-size : 15px;">${storageLocation.user_id }</font></td>
										<td><font style="font-size : 15px;">${storageLocation.orderid }</font></td>
										<td><font style="font-size : 15px;">${storageLocation.goodid }/${storageLocation.odid}</font></td>
										<td><font style="font-size : 15px;">${storageLocation.createtime }</font></td>
										<td >
											<c:forEach var="pic" items="${storageLocation.picList}" varStatus="m">
												<a id="example-${m.index }"  href="${pic }">
													<img width="150px" height="150px" alt="" src="${pic }" /></a>
												<script type="text/javascript">
                                                    $('#example-${m.index }').imgbox({
                                                        'speedIn'		: 0,
                                                        'speedOut'		: 0,
                                                        'alignment'		: 'center',
                                                        'overlayShow'	: true,
                                                        'allowMultiple'	: false
                                                    });
												</script>
											</c:forEach>
										</td>
									   
										<td><font style="font-size : 15px;">${storageLocation.position }</font></td>
										<td><font style="font-size : 15px;">${storageLocation.goodstatus } </font></td>
										<td>
										
												 <div style="overflow-y:scroll;height:150px;">
													 <div class="w-font">
														 <font style="font-size : 15px;">${storageLocation.warehouse_remark }</font>
													 </div>
												 </div>
												 
												 <div class="w-margin-top">
												    
												 	<input type="button" value="二次验货" class="repalyBtn" style="left:3px;" onclick="resetChecked('${storageLocation.orderid }',${storageLocation.goodid });"/>
													 <c:if test="${storageLocation.is_refund == 0 && storageLocation.od_state == -1}">
														 <input type="button" id="re_${storageLocation.orderid }${storageLocation.goodid }" value="退货" class="repalyBtn" style="left:3px;margin-left:380px;" onclick="refundGoods('${storageLocation.orderid }',${storageLocation.goodid });"/>
													 </c:if>
													<input type="button" value="回复" class="repalyBtn" onclick="doReplay(${storageLocation.user_id },'${storageLocation.orderid }',${storageLocation.goodid });"/>
												
													
												</div>							
										</td>
										<td><input type="button" value="退货申请" align="center" onclick="returnNum(${storageLocation.odid},'${storageLocation.orderid }');"/></td>
									</tr>
								</c:if>
							</c:forEach>        
							
						</table>                                   
					</div>
				</div>
			</c:forEach>
		</div>
		<!-- 用来空行 -->
		    
		<div>
		<!-- class='classSelect' -->
			<select  onchange="window.location=this.value" >
				<option selected="selected" value='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>每页显示${oip.pageSize}条</option>
				
				<c:forEach begin="1" end="3" var="i">
					<c:if test="${i*50 != oip.pageSize}">
						<option value='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${i*50 }'> 每页显示${i*50}条</option>
					</c:if>
				</c:forEach>
				
			</select>
			 <a href='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=1&pageSize=${oip.pageSize }' class='className'>第一页</a>
			 <a href='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize }' class='className'>上一页</a>
			 <a href='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.nextPage }&pageSize=${oip.pageSize }' class='className'>下一页</a>
			 <a href='getOrderinfoPage.do?ckStartTime=${oip.ckStartTime}&goodid=${oip.goodid}&ckEndTime=${oip.ckEndTime}&orderid=${oip.orderid}&userid=${oip.userid }&pageNum=${oip.pageCount }&pageSize=${oip.pageSize }' class='className'>最后一页</a>
		</div>
		<div>
			<h4>当前第${oip.pageNum}页 &nbsp;&nbsp; 共${oip.pageCount}页&nbsp;&nbsp; 共${oip.pageSum }记录 </h4>
			
		</div>         
	</div>
	
	<div>
    	<div class="repalyDiv" style="display:none;">
			<input id="rk_orderNo" type="hidden" value="">
			<input id="rk_goodsid" type="hidden" value="">
			回复内容: <a id="hide_repalyDiv" style="color: red;float: right;margin-right: 10px;font-size: 24px;text-decoration:none" href="javascript:void(0);">X</a><br>
			<textarea name="remark_content" rows="8" cols="50" id="remark_content"></textarea>
			<font color="red" id="ts"></font><br>
			<c:if test="${oip.roleType!=2}">
			   <input type="button" id="repalyBtnId" onclick="saveRepalyContent(1)" value="验货无误">
			</c:if>
			<input type="button" id="repalyBtnId" onclick="saveRepalyContent(0)" value="提交回复">
		</div>
    </div>
	
	<input type="hidden"  id="ordersLength" value="${fn:length(oip.allOrders)}"/>  <!-- 商品个数  用来做表格颜色-->
	<input type="hidden"  id="pageNum" value="${oip.pageNum}"/>					   <!-- 当前第几页  submit 提交需要的值 -->
	<input type="hidden"  name="pageSize" value="${oip.pageSize}"/>				   <!-- 每页显示多少条  submit 提交需要的值 -->
	<!-- <input type="hidden"  name="saleOrPurchase" value=""/>				    只看销售与采购 -->          
	
	                   
	</form>
	
</body>
</html>