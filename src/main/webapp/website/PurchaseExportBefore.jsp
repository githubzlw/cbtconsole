<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>

<title>待出库商品处理</title>
<style type="text/css">
table{border-collapse:collapse;}
.a{ height: 20px;
	position: fixed;
	top: 12%; border: 0; }
.aa{ height: 20px;
	position: fixed;;
	top: 8%; border: 0px; }
.aaa{ width: 48%; height: 20%;
	overflow:auto; top: 15%; position: fixed;
	border: 1px solid blue; }
.bb{ height: 30%; left: 55%;
	position: fixed;
	top: 12%; border: 0; }
.bbb{ width: 31%; height:20%;
	position: fixed;left:55%;
	overflow-x: auto;top:15%;
	border: 1px solid blue; }
.ccc{ width: 80%; height: 70%;
	overflow:auto; top: 40%;
	position: fixed; }
.e { font-size: 28px;
	font-weight: bold;
	font-family: "微软雅黑" }
.ee { font-size: 12px;
	font-weight: bold;
	font-family: "微软雅黑" }
.eee { font-size: 18px;
	font-weight: bold;
	font-family: "微软雅黑" }
.f { width: 90px;
	height: 25px;
	background: wheat; }
.ff { width: 100px;
	height: 15px; }
.fff { width: 60px;
	height: 15px; }
.style { margin: 0 auto;
	margin-top: 0;
	border-radius: 15px;
	border: 1px red; }
</style>
</head>
<script type="text/javascript">
function Fnclear(){
// 	$("#idresponser").val("");
	$("#iduserid").val(null);
	$("#idorderid").val(null);
	$("#iduserid").css("backgroundColor","");
	$("#idorderid").css("backgroundColor","");
}
function FnLoading(){
	var btn = document.all("ordername");
	if(btn.length>=2){
		$("#idNextOrder").css("display","block");
		document.getElementById("nextOrder").value=document.getElementById("order_1").value;
		document.getElementById("nextOrderId").value="1";
	} else {
		$("#idNextOrder").css("display","none");
	}
}
//单击用户id显示用户订单
function FnOutId(id){
	//保存当前点击的用户id
	$("#h_userid").val("id_"+id);
	$.ajax({
		type : 'POST',
		url : '/cbtconsole/PurchaseServlet?action=getOrdersById&className=Purchase',
		dataType : 'text',
		data : {userid : id },
		success : function(data){
		
			var value = "";
			var dt = ((data.replace("[","")).replace("]","")).split(",");

			for(var i=0;i<dt.length;i++){  //用户id对应的所有订单号
			//	alert("用户订单号"+dt[i]);
				if(i%2==1){
					value = value + "<input type='button' name='ordername' id='order_"+i+"' value='"+dt[i]+"' "
					+"onclick=\"FnOutOrder('"+i+"','"+dt[i]+"');\" onfocus=\"setStyleTwo(this.id);\" style='width:200px;height:25px;'><br>";
				} else {
					value = value + "<input type='button' name='ordername' id='order_"+i+"' value='"+dt[i]+"' "
					+"onclick=\"FnOutOrder('"+i+"','"+dt[i]+"');\" onfocus=\"setStyleTwo(this.id);\" style='width:200px;height:25px;'>";
				}
				//测试
			//	if(i==0){
			//		value = value + "<input type='button' name='ordername' id='order_"+1+"' value='O3083629264361872' "
			//		+"onclick=\"FnOutOrder('"+1+"','"+dt[i]+"');\" onfocus=\"setStyleTwo(this.id);\" style='width:200px;height:25px;'>";

			//	}
			}
			
			document.getElementById("OrderListDiv").innerHTML=value;
			$("#orderDetailsDiv").css("display","none");
			FnOutOrder("order_0",dt[0]); //显示用户的第一个订单详情
			setStyleTwo("order_0");
		}
	});
}

//根据订单id显示订单详细信息
function FnOutOrder(i,order){
	//保存当前显示的订单id
	if(i!="order_0"){
		$("#h_orderid").val("order_"+i);
	}else{
		$("#h_orderid").val(i);
	}
	
//	alert($("#h_orderid").val());
	var iii = parseInt(i)+1;

	$.ajax({
		type : 'POST',
		url : '/cbtconsole/PurchaseServlet?action=getDetailsByOrder&className=Purchase',
		dataType : 'text',
		data : {orderno : $.trim(order) },
		success : function(data){

			$("#orderDetailsDiv").css("display","block");
			document.getElementById("orderDetailsDiv").innerHTML=data;
			if(document.getElementById("order_"+iii)){ //存在
				$("#idNextOrder").css("display","block");
				document.getElementById("nextOrder").value=document.getElementById("order_"+iii).value;
				document.getElementById("nextOrderId").value=iii;
			} else {
				$("#idNextOrder").css("display","none");
			} if(i=="order_0"){
				
				FnLoading();  
			}
		}
	});
}
function FnOutOrderTwo(){
	var valOrder = $.trim(document.getElementById("nextOrder").value);
	var valOrderId = document.getElementById("nextOrderId").value;
	
	var order = $("#proOrderid").val();//订单号
	
	if(valOrder == ''){  //当前用户id 的所有订单 已经处理完毕
	//	alert("当前用户id 的所有订单 已经处理完毕");
	
		//处理完毕删除用户id
		var h_userid = $("#h_userid").val();
		$("#"+h_userid).remove();
		return;
	}
//	alert(valOrder);
	var iii = parseInt(valOrderId)+1;
	$.ajax({
		type : 'POST',
		url : '/cbtconsole/PurchaseServlet?action=getDetailsByOrder&className=Purchase',
		dataType : 'text',
		data : {orderno : valOrder },
		success : function(data){
			$("#orderDetailsDiv").css("display","block");
			document.getElementById("orderDetailsDiv").innerHTML=data;
			if(document.getElementById("order_"+iii)){ //存在
				$("#idNextOrder").css("display","block");
				document.getElementById("nextOrder").value=document.getElementById("order_"+iii).value;
				document.getElementById("nextOrderId").value=iii;
			} else {
				$("#idNextOrder").css("display","none");
			}
			setStyleTwo("order_"+valOrderId);
		}
	});
}
function FnSearch(){
//	alert("123");
// 	var admid = document.getElementById("idresponser").value;///采购员ID//
	var userid = $.trim(document.getElementById("iduserid").value);///用户ID//
	var orderno = $.trim(document.getElementById("idorderid").value);///订单编号//
	if(userid==""&&orderno==""){
		$("#iduserid").css("backgroundColor","yellow");
		$("#idorderid").css("backgroundColor","yellow");
	} else {
		if(isNaN(userid)){
			$("#iduserid").css("backgroundColor","yellow");
			$("#idorderid").css("backgroundColor","");
		} else {
			var orderStyle = /^[A-Za-z0-9_]*$/;
	        if (orderStyle.test(orderno)==false) {
				$("#iduserid").css("backgroundColor","");
				$("#idorderid").css("backgroundColor","yellow");
	        } else {
				$("#iduserid").css("backgroundColor","");
				$("#idorderid").css("backgroundColor","");
	        	if(userid!=null&&userid!=""){
	        		FnOutId(userid);
	        	} $.ajax({
	        		type : 'POST',
	        		url : '/cbtconsole/PurchaseServlet?action=getDetailsByOrder&className=Purchase',
	        		dataType : 'text',
	        		data : {userid : userid,orderno : orderno },
	        		success : function(data){
	        			$("#orderDetailsDiv").css("display","block");
	        			document.getElementById("orderDetailsDiv").innerHTML=data;
	        			$("#idNextOrder").css("display","none");
	        		}
	        	});
	        }
		}
	}
}
function FnFeeCount(id,currency){
	//清空
	$("#feeCountRes1").val("");
		$("#feeCountRes2").val("");
		$("#feeCountRes3").val("");
		$("#feeCountRes5").val("");
		$("#feeCountRes6").val("");
	
	$("#transstyle").val(id);
	var country = $.trim(document.getElementById("proCountry").innerHTML);//transport
	var weight = $.trim(document.getElementById("proWeight").value);//产品重量
	var volume = $.trim(document.getElementById("proVolume").value);//产品体积
	var countrycode = $("#idzone").val();//4px对应的国家代码
	var trans = $("#idtrans").val();//4px运输方式 idtrans2a
	var idjcysfs = $("#idjcysfs").val();//佳成运输方式
	var jcCargoType = $("#jcCargoType").val();//货物类型
	var idarea = $("#idarea").val();//区域
	if(isNaN(weight)||country==""||weight==""){  // 4px，原飞航都必须有的
		$("#tdpro_weight").css("backgroundColor","red");
		document.getElementById("feestyle1").checked=false;
		document.getElementById("feestyle2").checked=false;
		document.getElementById("feestyle3").checked=false;
		
		if(id == 4){
			$("#idzone").val('nul');
		}
	} else if(id==1){  //4px
		$("#tdpro_weight").css("backgroundColor","");
		$("#tdzone").css("backgroundColor","");
		document.getElementById("feestyle2").checked=false;
		if(volume==""){
			$("#tdpro_colume").css("backgroundColor","red");
			document.getElementById("feestyle1").checked=false;
			$("#tdpro_colume").focus();
		} else {
			var v = volume.split("*");
			if(v[2]&&!v[3]){
				if(isNaN(v[0])||isNaN(v[1])||isNaN(v[2])||v[2]==""||v[1]==""||v[0]==""){
					$("#tdpro_colume").css("backgroundColor","red");
					document.getElementById("feestyle1").checked=false;
				} else {
					$("#tdpro_colume").css("backgroundColor","");
					if(countrycode=="nul"){
						$("#tdzone").css("backgroundColor","red");
						document.getElementById("feestyle1").checked=false;
					} else if(trans=="nul"){
						$("#tdzone").css("backgroundColor","");
						$("#tdtrans").css("backgroundColor","red");
						document.getElementById("feestyle1").checked=false;
					} else {
						$("#tdtrans").css("backgroundColor","");
						document.getElementById("feestyle1").checked=true;
						$("#countrate").val(1.5);
					 	$.ajax({
					 		type : 'POST',
					 		url : '/cbtconsole/PurchaseServlet?action=getFeeByStyle&className=Purchase',
					 		data : {
					 			id : id,
					 			currency : currency,
					 			volume : volume,
					 			weight : weight,
					 			country : country,
					 			countrycode : countrycode,
					 			trans : trans
					 			},
					 		success : function(fee){
					 			if(fee=="count_failure"){
					 				$("#feeCountRes1").val("");
					 				$("#feeCountRes2").val("");
					 				$("#feeCountRes3").val("");
					 				$("#feeCountRes5").val("");
					 				$("#feeCountRes6").val("");
					 				$("#feeCountDay").val("");
					 				alert("查询不到相关费用！");
					 			} else {
					 				var fees = fee.split("#");
					 				$("#feeCountRes1").val(fees[0]);
					 				$("#feeCountRes2").val(fees[0]);
					 				$("#feeCountRes3").val((fees[0]*1.5).toFixed(2));
					 				$("#feeCountRes5").val((parseFloat($("#feeCountRes3").val())-parseFloat($("#feeCountRes4").val())).toFixed(2));
					 				$("#id_feeCountRes5").val(parseFloat($("#feeCountRes5").val()));
					 				$("#feeCountRes6").val((parseFloat($("#feeCountRes5").val())+parseFloat($("#packageFee").val())).toFixed(2));
					 				if(fees[1]){
					 					$("#feeCountDay").val(fees[1]+"天");
					 				} else {
					 					$("#feeCountDay").val("");
					 				}
					 				document.getElementById("id_trans_fee").disabled=false;
									$("#td_trans_fee").css("backgroundColor","turquoise");
					 			}
					 		}
					 	});
					}
				}
			} else {
				$("#tdpro_colume").css("backgroundColor","red");
				document.getElementById("feestyle1").checked=false;
			}
		}
	}else if(id==3){  //佳成
		$("#tdpro_weight").css("backgroundColor","");
		$("#tdzone").css("backgroundColor","");
		document.getElementById("feestyle2").checked=false;
		document.getElementById("feestyle1").checked=false;
		if(volume==""){
			$("#tdpro_colume").css("backgroundColor","red");
			document.getElementById("feestyle3").checked=false;
			$("#tdpro_colume").focus();
		} else {
			var v = volume.split("*");
			if(v[2]&&!v[3]){
				if(isNaN(v[0])||isNaN(v[1])||isNaN(v[2])||v[2]==""||v[1]==""||v[0]==""){
					$("#tdpro_colume").css("backgroundColor","red");
					document.getElementById("feestyle3").checked=false;
				} else {
					$("#tdpro_colume").css("backgroundColor","");
					if(countrycode=="nul"){
						$("#tdzone").css("backgroundColor","red");
						document.getElementById("feestyle3").checked=false;
					}  else if(idjcysfs=="nul"){
						$("#tdzone").css("backgroundColor","");
						$("#tdjcysfs").css("backgroundColor","red");
						document.getElementById("feestyle3").checked=false;
					}
					else {
						$("#tdjcysfs").css("backgroundColor","");
						document.getElementById("feestyle3").checked=true;
						$("#countrate").val(1.5);
					 	$.ajax({
					 		type : 'POST',
					 		url : '/cbtconsole/PurchaseServlet?action=getFeeByStyle&className=Purchase',
					 		data : {
					 			id : id,
					 			currency : currency,
					 			volume : volume,
					 			weight : weight,
					 			country : country,
					 			countrycode : countrycode,
					 			trans : idjcysfs,
					 			jcCargoType : jcCargoType
					 			},
					 		success : function(fee){
					 			if(fee=="count_failure"){
					 				$("#feeCountRes1").val("");
					 				$("#feeCountRes2").val("");
					 				$("#feeCountRes3").val("");
					 				$("#feeCountRes5").val("");
					 				$("#feeCountRes6").val("");
					 				$("#feeCountDay").val("");
					 				alert("查询不到相关费用！");
					 			} else {
					 				var fees = fee.split("#");
					 				$("#feeCountRes1").val(fees[0]);
					 				$("#feeCountRes2").val(fees[0]);
					 				$("#feeCountRes3").val((fees[0]*1.5).toFixed(2));
					 				$("#feeCountRes5").val((parseFloat($("#feeCountRes3").val())-parseFloat($("#feeCountRes4").val())).toFixed(2));
					 				$("#id_feeCountRes5").val(parseFloat($("#feeCountRes5").val()));
					 				$("#feeCountRes6").val((parseFloat($("#feeCountRes5").val())+parseFloat($("#packageFee").val())).toFixed(2));
					 				if(fees[1]){
					 					$("#feeCountDay").val(fees[1]+"天");
					 				} else {
					 					$("#feeCountDay").val("");
					 				}
					 				document.getElementById("id_trans_fee").disabled=false;
									$("#td_trans_fee").css("backgroundColor","turquoise");
					 			}
					 		}
					 	});
					}
				}
			} 
			else {
				$("#tdpro_colume").css("backgroundColor","red");
				document.getElementById("feestyle1").checked=false;
			}
		}
	} 
	//嘉城这里没做好先不启用运费查询
	/* else if(id==4){ //获得佳成运输方式
		$("#tdpro_weight").css("backgroundColor","");
		$("#tdzone").css("backgroundColor","");
		document.getElementById("feestyle2").checked=false;
		document.getElementById("feestyle1").checked=false;
		if(volume==""){
			$("#tdpro_colume").css("backgroundColor","red");
			document.getElementById("feestyle3").checked=false;
			$("#tdpro_colume").focus();
		} else {
			var v = volume.split("*");
			if(v[2]&&!v[3]){
				if(isNaN(v[0])||isNaN(v[1])||isNaN(v[2])||v[2]==""||v[1]==""||v[0]==""){
					$("#tdpro_colume").css("backgroundColor","red");
					document.getElementById("feestyle3").checked=false;
				} else {
					$("#tdpro_colume").css("backgroundColor","");
					if(countrycode=="nul"){
						$("#tdzone").css("backgroundColor","red");
						document.getElementById("feestyle3").checked=false;
					}  else {
						$("#tdjcysfs").css("backgroundColor","");
						document.getElementById("feestyle3").checked=true;
						$("#countrate").val(1.5);
					 	$.ajax({
					 		type : 'POST',
					 		url : '/cbtconsole/PurchaseServlet?action=getFeeByStyle&className=Purchase',
					 		data : {
					 			id : id,
					 			currency : currency,
					 			volume : volume,
					 			weight : weight,
					 			country : country,
					 			countrycode : countrycode,
					 			trans : idjcysfs,
					 			jcCargoType : jcCargoType
					 			},
					 		success : function(data){
					 			if(data=="count_failure"){
					 				$("#idjcysfs").html("");
						 			$("#idjcysfs").append("<option value=\"nul\" selected=\"selected\">------运-输-方-式------</option>"); 
					 				$("#feeCountRes1").val("");
					 				$("#feeCountRes2").val("");
					 				$("#feeCountRes3").val("");
					 				$("#feeCountRes5").val("");
					 				$("#feeCountRes6").val("");
					 				$("#feeCountDay").val("");
					 				alert("查询不到相关运输方式！");
					 			}else{
					 				
					 			
						 			var list = eval("("+data+")");
						 			var i;
						 			$("#idjcysfs").html("");
						 			$("#idjcysfs").append("<option value=\"nul\" selected=\"selected\">------运-输-方-式------</option>"); 
						 			for(i=0; i<list.length; i++){
						 		//		alert(list[i].WLMX);
						 				
						 				$("#idjcysfs").append("<option value='"+list[i].WLMX+"'>"+list[i].WLMX+"</option>"); 
						 			}
						 	//		$('#idjcysfs').change();
						 			$("#idjcysfs option:selected").text("------运-输-方-式------");
					 			}
					 		}
					 	});
					}
				}
			} 
			
			else {
				$("#tdpro_colume").css("backgroundColor","red");
				document.getElementById("feestyle1").checked=false;
			}
		}
	}
	*/
	else if(id==2){  //原飞航
	 	$("#tdtrans").css("backgroundColor","");
	 	$("#tdzone").css("backgroundColor","");
		$("#tdpro_weight").css("backgroundColor","");
	 	$("#tdpro_colume").css("backgroundColor","");
		document.getElementById("feestyle1").checked=false;
		if(idarea=="nul"){
			$("#td_area").css("backgroundColor","red");
			document.getElementById("feestyle2").checked=false;
		} else {
			$("#td_area").css("backgroundColor","");
			document.getElementById("feestyle2").checked=true;
			$("#countrate").val(1.5);
			$.ajax({
		 		type : 'POST',
		 		url : '/cbtconsole/PurchaseServlet?action=getFeeByStyle&className=Purchase',
		 		data : {
		 			id : id,
		 			currency : currency,
		 			volume : volume,
		 			weight : weight,
		 			country : country,
		 			countrycode : countrycode,
		 			trans : trans,
		 			area : idarea
		 			},
		 		success : function(fee){
	 				var fees = fee.split("#");
	 				$("#feeCountRes1").val(fees[0]);
	 				$("#feeCountRes2").val(fees[0]);
	 				$("#feeCountRes3").val((fees[0]*1.5).toFixed(2));
	 				$("#feeCountRes5").val((parseFloat($("#feeCountRes3").val())-parseFloat($("#feeCountRes4").val())).toFixed(2));
	 				$("#id_feeCountRes5").val(parseFloat($("#feeCountRes5").val()));
	 				$("#feeCountRes6").val((parseFloat($("#feeCountRes5").val())+parseFloat($("#packageFee").val())).toFixed(2));
	 				if(fees[1]){
	 					$("#feeCountDay").val(fees[1]+"天");
	 				} else {
	 					$("#feeCountDay").val("");
	 				}
	 				document.getElementById("id_trans_fee").disabled=false;
					$("#td_trans_fee").css("backgroundColor","turquoise");
	 			}
		 	});
		}
	}
}
function FnFeeCountTwo(value,currency){
	$.ajax({
		type:'POST',
		url:'/cbtconsole/PurchaseServlet?action=getExchangeRate&className=Purchase',
		data:{currency : currency},
		success:function(excRate){
			var ex = (value * excRate).toFixed(2);
			$("#packageFee").val(ex)
			var unpaied = $("#feeCountRes5").val();//未支付运费
			$("#feeCountRes6").val((parseFloat(unpaied)+parseFloat($("#packageFee").val())).toFixed(2));//总的未支付费用
		}
	});
}

//输入美元计算总费用
function getTotalcost(value){
	if(value == ''){
		
		return;
	}
	$.ajax({
		type:'POST',
		url:'/cbtconsole/PurchaseServlet?action=getUsatoRmb&className=Purchase',

		success:function(excRate){
			var ex = (value * excRate).toFixed(2);
			$("#inRmb3").val(ex);
			var unpaied = $("#feeCountRes5").val();//未支付运费
			$("#feeCountRes6").val((parseFloat(unpaied)+parseFloat($("#packageFee").val())).toFixed(2));//总的未支付费用
		}
	});
	
}
//保存出库信息
function FnSaveOrderFeeInfo(){
	var uid = $("#userid").val();//用户id
	var order = $("#proOrderid").val();//订单号
	var packagefee = $.trim($("#packageFee").val());//额外包装费
	var actfee = $.trim($("#feeCountRes2").val());//实际运费
	var days = $("#feeCountDay").val();//交期
	var actcgetfee = $.trim($("#feeCountRes3").val());//实收运费
	var volume = $.trim($("#proVolume").val());//体积
	var weight = $.trim($("#proWeight").val());//重量
	var country = $.trim(document.getElementById("proCountry").innerHTML);//transport
	var feetrans = $.trim($("#feeCountRes5").val());//未支付运费
	var feecount = $.trim($("#feeCountRes6").val());//总的未支付费用
	var transport = $("#transstyle").val();//4PX,YuanFeiHang
	var zone = $("#idzone").val();//4px对应的国家代码
	var trans = $("#idtrans").val();//4px运输方式
	var idarea = $("#idarea").val();//区域
	var jcCargoType = $("#jcCargoType option:selected").val();//货物类型
	var admin = $("#idadmin").val();  //操作人员
	var id_trans_state = $("#id_trans_state").val(); //余额，运费余额抵扣
	var applicable_credit = $("#applicable_credit").val();
	var deduction = $("#id_trans_deduction").val();//运费余额抵扣的费用
	var currency = $("#ucurrency").val();
//	alert($("#idtrans option:selected").val());
	
	if(transport==1&&(uid==""||order==""||packagefee==""||volume==""||weight==""||country==""||feetrans==""||feecount=="")){
		alert("订单费用信息不全；请确认！");
	} else if(transport==2&&(uid==""||order==""||packagefee==""||weight==""||country==""||feetrans==""||feecount=="")){
		alert("订单费用信息不全；请确认！");
	} else {
		$.ajax({
			type : 'POST',
			url : '/cbtconsole/PurchaseServlet?action=saveOrderFeeDetails&className=Purchase',
			data : {
				uid : uid,
				order : order,
				currency : currency,
				packagefee : packagefee,
				actfee : actfee,
				actcgetfee : actcgetfee,
				volume : volume,
				weight : weight,
				country : country,
				feetrans : feetrans,
				feecount : feecount,
				transport : transport,
				zone : zone,
				trans : trans,
				area : idarea,
				days : days,
				admin : admin,
				id_ts : id_trans_state,
				app_credit : applicable_credit,
				deduction : deduction,
				jcCargoType : jcCargoType
				},
			success : function(i){
				if(i==0){
					alert("保存失败！");
				} else {
					
				//	FnOutOrderTwo();  这里不知道是什么暂时注释掉
				alert("保存成功");
				//保存成功隐藏本次的用户id对应的订单号
				var h_orderid = $("#h_orderid").val();
				
				$("#"+h_orderid).remove();
			//	alert(h_orderid);
				
				FnOutOrderTwo();   // 这里报错  
				//	$("#h_ch").val("1");
				}
			}
		});
	}
	
}
</script>
<body>
<div style="width:90%;height:100%; margin-left: 5%;">
	<div class="style">
		<div>
			<span class="e">待出库商品处理</span>
			<span class="eee"><a href="/cbtconsole/PurchaseServlet?action=getOut&className=Purchase" target="_blank">当天可出货商品</a></span>
		</div>
	</div>
	<div class="aa">
<!-- 		<span>采购负责人:</span><span> -->
<!-- 	       	 <select id="idresponser" style="width:100px;height:20px;" onkeypress="if (event.keyCode == 13) FnSearch();"> -->
<%-- 				<c:if test="${empty admuser}"> --%>
<!-- 					<option value="00">没有工作人员！</option> -->
<%-- 	            </c:if> --%>
<%-- 	            <c:if  test="${not empty admuser}"> --%>
<!-- 	            	<option value="" selected="selected">--采购人员--</option> -->
<%-- 					<c:forEach items="${admuser}" var="admu"> --%>
<%-- 						<option value="${admu.admName}">${admu.admName}</option> --%>
<%-- 					</c:forEach> --%>
<%-- 				</c:if> --%>
<!-- 			</select></span> -->
		<span>用户ID:</span><span><input type="text" id="iduserid" class="ff" onkeypress="if (event.keyCode == 13) FnSearch();"></span>
		<span>订单号:</span><span><input type="text" id="idorderid" style="width: 200px;height: 15px;" onkeypress="if (event.keyCode == 13) FnSearch();"></span>
		<span><input type="button" value="查询" onclick="FnSearch();" class="f"></span>
		<span><input type="button" value="重置" onclick="Fnclear();"></span>
	</div>
	<div class="a">可出货用户ID:</div>
	<div class="aaa">
       	<c:if test="${empty idlist}">
        	<div><font style="font-size: 24px" >没有可出货ID！</font></div>
		</c:if>
		<c:if test="${not empty idlist}">
			<c:forEach items="${idlist}" var="id"> 
        		<input type="button" value="${id.id}" id="id_${id.id}" name="name_user_id" onclick="FnOutId('${id.id}');" onfocus="setStyle(this.id);" class="f">
       		</c:forEach>
       	</c:if>
	</div>
	<div class="bb">客户<span id="user_order" style="color: red;"></span>对应订单:<span>
	</span>
	</div>
	<div class="bbb">
		<div id="OrderListDiv">
			<c:if test="${empty uiblist}">
				<div>
					<font style="font-size: 24px">没有相应数据！</font>
				</div>
			</c:if>
			<c:if test="${not empty uiblist}">
				<c:forEach items="${uiblist}" var="uib">
					<c:forEach items="${uib.purchaseBean}" var="pb" varStatus="i">
						<input type="button" name='ordername' id="order_${i.index}" value="${pb.orderNo}" onclick="FnOutOrder('${i.index}','${pb.orderNo}');" onfocus="setStyleTwo(this.id);" style="width:200px;height:25px;">
					</c:forEach>
				</c:forEach>
			</c:if>
		</div>
	</div>
	<div class="ccc" align="center">
	<div align="left"><span class="eee">订单详情：</span><span>操作人员：<input type="text" id="idadmin" value="Eric" onblur="fnAdmin();"></span></div>
	<div><br></div>
	<div id="orderDetailsDiv">
		<div>
			<table width="100%" border="1px" height="60%" class="ee">
				<tr>
					<td>用户ID:</td>
					<td><input type="text" value="1314" class="ff"></td>
					<td>销售负责人:</td>
					<td><input type="text" value="Ling" class="ff"></td>
					<td>账户余额:</td>
					<td><input type="text" value="3232 USD" class="ff"></td>
					<td>赠送运费余额:</td>
					<td><input type="text" value="32 USD" class="ff"></td>
					<td>采购负责人:</td>
					<td><input type="text" value="Camary" class="ff"></td>
				</tr>
				<tr>
					<td>订单号:</td>
					<td colspan="2"><input type="text" value="NC282060549390052" style="width: 180px;height: 15px;"></td>
					<td>支付时间:</td>
					<td colspan="2"><input type="text" value="2015-11-31 23:00" style="width: 150px;height: 15px;"></td>
					<td>国内交期:</td>
					<td><input type="text" value="12" class="fff">天</td>
					<td>国外交期:</td>
					<td><input type="text" value="2016-8-12" class="ff"></td>
				</tr>
				<tr>
					<td>收货地址:</td>
					<td colspan="5"><textarea id="" name="" rows="1" cols="60"></textarea></td>
					<td>运输方式:</td>
					
					<td colspan="3"></td>
				</tr>
				<tr>
					<td>已支付金额:</td>
					<td><input type="text" value="200 USD" class="ff"></td>
					<td>产品金额:</td>
					<td><input type="text" value="160 USD" class="ff"></td>
					<td>已支付运费:</td>
					<td><input type="text" value="30 USD" class="ff"></td>
					<td>已支服务费:</td>
					<td><input type="text" value="20 USD" class="ff"></td>
					<td>未支付费用:</td>
					<td><fmt:formatNumber type="number" value="" pattern="0.00" maxFractionDigits="2"/>10.00USD</td>
				</tr>
				<tr>
					<td>产品预估重量:</td>
					<td><input type="text" value="8 KG" class="fff">Kg</td>
					<td>产品预估体积:</td>
					<td><input type="text" value="002" class="fff">M<sup>3</sup></td>
					<td>预估运费:</td>
					<td><input type="text" value="25 USD" class="ff"></td>
					<td>额外包装费:</td>
					<td><input type="text" value="25 USD" class="ff"></td>
					<td colspan="2"></td>
				</tr>
				<tr>
					<td>产品重量:</td>
					<td><input type="text" value="8 KG" class="fff">Kg</td>
					<td>产品体积:</td>
					<td><input type="text" value="002" class="fff">M<sup>3</sup></td>
					<td>实际运费:</td>
					<td><input type="text" value="25 USD" class="ff"></td>
					<td>实收运费:</td>
					<td><input type="text" value="30 USD" class="ff"></td>
					<td colspan="2" bgcolor="wheat">实收运费，不得小于实际运费和预估运费</td>
				</tr>
				<tr>
					<td colspan="10" class="eee">预出库信息:</td>
				</tr>
				<tr>
					<td>已支付金额:</td>
					<td><input type="text" value="200 USD" class="ff"></td>
					<td>产品金额:</td>
					<td><input type="text" value="160 USD" class="ff"></td>
					<td>已支付运费:</td>
					<td><input type="text" value="30 USD" class="ff"></td>
					<td>已支服务费:</td>
					<td><input type="text" value="20 USD" class="ff"></td>
					<td>运输国家:</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="4"></td>
					<td>未支付运费:</td>
					<td><input type="text" value="0 USD" class="ff"></td>
					<td>未支服务费:</td>
					<td><input type="text" value="0 USD" class="ff"></td>
					<td>运输方式:</td>
					<td></td>
				</tr>
				<tr>
					<td colspan="2"></td>
					<td colspan="2"><input type="checkbox">运费余额抵扣</td>
					<td colspan="2"><input type="checkbox">余额抵扣/运费余额抵扣</td>
					<td colspan="2">运费计算:<input type="text" value="22 USD" class="fff">
					<input type="text" class="fff"></td>
					<td><input type="radio" name="feestyle" disabled="disabled">4PX计费</td>
					<td><input type="radio" name="feestyle" disabled="disabled">原飞航计费</td>
				</tr>
				<tr>
					<td colspan="6"></td>
					<td>未支付费用:</td>
					<td><input type="text" value="0 USD" class="ff"></td>
					<td colspan="2" align="center">示例:<input type="button" value="保存待出库信息" disabled="disabled"></td>
				</tr>
			</table>
		</div>
		<div><br></div>
		<div align="right">
			<input type="button" value="下一单" onclick="FnOutOrderTwo();" disabled="disabled">
		</div>
		</div>
		
		<input type="hidden" id="h_ch"/>
		<input type="hidden" id="h_orderid"/> <!-- 当前点击的订单id -->
		<input type="hidden" id="h_userid"/> <!-- 当前点击的用户id -->
		
	</div>
</div>
<script>
function FnTransFee(transfee,state){ //余额，运费余额抵扣
	if(state==1){
		var id_trans_state = $("#id_trans_state").val();
		if(id_trans_state==1){
			var feecount = $("#feeCountRes6").val(); //未支付总费用
			if(parseFloat(transfee)>=parseFloat(feecount)){ //余额大于等于未支付总费用
				$("#applicable_credit").val((parseFloat($("#applicable_credit").val())-parseFloat($("#feeCountRes5").val())-parseFloat($("#packageFee").val())).toFixed(2));
				$("#td_applicable_credit").css("backgroundColor","darkturquoise");
				$("#feeCountRes5").val("0.00");
				$("#feeCountRes6").val("0.00");
				$("#id_trans_deduction").val(parseFloat(feecount));//运费余额抵扣费用
			} else { //余额小于未支付总费用
				$("#applicable_credit").val("0.00");
				$("#td_applicable_credit").css("backgroundColor","darkturquoise");
				$("#feeCountRes5").val((parseFloat($("#feeCountRes5").val())-parseFloat(transfee)).toFixed(2));
				$("#feeCountRes6").val((parseFloat($("#feeCountRes5").val())+parseFloat($("#packageFee").val())).toFixed(2));
				$("#id_trans_deduction").val(parseFloat(transfee));//运费余额抵扣费用
			}
			$("#id_trans_state").val("2");
		} else if(id_trans_state==2){
			$("#applicable_credit").val($("#id_applicable_credit").val());
			$("#td_applicable_credit").css("backgroundColor","");
			$("#feeCountRes5").val(parseFloat($("#id_feeCountRes5").val()));
			$("#feeCountRes6").val((parseFloat($("#feeCountRes5").val())+parseFloat($("#packageFee").val())).toFixed(2));
			$("#id_trans_state").val("1");
			$("#id_trans_deduction").val("0");//运费余额抵扣费用
		}
	}
}
function proVolume(obj){
	if(obj==1&&$.trim($("#proVolume").val())==''){
		$("#proVolume").val('0*0*0');
	} else if($("#proVolume").val()=='0*0*0'){
		$("#proVolume").val('');
	}
}
function fnAdmin(){
	if($.trim($("#idadmin").val())==''){
		$("#idadmin").val('Eric');
	}
}
function fncountrate(rate){
	if(rate=="paid"){
		$("#feeCountRes3").val((parseFloat($("#feeCountRes4").val())).toFixed(2));
		$("#feeCountRes5").val((parseFloat($("#feeCountRes3").val())-parseFloat($("#feeCountRes4").val())).toFixed(2));
		$("#feeCountRes6").val((parseFloat($("#feeCountRes5").val())+parseFloat($("#packageFee").val())).toFixed(2));
	} else {
		$("#feeCountRes3").val((parseFloat($("#feeCountRes2").val())*rate).toFixed(2));
		$("#feeCountRes5").val((parseFloat($("#feeCountRes3").val())-parseFloat($("#feeCountRes4").val())).toFixed(2));
		$("#feeCountRes6").val((parseFloat($("#feeCountRes5").val())+parseFloat($("#packageFee").val())).toFixed(2));
	}
}
function setStyle(x){
	var uid = document.all("name_user_id");
	for(var i=0;i<uid.length;i++){
		uid[i].style.background="";
	}
	document.getElementById(x).style.background="gray";
	document.getElementById("user_order").innerHTML=document.getElementById(x).value;
}
function setStyleTwo(x){
	var uid = document.all("ordername");
	for(var i=0;i<uid.length;i++){
		uid[i].style.background="";
	}
	document.getElementById(x).style.background="darkturquoise";
}
</script>
</body>
</html>