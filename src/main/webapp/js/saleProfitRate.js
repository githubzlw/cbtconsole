$(function() {
		//设置datagrid属性
		setDatagrid();
		
//		$('#catid_tt').tree({    
//		    url:'../keywordCatidRecord/getCategorys',
//		    onClick: function(node){
//				$("#catid").val(node.id);
//				$("#catname").val(node.text);
//			}
//		});  
		
		$(document).keyup(function(event){
		  if(event.keyCode ==13){
			  var searchname = $("#searchname").val();
				
			$('#catid_tt').tree({   
				queryParams:{searchname:searchname},
			    url:'../keywordCatidRecord/getCategorys',
			    onClick: function(node){
					$("#catid").val(node.id);
					$("#catname").val(node.text);
				}
			});
		  }
		});
	});

	function setDatagrid() {
		$('#main-datagrid').datagrid({
			title : '利润商品管理',
			//align : 'center',
			width : "100%",
			fit : true,//自动补全 
			pageSize : 20,//默认选择的分页是每页20行数据
			pageList : [20, 30, 50, 100],//可以选择的分页集合
			nowrap : true,//设置为true，当数据长度超出列宽时将会自动截取
			striped : true,//设置为true将交替显示行背景。
			collapsible : true,//显示可折叠按钮
			toolbar : "#top_toolbar",//在添加 增添、删除、修改操作的按钮要用到这个
			url : "/cbtconsole/saleprofitrate/list",//url调用Action方法
			loadMsg : '数据装载中......',
			singleSelect : true,//为true时只能选择单行
			fitColumns : true,//允许表格自动缩放，以适应父容器
			pagination : true,//分页
			rownumbers : true,
			style : {
				padding : '8 8 10 8'
			},
			onLoadError : function() {
				//$.message.alert("提示信息", "获取数据信息失败","info");
				return;
			}
		});

	}
	
	function doQuery() {
		var query_keyword = $("#query_keyword").val();

		$("#main-datagrid").datagrid("load", {
			"keyword" : query_keyword
		});
	}
	
	function doReset() {
		$("#query_form")[0].reset();
	}
	
	function toAdd() {
		$('#keyword_add_win').window('open');
	}
	
	function doAdd() {
		$('#keyword_add').form({    
		    url:"/cbtconsole/saleprofitrate/insertInfo",    
		    onSubmit: function(){    
		    	var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
				}
				return isValid;	// 返回false终止表单提交    
		    },    
		    success:function(data){
		    	var res = JSON.parse(data);
		    	if(res.status) {
		    		$.messager.alert('message',res.message);
					$('#keyword_add_win').window('close');
			    	$('#keyword_add_win').form('clear');
			    	doQuery();
		    	}else{
		    		$.messager.alert('message',res.message);
		    	}
		    }    
		});    
		// submit the form    
		$('#keyword_add').submit();
}
	
	function toUpdate(id) {
		$.ajax({
			url : '/cbtconsole/keywordCatidRecord/get',
			method : 'post',
			data: {id : id},
			dataType:'json',
			success : function(data) {
				var res = data;
				if(res) {
					$("#update_id").val(res.id);
					$("#update_keyword").val(res.keyword);
					$("#update_catid1").val(res.catid1);
					$("#update_catid2").val(res.catid2);
					$("#update_catid3").val(res.catid3);
					
					$("#update_catid1name").val(res.catname1);
					$("#update_catid2name").val(res.catname2);
					$("#update_catid3name").val(res.catname3);
					
					if(res.keyword1) {
						var keys = res.keyword1.split(";");
						var InputsWrapper2 = $("#InputsWrapper2");
						InputsWrapper2.html("");
						x2 = 0;
						for(var i=0;i<keys.length;i++) {
							x2 ++;
							InputsWrapper2.append('<div><input class="easyui-validatebox" type="text" name="update_keyword1" value="'+keys[i]+'"/><a href="#" class="removeclass2">×</a></div>');
						}
					}
					//$("#update_keyword1").val(res.keyword1);
					
					$("input[name='update_issyn'][value='"+res.issyn+"']").prop('checked', true);//radio默认选中
					
					//$('#update_issyn').combobox('setValue', res.issyn);
					
					$('#keyword_update_win').window('open');
				}
				
			},
			error: function (msg) {
				alert(msg);
			}
		});
	}
	
	function doUpdate() {
		$('#keyword_update').form({    
		    url:"/cbtconsole/keywordCatidRecord/update",    
		    onSubmit: function(){    
		    	var isValid = $(this).form('validate');
				if (!isValid){
					$.messager.progress('close');	// 如果表单是无效的则隐藏进度条
				}
				return isValid;	// 返回false终止表单提交    
		    },    
		    success:function(data){
		    	var res = JSON.parse(data);
		    	if(res.status) {
		    		$.messager.alert('message',res.message);
					$('#keyword_update_win').window('close');
			    	$('#keyword_update_win').form('clear');
			    	doQuery();
		    	}else{
		    		$.messager.alert('message',res.message);
		    	}
		    }    
		});    
		// submit the form    
		$('#keyword_update').submit();
	}
	
	function doDelete(id) {
		$.messager.confirm('确认','您确认想要删除选中的项吗？',function(r){ 
			if (r){    
				$.ajax({
					url : '/cbtconsole/keywordCatidRecord/delete',
					method : 'post',
					data: {id : id},
					dataType:'json',
					success : function(data) {
						if(data.status) {
							$.messager.alert('message',data.message);
							doQuery();
						}else{
							$.messager.alert('message',data.message);
						}
						
					},
					error: function (msg) {
						  alert(msg);
			        }
				});
			}
		});
	}
	
	
	function issyn(id,issyn) {
		$.ajax({
			url : '/cbtconsole/keywordCatidRecord/updateSyn',
			method : 'post',
			data : {id : id,issyn : issyn},
			dataType : 'json',
			success : function(data) {
				var res = data;
				if(res.status) {
					$.messager.alert('message',res.message);
					doQuery();
				}else{
					$.messager.alert('message',res.message);
				}
			},
			error: function (msg) {
				alert(msg);
			}
		});
	}
	
	function exportTxt() {
		
		window.open('/cbtconsole/keywordCatidRecord/synonyms.txt', '_blank');
	}
	

	var x2;
	$(document).ready(function() {  
		  
		var MaxInputs       = 8; //maximum input boxes allowed  
		var InputsWrapper   = $("#InputsWrapper"); //Input boxes wrapper ID  
		var AddButton       = $("#AddMoreFileBox"); //Add button ID  
		  
		var x = InputsWrapper.length; //initlal text box count  
		var FieldCount=1; //to keep track of text box added  
		  
		$(AddButton).click(function (e)  //on add input button click  
		{  
	        if(x <= MaxInputs) //max input box allowed  
	        {  
	            FieldCount++; //text box added increment  
	            //add input box  
	            $(InputsWrapper).append('<div><input class="easyui-validatebox" type="text" name="keyword1" value=""/><a href="#" class="removeclass">×</a></div>');  
	            x++; //text box increment  
	        }  
			return false;  
		});  
		  
		$("body").on("click",".removeclass", function(e){ //user click on remove text  
	        if( x > 1 ) {  
                $(this).parent('div').remove(); //remove text box  
                x--; //decrement textbox  
	        }  
			return false;  
		})   
		
		
		var MaxInputs2       = 8; //maximum input boxes allowed  
		var InputsWrapper2   = $("#InputsWrapper2"); //Input boxes wrapper ID  
		var AddButton2       = $("#AddMoreFileBox2"); //Add button ID  
		  
		x2 = InputsWrapper2.length; //initlal text box count  
		var FieldCount2=1; //to keep track of text box added  
		  
		$(AddButton2).click(function (e)  //on add input button click  
		{  
	        if(x2 <= MaxInputs2) //max input box allowed  
	        {  
	            FieldCount2++; //text box added increment  
	            //add input box  
	            $(InputsWrapper2).append('<div><input class="easyui-validatebox" type="text" name="update_keyword1" value=""/><a href="#" class="removeclass2">×</a></div>');  
	            x2++; //text box increment  
	        }  
			return false;  
		});  
		  
		$("body").on("click",".removeclass2", function(e){ //user click on remove text  
	        if( x2 > 1 ) {  
                $(this).parent('div').remove(); //remove text box  
                x2--; //decrement textbox  
	        }  
			return false;  
		})
		  
	});  
	
	
	var lb;
	
	function category_win(a){
		lb = a;
		$('#category_win').window('open');
	}
	
	function toBlur(){
		var searchname = $("#searchname").val();
		
		$('#catid_tt').tree({   
			queryParams:{searchname:searchname},
		    url:'../keywordCatidRecord/getCategorys',
		    onClick: function(node){
				$("#catid").val(node.id);
				$("#catname").val(node.text);
			}
		});  
	}
	
	function qrCat(){
		var catid = $("#catid").val();
		var catname = $("#catname").val();
		if(catid == '') {
			$.messager.alert('message','请选择类别');
			return;
		}
		if(lb == 'catid1') {
			$("#catid1").val(catid);
			$("#catid1name").val(catname);
		}else if(lb == 'catid2') {
			$("#catid2").val(catid);
			$("#catid2name").val(catname);
		}else if(lb == 'catid3') {
			$("#catid3").val(catid);
			$("#catid3name").val(catname);
		}else if(lb == 'update_catid1') {
			$("#update_catid1").val(catid);
			$("#update_catid1name").val(catname);
		}else if(lb == 'update_catid2') {
			$("#update_catid2").val(catid);
			$("#update_catid2name").val(catname);
		}else if(lb == 'update_catid3') {
			$("#update_catid3").val(catid);
			$("#update_catid3name").val(catname);
		}
		
		$('#category_win').window('close');
		
		$("#catid").val("");
		$("#catname").val("");
	}