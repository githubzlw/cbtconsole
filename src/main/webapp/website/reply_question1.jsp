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
			 var replyContent = CKEDITOR.instances.content1.getData();
			 var title= document.getElementById("rc1").value;
			 var email= document.getElementById("email").value;
			 var userId= document.getElementById("userId").value;
			 if(replyContent==null||replyContent==""){
				 $("#ts").html("请输入回复内容");
				 return false;
			 }
			 var params = {  
		    			"id":id,
		    			"title":title,
		    			"email":email,
		    			"userId":userId,
		    			"replyContent":replyContent,
		    			"className":"EmailReceiveServlet",
		    			"action":"reply1",
		    			
		    		};
		       $.ajax({  
	                      url:'/cbtconsole/helpServlet',  
	                      type:"post",  
	                      data:params,  
	                     // dataType:"json",  
	                      success:function(data)  
	                              { 
	                    	  		if(data < 0){
	                    	  			alert("失败");
	                    	  		}else{
	                    	  			//window.opener.document.getElementById("spanrep"+index).innerHTML=replyContent;
	                    	  			//window.opener.document.getElementById("spantime"+index).innerHTML=data;
		    	   						alert("保存成功");
		    	   						window.close();
	                    	  		}
	                              }, 
	                      //error:function(){alert("保存失败");}
	                  }  
	              ); 
		}
		
	</script>
	   <script type="text/javascript" src="/cbtconsole/ckfinder/ckfinder.js"></script>
<script type="text/javascript" src="/cbtconsole/ckeditor/ckeditor.js"></script>
	</head>
	<body>
	
		<input type="hidden" value="${er.id}" name="id" id="gid">
		<input type="hidden" value="${er.email}" name="email" id="email">
		<input type="hidden" value="${userId}" name="userId" id="userId">
		
		
		回复标题:</br>
	    <input type="text" name="rc1"  size="40" id="rc1" value="RE:${er.title }"></input></br>
		回复内容:</br>
	       正文:<div contenteditable="true" style="width: 1000px; border:1px solid #C0C0C0" id="rc" runat="server">
				   <div><includetail>
				   		<textarea  id="content1" name="content" runat="server"><div  style="font-size:16px;" >
				         <br/>
				         </div>
                         <div style="font-size: 12px;font-family: Arial Narrow;padding:2px 0 2px 0;">------------------&nbsp;Original mail&nbsp;------------------</div>
				   		<div style="font-size: 12px;background:#efefef;padding:8px;"><div></div>
				   		<div><b>From:</b>&nbsp;${er.email }</div>
				   		<div><b>Subject:</b>&nbsp;${er.title }</div></div>
				   		<div><br></div>  ${er.content }</textarea>
                          <script type="text/javascript">
                           CKEDITOR.replace('content1');
                          </script>                            
                                                                         
                      </td>
				        </tr>
				   		
				   		</includetail></div>
				   	</div>
		<font color="red" id="ts"></font></br>
		<input type="button" id="replyButton" onclick="updateReply()" value="提交回复">

	</body>
</html>
