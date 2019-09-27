$(function(){
	
	//新增目录，跳转目录页面挑选
	$(".catalog-new").click(function(){
		window.open("/cbtconsole/apa/catalog_product.html", "_blank");
	})
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
		var current_page = parseInt($("#current_page").val());
		var total_page = parseInt($("#total_page").val());
		current_page = current_page - 1;
		if(current_page < 1){
			current_page = 1;
		}else if(current_page > total_page){
			current_page = total_page;
		}
		$("#current_page").val(current_page);
		search();
		
	})
	//下一页
	$(".btn_page_down").click(function(){
		var current_page = parseInt($("#current_page").val());
		var total_page = parseInt($("#total_page").val());
		current_page = current_page + 1;
		if(current_page < 1){
			current_page = 1;
		}else if(current_page > total_page){
			current_page = total_page;
		}
		$("#current_page").val(current_page);
		search();
	})
	
	//删除
	$(".catalog-delete").click(function(){
		var id = $(this).attr("name");
		 $.MsgBox.Confirm("提示", "确认删除此推荐目录?", function(){
			 $.ajax({
					url:"/cbtconsole/catalog/delete",
				    data:{"id":id},
				    type:"post",
				    success:function(data){
				    	  if(data.status == 200){
				    		 search();
				    	  }else{
				    		  $.MsgBox.Alert("提示", data.message);
				    	  }
				     },
				   	error:function(e){
				   		$.MsgBox.Alert("提示", "页面删除发生错误");
				   	}
				});
			 
			 });
	})
	
})

/**
 * 查询
 * @returns
 */
function search(){
	var page = $("#current_page").val();
	var template = $("#catalog-template").val();
	var catalogName = $("#catalog-name").val();
	window.location.href = "/cbtconsole/catalog/list?page="+page+"&template="+template+"&catalogName="+catalogName;
}