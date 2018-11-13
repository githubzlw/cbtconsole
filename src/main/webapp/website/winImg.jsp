<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>橱窗图片</title>
<style>
.a1{ color:#00afff;}

</style>
<script type="text/javascript">
	
</script>
</head>
<body style="background:#000; color:#fff;">
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
<div align="center">
	</div>
	<div>
		<table id="table1" align="center" border="1px" style="font-size: 13px;" width="800" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>AliExpress橱窗图片</th>
				

			</Tr>
			
				<Tr class="a" >
				<c:forEach items="${gbbs }" var="gbb" varStatus="i">
					<td> 
						<img  width='140px;' title="" height='140px;' src="${gbb.aliSourceImgUrl }" style='cursor: pointer;' >
					<!-- <br /><a target='_blank' href="https://s.1688.com/youyuan/index.htm?spm=a21bo.7925826.0.0.6ab4085cJJ0Lil&tab=imageSearch&from=plugin&imageType=https://img.alicdn.com&imageAddress=imgextra/i4/1840080575/TB2asnslcLJ8KJjy0FnXXcFDpXa_!!1840080575-0-beehive-scenes.jpg_300x300.jpg " class="a2" >info</a> -->
					<br /><a target='_blank' href="https://s.1688.com/youyuan/index.htm?spm=a21bo.7925826.0.0.6ab4085cJJ0Lil&tab=imageSearch&from=plugin&imageType=${gbb.imgpath }&imageAddress=${gbb.imgurl }" class="a2" >info</a>
					</td>
					</c:forEach>
				</Tr>
			
		</table>
		
	</div>
</div>
</body>
</html>