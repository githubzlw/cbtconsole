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
<title>图片比较</title>
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

	function findImgId(obj,obj1,obj2,obj3,tbPid,tburl,tbprice,tbname,delFlag){
		
 		var tb0 = $(obj).attr("id");
 		$("#"+tb0).css("border","6px solid red"); 
 		$("#"+obj1).css("border",""); 
 		$("#"+obj2).css("border",""); 
 		$("#"+obj3).css("border",""); 
 		
/* 		$.post("/cbtconsole/customerServlet",
	  			{action:'saveTbgooddata',className:'PictureComparisonServlet',goodId:tbPid,tbUrl:tburl,tbPrice:tbprice,tbName:tbname,delFlag:delFlag},
	  			function(res){
					/* if(res>0){
						alert("保存成功");
					}else{
						alert("保存失败");
					} */
	  	//}); 
 		
	}
	
	function findImg(obj,obj1,obj2,obj3,delFlag){
		
 		var tb0 = $(obj).attr("id");
 		$("#"+tb0).css("border","6px solid red"); 
 		$("#"+obj1).css("border",""); 
 		$("#"+obj2).css("border",""); 
 		$("#"+obj3).css("border",""); 
 		
	}
	
	function cancel(canl,aligSourceUrl){
		
 		$("#"+canl).css("background","red");
		var tabLen = $("#table").find("tr").length;
		for(var i=1; i<tabLen; i++){
			var tdLen= $("#table").find("tr:eq("+i+")").children().length;
			for(var j =1; j<tdLen; j++){
				var selUrl = $("#table tr:eq("+i+") td:eq("+j+")").find("input[id='aligSourceUrl']").val();
				var imgIdIndex = $("#table tr:eq("+i+") td:eq("+j+")").find("img").attr("id");
				if(aligSourceUrl==selUrl){
 					$("#"+imgIdIndex).css("border","");
				}
			}
		}
		
 		$.post("/cbtconsole/customerServlet",
			{action:'updateCachedata',className:'PictureComparisonServlet',url:aligSourceUrl,flag:0},
			function(res){
		}); 
		
/* 		$.post("/cbtconsole/customerServlet",
	  			{action:'saveTbgooddata',className:'PictureComparisonServlet',goodId:tbPid,delFlag:delFlag},
	  			function(res){
	  	}); */
	}
	
/* 	function fnSave(){

		var page1 = document.getElementById("page1").value;
		var page = Number(page1);
		page = page+1;
 		var ary=new Array();

		var s=$("#table").find("tr").length;
		for(var i=1;i<s;i++){
			var trId=$("#table tr:eq("+i+")").attr("id");
			var pId=trId.substring("2");
			var tb1=$("#table tr:eq("+i+") td:eq(1)").find("img").attr("style");
			var tb2=$("#table tr:eq("+i+") td:eq(2)").find("img").attr("style");
			var tb3=$("#table tr:eq("+i+") td:eq(3)").find("img").attr("style");
			var tb4=$("#table tr:eq("+i+") td:eq(4)").find("img").attr("style");
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
		//alert("保存成功");
		//window.open("/cbtconsole/customerServlet?action=findAllPicture&className=PictureComparisonServlet");
		window.open("/cbtconsole/customerServlet?action=findAllPicture&className=PictureComparisonServlet&page="+page);
		//window.location.href("/cbtconsole/customerServlet?action=findAllPicture&className=PictureComparisonServlet&page="+page);
		
		
	} */
	
	function fnSave(){

		var page1 = document.getElementById("page1").value;
		var page = Number(page1);
		page = page+1;
 		var ary=new Array();

		var s=$("#table").find("tr").length;
		for(var i=1;i<s;i++){
			
			var gbbUrl = $("#table tr:eq("+i+") td:eq(0)").find("input[id='gbbUrl']").val();
			
			//var trId=$("#table tr:eq("+i+")").attr("id");
			//var pId=trId.substring("2");
			var tb1=$("#table tr:eq("+i+") td:eq(1)").find("img").attr("style");
			var tb2=$("#table tr:eq("+i+") td:eq(2)").find("img").attr("style");
			var tb3=$("#table tr:eq("+i+") td:eq(3)").find("img").attr("style");
			var tb4=$("#table tr:eq("+i+") td:eq(4)").find("img").attr("style");
			var tb5=$("#table tr:eq("+i+") td:eq(5)").find("img").attr("style");
			var tb6=$("#table tr:eq("+i+") td:eq(6)").find("img").attr("style");
			var tb7=$("#table tr:eq("+i+") td:eq(7)").find("img").attr("style");
			var tb8=$("#table tr:eq("+i+") td:eq(8)").find("img").attr("style");
			var tbBorder1 = tb1.split(":");
			var tbBorder2 = tb2.split(":");
			var tbBorder3 = tb3.split(":");
			var tbBorder4 = tb4.split(":");
			
			if(tb5=="undefined" || tb5=="" || tb5==null){
				
			}else{
				var tbBorder5 = tb5.split(":");
				var tbBorder6 = tb6.split(":");
				var tbBorder7 = tb7.split(":");
				var tbBorder8 = tb8.split(":");
			}


			if(tbBorder1[2]=="undefined" || tbBorder1[2]=="" || tbBorder1[2]==null){
				
			}else{
				ary.push(gbbUrl+",tb1,"+tbBorder1[2]);
			}
			if(tbBorder2[2]=="undefined" || tbBorder2[2]=="" || tbBorder2[2]==null){
				
			}else{
				ary.push(gbbUrl+",tb2,"+tbBorder2[2]);
			}
			if(tbBorder3[2]=="undefined" || tbBorder3[2]=="" || tbBorder3[2]==null){
				
			}else{
				ary.push(gbbUrl+",tb3,"+tbBorder3[2]);
			}
			if(tbBorder4[2]=="undefined" || tbBorder4[2]=="" || tbBorder4[2]==null){
				
			}else{
				ary.push(gbbUrl+",tb4,"+tbBorder4[2]);
			}
			
			if(tb5=="undefined" || tb5=="" || tb5==null){
				
			}else{
				if(tbBorder5[2]=="undefined" || tbBorder5[2]=="" || tbBorder5[2]==null){
					
				}else{
					ary.push(gbbUrl+",tb5,"+tbBorder5[2]);
				}
				if(tbBorder6[2]=="undefined" || tbBorder6[2]=="" || tbBorder6[2]==null){
					
				}else{
					ary.push(gbbUrl+",tb6,"+tbBorder6[2]);
				}
				if(tbBorder7[2]=="undefined" || tbBorder7[2]=="" || tbBorder7[2]==null){
					
				}else{
					ary.push(gbbUrl+",tb7,"+tbBorder7[2]);
				}
				if(tbBorder8[2]=="undefined" || tbBorder8[2]=="" || tbBorder8[2]==null){
					
				}else{
					ary.push(gbbUrl+",tb8,"+tbBorder8[2]);
				}
			}

	
		}
		
 		for(var i=0;i<ary.length;i++){
			var gbbUrl=ary[i].split(',')[0];
			//var tbId=ary[i].split(',')[1];
			
			$.post("/cbtconsole/customerServlet?action=updateCacheFlag&className=PictureComparisonServlet",
					{url:gbbUrl,flag:1},
					function(res){
						if(res>0){
							//alert("保存成功");
						}
			});
		}
		window.open("/cbtconsole/customerServlet?action=findPictureCompare&className=PictureComparisonServlet&page="+page);
	}
	
	function fnSelectInfo(){
		window.location = "/cbtconsole/customerServlet?action=findAllTaoBaoInfo&className=PictureComparisonServlet";

	}
	
	function imgSelected(obj,aligSourceUrl,canl){
		var imgId = $(obj).attr("id");
		var tabLen = $("#table").find("tr").length;
		for(var i=1; i<tabLen; i++){
			var tdLen= $("#table").find("tr:eq("+i+")").children().length;
			for(var j =1; j<tdLen; j++){
				var selUrl = $("#table tr:eq("+i+") td:eq("+j+")").find("input[id='aligSourceUrl']").val();
				var imgIdIndex = $("#table tr:eq("+i+") td:eq("+j+")").find("img").attr("id");
				if(aligSourceUrl==selUrl){
					if(imgId==imgIdIndex){
						$("#"+imgIdIndex).css("border","6px solid red");
	 				}else{
	 					$("#"+imgIdIndex).css("border","");
	 				}
				}
			}
		}
		$("#"+canl).css("background","");
	}
	
	
</script>
</head>
<body style="background:#000; color:#fff;">
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
<div>

&nbsp;&nbsp;&nbsp;&nbsp;<p style="color:red;">已处理的产品数量:${resoultCount}</p>
<%-- 相似度高的产品:${count1}<br />
相似度很低的产品:${count2}<br />
相似度高，但 第一个产品的价格 > AliExpress价格*0.8 的产品:${count3}<br />
相似度高，但 第一个产品的价格 > AliExpress价格 的产品:${count4}<br />
相似度高，但 第一个产品的价格 < AliExpress价格*0.3 的产品:${count5}<br />
相似度高，但 第一个产品的价格 < AliExpress价格*0.2 的产品:${count6}<br />
相似度高，但 第一个产品的价格 < AliExpress价格*0.1 的产品:${count7}<br />
第1和第2 个产品都和AliExpress相似度高, 而且  Min (第一产品价格 和 第一产品价格）> AliExpress*0.8 的产品:${count8}<br />
第1,第2 ,第3个产品 都和AliExpress相似度高, 而且  Min (第1,第2,第3 产品价格）> AliExpress*0.8 的产品:${count9}<br />
有ali区间价格:${count10}<br />
前3个淘宝产品最低价格 > AliExpress价格:${count11} --%>
</div>
<div align="center">

<%-- 	<form action="/cbtconsole/customerServlet?action=findAllPicture&className=PictureComparisonServlet" onsubmit="return check();" method="post">
		<table>
			<tr>
				<td>相似度:
					<select id="similarity"  style="width:100px;" onchange="selectSimilarity(this.value)" >
						<option selected="selected"></option>
						<c:if test="${similarityId=='1'}">
							<option value="1" selected="selected">非常相似(0-5)</option>
							<option value="2">有点相似(5-10)</option>
							<option value="3">完全不一样(10以上)</option>
						</c:if>
						<c:if test="${similarityId=='2'}">
							<option value="1">非常相似(0-5)</option>
							<option value="2" selected="selected">有点相似(5-10)</option>
							<option value="3">完全不一样(10以上)</option>
						</c:if>
						<c:if test="${similarityId=='3'}">
							<option value="1">非常相似(0-5)</option>
							<option value="2">有点相似(5-10)</option>
							<option value="3" selected="selected">完全不一样(10以上)</option>
						</c:if>
						<c:if test="${similarityId==null || similarityId==''}">
							<option value="1">非常相似(0-5)</option>
							<option value="2">有点相似(5-10)</option>
							<option value="3">完全不一样(10以上)</option>
						</c:if>
					</select>
					<input type="hidden" id="similarityId" name ="similarityId">
				</td>
				<td>销量:<input type="text" value="${selled }" id="selled" name="selled" maxlength="10"><font id="ts" color="red"></font></td>
				<td>大分类：
					<select id="logistics" style="width:230px" onchange="getCodeId(this.value)">
						<option value="" selected="selected"></option> 
			             <c:forEach var="categoryList" items="${categoryList}">
			              	<c:if test="${categoryList.cid==cid}">
			             		<option value="${categoryList.cid}" selected="selected">${categoryList.categoryName}</option> 
			             	</c:if>
			             	<c:if test="${categoryList.cid!=cid}">
			             		<option value="${categoryList.cid}">${categoryList.categoryName}</option> 
			             	</c:if>
			             </c:forEach>
		          	</select>
		          	<input type="hidden" id="categoryId" name="categoryId" value="${cid}">
			   </td>
			   <td>小分类：
					<select id="logistics1" style="width:300px" onchange="getCodeId1(this.value)">
						<option value="" selected="selected"></option> 
			             <c:forEach var="categoryList1" items="${categoryList1}">
				             <c:if test="${categoryList1.cid==categoryId1}">
				             	<option value="${categoryList1.cid}" selected="selected">${categoryList1.categoryName}</option> 
				             </c:if>
				             <c:if test="${categoryList1.cid!=categoryId1}">
				             	<option value="${categoryList1.cid}">${categoryList1.categoryName}</option> 
				             </c:if>
			             </c:forEach>
		          	</select>
		          	<input type="hidden" id="categoryId1" name ="categoryId1">
			   </td>
			   <td><input type="submit" value="查询"></td>
			</tr>
		</table> 
	</form> --%>
	</div>
	<div>
<!-- 		<table id="table1" align="center" border="1px" style="font-size: 13px;" width="800" bordercolor="#8064A2" cellpadding="0" cellspacing="0" > -->
		<table id="table" align="center" border="1px" style="font-size: 13px;" width="800" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>AliExpress产品</th>
				<th>淘宝产品1</th>
				<th>淘宝产品2</th>
				<th>淘宝产品3</th>
				<th>淘宝产品4</th>
				<th>1688产品1</th>
				<th>1688产品2</th>
				<th>1688产品3</th>
				<th>1688产品4</th>
				<!-- <th>相似度值</th> -->
			</Tr>
			<c:forEach items="${gbbs }" var="gbb" varStatus="i">
				<Tr class="a" id="tr${i.index}">
					<td id="canl${i.index}"> 
						<img  width='200px' title="${gbb.goodsName}" height='200px;' src="${gbb.aliSourceImgUrl }" style='cursor: pointer;' >
						<br />${gbb.aliSourcePrice}&nbsp;&nbsp;&nbsp;<a target='_blank' href="${gbb.aligSourceUrl}" class="a1" >Info</a>
						<br /><input type="hidden" id="gbbUrl" value="${gbb.aligSourceUrl}">
<%-- 					 	<c:forEach items="${gbb.aliStyleList }" var="aliStys">
								<c:if test="${ aliStys.aliStyImg!=''}">
									<img src="${ aliStys.aliStyImg}" title="${aliStys.aliStyValue}">
								</c:if>
								<c:if test="${ aliStys.aliStyImg==''}">
									${aliStys.aliStyType}${aliStys.aliStyValue}
								</c:if>
						</c:forEach> --%>
					<%-- 	<input type="hidden" id="aligSourceUrl" value="${gbb.aligSourceUrl}"> --%>
						<br /><input style="height:30px;width:100px;" type="button" value="无货" onclick="cancel('canl${i.index}','${gbb.aligSourceUrl}')">
					</td>
					
					 <td> 
						<img  width='200px' title="${gbb.tbName}" height='200px;' src="${gbb.tbImg }"  style="cursor: pointer;border:6px solid red" 
							onclick="imgSelected(this,'${gbb.aligSourceUrl}','canl${i.index}')" id ="tbimg0${i.index}">
							<br /><fmt:formatNumber value="${gbb.tbprice/6.4}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;<a target='_blank' href="${gbb.tbUrl}" class="a1" >Info</a>
		<%-- 					<br />
							汉明距离:${gbb.imgCheck0 }
							<br />
							lire相似度${gbb.score }
							<br />
							<img  width='100px' title="" height='100px;' src="http://192.168.1.26${gbb.tbPath }"  style="cursor: pointer;" > --%>
<%-- 							<br />
							<c:forEach items="${gbb.styImgList }" var="styImgs">
								<c:if test="${ styImgs.styImg!=''}">
									<img src="${ styImgs.styImg}" title="${styImgs.styValue}">
								</c:if>
								<c:if test="${ styImgs.styImg==''}">
									${styImgs.styType}${styImgs.styValue}
								</c:if>
							</c:forEach> --%>
							<input type="hidden" id="aligSourceUrl" value="${gbb.aligSourceUrl}">
							
					</td>
					<td> 
						<img  width='200px' title="${gbb.tbName1}" height='200px;' src="${gbb.tbImg1 }" style="cursor: pointer;" 
							onclick="imgSelected(this,'${gbb.aligSourceUrl}')" id ="tbimg1${i.index}">
						<br /><fmt:formatNumber value="${gbb.tbprice1/6.4}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;<a target='_blank' href="${gbb.tbUrl1}" class="a1" >Info</a>
			<%-- 			<br />
							lire相似度${gbb.score1 }
							<br />
							<img  width='100px' title="" height='100px;' src="http://192.168.1.26${gbb.tbPath1 }"  style="cursor: pointer;" > --%>
						<%-- <br />
								<c:forEach items="${gbb.styImgList1 }" var="styImgs">
									<c:if test="${ styImgs.styImg1!=''}">
										<img src="${ styImgs.styImg1}" title="${styImgs.styValue1}">
									</c:if>
									<c:if test="${ styImgs.styImg1==''}">
										${styImgs.styType1}${styImgs.styValue1}
									</c:if>
								</c:forEach> --%>
								<input type="hidden" id="aligSourceUrl" value="${gbb.aligSourceUrl}">
					</td>
					<td> 
						<img  width='200px' title="${gbb.tbName2}" height='200px;' src="${gbb.tbImg2 }" style="cursor: pointer;" 
							onclick="imgSelected(this,'${gbb.aligSourceUrl}')" id ="tbimg2${i.index}">
							<br /><fmt:formatNumber value="${gbb.tbprice2/6.4}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;<a target='_blank' href="${gbb.tbUrl2}" class="a1" >Info</a>
<%-- 							<br />
								<c:forEach items="${gbb.styImgList2 }" var="styImgs">
									<c:if test="${ styImgs.styImg2!=''}">
										<img src="${ styImgs.styImg2}" title="${styImgs.styValue2}">
									</c:if>
									<c:if test="${ styImgs.styImg2==''}">
										${styImgs.styType2}${styImgs.styValue2}
									</c:if>
								</c:forEach> --%>
								<input type="hidden" id="aligSourceUrl" value="${gbb.aligSourceUrl}">
					</td>
					<td> 
						<img  width='200px' title="${gbb.tbName3}" height='200px;' src="${gbb.tbImg3 }" style="cursor: pointer;" 
							onclick="imgSelected(this,'${gbb.aligSourceUrl}')" id ="tbimg3${i.index}">
						<br /><fmt:formatNumber value="${gbb.tbprice3/6.4}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;
						<a target='_blank' href="${gbb.tbUrl3}" class="a1" >Info</a>
<%-- 						<br />
								<c:forEach items="${gbb.styImgList3 }" var="styImgs">
									<c:if test="${ styImgs.styImg3!=''}">
										<img src="${ styImgs.styImg3}" title="${styImgs.styValue3}">
									</c:if>
									<c:if test="${ styImgs.styImg3==''}">
										${styImgs.styType3}${styImgs.styValue3}
									</c:if>
								</c:forEach> --%>
								<input type="hidden" id="aligSourceUrl" value="${gbb.aligSourceUrl}">
					</td>
					
<%-- 					<c:forEach items="${gbb.pictureList}" var="cl" varStatus="j">
						<td> 
							<img  width='200px' title="" height='200px;' src="http://192.168.1.26${cl.imgurl }"  style="cursor: pointer;${cl.tbFlag!=null ? 'border:6px solid red':'border:'}" 
								onclick="imgSelected(this,'${cl.aligSourceUrl}')" id="img${i.index}${j.index}">
							<br /><fmt:formatNumber value="${cl.price/6.2}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;<a target='_blank' href="${cl.url}" class="a1" >Info</a>
							&nbsp;&nbsp;&nbsp;相似度值${cl.dScore }
							<input type="hidden" id="aligSourceUrl" value="${cl.aligSourceUrl}">
						
						</td>
					</c:forEach> --%>
					<c:forEach items="${gbb.pictureList}" var="cl" varStatus="j">
						<td> 
							<img  width='200px' title="" height='200px;' src="http://192.168.1.26${cl.imgurl }"  style="cursor: pointer;" 
								onclick="imgSelected(this,'${cl.aligSourceUrl}')" id="img${i.index}${j.index}">
							<br /><fmt:formatNumber value="${cl.price/6.4}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;<a target='_blank' href="${cl.url}" class="a1" >Info</a>
							<%-- &nbsp;&nbsp;&nbsp;相似度值${cl.dScore } --%>
							<input type="hidden" id="aligSourceUrl" value="${cl.aligSourceUrl}">
						
						</td>
					</c:forEach>
					
				<%-- 	<td>${gbb.minImgCheck }</td> --%>
					<%-- <td id="canl${i.index}"><input style="height:150px;" type="button" value="无货" onclick="cancel('canl${i.index}','${gbb.aligSourceUrl}')"></td> --%>
				</Tr>
			</c:forEach>
		</table>
		</br>
		<div style="width:180px; margin:auto;" >
			<button onclick="fnSave()"  style="width:110px;height:40px;">保存</button>
		</div>
		
 	 	<div align="center">${pager }
 	 	<%-- 	<a href="/cbtconsole/customerServlet?action=findAllTaoBaoInfo&className=PictureComparisonServlet" target="_blank" >已保存数据</a> --%>
 	 	</div>
		<input type ="hidden" id="page1" name="page1" value="${page}">
<%-- 	 	<div class="pages" id="pages" style="margin:auto;width:620px;">
		<span>&nbsp;&nbsp;总条数：${counto1}&nbsp;&nbsp;总页数：${count1}</span>&nbsp;&nbsp;当前页:${pageCount}<button onclick="pageFn(3,${pageCount})">上一页</button>&nbsp;<button  onclick="pageFn(2,${pageCount})">下一页</button></div>
		</div> --%>
	</div>
</div>
</body>
</html>