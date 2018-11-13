//<script type="text/javascript" src="${path}/js/warehousejs/tbGoodsSample.js"></script>
$(document).ready(function(){  //加载完成
	getTbGoodsSample('')  //样品表格数据加载
	getAliCategory();
	
})

//添加商品包
function insertTbGoodsSample(){
	
	var cid = $("#cid").val();//网站来源
	var category = $("#category").val();//样品类型名字
	var title = $("#title").val();//样品标题
	var viewimg = $("#viewimg").val();//样品图片
	$.ajax({
		type:"post", 
		url:"insertTbGoodsSample.do",
		data:{cid:cid,category:category,title:title,viewimg:viewimg}, 
		dataType:"text",
		success : function(data){  
			if(data=="1000"){
				getTbGoodsSample(''); 
				alert("添加商品成功");
			}else{
				//失败
				alert("失败");
			}
		}
	});
}

//清空样品
function emptyAllText(){
	$("#d_sampleInfo input[type='text']").val("");
}

//清空样品
function emptyDsdAllText(){
	$("#d_gsdInfo input[type='text']").val("");
}

//计算活动商品折扣
function JsDisount(){
	 var goodsprice= $("#gsd_goodsprice").val();
	 var originalprice= $("#gsd_originalprice").val();
	 $("#discount").val(((1-goodsprice/originalprice)*100).toFixed(1) + "%");
	
}

//读取商品根据id
function getGoodsDataById(){
	var val = $("#idOrUrl").val();
	$.ajax({
		type:"post", 
		url:"getGoodsDataById.do",
		data:{id:val,url:val}, 
		dataType:"text",
		success : function(data){ 
			var obj = eval("("+data+")");
			$("#gsd_goodsid").val(obj.goodsid);
			$("#gsd_goodsname").val(obj.name);
			$("#gsd_goodsurl").val(obj.goodsurl);
			var img = obj.img;
			$("#gsd_goodsimg").val(img);
			var sPrice = obj.factory_price + "";
			if(sPrice.indexOf(" ")!=-1){
				sPrice = sPrice.substring(0,sPrice.indexOf(" "));
			}
		
		   var goodsprice= $("#gsd_goodsprice").val();
		   var originalprice= $("#gsd_originalprice").val();
		   $("#gsd_flag").val("1");
			$("#gsd_type").val(obj.type);
			if(obj.weight.indexOf("kg")!=-1){
			
				obj.weight = obj.weight.substring(0,obj.weight.indexOf("kg"));
			}
			$("#gsd_weight").val(obj.weight);
			
			
		}
	});
}

//样品数据表格
function getTbGoodsSample(title){
	
	$.ajax({   
		type:"post", 
		url:"getTbGoodsSample.do",
		data:{title:title}, 
		dataType:"text",
		success : function(data){  
			var t= false;
			var objlist = eval("("+data+")");
			$("#table_id").html("");
			var newRow ="";
			newRow = "<tr> "+
			"<td width='169px'>" +
				"样品名称"+
			"</td>"+
			"</tr>";
			$('#table_id').append(newRow); 
			for(var i=0; i<objlist.length; i++){
				newRow = "<tr> "+
								"<td id='userid"+objlist[i].id+"'>" +
								"<a id='order_no"+i+"' href='javascript:void(0);' onclick=\"getTbGoodsSampleById('"+objlist[i].id+"')\" >"+objlist[i].title+"</a>"+
								"</td>"+
							"</tr>";
				 $('#table_id').append(newRow);  
				 t = true;
			}
			//有值 查询第一个
			if(t){
				getTbGoodsSampleById(objlist[0].id);
				
			}
			
		
		}
	});
}


//样品所有商品数据表格
function getTbGoodsSampleDetails(goodssampleid){
	$("#h_sample_id").val(goodssampleid);// 保存样品id  用来删除商品之后 在刷新 样品对应所有商品
	$.ajax({   
		type:"post", 
		url:"getTbGoodsSampleDetails.do",
		data:{goodssampleid:goodssampleid}, 
		dataType:"text",
		success : function(data){  
			var objlist = eval("("+data+")");
			$("#t_GoodsSampleDetails").html("");
			var newRow ="";
			newRow = "<tr> ";
			for(var i=0; i<objlist.length; i++){
//				商品图片<br/>
//				商品规格<br/>
//				商品价格<br/> 
//				商品折扣<br/> 
				newRow +=
								"<td id='userid"+objlist[i].id+"'>" +
								"<img onclick=\"getTbGoodsSampleDetailsById('"+objlist[i].id+"')\" width='150px' height='150px' src='"+objlist[i].goodsimg+"'/><br/>"+
								""+objlist[i].goodsname+"<br/>"+
								""+objlist[i].goodsprice+"<br/>"+
								""+objlist[i].discount+"<br/>"+
								"<input onclick=\"delteCommodityByid('"+objlist[i].id+"')\" type='button' value='删除'/>"+
								"</td>";
				if((i+1)%3 == 0){
					$('#t_GoodsSampleDetails').append(newRow+"</tr>");  
					newRow = "<tr> ";
				}
			}
			newRow += "</tr>";
			 
			 $('#t_GoodsSampleDetails').append(newRow);  
		}
	});
}
//删除单个商品根据id
function delteCommodityByid(id){
//	alert(id);
	$.ajax({
		type:"post", 
		url:"delteCommodityByid.do",
		data:{id:id}, 
		dataType:"text",
		success : function(data){ 
			
			if(data == "1000"){
				getTbGoodsSampleDetails($("#h_sample_id").val()) //刷新商品
			}else{
				alert("失败")
			}
		}
	});
}

//插入单个商品根据id
function insertTbGoodsSampleDetails(){
	
	var goodssampleid = $("#h_sample_id").val();
	var goodsid = $("#gsd_goodsid").val();
	var goodsname = $("#gsd_goodsname").val();
	var goodsimg = $("#gsd_goodsimg").val();
	var goodsprice = $("#gsd_goodsprice").val();
	var originalprice = $("#gsd_originalprice").val();
	var discount=((1-goodsprice/originalprice)*100).toFixed(1)+"%";
	var goodsurl=$("#gsd_goodsurl").val();
	var flag = $("#gsd_flag").val();
	var avilibleStock=$("#avilibleStock").val();
	var sold=$("#sold").val();
	var type = $("#gsd_type").val();
	var weight = $("#gsd_weight").val();
	
//	alert(id);
	$.ajax({
		type:"post", 
		url:"insertTbGoodsSampleDetails.do",
		data:{flag:flag,originalprice:originalprice,weight:weight,type:type,goodssampleid:goodssampleid,goodsid:goodsid,goodsname:goodsname,goodsurl:goodsurl,
			goodsimg:goodsimg,goodsprice:goodsprice,discount:discount,avilibleStock:avilibleStock,sold:sold
			}, 
		dataType:"text",
		success : function(data){ 
			
			if(data == "1000"){
				alert("成功")
				getTbGoodsSampleDetails($("#h_sample_id").val());
			}else{
				alert("失败")
			}
		}
	});
}

//修改单个商品根据id
function updateTbGoodsSampleDetailsByid(){
	
	var id = $("#h_gsd_id").val();
	var goodsid = $("#gsd_goodsid").val();
	var goodsname = $("#gsd_goodsname").val();
	var goodsurl = $("#gsd_AliURL").val();
	var goodsimg = $("#gsd_goodsimg").val();
	var goodsprice = $("#gsd_goodsprice").val();
	var flag = $("#gsd_flag").val();
	var type = $("#gsd_type").val();
	var weight = $("#gsd_weight").val();
	var originalprice = $("#gsd_originalprice").val();
	var discount=$("#discount").val();
	var avilibleStock=$("#avilibleStock").val();
	var sold=$("#sold").val();
	
	$.ajax({
		type:"post", 
		url:"updateTbGoodsSampleDetailsByid.do",
		data:{flag:flag,originalprice:originalprice,weight:weight,type:type,id:id,goodsid:goodsid,goodsname:goodsname,goodsurl:goodsurl,
			goodsimg:goodsimg,goodsprice:goodsprice,discount:discount,avilibleStock:avilibleStock,sold:sold
			}, 
		dataType:"text",
		success : function(data){ 
			
			if(data == "1000"){
			//	alert("成功")
				getTbGoodsSampleDetails($("#h_sample_id").val());
			}else{
				alert("失败")
			}
		}
	});
}
//修改单个样品根据id
function updateTbGoodsSampleByid(){
	var cid = $("#cid").val();
	var category = $("#category").val();
	var title = $("#title").val();
	var viewimg = $("#viewimg").val();
	var discount = $("#discount").val();
	var ymx_discount = $("#ymx_discount").val();
	
	var discountprice = $("#discountprice").val();
	if(discountprice==''){
		discountprice=0;
	}
	if(ymx_discount==''){                 
		ymx_discount=0;
	}
	
	var minnum = $("#minnum").val();
	var defaultnum = $("#defaultnum").val();
	var remark = $("#remark").val();
//	alert(id);
	$.ajax({
		type:"post", 
		url:"updateTbGoodsSampleByid.do",
		data:{ymx_discount:ymx_discount,cid:cid,id:$("#h_sample_id").val(),category:category,title:title,viewimg:viewimg,
			discount:discount,discountprice:discountprice,
			minnum:minnum,defaultnum:defaultnum,remark:remark}, 
		dataType:"text",
		success : function(data){ 
			
			if(data == "1000"){
				alert("成功")
			//	getTbGoodsSampleDetails($("#h_sample_id").val()) //刷新商品
			}else{
				alert("失败")
			}
		}
	});
}


//单件商品信息
function getTbGoodsSampleDetailsById(id){
	//保存商品id  用来做修改
	$("#h_gsd_id").val(id);
	$.ajax({
		type:"post", 
		url:"getTbGoodsSampleDetailsById.do",
		data:{id:id}, 
		dataType:"text",
		success : function(data){  
			var objlist = eval("("+data+")");
			$("#gsd_goodssampleid").val(objlist.goodssampleid);
			$("#gsd_goodsid").val(objlist.goodsid);
			$("#gsd_goodsname").val(objlist.goodsname);
			$("#gsd_goodsurl").val(objlist.goodsurl);
			$("#gsd_goodsimg").val(objlist.goodsimg);
			$("#gsd_goodsprice").val(objlist.goodsprice);
			$("#discount").val(objlist.discount)
			$("#avilibleStock").val(objlist.avilibleStock);
			$("#sold").val(objlist.sold);
			$("#gsd_type").val(objlist.type);
			$("#gsd_weight").val(objlist.weight);
			$("#gsd_originalprice").val(objlist.originalprice);
			$("#gsd_flag").val(objlist.flag);
			
		}
	});
}

//删除样品
function deleteTbGoodsSample(){
	if(!confirm("确定要删除吗"))
	{        
		return;
	}
	$.ajax({
		type:"post", 
		url:"deleteTbGoodsSample.do",
		data:{id:$("#h_sample_id").val()},
		dataType:"text",
		success : function(data){  
			if(data == "1000"){
				getTbGoodsSample('') //刷新样品
			}else{
				alert("失败")
			}
		}
	});
}

//获得样品下一级类型
function getSubType(){
	var id = $('#sel').val();
	$("#category").val($('#sel').find("option:selected").text());
	$("#cid").val(id);
	$.ajax({
		type:"post", 
		url:"getSubType.do",
		data:{id:id},
		dataType:"text",
		success : function(data){  
			var objlist = eval("("+data+")");
			var sel="<br id='brsel'/><select id='sel2' onchange='getSubType2()'>";
			sel += "<option >请选择</option>";
			var i=0
			for(; i<objlist.length; i++){
				sel += "<option value='"+objlist[i].cid+"'>"+objlist[i].category+"</option>";
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

//获得样品下一级类型2
function getSubType2(){
	var id = $('#sel2').val();
	$("#category").val($('#sel2').find("option:selected").text());
	$("#cid").val(id);
}
//获得样品类型
function getAliCategory(){
	$.ajax({
		type:"post", 
		url:"getAliCategory.do",
		dataType:"text",
		success : function(data){  
			var objlist = eval("("+data+")");
			for(var i=0; i<objlist.length; i++){
				$("#sel").append("<option value='"+objlist[i].cid+"'>"+objlist[i].category+"</option>");
			}
		}
	});
}
//查询单个样品信息,以及样品的所有商品信息
function getTbGoodsSampleById(id){
	//清空样品
		$("#d_sampleInfo input[type='text']").val("");

		$("#d_gsdInfo input[type='text']").val("");

	$.ajax({
		type:"post", 
		url:"getTbGoodsSampleById.do",
		data:{id:id}, 
		dataType:"text",
		success : function(data){  
			var objlist = eval("("+data+")");
			$("#category").val(objlist.category);
			$("#title").val(objlist.title);
			getTbGoodsSampleDetails(id) //查询样品对应所有商品信息
		}
	});
}

//添加样品   已取消使用整体折扣
function addSample(){
	//alert("123");
	 if ( event && event.stopPropagation ) { 
	    event.stopPropagation(); 
	} 
	else if (window.event) { 
	    window.event.cancelBubble = true; 
	}
	//点击div之外任意地方隐藏 div
	$(document).click(function(){
	     var wx = window.event.clientX;
	     var wy = window.event.clientY;
	     var d_left = document.getElementById('t_sample_add').offsetLeft;
		 var d_top = document.getElementById('t_sample_add').offsetTop;
		 var d_width = document.getElementById('t_sample_add').clientWidth;
		 var d_height = document.getElementById('t_sample_add').clientHeight;
		 if(wx < d_left || wy < d_top || wx > (d_left + d_width) || wy > (d_top + d_height)){
		 	$('#t_sample_add').hide();
		 }
	});
	$('#t_sample_add').show();
}