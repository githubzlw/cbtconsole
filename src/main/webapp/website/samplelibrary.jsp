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
    <title>送样管理</title>
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
        function serach(){
            var pid = $("#pid").val();
            var pids=pid.split(",")
            if (pids.length>3){
                alert("最多输入3pid")
            }
           window.location.href="/cbtconsole/orderSplit/deliverOrder?orderno=${orderno}&&pid="+pid+"&&userid=${userid}";
        }
        function deliver(od) {

            // var Tborder=$("#Tborderus").val()
            // var returnNO=$("#openRt").val()
            var sku=new Array()
            // sku= $('input:radio:checked').val();
            /*sku= $("input[type='radio']:checked").val();
           for (var i=0;i<sku.length;i++){

           }*/

            // var arr = [];
            // var color_spc="";

            // $('.col_spc input:checked').each(function(){
            //     var this_val = $(this).val();
            //     arr.push(this_val);
            //     color_spc = arr.toString();
            //     console.log(color_spc);
            // })

            var cusorder=${odls}
            var OrderMap = new Array();
            var userid=${userid}
            $('input:checkbox[name=odCount]').each(function(k) {
                var arr = [];
                var color_spc="";
                $('.col_spc'+k+' input:checked').each(function(){
                    var this_val = $(this).val();
                    arr.push(this_val);
                    color_spc = arr.toString();
                    console.log(color_spc);
                })
                if ($(this).is(':checked')) {
                    var retunum=$("#openNum"+k).val();
                    cusorder[k].new_barcode=retunum
                    OrderMap.push({userId:userid,orderNo:od,pid:cusorder[k].goods_pid,imgUrl:cusorder[k].car_img,skuId:cusorder[k].sku,enType:color_spc,isChoose:1,
                        goodsNum:retunum,enName:cusorder[k].good_name,catid:cusorder[k].catid1,weight:cusorder[k].weight,volumeWeight:cusorder[k].volume_weight,isSoldFlag:cusorder[k].is_sold_flag})
                }
            })
            $.ajax({
                type: "post",
                url: "/cbtconsole/orderSplit/saveNewOrder",
                contentType : 'application/json;charset=utf-8',
                data:JSON.stringify(OrderMap),
                success: function (res) {
                    if (res == 1) {
                        alert('操作成功');
                        // window.location.reload();
                        window.close();
                    } else {
                        alert('操作失败');
                        window.location.reload();
                    }
                }
            });
        }
    </script>
</head>
<body>
<h2>送样管理</h2>
商品pid：<input id="pid" type="text" value="">&nbsp;&nbsp;&nbsp;&nbsp; <button onclick="serach('${orderno}')">查询</button>多个商品用逗号隔开<br/><br/><br/>

<tr>
    <td>
        <input type="button"
               style="position: fixed; bottom: 528px; right: 50px; width: 150px; height: 30px;" id="open"
               onclick="deliver('${orderno}')" value="送样">
    </td>
</tr>
<div style="width:1233px;">
<table id="orderDetail" class="ormtable2" align="center">
    <tbody>
<tr class="detfretit">
    <td>商品编号</td>
    <td>商品图片</td>
    <td style="width: 200px;">商品名称</td>
    <td>库存数量</td>
    <td>单价</td>
    <td>货源</td>
    <td>操作</td>
</tr>
    </tbody>
<c:forEach items="${orderDetail}" var="orderd" varStatus="sd">
    <tr>
<td>${orderd.goods_pid}</td>
<td><img  src="${orderd.car_img}" height="200" width="200" alt=""><br/><a target="_blank" href="https://detail.1688.com/offer/${orderd.goods_pid}.html">商品货源链接</a></td>
        <td style="font-size: 18px"><a target="_blank" target="_blank" href="https://www.import-express.com/goodsinfo/cbtconsole-1${orderd.goods_pid}.html" >${orderd.good_name}</a></td>
<td>${orderd.can_remaining}</td>
<td>${orderd.goodsprice}</td>
<td class="col_spc${sd.index}">
    <c:forEach items="${orderd.skuList}" var="map">
        <span ><c:out value="${map.key}:">${map.key}:</c:out>
        <c:forEach items="${map.value}" var="sk">
            <%--<span><c:out value="${sk}"></c:out></span>--%>
            <label><input name="${map.key}${sd.index}" type="radio" value="${map.key}:${sk}" />${sk}</label>
        </c:forEach>
            </span><br><br>
    </c:forEach>
    </td>
<td><input name="odCount" type="checkbox"><span>送样.数量</span><input type="text" style="width: 20px" id="openNum${sd.index}"  value="1"></td>
    </tr>
</c:forEach>
</table>
</div>
</body>
</html>
