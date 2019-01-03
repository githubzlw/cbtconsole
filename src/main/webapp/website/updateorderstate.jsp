<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.cbt.website.userAuth.bean.*"%>
<%@page import="com.cbt.processes.servlet.Currency"%>
<%@page import="java.util.List"%>
<%@page import="com.cbt.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="http://apps.bdimg.com/libs/jquery/1.6.4/jquery.js"></script>
<title>采购人员修改订单状态</title>
<script type="text/javascript">
var orderid;
$(function(){
	orderid='<%=request.getParameter("orderNo")%>';
<%-- 	var state='<%=request.getParameter("state")%>'; --%>
	state='<%=request.getParameter("state")%>';
	orderprice= '<%=request.getParameter("price")%>';
	document.getElementById("originstate").value=state;
 	document.getElementById("orderid").value=orderid;
});
function update(){
	var updatestate=$("#updatestate").val();
	if(updatestat == -1 || updatestat == 6){
	    var remark =prompt("取消原因?","请输入备注");
	    if(remark){
	        updateOrderState(orderid,updatestate,remark);
		}
	}else{
	    updateOrderState(orderid,updatestate,"");
	}
}

function updateOrderState(orderid,updatestate,remark) {
	$.post("/cbtconsole/UpdateOrderStateServlet",
			{orderid:orderid,updatestate:updatestate,remark:remark},
			function(res){
				if(res>0){
					alert('修改成功！(点击之后需要等3分钟才有效果)');
					window.close();
				}else{
					alert('修改失败,请确认是否符合修改规则或刷新页面后重试！');
				}
			});
}


function recoverCancelOrder(orderid) {
    var updatestate = $("#updatestate").val();
    if (updatestat == -1 || updatestat == 6 || updatestat == 0) {
        alert("恢复订单状态错误");
    } else {
        $.ajax({
            type: "POST",
            url: "/cbtconsole/order/recoverCancelOrder",
            data: {
                "orderNo": orderid,
                "state": updatestate
            },
            success: function (data) {
                if (data.ok) {
                    alert("执行成功");
                } else {
                    alert("执行错误:' + data.message");
                }
            },
            error: function (res) {
                alert("获取网络路径失败");
            }
        });
    }
}

function updatePrice(){
	var price=$("#price").val();//修改后的价格
	var price1=$("#price1").val();//当前订单应该付的钱数。
	var extra_discount=$("#extra_discount").val();
// 	alert(extra_discount);
// 	alert(price1);
// 	alert(price);
	if (state==0) {
	$.post("/cbtconsole/UpdateOrderPriceServlet",
			{orderid:orderid,price:price,extra_discount:extra_discount,price1:price1},
			function(res){
				if(res>0){
					alert('修改成功！(点击之后需要等3分钟才有效果)');
					window.close();
					}else{
					alert('修改失败！');
				}
			});
	} else {
		alert("Sorry,请选择未付款订单！");
	}
}


 function query(){
	window.location=" /cbtconsole/WebsiteServlet?action=queryPyaprice&className=OrderwsServlet&orderNo=<%=request.getParameter("orderNo")%>&state=<%=request.getParameter("state")%>"
 }
// $(document).ready(function(){
<%-- 	window.location=" /cbtconsole/WebsiteServlet?action=queryPyaprice&className=OrderwsServlet&orderNo=<%=request.getParameter("orderNo")%>" --%>
// })

// $(window).load(function () {
<%-- 	window.location=" /cbtconsole/WebsiteServlet?action=queryPyaprice&className=OrderwsServlet&orderNo=<%=request.getParameter("orderNo")%>" --%>
//  })


$(function(){
	$("#aa").click(function(){
		alert(3);
	});

})

</script>
<script type="text/javascript">
function queryOrderinfo(){
	var orderid = '<%=request.getParameter("orderNo")%>';
	var state='<%=request.getParameter("state")%>';
		    		$.ajax({
		    			url:"/cbtconsole/WebsiteServlet?action=queryPyaprice&className=OrderwsServlet&orderNo="+orderid,
		    			type:"post",
		    			dataType:"json",
		    			success:function(data){
		    				var data1 =eval(data);
		    				if(order<0){
		    					alert("查询失败！");
		    				} else {
		    					alert("查询成功！");
		    					console.log(data1);
		    					for(var i=0; i < data1.length; i++) { //循环后台传过来的Json数组
		    				}


		    				}
		    			}
		    		});
		    	}

</script>
<!-- <script type="text/javascript"> -->
<!-- 			queryOrderinfo(); -->
<!-- 			</script> -->
</head>
<!-- onload="query()" -->
<body>
<!-- <input id="aa" type="button" value="点击"/> -->
订单号：<input id="orderid" type="text" disabled="disabled"><br>
		<input type="hidden" value="<%=request.getParameter("orderNo")%>">
现在状态：<input id="originstate" type="text" disabled="disabled"><br>
		<input type="hidden" value="<%=request.getParameter("state")%>">
修改状态：<select id="updatestate">
            <% if(!("-1".equals(request.getParameter("state")) || "6".equals(request.getParameter("state")))){%>
                <option value="-1">-1</option>
            <%}%>
			<option value="0">0</option>
			<option value="1">1</option>
			<option value="2">2</option>
			<option value="3">3</option>
			<option value="4">4</option>
			<option value="5">5</option>
            <% if(!("-1".equals(request.getParameter("state")) || "6".equals(request.getParameter("state")))){%>
                <option value="6">6</option>
            <%}%>
		</select><input type="button" value="修改" onclick="update()"><br>
订单应付款金额：<input id="price" type="text" style="width:88px;height:15px" value="${price}">
			<input type="button" id="queryOrderinfo" onclick="query()" value="查询">
			<input type="hidden" id="productcost" value="${product_cost}">
			<input type="hidden" id="service_fee" value="${service_fee}">
			<input type="hidden" id="cashback" value="${cashback}">
			<input type="hidden" id="discount_amount" value="${discount_amount}">
			<input type="hidden" id="extra_freight" value="${extra_freight}">
			<input type="hidden" id="extra_discount" value="${extra_discount}">
			<input type="hidden" id="share_discount" value="${share_discount}">
			<input type="hidden" id="price1" value="${price}">
			<input type="button" value="修改" onclick="updatePrice()">
			&nbsp;&nbsp;<input type="button" value="恢复取消订单" onclick="recoverCancelOrder()">
			<br>
<!-- 			<script type="text/javascript"> -->
<!-- // 				queryOrderinfo(); -->
<!-- 			</script> -->
		状态对应的意义：0:等待付款;1:购买中;<br>
		2:已到仓库;3:出运中;4:完结<br>
		5:订单审核;6:取消订单;-1:已取消订单;<br>
		<span style="color:red">修改后请重新查询订单状态或者刷新订单管理首页，谢谢配合！</span>
</body>

</html>