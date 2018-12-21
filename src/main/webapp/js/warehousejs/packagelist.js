//<script type="text/javascript" src="${path}/js/warehousejs/packagelist.js"></script>




function getPaymentFy(user_id,order_no,index,shipmentno){
	$.ajax({
		type:"post", 
		url:"getPaymentFy.do",
		dataType:"json",
		data:{user_id : user_id,order_no : order_no},
		success : function(data){  //返回受影响的行数
			if(data != '0'){
				$("#khfk"+order_no).html(data.sumPayment_amountRMB);
                $("#payPrice"+order_no).val(data.sumPayment_amountRMB);
				$("#h_khfk"+shipmentno).val(data.sumPayment_amountRMB);
				                 
	//			var yf = $("#yfId"+index).html();  //运费
	//			var cgje = $("#cgje"+order_no).html();  //采购金额
	//			alert(yf+"---"+cgje);                   
				
//				//如果   运费 + 相关订单所有采购费用  - 50美元 > 相关订单所有客户付款  就提醒  项目亏损		
//				var temp = Number(yf)+Number(cgje)-50;
//				alert(temp +"-------已付金额:"+data.sumPayment_amountRMB);
//				if(temp>Number(data.sumPayment_amountRMB)){
//					$("#xmtx"+order_no).html("项目亏损");
//				}else{
//					$("#xmtx"+order_no).html("正常");
//				}
				         
				            
				         
			}else{
				showMsg("<h1 style='color: red;'>1</h1>");
			}
		}
	});
}     


$(document).ready(function(){  //加载完成
	//getFpxProductCode("1","");  //运输方式
	getFpxCountryCode();	 //国家
                                    
})             

var $$ = function(func){  
    if (document.addEventListener) {  
        window.addEventListener("load", func, false);  
    }  
    else if (document.attachEvent) {  
        window.attachEvent("onload", func);  
    }  
}  
//加载完成    
                
$$(function(){  
	                                                                      
})  
                           

function test1(){
	$("input[id^='hgj_']").each(function(){
		
		var orderid=$(this).val();
	                                                      	                               
		var hchinapostbig = $("#hchinapostbig_"+orderid).val();
		//$("#select_id option:last").attr("index")  option").size()
	//	alert($("#fpxCountryCode"+orderid+":option").size());                                          
	//	alert(orderid+"---"+hchinapostbig);    
		getFpxCountryCode(orderid);
		$("#fpxCountryCode"+orderid).find("option[text='"+hchinapostbig+"']").attr("selected",true);
		                                                                                                                                                                                                    							
	                                                                                                                    
	}); 
}
                         
//嘉城运输方式
function getJcYsfs(orderid){
	var radio = $("input[name='ysfs"+orderid+"']:checked").val();  //出货方式  4px  佳成  原飞航
	if(Number(radio) == 3){
		var weight = $("#sweight"+orderid).html(); // 重量
		var volumelwh = $("#svolume"+orderid).html(); // 体积
		var fpxCountryCode = $("#fpxCountryCode"+orderid).val(); // 国家
		                                                 
		$.ajax({                                           
			type:"post", 
			url:"getJcYsfs.do",
			dataType:"text",
			data:{weight : weight,volumelwh : volumelwh,fpxCountryCode : fpxCountryCode},
			success : function(data){  //返回受影响的行数
				if(data != '500' && data != '600'){
					var list = eval("("+data+")");
					$("#fpxProductCode"+orderid).html("");
		 			$("#fpxProductCode"+orderid).append("<option selected=\"selected\">运输方式</option>"); 
		 			for(i=0; i<list.length; i++){
		 				$("#fpxProductCode"+orderid).append("<option value='"+list[i].WLMX+"'>"+list[i].WLMX+"</option>"); 
		 			}
				}else{
					showMsg("<h1 style='color: red;'>佳成运输方式查询失败！</h1>");
				}
			}
		});
	}
}

function yfjs(orderid){
	var radio = $("input[name='ysfs"+orderid+"']:checked").val();
	if(radio==4){
		getFreight_package("${eur}",$("#sweight"+orderid+"").html(),$("#svolume"+orderid+"").html(),$("#z_id"+orderid+"").val(),orderid,"","",$("#h_orderid2"+orderid+"").val(),"Epacket",$("#spid_"+orderid+"").val());
	}else if(radio==2){
		getFreight_package("${eur}",$("#sweight"+orderid+"").html(),$("#svolume"+orderid+"").html(),$("#z_id"+orderid+"").val(),orderid,"","",$("#h_orderid2"+orderid+"").val(),"原飞航",$("#spid_"+orderid+"").val());
	}else if(radio==6){
		getFreight_package("${eur}",$("#sweight"+orderid+"").html(),$("#svolume"+orderid+"").html(),$("#z_id"+orderid+"").val(),orderid,"","",$("#h_orderid2"+orderid+"").val(),"迅邮",$("#spid_"+orderid+"").val());
	}
}

function getYsfs(orderid){
	                              
	var type = $("input[name='ysfs"+orderid+"']:checked").val();  //出货方式  4px  佳成  原飞航  新增中通
	if(Number(type) == 1){
		getFpxProductCode("2",orderid);  //运输方式
	}else if(Number(type) == 2){
		yfhYsfs(orderid);    
	}else if(Number(type) == 3){
		$("#fpxProductCode"+orderid).empty();   
		$("#fpxProductCode"+orderid).append("<option value=''>不要选</option>");         
		getFreight_package("${eur}",$("#sweight"+orderid+"").html(),$("#svolume"+orderid+"").html(),$("#z_id"+orderid+"").val(),orderid,"","",$("#h_orderid2"+orderid+"").val(),"JCEX",$("#spid_"+orderid+"").val());
	}else if(Number(type) == 4){ //邮政           
		getQtFs(orderid);     
	} else if(Number(type) == 5 || Number(type) == 7){ //中通
		$("#fpxProductCode"+orderid).empty();   
		$("#fpxProductCode"+orderid).append("<option value=''>不要选</option>");            
	}  else if(Number(type) == 6){
		yfhYsfs(orderid);    
	}   else if(Number(type) == 7){
        $("#fpxProductCode"+orderid).empty();
        $("#fpxProductCode"+orderid).append("<option value=''>不要选</option>");
    }
	
}           
//原飞航区域
function yfhYsfs(orderid){
	var fpxProductCode = $("#fpxProductCode"+orderid);
	fpxProductCode.empty();
	fpxProductCode.append("<option value=''>区域</option>");
	fpxProductCode.append("<option value='美/加/墨'>美/加/墨</option>");
	fpxProductCode.append("<option value='南非'>南非</option>");
	fpxProductCode.append("<option value='澳洲'>澳洲</option>");
	fpxProductCode.append("<option value='西欧'>西欧</option>");
	fpxProductCode.append("<option value='东欧'>东欧</option> ");
    var yfh_eara=$("#yfh_eara").val();
    if(yfh_eara != null && yfh_eara != ""){
    	$("#fpxProductCode"+orderid).val(yfh_eara);
	}
}


//获取其他运输方式
function getQtFs(orderid){
	var fpxProductCode = $("#fpxProductCode"+orderid);
	$.ajax({
		type:"post", 
		url:"getQtFs.do",
		dataType:"text",
		success : function(data){  //
			var objlist = eval("("+data+")");
			$(fpxProductCode).empty(); 
			fpxProductCode.append("<option value='运输方式'>运输方式</option>");
			for(var i=0; i<objlist.length; i++){
				fpxProductCode.append("<option value='"+objlist[i]+"'>"+objlist[i]+"</option>");
			}
			var yz_type=$("#yz_type").val();
			if(yz_type != null && yz_type != ""){
                $("#fpxProductCode"+orderid).val(yz_type);
			}
		}
	});
}

//国家
function getFpxCountryCode(){
//	alert($("#fpxCountryCode option").size());
//	var fpxCountryCode = $("#fpxCountryCode");

	$.ajax({              
		type:"post",                        
		url:"getFpxCountryCode.do",
		dataType:"text",
		success : function(data){  //返回受影响的行数
			var objlist = eval("("+data+")");
			$("select[id^='fpxCountryCode']").each(function(){
				                       
			});              
			
			$("input[id^='hgj_']").each(function(){
				
				var orderid=$(this).val();
				var hchinapostbig = $("#hchinapostbig_"+orderid).val();
				
				for(var i=0; i<objlist.length; i++){
					if(hchinapostbig == objlist[i].chinese_name) {
						$("#fpxCountryCode"+orderid).append("<option selected='selected' value='"+objlist[i].country_code+"'>"+objlist[i].chinese_name+"</option>");
					}else{
						$("#fpxCountryCode"+orderid).append("<option value='"+objlist[i].country_code+"'>"+objlist[i].chinese_name+"</option>");
					}
				//	$("#fpxCountryCode"+orderid).find("option[text='"+hchinapostbig+"']").attr("selected",true);
				}     
				$("#fpxCountryCode"+orderid).find("option[text='"+hchinapostbig+"']").attr("selected",true);
				                                                                                                                                                                                                    							
			                                                                                                                    
			}); 
		
		}
	});
}

//4px运输方式
function getFpxProductCode(type,orderid){
//	alert($("#fpxCountryCode option").size());
                                   
	$.ajax({
		type:"post", 
		url:"getFpxProductCode.do",
		dataType:"text",
		success : function(data){  //返回受影响的行数
			var objlist = eval("("+data+")");
			if(type == 1){  //初次进来
				$("select[id^='fpxProductCode']").each(function(){
					for(var i=0; i<objlist.length; i++){
						$(this).append("<option value='"+objlist[i].productcode+"'>"+objlist[i].chinesename+"</option>");
					} 
				});                      
			}else{                                                      
				$("#fpxProductCode"+orderid).html("<option value='运输方式'>运输方式</option>");   
				for(var i=0; i<objlist.length; i++){
					$("#fpxProductCode"+orderid).append("<option value='"+objlist[i].productcode+"'>"+objlist[i].chinesename+"</option>");
				} 
			}
		}
	});
}

function getType(t){                                                                      
	if(t==1){
		return "4px";
	}else if(t==2){
		return "原飞航";                              
	}else if(t==3){
		return "JCEX";
	}else if(t==4){
		return "emsinten";
	}else if(t==5){
		return "zto";
	}else if(t==6){
        return "迅邮";
    }else if(t==7){
        return "飞特";
    }
}
//批量出库                                      
function batchCk(){
	$.jBox.tip("正在操作，清稍候", 'loading');                                             
	var tempOrderid;
	var tempFpxCountryCode;
	var tempFpxProductCode;
	var tempSumPrice;
	var tempTransportcompany;
	
	var sbxxList= new Array();  //申报list
	var sbi=0;
	
	var bgList= new Array();  //包裹信息
	var bgi=0;
	var t = 0;    
	
	var len = $("input:checkbox:checked").length;
	if(len==0){
		$.jBox.tip('请选择出库订单','error');                        
				window.setTimeout(function(){ 
					$("#msginfo").html(""); 
				},1500);
	    return ;
	} 
	
	$("input[name='cbox']:checked").each(function(){  
		var h_hxfk = Number($("#h_hxfk"+$(this).val()).val());
		var h_khfk = Number($("#h_khfk"+$(this).val()).val());
		var shipmentno2 = $(this).val();   //包裹号
		var orderid2 = $("#h_orderid"+shipmentno2).val(); //订单号          
		var h_flag = $("#h_flag"+shipmentno2).val();
		if(h_flag!=1){                              
			                     
		}else{    
			var shipmentno = $(this).val();   //包裹号
			var freight = $("#yfId"+shipmentno).val();   //实际运费
			if(freight==""||freight==null){
				freight = 0;
			}
			var estimatefreight = $("#h_yfId"+shipmentno).val();   //估算运费
			if(estimatefreight==""||estimatefreight==null){
				estimatefreight = 0;
			}
			var sweight = $("#sweight"+shipmentno).html();   //重量
			if(sweight.length==0){
			   	return  true;
			}
			var svolume = $("#svolume"+shipmentno).html();   //体积
			var isDropshipFlag = $("#isDropshipFlag"+shipmentno).val();  //dropship标识
			var orderid = $("#h_orderid2"+shipmentno).val(); //订单号
			var fpxCountryCode = $("#fpxCountryCode"+shipmentno).val(); //国家
			var fpxProductCode = $("#fpxProductCode"+shipmentno).val(); //运输方
			var sumPrice = $("#sumPrice"+orderid).val(); //总运费价格
			var transportcompany = $("input[name$='"+shipmentno+"']:checked").val(); //运输公司
			if(transportcompany == 4 && (fpxProductCode == null ||fpxProductCode =="" ||fpxProductCode=="运输方式")){
                $.jBox.tip('邮政出货需要选择具体的运费方式！','error');
                return;
			}
			t=1;
			//申报信息
			var sbzwpm = $("input[name='sbzwpm"+shipmentno+"']").val(); //中文品名
			var sbywpm = $("input[name='sbywpm"+shipmentno+"']").val(); //英文品名
			var sbphbz = $("input[name='sbphbz"+shipmentno+"']").val(); //配货备注式
			var sbsl = $("input[name='sbsl"+shipmentno+"']").val(); //数量
			var sbjg = $("input[name='sbjg"+shipmentno+"']").val(); //价格
            if(sbsl == null || sbsl == "" || sbjg == null || sbjg ==""){
                $.jBox.tip('请输入申报数量和单价！','error');
                return;
            }
			var sbdw = $("input[name='sbdw"+shipmentno+"']").val(); //单位
			var sbxxMap={};                    
			sbxxMap['orderid']= orderid;
			sbxxMap['productname']= sbzwpm;
			sbxxMap['producenglishtname']= sbywpm;
			sbxxMap['productnum']= sbsl;                           
			sbxxMap['productremark']= sbphbz;                  
			sbxxMap['productprice']= sbjg;          
			sbxxMap['productcurreny']= sbdw;  
			sbxxMap['shipmentno']= shipmentno;                        
			sbxxList[sbi] = sbxxMap;
			sbi++;                     
			var bgMap={};
			bgMap['shipmentno']= shipmentno;                    
			bgMap['sweight']= sweight;                    
			bgMap['svolume']= svolume;
			bgMap['orderid']= orderid;
			bgMap['fpxCountryCode']= fpxCountryCode;
			bgMap['fpxProductCode']= fpxProductCode;
			bgMap['transportcompany']= getType(transportcompany);
			bgMap['freight']= freight; 
			bgMap['isDropshipFlag']=isDropshipFlag
			if(estimatefreight==''){
				estimatefreight = freight;
			}
			bgMap['estimatefreight']= estimatefreight;    
			bgList[bgi] = bgMap;
			bgi++;                                                        
		}                                   
	});
	if(t==0){
		$.jBox.tip('没有可出库货物！','error');                        
		window.setTimeout(function(){ 
			$("#msginfo").html(""); 
		},1500);
        return ;
	}
	var mainMap={};  //主
	mainMap['sbxxList']=sbxxList;  //申报信息         
	mainMap['bgList']=bgList;        
	console.log(mainMap);                                                       
	$('#plckid').attr('disabled',"true");
	$.ajax({
		type:"post", 
		url:"batchCk",
		dataType:"json",  
	    contentType : 'application/json;charset=utf-8', 
	    data:JSON.stringify(mainMap),
		success : function(data){  
			
			$("#inshipment").val("批量出库");
			$('#inshipment').removeAttr("disabled"); 
			
			if(Number(data)>0){
				$.jBox.tip('出货成功。', 'success');
		//		$("#msginfo").html("<h1>保存成功！</h1>");
				window.setTimeout(function(){ 
					history.go(0);
				},1500); 
			}else{                                                    
				$.jBox.tip('出货失败。', 'error');                        
		//		$("#msginfo").html("<h1>失败！</h1>");
				window.setTimeout(function(){ 
					$("#msginfo").html(""); 
				},1500); 
			}
			         
		}
	});
}




//发送邮件
function sendMail(userid,orderNo,toEmail,orderarrs,currency){
	
	$.jBox.tip("正在发送邮件...", 'loading');
	
	var sbBuffer = "";
	var path = $("#h_path").val();                                                       
	
	//具体费用
	var fee =$("#sumPrice"+orderNo).val();                                             
	remaining_price = fee;                                      
	         
	fee += " "+currency+"";
	//添加付费页面
//	var feeHere = "<a target='_blank' href='"+path+"/processesServlet?action=getCenter&className=IndividualServlet>here.</a>";  //<a href="#">fee here.</a>
	var feeHere = "<a target='_blank' href='https://www.import-express.com/individual/getCenter'>here.</a>";
	//点击订单能跳到订单详情
	
	var tempStr = "";                                         
	var strTemp = orderarrs.substring(orderarrs.length,orderarrs.length-1);
	if(strTemp == ','){
		orderarrs = orderarrs.substring(0,orderarrs.length-1);
 	}                                              
	if(orderarrs.indexOf(',') != -1){
		var arr = orderarrs.split(',');
		for(var i=0; i<arr.length; i++){ 
			tempStr += "<a target='_blank' href='https://www.import-express.com/individual/getCenter'>"+orderarrs+"</a>,"
		}
		tempStr = tempStr.substring(0,tempStr.length-1);
	}else{ 
		tempStr += "<a target='_blank' href='https://www.import-express.com/individual/getCenter'>"+orderarrs+"</a>"
	}
	                             
	var orders = tempStr;
	sbBuffer +="<table>";

	sbBuffer +="<tr><td>"; //图片
	sbBuffer +="<img src='"+path+"/img/importexpress.png'/>";
	sbBuffer +="</td></tr>";
    sbBuffer +="<tr><td height='10px'></td></tr>";
	
	sbBuffer +="<tr><td>"; //用户邮箱
	sbBuffer += "Dear "+toEmail;
	sbBuffer +="</td></tr>";
	
	sbBuffer +="<tr><td>"; //用户邮箱
	sbBuffer +="Your ImportExpress order is arrived at our warehouse and ready to deliver out, please complete <br/>the delivery fee "+feeHere;
	sbBuffer +="</td></tr>";
	
	sbBuffer +="<tr><td height='10px'></td></tr>";
	
	sbBuffer +="<tr><td>"; //用户邮箱
	sbBuffer +="Total delivery fee:"+fee;
	sbBuffer +="</td></tr>";
	
	sbBuffer +="<tr><td height='10px'></td></tr>";
	
	sbBuffer +="<tr><td>"; //用户邮箱
	sbBuffer +="After we receive the above delivery fee, we will ship out your order ("+orders+")";
	sbBuffer +="</td></tr>";
	
	sbBuffer +="<tr><td height='10px'></td></tr>";
	
	sbBuffer +="<tr><td>"; //用户邮箱
	sbBuffer +="Best regards,";
	sbBuffer +="</td></tr>";
	
	sbBuffer +="<tr><td>"; //用户邮箱
	sbBuffer +="Import-Express.com";
	sbBuffer +="</td></tr>";   
	
	sbBuffer +="<tr><td><div style='background-color : #999999;border-width : 1px;border-color : #01000C;border-style : double;'>"; //用户邮箱
	sbBuffer +="PLEASE NOTE:<br/>"+
    "This email message was sent from a notification-only address that cannot accept incoming email. <br/>"+
    "PLEASE DO NOT REPLY to this message. If you have any questions or concerns, Access your account ";
	sbBuffer += "<a target='_blank' href='https://www.import-express.com/individual/getCenter'>here.</a>";
	sbBuffer +="</div></td></tr>";
	sbBuffer +="</table>";
	
	$("#sbBuffer").html(sbBuffer);  //发送内容
	//	return;
	$.ajax({
		type:"post",                           
		url:"sendMail.do",
		dataType:"text",
		data:{remaining_price:remaining_price,userid:userid,toEmail:toEmail,sbBuffer:sbBuffer,orderNo:orderNo},
		success : function(data){  
			if(data=="1"){
				//alert("发送成功");
				//window.setTimeout(function () { $.jBox.tip('XX已完成。', 'success'); }, 2000);
				$.jBox.tip('发送成功。', 'success');
			}else{
				$.jBox.tip('发送失败。', 'error');             
			}
		}
	});
}

$(document).ready(function(){
    $.ajax({
        type:"post",
        url:"/cbtconsole/freightFeeController/updateFeeCost",
        dataType:"text",
        data:{},
        success : function(data){

        }
    });
})

function insertWarningInfo(orderid){
    var remark=$("#yjremark"+orderid).val();
    var old_remark=$("#yjremark1"+orderid).val();
    if(remark == null || remark == ""){
        alert("请输入运费预警信息");
        return;
    }
    $.ajax({
        type:"post",
        url:"insertWarningInfo",
        dataType:"text",
        data:{remark:remark,orderid:orderid,old_remark:old_remark},
        success : function(data){
            if(data>0){
                $.jBox.tip('提交成功。', 'success');
                $("#yjremark"+orderid).attr('disabled',true);
                $("#inRemark"+orderid).attr('disabled',true);
                $("#plckid").attr('disabled',false);
                $("#spjg1"+orderid).html("允许出运");
            }else{
                $.jBox.tip('提交失败。', 'erro');
            }
        }
    });
}

//检验申报金额是否超出预定金额
function checkAmount(shipmentno,orderid){
    var sbsl = $("input[name='sbsl"+shipmentno+"']").val(); //数量
    var sbjg = $("input[name='sbjg"+shipmentno+"']").val(); //价格
	if(sbsl == null || sbsl == "" || sbjg == null || sbjg ==""){
		return;
	}
    $.ajax({
        type:"post",
        url:"checkAmount",
        dataType:"text",
        data:{sbsl:sbsl,sbjg:sbjg,orderid:orderid},
        success : function(data){
			if(data>0){
                $.jBox.tip('申报金额大于预设金额不允许出库。', 'erro');
				//申报金额超出预设金额不允许出库
                $("#yjremark"+orderid).attr('disabled',false);
                $("#inRemark"+orderid).attr('disabled',false);
                $("#plckid").attr('disabled',true);
                $("#disableWare").val("1");
			}
        }
    });
}
//查看申报注意事项
function openTipInfo(orderid){
    $.ajax({
        type:"post",
        url:"openTipInfo",
        dataType:"text",
        data:{"orderid":orderid},
        success : function(data){
            var objlist = eval("("+data+")");
            var html="";
            for(var i=0; i<objlist.length; i++){
                html +="<tr><td width='11%'>" + objlist[i].country + "</td><td width='11%'>" + objlist[i].regulation + "</td><td width='10%'>" + objlist[i].clear_require + "</td><td width='11%'>" + objlist[i].tax_code + "</td>" + "<td width='11%'>" + objlist[i].re_provider + "</td><td width='11%'>" + objlist[i].no_re_provider + "</td><td width='11%'>" + objlist[i].amount + "</td></tr>";
                html+="<tr><td colspan='7'><button onclick='displayChangeLogInfo()'>关闭</button></td></tr>";
            }
            if(objlist.length<=0){
                html += "<tr><td colspan='6' align='center'>暂无替换记录。</td></tr>";
            }
            html +="</table>";
            var rfddd = document.getElementById("displayChangeLog");
            rfddd.style.display = "block";
            $("#displayChangeLogs").append(html);
        }
    });
}

function displayChangeLogInfo(){
    var rfddd = document.getElementById("displayChangeLog");
    rfddd.style.display = "none";
    $("#displayChangeLogs tbody").html("");
}