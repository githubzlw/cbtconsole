<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>物流反馈信息</title>
<link rel="stylesheet" href="/cbtconsole/js/bootstrap/bootstrap.min.css">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript"
	src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
</head>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String date = sdf.format(new Date());
%>
<script type="text/javascript">
/*    $(function(){
	   $.ajax({
		   type:"post",
		   url:"/cbtconsole/trackingController/getadmInfo",
		   dataType:"json",
		   success:function(res){
			   var row="<option value='0'>全部</option>";
			   for(var i=0;i<res.length;i++){
				   row+="<option value="+res[i]+">"+res[i]+"</option>";
			   }
			   $("#admName").append(row);
		   }
	   })
   }) */



  function spider(){
	  var company = $("#company").val();
	  if(company=="JCEX"){
		  $("#result").html("JCEX 物流信息比较多 可能需要几分钟 ..请耐心等待..") ;
	  }else{
		  $("#result").html("物流信息正在抓取分析需等待点时间 .请耐心..") ;
	  }
	  var senttimeBegin = $("#senttimeBegin").val();
	  var senttimeEnd = $("#senttimeEnd").val();
	  $.ajax({
		  type:"post",
		  url:"/cbtconsole/trackingController/trackJcexInfo",
		  data:{company:company,senttimeBegin:senttimeBegin,senttimeEnd:senttimeEnd},
		  dataType:'json',
		  success:function(res){
			  $("#result").html("");
			  $("#trankList tbody").html("");
			  var tabStr ="";
			  if(res=="false"){
				  tabStr+=("<tr style='text-align:center; '><td colspan='7'><font color='red'>请选择开始时间</font></td></tr>")
				  $("#trankList tbody").append(tabStr);
			  }else{
				  tabStr+=("<tr style='text-align:center; '><td colspan='7'><font color='red'>"+res+" </font></td></tr>")
				  $("#trankList tbody").append(tabStr);
			  }
		  }
	  })
  }
  
  
  function select(){
	  var company = $("#company").val();
	  var admName = $("#admName").val();
	  $.ajax({
		  type:'post',
		  url:"/cbtconsole/trackingController/select",
		  data:{company:company,admName:admName},
		  dataType:'json',
		  success:function(res){
			  $("#trankList tbody").html("");
			  var tabStr ="";
			  if(res=="undown"){
				  tabStr+=("<tr style='text-align:center; '><td colspan='7'><font color='red'>物流跟踪数据抓取还没完成 ,稍后查看 </font></td></tr>")
				  $("#trankList tbody").append(tabStr);
			  }else if(res=="noproblem"){
				  tabStr+=("<tr style='text-align:center; '><td colspan='7'><font color='red'>物流跟踪数正常 </font></td></tr>")
				  $("#trankList tbody").append(tabStr);
			  }else{
				  for (var i = 0; i < res.length; i++) {
					  var j =i+1;
					  var remarks =res[i].remarks;
					  tabStr+=("<tr>");
					  tabStr+=("<td>"+j+"</td>");
					  tabStr+=("<td><a href='/cbtconsole/trackingController/trackinfoByExpressnoAndCompany?expressno="+res[i].expressno+"&company="+res[i].company+"'  target='_blank'>"+res[i].expressno+"</a></td>");
					  tabStr+=("<td>");
					  if(remarks.length>20&&remarks.indexOf("O")>-1){
						  var attr = remarks.substring(0,remarks.lastIndexOf(","));
						  var attrs = attr.split(",");
						  for(var j=0;j<attrs.length;j++){
							  tabStr+=("<a href='/cbtconsole/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo="+attrs[j]+"'  target='_blank'>"+attrs[j]+"</a><br>");
						  }
					  }else{
						  if(remarks.indexOf(",")>0){
							  remarks = remarks.substring(0,remarks.lastIndexOf(","));
						  }
						  tabStr+=("<a href='/cbtconsole/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo="+remarks+"' target ='_blank'>"+remarks+"</a>");
					  }
					  tabStr+=("</td>");
					  tabStr+=("<td>"+res[i].admName+"</td>");
					  tabStr+=("<td>"+res[i].company+"</td>");
					  tabStr+=("<td>"+res[i].createtime+"</td>");
					  if(res[i].flag==1){
						  tabStr+=("<td>运单超过十天还没签收</td>");
					  }
					  if(res[i].flag==2){
						  tabStr+=("<td>运单超过三天没有更新状态</td>");
					  }
					  if(res[i].bz==null){
						  tabStr+=("<td><input type='text' class='bz'>");
					  }else{
						  tabStr+=("<td><input type='text' class='bz' value="+res[i].bz+">");
					  }
					  tabStr+=("<input type='hidden'  class='trackid' value="+res[i].id+"><input type='hidden'  class='expressno' value="+res[i].expressno+"><input type='submit'  value='submit'  class='insertBz'></td>");
					  tabStr+=("</tr>");
				  }
				   $("#trankList tbody").append(tabStr);
			  }
			  }
	  })
  }
  
  $(function(){
	   $("#trankList tbody").on("click",".insertBz",function(){
		   var bz = $(this).siblings(".bz").val();
		   var id = $(this).siblings(".trackid").val();
		   $.ajax({
			   type:'post',
			   data:{bz:bz,id:id},
			   url:"/cbtconsole/trackingController/saveBzByExpressnoAndCompany",
			   dataType:'json',
			   success:function(res){
				   if(res=="success"){
					   alert("添加成功!")
				   }else{
					   alert("添加失败!")
				   }
			   }
		   })
		   
	   })
  })
  
  
  function respider(){
	  var company = $("#company").val();
	  if(company==null){
		  alert("请选中运输方式 ！");
	  }
	  $.ajax({
		  type:'post',
		  url:"/cbtconsole/trackingController/respider",
		  data:{company:company},
		  dataType:"json",
		  success:function(res){
			  if(res=="true"){
				  alert("更新 成功！")
			  }else{
				  alert("运单状态还没处理 ！")
			  }
		  }
	  })
  }
   
</script>
<body>
<h2 align="center">物流反馈信息</h2>
<div id="result" class="result" align="center" style="color: Red;font-size: 30px;font-weight: bold;"></div>
   <div>
        <form class="form-inline" role="form">
		<div class="form-group">
			<div class="input-group">
				<div class="input-group-addon">运输公司</div>
				<select id="company" class="form-control">
					<option value="0">请选择</option>
					<option value="JCEX">JCEX</option>
					<option value="emsinten">邮政</option>
					<option value="原飞航">原飞航</option>
					<option value="shunfeng">顺丰</option>
				</select>
				<div class="input-group-addon">销售负责人</div>
				<select id="admName" class="form-control">
				    <option value="all">全部</option>
				    <option value="Roxie">Roxie</option>
				    <option value="yin">yin</option>
				    <option value="Andy">Andy</option>
				    <option value="elise">elise</option>
				    <option value="Andrew">Andrew</option>
				    <option value="Linda">Linda</option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<div class="input-group">
				<div class="input-group-addon">开始时间</div>
				<input id='senttimeBegin' class="form-control" type="date" value="<%=date%>" style="width: 155px">
				<div class="input-group-addon">~</div>
				<input id='senttimeEnd' class="form-control" type="date" value="" style="width: 155px">
			</div>
		</div>
	    <button type="button" class="btn btn-warning" onclick="select()">查询</button>&nbsp;&nbsp;
		<button type="button" class="btn btn-primary" onclick="spider()">抓取分析</button>&nbsp;&nbsp;
		<button type="button" class="btn btn-danger" onclick="respider()">更新</button>
		</form>
      <table width="1000px" id="trankList"  class="table table-bordered table-condensed table-striped">
         <thead>
            <tr>
                <td>序号</td>
                <td>运单号</td>
                <td>订单号</td>
                <td>负责销售</td>
                <td>运输方式</td>
                <td>出库时间</td>
                <td>状态</td>
                <td>备注</td>
            </tr>
         </thead>
         <tbody></tbody>
         <%-- <c:forEach var="trank"  items="${list}" varStatus="index">
             <tr>
                <td>${index.index+1}</td>
                <td>${trank.expressno }</td>
                <c:if test="${trank.flag==1 }">
                  <td><font color="red">运单超过十天还没签收</font></td>
                </c:if>
                <c:if test="${trank.flag==2 }">
                  <td><font color="red">运单超过三天没有更新状态</font></td>
                </c:if>
            </tr>
         </c:forEach> --%>
      </table>
   </div>
</body>
</html>