<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ page session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="/cbtconsole/img/mathematics1.ico" type="image/x-icon"/>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>Config goods's status  and  parameters</title>
<style type="text/css">
.title{width:50px;height:20px;text-align: center;float: left;}
.text{float: left;}
.out{float: left;}
body{
width: 1100px;
margin: 0 auto;
}
.body{
width: 1100px;
height:auto;
border: 1px solid #e3e3e3;
}
.set{
float:left;
}
</style>
 <script type="text/javascript">
 
 function  fndown(){
	 var sid1 = document.getElementById("sid1").value;
	 var sid2 = document.getElementById("sid2").value;
	 var index = document.getElementById("index").value;
	 $.post("/cbtconsole/WebsiteServlet", {
			"id1":sid1,
			"id2":sid2,
			"index":index,
			"action":"imgDownload",
			"className":"GoodsConfigServlet"
		}, function(data) {
			
			document.getElementById("span-result").innerHTML=data;
			
		})
 }
 function  fnupload(){
	 var up1 = document.getElementById("up1").value;
	 var up2 = document.getElementById("up2").value;
	 $.post("/cbtconsole/WebsiteServlet", {
			"id1":up1,
			"id2":up2,
			"action":"imgUpload",
			"className":"GoodsConfigServlet"
		}, function(data) {
			
			document.getElementById("up-result").innerHTML=data;
			
		})
 }
    </script>
</head>
<body>
<div class="body">
<div style="font-size: 20px;font-weight: bold;background: #EDBFBF;height: 50px;text-align: center;">
<div style="height: 30px;top: 10px;position: relative;">Config Page</div>
</div>

<div class="mianset">
<div style="font-size: 20px;font-weight: bold;color: blue;background: #e3e3d3;height:30px;position: relative;"> 1.Setup</div>
<br/>
<div class="set"><a target="_blank" href="/cbtconsole/website/addwholesale.jsp">Hot words</a></div>
<div style="clear: both;"></div><br/>
</div>

<div class="goodsvalid">
<div style="color: blue;font-size: 20px;font-weight: bold;background: #e3e3d3;height:30px;">2.Set Goods Valid:</div>
<div class="note" style="color:red;">Note:
<br/>
<span>
(1).valid=1 :The data is valid ; valid=0 :The data is invalid  (valid=2:The data is valid  forever only when com=g)
</span>
<br/>
<span>(2).com=g:update goodsdata ; com=s ：update all ; com=e：update elly ; com=h:update hotwords ; com=w:update wholesale</span>
</div>
<br/>
<form action="/cbtconsole/goodsconfig" method="post" >
<input type="hidden" name="set" value="1" >
<div>
<div class="out">
<div class="title">valid:</div><textarea rows="1" cols="5" name="valid" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">com:</div><textarea rows="1" cols="5" name="com" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">url:</div><textarea rows="1" cols="101" name="url" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<input type="submit" value="Submit" style="width: 100px;">
<div style="clear: both;"></div><br/>
</div>
<div style="clear: both;"></div>
</div>
</form>
<div id="valid_result">
<div class="valid_titleresult" id="valid_resulrtitle" style="font-size: 20px;font-weight: bold;">Result:
<c:if test="${valid==0}"><span style="color: Red;"> fail</span></c:if>
<c:if test="${valid>0}"><span> success!!</span></c:if>
</div>
</div>
</div>
<br/>

<div class="goodsadd">
<div style="color: blue;font-size: 20px;font-weight: bold;background: #e3e3d3;height:30px;">3.Add Invalid Goods:</div>
<br/>
<form action="/cbtconsole/goodsconfig" method="post">
<input type="hidden" name="set" value="2" >
<div>
<div class="out">
<div class="title">url:</div><textarea rows="1" cols="132" name="url" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<input type="submit" value="Submit" style="width: 100px;">
<div style="clear: both;"></div><br/>
</div>
<div style="clear: both;"></div>
</div>
</form>
<div id="add_result">
<div class="add_titleresult" id="add_resulrtitle" style="font-size: 20px;font-weight: bold;">Result:
<c:if test="${add==0}"><span style="color: Red;"> fail</span></c:if>
<c:if test="${add>0}"><span> success!!</span></c:if>
</div>
</div>
</div>
<br/>
<div class="intens">
<div style="color: blue;font-size: 20px;font-weight: bold;background: #e3e3d3;height:30px;">4.Add Shielding Products:</div>
<div style="color: red;">Note:<br/>
<span>(1).Regx :Sensitive words</span><br/>
<span>(2).cid=1:Brand name  ; cid=2:Shielding words</span><br/>
<span>(3).catid:Category's id</span>
</div>
<br/>
<form action="/cbtconsole/goodsconfig" method="post" >
<input type="hidden" name="set" value="3" >
<div>
<div class="out">
<div class="title">Regx:</div><textarea rows="1" cols="71" name="Regx" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">cid:</div><textarea rows="1" cols="10" name="cid" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">catid:</div><textarea rows="1" cols="30" name="catid" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<input type="submit" value="Submit" style="width: 100px;">
<div style="clear: both;"></div><br/>
</div>
<div style="clear: both;"></div>
</div>
</form>
<div id="inten_result">
<div class="inten_titleresult" id="inten_resulrtitle" style="font-size: 20px;font-weight: bold;">Result:
<c:if test="${inten==0}"><span style="color: Red;"> fail</span></c:if>
<c:if test="${inten>0}"><span> success!!</span></c:if>
<c:if test="${inten<0}"><span> exsis!!</span></c:if>
</div>
</div>
</div>
<br>


<%-- <div class="refresh">
<div style="color: blue;font-size: 20px;font-weight: bold;background: #e3e3d3;height:30px;">5.Refresh Aliexpress Products:</div>
<div style="color: red;">Note:<br/>
<span>(1).id1 :min id</span><br/>
<span>(2).id2 :max id</span><br/>
</div>
<br/>
<form action="/cbtconsole/goodsconfig" method="post" >
<input type="hidden" name="set" value="4" >
<div>
<div class="out">
<div class="title">id1:</div><textarea rows="1" cols="50" name="id1" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">id2:</div><textarea rows="1" cols="50" name="id2" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<input type="submit" value="Submit" style="width: 200px;">
<div style="clear: both;"></div><br/>
</div>
<div style="clear: both;"></div>
</div>
</form>
<div id="refreshresult">
<div class="titleresult" id="refreshresultitle" style="font-size: 20px;font-weight: bold;">Result:
<c:if test="${refresh==0}"><span style="color: Red;"> fail <span>&nbsp;end with id=${refresh}</span></span></c:if>
<c:if test="${refresh>0}"><span> success!!<span>&nbsp;end with id=${refresh}</span></span></c:if>
</div>
</div>
</div> --%>

<div class="imgdownload">
<div style="color: blue;font-size: 20px;font-weight: bold;background: #e3e3d3;height:30px;">5.Download 1688 Product's imgs:</div>
<div style="color: red;">Note:<br/>
<span>(1).id1 :min id</span><br/>
<span>(2).id2 :max id</span><br/>
</div>
<br/>
<input type="hidden" name="set" value="6" >
<div>
<div class="out">
<div class="title">id1:</div><input id="sid1" class="text"></input>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">id2:</div><input id="sid2" class="text"></input>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">index:</div><input id="index" class="text"></input>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<input type="submit" value="Submit" style="width: 200px;"  onclick="fndown()">
<div style="clear: both;"></div><br/>
</div>
<div style="clear: both;"></div>
</div>
<div id="downloadresult">
<div class="titleresult" id="downloadtitle" style="font-size: 20px;font-weight: bold;">Result:
<span id="span-result"></span>
</div>
</div>
</div>
<div class="imgupload">
<div style="color: blue;font-size: 20px;font-weight: bold;background: #e3e3d3;height:30px;">6.upload 1688 Product's imgs:</div>
<div style="color: red;">Note:<br/>
<span>(1).id1 :min id</span><br/>
<span>(2).id2 :max id</span><br/>
</div>
<br/>
<input type="hidden" name="set" value="7" >
<div>
<div class="out">
<div class="title">id1:</div><input id="up1" class="text"></input>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">id2:</div><input id="up2" class="text"></input>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<input type="submit" value="Submit" style="width: 200px;"  onclick="fnupload()">
<div style="clear: both;"></div><br/>
</div>
<div style="clear: both;"></div>
</div>
<div id="uploadresult">
<div class="titleresult" id="uploadtitle" style="font-size: 20px;font-weight: bold;">Result:
<span id="up-result"></span>
</div>
</div>
</div>

</div>
</body>
</html>