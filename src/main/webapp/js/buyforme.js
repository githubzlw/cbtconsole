$(function(){
	$("#query_button_check").click(function(){
		doQuery();
	})
	$(".btn_page_up").click(function(){
		var page = Number($("#current_page").val());
		var tpage = Number($("#total_page").val());
		page = page -1;
		if(page < 1){
			$("#current_page").val(1);
		}else if(page > tpage){
			$("#current_page").val(tpage);
		}else{
			$("#current_page").val(page);
		}
		doQuery();
	})
	$(".btn_page_down").click(function(){
		var page = Number($("#current_page").val());
		var tpage = Number($("#total_page").val());
		page = page+1;
		if(page < 1){
			$("#current_page").val(1);
		}else if(page > tpage){
			$("#current_page").val(tpage);
		}else{
			$("#current_page").val(page);
		}
		doQuery();
	})
	$(".btn_page_qu").click(function(){
		doQuery();
	})
	
	
	
	
	
})

function doQuery(){
	var oderno = $("#query_oderno").val();
	var userid = $("#query_user_id").val();
	var sttime = $("#query_sttime").val();
	var edtime = $("#query_edtime").val();
	var state = $("#query_state").val();
	var page = $("#current_page").val();
	window.open("/cbtconsole/bf/orders?page="+page+"&userid="+userid+"&oderno="+oderno
			+"&state="+state+"&sttime="+sttime+"&edtime="+edtime, "_self");
}
