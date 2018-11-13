<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>活动商品添加</title>
</head>
<link rel="stylesheet" href="/cbtconsole/css/warehousejs/tbGoodsSample.css"
	type="text/css">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/warehousejs/EventGoods.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/table.css">


<body style="background-color: #FFFFFF;">
	<div align="center">
		<h1 align="center">活动商品添加</h1>
	</div>

	<div class="maindiv">

		<!-- left -->
		<div class="leftdiv" style="">
			<input type="text" oninput="getTbGoodsSample(this.value)"
				value="商品名称" />

			<table id="table_id">
				<tr>
					<td></td>
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
		<div class="rigthdiv">
			<input style="display: none;" type="button" onclick="addSample()"
				value="添加" />
			<!-- 样品详细类容 -->
			<div>
				<!-- 样品详细 -->
				<div id="d_sampleInfo" style="float: left;">
					<table>
						<tr>
							<td width="50px" colspan="2" align="center">商品类目 <input
								type="hidden" id="h_sample_id" />

							</td>
						</tr>
						<tr style="background-color: #f0ffff;">
							<td>类型名称</td>
							<td><select id="sel" onchange="getSubType()">
									<option>请选择</option>
							</select> <br /> <input type="hidden" id="category" /></td>
						</tr>
						<tr style="background-color: #f0ffff;">
							<td>显示名称</td>
							<td><input type="text" id="title" /></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input
								onclick="updateTbGoodsSampleByid()" type="button" value="修改" />
								<input onclick="insertTbGoodsSample()" type="button" value="添加" />
								<input onclick="deleteTbGoodsSample()" type="button" value="删除" />
								
							</td>

						</tr>
					</table>
				</div>

				<!-- 商品详细 -->
				<div id="d_gsdInfo">
					<table>
						<tr>
							<td colspan="2" align="center">添加商品 <input type="hidden"
								id="h_gsd_id" />

							</td>
						</tr>
						<tr>
						<tr style="background-color: #f0ffff;">
							<td colspan="2">商品链接<input id="idOrUrl" type="text" /><input
								type="button" value="读取" onclick="getGoodsDataById()" /> <br />
								<input type="hidden" id="category" /></td>
						</tr>

						</td>
						<tr style="background-color: #f0ffff;">
							<td>产品id</td>
							<td><input type="text" id="gsd_goodsid" /></td>
						</tr>
						<tr style="background-color: #f0ffff;">
							<td>产品名字</td>
							<td><input type="text" id="gsd_goodsname" /></td>
						</tr>

						</tr>
						<tr style="background-color: #f0ffff;">
							<td>产品链接</td>
							<td><input type="text" id="gsd_goodsurl" /></td>
						</tr>
						<tr style="background-color: #f0ffff;">
							<td>商品图片</td>
							<td><input type="text" id="gsd_goodsimg" /></td>
						</tr>
						<tr style="background-color: #f0ffff;">
							<td>商品工厂价</td>
							<td><input type="text"
								id="gsd_originalprice" /></td>
						</tr>
						<tr style="background-color: #f0ffff;">
							<td>折扣价格</td>
							<td><input type="text" id="gsd_goodsprice" width="8px"  /></td>
						</tr>
						<tr style="background-color: #FFCCCC;">
							<td>折扣</td>
							<td><input type="text" id="discount" width="8px" onblur="JsDisount(this.value)"
							/></td>
						</tr>
						<tr style="background-color: #f0ffff;">
							<td>库存数量</td>
							<td><input type="text"
								id="avilibleStock" /></td>
						</tr>
						<tr style="background-color: #f0ffff;">
							<td>已售出数量</td>
							<td><input type="text"
								id="sold" /></td>
						</tr>

						<tr style="background-color: #f0ffff;">
							<td>是否默认显示</td>
							<td><select id="gsd_flag">
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

						<tr style="background-color: #f0ffff;">
							<td>商品规格</td>
							<td><input type="text" id="gsd_type" /></td>
						</tr>
						<tr style="background-color: #f0ffff;">
							<td>商品重量</td>
							<td><input type="text" id="gsd_weight" /></td>
						</tr>
						<tr>
							<td colspan="2" align="center"><input
								onclick="updateTbGoodsSampleDetailsByid()" type="button"
								value="修改" /> <input onclick="insertTbGoodsSampleDetails()"
								type="button" value="添加" /> 
								<input onclick="emptyDsdAllText()" type="button" value="清空"/>
								</td>
						</tr>
					</table>
				</div>
				<div></div>
			</div>
			<br />
			<br />
			<!-- 样品对应所有商品 -->
			<div
				style="position: absolute; height: 500px; width: 1200px; overflow: auto">
				<table id="t_GoodsSampleDetails">
					<tr>
						<td></td>
						<td>商品图片<br /> 商品规格<br /> 商品价格<br /> 折扣<br />
						</td>
						<td>商品图片<br /> 商品规格<br /> 商品价格<br />                                                                                                                                                                                                                                                                              折扣<br />
						</td>
					</tr>
				</table>
			</div>

		</div>
	</div>
</body>

<div id="t_sample_add" style="display: none;">
	<table>
		<tr>
			<td colspan="2" align="center">添加样品详细</td>

		</tr>
		<tr>
			<td>样品类型id</td>
			<td><input type="text" id="cid" /></td>
		</tr>
		<tr>
			<td>样品类型名字</td>
			<td><input type="text" id="category" /></td>
		</tr>
		<tr>
			<td>样品标题</td>
			<td><input type="text" id="title" /></td>
		</tr>
		<tr>
			<td>样品图片</td>
			<td><input type="text" id="viewimg" /></td>
		</tr>
		<tr>
			<td>整体折扣(%)</td>
			<td><input type="text" id="discount" /></td>
		</tr>


		<tr>
			<td>商品价格总和</td>
			<td><input type="text" id="discountprice" /></td>
		</tr>
		<tr>
			<td>最低数量</td>
			<td><input type="text" id="minnum" /></td>
		</tr>
		<tr>
			<td>初始数量</td>
			<td><input type="text" id="defaultnum" /></td>
		</tr>
		<tr>
			<td>备注</td>
			<td><input type="text" id="remark" /></td>
		</tr>
		<tr>
			<td colspan="2" align="center"><input type="button" value="开始添加" /></td>
		</tr>
	</table>
</div>
</html>