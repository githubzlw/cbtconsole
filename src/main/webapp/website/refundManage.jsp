<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    //不允许浏览器端或缓存服务器缓存当前页面信息。
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    response.addHeader("Cache-Control", "no-cache");//浏览器和缓存服务器都不应该缓存页面信息
    response.addHeader("Cache-Control", "no-store");//请求和响应的信息都不应该被存储在对方的磁盘系统中；
    response.addHeader("Cache-Control", "must-revalidate");///于客户机的每次请求，代理服务器必须想服务器验证缓存是否过时；
%>
<html>
<head>
    <title>退款管理</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <style>
        .sp_style {
            margin-left: 10px;
            border-radius: 4px;
            border-width: 1px;
            border-style: solid;
            width: 142px;
            text-align: center;
            float: left;
            line-height: 32px;
        }

        .btn_sty {
            margin: 10px 0 10px 20px;
            width: 122px;
            color: #fff;
            background-color: #5db5dc;
            border-color: #2e6da4;
            font-size: 16px;
            border-radius: 5px;
            border: 1px solid transparent;
        }

        .refuse_sty {
            margin: 10px 0 10px 20px;
            width: 122px;
            color: red;
            background-color: #5db5dc;
            border-color: #2e6da4;
            font-size: 16px;
            border-radius: 5px;
            border: 1px solid transparent;
        }

        .is_choose {
            background-color: #5db5dc;
        }

        .is_tr {
            background-color: #bfbb89;
        }

        #div_secvlid {
            display: none;
            position: fixed;
            top: 25%;
            background: #8cdab6;
            padding: 50px;
            left: 45%;
            box-shadow: 1px 10px 15px #e2e2e2;
        }

    </style>
    <script>

        $(function () {
            initParam();
        });
        
        function initParam() {
            var type = "${type}";
            if(type !=null || type != ""){
                $("#query_type").val(type);
            }
            var appMoney = "${appMoney}";
            if(appMoney !=null || appMoney != ""){
                $("#query_app_money").val(appMoney);
            }
        }
    </script>
</head>
<body>

<c:if test=" ${success ==0}">
    <h2 style="color: red">${message}</h2>
</c:if>

<h3 style="text-align: center">退款管理</h3>
<div id="refund_top_toolbar" style="height: auto;">

    <form id="refund_query_form" action="/cbtconsole/refundCtr/queryForList" method="post">
        <div><span> 客户ID: <input type="text" id="query_user_id" style="width: 200px; height: 24px" value="${userId}"
                                 name="userId"/></span>
            <span> 账号: <input type="text" id="query_payPal_email" style="width: 200px; height: 24px"
                                    value="${payPalEmail}" name="payPalEmail"/></span>
            <span> 申请时间: <input id="query_begin_time" class="Wdate"
                                style="width: 110px; height: 24px" type="text" value="${beginTime}" name="beginTime"
                                onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
				<span>&nbsp;到&nbsp;</span><input id="query_end_time" class="Wdate" style="width: 110px; height: 24px;"
                                                 type="text" value="${endTime}" name="endTime"
                                                 onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/>
			</span>
            <span> 申请类型: <select id="query_type"
                                 style="font-size: 16px; height: 24px; width: 120px;" name="type">
					<option value="-1">全部</option>
                    <option value="0">余额提现</option>
                    <option value="1">PayPal</option>
			</select></span>
            <span> 金额分类: <select id="query_app_money"
                                 style="font-size: 16px; height: 24px; width: 120px;" name="appMoney">
					<option value="0">全部</option>
                    <option value="1">小于等于50</option>
                    <option value="2">大于50</option>
			</select></span>
            <span><input type="submit" class="btn_sty" value="查询" onclick="doQuery()"/></span>
        <span>&nbsp;&nbsp;&nbsp;操作人：${operatorName}</span></div>
        <%--<input type="hidden" id="query_state" value="${state}" name="state">--%>
        <input type="hidden" id="query_current_page" value="${page}" name="page">
        <input type="hidden" id="query_choose_state" value="${chooseState}" name="chooseState">


    </form>
    <div class="span_div" style="text-align: left;">

        <span style="margin-left: 100px;"><a href="/cbtconsole/refundss/rlist" target="_blank">未匹配用户申诉记录</a></span>
        <c:if test="${roleType == '0' }">
            <span style="margin-left: 65px;"><input type="button" onclick="showSecondValid(${operatorId})" value="二次验证密码" class="btn_sty"></span>
        </c:if>
        <span class="sp_style ${chooseState == -1 ? ' is_choose':''}" onclick="changeAndQuery(-1)">全部</span>
        <span class="sp_style ${chooseState == 0 ? ' is_choose':''}" onclick="changeAndQuery(0)">待审批</span>
        <span class="sp_style ${chooseState == 9 ? ' is_choose':''}" onclick="changeAndQuery(9)">已驳回</span>
        <%--<span class="sp_style" onclick="changeAndQuery(-1,this)">已通过</span>--%>
        <%--<span class="sp_style" onclick="changeAndQuery(-1)">已超期</span>--%>
        <span class="sp_style ${chooseState == 3 ? ' is_choose':''}" onclick="changeAndQuery(3)">待执行</span>
        <span class="sp_style ${chooseState == 4 ? ' is_choose':''}" onclick="changeAndQuery(4)">已完结</span>

    </div>
</div>
<div>
    <table id="refund_table" style="width: 100%;border-color: #b6ff00;" border="1" cellpadding="1"
           cellspacing="0" align="center">
        <thead>
        <tr align="center" bgcolor="#ccc" style="color: #ea6161;height: 50px;">
            <th width="35px">序号</th>
            <th width="105px">类型/状态</th>
            <%--<th width="60px">客户ID</th>--%>
            <th width="100px">PayPal帐号/客户邮箱</th>
            <th width="120px">详情</th>
            <th width="130px">操作时间</th>
            <th width="190px">销售初审情况</th>
            <th width="190px">主管审批情况</th>
            <th width="190px">财务确认情况</th>
            <th width="80px">操作按钮</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${list}" var="refund" varStatus="status">
            <tr bgcolor="${status.index % 2 ==0 ? '#fafafa':'#eaf2ff'}" style="height: 200px;">
                <td style="width: 35px;">${status.index + 1}</td>
                <td style="width: 105px;"><span>类型：${refund.typeDesc}</span><br>
                    <c:if test="${refund.state == 4}">
                        <span style="color:green;">状态：${refund.stateDesc}</span>
                    </c:if>
                    <c:if test="${refund.state == 9}">
                        <span style="color: red;">状态：${refund.stateDesc}</span>
                    </c:if>
                    <c:if test="${refund.state != 4 && refund.state != 9}">
                        <span>状态：${refund.stateDesc}</span>
                    </c:if>
                </td>
                <%--<td style="width: 60px;"><a target="_blank" href="/cbtconsole/userinfo/getUserInfo.do?userId=${refund.userId}" title="点击查看客户对账信息">
                        ${refund.userId}</a><br>销售:${refund.salesName}</td>--%>
                <td style="width: 100px;">
                    <span>客户ID:<a target="_blank" href="/cbtconsole/userinfo/getUserInfo.do?userId=${refund.userId}" title="点击查看客户对账信息">
                        ${refund.userId}</a><br>销售:${refund.salesName}
                    <c:if test="${fn:length(refund.paymentEmail) >0}"></span><br><br>
                        <span>PayPal帐号:<br>${refund.paymentEmail}</span><br>
                    </c:if>
                    <c:if test="${fn:length(refund.userEmail) >0}">
                        <span>客户邮箱:<br>${refund.userEmail}</span>
                    </c:if>
                </td>
                <td style="width: 120px;">
                    <span style="color: #3951ab;">客户余额:${refund.userBalance}<em>USD</em></span><br>
                    <c:if test="${fn:length(refund.orderNo) >0}">
                        <span>订单号:<br>${refund.orderNo}</span><br>
                    </c:if>
                    <span style="color: red;">申请金额:${refund.appliedAmount}<em>${refund.currency}</em></span><br>
                    <c:if test="${fn:length(refund.operationTime) >0}">
                        <span style="color: green;">审批金额:${refund.agreeAmount}<em>${refund.currency}</em></span><br>
                    </c:if>
                    <c:if test="${fn:length(refund.paymentNo) >0}">
                        <span>交易号:<br>${refund.paymentNo}</span><br>
                    </c:if>
                    <c:if test="${refund.type == 1}">
                        <a href="javascript:void(0);" onclick="openOrderDeal(${refund.userId},'${refund.orderNo}',this)" title="查看交易信息">查看交易信息</a><br>
                    </c:if>
                    <c:if test="${fn:length(refund.reasonCode) >0}">
                        <span>payPal类型:${refund.reasonCode}</span><br>
                    </c:if>
                    <c:if test="${fn:length(refund.reasonNote) >0}">
                        <br><span>payPal内容:[${refund.reasonNote}]</span><br>
                    </c:if>
                </td>
                <td style="width: 130px;">
                    <span>申请时间:<br>${refund.appliedTime}</span><br>
                    <c:if test="${fn:length(refund.endTime) >0}">
                        <span>截止时间:<br>${refund.endTime}</span><br>
                    </c:if>
                    <c:if test="${fn:length(refund.operationTime) >0}">
                        <span>操作时间:<br>${refund.operationTime}</span>
                    </c:if>
                </td>
                <td style="width: 190px;">
                    <c:if test="${refund.salesApproval != null}">
                        <span>审批情况：${refund.salesApproval.refundStateDesc}</span><br>
                        <span>理由：[${refund.salesApproval.remark}]</span><br>
                        <span>操作时间：${refund.salesApproval.createTime}</span><br>
                    </c:if>
                </td>
                <td style="width: 190px;">
                    <c:if test="${refund.adminApproval != null}">
                        <span>审批情况：${refund.adminApproval.refundStateDesc}</span><br>
                        <span>理由：[${refund.adminApproval.remark}]</span><br>
                        <span>操作时间：${refund.adminApproval.createTime}</span><br>
                    </c:if>
                </td>
                <td style="width: 190px;">
                    <c:if test="${refund.financialApproval != null}">
                        <span>审批情况：${refund.financialApproval.refundStateDesc}</span><br>
                        <span>理由：[${refund.financialApproval.remark}]</span><br>
                        <span>操作时间：${refund.financialApproval.createTime}</span><br>
                    </c:if>
                </td>
                <td style="width: 80px;">
                    <c:if test="${refund.state == 0}">
                        <input type="button" value="确认" class="btn_sty"
                               onclick="beforeSetAndRemark(${refund.id},${refund.state},${refund.type},${refund.userId},${refund.appliedAmount},'${refund.orderNo}',${operatorId},1,this)"/>
                        <br>
                        <input type="button" value="驳回" class="refuse_sty"
                               onclick="beforeSetAndRemark(${refund.id},${refund.state},${refund.type},${refund.userId},${refund.appliedAmount},'${refund.orderNo}',${operatorId},-1,this)"/>
                    </c:if>
                    <c:if test="${refund.state == 1 || refund.state == 2}">
                        <a href="javascript:void(0);" onclick="openDetails(${refund.id},this)" title="查看流程详细">查看流程详细</a><br><br>
                        <input type="button" value="确认" class="btn_sty"
                               onclick="beforeSetAndRemark(${refund.id},${refund.state},${refund.type},${refund.userId},${refund.agreeAmount},'${refund.orderNo}',${operatorId},1,this)"/>
                        <br>
                        <input type="button" value="驳回" class="refuse_sty"
                               onclick="beforeSetAndRemark(${refund.id},${refund.state},${refund.type},${refund.userId},${refund.agreeAmount},'${refund.orderNo}',${operatorId},-1,this)"/>
                    </c:if>
                    <%--Emma可以进行线下转账操作--%>
                    <c:if test="${refund.state == 3 && operatorId == 8}">
                        <a href="javascript:void(0);" onclick="openDetails(${refund.id},this)" title="查看流程详细">查看流程详细</a><br><br>
                        <input type="button" value="执行退款" class="btn_sty"
                               onclick="beforeSetAndRemark(${refund.id},${refund.state},${refund.type},${refund.userId},${refund.agreeAmount},'${refund.orderNo}',${operatorId},3,this)"/>
                        <input type="button" value="线下转账" class="btn_sty"
                               onclick="offLineRefund(${refund.id},${refund.type},${refund.userId},'${refund.orderNo}',${operatorId},this)"/>
                    </c:if>
                    <c:if test="${refund.state == 4}">
                        <a href="javascript:void(0);" onclick="openDetails(${refund.id},this)" title="查看流程详细">查看流程详细</a><br><br>
                        <b style="color: green;font-size: 18px;background-color: #efc6c6;">${refund.stateDesc}</b>
                    </c:if>
                    <c:if test="${refund.state == 9}">
                        <a href="javascript:void(0);" onclick="openDetails(${refund.id},this)" title="查看流程详细">查看流程详细</a><br><br>
                        <b style="color: red;font-size: 18px;background-color: #d2cecc;">${refund.stateDesc}</b>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <div style="text-align: center;">
        <span>当前页：<span id="query_page">${page}</span>/<span id="query_total_page">${totalPage}</span></span>
        <span>&nbsp;&nbsp;总数：<span id="query_total">${total}</span></span>
        <span>&nbsp;&nbsp;<input type="button" class="btn_sty" value="上一页" onclick="beforeQuery(${page},${page-1},${totalPage})"/></span>
        <span>&nbsp;&nbsp;<input type="button" class="btn_sty" value="下一页" onclick="beforeQuery(${page},${page+1},${totalPage})"/></span>
        <span>&nbsp;&nbsp;&nbsp;&nbsp;跳转页：<input id="jump_page" type="number" value="1"/>
        <input type="button" value="翻页" class="btn_sty" onclick="jumpPage(${page},${totalPage})"/></span>
    </div>
</div>

<div id="div_secvlid">
    <table id="confirm_table">
        <caption>退款信息确认</caption>
        <tr>
            <td>
                <input type="hidden" id="refund_id" value=""/>
                <input type="hidden" id="refund_type" value=""/>
                <input type="hidden" id="refund_state" value=""/>
                <input type="hidden" id="refund_action_flag" value=""/>
            </td>
        </tr>
        <tr>
            <td>用户ID：</td>
            <td><input type="text" id="refund_user_id" value="" style="width: 265px;" disabled="disabled"/></td>
        </tr>
        <tr>
            <td>订单号：</td>
            <td><input type="text" id="refund_order_no" value="" style="width: 265px;"/></td>
        </tr>
        <tr id="order_no_tr">
            <td colspan="2"><select id="select_order_no" onchange="chooseOrderNo(this)" style="width: 253px;height: 24px;">
                <option value="">请选择订单号</option>
            </select><input type="button" value="查询可用订单" onclick="findCanRefundOrderNo()"/><br></td>
        </tr>
        <tr>
            <td>退款金额：</td>
            <td><input type="number" id="refund_amount" value="0" style="width: 265px;"/></td>
        </tr>
        <tr>
            <td colspan="2">
                <br>
                <span><input type="checkbox" onclick="addToRemark(this)" value="产品缺失"/>产品缺失</span>
                <span><input type="checkbox" onclick="addToRemark(this)" value="交期问题"/>交期问题</span>
                <span><input type="checkbox" onclick="addToRemark(this)" value="未到货"/>未到货</span>
                <span><input type="checkbox" onclick="addToRemark(this)" value="质量问题"/>质量问题</span>
                <br>
                <span><input type="checkbox" onclick="addToRemark(this)" value="正常提现"/>正常提现</span>
                <span><input type="checkbox" onclick="addToRemark(this)" value="payPal诈骗"/>payPal诈骗</span>
                <span><input type="checkbox" onclick="addToRemark(this)" value="其他"/>其他</span>
            </td>
        </tr>
        <tr>
            <td>备注：</td>
            <td><textarea id="refund_remark" style="width: 265px;height: 80px;"></textarea></td>
        </tr>
        <tr id="second_admid">
            <td>账号：<input type="hidden" id="option_admin_id" value=""></td>
            <td><select id="select_op_id" disabled="disabled">
                <option value="1" selected="selected">Ling</option>
            </select></td>
        </tr>
        <tr id="second_pwd">
            <td>密码：</td>
            <td><input type="password" id="secvlid_pwd" value="" style="width: 265px;"/></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: center;"><input type="button" class="btn_sty" value="确定" onclick="setAndRemark()"/>
                <input type="button" class="btn_sty" value="取消" onclick="hideDivRemark()"/></td>
        </tr>
    </table>
</div>
</body>
<script>


    function beforeSetAndRemark(refundId, state, type,userId,amount,orderNo,operatorId, actionFlag,obj) {
        //背景色变色
        setChooseTr(obj);

        var roleType = "${roleType}";
        if (actionFlag < 0) {
            //拒绝退款
            var str = prompt('是否拒绝退款？请输入备注:', '');
            if(str){
                rejectRefund(refundId,9,userId,orderNo,operatorId,str);
            }
        } else if (actionFlag == 1) {
            if (state == 0) {
                if(roleType == 3 || roleType == 4){
                    //销售同意退款
                    showDivSecvlid(refundId,type,state,1,userId,amount,orderNo,operatorId);
                }else{
                    $.messager.alert("操作提示","需要销售同意，您无权限操作！");
                    hideDivRemark();
                    return false;
                }
            } else if (state == 1 || state == 2) {
                if(roleType == 0){
                    //主管同意退款

                    if(state == 1){
                        //Ling退款
                        if(operatorId == 1){
                            showDivSecvlid(refundId,type,state,state + 1,userId,amount,orderNo,operatorId);
                        }else{
                            $.messager.alert("操作提示","需要Ling同意，您无权限操作！");
                        }
                    }else if(state == 2){
                        //EMMA退款
                        if(operatorId == 8){
                            showDivSecvlid(refundId,type,state,state + 1,userId,amount,orderNo,operatorId);
                        }else{
                            $.messager.alert("操作提示","需要EMMA同意，您无权限操作！");
                        }
                    }
                }else{
                    $.messager.alert("操作提示","需要主管同意，您无权限操作！");
                    hideDivRemark();
                    return false;
                }
            } else {
                $.messager.alert("操作提示","无权限操作！");
                hideDivRemark();
                return false;
            }
        } else if (actionFlag == 3) {
            if(roleType == 0){
                //执行退款操作
                showDivSecvlid(refundId,type,state,4,userId,amount,orderNo,operatorId);
            }else{
                $.messager.alert("操作提示","无权限操作！");
                hideDivRemark();
                return false;
            }
        }
    }

    function setAndRemark() {
        var refundId = $("#refund_id").val();
        var type = $("#refund_type").val();
        var state = $("#refund_state").val();
        var actionFlag = $("#refund_action_flag").val();
        var userId = $("#refund_user_id").val();
        var orderNo = $("#refund_order_no").val();
        var operatorId = $("#select_op_id").val();
        if(operatorId == null || operatorId == "" || operatorId == 0){
            operatorId = $("#option_admin_id").val();
        }
        var refundAmount = $("#refund_amount").val();
        var secvlidPwd = $("#secvlid_pwd").val();
        var remark = $("#refund_remark").val();
        if(orderNo == null || orderNo == ""){
            $.messager.alert("操作提示","获取订单号失败");
            return false;
        }else if(refundAmount == null || refundAmount == "" || refundAmount <= 0){
            $.messager.alert("操作提示","获取退款金额失败");
            return false;
        }else if(remark == null || remark == ""){
            $.messager.alert("操作提示","获取备注信息失败");
            return false;
        }else if(actionFlag > 1 && (secvlidPwd == null || secvlidPwd == "")){
            $.messager.alert("操作提示","获取密码失败");
            return false;
        }else{
            $.messager.progress({
                title: '正在执行',
                msg: '请等待...'
            });
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/refundCtr/setAndRemark',
                data: {
                    "refundId":refundId,
                    "state":state,
                    "actionFlag":actionFlag,
                    "type":type,
                    "userId":userId,
                    "orderNo":orderNo,
                    "operatorId":operatorId,
                    "refundAmount":refundAmount,
                    "remark":remark,
                    "secvlidPwd":secvlidPwd
                },
                success: function (data) {
                    $.messager.progress('close');
                    var json = eval("(" + data + ")");
                    if (json.ok) {
                        //$.messager.alert("操作提示","执行成功");
                        window.location.reload();
                    } else {
                        $.messager.alert("操作提示",json.message);
                    }
                },
                error: function () {
                    $.messager.progress('close');
                    $.messager.alert("操作提示","执行失败,请联系管理员");
                }
            });
        }
    }

    /**
     * 拒绝退款
     * @param refundId
     * @param actionFlag
     * @param userId
     * @param operatorId
     * @param remark
     */
    function rejectRefund(refundId,actionFlag,userId,orderNo,operatorId,remark) {
        $.messager.progress({
            title: '正在执行',
            msg: '请等待...'
        });
        $.ajax({
            type: 'POST',
            dataType: 'text',
            url: '/cbtconsole/refundCtr/rejectRefund',
            data: {
                "refundId":refundId,
                "actionFlag":actionFlag,
                "userId":userId,
                "orderNo":orderNo,
                "operatorId":operatorId,
                "remark":remark
            },
            success: function (data) {
                $.messager.progress('close');
                var json = eval("(" + data + ")");
                if (json.ok) {
                    //$.messager.alert("操作提示","执行成功");
                    window.location.reload();
                } else {
                    $.messager.alert("操作提示",json.message);
                }
            },
            error: function () {
                $.messager.progress('close');
                $.messager.alert("操作提示","执行失败,请联系管理员");
            }
        });
    }

    /**
     * 已经线下转账
     * @param refundId
     * @param userId
     * @param operatorId
     * @param orderNo
     */
    function offLineRefund(refundId,type,userId,orderNo,operatorId,obj) {
        //背景色变色
        setChooseTr(obj);
        var str = prompt('是否线下转账？请输入备注:', '');
        if(str){
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/refundCtr/offLineRefund',
                data: {
                    "refundId":refundId,
                    "type":type,
                    "actionFlag":3,
                    "userId":userId,
                    "operatorId":operatorId,
                    "orderNo":orderNo,
                    "remark":str
                },
                success: function (data) {
                    $.messager.progress('close');
                    var json = eval("(" + data + ")");
                    if (json.ok) {
                        //$.messager.alert("操作提示","执行成功");
                        window.location.reload();
                    } else {
                        $.messager.alert("操作提示",json.message);
                    }
                },
                error: function () {
                    $.messager.progress('close');
                    $.messager.alert("操作提示","执行失败,请联系管理员");
                }
            });
        }else{
            hideTrChoose();
        }
    }

    /**
     * 根据输入的金额查询可用订单
     * @param userId
     * @param accId
     */
    function findCanRefundOrderNo(){
        $("#select_order_no").empty();
        $("#select_order_no").append('<option value="">请选择订单号</option>');
        var refundAmount = $("#refund_amount").val();
        var userId = $("#refund_user_id").val();
        var refundType = $("#refund_type").val();

        if(userId == 0){
            $.messager.alert("操作提示","获取用户ID失败");
        }else if(refundAmount == null || refundAmount == "" || refundAmount == 0){
            $.messager.alert("操作提示","获取退款金额失败");
        }else{
            if(refundType == 0){
                $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: '/cbtconsole/refundCtr/findCanRefundOrderNo',
                    data: {
                        userId: userId,
                        refundAmount: refundAmount
                    },
                    success: function (data) {
                        if (data.ok) {
                            if(data.total > 0){
                                $("#select_order_no").empty();
                                var json = data.data;
                                var context = '';
                                var count = 0;
                                for(var key in json){
                                    if(count == 0){
                                        $("#refund_order_no").val(key);
                                    }
                                    context +='<option value="'+key+'">'+json[key]+'</option>';
                                    count ++;
                                }
                                $("#select_order_no").append(context);
                            }else{
                                $.messager.alert("操作提示","无匹配的订单可用");
                            }
                        } else {
                            $.messager.alert("操作提示",data.message);
                        }
                    },
                    error: function (XMLResponse) {
                        $.messager.alert("操作提示","网络链接失败！");
                    }
                });
            }
        }
    }


    function addToRemark(obj) {
        if($(obj).is(":checked")){
            var content = $(obj).val();
            var remarkVal = $("#refund_remark").val();
            $("#refund_remark").val(remarkVal + "@" + content);
        }
    }

    function chooseOrderNo(obj) {
        var orderNo = $(obj).val();
        $("#refund_order_no").val(orderNo);
    }
    
    function changeAndQuery(chooseState) {
        $("#query_choose_state").val(chooseState);
        doQuery();
    }
    
    function doQuery() {
        $("#refund_query_form").submit();
    }
    
    function beforeQuery(currentPage,nextPage,totalPage) {
        if(nextPage >0 && nextPage <=totalPage){
            $("#query_current_page").val(nextPage);
            doQuery();
        }else{
            $.messager.alert("操作提示","无法翻页！");
            return false;
        }
    }

    function jumpPage(currentPage,totalPage){

        var nextPage = $("#jump_page").val();
        if(nextPage == null || nextPage == "" || nextPage < 1){
            $.messager.alert("操作提示","请输入跳转页");
            return false;
        }else{
            if(nextPage >0 && nextPage <=totalPage){
                $("#query_current_page").val(nextPage);
                doQuery();
            }else{
                $.messager.alert("操作提示","无法翻页！");
                return false;
            }
        }
    }

    function showDivSecvlid(refundId,type,state,actionFlag,userId,amount,orderNo,operatorId) {
        if(actionFlag == 1){
            $("#second_admid").css("display","none");
            $("#second_pwd").css("display","none");
        }
        if(state > 0 || type == 1){
            $("#order_no_tr").css("display","none");
        }
        $("#refund_id").val(refundId);
        $("#refund_type").val(type);
        $("#refund_state").val(state);
        $("#refund_action_flag").val(actionFlag);
        $("#refund_user_id").val(userId);
        $("#refund_amount").val(amount);
        $("#refund_order_no").val(orderNo);
        if(state > 1 && orderNo.length > 0){
            $("#refund_order_no").attr("disbaled","disbaled");
        }
        $("#select_op_id").val(operatorId);
        $("#option_admin_id").val(operatorId);
        $("#div_secvlid").show();
    }

    function hideDivRemark() {
        hideTrChoose();
        $("#second_admid").css("display","");
        $("#second_pwd").css("display","");
        $("#order_no_tr").css("display","");
        $("#secvlid_pwd").val("");
        $("#refund_id").val("");
        $("#refund_type").val("");
        $("#refund_state").val("");
        $("#refund_action_flag").val("");
        $("#refund_user_id").val("");
        $("#refund_amount").val("");
        $("#refund_order_no").val("");
        $("#refund_order_no").removeAttr("disbaled");
        $("#refund_remark").val("");
        $("#option_admin_id").val("");
        $("#div_secvlid").hide();
    }
    
    function hideTrChoose() {
        $("#refund_table tbody").find("tr").each(function () {
            $(this).removeClass("is_tr");
        });
    }
    
    function openOrderDeal(userId,orderNo,obj) {
        setChooseTr(obj);
        var url = "/cbtconsole/refundCtr/queryOrderDeal?userId=" + userId + "&orderNo=" + orderNo;
        var param = "height=860,width=1330,top=70,left=350,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
        window.open(url, "windows", param);
    }
    
    function openDetails(refundId,obj) {
        setChooseTr(obj);
        var url = "/cbtconsole/refundCtr/queryRefundDetails?refundId=" + refundId;
        var param = "height=860,width=1090,top=70,left=360,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
        window.open(url, "windows", param);
    }
    
    function showSecondValid(operatorId) {
        var url = "/cbtconsole/apa/secondaryValidation.html?operatorId="+operatorId;
        var param = "height=460,width=680,top=225,left=666,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
        window.open(url, "windows", param);
    }

    function setChooseTr(obj) {
        //背景色变色
        $(obj).parent().parent().addClass("is_tr").siblings().removeClass("is_tr");
    }
</script>
</html>
