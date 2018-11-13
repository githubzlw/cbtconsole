<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>更换货源</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
		<script type="text/javascript" >
		var record={
                num:"1"
            };
		var checkDecimal = function(n) {
			var decimalReg = /^\d{1,9}\.{0,1}(\d{1,2})?$/;//var decimalReg=/^[-\+]?\d{0,8}\.{0,1}(\d{1,2})?$/;
			if (n.value != "" && decimalReg.test(n.value)) {
				record.num = n.value;
			} else {
				if (n.value != "") {
					n.value = record.num;
				}
			}
		};
		var updateSupply =function(){
			 var orderNo= document.getElementById("orderNo").value;
			 var goodId= document.getElementById("goodId").value;
			 var oldSupply= document.getElementById("oldSupply").value;
			 var newSupply= document.getElementById("newSupply").value;
			 var index= document.getElementById("index").value;
			 if(oldSupply==null||oldSupply==""){
				 alert("请输入替换价格");
				 return;
			 }
			 if(newSupply==null||newSupply==""){
				 alert("请输入替换地址");
				 return;
			 }
		       var params = {  
		    			"orderNo":orderNo,
		    			"action":"upOrderChange",
		    			"className":"OrderwsServlet",
		    			"goodId":goodId,
		    			"oldInfo":oldSupply,
		    			"changeType":6,
		    			"newInfo":newSupply
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
		    	   						window.close();
		    	   						window.opener.document.getElementById("spanurl"+index).innerHTML=oldSupply+"<br/>"+newSupply;
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
	替换价格：<input type="text" name="oldSupply" id="oldSupply" size="20"  onblur="checkDecimal(this)"></input><br/>
	替换地址：<input type="text" name="newSupply" id="newSupply" size="20"></input>
	<button id="priceButton" onclick="updateSupply()">替换</button>
	</body>
</html>
