<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>客户信息综合查询页面</title>
<script type="text/javascript">
var str_personCharge='';
$(function(){
	$.post("/cbtconsole/ConfirmUserServlet",
			{},
			function(res){
				var json=eval(res);
				for (var i=0;i<json.length;i++){
					if(json[i].role=='0'||json[i].role=='1'){
					str_personCharge+='<option value='+json[i].id+'  flag="'+json[i].id+'">'+json[i].confirmusername+'</option>';
					$('#confirmuser').append('<option value='+json[i].id+' >'+json[i].confirmusername+'</option>');
				}
				}
			});
	search(1);
})
var page=1;
function confirm(value){
	var confirminfo=document.getElementById(value).value;
	$.post("/cbtconsole/UpdateConfirmUserServlet",
			{confirminfo:confirminfo,id:value},
			function(res){
				if(res==1){
					document.getElementById(value).disabled=true;
					document.getElementById("button"+value).disabled=true;
				}
			});
}
 
function fnpagePrevious(){
	if(page>1){
		page=page-1;	
	}
	search(page);
}
function fnpageNext(){
	page=page+1;
	if(page > page_count){
		alert("已到最后一页");
		return;
	}
	search(page);
}
function fninputNext(){
	var jump = $("#jump").val();
	if(jump == ""){
		alert("请输入页数");
		return;
	}
	if(parseInt(jump) > page_count){
		alert("页数大于总页数");
		return;
	}
	search(page);
}
var page_count = 0;
function search(value){
	page=value;
	var nextdate='';
	var userid=$('#userid').val();
	var username=$('#username').val();
	var useremail=$('#useremail').val();
	var datetype=$('input[name="datetype"]:checked').val();
	var confirmuserid=$('#confirmuser').val();
	var	previousdate=$('#previousdate').val();
	var	nextdate=$('#nextdate').val();
	if(nextdate != "" && previousdate == ""){
		alert("请输入开始时间");
		return;
	}
	$.post("/cbtconsole/ApplicationSummaryServlet",
			{previousdate:previousdate,nextdate:nextdate,page:page,userid:userid,username:username,useremail:useremail,confirmuserid:confirmuserid},
			function(res){
			$("#result tbody").html("");
			var json=eval(res.split('+')[0]);
			var size=parseInt(res.split('+')[1]);
			$("#result").append('<tr><td>用户id</td><td>用户名</td><td>用户邮箱</td><td>下单数</td><td>留言数量</td><td>批量优惠数量</td><td>邮费折扣数量</td><td>business询盘</td><td>日期</td><td>跟进人员</td><td>确认人员</td></tr>');
			for(var i=0;i<json.length;i++){
				$("#result").append('<tr></tr>');
				$("#result tr:eq("+(i+1)+")").append('<td>'+json[i].userid+'</td>');
				$("#result tr:eq("+(i+1)+")").append('<td>'+(json[i].username==''?'无':json[i].username)+'</td>');
				$("#result tr:eq("+(i+1)+")").append('<td>'+json[i].useremail+'</td>');
				$("#result tr:eq("+(i+1)+")").append('<td>'+json[i].count+'</td>');
				if(json[i].leave_message!=0){
					$("#result tr:eq("+(i+1)+")").append('<td><a target="_blank" href="/cbtconsole/customerServlet?action=findAll&className=GuestBookServlet&useremail='+json[i].useremail+'">'+json[i].leave_message+'</a></td>');
				}else{
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i].leave_message+'</td>');
				}
				
				if(json[i].batch_application!=0){
					$("#result tr:eq("+(i+1)+")").append('<td><a target="_blank"  href="/cbtconsole/website/preferential.jsp?useremail='+json[i].useremail+'">'+json[i].batch_application+'</a></td>');
				}else{
				   $("#result tr:eq("+(i+1)+")").append('<td>'+json[i].batch_application+'</td>');
				}
				
				if(json[i].postage_discount!=0){
					$("#result tr:eq("+(i+1)+")").append('<td><a href="/cbtconsole/website/goodpostage.jsp?useremail='+json[i].useremail+'">'+json[i].postage_discount+'</a></td>');
				}else{
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i].postage_discount+'</td>');
				}
				
				if(json[i].business_inquiries!=0){
				 $("#result tr:eq("+(i+1)+")").append('<td><a target="_blank" href="/cbtconsole/website/busiesslist.jsp?useremail='+json[i].useremail+'">'+json[i].business_inquiries+'</a></td>');
				}else{
				  $("#result tr:eq("+(i+1)+")").append('<td>'+json[i].business_inquiries+'</td>');
				}
				$("#result tr:eq("+(i+1)+")").append('<td>'+json[i].AppDate+'</td>');
				var user = json[i];
					$("#result tr:eq("+(i+1)+")").append('<td><select id="user'+i+'" name="user'+json[i].userid+'" onchange="gradeChange('
							+ i+","
							+ json[i].userid+',\'' + json[i].useremail + '\',\'' + json[i].username + '\')" ><option>'+str_personCharge+'</option></select><span style="color:red"></span></td>');
				if(json[i].confirmUser=='无'){
					$("#result tr:eq("+(i+1)+")").append('<td><input type="text" id="'+json[i].id+'"><input id="button'+json[i].id+'"type="button" value="确认" onclick="confirm(\''+json[i].id+'\')"></td>');
				}
				else{
					$("#result tr:eq("+(i+1)+")").append('<td>'+json[i].confirmUser+'</td>');
				}
				if (user.adminname != '') {
					$("#user" +i+ " option[flag='" + user.adminId + "']").attr('selected', 'selected');
					admName = user.adminname;
				}
			}
			$("#counto").html(size);
			page_count = Math.ceil(parseInt(size) / 20);
			$("#count").html(page_count);
			$("#page").html(page);
	});
}
//改变销售负责人
function gradeChange(idex,uid,email,username) {
	var vel=$("#user"+idex);
	var stradmid_=vel.find("option:selected").val();
	var admName = vel.find("option:selected").text();
	if(stradmid_ == ""){
		alert("请选择更进人选");
		return;
	}
	$.post("/cbtconsole/UpdateAdminUserServlet", {
		adminid : stradmid_,
		userid : uid,
		email:email,
		userName: username,
		admName: admName
	}, function(res) {
		if (res == 1) {
			$(".select" + uid +" ").val(stradmid_);
		    vel.next().html("更新成功");
		    var tr_rows = $("#result tr").length;
		    for (var i = 1; i < tr_rows; i++) {
		    	var tr_email = $("#result tr").eq(i).find("td:eq(2)").html();
		    	var tr_id = $("#result tr").eq(i).find("td:eq(0)").html();
		    	if(email == tr_email && tr_email != ""){
		    		$("#result tr").eq(i).find("select option[flag='" + stradmid_ + "']").attr('selected', 'selected');
		    	}
			}
		}
		else
			{
		    vel.next().html("更新失败");
			}
		window.setTimeout(function(){ 
			 vel.next().html("");
			},3000); 
	});
}
</script>
</head>
<body>
<div align="center">
	<div style="margin-top:20px">客户信息综合查询</div>
    <div style="margin-top:50px">
<!--     	<select id="type"> -->
<!--     		<option value=1 selected="selected">用户留言</option> -->
<!--     		<option value=2>批量优惠申请</option> -->
<!--     		<option value=3>邮费申请折扣</option> -->
<!--     		<option value=4>business询盘</option> -->
<!--     	</select> -->
    	用户id:<input type="text" id="userid" name="userid">
    	用户名:<input type="text" id="username" name="username">
    	用户邮箱:<input type="text" id="useremail" name="useremail">
    	跟进人员：<select id="confirmuser"><option value=0></option></select>
<!--     	查询日期类型：<select id="datetype"><option value=0></option><option value=1>具体日期</option><option value=2>日期区间</option></select> -->
    	<span id="previousdatetext" >开始日期：</span><input id="previousdate" name="previousdate" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})"/>
    	<span id="nextdatetext">结束日期：</span><input id="nextdate" name="nextdate" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" />
    	<input type="button" id="search" onclick="search(1)" value="查询">
    </div>
    <div>
    	<table id="result" border="1" cellpadding="3" cellspacing="0" style="margin-top:20px;width:1200px;text-align: center">
    	</table>
    </div>
    <br />
		<div class="pages" id="pages">

			<span>&nbsp;&nbsp;总条数：<font id="counto"></font>&nbsp;&nbsp;总页数：<font
				id="count"></font></span>&nbsp;&nbsp;当前页:<font id="page"></font>&nbsp;&nbsp;
		<button onclick="fnpagePrevious()">上一页</button>&nbsp;<button  onclick="fnpageNext()">下一页</button>
			&nbsp;<input id="jump" type="text">
			<button onclick="fninputNext()">转至</button>
		</div>
</div>
</body>
</html>