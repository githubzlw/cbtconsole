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
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
    <script type="text/javascript">

        function saveBrandInfo() {
            var shopId = $("#in_shop_id").val();
            var brandId = $("#in_brand_id").val();
            var brandName = $("#in_brand_name").val();
            var inAuthorizeState = $("#in_authorize_state").val();
            var termOfValidity = $("#term_of_validity").val();
            var certificateFile = $("#certificate_file").val();
            if (shopId && brandId && brandName && inAuthorizeState) {
                if (inAuthorizeState == 1 && !(termOfValidity || certificateFile)) {
                    $.messager.alert("提醒", "请输入有效期或者上传文件", "info");
                } else {
                    if(!brandId){
                        brandId= 0;
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
                            certificateFile:certificateFile
                        },
                        success: function (data) {
                            $.messager.progress('close');
                            if (data.ok) {
                                closeDialog();
                                setTimeout(function () {
                                    window.location.reload();
                                }, 1000);
                            } else {
                                $.messager.alert("提醒", '执行错误:' + data.message, "info");
                            }
                        },
                        error: function (res) {
                            $.messager.alert("提醒", '保存错误，请联系管理员', "error");
                        }
                    });
                }
            } else {
                $.message.alert("请输入完整数据");
                return;
            }
        }

        function uploadFile() {
            $("#file_form").form('submit', {
                type: "post",  //提交方式
                url: "/cbtconsole/ShopUrlC/uploadBrandFile", //请求url
                success: function (data) {
                    var data = eval('(' + data + ')');
                    if (data.ok) {
                        closeDialog();
                        $("#certificate_file").val(data.data);
                    } else {
                        $.messager.alert("提醒", data.message, "error");
                    }
                },
                error: function () {
                    $.messager.alert("提醒", "上传错误，请联系管理员", "error");
                }
            });
        }

        function enterBrandInfo() {
            $("#form_enter")[0].reset();
            $('#enter_div_sty').dialog('open');
        }

        function closeDialog() {
            $('#enter_div_sty').dialog('close');
            $("#form_enter")[0].reset();
        }


    </script>
</head>
<body>

<c:if test="${show == 0}">
    <h1 align="center">${msgStr}</h1>
</c:if>
<c:if test="${show > 0}">

    <div id="enter_div_sty" class="easyui-dialog" title="店铺品牌" data-options="modal:true" style="width: 888px; height: 300px;">
        <form id="form_enter" action="#" onsubmit="return false">
            <table>
                <tr>
                    <td>店铺ID</td>
                    <td><input id="in_shop_id" value="" style="width: 555px; height: 28px;" placeholder="请输入店铺ID"/></td>
                </tr>
                    <%--<tr id="shop_name_tr">
                        <td>店铺名称</td>
                        <td><span id="in_shop_name"></span></td>
                    </tr>--%>
                <tr id="brand_id_tr">
                    <td>品牌ID</td>
                    <td><input id="in_brand_id" value="0" style="width: 555px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td>品牌名称</td>
                    <td><input id="in_brand_name" value="" style="width: 555px; height: 28px;"/></td>
                </tr>
                <tr>
                    <td>授权状态</td>
                    <td><select id="in_authorize_state">
                        <option value="0">无授权</option>
                        <option value="1">已授权</option>
                        <option value="2">自有品牌</option>
                        <option value="3">无需授权</option>
                    </select></td>
                </tr>
                <tr>
                    <td>有效期</td>
                    <td><input id="term_of_validity" class="Wdate"
                               style="width: 100px; height: 24px" type="text" value=""
                               onfocus="WdatePicker({skin:'whyGreen',minDate:'2015-10-12',maxDate:'2050-12-20'})"/></td>
                </tr>
                <tr>
                    <td>授权文件<input id="certificate_file" type="hidden" value=""></td>
                    <td>
                        <form id="file_form" onsubmit="return false;">
                            <input id="file" type="file" name="file" multiple="false">
                            <input type="button" value="上传"/>
                        </form>
                    </td>
                </tr>
            </table>
            <div style="text-align: center;">
                <a href="javascript:void(0)" data-options="iconCls:'icon-add'"
                   class="easyui-linkbutton" onclick="saveBrandInfo()" style="width: 80px">保存</a>
                <a href="javascript:void(0)" data-options="iconCls:'icon-cancel'"
                   class="easyui-linkbutton" onclick="closeDialog()"
                   style="width: 80px">取消</a>
            </div>

        </form>
    </div>

    <h1 style="text-align: center;">店铺【${shopId}】品牌数据</h1>
    <table id="shop_brand_table" border="1" cellpadding="1"
           cellspacing="0" align="center">
        <thead>
        <tr align="center" bgcolor="#DAF3F5" style="height: 50px;">
            <th style="width: 120px;">品牌ID</th>
            <th style="width: 200px;">品牌名称</th>
            <th style="width: 90px;">店铺ID</th>
            <th style="width: 150px;">授权状态</th>
            <th style="width: 440px;">有效期</th>
            <th style="width: 150px;">创建时间</th>
            <th style="width: 180px;">更新时间</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${brandAuthorizationList}" var="brandInfo" varStatus="status">
            <tr bgcolor="#FFF7FB" style="height: 42px;">
                <td>
                        ${brandInfo.id}
                </td>
                <td>
                        ${brandInfo.brandName}
                </td>
                <td>
                        ${brandInfo.authorizeState}
                </td>
                <td>
                        ${brandInfo.termOfValidity}
                </td>
                <td>
                        ${brandInfo.certificateFile}
                </td>
                <td>
                        ${brandInfo.createTime}
                </td>
                <td>
                        ${brandInfo.localPath}
                </td>
                <td>
                        ${brandInfo.remotePath}
                </td>
                <td>
                        ${brandInfo.updateTime}
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>


</c:if>

</body>
</html>
