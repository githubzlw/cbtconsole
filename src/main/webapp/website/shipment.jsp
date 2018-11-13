<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入运费模板</title>
<style type="text/css">
td{border:solid red 1px;}
table{border-collapse:collapse;border:none;}
</style>
 <!-- Fine Uploader New/Modern CSS file
    ====================================================================== -->
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
    <link href="/cbtconsole/js/fine-uploader/fine-uploader-new.css" rel="stylesheet">
    <link rel="stylesheet" href="/cbtconsole/js/bootstrap/bootstrap.min.css">
    <!-- Fine Uploader JS file
    ====================================================================== -->
    <script type="text/javascript" src="/cbtconsole/js/fine-uploader/jquery.fine-uploader.js"></script>
	<script type="text/javascript" src="/cbtconsole/js/bootstrap/bootstrap.min.js"></script>
    <!-- Fine Uploader Thumbnails template w/ customization
    ====================================================================== -->
    <script type="text/template" id="qq-template-manual-trigger">
        <div class="qq-uploader-selector qq-uploader" qq-drop-area-text="拖拽文件到这里">
            <div class="qq-total-progress-bar-container-selector qq-total-progress-bar-container">
                <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-total-progress-bar-selector qq-progress-bar qq-total-progress-bar"></div>
            </div>
            <div class="qq-upload-drop-area-selector qq-upload-drop-area" qq-hide-dropzone>
                <span class="qq-upload-drop-area-text-selector"></span>
            </div>
            <div class="buttons">
                <div class="qq-upload-button-selector qq-upload-button">
                    <div>选择文件</div>
                </div>
                <button type="button" id="trigger-upload" class="btn btn-primary">
                    <i class="icon-upload icon-white"></i> 上传
                </button>
            </div>
            <span class="qq-drop-processing-selector qq-drop-processing">
                <span>Processing dropped files...</span>
                <span class="qq-drop-processing-spinner-selector qq-drop-processing-spinner"></span>
            </span>
            <ul class="qq-upload-list-selector qq-upload-list" aria-live="polite" aria-relevant="additions removals">
                <li>
                    <div class="qq-progress-bar-container-selector">
                        <div role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" class="qq-progress-bar-selector qq-progress-bar"></div>
                    </div>
                    <span class="qq-upload-spinner-selector qq-upload-spinner"></span>
                    <img class="qq-thumbnail-selector" qq-max-size="100" qq-server-scale>
                    <span class="qq-upload-file-selector qq-upload-file"></span>
                    <span class="qq-edit-filename-icon-selector qq-edit-filename-icon" aria-label="Edit filename"></span>
                    <input class="qq-edit-filename-selector qq-edit-filename" tabindex="0" type="text">
                    <span class="qq-upload-size-selector qq-upload-size"></span>
                    <button type="button" class="qq-btn qq-upload-cancel-selector qq-upload-cancel">取消</button>
                    <button type="button" class="qq-btn qq-upload-retry-selector qq-upload-retry">重试</button>
                    <button type="button" class="qq-btn qq-upload-delete-selector qq-upload-delete">删除</button>
                    <span role="status" class="qq-upload-status-text-selector qq-upload-status-text"></span>
                </li>
            </ul>

            <dialog class="qq-alert-dialog-selector">
                <div class="qq-dialog-message-selector"></div>
                <div class="qq-dialog-buttons">
                    <button type="button" class="qq-cancel-button-selector">关闭</button>
                </div>
            </dialog>

            <dialog class="qq-confirm-dialog-selector">
                <div class="qq-dialog-message-selector"></div>
                <div class="qq-dialog-buttons">
                    <button type="button" class="qq-cancel-button-selector">No</button>
                    <button type="button" class="qq-ok-button-selector">Yes</button>
                </div>
            </dialog>

            <dialog class="qq-prompt-dialog-selector">
                <div class="qq-dialog-message-selector"></div>
                <input type="text">
                <div class="qq-dialog-buttons">
                    <button type="button" class="qq-cancel-button-selector">取消</button>
                    <button type="button" class="qq-ok-button-selector">确定</button>
                </div>
            </dialog>
        </div>
    </script>

    <style>
        #trigger-upload {
            color: white;
            background-color: #00ABC7;
            font-size: 14px;
            padding: 7px 20px;
            background-image: none;
        }

        #fine-uploader-manual-trigger .qq-upload-button {
            margin-right: 15px;
        }

        #fine-uploader-manual-trigger .buttons {
            width: 36%;
        }

        #fine-uploader-manual-trigger .qq-uploader .qq-total-progress-bar-container {
            width: 60%;
        }
    </style>

    <title>Fine Uploader Manual Upload Trigger Demo</title>
</head>
<script type="text/javascript">
function delShipment(str){
	$.ajax({
		type:'post',
		url:'/cbtconsole/shipment/delShipment',
		data:{'idStr':str},
		dataType:'json',
		success:function(res){
			alert(res.message);
		},error:function(XMLHttpRequest, textStatus, errorThrown){
			alert("系统错误,请联系开发人员确认问题!");
		}
	});
}
</script>
<body>	
    <!-- Fine Uploader DOM Element
    ====================================================================== -->
    <div><h2>本页面为运单导入页面</h2></div>
	<div class="form-group">
		<div class="input-group">
			<div class="input-group-addon">运输公司</div>
			<select id="company" class="form-control" style="width: 100px">
				<option value="0">请选择</option>
				<option value="JCEX">佳成JCEX</option>
				<option value="emsinten">邮政</option>
				<option value="原飞航">原飞航</option>
				<option value="SF">顺丰-睦鹏</option>
				<option value="深圳诚泰">航邮-深圳诚泰</option>
				<option value="TL">泰蓝-舜衡安</option>
				<option value="灿鑫">灿鑫</option>
				<option value="迅邮">迅邮</option>
				<option value="大誉">大誉</option>
                <option value="衡欣">衡欣</option>
			</select>
		</div>
	</div>
    <div id="fine-uploader-manual-trigger"></div>
    <div>提示:
    	<p>1.上传文件格式为xls,xlsx</p>
    	<!-- <p style="color: red;font-size: 20px">2.请仔细检查上传表格的列数(不能有合并列,一列一格),请勿移动表格列顺序</p> -->
    	<p>2.<a href='/cbtconsole/source/shipExamples.xlsx' target="_blank">下载原飞航模板</a>&nbsp;<a href='/cbtconsole/source/YH.xls' target="_blank">下载航邮模板</a></p>
    	<p style="color: red;font-size: 20px">3.校验不通过的运单信息请仔细检查.删除后再重新上传</p>
    	<p>4.以下为各公司模板字段:</p>
    	<p>顺丰表头字段:<span style="color: red;font-size: 20px">下单日期格式:yyyy/MM/dd</span><table><tr><td>收件人</td><td>收件国家</td><td>参考单号</td><td>运单号</td><td>物流商</td><td>下单日期</td><td>件数</td><td>包装</td><td>计价重量</td><td>客户运费</td><td>特殊备注</td></tr></table></p>
    	<p>佳成表头字段:<span style="color: red;font-size: 20px">发件日期格式:yyyy-MM-dd</span><table><tr><td>DATE<br/>发件日期</td><td>CONS<br/>运单号</td><td>DEST<br/>收件地</td><td>PACKAGING</td><td>WT<br/>重量(kg)</td><td>PCS<br/>件数</td><td>NET CHARGE</td><td>PUEL SURCHARGE</td><td>安检费</td><td>OTHER<br/>其他</td><td>税金</td><td>Total<br/>小计</td></tr></table></p>
    	<p>邮政表头字段:<table><tr><td>运单号</td><td>转单号</td><td>快递类别</td><td>发件日期</td><td>目的地</td><td>收件人</td><td>件数</td><td>重量</td><td>费用</td><td>类型</td><td>备注</td></tr></table></p>
    	<p>泰蓝国际表头字段:<span style="color: red;font-size: 20px">日期格式:yyyy.MM.dd</span><table><tr><td>日期</td><td>运单号</td><td>渠道</td><td>目的地</td><td>件数</td><td>重量（KG）</td><td>单价</td><td>金额</td><td>备注</td></tr></table></p>
    </div>
	<div id="res" style="display: none;">
		<span id='info' style='color: red'>以下为校验未通过的运单信息:</span>
	</div>
    <!-- Your code to create an instance of Fine Uploader and bind to the DOM/template
    ====================================================================== -->
    <script>
    	var uploadSuccessDataNum = 0;
        var manualUploader = new qq.FineUploader({
            element: document.getElementById('fine-uploader-manual-trigger'),
            template: 'qq-template-manual-trigger',
            request: {
                endpoint: '/cbtconsole/shipment/shipMentUpload'
            },
            editFilename: {//编辑名字
                enable: true
            },
            thumbnails: {
                placeholders: {
                    waitingPath: '../js/fine-uploader/placeholders/waiting-generic.png',
                    notAvailablePath: '../js/fine-uploader/placeholders/not_available-generic.png'
                }
            },
            validation: {
                allowedExtensions: ['xls','xlsx'],
                sizeLimit:500000 //100*1000*1000 bytes
            },
            callbacks: {
            	/* 开始上传 */
                onUpload: function (id, name) {
                },
                /* 选择文件后 */
                onSubmitted: function (id, name) {
                	
                },
                /* 上传完成 */
                onComplete : function(id, name, responseJSON) {
                	//document.getElementById('ids').value=responseJSON.id;
                	
					if (responseJSON.success) {
						var json = responseJSON.list;
	                	if (json.length > 0) {
	                		var ids = "";
		                	var tabStr = "<table><tr><td>序号</td><td>运单号码</td><td>收件人国家</td><td>快件网络</td><td>件数</td><td>物品类型</td><td>实际重量</td><td>体积重量</td><td>结算重量</td><td>运费</td><td>燃油附加费</td><td>安检费</td><td>税金</td><td>总金额</td><td>备注</td><td>是否已经冻结</td></tr>";
		                	for (var i = 0; i < json.length; i++) {
		                		tabStr+="<tr><td>"+(i+1)+"</td><td>"+json[i].orderno+"</td><td>"+json[i].country+"</td><td>"+json[i].transportcompany+"</td><td>"+json[i].numbers+"</td><td>"+json[i].type+"</td><td>"+json[i].realweight+"</td><td>"+json[i].bulkweight+"</td><td>"+json[i].settleweight+"</td><td>"+json[i].charge+"</td><td>"+json[i].fuelsurcharge+"</td><td>"+json[i].securitycosts+"</td><td>"+json[i].taxs+"</td><td>"+json[i].totalprice+"</td><td>"+json[i].remark+"</td>";
		                		if(json[i].passFlag==1){
		                			tabStr+="<td>已冻结,不能再上传</td>";
		                		}else{
		                			tabStr+="<td>已冻结,不能再上传</td>";
		                		}
		                		tabStr+="</tr>"
								ids+=json[i].id+",";
							}
		                	tabStr+="</table>";
		                	ids=ids.substring(0,ids.length-1);
		                	var exportBtnStr = "<input type='button' value='导出' onclick=\"javascript:window.open('/cbtconsole/shipment/exportExcel?ids="+ids+"')\"/>";
		                	var delBtnStr = "&nbsp;<input type='button' value='删除' onclick='delShipment(&apos;"+ids+"&apos;)'/>";
		                	$('#info').append(exportBtnStr);
		                	$('#info').append(delBtnStr);
		                	$('#res').append(tabStr);
		                	$('#res').css('display', 'block');
						}else{
							alert("运单信息校验通过!");
						}
						/* if(responseJSON.successNum != undefined){
	                		uploadSuccessDataNum += responseJSON.successNum;
	                	} */
					}else{
						alert(responseJSON.message);
					}
				},
				onAllComplete : function (succeeded, failed) {
					/* if (failed.length > 0) {
						window.confirm("上传成功"+succeeded.length+"个文件!\r\n失败"+failed.length+"个文件!共导入"+uploadSuccessDataNum+"条数据!");
					}else if(succeeded.length > 0){
						window.confirm("上传成功"+succeeded.length+"个文件!\r\n共导入"+uploadSuccessDataNum+"条数据!");
					} */
				}
            },
            autoUpload: false,
            debug: true
        });
	
        qq(document.getElementById("trigger-upload")).attach("click", function() {
        	var company = $('#company').val();
        	if (company == '0') {
        		alert('请选择运输公司!');
				return;
			}
        	var postParams = { 
                    "company":company 
                }; 
        	manualUploader.setParams(postParams);
            manualUploader.uploadStoredFiles();
        });
    </script>
</body>
</html>