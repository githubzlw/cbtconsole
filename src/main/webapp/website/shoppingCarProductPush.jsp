<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.min.js"></script>
<title>购物车商品推送</title>
<style type="text/css">
*{padding:0;margin:0;}
.recontd {
	width: 160px;
	padding: 2px;
}

.rectable td {
	border: 1px solid #ddd;
}

.recondiv {
	width: 150px;
	position: relative;
}

.recontit {
	width: 150px;
	overflow: hidden;
	font-size: 12px;
	height: 42px;
	position: relative;
	text-align: left;
}

.recontit:after {
	content: "…";
	display: block;
	width: 12px;
	height: 12px;
	position: absolute;
	right: 0;
	bottom: 0;
	background: #fff;
	z-index: 1;
}

.reconpri {
	text-align: left;
	color: red;
    font-weight: bold;
}

.reconpri1, .reconpri2 {
	display: block;
}

.reconpri1 {
	float: left;
	width: 80%;
}

.reconpri2 {
	float: right;
	width: 20%;
	color: #000;
	text-decoration: none;
}


#showEmaildiv table td{border:0.5px solid } 
</style>
</head>
<script type="text/javascript">
	var datalength = 0;

	function loadGoodCarProduct() {
		$('#sendEmailbtn').hide();
		$.post("/cbtconsole/WebsiteServlet?action=getShopCarList&className=shoppingCartManagement",
						{
							userid : '${param.userid}'
						},
						function(res) {
							datalength = res.length; 
							var result = document.getElementById("result");
							if (result.getElementsByTagName("tr").length > 1) {
								result.innerHTML = "<tr>"
										+ result.getElementsByTagName("tr")[0].innerHTML
										+ "</tr>";
							}
							var json = eval(res);
							
							for (var i = 0; i < json.length; i++) {
								$("#result tr:eq(" + i + ")")
										.after('<tr></tr>');
								$("#result tr:eq(" + (i + 1) + ")").append("<td style='width:200px' class='findid'>购物车ID：" + json[i][25] +"<input type='hidden' id='goodscarid' value='"+json[i][25]+"'/><br/>"+
										"<p>原工厂价:<strong style='color:blue;' id='"+json[i][25]+"sourceprice'>"+json[i][3]+"</strong> USD</p>"+
										"<p style='color:red;'>上次修改工厂价:<strong id='"+json[i][25]+"preFactoryprice'>"+json[i][36]+"</strong> USD</p>"+
										"<p style='margin-top:5px;'>新工厂价:<input id='"+json[i][25]+"NewPrice' type='text' style='width:50px' disabled='true' onchange='Calcrate("+json[i][25]+",this.value)'>&nbsp;"+json[i][4]+"</p>"+
										/* "<p>体积重："+(Math.round(parseFloat(json[i][32])*100)/100)+" m³<br/>总重量："+(Math.round(parseFloat(json[i][31])*100)/100)+"<br/>"+ */
										"<p>运费:<span id='"+json[i][25]+"freight' style='color:blue;font-weight:bold;'>"+json[i][33]+"</span> USD<br/>"+
										"<p>体积重："+(Math.round(parseFloat(json[i][32])*100)/100)+" m³<br/>"+
										/* "修改总重量：<input type='text' style='width:50px' value='"+(Math.round(parseFloat(json[i][31])*100)/100)+"' onchange='updateWeight("+json[i][25]+",this.value,"+ json[i][26] +")'></p>"+ */
										"<p>利润率:<span id='"+json[i][25]+"rate' style='color:red;font-weight:bold;'></span></p><p>用户节省:<span id='"+json[i][25]+"save' style='color:red;font-weight:bold;'></span></p>"+
										"<br><p>是否优惠商品：<label style='color:red;'>"+(json[i][28]>0 ? '是':'否')+"</label><br/>"+
										"折扣类别："+json[i][34]+"<br/>"+
										"折扣比率：<label style='color:red;'>"+(json[i][30]>0 ? json[i][30] : '--')+"</label><br>"+
										"优惠限额：<label style='color:red;'>"+(json[i][30]>0 ? json[i][29] : '--')+"</label> USD<br>"+
										"</p></td>");
								
								$("#result tr:eq("+(i+1)+")").append('<td id="'+json[i][25]+'number">'+ json[i][26] +'</td>');
								
								var img ="";
								if (json[i][24]!="" && json[i][24] !=null &&json[i][24]!="null") {
									 img = '<img class="lazy" src="'+json[i][24]+'" data-original="'+json[i][24]+'" width = "50" height = "50"></img>';
								}else if(json[i][2]!="" && json[i][2] != null && json[i][2] !="null"){
									 img = '<span>'+json[i][2] +'</span>';
								}
								
								img +='</div></p><p class="recontit">'+ json[i][1]+ '</p></div></td>';
								
								var preferential = "";
								if (json[i][28] != 0) {
									preferential = "<span class='reconpri' style='border:1px #03B4D2 solid; color:#03B4D2;    font-weight: 300; font-size: 12px;padding: 2px;'>批量优惠</span>";
								}
								
								$("#result tr:eq(" + (i + 1) + ")")
										.append(
												'<td class="recontd"><div class="recondiv" style="width:180px"><img class="lazy" src="'+json[i][5]+'" data-original="'+json[i][5]+'" width = "150" height = "150" /></img><p class="reconpri"><span>'
														+ json[i][4] + '</span>:<span id="'+ json[i][25] +'ori">' + json[i][3] + '</span>&nbsp;RMB:<span>'+ json[i][27]
														+ '</span><p style="clear:both;">'+preferential+'<a href="'+json[i][6]+'" target="_blank" style="color:blue;font-weight:bold;">Info</a></p></span><div style="clear:both;">'+ img +'');
								//<a href="'+json[i][6]+'" class="reconpri2" target="_blank">Info</a>
								//'<img src="'+json[i][24]+'" width = "50" height = "50"></img></div></p><p class="recontit">'+ json[i][1]+ '</p>');
		
								
		
								//抓取第一个商品
								if(json[i][9]==null && json[i][8] == 0 && json[i][7] == null){
									$("#result tr:eq("+(i+1)+")").append('<td>无</td>');
								}else{
									$("#result tr:eq(" + (i + 1) + ")").append('<td class="recontd"><div id="'+json[i][25]+'s1" class="recondiv" onclick="divClick(this.id,event)"><img class="lazy" src="'+json[i][9]+'" data-original="'+json[i][9]+'" width = "150" height = "150"></img><p class="reconpri"><span class="reconpri1">'
									+ json[i][4]+'&nbsp;<span id="'+json[i][25]+'cp1">'
									+ json[i][8]
									+ '</span></span><a href="'+json[i][10]+'" class="reconpri2" target="_blank" style="color:blue">Info</a><div style="clear:both;"></div></p><p class="recontit">'
									+ json[i][7]
									+ '</p><span id="'+json[i][25]+'c1" style="color:red;visibility: hidden">√已选中该货源商品</span></div></td>');
								}
								//抓取第二个商品
								if(json[i][13]==null && json[i][12] == 0 && json[i][11] == null){
									$("#result tr:eq("+(i+1)+")").append('<td>无</td>');
								}else{
									$("#result tr:eq(" + (i + 1) + ")").append('<td class="recontd"><div id="'+json[i][25]+'s2" class="recondiv" onclick="divClick(this.id,event)"><img class="lazy" src="'+json[i][13]+'" data-original="'+json[i][13]+'" width = "150" height = "150" ></img><p class="reconpri"><span class="reconpri1">'
									+ json[i][4]+'&nbsp;<span id="'+json[i][25]+'cp2">'
									+ json[i][12]
									+ '</span></span><a href="'+json[i][14]+'" class="reconpri2" target="_blank" style="color:blue">Info</a><div style="clear:both;"></div></p><p class="recontit">'
									+ json[i][11]
									+ '</p><span id="'+json[i][25]+'c2" style="color:red;visibility: hidden">√已选中该货源商品</span></div></td>');
								}
								//抓取第三个商品
								if(json[i][17]==null && json[i][16] == 0 && json[i][15] == null){
									$("#result tr:eq("+(i+1)+")").append('<td>无</td>');
								}else{
									$("#result tr:eq(" + (i + 1) + ")").append('<td class="recontd"><div id="'+json[i][25]+'s3" class="recondiv" onclick="divClick(this.id,event)"><img class="lazy" src="'+json[i][17]+'" data-original="'+json[i][17]+'" width = "150" height = "150" ></img><p class="reconpri"><span class="reconpri1">'
									+ json[i][4]+ '&nbsp;<span id="'+json[i][25]+'cp3">'
									+ json[i][16]
									+ '</span></span><a href="'+json[i][18]+'" class="reconpri2" target="_blank" style="color:blue">Info</a><div style="clear:both;"></div></p><p class="recontit">'
									+ json[i][15]
									+ '</p><span id="'+json[i][25]+'c3" style="color:red; visibility: hidden">√已选中该货源商品</span></div></td>');
								}
								//抓取第四个商品
								if(json[i][21]==null && json[i][20] == 0 && json[i][19] == null){
									$("#result tr:eq("+(i+1)+")").append('<td>无</td>');
								}else{
									$("#result tr:eq(" + (i + 1) + ")").append('<td class="recontd"><div id="'+json[i][25]+'s4" class="recondiv" onclick="divClick(this.id,event)"><img class="lazy" src="'+json[i][21]+'" data-original="'+json[i][21]+'" width = "150" height = "150" ></img><p class="reconpri"><span class="reconpri1">'
									+ json[i][4]+'&nbsp;<span id="'+json[i][25]+'cp4">'
									+ json[i][20]
									+ '</span></span><a href="'+json[i][22]+'" class="reconpri2" target="_blank" style="color:blue">Info</a><div style="clear:both;"></div></p><p class="recontit">'
									+ json[i][19]
									+ '</p><span id="'+json[i][25]+'c4" style="color:red;visibility: hidden">√已选中该货源商品</span></div></td>');
								}
								
								//已购买商品源
								var sourceList = json[i][23];
								if (sourceList == "") {
									$("#result tr:eq("+(i+1)+")").append("<td colspan='2'>无</td>");
								}else{
									/* $("#result tr:eq(" + (i + 1) + ")").append('<td><table><tr><td>123</td></tr></table></td>'); */
									 for (var j = 0; j < sourceList.length; j++) {
										$("#result tr:eq(" + (i + 1) + ")").append('<td class="recontd"><div id="'+json[i][25]+'d'+j+'" class="recondiv" onclick="divClick(this.id,event)"><img class="lazy" src="'+sourceList[j][2]+'" data-original="'+sourceList[j][2]+'" width = "150" height = "150"></img><p class="reconpri"><span class="reconpri1">'
										+ json[i][4]+'&nbsp;<span id="'+json[i][25]+'np'+j+'">'
										+ sourceList[j][5]
										+ '</span></span><a href="'+sourceList[j][3]+'" class="reconpri2" target="_blank" style="color:blue">Info</a><div style="clear:both;"></div></p><p class="recontit">'
										+ sourceList[j][1]
										+ '</p><span id="'+json[i][25]+'n'+j	+'" style="color:red;visibility: hidden">√已选中该货源商品</span></div></td>');
									}
									/* $("#result tr:eq(" + (i + 1) +  ")").append('</tr></td>'); */
								}
							}
							
							$("#counto").html(json.length);
						});
	}
	
	Array.prototype.remove=function(dx) 
	{ 
	    if(isNaN(dx)||dx>this.length){return false;} 
	    for(var i=0,n=0;i<this.length;i++) 
	    { 
	        if(this[i]!=this[dx]) 
	        { 
	            this[n++]=this[i] 
	        } 
	    } 
	    this.length-=1 
	};
	/* Array.prototype.remove = function(val) {
		var index = this.indexOf(val);
		if (index > -1) {
		this.splice(index, 1);
		}
		}; */
	Array.prototype.indexOf = function(val) {
		for (var i = 0; i < this.length; i++) {
		if (this[i] == val) return i;
		}
		return -1;
		};


/* 存储选择的商品信息 */
var caridList = new Array(); //购物车id
var selecList = new Array(); //选中divid
var oripriceList = new Array(); //商品原价
var selpriceList = new Array(); //选中商品价格
var newpriceList = new Array(); //新价格
function divClick(val,e) {
	
	//抓取商品信息
	//var evt = e||window.event;
	//alert(e.target.id);
	if($(e.target).attr('class')=='reconpri2'){
		return;
	};
	var carid = val.substr(0,val.length -2);//获取购物车id
	var freight = parseFloat($('#'+carid+'freight').text());//运费
	var checksort = val.substr(val.indexOf('s'), val.length);//淘宝货源
	var sourcesort = val.substr(val.indexOf('d'), val.length);//已购买货源
	var price = parseFloat($('#'+val).find("span:eq(1)").text());//选中商品价格
	var oriprice = parseFloat($('#'+carid+'ori').text());//电商网站上商品的原价
//	var rateprice = parseFloat((price*1.2).toFixed(2));//货源价+20%利率后的价格
	var rateprice = parseFloat((((price+freight)*1.2)-freight).toFixed(2));//货源价+20%利率后的价格
	
	//var rateprice = parseFloat(price.toFixed(2));
	var saverate = parseFloat(((1-(rateprice+freight)/(oriprice+freight))*100).toFixed(2));//用户节省
	if (selecList.length <= 0) {//未选择任何商品
		caridList.push(carid);
		selecList.push(val);//选中的商品id值
		oripriceList.push(oriprice);
		selpriceList.push(price);
		for(var i=1;i<=5;i++){
			$('#'+carid+'s'+i).find("span:eq(2)").css({'visibility' : 'hidden'});
			$('#'+carid+'NewPrice').attr("disabled",true);
		}
		$('#'+val).find("span:eq(2)").css({'visibility' : 'visible'});
		$('#'+carid+'NewPrice').attr("disabled",false);
		
		if (rateprice > oriprice) {//如果加上利率后比电商价格高则给电商价格
			$('#'+carid+'NewPrice').val(rateprice);
			$('#'+carid+'save').text(saverate+"%");
			$('#'+carid+'rate').text("20%");//选中初始利率
			$('#'+carid+'rate').css('color','red');
			$('#'+carid+'save').css('color','red');
			newpriceList.push(oriprice);
		}else{
			$('#'+carid+'NewPrice').val(rateprice);
			$('#'+carid+'save').text(saverate+"%");
//			$('#'+carid+'rate').text((((oriprice-price)/oriprice)*100).toFixed(2)+"%");//选中初始利率
			$('#'+carid+'rate').text("20%");//选中初始利率
			$('#'+carid+'rate').css('color','#00FF00');
			$('#'+carid+'save').css('color','#00FF00');
			newpriceList.push(rateprice);
		}
	}else{
		for (var k = 0; k < selecList.length; k++) {
			var selectedid = selecList[k];
			var selectedcarid = caridList[k];
			if (val == selectedid) {//已存在相同的商品
				var slectStr = selecList.indexOf(selectedid);
				selecList.remove(slectStr);
				selpriceList.remove(slectStr);
				caridList.remove(slectStr);
				oripriceList.remove(slectStr);
				newpriceList.remove(slectStr);
				$('#'+val).find("span:eq(2)").css({'visibility' : 'hidden'});
				$('#'+carid+'NewPrice').attr("disabled",true);
				$('#'+carid+'NewPrice').val('');
				$('#'+carid+'rate').text('');
				$('#'+carid+'save').text('');
				exist = true;
				break;
			}else if(val != selectedid && carid == selectedcarid){//同一行的商品,删除原有的,再重新添加
				var caridStr = caridList.indexOf(selectedcarid);
				selecList.remove(caridStr);
				selpriceList.remove(caridStr);
				caridList.remove(caridStr);
				oripriceList.remove(slectStr);
				newpriceList.remove(caridStr);
				$('#'+selectedid).find("span:eq(2)").css({'visibility' : 'hidden'});
				caridList.push(carid);
				selecList.push(val);
				selpriceList.push(price);
				oripriceList.push(oriprice);
				//newpriceList.push(price);
				$('#'+val).find("span:eq(2)").css({'visibility' : 'visible'});
				if (rateprice > oriprice) {//如果加上利率后比电商价格高则给电商价格
					$('#'+carid+'NewPrice').val(rateprice);
					$('#'+carid+'save').text(saverate+"%");
					$('#'+carid+'rate').text("20%");//选中初始利率
//					$('#'+carid+'rate').text((((oriprice-price)/oriprice)*100).toFixed(2)+"%");//选中初始利率
					newpriceList.push(oriprice);
					$('#'+carid+'rate').css('color','red');
					$('#'+carid+'save').css('color','red');
				}else{
					$('#'+carid+'NewPrice').val(rateprice);
					$('#'+carid+'save').text(saverate+"%");
//					$('#'+carid+'rate').text((((oriprice-price)/oriprice)*100).toFixed(2)+"%");//选中初始利率
					$('#'+carid+'rate').text("20%");//选中初始利率
					newpriceList.push(rateprice);
					$('#'+carid+'rate').css('color','#00FF00');
					$('#'+carid+'save').css('color','#00FF00');
				}
				exist = true;
				break;
			}else if(k == selecList.length-1){//当前商品未选择任何
				caridList.push(carid);
				selecList.push(val);//选中的商品id值
				oripriceList.push(oriprice);
				selpriceList.push(price);
				for(var i=1;i<=5;i++){
					$('#'+carid+'s'+i).find("span:eq(2)").css({'visibility' : 'hidden'});
					$('#'+carid+'NewPrice').attr("disabled",true);
				}
				$('#'+val).find("span:eq(2)").css({'visibility' : 'visible'});
				$('#'+carid+'NewPrice').attr("disabled",false);
				
				if (rateprice > oriprice) {//如果加上利率后比电商价格高则给电商价格
					$('#'+carid+'NewPrice').val(rateprice);
					$('#'+carid+'save').text(saverate+"%");
//					$('#'+carid+'rate').text((((oriprice-price)/oriprice)*100).toFixed(2)+"%");//选中初始利率
					$('#'+carid+'rate').text("20%");//选中初始利率
					newpriceList.push(oriprice);
					$('#'+carid+'rate').css('color','red');
					$('#'+carid+'save').css('color','red');
				}else{
					$('#'+carid+'NewPrice').val(rateprice);
					$('#'+carid+'save').text(saverate+"%");
//					$('#'+carid+'rate').text((((oriprice-price)/oriprice)*100).toFixed(2)+"%");//选中初始利率
					$('#'+carid+'rate').text("20%");//选中初始利率
					newpriceList.push(rateprice);
					$('#'+carid+'rate').css('color','#00FF00');
					$('#'+carid+'save').css('color','#00FF00');
				}
				break;
			}
		}
	}
	
}

//失焦修改重量
function updateWeight(carid, weight, number){
	if(isNaN(weight)){
		alert("请输入正确重量。");
		return ;
	}
	$.ajax({
		url : "/cbtconsole/WebsiteServlet?action=updateWeight&className=shoppingCartManagement&carid="+carid+"&weight="+weight+"&number="+number,
		type:"post",
		dataType:"text",
		success:function(data){
			if(data<0){
				alert("修改失败");
			}
		},
		error:function(){
			alert("Unknown Error");
		}
	});
}
 
var ncarids = ""; //改价专用 收集购物车id
var nprices = ""; //改价专用 收集新价格
 //新价格失去焦点事件 //更改价格
function Calcrate(carid,val) {
	 	var newPrice = parseFloat(val);
		var index = caridList.indexOf(carid);//下标
		var seletedPrice = selpriceList[index];//选中的价格
		var oriPrice = oripriceList[index];//原价
		var freight = parseFloat($('#'+carid+'freight').text());//运费
		
		var profit = (((newPrice - seletedPrice) / (newPrice + freight))*100).toFixed(2);
		
		var saverate = parseFloat(((1-(newPrice+freight)/(oriPrice+freight))*100).toFixed(2));//用户节省
		if (newPrice>=oriPrice || newPrice <= seletedPrice) {
			$('#'+carid+'rate').css('color','red');
			$('#'+carid+'save').css('color','red');
			$('#'+carid+'rate').text(profit+"%");
			$('#'+carid+'save').text(saverate+"%");
		}else{
			$('#'+carid+'rate').css('color','#00FF00');
			$('#'+carid+'save').css('color','#00FF00');
			$('#'+carid+'rate').text(profit+"%");
			$('#'+carid+'save').text(saverate+"%");
		}
		
		newpriceList.splice(index,1,newPrice);//替换价格
};

function updateGoodsPrice(){
	var userid = ${param.userid};
	 $("#sendEmailbtn").attr("disabled", true);
	if (caridList.length == 0 || selpriceList.length == 0) {
		alert("请为客户选择货源!");
		return;
	}else{
 		$.ajax({
			url : "/cbtconsole/WebsiteServlet?action=updateGoodsCarPrice&className=shoppingCartManagement&carid="+caridList.join(',')+"&price="+newpriceList.join(',')+"&userid="+userid,
			type : "POST",
			dataType:"text",
			success:function(data){
				if(parseInt(data)==0){
					alert("修改失败");
					$("#sendEmailbtn").removeAttr("disabled");
				}else{
					alert("修改价格成功!");
					sendEmail();
				}
			},
			error:function(){
				alert("修改失败");
				$("#sendEmailbtn").removeAttr("disabled");
			}
		}); 
	}
}

/* 发送邮件 */
var emailtitle;
var emailContent;
var emailremark;
 function sendEmail() {
	 $("#sendEmailbtn").attr("disabled", true);
	 emailtitle = $('#emailTitle').val();
	 emailremark = $('#emailremark').val();
	var goodscarid ="";
	$('.findid').each(function(){
		goodscarid += $(this).find('#goodscarid').val()+',';
	});
	$.post(
			"/cbtconsole/WebsiteServlet?action=sendMailToUser&className=shoppingCartManagement",
			{
				userid : '${param.userid}',
				caridList : caridList.join(','),
				selecList : selecList.join(','),
				priceList : selpriceList.join(','),
				newpriceList :newpriceList.join(','),
				emailtitle : emailtitle,
				emailremark : emailremark,
				goodscarid : goodscarid
			},
			function(res) {
				if (res.message == "1") {
					alert("邮件发送成功!");
					$("#sendEmailbtn").removeAttr("disabled");
					$('#showEmailTable').css("display","none");
					$("#sendEmailbtn").hide();
				}else{
					alert("邮件发送失败!");
					$("#sendEmailbtn").removeAttr("disabled");
				}
		});
};

/* 显示邮件 */
function showEmail() {
		$("#showEmailTable tbody tr:eq(1) td:eq(1)")[0].innerHTML="";
	 if (caridList.length == 0 || selpriceList.length == 0) {
		alert("请为客户选择货源!");
		return;
		alert(oripriceList);
	}else{
		$.post(
				"/cbtconsole/WebsiteServlet?action=showEmail&className=shoppingCartManagement",
				{
					userid : '${param.userid}',
					caridList : caridList.join(','),
					selecList : selecList.join(','),
					priceList : selpriceList.join(','),
					newpriceList :newpriceList.join(',')
				},
				function(res) {
					$('#showEmailTable').css("display","block");
					$('#sendEmailbtn').show();
					$('#emailContent').append(res.message);
					emailContent = res.message;
					window.scrollTo(0,document.body.scrollHeight);

					/* var sourceprice = $("#sourceprice").text();
					var newprices = $("#newprices").text();
					var count = 1;
					$("#oldprice").html("USD "+sourceprice);
					$("#totalsaveprice").html("<b>USD "+save+"</b>"); //共节省 */
				});
	}
};
	
</script>
<body onload="loadGoodCarProduct()">
	<div>
		<br> <br>
		<table id="result" border="1"
			style="margin-top:20px;text-align: center;border-collapse:collapse;" class="rectable">
			<tr class="jiaotrti">
				<td style="width: 180px">购物车ID</td>
				<td style="width: 80px">购买数量</td>
				<td style="width: 160px">用户购物车商品</td>
				<td style="width: 120px">抓取商品1</td>
				<td style="width: 120px">抓取商品2</td>
				<td style="width: 120px">抓取商品3</td>
				<td style="width: 120px">抓取商品4</td>
				<td style="width: 120px;" colspan="2">已购买货源</td>
			</tr>
		</table>
	</div><br/>
	<hr/>
	<div>
	<div id="showEmaildiv">
		<p style="height:30px">邮件预览</p>
		<table id="showEmailTable" border='0' style="display: none;cellspacing:10px;cellpadding:10px;">
			<tr><td>邮件标题:</td><td><input id="emailTitle" type="text" style="width:600px" value="We have reduced price for your ImportExpress shopping cart selection!"/></td></tr>
			<tr><td>邮件内容:</td><td id="emailContent"></td></tr>
			<tr><td>邮件备注:</td><td><textarea id="emailremark" style="width:600px;height:100px"></textarea></td></tr>
		</table>
		
	</div>
	<input id="sendEmailbtn" type="button" value="修改价格并发送邮件" style="position:fixed;top:55%;right:10px;float: left;width:130px;height:30px;" onclick="updateGoodsPrice()"/>&nbsp;&nbsp;
	<input id="showEmailbtn" type="button" value="生成邮件" style="position:fixed;top:50%;right:10px;width:120px;height:30px;" onclick="showEmail()"/>
	</div>
</body>
<script type="text/javascript">
$(function() {
    $(".lazy").lazyload({
    	effect:'fadeIn' 
    });
});
</script>
</html>