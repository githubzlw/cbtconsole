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
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/main.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/website/order_detail_new.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/lhgdialog/lhgcore.lhgdialog.min.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/lhgdialog/lhgdialog.min.js?self=true&skin=discuz"></script>
<script type="text/javascript"
	src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<style type="text/css">
#prinum {
	width: 250px;
	position: absolute;
	top: 50%;
	left: 50%;
	background: #f6cece;
	border: 1px solid #ddd;
	padding: 20px;
	z-index: 999;
	padding-top: 20px;
	display: none;
}
.show_h3 {
	height: 20px;
	text-align: left;
}
.pridclose {
	position: absolute;
	top: 3px;
	right: 3px;
	color: #f00;
}
.imagetable tr td {
	border-top: 1px solid #ddd;
	border-left: 1px solid #ddd;
	border-bottom: 1px solid #ddd;
	text-align: center;
}

.imagetable tr td:last-child {
	border-right: 1px solid #ddd;
}
.peimask {
	background: #777;
	opacity: 0.8;
	filter: alpha(opacity = 80);
	position: absolute;
	top: 0;
	left: 0;
	z-index: 998;
	width: 100%;
	height: 100%;
	display: none;
}
.mod_pay3 {
	width: 500px;
	position: fixed;
	margin-left:40%;
	margin-top:10%;
	z-index: 1011;
	background: gray;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 1011;
	border: 15px solid #33CCFF;
}
.repalyBtn{
    height: 30px;
    width: 70px;
    background: #1c9439;
    border: 0px solid #dcdcdc;
    color: #ffffff;
    cursor: pointer;
}

.mask{
	display:none;position:absolute;left:0;top:0;bottom:0;right:0;margin:auto;
	background:rgba(0,0,0,0.6);width:300px;height:60px;line-height:60px;text-align:center;
	border-radius: 10px;font-size:20px;color:#fff;z-index:100;
}
#div_clothing,#ss_div,#dz_div{
	position: fixed;
	top: 50%;
	left: 50%;
	-webkit-transform: translate(-50%,-50%);
	-moz-transform: translate(-50%,-50%);
	-ms-transform: translate(-50%,-50%);
	-o-transform: translate(-50%,-50%);
	transform: translate(-50%,-50%);
}
#div_clothing table,#ss_div table,#dz_div table{background-color: pink;}
#div_clothing input,#ss_div input,#dz_div input{background-color: #eee;}
</style>

<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.js"></script>
<script type="text/javascript">
$(function() {
    $(".imgclass").lazyload({//加载图片
    	effect:'fadeIn',
    	threshold : 300
    });
});
</script>
<script type="text/javascript">
var adminid = ${order.adminid};
<%String sessionId = request.getSession().getId();
	String userJson = Redis.hget(sessionId, "admuser");
	Admuser user = (Admuser) SerializeUtil.JsonToObj(userJson, Admuser.class);
	int uid =0;
	if(user !=null){
		uid = user.getId();
	}else{
		user = new Admuser();
		user.setId(0);
		user.setAdmName("");
	}%>
$(function(){
	admid = '<%=uid%>';
})
//计算预估运费
$(document).ready(function(){
	var sale = <%=request.getAttribute("sale")%>;  //销售额
	var buy = <%=request.getAttribute("buy")%>;  //采购额
	var country = <%=request.getAttribute("country")%>;  //国家id
	var volume = <%=request.getAttribute("volume")%>; //总体积
	var weight = <%=request.getAttribute("weight")%>; //总重量
    var goodsWeight = <%=request.getAttribute("goodsWeight")%>; //产品总重量
	var jq = <%=request.getAttribute("avg_jq")%>; //交期平均值
	var rate = <%=request.getAttribute("rate")%>; //汇率
	var allFreight = <%=request.getAttribute("allFreight")%>; //国际运费
	var order_state = <%=request.getAttribute("order_state")%>;
    var actual_freight = <%=request.getAttribute("actual_freight")%>;
    var piaAmount=<%=request.getAttribute("piaAmount")%>;
    var estimatefreight = <%=request.getAttribute("estimatefreight")%>; //接口生成预估运费
    var es_buyAmount = <%=request.getAttribute("es_prices")%>;//预计采购金额
    var transportcompany = '<%=request.getAttribute("transportcompany")%>';//运输公司
    var shippingtype = '<%=request.getAttribute("shippingtype")%>';//运输方式
	var allWeight=<%=request.getAttribute("allWeight")%>;//产品表中公式计算的重量，custom_benchmark_ready-final_weight
	var awes_freight='<%=request.getAttribute("awes_freight")%>';//仓库称重后预估的运费
	var ac_weight= <%=request.getAttribute("ac_weight")%>; //eric称重重量包裹
	if(buy >0){
        buy=Number(buy)+Number(piaAmount);
	}
    if(allFreight == null || allFreight == ""){
		allFreight=0.00;
	}
    if(estimatefreight == null || estimatefreight == ""){
        estimatefreight=0.00;
    }
    if(es_buyAmount == null || es_buyAmount == ""){
        es_buyAmount=0.00;
        piaAmount=0.00;
    }else{
        es_buyAmount=Number(es_buyAmount)+Number(piaAmount);
	}
	var es_profit=sale-es_buyAmount-allFreight;
    es_profit=es_profit.toFixed(2);
    var ec_p=(sale-es_buyAmount-allFreight)/sale*100;
    if(ec_p == null || ec_p == ""){
        ec_p="0.00";
    }else{
        ec_p=ec_p.toFixed(2);
    }
    var ac_profit="--";
    var ac_p="--";
    var end_profit="--";
    var end_p="--";
    if(actual_freight == "654321"){
        actual_freight="--";
	}
  	if(awes_freight != "-"){
        ac_profit=sale-buy-awes_freight;
        ac_profit=ac_profit.toFixed(2);
        ac_p=(sale-buy-awes_freight)/sale*100;
        if(ac_p == null || ac_p == ""){
            ac_p="--";
        }else{
            ac_p=ac_p.toFixed(2);
        }
	}
	if(buy>0 && actual_freight != '--'){
        end_profit=sale-buy-actual_freight;
        end_profit=end_profit.toFixed(2);
        end_p=end_profit/sale*100;
        end_p=end_p.toFixed(2);
	}
	$("#es_price").html(es_buyAmount.toFixed(2));
    $("#esPidAmount").html("(包含预计国内运费:"+piaAmount+")");
    $("#pay_price").html(sale+";汇率:("+rate+")");
    $("#end_profit").html(end_profit);
    $("#end_p").html(end_p+"%");
    $("#transportcompany").html(transportcompany == null || transportcompany ==""?"--":transportcompany);
    $("#shippingtype").html(shippingtype == null || shippingtype == ""?"--":shippingtype);
    $("#buyAmount").html(buy.toFixed(2));
    $("#es_weight").html(weight);
    $("#ac_weight").html(ac_weight);
    $("#es_freight").html(allFreight.toFixed(2));
    $("#awes_freight").html(awes_freight);
    $("#ac_freight").html(actual_freight);
    $("#es_profit").html(es_profit);
    $("#ac_profit").html(ac_profit);
    // $("#goodsWeight").html(goodsWeight);
    $("#es_p").html(ec_p+"%");
    $("#ac_p").html(ac_p+"%");
	var cname = "<%=request.getAttribute("countryName")%>"; //国际运输方式中的国家名
	var orderNo = "<%=request.getAttribute("orderNo")%>"; //订单号
	var oids = "<%=request.getAttribute("str_oid")%>"; //订单下所有的oid
	if(orderNo!="" && cname!="" && cname!=null){
		searchCountry(cname, orderNo);
	}
	var userid = <%=request.getAttribute("userid")%>;
	queryRepeat(userid);
 	getBuyer(oids);
 	getAllBuyuser();
	fnGetAddress();
 	var adminName = '<%=user.getAdmName()%>';
 	if(adminName !="Ling"){
 		// $("#buyuser1").attr("disabled",true);
 		$("#buy_but").attr("disabled",true);
 		$("#saler").attr("disabled",true);
 		$("#saler_but").attr("disabled",true);
 	}
 	$("#ordercountry_value").val("${order.address.country}");
    $("#ordercountry").val($("#ordercountry_value").val());
});

function showMessage(msg) {
	$('.mask').show().text(msg);
	setTimeout(function() {
		$('.mask').hide();
	}, 1500);
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
					if(admid!=1 || adminName !="Ling"){ 
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
</script>

<link type="text/css" rel="stylesheet"
	href="/cbtconsole/css/web-ordetail.css" />
<title>订单详情管理</title>
<style type="text/css">
em {
	font-style: normal;
}

.orderInfo tr {
	border-bottom: 1px solid red;
}
</style>
</head>
<body onload="fninitbuy()">
	<div class="mask"></div>
	<div
		style="width: 50px; height: 50px; position: fixed; right: 50px; top: 800px; background: url() no-repeat;">
		<img src="/cbtconsole/img/website/top.png">
	</div>
	<a href="#top"> </a>
		<div class="mod_pay3" style="display: none;" id="repalyDiv1">
		<div>
				<a href="javascript:void(0)" class="show_x"
					onclick="$('#repalyDiv1').hide();" style="float: right;">╳</a>
			</div>
		    <input id="rk_orderNo" type="hidden" value="">
		    <input id="rk_odid" type="hidden" value="">
		    <input type="hidden" id="ordercountry_value">
		    回复内容:
		    <textarea name="remark_content" rows="8" cols="50" style="margin-top: 20px;" id="remark_content_"></textarea>
		    <input type="button" id="repalyBtnId" onclick="saveRepalyContent()" value="提交回复">
		</div>	
		<!-- 采购商品打分弹出层 -->
		<div class="mod_pay3" style="display: none;width:720px;" id="supplierDiv">
		<div>
			<a href="javascript:void(0)" style="margin-left:700px" class="show_x" onclick="FncloseSupplierDiv()">╳</a>
		</div>
		<center>
			<h3 class="show_h3">采购供应商打分</h3>
			<table id="supplierDivInfos" class="imagetable">
				<thead>
					<tr>
						<td rowspan="5"><span style="color:red">[质量]</span>:1:差   2:较差  3: 一般  4:无投诉  5: 优质</td>
					</tr>
					<%--<tr>--%>
						<%--<td rowspan="5"><span style="color:red">[服务]</span>:1:发货不及时  2:发货及时但是乱发  3:处理问题回复不及时  4:售后处理配合度一般  5:售后处理配合度好</td>--%>
					<%--</tr>--%>
					<tr>
					  <td>店铺</td><td>质量</td><td>是否有库存协议</td><td>支持退货天数</td>
					</tr>
					<tr>
					  <td>
						<span id="su_shop_id"></span>
					  </td>
					  <td>
					    <select id="quality">
                          <option value="0">---请选择---</option>
                          <option value="1">1分</option>
                          <option value="2">2分</option>
                          <option value="3">3分</option>
                          <option value="4">4分</option>
                          <option value="5">5分</option>
                        </select>
					  </td>
					  <%--<td>--%>
					    <%--<select id="service">--%>
                          <%--<option value="0">---请选择---</option>--%>
                          <%--<option value="1">1分</option>--%>
                          <%--<option value="2">2分</option>--%>
                          <%--<option value="3">3分</option>--%>
                          <%--<option value="4">4分</option>--%>
                          <%--<option value="5">5分</option>--%>
                        <%--</select>--%>
					  <%--</td>--%>
					  <td>
					    <input name="protocol" type="radio" value="2"/>有<input name="protocol" type="radio" value="1"/>无
					  </td>
					  <td>
					   <input type="text" id="su_data" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  
    onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'0')}else{this.value=this.value.replace(/\D/g,'')}"/>
					  </td>
					</tr>
					<tr>
					<td></td>
					<td></td>
					<td>
					  <input type="button" onclick="saveSupplier();" value="提交"/>
					</td>
					<td></td>
					<td></td>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</center>
	</div>
	<!-- 采样商品打分弹出层 -->
		<div class="mod_pay3" style="display: none;" id="supplierGoodsDiv">
		<div>
			<a href="javascript:void(0)" class="show_x" onclick="FncloseSupplierGoodsDiv()">╳</a>
		</div>
		<center>
			<h3 class="show_h3">采样商品打分</h3>
			<table id="supplierGoodsDivInfos" class="imagetable">
				<thead>
					<tr>
						<td rowspan="5"><span style="color:red">[质量]</span>:1:差   2:较差  3: 一般  4:无投诉  5: 优质</td>
					</tr>
					<%--<tr>--%>
						<%--<td rowspan="5"><span style="color:red">[服务]</span>:1:发货不及时  2:发货及时但是乱发  3:处理问题回复不及时  4:售后处理配合度一般  5:售后处理配合度好</td>--%>
					<%--</tr>--%>
					<tr>
					  <td>产品</td><td>质量</td><td>备注</td>
					</tr>
					<tr>
					  <td>
						<span id="su_goods_id"></span><br>
						<span id="su_goods_p_id"></span>
					  </td>
					  <td>
					    <select id="g_quality">
                          <option value="0">---请选择---</option>
                          <option value="1">1分</option>
                          <option value="2">2分</option>
                          <option value="3">3分</option>
                          <option value="4">4分</option>
                          <option value="5">5分</option>
                        </select>
					  </td>
					  <%--<td>--%>
					    <%--<select id="g_service">--%>
                          <%--<option value="0">---请选择---</option>--%>
                          <%--<option value="1">1分</option>--%>
                          <%--<option value="2">2分</option>--%>
                          <%--<option value="3">3分</option>--%>
                          <%--<option value="4">4分</option>--%>
                          <%--<option value="5">5分</option>--%>
                        <%--</select>--%>
					  <%--</td>--%>
					  <td>
					   <textarea rows="10" cols="15" id="su_g_remark"></textarea>
					  </td>
					</tr>
					<tr>
					<td></td>
					<td></td>
					<td>
					  <input type="button" onclick="saveGoodsSupplier();" value="提交"/>
					</td>
					<td></td>
					<td></td>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</center>
	</div>
	<!-- 质检-->
	<div id="div_clothing" style="display: none;">
		<form id="div_clothing_from">
			<table border="1">
				<tr>
					<td colspan="5">
						策融电商仓库服装检查checklist V1.2
					</td>
				</tr>
				<tr>
					<td style="text-align:center">客户ID/订单号：</td>
					<td style="text-align:center" colspan="2"><span id="clothing_orderid"></span><input type="hidden" id="c_catid"></td>
					<td style="text-align:center">商品ID：</td>
					<td style="text-align:center"><span id="clothing_goodsid"></span></td>
				</tr>
				<tr>
					<td style="text-align:center">检验范围</td>
					<td style="text-align:center">序号</td>
					<td style="text-align:center">检验内容</td>
					<td style="text-align:center" colspan="2">检验结果</td>
				</tr>
				<tr>
					<td style="text-align:center" rowspan="5">外观</td>
					<td style="text-align:center">1</td>
					<td style="text-align:center">外观完整</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_wg" value="1">完整<input type="radio" name="y_wg" value="0">破损</td>
				</tr>
				<tr>
					<td style="text-align:center">2</td>
					<td style="text-align:center">表面细节</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_bmxj" value="1">完好<input type="radio" name="y_bmxj" value="0">图案错误<input type="radio" name="y_bmxj" value="2">不对称</td>
				</tr>			<tr>
				<td style="text-align:center">3</td>
				<td style="text-align:center">外观细节</td>
				<td style="text-align:center" colspan="2"><input type="radio" name="y_wgxj" value="1">干净<input type="radio" name="y_wgxj" value="0">有污渍</td>
			</tr>			<tr>
				<td style="text-align:center">4</td>
				<td style="text-align:center">拉链</td>
				<td style="text-align:center" colspan="2"><input type="radio" name="y_ll" value="1">顺畅<input type="radio" name="y_ll" value="0">卡顿</td>
			</tr>
				<tr>
					<td style="text-align:center">5</td>
					<td style="text-align:center">线头</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_xt" value="1">无线头<input type="radio" name="y_xt" value="0">少量线头<input type="radio" name="y_xt" value="2">过多线头</td>
				</tr>
				<tr>
					<td style="text-align:center">质感</td>
					<td style="text-align:center">6</td>
					<td style="text-align:center">材质与描述</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_zg" value="1">一致<input type="radio" name="y_zg" value="0">不确定<input type="radio" name="y_zg" value="2">有区别</td>
				</tr>
				<tr>
					<td style="text-align:center">*气味</td>
					<td style="text-align:center">7</td>
					<td style="text-align:center">衣服气味</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_qw" value="1">无异味<input type="radio" name="y_qw" value="0">有异味</td>
				</tr>
				<tr>
					<td style="text-align:center">包装</td>
					<td style="text-align:center">8</td>
					<td style="text-align:center">包装</td>
					<td style="text-align:center" colspan="2"><input type="radio" name="y_bz" value="1">完好<input type="radio" name="y_bz" value="0">需更换</td>
				</tr>
				<tr>
					<td style="text-align:center">尺码表</td>
					<td style="text-align:center" colspan="2">要求尺寸</td>
					<td style="text-align:center" colspan="2">实测尺寸</td>
				</tr>
				<tr>
					<td style="text-align:center" rowspan="6">上衣/裙子</td>
					<td style="text-align:center">肩宽shoulder</td>
					<td style="text-align:center"><input type="text" id="jks_value"></td>
					<td style="text-align:center">肩宽</td>
					<td style="text-align:center"><input type="text" id="jk_remark"></td>
				</tr>
				<tr>
					<td style="text-align:center">胸围bust</td>
					<td style="text-align:center"><input type="text" id="xwb_value"></td>
					<td style="text-align:center">胸围</td>
					<td style="text-align:center"><input type="text" id="xw_remark"></td>
				</tr>
				<tr>
					<td style="text-align:center">腰围waist</td>
					<td style="text-align:center"><input type="text" id="yww_value"></td>
					<td style="text-align:center">腰围</td>
					<td style="text-align:center"><input type="text" id="yw_remark"></td>
				</tr>
				<tr>
					<td style="text-align:center">臀围hip</td>
					<td style="text-align:center"><input type="text" id="twh_value"></td>
					<td style="text-align:center">臀围</td>
					<td style="text-align:center"><input type="text" id="tw_remark"></td>
				</tr>
				<tr>
					<td style="text-align:center">袖长sleeve</td>
					<td style="text-align:center"><input type="text" id="xcs_value"></td>
					<td style="text-align:center">袖长</td>
					<td style="text-align:center"><input type="text" id="xc_remark"></td>
				</tr>
				<tr>
					<td style="text-align:center">衣长length</td>
					<td style="text-align:center"><input type="text" id="yzl_value"></td>
					<td style="text-align:center">衣长</td>
					<td style="text-align:center"><input type="text" id="yc_remark"></td>
				</tr>
				<tr>
					<td colspan="5"><input type="button" onclick="closeClothingData();" value="关闭"></td>
				</tr>
			</table>
		</form>
	</div>
	<!-- 首饰检查checklist-->
	<div style="display: none;" id="ss_div">
		<form>
			<table border="1">
				<tr>
					<td colspan="4">
						策融电商仓库首饰检查checklist V1.2
					</td>
				</tr>
				<tr>
					<td style="text-align:center" colspan="2"><span>订单号：</span></td>
					<td style="text-align:center"><span id="ss_orderid"></span><input type="hidden" id="ss_catid"></td>
					<td style="text-align:center"><span>商品ID：</span><span id="ss_goodsid"></span></td>
				</tr>
				<tr>
					<td style="width:80px;text-align:center">检验范围</td>
					<td style="width:50px;text-align:center">序号</td>
					<td style="width:200px;text-align:center">检验内容</td>
					<td style="width:200px;text-align:center">检验结果</td>
				</tr>
				<tr>
					<td rowspan="7" style="text-align:center">
						<span>外观</span>
					</td>
					<td style="text-align:center">1</td>
					<td style="text-align:center">外观完整</td>
					<td style="text-align:center"><input type="radio" name="s_wz" value="1">完整<input type="radio" name="s_wz" value="0">破损<input type="radio" name="s_wz" value="2">掉钻</td>
				</tr>
				<tr>
					<td style="text-align:center">2</td>
					<td style="text-align:center">款式 </td>
					<td style="text-align:center"><input type="radio" name="s_ks" value="1">正确<input type="radio" name="s_ks" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">3</td>
					<td style="text-align:center">颜色</td>
					<td style="text-align:center"><input type="radio" name="s_ys" value="1">正确<input type="radio" name="s_ys" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">4</td>
					<td style="text-align:center">表面细节</td>
					<td style="text-align:center"><input type="radio" name="s_bm" value="1">完好<input type="radio" name="s_bm" value="0">松动<input type="radio" name="s_bm" value="2">电镀层脱落</td>
				</tr>
				<tr>
					<td style="text-align:center">5</td>
					<td style="text-align:center">外观细节</td>
					<td style="text-align:center"><input type="radio" name="s_wgxj" value="1">干净<input type="radio" name="s_wgxj" value="0">有锈斑</td>
				</tr>
				<tr>
					<td style="text-align:center">6</td>
					<td style="text-align:center">链扣</td>
					<td style="text-align:center"><input type="radio" name="s_lk" value="1">正常<input type="radio" name="s_lk" value="0">松动</td>
				</tr>
				<tr>
					<td style="text-align:center">7</td>
					<td style="text-align:center">掉色</td>
					<td style="text-align:center"><input type="radio" name="s_ds" value="1">无掉色<input type="radio" name="s_ds" value="0">有掉色</td>
				</tr>
				<tr>
					<td style="text-align:center">质感</td>
					<td style="text-align:center">8</td>
					<td style="text-align:center">材质与描述</td>
					<td style="text-align:center"><input type="radio" name="s_cz" value="1">一致<input type="radio" name="s_cz" value="0">有区别</td>
				</tr>
				<tr>
					<td style="text-align:center">包装</td>
					<td style="text-align:center">9</td>
					<td style="text-align:center">包装</td>
					<td style="text-align:center"><input type="radio" name="s_bz" value="1">有气泡膜<input type="radio" name="s_bz" value="0">需更换</td>
				</tr>
				<tr>
					<td style="text-align:center" colspan="4"><input type="button" onclick="closeJewelryData();" value="关闭"></td>
				</tr>
			</table>
		</form>
	</div>
	<!--电子产品检查checklist -->
	<div id="dz_div" style="display: none;">
		<form>
			<table border="1">
				<tr>
					<td colspan="4" style="text-align:center">
						策融电商仓库电子产品检查checklist V1.2
					</td>
				</tr>
				<tr>
					<td style="text-align:center" colspan="2"><span>订单号：</span></td>
					<td style="text-align:center"><span id="dd_orderid"></span><input type="hidden" id="dd_catid"></td>
					<td style="text-align:center"><span>商品ID：</span><span id="dd_goodsid"></span></td>
				</tr>
				<tr>
					<td style="width:80px;text-align:center">检验范围</td>
					<td style="width:50px;text-align:center">序号</td>
					<td style="width:200px;text-align:center">检验内容</td>
					<td style="width:200px;text-align:center">检验结果</td>
				</tr>
				<tr>
					<td rowspan="7" style="text-align:center">外观</td>
					<td style="text-align:center">1</td>
					<td style="text-align:center">通电测试</td>
					<td style="text-align:center"><input type="radio" name="d_wg" value="1">正常<input type="radio" name="d_wg" value="0">无反应</td>
				</tr>
				<tr>
					<td style="text-align:center">2</td>
					<td style="text-align:center">款式</td>
					<td style="text-align:center"><input type="radio" name="d_ks" value="1">正确<input type="radio" name="d_ks" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">3</td>
					<td style="text-align:center">颜色</td>
					<td style="text-align:center"><input type="radio" name="d_ys" value="1">正确<input type="radio" name="d_ys" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">4</td>
					<td style="text-align:center">完整</td>
					<td style="text-align:center"><input type="radio" name="d_wz" value="1">完好<input type="radio" name="d_wz" value="0">套件有缺失<input type="radio" name="d_wz" value="2">无说明书</td>
				</tr>
				<tr>
					<td style="text-align:center">5</td>
					<td style="text-align:center">外观细节</td>
					<td style="text-align:center"><input type="radio" name="d_wg1" value="1">干净<input type="radio" name="d_wg1" value="0">生锈<input type="radio" name="d_wg1" value="2">起泡<input type="radio" name="d_wg1" value="3">褪色</td>
				</tr>
				<tr>
					<td style="text-align:center">6</td>
					<td style="text-align:center">外观细节2</td>
					<td style="text-align:center"><input type="radio" name="d_wg2" value="1">正常<input type="radio" name="d_wg2" value="0">缺角<input type="radio" name="d_wg2" value="2">碎屏</td>
				</tr>
				<tr>
					<td style="text-align:center">7</td>
					<td style="text-align:center">掉色</td>
					<td style="text-align:center"><input type="radio" name="d_ds" value="1">无掉色<input type="radio" name="d_ds" value="0">有掉色</td>
				</tr>
				<tr>
					<td style="text-align:center" rowspan="5">规格</td>
					<td style="text-align:center">8</td>
					<td style="text-align:center">内存</td>
					<td style="text-align:center"><input type="radio" name="d_nc" value="1">正确<input type="radio" name="d_nc" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">9</td>
					<td style="text-align:center">电池容量</td>
					<td style="text-align:center"><input type="radio" name="d_rl" value="1">正确<input type="radio" name="d_rl" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">10</td>
					<td style="text-align:center">屏幕大小</td>
					<td style="text-align:center"><input type="radio" name="d_pm" value="1">正确<input type="radio" name="d_pm" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">11</td>
					<td style="text-align:center">分辨率</td>
					<td style="text-align:center"><input type="radio" name="d_fbv" value="1">正确<input type="radio" name="d_fbv" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center">12</td>
					<td style="text-align:center">像素</td>
					<td style="text-align:center"><input type="radio" name="d_xs" value="1">正确<input type="radio" name="d_xs" value="0">错误</td>
				</tr>
				<tr>
					<td style="text-align:center" rowspan="2">内部细节</td>
					<td style="text-align:center">13</td>
					<td style="text-align:center">插头</td>
					<td style="text-align:center"><input type="radio" name="d_ct" value="1">正确<input type="radio" name="d_ct" value="0">错误</td>
				</tr>

				<tr>
					<td style="text-align:center">14</td>
					<td style="text-align:center">说明书</td>
					<td style="text-align:center"><input type="radio" name="d_sms" value="1">有中文<input type="radio" name="d_sms" value="0">有英文<input type="radio" name="d_sms" value="2">无说明书</td>
				</tr>
				<tr>
					<td style="text-align:center">包装</td>
					<td style="text-align:center">15</td>
					<td style="text-align:center">包装</td>
					<td style="text-align:center"><input type="radio" name="d_bz" value="1">有气泡膜<input type="radio" name="d_bz" value="0">需更换</td>
				</tr>
				<tr>
					<td style="text-align:center" colspan="4"><input type="button" onclick="closeClothingDivdd();" value="关闭"></td>
				</tr>
			</table>
		</form>
	</div>
		<!-- 评论框 start yyl-->
		<div class="mod_pay3" style="display: none;" id="commentDiv1">
		<div>
				<a href="javascript:void(0)" class="show_x"
					onclick="$('#commentDiv1').hide();" style="float: right;">╳</a>
			</div>
			 <input id="cm_id" type="hidden"  value="">
			<input id="cm_adminname" type="hidden" value="">
		    <input id="cm_orderNo" type="hidden" value="">
		    <input id="cm_goodsSource" type="hidden" value="">
		    <input id="cm_goodsPid" type="hidden" value="">
		    <input id="cm_adminId" type="hidden" value="">
		    <input type="hidden" id="cm_country" value="">
		    <input type="hidden" id="cm_oid" value="">
		    <input type="hidden" id="cm_carType" value="">
		    评论内容:
		    <textarea name="comment_content" rows="8" cols="50" style="margin-top: 20px;" id="comment_content_"></textarea>
		    <input type="button" id="commentBtnId2" onclick="saveCommentContent()" value="提交评论">
		</div>
		<!-- 评论end -->
	<div class="ormacon">
		<h3 class="ordmatit">订单详情管理</h3>
		<div>
			<input type="hidden" value="${order.pay_price_tow}"
				id="pay_price_tow"> <input type="hidden"
				value="${order.expect_arrive_time}" id="expect_arrive_time">
			<input type="hidden" value="${order.state}" id="order_state">
			<input type="hidden" value="${order.userName}" id="order_name">
			<input type="hidden" value="${payToTime}" id="payToTime">
			<table class="ormatable" id="orderInfo" cellpadding="1"
				cellspacing="1" class="orderInfo" align="center">
				<tr class="ormatrname">
					<td width="20%" class="ornmatd1">订单号:<input class="ormnum"
						type="text" name="orderNo" id="orderNo" readonly="readonly"
						value="${order.orderNo}" />
					</td>
					<td width="30%" class="ornmatd1"><em id="state_text">
							${order.state==-1 || order.state==6?'取消订单':'' }${order.state==1?'购买中':'' }
							<c:choose>
								<c:when test="${order.state==2 && order.checked==order.countOd}">
									已到仓库,验货无误
								</c:when>
								<c:when test="${order.state==2 && order.no_checked>0}">
									已到仓库，未校验
								</c:when>
								<c:when test="${order.state==2 && order.problem!=0}">
									已到仓库，校验有问题
								</c:when>
								<c:when test="${order.state==2}">
									已到仓库，状态错误
								</c:when>
							</c:choose>
							<%--${order.state==2 && Number(order.checked)==Number(order.countOd)?'已到仓库,验货无误':''}--%>
							${order.state==0?'等待付款':'' }${order.state==3?'出运中':'' }
							${order.state==4?'完结':'' }
							${order.state==5?'确认价格中':'' }${order.state==7?'预订单':'' }

						<%-- 	${order.state==-1?'订单已取消':'' }${order.state==1?'购买中':'' }${order.state==2?'已到仓库':'' }${order.state==0?'等待付款':'' }${order.state==3?'出运中':'' }${order.state==4?'完结':'' }${order.state==5?'确认价格中':'' }${order.state==7?'预订单':'' } --%>

					</em></td>
					<td width="50%" colspan="3">
					<c:if test="${fn:length(orderNos) > 0}">
						<span class="ornmatd1">关联订单:</span>
						<div class="ormrelanum">
							<c:forEach items="${orderNos}" var="order_correlation">
								<a target="_blank" href="/cbtconsole/orderDetails/queryByOrderNo.do?&orderNo=${order_correlation}&state=${order.state}&username=${order.userName}">${order_correlation}</a>&nbsp;
									
 							</c:forEach>
						</div>
					</c:if>
					</td>
					<td>
						<a href="/cbtconsole/customerRelationshipManagement/reorder?orderNo=${order.orderNo}"><input type="button" value="AddOrderToTest" style="color: red"></a>
					</td>
				</tr>
				<!-- 客户订单信息显示 -->
				<tr>
					<td class="ormtittd">
						<span>name:</span>${order.userName}(ID:<em id="userId">${order.userid}</em>)<br><a class="ordmlink" target="_blank" href="/cbtconsole/userinfo/getUserInfo.do?userId=${order.userid}">客户页面</a>
						<c:if test="${not empty order.backFlag}">
							<span style="color:red">${order.backFlag}</span>
						</c:if>
						<br>
						<span style="color: red; display: none" id="other_id"></span>
					</td>
					<td>
						<span class="ormtittd" style="margin-left:200px">Email:</span><a href="mailto:${order.userEmail}">${order.userEmail}</a><em></em>
					</td>
					<td>
						<span class="ormtittd" style="margin-left:100px">客户公司名称:</span><em>${order.businessName}</em>
					</td>
					<td>
						<span class="ormtittd">国内交期：</span>${order.deliveryTime}
					</td>
				</tr>
				<c:if test="${order.state != 0}">
					<tr>
						<td>
							<span class="ormtittd">付款时间：</span> 
							<c:forEach
								items="${pays}" var="pay">
								<c:if test="${pay.orderid == order.orderNo}">
									${pay.createtime} 
								</c:if>
							</c:forEach>
						</td>
						<td>
							<span class="ormtittd"  style="margin-left:200px">到账确认：</span><em id="dzConfirmtime">${order.dzConfirmtime}</em>
						</td>
						<td>
							<span class="ormtittd" style="margin-left:100px">付款金额：</span>${order.pay_price}<em id="currency">${order.currency}</em>
						</td>
						<td>
							<span class="ormtittd">还需付款：</span>${order.remaining_price}${order.currency}
						</td>
					</tr>
				</c:if>
				<tr>
					<td>
						<span class="ormtittd">国际运输段：</span>
						<c:if test="${not empty order.mode_transport}">
							${fn:indexOf(order.mode_transport, "@") > 1 ? fn:split(order.mode_transport,'@')[1]:""}[
							${fn:indexOf(order.mode_transport, "@") > 1 ? fn:split(order.mode_transport,'@')[0]:""}]
						</c:if>
					</td>
					<td class="ormtittd"  >
						<span class="ormtittd"  style="margin-left:100px">VIP级别：</span><span style="color:red;">${order.grade}</span>
					</td>
					<td class="ormtittd">
						<span class="ormtittd"  style="margin-left:100px">订单重量(kg)：</span><span><span  style="color:red;">${order.volumeweight}</span>（免邮产品重量(kg)：<span  style="color:red;">${feeWeight}）</span></span>
					</td>
					<td class="ormtittd" >
						<%--<span class="ormtittd"  style="margin-left:100px">订单体积：</span><span style="color:red;">${order.svolume}</span>--%>
					</td>
				</tr>
				<tr>
				  <c:if test="${order.state == 3 || order.state == 4 || order.state == 1 || order.state == 5 || order.state == 2}">
				  	<td class="ormtittd1" colspan="3">
				  		商品总金额 <span class="ormtittdred">（<fmt:formatNumber value="${order.product_cost+preferential_price}" pattern="#0.00" type="number" maxFractionDigits="2" />）
						</span> + 订单实收运费<span class="ormtittdred">（${actual_ffreight_+order.foreign_freight}）</span>
							+ 服务费 <span class="ormtittdred">（${order.service_fee}）</span>
						+ 手续费 <span class="ormtittdred">（${order.processingfee}）</span>
						+ 质检费 <span class="ormtittdred">（${order.actual_lwh}）</span>
						+ 会员费 <span class="ormtittdred">（${order.memberFee}）</span>
						+额外运费金额<span class="ormtittdred">(${order.extra_freight})</span>
							-  <c:if test="${order.order_ac != 0}">
							批量优惠金额<span class="ormtittdred">（${preferential_price}） </span>
							</c:if> -混批优惠金额<span class="ormtittdred">（${order.discount_amount}）</span>
							-订单满200减免<span class="ormtittdred">（ ${order.cashback}） </span>
							-手动优惠<span class="ormtittdred">（ ${order.extra_discount}）
						</span> -分享折扣<span class="ormtittdred">（ ${order.share_discount}）
						</span>  -新用户赠送运费<span class="ormtittdred">(0)
						<%--（${order.order_ac}） --%>
					</span> -返单优惠<span class="ormtittdred">（
								${order.coupon_discount}） </span> -${order.gradeName}<span class="ormtittdred">（
								${order.gradeDiscount}） </span> 
								+双清包税金额<span class="ormtittdred">（${order.vatBalance}） </span>
								-首单优惠<span class="ormtittdred">（${order.firstdiscount}） </span>
								+$50国际费用<span class="ormtittdred">（${order.actual_freight_c}） </span>
								=<b>实收金额</b><span
							class="ormtittdred ormtittdb"> （<fmt:formatNumber
							value="${(order.product_cost+actual_ffreight_+order.foreign_freight+order.processingfee+order.actual_lwh+order.memberFee+order.extra_freight-order.discount_amount+order.service_fee-order.cashback-order.share_discount-order.extra_discount-order.coupon_discount-order.order_ac + order.vatBalance-order.firstdiscount+order.actual_freight_c) >0 ?
									(order.product_cost+actual_ffreight_+order.foreign_freight+order.processingfee+order.extra_freight+order.actual_lwh+order.memberFee-order.discount_amount+order.service_fee-order.cashback-order.share_discount-order.extra_discount-order.coupon_discount-0-order.gradeDiscount + order.vatBalance - order.firstdiscount + order.actual_freight_c) : 0.00 }"
									pattern="#0.00" type="number" maxFractionDigits="2" />）
						</span>
						 <a href="/cbtconsole/orderDetails/orderPayDetails.do?orderNo=${order.orderNo}&userId=${order.userid}" target="_blank" style="text-decoration: none;">【到账详情 】</a>
					</td>
				  </c:if>
				   <c:if test="${order.state == -1 || order.state == 6 || order.state == 0}">
				   	<td class="ormtittd1" colspan="4">
					  <a href="/cbtconsole/orderDetails/orderPayDetails.do?orderNo=${order.orderNo}&userId=${order.userid}" target="_blank" style="text-decoration: none;">【到账详情 】</a>
					</td>
				   </c:if>
				</tr>
				<tr>
					<td colspan="4">${order.paytypes}</td>
				</tr>
				<tr>
					<td  style="margin-left:200px">
						<c:if test="${order.state == 3 || order.state == 4 || order.state == 1 || order.state == 2}">
							<a class="ordmlink" target="_blank" href="/cbtconsole/purchase/queryPurchaseInfo?pagenum=1&orderid=0&admid=${order.buyid}&userid=&orderno=${order.orderNo}&goodid=&date=&days=&state=&unpaid=0&pagesize=50&search_state=0">采购页面</a>&nbsp;&nbsp;
	 					</c:if> 
						<c:if test="${order.state == 3 || order.state == 4 }">
							<a class="ordmlink" target="_blank" href="/cbtconsole/website/forwarderpageplck.jsp?orderid=${order.orderNo}&flag=true">出货页面</a>&nbsp;&nbsp;
	 					</c:if> 
	 					<br />
	 					<c:if test="${order.state !=0 && order.state !=-1 && order.state !=6 }">
							<a class="ordmlink" style="text-decoration: underline;cursor: pointer;" 
								onclick="jumpTracking('${order.orderNo}', '${order.dropShipList}')">Tracking</a>&nbsp;&nbsp;
	 					</c:if> 
	 					<c:if test="${order.state !=6 }">
							<a class="ordmlink" style="text-decoration: underline;cursor: pointer;" 
								onclick="jumpDetails('${order.orderNo}', '${order.dropShipList}')">客户的订单页</a>&nbsp;&nbsp;
	 					</c:if> 
					</td>
					<td  style="margin-left:100px">
						<c:if test="${evaluate.evaluate != null && evaluate.evaluate !=''}">
								<span>用户评价：<span style="color:red;font-size:20px">${evaluate.evaluate}</span></span>
						</c:if> 
					</td>
					<td  colspan="2">
						 <c:if test="${order.orderRemark != null && order.orderRemark!=''}">
						 	<span>用户订单备注：<span style="color:red;font-size:20px">${order.orderRemark}</span></span>
						 </c:if>
					</td>
				</tr>
				<tr>
					<td colspan="4">
						<input type="hidden" value="${isDropshipOrder}">
						<%--<c:if test="${isDropshipOrder1==1}">--%>
							<%--<input type="button" style="position: fixed; bottom: 552px; right: 50px; width: 150px; height: 30px;" id="notifycustomer" name="button" value="确认并告知客户"--%>
							<%--${order.state==5||order.state==1?'':'disabled=disabled' }--%>
							<%--onclick="sendCutomers('${fn:length(orderDetail) > 0 ? orderDetail[0].orderid:''}',1,1)">--%>
						<%--</c:if> --%>
						<%--<c:if test="${isDropshipOrder1!=1}">--%>
							<%--<input type="button" style="position: fixed; bottom: 552px; right: 50px; width: 150px; height: 30px;" id="notifycustomer" name="button" value="确认并告知客户"--%>
							<%--${order.state==5||order.state==1?'':'disabled=disabled' }--%>
							<%--onclick="sendCutomers('${fn:length(orderDetail) > 0 ? orderDetail[0].orderid:''}',1,0)">--%>
						<%--</c:if> --%>
						<label style="display: none; color: red; position: fixed; bottom: 610px; right: 70px; width: 150px; height: 30px;" id="msg">(通知成功)</label>
						&nbsp;&nbsp; &nbsp;&nbsp;
						<c:if test="${order.state!=3 || order.state!=4 }">
							<input type="button" style="position: fixed; bottom: 520px; right: 50px; width: 150px; height: 30px;" id="closeOrder"
							onclick="fnCloseOrder('${order.orderNo}',${order.userid},${order.pay_price},
							'${order.currency}',${order.order_ac},'${order.userEmail}','${order.email}',
							${order.product_cost+preferential_price},${actual_ffreight_+order.foreign_freight},
							${order.actual_weight_estimate},${isDropshipOrder })"value="取消订单">&nbsp;&nbsp;
						</c:if> 
						<c:if test="${count==1 }">
							<input type="button" style="position: fixed; bottom: 400px; right: 50px; width: 150px; height: 30px; border-color: red" onclick="fnChangeProduct('${order.orderNo}')" value="建议替换">
						</c:if> 
						&nbsp;&nbsp; 
						<c:if test="${isSplitOrder == 0 && order.state != 3 && order.state != 4}">
							<input type="button" style="position: fixed; bottom: 490px; right: 50px; width: 150px; height: 30px;" id="split_order_btn"
							onclick="fnSplitOrder('${order.orderNo}','${order.email}','${param.paytime}')"
							value="拆单">&nbsp;&nbsp;
						</c:if> 
						<c:if test="${isSplitOrder == 1}">
							<input type="button" style="position: fixed; bottom: 490px; right: 50px; width: 150px; height: 30px;" id="split_order_btn"
							onclick="fnSplitDropShipOrder('${order.orderNo}','${order.email}','${param.paytime}')"
							value="拆单">
							&nbsp;&nbsp;
						</c:if>
					</td>
				</tr>
				<tr>
					<td>
						<input type="button" style="position: fixed; bottom: 458px; right: 50px; width: 150px; height: 30px;" id="open" onclick="afterReplenishment()" value="售后补货"> 
					</td>
					<td colspan="3" style="display: none;" id="td_buyuser">
						<%--<span style="margin-left:300px;">分配此订单的采购人员：</span>--%>
						<%--<select id="buyuser1" name="buyuser1" style="width: 110px;" onchange="changeOrderBuyer('${order.orderNo}',this.value);"></select> --%>
						<%--<span id="buyuserinfo"></span> --%>
						<%--<input type="submit" value="确认" id="buy_but" onclick="fnchangebuy()">--%>
						<%--<span style="font-size: 15px; font-weight: bold; color: red;" id="buyresult"></span> --%>
						<span style="margin-left:400px;">分配此订单的销售人员：</span>
						<select id="saler" name="saler" style="width: 110px;"></select> 
						<input type="submit" value="确认" id="saler_but" onclick="addUser(${order.userid},'${order.userName}','${order.userEmail}')">
						<span style="font-size: 15px; font-weight: bold; color: red;" id="salerresult"></span>
					</td>
				</tr>
				<!-- 客户信息展示结束 -->
			</table>
			<!-- 采购情况-->
			<span style="background-color: red">采购情况汇总</span>
			<div style="background-color: aqua">
				商品总数:<span style="color:red">${order.countOd}</span>;采购总数:<span style="color:red">${order.cg}</span>;
				入库总数:<span style="color:red">${order.rk}</span>;验货无误总数:<span style="color:red">${order.checkeds}</span>;
				验货疑问总数:<span style="color:red">${order.yhCount}</span>
			</div>
			
			<div id="remarkdiv">
				<div class="ormamark"><table style="border-collapse:separate; border-spacing:5px;">
					<tbody>
					<tr>
						<td><input id="remarkbtn" type="button" value="添加备注内容(对内)" onclick="addremark('${order.orderNo}')"></td>
						<td rowspan="2">备注内容:</td><td rowspan="2"><textarea id="orderremark_" style="width:400px;height:50px;"></textarea></td>
						<td id="success" style="color:red"></td>
						<td></td></tr></tbody></table></div>
			</div>
			<div >
				<div style="margin-top: 10px; padding: 5px;"
					id="tab_forwarder">
					<table
						style="display: block; border-collapse: separate; border-spacing: 5px; border: 1px solid #00afff;width:1400px;">
						<tr>
							<td>快递跟踪号：</td>
							<%-- <td><input value="${forwarder.express_no}" id="express_no"
								name="express_no" type="text"></td> --%>
							<td><a href="/cbtconsole/website/forwarderpageplck.jsp?expressNo=${forwarder.express_no}" target="_blank">${forwarder.express_no}</a></td>
							<td>物流公司名称：</td>
							<%-- <td><input value="${forwarder.logistics_name}"
								id="logistics_name" name="logistics_name" type="text"
								readonly="readonly"></td> --%>
							<td><c:if test="${not empty forwarder.express_no}">${forwarder.logistics_name}</c:if></td>
								<%--<td>给客户看的最新状态：</td>--%>
							<%--<td><input value="${forwarder.new_state}" id="new_state"--%>
								<%--name="new_state" type="text"></td>--%>
						</tr>
					</table>
				</div>
			</div>
			<div id="orderaddressdiv" style="margin-top: 10px; width: 1380px; height: 280px; border: 1px solid #0000FF; padding: 10px; display: block; float: left;margin-left:6px">
				<div style="float: left;margin-left:150px" id="div_address">
					订单地址:&nbsp;&nbsp;<br>
					<table style="height: 150px;border-collapse:separate; border-spacing:0px 10px;">
						<tr>
							<td>Recipients:</td>
							<td><input id="orderrecipients" type="text" maxlength="50"
								value="${order.address.recipients}" style="width: 200px" disabled="disabled"></td>
						</tr>
						<tr>
							<td>Street:</td>
							<td>
								<input id="orderstreet" type="text" maxlength="50" value="${order.address.address}" style="width: 200px" disabled="disabled"><br>
								<input id="orderstreet2" maxlength="50" type="text" value="${order.address.street}" style="width: 200px;" disabled="disabled">
							</td>
						</tr>
						<tr>
							<td>City:</td>
							<td><input id="ordercity" type="text" maxlength="50"
								value="${order.address.address2}" style="width: 200px" disabled="disabled"></td>
						</tr>
						<tr>
							<td>State:</td>
							<td><input id="orderstate" type="text" maxlength="50"
								value="${order.address.statename}" style="width: 200px" disabled="disabled"></td>
						</tr>
						<tr>
							<td>Country:</td>
							<td>
								<select id="ordercountry" style="width: 180px" disabled="disabled" >
									<c:forEach items="${countryList }" var="zone">
										<option value="${zone.country}">${zone.country}</option>
									</c:forEach>

								</select>
							</td>
						</tr>
						<tr>
							<td>Zip Code:</td>
							<td><input id="orderzipcode" type="text" maxlength="10"
								value="${order.address.zip_code}" style="width: 200px" disabled="disabled"></td>
						</tr>
						<tr>
							<td>Phone:</td>
							<td><input id="orderphone" type="text" maxlength="18"
								value="${order.address.phone_number}" style="width: 200px" disabled="disabled"></td>
						</tr>
						<tr>
							<td></td>
							<td><input id="OrderAddress" type="button" value="修改订单地址"
								onclick="OrderAddress()"></td>
						</tr>
					</table>
				</div>
				<div style="float: left; margin-left: 20px;margin-left:200px;" id="paypal_div">
					付款地址:&nbsp;&nbsp;<br>
					<table style="height: 150px;border-collapse:separate; border-spacing:0px 10px;">
					  <tr>
					  <td></td>
					    <td><input id="address_name" type="text" value=""
								style="width: 200px;" disabled="disabled"></td>
					  </tr>
					  <tr>
					  <td></td>
					    <td><input id="address_street" type="text" value=""
								style="width: 200px" disabled="disabled">
						</td>
					  </tr>
					  <tr>
					  <td></td>
					    <td><input id="address_city" type="text" value=""
								style="width: 200px" disabled="disabled">
						</td>
					  </tr>
					  <tr>
					  <td></td>
					    <td><input id="address_state" type="text" value=""
								style="width: 200px" disabled="disabled">
						</td>
					  </tr>
					  <tr>
					  <td></td>
					    <td><input id="address_country_code" type="text" value=""
								style="width: 200px;" disabled="disabled"></td>
					  </tr>
					  <tr>
					  <td></td>
					    <td></td>
					  </tr>
					  <tr>
					  <td></td>
					    <td>
					    	<a id="receiveremail" href="#"></a>
					    </td>
					  </tr>
					  <tr>
					  <td></td>
					    <td>
					    </td>
					  </tr>
					</table>
				</div>
			</div>
		</div>
				
		<br>
		<div>
				<table border="1" style="margin-left:160px;">
					<tr>
						<td>客户实际支付金额(￥):</td>
						<td style="text-align:center;vertical-align:middle;"><span id="pay_price" style="color:red;">-</span></td>
						<td rowspan="10">
							运输公司:<span style="color:red;" id="transportcompany">-</span>;运输方式:<span style="color:red;" id="shippingtype">-</span><br>
							预估国际运费=（商品重量和-首重）/续重*续重价格+首重价格<br>
							录入采购金额=SUM(录入采购额)+产品国内运费<br>
							实际采购金额=暂无（待上线）<br>
							根据预估重量预估运费计算预估利润金额RMB（预估利润率%）=客户实际支付金额-实际预估采购金额-预估国际运费<br>
							根据实际重量预估的运费实际利润金额RMB（实际利润率%）= 客户实际支付金额-录入采购金额-实际重量预估运费<br>
							根据物流公司运费计算最终利润金额RMB（最终利润率%）= 客户实际支付金额-录入采购金额-最终录入的运费
						</td>
					</tr>
					<tr>
						<td>预计采购金额(￥):</td>
						<td style="text-align:center;vertical-align:middle;"><span id="es_price" style="color:red;">-</span>
                           <span style="color:red;" id="esPidAmount"></span>
							<c:if test="${not empty tipprice}">
								<span style="color:blue">(${tipprice})</span>
							</c:if>
						</td>
					</tr>
					<tr>
						<td>录入采购金额(￥):</td>
						<td style="text-align:center;vertical-align:middle;"><span id="buyAmount" style="color:red;">-</span></td>
					</tr>
					<tr>
						<td>实际采购金额(￥):</td>
						<td style="text-align:center;vertical-align:middle;"><span id="acBuyAmount" style="color:red;">-</span></td>
					</tr>
					<tr>
						<td>预计重量(购物车重量)(kg):</td>
						<td style="text-align:center;vertical-align:middle;"><span id="es_weight" style="color:red;">-</span></td>
					</tr>
					<tr>
						<td>实际重量(仓库称重)(kg):</td>
						<td style="text-align:center;vertical-align:middle;"><span id="ac_weight" style="color:red;">-</span></td>
					</tr>
					<tr>
						<td>预计国际运费（按照产品重量+客户所选运输方式）(￥):</td>
						<td style="text-align:center;vertical-align:middle;"><span id="es_freight" style="color:red;">-</span>(
							<c:if test="${not empty order.mode_transport}">
								${fn:indexOf(order.mode_transport, "@") > 1 ? fn:split(order.mode_transport,'@')[0]:""}
							</c:if>)
						</td>
					</tr>
					<tr>
						<td>实际重量预估运费(￥):</td>
						<td style="text-align:center;vertical-align:middle;"><span id="awes_freight" style="color:red;">-</span></td>
					</tr>
					<tr>
						<td>最终录入的运费（物流公司）(￥):</td>
						<td style="text-align:center;vertical-align:middle;"><span id="ac_freight" style="color:red;">-</span></td>
					</tr>
					<tr>
						<td>根据预估重量预估运费计算预估利润金额(￥)（预估利润率）:</td>
						<td style="text-align:center;vertical-align:middle;"><span id="es_profit" style="color:red;">-</span>(<span id="es_p" style="color:red;">-</span>)</td>
					</tr>
					<tr>
						<td>根据实际重量预估的运费实际利润金额(￥)（实际利润率）:</td>
						<td style="text-align:center;vertical-align:middle;width:300px"><span id="ac_profit" style="color:red;">-</span>(<span id="ac_p" style="color:red;">-</span>)</td>
					</tr>
					<tr>
						<td>根据物流公司运费计算最终利润金额(￥)（最终利润率）:</td>
						<td style="text-align:center;vertical-align:middle;width:300px"><span id="end_profit" style="color:red;">-</span>(<span id="end_p" style="color:red;">-</span>)</td>
					</tr>
				</table>
		</div>
		<div>
			<span class="d"></span><span id="sumFreight" style="color: green;"
				class="c"></span>
		</div>
		<!-- 		<div style="color: red;font-size:25px;" >Warning:直接退钱操作的是线上的数据，所以时间稍微长,请不要重复点击!</div> -->
		<div>
			<br />
			<c:if test="${invoice=='1'}">
				<input type="button" value="查看invoice"
					onclick="window.open('/cbtconsole/autoorder/show?orderid=${param.orderNo}','_blank')">
			</c:if>
			<c:if test="${invoice=='2'}">
				<input type="button" value="查看invoice"
					onclick="window.open('/cbtconsole/autoorder/shows?orderid=${param.orderNo}','_blank')">
			</c:if>
			<br />
		</div>
		<br>
<%-- 		<input type="hidden" value="${isDropshipOrder}" name="cjcTem"> --%>
		<c:if test="${isDropshipOrder == 0 || isDropshipOrder == 2 || isDropshipOrder == 3}">
			<!-- 不是dropship订单 -->
			<div style="width:1440px;">
				<table id="orderDetail" class="ormtable2" align="center">
					<tbody>
						<tr class="detfretit">
							<td>Item/购物车id</td>
							<td colspan="2">详情</td>
							<td style="width:400px;">订单信息</td>
							<!-- 							<td>交期(新)</td> -->
							<!-- 							<td width="100">备注</td> -->
							<td style="width: 300px;">状态</td>
							<td>沟通</td>
							<td>货源/沟通</td>
							<!-- 							<td>采购时间</td> -->
							<td>采购员</td>
							<td style="width:500px;">订单操作</td>
							<%--<td style="width:200px">售后补货</td>--%>
							<%--<td>消息备注</td>--%>
						</tr>
					</tbody>
<%-- 					<input type="hidden" value="${orderDetail}" name="cjcTem1"> --%>
					<c:forEach items="${orderDetail}" var="orderd" varStatus="sd">
						<tr id="goodsid_${orderd.goodsid}"
							style="${orderd.state == 2?'background-color: #FF8484':''}">
							<td>${sd.index+1}<br>${orderd.goodsid}/${orderd.id}</td>
							<td><input type="hidden" value="${orderd.state}"> <a>
									<img class="imgclass" onclick="fnRend('${orderd.goods_url}')"
									width="200px" height="200px;" src="/cbtconsole/img/wy/grey.gif"
									data-original=" ${fn:replace(orderd.goods_img,'50x50','') }"
									style="cursor: pointer;">
							</a>
							<c:choose>
								<c:when test="${orderd.match_url!=null}">
									<a href="${orderd.match_url}"
									style="width: 200px; display: block; word-wrap: break-word;"
									target="_blank">商品货源链接</a>
								</c:when>
								<c:otherwise>
									<a href="${orderd.oldUrl}"
									style="width: 200px; display: block; word-wrap: break-word;"
									target="_blank">商品货源链接</a>
								</c:otherwise>
							</c:choose>
							 </td>
							<td style='width: 150px;'><span style="color: red;">商品名称:</span><br>${orderd.state == 2? "<br>用户已取消":""}${orderd.goodsname}<br>
								<span style="color: red;">客户下单规格:</span><br> <span
								style="color: #00B1FF;display: inline-block;max-width: 250px;overflow: hidden;word-wrap: break-word;"> 
<%-- 									<c:if test="${not empty orderd.goods_type}"> --%>
<%-- 										<c:forEach items="${fn:split(orderd.goods_type,',')}" --%>
<%-- 											var="types" varStatus="i"> --%>
<%-- 										${fn:split(types,':')[1]}&nbsp; --%>
<%-- 										</c:forEach> --%>
											${orderd.goods_type}
<%-- 									</c:if> --%>
									</span><br> <span style="color: #8064A2; word-break: break-all;">${orderd.remark}</span>
								<c:if test="${not empty orderd.img_type}">
									<c:forEach items="${fn:split(orderd.img_type,'@')}"
										var="img_type" varStatus="i">
										<c:if test="${fn:indexOf(img_type,'http') > -1}">
											<img style="max-width: 60px;max-height: 60px;" src="${img_type}">&nbsp; 
										</c:if>
									</c:forEach>
								</c:if></td>
							<td style="widows: 400px;">
								<c:if test="${not empty orderd.inventoryRemark}">
									<span style="background-color:chartreuse">库存备注:${orderd.inventoryRemark}</span><br>
								</c:if>
								<span style="color: red;">数量:</span><em
								id="number_${orderd.goodsid}" style="font-weight: bold;">${orderd.yourorder}
									${orderd.goodsUnit}</em><br>
<%-- 									<c:if --%>
<%-- 									test="${not empty orderd.seilUnit }"> --%>
<%-- 									<span style="color: #FF4500; font-weight: bold;">${orderd.seilUnit}</span> --%>
<%-- 								</c:if>  --%>
								<em id="change_number_${sd.index}" style="color: red;"> <c:if
										test="${not empty orderd.change_number }">
										<br>${orderd.change_number}
									</c:if>
							</em><br>
								<span style="color:red">(
								<c:if test="${orderd.is_sold_flag !=0}">
									免邮商品
								</c:if>
								<c:if test="${orderd.is_sold_flag ==0}">
									非免邮商品
								</c:if>
									)</span>
								<span style="color: red">成交价格：</span>$
								${orderd.goodsprice} <em id="change_price_${sd.index}"
								style="color: red;"> <c:if
										test="${not empty orderd.change_price }">
										<br>${orderd.change_price}</c:if></em><br /> 
<!-- 										<input type="button" -->
<%-- 								onclick="showHistoryPrice('${orderd.goods_url}')" value="查看历史价格"></input> --%>
								<br>
									<c:if test="${orderd.bm_flag == 1}">
										    <span style="color: red">ali产品价格：</span>
											<span>$${orderd.ali_price}</span>
											<span><br /><a target="_blank" href="${orderd.alipid }">ali产品链接</a></span>
									</c:if>
										<br /> 
										<span
								style="color: red;">备注:</span> ${orderd.remark} <c:if
									test="${orderd.extra_freight != 0}">&nbsp;额外运费:${orderd.extra_freight}</c:if>
							</td>
							<td><input type="hidden"
								value="${orderd.state},${order.state},${orderd.orsstate},${orderd.od_state},${orderd.checked}">
								<c:set value="${orderd.state}" var="ostate"></c:set> <em> <c:if
										test="${ostate==0}">
											${order.state==-1?'取消订单':'' }${order.state==0?'等待付款':'' }${order.state==1?'购买中':'' }${order.state==3?'出运中':'' }
											<%-- ${order.state==4?'完结2':'' } --%>
											
											<c:if test="${order.state==4}">
															 <!-- yyl 评论start -->
															<c:if test="${admuserinfo.roletype==1 || admuserinfo.admName=='Ling' || admuserinfo.admName=='ling' }"> <button  onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">添加/修改评论</button> </c:if>
															 <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">查看所有评论
																<input type="hidden" id="goods_img${orderd.goods_pid }" value="${orderd.goods_img}"/>
																<input type="hidden" id="goods_url${orderd.goods_pid }" value="${orderd.goods_url }"/>
																<input type="hidden" id="goodsname${orderd.goods_pid }" value="${orderd.goodsname }"/>
																<input type="hidden" id="goodsprice${orderd.goods_pid }" value="${orderd.goodsprice }"/>
																<input type="hidden" id="orderDetailId${orderd.goods_pid }" value="${orderd.id }"/>
																<input type="hidden" id="carType${orderd.goods_pid }" value="${orderd.car_type }"/>
																</button>
														完结
														<font color="red">销售评论状态： <font name="${orderd.goods_pid }ID">未评论</font></font>
												</c:if>
																	
											
											
											${order.state==5?'确认价格中':'' }
										<c:if test="${orderd.orsstate==1 && order.state==1}">
											<br>
											<br>
											<font color="red">【已经确认货源】</font>
										</c:if>
										<c:if
											test="${orderd.orsstate==0 && order.state==1 && orderd.iscancel!=1 && orderd.od_state!=13}">
											<br>
											<br>
											<font color="red">【还未录入货源】</font>
										</c:if>
										<c:if test="${orderd.orsstate==12 && order.state==1}">
											<br>
											<br>
											<font color="red">【替换货源】</font>
										</c:if>
										<c:if test="${orderd.orsstate==12 && order.state==5}">
											<br>
											<br>
											<font color="red">【替换货源】</font>
										</c:if>
										<c:if test="${orderd.od_state==13 && order.state==1}">
											<br>
											<br>
											<font color="red">【客户已经同意替换】</font>
										</c:if>
										<c:if test="${orderd.od_state==13 && order.state==5}">
											<br>
											<br>
											<font color="red">【客户已经同意替换】</font>
										</c:if>
										<c:if
											test="${orderd.orsstate==5 && order.state==1 && orderd.iscancel!=1}">
											<br>
											<br>
											<font color="red">【问题货源】</font>
										</c:if>
										<c:if
											test="${orderd.orsstate==5 && order.state==5 && orderd.iscancel!=1}">
											<br>
											<br>
											<font color="red">【问题货源】</font>
										</c:if>
										<c:if
											test="${orderd.orsstate==6 && order.state==1 && orderd.iscancel!=1}">
											<br>
											<br>
											<font color="red">【已入库，但货有问题】</font>
										</c:if>
										<c:if
											test="${orderd.orsstate==6 && order.state==5 && orderd.iscancel!=1}">
											<br>
											<br>
											<font color="red">【已入库，但货有问题】</font>
										</c:if>
										<c:if test="${orderd.orsstate==1 && order.state==5}">
											<br>
											<br>
											<font color="red">【货源已确认】</font>
										</c:if>
										<c:if test="${orderd.orsstate==3 && order.state==5}">
											<br>
											<br>
											<font color="red">【订单确认价格中】</font>
										</c:if>
									</c:if> <%-- 									<c:if test="${ostate==1 }"> --%> <!-- 										产品买了并已经到我们仓库 -->
									<%-- 									</c:if>  --%> <input type="hidden"
									value="${ostate},${orderd.checked},${orderd.goodstatus}">
									<c:if test="${ostate==1 && orderd.checked==0}">  
										已到仓库
										<c:if test="${orderd.goodstatus==5}">
											<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,已校验数量不对</a>
										</c:if>
										<c:if test="${orderd.goodstatus==4}">
											<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,已校验有疑问</a>
										</c:if>
										<c:if test="${orderd.goodstatus==3}">
											<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,已到仓库，已校验有破损</a>
										</c:if>
										<c:if test="${orderd.goodstatus==1}">
											<font color="red">,未校验</font>
										</c:if>
										<c:if test="${orderd.goodstatus==2}">
											<a style="color: red;" target="_Blank" href="${ctx}/warehouse/getOrderinfoPage.do?goodid=${orderd.id}">,已校验该到没到</a>
										</c:if>
										<%--<c:if test="${orderd.goodstatus != 1}">--%>
											<%--<input type="button" style="color:royalblue" value="查看质检结果" onclick="openCheckResult('${order.orderNo}','${orderd.goodsid}')">--%>
										<%--</c:if>--%>
									</c:if> 
									<c:if test="${ostate==1 && orderd.checked==1 && orderd.goodstatus==1}">
									 <!-- yyl 评论start -->
										<c:if test="${admuserinfo.roletype==1 || admuserinfo.admName=='Ling' || admuserinfo.admName=='ling' }"> <button  onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">添加/修改评论</button> </c:if>
										 <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">查看所有评论
											<input type="hidden" id="goods_img${orderd.goods_pid }" value="${orderd.goods_img}"/>
											<input type="hidden" id="goods_url${orderd.goods_pid }" value="${orderd.goods_url }"/>
											<input type="hidden" id="goodsname${orderd.goods_pid }" value="${orderd.goodsname }"/>
											<input type="hidden" id="goodsprice${orderd.goods_pid }" value="${orderd.goodsprice }"/>
											<input type="hidden" id="orderDetailId${orderd.goods_pid }" value="${orderd.id }"/>
											<input type="hidden" id="carType${orderd.goods_pid }" value="${orderd.car_type }"/>
											</button>
										</br>已到仓库<!-- '${orderd.goods_img}','${orderd.goods_url }','${orderd.goodsname }','${orderd.goodsprice }',${orderd.goodsid }' -->
										<font color="green">,验货无误</font>
										<%--<input type="button" style="color:royalblue" value="查看质检结果" onclick="openCheckResult('${order.orderNo}','${orderd.goodsid}')">--%>
											 </br>
									 <%--  <c:set var="mygoodsid1" value="${orderd.goods_pid }${'ID'}"></c:set> --%>
	 								<font color="red">
	 								销售评论状态： <font name="${orderd.goods_pid }ID">未评论</font>
									<!-- yyl 评论end -->
									</c:if>
									<c:if test="${ostate==1 && orderd.checked==1 && orderd.goodstatus !=1}">
									  	 <font color="red" style="font-size:20px;">已到仓库，验货状态错误，请联系管理员！！</font>
									</c:if>
							</em>	
									
								
									
									
							 <input type="hidden" name="ostate" value="${ostate}"> <em
								id="change_cancel_${sd.index}">${orderd.iscancel==1?'<br>系统取消':''}</em>
								<em id="user_cancel_${sd.index}">${orderd.state == 2? "<br>用户已取消":""}</em>
								</br>运单号:<em>${orderd.shipno}</em> <br>最近国内物流状态:<em>${orderd.shipstatus}</em>
							</td>
							<td style="width: 200px;"><c:if test="${orderd.state!=2 }">
									<button
										onclick="pricechange('${orderd.orderid}',${orderd.goodsid},${orderd.goodsprice}+'',${sd.index},${isDropshipOrder})"
										${order.state==5||order.state==1?'':'disabled=disabled' }>价格更新</button>
									<button
										onclick="deliverychange('${orderd.orderid}',${orderd.goodsid},${orderd.delivery_time}+'',${sd.index},${isDropshipOrder})"
										${order.state==5||order.state==1?'':'disabled=disabled' }>交期偏长</button>
									<button
										onclick="rationchange('${orderd.orderid}',${orderd.goodsid},${orderd.yourorder}+'',${sd.index},${isDropshipOrder})"
										${order.state==5||order.state==1?'':'disabled=disabled' }>订量偏低</button>
									<br />
									<!--后台取消商品前台客户确认  start 5.9 屏蔽掉-->
									<div class="shield">
										<button
											onclick="cancelchange('${orderd.orderid}',${orderd.goodsid},${sd.index})"
											${order.state==5||order.state==1?'':'disabled=disabled' }>取消(客)</button>
										<!--后台取消商品前台客户确认  end 5.9 屏蔽掉-->
										<!--直接取消商品不需要客户确认，直接给客户退钱 4.7 start 4.27 屏蔽掉-->
										<button id="deleteGoods${orderd.orderid}${orderd.goodsid}"
											onclick="deleteOrderGoods('${orderd.orderid}',${orderd.goodsid},${orderd.purchase_state},${order.userid })"
											${order.state==5||order.state==1?'':'disabled=disabled' }>退钱</button>
									</div>
									<!--直接取消商品不需要客户确认，直接给客户退钱 4.7 end-->
									<c:if test="${isDropshipOrder!=1}">
										<button
											onclick="communicatechange('${orderd.orderid}',${orderd.goodsid},${isDropshipOrder})"
											${order.state==5||order.state==1?'':'disabled=disabled' }>需沟通</button>
									</c:if>
									<c:if test="${isDropshipOrder==1}">
										<button
											onclick="communicatechange('${orderd.orderid}',${orderd.goodsid},${isDropshipOrder})"
											${order.state==5||order.state==1?'':'disabled=disabled' }>需沟通</button>
									</c:if>
								<c:if test="${orderd.shopFlag==1}">
									<input type="button" onclick="openSupplierGoodsDiv('${orderd.goods_pid}','${orderd.shop_id}');" value="商品打分" />
									<input type="button" onclick="openSupplierDiv('${orderd.shop_id}');" value="店铺打分" />
								</c:if>
									<h3 style="color: red;"
										id="t_delectGoods${orderd.orderid}${orderd.goodsid}"></h3>
									<h3 style="color: red;"
										id="f_delectGoods${orderd.orderid}${orderd.goodsid}"></h3>
									<input type="hidden" value="${isDropshipOrder1}" id="isDropshipOrder1">
									<input type="hidden" value="${isDropshipOrder}">
								</c:if>
								<!-- 需沟通消息记录 -->				
								<c:if test="${fn:length(orderd.change_communication)>0}">
									<hr>
									<c:if test="${orderd.ropType==5 && orderd.del_state==0 }">
										<button
											onclick="fnResolve('${order.orderNo}',${orderd.goodsid})">问题解决了</button>
										<label style="display: none; color: red;" id="msg2">(已解决)</label>
									</c:if>
									<div id="change_cmmunication_${orderd.goodsid}" style="overflow-y:scroll;height:200px;width:200px;">
										<c:forEach items="${orderd.change_communication}"
											var="change_communication">
											<span style='word-break: break-all; display: block;color:green'>${change_communication}</span>
											<hr>
										</c:forEach>
									</div>
								</c:if>
							</td>
							<td
								style="word-wrap: break-word; word-break: break-all; width: 240px; color: red">
								<font color="red" class="newsourceurl"> 
								
								
								<a href="/cbtconsole/editc/detalisEdit?pid=${orderd.goods_pid}" target="_blank">编辑链接</a>
								
								<span id="spanurl${sd.index}">
									<p>1688原始货源价格(RMB): ${orderd.price1688}</p>
								</span>
								<span id="spanurl${sd.index}">
									<p style="width:200px;">单件原始货源重量(kg): ${orderd.final_weight}</p>
								</span>
								<span id="spanurl${sd.index}">
									<p style="width:200px;">采购货源标题: ${orderd.goodsname}</p>
								</span>
								<span id="spanurl${sd.index}">
									<p style="width:200px;">合计加入购物车重量(kg): ${orderd.od_total_weight}</p>
								</span>
								<%--<span id="spanurl${sd.index}">--%>
									<%--<p style="width:200px;">产品总重量(kg): ${orderd.final_weight}</p>--%>
								<%--</span>--%>
								
								<span id="spanurl${sd.index}">
									<p>采购数量: ${orderd.buycount}</p>
								</span>
								<span id="spanurl${sd.index}">
									<p>供应商ID： <a target="_blank" style="color:red;" title="查看该供应商采购历史记录" href="/cbtconsole/website/shopBuyLog.jsp?shopId=${orderd.shop_id}">${orderd.shop_id}</a></p>
								</span>
								<span id="spanurl${sd.index}">
										<p>实际采购价格(RMB):${orderd.sourc_price}</p>
								<c:choose>
									  <c:when test="${orderd.newValue!=null}">
									  	<a href="${orderd.newValue}" target="_blank">实际采购货源链接</a>
									  </c:when>
									  <c:otherwise>
									  	<span>未录入货源链接</span>
									  </c:otherwise>
								</c:choose>
										<p>${orderd.oremark}</p> 
								</span>
							</font>
							
							</td>

							<td id="odid${orderd.id}">采购时间:${orderd.purchase_time}<br> <select
								id="buyer${orderd.id}"
								onchange="changeBuyer(${orderd.id},this.value);">
									<option value=""></option>
									<c:forEach var="aub" items="${aublist }">
										<option value="${aub.id }">${aub.admName}</option>
									</c:forEach>
							</select><span id="info${orderd.id}"></span>
							
							<!-- 消息备注列合并过来的-->
							<div style="overflow-y:scroll;height:200px;width:200px;">
								<div class="w-font">
									<font style="font-size: 15px;" id="rk_remark_${order.orderNo}${orderd.id}">${orderd.goods_info}</font>
								</div>
							</div>
							<div class="w-margin-top">
								<input type="button" value="回复" onclick="doReplay1('${order.orderNo}',${orderd.id});" class="repalyBtn" />
							</div>
								
							</td>
							<td style="word-break: break-all; width: 30px;"><input
								type="checkbox" style="zoom:140%;" onchange="fnChange(${orderd.id},this);"
								${orderd.state == 2?'checked="checked" disabled="true"':''}
								value="${orderd.id}"> <span>拆单、延后发货</span><input type="hidden"
								value="${orderd.state}">
								<br>
									<input type="checkbox" style="zoom:140%;" name="replenishment"
									onchange="fnChange(${orderd.id},this);"
									value="${orderd.goodsid}" /><span>补货，数量</span>
								<input type="text"
									   id="count_${orderd.goodsid}" style="width:50px;" value="补货数量"
									   onfocus="if (value =='补货数量'){value =''}"
									   onblur="if (value ==''){value='补货数量'}" />
							</td>
							<%--<td>--%>
								<%--<input type="text"--%>
								<%--id="count_${orderd.goodsid}" style="width:50px;" value="补货数量"--%>
								<%--onfocus="if (value =='补货数量'){value =''}"--%>
								<%--onblur="if (value ==''){value='补货数量'}" /><input type="checkbox" style="zoom:140%;" name="replenishment"--%>
								<%--onchange="fnChange(${orderd.id},this);"--%>
								<%--value="${orderd.goodsid}" />--%>
							<%--</td>--%>
						</tr>
					</c:forEach>
				</table>
			</div>
		</c:if>





		<!-- 是dropship订单 -->
		<c:if test="${isDropshipOrder==1}">
			<div id="dropshipOrderDiv">
				<c:forEach items="${orderDetail}" var="orderd" varStatus="sd">
					<div class="wrapper">
						<div class="tab_box">
							<c:set value="${orderd.dropshipid}" var="new_iorderid" />
							<c:if test="${new_iorderid != iorderid}">
								<div class="tab_top">
									<a href="#" class="toggle_btn">-</a> <strong>子订单号：</strong> <a
										target='_blank'
										href='/cbtconsole/orderDetails/queryChildrenOrderDetail.do?orderNo=
												${orderd.dropshipid}&state=${order.state}&username=${order.userEmail}&paytime=&isDropshipOrder=1'
										id="2">${orderd.dropshipid} </a> <strong>状态：</strong>
									<c:if test="${orderd.dropShipState!=0}">
												${orderd.dropShipState==-1?'后台取消订单':'' }${orderd.dropShipState==0?'等待付款':'' }${orderd.dropShipState==1?'购买中':'' }${orderd.dropShipState==3?'出运中':'' }
												<%-- ${orderd.dropShipState==4?'完结3':'' } --%>
												
												<c:if test="${order.dropShipState==4}">
															 <!-- yyl 评论start -->
															<c:if test="${admuserinfo.roletype==1 || admuserinfo.admName=='Ling' || admuserinfo.admName=='ling' }"> <button  onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">添加/修改评论</button> </c:if>
															 <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">查看所有评论
																<input type="hidden" id="goods_img${orderd.goods_pid }" value="${orderd.goods_img}"/>
																<input type="hidden" id="goods_url${orderd.goods_pid }" value="${orderd.goods_url }"/>
																<input type="hidden" id="goodsname${orderd.goods_pid }" value="${orderd.goodsname }"/>
																<input type="hidden" id="goodsprice${orderd.goods_pid }" value="${orderd.goodsprice }"/>
																<input type="hidden" id="orderDetailId${orderd.goods_pid }" value="${orderd.id }"/>
																<input type="hidden" id="carType${orderd.goods_pid }" value="${orderd.car_type }"/>
																</button>
														完结
														<br><font color="red">销售评论状态： <font name="${orderd.goods_pid }ID">未评论</font></font>
												</c:if>
												
												${orderd.dropShipState==5?'确认价格中':'' }${orderd.dropShipState==2?'已到仓库':'' }${orderd.dropShipState==6?'客户取消订单':'' }
									</c:if>
									<strong>商品销售金额：</strong>--

									<strong>采购金额：</strong>-- <strong>预估运费：</strong>--

								</div>
							</c:if>
							<table id="${orderd.dropshipid}" class="tab_data"
								style="display: block;">
								<c:if test="${new_iorderid != iorderid}">
									<tr class="detfretit">
										<td style="width: 5%;">Item</td>
										<td colspan="2" style="width: 20%;">详情</td>
										<td style="width: 10%;">订单信息</td>
										<!-- 										<td style="width: 3%;">客户公司名称</td> -->
										<td style="width: 3%;">状态</td>
										<!-- 										<td style="width: 5%;">交期(新)</td> -->
										<!-- 										<td style="width: 3%;">备注</td> -->
										<!-- 										<td style="width: 5%;">状态</td> -->
										<td style="width: 8%;">沟通</td>
										<td style="width: 10%;">货源/沟通</td>
										<td style="width: 5%;">采购时间</td>
										<td style="width: 2%;">采购员</td>
										<td style="text-align: center;">订单操作</td>
										<%--<td>消息备注</td>--%>
									</tr>
								</c:if>
								<c:set value="${orderd.dropshipid}" var="iorderid" />
								<tr id="goodsid_${orderd.goodsid}"
									style="${orderd.state == 2?'background-color: #FF8484':''}">
									<td style="width: 5%;">${sd.index+1}<br>${orderd.goodsid}
									</td>
									<td style="width: 5%;"><a> <img class="imgclass"
											onclick="fnRend('${orderd.goods_url}')" width="200px"
											height="200px;" src="/cbtconsole/img/wy/grey.gif"
											data-original=" ${fn:replace(orderd.goods_img,'50x50','') }"
											style="cursor: pointer;">
									</a></td>
									<td style="width: 8%;"><span style="color: red;">商品名称:<br></span>${orderd.state == 2? "<br>用户已取消":""}${orderd.goodsname}<br>
										<spna style="color:red;">客户下单规格:</spna><br> <span
										style="color: #00B1FF"> <c:if
												test="${not empty orderd.goods_type}">
<%-- 												<c:forEach items="${fn:split(orderd.goods_type,',')}" --%>
<%-- 													var="types" varStatus="i"> --%>
<%-- 																${fn:split(types,':')[1]}&nbsp; --%>
<%-- 															</c:forEach> --%>
											${orderd.goods_type }
											</c:if></span><br> <span
										style="color: #8064A2; word-break: break-all;">${orderd.remark}</span>
										<c:if test="${not empty orderd.img_type}">
											<c:forEach items="${fn:split(orderd.img_type,'@')}"
												var="img_type" varStatus="i">
												<c:if test="${fn:indexOf(img_type,'http') > -1}">
													<img style="max-width: 60px;max-height: 60px;" src="${img_type}">&nbsp; 
																</c:if>
											</c:forEach>
										</c:if></td>
									<td style="width: 5%;"><input type="hidden"
										id="${orderd.dropshipid}${pbsi.index}_s"
										value="${orderd.goodsprice}" /> <input type="hidden"
										id="${orderd.dropshipid}${pbsi.index}_sQuantity"
										value="${orderd.yourorder}" /> <input type="hidden"
										id="${orderd.dropshipid}${pbsi.index }_jg"
										value="${orderd.goodsprice}" /> <span style="color: red">数量：</span>
										<em id="number_${orderd.goodsid}" style="font-weight: bold;">${orderd.yourorder}</em>
										<em id="change_number_${sd.index}" style="color: red;"> <c:if
												test="${not empty orderd.change_number }">
												<br>
														${orderd.change_number}
													</c:if>
									</em><br> <span style="color: red;">客户成交价格:</span>${orderd.goodsprice}<em
										id="change_price_${sd.index}" style="color: red;"><c:if
												test="${not empty orderd.change_price }">
												<br>${orderd.change_price}</c:if></em> <c:if
											test="${not empty orderd.ffreight }">
											<br>
											<span style="color: red">购物车邮费：</span>
										${orderd.ffreight}
									</c:if> <br> <span style="color: red">交期(新)：</span> <em
										id="orderd_delivery_${sd.index}">${orderd.delivery_time}</em>
										<em id="change_delivery_${sd.index}" style="color: red;">
											<c:if test="${not empty orderd.change_delivery }">
												<br>
														${orderd.change_delivery}
													</c:if>
									</em><br> <span style="color: red">备注：</span> ${orderd.remark}
										<c:if test="${orderd.extra_freight != 0}">&nbsp;额外运费:${orderd.extra_freight}</c:if>

									</td>
									<td style="width: 20%;"><c:set value="${orderd.state}"
											var="ostate">
										</c:set> <em> <c:if test="${ostate==0}">
															${orderd.dropShipState==-1?'取消订单':'' }${orderd.dropShipState==0?'等待付款':'' }${orderd.dropShipState==1?'购买中':'' }${orderd.dropShipState==3?'出运中':'' }
<%-- 															${orderd.dropShipState==4?'完结1':'' } --%>
															<!-- 完结可以评价 -->
												<%-- ${order.state==4?'完结':'' } --%>
													<c:if test="${order.dropShipState==4}">
															 <!-- yyl 评论start -->
															<c:if test="${admuserinfo.roletype==1 || admuserinfo.admName=='Ling' || admuserinfo.admName=='ling' }"> <button  onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">添加/修改评论</button> </c:if>
															 <button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">查看所有评论
																<input type="hidden" id="goods_img${orderd.goods_pid }" value="${orderd.goods_img}"/>
																<input type="hidden" id="goods_url${orderd.goods_pid }" value="${orderd.goods_url }"/>
																<input type="hidden" id="goodsname${orderd.goods_pid }" value="${orderd.goodsname }"/>
																<input type="hidden" id="goodsprice${orderd.goods_pid }" value="${orderd.goodsprice }"/>
																<input type="hidden" id="orderDetailId${orderd.goods_pid }" value="${orderd.id }"/>
																<input type="hidden" id="carType${orderd.goods_pid }" value="${orderd.car_type }"/>
																</button>
														完结
														<br>销售评论状态： <font name="${orderd.goods_pid }ID">未评论</font>
												</c:if>
																				
															
															
															${orderd.dropShipState==5?'确认价格中':'' }
														<c:if
													test="${orderd.dropShipState==3 && orderd.dropShipState==5}">
													<br>
													<br>
													<font color="red">【采购中】</font>
												</c:if>
											</c:if> <c:if test="${ostate==1 && orderd.checked==0}">  
										已到仓库,
										<c:if test="${orderd.goodstatus==5}">
													<font color="red">已校验数量不对</font>
												</c:if>
												<c:if test="${orderd.goodstatus==4}">
													<font color="red">已校验有疑问</font>
												</c:if>
												<c:if test="${orderd.goodstatus==3}">
													<font color="red">已校验有破损</font>
												</c:if>
												<c:if test="${orderd.goodstatus==1}">
													<font color="red">未校验</font>
												</c:if>
												<c:if test="${orderd.goodstatus==2}">
													<font color="red">已校验该到没到</font>
												</c:if>
											</c:if> <c:if test="${ostate==1 && orderd.checked==1}">
										<!-- yyl 评论start -->
											<c:if test="${admuserinfo.roletype==1 || admuserinfo.admName=='Ling' || admuserinfo.admName=='ling' }"><button  onclick="showcomm('${orderd.id}','${orderd.car_type}','${admuserinfo.admName}','${order.orderNo}','${orderd.goods_pid }','${orderd.country }','${admuserinfo.id }')">添加/修改评论</button></c:if> 
											<button onclick="seeAllComments('${orderd.goods_pid }','${order.orderNo}')">查看所有评论
												<input type="hidden" id="goods_img${orderd.goods_pid }" value="${orderd.goods_img}"/>
											<input type="hidden" id="goods_url${orderd.goods_pid }" value="${goods_url }"/>
											<input type="hidden" id="goodsname${orderd.goods_pid }" value="${orderd.goodsname }"/>
											<input type="hidden" id="goodsprice${orderd.goods_pid }" value="${orderd.goodsprice }"/>
											<input type="hidden" id="orderDetailId${orderd.goods_pid }" value="${orderd.id }"/>
											<input type="hidden" id="carType${orderd.goods_pid }" value="${orderd.car_type }"/>
											</button>
											</br> 已到仓库,
										<font color="green">验货无误</font>
										</br>
												<%-- <c:set var="mygoodsid1" value="${orderd.goods_pid }${'ID'}"></c:set> --%>
											 <font color="red">
											 销售评论状态：<font name="${orderd.goods_pid }ID">未评论</font><%-- <c:if test="${not empty maps[mygoodsid1]   }">已评论 <button style="cursor:pointer" title="${maps[mygoodsid1].commentsContent }">显示评论</button>  --%>
											<%--  <input id="${orderd.goods_pid }" cmid="${maps[mygoodsid1].id }" type="hidden"  value="${maps[mygoodsid1].commentsContent }"></c:if> <c:if test="${ empty maps[mygoodsid1]  }">未评论</c:if></font> --%>
											 
											 <!--  yyl 评论 end-->
											</c:if>
											
									</em> <input type="hidden" name="ostate" value="${ostate}">
										<em id="change_cancel_${sd.index}">${orderd.iscancel==1?'<br>系统取消':''}</em>
										<em id="user_cancel_${sd.index}">${orderd.state == 2? "<br>已取消":""}</em>
										</br>运单号:<span style="color:red;">${orderd.shipno}</span> <br>最近国内物流状态:<span style="color:red;">${orderd.shipstatus}</span>
									</td>
									<input type="hidden" value="${isDropshipOrder}" id="h_isDropshipOrder1"/>
									<td style="width: 8%; text-align: center;">
										<button
											onclick="pricechange('${orderd.orderid}',${orderd.goodsid},${orderd.goodsprice}+'',${sd.index})"
											${order.state==5?'':'disabled=disabled' }>价格更新</button>
										<button
											onclick="deliverychange('${orderd.orderid}',${orderd.goodsid},${orderd.delivery_time}+'',${sd.index})"
											${order.state==5?'':'disabled=disabled' }>交期偏长</button>
										<button
											onclick="rationchange('${orderd.orderid}',${orderd.goodsid},${orderd.yourorder}+'',${sd.index})"
											${order.state==5?'':'disabled=disabled' }>订量偏低</button>
										<br />
										<button
											onclick="cancelchange('${orderd.orderid}',${orderd.goodsid},${sd.index})"
											${order.state==5?'':'disabled=disabled' }>直接取消</button>
										<button
											onclick="communicatechange('${orderd.orderid}',${orderd.goodsid})"
											${order.state==5?'':'disabled=disabled' }>需沟通</button>
										<!-- 需沟通消息记录 -->				
										<c:if test="${fn:length(orderd.change_communication)>0}">
											<hr>
											<c:if test="${orderd.ropType==5 && orderd.del_state==0 }">
												<button
													onclick="fnResolve('${order.orderNo}',${orderd.goodsid})">问题解决了</button>
												<label style="display: none; color: red;" id="msg2">(已解决)</label>
											</c:if>
											<div id="change_cmmunication_${orderd.goodsid}" style="overflow-y:scroll;height:200px;width:200px;">
												<c:forEach items="${orderd.change_communication}"
													var="change_communication">
													<span style='word-break: break-all; display: block;color:green'>${change_communication}</span>
													<hr>
												</c:forEach>
											</div>
										</c:if>
									</td>
									<!-- 添加列 -->
									<td style="word-wrap: break-word; word-break: break-all; width: 180px; color: red; width: 10%;">
										<font color="red" class="newsourceurl"> 
										
										<a href="http://192.168.1.34:8086/cbtconsole/editc/detalisEdit?pid=${orderd.goods_pid}" target="_blank">编辑链接</a>
										<span id="spanurl${sd.index}">
											<p>原始货源价格(RMB): ${orderd.price1688}</p>
										</span>
										<span id="spanurl${sd.index}">
											<p>1688货源重量（单件）(kg): ${orderd.weight1688}</p>
										</span>
										
										<span id="spanurl${sd.index}">
											<p>采购数量:${orderd.buycount}</p>
										</span> 
										<span id="spanurl${sd.index}">
											<p>采购价格(RMB):${orderd.sourc_price}</p> 
										<c:choose>
										  <c:when test="${orderd.oldsourceurl!=null}">
										  	<a href="${orderd.oldsourceurl}" target="_blank">实际采购货源链接</a>
										  </c:when>
										  <c:when test="${orderd.newsourceurl !=null}">
										  	<a href="${orderd.newsourceurl}" target="_blank">实际采购货源链接</a>
										  </c:when>
										  <c:otherwise>
										  	<span>未录入货源链接</span>
										  </c:otherwise>
										</c:choose>
										<p>${orderd.oremark}</p> <br />
										<a href="${orderd.oldUrl}" title='${orderd.oldUrl}' target="_blank">网站链接</a> </span>
										</font> 
									</td>
									<td style="color: red; width: 5%;">${orderd.purchase_time}</td>
									<td id="odid${orderd.id}" style="width: 2%;">
										<select id="buyer${orderd.id}" onchange="changeBuyer(${orderd.id},this.value);">
											<option value=""></option>
											<c:forEach var="aub" items="${aublist }">
												<option value="${aub.id }">${aub.admName}</option>
											</c:forEach>
											<!-- <option value="9">camry</option>
											<option value="32">sherry</option>
											<option value="36">cecile</option>
											<option value="48">Zalman</option>
											<option value="50">Alisa</option>
											<option value="51">Debora</option> -->
									</select><span id="info${orderd.id}"></span>
									
									<!-- 消息备注列合并过来的-->
									<div style="overflow-y:scroll;height:200px;width:200px;">
										<div class="w-font">
											<font style="font-size: 15px;" id="rk_remark_${order.orderNo}${orderd.id}">${orderd.goods_info}</font>
										</div>
									</div>
									<div class="w-margin-top">
										<input type="button" value="回复" onclick="doReplay1('${order.orderNo}',${orderd.id});" class="repalyBtn" />
									</div>
								
									</td>
									<td style="text-align: center; width: 5%;zoom:140%;"><input
										type="checkbox" onchange="fnChange(${orderd.id},this);"
										${orderd.state == 2?'checked="checked" disabled="true"':''}
										value="${orderd.id}"><input type="hidden"
										value="${orderd.state}"></td>
									<%--<td>--%>
										<%--<div style="overflow-y:scroll;height:200px;width:200px;">--%>
											<%--<div class="w-font">--%>
												<%--<font style="font-size: 15px;" id="rk_remark_${order.orderNo}${orderd.goodsid}">${orderd.goods_info}</font>--%>
											<%--</div>--%>
										<%--</div>--%>
										<%--<div class="w-margin-top">--%>
												<%--&lt;%&ndash; 								<c:if test="${orderd.goods_info != null && orderd.goods_info != ''}"> &ndash;%&gt;--%>
											<%--<input type="button" value="回复" onclick="doReplay1('${order.orderNo}',${orderd.goodsid});" class="repalyBtn" />--%>
												<%--&lt;%&ndash; 								</c:if> &ndash;%&gt;--%>
										<%--</div>--%>
									<%--</td>--%>
								</tr>
							</table>
							<script>jslr('${orderd.dropshipid}')</script>
						</div>
					</div>
				</c:forEach>
<!-- 				<div>提醒:出货问题-问题货源并且包含替换产品</div> -->
			</div>

		</c:if>
		<div id="prinum"></div>
		<div class="peimask"></div>
<!-- 		<div> -->
<!-- 			<table id="table" width="100%" align="center" border="1px" -->
<!-- 				style="font-size: 13px;" bordercolor="#8064A2" cellpadding="0" -->
<!-- 				cellspacing="0"> -->
<!-- 				<tr> -->

<!-- 					<td width="8%">客户id</td> -->
<!-- 					<td width="8%">客户名</td> -->
<!-- 					<td width="5%">邮件标题</td> -->
<!-- 					<td width="5%">邮件时间</td> -->

<!-- 					<td width="3%">销售名</td> -->

<!-- 					<td width="30%">正文内容</td> -->
<!-- 					<td width="5%">orderNO</td> -->

<!-- 				</tr> -->
<%-- 				<c:forEach items="${emaillist }" var="email" varStatus="i"> --%>
<!-- 					<tr> -->
<%-- 						<td width="30%">${email.customerId }</td> --%>
<%-- 						<td width="5%">${email.cname }</td> --%>
<%-- 						<td width="3%">${email.title }</td> --%>
<%-- 						<td width="3%">${email.createTime }</td> --%>
<%-- 						<td width="3%">${email.saleName }</td> --%>
<%-- 						<td width="3%">${email.content }</td> --%>
<%-- 						<td width="3%">${email.orderid }</td> --%>
<!-- 					</tr> -->
<%-- 				</c:forEach> --%>
<!-- 			</table> -->
<!-- 			<br /> -->
<%-- 			<div align="center">${pager }</div> --%>
<!-- 		</div> -->
	</div>	
</body>
<script type="text/javascript">
  $(function(){ 
 	 $(".toggle_btn").click(function(){ 
		 $(this).parent().next().toggle(); 
		
		 ($(this).html()=="+")?$(this).html("-"):$(this).html("+"); 
		 return false;
	 });
	})
	/* 异步加载该订单下的商品是否已经销售评论 yyl*/
$(function(){
	var orderNo=getUrl('orderNo');
	var goodPids = '${lists}';//获得该订单险所有商品的pid
	/* var controls=document.getElementsByName("550332258382ID");
	console.info(controls)
	 for(var i=0;i<controls.length;i++){
		 controls[i].innerHTML="ss"
	}  */
	$.ajax({
		type : "post",
		url : "/cbtconsole/goodsComment/searchIsComment",
		datatype : "json",
		data : {"orderNo":orderNo,"goodPids":goodPids},
		success : function(data) {
			for(var i = 0 ;i <data.length;i++){
			var pid =	data[i].goodsPid;
			var controls=document.getElementsByName(pid+"ID");
			for(var j=0;j<controls.length;j++){//cmid是该商品的id,
				controls[j].innerHTML="已评论 &nbsp;&nbsp;<button cmid='"+data[i].id+"' name='but"+pid+"' style='cursor:pointer' title=\""+data[i].commentsContent+"\">显示评论</button>"
			} 
			}
		}
	});
	
})	

//获取 url 后的参数值
function getUrl(para){
    var paraArr = location.search.substring(1).split('&');
    for(var i = 0;i < paraArr.length;i++){
        if(para == paraArr[i].split('=')[0]){
            return paraArr[i].split('=')[1];
        }
    }
    return '';
}

	var sum=0;
// 	for(var i=0;i<$("#orderDetail").find('tbody:eq(1)').find('tr').length;i++){
		
// 		sum+=parseFloat($("#orderDetail").find('tbody:eq(1)').find('tr:eq('+i+')').find('div').html());
// 		if(sum == 0){
// 			$("#sumFreight").html("该订单购物车运费：0USD");     
// 		}else{
// 			$("#sumFreight").html("该订单购物车中总运费："+(sum).toFixed(2) +"USD ("+(sum*6.89).toFixed(2)+"RMB)");   
// 		}
// 			}
	admid = '<%=uid%>';
	if (admid != 40) {
		$(".shield").hide();}
 </script>
</html>
<!-- 采购页面跳转使用 -->
<%
	request.getSession().setAttribute("cgtz", 1);
%>