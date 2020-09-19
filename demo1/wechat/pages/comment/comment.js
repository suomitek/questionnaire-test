// pages/comment/comment.js
var urls = require('../../utils/urls.js');
var util = require('../../utils/util.js');
var redis = require('../../libs/redis.js');
var dialog = require('../../libs/dialog/dialog.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    msg: "",
    title: "",
    length:1,
    comment:{
      subscriberId: 0,
      subscriberSurface: "",//
      comment: "",
      useless: 0,
      fabulous: 0,
      surface: "",
      nature: "",
      surfaceId: 0,
      costSurface: "",
      costNature: "",
      costSurfaceId: 0
    }
  },
  onLoad: function (options) {
    var title = options.title;
    var msg = options.msg;
    var length = 1;
    var comment = redis.get("comment");
    redis.remove("comment");
    this.setData({
      length: length,
      msg: msg,
      title: title,
      comment: comment
    })
  },
  addSave:function(){
    var url = urls.urlobj.commentAddSave;
    var comment = this.data.comment
    var msg = this.data.msg
    var neicong = comment.comment.replace(/(^\s*)|(\s*$)/g, ""); 
    if (neicong.length < this.data.length){
      dialog.showToastAutoError(msg+"太少了");
      if (neicong==""){
        comment.comment = ""
        this.setData({
          comment: comment
        })
      }
      
      return;
    }
    util.getServerData(url, comment, null).then(function (data) {
      if (data.data.resultCode == '200') {
        dialog.showToastAutoSuccess("评论成功")
        wx:wx.navigateBack({
          delta: 1,
        })
      }
    });
  },
  //所有为输入框的同步方法
  bindKeyInput(e) {
    var attr = e.currentTarget.dataset.attr
    var comment = this.data.comment;
    comment[`${attr}`] = e.detail.value
    this.setData({
      comment: comment
    })
  },
})