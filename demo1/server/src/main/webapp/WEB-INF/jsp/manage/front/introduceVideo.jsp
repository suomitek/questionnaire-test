<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<title>${constant.programName }系统介绍</title>
<meta name="keywords" content="系统介绍">
<meta name="description" content="系统介绍">
<link href="${basePath }/static/HUI/lib/video/video-js.css" rel="stylesheet">
<script src="${basePath }/static/HUI/lib/video/video.js"></script>
<script src="${basePath }/static/HUI/lib/video/videojs-contrib-hls.js"></script>
    <style>
        body {
            padding: 0;
            margin: 0;
        }
        div.videocontent {
            width:100%;
        }
        div.wrapper {
            max-
            :750px;
            margin: 0 auto;
        }

    </style>
</head>
<body >
<div class="wrapper">
    <div class="videocontent">
        <video id="example_video_1" class="video-js vjs-default-skin vjs-big-play-centered vjs-16-9" controls preload="none" data-setup="{}">
            <source src="${basePath }${introduceVideoUrl}"  type="application/x-mpegURL">
        </video>
    </div>
</div>
<script type="text/javascript" src="${basePath }/static/HUI/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        // $("#videoDiv").height()
        // $("#videoDiv").css("height", $(window).height()+"px");
        // $("#videoDiv").css("width", $(window).width()+"px");
        // alert($(window).height()); //浏览器当前窗口可视区域高度
        // alert($(document).height()); //浏览器当前窗口文档的高度
        // alert($(document.body).height());//浏览器当前窗口文档body的高度
        // alert($(document.body).outerHeight(true));//浏览器当前窗口文档body的总高度 包括border padding margin

        // alert($(window).width()); //浏览器当前窗口可视区域宽度
        // alert($(document).width());//浏览器当前窗口文档对象宽度
        // alert($(document.body).width());//浏览器当前窗口文档body的宽度
        // alert($(document.body).outerWidth(true));//浏览器当前窗口文档body的总宽度 包括border padding margin

    })

</script>
</body>
</html>