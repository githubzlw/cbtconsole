
var totalpage=0;//总页数
$(function(){
	//默认搜索
	search();
	$("#query_temp").change(function(){
		search();
	})
	//搜索
	$("#query_button").click(function(){
		$("#param-catid").val('');
		search();
	})
	$('.trnasparent').click(function(){
		$('.tc,.trnasparent,.tc1,tc2').hide();
		$(".pre-row").html('');
	});
	
	//图片懒加载
	$('.product-list').find(".product-img").lazyload({
		effect:"show",
		failure_limit:15,
		threshold:500
	});
	//跳转
	$("#catalog-jump-page").click(function(){
		search();
	})
	//上一页
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
	//下一页
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
	
	//预览
	$("#catalog_preview").click(function(){
		var preSaveKey = $("#pre-save-key").val();
		if(preSaveKey ==''){
			 $.MsgBox.Alert("提示", "请先挑选产品后再预览");
			return;
		}
		$.ajax({
		url:'/cbtconsole/catalog/create?preview=true',
		data:{
			"saveKey" : preSaveKey
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
							producthtml = producthtml+'<span class="close-x" name="'+item[j].pid+'" onclick="closeX(this)">X</span></div>';
							
						}
						producthtml = producthtml+'</div>';
						producthtml = producthtml+'</div>';
					}
					$(".pre-row").html(producthtml);
					$('.tc,.trnasparent,.tc1').show();
				}else{
					$.MsgBox.Alert("提示", "请先挑选产品后再预览");
				}
			}else{
				$.MsgBox.Alert("提示", "错误:"+data.message);
			}
		},
		error:function(XMLHttpRequest, textStatus, errorThrown){
			$.MsgBox.Alert("错误", textStatus);
		}
	});
		
	})
	
	//生成目录
	$("#catalog_create").click(function(){
		$('.tc,.trnasparent,.tc2').show();
	})
	
	//确认生成
	$(".boutique_goods_ok").click(function(){
		var catelogName = $("#catalog_name").val();
		if(catelogName == ''){
			 $.MsgBox.Alert("提示", "请先输入目录名称");
			return ;
		}
		
		var preSaveKey = $("#pre-save-key").val();
		if(preSaveKey ==''){
			$.MsgBox.Alert("提示", "请先挑选产品再生成目录");
			return;
		}
		var tem = $("#query_temp").val();
		$.ajax({
		url:'/cbtconsole/catalog/create',
		data:{
			"saveKey" : preSaveKey,
			"catalogname":catelogName,
			"tem":tem
		},
		type:"post",
		success:function(data){
			if(data.status == 200){
				$("[name=is_selected]:checkbox").prop("checked", false);
				$('#pre-save-key').val('');
				$(".pre-row").html('');
				$('.tc,.trnasparent,.tc2').hide();
				$.MsgBox.Alert("提示", "生成目录成功！！");
			}else{
				$.MsgBox.Alert("提示", data.message);
			}
		},
		error:function(e){
			$.MsgBox.Alert("提示", "目录生成失败");
		}
	});
	})
	//取消目录生成
	$(".boutique_goods_cancel").click(function(){
		$('.tc,.trnasparent,.tc1,tc2').hide();
		$("#catalog_name").val('');
	})
	//预览弹框里 清除所有
	$(".boutique_goods_clear").click(function(){
		 $.MsgBox.Confirm("提示", "确认清空所选的全部产品?", function(){
			 $.ajax({
					url:'/cbtconsole/catalog/clear',
					data:{
					},
					type:"post",
					success:function(data){
						if(data.status == 200){
							$("[name=is_selected]:checkbox").prop("checked", false);
							$("#pre-save-key").val('');
							$(".pre-row").html('');
							$('.tc,.trnasparent,.tc1').hide();
						}else{
							$.MsgBox.Alert("提示", data.message);
						}
					},
					error:function(e){
						$.MsgBox.Alert("提示", "清空全部产品失败");
					}
					
				})
		 });
		
	})
	//预览弹框里 生成目录
	$(".boutique_goods_create").click(function(){
		$('.tc,.trnasparent,.tc1').hide();
		$('.tc,.trnasparent,.tc2').show();
	})
	
	
})

function closeX(v){
	var pid = $(v).attr("name");
	var tem = $("#query_temp").val();
	var preSaveKey = $("#pre-save-key").val();
	var del = 1;
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
				$(".col-"+pid).remove();
				if(data.catid){
					var child = $(".row-cat-"+data.catid).child(".col-xs-3-product").length;
					if(child == 0){
						$(".row-cat-"+data.catid).remove();
					}
				}
			}
		},
		error:function(e){
			$.MsgBox.Alert("提示", "删除失败");
		}
	});
}

/**checkbox点击事件
 * @param v
 * @returns
 */
function checkClick(v){
		var pid = $(v).val();
		var del = 1;
		if($(v).is(':checked')){
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
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "选择产品失败");
			}
		});
		
	}



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
				productHtml = productHtml +'<input type="checkbox" class="is_boutique_check" name="is_selected" value="'+goodslist[i].goods_pid+'" onclick="checkClick(this)">';
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
			$.MsgBox.Alert("提示", "搜索请求错误");
		}
	});
}



