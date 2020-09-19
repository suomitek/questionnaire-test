// pages/add/addvote/addvote.js
var dateTimePicker = require('../../../libs/dateTimePicker.js');
var citesPicker = require('../../../libs/citesPicker.js');
var dialog = require('../../../libs/dialog/dialog.js');
import WxValidate from '../../../libs/WxValidate'
var util = require('../../../utils/util.js'); 
var urls = require('../../../utils/urls.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    save: false,//是否保存了 //用于hide页面 不放入草稿
    draft: 0,//是否产生草稿  只在第一步的地方判断生成就行
    host:urls.host,
    //进度
    currentschedule: 0,//默认0 有草稿用草稿的 
    showColorPicker1: false,
    colorData1: {
      //基础色相(色盘右上顶点的颜色)
      hueData: {
        colorStopRed: 255,
        colorStopGreen: 0,
        colorStopBlue: 0,
      },
      //选择点的信息
      pickerData: {
        x: 0,
        y: 480,
        red: 0,
        green: 0,
        blue: 0,
        hex: '#000000'
      },
      //色相控制条位置
      barY: 0
    },
    rpxRatio: 1, //单位rpx实际像素
    host: urls.host,
    scheduleList: [{
      title: "活动内容"
    },{
      title: "投票设置"
    }, {
      title: "候选项"
    }, {
      title: "活动设置"
    }],
    vote:{
      name: "",
      coverId:[0],
      cover:[""],
      imgIntroduce: [],
      imgIntroduceId: [],
      rewarImg: [],
      rewarImgId: [],
      candidate:[],
      themeColor:"#000",
      sponsor:0,
      reward:0,
      repeatFlag:0,
      restrictFlag:0,//地区限制
      comment:1,//开启评论
      overt:1,//默认公开投票
      outside:0,//不开放报名
      startTimeStr: util.timeToStr(new Date()),
      endTimeStr: util.timeToStr(new Date()),
      everyNum: 1,
      voteType: 2,
      province: "不限",
      city: "不限",
      district: "不限",
    },
    //次数,同微信数
    numList: [1,2,3,4,5,6,7,8,9,10],
    numIndex: 0,
    ipIndex:0,
    //时间
    startYear: 2000,
    endYear: 2050,
    //侧滑删除
    startX: 0, //开始坐标
    startY: 0,
  },
  //
  onShow: function () {
    var thisCandidate = wx.getStorageSync("thisCandidate")
    if (thisCandidate == null || thisCandidate == "") {// 这个值不存在 说明不是从 添加页面 来的
      return;
    }
    var vote = this.data.vote;
    var thisCandidateIndex = wx.getStorageSync(`thisCandidateIndex`);
    if (thisCandidateIndex == null || thisCandidateIndex == "") {// 添加候选项界面  的添加 来的
      vote.candidate.unshift(thisCandidate)//插入
    }else{// 添加候选项界面  的编辑 来的
      vote.candidate.splice(parseInt(thisCandidateIndex), 1, thisCandidate)//替换
      wx.removeStorage({
        key: 'thisCandidateIndex'
      })
    }
    this.setData({
      vote: vote
    })
    wx.removeStorage({
      key: 'thisCandidate'
    })
    this.setDraft(1, this.data.vote.voteType)//保存
  },
  onHide: function () {
    if (this.data.save==false){//是否保存过的标志
      this.setDraft(1, this.data.vote.voteType)//保存
    }
  },
  setDraft(flag, voteType){ // 设置草稿
    if (flag==1){//保存
      var addVoteData = this.data
      if (addVoteData.draft==1){//有草稿
        wx.setStorageSync(`addVoteData${voteType}`, this.copyData(addVoteData))//只保存需要的数据
      }
      
    }else{ // 删除草稿
      var key = `addVoteData${voteType}`;
      wx.removeStorageSync(key)
    }
  },
  //草稿数据，复制数据
  copyData(addVoteData){
    var voteData={
      currentschedule: addVoteData.currentschedule,
      draft: addVoteData.draft,
      showColorPicker1: addVoteData.showColorPicker1,
      colorData1: addVoteData.colorData1,
      rpxRatio: addVoteData.rpxRatio,
      vote: addVoteData.vote,
      numIndex: addVoteData.numIndex,
      ipIndex: addVoteData.ipIndex,
      dateTime: addVoteData.dateTime,
      dateTimeArray: addVoteData.dateTimeArray,
      dateTimeArray1: addVoteData.dateTimeArray1,
      dateTime1: addVoteData.dateTime1,
      citesSelects: addVoteData.citesSelects,
      thisCites: addVoteData.thisCites,
    }
    return voteData;
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var voteType = options.voteType;
   
    var addVoteData = wx.getStorageSync(`addVoteData${voteType}`)//取出一个类型一样的草稿
    if (addVoteData != null && addVoteData != ""){//有草稿 
      dialog.warnModalTrueBack("提示", "是否使用草稿?", function (flag) {
        if (flag == 1) {
          that.setData(that.copyData(addVoteData))
          return;
        }else{
          that.setDraft(0, voteType)//不要草稿 要 删除
        }
      })
    }
    ///////////////////////////////////////////////// 内存中没有草稿
    var vote = this.data.vote;
    // vote.voteType = options.voteType
    vote.voteType = options.voteType;
    var time = new Date().getTime();
    vote.startTimeStr = time
    vote.endTimeStr = time
    this.setData({
      vote: vote
    })

    //初始化两个时间选择器
    var obj = dateTimePicker.dateTimePicker(this.data.startYear, this.data.endYear);
    var obj1 =dateTimePicker.dateTimePicker(this.data.startYear, this.data.endYear);

    this.setData({
      dateTime: obj.dateTime,
      dateTimeArray: obj.dateTimeArray,
      dateTimeArray1: obj1.dateTimeArray,
      dateTime1: obj1.dateTime
    });

    //初始化不限地区选择器
    var data = citesPicker.liebiaogenxi("北京", "北京", "不限", 0);

    this.setData({
      citesSelects: data.citesSelects,
      thisCites: data.thisCites
    })
    //拾色器初始化
    wx.getSystemInfo({
      success(res) {
        that.setData({
          rpxRatio: res.screenWidth / 750
        })
      }
    })
  }, 
  //添加候选人
  addCandidate:function(){
    var voteType = this.data.vote.voteType;
    wx:wx.navigateTo({
      url: '/pages/add/addvote/addCandidate?voteType=' + voteType,
    })
  },
  editCandidate: function (e) {
    var index = e.currentTarget.dataset.index;
    var thisCandidate = this.data.vote.candidate[index];
    wx.setStorageSync(`thisCandidate`, thisCandidate)
    wx.setStorageSync(`thisCandidateIndex`, index+"")
    var voteType = this.data.vote.voteType;
    wx: wx.navigateTo({
      url: '/pages/add/addvote/addCandidate?voteType=' + voteType,
    })
  },
  //侧滑删除
  touchstart: function (e) {
    //开始触摸时 重置所有删除
    var vote = this.data.vote;
    var candidate = vote.candidate
    candidate.forEach(function (v, i) {
      if (v.isTouchMove)//只操作为true的
        v.isTouchMove = false;
    })
    vote.candidate = candidate;
    this.setData({
      startX: e.changedTouches[0].clientX,
      startY: e.changedTouches[0].clientY,
      vote: vote
    })
  },
  touchmove: function (e) {
    var that = this,
      index = e.currentTarget.dataset.index,//当前索引
      startX = that.data.startX,//开始X坐标
      startY = that.data.startY,//开始Y坐标
      touchMoveX = e.changedTouches[0].clientX,//滑动变化坐标
      touchMoveY = e.changedTouches[0].clientY,//滑动变化坐标
      //获取滑动角度
      angle = that.angle({ X: startX, Y: startY }, { X: touchMoveX, Y: touchMoveY });
    that.data.vote.candidate.forEach(function (v, i) {
      v.isTouchMove = false
      //滑动超过30度角 return
      if (Math.abs(angle) > 30) return;
      if (i == index) {
        if (touchMoveX > startX) //右滑
          v.isTouchMove = false
        else //左滑
          v.isTouchMove = true
      }
    })
    //更新数据
    that.setData({
      vote: that.data.vote
    })
  },
  angle: function (start, end) {
    var _X = end.X - start.X,
      _Y = end.Y - start.Y
    //返回角度 /Math.atan()返回数字的反正切值
    return 360 * Math.atan(_Y / _X) / (2 * Math.PI);
  },
  //删除事件
  del: function (e) {
    this.data.vote.candidate.splice(e.currentTarget.dataset.index, 1)
    this.setData({
      vote: this.data.vote
    })
  },
  //图片上传
  previewImage(e) {
    var that = this;
    var vote = this.data.vote;
    var attr = e.target.dataset.attr;
    var index = e.target.dataset.index;
    var ids = vote[`${attr}Id`]

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
    var vote = this.data.vote;
    console.log(index)
    if (index==-1){
      var filePath = data.data.filePath
      vote[`${attr}`].unshift(filePath)
      vote[`${attr}Id`].unshift( data.data.id);
    }else{
      vote[`${attr}`][index] = data.data.filePath;
      vote[`${attr}Id`][index] = data.data.id;
    }
    this.setData({
      draft: 1,//生成草稿
      vote: vote
    })
  },
  //删除文件
  fileImgDelete(e){
    var that = this;
    var vote = this.data.vote;
    var attr = e.target.dataset.attr;
    var index = e.target.dataset.index;
    var ids = vote[`${attr}Id`]
    var attrs = vote[`${attr}`]
    // util.fileImgDelete(ids[index], function (){///不删除服务器的图片
      ids.splice(index, 1)
      attrs.splice(index, 1)
      vote[`${attr}`] = attrs
      vote[`${attr}Id`] = ids
      that.setData({
        vote: vote
      })
    // })
    
  },
  //图片上传end

  //保存地区变化
  changeCites(e) {
    var vote = this.data.vote;
    var citesSelects = this.data.citesSelects
    vote.province = citesSelects[0][e.detail.value[0]];
    vote.city = citesSelects[1][e.detail.value[1]];
    vote.district = citesSelects[2][e.detail.value[2]];
    this.setData({ thisCites: e.detail.value, vote: vote });
  },
   // 选择省市区函数
  //子级别 地区变化
  changeCitesColumn(e) {
    var arr = this.data.thisCites
    var dateArr = this.data.citesSelects;
    arr[e.detail.column] = e.detail.value;
    var data = citesPicker.liebiaogenxi(dateArr[0][arr[0]], dateArr[1][arr[1]], dateArr[2][arr[2]], e.detail.column);
    this.setData({
      citesSelects: data.citesSelects,
      thisCites: data.thisCites
    })
  },
  //每个ip可投票微信号数量
  changeIPCiShu(e) {
    var vote = this.data.vote;
    vote.ipWxUserFrequency = this.data.numList[e.detail.value];
    this.setData({
      vote: vote,
      ipIndex: e.detail.value
    })
  },
  //每日投票次数
  changeCiShu(e) {
    var vote = this.data.vote;
    vote.frequency = this.data.numList[e.detail.value];
    this.setData({
      vote:vote,
      numIndex: e.detail.value
    })
  },
  
  //地区ip限制
  changeRrestrict(e) {
    var vote = this.data.vote;
    if (e.detail.value==false) {
      vote.restrictFlag = 0
    } else {
      vote.restrictFlag = 1
      //初始化不限地区选择器
      var data = citesPicker.liebiaogenxi("北京", "北京", "不限", 0);
      vote.province = "北京";
      vote.city = "北京";
      vote.district = "不限";
      this.setData({
        ipIndex:0,
        citesSelects: data.citesSelects,
        thisCites: data.thisCites
      })
    }
    this.setData({ vote: vote })
  },
  // 拾色器
  onChangeColor(e) {
    const index = e.target.dataset.id
    this.setData({
      [`colorData${index}`]: e.detail.colorData
    })
    var vote = this.data.vote;
    vote.themeColor = e.detail.colorData.pickerData.hex
    this.setData({ vote: vote })
  },
  toggleColorPicker(e) {
    const index = e.currentTarget.dataset.id
    this.setData({
      [`showColorPicker${index}`]: !this.data[`showColorPicker${index}`]
    })
    // this.hideVote()
  },
  closeColorPicker() {
    this.setData({
      showColorPicker1: false
    })
    // this.showVote()
  },
  //方便文本框内容的不会置顶
  // hideVote(){
  //   console.log("hideVote")
  //   var vote = this.data.vote;
  //   var voteHide = this.data.vote;
  //   vote.name = " "
  //   vote.introduce = " "
  //   this.setData({ 
  //     vote: vote,
  //     voteHide: voteHide
  //   })
  // },
  // showVote(){
  //   console.log("showVote")
  //   var voteHide = this.data.voteHide;
  //   this.setData({
  //     vote: voteHide
  //   })
  // },
  //所有为输入框的同步方法
  bindKeyInput(e) {
    var attr = e.currentTarget.dataset.attr
    var vote = this.data.vote;
    vote[`${attr}`] = e.detail.value
    this.setData({
      draft: 1,//生成草稿
      vote: vote
    })
  },
  // 所有的switch
  changeSwitch(e) {
    var attr = e.currentTarget.dataset.attr
    var vote = this.data.vote;
    if (e.detail.value) {
      vote[`${attr}`] = 1
    } else {
      vote[`${attr}`] = 0
    }
    this.setData({ vote: vote })
  },
  //开始时间变化
  changeDateTime(e) {
    this.setData({ dateTime: e.detail.value });
    this.compare(0)
  },
  //开始时间变化
  changeDateTimeColumn(e) {
    var arr = this.data.dateTime, dateArr = this.data.dateTimeArray;
    arr[e.detail.column] = e.detail.value;
    dateArr[2] = dateTimePicker.getMonthDay(dateArr[0][arr[0]], dateArr[1][arr[1]]);
    this.setData({
      dateTimeArray: dateArr
    });
  },
  //结束时间变化
  changeDateTime1(e) {
    this.setData({ dateTime1: e.detail.value });
    this.compare(1)
  },
  //结束时间变化
  changeDateTimeColumn1(e) {
    var arr1 = this.data.dateTime1, dateArr1 = this.data.dateTimeArray1;
    arr1[e.detail.column] = e.detail.value;
    dateArr1[2] = dateTimePicker.getMonthDay(dateArr1[0][arr1[0]], dateArr1[1][arr1[1]]);
    this.setData({
      dateTimeArray1: dateArr1
    });
  },
  //核对开始，结束时间
  compare: function (flag) {//flag ==  0  变开始  flag ==  1  变结束
    var dateTime1 = this.data.dateTime1
    var dateArr = this.data.dateTimeArray
    var dateArr1 = this.data.dateTimeArray1
    var dateTime = this.data.dateTime
    var time = util.timeStrTostamp(dateArr[0][dateTime[0]] + "-" + dateArr[1][dateTime[1]] + "-" + dateArr[2][dateTime[2]] + " "
      + dateArr[3][dateTime[3]] + ":" + dateArr[4][dateTime[4]] + ":" + dateArr[5][dateTime[5]]);

    var time1 = util.timeStrTostamp(dateArr1[0][dateTime1[0]] + "-" + dateArr1[1][dateTime1[1]] + "-" + dateArr1[2][dateTime1[2]] + " "
      + dateArr1[3][dateTime1[3]] + ":" + dateArr1[4][dateTime1[4]] + ":" + dateArr1[5][dateTime1[5]]);
    if (flag == 0){
      if (parseInt(time) > parseInt(time1)) {//开始比结束更晚
        var temp = new Array(dateTime.length);
        for (var i = 0; i < dateTime.length; i++) {
          temp[i] = dateTime[i]
        }
        this.setData({ dateTime1: temp });
        time1 = time
      }
    }else{
      if (parseInt(time) > parseInt(time1)) {//结束比开始更早
        var temp = new Array(dateTime1.length);
        for (var i = 0; i < dateTime1.length; i++) {
          temp[i] = dateTime1[i]
        }
        this.setData({ dateTime: temp });
        time = time1
      }
    }
    var vote = this.data.vote;
    vote.startTimeStr = time;
    vote.endTimeStr = time1;
    this.setData({ vote: vote });
  },
  //上一页
  turnSchedule0: function () {
    var currentschedule = this.data.currentschedule-1;
    this.setData({
      currentschedule: currentschedule
    })
  },
  //下一页
  turnSchedule1: function () {
    var currentschedule = this.data.currentschedule + 1;
    if (this.check()){//验证没问题就保存
      this.setData({
        currentschedule: currentschedule
      })
      this.setDraft(1, this.data.vote.voteType)//保存草稿
    }
  },
  check(){
    var vote = this.data.vote;
    var currentschedule = this.data.currentschedule
    var rules = {};
    var messages = {};
    switch (parseInt(currentschedule)) {
      case 3: /* 活动设置 */
        var endTime = new Date(vote.endTimeStr).getTime();
        var time = new Date().getTime();
        var scour = endTime - time;
        if (scour<60*5*1000){
          dialog.warnModal("错误","结束时间到现在至少为5分钟");
          return false;
        }
        if (vote.sponsor == 1) {//显示主办方
          //验证主办方信息
          messages.sponsorName = {
            required: '请输入主办方名称'
          }
          rules.sponsorName = {
            required: true
          }
          messages.sponsorPhone = {
            required: '请输入主办方电话',
            tel: '主办方电话错误',
          }
          rules.sponsorPhone = {
            required: true,
            tel: true
          }
        }
        if (vote.reward == 1) {//显示活动奖励
          //验证活动奖励说明
          messages.rewardDesc = {
            required: '请输入奖励说明'
          }
          rules.rewardDesc = {
            required: true
          }
        }
        if (vote.reward == 0 && vote.sponsor == 0) {
          return true;
        }
        break;
      case 2: /*候选项*/ 
        if (vote.candidate.length < 2) { // 候选项不能小于 2  至少两项
          dialog.showToastAutoError("至少有两个候选项");
          return false
        }
        break;
      case 1: /*投票设置      自己没有需要验证的*/  
        return true;
        break;

      case 0: /*活动内容*/
        messages = { name: { required: "请输入投票名称", maxlength: "投票名称长度不多于15位" }, introduce: { required: "请输入投票介绍" }, cover: { required: "请输入投票封面" } }; rules = { name: { required: true, maxlength: 20, }, introduce: { required: true }, cover: { required: true } };
        break;
    }
    var myWxValidate = new WxValidate(rules, messages)

    var params = JSON.parse(JSON.stringify(this.data.vote));
    params.cover = params.cover[0]

    // console.log(WxValidate)

    // 传入表单数据，调用验证方法
    if (!myWxValidate.checkForm(params)) {
      const error = myWxValidate.errorList[0]
      dialog.showToastAutoError(error.msg);
      return false
    }
    return true;
  },
  addSaveVote(){
    this.setDraft(1, this.data.vote.voteType)//保存草稿
    var wxUserInfo = wx.getStorageSync('wxUserInfo');
    var that = this;
    if (this.check()) {//验证没问题就保存
      var url = urls.urlobj.voteAddSave;
      var voteDataStr = JSON.stringify(this.copyData(this.data));
      util.getServerData(url, {
          voteDataStr: voteDataStr,
          wxUserId:wxUserInfo.id
        }, null).then(function (data) {
        if (data.data.resultCode == '200') {
          // that.setDraft(0, that.data.vote.voteType)//  添加成功 后 草稿 要 删除、、、、、、、、、、、、、、、、、测试测时候不删除先
          that.setData({ // 是否保存过的标志
            save: true
          })
          dialog.warnModalTrueBack("提示", "前往投票界面?", function (flag) {
            if (flag == 1) { // 投票界面
              wx.reLaunch({// 关闭所有页面 不在让他往 回跳了
                url: '/pages/vote/detailed?id=' + data.data.data.id,
              })
            } else { // 首页
              wx.reLaunch({//关闭所有页面 不在让他往 回跳了
                url: '/pages/index/index?currentPage=0&voteid=' + data.data.data.id,
              })
            }
          })
        }
      });
    }
  },
})