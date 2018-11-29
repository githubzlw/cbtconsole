<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.cbt.util.Redis"%>
<% 
String sessionId = request.getSession().getId();
String userJson = Redis.hget(sessionId, "admuser"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript">
//消息提醒
$(function() {

	var userJson = <%=userJson%>;
	if(userJson.roletype == 2){
		$("#customer_type5").hide();
		$("#delay_type10").hide();
	}
	//checkUserId();//第一次进行校检
	$(".type_message").hide();//隐藏消息分类的每个模块
	//setTimeout("queryStatistical()",10000);//10秒后读取一次
	//setInterval("queryStatistical()",60000);//60秒刷新一次

	//点击图标时显示内容体
	$(".message_count").click(function() {	
		$(".option_show").toggle();
	});
	
	$(".message_content").hide();//打开页面，首次时隐藏
	
});

//判断登录人是否是仓库Eric
function checkUserId(){
	var userJson = <%=userJson%>;
	var adminId = userJson.id;
	if(adminId == "15" || adminId == 15){
		$.ajax({   
			type : 'POST',
			url : '/cbtconsole/messageNotification/updateApprovedOrder',
			success : function(data) {
				
			}
		});
	}
}

function queryStatistical(){

	var userJson = <%=userJson%>;
	//clearMessageContent();
	$.ajax({
		type : 'POST',
		url : '/cbtconsole/messageNotification/queryStatistical',
		data : {
			adminId : userJson.id,
		},
		success : function(data) {
			if (data.ok) {
				var statisticals = data.data;	
				var total = 0;
				var ctLst = new Array();
				if(statisticals.length >0){
					for(var k=0;k<11;k++){
						ctLst[k] =0;
					}
					for(var i = 0; i < statisticals.length; i++){
						ctLst[statisticals[i].sendType] = statisticals[i].count;
						total += statisticals[i].count;
					}
					showTypeCount(ctLst,total);
				} else{
					$(".message_count").find("span").html("0");
					$("#total_zero").empty();
					$("#total_zero").text("当前无未处理数据");	
					//$(".type_message").show();
				}
			} else {
				$(".message_count").find("span").html("0");
				$("#total_zero").empty();
				$("#total_zero").text("获取消息失败，原因：" + data.message);
				//$(".type_message").show();
			}
		}
	});
}


//显示每个类别的数据数量
function showTypeCount(ctLst,total){
	for(var i=0;i<ctLst.length;i++){
// 		if(!(i == 3 || i == 4 || i == 6 || i == 8)){
			$("#count_type" + i).html(ctLst[i]);
// 		}
	}
	if(total > 99){
		$("#content_num").html("99+");
	} else{
		$("#content_num").html(total);
	}
}



//根据类别查询左边数据
function showMessageByType(type,obj){
	
	var userJson = <%=userJson%>;
	clearMessageContent();	
	$(".type_message").hide();
	if(obj != null){
		$(".main_ul").find("li").find("h2").removeClass('selected');
		$(obj).addClass('selected');
	}
	
	$.ajax({
		type : 'POST',
		url : '/cbtconsole/messageNotification/queryByAdminId',
		data : {
			adminId : userJson.id,
			sendType : type
		},
		success : function(data) {
			if (data.ok) {
				var messages = data.data;
				if (messages.length > 0) {
					if(data.total == 0){
						$("#content_num").html(0);
						$("#total_zero").show();
					} else{
						$("#total_zero").hide();						
					}
					var id = "#message_type"+ type;
					for (var i = 0; i < messages.length; i++) {
						var content="";
						var sendType = messages[i].sendType;
						if (messages[i].isRead == "Y") {
							content += "<li style='background-color:darkgrey'><span class='message_date'>" + new Date(messages[i].createTime).toLocaleString();
							content += "<a class='notState'>已读</a>";
						} else {
							content += "<li><span class='message_date'>" + new Date(messages[i].createTime).toLocaleString();
							content += "<a href='#' class='state' title='点击标记为已读' onclick='readMessage("+ messages[i].id 
							+ ",\"" + (messages[i].orderNo == null ? "":messages[i].orderNo) + "\"," 
							+ messages[i].sendType + "," + messages[i].senderId + "," + messages[i].message_id + ")'>标记为已读</a>";
						}
						content += "</span><br><span class='state_span' "; 
						content += ">";
						if (messages[i].orderNo != null && messages[i].orderNo != "") {
							content += "<a data-id='"+ messages[i].id + "," + messages[i].sendType + "'";
							if(!(sendType == 7 || sendType == 9)){
								content += " title='点击后,系统默认设置此消息已处理' ";
							} else{
								content += " title='点击并且处理完成后,请点击未处理按钮,设置此消息已处理' ";
							}
							content +=" class='orderNoUrl' href='"+messages[i].linkUrl+"'>(" + messages[i].orderNo + ")</a>";
						}
						content += messages[i].sendContent + "</span></li>";								
						$(id).find(".tips_list").append(content);
					}
					prepareToJump();				
					$("#total_zero").hide();
					$("#message_type" + type).show();
					$(".message_content").show();
				} else{
					$("#total_zero").show();
					setTimeout("closeTotalZero()",2000);//2秒后读取一次
				}
			} else {
				$(".message_count").find("span").html("0");
				$("#total_zero").empty();
				$("#total_zero").text("获取消息失败，原因：" + data.message);
				$(".type_message").show();
			}
		}
	});
	
}

//2秒后隐藏total_zero显示
function closeTotalZero(){
	$("#total_zero").hide();
}

//清理所有消息数据
function clearMessageContent() {
	for(var i=0;i<11;i++){
		$("#message_type" + i).find(".tips_list").empty();
	}
}



//消息体的数据点击时的跳转
function prepareToJump(){
	$(".orderNoUrl").unbind('click').click(function(){
		var userJson = <%=userJson%>;
		var roletype = userJson.roletype;
		var admName = userJson.admName;
		var admId = userJson.id;
		var hrefVal = $(this).attr("href");
		var url = "";
		
		var data_id = $(this).attr("data-id");
		var dataArr = data_id.split(",");
		
		if(dataArr.length == 2){
			if(dataArr[1] == 1){
				if(admName.toLowerCase() == "ling" || admName.toLowerCase() == "emmaxie" || admName.toLowerCase() == "admin1"){
					url = "/order/getOrderInfo.do?showUnpaid=0&orderno=" + hrefVal
				} else if(roletype == 1){
					url = "/order/getOrderInfo.do?showUnpaid=0&orderno=" + hrefVal
				} else if(roletype == 2){
					url = "/PurchaseServlet?action=getPurchaseByXXX&className=Purchase&pagenum=1&goodsid=&orderid_no_array="
							+"&orderid=0&admid="+admId+"&userid=&orderno="+hrefVal+"&goodid&date=&days=&state=&unpaid=0&pagesize=50&goodname=";
				} else{
					url = "/order/getOrderInfo.do?showUnpaid=0&orderno=" + hrefVal
				}
			} else if(dataArr[1] == 5){
				url = "/customerServlet?action=findAll&className=GuestBookServlet&userId=" + hrefVal;
			}else if(dataArr[1] == 0){
				url = "/PurchaseServlet?action=getStockOrderInfo&className=Purchase&pagenum=1&state=0&orderno="+hrefVal;
			} else if(dataArr[1] == 7){
				url = "/warehouse/getOrderinfoPage.do?orderid=" + hrefVal;
			}else if(dataArr[1] == 9){
				url = "/website/purchase_order_details.jsp?shipno="+hrefVal+"&id="+dataArr[0];
			}else if(dataArr[1] == 10){
				//物流延迟信息)
				if(hrefVal.indexOf("原飞航") != -1){
					var expressno =hrefVal.split("@")[1];
					alert(expressno);
					url="/trackingController/trackinfoByExpressnoAndCompany?companyAndExpressno=yfh@"+expressno;
				}else{
					url="/trackingController/trackinfoByExpressnoAndCompany?companyAndExpressno="+hrefVal;
				}
			}
			if(!(dataArr[1] == 7 || dataArr[1] == 9)){
				readMessage(dataArr[0],hrefVal,dataArr[1],admId,0);    
			}
			//页面跳转
	        window.open('/cbtconsole' + url); 
			
		} else{
			alert("解析数据失败，请刷新后重试");
		}
        return false;
	});
}

//
function hideDiv(){
	$(".message_content").hide();
}

//根据类型设置消息已处理
function doAllReaded(type){
	var userJson = <%=userJson%>;
		var adminId = userJson.id;
		var isDo = confirm("是否全部标记已处理？");
		if (isDo) {
			$.ajax({
				type : 'POST',
				url : '/cbtconsole/messageNotification/doAllReadedByUserId',
				data : {
					userId : userJson.id,
					type : type
				},
				success : function(data) {
					if (data.ok) {
						queryStatistical();
						showMessageByType(type,null);
					} else {
						alert(data.message);
					}
				},
				error : function() {
					alert("操作失败，请刷新页面重试");
				}
			});
		}
	}

	//单条消息已处理
	function readMessage(id, orderNo, type, senderid,message_id) {
		$.ajax({
			type : 'POST',
			url : '/cbtconsole/messageNotification/updateByOrderNoTypeSenderId',
			data : {
				id : id,
				orderNo : orderNo,
				type : type,
				senderid : senderid,
				message_id:message_id
			},
			success : function(data) {
				if (data.ok) {
					queryStatistical();
					showMessageByType(type,null);
				} else {
					alert(data.message);
				}
			}
		});
		return false;
	}
</script>
<style type="text/css">
/*消息提醒 */
.message_show {
	right: 5px;
	position: fixed;
	z-index: 998;
	top: 0px;
}

.message_count {
	background: url(/cbtconsole/img/messageNotice.gif) no-repeat;
	position: absolute;
	right: 10px;
	top: 0px;
	width: 50px;
	height: 55px;
	background-size: 50px auto;
	margin: 20px 0px 28px 20px;
}

.message_count span {
	border-radius: 100%;
	width: 25px;
	height: 25px;
	line-height: 25px;
	font-size: 14px;
	color: #fff;
	top: -9px;
	background: #ff0000;
	position: absolute;
	text-align: center;
	right: -5px;
}

.message_title {
	background: #d7d7d7 none repeat scroll 0 0;
	font-size: 15px;
	font-weight: normal;
	height: 30px;
	line-height: 30px;
	margin-bottom: 4px;
	overflow: hidden;
	padding-left: 5px;
	margin-top: 0px;
}

.message_content {
	border: 1px solid #000;
	background: #efeaea;
	overflow: hidden;
	overflow-y: scroll;
	min-height: 0px;
	max-height: 660px;
	margin: 30px;
	right: 148px;
	position: absolute;
	top: 50px;
}

.message_date {
	text-align: center;
	font-size: 15px;
	color: #999;
	padding: 10px 0;
}

.tips_list {
	padding: 0px 10px;
}

.tips_list li {
	padding: 5px 5px;
	line-height: 100%;
	background: #ffffcc;
	margin-bottom: 12px;
	font-size: 14px;
	list-style: none;
	width: 350px;
	text-align: left;
}
/* .state_span {display: block;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;margin-top: 5px;height: 20px;line-height: 20px;} */
.state_span {
	display: block;
	margin-top: 5px;
	line-height: 20px;
	width: 330px;
}
/* overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 2;} */
.tips_list li a.notState {
	color: #777778;
	float: right;
}

.tips_list li a.state {
	color: blue;
	float: right;
}

.all_read_btn {
	font-size: 16px;
	background: #44a823;
	border: 1px #aaa solid;
	color: #fff;
	height: 30px;
}

.main_ul li {
	color: #308930;
	padding: 0; /* 将默认的内边距去掉 */
	margin: 5px 0px 0px 20px;
	width: 140px;
}

.selected {
	background: #ffffcc;
}

.message_title_list {
	float: right;
}

.main_ul {
	margin-top: 80px;
	list-style: none;
	border: 1px solid #000;
	background: #efeaea;
	overflow: hidden;
	overflow-y: scroll;
	padding-left: 0;
}

.option_show {
	display: none;
}

.main_ul li h2 span {
	color: red;
}
</style>
<title>消息提醒</title>
</head>
<body>



	<div class="message_show" style="width: 420px;">

		<div>
			<div class="message_count">
				<span id="content_num">0</span>
			</div>

		</div>
		<div class="option_show">

			<div class="message_content">
				<p id="total_zero"
					style="color: red; font-size: 14px; width: 100px; display: none;">当前无未处理数据</p>
				<div id="message_type0" class="type_message">
					<input type="button" class="all_read_btn" value="全部已处理"
						onclick="doAllReaded(0)" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
						type="button" class="all_read_btn" value="关闭当前"
						onclick="hideDiv()" />
					<div class="message_infos">
						<ul class="tips_list"></ul>
					</div>
				</div>
				 <div id="message_type1" class="type_message">
					<input type="button" class="all_read_btn" value="全部已处理"
						onclick="doAllReaded(1)" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
						type="button" class="all_read_btn" value="关闭当前"
						onclick="hideDiv()" />
					<div class="message_infos">
						<ul class="tips_list"></ul>
					</div>
				</div>
				<div id="message_type2" class="type_message">
					<input type="button" class="all_read_btn" value="全部已处理"
						onclick="doAllReaded(2)" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
						type="button" class="all_read_btn" value="关闭当前"
						onclick="hideDiv()" />
					<div class="message_infos">
						<ul class="tips_list"></ul>
					</div>
				</div>
				<!-- <div id="message_type3" class="type_message">
					<input type="button" class="all_read_btn" value="全部已处理"
						onclick="doAllReaded(3)" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
						type="button" class="all_read_btn" value="关闭当前"
						onclick="hideDiv()" />
					<div class="message_infos">
						<ul class="tips_list"></ul>
					</div>
				</div> -->
				<!-- <div id="message_type4" class="type_message">
					<input type="button" class="all_read_btn" value="全部已处理"
						onclick="doAllReaded(4)" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
						type="button" class="all_read_btn" value="关闭当前"
						onclick="hideDiv()" />
					<div class="message_infos">
						<ul class="tips_list"></ul>
					</div>
				</div> -->
				<div id="message_type5" class="type_message">
					<input type="button" class="all_read_btn" value="全部已处理"
						onclick="doAllReaded(5)" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
						type="button" class="all_read_btn" value="关闭当前"
						onclick="hideDiv()" />
					<div class="message_infos">
						<ul class="tips_list"></ul>
					</div>
				</div>
				<!-- <div id="message_type6" class="type_message">
					<input type="button" class="all_read_btn" value="全部已处理"
						onclick="doAllReaded(6)" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
						type="button" class="all_read_btn" value="关闭当前"
						onclick="hideDiv()" />
					<div class="message_infos">
						<ul class="tips_list"></ul>
					</div>
				</div> -->
				<div id="message_type7" class="type_message">
					<input type="button" class="all_read_btn" value="全部已处理"
						onclick="doAllReaded(7)" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
						type="button" class="all_read_btn" value="关闭当前"
						onclick="hideDiv()" />
					<div class="message_infos">
						<ul class="tips_list"></ul>
					</div>
				</div>
				<!-- <div id="message_type8" class="type_message">
					<input type="button" class="all_read_btn" value="全部已处理"
						onclick="doAllReaded(8)" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
						type="button" class="all_read_btn" value="关闭当前"
						onclick="hideDiv()" />
					<div class="message_infos">
						<ul class="tips_list"></ul>
					</div>
				</div> -->
				<div id="message_type9" class="type_message">
					<input type="button" class="all_read_btn" value="全部已处理"
						onclick="doAllReaded(9)" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
						type="button" class="all_read_btn" value="关闭当前"
						onclick="hideDiv()" />
					<div class="message_infos">
						<ul class="tips_list"></ul>
					</div>
				</div>
				<div id="message_type10" class="type_message">
					<input type="button" class="all_read_btn" value="全部已处理"
						onclick="doAllReaded(10)" /> &nbsp;&nbsp;&nbsp;&nbsp; <input
						type="button" class="all_read_btn" value="关闭当前"
						onclick="hideDiv()" />
					<div class="message_infos">
						<ul class="tips_list"></ul>
					</div>
				</div>

			</div>


			<div class="message_title_list">

				<ul class="main_ul">
					<!-- <li style="text-align: right;"><input type="button"
						class="all_read_btn" value="全部已处理" onclick="doAllReaded()" /></li>
					<li style="text-align: right;"><input
						type="button" class="all_read_btn" value="立即获取"
						onclick="queryMessage()" /></li> -->
					<li ><h2 onclick="showMessageByType(9,this)"
						class="message_title" >
						疑似货源错误(<span id="count_type9">0</span>)
					</h2></li>
					 <li><h2 onclick="showMessageByType(1,this)"
							class="message_title">
							订单备注(<span id="count_type1">0</span>)
						</h2></li>
<!-- 					<li><h2 onclick="showMessageByType(2,this)" -->
<!-- 							class="message_title" > -->
<!-- 							订单状态修改(<span id="count_type2">0</span>) -->
<!-- 						</h2></li> -->
					<!-- <li><h2 onclick="showMessageByType(3,this)"
							class="message_title">
							新订单(<span id="count_type3">0</span>)
						</h2></li> -->
					<!-- <li><h2 onclick="showMessageByType(4,this)"
							class="message_title">
							拆单(<span id="count_type4">0</span>)
						</h2></li> -->
					<li id="customer_type5"><h2 onclick="showMessageByType(5,this)"
							class="message_title" >
							客户留言(<span id="count_type5">0</span>)
						</h2></li>
					<!-- <li><h2 onclick="showMessageByType(6,this)"
							class="message_title">
							出库(<span id="count_type6">0</span>)
						</h2></li> -->
<!-- 					<li><h2 onclick="showMessageByType(7,this)" -->
<!-- 							class="message_title" > -->
<!-- 							验货有疑问(<span id="count_type7">0</span>) -->
<!-- 						</h2></li> -->
					<!-- <li><h2 onclick="showMessageByType(8,this)"
							class="message_title">
							退款有反馈(<span id="count_type8">0</span>)
						</h2></li>	 -->				
					<li id="delay_type10"><h2 onclick="showMessageByType(10,this)"
							class="message_title" >
							物流延迟(<span id="count_type10">0</span>)
						</h2></li>
<!-- 					<li><h2 onclick="showMessageByType(0,this)" -->
<!-- 							class="message_title" > -->
<!-- 							系统通知(<span id="count_type0">0</span>) -->
<!-- 						</h2></li> -->
				</ul>
			</div>

		</div>

	</div>

</body>
</html>