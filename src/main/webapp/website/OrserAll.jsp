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
<title>老顾客跟踪</title>
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
	function FindOrder(value){
		window.open ("/cbtconsole/orderDetails/queryByOrderNo.do?orderNo="+value);
	}
	
	function infoSearch(value){
		var order = $("#order").val();
	
	
		window.location = "/cbtconsole/OldCustomShow/getCusOrder?usid="+value+"&order="+order;
		
	}
	$(function(){ 
		 $.ajax({
             type:"post",
             url:"/cbtconsole/OldCustomShow/getAlladm",
             dataType:"json",
             success : function(data){  
            	 $("#admName").append("<option value='-1'>销售选择</option>");
                	 $(data).each(function (index, item) {
                	 $("#admName").append("<option value="+item.admName+">"+item.admName+"</option>"); 
                	 });
                 }

             })
       
	});
	
</script>
</head>
<body>
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
	<div align="center" style="display: block;height: 30px;text-align: center;margin-bottom: 20px;">
	            <span style="line-height:25px;">订单号:</span><input type="text" id="order" value="${order==null?"":order}" class="rlcj" style="margin-right: 20px;"/> 
	           
				
				<input type="button" value="Search" class="rlcj" style="background: #7AB63F;padding: 0px 5px;color: #fff;margin-left: 10px;" onclick="infoSearch('${usid}');" />
				
	</div>
	
	<div style="width: 60%;min-width: 900px;max-width: 1400px;margin: 0 auto;">
		<table id="table1" align="center" border="1px" style="font-size: 13px;width:100%;" cellpadding="0" cellspacing="0" >
			<Tr>
				<th>订单号</th>
				<th>产品金额</th>
				<th>付款金额</th>
				<th>下单时间</th>
				<th>付款时间</th>
			</Tr>
			
			<c:forEach items="${orderAll }" var="gbb" varStatus="i">
				<Tr class="a" id="tr${gbb.orderNo}">
					<td><a href="javascript:void(0);" onclick="FindOrder('${gbb.orderNo}');">
						${gbb.orderNo}
						</a> 
					</td>
					<td> 
						${gbb.productCost}
					</td>
					<td> 
						${gbb.payPrice}
					</td>
					<td> 
						${gbb.createTime}
					</td>
					<td> 
						${gbb.orderpaytime}
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