<%@page import="org.apache.shiro.session.Session"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" import="net.sf.json.*,java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%
	request.setCharacterEncoding("UTF-8");
    List<Map<String,String>> goodslist=new ArrayList<Map<String,String>>();
    Map<String, String> falgmap=new HashMap<String, String>();
    Map<String, List<Map<String,String>>> catmap=new HashMap<String, List<Map<String,String>>>();
    String  catgps=null;
	Map<String,Map<String,String>> pagemap=new HashMap<String,Map<String,String>>();
    String responsetext = "[]"; 
    if(request.getAttribute("responsetext")!=null){
    	responsetext=request.getAttribute("responsetext").toString();
    }
    String goodsnum="4";
    if(request.getAttribute("goodsnum")!=null){
    	goodsnum = request.getAttribute("goodsnum").toString();
    }
    String check_result = "0";
    if(request.getAttribute("check_result")!=null){
    	check_result = request.getAttribute("check_result").toString();
    }
   List<Map<String,String>> mapListJson = new ArrayList<Map<String, String>>(); 
    JSONArray jsonArray = JSONArray.fromObject(responsetext);
    for (int i = 0; i < jsonArray.size(); i++) 
    { 
    	JSONObject jsonObject = jsonArray.getJSONObject(i); 
         Iterator<String> keyIter = jsonObject.keys(); 
         Map<String, String> valueMap = new HashMap<String, String>(); 
		String key;
         while (keyIter.hasNext()) 
         { 
        	 key = (String) keyIter.next();
          valueMap.put(key, jsonObject.get(key).toString()); 
         } 
       jsonObject = jsonArray.getJSONObject(i); 
       mapListJson.add(valueMap); 
    } 
    Iterator<Map<String,String>> it=mapListJson.iterator();
    String ip = null;
    if(mapListJson!=null&&!mapListJson.isEmpty()){
    	ip = mapListJson.get(0).get("ip");
    }
    while(it.hasNext()){
    	Map<String,String> map=it.next();
    	String name = map.get("key_name");
    	String key_type=map.get("key_type");
    	if(key_type!=null){
    		if("categps".equalsIgnoreCase(key_type)){
    			catgps = map.get("key_url");
    		}else if("goods".equalsIgnoreCase(key_type)){
	            goodslist.add(map);
	   	    }else if("page amount".equalsIgnoreCase(key_type) || "page current".equalsIgnoreCase(key_type) ||
	   	    		 "next page".equalsIgnoreCase(key_type)){
	    		pagemap.put(map.get("key_type"),map);
	   	    }else{
	   	    	if(name!=null){
	   	    		String value = name.replaceAll("&&", "\"");
	   		    	map.put("key_name", value);
	   	    	}
	   	    	
	   	    	String key_name=map.get("key_name");
	   	    	int result=key_name.indexOf("Active Filters");
	   	    	int categoriesnum=key_type.indexOf("Categories");
	   	    	if(result>=0){
	   	    		key_type="Active Filters";
	   	    	}
	   	    	if(categoriesnum>=0){
	   	    		key_type="Categories";
	   	    	}
	   	    	if(key_name.endsWith("&nbsp;X")){
	   	    		key_type=key_type+"&nbsp;X";
	   	    	}
	   	    	if(falgmap.get(key_type)==null){
	   	    		falgmap.put(key_type, key_type);
	   	    		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
	   	    		list.add(map);
	   	    		catmap.put(key_type, list);
	   	    	}else{
	   	    		List<Map<String,String>> list=catmap.get(key_type);
	   	    		list.add(map);
	   	    		catmap.put(key_type, list);
	   	    	}   	    	
	   	    }
    	}
    }
  request.setAttribute("catgps", catgps);
  request.setAttribute("catmap", catmap);
  request.setAttribute("goodslist", goodslist);
  request.setAttribute("pagemap", pagemap);
  request.setAttribute("check_result", check_result);
  request.setAttribute("goodsnum",goodsnum);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>
View more search results
</title>
<meta name="keywords" content="<%= request.getParameter("keyword") %>, Made in China">
<meta name="description" content="Product list for <%=request.getParameter("keyword") %>, made in China.">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<link rel="shortcut icon" href="/cbtconsole/img/mathematics1.ico" type="image/x-icon"/>
<style type="text/css">
div#container{width:966px;margin:0 auto;background:#fff;}
div#result {width:968px;}
div#clear {clear:both}
div#page{margin:0 auto;}
span#pageleft {float:left;}
span#pagenext {float:left}
span#pageright {float:left}
div#clear2 {clear:both}
.resultimg{ height: expression(this.width > 250 ? this.height = this.height * 250 / this.width : "auto");width: expression(this.width > 250 ? "250px" : "auto");max-width:250px;}
#selectdiv{padding-top:15px;}
.conback{position:absolute;top:10px;left:24px;width:30px;height:30px;background:url(/cbtconsole/img/conback.png) no-repeat;cursor:pointer;}
.newconmain{width:966px;float:right;}
.newcate{width:190px;float:left;}
.newcate a{text-decoration: none;color: rgb(81, 82, 84);}
.newprobody{width:966px;margin:0 auto;}
#keywordcate{background:#fff;}
#cateclaer{background:#fff;}
#catemove{background:#fff;}
.ul-item{width: 990px;height:395px; position: relative;}
.over {display: none; position: absolute;top: 0;left: 0; width: 100%; height: 100%; background-color: #f5f5f5;opacity:0.5; z-index: 1000;}
.layout {display: none; position: absolute;top: 40%;left: 40%; width: 20%;height: 20%;z-index: 1001;text-align:center;}
blockquote, body, button, dd, dl, dt, fieldset, form, h1, h2, h3, h4, h5, h6, hr, input, legend, li, ol, p, pre, td, textarea, th, ul{
margin: 0;padding: 0;}
body {font-size: 14px;margin: 0;padding: 0;font-family: Verdana, Arial, Helvetica, sans-serif;}
em{font-style: normal;}
ul li {list-style: none;}
ul{margin: 0;padding: 0;}
.resultimg a{text-decoration:none;color:#515254;}
#container table{border:0px;} 
.resultimg td{margin:20px;border:0px;padding:0px;background:#ffffff;border:solid 1px #CCCCCC;width:100px;height:22px;}
.lazy{width:"220";height:"220";}
.page a{text-decoration:none;color:#515254;}
#pageleft {text-decoration:none;}
.f-inputbox {display: inline-block;*display:inline;zoom:1;width: 43px;height: 27px;padding:0 0 0 5px; color: #333;border: 1px solid #ccc;-webkit-border-radius: 3px;-moz-border-radius: 3px; border-radius: 3px; background: #fff;vertical-align:bottom;}
.f-fuhao {overflow: hidden;float: left;width: 8px;height: 15px;line-height:15px;padding-top:7px;color: #666;}
.inputprice {display: inline-block;*display:inline;zoom:1;width: 32px;height: 15px;line-height: 15px;border: 0 none;padding-top:7px;padding-left:2px;outline: 0;color: #333;}
.inputminq {display:inline-block;height:24px;padding-top:3px;line-height:19px;width:43px;position:relative;top:-5px;border:1px solid #ccc;color:#333;border-radius: 3px;vertical-align: bottom;padding-left:5px;}
.inputmaxq {display:inline-block;height:24px;padding-top:3px;line-height:19px;width:43px;position:relative;top:-5px;border:1px solid #ccc;color:#333;border-radius: 3px;vertical-align: bottom;padding-left:5px;}
.min_name{margin-left:10px;font-size: 16px;position:relative;top:-5px;}
.f-heng {display: inline-block;*display:inline;zoom:1;line-height: 25px;height: 25px;}
.f-heng_m{position:relative;top:-5px;}
#pageright {text-decoration:none;}
#update {background:url(/cbtconsole/img/updaten.png) no-repeat;height:29px;overflow:hidden;width:55px;font-size:16px/14px;line-height:normal;padding-bottom:4px ;border:0 none;margin-top: -5px;}
#update:hover{background-position:-55px 0px;}
#selectdiv {margin-left:45px;position: relative;}
.select{border: 1px solid #ccc;border-radius:3px;height:29px;width:135px;}
.dboto{display:inline-block;height:27px;vertical-align:bottom;zoom:1;}
#divsort{display:inline-block;height:29px;vertical-align:bottom;zoom:1;margin-top: -5px;float:left;}
#divprice {float:left;height:29px;margin-top: -5px;}
#divmoq {float:left;}
#divupdate {float:left;}
#change {width:600px;margin-left: 25px;position: relative;}
#cate{margin-top:0px;margin-left:0px;}
#result{padding-top:5px;position:relative;}
.divimg{width:220px; height:240px;vertical-align: middle;}
.imgclass{max-height:220px;max-width:220px;position:relative;vertical-align:middle;}
.freeshipping{color: #7ab602;}
.shopname{color: #06c;}
.ui-pagination-active{font-size:16px;color:#ff7900;border:1px solid #349ec8;display:inline-block;height:25px;line-height:25px;text-align:center;}
.current{font-size:14px;color:#ff7900;border:1px solid #349ec8;}
.dic-class{width: 994px;height:350px;float:left; margin-top:20px; position: relative;}
.nameclass {min-height:24px;font-size: 14px;}
.priceu{min-height:24px;font-size: 14px;color:#c60f18;font-weight:bold; margin-top: 5px;}
.addnbt{display:none;position:relative;}
.price-div div{height:20px;margin-left: 3px;}
.increce div{height:20px;}
.init-class div{height:20px;}
.ul-item li{margin-left:14px;float:left;clear: none;width: 230px;position: relative;}
.divclass{position:absolute;top:0;left:0;z-index:999;width:230px;border:1px solid #e3e3e3;background:#fff;padding-bottom:5px;}
.divclassyi{position:relative;z-index:999;top:0;left:0;width:224px;/* border:1px solid #e3e3e3; */background:#fff;padding-bottom:2px;}
.main{padding-left:1px;width:229px;background: #ffffff;}
.mainyi{padding-left:1px; width:224px;background: #ffffff;}
#auto_div{width:341px;margin-left:2px;background-color:white;position:absolute;top:35px;left:0;z-index:999;}
#auto_div li{margin-left: 2px;line-height:25px;}
#keywordcate{margin-left: 0px;}
#keywordcate h2{font-size:15px;}
#keywordcate a{text-decoration: none;color: #349ec8;}
#catemove h2{font-size:15px;}
#cateclaer h2{font-size:15px;}
#cateclaer a{text-decoration: none;color: #515254;}
#catemove {margin-left:0px;margin-top:15px;}
#catemove a{text-decoration: none;color: #515254;}
#cateclaer {margin-top:15px;}
#cateclaer hr{width:160px;margin-top:12px;margin-left: 6px;border:1px solid #e3e3e3}
.cate_div_clear {margin-top:10px;}
.cate_div {padding-top:10px;}
.cate_div h2{margin: 6px;}
.cate_div_clear h2{margin: 6px;}
.cate_div ul{margin: 6px;}
.cate_div_clear ul{margin: 6px;}
.cate_div_title{align:left; font-size:12px;padding:4px 0px;}
#keywordcate ul{position:relative;}
.span_dot img{margin-top: 2px;}
.span_jian img{margin-top: 5px;}
.span_dui img{margin-top: 3px;}
#cate_div_active_clear{text-decoration: underline;margin-left: 60px;}
#cate_div_active_clear a{color: #349ec8;margin-top: 10px;}
#cate_div_active_clear img{height:15px;width:15px;}
.ui-pagination li{float:left;margin-left: 5px;}
.mainbody {postion:relative;width:966px;margin-left:0px;background:#fff;border:1px solid #e2e2e2;border-top:0;}
</style>
<script type="text/javascript">
$(function(){
	/*******************************sortmatch************************************************/
	var keywords = decodeURIComponent(document.getElementById("keywordsm").value);
	var price1 = document.getElementById("price1").value;
	var price2 = document.getElementById("price2").value;
	var minq = document.getElementById("minq").value;
	var maxq = document.getElementById("maxq").value;
	var orderNo = document.getElementById("source-orderno").value;
	var goodId = document.getElementById("source-goodsid").value;
	var aliUrl = document.getElementById("source-aliurl").value;
	var sortmatch = document.getElementById("sortmatch");
	if(sortmatch){
		sortmatch.onchange = function(){
			window.location.href = "/cbtconsole/goodswebsite?keyword="+encodeURIComponent(encodeURIComponent(keywords))+"&price1="+price1+
					"&price2="+price2+"&minq="+minq+"&maxq="+maxq+"&website=o&srt="+sortmatch.value+
					"&orderNo="+orderNo+"&goodId="+goodId+"&aliUrl="+aliUrl;
		}
	}
	/***********************************sortmatch**********************************************/
	/*******************************update************************************************/
	var update = document.getElementById("update");
	if(update){
		update.onclick = function(){
			window.location.href = "/cbtconsole/goodswebsite?keyword="+encodeURIComponent(encodeURIComponent(keywords))+
			"&price1="+price1+"&price2="+price2+"&minq="+minq+"&maxq="+maxq+"&website=o&srt="+sortmatch.value
			+"&orderNo="+orderNo+"&goodId="+goodId+"&aliUrl="+aliUrl;;
		}
	}
	/***********************************update**********************************************/
	
	var srt='${param.srt}';
	if(srt.trim()!=""){
		$("#sortmatch option[value='"+srt+"']").attr('selected','selected'); 
	}
	var catid='${param.catid}';
	if(catid.trim()!=""){
		$("#catID option[value='"+catid+"']").attr('selected','selected'); 
		var select = document.getElementById("search-category-value");
		if(select){
			var options = document.getElementById("catID");
			select.innerHTML=options.options[options.options.selectedIndex].text;
		}
	}
});
function checkNum(obj) {
    //检查是否是非数字值
    if (isNaN(obj.value)) {
        obj.value = "";
    }
    if (obj != null) {
        //检查小数点后是否对于两位
        if (obj.value.toString().split(".").length > 1 && obj.value.toString().split(".")[1].length > 2) {
            obj.value = "";
        }
    }
};

function checkMO(obj) {
    //检查是否是非数字值
    if (isNaN(obj.value)) {
        obj.value = "";
    }
    if (obj != null) {
    	if(obj.value.toString().substring(0,1) == 0){
    		obj.value = "";
    	}
        //检查小数点后是否对于两位
        if (obj.value.toString().split(".").length > 1) {
            obj.value = "";
        }
    }
};

/* function fnadd(obj,url,name,moq,price,img){
	if(obj.checked==true){
	   
	}
}
 */
function fnadd(obj,purl,name,price,img,url,goodId,orderNo){
	 
	 //alert($(obj).attr("checked")=="checked");
	 var checkFlag= $("#"+obj).is(':checked');
/* 	 alert(obj);
	//商品连接
	alert(purl);
	//名字+最小订量
	alert(name+","+moq);
	//价格+图片路径
	alert(price+","+img);
	alert(goodId+","+url); */
/* 	window.location = "/cbtconsole/WebsiteServlet?action=addSource&className=GoodsWebsiteServlet&purl="+purl+"&name="+name
			+"&price="+price+"&img="+img+"&url="+url+"&goodsid="+goodId+"&orderNo="+orderNo+"&checkFlag="+checkFlag;  */
	$.ajax({
		type:'POST',
		async:false,
		url:'/cbtconsole/WebsiteServlet?action=addSource&className=GoodsWebsiteServlet',
		data:{purl:purl,name:name,price:price,img:img,url:url,goodsid:goodId,checkFlag:checkFlag},
		success:function(){	
		
		}
	});
}
 
 function fnSearch(aliUrl,goodId,orderNo){
	 //alert(aliUrl);
	 var keyword = $("#goodsNameWord").val();
	 window.location = "/cbtconsole/WebsiteServlet?action=doPost&className=GoodsWebsiteServlet&keyword="+keyword+"&aliUrl="+aliUrl+"&goodId="+goodId+"&orderNo="+orderNo;
 }

 
 
</script>
</head>
<body>
<div style="display: none">
<h1 id="keyword" style="display: none">${param.keyword}</h1>
<input id="keywordsm" type="hidden"  value="${param.keyword}">
<input id="source-url" type="hidden"  value="${param.keyurl}">
<input id="source-orderno" type="hidden"  value="${param.orderNo}">
<input id="source-goodsid" type="hidden"  value="${param.goodId}">
<input id="source-aliurl" type="hidden"  value="${param.aliUrl}">
</div>
 <c:set var="keywordsr" value="${fn:replace(param.keyword,' ','%20')}"/>
 <c:set var="keywordsr" value="${fn:replace(keywordsr,'&','%26')}"/>
<div class="newprobody">
<div class="newconmain">
	    <div style="float:left;fwidth:967px;margin:0 auto;background:#fff;border:1px solid #e2e2e2;">
	      <div id="div1" style="width:810px;height:80px;position:relative;">
		       <div>
					<div id="selectdiv" style="padding-top: 15px;">
						<div id="divprice">
								<span class="prriwali">Price:</span>
								<div class="f-inputbox">
									<span class="f-fuhao">$</span>
									<input id="price1" class="inputprice" type="text" size="6" value="${param.price1}" onkeyup="checkNum(this)">
								</div> 
								<span class="f-heng">-</span>
								<div class="f-inputbox">
									<span class="f-fuhao">$</span>
									<input id="price2" class="inputprice" type="text" size="6" value="${param.price2}" onkeyup="checkNum(this)">
								</div>
						</div>
						<div class="dboto" id="divmoq">
							<span class="min_name">Min.Order:</span>
							<input id="minq" class="inputminq" type="text" size="6" value="${param.minq}" onkeyup="checkMO(this)">
							<span class="f-heng f-heng_m">-</span>
							<input id="maxq" class="inputmaxq" type="text" size="6" value="${param.maxq}" onkeyup="checkMO(this)" >
						</div>
						<input class="dboto" id="update" type="submit" value="" style="color:#fff;float:left;">	
						<div class="dboto" id="divsort">
									<span class="prriwalii">Sort By:</span>
									<select id="sortmatch" class="select">
										<option value="default">Best Match</option>
										<option value="bbPrice-asc">Price Low To High</option>
										<option value="bbPrice-desc">Price High To Low</option>
										<option value="order-desc">Best Selling</option>
									</select>
						</div> 
						<a style="text-decoration: none;" href="/cbtconsole/customerServlet?action=findOrdersPurchaseInfo&className=OrdersPurchaseServlet&orderNo=${orderNo}"  target="_blank">
							<input id="save" type="button" style="height: 30px; margin-top: -20px; margin-left: 8px;" value="保存货源" >	
						</a>
						<br />
						
						<div style="clear:both; margin-top:10px;">keyword:<input style="height: 24px;width:260px;" type="text" value=<%=new String(request.getParameter("keyword").getBytes("ISO8859-1"),"UTF-8") %> id="goodsNameWord">
						<input id="search" type="button" style="height: 30px; margin-top: -20px; margin-left: 8px;" value="搜索" onclick="fnSearch('${param.aliUrl}','${param.goodId}','${param.orderNo}')"></div>
				</div>
			
			</div>
	    </div>
	<c:set var="pagemap" value="${pagemap}"/> 
    <c:set var="end" value="${fn:length(goodslist)}"/>
    <div id="container">
      <div>
      <c:choose>
    <c:when test="${check_result!='0'}"> 
    <c:if test="${check_result=='1'}">
	    <div  style="font-size:18px;color:#ff0000;" id="keywordtable">
	    Sorry, but we don't sell fake brands or any products that infringe other company's brand and intellectual rights.Please try other keywords.
		</div>
    </c:if>
    <c:if test="${check_result=='2'}">
	    <div  style="font-size:18px;color:#ff0000;" id="keywordtable">
	    Sorry, but we can not purchase and deliver any kind of <span>${param.keyword }</span>.
		</div>
    </c:if>
    <c:if test="${check_result=='3'}">
	    <div  style="font-size:18px;color:#ff0000;" id="keywordtable">
	    Sorry, but we can not purchase and deliver any product in this category,please change other category.
		</div>
    </c:if>
    </c:when>
    <c:otherwise>
      <c:choose>
         <c:when test="${end>0}"> 
        <c:set var="i" value="1"/>  
		<div id="result" class="resultimg" style="float:left;position:relative;"> 
	       <div style="margin-left:-15px; " id="keywordtable"> 
	            <c:if test="${pagemap['page amount']['key_name']-pagemap['page current']['key_name']>0}">
			         <c:set var="goodspagenum" value="${end}"/> 
			         <c:set var="end" value="${goodspagenum%goodsnum !=0 ? goodspagenum-(goodspagenum%goodsnum) : goodspagenum}"/>
			     </c:if>
			     <c:if test="${end==0}">
			        <c:set var="end" value="1"></c:set>
			     </c:if>
			     <c:forEach var="goodsmap"  items="${goodslist}" end="${end-1}" > 
				     <c:if test="${i%goodsnum==1}">
				       <ul class="ul-item">
				     </c:if>
				         <li style="min-height: 375px;">  
				         <c:set value="${ fn:split(goodsmap['goods_name'],'+')}" var="goodsname" /> 
				         <c:set value="${ fn:length(goodsname)}" var="goodsnamesize" />
				          <div  class="divclass" style="font-size:13px;" > 
				           <c:if test="${i<=8}">
				           		<div class="divimg">  
					            	<a target="_blank" href="/cbtconsole/processesServlet?action=getSpider&amp;className=SpiderServlet&url=${goodsmap['goods_url']}" ><img class="imgclass" style="border:0;" src="${goodsmap['goods_image']}" /></a>
					           </div>
				           </c:if>
				           <c:if test="${i>8}">
					           <div class="divimg"> 
					            	<a target="_blank" href="/cbtconsole/processesServlet?action=getSpider&amp;className=SpiderServlet&url=${goodsmap['goods_url']}" ><img class="imgclass" style="border:0;" src="/cbtconsole/img/wy/grey.gif" data-original="${goodsmap['goods_image']}" width="640" height="640"/></a>
					           </div>
				           </c:if>
				           <div class="main">
				            <div class="nameclass" align="left">
					             <a target="_blank" href="/cbtconsole/processesServlet?action=getSpider&amp;className=SpiderServlet&url=${goodsmap['goods_url']}" title="${fn:replace(goodsmap['goods_name'], '+', '')}">
						              <span class="init-class">							               
							               <span class="name_one">
							                  &nbsp;${goodsname[0]}
							               </span>
							               <br>
							               <span class="name_two">
							                &nbsp;${goodsname[1]}
							               </span>
						              </span>
						              <span class="increce" style="display:none">
							               <span class="name_three">
							                &nbsp;${goodsname[2]}
							               </span>
							               <br>
							               <span class="name_four">
							                &nbsp;${goodsname[3]}
							               </span>
							           </span>
					              </a>
				            </div>
				            <div>
				              <div class="price-div" align="left">
				              <div class="priceu">
					               <c:if test="${goodsmap['goods_price']!='null'&& goodsmap['goods_price']!=''}">
					                     $${goodsmap['goods_price']}&nbsp;
					               </c:if> 
				              </div>
				              <div class="minorder">
					                   Sold:${goodsmap['goods_solder']}
					                   <%-- <span style="margin-left:28px;">MOQ:${goodsmap['goods_minOrder']}</span> --%>
				              </div>
				              <div class="add">
				              	<%-- Add to supply of goods：<input class="checkbox" type="checkbox" 
				              	onclick="fnadd(this,'${fn:replace(goodsmap['goods_url'],'&','%26')}','${fn:replace(goodsmap['goods_name'],'\'','%27') }','${goodsmap['goods_minOrder']}','${goodsmap['goods_price']}','${goodsmap['goods_image']}')" style="width: 20px;height: 20px;"> --%>
				              	
				              	<%-- <input type="button" value="选为货源" style="height:30px;"
				              	onclick="fnadd(this,'${goodsmap['goods_url']}','${goodsmap['goods_name']}','${goodsmap['goods_price']}','${goodsmap['goods_image']}','${aliUrl}','${goodId}','${orderNo}')" style="width: 20px;height: 20px;"> --%>
				              	
				              	Add to supply of goods：<input type="checkbox" class="checkbox" id="check${i}"
				              	onclick="fnadd('check${i}','${goodsmap['goods_url']}','${goodsmap['goods_name']}','${goodsmap['goods_price']}','${goodsmap['goods_image']}','${aliUrl}','${goodId}','${orderNo}')" style="width: 20px;height: 20px;">
				              </div>
				             </div>
				            </div>
				           </div>
				          </div>
				        </li>    
			            <c:if test="${i%goodsnum==0}">
			            </ul>
			           <!-- </div> -->
			          </c:if>
				      <c:set  var="i" value="${i+1}" />
				  </c:forEach>
	       </div>
		</div>
		<!-- 
		 -->
	    <div id="clear"></div>
	   
	    <div id="page" class="page" style="margin-left:37%;margin-top:1%;position: relative;height:25px;line-height:25px;font-size:16px;"> 
	       <c:set var="replacecat" value="${fn:replace(param.cat,' ','%20')}"/> 
	       <c:set var="replaceflag" value="/cbtconsole/goodswebsite?goodId=${param.goodId}&orderNo=${param.orderNo}&aliUrl=${param.aliUrl}&keyword"/> 
	       <c:set var="pagemap2" value="${fn:replace(pagemap['next page']['key_name'],'/cbtconsole/goodswebsite?keyword',replaceflag)} "/>
	       <span id="pagenext">    
	            ${fn:replace(pagemap2,'&&','')}
	       </span>
	       <div id="clear2"></div> 
	     </div>
	     </c:when>
         <c:otherwise>
               <div style="font-size:18px;color:#ff0000;" id="keywordtable">Sorry, no matches were found in products for "${param.keyword}".</div>
         </c:otherwise>
       </c:choose>
         </c:otherwise>
       </c:choose>
	    </div>
	    </div>
    
	</div>
	    <div style="clear: both;"></div>
	</div>
	<div style="clear:both;"></div>
</div>

	
<div style="clear:both;"></div>
<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.js"></script>
<script type="text/javascript">
	$(function(){ 
		$(".imgclass").lazyload({
		effect:'fadeIn',
		threshold : 300
		});
	});
</script>
</body>
</html>