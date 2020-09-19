<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="stylesheet" type="text/css" href="${basePath }/static/HUI/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="${basePath }/static/HUI/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="${basePath }/static/HUI/lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="${basePath }/static/HUI/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="${basePath }/static/HUI/h-ui.admin/css/style.css" />
<title>添加菜单</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="submintInfo" method="post" >
		<div class="row cl">
			<label class="form-label col-sm-2 col-xs-3" for="resName" style="text-align: right"><span class="c-red">*</span>菜单名称：</label>
			<div class="formControls col-sm-8 col-xs-8">
				<input id="resName" name="resName" class="input-text" placeholder="请输入菜单名称" type="text">
				<input id="parentId" name="parentId" type="hidden" value="${pResource.id}">
				<input id="resType" name="resType" type="hidden" value="${pResource.resType+1}">
				<input id="id" name="id" type="hidden" value="0">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2 col-xs-3" for="resCode" style="text-align: right"><span class="c-red">*</span>菜单编码：</label>
			<div class="formControls col-sm-8 col-xs-8">
				<input id="resCode" name="resCode" class="input-text" placeholder="请输入菜单编码" type="text">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2 col-xs-3" style="text-align: right"><span class="c-red">*</span>菜单路径：</label>
			<div class="formControls col-sm-8 col-xs-8">
				<input id="resUrl" name="resUrl" class="input-text" placeholder="请输入菜单路径" type="text">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2 col-xs-3" for="orderNum" style="text-align: right"><span class="c-red">*</span>菜单排序号：</label>
			<div class="formControls col-sm-8 col-xs-8">
				<input id="orderNum" name="orderNum" class="input-text" placeholder="请输入菜单排序号" type="text">
			</div>
		</div>
		<div class="row cl <c:if test="${pResource.resName == '无父菜单'}">hide</c:if>">
			<label class="form-label col-sm-2 col-xs-3" for="btnFlag" style="text-align: right"><span class="c-red">*</span>资源类别：</label>
			<div class="formControls col-sm-8 col-xs-8">
				<select name="btnFlag" id="btnFlag" class="select input-text">
					<option value="0" selected = "selected">页面资源</option>
					<option value="1">按钮资源</option>
				</select>
			</div>
		</div>
		<div class="row cl  <c:if test="${pResource.resName == '无父菜单'}">hide</c:if>" id="apis">
			<label class="form-label col-sm-2 col-xs-3" style="text-align: right"><span class="c-red">*</span>页面API：</label>
			<div class="formControls col-sm-8 col-xs-8">
				<div class="check-box">
					<input type="checkbox" id="api1" name="apis" value="Save">
					<label for="api1">Save</label>
					<input type="checkbox" id="api2" name="apis" value="Json">
					<label for="api2">Json</label>
				</div>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2 col-xs-3"style="text-align: right"><span class="c-red">*</span>父菜单名称：</label>
			<div class="formControls col-sm-8 col-xs-8">
				<input value="${pResource.resName}" readonly="readonly" class="input-text"type="text">
			</div>
		</div>

		<div class="row cl">
			<div class="col-xs-3 col-sm-2 col-xs-3 col-xs-offset-3 col-sm-offset-3">
				<input class="btn btn-primary radius" type="submit" id="submit" value="&nbsp;&nbsp;添加&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>
<%@include file="../footer.jsp"%>
<script src="http://libs.baidu.com/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="${basePath }/static/ajaxfileupload/ajaxfileupload-me-2.js" ></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/jquery.validate.js"></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/myValidate.js"></script>
<script type="text/javascript">
    $().ready(function() {
        // 后台属性是否存在的验证
        jQuery.validator.addMethod("checkAttribute", function(value, element,message) {
        var flag = true;
        $.ajax({
            type:'post',
            url:'${basePath}/manage/admin/resource/loadCheckAttributeIsExistence',// 跳转到 action
            cache:false,
            async:false,
            data:{"checkVal":value,"attribute":message,"id":$("#id").val()},
            dataType:'json',
            success:function(data) {
                if(data.resultCode =="200" ){
                    flag = true;
                }else{
                    flag = false;
                }
            }
        });
        return flag;
    }, "已被使用");

        $("#btnFlag").change(function(){
            if ($(this).val()==1){
                $("#apis").hide();
			}else {
                $("#apis").show();
			}
        });
        jQuery("#submintInfo").validate({
            rules: {
                resName: {
                    required: true,
                    minlength: 2,
                    maxlength:10,
                    checkAttribute:"resName"
                },
                resCode: {
                    required: true,
                    minlength: 2,
                    maxlength:20,
                    checkAttribute:"resCode"
                },
                orderNum: {
                    digits: true
                }
            },
            submitHandler: function(form) {
                var apisObj=$("input[name='apis']:checked");

                var apis;
                for(var i=0;i<apisObj.length;i++){
                    if(i==0){
                        apis = $(apisObj[i]).val();
                    }else{
                        apis += ","+$(apisObj[i]).val();
                    }
                }
                $.ajax( {
                    url:'${basePath}/manage/admin/resource/addSave',// 跳转到 action
                    type:'post',
                    cache:false,
                    data:{"resCode":$('#resCode').val(),"resName":$('#resName').val(),"resUrl":$('#resUrl').val(),
						"parentId":$('#parentId').val(),"resType":$('#resType').val(),"id":$('#id').val(),
						"orderNum":$('#orderNum').val(),"btnFlag":$('#btnFlag').val(),"apis":apis
					},
                    dataType:'json',
                    success:function(data) {
                        if(data.resultCode =="200" ){
                            layer.msg('添加成功',{icon:1,time:1000});
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.parentRefresh();
                            parent.layer.close(index);
                        }else{
                            layer.msg(data.resultMsg,{icon:5,time:1500});
                        }
                    },
                    error : function(data) {
                        layer.msg(data.resultMsg,{icon:5,time:1500});
                    }
                });
            }
        });
	})
</script>
</body>

</html>
