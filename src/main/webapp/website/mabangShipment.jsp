<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>马帮运单/SKU导入</title>
<style type="text/css">
td{border:solid red 1px;}
table{border-collapse:collapse;border:none;}
</style>
 <!-- Fine Uploader New/Modern CSS file
    ====================================================================== -->
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2-website.js"></script>
    <link href="/cbtconsole/js/fine-uploader/fine-uploader-new.css" rel="stylesheet">
    <!-- Fine Uploader JS file
    ====================================================================== -->
    <script type="text/javascript" src="/cbtconsole/js/fine-uploader/jquery.fine-uploader.js"></script>

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
<body>	
    <!-- Fine Uploader DOM Element
    ====================================================================== -->
    <div><h2>本页面为马帮运单/SKU导入页面</h2></div>
    <div id="fine-uploader-manual-trigger"></div>
    <div>提示:
    	<p>1.上传文件格式为xls,xlsx</p>
    	<p>2.excel文件为马帮导出的运单/SKU文件</p>
    	<p><h2 style="color: red">3.sku文件请更改文件名为sku.xls或sku.xlsx</h2></p>
    	<p  style="color: red">4.请勿修改文件列名!列名顺序无要求!</p>
    	<p>5.运单/SKU信息字段如下图:</p>
    	<p><img src="/cbtconsole/source/mabangShip.png" alt="运单信息字段"/></p>
    	<p>sku信息字段<img src="/cbtconsole/source/sku.png" alt="SKU信息字段"/></p>
    </div>
    <!-- Your code to create an instance of Fine Uploader and bind to the DOM/template
    ====================================================================== -->
    <script>
					        var manualUploader = new qq.FineUploader({
					            element: document.getElementById('fine-uploader-manual-trigger'),
					            template: 'qq-template-manual-trigger',
					            request: {
					                endpoint: '/cbtconsole/warehouse/mabangShipmentUpload'
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
					                sizeLimit:50000000 //100*1000*1000 bytes
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
					                	/* if(responseJSON.success){
					                		alert('导入成功!');
					                	}else{
					                		alert('导入失败!');
					                	} */
					                	alert(responseJSON.message);
									},
									onAllComplete : function (succeeded, failed) {
										//window.confirm("上传成功!");
									}
					            },
					            autoUpload: false,
					            debug: true
					        });
						
					        qq(document.getElementById("trigger-upload")).attach("click", function() {
					            manualUploader.uploadStoredFiles();
					        });
					    </script>
</body>
</html>