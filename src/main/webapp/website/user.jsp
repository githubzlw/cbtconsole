<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="com.cbt.util.*"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	String admuserJson = Redis.hget(request.getSession().getId(),
			"admuser");
	Admuser adm = (Admuser) SerializeUtil.JsonToObj(admuserJson,
			Admuser.class);
	int role = Integer.parseInt(adm.getRoletype());
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>用户信息管理</title>
<style type="text/css">
.but_color {
	background: #44a823;
	width: 80px;
	height: 24px;
	border: 1px #aaa solid;
	color: #fff;
}

.panel-title {
	text-align: center;
	height: 30px;
	font-size: 24px;
}
tr .td_class{width:230px;}
.td_class lable{
	float:left;
	width:120px;
}
.w_input input{width:200px;}
</style>
<script type="text/javascript">
	var str = '';
	var userid;
	var available;
	var roleType =
<%=role%>
	;
	$(function() {
		setDatagrid();
		$.post("/cbtconsole/ConfirmUserServlet",{"action" : "sell"},
						function(res) {
							var json = eval(res);
							str += '<option value="0">未分配</option>';
							for (var i = 0; i < json.length; i++) {
								if (json[i].role == '0' || json[i].role == '1') {
									str += '<option value="'+json[i].id+'" flag="'+json[i].confirmusername+'">'
											+ json[i].confirmusername
											+ '</option>';
								}
							}
							var str1 = '<option value="0">全部</option>';
							for (var i = 0; i < json.length; i++) {
								if (json[i].role == '0' || json[i].role == '1') {
									str1 += '<option value="'+json[i].id+'" flag="'+json[i].confirmusername+'">'
											+ json[i].confirmusername
											+ '</option>';
								}
							}
							$('select[id=admName]').append(str1);
						});
		$.post("/cbtconsole/grade/get", {}, function(res) {
			var json = eval(res);
			var grd = '<option value="0">全部</option>';
			for (var i = 0; i < json.length; i++) {
				grd += '<option value="'+json[i].gid+'">' + json[i].grade
						+ '</option>';
			}
			$('select[id=vip]').append(grd);
		});
		var opts = $("#easyui-datagrid").datagrid("options");
		opts.url = "/cbtconsole/warehouse/getUserInfo.do";
	})

	function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '用户信息管理',
			//iconCls : 'icon-ok',
			width : "100%",
			height : "100%",
			fit : true,//自动补全 
			autoRowWidth:false,
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [ 20 ],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
			// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '',//url调用Action方法
			loadMsg : '数据装载中......',
			singleSelect : true,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
			//sortName : 'xh',//当数据表格初始化时以哪一列来排序
			//sortOrder : 'desc',//定义排序顺序，可以是'asc'或者'desc'（正序或者倒序）。
			pagination : true,//分页
			rownumbers : true
		//行数
		});
	}

	function changeavailable(userid, available) {
		if (userid == 0 || userid == "") {
			$.messager.alert('提示', '获取用户id失败,请重试');
		} else {
			window
					.open(
							"/cbtconsole/website/change_available.jsp?userid="
									+ userid + "&available=" + available,
							"windows",
							"height=400,width=800,top=500,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
		}
	}

	function addUser(uid, useremail, username, admname) {
		var adminid = $("#admuser_" + uid + "").val();
        $.ajax({
            url: "/cbtconsole/order/addUser",
            type:"POST",
            data : {"adminid":adminid,"userid":uid,"admName":admname,"userName":username,"email":useremail,"orderNo":''},
            dataType:"json",
            success:function(data){
                if (data == 1) {
                    topCenter("更改用户负责人成功");
                    $('#easyui-datagrid').datagrid('reload');
                }else{
                    topCenter("更改用户负责人失败");
                }
            }
        });
	}

    function topCenter(msg){
        $.messager.show({
            title:'消息',
            msg:msg,
            showType:'slide',
            style:{
                right:'',
                top:document.body.scrollTop+document.documentElement.scrollTop,
                bottom:''
            }
        });
    }

	function useraddress(userid, name, currency) {
		window.open(
				"http://www.import-express.com/simulateLogin/toDeliverAddress?userName="
						+ userid + "&password=" + name.replace(/\s+/g,'') + "&currency="
						+ currency, "_blank");
	}
	function userlogin(userid, name, currency) {
		window.open(
				"http://www.import-express.com/simulateLogin/login?userName="
						+ userid + "&password=" + name.replace(/\s+/g,'') + "&currency="
						+ currency, "_blank");
	}
	function toShopCar(userid, name, currency) {
		window.open(
				"http://www.import-express.com/simulateLogin/toShopCar?userName="
						+ userid + "&password=" + name.replace(/\s+/g,'') + "&currency="
						+ currency, "_blank");
	}
	function upEmail(uid, email) {
		 $.messager.prompt('变更用户邮箱','请输入用户新邮箱',function(newemail){
					  if(newemail==null || newemail=="" || newemail==email){
						 $.messager.alert('提示','新邮箱不能为空或已存在');
						 return;
					  }
					  $.post("/cbtconsole/userinfo/upEmail", {
							oldemail:email,email:newemail,userid:uid
						}, function(res) {
							if(res == 1){
								$.messager.alert('提示','修改成功');
							}else if(res == 3){
								$.messager.alert('提示','请登录');
							}else{
								$.messager.alert('提示','修改失败');
							}
							
						});
			});
	}


	function fnsetDropshipUser() {
		$('#but2').prop('disabled', true);
		userid = $("#userid").val();
		var email = $("#email").val();
		$.ajax({
			type : 'post',
			url : '/cbtconsole/DropshipServlet',
			data : {
				'userid' : userid,
				'email' : email
			},
			dataType : 'json',
			success : function(res) {
				alert(res.message);
				$('#but2').prop('disabled', false);
			},
			error : function() {
				$.messager.alert('提示', '请求失败！');
				$('#but2').prop('disabled', false);
			}
		});
	}

	function fnsetAPIkey() {
		$('#but3').prop('disabled', true);
		userid = $("#userid").val();
		var email = $("#email").val();
		$.ajax({
			type : 'post',
			url : '/cbtconsole/APIServlet',
			data : {
				'userid' : userid,
				'email' : email
			},
			dataType : 'json',
			success : function(res) {
				alert(res.message);
				$('#but3').prop('disabled', false);
			},
			error : function() {
				$.messager.alert('提示', '申请失败！');
				$('#but3').prop('disabled', false);
			}
		});
	}

	function doQuery(page) {
		var userid = $("#userid").val();
		var conutry = $("#conutry").val();
		var admUser = $("#admName").val();
		var vip = $("#vip").val();
		var name = $("#name").val();
		var email = $("#email").val();
		var paymentid = $("#paymentid").val();
		var startdate = $("#startdate").val();
		var enddate = $("#enddate").val();
       var cyState= $("#cyState").is(':checked');
       var address=$("#address").val();
       if(cyState==false){
           cyState="";
	   }
        var wjState= $("#wjState").is(':checked');
        if(wjState==false){
            wjState="";
        }
        var shState= $("#shState").is(':checked');
        if(shState==false){
            shState="";
        }
        var fkState= $("#fkState").is(':checked');
        if(fkState==false){
            fkState="";
        }
        var cgState= $("#cgState").is(':checked');
        if(cgState==false){
            cgState="";
        }
        var dkState= $("#dkState").is(':checked');
        if(dkState==false){
            dkState="";
        }
		var state=$("#state").val();
		var paymentemail = $("#paymentemail").val();
		$("#easyui-datagrid").datagrid("load", {
			"userid" : userid,
			"name" : name,
			"cyState":cyState,
			"wjState":wjState,
			"shState":shState,
			"fkState":fkState,
			"cgState":cgState,
			"dkState":dkState,
			"address":address,
			"email" : email,
			"paymentid" : paymentid,
			"paymentemail" : paymentemail,
			"admUser" : admUser,
			"conutry" : conutry,
			"vip" : vip,
			"stateDate" : startdate,
			"endDate" : enddate,
			"state":state,
			"page" : page
		});
	}

	function doReset() {
		$("#userid").val("");
		$("#name").val("");
		$("#email").val("");
		$("#paymentid").val("");
		$("#enddate").val("");
		$("#startdate").val("");
		$("#conutry").val("");
		$("#paymentemail").val("");
		$("#vip").val("0");
		$("#admName").val("0");
		$("#state").val("");
	}
	
	function upPhone(uid, phone) {
		$('#win').window('open');
         $('#ff').form('load',{
				old_phone:phone,
				old_userid:uid,
			});
	}

	function fnUp(){
		var phone = $("#old_phone").val();
		var userid = $("#up_userid").val();
		var newphone = $("#new_phone").val();
		if(newphone == ""){
			$.messager.alert('提示', '请输入新电话');
			return;
		}
		if(userid == "" || userid == "0" ){alert("用户ID为空");return;}
		if(phone == newphone){
			$.messager.alert('提示', '用户新旧电话一致');
			return;
		}
		$.post("/cbtconsole/userinfo/upPhone", {
			oldPhone:phone,newPhone:newphone,userid:userid
		}, function(res) {
			if(res == 1){
				$.messager.alert('提示', '修改成功');
			}else if(res == 3){
				$.messager.alert('提示', '请登录');
			}else{
				$.messager.alert('提示', '修改失败');
			}
			
		});
	}
	function formatterUserid(value, row, index) {
        if(row.userid != null) {
            return '<a href="/cbtconsole/userinfo/getUserInfo.do?userId=' + row.userid + '" target=black>' + row.userid + ' </a>';
        }
        return;
    }
</script>
</head>
<body>
	<div id="win" class="easyui-window" title="变更用户电话号码" data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"  style="width:400px;height:200px;display: none;">
		 <form id="ff" method="post">
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="old_phone" id="old_phone" style="width:75%;" readonly="readonly" data-options="label:'现用电话:'">
				<input class="easyui-textbox" name="old_userid" id="old_userid" style="display:none">
			</div>
			<div style="margin-bottom:20px">
				<input class="easyui-textbox" name="new_phone" id="new_phone"  style="width:75%" data-options="label:'变更电话:',required:true,validType:'phone'">
			</div>
		</form>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="fnUp()" style="width:80px">确认修改</a>
		</div>
	</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<div>
			<table style="margin:auto;">
				<tr>
					<td class="td_class"><lable>用户ID：</lable><div class="w_input"><input id="userid" type="text"
						value="${param.userid}" onblur="this.value=this.value.trim();" 
						onkeypress="this.value=this.value.trim();if (event.keyCode == 13) doQuery(1)" /></div></td>
					<td class="td_class"><lable>注册邮箱：</lable><div class="w_input"><input id="email" type="text"
						value="" onblur="this.value=this.value.trim();" onkeypress="this.value=this.value.trim();if (event.keyCode == 13) doQuery(1)" /></div></td>
<!-- 					<td class="td_class">交易号：<input id="paymentid" type="text" -->
<!-- 						value="" onkeypress="if (event.keyCode == 13) doQuery(1)" /></td> -->
					<td class="td_class"><lable>PayPal邮箱：</lable><div class="w_input"><input id="paymentemail"
						type="text" value="" onblur="this.value=this.value.trim();" 
						onkeypress="this.value=this.value.trim();if (event.keyCode == 13) doQuery(1)" /></div></td>
					<td class="td_class"><lable>business name：</lable><div class="w_input"><input id="name"
						type="text" value=""
						onkeypress="if (event.keyCode == 13) doQuery(1)" /></div></td>
					<td class="td_class"><lable>国家：</lable><div class="w_input"><input id="conutry" type="text"
						value="" onkeypress="if (event.keyCode == 13) doQuery(1)" /></div></td>
					<td class="td_class"><lable>state：</lable><div class="w_input"><input id="state" type="text"
						value="" onkeypress="if (event.keyCode == 13) doQuery(1)" /></div></td>
					<td class="td_class"><lable>address：</lable><div class="w_input"><input id="address" type="text"
						value="" onkeypress="if (event.keyCode == 13) doQuery(1)" /></div></td>
				</tr>
				<tr>
					<td class="td_class" colspan="3"><lable>开始时间：</lable><div class="w_input"> <input id="startdate"
						name="startdate" readonly="readonly"
						onfocus="WdatePicker({isShowWeek:true})"
						value="${param.startdate}"
						onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}' />
						<input id="enddate" name="enddate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})"
						value="${param.startdate}"
						onkeydown='if(event.keyCode==13){fnInquiry(1,"${param.type}");}'  style="margin-laft:5px;"/></div></td>
					<td class="td_class"><lable>vip：</lable><div class="w_input"><select style="width: 177px;"
						id="vip"></select></div></td>
					<td class="td_class"><lable>负责人：</lable><div class="w_input"><select style="width: 177px;"
						id="admName"></select></div></td>
					<td>
						<input type="checkbox" name="cyState" id="cyState" value="3" ></input>出运中
						<input type="checkbox" id="wjState" name="wjState" value="4"></input>完结
						<input type="checkbox" id="shState" name="wjState" value="5"></input>订单审核
						<input type="checkbox" id="fkState" name="wjState" value="0"></input>未付款
						<input type="checkbox" id="cgState" name="wjState" value="1"></input>采购中
						<input type="checkbox" id="dkState" name="wjState" value="2"></input>到库
					</td>
				</tr>
			</table>
			<div style="    width: 165px;margin: auto;"><input class="but_color" type="button"
						id="query" value="查询" onclick="doQuery(1)">
					<input class="but_color" type="button"
						value="重置" onclick="doReset()"></div>
				
		</div>
	</div>

	<table class="easyui-datagrid" id="easyui-datagrid"
		style="width: 1800px; height: 900px">
		<thead>
			<tr>
				<th data-options="field:'userid',width:80,align:'center',formatter:formatterUserid">用户ID</th>
				<th data-options="field:'businessName',width:80,align:'center'">business name</th>
				<th data-options="field:'email',width:80,align:'center'">注册邮箱</th>
				<th data-options="field:'creattime',width:80,align:'center'">创建时间</th>
				<th data-options="field:'loginStyle',width:50,align:'center'">登录方式</th>
				<th data-options="field:'zone',width:60,align:'center'">国家</th>
				<th data-options="field:'statename',width:60,align:'center'">statename</th>
				<th data-options="field:'goodsPriceUrl',width:50,align:'center'">购物车总额</th>
				<th data-options="field:'available',width:25,align:'center'">余额</th>
				<th data-options="field:'userLogin',width:80,align:'center'">用户登陆</th>
				<th data-options="field:'userManager',width:80,align:'center'">用户管理</th>
				<th data-options="field:'grade',width:50">用户等级</th>
				<th data-options="field:'admuser',width:65,align:'center'">负责人</th>
				<th data-options="field:'currency',width:30,align:'center'">货币单位</th>
				<th data-options="field:'operation',width:195,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>