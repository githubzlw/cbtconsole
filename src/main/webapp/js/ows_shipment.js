$(function(){
	//查询
	$(".btn-check").click(function(){
		//owsorder.do
		clickCheck();
	})
	//添加运单号
	$(".btn-shipno").click(function(){
		//owsorder.do
		var orderno = $(".info-order-orderno").text();
		var shipno = $("#in-shipno").val();
		if(!shipno || shipno==''){
			return ;
		}
		$.ajax({
			url:"/cbtconsole/warehouse/shipno.do",
			data:{
				"shipno" : shipno,
				"orderno" : orderno
			},
			type:"post",
			success:function(data){
				if(data.status == 200){
					
				}else{
					$.MsgBox.Alert("提示", "添加运单号错误:"+data.message);
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "添加运单号失败");
			}
		})
		
	})
	//出运
	$(".btn-shipout").click(function(){
		var shipno = $("#in-shipno").val();
		if(!shipno || shipno==''){
			return ;
		}
		$.ajax({
			url:"/cbtconsole/warehouse/shipout.do",
			data:{
				"shipno" : shipno
			},
			type:"post",
			success:function(data){
				if(data.status == 200){
					
				}else{
					$.MsgBox.Alert("提示", "出运错误:"+data.message);
				}
			},
			error:function(e){
				$.MsgBox.Alert("提示", "出运失败");
			}
		})
		
	})
	
})
function clickStock(t){
	var orderno = $(t).attr("name");
	$(".check-in").val(orderno);
	clickCheck();
}
function clickCheck(){
	var useridOrOrderno = $(".check-in").val();
	if(!useridOrOrderno || useridOrOrderno==''){
		return ;
	}
	$.ajax({
	       url:"/cbtconsole/warehouse/owsorder",
	       data:{
	           "useridOrOrderno": useridOrOrderno
	       	  },
	       type:"post",
	       success:function(data){
	    	  if(data.status == 200){
	    		  $(".clear-all-table").html("");
	    		  $(".clear-all").text("");
	    		  var tbhtml = '';
	    		 var ows =  eval(data.ows);
	    		  for(var i=0;i<ows.length;i++){
	    			  var stock = ows[i];
	    			  tbhtml += '<tr>';
	    			  tbhtml += ' <td class="datagrid-userid">'+stock['user_id']+'</td>';
	    			  tbhtml += '<td class="datagrid-orderno"><a name="${stock.order_no}" onclick="clickStock(this)">'+stock['order_no']+'</a></td>';
	    			  tbhtml += '</tr>';
	    		  }
	    		  $(".clear-all-table").html(tbhtml);
	    		  
	    		  var owsorder = data.owsorder;
	    		  $('.info-order-userid').text(owsorder.user_id);
	    		  $('.info-order-orderno').text(owsorder.order_no);
	    		  $('.info-order-email').text(owsorder.email);
	    		  $('.info-address-country').text(owsorder.Country);
	    		  $('.info-address-state').text(owsorder.statename);
	    		  $('.info-address-city').text(owsorder.address2);
	    		  $('.info-address-code').text(owsorder.zipcode);
	    		  $('.info-address-phone').text(owsorder.phoneNumber);
	    		  $('.info-address-a').text(owsorder.address+owsorder.street);
	    		  $('.in-shipno').val(owsorder.shipmentno);
	    		  if(owsorder.shipmentno !=''){
	    			  $(".btn-shipno").hide();
	    		  }
	    	  }else{
	    		  $.MsgBox.Alert("提示", "获取订单信息错误:"+data.message);
	    	  }
	       },
	   	error:function(e){
	   		$.MsgBox.Alert("提示", "获取订单信息失败");
	   	}
	   })
}
