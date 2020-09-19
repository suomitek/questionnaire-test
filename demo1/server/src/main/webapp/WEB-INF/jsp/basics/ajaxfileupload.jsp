<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="${basePath }/static/HUI/lib/jquery/1.9.1/jquery.min.js"></script>
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
