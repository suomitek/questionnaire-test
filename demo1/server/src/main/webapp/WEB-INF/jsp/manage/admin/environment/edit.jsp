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
<link href="${basePath }/static/HUI/lib/video/video-js.css" rel="stylesheet">
<script src="${basePath }/static/HUI/lib/video/video.js"></script>
<script src="${basePath }/static/HUI/lib/video/videojs-contrib-hls.js"></script>
<title>修改系统设置</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="submintInfo" method="post" >
		<div class="row cl">
			<label class="form-label col-sm-2" for="name" style="text-align: right"><span class="c-red">*</span>系统名称：</label>
			<div class="formControls col-sm-8">
				<input id="name" name="name" value="${sysEnvironment.name}" class="input-text" placeholder="请输入系统名称" type="text">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" for="emailHost" style="text-align: right"><span class="c-red">*</span>邮箱主机：</label>
			<div class="formControls col-sm-8">
				<input id="emailHost" name="emailHost" value="${sysEnvironment.emailHost}" class="input-text" placeholder="请输入邮箱主机" type="text">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" for="emailSender" style="text-align: right"><span class="c-red">*</span>邮箱账户：</label>
			<div class="formControls col-sm-8">
				<input id="emailSender" name="emailSender" value="${sysEnvironment.emailSender}" class="input-text" placeholder="请输入邮箱账户" type="text">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" for="emailPassword" style="text-align: right"><span class="c-red">*</span>邮箱密码：</label>
			<div class="formControls col-sm-8">
				<input id="emailPassword" name="emailPassword" value="${sysEnvironment.emailPassword}" class="input-text" placeholder="请输入邮箱密码" type="text">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" for="emailPassword" style="text-align: right"><span class="c-red">*</span>修改系统介绍：</label>
			<div class="formControls col-sm-8">
				<span class="btn-upload">
				  <a href="javascript:void(0);" class="btn btn-primary btn-upload  radius size-MINI"><i class="Hui-iconfont">&#xe642;</i> 浏览文件</a>
				  <input type="file" id="Filedata" class="input-file" name="Filedata">
				</span>
				<input id="introduceVideoUrl" type="hidden" value="${sysEnvironment.introduceVideoUrl}"/>
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-2 col-sm-2 col-xs-offset-3 col-sm-offset-3">
				<input class="btn btn-primary radius size-MINI" type="submit" id="submit" value="&nbsp;&nbsp;修改&nbsp;&nbsp;">
			</div>
		</div>
		<div class="row cl col-5 col-offset-2">
			<video id="my_video_1" style="width: 270px;height: 480px;" class="video-js vjs-default-skin" controls preload="auto"
				   data-setup='{}'>
				<source src="${basePath }${sysEnvironment.introduceVideoUrl}" type="application/x-mpegURL">
			</video>
		</div>
	</form>
</article>
<%@include file="../footer.jsp"%>
<script src="http://libs.baidu.com/jquery/1.7.1/jquery.min.js"></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/jquery.validate.js"></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/myValidate.js"></script>
<script type="text/javascript" src="${basePath }/static/ajaxfileupload/ajaxfileupload-me-2.js" ></script>
<script type="text/javascript">
    $('input[id=Filedata]').change(function() {
        uploadify();
    });
    function uploadify(){
        var file=$("#Filedata").val();
        if(file==""||file==null){
            layer.msg('请选择视频',{icon: 5,time:1000});
            return;
        }
        $.ajaxFileUpload
        (
            {
                url:'${basePath }/basics/admin/video/upload',//用于文件上传的服务器端请求地址
                secureuri:false,//一般设置为false
                data: { "modelName": "video"},//放到/upload/video/video
                fileElementId:'Filedata',//文件上传空间的id属性
                dataType: 'json',//返回值类型 一般设置为json
                timeout: 5000,
                success: function (data, status)  //服务器成功响应处理函数
                {
                    if(data.resultcode=="200") {
                        $('#introduceVideoUrl').val(data.videoUrl)
                    }else{
                        layer.msg(data.resultmsg,{icon: 5,time:1000});
                    }
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    console.log("上传失败")
                }
            }
        )
        $('input[id=Filedata]').change(function() {
            uploadify();
        });
    }
</script>
<script type="text/javascript">
	jQuery("#submintInfo").validate({
		rules: {
			name: {
				required: true,
				minlength: 2,
				maxlength:10
			},
            emailHost: {
				required: true
			},
            emailSender: {
				required: true,
                email: true,
			},
            emailPassword: {
				required: true
			}
		},
		submitHandler: function(form) {
			$.ajax( {
				url:'${basePath}/manage/admin/environment/editSave',// 跳转到 action
				type:'post',
				cache:false,
				data:{"name":$('#name').val(),"emailHost":$('#emailHost').val(),
                    "emailSender":$('#emailSender').val(),"emailPassword":$('#emailPassword').val(),
					"introduceVideoUrl":$("#introduceVideoUrl").val()
				},
				dataType:'json',
				success:function(data) {
					if(data.resultCode =="200" ){
						layer.msg('修改成功',{icon:1,time:1000});
						var index = parent.layer.getFrameIndex(window.name);
						parent.parentRefresh();
						parent.layer.close(index);
					}else{
						layer.msg(data.resultMsg,{icon:5,time:1000});
					}
				},
				error : function(data) {
					layer.msg(data.resultMsg,{icon:5,time:1000});
				}
			});
		}
	});
</script>
</body>

</html>
