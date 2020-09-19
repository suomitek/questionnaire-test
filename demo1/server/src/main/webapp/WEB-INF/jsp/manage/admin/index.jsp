<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
<title>${environment.name }-后台</title>
<meta name="keywords" content="后台管理系统">
<meta name="description" content="后台管理系统">
</head>
<body>
<header class="navbar-wrapper">
	<div class="navbar navbar-fixed-top">
		<div class="container-fluid cl"> <a class="logo navbar-logo f-l mr-10 hidden-xs" href="#">${environment.name }</a> <a class="logo navbar-logo-m f-l mr-10 visible-xs" href="#"></a> <span class="logo navbar-slogan f-l mr-10 hidden-xs"></span> <a aria-hidden="false" class="nav-toggle Hui-iconfont visible-xs" href="javascript:;">&#xe667;</a>
			<nav class="nav navbar-nav">
			</nav>
			<nav id="Hui-userbar" class="nav navbar-nav navbar-userbar hidden-xs">
				<ul class="cl">
					<li>${admin.name}</li>
					<li class="dropDown dropDown_hover"> <a href="#" class="dropDown_A">${userName }<i class="Hui-iconfont">&#xe6d5;</i></a>
						<ul class="dropDown-menu menu radius size-MINI box-shadow">
							<li><a href="#" onclick="editPwd();">设置</a></li>
							<li><a href="#" onclick="loginOut();">退出</a></li>
						</ul>
					</li>
					<li id="Hui-skin" class="dropDown right dropDown_hover"> <a href="javascript:;" class="dropDown_A" title="换肤"><i class="Hui-iconfont" style="font-size:18px">&#xe62a;</i></a>
						<ul class="dropDown-menu menu radius size-MINI box-shadow">
							<li><a href="javascript:;" data-val="default" title="默认（黑色）">默认（黑色）</a></li>
							<li><a href="javascript:;" data-val="blue" title="蓝色">蓝色</a></li>
							<li><a href="javascript:;" data-val="green" title="绿色">绿色</a></li>
							<li><a href="javascript:;" data-val="red" title="红色">红色</a></li>
						</ul>
					</li>
				</ul>
			</nav>
		</div>
	</div>
</header>
<aside class="Hui-aside">
	<input runat="server" id="divScrollValue" type="hidden" value="" />
	<div class="menu_dropdown bk_2">
        <c:forEach items="${sysResourceVos}" begin="0" end="${sysResourceVos.size()}" step="1" var="sysResourceVo" varStatus="status">
            <shiro:hasPermission name="${sysResourceVo.resUrl}">
                <dl id="menu-admin">
                    <dt  style="font-weight:bold;"><i class="Hui-iconfont">&#xe61d;</i>${sysResourceVo.resName}<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i></dt>
                    <dd>
                        <ul>
                            <c:forEach items="${sysResourceVo.childrenResources}" var="childrenResource">
                                <shiro:hasPermission name="${childrenResource.resUrl}">
                                    <li><a data-href="${basePath }${childrenResource.resUrl}" data-title="${childrenResource.resName}" href="javascript:void(0)">${childrenResource.resName}</a></li>
                                </shiro:hasPermission>
                            </c:forEach>
                        </ul>
                    </dd>
                </dl>
            </shiro:hasPermission>
        </c:forEach>
	</div>
</aside>
<div class="dislpayArrow hidden-xs"><a class="pngfix" href="javascript:void(0);" onClick="displaynavbar(this)"></a></div>
<section class="Hui-article-box">
	<div id="Hui-tabNav" class="Hui-tabNav hidden-xs">
		<div class="Hui-tabNav-wp">
			<ul id="min_title_list" class="acrossTab cl">
				<li class="active"><span title="${sysResourceVos[0].childrenResources[0].resName}"></span></li>
			</ul>
		</div>
		<div class="Hui-tabNav-more btn-group"><a id="js-tabNav-prev" class="btn radius size-MINI btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d4;</i></a><a id="js-tabNav-next" class="btn radius size-MINI btn-default size-S" href="javascript:;"><i class="Hui-iconfont">&#xe6d7;</i></a></div>
	</div>
	<div id="iframe_box" class="Hui-article">
		<div class="show_iframe">
			<div style="display:none" class="loading"></div>
			<iframe scrolling="no" frameborder="0" src="${basePath}/manage/admin/welcome"></iframe>
		</div>
	</div>
</section>
<%@include file="footer.jsp"%>
<script type="text/javascript">
function editPwd(){
	layer_show("设置","${basePath}/manage/admin/constant/edit",450,350);
}
function loginOut(){
    $.ajax({
        type:"post",
        url:"${basePath}/manage/admin/logout",
        dataType:"json",
        data:{},
        success:function(data){
            console.log(data)
            if(data.resultCode=="200") {
                window.location.href="${basePath}/manage/admin/index";
            }else {
                layer.msg(data.resultMsg, {time:1000});
            }
        },
        error:function(e){
            window.location.href="${basePath}/manage/admin/index";
        }
    });
}

</script>
</body>
</html>