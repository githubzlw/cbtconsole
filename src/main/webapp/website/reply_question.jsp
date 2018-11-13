<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>回复留言问题</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
		<script type="text/javascript" >
		
		function updateReply(){
			 var id= document.getElementById("gid").value;
			 var replyContent= document.getElementById("rc").value;
			 var index = document.getElementById("index").value;
			 var name = '${param.name}';
			 var email = '${param.email}';
			 var qustion = "${param.qustion}";
			 var pname = "${param.pname}";
			 var purl = '${param.purl}'
			 if(replyContent==null||replyContent==""){
				 $("#ts").html("请输入回复内容");
				 return false;
			 }/* else{ return true;
				 window.close();
			 } */
			 var params = {  
		    			"id":id,
		    			"name":name,
		    			"email":email,
		    			"qustion":qustion,
		    			"pname":pname,
		    			"replyContent":replyContent,
		    			"className":"GuestBookServlet",
		    			"action":"reply",
		    			"userId":'${param.userId}',
		    			"purl":'${param.purl}'
		    		};
		       $.ajax({  
	                      url:'/cbtconsole/customerServlet',  
	                      type:"post",  
	                      data:params,  
	                     // dataType:"json",  
	                      success:function(data)  
	                              { 
	                    	  		if(data < 0){
	                    	  			alert("失败");
	                    	  		}else{
	                    	  			window.opener.document.getElementById("spanrep"+index).innerHTML=replyContent;
	                    	  			window.opener.document.getElementById("spantime"+index).innerHTML=data;
		    	   						alert("保存成功");
		    	   						window.close();
	                    	  		}
	                              }, 
	                      //error:function(){alert("保存失败");}
	                  }  
	              ); 
		}
		
	</script>
	</head>
	<body>
	<%-- <form action="/cbtconsole/AbstractServlet?action=reply&className=GuestBookServlet" onsubmit="return updateReply()"> --%>
		<input type="hidden" value="<%=request.getParameter("id")%>" name="id" id="gid">
		<input type="hidden" value="<%=request.getParameter("index")%>" name="id" id="index">
		回复内容:</br>
		<!-- <input type="text" name="replyContent" id="rc" height="50px;" size="80"></input> -->
		<textarea name="replyContent" rows="8" cols="50" id="rc"></textarea>
		<font color="red" id="ts"></font></br>
		<input type="button" id="replyButton" onclick="updateReply()" value="提交回复">
	<!-- </form> -->
	</body>
</html>
