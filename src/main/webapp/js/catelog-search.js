
var totalpage=0;//总页数
$(function(){
	search();
	$("#query_button").click(function(){
		$("#param-catid").val('');
		search();
	})
	$('.trnasparent').click(function(){
		$('.tc,.trnasparent,.tc1').hide();
	});
	//图片懒加载
	$('.product-list').find(".product-img").lazyload({
		effect:"show",
		failure_limit:15,
		threshold:500
	});
	
	$("#catalog-jump-page").click(function(){
		search();
	})
	$("#catalog-pre-page").click(function(){
		var currentPage = parseInt($("#current-page").val());
		currentPage = currentPage -1 ;
		if(currentPage < 1){
			currentPage = 1;
		}else if(currentPage > totalpage){
			currentPage = totalpage;
		}
		$("#current-page").val(currentPage);
		search();
	})
	$("#catalog-next-page").click(function(){
		var currentPage = parseInt($("#current-page").val());
		currentPage = currentPage + 1 ;
		if(currentPage > totalpage){
			currentPage = totalpage;
		}else if(currentPage < 1){
			currentPage = 1;
		}
		$("#current-page").val(currentPage);
		search();
	})
	
	$(".is_boutique_check").click(function(){
		var pid = $(this).val();
		var del = 1;
		if($(this).checked){
			del = 0;
		}
		var tem = $("#query_temp").val();
		var preSaveKey = $("#pre-save-key").val();
		$.ajax({
			url:'/cbtconsole/catalog/save',
			data:{
				"pid" : pid,
				"del" : del,
				"tem" : tem,
				"saveKey":preSaveKey
			},
			type:"post",
			success:function(data){
				if(data.status == 200){
					$("#pre-save-key").val(data.saveKey);
					alert(data.saveKey);
				}
			},
			error:function(e){
				
			}
		});
		
	})
	
	$("#catalog_preview").click(function(){
		$('.tc,.trnasparent,.tc1').show();
		var preSaveKey = $("#pre-save-key").val();
		if(preSaveKey ==''){
			return;
		}
		$.ajax({
		url:'/cbtconsole/catalog/create?preview=true',
		data:{
			"saveKey" : preSaveKey
		},
		type:"post",
		success:function(data){
			
			
			
			
			
		},
		error:function(e){
			
		}
	});
		
	})
	
	
	
	
	
})

/**
 * 搜索产品作为挑选
 * @returns
 */
function search(){
	$(".product-page").hide();
	var catid = $("#param-catid").val();
	var keyword = $("#query-keyword").val();
	var page = $("#current-page").val();
	var temp = $("#query_temp").val();
	var requestHost = "http://localhost:10004";
	if(temp == '2'){
		requestHost = "http://localhost:10004";
	}else if(temp == '4'){
		requestHost = "http://localhost:10004";
	}
	$(".product-list").html("");
	totalpage = 0;
	$.ajax({
		url:requestHost+'/catalog/product',
		data:{
			"keyword" : keyword,
			"catid" : catid,
			"page":page
		},
		type:"post",
		success:function(data){
			var size = parseInt(data.goodsSize);
			if(size < 1){
				$(".product-list").html("");
				return ;
			}
			var goodslist = data.goodsList
			var productHtml = '';
			for(var i=0;i<size;i++){
				productHtml = productHtml +'<div class="col-xs-2 product"><div class="product_in">';//src="https://www.import-express.com/newindex/img/dot.gif" data-
				productHtml = productHtml +'<a href="'+requestHost+goodslist[i].goods_url+'"><img src="'+goodslist[i].goods_image+'" class="product-img img-lazy img-responsive"></a>';
				productHtml = productHtml +'<input type="checkbox" class="is_boutique_check" name="is_selected" value="'+goodslist[i].goods_pid+'">';
				productHtml = productHtml +'<div class="info-product">';
				productHtml = productHtml +'<div class="product-name">'+goodslist[i].goods_name+'</div>';
				productHtml = productHtml +'<div class="product-price">Price:$'+goodslist[i].goods_price+' /'+goodslist[i].goodsPriceUnit+'</div>';
				productHtml = productHtml +'<div class="product-sold">Sols:'+goodslist[i].goods_solder+'</div>';
				productHtml = productHtml +'</div></div></div>';
			}
			$(".product-list").html(productHtml);
			var paramv = data.param;
			$("#current-page-h").html(paramv.currentPage);
			$("#total-page").html(paramv.amountPage);
			$("#record-count").html(data.recordCount);
			$("#current-page").val(paramv.currentPage);
			$("#param-catid").val(paramv.catid);
			$(".product-page").show();
			totalpage = parseInt(paramv.amountPage);
		},
		error:function(e){
			
		}
	});
}



