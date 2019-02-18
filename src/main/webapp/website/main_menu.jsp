<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@page import="java.util.List"%>
<%@page import="com.cbt.util.*"%>
<%@page import="com.cbt.util.Redis"%>
<%@page import="com.cbt.website.userAuth.bean.Admuser"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String sessionId = request.getSession().getId();
	String authJson = Redis.hget(sessionId, "userauth");

	String userJson = Redis.hget(sessionId, "admuser");
	Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
	int uid = user.getId();
    String admName = user.getAdmName();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>菜单</title>
<style type="text/css">
.usetablediv {
	width: 1000px;
	margin: 0 auto;
	padding: 10px;
	border: 1px solid #e2e2e2;
	position: relative;
}

.usetable {
	width: 1000px;
	margin: 0 auto;
	border-collapse: collapse;
}

.useback {
	float: right;
	margin: 30px 300px 0px 0px;
	font-size: 18px;
}

.bt {
	display: inline-block;
	padding: 6px 12px;
	margin-bottom: 0;
	font-size: 14px;
	font-weight: 400;
	line-height: 1.42857143;
	text-align: center;
	white-space: nowrap;
	vertical-align: middle;
	-ms-touch-action: manipulation;
	touch-action: manipulation;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
	background-image: none;
	border: 1px solid transparent;
	border-radius: 4px;
}

.menu_btn {
	margin: 8px 4px;
	margin-left: 0px;
	border: 1px #ababab solid;
	padding: 7px 4px;
	border-radius: 3px;
	background-color: rgb(255, 255, 255);
}

table {
	width: 100%;
	margin: auto;
}

table td {
	width: 390px;
}

table caption {
	background: #e2e2e2;
	padding-top: 8px;
	padding-bottom: 8px;
	color: #171313;
	text-align: left;
}

#order_manager button {
	border: 2px #ababab solid;
	font-weight: bold
}

.but_color {
	background: #44a823;
	width: 100px;
	height: 30px;
	border: 1px #aaa solid;
	color: #fff;
	float: right;
	margin: 30px 10px 0px 0px;
	font-size: 18px;
}
td > a {
	text-decoration: underline;
	font-size: 14px;
	cursor: default;
	color: black;
}
/* #marketing_mng{display: table;width: 50%;float: left;} */
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<script>
//获取url中参数
function getUrlParam(url,name) {
    var reg = new RegExp("(^|[&?])" + name + "=([^&]*)(&|$)");
    var r = url.match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}
//内网外网网址 ?innerNetUrl=192.168.1.27:10013&outerNetUrl=27.115.38.42:10013
function innerOuterNetUrl(btnUrl) {
    //说明 数据库中存储时候默认存储内网的跳转地址，后面代码中检测到是外网的话 会用outerNetUrl替换innerNetUrl
    if (btnUrl.indexOf("innerNetUrl=") != -1 && btnUrl.indexOf("outerNetUrl=") != -1) {
        innerNetUrl = getUrlParam(btnUrl,"innerNetUrl"); //内网时跳转地址
        outerNetUrl = getUrlParam(btnUrl,"outerNetUrl"); //外网时跳转地址
        btnUrl = btnUrl.replace("&innerNetUrl=" + innerNetUrl,"").replace("innerNetUrl=" + innerNetUrl,"");
        btnUrl = btnUrl.replace("&outerNetUrl=" + outerNetUrl,"").replace("outerNetUrl=" + outerNetUrl,"");
        if (btnUrl.indexOf("?") + 1 == btnUrl.length) {
            btnUrl = btnUrl.replace("?","");
        }
        if(location.hostname != 'localhost' && !(location.hostname.indexOf('192.168.') != -1)){
            btnUrl = btnUrl.replace(innerNetUrl, outerNetUrl);//外网访问的，使用外网地址
        }
    }
    return btnUrl;
}
	$(function() {
		hideTable();
		var json =
<%=authJson%>
	;

		if (json.length > 0) {
			var temporarily_unuse = {
				"count" : 0,
				"content" : "<tr>"
			};
			var other_utils = {
				"count" : 0,
				"content" : "<tr>"
			};
			var order_manager = {
				"count" : 0,
				"content" : "<tr>"
			};
			var customer_info_mng = {
				"count" : 0,
				"content" : "<tr>"
			};
			var procure_supplier_mng = {
					"count" : 0,
					"content" : "<tr>"
				};
			var warehouse_mng = {
				"count" : 0,
				"content" : "<tr>"
			};
			var financial_mng = {
				"count" : 0,
				"content" : "<tr>"
			};
			var marketing_mng = {
				"count" : 0,
				"content" : "<tr>"
			};
			var configuration_module = {
					"count" : 0,
					"content" : "<tr>"
				};
			var product_manager = {
				"count" : 0,
				"content" : "<tr>"
			};
			
			var regMark = /^\d{4}-\d{1,2}-\d{1,2}$/;
			var regExpMark = new RegExp(regMark);
			for (var i = 0; i < json.length; i++) {
				//时间判断 最近2天的显示红色
				var reMark = json[i].reMark;
				var reMarkStr='';
				if(reMark != '' && reMark != undefined && reMark != null){
					var reMarkArr = reMark.split("/");
					if(reMarkArr != null && reMarkArr.length == 2){
						if(regExpMark.test(reMarkArr[1])){
							//是日期格式
							if(parseInt(new Date() - new Date(reMarkArr[1] + ' 00:00')) / 1000 / 3600 / 24 <= 3){
								//3天内的日期 红色显示
								reMarkStr = "<br /><span style=\"color: #CC0033;font-weight: normal;\">" + reMark + "</span>";
							}
						}
					}
					if(reMarkStr == ''){
						//存储格式问题不显示红色
						reMarkStr = "<br /><span style=\"color: #8B8378;font-weight: normal;\">" + reMark + "</span>";
					}
				}
				//以按钮或者按标签方式显示 urlFlag=a
				var btnUrl = json[i].url;
				var btnAFlag = false;
				if (btnUrl.indexOf("urlFlag=a") != -1) {
					btnUrl = btnUrl.replace("&urlFlag=a","").replace("urlFlag=a","");
					if (btnUrl.indexOf("?") + 1 == btnUrl.length) {
						btnUrl = btnUrl.replace("?","");
					}
					btnAFlag = true;
				}
				//背景颜色 colorFlag=ccff9a
				var btnColor = '';
				if (btnUrl.indexOf("colorFlag=") != -1) {
					btnColor = getUrlParam(btnUrl,"colorFlag");
					btnUrl = btnUrl.replace("&colorFlag=" + btnColor,"").replace("colorFlag=" + btnColor,"");
					if (btnUrl.indexOf("?") + 1 == btnUrl.length) {
						btnUrl = btnUrl.replace("?","");
					}
					btnColor = "style=\"background-color: #" + btnColor + ";\""
				}
				//内网地址 外网地址
                btnUrl = innerOuterNetUrl(btnUrl);
				var content = '';
				if (btnUrl.substring(0, 4) == "http" || btnUrl.substring(0, 3) == "ftp") {
					content += '<td><a ' + btnColor + ' href="'+btnUrl+'" target="_blank" >';
					if (!btnAFlag) {
						content += '<button class="menu_btn" type="button">' + json[i].authName + reMarkStr + '</button>';
					} else {
						content += json[i].authName + reMarkStr
					}
					content += '</a></td>';
				} else {
					content += 'id="menu' + i + '" onclick="btnClick(\'' + btnUrl + '\')">' + json[i].authName + reMarkStr;
					if (!btnAFlag) {
						content = '<td><button ' + btnColor + ' class="menu_btn" type="button" ' + content + '</button></td>';
					} else {
						content += '<td><a ' + btnColor + ' href="#" ' + content + '</a></td>';
					}
				}

				if (json[i].moduleType == -1) {

					if (temporarily_unuse["count"] != 0
							&& temporarily_unuse["count"] % 5 == 0) {
						temporarily_unuse["content"] += '</tr><br><tr>'
								+ content;
					} else {
						temporarily_unuse["content"] += content;
					}
					temporarily_unuse["count"] += 1;
				} else if (json[i].moduleType == 0) {

					if (other_utils["count"] != 0
							&& other_utils["count"] % 5 == 0) {
						other_utils["content"] += '</tr><br><tr>' + content;
					} else {
						other_utils["content"] += content;
					}
					other_utils["count"] += 1;
				} else if (json[i].moduleType == 1) {

					if (order_manager["count"] != 0
							&& order_manager["count"] % 5 == 0) {
						order_manager["content"] += '</tr><br><tr>' + content;
					} else {
						order_manager["content"] += content;
					}
					order_manager["count"] += 1;
				} else if (json[i].moduleType == 2) {

					if (customer_info_mng["count"] != 0
							&& customer_info_mng["count"] % 5 == 0) {
						customer_info_mng["content"] += '</tr><br><tr>'
								+ content;
					} else {
						customer_info_mng["content"] += content;
					}
					customer_info_mng["count"] += 1;
				} else if (json[i].moduleType == 3) {

					if (procure_supplier_mng["count"] != 0
							&& procure_supplier_mng["count"] % 5 == 0) {
						procure_supplier_mng["content"] += '</tr><br><tr>' + content;
					} else {
						procure_supplier_mng["content"] += content;
					}
					procure_supplier_mng["count"] += 1;
				} else if (json[i].moduleType == 4) {
					//每行只放指定部分 这里根据orderNo进行分布
					/* if (warehouse_mng["count"] != 0
							&& warehouse_mng["count"] % 5 == 0) {
						warehouse_mng["content"] += '</tr><br><tr>' + content;
					} else {
						warehouse_mng["content"] += content;
					}
					warehouse_mng["count"] += 1; */
					if (json[i].orderNo % 5 == 1) {
						warehouse_mng["content"] += '</tr><br><tr>' + content;
					} else {
						warehouse_mng["content"] += content;
					}
					warehouse_mng["count"] += 1;
				} else if (json[i].moduleType == 5) {

					if (financial_mng["count"] != 0
							&& financial_mng["count"] % 5 == 0) {
						financial_mng["content"] += '</tr><br><tr>' + content;
					} else {
						financial_mng["content"] += content;
					}
					financial_mng["count"] += 1;
				} else if (json[i].moduleType == 6) {

					if (marketing_mng["count"] != 0
							&& marketing_mng["count"] % 5 == 0) {
						marketing_mng["content"] += '</tr><br><tr>' + content;
					} else {
						marketing_mng["content"] += content;
					}
					marketing_mng["count"] += 1;
				} else if (json[i].moduleType == 7) {

					if (product_manager["count"] != 0
							&& product_manager["count"] % 5 == 0) {
						product_manager["content"] += '</tr><br><tr>'
								+ content;
					} else {
						product_manager["content"] += content;
					}
					product_manager["count"] += 1;
				} else if (json[i].moduleType == 8) {

					if (configuration_module["count"] != 0
							&& configuration_module["count"] % 5 == 0) {
						configuration_module["content"] += '</tr><br><tr>'
								+ content;
					} else {
						configuration_module["content"] += content;
					}
					configuration_module["count"] += 1;
				}
			}

			if (temporarily_unuse["count"] > 0) {
				var temRemain = 5 - temporarily_unuse["count"] % 5;
				if (temRemain != 0 && temRemain != 5) {
					for (var k = 0; k < temRemain; k++) {
						temporarily_unuse["content"] += "<td></td>"
					}
				}
				$("#temporarily_unuse tbody").append(
						temporarily_unuse["content"] + '</tr>');
				$("#temporarily_unuse").show();
			}
			if (other_utils["count"] > 0) {
				var claRemain = 5 - other_utils["count"] % 5;
				if (claRemain != 0 && claRemain != 5) {
					for (var k = 0; k < claRemain; k++) {
						other_utils["content"] += "<td></td>"
					}
				}
				$("#other_utils tbody")
						.append(other_utils["content"] + '</tr>');
				$("#other_utils").show();
			}
			if (order_manager["count"] > 0) {
				var comRemain = 5 - order_manager["count"] % 5;
				if (comRemain != 0 && comRemain != 5) {
					for (var k = 0; k < comRemain; k++) {
						order_manager["content"] += "<td></td>"
					}
				}
				$("#order_manager tbody").append(
						order_manager["content"] + '</tr>');
				$("#order_manager").show();
			}
			if (customer_info_mng["count"] > 0) {
				var cusRemain = 5 - customer_info_mng["count"] % 5;
				if (cusRemain != 0 && cusRemain != 5) {
					for (var k = 0; k < cusRemain; k++) {
						customer_info_mng["content"] += "<td></td>"
					}
				}
				$("#customer_info_mng tbody").append(
						customer_info_mng["content"] + '</tr>');
				$("#customer_info_mng").show();
			}
			if (warehouse_mng["count"] > 0) {
				var warRemain = 5 - warehouse_mng["count"] % 5;
				if (warRemain != 0 && warRemain != 5) {
					for (var k = 0; k < warRemain; k++) {
						warehouse_mng["content"] += "<td></td>"
					}
				}
				$("#warehouse_mng tbody").append(
						warehouse_mng["content"] + '</tr>');
				$("#warehouse_mng").show();
			}

			if (financial_mng["count"] > 0) {
				var finRemain = 5 - financial_mng["count"] % 5;
				if (finRemain != 0 && finRemain != 5) {
					for (var k = 0; k < finRemain; k++) {
						financial_mng["content"] += "<td></td>"
					}
				}
				$("#financial_mng tbody").append(
						financial_mng["content"] + '</tr>');
				$("#financial_mng").show();
			}
			if (marketing_mng["count"] > 0) {
				var staRemain = 5 - marketing_mng["count"] % 5;
				if (staRemain != 0 && staRemain != 5) {
					for (var k = 0; k < staRemain; k++) {
						marketing_mng["content"] += "<td></td>"
					}
				}
				$("#marketing_mng tbody").append(
						marketing_mng["content"] + '</tr>');
				$("#marketing_mng").show();
			}
			if (configuration_module["count"] > 0) {
				var conRemain = 5 - configuration_module["count"] % 5;
				if (conRemain != 0 && conRemain != 5) {
					for (var k = 0; k < conRemain; k++) {
						configuration_module["content"] += "<td></td>"
					}
				}
				$("#configuration_module tbody").append(
						configuration_module["content"] + '</tr>');
				$("#configuration_module").show();
			}
			if (procure_supplier_mng["count"] > 0) {
				var proSupNum = 5 - procure_supplier_mng["count"] % 5;
				if (proSupNum != 0 && proSupNum != 5) {
					for (var k = 0; k < proSupNum; k++) {
						procure_supplier_mng["content"] += "<td></td>"
					}
				}
				$("#procure_supplier_mng tbody").append(
						procure_supplier_mng["content"] + '</tr>');
				$("#procure_supplier_mng").show();
			}
			if (product_manager["count"] > 0) {
				var proMngNum = 5 - product_manager["count"] % 5;
				if (proMngNum != 0 && proMngNum != 5) {
					for (var k = 0; k < proMngNum; k++) {
						product_manager["content"] += "<td></td>"
					}
				}
				$("#product_manager tbody").append(
						product_manager["content"] + '</tr>');
				$("#product_manager").show();
			}
		}
	});

	function hideTable() {
		$("#temporarily_unuse").hide();
		$("#other_utils").hide();
		$("#order_manager").hide();
		$("#customer_info_mng").hide();
		$("#warehouse_mng").hide();
		$("#financial_mng").hide();
		$("#marketing_mng").hide();
		$("#configuration_module").hide();
		$("#procure_supplier_mng").hide();
		$("#product_manager").hide();
	}

	function btnClick(url) {

		$
				.ajax({
					type : 'POST',
					url : '/cbtconsole/userLogin/openMainMenuUrl.do',
					data : {
						'url' : url
					},
					success : function(data) {
						if (data.ok) {
							var urlStr = url;
							if (urlStr.substring(0, 1) == "/") {
								urlStr = urlStr.substring(1);
							}
							window.open("/cbtconsole/" + urlStr);
						} else {
							//alert(data.message);
							//window.location.href="/cbtconsole/website/main_login.jsp";
							$.messager
									.alert(
											"提醒",
											data.message,
											"error",
											function() {
												window.location.href = "/cbtconsole/website/main_login.jsp";
											});
						}
					},
					error : function(res) {
						$.messager
								.alert(
										"提醒",
										"查询错误",
										"error",
										function() {
											window.location.href = "/cbtconsole/website/main_login.jsp";
										});

					}
				});

	}

	function loginOut() {
		window.location.href = "/cbtconsole/userLogin/loginOut.do";
	}
	function resetPwd(admName) {
        window.open("/cbtconsole/website/reset_pwd.html?admName=" + admName,
            "windows",
            "height=160px,width=400px,top=120px,left=200px,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
	}
</script>
</head>
<body>

	<div class="usetablediv">
		<table id="order_manager" border="0">
			<caption>订单核心流程</caption>
			<tbody></tbody>
		</table>
		<!-- <br> -->
		<table id="customer_info_mng" border="0">
			<caption>各种客户留言</caption>
			<tbody></tbody>
		</table>
		<!-- <br> -->
		<table id="procure_supplier_mng" border="0">
			<caption>采购&供应商管理</caption>
			<tbody></tbody>
		</table>
		<!-- <br> -->
		<table id="warehouse_mng" border="0">
			<caption>仓库管理</caption>
			<tbody></tbody>
		</table>

		<!-- <br> -->
		<table id="financial_mng" border="0">
			<caption>财务管理</caption>
			<tbody></tbody>
		</table>

		<table id="marketing_mng" border="0">
			<caption>营销&SEO管理</caption>
			<tbody></tbody>
		</table>


		<!-- <br> -->
		<table id="product_manager" border="0">
			<caption>产品管理</caption>
			<tbody></tbody>
		</table>

		<!-- <br> -->
		<table id="configuration_module" border="0">
			<caption>搜索配置模块</caption>
			<tbody></tbody>
		</table>
		<!-- <br> -->
		<table id="other_utils" border="0">
			<caption>【其他系统小工具】</caption>
			<tbody></tbody>
		</table>
		<!-- <br> -->
		<table id="temporarily_unuse" border="0">
			<caption>暂不用区</caption>
			<tbody></tbody>
		</table>

	</div>

    <div class="usetablediv" style="border: none;">
        <button class="but_color" onclick="loginOut()" style="margin-right: 100px">退出</button>
        <button class="but_color" onclick="resetPwd('<%=admName%>')">密码修改</button>
        <span style="float: right;margin: 36px 10px 0 0;">当前登录用户:&nbsp;<%=admName%></span>
    </div>

	<%-- <%  if(!(user.getId() ==1 || user.getAdmName().equalsIgnoreCase("Ling"))){%>
	<!-- 载入消息提醒jsp页面 -->
	<jsp:include page="message_notification.jsp"></jsp:include>
<%}  %> --%>
<!-- 后台更新记录链接 -->
<div style="position: absolute;top: 50px;right: 20px;font-size: 14px;" id="update_file">
	<!-- <a target="_blank" href="ftp://192.168.1.27/updateinfo">后台更新记录(账号&密码均为：share)</a><br />
	<span style="color: red;padding-left:150px;">2018-10-11</span> -->
</div>
<script type="text/javascript">
	$.ajax({
		type: "GET",
		url: "/cbtconsole/queryuser/queryAuthInfo.do?authId=1103",
		dataType:"json",
		success: function(msg){
			if (msg != undefined) {
				$("#update_file").append("<a style=\"color: black;\" target=\"_blank\" href=\"" + innerOuterNetUrl(msg.url) + "\">" + msg.authName + "</a><br />");
				var regMark = /^\d{4}-\d{1,2}-\d{1,2}$/;
				var regExpMark = new RegExp(regMark);
				var reMarkStr = '';
				if(regExpMark.test(msg.reMark)){
					//是日期格式
					if(parseInt(new Date() - new Date(msg.reMark + ' 00:00')) / 1000 / 3600 / 24 <= 3){
						//3天内的日期 红色显示
						reMarkStr = "<span style=\"color: red;padding-left:150px;\">" + msg.reMark + "</span>";
					}
				}
				if(reMarkStr == ''){
					//存储格式问题不显示红色
					reMarkStr = "<span style=\"padding-left:150px;\">" + msg.reMark + "</span>";
				}
				$("#update_file").append(reMarkStr);
			}
		}
	});
	$.ajax({
		type: "GET",
		url: "/cbtconsole/messages/findCustomerMessages",
		dataType:"json",
		success: function(msg){
			if (msg != undefined && msg.length > 0) {
				$(msg).each(function(index,item){
					var $obj = $($("button:contains('" + item.authName + "')").get(0));
					$obj.html($obj.html() + "<span style=\"color:red;\">(" + item.count + "条)</span>");
				});
			}
		}
	});
</script>

</body>
</html>