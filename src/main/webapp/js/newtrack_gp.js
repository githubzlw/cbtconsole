

var curr_order_num = 0;// 获取数据库的订单数量
var orderJson = {};
var jsonList = [];
var curr_order_no = "";// 获取数据库的订单数量
/**
 * 取消验货
 * @param isok
 * @param orderid
 * @param goodid
 * @param index
 * @param repState
 */
function updatecancelChecktatus(isok, orderid, odid, isDropshipOrder, goodid, index, repState) {
    var warehouseRemark = $('textarea[name ="warehouseRemark' + index + '"]').val();
    var barcode = document.getElementById("code_" + odid + "").innerHTML;
    seiUnit = document.getElementById("unit_" + odid + "").innerHTML;
    seiUnit = seiUnit.replace(/[^0-9]/ig, "");//产品计量单位
    if (seiUnit == null || seiUnit == "null" || seiUnit == "") {
        seiUnit = "1";
    }
    var count = document.getElementById("" + orderid + "count_" + odid + "").value;//取消验货数量
    var _count = document.getElementById("" + orderid + "_count" + odid + "").value;//销售数量
    var record_ = document.getElementById("" + orderid + "record_" + odid + "").innerHTML;//已验货数量
    if (count == null || count == "" || Number(count) <= 0) {
        alert("请输入取消验货数量");
        return;
    }
    var weight = $("#" + orderid + "weight" + odid + "").val();
    var cance_inventory_count = Number(count) + Number(record_)
        - (Number(_count) * Number(seiUnit));//库存数量
    if (isDropshipOrder == 3) {
        cance_inventory_count = count;
    }
    if (confirm("您确定要取消验货吗?")) {
        $
            .post(
                "/cbtconsole/order/updatecancelChecktatus",
                // "/cbtconsole/WebsiteServlet?action=updatecancelChecktatus&className=ExpressTrackServlet",
                {
                    orderid: orderid,
                    goodid: goodid,
                    repState: repState,
                    warehouseRemark: warehouseRemark,
                    barcode: barcode,
                    cance_inventory_count: cance_inventory_count,
                    count: count,
                    seiUnit: seiUnit,
                    odid: odid,
                    weight: weight
                }, function (res) {
                    if (res >= 1) {
                        var shipno = $('#shipno').val();
                        $("#search").val(shipno);
                        search();
                    } else {
                        alert("取消验货失败!");
                    }
                });
    }
}

/**
 * 取消入库
 * @param isok
 * @param orderid
 * @param goodid
 * @param index
 * @param repState
 */
function updatecanceltatus(isok, orderid, odid, goodid, index, repState) {
    var warehouseRemark = $('textarea[name ="warehouseRemark' + index + '"]')
        .val();
    var count = document.getElementById("" + orderid + "_count" + odid + "").innerHTML;//取消入库数量
    if (confirm("您确定要取消入库吗?")) {
        $
            .post(
                "/cbtconsole/order/updatecanceltatus",
                // "/cbtconsole/WebsiteServlet?action=updatecanceltatus&className=ExpressTrackServlet",
                {
                    orderid: orderid,
                    goodid: goodid,
                    repState: repState,
                    warehouseRemark: warehouseRemark,
                    odid: odid,
                    count: count
                }, function (res) {
                    if (res >= 1) {
                        //alert("取消入库成功,请刷新页面!");
                        var totalBuyCount = $("#totalBuyCount").val();//剩余入库商品数量
                        var remaining = Number(totalBuyCount)
                            + Number(count);
                        $("#totalBuyCount").val(remaining);
                        var shipno = $('#shipno').val();
                        $("#search").val(shipno);
                        search();
                    } else {
                        alert("取消入库失败!");
                    }
                });
    }
}

/**
 * 商品入库
 * @param isok
 * @param orderid
 * @param goodid
 * @param itemid
 * @param taobaoprice
 * @param shipno
 * @param strcartype
 * @param usid
 * @param goodspprice
 * @param goodurl
 * @param status
 * @param index
 * @param repState
 */
function updategoodstatus(isok, goodspid, orderid, goodid, itemid, taobaoprice, shipno,
                          strcartype, usid, goodspprice, position, odid,taobaoId, goodurl, status, index, repState) {
    position = position.replace("CR-", "");
    var warehouseRemark = $('textarea[name ="warehouseRemark' + index + '"]')
        .val();
    var barcode = document.getElementById("code_" + odid + "").innerHTML;
    var goods_name = document.getElementById("name_" + odid + "").innerHTML;
    var userid = $("#h_b5").val();
    var userName = $("#h_b6").val();
    var count = "0";
    var seiUnit = "";
    var _count = "";
    var record_ = "";
    if (document.getElementById("goods_state").checked == false) {
        goodspid = 1
    }
    ;
    if (checked == "1") {
        seiUnit = document.getElementById("unit_" + odid + "").innerHTML;
        seiUnit = seiUnit.replace(/[^0-9]/ig, "");//产品计量单位
        if (seiUnit == null || seiUnit == "null" || seiUnit == "") {
            seiUnit = "1";
        }
        count = document.getElementById("" + orderid + "count_" + odid + "").value;//验货数量
        _count = document.getElementById("" + orderid + "_count" + odid + "").value;//销售数量
        record_ = document.getElementById("" + orderid + "record_" + odid + "").innerHTML;//已验货数量
        if (count == null || count == "" || Number(count) <= 0) {
            alert("请输入验货数量");
            return;
        }
    } else {
        seiUnit = document.getElementById("unit_" + odid + "").innerHTML;
        seiUnit = seiUnit.replace(/[^0-9]/ig, "");//产品计量单位
        var totalBuyCount = $("#totalBuyCount").val();//剩余入库商品数量
        var _count = document.getElementById("" + orderid + "_count" + odid + "").innerHTML;//入库数量
        if (Number(totalBuyCount) <= 0) {
            alert("入库数量大于剩余采购数量，不能入库该商品");
            return;
        } else {
            var remaining = Number(totalBuyCount) - Number(_count);
            $("#totalBuyCount").val(remaining);
        }

    }
    var tbOrderId = $("#tborderid").val();
    if ($("#h_b7").val() == '0') {
        $("#kwhid").focus();
        alert("库位号输入有误，或者没有输入，请输入正确的库位号");
        return;
    }
    if (barcode.length == 0) {
        $("#kwhid").focus();
        alert("请重新输入入库条形码!");
    } else {
        $(isok).css("background", "red");
        $(isok).attr("disabled", true);
        $.ajax({
            type: 'POST',
            async: true,
            url: '/cbtconsole/order/updateGoodStatus',
            // url : '/cbtconsole/WebsiteServlet?action=updateGoodStatus&className=ExpressTrackServlet',
            data: {
                'goods_pid': goodspid,
                'orderid': orderid,
                'goodid': goodid,
                'status': status,
                'goodurl': goodurl,
                'barcode': barcode,
                'userid': userid,
                'userName': userName,
                'tbOrderId': tbOrderId,
                'odid': odid,
                'shipno': shipno,
                'itemid': itemid,
                'taobaoprice': taobaoprice,
                'repState': repState,
                'warehouseRemark': warehouseRemark,
                'count': count,
                'taobaoId':taobaoId
            },
            dataType: 'text',
            success: function (res) {
                var Positions = $("#dPositions").html();
                if (Positions.indexOf($("#h_b8").val()) < 1) {
                    var pos = Positions.substr(Positions.length - 1,
                        Positions.length);
                    if (pos == "," || pos == ":") {
                        Positions += $("#h_b8").val();
                    } else {
                        Positions += "," + $("#h_b8").val();
                    }
                    $("#dPositions").html(Positions);
                }
                //单件商品位置
                $("#position" + odid).css("color", "green");
                $("#position" + odid).html("存放位置：" + barcode);
                if (status == 0) {
                    var rkcnt = Number($("#rk" + orderid).html());
                    rkcnt += 1;
                    if ($("#status" + odid).html().indexOf("已经到货") < 0) {
                        $("#rk" + orderid).html(rkcnt);
                    }
                    $("#status" + odid).css("color", "green");
                    $("#status" + odid).html("已到货");
                    if (res != 999) {
                        $("#status" + orderid).css("color", "red");
                        $("#status" + orderid).html("不能出货");
                    } else {
                        $("#status" + orderid).css("color", "green");
                        $("#status" + orderid).html("可以出货");
                    }
                } else if (status == 1) {
                    if (res == 0) {
                        $("#status" + odid).css("color", "red");
                        $("#status" + odid).html("入库失败");
                    } else {
                        $("#status" + odid).css("color", "");
                        $("#status" + odid).html("已入库没有验货");
                    }
                } else if (status == 2) {
                    $("#status" + odid).css("color", "red");
                    $("#status" + odid).html("该货没到");
                } else if (status == 3) {
                    $("#status" + odid).css("color", "red");
                    $("#status" + odid).html("破损");
                } else if (status == 4) {
                    $("#status" + odid).css("color", "red");
                    $("#status" + odid).html("有疑问");
                } else if (status == 5) {
                    $("#status" + odid).css("color", "red");
                    $("#status" + odid).html("数量不够");
                } else if (status == 6) {
                	$("#status" + odid).css("color", "red");
                	$("#status" + odid).html("品牌未授权");
                }
            }
        });
    }
    // 标签打印
    if (status != 1) {
        put_print(orderid, usid, odid, strcartype, count, loginName,
            tbOrderId, _count, record_, seiUnit, goods_name, barcode,
            goodurl, odid, position);
    }
}

//验货是可以重新打印标签
function relabel(orderid, odid, taobao_itemid, strcar_type, userid, goods_p_price, goodurl) {
    document.getElementById("" + orderid + "_relabel_" + odid + "").disabled = true;
    var goods_name = document.getElementById("name_" + odid + "").innerHTML;
    var barcode = document.getElementById("code_" + odid + "").innerHTML;
    var tbOrderId = $("#tborderid").val();
    var seiUnit = document.getElementById("unit_" + odid + "").innerHTML;
    seiUnit = seiUnit.replace(/[^0-9]/ig, "");//产品计量单位
    if (seiUnit == null || seiUnit == "null" || seiUnit == "") {
        seiUnit = "1";
    }
    var count = document.getElementById("" + orderid + "count_" + odid + "").value;//验货数量
    var _count = document.getElementById("" + orderid + "_count" + odid + "").value;//销售数量
    var record_ = document.getElementById("" + orderid + "record_" + odid + "").innerHTML;//已验货数量
    if (count == null || count == "" || Number(count) <= 0) {
        alert("请输入验货数量");
        return;
    }
    window.setTimeout(function () {
        document.getElementById("" + orderid + "_relabel_" + odid + "").disabled = false;
    }, 1500);
    put_print(orderid, userid, odid, strcar_type, count, loginName,
        tbOrderId, _count, record_, seiUnit, goods_name, barcode,
        goodurl, odid);
}

/**
 * 入库标签打印
 * @param orderid
 * @param usid
 * @param goodid
 * @param strcartype
 * @param count
 * @param loginName
 * @param tbOrderId
 * @param count1
 * @param record_
 * @param unit
 * @param goods_name
 * @param barcode
 * @param goodurl
 */
function put_print(orderid, usid, odid, strcartype, count, loginName,
                   tbOrderId, count1, record_, unit, goods_name, barcode, goodurl, odid, position,
                   dp_num, dp_total, dp_city, dp_province, dp_country) {
    var d = new Date();
    var str = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
    document.getElementById("div_body").style.display = "none";
    document.getElementById("div_print1").style.display = "";
    document.getElementById("scanning_frame").style.display = "none";
    document.getElementById("taobao_info").style.display = "none";
    document.getElementById("barcode_info").style.display = "none";
    document.getElementById("operating_area").style.display = "none";
    document.getElementById("divTip").style.display = "none";
    if (dp_num > 0) {
        if (dp_total == 1) {
            document.getElementById('td1').innerHTML = orderid + "(D,T1)";
        } else {
            document.getElementById('td1').innerHTML = orderid + "(D)";
        }
    } else {
        document.getElementById('td1').innerHTML = orderid;
    }
    if (dp_num > 0) {
        document.getElementById('td2').innerHTML = usid + "(" + dp_city + "," + dp_province + "," + dp_country + ")";
    } else {
        document.getElementById('td2').innerHTML = usid;
    }

    document.getElementById('td3').innerHTML = odid;
    $("#td5").html(strcartype);
    document.getElementById('td6').innerHTML = count;
    document.getElementById('td7').innerHTML = str;
    document.getElementById('td10').innerHTML = position;
    document.getElementById('td9').innerHTML = tbOrderId;
    // jQuery('#qrcode').qrcode({
    //     width : 50,
    //     height : 50,
    //     text : goodid
    // });
    var barcode = $('#barcode_img'),
        str = odid,
        options = {
            format: "CODE128",
            displayValue: false,
            fontSize: 10,
            height: 20
        };
    JsBarcode(barcode, str, options);//原生
    barcode.JsBarcode(str, options);//jQuery
    setTimeout(
        function () {
            window.print();
            document.getElementById("div_body").style.display = "block";
            document.getElementById("scanning_frame").style.display = "block";
            document.getElementById("taobao_info").style.display = "block";
            document.getElementById("barcode_info").style.display = "block";
            document.getElementById("operating_area").style.display = "block";
            document.getElementById("divTip").style.display = "block";
            document.getElementById("div_print1").style.display = "none";
            document.getElementById('td1').innerHTML = "";
            document.getElementById('td2').innerHTML = "";
            document.getElementById('td3').innerHTML = "";
            $("#td5").html("");
            document.getElementById('td6').innerHTML = "";
            document.getElementById('td7').innerHTML = "";
            document.getElementById('td9').innerHTML = "";
            document.getElementById('td10').innerHTML = "";
            // document.getElementById('barcode_img').src="";
            // setTimeout(
            //     function() {
            //         var inventory_count = Number(count) + Number(record_)- (Number(count1) * Number(unit));//库存数量
            //         if ((Number(count) + Number(record_)) > (Number(count1) * Number(unit)) && Number(inventory_count) > 0 && goodid !="1400") {
            //             addInventory(barcode,inventory_count,orderid,goodid,count,record_,unit,goods_name,tbOrderId,strcartype,goodurl);
            //         }
            //     }, 1000)
        }, 1000)

}

// $.ajaxSetup({
//     async : false
// });

function addInventory(barcode, inventory_count, orderid, odid, count, record_, unit, goods_name, tbOrderId, strcartype, goodurl) {
    $.ajax({
        url: "/cbtconsole/WebsiteServlet?action=addInventory&className=ExpressTrackServlet",
        data: {
            "barcode": barcode,
            "inventory_count": inventory_count,
            "orderid": orderid,
            "odid": odid,
            "storage_count": Number(count) + Number(record_),
            "when_count": Number(count),
            "unit": unit
        },
        type: "post",
        success: function (data) {
            if (data > 0) {
                //打印库存标签
                alert("验货数量大于销售数量,存库【"
                    + inventory_count + "】件");
                put_print1(strcartype, inventory_count,
                    tbOrderId, goods_name, barcode,
                    odid, goodurl);
            }
        }
    });
}

/**
 * 验货无误
 * @param isok
 * @param orderid
 * @param goodid
 * @param itemid
 * @param taobaoprice
 * @param shipno
 * @param strcartype
 * @param usid
 * @param goodspprice
 * @param goodurl
 * @param status
 * @param index
 * @param repState
 */
function updateCheckStatus(isok, orderid, goodid, itemid, taobaoprice, shipno,
                           strcartype, usid, goodspprice, position, odid, isDropshipOrder, goodurl, status, index, repState,
                           dp_num, dp_total, dp_city, dp_province, dp_country) {
    position = position.replace("CR-", "");
    var seiUnit = document.getElementById("unit_" + odid + "").innerHTML;
    var unit = seiUnit.replace(/[^0-9]/ig, "");//产品计量单位
    if (unit == null || unit == "null" || unit == "") {
        unit = "1";
    }
    var warehouseRemark = $('textarea[name ="warehouseRemark' + index + '"]')
        .val();
    var barcode = document.getElementById("code_" + odid + "").innerHTML;//库位
    var userid = $("#h_b5").val();
    var userName = $("#h_b6").val();
    var tbOrderId = $("#tborderid").val();
    var count = document.getElementById("" + orderid + "count_" + odid + "").value;//验货数量
    var goods_name = document.getElementById("name_" + odid + "").innerHTML;
    var _count = document.getElementById("" + orderid + "_count" + odid + "").value;//销售数量
    var record_ = document.getElementById("" + orderid + "record_" + odid + "").innerHTML;//已验货数量
    // var weight= $("#"+orderid+"weight"+goodid+"").val();
    if ((Number(count) + Number(record_)) < (Number(_count) * Number(unit))) {
        alert("验货数量与销售数量不符,不能验货无误");
        return;
    } else if ((Number(count) + Number(record_)) > (Number(_count) * Number(unit))) {
        // alert("验货数量大于销售数量,存库【"+(Number(count)+Number(record_)-(Number(_count)*Number(unit)))+"】件");
        //          	put_print1(strcartype,(Number(count)+Number(record_)-(Number(_count)*unit)),tbOrderId,goods_name,barcode,goodid);
    } else if (count == null || count == "" || Number(count) == 0) {
        alert("请输入验货数量");
        return;
    }
    $
        .ajax({
            // url : "/cbtconsole/WebsiteServlet?action=updateCheckStatus&className=ExpressTrackServlet",
            url: "/cbtconsole/order/updateCheckStatus",
            type: "post",
            async: true,
            data: {
                orderid: orderid,
                goodid: goodid,
                status: status,
                goodurl: goodurl,
                barcode: barcode,
                userid: userid,
                userName: userName,
                tbOrderId: tbOrderId,
                shipno: shipno,
                itemid: itemid,
                odid: odid,
                taobaoprice: taobaoprice,
                repState: repState,
                warehouseRemark: warehouseRemark,
                count: (Number(count) + Number(record_))
                // weight:weight
            },
            success: function (data) {
                var res = data.split(",")[0];
                if (res > 0) {
                    var num = data.split(",")[1];
                    $(isok).attr("disabled", true);
                    //获取商品重量
                    getWeight(orderid, odid);
                    if (num > 0) {
                        console.log("该订单商品已全部的到货并且验货无误，提示跳转出货审核页面");
                        document.getElementById("chuku_" + orderid + "_" + odid + "").style.display = "inline-block";
                    }
                }
            }
        });
    $(isok).css("background", "red");
    $("#status" + odid).css("color", "red");
    $("#status" + odid).html("已验货");
    var inventory_count = Number(count) + Number(record_) - (Number(_count) * Number(unit));//库存数量
    if (isDropshipOrder == 3) {
        addInventory(barcode, count, orderid, odid, count, record_, unit, goods_name, tbOrderId, strcartype, goodurl);
    } else if ((Number(count) + Number(record_)) > (Number(_count) * Number(unit)) && Number(inventory_count) > 0 && goodid != "1400") {
        addInventory(barcode, inventory_count, orderid, odid, count, record_, unit, goods_name, tbOrderId, strcartype, goodurl);
    }
    put_print(orderid, usid, odid, strcartype, count, loginName, tbOrderId,
        _count, record_, unit, goods_name, barcode, goodurl, odid, position,dp_num, dp_total, dp_city, dp_province, dp_country);
}

//仓库位置
function getPosition() {
    var barcode = $("#kwhid").val();
    if (barcode.length < 1) {
        return;
    }
    $
        .post(
            "/cbtconsole/WebsiteServlet?action=getPositionAction&className=ExpressTrackServlet",
            {
                barcode: barcode,
            }, function (res) {
                if (res != '') {
                    $("#h_b8").val(res);
                    $("#h_b7").val("1");
                    $("#search").val("");
                    $("#search").focus();
                } else {
                    $("#positionid").html("库位条形码输入有误，查不到相关库位");
                    $("#h_b7").val("0");
                }
            });
    $("#kwhid").blur();
}

/**
 * 一键入库
 * @param type
 */
function allTrack(type) {
    var shipno = $("#ydid").html().split(":")[1];
    var barcode = document.getElementById("suggest_location").innerHTML;
    var userid = $("#h_b5").val();
    var userName = $("#h_b6").val();
    var count = "0";
    var tbOrderId = $("#tborderid").val();
    var rsJson =[];
    if(type == 1){
        if(curr_order_no == ""){
            alert("请选择一个我司订单入库");
            return false;
        }
        var suCount = 0;
        var message = "";
        for(var i=0;i<jsonList.length;i++){
            if(jsonList[i]["orderid"] == curr_order_no){
                if(parseInt(jsonList[i]["buycount"])  < parseInt(jsonList[i]["usecount"])){
                    suCount ++;
                    message = "订单：" + curr_order_no + ",odid:" + jsonList[i]["odid"] + ",采购数量:"
                        + jsonList[i]["buycount"] + ",少于客户购买数量:" + jsonList[i]["usecount"];
                    break;
                }
                rsJson.push(jsonList[i]);
            }
        }
        if(suCount > 0){
            alert(message);
            return false;
        }
    }
    $.ajax({
        // url : "/cbtconsole/WebsiteServlet?action=updateCheckStatus&className=ExpressTrackServlet",
        url: "/cbtconsole/order/allTrack",
        type: "post",
        async: true,
        data: {
            type: type,
            tbInfo: JSON.stringify(rsJson),
            shipno: shipno,
            barcode: barcode,
            userid: userid,
            userName: userName,
            tbOrderId: tbOrderId
        },
        success: function (res) {
            if (res > 0) {
                if (type == "1") {
                    alert("一键确认入库成功");
                } else if (type == "0") {
                    alert("一键取消入库成功");
                }
            } else {
                if (type == "1") {
                    alert("一键确认入库失败");
                } else if (type == "0") {
                    alert("一键取消入库失败");
                }
            }
        }
    });
}

function celsearch() {
    $("#search").val('');
}


function btnCap(orderid, odid) {
    getPhoto();
    var imgData = document.getElementById("canvas").toDataURL();
    var base64Data = imgData.substr(22);
    $.ajax({
        url: "/cbtconsole/WebsiteServlet?action=uploadImage&className=ExpressTrackServlet",
        type: "post",
        data: {
            "fileData": base64Data,
            orderid: orderid,
            odid: odid
        },
        async: false,
        success: function (response) {
            if (response.status == 1) {
                $("#bt_" + orderid + "_" + odid + "").css("color", "red");
                $("#pics_" + orderid + odid + "").append("<img width='40px' height='40px' onmouseout='closeBigImg();' ondblclick='delPics(this,\"" + orderid + "\",\"" + odid + "\",\"" + response.picPath + "\",\"" + response.localPath + "\")' onmouseover='BigImg(\"" + response.localPath + "\")'  src=\"" + response.localPath + "\"></img>");
                // setInterval(function(){
                //     $("#pics_"+orderid+odid+"").find('img').each(function(){
                //         var imgSrc=$(this).attr('data-original');
                //         $(this).attr('src',imgSrc);
                //     })
                // },100);


            } else {
                alert("图片上传失败,请重新拍摄");
            }
        },
        error: function (e) {
            alert("图片上传失败");
        }
    });
}

function initPhoto() {

    $("#search").focus();
    // 开始摄像头
    getMedia();
}

function getMedia() {
    var constraints = {
        video: {width: 500, height: 500},
        audio: true
    };
    //获得video摄像头区域
    var video = document.getElementById("video");
    //这里介绍新的方法，返回一个 Promise对象
    // 这个Promise对象返回成功后的回调函数带一个 MediaStream 对象作为其参数
    // then()是Promise对象里的方法
    // then()方法是异步执行，当then()前的方法执行完后再执行then()内部的程序
    // 避免数据没有获取到
    var promise = navigator.mediaDevices.getUserMedia(constraints);
    promise.then(function (MediaStream) {
        video.srcObject = MediaStream;
        video.play();
    });
}


function getPhoto() {
//获得Canvas对象
    var video = document.getElementById("video");
    var canvas = document.getElementById("canvas");
    var ctx = canvas.getContext('2d');
    ctx.drawImage(video, 0, 0, 400, 400);
}

function put_print1(strcartype, count, tbOrderId, goods_name, barcode, odid, goodurl, id_barcode) {
    var d = new Date();
    var str = d.getFullYear() + "-" + (d.getMonth() + 1) + "-" + d.getDate();
    document.getElementById("div_body").style.display = "none";
    document.getElementById("div_print1").style.display = "none";
    document.getElementById("scanning_frame").style.display = "none";
    document.getElementById("taobao_info").style.display = "none";
    document.getElementById("barcode_info").style.display = "none";
    document.getElementById("operating_area").style.display = "none";
    document.getElementById("divTip").style.display = "none";
    document.getElementById("div_print2").style.display = "block";
    document.getElementById('barcode').innerHTML = "-" + barcode;
    document.getElementById('goodsid').innerHTML = odid;
    document.getElementById('goods_name').innerHTML = goods_name.substr(0, goods_name.length / 4);
    document.getElementById('sku3').innerHTML = strcartype;
    document.getElementById('count2').innerHTML = count;
    document.getElementById('tborderid3').innerHTML = tbOrderId;
    document.getElementById('td14').innerHTML = str;
    jQuery('#qrcode3').qrcode({
        width: 50,
        height: 50,
        text: odid
    });
    window.print();
    document.getElementById("div_body").style.display = "block";
    document.getElementById("scanning_frame").style.display = "block";
    document.getElementById("taobao_info").style.display = "block";
    document.getElementById("barcode_info").style.display = "block";
    document.getElementById("divTip").style.display = "block";
    document.getElementById("operating_area").style.display = "block";
    document.getElementById("div_print1").style.display = "none";
    document.getElementById("div_print2").style.display = "none";
    document.getElementById('barcode').innerHTML = "";
    document.getElementById('goodsid').innerHTML = "";
    document.getElementById('goods_name').innerHTML = "";
    document.getElementById('sku3').innerHTML = "";
    document.getElementById('count2').innerHTML = "";
    document.getElementById('tborderid3').innerHTML = "";
    document.getElementById('td14').innerHTML = "";
    $("#qrcode3").html("");
}

function search() {
    document.getElementById('operatediv').style.display = 'block';
    $("#kwhid").val("");
    totalqty = "0";
    userBuyCount = "0";
    info = "";
    $("#tbImg").attr("src", "");
    $("#tbhref").attr("href", "");
    $("#itemName").html("");
    $("#totalprice").html("");
    $("#orderStatus").html("");
    $("#itemprice").html("");
    $("#itemqty").html("");
    $("#shipno").html("");
    $("#shipper").html("");
    $("#shipstatus").html("");
    var expresstrackid = $("#search").val();
    $("#shipno").val(expresstrackid);
    if ($("#search").val().length < 1) {
        return;
    }
    var flag = "0";
    var remarkUserId = "";
    var buyUrl = "";
    var jsonObj = "";
    var selectType = $("#select_count").val();
    $.post("/cbtconsole/order/getGoodsData", {expresstrackid: expresstrackid},
        function (res) {
            jsonObj = JSON.parse(res);
            if (res == "[]") {
                $("#taobaoInfos").html("");
                $("#div1").html('');
                $("#search").val('');
            } else {
                flag = "1";
                $("#div1").html('');
                var str = "";
                for (var i = 0; i < jsonObj.length; i++) {
                    totalqty = Number(totalqty) + Number(jsonObj[i].itemqty);
                    remarkUserId = jsonObj[i].username;
                    buyUrl = jsonObj[i].itemurl;
                    str += '<table border="1" width="310px" height="550px" style="float:left;margin-left:2px";"><tr>';
                    str += '<td width="220px"><br><div>商品名称：'
                        + jsonObj[i].itemname
                        + '</div></br><div>';
                    str += '<div style="float:left;"><a href="' + jsonObj[i].itemurl + '" target="_blank">';
                    str += '<img width="300px" height="300px" src="' + jsonObj[i].imgurl + '"/></a></div>';
                    str += '<div style="float:right;width:280px;">';
                    str += '<p style = "font-size:12px;">商品单价：'
                        + jsonObj[i].itemprice
                        + '</p>';
                    str += '<p style="word-wrap: break-word">商品规格：'
                        + jsonObj[i].sku
                        + '</p>';
                    str += '<p>商品数量：'
                        + jsonObj[i].itemqty
                        + '</p>';
                    $("#tborderid").val(jsonObj[i].orderid);
                    str += '</div>';
                    str += '<div style="clear:both;"></div>';
                    str += '</div></td></tr></table>';
                }
                $("#totalBuyCount").val(totalqty);
                str += '<table><div style="float: left; left: 0px; clear:both;margin-left:10px";><br><p>商品总价:'
                    + jsonObj[0].totalprice
                    + '&nbsp;&nbsp;&nbsp;物流单号：'
                    + jsonObj[0].shipno
                    + '&nbsp;&nbsp;&nbsp;物流公司:'
                    + jsonObj[0].shipper
                    + '</p>';
                str += '<div >物流状态：'
                    + jsonObj[0].shipstatus
                    + '</div>';
                str += '<div><span id="insertMessage"></span></div>';
                str += '</div></table>';

                $("#taobaoInfos").html('');
                $("#taobaoInfos").html('<h3 style="color:bisque;padding:10px 20px;background:#f5f5f5;color:#f40">淘宝信息</h3>' + str);
                $("#taobaoInfos").css("color", "black");
            }
        })
    $("#div").css("color", "red");
    $("#div").html("正在查询中，请稍候......");
    var flag_storage = "0";
    if (expresstrackid != '') {
        var barcode_info = "";
        // $.post("/cbtconsole/WebsiteServlet?action=getResultInfo&className=ExpressTrackServlet",{expresstrackid : expresstrackid,checked:checked},
        $.post("/cbtconsole/order/getResultInfo", {expresstrackid: expresstrackid, checked: checked, selectType:selectType},
            function (res) {
                if (res == "[]") {
                    $("#ydid").html("查询的运单号为:" + expresstrackid);
                    $("#div").html("当前订单已出库或已完结！或者当前扫描的快递号是原链接购买商品，请从下面列表中查找入库,谢谢！！");
                    $("#ship").html(expresstrackid)
                } else {
                    flag = "0";
                    //清空订单id文本框
                    if (checked == "1") {
                        $("#ydid").html("查询的运单号为:" + expresstrackid + "");
                        $("#ship").html(expresstrackid)
                    } else {
                        $("#ydid").html("查询的运单号为:" + expresstrackid);
                        $("#ship").html(expresstrackid)
                    }
                    $("#search").val('');
                    $('#startv').show();
                    var preorderid = '';
                    var num = 1;
                    var json = eval(res);
                    var size = 4;
                    var str = "";
                    var orderstate = "不能出货";
                    var color = "red";
                    var img = "";
                    $("#h_b4").val(json[0].taobao_orderid);
                    $("#itemid").val(json[0].taobao_itemid);
                    var order_num = json[0].order_num;
                    curr_order_num = order_num;
                    orderJson = {};
                    jsonList = [];
                    if (order_num > 1) {
                        $("#ydid").append("<span style='color:red;font-size:30px;'>【注意:该包裹对应了多个销售订单】</span>");
                    }
                    for (var i = 0; i < json.length; i++) {
                        userBuyCount = Number(userBuyCount) + Number(json[i].buycount);
                        img = json[i].goods_img_url;
                        if (preorderid == json[i].orderid) {
                        } else {
                            if (json[i].orderstate = 0) {
                                orderstate = "可以出货";
                                color = "green";
                            }

                            orderJson[json[i].orderid] = json[i].orderid;
                            getTaoBaoInfo(json[i].orderid);
                            $("#user_id").val(json[i].userid);
                            $("#import_id").val(json[i].orderid);
                            barcode_info += '<div id="barcode_info">';
                            barcode_info += '<div style="clear:both;"><h3 style="color:bisque;padding:10px 20px;background:#f5f5f5;color:#f40">库位信息</h3></br>ImportExpress 订单号：<a target="_blank" href="/cbtconsole/orderDetails/queryByOrderNo.do?orderNo='
                                + json[i].orderid
                                + '">'
                                + json[i].orderid
                                + '</a>' + json[i].invoice + '<br/>建议库位：<span style="color:red;font-size:24px;" id="suggest_location">'
                                + code
                                + '</span><br/>';
                            if (tbinfo.length > 0) {
                                for (var j = 0; j < tbinfo.length; j++) {
                                    barcode_info += "关联淘宝/1688订单[" + (j + 1) + "]:" + tbinfo[j].orderid + ';入库时间:' + tbinfo[j].storageTime + ';库位:' + tbinfo[j].barcode + ';可能关联的其他订单:' + tbinfo[j].itemname + '<br/>';
                                }
                            }
                            barcode_info += '总件数:'
                                + json[i].ordercount
                                + ';用户id：'
                                + json[i].userid
                                + '</div>';
                            barcode_info += '</div>';
                            barcode_info += '<div id="operating_area">';
                            barcode_info += '<div id="dPositions"><h3 style="color:bisque;padding:10px 20px;background:#f5f5f5;color:#f40">操作区</h3></br>所有商品存放仓库的位置:'
                            barcode_info += json[i].tbOrderIdPositions == null ? '还未存放' : json[i].tbOrderIdPositions;
                            barcode_info += '</div>';
                            if (json[i].orderremark.length > 0) {
                                barcode_info += '<div style="font-size: 20px;font-weight:bold;">备注内容(对内):'
                                var orderremark = json[i].orderremark;
                                for (var j = 0; j < orderremark.length; j++) {
                                    barcode_info += '<p>' + '备注人员：' + orderremark[j][2] + ',内容：' + orderremark[j][1] + '</p>';
                                }
                                barcode_info += '</div>';
                            }
                        }
                        str += '<table border="1" style="width:600px;height:800px;float:left; margin-left:5px; margin-bottom:5px;"><tr>';
                        str += '<td width="400px"><div>订单号:<span>' + json[i].orderid + '</span><input class="choose_chk chb_'+json[i].orderid+'" type="checkbox" disabled="disabled"/> </div><div>商品id：'
                            + json[i].goodsid + ';odid:' + json[i].odid + '</div>';
                        str += '<div>商品名称：<span id="name_' + json[i].odid + '">'
                            + json[i].goods_name
                            + '</span></div><div>';
                        var jsonCld = {};
                        curr_order_no = json[i].orderid;
                        jsonCld["orderid"] = json[i].orderid;
                        jsonCld["goodsid"] = json[i].goodsid;
                        jsonCld["odid"] = json[i].odid;
                        jsonCld["goods_pid"] = json[i].goods_pid;
                        jsonCld["taobao_orderid"] = json[i].taobao_orderid;
                        jsonCld["taobao_itemid"] = json[i].taobao_itemid;
                        jsonCld["skuID"] = json[i].skuID;
                        jsonCld["specId"] = json[i].specId;
                        jsonCld["buycount"] = json[i].buycount;
                        jsonCld["usecount"] = json[i].usecount;
                        jsonList.push(jsonCld);
                        str += '<div style="float:left;"><a href="https://www.import-express.com/goodsinfo/classic-vintage-heart-peach-heart-appearance-box-po-necklace-many-18888-122322005-1' + json[i].goods_pid + '.html" target="_blank">';
                        //str += '<div style="float:left;"><a href="'+json[i].goods_p_url+'" target="_blank">';
                        /*if(json[i].img){
                            if (json[i].img[0].length > 5 && json[i].img != null && json[i].img != "undefined") {
                                str += '<img width="300px" height="300px" src="'+json[i].img+'"/></a></div>';
                            } else{
                                str += '<img width= "300px" height="300px" src="'+img+'"/></a></div>';
                            }
                        }*/
                        //修正图片显示   大图显示  电商产品单页主图  点击显示  线上产品 详情
                        str += '<img width= "300px" height="300px" src="' + json[i].goods_img_url + '"/></a></div>';
                        str += '<div style="float:right;width:280px;">';
                        var pt = 0;
                        str += '<p id=' + "position" + json[i].odid + '>位置:<font color="red">';
                        if (json[i].position != '') {
                            str += json[i].position;
                            pt = 1;
                        } else {
                            str += '还未存放';
                        }
                        str += '</font> </p>';
                        str += '核查建议：<span style="color:cyan;font-size:24px;">'
                            + json[i].authorizedFlag
                            + '</span><br/>';
                        str += '建议库位：<span style="color:red;font-size:22px;" id="code_' + json[i].odid + '">'
                            + code
                            + '</span><br/>';
                        str += '产品单位：<span style="color:blue;font-size:20px;" id="unit_' + json[i].odid + '">'
                            + json[i].gcUnit
                            + '</span><br/>';
                        str += '客户备注：<span style="color:red;font-size:20px;font-weight:	bold;">'
                            + json[i].odRemark
                            + '</span>';
                        if (json[i].purchase_state == 1) {
                            if (checked == "0") {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "确认采购货源"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                            } else if (chescked == "1" && json[i].checked == 0) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "未验货"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                            } else if (checked == "1" && json[i].checked == 1) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "已验货"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                            }
                            //临时更改，后期可能需要重新处理
                            /*if (json[i].img != null || json[i].img != undefined || json[i].img != '') {
                                str += '<p>图片: <img width = "150" height="150" src="'+img+'"/></p>';
                            } else{
                                str += '<p>图片: <img width = "150" height="150" src="'+json[i].img+'"/></p>';
                            }*/
                            str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                            source1688_img
                            str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                        } else if (json[i].purchase_state == 2) {
                            if (checked == "0") {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "开始采购"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p> ';
                            } else if (checked == "1" && json[i].checked == 0) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "未验货"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p> ';
                            } else if (checked == "1" && json[i].checked == 1) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "已验货"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p> ';
                            }
                            str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                            str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                        } else if (json[i].purchase_state == 3) {
                            if (checked == "0") {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "采购中,未到货"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p> ';
                            } else if (checked == "1" && json[i].checked == 0) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "未验货"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p> ';
                            } else if (checked == "1" && json[i].checked == 1) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "已验货"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p> ';
                            }
                            str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                            str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                        }
                        else if (json[i].purchase_state == 4) {
                            if (checked == "0") {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="green">'
                                    + "已经到货"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                            } else if (checked == "1" && json[i].checked == 0) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="green">'
                                    + "未验货"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                            } else if (checked == "1" && json[i].checked == 1) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="green">'
                                    + "已验货"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                            }
                            str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                            str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                        } else if (json[i].purchase_state == 5) {
                            if (json[i].goodstatus == 2) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "货源问题:该货没到"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                                str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                                str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                            }
                            else if (json[i].goodstatus == 3) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "货源问题:破损"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                                str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                                str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                            }
                            else if (json[i].goodstatus == 4) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "货源问题:有疑问"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                                str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                                str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                            }
                            else if (json[i].goodstatus == 5) {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + "货源问题:数量不够"
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                                str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                                str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                            }
                            else {
                                str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                    + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                                str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                                str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                            }
                        }
                        if (checked == "1") {
                            queryRecord(json[i].odid);
                            str += '<p style="font-size:16px;font-weight:bold;" class="strcarype"><h4>已验货数量:<span id="' + json[i].orderid + 'record_' + json[i].odid + '" style="font-size:30px;color:#ff0000;">'
                                + id_qty
                                + '</span></h4><h4>单件重量(kg):<input type="text" style="width: 70px;" id="' + json[i].orderid + 'weight' + json[i].odid + '"></h4>'
                                + '<h4>体积重量(kg):<input type="text" style="width: 70px;" id="' + json[i].orderid + 'volumeWeight' + json[i].odid + '"></h4>';
                            /*'<input type="button" value="获取商品重量" onclick="getWeight(\''+json[i].orderid+'\',\''+json[i].odid+'\')">' +*/
                            if (json[i].weight == undefined) {
                                str += '<span name="save_weight">未保存过重量!</span><input type="button" value="将商品重量同步到产品库" style="margin-left:5px" onclick="saveWeight(\'' + json[i].orderid + '\',\'' + json[i].odid + '\',\'' + json[i].goods_pid + '\')">';
                            } else {
                                str += '<span name="save_weight">已保存的重量:' + json[i].weight + '<em>Kg</em></span>'
                                    + '<br><span name="save_volume_weight">已保存的体积重量:' + json[i].volume_weight + '<em>Kg</em></span>'
                                    + '<input type="button" value="将商品重量同步到产品库" style="margin-left:5px" onclick="saveWeight(\'' + json[i].orderid + '\',\'' + json[i].odid + '\',\'' + json[i].goods_pid + '\')">';
                            }
                            str += '<br />';
                            if (json[i].syn == undefined || json[i].syn == '0') {
                                str += '<span name="save_weight_flag">未同步到产品库!</span>';
                            } else {
                                str += '<span name="save_weight_flag">已同步到产品库</span>';
                            }
                            /*str += '<input type="button" value="将重量同步至产品库" style="margin-left:5px" onclick="saveWeightFlag(\''+json[i].orderid+'\',\''+json[i].odid+'\',\''+json[i].goods_pid+'\')"><br />';*/
                            str += '<span id="tip_' + json[i].orderid + json[i].odid + '" style="color:red"></span><h3>商品验货数量:<input type="hidden" id="' + json[i].orderid + '_count' + json[i].odid + '" value="' + json[i].usecount + '"><input type="text" style="width: 40px;" id="' + json[i].orderid + 'count_' + json[i].odid + '" value=""/>piece</h3></p>';
                        } else {
                            str += '<p style="font-size:16px;font-weight:bold;" class="strcarype"><h3>客户下单数量:<span id="' + json[i].orderid + '_count' + json[i].odid + '" style="font-size:35px;">'
                                + json[i].usecount
                                + '</span></h3></p>';
                        }
                        if (json[i].isExitPhone > 0) {
                            str += '<a target="_blank" href="/cbtconsole/website/inspection_picture_editing.jsp?goodsPid=' + json[i].goods_pid + '&odid=' + json[i].odid + '&oldOrderid=' + json[i].orderid + '" style="color:red">该商品存在验货图片,点击查看</a>';
                        }
                        str += '<div style="height: 50px;">'
                            + '<button style="font-size: 20px;" id="bt_' + json[i].orderid + '_' + json[i].odid + '" onclick="btnCap(\'' + json[i].orderid + '\',\'' + json[i].odid + '\')">拍摄</button><div id="pics_' + json[i].orderid + json[i].odid + '">'
                            + '</div><br />'
                            + '<canvas onclick="AutoResizeImage(this)"  id="canvas_' + json[i].orderid + '_' + json[i].odid + '"></canvas>'
                            + '</div>';
                        str += '</div>';
                        str += '<div style="clear:both;"></div>';
                        str += '<div><span>入库备注:</span><textarea type="text" name="warehouseRemark' + i + '" style="height :30px;" /><a target="_blank" style="display:none;color:red;text-decoration:underline;font-size: 20px;margin-left: 60px" id="chuku_' + json[i].orderid + '_' + json[i].odid + '" href="/cbtconsole/warehouse/getDetailsForOrderid?orderid=' + json[i].orderid + '&pageNum=1&pageSize=300">全部到库跳转到新出货审核页面</a></div><br><div style="clear:both;">';
                        if (checked == "1") {
                            str += '<p><input type="checkbox" id="goods_state" style="color: red" name="cha" value="1">商品质量差(质量差的商品请标记)</p>';
                        }
                        str += '<div style="display:none"><input type="checkbox" id="goods_state" style="color: red" name="cha" value="1">商品质量差(质量差的商品请标记)</div>';

                        var reg = new RegExp("'", "g");
                        //判断商品是否已经存放的
                        if (checked == "0") {
                            flag_storage = "1";
                            str += '<button style="height: 30px;width:80px;" name = "arrival' + i + '" onclick="updategoodstatus(this,\''
                                + json[i].goods_pid
                                + '\',\''
                                + json[i].orderid
                                + '\',\''
                                + json[i].goodsid
                                + '\',\''
                                + json[i].taobao_itemid
                                + '\',\''
                                + json[i].itemprice
                                + '\',\''
                                + expresstrackid
                                + '\',\''
                                + json[i].strcar_type.replace("'", "")
                                + '\',\''
                                + json[i].userid
                                + '\',\''
                                + json[i].goods_p_price
                                + '\',\''
                                + json[i].position
                                + '\',\''
                                + json[i].odid
                                + '\',\''
                                + json[i].taobaoId
                                + '\',\''
                                + json[i].goods_url.replace("'", "").replace(/[\r\n]/g, "")
                                + '\',1,' + i + ',1);">到货</button>';
                            str += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button style="height: 30px;width:80px;" onclick="updatecanceltatus(this,\''
                                + json[i].orderid
                                + '\',\''
                                + json[i].odid
                                + '\',\''
                                + json[i].goodsid
                                + '\',' + i + ')">入库取消</button>';
                        } else {
                            str += '<button style="height: 30px;width:80px;" name = "arrival' + i + '" onclick="updateCheckStatus(this,\''
                                + json[i].orderid
                                + '\',\''
                                + json[i].goodsid
                                + '\',\''
                                + json[i].taobao_itemid
                                + '\',\''
                                + json[i].itemprice
                                + '\',\''
                                + expresstrackid
                                + '\',\''
                                + json[i].strcar_type.replace("'", "")
                                + '\',\''
                                + json[i].userid
                                + '\',\''
                                + json[i].goods_p_price
                                + '\',\''
                                + json[i].position
                                + '\',\''
                                + json[i].odid
                                + '\',\''
                                + json[i].skuID
                                + '\',\''
                                + json[i].specId
                                + '\',\''
                                + json[i].isDropshipOrder
                                + '\',\''
                                + json[i].goods_url.replace("'", "").replace(/[\r\n]/g, "")
                                + '\',1,' + i +',0,' + json[i].dp_num + ',' + json[i].dp_total
                                + ',\'' + json[i].dp_city + '\',\'' + json[i].dp_province + '\',\'' + json[i].dp_country + '\')">验货无误</button>';
                            str += '<button style="height: 30px;width:80px;" onclick="updategoodstatus(this,\''
                                + json[i].goods_pid
                                + '\',\''
                                + json[i].orderid
                                + '\',\''
                                + json[i].goodsid
                                + '\',\''
                                + json[i].taobao_itemid
                                + '\',\''
                                + json[i].itemprice
                                + '\',\''
                                + json[i].shipno
                                + '\',\''
                                + json[i].strcar_type.replace("'", "")
                                + '\',\''
                                + json[i].userid
                                + '\',\''
                                + json[i].goods_p_price
                                + '\',\''
                                + json[i].position
                                + '\',\''
                                + json[i].odid
                                + '\',\''
                                + json[i].taobaoId
                                + '\',\''
                                + json[i].goods_url.replace("'", "").replace(/[\r\n]/g, "")
                                + '\',2,' + i + ',1)">该货没到</button><button style="height: 30px;width:80px;" onclick="updategoodstatus(this,\''
                                + json[i].goods_pid
                                + '\',\''
                                + json[i].orderid
                                + '\',\''
                                + json[i].goodsid
                                + '\',\''
                                + json[i].taobao_itemid
                                + '\',\''
                                + json[i].itemprice
                                + '\',\''
                                + json[i].shipno
                                + '\',\''
                                + json[i].strcar_type.replace("'", "")
                                + '\',\''
                                + json[i].userid
                                + '\',\''
                                + json[i].goods_p_price
                                + '\',\''
                                + json[i].position
                                + '\',\''
                                + json[i].odid
                                + '\',\''
                                + json[i].taobaoId
                                + '\',\''
                                + json[i].goods_url.replace("'", "")
                                + '\',3,' + i + ',1)">破损</button><button style="height: 30px;width:80px;" onclick="updategoodstatus(this,\''
                                + json[i].goods_pid
                                + '\',\''
                                + json[i].orderid
                                + '\',\''
                                + json[i].goodsid
                                + '\',\''
                                + json[i].taobao_itemid
                                + '\',\''
                                + json[i].itemprice
                                + '\',\''
                                + json[i].shipno
                                + '\',\''
                                + json[i].strcar_type.replace("'", "")
                                + '\',\''
                                + json[i].userid
                                + '\',\''
                                + json[i].goods_p_price
                                + '\',\''
                                + json[i].position
                                + '\',\''
                                + json[i].odid
                                + '\',\''
                                + json[i].taobaoId
                                + '\',\''
                                + json[i].goods_url.replace("'", "")
                                + '\',4,' + i + ',1)">有疑问</button><button style="height: 30px;width:80px;" onclick="updategoodstatus(this,\''
                                + json[i].goods_pid
                                + '\',\''
                                + json[i].orderid
                                + '\',\''
                                + json[i].goodsid
                                + '\',\''
                                + json[i].taobao_itemid
                                + '\',\''
                                + json[i].itemprice
                                + '\',\''
                                + json[i].shipno
                                + '\',\''
                                + json[i].strcar_type.replace("'", "")
                                + '\',\''
                                + json[i].userid
                                + '\',\''
                                + json[i].goods_p_price
                                + '\',\''
                                + json[i].position
                                + '\',\''
                                + json[i].odid
                                + '\',\''
                                + json[i].taobaoId
                                + '\',\''
                                + json[i].goods_url.replace("'", "")
                                + '\',5,' + i + ',1)">数量不够</button><button style="height: 30px;width:80px;" onclick="updatecancelChecktatus(this,\''
                                + json[i].orderid
                                + '\',\''
                                + json[i].odid
                                + '\',\''
                                + json[i].isDropshipOrder
                                + '\',\''
                                + json[i].goodsid
                                + '\',' + i + ')">验货取消</button>';
                            if (json[i].isDropshipOrder == 3) {
//											str+='<button style="height: 30px;width:80px;" onclick="window.open(\'/cbtconsole/supplierscoring/supplierproducts?shop_id='+json[i].shop_id+'&goods_pid='+json[i].goods_pid+'\',\'_blank\')">采样商品打分</button>';
//                                 str+='<button style="height: 30px;width:80px;" onclick="openSupplierGoodsDiv(\''+json[i].shop_id+'\',\''+json[i].goods_pid+'\')")">采样商品打分</button>';
                            } else if (json[i].shop_id != null && json[i].shop_id != "" && json[i].shop_id != "0000") {
//											str+='<button style="height: 30px;width:80px;" onclick="window.open(\'/cbtconsole/supplierscoring/supplierproducts?shop_id='+json[i].shop_id+'\',\'_blank\')">供应商打分</button>';
                                str += '<button style="height: 30px;width:80px;" onclick="openSupplierDiv(\'' + json[i].shop_id + '\')">供应商打分</button>';
                            }
                            str += '<button style="height: 30px;width:80px;" id="' + json[i].orderid + '_relabel_' + json[i].odid + '" onclick="relabel(\'' + json[i].orderid + '\',\'' + json[i].odid + '\',\'' + json[i].taobao_itemid + '\',\'' + json[i].strcar_type.replace("'", "") + '\',\'' + json[i].userid + '\',\'' + json[i].goods_p_price + '\',\'' + json[i].goods_url.replace("\'", "") + '\')">重打标签</button>';
                        }
                        str += '</div><div style="height:56px;font-size: 20px;color: red; font-weight: bold">备注：'
                        if (json[i].remark != 'null' && json[i].remark != null) {
                            str += json[i].remark.replace(/Microsoft/g, "</br>")
                        }
                        str += '</div>'
                        str += '</div></td></tr></table>';
                        preorderid = json[i].orderid;
                    }
                    str += '</div>';
                    $("#div").html('');
                    $("#div").html(str + barcode_info);
                    $("#div").css("color", "black");
                    if (checked == "0") {
                        $(".taobao_btn_class").prepend('<button onclick="noChooseBtn()" style="height: 30px;width: 80px;margin-left: 50px;">取消全选</button>');
                        for(var orno in orderJson){
                            $(".taobao_btn_class").prepend('<button id="btn_id_'+orno+'" onclick="chooseOrderAll(\''+orno +'\')" style="color: red;height: 30px;width: 120px;margin-left: 30px;">全选'+orno+'</button>');
                        }

                        $("#sureAllTrack").css("display", "block");
                        $("#canceAllTrack").css("display", "block");
                        $("#btn_id_" + curr_order_no).click();
                    }
                }
            });
        //补货
        if (checked == "0") {
            $.post("/cbtconsole/WebsiteServlet?action=getReplenishResultInfo&className=ExpressTrackServlet", {expresstrackid: expresstrackid},
                function (res) {
                    if (res == "[]") {
                        $("#replenish").html("");
                    } else {
                        var barcode_info = "";
                        flag_storage = "1";
                        flag = "0";
                        var img = "";
                        $("#ydid").html("查询的订单号为:" + expresstrackid);
                        $("#search").val('');
                        $('#startv').show();
                        var preorderid = '';
                        var num = 1;
                        var json = eval(res);
                        var size = 4;
                        var str = "";
                        var orderstate = "不能出货";
                        var color = "red";
                        $("#h_b4").val(json[0].taobao_orderid);
                        $("#itemid").val(json[0].taobao_itemid);
                        for (var i = 0; i < json.length; i++) {
                            img = json[i].goods_img_url;
                            if (preorderid == json[i].orderid) {
                            } else {
                                if (json[i].orderstate = 0) {
                                    orderstate = "可以出货";
                                    color = "green";
                                }
                                getTaoBaoInfo(json[i].orderid);
                                barcode_info += '<div style="padding-top:50px;hright:50px;clear:both;">ImportExpress 订单号：<a target="_blank" href="/cbtconsole/WebsiteServlet?action=getOrderDetail&className=OrderwsServlet&orderNo='
                                    + json[i].orderid
                                    + '">'
                                    + json[i].orderid
                                    + '</a>' + json[i].invoice + '<br/>建议库位：<span style="color:red;font-size:24px;">'
                                    + code
                                    + '</span><br/>';
                                if (tbinfo.length > 0) {
                                    for (var j = 0; j < tbinfo.length; j++) {
                                        barcode_info += "关联淘宝/1688订单[" + (j + 1) + "]:" + tbinfo[j].orderid + ';入库时间:' + tbinfo[j].storageTime + ';库位:' + tbinfo[j].barcode + ';可能关联的其他订单:' + tbinfo[j].itemname + '<br/>';
                                    }
                                }
                                '总件数：'
                                + json[i].ordercount
                                + ';用户id：'
                                + json[i].userid
                                + '</div>';
                                barcode_info += '<div  style="font-size: 30px;font-weight:bold; color: red;">'
                                if (json[i].goodsType == 0) {
                                    barcode_info += '补货订单'
                                } else {
                                    barcode_info += '其他货源'
                                }
                                barcode_info += '</div>';
                                barcode_info += '<div id="dPositions">所有商品存放仓库的位置:'
                                barcode_info += json[i].tbOrderIdPositions == null ? '还未存放' : json[i].tbOrderIdPositions;
                                barcode_info += '</div>';
                                if (json[i].orderremark.length > 0) {
                                    barcode_info += '<div style="font-size: 20px;font-weight:bold;">备注内容(对内):'
                                    var orderremark = json[i].orderremark;
                                    for (var j = 0; j < orderremark.length; j++) {
                                        barcode_info += '<p>' + '备注人员：' + orderremark[j][2] + ',内容：' + orderremark[j][1] + '</p>';
                                    }
                                    barcode_info += '</div>';
                                }
                            }
                            str += '<table border="1" width="600px" style="float:left;"><tr>';
                            str += '<td width="400px"><div>订单号:' + json[i].orderid + '</div><div>商品id：'
                                + json[i].goodsid + ';odid:' + json[i].odid + '</div>';
                            str += '<div>商品名称：<span id="name_' + json[i].odid + '">'
                                + json[i].goods_name
                                + '</span></div><div>';
                            str += '<div style="float:left;"><a href="https://www.import-express.com/goodsinfo/classic-vintage-heart-peach-heart-appearance-box-po-necklace-many-18888-122322005-1' + json[i].goods_pid + '.html" target="_blank">';
                            /*if(json[i].img){
                                                            if (json[i].img[0].length > 5 && json[i].img != null && json[i].img != "undefined") {
                                                                str += '<img width="300px" height="300px" src="'+json[i].img+'"/></a></div>';
                                                            } else{
                                                                str += '<img  width = "300px" height="300px" src="'+img+'"/></a></div>';
                                                            }
                                                        }else{
                                                            str += '<img  width = "300px" height="300px" src="'+img+'"/></a></div>';
                                                        }*/
                            str += '<img width= "300px" height="300px" src="' + json[i].goods_img_url + '"/></a></div>';
                            str += '<div style="float:right;width:280px;">';
                            var pt = 0;
                            str += '<p id=' + "position" + json[i].odid + '>位置:<font color="red">';
                            if (json[i].position != '') {
                                str += json[i].position;
                                pt = 1;
                            } else {
                                str += '还未存放';
                            }
                            str += '</font> </p>';
                            str += '建议库位：<span style="color:red;font-size:24px;" id="code_' + json[i].odid + '">'
                                + code
                                + '</span><br/>';
                            str += '产品单位：<span style="color:blue;font-size:20px;" id="unit_' + json[i].odid + '">'
                                + json[i].gcUnit
                                + '</span><br/>';
                            str += '客户备注：<span style="color:red;font-size:20px;font-weight:	bold;">'
                                + json[i].odRemark
                                + '</span>';
                            if (json[i].purchase_state == 0) {
                                if (checked == "0") {
                                    str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                        + "未完成 补货"
                                        + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                                } else if (checked == "1" && json[i].checked == 1) {
                                    str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                        + "已验货"
                                        + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                                } else if (checked == "1" && json[i].checked == 0) {
                                    str += '<p id=' + "status" + json[i].odid + '>状态:<font color="red">'
                                        + "未验货"
                                        + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                                }
                                /*if (json[i].img != null || json[i].img != undefined || json[i].img != '') {
                                    str += '<p>图片: <img width = "150" height="150" src="'+img+'"/></p>';
                                } else{
                                    str += '<p>图片: <img width = "150" height="150" src="'+json[i].img+'"/></p>';
                                }*/
                                str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                                source1688_img
                                str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                            } else if (json[i].purchase_state == 1) {
                                if (checked == "0") {
                                    str += '<p id=' + "status" + json[i].odid + '>状态:<font color="green">'
                                        + "已完成补货"
                                        + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                                } else if (checked == "1" && json[i].checked == 1) {
                                    str += '<p id=' + "status" + json[i].odid + '>状态:<font color="green">'
                                        + "已验货"
                                        + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                                } else if (checked == "1" && json[i].checked == 0) {
                                    str += '<p id=' + "status" + json[i].odid + '>状态:<font color="green">'
                                        + "未验货"
                                        + '</font></p><p class="strcarype">规格:' + json[i].strcar_type + '</p>';
                                }
                                /* if (json[i].img != null || json[i].img != undefined || json[i].img != '') {
                                     str += '<p>图片: <img width = "150" height="150" src="'+img+'"/></p>';
                                 } else{
                                     str += '<p>图片: <img width = "150" height="150" src="'+json[i].img+'"/></p>';
                                 }*/
                                str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                                source1688_img
                                str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                                /* if (json[i].img != null || json[i].img != undefined || json[i].img != '') {
                                     str += '<p>图片: <img width = "150" height="150" src="'+img+'"/></p>';
                                 } else{
                                     str += '<p>图片: <img width = "150" height="150" src="'+json[i].img+'"/></p>';
                                 }*/
                                str += '<div style="float:left;"><a href="' + json[i].goods_p_url + '" target="_blank">';
                                source1688_img
                                str += '<img width= "150px" height="150px" src="' + json[i].source1688_img + '"/></a></div>';
                            }
                            if (checked == "1") {
                                console.log(1);
                                queryRecord(json[i].odid);
                                str += '<p style="font-size:16px;font-weight:bold;"><h4>已验货数量:<span id="' + json[i].orderid + 'record_' + json[i].odid + '" style="font-size:30px;color:red;">'
                                    + id_qty
                                    + '</span></h4><h4>单件重量(kg):<input type="text" style="width: 70px;" id="\'+json[i].orderid+\'weight\'+json[i].odid+\'"></h4>'
                                    + '<h4>体积重量(kg):<input type="text" style="width: 70px;" id="' + json[i].orderid + 'volumeWeight' + json[i].odid + '"></h4>';
                                /*+ '<input type="button"  value="获取商品重量" onclick="getWeight(\''+json[i].orderid+'\',\''+json[i].odid+'\')">'*/
                                if (json[i].weight == undefined) {
                                    str += '<span name="save_weight">未保存过重量!</span><input type="button" value="将商品重量同步到产品库" style="margin-left:5px" onclick="saveWeight(\'' + json[i].orderid + '\',\'' + json[i].odid + '\',\'' + json[i].goods_pid + '\')">';
                                } else {
                                    str += '<span name="save_weight">已保存的重量:' + json[i].weight + '<em>Kg</em></span>'
                                        + '<br><span name="save_volume_weight">已保存的体积重量:' + json[i].volume_weight + '<em>Kg</em></span>'
                                        + '<input type="button" value="将商品重量同步到产品库" style="margin-left:5px" onclick="saveWeight(\'' + json[i].orderid + '\',\'' + json[i].odid + '\',\'' + json[i].goods_pid + '\')">';
                                }
                                str += '<br />';
                                if (json[i].syn == undefined || json[i].syn == '0') {
                                    str += '<span name="save_weight_flag">未同步到产品库!</span>';
                                } else {
                                    str += '<span name="save_weight_flag">已同步到产品库</span>';
                                }
                                /*str += '<input type="button" value="将重量同步至产品库" style="margin-left:5px" onclick="saveWeightFlag(\''+json[i].orderid+'\',\''+json[i].odid+'\',\''+json[i].goods_pid+'\')"><br />';*/
                                str += '<span id="tip_' + json[i].orderid + json[i].odid + '" style=";color:red"></span><h3>商品验货数量:<input type="hidden" id="' + json[i].orderid + '_count' + json[i].odid + '" value="' + json[i].usecount + '"><input type="text" style="width: 40px;" id="' + json[i].orderid + 'count_' + json[i].odid + '" value=""/>piece</h3></p>';
                            } else {
                                str += '<p style="font-size:16px;font-weight:bold;"><h3>客户下单数量:<span id="' + json[i].orderid + '_count' + json[i].odid + '" style="font-size:35px;">'
                                    + json[i].usecount
                                    + '</span></h3></p>';
                            }
                            str += '<div style="height: 80px;">'
                                + '<button style="font-size: 20px;" id="snap" onclick="btnCap(\'' + json[i].orderid + '\',\'' + json[i].odid + '\')">拍摄</button><br />'
                                + '<canvas onclick="AutoResizeImage(this)"  id="canvas_' + json[i].orderid + '_' + json[i].odid + '"></canvas>'
                                + '</div>';
                            str += '</div>';
                            str += '<div style="clear:both;"></div>';
                            str += '<div><span>入库备注:</span><textarea type="text" name="warehouseRemark' + i + '" style="height :30px;" /></div><div style="clear:both;">';
                            if (checked == "1") {
                                str += '<p><input type="checkbox" id="goods_state" style="color: red" name="cha" value="1">商品质量差(质量差的商品请标记)</p>';
                            }
                            str += '<div style="display:none"><input type="checkbox" id="goods_state" style="color: red" name="cha" value="1">商品质量差(质量差的商品请标记)</div>';
                            //判断商品是否已经存放
                            if (checked == "0") {
                                str += '<button style="height: 30px;width:80px;" onclick="updategoodstatus(this,\''
                                    + json[i].goods_pid
                                    + '\',\''
                                    + json[i].orderid
                                    + '\',\''
                                    + json[i].goodsid
                                    + '\',\''
                                    + json[i].taobao_itemid
                                    + '\',\''
                                    + json[i].itemprice
                                    + '\',\''
                                    + expresstrackid
                                    + '\',\''
                                    + json[i].strcar_type.replace("'", "")
                                    + '\',\''
                                    + json[i].userid
                                    + '\',\''
                                    + json[i].goods_p_price
                                    + '\',\''
                                    + json[i].position
                                    + '\',\''
                                    + json[i].odid
                                    + '\',\''
                                    + json[i].taobaoId
                                    + '\',\''
                                    + json[i].goods_url
                                    + '\',1,' + i + ',0)">到货</button>';
                                str += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button style="height: 30px;width:80px;" onclick="updatecanceltatus(this,\''
                                    + json[i].orderid
                                    + '\',\''
                                    + json[i].odid
                                    + '\',\''
                                    + json[i].goodsid
                                    + '\',' + i + ')">入库取消</button>';
                            } else {
                                str += '<button style="height: 30px;width:80px;" onclick="updateCheckStatus(this,\''
                                    + json[i].orderid
                                    + '\',\''
                                    + json[i].goodsid
                                    + '\',\''
                                    + json[i].taobao_itemid
                                    + '\',\''
                                    + json[i].itemprice
                                    + '\',\''
                                    + expresstrackid
                                    + '\',\''
                                    + json[i].strcar_type.replace("'", "")
                                    + '\',\''
                                    + json[i].userid
                                    + '\',\''
                                    + json[i].goods_p_price
                                    + '\',\''
                                    + json[i].position
                                    + '\',\''
                                    + json[i].odid
                                    + '\',\''
                                    + json[i].isDropshipOrder
                                    + '\',\''
                                    + json[i].goods_url
                                    + '\',1,' + i + ',0,' + json[i].dp_num + ',' + json[i].dp_total
                                    + ',\'' + json[i].dp_city + '\',\'' + json[i].dp_province + '\',\'' + json[i].dp_country + '\')">验货无误</button>';
                                str += '<button style="height: 30px;width:80px;" name = "arrival' + i + '" onclick="updatecancelChecktatus(this,\''
                                    + json[i].orderid
                                    + '\',\''
                                    + json[i].odid
                                    + '\',\''
                                    + json[i].isDropshipOrder
                                    + '\',\''
                                    + json[i].goodsid
                                    + '\',' + i + ',0)">验货取消</button>';
                                str += '<button style="height: 30px;width:80px;" id="' + json[i].orderid + '_relabel_' + json[i].odid + '" onclick="relabel(\'' + json[i].orderid + '\',\'' + json[i].odid + '\',\'' + json[i].taobao_itemid + '\',\'' + json[i].strcar_type.replace("'", "") + '\',\'' + json[i].userid + '\',\'' + json[i].goods_p_price + '\',\'' + json[i].goods_url.replace("\'", "") + '\')">重打标签</button>';
                            }

                            str += '</div><div style="height:56px;font-size:10px;">备注：'
                            if (json[i].remark != 'null' && json[i].remark != null) {
                                str += json[i].remark.replace(/Microsoft/g, "</br>")
                            }
                            str += '</div>'
                            str += '</div></td></tr></table>';
                            preorderid = json[i].orderid;
                        }
                        $("#replenish").html('');
                        $("#replenish").html(str + barcode_info);
                        $("#replenish").css("color", "black");
                    }
                });
        }
    }
    document.getElementById('operatediv').style.display = 'none';
    //发送消息给采购
    if (flag == "1") {
        var tbOrderId = $("#shipno").val();
        $.post("/cbtconsole/InsertMessageForServlet",
            {tbOrderId: tbOrderId, remarkUserId: remarkUserId, buyUrl: buyUrl},
            function (res) {
                if (res != null) {
                    var msg = "";
                    var data = res.split(",");
                    status = data[0];
                    if (data[1].indexOf("@") > -1) {
                        var info = data[1];
                        if (info.indexOf("&") > -1) {
                            var infos = info.split("&");
                            for (var j = 0; j < infos.length; j++) {
                                msg += "已入库销售订单号[" + infos[0].split("@")[0] + "]，库位[" + infos[1].split("@")[1] + "],";
                            }
                        } else {
                            msg += "已入库销售订单号[" + info.split("@")[0] + "]，库位[" + info.split("@")[1] + "]";
                        }
                    }
                    if (status > 0) {
                        $("#insertMessage").html('已通知采购[' + remarkUserId + '],等待处理,' + msg);
                    } else {
                        $("#insertMessage").html('通知采购[' + remarkUserId + ']失败');
                    }
                }
            });
    }
    if (flag_storage == "0") {
        //订单扫描入库没有商品匹配
        tbOrderId = $("#tborderid").val();
        expresstrackid = $("#shipno").val();
        $.ajax({
            type: 'post',
            dataType: "json",
            async: true,
            url: '/cbtconsole/warehouse/insertStorageProblemOrder',
            data: {tbOrderId: tbOrderId, remarkUserId: remarkUserId, expresstrackid: expresstrackid},
            success: function (data) {
                console.log(data);
            },
            error: function (msg) {
                console.log(msg);
            }
        });
    }
}

//在本页面弹出采购供应商打分DIV
function openSupplierDiv(shop_id) {
    var rfddd = document.getElementById("supplierDiv");
    rfddd.style.display = "block";
    document.getElementById('su_shop_id').innerHTML = shop_id;
}

//关闭采购供应商打分DIV
function FncloseSupplierDiv() {
    var rfddd = document.getElementById("supplierDiv");
    rfddd.style.display = "none";
    document.getElementById('su_shop_id').innerHTML = "";
//	 $("#service").val("0");
    $("#quality").val("0");
    $("#su_data").val("");
    $("input[name=protocol]").attr("checked", false);
}

//提交采购供应商打分数据
function saveSupplier() {
    var shop_id = document.getElementById("su_shop_id").innerHTML;
    if (shop_id == null || shop_id == "" || shop_id == "0000") {
        alert("店铺ID不符合打分规则");
        return;
    }
//	var service=$("#service").val();
    var quality = $("#quality").val();
    var su_data = $("#su_data").val();
    var protocol = $('input[name="protocol"]:checked').val();
    $.ajax({
        type: "POST",//方法类型
        dataType: 'json',
        url: '/cbtconsole/supplierscoring/saveproductscord',
        data: {qualityAvg: quality, rerundays: su_data, shopId: shop_id, inven: protocol},
        dataType: "json",
        success: function (data) {
            if (data.flag == 'success') {
                alert("采购供应商打分成功");
                FncloseSupplierDiv();
            } else {
                alert("采购供应商打分失败");
            }
        }
    });
}

//在本页面弹出采样商品打分DIV
function openSupplierGoodsDiv(shop_id, goods_pid) {
    var rfddd = document.getElementById("supplierGoodsDiv");
    rfddd.style.display = "block";
    document.getElementById('su_goods_id').innerHTML = goods_pid;
    document.getElementById('su_goods_p_id').innerHTML = shop_id;
//	 $("#g_service").val("0");
    $("#g_quality").val("0");
    $("#su_g_remark").val("");
}

//关闭采样商品打分DIV
function FncloseSupplierGoodsDiv() {
    var rfddd = document.getElementById("supplierGoodsDiv");
    rfddd.style.display = "none";
    document.getElementById('su_goods_id').innerHTML = "";
    document.getElementById('su_goods_p_id').innerHTML = "";
    $("#g_service option[value='0']").attr("selected", "selected");
    $("#g_quality option[value='0']").attr("selected", "selected");
    $("#su_g_remark").val("");
}

//提交采样商品打分数据
function saveGoodsSupplier() {
    var shop_id = document.getElementById("su_goods_p_id").innerHTML;
    var goods_pid = document.getElementById("su_goods_id").innerHTML;
//	var service=$("#g_service").val();
    var quality = $("#g_quality").val();
    var remark = $("#su_g_remark").val();
    $.ajax({
        type: "POST",//方法类型
        dataType: 'json',
        url: '/cbtconsole/supplierscoring/saveproductscord',
        data: {quality: quality, remarks: remark, shopId: shop_id, goodsPid: goods_pid},
        dataType: "json",
        success: function (data) {
            if (data.flag == 'success') {
                alert("采样商品打分成功");
                FncloseSupplierGoodsDiv();
            } else {
                alert("采样商品打分失败");
            }
        }
    });
}

// 获取验货商品重量
function getWeight(orderid, odid) {
    $.ajax({
        type: "POST",//方法类型
        dataType: 'json',
        url: '/cbtconsole/warehouse/getWeight',
        data: {orderid: orderid, odid: odid},
        dataType: "json",
        success: function (data) {
            if (data != null && data != "") {
                $("#" + orderid + "weight" + odid + "").val(data);
            }
        }
    });
}

//保存验货商品重量
function saveWeight(orderid, odid, pid) {
    var weight = $("#" + orderid + "weight" + odid + "").val();
    var volumeWeight = $("#" + orderid + "volumeWeight" + odid + "").val();
    if (!volumeWeight) {
        volumeWeight = 0;
    }
    //数据校验
    if (weight == undefined || weight == '') {
        document.getElementById("tip_" + orderid + odid).innerHTML = "未录入单件商品重量!";
        return;
    }
    var reg = new RegExp("^\\d+([.]{1}\\d+)?$");//重量录入值校验正则
    if (!reg.test(weight) || weight <= 0) {
        document.getElementById("tip_" + orderid + odid).innerHTML = "录入单件商品重量值不正确!";
        return;
    }
    //更新
    $.ajax({
        type: "POST",//方法类型
        dataType: 'json',
        url: '/cbtconsole/warehouse/saveWeight',
        data: {orderid: orderid, odid: odid, weight: weight, pid: pid, volumeWeight: volumeWeight},
        dataType: "json",
        success: function (data) {
            if (Number(data) == 1) {
                document.getElementById("tip_" + orderid + odid).innerHTML = "保存商品重量成功";
                $("#tip_" + orderid + odid).parent().parent().find("span[name=save_weight]").html("已保存的重量:" + weight + "<em>Kg</em>");
                $("#tip_" + orderid + odid).parent().parent().find("span[name=save_volume_weight]").html("已保存的体积重量:" + volumeWeight + "<em>Kg</em>")
                $("#tip_" + orderid + odid).parent().parent().find("span[name=save_weight_flag]").html("未同步到产品库");
            } else if (Number(data) == 1) {
                document.getElementById("tip_" + orderid + odid).innerHTML = "保存商品重量失败";
            } else if (Number(data) == 2) {
                document.getElementById("tip_" + orderid + odid).innerHTML = "保存商品重量的数据问题!";
            }
//            saveWeightFlag(orderid, odid, pid);//将修改商品重量 和 将重量同步到产品库 按钮合并（这里改动的页面中的）
        }
    });
}

//将重量同步至产品库
function saveWeightFlag(orderid, odid, pid) {
    //网页中获取之前保存记录
    var his_weight = $("#tip_" + orderid + odid).parent().parent().find("span[name=save_weight]").html();
    var volume_weight = $("#tip_" + orderid + odid).parent().parent().find("span[name=save_volume_weight]").html();
    //数据校验
    if (his_weight.indexOf("已保存") == -1) {
        document.getElementById("tip_" + orderid + odid).innerHTML = "未保存单件商品重量,请先保存重量!";
        return;
    }
    //更新
    $.ajax({
        type: "POST",//方法类型
        dataType: 'json',
        url: '/cbtconsole/warehouse/saveWeightFlag',
        data: {pid: pid, odId: odid},
        success: function (data) {
            //result 0-处理异常;2-pid数据问题;1-同步到产品库成功;3-未找到重量数据;4-已经同步到产品库过;
            if (Number(data) == 1) {
                document.getElementById("tip_" + orderid + odid).innerHTML = "同步到产品库成功";
                $("#tip_" + orderid + odid).parent().parent().find("span[name=save_weight_flag]").html("已同步到产品库")
            } else if (Number(data) == 4) {
                document.getElementById("tip_" + orderid + odid).innerHTML = "已经同步到产品库过";
            } else if (Number(data) == 3) {
                document.getElementById("tip_" + orderid + odid).innerHTML = "未找到已保存的单件商品重量,请先保存";
            } else {
                document.getElementById("tip_" + orderid + odid).innerHTML = "内部异常";
            }
        }
    });
}

function openQualityInspection(orderid, goodsid, catid) {
    //运动服饰,男装,女装
    if (catid != null && (catid == "311" || catid == "10166" || catid == "10165" || catid == "127380009")) {
        var rfddd = document.getElementById("div_clothing");
        rfddd.style.display = "block";
        document.getElementById('clothing_orderid').innerHTML = orderid;
        document.getElementById('clothing_goodsid').innerHTML = goodsid;
        $("#c_catid").val(catid);
    } else if (catid != null && catid == "54") {
        //首饰
        var rfddd = document.getElementById("ss_div");
        rfddd.style.display = "block";
        document.getElementById('ss_orderid').innerHTML = orderid;
        document.getElementById('ss_goodsid').innerHTML = goodsid;
        $("#ss_catid").val(catid);
    } else if (catid != null && (catid == "6" || catid == "7" || catid == "57" || catid == "58" || catid == "509" || catid == "10208")) {
        //电子
        var rfddd = document.getElementById("dz_div");
        rfddd.style.display = "block";
        document.getElementById('dd_orderid').innerHTML = orderid;
        document.getElementById('dd_goodsid').innerHTML = goodsid;
        $("#dd_catid").val(catid);
    }
}

function clearradio(name) {
    var x = document.getElementsByName(name);
    for (var i = 0; i < x.length; i++) {
        if (x[i].checked == true) {
            x[i].checked = false;
        }
    }
}

function resetClothingDiv(type) {
    clearradio("y_wg");
    clearradio("y_bmxj");
    clearradio("y_wgxj");
    clearradio("y_ll");
    clearradio("y_xt");
    clearradio("y_zg");
    clearradio("y_qw");
    clearradio("y_bz");
    $("#jks_value").val("");
    $("#jk_remark").val("");
    $("#xwb_value").val("");
    $("#xw_remark").val("");
    $("#yww_value").val("");
    $("#yw_remark").val("");
    $("#twh_value").val("");
    $("#tw_remark").val("");
    $("#xcs_value").val("");
    $("#xc_remark").val("");
    $("#yzl_value").val("");
    $("#yc_remark").val("");
    if (type == "1") {
        $("#c_catid").val("");
        document.getElementById('clothing_orderid').innerHTML = "";
        document.getElementById('clothing_goodsid').innerHTML = "";
    }
}

function saveClothingData() {
    var y_wg = checks("y_wg");
    var y_bmxj = checks("y_bmxj");
    var y_wgxj = checks("y_wgxj");
    var y_ll = checks("y_ll");
    var y_xt = checks("y_xt");
    var y_zg = checks("y_zg");
    var y_qw = checks("y_qw");
    var y_bz = checks("y_bz");
    var jks_value = $("#jks_value").val();
    var jk_remark = $("#jk_remark").val();
    var xwb_value = $("#xwb_value").val();
    var xw_remark = $("#xw_remark").val();
    var yww_value = $("#yww_value").val();
    var yw_remark = $("#yw_remark").val();
    var twh_value = $("#twh_value").val();
    var tw_remark = $("#tw_remark").val();
    var xcs_value = $("#xcs_value").val();
    var xc_remark = $("#xc_remark").val();
    var yzl_value = $("#yzl_value").val();
    var yc_remark = $("#yc_remark").val();
    var orderid = document.getElementById("clothing_orderid").innerHTML;
    var goodsid = document.getElementById("clothing_goodsid").innerHTML;
    var catid = $("#c_catid").val();
    $.ajax({
        type: "POST",//方法类型
        dataType: 'json',
        url: '/cbtconsole/warehouse/saveClothingData',
        data: {
            orderid: orderid,
            goodsid: goodsid
            ,
            catid: catid,
            y_wg: y_wg,
            y_bmxj: y_bmxj,
            y_wgxj: y_wgxj,
            y_ll: y_ll,
            y_xt: y_xt,
            y_zg: y_zg,
            y_qw: y_qw,
            y_bz: y_bz,
            jks_value: jks_value,
            jk_remark: jk_remark,
            xwb_value: xwb_value,
            xw_remark: xw_remark,
            yww_value: yww_value,
            yw_remark: yw_remark,
            twh_value: twh_value,
            tw_remark: tw_remark,
            xcs_value: xcs_value,
            xc_remark: xc_remark,
            yzl_value: yzl_value,
            yc_remark: yc_remark
        },
        dataType: "json",
        success: function (data) {
            if (Number(data) > 0) {
                alert("保存服装质检结果成功");
                closeClothingDiv();
            } else {
                alert("保存服装质检结果失败");
            }
        }
    });
}

//保存首饰质检结果
function saveJewelryData() {
    var orderid = document.getElementById("ss_orderid").innerHTML;
    var goodsid = document.getElementById("ss_goodsid").innerHTML;
    var catid = $("#ss_catid").val();
    var s_wz = checks("s_wz");
    var s_ks = checks("s_ks");
    var s_ys = checks("s_ys");
    var s_bm = checks("s_bm");
    var s_wgxj = checks("s_wgxj");
    var s_lk = checks("s_lk");
    var s_ds = checks("s_ds");
    var s_cz = checks("s_cz");
    var s_bz = checks("s_bz");
    $.ajax({
        type: "POST",//方法类型
        dataType: 'json',
        url: '/cbtconsole/warehouse/saveJewelryData',
        data: {
            orderid: orderid,
            goodsid: goodsid,
            catid: catid,
            s_wz: s_wz,
            s_ks: s_ks,
            s_ys: s_ys,
            s_bm: s_bm,
            s_wgxj: s_wgxj,
            s_lk: s_lk,
            s_ds: s_ds,
            s_cz: s_cz,
            s_bz: s_bz
        },
        dataType: "json",
        success: function (data) {
            if (Number(data) > 0) {
                alert("保存首饰质检结果成功");
                closeClothingDivss();
            } else {
                alert("保存首饰质检结果失败");
            }
        }
    });
}

//保存电子质检结果
function saveElectronicData() {
    var orderid = document.getElementById("dd_orderid").innerHTML;
    var goodsid = document.getElementById("dd_goodsid").innerHTML;
    var catid = $("#dd_catid").val();
    var d_wg = checks("d_wg");
    var d_ks = checks("d_ks");
    var d_ys = checks("d_ys");
    var d_wz = checks("d_wz");
    var d_wg1 = checks("d_wg1");
    var d_wg2 = checks("d_wg2");
    var d_ds = checks("d_ds");
    var d_nc = checks("d_nc");
    var d_rl = checks("d_rl");
    var d_pm = checks("d_pm");
    var d_fbv = checks("d_fbv");
    var d_xs = checks("d_xs");
    var d_ct = checks("d_ct");
    var d_sms = checks("d_sms");
    var d_bz = checks("d_bz");
    $.ajax({
        type: "POST",//方法类型
        dataType: 'json',
        url: '/cbtconsole/warehouse/saveElectronicData',
        data: {
            orderid: orderid,
            goodsid: goodsid,
            catid: catid,
            d_wg: d_wg,
            d_ks: d_ks,
            d_ys: d_ys,
            d_wz: d_wz,
            d_wg1: d_wg1,
            d_wg2: d_wg2,
            d_ds: d_ds,
            d_nc: d_nc,
            d_rl: d_rl,
            d_pm: d_pm,
            d_fbv: d_fbv,
            d_xs: d_xs,
            d_ct: d_ct,
            d_sms: d_sms,
            d_bz: d_bz
        },
        dataType: "json",
        success: function (data) {
            if (Number(data) > 0) {
                alert("保存电子质检结果成功");
                closeClothingDivdd();
            } else {
                alert("保存电子质检结果失败");
            }
        }
    });
}

//清空电子质检表单数据
function resetElectronicData(type) {
    if (type == "1") {
        document.getElementById('dd_orderid').innerHTML = "";
        document.getElementById('dd_goodsid').innerHTML = "";
        $("#dd_catid").val("");
    }
    clearradio("d_wg");
    clearradio("d_ks");
    clearradio("d_ys");
    clearradio("d_wz");
    clearradio("d_wg1");
    clearradio("d_wg2");
    clearradio("d_ds");
    clearradio("d_nc");
    clearradio("d_rl");
    clearradio("d_pm");
    clearradio("d_fbv");
    clearradio("d_xs");
    clearradio("d_ct");
    clearradio("d_sms");
    clearradio("d_bz");
}


function closeClothingDiv(type) {
    var rfddd = document.getElementById("div_clothing");
    rfddd.style.display = "none";
    document.getElementById('clothing_orderid').innerHTML = "";
    document.getElementById('clothing_goodsid').innerHTML = "";
    resetClothingDiv('1');
}

//清空首饰质检数据
function resetJewelryData(type) {
    if (type == "1") {
        document.getElementById('ss_orderid').innerHTML = "";
        document.getElementById('ss_goodsid').innerHTML = "";
        $("#ss_catid").val("");
    }
    clearradio("s_wz");
    clearradio("s_ks");
    clearradio("s_ys");
    clearradio("s_bm");
    clearradio("s_wgxj");
    clearradio("s_lk");
    clearradio("s_ds");
    clearradio("s_cz");
    clearradio("s_bz");
}

//关闭首饰质检窗口
function closeClothingDivss() {
    var rfddd = document.getElementById("ss_div");
    rfddd.style.display = "none";
    resetJewelryData('1');
}

function closeClothingDivdd() {
    resetElectronicData('1');
    var rfddd = document.getElementById("dz_div");
    rfddd.style.display = "none";
}

function checks(name) {
    var values = "0";
    var radios = document.getElementsByName(name);
    for (var i = 0; i < radios.length; i++) {
        if (radios[i].checked == true) {
            values = radios[i].value;
        }
    }
    return values;
}

//拍摄照片放大
function BigImg(img) {
    htm_ = "<img width='400px' height='400px' src=" + img + ">";
    $("#big_img").append(htm_);
    $("#big_img").css("display", "block");
}

function closeBigImg() {
    $("#big_img").css("display", "none");
    $('#big_img').empty();
}

//双击删除拍摄照片
function delPics(isOk, orderid, odid, picPath, localPath) {
    $.ajax({
        url: "/cbtconsole/WebsiteServlet?action=delPics&className=ExpressTrackServlet",
        type: "post",
        data: {
            "orderid": orderid,
            odid: odid,
            picPath: picPath
        },
        async: false,
        success: function (data) {
            if (data > 0) {
                $(isOk).remove();
            }
        },
        error: function (e) {
            alert("验货图片删除失败");
        }
    });
}

function chooseOrderAll(orderNo) {
    curr_order_no = orderNo;
    for (var orNo in orderJson) {
        if (orNo == orderNo) {
            $(".chb_" + orNo).prop("checked", true);
        } else {
            $(".chb_" + orNo).prop("checked", false);
        }
    }
}


function noChooseBtn() {
    $(".choose_chk").prop("checked", false);
    curr_order_no = "";
}