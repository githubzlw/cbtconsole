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
<title>Add Wholesale Products</title>
<style type="text/css">
.title{width:100px;height:20px;text-align: center;float: left;}
.text{float: left;}
.out{float: left;}
body{
width: 1350px;
margin:0 auto;
border: 1px solid #e3e3e3;
}
</style>
</head>
<body>
<div style="font-size: 20px;font-weight: bold;background: #EDBFBF;height: 50px;text-align: center;">
<div style="height: 30px;top: 10px;position: relative;">Add Wholesale Products</div>
</div>
<br/>
<form action="/cbtconsole/addwholesale" method="post">
<div>
<div class="out">
<div class="title">Hot words:</div><textarea rows="1" cols="30" name="kwywords" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">minprice:</div><textarea rows="1" cols="30" name="minprice" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">maxprice:</div><textarea rows="1" cols="30" name="maxprice" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">morder:</div><textarea rows="1" cols="30" name="morder" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div style="clear: both;"></div>
</div>

<div>
<div class="out">
<div class="title">weight:</div><textarea rows="1" cols="30" name="weight" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">per weight:</div><textarea rows="1" cols="30" name="perweight" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">volume:</div><textarea rows="1" cols="30" name="volume" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">goods unit:</div><textarea rows="1" cols="30" name="gunit" class="text" >piece</textarea>
<div style="clear: both;"></div><br/>
</div>
<div style="clear: both;"></div>
</div>


<div>
<div class="out">
<div class="title">price unit:</div><textarea rows="1" cols="30" name="punit" class="text">RMB</textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">url:</div><textarea rows="1" cols="125" name="url" class="text"></textarea>
<div style="clear: both;"></div><br/>
</div>
<div style="clear: both;"></div>
</div>
<input type="submit" value="Submit" style="margin-left: 40%;width: 200px;">
</form>





<div id="result">
<div class="titleresult" id="resulrtitle" style="font-size: 20px;font-weight: bold;">Result:
<c:if test="${spider.valid==0 && spider.hotwords!=null}"><span style="color: Red;"> fail</span></c:if>
<c:if test="${spider.valid==0 && (spider.hotwords==null||spider.hotwords=='')}"><span style="color: Red;"> kwywords is empty</span></c:if>
<c:if test="${spider.valid==1 }"><span> success!!</span></c:if>
<c:if test="${spider.valid==2 }"><span style="color: Blue"> exist !!</span></c:if>
</div>
<br/>
<div>
<div class="out">
<div class="title">Hot words:</div><textarea rows="1" cols="30" id="kwywords" class="text">${spider.hotwords}</textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">minprice:</div><textarea rows="1" cols="30" id="minprice" class="text" >${spider.minprice}</textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">maxprice:</div><textarea rows="1" cols="30" id="maxprice" class="text">${spider.maxprice}</textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">morder:</div><textarea rows="1" cols="30" id="morder" class="text">${spider.morder}</textarea>
<div style="clear: both;"></div><br/>
</div>

<div style="clear: both;"></div>
</div>
<div>
<div class="out">
<div class="title">weight:</div><textarea rows="1" cols="30" id="weight" class="text">${spider.weight}</textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">per weight:</div><textarea rows="1" cols="30" id="perweight" class="text">${spider.pweight}</textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">volume:</div><textarea rows="1" cols="30" id="volume" class="text">${spider.width}</textarea>
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">goods unit:</div><textarea rows="1" cols="30" id="gunit" class="text">${spider.gunit}</textarea>
<div style="clear: both;"></div><br/>
</div>
<div style="clear: both;"></div>
</div>
<div>

<div class="out">
<div class="title">price unit:</div><textarea rows="1" cols="30" id="punit" class="text">${spider.punit}</textarea>
<div style="clear: both;"></div>
</div>
<div class="out">
<div class="title">product id:</div><textarea rows="1" cols="30" id="pid" class="text">${spider.pid}</textarea>
<div style="clear: both;"></div>
</div>
<div class="out">
<div class="title">url:</div><textarea rows="1" cols="78" id="url" class="text">${spider.url}</textarea>
<div style="clear: both;"></div><br/>
</div>
<div style="clear: both;"></div><br/>
</div>
<div>
<div class="out">
<div class="title">product img:</div><img id="img" style="width:200px;height:200px;" class="text" src="${spider.img}">
<div style="clear: both;"></div><br/>
</div>
<div class="out">
<div class="title">product name:</div><textarea rows="13" cols="30" id="name" class="text">${spider.name}</textarea>
<div style="clear: both;"></div>
</div>
<div class="out">
<div class="title">wholesale peice:</div><textarea rows="13" cols="30" id="wprice" class="text">${spider.wprice}</textarea>
<div style="clear: both;"></div>
</div>
<div style="clear: both;"></div><br/>
</div>
</div>
</body>
</html>