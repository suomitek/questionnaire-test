// pages/wenjuan/analysis/analysis/wenben/index.js
var urls = require('../../../../../utils/urls.js');
var util = require('../../../../../utils/util.js');
var dialog = require('../../../../../libs/dialog/dialog.js');
var redis = require('../../../../../libs/redis.js');
Page({

  /**
   * 页面的初始数据
   */
  data: {
    host:urls.host,
    numList: [0,1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25],
    dafen:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var tihao = options.tihao;
    var name = options.name;
    var index = options.index;
    var problemId = options.problemId;
    var examineFlag = options.examineFlag;
    var qType = options.qType;
    var that = this;
    var url = urls.urlobj.listAnswerVo;
    util.getServerData(url, {
      problemId: problemId
    }, null).then(function (res) {
      var answerVo = res.data.data;
      var dafen=that.data.dafen;
      for (var x = 0; x < answerVo.length; x++) {
        dafen.push({
          aid: answerVo[x].id,
          obtain: answerVo[x].obtain,
        })
        answerVo[x]['hide'] = true
        answerVo[x]['createTimeStr'] = util.timeToStr(new Date(answerVo[x].createTime))
      }
      if (answerVo.length > 0) {//默认第一个打开
        answerVo[0]['hide'] = false
      }
      that.setData({
        index: index,
        name: name,
        tihao: tihao,
        qType: qType,
        dafen: dafen,
        answerVo: answerVo,
        problemId: problemId,
        examineFlag: examineFlag,
      })
    });
  },
  downloadSaveFile(e){/////////本来是要下载的
    var url = this.data.host + e.currentTarget.dataset.url;
    // util.downloadSaveFile(url);
  },
  pingfen(e) {///////////打分
    var aid = e.target.dataset.aid;//那个人的
    var index = e.target.dataset.index;//这个人的排序号
    var fen = e.target.dataset.fen;//分数
    var dafen = this.data.dafen;
    dafen[index].obtain = fen;
    this.setData({
      dafen: dafen
    })
  },
  dtijiao(){
    var dafen = this.data.dafen;
    var index = this.data.index;
    var problemId = this.data.problemId
    for(var  i=0;i<dafen.length;i++){
      if(dafen[i].obtain==-1){
        dialog.warnModal("错误", "请为第"+(i+1)+"项打分");
        return;
      }
    }
    var url = urls.urlobj.dafen;
    util.getServerData(url, {
      dafen: JSON.stringify(dafen),
      problemId: problemId
    }, null).then(function (res) {
      dialog.warnModalBack("成功", res.data.data.mag, function () {
        redis.put("index",index,60)
        wx.navigateBack({
          delta: 1,
        })
      });
    });
  }
})