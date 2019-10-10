var productArray = [];
var totalpage=0;//总页数
$(function(){
	//是否管理页面进入，记录id
	$("#list-product-edit").val(getUrlParam("id"));
	
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
	//弹框关闭
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
		$('.tc2').hide();
		var id = $("#list-product-edit").val();
		$.ajax({
		url:'/cbtconsole/catalog/product',
		data:{
			"id" : id
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
		
		var tem = $("#query_temp").val();
		var id = $("#list-product-edit").val();
		$.ajax({
		url:'/cbtconsole/catalog/create',
		data:{
			"catalogname":catelogName,
			"tem":tem,
			"id":id
		},
		type:"post",
		success:function(data){
			if(data.status == 200){
				$("[name=is_selected]:checkbox").prop("checked", false);
//				$('#pre-save-key').val('');
				$(".pre-row").html('');
				$('.tc,.trnasparent,.tc2').hide();
				$.MsgBox.Confirm("提示", "生成目录成功！！",function(){
					$("#list-product-edit").val("");
					window.open("/cbtconsole/apa/catalog_product.html", "_self");
				});
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
//							$("#pre-save-key").val('');
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
		if($(".col-xs-3-product").length==0){
			$.MsgBox.Alert("提示", "请先挑选产品后再生成目录！！");
		}else{
			$('.tc,.trnasparent,.tc2').show();
		}
	})
})

function closeX(v){
	var pid = $(v).attr("name");
	var tem = $("#query_temp").val();
	var del = 1;
	$.ajax({
		url:'/cbtconsole/catalog/save',
		data:{
			"pid" : pid,
			"del" : del,
			"tem" : tem
		},
		type:"post",
		success:function(data){
			if(data.status == 200){
//				$("#pre-save-key").val(data.saveKey);
				$("[value="+pid+"]:checkbox").prop("checked", false);
				$(".col-"+pid).remove();
				if(data.catid){
					var child = $(".row-cat-"+data.catid).children(".col-xs-3-product").length;
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
		var editid = $("#list-product-edit").val();
		var pid = $(v).val();
		var del = 1;
		if($(v).is(':checked')){
			del = 0;
		}
		var tem = $("#query_temp").val();
		$.ajax({
			url:'/cbtconsole/catalog/save',
			data:{
				"pid" : pid,
				"del" : del,
				"tem" : tem,
				"editid":editid
			},
			type:"post",
			success:function(data){
				if(data.status == 200){
//					$("#pre-save-key").val(data.saveKey);
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
	$(".product-list").html("");
	$(".category-list").html("");
	$(".product-page").hide();
	var editid = $("#list-product-edit").val();
	
	var temp = $("#query_temp").val();
	var requestHost = "https://www.import-express.com";
	if(temp == '2'){
		requestHost = "https://www.kidsproductwholesale.com/";
	}else if(temp == '4'){
		requestHost = "https://www.lovelypetsupply.com/";
	}
	productArray = [];
	//本地已经生成的目录商品优先展示
	if(editid != ''){
		catalogProduct(editid,requestHost);
	}
	//获取线上商品
	searchFromRemote(temp,requestHost);
}
/**已经生成的目录商品优先展示
 * @param id
 * @returns
 */
function catalogProduct(id,requestHost){
	$.ajax({
		url:'/cbtconsole/catalog/product',
		data:{
			"id" : id,
			"isManag" : true
		},
		type:"post",
		success:function(data){
			var size = parseInt(data.productSize);
			if(size < 1){
				$(".product-list").html("");
				return ;
			}
			var goodslist = eval(data.product);
			var productHtml = '';
			for(var i=0;i<size;i++){
				var item = goodslist[i].products;
				for(var j=0;j<item.length;j++){
					var product = item[j];
					productHtml = productHtml +'<div class="col-xs-2 product"><div class="product_in">';//src="https://www.import-express.com/newindex/img/dot.gif" data-
					productHtml = productHtml +'<a href="'+requestHost+product.url+'"><img src="'+product.img+'" class="product-img img-lazy img-responsive"></a>';
					productHtml = productHtml +'<input type="checkbox" class="is_boutique_check" name="is_selected" value="'+product.pid+'" onclick="checkClick(this)" checked="checked">';
					productHtml = productHtml +'<div class="info-product">';
					productHtml = productHtml +'<div class="product-name">'+product.name+'</div>';
					productHtml = productHtml +'<div class="product-price">Price:$'+product.price+' /'+product.unit+'</div>';
					productHtml = productHtml +'<div class="product-sold">Sols:'+product.sold+'</div>';
					productHtml = productHtml +'</div></div></div>';
					productArray.push(product.pid);
				}
			}
			$(".product-list").html(productHtml);
//			console.log(productArray);
		},
		error:function(e){
//			$.MsgBox.Alert("提示", "搜索请求错误");
		}
	});
}


/**获取线上商品
 * @returns
 */
function searchFromRemote(temp,requestHost){
	var catid = $("#param-catid").val();
	var keyword = $("#query-keyword").val();
	var page = $("#current-page").val();
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
			var paramv = data.param;
			$("#current-page-h").html(paramv.currentPage);
			$("#total-page").html(paramv.amountPage);
			$("#current-page").val(paramv.currentPage);
			$("#param-catid").val(paramv.catid);
			var qcatid = paramv.catid;
			var goodslist = data.goodsList
			var productHtml = $(".product-list").html();
			for(var i=0;i<size;i++){
				if(productArray.includes(goodslist[i].goods_pid)){
					continue;
				}
				productHtml = productHtml +'<div class="col-xs-2 product"><div class="product_in">';//src="https://www.import-express.com/newindex/img/dot.gif" data-
				productHtml = productHtml +'<a href="'+requestHost+goodslist[i].goods_url+'"><img src="'+goodslist[i].goods_image+'" class="product-img img-lazy img-responsive"></a>';
				productHtml = productHtml +'<input type="checkbox" class="is_boutique_check" name="is_selected" value="'+goodslist[i].goods_pid+'" onclick="checkClick(this)">';
				productHtml = productHtml +'<div class="info-product">';
				productHtml = productHtml +'<div class="product-name">'+goodslist[i].goods_name+'</div>';
				productHtml = productHtml +'<div class="product-price">Price:$'+goodslist[i].goods_price+' /'+goodslist[i].goodsPriceUnit+'</div>';
				productHtml = productHtml +'<div class="product-sold">Sols:'+goodslist[i].goods_solder+'</div>';
				productHtml = productHtml +'</div></div></div>';
			}
			var categoryHtml = "";
			var rootTree = data.rootTree
			for(var i=0;i<rootTree.length;i++){
				categoryHtml = categoryHtml +'<div class="category-lev1';
				if(qcatid==rootTree[i].cid){
					categoryHtml = categoryHtml +' category-lev1-select';
				}
				categoryHtml = categoryHtml+'"><span name="'+rootTree[i].cid+'" onclick="categorysearch(this)">'+rootTree[i].category+'</span>';
				var childens = rootTree[i].childens;
				for(var j=0;j<childens.length;j++){
					categoryHtml = categoryHtml +'<div class="category-lev2';
					if(qcatid==childens[j].cid){
						categoryHtml = categoryHtml +' category-lev2-select';
					}
					categoryHtml = categoryHtml +'"><span name="'+childens[j].cid+'" onclick="categorysearch(this)">'+childens[j].category+'</span>';
					var childens_c = childens[j].childens;
					for(var k=0;k<childens_c.length;k++){
						categoryHtml = categoryHtml +'<div class="category-lev3'
						if(qcatid==childens_c[k].cid){
							categoryHtml = categoryHtml +' category-lev3-select';
						}
						categoryHtml = categoryHtml +'"><span name="'+childens_c[k].cid+'" onclick="categorysearch(this)">'+childens_c[k].category+'</span>'+'</div>';
					}
					categoryHtml = categoryHtml +'</div>';
				}
				categoryHtml = categoryHtml +'</div>';
			}
			$(".category-list").html(categoryHtml);
			$(".product-list").html(productHtml);
			$("#record-count").html(data.recordCount);
			$(".product-page").show();
			totalpage = parseInt(paramv.amountPage);
		},
		error:function(e){
			$.MsgBox.Alert("提示", "搜索请求错误");
		}
	});
}

/**
 * 获取参数
 * @param name
 * @returns
 */
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}

function categorysearch(v){
	var catid = $(v).attr("name");
	$("#param-catid").val(catid);
	$("#current-page").val(1);
	search();
}

