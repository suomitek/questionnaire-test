// pages/add/addvote/addCandidate.js
var util = require('../../../utils/util.js');
var urls = require('../../../utils/urls.js');
var dialog = require('../../../libs/dialog/dialog.js');
import WxValidate from '../../../libs/WxValidate'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host: urls.host,
    candidate:{
      host: urls.host,//用于模板中的图片显示
      orderNum:0,//选手号
      isTouchMove: false,//用于侧滑删除的
      coverId: [],
      cover: [],
      imgIntroduce:[],
      imgIntroduceId:[],
      introduce:"",
      name:"",
      supNum:0,//票数
      useFlag:1,
      createTimeStr: util.timeToStr(new Date),
      voteType:2,
    }
  },
  onLoad: function (options) {
    var voteType = options.voteType;
    var page = options.page;
    var candidate = this.data.candidate
    if (typeof (page) == 'undefined'){//来自添加投票的地方
      var thisCandidate = wx.getStorageSync("thisCandidate")
      if (thisCandidate == null || thisCandidate == "") {// 这个值不存在 说明 是 添加候选项   用于
        candidate.voteType = voteType
        if (voteType != 1) {//添加的时候根据 类型 初始化封面
          candidate.coverId = [0];
          candidate.cover = [""];
        }
        this.setData({
          pageStr:"addVote",
          voteType: voteType,
          candidate: candidate
        })
      } else {//////////////////////////修改候选项
        candidate = thisCandidate
        candidate.cover = new Array(candidate.cover)
        wx.removeStorage({
          key: 'thisCandidate'
        })
        this.setData({
          pageStr: "addVote",
          voteType: voteType,
          candidate: candidate
        })
      }
      
    }else{
      var voteId = options.voteId;//要添加的 投票 
      if (page == "detailed") {//来自 detailed 游客 申请报名 页面
        candidate.useFlag = 0;//报名还没有通过
      }
      if (voteType != 1) {//添加的时候根据 类型 初始化封面
        candidate.coverId = [0];
        candidate.cover = [""];
      }
      candidate.voteId = voteId
      candidate.wxUserId = options.wxUserId
      this.setData({
        pageStr: page,
        voteType: voteType,
        candidate: candidate
      })
    }
    
  },
  baocun(e) {
    var rules = null;
    var messages = null;
    var voteType = this.data.voteType;
    switch (parseInt(voteType)){
      case 0: /*图片*/
        rules = { name: { required: true, }, cover: { required: true, } }; messages = { name: { required: "请输入参赛名称", }, cover: { required: "请上传封面", } };
        break;
      case 1: /*文字*/
        rules = { name: { required: true, }, introduce: { required: true, } }; messages = { name: { required: "请输入参赛名称", }, introduce: { required: "请输入文字介绍", } };
        break;
      case 2: /*图文*/
        rules = { name: { required: true, }, introduce: { required: true, }, cover: { required: true, } }; messages = { name: { required: "请输入参赛名称", }, introduce: { required: "请输入文字介绍", }, cover: { required: "请上传封面", } };
        break;
    }
    var myWxValidate = this.initValidate(rules, messages)
    var candidate = this.data.candidate;
    var params = JSON.parse(JSON.stringify(this.data.candidate));
    params.cover = params.cover[0]

    // 传入表单数据，调用验证方法
    if (!myWxValidate.checkForm(params)) {
      const error = myWxValidate.errorList[0]
      dialog.showToastAutoError(error.msg);

      return false
    }


    var page = this.data.pageStr
    if (page === "addVote"){//
      wx.setStorageSync("thisCandidate", params)
      wx.navigateBack({
        delta: 1,
      })
    } else { // detailed 和 listCadidate
      var url = urls.urlobj.addCandidate;//添加候选项
      util.getServerData(url, candidate, null).then(function (data) {
        dialog.warnModalBack("成功", "报名成功",function(){
          wx.navigateBack({
            delta: 1,
          })
        });
      });
    }
  },
  initValidate(rules, messages) {
    // 创建实例对象
    return new WxValidate(rules, messages)
  },
  //图片上传
  previewImage(e) {
    var that = this;
    var candidate = this.data.candidate;
    var attr = e.target.dataset.attr;
    var index = e.target.dataset.index;
    var ids = candidate[`${attr}Id`]

    wx.chooseImage({
      count: 1,//暂时只写了单图上传
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
      success(res) {
        var tempFilePaths = res.tempFilePaths;
        util.fileImgChangeSave(tempFilePaths, ids, 1, attr, index, function (data, attr, index) {
          if (index == -1) {
            var filePath =  data.data.filePath
            candidate[`${attr}`].unshift(filePath)
            candidate[`${attr}Id`].unshift(data.data.id);
          } else {
            candidate[`${attr}`][index] = data.data.filePath;
            candidate[`${attr}Id`][index] = data.data.id;
          }
          that.setData({
            candidate: candidate
          })
        })
      }
    })
  },
  //删除文件
  fileImgDelete(e) {
    var that = this;
    var candidate = this.data.candidate;
    var attr = e.target.dataset.attr;
    var index = e.target.dataset.index;
    var ids = candidate[`${attr}Id`]
    var attrs = candidate[`${attr}`]
    util.fileImgDelete(ids[index], function () {
      ids.splice(index, 1)
      attrs.splice(index, 1)
      candidate[`${attr}`] = attrs
      candidate[`${attr}Id`] = ids
      that.setData({
        candidate: candidate
      })
    })

  },
  //图片上传end
  //所有为输入框的同步方法
  bindKeyInput(e) {
    var attr = e.currentTarget.dataset.attr
    var candidate = this.data.candidate;
    candidate[`${attr}`] = e.detail.value
    this.setData({
      candidate: candidate
    })
  },
})