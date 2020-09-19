// pages/wenjuan/analysis/analysis.js
var urls = require('../../../utils/urls.js');
var util = require('../../../utils/util.js');
var dialog = require('../../../libs/dialog/dialog.js');
var redis = require('../../../libs/redis.js');
import * as echarts from '../../../ec-canvas/echarts';

const app = getApp();


Page({
  /**
   * 页面的初始数据
   */
  data: {
    host: urls.host,
    swiperTitle: [{
      text: "问卷数据"
    }, {
        text: "答题分析"
      }, {
        text: "答卷列表"
      }],
    currentPage: 0,
    gauge: {
      lazyLoad: true
    },
    line: {
      lazyLoad: true
    },
    ip:"",
    latitude:"",
    longitude: "",
    markers: [{
      id: '1',
      latitude: '38.850000',
      longitude: '115.484973',
      iconPath: '../../../imgs/add/didian_icon.png'
    }],
  },
  onShow(){
    var index = redis.get("index");
    if(index!=null){
      redis.remove("index");
      var problems = this.data.problems;
      problems[index].genre=1;
      this.setData({
        problems: problems
      })
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.ecComponent = this.selectComponent('#mychart-dom-gauge');//完成进度图
    this.lineComponent = this.selectComponent('#mychart-dom-line');//记录线性图
    var id = options.id;
    var that = this;
    var url = urls.urlobj.analysisQuestion;
    util.getServerData(url, {
      id: id
    }, null).then(function (res) {
      var question=res.data.data.question;
      var problems = res.data.data.problems;
      for(var x=0;x<problems.length;x++){
        problems[x]["lv"] = (problems[x].answerNumber / question.answerNumber).toFixed(4) * 100
        if (problems[x]["lv"]==null){
          problems[x]["lv"] =0
        }
        ///////////每道题的得分率
        problems[x]['fractionLv'] = (problems[x].fraction / (problems[x].score * problems[x].answerNumber)).toFixed(4) * 100
        if (problems[x]['fractionLv'] == null) {
          problems[x]['fractionLv'] = 0
        }
        ///////////每道题的平均分
        problems[x]['average'] = (problems[x].score * problems[x]['fractionLv']).toFixed(4) / 100


        for (var y = 0; y < problems[x].choices.length; y++) {
          problems[x].choices[y]["lv"] = (problems[x].choices[y].selectNum / problems[x].answerNumber).toFixed(4)*100
          if (problems[x].choices[y]["lv"] == null) {
            problems[x].choices[y]["lv"] = 0
          }
        }
      }
      var replyMarkers = []

      var replyVos = res.data.data.replyVos;
      for (var x = 0; x < replyVos.length; x++) {
        replyVos[x]['hide'] = true;
        replyVos[x]['createTimeStr'] = util.timeToStr(new Date(replyVos[x].createTime));
        if (res.data.data.question.restrictFlag == 1){//////限制地区
          replyMarkers.push({
            id: x,
            latitude: replyVos[x].latitude,
            longitude: replyVos[x].longitude,
            iconPath: '../../../imgs/add/didian_icon.png'
          })
        }
        
      }
      if (replyVos.length>0){//默认第一个打开
        replyVos[0]['hide'] = false
      }
      if (res.data.data.question.restrictFlag == 1) {//////限制地区
        that.setData({
          
        })
      }
      that.setData({
        lv: res.data.data.lv,
        replyVos: replyVos,
        replyMarkers: replyMarkers,
        question: res.data.data.question,
        problems: res.data.data.problems,
        chartLines: res.data.data.chartLines,
      })
      that.init()
    });
  },
  detailed(e) {
    var index = e.currentTarget.dataset.index;
    var questionId = this.data.replyVos[index].questionId;
    var wxUserId = this.data.replyVos[index].wxUserId;
    // redis.remove("choices")
    wx.navigateTo({
      url: '/pages/wenjuan/detailed?wxUserId=' + wxUserId + "&id=" + questionId,
    })
  },
  selectIp(e){
    var ip = e.currentTarget.dataset.ip;
    this.setData({
      ip:ip
    })
  },
  xiangqing(e){
    var index = e.currentTarget.dataset.index;
    var tihao = index+1;
    var name = this.data.problems[index].name;
    var problemId = this.data.problems[index].id;
    var qType = this.data.problems[index].qType;
    var examineFlag = this.data.question.examineFlag;
    wx.navigateTo({
      url: '/pages/wenjuan/analysis/analysis/wenben/index?name=' + name 
      + "&problemId=" + problemId + "&tihao=" + tihao + "&qType=" + qType
        + "&examineFlag=" + examineFlag + "&index=" + index,
    })
  },
  map(e){
    var that = this;
    var markers = this.data.markers;
    markers[0].longitude = e.currentTarget.dataset.longitude;
    markers[0].latitude = e.currentTarget.dataset.latitude;
    that.setData({
      markers: markers,
      latitude: e.currentTarget.dataset.latitude,
      longitude: e.currentTarget.dataset.longitude
    })
  },
  maskHide(){
    this.setData({
      ip: "",
      latitude:"",
      longitude:"",
    })
  },
  open(e) {
    var index = e.currentTarget.dataset.index;
    var replyVos = this.data.replyVos;
    replyVos[index].hide = !replyVos[index].hide;
    this.setData({
      replyVos: replyVos
    })
  },
  call(e) {
    var phone = e.currentTarget.dataset.phone;
    var name = e.currentTarget.dataset.name;
    wx.showActionSheet({
      itemList: [name + ":" + phone, '呼叫'],
      success: function (res) {
        if (res.tapIndex == 1) {
          wx.makePhoneCall({
            phoneNumber: phone,
          })
        }
      }
    })
  },
  bar(e) {
    var index = e.currentTarget.dataset.index;
    var problem = this.data.problems[index];
    redis.put("problem", problem, 6000)
    // redis.remove("choices")
    wx.navigateTo({
      url: '/pages/wenjuan/analysis/analysis/bar/index',
    })
  },
  pie(e) {
    var index = e.currentTarget.dataset.index;
    var problem = this.data.problems[index];
    redis.put("problem", problem, 6000)
    // redis.remove("choices")
    wx.navigateTo({
      url: '/pages/wenjuan/analysis/analysis/pie/index',
    })
  },
  // 点击按钮后初始化图表
  init: function () {
    var that = this;
    this.ecComponent.init((canvas, width, height) => {
      // 获取组件的 canvas、width、height 后的回调函数
      // 在这里初始化图表
      const chart = echarts.init(canvas, null, {
        width: width,
        height: height
      });
      that.setOption(chart);

      // 将图表实例绑定到 this 上，可以在其他成员函数（如 dispose）中访问
      this.chart = chart;

      // 注意这里一定要返回 chart 实例，否则会影响事件处理等
      return chart;
    });
    this.lineComponent.init((canvas, width, height) => {
      // 获取组件的 canvas、width、height 后的回调函数
      // 在这里初始化图表
      const chart = echarts.init(canvas, null, {
        width: width,
        height: height
      });
      that.setLineOption(chart);

      // 将图表实例绑定到 this 上，可以在其他成员函数（如 dispose）中访问
      this.chart = chart;

      // 注意这里一定要返回 chart 实例，否则会影响事件处理等
      return chart;
    });
  },
  setOption(chart) {////////////////////////////////////////时间进度图
    var lv = this.data.lv;
    var option = {
      title: {
        text: '时间进度图',
        left: 'center'
      },
      backgroundColor: "#ffffff",
      color: ["#37A2DA", "#32C5E9", "#67E0E3"],
      series: [{
        name: '完成进度',
        type: 'gauge',
        detail: {
          formatter: '{value}%'
        },
        axisLine: {
          show: true,
          lineStyle: {
            width: 30,
            shadowBlur: 0,
            color: [
              [0.3, '#67e0e3'],
              [0.7, '#37a2da'],
              [1, '#fd666d']
            ]
          }
        },
        data: [{
          value: lv,
          name: '完成率',
        }]

      }]
    };

    chart.setOption(option);
  },
  setLineOption(chart) {////////////////////////////////////////投票数量图
    var chartLines = this.data.chartLines;
    var xAxisData = [];
    var seriesData = [];
    for (var i = 0; i < chartLines.length; i++) {
      xAxisData.push(chartLines[i].name)
      seriesData.push(chartLines[i].num)
    }
    
    var option = {
      title: {
        text: '答卷数量历史曲线',
        left: 'center'
      },
      backgroundColor: "#ffffff",
      color: ["#37A2DA", "#67E0E3", "#9FE6B8"],
      grid: {
        containLabel: true
      },
      tooltip: {
        show: true,
        trigger: 'axis',
        formatter: function (params) {
          return params[0].axisValueLabel + "数量:" + params[0].data;
        }
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: xAxisData,
        // show: false
      },
      yAxis: {
        x: 'center',
        type: 'value',
        splitLine: {
          lineStyle: {
            type: 'dashed'
          }
        }
        // show: false
      },
      series: [{
        name:"数量",
        type: 'line',
        smooth: true,
        data: seriesData
      }]
    };

    chart.setOption(option);
  },
  turnPage: function (e) {
    this.setData({
      currentPage: e.currentTarget.dataset.index
    })
  },
  previewImage: function (e) {
    var current = e.target.dataset.src;
    wx.previewImage({
      current: current,
      urls: [current]
    })
  },
})