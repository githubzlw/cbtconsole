<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>Name Your Price</title>
<script type="text/javascript">
/* 	var check = function(){
		var uid = $("#userid").val();
		if(isNaN(uid)){
			$("#ts").html("请输入数字");
			return false;
		}else{return true;}
	}; */
	function send(userId){
		$.ajax({
			type:'POST',
			url:'/cbtconsole/customerServlet?action=customoreState&className=MoreActionServlet',
			data:{userId:userId,type:"3"},
			success:function(res){	
				if(res == "1"){
					alert("数据更新成功");
					location.reload();
				}
				
				
			}
		});
	}
	
	function fnSelect(value){
		document.getElementById("type").value=value;
	}
</script>
</head>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
<div align="center">
	<form action="/cbtconsole/customerServlet?action=findAllNameYourPrice&className=MoreActionServlet" onsubmit="" method="post">
		<table>
			<tr>
				<%-- <td>userid:<input type="text" value="${userId }" id="userid" name="userId"><font id="ts" color="red"></font></td>
				<td>username:<input type="text" value="${userName }" id="username" name="userName"></td>
				<td>useremail:<input type="text" id="useremail" value="${useremail}"name="useremail" ></td> --%>
				<td style="width: 120px;">state:
					<select id="similarity"  style="width:100px;" onchange="fnSelect(this.value)" >
										<option selected="selected" value="">all</option>
										<option value="1" >replied</option>
										<option value="0" >no reply</option>
										<%-- <c:choose>
											<c:when test="${type==1 }">
												<option value="1" selected="selected">replied</option>
											</c:when>
											<c:otherwise>
												<option value="1" >replied</option>
											</c:otherwise>
										</c:choose>
										
										<c:choose>
											<c:when test="${type==0 }">
												<option value="0" selected="selected">no reply</option>
											</c:when>
											<c:otherwise>
												<option value="0" >no reply</option>
											</c:otherwise>
										</c:choose> --%>
					</select>
					<input type="hidden" id="type" name ="type">
				</td>
							
				<td style="width: 150px;">useremail:<input type="text" id="useremail" value="${useremail}"name="useremail" ></td> 
				<td style="width: 120px;">开始日期：<input id="startdate" name="startdate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value="${startdate}" /></td>
					<td style="width: 120px;">结束日期：<input id="enddate" name="enddate"
						readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" value="${enddate}" /></td>
				<td><input type="submit" value="查询"></td>
			</tr>
		</table> 
	</form>
	</div>
	<div>
		<table id="table" align="center" border="1px" style="font-size: 13px;" width="800" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
<%-- 			<Tr>
				<th>序号</th>
				<th>user_id</th>
				<th>user_name</th>
				<th>email</th>
				<th>goods_img</th>
				<th>goods_price</th>
				<th>currency</th>
				<th>min_order_quantity</th>
				<th>goods_url</th>
				<th>quantity</th>
				<th>comment</th>
				<th>create_time</th>
			</Tr>
			<c:forEach items="${gbbs }" var="gbb" varStatus="i">
			<Tr>
				<td>${i.index+1 }</td>
				<td>${gbb.userId }</td>
				<td>${gbb.userName }</td>
				<td>${gbb.email }</td>
				<td> <a target='_blank' href="${gbb.purl }" ><img  width='50px' title="${gbb.pname }" height='50px;' src="${gbb.img }" style='cursor: pointer;' ></a></td>
				<td>${gbb.fprice }</td>
				<td>${gbb.currency }</td>
				<td>${gbb.minOrder }</td>
				<td><a href="/cbtconsole/processesServlet?action=getSpider&className=SpiderServlet&amp;url=${gbb.purl }" >${gbb.purl }</a></td>
				<td>${gbb.quantity }</td>
				<td style="word-break: break-all;">${gbb.comment }</td>
				<td>${gbb.createTime }</td>
			</Tr>
			</c:forEach> --%>
			
			<Tr>
				<th>序号</th>
				<th>user_id</th>
				<th>email_address</th>
				<th>date</th>
				<th>product</th>
				<th>What's your intended order quantity</th>
				<th>What's your target price</th>
				<th>Do you want exact the same product or have flexibility?</th>
				<th>comments</th>
				<th>state</th>
				<!-- <th>quantity</th>
				<th>comment</th>
				<th>create_time</th> -->
			</Tr>
			<c:forEach items="${gbbs }" var="gbb" varStatus="i">
			<Tr>
				<td>${i.index+1 }</td>
				<td>${gbb.userId }</td>
				<td>${gbb.email }</td>
				<td>${gbb.createTime }</td>
				<td><a href="/cbtconsole/processesServlet?action=getSpider&className=SpiderServlet&amp;url=${gbb.purl }" >${gbb.purl }</a></td>
				
				<td>${gbb.quantity }</td>
				<td>${gbb.targetPrice }</td>
				<td>${gbb.item }</td>
				<td>${gbb.comment }</td>
				
				
				<td>
				<c:if test="${gbb.state==0 }">
					no reply
				</c:if>
				<c:if test="${gbb.state==1 }">
					replied
				</c:if>
				</td>
				<td><input style="height:30px;width:100px;" type="button" value="action" onclick="send('${gbb.userId }')"></td>
			</Tr>
			</c:forEach>
		</table>
		</br>
		<c:if test="${ !empty gbbs}">
			<div align="center">${pager }</div>
		</c:if>
		
	</div>
</div>
</body>
</html>