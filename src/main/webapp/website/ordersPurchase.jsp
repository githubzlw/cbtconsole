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
<title>新采购画面</title>
<style>
.a1{ color:#00afff;}

.mod_pay3 { width: 400px; position: fixed;
	top: 200px; left: 30%;
	z-index: 1011; background: gray;
	padding: 5px; padding-bottom: 20px;
	z-index: 1011; border: 15px solid #33CCFF; }
.show_x { position: absolute; top: 5px; right: 15px; text-decoration: none; }
.show_h3 { height: 20px; text-align: left; }
.remark{border-color: ccff66; width: 170px; height: 20px;}
.remarktwo{border-color: ccff66; width: 170px; height: 60px;}
	
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

/* 	function findImgId(obj,obj1,obj2,obj3,tbPid,tburl,tbprice,tbname,delFlag,canl){
		
		var tb0 = $(obj).attr("id");
 		$("#"+tb0).css("border","6px solid red"); 
 		$("#"+canl).css("background","");
 		
	} */
	function findImgId(obj,canl){
		
		var tb0 = $(obj).attr("id");
 		$("#"+tb0).css("border","6px solid red"); 
 		$("#"+canl).css("background","");
 		
	}
	
	function cancel(canl,orderNoIndex,orderNo){
 		
		var tl= $("#table1").find("tr").length;
		for(var i=0;i<tl;i++){
			var trOrderNo= $("#table1 tr:eq("+i+")").attr("id");
			if(orderNoIndex==trOrderNo){
				var tdlen = $("#table1").find("tr:eq("+i+")").children("td").length;
 				for(var j=0;j<tdlen;j++){
					var tdImg = $("#table1 tr:eq("+i+") td:eq("+j+")").find("img").attr("id");
					if(tdImg=="undefined" || tdImg=="" || tdImg==null){
					}else{
						$("#"+tdImg).css("border",""); 
					}
				}
			}
		}
		$("#"+canl).css("background","red");
		
/*  		$.post("/cbtconsole/customerServlet",
	  			{action:'cancelChangeGoodData',className:'OrdersPurchaseServlet',orderNo:orderNo},
	  			function(res){
	  	}); */
	}
	
	function fnSave(){

/* 		var page1 = document.getElementById("page1").value;
		var page = Number(page1);
		page = page+1;*/
 		var ary=new Array(); 

		var tl= $("#table1").find("tr").length;
		for(var i=0;i<tl;i++){
			var trOrderNo= $("#table1 tr:eq("+i+")").attr("id");
				var selVal = $("#table1 tr:eq("+i+")").children("td").find("select").val();

				if(selVal==2){
					var tdlen = $("#table1 tr:eq("+i+")").children("td").length;
					for(var j=1;j<tdlen;j++){
						var imgSty = $("#table1 tr:eq("+i+") td:eq("+j+")").find("img").attr("style");
						if(imgSty=="undefined" || imgSty=="" || imgSty==null){
							
						}else{
							var imgStyBorder = imgSty.split(":"); 
						}
						if(imgStyBorder[2]=="undefined" || imgStyBorder[2]=="" || imgStyBorder[2]==null){
							
						}else{
							
							var orderNo = $("#table1 tr:eq("+i+")").children("td").find("input[id='orderNo']").val();
							var importUrl = $("#table1 tr:eq("+i+")").children("td").find("input[id='importUrl']").val();
							var goodsId = $("#table1 tr:eq("+i+")").children("td").find("input[id='goodsId']").val();
							var priceRmb = $("#table1 tr:eq("+i+")").children("td").find("input[id='priceRmb']").val();
							
							
							var ylbbUrl = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id*='ylbbUrl']").val();
							//alert(ylbbUrl);
							var tb1 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao1']").val();
							//alert(taobao1);
							var tb2 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao2']").val();
							//alert(taobao2);
							var tb3 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao3']").val();
							//alert(taobao3);
							var tb4 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao4']").val();
							//alert(taobao4);
							var tb5 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao5']").val();
							//alert(taobao4);
							var tb6 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao6']").val();
							//alert(taobao4);
							if(ylbbUrl=="undefined" || ylbbUrl=="" || ylbbUrl==null){
							}else {
								ary.push(orderNo+","+importUrl+","+goodsId+","+ylbbUrl+","+priceRmb);
							}
							if(tb1=="undefined" || tb1=="" || tb1==null){
							}else{
								ary.push(orderNo+","+importUrl+","+goodsId+","+tb1+","+priceRmb);
							}
							if(tb2=="undefined" || tb2=="" || tb2==null){
							}else{
								ary.push(orderNo+","+importUrl+","+goodsId+","+tb2+","+priceRmb);
							}
							if(tb3=="undefined" || tb3=="" || tb3==null){
							}else {
								ary.push(orderNo+","+importUrl+","+goodsId+","+tb3+","+priceRmb);
							}
							if(tb4=="undefined" || tb4=="" || tb4==null){
							}else{
								ary.push(orderNo+","+importUrl+","+goodsId+","+tb4+","+priceRmb);
							}
							if(tb5=="undefined" || tb5=="" || tb5==null){
							}else{
								ary.push(orderNo+","+importUrl+","+goodsId+","+tb5+","+priceRmb);
							}
							if(tb6=="undefined" || tb6=="" || tb6==null){
							}else{
								ary.push(orderNo+","+importUrl+","+goodsId+","+tb6+","+priceRmb);
							}
						}
					}
					
				}
			}
				
/*  			for(var i=0;i<ary.length;i++){
				var orderNo=ary[i].split(',')[0];
				var importUrl=ary[i].split(',')[1];
				var goodsId=ary[i].split(',')[2];
				var changeFlag=ary[i].split(',')[3];
				var priceRmb=ary[i].split(',')[4];
					
				$.ajax({
					type:'POST',
					async:false,
					url:'/cbtconsole/customerServlet?action=pageSaveChangeGoodData&className=OrdersPurchaseServlet',
					data:{orderNo:orderNo,importUrl:importUrl,goodsId:goodsId,changeFlag:changeFlag,priceRmb:priceRmb},
					success:function(){	
					
					}
				});
			}   */
			
 			var list= new Array();
			for(var i=0;i<ary.length;i++){
				var map = {}; 
				map["orderNo"] = ary[i].split(',')[0];
				map["importUrl"] = ary[i].split(',')[1];
				map["goodsId"] = ary[i].split(',')[2];
				map["changeFlag"] = ary[i].split(',')[3];
				map["priceRmb"] = ary[i].split(',')[4];
				list[i] = map;
			}
			var map2={};  //主
			map2['list']=list;  

			$.ajax({
				type:'POST',
				async:false,
				url:'/cbtconsole/customerServlet?action=pageSaveChangeGoodData&className=OrdersPurchaseServlet',
				data:{"data":JSON.stringify(map2)},
				success:function(){	
					window.open("/cbtconsole/customerServlet?action=findChangeGoodsInfo&className=OrdersPurchaseServlet");
				}
			}); 
			
 			
			
		
	}
	
	function fnSelectInfo(){
		window.location = "/cbtconsole/customerServlet?action=findAllTaoBaoInfo&className=PictureComparisonServlet";

	}
	
	function fnSearch(name,aliUrl,goodName,goodId,orderNo){
		
/* 		var goodName = goodName.replace("热销","").replace("热卖","").replace("顶级","").replace("出口","");
		var goodName = goodName.replace(goodName.replace(/[^0-9]/ig,""),"");
		goodName=goodName.substring(0,4); */
		//goodName=goodName.slice(-4);
		
		var tl= $("#table1").find("tr").length;
		for(var i=0;i<tl;i++){
			var id = $("#table1 tr:eq("+i+")").children("td").find("input[id*='nameText']").attr("id");
			if(name==id){
				goodName= $("#table1 tr:eq("+i+")").children("td").find("input[id*='nameText']").val();
			}
		}
		//alert(goodName);
		
		

		window.location = "/cbtconsole/WebsiteServlet?action=doPost&className=GoodsWebsiteServlet&srt=order-desc&keyword="+goodName+"&aliUrl="+aliUrl+"&goodId="+goodId+"&orderNo="+orderNo;
	}
	
	function fnLohSearch(similarityId,imgpath,aliUrl,goodId,orderNo,flag){
		
/* 		var tl= $("#table1").find("tr").length;
		for(var i=0;i<tl;i++){
			var id = $("#table1 tr:eq("+i+")").children("td").find("select[id*='similarityId']").attr("id");
			if(similarityId==id){
				index= $("#table1 tr:eq("+i+")").children("td").find("select[id*='similarityId']").val();
			}
		} */
		//aliUrl="http://www.aliexpress.com/item/Sanwony-New-Sexy-Women-Summer-Long-Sleeves-Deep-V-Floral-Print-Boho-Jumpsuit-For-Women-Clothing/32354082716.html";
		//alert(index);
		//window.open("/cbtconsole/customerServlet?action=localhostPtSearch&className=OrdersPurchaseServlet&imgpath="+imgpath+"&aliUrl="+aliUrl+"&goodId="+goodId+"&orderNo="+orderNo+"&index="+index);
		window.open("/cbtconsole/customerServlet?action=localhostPtSearchs&className=OrdersPurchaseServlet&imgpath="+imgpath+"&aliUrl="+aliUrl+"&goodId="+goodId+"&orderNo="+orderNo+"&flag="+flag);
	}
	
 	function fnFtSearch(aliUrl,goodId,orderNo){
		
		//window.location = "/cbtconsole/customerServlet?action=taobaoFtSearch&className=OrdersPurchaseServlet&aliUrl="+aliUrl+"&goodId="+goodId+"&orderNo="+orderNo;
		window.open("/cbtconsole/customerServlet?action=taobaoFtSearch&className=OrdersPurchaseServlet&aliUrl="+aliUrl+"&goodId="+goodId+"&orderNo="+orderNo);
	} 
	
			
			
	function fnAddGoodsSource(aliPid,aliUrl){
		
		//$("#rlname").val('');
		$("#rlprice").val('');
		$("#rlresource").val('');
		
		var rfddd = document.getElementById("rfddd");
		rfddd.style.display = "block";
		
		var hdaliPid = document.getElementById("hdaliPid");
		hdaliPid.value = aliPid;
		var hdaliUrl = document.getElementById("hdaliUrl");
		hdaliUrl.value = aliUrl;
		
	}
	
	function AddResource(){
		
		var hdaliPid=$("#hdaliPid").val();
		var hdaliUrl=$("#hdaliUrl").val();
		var rlname=$("#rlname").val();
		var rlprice=$("#rlprice").val();
		var rlpurl=$("#rlresource").val();
		var rlpimg=$("#rlpimg").val();
		var fdStart = rlpurl.indexOf("http");
		if(fdStart==-1){
			alert("货源链接必须以http开头！");
		}else if(rlpimg==null || rlpimg== "" || rlpimg.indexOf(".")<=-1){
			alert("图片不能为空");
		}else{
			$.ajax({
				type:'POST',
				url:'/cbtconsole/customerServlet?action=addSource&className=OrdersPurchaseServlet',
				data:{hdaliPid:hdaliPid,hdaliUrl:hdaliUrl,rlprice:rlprice,rlpurl:rlpurl,rlpimg:rlpimg},
				success:function(){
					FncloseOut();
					location.reload();
				}
			});
		}
	}
	
	function FncloseOut(){
		document.getElementById("rfddd").style.display = "none";
	}
	
	function fnChange(obj,orderNo,userid,aligoodsid,aliurl,alisprice,useCount,orderNo1,od_id,carid,currency){

		var tl= $("#table1").find("tr").length;
		for(var i=0;i<tl;i++){
			var trOrderNo= $("#table1 tr:eq("+i+")").attr("id");
			if(orderNo==trOrderNo){
				var flagBorder = 0;
				var tdlen = $("#table1 tr:eq("+i+")").children("td").length;
				for(var j=0;j<tdlen;j++){
					var tdImgStyle = $("#table1 tr:eq("+i+") td:eq("+j+")").find("img").attr("style");
					if(tdImgStyle=="undefined" || tdImgStyle=="" || tdImgStyle==null){
					}else{
						var tbBorder = tdImgStyle.split(":");
						if(tbBorder[2]=="undefined"|| tbBorder[2]=="" || tbBorder[2]==null){
						}else{
							flagBorder = flagBorder+1;
						}
					}
					
				}
				if(obj==1 && flagBorder>1){
					alert("只选一个正式货源");
				}
				if(obj==2 && flagBorder==0){
					alert("为何没替代货源");
				}
				if(obj==3 && flagBorder>=1){
					alert("不能有货源");
				}
				if(obj==1 && flagBorder==1){
					var tdlen1 = $("#table1 tr:eq("+i+")").children("td").length;
					for(var j=0;j<tdlen1;j++){
						var tdImgStyle = $("#table1 tr:eq("+i+") td:eq("+j+")").find("img").attr("style");
						if(tdImgStyle=="undefined" || tdImgStyle=="" || tdImgStyle==null){
						}else{
							var tbBorder = tdImgStyle.split(":");
							if(tbBorder[2]=="undefined"|| tbBorder[2]=="" || tbBorder[2]==null){
							}else{
								//flagBorder = flagBorder+1;
								var aliname = $("#table1 tr:eq("+i+")").children("td").find("input[id='goodsName']").val();
								
								var ylbbUrl = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id*='ylbbUrl']").val();
								//alert(ylbbUrl);
								var tb1 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao1']").val();
								//alert(taobao1);
								var tb2 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao2']").val();
								//alert(taobao2);
								var tb3 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao3']").val();
								//alert(taobao3);
								var tb4 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao4']").val();
								//alert(taobao4);
								var tb5 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao5']").val();
								//alert(taobao4);
								var tb6 = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='taobao6']").val();
								//alert(taobao4);
								if(ylbbUrl=="undefined" || ylbbUrl=="" || ylbbUrl==null){
								}else {
									var purl = ylbbUrl;
									var pname = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id*='aliSourceName']").val();
									var pprice = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id*='aliSourcePrice']").val();
									var pimg = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id*='aliSourceImgUrl']").val();
								}
								if(tb1=="undefined" || tb1=="" || tb1==null){
								}else{
									//ary.push(orderNo+","+importUrl+","+goodsId+","+tb1);
									var purl = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbUrl']").val();
									var pname = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbName']").val();
									var pprice = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbprice']").val();
									var pimg = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbImg']").val();
								}
								if(tb2=="undefined" || tb2=="" || tb2==null){
								}else{
									//ary.push(orderNo+","+importUrl+","+goodsId+","+tb2);
									var purl = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbUrl1']").val();
									var pname = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbName1']").val();
									var pprice = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbprice1']").val();
									var pimg = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbImg1']").val();
								}
								if(tb3=="undefined" || tb3=="" || tb3==null){
								}else {
									//ary.push(orderNo+","+importUrl+","+goodsId+","+tb3);
									var purl = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbUrl2']").val();
									var pname = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbName2']").val();
									var pprice = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbprice2']").val();
									var pimg = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbImg2']").val();
								}
								if(tb4=="undefined" || tb4=="" || tb4==null){
								}else{
									var purl = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbUrl3']").val();
									var pname = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbName3']").val();
									var pprice = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbprice3']").val();
									var pimg = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbImg3']").val();
								}
								if(tb5=="undefined" || tb5=="" || tb5==null){
								}else{
									var purl = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbUrl4']").val();
									var pname = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbName4']").val();
									var pprice = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbprice4']").val();
									var pimg = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbImg4']").val();
								}
								if(tb6=="undefined" || tb6=="" || tb6==null){
								}else{
									var purl = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbUrl5']").val();
									var pname = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbName5']").val();
									var pprice = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbprice5']").val();
									var pimg = $("#table1 tr:eq("+i+") td:eq("+j+")").find("input[id='tbImg5']").val();
								}
							}
						}
						
					}
					
					$.ajax({
						type:'POST',
						//async:false,
						url:'/cbtconsole/customerServlet?action=addOrderProductSource&className=OrdersPurchaseServlet',
						data:{userid:userid,aligoodsid:aligoodsid,aliurl:aliurl,pimg:pimg,alisprice:alisprice,aliname:aliname,useCount:useCount,orderNo:orderNo1,
							od_id:od_id,carid:carid,pprice:pprice,purl:purl,currency:currency,pname:pname},
						success:function(){	
							alert("数据保存成功");
						}
					});
					
				}
				
			}
		}
		

	}
	
	function deleteSource(goods_p_url,goods_url){
		if(window.confirm('是否需要删除该替换商品？')){
			console.log("goods_p_url="+goods_p_url);
			console.log("goods_url="+goods_url);
			$.ajax({
				type:"post",        
				url:"/cbtconsole/warehouse/deleteSource",
				dataType:"text",
				data:{goods_p_url:goods_p_url,goods_url:goods_url},
				success : function(data){
					if(data>0){
						location.reload();
					}else{
						alert("删除替换商品失败");
					}
				}   
			}); 
         }
	}
	
</script>
</head>
<body style="background:#000; color:#fff;">
<div align="left"><span onmousemove="$(this).css('color','#ff6e02');" onmouseout="$(this).css('color','#7AB63F');" onclick="window.location.href=history.go(-1)" style="color: #7AB63F;cursor: pointer;"><em  style="font-size: 19px;">&nbsp;</em></span></div>	<br>
<div align="center">
	</div>
	<div>
		<table id="table1" align="center" border="1px" style="font-size: 13px;" width="800" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<c:forEach items="${gbbs }" var="gbb" varStatus="j">
				<Tr class="a" id="tr${j.index}" >
					<td>
						订单ID:${gbb.orderNo}
					</td>
					<td>
						客户ID:${gbb.userName}
					</td>
					<td>
						Email:${gbb.email}
					</td>
					<td>
						下单时间:${gbb.createtime}
					</td>
					<td>
						产品金额:${gbb.productCost}&nbsp;&nbsp;${gbb.currency}
					</td>
				</tr>
					<c:forEach items="${gbb.pictureList }" var="gbbpl" varStatus="i">
					<tr id="${i.index}${gbbpl.orderNo}">
						<td id="canl${i.index}${gbbpl.orderNo}"> 
							<img  width='200px' title="" height='200px;' src="${gbbpl.imgpath }" style='' >
							<br />${gbb.currency}&nbsp;${gbbpl.price}&nbsp;&nbsp;&nbsp;<a target='_blank' href="${gbbpl.url}" class="a1" >Info</a>
							&nbsp;RMB&nbsp;${gbbpl.priceRmb}
							<br />商品号:${gbbpl.goodscarid}
							<br />${gbbpl.goodsName}
							<br />${gbbpl.goodsnamecn}
							<br />
	 				 		<select id="similarity"  style="width:100px;" onchange="fnChange(this.value,'${i.index}${gbbpl.orderNo}',
							'${gbb.userName}','${gbbpl.pId}','${gbbpl.url}','${gbbpl.price}','${gbbpl.useCount }',
							'${gbbpl.orderNo}','${gbbpl.order_details_id}','${gbbpl.goodscarid}','${gbb.currency}')" >
									<option selected="selected"></option>
									<option value="1">有货</option>
									<option value="2">替换</option>
									<option value="3">无货</option>
							</select>
							
							<br />
							<input style="height:30px;width:100px;" type="button" value="无对应" onclick="cancel('canl${i.index}${gbbpl.orderNo}','${i.index}${gbbpl.orderNo}','${gbbpl.orderNo}')">
							<br />
							<input style="height:30px;width:100px;"  type="button" value="手工录入" onclick="fnAddGoodsSource('${gbbpl.pId}','${gbbpl.url}')">
							<!-- 	</td> -->
							<input type="hidden" id="goodsName" value="${gbbpl.goodsName}">
							<input type="hidden" id="orderNo" value="${gbbpl.orderNo}">
							<input type="hidden" id="importUrl" value="${gbbpl.url}">
							<input type="hidden" id="goodsId" value="${gbbpl.pId}#${gbbpl.goodscarid}#${gbbpl.order_details_id}">
							<input type="hidden" id="priceRmb" value="${gbbpl.priceRmb}">
						</td>
						<!-- 1688 start -->
						<c:forEach items="${gbb.aliSourceList }" var ="asl" varStatus="k" >
							<c:if test="${ gbbpl.url==asl.aligSourceUrl && gbbpl.goodscarid==asl.goodscarid}">
							
								<td> 
										<img  width='200px' title="" height='200px;' src="${asl.aliSourceImgUrl }"  style="cursor: pointer;border:6px solid red;" 
										onclick="findImgId(this,'canl${i.index}${gbbpl.orderNo}')" id ="tbimg00${i.index}${k.index}${gbbpl.orderNo}">
										<br />&nbsp;RMB&nbsp;<fmt:formatNumber value="${asl.aliSourcePrice}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;
										<a target='_blank' href="${asl.aliSourceUrl}" class="a1" >Info</a>
										<a  href="javascript:deleteSource('${asl.aliSourceUrl}','${asl.aligSourceUrl}');" class="a1" >Delete</a>
										<br />${asl.aliSourceName}
									<input type="hidden" id="ylbbUrl${k.index}" value="${asl.aliSourceUrl}">
									
									<input type="hidden" id="aliSourceImgUrl${k.index}" value="${asl.aliSourceImgUrl}">
									<input type="hidden" id="aliSourceName${k.index}" value="${asl.aliSourceName}">
									<input type="hidden" id="aliSourcePrice${k.index}" value="${asl.aliSourcePrice}">
								</td>
							</c:if>
						</c:forEach>
						 <td> 
							<img  width='200px' title="" height='200px;' src="${gbbpl.tbImg }"  style="cursor: pointer;" onclick="findImgId(this,'canl${i.index}${gbbpl.orderNo}')" id ="tbimg0${i.index}${gbbpl.orderNo}">
								<br />&nbsp;RMB&nbsp;<fmt:formatNumber value="${gbbpl.tbprice}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;<a target='_blank' href="${gbbpl.tbUrl}" class="a1" >Info</a>
								<br />${gbbpl.tbName}
								<input type="hidden" id="taobao1" value="tb1">
								
								<input type="hidden" id="tbUrl" value="${gbbpl.tbUrl}">
								<input type="hidden" id="tbImg" value="${gbbpl.tbImg}">
								<input type="hidden" id="tbName" value="${gbbpl.tbName}">
								<input type="hidden" id="tbprice" value="${gbbpl.tbprice}">
								
						</td>
						
						<td> 
								<img  width='200px' title="" height='200px;' src="${gbbpl.tbImg1 }" style="cursor: pointer;" 
								onclick="findImgId(this,'canl${i.index}${gbbpl.orderNo}')" id ="tbimg1${i.index}${gbbpl.orderNo}">
							<br />&nbsp;RMB&nbsp;<fmt:formatNumber value="${gbbpl.tbprice1}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;<a target='_blank' href="${gbbpl.tbUrl1}" class="a1" >Info</a>
							<br />${gbbpl.tbName1}
									<input type="hidden" id="taobao2" value="tb2">
									
									<input type="hidden" id="tbUrl1" value="${gbbpl.tbUrl1}">
									<input type="hidden" id="tbImg1" value="${gbbpl.tbImg1}">
									<input type="hidden" id="tbName1" value="${gbbpl.tbName1}">
									<input type="hidden" id="tbprice1" value="${gbbpl.tbprice1}">
						</td>
						
						<td> 
								<img  width='200px' title="" height='200px;' src="${gbbpl.tbImg2 }" style="cursor: pointer;${gbbpl.tbFlag2!=null? 'border:6px solid red':'border:'}" 
								onclick="findImgId(this,'canl${i.index}${gbbpl.orderNo}')" id ="tbimg2${i.index}${gbbpl.orderNo}">
								<br />&nbsp;RMB&nbsp;<fmt:formatNumber value="${gbbpl.tbprice2}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;<a target='_blank' href="${gbbpl.tbUrl2}" class="a1" >Info</a>
								<br />${gbbpl.tbName2}
									<input type="hidden" id="taobao3" value="tb3">
									
									<input type="hidden" id="tbUrl2" value="${gbbpl.tbUrl2}">
									<input type="hidden" id="tbImg2" value="${gbbpl.tbImg2}">
									<input type="hidden" id="tbName2" value="${gbbpl.tbName2}">
									<input type="hidden" id="tbprice2" value="${gbbpl.tbprice2}">
						</td>
						
						<td> 
								<img  width='200px' title="" height='200px;' src="${gbbpl.tbImg3 }" style="cursor: pointer;${gbbpl.tbFlag3!=null? 'border:6px solid red':'border:'}" 
								onclick="findImgId(this,'canl${i.index}${gbbpl.orderNo}')" id ="tbimg3${i.index}${gbbpl.orderNo}">
							<br />&nbsp;RMB&nbsp;<fmt:formatNumber value="${gbbpl.tbprice3}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;
							<a target='_blank' href="${gbbpl.tbUrl3}" class="a1" >Info</a>
							<br />${gbbpl.tbName3}
									<input type="hidden" id="taobao4" value="tb4">
									
									<input type="hidden" id="tbUrl3" value="${gbbpl.tbUrl3}">
									<input type="hidden" id="tbImg3" value="${gbbpl.tbImg3}">
									<input type="hidden" id="tbName3" value="${gbbpl.tbName3}">
									<input type="hidden" id="tbprice3" value="${gbbpl.tbprice3}">
						</td>
						<td> 
								<img  width='200px' title="" height='200px;' src="${gbbpl.tbImg4}" style="cursor: pointer;${gbbpl.tbFlag4!=null? 'border:6px solid red':'border:'}" 
								onclick="findImgId(this,'canl${i.index}${gbbpl.orderNo}')" id ="tbimg4${i.index}${gbbpl.orderNo}">
							<br />&nbsp;RMB&nbsp;<fmt:formatNumber value="${gbbpl.tbprice4}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;
							<a target='_blank' href="${gbbpl.tbUrl4}" class="a1" >Info</a>
							<br />${gbbpl.tbName4}
									<input type="hidden" id="taobao5" value="tb5">
									
									<input type="hidden" id="tbUrl4" value="${gbbpl.tbUrl4}">
									<input type="hidden" id="tbImg4" value="${gbbpl.tbImg4}">
									<input type="hidden" id="tbName4" value="${gbbpl.tbName4}">
									<input type="hidden" id="tbprice4" value="${gbbpl.tbprice4}">
						</td>
						<td> 
								<img  width='200px' title="" height='200px;' src="${gbbpl.tbImg5 }" style="cursor: pointer;${gbbpl.tbFlag5!=null? 'border:6px solid red':'border:'}" 
								onclick="findImgId(this,'canl${i.index}${gbbpl.orderNo}')" id ="tbimg5${i.index}${gbbpl.orderNo}">
							<br />&nbsp;RMB&nbsp;<fmt:formatNumber value="${gbbpl.tbprice5}" pattern="#0.00" type="number" maxFractionDigits="2" />&nbsp;&nbsp;&nbsp;
							<a target='_blank' href="${gbbpl.tbUrl5}" class="a1" >Info</a>
							<br />${gbbpl.tbName5}
									<input type="hidden" id="taobao5" value="tb6">
									
									<input type="hidden" id="tbUrl5" value="${gbbpl.tbUrl5}">
									<input type="hidden" id="tbImg5" value="${gbbpl.tbImg5}">
									<input type="hidden" id="tbName5" value="${gbbpl.tbName5}">
									<input type="hidden" id="tbprice5" value="${gbbpl.tbprice5}">
						</td>
						
					</tr>
					</c:forEach>
			</c:forEach>
		</table>
		<br />
		<div style="width:580px; margin:auto;" >
			<button onclick="fnSave()"  style="width:210px;height:40px; float: left;">给客户写信,建议替代</button><p style="color: red;height: 40px;line-height: 40px;font-size: 13px;">(按钮按下，可能会花一点时间，请不要重复点击。)</p>
		</div>
		
		<div class="mod_pay3" style="display:none;" id="rfddd" >
		<center><h3 class="show_h3">录入替代货源</h3>
			<div><a href="javascript:void(0)" class="show_x" onclick="FncloseOut()">╳</a></div>
			<!-- <div>名字：<input type="text" name="rlname" id="rlname" class="remark" /></div> -->
			<div>价格：<input type="text" name="rlprice" id="rlprice" class="remark" /></div>
			<div>货源：<textarea name="rlresource" id="rlresource" class="remarktwo"></textarea></div>
			<div>图片：<textarea name="rlpimg" id="rlpimg" class="remarktwo"></textarea></div>
			<input type="hidden" id="hdaliPid" name="hdaliPid"/>
			<input type="hidden" id="hdaliUrl" name="hdaliUrl"/>
			<input type="button" id="idAddResource" value="提交" onclick="AddResource();"style="width: 90px; height: 40px; margin-top:20px;"/>
			<input type="button" value="取消" onclick="FncloseOut();"style="width: 90px; height: 40px;"/>
		</center>
		</div>


<%--  	 	<div align="center">${pager }
 	 	</div>
		<input type ="hidden" id="page1" name="page1" value="${page}"> --%>
	</div>
</body>
</html>