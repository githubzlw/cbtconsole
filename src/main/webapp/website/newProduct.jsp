<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="/cbtconsole/js/bootstrap/bootstrap.min.css">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="/cbtconsole/js/website/jquery.paginate.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="/cbtconsole/css/style.css">
<style type="text/css">
.categoryUl{
    width:1200px;
    position: relative;
    margin-bottom: 15px;
    display: inline-block;
    margin-left: 2px;
    margin: 0;
    padding: 0;
}
.categoryUl li{
   display: table-cell;
   float: left;
   margin-left: 20px;
}
.ulclass {
    position: relative;
    margin-bottom: 15px;
    display: inline-block;
    margin-left: 2px;
    margin: 0;
    padding: 0;
}

.ulclass li{
  width:220px; 
  margin-left:14px;
  margin-top:14px;
  list-style:none;
  display: table-cell;
  padding:0;  
  float: left;
}

.divclass{
    width: 220px;
    border: 1px solid #e3e3e3;
    background: #fff;
}
 
.title{
height:30px;
overflow: hidden;
text-overflow: ellipsis;
white-space: nowrap;
font-size: 1.1em;
}

.demo{
                width:580px;
                padding:10px;
                margin:10px auto;
                border: 1px solid #fff;
                background-color:#f7f7f7;
            }
.grey_bj{ position:fixed;width:100%;height:100%;background:	#E0E0E0;opacity:0.6;z-index: 1;display:none;}
.tank{width:450px; height:500px; padding-bottom:20px;background-color:#ffffff;position:fixed;top:15%;left:40%;border:2px solid #EEEEE0;border-radius:10px;-webkit-border-radius:10px;z-index: 2;display:none;}
.thank_top{width:96%;border-bottom:1px  dashed #ccc;margin:auto;text-align:right;padding-bottom: 10px;display:none;}
.close_1{color:#f2350c;font-size: 20px;font-weight: bold;text-decoration: none;padding-right: 3px;}
.redNetInfo{width:400px;height:400px;}
.redNetInfo td{
  text-align:center;
}
</style>
<script type="text/javascript">
$(function(){
      $.ajax({
    	  type:"post",
    	  url:"${ctx}/newProductController/getAllCategory",
    	  dataType:"json",
    	  success:function(res){
    		  for(var i=0;i<res.length;i++){
    			  $("#category").append("<option value="+res[i].cid+">"+res[i].category+"</option>")
    		  }
    	  }
      })
	
	 $(".close_1").click(function() {
			$("#bg").css("display","none");
			$("#share").css("display","none");
			$(".tanchu").css("display","none");
			$(".thank_top").css("display","none");
    });
      
      $("#reset").click(function(){
    	  $("input[name='url']").val('');
      })
	 
	 //add 
	 $("#submit").click(function(){
		 var url= "";
		 $("input[name='url']").each(function(){
			  if($(this).val()!=""){
				  url += $(this).val() +"##";
			  }
		 })
		var  createtime = $("#createtime").val();
		var category = $("#category").find("option:selected").text();
		var cid = $("#category").find("option:selected").val();
		if(cid==0){
			alert("请选择所属类目!");
		}else{
			if(url==""){
				 alert("请至少填一个url链接信息 ");
			 }else{
				 $.ajax({
					 type:"post",
					 url:"${ctx}/newProductController/pLAddNewProduct",
					 data:{createtime:createtime,category:category,cid:cid,url:url},
					 dataType:"json",
					 success:function(res){
						 if(res>0){
							 alert("新增成功!");
							 Search();
						 }
					 }
				 })
			 }
		}
		 
	 })
})

//add
function addOnePiece(){
	var createtime = $("#createtime").val();
	var category = $("#category").find("option:selected").text();
	if(category=="请选择"||createtime==""){
		alert("请选择相应的类别和日期!!");
	}else{
		var cid = $("#category").find("option:selected").val();
		var url = $("#url").val();
	    if(url==""){
	    	alert("新品url不为空!");
	    }else{
	    	$.ajax({
	    		type:'post',
	    		url:'${ctx}/newProductController/addNewProductInfo',
	    		data:{createtime:createtime,category:category,cid:cid,url:url},
	    		dataType:'json',
	    		success:function(res){
	    			if(res==2){
	    				alert("该url已存在! ");
	    			} else  if(res==1){
	    				Search();
	    			}else{
	    				alert("该url插入异常!");
	    			}
	    		}
	    	})
	    }
	}
}
		function Search() {
			var createtime = $("#createtime").val();
			if(createtime==""){
				alert("日期不能为空!") 
			}else{
			var cid = $("#category").find("option:selected").val();
			window.location.href="${ctx}/newProductController/showProductByCategory?createtime="+createtime+"&cid="+cid;
			}
		}

		//根据category 查询相应数据
		function  searchInfoByCategory(cid){
			var  createtime = $("#createtime").val();
			window.location.href="${ctx}/newProductController/showProductByCategory?createtime="+createtime+"&cid="+cid;
		}
		
		function resetCondition(){
			$("#url").val("");
			$("#createtime").val("");
			$("#category").val("0");
		}
		
		
		//***显示批量添加弹窗
		function showAddPage(){
			$("#bg").css("display","block");
			$("#share").css("display","block");
			$(".tanchu").css("display","block");
			$(".thank_top").css("display","block");
		}
		
		
		function  pdown(pid){
			if(confirm("确定下架该商品吗?")){
			 $.ajax({
				 type:"post",
				 url:"${ctx}/newProductController/down",
				 data:{pid:pid},
				 dataType:"json",
				 success:function(res){
					 if(res>0){
						 alert("下架成功!");
						 Search();
					 }else{
						 alert("下架失败!");
					 }
				 }
			 })
			}
		}
				
</script>
<title>新品区后台</title>
</head>
<body>
<div class="grey_bj" id="bg"></div>
<div class="tank"  id="share">
<div class="thank_top"><a href="javascript:void(0)"  class="close_1">X</a></div>
<div>
<input  type="hidden"  name="id"  value="" />
<table class="redNetInfo">
<tr>
    <td>新品URL:</td>
    <td><input  type="text"  class="form-control"  name="url" ></td>
</tr>
<tr>
    <td>新品URL2:</td>
    <td><input  type="text"  class="form-control"  name="url" ></td>
</tr>
<tr>
    <td>新品URL3:</td>
    <td><input  type="text"  class="form-control"   name="url" ></td>
</tr>
<tr>
    <td>新品URL4:</td>
    <td><input  type="text"  class="form-control"  name="url" ></td>
</tr>
<tr>
    <td>新品URL5:</td>
    <td><input  type="text"   class="form-control"  name="url" ></td>
</tr>
<tr>
    <td>新品URL6:</td>
    <td><input  type="text"  class="form-control"   name="url" ></td>
</tr>
<tr>
    <td><input type="button"  id="submit"   value="确定"></td>
    <td><input type="button"  id="reset"   value="重置"></td>
</tr>
</table>
</div>
</div>



<h2 align="center">新品区后台</h2>
    <div  style="width:100%;margin-left:200px;">
	<form class="form-inline" role="form">
		<div class="form-group">
				<div class="input-group">
					<div class="input-group-addon">上架时间:</div>
					<input id="createtime"   value="${createtime }" readonly="readonly" onfocus="WdatePicker({isShowWeek:true})" /></td>
				</div>
		</div>
		<p>
		<div class="form-group">
			<div class="input-group">
				<div class="input-group-addon">分&nbsp;&nbsp;类:</div>
				<select id="category" class="form-control">
					<option value="0">请选择</option>
				</select>
			</div>
		</div>
		<p>
		<div class="form-group">
			<div class="input-group">
				<div class="input-group-addon">产品链接:</div>
				<input id='url' class="form-control" type="text"  style="width: 255px">
			</div>
		</div>
		<button type="button" class="btn btn-warning" onclick="addOnePiece()">添加</button>
		<button type="button" class="btn btn-warning" onclick="resetCondition()">重置</button>
		<button type="button" class="btn btn-primary" onclick="showAddPage()">批量添加</button>
		<button type="button" class="btn btn-primary" onclick="Search()">查询</button>
	</form>
	</div>
	<div  id="newProduct" style="width:100%;margin-left:200px;">
	   <div id="categoryList"  style="width:65%;height:auto;background-color:#F0F0F0;">
	      <ul class="categoryUl">
		      <c:forEach  var="data"  items="${categoryList}">
		         <li><a href="#"  onclick="searchInfoByCategory(${data.cid })" >${data.category }<c:if test="${data.count>0 }">(${data.count})</c:if></a> </li>
		      </c:forEach>
	      </ul>
	   </div>
	 <!--   <div style="width:100%;margin-top:20px;">
	       <button type="button" class="btn btn-primary" onclick="">批量下架</button>
	   </div> -->
	   <div style="width:1200px;">
	     <ul class="ulclass"  style="width:1200px;margin-top:20px;">
	         <c:forEach  var="newproduct"  items="${dataList}">
		         <li>
		            <div class="divclass">
		            <div style="width:220px;">
 		               <a href="${newproduct.goods_url }"  target="_blank">
         		           <img  src="${newproduct.goods_img }"  style="width:220px;height:220px;">
		               </a>
		            </div>
		            <div>
		               <div class="title">${newproduct.goods_name}</div>
		               <div><font color="red">${newproduct.goods_price } / ${newproduct.goods_price_unit }</font></div>
		               <div>
		                  <span>sold: ${newproduct.goods_sold }</span>
		                  <span>MOQ: ${newproduct.goods_morder }</span>
		                </div>
		               <div>
		                 <c:if test="${newproduct.goods_free ==1 }">
		                   <span style="font-size: 12px; color: #7ab602;">Free Shipping</span>
		                 </c:if>
		               </div>
		               <div>
		                   <button  onclick="pdown(${newproduct.goods_pid})"  >下架</button>
		               </div>
		            </div>
		            </div>
		         </li>
		      </c:forEach>
	     </ul>
	   </div>
	</div>
</body>
</html>