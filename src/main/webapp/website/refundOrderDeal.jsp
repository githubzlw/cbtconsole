<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>客户订单交易详情</title>
</head>
<body>

<h1 style="text-align: center">订单交易详情</h1>

<table style="border-color: #c689c7;" border="1" cellpadding="1" cellspacing="0" align="center">
    <caption>订单信息</caption>
    <thead>
    <tr align="center">
        <td style="width: 190px;">订单号</td>
        <td style="width: 90px;">订单状态</td>
        <td style="width: 70px;">货币单位</td>
        <td style="width: 70px;">支付金额</td>
        <td style="width: 70px;">商品总额</td>
        <td style="width: 100px;">coupon折扣</td>
        <td style="width: 70px;">额外运费</td>
        <td style="width: 70px;">折扣金额</td>
        <td style="width: 50px;">返现</td>
        <td style="width: 70px;">国际运费</td>
        <td style="width: 70px;">额外折扣</td>
        <td style="width: 70px;">运费折扣</td>
        <td style="width: 180px;">创建时间</td>

    </tr>
    </thead>
    <tbody>
    <c:forEach items="${orderList }" var="orderInfo" varStatus="status">
        <tr bgcolor="${status.index % 2 ==0 ? '#fafafa':'#eaf2ff'}">
            <td> ${orderInfo.orderNo}</td>
            <td> ${orderInfo.stateDesc}</td>
            <td> ${orderInfo.currency}</td>
            <td> ${orderInfo.pay_price}</td>
            <td> ${orderInfo.product_cost}</td>
            <td> ${orderInfo.coupon_discount}</td>
            <td> ${orderInfo.extra_freight}</td>
            <td> ${orderInfo.discount_amount}</td>
            <td> ${orderInfo.cashback}</td>
            <td> ${orderInfo.foreign_freight}</td>
            <td> ${orderInfo.extra_freight}</td>
            <td> ${orderInfo.order_ac}</td>
            <td> ${orderInfo.createtime}</td>
        </tr>
    </c:forEach>

    </tbody>
</table>

<br><br>

<table style="border-color: #00ff43;" border="1" cellpadding="1" cellspacing="0" align="center">
    <caption>支付信息</caption>

    <thead>
    <tr align="center">
        <td style="width: 150px;">订单号</td>
        <td style="width: 100px;">支付类型</td>
        <td style="width: 80px;">支付金额</td>
        <td style="width: 80px;">货币单位</td>
        <td style="width: 120px;">交易号</td>
        <td style="width: 180px;">支付时间</td>


    </tr>
    </thead>
    <tbody>
    <c:forEach items="${paymentList}" var="payment" varStatus="status">
        <tr bgcolor="${status.index % 2 ==0 ? '#fafafa':'#eaf2ff'}">
            <td> ${payment.orderNo}</td>
            <td> ${payment.payTypeDesc}</td>
            <td> ${payment.paymentAmount}</td>
            <td> ${payment.currency}</td>
            <td> ${payment.paymentNo}</td>
            <td> ${payment.paymentTime}</td>
        </tr>
    </c:forEach>

    </tbody>

</table>
</body>
</html>
