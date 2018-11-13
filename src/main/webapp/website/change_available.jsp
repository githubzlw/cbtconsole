<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@page import="com.cbt.util.Redis" %>
<% String admuserJson = Redis.hget(request.getSession().getId(), "admuser"); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>修改余额</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">
        var userid = '${param.userid}';
        $(function () {
            var admuserJson = <%=admuserJson%>;

            $("#confirm").attr("disabled", true);

            $.post("/cbtconsole/ConfirmUserServlet", {},
                function (res) {
                    var json = eval(res);
                    var optionStr = "";
                    for (var i = 0; i < json.length; i++) {
                        //str+='<option value='+json[i].id+'>'+json[i].confirmusername+'</option>';
                        optionStr += '<option value=' + json[i].id + '>' + json[i].confirmusername + '</option>';
                    }
                    $('#modifyuser').append(optionStr);
                    $("#modifyuser").val(admuserJson.id);
                    $("#modifyuser").attr("disabled", true);
                });

            $.post("/cbtconsole/AvailableMoneyRemarkServlet",
                {userid: userid},
                function (res) {
                    $("#originremark").html(res);
                });

            $.post("/cbtconsole/refundss/getavailable",
                {userid: userid},
                function (res) {
                    $("#available_o").val(res.available);
                    $("#available_cuu").html(res.available);
                });
            $("#confirm").attr("disabled", false);

            $.post("/cbtconsole/userinfo/queryOrderInfos", {"userId":userid},
                function (res) {
                    var json = eval(res);
                    var optionStr = "";
                    if(json.length > 0){
                        for (var i = 0; i < json.length; i++) {
                            optionStr += '<option value=' + json[i] + '>' + json[i] + '</option>';
                        }
                        $("#order_list").empty();
                        $("#order_list").append(optionStr);
                    }
                });

        });

        function confirm() {
            $("#confirm").attr("disabled", true);
            var pcomplainid = $("#pcomplainid").val();
            var porderid = $("#porderid").val();
            var available = $("#available_m").val();
            var remark = $("#remark").val();
            var modifyuser = $("#modifyuser").find("option:selected").text();
            var modifyuserid = $("#modifyuser").val();
            var password = $("#password_m").val();
            var userid = $('#puserid').val();
            var available_o = $("#available_o").val();
            var flag = $("#flag").val();
            if (flag == 1) {
                available = "-" + available;
            }
            var orderId = $("#order_list").val();
            if(orderId == null || orderId == "" || orderId == "0"){
                $("#notice_id").show();
            }else{
                $.post("/cbtconsole/UpdateAvailableMoneyServlet",
                    {
                        userid: userid,
                        available: available,
                        remark: remark,
                        modifyuser: modifyuser,
                        password: password,
                        modifyuserid: modifyuserid,
                        flag: flag,
                        orderid: orderId,
                        complainid: pcomplainid
                    },
                    function (res) {
                        if (res == 1) {
                            alert("变更成功");
                            window.opener.location.reload();
                            //无法同步在user.jsp和userpayment.jsp一起使用这个document的id available
                            //window.opener.document.getElementById("available").innerHTML=(parseFloat(available_o)+parseFloat(available)).toFixed(2);
                            window.close();
                        } else if (res == 0) {
                            alert("输入密码错误,请确认密码");
                            $("#confirm").attr("disabled", false);
                        } else {
                            alert("执行失败,请重试或联系管理员");
                            $("#confirm").attr("disabled", false);
                        }
                    });

                //            //setTimeout('$("#confirm").attr("disabled", false)',1000)
            }

        }
    </script>
</head>
<body>
<input type="hidden" value="0" id="available_o" class="available_o">
<input type="hidden" value="${param.userid }" id="puserid" class="puserid">
<input type="hidden" value="${param.orderid }" id="porderid" class="porderid">
<input type="hidden" value="${param.complainid }" id="pcomplainid" class="pcomplainid">
<table>
    <tr>
        <td>账户余额:</td>
        <td><span id="available_cuu" class="available_cuu"
                  style="color: red;font-weight: bold;font-size: 16px">0.00</span>&nbsp;USD
        </td>
    </tr>
    <tr>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>余额操作:</td>
        <td>
            <select id="flag" style="width: 150px;">
                <option value=2 selected="selected">余额奖励或补偿</option>
                <option value=0>余额校正增加</option>
                <option value=1>余额校正减少</option>
            </select>
            <input onkeydown="return check(event)" id="available_m" name="available_m" type="text" value="0"/>
            <span style="color: red;font-size: 12px;">(余额校正增加、减少用于系统问题影响的用户余额错误的校正)</span>
        </td>
    </tr>
    <tr>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>选择订单：</td>
        <td>
            <select id="order_list" style="width: 150px;">
                <option value="">请选择订单号</option>
            </select>
            <b style="color: red;">(*必选)</b>
        </td>
    </tr>
    <tr>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td></td>
        <td></td>
    </tr>
    <tr>
        <td>原始备注：</td>
        <td><textarea id="originremark" name="originremark" style="width:420px;height:80px"
                      disabled="disabled"></textarea></td>
    </tr>
    <tr>
        <td>新加备注：</td>
        <td><textarea id="remark" name="remark" style="width:420px;height:80px"></textarea></td>
    </tr>
    <tr>
        <td>修改人：</td>
        <td><select id="modifyuser" name="modifyuser"></select></td>
    </tr>
    <tr>
        <td>密码：</td>
        <td><input id="password_m" name="password_m" type="password" value=""/></td>
    </tr>
</table>
<input type="submit" id="confirm" onclick="confirm()" value="确认">
<span id="notice_id" style="color: red;display: none;">请选择订单号！</span>
</body>
</html>
