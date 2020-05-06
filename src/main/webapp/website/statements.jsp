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
    <title>退货对账</title>
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
    <script type="text/javascript">
        var searchReport = "/cbtconsole/StatisticalReport/searchCoupusManagement"; //报表查询
    </script>
    <style type="text/css">
        .displaynone{display:none;}
        .item_box{display:inline-block;margin-right:52px;}
        .item_box select{width:150px;}
        .mod_pay3 { width: 600px; position: fixed;
            top: 100px; left: 15%;
            z-index: 1011; background: gray;
            padding: 5px; padding-bottom: 20px;
            z-index: 1011; border: 15px solid #33CCFF; }
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
        String orderid=request.getParameter("orderid");
    %>
    <script type="text/javascript">
        $(function(){
            document.onkeydown = function(e){
                var ev = document.all ? window.event : e;
                if(ev.keyCode==13) {
                    doQuery(1);
                }
            }
            setDatagrid();
            pid= '<%=request.getParameter("pid")%>'
            var opts = $("#easyui-datagrid").datagrid("options");
            opts.url = "/cbtconsole/Look/Lookstatement?mid=0&pid="+pid;
            orderid = '<%=orderid%>';
            if(orderid!==null && orderid!="null"){
                $("#orderid").textbox('setValue',orderid);
                doQuery(1);
            }
        })

        function setDatagrid() {
            $('#easyui-datagrid').datagrid({
                title : '退货对账单',
                align : 'center',
                width : 'auto',
                height : 'auto',
                fit : true,
                pageSize : 40,
                pageList : [ 40],
                nowrap : false,//列的内容超出所定义的列宽时,false为自动换行wewr
                striped : true,//设置为true将交替显示行背景。
                toolbar : "#top_toolbar",
                url : '',
                loadMsg : '数据装载中......',
                singleSelect : false,
                fitColumns : true,
                idField:'itemid',
                style : {
                    padding : '8 8 10 8'
                },
                pagination : true,
                rownumbers : true
            });
        }

        function doQuery(page) {
            var location_type= $('#location_type').combobox('getValue');
            var optTimeStart = $("#optTimeStart").val();
            var optTimeEnd = $("#optTimeEnd").val();
            var shipno = $("#shipno").val();
            var order = $("#order").val();
            var pid = $("#pid").val();
            var skuid = $("#skuid").val();
            var state = $('#state option:selected') .val();
            $("#easyui-datagrid").datagrid("load", {
                location_type : location_type,
                optTimeStart:optTimeStart,
                page:page,
                optTimeEnd:optTimeEnd,
                shipno:shipno,
                order:order,
                pid2:pid,
                skuid:skuid,
                state:state
            });
            gettatolmaney();
        }

        function doReset(){
            $('#location_type').combobox('setValue','-1');
            $("#optTimeStart").val('');
            $("#shipno").textbox('setValue','');
            $("#order").textbox('setValue','');
            $("#pid").textbox('setValue','');
            $("#optTimeEnd").val('');
            $("#state").textbox('setValue','-2');
        }

        function gettatolmaney(){
            var location_type= $('#location_type').combobox('getValue');
            var optTimeStart = $("#optTimeStart").val();
            var optTimeEnd = $("#optTimeEnd").val();
            $.ajax({
                type:"post",
                url:"/cbtconsole/Look/gettatolmaney",
                dataType:"text",
                data:{optTimeStart : optTimeStart,optTimeEnd:optTimeEnd,location_type:location_type},
                success : function(data){
                    // $('#easyui-datagrid').datagrid('reload');
                    // $("#tatolmaney").val(data)
                    $("#tatolmaney").html(data);
                }
            });
        }

        function blurs(){
            var value=$("#barcode").val();
            if (value ==''){
                $("#barcode").textbox('setValue','如:GS271');
            }
        }
        function focus(){
            var value=$("#barcode").val();
            if (value =='如:GS271'){
                $("#barcode").textbox('setValue','');
            }
        }

    </script>
</head>
<body text="#000000" onload="doQuery(this.page);">
<div align="center">
    <div id="top_toolbar" style="padding: 5px; height: auto">
        <div>
            <form id="query_form" action="#" onsubmit="return false;">
                <select class="easyui-combobox" name="location_type" id="location_type" style="width:15%;" data-options="label:'采销人员:',panelHeight:'auto',valueField: 'id',
                    textField: 'admName', value:'',selected:true,
                    url: '/cbtconsole/warehouse/getAllBuyer',
                    method:'get'">
                </select>
                <%--<input class="easyui-textbox" name="order" id="order" style="width:15%;" onkeypress="if (event.keyCode == 13) doQuery(1)"  data-options="label:'客户订单号:'">--%>
                <%--<input class="easyui-textbox" name="pid" id="pid" style="width:15%;" onkeypress="if (event.keyCode == 13) doQuery(1)"  data-options="label:'pid:'">--%>
                <%--<input class="easyui-textbox" name="skuid" id="skuid" style="width:15%;" onkeypress="if (event.keyCode == 13) doQuery(1)"  data-options="label:'skuid:'" value="${param.skuid }">--%>
                时间：<input id="optTimeStart" class="Wdate"
                          style="width: 110px; height: 24px" type="text" value=""
                          onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
                <span>&nbsp;-&nbsp;</span><input id="optTimeEnd" class="Wdate"
                                                 style="width: 110px; height: 24px;" type="text" value=""
                                                 onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
                </span>&nbsp;&nbsp;&nbsp;&nbsp;
            </select>&nbsp;&nbsp;&nbsp;&nbsp;
                <input class="but_color" type="button" value="查询" onclick="doQuery(1)">
                <input class="but_color" type="button" value="重置" onclick="doReset()">
            </form>
            总金额：<div id="tatolmaney"></div>
        </div>

    </div>
    <table class="easyui-datagrid" id="easyui-datagrid"   style="width:1200px;height:900px">
        <thead>
        <tr>
            <th data-options="field:'a1688Order',width:50,align:'center'">1688订单信息</th>
            <th data-options="field:'returnReason',width:40,halign:'center'">退货原因</th>
            <th data-options="field:'returntime',width:30,align:'center'">退货时间</th>
            <th data-options="field:'optUser',width:20,align:'center'">采购人员</th>
            <th data-options="field:'actual_money',width:20,align:'center'">采购录入金额</th>
            <%--<th data-options="field:'refund_money',width:20,align:'center'">1688抓取退货价格</th>--%>
            <%--<th data-options="field:'stateShow',width:50,align:'center'">操作</th>--%>


        </tr>
        </thead>
    </table>
</div>
</body>
</html>