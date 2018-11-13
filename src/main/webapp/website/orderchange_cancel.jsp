<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>不能购买：直接取消</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
		<script type="text/javascript" >
		var updateCancel =function(){
			 var orderNo= document.getElementById("orderNo").value;
			 var goodId= document.getElementById("goodId").value;
			 var oldInfo= document.getElementById("oldCancel").value;
			 var newInfo= document.getElementById("newCancel").value;
			 var index= document.getElementById("index").value;
			 if(newInfo==null||newInfo==""){
				 alert("请输入原因");
				 return;
			 }
		       var params = {  
		    			"orderNo":orderNo,
		    			"action":"upOrderChange",
		    			"className":"OrderwsServlet",
		    			"goodId":goodId,
		    			"oldInfo":oldInfo,
		    			"changeType":4,
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
		    	   						window.opener.document.getElementById("change_cancel_"+index).innerHTML='系统取消';
		    	   						//$('#orderDetail tr:eq('+(index+1)+') td:eq(13)',window.opener.document).html('已取消');
		    	   						window.close();
	                    	  		}else{
	                    	  			alert("保存失败");
	                    	  		}
	                              }, 
	                      error:function(){alert("保存失败");}
	                  }  
	              );  
	};
	</script>
	</head>
	<body>
	<input type="hidden" value="<%=request.getParameter("orderNo")%>" name="orderNo" id="orderNo">
	<input type="hidden" value="<%=request.getParameter("goodId")%>" name="goodId" id="goodId">
	<input type="hidden" value="<%=request.getParameter("index")%>" name="index" id="index">
	<input type="hidden" value="<%=request.getParameter("isDropshipOrder1")%>" name="isDropshipOrder1" id="isDropshipOrder1">
	<input type="hidden" value="" name="oldCancel" id="oldCancel">
	取消理由:<textarea rows="3" cols="20" name="newCancel" id="newCancel">discontinued</textarea>
	<button id="priceButton" onclick="updateCancel()">取消产品</button>
	</body>
</html>
