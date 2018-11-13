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
<title>留言问题查询</title>

<script type="text/javascript">
$(document).ready(function(){
	  $(".contact_diaj").click(function(){
	    $(".contact_neir").show();
	  });
$(document).ready(function(){
	  $(".contact_diaj").click(function(){
	    $(".contact_nei").show();
	  });

</script>

<script type="text/javascript">


	//页面按下enter按钮触发跳转事件
	$(function(){
		$(document).keyup(function(event) {
			if (event.keyCode == 13) {
				var page = $("#page").val();
				if(page != ""){
					jumpPage();
				}
			}
		});
	});
	//回复留言问题
	var reply=function(id,adminid){
		window.open("/cbtconsole/helpServlet?action=reply&className=EmailReceiveServlet&id="+id+"&adminid="+adminid,"windows","height=500,width=600,top=200,left=200,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no");
	};
	/* //回复留言问题
	var replyrecord=function(id){
		window.location = "/cbtconsole/customerServlet?action=replyrecord&className=EmailReceiveServlet&id="+id+"";
	}; */
	/* var delreply=function(id){
		window.location = "/cbtconsole/helpServlet?action=delreply&className=EmailReceiveServlet&id="+id+"";
	}; */
	var check = function(){
		var uid = $("#userid").val();
		if(isNaN(uid)){
			$("#ts").html("请输入数字");
			return false;
		}else{return true;}
	};
</script>
</head>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="javascript:history.back()" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&laquo;</em>&nbsp;Back</span></div>	<br>

	<div>
		<table id="table"  width="100%" align="center" border="1px" style="font-size: 13px;" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<Tr>

				<th width="5%">序号</th>
				<th width="5%">id</th>
				<!-- <th width="5%">留言号</th> -->
				<th width="10%">发件人</th>
                <th width="20%">原内容</th>
				<th width="10%">创建时间</th>
                <th width="20%">回复内容</th>
				<th width="10%">回复时间</th>
				<th width="20%">操作</th>
			
			</Tr>
			<c:forEach items="${cuslist}" var="cus" varStatus="i">
			<Tr>
			    <td>${i.index+1 }</td>
				<td>${cus.questionid }</td>
				
				<td>${cus.send }<c:if test="${cus.include==1 }"><br/><a href="/cbtconsole/download?filename=${cus.originalmessage }" title="${cus.originalmessage }">下载${cus.originalmessage }</a></c:if></td>
				<td>
				<div>
				<div  style="width:100%;height:120px;overflow: hidden;display: block;white-space: nowrap;text-overflow: ellipsis;">${cus.content }</div>
				<a href="javascript:void(0)" onclick="document.getElementById('contact_neir${cus.id }').style.display='block'">显示全文</a>
				<div id="contact_neir${cus.id }" style="padding:10px;display:none;width:50%;height:700px;background:#e2e2e2;position:fixed;left:50%;top:50%;transform:translate(-50%,-50%);-webkit-transform:translate(-50%,-50%); ">
					<a href="javascript:void(0)" onclick="document.getElementById('contact_neir${cus.id }').style.display='none';" style="float: right ;margin: 10px;">
	                Close</a>
					<div style="overflow: auto;width: 100%;height: 100%">
		                <p class="aaaa">${cus.content}</p>
		            </div>
                </div>
                </div>
        
				</td>
				<td>${cus.sendDate }</td>
		        <td>
				<c:if test="${cus.recontent !=null }"><div  style="width:100%;height:120px;overflow: hidden;display: block;white-space: nowrap;text-overflow: ellipsis;">${cus.recontent }</div>
				<a href="javascript:void(0)" onclick="document.getElementById('contact_nei${cus.id }').style.display='block'">显示全文</a>
				<div id="contact_nei${cus.id }" style="padding:10px;display:none;width:50%;height:700px;background:#e2e2e2;position:fixed;left:50%;top:50%;transform:translate(-50%,-50%);-webkit-transform:translate(-50%,-50%);">
					<a href="javascript:void(0)" onclick="document.getElementById('contact_nei${cus.id }').style.display='none';" style="float: right ;margin: 10px;">
              		 	Close</a>
					<div style="overflow: auto;width: 100%;height: 100%">
                		<p>${cus.recontent}</p>
                	</div>
                </div>
           </c:if>
				</td>
		
		     <td>${cus.reDate }</td>
		
				<td>
				<c:if test="${cus.userId !=0 }">
				<input type="button" onclick="reply(${cus.id},${adminid })" value="回复">
			    
				</c:if>
				</td>
				
			</Tr>
			</c:forEach>
		</table>
		<br/>
		<div align="center">${pager }</div>
	</div>

</body>
</html>