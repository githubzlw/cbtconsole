$(function(){
	$(".btn-search").click(function(){
		$("#current_page").val(1);
		search();
	})
	$(".btn_page_qu").click(function(){
		search();
	})
	$(".btn_page_up").click(function(){
		var current = parseInt($("#current_page").val());
		var total_page = parseInt($("#total_page").val());
		current = current - 1;
		if(current < 1){
			current = 1;
		}else if(current > total_page){
			current = total_page;
		}
		$("#current_page").val(current);
		search();
	})
	$(".btn_page_down").click(function(){
		var current = parseInt($("#current_page").val());
		var total_page = parseInt($("#total_page").val());
		current = current + 1;
		if(current < 1){
			current = 1;
		}else if(current > total_page){
			current = total_page;
		}
		$("#current_page").val(current);
		search();
	})
	
	$('.trnasparent').click(function(){
		$('.tc,.trnasparent,.tc1').hide();
	});
	
	$(".btn-updeta-s").click(function(){
		var catid = $("#update-catid").val()
		var category = $("#update-category").val()
		var content = $("#update-sy").val();
		$.ajax({
		       url:"/cbtconsole/synonyms/category/update",
		       data:{"catid":catid,"content":content},
		       type:"post",
		       success:function(data){
		    	  if(data.status == 200){
		    		  $('.tc,.trnasparent,.tc1').hide();
		    		  $.MsgBox.Confirm("提示", "更新成功,等待数据同步后刷新列表",function(){
		    			  location.reload();
		    		  });
		    	  }else{
		    		  $.MsgBox.Alert("提示", data.message);
		    	  }
		       },
		   	error:function(e){
		   		$.MsgBox.Alert("提示", "更新失败");
		   	}
		   })
	})
	
	$(".btn-add").click(function(){
		$("#update-catid").removeAttr("disabled");
		$("#update-category").removeAttr("disabled");
		$(".btn-updeta-s").hide();
		$(".btn-updeta-add").show();
		$('.tc,.trnasparent,.tc1').show();
	})
	$(".btn-updeta-add").click(function(){
		
		var catid = $("#update-catid").val()
		var category = $("#update-category").val()
		var content = $("#update-sy").val();
		$.ajax({
		       url:"/cbtconsole/synonyms/category/add",
		       data:{"catid":catid,"content":content,"category":category},
		       type:"post",
		       success:function(data){
		    	  if(data.status == 200){
		    		  $('.tc,.trnasparent,.tc1').hide();
		    		  $.MsgBox.Confirm("提示", "新增成功,等待数据同步后刷新列表",function(){
		    			  location.reload();
		    		  });
		    	  }else{
		    		  $.MsgBox.Alert("提示", data.message);
		    	  }
		       },
		   	error:function(e){
		   		$.MsgBox.Alert("提示", "新增失败");
		   	}
		   })
		
	})
	
	$(".btn-flush").click(function(){
		$.ajax({
			url:'/cbtconsole/init/flush/synonyms/category',
			data:{},
			type:"post",
			success:function(data){
				if(data.code==200){
					$.MsgBox.Alert("提示", "刷新成功");
				}else{
					$.MsgBox.Alert("提示", "刷新失败");
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "请求错误");
			}
		});
	})
	
})

/**
 * @param catid
 * @returns
 */
function updateCatid(catid){
	$("#update-catid").attr("disabled", "disabled");
	$("#update-category").attr("disabled", "disabled");
	$(".btn-updeta-s").show();
	$(".btn-updeta-add").hide();
	$('.tc,.trnasparent,.tc1').show();
	$("#update-catid").val(catid)
	$("#update-category").val($("#category-"+catid).text())
	$("#update-sy").text($("#synonyms-"+catid).text())
	
}

/**删除
 * @param catid
 * @returns
 */
function deleteCatid(catid){
	$.MsgBox.Confirm("提示", "确定删除吗?", function(){
		$.ajax({
		       url:"/cbtconsole/synonyms/category/delete",
		       data:{"catid":catid},
		       type:"post",
		       success:function(data){
		    	  if(data.status == 200){
		    		  $.MsgBox.Confirm("提示", "删除成功,等待数据同步后刷新列表",function(){
		    			  location.reload();
		    		  });
		    	  }else{
		    		  $.MsgBox.Alert("提示", data.message);
		    	  }
		       },
		   	error:function(e){
		   		$.MsgBox.Alert("提示", "删除失败");
		   	}
		   });
	})
	
	
}

/**查询
 * @returns
 */
function search(){
	var catid = $("#catid-p").val();
	var page = $("#current_page").val();
	window.open("/cbtconsole/synonyms/category/list?page="+page+"&catid="+catid, "_self");
	
}