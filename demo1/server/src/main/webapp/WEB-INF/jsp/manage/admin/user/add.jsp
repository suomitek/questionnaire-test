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
<title>添加用户</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="submintInfo" method="post" >
		<div class="row cl">
			<label class="form-label col-sm-2" for="name" style="text-align: right"><span class="c-red">*</span>用户姓名：</label>
			<div class="formControls col-sm-8">
				<input id="name" name="name" class="input-text" placeholder="请输入姓名" type="text">
				<input id="id" type="hidden" value="0">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" for="nickName" style="text-align: right"><span class="c-red">*</span>昵称：</label>
			<div class="formControls col-sm-8">
				<input id="nickName" name="nickName" class="input-text" placeholder="请输入昵称" type="text">
			</div>
		</div>
		<div class="row cl hide">
			<label class="form-label col-sm-2" for="nickName" style="text-align: right"><span class="c-red">*</span>类别：</label>
			<div class="formControls col-sm-8">
				<select name="userType" id="userType" class="select input-text">
					<option value="1" selected = "selected">商家管理员</option>
				</select>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" style="text-align: right" for="password"><span class="c-red">*</span>密码：</label>
			<div class="formControls col-sm-8">
				<input id="password" name="password" class="input-text" placeholder="请输入密码" type="password">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" style="text-align: right" for="confirm_password"><span class="c-red">*</span>再次密码：</label>
			<div class="formControls col-sm-8">
				<input id="confirm_password" name="confirm_password" class="input-text" placeholder="再次输入密码" type="password">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" for="telNumber" style="text-align: right"><span class="c-red">*</span>电话：</label>
			<div class="formControls col-sm-8">
				<input id="telNumber" name="telNumber" class="input-text" placeholder="电话" type="text">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" for="verifyCode" style="text-align: right"><span class="c-red">*</span>电话：</label>
			<div class="formControls col-xs-6 col-sm-6">
				<input id="verifyCode" name="verifyCode" class="input-text" placeholder="验证码" type="text">
			</div>
			<div class="col-xs-2 col-sm-2 text-r">
				<input class="btn btn-success" type="button" id="verifyCodeBtn" value="发送验证码">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-2" style="text-align: right" for="email"><span class="c-red">*</span>邮箱：</label>
			<div class="formControls col-sm-8">
				<input id="email" name="email" class="input-text" placeholder="邮箱" type="email">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-2 col-sm-2" style="text-align: right"><span class="c-red">*</span>头像：</label>
			<div class="formControls col-xs-3 col-sm-3">
				<img style="width: 40%;" src="${basePath }/static/img/temp_pic_2.jpg" id="portraitUrlShow" onclick="link('#portraitUrlShow','#portraitUrl')">
				<input id="portraitUrl" type="hidden" value="${basePath }/static/img/temp_pic_2.jpg" class="toJson">
				<input id="Filedata" type="file" style="display:none" name="Filedata">
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-2 col-sm-2 col-xs-offset-3 col-sm-offset-3">
				<input class="btn btn-primary radius size-MINI" type="submit" id="submit" value="&nbsp;&nbsp;添加&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>
<%@include file="../footer.jsp"%>
<%@include file="../../../basics/myValidate.jsp"%>
<script src="http://libs.baidu.com/jquery/1.7.1/jquery.min.js"></script>
<script type="text/javascript" src="${basePath }/static/ajaxfileupload/ajaxfileupload-me-2.js" ></script>
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
                data: { "modelName": "manage","entityName":"user"},//放到/upload/shop/shopStore
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
    $().ready(function() {
        $("#Filedata").change(function(e){
            var flag =$("#Filedata").attr("imgFlag");
            var filePath = $("#Filedata").val();
            // 获取“.”位置
            var extStart = filePath.lastIndexOf(".");
            // 获取文件格式后缀，并全部大写
            var ext = filePath.substring(extStart, filePath.length).toUpperCase();
            // 判断文件格式
            if (ext != ".BMP" && ext != ".PNG" && ext != ".JPG" && ext != ".JPEG") {
                layer.msg("图片仅限于.gif .png .jpg .jpeg文件。",{icon: 5,time:1000});
                return false;
             }
            uploadify("Filedata","#"+flag+"Show","#"+flag+"Url",flag)
        });
        /*
         * 异步上传
         */
        function uploadify(fileId,showId,valId,flag){

            var file=$("#"+fileId).val();
            if(file==""||file==null){
                layer.msg('请选择图片',{icon: 5,time:1000});
                return;
            }
            $.ajaxFileUpload({
                url:'${basePath}/manage/admin/uploadify/image/originalSingle',//用于文件上传的服务器端请求地址
                secureuri:false,//一般设置为false
                data: { "modelName": flag },//放到/upload/flag
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
            });
        }

        // 后台属性是否存在的验证
        jQuery.validator.addMethod("checkAttribute", function(value, element,message) {
        var flag = true;
        $.ajax({
            type:'post',
            url:'${basePath}/manage/admin/user/loadCheckAttributeIsExistence',// 跳转到 action
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
    }, "已被注册");

        jQuery("#submintInfo").validate({
            rules: {
                name: {
                    required: true,
                    minlength: 2,
                    maxlength:10
                },
                nickName: {
                    required: true,
                    minlength: 2,
                    maxlength:10,
                    checkAttribute:"nickName"
                },
                password: {
                    required: true,
                    isPassword:true,
                    minlength: 6,
                    maxlength:12
                },
                confirm_password: {
                    required: true,
                    equalTo: "#password"
                },
                telNumber:{
                    required:true,
                    isMobile:true,
                    checkAttribute:"telNumber"
                },
                verifyCode: {
                    required: true,
                    strLenght:6
                },
                email: {
                    required: true,
                    email: true,
                    checkAttribute:"mailbox"
                }
            },
            submitHandler: function(form) {
                var name = $('#name').val();
                var nickName = $('#nickName').val();
                var portraitUrl = $('#portraitUrl').val();
                var email = $('#email').val();
                var telNumber = $('#telNumber').val();
                var password = $('#password').val();
                var userType = $('#userType').val();
                $.ajax( {
                    url:'${basePath}/manage/admin/user/addSave',// 跳转到 action
                    type:'post',
                    cache:false,
                    data:{"name":name,"nickName":nickName,"portraitUrl":portraitUrl,
                        "mailbox":email,"telNumber":telNumber,"password":password,"userType":userType
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
