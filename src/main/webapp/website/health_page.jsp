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
<script type="text/javascript" src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>404跟踪</title>
<style>
*{word-wrap:break-word}html,body,h1,h2,h3,h4,h5,h6,hr,p,iframe,dl,dt,dd,ul,ol,li,pre,form,button,input,textarea,th,td,fieldset{margin:0;padding:0}ul,ol,dl{list-style-type:none}html,body{*position:static}html{font-family:sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%}address,caption,cite,code,dfn,em,th,var{font-style:normal;font-weight:400}input,button,textarea,select,optgroup,option{font-family:inherit;font-size:inherit;font-style:inherit;font-weight:inherit}input,button{overflow:visible;vertical-align:top;outline:none}body,th,td,button,input,select,textarea{font-family:Helvetica,Verdana,sans-serif,microsoft yahei,hiragino sans gb,helvetica neue,Helvetica,tahoma,arial,Verdana,sans-serif,wenquanyi micro hei,\5b8b\4f53;font-size:12px;color:#333;-webkit-font-smoothing:antialiased;-moz-osx-font-smoothing:grayscale}body{line-height:1.6}h1,h2,h3,h4,h5,h6{font-size:100%;font-weight:400}a,area{outline:none;blr:expression(this.onFocus=this.blur())}a{text-decoration:none;cursor:pointer}a:hover{text-decoration:none;outline:none}a.ie6:hover{zoom:1}a:focus{outline:none}a:hover,a:active{outline:none}:focus{outline:none}sub,sup{vertical-align:baseline}button,input[type=button],input[type=submit]{line-height:normal!important}img{border:0;vertical-align:middle}a img,img{-ms-interpolation-mode:bicubic}.img-responsive{max-width:100%;height:auto}*html{overflow:-moz-scrollbars-vertical;zoom:expression(function(ele){ele.style.zoom="1";document.execCommand("BackgroundImageCache",false,true)}(this))}header,footer,section,aside,details,menu,article,section,nav,address,hgroup,figure,figcaption,legend{display:block;margin:0;padding:0}time{display:inline}audio,canvas,video{display:inline-block;*display:inline;*zoom:1}audio:not([controls]){display:none}legend{width:100%;margin-bottom:20px;font-size:21px;line-height:40px;border:0;border-bottom:1px solid #e5e5e5}legend small{font-size:15px;color:#999}svg:not(:root){overflow:hidden}fieldset{border-width:0;padding:.35em .625em .75em;margin:0 2px;border:1px solid silver}input[type=number]::-webkit-inner-spin-button,input[type=number]::-webkit-outer-spin-button{height:auto}input[type=search]{-webkit-appearance:textfield;-moz-box-sizing:content-box;-webkit-box-sizing:content-box;box-sizing:content-box}input[type=search]::-webkit-search-cancel-button,input[type=search]::-webkit-search-decoration{-webkit-appearance:none}.cl:after,.clearfix:after{content:".";display:block;height:0;clear:both;visibility:hidden}.cl,.clearfix{zoom:1}
.a1{ color:#00afff;}
#table1 th,#table1 td{max-width:600px;}

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
	
	function infoSearch(){
		var userId = $("#userId").val();
		var timeFrom = $("#timeFrom").val();
		var timeTo = $("#timeTo").val();
		//state = $("#orderEnd").val();
		//window.location = "${ctx}/orderInfo/getOrders?state="+state+"&orderNo="+orderNo+"&timeFrom="+timeFrom+"&timeTo="+timeTo+"&page=1";
		window.location = "${ctx}/cbtconsole/customerServlet?action=getErrorInfo&className=PictureComparisonServlet&userId="+userId+"&timeFrom="+timeFrom+"&timeTo="+timeTo;
	}

	
</script>
</head>
<body>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
	<div align="center" style="display: block;height: 30px;text-align: center;margin-bottom: 20px;">
				<span style="line-height:25px;">用户id:</span><input type="text" id="userId" value="${userId}" class="rlcj" style="margin-right: 20px;"/>
				<span style="line-height:25px;">时间:</span> <input id="timeFrom" class="Wdate rlcj" style="width:120px;" type="text" value="${timeFrom }" onclick="WdatePicker({skin:'whyGreen',lang:'en'})" /><span> ~</span>
				<input id="timeTo" class="Wdate rlcj" type="text" style="width:120px;" value="${timeTo }" onfocus="WdatePicker({skin:'whyGreen',lang:'en'})" />
				<input type="button" value="Search" class="rlcj" style="background: #7AB63F;padding: 0px 5px;color: #fff;margin-left: 10px;" onclick="infoSearch();" />
	</div>
	<div style="width: 60%;min-width: 900px;max-width: 1400px;margin: 0 auto;">
		<table id="table1" align="center" border="1px" style="font-size: 13px;width:100%;" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>用户email</th>
				<th>用户id</th>
				<th>访问url</th>
				<th>时间</th>
			</Tr>
			
			<c:forEach items="${gbbs }" var="gbb" varStatus="i">
				<Tr class="a" id="tr${gbb.userName}">
					<td> 
						${gbb.email}
					</td>
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