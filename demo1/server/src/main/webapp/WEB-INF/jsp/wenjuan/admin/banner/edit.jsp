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
<title>修改轮播</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="submintInfo" method="post" >
		<div class="row cl">
			<label class="form-label col-sm-2" for="title" style="text-align: right"><span class="c-red">*</span>轮播标题：</label>
			<div class="formControls col-sm-8">
				<input id="title" name="title" value="${obj.title}" class="input-text toJson" placeholder="轮播标题" type="text">
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
			<label class="form-label col-sm-2" for="linkUrl" style="text-align: right"><span class="c-red">*</span>链接：</label>
			<div class="formControls col-sm-8">
				<input id="linkUrl" name="linkUrl" value="${obj.linkUrl}" class="input-text toJson" placeholder="链接" type="text">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-2 col-sm-2" style="text-align: right"><span class="c-red">*</span>轮播图片：</label>
			<div class="formControls col-xs-3 col-sm-3">
				<img style="width: 90%;" src="${basePath }${obj.imagePath}" id="imagePathShow" onclick="link('#imagePathShow','#imagePath')">
				<input id="imagePath" type="hidden" value="${basePath }${obj.imagePath}" class="toJson">
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
    var thisShowId=null;
    var thisValId=null;
    function link(showId,valId){
        thisShowId=showId;
        thisValId=valId;
        $('input[id=Filedata]').click();
    }
    $('input[id=Filedata]').change(function() {
        uploadify(thisShowId,thisValId);
    });
    /*
     * 异步上传
     */
    function uploadify(showId,valId){

        var file=$("#Filedata").val();
        if(file==""||file==null){
            layer.msg('请选择图片',{icon: 5,time:1000});
            return;
        }
        $.ajaxFileUpload
        (
            {
                url:'${basePath}/basics/admin/uploadify/image/originalSingle',//用于文件上传的服务器端请求地址
                secureuri:false,//一般设置为false
                data: { "modelName": "shop","entityName":"shopBanner"},//放到/upload/shop/shopStore
                fileElementId:'Filedata',//文件上传空间的id属性
                dataType: 'json',//返回值类型 一般设置为json
                success: function (data, status)  //服务器成功响应处理函数
                {
                    if(data.resultcode=="200") {
                        $(showId).attr("src",'${basePath}'+data.imageUrl);
                        $(valId).val('${basePath}'+data.imageUrl);
                    }else{
                        layer.msg(data.resultmsg,{icon: 5,time:1000});
                    }
                },
                error: function (data, status, e)//服务器响应失败处理函数
                {
                    layer.msg("上传失败",{icon: 5,time:1000});
                }
            }
        )
        $('input[id=Filedata]').change(function() {
            uploadify(thisShowId,thisValId);
        });
    }
</script>
<script type="text/javascript">
        jQuery("#submintInfo").validate({
            rules: {
                title: {
                    required: true,
                    minlength: 2,
                    maxlength:10,
                },orderNum:{
                    digits:true
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
