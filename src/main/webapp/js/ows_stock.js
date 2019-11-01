$(function(){
	//查询
	$(".query_button").click(function(){
		$("#current_page").val("1");
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
	
	//明细
	$(".btn-detail").click(function(){
		var owsid = $(this).attr("name")
		window.open("/cbtconsole/owstock/log?owsid="+owsid,"_blank");
	})
	
	
	
	
})

/**
 * 查询
 * @returns
 */
function search(){
	var page = $("#current_page").val();
	var pid= $("#query_goods_pid").val();
	var skuid = $("#query_goods_skuid").val();
	var href= "/cbtconsole/owstock/list?page="+page+"&pid="+pid+"&skuid="+skuid;
	window.open(href,"_self");
	
}