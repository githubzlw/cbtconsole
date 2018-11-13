<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ page import="com.cbt.bean.OrderDatailsNew" %>
<%@ page import="com.cbt.bean.OrderDatailsNew" %>
<%@ page import="java.util.*" %>

<div>
<div align="left"><span class="eee">订单详情：</span>
用户ID:${orderPayDetail.user_id}<input type="hidden" id="ucurrency" value="${orderPayDetail.ucurrency}">
合并单号(${orderPayDetail.orderarr})
<input type="hidden" value="${orderPayDetail.user_id}"id="orderPayDetailuser_id"/>
<!--
销售负责人:${orderPayDetail.admname}
账户余额:${orderPayDetail.available_m}
赠送运费余额:${orderPayDetail.applicable_credit}
重量(${orderPayDetail.ofweight})
体积(${orderPayDetail.ofvolume_lwh})
国家(${orderPayDetail.ofcountry_code})

区域：${orderPayDetail.oforder_area}
运输方式4px  原飞航 ____${orderPayDetail.oftrans_method}
  -->
</div>
	<table border="1" style="width: 100%;height: 60%" class="ee" id="innerTr">
		<tr>
			<td width="8%">用户ID:<input type="hidden" id="ucurrency" value="${orderPayDetail.ucurrency}"></td>
			<td width="10%"><input type="text" value="${orderPayDetail.user_id}" id="userid" class="ff" disabled="disabled"></td>
			<td width="8%">销售负责人:</td>
			<td width="10%"><input type="text" value="${orderPayDetail.admname}" id="salerid" class="ff" disabled="disabled"></td>
			<td width="8%">账户余额:</td>
			<td width="10%"><input type="text" value="${orderPayDetail.available_m}" class="ff" disabled="disabled"></td>
			<td width="8%">赠送运费余额:</td>
			<td width="20%" id="td_applicable_credit">
				<input type="text" id="applicable_credit" value="${orderPayDetail.applicable_credit}" class="ff" disabled="disabled">${orderPayDetail.ucurrency}
				<input type="hidden" id="id_applicable_credit" value="${orderPayDetail.applicable_credit}"></td>
			<td width="8%">采购负责人:</td>
			<td width="10%"><input type="text" value="${orderPayDetail.buyuser}" class="ff" disabled="disabled"></td>
		</tr>
		<tr>
			<td>订单号:</td>
			<td colspan="2"><input type="text" value="${orderPayDetail.order_no}" id="proOrderid" style="width: 180px;height: 15px;" readonly="readonly"></td>
			<td>支付时间:</td>
			<td colspan="2"><input type="text" value="${orderPayDetail.create_time}" style="width: 150px;height: 15px;" disabled="disabled"></td>
			<td>国内交期:</td>
			<td><input type="text" value="${orderPayDetail.delivery_time}" class="ff" disabled="disabled">天</td>
			<td>国外交期:</td>
			<td><input type="text" value="${orderPayDetail.delivery_time_abroad}" class="fff" disabled="disabled">天</td>
		</tr>
		<tr>
			<td height="20PX">收货地址:</td>
<%-- 			<td colspan="5"><textarea id="proCountry" rows="1" cols="50" disabled="disabled">${orderPayDetail.address_id}</textarea></td> --%>
			<td colspan="5">${orderPayDetail.address_id}</td>
			<td>运输方式:</td>
			<td colspan="3" id="proCountry" >${orderPayDetail.mode_transport}</td>
		</tr>
		<tr>
			<td>已支付金额:</td>
			<td><input type="text" value="${orderPayDetail.pay_price}${orderPayDetail.currency}" class="ff" disabled="disabled"></td>
			<td>产品金额:</td>
			<td><input type="text" value="${orderPayDetail.product_cost}${orderPayDetail.currency}" class="ff" disabled="disabled"></td>
			<td>已支付运费:</td>
			<td><input type="text" value="${orderPayDetail.pay_price_tow}${orderPayDetail.currency}" class="ff" disabled="disabled"></td>
			<td>已支服务费:</td>
			<td><input type="text" value="0.00" class="ff" disabled="disabled"></td>
			
			<!-- 不显示 -->
			<td>未支付费用:</td>
			<td><fmt:formatNumber type="number" value="${orderPayDetail.remaining_price}" pattern="0.00" maxFractionDigits="2"/>${orderPayDetail.currency}</td>
		</tr>
		<tr>
			<td>预估重量:</td>
			<td><input type="text" value="${orderPayDetail.actual_weight_estimate}" class="fff" disabled="disabled">Kg</td>
			<td>预估体积M<sup>3</sup>:</td>
			<td>

				<input type="text" value="${orderPayDetail.actual_lwh}" class="ff" disabled="disabled">

			
			</td>
			<td>预估运费:</td>
			<td><input type="text" value="${orderPayDetail.actual_ffreight}" class="ff" disabled="disabled"></td>
			<td bgcolor="66ffff">额外包装费:</td>
			<td bgcolor="66ffff"><input type="text" id="inRmb3" class="fff" onkeyup="FnFeeCountTwo(this.value,'${orderPayDetail.currency}');">RMB
				<input type="text" value="0.00" class="fff" id="packageFee" onkeyup="getTotalcost(this.value)" >${orderPayDetail.currency}</td> <!-- readonly="readonly" -->
			<td colspan="2"></td>
		</tr>
		<tr>
			<td bgcolor="66ffff">产品重量:</td>
			<td id="tdpro_weight" bgcolor="66ffff">
			
			
			<c:if test="${orderPayDetail.ofweight !='1.0'}">
				<input type="text" value="${orderPayDetail.ofweight}" class="fff" id="proWeight" >
			</c:if>
			<c:if test="${orderPayDetail.ofweight =='1.0'}">
				<input type="text" value="${orderPayDetail.actual_weight}" class="fff" id="proWeight" >
			</c:if>
			
			Kg</td>
			<td bgcolor="66ffff">产品体积CM<sup>3</sup>:</td>
			
			<td id="tdpro_colume" bgcolor="66ffff">
			<c:if test="${orderPayDetail.ofvolume_lwh != '0*0*0'}">
				<input type="text" value="${orderPayDetail.ofvolume_lwh}" class="ff" id="proVolume" onblur="proVolume(1);" onfocus="proVolume(2);">
			</c:if>
			<c:if test="${orderPayDetail.ofvolume_lwh == '0*0*0'}">
				<input type="text" value="0*0*0" class="ff" id="proVolume" onblur="proVolume(1);" onfocus="proVolume(2);">
			</c:if>
			
			</td>
			<td>实际运费:</td>
			<td><input type="text" class="fff" id="feeCountRes2" value="${orderPayDetail.ofacture_fee}" disabled="disabled"></td>
			<td bgcolor="66ffff">实收运费:</td>
			<td bgcolor="66ffff"><input type="text" class="ff" id="feeCountRes3" value="${orderPayDetail.ofacture_fee}" disabled="disabled">
				<select style="width: 90px;" id="countrate" onchange="fncountrate(this.value,'${orderPayDetail.currency}');">
					<option value="3.0">3.0</option>
					<option value="2.9">2.9</option>
					<option value="2.8">2.8</option>
					<option value="2.7">2.7</option>
					<option value="2.6">2.6</option>
					<option value="2.5">2.5</option>
					<option value="2.4">2.4</option>
					<option value="2.3">2.3</option>
					<option value="2.2">2.2</option>
					<option value="2.1">2.1</option>
					<option value="2.0">2.0</option>
					<option value="1.9">1.9</option>
					<option value="1.8">1.8</option>
					<option value="1.7">1.7</option>
					<option value="1.6">1.6</option>
					<option value="1.5" selected="selected">1.5</option>
					<option value="1.4">1.4</option>
					<option value="1.2">1.2</option>
					<option value="1.0">1.0</option>
					<option value="0.0">免邮</option>
					<option value="paid">已付运费</option>
				</select>
			</td>
			<td colspan="2" bgcolor="wheat">实收运费>=实际运费和预估运费</td>
		</tr>
		<tr>
			<td colspan="10" class="eee">预出库信息:</td>
		</tr>
		<tr>
			 <!-- 不显示 -->
			 
			<td rowspan="1" colspan="6">
				<input type="hidden" value="${orderPayDetail.pay_price}${orderPayDetail.currency}" class="ff" disabled="disabled">
				<input type="hidden" value="0.00" class="ff" disabled="disabled">
				<!-- ${orderPayDetail.currency} -->
				<input type="hidden" value="${orderPayDetail.pay_price_tow}" class="fff" id="feeCountRes4" disabled="disabled">
				
				<!-- 快递跟踪号： <input  id="express_no" name="express_no" type="hidden" > -->
			</td>
	
			
			
			<td>运输国家:</td>
			<td id="tdzone">
			<!-- onchange="FnFeeCount(1,'${orderPayDetail.currency}');" -->
				<select id="idzone" style="width: 90%" onchange="FnFeeCount(4,'${orderPayDetail.currency}')">
					<c:if test="${empty ccb}">
							<option value="nul" selected="selected">------订-单-国-家------</option>
						<c:forEach items="${ccblist}" var="ccb" varStatus="i">
						<c:if test="${ccb.countrycode == orderPayDetail.ofcountry_code}">
							<option  selected="selected" value="${ccb.countrycode}">${ccb.englishname}&nbsp;&nbsp;&nbsp;&nbsp;${ccb.chinesename}</option>
						</c:if>
						<c:if test="${ccb.countrycode != orderPayDetail.ofcountry_code}">
							<option value="${ccb.countrycode}">${ccb.englishname}&nbsp;&nbsp;&nbsp;&nbsp;${ccb.chinesename}</option>
						</c:if>
						</c:forEach>
					</c:if>
					<c:if  test="${not empty ccb}">
							<option value="${ccb}">${ccb1}</option>
					</c:if>
				</select>
			</td>
			
			
			<td>区域：</td>
			<td id="td_area">
				<select id="idarea" style="width: 90%" onchange="FnFeeCount(2,'${orderPayDetail.currency}');">
					<c:if test="${empty yfharea}">
							<option value="nul" selected="selected">--订-单-区-域--</option>
						<c:forEach items="${arealist}" var="yfh" varStatus="i">
						<c:if test="${orderPayDetail.oforder_area == yfh.yfharea_name}">
								<option selected="selected" value="${yfh.yfharea_name}">${yfh.yfharea_name}</option>
						</c:if>
						<c:if test="${orderPayDetail.oforder_area != yfh.yfharea_name}">
								<option value="${yfh.yfharea_name}">${yfh.yfharea_name}</option>
						</c:if>
						</c:forEach>
					</c:if>
					<c:if test="${not empty yfharea}">
							<option value="${yfharea}">${yfharea}</option>
					</c:if>
				</select>
			</td>
		</tr>
		<tr>
		
			<!-- 不显示  ${orderPayDetail.currency} -->
			<td colspan="6">
				<input type="hidden" value="${orderPayDetail.product_cost}${orderPayDetail.currency}" class="ff" disabled="disabled">
				<input type="hidden" value="0.00" class="ff" disabled="disabled">
				<input value="${orderPayDetail.ofacture_get_fee=='' ? 0:orderPayDetail.ofacture_get_fee }" type="hidden" class="fff" id="feeCountRes5" disabled="disabled">
				<input type="hidden" id="id_feeCountRes5" value="${orderPayDetail.ofacture_get_fee=='' ? 0:orderPayDetail.ofacture_get_fee }">
			
				物流公司名称：<input id="logistics_name" name="logistics_name" type="text" disabled="disabled">
				<select id="logistics" style="width:150px" onchange="getCodeId(this.value)">
						<option selected="selected">--选择--</option>
		             <c:forEach var="logisticsList" items="${logisticsList}" >
		               	<option value="${logisticsList.codeId}">${logisticsList.codeName}</option>
		             </c:forEach>
		          </select>
			</td>
			
				
			
			
			<td>运输方式:</td>
			<td id="tdtrans">
				<select id="idtrans" style="width: 90%" onchange="FnFeeCount(1,'${orderPayDetail.currency}');">
					<option value="nul" selected="selected">------运-输-方-式------</option>
					<c:forEach items="${pcblist}" var="pcb" varStatus="i">
					<c:if test="${orderPayDetail.oftrans_details == pcb.productcode}">
						<option selected="selected"  value="${pcb.productcode}">${pcb.englishname}&nbsp;&nbsp;&nbsp;&nbsp;${pcb.chinesename}</option>
					</c:if>
					<c:if test="${orderPayDetail.oftrans_details != pcb.productcode}">
						<option value="${pcb.productcode}">${pcb.englishname}&nbsp;&nbsp;&nbsp;&nbsp;${pcb.chinesename}</option>
					</c:if>
					</c:forEach>
				</select>
			</td>
			<td >佳成运输方式:</td>
			<td id="tdjcysfs">
			<select id="idjcysfs" style="width: 90%"  onchange="FnFeeCount(3,'${orderPayDetail.currency}')">
					<option value="nul" selected="selected">------运-输-方-式------</option>
					
				</select>
			</td>
		</tr>
		<tr>
			
			<td colspan="2" id="td_trans_fee">
			<input type="hidden" id="transstyle">
			<input type="checkbox" id="id_trans_fee" disabled="disabled" onclick="FnTransFee('${orderPayDetail.applicable_credit}',1);">
			运费余额抵扣<input type="hidden" id="id_trans_state" value="1"><input type="hidden" id="id_trans_deduction" value="0"></td>
			<td colspan="2"><input type="checkbox" disabled="disabled">余额抵扣/运费余额抵扣</td>
			
			<td colspan="2">
				<c:if test="${orderPayDetail.oftrans_method == '4PX'}">
				  <input type="radio" checked="checked" value="1" name="feestyle" id="feestyle1" onclick="FnFeeCount(1,'${orderPayDetail.currency}');">4PX计费
				</c:if>
				<c:if test="${orderPayDetail.oftrans_method != '4PX'}">
				  <input type="radio" value="1" name="feestyle" id="feestyle1" onclick="FnFeeCount(1,'${orderPayDetail.currency}');">4PX计费
				</c:if>
				
				<c:if test="${orderPayDetail.oftrans_method == 'YFH'}">
				  <input type="radio" checked="checked" value="2" name="feestyle" id="feestyle2" onclick="FnFeeCount(2),'${orderPayDetail.currency}';">原飞航计费
				</c:if>
				<c:if test="${orderPayDetail.oftrans_method != 'YFH'}">
				  <input type="radio" value="2" name="feestyle" id="feestyle2" onclick="FnFeeCount(2),'${orderPayDetail.currency}';">原飞航计费
				</c:if>
				
				<c:if test="${orderPayDetail.oftrans_method == 'JCEX'}">
				  <input type="radio" checked="checked" value="3" name="feestyle" id="feestyle3" onclick="FnFeeCount(3,'${orderPayDetail.currency}');"/>佳成计费
				</c:if>
				<c:if test="${orderPayDetail.oftrans_method != 'JCEX'}">
				  <input type="radio" value="3" name="feestyle" id="feestyle3" onclick="FnFeeCount(3,'${orderPayDetail.currency}');"/>佳成计费
				</c:if>
				
				
				
				
				<c:if test="${orderPayDetail.oftrans_method == 'other'}">
				  <input type="radio" checked="checked" value="5" name="feestyle" id="feestyle4" >其他出货方式
				</c:if>
				<c:if test="${orderPayDetail.oftrans_method != 'other'}">
				  <input type="radio" value="5" name="feestyle" id="feestyle4" >其他出货方式
				</c:if>
				
				</td>
			<td>货物类型:</td>
			<td >
			<div id="divselect4px">
			<select id="jcCargoType4PX" style="width: 90%" onchange="FnFeeCount(4,'${orderPayDetail.currency}')" >
					<option value="P" selected="selected">包裹</option>
					<option value="D" >文件</option>					
				</select>
			
			</div >
			<div id="divselectjc" style="display: none;">
			<select id="jcCargoType" style="width: 90%" onchange="FnFeeCount(4,'${orderPayDetail.currency}')" >
					<option value="PAK" selected="selected">包裹</option>
					<option value="DOC" >文件</option>
					<option value="CTN" >纸箱</option>
					<option value="OTHERS">其他</option>
					
				</select>
			</div>
			
			
			</td>
			<td colspan="2" bgcolor="wheat">运费计算:
			<input type="text" value="${orderPayDetail.ofacture_fee==null? 0:orderPayDetail.ofacture_fee}"  class="fff" id="feeCountRes1" disabled="disabled">
			<input type="text" value="${orderPayDetail.ofdelivery_time}" class="fff" id="feeCountDay" disabled="disabled"></td>
		</tr>
		<tr>
			<td colspan="6">
				快递单号：<input type="text" id="yfhdhid" value="${orderPayDetail.ofyfhNum}">
			</td>
			<td>未支付总费用:</td>
			<td><input type="text" class="ff" value="${orderPayDetail.ofunpay}" id="feeCountRes6" style="width: 70%;height: 95%;color: red;font-size: 24px; border-color: green;" readonly="readonly">
			<span style="font-size: 20PX;">${orderPayDetail.currency}</span></td>
			<!-- style="display: none" -->
			<td colspan="2"  align="center"><input   type="button" value="开始出库" onclick="FnSaveOrderFeeInfo();">
				<input type="button" value="添加申报" onclick="FnInnerTr()"/>
				
				<input type="hidden" value="出货" onclick="FnCreateFpx()"/>
			</td>
			
		</tr>
	</table>
<input type="hidden"  id="hgoodssum" value="${fn:length(odnList)}"/>  <!-- 商品个数 -->
<!-- 订单商品信息 -->
<div>
	<table border="1" style="width: 100%;height: 60%" class="ee">
		<tr>
			<td>goodsid</td>
			<td>orderid</td>
			<td>goods_title</td>
		</tr>
		<c:forEach items="${odnList}" var="od">
			<tr>
			<td>${od.goodsid }</td>
			<td>${od.orderid }</td>
			<td>${od.goods_title}</td>
		</tr>
		</c:forEach>
		
	</table>
</div>
</div>
<div><br></div>

<div align="right">
	<input type="button" id="idNextOrder" value="下一单" onclick="FnOutOrderTwo();">
	<input type="hidden" id="nextOrder">
	<input type="hidden" id="nextOrderId">
</div>
