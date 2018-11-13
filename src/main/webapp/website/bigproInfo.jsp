<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>大货区</title>
</head> 
<link rel="stylesheet" href="/cbtconsole/css/warehousejs/tbGoodsSample.css" type="text/css">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/warehousejs/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="/cbtconsole/css/table.css">
<script type="text/javascript">

$(function(){
	//加载 商品类别 
	$.ajax({
		type:"post", 
		url:"/cbtconsole/WebsiteServlet1?action=getAliCategory&className=BigGoodsAreaServlet",
		dataType:"json",
		success : function(data){  
			for(var i=0; i<data.length; i++){
				$("#sel").append("<option value='"+data[i].cid+"'>"+data[i].category+"</option>");
			}
		}
	});
	
	$("#clear").click(function(){
		$("input[type='text']").val("");
		$("#bigpro_img").attr("src","");
		$("#tipInfo").html("");
	})
})

function getSubType(){
	var id = $('#sel').val();
	$("#category").val($('#sel').find("option:selected").text());
	$("#cid").val(id);
	$.ajax({
		type:"post", 
		url:"/cbtconsole/WebsiteServlet1?action=getSubType&className=BigGoodsAreaServlet",
		data:{id:id},
		dataType:"json",
		success : function(data){  
			var sel="<br id='brsel'/><select id='sel2' onchange='getSubType2()'>";
			sel += "<option >请选择</option>";
			var i=0
			for(; i<data.length; i++){
				sel += "<option value='"+data[i].cid+"'>"+data[i].category+"&nbsp;&nbsp;("+data[i].num+")</option>";
			}
			sel += "</select>";
			      
			if(i>0){				
				$("#brsel").remove();			
				$("#sel2").remove();
				$('#sel').after(sel);
			}
		}
	});
}  
function getSubType2(){
	var id = $('#sel2').val();
	$("#category").val($('#sel2').find("option:selected").text());
	$("#cid").val(id);
	findGoodsByCategory('');
	 var category1  = $('#sel2').find("option:selected").text();
	 var cat = category1.indexOf("(");
	 var category = category1.substr(0,cat);
	$("#bigpro_category").val(category);
	
}

function soso(){
	$("#tipInfp").html("");
	//改变 商品所属类别下的 商品数量 
	 var category1  = $('#sel2').find("option:selected").text();
	 var cat = category1.indexOf("(");
	 var category = category1.substr(0,cat);
	var  id = $("#idOrUrl").val();
	$.post("/cbtconsole/WebsiteServlet1?action=findGoodsByPid&className=BigGoodsAreaServlet",{id:id},
			function(res){
				var parsedJson = JSON.parse(res);
					$("#bigpro_goodsid").val(parsedJson.goodsId);
					$("#bigpro_category").val(category);
					$("#bigpro_price").val(parsedJson.price);
					$("#bigpro_goodsurl").val(parsedJson.goodsurl);
					$("#bigpro_goodsname").val(parsedJson.title)
					$("#bigpro_img").attr("src",parsedJson.img);
					$("#bigpro_weight").val(parsedJson.weight);
					$("#bigpro_keyword").val(parsedJson.keyWord);
	});
}

function save(){
 	var pid = $("#idOrUrl").val();
	$.post("/cbtconsole/WebsiteServlet1?action=checkIsExistence&className=BigGoodsAreaServlet",{pid:pid},function(result){
		if(result=="true"){
			var id = $("#bigpro_goodsid").val();
			var catid =$('#sel2').find("option:selected").val(); 
		   /*  var  category =$('#sel2').find("option:selected").text();  */
			 //改变 商品所属类别下的 商品数量 
			 var category1  = $('#sel2').find("option:selected").text();
			 var cat = category1.indexOf("(");
			 var category = category1.substr(0,cat);
			 
			if(catid=="请选择"){
				alert("请选中 对应类别 ");
			}else{
			//var  catid = $("#catid").val();
			var  num = $("#bigpro_num").val();
			var  price = $("#bigpro_price").val();
			var  discount = $("#bigpro_discount").val();
			var  url = $("#bigpro_goodsurl").val();
			var title = $("#bigpro_goodsname").val();
			var  img = $("img").attr("src");
			var  weight = $("#bigpro_weight").val();
			var keyword = $("#bigpro_keyword").val();
			if(num==""||discount==""){
				$("#tipInfo").html("添加失败,请完整信息 ");
			}else{
		      $.post("/cbtconsole/WebsiteServlet1?action=save&className=BigGoodsAreaServlet",{id:id,category:category,num:num,price:price,discount:discount,url:url,title:title,img:img,weight:weight,catid:catid,keyword:keyword},function(res){
		    	  if(res=="true"){
		    		$("#tipInfo").html("添加成功");
		    		findGoodsByCategory('');
		    		changeNum();
		    	}else{
		    		$("#tipInfo").html("添加失败 ");
		    	}
		    })  	    
			}
			}
		}else{
			$("#tipInfo").html("该商品已经在大货区");
		}
	});
	  
}

function findGoodsByCategory(info){
	var catid =$('#sel').find("option:selected").val();
	var catid1 =$('#sel2').find("option:selected").val(); 
	if(catid=="请选择"||catid1=="请选择"){
		alert("请同时选中 对应类别 ");
	}else{
	$.post("/cbtconsole/WebsiteServlet1?action=findGoodsByCategoryId&className=BigGoodsAreaServlet",{catid:catid,catid1:catid1},function(res){
		var objlist = eval(res);
		var size = objlist.length;
		$("#size").html(size);
		$("#t_GoodsSampleDetails").html("");
		var newRow ="";
		newRow = "<tr> ";
		for(var i=0; i<objlist.length; i++){
//			商品图片<br/>
//			商品规格<br/>
//			商品价格<br/> 
			newRow +=
							"<td id='userid"+objlist[i].id+"'>" +
							"<img onclick=\"getTbGoodsSampleDetailsById('"+objlist[i].id+"')\" width='150px' height='150px' src='"+objlist[i].img+"'/><br/>"+
							""+objlist[i].title+"<br/>"+
							""+objlist[i].num+"Units(each $"+objlist[i].price+")<br/>"+
							"<input onclick=\"delteCommodityByid('"+objlist[i].id+"')\" type='button' value='下架 '/>"+
							"</td>";
			if((i+1)%3 == 0){
				$('#t_GoodsSampleDetails').append(newRow+"</tr>");  
				newRow = "<tr> ";
			}
		}
		newRow += "</tr>";
		 
		 $('#t_GoodsSampleDetails').append(newRow);  
		 
		 //改变 商品所属类别下的 商品数量 
		 var category1  = $('#sel2').find("option:selected").html();
		 var cat = category1.indexOf("(");
		 var newCategory = category1.substr(0,cat);
		 var category2 =newCategory+"("+size+")";
		 $('#sel2').find("option:selected").html(category2);
	})
	}
}

function clear1(){
	$("#sel").val("");
	$("#sel2").val("");
}


//下架

function  delteCommodityByid(id){
	var catid1 =$('#sel2').find("option:selected").val();
	$.ajax({
		type:"post", 
		url:"/cbtconsole/WebsiteServlet1?action=delteCommodityByid&className=BigGoodsAreaServlet",
		data:{id:id}, 
		dataType:"json",
		success : function(data){ 
			if(data){
				findGoodsByCategory(catid1);
			}else{
				alert("失败")
			}
		}
	});
}
</script>                     
    
<body style="background-color : #F4FFF4;">  
	<div align="center"><h1 align="center">大货区</h1></div> 
   
	<div class="maindiv">     
		
		<!-- left -->     
		<div class="leftdiv" style="">           
			<table id="table_id">   
			</table>   
		</div>
		
		<!-- rigth -->
		<div class="rigthdiv" style="align:center">
			<!-- 样品详细类容 -->
			<div style="width:1000px;height:250px;">  
				<!-- 样品详细 -->   
				<div id="d_sampleInfo" style="float:left;">
					<table>  
						<tr>  
							<td width="50px" colspan="2" align="center">产品类别
							<input type="hidden" id="h_sample_id"/>
							
							</td>
						</tr>      
						<tr style="background-color : #FFACB1;">  
							<td>产品所属类别</td>
							  
							<td>
							<select id="sel" onchange="getSubType()">
							<option>请选择</option>
							</select>
							<br/>
							<input type="hidden" id="category"/></td>
						</tr>
						<tr>  
							<td colspan="2" align="center">
							&nbsp;
							</td>
						</tr>
						<tr>  
							<td colspan="2" align="center">
							&nbsp;
							</td>
						</tr>
						<tr>  
							<td colspan="2" align="center">
							&nbsp;
							</td>
						</tr>
						<tr>  
							<td colspan="2" align="center">
							&nbsp;
							</td>
						</tr>
						<tr>  
							<td colspan="2" align="center">
							<input onclick="clear1()" type="button" value="清空"/>
							<input onclick="findGoodsByCategory('')" type="button" value="查看所属产品"/>
							</td>
							
						</tr>
					</table>    
				</div>         

				<!-- 商品详细 -->
				<div id="d_gsdInfo" style="float:left;" >  
					<table >  
						<tr>
							<td colspan="2" align="center">添加商品详细
							</td>
						</tr>  						      
						<tr style="background-color : #FFACB1;">   
							<td>产品id</td>  
							<td><input type="text" id="bigpro_goodsid"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>产品名字</td>  
							<td><input type="text" id="bigpro_goodsname"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>产品数量</td>  
							<td><input type="text" id="bigpro_num"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>产品单价</td>  
							<td><input type="text" id="bigpro_price"/></td>
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>折扣</td>  
							<td><input type="text" id="bigpro_discount" width="8px"/>(%) 
							</td>              
						</tr>
						<tr style="background-color : #FFACB1;">
							<td>所属类别</td>  
							<td><input type="text" id="bigpro_category" width="8px"/> 
							</td>              
						</tr>  
						<tr style="background-color : #FFACB1;">
							<td>商品链接</td>  
							<td><input type="text" id="bigpro_goodsurl"/></td>
						</tr>
						<tr style="display:none">
							<td><input type="text" id="bigpro_weight"/></td>
							<td><input type="text" id="bigpro_keyword"/></td>
						</tr>
						<tr>  
							<td colspan="2" align="center">
							<input onclick="save()" type="button" value="添加"/>
							<input id="clear" type="button" value="清空"/></td>
						</tr>
					</table> 
				</div>
				<div style="float:left;margin-left:10px;border :1px solid  #FFACB1;">
				   <img id="bigpro_img" src=""  width="200px" height="200px">
				</div>
				</div> 
				<div  id="tipInfo" ></div>
				<div>
					商品id或商品URL搜索:<input id="idOrUrl" type="text" /><input type="button" value="读取" onclick="soso()" />
				</div>
			  
			<br/><br/>
			<!-- 样品对应所有商品 -->
			<div style="align:center" >该类别下共有(<font id="size" color="red"></font>)个商品</div>
			<div style="position:absolute; height:500px; width:1200px; overflow:auto;">
				<table id="t_GoodsSampleDetails">  
					
				</table>
			</div>  
			
		</div>
	</div>
</body>
</html>