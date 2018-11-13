<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
		<table border="1" style="width: 100%">
			<tr align="center">
				<td colspan="2">
					<input type="button" id="buttoniidd" value="出&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;货" onclick="FnCreateFpx();" style="width: 100%; height:50px; background: wheat; font-size: 24px;">
				</td>
			</tr>
			<tr>
				<td width="50%" style="background: #FFCC99; color: #000"><div>基本信息：</div></td>
				<td width="50%"><div id="remind" >原飞航单号：<font color="red">(原飞航出货必填)</font><input type="text" id="idyuanfeihangno" name="exportname" class="ff" /></div></td>
			</tr>
		
			<tr>
				<td><span>客户单号:</span>
					<span><input type="text" id="idorderno" name="exportname" value="${logistics.orderno}" class="ff" /></span><br />
				</td>
				<td><span>运输方式:</span>
					<span>
					<c:if test="${ logistics.transMethod !='YFH'}">
					
						<select id="idtransport" name="exportname" style="width: 60%">
						<c:forEach items="${logistics.productCodeBean}" var="ccb" varStatus="i">
						  <c:if test="${logistics.transMethod==ccb.productcode }">
							<option value="${ccb.productcode},${ccb.englishname}">${ccb.englishname}&nbsp;&nbsp;&nbsp;&nbsp;${ccb.chinesename}</option>	
							</c:if>
							</c:forEach>
						</select>
						</c:if>
						
						<c:if test="${ logistics.transMethod =='YFH'}">
							
						</c:if>
					</span><br />
					
					<input id='h_transMethod' type="hidden" value="${logistics.transMethod}"/>
				</td>
			</tr>
			<tr>
				<td><span>商品重量:</span>
					<span><input type="text" id="idweight" name="exportname" value="${logistics.weight}" style="width: 110px;height: 14px;" />kg</span>
				</td>
				<td><span>货物类型:</span>
					<span>
						<select id="idgoodstype" name="exportname" style="width: 60%">
							<option value="P" selected="selected">Package&nbsp;&nbsp;包裹</option>
							<option value="D">Document&nbsp;&nbsp;文件</option>
						</select>
						
						<select id="jcCargoType" style="width: 60%;display: none;">
					<option value="PAK" selected="selected">包裹</option>
					<option value="DOC" >文件</option>
					<option value="CTN" >纸箱</option>
					<option value="OTHERS">其他</option>
					
				</select>
					</span>
				</td>
			</tr>
			<tr>
				<td class="user"><div>收件人信息:</div></td>
				<td class="admin"><div>寄件人信息:</div></td>
			</tr>
			<tr>
				<td>
					<span class="admin">姓名:</span>
					<span><input type="text" id="idusername" name="exportname" value="${logistics.userName}" class="ff" /></span><br />
					<span>Email:</span>
					<span><input type="text" id="iduseremail" name="exportname" value="${logistics.email}" class="ff" /></span><br />
				</td>
				<td>
					<span class="admin">姓名:</span>
					<span><input type="text" id="idadminname" name="exportname" value="${logistics.adminname}" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td>
				<span class="admin">公司:</span>
				<span><input type="text" id="idusercompany" name="exportname" value="　" class="ff" /></span><br />
				</td>
				<td><span class="admin">公司:</span>
					<span><input type="text" id="idadmincompany" name="exportname" value="${logistics.admincompany}" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td>
					<span class="admin">国家:</span>
					<span>
						<select id="iduserzone" name="exportname" style="width: 60%">
							<c:forEach items="${logistics.countryCodeBean}" var="ccb" varStatus="i">
							<c:if test="${logistics.countryCode==ccb.countrycode }">
								<option value="${ccb.countrycode}">${ccb.englishname}&nbsp;&nbsp;&nbsp;&nbsp;${ccb.chinesename}</option>
								</c:if>
							</c:forEach>
						</select>
					</span><br />
				</td>
				<td><span class="admin">国家:</span>
					<span><input type="text" id="idadminzone" name="exportname" value="${logistics.adminzone}" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td>
					<span class="admin">邮编:</span>
					<span><input type="text" id="idusercode" name="exportname" value="${logistics.zipcode}" class="ff" /></span><br />
				</td>
				<td><span class="admin">邮编:</span>
					<span><input type="text" id="idadmincode" name="exportname" value="${logistics.admincode}" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td>
					<span class="admin">州/省:</span>
					<span><input type="text" id="iduserstate" name="exportname" value="${logistics.statename}" style="width: 100px;height: 14px;" /></span><br />
					<span class="admin"> 市:</span>
					<span><input type="text" id="idusercity" name="exportname" value="${logistics.address2}" class="ff" /></span><br />
					<span class="admin">街道:</span>
					<span><textarea id="iduserstreet" name="exportname" rows="1" cols="10" class="fff">${logistics.userstreet}${logistics.address}</textarea></span><br />
				</td>
				<td><span class="admin">州/省:</span>
					<span><input type="text" id="idadminsprovince" name="exportname" value="${logistics.adminprovince}" style="width: 100px;height: 14px;" /></span><br />
					<span class="admin"> 市:</span>
					<span><input type="text" id="idadmincity" name="exportname" value="${logistics.admincity}" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td>
					<span class="admin">地址:</span>
					<span><textarea id="iduseraddress" name="exportname" rows="2" cols="22" class="fff">${logistics.userstreet}${logistics.address}${logistics.address2}${logistics.statename}&nbsp;&nbsp;${logistics.zipcode}</textarea></span><br />
				</td>
				<td><span class="admin">地址:</span>
					<span><textarea id="idadminaddress" name="exportname" rows="2" cols="22" class="fff">${logistics.adminaddress}</textarea></span><br />
				</td>
			</tr>
			<tr>
				<td>
					<span class="admin">电话:</span>
					<span><input type="text" id="iduserphone" name="exportname" value="${logistics.phone}" class="ff" /></span><br />
				</td>
				<td><span class="admin">电话:</span>
					<span><input type="text" id="idadminphone" name="exportname" value="${logistics.adminphone}" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td>
					<span class="admin">买家ID:</span>
					<span><input type="text" id="iduserid" name="exportname" value="${logistics.userid}" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td style="background: #FFCC99; color: #000"><div>海关申报信息:</div></td>
				<td style="background: #FFCC99; color: #000"><a href="#oranges"><input type="button" value="添加申报" onclick="FnInnerTr();"></a></td>
			</tr>
			<tr>
				<td colspan="2">
					<table border="1" style="width: 100%" id="innerTr">
					<%-- <c:forEach items="${logistics.productBean}" var="pcb" varStatus="i"> 
						<tr>
							<td width="15%" style="background: #FFCC99; color: #000"><span>品名(中):</span></td>
							<td width="50%" colspan="2"><span><input type="text" id="" name="idproduct" style="width: 300px;height: 14px;" /></span><br /></td>
						</tr>
						<tr>
							<td ><span>品名(英):</span></td>
							<td ><span><textarea id="" name="idproduct" rows="2" cols="30" class="fff">${pcb.productname}</textarea></span><br /></td>
							<td ><span>配货备注:</span>
								<span><textarea id="" name="idproduct" rows="1" cols="20" class="fff"></textarea></span><br /></td>
						</tr>
						<tr>
							<td style="border-bottom-color:red;  border-bottom-width:3px;"><span>数量:</span></td>
							<td colspan="2" style="border-bottom-color:red;  border-bottom-width:3px;"><span><input type="text" id="idproductid" name="idproduct" value="${pcb.productnum}" onblur="FnGoodsTotal();" style="width: 80px;height: 14px;" /></span>
							<span>价格:</span>
							<span><input type="text" id="" name="idproduct" value="${pcb.productprice}" style="width: 80px;height: 14px;" /></span>
							<span>单位:<input type="text" id="" name="idproduct" value="${pcb.productcurreny}" style="width: 40px;height: 14px;" /></span><br /></td>
						</tr>
 						</c:forEach> --%>
					</table>
					<table border="1" style="width: 100%">
						<tr>
							<td style="background: #FFFFAA; color: #000">备注:</td>
							<td><div id="exportnameremark"> ${logistics.mark}</div><input type="hidden" id="idremark" name="exportname" value="${logistics.mark}"></td>
							<td><span><a name="oranges">商品统计:</a></span><span><input type="text" id="idGoodsSum" name="exportname" value="0"></span></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div> </div>