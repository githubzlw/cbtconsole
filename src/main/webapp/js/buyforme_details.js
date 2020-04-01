var costList;
$(function(){
	getShippingCost();
	initXheditor();
	$(".btn-delivery-time").click(function(){
		var isvalid = 1;
		$(".lu-weight").each(function(){
			var luweight = $(this).val();
			if(isvalid == 1 && (luweight == '' || parseFloat(luweight) < 0.00001)){
				isvalid = 0;
			}
		})
		if(isvalid == 0){
			$.MsgBox.Alert("提示", "请录入正确的产品重量!");
		}
		var ormnum = $(".ormnum").text();
		var time = $(".delivery-time").val();
		var feight = $(".delivery-feight").val();
		var method = $(".delivery-method").val();
		if(time == '' || time == '' || method==''){
			$.MsgBox.Alert("提示", "请确认运费、交期是否准确!");
			return ;
		}
		jQuery.ajax({
			url:"/cbtconsole/bf/time",
			data:{
				"orderNo":ormnum,
				"feight":feight,
				"method":method,
				"time":time
			},
			type:"post",
			success:function(data){
				if(data.state == 200){
					$.MsgBox.Alert("提示", "交期确认成功");
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "失败");
			}
		});
	})
	$(".td-font-view").click(function(){
		var parentsdiv = $(this).parents(".de-td");
		var title = parentsdiv.find(".name-title").text();
		$("#tc_name").html(title);
		var img = parentsdiv.find(".img-de-v").attr("src");
		$(".img-product").attr("src",img);
		var bfdid = parentsdiv.find(".bfdid").val();
		$("#tc_bfdid").val(bfdid);
		var remark = parentsdiv.find(".de-remarl-q").text();
		$("#tc_remark").text(remark);
		var replay = parentsdiv.find(".remark-replay").html();
		$("#remark-replay-content").val(replay);
		$('.tc,.trnasparent,.tc1').show();
	})
	
	$('.trnasparent').click(function(){
		$('.tc,.trnasparent,.tc1').hide();
	});
	$('.transparent-bg').click(function(){
		$('.transparent,.transparent-bg').hide();
	})
	$(".b-add").click(function(){
		var html = '<tr class="sku-td">'+
			'<td><input type="text" class="input-w8 lu_sku" value=""></td>'+
			'<td class="td-price">USD:<span class="lu-price-buy"></span>(CNY:<input type="text" value="" class="lu-price-buy-c input-w1" onchange="changePrice(this)">)</td>'+
		'<td>USD:<input type="text" value="" class="lu-price-sale input-w1">(含运费<input type="text" value="" class="lu-ship-feight input-w1">)</td>'+
			'<td><input type="text" class="input-w6 lu_count" value="0"></td>'+
			'<td><input type="text" class="input-w5 lu_unit"></td>'+
			'<td><input type="text" class="input-w5 lu_url">'+
			'&nbsp;<button class="btn btn-success btn-add">录入</button></td>'+
		'</tr>';
		$(this).parents(".detail-div").find(".lu_tr").append(html);
		bindClick();
	})
	
	$(".btn-finsh").click(function(){
		var valid = 0;
		var time = $(".delivery-time").val();
		var method = $(".delivery-method").val();
		var feight = $(".delivery-feight").val();
		if(time=='' || feight==''||method==''){
			valid = 1;
		}else{
			$(".lu-weight").each(function(){
				var v = $(this).val();
				if(valid == 0 && v == ''){
					valid=1;
				}
			});
		}
		if(valid == 1){
			$.MsgBox.Alert("提示", "请确认交期时间、交期方式、重量信息是否准确");
			return ;
		}
		
		var bfid = $("#query_bf_id").val();
		jQuery.ajax({
			url:"/cbtconsole/bf/finsh",
			data:{
				"bfid":bfid
			},
			type:"post",
			success:function(data){
				if(data.state == 200){
					$.MsgBox.Alert("提示", "成功");
					window.location.reload();
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "失败");
			}
		});
		
		
	})
	$(".btn-re-n").click(function(){
		var orderNo = $(".ormnum").text();
		var remark = $(".remark-dn").val();
		if(remark == ''){
			return ;
		}
		jQuery.ajax({
			url:"/cbtconsole/bf/remark",
			data:{
				"orderNo":orderNo,
				"remark":remark
			},
			type:"post",
			success:function(data){
				if(data.state == 200){
					$.MsgBox.Alert("提示", "成功");
					window.location.reload();
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "失败");
			}
		});
		
		
	})
	bindClick();
	$(".btn-update").click(function(){
		var bfid = $("#query_bf_id").val();
		var trp = $(this).parents(".sku-u-td");
		var trdp = $(this).parents(".de-td");
		var bfdid = trdp.find(".bfdid").val();
		var num = trp.find(".lu_count").val();
		var numiid = trdp.find(".td-numiid").text();
		var price = trp.find(".lu-price-sale").val();
		var priceBuy = trp.find(".lu-price-buy").text();
		var priceBuyc = trp.find(".lu-price-buy-c").val();
		var shipFeight = trp.find(".lu-ship-feight").val();
		var url = trp.find(".lu_url").val();
		var unit = trp.find(".lu_unit").val();
		var sku = trp.find(".lu_sku").val();
		var id = trp.find(".lu_id").val();
		var weight = $(this).parents(".detail-div").find(".lu-weight").val();
		if(num == '' || parseInt(num) < 1 || price == '' || priceBuy==''||priceBuyc==''||url==''||sku==''||unit==''||shipFeight==''){
			$.MsgBox.Alert("提示", "请确认所填信息是否准确!");
			return ;
		}
		jQuery.ajax({
			url:"/cbtconsole/bf/add",
			data:{
				"id":id,
				"bfid":bfid,
				"bfdid":bfdid,
				"num":num,
				"numiid":numiid,
				"price":price,
				"priceBuy":priceBuy,
		    	 "priceBuyc":priceBuyc,
		    	 "shipFeight":shipFeight,
				"url":url,
				"weight":weight,
				"unit":unit,
				"sku":sku
			},
			type:"post",
			success:function(data){
				if(data.state == 200){
					$.MsgBox.Alert("提示", "成功");
					window.location.reload();
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "失败");
			}
		});
		
		
	})
	$(".btn-replay").click(function(){
		var bfdid = $("#tc_bfdid").val();
		var remark = $("#remark-replay-content").val();
		jQuery.ajax({
			url:"/cbtconsole/bf/deremark",
			data:{
				"remark":remark,
				"bfdid":bfdid
			},
			type:"post",
			success:function(data){
				if(data.state == 200){
					$.MsgBox.Alert("提示", "成功");
					window.location.reload();
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "失败");
			}
		});
		
		
	})
	$(".btn-invalid").click(function(){
		var id = $(this).parents(".sku-u-td").find(".lu_id").val();
		jQuery.ajax({
			url:"/cbtconsole/bf/invalid",
			data:{
				"id":id
			},
			type:"post",
			success:function(data){
				if(data.state == 200){
					$.MsgBox.Alert("提示", "成功");
					window.location.reload();
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "失败");
			}
		});
		
		
	})
	$(".btn-weight").click(function(){
		var bfdid = $(this).parents(".de-td").find(".bfdid").val();
		var weight = $(this).parents(".rowweight").find(".lu-weight").val();
		jQuery.ajax({
			url:"/cbtconsole/bf/weight",
			data:{
				"weight":weight,
				"bfdid":bfdid
			},
			type:"post",
			success:function(data){
				if(data.state == 200){
					$.MsgBox.Alert("提示", "成功");
					window.location.reload();
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "失败");
			}
		});
	})
	
	$(".delivery-method").change(function(){
		var method = $(this).val();
		for(var i=0;i<costList.length;i++){
			if(costList[i].shippingmethod.toLowerCase() == method.toLowerCase()){
				$(".delivery-feight").val(costList[i].shippingCost);
				$(".delivery-time").val(costList[i].delivery_time);
				break;
			}
		}
		
	})
	
	$('.img-lazy').lazyload({effect: "fadeIn"});
})


function bindClick(){
	$(".btn-add").click(function(){
		var bfid = $("#query_bf_id").val();
		var trp = $(this).parents(".sku-td");
		var trdp = $(this).parents(".de-td");
		var bfdid = trdp.find(".bfdid").val();
		var num = trp.find(".lu_count").val();
		var numiid = trdp.find(".td-numiid").text();
		var price = trp.find(".lu-price-sale").val();
		var priceBuy = trp.find(".lu-price-buy").text();
		var priceBuyc = trp.find(".lu-price-buy-c").val();
		var shipFeight = trp.find(".lu-ship-feight").val();
		var url = trp.find(".lu_url").val();
		var unit = trp.find(".lu_unit").val();
		var sku = trp.find(".lu_sku").val();
		var weight = $(this).parents(".detail-div").find(".lu-weight").val();
		if(num == '' || parseInt(num) < 1 || price == '' || priceBuy==''||priceBuyc==''||url==''||sku==''||unit==''||shipFeight==''){
			$.MsgBox.Alert("提示", "请确认所填信息是否准确!");
			return ;
		}
	    jQuery.ajax({
		       url:"/cbtconsole/bf/add",
		       data:{
		    	   "bfid":bfid,
		    	   "bfdid":bfdid,
		    	   "num":num,
		    	   "numiid":numiid,
		    	   "price":price,
		    	   "priceBuy":priceBuy,
		    	   "priceBuyc":priceBuyc,
		    	   "shipFeight":shipFeight,
		    	   "url":url,
		    	   "weight":weight,
		    	   "unit":unit,
		    	   "sku":sku
		    		   },
		       type:"post",
		       success:function(data){
		    	   if(data.state == 200){
						$.MsgBox.Alert("提示", "成功");
						window.location.reload();
					}
		       },
		   	error:function(e){
		   		$.MsgBox.Alert("提示", "失败");
		   	}
		   });
		
		
	})
}

function getShippingCost(){
	var countryId = $("#in-country-id").val();
	var weight = 0;
	$(".lu-weight").each(function(){
		var luweight = $(this).val();
		if(luweight != '' && parseFloat(luweight)>0.000001){
			var lucount = parseInt($(this).parents(".de-td").find(".lucount").val());
			if(lucount == 0){
				weight = weight + parseFloat(luweight);
			}else{
				weight = weight + parseFloat(luweight)*lucount;
			}
		}
	})
	if(weight < 0.00001){
		$.MsgBox.Alert("提示", "重量为0获取运费交期失败");
		return ;
	}
	// url:"https://www.import-express.com/shippingCost/getShippingCost",
	 jQuery.ajax({
		       url:"http://192.168.1.66:8087/shippingCost/getShippingCost",
		       data:{
		    	   "countryId":countryId,
		    	   "free":0,
		    	   "weight":weight
		    		   },
		       type:"get",
		       success:function(res){
		    	   if(res.code == 200){
		    		   var gmethod = $("#h-delivery-method").val();
						costList = res.data.transitPricecostList
						var method = '';
		    		   for(var i=0;i<costList.length;i++){
		    			   if(gmethod!='' && costList[i].shippingmethod.toLowerCase() == gmethod.toLowerCase()){
		    				   method +='<option selected="selected" value="'+costList[i].shippingmethod+'">'+costList[i].shippingmethod+'</option>';
		    			   }else{
		    				   method +='<option value="'+costList[i].shippingmethod+'">'+costList[i].shippingmethod+'</option>';
		    			   }
		    		   }
		    		   $(".delivery-method").html(method);
					}
		       },
		   	error:function(e){
		   		$.MsgBox.Alert("提示", "获取运费交期失败");
		   	}
		   });
}


//初始化xheditor
function initXheditor() {
	var orderNo = $(".ormnum").text();
    editorObj = $('#remark-replay-content').xheditor({
        tools: "full",
        html5Upload: false,
        upBtnText: "上传",
        upMultiple: 1,
        upImgUrl: "/cbtconsole/bf/xheditorUploads?orderNo=" + orderNo,
        upImgExt: "jpg,jpeg,gif,png"
    });
}
function changePrice(t){
	var cny = parseFloat($(t).val());
	var price = parseFloat(cny/7.0832).toFixed(2);
	$(t).parents(".td-price").find(".lu-price-buy").text(price);
	
}

