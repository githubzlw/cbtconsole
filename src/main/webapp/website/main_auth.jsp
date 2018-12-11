<%@page import="com.cbt.util.Cache"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@page import="java.util.List"%>
<%@ page import="java.awt.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>权限管理</title>
    <!-- 这是bootStrap所需要的css文件 -->
    <link href="/cbtconsole/css/bootstrap.min2.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <!-- 这是bootstrap所必要的jquery文件。 -->
    <script src="/cbtconsole/js/jquery-2.1.0.min.js"></script>
    <!-- 这是bootstrap的js文件 -->
    <script src="/cbtconsole/js/bootstrap/bootstrap.min2.js"></script>
    <!-- 颜色插件 -->
    <link href="/cbtconsole/css/bootstrap-colorpicker.css" rel="stylesheet" />
    <script src="/cbtconsole/js/bootstrap/bootstrap-colorpicker.js"></script>

    <!--easyui-->
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<style type="text/css">
.usebtn{margin:5px 0;}
.usecon{width:700px;margin:0 auto;}
</style>
<script type="text/javascript">
    //获取url中参数
    function getUrlParam(url,name) {
        var reg = new RegExp("(^|[&?])" + name + "=([^&]*)(&|$)");
        var r = url.match(reg); //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    }
	var userName;
	function fn() {
        $.ajax({
            type: "POST",//方法类型
            url:'/cbtconsole/userinfo/findAllUser',
            data:{
            },
            dataType:"json",
            success:function(result) {
                if(result != null && result.length>0){
                    for ( var x in result) {
                        $('#user').append("<button class='usebtn' type='button' value='" + result[x] + "' onclick='btnClick(this.value)'>" + result[x] + "</button><br/>");
                    }
                }
            }
        });
	}

	function btnClick(admName) {
		userName = admName;
		var obj = document.getElementsByName("auth");
		for ( var k in obj) {
			obj[k].checked = false;
		}
		$.post("/cbtconsole/AdmUserServlet?action=findAdmuserAuth", {
			'admName' : admName
		}, function(result) {
			for ( var x in result) {
				var auid = "checkbox"+result[x];
				document.getElementById(auid).checked=true;
			}
		});
	}
	
	function updateAuth() {
		if (userName == undefined || userName == "" || userName == null) {
			alert("请选择需要分配权限的用户");
			return;
		}
		var obj = document.getElementsByName("auth");
		var checkAuth = [];
		for ( var k in obj) {
			if (obj[k].checked) {
				checkAuth.push(obj[k].value);
			}
		}
		if (checkAuth == undefined || checkAuth.length == 0 || checkAuth == null) {
			alert("请选择将为用户分配的权限");
			return;
		}
		var str = checkAuth.join("'");
		$.post("/cbtconsole/AdmUserServlet?action=update", {
			'authId' : str,
			'admName' : userName
		}, function(result) {
				alert(result.message);
				location.reload();
			});
	}
	/*后台入口调整 新增/修改 入口*/
	function createMenu() {
        $("#updataAuthInfo").find("input[type='reset']").click()
        $('#createMenu').modal('show');
    }
	function updateMenu() {
        var authId = $("#update_authId").val();
        if($.isEmptyObject(authId)){
            $.messager.alert('message', '未选择入口!');
            return;
        }
        $.ajax({
            type: "POST",
            url: "/cbtconsole/queryuser/queryAuthInfo.do?authId=" + authId,
            dataType:"json",
            success: function(msg){
                if(msg != undefined){
                    $("#menu_message").text('');
                    $("#updataAuthInfo").find("input[type='reset']").click();
                    $("#menu_authId").val(msg.authId);
                    $("#menu_authName").val(msg.authName);
                    $("#menu_reMark").val(msg.reMark);
                    $("#menu_moduleType").val(msg.moduleType);
                    //以按钮或者按标签方式显示 urlFlag=a
                    var btnUrl = msg.url;
                    if (btnUrl.indexOf("urlFlag=a") != -1) {
                        btnUrl = btnUrl.replace("&urlFlag=a","").replace("urlFlag=a","");
                        if (btnUrl.indexOf("?") + 1 == btnUrl.length) {
                            btnUrl = btnUrl.replace("?","");
                        }
                        $("input[name='urlFlag'][value='2']").prop('checked','checked')
                    }
                    //背景颜色 colorFlag=ccff9a
                    if (btnUrl.indexOf("colorFlag=") != -1) {
                        var btnColor = getUrlParam(btnUrl,"colorFlag");
                        btnUrl = btnUrl.replace("&colorFlag=" + btnColor,"").replace("colorFlag=" + btnColor,"");
                        if (btnUrl.indexOf("?") + 1 == btnUrl.length) {
                            btnUrl = btnUrl.replace("?","");
                        }
                        $('#colorFlag').val('#' + btnColor);
                        $('#colorFlag').change();
                        /*$('#colorFlag').colorpicker({
                            color: '#' + btnColor
                        });*/
                    }
                    $("#menu_url").val(btnUrl);

                    $('#createMenu').modal('show');
                }
            }
        });
    }

</script>
</head>
<body onload="fn()">
<!-- 模态框（Modal） 后台入口调整 新增 -->
<div class="modal fade" id="createMenu" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 600px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">新增/修改后台入口</h4>
            </div>
            <div class="modal-body">
                <form id="updataAuthInfo" method="post">
                    <table class="table">
                        <input type="hidden" id="menu_authId" name="authId">
                        <tr>
                            <td>入口名称(必须)</td>
                            <td>
                                <input type="text" id="menu_authName" name="authName">
                                <span style="color: red;" id="menu_message"></span>
                            </td>
                        </tr>
                        <tr>
                            <td>入口链接(必须)</td>
                            <td><input type="text" id="menu_url" name="url"></td>
                        </tr>
                        <tr>
                            <td>修改时间(非必须)</td>
                            <td>
                                <input type="text" id="menu_reMark" name="reMark">
                                <span style="color: #CCC;">格式:adm/2018-12-06</span>
                            </td>
                        </tr>
                        <tr>
                            <td>模块(必须)</td>
                            <td>
                                <select class="form-control" id="menu_moduleType" name="moduleType">
                                    <option value="1">订单核心流程</option>
                                    <option value="2">各种客户留言</option>
                                    <option value="3">采购&供应商管理</option>
                                    <option value="4">仓库管理</option>
                                    <option value="5">财务管理</option>
                                    <option value="6">营销&SEO管理</option>
                                    <option value="7">产品管理</option>
                                    <option value="8">搜索配置模块</option>
                                    <option value="0">其他系统小工具</option>
                                    <option value="-1">暂不用区</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td>入口颜色(非必须)</td>
                            <td>
                                <div>
                                    <input id="colorFlag" name="colorFlag" type="text" class="form-control" value="" />
                                    <span style="position: absolute;right: 30px;top: 210px;color: #01a4ef;">&nbsp;X&nbsp;</span>
                                    <script type="text/javascript">
                                        <%--初始化颜色选择--%>
                                        $('#colorFlag').colorpicker({
                                            format:'hex'
                                        });
                                        $('#colorFlag').val('');
                                        $('#colorFlag').parent().find('span').click(function () {
                                            $('#colorFlag').val('');
                                        });
                                    </script>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td>入口显示方式(非必须)</td>
                            <td>
                                <input type="radio" value="1" name="urlFlag" checked="checked" />
                                <span>按钮</span>
                                <input type="radio" value="2" name="urlFlag" />
                                <span>链接</span>
                            </td>
                        </tr>
                        <%--<tr>
                            <td>入口权限</td>
                            <td></td>
                        </tr>--%>
                        <tr>
                            <td></td>
                            <td>
                                <input type="submit" value="提交">
                                <input type="reset" value="重置">
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <!-- <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button> -->
            </div>
        </div>
    </div>
</div>

	<div class="usecon">
        <h4>一、用户对应入口权限调整</h4>
		<form style="width: 300px; float: left;text-align:left">
			<fieldset style="height:600px;overflow: auto;">
				<legend>用户</legend>

				<span id="user"></span>
			</fieldset>
		</form>
		<form style="width: 300px; float: left;text-align:left;margin-left:20px;">
			<fieldset style="height:600px;overflow: auto;">
				<legend id="auth">权限</legend>
				<%
                Cache.clearCache();
				List<AuthInfo> authlist = Cache.getAllAuth();

				for(int i=0;i<authlist.size();i++){
				AuthInfo authinfo = (AuthInfo)authlist.get(i);
			%>
			<input type="checkbox" style="margin-top:10px;" name="auth" class="auth" id="checkbox<%=authinfo.getAuthId() %>" value="<%=authinfo.getAuthId() %>" /><%=authinfo.getAuthName() %><br/>
			<%	} %>
				
			</fieldset>
		</form>
		<div style="clear:both;padding-top:30px;margin-left:auto;margin-right:auto;width:100px;" ><button name="update" onclick="updateAuth()">commit</button></div>
	</div>
    <div class="usecon">
        <h4>二、后台入口调整</h4>
        <input type="button" onclick="createMenu()" value="新增"><br />
        <span style="float:left;">修改的入口:</span>
        <select class="form-control" id="update_authId" style="width: 260px;float: left;">
            <%
                for(int i=0;i<authlist.size();i++){
                    AuthInfo authinfo = (AuthInfo)authlist.get(i);
            %>
                <option value="<%=authinfo.getAuthId() %>"><%=authinfo.getAuthName() %></option>
            <%
                }
            %>
        </select>
        <input type="button" onclick="updateMenu()" value="修改">
    </div>
    <br /><br /><br /><br /><br />
</body>

<script type="text/javascript">
    $(function () {
        //更新/修改后台入口
        $("#updataAuthInfo").form({
            url:"/cbtconsole/queryuser/updateAuthInfo.do",
            onSubmit:function () {
                $("#menu_message").text('');
                var authName = $("#menu_authName").val();
                var url = $("#menu_url").val();
                if($.isEmptyObject(authName) || $.isEmptyObject(url)){
                    $("#menu_message").text('入口名称或入口链接未输入!');
                    return false; //必填数据未填写 终止表单
                }
                return true;
            },
            success:function (data) {
                var res = JSON.parse(data);
                if(res.status){
                    $('#createMenu').modal('hide');
                }
                $.messager.alert('message',res.message);
            }
        });
    });
</script>

</html>