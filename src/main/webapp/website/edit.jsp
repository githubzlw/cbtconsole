<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
 <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/xheditor-1.2.2.min.js"></script>
    <script type="text/javascript" src="/cbtconsole/js/xheditor_lang/zh-cn.js"></script>
<title>Editor</title>


 <style type="text/css">
    body {
	  width: 100%;
   }
    
    </style> 
    
<script type="text/javascript">
			window.onload=function(){
				var dd=$("iframe").contents().find("body").find('img'); 
				for(var i=0;i<dd.length;i++){
					dd[i].onclick=function(){
						var img = '<img src="'+this.src+'"/>';
						$("#deleteimage").val(img);
					}
				}
			}
</script>
</head>
<body class="body">
<div align="center" class="xheditor" >
<div>
	<form action="/cbtconsole/editc/save" method="post"  id="form_save">
	<!-- 文件上传保存路径 -->
	<input type="hidden" id="savePath" value="${savePath}" name="savePath">
	<input type="hidden" id="pid" value="${pid}" name="pid">
	<input type="hidden" id="localpath" value="${localpath}" name="localpath">
	<div id="baseinfo">
	<div>
	<span >&nbsp;&nbsp;&nbsp;关键词:</span>
	<textarea rows="2" cols="88"  name="goodskeyword">${goods.keyword}</textarea>
	<span >产品名称:</span>
	<textarea rows="2" cols="88" id="info_name" class="info_name" name="goodsname">${goods.enname}</textarea>
	</div>
	<div>
	<span >产品价格:</span>
	<textarea rows="2" cols="88" name="goodsprice">${goods.price}</textarea>
	<span >产品重量:</span>
	<textarea rows="2" cols="88" name="goodsweight">${goods.weight}kg</textarea><span></span>
	</div>
	<div><span>产品明细:</span><textarea rows="8" cols="190.7" name="goodsdetail">${goods.endetail}</textarea></div>
	
	<input type="hidden" value="${goods.lastPrice}" name="lastPrice">
	<c:if test="${skuAttrListSize > 0 }">
	<input id="skuAttrListSize_${skuAttrListSize}" value="${skuAttrListSize}" name="skuAttrListSize" type="hidden" >
	<div><span style="width: 68px;">产品规格:</span>
	<table border="1"  style="width:60%" align="center" height = 300px cellspacing=0  bordercolor=#000000>
	<c:forEach items="${skuAttrList}" var="skuPrice">
	<tr>
	<c:forEach items="${skuPrice.typeList}" var="typeList">
	<td> ${typeList.type} </td>
	<td>${typeList.value}</td>
	</c:forEach>
	<td>
	<input id="skuPropIds_${skuPrice.skuAttrBean.skuPropIds}" value="${skuPrice.skuAttrBean.skuPropIds}" name="skuPropIds" type="hidden" >
	<input type="text" value="${skuPrice.skuAttrBean.skuVal.actSkuCalPrice}" name="actSkuCalPrice_${skuPrice.skuAttrBean.skuPropIds}" />
	</td>
	</tr>
	</c:forEach>
	</table>
	</div>
	</c:if>
	
	<%-- <div><span style="width: 68px;">快递方式:</span><textarea rows="2" cols="211" name="goodsmethod">${goods.method}</textarea></div>
	<div><span style="width: 68px;">快递时间:</span><textarea rows="2" cols="211" name="goodsposttime">${goods.posttime}</textarea></div>
	<div><span style="width: 68px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;运费:</span><textarea rows="2" cols="211" name="goodsfeeprice">${goods.feeprice}</textarea></div>
	 --%>
	</div>
	<!-- 待编辑数据 -->
	<span>产品详情:</span>
	<textarea id="xheditor" rows="35" cols="211" name="content" class="xheditor {tools:'|,/,Cut,Copy,Paste,Pastetext,Blocktag,Fontface,FontSize,Bold,Italic,Underline,Strikethrough,FontColor,BackColor,SelectAll,Removeformat,Align,List,Outdent,Indent,Link,Unlink,Anchor,Img,Hr,Table,Source,Preview,Fullscreen,',html5Upload:false,skin:'vista',upImgUrl:'/cbtconsole/editc/uploads?savePath=${savePath}',upImgExt:'jpg,jpeg,png',onUpload:insertUpload}">
	   ${text}
	</textarea>
	
	<textarea style="display:none;" rows="35" cols="211" name="sku">${sku}</textarea>
	
	<input  id="form_submit"  type="submit" value="保存" style="width: 79%;height: 50px;background: #E0E8F5;font-size: 25px;margin-left: 66px;">
	</form>
	
	
</div>
	<div>
	<div style="margin-left: 80px;">
	~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	</div>
	<form action="/cbtconsole/editc/delete" method="post">
	<input id="deletepid" name="deletepid" type="hidden" value="${pid}">
	<input id="deletecatid" name="deletecatid" type="hidden" value="${goods.catid}">
	
	<div><span style="width: 98px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	<textarea id="deleteimage" rows="30" cols="211" name="deleteimage" class="xheditor {tools:'|,',html5Upload:false,skin:'vista'}">
	</textarea>
	</div>
	
	<input  id="delete_submit"  type="submit" value="删除" style="width: 79%;height: 50px;background: #E0E8F5;font-size: 25px;margin-left: 66px;">
	</form>
	
	</div>
</div>

    <script type="text/javascript">
    
    //alert($("#xheditor img").length);
    
    /* 图片回调函数  根据实际情况调用*/
    function  insertUpload(msg){
    }
    </script>
</body>
</html>