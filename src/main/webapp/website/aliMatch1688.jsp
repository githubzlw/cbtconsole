<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>速卖通对标1688</title>
    <script type="text/javascript" src="/cbtconsole/js/jquery-1.10.2.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="/cbtconsole/jquery-easyui-1.5.2/locale/easyui-lang-zh_CN.js"></script>
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="/cbtconsole/jquery-easyui-1.5.2/themes/icon.css">
</head>
<style>
    .img_sty {
        max-height: 180px;
        max-width: 180px;
    }

    .s_btn {
        display: inline-block;
        height:40px;
        width:80px;
        background: #169bd4;
        border-radius: 10px;
        text-align: center;
        color: #fff;
        cursor: pointer;
        font-size: 14px;
    }

    .inp_sty {
        height: 26px;
    }

    .s_give {
        display: inline-block;
        height:40px;
        width:80px;
        background: red;
        border-radius: 10px;
        text-align: center;
        color: #fff;
        cursor: pointer;
        font-size: 14px;
    }

    .b_sty {
        color: red;
    }

    .b_check {
        color: #1909ea;
    }
</style>
<script>
    $(document).ready(function () {
        var adminId = "${adminId}";
        getAdminList(adminId);
        var dealState = "${dealState}";
        if(!(dealState == null || dealState =="")){
            $("#query_deal_state").val(dealState);
        }
    });

    function getAdminList(adminId) {
        $.ajax({
            type: "POST",
            url: "/cbtconsole/singleGoods/getAdminList",
            data: {},
            success: function (data) {
                if (data.ok) {
                    $("#query_admin_id").empty();
                    var content = '<option value="0" selected="selected">全部</option>';
                    var json = data.data;
                    for (var i = 0; i < json.length; i++) {
                        if (json[i].id == adminId) {
                            content += '<option selected="select" value="' + json[i].id + '" ">' + json[i].confirmusername + '</option>';
                        } else {
                            content += '<option value="' + json[i].id + '" ">' + json[i].confirmusername + '</option>';
                        }
                    }
                    $("#query_admin_id").append(content);
                } else {
                    alert("获取用户列表失败，原因 :" + data.message);
                }
            },
            error: function (res) {
                alert("网络获取失败");
            }
        });
    }

    function openUrl(aliUrl) {
        //var aliUrl = 'https://www.aliexpress.com/item/10pc-New-Indian-God-Oil-Wipes-Retardante-Sex-Ejaculation-Delay-Wipes-Sexual-Wet-Tissue-Wipes-for/32901387578.html';
        var url = 'http://192.168.1.27:10013/cbt/searchPidByAliPid.jsp?aliUrl=' + aliUrl;
        window.open(url);
    }

    function setAliFlag(aliPid, obj) {
        $.ajax({
            type: "POST",
            url: "/cbtconsole/aliProductCtr/setAliFlag",
            data: {
                aliPid: aliPid,
                dealState: 1
            },
            success: function (data) {
                if (data.ok) {
                    $(obj).hide();
                    $(".ali1_" + aliPid).hide();
                    $(".ali2_" + aliPid).hide();
                    $(".p1688_" + aliPid).hide();
                    $(obj).parent().find('input').hide();
                    $(obj).parent().append('<b class="b_sty">放弃</b>');
                } else {
                    alert("执行失败,原因:" + data.message);
                }
            },
            error: function (res) {
                alert("网络获取失败");
            }
        });
    }

    function set1688PidFlag(aliPid, pid, dealState, obj, valid, aliPrice, keyword) {
    	   var edName = $("#"+pid+"_name").val();
    	   
         $.ajax({
            type: "POST",
            url: "/cbtconsole/aliProductCtr/set1688PidFlag",
            data: {
                aliPid: aliPid,
                pid: pid,
                dealState: dealState,
                valid:valid,
                aliPrice:aliPrice,
                edName:edName,
                keyword:keyword
                
            },
            success: function (data) {
                if (data.ok) {
                    $(obj).parent().find('input').hide();
                    if (dealState == 1) {
                        $(obj).parent().append('<b class="b_sty">相似</b>');
                    } else if (dealState == 2) {
                        $(".ali2_" + aliPid).hide();
                        $(".main_" + aliPid).find('input').hide();
                        $(".p1688_" + aliPid).hide();
                        $(".main_" + aliPid).append('<b class="b_sty">已对标</b>');
                        $(obj).parent().append('<b class="b_sty">对标</b>');
                    } else if(dealState == 3){
                    	  $(obj).parent().append('<b class="b_sty">删除同款</b>');
                    }
                } else {
                    alert("执行失败,原因:" + data.message);
                }
            },
            error: function (res) {
                alert("网络获取失败");
            }
         }); 
    }
    
    function develop1688Pid(aliPid, pid,aliPrice,obj,keyword) {
        $.ajax({
            type: "POST",
            url: "/cbtconsole/aliProductCtr/develop1688Pid",
            data: {
                aliPid: aliPid,
                pid: pid,
                aliPrice: aliPrice,
                keyword:keyword
            },
            success: function (data) {
                if (data.ok) {
                    $(obj).hide();
                    $(".p1688_" + aliPid).hide();
                    $(".ali2_" + aliPid).hide();
                    $(obj).parent().append('<b class="b_sty">爆款对标</b>');
                } else {
                    alert("执行失败,原因:" + data.message);
                }
            },
            error: function (res) {
                alert("网络获取失败");
            }
         });
    }
    
    /* function up1688PidFlag(pid, obj) {
 	   
      $.ajax({
         type: "POST",
         url: "/cbtconsole/aliProductCtr/up1688PidFlag",
         data: {
             pid: pid
         },
         success: function (data) {
        	   $(obj).parent().find('input').hide();
             if (data.ok) {
             } else {
                 alert("执行失败,原因:" + data.message);
             }
         },
         error: function (res) {
             alert("网络获取失败");
         }
      });  
 }*/
    		
    		
</script>
<body style="overflow-y: hidden;">


<div style="margin-bottom: -3px;text-align: center;width: 100%;height: 10%;">
    <h3 style="text-align: center;">速卖通对标1688</h3>
    <form action="/cbtconsole/aliProductCtr/queryForList" method="post">
        <span>AliPid:<input type="text" name="aliPid" class="inp_sty" value="${aliPid}"/></span>
        <span>关键词:<input type="text" name="keyword" class="inp_sty" value="${keyword}"/></span>
        <span>
            对标人:<select id="query_admin_id" name="adminId" style="height: 28px;"></select>
            </span>
        <span>处理状态:<select id="query_deal_state" name="dealState" style="height: 28px;">
            <option value="-1">全部</option>
            <option value="0">未处理</option>
            <option value="1">放弃</option>
            <option value="2">已对标</option>
        </select></span>
        <input type="hidden" value="1" name="page"/>
        &nbsp;&nbsp;&nbsp;<span><input type="submit" class="s_btn" value="查询"></span>
    </form>
</div>

<div style="width: 100%;height: 90%;overflow-y: auto">
    <table id="shop_category_id" border="1" cellpadding="1"
           cellspacing="0" align="center">
        <thead>
        <tr align="center" style="height: 50px;">
            <th style="width: 11%;background-color: #5aec6c;">速卖通商品信息</th>
            <th style="width: 44%;background-color: #c0c38e;" colspan="4">lire对标1688商品信息</th>
            <th style="width: 44%;background-color: #91dee2;" colspan="4">爆款对标商品信息</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${infos}" var="aliGd" varStatus="status">
            <tr style="height: 42px;background-color: #5aec6c">
                <td style="width: 11%;">
                    <div>
                        <span>AliPid:${aliGd.aliPid}</span>
                        <br><a target="_blank" href="${aliGd.aliUrl}"><img class="img_sty" src="${aliGd.aliImg}"/></a>
                        <br><span style="color: red">价格USD:${aliGd.aliPrice}</span>
                        <br><span>产品名称:${aliGd.aliName}</span>
                        <br><span>关键词:${aliGd.keyword}</span>
                        <br><span>对标人:${aliGd.adminName}</span>
                        <c:if test="${aliGd.dealState == 0}">
                            <br>
                            <span class="main_${aliGd.aliPid}">
                                <input type="button" class="s_btn" value="爆款对标" onclick="openUrl('${aliGd.aliUrl}')"/>
                        &nbsp;&nbsp;&nbsp;
                                <input type="button" class="s_give" value="放弃"
                                                 onclick="setAliFlag('${aliGd.aliPid}',this)"/>
                        </span>
                        </c:if>
                        <c:if test="${aliGd.dealState == 1}">
                            <br><span><b class="b_sty">放弃,操作人:${aliGd.adminName}</b></span>
                            <br><span><b class="b_sty">操作时间:${aliGd.updateTime}</b></span>
                        </c:if>

                        <c:if test="${aliGd.dealState == 2}">
                            <br><span><b class="b_check">已对标,操作人:${aliGd.adminName}</b></span>
                            <br><span><b class="b_check">操作时间:${aliGd.updateTime}</b></span>
                        </c:if>
                        <c:if test="${aliGd.dealState == 3}">
                            <br><span><b class="b_check">爆款对标,操作人:${aliGd.adminName}</b></span>
                            <br><span><b class="b_check">操作时间:${aliGd.updateTime}</b></span>
                        </c:if>
                    </div>
                </td>
                <c:if test="${fn:length(aliGd.productListLire) > 0}">
                    <c:forEach items="${aliGd.productListLire}" var="lireGd">
                        <td style="width: 11%;background-color: #c0c38e;">
                            <div>
                                <span>Pid:<a target="_blank" href="/cbtconsole/editc/detalisEdit?pid=${lireGd.pid}">${lireGd.pid}</a></span>
                                <br><a target="_blank" href="${lireGd.url}">
                                <img class="img_sty" src="${lireGd.remotePath}${lireGd.img}"/></a>
                                <br><span style="color: red">价格USD:${lireGd.showPrice}</span>
                                <br><span>产品名:${lireGd.name}</span>
                                <br><span>MOQ:${lireGd.moq}&nbsp;&nbsp;销量:${lireGd.sold}&nbsp;&nbsp;
                                <c:if test="${lireGd.isSoldFlag > 0}">
                                    <b style="color: green">免邮</b>
                                </c:if>
                                <c:if test="${lireGd.isSoldFlag == 0}">
                                    <b style="color: green">非免邮</b>
                                </c:if>
                                </span>
                                <c:if test="${lireGd.bmFlag == 1 && lireGd.isBenchmark == 1}">
                                    <br><span style="background-color: #e4d6d6;color: red;font-weight: 500;">原对标AliPid:${lireGd.oldAliPid}</span>
                                </c:if>
                                <br><span>状态:</span>
                                <c:if test="${lireGd.valid > 0}">
                                    <b style="color: green">在线</b>
                                </c:if>
                                <c:if test="${lireGd.valid == 0}">
                                    <b style="color: red">下架</b>
                                    <br><b style="color: red">原因：${lireGd.reasonName}</b>
                                </c:if>
                                <br><span>是否卖过:</span>
                                <c:if test="${lireGd.soldFlag == 1}">
                                    <b style="color: green">是</b>
                                </c:if>
                                <c:if test="${lireGd.soldFlag == 0}">
                                    <b style="color: green">否</b>
                                </c:if>
                                <br><span>是否人为编辑过:</span>
                                <c:if test="${lireGd.isEdit == 1}">
                                    <b style="color: green">是</b>
                                </c:if>
                                <c:if test="${lireGd.isEdit == 0}">
                                    <b style="color: green">否</b>
                                </c:if>
                                <br><span>人为编辑产品名:</span>
                                <input type="text" id="${lireGd.pid}_name"  class="inp_sty" value=""/>
                                <br><span>供应商:<b style="color: red">${lireGd.shopName}</b></span>
                                <br><span>供应商评分:<b style="color: red">${lireGd.qualityShop}</b></span>
                                <br><span>产品评分:<b style="color: red">${lireGd.qualityProduct}</b></span>


                                <c:if test="${lireGd.dealState > 0}">
                                    <c:if test="${lireGd.dealState == 1}">
                                        <br>
                                        <span>
                                          <b class="b_sty">相似</b>
					                                <c:if test="${aliGd.dealState == 0}">
					                                    &nbsp;&nbsp;<input type="button" class="s_btn ali2_${aliGd.aliPid}" value="对标"
					                                    onclick="set1688PidFlag('${aliGd.aliPid}','${lireGd.pid}',2,this,'${lireGd.valid}','${aliGd.aliPrice}','${aliGd.keyword}')"/>
					                                </c:if>
                                				</span>
                                    </c:if>
                                    <c:if test="${lireGd.dealState == 2}">
                                        <br><span><b class="b_sty">对标</b></span>
                                    </c:if>
                                    <c:if test="${lireGd.dealState == 3}">
                                        <br><span><b class="b_sty">删除同款</b></span>
                                    </c:if>
                                </c:if>
                                
                                <c:if test="${lireGd.dealState == 0}">
                                    <c:if test="${aliGd.dealState == 0}">
                                        <br><span><input type="button" value="相似" class="s_btn ali1_${aliGd.aliPid}"
                                                     onclick="set1688PidFlag('${aliGd.aliPid}','${lireGd.pid}',1,this,'${lireGd.valid}','${aliGd.aliPrice}','${aliGd.keyword}')"/>
                                        &nbsp;&nbsp;<input type="button" value="对标" class="s_btn ali2_${aliGd.aliPid}"
                                        onclick="set1688PidFlag('${aliGd.aliPid}','${lireGd.pid}',2,this,'${lireGd.valid}','${aliGd.aliPrice}','${aliGd.keyword}')"/>
                                    </c:if>
                                </span>
                                </c:if>

                                <c:if test="${lireGd.dealState < 2}">
                                    <br><span><input type="button" value="删除同款" class="s_btn ali3_${aliGd.aliPid}"
                                    onclick="set1688PidFlag('${aliGd.aliPid}','${lireGd.pid}',3,this,'${lireGd.valid}','${aliGd.aliPrice}','${aliGd.keyword}')"/></span>
                                </c:if>

                            </div>
                        </td>
                    </c:forEach>
                </c:if>
                <c:if test="${fn:length(aliGd.productListLire) == 0}">
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </c:if>
                <c:if test="${fn:length(aliGd.productListPython) > 0}">
                    <c:forEach items="${aliGd.productListPython}" var="pyGd">
                        <td style="width: 11%;background-color: #91dee2;">
                            <div>
                                <span>Pid:${pyGd.pid}</span>

                                <br><a target="_blank" href="${pyGd.url}">
                                <img class="img_sty" src="${pyGd.img}"/></a>
                                <br><span style="color: red">价格USD:${pyGd.showPrice}</span>
                                <br><span>产品名:${pyGd.name}</span>
                                <c:if test="${pyGd.sold > 0}">
                                    <br><span>销量:${pyGd.sold}</span>
                                </c:if>
                                <br><span>供应商:<b style="color: red">${pyGd.shopName}</b></span>
                                <br><span>供应商评分:<b style="color: red">${pyGd.qualityShop}</b></span>
                                <br><span>产品评分:<b style="color: red">${pyGd.qualityProduct}</b></span>
                                <br>
                                <c:if test="${pyGd.dealState > 0}">
                                    <span><b class="b_sty">爆款对标</b></span>
                                </c:if>
                                <c:if test="${aliGd.dealState == 0}">
                                    <span><input type="button" class="s_btn p1688_${aliGd.aliPid}" value="爆款对标"
                                                 onclick="develop1688Pid('${aliGd.aliPid}','${pyGd.pid}','${aliGd.aliPrice}',this,'${aliGd.keyword}')"/></span>
                                </c:if>
                            </div>
                        </td>
                    </c:forEach>
                </c:if>

            </tr>
        </c:forEach>

        </tbody>
    </table>


    <form id="submit_form" action="/cbtconsole/aliProductCtr/queryForList" method="post">
        <input type="hidden" id="page_ali_pid" name="aliPid" value="${aliPid}"/>
        <input type="hidden" id="page_keyword" name="keyword" value="${keyword}"/>
        <input type="hidden" id="page_adminId" name="adminId" value="${adminId}"/>
        <input type="hidden" id="query_current_page" name="page" value="${page}"/>
        <input type="hidden" id="page_dealState" name="dealState" value="${dealState}"/>
    </form>
    <div style="text-align: center;">
        <span>当前页：<span id="query_page">${page}</span>/<span id="query_total_page">${totalPage}</span></span>
        <span>&nbsp;&nbsp;总数：<span id="query_total">${total}</span></span>
        <span>&nbsp;&nbsp;<input type="button" class="s_btn" value="上一页"
                                 onclick="beforeQuery(${page},${page-1},${totalPage})"/></span>
        <span>&nbsp;&nbsp;<input type="button" class="s_btn" value="下一页"
                                 onclick="beforeQuery(${page},${page+1},${totalPage})"/></span>
        <span>&nbsp;&nbsp;&nbsp;&nbsp;跳转页：<input id="jump_page" type="number" value="1"/>
        <input type="button" value="翻页" class="s_btn" onclick="jumpPage(${page},${totalPage})"/></span>
    </div>
</div>
</body>
<script>
    function beforeQuery(currentPage, nextPage, totalPage) {
        if (nextPage > 0 && nextPage <= totalPage) {
            $("#query_current_page").val(nextPage);
            doQuery();
        } else {
            alert("无法翻页！");
            return false;
        }
    }

    function jumpPage(currentPage, totalPage) {

        var nextPage = $("#jump_page").val();
        if (nextPage == null || nextPage == "" || nextPage < 1) {
            alert("请输入跳转页");
            return false;
        } else {
            if (nextPage > 0 && nextPage <= totalPage) {
                $("#query_current_page").val(nextPage);
                doQuery();
            } else {
                alert("无法翻页！");
                return false;
            }
        }
    }

    function doQuery() {
        $("#submit_form").submit();
    }
</script>
</html>
