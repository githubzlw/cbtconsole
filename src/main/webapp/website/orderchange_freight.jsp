<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>交期偏长</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript">
		var updateFreight =function(){
			 var orderNo= document.getElementById("orderNo").value;
			 var goodId= document.getElementById("goodId").value;
			 var oldFreight= document.getElementById("oldFreight").value;
			 var newFreight= document.getElementById("newFreight").value;
			 var index= document.getElementById("index").value;
			 
		       var params = {  
		    			"orderNo":orderNo,
		    			"action":"upOrderChange",
		    			"className":"OrderwsServlet",
		    			"goodId":goodId,
		    			"oldInfo":oldFreight,
		    			"changeType":7,
		    			"newInfo":newFreight
		    		};
		       $.ajax({  
	                      url:'/cbtconsole/WebsiteServlet',  
	                      type:"post",  
	                      data:params,  
	                      dataType:"json",  
	                      success:function(data)  
	                              { 
	                    	  //alert(data.result+"==="+data);
	                    	  		if(data.result>0){
		    	   						alert("保存成功");
		    	   						$('#orderDetail tr:eq('+(index+1)+') td:eq(7)',window.opener.document).html(newFreight);
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
	运费修改
	
	<input type="hidden" value="<%=request.getParameter("orderNo")%>" name="orderNo" id="orderNo">
	<input type="hidden" value="<%=request.getParameter("goodId")%>" name="goodId" id="goodId">
	<input type="hidden" value="<%=request.getParameter("oldFreight")%>" name="oldFreight" id="oldFreight">
	<input type="hidden" value="<%=request.getParameter("index")%>" name="index" id="index">
	运费：<input id="newFreight" name="newFreight" type="text" />
	<button id="freightButton" onclick="updateFreight()">运费修改</button>
	</body>
</html>
