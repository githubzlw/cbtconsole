<%@page import="com.cbt.util.WebCookie"%>
<%@page import="com.cbt.bean.UserBean"%>
<%@page import="com.cbt.util.AppConfig"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<script type="text/javascript" src="/cbtconsole/js/main_top.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/index1.css">
<style type="text/css">
input::-ms-clear{display:none;}/*隐蔽文本框叉子*/
input::-ms-reveal{display:none;}/*隐蔽暗码框小眼睛*/
</style>


<div class="top">
<div class="head" >
		<div class="site_head">
			<div class="login_info">
 				<!-- <div style="position: absolute;top:58px;left:75px"> 
 					<img id="qiu" src="" width="37px" height="37px"> 
 				</div>  -->
				<ul id="userinfo">
				<li>
				</li>
				<li >
					<a></a>
				</li>
<!-- 				<li style="border-right:0 none;"> -->
<%-- 					<a href='/cbtconsole/WebsiteServlet?action=getMessage&className=Message&state=-1' class="message"><em style="color: #FFFF00;">(<%=message %>)</em></a> --%>
<!-- 				</li> -->		 
			</ul>
				<%-- <span></span>&nbsp;|&nbsp;
				<span></span>&nbsp;&nbsp;<span>d</span><span><img src="../img/top_message.png" style="padding-top: 10px;">新消息<em style="color: #FFFF00;">(0)</em></span> --%>
			</div>
			<%-- <img style="position:absolute;top:1px;left:550px" height="27px" src="/cbtconsole/img/myaccount.png" >
			<div style="position:absolute;top:6px;left:580px">
				<span><a id="account" style="color:#fff;text-decoration: none" <%=userinfo != null ? "href='"+AppConfig.ip+"/AbstractServlet?action=getCenter&className=IndividualServlet'" : "href='"+AppConfig.ip+"/cbtconsole/cbt/geton.jsp'" %>>My Account</a></span>		
			<span style="border-right: 1px solid #red;padding-left:10px"></span>
			</div> --%>
			<ul class="head_menu">
<!-- 				<li> -->
<%-- 					<a href="/cbtconsole/cbt/shop-car.jsp" class="shopping" style="background: url(/cbtconsole/img/myaccount.png) no-repeat 3px 2px;width:47px;"></a> --%>
<!-- 				</li> -->
				<li class="shop_car" >
					<a href="/cbtconsole/processesServlet?action=getShopCar&className=Goods" class="shopping">&nbsp;Cart&nbsp;<label style="margin-top:3px;color: #FFFF00;">(<span id="cartNumber">0</span>)</label></a>
				</li>
				<li id="message_li" style="margin-top:3px;">
					<a id="message_a" href='/cbtconsole/WebsiteServlet?action=getMessage&className=Message&state=-1' class="message"><em id="message" style="color: #FFFF00;">(0)</em></a>
				</li>
				<li style="border-right:0 none;margin-top:3px">
					<a href='/cbtconsole/processesServlet?action=getCenter&className=IndividualServlet' style="padding-left: 36px;position: relative;  background: url(/cbtconsole/img/myaccount.png) no-repeat 3px -2px;margin: -2px 0px -1px 0px;"><span style="color:#fff;text-decoration: none" >My Account</span></a>
				</li>
<!-- 				<li style="border-right:0 none;" > -->
<%-- 					<a href="/cbtconsole<%=userinfo != null ? "/processesServlet?action=getCenter&className=IndividualServlet" : "/cbt/geton.jsp" %>">My&nbsp;Account</a> --%>
<!-- 				</li> -->
				<!-- <li>
					<a href="http://www.xe.com/currencyconverter/" target="_blank">Currency Converter</a>
				</li> -->
			 
			</ul>
		</div>			
	</div>
	
<script type="text/javascript">
function createXMLHttp() {    
    var XmlHttp;
    if (window.ActiveXObject)
    {
        var arr=["MSXML2.XMLHttp.6.0","MSXML2.XMLHttp.5.0", "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0","MSXML2.XMLHttp","Microsoft.XMLHttp"];
        for(var i=0;i<arr.length;i++) {
            try {
                XmlHttp = new ActiveXObject(arr[i]);
                return XmlHttp;
            }
            catch(error) { }
        }
    } else {
        try {
            XmlHttp=new XMLHttpRequest();
            return XmlHttp;
        }
        catch(otherError) { }
    } 
} 


var path = '/cbtconsole';
var userid = 0;

var url=path+"/processesServlet?action=getUserInfo&className=IndexServlet";  
   var xmlHttp = createXMLHttp(); 
 xmlHttp.open('post',url,true); 
 xmlHttp.onreadystatechange = function() {
   if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
		var res=xmlHttp.responseText;
		var data = res.split(",");
		path = data[2];
		userid = data[3];
		var username = data[4];
		var cartNumber = data[0];
		var ordering = data[5];
		 if(data[3] == 0){
			 document.getElementById("userinfo").getElementsByTagName("li")[0].innerHTML="<a href=\""+data[2]+"/cbt/geton.jsp"+"\">Sign In</a>";;
		 }else{
			 document.getElementById("userinfo").getElementsByTagName("li")[0].innerHTML="<a href=\""+data[2]+"/processesServlet?action=logout&className=Login"+"\">Sign Out</a>";
			 document.getElementById("userinfo").getElementsByTagName("li")[1].innerHTML="<a href=\""+path+"/processesServlet?action=getCenter&className=IndividualServlet"+"\">"+username+"</a>";
		 }
		 if(ordering!="null"){
			 	document.getElementById("message_li").style.display = "block";
			 	document.getElementById("message_a").href="/cbtconsole"+ordering;
			 	document.getElementById("message_a").className="ordering";
			 	document.getElementById("message_a").innerHTML="Order In Process";
			}
		 document.getElementById("cartNumber").innerHTML=cartNumber;
		 if(document.getElementById("message")!=null){
			 if(data[1] == 0){
				 document.getElementById("message_li").style.display = "none";
			 }else{
				 document.getElementById("message").innerHTML="("+data[1]+")";
				 document.getElementById("message_li").style.display = "block";
			 }
		 }
     }
   };
   xmlHttp.send('');
</script>
	<div style="width:966px;margin:0 auto;border:1px solid rgb(234, 234, 234);padding-bottom:0px;background:#fff;">
	<div class="search">
		<div class="logo"><a href="javascript:window.location = 'http://'+window.location.hostname;"><img src="/cbtconsole/img/logo.png"/></a></div>
		<div class="search_r">
			<div style=" width:620px;position:relative;">
			<div id="productdiv" align="center" class="searchurl" onclick="changebtn2()">Search product</div>
				<div id="urldiv" align="center" class="producturl" onclick="changebtn1()">Give product URL</div>
				
				<div id="fastdaigou" class="fast_daigou" >
				<div id="urlwhole"style="" >
					<div style="">
					<table class="urltable">
					<tr>
					<td class="urlinputtd">
					<div class="urlinput">
					<div id="url_div">
						<input type="text" id="url"   class="search_url"/> 
						<div style="border:1px  solid #e3e3e3;display:none;" id="auto_div"></div> 
						<div style="clear:both;"></div>
					</div>
					</div>
					</td>
					<td id="selecttd">
					<div class="urlselect" id="selecttddiv">
					<select id="website" class="faselect" >
							<option value="a" selected="selected">Free Shipping</option>
							<option value="w" >Wholesale</option>
<!-- 							<option value="r" >Retail</option> -->
							<option value="h" >Factory</option>
<!-- 							<option value="t" >TaoBao</option> -->
						</select>
					</div>
					</td>
					<td>
					<div class="urlbtn1">
					<div onclick="fnSerch();" id="btn1"class="search_bt" style="background:url(/cbtconsole/img/i_bfm.png) no-repeat 0 0;display:none;">Buy for me</div>
					<div onclick="toProduct();" id="btn2"  class="search_bt"style="background:url(/cbtconsole/img/i_bfm.png) no-repeat 0 0;" >Search</div>
					</div>
					</td>
					</tr>
					</table>
						
						
					</div>
					
					
				</div>
				</div>
			</div>
			<!-- <div style="margin-top: 15px;postion:relative"> -->
			<!-- </div> -->
		</div>
		<!-- begin wanyang
		<div id="tipdiv" style="position:absolute;display:none;margin-top: 78px;left:200px;">
			<img style="position:absolute; left:230px;z-index:0;" width="500" height="70" src="/cbtconsole/img/wy/29a.png" />
			<img style="position:absolute; left:230px;z-index:2;" width="500" height="70" src="/cbtconsole/img/wy/29b.png" />
			<img style="position:absolute; top:-15px; left:700px; z-index:1" width="25" height="35" src="/cbtconsole/img/wy/arrow.png" />
		</div>
		 end wanyang-->
		
	</div>
	<div  class="related_search"><span  id="relatedsearch"></span></div> 
	
	<div class="navigation" id="navigationn" align="center">
	<div class="naviT" id="navitt">
		<ul id="naviT_ui">
			<li>
				<div class="navigation_alink" id="home"   align="center" ><a href="javascript:window.location = 'http://'+window.location.hostname;">Home</a></div>
			</li>
			<li>
				<div id="smallbusiness"  id="smallbusiness" align="center" ><a href="/cbtconsole/apa/Send_BusinessInquiryNow.html">Business Solution</a></div>
			</li>
			<li>
				<div id="buy"   align="center"><a href="/cbtconsole/apa/buyforme.html">Buy for me</a></div>
			</li>
			<li>
				<div id="shipping"  align="center" ><a href="/cbtconsole/apa/ShippingMethods.html">Shipping Methods</a></div>
			</li>
		</ul>
		</div>
		
	</div>
	</div>
</div>