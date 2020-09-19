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
	<link rel="stylesheet" href="${basePath }/static/HUI/lib/zTree/v3/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<title>资源列表</title>
</head>
<body>
<div class="pd-20">
	<input type="hidden" id="userId" value="${userId}"/>
	<div  class="cl pd-12 bg-1 bk-gray">
		<div class="zTreeDemoBackground left">
			<ul id="treeDemo" class="ztree"></ul>
		</div>
	</div>
	<div  class="cl pd-12 bg-1 bk-gray">
		${zTreeVoList}
	</div>
</div>
<%@include file="../footer.jsp"%>
<script type="text/javascript" src="${basePath }/static/HUI/lib/zTree/v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${basePath }/static/HUI/lib/zTree/v3/js/jquery.ztree.excheck-3.5.js"></script>
<!--请在下方写此页面业务相关的脚本-->
<SCRIPT type="text/javascript">
    <!--
    var setting = {
        data: {
            simpleData: {
                enable: true
            }
        }
    };

    var zNodes =[];

    $(document).ready(function(){
        $.ajax({
            type:'post',
            url:'${basePath}/manage/admin/resource/listResourceZTreeVoByType',// 跳转到 action
            cache:false,
            async:false,
            data:{"type":1},
            dataType:'json',
            success:function(data) {
                console.log(data);
                if(data.resultCode =="200" ){
                    zNodes = data.data;
                }else{
                    layer.msg(data.resultMsg,{icon:5,time:1000});
                }
            },
            error : function() {
                layer.msg(data.resultMsg,{icon:5,time:1000});
            }
        });
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    });
    //-->
</SCRIPT>
</body>
</html>
