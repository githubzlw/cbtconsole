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
	
	$("").click(function(){
		
		
		
		
	})
})