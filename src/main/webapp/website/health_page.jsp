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
	
	function fnSelectInfo(){
		window.location = "/cbtconsole/customerServlet?action=findAllTaoBaoInfo&className=PictureComparisonServlet";
	}
	function fnWinSearch(goodsPid){
		window.open("/cbtconsole/PurchaseServlet?action=winSearch&className=Purchase&goodsPid="+goodsPid,"_blank")
/* 		$.post("/cbtconsole/customerServlet",
	  			{action:'winSearch',className:'PictureComparisonServlet',goodsPid:goodsPid},
	  			function(res){
	  				//alert("成功");
	  	});  */
	}
	
</script>
</head>
<body>
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
		<table id="table1" align="center" border="1px" style="font-size: 13px;" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>用户id</th>
				<th>访问url</th>
				<th>时间</th>
			</Tr>
			
			<c:forEach items="${gbbs }" var="gbb" varStatus="i">
				<Tr class="a" id="tr${gbb.userName}">
					<td> 
						${gbb.userName}
					</td>
					<td> 
						${gbb.url}
					</td>
					<td> 
						${gbb.createtime}
					</td>
				</Tr>
			</c:forEach>
		</table>
		</br>
 	 	<div align="center">${pager }</div>
		
	</div>
</div>
</body>
</html>