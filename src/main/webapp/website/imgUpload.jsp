<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<% 
String path = request.getContextPath(); 
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; 
%> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>图片搜索</title>
<script type="text/javascript">
	var check = function(){
		
		var f = $("#fileName").val();
        if(f==""){ 
        	alert("请上传图片");return false;
        }else{
/*        		if(!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(f)){
	          alert("图片类型必须是.gif,jpeg,jpg,png中的一种")
	          return false;
        	} */
       		if(!/\.(jpg|png|GIF)$/.test(f)){
  	          alert("图片类型必须是.jpg,png,gif中的一种")
  	          return false;
          	}
        }
        
	}; 
	
 	function fnSelect(value){
		document.getElementById("selectId").value=value;
 	}
 	
	function subMit(){
        $("#form2").submit();
	}
	
	function fnSubmit(){
		check();
		$("#fileForm").submit();
	}
	
</script>
<base href="<%=basePath%>"> 
<meta http-equiv="pragma" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache"> 
<meta http-equiv="expires" content="0"> 
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3"> 
<meta http-equiv="description" content="This is my page"> 
<!-- 
<link rel="stylesheet" type="text/css" href="styles.css"> 
--> 
<style type="text/css">
	ul li{list-style:none;}
</style>

</head>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
<div align="center">

	<div style="display:none;">
		<form action="https://s.taobao.com/image" method="post" enctype="multipart/form-data" name="idform" id="idform">
			<input type="file" id="imgfile" name="imgfile" />
			<input type="submit" value="提交查询" id="subsearch" name="subsearch" />
		</form>
	</div>

	
	<form name="fileForm" id="fileForm" action="/cbtconsole/customerServlet?action=upLoadImg&className=SearchUploadServlet" onsubmit="return check();" method="post" enctype="multipart/form-data" >
		<table>
			<c:if test="${uploadFileName !=null}">
				<tr>
					<td>
						<img width='200px' title="" height='200px;'  src="/cbtconsole/searchupload${uploadFileName }"> 
						<p style="color:red;">${errorMessage }</p>
					</td>
				</tr>
			</c:if>
			<tr>
				<td>
					请选择上传的图片或文件:<input type="file" name="fileName" id="fileName" accept="image/jpeg,image/png,image/gif" onchange="return fnSubmit();"/>
				</td>
				
			</tr>
<!-- 			<tr>
		        <td>
		        	<input type="submit" value="上传"/>
		        </td>
			</tr> -->
		</table> 
	</form>
	
	<form action="/cbtconsole/customerServlet?action=findImg&className=SearchUploadServlet" name="form2" id="form2" method="post" >
		<table>
			<tr>
				<td>大类索引
					<select id="maxSelect" style="width:200px" onchange="fnSelect(this.value)" > 
								<option selected="selected"></option>
			             <c:forEach var="largeIndexList" items="${largeIndexList}">
			             	<c:if test="${maxSelectId==largeIndexList.indexName}">
			             		<option value="${largeIndexList.indexName}" selected="selected">${largeIndexList.indexNameCn}</option>
			             	</c:if>
			             	<c:if test="${maxSelectId!=largeIndexList.indexName}">
			             		<option value="${largeIndexList.indexName}">${largeIndexList.indexNameCn}</option> 
			             	</c:if>
			             </c:forEach>
		          </select>
		          <input type="hidden" id="selectId" name ="selectId">
		        </td>
		        <td>
		        	<input  type="button" value="搜索" onclick="subMit();"/> 
		        	<input type="hidden" id="uploadImg" name ="uploadImg" value="${uploadFileName }">
		        </td>
			</tr>
		</table> 
	</form>
	
	</div>
	<div style="width:1280px;margin:0 auto;position:relative;border:1px solid #777;">
		<ul>
			<!-- 淘宝数据 -->
			<c:if test="${gcb.tbUrl!=null}">
				<li style="width:280px;float:left;margin:10px;" id="tr">
					<div style="border:1px solid #777;padding:10px;position:relative;height:350px;">
						<a target='_blank' href="${gcb.tbUrl }" >
								<img  width='200px' title="" height='200px;' src="${gcb.tbImg }">
						</a>
						<br>
						${gcb.tbName }
						<br>
						USD ${gcb.tbprice }
					</div>
				</li>
				<li style="width:280px;float:left;margin:10px;" id="tr">
					<div style="border:1px solid #777;padding:10px;position:relative;height:350px;">
						<a target='_blank' href="${gcb.tbUrl1 }" >
								<img  width='200px' title="" height='200px;' src="${gcb.tbImg1 }">
						</a>
						<br>
						${gcb.tbName1 }
						<br>
						USD ${gcb.tbprice1 }
					</div>
				</li>
				<li style="width:280px;float:left;margin:10px;" id="tr">
					<div style="border:1px solid #777;padding:10px;position:relative;height:350px;">
						<a target='_blank' href="${gcb.tbUrl2 }" >
								<img  width='200px' title="" height='200px;' src="${gcb.tbImg2 }">
						</a>
						<br>
						${gcb.tbName2 }
						<br>
						USD ${gcb.tbprice2 }
					</div>
				</li>
				<li style="width:280px;float:left;margin:10px;" id="tr">
					<div style="border:1px solid #777;padding:10px;position:relative;height:350px;">
						<a target='_blank' href="${gcb.tbUrl3 }" >
								<img  width='200px' title="" height='200px;' src="${gcb.tbImg3 }">
						</a>
						<br>
						${gcb.tbName3 }
						<br>
						USD ${gcb.tbprice3 }
					</div>
				</li>
			</c:if>
			<!-- 1688数据 -->
			<c:forEach items="${gbbs }" var="gbb" varStatus="i">
				<li style="width:280px;float:left;margin:10px;" id="tr${i.index}">
				<div style="border:1px solid #777;padding:10px;position:relative;height:350px;">
						<a target='_blank' href="${gbb.newUrl }" >
								<img  width='200px' title="" height='200px;' src="http://192.168.1.26${gbb.newImg}">
							</a>
							<br>
							${gbb.newName }
							<br>
							USD ${gbb.newPrice }
							<%-- <br>
	 						相似度 ${gbb.score } --%>
							<%--zhushi start <input type="checkbox" class="checkbox"id="check${i.index}"
					              	onclick="fnadd('check${i.index}','${gbb.tbUrl }','${gbb.tbName }','${gbb.tbprice }','${gbb.tbImg }','${gbb.aligSourceUrl}','${gbb.goodsDataId}','${gbb.orderNo}')" style="width: 20px;height: 20px;"> zhushi end--%>
				</div>
			</c:forEach>
		</ul>
		<div style="clear:both;"></div>
	<%-- 	<div align="center">${pager }</div> --%>
	</div>
</div>
</body>
</html>