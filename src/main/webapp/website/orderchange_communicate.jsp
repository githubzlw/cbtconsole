<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>不能购买：需沟通</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="/cbtconsole/js/website/order_detail_new.js"></script>
		<script type="text/javascript" >
		var updateCommmunicate =function(){
			 var orderNo= document.getElementById("orderNo").value;
			 var goodId= document.getElementById("goodId").value;
			 var oldInfo= document.getElementById("oldCommunicate").value;
			 var newInfo= document.getElementById("newCommunicate").value;
			 var isDropshipOrder= document.getElementById("isDropship").value;
			 
			 if(newInfo==null||newInfo==""){
				 alert("请输入问题");
				 return;
			 }
		       var params = {  
		    			"orderNo":orderNo,
		    			"action":"upOrderChange",
		    			"className":"OrderwsServlet",
		    			"goodId":goodId,
		    			"oldInfo":oldInfo,
		    			"changeType":5,
		    			"newInfo":newInfo
		    		};
		       $.ajax({  
	                      url:'/cbtconsole/WebsiteServlet',  
	                      type:"post",  
	                      data:params,  
	                      dataType:"json",
	                      success:function(data)  
	                              { 
	                    	  		if(data.result>0){
		    	   						alert("保存成功");
		    	   						var flag=0;
		    	   						if(isDropshipOrder ==1){
		    	   						    flag=1;
										}
                                        sendCutomers(orderNo,1,flag);
		    	   						$(window.parent.document).find("#change_cmmunication_${goodid}").append("<br>"+newInfo);
		    	   						$("#send2").css("display","none");
		    	   						$("#send1").css("display","inline");
		    	   						/*var obj=setTimeout(refresh(orderNo), 3000);
		    	   						clearTimeout(obj);*/
		    	   						window.close();
		    	   						refresh(orderNo,isDropshipOrder);
	                    	  		}else{
	                    	  			alert("保存失败");
	                    	  		}
	                              }, 
	                      error:function(){alert("保存失败");}
	                  }  
	              );  
	};
	function fnResolve(){
		 var orderNo= document.getElementById("orderNo").value;
		 var goodId= document.getElementById("goodId").value;
		 
	       var params = {  
	    			"orderNo":orderNo,
	    			"action":"upOrderChangeResolve",
	    			"className":"OrderwsServlet",
	    			"goodId":goodId,
	    			"changeType":5
	    		};
	       $.ajax({  
                      url:'/cbtconsole/WebsiteServlet',  
                      type:"post",  
                      data:params,  
                      dataType:"json",  
                      success:function(data)  
                              { 
                    	  		if(data>0){
	    	   						//alert("保存成功");
	    	   						$("#send1").css("display","none");
	    	   						$("#send2").css("display","inline");
	    	   						/*var obj=setTimeout(refresh(orderNo), 3000);
	    	   						clearTimeout(obj);*/
	    	   						
	    	   						window.close();
	    	   						refresh(orderNo,isDropshipOrder);
                    	  		}else{
                    	  			alert("保存失败");
                    	  		}
                              }, 
                      error:function(){alert("保存失败");}
                  }  
              );  
	}
	
	function refresh(orderNo,isDropshipOrder){
		//document.location.href = "WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo="+orderNo+"&state=5&rand="+Math.random();
		
		/* if (isDropshipOrder!=1) {
			
		window.open("/cbtconsole/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo="+orderNo+"&state=5&rand="+Math.random());
		} else {
		window.open("/cbtconsole/WebsiteServlet?action=getChildrenOrderDetail&className=OrderwsServlet&isDropshipOrder="+isDropshipOrder+"&orderNo="+orderNo+"&state=5&rand="+Math.random());
		} */
		
		if(isDropshipOrder!=1){
			window.open("/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="+orderNo+"&state=5&rand="+Math.random());
		}else{
			window.open("/cbtconsole/orderDetails/queryByOrderNo.do?isDropshipOrder="+isDropshipOrder+"&orderNo="+orderNo+"&state=5&rand="+Math.random());		
		}
		window.opener.close();
	}
	</script>
	</head>
	<body>
		<input type="hidden" value="<%=request.getParameter("orderNo")%>" name="orderNo" id="orderNo">
		<input type="hidden" value="<%=request.getParameter("goodId")%>" name="goodId" id="goodId">
		<input type="hidden" value="<%=request.getParameter("isDropship")%>" name="isDropship" id="isDropship">
		<input type="hidden" value="1" name="oldCommunicate" id="oldCommunicate">
		
		<table border="0">
			<tr>
				<td rowspan="2">问题:</td>
				<td rowspan="2"><textarea rows="3" cols="20" name="newCommunicate" id="newCommunicate"></textarea></td>
				<td colspan="2">
					<label style="display: none;color:rend" id="send1">已发送，3秒后刷新</label><label style="display: none;color:rend" id="send2">已解决，3秒后刷新</label>
				</td>
			</tr>
			<tr>
				<td><button id="priceButton" onclick="updateCommmunicate()">发送</button></td>
				<td><button id="priceButton" onclick="fnResolve()">问题解决了</button></td>
				<%--公司网站：<select id="Web_site" style="font-size: 16px; height: 24px; width: 150px;">--%>
				<%--<option value="0" selected="selected">import-express</option>--%>
				<%--<option value="1">kidsproductwholesale</option>--%>
			<%--</select>--%>
			</tr>
		</table>
	
	<div id="addCon">
	<table>
		<tr><td>内容</td></tr>
	<c:forEach items="${cont}" var="consss">
		<tr>
			<td>${consss}</td>
		</tr>
	</c:forEach>
	</table>
</div>
	</body>
</html>
