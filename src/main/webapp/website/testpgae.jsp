<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript">
function getLogisticsInfo(){
	var nums=$("#nums").val();
	$.ajax({
		type:"post", 
		url:"/cbtconsole/warehouse/getWLHtml.do",
		dataType:"text",
		data:{nums:nums},
		success : function(data){  
			
		//	alert(data);     
			//var str = $('<div></div>').append(data).find('#jsResultList').html();
			//jsResultBlock result-block block-10
		//	var str = $('<div></div>').append(data).find("section").html();
		//	alert(str);
			$("#divid").html(data);    
		}
	});
}
</script>
</head>
<body>
<input type="text" value="" id="nums"/>
<input type="button" value="test" onclick="getLogisticsInfo()"/>
<div id='divid'></div>
</body>
</html>