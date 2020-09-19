package cn.csl.manage.entity;

import cn.csl.basics.util.annotation.check.CheckAnnotation;
import cn.csl.basics.util.annotation.search.SearchAnnotation;
import cn.csl.basics.util.annotation.unique.UniqueAnnotation;

public class SysResource {
    private Long id;

    @CheckAnnotation(type = "required",attribute="资源名称")
    @UniqueAnnotation(uniqueMsg = "资源名称已被使用")
    @SearchAnnotation(searchMsg = "资源名称")
    private String resName;
    @CheckAnnotation(type = "required",attribute="资源编码")
    @UniqueAnnotation(uniqueMsg = "资源编码已被使用")
    @SearchAnnotation(searchMsg = "资源编码")
    private String resCode;

    private Long parentId;
    @SearchAnnotation(searchMsg = "路径")
    private String resUrl;
    @SearchAnnotation(searchMsg = "级别")
    private Integer resType;

    private Integer orderNum;
    private Integer state;
    private Integer btnFlag;

    public Integer getBtnFlag() {
        return btnFlag;
    }

    public void setBtnFlag(Integer btnFlag) {
        this.btnFlag = btnFlag;
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

    @Override
    public boolean equals(Object obj) {
        try {
            if(((SysResource)obj).getId() == this.id){
                return true;
            }
        }catch (NullPointerException e){
            return false;
        }
        return false;
    }
}