// pages/vote/candidate.js
var app = getApp();
var urls = require('../../utils/urls.js');
var util = require('../../utils/util.js');
var bmapLocation = require('../../libs/bmapLocation.js');
var redis = require('../../libs/redis.js');
var dialog = require('../../libs/dialog/dialog.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    locationLogin: false,
    host: urls.host,
    code:"",
    inputCode:true, 
    comment: {//品论
      subscriberId: 0,
      subscriberSurface: "wx_user",//主人表
      subscriberNature:"avatarUrl,nickName",
      comment: "",
      useless: 0,
      fabulous: 0,
      surface: "wj_candidate",//被评论表
      nature: "orderNum",//被评论 属性
      surfaceId: 0,//被评论对象id
      costSurface: "wj_voterecord",//花费 
      costNature: "id",
      costSurfaceId: 0
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var index = options.index;
    var list = options.list;
    var thisVoteVo = wx.getStorageSync("thisVoteVo");
    wx.removeStorage({
      key: 'thisVoteVo'
    })
    var candidate = thisVoteVo[`${list}`][index];
    if (list == "candidateVos"){
      for (var i = 0; i < thisVoteVo.candidateVo2s.length;i++){
        if (thisVoteVo.candidateVo2s[i].id == candidate.id){
          candidate.paiming=i+1
          break;
        }
      }
    }else{
      candidate.paiming = index+1
    }
    this.setData({ 
      thisVoteVo: thisVoteVo,
      candidate: candidate
      })
    // wx.removeStorage({
    //   key: 'thisVoteVo'
    // })
  },
  toupiao(){
    var that = this;
    var wxUserInfo = wx.getStorageSync('wxUserInfo');
    var locationInfo = redis.get('locationInfo');
    var candidate = this.data.candidate;
    var vote = this.data.thisVoteVo
    var code = this.data.code;
    if (vote.suspend != 1) {//不为1就是暂停
      dialog.warnModal("错误", "活动暂停");
      return;
    }
    var data = {
      candidateId: candidate.id,
      wxUserId: wxUserInfo.id,
    }


    if (vote.overt != 1 && code == "") {//不公开且 投票码没有
      this.showInputCode()
      return;
    }
    data.code = code//设置投票码

    
    if (vote.restrictFlag == 1 && locationInfo==null) {//有地区限制
      if (that.data.locationLogin==false){
        this.getLocation(this.toupiao)//
        return;
      }
     
    }
    if (vote.restrictFlag == 1) {//有地区限制  设置地址信息
      data.province = locationInfo.cityInfo.province;
      data.city = locationInfo.cityInfo.city;
      data.district = locationInfo.cityInfo.district;
      data.street = locationInfo.cityInfo.street;
      data.streetNumber = locationInfo.cityInfo.streetNumber;
      data.latitude = locationInfo.latitude;
      data.longitude = locationInfo.longitude;
    }    
    var url = urls.urlobj.voterecordAddSave;
    util.getServerData(url, data, null).then(function (data) {
      if (data.data.resultCode == '200') {
        if (data.data.data.canFlag==1){//1为还有对候选项评论过  0 为已经品论过 或者没有开启评论
          dialog.warnModalTrueBack("提示", "投票成功,是否对候选项去评论?", function (flag) {
            if (flag == 1) { // 评论界面
              var comment = that.data.comment;
              comment.costSurfaceId = data.data.data.canFlag;
              comment.subscriberId = wxUserInfo.id;
              comment.surfaceId = candidate.id
              redis.put("comment", comment, 60)//60秒
              var title = "对" + candidate.name + "评价";
              var msg = "对" + candidate.name + "评价";
              wx.navigateTo({
                url: '/pages/comment/comment?title=' + title + "&msg=" + msg,
              })
            }
          })
        }else{
          dialog.showToastAutoSuccess("投票成功")
        }
      }
    });
  },
  getLocation(calldack){
    var that = this;
    that.setData({
      locationLogin:true//正在定位
    })
    wx.getSetting({
      success: function (res) {
        if (res.authSetting['scope.userLocation'] != true) {
          //用户没授权过
          wx.authorize({
            scope: 'scope.userLocation',
            success(res) {
              bmapLocation.getLocation(app.globalData.bmpAk, 0, function (flag) {
                if (flag == 1) {
                  that.setData({
                    locationLogin: false//正在定位
                  })
                  calldack()
                } else {
                  dialog.warnModal("错误", "定位失败");
                }
              })
            },
            fail(res) {//拒绝后再次授权
              wx.openSetting({
                success(res) {
                  if (res.authSetting["scope.userLocation"]) {
                    // res.authSetting["scope.userLocation"]为trueb表示用户已同意获得定位信息，此时调用getlocation可以拿到信息
                    bmapLocation.getLocation(app.globalData.bmpAk, 0, function (flag) {
                      if (flag == 1) {
                        that.setData({
                          locationLogin: false//正在定位
                        })
                        calldack()
                      } else {
                        dialog.warnModal("错误", "定位失败");
                      }
                    })
                  }
                }
              })
            }
          })
        } else {
          //用户授权过
          bmapLocation.getLocation(app.globalData.bmpAk, 0, function (flag) {
            if (flag == 1) {
              that.setData({
                locationLogin: false//正在定位
              })
              calldack()
            } else {
              dialog.warnModal("错误", "定位失败");
            }
          })
        }
      }
    })
    
  },
  //投票码
  input: function (e) {
    this.setData({ code: e.detail.value })
  },
  showInputCode(){
    this.setData({ inputCode: false })
  },
  model2confirm: function (e) {
    if (this.data.code==""){
      wx.showToast({
        title: '请输入投票密码',
        icon: 'none'
      })
    }else{
      this.toupiao()
      this.setData({ inputCode: true })
    }
  },
  model2cancel: function (e) {
    this.setData({ inputCode: true })
  }
})