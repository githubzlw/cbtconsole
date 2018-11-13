<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>邮费申请折扣</title>
</head>
<script type="text/javascript">
	var page = 1;
	function fn(val){
		 if(val==2){
			  page = page+1;
			  if(count<page){
				  return;
			  }
		  }else if(val == 3){
			  page = page-1;
			  if(0>=page){
				  page = 1;
				  return;
			  }
		  }
		var userid = $("#userid").val();
		if(location.search.indexOf('useremail')>0){
			document.getElementById("useremail").value=location.search.split('useremail=')[1];
		}
		if(userid == ""){
			userid=0;
		}
		$("#rows").html();
		$.post("/cbtconsole/WebsiteServlet",
  			{action:'getGoodpostage',className:'OrderwsServlet',userid:userid,page:page},
  			function(res){
  				var jsons = eval(res);
  				var json_length = jsons.length;
  				 $("#table tr:gt(0)").remove();
  				 var uid = 0;
  				 var j = 1;
  				 for (var i = 1; i < json_length; i++) {
  					 var json = jsons[i-1];
  				  $("#table tr:eq("+(i-1)+")").after("<tr></tr>");
  				  $("#table tr:eq("+i+")").append("<td>"+json[0]+"</td>");
  				  $("#table tr:eq("+i+") td:eq(0)").after("<td><a target='_blank' href="+json[4]+" ><img  width='50px' height='50px;' src="+json[5]+" style='cursor: pointer;' >"+json[11]+"</a></td>");
  				  $("#table tr:eq("+i+") td:eq(1)").after("<td>"+json[1]+"</td>");
  				  $("#table tr:eq("+i+") td:eq(2)").after("<td>"+json[2]+"</td>");
  				  $("#table tr:eq("+i+") td:eq(3)").after("<td>"+json[3]+"</td>");
  				  $("#table tr:eq("+i+") td:eq(4)").after("<td>"+json[6]+"</td>");
  				  $("#table tr:eq("+i+") td:eq(5)").after("<td>"+json[7]+"</td>");
  				  $("#table tr:eq("+i+") td:eq(6)").after("<td>"+(json[8]==1?'是':'否')+"</td>");
  				  $("#table tr:eq("+i+") td:eq(7)").after("<td>"+(json[9]==1?'是':'否')+"</td>");
  				  $("#table tr:eq("+i+") td:eq(8)").after("<td>"+json[10]+"</td>");
  				 }
  	  			$("#counto").html(jsons[json_length-1][0]);

  			  count = Math.ceil(jsons[json_length-1][0] / 40);
  			  $("#count").html(count);
  			  $("#counto").html(jsons[json_length-1][0]);
  			  $("#page").html(jsons[json_length-1][1]);
  			}
  			);
		fngetPds(val);
	}
	
	
	function fngetPds(val){
		 if(val==2){
			  page = page+1;
			  if(count<page){
				  return;
			  }
		  }else if(val == 3){
			  page = page-1;
			  if(0>=page){
				  page = 1;
				  return;
			  }
		  }
		var userid = $("#userid").val();
		var type = $("#type").val();
		if(userid == ""){
			userid=0;
		}
		var useremail=$('#useremail').val();
		$("#rows").html();
		$.post("/cbtconsole/WebsiteServlet",
 			{action:'getPds',className:'PreferentialwServlet',userid:userid,type:type,page:page,useremail:useremail},
 			function(res){
 				var jsons = eval(res);
 				var json_length = jsons.length+1;
 				 $("#table1 tr:gt(0)").remove();
 				 var uid = 0;
 				 var j = 1;
 				 for (var i = 1; i < json_length; i++) {
 					 var json = jsons[i-1];
 				  $("#table1 tr:eq("+(i-1)+")").after("<tr></tr>");
 				  $("#table1 tr:eq("+i+")").append("<td>"+(i+1)+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(0)").after("<td>"+json.userid+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(1)").after("<td>"+json.sessionid+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(2)").after("<td>"+json.name+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(3)").after("<td>"+json.email+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(4)").after("<td>"+json.phone+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(5)").after("<td>"+json.country+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(6)").after("<td>"+json.shopping_company+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(7)").after("<td>"+json.shopping_price+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(8)").after("<td>"+json.weight+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(9)").after("<td>"+(json.handlestate==0?'未处理':'已处理')+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(10)").after("<td>"+json.handleman+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(11)").after("<td>"+json.handletime+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(12)").after("<td>"+json.ip+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(13)").after("<td>"+(json.coi==0?'个人':'公司')+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(14)").after("<td>"+json.createtime+"</td>");
 				  var html = json.handlestate==0?"<button onclick='fnhandle(this,"+json.id+")'>已处理</button>处理人员:<input id='handleman'/>":"";
 				  $("#table1 tr:eq("+i+") td:eq(15)").after("<td>"+html+"</td>");
 				  $("#table1 tr:eq("+i+") td:eq(16)").after("<td><strong>"+json.admname+"</strong></td>");
 				 }
 				 var page = 1;
 				 var count = 0;
 				 if(json_length>1){
 					page = jsons[0].page;
 					count = jsons[0].count;
 				 }
 			 var countpage = Math.ceil(count / 40);
 			  $("#count1").html(countpage);
 			  $("#counto1").html(count);
 			  $("#page1").html(page);
 			}
 			);
	}
	
	function fnhandle(val,id){
		var handleman = $(val).next().val();
		if(handleman == ""){
			alert("请输入处理人员");
			return;
		}
		$.post("/cbtconsole/WebsiteServlet",
	 			{action:'upPostageD',className:'PreferentialwServlet',handleman:handleman,type:1,id:id},
	 			function(res){
	 				
	 				if(res){
	 					alert("保存成功");
	 				}else{
	 					alert("保存失败");
	 				}
	 			});
	}
	</script>
<body  onload="fn(1);">
<div align="center">
<div><a href="ordermgr.jsp">返回订单列表</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="preferential.jsp">批量优惠申请</a>&nbsp;&nbsp;<a href="goodpostage.jsp">邮费折扣申请</a>&nbsp;&nbsp;<a  href="busiesslist.jsp">Busiess 询盘</a></div>
<h3>邮费折扣申请</h3>
<div>
	userid:<input id="userid" name="userid" />
	useremail:<input id="useremail" name="useremail">
	是否处理:<select id="type">
			<option value="0" selected="selected">未处理</option>
			<option value="1">已处理</option>
			<option value="-1">全部</option>
			</select>
	&nbsp;&nbsp;<input type="button" value="查询" onclick="fn()">
</div>
<br>

<div>
	<table id="table1" border="1px" style="font-size: 13px;" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
		<tbody>
		
			<tr>
				<td>序号</td>
				<td>userid</td>
				<td>sessionid</td>
				<td>name</td>
				<td>email</td>
				<td>phone</td>
				<td>country</td>
				<td>运输方式</td>
				<td>运输价格</td>
				<td>重量</td>
				<td>是否处理</td>
				<td>处理人员</td>
				<td>处理时间</td>
				<td>ip</td>
				<td>公司/个人</td>
				<td>创建时间</td>
				<td>操作</td>
				<td>负责人</td>
			</tr>
			 
		</tbody>
	</table>
</div>
<div class="pages" id="pages1">
<span>&nbsp;&nbsp;总条数：<font id="counto1"></font>&nbsp;&nbsp;总页数：<font id="count1"></font></span>&nbsp;&nbsp;当前页<font id="page1"></font><button onclick="fngetPds(3)">上一页</button>&nbsp;<button  onclick="fngetPds(2)">下一页</button></div>

	<table id="table" border="1px" style="font-size: 13px;" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
		<tbody>
		
			<tr>
				<td>商品ID</td>
				<td>商品信息</td>
				<td>用户ID</td>
				<td>用户名称</td>
				<td>用户注册email</td>
				<td>价格</td>
				<td>数量</td>
				<td>是否免邮</td>
				<td>是否优惠申请</td>
				<td>申请邮费email</td>
			</tr>
			 
		</tbody>
	</table>
</div>
<div class="pages" id="pages">
<span>&nbsp;&nbsp;总条数：<font id="counto"></font>&nbsp;&nbsp;总页数：<font id="count"></font></span>&nbsp;&nbsp;当前页<font id="page"></font><button onclick="fn(3)">上一页</button>&nbsp;<button  onclick="fn(2)">下一页</button></div>
</body>
</html>