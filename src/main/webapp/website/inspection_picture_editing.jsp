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
<title>验货图片编辑</title>
<style type="text/css">
.but_color {
	background: #44a823;
	width: 80px;
	height: 24px;
	border: 1px #aaa solid;
	color: #fff;
}
.mod_pay3 {
	width: 400px;
	height:400px;
	position: fixed;
	top: 100px;
	left:3%;
	z-index: 1011;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 1011;
}
.panel-title {
	text-align: center;
	height: 30px;
	font-size: 34px;
}
tr .td_class{width:230px;}
.td_class lable{
	float:left;
	width:120px;
}
.w_input input{width:200px;}
	.div_box{float:left;margin-left:20px;}
</style>
<script type="text/javascript">
	$(function() {
		document.onkeydown = function(e){
			var ev = document.all ? window.event : e; 
			if(ev.keyCode==13) {
				doQuery(1,0);
			}
		}
		$('#pic_dlg').dialog('close');
        $('#pic_dlg1').dialog('close');
        $('#dlg2').dialog('close');
		setDatagrid();
		var opts = $("#easyui-datagrid").datagrid("options");
		opts.url = "/cbtconsole/warehouse/queryPictureInfos";
	})

    function BigImg(img){
        htm_="<img width='400px' src="+img+">";
        $("#big_img").append(htm_);
        $("#big_img").css("display","block");
    }

    function closeBigImg(){
        $("#big_img").css("display","none");
        $('#big_img').empty();
    }
	

	function setDatagrid() {
		$('#easyui-datagrid').datagrid({
			title : '验货图片编辑加评论',
			//iconCls : 'icon-ok',
			width : "100%",
			
			height : "100%",
			fit : true,//自动补全 
			autoRowWidth:false,
			pageSize : 10,//默认选择的分页是每页20行数据
			pageList : [ 10 ],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
			// 			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : '',//url调用Action方法
			loadMsg : '数据装载中......',
            nowrap:false,
			singleSelect : true,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
			//sortName : 'xh',//当数据表格初始化时以哪一列来排序
			//sortOrder : 'desc',//定义排序顺序，可以是'asc'或者'desc'（正序或者倒序）。
			pagination : true,//分页
			rownumbers : true
		//行数
		});
	}


	function doQuery(page) {
		var goods_pid = $("#goods_pid").val();
		var orderno=$("#orderno").val();
		var goods_id=$("#goods_id").val();
        var times = $("#times").val();
        var odid=$("#odid").val();
        var oldOrderid=$("#oldOrderid").val();
        var admuserid=$('#admuserid').combobox('getValue');
		$("#easyui-datagrid").datagrid("load", {
			"page" : page,
			"pid":goods_pid,
			"times":times,
			"orderno":orderno,
			"goods_id":goods_id,
			"admuserid":admuserid,
			"odid":odid,
			"oldOrderid":oldOrderid
		});
	}

	function doReset() {
		$("#goods_pid").textbox('setValue','');
        $("#orderno").textbox('setValue','');
        $("#goods_id").textbox('setValue','');
        $("#odid").val("");
        $("#oldOrderid").val("");
        $('#admuserid').combobox('setValue','<%=adm.getId()%>');
		$("times").val("1");
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
	
	function uploadInPic(goodsid,orderid,goods_pid,pic,i_id){
		$('#pic_dlg').dialog('open');
		$("#pid").val(goods_pid);
		$("#goodsid").val(goodsid);
		$("#orderid").val(orderid);
		$("#pic").val(pic);
		$("#i_id").val(i_id);
	}

    function uploadPics(goods_pid){
        $('#pic_dlg1').dialog('open');
        $("#new_pid").val(goods_pid);
    }

	function delInPic(goodsid,orderid,goods_pid,imgPath,i_id){
        $.messager.progress({
            title : '图片删除中',
            msg : '请等待...'
        });
        $.ajax({
            type: "POST",//方法类型
            dataType:'json',
            url:'/cbtconsole/warehouse/delInPic',
            data:{orderid:orderid,goodsid:goodsid,goods_pid:goods_pid,path:imgPath,i_id:i_id
            },
            dataType:"json",
            success:function(data) {
                $.messager.progress('close');
                if (data > 0) {
                    topCenter("验货图片删除成功");
                    $('#easyui-datagrid').datagrid('reload');
                } else {
                    topCenter("验货图片删除失败");
                }
            }
        });
	}
	
	function uploadTypePicture() {
		var pid=$("#pid").val();
		var goodsid=$("#goodsid").val();
		var orderid=$("#orderid").val();
		var pic=$("#pic").val();
		var formData = new FormData($("#uploadFileForm")[0]);
		$.messager.progress({
			title : '图片更换中',
			msg : '请等待...'
		});
		$.ajax({
			url : '/cbtconsole/warehouse/uploadPic',
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			success : function(data) {
				$.messager.progress('close');
				if (data.ok) {
					topCenter("验货图片更换成功");
					$('#pic_dlg').dialog('close');
					$("#uploadFileForm")[0].reset();
					$('#easyui-datagrid').datagrid('reload');
				} else {
					topCenter("验货图片更换失败");
				}
			},
			error : function(XMLResponse) {
				$.messager.progress('close');
				topCenter("验货图片更换失败");
			}
		});
	}

    function uploadTypePictures() {
        var formData = new FormData($("#uploadFileForm1")[0]);
        $.messager.progress({
            title : '上传图片中',
            msg : '请等待...'
        });
        $.ajax({
            url : '/cbtconsole/warehouse/uploadTypeNewPictures',
            type : 'POST',
            data : formData,
            contentType : false,
            processData : false,
            success : function(data) {
                $.messager.progress('close');
                if (data.ok) {
                    topCenter("验货图片上传成功");
                    $('#pic_dlg1').dialog('close');
                    $("#uploadFileForm1")[0].reset();
                    $('#easyui-datagrid').datagrid('reload');
                } else {
                    topCenter("验货图片上传失败");
                }
            },
            error : function(XMLResponse) {
                $.messager.progress('close');
                topCenter("验货图片上传失败");
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
	
	function closeUploadDialog() {
		$('#pic_dlg').dialog('close');
		$("#uploadFileForm")[0].reset();
	}
    function closeUploadDialog1() {
        $('#pic_dlg1').dialog('close');
        $("#uploadFileForm1")[0].reset();
    }
	// 停用启用ss
	function disabledPic(id,state,picture){
	    var str="";
	    if(state == "0"){
	        str="启用";
		}else{
	        str="停用";
		}
        $.messager.progress({
            title : '图片'+str+'中',
            msg : '请等待...'
        });
        $.ajax({
            type: "POST",//方法类型
            dataType:'json',
            url:'/cbtconsole/warehouse/disabled',
            data:{i_id:id,state:state,picture:picture},
            dataType:"json",
            success:function(data) {
                $.messager.progress('close');
                if (data > 0) {
                    topCenter("验货图片"+str+"成功");
                    $('#easyui-datagrid').datagrid('reload');
                } else {
                    topCenter("验货图片"+str+"失败");
                }
            }
        });
	}

    //打开评论弹窗
    function openEvaluation(goods_pid){
        $('#dlg2').dialog('open');
        $("#q_goods_pid").val(goods_pid);
    }
    //添加产品评论
	function insertEvaluation(){
	    var goods_pid=$("#q_goods_pid").val();
	    var evaluation=$("#evaluation").val();
	    if(evaluation == null || evaluation == ""){
            topCenter("评论内容不能为空");
            return;
		}
        $.ajax({
            type: "POST",//方法类型
            dataType:'json',
            url:'/cbtconsole/warehouse/insertEvaluation',
            data:{goods_pid:goods_pid,evaluation:evaluation},
            dataType:"json",
            success:function(data) {
                if(data>0){
                    topCenter("添加评论成功");
                    cance2();
                    $('#easyui-datagrid').datagrid('reload');
				}else{
                    topCenter("添加评论失败");
				}
            }
        });
	}
    function cance2(){
        $('#dlg2').dialog('close');
        $("#evaluation").textbox('setValue','');
        $("#q_goods_pid").val("");
    }

    function showEvaluation(pid){
        $.ajax({
            type: "POST",//方法类型
            dataType:'json',
            url:'/cbtconsole/warehouse/showEvaluation',
            data:{goods_pid:pid},
            dataType:"json",
            success:function(data) {
                if(data>0){
                    topCenter("展示成功");
                    cance2();
                    $('#easyui-datagrid').datagrid('reload');
                }else{
                    topCenter("展示失败");
                }
            }
        });
	}
	//验货图片关联验货商品
	function insInsp(goodsPid,odid,picPath,orderid){
        if(!confirm("确定关联该验货照片?")){
            return;
		}
        $.ajax({
            type: "POST",//方法类型
            dataType:'json',
            url:'/cbtconsole/warehouse/insInsp',
            data:{goodsPid:goodsPid,odid:odid,picPath:picPath,orderid:orderid},
            dataType:"json",
            success:function(data) {
                if(data>0){
                    topCenter("关联成功");
                    $('#easyui-datagrid').datagrid('reload');
                }else{
                    topCenter("关联失败");
                }
            }
        });
	}
	</script>
</head>
<body onload="doQuery(1);$('#pic_dlg').dialog('close');$('#pic_dlg1').dialog('close');;$('#dlg2').dialog('close');">
		<div id="dlg2" class="easyui-dialog" title="添加评论" data-options="modal:true" style="width:300px;height:300px;padding:10px;autoOpen:false;display: none;">
			<form id="ff" method="post" style="height:100%;">
				<input type="hidden" id="q_goods_pid"/>
				<div style="margin-bottom:20px;margin-left:35px;">
					<input class="easyui-textbox" name="evaluation" id="evaluation"  style="width:210px;height: 150px;"  data-options="multiline:true">
				</div>
				<div style="text-align:center;padding:5px 0">
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="insertEvaluation()" style="width:80px">提交</a>
					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance2()" style="width:80px">取消</a>
				</div>
			</form>
		</div>
		<div class="mod_pay3" style="display: none;" id="big_img">

		</div>
		<div id="pic_dlg" class="easyui-dialog" title="验货图片更换"
			data-options="modal:true"
			style="width: 460px; height: 150px; padding: 10px;">
			<form id="uploadFileForm" method="post" enctype="multipart/form-data">
				<input type="hidden" name="pid" id="pid" value="" />
				<input type="hidden" name="orderid" id="orderid" value="" />
				<input type="hidden" name="goodsid" id="goodsid" value="" />
				<input type="hidden" name="pic" id="pic" value="" />
				<input type="hidden" name="i_id" id="i_id" value="" />
				<div style="margin-bottom: 20px">
					图&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;片:<input
						id="local_picture" name="uploadfile" class="easyui-filebox"
						data-options="prompt:'...'"
						style="width: 360px">
				</div>
			</form>

			<div style="text-align: center; padding: 5px 0">
				<a href="javascript:void(0)" data-options="iconCls:'icon-add'"
					class="easyui-linkbutton"
					onclick="uploadTypePicture()" style="width: 80px">确认上传</a>
				<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
					class="easyui-linkbutton" onclick="closeUploadDialog()"
					style="width: 80px">关闭</a>
			</div>
	</div>
	<div id="pic_dlg1" class="easyui-dialog" title="验货图片上传"
			 data-options="modal:true"
			 style="width: 460px; height: 150px; padding: 10px;">
			<form id="uploadFileForm1" method="post" enctype="multipart/form-data">
				<input type="hidden" id="new_pid" name="new_pid">
				<div style="margin-bottom: 20px">
					图&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;片:<input
						id="local_picture1" name="uploadfile1" class="easyui-filebox"
						data-options="prompt:'...'"
						style="width: 360px">
				</div>
			</form>

			<div style="text-align: center; padding: 5px 0">
				<a href="javascript:void(0)" data-options="iconCls:'icon-add'"
				   class="easyui-linkbutton"
				   onclick="uploadTypePictures()" style="width: 80px">确认上传</a>
				<a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
				   class="easyui-linkbutton" onclick="closeUploadDialog1()"
				   style="width: 80px">关闭</a>
			</div>
	</div>
	<div id="top_toolbar" style="padding: 5px; height: auto">
		<input type="hidden" id="odid" value="${param.odid}">
		<input type="hidden" id="oldOrderid" value="${param.oldOrderid}">
		<input class="easyui-textbox" name="goods_pid" id="goods_pid" value="${param.goodsPid}" style="width:10%;margin-top: 15px;"  data-options="label:'商品pid:'">
		<input class="easyui-textbox" name="orderno" id="orderno" style="width:10%;margin-top: 15px;"  data-options="label:'订单号:'">
		<input class="easyui-textbox" name="goods_id" id="goods_id" style="width:10%;margin-top: 15px;"  data-options="label:'商品编号/购物车id:'">
		验货时间: <select name="times" id="times" style="width: 60px;">
		<option value="1">当天</option>
		<option value="3">最近三天</option>
		<option value="5">最近5天</option>
		<option value="7">最近一周</option>
		<option value="999">全部</option>
	</select>
		<select class="easyui-combobox" name="admuserid" id="admuserid" style="width:15%;" data-options="label:'电商采购人:',panelHeight:'auto',valueField: 'id',
		textField: 'admName', value:'<%=adm.getId()%>',selected:true,
		url: '/cbtconsole/warehouse/getAllBuyer',
		method:'get'">
		</select>
		<input class="but_color" type="button" value="查询" onclick="doQuery(1,0)"> 
		<input class="but_color" type="button" value="重置" onclick="doReset()">
		<br><a href="/cbtconsole/website/upload_video.jsp" target="_blank" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">产品单页视频展示操作</a>
	</div>
	<table class="easyui-datagrid" id="easyui-datagrid"
		style="width: 1800px; height: 900px">
		<thead>
			<tr>
				<th data-options="field:'orderid',width:10,align:'center'">商品pid</th>
				<th data-options="field:'valid',width:10,align:'center'">商品状态</th>
				<th data-options="field:'currency',width:20,align:'center'">主图图片</th>
				<th data-options="field:'gcUnit',width:80,align:'center'">验货图片</th>
                <th data-options="field:'evaluation',width:10,align:'center'">产品评论</th>
				<th data-options="field:'position',width:15,align:'center'">上传图片</th>
			</tr>
		</thead>
	</table>
</body>
</html>