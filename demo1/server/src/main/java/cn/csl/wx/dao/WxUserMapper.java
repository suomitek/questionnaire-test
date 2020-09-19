package cn.csl.wx.dao;

import cn.csl.wx.entity.WxUser;

public interface WxUserMapper {
    int insert(WxUser record);
    int updateByPrimaryKey(WxUser record);
    WxUser selectByPrimaryKey(Long id);
    WxUser loadByOpenId(String openid);
}