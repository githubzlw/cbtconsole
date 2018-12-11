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
	<title>采购订单管理页面</title>
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
			font-size: 34px;
		}
		tr .td_class{width:230px;}
		.td_class lable{
			float:left;
			width:120px;
		}
		.w_input input{width:200px;}
	</style>
	<script type="text/javascript">
        $(function() {
            document.onkeydown = function(e){
                var ev = document.all ? window.event : e;
                if(ev.keyCode==13) {
                    doQuery(1,0);
                }
            }
            $('#easyui-datagrid').datagrid({
                rowStyler:function(index,row){
                    var status=row.status
                    if (status=="<span style='color:green'>订单中商品已被取消</span>"){
                        return 'background-color:red;';
                    }
                }
            });

            setDatagrid();
            var opts = $("#easyui-datagrid").datagrid("options");
            opts.url = "/cbtconsole/warehouse/getPrePurchase";
            $('#dlg1').dialog('close');
        })


        function setDatagrid() {
            $('#easyui-datagrid').datagrid({
                title : '采购订单管理页面',
                //iconCls : 'icon-ok',
                width : "100%",

                height : "100%",
                fit : true,//自动补全
                autoRowWidth:false,
                pageSize : 40,//默认选择的分页是每页20行数据
                pageList : [ 40 ],//可以选择的分页集合
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


        function doQuery(page,type) {
            var iduserid = $("#iduserid").val();
            var orderid = $("#orderid").val();
            var goodsid = $("#goodsid").val();
            var goods_name = $("#goods_name").val();
            var goods_pid=$("#goods_pid").val();
            var admuserid=$('#admuserid').combobox('getValue');
            var state=$('#state').combobox('getValue');
            var days=$('#days').combobox('getValue');
            if(type!="0"){
                orderid=$("#v_info_"+type+"").val();
                days="365";
            }else{
                orderid="'"+orderid+"'";
            }
            $("#easyui-datagrid").datagrid("load", {
                "page" : page,
                "orderid":orderid,
                "userid":iduserid,
                "days":days,
                "goodsid":goodsid,
                "admuserid":admuserid,
                "state":state,
                "goods_name":goods_name,
				"goods_pid":goods_pid,
                "type":type
            });
            initData();
        }

        function doReset() {
            $("#orderid").textbox('setValue','');
            $("#goodsid").textbox('setValue','');
            $("#goods_name").textbox('setValue','');
            $("#iduserid").textbox('setValue','');
            $('#days').combobox('setValue','1');
            $('#state').combobox('setValue','-1');
            $("#goods_pid").textbox('setValue','');
            $('#admuserid').combobox('setValue','<%=adm.getId()%>');
        }

        function initData(){
            //超过交期项目   同意替换  验货有疑问  疑似货源贴错
            var admuserid='<%=adm.getId()%>';
            var old_admid=$("#old_admuserid").val();
            if(old_admid!="0"){
                admuserid=$('#admuserid').combobox('getValue');
            }
            var old_id=$("#old_admuserid").val();
            if(admuserid==old_id){
                return;
            }
            // for(var i=1;i<=6;i++){
            // 	$.ajax({
            // 		type:"post",
            // 		url:"/cbtconsole/warehouse/getOrderInfoCountByState.do",
            // 		data:{state:i,admuserid:admuserid},
            // 		dataType:"text",
            // 		async:false,
            // 		success : function(data){
            // 			if(data != '0'){
            // 			    var obj = eval("("+data+")");
            // 				$("#v_info_"+i+"").val(obj.ordernoarray);
            // 				$("#info_"+i+"").html(obj.ordernonum);
            // 			}
            // 		}
            // 	});
            // }

            //超过交期项目(订单)
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getOrderInfoCountByState.do",
                data:{state:1,admuserid:admuserid},
                dataType:"text",
                async:true,
                success : function(data){
                    if(data != '0'){
                        var obj = eval("("+data+")");
                        $("#v_info_1").val(obj.ordernoarray);
                        $("#info_1").html(obj.ordernonum);
                    }
                }
            });
            //建议替换(订单)
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getOrderInfoCountByState.do",
                data:{state:2,admuserid:admuserid},
                dataType:"text",
                async:true,
                success : function(data){
                    if(data != '0'){
                        var obj = eval("("+data+")");
                        $("#v_info_2").val(obj.ordernoarray);
                        $("#info_2").html(obj.ordernonum);
                    }
                }
            });
            //同意替换(订单)
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getOrderInfoCountByState.do",
                data:{state:3,admuserid:admuserid},
                dataType:"text",
                async:true,
                success : function(data){
                    if(data != '0'){
                        var obj = eval("("+data+")");
                        $("#v_info_3").val(obj.ordernoarray);
                        $("#info_3").html(obj.ordernonum);
                    }
                }
            });
            //验货有疑问(订单)
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getOrderInfoCountByState.do",
                data:{state:4,admuserid:admuserid},
                dataType:"text",
                async:true,
                success : function(data){
                    if(data != '0'){
                        var obj = eval("("+data+")");
                        $("#v_info_4").val(obj.ordernoarray);
                        $("#info_4").html(obj.ordernonum);
                    }
                }
            });
            //疑似没有购买记录(订单)
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getOrderInfoCountByState.do",
                data:{state:5,admuserid:admuserid},
                dataType:"text",
                async:true,
                success : function(data){
                    if(data != '0'){
                        var obj = eval("("+data+")");
                        $("#v_info_5").val(obj.ordernoarray);
                        $("#info_5").html(obj.ordernonum);
                    }
                }
            });
            $.ajax({
            	type:"post",
            	url:"/cbtconsole/warehouse/getOrderInfoCountByState.do",
            	data:{state:6,admuserid:admuserid},
            	dataType:"text",
            	async:true,
            	success : function(data1){
            		$("#v_info_6").val("");
            		$("#info_6").html("0");
            		if(data1 != '0'){
            		    var obj1 = eval("("+data1+")");
            		    console.log(obj1.ordernonum);
            			$("#v_info_6").val(obj1.ordernoarray);
            			$("#info_6").html(obj1.ordernonum);
            		}
            	}
            });
            //采购超24H无物流信息
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getOrderInfoCountByState.do",
                data:{state:7,admuserid:admuserid},
                dataType:"text",
                async:true,
                success : function(data1){
                    $("#v_info_7").val("");
                    $("#info_7").html("0");
                    if(data1 != '0'){
                        var obj1 = eval("("+data1+")");
                        console.log(obj1.ordernonum);
                        $("#v_info_7").val(obj1.ordernoarray);
                        $("#info_7").html(obj1.ordernonum);
                    }
                }
            });


            //超过1天未发货
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getNotShipped",
                data:{admuserid:admuserid},
                dataType:"text",
                async:true,
                success : function(data){
                    if(data != '0'){
                        var obj = eval("("+data+")");
                        $("#tb_info_6").html(obj.num);
                    }
                }
            });
            //发货3天未入库
            $.ajax({
                type:"post",
                url:"/cbtconsole/warehouse/getShippedNoStorage",
                data:{admuserid:admuserid},
                dataType:"text",
                async:true,
                success : function(data){
                    if(data != '0'){
                        // var obj = eval("("+data+")");
                        $("#tb_info_7").html("<a target='_blank' href='/cbtconsole/website/noStorageDetails.jsp' title='查看发货三天未入库商品详情'>"+data+"</a>");
                    }
                }
            });
            //当月分配种类
            $.ajax({
                type:'post',
                dataType:"text",
                async:true,
                url:'/cbtconsole/warehouse/getfpCount.do',
                data:{admuserid:admuserid},
                success:function(data){
                    if(data=='null'){
                        data = 0;
                    }
                    $("#tj_info_1").html(data);
                }});
            //当月已确认采购种类
            $.ajax({
                type:'post',
                dataType:"text",
                async:true,
                url:'/cbtconsole/warehouse/getMCgCount.do',
                data:{admuserid:admuserid},
                success:function(data){
                    if(data=='null'){
                        data = 0;
                    }
                    $("#tj_info_2").html(data);
                }});
            //当日分配采购种类
            $.ajax({
                type:'post',
                dataType:"text",
                async:true,
                url:'/cbtconsole/warehouse/getDistributionCount.do',
                data:{admuserid:admuserid},
                success:function(data){
                    if(data=='null'){
                        data = 0;
                    }
                    $("#tj_info_3").html(data);
                }});
            //每日采购数量  平均花费时间
            $.ajax({
                type:'post',
                dataType:"text",
                async:true,
                url:'/cbtconsole/warehouse/getSjCgCount.do',
                data:{admuserid:admuserid},
                success:function(data){
                    if(data=='null'){
                        data = 0;
                    }
                    var mydate = new Date();
                    if(data=='0'){
                        minutes = 0;
                    }else{
                        if (mydate.getHours()>=13) {
                            minutes = (mydate.getHours()-10)*60+mydate.getMinutes();
                            minutes = minutes/Number(data);
                        } else {
                            minutes = (mydate.getHours()-9)*60+mydate.getMinutes();
                            minutes = minutes/Number(data);
                        }
                    }
                    $("#tj_info_5").html("("+minutes.toFixed()+"分钟)");
                    $("#tj_info_4").html(data);
                }});
            $("#old_admuserid").val(admuserid);
        }
        function addOrderInfo(orderid,admuserid){
            console.log(orderid);
            console.log(admuserid);
            $("#ad_orderid").val(orderid);
            $("#ad_admuserid").val(admuserid);
            $('#dlg1').dialog('open');

        }

        function cance1(){
            $('#dlg1').dialog('close');
            $("#remark_").textbox('setValue','');
            $("#ad_orderid").val("");
            $("#ad_admuserid").val("");
        }

        //添加订单采购记录
        function insertOrderRemark(){
            var orderid=$("#ad_orderid").val();
            var admuserid=$("#ad_admuserid").val();
            var remark = $.trim(document.getElementById("remark_").value);
            $.ajax({
                type:'post',
                dataType:"text",
                async:true,
                url:'/cbtconsole/warehouse/insertOrderRemark',
                data:{admuserid:admuserid,orderid:orderid,remark:remark},
                success:function(data){
                    if(data=='1'){
                        topCenter("添加备注成功");
                        $('#easyui-datagrid').datagrid('reload');
                        cance1();
                    }else{
                        topCenter("添加备注失败");
                    }
                }});
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

        function purchasing_allocation(){
            $.ajax({
                type:'post',
                dataType:"text",
                async:true,
                url:'/cbtconsole/warehouse/purchasing_allocation',
                data:{},
                success:function(data){
                    if(data>0){
                        topCenter("采购分配成功");
                    }else{
                        topCenter("采购分配失败");
                    }
                }});
        }
	</script>
</head>
<body onload="doQuery(1,0)">
<div id="dlg1" class="easyui-dialog" title="备注" data-options="modal:true" style="width:300px;height:300px;padding:10px;autoOpen:false;display: none;">
	<form id="ff" method="post" style="height:100%;">
		<input type="hidden" id="ad_orderid"/>
		<input type="hidden" id="ad_admuserid"/>
		<div style="margin-bottom:20px;margin-left:35px;">
			<input class="easyui-textbox" name="remark_" id="remark_"  style="width:210px;height: 150px;"  data-options="multiline:true">
		</div>
		<div style="text-align:center;padding:5px 0">
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="insertOrderRemark()" style="width:80px">提交</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="cance1()" style="width:80px">取消</a>
		</div>
	</form>
</div>
<div id="top_toolbar" style="padding: 5px; height: auto">
	<div>
		<span style="font-size:20px;font-weight:bold">当月分配种类:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_1" style="font-size:20px;width:35px;margin-right:50px">0</span>
		<span style="font-size:20px;font-weight:bold">当月已确认采购种类:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_2" style="font-size:20px;width:35px;margin-right:50px">0</span>
		<span style="font-size:20px;font-weight:bold">当日被分配种类:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_3" style="font-size:20px;width:35px;margin-right:50px">0</span>
		<span style="font-size:20px;font-weight:bold">当日采购种类:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_4" style="font-size:20px;width:35px;margin-right:50px">0</span>
		<span style="font-size:20px;font-weight:bold">平均花费时间:</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tj_info_5" style="font-size:20px;width:130px;margin-right:50px">0</span>
		<span style="font-size:20px;font-weight:bold">超过1天未发货(订单):</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tb_info_6" style="font-size:20px;;margin-right:50px">0</span>
		<span style="font-size:20px;font-weight:bold">发货3天未入库(订单):</span><span class="easyui-label" data-options="iconCls:'icon-font',plain:true" id="tb_info_7" style="font-size:20px;">0</span>
	</div><br>
	<div style="font-size:15px;">
		超过交期项目(订单):<span onclick="doQuery(1,1);" class="easyui-linkbutton" data-options="iconCls:'icon-font',plain:true" id="info_1" style="color:red;width:38px;text-decoration:underline;margin-right:80px;">0</span>
		建议替换(订单):<span onclick="doQuery(1,2);" class="easyui-linkbutton" data-options="iconCls:'icon-font',plain:true" id="info_2" style="color:red;width:50px;text-decoration:underline;margin-right:80px;">0</span>
		同意替换(订单):<span onclick="doQuery(1,3);" class="easyui-linkbutton" data-options="iconCls:'icon-font',plain:true" id="info_3" style="color:red;width:38px;text-decoration:underline;margin-right:80px;">0</span>
		验货有疑问(订单):<span onclick="doQuery(1,4);" class="easyui-linkbutton" data-options="iconCls:'icon-font',plain:true" id="info_4" style="color:red;width:38px;text-decoration:underline;margin-right:80px;">0</span>
		疑似没有购买记录(订单):<span onclick="doQuery(1,5);" class="easyui-linkbutton" data-options="iconCls:'icon-font',plain:true" id="info_5" style="color:red;width:38px;text-decoration:underline">0</span>
		采购超24H无物流信息(订单):<span onclick="doQuery(1,7);" class="easyui-linkbutton" data-options="iconCls:'icon-font',plain:true" id="info_7" style="color:red;width:38px;text-decoration:underline">0</span>
		入库失败包裹(商品):<span onclick="doQuery(1,6);" class="easyui-linkbutton" data-options="iconCls:'icon-font',plain:true" id="info_6" style="color:red;width:38px;text-decoration:underline">0</span>
	</div>
	<input type="hidden" id="old_admuserid" value="0"/><input type="hidden" id="v_info_1"/><input type="hidden" id="v_info_2"/><input type="hidden" id="v_info_3"/>
	<input type="hidden" id="v_info_4"/><input type="hidden" id="v_info_5"/><input type="hidden" id="v_info_6"/><input type="hidden" id="v_info_7"/>
	<div>
		<form id="query_form" action="#" onsubmit="return false;" style="margin-left:0px;">
			<select class="easyui-combobox" name="state" id="state" style="width:12%;" data-options="label:'订  单  状  态:',panelHeight:'auto'">
				<option value="-1" selected="selected">全部</option>
				<option value="0">未采购</option>
				<option value="1">采购中</option>
				<option value="2">采购完成待入库</option>
				<option value="3">已到仓库,校验有问题</option>
				<option value="4">已到仓库,未校验</option>
				<option value="5">已到仓库,验货无误</option>
				<option value="6">出运中</option>
				<option value="7">完结</option>
				<option value="8">订单取消</option>
				<option value="9">采样订单</option>
				<!-- 						<option value=fff"10">采购数    量大于订单数量</option> -->
				<option value="11">历史替换</option>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<select class="easyui-combobox" name="days" id="days" style="width:8%;" data-options="label:'商品分配时间:',panelHeight:'auto'">
				<option value="1" selected="selected">当天</option>
				<option value="3">最近三天</option>
				<option value="7">最近一周</option>
				<option value="14">最近两周</option>
				<option value="30">最近一个月</option>
				<option value="90">最近三个月</option>
				<option value="180">最近半年</option>
				<option value="365">最近1年</option>
				<option value="999">全部</option>
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<select class="easyui-combobox" name="admuserid" id="admuserid" style="width:15%;" data-options="label:'电商采购人:',panelHeight:'auto',valueField: 'id',
                    textField: 'admName', value:'<%=adm.getId()%>',selected:true,
                    url: '/cbtconsole/warehouse/getAllBuyer',
                    method:'get'">
			</select>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="easyui-textbox" name="iduserid" id="iduserid" style="width:10%;margin-top: 10px;"  data-options="label:'客户编号(ID):'">&nbsp;&nbsp;
			<input class="easyui-textbox" name="goodsid" id="goodsid" style="width:12%;margin-top: 10px;"  data-options="label:'商品编号/购物车id:'">&nbsp;&nbsp;
			<input class="easyui-textbox" name="goods_pid" id="goods_pid" style="width:12%;margin-top: 10px;"  data-options="label:'商品pid:'">&nbsp;&nbsp;
			<input class="easyui-textbox" name="orderid" id="orderid" style="width:12%;margin-top: 10px;"  data-options="label:'客户订单号:'">&nbsp;&nbsp;
			<input class="easyui-textbox" name="goods_name" id="goods_name" style="width:10%;margin-top: 10px;"  data-options="label:'产品名称:'">&nbsp;&nbsp;
			<input class="but_color" type="button" value="查询" onclick="doQuery(1,0)">
			<input class="but_color" type="button" value="重置" onclick="doReset()">
		</form>
	</div>
	<c:if test="<%=adm.getId()==9 || adm.getId()==1%>">
		<a href="javascript:purchasing_allocation();" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">订单采购分配</a>
	</c:if>
	<a target="_blank" href="/cbtconsole/website/purchase_order_details.jsp;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">问题包裹反查</a>
	<%--<a target="_blank" href="/cbtconsole/website/comfirmOrderAndGenerate.jsp;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">一键生成1688未付款订单</a>--%>
</div>
<table class="easyui-datagrid" id="easyui-datagrid"
	   style="width: 1800px; height: 900px">
	<thead>
	<tr>
		<th data-options="field:'user_id',width:30,align:'center'">用户ID</th>
		<th data-options="field:'orderid',width:50,align:'center'">订单号</th>
		<th data-options="field:'country',width:30,align:'center'">订单国家</th>
		<th data-options="field:'saleer',width:25,align:'center'">销售</th>
		<th data-options="field:'fptime',width:40,align:'center'">分配时间</th>
		<th data-options="field:'paytime',width:50,align:'center'">支付时间</th>
		<th data-options="field:'domesticTime',width:20,align:'center'">国内准备段</th>
		<th data-options="field:'amount_s',width:30,align:'center'">分配总数/采购</th>
		<th data-options="field:'amounts',width:40,align:'center'">入库/验货无误/验货疑问/无物流信息</th>
		<th data-options="field:'status',width:30,align:'center'">当前状态</th>
		<th data-options="field:'option',width:30,align:'center'">操作</th>
	</tr>
	</thead>
</table>
<script type="text/javascript">
    initData();
</script>
</body>
</html>