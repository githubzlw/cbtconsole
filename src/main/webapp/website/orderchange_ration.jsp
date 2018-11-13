<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>不能购买：订量偏低</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="/cbtconsole/js/website/order_detail_new.js"></script>
		<script type="text/javascript" >
		var checkDecimal = function(n) {
			var ddd=/^\+?[1-9][0-9]*$/;
			if(!ddd.test(n.value)){
				alert("请输入数字");
			}
		};
		
		var updatePrice =function(){
			 var orderNo= document.getElementById("orderNo").value;
			 var goodId= document.getElementById("goodId").value;
			 var oldRation= document.getElementById("oldRation").value;
			 var newRation= document.getElementById("newRation").value;
			 var index= document.getElementById("index").value;
			 var isDropshipOrder=document.getElementById("isDropshipOrder1").value;
			 if(newRation==null||newRation==""){
				 alert("请输入最新定量");
				 return;
			 }
		       var params = {  
		    			"orderNo":orderNo,
		    			"action":"upOrderChange",
		    			"className":"OrderwsServlet",
		    			"goodId":goodId,
		    			"oldInfo":oldRation,
		    			"changeType":3,
		    			"newInfo":newRation
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
		    	   						//$('#orderDetail tr:eq('+(index+1)+') td:eq(17)',window.opener.document).html(newRation);
		    	   						window.opener.document.getElementById("change_number_"+index).innerHTML;
		    	   						alert("保存成功");
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
	<input type="hidden" value="<%=request.getParameter("orderNo")%>" name="orderNo" id="orderNo">
	<input type="hidden" value="<%=request.getParameter("goodId")%>" name="goodId" id="goodId">
	<input type="hidden" value="<%=request.getParameter("oldRation")%>" name="oldRation" id="oldRation">
	<input type="hidden" value="<%=request.getParameter("index")%>" name="index" id="index">
	<input type="hidden" value="<%=request.getParameter("isDropshipOrder1")%>" name="isDropshipOrder1" id="isDropshipOrder1">
	最低定量：<input type="text" name="newRation" id="newRation" maxlength="8" size="20" onblur="checkDecimal(this)"></input>
	<button id="priceButton" onclick="updatePrice()">定量变更</button>
	</body>
</html>
