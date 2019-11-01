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
	<script type="text/javascript"
			src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
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
#user_remark .remark_td1 {
    width: 450px;
}
#user_remark .remark_td2 {
    width: 200px;
}
#user_remark table,#user_remark table tr th, #user_remark table tr td {
    border:1px solid #CCC;
}
#user_remark table {
    width: 200px;
    min-height: 25px;
    line-height: 25px;
    text-align: center;
    border-collapse: collapse;
}
#user_remark tr {
    line-height: 24px;
}
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

		//参数中的userid填入用户ID
        var userid = getUrlParam('userid');
        if (!$.isEmptyObject(userid)){
            $("#userid").val(userid);
            doQuery(1);
        }

        $('#user_type input[name=user_type]').change(function () {
            var userid = $('#user_type input[name=userid]').val();
            var type = $("#user_type input[name=user_type]:checked").val();
            $.ajax({
                type: "POST",
                url: "/cbtconsole/queryuser/updateUserCheckout.do",
                data: {
                    userid:userid,
                    type:type
                },
                dataType:"json",
                success: function(msg){
                    if (msg.state == 'true') {
                        $('#user_type').window('close');
                        doQuery(1);
                    }
                }
            });
        });

	})

    // 获取url中参数
    function getUrlParam(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg); //匹配目标参数
        if (r != null) return unescape(r[2]); return null; //返回参数值
    }
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
		$.dialog({
			title : '模拟登陆暂停使用',
			content : "模拟登陆暂停使用",
			max : false,
			min : false,
			lock : true,
			drag : false,
			fixed : true,
			ok : function() {
				return;
			}/*,
			cancel : function() {
				return;
			}*/
		});
		/*window.open(
				"http://www.import-express.com/simulateLogin/toDeliverAddress?userName="
						+ userid + "&password=" + name.replace(/\s+/g,'') + "&currency="
						+ currency, "_blank");*/
	}
    function userloginJump() {
        var userid = $('#user_login_message input[name=userid]').val();
        var site = $("#user_login_message input[name=site]:checked").val();
        //获取数据 记录模拟登陆日志
        $.ajax({
            type:'post',
            url:'/cbtconsole/queryuser/insertLoginLog.do',
            data:{
                userid:userid,
                site:site
            },
            success:function(data){
                if(data.state == "true"){
                    if (data.bean.pass != undefined && data.bean.pass != '') {  // 有密码的账号 模拟post请求登陆
                        $("#user_login_from input[name=email]").val(data.bean.email);
                        $("#user_login_from input[name=pass]").val(data.bean.pass);
                        $("#user_login_from").attr("action", data.webSiteUrl + "/user/loginNew")
                        $("#user_login_from").submit();
                    } else if (data.encode != undefined) {  // 没有密码的第三方登陆 使用模拟登陆接口
                        window.open(data.webSiteUrl + "/simulateLogin/login?userName="
                            + data.encode + "&password=" + data.bean.name.replace(/\s+/g,'') + "&currency=" + data.bean.currency, "_blank");
                    }
                    $('#user_login_message').window('close');
                }else{
                    alert(data.message);
                }
            }
        });
    }
    function showUserType(userid, type) {
        $('#user_type input[name=user_type][value=' + type + ']').prop('checked', 'checked');
        $('#user_type input[name=userid]').val(userid);
        $('#user_type').window('open');
    }

    function openRecommendEmail(userId, site) {

		$.ajax({
			type:'post',
			url:'../userinfo/getUserAllInfoById',
			data:{
				userId:userId
			},
			success:function(data){
				if(data != null){
					if(data.ok){
						$("#send_web_site").val(site);
						$("#send_user_id").val(userId);
						var json = data.data;
						$("#send_user_email").val(json.email);
						$("#user_create_time").val(json.creattime);
						$("#user_buniess_info").val(json.businessinfo);
						if(json.productone && json.productone.length > 2){
							$("#user_goods_need").val(json.productone);
							$("#user_goods_require").val(json.requirementone);
						}else if(json.productone && json.producttwo.length > 2){
							$("#user_goods_need").val(json.producttwo);
							$("#user_goods_require").val(json.requirementtwo);
						}
						$("#sell_email").val(json.admuser);
						var jsonList = data.allData;
						if(jsonList && jsonList.length > 0){
							var content = '';
							for(var i = 0;i<jsonList.length;i++){
								content += '<tr>';
								content += '<td>时间:'+jsonList[i].createTime+'</td>';
								content += '<td>推送人:'+jsonList[i].adminName+'</td>';
								content += '<td><a href="'+jsonList[i].sendUrl+'">链接</a></td>';
								content += '</tr>';
							}
							$("#history_id").empty();
							$("#history_id").append(content);
							$("#history_table").show();
						}
						$("#send_recommend_id").window('open');
					}else{
						alert("获取信息失败");
					}
				}else{
					alert("网络错误");
				}
			}
		});
	}

	function sendRecommendEmail() {
		var userId = $("#send_user_id").val();
		var userEmail = $("#send_user_email").val();
		var createTime = $("#user_create_time").val();
		var buniessInfo = $("#user_buniess_info").val();
		var goodsNeed = $("#user_goods_need").val();
		var goodsRequire = $("#user_goods_require").val();
		var sendUrl = $("#send_url").val();
		var sellEmail = $("#sell_email").val();
		var webSite = $("#send_web_site").val();
		if(userId && userEmail && createTime && sendUrl && webSite){
			$("#notice_id").show();
			$.ajax({
				type:'post',
				url:'../userinfo/sendRecommendEmail',
				data:{
					userId:userId,
					userEmail:userEmail,
					createTime:createTime,
					buniessInfo:buniessInfo,
					goodsNeed:goodsNeed,
					sendUrl:sendUrl,
					sellEmail:sellEmail,
					goodsRequire:goodsRequire,
					webSite:webSite
				},
				success:function(data){
					$("#notice_id").hide();
					if(data.ok){
						$("#send_recommend_id").window('close');
					}else{
						alert("执行报错");
					}
				}
			});
		}else{
			alert("请填写必要的信息");
			return;
		}

	}

	function userlogin(userid, name, currency) {
        $('#user_login_message input[name=userid]').val(userid);
        $('#user_login_message').window('open');
		// $.dialog({
		// 	title : '模拟登陆暂停使用',
		// 	content : "模拟登陆暂停使用",
		// 	max : false,
		// 	min : false,
		// 	lock : true,
		// 	drag : false,
		// 	fixed : true,
		// 	ok : function() {
		// 		return;
		// 	}/*,
		// 	cancel : function() {
		// 		return;
		// 	}*/
		// });
		/*//获取加密信息
		$.ajax({
			type:'post',
			url:'../warehouse/encodeStr',
			data:{
				str:userid
			},
			success:function(data){
				if(data != null){
					userid = data;
					window.open(
							"http://www.import-express.com/simulateLogin/login?userName="
							+ userid + "&password=" + name.replace(/\s+/g,'') + "&currency="
							+ currency, "_blank");
				}else{
					alert("加密用户名报错勒！！");
				}
			}
		});*/

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
	//删除用户备注
	function deleteUserRemark(id) {
        $.messager.confirm('提示','你确定要删除该条备注吗?',function(r){
            if(r){
                $.ajax({
                    type: "POST",
                    url: "/cbtconsole/userinfo/updateUserRemark.do?id=" + id,
                    dataType:"json",
                    success: function(msg){
                        $.messager.alert('提示', msg.message);
                        $('#user_remark').window('close');
                    }
                });
            }
        });
    }
    //显示用户备注
    function showRemark(uid) {
        $('#user_remark .remark_list').html('');
        $("#user_remark input[name='userid']").val(uid);
        $('#new_user_remark').val('');
        //查询历史备注信息
        $.ajax({
            type: "POST",
            url: "/cbtconsole/userinfo/queryUserRemark",
            data: {userid:uid},
            dataType:"json",
            success: function(msg){
                if(msg != undefined && msg.length > 0){
                    var temHtml = '';
                    $(msg).each(function (index, item) {
                        var remarkArr = item.split('@@@@');
                        if(remarkArr != undefined && remarkArr.length == 3){
                            temHtml += '<tr><td class="remark_td1">' + remarkArr[1]
                                + '</td><td class="remark_td2">' + remarkArr[2]
                                + '</td><td>' + '<a href="#" onclick="deleteUserRemark(\'' + remarkArr[0] + '\')">删除</a>' + '</td></tr>';
                        }
                    });
                    $('#user_remark .remark_list').html(temHtml);
                }
                $('#user_remark').window('open');
            }
        });
    }
    //添加用户备注
    function addUserRemark() {
        var userid = $("#user_remark input[name='userid']").val();
        var remark = $('#new_user_remark').val();
        if(remark == undefined || remark == ''){
            $.messager.alert('提示', '请输入新添加的备注');
            return;
        }
        $.ajax({
            type: "POST",
            url: "/cbtconsole/userinfo/addUserRemark",
            data: {
                remark:remark,
                userid:userid
            },
            dataType:"json",
            success: function(res){
                $.messager.alert('提示', res.message);
                $('#user_remark').window('close');
            }
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
    <div style="display: none">
        <form id="user_login_from" method="post" target="_blank">
            <input type="text" name="email">
            <input type="password" name="pass">
        </form>
    </div>
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
    <div id="user_remark" class="easyui-window" title="添加/历史用户备注"
         data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"
         style="width:800px;height:auto;display: none;font-size: 16px;">
            <div style="margin-left:20px;">
                <input type="hidden" name="userid">
                <div style="margin-top:20px;">历史备注:</div>
                <div style="margin-left:20px;">
                    <table class="remark_list" style="width: 720px;word-break:break-all; word-wrap:break-all;">
                    </table>
                </div>
                <div style="margin-top:20px;">新添加备注:</div>
                <div style="margin-left:20px;">
                    <textarea rows="60" cols="60" id="new_user_remark" style="height: 80px;width: 400px;"></textarea><br />
                </div>
            </div>
            <div style="margin:20px 0 20px 40px;">
                <a href="javascript:void(0)" class="easyui-linkbutton"
                   onclick="addUserRemark()" style="width:80px">添加备注</a>
            </div>
    </div>
    <div id="user_login_message" class="easyui-window" title="选择登陆网站"
         data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"
         style="width:400px;height:240px;display: none;font-size: 16px;">
        <div style="margin-left:20px;">
            <input type="hidden" name="userid">
            <br />
            <input checked='checked' type='radio' name='site' value='1'/>
            <span onclick="">import-express</span>
            <br /><br />
            <input type='radio' name='site' value='2'/>
            <span>kidsproductwholesale</span>
            <br /><br />
            <input type='radio' name='site' value='3'/>
            <span>lovelypetsupply</span>
            <br /><br />
        </div>
        <div style="margin-left: 260px;">
            <button onclick="userloginJump()">模拟登陆</button>
        </div>
    </div>
    <div id="user_type" class="easyui-window" title="授权"
         data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"
         style="width:400px;height:200px;display: none;font-size: 16px;">
        <div style="margin-left:20px;">
            <input type="hidden" name="userid">
            <br /><br />
            <input type="radio" name="user_type" value="0">取消授权
            <br /><br />
            <input type="radio" name="user_type" value="1">进行授权
        </div>
        <div style="margin-left: 260px;">
        </div>
    </div>
	<div id="send_recommend_id" class="easyui-window" title="推荐目录推送"
         data-options="collapsible:false,minimizable:false,maximizable:false,closed:true"
         style="width:500px;height:400px;display: none;font-size: 16px;">
        <table align="center">
			<tr>
				<td>用户邮箱:</td><td>
				<input id="send_user_id" value="0" style="display: none"/>
				<input id="send_web_site" value="-1" style="display: none"/>
				<input id="send_user_email" style="width: 280px;"/></td>
			</tr>
			<tr>
				<td>注册日期:</td><td><input id="user_create_time" style="width: 280px;"/></td>
			</tr>
			<tr>
				<td>商务信息:</td><td><input id="user_buniess_info" style="width: 280px;"/></td>
			</tr>
			<tr>
				<td>产品需求:</td><td><input id="user_goods_need" style="width: 280px;"/>
			<br>
			<input id="user_goods_require" style="width: 280px;"/></td>
			</tr>
			<tr>
				<td>目录地址:</td><td><input id="send_url" style="width: 280px;"/><button>生成目录</button></td>
			</tr>
			<tr>
				<td>推送邮箱:</td><td><input id="sell_email" style="width: 280px;"/></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center"><button onclick="sendRecommendEmail()">推送</button>
				<span id="notice_id" style="display: none;color: red;">执行中，请等待...</span>
				</td>
			</tr>
		</table>
		<table align="center" id="history_table" style="display: none;" border="1" cellpadding="0">
			<caption>推送历史记录</caption>
			<tbody id="history_id">

			</tbody>
		</table>
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
				<th data-options="field:'userid',width:80,align:'c/getUserInfo.doenter',formatter:formatterUserid">用户ID</th>
				<th data-options="field:'businessName',width:50,align:'center'">business name</th>
				<th data-options="field:'email',width:80,align:'center'">注册邮箱</th>
				<th data-options="field:'creattime',width:80,align:'center'">创建时间</th>
				<th data-options="field:'loginStyle',width:50,align:'center'">登录方式</th>
				<th data-options="field:'zone',width:60,align:'center'">国家</th>
				<th data-options="field:'statename',width:60,align:'center'">statename</th>
				<th data-options="field:'goodsPriceUrl',width:50,align:'center'">购物车总额</th>
				<th data-options="field:'available',width:25,align:'center'">余额</th>
				<th data-options="field:'userLogin',width:80,align:'center'">用户登陆</th>
				<th data-options="field:'webSite',width:80,align:'center'">Web</th>
				<th data-options="field:'userManager',width:80,align:'center'">用户管理</th>
				<th data-options="field:'grade',width:50">用户等级</th>
				<th data-options="field:'admuser',width:85,align:'center'">负责人</th>
				<th data-options="field:'currency',width:30,align:'center'">货币单位</th>
				<th data-options="field:'operation',width:210,align:'center'">操作</th>
			</tr>
		</thead>
	</table>
</body>
</html>