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
		var html = '<tr>'+
			'<td><input type="text" class="form-control lu_skuid" value=""></td><td><input type="text" class="form-control lu_count" value="0"></td>'+
			'<td><input type="text" class="lu_url"><button  class="btn btn-success btn-add">录入</button></td></tr>';
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
		var bfdid = trdp.find(".bfdid").text();
		var num = trp.find(".lu_count").val();
		var numiid = trdp.find(".td-numiid").text();
		var price = trdp.find(".td-price").text();
		var url = trp.find(".lu_url").val();
		var sku = trp.find(".lu_sku").val();
	    jQuery.ajax({
		       url:"/cbtconsole/bf/add",
		       data:{
		    	   "bfid":bfid,
		    	   "bfdid":bfdid,
		    	   "num":num,
		    	   "numiid":numiid,
		    	   "price":price,
		    	   "url":url,
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
		var bfdid = trdp.find(".bfdid").text();
		var num = trp.find(".lu_count").val();
		var numiid = trdp.find(".td-numiid").text();
		var price = trdp.find(".td-price").text();
		var url = trp.find(".lu_url").val();
		var sku = trp.find(".lu_sku").val();
		var id = trp.find(".lu_id").val();
		jQuery.ajax({
			url:"/cbtconsole/bf/add",
			data:{
				"id":id,
				"bfid":bfid,
				"bfdid":bfdid,
				"num":num,
				"numiid":numiid,
				"price":price,
				"url":url,
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
	$('.img-lazy').lazyload({effect: "fadeIn"});
})