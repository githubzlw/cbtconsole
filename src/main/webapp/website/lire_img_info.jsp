<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>图片比较结果</title>
<style>
.a1{ color:#00afff;}

</style>
<script type="text/javascript">
	var check = function(){
		var uid = $("#selled").val();
		if(isNaN(uid)){
			$("#ts").html("请输入数字");
			return false;
		}else{return true;}
	};
	
 	function getCodeId1(value){
		$("#categoryId1").val(value);
		document.getElementById("categoryId1").value=value;
	}
	
 	function selectSimilarity(value){
 		$("#similarityId").val(value);
 	}
 	
	function getCodeId(value){
		window.location = "/cbtconsole/customerServlet?action=findAllPicture&className=PictureComparisonServlet&cid="+value;
	}

	//选中对标商品
	function findImgId(obj,obj1,obj2,obj3,obj4,obj5,obj6,obj7,obj8,obj9,obj10,obj11,tbPid,tburl,shopId,moqPrice,catId,priorityFlag,sourceProFlag,ylImg,selled){
		
 		var tb0 = $(obj).attr("id");
 		$("#"+tb0).css("border","6px solid red"); 
 		$("#"+obj1).css("border",""); 
 		$("#"+obj2).css("border",""); 
 		$("#"+obj3).css("border",""); 
 		$("#"+obj4).css("border",""); 
 		$("#"+obj5).css("border",""); 
 		$("#"+obj6).css("border",""); 
 		$("#"+obj7).css("border",""); 
 		$("#"+obj8).css("border",""); 
 		$("#"+obj9).css("border",""); 
 		$("#"+obj10).css("border",""); 
 		$("#"+obj11).css("border",""); 
 		
 
/*  		$.post("/cbtconsole/customerServlet",
	  			{action:'saveDbYlgooddata',className:'PictureComparisonServlet',goodId:tbPid,tbUrl:tburl,shopId:shopId,moqPrice:moqPrice,catId:catId,
 			priorityFlag:priorityFlag,sourceProFlag:sourceProFlag,ylImg:ylImg},
	  			function(res){ 
					if(res>0){
						alert("保存成功");
					}else{
						alert("保存失败");
					}  
	  	});  */
 		
 		
 		$.ajax({
			type : "post",
			datatype : "json",
			url : "/cbtconsole/customerServlet?action=saveDbYlgooddata&className=PictureComparisonServlet",
			data : {
				goodId:tbPid,tbUrl:tburl,shopId:shopId,moqPrice:moqPrice,catId:catId,
	 			priorityFlag:priorityFlag,sourceProFlag:sourceProFlag,ylImg:ylImg,selled:selled
			},
			success : function(res) {
				
			},
			error : function() {
				alert("保存失败，请重新登录。");
			}
       	})
       	
       	
	}
	
	//无对标
	function cancel(bench,appro,cancel,obj,obj1,obj2,obj3,obj4,obj5,obj6,obj7,obj8,obj9,obj10,obj11,tbPid,delFlag,sourceYlpid){
 		
 		$.post("/cbtconsole/customerServlet",
	  			{action:'updateYlFlag',className:'PictureComparisonServlet',goodId:tbPid,delFlag:delFlag,sourceYlpid:sourceYlpid},
	  			function(res){
	  				$("#"+cancel).css("background","blue");
	  				$("#"+obj).css("border",""); 
	  				$("#"+obj1).css("border",""); 
	  		 		$("#"+obj2).css("border",""); 
	  		 		$("#"+obj3).css("border",""); 
	  		 		$("#"+obj4).css("border",""); 
	  		 		$("#"+obj5).css("border",""); 
	  		 		$("#"+obj6).css("border",""); 
	  		 		$("#"+obj7).css("border",""); 
	  		 		$("#"+obj8).css("border",""); 
	  		 		$("#"+obj9).css("border",""); 
	  		 		$("#"+obj10).css("border",""); 
	  		 		$("#"+obj11).css("border",""); 
	  	}); 
	}
	
	//精准对标
	function bench(str,bench,appro,cancel,tbPid,delFlag,sourceYlpid){
 		
		var lotUnit = $(str).siblings("input[name=lotUnit]").val();
  		$.post("/cbtconsole/customerServlet",
	  			{action:'updateAliInfoFlag',className:'PictureComparisonServlet',goodId:tbPid,delFlag:delFlag,sourceYlpid:sourceYlpid,weight:"",lotUnit:lotUnit},
	  			function(res){
	  				$("#"+bench).css("background","red");
	  				$("#"+cancel).css("background","");
	  				$("#"+appro).css("background","");
	  	});
	}
	//近似对标
	function appro(str,bench,appro,cancel,tbPid,delFlag,sourceYlpid){
 		
		var weightVar = $(str).siblings("input[name=weightVar]").val();
		if(weightVar==""){
			alert("近似对标重量请输入");
			return;
		}
		
  		$.post("/cbtconsole/customerServlet",
	  			{action:'updateAliInfoFlag',className:'PictureComparisonServlet',goodId:tbPid,delFlag:delFlag,sourceYlpid:sourceYlpid,weight:weightVar,lotUnit:""},
	  			function(res){
	  				$("#"+appro).css("background","green");
	  				$("#"+cancel).css("background","");
	  				$("#"+bench).css("background","");
	  	});
	}
	//手工录入更新
	function updatePid(str,goodsPid,sourceYlpid,priorityFlag,sourceProFlag){
 		
		var ylUrl = $(str).siblings("input").val();
		if(ylUrl==""){
			alert("1688产品不能为空");
			return;
		}
		//alert(goodsPid);
 		$.post("/cbtconsole/customerServlet",
	  			{action:'updateDbYlbbPid',className:'PictureComparisonServlet',goodsPid:goodsPid,ylUrl:ylUrl,sourceYlpid:sourceYlpid,priorityFlag:priorityFlag,sourceProFlag:sourceProFlag},
	  			function(res){
	  				alert("成功");
	  	}); 
	}
	
	
	function fnSave(){

 		var ary=new Array();

		var s=$("#table1").find("tr").length;
		for(var i=1;i<s;i++){
			var trId=$("#table1 tr:eq("+i+")").attr("id");
			var pId=trId.substring("2");
			var tb1=$("#table1 tr:eq("+i+") td:eq(1)").find("img").attr("style");
			var tb2=$("#table1 tr:eq("+i+") td:eq(2)").find("img").attr("style");
			var tb3=$("#table1 tr:eq("+i+") td:eq(3)").find("img").attr("style");
			var tb4=$("#table1 tr:eq("+i+") td:eq(4)").find("img").attr("style");
			var tbBorder1 = tb1.split(":");
			var tbBorder2 = tb2.split(":");
			var tbBorder3 = tb3.split(":");
			var tbBorder4 = tb4.split(":");

			if(tbBorder1[2]=="undefined" || tbBorder1[2]=="" || tbBorder1[2]==null){
				
			}else{
				ary.push(pId+",tb1,"+tbBorder1[2]);
			}
			if(tbBorder2[2]=="undefined" || tbBorder2[2]=="" || tbBorder2[2]==null){
				
			}else{
				ary.push(pId+",tb2,"+tbBorder2[2]);
			}
			if(tbBorder3[2]=="undefined" || tbBorder3[2]=="" || tbBorder3[2]==null){
				
			}else{
				ary.push(pId+",tb3,"+tbBorder3[2]);
			}
			if(tbBorder4[2]=="undefined" || tbBorder4[2]=="" || tbBorder4[2]==null){
				
			}else{
				ary.push(pId+",tb4,"+tbBorder4[2]);
			}
	
		}
		
		for(var i=0;i<ary.length;i++){
			var pId=ary[i].split(',')[0];
			var tbId=ary[i].split(',')[1];
			
			$.post("/cbtconsole/customerServlet?action=pageSaveTbgooddata&className=PictureComparisonServlet",
					{pId:pId,tbId:tbId},
					function(res){
						if(res>0){
							//alert("保存成功");
						}
			});
		}
		alert("保存成功");
	}
	
	
	function fnLireSave(){

 		var ary=new Array();
 		var ary1=new Array();
		var s=$("#table1").find("tr").length;
		for(var i=1;i<s;i++){
			var trId=$("#table1 tr:eq("+i+")").attr("id");
			var pId=trId.substring("2");
			
			var pId1=$("#table1 tr:eq("+i+") td:eq(1)").find("span").attr("id");
			var pId2=$("#table1 tr:eq("+i+") td:eq(2)").find("span").attr("id");
			var pId3=$("#table1 tr:eq("+i+") td:eq(3)").find("span").attr("id");
			var pId4=$("#table1 tr:eq("+i+") td:eq(4)").find("span").attr("id");
			var pId5=$("#table1 tr:eq("+i+") td:eq(5)").find("span").attr("id");
			var pId6=$("#table1 tr:eq("+i+") td:eq(6)").find("span").attr("id");
			
			ary.push(pId);
			ary.push(pId1);
			ary.push(pId2);
			ary.push(pId3);
			ary.push(pId4);
			ary.push(pId5);
			ary.push(pId6);
			
			ary1.push(pId);
	
		}
		
		$.post("/cbtconsole/customerServlet?action=pageUpdateLireFlag&className=PictureComparisonServlet",
				{pId:ary.toString(),pidSor:ary1.toString()},
				function(res){
					if(res>0){
						//alert("保存成功");
					}
		});
		
/* 		for(var i=0;i<ary.length;i++){
			var pId=ary[i];
			
			$.post("/cbtconsole/customerServlet?action=pageUpdateLireFlag&className=PictureComparisonServlet",
					{pId:pId},
					function(res){
						if(res>0){
							//alert("保存成功");
						}
			});
		} */
		alert("保存成功");
	}
	
	
	function fnSelectInfo(){
		window.location = "/cbtconsole/customerServlet?action=findAllTaoBaoInfo&className=PictureComparisonServlet";
	}
	function fnWinSearch(goodsPid){
		window.open("/cbtconsole/PurchaseServlet?action=winSearch&className=Purchase&goodsPid="+goodsPid,"_blank")
	}
	
	//侵权商品
	function fnQinQ(goodsPid,flag){
		$.post("/cbtconsole/customerServlet",
	  			{action:'saveDbLiredata',className:'PictureComparisonServlet',goodsPid:goodsPid,flag:flag},
	  			function(res){
	  				/* $("#"+bench).css("background","red");
	  				$("#"+cancel).css("background","");
	  				$("#"+appro).css("background",""); */
	  				$("#"+goodsPid).html("侵权商品更新成功")
	  	});
	}
	//有中文
	function fnZhongW(goodsPid,flag){
		$.post("/cbtconsole/customerServlet",
	  			{action:'saveDbLiredata',className:'PictureComparisonServlet',goodsPid:goodsPid,flag:flag},
	  			function(res){
	  				$("#"+goodsPid).html("有中文更新成功")
	  	});
	}
	
	function fnTaiNK(goodsPid,flag){
		$.post("/cbtconsole/customerServlet",
	  			{action:'saveDbLiredata',className:'PictureComparisonServlet',goodsPid:goodsPid,flag:flag},
	  			function(res){
	  				$("#"+goodsPid).html("太难看更新成功")
	  	});
	}
	
	function fnJingP(goodsPid,flag){
		$.post("/cbtconsole/customerServlet",
	  			{action:'saveDbLiredata',className:'PictureComparisonServlet',goodsPid:goodsPid,flag:flag},
	  			function(res){
	  				$("#"+goodsPid).html("是精品更新成功")
	  	});
	}
	
	function fnXiaJ(goodsPid,flag){
		$.post("/cbtconsole/customerServlet",
	  			{action:'saveDbLiredata',className:'PictureComparisonServlet',goodsPid:goodsPid,flag:flag},
	  			function(res){
	  				$("#"+goodsPid).html("下架更新成功")
	  	});
	}
	
	function fnSame(pidSrc,pidSame,flag){
			$.post("/cbtconsole/customerServlet",
	  			{action:'saveDbLiredata',className:'PictureComparisonServlet',goodsPid:pidSrc,pidSame:pidSame,flag:flag},
	  			function(res){
	  				$("#"+pidSame).html("同款更新成功")
	  	});
	}
	
</script>
</head>
<body style="background:#000; color:#fff;">
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
<div align="center">
	</div>
	<div>
	<p style="color:#F00;text-align: center;font-size: 30px;" >每页处理完，要点最下面的保存按钮。点击完后，等待5秒在点下一页。</p>
		<table id="table1" align="center" border="1px" style="font-size: 13px;" width="800" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			
			<Tr>
				<th>搜索图</th>
				<th>lire产品1</th>
				<th>lire产品2</th>
				<th>lire产品3</th>
				<th>lire产品4</th>
				<th>lire产品5</th>
				<th>lire产品6</th>

			</Tr>
			<c:forEach items="${gbbs }" var="gbb" varStatus="i">
				<Tr class="a" id="tr${gbb.goodsName0}">
					<td> 
					
					  
					  <a id="a1" href="https://detail.1688.com/offer/${gbb.goodsName0}.html" target="_blank">
							<img  width='220px;' title="" height='220px;' src="${gbb.sourceImg }" style='cursor: pointer;' >
							<span style="color:#F00;" id="${gbb.goodsName0}"></span>
						</a>
						<c:if test="${gbb.sourceImg !=''}">
						 <br> 价格: ${gbb.price }
						 <br> 卖家在我司有多少产品: ${gbb.proCon }
						 <br> 该卖家质量水准: <c:if test="${gbb.shopQuality==0 }">没有验证</c:if><c:if test="${gbb.shopQuality!=0 }"><em style="color:#F00;">${gbb.shopQuality }</em></c:if>
						 <br> 商品评分: <c:if test="${gbb.proScore==0 }">没有评分</c:if><c:if test="${gbb.proScore!=0 }"><em style="color:#F00;">${gbb.proScore }</em></c:if>
						 <br> 1688深度验厂: <c:if test="${gbb.deepFactory==0 }">否</c:if><c:if test="${gbb.deepFactory!=0 }"><em style="color:#F00;">是</em></c:if>
						 <br> 销量: ${gbb.proSold }
						 <br><input type="button" value="侵权商品" onclick="fnQinQ('${gbb.goodsName0}',1);">
					  <input type="button" value="有中文" onclick="fnZhongW('${gbb.goodsName0}',2)">
					  <input type="button" value="太难看" onclick="fnTaiNK('${gbb.goodsName0}',3)">
					  <input type="button" value="是精品" onclick="fnJingP('${gbb.goodsName0}',4)">
					  <input type="button" value="下架" onclick="fnXiaJ('${gbb.goodsName0}',5)">
					  </c:if>
					</td>
					<td> 
						
					  <a id="a2" href="https://detail.1688.com/offer/${gbb.goodsName1}.html" target="_blank">
					  	<img  width='220px;' title="" height='220px;' src="${gbb.tbImg1 }" style='cursor: pointer;' >
					  	<span style="color:#F00;" id="${gbb.goodsName1}"></span>
					  </a>
					  <c:if test="${gbb.tbImg1 !=''}">
						 <br> 价格 ${gbb.tbprice }
						 <br> 卖家在我司有多少产品:${gbb.proCon1 }
						 <br> 该卖家质量水准:<c:if test="${gbb.shopQuality1==0 }">没有验证</c:if><c:if test="${gbb.shopQuality1!=0 }"><em style="color:#F00;">${gbb.shopQuality1 }</em></c:if>
						 <br> 商品评分:<c:if test="${gbb.proScore1==0 }">没有评分</c:if><c:if test="${gbb.proScore1!=0 }"><em style="color:#F00;">${gbb.proScore1 }</em></c:if>
						 <br> 1688深度验厂:<c:if test="${gbb.deepFactory1==0 }">否</c:if><c:if test="${gbb.deepFactory1!=0 }"><em style="color:#F00;">是</em></c:if>
						 <br> 销量:${gbb.proSold1 }
						 <br> <input type="button" value="侵权商品" onclick="fnQinQ('${gbb.goodsName1}',1);">
					  			<input type="button" value="有中文" onclick="fnZhongW('${gbb.goodsName1}',2)">
					  			<input type="button" value="太难看" onclick="fnTaiNK('${gbb.goodsName1}',3)">
					 				<input type="button" value="是精品" onclick="fnJingP('${gbb.goodsName1}',4)">
					  			<input type="button" value="下架" onclick="fnXiaJ('${gbb.goodsName1}',5)">
					  			<input type="button" value="同款" onclick="fnSame('${gbb.goodsName0}','${gbb.goodsName1}',6)">
				  </c:if>
					  			
					</td>
					<td> 
						
					  <a id="a3" href="https://detail.1688.com/offer/${gbb.goodsName2}.html" target="_blank">
					  	<img  width='220px;' title="" height='220px;' src="${gbb.tbImg2 }" style='cursor: pointer;' >
					  	<span style="color:#F00;" id="${gbb.goodsName2}"></span>
					  </a>
					  <c:if test="${gbb.tbImg2 !=''}">
						 <br> 价格 ${gbb.tbprice1 }
						 <br> 卖家在我司有多少产品:${gbb.proCon2 }
						 <br> 该卖家质量水准:<c:if test="${gbb.shopQuality2==0 }">没有验证</c:if><c:if test="${gbb.shopQuality2!=0 }"><em style="color:#F00;">${gbb.shopQuality2 }</em></c:if>
						 <br> 商品评分:<c:if test="${gbb.proScore2==0 }">没有评分</c:if><c:if test="${gbb.proScore2!=0 }"><em style="color:#F00;">${gbb.proScore2 }</em></c:if>
						 <br> 1688深度验厂:<c:if test="${gbb.deepFactory2==0 }">否</c:if><c:if test="${gbb.deepFactory2!=0 }"><em style="color:#F00;">是</em></c:if>
						 <br> 销量:${gbb.proSold2 }
						 <br><input type="button" value="侵权商品" onclick="fnQinQ('${gbb.goodsName2}',1);">
					  <input type="button" value="有中文" onclick="fnZhongW('${gbb.goodsName2}',2)">
					  <input type="button" value="太难看" onclick="fnTaiNK('${gbb.goodsName2}',3)">
					  <input type="button" value="是精品" onclick="fnJingP('${gbb.goodsName2}',4)">
					  <input type="button" value="下架" onclick="fnXiaJ('${gbb.goodsName2}',5)">
					  <input type="button" value="同款" onclick="fnSame('${gbb.goodsName0}','${gbb.goodsName2}',6)">
					  </c:if>
					</td>
					<td> 
						
					  <a id="a4" href="https://detail.1688.com/offer/${gbb.goodsName3}.html" target="_blank">
					  	<img  width='220px;' title="" height='220px;' src="${gbb.tbImg3 }" style='cursor: pointer;' >
					  	<span style="color:#F00;" id="${gbb.goodsName3}"></span>
					  </a>
						<c:if test="${gbb.tbImg3 !=''}">
						 <br> 价格 ${gbb.tbprice2 }
						 <br> 卖家在我司有多少产品:${gbb.proCon3 }
						 <br> 该卖家质量水准:<c:if test="${gbb.shopQuality3==0 }">没有验证</c:if><c:if test="${gbb.shopQuality3!=0 }"><em style="color:#F00;">${gbb.shopQuality3 }</em></c:if>
						 <br> 商品评分:<c:if test="${gbb.proScore3==0 }">没有评分</c:if><c:if test="${gbb.proScore3!=0 }"><em style="color:#F00;">${gbb.proScore3 }</em></c:if>
						 <br> 1688深度验厂:<c:if test="${gbb.deepFactory3==0 }">否</c:if><c:if test="${gbb.deepFactory3!=0 }"><em style="color:#F00;">是</em></c:if>
						 <br> 销量:${gbb.proSold3 }
						 <br><input type="button" value="侵权商品" onclick="fnQinQ('${gbb.goodsName3}',1);">
					  <input type="button" value="有中文" onclick="fnZhongW('${gbb.goodsName3}',2)">
					  <input type="button" value="太难看" onclick="fnTaiNK('${gbb.goodsName3}',3)">
					  <input type="button" value="是精品" onclick="fnJingP('${gbb.goodsName3}',4)">
					  <input type="button" value="下架" onclick="fnXiaJ('${gbb.goodsName3}',5)">
					  <input type="button" value="同款" onclick="fnSame('${gbb.goodsName0}','${gbb.goodsName3}',6)">
					  </c:if>
					</td>
					<td> 
						
					  <a id="a5" href="https://detail.1688.com/offer/${gbb.goodsName4}.html" target="_blank">
					  	<img  width='220px;' title="" height='220px;' src="${gbb.tbImg4 }" style='cursor: pointer;' >
					  	<span style="color:#F00;" id="${gbb.goodsName4}"></span>
					  </a>
					  <c:if test="${gbb.tbImg4 !=''}">
						 <br> 价格 ${gbb.tbprice3 }
						 <br> 卖家在我司有多少产品:${gbb.proCon4 }
						 <br> 该卖家质量水准:<c:if test="${gbb.shopQuality4==0 }">没有验证</c:if><c:if test="${gbb.shopQuality4!=0 }"><em style="color:#F00;">${gbb.shopQuality4 }</em></c:if>
						 <br> 商品评分:<c:if test="${gbb.proScore4==0 }">没有评分</c:if><c:if test="${gbb.proScore4!=0 }"><em style="color:#F00;">${gbb.proScore4 }</em></c:if>
						 <br> 1688深度验厂:<c:if test="${gbb.deepFactory4==0 }">否</c:if><c:if test="${gbb.deepFactory4!=0 }"><em style="color:#F00;">是</em></c:if>
						 <br> 销量:${gbb.proSold4 }
						 <br> <input type="button" value="侵权商品" onclick="fnQinQ('${gbb.goodsName4}',1);">
					  <input type="button" value="有中文" onclick="fnZhongW('${gbb.goodsName4}',2)">
					  <input type="button" value="太难看" onclick="fnTaiNK('${gbb.goodsName4}',3)">
					  <input type="button" value="是精品" onclick="fnJingP('${gbb.goodsName4}',4)">
					  <input type="button" value="下架" onclick="fnXiaJ('${gbb.goodsName4}',5)">
					  <input type="button" value="同款" onclick="fnSame('${gbb.goodsName0}','${gbb.goodsName4}',6)">
					  </c:if>
					</td>
					<td> 
						
					  <a id="a6" href="https://detail.1688.com/offer/${gbb.goodsName5}.html" target="_blank">
					  	<img  width='220px;' title="" height='220px;' src="${gbb.tbImg5 }" style='cursor: pointer;' >
					  	<span style="color:#F00;" id="${gbb.goodsName5}"></span>
					  </a>
						<c:if test="${gbb.tbImg5 !=''}">
						 <br> 价格 ${gbb.tbprice4 }
						 <br> 卖家在我司有多少产品:${gbb.proCon5 }
						 <br> 该卖家质量水准:<c:if test="${gbb.shopQuality5==0 }">没有验证</c:if><c:if test="${gbb.shopQuality5!=0 }"><em style="color:#F00;">${gbb.shopQuality5 }</em></c:if>
						 <br> 商品评分:<c:if test="${gbb.proScore5==0 }">没有评分</c:if><c:if test="${gbb.proScore5!=0 }"><em style="color:#F00;">${gbb.proScore5 }</em></c:if>
						 <br> 1688深度验厂:<c:if test="${gbb.deepFactory5==0 }">否</c:if><c:if test="${gbb.deepFactory5!=0 }"><em style="color:#F00;">是</em></c:if>
						 <br> 销量:${gbb.proSold5 }
						 <br><input type="button" value="侵权商品" onclick="fnQinQ('${gbb.goodsName5}',1);">
					  <input type="button" value="有中文" onclick="fnZhongW('${gbb.goodsName5}',2)">
					  <input type="button" value="太难看" onclick="fnTaiNK('${gbb.goodsName5}',3)">
					  <input type="button" value="是精品" onclick="fnJingP('${gbb.goodsName5}',4)">
					  <input type="button" value="下架" onclick="fnXiaJ('${gbb.goodsName5}',5)">
					  <input type="button" value="同款" onclick="fnSame('${gbb.goodsName0}','${gbb.goodsName5}',6)">
					  </c:if>
					</td>
					<td> 
						
					  <a id="a7" href="https://detail.1688.com/offer/${gbb.goodsName6}.html" target="_blank">
					  	<img  width='220px;' title="" height='220px;' src="${gbb.tbImg6 }" style='cursor: pointer;' >
					  	<span style="color:#F00;" id="${gbb.goodsName6}"></span>
					  </a>
					  <c:if test="${gbb.tbImg6 !=''}">
						 <br> 价格 ${gbb.tbprice5 }
						 <br> 卖家在我司有多少产品:${gbb.proCon6}
						 <br> 该卖家质量水准:<c:if test="${gbb.shopQuality6==0 }">没有验证</c:if><c:if test="${gbb.shopQuality6!=0 }"><em style="color:#F00;">${gbb.shopQuality6 }</em></c:if>
						 <br> 商品评分:<c:if test="${gbb.proScore6==0 }">没有评分</c:if><c:if test="${gbb.proScore6!=0 }"><em style="color:#F00;">${gbb.proScore6 }</em></c:if>
						 <br> 1688深度验厂:<c:if test="${gbb.deepFactory6==0 }">否</c:if><c:if test="${gbb.deepFactory6!=0 }"><em style="color:#F00;">是</em></c:if>
						 <br> 销量:${gbb.proSold6 }
						 <br><input type="button" value="侵权商品" onclick="fnQinQ('${gbb.goodsName6}',1);">
					  	<input type="button" value="有中文" onclick="fnZhongW('${gbb.goodsName6}',2)">
					  <input type="button" value="太难看" onclick="fnTaiNK('${gbb.goodsName6}',3)">
					  <input type="button" value="是精品" onclick="fnJingP('${gbb.goodsName6}',4)">
					  <input type="button" value="下架" onclick="fnXiaJ('${gbb.goodsName6}',5)">
					  <input type="button" value="同款" onclick="fnSame('${gbb.goodsName0}','${gbb.goodsName6}',6)">
					  </c:if>
					</td>
					
				</Tr>
			</c:forEach>
		</table>
		</br>
 	 	<div align="center">${pager }<input type="button" value="保存" onclick="fnLireSave();"></div>
		
	</div>
</div>
</body>
</html>
