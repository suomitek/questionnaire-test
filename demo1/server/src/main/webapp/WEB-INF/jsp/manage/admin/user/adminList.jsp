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
<title>未分配的管理员列表</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="submintInfo" method="post" >
		<div class="row cl">
			<label class="form-label col-sm-2" style="text-align: right"><span class="c-red">*</span>管理员：</label>
			<div class="formControls col-sm-8">
				<c:forEach items="${users}" var="user">
					<div class="radio-box">
						<input type="radio" id="radio-${user.id}" name="adminId" value="${user.id}" class="toJson">
						<label for="radio-${user.id}">${user.name}</label>
					</div>
				</c:forEach>
			</div>
		</div>
		<input id="id" type="hidden" value="${id}" class="toJson">
		<div class="row cl">
			<div class="col-xs-2 col-sm-2 col-xs-offset-3 col-sm-offset-3">
				<input class="btn btn-primary radius size-MINI" type="submit" id="submit" value="&nbsp;&nbsp;添加&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>
<%@include file="../footer.jsp"%>
<script src="http://libs.baidu.com/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="${basePath }/static/ajaxfileupload/ajaxfileupload-me-2.js" ></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/jquery.validate.js"></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/myValidate.js"></script>
<script src="${basePath }/static/js/inputToJson.js"></script>
<script type="text/javascript">
    $().ready(function() {
        jQuery("#submintInfo").validate({
            rules: {
                name: {
                    required: true
                }
            },
            submitHandler: function(form) {
                $.ajax( {
                    url:'${basePath}/bd/admin/department/changeAdminId',// 跳转到 action
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
	})
</script>
</body>

</html>
