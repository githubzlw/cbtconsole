
	var uidTem='';
	function showLeftTime() {
		var now = new Date();
		var year = now.getFullYear();
		var month = now.getMonth() + 1;
		var day = now.getDate();
		var hours = now.getHours() > 9 ? now.getHours() : "0" + now.getHours() ;
		var minutes = now.getMinutes() > 9 ? now.getMinutes() : "0" + now.getMinutes();
		var seconds = now.getSeconds() > 9 ? now.getSeconds() : "0" + now.getSeconds();
		document.getElementById("time1").innerHTML = "当前时间：" + year + "年"
				+ month + "月" + day + "日 " + hours + ":" + minutes + ":"
				+ seconds + "";
		//一秒刷新一次显示时间
		var timeID = setTimeout(showLeftTime, 1000);
		d = new Date(); //创建一个Date对象 
		localTime = d.getTime();
		localOffset = d.getTimezoneOffset() * 60000; //获得当地时间偏移的毫秒数 
		utc = localTime + localOffset; //utc即GMT时间 
		offset1 = -5; //以夏威夷时间为例，东10区 
		server = utc + (3600000 * offset1);
		nd = new Date(server);
		offset2 = -7;
		paypal = utc + (3600000 * offset2);
		nd2 = new Date(paypal);

		var year2 = nd.getFullYear();
		var month2 = nd.getMonth() + 1;
		var day2 = nd.getDate();
		var hours2 = nd.getHours() > 9 ? nd.getHours() : "0" + nd.getHours();
		var minutes2 = nd.getMinutes() > 9 ? nd.getMinutes()  : "0" + nd.getMinutes();
		var seconds2 = nd.getSeconds() > 9 ? nd.getSeconds()  : "0" + nd.getSeconds();
		document.getElementById("time2").innerHTML = "服务器时间：" + year2 + "年"
				+ month2 + "月" + day2 + "日 " + hours2 + ":" + minutes2 + ":"
				+ seconds2 + "";

		var year3 = nd2.getFullYear();
		var month3 = nd2.getMonth() + 1;
		var day3 = nd2.getDate();
		var hours3 = nd2.getHours() > 9 ? nd2.getHours() : "0" + nd2.getHours();
		var minutes3 = nd2.getMinutes() > 9 ? nd2.getMinutes() : "0" + nd2.getMinutes();
		var seconds3 = nd2.getSeconds() > 9 ? nd2.getSeconds() : "0" + nd2.getSeconds();
		document.getElementById("time3").innerHTML = "Paypal时间：" + year3 + "年"
				+ month3 + "月" + day3 + "日 " + hours3 + ":" + minutes3 + ":"
				+ seconds3 + "";
	}
	
	document.onkeydown = function(event) {
		var e = event || window.event || arguments.callee.caller.arguments[0];
		if (e && e.keyCode == 13) {
			fn(1);
		}
	}
	//数据导出
	function exportExcel() {
		var str = "";
		var tab = $("#table tr").length;
		if (tab < 0) {
			alert("无数据导出");
			return;
		}
		for (var i = 0; i < tab; i++) {
			var tr = $("#table tr").eq(i);
			var td = tr.find("td").length;
			for (var j = 0; j < td - 1; j++) {
				var html = tr.find("td").eq(j).html();
				if (j == 1) {
					html = "'" + html;
				}
				str += html + ",";
			}
			str += "\n";
		}
		str = encodeURIComponent(str);
		var uri = 'data:text/csv;charset=utf-8,\ufeff' + str;
		var downloadLink = document.createElement("a");
		downloadLink.href = uri;
		downloadLink.download = "order.csv";
		document.body.appendChild(downloadLink);
		downloadLink.click();
		document.body.removeChild(downloadLink);
	}
	
	var isshowUnpaid;
	function fnInquiry(va,type){
		var strRoletype = strm;
		 var strAdmid = strname;
		var startdate = $("#startdate").val();
		var enddate = $("#enddate").val();
		var userid = $("#userid").val();
		var orderno = $("#orderno").val();
		var paymentid=$("#paymentid").val();
		var useremail = $("#useremail").val();
		var state = $("#state").val();
		var trackState = $("#trackState").val();
		var showUnpaid = $('#isShowUnpaid').prop('checked')==true?1:0;
		if(state == 0){
			showUnpaid = 1;
		}
		if (showUnpaid==null) {
			showUnpaid = 0;
		}
		isshowUnpaid = showUnpaid;
		var username = "";
		var admuserid;
		if ($("#adminusersc").val() == null) {
			admuserid =strAdmid ;
			if(strRoletype==0)
				{
				admuserid=0;
				}
		} else {
			admuserid = $("#adminusersc").val();
			
		}
		var buyuser;
		if ($("#buyuser").val() == null) {
			buyuser = 0;
		} else {
			buyuser = $("#buyuser").val();
		}
		if (va == 1) {
			page = 1;
		} else if (va == 2) {
			page = page + 1;
			if (count < page) {
				return;
			}
		} else if (va == 3) {
			page = page - 1;
			if (0 >= page) {
				page = 1;
				return;
			}
		} else if (va == 4) {
			page = $("#jump").val();
		}
		window.location.href="/cbtconsole/order/getOrderInfo.do?page="+page+"&userid="+userid+"&orderno="+orderno+"&email="+useremail+"&state="+state+"&trackState="+trackState+"&buyuser="+buyuser+"&admuserid="+admuserid+"&startdate="+startdate+"&enddate="+enddate+"&showUnpaid="+isshowUnpaid+"&type="+type+"&paymentid="+paymentid
	}
	
	
	//判断是否为数字
	function IsNum(s) {
		if (s != null && s != "") {
			return !isNaN(s);
		}
		return false;
		document.cookie;
	}
	
	function reset() {
		$('#startdate').val('');
		$('#enddate').val('');
		$('#state').val('-2');
		$('#adminusersc').val('0');
		$('#buyuser').val('0');
		$('#orderno').val('');
		$('#userid').val('');
		$('#useremail').val('');
        $('#paymentid').val('');
		fnInquiry(1,'');
	}
	
	
	
	
	function myfunction(){
		var isshowUnpaidCheckBox = showUnpaid;
		isshowUnpaid = isshowUnpaidCheckBox;
		if (isshowUnpaidCheckBox == 1) {
			$("#isShowUnpaid").attr("checked",true);
		}
	}

function fnAddDate(datas,day){
	datas = new Date(datas);
	datas = +datas + 1000*60*60*24*day;
	datas = new Date(datas);
     //格式化
     return datas.getFullYear()+"-"+(datas.getMonth()+1)+"-"+datas.getDate();
}
//统计
function fnGetStatistic() {
    var url = '/cbtconsole/order/getOrderStates.do';
    var noDeleteCount = '';
    // if (typeof (sessionStorage.getItem("fnGetStatistic")) != 'undefined' && sessionStorage.getItem("fnGetStatistic") !=null) {
    //     noDeleteCount = JSON.parse(sessionStorage.getItem("fnGetStatistic"));
    // }
    if (noDeleteCount != '') {
        var json =noDeleteCount;
        var cacleorder = 0;
        for (var i = 0; i < json.length; i++) {
            if (json[i].state == "deliver") $("#errorgoods").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=deliver'>" + json[i].counts + "</a>");//出货问题
            // if(json[i].state == "purchase")$("#errorbuy").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=purchase'>" + json[i].counts + "</a>");//采购问题
            if (json[i].state == "agree") $("#getchange").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=agree'>" + json[i].counts + "</a>");//同意替换
            if (json[i].state == "suggest") $("#changes").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=suggest'>" + json[i].counts + "</a>");//建议替换
            if (json[i].state == "noChange") $("#noChange").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=noChange'>" + json[i].counts + "</a>");//取消替换
            // if (json[i].state == "cy") $("#onshipping").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=cy'>" + json[i].counts + "</a>");//出运
            if (json[i].state == "1") $("#onshippingw").html("<a target=\"_blank\" href='/cbtconsole/website/tab_track_info_list.html'>" + json[i].counts + "</a>");//出运中但物流预警数据
            // if(json[i].state == "ck")$("#allgoods").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&state=2'>" + json[i].counts + "</a>");//到达仓库
            if (json[i].state == "purchasewarning") $("#purchasewarning").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=purchasewarning'>" + json[i].counts + "</a>");//采购预警项目
            if (json[i].state == "storagewarning") $("#storagewarning").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=storagewarning'>" + json[i].counts + "</a>");//入库预警项目
            if (json[i].state == "shipmentwarning") $("#shipmentwarning").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=shipmentwarning'>" + json[i].counts + "</a>");//出货预警项目
            if (json[i].state == "close1" || json[i].state == "close") cacleorder += json[i].counts;//取消
            // if(json[i].state == "freightWaraing" )$("#freightWaraing").html("<a href='/cbtconsole/warehouse/getPackageInfoList'>" + json[i].counts + "</a>");//出运是运费过高预警
            if (json[i].state == "notshipping") $("#notshipping").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=notshipping'>" + json[i].counts + "</a>");//未出货项目
            if (json[i].state == "order_pending") $("#order_pending").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=order_pending'>" + json[i].counts + "</a>");//未出货项目
            if (json[i].state == "checkOrder") $("#checkOrder").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=checkOrder'>" + json[i].counts + "</a>");//质检服务订单
            //超过交期项目
        }
        $("#cacleorder").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=cacle'>" + cacleorder + "</a>");


    } else {
       var admuserid= $("#adminusersc").val()
		$.ajax({
			url: url,
            data:{ "admuserid":admuserid},
			success: function (res) {
				if (res) {
					sessionStorage.setItem("fnGetStatistic", JSON.stringify(res));
					var json = eval(res);
					var cacleorder = 0;
					for (var i = 0; i < json.length; i++) {
						if (json[i].state == "deliver") $("#errorgoods").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=deliver&admuserid="+admuserid+"'>" + json[i].counts + "</a>");//出货问题
						// if(json[i].state == "purchase")$("#errorbuy").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=purchase'>" + json[i].counts + "</a>");//采购问题
						if (json[i].state == "agree") $("#getchange").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=agree&admuserid="+admuserid+"'>" + json[i].counts + "</a>");//同意替换
						if (json[i].state == "suggest") $("#changes").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=suggest&admuserid="+admuserid+"'>" + json[i].counts + "</a>");//建议替换
                        if (json[i].state == "noChange") $("#noChange").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=noChange'>" + json[i].counts + "</a>");//取消替换
						// if (json[i].state == "cy") $("#onshipping").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=cy&admuserid="+admuserid+"'>" + json[i].counts + "</a>");//出运
			            if (json[i].state == "1") $("#onshippingw").html("<a target=\"_blank\" href='/cbtconsole/website/tab_track_info_list.html'>" + json[i].counts + "</a>");//出运中但物流预警数据
						// if(json[i].state == "ck")$("#allgoods").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&state=2'>" + json[i].counts + "</a>");//到达仓库
						if (json[i].state == "purchasewarning") $("#purchasewarning").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=purchasewarning&admuserid="+admuserid+"'>" + json[i].counts + "</a>");//采购预警项目
						if (json[i].state == "storagewarning") $("#storagewarning").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=storagewarning&admuserid="+admuserid+"'>" + json[i].counts + "</a>");//入库预警项目
						if (json[i].state == "shipmentwarning") $("#shipmentwarning").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=shipmentwarning&admuserid="+admuserid+"'>" + json[i].counts + "</a>");//出货预警项目
						if (json[i].state == "close1" || json[i].state == "close") cacleorder += json[i].counts;//取消
						// if(json[i].state == "freightWaraing" )$("#freightWaraing").html("<a href='/cbtconsole/warehouse/getPackageInfoList'>" + json[i].counts + "</a>");//出运是运费过高预警
						if (json[i].state == "notshipping") $("#notshipping").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=notshipping&admuserid="+admuserid+"'>" + json[i].counts + "</a>");//未出货项目
						if (json[i].state == "order_pending") $("#order_pending").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=order_pending&admuserid="+admuserid+"'>" + json[i].counts + "</a>");//未出货项目
                        //质检服务订单
                        if (json[i].state == "checkOrder") $("#checkOrder").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=checkOrder&admuserid="+admuserid+"'>" + json[i].counts + "</a>");
					}
					$("#cacleorder").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=cacle&admuserid="+admuserid+"'>" + cacleorder + "</a>");

				} else {
					alert('获取头部数量错误');
				}
			}
		});
	}
	 /*$.post("/cbtconsole/order/getOrderStates.do",
				{},
				function(res){
					var json = eval(res);
					var cacleorder = 0;
					for (var i = 0; i < json.length; i++) {
						if(json[i].state == "deliver")$("#errorgoods").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=deliver'>" + json[i].counts + "</a>");//出货问题
						// if(json[i].state == "purchase")$("#errorbuy").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=purchase'>" + json[i].counts + "</a>");//采购问题
						if(json[i].state == "agree")$("#getchange").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=agree'>" + json[i].counts + "</a>");//同意替换
						if(json[i].state == "suggest")$("#changes").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=suggest'>" + json[i].counts + "</a>");//建议替换
						if(json[i].state == "cy")$("#onshipping").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=cy'>" + json[i].counts + "</a>");//出运
            			if (json[i].state == "1") $("#onshippingw").html("<a target=\"_blank\" href='/cbtconsole/website/tab_track_info_list.html'>" + json[i].counts + "</a>");//出运中但物流预警数据
						// if(json[i].state == "ck")$("#allgoods").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&state=2'>" + json[i].counts + "</a>");//到达仓库
						if(json[i].state == "purchasewarning")$("#purchasewarning").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=purchasewarning'>" + json[i].counts + "</a>");//采购预警项目
						if(json[i].state == "storagewarning")$("#storagewarning").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=storagewarning'>" + json[i].counts + "</a>");//入库预警项目
						if(json[i].state == "shipmentwarning")$("#shipmentwarning").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=shipmentwarning'>" + json[i].counts + "</a>");//出货预警项目
						if(json[i].state == "close1" || json[i].state == "close") cacleorder += json[i].counts;//取消
                        // if(json[i].state == "freightWaraing" )$("#freightWaraing").html("<a href='/cbtconsole/warehouse/getPackageInfoList'>" + json[i].counts + "</a>");//出运是运费过高预警
						if(json[i].state == "notshipping" )$("#notshipping").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=notshipping'>" + json[i].counts + "</a>");//未出货项目
                        if(json[i].state == "order_pending" )$("#order_pending").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=order_pending'>" + json[i].counts + "</a>");//未出货项目
						//超过交期项目
					}
						$("#cacleorder").html("<a href='/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=cacle'>" + cacleorder + "</a>");
				});*/
}

function getMessageNum(uid){
	if(!uid){
		return;
	}
	var url= "/cbtconsole/messages/findBasicMessages";
	var mydate = new Date();
	var rYear =mydate.getFullYear(); 
	var rMonth = mydate.getMonth()+1;
	var rDay = mydate.getDate();
	var timeTo = rYear+'-'+ rMonth+'-'+rDay+' 23:59:59';
	mydate.setDate(mydate.getDate() -7);
	rDay = mydate.getDate();
	var timeFrom = rYear+'-'+ rMonth+'-'+rDay+' 00:00:00';
	var admuserid = uid;
    var noDeleteCount = '';
	if(typeof (sessionStorage.getItem("getMessageNum"))!= 'undefined' && sessionStorage.getItem("getMessageNum") != null){
       noDeleteCount = JSON.parse(sessionStorage.getItem("getMessageNum"));
    }
	if(noDeleteCount != '' && noDeleteCount != null){
        var messagesCountList = noDeleteCount;
        console.log(messagesCountList);
        var messagesCountVo = new Object();
        console.log(messagesCountVo);
        for(var i=0;i<messagesCountList.length;i++){
            messagesCountVo = messagesCountList[i];
            if(messagesCountVo.type=='propagemessage'){
                $('#propagemessage').html( messagesCountVo.noDeleteCount);
            }else if(messagesCountVo.type=='shopcarmarket'){
                $('#shopcarmarket').html(messagesCountVo.noDeleteCount);
                $('#shopcarmarket1').html(messagesCountVo.countAll);
            }
            else if(messagesCountVo.type=='customerInfoCollection'){
                $('#customerInfoCollection').html(messagesCountVo.noDeleteCount);
            }
            else if(messagesCountVo.type=='questionnum'){
            	$('#questionnum').html(messagesCountVo.noDeleteCount);
            }
            else if(messagesCountVo.type=='businquiries'){
                if(admuserid ==1 || admuserid == 83 || admuserid == 84){
                    $('#businquiries').html(messagesCountVo.noArrgCount);
                    $('#businquiries1').html(messagesCountVo.noDeleteCount);
                    $('#businquiries2').html(messagesCountVo.countAll);
                }else{
                    $('#businquiries').css("display","none");
                    $('#businquiries1').html(messagesCountVo.noDeleteCount);
                    $('#businquiries2').html(messagesCountVo.countAll);
                }
                $('#businquiries1').html(messagesCountVo.noDeleteCount);
                // if(admuserid ==1 || admuserid == 83){
                //     $('#businquiries').html(messagesCountVo.noArrgCount);
                //     $('#businquiries1').html(messagesCountVo.noDeleteCount);
                //     $('#businquiries2').html(messagesCountVo.countAll);
                // }else{
                //     $('#businquiries').css("display","none");
                //     $('#businquiries1').html(messagesCountVo.noDeleteCount);
                //     $('#businquiries2').html(messagesCountVo.countAll);
                // }
            }else if(messagesCountVo.type=='refundscom'){
                $('#refundscom').html(messagesCountVo.noDeleteCount);
                $('#refundscom1').html(messagesCountVo.countAll);
            }else if(messagesCountVo.type=='refundscom2'){
                $('#refundscom2').html(messagesCountVo.noDeleteCount);
            }
            else if(messagesCountVo.type=='cartMarket'){
                $('#shopcarmarket1').html(messagesCountVo.countAll);
                $("#shopcarmarket").html(messagesCountVo.noArrgCount);
            }else if(messagesCountVo.type=='ordermessage'){
                $('#ordermessage').html(messagesCountVo.noArrgCount);
            }
            if(messagesCountVo.type=='ordermeg'){
                if(admuserid !=1){
                    $('#showorder').css('display','none');
                }
                $('#ordermeg').html(messagesCountVo.noArrgCount);
            }


        }
	}else {
        jQuery.ajax({
            url:url,
            data:{"timeFrom":timeFrom,
                "timeTo":timeTo,
                "adminid":admuserid},
            type:"post",
            success:function(data, status){
                sessionStorage.setItem("getMessageNum",JSON.stringify(data.data));
                if(data.ok){
                    var messagesCountList = data.data;
                    console.log(messagesCountList);
                    var messagesCountVo = new Object();
                    console.log(messagesCountVo);
                    for(var i=0;i<messagesCountList.length;i++){
                        messagesCountVo = messagesCountList[i];
                        if(messagesCountVo.type=='propagemessage'){
                            sessionStorage.setItem("noDeleteCount",messagesCountVo.noDeleteCount);
                            sessionStorage.setItem("countAll",messagesCountVo.countAll);
                            $('#propagemessage').html( messagesCountVo.noDeleteCount);
                        }else if(messagesCountVo.type=='shopcarmarket'){
                            $('#shopcarmarket').html(messagesCountVo.noDeleteCount);
                            $('#shopcarmarket1').html(messagesCountVo.countAll);
                        }
                        else if(messagesCountVo.type=='customerInfoCollection'){
                            $('#customerInfoCollection').html(messagesCountVo.noDeleteCount);
                        }
                        else if(messagesCountVo.type=='questionnum'){
                        	$('#questionnum').html(messagesCountVo.noDeleteCount);
                        }
//        			else if(messagesCountVo.type=='systemfailure'){
//        				$('#systemfailure').html(messagesCountVo.noDeleteCount);
//        			}
                        else if(messagesCountVo.type=='businquiries'){
                            if(admuserid ==1 || admuserid ==83 || admuserid ==84){
                                $('#businquiries').html(messagesCountVo.noArrgCount);
                                sessionStorage.setItem("noArrgCount",messagesCountVo.noArrgCount);
                                $('#businquiries1').html(messagesCountVo.noDeleteCount);
                                $('#businquiries2').html(messagesCountVo.countAll);
                            }else{
                                $('#businquiries').css("display","none");
                                $('#businquiries1').html(messagesCountVo.noDeleteCount);
                                $('#businquiries2').html(messagesCountVo.countAll);
                            }
                            sessionStorage.setItem("noDeleteCount",messagesCountVo.noDeleteCount);
                            $('#businquiries1').html(messagesCountVo.noDeleteCount);
                            // if(admuserid ==1 || admuserid ==83){
                            //     $('#businquiries').html(messagesCountVo.noArrgCount);
                            //     sessionStorage.setItem("noArrgCount",messagesCountVo.noArrgCount);
                            //     $('#businquiries1').html(messagesCountVo.noDeleteCount);
                            //     $('#businquiries2').html(messagesCountVo.countAll);
                            // }else{
                            //     $('#businquiries').css("display","none");
                            //     $('#businquiries1').html(messagesCountVo.noDeleteCount);
                            //     $('#businquiries2').html(messagesCountVo.countAll);
                            // }
                        }/*else if(messagesCountVo.type=='batapply'){
        				if(admuserid ==1){
        				$('#batapply').html(messagesCountVo.noArrgCount);
        				$('#batapply1').html(messagesCountVo.noDeleteCount);
        				$('#batapply2').html(messagesCountVo.countAll);
        				}else{
        					$('#batapply').css("display","none");
            				$('#batapply1').html(messagesCountVo.noDeleteCount);
            				$('#batapply2').html(messagesCountVo.countAll);
        				}
        			}*/else if(messagesCountVo.type=='refundscom'){
                            $('#refundscom').html(messagesCountVo.noDeleteCount);
                            $('#refundscom1').html(messagesCountVo.countAll);
                        }else if(messagesCountVo.type=='refundscom2'){
                            $('#refundscom2').html(messagesCountVo.noDeleteCount);
                        }
                        else if(messagesCountVo.type=='cartMarket'){
                            $('#shopcarmarket1').html(messagesCountVo.countAll);
                            $("#shopcarmarket").html(messagesCountVo.noArrgCount);
                        }else if(messagesCountVo.type=='ordermessage'){
                            $('#ordermessage').html(messagesCountVo.noArrgCount);
                        }
                        if(messagesCountVo.type=='ordermeg'){
                            if(admuserid !=1){
                                $('#showorder').css('display','none');
                            }//else{
                            $('#ordermeg').html(messagesCountVo.noArrgCount);
                            //}
                        }


                    }
                }else{
                    alert(data.message);
                }
            },
            error:function(e){
                alert("消息数量信息查询失败！");
            }
        });
	}
}

function fnGetMessage(uid){
	//查询消息列表
	var mydate = new Date();
	var rYear =mydate.getFullYear(); 
	var rMonth = mydate.getMonth()+1;
	var rDay = mydate.getDate();
	var timeTo = rYear+'-'+ rMonth+'-'+rDay;
	//9.6 lyb  如果当前的日期<=7号，日期相减后 月份应该退到上一个月
	if(rDay<=7 ){
		if(rMonth==1){ //如果当前是1月份，月份退到12月，年份退到上一年
			rMonth = 12;
			rYear = rYear-1;
		}else{ //否则仅把月份退到上一个月
			rMonth = rMonth-1;
		}
	}
	mydate.setDate(mydate.getDate() -7);
	rDay = mydate.getDate();
	var timeFrom = rYear+'-'+ rMonth+'-'+rDay;
    var admuserid = uid;
	$('#adminid').val(admuserid);
	if(admuserid!=1){
		$('#assignment').css("display","none");
	}
	getMessageNum(uid); //获取各种消息数量
	$('.btn').click(function(){
		var type = $(this).parent().parent().find('#type').val();
		var style =$(this).parent().find('#style').val();
		var url="";
		if(type=="batapply"){
			url="/cbtconsole/messages/preferential";
			if(style=="noArrage"){
				url+='?adminid='+uid+'&type=-2';
				window.open(url);
			}else if(style=="noDelete"){
				url+='?adminid='+uid+'&type=0';
				window.open(url);
			}else{
				url+='?adminid='+uid+'&type=-1';
				window.open(url);
			}
		}else if(type=="propagemessage"){
            url="/cbtconsole/website/guestbook.jsp?status=0&questionType=2";
            if(",0,1,8,18,62,83,84,".indexOf("," + uid + ",") == -1){
                url += "&adminId=" + uid;
            }
            window.open(url);
		}else if(type=="customerInfoCollection"){
            url="/cbtconsole/apa/customerInfo_collection.html?is_report=2";
            if(",0,1,8,18,62,83,84,".indexOf("," + uid + ",") == -1){
                url += "&adminId=" + uid;
            }
            window.open(url);
		}else if(type=="questionnum"){
            url="/cbtconsole/question/questionlist?replayflag=1";
            if(",0,1,8,18,62,83,84,".indexOf("," + uid + ",") == -1){
                url += "&adminid=" + uid;
            }
            window.open(url);
		}else if(type=="businquiries"){
            url="/cbtconsole/messages/getBusiess?state=0";
            if(",0,1,8,18,62,83,84,".indexOf("," + uid + ",") == -1){
                url += "&adminid=" + uid;
            } else {
                url += "&adminid=0";
            }
            window.open(url);
		}else if(type=="shopcarmarket"){
			url="/cbtconsole/website/shoppingCartManagement.jsp";
			 if(style=="noDelete"){
				url+='?status=3';
				window.open(url);
			}else{
				url+='?status=2';
				window.open(url);
			}
		}else if(type=="refundscom"){
			//未处理数量
			url="/cbtconsole/complain/";
			if(style=="noDelete"){
				url+='searchComplainByParam?userid=&creatTime=&complainState=0&username=&toPage=1&currentPage=1';
				window.open(url);
			}else{
				url+='searchComplainByParam?userid=&creatTime=&complainState=-1&username=&toPage=1&currentPage=1';
				window.open(url);
			}
		}else if(type=="refundscom2"){
			url="/cbtconsole/complain/";
				url+='searchComplainByParam?userid=&creatTime=&complainState=2&username=&toPage=1&currentPage=1';
				window.open(url);
		}else if(type=="ordermessage"){
			url="/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=latelyOrdermessage";
			window.location.href=url;
		}else if(type=="systemfailure"){
//			fnInquiry(1,type);
			url="/cbtconsole/order/getOrderInfo.do?showUnpaid=1&type=systemfailure";
			window.location.href=url;
		}else{
			url="/cbtconsole/order/getOrderInfo.do?showUnpaid=0&type=ordermeg";
			window.open(url);
		}
	});
}

//比较时间
function fnComparisonDate(a, b) {
    var arr = a.split("-");
    var starttime = new Date(arr[0], arr[1], arr[2]);
    var starttimes = starttime.getTime();
    var arrs = b.split("-");
    var lktime = new Date(arrs[0], arrs[1], arrs[2]);
    var lktimes = lktime.getTime();
    if (starttimes >= lktimes) {
        return false;
    }
    else
        return true;

}
function reFreshoDate() {
	sessionStorage.removeItem('fnGetStatistic');
    sessionStorage.removeItem('getMessageNum');
    fnGetStatistic();
    getMessageNum(uidTem);
}
// $('#reFreshoDate').click(function () {
//     reFreshoDate();
// });

	function openCheckEmailForUser(orderid,email){
        $("#checkOrderid").val(orderid);
       $("#checkEmail").val(email);
        var rfddd = document.getElementById("checkDiv");
        rfddd.style.display = "block";
        $("#checkRemark").val("");
	}

	function closeCheckEmailForUser(){
        $("#checkRemark").val("");
        $("#checkOrderid").val("");
        $("#checkEmail").val("");
        var rfddd = document.getElementById("checkDiv");
        rfddd.style.display = "none";
	}

	function sendCheckEmailForUser(){
		var orderid=$("#checkOrderid").val();
		var email=$("#checkEmail").val();
		var remark=$("#checkRemark").val();
        $.ajax({
            type: "POST",//方法类型
            dataType:'json',
            url:'/cbtconsole/order/sendCheckEmailForUser',
            data:{"orderNo":orderid,"email":email,"remark":remark},
            dataType:"json",
            success:function(data) {
                if(data == 1){
                    alert("发送成功");
                    closeCheckEmailForUser();
                }else if(data == 2){
                    alert("已发送过质检邮件，不能重复发送");
                    closeCheckEmailForUser();
				}else{
                    alert("发送失败");
                }
            }
        });
	}
	function delOrderinfo(orderno,that){
		//搞一个确认弹窗
		$.dialog({
			title : '删除订单',
			content : "是否标记该订单在该页面不在显示",
			max : false,
			min : false,
			lock : true,
			drag : false,
			fixed : true,
			ok : function() {
				$.ajax({
					type:"POST",
					url:'/cbtconsole/order/delOrderinfo',
					data:{orderno:orderno},
					success:function(data) {
						if(data == 1){
							$.dialog.alert("Message",orderno + ' 删除成功 ! ');
							$('#tr_'+orderno).hide();
						}else{
							$.dialog.alert("Message",orderno + ' 删除失败 ! ');
						}
					}
				});
			},
			cancel : function() {
				return;
			}
		});

	}