<template>
  <div class="detail rollbar">
    <form>
      <div class="header-container">
        <div class="header-left container">
          <div>
            产品ID: <input type="text" v-model="settings.instId" disabled/>
          </div>
          <!--  遍历 settings.bars  处理分析周期  -->
          <div>
            <p>分析周期:</p>
            <div class="bars-container">
              <div v-for="(val) in bars" :key="val.code" class="bar-item">
                <input type="checkbox"
                       v-model="settings.bars"
                       v-bind:value="val.code"
                       v-bind:checked="settings.bars?.includes(val.code)"/>
                {{ val.name }}
              </div>
            </div>
          </div>

          <div>
            目标权重: <input type="number" v-model="settings.targetWeight"/>
          </div>
        </div>
        <!-- 下单配置  -->
        <div class="header-right container" v-if="placeOrder()">
          <h2>下单配置</h2>
          <div>
            <label>自动下单: <span class="required">*</span></label>
            <input name="enablePlaceOrder" type="radio"
                   v-if="placeOrder()"
                   v-model="placeOrder().enablePlaceOrder"
                   :value="false"
            />
            <span>否</span>

            <input name="enablePlaceOrder" type="radio"
                   v-if="placeOrder()"
                   v-model="placeOrder().enablePlaceOrder"
                   :value="true"
            />
            <span>是</span>
          </div>

          <div style="display: flex">
            <div style="display: flex">
              <label>下单金额: <span class="required">*</span></label>
              <input name="cash" type="number"
                     v-if="placeOrder()" v-model="placeOrder().cash"/>
            </div>
            <div style="display: flex">
              <label>杠杆倍数: <span class="required">*</span></label>
              <input disabled name="lever" type="number" v-model="placeOrder().lever"/>
<!--              <span>(当前账户配置: 全仓: x100, 逐仓 多x20  空x10)</span>-->
              <el-button type="warning">同步</el-button>
            </div>
          </div>
          <hr/>
          <div>
            <h2>下单限制</h2>
            <div>
              <label>允许开仓方向: <span class="required">*</span></label>
              <input name="sides" type="checkbox"
                     v-model="placeOrder().limit.sides"
                     value="long"
                     v-bind:checked="'long' in placeOrder().limit.sides"/>多仓
              <input name="sides" type="checkbox"
                     v-model="placeOrder().limit.sides"
                     value="short"
                     v-bind:checked="'short' in placeOrder().limit.sides"/>空仓
            </div>
            <div>
              <div>
                <label>看涨最高价: </label>
                <input name="maxPrice" type="number" v-model="placeOrder().limit.maxPrice"/>
              </div>
              <div>
                <label>看跌最低价: </label>
                <input name="minPrice" type="number" v-model="placeOrder().limit.minPrice"/>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="indicators-container">
        <p>计算指标:</p>
        <div class="indicators-items">
          <template v-for="(val) in indicators">
            <div>
              <input name="calculateSettingsFaceMap" type="checkbox"
                     @change="updateCalculateSettingsFaceMap(val.code, $event.target.checked)"
                     v-bind:value="val.code"
                     v-bind:checked="val.code in settings.calculateSettingsFaceMap"
                     v-bind:disabled="val.name === 'BOLL'"
              />
              <label>
                <!-- 判断如果 val.name === BOLL, 字体打下划线 -->
                <template v-if="val.name === 'BOLL'">
                  <span class="delete">{{ val.name }}</span>
                </template>
                <template v-else>
                  {{ val.name }}
                </template>
              </label>
            </div>
          </template>
        </div>
        <div class="indicators-detail">
          <!-- MACD -->
          <div style="flex: 1" v-show="isShow('MACD')">
            <h2>MACD</h2>
            <div ref="MACD" id="MACD" class="settings">
              <template v-for="(bar,index) in (bars)">
                <template v-if="settings.bars.includes(bar.code)">
                  <div ref="'macd-'+ {{bar.code}}" class="settings-item" :key="'macd_'+index">
                    <select disabled>
                      <option :value="bar.code">
                        {{ bar.name }}（分析周期）<p/>
                      </option>
                    </select>
                    <!-- 参数输入区域 -->
                    <div class="parameters">
                      <div class="parameter-row">
                        <label>权重: <span class="required">*</span></label>
                        <input type="number" v-model="macd(bar).weights" min="1"/>
                      </div>
                      <div class="parameter-row">
                        <label>短周期: <span class="required">*</span></label>
                        <input type="number" v-model="macd(bar).shortPeriod" min="1"/>
                      </div>
                      <div class="parameter-row">
                        <label>长周期: <span class="required">*</span></label>
                        <input type="number" v-model="macd(bar).longPeriod" min="1"/>
                      </div>
                      <div class="parameter-row">
                        <label>信号周期: <span class="required">*</span></label>
                        <input type="number" v-model="macd(bar).signalPeriod" min="1"/>
                      </div>
                    </div>

                    <!-- 策略1 -->
                    <div id="macd-strategy1" class="strategy">
                      <h2>策略1</h2>
                      <div class="parameter-row">
                        <label>是否金叉死叉: <span class="required">*</span></label>
                        <input name="goldenCrossLine" type="radio"
                               v-model="macd(bar).goldenCrossLine"
                               :value="false"
                               @change="updateGoldenCrossLine(false,bar)"
                        />否
                        <input type="radio"
                               v-model="macd(bar).goldenCrossLine"
                               :value="true"
                               @change="updateGoldenCrossLine(true,bar)"
                        />是
                      </div>
                    </div>

                    <!-- 策略2 -->
                    <div ref="macd-strategy2" class="strategy" v-show="!macd(bar).goldenCrossLine">
                      <h2>策略2</h2>
                      <div class="parameter-row">
                        <label>末位计算周期: <span class="required">*</span></label>
                        <input name="lastPeriod" type="number" v-model="macd(bar).lastPeriod" min="1"/>
                      </div>
                      <div class="parameter-row">
                        <label>最小连续次数: <span class="required">*</span></label>
                        <input name="minContinuityCount" type="number" v-model="macd(bar).minContinuityCount" min="1"/>
                      </div>
                      <div class="parameter-row">
                        <label>最大连续次数: <span class="required">*</span></label>
                        <input name="maxContinuityCount" type="number" v-model="macd(bar).maxContinuityCount" min="1"/>
                      </div>
                    </div>
                  </div>
                </template>
              </template>
            </div>
          </div>
          <!-- RSI -->
          <div style="flex: 1" v-show="isShow('RSI')">
            <h2>RSI</h2>
            <div ref="RSI" id="RSI" class="settings">
              <template v-for="(bar,index) in (bars)">
                <template v-if="settings.bars.includes(bar.code)">
                  <div ref="'macd-'+ {{bar.code}}" class="settings-item" :key="'rsi_'+index">
                    <select disabled>
                      <option :value="bar.code"> {{ bar.name }}（分析周期）</option>
                    </select>
                    <!-- 参数输入区域 -->
                    <div class="parameters">
                      <div class="parameter-row">
                        <label>周期: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('RSI',bar).period" min="1"/>
                      </div>
                      <div class="parameter-row">
                        <label>权重: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('RSI',bar).weights" min="1"/>
                      </div>
                    </div>

                    <!-- 策略 -->
                    <div class="strategy">
                      <h2>策略</h2>
                      <div class="parameter-row">
                        <label>超买界限: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('RSI',bar).overbought" min="1"/>
                      </div>
                      <div class="parameter-row">
                        <label>超卖界限: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('RSI',bar).oversold" min="1"/>
                      </div>
                    </div>

                  </div>
                </template>
              </template>
            </div>
          </div>
          <!-- KDJ -->
          <div style="flex: 1" v-show="isShow('KDJ')">
            <h2>KDJ</h2>
            <div ref="KDJ" id="KDJ" class="settings">
              <template v-for="(bar,index) in (bars)">
                <template v-if="settings.bars.includes(bar.code)">
                  <div ref="'kdj-'+ {{bar.code}}" class="settings-item" :key="'kdj_'+index">
                    <select disabled>
                      <option :value="bar.code"> {{ bar.name }}（分析周期）</option>
                    </select>
                    <!-- 参数输入区域 -->
                    <div class="parameters">
                      <div class="parameter-row">
                        <label>周期: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('KDJ',bar).period" min="1"/>
                      </div>
                      <div class="parameter-row">
                        <label>权重: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('KDJ',bar).weights" min="1"/>
                      </div>
                      <div class="parameter-row">
                        <label>平滑系数: <span class="required">*</span></label>
                        <input type="text" v-model="getIndex('KDJ',bar).alpha"/>
                      </div>
                    </div>
                    <!-- 策略 -->
                    <div class="strategy">

                      <!-- 新增 jlineStrategy.enable 参数 -->
                      <h3>J线策略:</h3>
                      <div class="parameter-row">
                        <label>J线策略启用: <span class="required">*</span></label>
                        <input type="checkbox" v-model="getIndex('KDJ',bar).jlineStrategy.enable"/>
                      </div>
                      <!-- 新增 jlineStrategy.overbought 参数 -->
                      <div class="parameter-row">
                        <label>超买阈值: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('KDJ',bar).jlineStrategy.overbought"/>
                      </div>
                      <!-- 新增 jlineStrategy.oversold 参数 -->
                      <div class="parameter-row">
                        <label>超卖阈值: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('KDJ',bar).jlineStrategy.oversold"/>
                      </div>

                      <h3>偏差策略: </h3>
                      <!-- 新增 deviationStrategy.enable 参数 -->
                      <div class="parameter-row">
                        <label>偏差策略启用: <span class="required">*</span></label>
                        <input type="checkbox" v-model="getIndex('KDJ',bar).deviationStrategy.enable"/>
                      </div>
                      <!-- 新增 deviationStrategy.deviationThreshold 参数 -->
                      <div class="parameter-row">
                        <label>偏差阈值: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('KDJ',bar).deviationStrategy.deviationThreshold"/>
                      </div>
                      <!-- 新增 goldenCrossStrategy.enable 参数 -->
                      <h3>金叉策略: </h3>
                      <div class="parameter-row">
                        <label>金叉策略启用: <span class="required">*</span></label>
                        <input type="checkbox" v-model="getIndex('KDJ',bar).goldenCrossStrategy.enable"/>
                      </div>
                      <!-- 新增 goldenCrossStrategy.goldenCrossThreshold 参数 -->
                      <div class="parameter-row">
                        <label>金叉阈值: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('KDJ',bar).goldenCrossStrategy.goldenCrossThreshold"/>
                      </div>
                      <!-- 新增 goldenCrossStrategy.deathCrossThreshold 参数 -->
                      <div class="parameter-row">
                        <label>死叉阈值: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('KDJ',bar).goldenCrossStrategy.deathCrossThreshold"/>
                      </div>
                    </div>
                  </div>
                </template>
              </template>
            </div>
          </div>
          <!-- K line -->
          <div style="flex: 1" v-if="isShow('K line')">
            <h2>K line</h2>
            <div ref="kLine" id="kLine" class="settings">
              <template v-for="(bar,index) in (bars)">
                <template v-if="settings.bars.includes(bar.code)">
                  <div ref="'kLine-'+ {{bar.code}}" class="settings-item" :key="'kLine_'+index">
                    <select disabled>
                      <option :value="bar.code"> {{ bar.name }}（分析周期）</option>
                    </select>
                    <!-- 参数输入区域 -->
                    <div class="parameters">
                      <div class="parameter-row">
                        <label>周期: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('K line',bar).period" min="1"/>
                      </div>
                      <div class="parameter-row">
                        <label>权重: <span class="required">*</span></label>
                        <input type="number" v-model="getIndex('K line',bar).weights" min="1"/>
                      </div>
                    </div>
                    <!-- 策略 -->
                    <div class="strategy">
                      <h2>策略1: 抄底策略</h2>
                      <div>
                        <div class="parameter-row">
                          <label>时间区间: <span class="required">*</span></label>
                          <input type="number" v-model="getIndex('K line',bar).lossStrategy.timePeriod" min="1"/> 毫秒
                        </div>
                        <div class="parameter-row">
                          <label>最小变更幅度: <span class="required">*</span></label>
                          <input type="number" v-model="getIndex('K line',bar).lossStrategy.minDiffRate"/>
                        </div>
                        <div class="parameter-row">
                          <label>回弹幅度: <span class="required">*</span></label>
                          <input type="number" v-model="getIndex('K line',bar).lossStrategy.reboundRate"/>
                        </div>
                      </div>
                    </div>
                  </div>
                </template>
              </template>
            </div>
          </div>
        </div>
      </div>

      <div class="container-tail" v-show="canUpdate">
        <button class="test-red" @click="save">保存</button>
        <button class="test-red" @click="applyAll">应用全部配置</button>
        <button class="test-red">取消</button>
      </div>
    </form>
  </div>
</template>

<script>
import store from "@/store";

export default {
  name: "SettingDetail",
  data() {
    return {
      defaultSettings: null,
    }
  },
  // props: ["settings", "save", "applyAll", "canUpdate",],
  props: {
    "settings": {required: true, type: Object},
    "canUpdate": {required: false, type: Boolean, defaultValue: true},
    "save": {required: false, type: Function},
    "applyAll": {required: false, type: Function},
  },
  methods: {

    updateGoldenCrossLine(check, bar) {
      this.macd(bar).goldenCrossLine = check;
    },

    updateCalculateSettingsFaceMap(key, check) {
      if (check) {
        if (!(key in this.settings.calculateSettingsFaceMap)) {
          this.$set(this.settings.calculateSettingsFaceMap, key, {
            enable: true,
            indicatorType: key,
            barSettingsMap: {}
          });
        } else {
          this.settings.calculateSettingsFaceMap[key].enable = true;
        }
      } else {
        this.settings.calculateSettingsFaceMap[key].enable = false;
      }
    },

    isShow(key) {
      if (!(key in this.settings.calculateSettingsFaceMap)) {
        return false
      }
      return this.settings.calculateSettingsFaceMap[key].enable;
    },

    macd(bar) {
      return this.getIndex("MACD", bar);
    },

    // 获取下单配置
    placeOrder() {
      return this.settings.placeOrderSettings;
    },

    getIndex(key, bar) {
      let val = this.settings.calculateSettingsFaceMap[key];
      // 判断 val 是否存在

      if (!val) {
        this.$set(this.settings.calculateSettingsFaceMap, key, {
          enable: true,
          indicatorType: key,
          barSettingsMap: {}
        });
        val = this.settings.calculateSettingsFaceMap[key];
      }
      if (key === 'KDJ') {
        console.log("KDJ配置1: ", val, "--- ", bar.code);
      }
      if (!val.barSettingsMap[bar.code]) {
        this.$set(val.barSettingsMap, bar.code, this.getOrDefault(key));
      }
      if (key === 'KDJ') {
        console.log("val", val)
        console.log("KDJ配置2: ", val.barSettingsMap[bar.code])
      }
      return val.barSettingsMap[bar.code];
    },

    getOrDefault(key) {
      let val = this.defaultSettings.calculateSettingsFaceMap[key];
      return val.barSettingsMap["1H"];
    }

  },

  computed: {
    bars() {
      return store.getters.bars;
    },
    indicators() {
      return store.getters.indicators;
    },
    defaultBar() {
      return {
        "code": "1H",
        "name": "1时",
        "enable": true
      };
    },
  },

  mounted() {
    this.$http.settings.getDefaultSettings().then(res => {
      if (res.code === 0) {
        this.defaultSettings = res.data;
      } else {
        this.$message.error(res.msg);
      }
    })
  }

}
</script>

<style scoped lang="less">
@import '../css/settings';
</style>