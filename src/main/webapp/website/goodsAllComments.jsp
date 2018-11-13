<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.8.2.min.js"></script>
<title>产品所有评论</title>
<style>
table, td, th
{
	border:1px solid black;
}
td
{
	padding:15px;
}



/* 隐藏框样式 */
.box{
    width:400px; margin-top:10%; margin:auto; padding:28px;
    height:350px; border:1px #111 solid;
    display:none;            /* 默认对话框隐藏 */
}
.box.show{display:block;} 
.box .x{ font-size:18px; text-align:right; display:block;}
.box input{width:80%; font-size:18px; margin-top:18px;}

</style>
</head>

<body>
			<form action="${pageContext.request.contextPath }/goodsComment/queryNewSaleAndCustomerComment.do" method="post">
			<table>
					<tr>
					<td>
					订单号：<input id="orderid" name="orderid" type="text" />
					产品id：<input id="goodsPid" name="goodsPid" type="text" />
					评论账号/ID：<input id="commentid" name="commentid" type="text" />
					时间：<input id="commenttime" name="commenttime" type="text" />
					销售：<select name="commentsale">
							<option value="">--请选择--</option>
							<option value="ling">ling
							<option value="ELISA">ELISA
						</select>
					评论者身份：<select id="commentIdentity" name="commentIdentity">
						<option value="">--请选择--</option>
						<option value="0">用户
						<option value="1">销售
					</select>
					</td>
						<td>
							<input id="search" type="submit"  value="查询" />
						</td>
					</tr>
				<tr><a href="/cbtconsole/website/comments_management.jsp">回到评论管理</a></tr>
				</table>
				</form>

	

<a href="${goods_url }"><img width="100px" height="150px" src="${goods_img }"></a> <div><font size="2px">${goodsname }</font> <br/>${goodsprice }/price </div> 
	
				
					
	 <table  >
	 		<thead>
	 			<tr>
					<td>id</td> 			
					<td>评论账号</td> 
					<td>订单详情编号</td>
					<td>规格</td>				
					<td>评论内容</td> 			
					<td>评论时间</td> 			
					<td>状态</td> 			
					<td>回复</td> 			
	 			</tr>
	 		</thead>
	 		<tbody>
	 		<c:forEach items="${CustCommentsBeans }" var="commentsBean">
	 			<tr>
					<td>${commentsBean.id }</td> 			
					<td>用户评论:<br/>
						用户ID:${commentsBean.userId }<br/>
						用户账号:${commentsBean.userName }</td> 
					<td>${commentsBean.orderDetailId }</td> 			
					<td>${commentsBean.carType }</td> 			
					<td>${commentsBean.commentsContent }</td> 			
					<td>${commentsBean.commentsTime }</td> 			
					<td><div id="cust${commentsBean.id }"> <c:if test="${commentsBean.showFlag==1 }">前端显示  <button onclick="setDisplay('${commentsBean.id}','0','cust')">设置为不显示</button> </c:if> <c:if test="${commentsBean.showFlag==0 }">前端不显示<button onclick="setDisplay('${commentsBean.id}','1','cust')">设置为显示</button></c:if></div></td> 			
					<td><button onclick="msgbox(1,'${commentsBean.userId}')">邮件回复</button></td> 			
	 			</tr>
	 		
	 		</c:forEach>
	 		<c:if test="${SaleCommentsBean != null}">
	 		<c:forEach items="${SaleCommentsBean}" var="saleCommentsBean">
	 			<tr>
					<td> ${saleCommentsBean.id }</td> 			
					<td>销售:${saleCommentsBean.userName }</td>
					 <td></td>		
					 <td></td>		
					<td>${saleCommentsBean.commentsContent }</td> 			
					<td>${saleCommentsBean.commentsTime }</td> 			
					<td><div id="sale${saleCommentsBean.id }" ><c:if test="${saleCommentsBean.showFlag== 1 }">前端显示  <button onclick="setDisplay('${saleCommentsBean.id}','0','sale')">设置为不显示</button></c:if> <c:if test="${saleCommentsBean.showFlag== 0 }">前端不显示<button onclick="setDisplay('${saleCommentsBean.id}','1','sale')">设置为显示</button></c:if>  </div> </td> 			
					<td></td> 			
	 			</tr>
	 			</c:forEach>
	 			</c:if>
	 		</tbody>
	 </table>	
 			<!-- 隐藏的窗口 -->
		<div id='inputbox' class="box">
        <a class='x' href=''; onclick="msgbox(0); return false;">关闭</a>
        <textarea id="mailContent" rows="20" cols="40"></textarea>
        <input type="hidden" id="userMailId" value="">
        <input type="button" onclick="sendMail()" value="发送">
     </div>
<script type="text/javascript">
		/* 参数1:评论的id 参数2:是否显示 0/不显示  1/显示  参数3:标识操作的是销售还是客户评论*/
	function setDisplay(id,showFlag,flag) {
		 $.ajax({
		    url:'/cbtconsole/goodsComment/updateComments.do',
		    type:'POST', 
		    async:true,    //或false,是否异步
		    data:{
		        id:id,
		        showFlag:showFlag
		    },
		    timeout:5000,    //超时时间
		    success:function(data){
		    	if(data == 'success'){
			        if(showFlag == 0){//设置为不显示
							if(flag == 'cust'){
								$("#cust"+id).html('前端不显示 <button onclick=setDisplay("'+id+'","1","cust")>设置为显示</button>')
							}
							if(flag == 'sale'){
								$("#sale"+id).html('前端不显示 <button onclick=setDisplay("'+id+'","1","sale")>设置为显示</button>')
							}
			        }
			        if(showFlag == 1){//设置为显示
			        	if(flag == 'cust'){
			        		$("#cust"+id).html('前端显示 <button onclick=setDisplay("'+id+'","0","cust")>设置为不显示</button>')
						}
						if(flag == 'sale'){
							$("#sale"+id).html('前端显示 <button onclick=setDisplay("'+id+'","0","sale")>设置为不显示</button>')
						}
			        }
		    		
		    	}
		    }
		}) 
		}
		/* 邮件 */
		/* 隐藏的输入框 */
		function msgbox(n,id){
			if(n==1){
				document.getElementById("userMailId").value=id
			}
		    document.getElementById('inputbox').style.display=n?'block':'none';     /* 点击按钮打开/关闭 对话框 */
		}
		/* 邮件发送 */
		function sendMail(){
			var content = document.getElementById("mailContent").value;
			var custId = document.getElementById("userMailId").value;
			if(content == ""){
				return;
			}
			 $.ajax({
				    url:'/cbtconsole/goodsComment/sendMailToCustomer.do',
				    type:'POST', 
				    async:true,    //或false,是否异步
				    data:{
				    	custId:custId,
				        content:content
				    },
				    success:function(data){
				    	if(data == 'success'){
				    		msgbox(0,null);
				    	}
				    }
			 });
		
	}


</script>
</body>

</html>