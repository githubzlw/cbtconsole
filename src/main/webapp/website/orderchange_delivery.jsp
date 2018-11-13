<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>交期偏长</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="/cbtconsole/js/website/order_detail_new.js"></script>
		<script type="text/javascript">
		var updatedeliver =function(){
			 var orderNo= document.getElementById("orderNo").value;
			 var goodId= document.getElementById("goodId").value;
			 var oldDeliver= document.getElementById("oldDeliver").value;
			 var newDeliver= document.getElementById("newDeliver").value;
			 var index= document.getElementById("index").value;
			 var isDropshipOrder=document.getElementById("isDropshipOrder1").value;
			 if(newDeliver==null||newDeliver==""){
				 alert("请选择日期");
				 return;
			 }
		       var params = {  
		    			"orderNo":orderNo,
		    			"action":"upOrderChange",
		    			"className":"OrderwsServlet",
		    			"goodId":goodId,
		    			"oldInfo":oldDeliver,
		    			"changeType":2,
		    			"newInfo":newDeliver
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
		    	   						//window.opener.document.getElementById("spandel"+index).innerHTML=newDeliver;
		    	   						// window.opener.document.getElementById("change_delivery_"+index).innerHTML="<br>"+newDeliver;
                                        var flag=0;
                                        if(isDropshipOrder ==1){
                                            flag=1;
                                        }
                                        sendCutomers(orderNo,1,flag);
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
	交期太长
	
	<input type="hidden" value="<%=request.getParameter("orderNo")%>" name="orderNo" id="orderNo">
	<input type="hidden" value="<%=request.getParameter("goodId")%>" name="goodId" id="goodId">
	<input type="hidden" value="<%=request.getParameter("oldDeliver")%>" name="oldDeliver" id="oldDeliver">
	<input type="hidden" value="<%=request.getParameter("index")%>" name="index" id="index">
	<input type="hidden" value="<%=request.getParameter("isDropshipOrder1")%>" name="isDropshipOrder1" id="isDropshipOrder1">
	天数：<input id="newDeliver" name="newDeliver" type="text" />
	<button id="deliverButton" onclick="updatedeliver()">交期变更</button>
	</body>
</html>
