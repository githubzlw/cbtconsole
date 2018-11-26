<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=utf-8"
		 import="com.cbt.website.bean.SearchTaobaoInfo" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page import="com.cbt.util.Redis" %>
<%@page import="com.cbt.util.AppConfig"%>
<%@ page import="com.cbt.website.userAuth.bean.Admuser" %>
<%@ page import="com.cbt.util.SerializeUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<%--<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>--%>
	<%--<script type='text/javascript'--%>
			<%--src='http://cdn.staticfile.org/jquery/2.1.1/jquery.min.js'></script>--%>
	<script src='https://code.jquery.com/jquery-1.10.1.min.js'></script>
	<script type="text/javascript"
			src="http://cdn.staticfile.org/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>
	<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
	<script type="text/javascript" src="/cbtconsole/js/JsBarcode.all.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/newtrack.js"></script>
	<title>快递扫描页面</title>
	<style>
		.mod_pay6 {
			width: 400px;
			height:400px;
			position: fixed;
			top: 100px;
			right: 15%;
			margin-left:400px;
			z-index: 1011;
			padding: 5px;
			padding-bottom: 20px;
			z-index: 1011;
		}
		ol, ul {
			list-style: none;
		}
		.mod_pay3 {
			width: 720px;
			position: fixed;
			top: 250px;
			left: 15%;
			z-index: 1011;
			background: gray;
			padding: 5px;
			padding-bottom: 20px;
			z-index: 1011;
			border: 15px solid #33CCFF;
		}
		.imagetable tr td {
			border-top: 1px solid #ddd;
			border-left: 1px solid #ddd;
			border-bottom: 1px solid #ddd;
			text-align: center;
		}
		.show_x {
			position: absolute;
			top: 5px;
			right: 15px;
			text-decoration: none;
		}
		a {
			color: #3d3d3d;
			text-decoration: none;
		}
		.m-feedback .tb-side {
			position: fixed;
			_position: absolute;
			right: 3px;
			bottom: 50px;
			z-index: 100000;
		}
		.m-feedback .tb-side li a {
			position: relative;
			width:120px;
			height: 48px;
			text-align: center;
			border-top: 1px solid #ddd;
			border-left: 1px solid #ddd;
			border-right: 1px solid #ddd;
			display: block;
			background: #f5f5f5;
			line-height: 48px;
		}
		.m-feedback .tb-side li:last-child{ border-bottom: 1px solid #ddd;}
		.m-feedback .tb-side li a:hover{background:#f40;color: #fff;}
		.m-feedback .tb-side li>a:hover{color: #fff;}
		/*    .m-feedback .tb-side a span {
                position: absolute;
                display: block;
                width: 120px;
                height: 48px;
                right: 0;
                text-indent: -10000px;
                outline: 0;
                overflow: hidden;
            }*/
		#div_clothing,#ss_div,#dz_div{
			position: fixed;
			top: 50%;
			left: 50%;
			-webkit-transform: translate(-50%,-50%);
			-moz-transform: translate(-50%,-50%);
			-ms-transform: translate(-50%,-50%);
			-o-transform: translate(-50%,-50%);
			transform: translate(-50%,-50%);
		}
		#div_clothing table,#ss_div table,#dz_div table{background-color: pink;}
		#div_clothing input,#ss_div input,#dz_div input{background-color: #eee;}

	</style>
	<style type="text/css">
		*{
			margin:0;
			padding:0;
		}
		.statusbtn {
			width: 120px;
			height: 50px;
			float: left;
			outline: none;
		}

		.white_content {
			display: none;
			position: relative;
			top: 25%;
			left: 25%;
			width: 50%;
			padding: 6px 16px;
			border: 12px solid #D6E9F1;
			background-color: white;
			z-index: 1002;
			overflow: auto;
		}

		.black_overlay {
			display: none;
			position: absolute;
			top: 0%;
			left: 0%;
			width: 100%;
			height: 100%;
			background-color: #f5f5f5;
			z-index: 1001;
			-moz-opacity: 0.8;
			opacity: .80;
			filter: alpha(opacity = 80);
		}

		.close {
			float: right;
			clear: both;
			width: 100%;
			text-align: right;
			margin: 0 0 6px 0
		}

		.close a {
			color: #333;
			text-decoration: none;
			font-size: 14px;
			font-weight: 700
		}

		.strcarype{color: black;font-size: 16px;font-weight:bold;}

		.loading { position: fixed; top: 0px; left: 0px;
			width: 100%; height: 100%; color:#fff; z-index:9999;
			background: #000 url(/cbtconsole/img/yuanfeihang/loaderTwo.gif) no-repeat 50% 300px;
			opacity: 0.4;}
		* { font-size:12px; font-family:Verdana,宋体; }
		html, body { margin:0px; padding:0px;margin-left:5px;}
		.b { margin-top:5px; padding:0px; overflow:auto; }
		.line0 { line-height:20px; background-color:lightyellow; padding:0px 15px; }
		.line1 { line-height:18px; background-color:lightblue; padding:0px 10px; }
		.w { position:absolute; left:10px; top:150px; width:280px; height:284px; overflow:hidden; border:2px groove #281; cursor:default; -moz-user-select:none; }
		.t { line-height:20px; height:20px; width:280px; overflow:hidden; background-color:#27C; color:white; font-weight:bold; border-bottom:1px outset blue; text-align:center; }
		.winBody { height:236px; width:600px; overflow-x:hidden; overflow-y:auto; border-top:0px inset blue; padding:10px; text-indent:10px; background-color:white; }
		#paizhao{position: fixed;right:0;top:0;}

	</style>
	<%
		//取出当前登录用户
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser adm =(Admuser)SerializeUtil.JsonToObj(userJson, Admuser.class);
		int userid=adm.getId();
		String loginName = adm.getAdmName();
		String shipno=request.getParameter("shipno");
		String barcode=request.getParameter("barcode");
		String type=request.getParameter("type");
		String goodsid=request.getParameter("goodsid");
		String orderid=request.getParameter("orderid");
	%>
	<script type="text/javascript">
        var checked="0";
        var _barcode="";
        var flag="0";
        $(function(){
            loginName = '<%=loginName%>';
            shipno='<%=shipno%>';
            _barcode='<%=barcode%>';
            orderid='<%=orderid%>';
            goodsid='<%=goodsid%>';
            type='<%=type%>';
            if(shipno!=null && shipno!='undefind' && shipno!="0"){
                $("#search").val(shipno);
                if(type=="1"){
                    checked="0";
                    if(orderid!=null && orderid!='undefind' && orderid!="null"){
                        // $.post("/cbtconsole/WebsiteServlet?action=insertTbInfo&className=ExpressTrackServlet",
						$.post("/cbtconsole/WebsiteServlet?action=insertTbInfo&className=ExpressTrackServlet",
                            {
                                orderid : orderid,
                                goodid : goodsid
                            }, function(res) {
                                if(res=="0"){
                                    alert("插入订单失败,不能关联入库");
                                    return;
                                }else{
                                    $("#search").val(res);
                                }
                            });
                    }
                }else{
                    checked="1";
                }
                document.getElementById('jsp_name').innerHTML= "验货页面--不可做入库操作";
                document.getElementById('jsp_name').style.backgroundColor="chartreuse";
                search();
            }
        })
	</script>
	<script type="text/javascript">
        var state = 0;
        var id_qty="0";
        function queryRecord(odid){
            id_qty="0";
            var record=records.split(";");
            for(var i=0;i<record.length;i++){
                var id_id=record[i].split(",")[1];
                if(id_id==odid){
                    itemqty=record[i].split(",")[2];
                    id_qty=itemqty;
                }
            }
        }

        var tbinfo="";
        var code="无";
        var records="";
        function getTaoBaoInfo(orderid){
            $.ajaxSetup({
                async: false
            });
            $.post("/cbtconsole/WebsiteServlet?action=getTaoBaoInfo&className=ExpressTrackServlet",{orderid : orderid},
                function(res){
                    tbinfo =eval(res);
                    if(tbinfo.length>0){
                        code=tbinfo[0].imgurl;
                        records=tbinfo[0].records;
                        if(checked=="1"){
                            code=_barcode;
                        }else if(code=="无"){
                            code="请清空库位然后扫描入库";
                        }
                        $("#kwhid").val(code);
                        $("#h_b8").val(code);
                        $("#h_b7").val("1");
                    }
                }
            );
        }

        var objimgwidth = 0;
        var objimgheight = 0;
        function AutoResizeImage(objImg){
            if($(objImg).width() != objimgwidth*2){
                objimgwidth = $(objImg).width()
                objimgheight = $(objImg).height()
                $(objImg).width(objimgwidth*2);
                $(objImg).height(objimgheight*2);
            }else{
                $(objImg).width(objimgwidth);
                $(objImg).height(objimgheight);
            }
        }
	</script>
</head>
<body onload="focus();">


<div id="operatediv" class="loading" style="display: none;"></div>
<div class="m-feedback" id="divTip">
	<div class="tb-side">
		<ul>
			<li class="gotop" style="display: block;">
				<a  href="#scanning_frame">
					<span class="icon-btn-top">扫描框</span>
				</a>
			</li>

			<li class="experience">
				<a href="#taobao_info">
					<span class="icon-util-qince">淘宝信息</span>
				</a>
			</li>

			<li class="help">
				<a  target="" href="#operating_area">
					<span class="icon-btn-help">操作区</span>
				</a>
			</li>
			<li class="advice advise-side-suggest">
				<a  href="#barcode_info" target="" class="">
					<span class="icon-btn-feedback">库位信息</span>
				</a>
			</li>
		</ul>
	</div>
</div>
<div id="div_body">
	<div class="mod_pay6" style="display: none;" id="big_img">

	</div>
	<%  if(!(adm.getId() ==1 || adm.getAdmName().equalsIgnoreCase("Ling") || adm.getAdmName().equalsIgnoreCase("emmaxie"))){%>
	<!-- 载入消息提醒jsp页面 -->
	<jsp:include page="message_notification.jsp"></jsp:include>
	<%}  %>

	<div id="paizhao" style="display: ;">
		<video id="video" width="400" height="400" autoplay></video><br>
		<canvas id="canvas" width="400"  height="400"></canvas>
	</div>
	<br>
	<!-- 扫描框开始 -->
	<div id="scanning_frame">
		<span id="jsp_name" style="background-color:red;font-size:30px;">入库扫描页面--不可做验货操作</span><br><h3 style="color:bisque;padding:10px 20px;background:#f5f5f5;color:#f40">扫描框</h3><br>
		请输入快递跟踪号：
		<input type="text" id="search" onFocus="celsearch()"  onkeypress="if (event.keyCode == 13) search()" >

		库位条形码：
		<input type="text" id="kwhid" onFocus="celkwhid()" readonly="readonly" onkeypress="if (event.keyCode == 13) getPosition()"/>

		<a href="../website/purchase_order_details.jsp"  target="_Blank">未按时入库订单列表</a>
		<h2 id="ydid"  style="clear:both;"></h2>
		<h2 id="positionid"></h2>
		<input type="hidden" id="tborderid" value=""/>
		<input type="hidden" id="shipno" value=""/>
		<input type="hidden" id="user_id" value=""/>  <!-- 用户ID-->
		<input type="hidden" id="import_id" value=""/> <!-- import orderid -->
		<input type="hidden" id="h_b4" value=""/>  <!-- 订单id -->
		<input type="hidden" id="h_b5" value="<%=adm.getId() %>"/> <!-- 当前登录用户id -->
		<input type="hidden" id="h_b6" value="<%=adm.getAdmName()%>"/> <!-- 当前登录用户密码 -->
		<input type="hidden" id="h_b7" value="0"/>  <!-- 判断订单是否输入以及是否输入有误 -->
		<input type="hidden" id="h_b8" value="0"/><!-- 保存本次查询的库位位置 -->
		<input type="hidden" id="h_b9" value=""/><!-- 保存本次查询的库位位置 -->
		<input type="hidden" id="itemid" value=""> <!-- itemid -->
		<input type="hidden" id="base64_jpeg" value=""/>
		<input type="hidden" id="base64_bmp" value="">
		<input type="hidden" id="totalBuyCount" value=""/>
	</div>
	<!-- 淘宝信息结束 -->
	<div id="div" style= "width: 100%; float: left; left: 0px;"></div>
	<div id="div1" style= "width: 100%; float: left; left: 0px;"></div>
	<div id="replenish"></div>
	<div id="fade" class="black_overlay"></div>
	<div style="clear:both;"></div>

	<div id="amazon"></div>
	<div id="taobao_info">
		<input type="button" id="sureAllTrack" onclick="allTrack(1)" value="一键确认入库" style="display: none;height: 30px;width:80px;float:left;color:red;margin-right:20px"/>
		<input type="button" id="canceAllTrack" onclick="allTrack(0)" value="一键取消入库" style="display: none;height: 30px;width:80px;color:red;"/>
		<div id="insertMessage" style="font-size:30px;color:red;"></div>
		<div id="taobaoInfos"></div>
	</div>
	<div class="mod_pay3" style="display: none;" id="supplierDiv">
		<div>
			<a href="javascript:void(0)" class="show_x" onclick="FncloseSupplierDiv()">╳</a>
		</div>
		<%-- 		<center> --%>
		<h3 class="show_h3">仓库供应商打分</h3>
		<table id="supplierDivInfos" class="imagetable">
			<thead>
			<tr>
				<td rowspan="5"><span style="color:red">[质量]</span>:1:差   2:较差  3: 一般  4:无投诉  5: 优质</td>
			</tr>
			<tr>
				<td>店铺</td><td>质量</td>><td>是否有库存协议</td><td>支持退货天数</td>
			</tr>
			<tr>
				<td>
					<span id="su_shop_id"></span>
				</td>
				<td>
					<select id="quality">
						<option value="0">---请选择---</option>
						<option value="1">1分</option>
						<option value="2">2分</option>
						<option value="3">3分</option>
						<option value="4">4分</option>
						<option value="5">5分</option>
					</select>
				</td>
				<td>
					<input name="protocol" type="radio" value="2"/>有<input name="protocol" type="radio" value="1"/>无
				</td>
				<td>
					<input type="text" id="su_data" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
						   onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"/>
				</td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td>
					<input type="button" onclick="saveSupplier();" value="提交"/>
				</td>
				<td></td>
				<td></td>
			</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<%-- 		</center> --%>
	</div>
	<div class="mod_pay3" style="display: none;" id="supplierGoodsDiv">
		<div>
			<a href="javascript:void(0)" class="show_x" onclick="FncloseSupplierGoodsDiv()">╳</a>
		</div>
		<%-- 		<center> --%>
		<h3 class="show_h3">采样商品打分</h3>
		<table id="supplierGoodsDivInfos" class="imagetable">
			<thead>
			<tr>
				<td rowspan="5"><span style="color:red">[质量]</span>:1:差   2:较差  3: 一般  4:无投诉  5: 优质</td>
			</tr>
			<tr>
				<td>产品</td><td>质量</td><td>备注</td>
			</tr>
			<tr>
				<td>
					<span id="su_goods_id"></span><br>
					<span id="su_goods_p_id"></span>
				</td>
				<td>
					<select id="g_quality">
						<option value="0">---请选择---</option>
						<option value="1">1分</option>
						<option value="2">2分</option>
						<option value="3">3分</option>
						<option value="4">4分</option>
						<option value="5">5分</option>
					</select>
				</td>
				<td>
					<textarea rows="10" cols="15" id="su_g_remark"></textarea>
				</td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td>
					<input type="button" onclick="saveGoodsSupplier();" value="提交"/>
				</td>
				<td></td>
				<td></td>
			</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<%-- 		</center> --%>
	</div>
	<!-- 质检-->
	<div id="div_clothing" style="display: none;">
		<form id="div_clothing_from">
			<table border="1">
				<tr>
					<td colspan="5">
						策融电商仓库服装检查checklist V1.2
					</td>
				</tr>
				<tr>
					<td style="text-align:center">客户ID/订单号：</td>
					<td style="text-align:center" colspan="2"><span id="clothing_orderid"></span><input type="hidden" id="c_catid"></td>
					<td style="text-align:center">商品ID：</td>
					<td style="text-align:center"><span id="clothing_goodsid"></span></td>
				</tr>
				<tr>
					<td style="text-align:center">检验范围</td>
					<td style="text-align:center">序号</td>
					<td style="text-align:center">检验内容</td>
					<td style="text-align:center" colspan="2">检验结果</td>
				</tr>
				<tr>
					<td style="text-align:center" rowspan="5">外观</td>
					<td style="text-align:center">1</td>
					<td style="text-align:center">外观完整</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_wg" value="1">完整<input type="radio" name="y_wg" value="0">破损</td>
				</tr>
				<tr>
					<td style="text-align:center">2</td>
					<td style="text-align:center">表面细节</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_bmxj" value="1">完好<input type="radio" name="y_bmxj" value="0">图案错误<input type="radio" name="y_bmxj" value="2">不对称</td>
				</tr>			<tr>
				<td style="text-align:center">3</td>
				<td style="text-align:center">外观细节</td>
				<td style="text-align:center" colspan="2"><input type="radio" name="y_wgxj" value="1">干净<input type="radio" name="y_wgxj" value="0">有污渍</td>
			</tr>			<tr>
				<td style="text-align:center">4</td>
				<td style="text-align:center">拉链</td>
				<td style="text-align:center" colspan="2"><input type="radio" name="y_ll" value="1">顺畅<input type="radio" name="y_ll" value="0">卡顿</td>
			</tr>
				<tr>
					<td style="text-align:center">5</td>
					<td style="text-align:center">线头</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_xt" value="1">无线头<input type="radio" name="y_xt" value="0">少量线头<input type="radio" name="y_xt" value="2">过多线头</td>
				</tr>
				<tr>
					<td style="text-align:center">质感</td>
					<td style="text-align:center">6</td>
					<td style="text-align:center">材质与描述</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_zg" value="1">一致<input type="radio" name="y_zg" value="0">不确定<input type="radio" name="y_zg" value="2">有区别</td>
				</tr>
				<tr>
					<td style="text-align:center">*气味</td>
					<td style="text-align:center">7</td>
					<td style="text-align:center">衣服气味</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_qw" value="1">无异味<input type="radio" name="y_qw" value="0">有异味</td>
				</tr>
				<tr>
					<td style="text-align:center">包装</td>
					<td style="text-align:center">8</td>
					<td style="text-align:center">包装</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_bz" value="1">完好<input type="radio" name="y_bz" value="0">需更换</td>
				</tr>
				<tr>
					<td style="text-align:center">尺码表</td>
					<td style="text-align:center" colspan="2">要求尺寸</td>
					<td style="text-align:center" colspan="2">实测尺寸</td>
				</tr>
				<tr>
					<td style="text-align:center" rowspan="6">上衣/裙子</td>
					<td style="text-align:center">肩宽shoulder</td>
					<td style="text-align:center"><input type="text" id="jks_value"></td>
					<td style="text-align:center">肩宽</td>
					<td style="text-align:center"><input type="text" id="jk_remark"></td>
				</tr>
				<tr>
					<td style="text-align:center">胸围bust</td>
					<td style="text-align:center"><input type="text" id="xwb_value"></td>
					<td style="text-align:center">胸围</td>
					<td style="text-align:center"><input type="text" id="xw_remark"></td>
				</tr>
				<tr>
					<td style="text-align:center">腰围waist</td>
					<td style="text-align:center"><input type="text" id="yww_value"></td>
					<td style="text-align:center">腰围</td>
					<td style="text-align:center"><input type="text" id="yw_remark"></td>
				</tr>
				<tr>
					<td style="text-align:center">臀围hip</td>
					<td style="text-align:center"><input type="text" id="twh_value"></td>
					<td style="text-align:center">臀围</td>
					<td style="text-align:center"><input type="text" id="tw_remark"></td>
				</tr>
				<tr>
					<td style="text-align:center">袖长sleeve</td>
					<td style="text-align:center"><input type="text" id="xcs_value"></td>
					<td style="text-align:center">袖长</td>
					<td style="text-align:center"><input type="text" id="xc_remark"></td>
				</tr>
				<tr>
					<td style="text-align:center">衣长length</td>
					<td style="text-align:center"><input type="text" id="yzl_value"></td>
					<td style="text-align:center">衣长</td>
					<td style="text-align:center"><input type="text" id="yc_remark"></td>
				</tr>
				<tr>
					<td colspan="2"><input type="button" value="提交" onclick="saveClothingData();"></td>
					<td colspan="2"><input type="button" value="重置" onclick="resetClothingDiv('0');"></td>
					<td><input type="button" onclick="closeClothingDiv();" value="关闭"></td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 首饰检查checklist-->
	<div style="display: none;" id="ss_div">
		<form>
			<table border="1">
				<tr>
					<td colspan="4">
						策融电商仓库首饰检查checklist V1.2
					</td>
				</tr>
				<tr>
					<td style="text-align:center" colspan="2"><span>订单号：</span></td>
					<td style="text-align:center"><span id="ss_orderid"></span><input type="hidden" id="ss_catid"></td>
					<td style="text-align:center"><span>商品ID：</span><span id="ss_goodsid"></span></td>
				</tr>
				<tr>
					<td style="width:80px;text-align:center">检验范围</td>
					<td style="width:50px;text-align:center">序号</td>
					<td style="width:200px;text-align:center">检验内容</td>
					<td style="width:200px;text-align:center">检验结果</td>
				</tr>
				<tr>
					<td rowspan="7" style="text-align:center">
						<span>外观</span>
					</td>
					<td style="text-align:center">1</td>
					<td style="text-align:center">外观完整</td>
					<td style="text-align:center"><input type="radio" name="s_wz" value="1">完整<input type="radio" name="s_wz" value="0">破损<input type="radio" name="s_wz" value="2">掉钻</td>
				</tr>
				<tr>
					<td style="text-align:center">2</td>
					<td style="text-align:center">款式 </td>
					<td style="text-align:center"><input type="radio" name="s_ks" value="1">正确<input type="radio" name="s_ks" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">3</td>
					<td style="text-align:center">颜色</td>
					<td style="text-align:center"><input type="radio" name="s_ys" value="1">正确<input type="radio" name="s_ys" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">4</td>
					<td style="text-align:center">表面细节</td>
					<td style="text-align:center"><input type="radio" name="s_bm" value="1">完好<input type="radio" name="s_bm" value="0">松动<input type="radio" name="s_bm" value="2">电镀层脱落</td>
				</tr>
				<tr>
					<td style="text-align:center">5</td>
					<td style="text-align:center">外观细节</td>
					<td style="text-align:center"><input type="radio" name="s_wgxj" value="1">干净<input type="radio" name="s_wgxj" value="0">有锈斑</td>
				</tr>
				<tr>
					<td style="text-align:center">6</td>
					<td style="text-align:center">链扣</td>
					<td style="text-align:center"><input type="radio" name="s_lk" value="1">正常<input type="radio" name="s_lk" value="0">松动</td>
				</tr>
				<tr>
					<td style="text-align:center">7</td>
					<td style="text-align:center">掉色</td>
					<td style="text-align:center"><input type="radio" name="s_ds" value="1">无掉色<input type="radio" name="s_ds" value="0">有掉色</td>
				</tr>
				<tr>
					<td style="text-align:center">质感</td>
					<td style="text-align:center">8</td>
					<td style="text-align:center">材质与描述</td>
					<td style="text-align:center"><input type="radio" name="s_cz" value="1">一致<input type="radio" name="s_cz" value="0">有区别</td>
				</tr>
				<tr>
					<td style="text-align:center">包装</td>
					<td style="text-align:center">9</td>
					<td style="text-align:center">包装</td>
					<td style="text-align:center"><input type="radio" name="s_bz" value="1">有气泡膜<input type="radio" name="s_bz" value="0">需更换</td>
				</tr>
				<tr>
					<td style="text-align:center"><input type="button" value="重置" onclick="resetJewelryData('0');"></td>
					<td style="text-align:center" colspan="2"><input type="button" onclick="saveJewelryData();" value="提交"></td>
					<td style="text-align:center"><input type="button" onclick="closeClothingDivss();" value="关闭"></td>
				</tr>
			</table>
		</form>
	</div>
	<!--电子产品检查checklist -->
	<div id="dz_div" style="display: none;">
		<form>
			<table border="1">
				<tr>
					<td colspan="4" style="text-align:center">
						策融电商仓库电子产品检查checklist V1.2
					</td>
				</tr>
				<tr>
					<td style="text-align:center" colspan="2"><span>订单号：</span></td>
					<td style="text-align:center"><span id="dd_orderid"></span><input type="hidden" id="dd_catid"></td>
					<td style="text-align:center"><span>商品ID：</span><span id="dd_goodsid"></span></td>
				</tr>
				<tr>
					<td style="width:80px;text-align:center">检验范围</td>
					<td style="width:50px;text-align:center">序号</td>
					<td style="width:200px;text-align:center">检验内容</td>
					<td style="width:200px;text-align:center">检验结果</td>
				</tr>
				<tr>
					<td rowspan="7" style="text-align:center">外观</td>
					<td style="text-align:center">1</td>
					<td style="text-align:center">通电测试</td>
					<td style="text-align:center"><input type="radio" name="d_wg" value="1">正常<input type="radio" name="d_wg" value="0">无反应</td>
				</tr>
				<tr>
					<td style="text-align:center">2</td>
					<td style="text-align:center">款式</td>
					<td style="text-align:center"><input type="radio" name="d_ks" value="1">正确<input type="radio" name="d_ks" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">3</td>
					<td style="text-align:center">颜色</td>
					<td style="text-align:center"><input type="radio" name="d_ys" value="1">正确<input type="radio" name="d_ys" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">4</td>
					<td style="text-align:center">完整</td>
					<td style="text-align:center"><input type="radio" name="d_wz" value="1">完好<input type="radio" name="d_wz" value="0">套件有缺失<input type="radio" name="d_wz" value="2">无说明书</td>
				</tr>
				<tr>
					<td style="text-align:center">5</td>
					<td style="text-align:center">外观细节</td>
					<td style="text-align:center"><input type="radio" name="d_wg1" value="1">干净<input type="radio" name="d_wg1" value="0">生锈<input type="radio" name="d_wg1" value="2">起泡<input type="radio" name="d_wg1" value="3">褪色</td>
				</tr>
				<tr>
					<td style="text-align:center">6</td>
					<td style="text-align:center">外观细节2</td>
					<td style="text-align:center"><input type="radio" name="d_wg2" value="1">正常<input type="radio" name="d_wg2" value="0">缺角<input type="radio" name="d_wg2" value="2">碎屏</td>
				</tr>
				<tr>
					<td style="text-align:center">7</td>
					<td style="text-align:center">掉色</td>
					<td style="text-align:center"><input type="radio" name="d_ds" value="1">无掉色<input type="radio" name="d_ds" value="0">有掉色</td>
				</tr>
				<tr>
					<td style="text-align:center" rowspan="5">规格</td>
					<td style="text-align:center">8</td>
					<td style="text-align:center">内存</td>
					<td style="text-align:center"><input type="radio" name="d_nc" value="1">正确<input type="radio" name="d_nc" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">9</td>
					<td style="text-align:center">电池容量</td>
					<td style="text-align:center"><input type="radio" name="d_rl" value="1">正确<input type="radio" name="d_rl" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">10</td>
					<td style="text-align:center">屏幕大小</td>
					<td style="text-align:center"><input type="radio" name="d_pm" value="1">正确<input type="radio" name="d_pm" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">11</td>
					<td style="text-align:center">分辨率</td>
					<td style="text-align:center"><input type="radio" name="d_fbv" value="1">正确<input type="radio" name="d_fbv" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">12</td>
					<td style="text-align:center">像素</td>
					<td style="text-align:center"><input type="radio" name="d_xs" value="1">正确<input type="radio" name="d_xs" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center" rowspan="2">内部细节</td>
					<td style="text-align:center">13</td>
					<td style="text-align:center">插头</td>
					<td style="text-align:center"><input type="radio" name="d_ct" value="1">正确<input type="radio" name="d_ct" value="0">错误</td>
				</tr>

				<tr>
					<td style="text-align:center">14</td>
					<td style="text-align:center">说明书</td>
					<td style="text-align:center"><input type="radio" name="d_sms" value="1">有中文<input type="radio" name="d_sms" value="0">有英文<input type="radio" name="d_sms" value="2">无说明书</td>
				</tr>
				<tr>
					<td style="text-align:center">包装</td>
					<td style="text-align:center">15</td>
					<td style="text-align:center">包装</td>
					<td style="text-align:center"><input type="radio" name="d_bz" value="1">有气泡膜<input type="radio" name="d_bz" value="0">需更换</td>
				</tr>
				<tr>
					<td style="text-align:center"><input type="button" value="重置" onclick="resetElectronicData('0');"></td>
					<td style="text-align:center" colspan="2"><input type="button" value="提交" onclick="saveElectronicData();"></td>
					<td style="text-align:center"><input type="button" onclick="closeClothingDivdd();" value="关闭"></td>
				</tr>
			</table>
		</form>
	</div>
</div>
<div id="div_print1"  style="margin:0;padding:0;position: fixed;left:0;bottom:0;display: none;width: 290px;height:170px;" align="center">
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
<div id="div_print2" style="margin:0;padding:0;position: fixed;left:0;bottom:0;display: none;width: 283px;height:142px;" align="center">
	<table border="1" style="width: 283px;">
		<tr>
			<td colspan="2"><span style="font-size: 30px;">K</span><span id="barcode"></span></td>
			<td  style="text-align:center;vertical-align:middle;width: 20px;" rowspan="2" ><div id="qrcode3"></div></td>
		</tr>
		<tr>
			<td style="text-align:center;vertical-align:middle;">Item ID</td>
			<td style="text-align:center;vertical-align:middle;">Spec</td>
		</tr>
		<tr>
			<td rowspan="2"><span id="goodsid" style="font-weight: bold;"></span></td>
			<td><span id="goods_name"></span></td>
			<td style="text-align:center;"><span>Qty</span></td>
		</tr>
		<tr>
			<td><span id="sku3" style="text-align:center;"></span></td>
			<td style="text-align: center;"><span id="count2"></span></td>
		</tr>
		<tr>
			<td  style="text-align:center;vertical-align:middle;" colspan="4">Scanning date:<span id="td14"></span></td>
		</tr>
	</table>
	<span id="tborderid3" style="float:right;"></span>
</div>
</body>
</html>