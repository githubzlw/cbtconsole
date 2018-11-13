<%@page import="com.cbt.util.WebCookie"%>
<%@page import="com.cbt.util.AppConfig"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<script type="text/javascript">
<% String[] userinfo = WebCookie.getUser(request);
%> 
(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

	  ga('create', 'UA-61939258-1', 'auto');
	  ga('send', 'pageview');
	  ga('set', '&uid', <%=userinfo != null ? userinfo[0] : "0"%>);
	  </script>
<div align="center" style="width:966px;margin:0 auto;background:#fff;border:1px solid #e2e2e2;">
<hr style="clear: both;border-top: 2px solid #349ec8;width: 100%">
<div style="width: 950px;clear: both;padding:0 8px 0 5px;" class="foot" align="left">
	<div class="foot1" align="center">
		<ul style="" class="foot_ul1">
			<li class="foot_li1">
				<ul  style="margin-left: 12px;">
					<li class="foot_bold">Quick Guide</li>
					<li class="foot_li2"><a href="/cbtconsole/apa/aboutus.html">About Us</a></li>
					<li class="foot_li2"><a href="/cbtconsole/apa/OurFees.html">Cost and fees</a></li>
					<li class="foot_li2"><a href="/cbtconsole/apa/ShoppingProcedure.html">Shopping Procedure</a></li>
				</ul>
			</li>
			<li class="foot_li1" >
				<ul  style="margin-left: 12px;">
					<li class="foot_bold ">Shipping</li>
<!-- 					<li class="foot_li2">International shipping</li> -->
<!--去掉cost calculator0619 <li class="foot_li2"><a href="/cbtconsole/cbt/ShippingCostEstimate.jsp">Cost Calculator</a></li> -->					
					<li class="foot_li2"><a href="/cbtconsole/apa/Location.html">Locations We Ship To</a></li>
					<li class="foot_li2"><a href="/cbtconsole/apa/CustomDuty.html">Custom &amp; Duty Related</a></li>
				</ul>
			</li>
			 <li class="foot_li1" >
				<ul style="margin-left: 12px;">
					<li class="foot_bold ">Security &amp; Privacy</li>
					<li class="foot_li2"><a href="/cbtconsole/apa/Conditionofus.html">Condition of use</a></li>
					<li class="foot_li2"><a href="/cbtconsole/apa/Privacy.html">Privacy Notice</a></li>
					<li class="foot_li2"></li>
				</ul>
			</li>
			<!-- <li class="foot_li1" >
				<ul style="margin-left: 22px;">
					<li class="foot_bold ">Stay Connected</li>
					<li class="foot_li2"><img width="30px" onclick="location='https://www.facebook.com/dialog/oauth/?client_id=749289691812908&redirect_uri=<%=AppConfig.ip%>/FacebookCallback&scope=email&display=page&expires=0'"  src="/cbtconsole/img/facebook.png"></li>
					<li class="foot_li2"></li>
					<li class="foot_li2"></li>
					<li class="foot_li2"></li>
				</ul>
			</li> -->
			<li class="foot_lit" style="border: 0px;width: 290px;">
				<ul style="margin-left: 12px;">
					<li class="foot_bold"style="width:290px;">
						<div style="font-size: 15px;">&nbsp;&nbsp;Customer Service</div>
					</li>
					<li><a href="/cbtconsole/apa/contact.html"class="footcontan">New to our site?<br/>learn more<em>>></em></a></li>
					<li class="foot_li2" style="width:290px;">Email:<a href="mailto:sales@sourcing-cn.com">sales@sourcing-cn.com</a></li>
					<li class="foot_li2" style="width:290px;"><div id="showTim">Stay Connected&nbsp;&nbsp;<img style="width:30px;vertical-align:middle;cursor:pointer;" onclick="location='https://www.facebook.com/dialog/oauth/?client_id=749289691812908&redirect_uri=<%=AppConfig.ip%>/FacebookCallback&scope=email&display=page&expires=0'"  src="/cbtconsole/img/facebook.png"></div></li>
				</ul>
			</li>
		</ul>
	</div>
</div>
<div align="center" class="shangbiao" style="height: 65px;clear: both;border-bottom: 1px solid #A9A9A9;border-top: 1px solid #A9A9A9;">
	<div style="width: 470px;margin-top: 10px;" align="left">
		<img src="/cbtconsole/img/wy/papal.jpg">
		
	</div>
</div>
<div class="tail" >
	<div class="tail_div" align="left">
	<ul> 
		<li class="tail_li2" >
			<img src="/cbtconsole/img/foot_logo.png">
		</li>
		<li class="tail_li3" >
			 Copyright 2015 ImportExpress Inc. All rights reserved.
		</li>
	</ul>
	</div>
</div>
<br>&nbsp;
<div style="width: 30px;"></div>
</div>