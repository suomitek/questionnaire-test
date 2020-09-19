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
<title>修改验证</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="submintInfo" method="post" >
		<div class="row cl">
			<label class="form-label col-sm-2" for="name" style="text-align: right"><span class="c-red">*</span>验证名称：</label>
			<div class="formControls col-sm-8">
				<input id="name" name="name" value="${obj.name}" class="input-text toJson" placeholder="验证名称" type="text">
				<input id="id"  class="toJson" type="hidden" value="${obj.id}">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" for="orderNum" style="text-align: right"><span class="c-red">*</span>排序号：</label>
			<div class="formControls col-sm-8">
				<input id="orderNum" name="orderNum" value="${obj.orderNum}" class="input-text toJson" placeholder="排序号" type="text">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" for="reg" style="text-align: right"><span class="c-red">*</span>规则：</label>
			<div class="formControls col-sm-8">
				<input id="reg" name="reg" value="${obj.reg}" class="input-text toJson" placeholder="规则" type="text">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-2 col-sm-2" for="icon" style="text-align: right"><span class="c-red">*</span>验证图片：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input id="icon" name="icon" class="input-text toJson" placeholder="微信小程序中的图片名称" type="text" value="${obj.icon}">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-2 col-sm-2" for="msg" style="text-align: right"><span class="c-red">*</span>错误提示：</label>
			<div class="formControls col-xs-8 col-sm-8">
				<input id="msg" name="msg" class="input-text toJson" placeholder="错误提示" type="text" value="${obj.msg}">
			</div>
		</div>
		<input id="Filedata" type="file" style="display:none" name="Filedata">
		<div class="row cl">
			<div class="col-xs-2 col-sm-2 col-xs-offset-3 col-sm-offset-3">
				<input class="btn btn-primary radius" type="submit" id="submit" value="&nbsp;&nbsp;修改&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>
<%@include file="../../../basics/footer.jsp"%>
<script type="text/javascript" src="${basePath }/static/ajaxfileupload/ajaxfileupload-me-2.js" ></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/jquery.validate.js"></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/myValidate.js"></script>
<script src="${basePath }/static/js/inputToJson.js"></script>
<script type="text/javascript">
	jQuery("#submintInfo").validate({
		rules: {
			title: {
				required: true,
				minlength: 2,
				maxlength:10,
			},orderNum:{
				digits:true
			},icon:{
				required: true,
			},msg:{
				required: true,
			}
		},
		submitHandler: function(form) {
			$.ajax( {
				url:'${basePath}/${url}/editSave',// 跳转到 action
				type:'post',
				cache:false,
				data:getJson(),
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

</script>
</body>

</html>
