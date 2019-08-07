$(function(){
	
	 $("#query_button").click(function(){
		doQuery(1);
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
		$("#lu_img").attr("src","");
		 $("#lu_name").html(""); 
		 $("#lu_catid").val(""); 
		 $("#lu_tr").html("");
		$('.tc,.trnasparent,.tc1').show();
		
	});
	$('#tc2').click(function(){
		/*$("#lu_img").attr("src","");
		$("#lu_name").html(""); 
		$("#lu_catid").val(""); 
		$("#lu_tr").html("");*/
		$('.tc,.trnasparent,.tc2').show();
		
	});
	$('.trnasparent').click(function(){
		$('.tc,.trnasparent,.tc1').hide();
		$('.tc,.trnasparent,.tc2').hide();
		$('.tc,.trnasparent,.tc3').hide();
	});
	
})
function getbarcode(v){
	$(v).html("STK111")
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
	    		   topCenter(data.reason); 
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
	    				 trHtml = trHtml+"<span class='lu_skuid'>"+skuM.skuId+"</span></td>";
	    				 trHtml = trHtml+"<td><input type='text' class='form-control lu_count' value='0'></td>";
	    				 trHtml = trHtml+'<td class="lu_barcode"><a onclick="getbarcode(this);"  class="lu_barcode_a">获取库位</a></td>';
	    				 trHtml = trHtml+'<td><input type="checkbox" name="entry" class="lu_is"></td></tr>';
	    			 }
	    		 }else{
	    			 trHtml = trHtml+"<tr><td ><span class='lu_sku'>as picture</span><br>";
    				 trHtml = trHtml+"<span class='lu_specid'>"+skuM.goods_pid+"</span><br>";
    				 trHtml = trHtml+"<span class='lu_skuid'>"+skuM.goods_pid+"</span></td>";
    				 trHtml = trHtml+"<td><input type='text' class='form-control lu_count'  value='0'></td>";
    				 trHtml = trHtml+'<td class="lu_barcode"><a onclick="getbarcode(this);" class="lu_barcode_a" >获取库位</a></td>';
    				 trHtml = trHtml+'<td><input type="checkbox" name="entry" class="lu_is"></td></tr>';
	    		 }
	    		 
	    		 $("#lu_tr").html(trHtml);
	    	   }
	       },
	   	error:function(e){
	   		topCenter("库存录入失败");
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
				topCenter(data.reason); 
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
						trHtml = trHtml+"<span class='lu_skuid'>"+skuM.skuId+"</span></td>";
						trHtml = trHtml+"<td><input type='text' class='form-control lu_count' value='0'></td>";
						trHtml = trHtml+'<td class="lu_barcode"><a onclick="getbarcode(this);"  class="lu_barcode_a">获取库位</a></td>';
						trHtml = trHtml+'<td><input type="checkbox" name="entry" class="lu_is"></td></tr>';
					}
				}else{
					trHtml = trHtml+"<tr><td ><span class='lu_sku'>as picture</span><br>";
					trHtml = trHtml+"<span class='lu_specid'>"+skuM.goods_pid+"</span><br>";
					trHtml = trHtml+"<span class='lu_skuid'>"+skuM.goods_pid+"</span></td>";
					trHtml = trHtml+"<td><input type='text' class='form-control lu_count'  value='0'></td>";
					trHtml = trHtml+'<td class="lu_barcode"><a onclick="getbarcode(this);" class="lu_barcode_a" >获取库位</a></td>';
					trHtml = trHtml+'<td><input type="checkbox" name="entry" class="lu_is"></td></tr>';
				}
				
				$("#lu_tr").html(trHtml);
			}
		},
		error:function(e){
			topCenter("库存录入失败");
		}
	});
}
function doQuery(page) {
	var page = $("#current_page").val();
	var goods_name = $('#query_goods_name').val();
	var goods_pid = $('#query_goods_pid').val();
    var goodscatid = $('#query_goodscatid').val();
	var minintentory = $('#query_minintentory').val();
	var maxintentory = $('#query_maxintentory').val();
	
	window.open("/cbtconsole/inventory/list?page="+page+"&goods_pid="+goods_pid+"&goodscatid="+goodscatid+"&minintentory="+minintentory+"&maxintentory="+maxintentory, "_self");
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

function topCenter(msg){
	$.messager.show({
		title:'消息',
		msg:msg,
		showType:'slide',
		style:{
			right:'',
			top:document.body.scrollTop+document.documentElement.scrollTop,
			bottom:''
		}
	});
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
              topCenter("操作成功");
              $('#dlg4').dialog('close');
              $('#easyui-datagrid').datagrid('reload');
          }else{
              topCenter("修改库存失败:"+data.reason);
          }
      },
      error:function(e){
          topCenter("修改库存失败");
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
			var lu_barcode = $(this).find(".lu_barcode_a").html();
			varray  = varray +";"+lu_sku+"|"+lu_specid+"|"+lu_skuid+"|"+lu_count+"|"+lu_barcode;
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
	       	}else{
	       		alert("库存录入失败");
	       	}
	       },
	   	error:function(e){
	   		alert("库存录入失败");
	   	}
	   });
}

