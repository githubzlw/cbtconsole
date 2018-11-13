<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
</style>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/report/datechoise.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" href="script/style.css" type="text/css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/AdminLTE.min.css">
<link rel="stylesheet" href="/cbtconsole/css/bootstrap/tablestyle.css">
<title>网站错误信息</title>
<style type="text/css">
table.altrowstable {
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #a9c6c9;
	border-collapse: collapse;
}

table.altrowstable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}

table.altrowstable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #a9c6c9;
}

.oddrowcolor {
	background-color: #F4F5FF;
}

.evenrowcolor {
	background-color: #FFF4F7;
}

/* 字体样式 */
body {
	font-family: arial, "Hiragino Sans GB", "Microsoft Yahei", sans-serif;
}

p.thicker {
	font-weight: 900
}
</style>
<script type="text/javascript">
		$(document).ready(function(){
			jQuery.ajax({
		        url:"/cbtconsole/StatisticalReport/getErrorFileInfo",
		        type:"post",
		        success:function(data){
		        	if(data){
		        		var reportDetailList=data.data.fileList;
		        		 for(var i=0;i<reportDetailList.length;i++){
		        			 htm_='';
		                 	htm_ = '<tr>                                               ';
		                 	htm_ += '<td align="center">'+         (i+1)                      +'</td>';
		                 	htm_ += '<td align="center">'+reportDetailList[i].fileName+'</td>';
		                 	htm_ += '<td align="center"><a href="../ShowFileServle?filename='+reportDetailList[i].fileName+'"  target="_blank">下载</a></td>';
		                 	htm_ += '</tr>                                             ';
		                	$('#categroyReport').append(htm_);
		        		 }
		        		}
		        	}});
		});
	 </script>
</head>
<body style="background-color: #F4FFF4;">
	<div align="center">
		<table id="categroyReport">
			<thead>
				<tr>
					<th width="50" style="text-align: center;">序号</th>
					<th width="180" style="text-align: center;">文件名</th>
					<th width="150" style="text-align: center;">操作</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
</body>
</html>