<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<title>类别折扣上限</title>
</head>
<script type="text/javascript">
function submits(){
	var cid=$("#cid").val();
	var category=$("#category").val();
	/* if(cid.trim()==""&&category.trim()==""){
		alert("请输入至少一个条件!");
		return;
	}else{	 */
	    $("#queryForm").submit();
		//-- $.post("/cbtconsole/cbt/orderws/paymentConfirm",{orderNo: '${param.orderNo}',userId: '${userId}',pass :userpass, paytypeid:paytypeflag},
    	
	//} 
}
function formReset(){
	$("#cid").val("");
	$("#category").val("");
}
//回车事件
$(document).keydown(function(event){ 
	if(event.keyCode == 13){ //绑定回车 
		 submits() 
	} 
}); 

function fnUpdateMaxDiscount(cid,index){
	var category = $('input[name ="category'+index+'"]').val();
	var maxDiscount = $('input[name ="maxDiscount'+index+'"]').val();
	//alert(cid+":"+category+":"+maxDiscount);
	if(maxDiscount.length > 0||maxDiscount !=""){
	 	 $.ajax({
		  	  type:"post",
			  url: "/cbtconsole/discountType/updateDiscountTypeByCid",
			  cache: false,
			  data:{"cid":cid,"category":category,"maxDiscount":maxDiscount},
			  success: function(data){
				 // $.Alert("OK");
				  alert("修改成功!");
			  },
			  error: function(data){
				  alert("修改失败,请刷新重新操作!");
			  }
		});  
	}else{
		alert(cid+"项您还没有输入折扣率,请输入折扣率 再点击保存!");
	}
}



</script>
 <style>
        table tr{height: 32px;line-height: 32px;border-bottom:1px solid red;}
 </style>
<body  style="text-align:center; position: absolute;  margin-left: 30%; margin-top: 2%; ">
	<div>
		<div><h1>类别折扣上限</h1></div>
	<form  id="queryForm" name="FRM"   method="post" action="/cbtconsole/discountType/queryForAliCategory"><!--  method="post" -->
		<div>
			<span>类别ID:</span><input id="cid" name="cid" type="text" value="${cid}"/>
			<span>类别名:</span><input id="category" name="category" type="text" value="${category}" />
			<td colspan=4><input type="button" style="width: 150px; margin-left: 50px;"  onclick="submits();" value="查询" /></td>
			<td><input type="button" style="width: 90px; margin-left: 50px;"  onclick="formReset();" value="重置" /></td>
		</div><br>
		<div><span style="color: gray;">类别ID可精确查询，类别名可模糊查询，无条件查询所有类别</span></div>
		<hr>
		<div>
			<table  border="0" cellpadding="3" cellspacing="1" width="100%" align="center" style="background-color: #b9d8f3;">
				<thead>
					<tr>
						<th class="text-center" style="width :20%">ID</th>
						<th class="text-center" style="width :50%">类别</th>
						<th class="text-center" style="width :30%">最高折扣率</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${aliCategoryList != null && fn:length(aliCategoryList) > 0 }">
						<c:forEach items="${aliCategoryList}" var="aliCategory"  varStatus="st">
							    <tr index="${st.index}" style="text-align: center; /* COLOR: #0076C8; */ BACKGROUND-COLOR: #F4FAFF; font-weight: bold">
									<td>${aliCategory.cid }</td>
									<td>${aliCategory.category }</td>
									<td><input  style="width :80px;" type="text" onkeyup="this.value=this.value.replace(/[^0-9.]/g,'')" maxlength="5"  name ="maxDiscount${st.index}" value="${aliCategory.maxDiscount }" /><span>&nbsp;%&nbsp;</span>
										<input type="hidden" name ="category${st.index}" value="${aliCategory.category }" />
										<input type="button" onclick="fnUpdateMaxDiscount(${aliCategory.cid },${st.index})" value="保存" />
									</td>
								</tr>
						</c:forEach>
					</c:if> 
				</tbody>
			</table>
		</div>
	</form>
	</div>
</body>
</html>