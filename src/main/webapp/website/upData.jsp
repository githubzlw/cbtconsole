<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>数据更新</title>
</head>
<script type="text/javascript">
  function respider(){
	  var company = $("#company").val();
	  if(company==null){
		  alert("请选中运输方式 ！");
	  }
	  $.ajax({
		  type:'post',
		  url:"/cbtconsole/trackingController/respider",
		  data:{company:company},
		  dataType:"json",
		  success:function(res){
			  if(res=="true"){
				  alert("更新 成功！")
			  }else{
				  alert("运单状态还没处理 ！")
			  }
		  }
	  })
  }
  
	//数据更新
	function butUpData(){
		
		alert("数据更新中，请等待。");
		$.post("/cbtconsole/customerServlet",
	  			{action:'updateDelFlagMax',className:'UpDataServlet',count:'100',startid:'0'},
	  			function(res){
	  				alert("数据更新成功。");
	  	}); 
	}
	
</script>
<body>
<div>
<h2>MAX(最近速卖通历史免邮价，当前速卖通免邮价) 除以 (1688价格*1.1 + 运费) > 3.5</h2>
<form id="form2" name="form2" method="post" action="/cbtconsole/Trans">
  <input type="button" name="button" id="button" value="updata" onclick="butUpData()"/>
</form>
</div>
 <br>

</body>
</html>