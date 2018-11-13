//只能输入数字
function fnNumberInput(v){
	var event = event || window.event || arguments.callee.caller.arguments[0];
	if(!(event.keyCode==46)&&!(event.keyCode==8)&&!(event.keyCode==37)&&!(event.keyCode==39)){
	    if(!((event.keyCode>=48&&event.keyCode<=57)||(event.keyCode>=96&&event.keyCode<=105))||event.shiftKey||event.keyCode==229){
	    	 if (event.preventDefault) {
	    		   //Firefox、Chrome有效
	    		   event.preventDefault();
	    		  } else {
	    		      //IE等效preventDefault
	    		     event.returnValue = false;
	    		  }
	    }}
}

function fnSuccess(msg){
	$.dialog.alert("Message",msg);
}
//价格输入框
function check(event) {
	var e = window.event || event;
	var target = e.srcElement || e.target;
	var k = e.keyCode;
	if (isFunKey(k)) {
		return true;
	}
	var c = getChar(k);
	if (target.value.length == '' && (c == '-' || c == '+')) {
		return true;
	}
	if (isNaN(target.value + getChar(k))) {
		return false;
	}
	return true;
}

function check2(thi){
	var e = window.event || event;
	var target = e.srcElement || e.target;
	var k = e.keyCode;
	if(k == 9){
		$(thi).next().focus();
	}
}
function isFunKey(code) {
	var funKeys = [ 8, 35, 36, 37, 39, 46 ];
	for (var i = 112; i <= 123; i++) {
		funKeys.push(i);
	}
	for (var i = 0; i < funKeys.length; i++) {
		if (funKeys[i] == code) {
			return true;
		}
	}
	return false;
}
function getChar(k) {
	if (k >= 48 && k <= 57) {
		return String.fromCharCode(k);
	}
	if (k >= 96 && k <= 105) {
		return String.fromCharCode(k - 48);
	}
	if (k == 110 || k == 190 || k == 188) {
		return ".";
	}
	if (k == 109 || k == 189) {
		return "-";
	}
	if (k == 107 || k == 187) {
		return "+";
	}
	return "#";
}
//去前后空格
String.prototype.trim=function() {

    return this.replace(/(^\s*)|(\s*$)/g,'');
}

function  resea(obj){
	alert(obj);
}
function fnSlz(action){
	var url1 = window.location+"";
	$.post("/cbtconsole/cbt/lz/slz",{action:action,pruduct_url:url1,url:""},
	        function(result){});
}