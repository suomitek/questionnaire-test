//index.js
//获取应用实例
const app = getApp()
var urls = require('../../../utils/urls.js');
var util = require('../../../utils/util.js');
var dialog = require('../../../libs/dialog/dialog.js');
var redis = require('../../../libs/redis.js');
Page({
  data: {
    host:urls.host,
    swiperTitle: [{
      text: "我的投票"
    }, {
        text: "我的问卷"
    }],
    currentPage: 0,
    showVideo: false,
    maskShow:false,
    maskName: "",
    maskId: 0,
    voteList: []
  },
  onLoad: function (options) {
    var wxUserInfo = wx.getStorageSync("wxUserInfo");
    var currentPage = options.currentPage;
    if (typeof (currentPage) != "undefined" ){
        this.setData({
          currentPage: currentPage
        })
    }
    var that = this;
    var url = urls.urlobj.voteList;
    util.getServerData(url, {
      wxUserId: wxUserInfo.id
    }, null).then(function (res) {
      that.setData({
        voteList: res.data.data
      })

    });
    var url = urls.urlobj.questionList;
    util.getServerData(url, {
      wxUserId: wxUserInfo.id
    }, null).then(function (res) {
      var questionList=res.data.data
      for (var i = 0; i < questionList.length;i++){
        var question = questionList[i];
        question.cover = JSON.parse(question.cover);
        questionList[i] = question
      }
      that.setData({
        questionList: questionList
      })
    });
  },
  maskShow(e){
    var id = e.currentTarget.dataset.id;
    var index = e.currentTarget.dataset.index;
    var type = e.currentTarget.dataset.type;
    var maskName = "";
    if (type === "question"){
      maskName = this.data.questionList[index].name;
    }else{
      maskName = this.data.voteList[index].name;
    }
    
    this.setData({
      maskName: maskName,
      maskId: id,
      maskIndex: index,
      maskType: type,
      maskShow: true
    })
  },
  maskHide(){
    this.setData({
      maskName: "",
      maskId: 0,
      maskShow: false
    })
  },
  analysis:function(){
    var id = this.data.maskId;
    var index = this.data.maskIndex;
    var that = this;
    if (this.data.maskType === "question") {
      wx.navigateTo({
        url: '/pages/wenjuan/analysis/analysis?id=' + id
      })
    } else {
      dialog.warnModal("错误", "投票活动暂未开启数据分析");
    }
  },
  copy: function () {
    var id = this.data.maskId;
    var index = this.data.maskIndex;
    var that = this;
    if (this.data.maskType === "question") {
      var question = this.data.questionList[index];
      var addWenJuanData={
        wentis: JSON.parse(question.problemsStr),
        setting: JSON.parse(question.questionDataStr),
      }
      wx.setStorageSync("addWenJuanData",addWenJuanData)
      dialog.warnModalTrueBack("提示", "已经存为草稿,是否前往添加?", function (flag) {
        if (flag == 1) {
          wx.navigateTo({
            url: '/pages/add/addwenjuan/addwenjuan'
          })
        }
      })
    } else {
      dialog.warnModal("错误", "投票活动不支持复制");
    }
  },
  zanting(){
    var id = this.data.maskId;
    if (this.data.maskType === "question") {
      var url = urls.urlobj.changeSuspend;
      util.getServerData(url, {
        id: id,
      }, null).then(function (res) {
        dialog.warnModal("成功", res.data.data);
      });
    } else {
      dialog.warnModal("错误", "投票活动不支持暂停");
    }
    
  },
  edit(){
    if (this.data.maskType === "question") {
      var index = this.data.maskIndex;
      var question = this.data.questionList[index];
      redis.put("editWenJuanDatasetting", JSON.parse(question.questionDataStr),60);
      wx.navigateTo({
        url: '/pages/edit/editwenjuan/editwenjuan'
      })
    } else {
      dialog.warnModal("错误", "投票活动不支持编辑");
    }
  },
  detailed: function () {
    var id = this.data.maskId;
    // this.maskHide();
    if (this.data.maskType ==="question"){
      wx.navigateTo({
        url: '/pages/wenjuan/detailed?id=' + id
      })
    }else{
      wx.navigateTo({
        url: '/pages/vote/detailed?id=' + id
      })
    }
  },
  deleteObj(){
    var that = this;
    var id = this.data.maskId;
    var index = this.data.maskIndex;

    if (this.data.maskType === "question") {
      var url = urls.urlobj.deleteQuestion;
      var questionList = that.data.questionList
      var name = questionList[index].name;
      dialog.warnModalTrueBack("提示", "确定要删除" + name + "?", function (flag) {
        if (flag == 1) {
          util.getServerData(url, {
            id: id
          }, null).then(function (res) {
            questionList.splice(index, 1)
            that.setData({
              questionList: questionList
            })
          });
        }
      })
    } else {
      var url = urls.urlobj.deleteVote;
      var voteList = that.data.voteList
      var name = voteList[index].name;
      dialog.warnModalTrueBack("提示", "确定要删除" + name + "?", function (flag) {
        if (flag == 1) {
          util.getServerData(url, {
            id: id
          }, null).then(function (res) {
            voteList.splice(index, 1)
            that.setData({
              voteList: voteList
            })
          });
        }
      })
    }

    




   

    
  },
  //分享
  onShareAppMessage: function (res) {
    var url = "";
    var id = this.data.maskId;
    var index = this.data.maskIndex;
    var avater = "";
    var host = this.data.host;
    var name = "";
    if (this.data.maskType === "question") {
      var question = this.data.questionList[index];
      url = '/pages/wenjuan/detailed?id=' + id;
      avater = question.cover[0];
      name = question.name;
    } else {
      var vote = this.data.voteList[index];
      url = '/pages/vote/detailed?id=' + id;
      avater = vote.covers[0];
      name = vote.name;
    }
    return {
      title: name,
      path: url,
      imageUrl: host + avater,  
      success: function (res) {
        // 转发成功
        wx.showToast({
          title: "分享成功",
          icon: 'success',
          duration: 2000
        })
      },
      fail: function (res) {
        // 分享失败
      },
    }
  },

  //生成海报
  //调用子组件的方法
  getSharePoster: function () {
    this.setData({ showVideo: false })
    var index = this.data.maskIndex;
    if (this.data.maskType === "question") {
      var question = this.data.questionList[index];
      var time = question.startTimeStr + " 至 " + question.endTimeStr;
      var avater = question.cover[0];
      var productname = question.name;
      this.setData({
        time: time,
        avater: avater,
        productname: productname,
      });
    } else {
      var vote = this.data.voteList[index];
      var time = vote.startTimeStr + " 至 " + vote.endTimeStr;
      var avater = vote.covers[0];
      var productname = vote.name;
      this.setData({
        time: time,
        avater: avater,
        productname: productname,
      });
    }

   
    this.selectComponent('#getPoster').getAvaterInfo()
  },
  myEventListener: function (e) {
    this.setData({ showVideo: true })
  },






  turnPage: function (e) {
    this.setData({
      currentPage: e.currentTarget.dataset.index
    })
  },
})
