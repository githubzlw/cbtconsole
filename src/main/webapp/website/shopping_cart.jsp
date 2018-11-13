<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<title>Shopping cart</title>
<script type="text/javascript">
  function fn(){
	  var userid = $("#userid").val();
	  var email = $("#email").val();
	  if (userid==""){
		  userid=0;
	  }
	  if(userid=="" && email=="") {
		  //alert(1);
		  document.getElementById("hcolor").style.display="block";
		  return;
		}else{
			  document.getElementById("hcolor").style.display="none";
		}
	  $("#table tr:gt(0)").remove();
	 // if(userid == ""){alert("Userid can not be empty");return;}
	  //if(email == ""){alert("email can not be empty");return;}
		$.post("/cbtconsole/WebsiteServlet",
  			{action:'getGoodsCar',className:'OrderwsServlet',userid:userid,email:email},
  			function(res){
			  var jsons = eval(res); 
			  //alert(jsons.length);
			  if(jsons.length==0){ 
				  return ;
			  }
			  var row = 1;
			  var json;
			  var n = 0;
			  var show =  show = jsons[jsons.length-1].showname;//商品分类名称
		 	  var sum_money = 0.0; //分类商品折扣后金额累加
			  var sum_class = 0.0; //分类商品折扣后金额累加
			  var num = 0;//同类商品数量累计
			  var indxrow=0;//行计数
			  var grade_discount = 1;
			  var isN
			  if(jsons.length>0){
				  grade_discount = parseFloat(jsons[0].grade_discount);
			  }
			  for (var i = jsons.length-1; i >= 0; i--) {
				  json = jsons[i];
				  //购物车商品分类 折扣小计
				  //如果没有到达最后一行数据，获取上一行小计数据
				  if(show != json.showname){
					  indxrow++;
					  var num = parseFloat(jsons[i+1].num);//同类商品数量累计
					  var deposit = 1.0;//折扣率
					  var freight=parseFloat(jsons[i+1].freight);//运费
					  var rate_price = parseFloat(jsons[i+1].rate_price); //折扣限额
					  var class_money = parseFloat(jsons[i+1].class_money); //同类商品总计金额
					  var rate = 0.0;//折扣后的金额
					  if(parseFloat(jsons[i+1].deposit_rates)!=0){
						  deposit = parseFloat(jsons[i+1].deposit_rates);
						  if(grade_discount<deposit){
							  deposit = grade_discount;
						  }
					  }
					  if(class_money>=rate_price){
						 rate = class_money*deposit;
					  }else{
						 rate = class_money;
					  }
					  sum_money =sum_money+ rate;//分类商品折扣后金额累加
					  sum_class =sum_class+ class_money; //分类商品折扣后金额累加
					  $("#table tr:eq("+(num)+")").after("<tr style='font-size:20px;text-align:right;'></tr>");
					  $("#table tr:eq("+(num+1)+")").append("<td colspan='17'>折扣限额：$"+rate_price+"&nbsp;&nbsp;小计：$"+class_money+"&nbsp;&nbsp;折扣后金额：$"+rate+"</td>");
					  num=0;
				  }
				  //如果到达最后一行，获取最后一行小计数据
				  if(i ==0){
					  indxrow=indxrow+1;
					  var deposit = 1.0;//折扣率
// 					  var freight=parseFloat(jsons[i+1].freight);//运费
					  var rate_price = parseFloat(jsons[i].rate_price);//折扣金额
					  var class_money = parseFloat(jsons[i].class_money); //同类商品总计金额
					  var rate = 0.0;//折扣后金额
					  if(parseFloat(jsons[i].deposit_rates)!=0){
						  deposit = parseFloat(jsons[i].deposit_rates);
						  if(grade_discount<deposit){
							  deposit = grade_discount;
						  }
					  }
					  if(class_money>=rate_price){
						   rate = class_money*deposit;
					  }else{
						 rate = class_money;
					  }
					  sum_money= sum_money+rate;
					  sum_class=sum_class+class_money;
					  $("#table tr:eq("+(num)+")").after("<tr style='font-size:20px;text-align:right;backgroundcolor:#dedede'></tr>");
					  $("#table tr:eq("+(num+1)+")").append("<td colspan='17'>折扣限额：$"+rate_price+"&nbsp;&nbsp;小计：$"+class_money+"&nbsp;&nbsp;折扣后金额：$"+rate.toFixed(2)+"</td>");
					  num=0;
				  }
				  
				  $("#table tr:eq("+(row-1)+")").after("<tr></tr>");
				  $("#table tr:eq("+row+")").append("<td>"+(i+1)+"</td>");
				  $("#table tr:eq("+row+") td:eq(0)").after("<td>"+json.id+"</td>");
				  $("#table tr:eq("+row+") td:eq(1)").after("<td>"+json.userId+"</td>");
				  $("#table tr:eq("+row+") td:eq(2)").after("<td><a href=\""+json.url+"\"  target='_Blank'>"+json.url+"</a></td>");
				  $("#table tr:eq("+row+") td:eq(3)").after("<td style='word-break:break-all'>"+json.name+"</td>");
				  $("#table tr:eq("+row+") td:eq(4)").after("<td><img width='80px' src='"+json.img_url+"'></td>");
			      $("#table tr:eq("+row+") td:eq(5)").after("<td align='center'><span contenteditable='true' tabindex='"+n+"'>"+json.notfreeprice+"</span><br/><button onclick='fnUpnumber(this,1,"+(indxrow+1)+","+json.id+")'>修改工厂价</button></td>"); 
				  n=n+1;
				  $("#table tr:eq("+row+") td:eq(6)").after("<td>"+json.freight+"</td>");
				  $("#table tr:eq("+row+") td:eq(7)").after("<td align='center'><span contenteditable='true' tabindex='"+n+"'>"+json.number+"</span><br/><button onclick='fnUpnumber(this,0,"+(indxrow+1)+","+json.id+")'>修改个数</button></td>");
				  n=n+1;
				  $("#table tr:eq("+row+") td:eq(8)").after("<td align='center'>修改体积：<br><input type='text' onblur='updateVolumeOrWeight(this,0,"+json.id+",this.value,\""+json.seilUnit+"\",\""+json.goodsUnit+"\")' style='width:140px;' value='"+json.width+"'/>"+
				  		"<span id='newgoodsnumber' style='display:none;'>"+json.number+"</span><br>"+
				  		"修改单个重量：<br><input type='text' onblur='updateVolumeOrWeight(this,1,"+json.id+",this.value,\""+json.seilUnit+"\",\""+json.goodsUnit+"\")' style='width:80px;' value='"+json.perWeight+"'/><br></td>");

				  $("#table tr:eq("+row+") td:eq(9)").after("<td>"+json.goods_type+"</td>");
				  $("#table tr:eq("+row+") td:eq(10)").after("<td>"+json.delivery_time+"</td>");
				  $("#table tr:eq("+row+") td:eq(11)").after("<td>"+json.remark+"</td>");
				  $("#table tr:eq("+row+") td:eq(12)").after("<td>"+json.createtime+"</td>");
				  $("#table tr:eq("+row+") td:eq(13)").after("<td>"+(json.freight_free==1?"免邮":"否")+"</td>");
				  $("#table tr:eq("+row+") td:eq(14)").after("<td>"+json.pWprice+"</td>");
				  $("#table tr:eq("+row+") td:eq(15)").after("<td>"+json.showname+"</td>");
				  
				  if(grade_discount< parseFloat(json.deposit_rates)){
				 	 $("#table tr:eq("+row+") td:eq(16)").after("<td>"+grade_discount+"</td>");
				  }else{
				 	 $("#table tr:eq("+row+") td:eq(16)").after("<td>"+json.deposit_rates+"</td>");
				  }
				  
			//	  $("#table tr:eq("+row+") td:eq(14)").after("<td><button onclick='fnUpnumber(0,"+(i+1)+","+json.id+")'>修改数量</button><button  onclick='fnUpnumber(1,"+(i+1)+","+json.id+")'>修改价格</button></td>");
				 show = json.showname;
				 num++;
				 indxrow++;
			}
			  
			 $("#table").append("<tr style='font-size:20px;text-align:right;backgroundcolor:#dedede'><td colspan='17'>总计金额：$"+Math.round(sum_class*100)/100+"&nbsp;&nbsp;折扣后金额：$"+Math.round(sum_money*100)/100+"</td></tr>");
	});
  }
  
/*   function fnClick(type,gid,id){
	  if(type==0){
		  var number_h = $("#table tr:eq("+id+") td:eq(7)");
		  var number = number_h.html();
		  number_h.html("<input type='text' value='"+number+"'>");
		  $("#table tr:eq("+id+") td:last button:eq(0)").html("保存数量");
		  $("#table tr:eq("+id+") td:last button:eq(0)").attr("onclick","fnUpnumber(0,"+id+","+gid+")");
	  }else{
		  var number_h = $("#table tr:eq("+id+") td:eq(6)");
		  var number = number_h.html();
		  number_h.html("<input type='text' value='"+number+"'>");
		  $("#table tr:eq("+id+") td:last button:eq(1)").html("保存价格");
		  $("#table tr:eq("+id+") td:last button:eq(1)").attr("onclick","fnUpnumber(1,"+id+","+gid+")");
	  }
  } */
  
  //8.30 修改重量和体积 lyb
	function updateVolumeOrWeight(o,flag, id,vw, seilUnit , goodsUnit){
		//flag 为0表示修改体积，flag为1表示修改重量
		var number = $(o).parent().find("span").html();
		$.ajax({
			url:"/cbtconsole/WebsiteServlet",
			data:{action:"updateVolumeOrWeight",className:"OrderwsServlet","flag":flag, "id":id, "vw":vw,"seilUnit":seilUnit,"goodsUnit":goodsUnit,"number":number},
			type:"post",
			dataType:"text",
			success:function(res){
				if(res==0){
					alert("修改失败");
				}
			},
			error:function(){
				alert("修改失败");
			}
		});
	}
  
  //修改价格和数量
  function fnUpnumber(val,type,id,gid){
	  var i = 0;
	  if(type==0){
		  i = 7;
	  }else{
		  i = 6;
	  }
	  var value = $(val).parent().find("span").html();
	  $.post("/cbtconsole/WebsiteServlet",
	  			{action:'updateGoods',className:'OrderwsServlet',gid:gid,type:type,value:value},
	  			function(res){
				 if(res > 0){
					 alert("保存成功");
		//			 $("#table tr:eq("+id+") td").eq(i).html(value);
				 }else{
					 alert("保存失败");
				 }
		}); 
	  $("#newgoodsnumber").html(value); 
  }
  

  
  $(function () {
      $("#userid").keyup(function () {
          //this.value = this.value.replace(/[^\d]/g, '').replace(/(\d{4})(?=\d)/g, "$1 ");
          this.value = this.value.replace(/[^\d]/g, '');
      })
  });
  
  function exportExcel(){
	  var str = "";
	  var tab = $("#table tr").length;
	  if(tab < 0){
		  alert("无数据导出");
		  return;
	  }
	  for(var i = 0;i < tab; i++){
		var tr = $("#table tr").eq(i); 
		var td = tr.find("td").length;
		for(var j = 0;j < td; j++){
			var td_ = tr.find("td").eq(j);
			var html = td_.html().replace(/,/g,".").replace(/&nbsp;/g," ");
			if(j==11){
				html="'"+html;
			}
			if(html == "img"){
				continue;
			}
			if(typeof(td_.find("a").attr("href")) != "undefined"){
				html = td_.find("a").attr("href");
			}
			if(typeof(td_.find("img:eq(0)").attr("src")) != "undefined"){
				continue;
			}
			if(typeof(td_.find("span:eq(0)").html()) != "undefined"){
				html=td_.find("span:eq(0)").html();
			}
			str += html + ",";
		}
	  	str += "\n";
	  }
	  str = encodeURIComponent(str);
	  var uri = 'data:text/csv;charset=utf-8,\ufeff' + str;
	  var downloadLink = document.createElement("a");
	  downloadLink.href = uri;
	  downloadLink.download = "shopping.csv";
	  document.body.appendChild(downloadLink);
	  downloadLink.click();
	  document.body.removeChild(downloadLink);
	  }
  

  
</script>
</head>
<body>
	<div>
		<table>
			<tbody>
				<tr>
					<td>userid:<input type="text" id="userid"  onkeydown='if(event.keyCode==13){fn()}'></td>
					<td>email:<input type="text" id="email"  onkeydown='if(event.keyCode==13){fn()}'> 
					 <input type="button" value="查询" onclick="fn();">&nbsp;<button onclick="exportExcel()">导出</button>
					 <span  id="hcolor" style="color:red;display:none;" >需要录入条件!</span></td>
				</tr>
			</tbody>
		</table>
		<br/>
		<div>
		<table id="table" border="1px" style="font-size: 13px;" bordercolor="#8064A2" cellpadding="0" cellspacing="0" >
			<tr>
				<td>序号</td>
				<td>id</td>
				<td>用户ID</td>
				<td width="240px">url</td>
				<td width="300px">title</td>
				<td>img</td>
				<td width="80px">工厂价</td>
 				<td width="20px">运费</td> 
				<td>商品数量</td>
				<td>重量和体积</td>
				<td>goods_type</td>
				<td>交期</td>
				<td width="80px">remark</td>
				<td>添加时间</td>
				<td>是否免邮</td>
				<td>批发价</td>
				<td>优惠类别</td>
				<td>优惠比例</td>
			</tr>
			
		</table>
	</div>
	</div>
</body>
</html>