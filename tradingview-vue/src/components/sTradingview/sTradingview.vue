<template>
  <div class="sTradingviewContent">
    <van-tabs
        class="tabsWrap"
        title-active-color="#6394f0"
        title-inactive-color="#7a90ad"
        color="#6394f0"
        background="#2a3041"
        v-model="interval"
        @change="changeTabs"
    >
      <van-tab
          v-for="(item, index) in tabsArr"
          :title="item.label"
          :name="item.resolution"
          :key="index"
      />
    </van-tabs>
    <div :id="domId" class="sTradingviewView"></div>
  </div>
</template>
<script>
import {widget} from "./static/charting_library.min.js";
import datafeeds from "./static/datafees.js";
import config from "./config";
import tabsConfig from "./config/tabs";
import {webSocketMixin} from "./mixin/webSocket";

export default {
  name: "sTradingview",
  mixins: [webSocketMixin],
  props: {
    //Dom元素id
    domId: {
      type: String,
      default: function () {
        return "tradeView";
      },
    },
    //请求地址
    wsUrl: {
      type: String,
      default: function () {
        return "";
      },
    },
    //参数
    marketName: {
      type: Object,
      required: true,
    },
    // 请求id
    marketId: {
      type: String | Number,
      default: function () {
        return "";
      },
    },
    // 小数值
    decimal: {
      type: String | Number,
      default: function () {
        return 100;
      },
    },
    //是否显示MACD
    is_MACD: {
      type: Boolean,
      default: function () {
        return false;
      },
    },
  },
  data() {
    return {
      tabsArr: tabsConfig,
      symbol: "LTC_USDT", //币名称
      interval: "1", //默认显示规则
      chart: null,
      initdata: {},
      countDate: 0, //累加条数
      startData: 0, //起始条数
      lengsData: 200, //结束长度
      datafeeds: new datafeeds(this),
      onLoadedCallback: null, //初始数据回调
      onRealtimeCallback: null, //websocket数据回调
    };
  },

  watch: {
    // 监听 marketName 属性的变化
    marketName: {
      deep: true, // 深度监听
      handler(newVal) {
        console.log("参数发送了变化")
      }
    },
  },
  mounted() {
    //加载K线图
    this.loadChart();
  },
  methods: {
    /**
     * 切换触发
     * e {string} reset=重置数据
     */
    changeTabs(e) {
      let self = this;
      this.interval = e;
      let chartType = e == "1s" ? 3 : 1;
      this.chart.activeChart().setChartType(chartType);
      this.setSymbols();
      this.chart.activeChart().resetData();
      this.webSocket("load");
      //MA显示隐藏
      this.toggleStudies(e);
    },

    //过滤 时段
    filter(time) {
      return time == "1s" ? "1S" : time;
    },

    // 请求数据
    getBars(
        symbolInfo,
        resolution,
        rangeStartDate,
        rangeEndDate,
        onLoadedCallback
    ) {
      this.onLoadedCallback = onLoadedCallback;
      // this.webSocket("load");
    },

    //socket
    subscribeBars(
        symbolInfo,
        resolution,
        onRealtimeCallback,
        subscriberUID,
        onResetCacheNeededCallback
    ) {
      this.onRealtimeCallback = onRealtimeCallback;
      // this.webSocket("get");
    },

    //获取配置信息
    getSymbol(symbol) {
      return {
        name: symbol,
        full_name: symbol,
        timezone: "Asia/Shanghai", //默认时区
        minmov: 1,
        minmov2: 0,
        pointvalue: 1,
        fractional: false,
        //设置周期 等于所有时间都是交易时段
        session: "24x7",
        has_intraday: true, //布尔值显示商品是否具有日内（分钟）历史数据
        has_no_volume: false,
        //设置是否支持周月线
        has_daily: true,
        //设置是否支持周月线
        // has_weekly_and_monthly: true,
        description: symbol,
        //设置精度  100表示保留两位小数   1000三位   10000四位
        pricescale: parseInt(this.decimal),
        ticker: symbol,
        supported_resolutions: ["1", "5", "15", "30", "60", "1D", "1W", "1M"],
        // seconds_multipliers: ["1S", "5S", "15S"],
        // volume_precision: 0, //整数显示此商品的成交量数字的小数位。0表示只显示整数。1表示保留小数位的1个数字字符
        // data_status: "streaming", //数据状态(streaming(实时),endofday(已收盘),pulsed(脉冲),delayed_streaming(延迟流动中))
      };
    },

    //设置品信息(重新获取初始数据/推送数据)
    setSymbols() {
      let self = this;
      self.chart.setSymbol(self.symbol, self.filter(self.interval), function (
          e
      ) {
        self.chart.chart().setVisibleRange(self.initdata);
        self.chart.chart().executeActionById("timeScaleReset");
      });

      this.chart
          .chart()
          .setResolution(
              self.filter(self.interval),
              function onReadyCallback() {
              }
          );
    },

    //卸载K线
    removeChart() {
      if (this.chart) {
        this.chart.remove();
        this.chart = null;
      }
    },
    //加载K线图插件
    loadChart() {
      let self = this;
      this.webSocket("load");
      this.chart = new widget({
        container_id: self.domId, //`id`属性为指定要包含widget的DOM元素id。
        symbol: self.symbol,
        interval: self.filter(self.interval),
        locale: "zh", //  语言
        autosize: true,
        fullscreen: false, //是否占用视图所有空间
        preset: "mobile",
        toolbar_bg: "#1e2235", //背景色
        datafeed: this.datafeeds,
        timezone: "Asia/Shanghai", //默认时区
        library_path: "/static/charting_library/", //默认脚本核心文件存储位置
        indicators_file_name: "custom-study(MACD红绿).js",
        drawings_access: {
          type: "black",
          tools: [{name: "Regression Trend"}],
        },
        //配置项
        ...config,
      });

      this.chart.onChartReady(function () {
        //检查是否存在MA
        self.toggleStudies(self.interval);
      });
    },
    /**
     * 根据状态 显示隐藏 MA
     * e {string} 时段
     */
    toggleStudies(e) {
      let self = this;
      if (e == "1s") {
        self.chart
            .activeChart()
            .getAllStudies()
            .forEach((e) => {
              if (e.name == "Moving Average") {
                self.chart.activeChart().removeEntity(e.id);
              }
            });
      } else {
        //检查是否存在MA
        this.getAllStudiesFun();
      }
    },
    //检查是否有 指标MA
    getAllStudiesFun() {
      let self = this;
      let strArr = [];
      self.chart
          .activeChart()
          .getAllStudies()
          .forEach((e) => {
            // console.log(e);
            strArr.push(e.name);
          });
      if (JSON.stringify(strArr).indexOf("Moving Average") == -1) {
        //创建指标
        self.createStudyFun();
      }
    },
    //创建显示指标
    createStudyFun() {
      let self = this;
      //设置均线种类 均线样式
      // self.chart.chart().createStudy('Moving Average', false, false, [5], null, {'Plot.color': 'rgb(150, 95, 196)'});
      // self.chart.chart().createStudy('Moving Average', false, false, [10], null, {'Plot.color': 'rgb(116,149,187)'});
      // self.chart.chart().createStudy('Moving Average', false, false, [20],null,{"plot.color": "rgb(58,113,74)"});
      // self.chart.chart().createStudy('Moving Average', false, false, [30],null,{"plot.color": "rgb(118,32,99)"});
      try {
        self.chart.chart().createStudy("Moving Average", !1, !1, [7], null, {});
        self.chart
            .chart()
            .createStudy("Moving Average", !1, !1, [10], null, {});
        self.chart
            .chart()
            .createStudy("Moving Average", !1, !1, [30], null, {});

        if (self.is_MACD) {
          self.chart.chart().createStudy("MACD", !1, !1, [20], null, {}); //MACD
          self.chart
              .chart()
              .createStudy(
                  "指数平滑异同移动平均线(MACD_Custom)",
                  false,
                  false,
                  [20],
                  null,
                  {}
              ); //自定义MACD
        }
      } catch (e) {
      }
    },
    //销毁之前
    beforeDestroy() {
      this.removeChart();
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep.van-tabs--line .van-tabs__wrap {
  height: 100%;
}

.sTradingviewContent {
  height: 100%;
  width: 100%;

  .tabsWrap {
    height: 88px;
  }

  .sTradingviewView {
    height: calc(100% - 88px);
  }
}
</style>
