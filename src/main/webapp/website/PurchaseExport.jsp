<%@ page import="com.cbt.website.bean.PurchaseBean"%>
<%@ page import="com.cbt.website.bean.PurchaseDetailsBean"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>当天可出货所用订单</title>
<style type="text/css">
table{border-collapse:collapse;}
a{text-decoration:none}
.aaa{ width: 11%; height: 16%;
	overflow:auto;top: 9%; position: fixed;
	border: 2px solid blue; }
.b { margin-left: 20px; }
.bb{ height: 20px;
	position: fixed;left: 13%;
	top: 6%; border: 0px; }
.bbb{ width: 20%; height: 16%;
	position: fixed;left: 13%;
	overflow: auto;top: 9%;
	border: 2px solid blue; }
.c { font-size: 14px; }
.cc { font-size: 18px;
	border: 1px;
	background: #BDB76B; }
.ccc{ width: 33%; height: 72%;
	position: fixed;font-size: 14px;
	overflow: auto;top: 27%;
	border: 2px solid green; }
.d { font-size: 17px;
	font-weight: bold; }
.ddd { width: 65%;
	height: 95%;
	right: 0px;
	position: fixed;
	background: snow;
	top: 1%;
	z-index: 1011;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 1011;
	overflow: auto;
	border: 2px solid #33CCFF; }
.e { font-size: 28px;
	font-weight: bold;
	font-family: "微软雅黑" }
.ee { font-size: 20px;
	font-weight: bold;
	font-family: "微软雅黑" }
.f { width: 90px;
	height: 25px;
	background: wheat; }
.ff{ width: 130px;
	height: 14px; }
.fff { font-size: 12px; color: #000}
.g { font-size: 17px; }
.h { font-size: 28px;
	text-decoration: none; }
.style { margin: 0 auto;
	margin-top: 0;
	border-radius: 15px;
	border: 1px red; }
.show_x { position: absolute;
	top: 5px;
	right: 15px; }
.show_h3 { height: 20px;
	text-align: left; }
.mod_pay1 { position: absolute;
	top: 0px;
	left: 0px;
	width: 100%;
	height: 1000px;
	z-index: 2222;
	background-color: #666;
	opacity: .3;
	display: none; }
.mod_pay2 { width: 400px;
	height: 700px;
	position: fixed;
	background: snow;
	top: 10px;
	left: 1%;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 7777;
	display: none;
	overflow: auto;
	border: 5px solid #33CCFF; }
.mod_pay3 { width: 745px;
	height: 448px;
	position: fixed;
	background-image: url('/cbtconsole/img/yuanfeihang/yuanfeihang.JPG');
	top: 0;
	left: 25%;
	padding: 5px;
	padding-bottom: 20px;
	z-index: 9999;
	display: none;
	overflow: auto;
	border: 5px solid #33CCFF; }
.show_up { position: fixed;
	top: 90%; right: 35px; }
/* .div { width:250px; height:40px; */
/* 	word-wrap:break-word; } */
.admin{background: #FFCC99; color: #000; }
.user{ background: wheat; color: #000; }
</style>
</head>
<script type="text/javascript">
function alertdivHide() {
	document.getElementById("alertdiv").style.display = "none";
	document.getElementById("bigestdiv").style.display = "none";
	document.getElementById("yuanfeihangdiv").style.display = "none";
}
function FnOutPortAll() {
	var chks = document.all('chk');
	var j = chks.length;
	var ck = "<div class='show_x'><a href='javascript:void(0)' onclick='alertdivHide();' class='h'>×</a></div>"
				+"<div class='e'>请核对出货订单：</div>";
	var alertdiv = document.getElementById("alertdiv");
	for (var i = 0; i < chks.length; i++) {
		if (chks[i].checked == true) {
			if (chks[i].name == "kehuID") {
				ck = ck + "<div style='color: #000'>客户ID：" + chks[i].value + "</div>";
			} else {
				ck = ck + "<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + chks[i].value + "<span><br/>";
			}
		}
	}
	ck = ck + "<input type='button' value='确认出货' onclick='outConfirm();'>"+
	"&nbsp;&nbsp;&nbsp;&nbsp;<input type='button' value='取消出货' onclick='alertdivHide();'>";
	alertdiv.innerHTML = "<span style='color:red'>" + ck + "</span>";
	alertdiv.style.display = "block";
	document.getElementById("bigestdiv").style.display = "block";
	$("#bigestdiv").css('height', document.body.scrollHeight);
}
function FnOutId(id){
	
	$.ajax({
		type : 'POST',
		url : '/cbtconsole/PurchaseServlet?action=getOutById&className=Purchase',
		dataType : 'text',
		data : {userid : id },
		success : function(data){
			document.getElementById("orderdetailsdiv").innerHTML = data;
			document.getElementById("hiddenUserid").value=id;
			document.getElementById("buttoniidd").disabled="disabled";
			document.getElementById("exportnameremark").innerHTML="";
			var exportname = document.all("exportname");
			for(var y=0;y<exportname.length;y++){
				exportname[y].value="";
			}
			var orderlist = document.all("hidorder");
			var odlist="";
			if(orderlist.length > 1){
				for(var i=0;i<orderlist.length;i++){
					var olv = orderlist[i].value;
					var addre = document.getElementById(olv).value;
					odlist = odlist + olv + "<input type='checkbox' id='chkk' name='order_"+olv+"' onclick=showdetails('"+olv+"');FnCombine('"+olv+"'); value='"+olv+"' /><br/>";
				}
				var hod = document.getElementById("hiddenOrderDiv");
				hod.innerHTML = "<span>"+odlist+"</span>";
			} else {
				odlist = document.getElementById("hidorder").value;
				var hod = document.getElementById("hiddenOrderDiv");
				hod.innerHTML = "<span>"+odlist+"</span><input type='checkbox' id='chkk' name='order_"+olv+"' onclick=showdetails('"+odlist+"');FnCombine('"+olv+"'); value='"+odlist+"'/>";
			}
		}
	});
}
function showdetails(order){

	var ssdd = document.getElementById("iidd_"+order);
	if(ssdd.style.display == "block"){
 		ssdd.style.display = "none";
	} else {
 		ssdd.style.display = "block";
	}
}
function FnHideOrder(){

	var orderlist = document.all("hidorder");
	var odlist="";
	if(orderlist.length > 1){
		for(var i=0;i<orderlist.length;i++){
			var olv = orderlist[i].value;
			odlist = odlist + olv + "<input type='checkbox' id='chkk' name='order_"+olv+"' onclick=showdetails('"+olv+"');FnCombine('"+olv+"'); value='"+olv+"' /><br/>";
		}
		var hod = document.getElementById("hiddenOrderDiv");
		hod.innerHTML = "<span>"+odlist+"</span>";
	} else {
		odlist = document.getElementById("hidorder").value;
		var hod = document.getElementById("hiddenOrderDiv");
		hod.innerHTML = "<span>"+odlist+"</span><input type='checkbox' id='chkk' name='order_"+olv+"' onclick=showdetails('"+odlist+"');FnCombine('"+olv+"'); value='"+odlist+"' />";
	}
}

function FntransMethod(yfh){
	alert(yfh);
}
//显示物流信息
function FnShowFpx(){
	var meth = document.getElementById("outMethod").value;//出货方式
	var chks = document.all('chkk');
//	alert(chks+"---"+meth);
	var j = chks.length;
	var aaa = 0;
	var wuliu = "";
	if(j > 1){
		for(var i = 0; i < j; i++) {
			if(chks[i].checked==true){
				aaa = aaa + 1;
				wuliu = wuliu + chks[i].value + ",";
			}
		}
	} else {
		wuliu = document.getElementById("chkk").value + ",";
	} if(wuliu.length==0){
		alert("请选择订单。");
 	} else {
		$.ajax({
			type : 'POST',
			url : '/cbtconsole/PurchaseServlet?action=showWuLiu&className=Purchase',
			dataType : 'text',
			data : { cks : wuliu,outmethod : meth },
			success : function(data){
				
				document.getElementById("wuliudiv").innerHTML = data;
				var outMethod = document.getElementById("outMethod").value;
				FnSetOutMethod(outMethod);
			}
		});
	}
	
	
	
}
function FnCombine(xxx){

	var chks = document.all('chkk');
	var j = chks.length;
	if(j > 1){
		var jjj = 0;
		var jj = 0;
		var ckvNext;
		var ckvvNext;
		for(var i = 0; i < j; i++){
			if(chks[i].checked==true){
				jjj = jjj + 1;
				var ckv;
				var ckkv = chks[i].value;
				if(ckkv.indexOf(",") > 0){
					var strod = ckkv.split(",");
					ckv = strod[0];
				} else {
					ckv = ckkv;
				}
				var ckvv = document.getElementById(ckv).value;
				for(var ii = i + 1; ii < j; ii++) {
					if(chks[ii].checked==true){
						var ckvNext;
						var ckkvNext = chks[ii].value;
						if(ckkvNext.indexOf(",") > 0){
							var strodnext = ckkvNext.split(",");
							ckvNext = strodnext[0];
						} else {
							ckvNext = ckkvNext;
						}
						ckvvNext = document.getElementById(ckvNext).value;
						if(ckvv!=ckvvNext){
							jj = jj + 1;
						}
					}
				}
			}
		} if(jjj == 0){
		} else if(jjj == 1){
			FnShowFpx();
		} else if(jj != 0){
			alert("收货地址不一致不能合并！");
			var orderd = document.getElementsByName("order_"+xxx);
			orderd[0].checked=false;
			var ssdd = document.getElementById("iidd_"+xxx);
			ssdd.style.display = "none";
		} else {
			var ord = "";
			for(var i = 0; i < j; i++) {
				if(chks[i].checked==true){
					ord = chks[i].value + ",";
					for(var iiii = i + 1; iiii < j; iiii++) {
						if(chks[iiii].checked==true){
							chks[i].checked = true;
						}
					}
				}
			}
			FnShowFpx();
		}
	} else {
		FnShowFpx();
	}
}
//出货
function FnCreateFpx(){

	var outMethod = document.getElementById("outMethod").value;  // 0 4PX  1原飞航    2其他  3佳成
	var idorderno = document.getElementById("idorderno").value;  //订单号
	var idtransport = document.getElementById("idtransport").value; //运输方式:
	var idweight = document.getElementById("idweight").value; //商品重量
	var idgoodstype = document.getElementById("idgoodstype").value;  //货物类型
	var idusername = document.getElementById("idusername").value;  //收件人姓名
	var iduseremail = document.getElementById("iduseremail").value;//收件人 Email
	var idadminname = document.getElementById("idadminname").value; //寄件人信息姓名
	var idusercompany = document.getElementById("idusercompany").value; //收件人公司
	var idadmincompany = document.getElementById("idadmincompany").value;//寄件人公司
	var iduserzone = document.getElementById("iduserzone").value;   //收件人国家
	var idadminzone = document.getElementById("idadminzone").value; //寄件人国家
	var idusercode = document.getElementById("idusercode").value;  //收件人邮编
	var idadmincode = document.getElementById("idadmincode").value;//寄件人邮编
	var iduserstate = document.getElementById("iduserstate").value; //收件人州/省
	var idusercity = document.getElementById("idusercity").value;  //收件人市
	var iduserstreet = document.getElementById("iduserstreet").value;  //收件人街道
	var iduseraddress = document.getElementById("iduseraddress").value; ////收件人地址
	var idadminaddress = document.getElementById("idadminaddress").value; //寄件人地址
	var iduserphone = document.getElementById("iduserphone").value; //收件人电话
	var idadminphone = document.getElementById("idadminphone").value;//寄件人电话
	var iduserid = document.getElementById("iduserid").value;//.买家ID
	var idremark = document.getElementById("idremark").value; //备注:
	var idadminsprovince = document.getElementById("idadminsprovince").value;//寄件人州/省
	var idadmincity = document.getElementById("idadmincity").value;//寄件人市
	var idGoodsSum = document.getElementById("idGoodsSum").value;//商品统计:
	var idyuanfeihangno = $.trim($("#idyuanfeihangno").val());//原飞航单号
	if(outMethod==0){
		var idproduct = document.all("idproduct");
		if(idproduct==null){
			alert("请添加申报信息！");
		} else {
			var prdktbn = "";
			for(var pb=1;pb<=idproduct.length;pb++){
				if(pb%6==0) {
					prdktbn = prdktbn + idproduct[pb-1].value + "//////";
				} else {
					prdktbn = prdktbn + idproduct[pb-1].value + "//";
				}
			}$.ajax({
				type : 'POST',
				url : '/cbtconsole/PurchaseServlet?action=OutPortNow&className=Purchase',
				data : {
					outmethod:outMethod,
					orderno:idorderno,
					transport:idtransport,
					weight:idweight,
					goodstype:idgoodstype,
					username:idusername,
					useremail:iduseremail,
					adminname:idadminname,
					usercompany:idusercompany,
					admincompany:idadmincompany,
					userzone:iduserzone,
					adminzone:idadminzone,
					usercode:idusercode,
					admincode:idadmincode,
					userstate:iduserstate,
					usercity:idusercity,
					userstreet:iduserstreet,
					useraddress:iduseraddress,
					adminaddress:idadminaddress,
					userphone:iduserphone,
					adminphone:idadminphone,
					userid:iduserid,
					remark:idremark,
					prdktbn:prdktbn,
					idadminsprovince:idadminsprovince,
					idadmincity:idadmincity,
					idgoodssum:idGoodsSum,
					idyuanfeihangno:idyuanfeihangno
				},
				success : function(outState) {
					if(outState==1||outState==2||outState==3){
						alert("勾选订单出货失败！");
					} else if(outState==5){
						alert("没有原飞航或单号！");
					} else if(outState==6){
						alert("包含单号已经出库！");
					} else {
						alert("勾选订单已出货成功！请自行刷新页面以便确认！");
					}
				}
			});
		}
	} else if(outMethod==1){ //原飞航出货，显示打印订单
		if($.trim($("#idyuanfeihangno").val())==""){//原飞航单号
			alert("没有原飞航单号！");
		} else {
			document.getElementById("yuanfeihangdiv").style.display = 'block';
			document.getElementById("adminna").innerHTML = document.getElementById("idadminname").value;
			document.getElementById("userna").innerHTML = document.getElementById("idusername").value;
			document.getElementById("adminph").innerHTML = document.getElementById("idadminphone").value;
			document.getElementById("admincomp").innerHTML = document.getElementById("idadmincompany").value;
			document.getElementById("adminaddre").innerHTML = document.getElementById("idadminaddress").value;
			document.getElementById("admincit").innerHTML = document.getElementById("idadmincity").value;
			document.getElementById("adminzon").innerHTML = document.getElementById("idadminzone").value;
			document.getElementById("admincod").innerHTML = document.getElementById("idadmincode").value;
			document.getElementById("usernam").innerHTML = document.getElementById("idusername").value;
			document.getElementById("userph").innerHTML = document.getElementById("iduserphone").value;
			document.getElementById("usercmp").innerHTML = document.getElementById("idusercompany").value;
			document.getElementById("ueseraddre").innerHTML = document.getElementById("iduseraddress").value;
			document.getElementById("usercit").innerHTML = document.getElementById("idusercity").value;
			document.getElementById("userzon").innerHTML = document.getElementById("iduserzone").value;
			document.getElementById("usercod").innerHTML = document.getElementById("idusercode").value;
		}
	} else {
		var express_no = $("#express_no").val();
		var logistics_name = $("#logistics_name").val();
		var logistics = $("#logistics").val();
		$.ajax({
			type:'POST',
			url:'/cbtconsole/PurchaseServlet?action=OutPortNow&className=Purchase',
			data:{
				outmethod:outMethod,
				orderno:idorderno,
				remark:idremark,
				express_no:express_no,
				logistics_name:logistics_name
			},
			success : function(outState) {
				if(outState==1||outState==2||outState==3){
					alert("勾选订单出货失败！");
				} else if(outState==5){
					alert("没有原飞航或单号！");
				} else if(outState==6){
					alert("包含单号已经出库！");
				} else {
					alert("勾选订单已出货成功！请自行刷新页面以便确认！");
				}
			}
		});
	}
}
function FnGoodsTotal(){
	var goodsnum = document.all("idproductid");
	var total = 0;
	if(goodsnum.length>1){
		for(var gs=0;gs<goodsnum.length;gs++){
			total = total + parseInt(goodsnum[gs].value);
		}
		document.getElementById("idGoodsSum").value = total;
	} else {
		document.getElementById("idGoodsSum").value=document.getElementById("idproductid").value;
	}
}
function FnPrint(obj){
	if(!confirm("确定打印？")){
		alert("已取消!");
	} else {
		var newWindow=window.open("打印窗口","_blank");
	    var docStr = obj.innerHTML;
		newWindow.document.write(docStr);
	    newWindow.document.close();
	    newWindow.print();
	    newWindow.close();
		var outMethod = document.getElementById("outMethod").value;
		var idorderno = document.getElementById("idorderno").value;
		var idtransport = document.getElementById("idtransport").value;
		var idweight = document.getElementById("idweight").value;
		var idgoodstype = document.getElementById("idgoodstype").value;
		var idusername = document.getElementById("idusername").value;
		var iduseremail = document.getElementById("iduseremail").value;
		var idadminname = document.getElementById("idadminname").value;
		var idusercompany = document.getElementById("idusercompany").value;
		var idadmincompany = document.getElementById("idadmincompany").value;
		var iduserzone = document.getElementById("iduserzone").value;
		var idadminzone = document.getElementById("idadminzone").value;
		var idusercode = document.getElementById("idusercode").value;
		var idadmincode = document.getElementById("idadmincode").value;
		var iduserstate = document.getElementById("iduserstate").value;
		var idusercity = document.getElementById("idusercity").value;
		var iduserstreet = document.getElementById("iduserstreet").value;
		var iduseraddress = document.getElementById("iduseraddress").value;
		var idadminaddress = document.getElementById("idadminaddress").value;
		var iduserphone = document.getElementById("iduserphone").value;
		var idadminphone = document.getElementById("idadminphone").value;
		var iduserid = document.getElementById("iduserid").value;
		var idremark = document.getElementById("idremark").value;
		var idadminsprovince = document.getElementById("idadminsprovince").value;
		var idadmincity = document.getElementById("idadmincity").value;
		var idGoodsSum = document.getElementById("idGoodsSum").value;
		var idyuanfeihangno = document.getElementById("idyuanfeihangno").value;//原飞航单号
// 		var idproduct = document.all("idproduct");
// 		var prdktbn = "";
// 		for(var pb=1;pb<=idproduct.length;pb++){
// 			if(pb%6==0) {
// 				prdktbn = prdktbn + idproduct[pb-1].value + "//////";
// 			} else {
// 				prdktbn = prdktbn + idproduct[pb-1].value + "//";
// 			}
// 		}
		if(!confirm("点击【确定】，确定出货；点击【取消】，取消出货。")){
			alert("已取消出货!");
		} else {
			$.ajax({
				type : 'POST',
				url : '/cbtconsole/PurchaseServlet?action=OutPortNow&className=Purchase',
				data : {
					outmethod:outMethod,
					orderno:idorderno,
					transport:idtransport,
					weight:idweight,
					goodstype:idgoodstype,
					username:idusername,
					useremail:iduseremail,
					adminname:idadminname,
					usercompany:idusercompany,
					admincompany:idadmincompany,
					userzone:iduserzone,
					adminzone:idadminzone,
					usercode:idusercode,
					admincode:idadmincode,
					userstate:iduserstate,
					usercity:idusercity,
					userstreet:iduserstreet,
					useraddress:iduseraddress,
					adminaddress:idadminaddress,
					userphone:iduserphone,
					adminphone:idadminphone,
					userid:iduserid,
					remark:idremark,
// 					prdktbn:prdktbn,
					idadminsprovince:idadminsprovince,
					idadmincity:idadmincity,
					idgoodssum:idGoodsSum,
					idyuanfeihangno:idyuanfeihangno
				},
				success : function(outState,obj) {
					if(outState==1||outState==2||outState==3){
						alert("勾选订单出货失败！");
					} else if(outState==5){
						alert("没有原飞航或单号！");
					} else if(outState==6){
						alert("包含单号已经出库！");
					} else {
						alert("勾选订单已出货成功！请自行刷新页面以便确认！");
					}
				}
			});
		}
	}
}
function FnSetOutMethod(mt){
	document.getElementById("outMethod").value=mt;
	if(mt==0){
		$("#otherMethoddiv").css("display","none"); //其他出货方式
		$("#wuliudiv").css("display","block"); //4px
		$("#remind").css("display","none");   //原飞航
		$("#idgoodstype").css("display","block");
		$("#jcCargoType").css("display","none");
	}else if(mt==1){
		$("#otherMethoddiv").css("display","none");
		$("#wuliudiv").css("display","block");
		$("#remind").css("display","block");
	} else if(mt==2){
		$("#otherMethoddiv").css("display","block");
		$("#wuliudiv").css("display","none");
	}else if(mt==3){
		$("#otherMethoddiv").css("display","none");
		$("#wuliudiv").css("display","block");
		$("#remind").css("display","none");   //原飞航
		$("#idgoodstype").css("display","none");
		$("#jcCargoType").css("display","block");
	//	alert($("#idgoodstype option:selected").val());
	}
}
function getCodeId(value){
	$("#logistics_name").val(value);
}
function FnCheckBtn(val){
	var chks = document.all('chkk');
	var j = chks.length;
	var wuliu = "";
	if(j > 1){
		for(var i = 0; i < j; i++) {
			if(chks[i].checked==true){
				wuliu = wuliu + chks[i].value + ",";
			}
		}
	} else {
		wuliu = document.getElementById("chkk").value + ",";
	}
	var value = $.trim(val);
	if(value!=null&&value!=""&&wuliu.length!=0){
		document.getElementById("btn_idother").disabled = false;
	} else {
		document.getElementById("btn_idother").disabled = true;	
	}
}
//添加shenbao
function FnInnerTr(){
	var _len = $("#innerTr tr").length;
	$("#innerTr").append("<tr id='"+_len+"'><td width='15%' style='background: #FFCC99; color: #000;'><span>品名(中):</span></td>"
 		+"<td width='50%' colspan='2'><span><input type='text' id='' name='idproduct' style='width: 300px;height: 14px;'/></span></td>"
		+"</tr><tr id='"+_len+"'><td ><span>品名(英):</span></td><td><span><textarea id='' name='idproduct' rows='2' cols='30' class='fff'></textarea></span><br/></td>"
		+"<td><span>配货备注:&nbsp;&nbsp;&nbsp;&nbsp;<a href='#' onclick='FnDeleteTr("+_len+")'>删除申报</a></span><span>"
		+"<textarea id='' name='idproduct' rows='1' cols='20' class='fff'></textarea></span><br /></td></tr><tr id='"+_len+"'><td style='border-bottom-color:red;  border-bottom-width:3px;'><span>数量:</span></td>"
		+"<td colspan='2' style='border-bottom-color:red;  border-bottom-width:3px;'><span><input type='text' id='idproductid' name='idproduct' value='1' onblur='FnGoodsTotal();' style='width: 80px;height: 14px;'/></span>"
		+"<span>价格:</span><span><input type='text' id='' name='idproduct' value='' style='width: 80px;height: 14px;' /></span>"
		+"<span>单位:<input type='text' id='' name='idproduct' value='' style='width: 40px;height: 14px;' /></span>&nbsp;&nbsp;&nbsp;&nbsp;"
		+"<a href=\"#oranges\"><input type=\"button\" value=\"添加申报\" onclick=\"FnInnerTr();\" style=\" width: 70px;height: 23px;background: wheat;\"></a></td></tr>");
}
function FnDeleteTr(vas){
	$("tr[id='"+vas+"']").remove();
}
</script>
<body><div>
<div id="yuanfeihangdiv" class="mod_pay3" >
<div class='show_x'><a href='javascript:void(0)' onclick='alertdivHide();' class='h'>×</a></div>
<div id="printdiv" style="width: 750px;height: 400px;top: 20;left: 23%;">
	<div style="padding-top: 6%; padding-left: 3%;height:20px;">
		<span style="font-size: 14px;font-family: 宋体;" id="adminna"></span>
		<span style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 8%" id="userna"></span></div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;height:20px;" id="adminph" ></div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;height:20px;" id="admincomp" ></div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;width: 250px; height: 40px; word-wrap:break-word;" id="adminaddre"></div>
	<div style="padding-top: 0.4%; padding-left: 3%;height:20px;">
		<span style="font-size: 14px;font-family: 宋体;height:20px;" id="admincit" ></span>
		<span style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;height:20px;" id="adminzon" ></span>
		<span style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 7%;height:20px;" id="admincod" ></span></div>
	<br><br>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 2%; padding-left: 3%;height:20px;" id="usernam" ></div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;height:20px;" id="userph" ></div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;height:20px;" id="usercmp" >公司名称</div>
	<div style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 3%;width: 250px; height: 40px; word-wrap:break-word;" id="ueseraddre"></div>
	<div style="padding-top: 0.4%; padding-left: 3%;height:20px;">
		<span style="font-size: 14px;font-family: 宋体;height:20px;" id="usercit" >城市</span>
		<span style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 5%;height:20px;" id="userzon" >国家</span>
		<span style="font-size: 14px;font-family: 宋体;padding-top: 0.4%; padding-left: 8%;height:20px;" id="usercod" ></span></div>
</div>
<input type="button" value="打印" onclick="FnPrint(document.getElementById('printdiv'));">
</div>

	<div id="alertdiv" class="mod_pay2"></div>
	<div id="bigestdiv" class="mod_pay1" onclick="alertdivHide();"></div>
		<div>
		<div class="style">
			<div>
				<span class="e">当天出货列表</span>
				<span>出货方式:
						<input type="radio" name="outMethodd" checked="checked" value="0" onclick="FnSetOutMethod(0);">4PX
								<input type="radio" name="outMethodd" value="1" onclick="FnSetOutMethod(3);">佳成
						<input type="radio" name="outMethodd" value="1" onclick="FnSetOutMethod(1);">原飞航
						<input type="radio" name="outMethodd" value="2" onclick="FnSetOutMethod(2);">其他
						<input type="hidden" id="outMethod" value="0">
				</span>
			</div>
		</div>
		<div>可出货用户ID：</div>
		<div class="aaa">
	        <span>
	        	<c:if test="${empty idlist}">
		        	<div><font style="font-size: 24px" >没有可出货ID！</font></div>
				</c:if>
				<c:if test="${not empty idlist}">
					<c:forEach items="${idlist}" var="id">
		        		<input type="button" value="${id.id}" onclick="FnOutId('${id.id}');" class="f">
	        		</c:forEach>
	        	</c:if>
	        </span>
	        <span><input type="hidden" id="hiddenUserid" ></span>
		</div>
		<div class="bb">对应订单：<span>
		</span>
		</div>
		<div class="bbb">
			<div id="hiddenOrderDiv"></div>
		</div>
	</div>
	<div class="ccc">
	<div id="otherMethoddiv" style="display: none;font-size: 12px">其他出货方式
		<table id="tab_forwarder" border="1px" width="100%">
			<tr>
				<td>快递跟踪号：</td>
				<td><input  id="express_no" name="express_no" type="text" onblur="FnCheckBtn(this.value)"></td>
			</tr>
			<tr>
				<td>物流公司名称：</td>
				<td>
					<input id="logistics_name" name="logistics_name" type="text" disabled="disabled"></td>
				<td><select id="logistics" style="width:150px" onchange="getCodeId(this.value)">
						<option selected="selected">--选择--</option>
		             <c:forEach var="logisticsList" items="${logisticsList}" >
		               	<option value="${logisticsList.codeId}">${logisticsList.codeName}</option>
		             </c:forEach>
		          </select>
				</td>
			</tr>
			<tr>
				<td colspan="3"><input type="button" id="btn_idother" onclick="FnCreateFpx();" value="出&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;货" disabled="disabled" style="width: 100%; height:50px; background: wheat; font-size: 24px;"></td>
			</tr>
		</table>
	</div>
	<div id="wuliudiv">
		<table width="100%" border="1">
			<tr align="center">
				<td colspan="2">
					<input type="button" id="buttoniidd" value="出&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;货" disabled="disabled" style="width: 100%; height:50px; background: wheat; font-size: 24px;">
				</td>
			</tr>
			<tr>
				<td width="50%" style="background: #FFCC99; color: #000"><div>基本信息：</div></td>
				<td width="50%"><div id="remind" style="display: none;">原飞航单号：<font color="red">(原飞航出货必填)</font><input type="text" class="ff" readonly="readonly"/></div></td>
			</tr>
			<tr>
				<td><span>客户单号:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
				<td><span>运输方式:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td><span>商品重量:</span>
					<span><input type="text" id="" name="exportname" value="" style="width: 110px;height: 14px;" />kg</span>
				</td>
				<td><span>货物类型:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span>
				</td>
			</tr>
			<tr>
				<td style="background: wheat; color: #000"><div>收件人信息:</div></td>
				<td style="background: #FFCC99; color: #000"><div>寄件人信息:</div></td>
			</tr>
			<tr>
				<td>
					<span>姓名:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
					<span>Email:</span>
					<span><input type="text" id="iduseremail" name="exportname" value="${logistics.email}" class="ff" /></span><br />
				</td>
				<td>
					<span>姓名:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td><span>公司:</span>
				<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
			</td>
			<td><span>公司:</span>
				<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
			</td>
			</tr>
			<tr>
				<td><span>国家:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
				<td><span>国家:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td><span>邮编:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
				<td><span>邮编:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td><span>州/省:</span>
					<span><input type="text" id="" name="exportname" value="" style="width: 100px;height: 14px;" /></span><br />
					<span>市:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
					<span>街道:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
				<td><span>州/省:</span>
					<span><input type="text" id="" name="exportname" value="" style="width: 100px;height: 14px;" /></span><br />
					<span> 市:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td><span>地址:</span>
					<span><textarea id="" name="exportname" rows="2" cols="22"></textarea></span><br />
				</td>
				<td><span>地址:</span>
					<span><textarea id="" name="exportname" rows="2" cols="22"></textarea></span><br />
				</td>
			</tr>
			<tr>
				<td><span>电话:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
				<td><span>电话:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td><span>买家ID:</span>
					<span><input type="text" id="" name="exportname" value="" class="ff" /></span><br />
				</td>
			</tr>
			<tr>
				<td style="background: #FFCC99; color: #000" colspan="2"><div>海关申报信息:</div></td>
			</tr>
			<tr>
				<td colspan="2">
					<table border="1" style="width: 100%">
						<tr>
							<td width="20%" style="background: #FFCC99; color: #000"><span>品名(中):</span></td>
							<td width="40%" colspan="2"><span><input type="text" id="" name="idproduct" style="width: 300px;height: 14px;" /></span><br /></td>
						</tr>
						<tr>
							<td><span>品名(英):</span></td>
							<td><span><textarea id="" name="idproduct" rows="2" cols="15"></textarea></span><br /></td>
							<td><span>配货备注:</span>
								<span><textarea id="" name="idproduct" rows="1" cols="15"></textarea></span><br /></td>
						</tr>
						<tr>
							<td><span>商品数量:</span></td>
							<td colspan="2"><span><input type="text" id="idproductid" name="idproduct" value="" onblur="FnGoodsTotal();" style="width: 80px;height: 14px;" /></span>
							<span>价格:</span>
							<span><input type="text" id="" name="idproduct" value="" style="width: 80px;height: 14px;" /></span>
							<span>单位:<input type="text" id="" name="idproduct" value="" style="width: 40px;height: 14px;" /></span><br /></td>
						</tr>
						<tr>
							<td style="background: #FFFFAA; color: #000">备注:<br /></td>
							<td><div id="exportnameremark"></div><input type="hidden" id="idremark" name="exportname" value=""></td>
							<td><span>商品统计:</span><span><input type="text" id="idGoodsSum" name="exportname" value=""></span></td>
						</tr>
					</table>
				</td>
			</tr>
		</table></div>
	</div>
	<div class="ddd">
		<div id="orderdetailsdiv">
		<c:if test="${empty uiblist}">
			<div>
				<font style="font-size: 24px">没有相应数据！</font>
			</div>
		</c:if>
		<c:if test="${not empty uiblist}">
		<c:forEach items="${uiblist}" var="uib">
		<div style="background: #FFCC99; color: #000">
			<span class="ee">客户ID：${uib.id}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
			<input type="hidden" id="chk" name="kehuID" checked="checked" value="${uib.id}<br/>客户Email：${uib.email}">
			<span class="ee">客户Email：${uib.email}</span>
		</div>
		<c:if test="${empty uib.purchaseBean}">
			<div>
				<font style="font-size: 24px">没有相应数据！</font>
			</div>
		</c:if>
		<c:if test="${not empty uib.purchaseBean}">
		<c:forEach items="${uib.purchaseBean}" var="pb">
		<div id="iidd_${pb.orderNo}" style="display: none">
			<div>
				<div>
					<input type="hidden" id="hidorder" value="${pb.orderNo}">
					<span class="d">订单号：</span><span class="c">${pb.orderNo}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<span class="d">交期：</span><span class="c">${pb.deliveryTime}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<span class="d">付款日期：</span><span class="c">${pb.paytime}</span><br/>
					<span class="d">订单地址：</span><span class="cc">${pb.orderaddress}</span>
					<input type="hidden" id="${pb.orderNo}" value="${pb.orderaddress}">
				</div>
				<table id="table" align="center" border="1px"style="font-size: 13px;" width="100%" bordercolor="gray">
					<tr>
						<td width="15%">商品编号/订单信息</td>
						<td width="25%">商品信息</td>
						<td width="35%">名称</td>
						<td width="25%">客户信息</td>
					</tr>
					<c:if test="${not empty pb.purchaseDetailsBean}">
					<c:forEach items="${pb.purchaseDetailsBean}" var="pbpdb">
					<tr>
						<td>
							<div>
								<span>商品号：</span>
								<span>${pbpdb.goodsid}</span><br>
								<span>编码：</span>
								<span>${pbpdb.goodsdata_id}</span>
							</div> 
						</td>
						<td>
							<div><a href="${pbpdb.goods_url}" target="_block">
								<img src="${pbpdb.googs_img}" width="50px" height="50px" /></a></div>
							<div><table><tr><td>Type：</td><td>${pbpdb.goods_type}</td></tr></table></div>
						</td>
						<td>${pbpdb.goods_title }</td>
						<td>
							<div>
								<span>Price：</span>
								<span>${pbpdb.goods_price}&nbsp;${pbpdb.currency}/piece</span>
							</div>
							<div>
								<span>Quantity：</span>
								<span>${pbpdb.googs_number}</span>
							</div>
							<div>
								<span>Remark：</span>
								<span>${pbpdb.remark}</span>
							</div>
						</td>
					</tr>
					</c:forEach>
					</c:if>
				</table>
				<div>&nbsp;</div>
			</div></div>
		</c:forEach>
		</c:if>
		</c:forEach>
		</c:if>
		</div>
	</div>
	${hideorder}
	</div>
	
</body>

</html>