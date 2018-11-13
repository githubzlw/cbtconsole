<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>          
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>正式出库</title>          
</head>       
<link rel="stylesheet" href="/cbtconsole/css/warehousejs/orderfeepage.css" type="text/css">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/orderfeepage.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/table.css">

  
<body style="background-color : #F4FFF4;">  
<!-- style="display: none;" -->                      
<div id ="sbBuffer" style="display: none;">
          
                                
                                       
	
</div>                          
                  
<div class="maindiv" >     
	<!-- TOP -->    
	<div >                     
		<h1>出库管理</h1>       
		<input type="hidden" value="/cbtconsole" id="h_path">    
		<span id="msgid"></span>
		<h3><!-- 到库订单:30&nbsp;&nbsp;&nbsp;&nbsp;  -->
		 <input   checked="checked" onclick="getOrderFee(this.value,'')" type="radio" name="typeOrder" value="1"/>	审核订单数量:<span id="s_auditOrder">20</span>&nbsp;&nbsp;&nbsp;&nbsp;
		 
		 <input onclick="getOrderFee(this.value,'')" type="radio" name="typeOrder" value="2"/>	等待付款订单数量:<span id="s_arrears">20</span>&nbsp;
		 (可出库<span id="s_already_paid"></span>)
		 <select id="notMoneyOrderinfo" onchange="setCurSelValue(this)">            
				<option>无</option>
		</select>
		 <!-- &nbsp;&nbsp;&nbsp;                 
			<a href="/cbtconsole/warehouse/getForwarder.do" target="_blank">已出货列表</a>
			&nbsp;&nbsp;&nbsp;   
			<a href="/cbtconsole/warehouse/getJcexPrintInfo.do" target="_blank">佳成运单批量导出</a>
			&nbsp;&nbsp;&nbsp; 
			<a href="/cbtconsole/website/shipment.jsp" target="_blank">导入运费模板</a> -->
		</h3>
		                                          
		                              
      
	</div>                
	<!-- 下 -->
	<div>
		<!-- left -->
		<div style="float:left;height:540px; OVERFLOW-Y: auto; OVERFLOW-X:hidden; width: 240px">
			<table  border="1" style="border-width : 1px;" id="t_order">
				<tr >
		 			<td colspan='2'>userid or orderno<input oninput='getOrderInfoByUseridAndOrderid()'  id='t_useridAndorderno' type='text' />
		 			<input type='hidden' id='h_orderid'/>
		 			</td>
		 		</tr>                
			                                      
				<!-- 
				<c:forEach items="${oflist }" var="orderFeePojo" varStatus="s">
					<tr>
					<td id="userid${s.index }">${orderFeePojo.userid }</td>
					<td >
					<a id="order_no${s.index }" href='javascript:void(0);' onclick="getOrderInfo('${orderFeePojo.orderno }','${orderFeePojo.userid }')" >${orderFeePojo.orderno }</a>
					</td>  
				</tr>
				</c:forEach>
				 -->
				
				
			</table>
		</div>
		<!-- right -->
		<div style="float:right" id="d_right">
			<!-- 订单信息 -->  
		  	  
			
 			<div class="ddinfodiv">
			
				&nbsp;&nbsp;订单详情 
				<div class="alldiv">
					&nbsp;用户id:<span id="s_userid">123</span>
					&nbsp;合并的订单:(<span id="s_mergeorders">0</span>)
					<input type='hidden' id='h_mergeorders'/>
				</div>
				<br/>                  
				                   
				<div class="alldiv">         
					&nbsp;收货地址：<br/>
					&nbsp;国家:<span id="s_country">123</span>
					&nbsp;省/州:<span id="s_statename">123</span>
					&nbsp;城市:<span id="s_address2">123</span>&nbsp;&nbsp;<br/>
					&nbsp;街道:<span id="s_street">123</span><span id="s_address">123</span>
					&nbsp;电话:<span id="s_phoneNumber">123</span>
					&nbsp;邮编:<span id="s_zipcode">123</span>
					&nbsp;邮箱:<span id="s_useremail">123</span>
					         
				</div>            
				<br/>          
				             
				<div class="alldiv">
					&nbsp;Paypal地址：<br/>
					&nbsp;residence_country:<span id="s_residence_country">0</span>  
					&nbsp;receiverEmail:<span id="s_receiverEmail">0&nbsp;&nbsp;</span>
					&nbsp;address_country_code:<span id="s_address_country_code">0</span>&nbsp;&nbsp;<br/>
					&nbsp;address_country:<span id="s_address_country">0</span>&nbsp;&nbsp;
					&nbsp;address_city:<span id="s_address_city">0</span>&nbsp;&nbsp;
					&nbsp;address_state:<span id="s_address_state">0</span>&nbsp;&nbsp;<br/>
					&nbsp;address_status:<span id="s_address_status">0</span>&nbsp;&nbsp;
					&nbsp;address_street:<span id="s_address_street">0</span><br/>
				</div>
				<br/>              
				
				<div class="alldiv">
					<!-- 用户余额 -->
					&nbsp;用户余额:<span id="s_available_m">0</span>(<span id="u_currency"></span>)
					&nbsp;赠送运费:<span id="s_applicable_credit">0</span>  (<span id="u_currency"></span>)
					&nbsp;产品总金额:<span id="s_product_cost">0</span>  (<span id="u_currency"></span>)
					&nbsp;折扣金额:<span id="s_discount_amount">0</span>  (<span id="u_currency"></span>)<br/>   
					&nbsp;已支付金额:<span id="s_payment_amount">0</span><span id="u_currency"></span>(包含赠送费用：<span id="s_sumOrder_ac">0</span>)   
					&nbsp;已支付运费(参考):<span id="s_pay_price_tow">0</span><span id="u_currency"></span>
					
				  	                
					 
					<input type="hidden" id='h_currency'/><span id="u_available_m"></span>
				</div> 
				
				<br/> 
				  
			</div>
			<!-- 出货信息 -->
			<div class="ddinfodiv" id="outddinfodiv" style="height: 370px">
				&nbsp;&nbsp;出货信息          
				<br/>
				<div class="alldiv">                         
					<!-- 出货方式 -->     
						<input onclick="showByType(1)" value="1" checked="checked" type="radio" name="ysfs"/>4PX运输&nbsp;&nbsp;&nbsp;&nbsp;
			 			<input onclick="showByType(2)" value="2" id="r_yfh"type="radio" name="ysfs"/>原飞航运输&nbsp;&nbsp;&nbsp;&nbsp;
						<input onclick="showByType(3)" value="3" type="radio" name="ysfs"/>佳成运输&nbsp;&nbsp;&nbsp;&nbsp;
						<input onclick="showByType(4)" value="4" type="radio" name="ysfs"/>其他运输&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div class="zldiv" style="top : 5px;  ">
					<!-- 对应不同的输入-->             
						<div id="packageAll">
							&nbsp;产品重量：<input id="weight" type="text"/>Kg&nbsp;&nbsp;&nbsp;&nbsp;
							&nbsp;产品体积：<input id="volumelwh" type="text"/>CM<sup>3</sup>&nbsp;&nbsp;&nbsp;&nbsp;
						</div>
						<div id="d_courierNumber" style="float:left; display:none;">           
						&nbsp;快递单号：<input id="courierNumber" type="text"/>
	 	 				</div>
	 	 				(客户选择的运输时间：<span id="s_mode_transport">0</span>)
	   				                                                                                    
		   				<div id="d_fpxCountryCode" style="float:left;">
			  			&nbsp;运输国家：            
	  			 		<select id="fpxCountryCode" onchange="getYsfs()">
	  	  					<option value="运输国家">运输国家</option>
	   		 			</select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						</div>
	 	 				             
						<div id="d_fpxProductCode" style="float:left;">
						&nbsp;运输方式：
	 	 				<select id="fpxProductCode" onchange="getFreight()">
		 	 			<option value="运输方式">运输方式</option>
						</select>
						                   
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						
						</div>    
						
						<div id="d_trans" style="float:left; display:none;">
						&nbsp;区域：
						<select id="trans" onchange="getFreight()">
							<option value="">区域</option>
							<option value="美/加/墨">美/加/墨</option>
							<option value="南非">南非</option>
							<option value="澳洲">澳洲</option>
							<option value="西欧">西欧</option>
							<option value="东欧">东欧</option> 
						</select>
						</div>
		 				
						<div id="d_logisticsName" style="display:none;">
						&nbsp;物流公司名称：
						<select id="logisticsName" onchange="getCodemaster()">
							<option value="emsinten" selected="selected">EMS国际</option>
						</select>
						</div>

			 	</div>       
		  		<br/>
				<!-- 运费 -->  
				<div class="alldiv">   
				                                                              
				&nbsp;          
				预估费用：<span id="foreign_freight" ></span>(<span id="u_currency"></span>)&nbsp;&nbsp;&nbsp;&nbsp;
				未支付费用：<input id="unpaidFreight" disabled="disabled" width="10px" type="text"/>(<span id="u_currency"></span>)&nbsp;&nbsp;&nbsp;&nbsp;
				运费：<input id="actualFreight" onkeyup="packingCharge()" type="text"/>(<span id="u_currency"></span>)&nbsp;&nbsp;&nbsp;&nbsp;
	  			(抵扣赠送运费<input id="subFreightid" onclick="subFreight(this)" disabled="disabled" type="checkbox"/>)&nbsp;&nbsp;&nbsp;&nbsp;
				<br/>
	  			&nbsp;&nbsp;包装费：<input id="packingCharge" onkeyup="packingCharge()"  width="10px" type="text"/>(<span id="u_currency"></span>)&nbsp;&nbsp;&nbsp;&nbsp;
		  		<select id="trans" onchange="getCalculatedPrice(this.value)">
						<option selected="selected" value="1.0">原价</option>
						<option value="1.1">1.1</option>
						<option value="1.2">1.2</option>
						<option value="1.3">1.3</option>               
						<option value="1.4">1.4</option>                      
						<option value="1.5">1.5</option>
						<option value="1.6">1.6</option>                  
						<option value="1.7">1.7</option>
						<option value="1.8">1.8</option>
						<option value="1.9">1.9</option>
						<option value="2.0">2.0</option>
						<option value="2.1">2.1</option>
						<option value="2.2">2.2</option>
						<option value="2.3">2.3</option>
						<option value="2.4">2.4</option>
						<option value="2.5">2.5</option>
						<option value="2.6">2.6</option>
						<option value="2.7">2.7</option>
						<option value="2.8">2.8</option>
						<option value="2.9">2.9</option>
						<option value="3.0">3.0</option>
						<option value="0">免邮</option>
				</select>
				<input type="button" value="免邮" onclick="getCalculatedPrice(0)"/>          
		  	<!-- 	是否免邮：<input id="r_freeShipping" width="10px" type="checkbox"/>(<span id="s_freeShipping">0</span>)  -->
				<input id="h_unpaidFreight"  width="10px" type="hidden"/> <!-- 保存上此计算之前的未支付运费 -->
				</div>   
				<br/>                              
				                                                                       
				<!-- 出货按钮 -->                                   
				<div class="alldiv" align="center">
				<input  id="inshipment" type="button" onclick="shipment()" class="btnOut" value="出货"/>
				<input type="hidden" onclick="sendMail()" class="btnOut" value="发送邮件"/>
				<input  id="inshipment" type="hidden" onclick="upadteData()" class="btnOut" value="修改"/>
				
				<input   type="button" onclick="sendMail()" class="btnOut" value="发邮件测试"/>
				</div>
		  		<br/>                                                                                                                                                
				                                                                                  
			   	              
				<!-- 申报信息 -->
				<div  class="alldiv" id="addsbdiv"style="height: 68px">
					<div  id="sbdiv">
						<div id="subsbdiv1">
							&nbsp;中文品名：<input type="text" name='sbzwpm'/>
							&nbsp;英文品名：<input type="text" name='sbywpm'/>
							&nbsp;配货备注：<input type="text" name='sbphbz'/><br/>
							&nbsp;数        量：<input type="text" name='sbsl'/>
		 					&nbsp;价        格：<input type="text" name='sbjg'/>
	    			 		&nbsp;单        位：<input  type="text" name='sbdw'/>
	 						<input  type="button" value="删除申报" onclick="removeSbDiv('subsbdiv1')"/>
	 					</div>
						
					</div>   
					
					<input type="button" onclick="addSb()" value="添加申报"/>
					
					<input type="hidden" onclick="testPrint();" value="测试"/>
				</div>
			</div>
		</div>
	</div>
</div>



<input type="hidden" id="useridQuery" value="${useridQuery }"/>
<input type="hidden" id="orderidQuery" value="${orderidQuery }"/>
</body>
</html>