//确认退款并备注
function confirmAndRemark(id, state, action, type) {
    if (type == 0 || type == 1) {
        var iid = id;
        //实际退款金额
        var account = $("#account_" + id).val();
        if (account == '') {
            account = '0';
        }
        //余额补偿表id
        var additionid = $("#additionid_" + id).val();
        //投诉表id
        var complainid = $("#complainid_" + id).val();
        var userid = $("#user_" + id).val();
        //订单号
        var orderid = $("#order_" + id).val();
        //交易号
        var payid = $("#payid_" + id).val();
        //退款来源  0-提现   1-paypal  2-网上投诉  3-邮件投诉
        var typeid = $("#type_" + id).val();
        //申请退款金额
        var appcount = $("#appcount_" + id).val();
        //备注
        var remarkTxt = $("#remark_" + id).val();
        remarkTxt = remarkTxt.trim();
        if (remarkTxt == "") {
            //alert("请输入备注信息");
            $.jBox.tip('请输入备注信息', 'fail');
            return;
        }
        var resontype = $("#reson_" + id).val();
        var agreepeople = $("#dealMan").html();
        var refundOrderNo = $("#refundOrderId_" + id).val();

        //判断是否是管理员
        if (admJson.roletype == 0 || admJson.admName == "Ling" || admJson.admName == "Emma") {

            //判断操作类型
            var msgStr = "当前退款金额:" + account + ",当前状态为:“";
            switch (state) {
                case -1:
                    msgStr += "销售已驳回退款";
                    break;
                case -3:
                    msgStr += "管理员已拒绝退款";
                    break;
                case 0:
                    msgStr += "申请退款";
                    break;
                case 1:
                    msgStr += "销售已同意退款";
                    break;
                case 3:
                    msgStr += "管理员已同意退款";
                    break;
            }
            var status = -3;
            //action ==0是拒绝
            if (action == 0) {
                msgStr += "”,是否拒绝退款?";
                if (state == 0 || state == 1 || state == 3) {
                    var isConfirm = confirm(msgStr);
                    if (isConfirm) {
                        submitRemark(id, remarkTxt, account, userid, refundOrderNo, status, action, type, null);
                    }
                } else if (state == 2) {
                    //alert("退款已完结,不能拒绝退款");
                    $.jBox.tip('退款已完结,不能拒绝退款', 'fail');
                } else if (state == -1) {
                    //alert("退款已完结,不能确认退款");
                    $.jBox.tip('销售驳回退款,不能拒绝退款', 'fail');
                } else if (state == -2) {
                    //alert("客户取消退款 ,不能拒绝退款");
                    $.jBox.tip('客户取消退款 ,不能拒绝退款', 'fail');
                } else if (state == -3) {
                    //alert("管理员拒绝退款,不能拒绝退款");
                    $.jBox.tip('管理员拒绝退款,不能拒绝退款', 'fail');
                }

            } else {
                msgStr += "”,订单号:" + refundOrderNo + ",是否确认退款?";
                if (state == 0 || state == 1 || state == 3) {
                    var isConfirm = confirm(msgStr);
                    if (isConfirm) {
                        if ((state == 1 || state == -1) && admJson.admName == "Ling") {
                            //Ling的退款
                            showSecVild(id, state, admJson.id, type);
                        } else if ((state == 3 || state == -3) && admJson.admName == "Emma") {
                            //Emma的退款
                            showSecVild(id, state, admJson.id, type);
                        } else {
                            $.jBox.tip('当前无权限确认退款', 'fail');
                        }
                    }
                } else if (state == 2) {
                    //alert("退款已完结,不能确认退款");
                    $.jBox.tip('退款已完结,不能确认退款', 'fail');
                } else if (state == -1) {
                    //alert("退款已完结,不能确认退款");
                    $.jBox.tip('销售驳回退款,不能确认退款', 'fail');
                } else if (state == -2) {
                    //alert("客户取消退款 ,不能确认退款");
                    $.jBox.tip('客户取消退款 ,不能确认退款', 'fail');
                } else if (state == -3) {
                    //alert("管理员拒绝退款,不能确认退款");
                    $.jBox.tip('管理员拒绝退款,不能确认退款', 'fail');
                }
            }

        } else if (admJson.roletype == 3 || admJson.roletype == 4) {
            if (state == 0) {
                var msgStr = "";
                var status = 1;
                if (action == 0) {
                    msgStr = "当前退款金额:" + account + ",订单号:" + refundOrderNo + ",是否拒绝退款?";
                    status = -1;
                } else {
                    msgStr = "当前退款金额:" + account + ",订单号:" + refundOrderNo + ",是否确认退款?";
                }
                var isConfirm = confirm(msgStr);
                if (isConfirm) {
                    submitRemark(id, remarkTxt, account, userid, refundOrderNo, status, action, type, null);
                }
            } else {
                //alert("你不是管理员,无其他操作权限");
                $.jBox.tip('你不是管理员,无其他操作权限', 'fail');
            }
        } else {
            //alert("你不是管理员或者销售,无其他操作权限");
            $.jBox.tip('你不是管理员或者销售,无其他操作权限', 'fail');
        }

    } else {
        //alert("只能paypal退款");
        $.jBox.tip('只能paypal退款或者余额提现', 'fail');
    }
}

//余额提现
//paypal退款


function showSecVild(id, state, userid, type) {
    $("#refund_id").val(id);
    $("#refund_type").val(type);
    $("#refund_state").val(state);
    $("#div_secvlid").show();
    $("#select_userid").val(userid);
}

function submitRemark(id, remarkTxt, account, userid, refundOrderNo, status, action, type, message) {
    var nwstate = status;
    if (type == 1) {
        if (action == 1) {
            if ((status == 1 || status == -1) && admJson.admName == "Ling") {
                nwstate = 3;
            } else if ((status == 3 || status == -3) && admJson.admName == "Emma") {
                nwstate = 3;
            }
        } else if (action == 0) {
            if (status == 1 && admJson.admName == "Ling") {
                nwstate = -3;
            } else if (status == 3 && admJson.admName == "Emma") {
                nwstate = -3;
            }
        }
    } else if (type == 0) {
        if (action == 1) {
            if ((status == 1 || status == -1) && admJson.admName == "Ling") {
                nwstate = 3;
            } else if ((status == 3 || status == -3) && admJson.admName == "Emma") {
                nwstate = 2;
            }
        } else if (action == 0) {
            if (status == 1 && admJson.admName == "Ling") {
                nwstate = -3;
            } else if (status == 3 && admJson.admName == "Emma") {
                nwstate = -3;
            }
        }
    }

    $.ajax({
        type: 'POST',
        url: '/cbtconsole/refundss/confirmAndRemark',
        data: {"id": id, "remark": remarkTxt, "account": account, "userid": userid, "refundOrderNo":refundOrderNo,"status": nwstate},
        success: function (data) {
            if (data.ok) {
                if (type == 1) {//paypal
                    if (action == 1 && nwstate == 3 && admJson.admName == "Ling" && account <= 50) {
                        $.jBox.tip('开始退款，请等待执行结果', 'success');
                        //subRefund(id);
                        refundByPayPalApi(userid,id);
                    } else if (action == 1 && nwstate == 3 && admJson.admName == "Emma" && account > 50) {
                        $.jBox.tip('开始退款，请等待执行结果', 'success');
                        //subRefund(id);
                        refundByPayPalApi(userid,id);
                    } else {
                        if (!(message == null || message == "")) {
                            alert(message);
                        }
                        window.location.reload();
                    }
                } else if (type == 0) {//balance
                    $.jBox.tip('此操作已保存', 'success');
                    window.location.reload();
                }

            } else {
                alert(data.message);
            }
        },
        error: function (res) {
            //alert("提交备注错误，请联系管理员--错误:"+res.statusText);
            $.jBox.tip('提交备注错误，请联系管理员', 'fail');
        }
    });

}

function checkSecvlidPwd() {
    var state = $("#refund_state").val();
    var id = $("#refund_id").val();
    var type = $("#refund_type").val();
    var password = $("#secvlid_pwd").val();
    var passwordTwo = $("#secvlid_pwd_two").val();
    var edit_type = $("#edit_type").val();
    var userid = $("#select_userid").val();
    if (password == null || password == "") {
        //alert("请输入验证密码");
        $.jBox.tip('请输入验证密码', 'fail');
        return;
    }
    if (userid == null || userid == "" || userid == 0) {
        //alert("获取用户失败,请重试");
        $.jBox.tip('获取用户失败,请重试', 'fail');
        return;
    }
    //subRefund(refund_id);
    //alert(id);
    //alert(password);


    $.ajax({
        type: 'POST',
        url: '/cbtconsole/secondaryValidation/checkExistsPassword',
        data: {userid: userid, password: password},
        success: function (data) {
            if (data.ok) {
                hideDivSecvlid();
                //实际退款金额
                var account = $("#account_" + id).val();
                if (account == '') {
                    account = '0';
                }
                //余额补偿表id
                var additionid = $("#additionid_" + id).val();
                //投诉表id
                var complainid = $("#complainid_" + id).val();
                var userid = $("#user_" + id).val();
                //订单号
                var orderid = $("#order_" + id).val();
                //交易号
                var payid = $("#payid_" + id).val();
                //退款来源  0-提现   1-paypal  2-网上投诉  3-邮件投诉
                var typeid = $("#type_" + id).val();
                //申请退款金额
                var appcount = $("#appcount_" + id).val();
                //备注
                var remarkTxt = $("#remark_" + id).val();
                remarkTxt = remarkTxt.trim();
                if (remarkTxt == "") {
                    //alert("请输入备注信息");
                    $.jBox.tip('请输入备注信息', 'fail');
                    return;
                }
                var resontype = $("#reson_" + id).val();
                var agreepeople = $("#dealMan").html();
                var refundOrderNo = $("#refundOrderId_" + id).val();
                if (account > 50 && admJson.admName != "Emma") {
                    submitRemark(id, remarkTxt, account, userid,refundOrderNo, 3, 1, 1, "保存成功,退款金额大于$50,请Emma操作");
                } else {
                    submitRemark(id, remarkTxt, account, userid,refundOrderNo, 3, 1, 1, null);
                }
            } else {
                //alert(data.message);
                $.jBox.tip(data.message, 'fail');
            }
        },
        error: function () {
            //alert("提交验证错误，请联系管理员");
            $.jBox.tip('提交验证错误，请联系管理员', 'fail');
        }
    });
}

function hideDivSecvlid() {
    $("#div_secvlid").hide();
    $("#secvlid_pwd").val("");
}

function checkSecvlidPwdAddEdit() {
    var password_one = $("#secvlid_pwd_addedit").val();
    var password_two = $("#secvlid_pwd_addedit_two").val();

    var userid = $("#userid_lst").val();
    var remark = $("#secvlid_addedit_remark").val();

    if (password_one == null || password_one == "") {
        //alert("请输入密码");
        $.jBox.tip('请输入密码', 'fail');
        return;
    } else if (password_two == null || password_two == "") {
        //alert("请再次输入密码");
        $.jBox.tip('请再次输入密码', 'fail');
        return;
    }
    if (password_one != password_two) {
        $.jBox.tip('两次密码输入不一致', 'fail');
        //alert("两次密码输入不一致");
        return;
    } else {
        var reg = new RegExp("[`~!@#$%^&*()+=|{}':;',\\[\\].<>~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？?]");
        var reg2 = /[0-9]/;
        var reg3 = /[A-Za-z]/;
        if (password_one.length < 8) {
            //alert("输入密码最少8位");
            $.jBox.tip('输入密码最少8位', 'fail');
            return;
        } else if (!reg.test(password_one)) {
            //alert("输入框中必须含有特殊字符");
            $.jBox.tip('输入框中必须含有特殊字符', 'fail');
            return;
        } else if (!reg2.test(password_one)) {
            //alert("输入框中必须含有数字");
            $.jBox.tip('输入框中必须含有数字', 'fail');
            return;
        } else if (!reg3.test(password_one)) {
            //alert("输入框中必须字母");
            $.jBox.tip('输入框中必须字母', 'fail');
            return;
        } else if (reg.test(password_one) && reg2.test(password_one) && reg3.test(password_one)) {
            var edit_type = $("#edit_type").val();
            if (edit_type == 0) {
                $.ajax({
                    type: 'POST',
                    dataType: 'text',
                    url: '/cbtconsole/secondaryValidation/insertSecondaryValidation',
                    data: {userid: userid, password: password_one, remark: remark},
                    success: function (data) {
                        var json = eval("(" + data + ")");
                        if (json.ok) {
                            //alert("执行成功");
                            $.jBox.tip('执行成功', 'fail');
                            //window.close();
                        } else {
                            //alert(json.message);
                            $.jBox.tip(json.message, 'fail');
                        }
                    },
                    error: function () {
                        //alert("执行失败,请联系管理员");
                        $.jBox.tip('执行失败,请联系管理员', 'fail');
                    }
                });
            } else if (edit_type == 1) {
                var secvlid_old = $("#secvlid_old").val();
                if (secvlid_old == null || secvlid_old == "") {
                    //alert("请输入原始密码");
                    $.jBox.tip('请输入原始密码', 'fail');
                    return;
                }
                $.ajax({
                    type: 'POST',
                    dataType: 'text',
                    url: '/cbtconsole/secondaryValidation/updateByUserId',
                    data: {userid: userid, oldPassword: secvlid_old, password: password_one, remark: remark},
                    success: function (data) {
                        var json = eval("(" + data + ")");
                        if (json.ok) {
                            //alert("执行成功");
                            $.jBox.tip('执行成功', 'success');
                            hideDivSecvlidAddEdit();
                        } else {
                            //alert(json.message);
                            $.jBox.tip(json.message, 'fail');
                        }
                    },
                    error: function () {
                        //alert("执行失败,请联系管理员");
                        $.jBox.tip('执行失败,请联系管理员', 'fail');
                    }
                });
            }
        }
    }
}

function showSecValid(type) {
    $("#userid_lst").val(admJson.id);
    $("#div_secvlid_addedit").show();
    $("#edit_type").val(type);
    if (type == 0 || type == "0") {
        $("#div_secvlid_title").text("设置验证密码");
        $("#add_update").val("新增");
        $("#secvlid_old").hide();
        $("#check_old").hide();
    } else if (type == 1 || type == "1") {
        $("#div_secvlid_title").text("修改验证密码");
        $("#add_update").val("修改");
        $("#secvlid_old").show();
        $("#check_old").show();
    }

    //var url = "/cbtconsole/apa/secondaryValidation.html?edittype="+type;
    //var config = "width=500,height=200,top=300,left=600,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no"
    //window.open(url, "二次验证密码", config);
}

function hideDivSecvlidAddEdit() {
    $("#div_secvlid_addedit").hide();
    $("#secvlid_old").val("");
    $("#secvlid_pwd").val("");
    $("#secvlid_pwd_two").val("");
    $("#secvlid_remark").val("");
}


function oldSubmitRemark(id, remarkTxt, agreepeople, typeid, appcount, account,
                         orderid, payid, userid, additionid, complainid, resontype, message) {
    $.ajax({
        type: 'POST',
        url: '/cbtconsole/refundss/doRemark',
        data: {
            "id": id, "remark": remarkTxt, "agreepeople": agreepeople,
            "typeid": typeid, "appcount": appcount, "account": account,
            "orderid": orderid, "payid": payid, "userid": userid,
            "additionid": additionid, "complainid": complainid, "resontype": resontype
        },
        success: function (res) {
            if (res == '-1') {
                alert("请输入正确的退款金额 ,当前输入的退款金额为:" + account);
                return;
            } else {
                if (res.length < 5) {
                    var remark = document.getElementById("remark_" + id);
                    remark.style.background = 'wheat';
                    if (!(message == null || message == "")) {
                        alert(message);
                    }
                    window.location.reload();
                } else {
                    $.dialog.confirm("Message", res, function () {
                        var remark = document.getElementById("remark_" + id);
                        remark.style.background = 'wheat';
                        window.location.reload();
                    }, function () {
                    });
                }
            }
        },
        error: function (res) {
            alert("提交备注错误，请联系管理员--错误:" + res.statusText);
        }
    });
}


function mhide() {
    window.location.reload();
    $('#form_saleid').val('');
    $("#div_iframe").hide();
}

function subRefund(rid) {
    if (rid == '') {
        return;
    }
    $("#batchrefund").removeAttr("onclick");
    $("#batchrefund").removeClass("btn").addClass("btn2");

    $(".btn_tk").attr('disabled', true);
    $(".btn_tk").css('background-color', '#9d9d9d');
    var btn = document.getElementById("refund_submit");
    var frm = document.forms["refund_form"];

    $('#form_saleid').val(rid);
    frm.action = "http://192.168.5.156:8080/sale-refund";
    frm.target = "refund_iframe";
    var hwid = ($(window).width() - $("#div_iframe").width()) / 2;
    var heig = ($(window).height() - $("#div_iframe").height()) / 2;
    $("#div_iframe").show().css({"left": hwid, "top": heig});
    frm.submit();
    var ifm = document.getElementById("refund_iframe");
    if (ifm != null && ifm.attachEvent) {
        ifm.attachEvent("onload", function () {
            btn.disabled = "";
            var str = ifm.contentWindow;
            alert(str.document.body.innerHTML);
            ifm.src = "about:blank";
            ifm.detachEvent("onload", arguments.callee);
        });
    } else {
        ifm.onload = function () {
            btn.disabled = "";
            var str = ifm.contentWindow;
            alert(str.document.body.innerHTML);
            ifm.src = "about:blank";
            ifm.onload = null;
        }
    }

    window.location.reload();
    return;
}


function refundByPayPalApi(userId,rdId){
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: '/cbtconsole/refundss/refundByPayPalApi',
        data: {
            userId: userId,
            rdId: rdId
        },
        success: function (data) {

            if (data.ok) {
                alert('退款成功');
                window.location.reload();
            } else {
                alert(data.message);
            }
        },
        error: function (XMLResponse) {
            alert('error');
        }
    });
}



$(function () {
    $("#_status").val(statue);
    $("#from_status").val(type);
    // 初始页面默认选中状态值
    var url = window.location.href;
    var action = url.split("/cbtconsole/")[1];
    if (action == "refundss/getAllRefundApply") {
        if (consoleName == 'Emma') {
            $("#_status").val(3);
        } else if (consoleName == 'Ling') {
            $("#_status").val(1);
        } else {
            $("#_status").val(0);
        }
        $("#search_btn").click();
    }

    $("select[id^='remarkList']").change(function () {
        var txt = $(this).val();
        var obj = $(this).parent().find("textarea");
        if (txt != "其他原因") {
            obj.val(txt);
            obj.attr("readonly", "readonly");
        } else {
            obj.html("");
            obj.removeAttr("readonly");
            obj.focus();
        }
        $(this).hide();
    })

    if (consoleName != 'Emma') {
        $("button[name^='tijiao']").each(function () {
            $(this).css("display", "none");
        })
    }
});


function finish(refundId, userEmail, userName, price, currency) {
    var additionid = $("#additionid_" + refundId).val();
    if (refundId != 0 && userEmail != "") {
        $.ajax({
            type: 'POST',
            url: '/cbtconsole/refundss/finishRefund',
            data: {
                "refundId": refundId,
                "userEmail": userEmail,
                "userName": userName,
                "appcount": price,
                "currency": currency,
                "additionid": additionid
            },
            success: function (res) {
                alert(res.message);
                window.location.reload();
            },
            error: function (res) {
                alert(2);
            }
        });

    }
}


function resetPar() {
    $("#userid").val("");
    $("#useraccount").val("");
    $("#applyDate").val("");
    $("#agreeTime").val("");
    $("#_status").val(1);
}


function deal(uid, appcount, rid, flag, appcurrency) {
    var refuse = "";
    if (flag == 2) {
        refuse = $('#refuse_' + rid).val();
    }
    if (flag == 2 && refuse == '') {
        alert('请填写拒绝理由');
        return;
    }
    var type = $('#type_' + rid).val();
    var admName = $("#dealMan").text();
    //int uid,double appcount,int rid,String admuser
    $.ajax({
        type: 'POST',
        url: '/cbtconsole/refundss/dealRefund',
        data: {
            uid: uid,
            appcount: appcount,
            rid: rid,
            admuser: admName,
            agreeOrNot: flag,
            "appcurrency": appcurrency,
            "refuse": refuse,
            type: type
        },
        success: function (res) {
            alert(res.message);
            window.location.reload();
        },
        error: function (res) {
            alert(res.message);
        }
    });
}

function reportForm() {
    window.location.href = "/cbtconsole/refundss/report?sdate=&edate=&reason=0&page=1"
}

//5.27因新需求废弃标准化选项   废弃以下两个方法
function selectData(id) {
    $("#remarkList_" + id).show();
}

function hasSelect(id) {
    $("#remarkList_" + id).hide();
}

var startrow = 1;
$("#prepage").click(function () {
    var pagenow = parseInt($("#pagenow").text(), 10);
    if (pagenow == 1) {
        alert("当前是首页");
    } else {
        startrow = pagenow - 1;
        search();
    }
});
$("#nextpage").click(function () {
    var pagenow = parseInt($("#pagenow").text(), 10);
    var pagecount = parseInt($("#pagecount").text(), 10);
    if (pagenow == pagecount) {
        alert("当前是尾页");
    } else {
        startrow = pagenow + 1;
        search();
    }
});
$("#jumpToPage").click(function () {
    var pages = $("#topage").val();
    var pagecount = parseInt($("#pagecount").text(), 10);
    if (pages == null) {
        alert("请输入页码");
    } else if (isNaN(pages)) {
        alert("请输入正确的页码");
    } else if (pages < 1 || pages > pagecount) {
        alert("页码超出范围");
    } else {
        startrow = pages;
        search();
    }
});

function enterToJump() {
    var pages = $("#topage").val();
    var pagecount = parseInt($("#pagecount").text(), 10);
    if (pages == null) {
        alert("请输入页码");
    } else if (isNaN(page)) {
        alert("请输入正确的页码");
    } else if (pages < 1 || pages > pagecount) {
        alert("页码超出范围");
    } else {
        startrow = pages;
        search();
    }
}

function search() {
    var uid = $("#userid").val().trim();
    if (uid == "") {
        uid = 0;
    }
    var useraccount = $("#useraccount").val().trim();
    var applyDate = $("#applyDate").val().trim();
    var agreeTime = $("#agreeTime").val().trim();
    var status = $("#_status").val().trim();
    var type = $("#from_status").val().trim();
    /* if(status==2 && consoleName=='Emma'){
        status=6;
    }
    if(status==4 && consoleName=='Emma'){
        status=7;
    } */
    if (uid != "" || useraccount != "" || applyDate != "" || status != "") {
        window.location = '/cbtconsole/refundss/searchByParam?userid=' + uid + '&username=' + useraccount
            + '&appdate=' + applyDate + '&agreeTime=' + agreeTime + '&status=' + status + '&startrow=' + startrow + "&type=" + type;
    } else {
        alert("请至少输入一个查询条件");
    }
}


//6.6新需求     Emma添加反馈
function addFeedBack(id, orderno) {
    var feedBackTxt = $("#feedback_" + id).val();
    var aa = $.trim(feedBackTxt);
    if (aa == "") {
        alert('请输入内容');
        return;
    }
    $.ajax({
        type: 'POST',
        url: '/cbtconsole/refundss/addFeedback',
        data: {rid: id, feedback: feedBackTxt, orderno: orderno},
        success: function () {
            var remark = document.getElementById("feedback_" + id);
            remark.style.backgroundColor = 'wheat';
        },
        error: function () {
            alert("反馈失败了，请联系管理员");
        }
    });
}

function fnmsg(count) {
    if (count < 1) {
        return;
    }
    window.location = "/cbtconsole/refundss/messages?state=N";

}

function getRecordsDetail(uid) {
    window.location = "/cbtconsole/rechargeRecord/findRecordByUid?uid=" + uid;
}

function changeavailable(userid, orderid, cid) {
    window.open("/cbtconsole/website/change_available.jsp?userid=" + userid + "&available=0&orderid=" + orderid + "&complainid=" + cid, "windows", "height=500,width=900,top=300,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
}

function fnsubmint() {
    var ruserid = $("#ruserid").val();
    if (ruserid == "") {
        alert("用户id不可为空");
        return false;
    }
    var rappcount = $("#rappcount").val();
    if (rappcount == "") {
        alert("退款金额 不可为空");
        return false;
    }
    var rpaypal = $("#rpaypal").val();
    if (rpaypal == "") {
        alert("paypal账号不可为空");
        return false;
    }
    var rorderid = $("#rorderid").val();
    var rpayid = $("#rpayid").val();
    var rtype = $("#rtype").val();

    $.ajax({
        type: 'POST',
        dataType: 'text',
        url: '/cbtconsole/refundss/addRefund',
        data: {
            ruserid: ruserid,
            rappcount: rappcount,
            rpaypal: rpaypal,
            rorderid: rorderid,
            rpayid: rpayid,
            rtype: rtype
        },
        success: function (res) {
            if (res > 0) {
                window.location.reload();
            } else {
                alert('添加失败，请重新添加');
            }
        },
        error: function (XMLResponse) {
            alert('error');
        }
    });

}

/* 全选  */
function fnselect() {
    var checked = $("#checked").val();
    $(".checkpid").prop("checked", checked == '0');
    $("#checked").val(checked == '0' ? '1' : '0');
}

/* 批量退款 */
function fnrefund() {
    var checked = "";
    fnselect();
    $(".checkpid:checked").each(function () {
        checked += this.value + ',';
    });
    /* $("#formpids").val(checked); */
    if (checked == '') {
        return;
    }
    subRefund(checked);
}

/**
 * 根据输入的金额查询可用订单
 * @param userId
 * @param accId
 */
 function findCanRefundOrderNo(userId,accId,selectId,orderNo,refundType){
    $("#" + selectId).empty();
    $("#" + selectId).append('<option value="">请选择订单号</option>');
    var refundAmount = $("#" + accId).val();
    if(userId == 0){
        $.jBox.tip('获取用户ID失败，请重新刷新页面', 'fail');
    }else if(refundAmount == null || refundAmount == "" || refundAmount == 0){
        $.jBox.tip('获取输入金额失败，请确认输入是否正确', 'fail');
    }else{
        if(refundType == 0){
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: '/cbtconsole/refundss/findCanRefundOrderNo',
                data: {
                    userId: userId,
                    refundAmount: refundAmount
                },
                success: function (data) {

                    if (data.ok) {
                        if(data.total > 0){
                            $("#" + selectId).empty();
                            var json = data.data;
                            var context = '';
                            for(var key in json){
                                context +='<option value="'+key+'">'+json[key]+'</option>';
                            }
                            $("#" + selectId).append(context);
                        }else{
                            $.jBox.tip('无匹配的订单可用', 'fail');
                        }
                    } else {
                        $.jBox.tip(data.message, 'fail');
                    }
                },
                error: function (XMLResponse) {
                    alert('error');
                }
            });
        }else if(refundType == 1){
            if(orderNo == null || orderNo == ""){
                $.jBox.tip('无匹配的订单可用', 'fail');
            }else{
                $.jBox.tip('当前PayPal申述，使用原有订单', 'success');
                $("#" + selectId).empty();
                $("#" + selectId).append('<option value="'+orderNo+'">'+orderNo+'</option>');
            }

        }
    }
 }


jQuery.browser = {};
(function () {
    jQuery.browser.msie = false;
    jQuery.browser.version = 0;
    if (navigator.userAgent.match(/MSIE ([0-9]+)./)) {
        jQuery.browser.msie = true;
        jQuery.browser.version = RegExp.$1;
    }
})();