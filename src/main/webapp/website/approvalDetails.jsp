<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>取消订单审批流程详情</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>

</head>
<body>
<h1 style="text-align: center">取消订单审批流程详情</h1>

<table style="border-color: #c689c7;" border="1" cellpadding="1" cellspacing="0" align="center">
    <caption>申请信息</caption>
    <tr>
        <td>申请ID:${approvalBean.id}</td>
        <td>申请类型:${approvalBean.typeDesc}</td>
        <td>申请金额:${approvalBean.payPrice}<em>USD</em></td>
        <td>申请时间:${approvalBean.createTime}</td>
    </tr>
    <tr>
        <td>客户ID:${approvalBean.userId}</td>
        <td>申请状态:${approvalBean.dealStateDesc}</td>
        <td>审批金额:${approvalBean.agreeAmount}<em>USD</em></td>
        <td>操作时间:${approvalBean.updateTime}</td>
    </tr>
</table>

<br><br>

<table style="border-color: #00ff43;" border="1" cellpadding="1" cellspacing="0" align="center">
    <caption>审批详情</caption>

    <thead>
    <tr align="center">
        <td style="width: 35px;">序号</td>
        <td style="width: 150px;">订单号</td>
        <td style="width: 80px;">审批金额</td>
        <td style="width: 80px;">审批状态</td>
        <td style="width: 80px;">退款方式</td>
        <td style="width: 60px;">操作人</td>
        <td style="width: 160px;">审批时间</td>
        <td style="width: 300px;">备注</td>

    </tr>
    </thead>
    <tbody>
    <c:forEach items="${approvalDetails}" var="approvalDt" varStatus="status">
        <tr bgcolor="${status.index % 2 ==0 ? '#fafafa':'#eaf2ff'}" style="height: 140px;">
            <td style="width: 35px;">${status.index + 1}</td>
            <td style="width: 150px;">${approvalDt.orderNo}</td>
            <td style="width: 80px;">${approvalDt.payPrice}<em>USD</em></td>
            <td style="width: 80px;">${approvalDt.dealStateDesc}</td>
            <td style="width: 80px;">${approvalDt.refundMethodDesc}</td>
            <td style="width: 60px;">${approvalDt.adminName}</td>
            <td style="width: 160px;">${approvalDt.createTime}</td>
            <td style="width: 300px;">${approvalDt.remark}</td>
        </tr>
    </c:forEach>

    </tbody>

</table>

</body>
</html>
