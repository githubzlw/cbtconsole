
function fnChangeMethod(f_methodc, f_price, f_date, c_id, c_namec) {
	fnClose();
	$("#actual_ffreight").val(f_price);
	var mode_transport = $("#mode_transport").val();
	var sp = mode_transport.split("@");
	mode_transport = f_methodc + "@" + f_date + "@" + c_namec + "@" + f_price
			+ "@" + sp[sp.length - 1];
	$("#mode_transport").val(mode_transport);
	j = 0;
	changeFee = 1;
}
function fnClose() {
	$.dialog({
		id : 'shipping_method'
	}).close();
}
function searchCountry(cname, orderNo){
    $.ajax({
        url:"/cbtconsole/orderInfo/queryCountryNameByOrderNo.do",
        type : "post",
        dataType:"json",
        data :{"orderNo":orderNo},
        success:function(data){
            if(data.ok){
                if(cname!=data){
                    $("#od_country").css("display","inline");
                }
            }
        }
    });
}
function fnShippingMethod(weight) {
	var actual_lwh = $("#actual_lwh").val();
	var currency = $("#currency").val();
	var country = $("#country").val();
	$.dialog({
		id : 'shipping_method',
		width : '852px',
		height : '510px',
		fixed : true,
		max : false,
		min : false,
		skin : 'discu',
		lock : true,
		title : 'Change Combine Shipping Method',
		drag : false,
		cache : false,
		resize : false,
		content : 'url:/cbtconsole/cbt/shipping-method.jsp?sum_v='
				+ (actual_lwh == "" ? 0 : actual_lwh) + '&sum_w=' + weight
				+ '&max_w=${max_w}&currency=' + currency + '&state=1&country='
				+ country
	});
}

// 提醒用户支付运费
function fnEmail_payfright() {
	var orderNo = $("#orderNo").val();
	var currency = $("#currency").html();
	var actual_ffreight = $("#actual_ffreight").val();// 实际运费
	var pay_ffreight = $("#pay_price_tow").val();// 支付运费金额
	var weight = $("#actual_weight_estimate").html();// 原本重量
	var actual_weight = $("#actual_weight").html();// 实际重量
	var arrive_time = $("#expect_arrive_time").val();// 到货日期
	var transport_time = $("#transport_time").val();// 国际运输时间
	if (transport_time == "") {
		alert("运输时间为空！");
		return;
	}
	if (arrive_time == "") {
		alert("预计到货时间为空！");
		return;
	}
	var remark = $("#remark").val();
	var price = $("#price").val();
	var copyEmail = $("#email").val();
	var userid = $("#userId").val();
	$.dialog({
		id : 'login',
		width : '550px',
		height : 465,
		fixed : true,
		max : false,
		min : false,
		skin : 'discuz',
		lock : true,
		title : '提醒用户支付运费',
		drag : false,
		content : 'url:/cbtconsole/website/email_payfright.jsp?orderNo='
				+ orderNo + '&currency=' + currency + '&actual_ffreight='
				+ actual_ffreight + '&pay_price_tow=' + pay_price_tow
				+ +'&weight=' + weight + '&actual_weight=' + actual_weight
				+ '&arrive_time=' + arrive_time + '&transport_time='
				+ transport_time + '&copyEmail=' + copyEmail + '&userid='
				+ userid
	});
}

function fnChange(id, thi) {
	if ($(thi).prop("checked")) {
		$(thi).parent().parent().css("background-color", "#84FFFF");
	} else {
		$(thi).parent().parent().css("background-color", "");
	}
}
function getNum(youdrder,thi) {
	var num= $(thi).val()
	if (num>youdrder){
		alert("你输入的拆单数量不能大于订单数量")
	}
}

function fnChangeAll(state) {
	if (state == 0) {
		$("#orderDetail input:checkbox").prop("checked", "checked");
		$("#orderDetail tr:gt(0)").css("background-color", "#84FFFF");
	} else {
		$("#orderDetail input:checkbox").each(function() {
			this.checked = !this.checked;
			if (this.checked) {
				$(this).parent().parent().css("background-color", "#84FFFF");
			} else {
				$(this).parent().parent().css("background-color", "");
			}
		});
	}
}

// 确认到账
function fnConfirmnamebtn() {
	var userid = $("#userId").html();
	var orerNos = $("#orderNo").val();
	window
			.open(
					'/cbtconsole/website/paymentConfirm.jsp?orderNo=' + orerNos
							+ '&userid=' + userid,
					"windows",
					"height=200,width=600,top=250,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
}

// 重量失焦计算运输方式
// 提示总重量
var changeFee = 0;
function fnChangeVolume(shipMethod) {
	var volume_l = $("#volume_l").val();
	var volume_w = $("#volume_w").val();
	var volume_h = $("#volume_h").val();
	var actual_weight = $("#actual_weight").val();
	var sum_vv = $("#actual_lwh").val();
	if (volume_l != "" && volume_w != "" && volume_h != ""
			&& actual_weight != "") {
		sum_vv = parseFloat(volume_l) * parseFloat(volume_h)
				* parseFloat(volume_w) / 1000000;
		$("#actual_lwh").val(sum_vv.toFixed(5));
	} else {
		return;
	}
	if (shipMethod == "0") {
		return;
	}
	var weight = 0;
	var sum_ww = actual_weight;
	if (sum_ww == "") {
		return;
	}
	var currency = $("#currency").val();
	var countryId = document.getElementById("country").value;
	$.post("/cbtconsole/feeServlet", {
		action : 'getCost',
		className : 'ZoneServlet',
		countryid : countryId,
		weight : sum_ww,
		volume : sum_vv == "" ? 0 : sum_vv,
		singleweightmax : 0,
		currency : currency
	}, function(res) {
		$("#loading_ex").hide();
		$("#freight_tr1").show();
		var jsons = eval(res);
		var tab = $("#express_change tbody");
		var tab2 = $("#shopfeediv2 tbody");
		$("#shopfeediv2 tbody tr:gt(0)").remove();
		$("#express_change tbody tr:gt(0)").remove();
		changeFee = 2;
		var actual_ffreight = parseInt($("#actual_ffreight").val());
		for (var i = 0; i < jsons.length; i++) {
			var json = jsons[i];
			if (json.name == shipMethod) {
				if (actual_ffreight < parseInt(json.result)) {
					actual_ffreight = json.result;
				}
				$("#actual_ffreight").val(actual_ffreight);
				changeFee = 1;
				break;
			}
		}
		if (changeFee == 2) {
			alert("原来的运输方式不再可行，请切换其他运输方式");
			fnShippingMethod(sum_ww);
		}
	});
}

// 订单拆分
function fnSplitOrder(orderno, email, paytime) {
    if(orderno == null || orderno == ""){
        alert('获取订单号失败');
        return ;
    }
	var content = "";
	var tab = $("#orderDetail tr").length;
	var tab_yx = 0;
	var time_ = 0;
	var time = 0;
	var odids = "";
	var check_num = 0;
	// 判断是否包含输入数量拆单
	var flag = false;
	// 根据数量拆单json
	var jsonArr = {};
	for (var i = 1; i < tab; i++) {
		var cansonl = $("#user_cancel_" + (i - 1)).html();// 是否取消
		var od_time = $("#orderd_delivery_" + (i - 1)).html();// 交期时间
		var check = $("#orderDetail tr").eq(i).find(".choose_chk");
		if ($(check).next().val() != 2) {
			tab_yx++;
		}
		if (!$(check).is(":checked")) {
			// 没有选中
			if ($(check).next().val() != 2) {
				check_num++;
			}
			if (parseInt(od_time) > time_) {
				time_ = parseInt(od_time);
			}
		} else {
			odids += $(check).val() + "@";
			if (parseInt(od_time) > time) {
				time = parseInt(od_time);
			}
		}
	}
	if (check_num == 0 && !flag) {
		alert("请选择先出货的商品");
		return;
	}
	if (check_num == tab_yx) {
		alert("请选择后出货的商品");
		return;
	}
	$("#split_order_btn").hide();
	time = addDate(time, paytime);
	time_ = addDate(time_, paytime);
	// 不取消
	content = '<div id="split_div">选中的为后出货的商品<br><input type="radio" checked="checked" onclick="fnRodio(1)" value="1" name="splitOrder" id="spilitOrder1"><label for="spilitOrder1">两次出货</label><input type="radio" onclick="fnRodio(0)" value="0" name="splitOrder" id="spilitOrder2"><label for="spilitOrder2">取消产品</label><br>';
	content += '出货时间-<br>先出货订单：<input name="transport_time" id="ch_date1" readonly="readonly" value="'
			+ time_ + '" onfocus="WdatePicker()" /><br>';
	content += '<span style="color: red">【注意:拆分订单需要10s左右,</span><br>';
	content += '<span style="color: orange">数据同步到本地需要3分钟</span><br>';
	content += '<span style="color: blue">左右请耐心等待！】</span></div>';
	$
			.dialog({
				title : '确认拆分？',
				content : content,
				max : false,
				min : false,
				lock : true,
				drag : false,
				fixed : true,
				ok : function() {

					var state = $("#split_div input[type=radio]:checked ")
							.val();
					if (state == 1) {
						time_ = $("#ch_date1").val();
					} else {
						time_ = $("#ch_date1").val();
					}
					$.ajax({
						type : 'POST',
						url : '/cbtconsole/orderSplit/doSplit.do',
						data : {
							"orderno" : orderno,
							"odids" : odids,
							"state" : state
						},
						success : function(data) {
							if (data.ok) {
                                var text = " <div id=\"split_div\">密送人:<input name=\"email\" id=\"email\" type=\"text\"  " +
									"onfocus=\"if (value =='选填'){value =''; this.style.color='#000';}\" placeholder=\"选填\" " +
									"onblur=\"if (value ==''){value='选填'; this.style.color='#999999';}\"  />"
									"</div>";
                                $.dialog({
                                    title : '拆单成功是否要发送邮件！',
                                    content : text,
                                    max : false,
                                    min : false,
                                    lock : true,
                                    drag : false,
                                    fixed : true,
                                    ok : function() {
                                        var orderNew = data.data;
                                        var email = $('#email').val();
                                        var websiteType = $('#website_type').val();
                                        sendSplitSuccessEmail(orderno,orderNew,odids,time_,state,email);
                                    },
                                    cancel : function() {
                                        window.location.reload();
                                    }
                                });

							} else {
								alert(data.message);
								$("#split_order_btn").show();
							}
						},
						error : function(res) {
							alert("执行错误,请联系管理员");
							$("#split_order_btn").show();
						}
					});
				},
				cancel : function() {
				}
			});
}
function refreshParent() {
	parent.location.reload();
}
// 拆分Drop Ship 订单 fnSplitDropShipOrder
function fnSplitDropShipOrder(orderno, email, paytime) {
    if(orderno == null || orderno == ""){
        alert('获取订单号失败');
        return ;
    }
	var content = "";
	var tab = $("#orderDetail").find("tr").length;
	var tab_yx = 0;
	var time_ = 0;
	var time = 0;
	var odids = "";
	var check_num = 0;
	for (var i = 0; i < tab; i++) {
		var cansonl = $("#user_cancel_" + (i - 1)).html();// 是否取消
		var od_time = $("#orderd_delivery_" + (i - 1)).html();// 交期时间
		var check = $("#orderDetail").find("tr").eq(i)
				.find(".choose_chk");
		if ($(check).next().val() != 2) {
			tab_yx++;
		}

		if ($(check).is(":checked")) {
			odids += $(check).val() + "@";
			if ($(check).next().val() != 2) {
				check_num++;
			}
			if (parseInt(od_time) > time_) {
				time_ = parseInt(od_time);
			}
		} else {
			if (parseInt(od_time) > time) {
				time = parseInt(od_time);
			}
		}
	}
	if (check_num == 0) {
		alert("请选择先出货的商品");
		return;
	}
	if (check_num == tab_yx) {
		alert("请选择后出货的商品");
		return;
	}
	$("#split_order_btn").hide();
	time = addDate(time, paytime);
	time_ = addDate(time_, paytime);
	// 不取消
	content = '<div id="split_div">选中的为后出货的商品<br><input type="radio" checked="checked" onclick="fnRodio(1)" value="1" name="splitOrder" id="spilitOrder1"><label for="spilitOrder1">两次出货</label><input type="radio" onclick="fnRodio(0)" value="0" name="splitOrder" id="spilitOrder2"><label for="spilitOrder2">建议退款</label><br>';
	content += '出货时间-<br>先出货订单：<input name="transport_time" id="ch_date1" readonly="readonly" value="'
			+ time_ + '" onfocus="WdatePicker()" /><br>';
	content += '<span style="color: red">【注意:拆分订单需要10s左右,</span><br>';
	content += '<span style="color: orange">数据同步到本地需要3分钟</span><br>';
	content += '<span style="color: blue">左右请耐心等待！】</span></div>';
	$
			.dialog({
				title : '确认拆分？',
				content : content,
				max : false,
				min : false,
				lock : true,
				drag : false,
				fixed : true,
				ok : function() {
					var state = $("#split_div input[type=radio]:checked ")
							.val();
					if (state == 1) {
						time_ = $("#ch_date1").val();
					} else {
						time_ = $("#ch_date1").val();
					}
                    $.ajax({
                        type : 'POST',
                        url : '/cbtconsole/orderSplit/doSplit.do',
                        data : {
                            "orderno" : orderno,
                            "odids" : odids,
                            "state" : state
                        },
                        success : function(data) {
                            if (data.ok) {
                            	alert("拆单成功，请等待数据同步");
                                window.location.reload();
                            } else {
                                alert(data.message);
                                $("#split_order_btn").show();
                            }
                        },
                        error : function(res) {
                            alert("执行错误,请联系管理员");
                            $("#split_order_btn").show();
                        }
                    });

				},
				cancel : function() {
				}
			});
}
function deliver(orderno, usid, paytime,paymoney) {

    if(orderno == null || orderno == ""){
        alert('获取订单号失败');
        return ;
    }
    if (paymoney<50){
        alert('订单金额不足无法送样');
        return;
    }
    url="/cbtconsole/orderSplit/deliverOrder?orderno="+orderno+"&&userid="+usid;
    var param = "height=860,width=1500,top=80,left=200,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
     window.open(url, "windows", param);
    // window.location.href="/cbtconsole/orderSplit/deliverOrder?orderno="+orderno+"&&userid="+usid;
}
function fnRodio(state) {
	if (state == 0) {
		$("#div_split_time").hide();
	} else {
		$("#div_split_time").show();
	}
}

// 价格
function pricechange(orderNo, goodId, oldPrice, index,isDropshipOrder1) {
	window
			.open(
					"/cbtconsole/website/orderchange_price.jsp?orderNo="
							+ orderNo + "&goodId=" + goodId + "&oldPrice="
							+ oldPrice + "&index=" + index+"&isDropshipOrder1="+isDropshipOrder1 + "&changeType=33",
					"windows",
					"height=200,width=400,top=500,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
}
// 交期
var deliverychange = function(orderNo, goodid, oldDeliver, index,isDropshipOrder1) {
	window
			.open(
					"/cbtconsole/website/orderchange_delivery.jsp?orderNo="
							+ orderNo + "&goodId=" + goodid + "&oldDeliver="
							+ oldDeliver + "&index=" + index+"&isDropshipOrder1="+isDropshipOrder1 + "&changeType=44",
					"windows",
					"height=200,width=400,top=500,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
};
// 定量偏低
function rationchange(orderNo, goodid, oldRation, index,isDropshipOrder1) {
	window
			.open(
					"/cbtconsole/website/orderchange_ration.jsp?orderNo="
							+ orderNo + "&goodId=" + goodid + "&oldRation="
							+ oldRation + "&index=" + index+"&isDropshipOrder1="+isDropshipOrder1 + "&changeType=55",
					"windows",
					"height=200,width=400,top=500,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
};
// 直接取消
var cancelchange = function(orderNo, goodid, index) {
	window
			.open(
					"/cbtconsole/website/orderchange_cancel.jsp?orderNo="
							+ orderNo + "&goodId=" + goodid + "&index=" + index,
					"windows",
					"height=200,width=400,top=500,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
};
// 需要沟通
var communicatechange = function(orderNo, goodid, isDropship) {
	// alert(isDropship);
	// 获取聊天历史
	window
			.open(
					"/cbtconsole/WebsiteServlet?action=getOrderCommunication&className=OrderwsServlet&orderNo="
							+ orderNo
							+ "&goodId="
							+ goodid
							+ "&isDropship="
							+ isDropship + "&changeType=5",
					"windows",
					"height=200,width=400,top=500,left=500,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
};

// 打开订单备注内容
function openremark() {
	$("#openremark").val("隐藏内部沟通");
	$("#remarkdiv").css("display", "");
	$("#openremark").attr("onclick", "closeremark()");
}

// 关闭订单备注内容
function closeremark() {
	$("#openremark").val("查看内部沟通");
	$("#remarkdiv").css("display", "none");
	$("#openremark").attr("onclick", "openremark()");
}

// 显示备注
function showBz(orderNo) {
	$
			.post(
					"/cbtconsole/OrderRemarkServlet",
					{
						action : 'get',
						orderid : orderNo
					},
					function(res) {
                        $("#remarkdiv").html("");
						var json = eval(res);
						var str1 = '<table style="border-collapse:separate; border-spacing:5px;"><tr><td><input id="remarkbtn" type="button" value="添加备注内容(对内)" onclick="addremark(\''
								+ orderNo
								+ '\')"></td><td rowspan="2">备注内容:</td><td rowspan="2"><textarea id="orderremark_" style="width:400px;height:50px;"></textarea></td><td id="success" style="color:red"></td><td></td></tr>'
								+ '<tr><td></td><td></td><td></td></tr></table>';
						$("#remarkdiv").html(str1);
						$("#openremark").show();

						$("#idshouBz").remove();
						var remarkuser = new Array();
						var str = '<div  class="ormamark">';
						for (var i = 0; i < json.length; i++) {
							str += '<div  align="left" style="word-wrap: break-word;word-break: normal; ">备注人员：'
									+ json[i][2]
									+ ',时间：'
									+ json[i][3]
									+ ',内容：'
									+ json[i][1] + '</div>';
						}
						$("#remarkdiv").html(str + str1 + "</div>");
					});
}

function addremark(orderNo) {
	var orderremark = $("#orderremark_").val();
	if($.trim(orderremark) == ''){
		alert('请输入备注信息');
	}else{
		$.post("/cbtconsole/OrderRemarkServlet", {
			action : 'add',
			orderid : orderNo,
			orderremark : orderremark
		}, function(res) {
			if (res == 1) {
				$("#success").html("Success!!!");
				window.location.href = location;
			} else {
				alert('Add Failed!!!');
			}
		});
	}
}

// 取消订单
function fnCloseOrder(orderno, userId, actualPay, currency, order_ac, email,
		confirmEmail, totalPrice, freight, weight, isDropshipOrder) {

	if(orderno == null || orderno == ""){
        alert('获取订单号失败');
        return ;
	}
	//var isCf = confirm("是否确定取消订单?");
	var text = "";
    $.dialog({
        title : '是否确定取消订单?',
        content : text,
        max : false,
        min : false,
        lock : true,
        drag : false,
        fixed : true,
        ok : function() {
                // 按钮不可用
                $("#closeOrder").attr("disabled", true);
                $("#closeOrder").hide();
                $(".mask").show().text("正在执行，请等待...");
                var websiteType = $('#website_type').val();
                // ==2 是补货订单能进行退款操作
                if (isDropshipOrder == 2) {

                    var params = {
                        "orderNo" : orderno,
                        "userId" : userId,
                        "actualPay" : actualPay,
                        "currency" : currency,
                        "order_ac" : order_ac,
                        "email" : email,
                        "confirmEmail" : confirmEmail,
                        "totalPrice" : totalPrice,
                        "weight" : weight,
                        "isDropshipOrder" : isDropshipOrder,
						"websiteType" : websiteType
                    };
                    $.ajax({
                        url : '/cbtconsole/orderDetails/closeOrder.do',
                        type : "post",
                        data : params,
                        dataType : "json",
                        success : function(data) {
                            if (data.ok) {
                                showMessage('取消成功,请等待订单状态更新');
                                setTimeout(function() {
                                    window.location.reload();
                                }, 1500);
                            } else {
                                $(".mask").hide();
                                showMessage(data.message);
                                $('#closeOrder').removeAttr("disabled");
                                $("#closeOrder").show();
                            }
                        },
                        error : function() {
                            showMessage('取消失败，请联系开发人员');
                            $('#closeOrder').removeAttr("disabled");
                            $("#closeOrder").show();
                        }
                    });

                } else {
                    //Added <V1.0.1> Start： cjc 2018/10/23 16:21 TODO 判断是否是droship子订单，如果是子订单则要去 查询子订单的状态 0:默认不是  1：是
                    var  isDropshipOrder1 = 0;
                    var temParm = $('#isDropshipOrder1').val();
                    if(typeof (temParm) != 'undefined' && temParm != ''){
                        isDropshipOrder1 = $('#isDropshipOrder1').val();
                    }
                    //End：

                    // 如果订单总金额<= 0,不能进行退款操作
                    if (actualPay <= 0) {
                        $('#closeOrder').removeAttr("disabled");
                        $("#closeOrder").show();
                        showMessage('余额小于等于0，不能取消');
                    } else {
                        var params = {
                            "orderNo" : orderno,
                            "userId" : userId,
                            "actualPay" : actualPay,
                            "currency" : currency,
                            "order_ac" : order_ac,
                            "email" : email,
                            "confirmEmail" : confirmEmail,
                            "totalPrice" : totalPrice,
                            "weight" : weight,
                            "freight" : freight,
                            "isDropshipOrder" : isDropshipOrder,
                            'isDropshipOrder1':isDropshipOrder1,
							"websiteType" : websiteType
                        };

                        $.ajax({
                            url : '/cbtconsole/orderDetails/closeOrder.do',
                            type : "post",
                            data : params,
                            dataType : "json",
                            success : function(data) {
                                if (data.ok) {
                                    showMessage('取消成功,请等待订单状态更新');
                                    setTimeout(function() {
                                        window.location.reload();
                                    }, 1500);
                                } else {
                                    $(".mask").hide();
                                    showMessage(data.message,3000);
                                    $('#closeOrder').removeAttr("disabled");
                                    $("#closeOrder").show();
                                }
                            },
                            error : function() {
                                showMessage('取消失败，请联系开发人员');
                                $('#closeOrder').removeAttr("disabled");
                                $("#closeOrder").show();
                            }
                        });
                    }
                }
        },
        cancel : function() {
        }
    });
}

function showMessage(msg) {
    $('.mask').show().text(msg);
    setTimeout(function() {
        $('.mask').hide();
    }, 1500);
}

function queryRepeat(uid){
    $.ajax({
        url:"/cbtconsole/orderDetails/queryRepeatUserid.do",
        type:"post",
        dataType:"json",
        data : {"userid":uid},
        success:function(data){
            if(data.ok){
                var json = data.data;
                $("#other_id").css("display","inline");
                var content = "相似用户的id： ";
                for(var i=0;i<json.length;i++){
                    content +=json[i]+"&nbsp;";
                }
                $("#other_id").text("相似用户的id： ");
                if(json == null || json == ""){
                    $("#other_id").css("display","none");
                }
            }
        }
    });
}

function changeOrderBuyer(orderid,admuserid){
    $.ajax({
        url:"/cbtconsole/orderDetails/changeOrderBuyer.do",
        type:"post",
        dataType:"json",
        data : {"orderid":orderid,"admuserid":admuserid},
        success:function(data){
            if(data.ok){
                $("#buyuserinfo").text("执行成功");
            }else{
                $("#buyuserinfo").text("执行失败");
            }
            window.location.reload();
        },
        error : function(res){
            $("#buyuserinfo").text("执行失败,请联系管理员");
        }

    });
}

//获取采购员
function getBuyer(oids){
    var adminName = '<%=user.getAdmName()%>';
    $.ajax({
        url:"/cbtconsole/orderDetails/qyeruBuyerByOrderNo.do",
        type:"post",
        dataType:"json",
        data : {"str_oid" : oids},
        success:function(data){
            if(data.ok>0){
                var json = data.data;
                for(var i=0;i< json.length;i++){
//  					 $("#odid"+json[i].odid).append(json[i].admName);
                    for(var j=0;j<document.getElementById("buyer"+json[i].odid).options.length;j++){
                        if (document.getElementById("buyer"+json[i].odid).options[j].text == json[i].admName){
                            document.getElementById("buyer"+json[i].odid).options[j].selected=true;
                            break;
                        }
                    }
                    if(admid!=1 || adminName !="Ling" || adminName !="emmaxie"){
                        $("#buyer"+json[i].odid).attr("disabled",true);
                    }
                }
            }
        }
    });
}

//计算利润
function jslr(orderno){

    var i = 0;
    var j =0;
    var hsSum = 0;
    var heSum = 0;
    $("div[id^='"+orderno+"']").each(function(){

        var sBut = $(this).children("div:first").children().eq(1).val();
        alert(sBut);
        var divId = $(this).attr('id');

        //获得原价   和货源价
        var hs = $("#"+divId+"_s").val();
        var _sQuantity = $("#"+divId+"_sQuantity").val();
        var he = $("#"+divId+"_e").val();
        var _eQuantity = $("#"+divId+"_eQuantity").val();
        ///${pb.orderNo}${pbsi.index}_eQuantity
        alert(sBut+"------"+hs+"----"+he);
        //是否确认货源
        //	if(sBut =="取消货源"){
        //有一个为空的价格就不计算
        if(hs!='' & he!=''){
            hsSum += Number(hs)*Number(_sQuantity);

            heSum += Number(he)*Number(_eQuantity);
            i++;
        }
        //	}


        j++;


        alert($(this).attr('id')+"------"+sBut);
    });

    if(heSum == 0){
        $("#"+orderno+"_span").html(j+"/"+i+"   利润：0%");
        $("#"+orderno+"_span_s").html(0);
        $("#"+orderno+"_span_e").html(0);
    }else{
        $("#"+orderno+"_span_s").html((hsSum).toFixed(2) +"USD ("+(hsSum*6.78).toFixed(2)+")");
        $("#"+orderno+"_span_e").html(heSum.toFixed(2));
        var t = Number(hsSum)*6.78 - Number(heSum);
        t = t*100/(Number(hsSum)*6.78);
        t = parseInt(t);//.toFixed(2);
        $("#"+orderno+"_span").html(j+"/"+i+"   利润："+t+"%");

        //alert(j+"/"+i+"   利润："+t+"%");
    }
}

//显示产品历史的价格
function showHistoryPrice(url){
    $.ajax({
        url: "/cbtconsole/orderDetails/showHistoryPrice.do",
        type:"POST",
        dataType:"json",
        data : {"url":url},
        success:function(data){
            if(data.ok){
                var json = data.data;
                var pri = "";
                pri += "<div class='pridivbg'><a class='pridclose' onclick='priclose()'>X</a>"
                for(var i =0;i<json.length;i++){
                    pri += "<p>"+json[i][1]+" &nbsp;&nbsp; "+json[i][0]+"</p>";
                }
                pri +="</div>";
                var topHg = ($(window).height()-$("#prinum").height())/2 + $(document).scrollTop();
                var lefhWt = ($(window).width()-$("#prinum").width())/2;
                $("#prinum").show().append(pri).css({"top":topHg,"left":lefhWt});
                $(".peimask").show().css("height",$(document).height());

            }else{
                data(info.message);
            }
        },
        error: function(res) {
            alert('请求失败,请重试');
        }
    });
}

function priclose(){
    $(".pridivbg").remove();
    $("#prinum").hide();
    $(".peimask").hide();
}

function afterReplenishment(){
    var str=document.getElementsByName("replenishment");
    var orderid=$("#orderNo").val();
    var objarray=str.length;
    var parm="";
    for (i=0;i<objarray;i++){
        if(str[i].checked == true){
            var count=$("#count_"+str[i].value).val();
            if(count=="补货数量"){
                alert("请输入补货数量");
                return;
            }
            parm+=str[i].value+":"+count+":"+orderid+",";
        }
    }
    $.ajax({
        url: "/cbtconsole/orderDetails/afterReplenishment.do",
        type:"POST",
        dataType:"json",
        data : {"parm":parm},
        success:function(data){
            alert(data.message);
        },
        error : function(res){
            alert("执行失败，请联系管理员");
        }
    });
}

//保存或者修改评论yyl
function saveCommentContent(){
    var cmid = $("#cm_id").val();
    var adminname = $("#cm_adminname").val();
    var orderNo = $("#cm_orderNo").val();
    var goods_pid = $("#cm_goodsPid").val();
    var goodsSource = $("#cm_goodsSource").val();
    var adminId = $("#cm_adminId").val();
    var countryId = $("#cm_country").val();
    var oid = $("#cm_oid").val();
    var carType = $("#cm_carType").val();
    var commentcontent = $("#comment_content_").val();
    $.ajax({
        type : 'POST',
        async : false,
        url : '/cbtconsole/goodsComment/savecomment.do',
        data : {
            'id':cmid,
            'userName' : adminname,
            'orderNo' : orderNo,
            'goodsPid' : goods_pid,
            'goodsSource' : goodsSource,
            'adminId' : adminId,
            'countryId' : countryId,
            'oid' : oid,
            'car_type' : carType,
            "commentsContent":commentcontent,
        },
        dataType : 'json',
        success : function(data){
            if(data.success == true){
                $('#commentDiv1').hide();
                //将改页所有pid等于改pid的产品销售评论改变commentcontent
                var button=document.getElementsByName(goods_pid+"ID");
                for(var j=0;j<button.length;j++){
                    button[j].innerHTML="已评论 &nbsp;&nbsp;<button cmid='"+data.cmid+"' name='but"+goods_pid+"' style='cursor:pointer' title=\""+commentcontent+"\">显示评论</button>"
                }
            }else{
                alert("操作失败!")
            }
        }
    });
}
//弹出评论框yyl
function showcomm(id,car_type,adminname,orderNo,goods_pid,countryid,admindid){
    var controls=document.getElementsByName("but"+goods_pid);
    $("#cm_id").val($(controls[0]).attr("cmid"));
    $("#cm_adminname").val(adminname);
    $("#cm_orderNo").val(orderNo);
    $("#cm_goodsPid").val(goods_pid);
    $("#cm_country").val(countryid);
    $("#cm_adminId").val(admindid);
    $("#cm_oid").val(id);
    $("#cm_carType").val(car_type);
    $("#comment_content_").val($(controls[0]).attr("title"));
    var rfddd1 = document.getElementById("commentDiv1");
    rfddd1.style.display = "block";;

}

//备注回复
function doReplay1(orderid,odid){
    $("#remark_content_").val("");
    $("#rk_orderNo").val(orderid);
    $("#rk_odid").val(odid);
    var rfddd = document.getElementById("repalyDiv1");
    rfddd.style.display = "block";
}

function showMessage(msg) {
    $('.mask').show().text(msg);
    setTimeout(function() {
        $('.mask').hide();
    }, 1500);
}

function queryRepeat(uid){
    $.ajax({
        url:"/cbtconsole/orderDetails/queryRepeatUserid.do",
        type:"post",
        dataType:"json",
        data : {"userid":uid},
        success:function(data){
            if(data.ok){
                var json = data.data;
                $("#other_id").css("display","inline");
                var content = "相似用户的id： ";
                for(var i=0;i<json.length;i++){
                    content +=json[i]+"&nbsp;";
                }
                $("#other_id").text("相似用户的id： ");
                if(json == null || json == ""){
                    $("#other_id").css("display","none");
                }
            }
        }
    });
}

function changeOrderBuyer(orderid,admuserid){
    $.ajax({
        url:"/cbtconsole/orderDetails/changeOrderBuyer.do",
        type:"post",
        dataType:"json",
        data : {"orderid":orderid,"admuserid":admuserid},
        success:function(data){
            if(data.ok){
                $("#buyuserinfo").text("执行成功");
            }else{
                $("#buyuserinfo").text("执行失败");
            }
            window.location.reload();
        },
        error : function(res){
            $("#buyuserinfo").text("执行失败,请联系管理员");
        }

    });
}

//获取采购员
function getBuyer(oids){
    var adminName = '<%=user.getAdmName()%>';
    $.ajax({
        url:"/cbtconsole/orderDetails/qyeruBuyerByOrderNo.do",
        type:"post",
        dataType:"json",
        data : {"str_oid" : oids},
        success:function(data){
            if(data.ok>0){
                var json = data.data;
                for(var i=0;i< json.length;i++){
//  					 $("#odid"+json[i].odid).append(json[i].admName);
                    for(var j=0;j<document.getElementById("buyer"+json[i].odid).options.length;j++){
                        if (document.getElementById("buyer"+json[i].odid).options[j].text == json[i].admName){
                            document.getElementById("buyer"+json[i].odid).options[j].selected=true;
                            break;
                        } if (document.getElementById("Abuyer").options[j].text == json[i].admName){
                            document.getElementById("Abuyer").options[j].selected=true;
                            break;
                        }
                    }
                    if(admid!=1 || adminName !="Ling" || adminName !="emmaxie"){
                        $("#buyer"+json[i].odid).attr("disabled",true);
                        $("#Abuyer").attr("disabled",true);
                    }
                }
            }
        }
    });
}

//计算利润
function jslr(orderno){

    var i = 0;
    var j =0;
    var hsSum = 0;
    var heSum = 0;
    $("div[id^='"+orderno+"']").each(function(){

        var sBut = $(this).children("div:first").children().eq(1).val();
        alert(sBut);
        var divId = $(this).attr('id');

        //获得原价   和货源价
        var hs = $("#"+divId+"_s").val();
        var _sQuantity = $("#"+divId+"_sQuantity").val();
        var he = $("#"+divId+"_e").val();
        var _eQuantity = $("#"+divId+"_eQuantity").val();
        ///${pb.orderNo}${pbsi.index}_eQuantity
        alert(sBut+"------"+hs+"----"+he);
        //是否确认货源
        //	if(sBut =="取消货源"){
        //有一个为空的价格就不计算
        if(hs!='' & he!=''){
            hsSum += Number(hs)*Number(_sQuantity);

            heSum += Number(he)*Number(_eQuantity);
            i++;
        }
        //	}


        j++;


        alert($(this).attr('id')+"------"+sBut);
    });

    if(heSum == 0){
        $("#"+orderno+"_span").html(j+"/"+i+"   利润：0%");
        $("#"+orderno+"_span_s").html(0);
        $("#"+orderno+"_span_e").html(0);
    }else{
        $("#"+orderno+"_span_s").html((hsSum).toFixed(2) +"USD ("+(hsSum*6.78).toFixed(2)+")");
        $("#"+orderno+"_span_e").html(heSum.toFixed(2));
        var t = Number(hsSum)*6.78 - Number(heSum);
        t = t*100/(Number(hsSum)*6.78);
        t = parseInt(t);//.toFixed(2);
        $("#"+orderno+"_span").html(j+"/"+i+"   利润："+t+"%");

        //alert(j+"/"+i+"   利润："+t+"%");
    }
}

//显示产品历史的价格
function showHistoryPrice(url){
    $.ajax({
        url: "/cbtconsole/orderDetails/showHistoryPrice.do",
        type:"POST",
        dataType:"json",
        data : {"url":url},
        success:function(data){
            if(data.ok){
                var json = data.data;
                var pri = "";
                pri += "<div class='pridivbg'><a class='pridclose' onclick='priclose()'>X</a>"
                for(var i =0;i<json.length;i++){
                    pri += "<p>"+json[i][1]+" &nbsp;&nbsp; "+json[i][0]+"</p>";
                }
                pri +="</div>";
                var topHg = ($(window).height()-$("#prinum").height())/2 + $(document).scrollTop();
                var lefhWt = ($(window).width()-$("#prinum").width())/2;
                $("#prinum").show().append(pri).css({"top":topHg,"left":lefhWt});
                $(".peimask").show().css("height",$(document).height());

            }else{
                data(info.message);
            }
        },
        error: function(res) {
            alert('请求失败,请重试');
        }
    });
}

function priclose(){
    $(".pridivbg").remove();
    $("#prinum").hide();
    $(".peimask").hide();
}

function afterReplenishment(){
    var str=document.getElementsByName("replenishment");
    var orderid=$("#orderNo").val();
    var objarray=str.length;
    var parm="";
    for (i=0;i<objarray;i++){
        if(str[i].checked == true){
            var count=$("#count_"+str[i].value).val();
            if(count=="补货数量"){
                alert("请输入补货数量");
                return;
            }
            parm+=str[i].value+":"+count+":"+orderid+",";
        }
    }
    $.ajax({
        url: "/cbtconsole/orderDetails/afterReplenishment.do",
        type:"POST",
        dataType:"json",
        data : {"parm":parm},
        success:function(data){
            alert(data.message);
        },
        error : function(res){
            alert("执行失败，请联系管理员");
        }
    });
}

//保存或者修改评论yyl
function saveCommentContent(){
    var cmid = $("#cm_id").val();
    var adminname = $("#cm_adminname").val();
    var orderNo = $("#cm_orderNo").val();
    var goods_pid = $("#cm_goodsPid").val();
    var goodsSource = $("#cm_goodsSource").val();
    var adminId = $("#cm_adminId").val();
    var countryId = $("#cm_country").val();
    var oid = $("#cm_oid").val();
    var carType = $("#cm_carType").val();
    var commentcontent = $("#comment_content_").val();
    $.ajax({
        type : 'POST',
        async : false,
        url : '/cbtconsole/goodsComment/savecomment.do',
        data : {
            'id':cmid,
            'userName' : adminname,
            'orderNo' : orderNo,
            'goodsPid' : goods_pid,
            'goodsSource' : goodsSource,
            'adminId' : adminId,
            'countryId' : countryId,
            'oid' : oid,
            'car_type' : carType,
            "commentsContent":commentcontent,
        },
        dataType : 'json',
        success : function(data){
            if(data.success == true){
                $('#commentDiv1').hide();
                //将改页所有pid等于改pid的产品销售评论改变commentcontent
                var button=document.getElementsByName(goods_pid+"ID");
                for(var j=0;j<button.length;j++){
                    button[j].innerHTML="已评论 &nbsp;&nbsp;<button cmid='"+data.cmid+"' name='but"+goods_pid+"' style='cursor:pointer' title=\""+commentcontent+"\">显示评论</button>"
                }
            }else{
                alert("操作失败!")
            }
        }
    });
}
//弹出评论框yyl
function showcomm(id,car_type,adminname,orderNo,goods_pid,countryid,admindid){
    var controls=document.getElementsByName("but"+goods_pid);
    $("#cm_id").val($(controls[0]).attr("cmid"));
    $("#cm_adminname").val(adminname);
    $("#cm_orderNo").val(orderNo);
    $("#cm_goodsPid").val(goods_pid);
    $("#cm_country").val(countryid);
    $("#cm_adminId").val(admindid);
    $("#cm_oid").val(id);
    $("#cm_carType").val(car_type);
    $("#comment_content_").val($(controls[0]).attr("title"));
    var rfddd1 = document.getElementById("commentDiv1");
    rfddd1.style.display = "block";;

}

//手动调整采购人员
function changeBuyer(odid,buyid){
    $.ajax({
        url:"/cbtconsole/orderDetails/changeBuyer.do",
        type:"post",
        dataType:"json",
        data : {"odid":odid,"admuserid":buyid},
        success:function(data){
            if(data.ok){
                $("#info"+odid).text("执行成功");
            }else{
                $("#info"+odid).text("执行失败");
            }
            window.location.reload();
        },
        error : function(res){
            $("#info"+odid).text("执行失败,请联系管理员");
        }
    });
}
//手动调整整单采购人员
function changeAllBuyer(orderNo,buyid){
    $.ajax({
        url:"/cbtconsole/orderDetails/changeAllBuyer",
        type:"post",
        dataType:"json",
        data : {"orderNo":orderNo,"admuserid":buyid},
        success:function(data){
            if(data.ok){
                $("#orderbuyer").text("执行成功");
            }else{
                $("#orderbuyer").text("执行失败");
            }
            window.location.reload();
        },
        error : function(res){
            $("#orderbuyer").text("执行失败,请联系管理员");
        }
    });
}

//备注回复
function doReplay1(orderid,odid){
    $("#remark_content_").val("");
    $("#rk_orderNo").val(orderid);
    $("#rk_odid").val(odid);
    var rfddd = document.getElementById("repalyDiv1");
    rfddd.style.display = "block";
}

// 确认弹出框关闭方法
function fnPaymentConfirmClose(confirmtime, confirmname) {
	location.reload();
}

function fnChangeProduct(orderNo) {
	window.location.href = '/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&selled=2&orderNo='
			+ orderNo;
}

function sendCutomers(orderNo, whichOne, isDropship) {
	// $("#notifycustomer").attr("disabled", "disabled");
	// $("#msg").css("display", "none");
	//  var Website=$("#Web_site").val();
	$.ajax({
		url : '/cbtconsole/order/sendCutomers',
		type : "post",
		data : {"orderNo" : orderNo, "whichOne" : whichOne, "isDropship" : isDropship},
		success : function(data) {
			console.log(data);
			// $("#notifycustomer").removeAttr("disabled");
			if (data.result > 0) {
				$("#msg").css("display", "inline");
			} else {
				alert("发送无效或者失败,请通知开发人员");
			}
		}
	});
}

function fnRend(url) {
	//window.open(decodeURIComponent(url));
	window.open(url);
};

function addDate(n, paytime) {
	var s, d, t, t2;
	var paytime = paytime.split(" ")[0];
	t = new Date(paytime).getTime();
	t2 = n * 1000 * 3600 * 24;
	t += t2;
	d = new Date(t);
	s = d.getUTCFullYear() + "-";
	s += ("00" + (d.getUTCMonth() + 1)).slice(-2) + "-";
	s += ("00" + d.getUTCDate()).slice(-2);
	return s;
}

var countryid=0;
// 打开订单详情地址
function openorderaddress() {
//	$("#open").val("隐藏订单详情地址");
//	$("#orderaddressdiv").css("display", "");
//	$("#open").attr("onclick", "closeorderaddress()");
// 	$.post("/cbtconsole/feeServlet?action=getAllZone&className=ZoneServlet",
// 			function(data) {
// 				var json = eval(data);
// 				if (countryid == 36) {
// 					countryid = 43;
// 				}
// 				for (var i = 0; i < json.length; i++) {
// 					if (countryid.replace(/\s+/g,"")!="0" && (json[i].id.replace(/\s+/g,"") == countryid.replace(/\s+/g,"") || json[i].country.replace(/\s+/g,"") == countryid.replace(/\s+/g,""))) {
// 						$('#ordercountry').append(
// 								'<option value="' + json[i].country.replace(/\s+/g,"")
// 										+ '" selected="selected">'
// 										+ json[i].country.replace(/\s+/g,"") + '</option>');
// 					} else {
// 						$('#ordercountry').append(
// 								'<option value="' + json[i].country.replace(/\s+/g,"") + '">'
// 										+ json[i].country.replace(/\s+/g,"") + '</option>');
// 					}
// 				}
// 				$("#ordercountry").val($("#ordercountry_value").val());
// 			});
   // $("#ordercountry").val($("#ordercountry_value").val());
}
// 直接抵扣赠送运费操作
function refreight() {
	var unpaid_freight = $("#unpaid_freight").html();
	var refreight = $("#refreight").html();
	var re = 0;
	var f_unpaid_freight = parseFloat(unpaid_freight);
	var f_refreight = parseFloat(refreight);
	if (f_refreight > f_unpaid_freight) {
		re = f_unpaid_freight;
	} else {
		re = f_refreight;
	}
	if (re < 0.0001) {
		return;
	}
	$("#refreightResult").html("");
	var orderNo = $("#orderNo").val();
	var userId = $("#userId").html();
	var href = window.location;
	$
			.post(
					"/cbtconsole/WebsiteServlet?action=freightDeduction&className=OrderwsServlet&refreight="
							+ re + "&orderNo=" + orderNo + "&userId=" + userId,
					function(data) {
						var i_data = parseInt(data);
						if (i_data > 0) {
							window.location.href = href;
						} else if (i_data === -1) {
							$("#refreightResult").html("订单更改失败");
						} else if (i_data === -1) {
							$("#refreightResult").html("用户赠送运费更改失败");
						}

					});

}

// 问题解决了
function fnResolve(orderNo, goodId) {

	var params = {
		"orderNo" : orderNo,
		"action" : "upOrderChangeResolve",
		"className" : "OrderwsServlet",
		"goodId" : goodId,
		"changeType" : 5
	};
	$
			.ajax({
				url : '/cbtconsole/WebsiteServlet',
				type : "post",
				data : params,
				dataType : "json",
				success : function(data) {
					// alert(data.result+"==="+data);
					if (data > 0) {
						$("#msg2").css("display", "inline");
						var obj = setTimeout(null, 3000);
						clearTimeout(obj);
						//parent.location.href = "WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo="
								//+ orderNo + "&state=5&rand=" + Math.random();
						parent.location.href = "/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="
								+orderNo+"&state=5&rand="+Math.random();
					} else {
						alert("保存失败");
					}
				},
				error : function() {
					alert("保存失败");
				}
			});
}

// 关闭订单详情地址
function closeorderaddress() {
//	$("#open").val("查看订单详情地址");
//	$("#orderaddressdiv").css("display", "none");
//	$("#open").attr("onclick", "openorderaddress()");
}
function OrderAddress() {
	document.getElementById("orderrecipients").disabled = false;
	document.getElementById("orderstreet").disabled = false;
	document.getElementById("orderstreet2").disabled = false;
	document.getElementById("ordercity").disabled = false;
	document.getElementById("orderstate").disabled = false;
	document.getElementById("ordercountry").disabled = false;
	document.getElementById("orderzipcode").disabled = false;
	document.getElementById("orderphone").disabled = false;
	document.getElementById("OrderAddress").value = "更新订单地址";
	$("#OrderAddress").attr("onclick", "updateOrderAddress()");
}
function updateOrderAddress() {
	var isDropFlag = document.getElementById("is_drop_flag").value;
	if(!isDropFlag || isDropFlag == null || isDropFlag == ""){
		isDropFlag = 0;
	}
	var address = document.getElementById("orderstreet").value;
	var address2 = document.getElementById("ordercity").value;
	var statename = document.getElementById("orderstate").value;
	var countryid = document.getElementById("ordercountry").value;
	var zipcode = document.getElementById("orderzipcode").value;
	var phonenumber = document.getElementById("orderphone").value;
	var recipients = document.getElementById("orderrecipients").value;
	var street = document.getElementById("orderstreet2").value;
	var orderNo = document.getElementById("orderNo").value;

	if(countryid == 0 || countryid == "" || countryid=="0"){
		alert('请选择国家');
		return;
	}else{
		$.post("/cbtconsole/WebsiteServlet?action=updateOrderAddress&className=OrderwsServlet",
				{
					orderid : orderNo,
					address : address,
					address2 : address2,
					statename : statename,
					countryid : countryid,
					zipcode : zipcode,
					isDropFlag : isDropFlag,
					phonenumber : phonenumber,
					recipients : recipients,
					street : street
				}, function(data) {
					if (data == 0) {
						alert('更新失败');
					} else {
						alert('更新成功');
						window.location.href = location;
					}
				});
	}
}

var getAddress = 0;
function fnGetAddress() {
	if (getAddress == 1)
		return;
	var orderNo = document.getElementById("orderNo").value;
    $.ajax({
        type: "GET",
        url: "/cbtconsole/orderDetails/getIpnaddress?orderid="+orderNo,
        dataType:"json",
        success:function(data){
            console.log("res=="+data.orderid);
            if(data.stripe){
                console.log("data.stripe="+data.stripe);
                var stripeJson=JSON.parse(data.stripe)
                document.getElementById("address_name").value = stripeJson.data.object.source.name;
                document.getElementById("address_street").value = 'brand=' + stripeJson.data.object.source.brand;
                document.getElementById("address_city").value = 'funding=' + stripeJson.data.object.source.funding;
                document.getElementById("address_state").value = 'last4=' + stripeJson.data.object.source.last4;
                document.getElementById("address_country_code").value = stripeJson.data.object.source.country;
            }else {
                console.log("res==" + data.receiver_email);
                if (data.receiver_email == null || data.receiver_email == "" || data.receiver_email == "null") {
                    return;
                }
                $("#receiveremail").text(data.receiver_email);
                $("#receiveremail").attr("href", "mailto:" + data.receiver_email);
                document.getElementById("address_name").value = data.address_name;
                document.getElementById("address_country_code").value = data.address_country_code;
                // if (data.address_country != undefined) {
                //     document.getElementById("address_country").value = data.address_country;
                // }
                document.getElementById("address_city").value = data.address_city;
                document.getElementById("address_state").value = data.address_state;
                document.getElementById("address_street").value = data.address_street;

                console.log("data.stripe=" + data.stripe);
                if (data.stripe) {
                    document.getElementById("address_country_code").value = data.stripe.data.source.country;
                }
            }
        }
    });

	// $.post("/cbtconsole/WebsiteServlet?action=getOrderAddress&className=OrderwsServlet",
	// 				{
	// 					orderid : orderNo
	// 				},
	// 				function(data) {
	// 					if (data !== null && data !== 'null' && data !== undefined) {
	// 						var json = eval(data);
	// 						// console.log(data);
	// 						for (var i = 0; i < json.length; i++) {
	// 							var orderaddress = json[i];
	// 							if((orderaddress.address== null || orderaddress.address== "" || orderaddress.address== "null")
	// 								&& (orderaddress.zip_code== null || orderaddress.zip_code== "" || orderaddress.zip_code== "null")){
	// 								document.getElementById("div_address").style.display = "none";
	// 								return;
	// 							}
	// 							document.getElementById("orderstreet").value = orderaddress.address;
	// 							document.getElementById("ordercity").value = orderaddress.address2;
	// 							document.getElementById("orderstate").value = orderaddress.statename;
	// 							countryid = orderaddress.country;
	// 							document.getElementById("orderzipcode").value = orderaddress.zip_code;
	// 							document.getElementById("orderphone").value = orderaddress.phone_number;
	// 							if ((orderaddress.recipients + '') == 'null') {
	// 								document.getElementById("orderrecipients").value = '';
	// 							} else {
	// 								document.getElementById("orderrecipients").value = orderaddress.recipients;
	// 							}
	// 							document.getElementById("orderstreet2").value = orderaddress.street;
	// 						}
	// 					}
	// 				});
	getAddress = 1;
}

function getAllBuyuser() {
	var buysrt = '';
	var saler = '';
	$.post(
					"/cbtconsole/ConfirmUserServlet",
					{
						"action" : "sell"
					},
					function(res) {
						var json = eval(res);
						saler = saler + '<option value="0">  </option>';
						for (var i = 0; i < json.length; i++) {
								if (json[i].id == adminid) {
									$("#saler_em").html(json[i].confirmusername);
								}
								saler += '<option value="'
										+ json[i].id
										+ '"  '
										+ (json[i].id == adminid ? 'selected="selected"'
												: '') + '>'
										+ json[i].confirmusername + '</option>';
							}
						$('#saler').html(saler);
					});
}
function fninitbuy() {
	$("#td_buyuser").show();
	openorderaddress();
	if (document.getElementById("orderNo")) {
		var orderNo = document.getElementById("orderNo").value;
		$.post("${path}/WebsiteServlet", {
			action : 'getInit',
			className : 'OrderwsServlet',
			orderno : orderNo
		}, function(res) {
			$("#buyuser option[value='" + res + "']").attr('selected',
					'selected');
		});
		showBz(orderNo);
	}
	clearCheckBox();
}

//去除火狐浏览器拆单后复选框选中的问题
function clearCheckBox(){
	$("input[type='checkbox']").removeAttr("checked");
}

function fnchangebuy() {
	if (document.getElementById("orderNo")) {
		var orderNo = document.getElementById("orderNo").value;
		var options = document.getElementById("buyuser");
		var buyuser = options.options[options.options.selectedIndex].text;
		;
		var buyid = options.options[options.options.selectedIndex].value;
		$("#buy_but").attr("disabled", "disabled");
		$.post("/cbtconsole/WebsiteServlet", {
			action : 'getChangeBuy',
			className : 'OrderwsServlet',
			orderno : orderNo,
			buyid : buyid,
			buyuser : buyuser
		}, function(res) {
			var reslt = res.split('=');
			var result = '';
			if (reslt.length > 0) {
				if (reslt[0] == 'add') {
					if (reslt[1] == '0') {
						result = '分配失败';
					} else {
						result = '分配成功';
						$("#buyuser_em").html(buyuser);
						window.location.reload(true);
					}
				} else {
					if (reslt[1] == '0') {
						result = '更改失败';
					} else {
						result = '更改成功';
						$("#buyuser_em").html(buyuser);
						window.location.reload(true);
					}
				}
				$("#salerresult").html("");
			}
			document.getElementById("buyresult").innerHTML = result;
			$("#buy_but").removeAttr("disabled");
		});
	}
}
function fnShowForwarder() {
	$("#tab_forwarder").toggle();
}
// 改变销售负责人
function addUser(uid, userName, userEmail) {
	var id = $("#saler").find("option:selected").val();
	var stradmName_ = $("#saler").find("option:selected").text();
	$("#saler_but").attr("disabled", "disabled");
    $.ajax({
        url: "/cbtconsole/order/addUser",
        type:"POST",
        data : {"adminid":id,"userid":uid,"admName":stradmName_,"userName":userName,"email":userEmail},
        dataType:"json",
        success:function(data){
            if(data>0){
                document.getElementById("salerresult").innerHTML = "更新成功";
                $("#saler_em").html(stradmName_);
                window.location.reload(true);
            }else{
                document.getElementById("salerresult").innerHTML = "更新失败";
            }
            $("#buyuser_em").html("");
            $("#saler_but").removeAttr("disabled");
        }
    });
}

// 销售直接取消产品
var deleteOrderGoods = function(orderNo, goodId, purchase_state, userId) {
	var params = {
		"orderNo" : orderNo,
		"goodId" : goodId,
		"purchase_state" : purchase_state,
		"userId" : userId
	};
	$("#deleteGoods" + orderNo + goodId).hide();
	$.ajax({
		url : 'warehouse/deleteOrderGoods',
		type : "post",
		dataType : "text",
		// data:{orderNo:orderNo,goodId:goodId,purchase_state:purchase_state},
		data : params,
		success : function(data)

		{
			// alert(data.result+"==="+data);
			if (data > 0) {
				window.location.href = location;
				$("#t_delectGoodsorderNo" + orderNo + goodId).html("取消成功");
				// console.log("hihihjkjkjk:"+"#t_delectGoodsorderNo"+orderNo+goodId);
				window.setTimeout(function() {
					$("#t_delectGoodsorderNo").hide();
				}, 1500);
				alert("取消成功");
				// zlw add start
				// $("#purchaseId").attr("disabled","disabled");
				// zlw add end

			} else {
				$("#deleteGoods").show();
				$("#f_delectGoods" + orderNo + goodId).html("取消失败");
				// console.log("hihihjkjkjk:"+"#f_delectGoods"+orderNo+goodId);
				window.setTimeout(function() {
					$("#f_delectGoods").hide();
				}, 1500);
				// alert("");
			}
		},
		error : function() {
			alert("取消失败");
		}
	});
}


// 在本页面弹出采购供应商打分DIV
function openSupplierDiv(shop_id){
	var rfddd = document.getElementById("supplierDiv");
	rfddd.style.display = "block";
	document.getElementById('su_shop_id').innerHTML= shop_id;
}

//关闭采购供应商打分DIV
function FncloseSupplierDiv(){
	var rfddd = document.getElementById("supplierDiv");
	rfddd.style.display = "none";
	document.getElementById('su_shop_id').innerHTML="";
	 // $("#service option[value='0']").attr("selected","selected");
	 $("#quality option[value='0']").attr("selected","selected");
	 $("#su_data").val("");
	 $("input[name=protocol]").attr("checked",false);
}

// 提交采购供应商打分数据
function saveSupplier(){
	var shop_id=document.getElementById("su_shop_id").innerHTML;
	if(shop_id == null || shop_id == "" || shop_id == "0000"){
		alert("店铺ID不符合打分规则");
		return;
	}
	var service=$("#service").val();
	var quality=$("#quality").val();
	var su_data=$("#su_data").val();
	var  protocol=$('input[name="protocol"]:checked').val();
	$.ajax({
        type: "POST",//方法类型
        dataType:'json',
		url:'/cbtconsole/supplierscoring/saveproductscord',
		data:{quality:quality,rerundays:su_data,shopId:shop_id,inven:protocol},
		dataType:"json",
		success:function(data){
	    	if(data.flag == 'success'){
	    		alert("采购供应商打分成功");
	    		FncloseSupplierDiv();
	    	}else{
	    		alert("采购供应商打分失败");
	    	}
		}
	});
}

//在本页面弹出采样商品打分DIV
function openSupplierGoodsDiv(shop_id,goods_pid){
	var rfddd = document.getElementById("supplierGoodsDiv");
	rfddd.style.display = "block";
	document.getElementById('su_goods_id').innerHTML= goods_pid;
	document.getElementById('su_goods_p_id').innerHTML= shop_id;
	 // $("#g_service").val("0");
	 $("#g_quality").val("0");
	 $("#su_g_remark").val("");
}

//关闭采样商品打分DIV
function FncloseSupplierGoodsDiv(){
	var rfddd = document.getElementById("supplierGoodsDiv");
	rfddd.style.display = "none";
	document.getElementById('su_goods_id').innerHTML="";
	document.getElementById('su_goods_p_id').innerHTML="";
	 // $("#service").val("0");
	 $("#quality").val("0");
	 $("#su_g_remark").val("");
}

//提交采样商品打分数据
function saveGoodsSupplier(){
	var goods_pid=document.getElementById("su_goods_p_id").innerHTML;
	var shop_id=document.getElementById("su_goods_id").innerHTML;
	// var service=$("#g_service").val();
	var quality=$("#g_quality").val();
	var remark=$("#su_g_remark").val();
	$.ajax({
        type: "POST",//方法类型
        dataType:'json',
		url:'/cbtconsole/supplierscoring/saveproductscord',
		data:{quality:quality,remarks:remark,shopId:shop_id,goodsPid:goods_pid},
		dataType:"json",
		success:function(data){
	    	if(data.flag == 'success'){
	    		alert("采样商品打分成功");
	    		FncloseSupplierGoodsDiv();
	    	}else{
	    		alert("采样商品打分失败");
	    	}
		}
	});
}
//查看商品质检结果
function openCheckResult(orderid,goodsid){
    var params = {
        "orderid" : orderid,
        "goodsid" : goodsid
    };
    $.ajax({
        url : '/cbtconsole/orderDetails/openCheckResult',
        type : "post",
        data : params,
        dataType : "json",
        success : function(data) {
			console.log(data);
			var datas=data.data;
			for(var i=0;i<datas.length;i++){
				var catid=datas[i].catid;
				// console.log(datas[i].result.d_cp_remark);
                //运动服饰,男装,女装
                if(catid!= null && (catid=="311" || catid=="10166" || catid=="10165" || catid=="127380009")){
                    var rfddd = document.getElementById("div_clothing");
                    rfddd.style.display = "block";
                    writeClothingData(orderid,goodsid,datas[i].result);
                }else if(catid!= null && catid=="54"){
                    //首饰
                    var rfddd = document.getElementById("ss_div");
                    rfddd.style.display = "block";
                    writeJewelryData(orderid,goodsid,datas[i].result);
                }else if(catid!= null && (catid=="6" || catid=="7" || catid=="57" || catid=="58" || catid=="509" || catid=="10208")){
                    //电子
                    var rfddd = document.getElementById("dz_div");
                    rfddd.style.display = "block";
                    writeElectronicData(orderid,goodsid,datas[i].result);
                }
			}
        }
    });


    function writeClothingData(orderid,goodsid,map){
        checkRadio("y_wg",map.y_wg);
        checkRadio("y_bmxj",map.y_bmxj);
        checkRadio("y_wgxj",map.y_wgxj);
        checkRadio("y_ll",map.y_ll);
        checkRadio("y_xt",map.y_xt);
        checkRadio("y_zg",map.y_zg);
        checkRadio("y_qw",map.y_qw);
        checkRadio("y_bz",map.y_bz);
        $("#jks_value").val(map.jks_value);
        $("#jk_remark").val(map.jk_remark);
        $("#xwb_value").val(map.xwb_value);
        $("#xw_remark").val(map.xw_remark);
        $("#yww_value").val(map.yww_value);
        $("#yw_remark").val(map.yw_remark);
        $("#twh_value").val(map.twh_value);
        $("#tw_remark").val(map.tw_remark);
        $("#tw_remark").val(map.tw_remark);
        $("#xc_remark").val(map.xc_remark);
        $("#yzl_value").val(map.yzl_value);
        $("#yc_remark").val(map.yc_remark);
        $("#xcs_value").val(map.xcs_value);
        document.getElementById('clothing_orderid').innerHTML= orderid;
        document.getElementById('clothing_goodsid').innerHTML= goodsid;
	}


    function writeJewelryData(orderid,goodsid,map){
        document.getElementById('ss_orderid').innerHTML= orderid;
        document.getElementById('ss_goodsid').innerHTML= goodsid;
        checkRadio("s_wz",map.s_wz);
        checkRadio("s_ks",map.s_ks);
        checkRadio("s_ys",map.s_ys);
        checkRadio("s_bm",map.s_bm);
        checkRadio("s_wgxj",map.s_wgxj);
        checkRadio("s_lk",map.s_lk);
        checkRadio("s_ds",map.s_ds);
        checkRadio("s_cz",map.s_cz);
        checkRadio("s_bz",map.s_bz);

	}

    function writeElectronicData(orderid,goodsid,map){
        document.getElementById('dd_orderid').innerHTML= orderid;
        document.getElementById('dd_goodsid').innerHTML= goodsid;
        checkRadio("d_wg",map.d_wg);
        checkRadio("d_ks",map.d_ks);
        checkRadio("d_ys",map.d_ys);
        checkRadio("d_wz",map.d_wz);
        checkRadio("d_wg1",map.d_wg1);
        checkRadio("d_wg2",map.d_wg2);
        checkRadio("d_ds",map.d_ds);
        checkRadio("d_nc",map.d_nc);
        checkRadio("d_rl",map.d_rl);
        checkRadio("d_pm",map.d_pm);
        checkRadio("d_fbv",map.d_fbv);
        checkRadio("d_xs",map.d_xs);
        checkRadio("d_ct",map.d_ct);
        checkRadio("d_sms",map.d_sms);
        checkRadio("d_bz",map.d_bz);
	}
}

function closeJewelryData(){
    var rfddd = document.getElementById("ss_div");
    rfddd.style.display = "none";
    resetJewelryData();
}

function resetJewelryData(){
	document.getElementById('ss_orderid').innerHTML= "";
	document.getElementById('ss_goodsid').innerHTML= "";
	$("#ss_catid").val("");
    clearradio("s_wz");
    clearradio("s_ks");
    clearradio("s_ys");
    clearradio("s_bm");
    clearradio("s_wgxj");
    clearradio("s_lk");
    clearradio("s_ds");
    clearradio("s_cz");
    clearradio("s_bz");
}

function closeClothingDivdd(){
    var rfddd = document.getElementById("dz_div");
    rfddd.style.display = "none";
     resetElectronicData();
}

//清空电子质检表单数据
function resetElectronicData(){
    document.getElementById('dd_orderid').innerHTML= "";
    document.getElementById('dd_goodsid').innerHTML= "";
    $("#dd_catid").val("");
    clearradio("d_wg");
    clearradio("d_ks");
    clearradio("d_ys");
    clearradio("d_wz");
    clearradio("d_wg1");
    clearradio("d_wg2");
    clearradio("d_ds");
    clearradio("d_nc");
    clearradio("d_rl");
    clearradio("d_pm");
    clearradio("d_fbv");
    clearradio("d_xs");
    clearradio("d_ct");
    clearradio("d_sms");
    clearradio("d_bz");
}


function closeClothingData(){
    var rfddd = document.getElementById("div_clothing");
    rfddd.style.display = "none";
    document.getElementById('clothing_orderid').innerHTML= "";
    document.getElementById('clothing_goodsid').innerHTML= "";
    resetClothingDiv();
}

function clearradio(name){
    $("input[name="+name+"]").removeAttr("checked");
}

function checkRadio(name,value){
    var radiovar = document.getElementsByName(""+name+"");
    for(var i=0;i<radiovar.length;i++)
    {
        if(radiovar[i].value==value)
            radiovar[i].checked = "checked";
    }
}

function resetClothingDiv(){
    clearradio("y_wg");
    clearradio("y_bmxj");
    clearradio("y_wgxj");
    clearradio("y_ll");
    clearradio("y_xt");
    clearradio("y_zg");
    clearradio("y_qw");
    clearradio("y_bz");
    $("#jks_value").val("");
    $("#jk_remark").val("");
    $("#xwb_value").val("");
    $("#xw_remark").val("");
    $("#yww_value").val("");
    $("#yw_remark").val("");
    $("#twh_value").val("");
    $("#tw_remark").val("");
    $("#xcs_value").val("");
    $("#xc_remark").val("");
    $("#yzl_value").val("");
    $("#yc_remark").val("");
    $("#c_catid").val("");
    document.getElementById('clothing_orderid').innerHTML= "";
    document.getElementById('clothing_goodsid').innerHTML= "";
}
function sendSplitSuccessEmail(orderno,ordernoNew,odids,time_,state,email) {
    var s = orderno;
    $.ajax({
        type : 'POST',
        url : '/cbtconsole/orderSplit/genOrderSplitEmail.do',
        data : {
            "orderno" : orderno,
            "odids" : odids,
            "ordernoNew" : ordernoNew,
            "time_" : time_,
            "state" : state,
            "email" : email
        },
        success : function(data) {
            if(data != null){
                $.dialog({
                    title : '发送拆分邮件结果！',
                    content : data,
                    max : false,
                    min : false,
                    lock : true,
                    drag : false,
                    fixed : true,
                    ok : function() {
                        window.location.reload();
                    },
                    cancel : function() {
                        window.location.reload();
                    }
                });
            }
        },
        error:function(msg){
            $.messager.show({
                title:'消息',
                msg:"发送邮件失败！！！",
                showType:'slide',
                style:{
                    right:'',
                    top:document.body.scrollTop+document.documentElement.scrollTop,
                    bottom:''
                }
            });
        }
    });
}
function showMessage(msg,time) {
    $('.mask').show().text(msg);
    if(!time){
        time=1500;
	}
    setTimeout(function() {
        $('.mask').hide();
    }, time);
}

function searchCountry(cname, orderNo){
    $.ajax({
        url:"/cbtconsole/orderInfo/queryCountryNameByOrderNo.do",
        type : "post",
        dataType:"json",
        data :{"orderNo":orderNo},
        success:function(data){
            if(data.ok){
                if(cname!=data){
                    $("#od_country").css("display","inline");
                }
            }
        }
    });
}

function queryRepeat(uid){
    $.ajax({
        url:"/cbtconsole/orderDetails/queryRepeatUserid.do",
        type:"post",
        dataType:"json",
        data : {"userid":uid},
        success:function(data){
            if(data.ok){
                var json = data.data;
                $("#other_id").css("display","inline");
                var content = "相似用户的id： ";
                for(var i=0;i<json.length;i++){
                    content +=json[i]+"&nbsp;";
                }
                $("#other_id").text("相似用户的id： ");
                if(json == null || json == ""){
                    $("#other_id").css("display","none");
                }
            }
        }
    });
}
//手动调整采购人员
function changeBuyer(odid,buyid){
    $.ajax({
        url: "/cbtconsole/order/changeBuyer",
        type:"POST",
        data : {"odid":odid,"admuserid":buyid},
        dataType:"json",
        success:function(data){
            if(data>0){
                $("#info"+odid).text("执行成功");
            }else{
                $("#info"+odid).text("执行失败");
            }
        }
    });
}

function changeOrderBuyer(orderid,admuserid){
    $.ajax({
        url:"/cbtconsole/orderDetails/changeOrderBuyer.do",
        type:"post",
        dataType:"json",
        data : {"orderid":orderid,"admuserid":admuserid},
        success:function(data){
            if(data.ok){
                $("#buyuserinfo").text("执行成功");
            }else{
                $("#buyuserinfo").text("执行失败");
            }
            window.location.reload();
        },
        error : function(res){
            $("#buyuserinfo").text("执行失败,请联系管理员");
        }

    });
}

//获取采购员
function getBuyer(oids){
    var adminName = '<%=user.getAdmName()%>';
    $.ajax({
        url:"/cbtconsole/orderDetails/qyeruBuyerByOrderNo.do",
        type:"post",
        dataType:"json",
        data : {"str_oid" : oids},
        success:function(data){
            if(data.ok>0){
                var json = data.data;
                for(var i=0;i< json.length;i++){
//  					 $("#odid"+json[i].odid).append(json[i].admName);
                    for(var j=0;j<document.getElementById("buyer"+json[i].odid).options.length;j++){
                        if (document.getElementById("buyer"+json[i].odid).options[j].text == json[i].admName){
                            document.getElementById("buyer"+json[i].odid).options[j].selected=true;
                            break;
                        }
                    }
                    if(admid!=1){
                        $("#buyer"+json[i].odid).attr("disabled",true);
                    }
                }
            }
        }
    });
}

//计算利润
function jslr(orderno){

    var i = 0;
    var j =0;
    var hsSum = 0;
    var heSum = 0;
    $("div[id^='"+orderno+"']").each(function(){

        var sBut = $(this).children("div:first").children().eq(1).val();
        alert(sBut);
        var divId = $(this).attr('id');

        //获得原价   和货源价
        var hs = $("#"+divId+"_s").val();
        var _sQuantity = $("#"+divId+"_sQuantity").val();
        var he = $("#"+divId+"_e").val();
        var _eQuantity = $("#"+divId+"_eQuantity").val();
        ///${pb.orderNo}${pbsi.index}_eQuantity
        alert(sBut+"------"+hs+"----"+he);
        //是否确认货源
        //	if(sBut =="取消货源"){
        //有一个为空的价格就不计算
        if(hs!='' & he!=''){
            hsSum += Number(hs)*Number(_sQuantity);

            heSum += Number(he)*Number(_eQuantity);
            i++;
        }
        //	}


        j++;


        alert($(this).attr('id')+"------"+sBut);
    });

    if(heSum == 0){
        $("#"+orderno+"_span").html(j+"/"+i+"   利润：0%");
        $("#"+orderno+"_span_s").html(0);
        $("#"+orderno+"_span_e").html(0);
    }else{
        $("#"+orderno+"_span_s").html((hsSum).toFixed(2) +"USD ("+(hsSum*6.78).toFixed(2)+")");
        $("#"+orderno+"_span_e").html(heSum.toFixed(2));
        var t = Number(hsSum)*6.78 - Number(heSum);
        t = t*100/(Number(hsSum)*6.78);
        t = parseInt(t);//.toFixed(2);
        $("#"+orderno+"_span").html(j+"/"+i+"   利润："+t+"%");

        //alert(j+"/"+i+"   利润："+t+"%");
    }


}

//显示产品历史的价格
function showHistoryPrice(url){
    $.ajax({
        url: "/cbtconsole/orderDetails/showHistoryPrice.do",
        type:"POST",
        dataType:"json",
        data : {"url":url},
        success:function(data){
            if(data.ok){
                var json = data.data;
                var pri = "";
                pri += "<div class='pridivbg'><a class='pridclose' onclick='priclose()'>X</a>"
                for(var i =0;i<json.length;i++){
                    pri += "<p>"+json[i][1]+" &nbsp;&nbsp; "+json[i][0]+"</p>";
                }
                pri +="</div>";
                var topHg = ($(window).height()-$("#prinum").height())/2 + $(document).scrollTop();
                var lefhWt = ($(window).width()-$("#prinum").width())/2;
                $("#prinum").show().append(pri).css({"top":topHg,"left":lefhWt});
                $(".peimask").show().css("height",$(document).height());

            }else{
                data(info.message);
            }
        },
        error: function(res) {
            alert('请求失败,请重试');
        }
    });
}
function priclose(){
    $(".pridivbg").remove();
    $("#prinum").hide();
    $(".peimask").hide();
}

function afterReplenishment(){
    var str=document.getElementsByName("replenishment");
    var orderid=$("#orderNo").val();
    var objarray=str.length;
    var parm="";
    for (i=0;i<objarray;i++){
        if(str[i].checked == true){
            var count=$("#count_"+str[i].value).val();
            if(count=="补货数量"){
                alert("请输入补货数量");
                return;
            }
            parm+=str[i].value+":"+count+":"+orderid+",";
        }
    }
    $.ajax({
        url: "/cbtconsole/orderDetails/afterReplenishment.do",
        type:"POST",
        dataType:"json",
        data : {"parm":parm},
        success:function(data){
            alert(data.message);
        },
        error : function(res){
            alert("执行失败，请联系管理员");
        }
    });
}


//备注回复
function doReplay1(orderid,odid){
    $("#remark_content_").val("");
    $("#rk_orderNo").val(orderid);
    $("#rk_odid").val(odid);
    var rfddd = document.getElementById("repalyDiv1");
    rfddd.style.display = "block";
}

//增加商品沟通信息
function saveRepalyContent(){
    var orderid=$("#rk_orderNo").val();
    var odid=$("#rk_odid").val();
    var text=$("#remark_content_").val();
    $.ajax({
        type : 'POST',
        async : false,
        url : '/cbtconsole/PurchaseServlet?action=saveRepalyContent&className=Purchase',
        data : {
            'orderid' : orderid,
            'odid' : odid,
            "type":'2',
            'text' : text
        },
        dataType : 'text',
        success : function(data){
            if(data.length>0){
                $("#rk_remark_"+orderid+odid+"").html(data);
                $('#repalyDiv1').hide();
            }
        }
    });
}



//弹出评论框yyl
function showcomm(id,car_type,adminname,orderNo,goods_pid,countryid,admindid){
    //var timer1 = setTimeout(function(){
    var controls=document.getElementsByName("but"+goods_pid);
    $("#cm_id").val($(controls[0]).attr("cmid"))//获取主键
    $("#cm_adminname").val(adminname);
    $("#cm_orderNo").val(orderNo);
    $("#cm_goodsPid").val(goods_pid);
    $("#cm_country").val(countryid);
    $("#cm_adminId").val(admindid);
    $("#cm_oid").val(id);
    $("#cm_carType").val(car_type);
    $("#comment_content_").val($(controls[0]).attr("title"))
    var rfddd1 = document.getElementById("commentDiv1");
    rfddd1.style.display = "block";;
    //},2000)

}


//弹窗
function openWindow(url) {
    window.open(url, 'bwindow', 'left=100,top=50,height=600,width=1000,toolbar=no,menubar=no,scrollbars=yes')
}
//查看所有评论入口,使用动态菜单完成 yyl
function seeAllComments(goods_pid, orderNo){
    openWindow("/cbtconsole/website/reviewManagement.jsp?orderno=" + orderNo);
    /* var goods_img = $("#goods_img"+goods_pid).val();
    var oldUrl = $("#goods_url"+goods_pid).val();
    var goodsname = $("#goodsname"+goods_pid).val();
    var goodsprice = $("#goodsprice"+goods_pid).val();
    form = $("<form></form>")
     input1 = $("<input type='hidden' name='goods_img' />")
     input1.attr('value',goods_img)
     input2 = $("<input type='hidden' name='goods_url' />")
     input2.attr('value',oldUrl)
     input3 = $("<input type='hidden' name='goodsname' />")
     input3.attr('value',goodsname)
     input4 = $("<input type='hidden' name='goodsprice' />")
     input4.attr('value',goodsprice)
     input5 = $("<input type='hidden' name='goods_pid' />")
     input5.attr('value',goods_pid)

     form.append(input1)
     form.append(input2)
     form.append(input3)
     form.append(input4)
     form.append(input5)
     openWindow("/cbtconsole/goodsComment/selectcomments.do?" + form.serialize());
     */
}

function jumpTracking(orderid, isDropshipOrderList) {
    //跳转到tracking页面
    if (isDropshipOrderList != undefined && isDropshipOrderList != '') {
        var orderArr = isDropshipOrderList.split(",");
        for(var i=0;i<orderArr.length;i++){
            window.open(
                "http://www.import-express.com/apa/tracking.html?loginflag=true&orderNo="
                + orderArr[i], "_blank");
        }
    } else {
        window.open(
            "http://www.import-express.com/apa/tracking.html?loginflag=true&orderNo="
            + orderid, "_blank");
    }
}
function jumpDetails(orderid, isDropshipOrderList) {
    //跳转到details页面
    if (isDropshipOrderList != undefined && isDropshipOrderList != '') {
        var orderArr = isDropshipOrderList.split(",");
        for(var i=0;i<orderArr.length;i++){
            window.open(
                "http://www.import-express.com/orderInfo/ctporders?paystatus=1&comformFlag=0&loginFlag=true&orderNo="
                + orderArr[i], "_blank");
        }
    } else {
        window.open(
            "http://www.import-express.com/orderInfo/ctporders?paystatus=1&comformFlag=0&loginFlag=true&orderNo="
            + orderid, "_blank");
    }
}

function fnmessage() {
    var text = " <div id=\"split_div\">密送人:<input name=\"email\" id=\"email\" type=\"text\"  onfocus=\"if (value =='选填'){value =''; this.style.color='#000';}\" placeholder=\"选填\" onblur=\"if (value ==''){value='选填'; this.style.color='#999999';}\"  /></div>";
    $.dialog({
        title : '拆单成功是否要发送邮件！',
        content : text,
        max : false,
        min : false,
        lock : true,
        drag : false,
        fixed : true,
        ok : function() {
            var message = $('#email').val();
            alert(message);
        },
        cancel : function() {
        }
    });
}

function openSplitNumPage(orderNo) {
    var url = "/cbtconsole/orderDetails/splitByNumPage?orderNo=" + orderNo;
    var param = "height=860,width=1500,top=80,left=200,toolbar=no,menubar=no,scrollbars=yes, resizable=no,location=no, status=no";
    window.open(url, "windows", param);
}