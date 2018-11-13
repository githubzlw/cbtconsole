<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="../css/eight.css"/>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>产品搜索结果页面</title>
<script type="text/javascript">
function fnsave(){
	var pids = "";
	$("input[name='checkbox']:checked").each(function(){
		pids += this.value+ ',';
	});
	if(pids===''){
		return ;
	}
	var indexId = $("#indexid").val();
	if(indexId===''||indexId==='0'){
		return ;
	}
	var page = $('#current').val();
	scroll(0,0);
	$(".egsput").html("正在保存中，请耐心等候........");
	$.ajax({
		type:'POST',
		dataType:'text',
		url:'/cbtconsole/SearchByPicController/saveGoods',
		data:{indexId:indexId,pids:pids,page:page},
		success:function(res){
			var rest = parseInt(res);
			var currentpage = parseInt($('#current').val());
			if(rest>0){
				var indexid = $('#indexid').val();
				if(res>currentpage){
					var  page =currentpage+1;
				   $(".egsput").html("第"+currentpage+"页保存成功,<a href='/cbtconsole/SearchByPicController/resultlist?indexid="+
						   indexid+"&page="+page+"'>请继续操作下一页</a>");
				}else{
					$(".egsput").html("第"+currentpage+"页保存成功");
				}
				
				$(".eixcon").children().remove();
				$(".downpage").remove();
			}else{
				$(".egsput").html("第"+currentpage+"页保存失败");
				$(".downpage").remove();
				$(".eixcon").children().remove();
			}
		},
		error:function(XMLResponse){
			alert('error');
		}
	});
}

function fnpage(){
	var total = $('#total').val();
	var pageNext = parseInt($('#pageNext').val());
	if(pageNext==''||pageNext>total || pageNext < 1 ){
		return;
	}
	var indexid = $('#indexid').val();
	var minprice = $('#minprice').val();
	var maxprice = $('#maxprice').val();
	window.location.href="/cbtconsole/SearchByPicController/resultlist?indexid="+indexid+"&page="+pageNext+
	"&minprice="+minprice+"&maxprice="+maxprice;
}
function pageScroll() { 
	window.scrollBy(0,-10); 
	scrolldelay = setTimeout('pageScroll()',100); 
	}
	
function checkall(){
	var name = $("#eikeywords").val();
	if(name == ""){
	    alert("屏蔽词不可为空");
	    return false;
	}

	if(indexid == ""){
	    alert("index id 不可为空");
	    return false;
	}
	}
function  fntr(){
	var indexid = $('#indexid').val();
	var page = $('#current').val();
	window.location.href="/cbtconsole/SearchByPicController/retrans?indexid="+indexid+"&page="+page;
}
function  fnprice(){
	var indexid = $('#indexid').val();
	var page = $('#current').val();
	var minprice = $('#minprice').val();
	var maxprice = $('#maxprice').val();
	window.location.href="/cbtconsole/SearchByPicController/resultlist?indexid="+indexid+"&page="+
			page+"&minprice="+minprice+"&maxprice="+maxprice;
}

//检查是否是非数字值
function checkNum(obj) {
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
</script>
</head>
<body>
<div style="width: 1350px;margin: 0 auto;margin-top: 5px;" align="center">
	<span id="egsput" class="egsput" style="width: 500px;height:100px;color: red;font-size: 50px;"></span>
</div>
<div class="eixcon">
	<input type="button" class="eigbtn" value="增加屏蔽关键词" onclick="$('.eiping').toggle()"/>
	<input type="button" class="eigbtn" value="翻译" onclick="fntr()"/>
	<span>价格：<input type="text" class="eigbtni" id="minprice" class="minprice" value="${minprice}" onkeyup="checkNum(this)"/>
	-
	<input type="text" class="eigbtni" id="maxprice" class="maxprice" value="${maxprice}" onkeyup="checkNum(this)"/>
	</span>
	
	<input type="button" class="eigbtn" id="search" class="search" value="搜索" onclick="fnprice()"/>
	<div class="eiping">
	<form action="${ctx }/SearchByPicController/update" method="post" onsubmit="return checkall()">
		<span>屏蔽关键词:</span>
		<input type="hidden" value="${indexid}" id="indexid" class="indexid" name="indexid">
		<input type="hidden" class="current" id="current" value="${current}" name="page"> 
		<input type="text" name="keywords" value="${keywords}" class="eireinp"  id="eikeywords" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
		<input type="submit" class="eigbtn" style="margin-left:0;display:inline-block;margin-left:15px;" value="屏蔽"/>
	</form>
	</div>
	<div class="eiresul">
	<c:forEach  var="item" items="${items}"  varStatus="status">
	<li class="eireli">
				<div class="eirelidcon">
					<div class="eireimg">
						<a href="${item.goodsUrl }">
							<img width="250px" height="250px" class="rimgai" src="/cbtconsole/img/wy/grey.gif" data-original="${item.goodsImg }">
						</a>
					</div>
					<p class="eritxt">
						<c:if test="${item.goodsValid==1}">
						<input type="checkbox" checked="checked" name="checkbox" value="${item.goodsPid}" id="${item.goodsPid}"/>
						</c:if>
						<c:if test="${item.goodsValid!=1}">
						<input type="checkbox"  name="checkbox" value="${item.goodsPid}" id="${item.goodsPid}"/>
						</c:if>
						
						<label for="${item.goodsPid}" class="check-box"></label> 
						
						
						<a href="${item.goodsUrl }"><span class="eirpname" title="${item.goodsName}">${item.goodsName}</span></a>
					</p>
					<p class="eritxt">
						<span class="eirprice"><em>$</em>${item.goodsPriceRe }</span>
						<span class="eirpsold">
						<em>${item.trade}</em>
						</span>
					</p>
					<p class="eritxt ericompn">
						公司名：<span title="${item.factoryName}">${item.factoryName}</span>
					</p>
				</div>
			</li>
	
	</c:forEach>
	
		
	</div>
	<input type="button" class="eigbtn" id="savebtn" style="margin-left:0;" value="保存搜索结果" onclick="fnsave()"/>
</div>
<div align="center" id="downpage" class="downpage">
<input type="hidden" id="total" value="${total}"> 
<c:set var="add" value=""></c:set>
<c:set var="release" value=""></c:set>
<c:if test="${current>1}">
<c:set var="release" value="/cbtconsole/SearchByPicController/resultlist?indexid=${indexid}&page=${current-1}&minprice=${minprice}&maxprice=${maxprice}"></c:set>
</c:if>
<c:if test="${total>current}">
<c:set var="add" value="/cbtconsole/SearchByPicController/resultlist?indexid=${indexid}&page=${current+1}&minprice=${minprice}&maxprice=${maxprice}"></c:set>
</c:if>

<div>当前页/总页数:&nbsp;<span>${current}/${total}</span>
	<input type="button" value="上一页" onclick="javaScript:window.location.href='${release}'">
	<input type="button" value="下一页" onclick="javaScript:window.location.href='${add}'">
	 至<input type="text" value=""  id="pageNext"><input type="button" value="跳转" onclick="fnpage()"  >
	页
</div>

</div>
<br>
<script type="text/javascript" src="/cbtconsole/js/jquery.lazyload.js"></script>
<script type="text/javascript">
$(function() {
    $(".rimgai").lazyload({
    	effect:'fadeIn',
    	threshold : 300
    });
});
</script>
</body>
</html>