$(function(){
	$(".btn-check").click(function(){
		//owsorder.do
		var useridOrOrderno = $(".check-in").val();
		if(!useridOrOrderno || useridOrOrderno==''){
			return ;
		}
		$.ajax({
		       url:"/cbtconsole/warehouse/owsorder.do",
		       data:{
		           "useridOrOrderno" : useridOrOrderno
		       	  },
		       type:"post",
		       success:function(data){
		    	  if(data.status == 200){
		    		  $(".clear-all-table").html("");
		    		  $(".clear-all").text("");
		    		  var tbhtml = '';
		    		 var ows =  data.ows;
		    		  for(var i=0;i<ows.length;i++){
		    			  var stock = ows[i];
		    			  tbhtml += '<tr>';
		    			  tbhtml += ' <td class="datagrid-userid">'+stock['user_id']+'</td>';
		    			  tbhtml += '<td class="datagrid-orderno">'+stock['order_no']+'</td>';
		    			  tbhtml += '</tr>';
		    		  }
		    		  $(".clear-all-table").html(tbhtml);
		    		  
		    		  var owsorder = data.owsorder;
		    		  $('.info-order-userid').text(owsorder['user_id']);
		    		  $('.info-order-orderno').text(owsorder['order_no']);
		    		  $('.info-order-email').text(owsorder.email);
		    		  $('.info-address-country').text(owsorder.Country);
		    		  $('.info-address-state').text(owsorder.statename);
		    		  $('.info-address-city').text(owsorder.address2);
		    		  $('.info-address-code').text(owsorder.zipcode);
		    		  $('.info-address-phone').text(owsorder.phoneNumber);
		    		  $('.info-address-a').text(owsorder.address+owsorder.street);
		    	  }
		       },
		   	error:function(e){
		   		alert("失败");
		   	}
		   })
		
		
		
	})
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
					alert("更新成功");
				}else{
					alert(data.message);
				}
			},
			error:function(e){
				alert("失败");
			}
		})
		
	})
	
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
					
				}
			},
			error:function(e){
				alert("失败");
			}
		})
		
	})
	
	
	
})