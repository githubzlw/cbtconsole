<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link type="text/css" rel="stylesheet" href="/css/baise.css"/>
<link type="text/css" rel="stylesheet" href="../css/eight.css"/>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>基本信息</title>
<script type="text/javascript">
function checkall(){
	var name = $("#eiputen").val();
	if(name == ""){
	    alert("产品英文名称不可为空");
	    return false;
	}
	}

</script>
</head>
<body>
<h3 class="eititl">基本信息</h3>
<div class="eixcon">
	<table class="eitable">
	  <tr>
	     <td>订单数量:</td>
	     <td>${index.quantity }</td>
	     <td>客户邮箱:</td>
	     <td>${index.email }</td>
	     <td>客户留言:</td>
	     <td>${index.comments }</td>
	  </tr>
	  <tr>
	     <td>价格区间:</td>
	     <td>${index.minu }-${index.maxu }美元</td>
	     <td>提交时间:</td>
	     <td>${index.createtime }</td>
	     <td>&nbsp;</td>
	     <td>&nbsp;</td>
	  </tr>
	
	</table>
</div>
<div class="eixcon">
	<form method="post" action="/cbtconsole/SearchByPicController/save" onsubmit="return checkall()">
		<input type="hidden" value="${index.id }" id="submit_id" name="id">
		<input type="hidden" value="${searchIndex.id }"   name="indexid">
		<table class="eitable">
			<tr class="eigname">
				<td>产品英文名字</td>
				<td>英文别名1</td>
				<td>英文别名2</td>
				<td>英文别名3</td>
				<td>我司翻译类目名</td>
			</tr>
			<tr>
				<td>
					<input type="text" name="en" id="eiputen" class="eiput" value="${index.keyWords }" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
				<td>
					<input type="text" name="enone" class="eiput" value="${searchIndex.enNameOne }" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
				<td>
					<input type="text" name="entwo" class="eiput" value="${searchIndex.enNameTwo }" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
				<td>
					<input type="text" name="enthree" class="eiput" value="${searchIndex.enNameThree }" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
				<td>
					<input type="text" name="trcatid" class="eiput" value="${searchIndex.translationCatid }" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
			</tr>
			<tr class="eigname">
				<td>中文名字1</td>
				<td>中文名字2</td>
				<td>中文名字3</td>
				<td>中文名字4</td>
				<td>1688类别ID(逗号分隔)</td>
			</tr>
			<tr>
				<td>
					<input type="text" name="cn" class="eiput" value="${searchIndex.cnName}" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
				<td>
					<input type="text" name="cnone" class="eiput" value="${searchIndex.cnNameOne  }" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
				<td>
					<input type="text" name="cntwo" class="eiput" value="${searchIndex.cnNameTwo }" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
				<td>
					<input type="text" name="cnthree" class="eiput" value="${searchIndex.cnNameThree }" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
				<td>
					<input type="text" name="decatid" class="eiput" value="${searchIndex.detailCatid }" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
			</tr>
		</table>
		<table class="eitable">
			<tr class="eigname">
				<td>屏蔽关键词(可以多个，逗号分隔)</td>
				<td>最小价格</td>
				<td>最大价格</td>
			</tr>
			<tr>
				<td>
					<input type="text" name="keywords" value="${searchIndex.keywords }" class="eiput" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
				<td>
					<input type="text" name="minprice" value="${searchIndex.minPrice }" class="eiput" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
				<td>
					<input type="text" name="maxprice" value="${searchIndex.maxPrice }" class="eiput" onfocus="$(this).css('border','1px solid #00afff')" onblur="$(this).css('border','1px solid #ddd')"/>
				</td>
			</tr>
		</table>
		<input type="submit" value="提交" class="eigbtn" style="display: block;"/>
	</form>
<div style="color: Red;">结果：<span id="strresults" class="strresults">${results}</span></div>
<div ><a href="/cbtconsole/SearchByPicController/select" >管理界面</a></div>
<br>
<div>
<span>说明:</span>
<br>
<span>(1)我司翻译类目说明：</span><span>输入类别id,类别id与类目对照如下</span>
<br>
<table>
<tr>
<td width="165px">1:服装&amp;鞋 </td>
<td width="165px">2:消费电子及配件</td>
<td width="165px">3:办公用品</td>
<td width="185px">4:家庭用品,家具&amp;园艺</td>
<td width="100px">5:首饰</td>
<td width="165px">6:手表</td>
<td width="165px">7:箱包</td>
<td width="165px">8:母婴用品</td>
</tr>
<tr>
<td width="165px">9:运动户外</td>
<td width="165px">10:美妆</td>
<td width="165px">11:汽配&amp;摩托</td>
<td width="185px">12:玩具</td>
<td width="100px">13:灯具</td>
<td width="165px">14:电工电子电器</td>
<td width="165px">15:宠物及园艺</td>
<td width="165px">16:礼品</td>
</tr>
<tr>
<td width="165px">17:五金工具</td>
<td width="165px">18:医药保养</td>
<td width="165px">19:日用百货</td>
<td width="185px">20:工业化工能源</td>
<td width="100px">21:其他</td>
<td width="165px">22:印刷工业</td>
<td width="165px">23:安全防护</td>
<td width="165px">24:交通运输</td>
</tr>

</table>

</div>
</div>
</body>
</html>