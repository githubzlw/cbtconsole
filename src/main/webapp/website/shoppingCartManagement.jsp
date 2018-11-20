<%@page import="com.cbt.website.userAuth.bean.Admuser"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@page import="com.cbt.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link type="text/css" rel="stylesheet" href="/cbtconsole/css/houtai.css"/>
<title>购物车营销</title>
<%
String s = request.getParameter("status");
if(s!=null &&! "".equals(s)){
	request.setAttribute("status", s);
}else{
	request.setAttribute("status", "1");
}

%>
<script type="text/javascript">
var userstr='';
$(function(){
	$.post("/cbtconsole/ConfirmUserServlet",
			{action:"sell"},
			function(res){
				var json=eval(res);
				userstr+='<option value="0">请选择</option>';
				for (var i=0;i<json.length;i++){
					if(json[i].role=='0' || json[i].role=='3' || json[i].role=='4'){
						userstr+='<option value="'+json[i].id+'" flag="'+json[i].confirmusername+'"">'+json[i].confirmusername+'</option>';
					}
				}
			});
})

function addUser(id){ 
	var adminid=$("#"+id+"user").find("option:selected").val();
	var admname = $("#"+id+"user").find("option:selected").text();
	var userEmail = $("#"+id+"email").text();
	if (adminid==0) {
		alert("请选择正确的处理人员");
		return;
	}

	$.post("/cbtconsole/WebsiteServlet",
			{"action":"addUserInCharge","className":"OrderwsServlet",adminid:adminid,userid:id,admName:admname,useremail:userEmail,username:userEmail},
			function(res){
				if(res==1){
					/* $("#"+id+"td").html(admname); */
					$("#"+id+"user").val(adminid);
					$("#"+id+"msg").css("display","block");
				}
				window.setTimeout(function(){ 
					$("#"+id+"msg").css("display","none");
					},3000); 
			});
}



function reset(orderid){
	$("#dayvalue"+orderid).removeAttr("disabled");
}


function purchasedays(orderid){
	$("#dayvalue"+orderid).attr("disabled","disabled");
	var dayvalue=$("#dayvalue"+orderid).val();
	$.post("/cbtconsole/WebsiteServlet?action=updatePurchaseDays&className=DeliveryWarningServlet",
			{orderid:orderid,dayvalue:dayvalue},
			function(res){
				if(res>0){
					alert('提交成功');
				}
			});
}

var page = 1;
var count = 1;
var isorder;
function fnquery(va){
	var options = document.getElementById("user");
	var isorders = $('#isorders').prop('checked')==true?1:0;
	if (isorders==null) {
		isorders = 0;
	}
	isorder = isorders;
	var admid = null;
	var userid = null;
	if(options){
		admid=$("#user").val();
	}
	userid=$("#userid").val();
	if (userid!="") {
		page = 1;
	}
	if (va == 1) {
		page = 1;
	} else if (va == 2) {
		page = page + 1;
		if (count < page) {
			return;
		}
	} else if (va == 3) {
		page = page - 1;
		if (0 >= page) {
			page = 1;
			return;
		}
	} else if (va == 4) {
		page =  $("#forwordPage").val();
	}
	var status =  ${status};
	$.post("/cbtconsole/shopCarMarketingCtr/queryShopCarList",
			{admid:admid,userid:userid,currenPage:page,isorder:isorder,status:status},
			function(res){
				$("#load").css("display","none");
				var result = document.getElementById("result");
				if(result.getElementsByTagName("tr").length>1){
				  result.innerHTML="<tr>"+result.getElementsByTagName("tr")[0].innerHTML+"</tr>";
				}
				var json=eval(res);

				for(var i=0;i<json.length;i++){
					$("#result tr:eq("+i+")").after('<tr height="40"></tr>');
// 					$("#result tr:eq("+(i+1)+")").append("<td style='width:250px'><a href='javascript:show("+ json[i][0] + ")'>"+json[i][0]+"</a></td>");/cbtconsole/website/shoppingCarProductPush.jsp?userid=
 					$("#result tr:eq("+(i+1)+")").append("<td style='width:250px'><a href='/cbtconsole/shopCarMarketingCtr/sendEmailCarInfoByUserId?userId="+ json[i][0] +"' target='_blank'>"+json[i][0]+"</a></td>");
					$("#result tr:eq("+(i+1)+")").append('<td id="'+json[i][0]+'email"><a href="/cbtconsole/userinfo/getUserInfo.do?userId='+json[i][0]+'" target="_blank">'+  json[i][1]+'</a></td>');
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][2]+'</td>');
					if (parseFloat(json[i][3]) > 150) {
						$("#result tr:eq("+(i+1)+")").append('<td style="color: red; font-weight: bold;">'+json[i][3]+'</td>');
					}else{
						$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][3]+'</td>');
					}
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][4]+'</td>');
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][5]+'</td>');
					
					if((json[i][10]+'')=='null' || json[i][10] == undefined){
						$("#result tr:eq("+(i+1)+")").append('<td>无</td>');
					}else{
						$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][10]+'</td>');
					}

					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][6]+'</td>');
					
					
					if((json[i][10]+'')=='null' || json[i][10] == undefined){
						$("#result tr:eq("+(i+1)+")").append('<td>无</td>');
					}else{
						$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][10]+'</td>');
					}
					if((json[i][9]+'')=='null' || json[i][9] == undefined){
						$("#result tr:eq("+(i+1)+")").append('<td id="'+json[i][0]+'td"><select id="'+json[i][0]+'user">'+userstr+'</select><input type="button" value="确认" onclick="addUser('+json[i][0]+')"><p id="'+json[i][0]+'msg" style="display: none;color:red">更新成功</p></td>');
					}else{
						$("#result tr:eq("+(i+1)+")").append('<td id="'+json[i][0]+'td"><select id="'+json[i][0]+'user">'+userstr+'</select><input type="button" value="确认" onclick="addUser('+json[i][0]+')"><p id="'+json[i][0]+'msg" style="display: none;color:red">更新成功</p></td>');
						$("#"+json[i][0]+"user option[flag='" + json[i][9] + "']").attr('selected', 'selected');
					}
					if((json[i][5]+'')=='null' || json[i][5] == undefined){
						$("#result tr:eq("+(i+1)+")").append('<td>无</td>');
					}else{
						$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][5]+'</td>');
					}

					$("#result tr:eq("+(i+1)+")").append("<td><input type='button' value='操作' onclick='show("+ json[i][0] + ")'></td>");
					/* if((json[i][4]+'')=='null'){
						$("#result tr:eq("+(i+1)+")").append('<td>无</td>');
					}else{
						$("#result tr:eq("+(i+1)+")").append('<td><img src="'+json[i][4]+'" width="80px" height="80px"></td>');
					}
					
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][10]+'/'+json[i][5]+'</td>');
					if((json[i][6]+'')=='null'){
						json[i][6]='无';
					}
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][6]+'</td>');
					if((json[i][14]+'')=='null'){
						json[i][14]='无';
					}
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][14]+'</td>');
					if((json[i][7]+'')=='null'){
						json[i][7]='无';
					} */
				}
				if (json.length>0) {					
					count = json[0][7];
					if (json[0][7]!="") {					
						$("#counto").html(json[0][7]);
						count = Math.floor((json[0][7]+40)/40);
						$("#counnum").html(count);
						$("#currPage").html(page);
					}
				}else{
					$("#counto").html(0);
					$("#counnum").html(0);
					$("#currPage").html(1);
				}
		});
}


function resetIn() {
	$("#userid").val("");
}

function show(userId) {
	var iWidth=1680; //弹出窗口的宽度;
	var iHeight=880; //弹出窗口的高度;
	var iTop = (window.screen.availHeight-30-iHeight)/2; //获得窗口的垂直位置;
	var iLeft = (window.screen.availWidth-10-iWidth)/2; //获得窗口的水平位置;
	var param = "height="+iHeight+",width="+iWidth+",top="+iTop+",left="+iLeft+",toolbar=no,menubar=no,scrollbars=yes, resizable=yes,location=no, status=no";
	//var url = '/cbtconsole/website/shoppingCarProductPush.jsp?userid=' +userId;
    //var url = '/cbtconsole/shopCarMarketingCtr/queryCarInfoByUserId?userId=' +userId;
	var url = '/cbtconsole/shopCarMarketingCtr/queryShoppingCarByUserId?userId=' +userId;
	//window.open(url,'windows',param);
	window.open(url);
}

</script>
</head>
<body>
    <div align="center" class="jiaodiv">
    	<div><span class="jiaoti">购物车营销</span></div><span style="color:red" id="load">正在加载.....请稍候</span>
    	<br/>
    	<br/>
    	<div><input type="checkbox" id="isorders" name="isorders">筛选未下单用户&nbsp;&nbsp;用户ID:<input type="text" id="userid" style="width:100px;height:30px;" onkeyup="this.value=this.value.replace(/\D/g,'')" onkeydown='if(event.keyCode==13){fnquery(1)}'/>&nbsp;&nbsp;<input style="width:50px;height:30px;" type="submit" value="查询" onclick="fnquery(1)"/>&nbsp;<input style="width:50px;height:30px;" type="reset" value="重置" onclick="resetIn()"/></div>
    	<table id="result" border="1" style="margin-top:20px;width:1600px;text-align: center">
    		<tr class="jiaotrti" style="background-color: gainsboro;">
    			<td style="width:70px">用户id</td>
    			<td style="width:200px">用户email</td>
    			<td style="width:80px">商品种类</td>
    			<td style="width:80px">总金额</td>
     			<td style="width:80px">均价</td>
     			<td style="width:120px">最后添加时间</td>
     			<td style="width:120px">最后访问时间</td>
    			<td style="width:80px">用户货币</td>
    			<td style="width:100px">默认国家</td>
    			<td style="width:160px">跟进人员</td>
    			<td style="width:120px">跟进时间</td>
    			<td style="width:80px">操作</td>
    		</tr>
    	</table>
    </div> 
    <div style="width:auto; height:150px; text-align: center;line-height: 150px">
    	<span>&nbsp;&nbsp;总条数：<font id="counto"></font>&nbsp;&nbsp;总页数：<font id="counnum"></font>&nbsp;&nbsp;当前页：<font id="currPage"></font></span>
    	 <button onclick='fnquery(3)'>上一页</button>&nbsp;<button onclick='fnquery(2)'>下一页</button>&nbsp;&nbsp;
    	 <button onclick='fnquery(4)'>跳转至</button>第<input type="text" id="forwordPage" style="width:40px" onkeydown='if(event.keyCode==13){fnquery(4)}'>页
    </div>
    
    <script type="text/javascript">fnquery(1);</script>
    
</body>
</html>