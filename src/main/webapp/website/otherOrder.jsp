<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@page import="com.cbt.util.Redis" %>
<%@page import="com.cbt.util.SerializeUtil" %>
<%@page import="com.cbt.website.userAuth.bean.Admuser" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>特殊订单操作</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.8.2.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/main.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/website/order_detail_new.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/lhgdialog/lhgdialog.min.js?self=true&skin=discuz"></script>
    <script type="text/javascript"
            src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
    <style type="text/css">
        #prinum {
            width: 250px;
            position: absolute;
            top: 50%;
            left: 50%;
            background: #f6cece;
            border: 1px solid #ddd;
            padding: 20px;
            z-index: 999;
            padding-top: 20px;
            display: none;
        }

        .show_h3 {
            height: 20px;
            text-align: left;
        }

        .pridclose {
            position: absolute;
            top: 3px;
            right: 3px;
            color: #f00;
        }

        .imagetable tr td {
            border-top: 1px solid #ddd;
            border-left: 1px solid #ddd;
            border-bottom: 1px solid #ddd;
            text-align: center;
        }

        .imagetable tr td:last-child {
            border-right: 1px solid #ddd;
        }

        .peimask {
            background: #777;
            opacity: 0.8;
            filter: alpha(opacity=80);
            position: absolute;
            top: 0;
            left: 0;
            z-index: 998;
            width: 100%;
            height: 100%;
            display: none;
        }

        .mod_pay3 {
            width: 500px;
            position: fixed;
            margin-left: 40%;
            margin-top: 10%;
            z-index: 1011;
            background: gray;
            padding: 5px;
            padding-bottom: 20px;
            z-index: 1011;
            border: 15px solid #33CCFF;
        }

        .repalyBtn {
            height: 30px;
            width: 70px;
            background: #1c9439;
            border: 0px solid #dcdcdc;
            color: #ffffff;
            cursor: pointer;
        }

        .mask {
            display: none;
            position: absolute;
            left: 0;
            top: 0;
            bottom: 0;
            right: 0;
            margin: auto;
            background: rgba(0, 0, 0, 0.6);
            width: 300px;
            height: 60px;
            line-height: 60px;
            text-align: center;
            border-radius: 10px;
            font-size: 20px;
            color: #fff;
            z-index: 100;
        }

        #div_clothing, #ss_div, #dz_div {
            position: fixed;
            top: 50%;
            left: 50%;
            -webkit-transform: translate(-50%, -50%);
            -moz-transform: translate(-50%, -50%);
            -ms-transform: translate(-50%, -50%);
            -o-transform: translate(-50%, -50%);
            transform: translate(-50%, -50%);
        }

        #div_clothing table, #ss_div table, #dz_div table {
            background-color: pink;
        }

        #div_clothing input, #ss_div input, #dz_div input {
            background-color: #eee;
        }
    </style>
    <link type="text/css" rel="stylesheet"
          href="/cbtconsole/css/web-ordetail.css"/>
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <style type="text/css">
        em {
            font-style: normal;
        }

        .orderInfo tr {
            border-bottom: 1px solid red;
        }
    </style>
    <script type="text/javascript">
        function FindTbOrder() {
            var Tborder=$("#Tborderus").val()
            window.location.href="/cbtconsole/Look/FindOtherOrder?Tborder="+Tborder
        }

        function returnOt() {
            var Tborder=$("#Tborderus").val()
            var returnNO=$("#openRt").val()
            var cusorder=1
            $.ajax({
                type: "post",
                url: "/cbtconsole/Look/AddRetAllOrder",
                data: {cusorder:cusorder,tbOrder:Tborder,returnNO:returnNO},
                success: function (res) {
                    if (res.rows == 1) {
                        $.messager.alert('提示', '操作成功');
                        window.location.reload();
                    } else {
                        $.messager.alert('提示', '操作失败');
                        window.location.reload();
                    }
                    $('#order_remark').window('close');
                    doQuery(page);
                }
            });
        }

        function returnOth() {
            var Tborder=$("#Tborderus").val()
            var returnNO=$("#openRt").val()
            var cusorder=1
            var OrderMap = new Array();
            $('input:checkbox[name=Split_open]').each(function(k) {
                if ($(this).is(':checked')) {
                    var tbid=$("#order"+k).text();
                    var retunum=$("#openNum"+k).val();
                    alert(retunum)
                    OrderMap.push({tbId:tbid,returnReason:returnNO,returnNumber:retunum})
                }
            })
            $.ajax({
                type: "post",
                url: "/cbtconsole/Look/AddOtherOrder",
                contentType : 'application/json;charset=utf-8',
                data:JSON.stringify(OrderMap),
                success: function (res) {
                    if (res.rows == 1) {
                        $.messager.alert('提示', '操作成功');
                        window.location.reload();
                    } else {
                        $.messager.alert('提示', '操作失败');
                        window.location.reload();
                    }
                    $('#order_remark').window('close');
                    doQuery(page);
                }
            });
        }
    </script>
</head>
<body>
<h2>特殊订单</h2>
1688订单号：<input type="text" id="Tborderus" value="${Tborder}">&nbsp;&nbsp;&nbsp;&nbsp; <button onclick="FindTbOrder()">查询</button><br/><br/><br/>

<tr>
    <td>
        <input type="button"
               style="position: fixed; bottom: 528px; right: 50px; width: 150px; height: 30px;" id="open"
               onclick="returnOt()" value="整单退货" >
    </td>
    <td>
        <input type="button"
               style="position: fixed; bottom: 458px; right: 50px; width: 150px; height: 30px;" id="openOt"
               onclick="returnOth()" value="部分退货" >
    </td>
    <td>
       <span style="position: fixed; bottom: 608px; right: 50px; width: 150px; height: 30px;">退货理由： <input type="text"
                                                                                                           id="openRt" value="错误订单">
           </span>
    </td>
</tr>
<div style="width:1666px;">
    <table id="orderDetail" class="ormtable2" align="center">
        <tbody>
        <tr class="detfretit">
            <td>淘宝id</td>
            <td>1688卖家信息</td>
            <td style="width: 200px;">1688订单号</td>
            <td>1688运单号</td>
            <td>产品名称</td>
            <td>产品pid</td>
            <td>产品数量</td>
            <td>产品sku</td>
            <td>产品图片</td>
            <td>下单时间</td>
            <td>签收时间</td>
            <td>操作</td>
        </tr>
        </tbody>
        <c:forEach items="${orderDetail}" var="order" varStatus="sd">
            <tr>
                <td id="order${sd.index}">${order.tbId}</td>
                <td>${order.sellerpeo}</td>
                <td>${order.a1688Order}</td>
                <td>${order.a1688Shipno }</td>
                <td><a href="https://www.importx.com/goodsinfo/122916001-121814002-1${order.item}.html" target="_blank">${order.itemname }</a></td>
                <td><a href="https://detail.1688.com/offer/${order.item}.html" target="_blank">${order.item}</a></td>
                <td>${order.itemNumber}</td>
                <td>${order.sku}</td>
                <td><img src="${order.imgurl }" alt=""></td>
                <td>${order.placeDate}</td>
                <td>${order.signtime}</td>
                <td><input name="Split_open" type="checkbox"><span>退货数量</span><input type="text" style="width: 20px" id="openNum${sd.index}"  value="${order.itemNumber}">
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
