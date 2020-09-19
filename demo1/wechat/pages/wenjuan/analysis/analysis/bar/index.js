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
    this.ecComponent = this.selectComponent('#mychart-dom-bar');//完成进度图
    var problem = redis.get("problem")
    redis.remove("problem")
    var name = problem.name;
    var series = [];
    var yAxis = [];
    for (var i = 0; i < problem.choices.length; i++) {
      yAxis.push("选项:" + (i + 1))
      series.push(problem.choices[i].selectNum)
    }
    this.setData({
      name: name,
      series: series,
      yAxis: yAxis,
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
    var series = this.data.series;
    var yAxis = this.data.yAxis;
    var option = {
      title: {
        text: name + "柱状图",
        left: 'center'
      },
      color: ['#37a2da'],
      tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
          type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        },
        confine: true
      },
      grid: {
        left: 20,
        right: 20,
        bottom: 15,
        top: 40,
        containLabel: true
      },
      xAxis: [
        {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#999'
            }
          },
          axisLabel: {
            color: '#666'
          }
        }
      ],
      yAxis: [
        {
          type: 'category',
          axisTick: { show: false },
          data: yAxis,
          axisLine: {
            lineStyle: {
              color: '#999'
            }
          },
          axisLabel: {
            color: '#666'
          }
        }
      ],
      series: [
        {
          name: '数量',
          type: 'bar',
          label: {
            normal: {
              show: true,
              position: 'inside'
            }
          },
          data: series,
          itemStyle: {
            // emphasis: {
            //   color: '#37a2da'
            // }
          }
        }
      ]
    };
    chart.setOption(option);
  }
});
