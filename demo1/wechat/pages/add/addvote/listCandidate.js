// pages/add/addvote/listCandidate.js
var dialog = require('../../../libs/dialog/dialog.js');
import WxValidate from '../../../libs/WxValidate'
var util = require('../../../utils/util.js');
var urls = require('../../../utils/urls.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host:urls.host,
    swiperTitle: [{
        text: "待审核"
      }, {
        text: "已通过"
      }, {
        text: "已拒绝"
      }
    ],
    currentPage: 0,
  },
  onLoad(options){
    var that = this;
    var voteId = options.voteId;
    var url = urls.urlobj.listCandidate;
  
    util.getServerData(url, {
      voteId: voteId
    }, null).then(function (res) {
      if (res.data.resultCode == '200') {
        that.setData({
          voteId: voteId,
          candidates: res.data.data
        })
      }
    });
  },
  addCandidate: function () {//添加投票选手 
    var voteType = this.data.candidates[0].voteType
    var voteId = this.data.voteId
    var wxUserInfo = wx.getStorageSync('wxUserInfo');
    wx.navigateTo({
      url: '/pages/add/addvote/addCandidate?page=detailed&voteType=' + voteType + "&voteId=" + voteId + "&wxUserId=" + wxUserInfo.id
    })
  },
  editCandidate(e){
    var that = this;
    var useFlag = e.currentTarget.dataset.useflag
    var index = e.currentTarget.dataset.index
    var candidates = this.data.candidates
    var id = candidates[index].id;
    var url = urls.urlobj.editCandidate;
    var data = {
      id: id, useFlag: useFlag
    };
    console.log(url)
    console.log(data)
    util.getServerData(url,data, null).then(function (data) {
        candidates[index].useFlag = useFlag
        that.setData({
          candidates: candidates
        })
    });
  },
  turnPage: function (e) {
    this.setData({
      currentPage: e.currentTarget.dataset.index
    })
  },
})