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
<title>编辑微信参数</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="submintInfo" method="post" >
		<div class="row cl">
			<label class="form-label col-sm-3 col-xs-3" for="appId" style="text-align: right"><span class="c-red">*</span>小程序appid：</label>
			<div class="formControls col-sm-7 col-xs-7">
				<select name="projectType" id="projectType" class="select toJson">
					<option value="0" <c:if test="${wxParam.projectType ==0}">selected = "selected"</c:if>>都不使用</option>
					<option value="1" <c:if test="${wxParam.projectType ==1}">selected = "selected"</c:if>>仅小程序</option>
					<option value="2" <c:if test="${wxParam.projectType ==2}">selected = "selected"</c:if>>仅公众号</option>
					<option value="3" <c:if test="${wxParam.projectType ==3}">selected = "selected"</c:if>>两者都用</option>
				</select>
				<input id="id"  class="toJson" type="hidden" value="${wxParam.id}">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-sm-3 col-xs-3" for="keyPath" style="text-align: right"><span class="c-red">*</span>证书地址：</label>
			<div class="formControls col-sm-7 col-xs-7">
				<input id="keyPath" name="keyPath" value="${wxParam.keyPath}" class="input-text toJson" placeholder="证书地址" type="text">
			</div>
		</div>
		<div class="row cl xiao">
			<label class="form-label col-sm-3 col-xs-3" for="appId" style="text-align: right"><span class="c-red">*</span>小程序appid：</label>
			<div class="formControls col-sm-7 col-xs-7">
				<input id="appId" name="appId" value="${wxParam.appId}" class="input-text toJson" placeholder="小程序appid" type="text">
			</div>
		</div>
		<div class="row cl xiao">
			<label class="form-label col-sm-3 col-xs-3" for="appSecret" style="text-align: right"><span class="c-red">*</span>小程序appSecret：</label>
			<div class="formControls col-sm-7 col-xs-7">
				<input id="appSecret" name="appSecret" value="${wxParam.appSecret}" class="input-text toJson" placeholder="小程序appSecret" type="text">
			</div>
		</div>
		<div class="row cl xiao">
			<label class="form-label col-sm-3 col-xs-3" for="mchId" style="text-align: right"><span class="c-red">*</span>小程序mchId：</label>
			<div class="formControls col-sm-7 col-xs-7">
				<input id="mchId" name="mchId" value="${wxParam.mchId}" class="input-text toJson" placeholder="小程序mchId" type="text">
			</div>
		</div>
		<div class="row cl xiao">
			<label class="form-label col-sm-3 col-xs-3" for="mchKey" style="text-align: right"><span class="c-red">*</span>小程序mchKey：</label>
			<div class="formControls col-sm-7 col-xs-7">
				<input id="mchKey" name="mchKey" value="${wxParam.mchKey}" class="input-text toJson" placeholder="小程序mchKey" type="text">
			</div>
		</div>
		<div class="row cl gong">
			<label class="form-label col-sm-3 col-xs-3" for="mpAppId" style="text-align: right"><span class="c-red">*</span>公众号appid：</label>
			<div class="formControls col-sm-7 col-xs-7">
				<input id="mpAppId" name="mpAppId" value="${wxParam.mpAppId}" class="input-text toJson" placeholder="公众号appid" type="text">
			</div>
		</div>
		<div class="row cl gong">
			<label class="form-label col-sm-3 col-xs-3" for="mpAppSecret" style="text-align: right"><span class="c-red">*</span>公众号appSecret：</label>
			<div class="formControls col-sm-7 col-xs-7">
				<input id="mpAppSecret" name="mpAppSecret" value="${wxParam.mpAppSecret}" class="input-text toJson" placeholder="公众号appSecret" type="text">
			</div>
		</div>
		<div class="row cl gong">
			<label class="form-label col-sm-3 col-xs-3" for="mpMchId" style="text-align: right"><span class="c-red">*</span>公众号mchId：</label>
			<div class="formControls col-sm-7 col-xs-7">
				<input id="mpMchId" name="mpMchId" value="${wxParam.mpMchId}" class="input-text toJson" placeholder="公众号mchId" type="text">
			</div>
		</div>
		<div class="row cl gong">
			<label class="form-label col-sm-3 col-xs-3" for="mpMchKey" style="text-align: right"><span class="c-red">*</span>公众号mchKey：</label>
			<div class="formControls col-sm-7 col-xs-7">
				<input id="mpMchKey" name="mpMchKey" value="${wxParam.mchKey}" class="input-text toJson" placeholder="公众号mchKey" type="text">
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-2 col-sm-2 col-xs-offset-3 col-sm-offset-3">
				<input class="btn btn-primary radius" type="submit" id="submit" value="&nbsp;&nbsp;编辑&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>
<%@include file="../../../basics/footer.jsp"%>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/jquery.validate.js"></script>
<script src="${basePath }/static/HUI/lib/jquery-validation-1.17.0/dist/myValidate.js"></script>
<script src="${basePath }/static/js/inputToJson.js"></script>
<script type="text/javascript">
    $("#projectType").bind("change", function () { //按钮 更新提示语
        var projectType = $("#projectType").val();
        changType(projectType);
	});
    changType(${wxParam.projectType});
    function changType(projectType){
        var xiaos=$(".xiao");
        var gongs=$(".gong");

        $("#appSecret").removeClass("toJson");
        $("#appId").removeClass("toJson");
        $("#mchId").removeClass("toJson");
        $("#mchKey").removeClass("toJson");
        $("#mpAppId").removeClass("toJson");
        $("#mpAppSecret").removeClass("toJson");
        $("#mpMchId").removeClass("toJson");
        $("#mpMchKey").removeClass("toJson");

        if(projectType==0||projectType==2){
            for(var i=0;i<xiaos.length;i++){
                $(xiaos[i]).hide();
            }
        }else if(projectType ==3||projectType==1){
            for(var i=0;i<xiaos.length;i++){
                $(xiaos[i]).show();
            }
            $("#appSecret").addClass("toJson");
            $("#appId").addClass("toJson");
            $("#mchId").addClass("toJson");
            $("#mchKey").addClass("toJson");
		}
        if(projectType==1||projectType==0){
            for(var i=0;i<gongs.length;i++){
                $(gongs[i]).hide();
            }
        }else  if(projectType ==3||projectType==2){
            for(var i=0;i<gongs.length;i++){
                $(gongs[i]).show();
            }
            $("#mpAppId").addClass("toJson");
            $("#mpAppSecret").addClass("toJson");
            $("#mpMchId").addClass("toJson");
            $("#mpMchKey").addClass("toJson");
        }
	}
	jQuery("#submintInfo").validate({
		rules: {
			appId: {
				required: true
			},appSecret: {
				required: true
			},mchId: {
				required: true
			},mchKey: {
				required: true
			},mpAppId: {
				required: true
			},mpAppSecret: {
				required: true
			},mpMchId: {
				required: true
			},mpMchKey: {
				required: true
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
						layer.msg('编辑成功',{icon:1,time:1000});
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
