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
<!-- 付款调查 -->                              
			<div align="left" style="width: 600px">
			<ul>                           
				<li><h3>After Received:(<span>${totalNumber}</span>)</h3></li>                 
				<li><h4>1.What do you like most in import-express?</h4></li>
				<li>Low Price<span id="ALow">(0,0%)</span></li>
				<li>Combine Shipping<span id="ACombine">(0,0%)</span></li>
				<li>Business Friendly<span id="ABusiness">(0,0%)</span></li>
				<li>Other<span id="AOther">(0,0%)</span></li>
			</ul>         
			<ul>         
				<li><h4>2.How do you feel with the shipping methods we provide?</h4></li>
				<li>Too complex, don't get it<span id="BToo">(0,0%)</span></li>
				<li>Easy to understand<span id="BEasy">(0,0%)</span></li>
			</ul>
			<ul>                       
				<li><h4>3.Who are you?</h4></li>
				<li>Individual Consumer<span id="CIndividual">(0,0%)</span></li>            
				<li>Wholesaler<span id="CWholesaler">(0,0%)</span></li>
				<li>Retailer<span id="CRetailer">(0,0%)</span></li>
			</ul>
			<ul>                              
				<li><h4>4.Please give your suggestion, it's very precious to us:</h4></li>
				                     
				
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
					if(id.indexOf(" ") != -1){
						id = id.substring(0,id.indexOf(" "));           
					}
					if($("#${problem.title}"+id).size()>0){
						$("#${problem.title}"+id).html(v);
					}                     
					//$("#d_id").html($("#d_id").html()+"#${problem.title}----${problem.wt}----"+id+"<br/>");            
				</script>                           
			</c:forEach>                                                                          
			<!-- 收货调查 -->
		</div>
</body>
</html>