<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="/cbtconsole/img/mathematics1.ico" type="image/x-icon"/>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/main.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.min.js?self=true&skin=discuz"></script>
 <script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
</head>
<!-- begin：超链接下划线样式-->
<script type="text/javascript">

var pname="${fn:replace(spider.pName,'\"','\'\'')}"+1;
document.title = pname == 1 ? "import-express" : pname;
var pDescription_p = '${spider.pDescription}';
var infourl = '${spider.infourl}';
var info_ori = "${spider.flag}";
var free = "${spider.free}";
var email_ = "${email}";
</script>
<script type="text/javascript" src="/cbtconsole/js/spider-website.js"></script>
<body>
<h1 style="display:none">${spider.com}</h1>
<div class="bodycontainer">
<jsp:include page="main_top.jsp"></jsp:include>
<div id="light1" class="white_content" style="top:25%;border-radius:5px;">
      <div class="close" onclick="hide()" ><a href="javascript:void(0)" onmouseout="this.style.color='#000000'" onmousemove="this.style.color='#2296D8'">Close&nbsp;<img onclick="hide()" src="/cbtconsole/img/express_close.png"></a></div>
       <div class="con_1">
       		We give each new customer a $50 credit.  It can only be applied against shipping cost.
      	</div>
</div>
<div id="light2" class="white_content" style="top:25%;border-radius:5px;">
      <div class="close" onclick="hide()"><a href="javascript:void(0)" onclick="hide()"  onmouseout="this.style.color='#000000'" onmousemove="this.style.color='#2296D8'">Close&nbsp;<img onclick="hide()" src="/cbtconsole/img/express_close.png"></a></div>
       <br/>
       <div class="con_1">
       		If you want to ship this together with other products, please change shipping to <em id="con_1"></em>&nbsp;&nbsp;&nbsp;<button id="proceed" class="spiderproceed" onclick="fnProceed();">Proceed</button>
      	</div>
</div>
<div id="light4" class="white_content" style="top:25%;border-radius:5px;">
      <div class="close" onclick="hide(4)"><a href="javascript:void(0)" onclick="hide1(4)"  onmouseout="this.style.color='#000000'" onmousemove="this.style.color='#2296D8'">Close&nbsp;<img onclick="hide(4)" src="/cbtconsole/img/express_close.png"></a></div>
       <div class="con_1">
       		Thank you for contacting us, we will reply you shortly.  New discounted price can be found in your shopping cart in 2 days.
      	</div>
</div>
<div id="light3" class="white_content" style="top:25%;border-radius:5px;">
      <div class="close" onclick="hide(3)"><a href="javascript:void(0)" onclick="hide1(3)"  onmouseout="this.style.color='#000000'" onmousemove="this.style.color='#2296D8'">Close&nbsp;<img onclick="hide(3)" src="/cbtconsole/img/express_close.png"></a></div>
       <br/>
       <div class="con_1" style="width: 670px;margin-left: 50px;">
       		<ul class="spiderpreferential">
       			<li >Country:<div class="conshipmodiv" style="float: none;">
					<a class="contry_a1" id="contry_a1" style="margin-left: 91px;"  onclick="fnShowChengeC(1);"><img id="country_img1" src="/cbtconsole/img/country/36.jpg">&nbsp;&nbsp;<span id="chengeContry1">EAST OF USA</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="black_arrow1" style="text-align: right;">▼</span></a>
        		<input type="hidden" id="countryid1" value="36">
        		<div id="showContry1" class="conshowcotry" style="left: 157px;top: 65px;">
        			<ul id="showContry_ul1" class="conshowcotryul">
        				<li  class="contry_li"  onmousemove="this.className='contry_li ontry_li-hover'"  onmouseout="this.className='contry_li'" onclick="fnChangeCountry1('United States1');">
        					<span class="contrylispan">
        						&nbsp;<img src="/cbtconsole/img/country/36.jpg">&nbsp;&nbsp;<span style="font-size: 12px;line-height: 16px;"></span>&nbsp;
        						<input type="hidden" value="">
        					</span>
        				</li>
        			</ul>
        		</div>
				</div></li>
       			<li>Emal:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="expresstypein1" type="text" id="email"></li>
       			<li>Quantity:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="expresstypein1" type="text" id="quantity" onkeydown="fnNumberInput();"></li>
       			<li>Other Notes:&nbsp;<textarea class="expresstypein1" style="height: 50px;" rows="2" cols="4" id="note"></textarea></li>
       			<li style="margin-left: 30px;"><button onclick="fnPreferential()">Request Now</button></li>
       			<li>Discount can be granted only if order</li>
       			<li>quantity is considerably larger than MOQ</li>
       			<li>Valid for two months</li>
       		</ul>
       		<div id="alter"></div>
      	</div>
</div>
<div id="light" class="white_content">
       <div class="close" onclick="hide()"><span style="cursor: pointer;" onclick="hide()" onmouseout="this.style.color='#000000'" onmousemove="this.style.color='#2296D8'">Close&nbsp;<img onclick="hide()" src="/cbtconsole/img/express_close.png"></span></div>
      <div class="con">
      	<div>
        	<div class="conshipmo">Ship my order(s) to: </div>
        	<div class="conshipmodiv">
        		<a class="contry_a" id="contry_a"   onclick="fnShowChengeC('');"><img id="country_img" src="/cbtconsole/img/country/36.jpg">&nbsp;&nbsp;<span id="chengeContry">EAST OF USA</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="black_arrow" style="text-align: right;">▼</span></a>
        		<input type="hidden" id="countryid" value="36">
        		<div id="showContry" class="conshowcotry">
        			<ul id="showContry_ul" class="conshowcotryul">
        				<li  class="contry_li"  onmousemove="this.className='contry_li ontry_li-hover'"  onmouseout="this.className='contry_li'" onclick="fnChangeCountry('United States1');">
        					<span class="contrylispan">
        						&nbsp;<img src="/cbtconsole/img/country/36.jpg">&nbsp;&nbsp;<span style="font-size: 12px;line-height: 16px;"></span>&nbsp;
        						<input type="hidden" value="">
        					</span>
        				</li>
        			</ul>
        		</div> 
        	</div>
        	<div style="clear: both;"></div>
        </div>
        <div class="shiipingfreediv">
        	<div class="shipping-dialog-free">
        	<div class="freight_detail_div">
        	 <div id="freight_detail_15">
        		<img onclick="fnFreight_detail('freight_detail_15')" class="freeigntimg1"src="/cbtconsole/img/freight_arrow.png"/>Great for shipping to Africa and Mid East,No electronics allowed,Usually deliver in 7 days.
        	</div>
        	 <div id="freight_detail_16">
        		<img onclick="fnFreight_detail('freight_detail_16')" class="freeigntimg1" src="/cbtconsole/img/freight_arrow.png"/>Allow electronics,For bulky shipments, weight（kg) is calculated as length(cm)*width(cm)*height(cm)/5000,Usually deliver in 3 days.
        	</div>
        	<div id="freight_detail_14">
        		<img onclick="fnFreight_detail('freight_detail_14')" class="freeigntimg1" src="/cbtconsole/img/freight_arrow.png"/>We will ship to a major airport near you and clear custom,You will need to arrange shipping from airport,We ship to all international airports.
        	</div>
        	<div id="freight_detail_13">
        	<ul >
        		<li><img onclick="fnFreight_detail('freight_detail_13')" class="freeigntimg1"src="/cbtconsole/img/freight_arrow.png"/><b>We will ship to a major seaport or inland ports and clear custom,You will need to arrange shipping from the port.</b><br/>We ship to the following ports:</li>
        		<li class="freeeigntli1">
        		<table id="express_sweden" class="expresswetable">
        			<tr >
        				<td class="expresswetabletd1">Australia:</td>
        				<td class="expresswetabletd2">&nbsp;Sydney,Brisbane,Melbourne,Auckland;</td>
        			</tr>
        			<tr >
        				<td class="expresswetabletd3">Africa:</td>
        				<td class="expresswetabletd2">&nbsp;Durban,Cape Town,Johannesburg;</td>
        			</tr>
        			<tr >
        				<td class="expresswetabletd3">South America:</td>
        				<td class="expresswetabletd2">&nbsp;Port of Santos,Rio Grande,Buenos Aires,Port of Arica.</td>
        			</tr>
        			<tr >
        				<td class="expresswetabletd3">Europe:</td>
        				<td class="expresswetabletd4" align="left">&nbsp;Hamburg,Rotterdam,Antwerp,Barcelona,Istanbul,Felixstowe,LE HAVRE,Southampton,Naples.</td>
        			</tr>
        			<tr>
        				<td class="expresswetabletd3">North America:</td>
        				<td colspan="2" class="expresswetabletd5" >&nbsp;Vancouver,New York,Toronto,Los Angeles,Montreal,Auckland,Chicago,Miami,Seattle,Savannah,Houston,Norfolk,Baltimore,Philadelphia,Boston,Charleston,Portland,Atlanta,Calgary</td>
        			</tr>
        		</table></li>
        	</ul>
        	</div>
        	<div id="freight_detail_12"><img onclick="fnFreight_detail('freight_detail_12')" class="freeigntimg1" src="/cbtconsole/img/freight_arrow.png"/>We ship the products by sea to your country (15-30 days) then load it onto a truck and ship to your door.</div>
        	<div id="freight_detail_11"><img onclick="fnFreight_detail('freight_detail_11')" class="freeigntimg1" src="/cbtconsole/img/freight_arrow.png"/>We ship the products by sea to your country(15-30 days) then use FedEx to ship to your door.</div>
        	<div id="freight_detail_6" >
        	 <ul class="freightde6ul">
        		<li><img onclick="fnFreight_detail('freight_detail_6')" class="freigntimg2"  src="/cbtconsole/img/freight_arrow.png"/>Allow electronics with battery inside,Delivery time:</li>
        		<li class="freightde6uli">
        			<table id="express_sweden"  class="freignlitablr" >
        			<tr  align="center">
        				<td class="freignlitablrtd1">Country</td>
        				<td class="freignlitablrtd2">Asia</td>
        				<td class="freignlitablrtd2">N. America</td>
        				<td class="freignlitablrtd2">Australia</td>
        				<td class="freignlitablrtd2">Europe</td>
        				<td class="freignlitablrtd2">S. America</td>
        				<td class="freignlitablrtd2">Mid East</td>
        				<td class="freignlitablrtd3">Africa</td>
        			</tr>
        			<tr align="center">
        				<td class="freignlitablrtd4">Days</td>
        				<td class="freignlitablrtd5">15-20days</td>
        				<td class="freignlitablrtd5">25-30days</td>
        				<td class="freignlitablrtd5">10-15days</td>
        				<td class="freignlitablrtd5">20-30days</td>
        				<td class="freignlitablrtd5">25-30days</td>
        				<td class="freignlitablrtd5">20-25days</td>
        				<td class="freignlitablrtd6"">30-40days</td>
        			</tr>
        		</table></li>
        	</ul>
        	</div>
        	 <div id="freight_detail_7">
        	 <img onclick="fnFreight_detail('freight_detail_7')" class="freeigntimg1" src="/cbtconsole/img/freight_arrow.png"/>
        		Allow electronics with battery inside,Normally deliver in 7-15 days.
        	</div>
        	<div id="freight_detail_1">
        	<img onclick="fnFreight_detail('freight_detail_1')" class="freeigntimg1" src="/cbtconsole/img/freight_arrow.png"/>Delivery in 5-7 days.
        	</div>
        	 <div id="freight_detail_3"><img onclick="fnFreight_detail('freight_detail_3')" style="cursor: pointer;" width="20px" src="/cbtconsole/img/freight_arrow.png"/>
        		Use DHL to ship to your country and Fullfillment Service by Amazon,Accept products with batteries.
        	</div>
        	<div id="freight_detail_4">
        		<img onclick="fnFreight_detail('freight_detail_4')" style="cursor: pointer;" width="20px" src="/cbtconsole/img/freight_arrow.png"/>Use China Post to ship by sea, then airmail to your door.  Takes a long time.
        	</div>
        	<div id="freight_detail_5"><img onclick="fnFreight_detail('freight_detail_5')" style="cursor: pointer;" width="20px" src="/cbtconsole/img/freight_arrow.png"/>Use China Post to ship by air.</div>
        	 <div id="freight_detail_9" style="display: none;"><img onclick="fnFreight_detail('freight_detail_9')" style="cursor: pointer;" width="20px" src="/cbtconsole/img/freight_arrow.png"/>
        		 Express Mail Service offered by China Post,Good for shipping bulky items,For bulky shipments, weight(kg) is calculated as length(cm)*width(cm)*height(cm)/8000.
        	</div>
        	<div id="freight_detail_2">
        		 <img onclick="fnFreight_detail('freight_detail_2')" class="freeigntimg1"src="/cbtconsole/img/freight_arrow.png"/>
        		 Express service originally offered for eBay sellers based in China, good for shipments under 2KG,Deliver in 7-10 days.
        	</div>
        	<div id="freight_detail_10">
				<img onclick="fnFreight_detail('freight_detail_10')" class="freeigntimg1" src="/cbtconsole/img/freight_arrow.png"/>
        		Express service for shipments under 2KG.  Doesn't accept products with batteries inside,Deliver in 7-10 days.
        	</div>
        	<div id="freight_detail_8"><img onclick="fnFreight_detail('freight_detail_8')" class="freeigntimg1" src="/cbtconsole/img/freight_arrow.png"/>Express service for shipments under 30KG.  Not accepting products with batteries inside.
        	</div>
        	<!-- <br/>If you accept product without packaging, shipping cost can usually reduce by 10-50% --></div>
        		<table class="freighttable3"id="express_change" >
        			<thead>
        				<tr class="freighttable3tr1">
	       					<th colspan="4" align="left">Choose Shipping Method&nbsp;&nbsp;(Total Weight:<em style="color: red;" id="sum_w2">0KG</em>)</th>
				            <td rowspan="2" align="left" style="color: white;vertical-align:top;"><img class="freighttable3tr1img"  src="/cbtconsole/img/express_blue.png"><div style="margin-left: 10px;margin-top: -53px;padding-top:0px;width: 280px;">If weight doubles(add another <em id="sum_w1">0</em>kg)shipping cost only goes up by</div></td>
        				</tr>
        				<tr class="freighttable3tr2">
	       					<th></th>
	       					<td width="200px;">Shipping Company</td>
				            <td>Estimated Delivery Time</td>
				            <td width="120px;" >Shipping Cost</td>
        				</tr>
        			</thead>
        			<tbody>
        				<tr id="freight_tr1">
        				<th><input type="radio" name="express_type"  value="1" onclick="fnRadio(this,0);"></th>
        				<td onclick="fnExpressClick('freight_detail_4');" class="freighttable3td1" >China Post SAL</td>
			            <td><em>30</em> Days</td>
			            <td class="freighttable3td2">US $<em>0</em></td>
			            <td class="freighttable3td3">&nbsp;&nbsp;&nbsp;US $<em>0</em></td>
			            </tr>
        			</tbody>
        		</table>
        		<div id="loading_ex" align="center" style="display: none;">
        			<img src="/cbtconsole/img/wy/loading29.gif">
        		</div>
        		</div>
        
        </div>
           <div class="expresstypediv1" >
	        <img id="ok_img" class="expresstypediv1ok" onclick="fnChange_express_type(0)"  src="/cbtconsole/img/spider_bt.png" >
	        <div id="ok_button" onclick="fnChange_express_type(0)" class="expresstypediv1okbtn">OK</div>
        </div>
         </div>
        	<div id="ac_show">Applicable Credit:&nbsp;$<em id="ac">0</em>&nbsp;<img onclick="fnShowAC();"class="expresstypediv1help" src="/cbtconsole/img/help.png">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Shipping Cost After Credit:&nbsp;<em id="credit" style="color: #CC0007;font-weight: bold;">$0</em></div>
        	<br/>
        	<div><em class="expresstypeem1">Special&nbsp;Promotion</em>&nbsp;(7/10&nbsp;to&nbsp;8/10/2015)</div>
        	<div>Each day,we give 20 customers 40% discount on shipping cost.To participate, you can simply submit your email address. Don't forget to add products to shopping cart first.</div>
        	<br/>
        	<div style="font-size: 14px;"><em style="font-weight: bold;">Email Address:</em>&nbsp;<input id="email1" type="text" class="expresstypein1">&nbsp;<button class="freight_email_bt" onclick="fnSaveEmail()">Submit</button>&nbsp;<em id="email_em" style="color: red;"></em></div>
        <div class="expresstypediv2">Note:&nbsp;Calculation is estimated from weight info listed by seller, and needs to be verified later on.  If actual weight is less, shipping cost will be reduced.
      </div>
</div>	
<div id="fade" class="black_overlay"></div>
	<div align="center">
	<div class="mainbody">
		<div align="left" class="spider_explain" style="  z-index: 0">
			<input type="hidden" value="${spider.pUrl}" id="url_val" name="url"/> 
			<input type="hidden" value="${spider.pID}" id="item_id" name="itemID"/> 
			<input type="hidden" value="${spider.sID}" id="shop_id" name="shopID"/>
			<input type="hidden" value="${spider.maxOrder}" id="norm_most" name="norm_most"/>
			<input type="hidden" value="${spider.minOrder}" id="norm_least" name="norm_least"/>
			<input type="hidden" value="${spider.pTime}" id="delivery_time" name="delivery_time"/>
			<input type="hidden" value="${spider.pTime}" id="delivery_time" name="delivery_time"/>
			<input type="hidden" value="${spider.weight==''?0:spider.weight}" id="weight" name="weight"/>
			<input type="hidden" value="${spider.sellUnits}" id="sellUnits" name="sellUnits"/>
			<input type="hidden" value="${spider.pGoodsUnit}" id="pGoodsUnit" name="pGoodsUnit"/>
			<input type="hidden" value="${spider.width}" id="width" name="width"/>
			<input type="hidden" value="${spider.perWeight==''?0:spider.perWeight}" id="perWeight" name="perWeight"/>
			<input type="hidden" value="${spider.method}" id="method" name="method"/>
			<input type="hidden" value="${spider.time}" id="time" name="time"/>
			<c:if test="${param.FreeShipping == 1 || spider.free == 1}">
				<input type="hidden" value="1" id="freight_free">
			</c:if>
			<c:if test="${param.FreeShipping != 1 && spider.free != 1}">
				<input type="hidden" value="0" id="freight_free">
			</c:if>
			<table class="freighttable4" id="loadingjsp" >
				<tr>
					<td align=center >
						<p>
							<font color=gray>Loading…</font>
						</p>
						<p>
							<input type=text name=chart size=46
								class="freighttable4input1">
							<br> <input type=text name=percent size=46
								class="freighttable4input2">
						</p>
					</td>
				</tr>
			</table>  
	</div>
	<div align="right" >
	<div id="goodsInfo" class="goodingor1">
		<div style="font-size:0.9em;">
		<span>${fn:split(spider.category,'^^')[0]}</span> 
		<c:if test="${spider.cateurl!=null&&spider.cateurl!=''}">
		<span>&gt;</span> 
		<span><a href="/cbtconsole/goodsTypeServerlet?keyword=&price1=&price2=&minq=&maxq=${spider.cateurl}">${fn:split(spider.category,'^^')[1]}</a></span> 
		</c:if>
		</div>
		<br/>
		<h3 id="title" class="goodingor1h3">${spider.pName}</h3>
		<!-- <hr align="left" /> -->
		<div class="goods_img">
			<div class="goodsimgdiv1"> 
			<c:if test="${spider.pImage!=null}"> 
				<c:choose>
				<c:when test="${fn:indexOf(spider.pImage[0],'360buyimg') > -1 }">
				<img  alt="" id="img" class="img1"  
								 src="${fn:replace(spider.pImage[0],'n5','n1')}${spider.imgSize[1]}">	
				</c:when>
				<c:when test="${fn:indexOf(spider.pImage[0],'rightinthebox.com') > -1 }">
				<img  alt="" id="img" class="img1"  
								 src="${fn:replace(spider.pImage[0],'50x50','384x384')}${spider.imgSize[1]}">
				</c:when>
				<c:otherwise>
					<img  alt="" id="img" class="img1"  
								 src="${spider.pImage[0]}${spider.imgSize[1]}">		
				</c:otherwise>
				</c:choose>
				 			 
			</c:if>
			<%-- <c:if test="${spider.pImage[0]!=''}">
				<c:if test="${ fn:indexOf(spider.pImage[0], 'taobaocdn') > -1 }">
								<img  alt="" id="img" class="img1" style="width: 100%;position:relative;"  
								 src="${spider.pImage[0]}${spider.imgSize[1]}">
							</c:if> 
			</c:if> --%>
			<c:if test="${spider.pImage==null}">
				<img alt=""  class="img1_s" id="img" src="/cbtconsole/img/1.png">
			</c:if>
			</div>
			<div align="left">
				<ul id="imgs" class="imgs">
				<c:if test="${spider.pImage[0]!=''}">
					
					<c:if test="${fn:length(spider.pImage)>4}">
						<li style="width: 10px;border: 0px none;margin-right: 5px;" onclick="fnLeftImg();"><img src="/cbtconsole/img/leftr.png"></li>
					</c:if>
					
					<c:forEach var="img" items="${spider.pImage}" varStatus="i" >
						<c:choose>
						<c:when test="${fn:indexOf(img,'360buyimg') > -1 }">
							<c:set var="imgshow" value="${fn:replace(img,'n5','n1') }"></c:set>
						</c:when>
						<c:when test="${fn:indexOf(img,'rightinthebox.com') > -1 }">
						<c:set var="imgshow" value="${fn:replace(img,'50x50','384x384') }"></c:set>
						</c:when>
						<c:otherwise>
						<c:set var="imgshow" value="${img}"></c:set>
						</c:otherwise>
						</c:choose>
						<c:if test="${i.count==1}">
							<li id="li_1" class="img_li" >
								<div ><img id="img_value" style="display:block;" onclick="fnClickImg('${imgshow}','${spider.imgSize[1]}');"
								alt="" src="${img}${spider.imgSize[0]}"></div></li>
						</c:if>
						<c:if test="${i.count>1}">
							<li id="li_${i.index + 1}" class="img_li" style="${i.index + 1 > 4 ? 'display:none':'display:block'}"><div ><img onclick="fnClickImg('${imgshow}','${spider.imgSize[1]}');"
								alt="" src="${img}${spider.imgSize[0]}"></div></li>
						</c:if>
						
					</c:forEach>
					<c:if test="${fn:length(spider.pImage)>4}">
						<li style="width: 10px;margin-left: 5px;border: 0px none;"onclick="fnRightImg(${fn:length(spider.pImage)});"><img src="/cbtconsole/img/rightr.png"></li>
					</c:if>
					</c:if>
				</ul>
			</div>

		</div>
		<div class="goods_info">

			<table cellspacing="10px;" cellpadding="6px;" width="507px"  >
				
				<tr >
					<td align="right" class="goods_font">Promotional Price:</td>
				<td class="sprice">USD
				<c:choose>
				      <c:when test="${spider.pSprice!=null&&spider.pSprice != '' }">
					       <label id='price'>${fn:trim(spider.pSprice)}</label> / ${spider.pGoodsUnit}
					  </c:when>
					 <c:when  test="${((spider.pSprice == '' ||   fn:indexOf(spider.pSprice, '-') >-1 ||   fn:indexOf(spider.pOprice, '-') > -1)&& fn:length(spider.pWprice) == 0 ) || (spider.pSprice == null && spider.pOprice == null)  }">
				     <input id='price_input' style="height: 22px;" onkeydown='return check(event)' type='text'/>
				     </c:when>
				     </c:choose>
				</td>
				</tr>
				<tr >
					<c:if test="${(spider.pSprice != '' &&   fn:indexOf(spider.pSprice, '-') > -1 )}">
				     
					<td align="right" class="goods_font">Range: </td><td class="sprice" style="font-size: 16px;"> USD&nbsp;<label >${spider.pSprice}</label> / ${spider.pGoodsUnit}</td>
				     </c:if>
				</tr>
				<c:if test="${spider.pSprice != null && spider.pOprice != null}">
				<tr >
					<td align="right" class="goods_font">List Price:</td>
				<td >USD
				     <c:if test="${spider.pOprice != ''}">
				     <label>${spider.pOprice}</label> / ${spider.pGoodsUnit}
				     </c:if>
				</td>
				</tr>
				</c:if>
				<c:if test="${spider.minOrder != 'null'&& spider.minOrder != '' }">
					<tr>
						<td align="right" class="goods_font">Min.Order Quantity:</td>
						<td><a   id="seller">${spider.minOrder}</a></td>
					</tr>
				</c:if>
					<c:if test="${ fn:length(spider.pSize) > 0 }">
				<tr id="tr_size">
						<td align="right" style="vertical-align: top;" class="goods_font">Size:</td>

						<td id="goods_size">
								<div class="div_c">
									<ul>
						<c:forEach var="size" items="${spider.pSize}" varStatus="i">
											<li onclick="fnClick_style('goods_size',${i.count});"><a>${size}</a></li>
									
							</c:forEach>
									</ul>
								</div>
				</td>
				</tr>
					</c:if>
					<c:if test="${ fn:length(spider.pColor) > 0 }">
				<tr>
						<td align="right" class="goods_font" style="vertical-align: top;">Color:</td>
						<td id="goods_color"><c:forEach var="color"
								items="${spider.pColor}" varStatus="i">
								<div class="div_c">
									<ul>
										<c:if test="${i.count==1}">
											<li onclick="fnClick_style('goods_color',${i.count});"><a>${color}</a></li>
										</c:if>
										<c:if test="${i.count>1}">
											<li onclick="fnClick_style('goods_color',${i.count});"><a>${color}</a></li>
										</c:if>
									</ul>
								</div>
							</c:forEach></td>
				</tr> 
					</c:if>
				<c:if test="${ fn:length(spider.pWprice) > 0 }"> 
				 <tr>
					<td align="right" class="goods_font" style="vertical-align: top;">Wholesale Price:</td>
					<td><table class="pwricetable1" id="pWprice">
						<tr >
							<td class="pwricetable1td1" >Qty</td>
							<td  class="pwricetable1td2">Price</td>
						</tr>
						<c:set var="url" value=""></c:set>
						<c:forEach var="pWprice"
								items="${spider.pWprice}" varStatus="i">
						<tr style="${i.index == 0 ? 'background-color:#FFFFCC':''}" > 
							<td  class="pwricetable1td3"><input type="hidden" id="wprice" value="${spider.pWprice}"/><em>${fn:split(pWprice, '$')[0]}</em></td>
							<td class="pwricetable1td4">US $<em>${fn:split(pWprice, '$')[1]}</em></td>
						</tr>
						</c:forEach>
					</table></td>
				</tr>
				</c:if>
				<tr>
					<td align="right" class="goods_font">Quantity:</td>
					<td class="ji">
					<c:set var="minorder" value="${spider.minOrder == ''|| spider.minOrder == null? 1: fn:split(spider.minOrder, ' ')[0] }"></c:set>
						<input
						onkeydown="fnNumberInput();" onblur="if(this.value == ''||this.value ==0)this.value=1;${ fn:length(spider.pWprice) > 0 ? 'fnWprice()':'' }"  maxlength="5" id="number"
						class="tb_text" value="${minorder}" />
						<a class="jian" onclick="fnMinusNumber('number',this)" title=""></a>
						<a  onclick="fnAddNumber('number',this)"title=""></a>
                        
                        <span style="clear:both;"></span>
						</td>
				
				</tr>
				<c:if test="${spider.pTime != null&&spider.pTime != '' }">
				<tr >
					<td align="right" class="goods_font">Processing Time:</td>
					<td>${fn:split(spider.pTime,'E')[0]}&nbsp; days</td>
				</tr>
				</c:if>
				<c:if test="${spider.sell != null&&spider.sell != '' }">
				<tr >
					<td align="right" class="goods_font">Sold:</td>
					<c:if test="${spider.sell == '0'||spider.sell == '1' }">
						<td>${spider.sell}&nbsp;${spider.pGoodsUnit }</td>
					</c:if>
					<c:if test="${spider.sell != '0'&&spider.sell != '1' }">
					<td>${spider.sell}&nbsp;${spider.pGoodsUnit }s</td>
					</c:if>
				</tr>
				</c:if>
				<c:if test="${spider.free == 1}">
				<tr id="free_tr1">
					<td align="right" class="goods_font" style="vertical-align: initial;">Shipping Cost:</td>
					<td ><em style="color: green;">Free Shipping</em> to<em style="color:#8400FF;"> United States</em>&nbsp;via <em id="method_em">${spider.method}(Deliver in ${spider.time} days)</em>
						<input type="hidden" id="freight" value="0" />
						<span> &nbsp;For combined shipping options, please add to cart before choosing.</span>
					</td>
				</tr>
				</c:if>
				<tr id="free_tr"></tr>
				<!--<tr>
					<td align="right" class="goods_font" style="vertical-align: top;">Remark:</td>
					<td>
					<textarea class="goodsfonttexta"onfocus="if(this.value == 'You can add more product information here, e.g. color,size,matrial,type,etc.'){this.value=''}this.style.color='#000000'" onblur="if(this.value == ''){this.value='You can add more product information here, e.g. color,size,matrial,type,etc.';this.style.color='#AAABAF'}" rows="4" id="remark" cols="">You can add more product information here, e.g. color,size,matrial,type,etc.</textarea></td>
				</tr>  -->
				<tr>
					<td></td>
					<td>
					<script type="text/javascript">
					fnGoodFree();
					</script>
					<div id="bt_add" class="bt_"onclick="add_goods('${spider.pImage[0]}${spider.imgSize[0]}');" style="">Add To Order</div>
						<div id="img_add" class="bt_add_img" >
						<img style="width: 26px;height: 26px;" src="${spider.pImage[0]}${spider.imgSize[0]}" />
						</div>
					<div id="bt_add" class="bt_"  onclick="fnShowAO();" style="margin-left: 170px;margin-top: -40px;">Volume Discount</div>	
					
				</tr>
			</table>
			<div style="margin-left:120px">
			<div class="goodssavelatediv"><a  onclick="fnaddCollection()" class="goodssavelatediva"><img id="collection_img" src="/cbtconsole/img/87a.png" style="margin-bottom: -6px;"/>Add to Favorite</a>&nbsp;<em id="savelater_alert" style="font-size: 10px;font-weight: normal;"></em></div>
			
				<div>
					<img width="30px" src="/cbtconsole/img/buyprotection.jpg">
					<div class="goodgreengodiv2"><span style="font-weight:bold;font-size:15px">Buyer Advantage</span></div>
				</div>
				<div>
					<img class="goodgreengoimg1"alt="" src="/cbtconsole/img/greengou.jpg">
					<div class="goodgreengodiv1">Save money by combine shipping from mutiple vendors</div>
				</div>
				<div>
					<img class="goodgreengoimg1"alt="" src="/cbtconsole/img/greengou.jpg">
					<div class="goodgreengodiv1">Full refund if you don't receive order</div>
				</div>
			</div>
		</div>
		<div style="clear:both"></div>
	</div>
			<div id="table_car" class="nav" >
				
				<div class="tablecanavdiv1">
					<img src="/cbtconsole/img/spider_cartop.png" class="tablecanavdiv1img" >
					<!-- <div class="div1"></div> -->
					<div class="div_info" align="center">
						Shopping Cart <label id="shop_number">0</label> Item(s)
					</div>
				</div>
				<div class="div_info_goods">

					<table id="tab_car" cellspacing="0px;" cellpadding="5px;"
						width="100%">
					</table>
				</div>
				<div class="account_content" align="left" >
				<ul class="acccontentul1">
				<li class="acccontentul1li">Total Price:<span class="acccontentul1lispan">$<em  id="total_price">0</em></span></li>
				<li class="acccontentul1li">Total Chargable Weight: <span class="acccontentul1lispan"><em  id="sum_w">0 </em> KG</span> </li>
				<li class="acccontentul1li">Shipping Cost:<span class="acccontentul1lispan">$<em  id="shipping_cost">0 </em></span></li>
				<li class="acccontentul1li"><div style="float: left;">Applicable Credit:&nbsp;$50&nbsp;&nbsp;</div><div style="float: left;cursor: pointer;" onclick="fnShowAC();"><img src="/cbtconsole/img/help.png"></div></li>
				</ul>
				</div>
				<div class="account_bt" align="center" style="background-color: #FFFFFF;height: 34px;">
					<img src="/cbtconsole/img/spider_bt.png" class="spiderbtnimg1"onclick="fnBalance();">
					<div class="bt_add1"  onclick="fnBalance();">Check out</div>
					<!-- <button class="bt_add1" onclick="fnBalance();">Proceed To Checkout</button> -->
				</div>
				<div class="div1 div_info_goods_tr"></div>
				<div class="goodstrdiv1">
				<img class="goodstrdiv1img"src="/cbtconsole/img/75.png">
				<div class="contrychangediv1" onclick="show()"><img id="country_change_img" src="/cbtconsole/img/country/36.jpg">&nbsp;Ship to <em id="country_change" title="EAST OF USA">USA EAST</em> <em style="color: #00B1FF">&gt;&gt;</em></div></div>
			</div>
			<script type="text/javascript">
			fn();
			fnGoodinfo();
			fnGetAC();
			/* if('${param.pre}'==1){
				fnShowAO();
			} */
			fngetCollection();
			</script>
			</div>
			<div class="contestingodiv1">
			<div class="contestingodiv1">
<c:set var="div_width" value="${spider.supplier == null ? 220 : 0}"></c:set>
<div id="desc" class="descinfo" >
	<div  class="contestingodiv1right" style="width: ${725+div_width}px;">
	<br/>
	<div class="contestingodiv1rightd1" align="left"><!-- <img width="38" alt="" src="/cbtconsole/img/goods_.png"> -->
	<div class="contestingodiv1rightd2">Commodity <span style="color:#EC9854">Details</span></div></div>
	<div  class="desc" >
		<div class="goods_info1" style="max-height:450px ;padding-bottom:40px;border-top:2px solid #EC9854; ">
			<ul>
				<c:forEach var="item" items="${spider.pInfo}">
				<c:set var="li_width" value="260"></c:set>
				<c:choose>
					<c:when test="${fn:length(item.value) > 70}">
						<c:set var="li_width" value="${625+div_width}"></c:set>
					</c:when>
					<c:otherwise>
						<c:set var="li_width" value="${260}"></c:set>
					</c:otherwise>
				</c:choose>
				<li title="${item.value}" style="width: ${li_width}px;">${item.value}</li>
				</c:forEach>
				<c:if test="${fn:length(spider.pInfo)%3 == 1}">
					<li></li><li></li>
				</c:if> 
				<c:if test="${fn:length(spider.pInfo)%3 == 2}">
					<li></li>
				</c:if> 
			</ul>
			<span style="clear:both;"></span>
		<c:if test="${spider.pInfo!=null&&spider.pInfo!=''}">
			<hr class="spiinforhr1" style="width: ${725+div_width}px;">
		</c:if>
		</div>
	</div> 
	
	
	
	
	<div  align="left">
	<%-- <c:forEach var="item" items="${spider.pDetail}">  
	<c:if test="${fn:indexOf(item,'secHeader') > -1 }">
	<div id="technical-data_feature_div">
		<div id="technical-data">
			<hr noshade="true" size="1" class="bucketDivider">
			${item}
		</div>
	</div> 
	</c:if>
	</c:forEach>  --%> 
	<%-- <c:forEach var="item" items="${spider.pDetail}">  
	<c:if test="${fn:indexOf(item,'technical_details') > -1 }">
	<div id="technical-data_feature_div">
		<div id="technical-data">
			<hr noshade="true" size="1" class="bucketDivider">
			${item}
		</div>
	</div> 
	</c:if>
	</c:forEach>  --%>  
	<c:if test="${spider.pDescription != null}">
	<div id="technical-data_feature_div">
		<div id="technical-data">
			<hr noshade="true" size="1" class="bucketDivider">
			<div id="pDescription"></div>
		</div>
	</div> 
	</c:if>
	</div>
	<input type="hidden" id="apiItemDesc" value="${spider.apiItemDesc}">
	<div align="left" id="info_ori" style="width: ${715+div_width}px;max-width:${750+div_width}px;clear:both;">
		${spider.flag=="0" ? "" : spider.info_ori }
	</div>
	<div style="clear:both"></div>
</div>
		 <c:if test="${spider.supplier!= null&&spider.supplier!='' }">
			<div class="supplier" style="margin-left: 8px;margin-top: 43px;">
				<div class="supplii" >
					<ul>
						<c:forEach var="item" items="${spider.supplier}" begin="0" end="5" varStatus="i">
						 	<c:if test="${ item != null }">
							<c:if test="${i.count==1}">
							<div style="font-size: 18px;text-align: left;" >Other offering from the seller:</div>
							<br/>
							</c:if>
							<li>
							<div id="simg" style="/*border: 1px solid #ededed;*/">
								<a href="/cbtconsole/processesServlet?action=getSpider&className=SpiderServlet&url=${item.g_url}"><img width="190px" height="140px;" src="${item.g_img}">
									</a>
								</div>
								<div id="sinfo" align="left">
								<div style="width:190px;font-size: 12px;" id="stitle">
									<a href="/cbtconsole/processesServlet?action=getSpider&className=SpiderServlet${item.g_url}">
										${item.g_name}
									</a>
								</div>
								<div id="sprice" style="width:190px;font-size: 14px;">${item.g_price}</div>
								<div id="smin" style="width:190px;font-size: 12px;">${item.g_min}</div> 
								</div>
								<br/><br/>
								</li>
							</c:if>   
						</c:forEach> 
					</ul> 
					
				</div>
		<c:if test="${spider.supplierUrl!=null&&spider.supplierUrl!=''}">
		<div id="supplieraddress" style="margin-left:140px;"><a target="_blank" href="/cbtconsole/goodsTypeServerlet?keyword=&price1=&price2=&minq=&maxq=&website=w&srt=default${spider.supplierUrl}">View All</a></div>
		</c:if>
			</div>
		</c:if>  
			</div> 
			<div style="clear:both;width:956px;padding:0 4px;">
			<div class="spiderhow"></div>
			<%-- lizhanjun最近浏览的显示 --start --%>
		<div class="resentvdiv1">
				 <%-- <jsp:include page="recent-view.jsp"></jsp:include>  --%>
			<c:if test="${not empty recentProducts }">
				   <hr style="width: 956px;margin:0 auto;"/>
			  		  <h2 class="spih2text">Recent View</h2><br/>
		   	    </c:if>
				<table align="left">
					<tr>
					<c:forEach items="${recentProducts}" var="rp">
							<td>
								<div>
									<a href="/cbtconsole/processesServlet?action=getSpider&className=SpiderServlet&url=${rp.purl }">
									<img src="${rp.imgUrl }" width="100" height="100" />
								</a>
								</div>
								<div>
									<a href="/cbtconsole/processesServlet?action=getSpider&className=SpiderServlet&url=${rp.purl }">
									<span title="${rp.pname }">${fn:substring(rp.pname,0,8)}...</span>
								</a>
								</div>
								<span  class="pi" ><c:if test="${not empty rp.price }">$</c:if>${rp.price }&nbsp;&nbsp;MOQ:${fn:replace(rp.minOrder,"件","Piece") }  </span>
							</td>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</c:forEach>
					</tr>
					<tr align="right">
						<td colspan="6">${pager }</td>
					</tr>
				</table> 
			</div>
	 <%-- lizhanjun最近浏览的显示 --end --%>
    	<%--lizhanjun客户留言 --start --%>
		<div class="resentsaydiv1">
		<hr align="left" style="width: 955px;">
		<h2 class="spih2text">Questions about the product?</h2>
		<div align="left">
				<c:forEach items="${gbbs }" var="gbb" varStatus="i">
				  <c:if test="${not empty gbb.replyContent}"> 
					<h4>Q:${gbb.content }(This question is up to you in ${gbb.showTime })</h4>
					<h4>A:${gbb.replyContent }</h4>
				  </c:if> 
					<%-- <c:if test="${not empty gbb.reps }">
						A:<c:forEach items="${gbb.reps }" var="rep" begin="0" end="0"> 
							${rep.replyContent }
						</c:forEach>
						<a href="/cbtconsole/AbstractServlet?action=findByGBid&className=GuestBookServlet&guestbookId=${gbb.id}" style="text-decoration: none;font-weight:bold" title="Click to view more answer about this problem">To view all ${fn:length(gbb.reps)} answers</a></br>
					</c:if>
					<c:if test="${fn:length(gbb.reps) == 0}">
						<a href="/cbtconsole/AbstractServlet?action=findByGBid&className=GuestBookServlet&guestbookId=${gbb.id}" style="text-decoration: none;font-weight:bold" title="Click on the answer to this question">Let me answer</a>
						</br>
					</c:if> --%>
				</c:forEach>
				<%-- <c:if test="${fn:length(gbbs) > 2}">
					<a href="/cbtconsole/AbstractServlet?action=findByGBid&className=GuestBookServlet&guestbookId=${gbb.id}" 
					style="text-decoration: none;font-weight:bold" title="Click to view more answer about this problem">
						Look at all the questions and answers</a>
				</c:if> --%>
		<%-- <form action="/cbtconsole/AbstractServlet?action=addGuestBook&className=GuestBookServlet" method="post" onsubmit="return checkGuestBook();"> --%>
			<font id="ts">${lyts}</font><br>
			<input class="cdInlineAskQuestionPostBoxText" name="content" onfocus="cleanGuest();" id="guestbook" placeholder="Have a question? Ask the owners here." maxlength="150">
			<input type="hidden" id="pid" value="${spider.pID }">
			<input type="hidden" id="purl" value="${spider.pUrl }">
			<input type="hidden" id="pimg" value="${spider.pImage }">
			<input type="hidden" id="pname" value="${spider.pName }">
			<input type="hidden" id="price1" value="${spider.pSprice }">
			<div id="cd-post-button"  onclick="checkGuestBook();">Ask</div>
		<!-- 	</form> -->
		</div>
		</div>
	<%-- lizhanjun客户留言 --end --%>	
	<c:if test="${spider.relate!= null&&spider.relate!='' }">
<div id="relateSearch" class="relatesearchdiv1" >
<h3 class="spih2text">Relate Search:</h3>
<c:forEach var="item" items="${spider.relate}" begin="0" varStatus="i">
	<c:if test="${ item != null }">
		<div  class="relatkeydiv2"><a target="_blank" href="/cbtconsole/goodsTypeServerlet?keyword=${item.name}&price1=&price2=&minq=&maxq=&website=${item.website}&srt=default${item.url}">${item.name}</a></div>
	</c:if>
</c:forEach>
<div style="clear:both;"></div>
</div>
</c:if>
	</div>	
	</div>
	</div>
	</div>
</div>

<div class="bottomdown">
	<jsp:include page="main_down.jsp"></jsp:include>
</div>
</div>
</body>
</html>