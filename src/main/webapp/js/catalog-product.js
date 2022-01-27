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
	
	productArray = [];
	//本地已经生成的目录商品优先展示
	if(editid != ''){
		catalogProduct(editid);
	}else{
		//获取线上商品
		searchFromRemote();
	}
}
/**已经生成的目录商品优先展示
 * @param id
 * @returns
 */
function catalogProduct(id){
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
			var requestHost = "https://www.import-express.com";
			if(data.template == 2){
				requestHost = "https://www.kidscharming.com";
			}else if(data.template == 4){
				requestHost = "https://www.petstoreinc.com";
			}
			
			var goodslist = eval(data.product);
			var productHtml = '';
			for(var i=0;i<size;i++){
				var item = goodslist[i].products;
				for(var j=0;j<item.length;j++){
					var product = item[j];
					productHtml = productHtml +'<div class="col-xs-2 product"><div class="product_in">';//src="https://www.import-express.com/newindex/img/dot.gif" data-
					productHtml = productHtml +'<a href="'+requestHost+product.url+'"><img src="/cbtconsole/img/beforeLoad.gif" data-original="'+product.img+'" class="product-img img-lazy img-responsive"></a>';
					productHtml = productHtml +'<input type="checkbox" class="is_boutique_check" name="is_selected" value="'+product.pid+'" onclick="checkClick(this)" checked="checked">';
					productHtml = productHtml +'<div class="info-product">';
					productHtml = productHtml +'<div class="product-name">'+product.name+'</div>';
					productHtml = productHtml +'<div class="product-price">Price:$'+product.price+' /'+product.unit+'</div>';
					productHtml = productHtml +'<div class="product-sold">Sols:'+product.sold+'</div>';
					productHtml = productHtml +'</div></div></div>';
					productArray.push(product.pid);
				}
			}
			$("#catalog_name").val(data.catalogName);
			
			 $("#query_temp").val(data.template);
			
			$(".product-list").html(productHtml);
			$('.img-lazy').lazyload({effect: "fadeIn"});
//			console.log(productArray);
			searchFromRemote();
		},
		error:function(e){
//			$.MsgBox.Alert("提示", "搜索请求错误");
		}
	});
}


/**获取线上商品
 * @returns
 */
function searchFromRemote(){
	var temp = $("#query_temp").val();
	var requestHost = "https://www.import-express.com";
	if(temp == 2){
		requestHost = "https://www.kidscharming.com";
	}else if(temp == 4){
		requestHost = "https://www.petstoreinc.com";
	}
	var catid = $("#param-catid").val();
	var keyword = $("#query-keyword").val();
	var page = $("#current-page").val();
	totalpage = 0;
	$.ajax({
		url:'/cbtconsole/catalog/search',
		data:{
			"keyword" : keyword,
			"catid" : catid,
			"page":page,
			"site":temp
		},
		type:"post",
		success:function(data){
			var size = data.recordCount;
			var goodslist = data.goodslist
			if(size < 1 || !goodslist){
				$(".product-list").html("");
				return ;
			}
			var paramv = data.param;
			var pagemap = data.pagemap;
			$("#current-page-h").html(pagemap.current);
			$("#total-page").html(pagemap.amount);
			$("#current-page").val(pagemap.current);
			$("#param-catid").val(paramv.catid);
			var qcatid = paramv.catid;
			var productHtml = $(".product-list").html();
			for(var i=0;i<goodslist.length;i++){
				if(productArray.includes(goodslist[i].id)){
					continue;
				}
				productHtml = productHtml +'<div class="col-xs-2 product"><div class="product_in">';//src="https://www.import-express.com/newindex/img/dot.gif" data-
				productHtml = productHtml +'<a href="'+requestHost+goodslist[i].url+'"><img src="/cbtconsole/img/beforeLoad.gif" data-original="'+goodslist[i].image+'" class="product-img img-lazy img-responsive"></a>';
				productHtml = productHtml +'<input type="checkbox" class="is_boutique_check" name="is_selected" value="'+goodslist[i].id+'" onclick="checkClick(this)">';
				productHtml = productHtml +'<div class="info-product">';
				productHtml = productHtml +'<div class="product-name">'+goodslist[i].name+'</div>';
				productHtml = productHtml +'<div class="product-price">Price:$'+goodslist[i].price+' /'+goodslist[i].priceUnit+'</div>';
				productHtml = productHtml +'<div class="product-sold">Sols:'+goodslist[i].sold+'</div>';
				productHtml = productHtml +'</div></div></div>';
			}
			var categoryHtml = "";
			var rootTree = data.rootTree
			if(rootTree){
				for(var i=0;i<rootTree.length;i++){
					categoryHtml = categoryHtml +'<div class="category-lev1';
					if(qcatid==rootTree[i].id){
						categoryHtml = categoryHtml +' category-lev1-select';
					}
					categoryHtml = categoryHtml+'"><span name="'+rootTree[i].id+'" onclick="categorysearch(this)">'+rootTree[i].name+'</span>';
					var childens = rootTree[i].childen;
					for(var j=0;j<childens.length;j++){
						categoryHtml = categoryHtml +'<div class="category-lev2';
						if(qcatid==childens[j].id){
							categoryHtml = categoryHtml +' category-lev2-select';
						}
						categoryHtml = categoryHtml +'"><span name="'+childens[j].id+'" onclick="categorysearch(this)">'+childens[j].name+'</span>';
						var childens_c = childens[j].childen;
						for(var k=0;k<childens_c.length;k++){
							categoryHtml = categoryHtml +'<div class="category-lev3'
							if(qcatid==childens_c[k].id){
								categoryHtml = categoryHtml +' category-lev3-select';
							}
							categoryHtml = categoryHtml +'"><span name="'+childens_c[k].id+'" onclick="categorysearch(this)">'+childens_c[k].name+'</span>'+'</div>';
						}
						categoryHtml = categoryHtml +'</div>';
					}
					categoryHtml = categoryHtml +'</div>';
				}
			}
			$(".category-list").html(categoryHtml);
			$(".product-list").html(productHtml);
			$("#record-count").html(data.recordCount);
			if(data.recordCount > 0){
				$(".product-page").show();
			}
			totalpage = parseInt(pagemap.amount);
			$('.img-lazy').lazyload({effect: "fadeIn"});
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

