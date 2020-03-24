$(function(){
	$(".btn-lu").click(function(){
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
			'<td><input type="text" class="input-w5 lu_url">'+
			'&nbsp;<button class="btn btn-success btn-add">录入</button></td>'+
		'</tr>';
		$("#lu_tr").append(html);
		
	})
	
	$(".btn-finsh").click(function(){
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
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "失败");
			}
		});
		
		
	})
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
		var sku = trp.find(".lu_sku").val();
		var weight = $(this).parents(".detail-div").find(".lu-weight").val();
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
		    	   "sku":sku
		    		   },
		       type:"post",
		       success:function(data){
		    	   if(data.state == 200){
						$.MsgBox.Alert("提示", "成功");
					}
		       },
		   	error:function(e){
		   		$.MsgBox.Alert("提示", "失败");
		   	}
		   });
		
		
	})
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
		var sku = trp.find(".lu_sku").val();
		var id = trp.find(".lu_id").val();
		var weight = $(this).parents(".detail-div").find(".lu-weight").val();
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
				"sku":sku
			},
			type:"post",
			success:function(data){
				if(data.state == 200){
					$.MsgBox.Alert("提示", "成功");
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "失败");
			}
		});
		
		
	})
	$(".btn-delete").click(function(){
		var bfdid = $(this).parents("").find(".bfdid").val();
		var id = $(this).parents("").find(".lu_id").val();
		jQuery.ajax({
			url:"/cbtconsole/bf/add",
			data:{
				"id":id,
				"bfid":bfid,
				"delete":1
			},
			type:"post",
			success:function(data){
				if(data.state == 200){
					$.MsgBox.Alert("提示", "成功");
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
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "失败");
			}
		});
	})
	$('.img-lazy').lazyload({effect: "fadeIn"});
})

function changePrice(t){
	var cny = parseFloat($(t).val());
	var price = parseFloat(cny/7.0832).toFixed(2);
	$(t).parents(".td-price").find(".lu-price-buy").text(price);
	
}

