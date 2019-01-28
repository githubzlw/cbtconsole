<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>取消订单退款审批</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
</head>
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
    $(document).ready(function () {
        var adminId = "${adminId}";
        getAdminList(adminId);
        var dealState = "${dealState}";
        if (!(dealState == null || dealState == "")) {
            $("#query_deal_state").val(dealState);
        }
        var type = "${type}";
        if (!(type == null || type == "")) {
            $("#query_type").val(type);
        }
    });

    function getAdminList(adminId) {
        $.ajax({
            type: "POST",
            url: "/cbtconsole/singleGoods/getAdminList",
            data: {},
            success: function (data) {
                if (data.ok) {
                    $("#query_admin_id").empty();
                    var content = '<option value="0" selected="selected">全部</option>';
                    var json = data.data;
                    for (var i = 0; i < json.length; i++) {
                        if (json[i].id == adminId) {
                            content += '<option selected="select" value="' + json[i].id + '" ">' + json[i].confirmusername + '</option>';
                        } else {
                            content += '<option value="' + json[i].id + '" ">' + json[i].confirmusername + '</option>';
                        }
                    }
                    $("#query_admin_id").append(content);
                } else {
                    alert("获取用户列表失败，原因 :" + data.message);
                }
            },
            error: function (res) {
                alert("网络获取失败");
            }
        });
    }

    function openOrderDeal(userId, orderNo, obj) {
        setChooseTr(obj);
        var url = "/cbtconsole/refundCtr/queryOrderDeal?userId=" + userId + "&orderNo=" + orderNo;
        var param = "height=860,width=1330,top=70,left=350,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
        window.open(url, "windows", param);
    }

    function setChooseTr(obj) {
        //背景色变色
        $(obj).parent().parent().parent().addClass("is_tr").siblings().removeClass("is_tr");
    }


    function hideTrChoose() {
        $("#approval_table tbody").find("tr").each(function () {
            $(this).removeClass("is_tr");
        });
    }

</script>
<body style="overflow-y: hidden;">


<div style="margin-bottom: -3px;text-align: center;width: 100%;height: 10%;">
    <h3 style="text-align: center;">取消订单退款审批</h3>
    <form action="/cbtconsole/orderCancelApprovalCtr/queryForList" method="post">
        <span>客户ID:<input type="text" name="userId" class="inp_sty" value="${userId}"/></span>
        <span>订单号:<input type="text" name="orderNo" class="inp_sty" value="${orderNo}"/></span>
        <span>
            销售:<select id="query_admin_id" name="adminId" style="height: 28px;"></select>
            </span>
        <span>类别:<select id="query_type" name="type" style="height: 28px;">
            <option value="0">全部</option>
            <option value="1">客户申请</option>
            <option value="2">后台申请</option>
        </select></span>
        <span>处理状态:<select id="query_deal_state" name="dealState" style="height: 28px;">
            <option value="-1">全部</option>
            <option value="0">待审批</option>
            <option value="1">销售审批</option>
            <option value="2">主管审批</option>
            <option value="3">完成</option>
            <option value="4">驳回</option>
        </select></span>
        <input type="hidden" value="1" name="page"/>
        &nbsp;&nbsp;&nbsp;<span><input type="submit" class="btn_sty" value="查询"></span>
        &nbsp;&nbsp;&nbsp;<span>操作人：${operatorName}</span>
    </form>
</div>

<div style="width: 100%;height: 90%;overflow-y: auto">
    <table border="1" cellpadding="1" cellspacing="0" align="center">
        <thead>
        <tr align="center" style="height: 50px;">
            <th width="35px">序号</th>
            <th width="130px">类型/状态</th>
            <th width="280px">详情</th>
            <th width="160px">操作时间</th>
            <th width="190px">销售初审情况</th>
            <th width="190px">主管审批情况</th>
            <th width="190px">退款情况</th>
            <th width="80px">操作按钮</th>
        </tr>
        </thead>
        <tbody>

        <c:forEach items="${list}" var="aproval" varStatus="status">
            <tr bgcolor="${status.index % 2 ==0 ? '#fafafa':'#eaf2ff'}" style="height: 200px;">
                <td style="width: 35px;">${status.index + 1}</td>
                <td style="width: 130px;"><span>类型：${aproval.typeDesc}</span><br>
                    <c:if test="${aproval.dealState == 3}">
                        <span style="color:green;">状态：${aproval.dealStateDesc}</span>
                    </c:if>
                    <c:if test="${aproval.dealState == 4}">
                        <span style="color: red;">状态：${aproval.dealStateDesc}</span>
                    </c:if>
                    <c:if test="${aproval.dealState != 3 && aproval.dealState != 4}">
                        <span>状态：${aproval.dealStateDesc}</span>
                    </c:if>

                </td>
                    <%--<td style="width: 60px;"><a target="_blank" href="/cbtconsole/userinfo/getUserInfo.do?userId=${aproval.userId}" title="点击查看客户对账信息">
                            ${aproval.userId}</a><br>销售:${aproval.adminName}</td>--%>
                <td style="width: 280px;">
                    <span>客户ID:<a target="_blank" href="/cbtconsole/userinfo/getUserInfo.do?userId=${aproval.userId}"
                                  title="点击查看客户对账信息">
                            ${aproval.userId}</a>
                    <br><span style="color: #3951ab;">客户余额:${aproval.userBalance}<em>USD</em></span>
                    <br>销售:${aproval.adminName}<br><br>
                    <span>订单号:${aproval.orderNo}</span><br>
                    <span style="color: red;">申请金额:${aproval.payPrice}<em>USD</em></span><br>
                    <c:if test="${fn:length(aproval.updateTime) >0}">
                        <span style="color: green;">审批金额:${aproval.agreeAmount}<em>USD</em></span><br>
                    </c:if>
                    <a href="javascript:void(0);" onclick="openOrderDeal(${aproval.userId},'${aproval.orderNo}',this)"
                       title="查看交易信息">查看交易信息</a><br>
                </td>
                <td style="width: 160px;">
                    <span>申请时间:<br>${aproval.createTime}</span><br>
                    <c:if test="${fn:length(aproval.updateTime) >0}">
                        <span>操作时间:<br>${aproval.updateTime}</span>
                    </c:if>
                </td>
                <td style="width: 190px;">
                    <c:if test="${aproval.approval1 != null}">
                        <span>审批情况：${aproval.approval1.dealStateDesc}</span><br>
                        <span>理由：[${aproval.approval1.remark}]</span><br>
                        <span>操作时间：${aproval.approval1.createTime}</span><br>
                    </c:if>
                </td>
                <td style="width: 190px;">
                    <c:if test="${aproval.approval2 != null}">
                        <span>审批情况：${aproval.approval2.dealStateDesc}</span><br>
                        <span>理由：[${aproval.approval2.remark}]</span><br>
                        <span>操作时间：${aproval.approval2.createTime}</span><br>
                    </c:if>
                </td>
                <td style="width: 190px;">
                    <c:if test="${aproval.approval3 != null}">
                        <span>审批情况：${aproval.approval3.dealStateDesc}</span><br>
                        <span>理由：[${aproval.approval3.remark}]</span><br>
                        <span>操作时间：${aproval.approval3.createTime}</span><br>
                    </c:if>
                </td>
                <td style="width: 80px;">
                    <c:if test="${aproval.dealState == 0}">
                        <input type="button" value="确认" class="btn_sty"
                               onclick="beforeAddRemark(${aproval.id},${aproval.dealState},${aproval.userId},${aproval.payPrice},'${aproval.orderNo}',${operatorId},1,this)"/>
                        <br>
                        <input type="button" value="驳回" class="refuse_sty"
                               onclick="beforeAddRemark(${aproval.id},4,${aproval.userId},${aproval.payPrice},'${aproval.orderNo}',${operatorId},-1,this)"/>
                    </c:if>
                    <c:if test="${aproval.dealState == 1 || aproval.dealState == 2}">
                        <a href="javascript:void(0);" onclick="openDetails(${aproval.id},this)"
                           title="查看流程详细">查看流程详细</a>
                        <c:if test="${roleType == 0}">
                            <br><br>
                            <input type="button" value="确认" class="btn_sty"
                                   onclick="beforeAddRemark(${aproval.id},${aproval.dealState},${aproval.userId},${aproval.agreeAmount},'${aproval.orderNo}',${operatorId},1,this)"/>
                            <br>
                            <input type="button" value="驳回" class="refuse_sty"
                                   onclick="beforeAddRemark(${aproval.id},4,${aproval.userId},${aproval.agreeAmount},'${aproval.orderNo}',${operatorId},-1,this)"/>
                        </c:if>
                    </c:if>
                        <%--Emma可以进行线下转账操作--%>
                    <c:if test="${aproval.dealState == 4}">
                        <a href="javascript:void(0);" onclick="openDetails(${aproval.id},this)"
                           title="查看流程详细">查看流程详细</a>
                        <c:if test="${operatorId == 83 || operatorId == 8}">
                            <br><br>
                            <input type="button" value="执行退款" class="btn_sty"
                                   onclick="beforeAddRemark(${aproval.id},${aproval.dealState},${aproval.userId},${aproval.agreeAmount},'${aproval.orderNo}',${operatorId},3,this)"/>
                            <input type="button" value="线下转账" class="btn_sty"
                                   onclick="offLineRefund(${aproval.id},${aproval.type},${aproval.userId},'${aproval.orderNo}',${operatorId},this)"/>
                        </c:if>
                    </c:if>
                    <c:if test="${aproval.dealState == 3}">
                        <a href="javascript:void(0);" onclick="openDetails(${aproval.id},this)"
                           title="查看流程详细">查看流程详细</a><br><br>
                        <b style="color: green;font-size: 18px;background-color: #efc6c6;">${aproval.dealStateDesc}</b>
                    </c:if>
                    <c:if test="${aproval.dealState == 9}">
                        <a href="javascript:void(0);" onclick="openDetails(${aproval.id},this)"
                           title="查看流程详细">查看流程详细</a><br><br>
                        <b style="color: red;font-size: 18px;background-color: #d2cecc;">${aproval.dealStateDesc}</b>
                    </c:if>
                </td>
            </tr>
        </c:forEach>

        </tbody>
    </table>


    <form id="submit_form" action="/cbtconsole/orderCancelApprovalCtr/queryForList" method="post">
        <input type="hidden" id="page_user_id" name="userId" value="${userId}"/>
        <input type="hidden" id="page_order_no" name="orderNo" value="${orderNo}"/>
        <input type="hidden" id="page_type" name="type" value="${type}"/>
        <input type="hidden" id="query_current_page" name="page" value="${page}"/>
        <input type="hidden" id="page_dealState" name="dealState" value="${dealState}"/>
    </form>
    <div style="text-align: center;">
        <span>当前页：<span id="query_page">${page}</span>/<span id="query_total_page">${totalPage}</span></span>
        <span>&nbsp;&nbsp;总数：<span id="query_total">${total}</span></span>
        <span>&nbsp;&nbsp;<input type="button" class="btn_sty" value="上一页"
                                 onclick="beforeQuery(${page},${page-1},${totalPage})"/></span>
        <span>&nbsp;&nbsp;<input type="button" class="btn_sty" value="下一页"
                                 onclick="beforeQuery(${page},${page+1},${totalPage})"/></span>
        <span>&nbsp;&nbsp;&nbsp;&nbsp;跳转页：<input id="jump_page" type="number" value="1"/>
        <input type="button" value="翻页" class="btn_sty" onclick="jumpPage(${page},${totalPage})"/></span>
    </div>
</div>


<div id="div_secvlid">
    <table id="confirm_table">
        <caption>取消订单退款信息确认</caption>
        <tr>
            <td>
                <input type="hidden" id="approval_id" value=""/>
                <input type="hidden" id="approval_state" value=""/>
            </td>
        </tr>
        <tr>
            <td>用户ID：</td>
            <td><input type="text" id="approval_user_id" value="" style="width: 265px;" disabled="disabled"/></td>
        </tr>
        <tr>
            <td>订单号：</td>
            <td><input type="text" id="approval_order_no" value="" style="width: 265px;" readonly="readonly"/></td>
        </tr>
        <tr>
            <td>退款金额：</td>
            <td><input type="number" id="approval_amount" value="0" style="width: 265px;"/></td>
        </tr>
        <tr>
            <td colspan="2">
                <br>
                <span><input type="checkbox" onclick="addToRemark(this)" value="产品缺失"/>产品缺失</span>
                <span><input type="checkbox" onclick="addToRemark(this)" value="交期问题"/>交期问题</span>
                <span><input type="checkbox" onclick="addToRemark(this)" value="未到货"/>未到货</span>
                <span><input type="checkbox" onclick="addToRemark(this)" value="质量问题"/>质量问题</span>
                <br>
                <span><input type="checkbox" onclick="addToRemark(this)" value="正常取消"/>正常取消</span>
                <span><input type="checkbox" onclick="addToRemark(this)" value="其他"/>其他</span>
            </td>
        </tr>
        <tr>
            <td>备注：</td>
            <td><textarea id="approval_remark" style="width: 265px;height: 80px;"></textarea></td>
        </tr>
        <tr id="second_admid">
            <td>账号：<input type="hidden" id="option_admin_id" value=""></td>
            <td><select id="select_op_id" disabled="disabled">
                <option value="1" selected="selected">Ling</option>
                <option value="8" selected="selected">Mandy</option>
                <option value="83" selected="selected">EmmaXie</option>
            </select></td>
        </tr>
        <tr id="second_pwd_tr">
            <td>密码：</td>
            <td><input type="password" id="second_pwd" value="" style="width: 265px;"/></td>
        </tr>
        <tr>
            <td colspan="2" style="text-align: center;"><input type="button" id="approval_bnt_enter" class="btn_sty"
                                                               value="确定" onclick="setStateAndRemark()"/>
                <input type="button" class="btn_sty" value="取消" onclick="hideDivRemark()"/>
                <span id="show_notice" style="color: red;display: none;">正在执行,请等待...</span></td>
        </tr>
    </table>
</div>
</body>
<script>

    function beforeAddRemark(approvalId, dealState, userId, amount, orderNo, operatorId, actionFlag, obj) {
        //背景色变色
        setChooseTr(obj);

        var roleType = "${roleType}";
        if (dealState == 4) {
            //拒绝退款
            var str = prompt('是否拒绝退款？请输入备注:', '');
            if (str) {
                rejectRefund(approvalId, 4, userId, orderNo, operatorId, str);
            }
        } else if (dealState < 2) {
            if (dealState == 0) {
                if (roleType == 3 || roleType == 4) {
                    //销售同意退款
                    showDivSecvlid(approvalId, dealState + 1, userId, amount, orderNo, operatorId);
                } else {
                    alert("需要销售同意，您无权限操作！");
                    hideDivRemark();
                    return false;
                }
            } else if (dealState == 1 || dealState == 2) {
                if (roleType == 0) {
                    //主管同意退款
                    if (operatorId == 1 || operatorId == 8 || operatorId == 83) {
                        showDivSecvlid(approvalId, dealState + 1, userId, amount, orderNo, operatorId);
                    } else {
                        alert("需要Ling或Mandy或Emma同意，您无权限操作！");
                    }
                } else {
                    alert("需要主管同意，您无权限操作！");
                    hideDivRemark();
                    return false;
                }
            } else {
                alert("无权限操作！");
                hideDivRemark();
                return false;
            }
        } else if (dealState == 2) {
            if (roleType == 0) {
                //执行退款操作
                showDivSecvlid(approvalId, 3, userId, amount, orderNo, operatorId);
            } else {
                alert("无权限操作！");
                hideDivRemark();
                return false;
            }
        }
    }

    function setStateAndRemark() {
        var approvalId = $("#approval_id").val();
        var dealState = $("#approval_state").val();
        var userId = $("#approval_user_id").val();
        var orderNo = $("#approval_order_no").val();
        var operatorId = $("#select_op_id").val();
        if (operatorId == null || operatorId == "" || operatorId == 0) {
            operatorId = $("#option_admin_id").val();
        }
        var refundAmount = $("#approval_amount").val();
        var secvlidPwd = $("#second_pwd").val();
        var remark = $("#approval_remark").val();
        if (orderNo == null || orderNo == "") {
            alert("获取订单号失败");
            return false;
        } else if (refundAmount == null || refundAmount == "" || refundAmount <= 0) {
            alert("获取退款金额失败");
            return false;
        } else if (remark == null || remark == "") {
            alert("获取备注信息失败");
            return false;
        } else if (dealState > 1 && operatorId != 1 && (secvlidPwd == null || secvlidPwd == "")) {
            alert("获取密码失败");
            return false;
        } else {
            /*$.messager.progress({
                title: '正在执行',
                msg: '请等待...'
            });*/
            $("#approval_bnt_enter").prop("disabled", true);
            $("#show_notice").show();
            $.ajax({
                type: 'POST',
                dataType: 'text',
                url: '/cbtconsole/orderCancelApprovalCtr/setStateAndRemark',
                data: {
                    "approvalId": approvalId,
                    "dealState": dealState,
                    "userId": userId,
                    "orderNo": orderNo,
                    "operatorId": operatorId,
                    "refundAmount": refundAmount,
                    "remark": remark,
                    "secvlidPwd": secvlidPwd
                },
                success: function (data) {
                    //$.messager.progress('close');
                    $("#approval_bnt_enter").prop("disabled", false);
                    $("#show_notice").hide();
                    var json = eval("(" + data + ")");
                    if (json.ok) {
                        //alert("执行成功");
                        hideDivRemark();
                        if (json.message == null || json.message == "") {
                            alert("执行成功");
                        } else {
                            alert("执行成功," + json.message);
                        }
                        window.location.reload();
                    } else {
                        alert("执行失败," + json.message);
                    }
                },
                error: function () {
                    $("#approval_bnt_enter").prop("disabled", false);
                    $("#show_notice").hide();
                    //$.messager.progress('close');
                    alert("执行失败,请联系管理员");
                }
            });
        }
    }

    /**
     * 拒绝退款
     * @param approvalId
     * @param actionFlag
     * @param userId
     * @param operatorId
     * @param remark
     */
    function rejectRefund(approvalId, actionFlag, userId, orderNo, operatorId, remark) {
        $.messager.progress({
            title: '正在执行',
            msg: '请等待...'
        });
        $.ajax({
            type: 'POST',
            dataType: 'text',
            url: '/cbtconsole/orderCancelApprovalCtr/rejectRefund',
            data: {
                "approvalId": approvalId,
                "actionFlag": actionFlag,
                "userId": userId,
                "orderNo": orderNo,
                "operatorId": operatorId,
                "remark": remark
            },
            success: function (data) {
                $.messager.progress('close');
                var json = eval("(" + data + ")");
                if (json.ok) {
                    //alert("执行成功");
                    window.location.reload();
                } else {
                    alert(json.message);
                }
            },
            error: function () {
                $.messager.progress('close');
                alert("执行失败,请联系管理员");
            }
        });
    }


    function showDivSecvlid(approvalId, dealState, userId, amount, orderNo, operatorId) {
        if (dealState == 0 || dealState == 1) {
            $("#second_admid").css("display", "none");
            $("#second_pwd_tr").css("display", "none");
        }
        $("#approval_id").val(approvalId);
        $("#approval_state").val(dealState);
        $("#approval_user_id").val(userId);
        $("#approval_amount").val(amount);
        $("#approval_order_no").val(orderNo);
        $("#select_op_id").val(operatorId);
        $("#option_admin_id").val(operatorId);
        $("#div_secvlid").show();
    }

    function hideDivRemark() {
        hideTrChoose();
        $("#second_admid").css("display", "");
        $("#second_pwd_tr").css("display", "");
        $("#order_no_tr").css("display", "");
        $("#second_pwd").val("");
        $("#approval_id").val("");
        $("#approval_state").val("");
        $("#approval_user_id").val("");
        $("#approval_amount").val("");
        $("#approval_order_no").val("");
        $("#approval_remark").val("");
        $("#option_admin_id").val("");
        $("#div_secvlid").hide();
        $("#approval_bnt_enter").prop("disabled", false);
        $("#show_notice").hide();
    }


    function openDetails(approvalId, obj) {
        setChooseTr(obj);
        var url = "/cbtconsole/orderCancelApprovalCtr/queryApprovalDetails?approvalId=" + approvalId;
        var param = "height=860,width=1090,top=70,left=360,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
        window.open(url, "windows", param);
    }

    function beforeQuery(currentPage, nextPage, totalPage) {
        if (nextPage > 0 && nextPage <= totalPage) {
            $("#query_current_page").val(nextPage);
            doQuery();
        } else {
            alert("无法翻页！");
            return false;
        }
    }

    function addToRemark(obj) {
        if ($(obj).is(":checked")) {
            var content = $(obj).val();
            var remarkVal = $("#approval_remark").val();
            $("#approval_remark").val(remarkVal + "@" + content);
        }
    }

    function jumpPage(currentPage, totalPage) {

        var nextPage = $("#jump_page").val();
        if (nextPage == null || nextPage == "" || nextPage < 1) {
            alert("请输入跳转页");
            return false;
        } else {
            if (nextPage > 0 && nextPage <= totalPage) {
                $("#query_current_page").val(nextPage);
                doQuery();
            } else {
                alert("无法翻页！");
                return false;
            }
        }
    }

    function doQuery() {
        $("#submit_form").submit();
    }
</script>
</html>
