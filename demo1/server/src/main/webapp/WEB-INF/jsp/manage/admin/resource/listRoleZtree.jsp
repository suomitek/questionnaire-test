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
	<title>角色菜单</title>
</head>
<body>
<div class="pd-20">
	<div class="row cl mb-20">
		<div class="col-xs-3 col-sm-2 col-xs-3">
			<input class="btn btn-primary radius size-MINI" type="submit" id="submit" value="&nbsp;&nbsp;保存&nbsp;&nbsp;">
		</div>
	</div>
	<div  class="cl bg-1 bk-gray">
		<div class="zTreeDemoBackground left">
			<ul id="treeDemo" class="ztree"></ul>
		</div>
	</div>
</div>
<%@include file="../footer.jsp"%>
<script type="text/javascript" src="${basePath }/static/HUI/lib/zTree/v3/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${basePath }/static/HUI/lib/zTree/v3/js/jquery.ztree.excheck-3.5.js"></script>
<!--请在下方写此页面业务相关的脚本-->
<SCRIPT type="text/javascript">
    var setting = {
        view: {
            selectedMulti: false
        },
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };
    var zNodes =[];
	function huoqu(){
        var treeObj = $.fn.zTree.getZTreeObj("tree");
        var nodes = treeObj.getCheckedNodes(true);
	}
    $(document).ready(function(){
        $.ajax({
            type:'post',
            url:'${basePath}/manage/admin/resource/listRoleZtreeJson',// 跳转到 action
            cache:false,
            async:false,
            data:{"sysRoleId":${sysRoleId}},
            dataType:'json',
            success:function(data) {
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
	$("#submit").bind("click",function () {
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
        var nodes = treeObj.getCheckedNodes(true);
        var ids;
        for(var i=0;i<nodes.length;i++){
            if(i==0){
                ids=nodes[i].id;
			}else{
                ids=ids+","+nodes[i].id;
			}
		}
        $.ajax( {
            url:'${basePath}/manage/admin/resource/editRoleResource',// 跳转到 action
            type:'post',
            cache:false,
            data:{"sysRoleId":${sysRoleId},"ids":ids},
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
    })
</SCRIPT>
</body>
</html>
