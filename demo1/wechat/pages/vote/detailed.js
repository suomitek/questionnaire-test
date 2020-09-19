// pages/vote/detailed.js
var util = require('../../utils/util.js');
var urls = require('../../utils/urls.js');
var dialog = require('../../libs/dialog/dialog.js');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    loading:false,
    fengye: {
      draw: 1,
      length: 10,
      surface:"wj_candidate",
      nature:"orderNum",

      subscriberId: 0,
      subscriberSurface: "wx_user",
    },
    fengReturn: {
      draw: 1,
      length: 1,
      pageNum:1,
    },
    commentrecord:{
      subscriberSurface:"wx_user",
      subscriberId:0,
      fabulous:1,
      surfaceId:0,
      surface:"wj_comment"
    },
    host:urls.host,
    countDownList: [],
    actEndTimeList: [],
    commentList:[],
    colorStyle:{
      timebgcolor: "#999",
    },
    xiding: {
      topItem: "#xidingTop",
      thisItem: "#xiding",
      top: -1,// 
      xidingtop: 0,//上边的元素高度
      scrollTop: 0,//滚动距离
      windowHeight: 0,//屏幕高度
    },
    swiperTitle: [{
        text: "候选项"
      }, {
        text: "排行榜"
    }],
    currentPage: 0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var fengye = this.data.fengye;
    var wxUserInfo = wx.getStorageSync('wxUserInfo'); 
    fengye.subscriberId = wxUserInfo.id;
    var that = this;
    var id = options.id;
    // var id = 6;
    var url = urls.urlobj.voteDetailed;
    util.getServerData(url, {
      id: id,
      wxUserId: wxUserInfo.id
    }, null).then(function (data) {
      if (data.data.resultCode == '200') {
        // console.log(data.data.data);
        var voteVo = data.data.data
        voteVo.startTimeStr = voteVo.startTimeStr.replace(/-/g, '/');
        voteVo.endTimeStr = voteVo.endTimeStr.replace(/-/g, '/');
        if (voteVo.name.length>10){
          voteVo.name2 = voteVo.name.substring(0,10)+" . . .";
        }
        that.setData({
          wxUserInfo: wxUserInfo,
          voteVo: data.data.data
        })
        that.getColor();
        let endTimeList = [];
        that.setData({ actEndTimeList: endTimeList });
        that.countDown();
        that.xitingInit();
        if (voteVo.comment==1){//开启评论
          that.fetchCommentList(1);
        }
      }
    });
    
  },
  thumbs(e){
    var that = this;
    var url = urls.urlobj.thumbsSave;
    var index = e.currentTarget.dataset.index;
    var commentList = that.data.commentList;
    var commentId = commentList[index].id
    var wxUserInfo = this.data.wxUserInfo;
    var commentrecord = this.data.commentrecord;
    commentrecord.subscriberId = wxUserInfo.id;
    commentrecord.surfaceId = commentId;
    util.getServerData(url, commentrecord, null).then(function (data) {
      var flag = data.data.data
      commentList[index].fabulous = parseInt(commentList[index].fabulous) + flag;
      commentList[index].mark = parseInt(commentList[index].mark) + flag
      that.setData({
        commentList: commentList
      })
    });
  },
  onReachBottom() {
    if (this.data.comment == 0) {//开启评论
      return;
    }
    // 下拉触底，先判断是否有请求正在进行中
    // 以及检查当前请求页数是不是小于数据总页数，如符合条件，则发送请求
    if (!this.loading && this.data.fengReturn.pageNum >= this.data.fengye.draw) {
      this.fetchCommentList(this.data.fengye.draw + 1)
    }
  },
  fetchCommentList(pageNo) {
    var url = urls.urlobj.commentListJson;
    var fengye = this.data.fengye;
    var that = this;
    that.setData({ loading:true})
    util.getServerData(url, fengye , null).then(function (data) {
      var fengReturn = data.data.data
      fengye.draw = parseInt(fengye.draw)+1
      that.setData({ 
        loading: false,
        fengye: fengye,
        commentList: data.data.data.data,
        fengReturn: fengReturn
      })
    });
  },
  turnPage: function (e) {//tar切换
    this.setData({
      currentPage: e.currentTarget.dataset.index
    })
  },
  timeFormat(param) {//小于10的格式化函数
    return param < 10 ? '0' + param : param;
  },
  countDown() {//倒计时函数
    // 获取当前时间，同时得到活动结束时间数组
    let newTime = new Date().getTime();
    let endTimeList = [this.data.voteVo.startTimeStr, this.data.voteVo.endTimeStr];
    let countDownArr = [];

    // 对结束时间进行处理渲染到页面
    endTimeList.forEach(o => {
      let endTime = new Date(o).getTime();
      let obj = null;
      // 如果活动未结束，对时间进行处理
      if (endTime - newTime > 0) {
        let time = (endTime - newTime) / 1000;
        // 获取天、时、分、秒
        let day = parseInt(time / (60 * 60 * 24));
        let hou = parseInt(time % (60 * 60 * 24) / 3600);
        let min = parseInt(time % (60 * 60 * 24) % 3600 / 60);
        let sec = parseInt(time % (60 * 60 * 24) % 3600 % 60);
        obj = {
          day: this.timeFormat(day),
          hou: this.timeFormat(hou),
          min: this.timeFormat(min),
          sec: this.timeFormat(sec)
        }
      } else {//活动已结束，全部设置为'00'
        obj = {
          day: '00',
          hou: '00',
          min: '00',
          sec: '00'
        }
      }
      countDownArr.push(obj);
    })
    // 渲染，然后每隔一秒执行一次倒计时函数
    this.setData({ countDownList: countDownArr })
    //判断开没开始
    var voteVo = this.data.voteVo;
    if (countDownArr[0].day == "00" && countDownArr[0].hou == "00" && countDownArr[0].min == "00" && countDownArr[0].sec == "00"){
      //已将开始了
      if (countDownArr[1].day == "00" && countDownArr[1].hou == "00" && countDownArr[1].min == "00" && countDownArr[1].sec == "00") {
        //已将结束了
        voteVo.flag = 3
      }else{
        voteVo.flag = 2
      }
    }else{
      voteVo.flag = 1
    }
    this.setData({ voteVo: voteVo})
    setTimeout(this.countDown, 1000);
  },
  getColor(){
    var colorStyle = {
      timebgcolor: "#f1f1f1",
      weikaishi: "#9f10f1",
      jinxingzhong: "#38c100",
      yijishu: "#f11020",
      zanting:"#f19410"
    }
    // console.log(colorStyle)
    this.setData({
      colorStyle: colorStyle
    })
  },
  govote: function () {//添加投票 
    wx.navigateTo({
      url: '/pages/add/addvote/selectStyle'
    })
  },
  addCandidate: function (e) {//添加投票选手 
    var enlist = e.currentTarget.dataset.enlist;
    if (enlist == -1) {
      dialog.warnModal("错误", "您已经被拒绝了");
      return;
    }
    if (enlist == -2) {
      dialog.warnModal("错误", "您已经申请过请等待审核");
      return;
    }
    var voteType = this.data.voteVo.voteType
    var voteId = this.data.voteVo.id
    var wxUserInfo = wx.getStorageSync('wxUserInfo');
    wx.navigateTo({
      url: '/pages/add/addvote/addCandidate?page=detailed&voteType=' + voteType + "&voteId=" + voteId + "&wxUserId=" + wxUserInfo.id
    })
  },
  listCandidate: function (e) {//管理投票选手
    var list = e.currentTarget.dataset.list;
    var voteId = this.data.voteVo.id
    wx.navigateTo({
      url: '/pages/add/addvote/listCandidate?voteId=' + voteId
    })
  },
  goxiangqin: function () {//查看候选项
    var voteVo = this.data.voteVo;
    wx.setStorageSync("thisVoteVo", voteVo)
    wx.navigateTo({
      url: '/pages/vote/xiangqin'
    })
  },
  gocandidate: function (e) {
    var index = e.currentTarget.dataset.index;
    var list = e.currentTarget.dataset.list;
    var voteVo = this.data.voteVo;
    wx.setStorageSync("thisVoteVo", voteVo)
    wx.navigateTo({ 
      url: '/pages/vote/candidate?index=' + index + "&list=" + list
    })
  },
  onPageScroll: function (ev) {
    this.xiting(ev)
  },
  xitingInit() {
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        // 高度,宽度 单位为px
        var xiding = that.data.xiding
        xiding.windowHeight = res.windowHeight
        that.setData({
          xiding: xiding
        })
      }
    })
  },
  xiting(ev) {
    var that = this;
    var xidingtop = this.data.xiding.xidingtop;
    var top = this.data.xiding.top;
    var xiding = this.data.xiding;
    const query2 = wx.createSelectorQuery()
    query2.select(xiding.topItem).boundingClientRect(function (rect) {
      var xiding = that.data.xiding
      xiding.xidingtop = rect.height//上边的元素高度
      that.setData({
        xiding: xiding
      })
    }).exec();

    const query = wx.createSelectorQuery()
    query.select(xiding.thisItem).boundingClientRect();
    query.selectViewport().scrollOffset()
    query.exec(function (res) {
      setTimeout(function () {
        top = parseInt(ev.scrollTop)
        var xiding = that.data.xiding
        xiding.scrollTop = ev.scrollTop//上边的元素高度
        xiding.top = top//上边的元素高度
        that.setData({
          xiding: xiding
        })
      }, 0)
    })
  }
})