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
<title>添加前台URL</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="submintInfo" method="post" >
		<div class="row cl">
			<label class="form-label col-sm-2" for="linkName" style="text-align: right"><span class="c-red">*</span>URL名称：</label>
			<div class="formControls col-sm-8">
				<input id="linkName" name="linkName" class="input-text toJson" placeholder="URL名称" type="text">
				<input id="id"  class="toJson" type="hidden" value="0">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" for="linkCode" style="text-align: right"><span class="c-red">*</span>URL编码：</label>
			<div class="formControls col-sm-8">
				<input id="linkCode" name="linkCode" class="input-text toJson" placeholder="URL编码" type="text">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" for="linkUrl" style="text-align: right"><span class="c-red">*</span>URL地址：</label>
			<div class="formControls col-sm-8">
				<input id="linkUrl" name="linkUrl" class="input-text toJson" placeholder="URL地址" type="text">
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-2 col-sm-2 col-xs-offset-3 col-sm-offset-3">
				<input class="btn btn-primary radius" type="submit" id="submit" value="&nbsp;&nbsp;添加&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>
<%@include file="../../../basics/footer.jsp"%>
<%@include file="../../../basics/myValidate.jsp"%>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/jquery.validate.js"></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/myValidate.js"></script>
<script src="${basePath }/static/js/inputToJson.js"></script>
<script type="text/javascript">
        jQuery("#submintInfo").validate({
            rules: {
                linkName: {
                    required: true,
                    minlength: 2,
                    maxlength:10,
                    checkAttribute:"linkName"
                },linkCode:{
                    required: true,
                    checkAttribute:"linkCode"
                },linkUrl:{
                    required: true,
                    checkAttribute:"linkUrl"
                }
            },
            submitHandler: function(form) {
                $.ajax( {
                    url:'${basePath}/${url}/addSave',// 跳转到 action
                    type:'post',
                    cache:false,
                    data:getJson(),
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

</script>
</body>

</html>
