package cn.csl.wx.dao;


import cn.csl.wx.entity.WxParam;

import java.util.List;

public interface WxParamMapper {
    int insert(WxParam record);
    int updateByPrimaryKey(WxParam record);
    WxParam selectByPrimaryKey(Long id);
    List<WxParam> listAll();
}