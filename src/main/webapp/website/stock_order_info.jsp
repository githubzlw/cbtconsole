<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page import="com.cbt.util.Redis"%>
<%@page import="com.cbt.util.AppConfig"%>
<%@ page import="com.cbt.website.userAuth.bean.Admuser"%>
<%@ page import="com.cbt.util.SerializeUtil"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存处理</title>
<script type="text/javascript"
	src="/cbtconsole/js/jquery-1.10.2-website.js"></script>

<script type="text/javascript" src="/cbtconsole/js/jquery-1.8.0.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/warehousejs/thelibrary.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/warehousejs/jquery.messager.js"></script>
	<script type="text/javascript"
			src="http://cdn.staticfile.org/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
	<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
	<script type="text/javascript" src="/cbtconsole/js/JsBarcode.all.js"></script>


<link rel="stylesheet" href="/cbtconsole/css/warehousejs/measure.css"
	type="text/css">
<style type="text/css">
.select {
	width: 200px;
	height: 15px;
	color: #000;
	font-size: 12px;
	border: 1px solid #000;
}
.mod_pay3 { width: 600px; position: fixed;
	top: 100px; left: 15%;      
	z-index: 1011; background: gray;
	padding: 5px; padding-bottom: 20px;
	z-index: 1011; border: 15px solid #33CCFF; }
.loading {
	position: fixed;
	top: 0px;
	left: 0px;
	width: 100%;
	height: 100%;
	color: #fff;
	z-index: 9999;
	/*     background: #000 url(/cbtconsole/img/yuanfeihang/loaderTwo.gif) no-repeat 50% 300px; */
	opacity: 0.4;
	display: none;
}

ul#img_sf_1 {
	list-style: none;
	float: center;
	display: inline;
}

ul#img_sf_1 li {
	float: left;
	width: 150px;
	height: 60px;
	display: inline;
	margin: 2px 2px 2px 5px;
}

ul#img_sf_11 li a {
	width: 150px;
	height: 60px;
	display: block;
}

ul#img_sf_1 li a img {
	width: 100%;
	height: 100%;
	border: 1px #999 solid;
}

ul#img_sf_1 li a:hover {
	z-index: 100;
	margin: -32px 0px 0px -32px;
	position: absolute;
}

ul#img_sf_1 li a img :hover {
	width: 600px;
	height: 600px;
	border: 1px #999 solid;
}
</style>

<%
	String orderno=request.getParameter("orderno");
%>
<script type="text/javascript">
    var orderno='<%=orderno%>';
    if(orderno!=null && orderno!=""){
    	$("#orderno").val(orderno);
    	setTimeout(function(){
    		FnSearch(1);
			}, 1000)
    }
   
	function FnSearch(orderarrs) {
		var pagenum = "1";
		var orderid = (orderarrs - 1) * 50;
		var orderno = $.trim(document.getElementById("orderno").value);///订单编号//
		var goodid = $.trim(document.getElementById("goodid").value);///商品编号//
		var days = document.getElementById("searchdays").value;///最近几天//
		var admsuerid = document.getElementById("admsuerid").value;//采购人id
		var state = document.getElementById("state").value;//状态
		if (isNaN(goodid)) {
			alert("商品编号键入错误！");
		} else {
			var orderStyle = /^[A-Za-z0-9_]*$/;
			if (orderStyle.test(orderno) == false) {
				alert("订单号键入错误！");
			} else {
				var orderid_no_array = "";
				var goodsid = "";

				window.location = "/cbtconsole/PurchaseServlet?action=getStockOrderInfo&className=Purchase"
						+ "&pagenum="
						+ pagenum
						+ "&goodsid="
						+ goodsid
						+ "&orderid_no_array="
						+ orderid_no_array
						+ "&orderid="
						+ orderid
						+ "&orderno="
						+ orderno
						+ "&goodid="
						+ goodid
						+ "&days=" + days + "&admsuerid=" + admsuerid+ "&state=" + state;

			}
		}
	}

	function getTaoBaoInfo(order_no, goodsid) {
		$.ajaxSetup({
			async : false
		});
		$
				.post(
						"/cbtconsole/WebsiteServlet?action=getTaoBaoInfo&className=ExpressTrackServlet",
						{
							orderid : order_no
						}, function(res) {
							tbinfo = eval(res);
							if (tbinfo.length > 0) {
								code = tbinfo[0].imgurl;
								if (code == "无") {
									code = "无剩余空库位无法移库";
									$("#confirm_button_"+order_no+goodsid+"").attr("disabled", true);
									$("#update_button_"+order_no+goodsid+"").attr("disabled", true);
								}
								$("#" + order_no + goodsid).html(code);
							}
						});
	}

	function confirmMoveLibrary(orderid, goodsid,remaining,purchaseCount,goods_name,strcartype,usid) {
		var barcode=document.getElementById(""+orderid+goodsid+"").innerHTML;
		$.ajaxSetup({
			async : false
		});
		 if (!window.confirm('确认是否移库？')) {
	           return;
	        }
		$
				.post(
						"/cbtconsole/WebsiteServlet?action=confirmMoveLibrary&className=ExpressTrackServlet",
						{
							orderid : orderid,goodsid:goodsid,barcode:barcode,remaining:remaining,purchaseCount:purchaseCount
						}, function(res) {
							if(Number(res)>0){
								alert("商品移库成功");
								var count=purchaseCount;
								if(Number(remaining)<=Number(purchaseCount)){
                                    count=remaining;
								}
                                put_print(orderid, usid, goodsid, strcartype, count, "",
                                    "0000", "", "", "", goods_name, barcode, "");
								setTimeout(function(){
									FnSearch(1);
								}, 3000)
							}else{
								alert("商品移库失败");
							}
						});
	}

    /**
     * 入库标签打印
     * @param orderid
     * @param usid
     * @param goodid
     * @param strcartype
     * @param count
     * @param loginName
     * @param tbOrderId
     * @param count1
     * @param record_
     * @param unit
     * @param goods_name
     * @param barcode
     * @param goodurl
     */
    function put_print(orderid, usid, goodid, strcartype, count, loginName,
                       tbOrderId, count1, record_, unit, goods_name, barcode, goodurl) {
        var d = new Date();
        var str = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
        document.getElementById("div_body").style.display = "none";
        document.getElementById("div_print1").style.display = "";
        document.getElementById('td1').innerHTML = orderid;
        document.getElementById('td2').innerHTML = usid;
        document.getElementById('td3').innerHTML = goodid;
        $("#td5").html(strcartype);
        document.getElementById('td6').innerHTML = count;
        document.getElementById('td7').innerHTML = str;
        document.getElementById('td9').innerHTML = tbOrderId;
        var barcode = $('#barcode_img'),
            str = goodid,
            options = {
                format:"CODE128",
                displayValue:false,
                fontSize:10,
                height:20
            };
        JsBarcode(barcode, str, options);
        barcode.JsBarcode(str,options);
        setTimeout(
            function() {
                window.print();
                document.getElementById("div_body").style.display = "block";
                document.getElementById("div_print1").style.display = "none";
                document.getElementById('td1').innerHTML = "";
                document.getElementById('td2').innerHTML = "";
                document.getElementById('td3').innerHTML = "";
                $("#td5").html("");
                document.getElementById('td6').innerHTML = "";
                document.getElementById('td7').innerHTML = "";
                document.getElementById('td9').innerHTML = "";
            }, 1000)
	}
	
	 function update_inventory(allRemaining,od_id,barcode){
		    var rfddd = document.getElementById("insertOrderInfo");
			rfddd.style.display = "block";
			document.getElementById("all_remaining").value=allRemaining;
			document.getElementById("old_remaining").value=allRemaining;
			document.getElementById("od_id").value=od_id;
			document.getElementById("barcode").value=barcode;
	 }
	 
	 function FncloseInsert(){
		    var rfddd = document.getElementById("insertOrderInfo");
			rfddd.style.display = "none";
			document.getElementById("od_id").value="";
			document.getElementById("all_remaining").value="";
			document.getElementById("old_remaining").value="";
			document.getElementById("barcode").value="";
	 }
	 
	 function updateInventory(){
		 	var all_remaining = $.trim(document.getElementById("all_remaining").value);
		 	var old_remaining = $.trim(document.getElementById("old_remaining").value);
		 	var barcode = $.trim(document.getElementById("barcode").value);
			var remark = $.trim(document.getElementById("remark").value);
			var od_id = $.trim(document.getElementById("od_id").value);
		 	if(Number(all_remaining)<0){
		 		alert("库存不能为负数");
		 		return;
		 	}
		 	if(remark==null || remark==""){
		 		alert("请输入盘点有误原因");
		 		return;
		 	}
			$.ajaxSetup({
				async : false
			});
			$.post("/cbtconsole/WebsiteServlet?action=updateLockInventory&className=ExpressTrackServlet",{
				all_remaining : all_remaining,barcode:barcode,od_id:od_id,remark:remark,old_remaining:old_remaining
					}, function(res) {
						if(res>0){
							FncloseInsert();
							FnSearch(1);
							setTimeout(function(){
								alert("库存盘点成功已重新分配库存");
							}, 3000)
						}else{
							FncloseInsert();
							FnSearch(1);
							alert("库存盘点失败")
						}
								
				});
	 }
</script>
</head>
<body style="background-color: #F4FFF4;">
<div id="div_body">
	<a target="_blank" style="margin-left:1400px;" href="/cbtconsole/website/cancel_goods_inventory.jsp">商品已被取消，已使用库存待还原</a>
	<input type="hidden" id="info_id" value="" />
	<input type="hidden" id="user_id" value="" />
	<div align="center">
		<div>
			<h1>
				<span id="title">库存处理</span>
			</h1>
		</div>
		<div id="msginfo"></div>
		<br /> <br />
		<!-- 扫描 -->
		<div style="width: 1300px">
			商品分配时间:<select id="searchdays"
						   style="width: 100px; text-align: center;">
			<option value="" selected="selected">--最近天数--</option>
			<option value="3">最近三天</option>
			<option value="7">最近一周</option>
			<option value="14">最近两周</option>
			<option value="30">最近一个月</option>
		</select> 采购人:<select id="admsuerid"
							  style="width: 100px; text-align: center;">
			<option value="" selected="selected">--全部--</option>
			<option value="51">buy5</option>
			<option value="50">Alisa</option>
			<option value="9">camry</option>
			<option value="53">mindy</option>
			<option value="58">buy2</option>
		</select> 状态:<select id="state" style="width: 150px; text-align: center;">
			<option value="-1">全部</option>
			<option value="0">待确认</option>
			<option value="1">已确认</option>
		</select> 订单号:<input type="text" value="" id="orderno"
							 onkeypress="if (event.keyCode == 13) FnSearch('1');" /> 商品编号:<input
				type="text" id="goodid" class="h" value="${goodid}"
				onkeypress="if (event.keyCode == 13) FnSearch('1');" /> <input
				type="button" value="查询" onclick="FnSearch('1');" />
		</div>
		<div class="mod_pay3" style="display:none;" id="insertOrderInfo" >
			<input type="hidden" id="od_id"/>
			<center>
				<div class="w-group">
					<div class="w-div">
						当前库存数量:<input type="text" id="all_remaining" class="remark w-remark">
						<input type="hidden" id="old_remaining" class="remark w-remark">
					</div>
				</div>
				<div class="w-group">
					当   前 库    位：<input type="text" id="barcode" disabled="disabled" class="remark w-remark"/>
				</div>
				<div class="w-group">
					备注：<input type="text" id="remark" class="remark w-remark"/>
				</div>
				<input type="button" id="idAddResource" value="提交" onclick="updateInventory();"style="width: 90px; height: 40px; margin-top:20px;"/>
				<input type="button" value="取消" onclick="FncloseInsert();"style="width: 90px; height: 40px;"/>
			</center>
			<span style="color:red">tip:此操作只是盘点库存，若想使用库存，请返回【库存处理】页面，点击“确认并移库完成”按钮</span>
		</div>
		<!-- 表格 -->
		<c:if test="${not empty pblist}">
			<c:forEach items="${pblist}" var="pb" varStatus="pbsi">
				<div class="b">
					<table id="${pb.orderNo}" align="center" border="1px"
						   style="font-size: 13px;" width="100%" bordercolor="gray">
						<tr id="ht${pb.orderNo}">
							<td colspan="8">
								<div style="background-color: #E4F2FF;">
									<input type='hidden' name='pagenum' value='${pb.orderid}'>
									<span class="d">订单号：</span><span class="c" id="order_no">${pb.orderNo}</span>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <span class="d">交期：</span><span
										class="c">${pb.deliveryTime}</span>&nbsp;&nbsp; <span
										class="d">销售负责人：</span><span class="c">${pb.saler}</span>&nbsp;&nbsp;


								</div>
							</td>
						</tr>
						<tr id="ht${pb.orderNo}">
							<td width="25%" colspan="2">商品信息</td>
							<td width="15%">名称</td>
							<td width="15%">状态</td>
							<td width="15%">库存及订单信息</td>
							<td width="15%">操作 <br>
							<td width="15%">备注</td>
						</tr>
						<tr>
							<td width="13%" align="center"><c:set var="t_i" value="0"></c:set>
								<ul id="img_sf_1">
									<c:if test="${not empty pb.img_type}">
										<c:forEach items="${fn:split(pb.img_type,'@')}" var="img_type"
												   varStatus="i">
											<c:if test="${fn:indexOf(img_type,'http') > -1}">

												<c:set var="t_i" value="1"></c:set>
												<li><a id="example-${i.index }" target="_blank"
													   href="${pb.goods_url}"> <img height="150" width="150"
																					src="${img_type}" data-original="${img_type}">&nbsp;
												</a></li>
											</c:if>
										</c:forEach>
									</c:if>
									<c:if test="${t_i == 0}">
									<li><a href="${pb.goods_url}" target="_block"><img
											class="lazy" src="${pb.fileimgname}"
											data-original="${pb.fileimgname}" width="150px"
											height="150px"></a>
										</c:if>
										<c:if test="${t_i == 1}">
									<li><a href="${pb.goods_url}" target="_block"><img
											class="lazy" src="${pb.fileimgname}"
											data-original="${pb.fileimgname}" width="50px" height="50px" /></a>
										</c:if>
								</ul></td>
							<td width="12%"><span>商品号：</span><span>${pb.goodsid};</span><br>
								<input type="hidden" id="order_${pbsi.index+1}"
									   value="${pb.orderNo}" /> <input type="hidden"
																	   id="goodsid_${pbsi.index+1}" value="${pb.goodsid}" /> <span>编码：</span><span>${pb.goodsdata_id};</span><br>
								<input type="hidden" id="hdgd" value="${pb.goodsdata_id}">
								<div style="width: 100%; word-wrap: break-word;">
									<span>Type：</span><font class="dd">${pb.goods_type}</font>
								</div> <br /> <br /> <span id='${pb.goodsdata_id}${pbsi.index }_ylj'></span><br>

							</td>
							<!-- 名称 -->
							<td width="15%">
								<div
										style="width: 100%; word-wrap: break-word; word-break: break-all">${pb.goods_title }</div>

							</td>
							<td width="15%">
								<c:if test="${pb.flag==1}">
									已确认
								</c:if>
								<c:if test="${pb.flag==0}">
									待确认
								</c:if>
							</td>
							<td width="10%">库存数量：<span style="color:red;font-size:x-large;">${pb.allRemaining}</span><br/>
								库存位置：<span style="font-size:x-large;color:red;">${pb.barcode}</span><br> 采购：<span>${pb.admin}</span><br />
								客户备注：
							</td>
							<td width="5%">该订单使用库存：<span
									id="remaining_${pb.orderNo}${pb.goodsid}" style="color:green;font-size:x-large;">${pb.remaining}</span><br />
								移入库位：<span id="${pb.orderNo}${pb.goodsid}" style="font-size:x-large;color:red;"></span><br />
								<c:if test="${pb.flag==1}">
									<input type="button" disabled="disabled"  onclick="confirmMoveLibrary('${pb.orderNo}','${pb.goodsid}','${pb.remaining}','${pb.purchaseCount}','${pb.goods_title }','${pb.goods_type}','${pb.userid}');" value="确认并移库完成" />
									<input type="button" disabled="disabled" onclick="update_inventory('${pb.allRemaining}','${pb.od_id}','${pb.barcode}');" value="库存有误" />
								</c:if>
								<c:if test="${pb.flag==0}">
									<input type="button" id="confirm_button_${pb.orderNo}${pb.goodsid}"  onclick="confirmMoveLibrary('${pb.orderNo}','${pb.goodsid}','${pb.remaining}','${pb.purchaseCount}','${pb.goods_title }','${pb.goods_type}','${pb.userid}');" value="确认并移库完成" />
									<input type="button" id="update_button_${pb.orderNo}${pb.goodsid}" onclick="update_inventory('${pb.allRemaining}','${pb.od_id}','${pb.barcode}');" value="库存有误" />
								</c:if>
								<script
										type="text/javascript">
                                    getTaoBaoInfo('${pb.orderNo}',
                                        '${pb.goodsid}');
								</script>
							</td>
							<td width="10%">${pb.remark}</td>
						</tr>
					</table>
				</div>

			</c:forEach>
		</c:if>

		<script type="text/javascript">
            document.getElementById("state").value = ${state};
		</script>

		<div style="text-align: center;" id="pagediv">
			共查${state}到<span id="datacount">${allCount}</span>条数据&nbsp;&nbsp; <input
				type="button" id="prePage" onclick="FnSearch('${pagenum-1 }')"
				value="上一页" />&nbsp; 第<span id="nowPage">${pagenum}</span>页/共<span
				id="allPage">${pageSize}</span>页 <input type="button" id="nextPage"
														onclick="FnSearch('${pagenum+1 }')" value="下一页" />
		</div>
	</div>
	<div id="operatediv" class="loading"></div>
</div>
<div id="div_print1" style="margin:0;padding:0;position: fixed;left:0;bottom:0;display: none;width: 290px;height:170px;" align="center">
	<table  border="1">
		<tr>
			<td   style="text-align:center;vertical-align:middle;width: 20px;height: 15px;" colspan="4" rowspan="1" >
				<%--<div id="qrcode"></div>--%>
				<img id="barcode_img"/>
			</td>
		</tr>
		<tr>

			<td style="text-align:left;height: 15px;" colspan="4">Order#:<span id="td1"></span></td>
		</tr>
		<tr>
			<td style="text-align:left;height: 15px;" colspan="3"><span>User ID:</span><span id="td2" ></span></td>
		</tr>
		<tr >
			<td  style="text-align:center;">Item ID</td>
			<td  style="text-align:center;vertical-align:middle;">Spec</td>
			<td  style="text-align:center;vertical-align:middle;">Qty</td>
		</tr>
		<tr>
			<td  style="text-align:center;vertical-align:middle;"><span id="td3" style="font-weight: bold;"></span></td>
			<td  style="text-align:center;vertical-align:middle;"><div id="td5" style="word-wrap:break-word;width:200px;"></div></td>
			<td  style="text-align:center;vertical-align:middle;"><span id="td6"></span></td>
		</tr>
		<tr>
			<td  style="text-align:center;vertical-align:middle;" colspan="4">Scanning date:<span id="td7"></span></td>
		</tr>
	</table>
	<span id="td9" style="float:right;"></span>
</div>
</body>
<script type="text/javascript">
	$("#prePage").click(function() {
		var nowPage = $("#nowPage").html();
		if (parseInt(nowPage) <= 1) {
			alert("已到达首页");
			return false;
		} else {
			$("#nowPage").html(parseInt(nowPage) - 1)
			search(parseInt(nowPage) - 1);
		}
	});
	$("#nextPage").click(function() {
		var nowPage = $("#nowPage").html();
		var allPage = $("#allPage").html();
		if (parseInt(nowPage) == parseInt(allPage)) {
			alert("已到达尾页");
			return false;
		} else {
			$("#nowPage").html(parseInt(nowPage) + 1)
			search(parseInt(nowPage) + 1);
		}
	});
	$("#jumpPage").click(function() {
		var allPage = $("#allPage").html();
		var topage = $("#toPage").val();
		if (isNaN(topage)) {
			alert("请输入正确的页码");
			return false;
		} else if (parseInt(topage) <= 0 || parseInt(topage) > allPage) {
			alert("页码超出范围");
			return false;
		} else {
			$("#nowPage").html(parseInt(topage))
			search(parseInt(topage));
		}
	});
</script>
</html>