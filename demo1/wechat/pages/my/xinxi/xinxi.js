// pages/my/xinxi/xinxi.js
var util = require('../../../utils/util.js');
var urls = require('../../../utils/urls.js');
var dialog = require('../../../libs/dialog/dialog.js');
var myValidate = require('../../../libs/MyValidate.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    inputCode: true, 
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var wxUserInfo = wx.getStorageSync('wxUserInfo');
    this.setData({
      wxUserInfo: wxUserInfo
    })
  },
  baocun:function(){
    var wxUserInfo = wx.getStorageSync('wxUserInfo');
    var wxUserInfo2 = this.data.wxUserInfo;
    var that = this;
    if (wxUserInfo2.realName == "") {
      wx.showToast({
        title: '请输入用户名',
        icon: 'none'
      })
    } else if (wxUserInfo2.phone == "") {
      wx.showToast({
        title: '请输入手机号',
        icon: 'none'
      })
    } else {

      var url = urls.urlobj.editInfo;
      var data = {
        id: wxUserInfo.id,
        phone: wxUserInfo2.phone,
        realName: wxUserInfo2.realName,
      }
      util.getServerData(url, data, null).then(function (res) {
        if (res.data.resultCode == '200') {
          wx.setStorageSync('wxUserInfo', res.data.data);
          dialog.warnModalBack("成功", "修改成功",function(){
            wx.navigateBack({
              delta: 1,
            })
          });
        }
      });
    }




  },
  //输入
  input: function (e) {
    var val = e.detail.value;
    var attr = e.currentTarget.dataset.attr;
    var wxUserInfo = this.data.wxUserInfo;
    wxUserInfo[`${attr}`] = val;
    this.setData({ wxUserInfo: wxUserInfo, val: val})
  },
  showInputCode(e) {
    var attr = e.currentTarget.dataset.attr;
    var title = e.currentTarget.dataset.title;
    var val = e.currentTarget.dataset.val;
    this.setData({ inputCode: false, attr: attr, title: title, val: val})
  },
  model2confirm: function (e) {
    this.setData({ inputCode: true })
  },
  model2cancel: function (e) {
    this.setData({ inputCode: true })
  }
})