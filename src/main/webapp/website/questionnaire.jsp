<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>问卷调查统计</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>

<style type="text/css">
span{color: red;}
div{border-width : 0px;
	border-style : solid;}
</style>

<script type="text/javascript">
$(function (){
	paymentSurvey();       
})
//付款调查
function paymentSurvey(){       
	$.ajax({
		type:"post",                
		url:"${pageContext.request.contextPath}/warehouse/questionnaireStatistics.do",
		dataType:"text",
		success : function(data){  
			if(data!='0'){
				$("#surveyContent").html(data);
			}else{
				$("#surveyContent").html("无");
			}      
		}
	});                                       
}
//收货调查
function receivingInvestigation(){       
	$.ajax({
		type:"post",                
		url:"${pageContext.request.contextPath}/warehouse/receivingInvestigation.do",
		dataType:"text",
		success : function(data){  
			if(data!='0'){
				$("#surveyContent").html(data);
			}else{
				$("#surveyContent").html("无");
			}      
		}
	});                                       
}

function distributionSurvey(type){
	if(type == '0'){
		receivingInvestigation();
	}else{
		paymentSurvey();
	}
}

</script>
</head>     
<body>                           
	<div align="center" >
		<div><h1>问卷调查统计</h1></div>       
		<div>
			<input type="radio" checked="checked" onclick="distributionSurvey(1)" name="paymentType">付款调查
			<input type="radio" onclick="distributionSurvey(0)" name="paymentType">收货调查
		</div>
		<div id="surveyContent">                               
			
		</div>
	</div>   
	
	<div id="d_id"></div>             
</body>         
				     
</html>