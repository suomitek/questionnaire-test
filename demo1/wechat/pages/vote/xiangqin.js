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
    var thisVoteVo = wx.getStorageSync("thisVoteVo");
    wx.removeStorage({
      key: 'thisVoteVo'
    })
    thisVoteVo.startTimeStr = thisVoteVo.startTimeStr.replace(/\//g, '-');
    thisVoteVo.endTimeStr = thisVoteVo.endTimeStr.replace(/\//g, '-');
    this.setData({ thisVoteVo: thisVoteVo})
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