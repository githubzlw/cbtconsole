<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>黑名单列表</title>
<style>
body{
   width: 100%;
}
table{
   width: 800px;
   border-collapse: collapse;
}
.manager{
  margin-top: 20px;
}
.page{
 padding-left: 673px;
}
.top{
 font-size: 20px;
}
</style>
<script type="text/javascript">   
   var  list=[]; 
   function delblacklist(){
	   $('[type="checkbox"]:checked').each(function(){
		      var value=$(this).attr('value'); 
		      list.push(value);
		}); 
	   if(list.length==0){
		   alert("必须选择一个删除");
		   return;
	   }

	   $.get("/cbtconsole/BlackListServlet",{action: "delblacklist",page:'${param.page}',ids:list.join(",")},function(result){
		    var json=eval('('+result+')');
		    var code=json[0].code;
		    alert(json[0].msg);
		    if(code==1){
		    	 window.location.href="/cbtconsole/BlackListServlet?action=blacklist&page=${param.page}";
		    }	
		    list.length=0;
		});
   }
   
   var  id=[]; 
   function modifyblacklist(){
	   $('[type="checkbox"]:checked').each(function(){
		      var value=$(this).attr('value'); 
		      id.push(value);
		}); 
	   if(id.length==0){
		   alert("必须选择一个修改");
		   return;
	   }else if(id.length>1){
		   alert("只能选择一个修改");
		   id.length=0;
		   return;
	   }
	   window.location.href="/cbtconsole/BlackListServlet?action=modifyblacklist&page=${param.page}&id="+id;
   }
   
   function selectblacklist(){
	   var email=$("#selemail").val();
	   var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/; 
	   if(email.trim()==""){
		   alert("邮箱不能为空");
		   $("#email").focus();
		   return false;
	   }else if(!reg.test(email)){
		   alert("邮箱格式错误");
		   return false;
	   }
	   window.location.href="/cbtconsole/BlackListServlet?action=blacklist&page=${param.page}&email="+email;
   }
   
   //href="/cbtconsole/BlackListServlet?action=delblacklist"
</script>
</head>
<body>
<div align="center">
    <div class="top">
        <span>黑名单列表</span>
    </div>
    <div style="height: 10px">
    </div>
    <div class="select">
              邮箱:<input type="text" id="selemail" value="${param.email}"/>
        <input type="button" id="selebutton" onclick="selectblacklist()" value="查询"/>
    </div>
    <div class="manager">
        <a href="/cbtconsole/BlackListServlet?action=addblacklist">添加黑名单</a>
        <a onclick="modifyblacklist()" href="javascript:void(0);" >修改黑名单</a>
        <a onclick="delblacklist()" href="javascript:void(0);" >删除黑名单</a>
    </div>
    <div style="height: 10px">
    </div>
	<div>
		<table border="1">
		        <tr> 
			        <th></th> 
			        <th>邮箱</th> 
			        <th>创建时间</th> 
			        <th>操作人</th> 
			        <th>ip</th> 
			    </tr> 
				<c:forEach items="${list}" var="list">     
				      <tr>   
				          <td><input type="checkbox" id="checkbox" name="checkbox" value="${list.id}"/></td>
				          <td>${list.email}</td>
				          <td>${list.createtime}</td>
				          <td>${list.operatorid}</td>
				          <td>${list.userip}</td>
				      </tr>     
				</c:forEach> 
		</table> 
	</div>
	<%-- <div class="page">
	   <table>
	       <tr>
	           <td>
		           <span><a href="/cbtconsole/BlackListServlet?action=blacklist&page=1">首页</a></span><span>|</span>
		           <c:if test="${param.pagenum!=1}">
		                 <span><a href="/cbtconsole/BlackListServlet?action=blacklist&page=${param.pagenum-1}">上一页</a></span>
		                 <span>|</span>
		           </c:if>
		           <span><a href="/cbtconsole/BlackListServlet?action=blacklist&page=${param.pagenum+1}">下一页</a></span>
	           </td>
	       </tr>
	   </table>
	</div> --%>
	<div align="center">${pager }</div>
</div>
</body>
</html>