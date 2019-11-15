<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>店铺品牌信息</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <script type="text/javascript"
            src="/cbtconsole/js/My97DatePicker/WdatePicker.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <style type="text/css">
        .but_color {
            background: #44a823;
            /*width: 70px;*/
            height: 24px;
            border: 1px #aaa solid;
            color: #fff;
            margin-left: 12px;
        }

        .del_color {
            margin-bottom: 3px;
            margin-top: 6px;
            background: red;
            /*width: 70px;*/
            height: 24px;
            border: 1px #aaa solid;
            color: #fff;
            margin-left: 12px;
        }
    </style>
    <script type="text/javascript">

        $(function () {
            closeDialog();
        });

        function saveBrandInfo(shopId) {
            var brandId = $("#in_brand_id").val();
            var brandName = $("#in_brand_name").val();
            var inAuthorizeState = $("#in_authorize_state").val();
            var termOfValidity = $("#term_of_validity").val();
            var certificateFile = $("#certificate_file").val();
            var remotePath = $("#remote_path").val();
            var localPath = $("#local_path").val();
            if (shopId && brandId && brandName && inAuthorizeState) {
                if (inAuthorizeState == 1 && !(termOfValidity || certificateFile)) {
                    $.messager.alert("提醒", "请输入有效期或者上传文件", "info");
                } else {
                    if (!brandId) {
                        brandId = 0;
                    }
                    $.ajax({
                        type: "POST",
                        url: "/cbtconsole/ShopUrlC/saveBrandInfo",
                        data: {
                            shopId: shopId,
                            brandId: brandId,
                            brandName: brandName,
                            inAuthorizeState: inAuthorizeState,
                            termOfValidity: termOfValidity,
                            certificateFile: certificateFile,
                            remotePath: remotePath,
                            localPath: localPath
                        },
                        success: function (data) {
                            if (data.ok) {
                                closeDialog();
                                setTimeout(function () {
                                    window.location.reload();
                                }, 500);
                            } else {
                                $.messager.alert("提醒", '执行错误:' + data.message, "error");
                            }
                        },
                        error: function (res) {
                            $.messager.alert("提醒", '保存错误，请联系管理员', "error");
                        }
                    });
                }
            } else {
                $.messager.alert("请输入完整数据");
                return;
            }
        }

        function uploadFile() {
            $.messager.progress({
                title: '上传本地图片到服务器',
                msg: '请等待...'
            });
            $("#file_form").form('submit', {
                type: "post",  //提交方式
                url: "/cbtconsole/ShopUrlC/uploadBrandFile", //请求url
                success: function (data) {
                    $.messager.progress('close');
                    var data = eval('(' + data + ')');
                    if (data.ok) {
                        $("#file_form").hide();
                        var rsData = data.data;
                        var fileNames = '';
                        var remotePaths = '';
                        var localPaths = '';
                        for(var i=0;i<rsData.length;i++){
                            if(i < rsData.length - 1){
                                fileNames += rsData[i][2] + ';';
                                remotePaths += rsData[i][1] + ';';
                                localPaths += rsData[i][2] + "@"+rsData[i][0] + ';';
                            }else{
                                fileNames += rsData[i][2];
                                remotePaths += rsData[i][1];
                                localPaths += rsData[i][2] + "@"+rsData[i][0];
                            }
                        }


                        $("#certificate_file").val(fileNames);
                        $("#certificate_file").show();
                        $("#remote_path").val(remotePaths);
                        $("#local_path").val(localPaths);
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function () {
                    $.messager.progress('close');
                    $.messager.alert("提醒", "上传错误，请联系管理员", "error");
                }
            });
        }

        function updateBrandInfo(brandId, brandName, authorizeState, termOfValidity, certificateFile, localPath, remotePath) {
            // resetForm();
            $("#brand_id_tr").show();
            $("#in_brand_id").val(brandId);
            $("#in_brand_name").val(brandName);
            $("#in_authorize_state").val(authorizeState);
            $("#term_of_validity").val(termOfValidity);
            if (authorizeState == 1 && certificateFile) {
                $("#certificate_file").val(certificateFile);
                $("#certificate_file").show();
                // $("#file_div").hide();
                $("#remote_path").val(remotePath);
                $("#local_path").val(localPath);
            }
            $('#enter_div_sty').dialog('open');
        }

        function enterBrandInfo() {
            resetForm();
            $('#enter_div_sty').dialog('open');
        }

        function closeDialog() {
            $('#enter_div_sty').dialog('close');
            resetForm();
        }

        function resetForm() {
            $("#in_brand_id").val(0);
            $("#in_brand_name").val("");
            $("#in_authorize_state").val(0);
            $("#term_of_validity").val("");
            $("#certificate_file").hide();
            $("#file_div").show();
            $("#brand_id_tr").hide();
            $("#remote_path").val("");
            $("#local_path").val("");
        }

        function deleteAuthorizedInfo(brandId, shopId) {
            $.ajax({
                type: "POST",
                url: "/cbtconsole/ShopUrlC/deleteAuthorizedInfo",
                data: {
                    shopId: shopId,
                    brandId: brandId
                },
                success: function (data) {
                    if (data.ok) {
                        closeDialog();
                        setTimeout(function () {
                            window.location.reload();
                        }, 500);
                    } else {
                        $.messager.alert("提醒", '执行错误:' + data.message, "error");
                    }
                },
                error: function (res) {
                    $.messager.alert("提醒", '保存错误，请联系管理员', "error");
                }
            });
        }

    </script>
</head>
<body>

<c:if test="${show == 0}">
    <h1 align="center">${msgStr}</h1>
</c:if>
<c:if test="${show > 0}">

    <div id="enter_div_sty" class="easyui-dialog" title="店铺品牌" data-options="modal:true"
         style="width: 744px; height: 366px;">
        <table>
            <tr>
                <td>店铺ID</td>
                <td>
                    <input id="in_shop_id" style="width: 168px; height: 28px;" value="${shopId}"
                           disabled="disabled"/>
                </td>
            </tr>
                <%--<tr id="shop_name_tr">
                    <td>店铺名称</td>
                    <td><span id="in_shop_name"></span></td>
                </tr>--%>
            <tr id="brand_id_tr" style="display: none;">
                <td>品牌ID</td>
                <td><input id="in_brand_id" value="0" style="width: 168px; height: 28px;" disabled="disabled"/></td>
            </tr>
            <tr>
                <td>品牌名称</td>
                <td><input id="in_brand_name" value="" style="width: 599px; height: 28px;"/></td>
            </tr>
            <tr>
                <td>授权状态</td>
                <td><select id="in_authorize_state" style="width: 168px; height: 28px;">
                    <option value="0">无授权</option>
                    <option value="1">已授权</option>
                    <option value="2">自有品牌</option>
                    <option value="3">无需授权</option>
                    <option value="-1">侵权</option>
                </select></td>
            </tr>
            <tr>
                <td>有效期</td>
                <td><input id="term_of_validity" class="Wdate"
                           style="width: 168px; height: 24px" type="text" value=""
                           onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/></td>
            </tr>
            <tr>
                <td>授权文件</td>
                <td>
                    <input id="certificate_file" style="display: none;width: 599px; height: 28px;" value=""
                           disabled="disabled"/>
                    <input type="hidden" id="remote_path" value=""/>
                    <input type="hidden" id="local_path" value=""/>
                    <div id="file_div">
                        <form id="file_form" method="post" enctype="multipart/form-data">
                            <input type="button" onclick="uploadFile()" value="上传"/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input id="file" type="file" name="file" multiple="true">
                            <span style="color: red;">*请在保存之前上传文件(选择文件,可多选->上传)</span>
                        </form>
                    </div>

                </td>
            </tr>
        </table>
        <br>
        <div style="text-align: center;">
            <br><br>
            <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
               class="easyui-linkbutton" onclick="saveBrandInfo('${shopId}')" style="width: 80px">保存</a>
            <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
               class="easyui-linkbutton" onclick="closeDialog()"
               style="width: 80px">取消</a>
        </div>
    </div>

    <p style="text-align: center;">
        <b style="font-size: 26px;">店铺【${shopId}】品牌数据</b>
        &nbsp;&nbsp;&nbsp;&nbsp;<button class="but_color" onclick="enterBrandInfo()">新增店铺品牌</button>
    </p>

    <table id="shop_brand_table" border="1" cellpadding="1"
           cellspacing="0" align="center">
        <thead>
        <tr align="center" style="height: 50px;background-color: #ccd3d4">
            <th style="width: 100px;">品牌ID</th>
            <th style="width: 220px;">品牌名称</th>
                <%--<th style="width: 130px;">店铺ID</th>--%>
            <th style="width: 100px;">授权状态</th>
            <th style="width: 100px;">有效期</th>
            <th style="width: 400px;">授权文件</th>
            <th style="width: 170px;">创建时间</th>
            <th style="width: 170px;">更新时间</th>
            <th style="width: 80px;">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${brandAuthorizationList}" var="brandInfo" varStatus="status">
            <tr style="height: 42px;">
                <td>
                        ${brandInfo.id}
                </td>
                <td>
                        ${brandInfo.brandName}
                </td>
                    <%--<td>
                            ${brandInfo.shopId}
                    </td>--%>
                <td style="text-align: center;">
                    <c:if test="${brandInfo.authorizeState == 0}">
                        无授权
                    </c:if>
                    <c:if test="${brandInfo.authorizeState == 1}">
                        已授权
                    </c:if>
                    <c:if test="${brandInfo.authorizeState == 2}">
                        自有品牌
                    </c:if>
                    <c:if test="${brandInfo.authorizeState == 3}">
                        无需授权
                    </c:if>
                    <c:if test="${brandInfo.authorizeState == -1}">
                        <b style="color: red">侵权</b>
                    </c:if>
                </td>
                <td>
                        ${brandInfo.termOfValidity}
                </td>
                <td>
                    <c:if test="${brandInfo.authorizeState == 1 && brandInfo.certificateFile !=null}">
                        <c:set value="${ fn:split(brandInfo.localPath, ';') }" var="fileList" />
                        <c:forEach items="${ fileList }" var="flName">
                            <c:set value="${ fn:split(flName, '@') }" var="spData" />
                            <a target="_blank" href="${spData[1]}">${spData[0]}</a><strong>&nbsp;||&nbsp;</strong>
                        </c:forEach>
                    </c:if>
                </td>
                <td>
                        ${brandInfo.createTime}
                </td>
                    <%--<td>
                            ${brandInfo.localPath}
                    </td>
                    <td>
                            ${brandInfo.remotePath}
                    </td>--%>
                <td>
                        ${brandInfo.updateTime}
                </td>
                <td>
                    <button class="but_color"
                            onclick="updateBrandInfo(${brandInfo.id},'${brandInfo.brandName}',${brandInfo.authorizeState},
                                    '${brandInfo.termOfValidity}','${brandInfo.certificateFile}','${brandInfo.localPath}','${brandInfo.remotePath}')">编辑品牌
                    </button>
                    <button class="del_color" onclick="deleteAuthorizedInfo(${brandInfo.id},'${shopId}')">
                        删除品牌
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


</c:if>

</body>
</html>
