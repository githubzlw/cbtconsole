$(function(){
	//查询
	$(".query_button").click(function(){
		$("#current_page").val("1");
		$("#query_owsid").val("");
		search();
	})
	//跳转
	$(".btn_page_qu").click(function(){
		search();
	})
	//上一页
	$(".btn_page_up").click(function(){
		var total = parseInt($("#total_page").val());
		var current = parseInt($("#current_page").val());
		current = current - 1;
		if(current < 1){
			current = 1;
		}else if(current > total){
			current = total;
		}
		$("#current_page").val(current)
		search();
	})
	//下一页
	$(".btn_page_down").click(function(){
		var total = parseInt($("#total_page").val());
		var current = parseInt($("#current_page").val());
		current = current + 1;
		if(current < 1){
			current = 1;
		}else if(current > total){
			current = total;
		}
		$("#current_page").val(current)
		search();
	})
})

/**
 * 查询
 * @returns
 */
function search(){
	var page = $("#current_page").val();
	var code= $("#query_goods_code").val();
	var pid= $("#query_goods_pid").val();
	var skuid = $("#query_goods_skuid").val();
	var owsid = $("#query_owsid").val();
	var type = $("#query_goods_type").val();
	var odid = $("#query_goods_odid").val();
	var href= "/cbtconsole/owstock/log?page="+page+"&pid="+pid+"&skuid="+skuid+"&owsid="+owsid+"&code="+code+"&type="+type+"&odid="+odid;
	window.open(href,"_self");
	
}