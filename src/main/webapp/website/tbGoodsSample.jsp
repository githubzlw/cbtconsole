<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>样品添加</title>
</head> 
<link rel="stylesheet" href="/cbtconsole/css/warehousejs/tbGoodsSample.css" type="text/css">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/tbGoodsSample.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/table.css">
                     
          
<body style="background-color : #F4FFF4;">  
	<div align="center"><h1 align="center">样品添加</h1></div> 
   
	<div class="maindiv">     
		
		<!-- left -->     
		<div class="leftdiv" style="">           
			<input type="text" oninput="getTbGoodsSample(this.value)" value=""/>
			
			<table id="table_id">   
				<tr>  
					<td ></td>
				</tr>
				<tr>  
					<td></td>   
				</tr>
				<tr>
					<td></td>
				</tr>                                 
			</table>   
		</div>
		         
		<!-- rigth -->
		<div class="rigthdiv" >
			<input style="display: none;" type="button" onclick="addSample()" value="添加样品"/> 
			<!-- 样品详细类容 -->
			<div >  
				<!-- 样品详细 -->   
				<div id="d_sampleInfo" style="float:left;">
					<table >  
						<tr>  
							<td width="50px" colspan="2" align="center">样品详细
							<input type="hidden" id="h_sample_id"/>
							
							</td>
						</tr>      
						<tr style="background-color : #FFACB1;">  
							<td>样品类型名字</td>
							  
							<td>
							<select id="sel" onchange="getSubType()">
							<option>请选择</option>
							</select>
							<br/>
							<input type="hidden" id="category"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>样品标题</td>
							<td><input type="text" id="title"/></td>
						</tr>
						
						<tr style="background-color : #FFACB1;">
							<td>整体折扣</td>
							<td><input type="text" id="discount"/>(%)</td>
						</tr>

						
						
						<tr style="background-color : #FFACB1;">    
							<td>最低种类数量</td>
							<td><input type="text" id="minnum"/></td>
						</tr >
						<tr style="background-color : #FFACB1;">
							<td>初始数量</td>  
							<td><input type="text" id="defaultnum"/></td>
						</tr>
						<tr >  
							<td>cid</td>  
							<td><input type="text" id="cid"/></td>
						</tr>
						<tr >
						<td>亚马逊折扣</td>
						<td><input type="text" id="ymx_discount"/>(%)</td>
						</tr>
						<tr>  
							<td>样品图片</td>
							<td><input type="text" id="viewimg"/></td>
						</tr>
						<tr >
							<td>商品价格总和</td>
							<td><input type="text" id="discountprice"/></td>
						</tr>
						<tr>
							<td>备注</td>  
							<td><input type="text" id="remark"/></td>
						</tr>
						<tr>  
							<td colspan="2" align="center">
							<input onclick="updateTbGoodsSampleByid()" type="button" value="修改"/>
							<input onclick="insertTbGoodsSample()" type="button" value="添加"/>
							<input onclick="emptyAllText()" type="button" value="清空"/>
							<input style="background-color: #FFACB1;" onclick="deleteTbGoodsSample()" type="button" value="删除"/>
							</td>
							
						</tr>
					</table>    
				</div>          
			
				<!-- 商品详细 -->
				<div id="d_gsdInfo" >  
					<table  >                
						<tr>
							<td colspan="2" align="center">添加商品详细
							<input type="hidden" id="h_gsd_id"/>
							
							</td>
						</tr>  
						<tr >
						<td colspan="2">商品id或者商品链接<input id="idOrUrl" type="text" /><input type="button" value="读取" onclick="getGoodsDataById()" />
						</td>
							       
						</tr>  
						    
						<tr >
							<td>基本样品表id</td>  
							<td><input disabled="disabled" type="text" id="gsd_goodssampleid"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">   
							<td>产品id</td>  
							<td><input type="text" id="gsd_goodsid"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>产品名字</td>  
							<td><input type="text" id="gsd_goodsname"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>商品链接</td>  
							<td><input type="text" id="gsd_goodsurl"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>商品图片</td>  
							<td><input type="text" id="gsd_goodsimg"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>商品免邮价</td>
							<td><input onkeyup="JsPrice(this.value)" type="text" id="gsd_originalprice"/></td>
						</tr> 
						<tr style="background-color : #FFACB1;">
							<td>折扣价格</td>  
							<td><input type="text" id="gsd_goodsprice" width="8px"/> 
							</td>              
						</tr>    
					
						<tr style="background-color : #FFACB1;">
							<td>是否默认显示</td>
							<td>
							<select id="gsd_flag">
								<option value="1">显示</option>
								<option value="0">不显示</option>
							</select>
						</tr>
						<!-- 
						<tr style="background-color : #FFACB1;">              
							<td>cid</td>  
							<td><input type="text" id="gsd_cid"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>cidpath</td>
							<td><input type="text" id="gsd_cidpath"/></td>
						</tr >
						<tr>
							<td>category</td>
							<td><input type="text" id="gsd_category"/></td>
						</tr>  
						 -->
						
						<tr style="background-color : #FFACB1;">
							<td>商品规格</td>
							<td><input type="text" id="gsd_type"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>商品重量</td>
							<td><input type="text" id="gsd_weight"/></td>
						</tr> 
						
						
						<tr >    
							<td>亚马逊货号</td>  
							<td><input type="text" id="gsd_amazongoosid"/></td>
						</tr>
						<tr >
							<td>亚马逊价格</td>
							<td><input type="text" id="gsd_amazongoosprice"/></td>
						</tr>
						<tr>  
							<td colspan="2" align="center">
							<input onclick="updateTbGoodsSampleDetailsByid()" type="button" value="修改"/>
							<input onclick="insertTbGoodsSampleDetails()" type="button" value="添加"/>
							<input onclick="emptyDsdAllText()" type="button" value="清空"/></td>
						</tr>
					</table> 
				</div>     
	             <div>  
					<table  >  
						<tr>
							<td  align="center" colspan="2">批量导入</td>
						</tr>  
						<tr>
							<td  align="center">用户id 或者 订单号</td>
							<td  align="center"><input id="useridORorderid" type="text"/>
							<input type="button" value="导入" onclick="batchImportTbGSD()" />
							<p id="msg_p" style="color: red;"></p>
							</td>
						</tr>  
					</table> 
				</div>    
				<div>
						
				</div>
			</div>   
			<br/><br/>
			<!-- 样品对应所有商品 -->
			<div style="position:absolute; height:500px; width:1200px; overflow:auto">
				<table id="t_GoodsSampleDetails">  
					<tr>
						<td>
						
						</td>
						<td>
							商品图片<br/>
							商品规格<br/>  
							商品价格<br/>
						</td>
						<td>
							商品图片<br/>
							商品规格<br/>
							商品价格<br/>
						</td>
					</tr>
				</table>
			</div>  
			
		</div>
	</div>
</body>

<div id="t_sample_add" style="display: none;">
<table  >
	<tr>
		<td colspan="2" align="center">添加样品详细</td>
		
	</tr>
	<tr>
		<td>样品类型id</td>
		<td><input type="text" id="cid"/></td>
	</tr>
	<tr>
		<td>样品类型名字</td>  
		<td><input type="text" id="category"/></td>
	</tr>
	<tr>
		<td>样品标题</td>
		<td><input type="text" id="title"/></td>
	</tr>
	<tr>
		<td>样品图片</td>
		<td><input type="text" id="viewimg"/></td>
	</tr>
	<tr>
		<td>整体折扣(%)</td>
		<td><input type="text" id="discount"/></td>
	</tr>
	
	
	<tr>
		<td>商品价格总和</td>
		<td><input type="text" id="discountprice"/></td>
	</tr>
	<tr>
		<td>最低数量</td>
		<td><input type="text" id="minnum"/></td>
	</tr>
	<tr>
		<td>初始数量</td>
		<td><input type="text" id="defaultnum"/></td>
	</tr>
	<tr>
		<td>备注</td>  
		<td><input type="text" id="remark"/></td>
	</tr>
	<tr>
		<td colspan="2" align="center"><input type="button" value="开始添加"/></td>
	</tr>
</table>
</div>
</html>