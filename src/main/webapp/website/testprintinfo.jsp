<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/table.css">
<title>Insert title here</title>
<style type="text/css">
.text1{border:0;border-bottom:1 solid black;background:;}
</style>         
<script type="text/javascript">
function test(){            
	window.print();
}         

/*
var textWidth = function(text){ 
	text = text+text;
    var sensor = $('<pre>'+ text +'</pre>').css({display: 'none'}); 
    $('body').append(sensor); 
    var width = sensor.width();
    sensor.remove(); 
    return width;          
};           
*/

var textWidth = function (text) {
	var font ="normal 13px Helvetica, Arial, sans-serif";
    var currentObj = $('<span>').hide().appendTo(document.body);
    $(currentObj).html(text).css('font', font);
    var width = currentObj.width();
    currentObj.remove();
    return width;
}
</script>                           

</head>                     
<body>
<div id="jcexprintinfoid" class="alldiv">
	<!-- background:url(../img/jcex.png)  JCEX.JPG-->
	<div style="height : 682px;width : 723px; ">
		<table  border="1px" width="723px">         
			<tr >                     
				<td width="450px"><img onclick="test()" alt="" width="172px" height="51px" src="../img/JCEX.JPG">
				&nbsp;&nbsp;总件数             
				           
				</td>                                                             
				<td></td>                          
			</tr>                                          
			<tr>
				<td colspan="2">From:</td>
			</tr>
			<tr>
				<td height="100px">
				发件人信息  <br/>
				发件人姓名:  <input name="test" class="text1"   type="text" value="${uod.userid }"/> <br/>
				公司名:&nbsp;&nbsp;   <input class="text1"   type="text" value="${uod.orderno }"/> <br/>
				电话: &nbsp;&nbsp;&nbsp;<input class="text1"   type="text" value=""/> <br/>
			<!-- 	 联系人&nbsp;&nbsp; <input class="text1" type="text"   value=""/> <br/> -->
				地址: &nbsp;&nbsp;&nbsp;<input class="text1"   type="text" value=""/> <br/>
				</td>
				<td><img src="/cbtconsole" /></td>
			</tr>
                       
			<tr>
				<td colspan="2">To:</td>
			</tr>
			<tr>
				<td height="80px">
					收件人信息&nbsp;<br/>
					收件人公司名:<input class="text1"   type="text" value="${uod.orderno }"/><br/>
					收件人地址:&nbsp;<input class="text1"   type="text" value="${uod.userstreet }${uod.address }"/><br/>
					收件人姓名:&nbsp;<input class="text1"   type="text" value="${uod.userName }"/>
				</td>
				<td>
				<br/>
				</td>
			</tr>            
			<tr>             
				<td height="100px">                                 
					国家:	<input class="text1"   type="text" value="${uod.zone }"/>		城市:<input class="text1" type="text"   value="${uod.address2 }"/><br/>
					电话:	<input class="text1"   type="text" value="${uod.phone }"/>		州名:<input class="text1" type="text"   value="${uod.statename }"/><br/>
					邮编:	<input class="text1" type="text"   value="${uod.usercode }"/>			<br/>
					重量:	<input class="text1" type="text"   value="${uod.weight }"/>			<br/>
						
				</td>
				<td>
					
					<c:forEach items="${uod.productBean}" var="obj"> <!-- ${uod.productBean[0].productnum } -->
					申报价值:	<input name="test" class="text1" type="text"   value="${obj.productprice }"/> 
					<input name="test" class="text1" type="text"   value="${obj.productcurreny }"/>		<br/>
					申报品名:<input class="text1" name="test" width="10px"  type="text" value="${obj.productname }"/> <br/>
					PCS	 C(cm)	K(cm)	G(cm)<br/>
						数量	<input class="text1" name="test" width="10px"  type="text" value="${obj.productnum }"/>
						长<input class="text1" name="test" width="10px"  type="text" value="${pc }"/>
						宽<input class="text1" name="test" width="10px"  type="text" value="${pk }"/>
						高<input class="text1" name="test" width="10px"  type="text" value="${pg }"/><br/>
					</c:forEach>
					       
					
				</td>          
			</tr>
			<tr>
				<td>    
				I/We agree that JCEX'Terms & Conditions of <br/>
				Carriage are the terms of the contract between me/us and <br/>
				JCEX.I/We guarantee that the shipment does not <br/>
				contain any dangerous and prohibited goods. <br/>
				</td>
				<td>
					Your Signature                  <br/>               
					Received by Express <br/>
				</td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>           
<script type="text/javascript">

//input宽度自适应
//$("input").unbind('keydown').bind('keydown', function(){
	
//    $(this).width(textWidth($(this).val())+2);
//});
$("input[name='test']").each(function(){
	//alert(this.name)
	if(textWidth($(this).val())+2 < 5){
		$(this).width(10);
	}else{
		$(this).width(textWidth($(this).val()));
	}
	 
	});
//input宽度自适应
$("input[name='test']").unbind('keydown').bind('keydown', function(){
	
  $(this).width(textWidth($(this).val())+10);
});

</script> 

<%
request.getSession().removeAttribute("uod");
request.getSession().removeAttribute("pc");
request.getSession().removeAttribute("pk");
request.getSession().removeAttribute("pg");
request.getSession().removeAttribute("number");


%>