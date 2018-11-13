<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>保存AliExpress 前6页搜索结果</title>
</head>
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<script type="text/javascript">
function fnAdd(){
	var search_content = $("#search_content li");
	var isNull = 0;
	var li_length = $("#search_content li").length;
	for (var i = 0; i < li_length; i++) {
		var keyword = $(search_content).eq(i).find("input:eq(0)").val();
		if($.trim(keyword) == ""){
			$(search_content).eq(i).find("input:eq(0)").css("border","1px solid red");
			isNull = 1;
		}
	}
	if(isNull == 1){
		alert("关键字不能为空");
	}else{
		$("#search_content").append('<li>搜索关键字:<input type="text">&nbsp;类别ID：<input type="text" value="0">&nbsp;<input type="button" onclick="fnAdd()" style="font-size: 24px;"  value="+"><input type="button" onclick="fnDel(this)" style="font-size: 24px;"  value="-"></li>');
	}
}

function fnSearch(){
	var search_content = $("#search_content li");
	var isNull = 0;
	var search_content = "";
	var li_length = $("#search_content li").length;
	for (var i = 0; i < li_length; i++) {
		var keyword = $("#search_content li").eq(i).find("input:eq(0)").val();
		var typeId = $("#search_content li").eq(i).find("input:eq(1)").val();
		if($.trim(keyword) == ""){
			$("#search_content li").eq(i).find("input:eq(0)").css("border","red");
			isNull = 1;
		}else{
			search_content += keyword + "," + typeId+"@";
		}
	}
	if(isNull == 1){
		alert("关键字不能为空");
	}else{
		var sort = $("input[name='sort']:checked").val();
		$("#ex").attr("disabled","disabled");
		$.post("/cbtconsole/WebsiteServlet",
				{action:'search_aliexpress',className:'Aliexpress_top120',searchinfo:search_content,sort:sort},
				function(res){
					if(res > 0){
						$("#results").show();
						$("#download_div").show();
						$("#download").html(res);
						$.post("/cbtconsole/WebsiteServlet",
								{action:'downloadImgThred',className:'Aliexpress_top120',searchinfo:search_content,sort:sort},
								function(res){
								});
						time1 = setInterval(fnGetImgDownload,2000);
					}else{
						alert("没有找到数据");
					}
				});
	}
}
var time1;
function fnGetImgDownload(){
	$.post("/cbtconsole/WebsiteServlet",
			{action:'downloadImg',className:'Aliexpress_top120'},
			function(res){
				if(res == 0){
					clearInterval(time1);
				} 
				$("#download").html(res);
			});
}
 
function fnDel(val){
	var search_content = $("#search_content li").length;
	if(search_content == 1){
		return;
	}
	$(val).parent().remove();
}

function fnToResults(){
	window.open("/cbtconsole/WebsiteServlet?action=get_aliexpress_results&className=Aliexpress_top120");      
	window.location.href="";
}

</script>
<body>
<div align="center">
<div><a href="/cbtconsole/WebsiteServlet?action=get_aliexpress_results&className=Aliexpress_top120" target="_blank">查看关键字搜索结果</a></div>
	<h1>保存AliExpress 前6页搜索结果</h1>
	<div style="width: 700px;">
		<ul id="search_content" style="list-style: none;border: 1px solid red;">
			<li>搜索关键字:<input type="text">&nbsp;类别ID：<input type="text" value="0">&nbsp;<input type="button" onclick="fnAdd()" style="font-size: 24px;"  value="+"><input type="button" onclick="fnDel(this)" style="font-size: 24px;"  value="-"></li>
		</ul>
		<div>
			排序方式：<input type="radio" name="sort" id="sort0" value="0"><label for="sort0">价格</label>
				   <input type="radio" checked="checked" name="sort" id="sort1" value="1"><label for="sort1">销量</label>
			&nbsp;&nbsp;<input type="button" id="ex" style="font-size: 24px;" onclick="fnSearch()" value="执行"><label id="results" style="color: red;display: none;">执行结果保存完毕,正在进行 图片搜索</label>
		</div>
		<div style="display: none" id="download_div">剩余图片下载数量：<label id="download"></label></div>
	</div>
</div>
</body>
</html>