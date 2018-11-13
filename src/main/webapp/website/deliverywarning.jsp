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
<title>交期预警页面</title>
<script type="text/javascript">
function fnsale(){
	var str;
	$.post("/cbtconsole/ConfirmUserServlet",
			{action:'currentUser'},
			function(res){
				var json=eval(res);
				if (json.length >1 ) {
					var str = '<option value="0">全部</option>';
				}
				for (var i=0;i<json.length;i++){
					if(json[i].role=='0'||json[i].role=='1'){
					str+='<option value="'+json[i].id+'" flag="'+json[i].confirmusername+'"">'+json[i].confirmusername+'</option>';
					}
				}
				document.getElementById("user").innerHTML=str;
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

function fnquery(){
	var options = document.getElementById("user");
	var admid = null;
	var userid = null;
	if(options){
		admid=$("#user").val();
	}
	userid=$("#userid").val();
	$.post("/cbtconsole/WebsiteServlet?action=DeliveryWarningList&className=DeliveryWarningServlet",
			{admid:admid,userid:userid},
			function(res){
				$("#load").css("display","none");
				var result = document.getElementById("result");
				if(result.getElementsByTagName("tr").length>1){
				  result.innerHTML="<tr>"+result.getElementsByTagName("tr")[0].innerHTML+"</tr>";
				}
				var json=eval(res);
				for(var i=0;i<json.length;i++){
					$("#result tr:eq("+i+")").after('<tr></tr>');
					$("#result tr:eq("+(i+1)+")").append("<td style='width:250px'><a target='_blank' href='/cbtconsole/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo="+json[i][0]+"&state="+json[i][8]+"&username="+json[i][3]+"'>"+json[i][0]+"</a></td>");
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][13]+'</td>');
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][1]+'</td>');
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][12]+'</td>');
					//$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][2]+'</td>');
					//$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][3]+'</td>');
					if((json[i][4]+'')=='null'){
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
					}
					$("#result tr:eq("+(i+1)+")").append('<td style="width:150px;word-wrap:break-word;word-break:break-all">'+json[i][7]+'</td>');
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][9]+'</td>');
					$("#result tr:eq("+(i+1)+")").append('<td><select id="dayvalue'+json[i][0]+'"><option value=1>1 天</option><option value=2>2 天</option><option value=3>一周以内</option><option value=4>n 天</option></select><input type="button" value="提交" onclick="purchasedays(\''+json[i][0]+'\')"><input type="button" value="重置" onclick="reset(\''+json[i][0]+'\')"></td>');
					if(json[i][11]!=0){
						$("#dayvalue"+json[i][0]).val(json[i][11]);
						$("#dayvalue"+json[i][0]).attr("disabled","disabled");
					}
				}
				$("#counto").html(json.length);
		});
}


function resetIn() {
	$("#userid").val("");
	$("#user").val(0);
}

/* $(function(){
	$.post("/cbtconsole/WebsiteServlet?action=DeliveryWarningList&className=DeliveryWarningServlet",
			{},
			function(res){
				$("#load").css("display","none");
				var json=eval(res);
				for(var i=0;i<json.length;i++){
					$("#result tr:eq("+i+")").after('<tr></tr>');
					$("#result tr:eq("+(i+1)+")").append("<td><a target='_blank' href='/cbtconsole/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo="+json[i][0]+"&state="+json[i][8]+"&username="+json[i][3]+"'>"+json[i][0]+"</a></td>");
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][1]+'</td>');
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][13]+'</td>');
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][12]+'</td>');
					//$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][2]+'</td>');
					//$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][3]+'</td>');
					if((json[i][4]+'')=='null'){
						$("#result tr:eq("+(i+1)+")").append('<td>无</td>');
					}else{
						$("#result tr:eq("+(i+1)+")").append('<td><img src="'+json[i][4]+'" width="80px" height="80px"></td>');
					}
					
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][10]+'/'+json[i][5]+'</td>');
					if((json[i][6]+'')=='null'){
						json[i][6]='无';
					}
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][6]+'</td>');
					if((json[i][7]+'')=='null'){
						json[i][7]='无';
					}
					$("#result tr:eq("+(i+1)+")").append('<td style="width:150px;word-wrap:break-word;word-break:break-all">'+json[i][7]+'</td>');
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i][9]+'</td>');
					$("#result tr:eq("+(i+1)+")").append('<td><select id="dayvalue'+json[i][0]+'"><option value=1>1 天</option><option value=2>2 天</option><option value=3>一周以内</option><option value=4>n 天</option></select><input type="button" value="提交" onclick="purchasedays(\''+json[i][0]+'\')"><input type="button" value="重置" onclick="reset(\''+json[i][0]+'\')"></td>');
					if(json[i][11]!=0){
						$("#dayvalue"+json[i][0]).val(json[i][11]);
						$("#dayvalue"+json[i][0]).attr("disabled","disabled");
					}
				}
		});
}) */

</script>
</head>
<body>
    <div align="center" class="jiaodiv">
    	<div><span class="jiaoti">交期预警订单</span><span class="jiaotfu">(订单所有货源没有完全确认且超过下单时间48小时后进行预警)</span></div><span style="color:red" id="load">正在加载.....请稍候</span>
    	<br/>
    	<br/>
    	<div>用户ID:<input type="text" id="userid" style="width:100px;height:30px;" onkeyup="this.value=this.value.replace(/\D/g,'')" onkeydown='if(event.keyCode==13){fnquery()}'/>&nbsp;跟进销售人员：<select id="user" style="width:150px;height:30px;"></select><input style="width50px;height:30px;" type="submit" value="查询" onclick="fnquery()"/><input style="width:50px;height:30px;" type="reset" value="重置" onclick="resetIn()"/></div>
    	<table id="result" border="1" style="margin-top:20px;width:1600px;text-align: center">
    		<tr class="jiaotrti">
    			<td style="width:200px">订单号</td>
    			<td style="width:80px">用户id</td>
    			<td style="width:120px">支付时间</td>
    			<td style="width:120px">交期时间</td>
<!--     			<td>客户邮箱</td> -->
<!--     			<td>客户名</td> -->
    			<td>订单中 第一个产品</td>
    			<td style="width:100px">采购数/总数</td>
    			<td style="width:80px">指定销售</td>
    			<td style="width:80px">指定采购</td>
    			<td>订单comment</td>
    			<td>订单金额</td>
    			<td>采购天数</td>
    		</tr>
    	</table>
    </div> 
    <div style="width:auto; height:150px; text-align: center;line-height: 150px">
    	<span>&nbsp;&nbsp;总条数：<font id="counto"></font></span>
    </div>
    <script type="text/javascript">fnsale();</script>
    <script type="text/javascript">fnquery();</script>
    
</body>
</html>