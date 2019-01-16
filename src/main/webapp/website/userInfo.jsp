<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>客户详情页</title>
    <link type="text/css" rel="stylesheet" href="/cbtconsole/css/web-ordetail.css"/>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <style type="text/css">

        .tr_sty {
            text-align: center;
            font-weight: bold;
            font-size: 16px;
        }

        .td_right {
            text-align: right;
            width: 50%;
        }

        .td_left span {
            text-align: left;
            font-size: 16px;
        }

        /*#table1 {
            margin-left: 36%;
        }*/
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
</head>
<body>

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

<script type="text/javascript">
    //删除用户备注
    function deleteUserRemark(id) {
        $.messager.confirm('提示','你确定要删除该条备注吗?',function(r){
            if(r){
                $.ajax({
                    type: "GET",
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
            type: "GET",
            url: "/cbtconsole/userinfo/queryUserRemark.do",
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
            type: "GET",
            url: "/cbtconsole/userinfo/addUserRemark.do",
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
</script>
<input type="hidden" id="userid" value="${userId}">
<div class="useindiv">
    <table class="ormatable usrintable">
        <tr>
            <td class="ornmatd1">
                <span class="ormtittd">客户ID:<a href="/cbtconsole/website/paycheck_new.jsp?userid=${user.id}" title="点击进入客户到账情况" target="_blank">${user.id}</a></span></td>
            <td><span class="ormtittd">客户邮箱:${user.email }</td>
            <td><span class="ormtittd">订单数量:${user.ordercount }</td>
            <td><span class="ormtittd">购物车数量:</span><a class="ordmlink"
                                                       href="/cbtconsole/website/shoppingCarProductPush.jsp?userid=${user.id }">${user.carcount }</a>
            </td>
        </tr>
        <tr>
            <td><span class="ormtittd">用户余额:</span><span
                    class="ormtittdred">${user.available_m } ${user.currency}</span></td>
            <td><span class="ormtittd">赠送运费余额:</span><span
                    class="ormtittdred">${user.applicable_credit } ${user.currency}</span></td>
            <td><span class="ormtittd">paypal账号:</span>
                <c:forEach items="${paypays}" var="paypal">
                    ${paypal},
                </c:forEach>
            </td>
            <td><span class="ormtittd">otheremail:${userex.otheremail }</td>
        </tr>
        <tr>
            <td><span class="ormtittd">otherphone:${userex.otherphone }</td>
            <td><span class="ormtittd">whatsapp:${userex.whatsapp }</td>
            <td><span class="ormtittd">facebook:${userex.facebook }</td>
            <td><span class="ormtittd">Twitter:${userex.tweater }</td>
        </tr>
        <tr>
            <td><span class="ormtittd">kiki:${userex.kiki }</td>
            <td><span class="ormtittd">skype:${userex.skype }</td>
            <td>
                <c:if test="${backList>0}">
                    <span style="color:Red">用户黑名单&nbsp;&nbsp;</span>
                </c:if>
                <c:if test="${payBackList>0}">
                    <span style="color:Red">支付账号黑名单&nbsp;&nbsp;</span>
                </c:if>
                <c:if test="${backAddressCount>0}">
                    <span style="color:Red">订单城市黑名单</span>
                </c:if>
            </td>
            <td><button href="#" onclick="showRemark('${user.id}')">备注</button></td>
        </tr>
    </table>

    <div id="orderaddressdiv" class="useiadd">
        <h3>订单地址:&nbsp;&nbsp;</h3>
        <table class="usrintable useorstab ormatable">
            <tr class="useinatit">
                <td>Recipients</td>
                <td>City</td>
                <td>Street</td>
                <td>State</td>
                <td>Country</td>
                <td>Zip Code</td>
                <td>Phone</td>
            </tr>
            <c:forEach items="${addresslist}" var="address">
                <tr>
                    <td>${address.recipients}</td>
                    <td>${address.address2}</td>
                    <td>${address.address},${street}</td>
                    <td>${address.statename}</td>
                    <td>${address.countryname}</td>
                    <td>${address.zip_code}</td>
                    <td>${address.phone_number}</td>
                </tr>
            </c:forEach>
        </table>
        <br>

        <div>

            <div style="float: left;width: 27%;">

                <c:set value="${payment.balance==payment.currencyBalance }" var="banceFlag"></c:set>

                <c:if test="${financial.warnFlag > 0}">
                    <b style="font-size: 18px;color: red;">${financial.warnMsg}</b>
                </c:if>
                <table id="table1" class="useorstab ormatable">
                    <caption class="useindohead">账户汇总(单位$)</caption>
                    <tr>
                        <td colspan="2" class="tr_sty">订单金额</td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">下订单总金额:</td>
                        <td colspan="1" class="td_left"><span> ${financial.totalOrderAmount}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">实际完成订单金额:</td>
                        <td colspan="1" class="td_left"><span> ${financial.actualOrderAmount}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">被取消的订单金额:</td>
                        <td colspan="1" class="td_left"><span> ${financial.cancelOrderAmount}</span>
                        </td>
                    </tr>


                    <tr>
                        <td colspan="2" class="tr_sty">支付金额</td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">总的实际到账金额:</td>
                        <td colspan="1" class="td_left"><span> ${financial.actualPayAmount}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">Paypal:</td>
                        <td colspan="1" class="td_left"><span> ${financial.payPalPay}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">Wire Transfer:</td>
                        <td colspan="1" class="td_left"><span> ${financial.wireTransferPay}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">余额支付金额:</td>
                        <td colspan="1" class="td_left"><span> ${financial.balancePay}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">stripePay支付金额:</td>
                        <td colspan="1" class="td_left"><span> ${financial.stripePay}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">充值:</td>
                        <td colspan="1" class="td_left"><span> ${financial.payForBalance}</span>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="tr_sty">补偿和余额</td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">人为额外奖励或补偿:</td>
                        <td colspan="1" class="td_left"><span> ${financial.compensateAmount}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">账号应有余额(算法一)：</td>
                        <td colspan="1" class="td_left"><span> ${financial.dueBalance}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">账号应有余额(算法二)：</td>
                        <td colspan="1" class="td_left"><span> ${financial.dueBalance2}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">强制平账余额:</td>
                        <td colspan="1" class="td_left"><span> ${financial.balanceCorrection}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">客户在我司账户实际余额:</td>
                        <td colspan="1" class="td_left"><span> ${financial.currentBalance}</span>
                        </td>
                    </tr>


                    <tr>
                        <td colspan="2" class="tr_sty">提现或退款</td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">申请中的 提现的金额:</td>
                        <td colspan="1" class="td_left"><span> ${financial.dealRefundAmount}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">实际发放的 退款或提现:</td>
                        <td colspan="1" class="td_left"><span> ${financial.hasRefundAmount}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">系统API退款：</td>
                        <td colspan="1" class="td_left"><span> ${financial.apiRefundAmount}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">人工退款:</td>
                        <td colspan="1" class="td_left"><span> ${financial.manualRefundAmount}</span>
                        </td>
                    </tr>

                    <tr>
                        <td colspan="2" class="tr_sty">申诉情况</td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">申诉处理中:</td>
                        <td colspan="1" class="td_left"><span> ${financial.dealRefundAmount}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">申诉被取消：</td>
                        <td colspan="1" class="td_left"><span> ${financial.cancelRefundAmount}</span>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="1" class="td_right">申诉完结：</td>
                        <td colspan="1" class="td_left"><span> ${financial.hasRefundAmount}</span>
                        </td>
                    </tr>


                </table>

            </div>

            <div style="float: left;width: 72%;">

                <p>
                    <span>算法1：账号应有余额=总的实际到账金额-总的实际完成订单金额+额外奖励或补偿-申请退款</span>
                    <br>
                    <span>算法2：账号应有余额=被取消的订单金额-余额支付的金额+额外奖励或补偿-申请退款+充值</span>
                </p>
                <table id="order_list_tb" border="1" cellpadding="0" cellspacing="0">
                    <caption class="useindohead">订单列表</caption>
                    <thead>
                    <tr class="useinatit">
                        <td style="width: 190px;">订单号(点击查看订单详情)</td>
                        <td style="width: 90px;">订单状态</td>
                        <td style="width: 60px;">货币单位</td>
                        <td style="width: 70px;">支付金额</td>
                        <td style="width: 70px;">商品总额</td>
                        <td style="width: 90px;">coupon折扣</td>
                        <td style="width: 70px;">额外运费</td>
                        <td style="width: 70px;">折扣金额</td>
                        <td style="width: 50px;">返现</td>
                        <td style="width: 70px;">国际运费</td>
                        <td style="width: 70px;">额外折扣</td>
                        <td style="width: 70px;">运费折扣</td>
                        <td style="width: 180px;">创建时间</td>
                        <td style="width: 190px;">到账详情</td>
                    </tr>
                    </thead>
                    <tbody>
                   <c:forEach items="${orderList }" var="orderInfo" >
                        <tr>
                            <td><a href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo=${orderInfo.orderNo}" title="订单详情" target="_blank">${orderInfo.orderNo}</a></td>
                            <td> ${orderInfo.stateDesc}</td>
                            <td> ${orderInfo.currency}</td>
                            <td> ${orderInfo.pay_price}</td>
                            <td> ${orderInfo.product_cost}</td>
                            <td> ${orderInfo.coupon_discount}</td>
                            <td> ${orderInfo.extra_freight}</td>
                            <td> ${orderInfo.discount_amount}</td>
                            <td> ${orderInfo.cashback}</td>
                            <td> ${orderInfo.foreign_freight}</td>
                            <td> ${orderInfo.extra_discount}</td>
                            <td> ${orderInfo.order_ac}</td>
                            <td> ${orderInfo.createtime}</td>
                            <td><a href="/cbtconsole/website/paycheck_details_new.jsp?orderNo=${orderInfo.orderNo}" title="到账详情" target="_blank">${orderInfo.orderNo}</a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <c:if test="${not empty rechangeList}">
                    <br><br>
                    <table id="user_balance_change" border="1" cellpadding="0" cellspacing="0">
                        <caption class="useindohead">余额变更记录[余额总收入:${balanceTotalRevenue};补偿总收入:${compensationTotalRevenue};余额总支出:${balanceTotalSpending}]</caption>
                        <thead>
                        <tr class="useinatit">
                            <td style="width: 80px;">金额</td>
                            <td style="width: 120px;">备注ID</td>
                            <td style="width: 180px;">创建时间</td>
                            <td style="width: 80px;">操作人</td>
                            <td style="width: 80px;">进账标识</td>
                            <td style="width: 80px;">货币单位</td>
                            <td style="width: 100px;">操作后余额</td>
                            <td style="width: 200px;">备注</td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${rechangeList }" var="rerd" >
                            <tr>
                                <td> ${rerd.price}</td>
                                <td> ${rerd.remarkId}</td>
                                <td> ${rerd.dataTime}</td>
                                <td> ${rerd.adminUser}</td>
                                <td> ${rerd.useSignDesc}</td>
                                <td> ${rerd.currency}</td>
                                <td> ${rerd.balanceAfter}</td>
                                <td> ${rerd.remark}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>


                <c:if test="${not empty addBlList}">
                    <br><br>
                    <table id="add_user_balance" border="1" cellpadding="0" cellspacing="0">
                        <caption class="useindohead">余额补偿记录</caption>
                        <thead>
                        <tr class="useinatit">
                            <td style="width: 80px;">金额</td>
                            <td style="width: 80px;">操作人</td>
                            <td style="width: 180px;">创建时间</td>
                            <td style="width: 360px;">备注</td>

                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${addBlList }" var="adb" >
                            <tr>
                                <td> ${adb.money}</td>
                                <td> ${adb.admin}</td>
                                <td> ${adb.createtime}</td>
                                <td> ${adb.remark}</td>

                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>


                <c:if test="${not empty refundList}">
                    <br><br>
                    <table id="user_refund" border="1" cellpadding="0" cellspacing="0">
                        <caption class="useindohead">退款记录</caption>
                        <thead class="useinatit">
                        <tr>
                            <td style="width: 80px;">申请金额</td>
                            <td style="width: 90px;">申请时间</td>
                            <td style="width: 70px;">金额单位</td>
                            <td style="width: 120px;">关联订单号</td>
                            <td style="width: 70px;">状态</td>
                            <td style="width: 90px;">同意时间</td>
                            <td style="width: 70px;">退款金额</td>
                            <td style="width: 90px;">结束时间</td>
                            <td style="width: 200px;">备注</td>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${refundList }" var="rfd" >
                            <tr>
                                <td> ${rfd.appcount}</td>
                                <td> ${rfd.apptime}</td>
                                <td> ${rfd.currency}</td>
                                <td> ${rfd.orderid}</td>
                                <td> ${rfd.statusDesc}</td>
                                <td> ${rfd.agreetime}</td>
                                <td> ${rfd.account}</td>
                                <td> ${rfd.endtime}</td>
                                <td> ${rfd.remark}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>

            </div>


            <%-- <div>I1实际下单总金额-O总的实际完成订单金额+E额外补偿或奖励金额 =B期末余额=B1客户在我司账户实际余额</div>
            ${payment.paymentAll }-${payment.orderPriceAll}+${payment.addBalance } = ${payment.balance }=${payment.currencyBalance }
            <div>I1实际下单总金额-R总的退款金额 =O总的实际完成订单金额</div>
            ${payment.paymentAll }-${payment.refund }=${payment.orderPriceAll} --%>
        </div>

        <br><br><br><br><br>
        <div class="payDetail">
            <table id="table6" border="1" cellpadding="0" cellspacing="0" class="usrintable useorstab ormatable"
                   style="table-layout:fixed;">
                <caption class="useindohead">收支明细</caption>
                <tr class="useinatit">
                    <td>时间</td>
                    <td>类型</td>
                    <td>收入</td>
                    <td>支出</td>
                    <td>备注</td>
                </tr>
                <c:forEach items="${payDetailList }" var="payDetail">
                    <tr>
                        <td style="widh:300px;">${payDetail.createtime}</td>
                        <td style="widh:200px;">
                            <c:if test="${payDetail.type=='0' }">PayPal进账</c:if>
                            <c:if test="${payDetail.type=='1' }">Wire Transfer进账</c:if>
                            <c:if test="${payDetail.type=='2' }">奖励或补偿</c:if>
                            <c:if test="${payDetail.type=='3' }">订单消费</c:if>
                            <c:if test="${payDetail.type=='4' }">已退款</c:if>
                            <c:if test="${payDetail.type=='5' }">Stripe进账</c:if>
                            <c:if test="${payDetail.type=='6' }">申诉处理中</c:if>
                            <c:if test="${payDetail.type=='7' }">提现处理中</c:if>
                            <c:if test="${payDetail.type=='8' }">余额支付</c:if>
                            <c:if test="${payDetail.type=='9' }">余额增加</c:if>
                        </td>
                        <td style="widh:250px;">${payDetail.money_in }</td>
                        <td style="widh:250px;">${payDetail.money_out }</td>
                        <td style="widh:500px;word-break:break-all;word-wrap:break-word;">${payDetail.remark} </td>
                    </tr>

                </c:forEach>
                <tr>
                    <td colspan="5" style="font-size: 16px;">
                        <span>收入</span>
                        <span class="span_split">&nbsp;</span>
                        <span>USD:${sumMap.USD_in }
                            <span class="span_split">&nbsp;</span>
                            AUD:${sumMap.AUD_in }
                            <span class="span_split">&nbsp;</span>
                            GBP:${sumMap.GBP_in}
                            <span class="span_split">&nbsp;</span>
                            EUR:${sumMap.EUR_in }
                            <span class="span_split">&nbsp;</span>
                            CAD:${sumMap.CAD_in }</span>
                        <span class="span_split">&nbsp;</span>
                        <span class="span_split">&nbsp;</span>
                        |
                        <span class="span_split">&nbsp;</span>
                        <span class="span_split">&nbsp;</span>
                        <span>支出</span>
                        <span class="span_split">&nbsp;</span>
                        <span>
                            USD:${sumMap.USD_out }
                            <span class="span_split">&nbsp;</span>
                            AUD:${sumMap.AUD_out }
                            <span class="span_split">&nbsp;</span>
                            GBP:${sumMap.GBP_out}
                            <span class="span_split">&nbsp;</span>
                            EUR:${sumMap.EUR_out }
                            <span class="span_split">&nbsp;</span>
                            CAD:${sumMap.CAD_out }</span>
                    </td>
                </tr>
            </table>
            <div>
                <input type="hidden" id="payDetailtotalpage" value="${payDetailpagecount}">

                总共:&nbsp;&nbsp;<span id="payDetailtotal"><em
                    id="payDetailpagenow">${payDetailpagenow}</em><em>/</em> ${payDetailpagecount}</span>
                页&nbsp;&nbsp;
                <input type="button" value="上一页" onclick="fnpayDetailjump(-1)" class="btn">
                <input type="button" value="下一页" onclick="fnpayDetailjump(1)" class="btn">

                第<input id="payDetailpage" type="text" value="${payDetailpagenow}" style="height: 26px;">
                <input type="button" value="查询" onclick="fnpayDetailjump(0)" class="btn">
            </div>

        </div>
        <br><br>
    </div>
    <div>
        <div id="orderpaytable" style="display:none;">
            <div style="font-size: 18px;">I1订单总金额 - R订单产品取消 = O订单完成金额</div>
            <table id="table5" border="1" cellpadding="0" cellspacing="0" class="usrintable useorstab ormatable">
                <tr class="useinatit">
                    <td>Date</td>
                    <td>Order#</td>
                    <td>I1订单总金额<br>(实际下单总金额含余额支付)</td>
                    <td>I订单到账金额<br>(仅PayPal+Transfer)</td>
                    <td>R订单产品取消($)</td>
                    <td>E额外奖励或补偿($)</td>
                    <td>O订单完成金额($)</td>
                    <td>paypal申诉($)</td>
                    <!-- <td>B余额($)</td> -->
                </tr>
                <c:if test="${payment.addBalance !='0.00' }">
                    <tr>
                        <td></td>
                        <td></td>
                        <td>0.00</td>
                        <td>0.00</td>
                        <td>0.00</td>
                        <td>${payment.addBalance}</td>
                        <td>0.00</td>
                        <td>0.00</td>
                        <!-- <td></td> -->
                    </tr>
                </c:if>

                <c:forEach items="${orderPayList }" var="orderpay">
                    <tr class="${orderpay.isBalance==1? 'style_red':'style_blank' }">
                        <td>${orderpay.dateTime }</td>
                        <td>${orderpay.orderNo }</td>
                        <td>${orderpay.paymentAmount }</td>
                        <td>${orderpay.payment }</td>
                        <td>-${orderpay.cancelOrder }</td>
                        <td>${orderpay.additionalBalance }</td>
                        <td>${orderpay.payPrice }</td>
                        <td>${orderpay.refund }</td>
                            <%-- <td>${orderpay.balance }</td> --%>
                    </tr>
                </c:forEach>

            </table>
            <div>
                <input type="hidden" id="orderPaytotalpage" value="${orderPaypagecount}">

                总共:&nbsp;&nbsp;<span id="orderPaytotal"><em
                    id="orderPaypagenow">${orderPaypagenow}</em><em>/</em> ${orderPaypagecount}</span>
                页&nbsp;&nbsp;
                <input type="button" value="上一页" onclick="fnorderPayjump(-1)" class="btn">
                <input type="button" value="下一页" onclick="fnorderPayjump(1)" class="btn">

                第<input id="orderPaypage" type="text" value="${orderPaypagenow}" style="height: 26px;">
                <input type="button" value="查询" onclick="fnorderPayjump(0)" class="btn">
            </div>


        </div>

        <c:if test="${payment.addBalance !='0.00' }">
            <div id="additionaltable" style="display:none;">

                <table id="table7" border="1" cellpadding="0" cellspacing="0" class="usrintable useorstab ormatable">
                    <caption class="useindohead">余额补偿与奖励记录</caption>
                    <tr class="useinatit">
                        <td style="width: 135px;">Date</td>
                        <td style="width: 235px;">Order#</td>
                        <td style="width: 100px;">Price</td>
                        <td style="width: 100px;">Admin</td>
                        <td style="width: 929px;">Remark</td>
                    </tr>

                    <c:forEach items="${additionalList}" var="additional">
                        <tr>
                            <td>${additional.createTime }</td>
                            <td>${additional.orderid }</td>
                            <td>${additional.money }</td>
                            <td>${additional.admin }</td>
                            <td>${additional.remark }</td>
                        </tr>
                    </c:forEach>
                </table>
                <div>
                    <input type="hidden" id="additionaltotalpage" value="${additionalpagecount}">

                    总共:&nbsp;&nbsp;<span id="additionalpagetotal"><em
                        id="additionalpagenow">${additionalpagenow}</em><em>/</em> ${additionalpagecount}</span>
                    页&nbsp;&nbsp;
                    <input type="button" value="上一页" onclick="fnadditionaljump(-1)" class="btn">
                    <input type="button" value="下一页" onclick="fnadditionaljump(1)" class="btn">

                    第<input id="additionalpage" type="text" value="${additionalpagenow}" style="height: 26px;">
                    <input type="button" value="查询" onclick="fnadditionaljump(0)" class="btn">
                </div>
            </div>
        </c:if>

        <div class="useiadd" id="balancetable" style="display:none;">
            <c:if test="${pagecount>0 }">
                <table id="table4" border="1" cellpadding="0" cellspacing="0" class="usrintable useorstab ormatable">
                    <caption class="useindohead">用户余额变更记录</caption>
                    <tr class="useinatit">
                        <td style="width:236px;">Date</td>
                        <td style="width:315px;">Order#</td>
                        <td style="width:100px;">Price</td>
                        <td style="width:100px;">Action</td>
                        <td style="width:550px;">Transaction description</td>
                        <td style="width:100px;">Balance</td>
                        <td style="width:98px;">Currency</td>
                        <!-- <td>Administrator</td> -->
                    </tr>
                    <c:forEach items="${recordList }" var="record" varStatus="step">
                        <tr class="autotr">
                            <td>${record.datatime }</td>
                            <td>
                                <c:if test="${record.remark_id!=null && fn:length(record.remark_id) >10}">
                                    <c:forEach items="${fn:split(record.remark_id,',') }" var="orderno">
                                        ${orderno}
                                        <br>
                                        <a target='_blank' class="ordmlink"
                                           href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo=${orderno}&state=&username=&paytime=">订单详情</a>
                                        <a target='_blank' class="ordmlink"
                                           href="/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid=999&orderno=${orderno}&days=365&unpaid=0&pagesize=50&orderarrs=0&search_state=0">采购详情</a>
                                        <a target='_blank' class="ordmlink"
                                           href="/cbtconsole/website/forwarderpageplck.jsp?orderid=${orderno}">出货详情</a>
                                        <br>
                                    </c:forEach>
                                </c:if>
                            </td>
                            <td>${record.price }</td>
                            <td><c:if test="${record.type==0}"></c:if>
                                <c:if test="${record.type==1}">取消订单</c:if>
                                <c:if test="${record.type==2}">多余金额</c:if>
                                <c:if test="${record.type==3}">重复支付</c:if>
                                <c:if test="${record.type==4}">手动修改</c:if>
                                <c:if test="${record.type==5}">其他</c:if>
                                <c:if test="${record.type==7}">余额抵扣</c:if>
                                <c:if test="${record.type==8}">申请提现</c:if>
                                <c:if test="${record.type==9}">提现取消</c:if>
                                <c:if test="${record.type==10}">提现拒绝</c:if></td>
                            <td>${record.remark }</td>
                            <td>${record.balanceAfter }</td>
                            <td>${record.currency }</td>
                                <%-- <td>${record.adminUser }</td> --%>
                        </tr>
                    </c:forEach>
                </table>
                <br>
                <br>
                <div>
                    <input type="hidden" id="totalpage" value="${pagecount}">

                    总共:&nbsp;&nbsp;<span id="pagetotal"><em id="pagenow">${pagenow}</em><em>/</em> ${pagecount}</span>
                    页&nbsp;&nbsp;
                    <input type="button" value="上一页" onclick="fnjump(-1)" class="btn">
                    <input type="button" value="下一页" onclick="fnjump(1)" class="btn">

                    第<input id="page" type="text" value="${pagenow}" style="height: 26px;">
                    <input type="button" value="查询" onclick="fnjump(0)" class="btn">
                </div>
                <br><br>
                <br><br>

            </c:if>

            <%-- <table class="usrintable useorstab ormatable">
                <caption class="useindohead">用户消费记录</caption>
                <thead>
                    <tr class="useinatit">
                    <td>Date</td>
                    <td>Order#</td>
                    <td>Transaction description</td>
                    <td>Balance</td>
                    </tr>
                </thead>
                <tbody id="trRecords">
                    <c:forEach items="${recordsList }" var="record">
                        <tr>
                                <td>${record.datatime }</td>
                        <c:choose>
                            <c:when test="${record.remark_id!=null}">
                                <td>
                                    <c:forEach items="${fn:split(record.remark_id,',') }" var="orderno">
                                    ${orderno}
                                    <a target='_blank' class="ordmlink" href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo=${orderno}&state=&username=&paytime=">订单详情</a>
                                    <a target='_blank' class="ordmlink" href="/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid=999&orderno=${orderno}&days=365&unpaid=0&pagesize=50&orderarrs=0&search_state=0">采购详情</a>
                                    <a target='_blank' class="ordmlink" href="/cbtconsole/website/forwarderpageplck.jsp?orderid=${orderno}">出货详情</a>
                                    <br>
                                    </c:forEach>
                                </td>
                            </c:when>
                            查询数据中没有包含该字段属性porderid
                            <c:otherwise>
                                <td>
                                <c:forEach items="${fn:split(record.porderid,',') }" var="orderno">
                                    ${orderno}
                                    <a target='_blank' class="ordmlink" href="/cbtconsole/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo=${orderno}&state=&username=&paytime=">订单详情</a>
                                    <a target='_blank' class="ordmlink" href="/cbtconsole/PurchaseServlet?action=getPurchaseByXXX&className=Purchase&pagenum=1&orderid=0&admid=1&userid=&orderno=${orderno}&goodid=&date=&days=&state=&unpaid=0&pagesize=50">采购详情</a>
                                    <a target='_blank' class="ordmlink" href="/cbtconsole/cbtconsole/warehouse/getOrderInfoInspection.do?userid=&orderid=${orderno}&pageSize=50&orderstruts=1">出货详情</a>
                                    <br>
                                    </c:forEach>
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <c:choose>
                            <c:when test="${record.type!=null }">
                                <c:choose>
                                <c:when test="${record.type==7 }">
                                 <c:choose>
                                  <c:when test="${record.paytype!=null and record.paytype==0 }">
                                      <td>
                                        Paid ${record.currencyshow }${record.price} for goods(PayPal Transaction No: ${record.paymentId })
                                    </td>
                                  </c:when>
                                  <c:otherwise>
                                    <td>
                                        Paid ${record.currencyshow }${record.price} for goods(Used Balance)
                                    </td>
                                  </c:otherwise>
                                </c:choose>
                                </c:when>
                                <c:when test="${record.type==1 }">
                                    <td>Refund ${record.currencyshow }${record.price} to your account, (order cancelled)</td>
                                </c:when>
                                </c:choose>
                            </c:when>
                        </c:choose>
                            <td>
                                ${record.afterBalanceshow }
                            </td>
                        </tr>
                    </c:forEach>


                </tbody>
                <tfoot>
                        <c:forEach items="${refundList}" var="refund">
                        <tr>
                            <td>${refund.rdatatime}</td>
                            <td>N/A</td>
                            <td>
                                Withdrawing ${refund.currencyshow }${refund.rprice}
                                <c:choose>
                                <c:when test="${refund.agreeapp==0 &&refund.iscancle!=1 &&refund.isend!=1}">
                                    <br/>
                                    <span style="color:red">(under review)</span>
                                </c:when>
                                <c:when test="${refund.agreeapp==2 }">
                                    <br/>
                                    <span style="color:red">(Withdraw Request Rejected)</span>
                                </c:when>
                                <c:when test="${refund.iscancle==1 }">
                                    <br/>
                                    <span style="color:red">(Has canceled)</span>
                                </c:when>
                                </c:choose>
                            </td>
                            <td>
                                ${refund.currencyshow }${refund.rbalanceAfter }
                            </td>
                        </tr>
                    </c:forEach>
                </tfoot>
            </table> --%>
        </div>
    </div>
</div>
</body>
<script type="text/javascript">
    function fnjump(obj) {
        var page = $("#page").val();
        if (page == "") {
            page = "1";
            return;
        }
        if (obj == -1) {
            if (parseInt(page) < 2) {
                return;
            }
            page = parseInt(page) - 1;
        } else if (obj == 1) {
            if (parseInt(page) > parseInt($("#totalpage").val()) - 1) {
                return;
            }
            page = parseInt(page) + 1;
        }
        $("#page").val(page);
        var userid = $("#userid").val();
        $("#pagenow").html(page);
        $.post("/cbtconsole/paycheckc/record", {
            userid: userid, page: page
        }, function (res) {
            var json = eval(res);
            var html = "";
            if (json != null && json != undefined && json != '') {
                $("#table4 tbody tr").eq(0).nextAll().remove();
                for (var i = 0; i < json.length; i++) {
                    var record = json[i];
                    $("#table4 tr:eq(" + (i) + ")").after("<tr></tr>");
                    /* $("#table4 tr:eq("+(i)+")").after("<td >"+record.adminUser+"</td>"); */
                    $("#table4 tr:eq(" + (i) + ")").after("<td >" + record.currency + "</td>");
                    $("#table4 tr:eq(" + (i) + ")").after("<td >" + record.balanceAfter + "</td>");
                    $("#table4 tr:eq(" + (i) + ")").after("<td >" + record.remark + "</td>");

                    if (record.type == 0) {
                        $("#table4 tr:eq(" + (i) + ")").after("<td ></td>");
                    } else if (record.type == 1) {
                        $("#table4 tr:eq(" + (i) + ")").after("<td >取消订单</td>");
                    } else if (record.type == 2) {
                        $("#table4 tr:eq(" + (i) + ")").after("<td >多余金额</td>");
                    } else if (record.type == 3) {
                        $("#table4 tr:eq(" + (i) + ")").after("<td >重复支付</td>");
                    } else if (record.type == 4) {
                        $("#table4 tr:eq(" + (i) + ")").after("<td >手动修改</td>");
                    } else if (record.type == 5) {
                        $("#table4 tr:eq(" + (i) + ")").after("<td >其他</td>");
                    } else if (record.type == 7) {
                        $("#table4 tr:eq(" + (i) + ")").after("<td >余额抵扣</td>");
                    } else if (record.type == 8) {
                        $("#table4 tr:eq(" + (i) + ")").after("<td 申请提现</td>");
                    } else if (record.type == 9) {
                        $("#table4 tr:eq(" + (i) + ")").after("<td >提现取消</td>");
                    } else if (record.type == 10) {
                        $("#table4 tr:eq(" + (i) + ")").after("<td >提现拒绝</td>");

                    }
                    $("#table4 tr:eq(" + (i) + ")").after("<td >" + record.price + "</td>");
                    var html = '';
                    if (record.remark_id != null && record.remark_id != '' && record.remark_id.length > 10) {
                        var orerids = record.remark_id.split(',');
                        for (var k = 0; k < orerids.length; k++) {
                            html = orerids[k] +
                                '<br><a target="_blank" class="ordmlink" href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo=' + orerids[k] + '&state=&username=&paytime=">订单详情</a> '
                                + '<a target="_blank" class="ordmlink" href="/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid=999&orderno=' + orerids[k] + '&days=365&unpaid=0&pagesize=50&orderarrs=0&search_state=0">采购详情</a> '
                                + '<a target="_blank" class="ordmlink" href="/cbtconsole/website/forwarderpageplck.jsp?orderid=' + orerids[k] + '">出货详情</a>	'
                                + '<br>';
                        }
                    }
                    $("#table4 tr:eq(" + (i) + ")").after("<td >" + html + "</td>");
                    $("#table4 tr:eq(" + (i) + ")").after("<td >" + record.datatime + "</td>");

                }
            }
        });


    }

    function fnadditionaljump(obj) {
        var page = $("#additionalpage").val();
        if (page == "") {
            page = "1";
            return;
        }
        if (obj == -1) {
            if (parseInt(page) < 2) {
                return;
            }
            page = parseInt(page) - 1;
        } else if (obj == 1) {
            if (parseInt(page) > parseInt($("#additionaltotalpage").val()) - 1) {
                return;
            }
            page = parseInt(page) + 1;
        }
        $("#additionalpage").val(page);
        var userid = $("#userid").val();
        $("#additionalpagenow").html(page);
        $.post("/cbtconsole/userinfo/additional", {
            userid: userid, page: page
        }, function (res) {
            var json = eval(res);
            var html = "";
            if (json != null && json != undefined && json != '') {
                $("#table6 tbody tr").eq(0).nextAll().remove();
                for (var i = 0; i < json.length; i++) {
                    var record = json[i];
                    $("#table6 tr:eq(" + (i) + ")").after("<tr></tr>");
                    $("#table6 tr:eq(" + (i) + ")").after("<td >" + record.remark + "</td>");
                    $("#table6 tr:eq(" + (i) + ")").after("<td >" + record.admin + "</td>");
                    $("#table6 tr:eq(" + (i) + ")").after("<td >" + record.money + "</td>");
                    $("#table6 tr:eq(" + (i) + ")").after("<td >" + record.orderid + "</td>");
                    $("#table6 tr:eq(" + (i) + ")").after("<td >" + record.createTime + "</td>");
                }
            }
        });
    }

    function fnorderPayjump(obj) {
        var page = $("#orderPaypage").val();
        if (page == "") {
            page = "1";
            return;
        }
        if (obj == -1) {
            if (parseInt(page) < 2) {
                return;
            }
            page = parseInt(page) - 1;
        } else if (obj == 1) {
            if (parseInt(page) > parseInt($("#orderPaytotalpage").val()) - 1) {
                return;
            }
            page = parseInt(page) + 1;
        }
        $("#orderPaypage").val(page);
        var userid = $("#userid").val();
        $("#orderPaypagenow").html(page);
        $.post("/cbtconsole/userinfo/order", {
            userid: userid, page: page
        }, function (res) {
            var json = eval(res);
            var html = "";
            if (json != null && json != undefined && json != '') {
                $("#table5 tbody tr").eq(0).nextAll().remove();
                for (var i = 0; i < json.length; i++) {
                    var record = json[i];
                    if (record.isBalance == 1) {
                        /*  $("#table5 tr:eq("+(i)+")").after("<tr class='style_red'>"); */
                        $("#table5 tr:eq(" + (i) + ")").after("<tr class='style_red'><td >" + record.dateTime + "</td><td >" + record.orderNo + "</td><td >"
                            + record.paymentAmount + "</td><td >" + record.payment + "</td><td >" + record.cancelOrder + "</td><td >" + record.additionalBalance + "</td><td >"
                            + record.payPrice + "</td><td >" + record.refund + "</td></tr>");
                    } else {
                        $("#table5 tr:eq(" + (i) + ")").after("<tr class='style_blank'><td >" + record.dateTime + "</td><td >" + record.orderNo + "</td><td >" + record.paymentAmount
                            + "</td><td >" + record.payment + "</td><td >" + record.cancelOrder + "</td><td >" + record.additionalBalance + "</td><td >" + record.payPrice
                            + "</td><td >" + record.refund + "</td></tr>");
                        /*  $("#table5 tr:eq("+(i)+")").after("<tr class='style_blank'>"); */
                    }
                    /* $("#table5").append("<td>"+record.balance+"</td>");
                    $("#table5 tr:eq("+(i)+")").after("<td >"+record.payPrice+"</td>");
                    $("#table5 tr:eq("+(i)+")").after("<td >"+record.additionalBalance+"</td>");
                    $("#table5 tr:eq("+(i)+")").after("<td >"+record.refund+"</td>");
                    $("#table5 tr:eq("+(i)+")").after("<td >"+record.payment+"</td>");
                    $("#table5 tr:eq("+(i)+")").after("<td >"+record.paymentAmount+"</td>");
                    $("#table5 tr:eq("+(i)+")").after("<td >"+record.orderNo+"</td>"); */
                }
            }
        });
    }

    function fnpayDetailjump(obj) {
        var page = $("#payDetailpage").val();
        if (page == "") {
            page = "1";
            return;
        }
        if (obj == -1) {
            if (parseInt(page) < 2) {
                return;
            }
            page = parseInt(page) - 1;
        } else if (obj == 1) {
            if (parseInt(page) > parseInt($("#payDetailtotalpage").val()) - 1) {
                return;
            }
            page = parseInt(page) + 1;
        }
        $("#payDetailpage").val(page);
        var userid = $("#userid").val();
        $("#payDetailpagenow").html(page);
        $.post("/cbtconsole/userinfo/payDetail", {
            userid: userid, page: page
        }, function (res) {
            var json = eval(res);
            var html = "";
            if (json != null && json != undefined && json != '') {
                $("#table6 tbody tr").eq(0).nextAll().remove();
                for (var i = 0; i < json.length - 1; i++) {
                    var record = json[i];
                    var type = '';
                    if (record.type == '0') {
                        type = 'PayPal进账';
                    } else if (record.type == '1') {
                        type = 'Wire Transfer进账';
                    } else if (record.type == '2') {
                        type = '奖励或补偿';
                    } else if (record.type == '3') {
                        type = '订单消费';
                    } else if (record.type == '4') {
                        type = '已退款';
                    } else if (record.type == '5') {
                        type = '提现处理中';
                    } else if (record.type == '6') {
                        type = '申诉处理中';
                    } else if (record.type == '8') {
                        type = '余额支付';
                    } else if (record.type == '9') {
                        type = '余额增加';
                    }

                    $("#table6 tr:eq(" + (i) + ")").after("<tr><td style='widh:300px;'>" + record.createtime + "</td><td style='widh:200px;'>" + type
                        + "</td><td style='widh:250px;'>" + record.money_in
                        + "</td><td style='widh:250px;'>" + record.money_out
                        + "</td><td style='widh:500px;word-break:break-all;word-wrap:break-word;'>" + record.remark + "</td></tr>");
                }
                var sumMap = json[json.length - 1];
                $("#table6 tr:eq(" + (json.length - 1) + ")").after("<tr><td colspan='5' style='font-size: 16px;'><span>收入&nbsp;</span>"
                    + "<span>USD:" + sumMap.USD_in + "&nbsp;AUD:" + sumMap.AUD_in + "&nbsp;GBP:" + sumMap.GBP_in
                    + "&nbsp;EUR:" + sumMap.EUR_in + "&nbsp;CAD:" + sumMap.CAD_in + "</span>&nbsp;&nbsp;"
                    + "|&nbsp;&nbsp;<span>支出&nbsp;</span><span>USD:"
                    + sumMap.USD_out + "&nbsp;AUD:" + sumMap.AUD_out + "&nbsp;GBP:" + sumMap.GBP_out + "&nbsp;EUR:"
                    + sumMap.EUR_out + "&nbsp;CAD:" + sumMap.CAD_out + "</td></tr>");

            }
        });
    }


</script>
</html>