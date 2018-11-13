<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <style type="text/css">
    </style>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
    <title>客户评论管理</title>
    <meta http-equiv="Content-Type" content="text/html; charset=gb2312">
    <link rel="stylesheet" href="script/style.css" type="text/css">
    <link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
    <link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
    <script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js" type="text/javascript"></script>
    <script type="text/javascript">
        var updateSourcesUrl = "/cbtconsole/StatisticalReport/updateSources"; //盘点库存
    </script>
    <style type="text/css">
        .displaynone{display:none;}
        .item_box{display:inline-block;margin-right:52px;}
        .item_box select{width:150px;}
        .w-group{margin-bottom: 10px;width: 60%;text-align: center;}
        .w-label{float:left;}
        .w-div{margin-left:120px;}
        .w-remark{width:100%;}
        table.imagetable {
            font-family: verdana,arial,sans-serif;
            font-size:11px;
            color:#333333;
            border-width: 1px;
            border-color: #999999;
            border-collapse: collapse;
        }
        .mod_pay3 {
            width: 400px;
            height:400px;
            position: fixed;
            top: 100px;
            left: 15%;
            margin-left:400px;
            z-index: 1011;
            padding: 5px;
            padding-bottom: 20px;
            z-index: 1011;
        }
        table.imagetable th {
            background:#b5cfd2 url('cell-blue.jpg');
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #999999;
        }
        .panel-title {
            text-align: center;
            height: 30px;
            font-size: 24px;
        }
        table.imagetable td {
            /* 	background:#dcddc0 url('cell-grey.jpg'); */
            border-width: 1px;
            padding: 8px;
            border-style: solid;
            border-color: #999999;
            word-break: break-all;
        }
        .displaynone{display:none;}
        .but_color {
            background: #44a823;
            width: 80px;
            height: 24px;
            border: 1px #aaa solid;
            color: #fff;
        }

    </style>
    <%
        String in_id=request.getParameter("in_id");
    %>
    <script type="text/javascript">
		// 获取url中参数
	    function getUrlParam(name) {
	        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	        var r = window.location.search.substr(1).match(reg); //匹配目标参数
	        if (r != null) return unescape(r[2]); return null; //返回参数值
	    }
        $(function(){
            setDatagrid();
        	$("#easyui-datagrid").datagrid("options").url = "/cbtconsole/StatisticalReport/reviewManagerment";
        	
            $("#goods_pid").textbox('setValue',getUrlParam('goods_pid'));
            $("#orderno").textbox('setValue',getUrlParam('orderno'));
            $("#userid").textbox('setValue',getUrlParam('userid'));
            doQuery(1);
        })



        function setDatagrid() {
            $('#easyui-datagrid').datagrid({
                title : '客户评论管理',
                //iconCls : 'icon-ok',
                width : "100%",
                fit : true,//自动补全
                pageSize : 20,//默认选择的分页是每页20行数据
                pageList : [ 20],//可以选择的分页集合
                nowrap : false,//设置为true，当数据长度超出列宽时将会自动截取
                striped : true,//设置为true将交替显示行背景。
// 			collapsible : true,//显示可折叠按钮
                toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
                url : '',//url调用Action方法
                //url : '/cbtconsole/StatisticalReport/reviewManagerment',//url调用Action方法
                loadMsg : '数据装载中......',
                singleSelect : false,//为true时只能选择单行
                fitColumns : true,//允许表格自动缩放，以适应父容器
                idField:'itemid',
                //sortName : 'xh',//当数据表格初始化时以哪一列来排序
// 			sortOrder : 'desc',//定义排序顺序，可以是'asc'或者'desc'（正序或者倒序）。
                pagination : true,//分页
                rownumbers : true
                //行数
            });
        }
        
        function doQuery(page) {
            var goods_pid = $("#goods_pid").val();
            var orderno=$("#orderno").val();
            var userid=$("#userid").val();
            $("#easyui-datagrid").datagrid("load", {
                "page":page,
                "goods_pid":goods_pid,
                "orderno":orderno,
                "userid":userid
            });
        }

        function doReset(){
            $("#goods_pid").textbox('setValue','');
            $("#orderno").textbox('setValue','');
            $("#userid").textbox('setValue','');
        }




        function BigImg(img){
            htm_="<img width='400px' height='400px' src="+img+">";
            $("#big_img").append(htm_);
            $("#big_img").css("display","block");
        }

        function closeBigImg(){
            $("#big_img").css("display","none");
            $('#big_img').empty();
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

        function setDisplay(id,showFlag) {
            $.ajax({
                url:'/cbtconsole/StatisticalReport/setDisplay',
                type:'POST',
                async:true,
                data:{
                    id:id,
                    showFlag:showFlag
                },
                success:function(data){
                    if(data>0){
                        $('#easyui-datagrid').datagrid('reload');
                    }else{
                        topCenter("操作失败，请重试");
                    }
                }
            })
        }
        //邮件回复
        function msgbox(userid){
            $('#dlg1').dialog('open');
            $("#emailUser").val(userid);
        }
        //给客户发邮件
        function sendEmailToUser(){
            var userid=$("#emailUser").val();
            var remark=$("#remark_").val();
            if(remark == null || remark == ""){
                topCenter("请输入回复内容");
                return;
            }
            $.ajax({
                url:'/cbtconsole/goodsComment/sendMailToCustomer.do',
                type:'POST',
                async:true,
                data:{
                    custId:userid,
                    content:remark
                },
                success:function(data){
                    if(data == 'success'){
                        cance1();
                    }else{
                        topCenter("邮件发送失败");
                    }
                }
            });
        }

        function cance1(){
            $('#dlg1').dialog('close');
            $("#remark_").textbox('setValue','');
            $("#emailUser").val("");
        }
    </script>
</head>
<body text="#000000" onload=" $('#dlg1').dialog('close');">
<div class="mod_pay3" style="display: none;" id="big_img">

</div>
<div id="dlg1" class="easyui-dialog" title="输入邮件内容" data-options="modal:true" style="width:400px;height:400px;padding:10px;autoOpen:false;;closed:true;display: none;">
    <form id="ff" method="post" style="height:100%;">
        <div style="margin-bottom:20px;margin-left:35px;">
            <input type="hidden" id="emailUser" name="emailUser">
            <input class="easyui-textbox" name="remark_" id="remark_"  style="width:300px;height:260px"  data-options="label:'回复内容:'">
        </div>
        <div style="text-align:center;padding:5px 0">
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="sendEmailToUser()" style="width:80px;margin-left: 30px;">发送</a>
            <a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance1()" style="width:80px">取消</a>
        </div>
    </form>
</div>
<div id="top_toolbar" style="padding: 5px; height: auto;">
    <input class="easyui-textbox" name="goods_pid" id="goods_pid" style="width:10%;margin-top: 15px;margin-left: 200px;"  data-options="label:'商品pid:'">
    <input class="easyui-textbox" name="orderno" id="orderno" style="width:10%;margin-top: 15px;"  data-options="label:'订单号:'">
    <input class="easyui-textbox" name="userid" id="userid" style="width:10%;margin-top: 15px;"  data-options="label:'userid:'">
    <input class="but_color" type="button" value="查询" onclick="doQuery(1,0)">
    <input class="but_color" type="button" value="重置" onclick="doReset()">
</div>

<table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
    <thead>
    <tr>
        <th data-options="field:'goodsInfo',width:80,align:'center'">产品</th>
        <th data-options="field:'goodsid',width:80,align:'center'">订单详情编号</th>
        <th data-options="field:'car_type',width:80,align:'center'">规格</th>
        <th data-options="field:'user_id',width:50,align:'center'">评论账号</th>
        <th data-options="field:'comments_content',width:80,align:'center'">评论内容</th>
        <th data-options="field:'comments_time',width:50,align:'center'">评论时间</th>
        <th data-options="field:'picPath',width:50,align:'center'">评论图片</th>
        <th data-options="field:'show_flag',width:50,align:'center'">状态</th>
        <th data-options="field:'sendEmail',width:50,align:'center'">回复</th>
    </tr>
    </thead>
</table>
</body>
</html>