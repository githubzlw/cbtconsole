<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
<link rel="stylesheet" type="text/css"
    href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
    href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css"
    href="/cbtconsole/jquery-easyui-1.5.2/demo/demo.css">
<script type="text/javascript"
    src="/cbtconsole/jquery-easyui-1.5.2/jquery.min.js"></script>
<script type="text/javascript"
    src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
<script type="text/javascript"
    src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
<title>替换尺码表</title>
<style>
*{padding:0;margin:0;list-style:none;}
.shopid_box{width:100%;overflow:hidden;margin-bottom:20px;}
.ul_list{width:100%;overflow:hidden;}
.li_list{float:left;width:40%;margin-right:10px;margin-bottom:10px;position:relative;overflow: hidden;height: 305px;border:1px solid #ccc;}   
.li_mask{width:100%;height:100%;position:absolute;left:0;right:0;bottom:0;top:0;margin:auto;z-index:10;background:rgba(0,0,0,0.6);color:greenyellow;font-size:16px;text-align:center}
.li_list .imgs{width:100%;height:270px;overflow:hidden;}
.li_list img{display:block;max-width:100%;max-height:100%;width:auto;height:auto;}
.li_list span{display:block;width:90%;overflow:hidden;font-size:14px;line-height:20px;color:#666;white-space:nowrap;text-overflow:ellipsis;
    position: absolute;bottom: 3px;left: 10px;}
    
    

.shopid{float:left;height:34px;line-height:34px;}
.shopid_p{float:left;margin-left:15px;height:34px;line-height:34px;}
.submitButton{display:inline-block;height:34px;line-height:34px;padding:0 20px;cursor:pointer;background:pink;border-radius:4px;font-size:14px;}
.submitButton:hover{background:blue;color:#fff;}
.submitButton:active{background:#999;color:red}
.shipid_txt{display:inline-block;margin-left:15px;line-height:34px;font-size:14px;}
.li_list input{}

.singleButton{display:block;overflow:hidden;font-size:14px;line-height:20px;color:#666;white-space:nowrap;text-overflow:ellipsis;
    position: absolute;bottom: 3px;left: 10px;display:inline-block;height:34px;line-height:34px;padding:0 20px;cursor:pointer;background:pink;border-radius:4px;font-size:14px;}
.singleButton:hover{background:blue;color:#fff;}
.singleButton:active{background:#999;color:red}
.sssd{width:40%;}
.transparent{width:100%;height:100%;background-color:rgba(0,0,0,.8);position:fixed;top:0;left:0;display:none;text-align:center;z-index:100;}
.transparent img{max-width:100%;max-height:100%;width:auto;height:auto;}
</style>
</head>
<body>
<div class="transparent">
    <img src="">
</div>
<div class="shopid_box">
<h2 class="shopid">一级类别:</h2>
<select class="catoption_lv1" value="">
<option class="311">童装</option>
<option class="312">内衣</option>
<option class="127380009">运动服饰</option>
<option class="10166">女装</option>
<option class="10165">男装</option>
</select>
<select class="catoption_minlv">
</select>
<p class="shopid_p">
<span class="submitButton" >替换已上传的尺码表</span>
</p>
</div>
</body>
<script type="text/javascript">
$(document).ready(function(){
	$(".catoption_lv1").change(function(){
		var opt = $(".catoption_lv1").val();
		if(opt=='童装'){loadCategoryName('311');}
		if(opt=='内衣'){loadCategoryName('312');}
		if(opt=='运动服饰'){loadCategoryName('127380009');}
		if(opt=='女装'){loadCategoryName('10166');}
		if(opt=='男装'){loadCategoryName('10165');}
	});
	$(".catoption_minlv").change(function(){
        var optHtml = $(".catoption_minlv").val();
        var opObj = $(".catoption_minlv").find("option");
        for(var i=0;i<opObj.length;i++){
        	if($(opObj[i]).html()==optHtml){
        		getSizeChart($(opObj[i]).attr("class"));
        		break;
        	}
        }
    });
	loadCategoryName('311');
	
	//阻止浏览器默认行，从而能实现自定义拖拽图片功能
    $(document).on({ 
        dragleave:function(e){    //拖离 
            e.preventDefault(); 
        }, 
        drop:function(e){  //拖后放 
            e.preventDefault(); 
        }, 
        dragenter:function(e){    //拖进 
            e.preventDefault(); 
        }, 
        dragover:function(e){    //拖来拖去 
            e.preventDefault(); 
        } 
    });
});

function uploadImg(rowid,imgFile){
    //获取图片文件
    //var file = imgFile.files[0];//文件对象
    var file = imgFile;
    var name = file.name;//图片名
    //检测浏览器对FileReader的支持
    if(window.FileReader) {
        var reader = new FileReader();
        reader.onload = function(){//异步方法,文件读取成功完成时触发
            var dataImg = reader.result;//文件一旦开始读取，无论成功或失败，实例的 result 属性都会被填充。如果读取失败，则 result 的值为 null ，否则即是读取的结果
            syncUpload(rowid,name,dataImg);
        }
        reader.readAsDataURL(file);//将文件读取为 DataURL
    }else{
        console.log("浏览器不支持H5的FileReader!");
    }
}

function syncUpload(rowid,name,dataImg){
    var imgFile = dataImg.replace(/\+/g,"#wb#");//将所有“+”号替换为“#wb#”
    imgFile = imgFile.substring(imgFile.indexOf(",")+1);//截取只保留图片的base64部分,去掉了data:image/jpeg;base64,这段内容
    imgFile = encodeURIComponent(imgFile);//把字符串作为 URI 组件进行编码。后台容器会自动解码一次
    name = encodeURIComponent(encodeURIComponent(name));//这里对中文参数进行了两次URI编码，后台容器自动解码了一次，获取到参数后还需要解码一次才能得到正确的参数内容
    var mydata = "method=syncUpload&imgFile="+imgFile+"&imgName="+name+"&rowid="+rowid;
    $.ajax({
         url: "/cbtconsole/order/uploadImg",
         data:mydata,
         type: "post",
         dataType: "json",
         success: function(data){
             console.log(data);
         }
     });
}

function previewImg(thisObj,imgfie){
	var windowURL = window.URL || window.webkitURL;
	var $img = $($(thisObj).parent().find("img")[0]);
	var dataURL = windowURL.createObjectURL(imgfie);
    //允许上传的图片格式  
//     var newPreview = imgfie.type;
//     var regext = /\.jpg$|\.gif$|\.jpeg$|\.png$|\.bmp$/gi;
//     if (!regext.test(newPreview)) {
//         newPreview=="";
//         alert("选择的照片格式不正确，请重新选择！");
//         $(thisObj).after($(thisObj).clone($(fileObj)));
//         $(thisObj).remove();
//         return false;
//     }
    var rowid = $(thisObj).parent().find(".rowid").val();
    $img.attr("src", dataURL);
    uploadImg(rowid,imgfie);
}

function imgUpLoad(){
	$(".pictureFile").change(function() {
        var $file = $(this);
        var fileObj = $file[0];
        var windowURL = window.URL || window.webkitURL;
        var dataURL;
        var $img = $($file.parent().find("img")[0]);
        //当图片名称为空时，照片不显示。
        if(this.value.trim()==""){
            return;
        }
        if (fileObj && fileObj.files && fileObj.files[0]) {
        	previewImg(this,fileObj.files[0]);
        }
    });
}

function imgUpload1(){
	//定义能够拖拽的区域
    var box = $(".ul_list").find(".li_list_replace");
	for(var i=0;i<box.length;i++){
		var boxxc = box[i];
		//var thisobj = $(boxxc).find(".pictureFile")[0];
		boxxc.addEventListener("drop",function(e){
	        e.preventDefault(); //取消默认浏览器拖拽效果
	        var fileList = e.dataTransfer.files; //获取文件对象
	        //检测是否是拖拽文件到页面的操作 
	        if(fileList.length == 0){ 
	            return false; 
	        } 
	        //检测文件是不是图片 
	        if(fileList[0].type.indexOf('image') === -1){ 
	            alert("您拖的不是图片！"); 
	            return false; 
	        }
	        //$(this).find(".pictureFile")[0].value=fileList[0];
	        previewImg($(this).find(".pictureFile")[0],fileList[0]);
	    });
	}
    
}

function loadCategoryName(catid){
	$(".catoption_minlv").html("");
	if(catid=='311'){$(".catoption_lv1").val("童装");}
	if(catid=='312'){$(".catoption_lv1").val("内衣");}
	if(catid=='127380009'){$(".catoption_lv1").val("运动服饰");}
	if(catid=='10166'){$(".catoption_lv1").val("女装");}
	if(catid=='10165'){$(".catoption_lv1").val("男装");}
	$.ajax({
        type:"post", 
        url:"/cbtconsole/order/loadCategoryName.do",
        data:{"catid":catid},
        dataType:"text",
        async:true,
        success : function(data){
        	data = JSON.parse(data);
            for(var i=0;i<data.length;i++){
            	var catName = data[i].name;
            	var catid = data[i].catid;
            	var optionstr = "<option class='"+catid+"'>"+catName+"</option>";
            	$(".catoption_minlv").append(optionstr);
            	if(i==0){
            		getSizeChart(catid);
            	}
            }
        }
    });
}

//加载 尺码表
function getSizeChart(catid){
	$.ajax({
        type:"post", 
        url:"/cbtconsole/order/getSizeChart.do",
        data:{'catid':catid},
        dataType:"text",
        async:true,
        success : function(data){
        	var dataJson = JSON.parse(data);
        	var shopHtml = "";
            if(dataJson.length>0){
            	var shopHtml = '<ul class="ul_list">';
            	for(var i=0;i<dataJson.length;i++){
            		var catimg = dataJson[i];
            		shopHtml = shopHtml + '<li class="li_list">';
                    shopHtml = shopHtml + '<div class="imgs"><img src="'+catimg.remotpath+'" onclick="bigPic(this)"></div>';
                    shopHtml = shopHtml + '<span calss="goods_name">产品id：<em>'+catimg.pid+'</em></span>';
                    shopHtml = shopHtml + '</li>';
                    shopHtml = shopHtml + '<li class="li_list li_list_replace"><input class="rowid" value="'+catimg.id+'"/><input class="pictureFile" type="file"/>';
                    shopHtml = shopHtml + '<div class="imgs"><img src=""></div>';
                    shopHtml = shopHtml + '</li>';
            	}
            	shopHtml = shopHtml + '</ul>';
            }
            $(".shopid_box").append(shopHtml);
            imgUpLoad();
            imgUpload1();
        }
    });
}
// 获取大图
function bigPic(obj){
	$('.transparent').show();
	var src = $(obj).attr('src');
	$('.transparent img').attr('src',src);	
}
// 关闭大图
$('.transparent').click(function(){
	$('.transparent').hide();
})

</script>
</html>