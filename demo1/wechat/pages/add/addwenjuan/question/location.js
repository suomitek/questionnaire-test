// pages/add/addwenjuan/question/location.js
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
    dingwei: {
      id: 0,
      questionId: 0,
      name: "",
      cover: [],//
      coverId: [],
      orderNum: -1,
      obtain: -1,
      qType: "location",// 填空
      must: 1,
      location:"",
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
        dingwei: wenti
      })
    }else{
      wenti = this.data.dingwei;
      wenti.examineFlag = examineFlag;
      this.setData({
        dingwei: wenti
      })
    }
  },
  //每个分数的变化
  changeFen(e) {
    var wenti = this.data.dingwei;
    wenti.scoreIndex = e.detail.value;
    wenti.score = this.data.numList[e.detail.value];
    this.setData({
      dingwei: wenti
    })
  },
  timuSave() {
    var wenti = this.data.dingwei;
    if (this.check()) {
      redis.put("wenti", wenti, 600)
      wx.navigateBack({
        delta: 1
      })
    }
  },
  check() {
    //验证
    var dingwei = this.data.dingwei;
    if (dingwei.name == "") {
      dialog.warnModal("错误", "请输入题目");
      return false;
    }
    return true;
  },
  //图片上传
  previewImage(e) {
    var that = this;
    var dingwei = this.data.dingwei;
    var attr = e.target.dataset.attr;
    var index = e.target.dataset.index;
    var ids = dingwei[`${attr}Id`]

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
    var dingwei = this.data.dingwei;
    if (index == -1) {
      var filePath = data.data.filePath
      dingwei[`${attr}`].unshift(filePath)
      dingwei[`${attr}Id`].unshift(data.data.id);
    } else {
      dingwei[`${attr}`][index] = data.data.filePath;
      dingwei[`${attr}Id`][index] = data.data.id;
    }
    this.setData({
      dingwei: dingwei
    })
  },
  //所有为输 题目 的同步方法
  binddingweiKeyInput(e) {
    var attr = e.currentTarget.dataset.attr
    var dingwei = this.data.dingwei;
    dingwei[`${attr}`] = e.detail.value
    this.setData({
      dingwei: dingwei
    })
  },
})