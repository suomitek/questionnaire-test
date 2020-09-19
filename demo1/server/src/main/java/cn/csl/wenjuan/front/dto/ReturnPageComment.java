package cn.csl.wenjuan.front.dto;

import cn.csl.basics.util.TableReturnUtil;
import cn.csl.wenjuan.entity.WjComment;
import cn.csl.wenjuan.front.vo.WjCommentVo;

import java.util.List;

public class ReturnPageComment {

    private long draw=0;//当前页面
    private long pageMax=0;//页面数量总计
    private long length=10;//每页显示页数
    private long recordsTotal;//符合条件的总条数
    private List<WjCommentVo> commentVos;

    public long getDraw() {
        return draw;
    }

    public void setDraw(long draw) {
        this.draw = draw;
    }

    public long getPageMax() {
        return pageMax;
    }

    public void setPageMax() {
        if (this.getRecordsTotal()%this.getLength()!=0){
            this.pageMax = this.getRecordsTotal()/this.getLength()+1;
        }else{
            this.pageMax = this.getRecordsTotal()/this.getLength();
        }
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public List<WjCommentVo> getCommentVos() {
        return commentVos;
    }

    public void setCommentVos(List<WjCommentVo> commentVos) {
        this.commentVos = commentVos;
    }
}
