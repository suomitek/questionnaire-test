// pages/add/addwenjuan/question/pingfen.js
var util = require('../../../../utils/util.js');
var urls = require('../../../../utils/urls.js');
var dialog = require('../../../../libs/dialog/dialog.js');
var redis = require('../../../../libs/redis.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host: urls.host,
    numList: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25],
    pingfen: {
      id: 0,
      questionId: 0,
      name: "",
      cover: [],//
      coverId: [],
      orderNum: -1,
      qType: "pingfen",// 填空
      must: 1,
      obtain: -1,
      maxFen: 5,//满分
      fen:0,
      score: 1,///默认1分
      scoreIndex: 0,///默认0 为 1分
      examineFlag: 0,
    }
  },
  onLoad: function (options) {
    var wenti = redis.get("wenti")
    var examineFlag = parseInt(options.examineFlag);
    if (wenti != null) {
      redis.remove("wenti")
      wenti.examineFlag = examineFlag;
      this.setData({
        pingfen: wenti
      })
    }else{
      wenti = this.data.pingfen;
      wenti.examineFlag = examineFlag;
      this.setData({
        pingfen: wenti
      })
    }
  },
  //每个分数的变化
  changeFen(e) {
    var wenti = this.data.pingfen;
    wenti.scoreIndex = e.detail.value;
    wenti.score = this.data.numList[e.detail.value];
    this.setData({
      pingfen: wenti
    })
  },
  timuSave() {
    var wenti = this.data.pingfen;
    if (this.check()) {
      redis.put("wenti", wenti, 600)
      wx.navigateBack({
        delta: 1
      })
    }
  },
  check() {
    //验证
    var pingfen = this.data.pingfen;
    if (pingfen.name == "") {
      dialog.warnModal("错误", "请输入题目");
      return false;
    }
    return true;
  },
  //图片上传
  previewImage(e) {
    var that = this;
    var pingfen = this.data.pingfen;
    var attr = e.target.dataset.attr;
    var index = e.target.dataset.index;
    var ids = pingfen[`${attr}Id`]

    wx.chooseImage({
      count: 1,//暂时只写了单图上传
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success(res) {
        var tempFilePaths = res.tempFilePaths;
        // var uploadTask = util.singleImgUpload(tempFilePaths,that.huidian)
        util.fileImgChangeSave(tempFilePaths, ids, 1, attr, index, that.addhuidiao)
      }
    })
  },
  //图片上传完成的回调'
  addhuidiao(data, attr, index) {
    var pingfen = this.data.pingfen;
    if (index == -1) {
      var filePath = data.data.filePath
      pingfen[`${attr}`].unshift(filePath)
      pingfen[`${attr}Id`].unshift(data.data.id);
    } else {
      pingfen[`${attr}`][index] = data.data.filePath;
      pingfen[`${attr}Id`][index] = data.data.id;
    }
    this.setData({
      pingfen: pingfen
    })
  },
  //所有为输 题目 的同步方法
  bindpingfenKeyInput(e) {
    var attr = e.currentTarget.dataset.attr
    var pingfen = this.data.pingfen;
    pingfen[`${attr}`] = e.detail.value
    this.setData({
      pingfen: pingfen
    })
  },
  // 所有的switch
  changeSwitch(e) {
    var attr = e.currentTarget.dataset.attr
    var pingfen = this.data.pingfen;
    if (e.detail.value) {
      pingfen[`${attr}`] = 1
    } else {
      pingfen[`${attr}`] = 0
    }
    this.setData({ pingfen: pingfen })
  },
  ////  数字按钮
  changeNum(e) {
    const attr = e.target.dataset.attr;
    var pingfen = this.data.pingfen;
    const flag = parseInt(e.target.dataset.flag);
    var min = parseInt(e.target.dataset.min);
    var max = parseInt(e.target.dataset.max);
    var thisNum = parseInt(pingfen[`${attr}`]);
    thisNum += flag;
    if (thisNum >= min && thisNum<=max){
      pingfen[`${attr}`] = thisNum;
      this.setData({ pingfen: pingfen })
    }
  },
})