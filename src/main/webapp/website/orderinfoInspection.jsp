<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="com.cbt.util.Redis"%>
<%@page import="com.cbt.util.SerializeUtil"%>
<%@page import="com.cbt.website.userAuth.bean.Admuser"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>商品出库审核</title>

	<%
		String sessionId = request.getSession().getId();
		String userJson = Redis.hget(sessionId, "admuser");
		Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
	%>
	<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery-1.9.1.min.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.min.js"></script>

	<link id="skin" rel="stylesheet" href="/cbtconsole/js/warehousejs/jBox/Skins2/Green/jbox.css" />
	<script type="text/javascript" src="/cbtconsole/js/warehousejs/jBox/jquery.jBox-2.3.min.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/warehousejs/jBox/i18n/jquery.jBox-zh-CN.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/warehousejs/thelibrary.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/ajaxfileupload.js"></script>




	<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.messager.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/JsBarcode.all.js"></script>

	<%

		String ip = request.getLocalAddr();
		ip="http://"+ip;
	%>
	<!-- CSS goes in the document HEAD or added to your external stylesheet -->
	<style type="text/css">
		/* 表格样式 */
		table.altrowstable {
			font-family: verdana,arial,sans-serif;
			font-size:11px;
			color:#333333;
			border-width: 1px;
			border-color: #a9c6c9;
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
		}
		.oddrowcolor{
			background-color:#F4FFF4;
		}
		.evenrowcolor{
			background-color:#F4FFF4;
		}




		/* 字体样式 */
		body{font-family: arial,"Hiragino Sans GB","Microsoft Yahei",sans-serif;}
		p.thicker {font-weight: 900}

		/* button样式 */
		.className{
			line-height:30px;
			height:30px;
			width:88px;
			color:#ffffff;
			background-color:#7fa7e8;
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
			background-color:#4a8cf7;
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
			background-color : #E0F8FF;
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
			background-color:#7fa7e8;
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
			background-color:#4a8cf7;
		}

		/* 审核按钮 */
		.classSh{
			line-height:24px;
			height:24px;
			width:85px;
			color:#141513;
			background-color:#c9d9f2;
			font-size:14px;
			font-weight:400;
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
		.classSh:hover{
			background-color:#b6ccf0;
		}
		/* 审核通过 按钮*/
		.classSHTG{
			line-height:25px;
			height:25px;
			width:77px;
			color:#ffffff;
			background-color:#6dab5e;
			font-size:15px;
			font-weight:normal;
			font-family:Arial;
			border:1px solid #ffffff;
			-webkit-border-top-left-radius:4px;
			-moz-border-radius-topleft:4px;
			border-top-left-radius:4px;
			-webkit-border-top-right-radius:4px;
			-moz-border-radius-topright:4px;
			border-top-right-radius:4px;
			-webkit-border-bottom-left-radius:4px;
			-moz-border-radius-bottomleft:4px;
			border-bottom-left-radius:4px;
			-webkit-border-bottom-right-radius:4px;
			-moz-border-radius-bottomright:4px;
			border-bottom-right-radius:4px;
			-moz-box-shadow: inset 0px 20px 0px -24px #e67a73;
			-webkit-box-shadow: inset 0px 20px 0px -24px #e67a73;
			box-shadow: inset 0px 20px 0px -24px #e67a73;
			text-align:center;
			display:inline-block;
			text-decoration:none;
		}
		.classSHTG:hover{
			background-color:#3bc306;
		}
		/* 有问题 按钮*/
		.classYWT{
			line-height:25px;
			height:25px;
			width:77px;
			color:#ffffff;
			background-color:#f78d83;
			font-size:15px;
			font-weight:normal;
			font-family:Arial;
			border:1px solid #ffffff;
			-webkit-border-top-left-radius:4px;
			-moz-border-radius-topleft:4px;
			border-top-left-radius:4px;
			-webkit-border-top-right-radius:4px;
			-moz-border-radius-topright:4px;
			border-top-right-radius:4px;
			-webkit-border-bottom-left-radius:4px;
			-moz-border-radius-bottomleft:4px;
			border-bottom-left-radius:4px;
			-webkit-border-bottom-right-radius:4px;
			-moz-border-radius-bottomright:4px;
			border-bottom-right-radius:4px;
			-moz-box-shadow: inset 0px 20px 0px -24px #e67a73;
			-webkit-box-shadow: inset 0px 20px 0px -24px #e67a73;
			box-shadow: inset 0px 20px 0px -24px #e67a73;
			text-align:center;
			display:inline-block;
			text-decoration:none;
		}
		.classYWT:hover{
			background-color:#eb675e;
		}
		.fixed_div {
			position:absolute;
			z-index:2008;
			top:50px;
			left:20px;
		}
		.w-photo{float:left;text-align: center;display: inline-block;
			border: 1px solid #c2c2c2;
			margin-right: 8px;
			border-radius: 4px;
			line-height: 25px;
			height: 25px;
			width: 60px;
			color: #ffffff;
			background-color: #dadada;
			font-size: 15px;
			text-decoration: none;}
		#paizhao{position: fixed;right:0;top:0;}

	</style>
	<!--  -->
	<%
		String orderstruts=request.getParameter("orderstruts");
		request.setAttribute("orderstruts",orderstruts);
	%>
	<script type="text/javascript">
        //图片文件后缀名的验证
        function image_check(feid) {
            var img = document.getElementById(feid);
            return /.(jpg|jpeg|png)$/.test(img.value.toLowerCase()) ? true : (function() {
                alert('图片格式仅支持jpg、jpeg、png格式。');
                return false;
            })();
        }
        //本地无法访问时候切换在线图片
        function imgError(obj, srcUrl) {
            obj.onerror='';
            obj.src= 'https://img.import-express.com/importcsvimg/inspectionImg/' + srcUrl;
        }
        //加载完成之后
        $(document).ready(function(){

            $("input[id^='inputPrintid']").attr("checked","true");
            $("#allSelcheckid").attr("checked","true");

        })
        window.onload=function(){
            var ordersLength = document.getElementById("ordersLength").value;
            for(var i=0; i<ordersLength; i++){
                altRows('alternatecolor'+(i+1));
            }

        }
        function scrollImg(){
            var posX,posY;
            if (window.innerHeight) {
                posX = window.pageXOffset;
                posY = window.pageYOffset;
            }
            else if (document.documentElement && document.documentElement.scrollTop) {
                posX = document.documentElement.scrollLeft;
                posY = document.documentElement.scrollTop;
            }
            else if (document.body) {
                posX = document.body.scrollLeft;
                posY = document.body.scrollTop;
            }

            var ad=document.getElementById("gdwzdiv");
            ad.style.top=(posY+100)+"px";
            ad.style.left=(posX+50)+"px";
            setTimeout("scrollImg()",100);
        }
        function setBntJy(obj){
            //alert("123");
            $(obj).attr("disabled", true);

            setTimeout(function () {

            }, 2000);
            //alert("456");
        }
        function funkeyup(val){


            $("#selStruts option").each(function(){
                var optionVal = $(this).val();
                var staStr = "&userid=";
                var start = optionVal.indexOf(staStr)+staStr.length;
                var str1 = optionVal.substring(0,start);

                var endStr = "&orderstruts";
                var end = optionVal.indexOf(endStr);
                var str2 = optionVal.substring(end,optionVal.length);
                $(this).val(str1+val+str2);
            });

            $("#selDate option").each(function(){
                var optionVal = $(this).val();
                var staStr = "&userid=";
                var start = optionVal.indexOf(staStr)+staStr.length;
                var str1 = optionVal.substring(0,start);

                var endStr = "&orderstruts";
                var end = optionVal.indexOf(endStr);
                var str2 = optionVal.substring(end,optionVal.length);
                $(this).val(str1+val+str2);
            });
        }

        function funkeyup2(val){

            $("#selStruts option").each(function(){
                var optionVal = $(this).val();
                var staStr = "&orderid=";
                var start = optionVal.indexOf(staStr)+staStr.length;
                var str1 = optionVal.substring(0,start);

                var endStr = "&userid";
                var end = optionVal.indexOf(endStr);
                var str2 = optionVal.substring(end,optionVal.length);
                $(this).val(str1+val+str2);
            });

            $("#selDate option").each(function(){
                var optionVal = $(this).val();
                var staStr = "orderid=";
                var start = optionVal.indexOf(staStr)+staStr.length;
                var str1 = optionVal.substring(0,start);

                var endStr = "&userid";
                var end = optionVal.indexOf(endStr);
                var str2 = optionVal.substring(end,optionVal.length);
                $(this).val(str1+val+str2);
            });
        }
        //页面被点击
        function bodyFun(){
            //alert("我被点击");
//	$(':button').attr({"disabled":"disabled"});

//	setTimeout(function () { 
//		$(':button').attr({"disabled":""});
//	   }, 1500);

        }
        //控制重复点击
        function setButton(){
        }
        //scrollImg();


        document.onkeydown=keyDownSearch;

        function keyDownSearch(e) {
            // 兼容FF和IE和Opera
            var theEvent = e || window.event;
            var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
            if (code == 13) {
                aSubmit();//具体处理函数
                return false;
            }
            return true;
        }

        function BigImg(objImg,imgPath,orderid){
            var width = $(objImg).width();
            if(width==50)
            {
                $(objImg).width(300);
                $(objImg).height(300);
                $("#delImgs").val("");
                $("#delImgs").val(imgPath);
                $("#delImgsOrderid").val("");
                $("#delImgsOrderid").val(orderid);
                document.getElementById("del_"+orderid+"").style.display="block";
            }
            else
            {
                document.getElementById("del_"+orderid+"").style.display="none";
                $("#delImgs").val("");
                $("#delImgsOrderid").val("");
                $(objImg).width(50);
                $(objImg).height(50);
            }
        }

        function delImg(){
            var delPath=$("#delImgs").val();
            var delPathOrderid=$("#delImgsOrderid").val();
            if(delPathOrderid==null || delPathOrderid==""){
                alert("没有选择图片");
                return;
            }
            $.ajax({
                url: "/cbtconsole/WebsiteServlet?action=delUploadImage&className=ExpressTrackServlet",
                type: "post",
                data: {"delPath": delPath, "delPathOrderid" : delPathOrderid},
                async: false,
                success: function (response) {
                    if(response!=null){
                        alert("删除成功");
                        location.reload();
                    }
                }, error: function (e) {
                    alert("删除图片失败");
                }
            });
        }

	</script>

</head>
<body style="background-color : #F4FFF4;" >

<div id="div_show">
	<input type="hidden" id="delImgs"/>
	<input type="hidden" id="delImgsOrderid"/>
	<div id="paizhao" style="display: none;">
		<video id="video" width="400" height="400" autoplay></video><br>
		<canvas id="canvas" width="400"  height="400"></canvas>
	</div>
	<div align="center" >
		<div id="dddd"></div>
		<div><H1>商品出库审核  <a href="/cbtconsole/warehouse/getOrderInfoInspectionall.do" style="font-size: 14px">当前原版,点击进入查全部</a></H1>
			<div id="showimg"></div>
		</div>

		<div id="gdwzdiv">
			<form id="idForm" action="getOrderInfoInspection.do" method="GET">
				全选<input id="allSelcheckid"  value="123" type="checkbox" onclick="selChecked()" />
				<!-- 打印订单  class='querycss'-->
				<a href='javascript:void(0);' onclick="print_two()" class='className'>打印</a>
				<select size="4" id="selStruts" style="background-color: #F445FF4"  onchange="window.location=this.value" >
					<option id='selStruts1' selected="selected"
							value='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=1&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>未审核</option>
					<option id='selStruts2' value='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=2&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>已通过</option>
					<option id='selStruts3' value='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=3&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>有问题</option>
					<option id='selStruts0' value='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=0&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>所有</option>
				</select>

				<!-- 时间查询  class='querycss'-->
				<select id="selDate" size="4"  style="background-color: #F445FF4"  onchange="window.location=this.value" >

					<option id="selDate"  selected="selected"
							value='getOrderInfoInspection.do?orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts}&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>不限时间</option>
					<option id="selDate1" value='getOrderInfoInspection.do?day=1&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts}&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>一天以内</option>
					<option id="selDate3" value='getOrderInfoInspection.do?day=3&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts}&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>三天以内</option>
					<option id="selDate7" value='getOrderInfoInspection.do?day=7&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts}&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>一周以内</option>

				</select>

				<select id="selOrderPositon" size="3"  style="background-color: #F445FF4"  onchange="window.location=this.value" >
					<option id="selorder0"  selected="selected"
							value='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts}&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}&orderPosition=0'>所有订单</option>

					<option  id="selorder1"  value='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts}&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}&orderPosition=1'>公司订单</option>
					<option  id="selorder2" value='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts}&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}&orderPosition=2'>仓库订单</option>
				</select>
				<script type="text/javascript">
                    $("#selStruts").find("option[selected$='selected']").attr("selected","");
                    $("#selStruts${oip.orderstruts}").attr("selected","selected");

                    $("#selDate").find("option[selected$='selected']").attr("selected","");
                    $("#selDate${oip.day}").attr("selected","selected");

                    $("#selOrderPositon").find("option[selected$='selected']").attr("selected","");
                    $("#selorder${orderPos}").attr("selected","selected");
				</script>

				用户ID:<input onkeyup="funkeyup(this.value)" class="querycss" style="width : 100px;" id="idname" name="userid" value="${oip.userid}" type="text"/>
				订单号:<input onkeyup="funkeyup2(this.value)" class="querycss" style="width : 160px;" id="idname" name="orderid" value="${oip.orderid}" type="text"/>


				<a href='javascript:void(0);' onclick="aSubmit()" class='className'>查询</a>
				<a href='javascript:void(0);' onclick="cleText()" class='className'>清空</a>
				<a href='javascript:void(0);' onclick="aLLSh()" class='className'>批量审核</a>
				<a href='javascript:void(0);' onclick="printALLbq()" id='pldybnt' class='className'>批量打印</a>
				<input type="hidden"  id="h_day" name="day" value="${oip.day}"/>
				<input type="hidden"  id="pageNum" value="${oip.pageNum}"/>					   <!-- 当前第几页  submit 提交需要的值 -->
				<input type="hidden"  name="pageSize" value="${oip.pageSize}"/>				   <!-- 每页显示多少条  submit 提交需要的值 -->
				<input type="hidden" id="h_orderstruts" name="orderstruts" value="${oip.orderstruts}"/>
				<input type="hidden"  name="orderPosition"  value="${orderPos}">
				<input type="hidden" id="base64_jpeg" value=""/>
				<input type="hidden" id="base64_bmp" value="">
				<input type="hidden" id="totalBuyCount" value=""/>

			</form>
		</div>

		<%  if(!(user.getId() ==1 || user.getAdmName().equalsIgnoreCase("Ling") || user.getAdmName().equalsIgnoreCase("emmaxie"))){%>
		<!-- 载入消息提醒jsp页面 -->
		<jsp:include page="message_notification.jsp"></jsp:include>
		<%}  %>

		<div><h3></h3></div>
		<div>
			<c:forEach items="${oip.allUserids}" var="strOrder" varStatus="status">
				<c:set value="0" var="strOrderT" />
				<div>

					<div>
						<table width="1350px" class="altrowstable" id="alternatecolor${status.count}">
							<tr id="tr1${strOrder }" class="someclass" ><td colspan="8"><div >用户ID:${strOrder }
								&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp

								&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp

							</div></td></tr>
							<tr id="tr2${strOrder }" style="background-color:#F5F5F5;">

								<td><font style="font-size : 15px;">打印</font></td>
								<td><font style="font-size : 15px;">用户ID</font></td>
								<td><font style="font-size : 15px;">订单编号</font></td>
								<td><font style="font-size : 15px;">审核时间</font></td>
								<td><font style="font-size : 15px;">出库照片</font></td>
								<td><font style="font-size : 15px;">验证审核</font></td>


								<td><font style="font-size : 15px;">是否合并</font></td>
								<td><font style="font-size : 15px;">打印标签数量</font></td>

							</tr>
							<c:set var="setTi" value="0"></c:set>
							<c:set var="setTi2" value="0"></c:set>
							<c:forEach items="${oip.pagelist}" var="storageLocation" varStatus="oipStatus">
								<c:if test="${storageLocation.user_id == strOrder}">
									<tr  onmouseover="buttonMouseover('${storageLocation.user_id }','${storageLocation.order_no }') "  id="trid${storageLocation.order_no }">

										<td width="5%" id="tdPrintid${strOrder }"><input id="inputPrintid${strOrder }" type="checkbox"  class='classSh' value="${storageLocation.order_no } "/></td>
										<td width="7%">
											<font style="font-size : 15px;">${storageLocation.user_id }</font>
										</td>
										<td width="20%"><font style="font-size : 15px;">${storageLocation.order_no } </font>
											<c:if test="${storageLocation.isDropshipOrder==1 }">
												<img src="/cbtconsole/img/ds1.png" style="width:25px;margin-top: 2px;" title="drop shipping" >
											</c:if>
											<c:if test="${orderstruts == 1 || orderstruts == null || orderstruts == ''}">
												<br><a target="_blank" href="/cbtconsole/warehouse/getDetailsForOrderid?orderid=${storageLocation.order_no }&pageNum=1&pageSize=300">to新出库审核页面</a>
											</c:if>
										</td>
										<td width="7%"><font style="font-size : 15px;">${storageLocation.create_time }</font></td>
										<td width="33%">
											<!--原本的拍照上传功能-->
											<%--<input type="button" onclick="btnCap('${storageLocation.order_no}')" value="拍照" class="w-photo"/>--%>
											<!-- 添加选择框 文件上传 -->
											<div>
												<input type="file" style="height: 25px;" id="fileImg${storageLocation.order_no}" name="file" multiple="true" style="float: left;"/>
												<input type="button" style="height: 25px;" onclick="btnCapFile('${storageLocation.order_no}')" value="上传" style="float: left;"/>
											</div>
											<!--上传后图片显示-->
											<div id="orderImgs_${storageLocation.order_no}">${storageLocation.localImgPath}</div>
											<input type="button" onclick="delImg();" id="del_${storageLocation.order_no}" style="display: none" value="删除" class="w-photo"/>
										</td>
										<td width="10%">
											<font style="font-size : 15px;">
												<span  id="msgid${storageLocation.order_no }"></span>
												<input id="aYztg" class='classYWT' type="button" style="" onclick="showProblemDiv('${storageLocation.order_no }')" value="有问题"/>
												<input id="deleteTgAndBtg" class='classYWT' type="button" style="" onclick="deleteSh('${storageLocation.order_no }')" value="重新审核"/>
												<input id="bYzbtg"type="button" name='shtgan${storageLocation.order_no }' onclick="auditPass('${storageLocation.order_no }',${storageLocation.isDropshipOrder});" class='classSHTG' value="审核通过"/>
											</font>

											<!-- 用于批量审核  -->
											<input  type="hidden"  name="order_autopass${storageLocation.order_no }"  value="${storageLocation.order_no },${storageLocation.isDropshipOrder},${storageLocation.user_id }">
											<!-- 保存用户的订单 -->
											<input type="hidden" value="${storageLocation.order_no }" name="orders${strOrder }">
											<!--暂时没用，这里用来做动态操作表格的 -->
											<input type="hidden"  id="h_isRemoveTr${strOrder }"/>
										</td>



										<!--  -->

										<!-- 合并行 -->
										<c:if test="${strOrderT == 0}">
											<c:if test="${storageLocation.mergestate =='1'}">
												<%--<td id="rowspanid${strOrder }"><input id="inputid${strOrder }" type="button" onclick="updateHb('${storageLocation.mergeOrders}')" class='classSh' value="取消合并" disabled="disabled"/></td>--%>
												<td id="rowspanid${strOrder }"><span style="color:green;">客户已选合并</span></td>
											</c:if>
											<c:if test="${storageLocation.mergestate =='0'}">
												<td id="rowspanid${strOrder }"><span style="color:red;">客户默认不合并</span></td>
												<%--<td id="rowspanid${strOrder }"><input id="inputid${strOrder }" type="button" onclick="mergeOrders()" class='classSh' value="合并" disabled="disabled"/></td>--%>
												<c:set var="setTi2" value="1"></c:set>
											</c:if>
											<c:if test="${storageLocation.mergestate !='0' && storageLocation.mergestate !='1'}">
												<td id="rowspanid${strOrder }"><input id="inputid${strOrder }" type="button" value="站位"/></td>
											</c:if>
										</c:if>

										<input id="mergestate${storageLocation.order_no }" type="hidden" value="${storageLocation.mergestate}"/>

										<!-- 如果是已通过 -->
										<c:if test="${oip.orderstruts ==2}">

											<c:if test="${strOrderT == 0 || setTi2 =='1'}">   <!-- 如果是合并-->
												<td id="rowspanid2${strOrder }" style="width:8%" class="ydy${storageLocation.order_no }">
													<!-- 用来打印做判断 是否合并打印 -->
													<c:set var="setTi" value="${setTi+1 }"></c:set>
													<c:if test="${storageLocation.len=='' ||storageLocation.len==null}">
														<input id="num1"type="button"  onclick="setNumber('${storageLocation.order_no }',1);" style="width: 20px"  class='classSHTG' value="1"/>
														<input id="num2"type="button"  onclick="setNumber('${storageLocation.order_no }',2);" style="width: 20px"  class='classSHTG' value="2"/>
														<input id="num3"type="button"  onclick="setNumber('${storageLocation.order_no }',3);" style="width: 20px"  class='classSHTG' value="3"/>
														<input id="num4"type="button"  onclick="setNumber('${storageLocation.order_no }',4);" style="width: 20px"  class='classSHTG' value="4"/>
														<input id="num5"type="button"  onclick="setNumber('${storageLocation.order_no }',5);" style="width: 20px"  class='classSHTG' value="5"/>
														<input id="num6"type="button"  onclick="setNumber('${storageLocation.order_no }',6);" style="width: 20px"  class='classSHTG' value="6"/>
														<input id="num7"type="button"  onclick="setNumber('${storageLocation.order_no }',7);" style="width: 20px"  class='classSHTG' value="7"/>
														<input id="num8"type="button"  onclick="setNumber('${storageLocation.order_no }',8);" style="width: 20px"  class='classSHTG' value="8"/>
														<input id="num9"type="button"  onclick="setNumber('${storageLocation.order_no }',9);" style="width: 20px"  class='classSHTG' value="9"/>
														&nbsp;&nbsp;
														<input style="width:30px" id="splitQuantity${storageLocation.order_no }" type="text" value="1"/>
														<input id="batchInsertSPId${storageLocation.order_no }"type="button" name="btn${storageLocation.order_no }" onclick="$(this).val('正在操作');batchInsertSP('${storageLocation.user_id }','${storageLocation.order_no }',0);" style="width: 120px"  class='classSHTG' value="输入数量打印"/>
													</c:if>
													<c:if test="${storageLocation.len!='' &&storageLocation.len==1}">

														<input style="width:30px" id="splitQuantity${storageLocation.order_no }" type="text" value="${storageLocation.len}"/>
														<input id="batchInsertSPId${storageLocation.order_no }" style="width: 150px" type="button" onclick="deleteShippingPackage();" class='classYWT' value="删除已打印"/>
														${storageLocation.shipmentno}
													</c:if>
													<c:if test="${storageLocation.len>1}">
														<input style="width:30px" id="splitQuantity${storageLocation.order_no }" type="text" value="${storageLocation.len}"/>
														<input id="batchInsertSPId${storageLocation.order_no }" style="width: 150px" type="button" onclick="deleteShippingPackage();" class='classYWT' value="删除已打印"/>
														${storageLocation.shipmentno}
													</c:if>

												</td>
											</c:if>
										</c:if>
										<!-- 如果是不是已通过 -->
										<c:if test="${oip.orderstruts ==1 ||oip.orderstruts ==0||oip.orderstruts ==3}">
											<td id="rowspanid2${strOrder }" style="width:10%">
												<c:if test="${storageLocation.len=='' ||storageLocation.len==null}">
													<input id="num1"type="button"   onclick="setNumber('${storageLocation.order_no }',1);" style="width: 20px"  class='classSHTG' value="1"/>
													<input id="num2"type="button"   onclick="setNumber('${storageLocation.order_no }',2);" style="width: 20px"  class='classSHTG' value="2"/>
													<input id="num3"type="button"   onclick="setNumber('${storageLocation.order_no }',3);" style="width: 20px"  class='classSHTG' value="3"/>
													<input id="num4"type="button"   onclick="setNumber('${storageLocation.order_no }',4);" style="width: 20px"  class='classSHTG' value="4"/>
													<input id="num5"type="button"   onclick="setNumber('${storageLocation.order_no }',5);" style="width: 20px"  class='classSHTG' value="5"/>
													<input id="num6"type="button"   onclick="setNumber('${storageLocation.order_no }',6);" style="width: 20px"  class='classSHTG' value="6"/>
													<input id="num7"type="button"   onclick="setNumber('${storageLocation.order_no }',7);" style="width: 20px"  class='classSHTG' value="7"/>
													<input id="num8"type="button"   onclick="setNumber('${storageLocation.order_no }',8);" style="width: 20px"  class='classSHTG' value="8"/>
													<input id="num9"type="button"   onclick="setNumber('${storageLocation.order_no }',9);" style="width: 20px"  class='classSHTG' value="9"/>
													&nbsp;&nbsp;
													<input style="width:30px" id="splitQuantity${storageLocation.order_no }" type="text" value="1"/>
													<input id="batchInsertSPId${storageLocation.order_no }"type="button" name="btn${storageLocation.order_no }" onclick="$(this).val('正在操作');batchInsertSP('${storageLocation.user_id }','${storageLocation.order_no }',0);" style="width: 120px"  class='classSHTG' value="输入数量打印"/>
												</c:if>
												<c:if test="${storageLocation.len!='' &&storageLocation.len==1}">

													<input style="width:30px" id="splitQuantity${storageLocation.order_no }" type="text" value="${storageLocation.len}"/>
													<input id="batchInsertSPId${storageLocation.order_no }"type="button" name="btn${storageLocation.order_no }" onclick="$(this).val('正在操作');batchInsertSP('${storageLocation.user_id }','${storageLocation.order_no }',0);" style="width: 120px" class='classSHTG' value="输入数量打印"/>

												</c:if>
												<c:if test="${storageLocation.len>1}">
													<input style="width:30px" id="splitQuantity${storageLocation.order_no }" type="text" value="${storageLocation.len}"/>
													<input id="batchInsertSPId${storageLocation.order_no }"type="button" onclick="deleteShippingPackage();" class='classYWT' value="取消拆分"/>
												</c:if>
											</td>
										</c:if>
											<%-- <c:if test="${storageLocation.isDropshipOrder==0 }">    --%>
										<c:set value="${strOrderT+1 }" var="strOrderT" />
											<%--  </c:if> --%>
									</tr  >

									<!-- 有问题 -->
									<tr onmouseover="buttonMouseover('${storageLocation.user_id }','${storageLocation.order_no }') " id="showProblemDiv${storageLocation.order_no }" style="display: none;">
										<td>问题描述:</td>
										<td colspan="2"><input type="text"  id="problem${storageLocation.user_id }${storageLocation.order_no }" height="80px" width="350px" ></input></td>
										<td colspan="1">
											<div>
												<input class='classYWT' onclick="auditNotPass('${storageLocation.order_no }',${storageLocation.isDropshipOrder})" type="button" value="不通过"/>
												<input id="idjxtg" class='classSHTG' onclick="auditPass('${storageLocation.order_no }',${storageLocation.isDropshipOrder})" type="button" value="继续通过"/>
											</div>
										</td>
									</tr>

								</c:if>

							</c:forEach>


							<!-- 如果用户只有一个订单就不需要合并 disabled-->
							<c:if test="${strOrderT == 0}">
								<script type="text/javascript">
                                    $("#inputid"+'${strOrder }').hide();
                                    $("#inputid"+'${strOrder }').attr("disabled","true");
                                    $("#inputid"+'${strOrder }').css("color","red")
                                    $("#inputid"+'${strOrder }').val("不需要合并")
								</script>
							</c:if>


							<!-- 添加合并列-->
							<script type="text/javascript">
                                var rowlen = "${strOrderT}";
                                $("#rowspanid"+'${strOrder }').attr("rowspan",rowlen);
							</script>
							<!--  -->

							<!-- 打印标签数量 是否合并-->
							<c:if test="${setTi == 1}">
								<script type="text/javascript">

                                    var rowlen = "${strOrderT}";
                                    $("#rowspanid2"+'${strOrder }').attr("rowspan",rowlen);
								</script>
							</c:if>



						</table>
					</div>
				</div>
			</c:forEach>
		</div>
		<!-- 用来空行 -->
		<div>
			<h3></h3>
		</div>
		<div>
			<select class='classSelect' onchange="window.location=this.value" >
				<option selected="selected" value='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize}'>每页显示${oip.pageSize}条</option>

				<c:forEach begin="1" end="3" var="i">
					<c:if test="${i*50 != oip.pageSize}">
						<option value='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts }&pageNum=${oip.previousPage }&pageSize=${i*50 }'> 每页显示${i*50}条</option>
					</c:if>
				</c:forEach>

			</select>
			<a href='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts }&pageNum=1&pageSize=${oip.pageSize }' class='className'>第一页</a>
			<a href='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize }' class='className'>上一页</a>
			<a href='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts }&pageNum=${oip.nextPage }&pageSize=${oip.pageSize }' class='className'>下一页</a>
			<a href='getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts }&pageNum=${oip.pageCount }&pageSize=${oip.pageSize }' class='className'>最后一页</a>
		</div>
		<div>
			<h4>当前第${oip.pageNum}页 &nbsp;&nbsp; 共${oip.pageCount}页&nbsp;&nbsp; 共${oip.pageSum }记录 </h4>

		</div>

		<!-- 输入问题div   displayblock  显示-->
		<div id="ProblemDiv" style="display: none;" >

		</div>
	</div>
	<a href="getOrderInfoInspection.do?day=${oip.day}&orderid=${oip.orderid}&userid=${oip.userid }&orderstruts=${oip.orderstruts }&pageNum=${oip.previousPage }&pageSize=${oip.pageSize }"><span id="spanId"></span></a>
	<input type="hidden"  id="ordersLength" value="${fn:length(oip.allUserids)}"/>  <!-- 商品个数  用来做表格颜色-->

	<input type="hidden"  id="h_userid"/>    <!-- 当前鼠标指向用户id -->
	<input type="hidden"  id="h_orderno"/>   <!-- 当前鼠标指向用户订单 -->


	<c:if test="${oip.orderstruts == '0'}">
		<script type="text/javascript">
            //	alert("我是所有");

            //查询所有 都不能点击
            setButtonDisabled("input[id='aYztg']");       //通过
            setButtonDisabled("input[id^='inputid']");	  //合并
            setButtonDisabled("input[id='bYzbtg']");	  //有问题
            setButtonDisabled("input[id='deleteTgAndBtg']");	  //重新审核


            setButtonDisabled("input[id^='batchInsertSPId']");	  //打印按钮
            setButtonDisabled("input[id^='num']");	  //打印数量
            setButtonDisabled2("input[id^='splitQuantity']");	  //打印数量

		</script>
	</c:if>



	<c:if test="${oip.orderstruts == '2'}">
		<script type="text/javascript">
            //	alert("我是已通过");
            //	setButtonDisabled("input[id^='inputid']");	  //合并
            setButtonDisabled("input[id='bYzbtg']");	  //有问题
            setButtonDisabled("input[id='idjxtg']");	  //继续通过
		</script>
	</c:if>

	<c:if test="${oip.orderstruts == '3'}">
		<script type="text/javascript">
            //	alert("我是有问题");
            setButtonDisabled("input[id='aYztg']");       //通过
            setButtonDisabled("input[id^='inputid']");	  //合并

            setButtonDisabled("input[id^='batchInsertSPId']");	  //打印按钮
            setButtonDisabled("input[id^='num']");	  //打印数量
            setButtonDisabled2("input[id^='splitQuantity']");	  //打印数量
		</script>
	</c:if>

	<c:if test="${oip.orderstruts == '1'}">
		<script type="text/javascript">
            //	alert("我是未审核");
            setButtonDisabled("input[id^='inputid']");	  //合并
            setButtonDisabled("input[id='deleteTgAndBtg']");	  //重新审核

            setButtonDisabled("input[id^='batchInsertSPId']");	  //打印按钮
            setButtonDisabled("input[id^='num']");	  //打印数量
            setButtonDisabled2("input[id^='splitQuantity']");	  //打印数量
            //不需要禁用
		</script>
	</c:if>
	<script type="text/javascript">
        window.addEventListener("DOMContentLoaded", function() {
            // Grab elements, create settings, etc.  
            var canvas = document.getElementById("canvas"),
                context = canvas.getContext("2d"),
                video = document.getElementById("video"),
                videoObj = { "video": true },
                errBack = function(error) {
                    console.log("Video capture error: ", error.code);
                };

            // Put video listeners into place  
            if(navigator.getUserMedia) { // Standard  
                //alert("支持navigator.getUserMedia");
                navigator.getUserMedia(videoObj, function(stream) {
                    video.src = stream;
                    video.play();
                }, errBack);
            } else if(navigator.webkitGetUserMedia) { // WebKit-prefixed  
                //alert("支持navigator.webkitGetUserMedia");
                navigator.webkitGetUserMedia(videoObj, function(stream){
                    video.src = window.webkitURL.createObjectURL(stream);
                    video.play();
                }, errBack);
            }
            else if(navigator.mozGetUserMedia) { // Firefox-prefixed  
                //alert("支持navigator.mozGetUserMedia");
                navigator.mozGetUserMedia(videoObj, function(stream){
                    video.src = window.URL.createObjectURL(stream);
                    video.play();
                }, errBack);
            }

        }, true);

        //上传图片 feid为 input[file] 的id，callback为回调函数
        function ajax_upload(feid, orderid, callback) {
            if(image_check(feid)) { //文件后缀名的验证
                $.ajaxFileUpload({
                    fileElementId: feid, //需要上传的文件域的ID，即<input type="file">的ID。
                    url: '/cbtconsole/warehouse/uploadImageFile', //后台方法的路径
                    type: 'post', //当要提交自定义参数时，这个参数要设置成post
                    data: {orderid:orderid},
                    dataType: 'json', //服务器返回的数据类型。可以为xml,script,json,html。如果不填写，jQuery会自动判断。
                    secureuri: false, //是否启用安全提交，默认为false。
                    async: true, //是否是异步
                    success: function(data) { //提交成功后自动执行的处理函数，参数data就是服务器返回的数据。
                        if(callback) callback.call(this, data);
                    },
                    error: function(data, status, e) { //提交失败自动执行的处理函数。
                        console.error(e);
                    }
                });
            }
        }
        // 出库照片图片上传
        function btnCapFile(orderid){
            ajax_upload("fileImg" + orderid, orderid, function(data) {
                //上传图片的成功后的回调 显示上传的图片
                if(data != null && data.uploadImgList != null && data.uploadImgList.length > 0){
                    var orderid = data.orderid;
                    $(data.uploadImgList).each(function(indexImg, itemImg) {
                        var temId = itemImg.substring(itemImg.indexOf("/") + 1, itemImg.indexOf("."));
                        var temSrc = data.imagehost + itemImg;
                        var temHtml = "<img onerror=\"imgError(this,\'" + itemImg + "\')\" width='50px' height='50px' onclick=\"BigImg(this,\'"
                            + itemImg
                            + "\',\'"
                            + orderid
                            + "\');\" id='"
                            + temId
                            + "' src='" + temSrc + "' />";
                        $("#orderImgs_"+orderid+"").append(temHtml);
                    });
                    //上传后文件选择框清空
                    $("#fileImg" + orderid).get(0).value = ""
                }
            });
        }

        function btnCap(orderid){
            //document.getElementById("paizhao").style.display="";
//         	alert(orderid+"--"+goodsid);
            var canvas = document.getElementById("canvas"),
                context = canvas.getContext("2d"),
                video = document.getElementById("video"),
                videoObj = { "video": true },
                errBack = function(error) {
                    console.log("Video capture error: ", error.code);
                };
            if(navigator.getUserMedia) {
                navigator.getUserMedia(videoObj, function(stream) {
                    video.src = stream;
                    video.play();
                }, errBack);
            } else if(navigator.webkitGetUserMedia) {
                navigator.webkitGetUserMedia(videoObj, function(stream){
                    video.src = window.webkitURL.createObjectURL(stream);
                    video.play();
                }, errBack);
            }
            else if(navigator.mozGetUserMedia) {
                navigator.mozGetUserMedia(videoObj, function(stream){
                    video.src = window.URL.createObjectURL(stream);
                    video.play();
                }, errBack);
            }
            // 触发拍照动作
//         document.getElementById("snap33").addEventListener("click", function() {  
            context.drawImage(video, 0, 0, 400, 400);
            var imgData = document.getElementById("canvas").toDataURL();
            var base64Data = imgData.substr(22);
            $.ajax({
                url: "/cbtconsole/WebsiteServlet?action=uploadImage&className=ExpressTrackServlet",
                type: "post",
                data: {"fileData": base64Data, orderid : orderid},
                async: false,
                success: function (response) {
                    if(response!=null){
                        $("#orderImgs_"+orderid+"").html("");
                        $("#orderImgs_"+orderid+"").append(response);
                    }
                }, error: function (e) {
                    alert("图片上传失败");
                }
            });
        }



        var dragObj=document.getElementById("paizhao");
        var w=$(window).width();
        w=w-320;


        dragObj.style.left =w+"px";
        dragObj.style.top = "0px";
        var mouseX, mouseY, objX, objY;
        var dragging = false;
        dragObj.onmousedown = function (event) {
            event = event || window.event;

            dragging = true;
            dragObj.style.position = "fixed";
            mouseX = event.clientX;
            mouseY = event.clientY;
            objX = parseInt(dragObj.style.left);
            objY = parseInt(dragObj.style.top);
        }

        document.onmousemove = function (event) {
            event = event || window.event;
            if (dragging) {
                dragObj.style.left = parseInt(event.clientX - mouseX + objX) + "px";
                dragObj.style.top = parseInt(event.clientY - mouseY + objY) + "px";
            }
        }

        document.onmouseup = function () {
            dragging = false;
        }

	</script>
</div>
<div id="div_print" style="display: none;height:600px;margin-left:90px;">
	<table>
		<tr>
			<td><span style="font-weight:bold;font-size:30px;" id="sp_id"></span><span id="orderid" style="margin-left:50px;font-weight:bold;font-size:30px;"></span></td>
			<!--         <td><span id="country"></span></td> -->
		</tr>
		<tr><td rowspan="2"><span style="margin-left:0px;font-weight:bold;font-size:30px;" id="country"></span></td></tr>
		<tr></tr>
		<tr></tr>
		<tr></tr>
		<tr></tr>
		<tr></tr>
		<tr><td rowspan="2"><canvas id="canvascode" style="height:150px;width:300px;margin-left:0px;"></canvas></td></tr>
	</table>
</div>

</body>

</html>