<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<title>文件目录</title>
<style type="text/css">
body{width: 1200px;margin: 0 auto;}
.body{width: 1200px;height:auto;}
.mainlu2{ 
color:#2352E8;
font-size:25px;

}

.mainlu3{cursor: pointer;width: 200px;}
.mainlu5{cursor: pointer;width: 200px;}
.mainlu7{cursor: pointer;width: 200px;}
.mainlu9{cursor: pointer;width: 200px;}


.mainlm3{margin-left: 50px;display:none;}
.mainlm5{margin-left: 50px;display:none;}
.mainlm7{margin-left: 50px;display:none;}
.mainlm9{margin-left: 50px;display:none;}

.mainlm{border: 1px solid #e3e3e3;}


</style>
<script type="text/javascript">
function fnget(obj,index){
	if($("#show"+index).val()=='0'){
		$("#"+obj+index).show();
		$("#show"+index).val(1);
		
	}else{
		$("#"+obj+index).hide();
		$("#show"+index).val(0);
	}
}

function fndelete() {
	var checked = '';
	$("input[name='delete']:checked").each(function(){
		  checked += this.value+ ',';
	});
	if(checked==''){
		return;
	}
	$.ajax({
		type:'POST',
		dataType:'text',
		url:'/cbtconsole/cutom/delete',
		data:{path:checked},
		success:function(res){
			window.location.href = "/cbtconsole/cutom/file";
		},
		error:function(XMLResponse){
			alert('error');
		}
	});
}

</script>

</head>
<body class="body">
<br>
<br>
<div align="center" style="font-size: 30px;">目&nbsp;&nbsp;录 </div>
<div class="param"><input type="button" value="删除" onclick="fndelete()"></div>
<div class="mainlm" >
<c:forEach items="${files}" var="file">
<c:set value="${file.index}" var="index"></c:set>
<c:if test="${file.isFile==1}">
<div class="mainlu2" >
	<input type="checkbox" name="delete" value="${file.fileParent}${file.filePath}"><span class="mainlu3" onclick="fnget('mainlm3','${index}')" >${file.filePath }</span>
	<input type="hidden" value="0" id="show${index}">
				<div class="mainlm3" id="mainlm3${index}">
				<c:forEach items="${file.list}" var="file2"  varStatus="index">
				<c:set value="${file.index}${file2.index}" var="index2"> </c:set>
				<c:if test="${file2.isFile==1}">
				<div class="mainlu4" >
				  <input type="checkbox" name="delete" value="${file2.fileParent}${file2.filePath}">
				  <span class="mainlu5" onclick="fnget('mainlm5','${index2}')">${file2.filePath }</span>
				    <input type="hidden" value="0" id="show${index2}">
							<div class="mainlm5" id="mainlm5${index2}">
							<c:forEach items="${file2.list}" var="file3">
							<c:set value="${file.index}${file2.index}${file3.index}" var="index3"> </c:set>
							<c:if test="${file3.isFile==1}">
							<div class="mainlu6" >
							<input type="checkbox" name="delete" value="${file3.fileParent}${file3.filePath}">
							<span class="mainlu7" onclick="fnget('mainlm7','${index3}')">${file3.filePath }</span>
							<input type="hidden" value="0" id="show${index3}">
									<div class="mainlm7" id="mainlm7${index3}">
											<c:forEach items="${file3.list}" var="file4">
											<c:set value="${file.index}${file2.index}${file3.index}${file4.index}" var="index4"> </c:set>
											<c:if test="${file4.isFile==1}">
											<div class="mainlu8" >
											<input type="checkbox" name="delete" value="${file4.fileParent}${file4.filePath}">
											<span class="mainlu9" onclick="fnget('mainlm9','${index4}')">${file4.filePath }</span>
											<input type="hidden" value="0" id="show${index4}">
													<div class="mainlm9" id="mainlm9${index4}">
														<c:forEach items="${file4.list}" var="file5">
														
														<c:if test="${file5.isFile==1}">
														<div class="mainlu10" >
														<input type="checkbox" name="delete" value="${file5.fileParent}${file5.filePath}">
														<span class="mainlu11">${file5.filePath }</span>
														
														
														</div>
														
														</c:if>
														<c:if test="${file5.isFile==0}">
														<div class="mainlu10" style="color: black;">
														<input type="checkbox"  value="${file5.fileParent}${file5.filePath}" name="delete">${file5.filePath }
														</div>
														</c:if>
														
														
														</c:forEach>
														</div>
											</div>
											</c:if>
											<c:if test="${file4.isFile==0}">
											<div class="mainlu8" style="color: black;">
											<input type="checkbox" value="${file4.fileParent}${file4.filePath}" name="delete">${file4.filePath }
											</div>
											</c:if>
											</c:forEach>
										</div>
							</div>
							</c:if>
							<c:if test="${file3.isFile==0}">
							<div class="mainlu6" style="color: black;">
							<input type="checkbox" value="${file3.fileParent}${file3.filePath}" name="delete">${file3.filePath }
							</div>
							</c:if>
							
							</c:forEach>
							</div>
				</div>
				
				</c:if>
				<c:if test="${file2.isFile==0}">
				<div class="mainlu4" style="color: black;">
				<input type="checkbox" value="${file2.fileParent}${file2.filePath}" name="delete">${file2.filePath }
				</div>
				</c:if>
				</c:forEach>
				</div>
	</div>

</c:if>
<c:if test="${file.isFile==0}">
<div class="mainlu2" style="color: black;">
<input type="checkbox" value="${file.fileParent}${file.filePath}" name="delete">${file.filePath }
</div>
</c:if>


</c:forEach>
</div>
<br>
<br>
<br>
<br>

</body>
</html>