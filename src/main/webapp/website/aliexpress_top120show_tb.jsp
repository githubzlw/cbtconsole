<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgcore.lhgdialog.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.min.js?self=true&skin=discuz"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<title>aliexpress搜索后图片进行的淘宝相关图片显示</title>
</head>
<script type="text/javascript">
function fnSave(){
	var tab_goods = $("#tab_goods tr").length;
	var sourch_number = 0;
	var jsons = '';
	for (var i = 1; i < tab_goods; i++) {
		var input_val = $("#val_"+i).val();
		var val_state = $("#val_state"+i).val();
		if(val_state == 0){
			sourch_number ++;
		}
		/* if(input_val == ""){
			alert("第"+i+"条数据没有选择");
			return;
		} */
		if(input_val == "1" || input_val == ""){//之前已选择过不保存
			continue;
		}
		jsons += input_val;
		if(i != tab_goods-1){
			jsons += ",";
		}
	}
	jsons = "[" + jsons + "]";
	$.post("/cbtconsole/WebsiteServlet",
			{action:'saveAliexpressSourch',className:'Aliexpress_top120',datainfo:jsons,typeid:'${param.id}',search_number:sourch_number},
			function(res){
				alert(res);
			});
}

function fnChangeTb(val,index,id,url,purl,img,price,name,repeat,state){
	$("#bnt_"+index).css("border","");
	//repeat 是否原先已选择过该货源
	//state 之前有无货源状态
	if(repeat == 1){
		$("#val_"+index).val(1);
		return;
	}
	$("#val_state"+index).val(0);
	$("#val_"+index).val('{"gid":'+id+',"url":"'+url+'","purl":"'+purl+'","img":"'+img+'","price":"'+price+'","name":"'+name+'","sourceType":"TB","state":' + state + '}');
	$("#tab_goods tr").eq(index).find("img").css("border","2px solid transparent");
	$(val).css("border","2px solid red");
	//$("#val_"+index).val(id + "&&" + url + "&&" + purl + "&&" + img + "&&" + price + "&&" + name);
}

function fnNoSource(val,index,id,state){
	if(state == 0){//无货源
		$("#val_"+index).val('{"gid":"'+id+'","url":"","purl":"","img":"","price":"","name":"","sourceType":"TB","state":0}');
		$("#val_state"+index).val(1);
		$("#tab_goods tr").eq(index).find("img").css("border","2px solid transparent");
		$(val).css("border","2px solid red");
	}
}

function fnAddSource(index,id,url,state){
	$.dialog({
		id:'ao',
		width: '800px',
	    height: 310,
		fixed: true,
		max: false,
	    min: false,
		skin: 'discuz',
	    lock: true,
	    title: '手动添加货源',
	    drag: false,
	    content: 'url:/cbtconsole/website/aliexpress_top120sourch.jsp?typeid='+'${param.id}'+"&url="+encodeURIComponent(url)+"&state="+state+"&index="+index+"&gid="+id,
	    close: function () {
	    }
	});
}
//手动添加货源成功后回调
function fnAddSource_back(index,url){
	$("#bnt_"+index).css("border","");
	$("#bnt1_"+index).css("border","2px solid red");
	$("#sj_url"+index).append('<a target="_blank" href="'+ url +'">查看链接</a>&nbsp;');
	$("#tab_goods tr").eq(index).find("img").css("border","2px solid transparent");
	$("#val_"+index).val('1');
}

function fnCansonlSource(index){
	$("#bnt_"+index).css("border","");
	$("#tab_goods tr").eq(index).find("img").css("border","2px solid transparent");
	$("#val_"+index).val('1');
}
</script>
<body>
<div align="center">
	<h2>aliexpress搜索后图片进行的淘宝相关图片显示</h2>
	<table border="1" id="tab_goods">
		<tr>
			<td>序号</td>
			<td>Aliexpress</td>
			<td>淘宝搜索1</td>
			<td>淘宝搜索2</td>
			<td>淘宝搜索3</td>
			<td>淘宝搜索4</td>
			<td>其他货源</td>
			<td>其他操作</td>
		</tr>
	<c:forEach items="${results}" var="goodsCheckBean" varStatus="i" >
		<tr>
			<td>${i.index+1}</td>
			<td style="color: red;"><img title="${goodsCheckBean.goodsName}" style="border:2px solid transparent" width="170" height="170" src="${goodsCheckBean.imgpath}"><br>${goodsCheckBean.price}<a href="${goodsCheckBean.url}">查看链接</a></td>
			<c:set value="0" var="goodsSourch_tb"></c:set>
			<c:set value="" var="goodsSourch_sj_urls"></c:set>
			<c:forEach items="${goodsCheckBean.aliSourceList}" var="goodsSourch" varStatus="j">
			<c:choose>
				<c:when test="${goodsSourch.aliSourceUrl==goodsCheckBean.tbUrl}">
					<c:set value="1" var="goodsSourch_tb"></c:set>
				</c:when>
				<c:when test="${goodsSourch.aliSourceUrl==goodsCheckBean.tbUrl1}">
					<c:set value="2" var="goodsSourch_tb"></c:set>
				</c:when>
				<c:when test="${goodsSourch.aliSourceUrl==goodsCheckBean.tbUrl2}">
					<c:set value="3" var="goodsSourch_tb"></c:set>
				</c:when>
				<c:when test="${goodsSourch.aliSourceUrl==goodsCheckBean.tbUrl3}">
					<c:set value="4" var="goodsSourch_tb"></c:set>
				</c:when>
				<c:otherwise>
					<c:set value="5" var="goodsSourch_tb"></c:set>
					<c:set value="${goodsSourch_sj_urls}@${goodsSourch.aliSourceUrl}" var="goodsSourch_sj_urls"></c:set>
				</c:otherwise>
			</c:choose>
			</c:forEach>
			<td style="color: red;"><img style="border:2px solid ${goodsSourch_tb == 1?'red':'transparent'}" title="${goodsCheckBean.tbName}" width="170" height="170" onclick="fnChangeTb(this,${i.index+1},${goodsCheckBean.pId},'${goodsCheckBean.url}','${goodsCheckBean.tbUrl}','${goodsCheckBean.tbImg}','${goodsCheckBean.tbprice}','${goodsCheckBean.tbName}',${goodsSourch_tb == 1?1:0},${goodsCheckBean.aligSourceUrl})" src="${goodsCheckBean.tbImg}"><br>${goodsCheckBean.tbprice}<a target="_blank" href="${goodsCheckBean.tbUrl}">查看链接</a></td>
			<td style="color: red;"><img style="border:2px solid ${goodsSourch_tb == 2?'red':'transparent'}" title="${goodsCheckBean.tbName1}" width="170" height="170" onclick="fnChangeTb(this,${i.index+1},${goodsCheckBean.pId},'${goodsCheckBean.url}','${goodsCheckBean.tbUrl1}','${goodsCheckBean.tbImg1}','${goodsCheckBean.tbprice1}','${goodsCheckBean.tbName1}',${goodsSourch_tb == 2?1:0},${goodsCheckBean.aligSourceUrl})" src="${goodsCheckBean.tbImg1}"><br>${goodsCheckBean.tbprice1}<a target="_blank" href="${goodsCheckBean.tbUrl1}">查看链接</a></td>
			<td style="color: red;"><img style="border:2px solid ${goodsSourch_tb == 3?'red':'transparent'}" title="${goodsCheckBean.tbName2}" width="170" height="170" onclick="fnChangeTb(this,${i.index+1},${goodsCheckBean.pId},'${goodsCheckBean.url}','${goodsCheckBean.tbUrl2}','${goodsCheckBean.tbImg2}','${goodsCheckBean.tbprice2}','${goodsCheckBean.tbName2}',${goodsSourch_tb == 3?1:0},${goodsCheckBean.aligSourceUrl})" src="${goodsCheckBean.tbImg2}"><br>${goodsCheckBean.tbprice2}<a target="_blank" href="${goodsCheckBean.tbUrl2}">查看链接</a></td>
			<td style="color: red;"><img style="border:2px solid ${goodsSourch_tb == 4?'red':'transparent'}" title="${goodsCheckBean.tbName3}" width="170" height="170"  onclick="fnChangeTb(this,${i.index+1},${goodsCheckBean.pId},'${goodsCheckBean.url}','${goodsCheckBean.tbUrl}','${goodsCheckBean.tbImg3}','${goodsCheckBean.tbprice3}','${goodsCheckBean.tbName3}',${goodsSourch_tb == 4?1:0},${goodsCheckBean.aligSourceUrl})" src="${goodsCheckBean.tbImg3}"><br>${goodsCheckBean.tbprice3}<a target="_blank" href="${goodsCheckBean.tbUrl3}">查看链接</a></td>
			<td style="color: red;" id="sj_url${i.index+1}">
			<c:if test="${goodsSourch_tb == 5}">
				<c:forEach items="${fn:split(goodsSourch_sj_urls,'@')}" var="goodsSourch_sj_url">
					<c:if test="${goodsSourch_sj_url!=''}">
						<a target="_blank" href="${goodsSourch_sj_url}">查看链接</a>&nbsp;
					</c:if>
				</c:forEach>
			</c:if>
			
			</td>
			<td style="color: red;"><!-- <input type="button" value="查看手动添加" style="font-size: 20px;"><br> -->
			<input type="button" id="bnt1_${i.index+1}" onclick="fnAddSource(${i.index+1},${goodsCheckBean.pId},'${goodsCheckBean.url}',${goodsCheckBean.aligSourceUrl});" value="手动添加" style="font-size: 20px;"><br>
			<input style="font-size: 20px;${goodsCheckBean.aligSourceUrl=='1'?'border:2px solid red':''}" id="bnt_${i.index+1}" onclick="fnNoSource(this,${i.index+1},${goodsCheckBean.pId},${goodsCheckBean.aligSourceUrl})" type="button" style="${goodsCheckBean.aligSourceUrl=='1'?'border:1px solid red':''}" value="无货源" style="font-size: 20px;">
				<input type="button" id="bnt1_${i.index+1}" onclick="fnCansonlSource(${i.index+1});" value="取消" style="font-size: 20px;">
				<input type="hidden" value="" id="val_${i.index+1}"><input type="hidden" value="${goodsCheckBean.aligSourceUrl}" id="val_state${i.index+1}">
			</td>
		</tr>
	</c:forEach>
	</table>
	<div><br><input type="button" onclick="fnSave()" value="确认并保存" style="font-size: 20px;"></div>
</div>
</body>
</html>