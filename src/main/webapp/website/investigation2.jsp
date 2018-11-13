<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
</head>
<body>
	<!-- 收货调查 -->                              
			<div align="left" style="width: 600px">
			<ul>                  
				<li><h3>After Shopping:(<span>${totalNumber}</span>)</h3></li>                 
				<li><h4>1.Dose the product fit description?</h4></li>
				<li>Fit<span id="Fit">(0,0%)</span></li>
				<li>So-so<span id="So-so">(0,0%)</span></li>
				<li>Doesn't fit<span id="Doesn">(0,0%)</span></li>
			</ul>                    
			<ul>         
				<li><h4>What do you think we need to improvement?</h4></li>
				<li>Provide a richer variety of products<span id="Bparvop">(0,0%)</span></li>
				<li>Provide more cheaper products<span id="Bpmcp">(0,0%)</span></li>
				<li>Provide more convenient payment flow<span id="Bpmcpf">(0,0%)</span></li>
				<li>Optimize commodity classification and searching<span id="Boccas">(0,0%)</span></li>
				<li>Other<span id="Bo">(0,0%)</span></li>        
			</ul>
			<ul>                              
				<li><h4>3.Your evaluation of goods:</h4></li>
				                     
				
				<c:forEach items="${listStr }" var="str">
					<li>${str }</li>
				</c:forEach> 
			</ul>        
			<!-- 百分比信息统计 -->
			<c:forEach items="${allProblem }" var="problem">
				<script>
					var percentage = Number('${problem.cnt}') / Number('${totalNumber}');
					percentage = Number(percentage.toFixed(2))*100;
					var v = " (${problem.cnt},"+percentage.toFixed(2)+"%)";
					var id = "${problem.wt}";
					
					var title = '${problem.title}';
					if(title == "A"){
						id = id.replace(/[ ]/g,"");
						if(id=="Doesn'tfit"){
							id= "Doesn";
						}
						
					}else{
						id = title;                 
					}            
					
					if($("#"+id).size()>0){             
						$("#"+id).html(v);
					}
					//$("#d_id").html($("#d_id").html()+"#${problem.title}----${problem.wt}----"+id+"<br/>");   
					             
				</script>                           
			</c:forEach>         
			
			<c:forEach items="${allProblem2 }" var="problem">
				<script>
					var percentage = Number('${problem.cnt}') / Number('${totalNumber}');
					percentage = Number(percentage.toFixed(2))*100;
					var v = " (${problem.cnt},"+percentage.toFixed(2)+"%)";
					var id = "${problem.wt}";
					
					var title = '${problem.title}';
					id = title;
			//		$("#d_id").html($("#d_id").html()+"#${problem.title}----${problem.wt}----"+id+"<br/>");      
					if($("#"+id).size()>0){
						$("#"+id).html(v);
					}              
				</script>                                          
			</c:forEach>        
			                         
			                                                                           
			<!-- 收货调查 -->
		</div>
</body>
</html>