<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="shortcut icon" href="/cbtconsole/img/mathematics1.ico"
	type="image/x-icon" />
<title>Customized</title>
<link href="../css/customized.css" rel="stylesheet">
<script src="../js/jquery-1.8.2.min.js"></script>
</head>
<body>
	<div class="customized">
		<div class="location">
			<a href="#">&#60;&#60;&nbsp;Back</a>
		</div>
		<h2 class="cust_title">定制产品:</h2>
		<p class="cust_title" id="cust_title"><a href="" target="_blank"></a></p>
		<p id="proct_link"></p>
		<div class="pic_info">
			<div class="img_box">
				<img src="" width="183"/>
			</div>
			<table class="img_tab">
				<tr>
					<td width="92"><span class="item">Unite Price :</span></td>
					<td><div class="price">
							<span>4.34-4.87</span> <strong>USD</strong> / piece
						</div></td>
				</tr>
				<tr>
					<td><span class="item">Size ：</span></td>
					<td><ul class="size_list">
							
						
								<li>6</li>
							
						</ul></td>
				</tr>
				<tr>
					<td><span class="item">Business Discount ：</span></td>
					<td><div class="ClassDiscount_box">
							&nbsp;&nbsp;<em>5%-20%</em> off if spend over <em>USD300</em> in
							<span> Colothing& Accessories </span>
						</div></td>
				</tr>
			</table>
		</div>
		<div class="mark">
			<div class="size_layer" style="display: none;">
				<a href="#">X</a>
				<h3>size</h3>
				<ul class="size_list">
					<li>3T</li>
					<li>2T</li>
					<li>6</li>
					<li>4T</li>
					<li>8</li>
				</ul>
				<h3>Length Conversion</h3>
				<table class="size_layer_tab">
					<tr>
						<th colspan="3">Length</th>
					</tr>
					<tr>
						<td colspan="2">Imperial</td>
						<td>Metric</td>
					</tr>
					<tr>
						<td>1inch[in]</td>
						<td>-------</td>
						<td>2.54cm</td>
					</tr>
					<tr>
						<td>1 foot[ft]</td>
						<td>12 in</td>
						<td>0.3048cm</td>
					</tr>
					<tr>
						<td>1 yard[yd]</td>
						<td>3 foot</td>
						<td>0.9144cm</td>
					</tr>
				</table>
			</div>
		</div>
		<table class="attribute_tab">
			<tr>
				<td class="item"><strong>Optional<br/> Delivery<br/>
Time
				</strong></td>
				<td><span id="date"></span></td>
			</tr>
		
			<tr>
				<td class="item"><strong>Order<br /> Quantity
				</strong></td>
				<td><div class="number_box">
						<input id="number" class="tbs_text" value="1" readonly>
					
					</div></td>
			</tr>
			<tr>
				<td class="item"><strong>Color</strong></td>
				<td><ul class="attr_list">
						<li></li>
					</ul> <span class="color_txt">#f29cda</span>
					<div class="note_box">
						<span>Note:</span><em></em>
						<textarea id="color_textarea" readonly></textarea>
					</div></td>
			</tr>
			<tr>
				<td class="item"><strong>Size</strong></td>
				<td><div class="size_tab">
				
				<ul style="display: block;">
							<li><span class='w1'>Chest</span> : <em style="width: 103px;"> <input
									type="text" value="" readonly/>
							</em></li>
							<li><span  class='w2'>Length</span> : <em style="width: 119px;"> <input
									type="text" value="" readonly/>
							</em></li>
							<li><span  class='w1'>Hip</span> : <em style="width: 103px;"> <input
									type="text" value="" readonly/>
							</em></li>
							<li><span  class='w2'>Sleeve Length</span> : <em style="width:118px;">
									<input type="text" value="" readonly/>
							</em></li>
							<li><span  class='w1'>Hem</span> : <em style="width:103px;">
									<input type="text" value="" readonly/>
							</em></li>
							<li><span  class='w2'>Shoulder Width</span> : <em style="width:118px;">
									<input type="text" value="" readonly/>
							</em></li>
							
							<li><span  class='w1'>Waist</span> : <em style="width:104px;">
									<input type="text" value="" readonly/>
							</em></li>
							
								<li><span class='w2'>Neck</span> : <em style="width:118px;">
									<input type="text" value="" readonly/>
							</em></li>
								<li><span  class='w1'>unit</span> : <em class="radio_em">
									<i></i>
							</em></li>
						<!-- 	<li><a href="">size conversion</a></li> -->
						</ul>
				
				
				
					</div>
					<form id="uploadForm">
						<p class="chart_p">
							<a href="javascript:;" class="chart_btn"> download Size Chart
							</a> 
						</p>
					</form>
					<p style="padding-top: 15px;" id="file_location" class="file_name"></p>
					<div class="Chart_box">
						<div class="note_box">
							<span>Note:</span><em></em>
							<textarea id="size_note" readonly></textarea>
						</div>
					</div></td>
			</tr>
			<tr>
				<td class="item"><strong>Logo</strong></td>
				<td><div class="upload_btn_box">
						<form id="uploadForm2">
							<div class="up_btn">
								<a href="javascript:;" class="line_one" id="Upload_logo" target="_blank">downLoad Logo
								</a>
							</div>
						</form>
					</div>
					<div class="craft_cont">
						<label> <input type="radio" name="sc" value="0"
							checked="checked" /> Screen Printing
						</label> <label> <input type="radio" name="sc" value="0" />
							Embroidery
						</label> <label> <input type="radio" name="sc" value="0" />
							Thermal Transfer
						</label> <a href="#">Size & Position</a>
					</div>
					<div class="note_box">
						<span>Note:</span><em></em>
						<textarea id="logo_note" readonly></textarea>
					</div></td>
			</tr>
			<tr>
				<td class="item"><strong>Label</strong></td>
				<td><div class="upload_btn_box">
							
						<div class="up_btn"  id="Label_name_a">
							<a href="#"  class="line_one">download Label</a>
						</div>
						<p id="lab_err"></p>
					
						
					</div>
					<div class="note_box">
						<span>Note:</span><em></em>
						<textarea id="label_file_textarea" readonly></textarea>
					</div></td>
			</tr>
			<tr>
				<td class="item"><strong>Tag</strong></td>
				<td><div class="upload_btn_box">
						<form id="uploadForm4">
							<div class="up_btn" id="download_tag">
								<a href="javascript:;" class="line_one">download Tag</a>
							</div>
							<p id="lab_err1"></p>
						</form>
						<p class="file_name" id="tag_file"></p>
					</div>

					<div class="note_box">
						<span>Note:</span><em></em>
						<textarea id="tag_note" readonly></textarea>
					</div></td>

			</tr>

		</table>

		<div class="upload_logo">
			<div class="close_top">
				<a href="#"></a>
			</div>
			<p>
				<strong>Size :</strong>length <input type="text" class="text"
					value="" readonly/> cm &#12288;&#12288; Width <input type="text"
					class="text" value="" readonly/> cm
			</p>
			<div>
				<h4>Please select the location of the logo ：</h4>
				<div class="tara_cbox">
					<table class="tara_tab">
						<tr>
							<td>&nbsp;</td>
							<td colspan="3">Front</td>
							<td colspan="2">Back</td>
							<td>Armband</td>
						</tr>
						<tr>
							<td>Position:</td>
							<td><img src="../img/picg1.jpg"> <input
								type="checkbox" value="0" disabled="disabled" /></td>
							<td><img src="../img/picg2.jpg"> <input
								type="checkbox" value="0" disabled="disabled" /></td>
							<td><img src="../img/picg3.jpg"> <input
								type="checkbox" value="0" disabled="disabled" /></td>
							<td><img src="../img/picg4.jpg"> <input
								type="checkbox" value="0" disabled="disabled" /></td>
							<td><img src="../img/picg5.jpg"> <input
								type="checkbox" value="0" disabled="disabled" /></td>
							<td><img src="../img/picg6.jpg"> <input
								type="checkbox" value="0" disabled="disabled" /></td>
						</tr>
					</table>
					<p class="error">If there are not the options that you want,
						please leave us a message</p>
				</div>
			</div>
			<a href="#" class="comfire">Comfirm</a>
		</div>
		<div class="layer"></div>
		<div class="Standard">
			<img src="../img/size_b.jpg" /> <a href="#" class="closet "></a>
		</div>
	</div>
	<div id="dvMsgBox">
		<div class="top">
			<div class="right">
				<div class="title" id="dvMsgTitle">Prompt</div>
			</div>
		</div>
		<div class="body_comt">
			<div class="right">
				<div class="ct" id="dvMsgCT">success</div>
			</div>
		</div>
		<div class="bottom" id="dvMsgBottom" style="height: 45px;">
			<div class="right">
				<div class="btn" id="dvMsgBtns">
					<input class="btn" value="OK" type="button">
				</div>
			</div>
		</div>
	</div>
	<div id="ShowBolightBox"></div>
	<script>

	
	$(function(){
		$.getUrlParam = function(name)
		{
		var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r!=null) return unescape(r[2]); return null;
		}
		$.ajax({
			url : '../customized/getCustomized',
			method : 'post',
			data : {customizedId:$.getUrlParam('customizedId')},
			dataType:'json',
			success : function(data) {
				$("#number").val(data.quantity);
				$("#date").html(data.delivery_time);
				$(".attr_list li").css("background",data.color);
				//$(".color_txt").css("color",data.color).html();
				$("#color_textarea").val(data.color_note);
				var size_data=jQuery.parseJSON(data.size_data);
				var logo_file=jQuery.parseJSON(data.logo_data);
				
				
				$(".size_tab li:eq(0) input").val(size_data["Chest"]);
				$(".size_tab li:eq(1) input").val(size_data["Length"]);
				$(".size_tab li:eq(2) input").val(size_data["Hem"]);
				$(".size_tab li:eq(3) input").val(size_data["Sleeve Length"]);
				$(".size_tab li:eq(4) input").val(size_data["Hem"]);
				$(".size_tab li:eq(5) input").val(size_data["Shoulder Width"]);
				$(".size_tab li:eq(6) input").val(size_data["Waist"]);
				$(".size_tab li:eq(7) input").val(size_data["Neck"]);
				$(".size_tab li:eq(8) i").html(size_data["unit"]);
				
				
				$("#size_note").val(data.size_note);
				if(data.logo_file){
					$("#Upload_logo").attr("href",data.logo_file);
				}else{
					$("#Upload_logo").parent().hide();
				}
				$(".craft_cont input:eq(0)").val(logo_file["screen_printing"]);
				$(".craft_cont input:eq(1)").val(logo_file["embroidery"]);
				$(".craft_cont input:eq(2)").val(logo_file["thermal_transfer"]);
				$(".upload_logo p input:eq(0)").val(logo_file.length);
				$(".upload_logo p input:eq(1)").val(logo_file.width);
				$(".tara_tab input:eq(0)").val(logo_file.front_1);
				$(".tara_tab input:eq(1)").val(logo_file.front_2);
				$(".tara_tab input:eq(2)").val(logo_file.front_3);
				$(".tara_tab input:eq(3)").val(logo_file.back_1);
				$(".tara_tab input:eq(4)").val(logo_file.back_2);
				$(".tara_tab input:eq(5)").val(logo_file.armband);
				$(".img_box img").attr("src",data.img_url);
				
				$(".tara_tab td input").each(function(index, element) {
				    if($(this).val()==1){
						$(this).attr("checked","checked");
					}
				});
				$(".craft_cont label input").each(function(index, element) {
				    if($(this).val()!=1){
						$(this).parents("label").hide();
					}
				});
				$("#logo_note").val(data.logo_note);
				if(data.label_file){
					$("#Label_name_a a").attr("href",data.label_file);
				}else{
					$("#Label_name_a").hide();
					$("#lab_err").html("Offered by Supplier");
					
				}
				
				
				$("#label_file_textarea").val(data.label_note);
				if(data.tag_file){
					$("#download_tag a").attr("href",data.tag_file);
				}else{
					$("#download_tag").hide();
					$("#lab_err1").html("Offered by Supplier");
					
				}
				$("#tag_note").val(data.tag_note);
				$(".price strong").html(data.price_unit);
				$(".img_tab .price span").html(data.price);
				$("#cust_title a").html(data.pname);
				$("#cust_title a").attr("href",data.related_url);
				$(".size_list li").html(data.size);
				if(data.size_file){
					$(".chart_btn").attr("href",data.size_file);
				}else{
					$("#uploadForm").hide();
				}
				$(".ClassDiscount_box span").html(data.discount_category);
				$(".ClassDiscount_box em:eq(1)").html(data.discount_from);
				$(".ClassDiscount_box em:eq(0)").html(data.discount_rate);
			
				
			},
			error: function (msg) {
			  alert(msg);
            }
		});
		
		$(".craft_cont a").click(function() {
			$(".upload_logo").show();
			$(".layer").show();
			return false;
		});
		$(".close_top a").click(function() {
			$(".upload_logo").hide();
			$(".layer").hide();
			return false;
		});
		$(".location a").click(function(){
			window.history.go(-1);
		});
		$(".comfire").click(function() {
			$(".close_top a").click();
			return false;
		});

	})
		
	</script>	<br/><br/>
</body>
</html>