$(function(){
	//弹框关闭
	$('.trnasparent').click(function(){
		$('.tc,.trnasparent,.tc1,tc2').hide();
		$(".pre-row").html('');
	});
	//新增目录，跳转目录页面挑选
	$(".catalog-new").click(function(){
		window.open("/cbtconsole/apa/catalog_product.html", "_blank");
	})
	//查询
	$(".btn-query-list").click(function(){
		$("#current_page").val("1");
		getCatalogList();
	})
	//跳转
	$(".btn_page_qu").click(function(){
		getCatalogList();
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
		getCatalogList();
		
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
		getCatalogList();
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
				    		  getCatalogList();
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
	
	//预览
	$(".catalog-preview").click(function(){
		var id = $(this).attr("name");
		$.ajax({
		url:'/cbtconsole/catalog/product',
		data:{
			"id" : id,
			"isManag" : true
		},
		type:"post",
		success:function(data){
			$(".pre-row").html('');
			if(data.status == 200){
				if(data.productSize>0){
					var product = eval(data.product);
					var producthtml='';
					for(var i=0;i<data.productSize;i++){
						producthtml = producthtml+'<div class="row category-row-product row-cat-'+product[i].catid+'"><div class="h3-div">';
						producthtml = producthtml+'<h3 class="h3-c">category id: '+product[i].catid+' -------- category name: '+product[i].category+'</h3></div>';
						producthtml = producthtml+'<div class="row catalog_ul clearfix">';
						var item = eval(product[i].products);
						for(var j=0;j<item.length;j++){
							producthtml = producthtml+' <div class="col-xs-3 col-'+item[j].pid+' col-xs-3-product">';
							producthtml = producthtml+' <a href="'+item[j].url+'" class="catalog_link">';
							producthtml = producthtml+' <img class="catalog_img img-responsive" src="'+item[j].img+'"></a>';
//							producthtml = producthtml+'<span class="close-x" name="'+item[j].pid+'" onclick="closeX(this)">X</span></div>';
							producthtml = producthtml+'</div>';
						}
						producthtml = producthtml+'</div>';
						producthtml = producthtml+'</div>';
					}
					$(".pre-row").html(producthtml);
					$('.tc,.trnasparent,.tc1').show();
				}else{
					$.MsgBox.Alert("提示", "数据错误");
				}
			}else{
				$.MsgBox.Alert("提示", "预览错误:"+data.message);
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			$.MsgBox.Alert("错误", textStatus);
		}
	});
		
	})
	
	$(".catalog-edit").click(function(){
		var id = $(this).attr("name");
		window.open("/cbtconsole/apa/catalog_product.html?id="+id, "_blank");
	})
})

/**
 * 查询
 * @returns
 */
function getCatalogList(){
	var page = $("#current_page").val();
	var template = $("#catalog-template").val();
	var catalogName = $("#catalog-name").val();
	window.location.href = "/cbtconsole/catalog/list?page="+page+"&template="+template+"&catalogName="+catalogName;
}