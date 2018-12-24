<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@page import="com.cbt.util.Redis"%>
<%@page import="com.cbt.util.SerializeUtil"%>
<%@page import="com.cbt.website.userAuth.bean.Admuser"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>包裹列表</title>
<%
String sessionId = request.getSession().getId();
String userJson = Redis.hget(sessionId, "admuser");
Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
%>
<link rel="stylesheet" href="/cbtconsole/css/warehousejs/lrtk.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/warehousejs/measure.css" type="text/css">
<link id="skin" rel="stylesheet" href="/cbtconsole/js/warehousejs/jBox/Skins2/Green/jbox.css" />


<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/layer.js"></script>  
 <script type="text/javascript">
        var $190 = $;
    </script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.min.js"></script> 
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.imgbox.pack.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.freezeheader.js"></script>                                       
                                                 

<script type="text/javascript" src="/cbtconsole/js/warehousejs/jBox/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jBox/i18n/jquery.jBox-zh-CN.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/packagelist.js"></script>
                              
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="com.cbt.processes.servlet.Currency" %>
<%@ page import="java.util.Map" %>
 <style type="text/css">                
body{font-family: arial,"Hiragino Sans GB","Microsoft Yahei",sans-serif;}
.mod_pay4 {
	width: 720px;
	position: fixed;
	top: 100px;
	left: 45%;
	z-index: 1011;
	background: gray;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 1011;
	border: 15px solid #33CCFF;
}
span{text-indent : 0em;word-spacing : 1px;letter-spacing : 1px;overflow : visible;color : #980013;font-size : 10pt;}                                                                  
                                                       
 </style>
<script type="text/javascript">
//多版本 jquery 共存 ,引入的js顺序不能改变  
/*  var $jq = jQuery.noConflict(true); 
 */
var count = "${fn:length(list)}"
var len = 0;
function insertSbxx(){
	 var zwpm = $("#sr_zwpm").val();
	 var ywpm = $("#sr_ywpm").val();
	 var px = $("#sr_px").val();
	 
	 $.ajax({          
		type:"post",                             
		url:"insertSbxx",
		dataType:"text",
		data:{'zwpm':zwpm,'ywpm':ywpm,'px':px},
		success : function(data){  
			if(Number(data)>0){
				alert("成功");
			}
		}
	 });
}
// 计算包裹预估运费
var falg = 0;
function getFreight_package(eur,weight,volume,countryid,index,day,user_id,order_no,transport,sp_id){
	var t =volume.split("*");
	var vol;
	if(t.length>2){
		vol = (t[0]*0.01)*(t[1]*0.01)*([2]*0.01);
	}
	var subShippingmethod=$("#fpxProductCode"+index+"").val();
// 	if((transport=="Epacket" || transport=="原飞航") && (subShippingmethod=="运输方式" || subShippingmethod=="")){
// 		alert("请选择运输方式");
// 		return;
// 	}
	$("#yfId"+index).val(0);
	$("#h_yfId"+index).val(0);
	var rmb_eur=$("#eur_"+index).val();
	$.ajax({          
		type:"post", 
// 		url:"/cbtconsole/feeServlet?action=getFreight_package&className=GetFright",
		//新的运费接口
		url:"/cbtconsole/freightFeeController/getOrderFreight",
		dataType:"text",
		async: false,
		data:{'weight':weight,'volume':vol,'countryid':countryid,'transport':transport,'subShippingmethod':subShippingmethod,'volumes':volume,"sp_id":sp_id},
		success : function(data){
			if(data<=0){
				$.ajax({          
					type:"post", 
					url:"/cbtconsole/feeServlet?action=getFreight_package&className=GetFright",
					dataType:"json",
					async: false,
					data:{'weight':weight,'volume':vol,'countryid':countryid},
					success : function(data){
						len++;
						var ds = day.split("-");
						var d;
						var ds2;
						var d2;
						var dataIndex = -1;                                     
						var min = 999;
						for(var i=0; i<data.length; i++){
							ds2 = data[i].days.split("-");
							if(ds.length>1 && ds2.length>1){
								if(Math.abs(ds2[0]-ds[0]) < min){
									min = Math.abs(ds2[0]-ds[0]);
									dataIndex = i;
								}
							}       
						}
						if(dataIndex!=-1){                                                 
							var yfTemp = data[dataIndex].result;                                      
							$("#yfId"+index).val(parseInt(yfTemp));//.toFixed(2)
                            document.getElementById("ygyf"+index+"").innerText=(yfTemp*rmb_eur).toFixed(2);
							$("#h_yfId"+index).val(parseInt(yfTemp));//.toFixed(2)
						}else{
                            document.getElementById("ygyf"+index+"").innerText=0;
						}
					}
					})
			}else{
				$("#yfId"+index).val(data);//.toFixed(2)
                document.getElementById("ygyf"+index+"").innerText=(data*rmb_eur).toFixed(2);
				$("#h_yfId"+index).val(data);//.toFixed(2)
			}
			$("#yffs"+index).html(transport);
			var h_hxfk = $("#h_hxfk"+index).val();			//还需支付金额
			var h_khfk = $("#h_khfk"+index).val();   		//已支付产品金额(包含赠送运费)
			var h_orderid = $("#h_orderid2"+index).val();  //订单号
			var yzfyf = $("#yzfyf"+h_orderid).html();//已支付运费
			var h_flag = $("#h_flag"+index).val();			//默认出货
			var sumPrice = $("#sumPrice"+h_orderid).val();//总运费
			sumPrice = Number(sumPrice)*Number(eur);                           
			var h_ygyf = $("#h_ygyf"+h_orderid).val();//预估运费          
			
			var h_sfmy = $("#h_sfmy"+index).val();  //是否免邮             
			if(h_sfmy==1){
				$("#"+index).css("background-color","#D7FFEE"); 
				$("#spjg"+h_orderid).html("正常");
				return;
			}
			                                               
//			1、仅支付产品费未支付运费           
//			   当计算的运费大于预估运费（计算运费>1.5*预估运费）                       
//			   需要出库人员确认后，才可以按照原价收取运费。                                  
//			   运费到账后可以出货..                                    
	//		alert("已支付产品金额："+h_khfk+"--已支付运费"+yzfyf+"---计算运费"+sumPrice+"---预估运费"+h_ygyf);
			if(h_khfk>0 && yzfyf==0){
				if(sumPrice>(h_ygyf*1.5)){                                     
					//$("#h_flag"+index).val("0"); //不出货
					$("#"+index).css("background-color","#FFB5B5"); 
					var inputHtml = "允许出货:<input id='chk"+order_no+"' onclick='cliFun(\""+order_no+"\")' type='checkbox' />";
					$("#sfyxch"+h_orderid).html(inputHtml);
					var msgHtml = "在仅支付产品费未支付运费。计算的运费大于预估运费";
					$("#spjg"+h_orderid).html(msgHtml);
					
					
					
					return ;               
				}                                                           
			}                  
//			2、支付产品费和运费
//			   当计算的运费大于预估运费（计算运费>1.5*预估运费）  
//			   需要出库人员确认后，才可以出库
			if(h_khfk>0 && yzfyf>0){
				if(sumPrice>(h_ygyf*1.5)){
					//$("#h_flag"+index).val("0"); //不出货
					$("#"+index).css("background-color","#FFB5B5"); 
			       
					var inputHtml = "允许出货:<input id='chk"+order_no+"' onclick='cliFun(\""+order_no+"\")' type='checkbox' />";
					$("#sfyxch"+h_orderid).html(inputHtml);
					var msgHtml = "在支付产品费和运费。计算的运费大于预估运费";
					$("#spjg"+h_orderid).html(msgHtml);
					return ;
				}                                                        
			}

			$("#spjg"+h_orderid).html("正常");
			$("#sfyxch"+h_orderid).html("");              
			$("#"+index).css("background-color","#D7FFEE");
			//运费预警
			var a=Number(document.getElementById("cgje"+order_no+"").innerHTML)+Number(document.getElementById("ygyf"+index+"").innerHTML);
			var b=$("#payPrice"+order_no+"").val();
			b=b.replace(",","");
			if(Number(a)-Number(b)<=10){
                $("#spjg1"+h_orderid).html("采购额加运费超出销售金额");
                $("#yjremark"+order_no).attr('disabled',false);
                $("#inRemark"+order_no).attr('disabled',false);
                $("#plckid").attr('disabled',true);
                $("#disableWare").val("1");
            }else{
                $("#spjg1"+h_orderid).html("可以出运");
                $("#yjremark"+order_no).attr('disabled',true);
                $("#inRemark"+order_no+"").attr('disabled',true);
                $("#plckid").attr('disabled',false);
			}
		}
	});
}
                                                                                     
 function jssfch(eur,weight,volume,countryid,index,day,user_id,order_no){
	 var h_hxfk = $("#h_hxfk"+index).val();			//还需支付金额
		var h_khfk = $("#h_khfk"+index).val();   		//已支付产品金额(包含赠送运费)
		var h_orderid = $("#h_orderid2"+index).val();  //订单号
		var yzfyf = $("#yzfyf"+h_orderid).html();//已支付运费
		var h_flag = $("#h_flag"+index).val();			//默认出货
		var sumPrice = $("#sumPrice"+h_orderid).val();//总运费
		sumPrice = Number(sumPrice)*Number(eur);
		var h_ygyf = $("#h_ygyf"+h_orderid).val();//预估运费          
		var h_sfmy = $("#h_sfmy"+index).val();  //是否免邮             
		if(h_sfmy==1){
			$(".mf_"+h_orderid).val("1");             
			$("tr."+h_orderid).css("background-color","#D7FFEE");
			$("#spjg"+h_orderid).html("正常");
			return ;
		}
//		1、仅支付产品费未支付运费                           
//		   当计算的运费大于预估运费（计算运费>1.5*预估运费）                       
//		   需要出库人员确认后，才可以按照原价收取运费。              
//		   运费到账后可以出货..                                    
//		alert("已支付产品金额："+h_khfk+"--已支付运费"+yzfyf+"---计算运费"+sumPrice+"---预估运费"+h_ygyf);
		
		if(h_khfk>0 && yzfyf==0){
			if(sumPrice>(h_ygyf*1.5)){ 
				
		//		$("#h_flag"+index).val("0"); //不出货
				$(".mf_"+h_orderid).val("0");
				$("tr."+h_orderid).css("background-color","#FFB5B5"); 
				var inputHtml = "允许出货:<input id='chk"+h_orderid+"' onclick='cliFun(\""+h_orderid+"\")' type='checkbox' />";
				$("#sfyxch"+h_orderid).html(inputHtml);
				
				var msgHtml = "在仅支付产品费未支付运费。计算的运费大于预估运费";
				$("#spjg"+h_orderid).html(msgHtml);
				return ;
			}                             
		}                  
//		2、支付产品费和运费
//		   当计算的运费大于预估运费（计算运费>1.5*预估运费）  
//		   需要出库人员确认后，才可以出库
		if(h_khfk>0 && yzfyf>0){                                   
			if(sumPrice>(h_ygyf*1.5)){          
				$(".mf_"+h_orderid).val("0");
				$("tr."+h_orderid).css("background-color","#FFB5B5"); 
				var inputHtml = "允许出货:<input id='chk"+h_orderid+"' onclick='cliFun(\""+h_orderid+"\")' type='checkbox' />";
				$("#sfyxch"+h_orderid).html(inputHtml);
				
				var msgHtml = "在支付产品费和运费。计算的运费大于预估运费";
				$("#spjg"+h_orderid).html(msgHtml);
				return ;
			}                                                        
		}     
		$(".mf_"+h_orderid).val("1");             
		$("tr."+h_orderid).css("background-color","#D7FFEE"); 
		$("#sfyxch"+h_orderid).html("");   
		$("#spjg"+h_orderid).html("正常");                               
 }
 //是否确认出货
 function cliFun(orderid){
	 if($("#chk"+orderid).attr("checked")){                                                      
		 $(".mf_"+orderid).val("1");
			$("tr."+orderid).css("background-color","#D7FFEE"); 
			return ;                 
	 }else{
		 $(".mf_"+orderid).val("0");                 
			$("tr."+orderid).css("background-color","#FFB5B5"); 
			return ;  
	 }           
 }              
 //显示图片
 function testFun(shipmentno){
	 var showHtml = $("#divshow"+shipmentno).html();
	 layer.open({
		  type: 1,
		  title: false,
		  closeBtn: 0,
		  shadeClose: true,
		  skin: 'yourclass',
		  content: showHtml
		});                                            
 }        
 
 //选择申报
 function selChlick(orderid){
	 
	 var strText = $("#sel"+orderid+" option:selected").text();
	 var strVal = $("#sel"+orderid+" option:selected").val();
	 $("input[id='sbzwpm"+orderid+"']").val(strText);
	 $("input[id='sbywpm"+orderid+"']").val(strVal);                                                                  
 }
 //选择申报                    
 function selChlick2(orderid,zwpm,ywpm){
	 $("input[id='sbzwpm"+orderid+"']").val(zwpm);                  
	 $("input[id='sbywpm"+orderid+"']").val(ywpm);                                                                  
 }
 
 

 /*
 中文品名：<input id="sbzwpm${obj.orderid }" style="width: 123px" type="text" name='sbzwpm${obj.shipmentno }'/><br/>
	英文品名：<input id="sbywpm${obj.orderid }" style="width: 123px" type="text" name='sbywpm${obj.shipmentno }'/><br/>
	配货备注：<input id="sbphbz${obj.orderid }" style="width: 30px" type="text" name='sbphbz${obj.shipmentno }'/>
	数&#12288;&#12288;量：<input id="sbsl${obj.orderid }" style="width: 30px" value="1" type="text" name='sbsl${obj.shipmentno }'/><br/>                     
	价&#12288;&#12288;格：<input id="sbjg${obj.orderid }" style="width: 30px" type="text" name='sbjg${obj.shipmentno }' 
	value="<fmt:formatNumber  value='${obj.sumcgprice*0.4/obj.cont*rtu }' pattern='0.00'/>"/>
	单&#12288;&#12288;位：<input id="sbdw${obj.orderid }" style="width: 30px" value="usd"  type="text" name='sbdw${obj.shipmentno }'/>         
	*/

	function test123(){
		$jq.jBox.tip(" 你懂的...", 'loading');
		// 模拟2秒后完成操作
		window.setTimeout(function () { $jq.jBox.tip('已完成。', 'success'); }, 2000);
	}
	
	//全选 
	function  AllChoose(){
		if($190("#allSelcheckid").prop("checked") == true){
			$190("input[name='cbox']").prop('checked',true );//全选 
			$190("#tabId tr:not(:first)").each(function(){
				  if($190(this).css("display")=="none"){
					  $190(this).find("input[name='cbox']").prop('checked',false);
				  }
			});
		}else{
			$190("input[name='cbox']").prop('checked',false);//反选  
		} 
		
	}
	
</script>

</head>
<body style="background-color : #F4FFF4;" onclick="">
<div align="center">
	<input type="hidden" id="disableWare" value="0">
	<div><h1>包裹列表</h1>                                    
			<!-- <a href="/cbtconsole/warehouse/getForwarderPlck.do" target="_blank">已出货列表</a> -->
			&nbsp;&nbsp;&nbsp;   
			<!-- <a href="/cbtconsole/warehouse/getJcexPrintInfoPlck.do" target="_blank">佳成运单批量导出</a> -->
			&nbsp;&nbsp;&nbsp;                                               
			<!-- <a  href="/cbtconsole/website/shipment.jsp" target="_blank">导入运费模板</a> -->
			&nbsp;&nbsp;&nbsp;  
			<!-- <input id="showCqOrder" type="button" value="显示差钱订单" onclick="isKq()"/>&nbsp;&nbsp;(<span id="yccont"></span>) -->
	</div>  
	
	<%  if(!"0".equals(user.getRoletype())){%>
		<!-- 载入消息提醒jsp页面 -->
		<jsp:include page="message_notification.jsp"></jsp:include>
	<%}  %>
	                                                                                                   
	<div style="display: none;">
		中文品名：<input type="text" id="sr_zwpm"/> <br/>
		中文品名：<input type="text" id="sr_ywpm"/> <br/>
		(输入拼音的第一个字母,比如裤子就输入：kz) <br/>
		排序：<input type="text" id="sr_px"/><br/>
		<input type="button" value="添加" onclick="insertSbxx()"/>
	</div>                        
	<div id="msginfo"></div>
	<br/><br/>                
	                                                          
	<!-- 表格 -->                                                     
	<div id="box">                           
		<table id="tabId" class="altrowstable" style="width: 1850px;">    
		<thead>                                                                                    
			<tr id="123">   
			    <td>全选<input type="checkbox" id="allSelcheckid"  style="width:15px;height:15px;" onclick="AllChoose()"></td>                                                                                                                                                                                  
				<td>包裹号</td>                                                                                             
				<td style="word-wrap:break-word;width:264px;">综合信息</td>                                                                                                      
				<td>重量(KG)</td>                   
				<td>体积(CM<sup>3</sup>)</td>            
				<td>客户地址</td>            
				<td>客户选择交期</td>                     
				<td>客户选择运输方式</td>                  
				<td>估算交期(天)</td>                             
				<td>估算运费(USD)</td>                           
				<td>估算运输方式</td>  
				<!-- <td>是否支付额外运费</td>   --><!--暂时隐藏  -->
				<td style="width:200px">运输公司</td>
				<td>运输方式</td>               
				<td>申报信息</td> 
				<td>申报种类</td>    
				<td>还需付款(USD)</td>                                                                                                    
				<td>商品图片</td>                                                      
			</tr>                                        

		</thead>                     
		<tbody>
			<c:forEach items="${list }" var="obj" varStatus="s">
				<!-- 所有金额 -->

				<c:set value="${ fn:split(obj.allprice, ',') }" var="str1" />
				<c:forEach items="${ str1 }" var="s">
					<c:if test="${fn:contains(s,'sumProduct_cost')}">
						<c:set value="${fn:replace(s, 'sumProduct_cost:', '')}" var="sumProduct_cost" /><!-- sumDiscount_amount -->
					</c:if>
					<c:if test="${fn:contains(s,'sumPayment_amount')}">
						<c:set value="${fn:replace(s, 'sumPayment_amount:', '')}" var="sumPayment_amount" /><!-- 已支付产品总金额: -->
					</c:if>
					<c:if test="${fn:contains(s,'sumPay_price_tow')}">
						<c:set value="${fn:replace(s, 'sumPay_price_tow:', '')}" var="sumPay_price_tow" /><!-- 已支付运费 -->
					</c:if>
					<c:if test="${fn:contains(s,'sumOrder_ac')}">
						<c:set value="${fn:replace(s, 'sumOrder_ac:', '')}" var="sumOrder_ac" /><!-- 赠送的总费用 -->
					</c:if>
					<c:if test="${fn:contains(s,'sumForeign_freight')}">
						<c:set value="${fn:replace(s, 'sumForeign_freight:', '')}" var="sumForeign_freight" /><!-- 预估费用 -->
					</c:if>  
					<c:if test="${fn:contains(s,'sumDiscount_amount')}">
						<c:set value="${fn:replace(s, 'sumDiscount_amount:', '')}" var="sumDiscount_amount" /><!-- 预估费用 -->
					</c:if> 
				</c:forEach>
				<c:set value="0" var="v_set"></c:set>  
				<c:if test="${fn:contains(obj.mode_transport,'@0.0@all')}">
					<c:set value="1" var="v_set"></c:set>
				</c:if>    
			                                                                                                                                                                                                                                     
			<tr <%-- id="${obj.shipmentno }"  --%>  class="${obj.orderid }"     bgcolor=${obj.overTimeFlag==1?"#FFD39B":"#d7ffee"}>
			    <td style="width:50px"><input  type="checkbox" name="cbox" style="width:15px;height:15px;"  value="${obj.shipmentno}"></td>
				<td style="width: 10px" id='bgh${obj.shipmentno }'>${obj.shipmentno }</td> <!-- 包裹号 -->
				<c:if test="${tid != obj.orderid }"><!-- 用户信息 -->                                
					<td style="width: 150px;"  id="yhxxhb${obj.orderid }">       
						用户id：<span  id="userid${obj.orderid }">${obj.user_id }</span><br/>  <!-- user_id-->  
						订单号：<span  id="${obj.orderid }">	
						<!--  -->
						<!-- <a  target="_blank" href="/cbtconsole</a> -->
							<a  target="_blank" href="/cbtconsole/orderDetails/queryByOrderNo.do?&orderNo=${obj.orderid }">${obj.orderid }</a>
							<c:if test="${obj.isDropshipFlag==1}">
							  <img  src="/cbtconsole/img/ds1.png" style="width:25px;margin-top: 2px;"  title="drop shipping">
							</c:if>
						</span> <br/> 
						订单付款时间:<span>${obj.orderpaytime }</span>
						<input type="hidden" id="isDropshipFlag${obj.shipmentno}"  value="${obj.isDropshipFlag}">         
						=======================<br/>
						<input type="hidden" id="eur_${obj.shipmentno}" value="${obj.exchange_rate}">
						采购金额&#12288;:<span id="cgje${obj.orderid }">${obj.sumcgprice}</span>(RMB)<br/>
						质检费&#12288;:<span id="jzf${obj.orderid }">${obj.actual_lwh}</span>(USD)<br/>
						客户已付款:<span id="khfk${obj.orderid }"></span>
						<fmt:formatNumber type="number" value="${obj.sumprice*obj.exchange_rate}"/>
						</span>(RMB)<br/>   <!-- 客户已付款 -->
						<input type="hidden" id="payPrice${obj.orderid }" value="${obj.sumprice*obj.exchange_rate}">
						还需付款&#12288;:<span id="hxfk${obj.orderid }">                        
						<fmt:formatNumber type="number" value="${obj.remaining_price==null?0:obj.remaining_price*obj.exchange_rate}"/>
						</span>(RMB)<br/>   <!-- 还需付款-->
						预估运费:<span >
						<span id="ygyf${obj.shipmentno}">${obj.estimatefreight}</span>
						<%--<fmt:formatNumber type="number" value="${obj.estimatefreight*eur}"/>--%>
						</span>(RMB)<br/>   <!-- 预估运费-->                                
						                                   
						已支付运费:<span id="yzfyf${obj.orderid }">
						<fmt:formatNumber type="number" value="${sumPay_price_tow*obj.exchange_rate}"/>
						</span>(RMB)<br/>   <!-- 已支付运费-->
						用户订单备注:<br/>
						<span>${obj.orderremark}</span>
						=======================<br/>
						警告：<span id="spjg${obj.orderid }">;<span id="fright_${obj.orderid }"></span>
						</span>
						=======================<br/>
						运费预警：<span id="spjg1${obj.orderid }"></span><br>
						<textarea id="yjremark1${obj.orderid }" style="height:100px;" disabled="disabled">${obj.yjRemark}</textarea><br>
						<textarea id="yjremark${obj.orderid }"></textarea><br>
						<input type="button" value="提交备注允许出库" id="inRemark${obj.orderid }"  onclick="insertWarningInfo('${obj.orderid }');"/>
						</span>
						<!-- 是否允许出货 -->
					    <div id="sfyxch${obj.orderid }"></div>
					</td>
				</c:if>
				<input type="hidden" id="spid_${obj.shipmentno}" value="${obj.id}"/>
				<td style="width: 10px" id="sweight${obj.shipmentno }">${obj.sweight }</td>    <!-- 重量 -->
				<td style="width: 10px" id="svolume${obj.shipmentno }">${obj.svolume }</td>    <!-- 体积 -->
				<td style="width: 45px">                            
				<c:set var="ssst" value="${obj.uod.address}${obj.uod.userstreet}"/>                                    
				<c:set value="${ fn:split(ssst, ',') }" var="s1" />
				<c:forEach items="${ s1 }" var="st">         
					${st }<br/>                                                             
				</c:forEach>                                                                                                             
				</td>  <!-- 客户地址-->                                                                              
				<td style="width: 65px">${obj.day } </td>              		 <!-- 客户选择交期 -->
				<td style="width: 90px">${obj.transport } </td>              <!-- 客户选择原方式 -->
				<td style="width: 45px" id="yjjq${obj.shipmentno }">0</td><!-- 估算交期 -->                                 
				<td style="width: 45px">
					<input style="width: 40px" type="text" disabled="disabled" id="yfId${obj.shipmentno }" value="0"/>
					<input style="width: 30px" type="hidden"id="h_yfId${obj.shipmentno }" value="0"/>
				</td><!-- 估算运费 --> 
				<td style="width: 65px" id="yffs${obj.shipmentno }">0</td><!-- 估算运输方式 -->
				<!-- 暂时隐藏 -->
				<%-- <td style="width: 65px" >
				<c:if test="${obj.sumextra_freight>0}">
				<font color="red">有，支付金额为：${obj.sumextra_freight}(RMB)</font>
				</c:if>
				<c:if test="${obj.sumextra_freight==0}">
				<font color="red">无</font>
				</c:if>
				</td>    --%>           
				<td style="width: 75px" id="ysgs${obj.shipmentno }">     <!-- 运输公司 -->                                                  
					<!-- 出货方式 -->
					<c:choose>
						<c:when test="${fn:toLowerCase(obj.transportcompany) =='epacket' || fn:toLowerCase(obj.transportcompany) =='emsinten'}">
							<input  value="1" onclick="getYsfs('${obj.shipmentno }')"  type="radio" name="ysfs${obj.shipmentno }"/>4PX<br/>
							<input  value="2" onclick="getYsfs('${obj.shipmentno }')" type="radio" name="ysfs${obj.shipmentno }"/>原飞航(${obj.yfhFreight})<br/>
							<input  value="3" onclick="getYsfs('${obj.shipmentno }')" type="radio" name="ysfs${obj.shipmentno }"/>佳成(${obj.jcexFreight})<br/>
							<input  value="4" onclick="getYsfs('${obj.shipmentno }')" checked="checked"  type="radio" name="ysfs${obj.shipmentno }"/>邮政 <br/>
							<input  value="5" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>中通 <br/>
							<input  value="6" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>迅邮<br/>
							<input  value="7" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>飞特
							<input type="hidden" id="yz_type" value="${obj.shippingtype }"/>
						</c:when>
						<c:when test="${fn:toLowerCase(obj.transportcompany) =='原飞航'}">
							<input  value="1" onclick="getYsfs('${obj.shipmentno }')"  type="radio" name="ysfs${obj.shipmentno }"/>4PX<br/>
							<input  value="2" onclick="getYsfs('${obj.shipmentno }')" checked="checked" type="radio" name="ysfs${obj.shipmentno }"/>原飞航(${obj.yfhFreight})<br/>
							<input  value="3" onclick="getYsfs('${obj.shipmentno }')" type="radio" name="ysfs${obj.shipmentno }"/>佳成(${obj.jcexFreight})<br/>
							<input  value="4" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>邮政 <br/>
							<input  value="5" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>中通 <br/>
							<input  value="6" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>迅邮<br/>
							<input  value="7" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>飞特
							<input type="hidden" id="yfh_eara" value="${obj.shippingtype }"/>
					</c:when>
						<c:when test="${fn:toLowerCase(obj.transportcompany) =='jcex'}">
							<input  value="1" onclick="getYsfs('${obj.shipmentno }')"  type="radio" name="ysfs${obj.shipmentno }"/>4PX<br/>
							<input  value="2" onclick="getYsfs('${obj.shipmentno }')" type="radio" name="ysfs${obj.shipmentno }"/>原飞航(${obj.yfhFreight})<br/>
							<input  value="3" onclick="getYsfs('${obj.shipmentno }')" checked="checked" type="radio" name="ysfs${obj.shipmentno }"/>佳成(${obj.jcexFreight})<br/>
							<input  value="4" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>邮政 <br/>
							<input  value="5" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>中通 <br/>
							<input  value="6" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>迅邮<br/>
							<input  value="7" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>飞特
						</c:when>
						<c:when test="${fn:toLowerCase(obj.transportcompany) =='zto'}">
							<input  value="1" onclick="getYsfs('${obj.shipmentno }')"  type="radio" name="ysfs${obj.shipmentno }"/>4PX<br/>
							<input  value="2" onclick="getYsfs('${obj.shipmentno }')" type="radio" name="ysfs${obj.shipmentno }"/>原飞航(${obj.yfhFreight})<br/>
							<input  value="3" onclick="getYsfs('${obj.shipmentno }')" type="radio" name="ysfs${obj.shipmentno }"/>佳成(${obj.jcexFreight})<br/>
							<input  value="4" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>邮政 <br/>
							<input  value="5" onclick="getYsfs('${obj.shipmentno }')" checked="checked"  type="radio" name="ysfs${obj.shipmentno }"/>中通 <br/>
							<input  value="6" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>迅邮<br/>
							<input  value="7" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>飞特
						</c:when>
						<c:when test="${fn:toLowerCase(obj.transportcompany) =='飞特' }">
							<input  value="1" onclick="getYsfs('${obj.shipmentno }')"  type="radio" name="ysfs${obj.shipmentno }"/>4PX<br/>
							<input  value="2" onclick="getYsfs('${obj.shipmentno }')" type="radio" name="ysfs${obj.shipmentno }"/>原飞航(${obj.yfhFreight})<br/>
							<input  value="3" onclick="getYsfs('${obj.shipmentno }')" type="radio" name="ysfs${obj.shipmentno }"/>佳成(${obj.jcexFreight})<br/>
							<input  value="4" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>邮政 <br/>
							<input  value="5" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>中通 <br/>
							<input  value="6" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>迅邮<br/>
							<input  value="7" onclick="getYsfs('${obj.shipmentno }')"  checked="checked" type="radio" name="ysfs${obj.shipmentno }"/>飞特
						</c:when>
						<c:otherwise>
							<input  value="1" onclick="getYsfs('${obj.shipmentno }')"  type="radio" name="ysfs${obj.shipmentno }"/>4PX<br/>
							<input  value="2" onclick="getYsfs('${obj.shipmentno }')" type="radio" name="ysfs${obj.shipmentno }"/>原飞航(${obj.yfhFreight})<br/>
							<input  value="3" onclick="getYsfs('${obj.shipmentno }')"  type="radio" name="ysfs${obj.shipmentno }"/>佳成(${obj.jcexFreight})<br/>
							<input  value="4" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>邮政 <br/>
							<input  value="5" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>中通 <br/>
							<input  value="6" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>迅邮<br/>
							<input  value="7" onclick="getYsfs('${obj.shipmentno }')"   type="radio" name="ysfs${obj.shipmentno }"/>飞特
						</c:otherwise>
					</c:choose>

				</td>                                                                                                                                                                           
				<td style="width: 150px" id="xz_ysfs${obj.shipmentno }">  
				 ${obj.xsbz }  <br/>                                                          
					<select id="fpxCountryCode${obj.shipmentno }" >
	 	  					<option value="运输国家">运输国家</option>                                                                                                      
	  		 		</select><br/><br/>                                                                                                 
	                       <input id="hgj_${obj.shipmentno }" type="hidden" value="${obj.shipmentno }" />  <!-- 辅助选择国家 -->
	                       <input id="hchinapostbig_${obj.shipmentno }" type="hidden" value="${obj.chinapostbig}" />  <!-- 辅助选择国家 -->                                                   
				                                                                                                                                                                     
		 			<select id="fpxProductCode${obj.shipmentno }" onchange="yfjs(${obj.shipmentno });">                                                   
	 	 				<option  value="运输方式">运输方式</option>
					</select>                                                                                                                                                                                                                      
				</td>                                                                                                                    
				<td style="width: 220px" id="sbxx${obj.orderid }"> <!-- 申报信息 -->  
							<c:if test="${tid != obj.orderid }">  
							<!--       
							<select id="sel${obj.orderid }" onchange="selChlick('${obj.orderid }')">
								<option>请选择</option>                                                                     
								<option value="watch">表</option>
								<option value="clothes">衣服</option>
							</select>
							<br/>     
							 -->                                                                                                 
							</c:if>
							<c:if test="${obj.cacount>0}">
								<button onclick="openTipInfo('${obj.orderid}');">查看申报注意事项</button><br/>
							</c:if>
							中文品名：<input id="sbzwpm${obj.orderid }" style="width: 123px" type="text" name='sbzwpm${obj.shipmentno }'/><br/>
							英文品名：<input id="sbywpm${obj.orderid }" style="width: 123px" type="text" name='sbywpm${obj.shipmentno }'/><br/>
							配货备注：<input id="sbphbz${obj.orderid }" style="width: 30px" type="text" name='sbphbz${obj.shipmentno }'/>
							数&#12288;&#12288;量：<input id="sbsl${obj.orderid }" class="sbsl${obj.shipmentno }" style="width: 30px" value="1" type="text" name='sbsl${obj.shipmentno }' onblur="checkAmount('${obj.shipmentno }','${obj.orderid }');"/><br/>
		 					价&#12288;&#12288;格：<input id="sbjg${obj.orderid }" class="sbjg${obj.shipmentno }" style="width: 30px" type="text" name='sbjg${obj.shipmentno }' onblur="checkAmount('${obj.shipmentno }','${obj.orderid }');"
		 					value="<fmt:formatNumber  value='${obj.sumcgprice*0.4/obj.cont*obj.exchange_rate }' pattern='0.00'/>"/>
	    			 		单&#12288;&#12288;位：<input id="sbdw${obj.orderid }" style="width: 30px" value="usd"  type="text" name='sbdw${obj.shipmentno }'/>                   
						          
						          <script>                                            
						          		//不同重量设置不同 申报
						          		var zl  = Number($("#sweight${obj.shipmentno }").html());
						          		if(zl<=3){
						          			$(".sbsl${obj.shipmentno }").val("5");
						          			$(".sbjg${obj.shipmentno }").val("20");
						          		}else if(zl>=4 && zl<=8){
						          			$(".sbsl${obj.shipmentno }").val("15");
						          			$(".sbjg${obj.shipmentno }").val("45");
						          		}else if(zl>=9 && zl<=12){
						          			$(".sbsl${obj.shipmentno }").val("45");                               
						          			$(".sbjg${obj.shipmentno }").val("90");
						          		}                                                
						          </script>                                                                                                                                                             
				</td>                                                                                                                                                                        
                                                                                                                              
				<!-- 计算是否需要合并 -->                                               
				<input type="hidden" id="h${obj.orderid }"/>
				                                                                                                               
				<!-- 是否还需付款，用来判断出库 -->      
				<input type="hidden" id="h_sfmy${obj.shipmentno }" value="${v_set }"/>
				<input type="hidden" id="z_id${obj.shipmentno }" value="${obj.zid }"/>
				<input type="hidden" id="h_hxfk${obj.shipmentno }" value="${obj.remaining_price}"/>
				<input type="hidden" id="h_khfk${obj.shipmentno }" value="${obj.sumprice}"/>    
				<input type="hidden" id="h_orderid2${obj.shipmentno }" value="${obj.orderid }"/><!-- 订单号 -->
				<input type="hidden" id="h_flag${obj.shipmentno }" class="mf_${obj.orderid  }" value="1"/><!-- 是否出货标志   1代表可以-->                                                           
				<c:if test="${tid != obj.orderid }">
					<input type="hidden" id="h_ygyf${obj.orderid }" value="${sumForeign_freight*obj.exchange_rate }"/><!-- 预估运费-->
					<input type="hidden" id="h_orderid${obj.shipmentno }" value="${obj.orderid }"/><!-- 订单号 -->
					          
					<td id="sbxx_${obj.orderid }" style="width: 100px">
						<c:forEach items="${sbxxlist }" var="sbxx">
							<input onclick="selChlick2('${obj.orderid }','${sbxx.zwpm }','${sbxx.ywpm }')" type="button" value="${sbxx.zwpm }">    
						</c:forEach>
					</td>                              
					<td id="hb_yf${obj.orderid }">  
					<!-- 是否免邮&#12288;: -->
					<span>${v_set==1? '免邮':'非免邮' }</span><br/>   <!-- 是否免邮 -->  
					<c:if test="${obj.issendmail =='0'}">
						未通知客户付运费
					</c:if>
					<c:if test="${obj.issendmail =='1'}">
						已通知客户付运费
					</c:if>
					<br/>
					<input onkeyup="jssfch('${obj.exchange_rate}','${obj.sweight }','${obj.svolume }','${obj.zid }','${obj.shipmentno}','${obj.day }','${obj.user_id}','${obj.orderid}')" type="text" id="sumPrice${obj.orderid }" value="${obj.remaining_price}" style="width: 25px"/>
					<input type="hidden" value="${obj.issendmail }" id="issendmail${obj.orderid }"/>
					
					<input type="hidden" id="remaining_price${obj.orderid }" value="${obj.remaining_price}"/>
					<input type="button" value="通知客户付运费" onclick="sendMail('${obj.userid }','${obj.orderid }','${obj.email }','${obj.remarks }','${obj.currency}')"/>
					</td>    <!-- 总运费-->                         
					  
					                                                                                                                                                                                                        
					<!-- 所有图片 --> 
					<td id="jqxx${obj.orderid }">
						<c:forEach items="${obj.listImg }" var="img" varStatus="si">
						                           
						<c:if test="${si.index <4}">
							<a id="example-${obj.shipmentno }_${si.index }" href="${img }">
							      <img src="${img }" style="width: 50px; height: 50px;">   
							</a>                                                                                                                   
							                               
						<script type="text/javascript">  
						$('#example-${obj.shipmentno }_${si.index }').imgbox({
								'speedIn'		: 0,
								'speedOut'		: 0, 
								'alignment'		: 'center',
								'overlayShow'	: true,                       
								'allowMultiple'	: false,
								'zoomOpacity' : true
							});                      
						</script>            
						</c:if>   
							                        
						</c:forEach>   
						<c:if test="${fn:length(obj.listImg) >4}">
						<br/>
						&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;&#12288;
						&#12288;&#12288;
						<a href="javascript:void(0);" onclick="testFun('${obj.shipmentno }')">查看更多</a>
						</c:if>                                                      
					</td>              
					 <div style="display: none;"  id="divshow${obj.shipmentno }">
						 <div>
							 <c:forEach items="${obj.listImg }" var="img" varStatus="imgsi">
							    <img  style="width: 80px; height: 80px;" src="${img }">                                                        
							 </c:forEach>                                   
						 </div>                                                                                                                                      
					</div>                                                                                         
					
				</c:if>                                                                  
				<script type="text/javascript">
					//计算运费              
					//getFreight_package("${obj.exchange_rate}","${obj.sweight }","${obj.svolume }","${obj.zid }","${obj.shipmentno}","${obj.day }",'${obj.user_id}','${obj.orderid}','${obj.transport},'${obj.id},'${obj.expressno}');
					//合并
					$("#hb_yf${obj.orderid }").attr("rowspan",$("input[id='h${obj.orderid }']").size());  
					$("#jqxx${obj.orderid }").attr("rowspan",$("input[id='h${obj.orderid }']").size());
					$("#yhxxhb${obj.orderid }").attr("rowspan",$("input[id='h${obj.orderid }']").size());
					$("#sbxx_${obj.orderid }").attr("rowspan",$("input[id='h${obj.orderid }']").size());
					                           
				</script>                                                                      
                             
                 <script type="text/javascript">
					  getYsfs('${obj.shipmentno }')
				 </script>                                      
			<!-- 用来判断以前是否存在orderid -->                 
			<c:set value="${obj.orderid }" var="tid"></c:set>
			</tr>

			</c:forEach>  
			</tbody>	                                       
		</table>               
                                        
		<input id="plckid" type="button" value="批量出库" onclick="batchCk()" style="width: 350px"/>
	</div>                           
</div>                              
 <input type="hidden" value="/cbtconsole" id="h_path">
 
<!-- <script>      
var kg = false;
function hideOrShowTr(t){
	
	if(t){
		var yclen = 0;
		$("tr:not(:first)").each(function(){   
    		var id = $(this).attr('id');   
    		var a = $("#"+id+" div[id^='sfyxch']").html();    
    		if(a.length > 0){
    			 $("#"+id).show();  
    			 yclen++;
    		}                                                                                            
    	});                 
		$("#yccont").html(yclen);
		$("#showCqOrder").val("隐藏差钱订单");
		kg = false;                
    	
	}else{
		var yclen = 0;
		$("tr:not(:first)").each(function(){   
    		var id = $(this).attr('id');                                                                                  
    		var a = $("#"+id+" div[id^='sfyxch']").html();    
    		if(a.length > 0){
    			 $("#"+id).hide();    
    			 yclen++;                                                                             
    		}                                                                                            
    	});                                   
		$("#yccont").html(yclen);
		$("#showCqOrder").val("显示差钱订单");
		kg = true;

	}
}
var timer = setInterval(function(){    //开启定时器
    if(count==len){
    	clearInterval(timer); //清除定时器         
    	hideOrShowTr(kg);
    }
},30);    
                                                          
function isKq(){
	hideOrShowTr(kg);
}

</script>   -->
<div class="mod_pay4" style="display: none;" id="displayChangeLog">
	<div>
		<a href="javascript:void(0)" class="show_x" onclick="displayChangeLogInfo()">╳</a>
	</div>
	<center>
		<h3 class="show_h3">出货注意事项</h3>
		<table id="displayChangeLogs" class="imagetable" border="1">
			<thead>
			<tr>
				<td width='11%'>国家/地区</td>
				<td width='11%'>法规</td>
				<td width='11%'>清关要求</td>
				<td width='11%'>税则</td>
				<td width='11%'>建议物流商</td>
				<td width='11%'>不建议物流商</td>
				<td width='11%'>申报金额</td>
			</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</center>
</div>
</body>
</html>


