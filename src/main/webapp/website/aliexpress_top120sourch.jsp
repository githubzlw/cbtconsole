<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>手动添加货源</title>
</head>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript">
function fnSave(){
	var typeid = ${param.typeid};
	var index = ${param.index};
	var state = ${param.state};
	var url = '${param.url}';
	 var name = $("#name").val();
	 /*var price = $("#price").val(); */
	var purl = $("#url").val();
	/* var imgurl = $("#imgurl").val(); */
	var fromweb = $("#fromweb").val();
	/* if(name == ""){
		alert("商品名称不能为空");
		return;
	}else if(price == ""){
		alert("商品价格不能为空");
		return;
	}else */ if(purl == ""){
		alert("商品链接不能为空");
		return;
	}/* else if(imgurl == ""){
		alert("商品图片链接不能为空");
		return;
	} */
	//保存手动添加货源，添加完后若原链接是无货源则改为有货源，type表数量修改，页面240个显示有货源选择，显示列表增加该商品；若有货源则添加货源表并显示，页面240个显示有货源选择
	$.post("/cbtconsole/WebsiteServlet",
			{action:'saveAliexpressSourchURL',className:'Aliexpress_top120',purl:purl,typeid:typeid,url:url,state:state,name:name,gid:'${param.gid}'},
			function(res){
				if(res > 0){
					alert("添加成功");
					//父窗体显示连接
					parent.fnAddSource_back(index,purl);
				}else{
					alert("添加失败");
				}
			});
}
</script>
<body>
	<div align="center">
		<table>
			<tr><td align="right">商品名称:</td><td><input type="text" id="name"></td></tr>
			<!-- <tr><td align="right">商品价格:</td><td><input type="text" id="price"></td></tr> -->
			<tr><td align="right">商品链接:</td><td><input type="text" id="url"></td></tr>
			<!-- <tr><td align="right">图片链接:</td><td><input type="text" id="imgurl"></td></tr>
			 --><tr><td align="right">商品来源:</td><td><input type="text" id="fromweb" value="TB" disabled="disabled"></td></tr>
			<tr><td align="center"><input type="button" value="保存" onclick="fnSave();"></td></tr>
		</table>
	</div>
</body>
</html>