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
<title>修改角色</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="submintInfo" method="post" >
		<div class="row cl">
			<label class="form-label col-sm-2 col-xs-3" for="roleName" style="text-align: right"><span class="c-red">*</span>角色名称：</label>
			<div class="formControls col-sm-8 col-xs-8">
				<input id="roleName" value="${sysRole.roleName}" name="roleName" class="input-text" placeholder="请输入角色名称" type="text">
				<input id="id" type="hidden" value="${sysRole.id}">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2 col-xs-3" for="roleDesc" style="text-align: right"><span class="c-red">*</span>角色描述：</label>
			<div class="formControls col-sm-8 col-xs-8">
				<textarea id="roleDesc" name="roleDesc" class="textarea" placeholder="请输入角色描述">${sysRole.roleDesc}</textarea>
			</div>
		</div>

		<div class="row cl">
			<div class="col-xs-3 col-sm-2 col-xs-3 col-xs-offset-3 col-sm-offset-3">
				<input class="btn btn-primary radius size-MINI" type="submit" id="submit" value="&nbsp;&nbsp;修改&nbsp;&nbsp;">
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
            url:'${basePath}/manage/admin/role/loadCheckAttributeIsExistence',// 跳转到 action
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

        jQuery("#submintInfo").validate({
            rules: {
                roleName: {
                    required: true,
                    minlength: 2,
                    maxlength:10,
                    checkAttribute:"roleName"
                },
                roleDesc: {
                    required: true,
                    minlength: 2,
                    maxlength:10
                }
            },
            submitHandler: function(form) {
                var roleName = $('#roleName').val();
                var roleDesc = $('#roleDesc').val();
                $.ajax( {
                    url:'${basePath}/manage/admin/role/editSave',// 跳转到 action
                    type:'post',
                    cache:false,
                    data:{"roleName":roleName,"roleDesc":roleDesc,"id":$("#id").val()},
                    dataType:'json',
                    success:function(data) {
                        if(data.resultCode =="200" ){
                            layer.msg('修改成功',{icon:1,time:1000});
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
