// pages/vote/xiangqin.js
var urls = require('../../utils/urls.js');
var util = require('../../utils/util.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host: urls.host
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var setting = wx.getStorageSync("thisWenjuanSetting");
    wx.removeStorage({
      key: 'thisWenjuanSetting'
    })
    setting.startTimeStr = setting.startTimeStr.replace(/\//g, '-');
    setting.endTimeStr = setting.endTimeStr.replace(/\//g, '-');
    this.setData({ setting: setting})
  },
  call(e){
    var phone = e.currentTarget.dataset.phone;
    var name = e.currentTarget.dataset.name;
    wx.showActionSheet({
      itemList: [name + ":" + phone, '呼叫'],
      success: function (res) {
        if (res.tapIndex == 1) {
          wx.makePhoneCall({
            phoneNumber: phone,
          })
        }
      }
    })
  }
})