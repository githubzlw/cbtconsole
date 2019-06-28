<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>价格更新</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="/cbtconsole/js/website/order_detail_new.js"></script>
		<script type="text/javascript" >
		var record={
                num:"1"
            };
		var checkDecimal = function(n) {
			var decimalReg = /^\d{1,9}\.{0,1}(\d{1,3})?$/;//var decimalReg=/^[-\+]?\d{0,8}\.{0,1}(\d{1,2})?$/;
			if (n.value != "" && decimalReg.test(n.value)) {
				record.num = n.value;
			} else {
				if (n.value != "") {
					n.value = record.num;
				}
			}
		};
		
		var updatePrice =function(){
			 var orderNo= document.getElementById("orderNo").value;
			 var goodId= document.getElementById("goodId").value;
			 var oldPrice= document.getElementById("oldPrice").value;
			 var newPrice= document.getElementById("newPrice").value;
			 var index= document.getElementById("index").value;
			 var isDropshipOrder=document.getElementById("isDropshipOrder1").value;
			 if(newPrice==null||newPrice==""){
				 alert("请输入变更价格");
				 return;
			 }
		       var params = {  
		    			"orderNo":orderNo,
		    			"action":"upOrderChange",
		    			"className":"OrderwsServlet",
		    			"goodId":goodId,
		    			"oldInfo":oldPrice,
		    			"changeType":1,
		    			"newInfo":newPrice
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
		    	   						//window.opener.document.getElementById("spanpri"+index).innerHTML=newPrice;
		    	   						window.opener.document.getElementById("change_price_"+index).innerHTML="<br>"+newPrice;
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
	更改价格<br/>
	<input type="hidden" value="<%=request.getParameter("orderNo")%>" name="orderNo" id="orderNo">
	<input type="hidden" value="<%=request.getParameter("goodId")%>" name="goodId" id="goodId">
	<input type="hidden" value="<%=request.getParameter("oldPrice")%>" name="oldPrice" id="oldPrice">
	<input type="hidden" value="<%=request.getParameter("index")%>" name="index" id="index">
	<input type="hidden" value="<%=request.getParameter("isDropshipOrder1")%>" name="isDropshipOrder1" id="isDropshipOrder1">
	新价格:<input type="text" name="newPrice" id="newPrice" size="20" onblur="checkDecimal(this)"></input>
	<button id="priceButton" onclick="updatePrice()">价额变更</button>
	公司网站：<select id="Web_site" style="font-size: 16px; height: 24px; width: 150px;">
		<option value="0" selected="selected">import-express</option>
		<option value="1">kidsproductwholesale</option>
	</select>
	</body>
</html>
