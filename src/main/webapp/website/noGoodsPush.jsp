<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script type="text/javascript" src="/cbtconsole/js/lhgdialog/lhgdialog.js"></script>
<style type="text/css">
.main1{
	display:none;
    width: 500px;
    height: 300px;
    position: fixed;
    left: 0px;
    top: 0px;
    background-color: rgba(242, 242, 242, 1);
    box-sizing: border-box;
    border-width: 1px;
    border-style: solid;
    border-color: rgba(153, 153, 153, 1);
    border-radius: 0px;
    
} 
.mailisel{width:120px;height:25px;padding:2px;border:1px solid #ddd;}
    .mailtab{border-collapse:collapse;margin:20px auto 10px;font-size:18px;}
    .maitit{background: #138cdd;padding:5px 0;color:#fff;position:relative;}
    .maiclo{display:inline-block;font-size:15px;position:absolute;right:5px;top:4px;z-index:2;cursor:pointer;}
    .mailibtn{width: 116px;
    height: 33px;
    background: inherit;
    background-color: rgba(22, 155, 213, 1);
    border: none;
    border-radius: 5px;color:#fff;margin-top:10px;}
</style>
<script type="text/javascript">
var len = 0; //货源数量
var json = ""; //货源信息
var suc = 0; //成功个数
var fail = 0; //失败个数
$(document).ready(function(){
	searchNoGoods();
	
	//推送
	$("#push").click(function(){
		suc = 0; fail=0;
		if(len==0){
			alert("没有可推送的货源");
			return ;
		}
		$("#push_msg").css("display","inline");
		for(var i=0;i<len;i++){
			$.ajax({
				url:"/cbtconsole/WebsiteServlet?action=pushGoods&className=NoGoodsPushServlet&carUrl="+json[i].carUrl+"&rand="+Math.random(),
				type:"post",
				dataType:"text",
				async:false,
				success:function(d){
					if(d=="1" || d==1){
						suc++;
						$("#succ").html(suc);
					}else{
						fail++;
						$("#fail").html(fail);
					}
				},
				error:function(){
					fail++;
					$("#fail").html(fail);
				}
			}); 
		}
	});
	
	//手动输入链接推送
	$("#push2").click(function(){
		var url = $("#goods_url").val();
		if(url==""){return ;}
		suc = 0; fail=0;
		$("#push_msg").css("display","inline");
		$.ajax({
			url:"/cbtconsole/WebsiteServlet?action=pushGoods&className=NoGoodsPushServlet&carUrl="+url,
			type:"post",
			dataType:"text",
			async:false,
			success:function(d){
				if(d=="1" || d==1){
					suc++;
					$("#succ").html(suc);
				}else{
					fail++;
					$("#fail").html(fail);
				}
			},
			error:function(){
				fail++;
				$("#fail").html(fail);
			}
		});
	});
	
});



function searchNoGoods(){
	$("#tab tbody").html("");
	$.ajax({
		url : "/cbtconsole/WebsiteServlet?action=selectNoGoods&className=NoGoodsPushServlet",
		type:"post",
		dataType:"json",
		success:function(data){
			len = data.length;
			json = data;
			if(data.length>0){
				$("#total").html(len);
				for(var i=0;i<data.length;i++){
					$("#tab tbody").append("<tr bgcolor='"+(i%2==0 ? '#FDFCDE' : '#FFE8E2')+"' height='70px'><td>"+(i+1)+"</td>"+
							"<td ><a href='"+data[i].carUrl+"' target='_blank'><img src='"+data[i].img+"' width='60px' height='60px'/></a></td>"+
							"<td >"+data[i].goodsName+"</td>"+
							"<td ><a href='"+data[i].carUrl+"' target='_blank'>"+data[i].carUrl+"</a></td>"+
							"</tr>");
				}
			}else{
				$("#tab tbody").html("<tr bgcolor='#ECD3B4' height='30px'><td colspan='4' style='color:red;'>暂无可推送的商品</td></tr>");
			}
		},
		error:function(){
			alert("Error 500");
		}
	});
}


function mhide(){
	$(".main1").hide();
}


function  fnvalidg(type){
	if(type==0){
		var hwid = ($(document).width()- $(".main1").width())/2;
		var heig = ($(window).height()- $(".main1").height())/2;
		$(".main1").show().css({"left":hwid,"top":heig});
		return ;
	}
	var sgurl  = $("#sgurl").val();
	$(".mailibtn").attr('disabled',true); 
	$(".mailibtn").css('background-color','#c1c1c1'); 
	if(type==1){
		$.post("/cbtconsole/invalidgc/istore",
				{surl:sgurl},
				function(res){
					if(res=='-1'){
						$.dialog.alert("Message",'错误:屏蔽店铺链接无效 ');
					}else if(res=='1'){
					  $("#sgurl").val('');
						$.dialog.alert("Message",'屏蔽店铺操作成功 ');
					}else if(res=='0'){
						$.dialog.alert("Message",'屏蔽店铺操作失败 ');
					}else if(res=='-2'){
						$.dialog.alert("Message",'该店铺已经屏蔽 ');
					}
					 $("#sgurl").val('');
					$(".mailibtn").attr('disabled',false); 
					$(".mailibtn").css('background-color','#169bd5'); 
					
				});
	}else if(type==2){
		$.post("/cbtconsole/invalidgc/igoods",
				{text:sgurl},
				function(res){
					if(res=='-1'){
						$.dialog.alert("Message",'错误:屏蔽商品链接无效 ');
					}else if(res=='1'){
						$.dialog.alert("Message",'屏蔽商品操作成功 ');
					  $("#sgurl").val('');
					}else if(res=='0'){
						$.dialog.alert("Message",'屏蔽商品操作失败:数据表没有该产品信息 ');
					}else if(res=='-2'){
						$.dialog.alert("Message",'该产品已经屏蔽');
					}
					 $("#sgurl").val('');
					$(".mailibtn").attr('disabled',false); 
					$(".mailibtn").css('background-color','#169bd5'); 
				});
	}
	
	
	
}


</script>
<title>无货源商品推送</title>
</head>
<body>
  <h1 align="center">无货源商品推送</h1><br>
  <div style="text-align: center;">
  	近期共<label id="total">0</label>个无货源商品&nbsp;&nbsp;
  	<input type="button"  value="推送本页商品" style="width:200px;height:40px;font-size:20px;" id="push">
  	<input type="button"  value="屏蔽产品  / 店铺" style="width:200px;height:40px;font-size:20px;" onclick="fnvalidg(0)">
  	<input type="button"  value="查看屏蔽产品  / 店铺列表" style="width:250px;height:40px;font-size:20px;" onclick="window.open('/cbtconsole/invalidgc/ilist?page=1&type=1','_blank')">
  	<input type="button"  value="查看关键词 / 类别屏蔽列表" style="width:250px;height:40px;font-size:20px;" onclick="window.open('/cbtconsole/invalidgc/intensve','_blank')">
  	
  	<br>
  	<span style="display: none;color:green;" id="push_msg"><b>推送过程中，请勿关闭本页! 推送成功<label id="succ" style="color:red;">0</label>个，失败<label id="fail"  style="color:red;">0</label>个。</b></span>
  </div>
  <div style="margin:auto;width:1200px;">
  	无货源商品链接：<input type="text" id="goods_url"  style="width:960px; "/>
  	<input type="button" value="手动推送" style="font-size:18px;" id="push2">
  </div>
  <table id="tab" style="text-align:center;" align="center" border="0" cellspacing="2" width="1200px">
    <thead>
      <tr bgcolor="#62AFF7" style="color:#ffffff;" height="30px">
        <th width="50px">序号</th>
        <th width="100px">商品图片</th>
        <th width="350px">商品名称</th>
        <th width="350px">商品链接</th>
      </tr>
    </thead>
    <tbody></tbody>
  </table>
  
  <div class="main1" align="center" >
	<div class="maitit">屏蔽产品/店铺<span class="maiclo" onclick="mhide()">X</span></div>
	<table class="mailtab">
	<tr><td style="width: 450px;">产品/店铺链接:</td></tr>
	<tr><td style="width: 450px;">
	<textarea rows="10" cols="60" id="sgurl" style="width:400px;height:100px;"></textarea>
	 </td></tr>
	<tr><td style="height: 2px;"></td></tr>
	</table>
	<input type="button" value="屏蔽店铺" class="mailibtn" onclick="fnvalidg(1)">
	<input type="button" value="屏蔽产品" class="mailibtn" onclick="fnvalidg(2)">
  </div>
  
</body>
</html>