package cn.csl.manage.admin.vo;

import cn.csl.basics.util.annotation.check.CheckAnnotation;
import cn.csl.basics.util.annotation.search.SearchAnnotation;
import cn.csl.basics.util.annotation.unique.UniqueAnnotation;
import cn.csl.manage.entity.SysResource;
import org.apache.commons.beanutils.BeanUtils;

import java.util.List;

public class SysResourceVo extends SysResource {
    private Long id;
    private String resName;
    private String resCode;
    private Long parentId;
    private String resUrl;
    private Integer resType;
    private Integer orderNum;
    private Integer state;
    public List<SysResource> childrenResources;
    private Integer btnFlag;

    public SysResourceVo(SysResource sysResource, List<SysResource> childrenResources){
        this.id = sysResource.getId();
        this.btnFlag = sysResource.getBtnFlag();
        this.resName = sysResource.getResName();
        this.resCode = sysResource.getResCode();
        this.parentId = sysResource.getParentId();
        this.resUrl = sysResource.getResUrl();
        this.resType = sysResource.getResType();
        this.orderNum = sysResource.getOrderNum();
        this.state = sysResource.getState();
        this.childrenResources = childrenResources;
    }

    public Integer getBtnFlag() {
        return btnFlag;
    }

    public void setBtnFlag(Integer btnFlag) {
        this.btnFlag = btnFlag;
    }

    public List<SysResource> getChildrenResources() {
        return childrenResources;
    }

    public void setChildrenResources(List<SysResource> childrenResources) {
        this.childrenResources = childrenResources;
    }
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName == null ? null : resName.trim();
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode == null ? null : resCode.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getResUrl() {
        return resUrl;
    }

    public void setResUrl(String resUrl) {
        this.resUrl = resUrl == null ? null : resUrl.trim();
    }

    public Integer getResType() {
        return resType;
    }

    public void setResType(Integer resType) {
        this.resType = resType;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
