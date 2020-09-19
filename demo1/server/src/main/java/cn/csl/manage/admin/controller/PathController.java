package cn.csl.manage.admin.controller;

import cn.csl.manage.service.SysResourceServive;
import cn.csl.manage.entity.SysResource;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class PathController {
    @Resource
    private SysResourceServive sysResourceServive;
    public String getPath(HttpServletRequest request){
        String url = request.getRequestURI();
        List<SysResource> resources = sysResourceServive.listByAttribute("resUrl",url);
        List<SysResource> thisPaths = new ArrayList<>();
        String path="首页";
        if (resources.size()>1){
            SysResource sysResource = resources.get(0);
            thisPaths.add(sysResource);
            while (sysResource.getParentId()!=0){
                sysResource = sysResourceServive.loadByAttribute("id",String.valueOf(sysResource.getParentId()));
                thisPaths.add(sysResource);
            }
        }
        for (int i=thisPaths.size()-1;i>0;i--){
            path+=">"+thisPaths.get(i).getResName();
        }
        return path;
    }
}
