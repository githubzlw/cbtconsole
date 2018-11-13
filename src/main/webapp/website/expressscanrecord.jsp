<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>快递扫描记录</title>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
<script>
function insertExpressRecord(express_code){
	$.ajax({                                        
		type:"post", 
		url:"/cbtconsole/takeGoods/insertExpressRecord.do",
		data:{express_code:express_code}, 
		dataType:"text",
		success : function(data){    
			$("#msg").html('记录成功');                 
			$("#search").val("");
			window.setTimeout(function(){ 
				$("#msg").html("");               
			},1500);
		}
	});
}
function search() {
	//插入扫描记录	
	insertExpressRecord($("#search").val());
}
</script>
</head>           
<body>
<div align="center">                            
	<div>
	<h1>快递扫描记录</h1>
	<a href="/cbtconsole/takeGoods/getExpressRecord.do"  target="_Blank" onclick="getExpressRecord()">查看扫描记录</a>|
	<br/>
	<h1 id="msg"></h1>
	</div>
	<div>
			请输入快递号：
	<input type="text" id="search"  onFocus="celsearch()"  onkeypress="if (event.keyCode == 13) search()" > <!-- oninput="checkSear()"   判断上一次商品是否已经入库 -->
	<input type="button" value="扫描" onclick="search()"/>	
	</div>
<div>

</div>
</div>
</body>
</html>