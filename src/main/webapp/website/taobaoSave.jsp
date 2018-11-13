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

	function findImgId(obj,obj1,obj2,obj3,obj4,obj5,obj6,obj7,obj8,obj9,obj10,obj11,tbPid,tburl,shopId,moqPrice,catId,delFlag){
		
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
 		
 /* 		$.post("/cbtconsole/customerServlet",
	  			{action:'saveTbgooddata',className:'PictureComparisonServlet',goodId:tbPid,tbUrl:tburl,tbPrice:tbprice,tbName:tbname,delFlag:delFlag},
	  			function(res){ */
					/* if(res>0){
						alert("保存成功");
					}else{
						alert("保存失败");
					} */
	  	//}); 
 
 //findImgId(this,'tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','${gbb.goodsPid}','${gbb.tbUrl}','${gbb.tbprice}','${gbb.tbName}',0)"
 
 		$.post("/cbtconsole/customerServlet",
	  			{action:'saveTbgooddata',className:'PictureComparisonServlet',goodId:tbPid,tbUrl:tburl,shopId:shopId,moqPrice:moqPrice,catId:catId},
	  			function(res){ 
					 /* if(res>0){
						alert("保存成功");
					}else{
						alert("保存失败");
					}  */
	  	}); 
 		
	}
	
/* 	function cancel(obj,obj1,obj2,obj3,obj4,obj5,tbPid,delFlag){
		$("#"+obj).css("border","6px solid blue"); 
 		$("#"+obj1).css("border","6px solid blue"); 
 		$("#"+obj2).css("border","6px solid blue"); 
 		$("#"+obj3).css("border","6px solid blue"); 
 		$("#"+obj4).css("border","6px solid blue"); 
 		$("#"+obj5).css("border","6px solid blue"); 
 		
 		$.post("/cbtconsole/customerServlet",
	  			{action:'updateAliFlag',className:'PictureComparisonServlet',goodId:tbPid},
	  			function(res){
	  	}); 
	} */
	
	//无对标
	function cancel(bench,appro,cancel,obj,obj1,obj2,obj3,obj4,obj5,obj6,obj7,obj8,obj9,obj10,obj11,tbPid,delFlag,sourceYlpid){
/* 		$("#"+obj).css("border","6px solid blue"); 
 		$("#"+obj1).css("border","6px solid blue"); 
 		$("#"+obj2).css("border","6px solid blue"); 
 		$("#"+obj3).css("border","6px solid blue"); 
 		$("#"+obj4).css("border","6px solid blue"); 
 		$("#"+obj5).css("border","6px solid blue"); 
 		$("#"+obj6).css("border","6px solid blue"); 
 		$("#"+obj7).css("border","6px solid blue"); 
 		$("#"+obj8).css("border","6px solid blue"); 
 		$("#"+obj9).css("border","6px solid blue"); 
 		$("#"+obj10).css("border","6px solid blue"); 
 		$("#"+obj11).css("border","6px solid blue"); */ 
 		
 		$.post("/cbtconsole/customerServlet",
	  			{action:'updateAliInfoFlag',className:'PictureComparisonServlet',goodId:tbPid,delFlag:delFlag,sourceYlpid:sourceYlpid},
	  			function(res){
	  				$("#"+cancel).css("background","blue");
	  				$("#"+bench).css("background","");
	  				$("#"+appro).css("background","");
	  	}); 
	}
	
	//精准对标
	function bench(bench,appro,cancel,tbPid,delFlag,sourceYlpid){
 		
 		$.post("/cbtconsole/customerServlet",
	  			{action:'updateAliInfoFlag',className:'PictureComparisonServlet',goodId:tbPid,delFlag:delFlag,sourceYlpid:sourceYlpid},
	  			function(res){
	  				$("#"+bench).css("background","red");
	  				$("#"+cancel).css("background","");
	  				$("#"+appro).css("background","");
	  	}); 
	}
	//近似对标
	function appro(bench,appro,cancel,tbPid,delFlag,sourceYlpid){
 		
 		$.post("/cbtconsole/customerServlet",
	  			{action:'updateAliInfoFlag',className:'PictureComparisonServlet',goodId:tbPid,delFlag:delFlag,sourceYlpid:sourceYlpid},
	  			function(res){
	  				$("#"+appro).css("background","green");
	  				$("#"+cancel).css("background","");
	  				$("#"+bench).css("background","");
	  	}); 
	}
	//手工录入更新
	function updatePid(goodsPid,sourceYlpid){
 		
		var ylPid = $("#ylPid").val();
		if(ylPid==""){
			alert("1688产品Id不能为空");
			return;
		}
		//alert(goodsPid);
 		$.post("/cbtconsole/customerServlet",
	  			{action:'updateYlPid',className:'PictureComparisonServlet',goodsPid:goodsPid,ylPid:ylPid,sourceYlpid:sourceYlpid},
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
	
	function fnSelectInfo(){
		window.location = "/cbtconsole/customerServlet?action=findAllTaoBaoInfo&className=PictureComparisonServlet";
	}
	
	
</script>
</head>
<body style="background:#000; color:#fff;">
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
<div align="center">
	<%-- <form action="/cbtconsole/customerServlet?action=findAllPicture&className=PictureComparisonServlet" onsubmit="return check();" method="post">
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
			             	<c:if test="${categoryList.cid==categoryId}">
			             		<option value="${categoryList.cid}" selected="selected">${categoryList.categoryName}</option> 
			             	</c:if>
			             	<c:if test="${categoryList.cid!=categoryId}">
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
		<table id="table1" align="center" border="1px" style="font-size: 13px;" width="800" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>AliExpress产品</th>
				<th>1688产品1</th>
				<th>1688产品2</th>
				<th>1688产品3</th>
				<th>1688产品4</th>
				<th>1688产品5</th>
				<th>1688产品6</th>
				<th>1688产品7</th>
				<th>1688产品8</th>
				<th>1688产品9</th>
				<th>1688产品10</th>
				<th>1688产品11</th>
				<th>1688产品12</th>
				<c:if test="${similarityId!=''}">
					<th>手工录入1688产品Id</th>
				</c:if>
				

			</Tr>
			<c:forEach items="${gbbs }" var="gbb" varStatus="i">
				<Tr class="a" id="tr${gbb.pId}">
					<td> 
						<img  width='140px;' title="${gbb.goodsName}" height='140px;' src="${gbb.imgpath }" style='cursor: pointer;' >
						 <br> ${gbb.goodsPid}
						<br>
						<input id="bench${gbb.goodsPid}" style="height: 30px;width: 86px;float: right;" type="button" value="精准对标" onclick="bench('bench${gbb.goodsPid}','appro${gbb.goodsPid}','cancel${gbb.goodsPid}','${gbb.goodsPid}',1,'${ylbbPid}')">
						<br>
						<input id="appro${gbb.goodsPid}" style="height: 30px;width: 86px;float: right;" type="button" value="近似对标" onclick="appro('bench${gbb.goodsPid}','appro${gbb.goodsPid}','cancel${gbb.goodsPid}','${gbb.goodsPid}',2,'${ylbbPid}')">
						<br>
						<input id="cancel${gbb.goodsPid}" style="height: 30px;width: 86px;float: right;" type="button" value="无对标" onclick="cancel('bench${gbb.goodsPid}','appro${gbb.goodsPid}','cancel${gbb.goodsPid}','tbimg0${i.index}','tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','tbimg6${i.index}','tbimg7${i.index}','tbimg8${i.index}','tbimg9${i.index}','tbimg10${i.index}','tbimg11${i.index}','${gbb.goodsPid}',3,'${ylbbPid}')">
						
						<br /><a target='_blank' href="https://www.aliexpress.com/item/50-Pcs-Stainless-Steel-Rings-For-Nylon-Darts-Shafts-Dart-Professional-Silver-Dart-Shaft-Accessories-Hunting/${gbb.goodsPid}.html" class="a1" >Info</a>
						
					</td>
					
					<td> 
						<img  width='140px;' title="${gbb.tbName}" height='140px;' src="${gbb.tbImg }"  style="cursor: pointer;${gbb.tbflagname == 'tb1' ? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','tbimg6${i.index}','tbimg7${i.index}','tbimg8${i.index}','tbimg9${i.index}','tbimg10${i.index}','tbimg11${i.index}','${gbb.goodsPid}','${gbb.tbUrl}','${gbb.shopId}','${gbb.moqPrice}','${gbb.categoryid}',0)" id ="tbimg0${i.index}">
							<%-- <br /><fmt:formatNumber value="${gbb.tbprice/6.2}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;<a target='_blank' href="${gbb.tbUrl}" class="a1" >Info</a> --%>
							<br />${gbb.moqPrice}
							<br /><a target='_blank' href="${gbb.tbUrl}" class="a1" >Info</a>${gbb.imgCheck}
					</td>
					<td> 
						<img  width='140px;' title="${gbb.tbName1}" height='140px;' src="${gbb.tbImg1 }" style="cursor: pointer;${gbb.tbflagname == 'tb2'? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg0${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','tbimg6${i.index}','tbimg7${i.index}','tbimg8${i.index}','tbimg9${i.index}','tbimg10${i.index}','tbimg11${i.index}','${gbb.goodsPid}','${gbb.tbUrl1}','${gbb.shopId1}','${gbb.moqPrice1}','${gbb.categoryid1}',0)" id ="tbimg1${i.index}">
						<%-- <br /><fmt:formatNumber value="${gbb.tbprice1/6.2}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;<a target='_blank' href="${gbb.tbUrl1}" class="a1" >Info</a> --%>
							<br />${gbb.moqPrice1}
							<br /><a target='_blank' href="${gbb.tbUrl1}" class="a1" >Info</a>${gbb.imgCheck1}
					</td>
					<td> 
						<img  width='140px;' title="${gbb.tbName2}" height='140px;' src="${gbb.tbImg2 }" style="cursor: pointer;${gbb.tbflagname == 'tb3'? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg0${i.index}','tbimg1${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','tbimg6${i.index}','tbimg7${i.index}','tbimg8${i.index}','tbimg9${i.index}','tbimg10${i.index}','tbimg11${i.index}','${gbb.goodsPid}','${gbb.tbUrl2}','${gbb.shopId2}','${gbb.moqPrice2}','${gbb.categoryid2}',0)" id ="tbimg2${i.index}">
							<%-- <br /><fmt:formatNumber value="${gbb.tbprice2/6.2}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;<a target='_blank' href="${gbb.tbUrl2}" class="a1" >Info</a> --%>
							<br />${gbb.moqPrice2}
							<br /><a target='_blank' href="${gbb.tbUrl2}" class="a1" >Info</a>${gbb.imgCheck2}
					</td>
					<td> 
						<img  width='140px;' title="${gbb.tbName3}" height='140px;' src="${gbb.tbImg3 }" style="cursor: pointer;${gbb.tbflagname == 'tb4'? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg0${i.index}','tbimg1${i.index}','tbimg2${i.index}','tbimg4${i.index}','tbimg5${i.index}','tbimg6${i.index}','tbimg7${i.index}','tbimg8${i.index}','tbimg9${i.index}','tbimg10${i.index}','tbimg11${i.index}','${gbb.goodsPid}','${gbb.tbUrl3}','${gbb.shopId3}','${gbb.moqPrice3}','${gbb.categoryid3}',0)" id ="tbimg3${i.index}">
						<%-- <br /><fmt:formatNumber value="${gbb.tbprice3/6.2}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;
						<a target='_blank' href="${gbb.tbUrl3}" class="a1" >Info</a> --%>
						<br />${gbb.moqPrice3}
						<br /><a target='_blank' href="${gbb.tbUrl3}" class="a1" >Info</a>${gbb.imgCheck3}
					</td>
					<td> 
						<img  width='140px;' title="" height='140px;' src="${gbb.tbImg4 }" style="cursor: pointer;${gbb.tbflagname == 'tb5'? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg0${i.index}','tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg5${i.index}','tbimg6${i.index}','tbimg7${i.index}','tbimg8${i.index}','tbimg9${i.index}','tbimg10${i.index}','tbimg11${i.index}','${gbb.goodsPid}','${gbb.tbUrl4}','${gbb.shopId4}','${gbb.moqPrice4}','${gbb.categoryid4}',0)" id ="tbimg4${i.index}">
							<br />${gbb.moqPrice4}
							<br /><a target='_blank' href="${gbb.tbUrl4}" class="a1" >Info</a>${gbb.imgCheck4}
					</td>
					<td> 
						<img  width='140px;' title="" height='140px;' src="${gbb.tbImg5 }" style="cursor: pointer;${gbb.tbflagname == 'tb6'? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg0${i.index}','tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg6${i.index}','tbimg7${i.index}','tbimg8${i.index}','tbimg9${i.index}','tbimg10${i.index}','tbimg11${i.index}','${gbb.goodsPid}','${gbb.tbUrl5}','${gbb.shopId5}','${gbb.moqPrice5}','${gbb.categoryid5}',0)" id ="tbimg5${i.index}">
						<br />${gbb.moqPrice5}
						<br /><a target='_blank' href="${gbb.tbUrl5}" class="a1" >Info</a>${gbb.imgCheck5}
					</td>
					
					
					
					<td> 
						<img  width='140px;' title="" height='140px;' src="${gbb.tbImg6 }" style="cursor: pointer;${gbb.tbflagname == 'tb6'? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg0${i.index}','tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','tbimg7${i.index}','tbimg8${i.index}','tbimg9${i.index}','tbimg10${i.index}','tbimg11${i.index}','${gbb.goodsPid}','${gbb.tbUrl6}','${gbb.shopId6}','${gbb.moqPrice6}','${gbb.categoryid6}',0)" id ="tbimg6${i.index}">
						<br />${gbb.moqPrice6}
						<br /><a target='_blank' href="${gbb.tbUrl6}" class="a1" >Info</a>${gbb.imgCheck6}
					</td>
					<td> 
						<img  width='140px;' title="" height='140px;' src="${gbb.tbImg7 }" style="cursor: pointer;${gbb.tbflagname == 'tb6'? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg0${i.index}','tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','tbimg6${i.index}','tbimg8${i.index}','tbimg9${i.index}','tbimg10${i.index}','tbimg11${i.index}','${gbb.goodsPid}','${gbb.tbUrl7}','${gbb.shopId7}','${gbb.moqPrice7}','${gbb.categoryid7}',0)" id ="tbimg7${i.index}">
						<br />${gbb.moqPrice7}
						<br /><a target='_blank' href="${gbb.tbUrl7}" class="a1" >Info</a>${gbb.imgCheck7}
					</td>
					<td> 
						<img  width='140px;' title="" height='140px;' src="${gbb.tbImg8 }" style="cursor: pointer;${gbb.tbflagname == 'tb6'? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg0${i.index}','tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','tbimg6${i.index}','tbimg7${i.index}','tbimg9${i.index}','tbimg10${i.index}','tbimg11${i.index}','${gbb.goodsPid}','${gbb.tbUrl8}','${gbb.shopId8}','${gbb.moqPrice8}','${gbb.categoryid8}',0)" id ="tbimg8${i.index}">
						<br />${gbb.moqPrice8}
						<br /><a target='_blank' href="${gbb.tbUrl8}" class="a1" >Info</a>${gbb.imgCheck8}
					</td>
					<td> 
						<img  width='140px;' title="" height='140px;' src="${gbb.tbImg9 }" style="cursor: pointer;${gbb.tbflagname == 'tb6'? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg0${i.index}','tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','tbimg6${i.index}','tbimg7${i.index}','tbimg8${i.index}','tbimg10${i.index}','tbimg11${i.index}','${gbb.goodsPid}','${gbb.tbUrl9}','${gbb.shopId9}','${gbb.moqPrice9}','${gbb.categoryid9}',0)" id ="tbimg9${i.index}">
						<br />${gbb.moqPrice9}
						<br /><a target='_blank' href="${gbb.tbUrl9}" class="a1" >Info</a>${gbb.imgCheck9}
					</td>
					<td> 
						<img  width='140px;' title="" height='140px;' src="${gbb.tbImg10 }" style="cursor: pointer;${gbb.tbflagname == 'tb6'? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg0${i.index}','tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','tbimg6${i.index}','tbimg7${i.index}','tbimg8${i.index}','tbimg9${i.index}','tbimg11${i.index}','${gbb.goodsPid}','${gbb.tbUrl10}','${gbb.shopId10}','${gbb.moqPrice10}','${gbb.categoryid10}',0)" id ="tbimg10${i.index}">
						<br />${gbb.moqPrice10}
						<br /><a target='_blank' href="${gbb.tbUrl10}" class="a1" >Info</a>${gbb.imgCheck10}
					</td>
					<td> 
						<img  width='140px;' title="" height='140px;' src="${gbb.tbImg11 }" style="cursor: pointer;${gbb.tbflagname == 'tb6'? 'border:6px solid red':'border:'}" 
							onclick="findImgId(this,'tbimg0${i.index}','tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','tbimg6${i.index}','tbimg7${i.index}','tbimg8${i.index}','tbimg9${i.index}','tbimg10${i.index}','${gbb.goodsPid}','${gbb.tbUrl11}','${gbb.shopId11}','${gbb.moqPrice11}','${gbb.categoryid11}',0)" id ="tbimg11${i.index}">
						<br />${gbb.moqPrice11}
						<br /><a target='_blank' href="${gbb.tbUrl11}" class="a1" >Info</a>${gbb.imgCheck11}
					</td>
					
					<c:if test="${similarityId!=''}">
						<td> 
							<input width='140px;' height='140px;' type="text" name="ylPid"  id="ylPid" value='' onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>
							<input id="bench${gbb.goodsPid}" style="height: 30px;width: 86px;float: right;" type="button" value="确认" onclick="updatePid('${gbb.goodsPid}','${ylbbPid}');">
						</td>
					</c:if>
					
					
					
					<%-- <td>${gbb.minImgCheck }</td> --%>
 					<%-- <td><input style="height:50px;" type="button" value="无对应" onclick="cancel('tbimg0${i.index}','tbimg1${i.index}','tbimg2${i.index}','tbimg3${i.index}','tbimg4${i.index}','tbimg5${i.index}','${gbb.goodsPid}',1)"></td> --%>
				</Tr>
			</c:forEach>
		</table>
		</br>
 	 	<div align="center">${pager }</div>
		
	</div>
</div>
</body>
</html>