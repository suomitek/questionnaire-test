import * as echarts from '../../../../../ec-canvas/echarts';
var redis = require('../../../../../libs/redis.js');
let chart = null;



Page({
  data: {
    ec: {
      lazyLoad: true
      // onInit: initChart
    }
  },
  onLoad(){
    this.ecComponent = this.selectComponent('#mychart-dom-pie');//完成进度图
    var problem = redis.get("problem")
    redis.remove("problem")
    var name = problem.name;
    var data = [];
    for (var i = 0; i < problem.choices.length; i++) {
      data.push({
        value: problem.choices[i].selectNum,
        name:"选项:" + (i + 1)
      })
    }
    this.setData({
      name: name,
      data: data,
      problem: problem
    })

    this.init();

  },
  init() {
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
  },
  setOption(chart) {
    var name = this.data.name;
    var data = this.data.data;
    var option = {
      title: {
        text: name+"饼图",
        left: 'center'
      },
      backgroundColor: "#ffffff",
      color: ["#37A2DA", "#32C5E9", "#67E0E3", "#91F2DE", "#FFDB5C", "#FF9F7F", "#0cfbf3", "#17e679", "#f7ab17", "#4612db"],
      tooltip: {
        show: true,
      },
      series: [{
        label: {
          normal: {
            fontSize: 14
          }
        },
        type: 'pie',
        center: ['50%', '50%'],
        radius: [0, '60%'],
        data: data,
        itemStyle: {
          emphasis: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 2, 2, 0.3)'
          }
        }
      }]
    };

    chart.setOption(option);
  }
});
