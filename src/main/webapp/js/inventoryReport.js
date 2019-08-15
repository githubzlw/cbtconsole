$(function(){
	var query_goodscatid_q = $("#query_goodscatid_q").val();
	//获取类别
	jQuery.ajax({
	       url:"/cbtconsole/inventory/catlist",
	       data:{},
	       type:"post",
	       success:function(data){
	    	  if(data.status == 200){
	    		  var catHtml = '<option value="0">All Category</option>';
	    		 for(var i=0;i<data.catSize;i++){
	    			 var cat = data.catList[i];
	    			 catHtml = catHtml+'<option value="'+cat.catid+'"';
	    			 if(query_goodscatid_q == cat.catid){
	    				 catHtml =  catHtml+' selected="selected"';
	    			 }
	    			 catHtml =  catHtml+'>'+cat.catname+'</option>';
	    		 }
	    		 $("#query_catid_select").html(catHtml);
	    		 $("#query_goodscatid").html(catHtml);
	    		 $("#query_goodscatid-in").html(catHtml);
	    	  }else{
	    		  alert(data.reason);
	    	  }
	       },
	   	error:function(e){
	   		alert("获取类别列表失败");
	   	}
	   });
	
	var isCheckStart = $("#check_id").val();
	if(isCheckStart){
		if(isCheckStart != '0'){
			$(".p_q_r").attr("readonly","readonly");
			$(".q_in_r").removeAttr("readonly");
			$(".q_in_barcode").removeAttr("readonly");
			$(".p_qs_r").attr("disabled", "disabled");
//		$(".p_qs_r").attr("style", "background-color: #EEEEEE;");//设为灰色，看起来更像不能操作的按钮
			$("#query_button_check_start").attr("disabled", "disabled");
//		$("#query_button_check_start").attr("style", "background-color: #EEEEEE;");
			
			$(".qbt_check").removeAttr("disabled", "disabled");
//		$(".qbt_check").attr("style", "background-color: #fff;");
		}else{
			$(".qbt_check").attr("disabled", "disabled");
//		$(".qbt_check").attr("style", "background-color: #EEEEEE;");
			
			$("#query_button_check_start").removeAttr("disabled", "disabled");
//		$("#query_button_check_start").attr("style", "background-color: #fff;");
		}
		
		$("#query_catid_select").change(function(){
			var catid = $(this).val();
			$("#query_goodscatid").val(catid);//设置value为xx的option选项为默认选中
			doQuery(1,1);
		})
		$("#query_goodscatid").change(function(){
			var catid = $(this).val();
			$("#query_catid_select").val(catid);//设置value为xx的option选项为默认选中
		})
		 $("#query_button_check").click(function(){
				doQuery(1,1);
			});
	}
	//开始盘点
	$("#query_button_check_start").click(function(){
		var checkCategory = $("#query_catid_select").val();
	    jQuery.ajax({
		       url:"/cbtconsole/inventory/check/start",
		       data:{"checkCategory":checkCategory},
		       type:"post",
		       success:function(data){
		    	  if(data.status == 200){
		    		  $("#check_id").val(data.check_id);
		    		  $("#query_button_check_start").attr("disabled", "disabled");
//		    		  $("#query_button_check_start").attr("style", "background-color: #EEEEEE;");
		    			
		    		  $(".qbt_check").removeAttr("disabled", "disabled");
//		    		 $(".qbt_check").attr("style", "background-color: #fff;");
		    		 $(".p_q_r").attr("readonly","readonly");
		    		 $("#query_catid_select").attr("readonly","readonly");
		    			$(".p_qs_r").attr("disabled", "disabled");
//		    	        $(".p_qs_r").attr("style", "background-color: #EEEEEE;");//设为灰色，看起来更像不能操作的按钮
		    	        $(".q_in_barcode").removeAttr("readonly");
		    		    $(".q_in_r").removeAttr("readonly");
		    	  }else{
		    		  $("#check_id").val(0);
		    		  alert(data.reason);
		    	  }
		       },
		   	error:function(e){
		   		alert("开始盘点失败");
		   	}
		   });
		
	})
	//取消盘点
	$("#query_button_check_cancel").click(function(){
		$(".qbt_check").attr("disabled", "disabled");
//		    		  $(".qbt_check").attr("style", "background-color: #EEEEEE;");
		$("#query_button_check_start").removeAttr("disabled", "disabled");
//		    			$("#query_button_check_start").attr("style", "background-color: #fff;");
		$(".p_q_r").removeAttr("readonly");
		$("#query_catid_select").removeAttr("readonly");
		$(".p_qs_r").removeAttr("disabled");
//		    	        $(".p_qs_r").attr("style", "background-color: #fff");
		$(".q_in_r").attr("readonly","readonly");
		$(".q_in_barcode").attr("readonly","readonly");
		var check_id = $("#check_id").val();
		jQuery.ajax({
		       url:"/cbtconsole/inventory/check/cancel",
		       data:{"check_id":check_id},
		       type:"post",
		       success:function(data){
		    	   $("#check_id").val(0);
		    	  if(data.status == 200){
		    	  }else{
		    		  alert(data.reason);
		    	  }
		       },
		   	error:function(e){
		   		alert("error盘点取消失败");
		   	}
		   });
		doQuery(1,1);
		
	})
	//完成盘点
	$("#query_button_check_done").click(function(){
		var check_id = $("#check_id").val();
		
		$(".qbt_check").attr("disabled", "disabled");
		$("#query_button_check_start").removeAttr("disabled", "disabled");
		$("#check_id").val(0);
		$("#query_catid_select").removeAttr("readonly");
		$(".p_q_r").removeAttr("readonly");
		$(".p_qs_r").removeAttr("disabled");
		$(".q_in_r").attr("readonly","readonly");
		$(".q_in_barcode").attr("readonly","readonly");
		
		window.location.href ="/cbtconsole/inventory/check/done?check_id="+check_id;
		doQuery(1,1);
	})
	
	$("#query_button").click(function(){
		doQuery(1,0);
	});
	
	 $("#luimport").click(function(){
		 $('#dlg6').dialog('open'); 
	 })
	 $('.serial img').click(function(){
		$('.transparent,.transparent-bg').show();
		var src = $(this).attr('src');
		$('.transparent img').attr('src',src);
	});
	$('.transparent-bg').click(function(){
		$('.transparent,.transparent-bg').hide();
	})
	
	$('#tc1').click(function(){
		$("#lu_img").attr("src","https://img.kidsproductwholesale.com/importcsvimg/webpic/img/cl_72/children/banner1.jpg");
		 $("#lu_name").html("产品名称产品名称产品名称产品名称"); 
		 $("#lu_catid").val(""); 
		 var trHtml=''
		 trHtml = trHtml+"<tr><td ><span class='lu_sku'>as picture</span><br>";
		 trHtml = trHtml+"<span class='lu_specid'></span><br>";
		 trHtml = trHtml+"<span class='lu_skuid'></span></td>";
		 trHtml = trHtml+"<td><input type='text' class='form-control lu_count'  value='0'></td>";
		 trHtml = trHtml+'<td class="lu_barcode"><input type="text" placeholder="请输入库位条形码" class="lu_barcode_a"></td>';
		 trHtml = trHtml+'<td><input type="checkbox" name="entry" class="lu_is"></td></tr>';
		 $("#lu_tr").html(trHtml);
		$('.tc,.trnasparent,.tc1').show();
		
	});
	$('#tc2').click(function(){
		$('.tc,.trnasparent,.tc2').show();
		
	});
	$('.trnasparent').click(function(){
		$('.tc,.trnasparent,.tc1').hide();
		$('.tc,.trnasparent,.tc2').hide();
		$('.tc,.trnasparent,.tc3').hide();
	});
	
})


/**更新盘点
 * @param index
 * @returns
 */
function updateCheckRecord(index){
	var tr = $("#datagrid-row-r2-2-"+index);
	var check_id = $("#check_id").val();
	var in_id = $("#datagrid-row-r2-2-"+index).find(".q_inventory_id").val();
	var record_id = $("#datagrid-row-r2-2-"+index).find(".q_record_id").val();
	
	var before_barcode= $("#datagrid-row-r2-2-"+index).find(".q_in_barcode_h").val(); 
	var after_barcode= $("#datagrid-row-r2-2-"+index).find(".q_in_barcode").val(); 
	var inventory_remaining= $("#datagrid-row-r2-2-"+index).find(".datagrid-cell-c2-remaining").html(); 
	var check_remaining= $("#datagrid-row-r2-2-"+index).find(".c_remaining").val(); 
	
	var goods_pid= $("#datagrid-row-r2-2-"+index).find(".datagrid-cell-c2-goodsPid").html(); 
	var goods_sku= $("#datagrid-row-r2-2-"+index).find(".emsku").html(); 
	var goods_specid= $("#datagrid-row-r2-2-"+index).find(".emspecid").html(); 
	var goods_skuid= $("#datagrid-row-r2-2-"+index).find(".emskuid").html(); 
	var goods_price= $("#datagrid-row-r2-2-"+index).find(".emprice").html(); 
	jQuery.ajax({
	       url:"/cbtconsole/inventory/check/update",
	       data:{
	           "check_id" : check_id,
	           "in_id" : in_id,
	           "record_id" : record_id,
	           "inventory_remaining" : inventory_remaining,
	           "check_remaining" : check_remaining,
	           "goods_pid" : goods_pid,
	           "goods_sku" : goods_sku,
	           "goods_specid" : goods_specid,
	           "before_barcode" : before_barcode,
	           "after_barcode" : after_barcode,
	           "goods_skuid" : goods_skuid,
	           "goods_price" : goods_price
	       	  },
	       type:"post",
	       success:function(data){
	    	  if(data.status == 200){
	    		  $("#datagrid-row-r2-2-"+index).find(".q_record_id").val(data.recordId);
	    	  }
	       },
	   	error:function(e){
	   		alert("库存录入失败");
	   	}
	   });
}

/**获取库位
 * @param v
 * @param goods_pid
 * @returns
 */
function getbarcode(v,goods_pid){
	jQuery.ajax({
	       url:"/cbtconsole/inventory/get/barcode",
	       data:{
	           "goods_pid" : goods_pid
	       	  },
	       type:"post",
	       success:function(data){
	    	  if(data.status == 200){
	    		  $(v).html(data.barcode)
	    	  }
	       },
	   	error:function(e){
	   		alert("库存录入失败");
	   	}
	   });
}
/**
 * 获取产品
 * @returns
 */
function getProduct(){
	var goods_pid = $("#lu_pid").val();
	if(!goods_pid || goods_pid==''){
		return ;
	}
	
	jQuery.ajax({
	       url:"/cbtconsole/inventory/get/product",
	       data:{
	           "goods_pid" : goods_pid
	       	  },
	       type:"post",
	       success:function(data){
	    	   if(data.status== 500){
	    		   alert(data.reason); 
	    	   }else{
	    	     $("#lu_img").attr("src",data.goodsImg);
	    		 $("#lu_name").html(data.goodsName); 
	    		 $("#lu_catid").val(data.goodsCatid); 
	    		 $("#lu_price").val(data.goodsPice); 
	    	     var trHtml = '';
	    		 if(data.skuListSize > 0){
	    			 for(var i=0;i<data.skuListSize;i++){
	    				 var skuM = data.skuList[i];
	    				 trHtml = trHtml+"<tr><td ><span class='lu_sku'>"+skuM.sku+"</span><br>";
	    				 trHtml = trHtml+"<span class='lu_specid'>"+skuM.specId+"</span><br>";
	    				 trHtml = trHtml+"<span class='lu_skuid'>"+skuM.skuId+"</span>";
	    				 
	    				 trHtml = trHtml+"<input type='hidden' class='lu_sku_img' value='"+skuM.skuimg+"'></td>";
	    				 
	    				 trHtml = trHtml+"<td><input type='text' class='form-control lu_count' value='0'></td>";
	    				 trHtml = trHtml+'<td class="lu_barcode"><input type="text" placeholder="请输入库位条形码" class="lu_barcode_a"></td>';
//	    				 trHtml = trHtml+'<td class="lu_barcode"><a onclick="getbarcode(this,\''+skuM.goods_pid+'\');"  class="lu_barcode_a">获取库位</a></td>';
	    				 trHtml = trHtml+'<td><input type="checkbox" name="entry" class="lu_is"></td></tr>';
	    			 }
	    		 }else{
	    			 trHtml = trHtml+"<tr><td ><span class='lu_sku'>as picture</span><br>";
    				 trHtml = trHtml+"<span class='lu_specid'>"+skuM.goods_pid+"</span><br>";
    				 trHtml = trHtml+"<span class='lu_skuid'>"+skuM.goods_pid+"</span></td>";
    				 trHtml = trHtml+"<td><input type='text' class='form-control lu_count'  value='0'></td>";
    				 trHtml = trHtml+'<td class="lu_barcode"><input type="text" placeholder="请输入库位条形码" class="lu_barcode_a"></td>';
//    				 trHtml = trHtml+'<td class="lu_barcode"><a onclick="getbarcode(this,\''+skuM.goods_pid+'\');" class="lu_barcode_a" >获取库位</a></td>';
    				 trHtml = trHtml+'<td><input type="checkbox" name="entry" class="lu_is"></td></tr>';
	    		 }
	    		 
	    		 $("#lu_tr").html(trHtml);
	    	   }
	       },
	   	error:function(e){
	   		alert("库存录入失败");
	   	}
	   });
}
/**
 * 获取产品
 * @returns
 */
function getTbOrder(){
	var tb_order_shipno = $("#tb_order_shipno").val();
	if(!tb_order_shipno || tb_order_shipno==''){
		return ;
	}
	
	jQuery.ajax({
		url:"/cbtconsole/inventory/get/tborder",
		data:{
			"order_shipno" : tb_order_shipno
		},
		type:"post",
		success:function(data){
			if(data.status== 500){
				alert(data.reason); 
			}else{
				var trHtml = '';
				var tb_h_shipno = '';
				var tb_h_order = '';
				for(var i=0;i<data.tbGoodsSize;i++){
					var skuM = data.tbGoodsList[i];
					tb_h_shipno = skuM.shipno;
					tb_h_order = skuM.orderid;
					trHtml = trHtml+'<tr class="lu_tb_tr_c"><td class="lu_tb_index'+i+'">'+i+'</td>';
					trHtml = trHtml+'<td class="lu_tb_name lu_tb_name'+i+'">'+skuM.itemname+'</td>';
					trHtml = trHtml+'<td><img src="'+skuM.imgurl+'" alt="" class="img-responsive"></td>';
					trHtml = trHtml+'<td class="lu_tb_skuc">Sku:<span  class="lu_tb_sku lu_tb_sku'+i+'">'+skuM.sku+'</span><br>';
					trHtml = trHtml+'Skuid:<span  class="lu_tb_skuid lu_tb_skuid'+i+'">'+skuM.skuID+'</span><br>';
					trHtml = trHtml+'Specid<span  class="lu_tb_specidc lu_tb_specidc'+i+'">'+skuM.specId+'</span></td>';
					trHtml = trHtml+'<td class="llu_tb_count u_tb_count'+i+'">'+skuM.itemqty+'</td>';
					trHtml = trHtml+'<td><input type="text" class="form-control lu_tb_a_count lu_tb_a_count'+i+'" value="'+skuM.itemqty+'"></td>';
					trHtml = trHtml+'<td class="lu_tb_bar lu_tb_bar'+i+'"><input type="text" placeholder="请输入库位条形码" class="lu_tb_barcode"></td>';
//					trHtml = trHtml+'<td class="lu_tb_bar lu_tb_bar'+i+'"><a class="gain lu_tb_barcode lu_tb_barcode'+i+'" onclick="getbarcode(this,\''+skuM.itemid+'\');">获取库位</a></td>';
					trHtml = trHtml+'<td><input type="checkbox" class="lu_tb_checkbox lu_tb_checkbox'+i+'" value="'+i+'">';
					trHtml = trHtml+'<input type="hidden" class="lu_tb_pid lu_tb_pid'+i+'" value="'+skuM.itemid+'">';
					trHtml = trHtml+'<input type="hidden" class="lu_tb_img lu_tb_img'+i+'" value="'+skuM.imgurl+'">';
					trHtml = trHtml+'<input type="hidden" class="lu_tb_url lu_tb_url'+i+'" value="'+skuM.itemurl+'"></td>';
					trHtml = trHtml+'<input type="hidden" class="lu_tb_price lu_tb_price'+i+'" value="'+skuM.itemprice+'"></td>';
				}
				
				$("#tb_h_order").val(tb_h_order);
				$("#tb_h_shipno").val(tb_h_shipno);
				$("#lu_tb_tr").html(trHtml);
			}
		},
		error:function(e){
			alert("库存录入失败");
		}
	});
}
function doQuery(page,flag) {
	var page = $("#current_page").val();
	var goods_name = $('#query_goods_name').val();
	var goods_pid = $('#query_goods_pid').val();
	var minintentory = $('#query_minintentory').val();
	var maxintentory = $('#query_maxintentory').val();
	var queryLine = $('#query_line').val();
	if(flag == 0){
		var goodscatid = $('#query_goodscatid-in').val();
		window.open("/cbtconsole/inventory/list?page="+page+"&goods_pid="+goods_pid+"&goodscatid="+goodscatid+"&minintentory="+minintentory+"&maxintentory="+maxintentory+"&isline="+queryLine, "_self");
	}else{
		var goodscatid = $('#query_goodscatid').val();
		var check_id = $("#check_id").val();
		window.open("/cbtconsole/inventory/check/list?page="+page+"&goods_pid="+goods_pid+"&goodscatid="+goodscatid+"&minintentory="+minintentory+"&maxintentory="+maxintentory+"&isline="+queryLine+"&check_id="+check_id, "_self");
	}
}

function doReset(){
	$('#query_goods_name').val("");
	$('#query_goods_pid').val("");
	$('#query_minintentory').val("");
	$('#query_maxintentory').val("");
    $('#query_goodscatid').combobox('setValue','0');
}

function BigImg(img){
	htm_="<img width='400px' height='400px' src="+img+">";
	$("#big_img").append(htm_);
	$("#big_img").css("display","block");
}

function closeBigImg(){
	$("#big_img").css("display","none");
	$('#big_img').empty();
}

/* 
*type： 0-单个产品库存进去  1- 头部按钮进去
*index 产品库存序号
*in-id 库存表id
*/
function updateInventory(type,index,in_id){
	$("#index_igoodsID").val('');
	$("#index_iskuid").val('');
	$("#index_ispecid").val('');
	$("#index_igoodsname").html('');
	$("#index_isku").html('');
	$("#index_iremaining").html('');
	$("#index_icanremaining").html('');
	$("#index_ichangcount").val('');
	$("#index_iremark").val('');
	$("#index_iimg").attr('src','');
	$("#index_in_id").val('0');
	
	if(index && index!=''){
		var trd = $("#datagrid-row-r2-2-"+index);
		$("#index_igoodsID").val(trd.find(".datagrid-cell-c2-goodsPid").text());
		$("#index_iskuid").val(trd.find(".emskuid").text());
		$("#index_ispecid").val(trd.find(".emspecid").text());
		$("#index_igoodsname").html(trd.find(".datagrid-cell-c2-goodsName").text());
		$("#index_isku").html(trd.find(".emsku").text());
		$("#index_iremaining").html(trd.find(".datagrid-cell-c2-remaining").text());
		$("#index_icanremaining").html(trd.find(".datagrid-cell-c2-canRemaining").text());
		$("#index_iimg").attr("src",trd.find(".datagrid-cell-c2-carImg img").attr("src"));
		$("#index_ichangcount").val('0');
		$("#index_in_id").val(in_id);
	}
	$('.tc,.trnasparent,.tc3').show();
}
/*
* 库存报损
*/
function addLoss(){
 var igoodsId=$("#index_igoodsID").val();
 var iskuid= $("#index_iskuid").val();
 var ispecid= $("#index_ispecid").val();
 var changeNumber= $("#index_ichangcount").val();
 var remark=$("#index_iremark").val();
	var  change_type = "0"; 
 $(".radio_change").each(function(){
	   if($(this).is(':checked')){
		   change_type = $(this).val();
	   }
 })
  var in_id = $("#index_in_id").val();
  jQuery.ajax({
      url:"/cbtconsole/inventory/addLoss",
      data:{
          "igoodsId":igoodsId,
          "iskuid":iskuid,
          "ispecid":ispecid,
          "changeNumber":changeNumber,
			"remark":remark,
			"in_id":in_id,
			"change_type":change_type
      },
      type:"post",
      success:function(data){
          var status = data.status
          if(status == 200){
        	  $('.tc,.trnasparent,.tc3').hide();
              window.location.reload();
          }else{
        	  alert("修改库存失败:"+data.reason);
          }
      },
      error:function(e){
    	  alert("修改库存失败");
      }
  });
}



//导出报表
function exportData(){
	//生成报表
	var have_barcode=$('#have_barcode').combobox('getValue');
	var flag =$('#flag').val();
	var type =$('#type').val();
	var goodinfo =$('#goodinfo').val();
	var scope =$('#scope').val();
	var count =$('#count').val();
	var sku =$('#sku').val()
	var barcode =$('#barcode').val();
	var type1 =$('#type1').val();
	var type_="0";
	var startdate = $("#startdate").val();
	var enddate = $("#enddate").val();
  var goodscatid=$('#goodscatid').combobox('getValue');
	if(type1!=null){
		type_=type1;
	}
	if(goodscatid == "全部"){
      goodscatid="abc";
	}else if(goodscatid == "其他"){
      goodscatid="bcd"
	}
	window.location.href ="/cbtconsole/inventory/exportGoodsInventory?startdate="+startdate+"&enddate="+enddate+"&type="+type+"&goodinfo="+goodinfo+"&scope="+scope+"&count="+count+"&sku="+sku+"&type_="+type_+"&barcode="+barcode+"&flag="+flag+"&goodscatid="+goodscatid;
}

function saveInventory(){
	var lu_pid = $("#lu_pid").val();
	var lu_name = $("#lu_name").html();
	var lu_img = $("#lu_img").attr("src");
	var lu_catid = $("#lu_catid").val();
	var lu_price = $("#lu_price").val();
	var varray = "";
	$("#lu_tr tr").each(function(){
		if($(this).find(".lu_is").is(':checked')){
			var lu_sku = $(this).find(".lu_sku").html();
			var lu_specid = $(this).find(".lu_specid").html();
			var lu_skuid = $(this).find(".lu_skuid").html();
			var lu_count = $(this).find(".lu_count").val();
			var lu_barcode = $(this).find(".lu_barcode_a").val();
			var lu_sku_img = $(this).find(".lu_sku_img").val();
			varray  = varray +";"+lu_sku+"|d|"+lu_specid+"|d|"+lu_skuid+"|d|"+lu_count+"|d|"+lu_barcode+"|d|"+lu_sku_img;
		}
	})
	var reasonType = "0";
	$(".lu_reason").each(function(){
		if($(this).is(':checked')){
			reasonType = $(this).val();
		 }
	})
	var remark = $("#lu_remark").val();
	jQuery.ajax({
	       url:"/cbtconsole/inventory/input",
	       data:{
	    	   "lu_price" : lu_price,
	           "lu_pid" : lu_pid,
	           "lu_name":lu_name,
	           "lu_img":lu_img,
	       	  "lu_catid":lu_catid,
	       	  "varray":varray,
	       	  "reasonType":reasonType,
	       	  "isTBOrder":0,
	       	  "remark":remark
	       	  },
	       type:"post",
	       success:function(data){
	       	if(data.status==200){
				alert("库存录入成功");
				$('.tc,.trnasparent,.tc1').hide();
				location.reload();
	       	}else{
	       		alert("库存录入失败");
	       	}
	       },
	   	error:function(e){
	   		alert("库存录入失败");
	   	}
	   });
}
function saveTbInventory(){
	var tb_h_order = $("#tb_h_order").val();
	var tb_h_shipno = $("#tb_h_shipno").val();
	var varray = "";
	$("#lu_tb_tr tr").each(function(){
		if($(this).find(".lu_tb_checkbox").is(':checked')){
			
			var lu_name = $(this).find(".lu_tb_name").html();
			var lu_pid = $(this).find(".lu_tb_pid").val();
			var lu_img = $(this).find(".lu_tb_img").val();
			var lu_url = $(this).find(".lu_tb_url").val();
			var lu_sku = $(this).find(".lu_tb_sku").html();
			var lu_specid = $(this).find(".lu_tb_specidc").html();
			var lu_skuid = $(this).find(".lu_tb_skuid").html();
			var lu_count = $(this).find(".lu_tb_a_count").val();
			var lu_barcode = $(this).find(".lu_tb_barcode").val();
			var lu_price = $(this).find(".lu_tb_price").val();
			varray  = varray +";"+lu_sku+"|d|"+lu_specid+"|d|"+lu_skuid+"|d|"+lu_count+"|d|"+lu_barcode+"|d|"+lu_name+"|d|"+lu_pid+"|d|"+lu_img+"|d|"+lu_url+"|d|"+lu_price;
		}
	})
	var reasonType = "0";
	$(".lu_tb_reason").each(function(){
		if($(this).is(':checked')){
			reasonType = $(this).val();
		}
	})
	var remark = $("#lu_tb_remark").val();
	jQuery.ajax({
		url:"/cbtconsole/inventory/input",
		data:{
			"tb_order" : tb_h_order,
			"tb_shipno" : tb_h_shipno,
			"varray":varray,
			"reasonType":reasonType,
			"isTbOrder":"1",
			"remark":remark
		},
		type:"post",
		success:function(data){
			if(data.status==200){
				alert("库存录入成功");
				$('.tc,.trnasparent,.tc2').hide();
				location.reload();
			}else{
				alert("库存录入失败");
			}
		},
		error:function(e){
			alert("库存录入失败");
		}
	});
}

